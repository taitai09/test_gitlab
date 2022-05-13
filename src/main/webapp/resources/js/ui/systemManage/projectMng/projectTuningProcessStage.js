var checkPerformanceYn;
var checkErrorYn;
var projectFormName;

var clickedBtnName;

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
	
	$("#detail_form #crud_flag").val("C");
});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트',width:"20%",halign:'center',align:'center'},
			{field:'tuning_prgrs_step_seq',title:'튜닝진행단계일련번호',width:"8%",halign:'center',align:'center'},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계명',width:"15%",halign:'center',align:'center'},
			{field:'tuning_prgrs_step_desc',title:'튜닝진행단계 설명',width:"40%",halign:'center',align:'center'},
			{field:'del_yn',title:'삭제여부',width:"5%",halign:'center',align:'center'},
			{field:'project_id',hidden:'true'}
		]],
		onSelect:function(index,row) {
//			$("#detail_form #project_id").val(row.project_id);
//			$("#detail_form #project_nm").textbox('setValue',row.project_nm);
			$("#detail_form #project_id").combobox('setValue',row.project_id);
			$("#detail_form #project_id").combobox('setText',row.project_nm);
			$("#detail_form #tuning_prgrs_step_seq_web").textbox('setValue',row.tuning_prgrs_step_seq);
			$("#detail_form #tuning_prgrs_step_seq").val(row.tuning_prgrs_step_seq);
			$("#detail_form #tuning_prgrs_step_nm").textbox('setValue',row.tuning_prgrs_step_nm);
			$("#detail_form #tuning_prgrs_step_desc").val(row.tuning_prgrs_step_desc);
			$("#detail_form #del_yn").combobox('setValue',row.del_yn);
			$("#detail_form #crud_flag").val("U");

//			$("#detail_form #project_nm").textbox('readonly',true);
			$("#detail_form #project_id").combobox('readonly',true);
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_ShowProjectList() {
//	$("#projectList_form #project_nm").textbox('setValue', '');
	$("#projectList_form #project_id").combobox('setValue', '');
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

function formValidationCheck(){
//	if($("#submit_form #project_nm").textbox('getValue') == ""){
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
	$("#tableList").datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallProjectTuningProcessStageList();
}

function ajaxCallProjectTuningProcessStageList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 튜닝진행단계 조회"," ");
	ajaxCall("/ProjectTuningPrgrsStep/ProjectTuningPrgrsStepList", $("#submit_form"), "POST", callback_ProjectTuningProcessStageListAction);
}

//callback 함수
var callback_ProjectTuningProcessStageListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

var saveTypeText = "";
function Btn_SaveSetting(){
	clickedBtnName = "save";
	
	if($("#detail_form #project_id").combobox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}	
	
	if($('#detail_form #tuning_prgrs_step_nm').textbox('getValue') == '') {
		parent.$.messager.alert('경고','튜닝진행단계명을 입력해 주세요','warning');
		return false;
	}
	
	if(byteLength($('#detail_form #tuning_prgrs_step_nm').textbox('getValue')) > 100) {
		parent.$.messager.alert('경고','튜닝진행단계명은 100Byte 이하로 입력해 주세요.','warning');
		return false;
	}
	
	if(byteLength($('#detail_form #tuning_prgrs_step_desc').val()) > 400) {
		parent.$.messager.alert('경고','튜닝진행단계 설명은 400Byte 이하로 입력해 주세요.','warning');
		return false;
	}
	
	var progressMsg = '';
	var url = '';
	
	if($("#detail_form #crud_flag").val() == 'C') {
		progressMsg = '프로젝트 튜닝진행단계를 등록합니다.';
		url = '/ProjectTuningPrgrsStep/insert';
	} else if($("#detail_form #crud_flag").val() == 'U') {
		progressMsg = '프로젝트 튜닝진행단계를 수정합니다.';
		url = '/ProjectTuningPrgrsStep/update';
	}
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress(progressMsg," ");
	ajaxCall(url, $("#detail_form"), "POST", callback_ProjectWrkjobSave);
}

//callback 함수
var callback_ProjectWrkjobSave = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				fn_ResetField();
				fnSearch();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_ResetField(){
	clickedBtnName = "reset";
	fn_ResetField();
}

function fn_ResetField(){
//	$("#detail_form #project_nm").textbox({readonly:false});
//	$("#detail_form #project_nm").textbox('setValue', '');
	$("#detail_form #project_id").combobox({readonly:false});
	$("#detail_form #project_id").combobox('setValue', '');
	$("#detail_form #tuning_prgrs_step_seq_web").textbox('setValue', '');
	$("#detail_form #tuning_prgrs_step_seq").val('');
	$("#detail_form #tuning_prgrs_step_nm").textbox('setValue', '');
	$("#detail_form #del_yn").combobox('setValue','N');
	$("#detail_form #tuning_prgrs_step_desc").val('');
	$('#detail_form #project_id').combobox("textbox").attr("placeholder","선택");
	
	$("#detail_form #crud_flag").val("C");
}

function Excel_Download() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}
	
	$("#submit_form").attr("action","/ProjectTuningPrgrsStep/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}