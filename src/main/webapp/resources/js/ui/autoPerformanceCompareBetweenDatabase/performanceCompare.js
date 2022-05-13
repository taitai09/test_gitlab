var timer;
// 현재 사용안함.
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#ownerEditBox').window({
		title : "OWNER <a style='color:lightblue'>in</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeOwnerBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(435),
		left:getWindowLeft(1475),
		closable:false
	});
	$('#moduleEditBox').window({
		title : "MODULE <a style='color:lightblue'>like</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeModuleBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(435),
		left:getWindowLeft(465),
		closable:false
	});
	$('#table_nameEditBox').window({
		title : "TABLE_NAME <a style='color:lightblue'>in</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeTableNameBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(435),
		left:getWindowLeft(-560),
		closable:false
	});
	
	$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
	$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
	$('#table_name_list').textbox('textbox').prop('placeholder', 'ORDER, CUSTOMER, PRODUCT');
	
	$('#saveSQLPerformance').window("close");
	$('#ownerEditBox').window("close");
	$('#moduleEditBox').window("close");
	$('#table_nameEditBox').window("close");
	
	// 프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			$("#submit_form #data_yn").val("N");
			
			// 수행결과 초기화
			initPerfCheckResult();
			
			// 점검팩 호출
			$("#submit_form #sql_auto_perf_check_id").val("");
			loadSqlPerformanceCombo( data.project_id );
			
			// 원천 DB
			$("#submit_form #original_dbid").combobox({
				url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+data.project_id,
				method:"get",
				valueField:'original_dbid',
				textField:'original_db_name',
				panelHeight: 300,
				onLoadError: function() {
					/* modal progress close */
					if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				},
				onChange: function(newValue, oldValue) {
					if ( newValue == '' ) {
						return; 
					}
					
					var item = $.grep($(this).combobox('getData'), function(row) {
						if ( row.original_dbid == newValue ) {
							return true;
						} else {
							return false;
						}
					});
					
				},
				onLoadSuccess: function(items) {
					/* modal progress close */
					if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
					
					$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
				}
			});	
			
			// 대상 DB
			$("#submit_form #perf_check_target_dbid").combobox({
				url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+data.project_id,
				method:"get",
				valueField:'perf_check_target_dbid',
				textField:'perf_check_target_db_name',
				panelHeight: 300,
				onLoadError: function() {
					/* modal progress close */
					if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				},
				onChange: function(newValue, oldValue) {
					if (newValue == '') return;
					
					var item = $.grep($(this).combobox('getData'), function(row) {
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
					
					$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
				}
			});
			
			onReadonlyCheck();
		},
		onLoadSuccess: function(items) {
			$("#submit_form #project_id").combobox("textbox").attr("placeholder",'선택');
			
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$("#topn_cnt").textbox("readonly",true);
	
	$("#all_sql_yn").val("Y");
	$("#all_sql_yn_chk").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").val("Y");
				$("#topn_cnt").textbox("setValue","");
				$("#topn_cnt").textbox("readonly",true);
			} else {
				$("#all_sql_yn").val("N");
				$("#topn_cnt").textbox("readonly",false);
				$(".topn_cnt .textbox-prompt").removeClass("textbox-prompt");
				$(".topn_cnt .validatebox-readonly").removeClass("validatebox-readonly");
			}
		}
	});
	
	$("#literal_except_yn").val("Y");
	$("#literal_except_yn_chk").checkbox({
		value:"Y",
		checked:true,
		onChange: function( checked ) {
			if ( checked ) {
				$("#literal_except_yn").val("Y");
			} else {
				$("#literal_except_yn").val("N");
			}
		}
	});
	$(".sql_time_limt_cd .textbox").keyup(function() {
		if ( !floatCheck( $(".sql_time_limt_cd .textbox-text").val() ) ) {
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
	
	$('.owner_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}
		
		$("#ownerEdit").val($("#owner_list").val() );
		$('#ownerEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(1475)
		});
		$("#ownerEditBox").window("open");
		
	});
	$('.module_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}
		
		$("#moduleEdit").val( $("#module_list").val() );
		$('#moduleEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(465)
		});
		$("#moduleEditBox").window("open");
	});
	$('.table_name_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}
		
		$("#table_nameEdit").val( $("#table_name_list").val() );
		$('#table_nameEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(-560)
		});
		$("#table_nameEditBox").window("open");
	});
	
	$("#owner_list").keyup(function() {
		$("#ownerEdit").val( $("#owner_list").textbox("getValue") );
	});
	$("#module_list").keyup(function() {
		$("#moduleEdit").val( $("#module_list").textbox("getValue") );
	});
	$("#table_name_list").keyup(function() {
		$("#table_nameEdit").val( $("#table_name_list").textbox("getValue") );
	});
	
	$("#sql_time_limt_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id="+$("#commonCode").val(),
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
	
	initPerfCheckResult();
	
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked && $("#refresh").val() == "N" ) {
					$("#refresh").val("Y");
					
					if ( $('#project_id').combobox("getValue") == "" ) {
						parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
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
					$("#timer_value").textbox({disabled:false});
					
					window.clearTimeout(timer);
				}
				
			},300);
			
		}
	});
	
});

function loadSqlPerformanceCombo( projectId ) {
	// submit_form SQL점검팩 콤보
	$('#submit_form #sqlPerformanceP').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId,
		method:"post",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onSelect: function(item) {
			if ( item.sql_auto_perf_check_id == null) {
				$("#submit_form #sqlPerformanceP").combobox("setValue",'');
			} else {
				$("#submit_form #sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
				$("#submit_form #data_yn").val("N");
				$("#submit_form #data_yn").val(item.data_yn);
			}
			
			ajaxCall("/AutoPerformanceCompareBetweenDatabase/getSqlPerfDetailInfo",
					$("#submit_form"),
					"POST",
					callback_readSqlPerfDetail);
		},
		onShowPanel: function() {
			loadSqlPerformanceCombo( projectId );
			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
		onLoadSuccess: function() {
			$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
			
			let sqlAutoPerfId = $("#submit_form #sql_auto_perf_check_id").val(); 
			
			if ( sqlAutoPerfId != '' && sqlAutoPerfId != null ) {
				$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfId );
			}
			
			$("#all_sql_yn_chk").checkbox("check");
			$("#submit_form #perf_check_range_begin_dt").datebox("setValue", $("#startDate").val() );
			$("#submit_form #perf_check_range_end_dt").datebox("setValue", $("#endDate").val() );
			$("#sql_time_limt_cd").combobox("setValue", "01");
			$("#submit_form #max_fetch_cnt").numberbox("setValue","100000");
			
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
}

function commaCheck( array ) {
	if ( array.length > 100) {
		parent.$.messager.alert('경고','값은 100개 까지 입력 가능합니다.','warning');
		return false;
	}
}

function floatCheck( obj ) {
	var fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;
	 
	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

var callback_readSqlPerfDetail = function(value) {
	var data = JSON.parse(value)[0];
	
	if (typeof data == 'undefined') {
		/* modal progress close */
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		return;
	}
	
	if ( data.perf_check_sql_source_type_cd == 1 ) {
		$('#AWR').radiobutton({checked: true});
	} else {
		$("#entireSQL").radiobutton({checked: true});
	}
	
	$("#submit_form #original_dbid").combobox("setValue", data.original_dbid);
	$("#submit_form #perf_check_target_dbid").combobox("setValue", data.perf_check_target_dbid);
	
	$("#submit_form #perf_check_range_begin_dt").textbox("setValue", data.perf_check_range_begin_dt);
	$("#submit_form #perf_check_range_end_dt").textbox("setValue", data.perf_check_range_end_dt);
	
	if ( data.all_sql_yn != null && data.all_sql_yn != "" ) {
		$("#submit_form #all_sql_yn").val( data.all_sql_yn );
		
		if ( data.all_sql_yn == "N" ) {
			$("#submit_form #all_sql_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #all_sql_yn_chk").checkbox("check");
		}
	} else {
		$("#submit_form #all_sql_yn").val("Y");
		$("#submit_form #all_sql_yn_chk").checkbox("check");
	}
	
	$("#submit_form #topn_cnt").textbox("setValue", data.topn_cnt);
	
	if ( data.literal_except_yn != null && data.literal_except_yn != "" ) {
		$("#submit_form #literal_except_yn").val( data.literal_except_yn );
		
		if ( data.literal_except_yn == "N" ) {
			$("#submit_form #literal_except_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #literal_except_yn_chk").checkbox("check");
		}
	} else {
		$("#submit_form #literal_except_yn").val("Y");
		$("#submit_form #literal_except_yn_chk").checkbox("check");
	}
	
	if ( data.sql_time_limt_cd != null && data.sql_time_limt_cd != '' ) {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", data.sql_time_limt_cd);
	} else {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", "01");
	}
	
	if ( data.sql_time_limt_cd == '99') {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", data.sql_time_direct_pref_value);
	}
	
	if ( data.max_fetch_cnt != '' && data.max_fetch_cnt != null ) {
		$('#max_fetch_cnt').textbox('setValue', data.max_fetch_cnt);
	} else {
		$('#max_fetch_cnt').textbox('setValue', "100000");
	}
	
	$("#submit_form #owner_list").textbox("setValue", data.owner_list);
	$("#submit_form #module_list").textbox("setValue", data.module_list);
	$("#submit_form #table_name_list").textbox("setValue", data.table_name_list);
	
	onReadonlyCheck();
	Btn_RefreshChange();
	
	$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
	$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
}

function onReadonlyCheck() {
	if ( $("#submit_form #data_yn").val() == "Y" ) {
		$("#original_dbid").combobox("readonly","true");
		$("#perf_check_target_dbid").combobox("readonly","true");
		$("#perf_check_range_begin_dt").textbox("readonly","true");
		$("#perf_check_range_end_dt").textbox("readonly","true");
		$("#all_sql_yn_chk").checkbox("readonly","true");
		$("#topn_cnt").textbox("readonly","true");
		$("#literal_except_yn_chk").checkbox("readonly","true");
		$("#sql_time_limt_cd").combobox("readonly","true");
		$("#owner_list").textbox("readonly","true");
		$("#module_list").textbox("readonly","true");
		$("#table_name_list").textbox("readonly","true");
		$("#ownerEdit").attr("readonly","true");
		$("#moduleEdit").attr("readonly","true");
		$("#table_nameEdit").attr("readonly","true");
		$("#AWR").radiobutton("readonly", true);
		$("#entireSQL").radiobutton("readonly", true);
		$("#max_fetch_cnt").textbox("readonly", true);
		
	} else {
		$(".textbox").removeClass("textbox-readonly");
		
		$(".textbox-icon-disabled").removeClass("textbox-icon-disabled");
		$(".validatebox-readonly").removeClass("validatebox-readonly");
		$(".checkbox-readonly").removeClass("checkbox-readonly");
		
		$("#original_dbid").combobox("readonly",false);
		$("#perf_check_target_dbid").combobox("readonly",false);
		$("#perf_check_range_begin_dt").textbox("readonly",false);
		$("#perf_check_range_end_dt").textbox("readonly",false);
		
		if ( $("#perf_check_range_begin_dt").textbox("getValue") == "" || $("#perf_check_range_begin_dt").textbox("getValue") == null ) {
			$("#perf_check_range_begin_dt").textbox("setValue", $("#startDate").val());
		}
		
		if ( $("#perf_check_range_end_dt").textbox("getValue") == "" || $("#perf_check_range_end_dt").textbox("getValue") == null ) {
			$("#perf_check_range_end_dt").textbox("setValue", $("#endDate").val());
		}
		
		$("#all_sql_yn_chk").checkbox("readonly",false);
		
		$("#topn_cnt").textbox("readonly",false);
		
		if ( $("#submit_form #all_sql_yn").val() == "N") {
			$("#submit_form #all_sql_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #all_sql_yn_chk").checkbox("check");
			$("#topn_cnt").textbox("readonly",true);
		}
		
		$("#literal_except_yn_chk").checkbox("readonly",false);
		$("#max_fetch_cnt").textbox("readonly",true);
		
		if ( $("#literal_except_yn").val() == "N" ) {
			$("#submit_form #literal_except_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #literal_except_yn_chk").checkbox("check");
		}

		$("#sql_time_limt_cd").combobox("readonly",false);
		
		$("#owner_list").textbox("readonly",false);
		$("#module_list").textbox("readonly",false);
		$("#table_name_list").textbox("readonly",false);
		$("#ownerEdit").attr("readonly",false);
		$("#moduleEdit").attr("readonly",false);
		$("#table_nameEdit").attr("readonly",false);
		
		$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
		$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
		$('#table_name_list').textbox('textbox').prop('placeholder', 'ORDER, CUSTOMER, PRODUCT');
		
		if ( $("#submit_form #sql_time_limt_cd").combobox("getValue") == "" || $("#submit_form #sql_time_limt_cd").combobox("getValue") == null ) {
			$("#sql_time_limt_cd").combobox("setValue","1");
		}
		$("#AWR").radiobutton("readonly", false);
		$("#entireSQL").radiobutton("readonly", false);
		$("#entireSQL").radiobutton({checked: true});
		
		$("#max_fetch_cnt").textbox("readonly",false);
	}
}

function checkCondition() {
	if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sqlPerformanceP" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','ASIS DB(원천DB)를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #perf_check_target_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','TOBE DB(목표DB)를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #perf_check_range_begin_dt").textbox("getValue") == '' ||
		 $("#submit_form #perf_check_range_end_dt").textbox("getValue") == ''	) {
		parent.$.messager.alert('경고','수집기간을 입력해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #perf_check_range_begin_dt").textbox("getValue") >	$("#submit_form #perf_check_range_end_dt").textbox("getValue") ) {
		parent.$.messager.alert('경고','수집기간을 확인해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #topn_cnt").textbox("getValue") == '' &&  $("#all_sql_yn_chk").checkbox("options").checked == false ) {
		parent.$.messager.alert('경고','TOP N을 입력해 주세요.','warning');
		return false;
	}
	
	return true;
}

function textCheck() {
	var split = ""; 
	
	split1 = $(".owner_list .textbox-text").val().trim().split("\,");
	if ( split1.length > 100) {
		parent.$.messager.alert('경고','OWNER 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( split1.length > 1 ) {
			for ( var ownerIdx = 0; ownerIdx < split1.length; ownerIdx++) {
				if ( split1[ownerIdx].trim() == "" ) {
					parent.$.messager.alert('경고','OWNER 값을 확인 후 다시 성능비교 를 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	split2 = $(".module_list .textbox-text").val().trim().split("\,");
	if ( split2.length > 100) {
		parent.$.messager.alert('경고','MODULE 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( split2.length > 1 ) {
			for ( var moduleIdx = 0; moduleIdx < split2.length; moduleIdx++) {
				if ( split2[moduleIdx].trim() == "" ) {
					parent.$.messager.alert('경고','MODULE 값을 확인 후 다시 성능비교 를 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	
	split3 = $(".table_name_list .textbox-text").val().trim().split("\,");
	if ( split3.length > 100) {
		parent.$.messager.alert('경고','TABLE_NAME 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( split3.length > 1 ) {
			for ( var tableNameIdx = 0; tableNameIdx < split3.length; tableNameIdx++) {
				if ( split3[tableNameIdx].trim() == "" ) {
					parent.$.messager.alert('경고','TABLE_NAME 값을 확인 후 다시 성능비교 를 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	return true;
}

function Btn_SqlAutoPerfCompare() {
	
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
	var param = "확인";
	var msgStr ="실행 하시겠습니까?";
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			ajaxCall("/AutoPerformanceCompareBetweenDatabase/getTuningTargetCount",
					$("#submit_form"),
					"POST",
					callback_getTuningTargetCountAction);
		}
	});
}

var callback_getTuningTargetCountAction = function( result ) {
	
	if ( result.result ) {
		
		var sqlTimeLimt_cd = $("#sql_time_limt_cd").combobox("getValue");
		
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
//			$("#sql_time_direct_pref_value").val( $(".sql_time_limt_cd .textbox-text").val() );
		}
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/countExecuteTms",
				$("#submit_form"),
				"POST",
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
		$("#submit_form #data_yn").val("Y");
		onReadonlyCheck();
		
		// 02. SQL 자동성능점검 수행
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/updateSqlAutoPerformance",
				$("#submit_form"),
				"POST",
				callback_updateSqlAutoPerformanceAction);
	} else {
		if ( result.result == false && result.txtValue == "true" ) {
			var param = "확인";
			var msgStr ="이전에 수행된 실행 정보가 있습니다. 재수행시 이전 실행 정보가 삭제됩니다.<br>계속 진행하시겠습니까?";
			
			parent.$.messager.confirm( param,msgStr,function(r) {
				if (r) {
					if (parent.openMessageProgress != undefined) parent.openMessageProgress("대상을 선정중입니다.<br>대상 선정이 완료되면 작업이 자동실행 됩니다."," ");
					
					// 01. 수행결과 초기화
					initPerfCheckResult();
					
					// 화면  readOnly 처리
					$("#submit_form #data_yn").val("Y");
					onReadonlyCheck();
					
					// 02. SQL 자동성능점검 수행
					ajaxCall("/AutoPerformanceCompareBetweenDatabase/updateSqlAutoPerformance",
							$("#submit_form"),
							"POST",
							callback_updateSqlAutoPerformanceAction);
					
				} else {
					if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
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
	
	// 수행 결과 조회
	Btn_RefreshChange();
	
	if ( !result.result ) {
		parent.$.messager.alert('정보',result.message, 'warning');
		
		$("#submit_form #data_yn").val("N");
		onReadonlyCheck();
		
		return false;
	} else {
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/performanceCompareCall",
				$("#submit_form"),
				"POST",
				callback_performanceCompareCallAction);
	}
}

/* SQL-3 : 수행결과 조회 */
function Btn_RefreshChange() {
	var intSec = 0;
	
	Btn_LoadPerformanceCheckCount();
	
	if ( $("#refresh").val() == "Y" ) {
		intSec = strParseInt( $("#timer_value").textbox("getValue"),0 );
		timer = window.setTimeout("Btn_RefreshChange()",(intSec*1000));
	} else {
		window.clearTimeout(timer);
	}
}

function Btn_LoadPerformanceCheckCount() {
	/* modal progress open */
	if (parent.openMessageProgress != undefined) parent.openMessageProgress("수행결과 조회"," ");
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerformanceCheckCount",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_performanceCompareCallAction = function () {
	// 수행 결과 조회
	Btn_RefreshChange();
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	var data = JSON.parse(rows)[0];
	
	if ( typeof data == 'undefined' ) {
		/* modal progress close */
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		if ( $("#submit_form #data_yn").val() == "Y" && $("#original_dbid").combobox("getValue") != '' ) {
			parent.$.messager.alert('정보','수집된 SQL이 0건 입니다.<br> 조건값들을 조정 후 재수행 바랍니다.','info');
			
			ajaxCall("/AutoPerformanceCompareBetweenDatabase/updateAutoPerfChkIsNull",
					$("#submit_form"),
					"POST",
					callback_UpdateAutoPerfChkIsNullAction);
		}
		
		return;
	} else {
		if ( data.total_cnt == 0 ) {
			parent.$.messager.alert('정보','수집된 SQL이 0건 입니다.<br> 조건값들을 조정 후 재수행 바랍니다.','info');
			
			ajaxCall("/AutoPerformanceCompareBetweenDatabase/updateAutoPerfChkIsNull",
					$("#submit_form"),
					"POST",
					callback_UpdateAutoPerfChkIsNullAction);
			
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
	}
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

var callback_UpdateAutoPerfChkIsNullAction = function( result ) {
	$("#submit_form #data_yn").val("N");
	
	onReadonlyCheck();
	
	window.clearTimeout(timer);
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
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/countExecuteTms",
			$("#submit_form"),
			"POST",
			callback_ForceCountExecuteTmsAction);
}

var callback_ForceCountExecuteTmsAction = function(result) {
	if ( result.txtValue == 'false' ) {
		
		// 수행중일 경우
		var param = "확인";
		var msgStr = $('#submit_form #sqlPerformanceP').combobox('getText') + "에서 수행 중인 작업을 강제완료처리 하시겠습니까?";
		
		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("강제완료처리", "강제 완료 처리 중입니다.");
				
				ajaxCall("/AutoPerformanceCompareBetweenDatabase/forceUpdateSqlAutoPerformance",
						$("#submit_form"),
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
		parent.$.messager.alert('정보','해당 SQL점검팩에 대한 강제 완료 처리가 완료되었습니다.','info');
	} else {
		parent.$.messager.alert('정보','강제완료처리 하지 못했습니다.' , 'info');
	}
	
	Btn_RefreshChange();
}

function closeOwnerBox() {
	$("#owner_list").textbox( "setValue", $("#ownerEdit").val() );
	$('#ownerEditBox').window("close");
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

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	
	setTimeout(function() {
		if ( $("#submit_form #sqlPerformanceP").combobox("getValue") == sqlPerfId ) {
			$("#submit_form #sqlPerformanceP").combobox("setValue","");
			$("#submit_form #sql_auto_perf_check_id").val("");
			
			$("#submit_form #data_yn").val("N");
			onReadonlyCheck();
			
			$("#submit_form #original_dbid").combobox("setValue","");
			$("#submit_form #perf_check_target_dbid").combobox("setValue","");
			
			$("#entireSQL").radiobutton({checked: true});
			
			$("#submit_form #perf_check_range_begin_dt").textbox("setValue", $("#startDate").val());
			$("#submit_form #perf_check_range_end_dt").textbox("setValue", $("#endDate").val());
			
			$("#submit_form #all_sql_yn").val("Y");
			$("#submit_form #all_sql_yn_chk").checkbox("check");
			$("#submit_form #topn_cnt").textbox("setValue", "");
			$("#topn_cnt").textbox("readonly",true);
			
			$("#submit_form #literal_except_yn").val("Y");
			$("#submit_form #literal_except_yn_chk").checkbox("check");
			
			$("#submit_form #sql_time_limt_cd").combobox("setValue", "01");
			
			$('#max_fetch_cnt').textbox('setValue', "100000");
			
			$("#submit_form #owner_list").textbox("setValue", "");
			$("#submit_form #module_list").textbox("setValue", "");
			$("#submit_form #table_name_list").textbox("setValue", "");
			console.log("탄다 : "+$("#submit_form #sqlPerformanceP").combobox("getValue") +" , "+ sqlPerfId);
		}
	},300);
}
