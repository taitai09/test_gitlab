$(document).ready(function() {
	$("body").css("visibility", "visible");
	
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	$('#project_id').combobox({
		url:"/sqlStandardOperationDesign/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$ ( '#project_id'). combobox ( 'textbox'). attr ( 'placeholder', ' 선택');
		},
		onChange:function( newValue, oldValue ) {
			$('#loadSQLProjectUnit_form #project_id').val( newValue );
		}
	});
	
	createList();
});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트',halign:"center",align:'left'},
			{field:'qty_chk_idt_cd',title:'품질 점검 지표',halign:"center",align:'center'},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',halign:"center",align:'left'},
			{field:'err_cnt',title:'작업결과(건수)',halign:"center",align:'right'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#project_nm").textbox("setValue", row.project_nm);
	
	$("#project_id").combobox("setValue",row.project_id);
	
	Btn_OnClick();
}

function Btn_OnClick(){
	if($('#project_id').combobox("getValue") == '' || $('#project_id').combobox("getValue") == null) {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val()," ");
	
	ajaxCallTableList();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
}

function ajaxCallTableList(){
	ajaxCall("/loadQualityReviewWork",
			$("#submit_form"),
			"POST",
			callback_TableListAction);
	
	ajaxCall("/loadSqlCount",
			$("#submit_form"),
			"POST",
			callback_LoadSqlCountAction);
	
	ajaxCall("/loadWorkStatus",
			$("#submit_form"),
			"POST",
			callback_LoadWorkStatusAction);
//	ajaxCall("/checkProc",
//			$("#submit_form"),
//			"POST",
//			callback_StatusAction);
}

var callback_TableListAction = function(result) {
	try {
		var data = JSON.parse(result);
		
		json_string_callback_common(JSON.stringify(data), '#tableList', true);
	} catch(err) {
		console.error(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

var callback_LoadSqlCountAction = function(result) {
	try {
		var data = JSON.parse(result);
		
		if(data.rows.length > 0) {
			var rnum = data.rows[0].rnum;
			$(".perf_check_result_blue").val('SQL 수 : ' + rnum);
		}
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_LoadWorkStatusAction = function(result) {
	try {
		var data = JSON.parse(result);
		
		if(data.rows.length > 0) {
			var process1 = data.rows[0].process1;
			var workStatus = '';
			
			if(process1 == '오류') {
				workStatus = '작업상태 : ' + process1;
				
				ajaxCall("/loadErrorMessage",
						$("#submit_form"),
						"POST",
						callback_LoadErrorMessageAction);
			} else if(process1 == '검토중') {
				setHiddenErrorMessageSpan();
				
				var process2 = data.rows[0].process2;
				
				if(process2 == null) {
					workStatus = '작업상태 : 검토중';
				} else {
					workStatus = '작업상태 : 검토중' + data.rows[0].process2;
				}
			} else {
				setHiddenErrorMessageSpan();
				
				workStatus = '작업상태 : ' + process1;
			}
			
			$(".perf_check_result_green").val(workStatus);
		}
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_LoadErrorMessageAction = function(result) {
	try {
		var data = JSON.parse(result);
		
		if(data.rows.length > 0) {
			var errMessage = data.rows[0].err_table_name + " : " + data.rows[0].err_sbst;
			
			$('#error_message').textbox('setValue', errMessage.trim());
			$('#error_message_span').css('visibility', 'visible');
		} else {
			setHiddenErrorMessageSpan();
		}
	} catch(err) {
		setHiddenErrorMessageSpan();
		parent.$.messager.alert('에러', err.message);
	}
};

function setHiddenErrorMessageSpan() {
	$('#error_message_span').css('visibility', 'hidden');
}

var callback_StatusAction = function(result) {
	try {
		var data = JSON.parse(result);
		var msg = "";
		
		if(data.rows.length == 0) {
			msg = "미수행";
		} else if(data.rows[0].wrk_end_dt != null) {
			msg = "검토완료";
		} else if(data.rows[0].wrk_end_dt == null) {
			msg = "검토중";
		}
		
		if(data.rows.length > 0) {
			var process1 = data.rows[0].process1;
			var workStatus = '';
			
			console.log(data);
			
			if(process1 == '오류') {
				workStatus = '작업상태 : ' + process1;
				
				ajaxCall("/loadErrorMessage",
						$("#submit_form"),
						"POST",
						callback_LoadErrorMessageAction);
			} else if(process1 == '검토중') {
				setHiddenErrorMessageSpan();
				
				var process2 = data.rows[0].process2;
				
				if(process2 == null) {
					workStatus = '작업상태 : 검토중';
				} else {
					workStatus = '작업상태 : 검토중' + data.rows[0].process2;
				}
			} else {
				setHiddenErrorMessageSpan();
				
				workStatus = '작업상태 : ' + process1;
			}
			
			$(".perf_check_result_green").val(workStatus);
		}
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function Excel_Download(){
	if($('#project_id').combobox("getValue") == '') {
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	
	$("#submit_form").attr("action","/excelDownQualityReviewWork");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
//	openMessageProgress();
}

/* 팝업 호출 */
function showLoadSqlProjectUnitPop(){
	if($('#project_id').combobox("getValue") == '') {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$('#loadSQLProjectUnit_form #project_nm').textbox('setValue', $('#submit_form #project_id').combobox('getText'));
	$('#loadSQLProjectUnit_form #project_id').val( $('#submit_form #project_id').combobox('getValue') );
	$('#loadSQLProjectUnit_form #uploadFile').textbox('setValue','');

	$('#loadSqlProjectUnitPop').window("open");
}

function Btn_Work() {
	if($('#project_id').combobox("getValue") == '') {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	ajaxCall("/checkQualityReviewWorkInRun",
			$("#submit_form"),
			"POST",
			callback_CheckQualityReviewWorkInRunAction);
}

var callback_CheckQualityReviewWorkInRunAction = function(result) {
	try {
		var data = JSON.parse(result);
		var wrkProcessYn = data.rows[0].wrk_process_yn;
		var msg = "";
		
		if(wrkProcessYn == 'Y') {
			msg = "선택한 프로젝트에서 진행 중인 품질검토 작업이 있습니다. 품질검토 작업을 다시 진행하려면 [강제완료처리] 버튼을 클릭하여 강제완료처리 후 수행 바랍니다!";
			
			var w = parent.$.messager.alert('경고',msg,'warning');
			
			w.window('resize', {width:330}).window('center');
			
			return false;
		} else {
			var param = "Confirm";
			msg = "품질검토작업을 수행하시겠습니까?";
			
			
			parent.$.messager.confirm(param,msg,function(r){
				if (r){
					$("#on_click").addClass('l-btn-disabled');
					$("#on_click").removeAttr('onclick');
					$("#work").addClass('l-btn-disabled');
					$("#work").removeAttr('onclick');
					
					ajaxCall("/preProcess",
							$("#submit_form"),
							"POST",
							callback_PreProcessAction);
				}
			});
		}
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_PreProcessAction = function(result) {
	try {
		ajaxCallTableList();
		
		ajaxCall("/qualityReviewOperation",
				$("#submit_form"),
				"POST",
				null);
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
}

function Btn_ForceProcessingCompleted() {
	if($('#project_id').combobox("getValue") == '') {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	ajaxCall("/checkQualityReviewWorkInRun",
			$("#submit_form"),
			"POST",
			callback_ForceCheckQualityReviewWorkInRunAction);
}

var callback_ForceCheckQualityReviewWorkInRunAction = function(result) {
	try {
		var data = JSON.parse(result);
		var wrkProcessYn = data.rows[0].wrk_process_yn;
		
		if(wrkProcessYn == 'Y') {
			var param = "Confirm";
			var msg = "선택한 프로젝트에서 수행중인 품질검토작업을 강제종료하시겠습니까?";
			
			parent.$.messager.confirm(param,msg,function(r){
				if (r){
					/* modal progress open */
					if(parent.openMessageProgress != undefined) parent.openMessageProgress('', '강제완료처리 중입니다.');
					
					ajaxCall("/forceProcessingCompleted",
							$("#submit_form"),
							"POST",
							callback_ForceProcessingCompletedAction);
				}
			});
		} else {
			var msg = "강제완료대상 SQL 표준 품질검토 작업이 없습니다.";
			var w = parent.$.messager.alert('정보',msg,'info');
			
			w.window('resize', {width:330}).window('center');
			
			return false;
		}
	} catch(err) {
		parent.$.messager.alert('에러', err.message);
	}
}

function callback_ForceProcessingCompletedAction(result) {
	var data = null;
	var jsonFlag = false;
	var msg = "강제완료처리가 되었습니다.";
	
	try {
		data = JSON.parse(result);
		
		jsonFlag = (typeof data === 'object');
	} catch(err) {
		jsonFlag = false;
	}
	
	if(jsonFlag && data.result) {
		parent.infoMessager(msg);
	} else {
		parent.infoMessager(msg);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	Btn_OnClick();
}