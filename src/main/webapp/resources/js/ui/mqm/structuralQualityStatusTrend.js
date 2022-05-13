var chartNonCompliantStandardizationRateTrend; //첫번째
var chartStatusByModel;  //두번째
var chartStandardizationRateStatusByModel; //세번째
var chartNonComplianceStatus; //네번째
var chartNumberNonCompliantByModel; // 다섯번째
var chartStandardComplianceRateTrend; // 여섯번째

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createChartNonCompliantStandardizationRateTrend();
	createChartStatusByModel();
	createChartStandardizationRateStatusByModel();
	createChartNonComplianceStatus();
	createChartNumberNonCompliantByModel();
	createChartStandardComplianceRateTrend();
	
	$('#project_nm').textbox({
		editable:false,
		icons:[{
			iconCls:'icon-search',
			handler:function() {
				Btn_ShowProjectList();
			}
		}]
	});
	
	
	initializeStatusByWork();
});

function initializeStatusByWork() {
//	$("label[for='incre_ent_cnt_text']").text('엔터티 증감');
//	$("label[for='incre_att_cnt_text']").text('속성 증감');
//	$("label[for='incre_std_rate_text']").text('표준화율 증감');
//	$("label[for='incre_err_cnt_text']").text('비표준 속성 증감');
	
	$('#last_ent_cnt').textbox('setValue', '0');
	$('#last_att_cnt').textbox('setValue', '0');
	$('#last_err_101_cnt').textbox('setValue', '0');
	$('#last_std_rate').textbox('setValue', '0%');
	$('#incre_ent_cnt').textbox('setValue', '0');
	$('#incre_att_cnt').textbox('setValue', '0');
	$('#incre_std_rate').textbox('setValue', '0%');
	$('#incre_err_cnt').textbox('setValue', '0');
	
	$('#last_extrac_dt').textbox('setValue', '');
	$('#pre_extrac_dt').textbox('setValue', '');
	
	$('#incre_ent_cnt_sign').removeClass('downArrow2');
	$('#incre_ent_cnt_sign').removeClass('upArrow2');
	$('#incre_att_cnt_sign').removeClass('downArrow2');
	$('#incre_att_cnt_sign').removeClass('upArrow2');
	$('#incre_std_rate_sign').removeClass('downArrow2');
	$('#incre_std_rate_sign').removeClass('upArrow2');
	$('#incre_err_sign').removeClass('downArrow2');
	$('#incre_err_sign').removeClass('upArrow2');
	
	$('#last_ent_cnt').textbox('textbox').css('font-weight', 600);
	$('#last_att_cnt').textbox('textbox').css('font-weight', 600);
	$('#last_err_101_cnt').textbox('textbox').css('font-weight', 600);
	$('#last_std_rate').textbox('textbox').css('font-weight', 600);
	$('#incre_ent_cnt').textbox('textbox').css('font-weight', 600);
	$('#incre_att_cnt').textbox('textbox').css('font-weight', 600);
	$('#incre_std_rate').textbox('textbox').css('font-weight', 600);
	$('#incre_err_cnt').textbox('textbox').css('font-weight', 600);
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
	
	$("#project_id").val(row.project_id);
	
	settingComboBox(row.project_id);

}
function settingComboBox(project_id){
	// 구조품질집계표 - 라이브러리명
	$('#submit_form #lib_nm').combobox({
	    url:"/Mqm/GetOpenqErrCntLibNm?project_id="+project_id,
	    method:"get",
	    textField:'lib_nm',
	    valueField:'lib_cd',
		onLoadError: function(){
			parent.$.messager.alert('','라이브러리명 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
}

//첫번째 차트
function createChartNonCompliantStandardizationRateTrend(result){
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
	
	if(chartNonCompliantStandardizationRateTrend != "undefined" && chartNonCompliantStandardizationRateTrend != undefined){
		chartNonCompliantStandardizationRateTrend.destroy();
	}
	
	Ext.define("chartNonCompliantStandardizationRateTrend.colors", {	// Label 색상 정의
		singleton:  true,
		ERR_CNT: '#5e9cd5',				// 미준수건수
		STD_RATE: '#f0790f',			// 표준화율
	});
	
	chartNonCompliantStandardizationRateTrend = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartNonCompliantStandardizationRateTrend"),
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
			insetPadding : '3 5 0 3', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['err_cnt'],	// 미준수 건수
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
					y : 0
				},
				fields: ['std_inspect_day']
			},{
				type: 'numeric',
				position: 'right',
				minimum: 0,
				maximum: 100,
				fields: ['std_rate']		// 진행률
			}],
			series : [{
				type : 'bar',
				fill : false,
				style : {
					opacity: 0.8,
					minGapWidth: 10,
				},
/*				marker: {
					radius: 2,
					lineWidth: 2
				},*/
				xField : 'std_inspect_day',
				yField : ['err_cnt'],
				stacked: false,
				title: '',
				colors: [chartNonCompliantStandardizationRateTrend.colors.ERR_CNT],
				label: {
					field: ['err_cnt'],
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				},
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
				xField : 'std_inspect_day',
				yField : 'std_rate',
				title: '',
				colors: [chartNonCompliantStandardizationRateTrend.colors.STD_RATE],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = item.field;
						
						tooltip.setHtml("[표준화율] " + xField + " : " + yField);
					}
				}
			}]
		}]
	});
}

//두번째 차트
function createChartStatusByModel(result){
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
	
	if(chartStatusByModel != "undefined" && chartStatusByModel != undefined){
		chartStatusByModel.destroy();
	}
	
	Ext.define("chartStatusByModel.colors", {	// Label 색상 정의
		singleton:  true,
		LAST_ERR_CNT: '#558fd1',		// 금회차 미준수본수
		LAST_ERR_101_CNT: '#f0790f',		// 금회차 준수율
		LAST_STD_RATE: '#ffcc30',		// 전회차 준수율
	});
	
	chartStatusByModel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartStatusByModel"),
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
				fields: ['last_err_cnt','last_err_101_cnt'],	// 미준수건수, 비표준속성수
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
				fields: ['model_nm']
			},{
				type: 'numeric',
				position: 'right',
				fields: ['last_std_rate'],		// 표준화율
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
				xField : 'model_nm',
				yField : ['last_err_cnt'],
				stacked: false,
				title: '',
				colors: [chartStatusByModel.colors.LAST_ERR_CNT, chartStatusByModel.colors.LAST_ERR_101_CNT],
				label: {
					field: ['last_err_cnt'],
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.field);
						var model_nm = record.get('model_nm');
						
						tooltip.setHtml("[" + model_nm + "] 미준수 건수 : " + yField);
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
					'stroke-width': 2
				},
				xField : 'model_nm',
				yField : 'last_err_101_cnt',
				title: '',
				colors: [chartStatusByModel.colors.LAST_ERR_101_CNT],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var model_nm = record.get('model_nm');
						tooltip.setHtml("[" + model_nm + "] 비표준 속성수 : " + yField);
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
				xField : 'model_nm',
				yField : 'last_std_rate',
				title: '',
				colors: [chartStatusByModel.colors.LAST_STD_RATE],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
//						var xField = record.get(item.series.getXField());
						var model_nm = record.get('model_nm');
						var title = item.field;
						
						tooltip.setHtml("[" + model_nm + "] 금회차 표준화율 : " + yField);
					}
				}
			}],
			listeners: {
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					
					$('#submit_form #model_nm').val(data.model_nm);
					
					ajaxCallChartNonComplianceStatus(); //4번째
					ajaxChartNumberNonCompliantByModel();//5번째
					ajaxChartStandardComplianceRateTrend();//6번째
				},
	            itemmouseover: function(series, item, event, eOpts) {
		               series.el.dom.style.cursor = 'pointer';
		            },
	            itemmouseout: function(series, item, event, eOpts) {
	               series.el.dom.style.cursor = '';
	            }
			}
		}]
	});
}

//세번째차트
function createChartStandardizationRateStatusByModel(result){
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
	
	if(chartStandardizationRateStatusByModel != "undefined" && chartStandardizationRateStatusByModel != undefined){
		chartStandardizationRateStatusByModel.destroy();
	}
	
	Ext.define("chartStandardizationRateStatusByModel.colors", {	// Label 색상 정의
		singleton:  true,
		PRE_STD_RATE: '#558fd1',		// 전회차 표준화율
		LAST_STD_RATE: '#f0790f',		// 금회차 표준화율
//		LAST_ERR_101_CNT: '#ffcc30',		// 101속성비표준
//		LAST_ERR_101_CNT: '#E3181B',		// 101속성비표준
		LAST_ERR_101_CNT: '#ff0000',		// 101속성비표준
	});
	
	//세번째차트
	chartStandardizationRateStatusByModel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartStandardizationRateStatusByModel"),
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
				fields: ['pre_std_rate','last_std_rate'],	// 전회차 표준화율,금회차표준화율
				adjustByMajorUnit: true,
				minorTickSteps: 10,
				minimum: 0,
				maximum: 100,
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
				fields: ['model_nm']
			},{
				type: 'numeric',
				position: 'right',
				fields: ['last_err_101_cnt'],		// 101속성비표준
				fixedAxisWidth: 1,
				minimum: 0
//				maximum: 5000
			}],
			series : [{
				type : 'bar',
				style: {
					opacity: 0.5,
					minGapWidth: 10,
				},
				xField : 'model_nm',
				yField : ['pre_std_rate','last_std_rate'],
				stacked: false,
				title: '',
				colors: [chartStandardizationRateStatusByModel.colors.PRE_STD_RATE, chartStandardizationRateStatusByModel.colors.LAST_STD_RATE],
//				label: {
//					field: ['pre_std_rate','last_std_rate'],
//					display: 'insideEnd',
//					orientation: 'horizontal',
//					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
//				},
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.field);
						var model_nm = record.get('model_nm');
						var title = item.field == 'pre_std_rate' ? '전회차 표준화율' : '금회차 표준화율';

						tooltip.setHtml("[" + model_nm + "] " + title + " : " + yField);
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
					'stroke-width': 2
				},
				xField : 'model_nm',
				yField : 'last_err_101_cnt',
				title: '',
				colors: [chartStandardizationRateStatusByModel.colors.LAST_ERR_101_CNT],
				tooltip: {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var model_nm = record.get('model_nm');
						tooltip.setHtml("[" + model_nm + "] 101속성비표준 : " + yField);
					}
				}
			}],
		}]
	});
}

//네번째 차트
function createChartNonComplianceStatus(result){
	let data;
	if(result != null && result != undefined){
		try{
			data = JSON.parse(result);
			data = data.rows;
			
			if($('#submit_form #model_nm').val() != null && $('#submit_form #model_nm').val() != '') {
				$('#titleChartNonComplianceStatus').html("품질지표별 미준수 현황(" + $('#submit_form #model_nm').val() + ")");
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
//		ERR_CNT: '#5e9cd5'
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
			innerPadding : '3 5 3 0', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : '5 30 5 5', // 차트 밖 여백

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


//다섯번째 차트
function createChartNumberNonCompliantByModel(result) {
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
	
	if(chartNumberNonCompliantByModel != "undefined" && chartNumberNonCompliantByModel != undefined){
		chartNumberNonCompliantByModel.destroy();		
	}
	
	chartNumberNonCompliantByModel = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartNumberNonCompliantByModel"),
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
//			insetPadding : '0 0 15 5', // 차트 밖 여백
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : false,
//				minimum: 0,
//				minorTickSteps: 0,
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
//여섯번째 차트
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
//				minimum: 0,
				maximum:100,
//				minorTickSteps: 0,
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

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#submit_form #strStartDt').val($("#startDate").textbox("getValue"));
	$('#submit_form #strEndDt').val($("#endDate").textbox("getValue"));
	
	if($("#project_nm").val() == '') {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	if(!compareAnBDate($('#startDate').textbox('getValue'), $('#endDate').textbox('getValue'))) {
		var msg = "기준 일자를 확인해 주세요.<br>시작일자[" + $('#startDate').textbox('getValue') + "] 종료일자[" + $('#endDate').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	ajaxCallData();
}

//검색
function ajaxCallData() {
	
	$("#submit_form #model_nm").val('');
	ajaxCallChartchartNonCompliantStandardizationRateTrend();
	ajaxCallStatusByModelAll();
	ajaxChartStatusByModel();
	ajaxCallChartStandardizationRateStatusByModel();
	ajaxCallChartNonComplianceStatus();
	ajaxChartNumberNonCompliantByModel();
	ajaxChartStandardComplianceRateTrend();
}

function ajaxCallChartchartNonCompliantStandardizationRateTrend() {
	
	ajaxCall("/Mqm/ChartNonCompliantStandardizationRateTrend",
			$("#submit_form"),
			"POST",
			callback_ChartNonCompliantStandardizationRateTrend);
}

function ajaxCallStatusByModelAll() {
	
	ajaxCall("/Mqm/StatusByModelAll",
			$("#submit_form"),
			"POST",
			callback_StatusByModelAll);
}

function ajaxChartStatusByModel() {
	
	ajaxCall("/Mqm/ChartStatusByModel",
			$("#submit_form"),
			"POST",
			callback_ChartStatusByModel);
}

function ajaxCallChartStandardizationRateStatusByModel() {
	
	ajaxCall("/Mqm/ChartStandardizationRateStatusByModel",
			$("#submit_form"),
			"POST",
			callback_ChartStandardizationRateStatusByModel);
}
function ajaxCallChartNonComplianceStatus() {
	
	ajaxCall("/Mqm/ChartNonComplianceStatus",
			$("#submit_form"),
			"POST",
			callback_ChartNonComplianceStatus);
}

//다섯번째 차트
function ajaxChartNumberNonCompliantByModel() {
	
	ajaxCall("/Mqm/ChartNumberNonCompliantByModel",
			$("#submit_form"),
			"POST",
			callback_ChartNumberNonCompliantByModel);
}

//여섯번째 차트
function ajaxChartStandardComplianceRateTrend() {
	
	ajaxCall("/Mqm/ChartStandardComplianceRateTrend",
			$("#submit_form"),
			"POST",
			callback_ChartStandardComplianceRateTrend);
}


var callback_StatusByModelAll = function(result) {
	var data = JSON.parse(result);
	var row = data.rows[0];
	
	if(row == null || row == undefined) {
		initializeStatusByWork();
		return;
	}
	
	$('#last_ent_cnt').textbox('setValue', reviseData(row.last_ent_cnt));  //엔터티수
	$('#last_att_cnt').textbox('setValue', reviseData(row.last_att_cnt)); //속성수
	$('#last_err_101_cnt').textbox('setValue', reviseData(row.last_err_101_cnt)); //비표준속성수
//	$('#last_std_rate').textbox('setValue', row.last_std_rate); //표준화율
	$('#last_std_rate').textbox('setValue', reviseData(row.last_std_rate) + "%"); //표준화율
	
	applyArrowPoint($('#incre_ent_cnt_sign'), row.incre_ent_cnt_sign);
	$('#incre_ent_cnt').textbox('setValue', reviseData(row.incre_ent_cnt));
	
	applyArrowPoint($('#incre_att_cnt_sign'), row.incre_att_cnt_sign);
	$('#incre_att_cnt').textbox('setValue', reviseData(row.incre_att_cnt));
	
	applyArrowPoint($('#incre_std_rate_sign'), row.incre_std_rate_sign);
	$('#incre_std_rate').textbox('setValue', reviseData(row.incre_std_rate) + "%");
	
	applyArrowPoint($('#incre_err_sign'), row.incre_err_sign);
	$('#incre_err_cnt').textbox('setValue', reviseData(row.incre_err_cnt));
}

function applyArrowPoint(attr_id, sign) {
	switch(sign) {
	case "-1":
		attr_id.removeClass('upArrow2');
		attr_id.addClass('downArrow2');
		break;
	case "1":
		attr_id.removeClass('downArrow2');
		attr_id.addClass('upArrow2');
		break;
	case "0": default:
		attr_id.removeClass('downArrow2');
		attr_id.removeClass('upArrow2');
		break;
	}
}

function reviseData(data) {
	if(data == undefined) {
		data = 0;
	} else if(data.indexOf(".") == 0) {
		data = "0" + data;
	}
	
	return data;
}

var callback_ChartStatusByModel = function(result) {
//	chart_callback(result, chartStatusByModel);
	createChartStatusByModel(result);
};

var callback_ChartNonCompliantStandardizationRateTrend = function(result) {
//	chart_callback(result, chartStandardizationRateStatusByModel);
	createChartNonCompliantStandardizationRateTrend(result);
};

function reviseDate(data) {
	if(data == undefined) {
		data = "N/A";
	}
	
	return data;
}

var callback_ChartStandardizationRateStatusByModel = function(result) {
//	chart_callback(result, chartStandardizationRateStatusByModel);
	
	var data = JSON.parse(result);
	var row = data.rows[0];
	
	if(row == null || row == undefined) {
		initializeStatusByWork();
		createChartStandardizationRateStatusByModel(null);
		return;
	}
//	console.log("확인:::",row);
	$('#extrac_dt').val(row.last_extrac_dt);

	$('#last_extrac_dt').textbox('setValue', reviseDate(row.last_extrac_dt));
	$('#pre_extrac_dt').textbox('setValue', reviseDate(row.pre_extrac_dt));
	
	createChartStandardizationRateStatusByModel(result);
};

var callback_ChartNonComplianceStatus = function(result) {
//	chart_callback(result, chartStandardizationRateStatusByModel);
	createChartNonComplianceStatus(result);
};

//다섯번째 차트
var callback_ChartNumberNonCompliantByModel = function(result) {
	var store;
	var data;
	var chart = this.chartNumberNonCompliantByModel.down("chart");
	var nameArray = [];
//	console.log(result.txtValue);

	if(result.result == false) {
		try {
			createChartNumberNonCompliantByModel(null);
			
			if($('#submit_form #model_nm').val() != null && $('#submit_form #model_nm').val() != '') {
				$('#titleChartNumberNonCompliantByModel').html("모델별 미준수 건수 추이(" + $('#submit_form #model_nm').val() + ")");
			} else {
				$('#titleChartNumberNonCompliantByModel').html("모델별 미준수 건수 추이(전체)");
			}
		} catch(err) {
			console.log("callback_ChartNumberNonCompliantByModel error:" + err.message);
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
				xField : 'std_inspect_day',
				yField : post.model_nm,
				title : post.model_nm,
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
		store = chartNumberNonCompliantByModel.down("chart").getStore();
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #model_nm').val() != null && $('#submit_form #model_nm').val() != '') {
			$('#titleChartNumberNonCompliantByModel').html("모델별 미준수 건수 추이(" + $('#submit_form #model_nm').val() + ")");
		} else {
			$('#titleChartNumberNonCompliantByModel').html("모델별 미준수 건수 추이(전체)");
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

//여섯번째 차트
var callback_ChartStandardComplianceRateTrend = function(result) {
	var store;
	var data;
	var chart = this.chartStandardComplianceRateTrend.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartStandardComplianceRateTrend(null);	
			if($('#submit_form #model_nm').val() != null && $('#submit_form #model_nm').val() != '') {
				$('#titleChartStandardComplianceRateTrend').html("표준 준수율 추이(" + $('#submit_form #model_nm').val() + ")");
			} else {
				$('#titleChartStandardComplianceRateTrend').html("표준 준수율 추이(전체)");
			}
		} catch(err) {
			console.log("callback_ChartStandardComplianceRateTrend error:" + err.message);
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
				xField : 'std_inspect_day',
				yField : post.model_nm,
				title : post.model_nm,
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
		
		if($('#submit_form #model_nm').val() != null && $('#submit_form #model_nm').val() != '') {
			$('#titleChartStandardComplianceRateTrend').html("표준 준수율 추이(" + $('#submit_form #model_nm').val() + ")");
		} else {
			$('#titleChartStandardComplianceRateTrend').html("표준 준수율 추이(전체)");
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};
