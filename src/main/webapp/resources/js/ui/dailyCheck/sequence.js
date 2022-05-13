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
			{field:'sequence_owner',title:'SEQUENCE_OWNER',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'sequence_name',title:'SEQUENCE_NAME',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'min_value',title:'MIN_VALUE',width:"11%",halign:"center",align:"right",sortable:"true"},
			{field:'max_value',title:'MAX_VALUE',width:"11%",halign:"center",align:"right",sortable:"true"},
			{field:'increment_by',title:'INCREMENT_BY',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'cycle_flag',title:'CYCLE_FLAG',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'order_flag',title:'ORDER_FLAG',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'cache_size',title:'CACHE_SIZE',width:"11%",halign:"center",align:"right",sortable:"true"},
			{field:'last_number',title:'LAST_NUMBER',width:"11%",halign:"center",align:"right",sortable:"true"}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - SEQUENCE"," "); 

	ajaxCall("/PreventiveCheck/Sequence",
			$("#submit_form"),
			"POST",
			callback_SequenceAction);
}

//callback 함수
var callback_SequenceAction = function(result) {
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
					exceptObjectNameArry += nullStringToBlank(rows[i].sequence_owner) +"^"+nullStringToBlank(rows[i].sequence_name) + "|";
				}		
				$("#submit_form #check_except_object").val(strRight(exceptObjectNameArry,1));
		
				ajaxCall("/ExceptionManagement/RegistException", $("#submit_form"), "POST", callback_RegistExceptionAction);
			}
		
		});
	}else{
		parent.$.messager.alert('','예외등록할 데이터를 선택해 주세요.');
	}
}

