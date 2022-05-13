var dbSizeChart;
var topTablespaceChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#dbSizeChart").width();

    if(dbSizeChart != "undefined" && dbSizeChart != undefined){
    	dbSizeChart.setSize(width);		
	}
    if(topTablespaceChart != "undefined" && topTablespaceChart != undefined){
    	topTablespaceChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createDbSizeChart();
	createTopTablespaceChart();
	
	// Database 조회			
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',    
		onLoadSuccess: function() {
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#tablespace_name").val(row.tablespace_name);
			newTabLink();
		},			
		columns:[[
//			{field:'tablespace_name',title:'TABLESPACE_NAME',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'total_size_mb',title:'TOTAL_SIZE_MB',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'used_space_mb',title:'USED_SPACE_MB',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'used_percent',title:'USED_PERCENT',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'block_size',title:'BLOCK_SIZE',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'initial_extent',title:'INITIAL_EXTENT',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'next_extent',title:'NEXT_EXTENT',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'min_extents',title:'MIN_EXTENTS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'max_extents',title:'MAX_EXTENTS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'max_size',title:'MAX_SIZE',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'pct_increase',title:'PCT_INCREASE',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'min_extlen',title:'MIN_EXTLEN',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},						
//			{field:'status',title:'STATUS',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'contents',title:'CONTENTS',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'logging',title:'LOGGING',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'force_logging',title:'FORCE_LOGGING',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'extent_management',title:'EXTENT_MANAGEMENT',width:"12%",halign:"center",align:'center',sortable:"true"},
//			{field:'allocation_type',title:'ALLOCATION_TYPE',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'plugged_in',title:'PLUGGED_IN',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'segment_space_management',title:'SEGMENT_SPACE_MANAGEMENT',width:"15%",halign:"center",align:'center',sortable:"true"},
//			{field:'def_tab_compression',title:'DEF_TAB_COMPRESSION',width:"12%",halign:"center",align:'center',sortable:"true"},
//			{field:'retention',title:'RETENTION',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'bigfile',title:'BIGFILE',width:"6%",halign:"center",align:'center',sortable:"true"},
//			{field:'predicate_evaluation',title:'PREDICATE_EVALUATION',width:"12%",halign:"center",align:'center',sortable:"true"},
//			{field:'encrypted',title:'ENCRYPTED',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'cpmpress_for',title:'CPMPRESS_FOR',width:"8%",halign:"center",align:'right',sortable:"true"}
			{field:'tablespace_name',title:'TABLESPACE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'total_size_mb',title:'TOTAL_SIZE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space_mb',title:'USED_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_percent',title:'USED_PERCENT',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'block_size',title:'BLOCK_SIZE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'initial_extent',title:'INITIAL_EXTENT',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'next_extent',title:'NEXT_EXTENT',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'min_extents',title:'MIN_EXTENTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'max_extents',title:'MAX_EXTENTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'max_size',title:'MAX_SIZE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'pct_increase',title:'PCT_INCREASE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'min_extlen',title:'MIN_EXTLEN',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},						
			{field:'status',title:'STATUS',halign:"center",align:'center',sortable:"true"},
			{field:'contents',title:'CONTENTS',halign:"center",align:'center',sortable:"true"},
			{field:'logging',title:'LOGGING',halign:"center",align:'center',sortable:"true"},
			{field:'force_logging',title:'FORCE_LOGGING',halign:"center",align:'center',sortable:"true"},
			{field:'extent_management',title:'EXTENT_MANAGEMENT',halign:"center",align:'center',sortable:"true"},
			{field:'allocation_type',title:'ALLOCATION_TYPE',halign:"center",align:'center',sortable:"true"},
			{field:'plugged_in',title:'PLUGGED_IN',halign:"center",align:'center',sortable:"true"},
			{field:'segment_space_management',title:'SEGMENT_SPACE_MANAGEMENT',halign:"center",align:'center',sortable:"true"},
			{field:'def_tab_compression',title:'DEF_TAB_COMPRESSION',halign:"center",align:'center',sortable:"true"},
			{field:'retention',title:'RETENTION',halign:"center",align:'center',sortable:"true"},
			{field:'bigfile',title:'BIGFILE',halign:"center",align:'center',sortable:"true"},
			{field:'predicate_evaluation',title:'PREDICATE_EVALUATION',halign:"center",align:'center',sortable:"true"},
			{field:'encrypted',title:'ENCRYPTED',halign:"center",align:'center',sortable:"true"},
			{field:'cpmpress_for',title:'CPMPRESS_FOR',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("TABLESPACE 분석"," "); 
	
	ajaxCall("/TablespaceAnalysis",
		$("#submit_form"),
		"POST",
		callback_TablespaceAnalysisAddTable);
	
	ajaxCall("/TablespaceAnalysis/DBSizeChart",
			$("#submit_form"),
			"POST",
			callback_DBSizeChartAction);
	
	ajaxCall("/TablespaceAnalysis/TOPTablespaceChart",
			$("#submit_form"),
			"POST",
			callback_TOPTablespaceChartAction);	
}

//callback 함수
var callback_TablespaceAnalysisAddTable = function(result) {
	json_string_callback_common(result,'#tableList',false);
};

//callback 함수
var callback_DBSizeChartAction = function(result) {
	chart_callback(result, dbSizeChart);
};

//callback 함수
var callback_TOPTablespaceChartAction = function(result) {
	var store;
	var data;	
	var chart = topTablespaceChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				smooth : true,
				xField : 'base_day',
				yField : post.tablespace_name,
				title : post.tablespace_name,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " MB");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            $("#tablespace_name").val(item.series.getTitle());
						newTabLink();
			        }
			    }
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = topTablespaceChart.down("chart").getStore();
		store.loadData(data.rows);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function createDbSizeChart(){
	if(dbSizeChart != "undefined" && dbSizeChart != undefined){
		dbSizeChart.destroy();
	}

	dbSizeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dbSizeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
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
				type : 'line',
				smooth : true,
				xField : 'base_day',
				yField : 'total_size_mb',
				title : 'total_size_mb',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("base_day")+" : " + record.get("total_size_mb") + " MB");
					}
				}
			}
		}]
	});
}

function createTopTablespaceChart(){
	if(topTablespaceChart != "undefined" && topTablespaceChart != undefined){
		topTablespaceChart.destroy();
	}
	
	topTablespaceChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("topTablespaceChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
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
				title : '(MB)',
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
			series : []
		}]
	});
}

function newTabLink(){
	var menuId = $("#menu_id").val() + "1";
	var menuNm = "TABLESPACE 상세 분석";
	var menuUrl = "/TablespaceAnalysis/Detail";
	var menuParam = "dbid="+$("#dbid").val()+"&db_name="+$('#selectCombo').combobox('getText')+"&tablespace_name="+$("#tablespace_name").val();
	
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}