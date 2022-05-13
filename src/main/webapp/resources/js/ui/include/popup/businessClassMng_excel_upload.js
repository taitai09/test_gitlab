$(document).ready(function() {
	$('#businessClassMng_excel_upload_popup').window({
		title : "업무 분류 체계 관리 엑셀 업로드",
		top:getWindowTop(160),
		left:getWindowLeft(420)
	});
	
});



function Btn_UploadBusinessClassMng(){
	var objFile = $('#businessClassMng_form #uploadFile').textbox('getValue');
	var ext = objFile.substring(objFile.lastIndexOf(".")+1);
	if($('#businessClassMng_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','업무 분류체계 관리 엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	var formData = new FormData($("#businessClassMng_form")[0]);
	console.log("formData:",formData);

	ajaxMultiCall("/Mqm/BusinessClassMng/ExcelUpload",
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
				resultText += "<BR/>"+result.object.errMsg;
			}
		}
		$.messager.alert('',resultText,'info', function(){
			setTimeout(function() {
				Btn_OnClosePopup('businessClassMng_excel_upload_popup');
//				eval("if_"+frameName).Btn_OnClick();
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('businessClassMng_excel_upload_popup');
		});
	}
};



//품질검토 예외대상 관리
function Btn_BusinessClassMngFormDownload(){
	$("#businessClassMng_form").attr("action","/Common/ExcelFormDownload/BusinessClassMng_Upload_Form");
	$("#businessClassMng_form").submit();
	
	//	form = document.excel_form;
//	form.action="/Mqm/BusinessClassMng/internal";
//	form.target = "hiddenifr";
//	form.submit();
}
