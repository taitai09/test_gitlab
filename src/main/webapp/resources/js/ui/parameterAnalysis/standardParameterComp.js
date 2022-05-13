var parameterChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#parameterChart").width();

    if(parameterChart != "undefined" && parameterChart != undefined){
    	parameterChart.setSize(width);		
	}
});

/*
datagrid 		onClickRow : function(index,row) {
treegrid 		onClickRow : function(row) {
*/
$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			var menuParam = "dbid="+row.dbid
				+"&inst_id="+row.inst_id
				+"&parameter_name="+row.parameter_name
				+ "&call_from_parent=Y";
			;
			createNewTab("183", "파라미터 변경이력", "/ParameterChangeHistory", menuParam)
		},		
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'inst_id',hidden:"true"},
			{field:'parameter_name',title:'PARAMETER_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'db_value',title:'DB_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'standard_value',title:'STANDARD_VALUE',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	createParameterChart();
	Btn_OnClick();	
});

function createParameterChart(){
	if(parameterChart != "undefined" && parameterChart != undefined){
		parameterChart.destroy();		
	}
	parameterChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("parameterChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
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
				grid: true
			}],
			series : {
				type : 'bar',
				stacked : false, // stack 차트 해제
				style: {
      			  minGapWidth: 10
    			},
				xField : 'inst_nm',
				yField : 'cnt',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("inst_nm")+" : " + record.get("cnt") + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            $("#dbid").val(item.record.get("dbid"));
			            $("#inst_id").val(item.record.get("inst_id"));

			            showDetailList();
			        }
			    }
			}
		}]
	});
}

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	//$('#tableList').treegrid("loadData", []);
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("표준 파라미터 비교"," "); 
	
	ajaxCall("/StandardParameterComp/Chart",
		$("#submit_form"),
		"POST",
		callback_ChartAction);		
}

//callback 함수
var callback_ChartAction = function(result) {
	chart_callback(result, parameterChart);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
}

function showDetailList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("표준 파라미터 비교 - 상세"," "); 
	
	ajaxCall("/StandardParameterComp/Detail",
		$("#submit_form"),
		"POST",
		callback_DetailListAction);
}

//callback 함수
var callback_DetailListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
};