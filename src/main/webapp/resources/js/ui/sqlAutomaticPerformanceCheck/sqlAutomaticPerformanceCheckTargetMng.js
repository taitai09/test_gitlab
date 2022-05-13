$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'project_nm',title:'프로젝트명',width:"20%", halign:"center",align:"left"},
			{field:'db_name',title:'DB',width:"10%", halign:"center",align:"left"},
			{field:'sql_idfy_cond_type_nm',title:'성능점검대상범위',width:"10%", halign:"center",align:"left"},
			{field:'owner',title:'OWNER',width:"10%", halign:"center",align:'left'},
			{field:'table_name',title:'테이블명',width:"15%", halign:"center",align:'left'},
			{field:'module',title:'MODULE',width:"15%", halign:"center",align:'left'},
			{field:'project_id',hidden:true},
			{field:'check_target_seq',hidden:true},
			{field:'dbid',hidden:true},
			{field:'sql_idfy_cond_type_cd',hidden:true}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#project_id").combobox("textbox").attr("placeholder",'선택');
	
	$('#tableList').datagrid("loadData", []);
	
	$("#detail_form #sql_idfy_cond_type_cd").val('1');
	
	$('#detail_form #sql_idfy_cond_type1').radiobutton({
		onChange:function(val){
			if(val == true){
				resetDetailField('1');
				initOwnerDetail();
			}
		}
	});
	$('#sql_idfy_cond_type2').radiobutton({
		onChange:function(val){
			if(val == true){
				resetDetailField('2');
				initOwnerDetail();
			}
		}
	});
	$('#sql_idfy_cond_type3').radiobutton({
		onChange:function(val){
			if(val == true){
				resetDetailField('3');
			}
		}
	});
	
	$("#submit_form #project_id").combobox("textbox").attr("placeholder",'선택');
	$("#detail_form #project_id").combobox("textbox").attr("placeholder",'선택');
	$("#submit_form #selectCombo").combobox("textbox").attr("placeholder",'전체');
	$("#detail_form #selectComboOrigin").combobox("textbox").attr("placeholder",'선택');
	
	Btn_ResetField('1');
	
	$('#detail_form #project_check_target_type_cd').val("3");
	
	initOwnerDetail();
});

function resetDetailField(setDetailView) {
	$("#detail_form #sql_idfy_cond_type_cd").val(setDetailView);

	setDetailViewControl(setDetailView);
	
//	$("#detail_form #selectComboOrigin").combobox("reset");
	$("#detail_form #owner_detail").combobox("reset");
	$("#detail_form #table_name").textbox("setValue", "");
	$("#detail_form #module").textbox("setValue", "");
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

function Btn_TableBatchSavePopUp() {
	if($("#detail_form #project_id").combobox('getValue')== ""){
		parent.$.messager.alert('','프로젝트는 필수항목입니다. 프로젝트 조회한 후에 등록/삭제 하시기 바랍니다.');
		return;
	}

	$('#projectSqlIdfyConditionPop_form #project_id').val($("#detail_form #project_id").combobox('getValue'));
	$('#projectSqlIdfyConditionPop_form #dbid').val($("#detail_form #dbid").val());
	$('#projectSqlIdfyConditionPop_form #owner').val($("#detail_form #owner").val());
	$('#projectSqlIdfyConditionPop_form #project_check_target_type_cd').val($('#detail_form #project_check_target_type_cd').val());

	$('#projectSqlIdfyConditionPop_form #selectList').datagrid('loadData',[]);
	$('#projectSqlIdfyConditionPop_form #targettList').datagrid('loadData',[]);
	
	$('#projectSqlIdfyConditionPop_form #selectComboOrigin').combobox('clear');
	$('#projectSqlIdfyConditionPop_form #selTableOwner').combobox('clear');
	
	$('#projectSqlIdfyConditionPop').window("open");

	getSelectProjectSqlIdfyConditionTable();
	
	$('#projectSqlIdfyConditionPop_form #selectList').datagrid('removeFilterRule');
	
	Btn_TableBatchPopUpSelTableOwner();
}

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #project_nm").textbox("setValue", row.project_nm);
	$("#submit_form #project_id").combobox('setValue',row.project_id);
	$("#submit_form #db_name").val(row.db_name);
	
	loadSelectCombo(row.project_id);
	
	loadOriginalDb($("#submit_form #project_id").combobox('getValue'));
	
	getUserName();
}

function loadSelectCombo(project_id) {
	if (project_id == "") {
		return;
	}
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DB"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		$("#submit_form #selectCombo").combobox({
			url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+project_id+"&isAll=Y",
			method:"get",
			valueField:'original_dbid',
			textField:'original_db_name',
			panelHeight: 300,
			onLoadError: function(){
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			},
			onLoadSuccess: function(items) {
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				
			}
		});
	} catch(err) {
		consoloe.error(err.message);
	}
}
function loadDetailSelectCombo(project_id) {
	if (project_id == "") {
		return;
	}
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DetailDB"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		loadOriginalDb(project_id);
		
		$("#detail_form #selectComboOrigin").combobox({
			url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+project_id+"&isChoice=N",
			method:"get",
			valueField:'original_dbid',
			textField:'original_db_name',
			panelHeight: 300,
			onLoadError: function(){
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			},
			onLoadSuccess: function(items) {
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				//loadOriginalDb(project_id);
				$("#detail_form #selectComboOrigin").combobox("textbox").attr("placeholder",'선택');
			}
		});
	} catch(err) {
		consoloe.error(err.message);
	}
}

function getUserName(){
	if($("#submit_form #project_id").combobox('getValue') == "") {
		return;
	}
	console.log("project_id  : " +  $("#submit_form #project_id").combobox('getValue'));
	$('#submit_form #owner_search').combobox({
		url:"/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/GetProjectUserNameComboBox?project_id="+$("#submit_form #project_id").combobox('getValue')+"&isAll=Y",
		method:'get',
		valueField:'user_id',
		textField:'user_nm',
		onSelect: function(rec) {
			$('#submit_form #owner').val(rec.user_nm);
		},
		onLoadError: function(){
			parent.$.messager.alert('','프로젝트 OWNER 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function initOwnerDetail() {
	$('#detail_form #owner_detail').combobox({
		url:"/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/GetUserNameComboBox?dbid="+$("#detail_form #dbid").val()+"&isChoice=N",
		method:'get',
		valueField:'user_id',
		textField:'user_nm',
		onChange: function(newValue, oldValue) {
			if(newValue == '') {
				return;
			}
			$('#detail_form #owner').val(newValue);
		},
		onLoadError: function(){
			parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items) {
			if($("#detail_form #owner").val() != '') {
				$(this).combobox('setValue', $("#detail_form #owner").val());
			}
			
			$("#detail_form #owner_detail").combobox("textbox").attr("placeholder",'선택');
		}
	});
}

function callback_LoadOwnerDetilaAction(result) {
	var data = JSON.parse(result);
	
	$('#detail_form #owner_detail').combobox('clear');
	$('#detail_form #owner_detail').combobox('loadData', data);
	$("#detail_form #owner_detail").combobox("textbox").attr("placeholder",'선택');
}

function formValidationCheck(){
	return true;
}

function Btn_OnClick(project_id){
	$("#submit_form #currentPage").val("1");
	$("#submit_form #pagePerCount").val("10");
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	
	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	fnSearch(project_id);
}

function fnSearch(project_id) {
	if ( project_id == null || project_id == '') {
		var projectId = $("#submit_form #project_id").combobox("getValue");
	} else {
		var projectId = project_id;
		$("#submit_form #project_id").combobox("setValue", projectId);
		loadSelectCombo(projectId);
		getUserName();
	}
	
	if(projectId == null || projectId == ""){
		parent.$.messager.alert('경고','프로젝트를 선택하십시오.','warning');
		parent.$.messager.progress('close');
		return;
	}
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 자동 성능 점검 대상 관리"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/ProjectSqlIdfyConditionList",
				$("#submit_form"),
				"POST",
				callback_SearchProjectSqlIdfyConditionListAction);
	} catch(err) {
		consoloe.error(err.message);
	}
}

var callback_SearchProjectSqlIdfyConditionListAction = function(result) {
	Btn_ResetField();
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	fnEnableDisablePagingBtn(dataLength);
	
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function loadOriginalDb(project_id) {
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DB"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		$("#selectComboOrigin").combobox({
			url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+project_id+"&isChoice=Y",
			method:"get",
			valueField:'original_dbid',
			textField:'original_db_name',
			panelHeight: 500,
			onLoadError: function(){
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			},
			onChange: function(newValue, oldValue) {
				if(newValue == '') {
					$('#detail_form #owner_detail').combobox('clear');
					$('#detail_form #owner_detail').combobox('loadData', []);
					return;
				}
				
				$("#detail_form #dbid").val(newValue);
				$('#detail_form #owner').val('');
				
				ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/GetUserNameComboBox?dbid="+$("#detail_form #dbid").val()+"&isChoice=N",
						null,
						"GET",
						callback_LoadOwnerDetilaAction);
			},
			onLoadSuccess: function(items) {
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
		});
	} catch(err) {
		consoloe.error(err.message);
	}
}

function Btn_ResetField(gubun){
	// gubun : 1 init, 2 : 초기화버튼 클릭
//	if (gubun == '2') {
//		if ($("#submit_form #project_id").combobox('getValue') == null || $("#submit_form #project_id").combobox('getValue')==""){
//		parent.$.messager.alert('','프로젝트는 필수항목입니다. 프로젝트 조회 후 초기화를 하시기 바랍니다');
//		return false;
//		}
//	}
	
	$("#detail_form #crud_flag").val("C");
	
	$("#tableList").datagrid('clearSelections');
	
//	$("#detail_form #project_nm").textbox("textbox").css("background-color","#F4F4F4");
//	$("#detail_form #project_nm").textbox("setValue", $("#submit_form #project_nm").val());
	$("#detail_form #dbid").val("");
	$("#detail_form #owner").val("");

	$("#detail_form #selectComboOrigin").combobox("setValue", "");
	$("#detail_form #owner_detail").combobox("setValue", "");
	$("#detail_form #table_name").textbox("setValue", "");
	$("#detail_form #module").textbox("setValue", "");
	$("#detail_form #sql_idfy_cond_type_cd").val("2");
	$("#detail_form #project_id").combobox("setValue","");
	$("#detail_form #check_target_seq").val(0);
	$('#sql_idfy_cond_type1').radiobutton({
		checked: false
		});
	$('#sql_idfy_cond_type2').radiobutton({
		checked: true
		});
	$('#sql_idfy_cond_type3').radiobutton({
		checked: false
		});
	$("#detail_form #owner_detail").combobox({disabled:false});
	$("#detail_form #table_name").textbox({readonly:false});
	$("#detail_form #table_name").textbox("textbox").css("background-color","#ffffff");
	$("#detail_form #module").textbox({readonly:true});
	$("#detail_form #module").textbox("textbox").css("background-color","#F4F4F4");
}

function setDetailView(selRow){
	var num = 0;
	if ( selRow.module != null ) {
		num = 3;
	} else if(selRow.table_name != null) {
		num = 2;
	} else {
		num = 1;
	}
	resetDetailField(num);
	$("#crud_flag").val("U");
//	$("#detail_form #project_nm").textbox({readonly:true});
	$("#detail_form #project_id").combobox("setValue", selRow.project_id);
//	$("#detail_form #project_nm").textbox("textbox").css("background-color","#F4F4F4");
	
	$("#detail_form #selectComboOrigin").combobox("setValue", selRow.dbid);
	$("#detail_form #check_target_seq").val(selRow.check_target_seq);
	$("#detail_form #sql_idfy_cond_type_cd").val(selRow.sql_idfy_cond_type_cd);
	$("#detail_form #owner_detail").combobox('setValue', selRow.owner);
	$("#detail_form #table_name").textbox("setValue", selRow.table_name);
	$("#detail_form #module").textbox("setValue", selRow.module);
	$("#detail_form #owner").val(selRow.owner);
	$("#detail_form #project_id").combobox("setValue",selRow.project_id);
	
}

function setDetailViewControl(sql_idfy_cond_type){
	if (sql_idfy_cond_type == '1'){
		$('#sql_idfy_cond_type1').radiobutton({
			checked: true
		});
		$('#sql_idfy_cond_type2').radiobutton({
			checked: false
		});
		$('#sql_idfy_cond_type3').radiobutton({
			checked: false
		});

		$("#detail_form #owner_detail").combobox({disabled:false});
		$("#detail_form #owner").val('');
		$("#detail_form #table_name").textbox({readonly:true});
		$("#detail_form #table_name").textbox("textbox").css("background-color","#F4F4F4");
		$("#detail_form #module").textbox({readonly:true});
		$("#detail_form #module").textbox("textbox").css("background-color","#F4F4F4");
		
		return;
	} else if(sql_idfy_cond_type == '2'){
		$('#sql_idfy_cond_type1').radiobutton({
			checked: false
			});
		$('#sql_idfy_cond_type2').radiobutton({
			checked: true
			});
		$('#sql_idfy_cond_type3').radiobutton({
			checked: false
			});
		$("#detail_form #owner_detail").combobox({disabled:false});
		$("#detail_form #owner").val('');
		$("#detail_form #table_name").textbox({readonly:false});
		$("#detail_form #table_name").textbox("textbox").css("background-color","#FFFFFF");
		$("#detail_form #module").textbox({readonly:true});
		$("#detail_form #module").textbox("textbox").css("background-color","#F4F4F4");
		
		return;
	} else if(sql_idfy_cond_type == '3'){
		$('#sql_idfy_cond_type1').radiobutton({
			checked: false
			});
		$('#sql_idfy_cond_type2').radiobutton({
			checked: false
			});
		$('#sql_idfy_cond_type3').radiobutton({
			checked: true
			});

		$("#detail_form #owner_detail").combobox({disabled:true});
		$("#detail_form #owner").val('');
		$("#detail_form #table_name").textbox({readonly:true});
		$("#detail_form #table_name").textbox("textbox").css("background-color","#F4F4F4");
		$("#detail_form #module").textbox({readonly:false});
		$("#detail_form #module").textbox("textbox").css("background-color","#FFFFFF");
		$("#detail_form #owner_detail").combobox("reset");
		
		return;
	}
}

function Btn_SaveProjectSqlIdfyCondition(){
	if($("#detail_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('','프로젝트명은 필수항목 입니다.');
		return false;
	}
	
	if($('#detail_form #dbid').val() == '') {
		parent.$.messager.alert('','DB는 필수항목 입니다.');
		return false;
	}
	
	if ($("#detail_form #sql_idfy_cond_type_cd").val() == "1"){
		if ($("#detail_form #owner_detail").combobox('getValue') == ""){
			parent.$.messager.alert('','OWNER는 필수항목 입니다.');
			return false;
		}
	} else if ($("#detail_form #sql_idfy_cond_type_cd").val() == "2"){
		if ($("#detail_form #owner_detail").combobox('getValue') == ""){
			parent.$.messager.alert('','OWNER는 필수항목 입니다.');
			return false;
		}
		if($("#detail_form #table_name").textbox('getValue') == ""){
			parent.$.messager.alert('','테이블명은 필수항목 입니다.');
			return false;
		}
	} else{
		if($("#detail_form #module").textbox('getValue')== ""){
			parent.$.messager.alert('','MODULE은 필수항목 입니다.');
			return false;
		} else {
			$("#detail_form #module").textbox('setValue', $.trim( $("#detail_form #module").val() ));
		}
	}
	
	ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Save",
			$("#detail_form"),
			"POST",
			callback_SaveProjectSqlIdfyConditionListAction);
}

//callback 함수
var callback_SaveProjectSqlIdfyConditionListAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','SQL 자동 성능 점검 대상 관리 정보 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				getUserName();
				Btn_OnClick($("#detail_form #project_id").combobox('getValue'));
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function Btn_DeleteProjectSqlIdfyCondition(){
	if($("#detail_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('','조회 후 삭제 하시기 바랍니다.');
		return false;
	}
	
	if($('#tableList').datagrid('getSelected') == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}

	parent.$.messager.confirm('Confirm','SQL 자동 성능 점검 대상 관리 정보를 삭제하시겠습니까?',function(r){
		if (r){
			ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Delete",
					$("#detail_form"),
				"POST",
					callback_DeleteProjectSqlIdfyConditionAction);
		}
	});
}

//callback 함수
var callback_DeleteProjectSqlIdfyConditionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','SQL 자동 성능 점검 대상 관리 정보를 삭제하였습니다.','info',function(){
			setTimeout(function() {
				getUserName();
				Btn_OnClick($("#detail_form #project_id").combobox('getValue'));
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function Excel_Download() {
	if($('#project_id').val() == ""){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	
	$("#submit_form").attr("action","/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/ExcelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}