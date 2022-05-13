var timer;
var actionFlag = false;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('[name=perf_compare_meth_cd]').val(2);
	$('[name=dml_exec_yn]').val('N');
	$('[name=parallel_degree]').val(1);
	$('[name=multiple_exec_cnt]').val(1);
	$('[name=multiple_bind_exec_cnt]').val(1);
	
	// popup location setting
	$('#moduleEditBox').window({
		title : "MODULE <a style='color:lightblue'>like</a> <a href='javascript:;' " +
				"style='float:right; font-size:15px;' class='w20 easyui-linkbutton' " +
				"onclick='closeModuleBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(435),
		left:getWindowLeft(465),
		closable:false
	});
	$('#table_nameEditBox').window({
		title : "TABLE_NAME <a style='color:lightblue'>in</a> <a href='javascript:;' " +
				"style='float:right; font-size:15px;' class='w20 easyui-linkbutton'" +
				" onclick='closeTableNameBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(435),
		left:getWindowLeft(-560),
		closable:false
	});
	
	$('#moduleEditBox').window('close');
	$('#table_nameEditBox').window('close');
	
	$("#topn_cnt").textbox("readonly",true);
	
	// 보조텍스트 세팅
	$('#sqlPerformanceP').combobox("textbox").attr("placeholder",'선택');
	$('#owner_list').combobox('textbox').attr("placeholder",'선택');
	$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
	$('#table_name_list').textbox('textbox').prop('placeholder', 'ORDER, CUSTOMER, PRODUCT');
	
	// SQL점검팩 combobox 클릭시 데이터 불러오기
	$('#sqlPerformanceP').siblings('span').on('click', function(){
		$('#sqlPerformanceP').combobox('clear');
		
		beginDate = encodeURIComponent( $('#perf_check_exec_begin_dt').datebox('getText') );
		endDate = encodeURIComponent( $('#perf_check_exec_end_dt').datebox('getText') );
		
		if ( checkConditionDate() == false) {
			return;
			
		}else {
			$('#sqlPerformanceP').combobox({
				url:"/AnalyzeImpactChangedTable/getSqlPerfPacName?perf_check_exec_begin_dt=" + beginDate
					+ "&perf_check_exec_end_dt=" + endDate
					+ "&database_kinds_cd=" + database_kinds_cd,
				method:"get",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onSelect: function(data) {
					if ( data.sql_auto_perf_check_id == null) {
						$('#sqlPerformanceP').combobox("setValue",'');
						
					} else {
						$('#data_yn').val(data.data_yn);
						$('#oldValue').val(data.sql_auto_perf_check_id);
						
						setFinalInfo(data.sql_auto_perf_check_id);		// 최종 성능비교한 SQL점검팩 정보
					}
				},
				onChange: function(newValue, oldValue){
					if (newValue == ''){
						$('#data_yn').val('I');
						
						onReadonlyCheck('I');
						
						initPerfCheckResult();
						eraseOldData();
					}
					
					$("#refresh").val("N");
					$("#timer_value").textbox({disabled:false});
					$('#chkRefresh').switchbutton('setValue','off');
					$('#chkRefresh').switchbutton("uncheck");
					
					window.clearTimeout(timer);
				},
				onLoadSuccess: function(item) {
					let old_value = $('#oldValue').val();
					if ( old_value == '' ){
						$('#sqlPerformanceP').combobox('textbox').attr('placeholder','선택');
					}else {
						let currentValue = $('#sqlPerformanceP').combobox('getText');
						$('#sqlPerformanceP').combobox("setValue",old_value);
					}
				},
				onLoadError: function() {
					parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
					return false;
				}
			});
			
		}
	});
	
	//소스DB 불러오기
	$("#original_dbid").combobox({
		url:"/AnalyzeImpactChangedTable/getOperationDB?db_operate_type_cd=3&database_kinds_cd="+database_kinds_cd,
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight: 300,
		onChange: function(newValue, oldValue) {
			if ( newValue == '' ) {
				return; 
			}
			
			let item = $.grep($(this).combobox('getData'), function(row) {
				if ( row.original_dbid == newValue ) {
					return true;
					
				} else {
					return false;
				}
			});
			
			if($("#original_dbid").attr('readonly') != 'readonly'){
				loadTableOwner(newValue.original_dbid);		// OWNER list 불러오기
				$("#owner_list").combobox("readonly",false);
				
			}
		},
		onLoadError: function() {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onLoadSuccess: function(items) {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			$("#original_dbid").combobox("textbox").attr("placeholder",'선택');
		}
	});
	
	// 대상 DB 불러오기
	$("#perf_check_target_dbid").combobox({
		url:"/AnalyzeImpactChangedTable/getOperationDB?db_operate_type_cd=2&database_kinds_cd="+database_kinds_cd,
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight: 300,
		onLoadError: function() {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onChange: function(newValue, oldValue) {
			if (newValue == '') return;
			
			let item = $.grep($(this).combobox('getData'), function(row) {
				if (row.original_dbid == newValue) {
					return true;
				} else {
					return false;
				}
			});
			
		},
		onLoadSuccess: function(items) {
			/* modal progress close */
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			$("#perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
		}
	});
	
	let perf_check_range_begin_dt = $('#perf_check_range_begin_dt').datebox('getText');
	let perf_check_range_end_dt = $('#perf_check_range_end_dt').datebox('getText');
	
	// 수집기간 날짜 변경 시 유효성 체크 : 1. 시작 날짜 체크
	$('#perf_check_range_begin_dt').datebox({
		onChange:function(newValue,oldValue){
			if( strToDate(newValue) > strToDate(perf_check_range_end_dt) ){
				// 선택 한 날짜가 end_dt보다 과거일 경우 이전 선택 날짜로 세팅
				$('#perf_check_range_begin_dt').datebox('setValue', oldValue );
				perf_check_range_begin_dt = oldValue;
				
			}else {
				perf_check_range_begin_dt = newValue;
			}
		}
	});
	
	// 수집기간 날짜 변경 시 유효성 체크 : 2. 끝 날짜 체크
	$('#perf_check_range_end_dt').datebox({
		onChange:function(newValue,oldValue){
			if( strToDate(newValue) < strToDate(perf_check_range_begin_dt) ){
				// 선택 한 날짜가 begin_dt보다 과거일 경우 이전 선택 날짜로 세팅
				$('#perf_check_range_end_dt').datebox('setValue', oldValue );
				perf_check_range_end_dt = oldValue;
				
			}else {
				perf_check_range_end_dt = newValue;
			}
		}
	});
	
	$("#all_sql_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("[name=all_sql_yn]").val("Y");
				$("#topn_cnt").textbox("setValue","");
				$("#topn_cnt").textbox("readonly",true);
			} else {
				$("[name=all_sql_yn]").val("N");
				$("#topn_cnt").textbox("readonly",false);
				$(".topn_cnt .textbox-prompt").removeClass("textbox-prompt");
				$(".topn_cnt .validatebox-readonly").removeClass("validatebox-readonly");
			}
		}
	});
	
	$("#literal_except_yn").checkbox({
		value:"Y",
		checked:true,
		onChange: function( checked ) {
			if ( checked ) {
				$("#literal_except_yn").checkbox('setValue', 'Y');
			} else {
				$("#literal_except_yn").checkbox('setValue', 'N');
			}
		}
	});
	
	$("#time_limt_wrapper .textbox").keyup(function() {
		if ( !floatCheck( $("#time_limt_wrapper .textbox-text").val() ) ) {
			parent.$.messager.alert('경고','SQL Time Limit는 소수점을 입력할 수 없습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
		
		if ( $("#sql_time_limt_cd").combobox("getText") != "무제한" && $("#sql_time_limt_cd").combobox("getText") != "" && /^(\-|\+)?([0-9]+)$/.test( $("#sql_time_limt_cd").combobox("getText") ) == false ) {
			parent.$.messager.alert('경고','SQL Time Limit는 정수만 입력할 수 있습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
	});
	
	$('.table_name_list .searchbox-button').click( function() {
		$("#table_nameEdit").val( $("#table_name_list").val() );
		
		$('#table_nameEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(465)
		});
		$("#table_nameEditBox").window("open");
	});
	
	$('.module_list .searchbox-button').click( function() {
		$("#moduleEdit").val( $("#module_list").val() );
		
		$('#moduleEditBox').window("move",{
			top:getWindowTop(435),
			left:getWindowLeft(-560)
		});
		$("#moduleEditBox").window("open");
	});
	
	$("#table_name_list").keyup(function() {
		$("#table_nameEdit").val( $("#table_name_list").textbox("getValue") );
	});
	$("#module_list").keyup(function() {
		$("#moduleEdit").val( $("#module_list").textbox("getValue") );
	});
	
	$("#sql_time_limt_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id="+encodeURIComponent(commonCode),
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess:function( item ) {
			$("#sql_time_limt_cd").combobox("setValue",item[0].cd);
		},
		onChange:function( newValue, oldValue ) {
			if ( newValue == "99" ) {
				$("#sql_time_limt_cd").combobox("setValue","");
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",false);
				
			} else {
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",true);
			}
			
		}
	});
	
	// 수행결과 초기화
	initPerfCheckResult();
	
	// 수행결과 -> 새로고침 슬라이드 버튼 생성
	$('#chkRefresh').switchbutton({
		value: "off",
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked && $("#refresh").val() == "N" ) {
					$("#refresh").val("Y");
					$('#chkRefresh').switchbutton('setValue','on');
					
					if ( $("#submit_form #sqlPerformanceP" ).textbox("getValue") == '' ) {
						parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					$("#timer_value").textbox({disabled:true});
					
					Btn_RefreshChange();
				} else {
					$("#refresh").val("N");
					$('#chkRefresh').switchbutton('setValue','off');
					$("#timer_value").textbox({disabled:false});
					
					window.clearTimeout(timer);
				}
				
			},300);
		}
	});
	
});

// OWNER 리스트 불러오기
function loadTableOwner(original_dbid){
	let dbid = encodeURIComponent( $('#original_dbid').combobox('getValue') );
	
	$('#owner_list').combobox({
		url:"/AnalyzeImpactChangedTable/getTableOwner?dbid=" + dbid,
		method:"get",
		valueField:'user_nm',
		textField:'user_nm',
		panelHeight: 300,
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
		onLoadSuccess: function(item) {
			$('#owner_list').combobox('textbox').attr('placeholder','선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

// 선택된 SQL 점검팩의 최신 데이터 불러오기
function setFinalInfo(data){
	let submitData = {
			sql_auto_perf_check_id: data,
			database_kinds_cd : database_kinds_cd
	};
	
	ajaxCallWithSimpleData(
			"/AnalyzeImpactChangedTable/getSqlPerfDetailInfo",
			submitData,
			"GET",
			callback_setFinalInfo);
}

// 불러온 최신 데이터 세팅
var callback_setFinalInfo = function(result){
	let data;
	if(result != undefined) {
		data= JSON.parse(result)[0];
	}
	
	if (typeof data == 'undefined') {
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		return;
	}
	
	$('#original_dbid').combobox('setValue', data.original_dbid);
	$('#perf_check_target_dbid').combobox('setValue', data.perf_check_target_dbid);
	
	if ( data.perf_check_range_begin_dt != null && data.perf_check_range_begin_dt != '' ){
		$('#perf_check_range_begin_dt').datebox('setValue', data.perf_check_range_begin_dt);
	}else {
		$('#perf_check_range_begin_dt').datebox('setValue', perfCheckRangeBeginDt);
	}
	if ( data.perf_check_range_end_dt != null && data.perf_check_range_end_dt != '' ){
		$('#perf_check_range_end_dt').datebox('setValue', data.perf_check_range_end_dt);
	}else {
		$('#perf_check_range_end_dt').datebox('setValue', perfCheckRangeEndDt);
	}
	
	if ( data.all_sql_yn != null && data.all_sql_yn != '' ) {
		$('[name=all_sql_yn]').val( data.all_sql_yn );
		
		if ( data.all_sql_yn == 'N' ) {
			$('[name=all_sql_yn]').val('N');
			$('#all_sql_yn').checkbox('uncheck');
		} else {
			$('[name=all_sql_yn]').val('Y');
			$('#all_sql_yn').checkbox('check');
		}
	} else {
		$('[name=all_sql_yn]').val('Y');
		$('#all_sql_yn').checkbox('check');
	}
	
	$('#topn_cnt').textbox('setValue', data.topn_cnt);
	
	if ( data.literal_except_yn != null && data.literal_except_yn != "" ) {
		$("#literal_except_yn").checkbox( 'setValue', data.literal_except_yn );
		
		if ( data.literal_except_yn == "N" ) {
			$("#literal_except_yn").checkbox("uncheck");
		} else {
			$("#literal_except_yn").checkbox("check");
		}
	} else {
		$("#literal_except_yn").checkbox( 'setValue', "Y" );
		$("#literal_except_yn").checkbox("check");
	}
	
	$("#sql_time_limt_cd").combobox('setValue', data.sql_time_limt_cd);
	
	$("#max_fetch_cnt").numberbox('setValue', data.max_fetch_cnt);
	
	if ( data.sql_time_limt_cd == '99') {
		$("#sql_time_limt_cd").combobox('setValue', data.sql_time_direct_pref_value);
	}
	$('#owner_list').combobox('setValue', data.owner_list);
	$('#module_list').textbox('setValue', data.module_list);
	$('#table_name_list').textbox('setValue', data.table_name_list);
	
	if(data.perf_check_sql_source_type_cd == 2){
		$("#entireSQL").radiobutton({checked: true});
	}else {
		$('#AWR').radiobutton({checked: true});
	}
	
	actionFlag = true;	//sql점검팩 클릭 시 actonFlag true, 수행결과 0건이어도 메세지 안뜸
	Btn_RefreshChange();
	
	onReadonlyCheck($('#data_yn').val());
	
	$('#original_dbid').combobox('textbox').attr('placeholder','선택');
	$('#perf_check_target_dbid').combobox('textbox').attr('placeholder','선택');
}

function commaCheck( array ) {
	if ( array.length > 100) {
		parent.$.messager.alert('경고','값은 100개 까지 입력 가능합니다.','warning');
		return false;
	}
}

function floatCheck( obj ) {
	let fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;
	 
	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

function onReadonlyCheck(data_yn) {
	if ( data_yn == "Y" ) {
		$("#original_dbid").combobox("readonly","true");
		$("#original_dbid").attr("readonly",true);
		$("#perf_check_target_dbid").combobox("readonly","true");
		$("#perf_check_range_begin_dt").datebox("readonly","true");
		$("#perf_check_range_end_dt").datebox("readonly","true");
		$("#all_sql_yn").checkbox("readonly","true");
		$("#topn_cnt").textbox("readonly", true);
		$("#literal_except_yn").checkbox("readonly","true");
		$("#sql_time_limt_cd").combobox("readonly","true");
		$("#owner_list").combobox("readonly","true");
		$("#module_list").textbox("readonly","true");
		$("#table_name_list").textbox("readonly","true");
		$("#moduleEdit").attr("readonly","true");
		$("#table_nameEdit").attr("readonly","true");
		$("#AWR").radiobutton("readonly", true);
		$("#entireSQL").radiobutton("readonly", true);
		$("#max_fetch_cnt").numberbox("readonly", true);
		
	} else {
		$(".textbox").removeClass("textbox-readonly");
		
		$(".textbox-icon-disabled").removeClass("textbox-icon-disabled");
		$(".validatebox-readonly").removeClass("validatebox-readonly");
		$(".checkbox-readonly").removeClass("checkbox-readonly");
		
		$("#original_dbid").attr("readonly",false);
		$("#original_dbid").combobox("readonly",false);
		$("#perf_check_target_dbid").combobox("readonly",false);
		$("#perf_check_range_begin_dt").datebox("readonly",false);
		$("#perf_check_range_end_dt").datebox("readonly",false);
		
		if ( $("#perf_check_range_begin_dt").datebox("getValue") == "" || $("#perf_check_range_begin_dt").datebox("getValue") == null ) {
			$("#perf_check_range_begin_dt").datebox("setValue", perfCheckRangeBeginDt);
		}
		
		if ( $("#perf_check_range_end_dt").datebox("getValue") == "" || $("#perf_check_range_end_dt").datebox("getValue") == null ) {
			$("#perf_check_range_end_dt").datebox("setValue", perfCheckRangeEndDt);
		}
		
		$("#all_sql_yn").checkbox("readonly",false);
		
		$("#topn_cnt").textbox("readonly",false);
		
		if ( $("[name=all_sql_yn]").val() == "N") {
			$("#all_sql_yn").checkbox("uncheck");
		} else {
			$("#all_sql_yn").checkbox("check");
			$("#topn_cnt").textbox("readonly",true);
		}
		
		$("#literal_except_yn").checkbox("readonly",false);
		
		if ( $("#literal_except_yn").val() == "N" ) {
			$("#literal_except_yn").checkbox("uncheck");
		} else {
			$("#literal_except_yn").checkbox("check");
		}

		$("#sql_time_limt_cd").combobox("readonly",false);
		
		$("#owner_list").combobox("readonly",false);
		$("#module_list").textbox("readonly",false);
		$("#table_name_list").textbox("readonly",false);
		$("#moduleEdit").attr("readonly",false);
		$("#table_nameEdit").attr("readonly",false);
		
		$('#owner_list').combobox('textbox').attr("placeholder",'선택');
		$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
		$('#table_name_list').textbox('textbox').prop('placeholder', 'ORDER, CUSTOMER, PRODUCT');
		
		if ( $("#sql_time_limt_cd").combobox("getValue") == "" || $("#sql_time_limt_cd").combobox("getValue") == null ) {
			$("#sql_time_limt_cd").combobox("setValue","1");
		}
		
		$("#AWR").radiobutton("readonly", false);
		$("#entireSQL").radiobutton("readonly", false);
		$("#max_fetch_cnt").numberbox("readonly", false);
		
		if ( $("#max_fetch_cnt").numberbox("getValue") == "" || $("#max_fetch_cnt").numberbox("getValue") == null ) {
			$("#max_fetch_cnt").numberbox("setValue","100000");
		}
	}
}

function checkCondition() {
	if ( $("#sqlPerformanceP" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#perf_check_target_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','목표DB를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#perf_check_range_begin_dt").textbox("getValue") == '' ||
		 $("#perf_check_range_end_dt").textbox("getValue") == ''	) {
		parent.$.messager.alert('경고','수집기간을 입력해 주세요.','warning');
		return false;
	}
	
	if ( $("#perf_check_range_begin_dt").textbox("getValue") >	$("#perf_check_range_end_dt").textbox("getValue") ) {
		parent.$.messager.alert('경고','수집기간을 확인해 주세요.','warning');
		return false;
	}
	
	if ( $("#topn_cnt").textbox("getValue") == '' &&  $("#all_sql_yn").checkbox("options").checked == false ) {
		parent.$.messager.alert('경고','TOP N을 입력해 주세요.','warning');
		return false;
	}
	
	return true;
}

function textCheck() {
	let split = ""; 
	
	let table_owner = $("#owner_list").combobox('getValue');
	if ( table_owner == null || table_owner == '' ) {
		parent.$.messager.alert('경고','TABLE_OWNER 값을 확인 후 다시 성능 영향도 분석을 실행해 주세요.','warning');
		return false;
	}
	
	let table_name_list = $("#table_name_list").searchbox('getValue');
	if ( table_name_list == '' || table_name_list == null ){
		parent.$.messager.alert('경고','TABLE_NAME 값을 확인 후 다시 성능 영향도 분석을 실행해 주세요.','warning');
		return false;
	}
	
	let module_list = $("#module_list").searchbox('getValue').trim().split("\,");
	if ( module_list.length > 100) {
		parent.$.messager.alert('경고','MODULE 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( module_list.length > 1 ) {
			for ( let moduleIdx = 0; moduleIdx < module_list.length; moduleIdx++) {
				if ( split2[moduleIdx].trim() == "" ) {
					parent.$.messager.alert('경고','MODULE 값을 확인 후 다시 성능 영향도 분석을 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	return true;
}

function Btn_startAnalysis() {
	
	if ( !checkCondition() ) {
		return;
	}
	
	if ( $("#sql_time_limt_cd").combobox("getText").trim() == "" ) {
		parent.$.messager.alert('경고','SQL Time Limit를 입력해주세요.','warning');
		$("#sql_time_limt_cd").combobox("setText","");
		return;
	}
	
	if ( !textCheck() ) {
		return;
	}
	let param = "확인";
	let msgStr ="실행 하시겠습니까?";
	actionFlag = false;
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			ajaxCallWithSimpleData(
					"/AnalyzeImpactChangedTable/getTuningTargetCount",
					{sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue')},
					"GET",
					callback_getTuningTargetCountAction);
		}
	});
}

var callback_getTuningTargetCountAction = function( result ) {
	
	if ( result.result ) {
		
		let sqlTimeLimt_cd = $("#sql_time_limt_cd").combobox("getValue");
		
		if ( sqlTimeLimt_cd == "1" ) {
			$("#sql_time_limt_cd").combobox("setValue","01");
			$("#sql_time_direct_pref_value").val( '' );
			
		} else if ( sqlTimeLimt_cd != "01" && sqlTimeLimt_cd != "02" && sqlTimeLimt_cd != "03" && 
				sqlTimeLimt_cd != "04" && sqlTimeLimt_cd != "05" && sqlTimeLimt_cd != "98" ) {
			$("#sql_time_direct_pref_value").val( $("#sql_time_limt_cd").combobox("getText") );

		} else if ( sqlTimeLimt_cd == "98" ) {
			$("#sql_time_direct_pref_value").val( "0" );
			
		} else {
			$("#sql_time_direct_pref_value").val( '' );
		}
		
		let submitData = {
				sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
				database_kinds_cd : database_kinds_cd
		};
		
		ajaxCallWithSimpleData(
				"/AnalyzeImpactChangedTable/countExecuteTms",
				submitData,
				"GET",
				callback_CountExecuteTmsAction);
	} else {
		parent.$.messager.alert('정보','튜닝대상으로 선정되었던 SQL이 존재합니다.<br>튜닝대상 선정 작업이 수행되었을 경우 재실행 할 수 없습니다.','info');
	}
}

var callback_CountExecuteTmsAction = function( result ) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result && result.txtValue == "true" ) {
		
		if (parent.openMessageProgress != undefined) parent.openMessageProgress("대상을 선정중입니다.<br>대상 선정이 완료되면 작업이 자동실행 됩니다."," ");
		// 01. 수행결과 초기화
		initPerfCheckResult();
		
		// 화면  readOnly 처리
		$("#data_yn").val("Y");
		// 화면  readOnly 처리
		onReadonlyCheck('Y');
		
		// 02. SQL 자동성능점검 수행
		ajaxCall("/AnalyzeImpactChangedTable/updateSqlAutoPerformance",
				$("#submit_form"),
				"POST",
				callback_updateSqlAutoPerformanceAction);
		
	} else {
		
		if ( result.result == false && result.txtValue == "true" ) {
			let param = "확인";
			let msgStr ="이전에 수행된 실행 정보가 있습니다. 재수행시 이전 실행 정보가 삭제됩니다.<br>계속 진행하시겠습니까?";
			
			parent.$.messager.confirm( param,msgStr,function(r) {
				if (r) {
					
					if (parent.openMessageProgress != undefined) parent.openMessageProgress("대상을 선정중입니다.<br>대상 선정이 완료되면 작업이 자동실행 됩니다."," ");
					// 01. 수행결과 초기화
					initPerfCheckResult();
					
					// 화면  readOnly 처리
					$("#data_yn").val("Y");
					// 화면  readOnly 처리
					onReadonlyCheck('Y');
					
					// 02. SQL 자동성능점검 수행
					ajaxCall("/AnalyzeImpactChangedTable/updateSqlAutoPerformance",
							$("#submit_form"),
							"POST",
							callback_updateSqlAutoPerformanceAction);
				}
			});
		} else {
			/* modal progress close */
			if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
			
			parent.$.messager.alert('정보','해당 SQL점검팩에 대한 작업을 수행 중입니다.<br>수행결과에서 진행상황을 검색할 수 있습니다.','info');
		}
	}
}

function callback_updateSqlAutoPerformanceAction( result ) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result == false ) {
		parent.$.messager.alert('정보',result.message, 'info');
		
		$("#data_yn").val("N");
		onReadonlyCheck('N');
		
		return false;
		
	} else {
		ajaxCall("/AnalyzeImpactChangedTable/performanceCompareCall",
				$("#submit_form"),
				"POST",
				callback_performanceCompareCallAction);
	}
}

/* SQL-3 : 수행결과 조회 */
function Btn_RefreshChange() {
	let intSec = 0;
	
	Btn_LoadPerformanceCheckCount();
	
	if ( $("#refresh").val() == "Y" ) {
		intSec = strParseInt( $("#timer_value").textbox("getValue"),0 );
		timer = window.setTimeout("Btn_RefreshChange()",(intSec*1000));
	} else {
		window.clearTimeout(timer);
	}
}
function Btn_LoadPerformanceCheckCount() {
	if($("#refresh").val() == 'N'){
		// modal progress open
		if (parent.openMessageProgress != undefined) parent.openMessageProgress("수행결과 조회"," ");
	}
	
	let submitData = {
			sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
			database_kinds_cd : database_kinds_cd
	};
	
	ajaxCallWithSimpleData(
			"/AnalyzeImpactChangedTable/loadPerformanceCheckCount",
			submitData,
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	let data = JSON.parse(rows)[0];
	let submitData = "";

	if ( typeof data == 'undefined' ) {
		// modal progress close
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		if ( $('#data_yn').val() == "Y" && $("#original_dbid").combobox("getValue") != '') {
			if(actionFlag == false){
				//sql점검팩 선택으로 조회시 actionFlag true, 실행버튼 및 새로고침으로 조회시 false
				//점검팩 선택으로 조회된 데이터가 없을 경우, db null로 업데이트 생략 및 경고 메세지 생략, readonly만 해제
				parent.$.messager.alert('정보','수집된 SQL이 0건 입니다.<br> 조건값들을 조정 후 재수행 바랍니다.','info');
			}
			
			submitData = {
					sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
					database_kinds_cd : database_kinds_cd
			};
			
			ajaxCallWithSimpleData(
					"/AnalyzeImpactChangedTable/updateAutoPerfChkIsNull",
					submitData,
					"POST",
					callback_UpdateAutoPerfChkIsNullAction);
		}
		actionFlag = true;
		
		return;
		
	} else {
		if ( data.total_cnt == 0 ) {
			if(actionFlag == false){
				//sql점검팩 선택으로 조회시 actionFlag true, 실행버튼 및 새로고침으로 조회시 false
				//점검팩 선택으로 조회된 데이터가 없을 경우, db null로 업데이트 생략 및 경고 메세지 생략, readonly만 해제
				parent.$.messager.alert('정보','수집된 SQL이 0건 입니다.<br> 조건값들을 조정 후 재수행 바랍니다.','info');
			}
			
			submitData = {
					sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
					database_kinds_cd : database_kinds_cd
			};
			
			ajaxCallWithSimpleData(
					"/AutoPerformanceCompareBetweenDatabase/updateAutoPerfChkIsNull",
					submitData,
					"POST",
					callback_UpdateAutoPerfChkIsNullAction);
			
			actionFlag = true;
			
			return;
		}
		$(".perf_check_result_blue").val(data.total_cnt);
		$(".perf_check_result_green").val(data.completed_cnt);
		$(".perf_check_result_gray").val(data.performing_cnt);
		$(".perf_check_result_orange").val(data.err_cnt);
		
		if ( data.perf_check_force_close_yn == 'N' ) {
			$("#perf_check_result_red").hide();
			
			if ( dateFormatValidationCheck( data.perf_check_error ) ) {
				$("#perf_check_result_violet").val("점검 완료");
				$("#perf_check_result_violet").show();
				$("#refresh").val("N");
				$('#chkRefresh').switchbutton("uncheck");
				window.clearTimeout(timer);
			} else {
				$("#perf_check_result_violet").hide();
			}
		} else {
			$("#perf_check_result_violet").hide();
			
			$(".perf_check_result_red").val("강제완료: " + data.perf_check_force_close_yn);
			$("#perf_check_result_red").show();
			$("#refresh").val("N");
			$('#chkRefresh').switchbutton("uncheck");
			window.clearTimeout(timer);
		}
		actionFlag = false;
	}
	
	// modal progress close
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

var callback_UpdateAutoPerfChkIsNullAction = function( result ) {
	$("#data_yn").val("N");
	
	$("#refresh").val("N");
	$("#timer_value").textbox({disabled:false});
	$('#chkRefresh').switchbutton('setValue','off');
	$('#chkRefresh').switchbutton("uncheck");

	window.clearTimeout(timer);
	
	onReadonlyCheck('N');
}

var callback_performanceCompareCallAction = function () {
	// 수행 결과 조회
	Btn_RefreshChange();
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

// 수행결과 초기화
function initPerfCheckResult() {
	$(".perf_check_result_blue").val("전체: 0");
	$(".perf_check_result_green").val("수행완료: 0");
	$(".perf_check_result_gray").val("수행중: 0");
	$(".perf_check_result_orange").val("오류: 0");
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
}

function Btn_ForceUpdateSqlAutoPerformanceCheck() {
	if ( !checkCondition() ) {
		return;
	}
	
	let submitData = {
			sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
			database_kinds_cd : database_kinds_cd
	};
	
	ajaxCallWithSimpleData(
			"/AnalyzeImpactChangedTable/countExecuteTms",
			submitData,
			"GET",
			callback_ForceCountExecuteTmsAction);
}

var callback_ForceCountExecuteTmsAction = function(result) {
	if ( result.txtValue == 'false' ) {
		
		// 수행중일 경우
		let param = "확인";
		let msgStr = $('#sqlPerformanceP').combobox('getText') + "에서 수행 중인 작업을 강제완료처리 하시겠습니까?";
		
		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("강제완료처리", "강제 완료 처리 중입니다.");
				
				let submitData = {
						sql_auto_perf_check_id: $('#sqlPerformanceP').combobox('getValue'),
						database_kinds_cd : database_kinds_cd
				};
				
				ajaxCallWithSimpleData(
						"/AnalyzeImpactChangedTable/forceUpdateSqlAutoPerformance",
						submitData,
						"POST",
						callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
			}
		});
	} else {
		parent.$.messager.alert('정보','해당 SQL점검팩에서 실행 중인 작업이 없습니다.','info');
	}
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result ) {
		$("#refresh").val("N");
		$("#timer_value").textbox({disabled:false});
		$('#chkRefresh').switchbutton('setValue','off');
		$('#chkRefresh').switchbutton("uncheck");
		
		Btn_RefreshChange();
		
		parent.$.messager.alert('정보','해당 SQL점검팩에 대한 강제 완료 처리가 완료되었습니다.','info');
	} else {
		parent.$.messager.alert('정보','강제완료처리 하지 못했습니다.' , 'info');
	}
	
	Btn_RefreshChange();
}


function closeModuleBox() {
	$("#module_list").textbox( "setValue", $("#moduleEdit").val() );
	$('#moduleEditBox').window("close");
}
function closeTableNameBox() {
	$("#table_name_list").textbox( "setValue", $("#table_nameEdit").val() );
	$('#table_nameEditBox').window("close");
}

function Btn_AuthorityScript() {
	if ( !checkCondition() ) {
		return;
	}
	
	$('#authorityScript_form #db').textbox( 'setValue', $('#perf_check_target_dbid').combobox('getText') );
	$('#authorityScript_form #dbid').val( $('#perf_check_target_dbid').combobox('getValue') );
	
	$('#authorityScript_form #checkTable').checkbox('reset');
	$('#authorityScript_form #checkView').checkbox('reset');
	$('#authorityScript_form #checkSequence').checkbox('reset');
	$('#authorityScript_form #checkFunction').checkbox('reset');
	$('#authorityScript_form #checkProcedure').checkbox('reset');
	$('#authorityScript_form #checkPackage').checkbox('reset');
	
	$('#authorityScript_form #scriptView').val('');
	
	LoadUserName();
	
	$('#authorityScriptPop').window("open");
}
//점검팩 삭제 시 SQL점검팩 클리어
function sqlPerfPacClear( sqlPerfId ) {
	let pastSqlPerfId = $('#sqlPerformanceP').combobox('getValue');
	
	if( sqlPerfId == pastSqlPerfId ){
		eraseOldData();
		
	}else {
		return;
	}
}
function eraseOldData(){
	$('#oldValue').val('');
	$('#sqlPerformanceP').combobox('clear');
	$('#original_dbid').combobox('clear');
	$('#perf_check_target_dbid').combobox('clear');
	
	$('#perf_check_exec_begin_dt').datebox('setValue', beginDate);
	$('#perf_check_exec_end_dt').datebox('setValue', endDate);
	$('#perf_check_range_begin_dt').datebox('setValue', perfCheckRangeBeginDt);
	$('#perf_check_range_end_dt').datebox('setValue', perfCheckRangeEndDt);
	
	$('#oneMonth').radiobutton('check');
	
	$("#sql_time_limt_cd").combobox('setValue', '1');
	$("#max_fetch_cnt").numberbox('setValue', 100000);
	
	$('[name=all_sql_yn]').val('Y');
	$('#all_sql_yn').checkbox('check');
	
	$("#literal_except_yn").checkbox( 'setValue', "Y" );
	$("#literal_except_yn").checkbox("check");
	
	$('#AWR').radiobutton({checked: true});
	
	$('#owner_list').combobox('setValue', '');
	$('#module_list').textbox('setValue', '');
	$('#table_name_list').textbox('setValue', '');
	
	$('#data_yn').val('I');
	
	onReadonlyCheck($('#data_yn').val());
	
	initPerfCheckResult();
	
	$('#sqlPerformanceP').combobox('textbox').attr('placeholder','선택');
	$('#original_dbid').combobox('textbox').attr('placeholder','선택');
	$('#perf_check_target_dbid').combobox('textbox').attr('placeholder','선택');
	$('#owner_list').combobox('textbox').attr("placeholder",'선택');
	$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
	$('#table_name_list').textbox('textbox').prop('placeholder', 'ORDER, CUSTOMER, PRODUCT');
	
	$("#refresh").val("N");
	$("#timer_value").textbox({disabled:false});
	$('#chkRefresh').switchbutton('setValue','off');
	$('#chkRefresh').switchbutton("uncheck");
	
	window.clearTimeout(timer);
}