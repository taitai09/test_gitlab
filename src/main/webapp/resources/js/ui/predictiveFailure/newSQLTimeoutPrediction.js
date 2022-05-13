var chartNewSQLTimeoutPredictionPanel;
var vSqlId;

Ext.EventManager.onWindowResize(function () {
	var width = $("#chartNewSQLTimeoutPredictionPanel").width();
	
	if(chartNewSQLTimeoutPredictionPanel != "undefined" && chartNewSQLTimeoutPredictionPanel != undefined){
		chartNewSQLTimeoutPredictionPanel.setSize(width);
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createNewSQLTimeoutPredictionChart();
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#selectDbidCombo').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$('#sqlTabs').tabs({
		"onSelect": function(title, index) {
			if(index == 0) {
				var width = $("#chartNewSQLTimeoutPredictionPanel").width();
				
				if(chartNewSQLTimeoutPredictionPanel != "undefined" && chartNewSQLTimeoutPredictionPanel != undefined){
					chartNewSQLTimeoutPredictionPanel.setSize(width);
				}
			} else if(index == 1) {
				listeners_method();
			}
		}
	});
	
	$('#selectDbidCombo').combobox("setValue",$("#dbid").val());
	
	$("#tableList").datagrid({
		view: myview,
		fitColumns : false,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : false,
		onClickRow : function(index,row) {
			$("#submit_form #dbid").val($('#selectDbidCombo').combobox('getValue'));
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #plan_hash_value").val(row.plan_hash_value);
			
			ajaxCallDrawChart();
		},
		columns:[[
			{field:'chk',halign:"center",align:'center',checkbox:'true',rowspan:2},
			{field:'except_yn',title:'예외',width:"3%",halign:"center",align:'center',sortable:"true",rowspan:2},
			{field:'sql_id',title:'SQL_ID',width:"12%",halign:"center",align:'left',sortable:"true",rowspan:2},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',width:"12%",halign:"center",align:'left',sortable:"true",rowspan:2},
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
			switch($('#except_yn_combo').val()) {
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
			
			$("#submit_form #sql_id").val('신규 SQL 타임아웃 예측 차트');
			ajaxCallDrawChart();
			
			var title = $('#sqlTabs').find('.tabs-header').find('.tabs-title');
			$(title[1]).html('성능분석');
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	function createNewSQLTimeoutPredictionChart(result){
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
		
		if(chartNewSQLTimeoutPredictionPanel != "undefined" && chartNewSQLTimeoutPredictionPanel != undefined){
			chartNewSQLTimeoutPredictionPanel.destroy();		
		}
		
		Ext.define("NewSQLTimeoutPrediction.colours", {	// Label 색상 정의
			singleton:  true,
			SQL_TIMEOUT_LIMIT: '#000',
			AVG_ELAPSED_TIME: '#0F4',
			PAST_ELAPSED_TIME_TREND: '#00F',
			FUTURE_ELAPSED_TIME_TREND: '#F00'
		});
		
		chartNewSQLTimeoutPredictionPanel = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("chartNewSQLTimeoutPredictionPanel"),
			layout : 'fit',
			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				captions: {
					title: {
						text: "신규 SQL 타임아웃 예측 차트",
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
					fields: ['snap_dt'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : [{
					type : 'line',
					fill : false,
					style: {
						lineWidth: 2,
						lineDash: [6, 3],
					},
					xField : 'snap_dt',
					yField : ['sql_timeout_limit'],
					title : '타임아웃시간',
					colors: [NewSQLTimeoutPrediction.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
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
					yField : ['avg_elapsed_time'],
					title : '수행시간',
					colors: [NewSQLTimeoutPrediction.colours.AVG_ELAPSED_TIME],	// Label 색상 설정
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
					yField : ['past_elapsed_time_trend'],
					title : '수행시간추이',
					colors: [NewSQLTimeoutPrediction.colours.PAST_ELAPSED_TIME_TREND],	// Label 색상 설정
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
					yField : ['future_elapsed_time_trend'],
					title : '예측추이',
					colors: [NewSQLTimeoutPrediction.colours.FUTURE_ELAPSED_TIME_TREND],	// Label 색상 설정
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
	
	var call_from_parent = $("#call_from_parent").val();
	if(call_from_parent == "Y"){
		
		$("#timeout_condition").val($("#temp_timeout_condition").val());
		$("#except_yn").val($("#temp_except_yn").val());
		
		$("#timeout_condition").combobox('setValue',$("#temp_timeout_condition").val());
		$("#except_yn_combo").combobox('setValue',$("#temp_except_yn").val());
		Btn_OnClick();
	}
	
});

function listeners_method() {
	var menuId = $("#menu_id").val();
	var menuName = "SQL 성능 분석";
	var menuUrl = "/SQLPerformance";
	var dbIdCombo = $('#selectDbidCombo').combobox('getValue');
	var dbId = $('#dbid').val();
	var sql_id = $("#sql_id").val();
	var plan_hash_value = $("#plan_hash_value").val();
	
	if(dbIdCombo == "") {
		$('#sqlTabs').tabs('select', 0);
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if(dbId == "") {
		$('#sqlTabs').tabs('select', 0);
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	if(sql_id == "" || sql_id == '신규 SQL 타임아웃 예측 차트') {
		$('#sqlTabs').tabs('select', 0);
		parent.$.messager.alert('','데이터(SQL ID)를 선택해 주세요.');
		return false;
	}
	
	if(plan_hash_value == "") {
		$('#sqlTabs').tabs('select', 0);
		parent.$.messager.alert('','데이터(PLAN HASH VALUE)를 선택해 주세요.');
		return false;
	}
	
	if(vSqlId == sql_id) {
		console.log("vSqlId[" + vSqlId + '] sql_id[' + sql_id + "]");
		return;
	}
	
	/* modal progress open */
	parent.openMessageProgress("SQL 정보를 조회중입니다."," ");
	
	//openSQLInformation($("#menu_id").val(), "sqlTabs", $("#dbid").val(), sql_id, plan_hash_value);
	openSQLInformation(menuId, "sqlTabs", $("#dbid").val(), sql_id, plan_hash_value);
}

/*
 * 하위탭 SQL정보 생성 함수 - 신규 SQL 타임아웃 예측
 * reference by main.js(function createSQLLoadNewTab) 
 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 * dbId : 파라미터 1
 * sqlId : 파라미터 2
 * planHashValue : 파라미터 3
 */
function openSQLInformation(menuId, tabId, dbId, sqlId, planHashValue){
	vSqlId = sqlId;
	var objTab = parent.eval("if_"+menuId).$('#'+tabId);
	var tabName = "성능분석(" + sqlId + ")";
	var tabUrl = "/SQLInformationNSTP?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue;

	renewTab(objTab, tabId, tabName, tabUrl);
}

/* 탭 갱신 함수 */
function renewTab(objTab, tabId, tabName, tabUrl){
	objTab.tabs('select', 1);
	var tab = objTab.tabs('getSelected');
	console.log("tab... " + tabName);
	objTab.tabs('update', {
		tab: tab,
		title:tabName,
		options: {
			content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;height:99%"></iframe>'
		}
	});
	
	var title = $('#sqlTabs').find('.tabs-header').find('.tabs-title');
	$(title[1]).html(tabName);
}

function Btn_OnClick(){
	console.log("Btn_OnClick");
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if(!compareAnBDate($('#start_day').textbox('getValue'), $('#end_day').textbox('getValue'))) {
		parent.$.messager.alert('','분석일을 확인해 주세요.');
		return false;
	}
	
	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	$("#timeout_condition").val($('#timeout_condition').combobox('getValue'));
	$("#except_yn").val($('#except_yn_combo').combobox('getValue'));
	
	$("#start_first_exec_day").val(strReplace($("input[name='start_day']").val(),"-",""));
	$("#end_first_exec_day").val(strReplace($("input[name='end_day']").val(),"-",""));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("신규 SQL 타임아웃 예측"," "); 
	
	ajaxCall("/NewSQLTimeoutPredictionList",
			$("#submit_form"),
			"POST",
			callback_NewSQLTimeoutPredictionAddTable);
}

//callback 함수
var callback_NewSQLTimeoutPredictionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);	
}

function ajaxCallDrawChart(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("신규 SQL 타임아웃 예측"," "); 
	
	$('#sqlTabs').tabs('select',0);
	
	// 차트 데이터 조회
	ajaxCall("/newSQLTimeoutPredictionChartList",
			$("#submit_form"),
			"POST",
			callback_drawChartAction);
}

var callback_drawChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = chartNewSQLTimeoutPredictionPanel.down("chart").getStore();
	store.loadData(data.rows);
	
	chartNewSQLTimeoutPredictionPanel.down('chart').captions.title.setText($('#sql_id').val());
	chartNewSQLTimeoutPredictionPanel.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var changeTabTitle  = function() {
	/*var tab = $('#sqlTabs').tabs('getTab', 1);	// #1. Not running
	$('#sqlTabs').tabs('update', {
		tab: tab,
		options: {
			title: "성능추이" + "AB"
		}
	});
	
	var tab = $('#sqlTabs').tabs('getTab', 1);	// #2. Not running
	tab.panel('setTitle', '성능추이' + 'DF');
	
	$('#sqlTabs').panel({	// #3. Not running
		title: "Title"
	});
	$('#sqlTabs').panel("refresh");*/
	
	var titles = $('#sqlTabs').find('.tabs-header').find('.tabs-title');
	$(titles[0]).html('성능추이(' + $('#sql_id').val() + ')');
}

/* 예외 등록 */
var Btn_UpdateExceptY = function() {
	var rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length <= 0){
		parent.$.messager.alert('','예외 등록할 SQL 리스트를 체크해 주세요.');
		return false;
	}
	
	var dbid = $('#selectDbidCombo').combobox('getValue');
	
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		
		if(row.except_yn == 'Y') {
			continue;
		}
		
		$('#update_form #dbid').val($('#selectDbidCombo').combobox('getValue'));
		$('#update_form #sql_id').val(row.sql_id);
		$('#update_form #except_yn').val(row.except_yn);
		
		ajaxCall("/newSQLTimeoutPredictionExceptYnUpdate",
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
		parent.$.messager.alert('','예외 해제할 SQL 리스트를 체크해 주세요.');
		return false;
	}
	
	var dbid = $('#selectDbidCombo').combobox('getValue');
	
	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		
		if(row.except_yn == 'N') {
			continue;
		}
		
		$('#update_form #dbid').val($('#selectDbidCombo').combobox('getValue'));
		$('#update_form #sql_id').val(row.sql_id);
		$('#update_form #except_yn').val(row.except_yn);
		
		ajaxCall("/newSQLTimeoutPredictionExceptYnUpdate",
				$('#update_form'),
				"GET",
				null);
		
		if(i == rows.length - 1) {
			Btn_OnClick();
		}
	}
}