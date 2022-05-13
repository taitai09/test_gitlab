$(document).ready(function() {
/*	
	$('#projectListPop').window({
		title : "프로젝트 조회",
		top:getWindowTop(870),
		left:getWindowLeft(950)
	});
*/	
	
	$('#projectListPop').window({
		title : "프로젝트 조회",
		top:50,
		left:getWindowLeft(950)
	});
	
	$("#projectList").datagrid({
		view: myview,
		singleSelect: true,
		onClickRow : function(index,row) {
			setProjectRow(row);
			
			Btn_OnClosePopup("projectListPop");
		},
		columns:[[
			{field:'project_id',title:'프로젝트ID',halign:"center",align:"center"},
			{field:'project_nm',title:'프로젝트명',halign:"center",align:'left'},
			{field:'project_desc',title:'프로젝트 설명',align:"center",align:'left'},
			{field:'del_yn',title:'종료여부',align:"center",align:'center'}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
});

function formValidationCheckPopup(){
	return true;
}

function fnSearchPopup(){
	ajaxCallSearchProjectList();
}

function Btn_SearchProjectList(){
	if(!formValidationCheckPopup()){
		return false;
	}
	
	$("#projectList_form #currentPage").val("1");
	$("#projectList_form #pagePerCount").val("20");
	
	$('#projectList').datagrid('loadData',[]);
	
	ajaxCallSearchProjectList();
}

function ajaxCallSearchProjectList() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('프로젝트 조회','프로젝트를 조회중입니다.');
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	ajaxCall("/SQLAutomaticPerformanceCheck/searchProjectList",
			$("#projectList_form"),
			"POST",
			callback_searchProjectListAddTable);
}

//callback 함수
var callback_searchProjectListAddTable = function(result) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result, "#projectList",true);
	
	fnEnableDisablePagingBtnPopup(dataLength);
}