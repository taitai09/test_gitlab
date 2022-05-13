var currentCnt = 0;
var currentPagePerCount = 10;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 품질점검 지표코드(검색영역)
	$('#submit_form #qty_chk_idt_cd').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox1?isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	// 예외대상 객체구분(검색영역)
	$('#submit_form #obj_type_cd').combobox({
		url:"/Common/getCommonCode?isAll=Y&grp_cd_id=M0004",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	// 라이브러리명(검색영역)
	$('#submit_form #lib_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox3?isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(val){
			SelectCombobox4(val.cd_nm);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	SelectCombobox4('');
	SelectCombobox5('','');
	SelectCombobox4_Bottom('');
	SelectCombobox5_Bottom('','');
	
	
	// 품질점검 지표코드
	$('#detail_form #qty_chk_idt_cd').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox1?isChoice=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(rec){
			$("#detail_form #qty_chk_idt_nm").textbox('setValue',rec.qty_chk_idt_nm);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	// 예외대상 객체구분
	$('#detail_form #obj_type_cd').combobox({
		url:"/Common/getCommonCode?isChoice=Y&grp_cd_id=M0004",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	// 라이브러리명
	$('#detail_form #lib_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox3?isChoice=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(val){
			SelectCombobox4_Bottom(val.cd_nm);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	
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

var clickedTableListRow ;  // 클릭한순간 row의 값을 저장해둔다. 리드온리를 한 콤보박스4,5에 값을 넣으는 용도.
//changeList
function getTableList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				clickedTableListRow = row;
				setDetailView(row);
			},		
			columns:[[
				{field:'qty_chk_idt_cd',title:'품질점검 지표코드',width:"10%",halign:"center",align:"center"},
				{field:'qty_chk_idt_nm',title:'품질점검 지표명',width:"10%",halign:"center",align:'center'},
				{field:'obj_type',title:'예외대상 객체구분',width:"10%",halign:"center",align:'center'},
				{field:'lib_nm',title:'라이브러리명',width:"10%",halign:"center",align:'center'},
				{field:'model_nm',title:'모델명',width:"10%",halign:"center",align:'center'},
				{field:'sub_nm',title:'주제영역명',width:"10%",halign:"center",align:'center'},
				{field:'ent_nm',title:'엔터티명',width:"10%",halign:"center",align:'center'},
				{field:'att_nm',title:'속성명',width:"30%",halign:"center",align:'center'},
				{field:'remark',title:'비    고',width:"30%",halign:"center",align:'center'},
				{field:'rqpn',title:'요청자',width:"10%",halign:"center",align:'center'},
				{field:'rgdtti',title:'등록일시',width:"10%",halign:"center",align:'center'},
				{field:'obj_type_cd',hidden:true}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		
};

function SelectCombobox4(lib_nm){
	if(lib_nm == null || lib_nm == '' || lib_nm == 'undefined'){
		lib_nm = "";
	}
	// 모델명(검색영역)
	$('#submit_form #model_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox4?isAll=Y&lib_nm="+lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(val){
			SelectCombobox5(lib_nm, val.cd_nm);	
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
};
function SelectCombobox5(lib_nm, model_nm){
	if(lib_nm == 'undefined' || model_nm == 'undefined' || lib_nm == '' || model_nm ==''){
		model_nm = "";
		lib_nm = "";
	}
	
	// 주제영역명(검색영역)
	$('#submit_form #sub_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox5?isAll=Y&model_nm="+model_nm+"&lib_nm="+lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onChange:function(newVal,oldVal){
			$(this).combobox('setText',newVal);
		}
	});		
};

function SelectCombobox4_Bottom(lib_nm){
	if(lib_nm == null || lib_nm == '' || lib_nm == 'undefined'){
		lib_nm = "";
	}
	// 모델명(검색영역)
	$('#detail_form #model_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox4?isAll=Y&lib_nm="+lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(val){
			SelectCombobox5_Bottom(lib_nm, val.cd_nm);	
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(rec){
			if(clickedTableListRow != null){
				$(this).combobox('setValue',clickedTableListRow.model_nm);
				$("#detail_form #model_nm").combobox("textbox").css("background-color","#F4F4F4");
				$("#detail_form #sub_nm").combobox("textbox").css("background-color","#F4F4F4");
			}
		}
	});
};

var record;
function SelectCombobox5_Bottom(lib_nm, model_nm){
	if(lib_nm == 'undefined' || model_nm == 'undefined' || lib_nm == '' || model_nm ==''){
		model_nm = "";
		lib_nm = "";
	}
	
	// 주제영역명
	$('#detail_form #sub_nm').combobox({
		url:"/Mqm/QualityRevExcManagement/SelectCombobox5?isChoice=Y&model_nm="+model_nm+"&lib_nm="+lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(rec){
			if(clickedTableListRow != null){
				var sub_nm = clickedTableListRow.sub_nm;
				sub_nm =  sub_nm.replace('&lt;','<').replace('&gt;', '>');
				$(this).combobox('setValue',sub_nm);
				$(this).combobox('setText',sub_nm);
			}
		},
//		onSelect:function(rec){
//			record = rec;
//			console.log("rec.cd :"+rec.cd);
//			console.log("rec.cd_nm :"+rec.cd_nm);
//			$(this).combobox('setText',rec.cd);
//		},
		onChange:function(newVal,oldVal){
			console.log("oldVal:",oldVal);
			console.log("newVal:",newVal);
			$(this).combobox('setText',newVal);
		}
	});		
};

/*function Btn_OnClick(){
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 지표 관리"," ");
	
		ajaxCall("/Mqm/QualityRevExcManagement",
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
		
		if($("#detail_form #qty_chk_idt_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','품질점검 지표코드를 선택해 주세요.');
			return false;
		}
		if($("#detail_form #obj_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','예외대상 객체구분을 선택해 주세요.');
			return false;
		}
//		if($("#detail_form #lib_nm").combobox('getValue') == ""){
//			parent.$.messager.alert('','라이브러리명을 선택해 주세요.');
//			return false;
//		}
//		if($("#detail_form #model_nm").combobox('getValue') == ""){
//			parent.$.messager.alert('','모델명을 선택해 주세요.');
//			return false;
//		}
//		if($("#detail_form #sub_nm").combobox('getValue') == ""){
//			parent.$.messager.alert('','주제영역명을 선택해 주세요.');
//			return false;
//		}
//		if($("#detail_form #obj_type_cd").textbox('getValue') == ""){
//			parent.$.messager.alert('','엔터티명을 입력해 주세요.');
//			return false;
//		}
//		if($("#detail_form #att_nm").textbox('getValue') == ""){
//			parent.$.messager.alert('','속성명을 입력해 주세요.');
//			return false;
//		}
//		if($("#detail_form #rqpn").textbox('getValue') == ""){
//			parent.$.messager.alert('','요청자를 입력해 주세요.');
//			return false;
//		}
//		if($("#detail_form #remark").textbox('getValue') == ""){
//			parent.$.messager.alert('','비고를 입력해 주세요.');
//			return false;
//		}
		
		ajaxCall("/Mqm/QualityRevExcManagement/Save",
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
		
			ajaxCall("/Mqm/QualityRevExcManagement/Delete",
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
	
	$("#detail_form #qty_chk_idt_cd").combobox({readonly:true});
	$("#detail_form #obj_type_cd").combobox({readonly:true});
	$("#detail_form #lib_nm").combobox({readonly:true});
	$("#detail_form #model_nm").combobox({readonly:true});
	$("#detail_form #sub_nm").combobox({readonly:true});
	$("#detail_form #ent_nm").textbox({readonly:true});
	$("#detail_form #att_nm").textbox({readonly:true});
	
	
	$("#detail_form #qty_chk_idt_cd").combobox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #qty_chk_idt_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #obj_type_cd").combobox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #lib_nm").combobox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #model_nm").combobox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #sub_nm").combobox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #ent_nm").textbox("textbox").css("background-color","#F4F4F4");
	$("#detail_form #att_nm").textbox("textbox").css("background-color","#F4F4F4");
	
	
	
	
	$("#crud_flag").val("U");
		$("#detail_form #qty_chk_idt_cd").combobox("setValue", selRow.qty_chk_idt_cd);
		$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
		$("#detail_form #obj_type_cd").combobox("setValue", selRow.obj_type_cd);
		$("#detail_form #lib_nm").combobox("setValue", selRow.lib_nm);
//		$("#detail_form #model_nm").combobox("setValue", selRow.model_nm);
//		$("#detail_form #sub_nm").combobox("setValue", selRow.sub_nm);
		$("#detail_form #ent_nm").textbox("setValue", selRow.ent_nm);
		$("#detail_form #att_nm").textbox("setValue", selRow.att_nm);
		$("#detail_form #rqpn").textbox("setValue", selRow.rqpn);
		$("#detail_form #remark").textbox("setValue", selRow.remark);

};


function Btn_ResetField(){
	clickedTableListRow = null;
	$("#crud_flag").val("C");
	$("#detail_form #qty_chk_idt_cd").combobox({readonly:false});
	$("#detail_form #obj_type_cd").combobox({readonly:false});
	$("#detail_form #lib_nm").combobox({readonly:false});
	$("#detail_form #model_nm").combobox({readonly:false});
	$("#detail_form #sub_nm").combobox({readonly:false});
	$("#detail_form #ent_nm").textbox({readonly:false});
	$("#detail_form #att_nm").textbox({readonly:false});
	
	$("#detail_form #qty_chk_idt_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #obj_type").combobox("setValue", "");
	$("#detail_form #lib_nm").combobox("setValue", "");
	$("#detail_form #model_nm").combobox("setValue", "");
	$("#detail_form #sub_nm").combobox("setValue", "");
	$("#detail_form #ent_nm").textbox("setValue", "");
	$("#detail_form #att_nm").textbox("setValue", "");
	$("#detail_form #rqpn").textbox("setValue", "");
	$("#detail_form #remark").textbox("setValue", "");
	
	$("#tableList").datagrid('unselectAll');
}

function Excel_Download(){

	$("#submit_form").attr("action","/Mqm/QualityRevExcManagement/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

//일괄업로드 팝업
function Excel_Upload(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	//parent.$("#sql_text_pop").val("");
	//parent.$("#bindPopTbl tbody tr").remove();
	$('#qualityRevExcMng_excel_upload_popup').window("open");
//	$('#excelUploadPop').window("open");
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질검토 예외 대상 관리"," ");
	
	ajaxCall("/Mqm/QualityRevExcManagement",
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
