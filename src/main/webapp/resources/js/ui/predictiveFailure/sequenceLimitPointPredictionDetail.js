var chartDrawPanel1;
var chartDrawPanel2;

Ext.EventManager.onWindowResize(function () {
	var width1 = $("#chartDrawPanel1").width();
	var width2 = $("#chartDrawPanel2").width();
	
    if(chartDrawPanel1 != "undefined" && chartDrawPanel1 != undefined){
    	chartDrawPanel1.setSize(width1);
	}
    if(chartDrawPanel2 != "undefined" && chartDrawPanel2 != undefined){
    	chartDrawPanel2.setSize(width2);
	}
});

var dockedRightLegend;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var nav_from_parent = $("#nav_from_parent").val();
	console.log("nav_from_parent:"+nav_from_parent);
	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left',
        }
        ,width:'15%'
	});	

	
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
		onLoadSuccess: function(items) {
			parent.$.messager.progress('close');
			if (items.length){
				var opts = $(this).combobox('options');
				//$(this).combobox('select', items[0][opts.valueField]);
				console.log("dbid:"+$("#dbid").val());
				$(this).combobox('setValue', $("#dbid").val());
			}
		}
	});	
	
	createTable();	
	createChart1();
	createChart2();
	if(nav_from_parent == "Y"){
		$("#selectValue").combobox('setValue',$("#predict_standard").val());
		ajaxCallTableList();
	}
});

function createTable(){
	$("#tableList").datagrid({
		view: myview,
		nowrap : false,
		singleSelect : true,
		onClickRow : function(index,row) {
			$("#prediction_dt").val(row.prediction_dt);
			$("#dbid").val(row.dbid);
			$("#sequence_owner").val(row.sequence_owner);
			$("#sequence_name").val(row.sequence_name);
			ajaxCallDrawChart();
		},			
		columns:[[
			{field:'prediction_dt',hidden:'true'},
			{field:'sequence_owner',hidden:'true'},
			{field:'prediction_dt',hidden:'true'},
			{field:'dbid',hidden:'true'},
			{field:'sequence_name',title:'SEQUENCE',halign:"center",align:'left',sortable:"true"},
			{field:'sequence_max_value',title:'MAX VALUE',halign:"center",align:'right',sortable:"true"},
			{field:'before_3_month_sequence_ratio',title:'3개월전 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'before_2_month_sequence_ratio',title:'2개월전 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'before_1_month_sequence_ratio',title:'1개월전 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'current_sequence_ratio',title:'현재 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'after_1_month_sequence_ratio',title:'1개월후 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'after_2_month_sequence_ratio',title:'2개월후 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'after_3_month_sequence_ratio',title:'3개월후 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'after_6_month_sequence_ratio',title:'6개월후 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"},
			{field:'after_12_month_sequence_ratio',title:'12개월후 SEQ사용율(%)',halign:"center",align:'right',sortable:"true"}
		]],
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
}

function createChart1(result){
	
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
	
	if(chartDrawPanel1 != "undefined" && chartDrawPanel1 != undefined){
		chartDrawPanel1.destroy();
	}
	
	Ext.define("SequenceLimitPredictionDetail1.colours", {	// Label 색상 정의
		singleton:  true,
		THRESHOLD_USAGE: '#000',
		LAST_NUMBER_USAGE: '#0F4',
		PAST_SEQUENCE_USAGE_TREND: '#00F',
		FUTURE_SEQUENCE_USAGE_TREND: '#F00'
	});

	chartDrawPanel1 = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartDrawPanel1"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			captions: {
				title: {
					text: "사용률 예측",
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 15px Arial',
						fill:"#000000"
					}
				}
			},
			legend: {
				docked: 'bottom',
				marker: {
					type: 'square'
				},
				border: {
					radius: 0
				}
			},
			type:'text',
			innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
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
				fields: ['snap_dt']
			}],
			series : [{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2,
					lineDash: [6, 3]
				},
				xField : 'snap_dt',
				yField : ['sequence_threshold_usage'],
				title : '한계사용률',
				colors: [SequenceLimitPredictionDetail1.colours.THRESHOLD_USAGE],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2
				},
				xField : 'snap_dt',
				yField : ['sequence_last_number_usage'],
				title : '사용률',
				colors: [SequenceLimitPredictionDetail1.colours.LAST_NUMBER_USAGE],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2
				},
				xField : 'snap_dt',
				yField : ['past_sequence_usage_trend'],
				title : '사용률추이',
				colors: [SequenceLimitPredictionDetail1.colours.PAST_SEQUENCE_USAGE_TREND],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2,
					lineDash: [6, 3]
				},
				xField : 'snap_dt',
				yField : ['future_sequence_usage_trend'],
				title : '예측추이',
				colors: [SequenceLimitPredictionDetail1.colours.FUTURE_SEQUENCE_USAGE_TREND],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			}]
		}]
	});

}

function createChart2(result){
	
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
	
	if(chartDrawPanel2 != "undefined" && chartDrawPanel2 != undefined){
		chartDrawPanel2.destroy();
	}
	
	Ext.define("SequenceLimitPredictionDetail2.colours", {	// Label 색상 정의
		singleton:  true,
		THRESHOLD_VALUE: '#000',
		LAST_NUMBER: '#0F4',
		PAST_SEQUENCE_VALUE_TREND: '#00F',
		FUTURE_SEQUENCE_VALUE_TREND: '#F00'
	});

	chartDrawPanel2 = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartDrawPanel2"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			captions: {
				title: {
					text: "사용값 예측",
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 15px Arial',
						fill:"#000000"
					}
				}
			},
			legend: {
				docked: 'bottom',
				marker: {
					type: 'square'
				},
				border: {
					radius: 0
				}
			},
			type:'text',
			innerPadding : '0 15 0 15',// 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
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
				fields: ['snap_dt']
			}],
			series : [{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2,
					lineDash: [6, 3],
				},
				xField : 'snap_dt',
				yField : ['sequence_threshold_value'],
				title : '한계사용값',
				colors: [SequenceLimitPredictionDetail2.colours.THRESHOLD_VALUE],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2
				},
				xField : 'snap_dt',
				yField : ['sequence_last_number'],
				title : '사용값',
				colors: [SequenceLimitPredictionDetail2.colours.LAST_NUMBER],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth:2
				},
				xField : 'snap_dt',
				yField : ['past_sequence_value_trend'],
				title : '사용값추이',
				colors: [SequenceLimitPredictionDetail2.colours.PAST_SEQUENCE_VALUE_TREND],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			},{
				type : 'line',
				fill : false,
				style: {
					lineWidth: 2,
					lineDash: [6, 3],
				},
				xField : 'snap_dt',
				yField : ['future_sequence_value_trend'],
				title : '예측추이',
				colors: [SequenceLimitPredictionDetail2.colours.FUTURE_SEQUENCE_VALUE_TREND],	// Label 색상 설정
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
					}
				}
			}]
		}]
	});

}

function ajaxCallTableList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Sequence 한계점예측"," "); 
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/SequenceLimitPointArrivalList",
			$("#submit_form"),
			"POST",
			callback_TableListAction);	
}

/* Tablespace 한계점 예측 - 상세 리스트 */
//callback 함수
var callback_TableListAction = function(result) {
	json_string_callback_common(result, '#tableList', true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Btn_OnClick(){
	$('#tableList').datagrid("loadData", []);
	
	if($('#selectCombo').combobox('getValue') == "") {
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	ajaxCallTableList();
	
	if($('#selectValue').combobox('getValue') == ""){
		parent.$.messager.alert('','예측 기준을 선택해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	createChart1();
	createChart2();

}

function ajaxCallDrawChart(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Sequence 한계점예측"," "); 
	// 차트 데이터 조회
	ajaxCall("/sequenceLimitPointPredictionDetailChartList",
			$("#submit_form"),
			"POST",
			callback_drawChartAction);
}

var callback_drawChartActionOldVersion = function(result) {
	createChart1(result);
	createChart2(result);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_drawChartAction = function(result) {
	callback_redrawChart1(result);
	callback_redrawChart2(result);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_redrawChart1 = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = chartDrawPanel1.down("chart").getStore();
	store.loadData(data.rows);
	
	chartDrawPanel1.down("chart").redraw();
}

var callback_redrawChart2 = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = chartDrawPanel2.down("chart").getStore();
	store.loadData(data.rows);
	
	chartDrawPanel2.down("chart").redraw();
}
