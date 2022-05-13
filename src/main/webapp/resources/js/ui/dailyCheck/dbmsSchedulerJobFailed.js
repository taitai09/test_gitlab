$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	fnCreateDbidCombobox();	

	$("#strStartDt").datebox({
		onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			
			// 점검회차 설정
			setCheckSeq(selDate);
		}
	});
	
	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());

	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'inst_id',title:'INST_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'log_id',title:'LOG_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'log_date',title:'LOG_DATE',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'owner',title:'OWNER',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'job_name',title:'JOB_NAME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'job_subname',title:'JOB_SUBNAME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'status',title:'STATUS',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'error',title:'ERROR#',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'req_start_date',title:'REQ_START_DATE',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'actual_start_date',title:'ACTUAL_START_DATE',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'run_duration',title:'RUN_DURATION',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'session_id',title:'SESSION_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'slave_pid',title:'SLAVE_PID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'cpu_used',title:'CPU_USED_TIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'credential_owner',title:'CREDENTIAL_OWNER',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'credential_name',title:'CREDENTIAL_NAME',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'destination_owner',title:'DESTINATION_OWNER',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'destination',title:'DESTINATION',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'additional_info',title:'ADDITIONAL_INFO',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'errors',title:'ERRORS',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'output',title:'OUTPUT',width:"10%",halign:"center",align:"left",sortable:"true"}
			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#selectDbid').combobox("setValue",$("#dbid").val());
	$('#selectCheckSeq').combobox("setValue",$("#check_seq").val());	
	Btn_OnClick();
});



function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - DBMS SCHEDULER JOB FAILED"," "); 

	ajaxCall("/PreventiveCheck/DbmsSchedulerJobFailed",
			$("#submit_form"),
			"POST",
			callback_DbmsSchedulerJobFailedAction);
}

//callback 함수
var callback_DbmsSchedulerJobFailedAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};