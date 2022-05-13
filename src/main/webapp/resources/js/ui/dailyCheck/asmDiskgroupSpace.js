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
			{field:'name',title:'NAME',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'group_number',title:'GROUP_NUMBER',width:"12%",halign:"center",align:"right",sortable:"true"},
			{field:'state',title:'STATE',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'total_mb',title:'TOTOAL_SPACE(GB)',width:"12%",halign:"center",align:"right",sortable:"true"},
			{field:'free_mb',title:'FREE_SPACE(GB)',width:"12%",halign:"center",align:"right",sortable:"true"},
			{field:'space_used_percent',title:'SPACE_USED(%)',width:"16%",halign:"center",align:"right",sortable:"true"},
			{field:'threshold_percent',title:'THRESHOLD(%)',width:"14%",halign:"center",align:"right",sortable:"true"}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - ASM DISKGROUP SPACE"," "); 

	ajaxCall("/PreventiveCheck/AsmDiskgroupSpace",
			$("#submit_form"),
			"POST",
			callback_AsmDiskgroupSpaceAction);
}

//callback 함수
var callback_AsmDiskgroupSpaceAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};