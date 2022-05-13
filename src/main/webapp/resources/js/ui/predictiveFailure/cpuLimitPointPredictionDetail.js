var cpuUsageChart;

Ext.EventManager.onWindowResize(function () {
	var width = $("#cpuUsageChart").width();
	
    if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
    	cpuUsageChart.setSize(width);		
	}
});	
		
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
	
	$('#selectCombo').combobox("setValue",$("#dbid").val());
	
	createCPUUsageChart();
	Btn_OnClick();	
});

function createCPUUsageChart(){
	if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
		cpuUsageChart.destroy();		
	}
	
	Ext.define("CpuLimitPointPrediction.colours", {	// Label 색상 정의
		singleton:  true,
		CPU_LIMIT: '#000',
		CPU_USAGE: '#0F4',
		CPU_TREND: '#00F',
		CPU_PREDICTION_TREND: '#F00'
	});
	
	cpuUsageChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("cpuUsageChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            docked: 'bottom',
	            marker: {
	                type: 'square'
	            },
	            border: {
	                radius: 0
	            }
	        },
	        innerPadding : '25 15 5 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
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
				position : 'bottom',
				renderer: function(v) {
					return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
				}
			}],
			series : [{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'cpu_limit',
				title : '한계사용률',		
				colors : [CpuLimitPointPrediction.colours.CPU_LIMIT],
				style : {
					//stroke: '#000',
					fill: '#000',
                    lineDash: [6, 3]
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " %");
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'cpu_usage',
				title : '사용률',
				colors : [CpuLimitPointPrediction.colours.CPU_USAGE],
				style : {
					//stroke: '#0F4',
					lineWidth: 2
				},				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " %");
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'past_cpu_usage_trend',
				title : '사용률추이',
				colors : [CpuLimitPointPrediction.colours.CPU_TREND],
				style : {
					//stroke: '#00F',
					lineWidth: 2
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " %");
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'future_cpu_usage_trend',
				title : '예측추이',
				colors : [CpuLimitPointPrediction.colours.CPU_PREDICTION_TREND],
				style : {
					//stroke: '#F00',
					lineWidth: 2,
					lineDash: [6, 3],
					strokeOpacity: .8,
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " %");
					}
				}
			}]
		}]
	});	
}

function Btn_OnClick(){
	/*if($('#selectValue').combobox('getValue') == ""){
		parent.$.messager.alert('','예측 기준을 선택해 주세요.');
		return false;
	}*/
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	$("#dbid").val($('#selectCombo').combobox("getValue"));
	$("#inst_id").val( $('#selectValue').combobox("getValue") );

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("CPU 한계점 도달 - 상세"," "); 
	
	// 차트 데이터 조회
	ajaxCall("/CPULimitPredictionChart",
			$("#submit_form"),
			"POST",
			callback_CPULimitPredictionChartAction);
}

var callback_CPULimitPredictionChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = cpuUsageChart.down("chart").getStore();	
	store.loadData(data.rows);
	cpuUsageChart.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};