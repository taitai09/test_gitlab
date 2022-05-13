$(document).ready(function() {
	$('#maintainQualityCheckException_excel_upload_popup').window({
		title : "SQL 품질점검 예외 대상 관리 엑셀 업로드",
		top:getWindowTop(160),
		left:getWindowLeft(420)
	});
});

function Btn_UploadQualityRevExcMngExcelAction(){
	var objFile = $('#maintainQualityCheckException_form #uploadFile').textbox('getValue');
	var ext = objFile.substring(objFile.lastIndexOf(".")+1);
	if($('#maintainQualityCheckException_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','SQL 품질검토 예외 대상 관리에 업로드할 <BR/>엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	var formData = new FormData($("#maintainQualityCheckException_form")[0]);
	console.log("formData:",formData);
	
	ajaxMultiCall("/excelUploadMaintainQualityCheckException",
			formData,
			"POST",
			callback_UploadExcelFileAction);
}

//callback 함수
var callback_UploadExcelFileAction = function(result) {
	if(result.result){
		var resultText = "엑셀 업로드가 완료되었습니다. <BR/> 총 [ " + result.object.totalCnt +" ] 건 중 [ " + result.object.succCnt + " ] 건 성공하였습니다.";
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
				Btn_OnClosePopup('maintainQualityCheckException_excel_upload_popup');
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('maintainQualityCheckException_excel_upload_popup');
		});
	}
};

//품질검토 예외대상 관리
function Btn_QualityRevExcMngFormDownload(){
	$("#maintainQualityCheckException_form").attr("action","/Common/ExcelFormDownload/Form1_SQL품질점검제외대상");
	$("#maintainQualityCheckException_form").submit();
}
