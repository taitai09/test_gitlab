var chartBG;
var chartET;
var chartPC;
var chartBGPie;
var chartBGBar;
var chartETPie;
var chartETBar;
var chartPCPie;
var chartPCBar;
var chartPCSql;
var chartEHSql;
var chartPCSqlPie;
var chartPCSqlBar;
var chartEHSqlPie;
var chartEHSqlBar;
var isFirstLoading = 'true';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#checkHighestRankYn').checkbox({
		onChange:function(val) {
			workjobComboTree(val);
		}
	});
	
	$('#checkHighestRankYn').checkbox('check');
	
	$('#begin_dt').datebox('clear');
	$('#end_dt').datebox('clear');
	
	$('#base_daily').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#base_weekly').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#base_monthly').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	resetBasePeriod(2);
	
//	getChartData();
});

function workjobComboTree(isTopLevel) {
	let workjobComboTreeUrl = '';
	
	if(isTopLevel) {
		workjobComboTreeUrl = '/Common/getWrkJobTopLevel';
	} else {
		workjobComboTreeUrl = '/Common/getWrkJobCd';
	}
	
//	$('#wrkjob_cd_top_level').combotree('clear');
	
	// 시스템 유형에 관계없이 업무구분 조회
	$('#wrkjob_cd_top_level').combotree({
		url:workjobComboTreeUrl,
		method:"get",
//		id:'id',
//		text:'text',
		valueField:'id',
		textField:'text',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(node, data){
			$('#wrkjob_cd').val('');
			$("#submit_form #wrkjob_cd_top_level").combotree("textbox").attr("placeholder",'선택');
			combotreeSetValue();
		},
		onChange: function(newValue, oldValue) {
			$('#wrkjob_cd').val(newValue);
		}
	});
}

function combotreeSetValue() {
	$('#wrkjob_cd_top_level').combotree('setValue', '');
}

function resetBasePeriod(base_period_value) {
	$('#base_period_value').val(base_period_value);
	
	switch(base_period_value) {
	case 1:
		$('#base_daily').radiobutton({
			checked: true
		});
		
		$('#base_weekly').radiobutton({
			checked: false
		});
		
		$('#base_monthly').radiobutton({
			checked: false
		});
		break;
	case 2:
		$('#base_daily').radiobutton({
			checked: false
		});
		
		$('#base_weekly').radiobutton({
			checked: true
		});
		
		$('#base_monthly').radiobutton({
			checked: false
		});
		break;
	case 3:
		$('#base_daily').radiobutton({
			checked: false
		});
		
		$('#base_weekly').radiobutton({
			checked: false
		});
		
		$('#base_monthly').radiobutton({
			checked: true
		});
		break;
	}
	
	if(isFirstLoading == 'true') {
		ajaxCall("/RunSqlPerformanceChangeTracking/baseDate",
				$("#submit_form"),
				"POST",
				callback_BaseDateAction);
	} else {
		ajaxCall("/RunSqlPerformanceChangeTracking/baseDatePeriodOnly",
				$("#submit_form"),
				"POST",
				callback_BaseDatePeriodOnlyAction);
	}
}

var callback_BaseDateAction = function(result) {
	let data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','기준일자를 조회하지 못하였습니다.');
		return false;
	}
	
	let row = data[0];
	
	$('#begin_dt').datebox('setValue', row.begin_dt);
	$('#end_dt').datebox('setValue', row.end_dt);
	
	getChartData();
	if(isFirstLoading == 'true') {
		isFirstLoading = 'false';
		
		search(false);
	}
}

var callback_BaseDatePeriodOnlyAction = function(result) {
	let data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','기준일자를 조회하지 못하였습니다.');
		return false;
	}
	
	let row = data[0];
	
	$('#begin_dt').datebox('setValue', row.begin_dt);
	$('#end_dt').datebox('setValue', row.end_dt);
}

function search(isClickEvent) {
	getChartData();
}

function Btn_OnClick(){
	if(!isValidationCheck()) {
		return;
	}
	
	search(true);
}

function isValidationCheck() {
	if( ($('#checkHighestRankYn').checkbox('options').checked == false) &&
			($('#wrkjob_cd_top_level').combotree('getValue') == '') ) {
		parent.$.messager.alert('경고','업무를 선택해 주세요','warning');
		return false;
	}
	
	if(!compareAnBDate($('#begin_dt').datebox('getValue'), $('#end_dt').datebox('getValue'))) {
		let msg = "기준일자를 확인해 주세요.<br>시작일자[" + $('#begin_dt').datebox('getValue') + "] 종료일자[" + $('#end_dt').datebox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	return true;
}

var northBarChartHeight = 300;
var southPieChartHeight = 150;
var southPieChartWidth = 250;
var southBarChartWidth = 500;
var nouthBarChartInsetPadding = '10 5 0 5';
var southBarChartInsetPadding = '10 0 0 30';
var redColor = '#ff0000';
var greenColor = '#92d050';
var blueColor = '#00b0f0';

function createBG(result){
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
	
	if(chartBG != "undefined" && chartBG != undefined){
		chartBG.destroy();
	}
	
	Ext.define("chartBG.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: blueColor,	// 일일 부적합률
		COLOR_02: redColor		// 일일 부적합
	});
	
	chartBG = Ext.create("Ext.panel.Panel",{
		width : '100%',
//		width : southBarChartWidth,
//		height : northBarChartHeight,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartBG"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '20 0 0 0',
			store : {
				data : data
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['buffer_gets_fail'],
				minimum: 0
			},{
				type : 'numeric',
				position : 'right',
				fields: ['buffer_gets_fail_ratio'],
				minimum: 0,
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'day'
			}],
			series : [{
				type : 'bar',
				xField : 'day',
				yField : 'buffer_gets_fail',
				title: '일일 부적합',
				colors: [chartBG.colors.COLOR_02],
				label: {
					field: 'buffer_gets_fail',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			},{
				type : 'line',
				style: {
					lineWidth: 3,
				},
				xField : 'day',
				yField : 'buffer_gets_fail_ratio',
				title: '일일 부적합률(%)',
				colors: [chartBG.colors.COLOR_01],
				label: {
					field: 'buffer_gets_fail_ratio',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
}

function createBGPie(result){
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
	
	if(chartBGPie != "undefined" && chartBGPie != undefined){
		chartBGPie.destroy();
	}
	
	let pass = 0, fail = 0;
	
	if ( data != null && data.length > 0 ){
		let dataLen = data.length;
		
		for(let index = 0; index < dataLen; index++) {
			pass += Number(data[index].buffer_gets_pass);
			fail += Number(data[index].buffer_gets_fail);
		}
	}
	
	Ext.define("chartBGPie.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,	// 적합
		COLOR_02: redColor		// 부적합
	});
	
	chartBGPie = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southPieChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartBGPie"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
			border : false,
			store : {
				fields: ['name', 'data1'],
//				data : data,
				data : [{
					name : '적합',
					data1 : pass
				},{
					name : '부적합',
					data1 : fail
				}]
			},
			legend : {
				type : 'dom'
			},
			series : [{
				type : 'pie',
				colors: [chartBGPie.colors.COLOR_01, chartBGPie.colors.COLOR_02],
				angleField : 'data1',
				label: {
					field : 'name',
					display: 'inside'
				},
				tooltip:{
					trackMouse:true,
					renderer:function(tooltip,record){
						tooltip.setHtml(record.data.name+"["+record.data.data1+"]");
					}
				},
				donut : 0
			}]
		}]
	});
}

function createBGBar(result){
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
	
	if(chartBGBar != "undefined" && chartBGBar != undefined){
		chartBGBar.destroy();
	}
	
	let improved = 0, regressed = 0;
	let dataLen = data.length;
	
	for(let index = 0; index < dataLen; index++) {
		improved += Number(data[index].buffer_gets_improve);
		regressed += Number(data[index].buffer_gets_regress);
	}
	
	if(dataLen == 0) {
		improved = null;
		regressed = null;
	}
	
	Ext.define("chartBGBar.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,
		COLOR_02: redColor
	});
	
	chartBGBar = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southBarChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartBGBar"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
//				data : data
				fields: ['name', 'value'],
				data: [{
					name : 'Improved',
					data1 : improved
				},{
					name : 'Regressed',
					data2 : regressed
				}]
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				fields: 'data1'
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'name'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'name',
				yField : ['data1','data2'],
				title: ['Improved','Regressed'],
				colors: [chartBGBar.colors.COLOR_01, chartBGBar.colors.COLOR_02],
				label: {
					field: ['data1','data2'],
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
}

function createET(result){
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
	
	if(chartET != "undefined" && chartET != undefined){
		chartET.destroy();
	}
	
	Ext.define("chartET.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: blueColor,	// 일일 부적합률
		COLOR_02: redColor		// 일일 부적합
	});
	
	chartET = Ext.create("Ext.panel.Panel",{
		width : '100%',
//		height : northBarChartHeight,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartET"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '20 0 0 0',
			store : {
				data : data
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['elapsed_time_fail'],
				minimum: 0
			},{
				type : 'numeric',
				position : 'right',
				fields: ['elapsed_time_fail_ratio'],
				minimum: 0,
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'day'
			}],
			series : [{
				type : 'bar',
				xField : 'day',
				yField : 'elapsed_time_fail',
				title: '일일 부적합',
				colors: [chartET.colors.COLOR_02],
				label: {
					field: 'elapsed_time_fail',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			},{
				type : 'line',
				style: {
					lineWidth: 3,
				},
				xField : 'day',
				yField : 'elapsed_time_fail_ratio',
				title: '일일 부적합률(%)',
				colors: [chartET.colors.COLOR_01],
				label: {
					field: 'elapsed_time_fail_ratio',
//					display: 'insideEnd',
					display: 'outside',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
}

function createETPie(result){
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
	
	if(chartETPie != "undefined" && chartETPie != undefined){
		chartETPie.destroy();
	}
	
	let pass = 0, fail = 0;
	
	if ( data != null && data.length > 0 ) {
		let dataLen = data.length;
		
		for(let index = 0; index < dataLen; index++) {
			pass += Number(data[index].elapsed_time_pass);
			fail += Number(data[index].elapsed_time_fail);
		}
	}
	
	Ext.define("chartETPie.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,	// 적합
		COLOR_02: redColor		// 부적합
	});
	
	chartETPie = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southPieChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartETPie"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
			border : false,
			store : {
				fields: ['name', 'data1'],
//				data : data,
				data : [{
					name : '적합',
					data1 : pass
				},{
					name : '부적합',
					data1 : fail
				}]
				,autoLoad : true
			},
			legend : {
				type : 'dom'
			},
			series : [{
				type : 'pie',
				colors: [chartETPie.colors.COLOR_01, chartETPie.colors.COLOR_02],
				angleField : 'data1',
				label: {
					field : 'name',
					display: 'inside'
				},
				tooltip:{
					trackMouse:true,
					renderer:function(tooltip,record){
						tooltip.setHtml(record.data.name+"["+record.data.data1+"]");
					}
				},
				donut : 0
			}]
		}]
	});
}

function createETBar(result){
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
	
	if(chartETBar != "undefined" && chartETBar != undefined){
		chartETBar.destroy();
	}
	
	let improved = 0, regressed = 0;
	let dataLen = data.length;
	
	for(let index = 0; index < dataLen; index++) {
		improved += Number(data[index].elapsed_time_improve);
		regressed += Number(data[index].elapsed_time_regress);
	}
	
	if(dataLen == 0) {
		improved = null;
		regressed = null;
	}
	
	Ext.define("chartETBar.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,
		COLOR_02: redColor
	});
	
	chartETBar = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southBarChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartETBar"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
//			insetPadding : nouthBarChartInsetPadding,
			store : {
//				data : data
				fields: ['name', 'value'],
				data: [{
					name : 'Improved',
					data1 : improved
				},{
					name : 'Regressed',
					data2 : regressed
				}]
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				fields: 'data1'
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'name'
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'name',
				yField : ['data1','data2'],
				title: ['Improved','Regressed'],
				colors: [chartETBar.colors.COLOR_01, chartETBar.colors.COLOR_02],
				label: {
					field: ['data1','data2'],
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
	
	$('#polar-1036-body').css('border-style', 'hidden');
}

function createPCSql(result){
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
	
	if(chartPCSql != "undefined" && chartPCSql != undefined){
		chartPCSql.destroy();
	}
	
	Ext.define("chartPCSql.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: blueColor,	// 일일 부적합률
		COLOR_02: redColor		// 일일 부적합
	});
	
	chartPCSql = Ext.create("Ext.panel.Panel",{
		width : '100%',
//		height : northBarChartHeight,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartPCSql"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '20 0 0 0',
			store : {
				data : data
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['fail'],
				minimum: 0
			},{
				type : 'numeric',
				position : 'right',
				fields: ['fail_ratio'],
				minimum: 0,
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'day'
			}],
			series : [{
				type : 'bar',
				xField : 'day',
				yField : 'fail',
				title: '일일 부적합',
				colors: [chartPCSql.colors.COLOR_02],
				label: {
					field: 'fail',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			},{
				type : 'line',
				style: {
					lineWidth: 3,
				},
				xField : 'day',
				yField : 'fail_ratio',
				title: '일일 부적합률(%)',
				colors: [chartPCSql.colors.COLOR_01],
				label: {
					field: 'fail_ratio',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
}

function createPCSqlPie(result){
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
	
	if(chartPCSqlPie != "undefined" && chartPCSqlPie != undefined){
		chartPCSqlPie.destroy();
	}
	
	let pass = 0, fail = 0;
	
	if ( data != null && data.length > 0 ) {
		let dataLen = data.length;
		
		for(let index = 0; index < dataLen; index++) {
			pass += Number(data[index].pass);
			fail += Number(data[index].fail);
		}
	};
	Ext.define("chartPCSqlPie.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,	// 적합
		COLOR_02: redColor		// 부적합
	});
	
	chartPCSqlPie = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southPieChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartPCSqlPie"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
			border : false,
			store : {
				fields: ['name', 'data1'],
//				data : data,
				data : [{
					name : '적합',
					data1 : pass
				},{
					name : '부적합',
					data1 : fail
				}]
				,autoLoad : true
			},
			legend : {
				type : 'dom'
			},
			series : [{
				type : 'pie',
				colors: [chartPCSqlPie.colors.COLOR_01, chartPCSqlPie.colors.COLOR_02],
				angleField : 'data1',
				label: {
					field : 'name',
					display: 'inside'
				},
				tooltip:{
					trackMouse:true,
					renderer:function(tooltip,record){
						tooltip.setHtml(record.data.name+"["+record.data.data1+"]");
					}
				},
				donut : 0
			}]
		}]
	});
}

function createPCSqlBar(result){
	let data;
	
	try {
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
		
		if(chartPCSqlBar != "undefined" && chartPCSqlBar != undefined){
			chartPCSqlBar.destroy();
		}
		
		let data1;
		let data2;
		
		if(data.length == 0) {
			data1 = new Object();
			data2 = new Object();
			
			data1.less_than_1 = null;
			data1.less_than_2 = null;
			data1.less_than_5 = null;
			data1.less_than_10 = null;
			data1.less_than_30 = null;
			data1.less_than_100 = null;
			data1.more_than_100 = null;
			
			data.less_than_1 = null;
			data2.less_than_2 = null;
			data2.less_than_5 = null;
			data2.less_than_10 = null;
			data2.less_than_30 = null;
			data2.less_than_100 = null;
			data2.more_than_100 = null;
		} else {
			data1 = data[0];
			data2 = data[1];
		}
		
		Ext.define("chartPCSqlBar.colors", {	// Label 색상 정의
			singleton:  true,
			COLOR_01: greenColor,
			COLOR_02: redColor
		});
		
		chartPCSqlBar = Ext.create("Ext.panel.Panel",{
//			width : '100%',
//			height : '100%',
			width : southBarChartWidth,
			height : southPieChartHeight,
			border : false,
			renderTo : document.getElementById("chartPCSqlBar"),
			layout : 'fit',
			items : [{
				xtype : 'cartesian',
				border : false,
				insetPadding : southBarChartInsetPadding,
				store : {
//					data : data
					fields : ['bar_label', 'pass', 'fail'],
					data : [{
						bar_label : '< 1x',
						pass : data2.less_than_1,
						fail : data1.less_than_1
					},{
						bar_label : '< 2x',
						pass : data2.less_than_2,
						fail : data1.less_than_2
					},{
						bar_label : '< 5x',
						pass : data2.less_than_5,
						fail : data1.less_than_5
					},{
						bar_label : '< 10x',
						pass : data2.less_than_10,
						fail : data1.less_than_10
					},{
						bar_label : '< 30x',
						pass : data2.less_than_30,
						fail : data1.less_than_30
					},{
						bar_label : '< 50x',
						pass : data2.less_than_50,
						fail : data1.less_than_50
					},{
						bar_label : '< 100x',
						pass : data2.less_than_100,
						fail : data1.less_than_100
					},{
						bar_label : '>= 100x',
						pass : data2.more_than_100,
						fail : data1.more_than_100
					}]
					,autoLoad : true
				},
				legend : {
					type : 'dom'
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minimum: 0
				},{
					type : 'category',
					position : 'bottom',
					label : {
						x : 0,
						y : 0,
					},
					fields : ['bar_label']
				}],
				series : [{
					type : 'bar',
					axis: 'left',
					style: {
						minGapWidth: 10,
					},
					xField : 'bar_label',
					yField : ['pass','fail'],
					stacked: false,
					title: ['적합', '부적합'],
					colors: [chartPCSqlBar.colors.COLOR_01, chartPCSqlBar.colors.COLOR_02],
					label: {
						field: ['pass','fail'],
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
							
							tooltip.setHtml(xField + " : " + yField);
						}
					}
				}]
			}]
		});
	} catch(error) {
		top.$.messager.alert('createPCSqlBar',error.getMessage());
	}
}

function createEHSql(result){
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
	
	if(chartEHSql != "undefined" && chartEHSql != undefined){
		chartEHSql.destroy();
	}
	
	Ext.define("chartEHSql.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: blueColor,	// 일일 부적합률
		COLOR_02: redColor		// 일일 부적합
	});
	
	chartEHSql = Ext.create("Ext.panel.Panel",{
		width : '100%',
//		height : northBarChartHeight,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartEHSql"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '20 0 0 0',
			store : {
				data : data
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				fields: ['fail'],
				minimum: 0
			},{
				type : 'numeric',
				position : 'right',
				fields: ['fail_ratio'],
				minimum: 0,
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : 'day'
			}],
			series : [{
				type : 'bar',
				xField : 'day',
				yField : 'fail',
				title: '일일 부적합',
				colors: [chartEHSql.colors.COLOR_02],
				label: {
					field: 'fail',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			},{
				type : 'line',
				style: {
					lineWidth: 3,
				},
				xField : 'day',
				yField : 'fail_ratio',
				title: '일일 부적합률(%)',
				colors: [chartEHSql.colors.COLOR_01],
				label: {
					field: 'fail_ratio',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: '9px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif',
				}
			}]
		}]
	});
}

function createEHSqlPie(result){
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
	
	if(chartEHSqlPie != "undefined" && chartEHSqlPie != undefined){
		chartEHSqlPie.destroy();
	}
	
	let pass = 0, fail = 0;
	
	if ( data != null && data.length > 0 ){
		let dataLen = data.length;
	
		for(let index = 0; index < dataLen; index++) {
			pass += Number(data[index].pass);
			fail += Number(data[index].fail);
		}
	}
	Ext.define("chartEHSqlPie.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,	// 적합
		COLOR_02: redColor		// 부적합
	});
	
	chartEHSqlPie = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : 250,
		height : 150,
		border : false,
		renderTo : document.getElementById("chartEHSqlPie"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
			border : false,
			store : {
				fields: ['name', 'data1'],
//				data : data,
				data : [{
					name : '적합',
					data1 : pass
				},{
					name : '부적합',
					data1 : fail
				}]
				,autoLoad : true
			},
			legend : {
				type : 'dom'
			},
			series : [{
				type : 'pie',
				colors: [chartEHSqlPie.colors.COLOR_01, chartEHSqlPie.colors.COLOR_02],
				angleField : 'data1',
				label: {
					field : 'name',
					display: 'inside'
				},
				tooltip:{
					trackMouse:true,
					renderer:function(tooltip,record){
						tooltip.setHtml(record.data.name+"["+record.data.data1+"]");
					}
				},
				donut : 0
			}]
		}]
	});
}

function createEHSqlBar(result){
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
	
	if(chartEHSqlBar != "undefined" && chartEHSqlBar != undefined){
		chartEHSqlBar.destroy();
	}
	
	let data1;
	let data2;
	
	if(data == null || data.length == 0) {
		data1 = new Object();
		data2 = new Object();
		
		data1.less_than_0_dot_1 = null;
		data1.less_than_0_dot_3 = null;
		data1.less_than_1 = null;
		data1.less_than_3 = null;
		data1.less_than_10 = null;
		data1.less_than_60 = null;
		data1.more_than_60 = null;
		
//		data.less_than_0_dot_1 = null;
		data2.less_than_0_dot_1 = null;
		data2.less_than_0_dot_3 = null;
		data2.less_than_1 = null;
		data2.less_than_3 = null;
		data2.less_than_10 = null;
		data2.less_than_60 = null;
		data2.more_than_60 = null;
	} else {
		data1 = data[0];
		data2 = data[1];
	}
	
	Ext.define("chartEHSqlBar.colors", {	// Label 색상 정의
		singleton:  true,
		COLOR_01: greenColor,
		COLOR_02: redColor
	});
	
	chartEHSqlBar = Ext.create("Ext.panel.Panel",{
//		width : '100%',
//		height : '100%',
		width : southBarChartWidth,
		height : southPieChartHeight,
		border : false,
		renderTo : document.getElementById("chartEHSqlBar"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			insetPadding : southBarChartInsetPadding,
			store : {
//				data : data
				fields : ['bar_label', 'pass', 'fail'],
				data : [{
					bar_label : '<= 0.1s',
					pass : data2.less_than_0_dot_1,
					fail : data1.less_than_0_dot_1
				},{
					bar_label : '< 0.3s',
					pass : data2.less_than_0_dot_3,
					fail : data1.less_than_0_dot_3
				},{
					bar_label : '< 1s',
					pass : data2.less_than_1,
					fail : data1.less_than_1
				},{
					bar_label : '< 3s',
					pass : data2.less_than_3,
					fail : data1.less_than_3
				},{
					bar_label : '< 10s',
					pass : data2.less_than_10,
					fail : data1.less_than_10
				},{
					bar_label : '< 60s',
					pass : data2.less_than_60,
					fail : data1.less_than_60
				},{
					bar_label : '>= 60s',
					pass : data2.more_than_60,
					fail : data1.more_than_60
				}]
				,autoLoad : true
			},
			legend : {
				type : 'dom'
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0
			},{
				type : 'category',
				position : 'bottom',
				label : {
					x : 0,
					y : 0,
				},
				fields : ['bar_label']
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				style: {
					minGapWidth: 10,
				},
				xField : 'bar_label',
				yField : ['pass','fail'],
				stacked: false,
				title: ['적합', '부적합'],
				colors: [chartEHSqlBar.colors.COLOR_01, chartEHSqlBar.colors.COLOR_02],
				label: {
					field: ['pass','fail'],
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
						
						tooltip.setHtml(xField + " : " + yField);
					}
				}
			}]
		}]
	});
}

function getChartData() {
	let chartUrl = new Array();
	
	chartUrl.push("/RunSqlPerformanceChangeTracking/chart");
	chartUrl.push("/RunSqlPerformanceChangeTracking/chartPerformance01");
	chartUrl.push("/RunSqlPerformanceChangeTracking/chartPerformance02");
	chartUrl.push("/RunSqlPerformanceChangeTracking/chartException01");
	chartUrl.push("/RunSqlPerformanceChangeTracking/chartException02");
	
	console.log("begin_dt[" + $('#begin_dt').datebox('getValue'));
	
	for(let index=0; index < chartUrl.length; index++) {
		ajaxCall(chartUrl[index],
				$("#submit_form"),
				"POST",
				eval('callback_chart_' + index));
	}
}

var callback_chart_0 = function(result) {
//	console.log("callback_chart_0");
//	console.log(result);
	
	createBG(result);
	createET(result);
//	createPC(result);
	
	createBGPie(result);
	createETPie(result);
//	createPCPie(result);
	
	createBGBar(result);
	createETBar(result);
//	createPCBar(result);
}

var callback_chart_1 = function(result) {
//	console.log("callback_chart_1");
//	console.log(result);
	
	createPCSql(result);
	createPCSqlPie(result);
}

var callback_chart_2 = function(result) {
//	console.log("callback_chart_2");
//	console.log(result);
	
	createPCSqlBar(result);
}

var callback_chart_3 = function(result) {
//	console.log("callback_chart_3");
//	console.log(result);
	
	createEHSql(result);
	createEHSqlPie(result);
}

var callback_chart_4 = function(result) {
//	console.log("callback_chart_4");
//	console.log(result);
	
	createEHSqlBar(result);
}