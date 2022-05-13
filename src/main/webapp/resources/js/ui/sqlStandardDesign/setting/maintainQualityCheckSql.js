var selectedRowData;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	initializeCombobox1();
	initializeCombobox2();
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			selectedRowData = row;
			setDetailView(row);
		},
		columns:[[
			{field:'qty_chk_idt_cd',title:'품질 점검<br/>지표 코드',width:"05%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_sql',title:'품질 점검 RULE',width:"60%",halign:"center",align:'left'},
			{field:'dml_yn',title:'DML 여부',width:"5%",halign:"center",align:'center'},
			{field:'project_by_mgmt_yn',title:'프로젝트 단위 관리 여부',width:"10%",halign:"center",align:'center'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#detail_form #rowStatus").val("i");
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	
	$("#nextBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	Btn_OnClick();
});

function initializeCombobox1() {
	// 품질점검지표코드 조회
	$('#submit_form #qty_chk_idt_cd').combobox({
		url:"/getQtyChkIdtCd",
		method:"get",
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function initializeCombobox2() {
	$('#detail_form #ui_qty_chk_idt_cd').combobox({
		url:"/getQtyChkIdtCd2",
		method:"get",
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function Btn_SaveSetting(){
	if($("#detail_form  #ui_qty_chk_idt_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 코드를 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #dml_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','DML여부를 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #qty_chk_sql").val() == ""){
		parent.$.messager.alert('','품질 점검 RULE을 입력해 주세요.');
		return false;
	}
	if($("#detail_form #project_by_mgmt_yn").val() == ""){
		parent.$.messager.alert('','프로젝트 단위 관리 여부를 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #rowStatus").val() == 'i') {
		$("#detail_form #qty_chk_idt_cd").val($("#detail_form  #ui_qty_chk_idt_cd").combobox('getValue'));
	}
	
	ajaxCall("/saveMaintainQualityCheckSql",
			$("#detail_form"),
			"POST",
			callback_SaveMaintainQualityCheckSqlAction);
}

//callback 함수
var callback_SaveMaintainQualityCheckSqlAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				initializeCombobox1();
				initializeCombobox2();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteInstance(){
	var row = selectedRowData;
	
	if(row == null) {
		return;
	}
	
	$('#detail_form #qty_chk_idt_cd').val(selectedRowData.qty_chk_idt_cd);
	
	parent.$.messager.confirm('Confirm','품질 점검 RULE 정보를 삭제하시겠습니까?',function(r){
		if (r){
			ajaxCall("/deleteMaintainQualityCheckSql",
					$("#detail_form"),
					"POST",
					callback_DeleteMaintainQualityCheckSqlAction);
		}
	});
}

//callback 함수
var callback_DeleteMaintainQualityCheckSqlAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','품질 점검 RULE 정보를 삭제하였습니다.','info',function(){
			setTimeout(function() {
				initializeCombobox1();
				initializeCombobox2();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setDetailView(selRow){
	$("#detail_form #rowStatus").val("u");
	$("#detail_form #ui_qty_chk_idt_cd").combobox({disabled: true});
	$("#detail_form #ui_qty_chk_idt_cd").combobox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_cd").val(selRow.qty_chk_idt_cd);
	$("#detail_form #dml_yn").combobox("setValue", selRow.dml_yn);
	$("#detail_form #project_by_mgmt_yn").combobox("setValue", selRow.project_by_mgmt_yn);
	$("#detail_form #qty_chk_sql").val(selRow.qty_chk_sql);
}

function Btn_ResetField(){
	$("#tableList").datagrid("unselectAll");
	selectedRowData = null;
	
	$("#detail_form #rowStatus").val("i");
	$("#detail_form #ui_qty_chk_idt_cd").combobox("setValue", "");
	$("#detail_form #ui_qty_chk_idt_cd").textbox({disabled: false});
	$("#detail_form #qty_chk_idt_cd").val("");
	$("#detail_form #dml_yn").combobox("setValue", "");
	$("#detail_form #project_by_mgmt_yn").combobox("setValue", "");
	$("#detail_form #qty_chk_sql").val( "");
}

/*페이징처리시작*/
function fnSetCurrentPage(direction){
	console.log("direction : "+direction);
	var currentPage = $("#submit_form #currentPage").val();
	
	console.log("currentPage : "+currentPage);
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		console.log("currentPage2 : "+currentPage);
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}
	
	Btn_OnClick('P');
}

function Btn_OnClick(val){
	if(!formValidationCheck()){  //현재 없음.
		return;
	}
	
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val('10');
	}
	
	fnSearch();
}

function fnSearch(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	//if(parent.openMessageProgress != undefined) parent.openMessageProgress("업무 분류체계 관리"," ");
	
	ajaxCall("/maintainQualityCheckSql",
			$("#submit_form"),
			"POST",
			callback_MaintainQualityCheckSqlAction);
}

//callback 함수
var callback_MaintainQualityCheckSqlAction = function(result) {
	Btn_ResetField();
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	//if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	fnControlPaging(result);
};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);
	
	var data;
	var dataLength=0;
	
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	
	//페이지를 보여줄지말지 여부를 결정
	if(currentPage > 1){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if(dataLength > 10){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else{
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	
	if(currentPage == 1){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		if(dataLength > pagePerCount){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}
};

function formValidationCheck(){
	return true;
}
/*페이징처리끝*/

function Excel_Download(){
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}
	$("#submit_form").attr("action","/excelDownMaintainQualityCheckSql");
	$("#submit_form").submit();
}

function copy_to_clipboard() {
	if ($("#detail_form #qty_chk_sql").val( ) == ""){
		return;
	}
	
	var copyText = document.getElementById("qty_chk_sql");
	copyText.focus();
	copyText.select();
	document.execCommand("Copy");
	copyText.setSelectionRange(0, 0);
	copyText.scrollTop = 0;
	parent.$.messager.alert('RULE 복사','품질 점검 RULE이 복사되었습니다.');
}

function Btn_OpenPopup() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$('#rulesForWritingSqlQueriesPopup').window("open");
	
	//checkRGBColor();
	//ajaxCallGetRGBColor();
	//showViewer();
}
