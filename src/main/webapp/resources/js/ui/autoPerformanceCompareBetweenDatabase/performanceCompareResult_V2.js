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
var toggleNum;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(550),
		left:getWindowLeft(550)
	});
	
	$("#tuningAssignPop").window("close");
	
	// 프로젝트 조회
	$('#submit_form #project_id_cd').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// submit_form SQL점검팩 콤보
			$('#submit_form #sqlPerformanceP').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
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
						url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
						+"&database_kinds_cd="+$("#database_kinds_cd").val(),
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
	
	// select문 check
	$("#select_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				if ( $("#dml_yn").checkbox("options").checked == false ) {
					$("#select_yn").checkbox("check");
				}
			}
		}
	});
	
	// dml문 check
	$("#dml_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				if ( $("#select_yn").checkbox("options").checked == false ) {
					$("#dml_yn").checkbox("check");
				}
			}
		}
	});
	
	// 전체 check
	$("#all_sql_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// plan변경 check
	$("#plan_change_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	//  성능저하 check
	$("#perf_down_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// 성능 부적합 check
	$("#notPerf_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// 오류 check
	$("#error_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
				$("#error_yn").checkbox("check");
				
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false &&
					 $("#perf_down_yn").checkbox("options").checked == false &&
					 $("#notPerf_yn").checkbox("options").checked == false &&
					 $("#fullScan_yn").checkbox("options").checked == false &&
					 $("#partition_yn").checkbox("options").checked == false &&
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#all_sql_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
				
				$("#error_yn").checkbox("uncheck");
			}
		}
	});
	
	// FULL SCAN check
	$("#fullScan_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});

	// PARTITION ALL ACCESS check
	$("#partition_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
					 $("#perf_down_yn").checkbox("options").checked == false && 
					 $("#notPerf_yn").checkbox("options").checked == false && 
					 $("#fullScan_yn").checkbox("options").checked == false && 
					 $("#partition_yn").checkbox("options").checked == false && 
					 $("#timeOut_yn").checkbox("options").checked == false && 
					 $("#maxFetch_yn").checkbox("options").checked == false && 
					 $("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// TIME-OUT check
	$("#timeOut_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("check");
				
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
						$("#perf_down_yn").checkbox("options").checked == false && 
						$("#notPerf_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#timeOut_yn").checkbox("options").checked == false && 
						$("#maxFetch_yn").checkbox("options").checked == false && 
						$("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// MAX FETCH 초과 check
	$("#maxFetch_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#timeOut_yn").checkbox("uncheck");
				$("#maxFetch_yn").checkbox("check");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked == false && 
						$("#perf_down_yn").checkbox("options").checked == false && 
						$("#notPerf_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#timeOut_yn").checkbox("options").checked == false && 
						$("#maxFetch_yn").checkbox("options").checked == false && 
						$("#error_yn").checkbox("options").checked == false ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SQL Profile 적용
	$("#sql_profile_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				if ( $("#not_sql_profile_yn").checkbox("options").checked == false ) {
					$("#sql_profile_yn").checkbox("check");
				}
			}
		}
	});
	// SQL Profile 미적용
	$("#not_sql_profile_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				if ( $("#sql_profile_yn").checkbox("options").checked == false ) {
					$("#not_sql_profile_yn").checkbox("check");
				}
			}
		}
	});
	// SQL 검토결과 검토
	$("#sql_review_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				if ( $("#not_sql_review_yn").checkbox("options").checked == false ) {
					$("#sql_review_yn").checkbox("check");
				} else if ( $("#not_sql_review_yn").checkbox("options").checked == true ){
					$("#review_keyword").textbox("setValue","");
				}
			}
		}
	});
	// SQL 검토결과 미검토
	$("#not_sql_review_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#review_keyword").textbox("setValue","");
				$("#review_keyword").textbox("textbox").attr("placeholder","sql검토 키워드 입력");
			} else {
				if ( $("#sql_review_yn").checkbox("options").checked == false ) {
					$("#not_sql_review_yn").checkbox("check");
				} else {
					$("#sql_review_yn").checkbox("check");
				}
			}
		}
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
	$("#review_keyword").textbox("textbox").attr("placeholder","sql검토 키워드 입력");
	
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
	
	$("#review_keyword").textbox("textbox").keyup(function() {
		if ( $(this).val() != null && $(this).val() != '' ) {
			$("#not_sql_review_yn").checkbox("uncheck");
		} else {
			$("#not_sql_review_yn").checkbox("check");
		}
	});
	$("#review_keyword").textbox("textbox").focus(function() {
		$("#review_keyword").textbox("textbox").attr("placeholder","");
	});
	$("#review_keyword").textbox("textbox").blur(function() {
		$("#review_keyword").textbox("textbox").attr("placeholder","sql검토 키워드 입력");
		if ( $("#review_keyword").textbox("getValue") != null && $("#review_keyword").textbox("getValue") != '' ) {
			$("#not_sql_review_yn").checkbox("uncheck");
		} else {
			$("#not_sql_review_yn").checkbox("check");
		}
	});
	
	$("#memoEdit").focusin(function() {
		if ( $("#memoEdit").val().length <= 0 ) {
			$("#memoEdit").val(" ");
		}
	});
	
	$("#memoEdit").focusout(function() {
		if ( $("#memoEdit").val().length == 1 && $("#memoEdit").val() == " " ) {
			$("#memoEdit").val("");
		}
	});
	
	initPerfCheckResult();
	
	createPerfImpactChart();
	createElapsedTimeChart();
	createBufferGetsChart();
	createPlanChangeChart();
	createPerfFitChart();
	
	createList();
	$('#tableList').datagrid("loadData", []);
	
//	$('#tableList').datagrid('sort', {	
//		sortName: 'buffer_increase_ratio'
//	,elapsed_time_increase_ratio,asis_executions,tobe_executions,asis_rows_processed,tobe_rows_processed,asis_elapsed_time,tobe_elapsed_time,asis_buffer_gets,tobe_buffer_gets'
//			
//		,sortOrder: 'asc'
//	});
	
	toggleNum = 1;
	/* modal progress close */
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
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
		COLOR_04: '#00af50',
		COLOR_05: '#ffd700'
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
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_fetch_exceed_title',
				yField : 'perf_impact_fetch_exceed_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_05],
				label: {
					field: 'perf_impact_fetch_exceed_chart',
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
			{field:'chk',halign:"center",align:"center",checkbox:"true",rowspan:'2'},
			{field:'tuning_status_nm',title:'튜닝상태',width:'3%',halign:'center',align:'center',rowspan:'2'},
			{field:'perf_impact_type_nm',title:'성능임팩트<br>유형',width:'5%',halign:'center',align:'center',rowspan:'2'},
			{field:'buffer_increase_ratio',title:'버퍼<br> 임팩트(배)',width:'4%',halign:'center',align:'right',
				formatter:getNumberFormatNullChk,sortable:true,rowspan:'2',sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'4%',halign:'center',align:'right',
				formatter:getNumberFormatNullChk,sortable:true,rowspan:'2',sorter:numberSorter},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',styler:cellStyler,rowspan:'2'},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'3%',halign:'center',align:'center'
				,formatter:setValueNull,styler:cellPlanStyler,rowspan:'2'},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left',rowspan:'2'},
			
			{title:'PLAN HASH VALUE',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'EXECUTIONS',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'ROWS PROCESSED',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'ELAPSED TIME',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'BUFFER GETS',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'FULLSCAN YN',width:'5%',halign:"center",align:'right',colspan:2},
			{title:'PARTITION ALL ACCESS YN',width:'5%',halign:"center",align:'right',colspan:2},
			 
			{field:'sql_command_type_cd',title:'SQL 명령 유형',width:'5%',halign:"center",align:'center',rowspan:'2'},
			{field:'parsing_schema_name',title:'PARSING<br>SCHEMA NAME ',width:'6%',halign:"center",align:'left',rowspan:'2'},
			{field:'err_code',title:'에러코드',width:'5%',halign:"center",align:'left',rowspan:'2'},
			{field:'err_msg',title:'에러메시지',width: '10%',halign:"center",align:'left',rowspan:'2'},
			{field:'review_sbst',title:'검토결과',width: '10%',halign:"center",align:'left',rowspan:'2'},
			{field:'sql_profile_nm',title:'PROFILE NAME',width:'12%',halign:"center",align:'left',rowspan:'2'},
			{field:'sql_text_web',title:'SQL TEXT',width: '15%',halign:"center",align:'left',rowspan:'2'},
			{field:'tuning_no',title:'튜닝번호',width:'4%',halign:"center",align:'right',rowspan:'2'},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:'2'},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left',rowspan:'2'},
			{field:'perf_check_target_dbid',title:'perf_check_target_dbid',hidden:'true',rowspan:'2'},
			{field:'perf_check_sql_source_type_cd',title:'perf_check_sql_source_type_cd',hidden:'true',rowspan:'2'}
			],[
			{field:'asis_plan_hash_value',title:'ASIS',width:'5%',halign:"center",align:'right'},
			{field:'tobe_plan_hash_value',title:'TOBE',width:'5%',halign:"center",align:'right'},
			{field:'asis_executions',title:'ASIS',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_executions',title:'TOBE',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_rows_processed',title:'ASIS',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_rows_processed',title:'TOBE',width:'3%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_elapsed_time',title:'ASIS',width:'4%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_elapsed_time',title:'TOBE',width:'4%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_buffer_gets',title:'ASIS',width:'4%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'TOBE',width:'4%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_fullscan_yn',title:'ASIS',width:'4%',halign:"center",align:'center'},
			{field:'tobe_fullscan_yn',title:'TOBE',width:'4%',halign:"center",align:'center'},
			{field:'asis_partition_all_access_yn',title:'ASIS',width:'5%',halign:"center",align:'center'},
			{field:'tobe_partition_all_access_yn',title:'TOBE',width:'5%',halign:"center",align:'center'}
		]],
		onSelect:function( index, row ) {
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #dbid").val(row.original_dbid);
			$("#submit_form #plan_hash_value").val(row.asis_plan_hash_value);
			$("#submit_form #asis_plan_hash_value").val(row.tobe_plan_hash_value);
			$("#submit_form #sql_command_type_cd").val(row.sql_command_type_cd);
			$("#submit_form #perf_check_sql_source_type_cd").val(row.perf_check_sql_source_type_cd);
			$("#submit_form #tobe_executions").val(row.tobe_executions);
			
			$('#loadExplainPlan_form [name = asis_plan_hash_value]').val( row.asis_plan_hash_value );
			
			//plan 비교 기능을 위해 데이터 세팅
			$('#loadExplainPlan_form [name = dbid]').val(row.original_dbid);
			$('#loadExplainPlan_form [name = sql_id]').val(row.sql_id);
			$('#loadExplainPlan_form [name = plan_hash_value]').val(row.asis_plan_hash_value);
			$('#loadExplainPlan_form [name = tobe_plan_hash_value]').val(row.tobe_plan_hash_value);
			$('#loadExplainPlan_form [name = sql_command_type_cd]').val(row.sql_command_type_cd);
			$('#loadExplainPlan_form [name = tobe_executions]').val(row.tobe_executions);
			$('#loadExplainPlan_form [name = perf_check_sql_source_type_cd]').val( $('#submit_form #perf_check_sql_source_type_cd').val() );
			
			$('#memo_form [name = dbid]').val(row.original_dbid);
			$('#memo_form [name = sql_id]').val(row.sql_id);
			
			openExplainPlan(row.plan_change_yn);
		},
		onCheck:function(index, rows) {
			cnt++;
		},
		onUncheck:function(index, rows) {
			if ( $("#profile_form #isAll").val() == "A" ) {
				cnt = 40;
				isAllReset();
			}
			$("#assign_form #isAll").val("");
			$("#profile_form #isAll").val("");
			cnt--;
		},
		onCheckAll:function( rows ) {
			$("#assign_form #isAll").val("A");
			$("#submit_form #isAll").val("A");
			$("#profile_form #isAll").val("A");
			cnt = totCnt;
			$("#sqlProfileAppPop #sqlIdPop").html( rows[0].sql_id );
			$("#sqlProfileAppPop #planHashValuePop").html( rows[0].asis_plan_hash_value );
			$("#sqlProfileAppPop #profilePop").html( "OP_"+rows[0].sql_id+"_"+rows[0].asis_plan_hash_value );
		},
		onUncheckAll:function( rows ) { 
			isAllReset();
			cnt = 0;
			$("#sqlProfileAppPop #sqlIdPop").html( "" );
			$("#sqlProfileAppPop #planHashValuePop").html( "" );
			$("#sqlProfileAppPop #profilePop").html( "" );
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function() {
			
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

function setValueNull( val, row ) {
	return '';
}
function cellPlanStyler( value,row,index ) {
	if ( row.plan_change_yn == 'Y' ) {
		return 'font-size:0px; background-image:url(/resources/images/planNotEqual.png);background-repeat:no-repeat;background-position: center;background-size: 60px;';
	} else if ( row.plan_change_yn == 'N' ){
		return 'font-size:0px; background-image:url(/resources/images/planEqual.png);background-repeat:no-repeat;background-position: center;background-size: 60px;';
	} else {
		return '';
	}
}

function checkCondition() {
	if ( $("#submit_form #project_id_cd" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sqlPerformanceP" ).combobox("getValue") == '' ) {
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
	
	$('.sqlMemo').css('visibility','visible');
	// Plan이 변경되었을 경우에만 Plan 비교 버튼 보이기
	if ( plan_change_yn == 'Y' ) {
		$('#planCompare').css('visibility','visible');
	} else {
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
	
//	$("#bindValueList").datagrid({
//		view: myview,
//		fitColumns:true,
//		columns:[[
//			{field:'bind_var_nm',title:'BIND_VAR_NM',halign:"center",align:'center'},
//			{field:'bind_var_value',title:'BIND_VAR_VALUE',halign:"center",align:'left'},
//			{field:'bind_var_type',title:'BIND_VAR_TYPE',halign:"center",align:'right'}
//		]],
//		onLoadError:function() {
//			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
//		}
//	});
	$("#bindValueList").datagrid({
		view: myview,
		fitColumns:true,
		columns:[[
			{field:'no',title:'NO',halign:"center",width:10,align:'right'},
			{field:'bind_var_nm',title:'NAME',halign:"center",width:20,align:'left'},
			{field:'bind_var_value',title:'VALUE',halign:"center",width:20,align:'left'},
			{field:'plan_hash_value',title:'PLAN HASH<br>VALUE',halign:"center",width:18,align:'right'},
			{field:'elapsed_time',title:'ELAPSED<br>TIME',halign:"center",width:18,formatter:getNumberFormatNullChk,align:'right'},
			{field:'buffer_gets',title:'BUFFER<br>GETS',halign:"center",width:15,formatter:getNumberFormatNullChk,align:'right'},
			{field:'rows_processed',title:'ROWS<br>PROCESSED',halign:"center",width:15,align:'right'},
			{field:'planCompare',title:'PLAN<br>비교',halign:"center",width:15,align:'center'}
		]],
		onLoadSuccess:function(data) {
			// datagrid 셀 병합.
			let cnt = 1;
			let idx = 0;
			for( let listIdx=1; listIdx <= data.rows.length; listIdx++ ) {
				
				if ( listIdx == data.rows.length ) {
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'no',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'plan_hash_value',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'elapsed_time',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'buffer_gets',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'rows_processed',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'planCompare',
						rowspan: cnt,
					});
				} else if ( data.rows[idx].no == data.rows[listIdx].no ) {
					cnt ++;
				} else {
					
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'no',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'plan_hash_value',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'elapsed_time',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'buffer_gets',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'rows_processed',
						rowspan: cnt
					});
					$('#bindValueList').datagrid('mergeCells',{
						index:idx,
						field:'planCompare',
						rowspan: cnt
					});
					cnt = 1;
					idx = listIdx;
				}
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
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
	let dmlExecYn = "";
	let sqlCommend = "";
	let tobePlanHashValue = "";
	let code = $("#submit_form #perf_check_sql_source_type_cd").val();
	let PlanHashValueOld = "";
	
	$.ajax({
		url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfDetailInfo",
		data:{
			project_id : $("#submit_form #project_id_cd").combobox("getValue") ,
			sql_auto_perf_check_id : $("#submit_form #sqlPerformanceP").combobox("getValue") ,
			database_kinds_cd : $('#submit_form [name = database_kinds_cd]').val()
		},
		type:"POST",
		dataType:"json",
		success: function( result ) {
			dmlExecYn = result[0].dml_exec_yn;
			sqlCommend = $("#submit_form #sql_command_type_cd").val();
			tobePlanHashValue = $('#loadExplainPlan_form [name = tobe_plan_hash_value]').val();
		},
		error: function( result ) {
			dmlExecYn = "";
			sqlCommend = "";
			tobePlanHashValue = "";
		}
	});
	
	$("#textArea").val('');
	$("#asisTextPlan").val('');
	$("#operTextPlan").val('');
	
	$('#memoBox').window({
		title : "SQL 검토결과",
		top:getWindowTop(315),
		left:getWindowLeft(1435),
		closable:false
	});
	$('#memoBox').window("close");
	
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
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadExplainInfoBindValue",
			submit_form, "POST", callback_BindValueListNewAction);
	
	/* Tobe Plan */
	setTimeout(function() {
		let url = "";
		PlanHashValueOld = $("#plan_hash_value").val();
		$("#plan_hash_value").val( $("#asis_plan_hash_value").val() );
		
		console.log( dmlExecYn +" , "+ sqlCommend+" , "+tobePlanHashValue );
		if ( (dmlExecYn == "N" && sqlCommend != "SELECT" ) ||
				(sqlCommend == "INSERT" && (tobePlanHashValue == "" || tobePlanHashValue == 0 )) ) {
			url = "/AutoPerformanceCompareBetweenDatabase/loadAfterDMLTextPlanListAll";
		} else {
			url = "/AutoPerformanceCompareBetweenDatabase/loadAfterSelectTextPlanListAll";
		} 
		
		ajaxCall(url
				, $("#submit_form")
				, "POST"
				, callback_SQLPerformTextOperPlanListAction );
		
		$("#plan_hash_value").val(PlanHashValueOld);
		PlanHashValueOld = "";
	},500);
	
}

// callback
var callback_BindValueListNewAction = function(result) {
	var data = null;
	try{
		data = JSON.parse(result);
	
		if ( data.result != undefined && data.result == false ) {
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
	
	if ( $("#profile_form #cntPage").val() != null && $("#profile_form #cntPage").val() != '' ) {
		$("#currentPage").val( $("#profile_form #cntPage").val() );
		$("#profile_form #cntPage").val("");
	} else if ( $("#assign_form #cntPage").val() != null && $("#assign_form #cntPage").val() != '' ) {
		$("#currentPage").val( $("#assign_form #cntPage").val() );
		$("#assign_form #cntPage").val("")
	} else {
		$("#currentPage").val("1");
	}
	$("#pagePerCount").val("40");
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
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능 영향도 분석 결과 조회"," ");
	
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
	
	cnt = 0;
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
	
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerfResultCount",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	var data = JSON.parse(rows)[0];
	
	if ( typeof data == 'undefined') {
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		return;
	}
	
	$(".perf_check_result_blue").val(data.total_cnt);
	$(".perf_check_result_green").val(data.completed_cnt);
	$(".perf_check_result_orange").val(data.err_cnt);
	
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
	
	let src = "/AutoPerformanceCompareBetweenDatabase/excelDownload";
	$("#submit_form").attr("action", src);
	$("#submit_form").submit();
	showDownloadProgress( src );
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
	$(".performanceResultCount").val("검색결과 건수 : "+parseInt(totCnt).toLocaleString());
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
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
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
		
		$('#assign_form #dbid').val($('#dbid').val());
		$("#assign_form #choice_div_cd").val( CHOICE_DIV_CD );
		$("#submit_form #choice_div_cd").val( CHOICE_DIV_CD );
		$("#assign_form #tuning_status_cd").val( TUNING_STATUS_CD );
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
		
		$("#sqlProfileAppPop #sqlIdPop").html( rows[rows.length-1].sql_id );
		$("#sqlProfileAppPop #planHashValuePop").html( rows[rows.length-1].asis_plan_hash_value );
		$("#sqlProfileAppPop #profilePop").html( "OP_"+rows[rows.length-1].sql_id+"_"+rows[rows.length-1].asis_plan_hash_value );
		
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
			$("#sqlProfileAppPop #profileText").show();
		} else if ( type == 2 && equalCnt == rows.length ) {
			$(".transBtn").hide();
			
			$('#sqlProfileAppPop #transTargetDB').combobox("setValue","");
			$('#sqlProfileAppPop #sqlProfileType').val("2");
			$("#sqlProfileAppPop #profileText").show();
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
			$("#sqlProfileAppPop #profileText").hide();
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
		
		if ( rows.length > 1 ) {
			$("#sqlProfileAppPop #showHide").show();
		} else {
			$("#sqlProfileAppPop #showHide").hide();
		}
		
		$("#sqlProfileAppPop #Btn_profile").html( btnName );
		
		if ( $("#profile_form #isAll").val() != "A" ) {
			$("#sqlProfileAppPop #profileCnt").html( rows.length );
		} else{
			$("#sqlProfileAppPop #profileCnt").html( cnt );
		}
		
		$('#sqlProfileAppPop').window("open");
	
	} else {
		parent.$.messager.alert('경고','SQL Profile을 '+ btnName +'할 대상을 선택해 주세요.','warning');
		return false;
	}
}

function Btn_SqlProfile() {
	if ( $('#sqlProfileAppPop #sqlProfileType').val() == "3" && $("#sqlProfileAppPop #transTargetDB" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','이관대상DB를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	$("#profile_form #cntPage").val( $("#currentPage").val() );
	
	let param = "확인";
	let msgStr = $("#sqlProfileAppPop #Btn_profile").html()+" 하시겠습니까?";
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			/* modal progress open */
			if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("SQL PROFILE "+$("#sqlProfileAppPop #Btn_profile").html()+" 중 입니다."," ");
			
			// 개별선택 시 사용할 데이터  전체 시엔 사용 X
			let data = $('#tableList').datagrid('getChecked');
			
			sqlProfileDataMasterCall( data );
		}
	});
}

// SQL Profile 값 설정
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
	
	console.log ( $("#sqlIdList").val() + "\n"+ $("#planHashValueList").val() );
	
	// SQLProfile적용/삭제/이관 실행.
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
	
	isAllReset();
	Btn_SqlAutoPerfSearch();
};

// tunerAssign_popup의 기능들은 tunerAssign_popup.js를 사용
// 튜닝요청 및 담당자 지정만 재정의.
function Btn_SaveTuningDesignation(){
	var projectId = $('#assign_form #project_id').combobox('getValue');
	var sqlAutoPerfCheckId = $('#assign_form #sqlPerformanceP').combobox('getValue');
	
	$("#assign_form #sql_auto_perf_check_id").val( sqlAutoPerfCheckId );
	$("#assign_form #cntPage").val( $("#currentPage").val() );
	
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
	var data = JSON.parse( result );
	
	dataListCall( data.rows );
}

// 개별 시
function dataListCall( data ) {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	if ( data.length > 0 ) {
		let sqlIdArry = "";
		let parsingSchemaNameArry = "";
		let moduleArry = "";
		let asisPlanHashValueArry = "";
		let tobePlanHashValueArry = "";
		
		$("#assign_form #perf_check_sql_source_type_cd").val( data[0].perf_check_sql_source_type_cd );
		
		for ( var checkdList = 0 ; checkdList < data.length; checkdList++ ) {
			sqlIdArry += data[checkdList].sql_id + "|";
			parsingSchemaNameArry += data[checkdList].parsing_schema_name + "|";
			moduleArry += data[checkdList].module + "|";
			asisPlanHashValueArry += data[checkdList].asis_plan_hash_value + "|";
			tobePlanHashValueArry += data[checkdList].tobe_plan_hash_value + "|";
		}
		
		$("#assign_form #sqlIdArry").val( strRight( sqlIdArry,1 ) );
		$("#assign_form #parsing_schema_name").val( strRight( parsingSchemaNameArry,1 ) );
		$("#assign_form #module").val( strRight( moduleArry,1 ) );
		$("#assign_form #asisPlanHashValueArry").val( strRight( asisPlanHashValueArry,1 ) );
		$("#assign_form #planHashValueArry").val( strRight( tobePlanHashValueArry,1 ) );
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/InsertTuningRequest",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();;
		
		isAllReset();
		Btn_SqlAutoPerfSearch();
	}
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if ( result.result ) {
			
			/* modal progress close */
//		closeMessageProgress();
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
		
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
				
				isAllReset();
				Btn_SqlAutoPerfSearch();
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);
		});
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
		
		isAllReset();
		Btn_SqlAutoPerfSearch();
	}
};

function gridWide() {
	toggleNum = (toggleNum + 1) % 2;
	
	if ( toggleNum == 0 ) {
		document.getElementById("searchHide").style.display = "none";
		document.getElementById("chartHide").style.display = "none";
		
		$("#tableListHeight").css("height","628px");
		$("#tableListHeight > div > .panel-body").css("height","628px");
		$("#tableList").datagrid('resize',{
			  height: 628
			});
	} else {
		document.getElementById("searchHide").style.display = "block";
		document.getElementById("chartHide").style.display = "block";
		
		$("#tableListHeight").css("height","285px");
		$("#tableListHeight > div > .panel-body").css("height","285px");
		$("#tableList").datagrid('resize',{
			  height: 285
			});
	}
}

function Btn_SetSQLmemo() {
	$('#memoBox').window({
		top:getWindowTop(315),
		left:getWindowLeft(1435)
	});
	
	// sql 검토결과조회
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/getSqlMemo",
			$("#memo_form"),
			"POST",
			callback_getSqlMemo);
	
	$("#memoBox").window("open");
}

var callback_getSqlMemo = function(result) {
	if ( result.result ) {
		$("#memoEdit").val( result.message );
		$("#review_sbst").val( result.message );
	} else {
		$("#memoEdit").val("");
//		parent.$.messager.alert('경고',result.message,'warning');
	}
}

function Btn_memoSave() {
	if ( $("#memo_form #memoEdit" ).val() == null || $("#memo_form #memoEdit" ).val() == '' ) {
		parent.$.messager.alert('경고','저장할 SQL검토결과를 입력해 주세요.','warning');
		return false;
	} else {
		var param = "확인";
		var msgStr = "저장 하시겠습니까?";
		
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				const maxByte = 4000; //최대 100바이트
				const totalByte = fn_checkByte( $("#memo_form #memoEdit").val() );
				
				if ( totalByte > maxByte ) {
					parent.$.messager.alert('경고','최대 4000Byte까지 저장이 가능합니다.<br>현재 : '+totalByte+'byte','warning');
					return false;
				} else {
					$("#review_sbst").val( $("#memo_form #memoEdit").val() );
					
					// sql 검토결과저장
					ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/updateSqlMemo",
							$("#memo_form"),
							"POST",
							callback_updateSqlMemo);
				}
			}
		});
	}
}

var callback_updateSqlMemo = function(result) {
	if ( result.result ) {
		parent.$.messager.alert('정보',result.message,'info');
	} else {
		parent.$.messager.alert('경고',result.message,'warning');
	}
	
	$("#memoBox").window("close");
}

function Btn_memoDelete() {
	if ( $("#review_sbst").val() == null || $("#review_sbst").val() == '' ) {
		parent.$.messager.alert('경고','삭제할 SQL검토결과가 없습니다.','warning');
		return false;
	} else {
//		$("#review_sbst").val( $("#memo_form #memoEdit").val() );
		var param = "확인";
		var msgStr = "삭제 하시겠습니까?";
		
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				// sql 검토결과삭제
				ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/deleteSqlMemo",
						$("#memo_form"),
						"POST",
						callback_deleteSqlMemo);
			}
		});
	}
}

var callback_deleteSqlMemo = function(result) {
	if ( result.result ) {
		parent.$.messager.alert('정보',result.message,'info');
		
		$("#memoEdit").val( "" );
	} else {
		parent.$.messager.alert('경고',result.message,'warning');
		$("#memo_form #memoEdit").val( $("#review_sbst").val() );
	}
	$("#memoBox").window("close");
}

function Btn_ReSqlAutoPerfCompare() {
	if( checkCondition() == false ) {
		return false;
	}
	
	let rows = $('#tableList').datagrid('getChecked');
	console.log("재실행할 건수 ===> "+rows.length);
	
	if ( rows.length <= 0 ) {
		parent.$.messager.alert('경고','재실행 할 데이터를 선택해 주세요.','warning');
		return false;
	} else {
		/* modal progress open */
		if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("SQL을 재실행 중입니다."," ");
		
		let param = "확인";
		let msgStr ="선택한 SQL을 재실행 하시겠습니까?";
		
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				let sqlIdArr = "";
				
				for (var sqlIdIdx = 0; sqlIdIdx < rows.length; sqlIdIdx++) {
					if ( sqlIdIdx != rows.length-1 ) {
						sqlIdArr += rows[sqlIdIdx].sql_id+",";
					} else {
						sqlIdArr += rows[sqlIdIdx].sql_id;
					}
				}
				console.log("sqlIdArr======> "+sqlIdArr);
				$("#submit_form #sqlIdList").val( sqlIdArr );
				
				ajaxCall("/AutoPerformanceCompareBetweenDatabase/performanceCompareReCall",
						$("#submit_form"),
						"POST",
						callback_performancecompareReCall);
			} else {
				/* modal progress close */
				if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
			}
		});
	}
}

var callback_performancecompareReCall = function(result) {
	if( result.result) {
		parent.$.messager.alert('','재실행이 완료 되었습니다.','info');
	} else {
		parent.$.messager.alert('경고',result.message,'error');
	}
	
	$("#profile_form #cntPage").val( $("#currentPage").val() );
	
	isAllReset();
	Btn_SqlAutoPerfSearch();
	
	/* modal progress close */
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
}

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	
	if ( $("#submit_form #sqlPerformanceP").combobox("getValue") == sqlPerfId ) {
		$("#submit_form #sqlPerformanceP").combobox("setValue","");
	}
}

function resultSearch( projectId, sqlAutoPerfCheckId , dbcd) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	
	Btn_OnClosePopup('tuningAssignPop');
	Btn_OnClosePopup('memoBox');
	Btn_OnClosePopup('planComparePop');
	Btn_OnClosePopup('sqlProfileAppPop');
	
	$("#loadExplainPlanPop #textArea").val('');
	$('#loadExplainPlanPop #asisTextPlan').val("");
	$('#loadExplainPlanPop #operTextPlan').val("");
	
	isAllReset();
	
	Btn_OnClosePopup('loadExplainPlanPop');
	
	toggleNum = 0;
	gridWide();
	
	$("#submit_form #project_id_cd").combobox("setValue", projectId);
	$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfCheckId);
	
	if ( $("#database_kinds_cd").val() == null || $("#database_kinds_cd").val() == '' && dbcd != null && dbcd != '') {
		$("#database_kinds_cd").val( dbcd );
	}
	
	$("#submit_form #select_yn").checkbox("check");
	$("#submit_form #dml_yn").checkbox("check");
	$("#submit_form #all_sql_yn").checkbox("check");
	
	$("#submit_form #sql_profile_yn").checkbox("check");
	$("#submit_form #not_sql_profile_yn").checkbox("check");
	$("#submit_form #sql_review_yn").checkbox("check");
	$("#submit_form #not_sql_review_yn").checkbox("check");
	 
	$("#submit_form #buffer_gets_1day").textbox("setValue","");
	$("#submit_form #asis_elapsed_time").textbox("setValue","");
	$("#submit_form #buffer_gets_regres").textbox("setValue","");
	$("#submit_form #elapsed_time_regres").textbox("setValue","");
	$("#submit_form #review_keyword").textbox("setValue","");
	$("#submit_form #search_sql_id").textbox("setValue","");
	
	Btn_SqlAutoPerfSearch(1);
	
	setTimeout(function(){
		$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfCheckId);
	}, 500);
	
	if ( closeMessageProgress != undefined ) closeMessageProgress();
}

function openMessageProgress(msgTitle, msgText){
	var strText = "데이터를 불러오는 중입니다.";
	if(msgText == ""){
		msgText = strText;
	}
	
	$.messager.progress({
		title:'Please waiting',
		msg:msgTitle,
		text:msgText,
		interval:100
	});	
}

/* 데이터 로딩 modal close */
function closeMessageProgress(){
	$.messager.progress('close');
}

// String => 바이트 로 변환
function fn_checkByte( str ) {
	const text_val = str; //입력한 문자
	const text_len = str.length; //입력한 문자수
	
	let totalByte=0;
	
	for( let strIdx = 0; strIdx < text_len ; strIdx++){
		const each_char = text_val.charAt(strIdx);
		const uni_char = escape(each_char) //유니코드 형식으로 변환
		
		if ( uni_char.length > 4 ) {
			// 한글 : 2Byte
			totalByte += 2;
		} else {
			// 영문,숫자,특수문자 : 1Byte
			totalByte += 1;
		}
	}
	
	return totalByte;
}

function isAllReset() {
	$("#assign_form #isAll").val("");
	$("#submit_form #isAll").val("");
	$("#profile_form #isAll").val("");
}
