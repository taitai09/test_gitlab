var currentPagePerCount = 10;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 모델링 단계(검색영역)
	$('#submit_form #mdi_pcs_cd').combobox({
		url:"/Common/getCommonCode?isAll=Y&grp_cd_id=M0001",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 품질지표 유형(검색영역)
	$('#submit_form #qty_chk_tp_cd').combobox({
		url:"/Common/getCommonCode?isAll=Y&grp_cd_id=M0002",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 모델링 단계
	$('#detail_form #mdi_pcs_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=M0001",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 품질지표 유형
	$('#detail_form #qty_idt_tp_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=M0002",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 품질점검 유형
	$('#detail_form #qty_chk_tp_cd').combobox({
		url:"/Common/getCommonCode?&grp_cd_id=M0003",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
//	Btn_OnClick();
	getTableList();

	var t = $('#qty_chk_idt_nm');
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
				{field:'mdi_pcs_nm',title:'모델링 단계',width:"10%",halign:"center",align:"center"},
				{field:'qty_chk_idt_cd',title:'품질점검 지표코드',width:"10%",halign:"center",align:'center'},
				{field:'qty_chk_idt_nm',title:'품질점검 지표명',width:"10%",halign:"center",align:'center'},
				{field:'qty_idt_tp_nm',title:'품질지표 유형',width:"10%",halign:"center",align:'center'},
				{field:'qty_chk_tg_yn',title:'품질점검 대상여부',width:"10%",halign:"center",align:'center'},
				{field:'qty_chk_tp_nm',title:'품질점검 유형',width:"10%",halign:"center",align:'center'},
				{field:'srt_ord',title:'정렬순서',width:"10%",halign:"center",align:'center'},
				{field:'qty_chk_cont',title:'품질점검내용',width:"30%",halign:"center",align:'left'},
				{field:'slv_rsl_cont',title:'해결방안내용',width:"30%",halign:"center",align:'left'},
				{field:'qty_chk_result_tbl_nm',title:'품질점검결과 테이블명',width:"10%",halign:"center",align:'center'},
				{field:'excel_output_yn',title:'엑셀출력 대상여부',width:"10%",halign:"center",align:'center'},
				{field:'output_start_row',title:'엑셀출력 시작행',width:"10%",halign:"center",align:'center'},
				{field:'output_start_col',title:'엑셀출력 시작열',width:"10%",halign:"center",align:'center'},
				{field:'mdi_pcs_cd',hidden:true},
				{field:'qty_idt_tp_cd',hidden:true},
				{field:'qty_chk_tp_cd',hidden:true},
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
	
		ajaxCall("/Mqm/QualityCheckManagement",
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
		
		if($("#detail_form #mdi_pcs_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','모델링 단계를 선택해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_idt_cd").textbox('getValue') == ""){
			parent.$.messager.alert('','품질점검 지표코드를 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_idt_cd").textbox('getValue').length > 5 ){
			parent.$.messager.alert('','품질점검 지표코드의 길이를 5자 이내로 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_idt_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','품질점검 지표명을 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_idt_nm").textbox('getValue').length > 50){
			parent.$.messager.alert('','품질점검 지표명의 길이를 50자 이내로 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_idt_tp_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','품질지표 유형을 선택해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_tg_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','품질점검 대상여부를 선택해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_tp_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','품질점검 유형을 선택해 주세요.');
			return false;
		}
//		if($("#detail_form #srt_ord").textbox('getValue') == ""){
//			parent.$.messager.alert('','정렬순서를 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #srt_ord").textbox('getValue').length > 3){
			parent.$.messager.alert('','정렬순서의 길이를 3자 이내로 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_cont").textbox('getValue') == ""){
			parent.$.messager.alert('','품질점검내용을 입력해 주세요.');
			return false;
		}
		if($("#detail_form #qty_chk_cont").textbox('getValue').length > 2000){
			parent.$.messager.alert('','품질점검내용의 길이를 2000자 이내로 입력해 주세요. ');
			return false;
		}
//		if($("#detail_form #slv_rsl_cont").textbox('getValue') == ""){
//			parent.$.messager.alert('','해결방안내용을 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #slv_rsl_cont").textbox('getValue').length > 2000){
			parent.$.messager.alert('','해결방안내용의 길이를 2000자 이내로 입력해 주세요.');
			return false;
		}
//		if($("#detail_form #qty_chk_result_tbl_nm").textbox('getValue') == ""){
//			parent.$.messager.alert('','품질점검결과 테이블명을 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #qty_chk_result_tbl_nm").textbox('getValue').length > 50){
			parent.$.messager.alert('','품질점검결과 테이블명의 길이를 50자 이내로 입력해 주세요.');
			return false;
		}
		if($("#detail_form #excel_output_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','엑셀출력 대상여부를 선택해 주세요.');
			return false;
		}
//		if($("#detail_form #output_start_row").textbox('getValue') == ""){
//			parent.$.messager.alert('','엑셀출력 시작행을 입력해 주세요.');
//			return false;
//		}
		if($("#detail_form #output_start_row").textbox('getValue').length > 3){
			parent.$.messager.alert('','엑셀출력 시작행의 길이를 3자 이내로 입력해 주세요.');
			return false;
		}
		if($("#detail_form #output_start_col").textbox('getValue').length > 3){
			parent.$.messager.alert('','엑셀출력 시작열의 길이를 3자 이내로 입력해 주세요.');
			return false;
		}
		
		
		ajaxCall("/Mqm/QualityCheckManagement/Save",
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
		
			ajaxCall("/Mqm/QualityCheckManagement/Delete",
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
	
	$("#detail_form #qty_chk_idt_cd").textbox({readonly:true});
	$("#detail_form #qty_chk_idt_cd").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #mdi_pcs_cd").combobox("setValue", selRow.mdi_pcs_cd);
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	$("#detail_form #qty_idt_tp_cd").combobox("setValue", selRow.qty_idt_tp_cd);
	$("#detail_form #qty_chk_tg_yn").combobox("setValue", selRow.qty_chk_tg_yn);
	$("#detail_form #qty_chk_tp_cd").combobox("setValue", selRow.qty_chk_tp_cd);
	$("#detail_form #srt_ord").textbox("setValue", selRow.srt_ord);
	$("#detail_form #qty_chk_cont").textbox("setValue", selRow.qty_chk_cont);
	$("#detail_form #slv_rsl_cont").textbox("setValue", selRow.slv_rsl_cont);
	$("#detail_form #qty_chk_result_tbl_nm").textbox("setValue", selRow.qty_chk_result_tbl_nm);
	$("#detail_form #excel_output_yn").combobox("setValue", selRow.excel_output_yn);
	$("#detail_form #output_start_row").textbox("setValue", selRow.output_start_row);
	$("#detail_form #output_start_col").textbox("setValue", selRow.output_start_col);

}
function Btn_ResetField(){
	
	$("#detail_form #crud_flag").val("C");
	
	$("#detail_form #qty_chk_idt_cd").textbox({readonly:false});
	$("#detail_form #mdi_pcs_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #qty_idt_tp_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_tg_yn").combobox("setValue", "");
	$("#detail_form #qty_chk_tp_cd").combobox("setValue", "");
	$("#detail_form #srt_ord").textbox("setValue", "");
	$("#detail_form #qty_chk_cont").textbox("setValue", "");
	$("#detail_form #slv_rsl_cont").textbox("setValue", "");
	$("#detail_form #qty_chk_result_tbl_nm").textbox("setValue", "");
	$("#detail_form #excel_output_yn").combobox("setValue", "");
	$("#detail_form #output_start_row").textbox("setValue", "");
	$("#detail_form #output_start_col").textbox("setValue", "");
	
	$("#tableList").datagrid('unselectAll');
}

function Excel_Download(){

	$("#submit_form").attr("action","/Mqm/QualityCheckManagement/ExcelDown");
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
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 지표 관리"," ");
	
		ajaxCall("/Mqm/QualityCheckManagement",
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

