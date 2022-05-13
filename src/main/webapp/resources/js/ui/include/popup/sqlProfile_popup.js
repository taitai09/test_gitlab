$(document).ready(function() {
	$('#sqlProfilePop').window({
		title : "SQL PROFILE",
		top:getWindowTop(230),
		left:getWindowLeft(500)
	});
});

function Btn_SaveSqlProfile(){
	var profile = $("#sqlProfile").textbox("getValue");
	
	if(profile == ""){
		$.messager.alert('','PROFILE명을 입력해 주세요.');
		return false;
	}
	
	if(profile.length > 30){
		$.messager.alert('','PROFILE명은 30자 이내로 입력해 주세요.');
		return false;		
	}

	$("#profile_form #sql_profile").val($("#sqlProfile").textbox("getValue"));	
//	$("#profile_form #dbid").val($("#selectDbidCombo").combobox("getValue"));	
	$("#profile_form #dbid").val($("#dbid").val());	

	ajaxCall("/Common/SQLProfileApply",
			$("#profile_form"),
			"POST",
			callback_SQLProfileApplyAction);	
}

//callback 함수
var callback_SQLProfileApplyAction = function(result) {
	if(result.result){
		$.messager.alert('','SQL Profile 적용이 완료 되었습니다.','info', function(){
			setTimeout(function() {
				if($("#save_yn").val() == "Y"){
					// 프로파일명 전달
//					eval("if_"+frameName).$("#submit_form #sql_profile_nm").val($("#profile_form #sqlProfile").textbox("getValue"));
//					eval("if_"+frameName).SqlProfileUpdate();					
					$("#submit_form #sql_profile_nm").val($("#profile_form #sqlProfile").textbox("getValue"));
					SqlProfileUpdate();					
				}
				
				Btn_OnClosePopup('sqlProfilePop');
			},1000);			
		});
	}else{
		$.messager.alert('',result.message,'error', function(){
			setTimeout(function() {
				Btn_OnClosePopup('sqlProfilePop');
			},1000);			
		});
	}
};