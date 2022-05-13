var currentPagePerCount = 10;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
//	Btn_OnClick();
	getTableList();

	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	
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
});

//changeList
function getTableList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'ent_type_cd',title:'엔터티 유형',width:"10%",halign:"center",align:"center"},
				{field:'ref_ent_type_nm',title:'참조 엔터티 PREFIX',width:"10%",halign:"center",align:'center'},
				{field:'ent_type_nm',title:'엔터티 SUFFIX',width:"10%",halign:"center",align:'center'},
				{field:'tbl_type_nm',title:'테이블 유형',width:"10%",halign:"center",align:'center'},
				{field:'tbl_type_cd',title:'테이블 유형 코드',width:"10%",halign:"center",align:'center'},
				{field:'ent_type_desc',title:'설명',width:"50%",halign:"center",align:'left'}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		
};


/*function Btn_OnClick(){
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 지표 관리"," ");
	
		ajaxCall("/Mqm/EntityManagement",
				$("#submit_form"),
				"POST",
				callback_BtnOnclickAction);
}*/

//callback 함수
var callback_BtnOnclickAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
		if($("#detail_form #ent_type_cd").textbox('getValue') == ""){
			parent.$.messager.alert('','엔터티 유형을 입력해 주세요.');
			return false;
		}
		if($("#detail_form #ent_type_cd").textbox('getValue').length > 10){
			parent.$.messager.alert('','엔터티 유형의 길이를 10자 이내로 입력해 주세요.');
			return false;
		}
		
//		if($("#detail_form #ref_ent_type_nm").textbox('getValue') == ""){
//			parent.$.messager.alert('','엔터티 PREFIX를 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #ref_ent_type_nm").textbox('getValue').length > 5){
			parent.$.messager.alert('','참조 엔터티 PREFIX의 길이를 길이를 5자 이내로 입력해 주세요.');
			return false;
		}
		
//		if($("#detail_form #ent_type_nm").textbox('getValue') == ""){
//			parent.$.messager.alert('','엔터티 SUFFIX를 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #ent_type_nm").textbox('getValue').length > 10){
			parent.$.messager.alert('','엔터티 SUFFIX의 길이를 10자 이내로 입력해 주세요.');
			return false;
		}
		
//		if($("#detail_form #tbl_type_nm").textbox('getValue') == ""){
//			parent.$.messager.alert('','테이블 유형을 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #tbl_type_nm").textbox('getValue').length > 10){
			parent.$.messager.alert('','테이블 유형의 길이를 10자 이내로 입력해 주세요.');
			return false;
		}
		
//		if($("#detail_form #tbl_type_cd").textbox('getValue') == ""){
//			parent.$.messager.alert('','테이블 유형 코드를 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #tbl_type_cd").textbox('getValue').length > 10){
			parent.$.messager.alert('','테이블 유형 코드의 길이를 10자 이내로 입력해 주세요.');
			return false;
		}
		
//		if($("#detail_form #ent_type_desc").textbox('getValue') == ""){
//			parent.$.messager.alert('','설명을 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #ent_type_desc").textbox('getValue').length > 2000){
			parent.$.messager.alert('','설명의 길이를 2000자 이내로 입력해 주세요.');
			return false;
		}
		
		ajaxCall("/Mqm/EntityManagement/Save",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteSetting(){
	
	parent.$.messager.confirm('', '정말 삭제 하시겠습니까?', function(check) {
		if (check) {
		
			ajaxCall("/Mqm/EntityManagement/Delete",
					$("#detail_form"),
					"POST",
					callback_DeleteSettingAction);	
		}
	});
		
}
//callback 함수
var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제가 완료 되었습니다.','info',function(){
			setTimeout(function() {			
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){

	$("#detail_form #crud_flag").val("U");
	$("#detail_form #ent_type_cd").textbox({readonly : true});
	$("#detail_form #ent_type_cd").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #ent_type_cd").textbox("setValue", selRow.ent_type_cd);
	$("#detail_form #ref_ent_type_nm").textbox("setValue", selRow.ref_ent_type_nm);
	$("#detail_form #ent_type_nm").textbox("setValue", selRow.ent_type_nm);
	$("#detail_form #tbl_type_nm").textbox("setValue", selRow.tbl_type_nm);
	$("#detail_form #tbl_type_cd").textbox("setValue", selRow.tbl_type_cd);
	$("#detail_form #ent_type_desc").textbox("setValue", selRow.ent_type_desc);
	
}

function Btn_ResetField(){
	
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #ent_type_cd").textbox({readonly : false});
	$("#detail_form #ent_type_cd").textbox("setValue", "");
	$("#detail_form #ref_ent_type_nm").textbox("setValue", "");
	$("#detail_form #ent_type_nm").textbox("setValue", "");
	$("#detail_form #tbl_type_nm").textbox("setValue", "");
	$("#detail_form #tbl_type_cd").textbox("setValue", "");
	$("#detail_form #ent_type_desc").textbox("setValue", "");
	
	$("#tableList").datagrid('unselectAll');
}

function Excel_Download(){

	$("#submit_form").attr("action","/Mqm/EntityManagement/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}


/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#submit_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}

	Btn_OnClick('P');
}

function Btn_OnClick(val){

	if(!formValidationCheck()){
		return false;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
	}
	fnSearch();
	$("#call_from_parent").val("N");
	$("#call_from_child").val("N");
}

function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("엔터티 유형 관리"," ");
	
		ajaxCall("/Mqm/EntityManagement",
				$("#submit_form"),
				"POST",
				callback_BtnOnclickAction);
}

//callback 함수
var callback_BtnOnclickAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);  //페이버튼세팅
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
	
/*	if($("#submit_form #search_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해주세요.');
		return false;
	}*/
	return true;
}
/*페이징처리끝*/
