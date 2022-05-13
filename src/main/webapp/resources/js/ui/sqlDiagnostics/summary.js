var dayChart;
var weekChart;
var monthChart;

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    if(dayChart != "undefined" && dayChart != undefined){
    	dayChart.setSize(width);		
	}
    if(weekChart != "undefined" && weekChart != undefined){
    	weekChart.setSize(width);		
	}
    if(monthChart != "undefined" && monthChart != undefined){
    	monthChart.setSize(width);		
	}
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createDayChart();
	//createWeekChart();
	//createMonthChart();
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onChange: function(newValue,oldValue) {
			fnInitSearchBtnClickFlag();
		},
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
		    	var dbid = $("#dbid").val();
		    	if(dbid != '' && dbid != 'null'){
		    		$(this).combobox('setValue', dbid);
		    		
					var call_from_parent = $("#call_from_parent").val();
					if(call_from_parent == "Y"){
//						$('#strStartDt').textbox('setValue',$("#gather_day").val()); //문제있을시 다시 넣음. 20191104 지움.
						Btn_OnClick();
					}		    		
		    	}				
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
		}
	});	
	
	$("#summeryTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			if(searchBtnClickCount == "" || searchBtnClickCount == "0"){
				if($('#selectCombo').combobox('getValue') == ""){
					parent.$.messager.alert('','DB를 선택해 주세요.');
					createDayChart();
					createWeekChart();
					createMonthChart();
					return false;
				}else{
					parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
					createDayChart();
					createWeekChart();
					createMonthChart();
					return false;
				}
			}
			if(index == 0){
				$("#day_gubun").val("D");
				var tab1ClickCount = $("#submit_form #tab1ClickCount").val();
				if(tab1ClickCount < searchBtnClickCount){
					createDayChart();
					$("#submit_form #tab1ClickCount").val(1);
				}
			}else if(index == 1){
				$("#day_gubun").val("W");
				var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
				if(tab2ClickCount < searchBtnClickCount){
					createWeekChart();
					$("#submit_form #tab2ClickCount").val(1);
				}
			}else{
				$("#day_gubun").val("M");
				var tab3ClickCount = $("#submit_form #tab3ClickCount").val();
				if(tab3ClickCount < searchBtnClickCount){
					createMonthChart();
					$("#submit_form #tab3ClickCount").val(1);
				}
			}
			fnSearch();
			
		}
	});
});

function createDayChart(){
	if(dayChart != "undefined" && dayChart != undefined){
		dayChart.destroy();
	}
	dayChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dayChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			flipXY : true, // 가로/세로 축 변경
			innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				majorTickSteps : 20,
				minorTickSteps: 0,
        		minimum: 0,
        		maximum: 20,
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
				position : 'left',
				grid: true,
				label : {
					textAlign : 'right'
				}
			}],
			series : {
				type : 'bar',
				stacked : false, // stack 차트 해제
				style: {
      			  minGapWidth: 10
    			},
				xField : 'sql_diag_type_nm',
				yField : 'diag_cnt',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("sql_diag_type_nm")+" : " + record.get("diag_cnt") + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            var gubun = "";
			            var menuUrl = "";
			            
			            if(item.record.get("sql_diag_type_cd") == "001"){
			            	gubun = "1";
			            	menuUrl = "/SQLDiagnostics/PlanChangeSQL";				            
			            }else if(item.record.get("sql_diag_type_cd") == "002"){
			            	gubun = "2";
			            	menuUrl = "/SQLDiagnostics/NewSQL";				            
			            }else if(item.record.get("sql_diag_type_cd") == "003"){
			            	gubun = "3";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=1";
			            }else if(item.record.get("sql_diag_type_cd") == "004"){
			            	gubun = "4";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=2";				            
			            }else if(item.record.get("sql_diag_type_cd") == "005"){
			            	gubun = "5";
			            	menuUrl = "/SQLDiagnostics/TempOveruseSQL";				            
			            }else if(item.record.get("sql_diag_type_cd") == "006"){
			            	gubun = "6";
			            	menuUrl = "/SQLDiagnostics/FullScan";				            
			            }else if(item.record.get("sql_diag_type_cd") == "007"){
			            	gubun = "7";
			            	menuUrl = "/SQLDiagnostics/DeleteWithoutCondition";         
			            }else if(item.record.get("sql_diag_type_cd") == "009"){     //TOPSQL
			            	gubun = "8";
			            	menuUrl = "/SQLDiagnostics/TopSql";	
				        }else if(item.record.get("sql_diag_type_cd") == "011"){    //OFFLOAD_EFFC_SQL
	            			gubun = "9";
	            			menuUrl = "/SQLDiagnostics/OffloadSql";
						}else if(item.record.get("sql_diag_type_cd") == "012"){     //OFFLOAD_EFFC_REDUCE_SQL
							gubun = "10";
				        	menuUrl = "/SQLDiagnostics/OffloadEffcReduceSql";
						}
			            
			            createNewTab(gubun,item.record.get("sql_diag_type_nm"),menuUrl,item.record.get("min_day"),item.record.get("max_day"));
			        }
			    }
			}
		}]
	});
}

function createWeekChart(){
	if(weekChart != "undefined" && weekChart != undefined){
		weekChart.destroy();
	}
	
	Ext.define("weekChart.colors", {	// Label 색상 정의
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
		OFFLOAD_EFFC_REDUCE_SQL:'#96CF65',
	});
	
	weekChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("weekChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			border : false,
			legend : {
			  type: 'dom',
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
      			  minGapWidth: 10
    			},
				xField : 'gather_day',
				yField : fieldName,
				title : fieldRealName,
				colors: [
					weekChart.colors.PLAN_CHANGE_SQL,
					weekChart.colors.NEW_SQL,
					weekChart.colors.LITERAL_SQL_TEXT,
					weekChart.colors.LITERAL_PLAN_HASH_VALUE,
					weekChart.colors.TEMP_USAGE_SQL,
					weekChart.colors.FULLSCAN_SQL,
					weekChart.colors.DELETE_SQL,
					weekChart.colors.TOPSQL,
					weekChart.colors.OFFLOAD_EFFC_SQL,
					weekChart.colors.OFFLOAD_EFFC_REDUCE_SQL
					],
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
			        	var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			            var sector = item.series.getTitle()[fieldIndex];
			            var gubun = "";
			            var menuUrl = "";
			            
			            if(fieldIndex == 0){
			            	gubun = "1";
			            	menuUrl = "/SQLDiagnostics/PlanChangeSQL";				            
			            }else if(fieldIndex == 1){
			            	gubun = "2";
			            	menuUrl = "/SQLDiagnostics/NewSQL";				            
			            }else if(fieldIndex == 2){
			            	gubun = "3";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=1";
			            }else if(fieldIndex == 3){
			            	gubun = "4";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=2";				            
			            }else if(fieldIndex == 4){
			            	gubun = "5";
			            	menuUrl = "/SQLDiagnostics/TempOveruseSQL";				            
			            }else if(fieldIndex == 5){
			            	gubun = "6";
			            	menuUrl = "/SQLDiagnostics/FullScan";				            
			            }else if(fieldIndex == 6){
			            	gubun = "7";
			            	menuUrl = "/SQLDiagnostics/DeleteWithoutCondition";				            
			            }else if(fieldIndex == 7){
			            	gubun = "8";
			            	menuUrl = "/SQLDiagnostics/TopSql";
				        }else if(fieldIndex == 8){
				        	gubun = "9";
				        	menuUrl = "/SQLDiagnostics/OffloadSql";
						}else if(fieldIndex == 9){
							gubun = "10";
							menuUrl = "/SQLDiagnostics/OffloadEffcReduceSql";
						}
			            
			            createNewTab(gubun,sector,menuUrl,item.record.get("min_day"),item.record.get("max_day"));
			        }
			    }
			}
		}]
	});
}

function createMonthChart(){
	if(monthChart != "undefined" && monthChart != undefined){
		monthChart.destroy();
	}
	
	Ext.define("monthChart.colors", {	// Label 색상 정의
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
		OFFLOAD_EFFC_REDUCE_SQL:'#96CF65',
	});
	
	monthChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("monthChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			legend : {
				type: 'dom',
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
				xField : 'gather_day',
				yField : fieldName,
				title : fieldRealName,
				colors: [
					monthChart.colors.PLAN_CHANGE_SQL,
					monthChart.colors.NEW_SQL,
					monthChart.colors.LITERAL_SQL_TEXT,
					monthChart.colors.LITERAL_PLAN_HASH_VALUE,
					monthChart.colors.TEMP_USAGE_SQL,
					monthChart.colors.FULLSCAN_SQL,
					monthChart.colors.DELETE_SQL,
					monthChart.colors.TOPSQL,
					monthChart.colors.OFFLOAD_EFFC_SQL,
					monthChart.colors.OFFLOAD_EFFC_REDUCE_SQL
					],
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
			        	var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			            var sector = item.series.getTitle()[fieldIndex];
			            var gubun = "";
			            var menuUrl = "";
			            
			            if(fieldIndex == 0){
			            	gubun = "1";
			            	menuUrl = "/SQLDiagnostics/PlanChangeSQL";				            
			            }else if(fieldIndex == 1){
			            	gubun = "2";
			            	menuUrl = "/SQLDiagnostics/NewSQL";				            
			            }else if(fieldIndex == 2){
			            	gubun = "3";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=1";
			            }else if(fieldIndex == 3){
			            	gubun = "4";
			            	menuUrl = "/SQLDiagnostics/LiteralSQL?literal_type_cd=2";				            
			            }else if(fieldIndex == 4){
			            	gubun = "5";
			            	menuUrl = "/SQLDiagnostics/TempOveruseSQL";				            
			            }else if(fieldIndex == 5){
			            	gubun = "6";
			            	menuUrl = "/SQLDiagnostics/FullScan";				            
			            }else if(fieldIndex == 6){
			            	gubun = "7";
			            	menuUrl = "/SQLDiagnostics/DeleteWithoutCondition";				            
			            }else if(fieldIndex == 7){
			            	gubun = "8";
			            	menuUrl = "/SQLDiagnostics/TopSql";				            
			            }else if(fieldIndex == 8){
			            	gubun = "9";
			            	menuUrl = "/SQLDiagnostics/OffloadSql";				            
			            }else if(fieldIndex == 9){
			            	gubun = "10";
			            	menuUrl = "/SQLDiagnostics/OffloadEffcReduceSql";				            
			            }
			            
			            createNewTab(gubun,sector,menuUrl,item.record.get("min_day"),item.record.get("max_day"));
			        }
			    }
			}
		}]
	});
}		

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	var pp = $('#summeryTab').tabs('getSelected');
	var index = pp.panel('options').index;

	fnUpdateSearchBtnClickFlag();	
	if(index == 0){
		$("#day_gubun").val("D");
		$("#submit_form #tab1ClickCount").val(1);
	}else if(index == 1){
		$("#day_gubun").val("W");
		$("#submit_form #tab2ClickCount").val(1);
	}else if(index == 2){
		$("#day_gubun").val("M");
		$("#submit_form #tab3ClickCount").val(1);
	}
//	createDayChart();
	fnSearch();
}

function fnSearch(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#gather_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 진단","데이터를 조회 중입니다."," "); 
	
	// 차트 데이터 조회
	searchSummaryChart();
	// 요약 데이터 조회
	searchSummary();
}

function searchSummaryChart(){	
	ajaxCall("/SQLDiagnostics/SummaryChart",
			$("#submit_form"),
			"POST",
			callback_SummaryChartAction);	
}

var callback_SummaryChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	var diagCntArray = [];
	var maxValue = 0;
	var maxCnt = 0;

	if($("#day_gubun").val() == "D"){
		for(var i = 0 ; i < data.rows.length ; i++){
			diagCntArray.push(parseInt(data.rows[i].diag_cnt));
		}
		
		maxValue = Math.max.apply(null,diagCntArray);		
		maxCnt = maxValue + (10 - maxValue%10);

		store = dayChart.down("chart").getStore();
		
		dayChart.down("chart").getAxes()[0].setMajorTickSteps(maxCnt);
		dayChart.down("chart").getAxes()[0].setMaximum(maxCnt);
	}else if($("#day_gubun").val() == "W"){
		store = weekChart.down("chart").getStore();
	}else {
		store = monthChart.down("chart").getStore();
	}
	
	store.loadData(data.rows);
};

function searchSummary(){	
	ajaxCall("/SQLDiagnostics/Summary",
			$("#submit_form"),
			"POST",
			callback_SummaryAction);	
}

//callback 함수
var callback_SummaryAction = function(result) {
	
	$("#summeryTbl tbody tr").remove();
	var strHtml = "";
	
	if(result.result){		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			var minDay = "";
			var maxDay = "";
			
			strHtml += "<tr>";
			if(result.txtValue == "D"){
				strHtml += "<td class='ctext'>"+formatReserDate(post.gather_day,'')+"</td>";	
			}else{
				if(post.gather_day == "SUM" || post.gather_day == "SUM/W"){
					strHtml += "<td class='ctext' style='background-color:#F3F2F2;'>"+post.gather_day+"</td>";
				}else{
					strHtml += "<td class='ctext'>"+formatReserDate(post.gather_day,'')+" [ "+post.day_name+" ]</td>";	
				}
			}		
			
			if(post.gather_day == "SUM"){
				minDay = formatReserDate(post.min_day,'');
			}else{
				minDay = formatReserDate(post.gather_day,'');
			}
			
			maxDay = formatReserDate(post.max_day,'');
			
			if(post.day_name != 'SUM' && post.day_name != 'SUM/W'){
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"1\",\"플랜변경\",\"/SQLDiagnostics/PlanChangeSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.plan_change_sql+"</td>";	
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"2\",\"신규\",\"/SQLDiagnostics/NewSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.new_sql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"3\",\"LITERAL(SQL_TEXT기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=1\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_sql_text+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"4\",\"LITERAL(PLAN_HASH_VALUE기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=2\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_plan_hash_value+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"5\",\"TEMP과다사용\",\"/SQLDiagnostics/TempOveruseSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.temp_usage_sql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"6\",\"FULLSCAN\",\"/SQLDiagnostics/FullScan\",\""+minDay+"\",\""+maxDay+"\");'>"+post.fullscan_sql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"7\",\"조건절없는DELETE\",\"/SQLDiagnostics/DeleteWithoutCondition\",\""+minDay+"\",\""+maxDay+"\");'>"+post.delete_sql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"8\",\"TOPSQL\",\"/SQLDiagnostics/TopSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.topsql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"9\",\"OFFLOAD 비효율 SQL\",\"/SQLDiagnostics/OffloadSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_sql+"</td>";
				strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createNewTab(\"10\",\"OFFLOAD 효율저하 SQL\",\"/SQLDiagnostics/OffloadEffcReduceSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_reduce_sql+"</td>";
				strHtml += "</tr>";
			}else if(post.day_name == 'SUM'){
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"1\",\"플랜변경\",\"/SQLDiagnostics/PlanChangeSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.plan_change_sql+"</td>";	
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"2\",\"신규\",\"/SQLDiagnostics/NewSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.new_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"3\",\"LITERAL(SQL_TEXT기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=1\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_sql_text+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"4\",\"LITERAL(PLAN_HASH_VALUE기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=2\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_plan_hash_value+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"5\",\"TEMP과다사용\",\"/SQLDiagnostics/TempOveruseSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.temp_usage_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"6\",\"FULLSCAN\",\"/SQLDiagnostics/FullScan\",\""+minDay+"\",\""+maxDay+"\");'>"+post.fullscan_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"7\",\"조건절없는DELETE\",\"/SQLDiagnostics/DeleteWithoutCondition\",\""+minDay+"\",\""+maxDay+"\");'>"+post.delete_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"8\",\"TOPSQL\",\"/SQLDiagnostics/TopSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.topsql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"9\",\"OFFLOAD 비효율 SQL\",\"/SQLDiagnostics/OffloadSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab(\"10\",\"OFFLOAD 효율저하 SQL\",\"/SQLDiagnostics/OffloadEffcReduceSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_reduce_sql+"</td>";
				strHtml += "</tr>";
			}else{
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"1\",\"플랜변경\",\"/SQLDiagnostics/PlanChangeSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.plan_change_sql+"</td>";	
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"2\",\"신규\",\"/SQLDiagnostics/NewSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.new_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"3\",\"LITERAL(SQL_TEXT기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=1\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_sql_text+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"4\",\"LITERAL(PLAN_HASH_VALUE기반)\",\"/SQLDiagnostics/LiteralSQL?literal_type_cd=2\",\""+minDay+"\",\""+maxDay+"\");'>"+post.literal_plan_hash_value+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"5\",\"TEMP과다사용\",\"/SQLDiagnostics/TempOveruseSQL\",\""+minDay+"\",\""+maxDay+"\");'>"+post.temp_usage_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"6\",\"FULLSCAN\",\"/SQLDiagnostics/FullScan\",\""+minDay+"\",\""+maxDay+"\");'>"+post.fullscan_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"7\",\"조건절없는DELETE\",\"/SQLDiagnostics/DeleteWithoutCondition\",\""+minDay+"\",\""+maxDay+"\");'>"+post.delete_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"8\",\"TOPSQL\",\"/SQLDiagnostics/TopSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.topsql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"9\",\"OFFLOAD 비효율 SQL\",\"/SQLDiagnostics/OffloadSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_sql+"</td>";
				strHtml += "<td class='rtext' style='background-color:#F3F2F2;cursor:pointer;' onClick='createNewTab2(\"10\",\"OFFLOAD 효율저하 SQL\",\"/SQLDiagnostics/OffloadEffcReduceSql\",\""+minDay+"\",\""+maxDay+"\");'>"+post.offload_effc_reduce_sql+"</td>";
				strHtml += "</tr>";
			}
		}
	}else{
		strHtml = "<tr><td colspan='"+$("#colspanCnt").val()+"' class='ctext'>검색된 데이터가 존재하지 않습니다.</td></tr>";
	}

	$("#summeryTbl tbody").append(strHtml);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

/* SQL 진단 상세 탭 생성 */
function createNewTab(gubun, menuNm, menuUrl, startDt, endDt){
	
	var dayGubun = $("#day_gubun").val();
	if(dayGubun == "D" || dayGubun == "W"){ endDt = startDt};
	
	var menuId = $("#menu_id").val() + gubun;
	var menuParam = "dbid="+$("#dbid").val()+"&strStartDt="+startDt+"&strEndDt="+endDt;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}


/* SQL 진단 상세 탭 생성  (WEEK 일 경우에만)*/
function createNewTab2(gubun, menuNm, menuUrl, startDt, endDt){
	
	//var dayGubun = $("#day_gubun").val();
	//if(dayGubun == "D" || dayGubun == "W"){ endDt = startDt};
	
	var menuId = $("#menu_id").val() + gubun;
	var menuParam = "dbid="+$("#dbid").val()+"&strStartDt="+startDt+"&strEndDt="+endDt;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}