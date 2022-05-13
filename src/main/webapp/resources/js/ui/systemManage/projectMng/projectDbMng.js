var projectFormName;
var clickedBtnName;
var perfCheckTargetDbid;

$(document).ready(function() {
	$("body").css("visibility", "visible");

	createList();
	
//	$("#submit_form #project_nm").textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				projectFormName = "#submit_form";
//				Btn_ShowProjectList();
//			}
//		}]
//	});
//	
//	$("#detail_form #project_nm").textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				projectFormName = "#detail_form";
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	$('#submit_form #project_id').combobox("textbox").attr("placeholder","선택");
	$('#detail_form #project_id').combobox("textbox").attr("placeholder","선택");
	
	//성능점검원천 DB 조회		
	$('#detail_form #perf_check_original_dbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items) {
			if (items.length){
				//var opts = $(this).combobox('options');
				//$(this).combobox('select', items[0][opts.valueField]);
			}
			
			$('#detail_form #perf_check_original_dbid').combobox("textbox").attr("placeholder","선택");
		},		
		onSelect:function(rec){
		}		
	});
	
	//성능점검대상 DB 조회
	$('#detail_form #perf_check_target_dbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items) {
			if (items.length){
				//var opts = $(this).combobox('options');
				//$(this).combobox('select', items[0][opts.valueField]);
			}
			
			$('#perf_check_target_dbid').combobox("textbox").attr("placeholder","선택");
		},		
		onSelect:function(rec){
			if(rec.dbid == ''){
				return false;
			}else{
				perfCheckTargetDbid = rec.dbid;
			}
		}
	});	

});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트',width:"40%",halign:'center',align:'center'},
			{field:'perf_check_original_db_name',title:'성능점검원천 DB',width:"15%",halign:'center',align:'center'},
			{field:'perf_check_target_db_name',title:'성능점검대상 DB',width:"15%",halign:'center',align:'center'},
			{field:'project_id',hidden:'true'},
			{field:'perf_check_original_dbid',hidden:'true'},
			{field:'perf_check_target_dbid',hidden:'true'}
		]],
		onSelect:function(index,row) {
//			$("#detail_form #project_id").val(row.project_id);
//			$("#detail_form #project_nm").textbox('setValue',row.project_nm);
			$("#detail_form #project_id").combobox('setValue',row.project_id);
			$("#detail_form #project_id").combobox('setText' ,row.project_nm);

			$("#detail_form #perf_check_original_dbid").combobox('setValue',row.perf_check_original_dbid);
			$("#detail_form #perf_check_target_dbid").combobox('setValue',row.perf_check_target_dbid);

			$("#detail_form #project_id").combobox('readonly',true);
			$("#detail_form #perf_check_original_dbid").combobox('readonly',true);
//			$("#detail_form #perf_check_target_dbid").combobox('readonly',true);
			$("#detail_form #crud_flag").val("U");

		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_ShowProjectList() {
	$("#projectList_form #project_nm").textbox('setValue', '');
	$("#projectList_form #del_yn").combobox('setValue','N');
	$("#projectList_form #projectList").datagrid('loadData',[]);
	
	$("#projectListPop").window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$(projectFormName+" #project_nm").textbox("setValue", row.project_nm);
	
	$(projectFormName+" #project_id").val(row.project_id);
	
	
}

function formValidationCheck(){
	if($("#submit_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}	
	return true;
}

function Btn_OnClick(){
	if(!formValidationCheck()){
		return;
	}
	fnSearch();
}

function fnSearch(){
	if($("#submit_form #project_id").combobox('getValue') == ""){
		return false;
	}
	
	$("#tableList").datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallProjectDbList();
}

function ajaxCallProjectDbList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 DB 목록 조회"," ");
	ajaxCall("/ProjectDbList", $("#submit_form"), "POST", callback_ProjectDbList);
}

//callback 함수
var callback_ProjectDbList = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

var saveTypeText = "";
function Btn_SaveSetting(){
	clickedBtnName = "save";
	
//	if($("#detail_form #project_nm").textbox('getValue') == ""){
	if($("#detail_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}	
	if($("#detail_form #perf_check_original_dbid").combobox('getValue') == ""){
		parent.$.messager.alert('경고','성능점검원천 DB를 선택해 주세요','warning');
		return false;
	}	
	if($("#detail_form #perf_check_target_dbid").combobox('getValue') == ""){
		parent.$.messager.alert('경고','성능점검대상 DB를 선택해 주세요','warning');
		return false;
	}
	
	/* modal progress open */
	if($("#detail_form #crud_flag").val() == "C"){
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 DB를 등록합니다."," ");
		saveTypeText = "등록";
	}else{
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 DB를 수정합니다."," ");
		saveTypeText = "수정";
	}
	ajaxCall("/ProjectDb/Save", $("#detail_form"), "POST", callback_ProjectDbSave);
}

function Btn_DeleteSetting(){
//	if($("#detail_form #project_nm").textbox('getValue') == ""){
	if($("#detail_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('경고','삭제할 데이터를 선택해 주세요','warning');
		return false;
	}	
	if($("#detail_form #perf_check_original_dbid").combobox('getValue') == ""){
		parent.$.messager.alert('경고','삭제할 데이터를 선택해 주세요','warning');
		return false;
	}	
	if($("#detail_form #perf_check_target_dbid").combobox('getValue') == ""){
		parent.$.messager.alert('경고','삭제할 데이터를 선택해 주세요','warning');
		return false;
	}
	
	var data = $("#tableList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('경고','삭제할 데이터를 선택해 주세요','warning');
		return false;
	}
	
	parent.$.messager.confirm('확인', '프로젝트 DB를 삭제하시겠습니까?', function(r){
		if (r){
			clickedBtnName = "delete";
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 DB를 삭제합니다."," ");
			ajaxCall("/ProjectDb/Delete", $("#detail_form"), "POST", callback_ProjectDbDelete);
		}
	});
}

//callback 함수
var callback_ProjectDbSave = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 DB를 '+saveTypeText+'하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
			},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 DB 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};
//callback 함수
var callback_ProjectDbInsert = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 DB를 등록하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 DB 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectDbUpdate = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 DB를 수정하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 DB 수정중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectDbDelete = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 DB를 삭제하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 DB 삭제중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_ResetField(){
	clickedBtnName = "reset";
	fn_ResetField();
}

function fn_ResetField(){
	perfCheckTargetDbid = "";
//	$("#detail_form #project_nm").textbox('readonly',false);
//	$("#detail_form #project_nm").textbox({readonly:false});
	$("#detail_form #project_id").combobox('readonly',false);
	$("#detail_form #project_id").combobox({readonly:false});

	$("#detail_form #perf_check_original_dbid").combobox({readonly:false});
	$("#detail_form #perf_check_target_dbid").combobox({readonly:false});

	$("#detail_form #perf_check_original_dbid").combobox('setValue','');
	$("#detail_form #perf_check_target_dbid").combobox('setValue','');

	if(clickedBtnName == "delete" || clickedBtnName == "reset"){
//		$("#detail_form #project_nm").textbox("setValue", "");
		$("#detail_form #project_id").combobox('setValue','');
		$('#detail_form #project_id').combobox("textbox").attr("placeholder","선택");
	}
		
	$("#detail_form #crud_flag").val("C");
	
}

function Excel_Download() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}

	$("#submit_form").attr("action","/ProjectDb/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}