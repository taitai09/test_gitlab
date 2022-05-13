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

var dockedRightLegend1;
var dockedRightLegend2;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	dockedRightLegend1 = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left',
        }
        ,width:'15%'
	});
	
	dockedRightLegend2 = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left',
        }
        ,width:'25%'
	});
	
	createDmlTableChart();
	createDmlTableHistoryChart();
	
	// Database 조회			
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess:function(){
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
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
					$('#selectUserName').combobox('textbox').attr("placeholder","선택");
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
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'base_day',title:'분석일',halign:"center",align:"center",formatter:getDateFormat,sortable:"true"},			
			{field:'partitioned',title:'PARTITIONED',halign:"center",align:"center",sortable:"true"},
			{field:'partitioning_type',title:'PARTITIONING_TYPE',halign:"center",align:"center",sortable:"true"},
			{field:'part_key_column',title:'PART_KEY_COLUMN',halign:"center",align:"center",sortable:"true"},
			{field:'subpart_key_column',title:'SUBPART_KEY_COLUMN',halign:"center",align:"center",sortable:"true"},			
			{field:'allocated_space_mb',title:'ALLOCATED_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space_mb',title:'USED_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space_mb',title:'RECLAIMABLE_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space',title:'RECLAIMABLE_SPACE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sample_size',title:'SAMPLE_SIZE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},						
			{field:'empty_blocks',title:'EMPTY_BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_space',title:'AVG_SPACE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'chain_cnt',title:'CHAIN_CNT',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_row_len',title:'AVG_ROW_LEN',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_space_freelist_blocks',title:'AVG_SPACE_FREELIST_BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'degree',title:'DEGREE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',halign:"center",align:'center',sortable:"true"},
			{field:'comments',title:'COMMENTS',width:'400px',halign:"center",align:"left",sortable:"true"}
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
	json_string_callback_common(result,'#tableList',true);
};

//callback 함수
var callback_TOPDMLTableChartAction = function(result) {
	chart_callback(result, tableChart);
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
		
		if(data.rows[0] == null) {
			store.loadData([]);
		} else {
			store.loadData(data.rows);
		}
		
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
};

var callback_TOPDMLTableHistoryChartAction1 = function(result) {
	createDmlTableHistoryChart(result);
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
			legend : dockedRightLegend1,
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
				radius : 20,
				grid: true,
				translationY : 30,
				label : {
					x : 0,
					y : 0,
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
				label: {
                    //field: ['table_name'],
                    display: 'insideEnd',
                    renderer: function(text, sprite, config, rendererData, index) {
                    		rd = rendererData;
							// here I would like to get fieldname ('value' or 'value2')
							var record = rendererData.store.getAt(index);
                        return text;
                    }
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
			        	var table_name = item.record.get('table_name');
			            $("#table_name").val(table_name);
						newTabLink();
			        }
			    }
			}
		}]
	});			
}

function createDmlTableHistoryChart(result){
	var data;
	
	if(result != null && result != undefined && result.result){
		try{
			data = JSON.parse(result.txtValue);
			data = data.rows;
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	
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
			legend : dockedRightLegend2,
//	        innerPadding : '25 50 20 0', // 차트안쪽 여백[top, right, bottom, left]
//			insetPadding : '20 50 20 20', // 차트 밖 여백
		    innerPadding : '10 10 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 5, // 차트 밖 여백
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
				position : 'bottom',
				label : {
//					textAlign: 'bottom'
				}
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