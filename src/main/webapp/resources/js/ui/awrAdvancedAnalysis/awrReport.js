var osStatChart;
var foreWaitChart;
var backWaitChart;
var foreTopEventChart;
var backTopEventChart;

Ext.EventManager.onWindowResize(function () {
    var width = $("#osStatChart").width();

    if(osStatChart != "undefined" && osStatChart != undefined){
    	osStatChart.setSize(width);		
	}    
    if(foreWaitChart != "undefined" && foreWaitChart != undefined){
    	foreWaitChart.setSize(width);		
	}
    if(backWaitChart != "undefined" && backWaitChart != undefined){
    	backWaitChart.setSize(width);		
	}
    if(foreTopEventChart != "undefined" && foreTopEventChart != undefined){
    	foreTopEventChart.setSize(width);		
	}
    if(backTopEventChart != "undefined" && backTopEventChart != undefined){
    	backTopEventChart.setSize(width);		
	}
});

$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// Instance 조회			
	    	$('#selectInstance').combobox({
	    	    url:"/Common/getAgentInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','Instance 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$("#inst_id").val("");
	    	$("#snap_id").val("");	    	
	    	$("#start_snap_id1").textbox("setValue","");
	    	$("#end_snap_id1").textbox("setValue","");
	    }	    
	});	
	
	$("#awrReportTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1)			
				if(index == 0){
					createReportChart();		
				}else{
					searchAnalysis();
				}
			}
		}
	});
});

function Btn_OnClick(){
	var tab = $('#awrReportTab').tabs('getSelected');
	var index = $('#awrReportTab').tabs('getTabIndex',tab);

	fnUpdateSearchBtnClickFlag();
	
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	var tabIndex = index + 1;
	var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
	
	if(tabClickCount < searchBtnClickCount){
		$("#submit_form #tab"+tabIndex+"ClickCount").val(1);	
		if(index == 0){
			createReportChart();		
		}else{
			searchAnalysis();
		}
	}
}

function searchAnalysis(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance를 선택해 주세요.');
		return false;
	}	
	
	if($('#start_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	if($('#end_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#gubun").val($('#selectGubun').combobox('getValue'));

	ajaxCall("/AWRReport",
		$("#submit_form"),
		"POST",
		callback_AWRReportAction);		
	
	var win = parent.$.messager.progress({
		title:'Please waiting',
		msg:'AWR 분석',
		text:'생성중입니다.',
		interval:100
	});
}

//callback 함수
var callback_AWRReportAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#dataArea").html("");
		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			if($("#selectGubun").combobox("getValue") == "html"){
				strHtml += post.row_value;
			}else{
				strHtml += strReplace(strReplace(post.row_value,"\n","<br/>")," ","&nbsp;") + "<br/>";
			}
		}
		$("#dataArea").append(strHtml);
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};

function createReportChart(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance를 선택해 주세요.');
		return false;
	}	
	
	if($('#start_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	if($('#end_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 SNAP ID를 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));	
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("AWR Analysis 챠트"," "); 
	
	createOsStatChart();
	createForeWaitChart();
	createBackWaitChart();
	createForeTopEventChart();
	createBackTopEventChart();
}

function createOsStatChart(){
	if(osStatChart != "undefined" && osStatChart != undefined){
		osStatChart.destroy();
	}

	osStatChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("osStatChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 10 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백			
			legend : {
				docked : 'bottom'
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'area',
				xField : 'snap_time',
				yField : ['user_percent','sys_percent','iowait_percent'],
				title : ['USER','SYS','IOWAIT'],
				marker: {
	                opacity: 0,
	                scaling: 0.01,
	                animation: {
	                    duration: 200,
	                    easing: 'easeOut'
	                }
	            },
	            highlightCfg: {
	                opacity: 1,
	                scaling: 1.5
	            },
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
						gubun = item.series.getTitle()[fieldIndex];
						
						tooltip.setHtml(gubun + " : " + record.get(item.field) + " %");
					}
				}
			}]
		}]
	});
	
	ajaxCall("/AWRReport/OSStatChart",
			$("#submit_form"),
			"POST",
			callback_OSStatChartAction);
}

//callback 함수
var callback_OSStatChartAction = function(result) {
	chart_callback(result, osStatChart);
};

function createForeWaitChart(){
	if(foreWaitChart != "undefined" && foreWaitChart != undefined){
		foreWaitChart.destroy();
	}
	
	foreWaitChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("foreWaitChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            docked: 'bottom'
	        },
	        innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : []
		}]
	});	
	
	// Wait Class Chart
	ajaxCall("/AWRReport/ForeWaitClassChart",
			$("#submit_form"),
			"POST",
			callback_ForeWaitClassChartAction);
}

var callback_ForeWaitClassChartAction = function(result) {
	var store;
	var data;
	var chart = foreWaitChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'snap_dt',
				yField : post.wait_class,
				title : post.wait_class,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("snap_dt")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function createBackWaitChart(){
	if(backWaitChart != "undefined" && backWaitChart != undefined){
		backWaitChart.destroy();
	}
	
	backWaitChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("backWaitChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            docked: 'bottom'
	        },
	        innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : []
		}]
	});	
	
	// Wait Class Chart
	ajaxCall("/AWRReport/BackWaitClassChart",
			$("#submit_form"),
			"POST",
			callback_BackWaitClassChartAction);
}

var callback_BackWaitClassChartAction = function(result) {
	var store;
	var data;
	var chart = backWaitChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'snap_dt',
				yField : post.wait_class,
				title : post.wait_class,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("snap_dt")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function createForeTopEventChart(){
	if(foreTopEventChart != "undefined" && foreTopEventChart != undefined){
		foreTopEventChart.destroy();
	}
	
	foreTopEventChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("foreTopEventChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            docked: 'bottom'
	        },
	        innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : []
		}]
	});	
	
	// Top Event Chart
	ajaxCall("/AWRReport/ForeTOPEventChart",
			$("#submit_form"),
			"POST",
			callback_ForeTOPEventChartAction);
}

var callback_ForeTOPEventChartAction = function(result) {
	var store;
	var data;
	var chart = foreTopEventChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'snap_dt',
				yField : post.event_name,
				title : post.event_name,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("snap_dt")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function createBackTopEventChart(){
	if(backTopEventChart != "undefined" && backTopEventChart != undefined){
		backTopEventChart.destroy();
	}
	
	backTopEventChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("backTopEventChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
		    legend: {
	            docked: 'bottom'
	        },
	        innerPadding : '25 25 20 25', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(CNT)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : []
		}]
	});	
	
	// Top Event Chart
	ajaxCall("/AWRReport/BackTOPEventChart",
			$("#submit_form"),
			"POST",
			callback_BackTOPEventChartAction);
}

var callback_BackTOPEventChartAction = function(result) {
	var store;
	var data;
	var chart = backTopEventChart.down("chart");
	var nameArray = [];
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'snap_dt',
				yField : post.event_name,
				title : post.event_name,				
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("snap_dt")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " CNT");
					}
				}
			});
		}
		
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
};

/* SNAP_ID 선택 팝업 */
//function showSnapPopup(gubun){
//	parent.strGb = gubun;
//	
//	if($('#selectCombo').combobox('getValue') == ""){
//		parent.$.messager.alert('','DB를 선택해 주세요.');
//		return false;
//	}
//	
//	$("#dbid").val($('#selectCombo').combobox('getValue'));
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();	
//	
//	parent.$('#snapList_form #snap_id').val("0");
//	parent.$('#snapList_form #startSnapIdList').datagrid('loadData',[]);
//	parent.$('#snapList_form #endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$('#snapList_form #dbid').val($('#selectCombo').combobox('getValue'));
//	parent.$('#snapList_form #dbName').textbox('setValue',$('#selectCombo').combobox('getText'));
//	parent.$('#snapList_form #instance_number').val($('#selectInstance').combobox('getValue'));
//
//	parent.$('#startSnapIdList').datagrid('loadData',[]);
//	parent.$('#endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
//	parent.$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);	
//	
//	parent.$('#snapListPop').window("open");
//}

/* 베이스라인 선택 팝업 */
function showBaseLinePopup(strNo){
	strNoGb = strNo;
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance 선택해 주세요.');
		return false;
	}		
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));	

	$('#baseLinePop').window("open");
	
	$('#baseLineList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("베이스 라인 선택"," "); 
	
	ajaxCall("/AWRAdvancedAnalysis/Popup/BaseLineList",
			$("#submit_form"),
			"POST",
			callback_BaseLineList);	
}

//callback 함수
var callback_BaseLineList = function(result) {
	json_string_callback_common(result,'#baseLineList',true);
}


function Btn_SaveClick(){
	$("#submit_form").attr("action","/AWRReport/Save");
	$("#submit_form").submit();		
}

function Btn_PrintClick() {
   $("#dataArea").printElement({pageTitle: 'AWR REPORT',printMode: 'popup',overrideElementCSS:true,leaveOpen:false});
}