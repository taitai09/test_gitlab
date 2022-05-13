$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#basicInfoTab").tabs({
		plain:true
	});

	var tabIndex = 0;
//	if($("#pref_grp").val() != ""){
//		tabIndex = parseInt(strRight($("#pref_grp").val(),1)) - 1;
//	}
	
	if($("#pref_grp").val() != ""){
		if(parseInt($("#pref_grp").val())==16) tabIndex = 0;
		else if(parseInt($("#pref_grp").val())==20) tabIndex = 1;
		else if(parseInt($("#pref_grp").val())==22) tabIndex = 2;
		else alert("menu 정보가 없습니다.")
	}
	
	$('#basicInfoTab').tabs('select', tabIndex);
});

function Btn_OnClick(){
	$("#submit_form").attr("action","/SystemInfo");
	$("#submit_form").submit();	
}

function Btn_SaveBasic(grpId){
	var obj = $("."+grpId+"_pref_sub_id");
	var chkErrorCnt = 0;
	var idArry = "";
	var valueArry = "";
	
	for (var i = 0 ; i < obj.size() ; i++) {
		var subId = obj.eq(i).val();
		var dataTypeCd = $("#"+grpId+subId+"_pref_data_type_cd").val();
		var prePrefValue = $("#"+grpId+subId+"_pre_pref_value").val();
		var minValue = $("#"+grpId+subId+"_min_pref_value").val();
		var maxValue = $("#"+grpId+subId+"_max_pref_value").val();
		var inputValue = $("#"+grpId+subId).textbox("getValue");

		if(inputValue == ""){
			chkErrorCnt++;
		}
		
		if(dataTypeCd == "1"){
			if(isNaN(inputValue)) {
				chkErrorCnt++;
			}
			
			if((parseFloat(minValue) > parseFloat(inputValue)) || (parseFloat(inputValue) > parseFloat(maxValue))){
				chkErrorCnt++;	
			}
		}

		// 이전 값과 다른 경우만 저장함..
		if(prePrefValue != inputValue){
			idArry += grpId+subId + "|";
			valueArry += $.trim(inputValue) + "|";			
		}
	}

	if(chkErrorCnt > 0){
		parent.$.messager.alert('','기준정보 값이 입력범위를 벗어났습니다.');
		return false;
	}
	
	$("#submit_form #pref_grp").val(grpId);
	
	$("#detail_form #prefIdArry").val(strRight(idArry,1));
	$("#detail_form #prefValueArry").val(strRight(valueArry,1));
	
	ajaxCall("/InsertBasicInformation",
			$("#detail_form"),
			"POST",
			callback_InsertBasicInformation);
}

//callback 함수
var callback_InsertBasicInformation = function(result) {
	if(result.result){
		parent.$.messager.alert('','적용 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});		
	}
};