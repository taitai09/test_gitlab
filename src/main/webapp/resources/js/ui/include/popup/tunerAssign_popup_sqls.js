var tuning_prgrs_step_seq = "";
var project_id = "";
var params ="";

$(document).ready(function() {
	initPopupSet();
	
	$('#tuningAssignPopSqls').window({
		title : "튜닝 요청",
		top:getWindowTop(200),
		left:getWindowLeft(440)
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
	
	if($('#selectTuner').combobox('getValue') == ""){
		if($('#auto_share').val() == "N"){
			$.messager.alert('','튜닝담당자를 선택해 주세요.');
			return false;
		}
	}
	
	if($('#auto_share').val() == "N"){
		$("#assign_form #perfr_id").val($('#assign_form #selectTuner').combobox('getValue'));
	}
	
	let strUrl = "/Sqls/Popup/InsertTuningRequest";
	
	ajaxCall(strUrl,
			$("#assign_form"),
			"POST",
			callback_InsertTuningRequest);
	
	/* modal progress open */
	parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if(result.result){
		/* modal progress close */
		parent.closeMessageProgress();
		
		if(result.list != null && result.list.length > 0) {
			callMessage(result.list, result.txtValue);
		} else {
			$.messager.alert('','튜닝요청이 정상적으로 처리되었습니다.','info',function(){
				setTimeout(function() {
					//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
					parent.parent.searchWorkStatusCount();
					
					Btn_OnClosePopup('tuningAssignPopSqls');
					Btn_OnClick();
				},1000);
			});
		}
	} else {
		if(result.list != null && result.list.length > 0) {
			callMessage(result.list, result.txtValue);
		} else {
			parent.$.messager.alert('','튜닝 요청 중 오류가 발생하였습니다.');
		}
		
		/* modal progress close */
		parent.closeMessageProgress();
	}
};

function callMessage(list, totalCount) {
	let length = list.length;
	let message = '';
	
	if(totalCount == length) {
		message = length + ' 건은 이미 튜닝 요청 처리 중 입니다.\n이미 요청 처리 중인 SQL의 "SQL 식별자(DBIO)"는 다음과 같습니다.\n\n\n';
	} else {
		message = totalCount + ' 건이 튜닝 요청 되었으며, ' + length + ' 건은 이미 튜닝 요청 처리 중 입니다.\n이미 요청 처리 중인 SQL의 "SQL 식별자(DBIO)"는 다음과 같습니다.\n\n\n';
	}
	
	for(let listIndex = 0; listIndex < length; listIndex++) {
		message += list[listIndex];
		
		if(listIndex < list.length - 1) {
			message += '\n';
		}
	}
	
	$('#messagePop').window({title: '정보'});
	$('#messagePop').window({iconCls: 'icon-info'});
	
	$('#message_form #message').val(message);
	$("#messagePop").window({
		width: 700,
		height: 300,
		modal : true
	});
	$("#messagePop").window("open");
}

function initPopupSet(){
	$('#chkAutoShare').switchbutton({checked:false});
	$("#auto_share").val("N");	
	$('#selectTuner').combobox({readonly:false});
	$('#selectTuner').combobox('setValue','');
	$('#project_id').combobox('setValue','');
};

function Btn_OnClose() {
	Btn_OnClosePopup('messagePop');
	
	//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
	parent.parent.searchWorkStatusCount();
	
	Btn_OnClosePopup('tuningAssignPopSqls');
	Btn_OnClick();
}
