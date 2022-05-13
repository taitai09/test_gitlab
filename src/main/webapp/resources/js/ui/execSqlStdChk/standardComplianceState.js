const emptyData = JSON.stringify({result:true, rows: []});
var countChart;
var ratioChart;
var countByIndexChart;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createProjectCombo();
	createSchedulerCombo();
	
	createCountChart();
	createRatioChart();
	createCountByIndexChart();
});	//end of document ready

function createProjectCombo(){
	$('#project_combo').combobox({
		url:'/sqlStandardOperationDesign/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onChange: function(newValue, oldValue){
			loadSchedulerCombo(newValue);
		},
		onLoadSuccess: function() {
			if( $('#call_from_parent').val() ){
				$(this).combobox('setValue', $('#project_id_inherited').val());
			}
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function createSchedulerCombo(){
	$('#scheduler_no').combobox({
		valueField:'sql_std_qty_scheduler_no',
		textField:'job_scheduler_nm',
		onLoadSuccess: function() {
			let scheduler_no_inherited = $('#scheduler_no_inherited').val();
			if( $('#call_from_parent').val() && scheduler_no_inherited ){
				$(this).combobox('setValue', scheduler_no_inherited);
				
				Btn_OnClick();
			}
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function loadSchedulerCombo(project_id){
	$('#scheduler_no').combobox('clear');
	$('#scheduler_no').combobox('loadData',[]);
	
	$('#project_id').val( project_id );
	
	ajaxCall_loadSchedulerCombo();
}

function ajaxCall_loadSchedulerCombo(){
	ajaxCall('/execSqlStdChk/loadSchedulerCombo', 
			$("#submit_form"), 
			'GET',
			callback_loadSchedulerCombo);
}

var callback_loadSchedulerCombo = function(result){
	try {
		let data = JSON.parse(result);
		$('#scheduler_no').combobox('loadData', data);
		
	} catch(err) {
		console.log(err.message);
	}
}

function createCountChart(result){
	let data = [];
	let labelPosition = 'horizontal';
	
	if( result ){
		try{
			data = JSON.parse(result);
			data = data.rows;
			
			if( data.length > 20 ){
				labelPosition = 'vertical';
			}
			
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}
	
	if(countChart != "undefined" && countChart != undefined){
		countChart.destroy();
	}
	
	Ext.define("countChart.colors", {	// Label 색상 정의
		singleton: true,
		totalErrCnt: '#4472C4'
	});
	
	countChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("countChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			captions: {
				title: {
					text: "표준 미준수 본수",
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 18px Arial',
						fill:"#000000"
					}
				}
			},
			border : false,
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			type:'text',
			innerPadding : '20 0 0 0', // 차트안쪽 여백
			insetPadding : '20 3 5 4', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['TOT_ERR_CNT'],
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
				fields: ['SQL_STD_GATHER_DAY']
			}],
			series : {
				type : 'bar',
				style: {
					opacity: 0.8,
					minGapWidth: 10,
				},
				xField : 'SQL_STD_GATHER_DAY',
				yField : 'TOT_ERR_CNT',
				stacked: false,
				title: '표준 미준수 본수',
				colors: [countChart.colors.totalErrCnt],
				label: {
					field: 'TOT_ERR_CNT',
					display: 'outside',
					orientation: labelPosition,
					font: '10px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				},
			},
			listeners: {
				itemmouseover: function(series, item, event, eOpts) {
					series.el.dom.style.cursor = 'pointer';
				},
				itemmouseout: function(series, item, event, eOpts) {
					series.el.dom.style.cursor = '';
				},
				itemclick: function (chart, item, event) {
					$('#sql_std_gather_day').val( item.record.data.SQL_STD_GATHER_DAY );
					chart_callback(emptyData, countByIndexChart);
					
					ajaxCall_LoadCountByIndex();
				}
			}
		}]
	});
}

function createRatioChart(result){
	let data = [];
	let labelPosition = 'horizontal';
	
	if( result ){
		try{
			data = JSON.parse(result);
			data = data.rows;
			
			if( data.length > 20 ){
				labelPosition = 'vertical';
			}
			
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}
	
	if(ratioChart != "undefined" && ratioChart != undefined){
		ratioChart.destroy();
	}
	
	Ext.define("ratioChart.colors", {
		singleton: true,
		comply: '#4472C4',
		notComply: '#ED7D31'
	});
	
	ratioChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("ratioChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			captions: {
				title: {
					text: "표준 준수율",
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 18px Arial',
						fill:"#000000"
					}
				}
			},
			border : false,
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			legend : {
				type: 'sprite',
				docked : 'bottom'
			},
			type:'text',
			innerPadding : '3 0 0 0',	// 차트 안 여백
			insetPadding : '20 3 5 4',	// 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['CPLA_RATE', 'NON_CPLA_RATE'],
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
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields: ['SQL_STD_GATHER_DAY']
			}],
			series : {
				type : 'bar',
				style: {
					opacity: 0.8,
					minGapWidth: 10,
				},
				xField : 'SQL_STD_GATHER_DAY',
				yField : ['CPLA_RATE', 'NON_CPLA_RATE'],
				stacked: true,
				title: ['준수율', '미준수율'],
				colors: [ratioChart.colors.comply, ratioChart.colors.notComply],
				label: {
					field: ['CPLA_RATE', 'NON_CPLA_RATE'],
					display: 'insideEnd',
					orientation: labelPosition,
					font: '10px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif'
				}
			}
		}]
	});
}

function createCountByIndexChart(result){
	let data = [];
	
	if( result ){
		try{
			data = JSON.parse(result);
			data = data.rows;
			
		}catch(e){
			parent.$.messager.alert('',e.message);
		}
	}
	
	if(countByIndexChart != "undefined" && countByIndexChart != undefined){
		countByIndexChart.destroy();
	}
	
	Ext.define("countByIndexChart.colors", {
		singleton: true,
		errCnt: '#4472C4'
	});
	
	countByIndexChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		layout : 'fit',
		border : false,
		renderTo : document.getElementById("countByIndexChart"),
		items : [{
			xtype : 'cartesian',
			flipXY : true,
			captions: {
				title: {
					text: '품질지표별 미준수 본수('+ $('#sql_std_gather_day').val() +')',
					align: 'center',
					style: {
						color: "#000000",
						font: 'bold 18px Arial',
						fill:"#000000"
					}
				}
			},
			border : false,
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			type:'text',
			innerPadding : '3 35 0 0', // 차트안쪽 여백
			insetPadding : '20 0 5 4', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'category',
				position : 'left',
				fields: ['QTY_CHK_IDT_NM'],
				label : {
					x : 0,
					y : 0,
					textAlign : 'right'
				},
			},{
				type : 'numeric',
				position : 'bottom',
				fields: 'ERR_CNT',
				minorTickSteps: 0,
				minimum: 0,
				grid: {
					odd: {
						opacity: 0.5,
						stroke: '#bbb',
						lineWidth: 1
					}
				}
			}],
			series : {
				xField : 'QTY_CHK_IDT_NM',
				yField : 'ERR_CNT',
				stacked: false,
				type : 'bar',
				style: {
					opacity: 0.8,
					minGapWidth: 10,
				},
				title: '',
				colors: [countByIndexChart.colors.errCnt],
				label: {
					field: 'ERR_CNT',
					display: 'outside',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}
		}]
	});
}

function Btn_OnClick(){
	if( formValidationCheck() ){
		chart_callback(emptyData, countChart);
		chart_callback(emptyData, ratioChart);
		chart_callback(emptyData, countByIndexChart);
		
		ajaxCall_LoadStdComplianceState();
	}
	$('#call_from_parent').val('');
}


function ajaxCall_LoadStdComplianceState(){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 준수 현황 조회'," ");
	
	ajaxCall("/execSqlStdChk/loadStdComplianceState",
			$("#submit_form"),
			"GET",
			callback_LoadStdComplianceState);
}

var callback_LoadStdComplianceState = function(result){
	try{
		if( result ){
			let data = JSON.parse(result);
			let total = data.totalCount
			
			if( total > 0 ){
				$('#sql_std_gather_day').val(data.rows[total-1].SQL_STD_GATHER_DAY);
				ajaxCall_LoadCountByIndex();
			}
			
			createCountChart( result );
			createRatioChart( result );
		}
		
	} catch(err) {
		console.error(err.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function ajaxCall_LoadCountByIndex(){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 준수 현황 조회'," ");
	
	ajaxCall("/execSqlStdChk/loadCountByIndex",
			$("#submit_form"),
			"GET",
			callback_LoadCountByIndex);
}

var callback_LoadCountByIndex = function(result){
	try{
		createCountByIndexChart( result );
		
	} catch(err) {
		console.error(err.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function formValidationCheck(){
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	if ( $('#scheduler_no').combobox('getValue') == '' ) {
		warningMessager('스케줄러를 선택해 주세요.');
		return false;
	}
	
	let startDay = $('#start_day').datebox('getText');
	if ( startDay == '' ) {
		warningMessager('기준일자를 입력해 주세요.');
		return false;
	}
	
	let endDay = $('#end_day').datebox('getText');
	if ( endDay == '' ) {
		warningMessager('기준일자를 입력해 주세요.');
		return false;
	}
	
	if ( compareAnBDate(startDay, endDay) == false ){
		warningMessager('시작일과 종료일을 확인해 주세요.<br>start[' + startDay + '] end[' + endDay + ']');
		return false;
	}
	compareAnBDate(startDay, endDay);
	return true;
}