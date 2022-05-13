var checkPerformanceYn;
var checkErrorYn;
var checkPlanChangeStatus;
var chartPerfImpactPanel;
var chartElapsedTimePanel;
var chartBufferGetsPanel;
var chartPlanChangePanel;
var chartPerfFitPanel;
var timer;
var cnt;
var totCnt;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(550),
		left:getWindowLeft(550)
	});
	
	$("#tuningAssignPop").window("close");
	createPerfImpactChart();
	createElapsedTimeChart();
	createBufferGetsChart();
	createPlanChangeChart();
	createPerfFitChart();
	
	createList();
	
	// 프로젝트 조회
	$('#submit_form #project_id_cd').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
//			$("#submit_form #sql_auto_perf_check_id").val("");
			// submit_form SQL점검팩 콤보
			$('#submit_form #sqlPerformanceP').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id,
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onSelect: function(item) {
//					$("#submit_form #sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
					$("#submit_form #data_yn").val(item.data_yn);
					
				},
				onShowPanel: function() {
					let sqlAutoPerfId = $('#submit_form #sqlPerformanceP').combobox("getValue");
					
					$('#submit_form #sqlPerformanceP').combobox({
						url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id,
						method:"post",
						valueField:'sql_auto_perf_check_id',
						textField:'perf_check_name',
						panelHeight: 300,
						onLoadSuccess: function() {
							$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
							
							if ( sqlAutoPerfId != '' && sqlAutoPerfId != null ) {
								$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfId );
							} else {
								$("#submit_form #sqlPerformanceP").combobox("setValue", "" );
							}
						}
					});
					
					$(".textbox").removeClass("textbox-focused");
					$(".textbox-text").removeClass("tooltip-f");
				},
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onLoadSuccess: function() {
					$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id_cd").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	// SELECT 문 > 전체 check
	$("#all_sql_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true && 
						$("#perf_down_yn").checkbox("options").checked != true && 
						$("#notPerf_yn").checkbox("options").checked != true && 
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > plan변경 check
	$("#plan_change_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				if ( $("#perf_down_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 성능저하 check
	$("#perf_down_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 성능 부적합 check
	$("#notPerf_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#perf_down_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 오류 check
	$("#error_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#error_yn").checkbox("check");
				
				$("#error_dml_yn").checkbox("check");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#perf_down_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
				
				$("#error_dml_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			}
		}
	});
	
	// dml문 check
	$("#dml_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false && 
						$("#error_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#fullScan_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#partition_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked ) {
					$("#error_dml_yn").checkbox("check");
				} else if ( $("#error_yn").checkbox("options").checked ) {
					$("#error_dml_yn").checkbox("check");
				} else {
					$("#all_dml_yn").checkbox("uncheck");
					$("#fullScan_yn").checkbox("uncheck");
					$("#partition_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
					$("#dml_yn").checkbox("uncheck");
				}
			} else {
				$("#all_dml_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#error_dml_yn").checkbox("uncheck");
				$("#dml_yn").checkbox("uncheck");
			}
		}
	});

	// DML 문 > 전체 check
	$("#all_dml_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#dml_yn").checkbox("check");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#error_dml_yn").checkbox("uncheck");
			} else {
				if ( $("#fullScan_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#dml_yn").checkbox("uncheck");
				}
			}
		}
	});

	// DML 문 > FULL SCAN check
	$("#fullScan_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#dml_yn").checkbox("check");
				
				if ( $("#all_dml_yn").checkbox("options").checked ||
						$("#error_dml_yn").checkbox("options").checked ) {
					$("#all_dml_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
				}
			} else {
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#all_dml_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
			}
		}
	});

	// DML 문 > PARTITION ALL ACCESS check
	$("#partition_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#dml_yn").checkbox("check");
				
				if ( $("#all_dml_yn").checkbox("options").checked ||
						$("#error_dml_yn").checkbox("options").checked ){
					$("#all_dml_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
				}
			} else {
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#all_dml_yn").checkbox("options").checked == false &&
						$("#fullScan_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
			}
		}
	});
	
	// DML 문 > 오류 check
	$("#error_dml_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#dml_yn").checkbox("check");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#all_dml_yn").checkbox("uncheck");
				
				$("#error_yn").checkbox("check");
			} else {
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#fullScan_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#all_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
				
				if ( $("#dml_yn").checkbox("options").checked ) {
					$("#error_yn").checkbox("uncheck");
				}
			}
		}
	});
	
	$("#sql_profile_yn").checkbox({
		value:"Y",
		checked: false
	});
	
	// 이전튜닝대상 선정 SQL 제외 check
	$('#chkExcept').switchbutton({
		value:"Y",
		checked:true,
		onText:"Yes",
		offText:"No",
		onChange:function( checked ) {
			if ( checked ) {
				$("#submit_form #sqlExclude").val("Y");
			} else {
				$("#submit_form #sqlExclude").val("");
			}
		}
	});
	
	$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	$("#asis_elapsed_time").numberbox("textbox").attr("placeholder","3");
	$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	$("#elapsed_time_regres").numberbox("textbox").attr("placeholder","10");
	
	$("#buffer_gets_1day").textbox("textbox").focus(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_1day").textbox("textbox").blur(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	});
	
	$("#asis_elapsed_time").textbox("textbox").focus(function(){
		$("#asis_elapsed_time").textbox("textbox").attr("placeholder","");
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	$("#asis_elapsed_time").textbox("textbox").blur(function(){
		$("#asis_elapsed_time").textbox("textbox").attr("placeholder","3");
		
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	$(".asis_elapsed_time .textbox").keyup(function() {
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	$("#buffer_gets_regres").textbox("textbox").focus(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_regres").textbox("textbox").blur(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	});
	
	$("#elapsed_time_regres").textbox("textbox").focus(function(){
		$("#elapsed_time_regres").textbox("textbox").attr("placeholder","");
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	$("#elapsed_time_regres").textbox("textbox").blur(function(){
		$("#elapsed_time_regres").textbox("textbox").attr("placeholder","10");
		
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	$(".elapsed_time_regres .textbox").keyup(function() {
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	
	initPerfCheckResult();
	
	$('#tableList').datagrid("loadData", []);
});

function createPerfImpactChart(result){
	let data;
	
	if ( result != null && result != undefined ) {
		try {
			data = JSON.parse(result);
			data = data.rows;
		} catch(e) {
			parent.$.messager.alert('경고', e.message ,'warning');
		}
	} else {
		data = {};
	}
	
	if ( chartPerfImpactPanel != "undefined" && chartPerfImpactPanel != undefined ) {
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
	if ( result != null && result != undefined ) {
		try {
			data = JSON.parse(result);
			data = data.rows;
		} catch(e) {
			parent.$.messager.alert('경고', e.message ,'warning');
		}
	} else {
		data = {};
	}
	
	if ( chartElapsedTimePanel != "undefined" && chartElapsedTimePanel != undefined ) {
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
	if ( result != null && result != undefined ) {
		try {
			data = JSON.parse(result);
			data = data.rows;
		} catch(e) {
			parent.$.messager.alert('경고', e.message ,'warning');
		}
	} else {
		data = {};
	}
	
	if ( chartBufferGetsPanel != "undefined" && chartBufferGetsPanel != undefined ) {
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
	if ( result != null && result != undefined ) {
		try {
			data = JSON.parse(result);
			data = data.rows;
		} catch(e) {
			parent.$.messager.alert('경고', e.message ,'warning');
		}
	} else {
		data = {};
	}
	
	if ( chartPlanChangePanel != "undefined" && chartPlanChangePanel != undefined ) {
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

function createPerfFitChart(result){
	let data;
	if ( result != null && result != undefined ) {
		try {
			data = JSON.parse(result);
			data = data.rows;
		} catch(e) {
			parent.$.messager.alert('경고', e.message ,'warning');
		}
	}else{
		data = {};
	}
	
	if ( chartPerfFitPanel != "undefined" && chartPerfFitPanel != undefined ) {
		chartPerfFitPanel.destroy();
	}
	
	Ext.define("chartPerfFitPanel.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: '#fe0000',
		COLOR_02: '#00af50',
		COLOR_03: '#01b0f1',
		COLOR_04: '#0170c1',
		COLOR_05: '#7030a0'
	});
	
	chartPerfFitPanel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartPerfFitPanel"),
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
				xField : 'perf_chk_indc_n_title',
				yField : 'perf_chk_indc_n_chart',
				colors: [chartPerfFitPanel.colors.COLOR_01],
				label: {
					field: 'perf_chk_indc_n_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_chk_indc_y_title',
				yField : 'perf_chk_indc_y_chart',
				colors: [chartPerfFitPanel.colors.COLOR_02],
				label: {
					field: 'perf_chk_indc_y_chart',
					display: 'insideEnd',
					orientation: 'horizontal'
				}
			}]
		}]
	});
}

function createList() {
	cnt = 0;
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',width:'3%',halign:'center',align:'center'},
			{field:'perf_impact_type_nm',title:'성능임팩트<br>유형',width:'4%',halign:'center',align:'center'},
			{field:'buffer_increase_ratio',title:'버퍼<br> 임팩트(배)',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',styler:cellStyler},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'3%',halign:'center',align:'center'},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left'},
			
			{field:'asis_plan_hash_value',title:'ASIS<br>PLAN<br>HASH VALUE',width:'5%',halign:"center",align:'right'},
			{field:'tobe_plan_hash_value',title:'TOBE<br>PLAN<br>HASH VALUE',width:'5%',halign:"center",align:'right'},
			{field:'asis_executions',title:'ASIS<br>EXECUTIONS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_executions',title:'TOBE<br>EXECUTIONS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_rows_processed',title:'ASIS<br>ROWS<br>PROCESSED',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_rows_processed',title:'TOBE<br>ROWS<br>PROCESSED',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_elapsed_time',title:'ASIS<br>ELAPSED<br>TIME',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_elapsed_time',title:'TOBE<br>ELAPSED<br>TIME',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_buffer_gets',title:'ASIS<br>BUFFER<br>GETS',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_buffer_gets',title:'TOBE<br>BUFFER<br>GETS',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_fullscan_yn',title:'ASIS<br>FULLSCAN<br>YN',width:'4%',halign:"center",align:'center'},
			{field:'tobe_fullscan_yn',title:'TOBE<br>FULLSCAN<br>YN',width:'4%',halign:"center",align:'center'},
			{field:'asis_partition_all_access_yn',title:'ASIS<br>PARTITION ALL<br>ACCESS YN',width:'5%',halign:"center",align:'center'},
			{field:'tobe_partition_all_access_yn',title:'TOBE<br>PARTITION ALL<br>ACCESS YN',width:'5%',halign:"center",align:'center'},
			
			{field:'sql_command_type_cd',title:'SQL 명령 유형',width:'5%',halign:"center",align:'center'},
			{field:'err_code',title:'에러코드',width:'5%',halign:"center",align:'left'},
			{field:'err_msg',title:'에러메시지',width: '10%',halign:"center",align:'left'},
			{field:'sql_text_web',title:'SQL TEXT',width: '15%',halign:"center",align:'left'},
			{field:'tuning_no',title:'튜닝번호',width:'4%',halign:"center",align:'right'},
			{field:'sql_profile_nm',title:'PROFILE NAME',width:'12%',halign:"center",align:'left'},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left'},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left'},
			{field:'perf_check_target_dbid',title:'perf_check_target_dbid',hidden:'true'},
			{field:'perf_check_sql_source_type_cd',title:'perf_check_sql_source_type_cd',hidden:'true'}
		]],
		onSelect:function( index, row ) {
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #dbid").val(row.original_dbid);
			$("#submit_form #plan_hash_value").val(row.asis_plan_hash_value);
			$("#submit_form #asis_plan_hash_value").val(row.tobe_plan_hash_value);
			$("#submit_form #sql_command_type_cd").val(row.sql_command_type_cd);
			$("#submit_form #perf_check_sql_source_type_cd").val(row.perf_check_sql_source_type_cd);
			
//			$('#loadExplainPlan_form [name = asis_plan_hash_value]').val( row.asis_plan_hash_value );
			
			//plan 비교 기능을 위해 데이터 세팅
			$('#loadExplainPlan_form [name = dbid]').val(row.original_dbid);
			$('#loadExplainPlan_form [name = sql_id]').val(row.sql_id);
			$('#loadExplainPlan_form [name = plan_hash_value]').val(row.asis_plan_hash_value);
			$('#loadExplainPlan_form [name = sql_command_type_cd]').val(row.sql_command_type_cd);
			$('#loadExplainPlan_form [name = perf_check_sql_source_type_cd]').val( $('#submit_form #perf_check_sql_source_type_cd').val() );
			
			openExplainPlan(row.plan_change_yn);
		},
		onCheck:function(index, rows) {
			cnt++;
			$("#sqlProfileAppPop #sqlIdPop").html( rows.sql_id );
			$("#sqlProfileAppPop #planHashValuePop").html( rows.asis_plan_hash_value );
			$("#sqlProfileAppPop #profilePop").html( "OP_"+rows.sql_id+"_"+rows.asis_plan_hash_value );
		},
		onUncheck:function(index, rows) {
			if ( $("#profile_form #isAll").val() == "A" ) {
				cnt = 20;
			}
			$("#assign_form #isAll").val("");
			$("#profile_form #isAll").val("");
			cnt--;
		},
		onCheckAll:function( rows ) {
			$("#assign_form #isAll").val("A");
			$("#profile_form #isAll").val("A");
			cnt = totCnt;
			$("#sqlProfileAppPop #sqlIdPop").html( rows[0].sql_id );
			$("#sqlProfileAppPop #planHashValuePop").html( rows[0].asis_plan_hash_value );
			$("#sqlProfileAppPop #profilePop").html( "OP_"+rows[0].sql_id+"_"+rows[0].asis_plan_hash_value );
		},
		onUncheckAll:function( rows ) { 
			$("#assign_form #isAll").val("");
			$("#profile_form #isAll").val("");
			cnt = 0;
			$("#sqlProfileAppPop #sqlIdPop").html( "" );
			$("#sqlProfileAppPop #planHashValuePop").html( "" );
			$("#sqlProfileAppPop #profilePop").html( "" );
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function cellStyler( value,row,index ) {
	// 적합
	if ( row.perf_check_result_yn == '적합' ) {
		return 'background-color:#1A9F55;color:white;';
	} else if ( row.perf_check_result_yn == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	} else { // 부적합
		return '';
	}
}

function checkCondition() {
	if ( $("#submit_form #project_id_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sqlPerformanceP" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	return true;
}

// 페이지처리
function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallTableList();
}

function openExplainPlan(plan_change_yn) {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	
	// Plan이 변경되었을 경우에만 Plan 비교 버튼 보이기
	if(plan_change_yn == 'Y'){
		$('#planCompare').css('visibility','visible');
		
	}else {
		$('#planCompare').css('visibility','hidden');
	}
	
	$("#tabs").tabs('update',{
		tab: $("#tabs").tabs('getTab',1),
		options:{
			title:'ASIS Plan'
		}
	});
	$("#tabs").tabs('update',{
		tab: $("#tabs").tabs('getTab',2),
		options:{
			title:'TOBE Plan'
		}
	});
	$(".tabs-last:not(.tabs-first)").hide();//('display','none');
	
	$("#bindValueList").datagrid({
		view: myview,
		fitColumns:true,
		columns:[[
			{field:'bind_var_nm',title:'BIND_VAR_NM',halign:"center",align:'center'},
			{field:'bind_var_value',title:'BIND_VAR_VALUE',halign:"center",align:'left'},
			{field:'bind_var_type',title:'BIND_VAR_TYPE',halign:"center",align:'right'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
//	$("#bindValueList").datagrid({
//		view: myview,
//		fitColumns:true,
//		columns:[[
//			{field:'no',title:'NO',halign:"center",width:10,align:'right'},
//			{field:'bind_var_nm',title:'NAME',halign:"center",width:20,align:'left'},
//			{field:'bind_var_value',title:'VALUE',halign:"center",width:20,align:'left'},
//			{field:'plan_hash_value',title:'PLAN HASH<br>VALUE',halign:"center",width:18,align:'right'},
//			{field:'elapsed_time',title:'ELAPSED<br>TIME',halign:"center",width:18,align:'right'},
//			{field:'buffer_gets',title:'BUFFER<br>GETS',halign:"center",width:15,align:'right'},
//			{field:'rows_processed',title:'ROWS<br>PROCESSED',halign:"center",width:15,align:'right'},
//			{field:'planCompare',title:'플랜비교',halign:"center",width:15,align:'center'}
//		]],
//		onLoadSuccess:function(data) {
//			// datagrid 셀 병합.
//			let cnt = 1;
//			let idx = 0;
//			for( let listIdx=1; listIdx <= data.rows.length; listIdx++ ) {
//				
//				if ( listIdx == data.rows.length ) {
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'no',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'plan_hash_value',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'elapsed_time',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'buffer_gets',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'rows_processed',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'planCompare',
//						rowspan: cnt,
//					});
//				} else if ( data.rows[idx].no == data.rows[listIdx].no ) {
//					cnt ++;
//				} else {
//					
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'no',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'plan_hash_value',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'elapsed_time',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'buffer_gets',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'rows_processed',
//						rowspan: cnt
//					});
//					$('#bindValueList').datagrid('mergeCells',{
//						index:idx,
//						field:'planCompare',
//						rowspan: cnt
//					});
//					cnt = 1;
//					idx = listIdx;
//				}
//			}
//		},
//		onLoadError:function() {
//			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
//		}
//	});
	
	$("#operPlanCopy").linkbutton({
		text:"TOBE PLAN 복사"
	});
	
	$("#loadExplainPlanPop").window({
		top:0,
		left:50
	});
	
	$('#loadExplainPlanPop').window("open");
	
	$("#loadExplainPlan_form #bindValueList").datagrid("resize",{
		width: 400
	});
	
	console.log("plan_hash_value[" + $("#submit_form #plan_hash_value").val() + "]");
	
	loadExplainPlanNew( $('#submit_form') );
	
	$("#loadExplainPlanPop").window('setTitle', "SQL Info( " + $("#submit_form #sql_id").val()+ " )");
	
	$('#tabs').tabs('select', 0);
}

function loadExplainPlanNew( submit_form ) {
	$("#textArea").val('');
	$("#asisTextPlan").val('');
	$("#operTextPlan").val('');
	
	/* 상단 텍스트 박스 */
	let code = $("#submit_form #perf_check_sql_source_type_cd").val();
	
	if ( code == "1" ) {
		// AWR
		ajaxCall("/SQLInformation/SQLText", submit_form, "POST", callback_SQLTextAction);
		
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlan", submit_form, "POST", callback_SQLPerformTextAsisPlanListAction );
	} else {
		// VSQL
		ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainSqlText", submit_form, "POST", callback_SQLTextAction);
		
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlanAll", submit_form, "POST", callback_SQLPerformTextAsisPlanListAction );
	}
	
	$('#bindValueList').datagrid('loadData',[]);
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadExplainBindValueNew",submit_form, "POST", callback_BindValueListNewAction);
//	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadExplainInfoBindValue",submit_form, "POST", callback_BindValueListNewAction);
	
	/* Tobe Plan */
	$("#plan_hash_value").val( $("#asis_plan_hash_value").val() );
	if ( $("#sql_command_type_cd").val() == "SELECT" ) {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadAfterSelectTextPlanListAll", $("#submit_form"), "POST", callback_SQLPerformTextOperPlanListAction );
	} else {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadAfterDMLTextPlanListAll", $("#submit_form"), "POST", callback_SQLPerformTextOperPlanListAction );
	}
	
}

// callback
var callback_BindValueListNewAction = function(result) {
	var data = null;
	try{
		data = JSON.parse(result);
	
		if ( data.result != undefined && !data.result ) {
			var opts = $("#bindValueList").datagrid('options');
			var vc = $("#bindValueList").datagrid('getPanel').children('div.datagrid-view');
			vc.children('div.datagrid-empty').remove();
			if ( !$("#bindValueList").datagrid('getRows').length ) {
				var emptyMsg = "검색된 데이터가 없습니다.";
				var d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
				d.css({
					top:50
				});
				$('#bindValueList').datagrid('loaded');
			}
		} else {
			$("#bindValueList").datagrid({rownumbers:true});
			$('#bindValueList').datagrid("loadData", data);
			$('#bindValueList').datagrid('loaded');
			$("#bindValueList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
			$("#bindValueIsNull").val("Y");  //bindValueList 에 데이터가 없을경우	
			
			if ( data.totalCount > 0 ) {
				$("#bindValueIsNull").val("N"); //bindValueList 에 데이터가 있을경우	
				$("#bindBtn").linkbutton({disabled:false});	
			}
		}
	
	} catch(data) {
		$('#bindValueList').datagrid('loaded');
	}
}

function Btn_compare( plan_hash_value ) {
	let planHashValue = $('#loadExplainPlan_form [name = plan_hash_value]').val();
	$('#loadExplainPlan_form [name = plan_hash_value]').val( plan_hash_value );
	
	ajaxCall("/Common/getAsisPlanCompareResult"
				,$('#loadExplainPlan_form')
				,"GET"
				,callback_planCompare);
	$('#loadExplainPlan_form [name = plan_hash_value]').val( planHashValue );
}

function Btn_SqlAutoPerfSearch( num ) {
	if ( !checkCondition() ) {
		return;
	}
	cnt = 0;
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	$("#checkAll").val("");
	$("#sqlExclude").val("");
	$("#assign_form #isAll").val("");
	$("#profile_form #isAll").val("");
	$('#assign_form #sqlIdArry').val("");
	
	$("#assign_form #project_id").combobox("readonly", false);
	$("#assign_form #sqlPerformanceP").combobox("readonly", false);
	
	$('#tableList').datagrid("loadData", []);
	
	// SQLInfo 페이지 적용할 project_id
	if ( num == 1 ) {
		$("#submit_form #project_id").val( $("#submit_form #project_id_cd").combobox("getValue") );
		$("#submit_form #sql_auto_perf_check_id").val( $("#submit_form #sqlPerformanceP").combobox("getValue") );
	}
	// plan 비교 기능을 위해 데이터 세팅
	$('#loadExplainPlan_form [name = project_id]').val( $('#submit_form #project_id_cd').combobox('getValue') );
	$('#loadExplainPlan_form [name = sql_auto_perf_check_id]').val( $('#submit_form #sqlPerformanceP').combobox('getValue') );
	
	//수행결과조회
	Btn_LoadPerfResultCount();
	//chart조회
	ajaxCallChartData();
	//검색결과건수 조회
	loadPerformanceResultCount();
	//grid조회
	ajaxCallTableList();
}

// chart 조회
function ajaxCallChartData() {
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerfChartData",
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
	chart_callback(result, chartPerfFitPanel);
};

function ajaxCallTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능비교 결과 조회"," ");
	
	$('#submit_form #select_sql').val("00");
	$('#submit_form #select_perf_impact').val("00");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerformanceResultList",
			$("#submit_form"),
			"POST",
			callback_LoadPerfResultListAction);
}

var callback_LoadPerfResultListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

//수행결과 초기화
function initPerfCheckResult() {
	$(".perf_check_result_blue").val("전체: 0");
	$(".perf_check_result_green").val("수행완료: 0");
	$(".perf_check_result_orange").val("오류: 0");
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
}

//수행 결과 조회
function Btn_LoadPerfResultCount() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("수행결과 조회"," ");
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerfResultCount",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	var data = JSON.parse(rows)[0];
	
	if ( typeof data == 'undefined') {
		/* modal progress close */
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		return;
	}
	
	$(".perf_check_result_blue").val(data.total_cnt);
	$(".perf_check_result_green").val(data.completed_cnt);
	$(".perf_check_result_orange").val(data.err_cnt);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

// 엑셀 다운
function Excel_Download() {
	if ( !checkCondition() ) {
		return;
	}
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$('#submit_form #select_sql').val("00");
	$('#submit_form #select_perf_impact').val("00");
	
	$("#submit_form").attr("action","/AutoPerformanceCompareBetweenDatabase/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

// 검색결과 건수
function loadPerformanceResultCount() {
	if ( !checkCondition() ) {
		return;
	}
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/getPerformanceResultCount",
			$("#submit_form"),
			"POST",
			callback_loadPerformanceResultCountAction);
}

var callback_loadPerformanceResultCountAction = function( result ) {
	totCnt = result.txtValue;
	$(".performanceResultCount").val("검색결과 건수 : "+result.txtValue);
}
function initPopupSet(){
	// popup 초기화
	$('#assign_form #chkAutoShare').switchbutton("uncheck");
	$('#assign_form #chkExcept').switchbutton("check");
	$('#assign_form #auto_share').val("N");
	$('#assign_form #selectTuner').combobox({readonly:false});
	$('#assign_form #selectTuner').combobox('setValue','');
	$("#assign_form #project_id").combobox('setValue','');
	$("#assign_form #sqlPerformanceP").combobox('setValue','');
	
};

// 튜닝대상선정 popup
function showTuningReqPopup(){
	if ( !checkCondition() ) {
		return;
	}
	
	$('#assign_form #project_id').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// submit_form SQL점검팩 콤보
			$('#assign_form #sqlPerformanceP').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id,
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onSelect: function(item) {
//					$("#submit_form #sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
					$("#submit_form #data_yn").val(item.data_yn);
					
				},
				onLoadSuccess: function() {
//					$("#assign_form #sqlPerformanceP").combobox("setValue", $("#submit_form #sqlPerformanceP").combobox("getValue") );
					$("#assign_form #sqlPerformanceP").combobox("setValue", $("#submit_form #sql_auto_perf_check_id").val() );
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
		},
		onLoadSuccess: function() {
			$("#assign_form #project_id").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	var tuningNoArry = "";
	var rows = $('#tableList').datagrid('getChecked');
	
	initPopupSet();
	
	$("#assign_form #project_id").combobox( "setValue", $("#submit_form #project_id").val() );
	$("#assign_form #sqlPerformanceP").combobox( "setValue", $("#submit_form #sql_auto_perf_check_id").val() );
	$("#assign_form #project_id").combobox("readonly","true");
	$("#assign_form #sqlPerformanceP").combobox("readonly","true");
	
	if ( rows.length > 0 ) {
		$("#dbid").val( rows[0].original_dbid );
		
		$('#tuningAssignPop').window({
			top:getWindowTop(550),
			left:getWindowLeft(550)
		});
		
		$('#tuningAssignPop').window("open");
		$('#assign_form #dbid').val("");
		$("#assign_form #sql_auto_perf_check_id").val("");
		
		$('#assign_form #tuningNoArry').val("");
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("M");
		
		const CHOICE_DIV_CD = "G";
		const TUNING_STATUS_CD = "3";
		const PARSING_SCHEMA_NAME = "OPENSIMUL";
		
		$('#assign_form #dbid').val($('#dbid').val());
		$("#assign_form #choice_div_cd").val( CHOICE_DIV_CD );
		$("#assign_form #tuning_status_cd").val( TUNING_STATUS_CD );
		$("#assign_form #parsing_schema_name").val( PARSING_SCHEMA_NAME );
		$("#submit_form #choice_cnt").val(rows.length);
		
		// 튜닝 담당자 조회
		$('#assign_form #selectTuner').combobox({
			url:"/Common/getTuner?dbid="+$("#dbid").val(),
			method:"get",
			valueField:'tuner_id',
			textField:'tuner_nm',
			onLoadSuccess:function() {
				$("#assign_form #selectTuner").combobox("textbox").attr("placeholder",'선택');
			}
		});		
	} else {
		parent.$.messager.alert('경고','튜닝 대상을 선택해 주세요.','warning');
	}
}

// SQL Profile POPUP OPEN
function showSqlProfilePopup( type ) {
	if ( !checkCondition() ) {
		return;
	}
	
	let rows = $('#tableList').datagrid('getChecked');
	let strName = "";
	let btnName = "";
	
	if ( type == 1 ) {
		strName = "SQL Profile 적용";
		btnName = "적용";
	} else if ( type == 2 ) {
		strName = "SQL Profile 삭제";
		btnName = "삭제";
	} else {
		strName = "SQL Profile 이관";
		btnName = "이관";
	}
	
	if ( rows.length > 0 ) {
		let equalCnt = 0;
		
		for ( var dataIdx = 0; dataIdx < rows.length; dataIdx++ ) {
			if ( rows[dataIdx].sql_profile_nm != null && rows[dataIdx].sql_profile_nm != '' ) {
				equalCnt ++;
			} else {
				equalCnt --;
			}
		}
		
		if ( type == 1 ) {
			$(".transBtn").hide();
			
			$('#sqlProfileAppPop #transTargetDB').combobox("setValue","");
			$('#sqlProfileAppPop #sqlProfileType').val("1");
		} else if ( type == 2 && equalCnt == rows.length ) {
			$(".transBtn").hide();
			
			$('#sqlProfileAppPop #transTargetDB').combobox("setValue","");
			$('#sqlProfileAppPop #sqlProfileType').val("2");
		} else if ( type == 3 && equalCnt == rows.length ){
			$(".transBtn").show();
			
			$('#sqlProfileAppPop #transTargetDB').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getOperationDB",
				method:"post",
				valueField:'dbid',
				textField:'db_name',
				panelHeight: 300,
				onSelect: function(item) {
					$("#submit_form #transfer_dbid").val( item.dbid );
				},
				onLoadSuccess: function() {
					$("#sqlProfileAppPop #transTargetDB").combobox("textbox").attr("placeholder",'선택');
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
			
			$('#sqlProfileAppPop #sqlProfileType').val("3");
			equalCnt = 0;
		} else {
			parent.$.messager.alert('경고','SQL Profile적용된 대상을 선택해 주세요.','warning');
			return false;
		}
		
		$('#sqlProfileAppPop').window({
			top:getWindowTop(550)
			,left:getWindowLeft(550)
			,width:500
			,height:250
			,title: strName
		});
		
		if ( cnt > 1 ) {
			$("#sqlProfileAppPop #showHide").show();
		} else {
			$("#sqlProfileAppPop #showHide").hide();
		}
		
		$("#sqlProfileAppPop #Btn_profile").html( btnName );
		$("#sqlProfileAppPop #profileCnt").html( cnt );
		$('#sqlProfileAppPop').window("open");
	
	} else {
		parent.$.messager.alert('경고','SQL Profile을 '+ btnName +'할 대상을 선택해 주세요.','warning');
		return false;
	}
}

function Btn_SqlProfile() {
	if ( $('#sqlProfileAppPop #sqlProfileType').val() == "3" ) {
		if ( $("#sqlProfileAppPop #transTargetDB" ).combobox("getValue") == '' ) {
			parent.$.messager.alert('경고','이관대상DB를 먼저 선택해 주세요.','warning');
			return false;
		}
	}
	var param = "확인";
	var msgStr = $("#sqlProfileAppPop #Btn_profile").html()+" 하시겠습니까?";
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			if ( $("#profile_form #isAll").val() == "A" ) { // 전체선택 시
				
				/* 전체시 List 재조회 */
				ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerformanceResultListAll",
						$("#submit_form"),
						"POST",
						callback_callSqlProfileApplyAllAction);
				
			} else {// 개별선택 시
				let data = $('#tableList').datagrid('getChecked');
				
				sqlProfileDataMasterCall( data );
			}
		}
	});
}

var callback_callSqlProfileApplyAllAction = function( result ) {
	var data = JSON.parse( result );
	sqlProfileDataMasterCall( data.rows );
}

function sqlProfileDataMasterCall( data ){
	let type = $('#sqlProfileAppPop #sqlProfileType').val();
	var sqlIdList;
	var hashValueList;
		
	for ( var dataIdx = 0; dataIdx < data.length; dataIdx++ ) {
		sqlIdList += ","+data[dataIdx].sql_id;
		hashValueList += ","+data[dataIdx].asis_plan_hash_value;
	}
	
	$("#sqlIdList").val( sqlIdList );
	$("#planHashValueList").val( hashValueList );
	$("#searchKey2").val( type );
	$("#perf_check_target_dbid").val( data[0].perf_check_target_dbid );
	
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("SQL PROFILE "+$("#sqlProfileAppPop #Btn_profile").html()+" 중 입니다."," ");
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/callSqlProfileApply",
			$('#submit_form'),
			"POST",
			callback_CallSqlProfileApplyAction);
	
}

var callback_CallSqlProfileApplyAction = function( result ){
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	Btn_OnClosePopup('sqlProfileAppPop');
	
	
	if ( result.result ) {
		if ( result.txtValue != "" ) {
			parent.$.messager.alert('',result.txtValue+"<br>"+$("#sqlProfileAppPop #Btn_profile").html()+'완료 되었습니다.','info');
		} else {
			parent.$.messager.alert('',$("#sqlProfileAppPop #Btn_profile").html()+'완료 되었습니다.','info');
		}
	} else {
		parent.$.messager.alert('',$("#sqlProfileAppPop #Btn_profile").html()+'하지 못했습니다.<br>관리자에게 문의하여 주십시요.','info');
	}
	
	Btn_SqlAutoPerfSearch();
};

// tunerAssign_popup의 기능들은 tunerAssign_popup.js를 사용
// 튜닝요청 및 담당자 지정만 재정의.
function Btn_SaveTuningDesignation(){
	var projectId = $('#assign_form #project_id').combobox('getValue');
	var sqlAutoPerfCheckId = $('#assign_form #sqlPerformanceP').combobox('getValue');
	
	$("#assign_form #sql_auto_perf_check_id").val( sqlAutoPerfCheckId );
	
	if ( projectId != "" && sqlAutoPerfCheckId == "" ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	params = "project_id="+ projectId +"&sql_auto_perf_check_id="+sqlAutoPerfCheckId;
	
	if ( $('#selectTuner').combobox('getValue') == "" ) {
		if ( $('#auto_share').val() == "N" ) {
			parent.$.messager.alert('경고','튜닝담당자를 선택해 주세요.','warning');
			return false;
		}
	}
	
	$("#assign_form #perfr_id").val( $('#selectTuner').combobox('getValue') );
	$('#assign_form #sqlIdArry').val("");
	
	// 전체 check 시
	if ( $("#assign_form #isAll").val() == "A" ) {
		/* modal progress open */
		if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("자동 성능 점검 조회"," ");
		
		$('#submit_form #select_sql').val("00");
		$('#submit_form #select_perf_impact').val("00");
		
		if ( $("#chkExcept").switchbutton("options").checked ) {
			$("#submit_form #sqlExclude").val("Y");
		} else {
			$("#submit_form #sqlExclude").val("");
		}
		/* 전체시 List 재조회 */
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerformanceResultListAll",
				$("#submit_form"),
				"POST",
				callback_ReLoadPerfResultListAllAction)
	// 개별 check 시
	} else {
		var data = $('#tableList').datagrid('getChecked');
		
		if ( $("#chkExcept").switchbutton("options").checked == true ) {
			var numbers = "";
			// tuning_no로 판단
			for ( var checkds = data.length-1; checkds > 0-1; checkds-- ) {
				if ( data[checkds].tuning_no != null ) {
					data.splice( checkds, 1 );
				}
			}
			
			dataListCall( data );
		} else {
			dataListCall( data );
		}
	}
}

var callback_ReLoadPerfResultListAllAction = function( result ) {
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	var data = JSON.parse( result );
	
	if ( data.rows.length > 0 ) {
		var sqlIdArry = "";
		
		for ( var checkdList = 0 ; checkdList < data.rows.length; checkdList++ ) {
			sqlIdArry += data.rows[checkdList].sql_id + "|";
		}
		
		$("#assign_form #sqlIdArry").val( strRight( sqlIdArry,1 ) );
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/InsertTuningRequest",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정 되었습니다.','info');
		parent.closeMessageProgress();
		Btn_SqlAutoPerfSearch();
	}
}

// 개별 시
function dataListCall( data ) {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	if ( data.length > 0 ) {
		var sqlIdArry = ""
			
		for ( var checkdList = 0 ; checkdList < data.length; checkdList++ ) {
			sqlIdArry += data[checkdList].sql_id + "|";
		}
		
		$("#assign_form #sqlIdArry").val( strRight( sqlIdArry,1 ) );
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/InsertTuningRequest",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		Btn_SqlAutoPerfSearch();
	}
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if ( result.result ) {
			
			/* modal progress close */
//		closeMessageProgress();
		parent.closeMessageProgress();
		
		let cnt = 0;
		let msg = "";
		
		for ( var checkCnt = 0; checkCnt < result.object.length; checkCnt++ ) {
			cnt =  cnt + ( 1 * result.object[checkCnt].perfr_id_cnt);
		} 
		
		if ( result.message == "Y" ) {
			msg = "이전 튜닝대상선정 SQL을 제외한<br> "+ cnt + "건이 선정 되었습니다."
		} else {
			msg = "튜닝대상 "+ cnt +"건이 선정 되었습니다."
		}
		
		$.messager.alert('', msg ,'info',function(){
			setTimeout(function() {
				
				Btn_OnClosePopup('tuningAssignPop');
				Btn_SqlAutoPerfSearch();
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);
		});
	}
};

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	
	if ( $("#submit_form #sqlPerformanceP").combobox("getValue") == sqlPerfId ) {
		$("#submit_form #sqlPerformanceP").combobox("setValue","");
	}
}