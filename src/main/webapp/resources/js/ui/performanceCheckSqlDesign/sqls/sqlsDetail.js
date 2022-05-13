var chartExecutionsPanel;
var chartElapsedTimePanel;
var chartBufferGetsPanel;
var chartDiskReadsPanel;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	/* modal progress open */
	//top.openMessageProgress("SQL Detail이 조회중입니다.");
	
	createExecutionsChart();
	createElapsedTimeChart();
	createBufferGetsChart();
	createDiskReadsChart();
	
	$('#treePlan').tree({
		animate:true,
		lines:true
	});
	
	$("#bottomList").datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'No',halign:"center",align:'right'},
			{field:'log_dt',title:'LOG_DT',halign:'center',align:'center'},
			{field:'inst_id',title:'INST_ID',halign:'center',align:'right'},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},			
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true"},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true"}
		]],
		onLoadError:function() {
			top.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#bindValueList").datagrid({
		view: myview,
		columns:[[
//			{field:'snap_id',title:'SNAP_ID',halign:"center",align:'center'},
			{field:'name',title:'NAME',halign:"center",align:'center'},
			{field:'value',title:'VALUE',halign:"center",align:'left'},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right'},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right'},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right'},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right'},
			{field:'instance_number',title:'INST_ID',halign:"center",align:'right'},
			{field:'exec_time',title:'EXECUTE_TIME',halign:"center",align:'center'}
		]],
		onLoadError:function() {
			top.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		} 
	});
	
	$("#outLineList").datagrid({
		view: myview,
		columns:[[
			{field:'hint',title:'OUTLINE',width:"100%",halign:"center",align:'left'}
		]],
		
		onLoadError:function() {
			top.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#similarityList").datagrid({
		view: myview,
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"500px",halign:"center",align:'right',sortable:"true"}
		]],
		
		onLoadError:function() {
			top.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#sqlGridPlanList").treegrid({
		idField:'id',
		treeField:'operation',
		lines: true,
		columns:[[
			{field:'id',title:'ID',halign:"center",align:"center"},
			{field:'imid',title:'IMID',halign:"center",align:'right'},
			{field:'operation',title:'OPERATION',halign:"center",align:'left'},
			{field:'object_node',title:'OBJECT NODE',halign:"center",align:'right'},
			{field:'object',title:'OBJECT#',halign:"center",align:'right'},
			{field:'object_owner',title:'OBJECT OWNER',halign:"center",align:'left'},
			{field:'object_name',title:'OBJECT NAME',halign:"center",align:'left'},
			{field:'object_type',title:'OBJECT TYPE',halign:"center",align:'left'},
			{field:'optimizer',title:'OPTIMIZER',halign:"center",align:'left'},
			{field:'cost',title:'COST',halign:"center",align:'right'},
			{field:'cardinality',title:'CADINALITY',halign:"center",align:'right'},
			{field:'bytes',title:'BYTES',halign:"center",align:'right'},
			{field:'other_tag',title:'OTHER_TAG',halign:"center",align:'right'},
			{field:'partition_start',title:'PARTITION START',halign:"center",align:'right'},
			{field:'partition_stop',title:'PARTITION STOP',halign:"center",align:'right'},
			{field:'cpu_cost',title:'CPU COST',halign:"center",align:'right'},
			{field:'io_cost',title:'IO COST',halign:"center",align:'right'},
			{field:'access_predicates',title:'ACCESS PREDICATES',halign:"center",align:'left'},
			{field:'filter_predicates',title:'FILTER PREDICATES',halign:"center",align:'left'},
			{field:'projection',title:'PROJECTION',halign:"center",align:'left'},
			{field:'time',title:'TIME',halign:"center",align:'right'},
			{field:'qblock_name',title:'QBLOCK_NAME',halign:"center",align:'left'},
			{field:'timestamp',title:'TIMESTAMP',halign:"center",align:'left'}
		]],
		
		onLoadError:function() {
			top.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#bindBtn").linkbutton({disabled:true});
	$("#historyBtn").linkbutton({disabled:true});
	
	searchSqlPerformancePopup();
});

function comparePreviousValue(val, row) {
	if(val === null || val == 0) {
		try {
			let rowIndex = $('#bindValueList').datagrid('getRowIndex', row);
			let previousRow = $('#bindValueList').datagrid('getRows')[rowIndex -1];
			
			if(previousRow.exec_time != row.exec_time) {
				return val;
			}
		} catch(error) {
			console.log(error.message);
		}
		
		return '';
	} else {
		return val;
	}
}

function createExecutionsChart(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	if(chartExecutionsPanel != "undefined" && chartExecutionsPanel != undefined){
		chartExecutionsPanel.destroy();
	}
	
	Ext.define("chartExecutionsPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50'
	});
	
	chartExecutionsPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartExecutionsPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				fill : false,
				marker : {
					lineWidth: 2,
					radius : 4,
				},
//				axis: 'left',
				xField : 'log_dt',
				yField : 'executions',
				colors: [chartExecutionsPanel.colors.COLOR_01],
//				label: {
//					field: 'before_buffer_gets_chart',
//					display: 'insideEnd',
//					orientation: 'horizontal'
//				}
			}]
		}]
	});
}

function createElapsedTimeChart(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	if(chartElapsedTimePanel != "undefined" && chartElapsedTimePanel != undefined){
		chartElapsedTimePanel.destroy();
	}
	
	Ext.define("chartElapsedTimePanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50'
	});
	
	chartElapsedTimePanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartElapsedTimePanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				fill : false,
				marker : {
					lineWidth: 2,
					radius : 4,
				},
//				axis: 'left',
				xField : 'log_dt',
				yField : 'elapsed_time',
				colors: [chartElapsedTimePanel.colors.COLOR_02],
//				label: {
//					field: 'before_buffer_gets_chart',
//					display: 'insideEnd',
//					orientation: 'horizontal'
//				}
			}]
		}]
	});
}

function createBufferGetsChart(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	if(chartBufferGetsPanel != "undefined" && chartBufferGetsPanel != undefined){
		chartBufferGetsPanel.destroy();
	}
	
	Ext.define("chartBufferGetsPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50'
	});
	
	chartBufferGetsPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartBufferGetsPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				fill : false,
				marker : {
					lineWidth: 2,
					radius : 4,
				},
//				axis: 'left',
				xField : 'log_dt',
				yField : 'buffer_gets',
				colors: [chartBufferGetsPanel.colors.COLOR_03],
//				label: {
//					field: 'before_buffer_gets_chart',
//					display: 'insideEnd',
//					orientation: 'horizontal'
//				}
			}]
		}]
	});
}

function createDiskReadsChart(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	if(chartDiskReadsPanel != "undefined" && chartDiskReadsPanel != undefined){
		chartDiskReadsPanel.destroy();
	}
	
	Ext.define("chartDiskReadsPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50'
	});
	
	chartDiskReadsPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartDiskReadsPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				fill : false,
				marker : {
					lineWidth: 2,
					radius : 4,
				},
//				axis: 'left',
				xField : 'log_dt',
				yField : 'buffer_gets',
				colors: [chartDiskReadsPanel.colors.COLOR_04],
//				label: {
//					field: 'before_buffer_gets_chart',
//					display: 'insideEnd',
//					orientation: 'horizontal'
//				}
			}]
		}]
	});
}

function searchSqlPerformancePopup(){
	$('#treePlan').tree('loadData',[]);
	$('#bindValueList').datagrid('loadData',[]);
	$('#outLineList').datagrid('loadData',[]);
	$('#similarityList').datagrid('loadData',[]);
	$('#sqlGridPlanList').treegrid('loadData',[]);
	$("#textArea").html("");
	
	$("#currentPage").val("1");
	$("#bindPage").val("1");
	$("#historyPage").val("1");
	
	/* 상단 텍스트 박스 */
	ajaxCall("/Sqls/sqlTextAll",
			$("#submit_form"),
			"POST",
			callback_SQLTextAction);
	
	/* Bind Value */
	ajaxCall("/Sqls/bindValueListAll",
			$("#submit_form"),
			"POST",
			callback_BindValueAction);
	
	/* Tree Plan */
	ajaxCall("/Sqls/sqlTreePlanListAll",
			$("#submit_form"),
			"POST",
			callback_TreePlanAction);
	
	/* Text Plan */
	ajaxCall("/Sqls/sqlTextPlanListAll",
			$("#submit_form"),
			"POST",
			callback_TextPlanAction);
	
	/* Grid Plan */
	ajaxCall("/Sqls/sqlGridPlanListAll",
			$("#submit_form"),
			"POST",
			callback_GridPlanAction);
	
	/* OutLine */
	ajaxCall("/Sqls/outLineListAll",
			$("#submit_form"),
			"POST",
			callback_OutLineAction);
	
	ajaxCall("/Sqls/sqlStatTrend",
			$("#submit_form"),
			"POST",
			callback_SqlStatTrendAction);
	
	ajaxCall("/Sqls/sqlPerformHistoryList",
			$("#submit_form"),
			"POST",
			callback_SQLPerformHistoryAddTable);
}

//callback 함수
var callback_SQLTextAction = function(result) {
	if(result.result){
		var post = result.object;
		//$("#textArea").html(strReplace(post.sql_text, "\r\n", "<br/>"));
		if(post != null){
			$("#textArea").val(post[0].sql_text);
		}
	}else{
		parent.$.messager.alert('SQL TEXT',result.message);
	}
};

//callback 함수
var callback_BindValueAction = function(result) {
	var data = JSON.parse(result);
	console.log(data);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#bindValueList').datagrid("loadData", data);
		$("#bindValueList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}
	
	$("#bindBtn").linkbutton({disabled:false});
};

//callback 함수
var callback_SQLPerformHistoryAddTable = function(result) {
	json_string_callback_common(result,'#bottomList',false);
	$("#bottomList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	
	$("#historyBtn").linkbutton({disabled:false});
};

//callback 함수
var callback_TreePlanAction = function(result) {
	var data = JSON.parse(result);

	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#treePlan').tree("loadData", data);
	}
	$("#treePlan .tree-node" ).css( "cursor", "default" );
	
};

//callback 함수
var callback_TextPlanAction = function(result) {
	var strHtml = "";
	
	$("#textPlan li").remove();
	
	if(result.result){
		if(result.object == null) {
			return;
		}
		
		strHtml += "<li><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			strHtml += "<li>"+strReplace(post.execution_plan,' ','&nbsp;')+"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#textPlan").append(strHtml);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_GridPlanAction = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#sqlGridPlanList').treegrid("loadData", data);
		$('#sqlGridPlanList').treegrid('loaded');
		$("#sqlGridPlanList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#sqlGridPlanList div.datagrid-cell.tree-node" ).css( "cursor", "default !important" );
	}
};

//callback 함수
var callback_OutLineAction = function(result) {
	json_string_callback_common(result,'#outLineList',false);
};

//callback 함수
var callback_SimilaritySqlAction = function(result) {
	json_string_callback_common(result,'#similarityList',false);
};

function Btn_NextBindSearch(){
	var currentPage = $("#bindPage").val();
	currentPage++;
	
	$("#bindPage").val(currentPage);
	$("#currentPage").val(currentPage);
	
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	ajaxCall("/Sqls/BindValueNext",
		$("#submit_form"),
		"POST",
		callback_BindValueNextAddTable);
}

//callback 함수
var callback_BindValueNextAddTable = function(result) {
	let previousRowIndex = $('#bindValueList').datagrid('getData').rows.length;
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			$('#bindValueList').datagrid('appendRow',{
//				snap_id         : post.snap_id,
				name            : post.name,
				value           : post.value,
				elapsed_time    : strParseFloat(post.elapsed_time,0),
				buffer_gets     : strParseFloat(post.buffer_gets,0),
				rows_processed  : strParseFloat(post.rows_processed,0),
				plan_hash_value : post.plan_hash_value,
				exec_time       : post.exec_time,
				exec_inst_id    : post.exec_inst_id
			});
		}
	}else{
		top.$.messager.alert('','더 이상 검색된 데이터가 없습니다.');
		$("#bindBtn").linkbutton({disabled:true});
	}
	
	$('#bindValueList').datagrid('loaded');
	$('#bindValueList').datagrid('reload');
	
	$('#bindValueList').datagrid('scrollTo', previousRowIndex);
};

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}

function Btn_NextHistorySearch(){
	var currentPage = $("#historyPage").val();
	currentPage++;
	
	$("#historyPage").val(currentPage);
	$("#currentPage").val(currentPage);
	
	$('#bottomList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bottomList').datagrid('loading');
	
	ajaxCall("/Sqls/sqlPerformHistoryList",
		$("#submit_form"),
		"POST",
		callback_SQLPerformHistoryAddTableNext);
}

//callback 함수
var callback_SQLPerformHistoryAddTableNext = function(result) {
	let previousRowIndex = $('#bottomList').datagrid('getData').rows.length;
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			$('#bottomList').datagrid('appendRow',{
				begin_interval_time      : post.begin_interval_time,
				snap_id                  : post.snap_id,
				instance_number          : post.instance_number,
				sql_id                   : post.sql_id,
				plan_hash_value          : post.plan_hash_value,
				module                   : post.module,
				parsing_schema_name      : post.parsing_schema_name,
				elapsed_time             : strParseFloat(post.elapsed_time,0),
				cpu_time                 : strParseFloat(post.cpu_time,0),
				buffer_gets              : strParseFloat(post.buffer_gets,0),
				disk_reads               : strParseFloat(post.disk_reads,0),
				rows_processed           : strParseFloat(post.rows_processed,0),
				clwait_time              : strParseFloat(post.clwait_time,0),
				iowait_time              : strParseFloat(post.iowait_time,0),
				apwait_time              : strParseFloat(post.apwait_time,0),
				ccwait_time              : strParseFloat(post.ccwait_time,0),
				cpu_rate                 : strParseFloat(post.cpu_rate,0),
				clwait_rate              : strParseFloat(post.clwait_rate,0),
				iowait_rate              : strParseFloat(post.iowait_rate,0),
				apwait_rate              : strParseFloat(post.apwait_rate,0),
				ccwait_rate              : strParseFloat(post.ccwait_rate,0),
				executions               : post.executions,
				parse_calls              : post.parse_calls,
				fetches                  : post.fetches,
				optimizer_env_hash_value : post.optimizer_env_hash_value
			});
		}
	}else{
		top.$.messager.alert('','더 이상 검색된 데이터가 없습니다.');
		$("#historyBtn").linkbutton({disabled:true});
	}
	
	$('#bottomList').datagrid('loaded');
	$('#bottomList').datagrid('reload');
	
	$('#bottomList').datagrid('scrollTo', previousRowIndex);
};

var callback_SqlStatTrendAction = function(result) {
	chart_callback(result, chartExecutionsPanel);
	chart_callback(result, chartElapsedTimePanel);
	chart_callback(result, chartBufferGetsPanel);
	chart_callback(result, chartDiskReadsPanel);
	
	/* modal progress close */
	top.closeMessageProgress();
}
