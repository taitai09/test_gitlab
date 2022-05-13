var improvementsChart;
var tunerJobChart;
var preventionChart;
var riskDiagnosisChart;
var tuningProgressChart;
var objectChangeChart;

var risk_jsp_page = [[ '001','databaseStatus' ],
	[ '004','instanceStatus' ],
	[ '005','listenerStatus' ],
	[ '006','dbfiles' ],
	[ '019','backgroundDumpSpace' ],
	[ '020','archiveLogSpace' ],
	[ '021','alertLogSpace' ],
	[ '022','fraSpace' ],
	[ '023','asmDiskgroupSpace' ],
	[ '024','tablespace' ],
	[ '026','invalidObject' ],
	[ '029','unusableIndex' ],
	[ '032','sequence']
	];

Ext.EventManager.onWindowResize(function () {
    var width1 = $("#improvementsChart").width();
    var width2 = $("#tunerJobChart").width();
    var width3 = $("#preventionChart").width();
    var width4 = $("#objectChangeChart").width();
    
    if(improvementsChart != "undefined" && improvementsChart != undefined){
    	improvementsChart.setSize(width1);		
	}
    if(tunerJobChart != "undefined" && tunerJobChart != undefined){
    	tunerJobChart.setSize(width2);	
	}
    if(preventionChart != "undefined" && preventionChart != undefined){
    	preventionChart.setSize(width3);
	}
    if(riskDiagnosisChart != "undefined" && riskDiagnosisChart != undefined){
    	riskDiagnosisChart.setSize(width3);
	}
    if(tuningProgressChart != "undefined" && tuningProgressChart != undefined){
    	tuningProgressChart.setSize(width3);
	}
    if(objectChangeChart != "undefined" && objectChangeChart != undefined){
    	objectChangeChart.setSize(width4);
	}
});	

$(document).ready(function() {
	
	fnUpdateSearchBtnClickFlag();
	
	//createImprovementsChart();
	//createTunerJobChart();
	//DB 예방 점검 현황
	createPreventionChart();
	//오브젝트 변경 현황(최근 일주일)
	//createObjectChangeChart();
	//튜너미할당요청목록	
	$("#tuningRequestList").datagrid({
		view: myview,
		nowrap : false,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'chk',width:"100",halign:"center",align:"center",checkbox:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_no',hidden:"true"},
			{field:'dbid',hidden:"true"},			
			{field:'choice_div_cd',hidden:'true'},
			{field:'tuning_requester_nm',title:'요청자',width:'5%',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝기한일',halign:"center",align:'center',sortable:"true"},			
			{field:'request_why',title:'요청사유',halign:"center",align:'left',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	

	$("#riskDiagnosisList").datagrid({
		view: myview,
		nowrap : false,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd;
			
			createNewTab(menuParam);
		},			
		columns:[[
			{field:'dbid',title:'DBID',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'db_name',title:'DB',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'ordering',title:'ORDERING',width:"7%",halign:"center",align:'center',sortable:"true"},
			{field:'plan_change_sql',title:'플랜변경',width:"7%",halign:"center",align:'center',sortable:"true"},
			{field:'new_sql',title:'신규',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'literal_sql_text',title:'LITERAL(SQL_TEXT기반)',width:"7%",halign:"center",align:'center',sortable:"true"},
			{field:'literal_plan_hash_value',title:'LITERAL(PLAN_HASH_VALUE기반)',width:"7%",halign:"center",align:'center',sortable:"true"},
			{field:'temp_usage_sql',title:'TEMP과다사용',width:"8%",halign:"center",align:'center',sortable:"true"},			
			{field:'fullscan_sql',title:'FULLSCAN',width:"8%",halign:"center",align:'center',sortable:"true"},			
			{field:'delete_sql',title:'조건절없는DELETE',width:"15%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	//튜닝 지연 목록
	$("#tuningDelayList").datagrid({
		view: myview,
		nowrap : false,
		onClickRow : function(index,row) {
			var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd;

			createNewTab("110", "성능 개선 관리", "/ImprovementManagementView", menuParam)
		},			
		columns:[[
			{field:'tuning_no',title:'TUNING_NO',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd',hidden:'true'},
			{field:'choice_div_cd_nm',title:'선정구분',halign:"center",align:'center',sortable:"true"},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_cd',hidden:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',halign:"center",align:'center',sortable:"true"},						
			{field:'perfr_nm',title:'담당튜너',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_cd_nm',title:'요청업무',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:"120px",halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_due_dt',title:'튜닝완료요청일자',halign:"center",align:'center',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'batch_work_div_cd_nm',title:'배치작업구분',halign:"center",align:'center',sortable:"true"}
		]],
    	//fitColumns:true,
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	//긴급 조치 대상
	$("#urgentActionList").datagrid({
		view: myview,
		nowrap : false,
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			$("#dbid").val(row.dbid);
			var jsp_page = getJspPage(row.check_pref_id);
			$("#check_item_name").val(jsp_page);
			$("#check_day").val(row.check_day);
			$("#check_seq").val(row.check_seq);
			$("#strStartDt").val(row.check_dt);

			//console.log("index:"+index+" field:"+field+" value:"+value);
			//console.log("check_day:"+row.check_day+" check_seq:"+row.check_seq+" check_dt:"+row.check_dt+" jsp_page:"+jsp_page);
			if(field != "action_btn"){
				createNewTab1(row.dbid, row.check_pref_id, row.check_tbl);
			}
		},		
//		onClickRow : function(index,row) {
//			$("#dbid").val(row.dbid);
//			var jsp_page = getJspPage(row.check_pref_id);
//			$("#check_item_name").val(jsp_page);
//			$("#check_day").val(row.check_day);
//			$("#check_seq").val(row.check_seq);
//			$("#strStartDt").val(row.check_day);
//
//		},			
		columns:[[
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'dbid',title:'DBID',halign:"center",align:'center',sortable:"true"},
			{field:'check_pref_id',hidden:"true"},
			{field:'check_day',hidden:"true"},
			{field:'check_seq',hidden:"true"},
			{field:'check_dt',hidden:"true"},
			{field:'check_tbl',hidden:"true"},
			{field:'check_pref_nm',title:'리스크 유형',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_sbst',title:'내용',halign:"center",align:'left',sortable:"true"},						
			{field:'emergency_action_yn',title:'조치여부',halign:"center",align:'center',sortable:"true"},						
			{field:'emergency_actor_nm',title:'조치자',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_dt',title:'조치일시',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_no',hidden:"true"},
			{field:'emergency_action_target_id',hidden:"true"},
			{field:'action_btn',title:'조치 처리',width:"100",halign:"center",align:"center",formatter:actionBtn}
		]],
		onLoadSuccess:function(){
			$(this).datagrid('getPanel').find('a.easyui-linkbutton').linkbutton();
		},
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$("#checkTab").tabs({
		plain: true,
		onSelect:function(title, index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
				if(index == 0){
					createPreventionChart();
				}else if(index == 1){
					createRiskDiagnosisChart();
				}else if(index == 2){
					createTuningProgressChart();
				}else if(index == 3){
					//$('#tuningDelayList').datagrid("reload");
					//$('#tuningDelayList').datagrid('fitColumns', true);
					/* 튜닝 지연 현황 리스트 */
					ajaxCallTuningDelay();			
				}
			}
		}
	});

	$("#objectTab").tabs({
		plain: true,
		onSelect:function(title, index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 5;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();			
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
				if(index == 0){
					ajaxCallUrgentAction();
				}else if(index == 1){
					createObjectChangeChart();
				}
			}
		}
	});
	
	$("#requestTab").tabs({
		plain: true,
		onSelect:function(title, index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 7;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();			
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
				if(index == 0){
					ajaxCallTuningRequest();
				}else if(index == 1){
					createTunerJobChart();
				}
			}
		}
	});	
	OnSearch();
});
//성능 개선 유형 현황
function createImprovementsChart(){
	if(improvementsChart != "undefined" && improvementsChart != undefined){
		improvementsChart.destroy();
	}
	improvementsChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("improvementsChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
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
			series : {
				type : 'bar',
				stacked : false,
				style: {
      			  minGapWidth: 25,
      			  style:'cursor:pointer;'
    			},
				xField : ['impr_type_cd_nm'],
				yField : ['impr_type_cnt'],
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold',
        			style:'cursor:pointer;'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
//						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
//			            sector = item.series.getTitle()[fieldIndex],
//			            value = Ext.util.Format.number(record.get(item.field));
//
//						tooltip.setHtml(sector+" : " + value + " 건");
						tooltip.setHtml(item.record.get('impr_type_cd_nm') + " : " + item.record.get('impr_type_cnt') + " 건");
					}
				}
			}
		}]
	});	
}
//튜너별할당현황
function createTunerJobChart(){
	if(tunerJobChart != "undefined" && tunerJobChart != undefined){
		tunerJobChart.destroy();
	}
	tunerJobChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("tunerJobChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
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
			series : {
				type : 'bar',
				stacked : false,
				style: {
      			  minGapWidth: 25
    			},
				xField : 'perfr_nm',
				yField : 'tuning_cnt',
//				highlight: {
//      				strokeStyle: 'black',
//        			fillStyle: 'gold'
//    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.record.get('perfr_nm') + " : " + item.record.get('tuning_cnt') + " 건");
					}
				}
			}
		}]
	});
	
	/* 튜너별할당현황:튜너별 작업부하 현황 차트 */
	ajaxCall("/Dashboard/TunerJobChart",
			$("#submit_form"),
			"POST",
			callback_TunerJobChartAction);
	
}
//DB 예방 점검 현황
function createPreventionChart(){
	$("#submit_form #tab1ClickCount").val(1);
	
	if(preventionChart != "undefined" && preventionChart != undefined){
		preventionChart.destroy();
	}

	preventionChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("preventionChart"),
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
			legend : {
				docked : 'right',
				style:'cursor:pointer;'
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
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['database','instance','space','object','statistics'],
				title : ['DATABASE','INSTANCE','SPACE','OBJECT','STATISTICS'],
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
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        	var menuId = "118";
			        	var menuNm = "일 예방 점검";
			        	var menuUrl = "/PreventiveCheck";
			        	var menuParam = "dbid="+item.record.get("dbid")+"&db_name="+item.record.get('db_name');

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});	

	/* DB 예방 점검 현황 차트 */
	ajaxCall("/Dashboard/PreventionChart",
			$("#submit_form"),
			"POST",
			callback_PreventionChartAction);	
}
/*--성능 리스크 진단 현황--*/
function createRiskDiagnosisChart(){
	if(riskDiagnosisChart != "undefined" && riskDiagnosisChart != undefined){
		riskDiagnosisChart.destroy();
	}

	riskDiagnosisChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("riskDiagnosisChart"),
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
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['plan_change_sql','new_sql','literal_sql_text','literal_plan_hash_value','temp_usage_sql','fullscan_sql','delete_sql'],
				title : ['플랜변경','신규','LITERAL(SQL)','LITERAL(PLAN)','TEMP과다사용','FULLSCAN','조건절없는DELETE'],
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
				},
				listeners: {
			        itemclick: function (chart, item, event) {			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);

			            var menuId = "100";
			            var menuNm = "";
			            var menuUrl = "";
			            var menuParam = "";
			            
			            if(fieldIndex == 0){
			            	menuId = menuId + "1";
			            	menuNm = "플랜변경";
			            	menuUrl = "/SQLDiagnostics/PlanChangeSQL";				            
			            }else if(fieldIndex == 1){
			            	menuId = menuId + "2";
			            	menuNm = "신규 SQL";
			            	menuUrl = "/SQLDiagnostics/NewSQL";				            
			            }else if(fieldIndex == 2){
			            	menuId = menuId + "3";
			            	menuNm = "LITERAL(SQL_TEXT기반)";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=1";
			            }else if(fieldIndex == 3){
			            	menuId = menuId + "4";
			            	menuNm = "LITERAL(PLAN_HASH_VALUE기반)";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=2";				            
			            }else if(fieldIndex == 4){
			            	menuId = menuId + "5";
			            	menuNm = "TEMP과다사용";
			            	menuUrl = "/SQLDiagnostics/TempOveruseSQL";				            
			            }else if(fieldIndex == 5){
			            	menuId = menuId + "6";
			            	menuNm = "FULL SCAN";
			            	menuUrl = "/SQLDiagnostics/FullScan";				            
			            }else if(fieldIndex == 6){
			            	menuId = menuId + "7";
			            	menuNm = "조건절없는DELETE";
			            	menuUrl = "/SQLDiagnostics/DeleteWithoutCondition";				            
			            }
			            
			            menuParam = "dbid="+item.record.get("dbid")+"&strStartDt="+item.record.get("gather_day")+"&strEndDt="+item.record.get("gather_day");
 
			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});	

	/* 성능 리스크 진단 현황 리스트 */
	ajaxCall("/Dashboard/RiskDiagnosisChart",
			$("#submit_form"),
			"POST",
			callback_RiskDiagnosisChartAction);	
}
//튜닝 진행 목록
function createTuningProgressChart(){
	if(tuningProgressChart != "undefined" && tuningProgressChart != undefined){
		tuningProgressChart.destroy();
	}
	
	var newLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right'
	});	

	tuningProgressChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("tuningProgressChart"),
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
			legend : newLegend,
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
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 25,
      			  cursor:'hand'
    			},
				xField : 'db_name',
				yField : ['process_1','process_2','process_3','process_6','process_8','process_7'],
				title : ['요청','접수','튜닝중','적용대기','튜닝완료','적용반려'],
				stacked: true,
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
				},
				listeners: {
			        itemclick: function (chart, item, event) {			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);

			        	var menuId = "110";
			        	var menuNm = "성능 개선 관리";
			        	var menuUrl = "/ImprovementManagement";
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            if(fieldIndex == 0){
			            	strCd = "2";
			            }else if(fieldIndex == 1){
			            	strCd = "3";
			            }else if(fieldIndex == 2){
			            	strCd = "5";
			            }else if(fieldIndex == 3){
			            	strCd = "6";
			            }else if(fieldIndex == 4){
			            	strCd = "7";
			            }else if(fieldIndex == 5){
			            	strCd = "8";
			            }
			            
			            menuParam = "dbid="+item.record.get("dbid")+"&day_gubun=ALL&tuning_status_cd="+strCd;
			            
			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});		

	/* 튜닝 진행 현황 차트 */
	ajaxCall("/Dashboard/TuningProgressChart",
			$("#submit_form"),
			"POST",
			callback_TuningProgressChartAction);
}
//오브젝트 변경 현황(최근 일주일)
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
			legend : {
				docked : 'right'
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
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
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['table_create','table_drop','table_modify','column_add','column_drop','column_modify','index_create','index_drop','index_modify'],
				title : ['TABLE_CREATE','TABLE_DROP','TABLE_MODIFY','COLUMN_ADD','COLUMN_DROP','COLUMN_MODIFY','INDEX_CREATE','INDEX_DROP','INDEX_MODIFY'],
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
				},
				listeners: {
			        itemclick: function (chart, item, event) {			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			        	var object_change_type = item.field;

			        	var menuId = "191";
			        	var menuNm = "오브젝트 변경 분석";
			        	var menuUrl = "/ObjectChangeAnalysis";
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            menuParam = "dbid="+item.record.get("dbid")+"&base_day_gubun=2&object_change_type="+object_change_type;

			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});		
	/* 오브젝트 변경 현황 차트 */
	ajaxCall("/Dashboard/ObjectChangeChart", $("#submit_form"), "POST", callback_ObjectChangeChartAction);
}

function OnSearch(){
	$('#tuningRequestList').datagrid("loadData", []);
	$('#tuningDelayList').datagrid("loadData", []);
	$('#urgentActionList').datagrid("loadData", []);
	
	/* 성능 개선 유형 차트 */
	//ajaxCall("/Dashboard/ImprovementsChart", $("#submit_form"), "POST", callback_ImprovementsChartAction);

	/* 튜너미할당요청목록:튜닝 요청현황 리스트 */
	ajaxCallTuningRequest();
	
	/* 긴급 조치 대상 리스트 */
	ajaxCallUrgentAction();	
}

//callback 함수
var callback_ImprovementsChartAction = function(result) {
	chart_callback(result, improvementsChart);
};

//callback 함수
var callback_TunerJobChartAction = function(result) {
	chart_callback(result, tunerJobChart);
};
/* 튜너미할당요청목록:튜닝 요청현황 리스트 */
//callback 함수
var callback_TuningRequestAction = function(result) {
	var data = JSON.parse(result);
	$('#tuningRequestList').datagrid("loadData", data);
	$('#tuningRequestList').datagrid('loaded');	
};

//callback 함수
var callback_PreventionChartAction = function(result) {
	chart_callback(result, preventionChart);
};

//callback 함수
var callback_RiskDiagnosisChartAction = function(result) {
	chart_callback(result, riskDiagnosisChart);
};

//callback 함수
var callback_TuningProgressChartAction = function(result) {
	chart_callback(result, tuningProgressChart);
};
/* 튜닝 지연 현황 리스트 */
//callback 함수
var callback_TuningDelayAction = function(result) {
	var data = JSON.parse(result);
	$('#tuningDelayList').datagrid("loadData", data);
	$('#tuningDelayList').datagrid('loaded');	

	//$( ".tabs-panels:eq(0) .panel-htop:eq( 3 ) td" ).css( "cursor", "default" );
};

//callback 함수
var callback_ObjectChangeChartAction = function(result) {
	chart_callback(result, objectChangeChart);

};
/* 긴급 조치 대상 리스트 */
//callback 함수
var callback_UrgentActionAction = function(result) {
	var data = JSON.parse(result);
	$('#urgentActionList').datagrid("loadData", data);
	$('#urgentActionList').datagrid('loaded');
};

//function showTuningAssignAllPopup(){
//	var tuningNoArry = "";
//	var totalCnt = 0;
//	var newAssignCnt = 0; // 튜닝담당자 지정 count
//	var reAssignCnt = 0; // 튜닝담당자 재지정 count
//	var dbCnt = 0;
//	var tempDB = "";
//	var strDbid = "";
//	
//	rows = $('#tuningRequestList').datagrid('getSelections');
//
//	if(rows != null && rows != ""){
//		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//		parent.frameName = $("#menu_id").val();
//		
//		totalCnt = rows.length;
//		parent.$("#assignAll_form #tuningNoArry").val("");
//		
//		for(var i = 0 ; i < rows.length ; i++){
//			tuningNoArry += rows[i].tuning_no + "|";	
//			strDbid = rows[0].dbid;
//			
//			/* 1. 튜닝담당자 지정
//			 *  튜닝담당자가 없고, 요청상태가 "요청"이면서 진행상태가 "요청" 인 경우
//			 * 2. 튜닝담당자 재지정 
//			 *  튜닝담당자가 있고, 진행상태가 "튜닝중","접수" 인 경우 
//			*/
//			if((rows[i].perfr_id == "" || rows[i].perfr_id == null) && (rows[i].choice_div_cd == "3" && rows[i].tuning_status_cd == "2")){
//				newAssignCnt++;
//			}else if((rows[i].perfr_id != "" || rows[i].perfr_id != null) && (rows[i].tuning_status_cd == "3" || rows[i].tuning_status_cd == "5")){
//				reAssignCnt++;
//			}
//
//			// DB가 다른 경우
//			if(rows[i].dbid != tempDB && tempDB != ""){
//				dbCnt++;
//			}else{
//				tempDB = rows[i].dbid;	
//			}
//		}
//
//		if(dbCnt > 0){
//			parent.$.messager.alert('','튜닝담당자 지정하려는 대상에 다른 DB가 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
//			return false;			
//		}else{
//			if((newAssignCnt < totalCnt) && !(reAssignCnt == totalCnt)){
//				parent.$.messager.alert('','요청상태가 \"요청\"이면서 진행상태가 \"요청\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
//				return false;
//			}
//			
//			if((reAssignCnt < totalCnt) && !(newAssignCnt == totalCnt)){
//				parent.$.messager.alert('','진행상태가 \"접수\", \"튜닝중\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
//				return false;
//			}			
//		}
//
//		if(newAssignCnt == totalCnt){
//			parent.$("#assignAll_form #tuningStatusArry").val("N");	
//		}else{
//			parent.$("#assignAll_form #tuningStatusArry").val("R");
//		}
//		
//		// 튜닝담당자 조회
//		parent.$('#assignAll_form #selectTuner').combobox({
//		    url:"/Common/getTuner?dbid="+strDbid,
//		    method:"get",
//			valueField:'tuner_id',
//		    textField:'tuner_nm'
//		});	
//		
//		parent.$("#assignAll_form #tuningNoArry").val(strRight(tuningNoArry,1));
//		parent.$('#tuningAssignAllPop').window("open");
//	}else{
//		parent.$.messager.alert('','튜닝담당자 지정할 대상을 선택해 주세요.');
//	}
//}

function showTuningAssignAllPopup(){
	var tuningNoArry = "";
	var totalCnt = 0;
	var newAssignCnt = 0; // 튜닝담당자 지정 count
	var reAssignCnt = 0; // 튜닝담당자 재지정 count
	var dbCnt = 0;
	var tempDB = "";
	var strDbid = "";
	
	rows = $('#tuningRequestList').datagrid('getSelections');

	if(rows != null && rows != ""){
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		totalCnt = rows.length;
		$("#assignAll_form #tuningNoArry").val("");
		
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";	
			strDbid = rows[0].dbid;
			
			/* 1. 튜닝담당자 지정
			 *  튜닝담당자가 없고, 요청상태가 "요청"이면서 진행상태가 "요청" 인 경우
			 * 2. 튜닝담당자 재지정 
			 *  튜닝담당자가 있고, 진행상태가 "튜닝중","접수" 인 경우 
			*/
			if((rows[i].perfr_id == "" || rows[i].perfr_id == null) && (rows[i].choice_div_cd == "3" && rows[i].tuning_status_cd == "2")){
				newAssignCnt++;
			}else if((rows[i].perfr_id != "" || rows[i].perfr_id != null) && (rows[i].tuning_status_cd == "3" || rows[i].tuning_status_cd == "5")){
				reAssignCnt++;
			}

			// DB가 다른 경우
			if(rows[i].dbid != tempDB && tempDB != ""){
				dbCnt++;
			}else{
				tempDB = rows[i].dbid;	
			}
		}

		if(dbCnt > 0){
			parent.$.messager.alert('','튜닝담당자 지정하려는 대상에 다른 DB가 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
			return false;			
		}else{
			if((newAssignCnt < totalCnt) && !(reAssignCnt == totalCnt)){
				parent.$.messager.alert('','요청상태가 \"요청\"이면서 진행상태가 \"요청\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
				return false;
			}
			
			if((reAssignCnt < totalCnt) && !(newAssignCnt == totalCnt)){
				parent.$.messager.alert('','진행상태가 \"접수\", \"튜닝중\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
				return false;
			}			
		}

		if(newAssignCnt == totalCnt){
			$("#assignAll_form #tuningStatusArry").val("N");	
		}else{
			$("#assignAll_form #tuningStatusArry").val("R");
		}
		
		// 튜닝담당자 조회
		$('#assignAll_form #selectTuner').combobox({
		    url:"/Common/getTuner?dbid="+strDbid,
		    method:"get",
			valueField:'tuner_id',
		    textField:'tuner_nm'
		});	
		
		$("#assignAll_form #tuningNoArry").val(strRight(tuningNoArry,1));
		$('#tuningAssignAllPop').window("open");
	}else{
		parent.$.messager.alert('','튜닝담당자 지정할 대상을 선택해 주세요.');
	}
}

function ajaxCallTuningRequest(){
	$("#submit_form #tab7ClickCount").val(1);
	/* 튜너미할당요청목록:튜닝 요청현황 리스트 */
	ajaxCall("/Dashboard/TuningRequest", $("#submit_form"), "POST", callback_TuningRequestAction);	
}

function ajaxCallTuningDelay(){
	/* 튜닝 지연 현황 리스트 */
	ajaxCall("/Dashboard/TuningDelay", $("#submit_form"), "POST", callback_TuningDelayAction);	
}

function ajaxCallUrgentAction(){
	$("#submit_form #tab5ClickCount").val(1);
	/* 긴급 조치 대상 리스트 */
	ajaxCall("/Dashboard/UrgentAction", $("#submit_form"), "POST", callback_UrgentActionAction);	
}

/* 조치여부 업데이트 */
function actionBtn(val, row) {
	if(row.emergency_action_yn == "N"){
		return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_UpdateUrgentAction(\""+row.dbid+"\",\""+row.emergency_action_no+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 조치완료</a>";	
	}else{
		return "";
	}    
}

function Btn_UpdateUrgentAction(dbid, emergencyActionNo){
	//console.log("emergencyActionNo:"+emergencyActionNo+" dbid:"+dbid);
	$("#emergency_action_no").val(emergencyActionNo);
	$("#dbid").val(dbid);
	
	ajaxCall("/Dashboard/UpdateUrgentAction", $("#submit_form"), "POST", callback_UpdateUrgentActionAction);		
}

//callback 함수
var callback_UpdateUrgentActionAction = function(result) {
	if(result.result){
		parent.$.messager.alert({
			msg : '긴급 조치 처리가 완료되었습니다.',
			fn :function(){
				ajaxCallUrgentAction();
			} 
		});
	}else{
		parent.$.messager.alert('error','긴급 조치 처리가 실패하였습니다.');
	}
};

/* 일 예방 점검 탭 생성 */
function createNewTab1(dbid, check_pref_id, check_tbl){
	var menuUrl = "/PreventiveCheck/DetailCheckInfo";
	var menuParam = "dbid=" + $("#dbid").val() 
			+ "&strStartDt=" + $("#strStartDt").val() 
			+ "&check_item_name=" + $("#check_item_name").val() 
			+ "&check_day=" + $("#check_day").val() 
			+ "&check_seq=" + $("#check_seq").val();
	
			setNewTabInfo(check_pref_id,check_tbl);

	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}

var menuNm = "";
var menu_id = "";
function setNewTabInfo(check_pref_id,check_tbl){
	for(var i=0;i<risk_jsp_page.length;i++){
		var array = risk_jsp_page[i];
		for(var j=0;j<array.length;j++){
			var k = array[0];
			var v = array[1];
			if(check_pref_id == k){
				menuId = $("#menu_id").val() + j;
				break;
			}
		}
	}	
	menuNm = strReplace(check_tbl, "_"," ").toUpperCase(); 
}

function getJspPage(check_pref_id){
	//console.log("check_pref_id :"+check_pref_id);
	for(var i=0;i<risk_jsp_page.length;i++){
		var array = risk_jsp_page[i];
		for(var j=0;j<array.length;j++){
			var k = array[0];
			var v = array[1];
			if(check_pref_id == k){
				return v;
			}
		}
	}
	return "";
}