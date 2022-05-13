var checkPerformanceYn;
var checkErrorYn;
var projectFormName;

var lib_nm="";
var model_nm="";
var sub_nm="";

var clickedBtnName;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createList();
	selectAllLibNm();
	
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
//		}],
//		onChange:function(value){
//		}
//	});
	
});

// 라이브러리명 조회
function selectAllLibNm(){
	$('#lib_nm').combobox({
	    url:"/Common/getAllLibNm?isChoice=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items) {
			if(lib_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
		},
		onClick:function(){
			model_nm = "";
			sub_nm = "";
		},
		onLoadError: function(){
			parent.$.messager.alert('','라이브러리명 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	console.log("rec.cd :["+rec.cd+"] rec.cd_nm :["+rec.cd_nm+"]");
	    	$("#detail_form #model_nm").combobox("loadData","[]");
	    	$("#detail_form #sub_nm").combobox("loadData","[]");
	    	$("#detail_form #model_nm").combobox("setValue", "");
	    	$("#detail_form #sub_nm").combobox("setValue", "");
	    	$("#detail_form #model_nm").combobox("setText", "선택");
	    	$("#detail_form #sub_nm").combobox("setText", "선택");
	    	if(rec.cd != ""){
	    		lib_nm = rec.cd_nm;
	    		selectAllModelNm(rec.cd_nm);
	    	}
	    }	    
	});
}

// 모델명 조회			
function selectAllModelNm(){
	$('#model_nm').combobox({
		url:"/Common/getAllModelNm?isChoice=Y"+"&lib_nm="+lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("model_nm :"+model_nm);
			if(model_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				$(this).combobox('setValue', model_nm);
			}
		},			
		onLoadError: function(){
			parent.$.messager.alert('','모델명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
	    	console.log("rec.cd :["+rec.cd+"] rec.cd_nm :["+rec.cd_nm+"]");
	    	if(rec.cd != ""){
				model_nm = rec.cd_nm;
				selectAllSubNm();
	    	}else{
		    	$("#detail_form #sub_nm").combobox("loadData","[]");
		    	$("#detail_form #sub_nm").combobox("setValue", "");
	    	}
		}	    
	});	
}

// 주제영역명 조회
function selectAllSubNm(){
	$('#sub_nm').combobox({
		url:"/Common/getAllSubNm?isChoice=Y"+"&lib_nm="+lib_nm+"&model_nm="+model_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("sub_nm :"+sub_nm);
			if(sub_nm == ""){
				console.log("items.length :"+items.length);
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				if(sub_nm == null){
					$(this).combobox('setValue', "");
					$(this).combobox('setText', "");
				}else{
					$(this).combobox('setValue', sub_nm);
				}
			}
		},			
		onLoadError: function(){
			parent.$.messager.alert('','주제영역명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
	    	console.log("rec.cd :["+rec.cd+"] rec.cd_nm :["+rec.cd_nm+"]");
		}	    
	});	
}

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트',halign:'center',align:'center'},
			{field:'lib_nm',title:'라이브러리명',halign:'center',align:'center'},
			{field:'model_nm',title:'모델명',halign:'center',align:'center'},
			{field:'sub_nm',title:'주제영역명',halign:'center',align:'center'},
			{field:'project_id',title:'프로젝트ID',hidden:'true'},
			{field:'check_target_seq',title:'점검대상일련번호',hidden:'true'},
			{field:'project_check_target_type_cd',title:'프로젝트점검대상유형코드',hidden:'true'}
		]],
		onSelect:function(index,row) {
			lib_nm = row.lib_nm;
			model_nm = row.model_nm;
			sub_nm = row.sub_nm;
			console.log("sub_nm :["+row.sub_nm+"]");
			$("#detail_form #project_id").combobox('setText',$("#submit_form #project_id").combobox("getText"));
			$("#detail_form #project_id").combobox('setValue',$("#submit_form #project_id").combobox("getValue"));
//			$("#detail_form #project_nm").textbox('setValue',row.project_nm);
			$("#detail_form #lib_nm").combobox('setValue','');
			$("#detail_form #lib_nm").combobox('setValue',row.lib_nm);
			$("#detail_form #check_target_seq").val(row.check_target_seq);
			$("#detail_form #project_check_target_type_cd").val(row.project_check_target_type_cd);
			
			$(".fa-save").parent().parent().parent().hide();
			$("#crud_flag").val("U");

			$("#detail_form #project_id").textbox('readonly',true);
			$("#detail_form #lib_nm").combobox('readonly',true);
			$("#detail_form #model_nm").combobox('readonly',true);
			$("#detail_form #sub_nm").combobox('readonly',true);
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
	console.log("row.project_id :"+row.project_id);
	$(projectFormName+" #project_id").val(row.project_id);
	console.log("#project_id :"+$(projectFormName+" #project_id").val());
	
	Project_ResetField();
	selectAllLibNm();
	
}

function formValidationCheck(){
	if($("#submit_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}	
	return true;
}

function Btn_OnClick(){
	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	$("#detail_form #currentPage").val("1");
	$("#detail_form #pagePerCount").val("20");
	
	if(!formValidationCheck()){
		return;
	}

	Project_ResetField();
	selectAllLibNm();

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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 모델 목록 조회"," ");
	ajaxCall("/ProjectModelList", $("#submit_form"), "POST", callback_ProjectWrkjobList);
}

//callback 함수
var callback_ProjectWrkjobList = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

var saveTypeText = "";
function Btn_SaveSetting(){
	clickedBtnName = "save";
//	if($("#detail_form #project_nm").textbox('getValue') == ""){
//		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
//		return false;
//	}
	
	if($("#detail_form #project_id").val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}

	if($("#detail_form #lib_nm").combobox('getValue') == ""){
		parent.$.messager.alert('경고','라이브러리명을 선택해 주세요','warning');
		return false;
	}

	if($("#detail_form #model_nm").combobox('getValue') == ""){
		parent.$.messager.alert('경고','모델명을 선택해 주세요','warning');
		return false;
	}

	/* modal progress open */
//	if($("#detail_form #crud_flag").val() == "C"){
//		saveTypeText = "등록";
//		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 모델을 등록합니다."," ");
//	}else{
//		saveTypeText = "수정";
//		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 모델을 수정합니다."," ");
//	}
	if($("#detail_form #crud_flag").val() == "C"){
		saveTypeText = "등록";
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 모델을 등록합니다."," ");
	}else if($("#detail_form #crud_flag").val() == "U"){
		parent.$.messager.alert('경고','모든 값이 PK이므로 수정이 불가능합니다.','warning');
		return false;		
	}
	ajaxCall("/ProjectModel/Save", $("#detail_form"), "POST", callback_ProjectWrkjobSave);
}

function Btn_DeleteSetting(){
	clickedBtnName = "delete";
	/*프로젝트ID*/
	if($("#detail_form #project_id").val() == ""){
		parent.$.messager.alert('경고','삭제할 데이터를 그리드에서 선택해 주세요','warning');
		return false;		
	}
	 /*점검대상일련번호*/
	if($("#detail_form #check_target_seq").val() == ""){
		parent.$.messager.alert('경고','삭제할 데이터를 그리드에서 선택해 주세요','warning');
		return false;		 
	}
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 모델을 삭제합니다."," ");
	ajaxCall("/ProjectModel/Delete", $("#detail_form"), "POST", callback_ProjectWrkjobDelete);
}

//callback 함수
var callback_ProjectWrkjobSave = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 모델을 '+saveTypeText+'하였습니다.','info',function(){
			setTimeout(function() {			
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 모델 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobInsert = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 모델을 등록하였습니다.','info',function(){
			setTimeout(function() {			
				fn_ResetField();
				fnSearch();
			},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 모델 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobUpdate = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','프로젝트 모델을 수정하였습니다.','info',function(){
			setTimeout(function() {			
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 모델 수정중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobDelete = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 모델을 삭제하였습니다.','info',function(){
			setTimeout(function() {			
				fn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','프로젝트 모델 삭제중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_ResetField(){
	clickedBtnName = "reset";
	fn_ResetField();
}

function fn_ResetField(){
	$(".fa-save").parent().parent().parent().show();
	
	$("#detail_form #crud_flag").val("C");
	
	$("#detail_form #project_id").textbox('readonly',false);
	$("#detail_form #project_id").textbox({readonly:false});
	
	if(clickedBtnName == "delete" || clickedBtnName == "reset"){
//		$("#detail_form #project_nm").textbox("setValue", "");
		$("#detail_form #project_id").combobox("setText", "선택");
	}
	
	$("#detail_form #model_nm").combobox("loadData","[]");
	$("#detail_form #sub_nm").combobox("loadData","[]");

	$("#detail_form #lib_nm").combobox("setValue", "");
	$("#detail_form #model_nm").combobox("setValue", "");
	$("#detail_form #sub_nm").combobox("setValue", "");
	
	$("#detail_form #lib_nm").combobox("setText", "선택");
	$("#detail_form #model_nm").combobox("setText", "선택");
	$("#detail_form #sub_nm").combobox("setText", "선택");
	
	$("#detail_form #lib_nm").combobox('readonly',false);
	$("#detail_form #model_nm").combobox('readonly',false);
	$("#detail_form #sub_nm").combobox('readonly',false);
	
	$("#detail_form #check_target_seq").val("");
	$("#detail_form #project_check_target_type_cd").val("");

	$("#tableList").datagrid('unselectAll');
}

function Project_ResetField(){
	console.log("Project_ResetField start");
	
	lib_nm="";
	model_nm="";
	sub_nm="";
	
	$("#detail_form #project_id").combobox("setValue", $("#submit_form #project_id").combobox("getText"));
	$("#detail_form #crud_flag").val("C");
	
	$("#detail_form #model_nm").combobox("loadData","[]");
	$("#detail_form #sub_nm").combobox("loadData","[]");
	
	$("#detail_form #lib_nm").combobox("setValue", "");
	$("#detail_form #model_nm").combobox("setValue", "");
	$("#detail_form #sub_nm").combobox("setValue", "");
	
	
	$("#detail_form #check_target_seq").val("");
	$("#detail_form #project_check_target_type_cd").val("");
	
	console.log("Project_ResetField end");
}

function Excel_Download() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}	
	$("#submit_form").attr("action","/ProjectModel/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}