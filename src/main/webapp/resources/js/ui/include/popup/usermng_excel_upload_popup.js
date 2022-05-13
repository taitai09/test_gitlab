$(document).ready(function() {
	$('#userExcelUploadPop').window({
		title : "사용자 등록",
		top:getWindowTop(160),
		left:getWindowLeft(420)
	});
	
//	// Work Job 조회			
//	$('#loadedExcel_form #wrkjob_cd').combobox({
//	    url:"/Common/getWrkJob",
//	    method:"get",
//		valueField:'wrkjob_cd',
//	    textField:'wrkjob_cd_nm',
//		onLoadError: function(){
//			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
//		}
//	});		
	
//	// 성능점검유형 조회
//	$('#loadedExcel_form #deploy_perf_check_type_cd').combobox({
//	    url:"/Common/getCommonCode?grp_cd_id=1026",
//	    method:"get",
//		valueField:'cd',
//	    textField:'cd_nm'
//	});

//	$("#chkAll").click( function (){
//		if ( $("#chkAll").is(":checked") ){
//			$(".chkCate").prop("checked", true);
//		}else{
//			$(".chkCate").prop("checked", false);
//		}
//	});	
});

function Btn_uploadUserExcelAction(){
	var objFile = $('#userExcelUpload_form #uploadFile').textbox('getValue');
	var ext = objFile.substring(objFile.lastIndexOf(".")+1);
	if($('#userExcelUpload_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','사용자 등록할 엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	var formData = new FormData($("#userExcelUpload_form")[0]);
	console.log("formData:",formData);

	ajaxMultiCall("/UserRegistExcel/Upload",
			formData,
			"POST",
			callback_UploadUserExcelFile);
}

//callback 함수
var callback_UploadUserExcelFile = function(result) {
	if(result.result){
		var resultText = "엑셀 업로드가 완료되었습니다. <BR/>총 [ "+result.object.totalCnt+" ] 건 중 실패건수 : [ "+result.object.errCnt+" ]건";
		if(result.object.isErr){
			resultText = "엑셀 업로드가 완료되었습니다. <BR/>총 [ "+result.object.totalCnt+" ] 건 중 실패건수 : [ "+result.object.errCnt+" ]건, <BR/>실패 행 : " + result.object.errIndex +" 번째 행,<BR/>실패 아이디 : "+result.object.errUserId
		}
		$.messager.alert('',resultText,'', function(){
			setTimeout(function() {
				Btn_OnClosePopup('userExcelUploadPop');
//				eval("if_"+frameName).Btn_OnClick();
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('userExcelUploadPop');
		});
	}
};

//사용자 업로드용 양식
function Btn_UserFormDownload(){
	form = document.excel_form;
	form.action="/Users/excelFormDownload/internal";
	form.target = "hiddenifr";
	form.submit();
}
//function downloadExcelTemp(){
//	location.href="/resources/document/ExcelTemplate.zip";	
//}
