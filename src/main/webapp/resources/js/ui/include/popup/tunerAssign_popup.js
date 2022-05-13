var tuning_prgrs_step_seq = "";
var project_id = "";
var params ="";
$(document).ready(function() {
	initPopupSet();	
	
	var Top = (WindowHeight - 150) / 2;
	var Left = (WindowWidth - 270) / 2; 
	if (Top < 0) Top = 0;
	if (Left < 0) Left = 0;
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(150),
		left:getWindowLeft(440)
	});
	
	$('#tuningAssignAllPop').window({
		title : "튜닝담당자 지정",
		top:getWindowTop(150),
		left:getWindowLeft(270)
	});
	
	$('#chkAutoShare').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#auto_share").val("Y");
				$("#selectTuner").combobox({readonly:true});
			}else{
				$("#auto_share").val("N");
				$("#selectTuner").combobox({readonly:false});
			}
		}
	});
	
	//프로젝트 조회
	$('#assign_form #project_id').combobox({
		url:"/Common/getProject?isNotApplicable=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(rec){
			if(rec.project_id == ''){
				$("#tuning_prgrs_step_seq").combobox('setValue','');
			}
				
			project_id = rec.project_id;
			
			if(project_id != null && project_id != ''){
				//튜닝진행단계 조회
				$('#assign_form #tuning_prgrs_step_seq').combobox({
					url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?isNotApplicable=Y&project_id="+project_id,
					method:"get",
					valueField:'tuning_prgrs_step_seq',
					textField:'tuning_prgrs_step_nm',
					onSelect: function(rec){
						tuning_prgrs_step_seq = rec.tuning_prgrs_step_seq;
					},
					onLoadError: function(){
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
				});	
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});	
	
	
});

function Btn_SaveAssign(){

	if($('#project_id').combobox('getValue') != ""){
		if($('#tuning_prgrs_step_seq').combobox('getValue') == ""){
			$.messager.alert('','튜닝진행단계를 선택해 주세요.');
			return false;
		}
	}		
	
	
	params = "project_id="+project_id+"&tuning_prgrs_step_seq="+tuning_prgrs_step_seq;

	
	var strUrl = "";
	
	if($('#selectTuner').combobox('getValue') == ""){
		if($('#auto_share').val() == "N"){
			$.messager.alert('','튜닝담당자를 선택해 주세요.');
			return false;
		}
	}		

	if($("#auto_share").val() == "N"){
		$("#assign_form #perfr_id").val($('#selectTuner').combobox('getValue'));	
	}else{
		$("#assign_form #perfr_id").val("");
	}
	
	if($("#assign_form #strGubun").val() == "M"){
//		eval("if_"+frameName).$("#perfr_auto_assign_yn").val($("#assign_form #auto_share").val());
		$("#perfr_auto_assign_yn").val($("#assign_form #auto_share").val());
		if($("#assign_form #auto_share").val() == "N"){
//			eval("if_"+frameName).$("#submit_form #perfr_id").val($('#assign_form #selectTuner').combobox('getValue'));	
			$("#submit_form #perfr_id").val($('#assign_form #selectTuner').combobox('getValue'));	
		}else{
//			eval("if_"+frameName).$("#submit_form #perfr_id").val("");
			$("#submit_form #perfr_id").val("");
		}	
		
//		$("#assign_form #start_snap_id").val(eval("if_"+frameName).$("#submit_form #start_snap_id").val());
//		$("#assign_form #end_snap_id").val(eval("if_"+frameName).$("#submit_form #end_snap_id").val());
		$("#assign_form #start_snap_id").val($("#submit_form #start_snap_id").val());
		$("#assign_form #end_snap_id").val($("#submit_form #end_snap_id").val());
		
		strUrl = "/SQLTuningTarget/Popup/InsertTuningRequest";  //일반
	}else{
		strUrl = "/SQLDiagnostics/Popup/InsertTuningRequest";  //?
	}

	console.log("요청 URL:::"+ strUrl);
	ajaxCall(strUrl,
			$("#assign_form"),
			"POST",
			callback_InsertTuningRequest);
	
	/* modal progress open */
	//openMessageProgress('튜닝 대상 선정','튜닝 담당자를 지정중 입니다.');
	parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if(result.result){
			
			/* modal progress close */
//		closeMessageProgress();
		parent.closeMessageProgress();
		
		$.messager.alert('','튜닝요청이 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				//메시지 전송 삭제 2018-10-29
				// 해당 Tuner에게 Alert 전송
//				var strTitle = "SQL 튜닝대상 선정";
//				var strMsg = "";
//
//				for(var i = 0 ; i < result.object.length ; i++){
//					var post = result.object[i];
//					
//					if($("#assign_form #strGubun").val() == "M"){
//						strMsg = "SQL 튜닝대상 선정 [수동선정]<br/>"+ post.perfr_id_cnt +"건이 튜닝요청 되었습니다.";
//					}else{
//						if($("#assign_form #choice_div_cd").val() == "4"){						
//							strMsg = "SQL 진단 [FULL SCAN]<br/>"+ post.perfr_id_cnt +"건이 튜닝요청 되었습니다.";
//						}else if($("#assign_form #choice_div_cd").val() == "7"){
//							strMsg = "SQL 진단 [TEMP OVERUSE SQL]<br/>"+ post.perfr_id_cnt +"건이 튜닝요청 되었습니다.";
//						}else if($("#assign_form #choice_div_cd").val() == "5"){
//							strMsg = "SQL 진단 [PLAN CHANGE SQL]<br/>"+ post.perfr_id_cnt +"건이 튜닝요청 되었습니다.";
//						}else if($("#assign_form #choice_div_cd").val() == "6"){
//							strMsg = "SQL 진단 [NEW SQL]<br/>"+ post.perfr_id_cnt +"건이 튜닝요청 되었습니다.";
//						}
//					}
//					
//					messageSendByUser(post.perfr_id, strTitle, strMsg);
//				}
//				
				// 수동선정 일때.
				if($("#assign_form #strGubun").val() == "M"){
//					eval("if_"+frameName).$("#submit_form #choice_tms").val(result.txtValue);
					$("#submit_form #choice_tms").val(result.txtValue);
					
					
					// 튜닝대상저장
					ajaxCall("/ManualSelection/InsertTopsqlHandopChoiceExec?"+params,
//							eval("if_"+frameName).$("#submit_form"),
							$("#submit_form"),
							"POST",
							callback_InsertTopsqlHandopChoiceExec);
				}else{

					Btn_OnClosePopup('tuningAssignPop');
//					eval("if_"+frameName).Btn_OnClick();
					Btn_OnClick();
				}

				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);				
		});
	}
};

function initPopupSet(){
	$('#chkAutoShare').switchbutton({checked:false});
	$("#auto_share").val("N");	
	$('#selectTuner').combobox({readonly:false});
	$('#selectTuner').combobox('setValue','');
//	$('#tuning_prgrs_step_seq').combobox('setValue','');
	$('#project_id').combobox('setValue','');
	
//	Btn_OnClosePopup('tuningAssignPop');
};

//callback 함수
var callback_InsertTopsqlHandopChoiceExec = function(result) {
	params = ""; project_id = ""; tuning_prgrs_step_seq = "";  //초기화
	
	if(result.result){		
		setTimeout(function() {

			
			Btn_OnClosePopup('tuningAssignPop');
//			eval("if_"+frameName).Btn_OnClick();
			Btn_OnClick();
		},1000)
	}
}


function Btn_SaveAssignAll(){	
	if($('#assignAll_form #selectTuner').combobox('getValue') == ""){
		$.messager.alert('','튜닝담당자를 선택해 주세요.');
		return false;
	}		

	$("#assignAll_form #perfr_id").val($('#assignAll_form #selectTuner').combobox('getValue'));

	ajaxCall("/ImprovementManagement/SaveTunerAssignAll",
			$("#assignAll_form"),
			"POST",
			callback_SaveTunerAssignAllAction);
}

//callback 함수
var callback_SaveTunerAssignAllAction = function(result) {	
	if(result.result){
		$.messager.alert('','튜닝담당자 지정이 정상적으로 처리되었습니다.','info',function(){
			
			//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
			parent.parent.searchWorkStatusCount();
			
			Btn_OnAssignAllPopClose();
			
			setTimeout(function() {
				//메시지 전송 삭제 2018-10-29
				// 튜닝담당자(TUNER)와 개발자에게 ALERT 전송
//				var tunerId = $('#assignAll_form #selectTuner').combobox('getValue');
//				var tunerName = $('#assignAll_form #selectTuner').combobox('getText');
//				
////				var strMsg = "["+loginUserName+"]님이 ["+tunerName+"]님을 튜닝담당자로 지정하셨습니다.["+result.txtValue+"건]";
////				var strMsgDev = "["+loginUserName+"]님이 ["+tunerName+"]님을 튜닝담당자로 지정하셨습니다.";
//				var strMsg = "["+parent.loginUserName+"]님이 ["+tunerName+"]님을 튜닝담당자로 지정하셨습니다.["+result.txtValue+"건]";
//				var strMsgDev = "["+parent.loginUserName+"]님이 ["+tunerName+"]님을 튜닝담당자로 지정하셨습니다.";
//				
//				// 튜닝담당자(TUNER)에게 전송
//				messageSendByUser(tunerId, "튜닝담당자 지정", strMsg);
//				
//				for(var i = 0 ; i < result.object.length ; i++){
//					var post = result.object[i];
//					// 개발자에게 전송
//					messageSendByUser(post.tuning_requester_id, "튜닝담당자 지정", strMsgDev);	
//				}

				/* iframe에 함수 호출 */
//				eval("if_"+frameName).Btn_OnClick();
				try{
					Btn_OnClick();
				}catch(e){
					document.location.reload();
				}
			},1000);			
		});		
	}else{
		$.messager.alert('',result.message,'error');
	}
};

function Btn_OnAssignAllPopClose(){
	$('#tuningAssignAllPop').window("close");
}