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
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'owner',title:'OWNER',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'subpartition_name',title:'SUBPARTITION_NAME',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'object_type',title:'OBJECT_TYPE',width:"15%",halign:"center",align:"left",sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',width:"15%",halign:"center",align:"center",sortable:"true"},
			{field:'stattype_locked',title:'STATTYPE_LOCKED',halign:"center",align:"left",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#registExceptionBtn").click(function(){
		fnRegistException();
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - STATISTICS LOCKED TABLE"," "); 

	ajaxCall("/PreventiveCheck/StatisticsLockedTable",
			$("#submit_form"),
			"POST",
			callback_StatisticsLockedTableAction);
}

//callback 함수
var callback_StatisticsLockedTableAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

/**
 * 장애예방>일예방점검>예외등록
 * @returns
 */
function fnRegistException(){
	var checkedRows = $('#tableList').datagrid('getChecked');
	
	var dbid = $("#selectDbid").combobox('getValue');
	
	var exceptObjectNameArry = "";
	
	var rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		$.messager.confirm('', "선택한 점검대상을 예외등록하시겠습니까?", function(r){
			if(r){
				for(var i = 0; i < rows.length; i++){
					exceptObjectNameArry += nullStringToBlank(rows[i].owner) + "^"
							+ nullStringToBlank(rows[i].table_name) + "^"
							+ nullStringToBlank(rows[i].partition_name) + "^"
							+ nullStringToBlank(rows[i].subpartition_name) + "|";
				}
				$("#submit_form #check_except_object").val(strRight(exceptObjectNameArry,1));
		
				ajaxCall("/ExceptionManagement/RegistException", $("#submit_form"), "POST", callback_RegistExceptionAction);
			}
		
		});
	}else{
		parent.$.messager.alert('','예외등록할 데이터를 선택해 주세요.');
	}
}
