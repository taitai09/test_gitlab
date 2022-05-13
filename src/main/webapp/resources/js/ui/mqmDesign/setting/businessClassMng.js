var colorIdArry = new Array();
var colorValArry = new Array(); 

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'lib_nm',title:'라이브러리명',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'model_nm',title:'모델명',width:"10%",halign:"center",align:'left',sortable:"true"},
			{field:'sub_nm',title:'주제영역명',width:"10%",halign:"center",align:'left',sortable:"true"},
			{field:'sys_nm',title:'시스템명',width:"10%",halign:"center",align:'left'},
			{field:'sys_cd',title:'시스템코드',width:"5%",halign:"center",align:'center'},
			{field:'main_biz_cls_nm',title:'업무대분류명',width:"10%",halign:"center",align:'left'},
			{field:'main_biz_cls_cd',title:'업무대분류코드',width:"5%",halign:"center",align:'center'},
			{field:'mid_biz_cls_nm',title:'업무중분류명',width:"10%",halign:"center",align:'left'},
			{field:'mid_biz_cls_cd',title:'업무중분류코드',width:"5%",halign:"center",align:'center'},
			{field:'biz_desc',title:'주제영역정의',width:"15%",halign:"center",align:'left'},
			{field:'remark',title:'비고',width:"10%",halign:"center",align:'left'}
			]],

	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
	});
	
	var t = $("#submit_form #lib_nm");
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});
	var t1 = $("#submit_form #model_nm");
	t1.textbox('textbox').bind('keyup', function(e){
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

function Btn_SaveSetting(){
	if($("#detail_form #lib_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','라이브러리명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #model_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','모델명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #sub_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','주제영역명을 입력해 주세요.');
		return false;
	}
	
	ajaxCall("/Mqm/BusinessClassMng/Save",
			$("#detail_form"),
			"POST",
			callback_SaveInstanceAction);		
}

//callback 함수
var callback_SaveInstanceAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','업무 분류체계 정보 저장이 완료 되었습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteInstance(){
	if($("#detail_form #lib_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','라이브러리명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #model_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','모델명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #sub_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','주제영역명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #sys_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','시스템명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #main_biz_cls_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','업무대분류명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #mid_biz_cls_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','업무중분류명을 입력해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('Confirm','업무 분류체계 정보를 삭제하시겠습니까?',function(r){
	    if (r){
	        ajaxCall("/Mqm/BusinessClassMng/Delete",
	        		$("#detail_form"),
	        		"POST",
	        		callback_DeleteInstanceAction);		
	    }
	});

}

//callback 함수
var callback_DeleteInstanceAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','업무 분류체계 정보를 삭제하였습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	
	//$("#detail_form #qty_chk_idt_cd").textbox({readonly:true});
	//$("#detail_form #qty_chk_idt_cd").textbox("textbox").css("background-color","#F4F4F4");
	
	$("#detail_form #lib_nm").textbox({readonly:true});
	$("#detail_form #lib_nm").textbox("setValue", selRow.lib_nm);	
	$("#detail_form #lib_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #model_nm").textbox({readonly:true});
	$("#detail_form #model_nm").textbox("setValue", selRow.model_nm);
	$("#detail_form #model_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #sub_nm").textbox({readonly:true});
	$("#detail_form #sub_nm").textbox("setValue", selRow.sub_nm);
	$("#detail_form #sub_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #sys_nm").textbox({readonly:true});
	$("#detail_form #sys_nm").textbox("setValue", selRow.sys_nm);
	$("#detail_form #sys_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #sys_cd").textbox("setValue", selRow.sys_cd);
	$("#detail_form #main_biz_cls_nm").textbox({readonly:true});
	$("#detail_form #main_biz_cls_nm").textbox("setValue", selRow.main_biz_cls_nm);
	$("#detail_form #main_biz_cls_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #main_biz_cls_cd").textbox("setValue", selRow.main_biz_cls_cd);
	$("#detail_form #mid_biz_cls_nm").textbox("readonly", true);
	$("#detail_form #mid_biz_cls_nm").textbox("setValue", selRow.mid_biz_cls_nm);
	$("#detail_form #mid_biz_cls_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #mid_biz_cls_cd").textbox("setValue", selRow.mid_biz_cls_cd);
	$("#detail_form #biz_desc").textbox("setValue", selRow.biz_desc);
	$("#detail_form #remark").textbox("setValue", selRow.remark);
}

function Btn_ResetField(){
	$("#detail_form #crud_flag").val("C");
	
	$("#detail_form #lib_nm").textbox({readonly:false});
	$("#detail_form #lib_nm").textbox("setValue", "");
	$("#detail_form #model_nm").textbox("setValue", "");
	$("#detail_form #model_nm").textbox({readonly:false});
	$("#detail_form #sub_nm").textbox("setValue", "");
	$("#detail_form #sub_nm").textbox({readonly:false});
	$("#detail_form #sys_nm").textbox("setValue", "");
	$("#detail_form #sys_nm").textbox({readonly:false});
	$("#detail_form #sys_cd").textbox("setValue", "");
	$("#detail_form #main_biz_cls_nm").textbox({readonly:false});
	$("#detail_form #main_biz_cls_nm").textbox("setValue", "");
	$("#detail_form #main_biz_cls_cd").textbox("setValue", "");
	$("#detail_form #mid_biz_cls_nm").textbox({readonly:false});
	$("#detail_form #mid_biz_cls_nm").textbox("setValue", "");
	$("#detail_form #mid_biz_cls_cd").textbox("setValue", "");
	$("#detail_form #biz_desc").textbox("setValue", "");
	$("#detail_form #remark").textbox("setValue", "");
	
	$("#tableList").datagrid('unselectAll');
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
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("업무 분류체계 관리"," "); 
		
		ajaxCall("/Mqm/BusinessClassMng",
				$("#submit_form"),
				"POST",
				callback_BusinessClassMngAction);	
}

//callback 함수
var callback_BusinessClassMngAction = function(result) {
	Btn_ResetField();
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

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
	/*
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
			($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
			parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
			return false;
		}
	*/
	return true;
}
/*페이징처리끝*/


function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}
	$("#submit_form").attr("action","/Mqm/BusinessClassMng/ExcelDown");
	$("#submit_form").submit();
}


//일괄업로드 팝업
function Excel_Upload(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	//parent.$("#sql_text_pop").val("");
	//parent.$("#bindPopTbl tbody tr").remove();
	$('#businessClassMng_excel_upload_popup').window("open");
//	$('#excelUploadPop').window("open");
}