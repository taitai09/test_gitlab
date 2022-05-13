var cpuUsageChart;

Ext.EventManager.onWindowResize(function () {
	var width = $("#cpuUsageChart").width();
	
    if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
    	cpuUsageChart.setSize(width);
	}
});	
		
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createCPUUsageChart();
});

function createCPUUsageChart(){
	if(cpuUsageChart != "undefined" && cpuUsageChart != undefined){
		cpuUsageChart.destroy();
	}
	
	cpuUsageChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("cpuUsageChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
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
				title : '(%)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0,
					y : 0
				}
			}],
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 30
    			},
				xField : 'inst_nm',
				yField : ['baseline_cpu_usage','current_cpu_usage'],
				title : ['베이스라인 CPU사용률','기준일 CPU사용률'],
				stacked: false,
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            sector = item.series.getTitle()[fieldIndex],
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(sector+" : " + value + " %");
					}
				}
			},
			listeners: {
		        itemclick: function (chart, item, event) {			        	
							menuParam = ""
							+ "selectValue=" + $("#selectValue").combobox('getValue') 
							+ "&dbid=" + item.record.get("dbid") 
							+ "&db_name=" + item.record.get("db_name") 
							+ "&inst_id=" + item.record.get("inst_id") 
							+ "&inst_nm=" + item.record.get("inst_nm") 
							+ "&strStartDt=" + $("#strStartDt").datebox('getValue')
							+ "&searchKey=" + $("#searchKey").combobox('getValue');

		        	/* 탭 생성 */
					parent.noModifyDocSize = true;
		        	parent.openLink("Y", "1851", "CPU사용량 증가원인예측 상세", "/CPUIncreaseUsageDetail", menuParam);	
		        }
		    }
		}]
	});
}

function Btn_OnClick(){
	if($('#selectValue').combobox('getValue') == ""){
		parent.$.messager.alert('','예측 기준을 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').datebox('getValue') == ""){
		parent.$.messager.alert('','기준일을 입력해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("CPU 사용량 증가 원인 예측"," "); 
	
	// 차트 데이터 조회
	ajaxCall("/CPUIncreaseUsageChart",
			$("#submit_form"),
			"POST",
			callback_CPUIncreaseUsageChartAction);
}

var callback_CPUIncreaseUsageChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = cpuUsageChart.down("chart").getStore();
	store.loadData(data.rows);
	cpuUsageChart.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};