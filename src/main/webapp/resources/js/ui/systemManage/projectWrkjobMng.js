var checkPerformanceYn;
var checkErrorYn;
var projectFormName;

var clickedBtnName;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	createList();
	
	$("#submit_form #project_nm").textbox({
		editable:false,
		icons:[{
			iconCls:'icon-search',
			handler:function() {
				projectFormName = "#submit_form";
				Btn_ShowProjectList();
			}
		}]
	});
	
	$("#detail_form #project_nm").textbox({
		editable:false,
		icons:[{
			iconCls:'icon-search',
			handler:function() {
				projectFormName = "#detail_form";
				Btn_ShowProjectList();
			}
		}]
	});
	//combobox
	// Work Job 조회			
//	$('#detail_form #wrkjob_cd').combobox({
//	    url:"/Common/getWrkJob",
//	    method:"get",
//		valueField:'wrkjob_cd',
//	    textField:'wrkjob_cd_nm',
//	    onChange: function(newValue, oldValue) {
//	    	if(newValue != '') {
//	    		$('#wrkjob_cd_ui').textbox("setValue", newValue);
//	    	}
//	    },
//		onLoadError: function(){
//			parent.$.messager.alert('','업무 조회중 오류가 발생하였습니다.');
//		}
//	});	
	//combotree
	// 업무 리스트 조회	
	$('#detail_form #wrkjob_cd').combotree({
	    url:"/Common/getWrkJobCd",
	    method:'get',
	    valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
	    onChange: function(newValue, oldValue) {
	    	if(newValue != '') {
	    		$('#wrkjob_cd_ui').textbox("setValue", newValue);
	    	}
	    },
	    onLoadError: function(){
			parent.$.messager.alert('','업무 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트',width:'40%',halign:'center',align:'center'},
			{field:'wrkjob_cd_nm',title:'업무',width:'20%',halign:'center',align:'center'},
			{field:'project_id',hidden:'true'},
			{field:'wrkjob_cd',hidden:'true'}
		]],
		onSelect:function(index,row) {
			console.log("index :",index);
			console.log("row :",row);
			console.log("project_nm :",row.project_nm);
			console.log("project_id :",row.project_id);
			console.log("wrkjob_cd :",row.wrkjob_cd);
//			$("#detail_form #project_id").val(row.project_id);
//			$("#detail_form #wrkjob_cd").val(row.wrkjob_cd);
			$("#detail_form #project_id").val(row.project_id);
			$("#detail_form #project_nm").textbox('setValue',row.project_nm);
//			$("#detail_form #wrkjob_cd").combobox('setValue',row.wrkjob_cd);
			$("#detail_form #wrkjob_cd").combotree('setValue',row.wrkjob_cd);
			$("#crud_flag").val("U");

			$("#detail_form #project_nm").textbox('readonly',true);
			$("#detail_form #wrkjob_cd").combotree('readonly',true);
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
	
	$(projectFormName+" #dbid").val(row.dbid);
	
}

function formValidationCheck1(){
	if($("#detail_form #wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('경고','업무를 선택해 주세요.','warning');
		return false;
	}
	return true;
}

function formValidationCheck(){
	if($("#submit_form #project_nm").textbox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}	
	return true;
}

function Btn_OnClick(){
	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	$("#submit_form #currentPage").val("1");
	$("#submit_form #pagePerCount").val("15");
	
	if(!formValidationCheck()){
		return;
	}
	fnSearch();
}

function fnSearch(){
	$("#tableList").datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallProjectWrkjobList();
}

function ajaxCallProjectWrkjobList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 업무 목록 조회"," ");
	ajaxCall("/ProjectWrkjobList", $("#submit_form"), "POST", callback_ProjectWrkjobList);
}

//callback 함수
var callback_ProjectWrkjobList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	try{
		var data = JSON.parse(result);
		var dataLength=0;
		dataLength = data.dataCount4NextBtn;
		console.log("dataLength:"+dataLength);
		fnEnableDisablePagingBtn(dataLength);
	}catch(e){
		console.log("e.message:"+e.message);
	}

};

function detailFormValidationCheck(section){
	var message = '';
	
	if($("#detail_form #project_nm").textbox('getValue') == ""){
		if(section == 1) {
			message = '프로젝트를 선택해 주세요';
		} else if(section == 2) {
			message = '삭제할 데이터를 선택해 주세요';
		}
		
		parent.$.messager.alert('경고',message,'warning');
		return false;
	}
	
	if($("#detail_form #wrkjob_cd").textbox('getValue') == ""){
		if(section == 1) {
			message = '업무를 선택해 주세요';
		} else if(section == 2) {
			message = '삭제할 데이터를 선택해 주세요';
		}
		
		parent.$.messager.alert('경고',message,'warning');
		return false;
	}
	
	if(section == 2) {
		var data = $("#tableList").datagrid('getSelected');
		
		if(data == null) {
			parent.$.messager.alert('경고','삭제할 데이터를 선택해 주세요','warning');
			return false;
		}
	}
	
	return true;
}

var saveTypeText = "";
function Btn_SaveSetting(){
	if(!detailFormValidationCheck(1)) {
		return;
	}
	
	clickedBtnName = "save";
	/* modal progress open */
	if($("#detail_form #crud_flag").val() == "C"){
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 업무를 등록합니다."," ");
		saveTypeText = "등록";
	}else{
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 업무를 수정합니다."," ");
		saveTypeText = "수정";
	}
	ajaxCall("/ProjectWrkjob/Save", $("#detail_form"), "POST", callback_ProjectWrkjobSave);
}

function Btn_DeleteSetting(){
	if(!detailFormValidationCheck(2)) {
		return;
	}
	
	parent.$.messager.confirm('확인', '프로젝트 업무를 삭제하시겠습니까?', function(r){
		if (r){
			clickedBtnName = "delete";
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 업무를 삭제합니다.","");
			ajaxCall("/ProjectWrkjob/Delete", $("#detail_form"), "POST", callback_ProjectWrkjobDelete);
		}
	});
}

//callback 함수
var callback_ProjectWrkjobSave = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 업무를 '+saveTypeText+'하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
			},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 업무 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};
//callback 함수
var callback_ProjectWrkjobInsert = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 업무를 등록하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 업무 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobUpdate = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 업무를 수정하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 업무 수정중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobDelete = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 업무를 삭제하였습니다.','info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 업무 삭제중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_ResetField(){
	clickedBtnName = "reset";
	fn_ResetField();
}

function fn_ResetField(){
	console.log("fn_ResetField start");
	
	$("#detail_form #project_nm").textbox('readonly',false);
	$("#detail_form #wrkjob_cd").combotree('readonly',false);
	$("#detail_form #project_nm").textbox({readonly:false});
	$("#detail_form #wrkjob_cd").combotree({readonly:false});
	
	if(clickedBtnName == "delete" || clickedBtnName == "reset"){
		$("#detail_form #project_nm").textbox("setValue", "");
		$("#detail_form #project_id").val("");
	}
		
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #wrkjob_cd").combotree("setValue", "");
	
	$("#detail_form #dbid").val("");

	console.log("fn_ResetField end");
}

function Excel_Download() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}
	
	$("#submit_form").attr("action","/ProjectWrkjob/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}