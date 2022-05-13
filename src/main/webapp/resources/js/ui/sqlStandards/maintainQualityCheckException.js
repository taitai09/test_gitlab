var selectedRowData;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 품질점검 지표코드(검색영역)
	$('#submit_form #qty_chk_idt_cd').combobox({
		url:"/getQtyChkIdtCdFromException?dml_yn=Y",
		method:"get",
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 예외대상 객체구분(검색영역)
	$('#submit_form #search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=N&isAll=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onChange:function(newval,oldval){
			$('#submit_form #wrkjob_cd').val(newval);
		}
	});
	
	// 품질점검 지표코드
	$('#detail_form #bottom_qty_chk_idt_cd').combobox({
		url:"/getQtyChkIdtCdFromException?dml_yn=N",
		method:"get",
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_cd_nm',
		onLoadSuccess:function(){
			$ ('#bottom_qty_chk_idt_cd').combobox ('textbox').attr ( 'placeholder', '선택');
		},
		onChange:function(newval,oldval){
			$('#detail_form #qty_chk_idt_nm').textbox("setValue", newval);
		},
		onLoadError: function(){
			parent.$.messager.alert('','품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
		
	});
	
	// 예외대상 객체구분
	$('#detail_form #bottom_search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=N",
		method:'get',
		valueField:'id',
		textField:'text',
		onChange:function(newval,oldval){
			$('#detail_form #wrkjob_cd').val(newval);
		},
		onLoadSuccess: function() {
			$('#detail_form #bottom_search_wrkjob_cd').combobox('textbox').attr('placeholder','선택');
		}
	});
	
	getTableList();
	
	$("#rowStatus").val("i");
	
	Btn_OnClick();
});

function getTableList(){
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			selectedRowData = row;
			setDetailView(row);
		},
		columns:[[
			{field:'qty_chk_idt_cd',title:'품질 점검<br>지표 코드',width:"5%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"10%",halign:"center",align:'left'},
			{field:'wrkjob_cd',title:'업무 코드',width:"5%",halign:"center",align:'center'},
			{field:'dir_nm',title:'디렉토리명',width:"20%",halign:"center",align:'left'},
			{field:'dbio',title:'SQL 식별자(DBIO)',width:"30%",halign:"center",align:'left'},
			{field:'except_sbst',title:'예외사유',width:"10%",halign:"center",align:'left'},
			{field:'requester',title:'요청자',width:"5%",halign:"center",align:'center'},
			{field:'reg_dt',title:'등록 일시',width:"10%",halign:"center",align:'center'},
			{field:'sql_hash',hidden:true},
			{field:'sql_length',hidden:true}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
};

function Btn_SaveSetting(){
	if($("#detail_form #bottom_qty_chk_idt_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','품질점검 지표코드를 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #bottom_search_wrkjob_cd").combobox('getValue') == "" &&
			$("#detail_form #dbio").val() == "" &&
			$("#dir_nm").val() == ""){
		parent.$.messager.alert('','업무, SQL 식별자(DBIO), 디렉토리 중  하나를 입력해 주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #dir_nm").val()) > 400){
		parent.$.messager.alert('','디렉토리는 400Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #dbio").val()) > 4000){
		parent.$.messager.alert('','SQL 식별자(DBIO)는 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #requester").val()) > 100){
		parent.$.messager.alert('','요청자는 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#detail_form #except_sbst").val()) > 1000){
		parent.$.messager.alert('','예외사유는 100Byte 이내로 입력해주세요.');
		return false;
	}
	
	$("#detail_form #qty_chk_idt_cd").val($("#detail_form  #bottom_qty_chk_idt_cd").combobox('getValue'));
	$("#detail_form #wrkjob_cd").val($("#detail_form  #bottom_search_wrkjob_cd").combobox('getValue'));
	
	ajaxCall("/saveMaintainQualityCheckException",
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
		parent.$.messager.alert('',result.message,'');
	}
};

function Btn_DeleteSetting(){
	var row = selectedRowData;
	
	if(row == null) {
		return;
	}
	
	$("#detail_form #qty_chk_idt_cd").val(row.qty_chk_idt_cd);
	$("#detail_form #wrkjob_cd").val(row.wrkjob_cd);
	$("#detail_form #dir_nm").val(row.dir_nm);
	$("#detail_form #dbio").val(row.dbio);
	
	parent.$.messager.confirm('', '정말 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deleteMaintainQualityCheckException",
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
	$("#rowStatus").val("u");
	$("#detail_form #bottom_qty_chk_idt_cd").textbox({disabled: true});
	$("#detail_form #bottom_search_wrkjob_cd").textbox({disabled: true});
	
	$("#detail_form #bottom_qty_chk_idt_cd").combobox("setValue", selRow.qty_chk_idt_cd);
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", selRow.qty_chk_idt_nm);
	$("#detail_form #bottom_search_wrkjob_cd").combotree("setValue", selRow.wrkjob_cd);
	$("#detail_form #dir_nm").textbox("setValue", selRow.dir_nm);
	$("#detail_form #dir_nm").textbox("readonly", true);
	$("#detail_form #dbio").textbox("setValue", selRow.dbio);
	$("#detail_form #dbio").textbox("readonly", true);
	$("#detail_form #requester").textbox("setValue", selRow.requester);
	$("#detail_form #except_sbst").textbox("setValue", selRow.except_sbst);
};

function Btn_ResetField(){
	$("#tableList").datagrid("unselectAll");
	selectedRowData = null;
	
	$("#rowStatus").val("i");
	$("#detail_form #bottom_qty_chk_idt_cd").textbox({disabled: false});
	$("#detail_form #bottom_search_wrkjob_cd").textbox({disabled: false});
	
	$("#detail_form #qty_chk_idt_cd").val("");
	$("#detail_form #bottom_qty_chk_idt_cd").combobox("setValue", "");
	$("#detail_form #qty_chk_idt_nm").textbox("setValue", "");
	$("#detail_form #bottom_search_wrkjob_cd").combotree("setValue", "");
	$("#detail_form #dir_nm").textbox("setValue", "");
	$("#detail_form #dir_nm").textbox("readonly", false);
	$("#detail_form #dbio").textbox("setValue", "");
	$("#detail_form #dbio").textbox("readonly", false);
	$("#detail_form #requester").textbox("setValue", "");
	$("#detail_form #except_sbst").textbox("setValue", "");
	
	$('#bottom_qty_chk_idt_cd').combobox('textbox').attr( 'placeholder', '선택');
	$('#bottom_search_wrkjob_cd').combobox('textbox').attr( 'placeholder', '선택');
	
}

function Excel_Download(){
	$("#submit_form").attr("action","/excelDownMaintainQualityCheckException");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

//일괄업로드 팝업
function Excel_Upload(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#maintainQualityCheckException_excel_upload_popup').window("open");
}

function fnSearch(){
	ajaxCallTableList();
}

function Btn_OnClick(){
	if(!formValidationCheck()){
		return false;
	}
	
	if($("#submit_form #dbio").val() != null && $("#submit_form #dbio").val() != '' &&
			byteLength($("#submit_form #dbio").val()) > 4000){
		parent.$.messager.alert('','SQL 식별자(DBIO)는 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("10");
	
	$('#submit_form #tableList').datagrid("loadData", []);
	
	ajaxCallTableList();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
}

function ajaxCallTableList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 품질 검토 예외 대상"," ");
	
	ajaxCall("/maintainQualityCheckException",
			$("#submit_form"),
			"POST",
			callback_BtnOnclickAction);
}

//callback 함수
var callback_BtnOnclickAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
};

function formValidationCheck(){
	return true;
}
/*페이징처리끝*/
