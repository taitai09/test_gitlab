var chartCPUStatus; //첫번째
var chartElapsedTimeSQLStatus; //두번째
var chartCPUTimeSQLStatus; //세번째
var chartExecutionStatus; // 네번째
var chartChartExecutionStatus;
var chartLogicalReadsCPUStatus; //다섯번째
var chartPhysicalReadsStatus; //여섯번째
var isSelected = 'N';
var whatGridIs = ""; //선택된 그리드를 조회하려는 용도
var tabIndex = 0;
var vcnt = 0;
var newval = "";
var oldval = "";
var colors = ["#548DD4","#00AF50"]; //차트 색
$(document).ready(function() {
	
    $("body").css("visibility", "visible");
	createChartCPUStatus(); //첫번째
	createChartElapsedTimeSQLStatus(); //두번째
	createChartCPUTimeSQLStatus(); //세번째
	createChartExecutionStatus(); // 네번째
	createChartLogicalReadsCPUStatus(); //다섯번째
	createChartPhysicalReadsStatus(); //여섯번째
	
	createTableList(); //그리드 생성	
	
	
	//인스턴스 콤보박스
	$('#submit_form #select_inst_id').combobox({
		url:"/Common/getInstance?isChoice=N",
		method:'get',
		valueField:'inst_id',
		textField:'inst_nm',
		onSelect:function(val){
			$("#submit_form #dbid").val(val.dbid);
			$("#submit_form #inst_id").val(val.inst_id.split(",")[0]);
//			$("#submit_form #inst_nm").val(val.inst_nm);
			if(val.inst_id != null && val.inst_id != '') isSelected = 'Y';
			else isSelected = 'N';
		},
		onLoadSuccess: function(){
			$("#submit_form #select_inst_id").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	//디비콤보박스
	$('#submit_form #select_dbid').combobox({
		url:"/Common/getDatabase?isChoice=N",
		method:'get',
		valueField:'dbid',
		textField:'db_name',
		onSelect:function(val){
			$("#submit_form #dbid").val(val.dbid);
			$("#submit_form #inst_nm").val(val.inst_nm);
			$("#submit_form #inst_id").val(null);
			if(val.dbid != null && val.dbid != '') isSelected = 'Y';
			else isSelected = 'N';
		},
		onLoadSuccess: function(){
			$("#submit_form #select_dbid").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$("#span_inst_id").hide();
	
	// DB & INSTANCE (라디오박스)
	$('#select_db').radiobutton({
		onChange:function(val){
			if(val == true){
				$('#submit_form #select_inst_id').combobox("setValue",'');
				$('#submit_form #select_dbid').combobox("setValue",'');
				$("#span_inst_id").hide();
				$("#span_dbid").show();
			}else{
				$('#submit_form #select_dbid').combobox("setValue",'');
				$('#submit_form #select_inst_id').combobox("setValue",'');
				$("#span_dbid").hide();
				$("#span_inst_id").show();
			}
		}
	});
	
	// 전일 & 일주일전 & 한달전 (라디오박스)
	$('#select_adayago').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#startDate").datebox("setValue",$("#aDayAgo").val());
				$("#endDate").datebox("setValue",$("#aDayAgo").val());

			}

		}
	});
	$('#select_aweekago').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#startDate").datebox("setValue",$("#aWeekAgo").val());
				$("#endDate").datebox("setValue",$("#nowDate").val());
			}
		}
	});
	$('#select_amonthago').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#startDate").datebox("setValue",$("#aMonthAgo").val());
				$("#endDate").datebox("setValue",$("#nowDate").val());
			}
		}
	});
	
	// 전체 & 주간  & 야간 (라디오박스)
	$('#select_alltime').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#whatTime").val("allTime");
			}
			initTabCnt();
			if(whatGridIs != '')
				getGridTable();
		}
	});
	
	$('#select_daytime').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#whatTime").val("dayTime");
			}
			initTabCnt();
			if(whatGridIs != '')
				getGridTable();
		}
	});
	
	$('#select_nighttime').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#whatTime").val("nightTime");
			}
			initTabCnt();
			if(whatGridIs != '')
				getGridTable();
		}
	});
	
	
//	$('#tag_module').tagbox('setValue',["테스트2","테스트3"]);

	$('#tag_module').tagbox({
		onRemoveTag:function(val){
			$("#module").val("");
			getGridResultList(whatGridIs);
		}
	});
	$('#tag_action').tagbox({
		onRemoveTag:function(val){
			$("#action").val("");
			getGridResultList(whatGridIs);
		}
	});
	$('#tag_parsing_schema').tagbox({
		onRemoveTag:function(val){
			$("#parsing_schema_name").val("");
			getGridResultList(whatGridIs);
		}
	});

	//탭 클릭시
	$("#tabsDiv").tabs({
		plain:true,
		onSelect: function(title,index){
			tabIndex = index;
			
			if(whatGridIs != '') 
				getGridTable();
			
//			$("#tab"+index+"ClickCount").val(1);
		}		
	});
	
	
	var a = $('#activity');
	a.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	var t = $('#top');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	
});

function getGridTable(){
//	alert("tabIndex:::"+tabIndex+" / tab0:"+$("#tab0ClickCount").val()+" / tab1:"+$("#tab1ClickCount").val()+" / tab2:"+$("#tab2ClickCount").val());
	
	if(whatGridIs == '') return false;
	
	if(tabIndex == 0 && $("#tab0ClickCount").val() == 0){
		getGridModule(whatGridIs);
		getGridResultList(whatGridIs);
	}else if(tabIndex == 1 && $("#tab1ClickCount").val() == 0){
		getGridAction(whatGridIs);
		getGridResultList(whatGridIs);
	}else if(tabIndex == 2 && $("#tab2ClickCount").val() == 0){
		getGridParsingSchema(whatGridIs);
		getGridResultList(whatGridIs);
	}
	
	$("#tab"+tabIndex+"ClickCount").val(1);
}

function createTableList(){

	$("#tableList_module").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
			$('#tag_module').tagbox('setValue',"");
			
			if(row.module2 == '합계') {
				$('#tag_module').tagbox().init();
				$("#submit_form #module").val("");
				return false;
			}else if(row.module2 == null || row.module2.trim() == ''){
				$("#submit_form #module").val('NULL');
			}else{
				$("#submit_form #module").val(row.module2);
			}

			if(row.module2 != null && row.module2.length >= 14)
				$('#tag_module').tagbox('setValue',row.module2.substring(0,11)+"...");
			else if(row.module == null || row.module2.trim() == '')
				$('#tag_module').tagbox('setValue', 'NULL');
			else
				$('#tag_module').tagbox('setValue',row.module2);
			
			getGridResultList(whatGridIs);
		},		
		columns:[[
			{field:'module',hidden:true},
			{field:'module2',title:'MODULE',halign:"center",align:"left"},
			{field:'elapsed_time_ratio',title:'ELAPSED_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'cpu_time_ratio',title:'CPU_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'executions_ratio',title:'EXECUTIONS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'disk_reads_ratio',title:'DISK_READS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'buffer_gets_ratio',title:'BUFFER_GETS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'parse_calls_ratio',title:'PARSE_CALLS(%)',halign:"center",align:'right', styler:cellStyler},			
			{field:'wait_ratio',title:'WAIT(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'avg_cpu_time',title:'AVG_CPU_TIME(SEC)',halign:"center",align:'right',},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME(SEC)',halign:"center",align:'right'},			
			{field:'avg_disk_reads',title:'AVG_DISK_READS',halign:"center",align:'right'},
			{field:'avg_buffer_gets',title:'AVG_BUFFER_GETS',halign:"center",align:'right'},			
			{field:'avg_wait',title:'AVG_WAIT',halign:"center",align:'right'}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	$("#tableList_action").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$('#tag_action').tagbox('setValue',"");

			if(row.action2 == '합계') {
				$('#tag_action').tagbox().init();
				$("#submit_form #action").val("");
				return false;
			}else if(row.action2 == null || row.action2.trim() == ''){
				$("#submit_form #action").val('NULL');
			}else{
				$("#submit_form #action").val(row.action2);
			}
			
			if(row.action2 != null && row.action2.length >= 14)
				$('#tag_action').tagbox('setValue',row.action2.substring(0,11)+"...");
			else if(row.action2 == null || row.action2.trim() == '')
				$('#tag_action').tagbox('setValue','NULL');
			else
				$('#tag_action').tagbox('setValue',row.action2);
			
			getGridResultList(whatGridIs);

		},		
		columns:[[
			{field:'action',hidden:true},
			{field:'action2',title:'ACTION',halign:"center",align:"left"},
			{field:'elapsed_time_ratio',title:'ELAPSED_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'cpu_time_ratio',title:'CPU_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'executions_ratio',title:'EXECUTIONS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'disk_reads_ratio',title:'DISK_READS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'buffer_gets_ratio',title:'BUFFER_GETS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'parse_calls_ratio',title:'PARSE_CALLS(%)',halign:"center",align:'right', styler:cellStyler},			
			{field:'wait_ratio',title:'WAIT(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'avg_cpu_time',title:'AVG_CPU_TIME(SEC)',halign:"center",align:'right',},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME(SEC)',halign:"center",align:'right'},			
			{field:'avg_disk_reads',title:'AVG_DISK_READS',halign:"center",align:'right'},
			{field:'avg_buffer_gets',title:'AVG_BUFFER_GETS',halign:"center",align:'right'},			
			{field:'avg_wait',title:'AVG_WAIT',halign:"center",align:'right'}
			]],
			fitColumns:true,
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	$("#tableList_parsingSchema").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
			$('#tag_parsing_schema').tagbox('setValue',"");
			
			if(row.parsing_schema_name2 == '합계'){
				$("#submit_form #parsing_schema_name").val("");
				$('#tag_parsing_schema').tagbox().init();
				return false;
			}else if(row.parsing_schema_name2 == null || row.parsing_schema_name2.trim() == ''){
				$("#submit_form #parsing_schema_name").val('NULL');
			}else{
				$("#submit_form #parsing_schema_name").val(row.parsing_schema_name2);
			}

			if(row.parsing_schema_name2 != null && row.parsing_schema_name2.length >= 14)
				$('#tag_parsing_schema').tagbox('setValue',row.parsing_schema_name2.substring(0,11)+"...");
			else if(row.parsing_schema_name2 == null || row.parsing_schema_name2.trim() == '')
				$('#tag_parsing_schema').tagbox('setValue', 'NULL');			
			else
				$('#tag_parsing_schema').tagbox('setValue',row.parsing_schema_name2);			
			
			getGridResultList(whatGridIs);
			

		},		
		columns:[[
			{field:'parsing_schema_name',hidden:true},
			{field:'parsing_schema_name2',title:'PARSING_SCHEMA_NAME',halign:"center",align:"left"},
			{field:'elapsed_time_ratio',title:'ELAPSED_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'cpu_time_ratio',title:'CPU_TIME(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'executions_ratio',title:'EXECUTIONS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'disk_reads_ratio',title:'DISK_READS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'buffer_gets_ratio',title:'BUFFER_GETS(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'parse_calls_ratio',title:'PARSE_CALLS(%)',halign:"center",align:'right', styler:cellStyler},			
			{field:'wait_ratio',title:'WAIT(%)',halign:"center",align:'right', styler:cellStyler},
			{field:'avg_cpu_time',title:'AVG_CPU_TIME(SEC)',halign:"center",align:'right',},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME(SEC)',halign:"center",align:'right'},			
			{field:'avg_disk_reads',title:'AVG_DISK_READS',halign:"center",align:'right'},
			{field:'avg_buffer_gets',title:'AVG_BUFFER_GETS',halign:"center",align:'right'},			
			{field:'avg_wait',title:'AVG_WAIT',halign:"center",align:'right'}
			]],
			fitColumns:true,
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
	$("#tableList_result").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
		},		
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:"left",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'left', sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left', sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:'left', sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'left', sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right', sortable:"true", styler:cellStyler_EL},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',  sortable:"true", styler:cellStyler_CU},			
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right', sortable:"true", styler:cellStyler_EX},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right', sortable:"true", styler:cellStyler_DI},			
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right', sortable:"true", styler:cellStyler_BU},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right', sortable:"true", styler:cellStyler_RO},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left', sortable:"true"},			
			{field:'elapsed_time_ratio',hidden:true},
			{field:'cpu_time_ratio',hidden:true},
			{field:'executions_ratio',hidden:true},			
			{field:'buffer_gets_ratio',hidden:true},
			{field:'disk_reads_ratio',hidden:true},			
			{field:'rows_processed_ratio',hidden:true},
			]],
			fitColumns:true,
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}



function Btn_OnClick(){
	whatGridIs = "";
	
	$('#submit_#excel_inst_nm').textbox("setValue","");
	$('#notice_form #check1').textbox("setValue","");
	$('#notice_form #check2').textbox("setValue","");
	$('#notice_form #check3').textbox("setValue","");
	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
//	$('#submit_form #strStartDt').val($("#startDate").textbox("getValue"));
//	$('#submit_form #strEndDt').val($("#endDate").textbox("getValue"));
	
	if(!compareAnBDate($('#startDate').textbox('getValue'), $('#endDate').textbox('getValue'))) {
		var msg = "기준 일자를 확인해 주세요.<br>시작일자[" + $('#startDate').textbox('getValue') + "] 종료일자[" + $('#endDate').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	if(!formValidationCheck()){  
		return false;
	}	
	
	ajaxCallData();
	$('#tableList_module').datagrid('loadData',[]);
	$('#tableList_action').datagrid('loadData',[]);
	$('#tableList_parsingSchema').datagrid('loadData',[]);
	$('#tableList_result').datagrid('loadData',[]);
}


//검색매서드
function ajaxCallData() {
	
	ajaxCallChartCPUStatus(); //첫번째
	ajaxCallChartElapsedTimeSQLStatus(); //두번째
	ajaxCallChartCPUTimeSQLStatus(); //세번째
	ajaxCallChartExecutionStatus(); // 네번째
	ajaxCallChartLogicalReadsCPUStatus(); //다섯번째
	ajaxCallChartPhysicalReadsStatus(); //여섯번째
}



//첫번째 차트
function createChartCPUStatus(result){
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
	
	if(chartCPUStatus != "undefined" && chartCPUStatus != undefined){
		chartCPUStatus.destroy();		
	}
	
	chartCPUStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartCPUStatus"),
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
				minimum: 0,
//				maximum: 100,
				minorTickSteps: 0,
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
//두번째 차트
function createChartElapsedTimeSQLStatus(result){
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
	
	if(chartElapsedTimeSQLStatus != "undefined" && chartElapsedTimeSQLStatus != undefined){
		chartElapsedTimeSQLStatus.destroy();		
	}
	
	chartElapsedTimeSQLStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartElapsedTimeSQLStatus"),
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
				minimum: 0,
				minorTickSteps: 0,
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
			series: [],
			listeners: {
				itemclick: function (chart, item, event) {
					
					var data = item.record.data;
					var inst_nm = item.field;  //기본으로 필드에 정의
					
					$('#submit_form #strStartDt').val(data.snap_dt);
					$('#submit_form #strEndDt').val(data.snap_dt);
					$('#submit_form #inst_nm').val(inst_nm);
					$('#submit_form #excel_inst_nm').val(inst_nm);

					$('#notice_form #check1').textbox("setValue", inst_nm);
					$('#notice_form #check2').textbox("setValue","Elapsed Time SQL");
					$('#notice_form #check3').textbox("setValue", data.snap_dt);
				
					whatGridIs = 'elapsed_time';
					initTabCnt();
					
					vcnt += "El";
					if(vcnt.length > 2){
						oldval = newval;
					}
					newval = data.snap_dt+"EL";

					if(newval != oldval){
						getGridTable();
					}
					
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
//세번째 차트
function createChartCPUTimeSQLStatus(result){
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
	
	if(chartCPUTimeSQLStatus != "undefined" && chartCPUTimeSQLStatus != undefined){
		chartCPUTimeSQLStatus.destroy();		
	}
	
	chartCPUTimeSQLStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartCPUTimeSQLStatus"),
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
				minimum: 0,
				minorTickSteps: 0,
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
			series: [],
			listeners: {
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					var inst_nm = item.field;  //기본으로 필드에 정의
					
					$('#submit_form #strStartDt').val(data.snap_dt);
					$('#submit_form #strEndDt').val(data.snap_dt);
					$('#submit_form #inst_nm').val(inst_nm);
					$('#submit_form #excel_inst_nm').val(inst_nm);
				
					$('#notice_form #check1').textbox("setValue", inst_nm);
					$('#notice_form #check2').textbox("setValue","CPU Time SQL");
					$('#notice_form #check3').textbox("setValue", data.snap_dt);
					
					whatGridIs = 'cpu_time';
					initTabCnt();

					vcnt += "CP";
					if(vcnt.length > 2){
						oldval = newval;
					}
					newval = data.snap_dt+"CP";

					if(newval != oldval){
						getGridTable();
					}
					
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
//네번째 차트
function createChartExecutionStatus(result){
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
	
	if(chartExecutionStatus != "undefined" && chartExecutionStatus != undefined){
		chartExecutionStatus.destroy();		
	}
	
	chartExecutionStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartExecutionStatus"),
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
				minimum: 0,
				minorTickSteps: 0,
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
			series: [],
			listeners: {
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					var inst_nm = item.field;  //기본으로 필드에 정의
					
					$('#submit_form #strStartDt').val(data.snap_dt);
					$('#submit_form #strEndDt').val(data.snap_dt);
					$('#submit_form #inst_nm').val(inst_nm);
					$('#submit_form #excel_inst_nm').val(inst_nm);
					
					$('#notice_form #check1').textbox("setValue", inst_nm);
					$('#notice_form #check2').textbox("setValue","Executions");
					$('#notice_form #check3').textbox("setValue", data.snap_dt);
					
					whatGridIs = 'executions';
					initTabCnt();

					vcnt += "EX";
					if(vcnt.length > 2){
						oldval = newval;
					}
					newval = data.snap_dt+"EX";

					if(newval != oldval){
						getGridTable();
					}
					
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
//다섯번째 차트
function createChartLogicalReadsCPUStatus(result){
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
	
	if(chartLogicalReadsCPUStatus != "undefined" && chartLogicalReadsCPUStatus != undefined){
		chartLogicalReadsCPUStatus.destroy();		
	}
	
	chartLogicalReadsCPUStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartLogicalReadsCPUStatus"),
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
				minimum: 0,
				minorTickSteps: 0,
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
			series: [],
			listeners: {
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					var inst_nm = item.field;  //기본으로 필드에 정의
					
					$('#submit_form #strStartDt').val(data.snap_dt);
					$('#submit_form #strEndDt').val(data.snap_dt);
					$('#submit_form #inst_nm').val(inst_nm);
					$('#submit_form #excel_inst_nm').val(inst_nm);
				
					$('#notice_form #check1').textbox("setValue", inst_nm);
					$('#notice_form #check2').textbox("setValue","Logical Reads");
					$('#notice_form #check3').textbox("setValue", data.snap_dt);
					
					
					whatGridIs = 'buffer_gets';
					initTabCnt();

					vcnt += "BU";
					if(vcnt.length > 2){
						oldval = newval;
					}
					newval = data.snap_dt+"BU";

					if(newval != oldval){
						getGridTable();
					}
					
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
//여섯번째 차트
function createChartPhysicalReadsStatus(result){
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
	
	if(chartPhysicalReadsStatus != "undefined" && chartPhysicalReadsStatus != undefined){
		chartPhysicalReadsStatus.destroy();		
	}
	
	chartPhysicalReadsStatus = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("chartPhysicalReadsStatus"),
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
				minimum: 0,
				minorTickSteps: 0,
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
			series: [],
			listeners: {
				itemclick: function (chart, item, event) {
					var data = item.record.data;
					var inst_nm = item.field;  //기본으로 필드에 정의
					
					$('#submit_form #strStartDt').val(data.snap_dt);
					$('#submit_form #strEndDt').val(data.snap_dt);
					$('#submit_form #inst_nm').val(inst_nm);
					$('#submit_form #excel_inst_nm').val(inst_nm);
				
					$('#notice_form #check1').textbox("setValue", inst_nm);
					$('#notice_form #check2').textbox("setValue","Physical Reads");
					$('#notice_form #check3').textbox("setValue", data.snap_dt);
					
					whatGridIs = 'disk_reads';
					initTabCnt();

					vcnt += "DI";
					if(vcnt.length > 2){
						oldval = newval;
					}
					newval = data.snap_dt+"DI";

					if(newval != oldval){
						getGridTable();
					}
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



function ajaxCallChartCPUStatus() { //첫번째
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=cpu_usage",
			$("#submit_form"),
			"POST",
			callback_ChartCPUStatus);
}
var callback_ChartCPUStatus = function(result) {
	var store;
	var data;
	var chart = this.chartCPUStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartCPUStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleCartCPUStatus').html("CPU");
			} else {
				$('#titleCartCPUStatus').html("CPU");
			}
		} catch(err) {
			console.log("callback_chartCPUStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField + "%");
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartCPUStatus.down("chart").getStore();
		
//		this.chartCPUStatus.down("chart").setColors(colors);

		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
			$('#titleCartCPUStatus').html("CPU");
		} else {
			$('#titleCartCPUStatus').html("CPU");		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
};

function ajaxCallChartElapsedTimeSQLStatus() {  //두번째
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=elapsed_time",
			$("#submit_form"),
			"POST",
			callback_ChartElapsedTimeSQLStatus); 
}
var callback_ChartElapsedTimeSQLStatus = function(result) {
	var store;
	var data;
	var chart = this.chartElapsedTimeSQLStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartElapsedTimeSQLStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartElapsedTimeSQLStatus').html("Elapsed Time SQL");		
			} else {
				$('#titleChartElapsedTimeSQLStatus').html("Elapsed Time SQL");
			}
		} catch(err) {
			console.log("callback_ChartElapsedTimeSQLStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartElapsedTimeSQLStatus.down("chart").getStore();
		
//		this.chartElapsedTimeSQLStatus.down("chart").setColors(colors);
		
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
			$('#titleChartElapsedTimeSQLStatus').html("Elapsed Time SQL");		
		} else {
			$('#titleChartElapsedTimeSQLStatus').html("Elapsed Time SQL");
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};



function ajaxCallChartCPUTimeSQLStatus() {  //세번째
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=cpu_time",
			$("#submit_form"),
			"POST",
			callback_ChartCPUTimeSQLStatus);
}
var callback_ChartCPUTimeSQLStatus = function(result) {
	var store;
	var data;
	var chart = this.chartCPUTimeSQLStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartCPUTimeSQLStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartCPUTimeSQLStatus').html("CPU Time SQL");		
			} else {
				$('#titleChartCPUTimeSQLStatus').html("CPU Time SQL");		
			}
		} catch(err) {
			console.log("callback_ChartCPUTimeSQLStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartCPUTimeSQLStatus.down("chart").getStore();
//		this.chartCPUTimeSQLStatus.down("chart").setColors(colors);
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
//			$('#titleChartCPUTimeSQLStatus').html("CPU Time " + $('#submit_form #inst_nm').val());		
			$('#titleChartCPUTimeSQLStatus').html("CPU Time SQL");		
		} else {
			$('#titleChartCPUTimeSQLStatus').html("CPU Time SQL");		
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function ajaxCallChartExecutionStatus() {  //네번째
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=executions",
			$("#submit_form"),
			"POST",
			callback_ChartExecutionStatus);
}
var callback_ChartExecutionStatus = function(result) {
	var store;
	var data;
	var chart = this.chartExecutionStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartExecutionStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartExecutionStatus').html("Executions");		
			} else {
				$('#titleChartExecutionStatus').html("Executions");		
			}
		} catch(err) {
			console.log("callback_ChartExecutionStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartExecutionStatus.down("chart").getStore();
//		this.chartExecutionStatus.down("chart").setColors(colors);
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
			$('#titleChartExecutionStatus').html("Executions");		
		} else {
			$('#titleChartExecutionStatus').html("Executions");		
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function ajaxCallChartLogicalReadsCPUStatus() {  //다섯번재
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=buffer_gets",
			$("#submit_form"),
			"POST",
			callback_ChartLogicalReadsCPUStatus);
}
var callback_ChartLogicalReadsCPUStatus = function(result) {
	var store;
	var data;
	var chart = this.chartLogicalReadsCPUStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartLogicalReadsCPUStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartLogicalReadsCPUStatus').html("Logical Reads");		
			} else {
				$('#titleChartLogicalReadsCPUStatus').html("Logical Reads");		
			}
		} catch(err) {
			console.log("callback_ChartLogicalReadsCPUStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartLogicalReadsCPUStatus.down("chart").getStore();
//		this.chartLogicalReadsCPUStatus.down("chart").setColors(colors);
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartLogicalReadsCPUStatus').html("Logical Reads");		
			} else {
				$('#titleChartLogicalReadsCPUStatus').html("Logical Reads");		
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function ajaxCallChartPhysicalReadsStatus() {  // 여섯번재
	
	ajaxCall("/SQLPerformanceStatistics/ChartTopSqlTrendStatus?whatChartIs=disk_reads",
			$("#submit_form"),
			"POST",
			callback_ChartPhysicalReadsStatus);
}
var callback_ChartPhysicalReadsStatus = function(result) {
	var store;
	var data;
	var chart = this.chartPhysicalReadsStatus.down("chart");
	var nameArray = [];
	
	if(result.result == false) {
		try {
			createChartPhysicalReadsStatus(null);
			
			if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
				$('#titleChartPhysicalReadsStatus').html("Physical Reads");		
			} else {
				$('#titleChartPhysicalReadsStatus').html("Physical Reads");		
			}
		} catch(err) {
			console.log("callback_ChartPhysicalReadsStatus error:" + err.message);
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
					'stroke-width': 2
				},
				marker: {
					radius: 1,
					lineWidth: 1
				},
				xField : 'snap_dt',
				yField : post.inst_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var inst_nm = item.field;
						
												tooltip.setHtml("[ " + inst_nm + " ] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		store = chartPhysicalReadsStatus.down("chart").getStore();
//		this.chartPhysicalReadsStatus.down("chart").setColors(colors);
		store.loadData(data.rows);
		chart.redraw();
		
		if($('#submit_form #dbid').val() != null && $('#submit_form #dbid').val() != '') {
			$('#titleChartPhysicalReadsStatus').html("Physical Reads");		
		} else {
			$('#titleChartPhysicalReadsStatus').html("Physical Reads");		
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};


function getGridModule(whatGridIs){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList_module').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Module"," "); 
	
	ajaxCall("/SQLPerformanceStatistics/GridModule?whatGridIs="+whatGridIs,
			$("#submit_form"),
			"POST",
			callback_GridModule);	
}

//callback 함수
var callback_GridModule = function(result) {
json_string_callback_common(result,'#tableList_module',true);
//fnControlPaging(result);
};

function getGridAction(whatGridIs){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList_action').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Action"," "); 
	
	ajaxCall("/SQLPerformanceStatistics/GridAction?whatGridIs="+whatGridIs,
			$("#submit_form"),
			"POST",
			callback_GridAction);	
}
var callback_GridAction = function(result) {
	json_string_callback_common(result,'#tableList_action',true);
	//fnControlPaging(result);
	};


function getGridParsingSchema(whatGridIs){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList_parsingSchema').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Parsing Schema"," "); 
	
	ajaxCall("/SQLPerformanceStatistics/GridGridParsingSchema?whatGridIs="+whatGridIs,
			$("#submit_form"),
			"POST",
			callback_GridParsingSchema);	
}
var callback_GridParsingSchema = function(result) {
	json_string_callback_common(result,'#tableList_parsingSchema',true);
	//fnControlPaging(result);
};



function getGridResultList(whatGridIs){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList_result').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("TOP SQL 추이/현황"," "); 
	
	ajaxCall("/SQLPerformanceStatistics/GridTopSqlResultList?whatGridIs="+whatGridIs,
			$("#submit_form"),
			"POST",
			callback_GridResultList);	
}

var callback_GridResultList = function(result) {
	json_string_callback_common(result,'#tableList_result',true);
	//fnControlPaging(result);
};

function initTabCnt(){
	$("#tab0ClickCount").val(0);
	$("#tab1ClickCount").val(0);
	$("#tab2ClickCount").val(0);
}

function Excel_Download(){
	
	if(!formValidationCheck()){ 
		return false;
	}
	if(whatGridIs == ''){
		parent.$.messager.alert('경고','좌측 차트를 선택해 주세요.','warning');
		return false;
	}

	$("#submit_form").attr("action","/SQLPerformanceStatistics/GridTopSqlResultList/ExcelDown?whatGridIs="+whatGridIs);
	$("#submit_form").submit();
}


function cellStyler(value,row,index){
	if(value >= 100){
		return 'background-color:#15ae57;';
	}else if(value < 100){
		var colorVal = 100 - value;
	return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}

function cellStyler_EL(value,row,index){
	var val = row.elapsed_time_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}
function cellStyler_CU(value,row,index){
	var val = row.cpu_time_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}
function cellStyler_EX(value,row,index){
	var val = row.executions_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}
function cellStyler_BU(value,row,index){
	var val = row.buffer_gets_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}
function cellStyler_DI(value,row,index){
	var val = row.disk_reads_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}
function cellStyler_RO(value,row,index){
	var val = row.rows_processed_ratio;
	
	if(val >= 100){
		return 'background-color:#15ae57;';
	}else if(val < 100){
		var colorVal = 100 - val;
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}

function formValidationCheck(){

	if(isSelected != 'Y') {
		parent.$.messager.alert('경고','데이터베이스 또는 인스턴스를 선택해 주세요.','warning');
		return false;
	}
	
	if(!compareAnBDate($('#startDate').textbox('getValue'), $('#endDate').textbox('getValue'))) {
		var msg = "기준 일자를 확인해 주세요.<br>시작일자[" + $('#startDate').textbox('getValue') + "] 종료일자[" + $('#endDate').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	if($("#activity").textbox("getValue") == null || $("#activity").textbox("getValue") == '') {
		parent.$.messager.alert('경고','SQL Activity를 입력해 주세요.','warning');
		return false;
	}

	if($("#activity").textbox("getValue") > 100 || $("#activity").textbox("getValue") < 0) {
		parent.$.messager.alert('경고','SQL Activity를 0이상 100이하로 입력해 주세요.','warning');
		return false;
	}

	if($("#top").textbox("getValue") == null || $("#top").textbox("getValue") == '') {
		parent.$.messager.alert('경고','TOP을 입력해 주세요.','warning');
		return false;
	}
	if($("#top").textbox("getValue") < 0) {
		parent.$.messager.alert('경고','TOP을 0이상 입력해 주세요.','warning');
		return false;
	}

	if($("#startTime").textbox("getValue") == '' || $("#endTime").textbox("getValue") == '') {
 		parent.$.messager.alert('경고','시간을 제대로 입력해 주세요.','warning');
		return false;
	}
	
	
	return true;
}