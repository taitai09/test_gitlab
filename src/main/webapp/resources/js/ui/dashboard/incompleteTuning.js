var objectChangeAnalysisConditionChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#objectChangeAnalysisConditionChart").width();

    if(objectChangeAnalysisConditionChart != "undefined" && objectChangeAnalysisConditionChart != undefined){
    	objectChangeAnalysisConditionChart.setSize(width);		
	}
});

var dockedRightLegend;

$(document).ready(function() {
	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left'
        }
	});
	
	createobjectChangeAnalysisConditionChart();
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
		},			
		columns:[
		[
			{field:'dbid',title:'',halign:"center",align:"center",sortable:"true",rowspan:2},
			{field:'db_name',hidden:"true"},
			{title:'Table',halign:"center",align:"center",sortable:"true",colspan:3},
			{title:'Column',halign:"center",align:'right',sortable:"true",colspan:3},
			{title:'Index',halign:"center",align:'right',sortable:"true",colspan:3}
		],[
			{field:'table_create',title:'Create',halign:"center",align:"center",sortable:"true"},
			{field:'table_modify',title:'Modify',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'table_drop',title:'Drop',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			
			{field:'column_add',title:'Create',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'column_modify',title:'Modify',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'column_drop',title:'Drop',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			
			{field:'index_create',title:'Create',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'index_modify',title:'Modify',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'index_drop',title:'Drop',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
});

function Btn_OnClick(){
	if($('#selectChangeStandard').combobox('getValue') == ""){
		parent.$.messager.alert('','변경기준을 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#change_standard").val($('#selectChangeStandard').combobox('getValue'));
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("오브젝트변경분석현황"," "); 
	
	
	ajaxCall("/DashboardV2/getObjectChangeCheckCombined",
			$("#submit_form"),
			"POST",
			callback_objectChangeAnalysisConditionChartAction);
	
	ajaxCall("/DashboardV2/getObjectChangeCheckCombined",
			$("#submit_form"),
			"POST",
			callback_TablespaceAnalysisAddTable);
}
//callback 함수
var callback_objectChangeAnalysisConditionChartAction = function(result) {
	chart_callback(result, objectChangeAnalysisConditionChart);
};

//callback 함수
var callback_TablespaceAnalysisAddTable = function(result) {
	json_string_callback_common(result,'#tableList',false);
};


function createobjectChangeAnalysisConditionChart(){
	if(objectChangeAnalysisConditionChart != "undefined" && objectChangeAnalysisConditionChart != undefined){
		objectChangeAnalysisConditionChart.destroy();
	}

	objectChangeAnalysisConditionChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("objectChangeAnalysisConditionChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '90%',
			height : '90%',
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
            legend: dockedRightLegend,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
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
			series : {
				type : 'bar',
				smooth : true,
				stacked : true,
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['table_create','table_modify','table_drop','column_add','column_modify','column_drop','index_create','index_modify','index_drop'],
				title : ['table_create','table_modify','table_drop','column_add','column_modify','column_drop','index_create','index_modify','index_drop'],
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.field + " : " + item.record.get(item.field) + " 건");
					}
				},
//				listeners: {
//			        itemclick: function (chart, item, event) {
//			        	var menuId = $("#menu_id").val()+1;
//			        	var menuNm = "긴급 조치 대상";
//			        	var menuUrl = "/DashboardV2/urgentActionTarget";
//			        	var menuParam = "dbid="+item.record.get("dbid")+"&db_name="+item.record.get('db_name');
//
//			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
//			        }
//			    }
				listeners: {
			        itemclick: function (chart, item, event) {
			        	console.log("chart:",chart);
			        	console.log("item:",item);
			        	console.log("event:",event);
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			        	console.log("fieldIndex:"+fieldIndex);

//			        	var menuId = "110";
//			        	var menuNm = "성능 개선 관리";
//			        	var menuUrl = "/ImprovementManagement";
//			        	var strCd = "";
//			        	var menuParam = "";			        	
//			        	
//			            if(fieldIndex == 0){
//			            	strCd = "2";
//			            }else if(fieldIndex == 1){
//			            	strCd = "3";
//			            }else if(fieldIndex == 2){
//			            	strCd = "5";
//			            }else if(fieldIndex == 3){
//			            	strCd = "6";
//			            }else if(fieldIndex == 4){
//			            	strCd = "7";
//			            }else if(fieldIndex == 5){
//			            	strCd = "8";
//			            }
//			            
//			            menuParam = "dbid="+item.record.get("dbid")+"&day_gubun=ALL&tuning_status_cd="+strCd;
//			            
//			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			    				
			}
		}]
	});
}

