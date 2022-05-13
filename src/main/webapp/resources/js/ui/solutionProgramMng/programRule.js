var colorIdArry = new Array();
var colorValArry = new Array(); 

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	//솔루션프로그램구분 상단
	$('#submit_form #slt_program_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1074&isChoice=N",
	    method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			$.messager.alert('','솔루션프로그램구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#submit_form #slt_program_div_cd").combobox("textbox").attr("placeholder","선택");
		}
	});
	//솔루션프로그램구분 하단
	$('#detail_form #slt_program_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1074&isChoice=N",
	    method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect:function(rec){
			if(rec.cd != ''){
				//목차
				$('#contents_id').combotree({
					idField:'id',
					treeField:'text',
					url:"/SolutionProgramMng/ProgramListTree?isChoice=menu&slt_program_div_cd="+rec.cd,
					method:"POST",
					onSelect : function (rows){
						selectedRow = rows;
					},
					onLoadError:function() {
						$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
					}
				});
			}
		},
		onLoadError: function(){
			$.messager.alert('','솔루션프로그램구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#detail_form #slt_program_div_cd").combobox("textbox").attr("placeholder","선택");
		}
	});


	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'slt_program_div_nm',title:'솔루션프로그램구분',width:"8%",halign:"center",align:"center",sortable:"true"},
			{field:'slt_program_sql_number',title:'프로그램 RULE번호',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'slt_program_sql_name',title:'프로그램 RULE명',width:"8%",halign:"center",align:'left'},
			{field:'contents_name',title:'목차명',width:"8%",halign:"center",align:'left'},
			{field:'slt_program_sql_desc',title:'설명',width:"12%",halign:"center",align:'left'},
			{field:'slt_program_chk_sql',title:'프로그램 RULE',width:"60%",halign:"center",align:'left'},
			{field:'slt_program_div_cd',hidden:true},
			{field:'contents_id',hidden:true}
			]],

	    	onLoadError:function() {
	    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
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
	
	
	var qq = $('#slt_program_sql_name');
	qq.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
});

function Btn_SaveSetting(){
	if($("#detail_form  #slt_program_div_cd").combobox('getValue') == ""){
		$.messager.alert('','솔루션프로그램구분을 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #slt_program_sql_name").textbox('getValue') == ""){
		$.messager.alert('','프로그램 RULE명을 입력해 주세요.');
		return false;
	}
	
	/*if($("#detail_form #contents_id").combotree('getValue') == "" && $("#detail_form #contents_id").combotree('getText') == "없음"){
		$.messager.alert('','목차를 선택해 주세요.');
		return false;
	}*/
	
	
	/*if($("#detail_form #slt_program_sql_desc").textbox('getValue') == ""){
		$.messager.alert('','설명을 입력해 주세요.');
		return false;
	}*/
	
	if($("#detail_form #slt_program_chk_sql").val() == ""){
		$.messager.alert('','프로그램 RULE을 입력해 주세요.');
		return false;
	}
	
	ajaxCall("/SolutionProgramMng/ProgramRule/Save",
			$("#detail_form"),
			"POST",
			callback_SaveProgramRuleAction);
}

//callback 함수
var callback_SaveProgramRuleAction = function(result) {
	if(result.result){
		$.messager.alert('','프로그램 RULE 저장이 완료 되었습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message);
	}
};

function Btn_DeleteSetting(){
	if($("#detail_form #slt_program_sql_number").textbox('getValue') == ""){
		$.messager.alert('','프로그램 RULE 번호를 선택해 주세요.');
		return false;
	}
	
	$.messager.confirm('Confirm','프로그램 RULE 정보를 삭제하시겠습니까?',function(r){
	    if (r){
	        ajaxCall("/SolutionProgramMng/ProgramRule/Delete",
	        		$("#detail_form"),
	        		"POST",
	        		callback_DeleteProgramRuleActiuon);		
	    }
	});

}

//callback 함수
var callback_DeleteProgramRuleActiuon = function(result) {
	if(result.result){
		$.messager.alert('','프로그램 RULE 정보 삭제하였습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	$("#crud_flag").val("U")
	
	$("#detail_form #slt_program_div_cd").combobox("setValue", selRow.slt_program_div_cd);
	$("#detail_form #slt_program_sql_number").textbox("setValue", selRow.slt_program_sql_number);
	$("#detail_form #slt_program_sql_name").textbox("setValue", selRow.slt_program_sql_name);
	$("#detail_form #contents_id").combotree("setValue", selRow.contents_id);
	$("#detail_form #slt_program_sql_desc").textbox("setValue", selRow.slt_program_sql_desc);
	$("#detail_form #slt_program_chk_sql").val(selRow.slt_program_chk_sql);
	
	var copyText = document.getElementById("slt_program_chk_sql");
	copyText.scrollTop = 0;
}

function Btn_ResetField(){
	$("#crud_flag").val("C")
//	$("#detail_form #qty_chk_idt_cd").combobox({readonly:false});
	$("#detail_form #slt_program_div_cd").combobox("setValue", "");
	$("#detail_form #slt_program_sql_number").textbox("setValue", "");
	$("#detail_form #slt_program_sql_name").textbox("setValue", "");
	$("#detail_form #contents_id").combobox("setValue", "");
	$("#detail_form #slt_program_sql_desc").textbox("setValue", "");
	$("#detail_form #slt_program_chk_sql").val("");
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
		if(openMessageProgress != undefined) openMessageProgress("솔루션 프로그램 RULE 관리"," "); 
		//if(parent.openMessageProgress != undefined) parent.openMessageProgress("솔루션 프로그램 RULE 관리"," "); 
		
		ajaxCall("/SolutionProgramMng/ProgramRule",
				$("#submit_form"),
				"POST",
				callback_ProgramRuleAction);	
}

//callback 함수
var callback_ProgramRuleAction = function(result) {
	Btn_ResetField();
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(closeMessageProgress != undefined) closeMessageProgress();
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
		$.messager.alert('',e.message);
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
	if($("#submit_form #slt_program_div_cd").combobox('getValue') == ""){
		$.messager.alert('','솔루션프로그램구분을 선택해 주세요.');
		return false;
	}
	return true;
}
/*페이징처리끝*/


function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}
	$("#submit_form").attr("action","/SolutionProgramMng/ProgramRule/ExcelDown");
	$("#submit_form").submit();
}

function copy_to_clipboard() {
	if ($("#detail_form #slt_program_chk_sql").val( ) == ""){
		return;
	}
	
	var copyText = document.getElementById("slt_program_chk_sql");
	copyText.focus();
	copyText.select();
	document.execCommand("Copy");
	copyText.setSelectionRange(0, 0);
	copyText.scrollTop = 0;   
	$.messager.alert('RULE 복사','프로그램 RULE이 복사되었습니다.');
}

function copy_encrypted_rule(){
	let before_sql = $('#tableList').datagrid('getSelected');
	let current_sql = $("#slt_program_chk_sql").val();
	
	if ( before_sql != null ){
		if( before_sql.slt_program_chk_sql == current_sql ){
			$("#encrypted_rule").val(before_sql.slt_encrypted_sql);
			
			let copyText = document.getElementById("encrypted_rule");
			copyText.focus();
			copyText.select();
			document.execCommand("Copy");
			copyText.setSelectionRange(0, 0);
			$.messager.alert('암호화 RULE 복사','암호화된 프로그램 RULE이 복사되었습니다.');
			
		}else if(before_sql.slt_program_chk_sql != current_sql){
			warningMessager('수정된 RULE이 저장되지 않았습니다.<br>RULE 저장 후 복사바랍니다.');
		}
	}
}