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
	
	// Work Job 조회			
	$('#selectCombo').combobox({
		url:"/Common/getWrkJob",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onChange: function(newValue,oldValue) {
			fnInitSearchBtnClickFlag();
		},
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
				var wrkjob_cd = $("#wrkjob_cd").val();
				if(wrkjob_cd != '' && wrkjob_cd != 'null'){
					$(this).combobox('setValue', wrkjob_cd);
					
					var call_from_parent = $("#call_from_parent").val();
					if(call_from_parent == "Y"){
						$('#strStartDt').textbox('setValue',$("#gather_day").val());
						Btn_OnClick();
					}
				}
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		}
	});	
	
	$("#summeryTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			if(searchBtnClickCount == "" || searchBtnClickCount == "0"){
				if($('#selectCombo').combobox('getValue') == ""){
					parent.$.messager.alert('','업무명을 선택해 주세요.');
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
					fnSearch();
				}
			}else if(index == 1){
				$("#day_gubun").val("W");
				var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
				if(tab2ClickCount < searchBtnClickCount){
					createWeekChart();
					$("#submit_form #tab2ClickCount").val(1);
					fnSearch();
				}
			}else{
				$("#day_gubun").val("M");
				var tab3ClickCount = $("#submit_form #tab3ClickCount").val();
				if(tab3ClickCount < searchBtnClickCount){
					createMonthChart();
					$("#submit_form #tab3ClickCount").val(1);
					fnSearch();
				}
			}
			
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
      			  minGapWidth: 50
    			},
    			xField : 'tr_perf_indc_type_nm',
				yField : 'total_cnt',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("tr_perf_indc_type_nm")+" : " + record.get("total_cnt") + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            var menuUrl = "";
			            
			            if(item.record.get("tr_perf_indc_type_cd") == "1"){
			            	menuUrl = "/ApplicationDiagnostics/Timeout";				            
			            }else if(item.record.get("tr_perf_indc_type_cd") == "2"){
			            	menuUrl = "/ApplicationDiagnostics/ElapsedTimeDelay";				            
			            }
			            
			            createTab(item.record.get("tr_perf_indc_type_cd"),item.record.get("tr_perf_indc_type_nm"),menuUrl,item.record.get("min_day"),item.record.get("max_day"));
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
				xField : 'base_day',
				yField : fieldName,
				title : fieldRealName,
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
			            	menuUrl = "/ApplicationDiagnostics/Timeout";				            
			            }else if(fieldIndex == 1){
			            	gubun = "2";
			            	menuUrl = "/ApplicationDiagnostics/ElapsedTimeDelay";				            
			            }
			            
			            createTab(gubun,sector,menuUrl,item.record.get("min_day"),item.record.get("max_day"));
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
				xField : 'base_day',
				yField : fieldName,
				title : fieldRealName,
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
			            	menuUrl = "/ApplicationDiagnostics/Timeout";				            
			            }else if(fieldIndex == 1){
			            	gubun = "2";
			            	menuUrl = "/ApplicationDiagnostics/ElapsedTimeDelay";				            
			            }
			            
			            createTab(gubun,sector,menuUrl,item.record.get("min_day"),item.record.get("max_day"));
			        }
			    }
			}
		}]
	});
}		


function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
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
	
	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));
	$("#base_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("애플리케이션 진단"," "); 
	
	
	// 차트 데이터 조회
	searchSummaryChart();
	// 요약 데이터 조회
	searchSummary();
}

function searchSummaryChart(){	
	ajaxCall("/ApplicationDiagnostics/SummaryChart",
			$("#submit_form"),
			"POST",
			callback_SummaryChartAction);	
}

var callback_SummaryChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	var totalCntArray = [];
	var maxValue = 0;
	var maxCnt = 0;

	if($("#day_gubun").val() == "D"){
		for(var i = 0 ; i < data.rows.length ; i++){
			totalCntArray.push(parseInt(data.rows[i].total_cnt));
		}
		
		maxValue = Math.max.apply(null,totalCntArray);		
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
	ajaxCall("/ApplicationDiagnostics/Summary",
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
				strHtml += "<td class='ctext'>"+formatReserDate(post.base_day,'')+"</td>";	
			}else{
				if(post.base_day == "SUM"){
					strHtml += "<td class='ctext'>"+post.base_day+"</td>";
				}else{
					strHtml += "<td class='ctext'>"+formatReserDate(post.base_day,'')+" [ "+post.day_name+" ]</td>";	
				}
			}		
			
			if(post.base_day == "SUM"){
				minDay = formatReserDate(post.min_day,'');
			}else{
				minDay = formatReserDate(post.base_day,'');
			}
			
			maxDay = formatReserDate(post.max_day,'');
			
			strHtml += "<td class='rtext'>"+post.total_cnt+"</td>";	
			strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createTab(\"1\",\"타임아웃\",\"/ApplicationDiagnostics/Timeout\",\""+minDay+"\",\""+maxDay+"\");'>"+post.timeout_cnt+"</td>";
			strHtml += "<td class='rtext' style='cursor:pointer;' onClick='createTab(\"2\",\"응답시간지연\",\"/ApplicationDiagnostics/ElapsedTimeDelay\",\""+minDay+"\",\""+maxDay+"\");'>"+post.elapsed_time_delay_cnt+"</td>";
			strHtml += "</tr>";
		}
	}else{
		strHtml = "<tr><td colspan='4' class='ctext'>검색된 데이터가 존재하지 않습니다.</td></tr>";
	}

	$("#summeryTbl tbody").append(strHtml);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

/* SQL 진단 상세 탭 생성 */
function createTab(gubun, menuNm, menuUrl, startDt, endDt){
	
	var dayGubun = $("#day_gubun").val();
	if(dayGubun == "D" || dayGubun == "W"){ endDt = startDt};
	
	var menuId = $("#menu_id").val() + gubun;
	var menuParam = "wrkjob_cd="+$("#wrkjob_cd").val()+"&strStartDt="+startDt+"&strEndDt="+endDt;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}