var selectedRowData;
var projectFormName;
var crudFlag;
var crudName;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
	
//	$("#submit_form #project_nm").textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				projectFormName = "#submit_form";
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	$("#detail_form #project_nm").textbox({
		editable:false,
		icons:[{
			iconCls:'icon-search',
			handler:function() {
				projectFormName = "#detail_form";
				Btn_ShowProjectList();
			}
		}]
	});
	
	$("#tableList").datagrid({
		view: myview,
		nowrap:false,
		onClickRow : function(index,row) {
			selectedRowData = row;
			setDetailView(row);
		},
		columns:[[
			{field:'apply_yn',title:'적용여부',width:"4%",halign:"center",align:"center"},
			{field:'project_nm',title:'프로젝트',width:"15%",halign:"center",align:"center"},
			{field:'qty_chk_idt_cd',title:'품질 점검<br/>지표 코드',width:"4%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"15%",halign:"center",align:'left'},
			{field:'qty_chk_idt_yn',title:'품질 점검<br/>지표 여부',width:"4%",halign:"center",align:'center'},
			{field:'srt_ord',title:'정렬 순서',width:"4%",halign:"center",align:'center'},
			{field:'qty_chk_cont',title:'품질 점검 내용',width:"25%",halign:"center",align:'left'},
			{field:'slv_rsl_cont',title:'해결 방안 내용',width:"25%",halign:"center",align:'left'},
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
			
			if(cd_nm == "적용"){
				$("#detail_form #crud_flag").val("C");
				$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
			}else if(cd_nm == "미적용"){
				$("#detail_form #crud_flag").val("D");
				$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
			}else{
				$("#detail_form #crud_flag").val("");
				$("i.regist").parent().html("<i class=\"btnIcon fas fa-save fa-lg fa-fw regist\"></i> 저장");
			}
			
		},
		onSelect:function(rec){
			console.log("rec :",rec);
		}
	});	
	
	//Btn_OnClick();
	Btn_ResetField();
});

function Btn_PrjtStdIdxApply(){
	console.log("프로젝트 표준 지표 적용");
	console.log("project_id :"+$("#submit_form #project_id").val());
	if($("#submit_form #project_id").val() != ""){
		ajaxCall("/ProjectSqlQtyChkIdx/Apply", $("#submit_form"), "POST", callback_ApplyAction);
	}else{
		parent.$.messager.alert('','프로젝트를 선택하여 주세요.','info');
	}
}
//callback 함수
var callback_ApplyAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','프로젝트 표준 지표 적용이 완료 되었습니다.','info',function(){
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
	
	if($("#submit_form #project_id").val() == ""){
		parent.$.messager.alert('','프로젝트를 선택하십시오.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로젝트 SQL 품질점검 지표 관리"," ");
	
	ajaxCall("/ProjectSqlQtyChkIdxList",
			$("#submit_form"),
			"POST",
			callback_MaintainQualityCheckIndicator);
}

//callback 함수
var callback_MaintainQualityCheckIndicator = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if($("#detail_form #apply_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','적용여부를 선택하십시오.');
		return false;
	}

	if(selectedRowData == null){
		parent.$.messager.alert('','검색된 목록에서 항목을 클릭한 후에 저장하세요.');
		return false;		
	}
	
	if(selectedRowData.apply_yn == "적용" && $("#detail_form #apply_yn").combobox('getText') == "적용"){
		parent.$.messager.alert('','현재 적용 상태입니다.');
		return false;
	}
	
	if(selectedRowData.apply_yn == "미적용" && $("#detail_form #apply_yn").combobox('getText') == "미적용"){
		parent.$.messager.alert('','현재 미적용 상태입니다.');
		return false;
	}
	
	if(selectedRowData.apply_yn == "미적용" && $("#detail_form #apply_yn").combobox('getText') == "적용"){
		$("#detail_form #crud_flag").val("C");
	}
	
	if(selectedRowData.apply_yn == "적용" && $("#detail_form #apply_yn").combobox('getText') == "미적용"){
		$("#detail_form #crud_flag").val("D");
	}
	
	if($("#detail_form #project_id").val() == ""){
		parent.$.messager.alert('','프로젝트를 선택하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 코드를 입력하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표명을 입력하십시오.');
		return false;
	}
	
//	if($("#detail_form #qty_chk_idt_yn").combobox('getValue') == ""){
//		parent.$.messager.alert('','품질 점검 지표 여부를 선택하십시오.');
//		return false;
//	}
	
//	if($("#detail_form #srt_ord").textbox('getValue') == ""){
//		parent.$.messager.alert('','정렬 순서를 입력하십시오.');
//		return false;
//	}
	
	if($("#detail_form #qty_chk_cont").val() == ""){
		parent.$.messager.alert('','품질 점검 내용을 입력하십시오.');
		return false;
	}	
	if($("#detail_form #slv_rsl_cont").val() == ""){
		parent.$.messager.alert('','해결 방안 내용을 입력하십시오.');
		return false;
	}
	
	if(byteLength($("#detail_form #qty_chk_idt_nm").val()) > 100){
		parent.$.messager.alert('','품질 점검 지표명을 100Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #qty_chk_cont").val()) > 4000){
		parent.$.messager.alert('','품질 점검 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #slv_rsl_cont").val()) > 4000){
		parent.$.messager.alert('','해결 방안 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(!formValidationCheck()) {
		return;
	}
	
	crudFlag = $("#detail_form #crud_flag").val();
	var url = "";
	if(crudFlag == 'C'){
		crudName = "저장";
	}else{
		crudName = "저장";
	}
	url = "/ProjectSqlQtyChkIdx/Save";
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
	var apply_yn = selRow.apply_yn;
	var apply_yn2;
	if(apply_yn == "적용") apply_yn2 = "Y";
	else apply_yn2 = "N";
	
	$("#detail_form #crud_flag").val("U");
	
	$("#detail_form #apply_yn").combobox('setValue',apply_yn2);
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	
	$("#detail_form #project_nm").textbox('setValue',selRow.project_nm);
	$("#detail_form #project_id").val(selRow.project_id);
	
	$("#detail_form #qty_chk_idt_yn").combobox("setValue", selRow.qty_chk_idt_yn);
	$("#detail_form #srt_ord").textbox("setValue", selRow.srt_ord);
	$("#detail_form #qty_chk_cont").val(selRow.qty_chk_cont);
	$("#detail_form #slv_rsl_cont").val(selRow.slv_rsl_cont);

	$("#detail_form #qty_chk_idt_cd").textbox('readonly', true);
	$("#detail_form #qty_chk_idt_nm").textbox('readonly',true);
	$("#detail_form #apply_yn").combobox('readonly',false);
	$("#detail_form #dml_yn").combobox('readonly',true);
	$("#detail_form #project_nm").textbox('readonly',true);
	$("#detail_form #qty_chk_idt_yn").combobox('readonly',true);
	$("#detail_form #srt_ord").textbox('readonly',true);
	$("#detail_form #qty_chk_cont").prop("readonly",true);
	$("#detail_form #slv_rsl_cont").prop("readonly",true);
	$("#detail_form #qty_chk_cont").css("background-color","#F2F3EF");
	$("#detail_form #slv_rsl_cont").css("background-color","#F2F3EF");
//	$("#detail_form #qty_chk_cont").css({"background-color":"#F2F3EF"});
//	$("#detail_form #slv_rsl_cont").css({"background-color":"#F2F3EF"});

}

function Btn_ResetField(){
	$("#tableList").datagrid("unselectAll");
	selectedRowData = null;
	
	$("#detail_form #apply_yn").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_cd").textbox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #project_nm").textbox('setValue','');
	$("#detail_form #project_id").val('');
	$("#detail_form #qty_chk_idt_yn").combobox("setValue", "");
	$("#detail_form #srt_ord").textbox("setValue", "");
	$("#detail_form #qty_chk_cont").val("");
	$("#detail_form #slv_rsl_cont").val("");

	$("#detail_form #apply_yn").combobox('readonly',false);
	$("#detail_form #qty_chk_idt_cd").textbox({readonly: true});
	$("#detail_form #qty_chk_idt_nm").textbox('readonly',true);
	$("#detail_form #dml_yn").combobox('readonly',true);
	$("#detail_form #project_nm").textbox('readonly',true);
	
	$("#detail_form #qty_chk_idt_yn").combobox('readonly',true);
	$("#detail_form #srt_ord").textbox('readonly',true);
	
	$("#detail_form #qty_chk_cont").prop("readonly",true);
	$("#detail_form #slv_rsl_cont").prop("readonly",true);
	$("#detail_form #qty_chk_cont").css("background-color","#F2F3EF");
	$("#detail_form #slv_rsl_cont").css("background-color","#F2F3EF");
}

function formValidationCheck(){
	if($("#detail_form #apply_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','적용여부를 선택하십시오.');
		return false;
	}
	
	if($("#detail_form #project_id").val() == ""){
		parent.$.messager.alert('','프로젝트를 선택하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 코드를 입력하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표명을 입력하십시오.');
		return false;
	}
	
//	if($("#detail_form #qty_chk_idt_yn").combobox('getValue') == ""){
//		parent.$.messager.alert('','품질 점검 지표 여부를 선택하십시오.');
//		return false;
//	}
	
//	if($("#detail_form #srt_ord").textbox('getValue') == ""){
//		parent.$.messager.alert('','정렬 순서를 입력하십시오.');
//		return false;
//	}
	
	if($("#detail_form #qty_chk_cont").val() == ""){
		parent.$.messager.alert('','품질 점검 내용을 입력하십시오.');
		return false;
	}	
	if($("#detail_form #slv_rsl_cont").val() == ""){
		parent.$.messager.alert('','해결 방안 내용을 입력하십시오.');
		return false;
	}
	
	if(byteLength($("#detail_form #qty_chk_idt_nm").val()) > 100){
		parent.$.messager.alert('','품질 점검 지표명을 100Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #qty_chk_cont").val()) > 4000){
		parent.$.messager.alert('','품질 점검 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #slv_rsl_cont").val()) > 4000){
		parent.$.messager.alert('','해결 방안 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
		
	if(onlyNumChk($("#detail_form #qty_chk_idt_cd").textbox("getValue")) == false) {
		parent.$.messager.alert('','품질 점검 지표 코드는 숫자만 입력하십시오.');
		return false;
	}
	
	if(onlyNumChk($("#detail_form #srt_ord").textbox("getValue")) == false) {
		parent.$.messager.alert('','정렬 순서는 숫자만 입력하십시오.');
		return false;
	}
	
	if($("#detail_form #qty_chk_idt_cd").textbox("getValue").length > 10) {
		parent.$.messager.alert('','품질 점검 지표 코드는 10자리까지 허용됩니다.');
		return false;
	}
	
	if($("#detail_form #srt_ord").textbox("getValue").length > 3) {
		parent.$.messager.alert('','정렬 순서는 3자리까지 허용됩니다.');
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
	$("#submit_form").attr("action","/ProjectSqlQtyChkIdx/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
