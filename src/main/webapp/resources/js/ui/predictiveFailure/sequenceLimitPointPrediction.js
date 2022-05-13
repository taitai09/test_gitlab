var chartDrawPanel;

Ext.EventManager.onWindowResize(function () {
	var width = $("#chartDrawPanel").width();
	
    if(chartDrawPanel != "undefined" && chartDrawPanel != undefined){
    	chartDrawPanel.setSize(width);
	}
});

var dockedRightLegend;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left',
        }
        ,width:'15%'
	});	

	createChart();
});

function createChart(result){
	var comboText = $('#selectValue').combobox('getText');
	comboText = comboText.replace(" 한계점 도달","");
	
	var data;
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
	
	if(chartDrawPanel != "undefined" && chartDrawPanel != undefined){
		chartDrawPanel.destroy();
	}

	chartDrawPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("chartDrawPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
//	        captions: {
//	            title: {
//	                text: comboText,
//	                align: 'center',
//	                style: {
//	                	color: "#000000",
//	                	font: 'bold 15px Arial',
//	                	fill:"#000000"
//	                }
//	            }
//	        },			
			border : false,
			width : '100%',
			height : '100%',
			type:'text',
			text:{
			    color: "red"
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
//			legend : dockedRightLegend,
		    innerPadding : '0 30 0 10',// 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
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
				title : ''
			},{
				type : 'category',
				position : 'bottom',
				grid: false,
				label : {
					x : 0,
					y : 0
				},
                fields: ['db_name']
			}],
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 30
    			},
				xField : 'db_name',
				yField : ['cnt'],
				position:'right',
				title : [comboText],
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
						console.log(item.field +" value:"+value);
						tooltip.setHtml(sector+" : " + value + "");
						//tooltip.setHtml(item.record.get('check_pref_nm') + " : " + item.record.get('cnt') + " 건");
					}
				}
			},
			listeners: {
		        itemclick: function (chart, item, event) {


			        	var menuId = $("#menu_id").val()+1;
			        	var menuNm = "Sequence 한계점예측 상세";
			        	var menuUrl = "/SequenceLimitPointPredictionDetail";
			        	var menuParam = 
			        		"predict_standard=" + $("#selectValue").combobox('getValue') 
			        		+ "&dbid=" + item.record.get("dbid") 
			        		+ "&db_name=" + item.record.get("db_name") 
			        		+ "&nav_from_parent=Y" 
			        		;

			        	
		        	/* 탭 생성 */
		        	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
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
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Sequence 한계점예측"," "); 
	
	// 차트 데이터 조회
	ajaxCall("/SequenceLimitPointPredictionChart",
			$("#submit_form"),
			"POST",
			callback_drawChartAction);
}

var callback_drawChartAction = function(result) {
	createChart(result);
	
	/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_drawChartAction1 = function(result) {
	var title = $('#selectValue').combobox('getText');
	console.log("title:"+title);
	var store;
	var data = JSON.parse(result);
	store = chartDrawPanel.down("chart").getStore();	
	store.loadData(data.rows);
	chartDrawPanel.down("chart").redraw();
	//chartDrawPanel.down("chart").setTitle(title);
	console.log(chartDrawPanel.down("chart").getHeader());
	chartDrawPanel.down("chart").setStyle({'color':'red'});
	chartDrawPanel.down("chart").setStyle({'border-color':'red'});
	chartDrawPanel.down("chart").setTitleAlign("center");
	chartDrawPanel.down("chart").getHeader().setStyle('background-color', 'white');
	chartDrawPanel.down("chart").getHeader().setStyle('background', 'red');
	chartDrawPanel.down("chart").getHeader().setStyle({'fontColor':'red'});
	chartDrawPanel.down("chart").getHeader().setStyle({'color':'red'});
	chartDrawPanel.down("chart").getHeader().setStyle('color','red');
	chartDrawPanel.down("chart").getHeader().setStyle('border', '0px');
	 
	/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};