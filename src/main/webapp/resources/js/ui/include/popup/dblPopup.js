var dblPopupChart;

$(document).ready(function() {
	$('#dblPopup').window({
		title : "Double Popup",
		top:getWindowTop(870),
		left:getWindowLeft(950)
	});
	
	createChart();
});

function createChart(result) {
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
	
	if(dblPopupChart != "undefined" && dblPopupChart != undefined){
		dblPopupChart.destroy();
	}
	
	Ext.define("dblPopupChart.colors", {	// Label 색상 정의
		singleton:  true,
		LAST_PROGRAM_CNT: '#5e9cd5',
		LAST_TOT_ERR_CNT: '#ec7626',
		LAST_CPLA_RATE: '#ff0000',
		PRE_CPLA_RATE: '#5f9dd6'
	});
	
	dblPopupChart = Ext.create("Ext.panel.Panel",{
//		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dblPopupChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
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
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			type:'text',
			innerPadding : '3 3 3 3', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['last_program_cnt','last_tot_err_cnt'],	// 금번회차 총본수, 금번회차 미준수본수
				minorTickSteps: 0,
				minimum: 0,
				grid: {
					odd: {
						opacity: 1,
						stroke: '#bbb',
						lineWidth: 1
					}
				},
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0
				},
				fields: ['wrkjob_cd_nm']
			},{
				type: 'numeric',
				position: 'right',
				fields: ['last_cpla_rate','pre_cpla_rate']		// 금번회차 준수율, 이전회차 준수율
			}],
			series : [{
				type : 'bar',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					'stroke-width': 2,
				},
				xField : 'wrkjob_cd_nm',
				yField : ['last_program_cnt'],
				title: '',
				colors: [dblPopupChart.colors.LAST_PROGRAM_CNT],
				label: {
					field: 'last_program_cnt',
//					display: 'insideEnd',
//					display: 'outside',
					orientation: 'horizontal'
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				},
			},{
				type : 'bar',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					'stroke-width': 2
				},
				xField : 'wrkjob_cd_nm',
				yField : 'last_tot_err_cnt',
				title: '',
				colors: [dblPopupChart.colors.LAST_TOT_ERR_CNT],
				label: {
					field: 'last_tot_err_cnt',
//					display: 'insideEnd',
					orientation: 'horizontal'
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				}
			},{
				type : 'line',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					'stroke-width': 2
				},
				xField : 'wrkjob_cd_nm',
				yField : 'last_cpla_rate',
				title: '',
				colors: [dblPopupChart.colors.LAST_CPLA_RATE],
				label: {
					field: 'last_cpla_rate',
//					display: 'insideEnd',
//					orientation: 'horizontal'
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				}
			},{
				type : 'line',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					'stroke-width': 2
				},
				xField : 'wrkjob_cd_nm',
				yField : 'pre_cpla_rate',
				title: '',
				colors: [dblPopupChart.colors.PRE_CPLA_RATE],
				label: {
					field: 'pre_cpla_rate',
//					display: 'insideEnd',
//					orientation: 'horizontal'
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				}
			}]
		}]
	});
}

function ajaxCallLoadChartStatusByWorkDbl() {
	$('#dblPopup_form #qty_chk_idt_cd').val('008');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartStatusByWork",
			$("#dblPopup_form"),
			"POST",
			callback_LoadChartStatusByWorkDbl);
}

var callback_LoadChartStatusByWorkDbl = function(result) {
	chart_callback(result, dblPopupChart);
	
	dblPopupChart.show();
};

function reloadChart() {
	//dblPopupChart.down('chart').getStore();
	dblPopupChart.down("chart").redraw();
}