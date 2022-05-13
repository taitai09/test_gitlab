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
			{field:'owner',title:'OWNER',width:"7%",halign:"center",align:"left",sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',width:"11%",halign:"center",align:"left",sortable:"true"},
			{field:'partitioned',title:'PARTITIONED',width:"8%",halign:"center",align:"left",sortable:"true"},
			{field:'inserts',title:'INSERTS',width:"6%",halign:"center",align:"right",sortable:"true"},
			{field:'updates',title:'UPDATES',width:"6%",halign:"center",align:"right",sortable:"true"},
			{field:'deletes',title:'DELETES',width:"6%",halign:"center",align:"right",sortable:"true"},
			{field:'truncated',title:'TRUNCATED',width:"6%",halign:"center",align:"left",sortable:"true"},
			{field:'timestamp',title:'TIMESTAMP',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'change_percent',title:'CHANGE(%)',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',width:"8%",halign:"center",align:"right",sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',width:"9%",halign:"center",align:"center",sortable:"true"}
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
	
	$("#currentPage").val("1");
	fnSearch();
}

function fnSearch(){
	$("#dbid").val($('#selectDbid').combobox('getValue'));	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - MISSING OR STALE STATISTICS"," "); 
	ajaxCall("/PreventiveCheck/MissingOrStaleStatistics",
			$("#submit_form"),
			"POST",
			callback_MissingOrStaleStatisticsAction);
	
}

//callback 함수
var callback_MissingOrStaleStatisticsAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);	
};

function formValidationCheck(){
	return true;
}

/**
 * 엑셀 다운로드
 * @returns
 */
function Excel_Download(){	
	if(!formValidationCheck()){
		return;
	}
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}

	$("#submit_form").attr("action","/PreventiveCheck/MissingOrStaleStatistics/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

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
							+ nullStringToBlank(rows[i].partition_name) + "|";
				}
				$("#submit_form #check_except_object").val(strRight(exceptObjectNameArry,1));
				console.log("check_except_object :"+$("#submit_form #check_except_object").val());
		
				ajaxCall("/ExceptionManagement/RegistException", $("#submit_form"), "POST", callback_RegistExceptionAction);
			}
		
		});
	}else{
		parent.$.messager.alert('','예외등록할 데이터를 선택해 주세요.');
	}
}
