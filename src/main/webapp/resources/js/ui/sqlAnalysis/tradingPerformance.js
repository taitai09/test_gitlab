$(document).ready(function() {
	// 업무구분 조회			
	$('#selectWrkJob').combobox({
	    url:"/Common/getWrkJobDev",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	$("#wrkjob_cd").val(rec.wrkjob_cd);
	    	$("#dbid").val(rec.dbid);
	    }	    
	});	
	
	$("#dbioList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#appl_hash").val(row.appl_hash);
			$("#sql_hash").val(row.sql_hash);
			
			getSubTables()
		},		
		columns:[[
			{field:'appl_name',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:"left",sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'exec_cnt',title:'수행횟수',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'appl_hash',title:'APPL_HASH',halign:"center",align:'right',sortable:"true"},
			{field:'sql_hash',title:'SQL_HASH',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"50%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dailyTradeList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'left',sortable:"true"},
			{field:'fail_cnt',title:'FAIL COUNT',halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG ELAPSED TIME',halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN ELAPSED TIME',halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX ELAPSED TIME',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#timeTradeList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'fail_cnt',title:'FAIL COUNT',halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dailyDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#timeDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#historyDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'snap_time',title:'SNAP TIME',halign:"center",align:"left",sortable:"true"},
			{field:'snap_id',title:'SNAP ID',halign:"center",align:'left',sortable:"true"},
			{field:'instance_number',title:'INSTANCE NUMBER',halign:"center",align:'right',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'right',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'row_processed',title:'ROW_PROCESSED',halign:"center",align:'right',sortable:"true"},
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true"},			
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'right',sortable:"true"},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true"},
			{field:'optimizer_env_hash_value',title:'OPTIMIZER ENV HASH VALUE',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
});

function Btn_OnClick(){
	if($('#wrkjob_cd').val() == ""){
		$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if(($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
		($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
		parent.$.messager.alert('','조회 구분값을 입력해주세요.');
		return false;
	}	

	$("#searchKey").val($('#selectKey').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#dbioList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("거래 성능 분석"," "); 
	
	ajaxCall("/TradingPerformance",
		$("#submit_form"),
		"POST",
		callback_TradingPerformanceAddTable);
}

//callback 함수
var callback_TradingPerformanceAddTable = function(result) {
	var data = JSON.parse(result);
	$('#dbioList').datagrid("loadData", data);

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function getSubTables(){
	/* 거래 일별 */
	$('#dailyTradeList').datagrid("loadData", []);
	$('#dailyTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dailyTradeList').datagrid('loading');	
	ajaxCall("/TradingPerformance/TradeDailyList",
			$("#submit_form"),
			"POST",
			callback_TradeDailyListAddTable);
	
	/* 거래 시간별 */
	$('#timeTradeList').datagrid("loadData", []);
	$('#timeTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#timeTradeList').datagrid('loading');		
	ajaxCall("/TradingPerformance/TradeTimeList",
			$("#submit_form"),
			"POST",
			callback_TradeTimeListAddTable);
	
	/* SQL식별자(DBIO) 일별 */
	$('#dailyDbioList').datagrid("loadData", []);
	$('#dailyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dailyDbioList').datagrid('loading');	
	ajaxCall("/TradingPerformance/DBIODailyList",
			$("#submit_form"),
			"POST",
			callback_DBIODailyListAddTable);
	
	/* SQL식별자(DBIO) 시간별 */
	$('#timeDbioList').datagrid("loadData", []);
	$('#timeDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#timeDbioList').datagrid('loading');	
	ajaxCall("/TradingPerformance/DBIOTimeList",
			$("#submit_form"),
			"POST",
			callback_DBIOTimeListAddTable);
	
	/* SQL식별자(DBIO) 수행이력 */
	$('#historyDbioList').datagrid("loadData", []);
	$('#historyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#historyDbioList').datagrid('loading');		
	ajaxCall("/TradingPerformance/DBIOHistoryList",
			$("#submit_form"),
			"POST",
			callback_DBIOHistoryListAddTable);	
}

//callback 함수
var callback_TradeDailyListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#dailyTradeList').datagrid("loadData", data);
	$('#dailyTradeList').datagrid('loaded');	
};

//callback 함수
var callback_TradeTimeListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#timeTradeList').datagrid("loadData", data);
	$('#timeTradeList').datagrid('loaded');	
};

//callback 함수
var callback_DBIODailyListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#dailyDbioList').datagrid("loadData", data);
	$('#dailyDbioList').datagrid('loaded');	
};

//callback 함수
var callback_DBIOTimeListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#timeDbioList').datagrid("loadData", data);
	$('#timeDbioList').datagrid('loaded');	
};

//callback 함수
var callback_DBIOHistoryListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#historyDbioList').datagrid("loadData", data);
	$('#historyDbioList').datagrid('loaded');	
};