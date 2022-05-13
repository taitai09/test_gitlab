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

	$("#problemTableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'inst_id',title:'INST_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'problem_id',title:'PROBLEM_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'problem_key',title:'PROBLEM_KEY',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'first_incident',title:'FIRST_INCIDENT',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'firstinc_time',title:'FIRSTINC_TIME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'last_incident',title:'LAST_INCIDENT',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'lastinc_time',title:'LASTINC_TIME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'impact1',title:'IMPACT1',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'impact2',title:'IMPACT2',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'impact3',title:'IMPACT3',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'impact4',title:'IMPACT4',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'service_request',title:'SERVICE_REQUEST',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'bug_number',title:'BUG_NUMBER',width:"10%",halign:"center",align:"left",sortable:"true"}
			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#incidentTableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'inst_id',title:'INST_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'incident_id',title:'INCIDENT_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'problem_id',title:'PROBLEM_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'create_time',title:'CREATE_TIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'close_time',title:'CLOSE_TIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'status',title:'STATUS',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'flood_controlled',title:'FLOOD_CONTROLLED',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'error_facility',title:'ERROR_FACILITY',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_number',title:'ERROR_NUMBER',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'error_arg1',title:'ERROR_ARG1',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg2',title:'ERROR_ARG2',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg3',title:'ERROR_ARG3',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg4',title:'ERROR_ARG4',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg5',title:'ERROR_ARG5',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg6',title:'ERROR_ARG6',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg7',title:'ERROR_ARG7',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg8',title:'ERROR_ARG8',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg9',title:'ERROR_ARG9',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg10',title:'ERROR_ARG10',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg11',title:'ERROR_ARG11',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'error_arg12',title:'ERROR_ARG12',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'signalling_component',title:'SIGNALLING_COMPONENT',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'signalling_subcomponent',title:'SIGNALLING_SUBCOMPONENT',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'subspect_component',title:'SUBSPECT_COMPONENT',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'subspect_subcomponent',title:'SUBSPECT_SUBCOMPONENT',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'ecid',title:'ECID',width:"7%",halign:"center",align:"left",sortable:"true"},
			{field:'impact',title:'IMPACT',width:"7%",halign:"center",align:"right",sortable:"true"}
			
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - ACTIVE INCIDENT"," "); 

	ajaxCall("/PreventiveCheck/ActiveIncident/Problem",
			$("#submit_form"),
			"POST",
			callback_ActiveIncidentProblemAction);
	
	ajaxCall("/PreventiveCheck/ActiveIncident/Incident",
			$("#submit_form"),
			"POST",
			callback_ActiveIncidentIncidentAction);	
}

//callback 함수
var callback_ActiveIncidentProblemAction = function(result) {
	var data = JSON.parse(result);
	$('#problemTableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ActiveIncidentIncidentAction = function(result) {
	var data = JSON.parse(result);
	$('#incidentTableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};