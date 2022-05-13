var tableChart;
var tableHistoryChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#tableChart").width();

    if(tableChart != "undefined" && tableChart != undefined){
    	tableChart.setSize(width);		
	}
    if(tableHistoryChart != "undefined" && tableHistoryChart != undefined){
    	tableHistoryChart.setSize(width);		
	}
});

$(document).ready(function() {
	createDmlTableChart();
	createDmlTableHistoryChart();
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    }	    
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#table_name").val(row.table_name);
			newTabLink();
		},			
		columns:[[
			{field:'table_name',title:'TABLE_NAME',width:"13%",halign:"center",align:"center",sortable:"true"},
			{field:'base_day',title:'BASE_DAY',width:"7%",halign:"center",align:"center",formatter:getDateFormat,sortable:"true"},			
			{field:'partitioned',title:'PARTITIONED',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'partitioning_type',title:'PARTITIONING_TYPE',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'part_key_column',title:'PART_KEY_COLUMN',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'subpart_key_column',title:'SUBPART_KEY_COLUMN',width:"12%",halign:"center",align:"center",sortable:"true"},			
			{field:'allocated_space_mb',title:'ALLOCATED_SPACE_MB',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space_mb',title:'USED_SPACE_MB',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space_mb',title:'RECLAIMABLE_SPACE_MB',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space',title:'RECLAIMABLE_SPACE',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sample_size',title:'SAMPLE_SIZE',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',width:"6%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},						
			{field:'empty_blocks',title:'EMPTY_BLOCKS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_space',title:'AVG_SPACE',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'chain_cnt',title:'CHAIN_CNT',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_row_len',title:'AVG_ROW_LEN',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_space_freelist_blocks',title:'AVG_SPACE_FREELIST_BLOCKS',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'degree',title:'DEGREE',width:"6%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'comments',title:'COMMENTS',width:"40%",halign:"center",align:"left",sortable:"true"}
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
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	
	$('#tableList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("REORG 대상 분석"," "); 
	
	ajaxCall("/ReorgTargetAnalysis",
		$("#submit_form"),
		"POST",
		callback_ReorgTargetAnalysisAddTable);		

	ajaxCall("/ReorgTargetAnalysis/TOPDMLTableChart",
			$("#submit_form"),
			"POST",
			callback_TOPDMLTableChartAction);
	
	ajaxCall("/ReorgTargetAnalysis/TOPDMLTableHistoryChart",
			$("#submit_form"),
			"POST",
			callback_TOPDMLTableHistoryChartAction);		
}

//callback 함수
var callback_ReorgTargetAnalysisAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_TOPDMLTableChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = tableChart.down("chart").getStore();
	store.loadData(data.rows);
	tableChart.down("chart").redraw();
};

//callback 함수
var callback_TOPDMLTableHistoryChartAction = function(result) {
	var store;
	var data;	
	var chart = tableHistoryChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'base_day',
				yField : post.table_name,
				title : post.table_name,	
				marker: {
	                radius: 2,
	                lineWidth: 2
	            },
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            $("#table_name").val(item.series.getTitle());
						newTabLink();
			        }
			    }
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}
};

function createDmlTableChart(){
	if(tableChart != "undefined" && tableChart != undefined){
		tableChart.destroy();
	}
	tableChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("tableChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			legend : {
				type: 'sprite',
				docked : 'right'
			},
		    innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 5, // 차트 밖 여백
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
				title : '(건)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0,
					y : -25,
					rotate: {
	                    degrees: -45
	                }
				}
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 20
				},
				xField : 'table_name',
				yField : ['inserts','updates','deletes'],
				title : ['INSERTS','UPDATES','DELETES'],
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
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            $("#table_name").val("");
						newTabLink();
			        }
			    }
			}
		}]
	});			
}

function createDmlTableHistoryChart(){
	if(tableHistoryChart != "undefined" && tableHistoryChart != undefined){
		tableHistoryChart.destroy();
	}
	
	tableHistoryChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("tableHistoryChart"),
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
				title : '(건)',
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
	var menuNm = "REORG 대상 상세 분석";
	var menuUrl = "/ReorgTargetAnalysis/Detail";
	var menuParam = "dbid="+$("#dbid").val()+"&db_name="+$('#selectCombo').combobox('getText')+"&owner="+$("#owner").val()+"&table_name="+$("#table_name").val();
	
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}