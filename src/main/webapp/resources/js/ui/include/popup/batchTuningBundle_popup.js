$(document).ready(function() {
	$('#batchTuningBundlePop').window({
		title : "일괄 튜닝 종료",
		top:getWindowTop(280),
		left:getWindowLeft(440)
	});
});

function Btn_OnSave(){	
	if($('#selectTuningEnd').combobox('getValue') == ""){
		$.messager.alert('','튜닝종료사유를 선택해 주세요.');
		return false;
	}
	
	if($('#tuning_end_why').val() == ""){
		$.messager.alert('','튜닝종료내용을 입력해 주세요.');
		return false;
	}

	$("#tuning_end_why_cd").val($('#selectTuningEnd').combobox('getValue'));
	
	ajaxCall("/SQLTuningTarget/Popup/EndBatchTuningBundle",
			$("#batchTuningBundle_form"),
			"POST",
			callback_EndBatchTuning);
}

//callback 함수
var callback_EndBatchTuning = function(result) {
	if(result.result){
		$.messager.alert('','일괄 튜닝 종료가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClosePopup('batchTuningBundlePop');
//				eval("if_"+frameName).showDetailTable();
				showDetailTable();
			},1000);			
		});
	}
};