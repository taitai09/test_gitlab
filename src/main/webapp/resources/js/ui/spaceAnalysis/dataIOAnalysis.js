var dataIOChart;

Ext.EventManager.onWindowResize(function () {
	var width = Ext.getBody().getViewSize().width - 40;
	if(dataIOChart != "undefined" && dataIOChart != undefined){
		dataIOChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createDataIOChart();
	
	// Database 조회			
	$('#selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#selectDbid').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#file_name").val(row.file_name);
			$("#file_id").val(row.file_id);
			newTabLink();
		},			
		columns:[[
//			{field:'file_name',title:'FILE_NAME',width:"20%",halign:"center",align:"left",sortable:"true"},
//			{field:'file_id',title:'FILE_ID',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'tablespace_name',title:'TABLESPACE_NAME',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'bytes',title:'BYTES',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'blocks',title:'BLOCKS',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'phyrds',title:'PHYRDS',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'phywrts',title:'PHYWRTS',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'wait_count',title:'WAIT_COUNT',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'time',title:'TIME',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
			{field:'file_name',title:'FILE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'file_id',title:'FILE_ID',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'bytes',title:'BYTES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blocks',title:'BLOCKS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'phyrds',title:'PHYRDS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'phywrts',title:'PHYWRTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'wait_count',title:'WAIT_COUNT',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'time',title:'TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		} 
	});
	
	$('.datatime').timespinner({
		showSeconds: true
	});
});

function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectDbid').combobox('getValue'));
	$("#start_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
	$("#end_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));
	
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터파일 I/O 분석"," "); 
	
	ajaxCall("/DataIOAnalysis",
		$("#submit_form"),
		"POST",
		callback_DataIOAnalysisAddTable);		
	
	ajaxCall("/DataIOAnalysis/DataFileIOChart",
			$("#submit_form"),
			"POST",
			callback_DataFileIOChartAction);	
}

//callback 함수
var callback_DataIOAnalysisAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

//callback 함수
var callback_DataFileIOChartAction = function(result) {
	chart_callback(result, dataIOChart);
};

function createDataIOChart(){
	if(dataIOChart != "undefined" && dataIOChart != undefined){
		dataIOChart.destroy();
	}
	dataIOChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dataIOChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			flipXY : true, // 가로/세로 축 변경
			border : false,
			innerPadding : '10 5 5 0', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			legend: {
				docked: 'right'
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				adjustByMajorUnit: true,
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
				title : '(CNT)'
			},{
				type : 'category',
				position : 'left',
				grid: true,
				label : {
					x : 60,
					textAlign : 'right'
				}	
			}],
			series : {
				type : 'bar',
				style: {
	  			  minGapWidth: 10
				},
				xField : 'file_name',
				yField : ['phyrds','phywrts'],
				title : ['PHYRDS','PHYWRTS'],
				highlight: {
	  				strokeStyle: 'black',
					fillStyle: 'gold'
				},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
						title = item.series.getTitle()[fieldIndex];
						value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('file_name')+"["+title+"] : " + value + " CNT");
					}
				}
			},
			listeners: {
				itemclick: function (chart, item, event) {
					$("#file_name").val(item.record.get("file_name"));
					$("#file_id").val(item.record.get("file_id"));
					newTabLink();
				}
			}
		}]
	});
}

function newTabLink(){
	var menuId = $("#menu_id").val() + "1";
	var menuNm = "데이터파일 I/O 상세 분석";
	var menuUrl = "/DataIOAnalysis/Detail";
	var menuParam = "dbid="+$("#dbid").val()+"&db_name="+$('#selectDbid').combobox('getText')+"&start_time="+$("#start_time").val()+"&end_time="+$("#end_time").val()+"&file_name="+$("#file_name").val()+"&file_id="+$("#file_id").val();
	
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}