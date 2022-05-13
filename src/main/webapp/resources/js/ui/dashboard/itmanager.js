var performanceChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#performanceChart").width();

    if(performanceChart != "undefined" && performanceChart != undefined){
    	performanceChart.setSize(width);		
	}
});

$(document).ready(function() {
	createPerformanceChart();
	OnSearch();
});

function createPerformanceChart(){
	if(performanceChart != "undefined" && performanceChart != undefined){
		performanceChart.destroy();
	}
	performanceChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("performanceChart"),
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
				docked : 'right'
			},
		    innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
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
				title : '(건수)'
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
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['process_1','process_2','process_3'],
				title : ['미완료(미지연)','미완료(지연)','완료'],
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

						tooltip.setHtml(sector+" : " + value + " 건");
					}
				}
			},
			listeners: {
		        itemclick: function (chart, item, event) {
		        	console.log("event.type:",event.type);
		        	console.log("event:",event);
		        	console.log("item:",item);
		        	console.log("item.field:",item.field);
							menuParam = 
							"tuningStatus=" + item.field
							+ "&dbid=" + item.record.get("dbid") 
							+ "&db_name=" + item.record.get("db_name") 
							;

		        	/* 탭 생성 */
		        	parent.openLink("Y", "110", "성능 개선 관리", "/ImprovementManagement", menuParam);	
		        }
		    }
		}]
	});
}

function OnSearch(){
	/* 성능 개선 진행 현황 */
	ajaxCall("/Dashboard/ImprovementProgress",
			$("#submit_form"),
			"POST",
			callback_ImprovementProgressAction);
	
		
	/* 성능개선 3개월 누적 실적 현황 차트 */
	ajaxCall("/Dashboard/ImprovementPerformanceChart",
			$("#submit_form"),
			"POST",
			callback_ImprovementPerformanceChartAction);
}

//callback 함수
var callback_ImprovementProgressAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#databaseInfo").html("");
		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			if(strParseInt(post.process_1,0) == 0 && strParseInt(post.process_2,0) == 0 && strParseInt(post.process_3,0) == 0 && strParseInt(post.process_4,0) == 0){
				strHtml += "<div class='database_none_back'>";
			}else{
				strHtml += "<div class='database_back'>";				
			}
			
			strHtml += "<div class='dba_name_txt'>"+post.dba_name+"</div>";
			strHtml += "<div class='dba_cnt_txt'>"+formatComma(post.process_1)+" 건</div>";
			strHtml += "<div class='dba_cnt_txt'>"+formatComma(post.process_2)+" 건</div>";
			strHtml += "<div class='dba_cnt_txt'>"+formatComma(post.process_3)+" 건</div>";
			strHtml += "<div class='dba_cnt_txt'>"+formatComma(post.process_4)+" 건</div>";
			strHtml += "<div class='db_name_txt'>"+post.db_name+"</div>";
			strHtml += "</div>";
		}
		
		$("#databaseInfo").html(strHtml);
	}else{
		parent.$.messager.alert('error','성능 개선 진행 현황 조회에 실패하였습니다.');
	}
};

//callback 함수
var callback_ImprovementPerformanceChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = performanceChart.down("chart").getStore();
	store.loadData(data.rows);
	performanceChart.down("chart").redraw();
};