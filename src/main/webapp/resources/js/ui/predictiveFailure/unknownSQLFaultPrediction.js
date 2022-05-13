var chartCpuUtilizationPanel;
var chartElapsedTimeDiffPanel;
var chartCpuTimeDiffPanel;
var chartPhysicalReadsDiffPanel;
var chartLogicalReadsDiffPanel;
var chartSortDiffPanel;

Ext.EventManager.onWindowResize(function () {
//	var width = $("#chartNewSQLTimeoutPredictionPanel").width();
//	
//	if(chartNewSQLTimeoutPredictionPanel != "undefined" && chartNewSQLTimeoutPredictionPanel != undefined){
//		chartNewSQLTimeoutPredictionPanel.setSize(width);
//	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createCpuUtilizationChart();
	createElapsedTimeDiffChart();
	createCpuTimeDiffChart();
	createPhysicalReadsDiffChart();
	createLogicalReadsDiffChar();
	createSortDiffChart();
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#selectDbidCombo").combobox("textbox").attr('placeholder',"선택");
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$('#dbid').val(rec.dbid);
			
			// Instance 조회
			$('#selectInstance').combobox({
				url:"/Common/getAgentInstance?dbid="+rec.dbid,
				method:"get",
				valueField:'inst_id',
				textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','Instance 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}
	});
	
	$('#selectDbidCombo').combobox("setValue",$("#dbid").val());
	
	$("#adhocTopSqlGrid").datagrid({
		view: myview,
		nowrap : true,
		fitColumns : false,
		checkOnSelect : false,
		selectOnCheck : false,
		singleSelect : false,
		columns:[[
			{field:'sql_id',title:'SQL ID',width:"14%",halign:"center",align:'left',sortable:"true"},
			{field:'cpu_time',title:'CPU TIME',width:"10%",halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time_ratio',title:'CPU TIME RATIO',width:"13%",halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED TIME',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'parse_calls',title:'PARSE CALLS',width:"11%",halign:"center",align:'right',sortable:"true"},
			{field:'rows_processed',title:'ROWS PROCESSED',width:"15%",halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK READS',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER GETS',width:"12%",halign:"center",align:'right',sortable:"true"}
			]],
		onLoadSuccess: function(data){
			var d = new Date();
			console.log("adhocTopSqlGrid onLoadSuccess " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#adhocTopModuleGrid").datagrid({
		view: myview,
		nowrap : true,
		fitColumns : false,
		checkOnSelect : false,
		selectOnCheck : false,
		singleSelect : false,
		columns:[[
			{field:'module',title:'MODULE',width:"17%",halign:"center",align:'center',sortable:"true"},
			{field:'cpu_time',title:'CPU TIME',width:"10%",halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time_ratio',title:'CPU TIME RATIO',width:"13%",halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED TIME',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'parse_calls',title:'PARSE CALLS',width:"11%",halign:"center",align:'right',sortable:"true"},
			{field:'rows_processed',title:'ROWS PROCESSED',width:"15%",halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK READS',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER GETS',width:"12%",halign:"center",align:'right',sortable:"true"},
			]],
		onLoadSuccess: function(data){
			var d = new Date();
			console.log("adhocTopModuleGrid onLoadSuccess " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	function createCpuUtilizationChart(result){
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
		
		if(chartCpuUtilizationPanel != "undefined" && chartCpuUtilizationPanel != undefined){
			chartCpuUtilizationPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartCpuUtilizationPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartCpuUtilizationPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['begin_interval_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'begin_interval_time',
					yField : ['cpu_usage'],
					title : 'CPU 사용률 추이',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
	
	function createElapsedTimeDiffChart(result){
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
		
		if(chartElapsedTimeDiffPanel != "undefined" && chartElapsedTimeDiffPanel != undefined){
			chartElapsedTimeDiffPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartElapsedTimeDiffPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartElapsedTimeDiffPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['snap_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['r_pgm_elapsed_time'],
					title : '정규 SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['a_pgm_elapsed_time'],
					title : 'Unknown SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
	
	function createCpuTimeDiffChart(result){
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
		
		if(chartCpuTimeDiffPanel != "undefined" && chartCpuTimeDiffPanel != undefined){
			chartCpuTimeDiffPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartCpuTimeDiffPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartCpuTimeDiffPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['snap_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['r_pgm_cpu_time'],
					title : '정규 SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['a_pgm_cpu_time'],
					title : 'Unknown SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
	
	function createPhysicalReadsDiffChart(result){
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
		
		if(chartPhysicalReadsDiffPanel != "undefined" && chartPhysicalReadsDiffPanel != undefined){
			chartPhysicalReadsDiffPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartPhysicalReadsDiffPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartPhysicalReadsDiffPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['snap_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['r_pgm_disk_reads'],
					title : '정규 SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['a_pgm_disk_reads'],
					title : 'Unknown SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
	
	function createLogicalReadsDiffChar(result){
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
		
		if(chartLogicalReadsDiffPanel != "undefined" && chartLogicalReadsDiffPanel != undefined){
			chartLogicalReadsDiffPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartLogicalReadsDiffPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartLogicalReadsDiffPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['snap_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['r_pgm_buffer_gets'],
					title : '정규 SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['a_pgm_buffer_gets'],
					title : 'Unknown SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
	
	function createSortDiffChart(result){
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
		
		if(chartSortDiffPanel != "undefined" && chartSortDiffPanel != undefined){
			chartSortDiffPanel.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		chartSortDiffPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartSortDiffPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: false,
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 13px Arial',
							fill:"#000000"
						}
					}
				},
				legend: {
					docked: 'bottom',
					marker: {
						type: 'square'
					},
					border: {
						radius: 0
					},
					type: 'dom'		// remove border from Legend 
				},
				type:'text',
				innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minorTickSteps: 0,
					minimum: 0,
					grid: {
						odd: {
							opacity: 1,
							fill: '#eee',
							stroke: '#bbb',
							lineWidth: 1
						}
					},
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['snap_time'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['r_pgm_sorts'],
					title : '정규 SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 1,
					},
					xField : 'snap_time',
					yField : ['a_pgm_sorts'],
					title : 'Unknown SQL',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					}
				}]
			}]
		});
	}
});

function Btn_ShowRegularSQLFilteringCase(){
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	initializeRegularSQLFilterCasePop();
	
	$('#regularSQLFilterCasePop').window("open");
	
	$("#regularSQLParsingSchemaNameList").datagrid("resize",{
		width: 800
	});
	
	$("#regularSQLFilteringCaseList").datagrid("resize",{
		width: 800
	});
}

function Btn_OnClick(){
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	var startDate = $("input[name='start_first_analysis_day").val();
	var endDate = $("input[name='end_first_analysis_day").val();
	
	if(checkDate(startDate) == false || checkDate(endDate) == false) {
		parent.$.messager.alert('','분석일의 형식을 확인해 주세요.');
		return false;
	}
	
	if(!compareAnBDate(startDate, endDate)) {
		parent.$.messager.alert('','분석일을 확인해 주세요.');
		return false;
	}
	
	$('#inst_id').val($('#selectInstance').combobox('getValue'));
	
	$('#adhocTopSqlGrid').datagrid("loadData", []);
	$('#adhocTopModuleGrid').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Unknown SQL 장애 예측"," ");
	
	ajaxCall("/unknownSQLFaultPredictionChartList",
			$("#submit_form"),
			"POST",
			callback_method);
}

var callback_method = function(result) {
	let CPU_USAGE			= "CPU_USAGE";
	let ELAPSED_TIME		= "ELAPSED_TIME";
	let CPU_TIME			= "CPU_TIME";
	let DISK_READS			= "DISK_READS";
	let BUFFER_GETS			= "BUFFER_GETS";
	let SORTS				= "SORTS";
	let ADHOC_TOP_SQL		= "ADHOC_TOP_SQL";
	let ADHOC_TOP_MODULE	= "ADHOC_TOP_MODULE";

	var data = JSON.parse(result);
	var unknownSqlStat = data.unknownSqlStat;
	
	var cpuUtilizationData = JSON.parse(unknownSqlStat.CPU_USAGE);
	var elapsedTimeDiffData = JSON.parse(unknownSqlStat.ELAPSED_TIME);
	var cpuTimeDiffData = JSON.parse(unknownSqlStat.CPU_TIME);
	var diskReadsDiffData = JSON.parse(unknownSqlStat.DISK_READS);
	var bufferGetsDiffData = JSON.parse(unknownSqlStat.BUFFER_GETS);
	var sortsDiffData = JSON.parse(unknownSqlStat.SORTS);
	var adhocTopSQLData = JSON.parse(unknownSqlStat.ADHOC_TOP_SQL);
	var adhocTopModuleData = JSON.parse(unknownSqlStat.ADHOC_TOP_MODULE);
	
	drawChartCpuUtilization(cpuUtilizationData);
	drawChartElapsedTimeDiff(elapsedTimeDiffData);
	drawChartCpuTimeDiff(cpuTimeDiffData);
	drawChartPhysicalReadsDiff(diskReadsDiffData);
	drawChartLogicalReadsDiff(bufferGetsDiffData);
	drawChartSortDiff(sortsDiffData);
	
	json_string_callback_common(unknownSqlStat.ADHOC_TOP_SQL, '#adhocTopSqlGrid', true);
	json_string_callback_common(unknownSqlStat.ADHOC_TOP_MODULE, '#adhocTopModuleGrid', true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var drawChartCpuUtilization = function(data) {
	var store = chartCpuUtilizationPanel.down("chart").getStore();
	store.loadData(data.rows);
	
	chartCpuUtilizationPanel.down("chart").redraw();
}

var drawChartElapsedTimeDiff = function(data) {
	var store = chartElapsedTimeDiffPanel.down("chart").getStore();
	store.loadData(data.rows);
	
	chartElapsedTimeDiffPanel.down("chart").redraw();
}

var drawChartCpuTimeDiff = function(data) {
	var store = chartCpuTimeDiffPanel.down("chart").getStore();
	
	store.loadData(data.rows);
	
	chartCpuTimeDiffPanel.down("chart").redraw();
}

var drawChartPhysicalReadsDiff = function(data) {
	var store = chartPhysicalReadsDiffPanel.down("chart").getStore();
	
	store.loadData(data.rows);
	
	chartPhysicalReadsDiffPanel.down("chart").redraw();
}

var drawChartLogicalReadsDiff = function(data) {
	var store = chartLogicalReadsDiffPanel.down("chart").getStore();
	
	store.loadData(data.rows);
	
	chartLogicalReadsDiffPanel.down("chart").redraw();
}
var drawChartSortDiff = function(data) {
	var store = chartSortDiffPanel.down("chart").getStore();
	
	store.loadData(data.rows);
	
	chartSortDiffPanel.down("chart").redraw();
}