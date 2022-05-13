var selectedRowData;
var projectFormName;
var crudFlag;
var crudName;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#submit_form #project_id').combobox({
		url:"/sqlStandardSettingDesign/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},onLoadSuccess: function() {
			$( '#submit_form #project_id' ).combobox( 'textbox' ).attr( 'placeholder' , '선택' );
		}
	});
	
	$.extend($.fn.datagrid.defaults.editors, {
		textarea: {
			init: function(container, options){
				var input = $('<textarea class="datagrid-editable-input"></textarea>').appendTo(container);
				return input;
			},
			getValue: function(target){
				return $(target).val();
			},
			setValue: function(target, value){
				$(target).val(value);
			},
			resize: function(target, width){
				$(target)._outerWidth(width);
			}
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			selectedRowData = row;
			setDetailView(row);
		},
		columns:[[
			{field:'apply_yn',title:'적용여부',width:"5%",halign:"center",align:"center"},
			{field:'project_nm',title:'프로젝트',width:"15%",halign:"center",align:"center"},
			{field:'qty_chk_idt_cd',title:'품질 점검<br/>지표 코드',width:"5%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_sql',title:'품질 점검 RULE',width:"50%",halign:"center",align:'center'},
			{field:'dml_yn',title:'DML 여부',width:"5%",halign:"center",align:'center'},
			{field:'project_id',title:'프로젝트',hidden:true}
		]],
		onSelect:function(index,row) {
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// 적용여부
	$('#detail_form #apply_yn').combobox({
		onChange:function(newValue, oldValue){
			var cd_nm = $(this).combobox('getText');
			console.log("cd_nm:"+cd_nm);
			console.log("newValue:",newValue);
			console.log("oldValue:",oldValue);

			if(selectedRowData != undefined){
				if(selectedRowData.apply_yn == "미적용" && cd_nm == "적용"){
					$("#detail_form #crud_flag").val("C");
					$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 등록");
				}else if(selectedRowData.apply_yn == "적용" && cd_nm == "적용"){
					$("#detail_form #crud_flag").val("U");
					$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
				}else if(selectedRowData.apply_yn == "미적용" && cd_nm == "미적용"){
					$("#detail_form #crud_flag").val("");
					$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 등록");
				}else if(selectedRowData.apply_yn == "적용" && cd_nm == "미적용"){
					$("#detail_form #crud_flag").val("D");
					$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
				}else{
					$("#detail_form #crud_flag").val("");
					$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
				}
			}
			
		},
		onSelect:function(rec){
			console.log("rec :",rec);
		}
	});	
	
	//Btn_OnClick();
	Btn_ResetField();
});

function Btn_PrjtStdRuleApply(){
	console.log("프로젝트 표준 RULE 적용");
	console.log("project_id :"+$("#submit_form #project_id").combobox("getValue"));
	if($("#submit_form #project_id").combobox("getValue") != ""){
		ajaxCall("/ProjectSqlQtyChkRule/Apply", $("#submit_form"), "POST", callback_ApplyAction);
	}else{
		parent.$.messager.alert('경고','프로젝트를 선택하여 주세요.','warning');
	}
}
//callback 함수
var callback_ApplyAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 표준 RULE 적용이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_ResetField();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_ShowProjectList() {
	$("#projectList_form #project_nm").textbox('setValue', '');
	$("#projectList_form #del_yn").combobox('setValue','N');
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

function Btn_OnClick(){
	var projectId = $("#submit_form #project_id").combobox("getValue");
	
	if ( projectId == "" || projectId == null ) {
		parent.$.messager.alert('경고','프로젝트를 선택하십시오.','warning');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 SQL 품질점검 RULE 관리"," ");
	
	ajaxCall("/ProjectSqlQtyChkRuleList",
			$("#submit_form"),
			"POST",
			callback_ProjectSqlQtyChkRuleList);
}

//callback 함수
var callback_ProjectSqlQtyChkRuleList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if(selectedRowData == null){
		parent.$.messager.alert('','검색된 목록에서 항목을 클릭한 후에 저장하세요.');
		return false;
	}
	
	console.log("selectedRowData :",selectedRowData);
	crudFlag = $("#detail_form #crud_flag").val();
	console.log("crudFlag :",crudFlag);
	var applyYn1 = $("#submit_form #apply_yn").combobox('getValue');
	var applyYn2 = $("#detail_form #apply_yn").combobox('getValue');
	console.log("applyYn1 :",applyYn1);
	console.log("applyYn2 :",applyYn2);
	
	if($("#detail_form #apply_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','적용여부를 선택하십시오.');
		return false;
	}
	
	console.log("selectedRowData.apply_yn :",selectedRowData.apply_yn);
	console.log("detail_form.apply_yn :",$("#detail_form #apply_yn").combobox('getValue'));
	if(selectedRowData.apply_yn == "미적용" && $("#detail_form #apply_yn").combobox('getText') == "미적용"){
		parent.$.messager.alert('','적용여부를 \'적용\'으로 선택후 등록하십시오.');
		return false;
	}
	
	if($("#detail_form #project_id").val() == ""){
		parent.$.messager.alert('','프로젝트를 선택하십시오.');
		return false;
	}
	
	if($("#detail_form #project_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','프로젝트를 선택하십시오.');
		return false;
	}

	if($("#detail_form #qty_chk_idt_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 코드를 입력하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_sql").val() == ""){
		parent.$.messager.alert('','품질 점검 RULE을 입력하십시오.');
		return false;
	}
	
	if(!formValidationCheck()) {
		return;
	}
	
	var url = "";
	if(crudFlag == 'C'){
		crudName = "등록";
	}else{
		crudName = "저장";
	}
	url = "/ProjectSqlQtyChkRule/Save";
	ajaxCall(url, $("#detail_form"), "POST", callback_SaveSettingAction);

}

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',crudName+'이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_ResetField();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setDetailView(selRow){
	console.log("selRow :",selRow);
	console.log("selRow.apply_yn :",selRow.apply_yn);
//	if(selRow.apply_yn == "미적용"){
//		console.log("미적용............");
//		$("#detail_form #crud_flag").val("C");
//		$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 등록");
//	}else if(selRow.apply_yn == "적용"){
//		$("#detail_form #crud_flag").val("U");
//		$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
//	}else{
//		$("#detail_form #crud_flag").val("");
//		$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
//	}
	
	var apply_yn = selRow.apply_yn;
	var apply_yn2 = selRow.apply_yn;
	if(apply_yn == "적용") apply_yn2 = "Y";
	else apply_yn2 = "N";
	
	$("#detail_form #crud_flag").val("U");
	
	$("#detail_form #apply_yn").combobox('setValue',apply_yn2);
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	
	$("#detail_form #project_nm").textbox('setValue',selRow.project_nm);
	$("#detail_form #project_id").val(selRow.project_id);

	$("#detail_form #dml_yn").combobox('setValue',selRow.dml_yn);
	$("#detail_form #qty_chk_sql").val(selRow.qty_chk_sql);
	
	$("#detail_form #qty_chk_idt_cd").textbox('readonly', true);
	$("#detail_form #qty_chk_idt_nm").textbox('readonly',true);
	$("#detail_form #apply_yn").combobox('readonly',false);
	$("#detail_form #dml_yn").combobox('readonly',true);
	$("#detail_form #project_nm").textbox('readonly',true);
}

function Btn_ResetField(){
	$("#tableList").datagrid("unselectAll");
	selectedRowData = null;
	
	$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 등록");
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", "");
	$("#detail_form #apply_yn").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #project_nm").textbox('setValue','');
	$("#detail_form #project_id").val('');
	$("#detail_form #dml_yn").combobox('setValue','');
	$("#detail_form #qty_chk_sql").val('');

	$("#detail_form #qty_chk_idt_cd").textbox({readonly: true});
	$("#detail_form #qty_chk_idt_nm").textbox('readonly',true);
	$("#detail_form #project_nm").textbox('readonly',true);
	$("#detail_form #apply_yn").combobox('readonly',false);
	$("#detail_form #dml_yn").combobox('readonly',true);
	
	$('#detail_form #apply_yn').combobox('textbox').attr( 'placeholder' , '선택' );
}

var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','품질 점검 지표를 삭제하였습니다.','info',function(){
			setTimeout(function() {
				Btn_ResetField();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function formValidationCheck(){
	
	if(onlyNumChk($("#detail_form #qty_chk_idt_cd").textbox("getValue")) == false) {
		parent.$.messager.alert('','품질 점검 지표 코드는 숫자만 입력하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_cd").textbox("getValue").length > 10) {
		parent.$.messager.alert('','품질 점검 지표 코드는 10자리까지 허용됩니다.');
		return false;
	}
	
	return true;
}

function Excel_Download(){
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}	
	$("#submit_form").attr("action","/ProjectSqlQtyChkRule/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
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
	parent.$.messager.alert('SQL 복사','품질점검 RULE이 복사되었습니다.');
}

function Btn_OpenPopup() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$('#rulesForWritingSqlQueriesPopup').window("open");
	
	//checkRGBColor();
	//ajaxCallGetRGBColor();
	//showViewer();
}
