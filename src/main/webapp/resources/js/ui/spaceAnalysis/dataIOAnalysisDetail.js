var datafileHistoryChart;

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    if(datafileHistoryChart != "undefined" && datafileHistoryChart != undefined){
    	datafileHistoryChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createDatafileHistoryChartChart();

	Btn_OnClick();
});

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터파일 I/O 분석 - 상세"," "); 
	
	ajaxCall("/DataIOAnalysis/Detail/DataFileIOHistoryChart",
			$("#submit_form"),
			"POST",
			callback_DataFileIOHistoryChartAction);	
}


//callback 함수
var callback_DataFileIOHistoryChartAction = function(result) {
	chart_callback(result, datafileHistoryChart);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function createDatafileHistoryChartChart(){	
	if(datafileHistoryChart != "undefined" && datafileHistoryChart != undefined){
		datafileHistoryChart.destroy();	
	}	
	
	datafileHistoryChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("datafileHistoryChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			legend: {
	            docked: 'right'
	        },
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
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
				xField : 'begin_interval_time',
				yField : 'phyrds',
				title : 'PHYRDS',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("begin_interval_time")+" : " + record.get("phyrds") + " CNT");
					}
				}
			},{
				type : 'line',
				xField : 'begin_interval_time',
				yField : 'phywrts',
				title : 'PHYWRTS',
				marker : {
					radius : 4,
					opacity: 0.3,
	                scaling: 0.5,
	                animation: {
	                    duration: 200,
	                    easing: 'easeOut'
	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("begin_interval_time")+" : " + record.get("phywrts") + " CNT");
					}
				}				
			},{
				type : 'line',
				xField : 'begin_interval_time',
				yField : 'wait_count',
				title : 'WAIT_COUNT',
				marker : {
					radius : 4,
					opacity: 0.3,
	                scaling: 0.5,
	                animation: {
	                    duration: 200,
	                    easing: 'easeOut'
	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("begin_interval_time")+" : " + record.get("wait_count") + " CNT");
					}
				}				
			},{
				type : 'line',
				xField : 'begin_interval_time',
				yField : 'time',
				title : 'TIME',
				marker : {
					radius : 4,
					opacity: 0.3,
	                scaling: 0.5,
	                animation: {
	                    duration: 200,
	                    easing: 'easeOut'
	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("begin_interval_time")+" : " + record.get("time") + " CNT");
					}
				}					
			}]
		}]
	});	
}