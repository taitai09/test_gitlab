let grid_apply_yn = "";
let inputbox_apply_yn = "";
let switchCode = "";
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 프로젝트ID 조회
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	
	// 품질점검지표코드 조회
	$('#submit_form #qty_chk_idt_cd').combobox({
	    url:"/Mqm/getQtyChkIdtCd",
	    method:"get",
		valueField:'qty_chk_idt_cd',
	    textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});

	// 품질점검지표코드 조회
	$('#detail_form #qty_chk_idt_cd').combobox({
	    url:"/Mqm/getQtyChkIdtCd",
	    method:"get",
		valueField:'qty_chk_idt_cd',
	    textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 모델링 단계
	$('#detail_form #mdi_pcs_cd').combobox({
		url:"/Common/getCommonCode?isChoice=Y&grp_cd_id=M0001",
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
		url:"/Common/getCommonCode?isChoice=Y&grp_cd_id=M0002",
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
		url:"/Common/getCommonCode?isChoice=Y&grp_cd_id=M0003",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});


	
	
/******
신규(그리드의 적용여부 = ‘미적용’) and ‘적용여부‘ 콤보 ‘적용’ 선택 후 등록
	switchCode = 1
변경(그리드의 적용여부 = ‘적용’) and ‘적용여부‘ 콤보 ‘적용’ 선택 후 등록
	switchCode = 2
변경(그리드의 적용여부 = ‘적용’ and ‘적용여부‘ 콤보 ‘미적용’ 선택 후 등록
	switchCode = 3
******/
	
	// 적용여부
	$('#detail_form #apply_yn').combobox({
		onSelect: function(rec){
			
			inputbox_apply_yn = rec.value;
			
			if(grid_apply_yn == 'N' && inputbox_apply_yn == 'Y'){
				$("#btn_save").html("등록");
				switchCode = '1';
			}else if(grid_apply_yn == 'Y' && inputbox_apply_yn == 'Y'){
				switchCode = '2';
				$("#btn_save").html("저장");
			}else{
				switchCode = '3';
				$("#btn_save").html("저장");
			}
			
		},
	});


	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'apply_yn',title:'적용여부',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'project_nm',title:'프로젝트',halign:"center",align:"center",sortable:"true"},
			{field:'mdi_pcs_nm',title:'모델링 단계',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'qty_chk_idt_cd',title:'품질점검 <br/>지표코드',width:"05%",halign:"center",align:"center",sortable:"true"},
			{field:'qty_chk_idt_nm',title:'품질점검 지표명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'qty_idt_tp_nm',title:'품질지표 유형',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'qty_chk_tg_yn',title:'품질점검 <br/>대상여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'qty_chk_tp_nm',title:'품질점검 유형',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'srt_ord',title:'정렬순서',width:"5%",halign:"center",align:'right',sortable:"true"},
			{field:'qty_chk_cont',title:'품질점검 내용',width:"10%",halign:"center",align:'left',sortable:"true"},
			{field:'slv_rsl_cont',title:'해결방안 내용',width:"10%",halign:"center",align:'left',sortable:"true"},
			{field:'qty_chk_sql',title:'품질점검 RULE',width:"80%",halign:"center",align:'left'},
			{field:'mdi_pcs_cd',title:'모델링단계코드',hidden:true},
			{field:'qty_idt_tp_cd',title:'품질지표유형코드',hidden:true},
			{field:'qty_chk_tp_cd',title:'품질점검유형코드',hidden:true},
			]],

	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
	});
	$("#detail_form #project_nm").textbox({readonly:true});
	$("#detail_form #mdi_pcs_cd").combobox({readonly:true});
	$("#detail_form #qty_chk_idt_cd").textbox({readonly:true});
	$("#detail_form #qty_chk_idt_nm").textbox({readonly:true});
	$("#detail_form #qty_idt_tp_cd").combobox({readonly:true});
	$("#detail_form #qty_chk_tg_yn").combobox({readonly:true});
	$("#detail_form #qty_chk_tp_cd").combobox({readonly:true});
	$("#detail_form #srt_ord").textbox({readonly:true});
	$("#detail_form #qty_chk_cont").textbox({readonly:true});
	$("#detail_form #slv_rsl_cont").textbox({readonly:true});
	
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


function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}
function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #project_nm").textbox("setValue", row.project_nm);
//	$("#detail_form #project_nm").textbox("setValue", row.project_nm);
	$("#submit_form #project_id").val(row.project_id);
	
	
//	searchPerformanceCheckId(row.project_id);
}

function Btn_ApplyStandardProject(){

	let project_id = $("#submit_form #project_id").val();
	
	if(!formValidationCheck()){  //현재 없음.
		return;
	}else{
		$("#detail_form #project_id").val(project_id);
	}
	
	ajaxCall("/Mqm/ProjectQualityCheckRuleMng/Apply",
			$("#detail_form"),
			"POST",
			callback_ApplyProjectAction);		
}

//callback 함수
var callback_ApplyProjectAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 표준 적용이 완료되었습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_SaveSetting(){
	
	let rows = $('#tableList').datagrid('getSelected');
	
	if(rows == null){
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	if(grid_apply_yn == "N" && inputbox_apply_yn == "N" ){
		parent.$.messager.alert('',"[적용여부]를 '적용'으로 선택후 등록하십시오.");
		return false;
	}
	
	if($("#detail_form #apply_yn").combobox('getValue') == "" || switchCode == ''){
		parent.$.messager.alert('','적용여부를 선택해 주세요.');
		return false;
	}
	
	
	if($("#detail_form #qty_chk_sql").val() == ""){
		parent.$.messager.alert('','품질점검 RULE을 입력해 주세요.');
		return false;
	}

	ajaxCall("/Mqm/ProjectQualityCheckRuleMng/Save?switchCode="+switchCode,
			$("#detail_form"),
			"POST",
			callback_SaveInstanceAction);		
}

//callback 함수
var callback_SaveInstanceAction = function(result) {
	let text = "";

	if(result.result){
		if(switchCode == 1) text = "등록이 완료 되었습니다.";
		else text = "저장이 완료 되었습니다.";
		
		parent.$.messager.alert('',text,'info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	$("#crud_flag").val("U")
	
	if(selRow.apply_yn == '적용') {
		grid_apply_yn = 'Y';
		$("#detail_form #apply_yn").combobox("setValue", grid_apply_yn);
	}else if(selRow.apply_yn == '미적용'){
		grid_apply_yn = 'N';
		$("#detail_form #apply_yn").combobox("setValue", grid_apply_yn);
	}else{
		grid_apply_yn = "";
		$("#detail_form #apply_yn").combobox("setValue", grid_apply_yn);
	}
	
	
	$("#detail_form #project_nm").textbox("setValue", selRow.project_nm);
	$("#detail_form #mdi_pcs_cd").combobox("setValue", selRow.mdi_pcs_cd);
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	$("#detail_form #qty_idt_tp_cd").combobox("setValue", selRow.qty_idt_tp_cd);
	$("#detail_form #qty_chk_tg_yn").combobox("setValue", selRow.qty_chk_tg_yn);
	$("#detail_form #qty_chk_tp_cd").combobox("setValue", selRow.qty_chk_tp_cd);
	$("#detail_form #srt_ord").textbox("setValue", selRow.srt_ord);
	$("#detail_form #qty_chk_cont").textbox("setValue", selRow.qty_chk_cont);
	$("#detail_form #slv_rsl_cont").textbox("setValue", selRow.slv_rsl_cont);
	
	$("#detail_form #project_id").val($("#submit_form #project_id").val());
	$("#detail_form #qty_chk_sql").val(selRow.qty_chk_sql);
	
	var copyText = document.getElementById("qty_chk_sql");
	copyText.scrollTop = 0;
}

function Btn_ResetField(){
	$("#crud_flag").val("C")
	
	$("#detail_form #apply_yn").combobox("setValue", "");
	$("#detail_form #project_nm").textbox("setValue", "");
	$("#detail_form #mdi_pcs_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #qty_idt_tp_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_tg_yn").combobox("setValue", "");
	$("#detail_form #qty_chk_tp_cd").combobox("setValue", "");
	$("#detail_form #srt_ord").textbox("setValue", "");
	$("#detail_form #qty_chk_cont").textbox("setValue", "");
	$("#detail_form #slv_rsl_cont").textbox("setValue", "");
	
	$("#detail_form #project_id").val("");
	$("#detail_form #qty_chk_sql").val("");
	
	var copyText = document.getElementById("qty_chk_sql");
	copyText.scrollTop = 0;
	
	$("#tableList").datagrid('clearSelections');

	
	
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
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 구조 품질점검 지표/RULE 관리"," "); 
		
		ajaxCall("/Mqm/ProjectQualityCheckRuleMng",
				$("#submit_form"),
				"POST",
				callback_searchAction);	
}

//callback 함수
var callback_searchAction = function(result) {
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
	
	if($('#submit_form #project_id').val() == null || $('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	return true;
	
}
/*페이징처리끝*/


function Excel_Download(){
	
	if( $('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	$("#submit_form").attr("action","/Mqm/ProjectQualityCheckRuleMng/ExcelDown");
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
	parent.$.messager.alert('RULE 복사','품질점검 RULE이 복사되었습니다.');
}

