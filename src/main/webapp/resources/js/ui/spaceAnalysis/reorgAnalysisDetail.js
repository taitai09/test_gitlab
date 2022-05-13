var dmlHistoryChart;

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    if(dmlHistoryChart != "undefined" && dmlHistoryChart != undefined){
    	dmlHistoryChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createDmlHistoryChart();
	Btn_OnClick();
});

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#segmentList').datagrid("loadData", []);
	$('#datafileList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Reorg 대상 분석 - 상세"," "); 
	
	ajaxCall("/ReorgTargetAnalysis/Detail/DMLHistoryChart",
		$("#submit_form"),
		"POST",
		callback_DMLHistoryChartAction);
}

//callback 함수
var callback_DMLHistoryChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = dmlHistoryChart.down("chart").getStore();
	store.loadData(data.rows);
	dmlHistoryChart.down("chart").redraw();

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function createDmlHistoryChart(){
	if(dmlHistoryChart != "undefined" && dmlHistoryChart != undefined){
		dmlHistoryChart.destroy();
	}
	
	dmlHistoryChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dmlHistoryChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            type: 'sprite',
	            docked: 'right'
	        },
	        innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
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
				xField : 'base_day',
				yField : 'inserts',
				title : 'INSERTS',		
				marker : {
					lineWidth: 2,
					radius : 4,
//					opacity: 0,
//	                scaling: 0.01,
//	                animation: {
//	                    duration: 200,
//	                    easing: 'easeOut'
//	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}
			},{
				type : 'line',
				xField : 'base_day',
				yField : 'updates',
				title : 'UPDATES',
				marker : {
					lineWidth: 2,
					radius : 4,
//					opacity: 0,
//	                scaling: 0.01,
//	                animation: {
//	                    duration: 200,
//	                    easing: 'easeOut'
//	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}				
			},{
				type : 'line',
				xField : 'base_day',
				yField : 'deletes',
				title : 'DELETES',
				marker : {
					lineWidth: 2,
					radius : 4,
//					opacity: 0,
//	                scaling: 0.01,
//	                animation: {
//	                    duration: 200,
//	                    easing: 'easeOut'
//	                }
				},
				highlightCfg : {
					opacity : 1,
					scaling : 1.5
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}				
			}]
		}]
	});	
}