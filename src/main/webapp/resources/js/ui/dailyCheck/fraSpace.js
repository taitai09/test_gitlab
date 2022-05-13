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

	$("#filestableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'name',title:'NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'number_of_files',title:'NUMBER_OF_FILES',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'total_space',title:'TOTAL_SPACE(GB)',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'space_used',title:'SPACE_USED(GB)',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'space_reclaimable',title:'SPACE_RECLAIMABLE',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'claim_before_usage_percent',title:'CLAIM_BEFORE_USAGE(%)',width:"14%",halign:"center",align:"right",sortable:"true"},
			{field:'claim_after_usage_percent',title:'CLAIM_AFTER_USAGE(%)',width:"14%",halign:"center",align:"right",sortable:"true"},
			{field:'threshold_percent',title:'THRESHOLD(%)',width:"12%",halign:"center",align:"right",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#usagetableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'file_type',title:'FILE_TYPE',width:"25%",halign:"center",align:"left",sortable:"true"},
			{field:'percent_space_used',title:'SPACE_USED(%)',width:"20%",halign:"center",align:"right",sortable:"true"},
			{field:'percent_space_reclaimable',title:'SPACE_RECLAIMABLE(%)',width:"20%",halign:"center",align:"right",sortable:"true"},
			{field:'number_of_files',title:'NUMBER_OF_FILES',width:"20%",halign:"center",align:"right",sortable:"true"}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - FRA SPACE"," "); 

	ajaxCall("/PreventiveCheck/FraSpace",
			$("#submit_form"),
			"POST",
			callback_FraSpaceAction);
	
	ajaxCall("/PreventiveCheck/FraUsage",
			$("#submit_form"),
			"POST",
			callback_FraUsageAction);	
}

//callback 함수
var callback_FraSpaceAction = function(result) {
	var data = JSON.parse(result);
	$('#filestableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_FraUsageAction = function(result) {
	var data = JSON.parse(result);
	$('#usagetableList').datagrid("loadData", data);
};