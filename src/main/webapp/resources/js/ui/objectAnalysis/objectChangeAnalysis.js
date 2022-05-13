var objectChangeChart;
var tabIndex = 0;

Ext.EventManager.onWindowResize(function () {
    var width = $("#objectChangeChart").width();
    
    if(objectChangeChart != "undefined" && objectChangeChart != undefined){
    	objectChangeChart.setSize(width);
	}
});	

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#selectBaseDayGubunCombo").combobox("textbox").attr("placeholder","선택");
	// Database 조회			
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=N",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#selectDbidCombo").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$("#checkTab").tabs({
		plain: true,
		onSelect:function(title, index){
			tabIndex = index;
			//console.log("checkTab title:"+title+" index:"+index);

			$("#base_day_gubun").val($('#selectBaseDayGubunCombo').combobox("getValue"));
			$("#dbid").val($('#selectDbidCombo').combobox("getValue"));
			
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
				if(index == 0){
					tableChangeAjaxCall();
				}else if(index == 1){
					columnChangeAjaxCall();
				}else if(index == 2){
					indexChangeAjaxCall();
				}
			}
		}
	});
	
	$('#submit_form #selectBaseDayGubunCombo').combobox('setValue',$("#base_day_gubun").val());
	$('#submit_form #selectDbidCombo').combobox('setValue',$("#dbid").val());
	
	createObjectChangeChart();
	createTagGrid();
});

function createObjectChangeChart(){
	if(objectChangeChart != "undefined" && objectChangeChart != undefined){
		objectChangeChart.destroy();
	}
	objectChangeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("objectChangeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
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
				title : '(건수)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0,
					y : 0
				}
                //,fields: ["TABLE_CREATE","TABLE_DROP","TABLE_MODIFY","COLUMN_ADD","COLUMN_DROP","COLUMN_MODIFY","INDEX_CREATE","INDEX_DROP","INDEX_MODIFY"]
			}],
			series : {
				type : 'bar',
				stacked : false,
				style: {
      			  minGapWidth: 20
    			},
				xField : ["GUBUN"],
				yField : ["CNT"]
				//,title : ["TABLE_CREATE","TABLE_DROP","TABLE_MODIFY","COLUMN_ADD","COLUMN_DROP","COLUMN_MODIFY","INDEX_CREATE","INDEX_DROP","INDEX_MODIFY"]
//				,highlight: {
//      				strokeStyle: 'black',
//        			fillStyle: 'gold'
//					strokeStyle: 'black',
//					fillStyle: '#93AB2F',
//    			}
				,tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.record.get('GUBUN') + " : " + item.record.get('CNT') + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        	var tabName = item.record.data.GUBUN;
			        	if(tabName == 'INDEX_MODIFY'){
			        		strIndex = 2;
			        	}else if(tabName == 'COLUMN_MODIFY'){
			        		strIndex = 1;
			        	}else{
			        		strIndex = 0;
			        	}
			        	$('#checkTab').tabs('select', strIndex);
			        }
			    }
			}
		}]
	});
	
	objectChangeAjaxCall();
}

function createTagGrid(){
	$("#tableChangeList").datagrid({
		view: myview,
		nowrap : false,
		//singleSelect : false,
		//checkOnSelect : true,
		//selectOnCheck : true,
		
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'inst_id',hidden:"true"},
			{field:'base_day',title:'변경일자',halign:"center",align:"center",sortable:"true"},			
			{field:'change_type',title:'변경유형',halign:"center",align:"center",sortable:"true"},			
			{field:'owner',title:'OWNER',halign:"center",align:"left",sortable:"true"},			
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE_NAME',halign:"left",align:"center",sortable:"true"},
			{field:'comments',title:'COMMENTS',width:'400px',halign:"left",align:"left",sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#columnChangeList").datagrid({
		view: myview,
		nowrap : false,
		//singleSelect : false,
		//checkOnSelect : true,
		//selectOnCheck : true,
			
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'inst_id',hidden:"true"},
			{field:'base_day',title:'변경일자',halign:"center",align:"center",sortable:"true"},			
			{field:'change_type',title:'변경유형',halign:"center",align:"center",sortable:"true"},			
			{field:'owner',title:'OWNER',halign:"center",align:"left",sortable:"true"},			
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'column_name',title:'COLUMN_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'data_type',title:'DATA_TYPE',halign:"center",align:"left",sortable:"true"},
			{field:'data_length',title:'DATA_LENGTH',halign:"center",align:"right",sortable:"true"},
			{field:'data_precision',title:'DATA_PRECISION',halign:"center",align:"right",sortable:"true"},
			{field:'data_scale',title:'DATA_SCALE',halign:"center",align:"right",sortable:"true"},
			{field:'nullable',title:'NULLABLE',halign:"center",align:"center",sortable:"true"},
			{field:'column_id',title:'COLUMN_ID',halign:"center",align:"right",sortable:"true"},
			{field:'comments',title:'COMMENTS',width:'250px',halign:"center",align:"left",sortable:"true"},
			{field:'before_column_info',title:'BEFORE_COLUMN_INFO',width:'250px',halign:"center",align:"left",sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#indexChangeList").datagrid({
		view: myview,
		nowrap : false,
		//singleSelect : false,
		//checkOnSelect : false,
		//selectOnCheck : false,
			
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'inst_id',hidden:"true"},
			{field:'base_day',title:'변경일자',halign:"center",align:"center",sortable:"true"},			
			{field:'change_type',title:'변경유형',halign:"center",align:"center",sortable:"true"},			
			{field:'owner',title:'OWNER',halign:"center",align:"left",sortable:"true"},			
			{field:'index_name',title:'INDEX_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'index_type',title:'INDEX_TYPE',halign:"center",align:"center",sortable:"true"},
			{field:'uniqueness',title:'UNIQUENESS',halign:"center",align:"center",sortable:"true"},
			{field:'columns',title:'COLUMNS',halign:"center",align:'left',sortable:"true"},
			{field:'before_columns',title:'BEFORE_COLUMNS',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$('#tableChangeList').datagrid("loadData", []);
	$('#columnChangeList').datagrid("loadData", []);
	$('#indexChangeList').datagrid("loadData", []);
	
	createObjectChangeChart();
	tableChangeAjaxCall();
	
}

function Btn_OnClick(){
	if($('#selectBaseDayGubunCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','변경 기준을 선택해 주세요.');
		return false;
	}
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();	

	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#base_day_gubun").val($('#selectBaseDayGubunCombo').combobox("getValue"));
	$("#dbid").val($('#selectDbidCombo').combobox("getValue"));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("테이블 변경"," "); 

	createObjectChangeChart();
	if(tabIndex == 1){
		columnChangeAjaxCall();
	}else if(tabIndex == 2){
		indexChangeAjaxCall();
	}else{
		tableChangeAjaxCall();
	}
	
}

function objectChangeAjaxCall(){
	ajaxCall("/ObjectChangeAnalysis/Chart", $("#submit_form"), "POST", callback_ObjectChangeAnalysisChartAction);
}

function tableChangeAjaxCall(){
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	var tabIndex = 1;
	var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
	if(tabClickCount < searchBtnClickCount){
		$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("테이블 변경"," ");
		ajaxCall("/ObjectChangeAnalysis/Table", $("#submit_form"), "POST", callback_TableChangeListAction);
	}
}

function columnChangeAjaxCall(){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("컬럼 변경"," ");
	ajaxCall("/ObjectChangeAnalysis/Column", $("#submit_form"), "POST", callback_ColumnChangeListAction);
}

function indexChangeAjaxCall(){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 변경"," ");
	ajaxCall("/ObjectChangeAnalysis/Index", $("#submit_form"), "POST", callback_IndexChangeListAction);
}

//callback 함수
var callback_ObjectChangeAnalysisChartAction = function(result) {
	chart_callback(result, objectChangeChart);
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

//callback 함수
var callback_TableChangeListAction = function(result) {
	json_string_callback_common(result, '#tableChangeList', true);
	parent.$.messager.progress('close');

};

//callback 함수
var callback_ColumnChangeListAction = function(result) {
	json_string_callback_common(result, '#columnChangeList', true);
	parent.$.messager.progress('close');
};

//callback 함수
var callback_IndexChangeListAction = function(result) {
	json_string_callback_common(result, '#indexChangeList', true);
	$( ".datagrid-view2:eq(2) td" ).css( "cursor", "default" );
	parent.$.messager.progress('close');
};

