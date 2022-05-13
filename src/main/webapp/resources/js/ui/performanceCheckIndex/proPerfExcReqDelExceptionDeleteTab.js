$(document).ready(function() {
	$("body").css("visibility", "visible");
	
//	$("#exec_handling_div").show(); // 예외처리
	$("#exec_remove_div").hide();  // 예외삭제 //예외삭제탭 현재 보류 상태
//	checkValidExec("exec_handling_form"); //나중에 validation 체크안될때 다른방법으로 실행해보기. 일단은 jstl 이용하여 하였음.
	
	
    //예외요청사유
	$('#exec_handling_form #exception_request_why_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1061",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(val){
			$('#detail_form #exception_request_why_cd').val(val);
			$('#detail_form #exception_request_why_cd_nm').val(val);
		}
	});

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
//	var checkboxLength = $("#exec_handling_form input[name=chk_info]").length;
//	alert(checkboxLength);
//		if(checkboxLength > 1){
//			alert(checkboxLength);
//			$('#exec_handling_form #change_indc_pass_max_value_'+1).textbox({
//				onChange:function(val){
//					if(val < 0){
//						$('#exec_handling_form #change_indc_pass_max_value_'+1).textbox("setValue","");
//						$('#exec_handling_form #change_indc_pass_max_value_'+1).textbox("setValue","");
//						parent.$.messager.alert('','0 이하는 입력할 수 없습니다.');
//					}
//					
//					$('#exec_handling_form #change_not_pass_pass_'+1).textbox("setValue",val + " 초과");
//					
//					if($('#exec_handling_form #change_indc_pass_max_value_'+1).textbox("getValue") == ''){
//						$('#exec_handling_form #change_indc_pass_max_value_'+1).textbox("setValue",''); 
//					};
//					
//				}
//			});
//		}
	
	
	var checkbox = $("input[name=chk_info]");
	checkbox.on("change",function(){
		if($(this).prop("checked")){
			$(this).parent().parent().addClass("chkRow");
		}else{
			$(this).parent().parent().removeClass();
		}
	});
	
	
	$("#exception_del_prc_why").textbox("setValue",$("#exception_del_prc_why").textbox("getValue").trim());
	
});//끝

function changeShowDiv(val){
	if(val == '1'){  //예외처리
		$("#exec_handling_div").show(); // 예외처리
		$("#exec_remove_div").hide();  // 예외삭제		
		$("#exec_handling").prop("checked",true);	
		$("#exec_remove").prop("checked",false);
//		checkValidExec("exec_handling_form");
	}else if(val == '2'){    //예외삭제
		$("#exec_handling_div").hide(); // 예외처리
		$("#exec_remove_div").show();  // 예외삭제		
		$("#exec_handling").prop("checked",false); 	
		$("#exec_remove").prop("checked",true);
//		checkValidExec("exec_remove_form");  //
	}
};


function checkBtn() {
    var str = "";  
    var idx = $("input[name='chk_info']").length;

    $("input:checkbox:checked").each(function(index){
    	for(var i = 0; i < idx; i++){
//	    	alert($(this).prop("checked"));
	//        str += $(this).val() + ",";  
	//        str += $(this).prop("checked") + ",";  
	    	if($(this).prop("checked")){
//	    		alert(idx);
	    		return idx;
	    	}
    	}
    });  
}

/* 기존 탭 새로고침 */
//function Btn_reProPerfExecReqTab(){
//	var menuId = "214";
//	var menuNm = "성능 점검 예외 요청";
//	var menuUrl = "/PerformanceCheckIndex/ProPerfExcReq";
////	var menuParam = "call_from_child=Y";
//	var menuParam = $("#exec_handling_form").serialize();
//	
//	
//	/* 탭 생성 */
//	parent.parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
//}

//예외처리 버튼
function Btn_ExceptionDelete(){
//	if(!checkValidation("exec_handling_form")){
//		return false;
//	}

	if($("#exception_prc_status_cd").val() != '1'){
		parent.$.messager.alert('','예외 처리상태가 요청중인 건에 대해서만 예외처리가 가능합니다');
		return false;
	}
	var checkbox = $("input[name=chk_info]:checked");
	if($("#exception_del_prc_why").val() == ''){
		parent.$.messager.alert('','예외 처리 사유를 입력해 주세요.');
		return false;
	}
	if($("#exception_prc_meth_cd").val() == '1'){
		if(checkbox.length == 0){
			parent.$.messager.alert('','예외를 적용할 체크된 점검지표가 없습니다.');
			return false;
		}
	}
	
	setDataToTheForm("exec_handling_form");

	if($("#exec_handling_form #exception_prc_status_cd").val() == '1' && $("#exec_handling_form #perf_check_auto_pass_yn").val() == 'Y' &&$("#exec_handling_form #exception_prc_meth_cd").val() == '1'){
		parent.$.messager.confirm('', '예외 처리 방법이 점검제외에서 지표단위점검으로 변경됩니다. 해당 프로그램에 대해<br/>지표단위점검으로 저장하시겠습니까?', function(check) {
			if (check) {
				ajaxCall("/PerformanceCheckIndex/DeployPerfChkExcptRequest/exceptionDelete",$("#exec_handling_form"),"POST",callback_exceptionHandlingAction);	
			}else{
				return false;
			}
		});
	}else if($("#exec_handling_form #exception_prc_status_cd").val() == '1' && $("#exec_handling_form #except_reg_yn").val() == 'Y'){
		ajaxCall("/PerformanceCheckIndex/DeployPerfChkExcptRequest/exceptionDelete",$("#exec_handling_form"),"POST",callback_exceptionHandlingAction);	
	}else{
		parent.$.messager.alert('','점검지표의 예외처리가 존재하고, 예외 처리상태가 요청중인 건에 대해서만 예외 삭제가 가능합니다.');
		return false;
	}
	
};
//callback 함수
var callback_exceptionHandlingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				parent.Btn_OnClick();
				parent.createExceptionDeleteTab_reload();
				parent.Btn_ResetFieldAll();
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
//	var checkbox = $("input[name=chk_info]:checked");
	if($("#exception_prc_why").val() == ''){
		parent.$.messager.alert('','예외 처리 사유를 입력해 주세요.');
		return false;
	}

	ajaxCall("/PerformanceCheckIndex/DeployPerfChkExcptRequest/rejectRequest",
			$("#exec_handling_form"),
			"POST",
			callback_rejectRequestAction);	
};
//callback 함수
var callback_rejectRequestAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				parent.Btn_OnClick();
				parent.createExceptionDeleteTab_reload();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};




function Btn_onClick(){
	parent.document.getElementById('exec_handling').src = "/PerformanceCheckIndex/ExceptionDelete/CreateTab?"+$("#exec_handling_form").serialize();
};

//서버에 보낼 데이터를 세팅한다.
function setDataToTheForm(formId){
	var checkbox = $("#"+formId+" input[name=chk_info]:checked");
//	alert("checkBoxId/formId:::"+ checkbox+"/"+formId);
	
	var dbio = new Array();
	var perf_check_indc_id = new Array();
	var excpt_pass_max_value = new Array();
	var excpt_yn_decide_div_cd = new Array();
	
	
	// 체크된 체크박스 값을 가져온다
	checkbox.each(function(i) {

		// checkbox.parent() : checkbox의 부모는 <td>이다.
		// checkbox.parent().parent() : <td>의 부모이므로 <tr>이다.
		var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();
		
		// td.eq(0)은 체크박스 이므로  td.eq(1)의 값부터 가져온다.
		//가져온 값을 배열에 담는다.
		dbio.push(td.eq(1).text());
		perf_check_indc_id.push(td.eq(2).text());
		excpt_pass_max_value.push(td.eq(8).text());
		excpt_yn_decide_div_cd.push(td.eq(11).text());
		
	});
	
	$("#"+formId+" #dbio_chk").val(dbio.join(","));
	$("#"+formId+" #perf_check_indc_id_chk").val(perf_check_indc_id.join(","));
	$("#"+formId+" #excpt_pass_max_value_chk").val(excpt_pass_max_value.join(","));
	$("#"+formId+" #excpt_yn_decide_div_cd_chk").val(excpt_yn_decide_div_cd.join(","));
	
	$("#"+formId+" #except_reg_yn").val("Y");

	console.log("program_id",$("#"+formId+" #program_id").val());
	console.log("dbio_chk",$("#"+formId+" #dbio_chk").val());
	console.log("perf_check_indc_id_chk",$("#"+formId+" #perf_check_indc_id_chk").val());
	console.log("excpt_pass_max_value_chk",$("#"+formId+" #excpt_pass_max_value_chk").val());
	console.log("excpt_yn_decide_div_cd_chk",$("#"+formId+" #excpt_yn_decide_div_cd_chk").val());
	console.log("exception_request_id",$("#"+formId+" #exception_request_id").val());

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

function Btn_SettingText(val){
	$('#exec_handling_form #exception_del_prc_why').textbox("setValue",val); 
}