var resultCnt = -1;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createIdxCombo('#search_qty_chk_idt_cd', 'Y');
	createIdxCombo('#qty_chk_idt_cd', 'N');
	
	createDbCombo();
	createSearchDbCombo();
	
	wrkjobCdComboTree('#search_wrkjob_cd', 'deploy_check_target_yn=N&isAll=Y');
	wrkjobCdComboTree('#wrkjob_cd', 'deploy_check_target_yn=N');
	
	createList();
	createPopUp();
	
	Btn_OnClick();
});

// 품질점검 지표코드
function createIdxCombo(comboboxId, dmlYn){
	$(comboboxId).combobox({
		url:"/execSqlStdChk/getQtyChkIdxCd?dml_yn="+dmlYn,
		method:"get",
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_cd_nm',
		onLoadError: function(){
			errorMessager('품질점검지표코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function createDbCombo(){
	$('#std_qty_target_db_name').combobox({
		url:'/Common/getDatabase',
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onChange: function(newValue,oldValue){
			if(newValue == -1){
				$(this).combobox('setValue', ' ');
			}
			
			$('#dbid').val(newValue);
		}
	});
}

function createSearchDbCombo(){
	$('#search_dbid').combobox({
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	loadDbCombo();
}

function loadDbCombo(){
	ajaxCall("/Common/getDatabase",
			null,
			"GET",
			callback_loadDbCombo);
}

var callback_loadDbCombo = function(result) {
	let data = JSON.parse(result);
	data.unshift({'dbid' : '', 'db_name' : '전체'});
	$('#search_dbid').combobox('loadData',data);
};

// 업무 콤보트리
function wrkjobCdComboTree(comboboxId, param){
	$(comboboxId).combotree({
		url:"/Common/getWrkJobCd?"+param,
		method:'get',
		valueField:'id',
		textField:'text',
	});
}

function createList(){
	$("#tableList").datagrid({
		view: myview,
		columns:[[
			{field:'qty_chk_idt_cd',title:'품질 점검<br>지표 코드',width:"4%",halign:"center",align:"center"},
			{field:'qty_chk_idt_nm',title:'품질 점검 지표명',width:"16%",halign:"center",align:'left'},
			{field:'std_qty_target_db_name',title:'표준점검DB',width:'5%',halign:'center',align:'center'},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'center'},
			{field:'dbio',title:'SQL 식별자(DBIO)',width:"17%",halign:"center",align:'left'},
			{field:'wrkjob_cd',title:'업무 코드',width:"4%",halign:"center",align:'center'},
			{field:'dir_nm',title:'디렉토리명',width:"16%",halign:"center",align:'left'},
			{field:'except_sbst',title:'예외사유',width:"14%",halign:"center",align:'left'},
			{field:'requester',title:'요청자',width:"5%",halign:"center",align:'center'},
			{field:'reg_dt',title:'등록일시',width:"8%",halign:"center",align:'center'},
			{field:'user_id',title:'등록자',width:'5%',halign:'center',align:'center'},
			{field:'dbid', hidden: true}
		]],
		onSelect : function(index,row) {
			setDataToInput(row);
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	});
};

/* 저장 */
function Btn_SaveSetting(){
	if(essentialCheck() == true && additionalCheck() == true){
		ajaxCall_SaveSetting();
	}
}

function ajaxCall_SaveSetting(){
	ajaxCall("/execSqlStdChk/saveException",
			$("#detail_form"),
			"POST",
			callback_SaveSettingAction);
}

var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);
		});
	}else{
		errorMessager(result.message);
	}
};

/* 수정 */
function Btn_ModifySetting(){
	if(additionalCheck() == true){
		ajaxCall_ModifySetting();
	}
}

function ajaxCall_ModifySetting(){
	ajaxCall("/execSqlStdChk/modifyException",
			$("#detail_form"),
			"POST",
			callback_ModifySettingAction);
}

var callback_ModifySettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);
		});
	}else{
		errorMessager(result.message);
	}
};

/* 삭제 */
function Btn_DeleteSetting(){
	let row = $("#tableList").datagrid('getSelected');
	
	if(row == null) {
		warningMessager('데이터를 선택해 주세요.');
		return;
	}else {
		confirmMessager('정말 삭제 하시겠습니까?',
				'/execSqlStdChk/deleteException',
				$("#detail_form"),
				'POST',
				callback_DeleteSettingAction);
	}
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
		errorMessager(result.message);
	}
};

function setDataToInput(data){
	if( data == null ){
		$("#tableList").datagrid("unselectAll");
		
		$("#qty_chk_idt_cd").textbox("readonly", false);
		$("#qty_chk_idt_cd").combobox("clear");
		
		$("#std_qty_target_db_name").combobox("clear");
		$("#std_qty_target_db_name").textbox("readonly", false);
		
		$("#sql_id").textbox('clear');
		$("#sql_id").textbox("readonly", false);
		
		$("#dbio").textbox('clear');
		$("#dbio").textbox("readonly", false);
		
		$("#wrkjob_cd").combotree('clear');
		$("#wrkjob_cd").combotree("readonly", false);
		
		$("#dir_nm").textbox('clear');
		$("#dir_nm").textbox("readonly", false);
		
		$("#requester").textbox('clear');
		$("#except_sbst").textbox('clear');
		
		$('#btnSave').attr('onClick','Btn_SaveSetting();');
		
	}else {
		$("#qty_chk_idt_cd").combobox("setValue", data.qty_chk_idt_cd);
		$("#qty_chk_idt_cd").textbox("readonly", true);
		
		$("#std_qty_target_db_name").combobox("setValue", data.dbid);
		$("#std_qty_target_db_name").textbox("readonly", true);
		
		$("#sql_id").textbox("setValue", data.sql_id);
		$("#sql_id").textbox("readonly", true);
		
		$("#dbio").textbox("setValue", data.dbio);
		$("#dbio").textbox("readonly", true);
		
		$("#wrkjob_cd").combotree("setValue", data.wrkjob_cd);
		$("#wrkjob_cd").combotree("readonly", true);
		
		$("#dir_nm").textbox("setValue", data.dir_nm);
		$("#dir_nm").textbox("readonly", true);
		
		$("#requester").textbox("setValue", data.requester);
		$("#except_sbst").textbox("setValue", data.except_sbst);
		
		$('#btnSave').attr('onClick','Btn_ModifySetting();');
	}
};

function Btn_ResetField(){
	setDataToInput(null);
}

function Excel_Download(){
	if( resultCnt < 0 ){
		warningMessager('데이터 조회 후 엑셀 다운로드 바랍니다.');
		return false;
	}
	
	$("#excel_form").attr("action","/execSqlStdChk/loadExceptionList/excelDownload");
	$("#excel_form").submit();
	$("#excel_form").attr("action","");
}

function fnSearch(){
	ajaxCall_ExceptionList();
}

function Btn_OnClick(){
	let dbio = $('#search_wrkjob_cd').val();
	
	if( formValidationCheck() == false ){
		warningMessager('SQL 식별자(DBIO)는 4000Byte 이내로 입력해 주세요.');
		return false;
	
	}else {
		$("#currentPage").val("1");
		$("#pagePerCount").val("10");
		
		Btn_ResetField();
		
		$('#tableList').datagrid("loadData", []);
		
		$("#excel_form [name=qty_chk_idt_cd]").val( $("#search_qty_chk_idt_cd").combobox('getValue') );
		$("#excel_form [name=dbid]").val( $("#search_dbid").combobox('getValue') );
		$("#excel_form [name=wrkjob_cd]").val( $("#search_wrkjob_cd").combobox('getValue') );
		$("#excel_form [name=dbio]").val( $("#search_dbio").textbox('getValue') );
		$("#excel_form [name=sql_id]").val( $("#search_sql_id").textbox('getValue') );
		
		ajaxCall_ExceptionList();
	}
}

function formValidationCheck(){
	if( dbio != null && dbio != '' && byteLength(dbio) > 4000){
		warningMessager('SQL 식별자(DBIO)는 4000Byte 이내로 입력해 주세요.');
		return false;
	}
	return true;
}

function ajaxCall_ExceptionList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 표준 점검 예외 대상"," ");
	
	ajaxCall("/execSqlStdChk/loadExceptionList",
			$("#submit_form"),
			"POST",
			callback_ExceptionList);
}

var callback_ExceptionList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	let data = JSON.parse(result);
	
	fnEnableDisablePagingBtn(data.dataCount4NextBtn);
	resultCnt = data.totalCount;
};

function essentialCheck(){
	if($("#qty_chk_idt_cd").combobox('getValue') == ""){
		warningMessager('품질점검 지표코드를 선택해 주세요.');
		return false;
	}
	
	let dbid = $("#dbid").val();
	let dir_nm = $("#dir_nm").textbox('getValue');
	let dbio = $("#dbio").textbox('getValue');
	let sql_id = $("#sql_id").textbox('getValue');
	
	if( $("#wrkjob_cd").combobox('getValue') == "" &&
			dbio == "" &&
			dir_nm == "" &&
			dbid == "" && sql_id == "" ){
		
		warningMessager('업무, SQL 식별자(DBIO), 디렉토리 중 하나를 입력하거나, '
						+'표준점검DB, SQL ID를 입력해 주세요.');
		
		return false;
	}
	
	if( dbid != "" && sql_id == "" ){
		warningMessager('SQL ID를 입력해 주세요.');
		return false;
	}
	
	if( sql_id != "" && dbid == "" ){
		warningMessager('표준점검DB를 선택해 주세요.');
		return false;
	}
	
	if(byteLength( dbio ) > 4000){
		warningMessager('SQL 식별자(DBIO)는 4000Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength( dir_nm ) > 400){
		warningMessager('디렉토리는 400Byte 이내로 입력해주세요.');
		return false;
	}
	
	return true;
}

function additionalCheck(){
	if(byteLength($("#requester").textbox('getValue')) > 100){
		warningMessager('요청자는 100Byte 이내로 입력해주세요.');
		return false;
	}
	
	if(byteLength($("#except_sbst").textbox('getValue')) > 1000){
		warningMessager('예외사유는 1000Byte 이내로 입력해주세요.');
		return false;
	}
	
	return true;
}

//일괄업로드 팝업
function createPopUp(){
	$('#maintainQualityCheckException_excel_upload_popup').window({
		title : "SQL 품질점검 예외 대상 관리 엑셀 업로드",
		top:getWindowTop(160),
		left:getWindowLeft(420)
	});
}

function Excel_Upload(){
	parent.frameName = $("#menu_id").val();		// iframe name에 사용된 menu_id를 상단 frameName에 설정
	
	$('#maintainQualityCheckException_form #uploadFile').textbox('clear');
	$('#maintainQualityCheckException_excel_upload_popup').window("open");
}

// 템플릿 다운로드
function Btn_QualityRevExcMngFormDownload(){
	$("#maintainQualityCheckException_form").attr("action","/Common/ExcelFormDownload/Form2_SQL품질점검제외대상");
	$("#maintainQualityCheckException_form").submit();
}

// 엑셀 업로드
function Btn_UploadQualityRevExcMngExcelAction(){
	let objFile = $('#maintainQualityCheckException_form #uploadFile').textbox('getValue');
	let ext = objFile.substring(objFile.lastIndexOf(".")+1);
	
	if($('#maintainQualityCheckException_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','SQL 표준 점검 예외 대상 관리에 업로드할 <BR/>엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	let formData = new FormData($("#maintainQualityCheckException_form")[0]);
	
	ajaxMultiCall("/execSqlStdChk/excelUpload",
					formData,
					"POST",
					callback_UploadExcelFileAction);
}
var callback_UploadExcelFileAction = function(result) {
	if(result.result){
		let resultText = "엑셀 업로드가 완료되었습니다. <BR/> 총 [ " + result.object.totalCnt +" ] 건 중 [ " + result.object.succCnt + " ] 건 성공하였습니다.";
		if(result.object.isErr){
			resultText = "엑셀 업로드가 완료되었습니다. <BR/>" +
						"총 [ "+result.object.totalCnt+" ] 건 중 성공건수 : [ "+result.object.succCnt+" ]건, <BR/>" +
						"실패 행 : " +result.object.errIndex +" 번째 행";
			if(result.object.errMsg != null && result.object.errMsg != 'undefined'){
				resultText +="<BR/>"+"마지막으로 오류가 발생한 원인<BR/>"+result.object.errMsg;
			}
		}
		
		$.messager.alert('',resultText,'info', function(){
			setTimeout(function() {
				$('#maintainQualityCheckException_form #uploadFile').textbox('clear');
				Btn_OnClosePopup('maintainQualityCheckException_excel_upload_popup');
				
				Btn_OnClick();
			},1000);
		});
		
	}else{
		$.messager.alert('',result.message,'error', function(){
			$('#maintainQualityCheckException_form #uploadFile').textbox('clear');
			Btn_OnClosePopup('maintainQualityCheckException_excel_upload_popup');
		});
	}
};