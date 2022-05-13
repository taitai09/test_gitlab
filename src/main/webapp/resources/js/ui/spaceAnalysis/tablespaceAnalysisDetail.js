var segmentStatChart;
var sizeHistoryChart;
var datafileChart;

Ext.EventManager.onWindowResize(function () {
    var width1 = $("#segmentStatChart").width();
    var width2 = $("#datafileChart").width();

    if(segmentStatChart != "undefined" && segmentStatChart != undefined){
    	segmentStatChart.setSize(width1);		
	}
    if(sizeHistoryChart != "undefined" && sizeHistoryChart != undefined){
    	sizeHistoryChart.setSize(width1);		
	}    
    if(datafileChart != "undefined" && datafileChart != undefined){
    	datafileChart.setSize(width2);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
	    	
	    	$('#selectUserName').combobox("setValue", $("#owner").val());
	    }	    
	});
	
	$('#selectCombo').combobox("setValue", $("#dbid").val());
	
	$("#segmentList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#owner").val(row.owner);
			$("#segment_name").val(row.segment_name);
			$("#partition_name").val(row.partition_name);
			$("#recommendations").val(row.recommendations);
			$("#c1").val(row.c1);
			$("#c2").val(row.c2);
			$("#c3").val(row.c3);
			
			setRecommend();
			makeSegmentChart();
		},			
		columns:[[
			{field:'owner',title:'OWNER',halign:"center",align:"left",sortable:"true"},
			{field:'segment_name',title:'SEGMENT_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'segment_type',title:'SEGMENT_TYPE',halign:"center",align:'left',sortable:"true"},
			{field:'segment_subtype',title:'SEGMENT_SUBTYPE',halign:"center",align:'center',sortable:"true"},			
			{field:'bytes',title:'SIZE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},			
			{field:'allocated_space_mb',title:'ALLOCATED_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space_mb',title:'USED_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space_mb',title:'RECLAIMABLE_SPACE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'chain_rowexcess',title:'CHAIN_ROWEXCESS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'recommendations',title:'RECOMMENDATIONS',hidden:"true"},			
			{field:'c1',title:'C1',hidden:"true"},
			{field:'c2',title:'C2',hidden:"true"},
			{field:'c3',title:'C3',hidden:"true"}
		]],
		rowStyler: function(index,row){
			if(row.recommendations != '' && row.recommendations != null){
				return 'background-color:#739dd7;color:#fff;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#datafileList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#file_name").val(row.file_name);
			makeDataFileChart();
		},			
		columns:[[
			{field:'file_name',title:'FILE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'file_id',title:'FILE_ID',halign:"center",align:'center',sortable:"true"},
			{field:'bytes',title:'SIZE_MB',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},			
			{field:'status',title:'STATUS',halign:"center",align:'center',sortable:"true"},
			{field:'user_bytes',title:'USER_BYTES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},			
			{field:'user_blocks',title:'USER_BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'online_status',title:'ONLINE_STATUS',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
	
	$("#segmentTab").tabs({
		plain: true
	});
	
	$("#datafileTab").tabs({
		plain: true
	});
	
	$("#recommendationDiv").hide();
	
	Btn_OnClick();
});

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#segmentList').datagrid("loadData", []);
	$('#datafileList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("TABLESPACE 분석 - 상세"," "); 
	
	ajaxCall("/TablespaceAnalysis/Detail/Segment",
		$("#submit_form"),
		"POST",
		callback_SegmentAddTable);
	
	ajaxCall("/TablespaceAnalysis/Detail/DataFile",
			$("#submit_form"),
			"POST",
			callback_DataFileAddTable);	
}

//callback 함수
var callback_SegmentAddTable = function(result) {
	json_string_callback_common(result,'#segmentList',true);
};

//callback 함수
var callback_DataFileAddTable = function(result) {
	json_string_callback_common(result,'#datafileList',false);
};

function makeSegmentChart(){
	$('#segmentTab').tabs('select', 1);
	
	if(segmentStatChart != "undefined" && segmentStatChart != undefined){
		segmentStatChart.destroy();	
	}	
	
	segmentStatChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("segmentStatChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			legend: {
	            type: 'sprite',
	            docked: 'right'
	        },
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
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
			series : [{
				type : 'line',
				xField : 'snap_dt',
				yField : 'phyrds',
				title : 'phyrds',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("snap_dt")+" : " + record.get("phyrds") + " CNT");
					}
				}
			},{
				type : 'line',
				xField : 'snap_dt',
				yField : 'phywrts',
				title : 'phywrts',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("snap_dt")+" : " + record.get("phywrts") + " CNT");
					}
				}
			}]
		}]
	});	
	
	if(sizeHistoryChart != "undefined" && sizeHistoryChart != undefined){
		sizeHistoryChart.destroy();	
	}
	
	sizeHistoryChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("sizeHistoryChart"),
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
			series : [{
				type : 'line',
				smooth : true,
				xField : 'base_day',
				yField : 'bytes',
				title : 'size_mb',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("base_day")+" : " + record.get("bytes") + " MB");
					}
				}
			}]
		}]
	});

	ajaxCall("/TablespaceAnalysis/Detail/SegmentStatistics",
			$("#submit_form"),
			"POST",
			callback_SegmentStatisticsAction);
	
	ajaxCall("/TablespaceAnalysis/Detail/SegmentSizeHistory",
			$("#submit_form"),
			"POST",
			callback_SegmentSizeHistoryAction);	
}

//callback 함수
var callback_SegmentStatisticsAction = function(result) {
	chart_callback(result, segmentStatChart);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

//callback 함수
var callback_SegmentSizeHistoryAction = function(result) {
	chart_callback(result, sizeHistoryChart);
};

function makeDataFileChart(){
	$('#datafileTab').tabs('select', 1);	
	if(datafileChart != "undefined" && datafileChart != undefined){
		datafileChart.destroy();
	}
	
	datafileChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("datafileChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			legend: {
	            type: 'sprite',
	            docked: 'right'
	        },
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
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
			series : [{
				type : 'line',
				smooth : true,
				xField : 'snap_dt',
				yField : 'phyrds',
				title : 'phyrds',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("snap_dt")+" : " + record.get("phyrds") + " CNT");
					}
				}
			},{
				type : 'line',
				smooth : true,
				xField : 'snap_dt',
				yField : 'phywrts',
				title : 'phywrts',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("snap_dt")+" : " + record.get("phywrts") + " CNT");
					}
				}
			}]
		}]
	});
	
	ajaxCall("/TablespaceAnalysis/Detail/DatafileStatistics",
			$("#submit_form"),
			"POST",
			callback_DatafileStatisticsAction);	
}

//callback 함수
var callback_DatafileStatisticsAction = function(result) {
	var store;
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','차트 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		store = datafileChart.down("chart").getStore();
		store.loadData(data.rows);
	}
};

function setRecommend(){
	var strHtml = "";
	
	if($("#recommendations").val() == ""){
		$("#recommendationDiv").hide();
	}else{
		$("#recommendationDiv").show();
		
		$("#recommendTbl tbody tr").remove();
		
		strHtml += "<tr><th>Recommendations</th><td>" + $("#recommendations").val() + "<td/></tr>";
		strHtml += "<tr><th>Run First</th><td>" + $("#c1").val() + "<td/></tr>";
		strHtml += "<tr><th>Run Second</th><td>" + $("#c2").val() + "<td/></tr>";
		strHtml += "<tr><th>Run Third</th><td>" + $("#c3").val() + "<td/></tr>";

		$("#recommendTbl tbody").append(strHtml);
	}
}