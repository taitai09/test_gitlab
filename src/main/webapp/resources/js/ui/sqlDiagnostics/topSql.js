$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : true,
		onClickRow : function(index,row) {
			// 신규 탭 생성..
			parent.createSQLNewTab($("#menu_id").val(), "sqlTabs", $("#dbid").val(), row.sql_id, row.plan_hash_value);
		},			
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
		    {field:'tuning_tgt_yn',title:'튜닝요청',halign:"center",align:'center',sortable:"true",},
		    {field:'gather_day',title:'기준일자',halign:"center",align:'center',sortable:"true",formatter:getDateFormat},
		    {field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:"center",sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA NAME',halign:"center",align:'center',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},			
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},			
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true", formatter:getNumberFormat},
			{field:'ratio_buffer_gets',title:'RATIO_BUFFER_GETS',halign:"center",align:'right',sortable:"true", formatter:getNumberFormat},
			{field:'ratio_elapsed_time',title:'RATIO_ELAPSED_TIME',halign:"center",align:'right',sortable:"true", formatter:getNumberFormat},
			{field:'ratio_cpu_time',title:'RATIO_CPU_TIME',halign:"center",align:'right',sortable:"true", formatter:getNumberFormat},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#sqlTabs').tabs({
		plain : true
	});	
	
	$('#selectCombo').combobox("setValue",$("#dbid").val());
	
	Btn_OnClick();
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 시작일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 종료일자를 선택해 주세요.');
		return false;
	}
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 진단 - TOP SQL"," "); 

	ajaxCall("/SQLDiagnostics/TopSql",
		$("#submit_form"),
		"POST",
		callback_TopSqlAddTable);		
}

//callback 함수
var callback_TopSqlAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}
//
//function showTuningReqPopup(){	
//	var sqlIdArry = "";
//	var gatherDayArry = "";
//	var planHashValueArry = "";
//	var rows = "";
//	
//	if($('#selectCombo').combobox('getValue') == ""){
//		parent.$.messager.alert('','DB를 선택해 주세요.');
//		return false;
//	}	
//	
//	$("#dbid").val($('#selectCombo').combobox('getValue'));
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();
//	
//	rows = $('#tableList').datagrid('getChecked');
//	
//	if(rows.length > 0){
//		parent.$('#tuningAssignPop').window("open");
//		
//		parent.$('#assign_form #dbid').val("");
//		parent.$('#assign_form #auto_share').val("N");
//		parent.$('#assign_form #sqlIdArry').val("");
//		parent.$('#assign_form #planHashValueArry').val("");
//		parent.$('#assign_form #gatherDayArry').val("");
//		parent.$('#assign_form #perfr_id').val("");
//		parent.$('#assign_form #strGubun').val("");
//
//		for(var i = 0 ; i < rows.length ; i++){
//			sqlIdArry += rows[i].sql_id + "|";
//			gatherDayArry += rows[i].gather_day + "|";
//			planHashValueArry += rows[i].plan_hash_value + "|";
//		}
//		
//		parent.$("#assign_form #sqlIdArry").val(strRight(sqlIdArry,1));
//		parent.$("#assign_form #gatherDayArry").val(strRight(gatherDayArry,1));
//		parent.$("#assign_form #planHashValueArry").val(strRight(planHashValueArry,1));
//
//		parent.$('#assign_form #dbid').val($('#dbid').val());
//		parent.$('#assign_form #choice_div_cd').val($('#choice_id').val());
//		parent.$('#assign_form #table_name').val($('#tableName').val());
//		
//		$("#submit_form #choice_cnt").val(rows.length);
//
//		// 튜닝 담당자 조회			
//		parent.$('#assign_form #selectTuner').combobox({
//		    url:"/Common/getTuner?dbid="+$("#dbid").val(),
//		    method:"get",
//			valueField:'tuner_id',
//		    textField:'tuner_nm'
//		});		
//	}else{
//		parent.$.messager.alert('','튜닝 대상을 선택해 주세요.');
//	}
//}


function showTuningReqPopup(){	
	var sqlIdArry = "";
	var gatherDayArry = "";
	var planHashValueArry = "";
	var rows = "";
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	rows = $('#tableList').datagrid('getChecked');
	initPopupSet();  //튜닝 요청 버그를 방지하기위해 초기화.
	
	if(rows.length > 0){
		$('#tuningAssignPop').window("open");
		
		$('#assign_form #dbid').val("");
		$('#assign_form #auto_share').val("N");
		$('#assign_form #sqlIdArry').val("");
		$('#assign_form #planHashValueArry').val("");
		$('#assign_form #gatherDayArry').val("");
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("");

		for(var i = 0 ; i < rows.length ; i++){
			sqlIdArry += rows[i].sql_id + "|";
			gatherDayArry += rows[i].gather_day + "|";
			planHashValueArry += rows[i].plan_hash_value + "|";
		}
		
		$("#assign_form #sqlIdArry").val(strRight(sqlIdArry,1));
		$("#assign_form #gatherDayArry").val(strRight(gatherDayArry,1));
		$("#assign_form #planHashValueArry").val(strRight(planHashValueArry,1));

		$('#assign_form #dbid').val($('#dbid').val());
		$('#assign_form #choice_div_cd').val($('#choice_id').val());
		$('#assign_form #table_name').val($('#tableName').val());
		
		$("#submit_form #choice_cnt").val(rows.length);

		// 튜닝 담당자 조회			
		$('#assign_form #selectTuner').combobox({
		    url:"/Common/getTuner?dbid="+$("#dbid").val(),
		    method:"get",
			valueField:'tuner_id',
		    textField:'tuner_nm'
		});		
	}else{
		parent.$.messager.alert('','튜닝 대상을 선택해 주세요.');
	}
}
