var tableSizeHistoryChart;

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    if(tableSizeHistoryChart != "undefined" && tableSizeHistoryChart != undefined){
    	tableSizeHistoryChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createTableSizeHistoryChart();

	$("#accessPathList").datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'access_path',title:'ACCESS_PATH',width:"70%",halign:"center",align:"left",sortable:"true"},
			{field:'access_path_count',title:'ACCESS_PATH_COUNT',width:"20%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#recommendList").datagrid({
		view: myview,
		columns:[[
			{field:'column_name',title:'COLUMN_NAME',width:"40%",halign:"center",align:"left",sortable:"true"},
			{field:'usage_cnt',title:'USAGE_CNT',width:"30%",halign:"center",align:'right',sortable:"true"},
			{field:'partition_key_recommend_rank',title:'PARTITION_KEY_RECOMMEND_RANK',width:"30%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	

	Btn_OnClick();
});

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#accessPathList').datagrid("loadData", []);
	$('#recommendList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("파티션 대상 분석 - 상세"," ");
	

	ajaxCall("/PartitionTargetAnalysis/Detail/TableSizeHistoryChart",
			$("#submit_form"),
			"POST",
			callback_TableSizeHistoryChartAction);
	
	ajaxCall("/PartitionTargetAnalysis/Detail/AccessPath",
		$("#submit_form"),
		"POST",
		callback_AccessPathAddTable);
	
	ajaxCall("/PartitionTargetAnalysis/Detail/PartitionRecommendation",
			$("#submit_form"),
			"POST",
			callback_PartitionRecommendationAddTable);	
}

//callback 함수
var callback_AccessPathAddTable = function(result) {
	json_string_callback_common(result,'#accessPathList',true);
	$("#accessPathList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_PartitionRecommendationAddTable = function(result) {
	if(result.result){
		var post = result.object;
		/*$("#rowCount").html("<b>총 " + post.usage_cnt + " 건</b>");*/
		
		$(".panel-title").eq(2).append("<span class='h3' style='margin-left:0px;'> (총 "+post.usage_cnt+"건)</span>");
		var data = JSON.parse(result.txtValue);
		$('#recommendList').datagrid("loadData", data);
		$("#recommendList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}else{
		if(result.message != '') {
			parent.$.messager.alert('',result.message);
		}
	}
};

function createTableSizeHistoryChart(){	
	if(tableSizeHistoryChart != "undefined" && tableSizeHistoryChart != undefined){
		tableSizeHistoryChart.destroy();	
	}	
	
	tableSizeHistoryChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("tableSizeHistoryChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minorTickSteps: 0,
        		minimum: 0,
				title : '(MB)',
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
				yField : 'bytes',
				title : 'SIZE(MB)',
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
						tooltip.setHtml(record.get("base_day")+" : " + record.get("bytes") + " MB");
					}
				}
			}]
		}]
	});	
}

//callback 함수
var callback_TableSizeHistoryChartAction = function(result) {
	chart_callback(result, tableSizeHistoryChart);
};