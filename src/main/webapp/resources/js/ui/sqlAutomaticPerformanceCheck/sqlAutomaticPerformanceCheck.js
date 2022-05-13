var checkPerformanceYn;
var checkErrorYn;
var checkPlanChangeStatus;
var chartPerfImpactPanel;
var chartElapsedTimePanel;
var chartBufferGetsPanel;
var chartPlanChangePanel;
var chartSqlTypePanel;
var timer;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createPerfImpactChart();
	createElapsedTimeChart();
	createBufferGetsChart();
	createPlanChangeChart();
	createSqlTypeChart();
	
	createList();
	
	// SQL유형 조회
	$('#selectSql').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1068&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
		}
	});
	
	// 성능 임팩트유형 조회
	$('#selectPerfImpact').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1025&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
		}
	});
	
	$("#project_id").combobox("textbox").attr("placeholder",'선택');
	
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	$('#startTime').timespinner("setValue", "00:00");
	$('#endTime').timespinner("setValue", "23:59");
	
	checkPerformanceYn = false;

	$('#performanceYnSwitch').switchbutton({
		checked: checkPerformanceYn,
		onText:"Y",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				$("#performance_yn").val("Y"); 
			}else {
				$("#performance_yn").val("N");
			}
		}
	});
	
	checkErrorYn = false;
	
	$('#errorYnSwitch').switchbutton({
		checked: checkErrorYn,
		onText:"오류",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				$("#error_yn").val("Y"); 
			}else {
				$("#error_yn").val("N");
			}
		}
	});
	
	checkPlanChangeStatus = false;
	
	$('#planChangeStatusSwitch').switchbutton({
		checked: checkPlanChangeStatus,
		onText:"변경",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				$("#plan_change_status").val("Y"); 
			}else {
				$("#plan_change_status").val("N");
			}
		}
	});
	
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
	
	$('#checkLiteralYn').checkbox({
		checked: true,
		onChange: function(checked) {
			if(checked) {
				$('#literal_except_yn').val("Y");
			} else {
				$('#literal_except_yn').val("N");
			}
		}
	});
	
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function(checked){
			if(checked) {
				$("#refresh").val("Y");
				$("#refresh_sec").textbox({disabled:false});
				
				Btn_RefreshSearch();
			}else{
				$("#refresh").val("N");
				$("#refresh_sec").textbox({disabled:true});
				
				window.clearTimeout(timer);
			}
		}
	});
	
	let topNTooltip = "값을 입력하지 않으면 전체 SQL";
	
	$('#topN_tooltip').tooltip({
		content : '<span style="color:#fff">' + topNTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});
	
	// 수행회차
	var projectId = $("#project_id").val();
	
	searchPerformanceCheckId(projectId);
	
});

function Btn_RefreshSearch(){
	var intSec = 0;
	
//	if($('#project_nm').textbox('getValue') == ""){
	if($('#project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	Btn_LoadPerformanceCheckCount();
	
	if($("#refresh").val() == "Y"){
		intSec = strParseInt($("#refresh_sec").textbox("getValue"),0);
		timer = window.setTimeout("Btn_RefreshSearch()",(intSec*1000));
	}else{
		window.clearTimeout(timer);
	}
}

function openExplainPlan() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectComboOrigin').combobox('getValue'));
	// 위치초기화
	$('#loadExplainPlanPop').window({
		top:0,
		left:52
	});
	
	$('#loadExplainPlanPop').window("open");
	
	$("#loadExplainPlan_form #bindValueList").datagrid("resize",{
		width: 400
	});
	
	console.log("plan_hash_value[" + $("#submit_form #plan_hash_value").val() + "]");
	
	loadExplainPlan($('#submit_form'));
	
	$("#loadExplainPlanPop").window('setTitle', "SQL 성능 자동 점검 (" + $("#submit_form #sql_id").val() + ")");
	$('#tabs').tabs('select', 0);
}

function createPerfImpactChart(result){
	let data;
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
	
	if(chartPerfImpactPanel != "undefined" && chartPerfImpactPanel != undefined){
		chartPerfImpactPanel.destroy();
	}
	
	Ext.define("chartPerfImpactPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50'
	});
	
	chartPerfImpactPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartPerfImpactPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_improved_title',
				yField : 'perf_impact_improved_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_01],
				label: {
					field: 'perf_impact_improved_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_regressed_title',
				yField : 'perf_impact_regressed_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_02],
				label: {
					field: 'perf_impact_regressed_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_unchanged_title',
				yField : 'perf_impact_unchanged_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_03],
				label: {
					field: 'perf_impact_unchanged_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_timeout_title',
				yField : 'perf_impact_timeout_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_04],
				label: {
					field: 'perf_impact_timeout_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createElapsedTimeChart(result){
	let data;
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
	
	if(chartElapsedTimePanel != "undefined" && chartElapsedTimePanel != undefined){
		chartElapsedTimePanel.destroy();
	}
	
	Ext.define("chartElapsedTimePanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#00af50',
		COLOR_02: '#0170c1'
	});
	
	chartElapsedTimePanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartElapsedTimePanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'before_elapsed_time_title',
				yField : 'before_elapsed_time_chart',
				colors: [chartElapsedTimePanel.colors.COLOR_01],
				label: {
					field: 'before_elapsed_time_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'after_elapsed_time_title',
				yField : 'after_elapsed_time_chart',
				colors: [chartElapsedTimePanel.colors.COLOR_02],
				label: {
					field: 'after_elapsed_time_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createBufferGetsChart(result){
	let data;
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
	
	if(chartBufferGetsPanel != "undefined" && chartBufferGetsPanel != undefined){
		chartBufferGetsPanel.destroy();
	}
	
	Ext.define("chartBufferGetsPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#00af50',
		COLOR_02: '#0170c1'
	});
	
	chartBufferGetsPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartBufferGetsPanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'before_buffer_gets_title',
				yField : 'before_buffer_gets_chart',
				colors: [chartBufferGetsPanel.colors.COLOR_01],
				label: {
					field: 'before_buffer_gets_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'after_buffer_gets_title',
				yField : 'after_buffer_gets_chart',
				colors: [chartBufferGetsPanel.colors.COLOR_02],
				label: {
					field: 'after_buffer_gets_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createPlanChangeChart(result){
	let data;
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
	
	if(chartPlanChangePanel != "undefined" && chartPlanChangePanel != undefined){
		chartPlanChangePanel.destroy();
	}
	
	Ext.define("chartPlanChangePanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#fe0000',
		COLOR_02: '#0170c1'
	});
	
	chartPlanChangePanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartPlanChangePanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'plan_change_y_title',
				yField : 'plan_change_y_chart',
				colors: [chartPlanChangePanel.colors.COLOR_01],
				label: {
					field: 'plan_change_y_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'plan_change_n_title',
				yField : 'plan_change_n_chart',
				colors: [chartPlanChangePanel.colors.COLOR_02],
				label: {
					field: 'plan_change_n_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createSqlTypeChart(result){
	let data;
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
	
	if(chartSqlTypePanel != "undefined" && chartSqlTypePanel != undefined){
		chartSqlTypePanel.destroy();
	}
	
	Ext.define("chartSqlTypePanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#0170c1',
		COLOR_03: '#00af50',
		COLOR_04: '#fe0000',
		COLOR_05: '#7030a0'
	});
	
	chartSqlTypePanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartSqlTypePanel"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'sql_command_select_title',
				yField : 'sql_command_select_chart',
				colors: [chartSqlTypePanel.colors.COLOR_01],
				label: {
					field: 'sql_command_select_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'sql_command_insert_title',
				yField : 'sql_command_insert_chart',
				colors: [chartSqlTypePanel.colors.COLOR_02],
				label: {
					field: 'sql_command_insert_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'sql_command_update_title',
				yField : 'sql_command_update_chart',
				colors: [chartSqlTypePanel.colors.COLOR_03],
				label: {
					field: 'sql_command_update_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'sql_command_delete_title',
				yField : 'sql_command_delete_chart',
				colors: [chartSqlTypePanel.colors.COLOR_04],
				label: {
					field: 'sql_command_delete_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'sql_command_merge_title',
				yField : 'sql_command_merge_chart',
				colors: [chartSqlTypePanel.colors.COLOR_05],
				label: {
					field: 'sql_command_merge_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'project_nm',title:'프로젝트명',halign:'center',align:'center',rowspan:2},
			{field:'sql_auto_perf_check_id',title:'수행회차',halign:'center',align:'center',rowspan:2},
			{field:'perf_impact_type_cd',title:'성능임팩트유형',halign:'center',align:'center',rowspan:2,hidden:true},
			{field:'sql_idfy_cond_type_nm',title:'성능임팩트유형',halign:'center',align:'center',rowspan:2},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(%)',halign:'center',align:'center',rowspan:2},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(%)',halign:'center',align:'center',rowspan:2},
			{field:'plan_change_yn',title:'Plan<br>변경여부',halign:'center',align:'center',rowspan:2},
			{field:'sql_id',title:'SQL ID',halign:'center',align:'center',rowspan:2},
			{title:'BEFORE',halign:'center',colspan:8},
			{title:'AFTER',halign:'center',colspan:8},
			{field:'sql_command_type_cd',title:'SQL 유형',halign:"center",align:'center',rowspan:2},
			{field:'err_code',title:'에러코드',halign:"center",align:'left',rowspan:2},
			{field:'err_msg',title:'에러메시지',width: '30%',halign:"center",align:'left',rowspan:2},
			{field:'sql_text',title:'SQL TEXT',halign:"center",align:'left',rowspan:2}
		],[
			{field:'before_plan_hash_value',title:'PLAN<br>HASH VALUE',halign:"center",align:'center'},
			{field:'before_executions',title:'EXECUTIONS',halign:"center",align:'right'},
			{field:'before_rows_processed',title:'ROWS<br>PROCESSED',halign:"center",align:'right'},
			{field:'before_elapsed_time',title:'ELAPSED<br>TIME',halign:"center",align:'right'},
			{field:'before_buffer_gets',title:'BUFFER<br>GETS',halign:"center",align:'right'},
			{field:'before_disk_reads',title:'DISK<br>READS',halign:"center",align:'right'},
			{field:'before_fullscan_yn',title:'FULLSCAN<br>YN',halign:"center",align:'center'},
			{field:'before_partition_all_access_yn',title:'PARTITION ALL<br>ACCESS YN',halign:"center",align:'center'},
			{field:'after_plan_hash_value',title:'PLAN<br>HASH VALUE',halign:"center",align:'center'},
			{field:'after_executions',title:'EXECUTIONS',halign:"center",align:'right'},
			{field:'after_rows_processed',title:'ROWS<br>PROCESSED',halign:"center",align:'right'},
			{field:'after_elapsed_time',title:'ELAPSED<br>TIME',halign:"center",align:'right'},
			{field:'after_buffer_gets',title:'BUFFER<br>GETS',halign:"center",align:'right'},
			{field:'after_disk_reads',title:'DISK<br>READS',halign:"center",align:'right'},
			{field:'alter_fullscan_yn',title:'FULLSCAN<br>YN',halign:"center",align:'center'},
			{field:'after_partition_all_access_yn',title:'PARTITION ALL<br>ACCESS YN',halign:"center",align:'center'}
		]],
		onSelect:function(index,row) {
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #plan_hash_value").val(row.before_plan_hash_value);
			$("#submit_form #sql_command_type_cd").val(row.sql_command_type_cd);
			
			openExplainPlan();
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}

function searchPerformanceCheckId(project_id) {
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수행회차"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		$("#perfCheckIdCombo").combobox({
			url:"/SQLAutomaticPerformanceCheck/searchPerformanceCheckId?project_id="+project_id,
			method:"get",
			valueField:'sql_auto_perf_check_id',
			textField:'sql_auto_perf_check_id',
			panelHeight: 500,
			onLoadError: function(){
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			},
			onChange: function(newValue, oldValue) {
				if(newValue == '') return;
				
				$("#sql_auto_perf_check_id").val(newValue);
				Btn_LoadPerformanceCheckCount();
				getRoundingInfo();
				Btn_OnClick();
			},
			onLoadSuccess: function(items) {
				$("#sql_auto_perf_check_id").val('');
				
				clearUI();
				
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				
				if (items.length > 0){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				} else {
					
				}
			},
		});	
	} catch(err) {
		consoloe.error(err.message);
	}
}

function getRoundingInfo() {
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("회차정보"," ");
	
	ajaxCall("/SQLAutomaticPerformanceCheck/getRoundingInfo",
			$("#submit_form"),
			"POST",
			callback_GetRoundingInfoAction);
}

var callback_GetRoundingInfoAction = function(result) {
	var data = JSON.parse(result)
	var row = data.rows[0];
	
	if(row == undefined) {
		return;
	}
	
	$('#selectComboOrigin').combobox('setValue', row.original_dbid);
	$('#targetDbName').textbox('setValue', row.perf_check_target_db_name);
	$('#perf_check_target_dbid').val(row.perf_check_target_dbid);
	$('#parsingSchemaName').textbox('setValue', row.parsing_schema_name);
	$('#parsing_schema_name').val(row.parsing_schema_name);
	var beginDt = row.perf_check_range_begin_dt.split(' ');
	var endDt = row.perf_check_range_end_dt.split(' ');
	$('#startDate').textbox('setValue', beginDt[0]);
	$('#startTime').textbox('setValue', beginDt[1].substring(0, beginDt[1].lastIndexOf(':')));
	$('#endDate').textbox('setValue', endDt[0]);
	$('#endTime').textbox('setValue', endDt[1].substring(0, endDt[1].lastIndexOf(':')));
	$('#topN').numberbox('setValue', row.topn_cnt);
	if(row.literal_except_yn != null && row.literal_except_yn == 'Y') {
		$('#checkLiteralYn').checkbox('check');
	} else {
		$('#checkLiteralYn').checkbox('uncheck');
	}
}

function loadOriginalDb(project_id) {
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("원천 DB"," ");
	
	try {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		$("#selectComboOrigin").combobox({
			url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+project_id,
			method:"get",
			valueField:'original_dbid',
			textField:'original_db_name',
			panelHeight: 500,
			onLoadError: function(){
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			},
			onChange: function(newValue, oldValue) {
				if(newValue == '') return;
				
				$("#dbid").val(newValue);
				
				var item = $.grep($(this).combobox('getData'), function(row) {
					if(row.original_dbid == newValue) {
						return true;
					} else {
						return false;
					}
				});
				
				// 대상 DB
				$('#targetDbName').textbox('setValue', item[0].perf_check_target_db_name);
				$('#perf_check_target_dbid').val(item[0].perf_check_target_dbid);
				
				// 파싱 스키마 네임
				$('#parsingSchemaName').textbox('setValue', item[0].parsing_schema_name);
			},
			onLoadSuccess: function(items) {
				/* modal progress close */
				if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				
				if (items.length > 0){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				} else {
					clearExcutionArea();
				}
			}
		});	
	} catch(err) {
		consoloe.error(err.message);
	}
}

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#project_nm").textbox("setValue", row.project_nm);
	
	$("#project_id").val(row.project_id);
	
	loadOriginalDb(row.project_id);
	
	searchPerformanceCheckId(row.project_id);
}

function clearUI() {
	$('#perfCheckIdCombo').combobox("clear");
	
	$('#performanceYnSwitch').switchbutton("reset");
	$('#errorYnSwitch').switchbutton("reset");
	$('#planStatusChangeSwitch').switchbutton("reset");
	
	$('#selectSql').combobox("reset");
	$('#selectPerfImpact').combobox("reset");
	
	$(".perf_check_result_blue").val('');
	$(".perf_check_result_green").val('');
	$(".perf_check_result_gray").val('');
	$(".perf_check_result_orange").val('');
	
	createPerfImpactChart(null);
	createElapsedTimeChart(null);
	createBufferGetsChart(null);
	createPlanChangeChart(null);
	createSqlTypeChart(null);
	
	$('#tableList').datagrid("loadData", []);
}

function clearExcutionArea() {
	$('#targetDbName').textbox('setValue', '');
	$('#parsingSchemaName').textbox('setValue', '');
	$('#startDate').textbox('setValue', $('#initStartDate').val());
	$('#endDate').textbox('setValue', $('#initNowDate').val());
	$('#startTime').textbox('setValue', "00:00");
	$('#endTime').textbox('setValue', "23:59");
	$('#topN').numberbox('clear');
	$('#checkLiteralYn').checkbox("check");
}

function Btn_LoadPerformanceCheckCount() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수행결과 조회"," ");
	
	ajaxCall("/SQLAutomaticPerformanceCheck/loadPerformanceCheckCount",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	var data = JSON.parse(rows)[0];
	
	if(typeof data == 'undefined') {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		return;
	}
	
	$(".perf_check_result_blue").val(data.total_cnt);
	$(".perf_check_result_green").val(data.completed_cnt);
	$(".perf_check_result_gray").val(data.performing_cnt);
	$(".perf_check_result_orange").val(data.err_cnt);
	
	if(data.perf_check_force_close_yn == 'N') {
		$("#perf_check_result_red").hide();
		
		if(dateFormatValidationCheck(data.perf_check_error)) {
			$("#perf_check_result_violet").val("점검 완료");
			$("#perf_check_result_violet").show();
		} else {
			$("#perf_check_result_violet").hide();
		}
	} else {
		$("#perf_check_result_violet").hide();
		
		$(".perf_check_result_red").val("강제완료: " + data.perf_check_force_close_yn);
		$("#perf_check_result_red").show();
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if(!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallTableList();
}

function Btn_OnClick(){
//	if($('#project_nm').textbox('getValue') == ""){
	if($('#project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	loadOriginalDb($("#project_id").val());
	getRoundingInfo();
	Btn_LoadPerformanceCheckCount();
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	ajaxCallChartData();
	ajaxCallTableList();
}

function ajaxCallChartData() {
	ajaxCall("/SQLAutomaticPerformanceCheck/loadChartData",
			$("#submit_form"),
			"POST",
			callback_ChartDataAction);
}

//callback 함수
var callback_ChartDataAction = function(result) {
	chart_callback(result, chartPerfImpactPanel);
	chart_callback(result, chartElapsedTimePanel);
	chart_callback(result, chartBufferGetsPanel);
	chart_callback(result, chartPlanChangePanel);
	chart_callback(result, chartSqlTypePanel);
};

function ajaxCallTableList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("자동 성능 점검 조회"," ");
	
	$('#submit_form #select_sql').val($('#selectSql').combobox('getValue'));
	$('#submit_form #select_perf_impact').val($('#selectPerfImpact').combobox('getValue'));
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/SQLAutomaticPerformanceCheck/loadPerformanceCheckList",
			$("#submit_form"),
			"POST",
			callback_TableListAction);
}

var callback_TableListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function maxPerformanceCheckId() {
	// 01. 수행회차 재조회(Max + 1)하여  Update
	var comboData = $("#perfCheckIdCombo").combobox('getData');
	comboData.push({valueField:'sql_auto_perf_check_id',textField:'sql_auto_perf_check_id',sql_auto_perf_check_id:'1'});
	$("#perfCheckIdCombo").combobox('loadData', comboData);
	comboData = $("#perfCheckIdCombo").combobox('getData');
	$("#perfCheckIdCombo").combobox('select', comboData[0].sql_auto_perf_check_id);
	$('#parsing_schema_name').val($('#parsingSchemaName').textbox('getValue'));
	
	// 02. 성능점검범위 일시를 상위로 Update
	var perf_check_range_begin_dt = $("#startDate").textbox("getValue") + " " + $("#startTime").timespinner("getValue");
	var perf_check_range_end_dt = $("#endDate").textbox("getValue") + " " + $("#endTime").timespinner("getValue");
	
	// 03. 미수행여부 초기화
	$('#performanceYnSwitch').switchbutton("reset");
	
	// 03. 오류여부 초기화
	$('#errorYnSwitch').switchbutton("reset");
	
	// 03. Plan 변경여부 초기화
	$('#planChnageStatusSwitch').switchbutton("reset");
	
	// 04. 수행결과 초기화
	initPerfCheckResult();
	
	// 05. List 초기화
	$('#tableList').datagrid("loadData", []);
	
	$("#sql_auto_perf_check_id").val($("#perfCheckIdCombo").combobox("getValue"));
	$("#perf_check_range_begin_dt").val(perf_check_range_begin_dt);
	$("#perf_check_range_end_dt").val(perf_check_range_end_dt);
	
	// 06. SQL 자동성능점검 수행
	ajaxCall("/SQLAutomaticPerformanceCheck/insertSqlAutomaticPerformanceCheck",
			$("#submit_form"),
		"POST",
		null);
}

function checkCondition() {
	if(!compareAnBDate($('#startDate').textbox('getValue'), $('#endDate').textbox('getValue'))) {
		var msg = "성능점검범위 일자를 확인해 주세요.<br>시작일자[" + $('#startDate').textbox('getValue') + "] 종료일자[" + $('#endDate').textbox('getValue') + "]";
		parent.$.messager.alert('오류',msg,'error');
		return false;
	}
	
	if(!validateHH24MMSS($("#startTime").val())) {
		var msg = "성능점검범위 시작 시간를 확인해 주세요.";
		parent.$.messager.alert('오류',msg,'error');
		return false;
	}
	
	if(!validateHH24MMSS($("#endTime").val())) {
		var msg = "성능점검범위 종료 시간를 확인해 주세요.";
		parent.$.messager.alert('오류',msg,'error');
		return false;
	}
	
	if(compareAnBDatatime($('#startDate').textbox('getValue'), $("#startTime").timespinner('getValue'), 
			$('#endDate').textbox('getValue'), $("#endTime").timespinner('getValue') ) <= 0) {
		var msg = "성능점검범위 시작 / 종료 시간을 확인해 주세요.";
		parent.$.messager.alert('오류',msg,'error');
		return false;;
	}
	
//	if($("#project_nm").val() == '') {
	if($("#project_id").val() == '') {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	if($("#sql_auto_perf_check_id").val() == '') {
		maxPerformanceCheckId();
		return false;
	}
	
	return true;
}

function Btn_SqlAutomaticPerformanceCheck() {
	if(!checkCondition()) {
		return;
	}
	
	var param = "확인";
	var msgStr = $('#targetDbName').textbox('getValue') + "(성능점검대상) DB에서 SQL성능자동점검을 수행하시겠습니까?";
	
	parent.$.messager.confirm(param,msgStr,function(r){
		if (r){
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 자동 성능 점검", "SQL 자동 성능 점검 중입니다.");
			
			$('#perf_check_range_begin_dt').val($('#startDate').textbox('getValue') + " " + $('#startTime').textbox('getValue'));
			$('#perf_check_range_end_dt').val($('#endDate').textbox('getValue') + " " + $('#endTime').textbox('getValue'));
			
			if($('#topN').numberbox('getValue') != "") {
				$('#topn_cnt').val($('#topN').numberbox('getValue'));
			}
			
			ajaxCall("/SQLAutomaticPerformanceCheck/countExecuteTms",
					$("#submit_form"),
					"POST",
					callback_CountExecuteTmsAction);
		}
	});
}

var callback_CountExecuteTmsAction = function(result) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if(result.result) {
		if(result.txtValue == 0) {
			ajaxCall("/SQLAutomaticPerformanceCheck/maxPerformanceCheckId",
					$("#submit_form"),
					"POST",
					callback_MaxPerformanceCheckIdAction);
		}
	} else {
		parent.$.messager.alert('정보','해당 프로젝트에 SQL 자동 성능 점검 수행 중입니다. [검색] 버튼을 클릭하면 수행결과를 확인 할 수 있습니다.','info');
	}
}

var callback_MaxPerformanceCheckIdAction = function(result) {
	var data = JSON.parse(result);
	
	// 01. 수행회차 재조회(Max + 1)하여  Update
	var comboData = $("#perfCheckIdCombo").combobox('getData');
	comboData.unshift({valueField:'sql_auto_perf_check_id',textField:'sql_auto_perf_check_id',sql_auto_perf_check_id:data[0].sql_auto_perf_check_id});
	$("#perfCheckIdCombo").combobox('loadData', comboData);
	
	// 02. 성능점검범위 일시를 상위로 Update
	var perf_check_range_begin_dt = $("#startDate").textbox("getValue") + " " + $("#startTime").timespinner("getValue");
	var perf_check_range_end_dt = $("#endDate").textbox("getValue") + " " + $("#endTime").timespinner("getValue");
	
	// 03. 미수행여부 초기화
	$('#performanceYnSwitch').switchbutton("reset");
	
	// 03. 오류여부 초기화
	$('#errorYnSwitch').switchbutton("reset");
	
	// 03. Plan 변경여부 초기화
	$('#planChangeStatusSwitch').switchbutton("reset");
	
	// 04. 수행결과 초기화
	initPerfCheckResult();
	
	// 05. List 초기화
	$('#tableList').datagrid("loadData", []);
	
	$("#sql_auto_perf_check_id").val($("#perfCheckIdCombo").combobox("getValue"));
	$("#perf_check_range_begin_dt").val(perf_check_range_begin_dt);
	$("#perf_check_range_end_dt").val(perf_check_range_end_dt);
	
	// 06. SQL 자동성능점검 수행
	ajaxCall("/SQLAutomaticPerformanceCheck/insertSqlAutomaticPerformanceCheck",
			$("#submit_form"),
		"POST",
		null);
}

function initPerfCheckResult() {
	$(".perf_check_result_blue").val("전체: 0");
	$(".perf_check_result_green").val("수행완료: 0");
	$(".perf_check_result_gray").val("수행중: 0");
	$(".perf_check_result_orange").val("오류: 0");
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
}

function Btn_ForceUpdateSqlAutomaticPerformanceCheck() {
	if(!checkCondition()) {
		return;
	}
	
	var param = "확인";
	var msgStr = $('#targetDbName').textbox('getValue') + "에서 수행 중인 SQL 자동성능점검 작업을 강제완료처리 하시겠습니까?";
	
	parent.$.messager.confirm(param,msgStr,function(r){
		if (r){
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 자동 성능 점검", "SQL 자동 성능 점검에 대한 강제 완료 처리 중입니다.");
			
			ajaxCall("/SQLAutomaticPerformanceCheck/forceUpdateSqlAutomaticPerformanceCheck",
					$("#submit_form"),
					"POST",
					callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
		}
	});
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if(result.result) {
		parent.$.messager.alert('정보','SQL 자동 성능 점검에 대한 강제 완료 처리가 완료되었습니다.','info');
	} else {
		parent.$.messager.alert('정보',result.message, 'info');
	}
}

function Excel_Download() {
//	if($('#project_nm').textbox('getValue') == ""){
	if($('#project_id').val() == ""){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	
	$("#submit_form").attr("action","/SQLAutomaticPerformanceCheck/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function Btn_AuthorityScript() {
	if($('#selectComboOrigin').combobox('getValue') == '') {
		parent.$.messager.alert('경고','원천 DB를 선택해 주세요.','warning');
		return false;
	}
	
	$('#authorityScript_form #db').textbox('setValue', $('#targetDbName').textbox('getValue'));
	$('#authorityScript_form #dbid').val($('#perf_check_target_dbid').val());
	
	$('#authorityScript_form #checkTable').checkbox('reset');
	$('#authorityScript_form #checkView').checkbox('reset');
	$('#authorityScript_form #checkSequence').checkbox('reset');
	$('#authorityScript_form #checkFunction').checkbox('reset');
	$('#authorityScript_form #checkProcedure').checkbox('reset');
	$('#authorityScript_form #checkPackage').checkbox('reset');
	
	$('#authorityScript_form #scriptView').val('');
	
	LoadUserName();
	
	$('#authorityScriptPop').window("open");
}