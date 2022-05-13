var originalDbid = '';
var prevSelection='';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// popup location setting
	$('#saveSQLPerformance').window({
		title : "SQL점검팩",
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});

	createList();

	$(".datagrid-header-check").html("선<br>택");
	
	//소스DB 불러오기
	$("#original_dbid").combobox({
		url:"/AnalyzeImpactChangedTable/getOperationDB?db_operate_type_cd=&database_kinds_cd="+$('#database_kinds_cd').val(),
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight: 300,
		onChange: function(newValue, oldValue) {
			$('#sqlPerformanceP').combobox('clear');
			originalDbid = newValue;
		},
		onShowPanel: function() {
			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
		},
		onLoadError: function() {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onLoadSuccess: function(items) {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			$("#original_dbid").combobox("textbox").attr("placeholder",'전체');
			$('#sqlPerformanceP').combobox('textbox').attr('placeholder','전체');
		}
	});
	
	$('#original_dbid').siblings('span').on('click', function(){
		$('#original_dbid').combobox("setValue",'');
	});
	
	// SQL점검팩 combobox 클릭시 데이터 불러오기
	$('#sqlPerformanceP').siblings('span').on('click', function(){
		$('#sqlPerformanceP').combobox('clear');
		
		beginDate = encodeURIComponent( $('#perf_check_exec_begin_dt').datebox('getText') );
		endDate = encodeURIComponent( $('#perf_check_exec_end_dt').datebox('getText') );
		
		url = "/AnalyzeImpactChangedTable/getSqlPerfPacName?perf_check_exec_begin_dt="
			+ beginDate + "&perf_check_exec_end_dt=" + endDate
			+ "&database_kinds_cd=" + $('#database_kinds_cd').val();
		
		if ( checkConditionDate() == false) {
			return;
			
		}else {
			if( $("#original_dbid").textbox("getValue") != ''){
				let originalDbid = encodeURIComponent( $("#original_dbid").textbox("getValue") );
				
				url = "/AnalyzeImpactChangedTable/getSqlPerfPacName?perf_check_exec_begin_dt="
					+ beginDate + "&perf_check_exec_end_dt=" + endDate
					+ "&database_kinds_cd=" + $('#database_kinds_cd').val()
					+ "&original_dbid=" + originalDbid + "&condition=1";
			}
			
			$('#sqlPerformanceP').combobox({
				url:url, 
					method:"get",
					valueField:'sql_auto_perf_check_id',
					textField:'perf_check_name',
					panelHeight: 300,
					onSelect: function(data) {
						if ( data.sql_auto_perf_check_id == null) {
							$('#sqlPerformanceP').combobox("setValue",'');
							
						} else {
							$('#dataYn').val(data.data_yn);
						}
						
					},
					onLoadSuccess: function(item) {
						$('#sqlPerformanceP').combobox('textbox').attr('placeholder','전체');
					},
					onLoadError: function() {
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
			});
			
		}
	});
	
	Btn_SqlAutoPerfSearch();
});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: "true",
		checkOnSelect : "false",
		selectOnCheck : "true",
		columns:[[
			{field:'chk',halign:'center',align:'center',checkbox:true},
			{field:'perf_check_name',title:'SQL점검팩명',halign:'center',width:'12%',align:'left'},
			{field:'perf_check_desc',title:'SQL점검팩 설명',halign:'center',width:'12%',align:'left'},
			{field:'original_db_name',title:'ASIS DB<br>(원천DB)',width:'4%',halign:'center',align:'left'},
			{field:'perf_check_target_db_name',title:'TOBE DB<br>(목표DB)',width:'4%',halign:'center',align:'left'},
			{field:'check_range_period',title:'수집기간',width:'9%',halign:'center',align:'center'},
			{field:'topn_cnt',title:'TOP N',halign:'center',align:'right'},
			{field:'literal_except_yn',title:'Literal<br>제외',width:'3%',halign:'center',align:'center'},
			{field:'sql_time_limt_nm',title:'SQL Time<br>Limit(분)',width:'4%',halign:'center',align:'right'},
			{field:'max_fetch_cnt',title:'최대 Fetch<br>건수',width:'4%',halign:'center',align:'right'},
			{field:'owner_list',title:'Owner',width:'12%',halign:'center',align:'left'},
			{field:'module_list',title:'Module',width:'12%',halign:'center',align:'left' },
			{field:'table_name_list',title:'Table Name',width:'12%',halign:'center',align:'left'},
			{field:'perf_check_exec_end_dt',title:'최종수행일시',halign:"center",align:'right'},
			{field:'perf_check_exec_time',title:'수행시간',halign:"center",align:'right'},
			{field:'perf_check_force_close_yn',title:'강제완료<br>처리',width:'3%',halign:'center',align:'center'},
			{field:'user_nm',title:'수행자명',halign:'center',align:'left' },
			{field:'sql_auto_perf_check_id',title:'sql_auto_perf_check_id',hidden:false }
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_SqlAutoPerfSearch() {
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$('#tableList').datagrid("loadData", []);
	
	//grid조회
	ajaxCallTableList();
}

// 페이지처리
function formValidationCheck() {
	return true;
}

function fnSearch() {
	ajaxCallTableList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress("SQL점검팩 조회", " ");
	
	/* SQL점검팩 리스트 */
	ajaxCall("/AnalyzeImpactChangedTable/loadSqlPerformancePacList",
			$("#submit_form"),
			"GET",
			callback_LoadSqlPerformancePacListAction);
}

var callback_LoadSqlPerformancePacListAction = function(result) {
	json_string_callback_common(result, '#tableList', true);

	let dataLength = JSON.parse(result).dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if (parent.closeMessageProgress != undefined)
		parent.closeMessageProgress();
}

// SQL점검팩 삭제
function deleteTable() {
	let data = $('#tableList').datagrid('getChecked');
	
	if (data.length > 0) {
		ajaxCallWithSimpleData("/AnalyzeImpactChangedTable/getTuningTargetCount",
								{sql_auto_perf_check_id:data[0].sql_auto_perf_check_id},
								"GET",
								callback_getTuningTargetCountAction);
		
	} else {
		parent.$.messager.alert('경고', '삭제할 점검팩을 선택해 주세요.', 'warning');
	}
}

var callback_getTuningTargetCountAction = function(result) {
	
	if (result.result) {
		ajaxCallWithSimpleData("/AnalyzeImpactChangedTable/countExecuteTms",
								{sql_auto_perf_check_id:$('#tableList').datagrid('getChecked')[0].sql_auto_perf_check_id},
								"GET",
								callback_CountExecuteTmsAction);
	} else {
		parent.$.messager.alert(
				'정보','튜닝대상으로 선정되었던 SQL이 존재합니다.<br>튜닝대상 선정 작업이 수행되었을 경우 SQL점검팩은 삭제할 수 없습니다.',	'info');
	}
}

var callback_CountExecuteTmsAction = function(result) {
	if (result.txtValue == "true") {
		let param = "확인";
		let msg = '선택된 SQL점검팩의 성능비교 정보가 모두 삭제됩니다.<br>삭제하시겠습니까?';

		parent.$.messager.confirm(param, msg, function(r) {
			if (r) {
				let submitData = {
						sql_auto_perf_check_id: $('#tableList').datagrid('getChecked')[0].sql_auto_perf_check_id,
						database_kinds_cd: $('#database_kinds_cd').val()
				};
				
				// SQL점검팩 삭제
				ajaxCallWithSimpleData(
						"/AnalyzeImpactChangedTable/deleteSqlAutoPerformanceChk",
						submitData,
						"POST",
						callback_deleteSqlAutoPerformanceChkAction);
			}
		});
		
	} else if (result.txtValue == "false") {
		parent.$.messager.alert(
						'정보','해당 SQL점검팩에 대한 성능비교 작업을 수행 중입니다.<br>성능비교 탭에서 진행상황을 검색할 수 있습니다.','info');
	}

}
var callback_deleteSqlAutoPerformanceChkAction = function(result) {
	let data = $('#tableList').datagrid('getChecked');
	
	if (result.result) {
		
		parent.$.messager.alert('정보', '점검팩 ' + data[0].perf_check_name + '을(를) 삭제하였습니다.', 'info');
		
		$('#sqlPerformanceP').combobox('clear');
		
		parent.ReloadSqlPerformancePac( $('#tableList').datagrid('getChecked')[0].sql_auto_perf_check_id );
		
	} else {
		parent.$.messager.alert('정보', '점검팩 ' + data[0].perf_check_name
				+ '을(를) 삭제하지 못했습니다.', 'info');
	}
	
	Btn_SqlAutoPerfSearch();
}
/* 등록 팝업 호출 */
function showSaveSQLPerformance_popup() {
	loadSqlPerformancePopupCombo();
	let selectData = $('#tableList').datagrid('getChecked');
	
	if( selectData.length > 0 ){
		let popupSqlAutoPerfCheckId = selectData[0].sql_auto_perf_check_id;
		
		$("#popup_sqlPerformanceP").combobox("setValue", popupSqlAutoPerfCheckId );
	}

	$('#saveSQLPerformance').window({
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	if ( $("#sqlPerformanceP").val() == null ) {
		$("#popup_perf_check_name").textbox("setValue","");
		$("#popup_perf_check_desc").textbox("setValue","");
	}
	
	$('#saveSQLPerformance').window("open");
}
function loadSqlPerformancePopupCombo() {
	// SQLPerformancePacCombo(Popup) // SQL점검팩 팝업콤보
	$('#popup_sqlPerformanceP').combobox({
		url:'/AnalyzeImpactChangedTable/getSqlPerfPacName?'
			+ "&database_kinds_cd=" + $('#database_kinds_cd').val(),
		method:"GET",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onChange: function(newValue) {
			$("#popup_sql_auto_perf_check_id").val(newValue);
			
			let submitData = {
					sql_auto_perf_check_id: newValue,
					database_kinds_cd: $('#database_kinds_cd').val()
			};
			
			ajaxCallWithSimpleData(
					"/AnalyzeImpactChangedTable/getSqlPerformanceInfo",
					submitData,
					"POST",
					callback_setPopUpData);
		},
		onLoadSuccess: function() {
			$("#popup_sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	
	});
}

var callback_setPopUpData = function(result){
	let jResult = JSON.parse(result);
	
	if ( jResult.length != 0 ) {
		$("#popup_perf_check_name").textbox("setValue",replaceHtmlTagToNormal(jResult[0].perf_check_name));
		$("#popup_perf_check_desc").textbox("setValue",replaceHtmlTagToNormal(jResult[0].perf_check_desc));
	}
}
function Btn_ResetField() {
	$("#popup_sqlPerformanceP").combobox("setValue","");
	$("#popup_sql_auto_perf_check_id").val("");
	$("#popup_perf_check_name").textbox("setValue","");
	$("#popup_perf_check_desc").textbox("setValue","");
	
}
function Btn_SaveSqlPerfPac() {
	if ( $("#popup_perf_check_name" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩명을 입력해 주세요.','warning');
		return false;
	}
	
	if ( $("#popup_perf_check_desc").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩 설명을 입력해 주세요.','warning');
		return false;
	}
	
	if ( getByteSize( $("#popup_perf_check_name" ).textbox("getValue") ) > 100 || getByteSize( $("#popup_perf_check_desc" ).textbox("getValue") ) > 100 ) {
		parent.$.messager.alert('경고','입력한 값이 100byte 초과합니다.<br> 다시 입력해 주세요.','warning');
		return false;
	}
	
	//insert / update 
	if ( $("#popup_sqlPerformanceP").combobox("getValue") == "" ) {
		// 등록 일 경우
		ajaxCall("/AnalyzeImpactChangedTable/insertSqlPerformanceInfo",
				$("#popup_form"),
				"POST",
				callback_SaveSqlPerfPac);
		
	} else {
		// 수정일 경우
		ajaxCall("/AnalyzeImpactChangedTable/updateSqlPerformanceInfo",
				$("#popup_form"),
				"POST",
				callback_SaveSqlPerfPac);
	}
}

var callback_SaveSqlPerfPac = function(result) {
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
						
				Btn_SqlAutoPerfSearch();
				
				$('#saveSQLPerformance').window("close");
				
				},500);
		});
		
	} else {
		parent.$.messager.alert('정보',result.message,'info');
	}
}

//문자열 byte
function getByteSize(s) {
	const str = s.toString();
	let byteSize = 0;
	let char = '';
	
	for (let strArr = 0; !isNaN(str.charCodeAt(strArr)); strArr++) {
		char = str.charCodeAt(strArr);
		
		// 12비트 이상으로 표현 가능한 유니코드
		if (char >> 11) {
			byteSize += 3;
		// 8비트 ~ 11비트로 표현 가능한 유니코드
		} else if (char >> 7) {
			byteSize += 2;
		// 7비트 이하로 표현 가능한 유니코드
		} else {
			byteSize += 1;
		}
	}
	
	return byteSize;
}
