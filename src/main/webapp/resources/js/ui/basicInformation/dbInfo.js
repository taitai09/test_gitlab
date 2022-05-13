$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onChange:function(newValue, oldValue){
			Btn_OnClick(newValue);
		},
		onLoadSuccess:function(){
			$('#selectCombo').combobox("textbox").attr("placeholder","선택");
		}
	});

	$("#basicInfoTab").tabs({
		plain:true
	});
	
	$("#selectCombo").combobox("setValue",$("#dbid").val());

	var tabIndex = 0;
	if($("#pref_grp").val() != ""){
		var prefGrp = $("#pref_grp").val();
		tabIndex = parseInt($("."+prefGrp+"_rownum").val());
	}

	$('#basicInfoTab').tabs('select', tabIndex);
});

function Btn_OnClick(strDbid){
	if($("#dbid").val() != strDbid){
		$("#dbid").val(strDbid);
		
		$("#submit_form").attr("action","/DBInfo");
		$("#submit_form").submit();
	}
}

function Btn_SaveBasic(grpId){
	var obj = $("."+grpId+"_pref_sub_id");
	var chkErrorCnt = 0;
	//var idArry = "";
	let valueArr = new Array();
	let dbid = $("#dbid").val();
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

		} else if(dataTypeCd == "2") {
			if(grpId != '21' && subId != '002' && inputValue.length <= 0 ) {
				chkErrorCnt++;
			}
			
			if(grpId == '21' && subId == '002') {
				chkErrorCnt = chkErrorCnt + check_21002(inputValue);
			}
		}

		// 이전 값과 다른 경우만 저장함..
		if(prePrefValue != inputValue){
			let pref_id = grpId+subId;
			value = {"pref_mgmt_type_cd" : pref_mgmt_type_cd,
					"dbid" : dbid,
					"pref_id": pref_id,
					"pref_value": inputValue}
			valueArr.push(value);
		}
		/*
		if(prePrefValue != inputValue){
			idArry += grpId+subId + "|";
			valueArry += $.trim(inputValue) + "|";			
		}
		*/
	}

	if(chkErrorCnt > 0){
		$.messager.alert('','기준정보 값이 공백이거나 입력범위를 벗어났습니다.');
		return false;
	}
	
	$("#submit_form #pref_grp").val(grpId);
	
	$("#detail_form #dbid").val($("#submit_form #dbid").val());
	//$("#detail_form #prefIdArry").val(strRight(idArry,1));
	//$("#detail_form #prefValueArry").val(strRight(valueArry,1));
	
	/*
	ajaxCall("/InsertBasicInformation",
			$("#detail_form"),
			"POST",
			callback_InsertBasicInformation);
	*/
	console.log('submit: ',valueArr[0]);
	ajaxCallWithJson("/InsertBasicInformationJson",
			valueArr,
			"POST",
			callback_InsertBasicInformation);
}

function check_21002(inputValue) {
	var chkErrorCnt = 0;
	
	if(inputValue.indexOf(' ') >= 0) {
		chkErrorCnt++;
	}
	
	var hyphenIndex = inputValue.indexOf('-');
	
	if(hyphenIndex < 1 || hyphenIndex == (inputValue.length - 1) ) {
		chkErrorCnt++;
	} else {
		var splitData = inputValue.split('-');
		
		if(splitData.length != 2) {
			chkErrorCnt++;
		} else {
			if(isNaN(splitData[0]) || isNaN(splitData[1])) {
				chkErrorCnt++;
			}
			
			if(splitData[0] < 0 || splitData[0] > 23) {
				chkErrorCnt++;
			}
			
			if(splitData[1] < 1 || splitData[1] > 24) {
				chkErrorCnt++;
			}
			
			if(Number(splitData[0]) >= Number(splitData[1])) {
				chkErrorCnt++;
			}
		}
	}
	
	return chkErrorCnt;
}

//callback 함수
var callback_InsertBasicInformation = function(result) {
	if(result.result){
		parent.$.messager.alert('','적용 되었습니다.','info',function(){
			setTimeout(function() {
				var tempDbid = $("#dbid").val();
				$("#dbid").val("");
				Btn_OnClick(tempDbid);				
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			setTimeout(function() {
				var tempDbid = $("#dbid").val();
				$("#dbid").val("");
				Btn_OnClick(tempDbid);
			},1000);	
		});		
	}
};