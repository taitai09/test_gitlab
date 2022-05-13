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
	$("body").css("visibility", "visible");
	
	createTableChart();
	createTableHistoryChart();
	
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
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'base_day',title:'분석일',halign:"center",align:"center",formatter:getDateFormat,sortable:"true"},			
			{field:'tablespace_name',title:'TABLESPACE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'num_rows',title:'현재 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'current_size_mb',title:'현재 사이즈(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'one_month_ago_size_mb',title:'1개월전 사이즈(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'three_month_ago_size_mb',title:'3개월전 사이즈(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'six_month_ago_size_mb',title:'6개월전 사이즈(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'one_month_size_increas_ratio',title:'사이즈증가율(%)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sample_size',title:'SAMPLE_SIZE',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("파티셔닝 대상 분석"," "); 
	
	
	ajaxCall("/PartitionTargetAnalysis",
		$("#submit_form"),
		"POST",
		callback_PartitionTargetAnalysisAddTable);
	
	ajaxCall("/PartitionTargetAnalysis/TOPSizeTableChart",
			$("#submit_form"),
			"POST",
			callback_TOPSizeTableChartAction);
	
	
	createTableHistoryChart(); //두번째 차트가 그려질때 에러발생하여 한번더 차트를 생성한다.

	ajaxCall("/PartitionTargetAnalysis/TOPSizeTableHistoryChart",
			$("#submit_form"),
			"POST",
			callback_TOPSizeTableHistoryChartAction);	
}

//callback 함수
var callback_PartitionTargetAnalysisAddTable = function(result) {
	json_string_callback_common(result,'#tableList',false);
};

//callback 함수
var callback_TOPSizeTableChartAction = function(result) {
	chart_callback(result, tableChart);
};

//callback 함수
var callback_TOPSizeTableHistoryChartAction = function(result) {
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
				yField : post.segment_name,
				title : post.segment_name,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("base_day")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " MB");
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
	}else{
		parent.$.messager.alert('',result.message);
	}

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function createTableChart(){
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
				title : '(MB)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0,
					y : -15,
					rotate: {
	                    degrees: -45
	                }
				}
			}],
			series : {
				type : 'bar',
				stacked : false, // stack 차트 해제
				style: {
	  			  minGapWidth: 20
				},
				xField : 'segment_name',
				yField : 'bytes',
				title : 'SIZE(MB)',
				highlight: {
	  				strokeStyle: 'black',
	    			fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('segment_name')+" : " + value + " MB");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        	var segment_name = item.record.get("segment_name");
			            $("#table_name").val(segment_name);
						newTabLink();			            
			        }
			    }
			}
		}]
	});			
}

function createTableHistoryChart(){
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
			insetPadding : 10, // 차트 밖 여백
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
			series : []
		}]
	});	
}

function newTabLink(){
	var menuId = $("#menu_id").val() + "1";
	var menuNm = "파티셔닝 대상 상세 분석";
	var menuUrl = "/PartitionTargetAnalysis/Detail";
	var menuParam = "dbid="+$("#dbid").val()+"&db_name="+$('#selectCombo').combobox('getText')+"&owner="+$("#owner").val()+"&table_name="+$("#table_name").val();
	
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}