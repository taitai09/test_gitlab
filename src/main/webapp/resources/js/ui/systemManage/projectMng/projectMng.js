var currentPagePerCount = 12;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#detail_form #project_id").textbox({readonly : true});
	$("#detail_form #project_id").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_create_dt").textbox({readonly : true});
	$("#detail_form #project_create_dt").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_creater_id").textbox({readonly : true});
	$("#detail_form #project_creater_id").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #user_nm").textbox({readonly : true});
	$("#detail_form #user_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #del_dt").textbox({readonly : true});
	$("#detail_form #del_dt").textbox("textbox").css("background-color","#F4F4F4");
//	$("#detail_form #del_yn").textbox("setValue","N");
	
//	Btn_OnClick();
	getTableList();

	var t = $('#submit_form #project_nm');
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
				{field:'project_id',title:'프로젝트ID',width:"5%",halign:"center",align:"center"},
				{field:'project_nm',title:'프로젝트명',width:"12%",halign:"center",align:'left'},
				{field:'project_desc',title:'프로젝트설명',width:"30%",halign:"center",align:'left'},
				{field:'project_create_dt',title:'등록일시',width:"8%",halign:"center",align:'center'},
				{field:'project_creater_id',title:'등록자ID',width:"8%",halign:"center",align:'center'},
				{field:'user_nm',title:'등록자명',width:"8%",halign:"center",align:'center'},
				{field:'del_yn',title:'종료여부',width:"5%",halign:"center",align:'center'},
				{field:'del_dt',title:'종료일시',width:"8%",halign:"center",align:'center'}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};


//callback 함수
var callback_BtnOnclickAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
		if($("#detail_form #project_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','프로젝트명을 입력해 주세요.');
			return false;
		}
		if($("#detail_form #project_desc").textbox('getValue') == ""){
			parent.$.messager.alert('','프로젝트 설명을 입력해 주세요.');
			return false;
		}
		if($("#detail_form #del_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','종료여부를 선택해 주세요.');
			return false;
		}
		
		if($("#detail_form #project_nm").textbox('getValue').length > 200 ){
			parent.$.messager.alert('','프로젝트명의 길이가 200자 이내로 입력해 주세요.');
			return false;
		}
		
		if($("#detail_form #project_desc").textbox('getValue').length > 500){
			parent.$.messager.alert('','프로젝트 설명의 길이를 500자 이내로 입력해 주세요.');
			return false;
		}

		ajaxCall("/ProjectMng/Save",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if($('#detail_form #crud_flag').val() == 'U') {
		message = "수정 되었습니다.";
	}
	
	if(result.result){
		parent.$.messager.alert('',message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//function Btn_DeleteSetting(){
//	
//	parent.$.messager.confirm('', '정말 삭제 하시겠습니까?', function(check) {
//		if (check) {
//		
//			ajaxCall("/Mqm/EntityManagement/Delete",
//					$("#detail_form"),
//					"POST",
//					callback_DeleteSettingAction);	
//		}
//	});
//		
//}
////callback 함수
//var callback_DeleteSettingAction = function(result) {
//	if(result.result){
//		parent.$.messager.alert('','삭제가 완료 되었습니다.','info',function(){
//			setTimeout(function() {			
//				Btn_OnClick();
//				Btn_ResetField();
//			},1000);	
//		});
//	}else{
//		parent.$.messager.alert('',result.message);
//	}
//};


function setDetailView(selRow){

	$("#detail_form #crud_flag").val("U");
	$("#detail_form #project_id").textbox({readonly : true});
	$("#detail_form #project_id").textbox("setValue", selRow.project_id);
	$("#detail_form #project_id").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_nm").textbox("setValue", selRow.project_nm);
	$("#detail_form #project_desc").textbox("setValue", selRow.project_desc);
	$("#detail_form #project_create_dt").textbox({readonly : true});
	$("#detail_form #project_create_dt").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_create_dt").textbox("setValue", selRow.project_create_dt);
	$("#detail_form #project_creater_id").textbox({readonly : true});
	$("#detail_form #project_creater_id").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_creater_id").textbox("setValue", selRow.project_creater_id);
	$("#detail_form #user_nm").textbox({readonly : true});
	$("#detail_form #user_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #user_nm").textbox("setValue", selRow.user_nm);
	$("#detail_form #del_yn").combobox("setValue", selRow.del_yn);
	$("#detail_form #del_dt").textbox({readonly : true});
	$("#detail_form #del_dt").textbox("setValue", selRow.del_dt);
	$("#detail_form #del_dt").textbox("textbox").css("background-color","#F4F4F4");
	
}

function Btn_ResetField(){
	
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #project_id").textbox({readonly : true});
	$("#detail_form #project_id").textbox("setValue", "");
	$("#detail_form #project_id").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_nm").textbox("setValue", "");
	$("#detail_form #project_desc").textbox("setValue", "");
	$("#detail_form #project_create_dt").textbox({readonly : true});
	$("#detail_form #project_create_dt").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #project_create_dt").textbox("setValue", "");
	$("#detail_form #project_creater_id").textbox("setValue", $("#submit_form #user_id").val());
	$("#detail_form #user_nm").textbox("setValue", $("#submit_form #user_nm").val());
	$("#detail_form #del_yn").textbox("setValue","N");
	$("#detail_form #del_dt").textbox({readonly : true});
	$("#detail_form #del_dt").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #del_dt").textbox("setValue", "");
}

function Excel_Download(){

	$("#submit_form").attr("action","/ProjectMng/ExcelDown");
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
	
		ajaxCall("/ProjectMng",
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
