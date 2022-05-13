var cpuUsageChart;
var userTimeChart;
var sysTimeChart;
var timeModelChart;

Ext.EventManager.onWindowResize(function () {
	var width1 = $("#cpuUsageChart").width();
	var width2 = $("#userTimeChart").width();
	var width3 = $("#sysTimeChart").width();
	var width4 = $("#timeModelChart").width();
	
    if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
    	cpuUsageChart.setSize(width1);		
	}
    if(userTimeChart != "undefined" && userTimeChart != undefined){
    	userTimeChart.setSize(width2);		
	}
    if(sysTimeChart != "undefined" && sysTimeChart != undefined){
    	sysTimeChart.setSize(width3);		
	}
    if(timeModelChart != "undefined" && timeModelChart != undefined){
    	timeModelChart.setSize(width4);		
	}
});	
		
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createCPUUsageChart();
	createUserTimeChart();
	createSysTimeChart();
	createTimeModelChart();
	
	$("#topSqlList").datagrid({
		view: myview,
		singleSelect:true,
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'cnt',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'activity',title:'ACTIVITY',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"80%",halign:"center",align:'left',sortable:"true"}			
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	Btn_OnClick();
});

function createCPUUsageChart(){
	if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
		cpuUsageChart.destroy();
	}

	cpuUsageChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("cpuUsageChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 10 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			legend: {
	            docked: 'bottom'
	        },
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'baseline_cpu_usage',
				title : '베이스라인 CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("베이스라인 CPU사용률[" + record.get("baseline_cpu_usage") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}
			},{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'current_cpu_usage',
				title : '기준일 CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("기준일 CPU사용률[" + record.get("current_cpu_usage") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}				
			}]
		}]
	});	
}

function createUserTimeChart(){
	if(userTimeChart != "undefined" && userTimeChart != undefined){
		userTimeChart.destroy();
	}

	userTimeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("userTimeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 10 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			legend: {
	            docked: 'bottom'
	        },			
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'baseline_user_time',
				title : '베이스라인 User CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("베이스라인 User CPU사용률[" + record.get("baseline_user_time") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}
			},{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'current_user_time',
				title : '기준일 User CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("기준일 User CPU사용률[" + record.get("current_user_time") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}				
			}]
		}]
	});	
}

function createSysTimeChart(){
	if(sysTimeChart != "undefined" && sysTimeChart != undefined){
		sysTimeChart.destroy();
	}

	sysTimeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("sysTimeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 10 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			legend: {
	            docked: 'bottom'
	        },			
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'baseline_sys_time',
				title : '베이스라인 SYS CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("베이스라인 SYS CPU사용률[" + record.get("baseline_sys_time") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}
			},{
				type : 'line',
				smooth : true,
				xField : 'snap_time',
				yField : 'current_sys_time',
				title : '기준일 SYS CPU사용률',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml("기준일 SYS CPU사용률[" + record.get("current_sys_time") + "] snap_time[" + record.get("snap_time") + "]");
					}
				}				
			}]
		}]
	});	
}


function createTimeModelChart(){
	if(timeModelChart != "undefined" && timeModelChart != undefined){
		timeModelChart.destroy();		
	}
	
	timeModelChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("timeModelChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			//flipXY : true, // 가로/세로 축 변경
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			legend : {
				docked : 'bottom'
			},
			innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
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
				title : '(Sec)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0
				}
			}],
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 10
    			},
				xField : 'stat_name',
				yField : ['baseline_time_model','current_time_model'],
				title : ['베이스라인 Time Model','기준일 Time Model'],
				stacked: false,
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get(item.field) + " Sec");
					}
				}
			},
			listeners: {
		        itemclick: function (chart, item, event) {			        	

		            var itemName = item.record.get("stat_name");
		        	
		            if(itemName == "CONNECTION MGMT"){
		        		$("#strGubun").val("01");
		        	}else if(itemName == "SEQUENCE LOAD"){
		        		$("#strGubun").val("02");
		        	}else if(itemName == "SQL EXECUTION"){
		        		$("#strGubun").val("03");
		        	}else if(itemName == "PARSE"){
		        		$("#strGubun").val("04");
		        	}else if(itemName == "PLSQL EXECUTION"){
		        		$("#strGubun").val("05");
		        	}else if(itemName == "PLSQL RPC"){
		        		$("#strGubun").val("06");
		        	}else if(itemName == "PLSQL COMPILATION"){
		        		$("#strGubun").val("07");
		        	}else if(itemName == "JAVA EXECUTION"){
		        		$("#strGubun").val("08");		        		
		        	}
		        	
		        	$("#subTitle").html("<i class='btnIcon fab fa-wpforms fa-lg fa-fw'></i> TOP SQL ( "+itemName+" )</span>");
		            
		            Search_TopSql();
		        }
		    }
		}]
	});
}

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("CPU 사용량 증가 원인 예측 - 상세"," "); 

	// CPU Usage 차트 데이터 조회
	ajaxCall("/CPUIncreaseUsageDetail/CPUUsageChart",
			$("#submit_form"),
			"POST",
			callback_CPUUsageChartAction);
	
	// User Time 차트 데이터 조회
	ajaxCall("/CPUIncreaseUsageDetail/UserTimeChart",
			$("#submit_form"),
			"POST",
			callback_UserTimeChartAction);
	
	// SYS Time 차트 데이터 조회
	ajaxCall("/CPUIncreaseUsageDetail/SysTimeChart",
			$("#submit_form"),
			"POST",
			callback_SysTimeChartAction);	
	
	// Time Model 차트 데이터 조회
	ajaxCall("/CPUIncreaseUsageDetail/TimeModelChart",
			$("#submit_form"),
			"POST",
			callback_TimeModelChartAction);
}

var callback_CPUUsageChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = cpuUsageChart.down("chart").getStore();	
	store.loadData(data.rows);
	cpuUsageChart.down("chart").redraw();
};

var callback_UserTimeChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = userTimeChart.down("chart").getStore();	
	store.loadData(data.rows);
	userTimeChart.down("chart").redraw();
};

var callback_SysTimeChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = sysTimeChart.down("chart").getStore();	
	store.loadData(data.rows);
	sysTimeChart.down("chart").redraw();
};

var callback_TimeModelChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = timeModelChart.down("chart").getStore();	
	store.loadData(data.rows);
	timeModelChart.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Search_TopSql(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("TOP SQL 조회"," "); 
	$('#topSqlList').datagrid("loadData", []);
	
	ajaxCall("/CPUIncreaseUsageDetail/TopSQL",
			$("#submit_form"),
			"POST",
			callback_TopSQLAddTableAction);
}

//callback 함수
var callback_TopSQLAddTableAction = function(result) {
	var data = JSON.parse(result);
	$('#topSqlList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};