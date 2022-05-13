var selectedRowData;

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
	
	$("#tableList").datagrid({
		view: myview,
		nowrap:false,
		onClickRow : function(index,row) {
			selectedRowData = row;
			setDetailView(row);
		},
		columns:[[
			{field:'qty_chk_idt_cd',title:'품질 점검<br/>지표 코드',width:"5%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_idt_yn',title:'품질 점검<br/>지표 여부',width:"5%",halign:"center",align:'center'},
			{field:'srt_ord',title:'정렬 순서',width:"5%",halign:"center",align:'center'},
			{field:'qty_chk_cont',title:'품질 점검 내용',width:"40%",halign:"center",align:'left'},
			{field:'slv_rsl_cont',title:'해결 방안 내용',width:"40%",halign:"center",align:'left'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#rowStatus").val("i");
	
	Btn_OnClick();
	
	var t1 = $("#submit_form #searchValue");
	
	t1.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});
});

//changeList
function updateMaintainQualityCheckIndicator(){
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'qty_chk_idt_cd',title:'품질 점검 지표 코드',width:"10%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_idt_yn',title:'품질 점검 지표 여부',width:"10%",halign:"center",align:'center'},
			{field:'srt_ord',title:'정렬 순서',width:"5%",halign:"center",align:'center'},
			{field:'qty_chk_cont',title:'품질 점검 내용',width:"40%",halign:"center",align:'left'},
			{field:'slv_rsl_cont',title:'해결 방안 내용',width:"40%",halign:"center",align:'left'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
};

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 지표 관리"," ");
	
	ajaxCall("/maintainQualityCheckIndicator",
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
	if($("#ui_qty_chk_idt_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 코드를 입력해 주세요.');
		return false;
	}
	
	if($("#qty_chk_idt_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표명을 입력해 주세요.');
		return false;
	}
	
	if($("#qty_chk_idt_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','품질 점검 지표 여부를 선택해 주세요.');
		return false;
	}
	
	if($("#srt_ord").textbox('getValue') == ""){
		parent.$.messager.alert('','정렬 순서를 입력해 주세요.');
		return false;
	}
	
	if($("#qty_chk_cont").val() == ""){
		parent.$.messager.alert('','품질 점검 내용을 입력해 주세요.');
		return false;
	}	
	if($("#slv_rsl_cont").val() == ""){
		parent.$.messager.alert('','해결 방안 내용을 입력해 주세요.');
		return false;
	}
	
	if(byteLength($("#qty_chk_idt_nm").val()) > 100){
		parent.$.messager.alert('','품질 점검 지표명을 100Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#qty_chk_cont").val()) > 4000){
		parent.$.messager.alert('','품질 점검 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#slv_rsl_cont").val()) > 4000){
		parent.$.messager.alert('','해결 방안 내용을 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(!formValidationCheck()) {
		return;
	}
	
	$('#qty_chk_idt_cd').val($('#ui_qty_chk_idt_cd').textbox('getValue'));
	
	ajaxCall("/saveMaintainQualityCheckIndicator",
			$("#detail_form"),
			"POST",
			callback_SaveSettingAction);
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
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
	$("#rowStatus").val("u");
	$("#ui_qty_chk_idt_cd").textbox("setValue", selRow.qty_chk_idt_cd);
	$("#ui_qty_chk_idt_cd").textbox({disabled: true});
	$("#qty_chk_idt_cd").val(selRow.inst_id);
	$("#qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	$("#qty_chk_idt_yn").combobox("setValue", selRow.qty_chk_idt_yn);
	$("#srt_ord").textbox("setValue", selRow.srt_ord);
	$("#qty_chk_cont").val(selRow.qty_chk_cont);
	$("#slv_rsl_cont").val(selRow.slv_rsl_cont);
}

function Btn_ResetField(){
	$("#tableList").datagrid("unselectAll");
	selectedRowData = null;
	
	$("#rowStatus").val("i");
	$("#ui_qty_chk_idt_cd").textbox("setValue", "");
	$("#ui_qty_chk_idt_cd").textbox({disabled: false});
	$("#qty_chk_idt_cd").val("");
	$("#qty_chk_idt_nm").textbox("setValue", "");
	$("#qty_chk_idt_yn").combobox("setValue", "");
	$("#srt_ord").textbox("setValue", "");
	$("#qty_chk_cont").val("");
	$("#slv_rsl_cont").val("");
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

function Btn_DeleteSetting(){
	var row = selectedRowData;
	
	if(row == null) {
		return;
	}
	
	$('#detail_form #qty_chk_idt_cd').val(selectedRowData.qty_chk_idt_cd);
	
	parent.$.messager.confirm('Confirm','품질 점검 지표를 삭제하시겠습니까?',function(r){
		console.log( $("#detail_form").serialize());
		if (r){
			ajaxCall("/deleteMaintainQualityCheckIndicator",
					$("#detail_form"),
					"POST",
					callback_DeleteSettingAction);
		}
	});
}

function formValidationCheck(){
	if(onlyNumChk($("#ui_qty_chk_idt_cd").textbox("getValue")) == false) {
		parent.$.messager.alert('','품질 점검 지표 코드는 숫자만 입력해 주세요.');
		return false;
	}
	
	if(onlyNumChk($("#srt_ord").textbox("getValue")) == false) {
		parent.$.messager.alert('','정렬 순서는 숫자만 입력해 주세요.');
		return false;
	}
	
	if($("#ui_qty_chk_idt_cd").textbox("getValue").length > 10) {
		parent.$.messager.alert('','품질 점검 지표 코드는 10자리까지 허용됩니다.');
		return false;
	}
	
	if($("#srt_ord").textbox("getValue").length > 3) {
		parent.$.messager.alert('','정렬 순서는 3자리까지 허용됩니다.');
		return false;
	}
	
	return true;
}

function Excel_Download(){
	$("#submit_form").attr("action","/excelDownMaintainQualityCheckIndicator");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
