$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 업무 조회
	$('#selectWrkJob').combotree({
		url:"/Common/getWrkJobCd",
		method:'get',
		onChange:function(newValue, oldValue){
			Btn_OnClick(newValue);
		},
		onLoadSuccess:function(){
			$('#selectWrkJob').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$("#basicInfoTab").tabs({
		plain:true
	});
	
	$("#selectWrkJob").combotree("setValue",$("#submit_form #wrkjob_cd").val());

	var tabIndex = 0;
	if($("#pref_grp").val() != ""){
		var prefGrp = $("#pref_grp").val();
		tabIndex = parseInt($("."+prefGrp+"_rownum").val());
	}
	
	$('#basicInfoTab').tabs('select', tabIndex);
});

function Btn_OnClick(strDbid){
	if($("#wrkjob_cd").val() != strDbid){
		$("#wrkjob_cd").val(strDbid);
		
		$("#submit_form").attr("action","/WorkJobInfo");
		$("#submit_form").submit();
	}
}

function Btn_SaveBasic(grpId){
	var obj = $("."+grpId+"_pref_sub_id");
	var chkErrorCnt = 0;
	var valueArr = new Array();
	var wrkjob_cd = $("#wrkjob_cd").val();
	let pref_mgmt_type_cd = $("#pref_mgmt_type_cd").val();
	
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
			let pref_id = grpId+subId;
			value = {"pref_mgmt_type_cd" : pref_mgmt_type_cd,
					"wrkjob_cd":wrkjob_cd,
					"pref_id": pref_id,
					"pref_value": inputValue}
			valueArr.push(value);
		}
	}

	if(chkErrorCnt > 0){
		parent.$.messager.alert('','기준정보 값이 공백이거나 입력범위를 벗어났습니다.');
		return false;
	}
	
	$("#submit_form #pref_grp").val(grpId);
	
	$("#detail_form #wrkjob_cd").val($("#submit_form #wrkjob_cd").val());
	
	ajaxCallWithJson("/InsertBasicInformationJson",
			valueArr,
			"POST",
			callback_InsertBasicInformation);
}

//callback 함수
var callback_InsertBasicInformation = function(result) {
	if(result.result){
		parent.$.messager.alert('','적용 되었습니다.','info',function(){
			setTimeout(function() {
				var tempDbid = $("#wrkjob_cd").val();
				$("#wrkjob_cd").val("");
				Btn_OnClick(tempDbid);
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			setTimeout(function() {
				var tempDbid = $("#wrkjob_cd").val();
				$("#wrkjob_cd").val("");
				Btn_OnClick(tempDbid);
			},1000);	
		});		
	}
};

/*
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
		parent.$.messager.alert('','기준정보 값이 공백이거나 입력범위를 벗어났습니다.');
		return false;
	}
	
	$("#submit_form #pref_grp").val(grpId);
	
	$("#detail_form #wrkjob_cd").val($("#submit_form #wrkjob_cd").val());
	$("#detail_form #prefIdArry").val(strRight(idArry,1));
	$("#detail_form #prefValueArry").val(strRight(valueArry,1));
	
	
	ajaxCall("/InsertBasicInformation",
			$("#detail_form"),
			"POST",
			callback_InsertBasicInformation);
}
*/