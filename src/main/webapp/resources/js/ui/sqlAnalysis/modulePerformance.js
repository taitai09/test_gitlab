var totalElapsedChart;
var avgElapsedChart;
var avgBufferChart;
var avgDiskChart;
var executionsChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#totalElapsedChart").width();
    if(totalElapsedChart != "undefined" && totalElapsedChart != undefined){
    	totalElapsedChart.setSize(width);		
	}
    if(avgElapsedChart != "undefined" && avgElapsedChart != undefined){
    	avgElapsedChart.setSize(width);		
	}
    if(avgBufferChart != "undefined" && avgBufferChart != undefined){
    	avgBufferChart.setSize(width);		
	}
    if(avgDiskChart != "undefined" && avgDiskChart != undefined){
    	avgDiskChart.setSize(width);		
	}
    if(executionsChart != "undefined" && executionsChart != undefined){
    	executionsChart.setSize(width);		
	}
});	

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
		onLoadSuccess: function(){
			$("#selectDbidCombo").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$('#selectRcount').combobox({
		onChange: function(newval, oldval){
//			var rcount = $(this).combobox("getValue");
//			if(rcount == ''){
//				parent.$.messager.alert('','전체 데이터를 조회하는데는 시간이 오래 소요됩니다.<br>잠시만 기다려주시기 바랍니다.');
//			}
			$("#rcount").val(newval);
//			$("#currentPage2").val("1");
			searchSQLList();
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#sql_id").val(row.sql_id);
			$("#submit_form #currentPage").val("1");
			
			getDetailModuleTable();
		},		
		columns:[[
			{field:'module',title:'MODULE',halign:"center",align:"left",sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:"left",sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true"},
			{field:'avg_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'avg_cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'avg_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},			
			{field:'avg_row_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},			
			{field:'avg_phyiscal_reads',title:'AVG_ORDS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'last_exec_time',title:'LAST_EXEC_TIME',halign:"center",align:'right',sortable:"true"},			
			{field:'sql_text',title:'SQL TEXT',halign:"center",align:'left',sortable:"true"}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dtlTableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			var menuParam = "dbid="+$("#dbid").val()
			+ "&sql_id=" + row.sql_id
			+ "&plan_hash_value=" + row.plan_hash_value
			+ "&call_from_parent=Y";
			
			parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
		},	
		columns:[[
			{field:'begin_interval_time',title:'BEGIN_INTERVAL_TIME',halign:"center",align:"center",sortable:"true",styler:cellStyler},
			{field:'snap_id',title:'SNAP_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'instance_number',title:'INSTANCE_NUMBER',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},			
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true",styler:cellStyler},			
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'optimizer_env_hash_value',title:'OPTIMIZER_ENV_HASH_VALUE',halign:"center",align:'right',sortable:"true",styler:cellStyler}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$('.datatime').timespinner({
		showSeconds: true
	});
	
	$("#moudleTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			if(index == 0){
				var tab1ClickCount = $("#submit_form #tab1ClickCount").val();
				if(tab1ClickCount < searchBtnClickCount){
//					$("#submit_form #tab1ClickCount").val(1);
					searchSQLList();
				}
			}else if(index == 1){
				var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
				if(tab2ClickCount < searchBtnClickCount){
//					$("#submit_form #tab2ClickCount").val(1);
					createTOPStatChart();
				}
			}			
		}
	});
	
	$('#module').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick();
				}
			}
		})
	});
});

function Btn_OnClick(){
	if(!formValidationCheck()){
		return false;
	};

	var tab = $('#moudleTab').tabs('getSelected');
	var index = $('#moudleTab').tabs('getTabIndex',tab);
	
	
	fnUpdateSearchBtnClickFlag();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#module').textbox("setValue", $('#module').textbox('getValue').trim());
	$("#start_interval_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
	$("#end_interval_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	$("#subSearchYn").val("N");
	
	if(index == "0"){
		searchSQLList();
	}else{
		createTOPStatChart();
	}
}

function searchSQLList(){
	
	$("#rcount").val($('#selectRcount').combobox('getValue'));
	$("#currentPage2").val("1");
	
	$("#submit_form #tab1ClickCount").val(1);
	
	$('#tableList').datagrid('loadData',[]);
	$('#dtlTableList').datagrid('loadData',[]);

	/* modal progress open */
//	if(openMessageProgress != undefined) openMessageProgress("모듈 성능 분석"," ");
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모듈 성능 분석"," ");
	
	getModulePerformanceTable();
//	ajaxCall("/ModulePerformance", $("#submit_form"), "POST", callback_ModulePerformanceAddTable);
}

var callback_ModulePerformanceAddTable = function(result) {
	
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	fnEnableDisablePagingBtn2(dataLength);

	json_string_callback_common(result,'#tableList',true);
}

function getModulePerformanceTable(){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모듈 성능 분석"," ");
	ajaxCall("/ModulePerformance", $("#submit_form"), "POST", callback_ModulePerformanceAddTable);
}


function fnSearch() {
	getDetailModuleTable();
}

function getDetailModuleTable(){
	$("#subSearchYn").val("Y");
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("상세 데이터를 불러오는 중입니다."," ");
	
	ajaxCall("/ModulePerformance/DtlModulePerformance",
			$("#submit_form"),
			"POST",
			callback_DtlModulePerformanceTable);
}

//callback 함수
var callback_DtlModulePerformanceTable = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#dtlTableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
}

function createTOPStatChart(){

	$("#submit_form #tab2ClickCount").val(1);

	createTotalElapsedChart();
	createAvgElapsedChart();
	createAvgBufferGetsChart();
	createAvgDiskReadsChart();
	createExecutionsChart();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모듈 성능 분석 - TOP SQL Stat"," "); 
	
	ajaxCall("/ModulePerformance/TOPStatChart",
			$("#submit_form"),
			"POST",
			callback_TOPStatChartAction);
}

function createTotalElapsedChart(){
	if(totalElapsedChart != "undefined" && totalElapsedChart != undefined){
		totalElapsedChart.destroy();
	}
	totalElapsedChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("totalElapsedChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			axes : [{
				type : 'numeric',
				position : 'left',
				adjustByMajorUnit: true,
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
				title : '(SEC)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label: {
					y : -12,
	                rotate: {
	                    degrees: -45
	                }
	            }
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 40
				},
				xField : 'sql_id',
				yField : 'tot_elapsed_time',
				title : 'TOTAL_ELAPSED_TIME',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('sql_id')+" : " + formatComma(value) + " sec");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
						var menuParam = "dbid="+$("#dbid").val()+"&sql_id="+item.record.get('sql_id');
						parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
			        }
			    }
			}
		}]
	});			
}

function createAvgElapsedChart(){
	if(avgElapsedChart != "undefined" && avgElapsedChart != undefined){
		avgElapsedChart.destroy();
	}
	avgElapsedChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("avgElapsedChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			axes : [{
				type : 'numeric',
				position : 'left',
				adjustByMajorUnit: true,
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
				title : '(SEC)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label: {
					y : -12,
	                rotate: {
	                    degrees: -45
	                }
	            }
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 40
				},
				xField : 'sql_id',
				yField : 'avg_elapsed_time',
				title : 'AVG_ELAPSED_TIME',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('sql_id')+" : " + formatComma(value) + " sec");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
						var menuParam = "dbid="+$("#dbid").val()+"&sql_id="+item.record.get('sql_id');
						parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
			        }
			    }
			}
		}]
	});			
}

function createAvgBufferGetsChart(){
	if(avgBufferChart != "undefined" && avgBufferChart != undefined){
		avgBufferChart.destroy();
	}
	avgBufferChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("avgBufferChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			axes : [{
				type : 'numeric',
				position : 'left',
				adjustByMajorUnit: true,
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
				title : '(SEC)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label: {
					y : -12,
	                rotate: {
	                    degrees: -45
	                }
	            }
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 40
				},
				xField : 'sql_id',
				yField : 'avg_buffer_gets',
				title : 'AVG_GUFFER_GETS',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('sql_id')+" : " + formatComma(value) + " sec");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
						var menuParam = "dbid="+$("#dbid").val()+"&sql_id="+item.record.get('sql_id');
						parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
			        }
			    }
			}
		}]
	});			
}

function createAvgDiskReadsChart(){
	if(avgDiskChart != "undefined" && avgDiskChart != undefined){
		avgDiskChart.destroy();
	}
	avgDiskChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("avgDiskChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			axes : [{
				type : 'numeric',
				position : 'left',
				adjustByMajorUnit: true,
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
				title : '(SEC)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label: {
					y : -12,
	                rotate: {
	                    degrees: -45
	                }
	            }
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 40
				},
				xField : 'sql_id',
				yField : 'avg_phyiscal_reads',
				title : 'AVG_DISK_READS',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('sql_id')+" : " + formatComma(value) + " sec");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
						var menuParam = "dbid="+$("#dbid").val()+"&sql_id="+item.record.get('sql_id');
						parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
			        }
			    }
			}
		}]
	});			
}

function createExecutionsChart(){
	if(executionsChart != "undefined" && executionsChart != undefined){
		executionsChart.destroy();
	}
	executionsChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("executionsChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			axes : [{
				type : 'numeric',
				position : 'left',
				adjustByMajorUnit: true,
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
				title : '(SEC)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label: {
					y : -12,
	                rotate: {
	                    degrees: -45
	                }
	            }
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 40
				},
				xField : 'sql_id',
				yField : 'executions',
				title : 'EXECUTIONS',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('sql_id')+" : " + formatComma(value) + " sec");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
						var menuParam = "dbid="+$("#dbid").val()+"&sql_id="+item.record.get('sql_id');
						parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);
			        }
			    }
			}
		}]
	});			
}

//callback 함수
var callback_TOPStatChartAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','차트 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		var chart1 = totalElapsedChart.down("chart");
		var chart2 = avgElapsedChart.down("chart");
		var chart3 = avgBufferChart.down("chart");
		var chart4 = avgDiskChart.down("chart");
		var chart5 = executionsChart.down("chart");
		
		var store1 = chart1.getStore();
		store1.loadData(data.rows);
		
		var store2 = chart2.getStore();
		store2.loadData(data.rows);
		
		var store3 = chart3.getStore();
		store3.loadData(data.rows);
		
		var store4 = chart4.getStore();
		store4.loadData(data.rows);
		
		var store5 = chart5.getStore();
		store5.loadData(data.rows);
		
		chart1.redraw();
		chart2.redraw();
		chart3.redraw();
		chart4.redraw();
		chart5.redraw();
	}	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Excel_MasterDownClick(){	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#module').textbox('getValue') == ""){
		parent.$.messager.alert('','MODULE을 입력해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	$('#module').textbox("setValue", $('#module').textbox('getValue').trim());
	$("#start_interval_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
	$("#end_interval_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));
	$("#rcount").val($('#selectRcount').combobox('getValue'));
	
	$("#submit_form").attr("action","/ModulePerformance/ExcelDown");
	$("#submit_form").submit();
}

function Excel_SlaveDownClick(){	
	if($('#subSearchYn').val() == "N"){
		parent.$.messager.alert('','마스터 테이블에서 선택된 값이 없습니다.');
		return false;
	}
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/ModulePerformance/DtlExcelDown");
	$("#submit_form").submit();
}

function cellStyler(value,row,index){
	var executions = row.executions;
	if(executions == "0"){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
	}
}


function formValidationCheck(){
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#module').textbox('getValue') == "" && $('#action').textbox('getValue') == ""){
		parent.$.messager.alert('','MODULE 또는 ACTION을 입력해 주세요.');
		return false;
	}
	return true;
}
