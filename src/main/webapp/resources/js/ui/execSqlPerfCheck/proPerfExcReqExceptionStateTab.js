$(document).ready(function() {
	
//	$("#exec_handling_div").show(); // 예외처리
	$("#exec_remove_div").hide();  // 예외삭제 //예외삭제탭 현재 보류 상태
//	checkValidExec("exec_handling_form"); //나중에 validation 체크안될때 다른방법으로 실행해보기. 일단은 jstl 이용하여 하였음.
	
	
    //예외요청사유
	$('#exec_handling_form #exception_request_why_cd').combobox({
		url:"/Common/getCommonCodeRef2?grp_cd_id=1061",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(val){
			$('#detail_form #exception_request_why_cd').val(val);
			$('#detail_form #exception_request_why_cd_nm').val(val);
		}
	});
	$('#exec_handling_form #exception_prc_meth_cd_chk').checkbox({disabled:true});

	
	
	
	//체크박스 전체선택 or 전체해제
	$("#checkedAll").on("click",function(){
		var chk = $(this).is(":checked");
	if(chk){
			$("input[name=chk_info]").each(function(){//체크박스 갯수만큼 for문을 돈다.
				var opt = $(this).prop("disabled");  //현재상태를 체크한다.
				if(!opt){//prop을 통해 true,false 값을 return받고,disabled이 아닌 박스틀만 체크한다.
					$(this).prop("checked",true);
					$(this).parent().parent().addClass("chkRow");
				}
			});
		}else if(!chk){
			$("input[name=chk_info]").each(function(){//체크박스 갯수만큼 for문을 돈다.
				var opt = $(this).prop("disabled");  //현재상태를 체크한다.
				if(!opt){//prop을 통해 true,false 값을 return받고,disabled이 아닌 박스틀만 체크한다.
					$(this).prop("checked",false);
					$(this).parent().parent().removeClass();
				}
			});
		}

	});
	

	addInputDataToNotPassPass();
	
	//체크박스라인 클래스 추가
	var checkbox = $("input[name=chk_info]");
	checkbox.on("change",function(){
		if($(this).prop("checked")){
			$(this).parent().parent().addClass("chkRow");
		}else{
			$(this).parent().parent().removeClass();
		}
	});
	
	$("#exception_prc_why").textbox("setValue",$("#exception_prc_why").textbox("getValue").trim());
	
});//끝



/*function Btn_reProPerfExecReqTab_old(){
	var menuId = "214";
	var menuNm = "성능 검증 예외 요청";
	var menuUrl = "/ExecPerformanceCheckIndex/ProPerfExcReq";
	$("#call_from_child").val("Y");
//	var menuParam = "call_from_child=Y";
	var menuParam = $("#exec_handling_form").serialize();
	
	 탭 생성 
	parent.parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}*/

/* 기존 탭 새로고침 */
function Btn_reProPerfExecReqTab(val){
	if(parent.fnChangeValue(val)){
		parent.Btn_OnClick();
		parent.createExceptionHandlingTab_reload();
	}
}


//예외처리 버튼
function Btn_ExceptionHandling(){
	if(!checkValidation("exec_handling_form")){
		return false;
	}

	if($("#exception_prc_status_cd").val() != '1'){
		parent.$.messager.alert('','예외 처리상태가 요청중인 건에 대해서만 예외처리가 가능합니다');
		return false;
	}
	var checkbox = $("input[name=chk_info]:checked");
	if($("#exception_prc_why").val() == ''){
		parent.$.messager.alert('','예외 처리 사유를 입력해 주세요.');
		return false;
	}
	if($("#exception_prc_meth_cd").val() == '1'){
		if(checkbox.length == 0){
			parent.$.messager.alert('','예외를 적용할 체크된 검증지표가 없습니다.');
			return false;
		}
	}
	
	setDataToTheForm("exec_handling_form");

	if($("#exec_handling_form #exception_prc_status_cd").val() == '1' && $("#exec_handling_form #exception_prc_meth_cd").val() == '1'){
		ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/exceptionHandling",$("#exec_handling_form"),"POST",callback_exceptionHandlingAction);	

	}else if($("#exec_handling_form #exception_prc_status_cd").val() == '1' && $("#exec_handling_form #exception_prc_meth_cd").val() == '2'){
		parent.$.messager.confirm('', '성능검증 검증제외를 선택하셨을 경우 성능검증을 수행하지 않습니다. 해당 프로그램에 대해<br/>검증제외로 저장하시겠습니까?', function(check) {
			if (check) {
				ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/exceptionHandling",$("#exec_handling_form"),"POST",callback_exceptionHandlingAction);	
			}else{
				return false;
			}
		});
	}else{
		parent.$.messager.alert('','정의되지 않은 성능검증방법코드입니다.');
		return false;
	}
	
};
//callback 함수
var callback_exceptionHandlingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
//				Btn_onClick();
				Btn_reProPerfExecReqTab(2);  //처리완료
//				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


//반려 버튼
function Btn_Reject(){
	
	if($("#exception_prc_status_cd").val() != '1'){
		parent.$.messager.alert('','예외 처리상태가 요청중인 건에 대해서만 반려가 가능합니다');
		return false;
	}
	var checkbox = $("input[name=chk_info]:checked");
	if($("#exception_prc_why").val() == ''){
		parent.$.messager.alert('','예외 처리 사유를 입력해 주세요.');
		return false;
	}

	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/rejectRequest",
			$("#exec_handling_form"),
			"POST",
			callback_rejectRequestAction);	
};
//callback 함수
var callback_rejectRequestAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_reProPerfExecReqTab(4); //처리반려
//				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


//예외삭제
//function Btn_RemoveException(){
//	
//	if(!checkValidation("exec_remove_form")){
//		return false;
//	}
//	var checkbox = $("input[name=chk_info]:checked");
//	if($("#exception_prc_why").val() == ''){
//		parent.$.messager.alert('','예외 처리 사유를 입력해 주세요.');
//		return false;
//	}
//	if(checkbox.length == 0){
//		parent.$.messager.alert('','예외를 삭제할 체크된 검증지표가 없습니다.');
//		return false;
//	}
//	setDataToTheForm("exec_remove_form");
//	
//	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/removeException",
//			$("#exec_remove_form"),
//			"POST",
//			callback_removeExceptionAction);	
//};
//callback 함수
//var callback_removeExceptionAction = function(result) {
//	if(result.result){
//		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
//			setTimeout(function() {
//				Btn_onClick();
////				Btn_ResetField();
//			},1000);	
//		});
//	}else{
//		parent.$.messager.alert('',result.message);
//	}
//};


function Btn_onClick(){
	parent.document.getElementById('exec_handling').src = "/ExecPerformanceCheckIndex/ExceptionHandling/CreateTab?"+$("#exec_handling_form").serialize();
};

function checkValidation(formId){
	var checkbox = $("#"+formId+" input[name=chk_info]:checked");
	
	var change_indc_pass_max_value = new Array();
	var change_except_reg_yn = new Array();
	var check = 0;

	// 체크된 체크박스 값을 가져온다
//	checkbox.each(function(i) {
	for(var i = 0; i < checkbox.length; i++){
		// checkbox.parent() : checkbox의 부모는 <td>이다.
		// checkbox.parent().parent() : <td>의 부모이므로 <tr>이다.
		var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();
		
		 if(td.eq(14).children().val() == '' && td.eq(16).children().val() == ''){
			parent.$.messager.alert('','예외 적용값을 정확히 입력해 주세요.');
			 check++;
		 }
		 if(check > 0){
			 break;
		 }
		 
		 if(td.eq(14).children().val() != '' && td.eq(14).children().val().length > 20){
			 parent.$.messager.alert('','예외 적용값의 적합의 길이가 너무 깁니다.<br/>20자 이내로 작성해 주세요.');
			 check++;
		 }
		 if(check > 0){
			 break;
		 }
		 
	}
	if(check > 0){
		return false;
	}else{
		return true;
	}
//	});
}
//서버에 보낼 데이터를 세팅한다.
function setDataToTheForm(formId){
	var checkbox = $("#"+formId+" input[name=chk_info]:checked");
//	alert("checkBoxId/formId:::"+ checkbox+"/"+formId);
	
	var perf_check_indc_id = new Array();
	var indc_yn_decide_div_cd = new Array();
	var perf_check_result_div_cd = new Array();
	var perf_check_meth_cd = new Array();
	var except_reg_yn = new Array();
	var indc_pass_max_value = new Array();
	var change_indc_pass_max_value = new Array();
	var change_not_pass_pass = new Array();
	var change_yn_decide_div_cd = new Array();
	

	// 체크된 체크박스 값을 가져온다
	checkbox.each(function(i) {

		// checkbox.parent() : checkbox의 부모는 <td>이다.
		// checkbox.parent().parent() : <td>의 부모이므로 <tr>이다.
		var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();
		
		// td.eq(0)은 체크박스 이므로  td.eq(1)의 값부터 가져온다.
		//가져온 값을 배열에 담는다.
		 perf_check_indc_id.push(td.eq(2).text());
		 indc_yn_decide_div_cd.push(td.eq(3).text());
		 perf_check_result_div_cd.push(td.eq(4).text());
		 perf_check_meth_cd.push(td.eq(5).text());
		 except_reg_yn.push(td.eq(6).text());
		 indc_pass_max_value.push(td.eq(8).text());
		 change_indc_pass_max_value.push(td.eq(14).children().val());
		 change_not_pass_pass.push(td.eq(15).children().val());
		 change_yn_decide_div_cd.push(td.eq(16).children().val());
		
	});
	
	$("#"+formId+" #perf_check_indc_id_chk").val(perf_check_indc_id.join(","));
	$("#"+formId+" #indc_yn_decide_div_cd_chk").val(indc_yn_decide_div_cd.join(","));
	$("#"+formId+" #perf_check_result_div_cd_chk").val(perf_check_result_div_cd.join(","));
	$("#"+formId+" #perf_check_meth_cd_chk").val(perf_check_meth_cd.join(","));
	$("#"+formId+" #except_reg_yn_chk").val(except_reg_yn.join(","));
	$("#"+formId+" #indc_pass_max_value_chk").val(indc_pass_max_value.join(","));
	$("#"+formId+" #change_indc_pass_max_value_chk").val(change_indc_pass_max_value.join(","));
	$("#"+formId+" #change_not_pass_pass_chk").val(change_not_pass_pass.join(","));
	$("#"+formId+" #change_yn_decide_div_cd_chk").val(change_yn_decide_div_cd.join(","));
	
	console.log("perf_check_indc_id_chk",$("#"+formId+" #perf_check_indc_id_chk").val());
	console.log("indc_yn_decide_div_cd_chk",$("#"+formId+" #indc_yn_decide_div_cd_chk").val());
	console.log("perf_check_result_div_cd_chk",$("#"+formId+" #perf_check_result_div_cd_chk").val());
	console.log("perf_check_meth_cd_chk",$("#"+formId+" #perf_check_meth_cd_chk").val());
	console.log("indc_pass_max_value_chk",$("#"+formId+" #indc_pass_max_value_chk").val());
	console.log("except_reg_yn_chk",$("#"+formId+" #except_reg_yn_chk").val());
	console.log("change_indc_pass_max_value_chk",$("#"+formId+" #change_indc_pass_max_value_chk").val());
	console.log("change_not_pass_pass_chk",$("#"+formId+" #change_not_pass_pass_chk").val());
	console.log("change_yn_decide_div_cd_chk",$("#"+formId+" #change_yn_decide_div_cd").val());

//	$("#ex3_Result1").html(" * 체크된 Row의 모든 데이터 = "+rowData);	
//	$("#ex3_Result2").html(tdArr);	
}




//체크박스의 갯수만큼 예외적용값 - 적합에 액션을 만든다.
var addInputDataToNotPassPass = function(){

	var checkboxL = $("#exec_handling_form input[name=chk_info]");
		checkboxL.each(function(i){
			
			$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox({
				onChange:function(val){
					if(val < 0){
						$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("setValue","");
						$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("setValue","");
						parent.$.messager.alert('','0 이하는 입력할 수 없습니다.');
					}
					
					$('#exec_handling_form #change_not_pass_pass_'+i).textbox("setValue",val + " 초과");
					
					if($('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("getValue") == ''){
						$('#exec_handling_form #change_not_pass_pass_'+i).textbox("setValue",''); 
					};
				}
			});
		});
	
};
var validCheckInputData = function(){   //나중에 해야함.
	var checkboxL = $("#exec_handling_form input[name=chk_info]");
	checkboxL.each(function(i){
		
		$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox({
			onChange:function(val){
				if(val < 0){
					$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("setValue","");
					$('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("setValue","");
					parent.$.messager.alert('','0 이하는 입력할 수 없습니다.');
				}
				
				$('#exec_handling_form #change_not_pass_pass_'+i).textbox("setValue",val + " 초과");
				
				if($('#exec_handling_form #change_indc_pass_max_value_'+i).textbox("getValue") == ''){
					$('#exec_handling_form #change_not_pass_pass_'+i).textbox("setValue",''); 
				};
			}
		});
	});
}
