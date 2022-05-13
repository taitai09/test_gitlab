var sqlAppCheckChart;
var topSqlChart;
var topSqlChart2;

var topSqlColor = '#FFF';

function reportWindowSize() {
	console.log("reportWiddowSize...........called");
	var totalWidth = $('#container').width();
	
	$("#dashboardV2LeftEasyui").css('width', (totalWidth / 2)+"px");
	$("#dashboardV2RightEasyui").css('width', (totalWidth / 2)+"px");
}


Ext.EventManager.onWindowResize(function () {
	window.onresize = reportWindowSize;
	var baseWidth = $("#sqlAppCheckChart").width();
	
	if(sqlAppCheckChart != "undefined" && sqlAppCheckChart != undefined){
		sqlAppCheckChart.setSize(baseWidth);
	}
	
	if(topSqlChart != "undefined" && topSqlChart != undefined){
		topSqlChart.setSize(baseWidth);
	}
	if(topSqlChart2 != "undefined" && topSqlChart != undefined){
		topSqlChart2.setSize(baseWidth);
	}
});	

$(document).ready(function() {
	createSqlAppCheckChart();
	createTopSqlChart();
	createTopSqlChart2();
	
	// DB Check List 01
	$("#dbCheckResultGrid01").datagrid({
		view : naview,
		nowrap : true,
		columns:[[
			{field:'db_name',title:'DB',width:'30%',halign:"center",align:'center'},
			{field:'total_cnt',title:'계',width:'20%',halign:"center",align:'right'},
			{field:'fatal_cnt',title:'Fatal',width:'20%',halign:"center",align:'right'},
			{field:'critical_cnt',title:'Critical',width:'20%',halign:"center",align:'right'},
			{field:'warning_cnt',title:'Warning',width:'20%',halign:"center",align:'right'},
			{field:'info_cnt',title:'Info',width:'20%',halign:"center",align:'right'},
			{field:'check_day',title:'CHECK_DAY',hidden:true}
		]],
		onSelect : function(index, row) {
			$('#submit_form_left #dbid').val(row.dbid);
			
			/* DB별 등급 개수 */
			ajaxCall("/DashboardV2/listGradeForDb", $("#submit_form_left"), "POST", callback_ListGradeForDb);
		},
		onLoadSuccess: function(data){
			if(data == null || data.rows.length == 0) {
				return;
			}
			
			$(this).datagrid("selectRow", 0);
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// DB Check List 02
	$("#dbCheckResultGrid02").datagrid({
		view : naview,
		nowrap : true,
		columns:[[
			{field:'grade',title:'등급',width:'20%',halign:"center",align:'center'},
			{field:'check_pref_nm',title:'점검항목',width:'60%',halign:"center",align:'left'},
			{field:'check_value1',title:'대상',width:'20%',halign:"center",align:'right'}
		]],
		onClickRow : function(index,row) {
			var menuId = $("#menu_id").val()+1;
			var menuNm = "일 예방 점검";
			var menuUrl = "/PreventiveCheck";
			var menuParam = 
				"dbid=" + row.dbid
				+ "&check_day=" + row.check_day
				+ "&check_seq=" + row.check_seq
				+ "&call_from_parent=Y"
				;
			console.log(menuParam);
			createNewTab(menuId, menuNm, menuUrl, menuParam);
		},
		onLoadSuccess: function(row,data){
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// SQL / APP Check List 01
	$("#sqlAppCheckGrid01").datagrid({
		view : naview,
		nowrap : true,
		columns:[[
			{field:'db_name',title:'DB',width:'80%',halign:"center",align:'center'},
			{field:'cnt',title:'대상',width:'35%',halign:"center",align:'right'}
		]],
		onClickRow : function(index,row) {
			$("#submit_form_left #dbid").val(row.dbid);
			
			/* SQL/APP 현황 챠트 */
			ajaxCall("/DashboardV2/chartSqlAppCheckDb", $("#submit_form_left"), "POST", callback_ChartSqlAppCheckDb);
			
			/* SQL/APP 진단현황 */
			ajaxCall("/DashboardV2/listSqlAppDiagStatus", $("#submit_form_left"), "POST", callback_ListSqlAppDiagStatus)
		},
		onLoadSuccess: function(row,data){
//			$(this).datagrid('mergeCells',{
//				index: 5,
//				field: 'tot_sum',
//				colspan: 4,
//				rowspan:0
//			});
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// SQL / APP Check List 02
	$("#sqlAppCheckGrid02").datagrid({
		view : naview,
		nowrap : true,
		columns:[[
			{field:'diag_type_nm',title:'점검 대상',width:'50%',halign:"center",align:'left',formatter:cellBackgroundColorSqlAppCheck},
			{field:'d_1',title:'전일',width:'17%',halign:"center",align:'right'},
			{field:'increase',title:'증가',width:'17%',halign:"center",align:'right'},
			{field:'d_2',title:'전전일',width:'17%',halign:"center",align:'right'},
			{field:'last_week',title:'최근 1주일',width:'22%',halign:"center",align:'right'},
			{field:'dbid', hidden:true},
			{field:'diag_type_cd',hidden:true},
			{field:'grp_cd_id',hidden:true}
		]],
		onClickRow : function(index,row) {
			var menuId = $("#menu_id").val()+1;
			var menuNm = null;
			var menuUrl = null;
			var menuParam = null;
			
			if(index <= 9) {
				menuNm = "SQL 진단";
				menuUrl = "/SQLDiagnostics/Summary";
				menuParam = 
					"dbid=" + row.dbid
					+ "&gather_day=" + row.base_day
					+ "&call_from_parent=Y"
					;
			} else {
				menuNm = "애플리케이션 진단";
				menuUrl = "/ApplicationDiagnostics/Summary";
				menuParam = 
					"wrkjob_cd=" + row.wrkjob_cd
					+ "&gather_day=" + row.base_day
					+ "&call_from_parent=Y"
					;
			}
			
			createNewTab(menuId, menuNm, menuUrl, menuParam);
		},
		onLoadSuccess: function(row,data){
			$(this).datagrid('mergeCells',{
				index: 5,
				field: 'tot_sum',
				colspan: 4,
				rowspan:0
			});
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// TOP SQL List 01
	$("#topSqlGrid01").datagrid({
		view : naview,
		nowrap : true,
		columns:[[
			{field:'db_name',title:'DB',width:'80%',halign:"center",align:'center',formatter:formatStrAndColor},
			{field:'cnt',title:'대상',width:'35%',halign:"center",align:'right'}
		]],
		onSelect : function(index,row) {
			$("#submit_form_left #dbid").val(row.dbid);
			
			//console.log("before select color:" + topSqlColor); // debug
			topSqlColor = row.rgb_color_value;
			//console.log("select color:" + topSqlColor); // debug
			
			/* TOPSQL 목록 */
			ajaxCall("/DashboardV2/listTopSql", $("#submit_form_left"), "POST", callback_ListTopSql);
			
			/* TOPSQL 챠트 */
			ajaxCall("/DashboardV2/chartTopSql", $("#submit_form_left"), "POST", callback_ChartTopSql);
			/* TOPSQL 챠트2 */
			ajaxCall("/DashboardV2/chartTopSql2", $("#submit_form_left"), "POST", callback_ChartTopSql2);
		},			
		onLoadSuccess: function(data){
			if(data == null || data.rows.length == 0) {
				return;
			}
			
			$(this).datagrid("selectRow", 0);
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// TOP SQL List 02
	$("#topSqlGrid02").datagrid({
		view : naview,
		nowrap : true,
		onSelect : function(index, row){
			if(row.sql_id.indexOf(".") != -1  || row.dbid == null || row.dbid == ''){
				return false;
			}
			
			var menuParam = "sql_id="+row.sql_id+"&dbid="+row.dbid+"&gather_day="+row.gather_day;
			var menuId = '131';
			/* 탭 생성 */
			parent.openLink("Y", menuId, "SQL 성능 분석", "/SQLPerformance", menuParam);
		},
		columns:[[
			{field:'sql_id',title:'SQL_ID',width:'35%',halign:"center",align:'left'},
			{field:'elapsed_time',title:'E.T',width:'16%',halign:"center",align:'right'},
			{field:'buffer_gets',title:'B.G',width:'25%',halign:"center",align:'right'},
			{field:'executions',title:'Exec',width:'16%',halign:"center",align:'right'},
			{field:'ratio_buffer_gets',title:'Activity',width:'20%',halign:"center",align:'right'}
//			{field:'sql_id',title:'SQL_ID',width:'35%',halign:"center",align:'left',styler:cellStylerCursorDefault},
//			{field:'elapsed_time',title:'E.T',width:'16%',halign:"center",align:'right',styler:cellStylerCursorDefault},
//			{field:'buffer_gets',title:'B.G',width:'25%',halign:"center",align:'right',styler:cellStylerCursorDefault},
//			{field:'executions',title:'Exec',width:'16%',halign:"center",align:'right',styler:cellStylerCursorDefault},
//			{field:'ratio_buffer_gets',title:'Activity',width:'20%',halign:"center",align:'right',styler:cellStylerCursorDefault}
		]],
		onLoadSuccess: function(row,data){
			$(this).datagrid('mergeCells',{
				index: 5,
				field: 'tot_sum',
				colspan: 4,
				rowspan:0
			});
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	function createTopSqlChart(result){
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
		
		if(topSqlChart != "undefined" && topSqlChart != undefined){
			topSqlChart.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		topSqlChart = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false, 
			renderTo : document.getElementById("topSqlChart"),
			layout : 'fit',
//			innerPadding : {top: 0, left: 10, right: 10, bottom: 0},
			items : [{
				xtype : 'cartesian',
				border: false,
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
//				legend: {
//					docked: 'bottom',
//					marker: {
//						type: 'square'
//					},
//					border: {
//						radius: 0
//					},
//					type: 'dom'		// remove border from Legend 
//				},
				type:'text',
				innerPadding : '10 10 5 5',// 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				store : {
					data : data
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
					title : false
				},{
					type : 'category',
					position : 'bottom',
					grid: false,
					label : {
						x : 0,
						y : 0
					},
					fields: ['base_day'],
					renderer: function(v) {
						return v.config.renderer["arguments"][1].split(' ')[0];	// xField Label 재정의
					}
				}],
				series : {
					type : 'line',
					marker: {
						radius: 2,
						lineWidth: 2
					},
					xField : 'base_day',
					yField : ['cnt'],
					title : '', // 'CPU 사용률 추이',
					//colors: [CpuUtilization.colours.SQL_TIMEOUT_LIMIT],	// Label 색상 설정
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
							var xField = record.get(item.series.getXField());
							var title = record.data.db_name;
							
							tooltip.setHtml("[" + title + "] " + xField + " : " + yField);
						}
					}
				},
				listeners: {
					itemclick: function (chart, item, event) {
						var data = item.record.data;
						var menuId = $("#menu_id").val()+1;
						var menuNm = "수동 선정";
//						var menuUrl = "/ManualSelection";
						var menuUrl = "/sqlPerformanceDesign/manualSelection/manualSelectionDesign";
						var menuParam = 
							"dbid=" + data.dbid
							+ "&start_snap_id=" + data.start_snap_id
							+ "&end_snap_id=" + data.end_snap_id
							+ "&elapsed_time=" + data.elapsed_time_threshold
							+ "&buffer_gets=" + data.buffer_gets_threshold
							+ "&executions=" + data.executions_threshold
							+ "&topn_cnt=" + data.topsql_cnt
							+ "&selectOrdered=" + 3
							+ "&before_choice_sql_except_yn=" + "N"
							+ "&call_from_parent=Y" 
							;
						
						createNewTab(menuId, menuNm, menuUrl, menuParam);
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
	
	function createTopSqlChart2(result){
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
		
		if(topSqlChart2 != "undefined" && topSqlChart2 != undefined){
			topSqlChart2.destroy();		
		}
		
//		Ext.define("CpuUtilization.colours", {	// Label 색상 정의
//			singleton:  true,
//			SQL_TIMEOUT_LIMIT: '#000',
//			AVG_ELAPSED_TIME: '#0F4',
//			PAST_ELAPSED_TIME_TREND: '#00F',
//			FUTURE_ELAPSED_TIME_TREND: '#F00'
//		});
		
		topSqlChart2 = Ext.create("Ext.panel.Panel",{
			width : '100%',
			height : '100%',
			border : false,
			renderTo : document.getElementById("topSqlChart2"),
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
					fields: ['sql_id'],
					label : {
						textAlign : 'right',
					}
				}],
				series : {
					type : 'bar',
					style: {
						opacity: 0.8,
						minGapWidth: 2,
					},
					xField : 'sql_id',
					yField : ['cnt'],
					title : '',
//					colors : [topSqlChart2.colors.CNT],
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
				},
				listeners: {
					itemclick: function (chart, item, event) {
						var data = item.record.data;
//						var menuId = $("#menu_id").val()+1;
						var menuId = '131';
						var menuNm = "SQL 성능 분석";
//						var menuUrl = "/ManualSelection";
						var menuUrl = "/SQLPerformance";
						var menuParam = 
							"dbid=" + data.dbid
							+ "&sql_id=" + data.sql_id
							+ "&plan_hash_value=" + data.plan_hash_value
							;
						
						createNewTab(menuId, menuNm, menuUrl, menuParam);
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
	
	$("#topSqlTab").tabs({
		plain: true,
		onSelect:function(title, index){

			if(index == 0){
				//topSqlChart
				/* SQL/APP 현황 챠트 */
				ajaxCall("/DashboardV2/chartTopSql", $("#submit_form_left"), "POST", callback_ChartTopSql);
				
			}else if(index == 1){
				createTopSqlChart2();
				//topSqlChart2
				/* SQL/APP 현황 챠트 */
				ajaxCall("/DashboardV2/chartTopSql2", $("#submit_form_left"), "POST", callback_ChartTopSql2);
			}
		}
	});
	
	
	OnSearch();
});

function createSqlAppCheckChart(){
	if(sqlAppCheckChart != "undefined" && sqlAppCheckChart != undefined){
		sqlAppCheckChart.destroy();		
	}
//	Ext.define("sqlAppCheckChart.colors", {	// Label 색상 정의
//		singleton:  true,
//		PLAN_CHANGE_SQL:'#95AD2B',
//		NEW_SQL:'#1361A3',
//		LITERAL_SQL_TEXT:'#A50F22',
//		LITERAL_PLAN_HASH_VALUE:'#FE8627',
//		TEMP_USAGE_SQL:'#FFCF50',
//		FULLSCAN_SQL:'#A51884',
//		DELETE_SQL:'#2BAD9B',
//		TOPSQL:'#0B74BB',
//		OFFLOAD_EFFC_SQL:'#1BB4EB',
//		OFFLOAD_EFFC_REDUCE_SQL:'#96CF65',
//	});
	
	sqlAppCheckChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("sqlAppCheckChart"),
		layout : 'fit',
//		innerPadding : {top: 0, left: 0, right: 0, bottom: 0},
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins : {
				chartitemevents: {
					moveEvents: true
				}
			},
			innerPadding : '10 10 5 5',// 차트안쪽 여백[top, right, bottom, left]
//			insetPadding : 20, // 차트 밖 여백
			store : {
//				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : false,
				minimum: 0,
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
				position : 'bottom',
//				grid: false,
			}],
			series: [
//				{
//					colors: [
//						sqlAppCheckChart.colors.PLAN_CHANGE_SQL,
//						sqlAppCheckChart.colors.NEW_SQL,
//						sqlAppCheckChart.colors.LITERAL_SQL_TEXT,
//						sqlAppCheckChart.colors.LITERAL_PLAN_HASH_VALUE,
//						sqlAppCheckChart.colors.TEMP_USAGE_SQL,
//						sqlAppCheckChart.colors.FULLSCAN_SQL,
//						sqlAppCheckChart.colors.DELETE_SQL,
//						sqlAppCheckChart.colors.TOPSQL,
//						sqlAppCheckChart.colors.OFFLOAD_EFFC_SQL,
//						sqlAppCheckChart.colors.OFFLOAD_EFFC_REDUCE_SQL
//					]
//				}
			]
		}]
	});
}

function tooltipAttribute(value,row, index) {
	return '<span title="' + value + '" class=\"easyui-tooltip\">' + value + '</span>';
}

function formatStrAndColor(value,row){
	var rgb_color_value = row.rgb_color_value;
	
	return formatTableStyle(value, rgb_color_value);
}

function formatTableStyle(value, rgb_color_value) {
	return '<table style="width:100%;border:0px;table-layout: fixed;">' +
				'<col style="width:90%;"/>' +
				'<col style="width:10%;"/>' +
				'<tr>' +
					'<td style="border:0px">&nbsp;' + value + '</td>' +
					'<td style="border:0px;background-color:'+rgb_color_value+';"></td>' +
				'</tr>' +
			'</table>';
}

function formatStyle(value,rgb_color_value, default_text){
	return  '<span title="' + default_text + '" class=\"easyui-tooltip\">' + formatTableStyle(value, rgb_color_value) + '</span>';
}

function cellBackgroundColorSqlAppCheck(value,row){
	var color = "";
	
	switch(row.diag_type_cd) {
	case "001":		// 플랜변경
		color = "#94ae0a";
		break;
	case "002":		// 신규
		color = "#115fa6";
		break;
	case "003":		// LITERAL(SQL_TEXT기반)
		color = "#a61120";
		break;
	case "004":		// LITERAL(PLAN_HASH_VALUE기반)
		color = "#ff8809";
		break;
	case "005":		// TEMP과다사용
		color = "#ffd13e";
		break;
	case "006":		// FULLSCAN
		color = "#a61187";
		break;
	case "007":		// 조건절없는DELETE
		color = "#24ad9a";
		break;
	case "1":		// TIMEOUT
		color = "#7c7474";
		break;
	case "2":		// 응답시간증가
		color = "#a66111";
		break;
		
	case "009":		// TOPSQL
		color = "#0672be";
		break;
	case "011":		//  OFFLOAD 비효율 SQL
		color = "#11b2ee";
		break;
	case "012":		//  OFFLOAD 효율저하 SQL
		color = "#94d05b";
		break;
		
		
		
	default:
		color = "#FFF";
		break;
	}
	
	return formatStyle(row.diag_type_nm, color, row.defaultText);
}

function OnSearch(){
//	initializeDbCheckResult();
	$('#gather_day_dash').val($('#gather_day_dash').val().replace(/-/g, ''));
	$('#check_date_topsql_diag_summary').val($('#check_date_topsql_diag_summary').val().replace(/-/g, ''));
	
	/* 등급별 전체 개수 */
	ajaxCall("/DashboardV2/totalCntGrade", $("#submit_form_left"), "POST", callback_TotalCntGrade);
	
	/* DB별 등급 개수 */
	ajaxCall("/DashboardV2/cntGradePerDb", $("#submit_form_left"), "POST", callback_CntGradePerDb);
	
	/* SQL/APP 진단 DB 목록 */
	ajaxCall("/DashboardV2/listSqlAppCheckDb", $("#submit_form_left"), "POST", callback_ListSqlAppCheckDb);
	
	/* TOPSQL DB목록 */
	ajaxCall("/DashboardV2/listTopSqlPerDb", $("#submit_form_left"), "POST", callback_ListTopSqlPerDb);
}

function reloadDbCheckResultGrid01(check_grade_cd) {
	$('#submit_form_left #check_grade_cd').val(check_grade_cd);
	
	/* DB별 등급 개수 */
	ajaxCall("/DashboardV2/reloadDbCheckResultGrid01", $("#submit_form_left"), "POST", callback_CntGradePerDb);
}

function initializeDbCheckResult() {
	$('#class_1').html('0');
	$('#class_2').html('0');
	$('#class_3').html('0');
	$('#class_4').html('0');
	
	$('#dbCheckResultGrid01').datagrid("loadData", []);
	$('#dbCheckResultGrid02').datagrid("loadData", []);
}

var callback_TotalCntGrade = function(result) {
	if(!result.result) {
		$('#class_1').html('Fatal : 0');
		$('#class_2').html('Critical : 0');
		$('#class_3').html('Warning : 0');
		$('#class_4').html('Info : 0');
	} else{
		var len = result.object.length;
		
		for(var index = 0; index < len; index++) {
			var obj = result.object[index];
			
			switch(Number(obj.grade)) {
			case 0:
				$('#class_4').html('Info : ' + obj.cnt);
				break;
			case 1:
				$('#class_3').html('Warning : ' + obj.cnt);
				break;
			case 2:
				$('#class_2').html('Critical : ' + obj.cnt);
				break;
			case 3:
				$('#class_1').html('Fatal : ' + obj.cnt);
				break;
			default:
				break;
			}
		}
	}
}

var callback_CntGradePerDb = function(result) {
	$('#dbCheckResultGrid01').datagrid("loadData", []);
	$('#dbCheckResultGrid02').datagrid("loadData", []);
	
	json_string_callback_common(result,'#dbCheckResultGrid01',true);
}

var callback_ListGradeForDb = function(result) {
	$('#dbCheckResultGrid02').datagrid("loadData", []);
	
	json_string_callback_common(result,'#dbCheckResultGrid02',true);
}

var callback_ListSqlAppCheckDb = function(result) {
	$('#sqlAppCheckGrid01').datagrid("loadData", []);
	json_string_callback_common(result,'#sqlAppCheckGrid01',true);
	
	var rows = JSON.parse(result).rows;
	
	if(rows.length == 0) return;
	
	var row = rows[0];
	
	$('#submit_form_left #dbid').val(row.dbid);
	
	/* SQL/APP 현황 챠트 */
	ajaxCall("/DashboardV2/chartSqlAppCheckDb", $("#submit_form_left"), "POST", callback_ChartSqlAppCheckDb);
	
	/* SQL/APP 진단현황 */
	ajaxCall("/DashboardV2/listSqlAppDiagStatus", $("#submit_form_left"), "POST", callback_ListSqlAppDiagStatus);
}

var callback_ChartSqlAppCheckDb = function(result) {
	
	var store;
	var data;
	var chart = this.sqlAppCheckChart.down("chart");
	var nameArray = [];

	Ext.define("sqlAppCheckChart.colors", {	// Label 색상 정의
		singleton:  true,
		PLAN_CHANGE_SQL:'#95AD2B',
		NEW_SQL:'#1361A3',
		LITERAL_SQL_TEXT:'#A50F22',
		LITERAL_PLAN_HASH_VALUE:'#FE8627',
		TEMP_USAGE_SQL:'#FFCF50',
		FULLSCAN_SQL:'#A51884',
		DELETE_SQL:'#2BAD9B',
		TOPSQL:'#0B74BB',
		OFFLOAD_EFFC_SQL:'#1BB4EB',
		OFFLOAD_EFFC_REDUCE_SQL:'#96CF65'
	});
	
	var colors2 = [
		sqlAppCheckChart.colors.PLAN_CHANGE_SQL,
		sqlAppCheckChart.colors.NEW_SQL,
		sqlAppCheckChart.colors.LITERAL_SQL_TEXT,
		sqlAppCheckChart.colors.LITERAL_PLAN_HASH_VALUE,
		sqlAppCheckChart.colors.TEMP_USAGE_SQL,
		sqlAppCheckChart.colors.FULLSCAN_SQL,
		sqlAppCheckChart.colors.DELETE_SQL,
		sqlAppCheckChart.colors.TOPSQL,
		sqlAppCheckChart.colors.OFFLOAD_EFFC_SQL,
		sqlAppCheckChart.colors.OFFLOAD_EFFC_REDUCE_SQL,
		'#7c7474',  // TIMEOUT
		'#a5601e'  // 응답시간증가
	];
	
	if(result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			nameArray.push({
				fill : false,
				style : {
					opacity: 1,
					'stroke-width': 2
				},
				type : 'line',
				xField : 'base_day',
				yField : post.diag_type_nm,
				title : post.diag_type_nm,
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var yField = record.get(item.series.getYField()) == null ? '0' : record.get(item.series.getYField());
						var xField = record.get(item.series.getXField());
						var title = record.data.db_name;
						
						tooltip.setHtml("[" + item.series.getTitle() + "] " + xField + " : " + yField);
					}
				}
			});
		}
		
		chart.setSeries(nameArray);
		data = JSON.parse(result.txtValue);
		
		
		store = sqlAppCheckChart.down("chart").getStore();
		store.loadData(data.rows);

		chart.setColors(colors2);
		chart.redraw();
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_ListSqlAppDiagStatus = function(result) {
	$('#sqlAppCheckGrid02').datagrid("loadData", []);
	json_string_callback_common(result,'#sqlAppCheckGrid02',true);
}

var callback_ListTopSqlPerDb = function(result) {
	$('#topSqlGrid01').datagrid("loadData", []);
	json_string_callback_common(result,'#topSqlGrid01',true);
	
	var rows = JSON.parse(result).rows;
	
	if(rows.length == 0) return;
	
	var row = rows[0];
	
	$('#submit_form_left #dbid').val(row.dbid);

	this.topSqlColor = row.rgb_color_value;
	
	/* SQL/APP 진단현황 */
	ajaxCall("/DashboardV2/listTopSql", $("#submit_form_left"), "POST", callback_ListTopSql);
	
	/* SQL/APP 현황 챠트 */
	ajaxCall("/DashboardV2/chartTopSql", $("#submit_form_left"), "POST", callback_ChartTopSql);
	/* SQL/APP 현황 챠트 */
	ajaxCall("/DashboardV2/chartTopSql2", $("#submit_form_left"), "POST", callback_ChartTopSql2);
}

var callback_ListTopSql = function(result) {
	$('#topSqlGrid02').datagrid("loadData", []);
	json_string_callback_common(result,'#topSqlGrid02',true);
}

var callback_ChartTopSql = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = this.topSqlChart.down("chart").getStore();
	store.loadData(data.rows);
	
	//console.log("callback_ChartTopSql topSqlColor:" + this.topSqlColor);  // debug
	
	var colors = [this.topSqlColor];
	
	this.topSqlChart.down("chart").setColors(colors);
	this.topSqlChart.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_ChartTopSql2 = function(result) {
	var store;
	var data = JSON.parse(result);
	
	store = this.topSqlChart2.down("chart").getStore();
	store.loadData(data.rows);
	
	//console.log("callback_ChartTopSql topSqlColor:" + this.topSqlColor);  // debug
	
	var colors = [this.topSqlColor];
	
	this.topSqlChart2.down("chart").setColors(colors);
	this.topSqlChart2.down("chart").redraw();
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}
