$(document).ready(function() {
	$('#excelUploadPop').window({
		title : "성능점검 등록",
		top:getWindowTop(160),
		left:getWindowLeft(420)
	});
	
	$('#loadedExcelPop').window({
		title : "성능점검 등록 - Excel Upload 결과",
		top:getWindowTop(700),
		left:getWindowLeft(500)
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

function uploadExcelAction(){
	var objFile = $('#excelUpload_form #uploadFile').textbox('getValue');
	//var ext = objFile.substring(objFile.length-3,objFile.length);
	var ext = objFile.substring(objFile.lastIndexOf(".")+1);
	//console.log("objFile:"+objFile);
	//console.log("ext:"+ext);
	
	if($('#excelUpload_form #uploadFile').textbox('getValue') == ""){
		$.messager.alert('','성능점검등록할 엑셀 파일을 선택해 주세요.');
		return false;
	}
	
	if (ext != "xls" && ext != "xlsx") {
		$.messager.alert('','엑셀 파일만 업로드가 가능합니다.');
		return false;
	}
	
	var formData = new FormData($("#excelUpload_form")[0]);

	ajaxMultiCall("/ExcelPerformanceCheck/Upload",
			formData,
			"POST",
			callback_UploadExcelFile);
}

//callback 함수
var callback_UploadExcelFile = function(result) {
	if(result.result){
//		$.messager.alert('','엑셀 업로드가 완료되었습니다.','info', function(){
//			setTimeout(function() {
//				Btn_OnClosePopup('excelUploadPop');
//				setPopTableList(result);
//				$('#loadedExcelPop').window("open");
//			},1000);
//		});
		Btn_OnClosePopup('excelUploadPop');
		setPopTableList(result);
		
		// 위치 초기화
		$('#loadedExcelPop').window({
			top:getWindowTop(700),
			left:getWindowLeft(500)
		});
		
		$('#loadedExcelPop').window("open");
	}else{
		$.messager.alert('',result.message,'error', function(){
			$("#popTableList tbody tr").remove();
			Btn_OnClosePopup('excelUploadPop');
		});
	}
};

function setPopTableList(result){
	$("#loadedExcel_form #popTableList tbody tr").remove();
	var strHtml = "";
	for(var i = 0 ; i < result.object.length ; i++){
		var post = result.object[i];
		var backValue = "style='background:#edf3fb;'";
		var chkValue = "checked";

		strHtml += "<tr "+backValue+">";
		strHtml += "<td class='ctext'><input type='checkbox' id='chk"+i+"' name='chk' value='"+i+"' class='chkCate chkBox' "+chkValue+" onClick='setBackground("+i+");'/></td>";
		strHtml += "<td><input type='hidden' id='upload_flag"+i+"' name='upload_flag' value='Y'/><input type='hidden' id='tr_cd"+i+"' name='tr_cd' value='"+post.tr_cd+"'/>"+post.tr_cd+"</td>";
		strHtml += "</tr>";
	}
	
	$("#loadedExcel_form #popTableList tbody").append(strHtml);
}

function setBackground(rowIndex){
	if($("#chk"+rowIndex).is(":checked")){
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#edf3fb");
	}else{
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#ffffff");
	}
}

function saveExcelAction(){
	var insertCnt = 0;
	
	if($("#loadedExcel_form #wrkjob_cd").combobox("getValue") == ""){
		$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if($("#loadedExcel_form #deploy_perf_check_type_cd").combobox("getValue") == ""){
		$.messager.alert('','성능점검유형을 선택해 주세요.');
		return false;
	}
	
	if($("#loadedExcel_form #deploy_day").textbox("getValue") == ""){
		$.messager.alert('','배포일자를 입력해 주세요.');
		return false;
	}
	
	$('.chkCate').each(function(){
		if(this.checked){
			insertCnt++;
	        $("#loadedExcel_form #upload_flag"+$(this).val()).val("Y");
		}else{
			$("#loadedExcel_form #upload_flag"+$(this).val()).val("N");
		}
	});
	
	if(insertCnt < 1){
		$.messager.alert('','성능점검등록할 데이터가 없습니다.');
		return false;
	}

	ajaxCall("/ExcelPerformanceCheck/Save",
			$("#loadedExcel_form"),
			"POST",
			callback_SaveExcelPerformanceCheckAction);	
}

//callback 함수
var callback_SaveExcelPerformanceCheckAction = function(result) {
	if(result.result){
		$.messager.alert('','성능점검 등록이 완료 되었습니다.','info', function(){
			setTimeout(function() {
				Btn_OnClosePopup('loadedExcelPop');
//				eval("if_"+frameName).Btn_OnClick();
				Btn_OnClick();
			},1000);			
		});
	}
};

function downloadExcelTemp(){
	location.href="/resources/document/ExcelTemplate.zip";	
}

function closeLoadedExcelPopup(){
	$.messager.confirm('', '업로드된 결과는  DB에 저장이 안된 상태입니다.<br/>창을 닫으시겠습니까?', function(r){
		if(r){
			Btn_OnClosePopup('loadedExcelPop');
		}
	});
}