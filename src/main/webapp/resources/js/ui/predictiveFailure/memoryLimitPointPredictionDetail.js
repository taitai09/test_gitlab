var memoryUsageChart;

Ext.EventManager.onWindowResize(function () {
	var width = $("#memoryUsageChart").width();
	
	if(memoryUsageChart != "undefined" && memoryUsageChart != undefined){
		memoryUsageChart.setSize(width);
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
	
	createMEMORYUsageChart();
	Btn_OnClick();
});

function createMEMORYUsageChart(){
	if(memoryUsageChart != "undefined" && memoryUsageChart != undefined){
		memoryUsageChart.destroy();
	}
	
	Ext.define("MemoryLimitPointPrediction.colours", {	// Label 색상 정의
		singleton:  true,
		PHYSICAL_MEMORY_SIZE: '#FFC001',		// 물리메모리
		MEM_LIMIT: '#000',						// 한계사이즈
		MEM_USAGE: '#23EE23',					// 사용량
		PAST_MEM_USAGE_TREND: '#1818a1',		// 사용량추이
		FUTURE_MEM_USAGE_TREND: '#ee2323'		// 예측추이
	});
	
	memoryUsageChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("memoryUsageChart"),
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
				title : '(GB)',
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
				yField : 'physical_memory_size',
				title : '물리메모리',
				colors : [MemoryLimitPointPrediction.colours.PHYSICAL_MEMORY_SIZE],
				style : {
					//stroke: '#000',
					fill: '#000',
					lineDash: [6, 3]
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(record.get(item.series.getYField()) != null) {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " GB");
						} else {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()));
						}
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'mem_limit',
				title : '한계사이즈',
				colors : [MemoryLimitPointPrediction.colours.MEM_LIMIT],
				style : {
					//stroke: '#000',
					fill: '#000',
					lineDash: [6, 3]
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(record.get(item.series.getYField()) != null) {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " GB");
						} else {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()));
						}
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'mem_usage',
				title : '사용량',
				colors : [MemoryLimitPointPrediction.colours.MEM_USAGE],
				style : {
					//stroke: '#0F4',
					lineWidth: 2
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(record.get(item.series.getYField()) != null) {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " GB");
						} else {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()));
						}
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'past_mem_usage_trend',
				title : '사용량추이',
				colors : [MemoryLimitPointPrediction.colours.PAST_MEM_USAGE_TREND],
				style : {
					//stroke: '#00F',
					lineWidth: 2,
					lineDash: [6, 3],
					strokeOpacity: .8,
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(record.get(item.series.getYField()) != null) {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " GB");
						} else {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()));
						}
					}
				}
			},{
				type : 'line',
				fill : false,
				xField : 'snap_dt',
				yField : 'future_mem_usage_trend',
				title : '예측추이',
				colors : [MemoryLimitPointPrediction.colours.FUTURE_MEM_USAGE_TREND],
				style : {
					//stroke: '#F00',
					lineWidth: 2,
					lineDash: [6, 3],
					strokeOpacity: .8,
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(record.get(item.series.getYField()) != null) {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()) + " GB");
						} else {
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()));
						}
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

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("MEMORY 한계점 도달 - 상세"," "); 
	
	// 차트 데이터 조회
	ajaxCall("/MEMORYLimitPredictionChart",
			$("#submit_form"),
			"POST",
			callback_MEMORYLimitPredictionChartAction);
}

var callback_MEMORYLimitPredictionChartAction = function(result) {
	var store;
	
	try{
		var data = JSON.parse(result);
		store = memoryUsageChart.down("chart").getStore();	
		store.loadData(data.rows);
		memoryUsageChart.down("chart").redraw();
		
	}catch(err){
		errorMessager(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};