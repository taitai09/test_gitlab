var performanceChart;

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    performanceChart.setSize(width);
});	

$(document).ready(function() {
	createPerformanceChart();

	$("#waitJobList").datagrid({
		view: myview,
		nowrap : true,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			createTab(menuParam);
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:'true'},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝기한일',halign:"center",align:'center',sortable:"true",formatter:getDateFormat},
			{field:'wrkjob_peculiar_point',title:'업무특이사항',halign:"center",align:'left',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#progressList").datagrid({
		view: myview,
		nowrap : true,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			createTab(menuParam);
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:'true'},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_change_dt',title:'튜닝시작일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_elapsed_time',title:'튜닝경과시간',halign:"center",align:'center',sortable:"true"},			
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝기한일',halign:"center",align:'center',sortable:"true",formatter:getDateFormat},
			{field:'wrkjob_peculiar_point',title:'업무특이사항',halign:"center",align:'left',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#expectedDelayList").datagrid({
		view: myview,
		nowrap : true,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			createTab(menuParam);
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',width:'80',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:'true'},
			{field:'tuning_status_nm',title:'튜닝상태',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},			
			{field:'tuning_elapsed_time',title:'튜닝경과시간',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_delay_time',title:'지연남은시간',halign:"center",align:'center',sortable:"true"},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝기한일',width:"100",halign:"center",align:'center',sortable:"true",formatter:getDateFormat},
			{field:'wrkjob_peculiar_point',title:'업무특이사항',halign:"center",align:'left',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',width:"150",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$("#delayList").datagrid({
		view: myview,
		nowrap : true,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			createTab(menuParam);
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',width:"80",halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:'true'},
			{field:'tuning_status_nm',title:'튜닝상태',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},			
			{field:'request_elapsed_time',title:'요청후 경과시간',halign:"center",align:'center',sortable:"true"},
			{field:'complete_over_time',title:'완료기한일 초과시간',halign:"center",align:'center',sortable:"true"},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝기한일',width:"100",halign:"center",align:'center',sortable:"true",formatter:getDateFormat},
			{field:'wrkjob_peculiar_point',title:'업무특이사항',halign:"center",align:'left',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',width:"150",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#completeList").datagrid({
		view: myview,
		nowrap : true,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			createTab(menuParam);
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:'true'},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',width:'120px',halign:"center",align:'left',sortable:"true"},
			{field:'imprb_elap_time',title:'개선전 수행시간',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'imprb_buffer_cnt',title:'개선전 버퍼수',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'imprb_pga_usage',title:'개선전 PGA',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'impra_elap_time',title:'개선후 수행시간',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'impra_buffer_cnt',title:'개선후 버퍼수',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'impra_pga_usage',title:'개선후 PGA',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'elap_time_impr_ratio',title:'수행시간 개선율',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'buffer_impr_ratio',title:'버퍼 개선율',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'pga_impr_ratio',title:'PGA 개선율',halign:"center",align:'right',formatter:getNumberFormat}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#progressTab").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
//				$('#progressList').datagrid("loadData", []);
				ajaxCallTuningProgress();
			}else if(index == 1){
//				$('#expectedDelayList').datagrid("loadData", []);
				ajaxCallTuningExpectedDelay();
			}else if(index == 2){
//				$('#delayList').datagrid("loadData", []);
				ajaxCallTuningDelay();
			}else{
//				$('#completeList').datagrid("loadData", []);
				ajaxCallTuningComplete();
			}
		}
	});
	
	OnSearch();
});

function createPerformanceChart(){
	if(performanceChart != "undefined" && performanceChart != undefined){
		performanceChart.destroy();
	}
	performanceChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("performanceChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			legend : {
				docked : 'right'
			},
		    innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
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
			}],
			series : [{
				type : 'line',
				stacked : false,
				style: {
      			  minGapWidth: 25
    			},
				xField : 'tuning_request_dt',
				yField : 'tuning_cnt',
				title : '튜닝',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            sector = item.series.getTitle()[fieldIndex],
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(sector+" : " + value + " 건");
					}
				}
			},{
				type : 'line',
				stacked : false,
				style: {
      			  minGapWidth: 25
    			},
				xField : 'tuning_request_dt',
				yField : 'knowledge_cnt',
				title : '사례등록',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            sector = item.series.getTitle()[fieldIndex],
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(sector+" : " + value + " 건");
					}
				}	
			}]
		}]
	});	
}

function OnSearch(){
//	$('#waitJobList').datagrid("loadData", []);
//	$('#progressList').datagrid("loadData", []);
	$('#expectedDelayList').datagrid("loadData", []);
	$('#delayList').datagrid("loadData", []);
	$('#completeList').datagrid("loadData", []);

	ajaxCallTuningWaitJob();
	ajaxCallTuningProgress();
	ajaxCallTuningPerformanceChart();	
}

function ajaxCallTuningWaitJob(){
	/* 튜닝 작업 대기 리스트 */
	ajaxCall("/Dashboard/TuningWaitJob",
			$("#submit_form"),
			"POST",
			callback_TuningWaitJobAction);
}
function ajaxCallTuningProgress(){
	/* 튜닝 진행 리스트 */
	ajaxCall("/Dashboard/TuningProgress",
			$("#submit_form"),
			"POST",
			callback_TuningProgressAction);	
}
function ajaxCallTuningExpectedDelay(){
	/* 튜닝 지연예상 리스트 */
	ajaxCall("/Dashboard/TuningExpectedDelay",
			$("#submit_form"),
			"POST",
			callback_TuningExpectedDelayAction);	
}
function ajaxCallTuningDelay(){
	/* 튜닝 지연 리스트 */
	ajaxCall("/Dashboard/TuningDelay",
			$("#submit_form"),
			"POST",
			callback_TuningDelayAction);	
}
function ajaxCallTuningComplete(){
	/* 주간 튜닝 완료 리스트 */
	ajaxCall("/Dashboard/TuningComplete",
			$("#submit_form"),
			"POST",
			callback_TuningCompleteAction);	
}
function ajaxCallTuningPerformanceChart(){
	/* 튜닝 실직 차트 */
	ajaxCall("/Dashboard/TuningPerformanceChart",
			$("#submit_form"),
			"POST",
			callback_TuningPerformanceChartAction);	
}

//callback 함수
var callback_TuningWaitJobAction = function(result) {
	var data = JSON.parse(result);
	$('#waitJobList').datagrid("loadData", data);
	$('#waitJobList').datagrid('loaded');	
};

//callback 함수
var callback_TuningProgressAction = function(result) {
	var data = JSON.parse(result);
	$('#progressList').datagrid("loadData", data);
	$('#progressList').datagrid('loaded');	
};
//튜닝 지연 예상
//callback 함수
var callback_TuningExpectedDelayAction = function(result) {
	var data = JSON.parse(result);
	$('#expectedDelayList').datagrid("loadData", data);
	$('#expectedDelayList').datagrid('loaded');	
};

//튜닝 지연
//callback 함수
var callback_TuningDelayAction = function(result) {
	var data = JSON.parse(result);
	$('#delayList').datagrid("loadData", data);
	$('#delayList').datagrid('loaded');	
};
//주간 튜닝 완료
//callback 함수
var callback_TuningCompleteAction = function(result) {
	var data = JSON.parse(result);
	$('#completeList').datagrid("loadData", data);
	$('#completeList').datagrid('loaded');	
};

//callback 함수
var callback_TuningPerformanceChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = performanceChart.down("chart").getStore();
	store.loadData(data.rows);
	performanceChart.down("chart").redraw();
};

function createTab(menuParam){
	parent.openLink("Y", "110", "성능 개선 관리 상세", "/ImprovementManagementView", menuParam);	
}