var chartNewAppTimeoutPredictionPanel;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createNewAppTimeoutPredictionChart();
	
	// Work Job 조회			
	$('#selectCombo').combobox({
		url:"/Common/getWrkJob",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
				var wrkjob_cd = $("#wrkjob_cd").val();
				if(wrkjob_cd != '' && wrkjob_cd != 'null'){
					$(this).combobox('setValue', wrkjob_cd);
				}
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		fitColumns : false,
		checkOnSelect : true,
		selectOnCheck : true,
		singleSelect : false,
		onClickRow : function(index,row) {
			$("#submit_form #wrkjob_cd").val($('#selectCombo').combobox('getValue'));
			$("#submit_form #tr_cd").val(row.tr_cd);
			
			ajaxCallDrawChart();
		},
		columns:[[
			{field:'chk',halign:"center",align:'center',checkbox:'true',rowspan:2},
			{field:'except_yn',title:'예외',width:"3%",halign:"center",align:'center',sortable:"true",rowspan:2},
			{field:'tr_cd',title:'애플리케이션',width:"24%",halign:"center",align:'left',sortable:"true",rowspan:2},
			{field:'first_exec_day',title:'최초수행일',width:"6%",halign:"center",sortable:"true",align:'center',rowspan:2},
			{field:'last_exec_day',title:'최종수행일',width:"6%",halign:"center",sortable:"true",align:'center',rowspan:2},
			{field:'exec_day_cnt',title:'수행일수',width:"4%",halign:"center",sortable:"true",align:'center',rowspan:2},
			{field:'exec_cnt',title:'수행횟수',width:"4%",halign:"center",sortable:"true",align:'center',rowspan:2},
			{field:'last_prediction_day',title:'예측일',width:"6%",halign:"center",sortable:"true",align:'center',rowspan:2},
			{title:'수행시간',halign:"center",colspan:8}
			],[
				{field:'after_1_week_elapsed_time',title:'1주일후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_2_week_elapsed_time',title:'2주일후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_3_week_elapsed_time',title:'3주일후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_1_month_elapsed_time',title:'1개월후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_2_month_elapsed_time',title:'2개월후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_3_month_elapsed_time',title:'3개월후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_6_month_elapsed_time',title:'6개월후',width:"4%",halign:"center",sortable:"true",align:'right'},
				{field:'after_12_month_elapsed_time',title:'12개월후',width:"4%",halign:"center",sortable:"true",align:'right'}
			]],
		onLoadSuccess: function(data){
			var d = new Date();
			console.log("onLoadSuccess " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
			switch($('#except_yn').val()) {
			case 'Y':
				$('#btn_update_except_y').linkbutton('disable');
				$('#btn_update_except_n').linkbutton('enable');
				break;
			case 'N':
				$('#btn_update_except_y').linkbutton('enable');
				$('#btn_update_except_n').linkbutton('disable');
				break;
			default:
				$('#btn_update_except_y').linkbutton('enable');
				$('#btn_update_except_n').linkbutton('enable');
				break;
			}
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	function createNewAppTimeoutPredictionChart(result){
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
		
		if(chartNewAppTimeoutPredictionPanel != "undefined" && chartNewAppTimeoutPredictionPanel != undefined){
			chartNewAppTimeoutPredictionPanel.destroy();		
		}
		
		Ext.define("NewAppTimeoutPrediction.colours", {	// Label 색상 정의
			singleton:  true,
			APP_TIMEOUT_LIMIT: '#000',
			AVG_ELAPSED_TIME: '#0F4',
			PAST_ELAPSED_TIME_TREND: '#00F',
			FUTURE_ELAPSED_TIME_TREND: '#F00'
		});
		
		chartNewAppTimeoutPredictionPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartNewAppTimeoutPredictionPanel"),
			layout : 'fit',
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: "신규 APP 타임아웃 예측 챠트",
						align: 'center',
						style: {
							color: "#000000",
							font: 'bold 15px Arial',
							fill:"#000000"
						}
					}
				},
				plugins: {
					chartitemevents: {
						moveEvents: true
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
					fields: ['base_day']
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 2,
						lineDash: [6, 3],
					},
					xField : 'base_day',
					yField : ['app_timeout_limit'],
					title : '타임아웃시간',
					colors: [NewAppTimeoutPrediction.colours.APP_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					},
					listeners: {
						itemclick: function(chart, item, event) {
							listeners_method();
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 2
					},
					xField : 'base_day',
					yField : ['avg_elapsed_time'],
					title : '수행시간',
					colors: [NewAppTimeoutPrediction.colours.AVG_ELAPSED_TIME],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					},
					listeners: {
						itemclick: function(chart, item, event) {
							listeners_method();
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth:2
					},
					xField : 'base_day',
					yField : ['past_elapsed_time_trend'],
					title : '수행시간추이',
					colors: [NewAppTimeoutPrediction.colours.PAST_ELAPSED_TIME_TREND],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					},
					listeners: {
						itemclick: function(chart, item, event) {
							listeners_method();
						}
					}
				},{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 2,
						lineDash: [6, 3],
					},
					xField : 'base_day',
					yField : ['future_elapsed_time_trend'],
					title : '예측추이',
					colors: [NewAppTimeoutPrediction.colours.FUTURE_ELAPSED_TIME_TREND],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							tooltip.setHtml("[" + item.series.getTitle() + "] " + record.get(item.series.getXField()) + " : " + record.get(item.series.getYField()));
						}
					},
					listeners: {
						itemclick: function(chart, item, event) {
							listeners_method();
						}
					}
				}]
			}]
		});
	}
	
	var call_from_parent = $("#call_from_parent").val();
	if(call_from_parent == "Y"){
		
		$("#selectCombo").val($("#wrkjob_cd").val());
		$("#timeout_condition").val($("#temp_timeout_condition").val());
		$("#except_yn").val($("#temp_except_yn").val());
		
		$("#selectCombo").combobox('setValue',$("#wrkjob_cd").val());
		$("#timeout_condition").combobox('setValue',$("#temp_timeout_condition").val());
		$("#except_yn").combobox('setValue',$("#temp_except_yn").val());
		Btn_OnClick();
	}
	
});

function listeners_method() {
	var wrkJob = $("#wrkjob_cd").val();
	var trCd = $("#tr_cd").val();
	var menuUrl = "/APPPerformance";
	
	console.log("wrkJob:" + wrkJob + " trCd:" + trCd);
	
	createTab("애플리케이션 분석",menuUrl,trCd);
}

/* 애플리케이션 탭 생성 */
function createTab(menuNm, menuUrl, trCd){
	//var menuId = $("#menu_id").val() + gubun;
	var menuId = $("#menu_id").val();
	var menuParam = "wrkjob_cd="+$("#wrkjob_cd").val()+"&tr_cd="+trCd;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}

function Btn_OnClick(){
	console.log("Btn_OnClick");
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if(!compareAnBDate($('#start_day').textbox('getValue'), $('#end_day').textbox('getValue'))) {
		parent.$.messager.alert('','분석일을 확인해 주세요.');
		return false;
	}
	
	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));
	$("#timeout_condition").val($('#timeout_condition').combobox('getValue'));
	$("#except_yn").val($('#except_yn').combobox('getValue'));
	
	$("#start_first_exec_day").val(strReplace($("input[name='start_day']").val(),"-",""));
	$("#end_first_exec_day").val(strReplace($("input[name='end_day']").val(),"-",""));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("신규 APP 타임아웃 예측"," "); 
	
	ajaxCall("/NewAppTimeoutPredictionList",
			$("#submit_form"),
			"POST",
			callback_NewAppTimeoutPredictionAddTable);
}

//callback 함수
var callback_NewAppTimeoutPredictionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);	
}

function ajaxCallDrawChart(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("신규 APP 타임아웃 예측"," "); 
	// 차트 데이터 조회
	ajaxCall("/newAppTimeoutPredictionChartList",
			$("#submit_form"),
			"POST",
			callback_drawChartAction);
}

var callback_drawChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = chartNewAppTimeoutPredictionPanel.down("chart").getStore();
	store.loadData(data.rows);
	
	chartNewAppTimeoutPredictionPanel.down('chart').captions.title.setText($('#tr_cd').val());
	chartNewAppTimeoutPredictionPanel.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

/* 예외 등록 */
var Btn_UpdateExceptY = function() {
	var rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length <= 0){
		parent.$.messager.alert('','애플리케이션  리스트를  선택해 주세요.');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		
		if(row.except_yn == 'Y') {
			continue;
		}
		
		$('#update_form #wrkjob_cd').val($('#selectCombo').combobox('getValue'));
		$('#update_form #tr_cd').val(row.tr_cd);
		$('#update_form #except_yn').val(row.except_yn);
		
		ajaxCall("/newAppTimeoutPredictionExceptYnUpdate",
				$('#update_form'),
				"GET",
				null);
		
		if(i == rows.length - 1) {
			Btn_OnClick();
		}
	}
}

/* 예외 해제 */
var Btn_UpdateExceptN = function() {
	var rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length <= 0){
		parent.$.messager.alert('','애플리케이션  리스트를  선택해 주세요.');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		
		if(row.except_yn == 'N') {
			continue;
		}
		
		$('#update_form #wrkjob_cd').val($('#selectCombo').combobox('getValue'));
		$('#update_form #tr_cd').val(row.tr_cd);
		$('#update_form #except_yn').val(row.except_yn);
		
		ajaxCall("/newAppTimeoutPredictionExceptYnUpdate",
				$('#update_form'),
				"GET",
				null);
		
		if(i == rows.length - 1) {
			Btn_OnClick();
		}
	}
}