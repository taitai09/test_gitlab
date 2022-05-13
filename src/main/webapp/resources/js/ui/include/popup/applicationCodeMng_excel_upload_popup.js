$(document).ready(function() {

	
	$('#applicationCodeExcelUploadPop').window({
		title : "애플리케이션 코드 등록",
//		top:getWindowTop(190),
		top:190,
		left:getWindowLeft(420)
	});
	
	// Work Job 조회			
	$('#loadedExcel_form #wrkjob_cd').combobox({
	    url:"/Common/getWrkJob",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		}
	});		
	
	// 성능점검유형 조회
	$('#loadedExcel_form #deploy_perf_check_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1026",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});

	$("#chkAll").click( function (){
		if ( $("#chkAll").is(":checked") ){
			$(".chkCate").prop("checked", true);
		}else{
			$(".chkCate").prop("checked", false);
		}
	});	
});

function Btn_uploadApplicationExcelAction(){
	var objFile = $('#applicationCodeExcelUpload_form #uploadFile').textbox('getValue');
	var ext = objFile.substring(objFile.lastIndexOf(".")+1);
	
	if($('#applicationCodeExcelUpload_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','애플리케이션코드를 등록할 엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	var formData = new FormData($("#applicationCodeExcelUpload_form")[0]);
	console.log("formData:",formData);
	ajaxMultiCall("/ApplicationCodeRegist/Upload",
			formData,
			"POST",
			callback_UploadApplicationCodeExcelFile);
}

//callback 함수
var callback_UploadApplicationCodeExcelFile = function(result) {
	/*if(result.result){
		$.messager.alert('','엑셀 업로드가 완료되었습니다.','info', function(){
			setTimeout(function() {
				Btn_OnClosePopup('applicationCodeExcelUploadPop');
//				eval("if_"+frameName).Btn_OnClick();
				Btn_OnClick();
			},1000);
		});
	}else{
		$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('applicationCodeExcelUploadPop');
		});
	}
	*/
	if(result.result){
		var resultText = "엑셀 업로드가 완료되었습니다. <BR/>총 [ "+result.object.totalCnt+" ] 건 중 실패건수 : [ "+result.object.errCnt+" ]건";
		if(result.object.isErr){
			resultText = "엑셀 업로드가 완료되었습니다. <BR/>총 [ "+result.object.totalCnt+" ] 건 중 실패건수 : [ "+result.object.errCnt+" ]건, <BR/>실패 행 : " + result.object.errIndex +" 번째 행,<BR/>실패 코드 : "+result.object.errTrcd
		}
		$.messager.alert({
			title: '엑셀 업로드 결과',
			msg: resultText,
			height: 'auto',
			width:400,
			maxHeight: 400,
			resizable: true
		});
		setTimeout(function() {
			Btn_OnClosePopup('applicationCodeExcelUploadPop');
//			eval("if_"+frameName).Btn_OnClick();
			Btn_OnClick();
		},3000);
		/*$.messager.alert('',resultText,'', function(){
			setTimeout(function() {
				Btn_OnClosePopup('applicationCodeExcelUploadPop');
//				eval("if_"+frameName).Btn_OnClick();
				Btn_OnClick();
			},1000);
		});*/
	}else{
		$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('applicationCodeExcelUploadPop');
		});
	}
	
	
};

//사용자 업로드용 양식
function Btn_ApplicationCodeFormDownload(){
//	form = document.excel_form;
//	form.action="/ApplicationCode/excelFormDownload/internal";
//	form.target = "hiddenifr";
//	form.submit();
	
	$("#applicationCodeExcelUpload_form").attr("action","/Common/ExcelFormDownload/ApplicationCode_Upload_Form");
	$("#applicationCodeExcelUpload_form").submit();
}
//function downloadExcelTemp(){
//	location.href="/resources/document/ExcelTemplate.zip";	
//}
