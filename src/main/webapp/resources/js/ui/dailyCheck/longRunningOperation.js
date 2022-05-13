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
			{field:'inst_id',title:'INST_ID',halign:"center",align:"right",sortable:"true"},
			{field:'sid',title:'SID',halign:"center",align:"right",sortable:"true"},
			{field:'serial',title:'SERIAL#',halign:"center",align:"right",sortable:"true"},
			{field:'start_time',title:'START_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'last_update_time',title:'LAST_UPDATE_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'elapsed_minute',title:'ELAPSED_MINUTE',halign:"center",align:"right",sortable:"true"},
			{field:'remaining_minute',title:'REMAINING_MINUTE',halign:"center",align:"right",sortable:"true"},
			{field:'done_percent',title:'DONE(%)',halign:"center",align:"right",sortable:"true"},
			{field:'message',title:'MESSAGE',halign:"center",align:"left",sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:"left",sortable:"true"},
			{field:'sql_plan_hash_value',title:'SQL_PLAN_HASH_VALUE',halign:"center",align:"right",sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:"left",sortable:"true"}
			
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - LONG RUNNING OPERATION"," "); 

	ajaxCall("/PreventiveCheck/LongRunningOperation",
			$("#submit_form"),
			"POST",
			callback_LongRunningOperationAction);
}

//callback 함수
var callback_LongRunningOperationAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};