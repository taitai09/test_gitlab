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
			{field:'sequence_id',title:'SEQUENCE_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'reason_id',title:'REASON_ID',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'owner',title:'OWNER',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'object_name',title:'OBJECT_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'subobject_name',title:'SUBOBJECT_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'object_type',title:'OBJECT_TYPE',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'reason',title:'REASON',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'time_suggested',title:'TIME_SUGGESTED',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'creation_time',title:'CREATION_TIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'suggested_action',title:'SUGGESTED_ACTION',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'advisor_name',title:'ADVISOR_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'metric_value',title:'METRIC_VALUE',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'message_type',title:'MESSAGE_TYPE',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'message_group',title:'MESSAGE_GROUP',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'message_level',title:'MESSAGE_LEVEL',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'hosting_client_id',title:'HOSTING_CLIENT_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'module_id',title:'MODULE_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'process_id',title:'PROCESS_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'host_id',title:'HOST_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'host_nw_addr',title:'HOST_NW_ADDR',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'instance_name',title:'INSTANCE_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'instance_number',title:'INSTANCE_NUMBER',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'user_id',title:'USER_ID',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'execution_context_id',title:'EXECUTION_CONTEXT_ID',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'error_instance_id',title:'ERROR_INSTANCE_ID',width:"13%",halign:"center",align:"left",sortable:"true"}
			
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - OUTSTANDING ALERT"," "); 

	ajaxCall("/PreventiveCheck/OutstandingAlert",
			$("#submit_form"),
			"POST",
			callback_OutstandingAlertAction);
}

//callback 함수
var callback_OutstandingAlertAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};