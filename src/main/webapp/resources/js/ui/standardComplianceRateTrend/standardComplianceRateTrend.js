var chartStandardComplianceRateTrendTotal;
var chartWorkStatus;
var chartNonComplianceStatus;
var chartStandardComplianceRateTrend;
var chartNonStandardComplianceRateTrend;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createProjectCombo();
	createQtyDivCombo();
	
	createChartStandardComplianceRateTrendTotal();
	createChartWorkStatus();
	createChartNonComplianceStatus();
	createChartStandardComplianceRateTrend();
	createChartNonStandardComplianceRateTrend();
	
	initializeStatusByWork();
});

function createProjectCombo(){
	$('#project_id').combobox({
		url:'/Common/getProject',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onChange:function(newValue,oldValue){
			$('#sql_std_qty_div_cd').combobox('clear');
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$( '#submit_form #project_id' ).combobox( 'textbox' ).attr( 'placeholder' , '선택' );
		}
	});
}

function createQtyDivCombo(){
	$('#sql_std_qty_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1087&ref_vl_1=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue,oldValue) {
			let projectId = $('#project_id').combobox('getValue');
			
			if( projectId ) {
				loadSchedulerCombo(projectId, newValue);
			}
		}
	});
}

function loadSchedulerCombo(projectId, divCd){
	$('#sql_std_qty_scheduler_no').combobox('loadData', []);
	// 형상기반SQL표준일괄점검 01 , 실행기반SQL표준일괄점검 02
	let divCode = '00';
	
	if ( divCd == '2' || divCd == 2 ) {
		divCode = '01';
	} else if ( divCd == '4' || divCd == 4 ) {
		divCode = '02';
	}
	
	$('#sql_std_qty_scheduler_no').combobox({
		url:'/StandardComplianceRateTrend/loadSchedulerList?project_id='+ projectId +'&sql_std_qty_div_cd='+ divCode,
		method:'get',
		valueField:'sql_std_qty_scheduler_no',
		textField:'job_scheduler_nm',
		onChange:function(newValue,oldValue){
		}
	});
}

function initializeStatusByWork() {
	$("label[for='incre_sql_text']").text('SQL 증감');
	$("label[for='incre_err_text']").text('미준수 증감');
	$("label[for='incre_rate_text']").text('준수율 증감');
	
	$('#last_program_cnt').textbox('setValue', '0');
	$('#last_tot_err_cnt').textbox('setValue', '0');
	$('#last_cpla_rate').textbox('setValue', '0%');
	$('#incre_sql_cnt').textbox('setValue', '0');
	$('#incre_err_cnt').textbox('setValue', '0');
	$('#incre_rate').textbox('setValue', '0%');
	
	$('#incre_sql_cnt_point').removeClass('downArrow');
	$('#incre_sql_cnt_point').removeClass('upArrow');
	$('#incre_err_cnt_point').removeClass('downArrow');
	$('#incre_err_cnt_point').removeClass('upArrow');
	$('#incre_rate_point').removeClass('downArrow');
	$('#incre_rate_point').removeClass('upArrow');
	
	$('#last_program_cnt').textbox('textbox').css('font-weight', 800);
	$('#last_tot_err_cnt').textbox('textbox').css('font-weight', 800);
	$('#last_cpla_rate').textbox('textbox').css('font-weight', 800);
	$('#incre_sql_cnt').textbox('textbox').css('font-weight', 800);
	$('#incre_err_cnt').textbox('textbox').css('font-weight', 800);
	$('#incre_rate').textbox('textbox').css('font-weight', 800);
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

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#project_nm").textbox("setValue", row.project_nm);
	
	$("#project_id").combobox('setValue',row.project_id);
}

function createChartStandardComplianceRateTrendTotal(result){
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
	
	if(chartStandardComplianceRateTrendTotal != "undefined" && chartStandardComplianceRateTrendTotal != undefined){
		chartStandardComplianceRateTrendTotal.destroy();
	}
	
	Ext.define("chartStandardComplianceRateTrendTotal.colors", {	// Label 색상 정의
		singleton:  true,
		TOTAL: '#5e9cd5',		// 프로그램본수
		CNT: '#f0790f',			// 미준수건수
		RATE: '#ff2d2d'			// 준수율
	});
	
	chartStandardComplianceRateTrendTotal = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartStandardComplianceRateTrendTotal"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			captions: {
				title: {
					text: false,
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 13px Arial',
						fill:"#000000"
					}
				}
			},
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			type:'text',
			innerPadding : '3 3 3 3', // 차트안쪽 여백
			insetPadding : '3 20 0 3', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['program_cnt','tot_err_cnt'],	// 총 건수
				minimum: 0,
				grid: {
					odd: {
						opacity: 0.5,
						stroke: '#bbb',
						lineWidth: 1
					}
				},
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0
				},
				fields: ['sql_std_gather_day']
			},{
				type: 'numeric',
				position: 'right',
				minimum: 0,
				maximum: 100,
				fields: ['cpla_rate']		// 진행률
			}],
			series : [{
				type : 'line',
				fill : false,
				style : {
					opacity: 0.8,
					'stroke-width': 2
				},
				marker: {
					radius: 2,
					lineWidth: 2
				},
				xField : 'sql_std_gather_day',
				yField : ['program_cnt'],
				title: '',
				colors: [chartStandardComplianceRateTrendTotal.colors.TOTAL],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[프로그램본수] " + xField + " : " + yField);
					}
				}
			},{
				type : 'line',
				fill : false,
				style : {
					opacity: 0.8,
					'stroke-width': 2
				},
				marker: {
					radius: 2,
					lineWidth: 2
				},
				xField : 'sql_std_gather_day',
				yField : 'tot_err_cnt',
				title: '',
				colors: [chartStandardComplianceRateTrendTotal.colors.CNT],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[미준수건수] " + xField + " : " + yField);
					}
				}
			},{
				type : 'line',
				fill : false,
				style : {
					opacity: 0.8,
					'stroke-width': 2
				},
				marker: {
					radius: 2,
					lineWidth: 2
				},
				xField : 'sql_std_gather_day',
				yField : 'cpla_rate',
				title: '',
				colors: [chartStandardComplianceRateTrendTotal.colors.RATE],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[표준준수율] " + xField + " : " + yField);
					}
				}
			}]
		}]
	});
}

function createChartWorkStatus(result){
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
	
	if(chartWorkStatus != "undefined" && chartWorkStatus != undefined){
		chartWorkStatus.destroy();
	}
	
	Ext.define("chartWorkStatus.colors", {	// Label 색상 정의
		singleton:  true,
		LAST_PROGRAM_CNT: '#558fd1',	// 금회차 총본수
		LAST_TOT_ERR_CNT: '#f0790f',	// 금회차 미준수본수
		LAST_CPLA_RATE: '#ff0000',		// 금회차 준수율
		PRE_CPLA_RATE: '#1e44ca'		// 전회차 준수율
	});
	
	chartWorkStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartWorkStatus"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			type:'text',
			innerPadding : '3 0 0 0', // 차트안쪽 여백
			insetPadding : '3 3 0 4', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['last_program_cnt','last_tot_err_cnt'],	// 금번회차 총본.수, 금번회차 미준수본수
				minorTickSteps: 0,
				minimum: 0,
				grid: {
					odd: {
						opacity: 0.5,
						stroke: '#bbb',
						lineWidth: 1
					}
				},
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields: ['wrkjob_cd_nm']
			},{
				type: 'numeric',
				position: 'right',
				fields: ['last_cpla_rate','pre_cpla_rate'],		// 금번회차 준수율, 이전회차 준수율
				fixedAxisWidth: 20,
				minimum: 0,
				maximum: 100
			}],
			series : [{
				type : 'bar',
				style: {
					opacity: 0.8,
					minGapWidth: 10,
				},
				xField : 'wrkjob_cd_nm',
				yField : ['last_program_cnt', 'last_tot_err_cnt'],
				stacked: false,
				title: '',
				colors: [chartWorkStatus.colors.LAST_PROGRAM_CNT, chartWorkStatus.colors.LAST_TOT_ERR_CNT],
				label: {
					field: ['last_program_cnt', 'last_tot_err_cnt'],
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.field);
//						var xField = record.get(item.series.getXField());
						var workjob_cd_nm = record.get('wrkjob_cd_nm');
						var title = item.field == 'last_program_cnt' ? '금회차 총본수' : '금회차 미준수본수';
						
						tooltip.setHtml("[" + workjob_cd_nm + "] " + title + " : " + yField);
					}
				},
			},{
				type : 'line',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					opacity: 0.8,
					'stroke-width': 1
				},
				xField : 'wrkjob_cd_nm',
				yField : 'last_cpla_rate',
				title: '',
				colors: [chartWorkStatus.colors.LAST_CPLA_RATE],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
//						var xField = record.get(item.series.getXField());
						var workjob_cd_nm = record.get('wrkjob_cd_nm');
						var title = item.field;
						
						tooltip.setHtml("[" + workjob_cd_nm + "] 금회차 준수율 : " + yField);
					}
				}
			},{
				type : 'line',
				marker: {
					radius: 2,
					lineWidth: 2
				},
				style: {
					opacity: 0.8,
					'stroke-width': 1
				},
				xField : 'wrkjob_cd_nm',
				yField : 'pre_cpla_rate',
				title: '',
				colors: [chartWorkStatus.colors.PRE_CPLA_RATE],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
//						var xField = record.get(item.series.getXField());
						var workjob_cd_nm = record.get('wrkjob_cd_nm');
						var title = item.field;
						
						tooltip.setHtml("[" + workjob_cd_nm + "] 전회차 준수율 : " + yField);
					}
				}
			}],
			listeners: {
				itemmouseover: function(series, item, event, eOpts) {
					series.el.dom.style.cursor = 'pointer';
				},
				itemmouseout: function(series, item, event, eOpts) {
					series.el.dom.style.cursor = '';
				},
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					
					var parameter_list = "project_id:" + $('#project_id').combobox('getValue')+ ",sql_std_qty_div_cd:" + $('#sql_std_qty_div_cd').combobox('getValue') +
										 ",start_day:" + $('#strStartDt').val() + ",end_day:" + $('#strEndDt').val() + ",wrkjob_cd:" + data.wrkjob_cd +
										 ",sql_std_qty_scheduler_no:"+$('#sql_std_qty_scheduler_no').combobox('getValue');
					$('#submit_form #parameter_list').val(parameter_list);
					
					$('#submit_form #wrkjob_cd_nm').val(data.wrkjob_cd_nm);
					
					ajaxCallLoadChartNonComplianceStatus();
					ajaxCallLoadChartStandardComplianceRateTrend();
					ajaxCallLoadChartNonStandardComplianceRateTrend();
				}
			}
		}]
	});
}

function createChartNonComplianceStatus(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
			
			if($('#submit_form #parameter_list').val().indexOf('wrkjob_cd') > 0) {
				$('#titleChartNonComplianceStatus').html("품질지표별 미준수 현황(" + $('#submit_form #wrkjob_cd_nm').val() + ")");
			} else {
				$('#titleChartNonComplianceStatus').html("품질지표별 미준수 현황(전체)");
			}
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}else{
		data = {};
	}
	
	if(chartNonComplianceStatus != "undefined" && chartNonComplianceStatus != undefined){
		chartNonComplianceStatus.destroy();
	}
	
	Ext.define("chartNonComplianceStatus.colors", {	// Label 색상 정의
		singleton:  true,
		ERR_CNT: '#7fcedb'
	});
	
	chartNonComplianceStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartNonComplianceStatus"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			flipXY : true, // 가로/세로 축 변경
			border : false,
			innerPadding : '3 0 3 0', // 차트안쪽 여백[top, right, bottom, left]
//			insetPadding : 10, // 차트 밖 여백
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				adjustByMajorUnit: true,
				minorTickSteps: 0,
				minimum: 0,
				grid: {
					odd: {
						opacity: 0.5,
						stroke: '#bbb',
						lineWidth: 1
					}
				}
			},{
				type : 'category',
				position : 'left',
				fields: ['qty_chk_idt_nm'],
				label : {
					textAlign : 'right',
				}
			}],
			series : {
				type : 'bar',
				style: {
					opacity: 0.8,
					minGapWidth: 10,
				},
				xField : 'qty_chk_idt_nm',
				yField : ['err_cnt'],
				title : '',
				colors : [chartNonComplianceStatus.colors.ERR_CNT],
				label: {
					field: 'err_cnt',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + xField + "] : " + yField);
					}
				}
			}
		}]
	});
}

function createChartStandardComplianceRateTrend(result) {
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
	
	if(chartStandardComplianceRateTrend != "undefined" && chartStandardComplianceRateTrend != undefined){
		chartStandardComplianceRateTrend.destroy();		
	}
	
	chartStandardComplianceRateTrend = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartStandardComplianceRateTrend"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins : {
				chartitemevents: {
					moveEvents: true
				}
			},
			innerPadding : '3 0 3 0',// 차트안쪽 여백[top, right, bottom, left]
//			insetPadding : 20, // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : false,
				grid: {
					odd: {
						opacity: 0.5,
						stroke: '#bbb',
						lineWidth: 1
					}
				}
			},{
				type : 'category',
				position : 'bottom',
			}],
			series: []
		}]
	});
}

function createChartNonStandardComplianceRateTrend(result) {
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
	
	if(chartNonStandardComplianceRateTrend != "undefined" && chartNonStandardComplianceRateTrend != undefined){
		chartNonStandardComplianceRateTrend.destroy();		
	}
	
	chartNonStandardComplianceRateTrend = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartNonStandardComplianceRateTrend"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins : {
				chartitemevents: {
					moveEvents: true
				}
			},
			innerPadding : '3 0 3 0',// 차트안쪽 여백[top, right, bottom, left]
//			insetPadding : 20, // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : false,
				grid: {
					odd: {
						opacity: 0.5,
//						fill: '#eee',
						stroke: '#bbb',
						lineWidth: 1
					}
				}
			},{
				type : 'category',
				position : 'bottom',
//				grid: false,
			}],
			series: []
		}]
	});
}

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#submit_form #strStartDt').val($("#startDate").textbox("getValue"));
	$('#submit_form #strEndDt').val($("#endDate").textbox("getValue"));
	
	if( $("#project_id").combobox('getValue') == null || $("#project_id").combobox('getValue') == '' ) {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	if( $("#sql_std_qty_div_cd").combobox('getValue') == null || $("#sql_std_qty_div_cd").combobox('getValue') == '' ) {
		warningMessager('SQL표준점검구분을 선택하여 주세요.');
		return false;
	}
	
	if( $("#sql_std_qty_scheduler_no").combobox('getValue') == null || $("#sql_std_qty_scheduler_no").combobox('getValue') == '' ) {
		parent.$.messager.alert('경고','스케줄러를 선택해 주세요.','warning');
		return false;
	}
	
	if(!compareAnBDate($('#startDate').textbox('getValue'), $('#endDate').textbox('getValue'))) {
		var msg = "기준 일자를 확인해 주세요.<br>시작일자[" + $('#startDate').textbox('getValue') + "] 종료일자[" + $('#endDate').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	ajaxCallData();
}

function ajaxCallData() {
	var parameter_list = "project_id:" + $("#project_id").combobox('getValue') + ",sql_std_qty_div_cd:" + $('#sql_std_qty_div_cd').combobox('getValue') +
						 ",start_day:" + $('#strStartDt').val() + ",end_day:" + $('#strEndDt').val() +
						 ",sql_std_qty_scheduler_no:"+$('#sql_std_qty_scheduler_no').combobox('getValue');
	$('#submit_form #parameter_list').val(parameter_list);
	
	ajaxCallLoadChartStandardComplianceRateTrendTotal();
	ajaxCallLoadStatusByWork();
	ajaxCallLoadChartStatusByWork();
	ajaxCallLoadChartNonComplianceStatus();
	ajaxCallLoadChartStandardComplianceRateTrend();
	ajaxCallLoadChartNonStandardComplianceRateTrend();
}

function ajaxCallLoadChartStandardComplianceRateTrendTotal() {
	$('#submit_form #qty_chk_idt_cd').val('006');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartStandardComplianceRateTrendTotal",
			$("#submit_form"),
			"POST",
			callback_LoadChartStandardComplianceRateTrendTotal);
}

function ajaxCallLoadStatusByWork() {
	$('#submit_form #qty_chk_idt_cd').val('007');
	
	ajaxCall("/StandardComplianceRateTrend/loadStatusByWork",
			$("#submit_form"),
			"POST",
			callback_LoadStatusByWork);
}

function ajaxCallLoadChartStatusByWork() {
	$('#submit_form #qty_chk_idt_cd').val('008');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartStatusByWork",
			$("#submit_form"),
			"POST",
			callback_LoadChartStatusByWork);
}

function ajaxCallLoadChartNonComplianceStatus() {
	$('#submit_form #qty_chk_idt_cd').val('009');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartNonComplianceStatus",
			$("#submit_form"),
			"POST",
			callback_LoadChartNonComplianceStatus);
}

function ajaxCallLoadChartStandardComplianceRateTrend() {
	$('#submit_form #qty_chk_idt_cd').val('010');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartStandardComplianceRateTrend",
			$("#submit_form"),
			"POST",
			callback_LoadChartStandardComplianceRateTrend);
}

function ajaxCallLoadChartNonStandardComplianceRateTrend() {
	$('#submit_form #qty_chk_idt_cd').val('011');
	
	ajaxCall("/StandardComplianceRateTrend/loadChartNonStandardComplianceRateTrend",
			$("#submit_form"),
			"POST",
			callback_LoadChartNonStandardComplianceRateTrend);
}

var callback_LoadChartStandardComplianceRateTrendTotal = function(result) {
//	chart_callback(result, chartStandardComplianceRateTrendTotal);
	createChartStandardComplianceRateTrendTotal(result);
};

var callback_LoadStatusByWork = function(result) {
	var data = JSON.parse(result);
	var row = data.rows[0];
	
	if(row == null || row == undefined) {
		initializeStatusByWork();
		return;
	}
	
	$('#last_program_cnt').textbox('setValue', row.last_program_cnt);
	$('#last_tot_err_cnt').textbox('setValue', row.last_tot_err_cnt);
	$('#last_cpla_rate').textbox('setValue', reviseData(row.last_cpla_rate) + "%");
	$('#incre_sql_cnt').textbox('setValue', row.incre_sql_cnt);
	applyArrowPoint($('#incre_sql_cnt_point'), row.incre_cnt_sign);
	$('#incre_err_cnt').textbox('setValue', row.incre_err_cnt);
	applyArrowPoint($('#incre_err_cnt_point'), row.incre_err_sign);
	$('#incre_rate').textbox('setValue', reviseData(row.incre_rate) + "%");
	applyArrowPoint($('#incre_rate_point'), row.incre_rate_sign);
	$('#last_sql_std_gather_day').textbox('setValue', row.last_sql_std_gather_day);
	$('#pre_sql_std_gather_day').textbox('setValue', row.pre_sql_std_gather_day);
}

function applyArrowPoint(attr_id, sign) {
	attr_id.removeClass('downArrow');
	attr_id.removeClass('upArrow');
	
	switch(sign) {
	case "-1":
		attr_id.addClass('downArrow');
		break;
	case "1":
		attr_id.addClass('upArrow');
		break;
	case "0": default:
		attr_id.removeClass('downArrow');
		attr_id.removeClass('upArrow');
		break;
	}
}

function reviseData(data) {
	if(data.indexOf(".") == 0) {
		data = "0" + data;
	}
	
	return data;
}

var callback_LoadChartStatusByWork = function(result) {
//	chart_callback(result, chartWorkStatus);
	createChartWorkStatus(result);
};

var callback_LoadChartNonComplianceStatus = function(result) {
//	chart_callback(result, chartNonComplianceStatus);
	createChartNonComplianceStatus(result);
};

var callback_LoadChartStandardComplianceRateTrend = function(result) {
	var store;
	var data;
	var chart = this.chartStandardComplianceRateTrend.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartStandardComplianceRateTrend(null);
			
			if($('#submit_form #parameter_list').val().indexOf('wrkjob_cd') > 0) {
				$('#titleChartStandardComplianceRateTrend').html("업무별 표준 준수율 추이(" + $('#submit_form #wrkjob_cd_nm').val() + ")");
			} else {
				$('#titleChartStandardComplianceRateTrend').html("업무별 표준 준수율 추이(전체)");
			}
		} catch(err) {
			console.log("callback_LoadChartStandardComplianceRateTrend error:" + err.message);
		}
		
		return;
	}
	
	if(result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			nameArray.push({
				fill : false,
				type : 'line',
				style : {
					opacity: 0.8,
					'stroke-width': 1
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'sql_std_gather_day',
				yField : post.wrkjob_cd_nm,
				title : post.wrkjob_cd_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartStandardComplianceRateTrend.down("chart").getStore();
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #parameter_list').val().indexOf('wrkjob_cd') > 0) {
			$('#titleChartStandardComplianceRateTrend').html("업무별 표준 준수율 추이(" + $('#submit_form #wrkjob_cd_nm').val() + ")");
		} else {
			$('#titleChartStandardComplianceRateTrend').html("업무별 표준 준수율 추이(전체)");
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_LoadChartNonStandardComplianceRateTrend = function(result) {
	var store;
	var data;
	var chart = this.chartNonStandardComplianceRateTrend.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		createChartNonStandardComplianceRateTrend(null);
		
		if($('#submit_form #parameter_list').val().indexOf('wrkjob_cd') > 0) {
			$('#titleChartStandardComplianceRateTrend').html("품질지표별 미준수 추이(" + $('#submit_form #wrkjob_cd_nm').val() + ")");
		} else {
			$('#titleChartStandardComplianceRateTrend').html("품질지표별 미준수 추이(전체)");
		}
		
		return;
	}
	
	if(result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			nameArray.push({
				fill : false,
				type : 'line',
				style : {
					opacity: 0.8,
					'stroke-width': 1
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				type : 'line',
				xField : 'sql_std_gather_day',
				yField : post.qty_chk_idt_nm,
				title : post.qty_chk_idt_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartNonStandardComplianceRateTrend.down("chart").getStore();
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #parameter_list').val().indexOf('wrkjob_cd') > 0) {
			$('#titleChartNonStandardComplianceRateTrend').html("품질지표별 미준수 추이(" + $('#submit_form #wrkjob_cd_nm').val() + ")");
		} else {
			$('#titleChartNonStandardComplianceRateTrend').html("품질지표별 미준수 추이(전체)");
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function Btn_Test() {
//	console.log("test");
	showChartPopup();
}

function Btn_Test2() {
//	console.log("test");
	showChartPopup();
}

function showPopupWindows() {
	var parameter_list = "project_id:" + $("#project_id").combobox('getValue') + ",sql_std_qty_div_cd:" + $('#sql_std_qty_div_cd').combobox('getValue') +
						 ",start_day:" + $('#strStartDt').val() + ",end_day:" + $('#strEndDt').val() +
						 ",sql_std_qty_scheduler_no:"+$('#sql_std_qty_scheduler_no').combobox('getValue');
	$('#dblPopup_form #parameter_list').val(parameter_list);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#dblPopup').window("open");
//	ajaxCallLoadChartStatusByWorkDbl();
//	$('#dblPopup_form #dblPopupChart').html($('#submit_form #chartWorkStatus').html());
}

function showChartPopup(items) {
	/*************************************************
	 * Ext 환경에서 팝업을 띄우는 방식
	 * 문제점 
	 * 1. 원래 화면인 "업무별 현황"의 챠트가 사라지는 문제점 발생
	 * 2. 띄워진 팝업에서 더블클릭 이벤트가 동작되는 문제
	 *************************************************/
	try {
		var chartPopup;
		var popupItems = chartWorkStatus;
		var chart = chartWorkStatus.down("chart");
		
		if(chart == null) {
			return;
		}
		
		var _docHeight = (document.height !== undefined) ? document.height : document.body.offsetHeight;
		var _docWidth = (document.width !== undefined) ? document.width : document.body.offsetWidth;
		
		console.log("height:" + _docHeight + " width:" + _docWidth);
		
		chartPopup = Ext.create('Ext.window.Window', {
			x:0,
			y:0,
			width:_docWidth,
			height:_docHeight,
//			renderTo : Ext.get('dblPopupChart'),
//			renderTo : document.getElementById('chartWorkStatus'),
			renderTo : document.getElementById($('#dblPopup_form #dblPopupChart')),
			title: $('#titleChartWorkStatus').html(),
			layout: 'fit',
			items: chart
			
		}).show();
	} catch(error) {
		console.log("error:" + error.message);
	}
	
	flag = 0 ;
	chartPopup.on('close', function(){
			flag = 1;
			reLoadChartWorkStatus();
//			alert('After close = '+chartPopup);
	});
}

function reLoadChartWorkStatus() {
	createChartWorkStatus();
	ajaxCallLoadChartStatusByWork();
}
