var dbCheckResultChart;
var urgentActionTargetCondChart;
//var perfImprWorkCondChart;
var incompleteTuningChart;
var appPerfCheckChart;
var sqlPerfCheckChart;
var reorgTargetCondChart;
var objectChangeCondChart;
var resourceLimitPredictChart;

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
    var width1 = $("#dbCheckResultChart").width();
    var width2 = $("#urgentActionTargetCondChart").width();
    //var width3 = $("#perfImprWorkCondChart").width();
    var width3 = $("#incompleteTuningChart").width();
    var width4 = $("#appPerfCheckChart").width();
    var width5 = $("#sqlPerfCheckChart").width();
    var width6 = $("#reorgTargetCondChart").width();
    var width7 = $("#objectChangeCondChart").width();
    var width8 = $("#resourceLimitPredictChart").width();

    if(dbCheckResultChart != "undefined" && dbCheckResultChart != undefined){
    	dbCheckResultChart.setSize(width1);		
	}
    if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
    	urgentActionTargetCondChart.setSize(width2);	
	}
//    if(perfImprWorkCondChart != "undefined" && perfImprWorkCondChart != undefined){
//    	perfImprWorkCondChart.setSize(width3);
//	}
    if(incompleteTuningChart != "undefined" && incompleteTuningChart != undefined){
    	incompleteTuningChart.setSize(width3);
	}
    if(appPerfCheckChart != "undefined" && appPerfCheckChart != undefined){
    	appPerfCheckChart.setSize(width4);
	}
    if(sqlPerfCheckChart != "undefined" && sqlPerfCheckChart != undefined){
    	sqlPerfCheckChart.setSize(width5);
	}
    if(reorgTargetCondChart != "undefined" && reorgTargetCondChart != undefined){
    	reorgTargetCondChart.setSize(width6);
	}
    if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
    	objectChangeCondChart.setSize(width7);
	}
    if(resourceLimitPredictChart != "undefined" && resourceLimitPredictChart != undefined){
    	resourceLimitPredictChart.setSize(width8);
	}
});	

var dockedRightLegend;

$(document).ready(function() {
//	createDbCheckResultChart();
//	createUrgentActionTargetCondChart();
//	createPerfImprWorkCondChart();	
//	createAppPerfCheckChart();
//	createSqlPerfCheckChart();
//	createReorgTargetCondChart();
//	createObjectChangeCondChart();
//	createResourceLimitPredictChart();

	function colspanFormatter(cssClass, cols) {
	    $('.' + cssClass).closest('tr').each(function() {
	        for (var c = cols - 1; c > 0; c--) {
	            $(this).children('td').eq(c).remove();
	        }
	        $(this).children('td').eq(0).attr('colspan', cols.toString());
	        $(this).children('td').eq(0).children(0).width('100%');
	        $(this).removeClass(cssClass);
	    });
	};
	
	//성능개선현황, 성능개선작업현황
	$("#perfImprCondition").datagrid({
		nowrap : true,
		onClickRow : function(index,row) {
			var menuParam = "tuning_status_cd=";
			createNewTab("110", "성능 개선 관리", "/ImprovementManagement", menuParam)
		},			
		columns:[[
			{field:'type',title:'구분',width:'30%',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'tot_sum',title:'전체누적',width:'16%',halign:"center",align:'center',sortable:"true"},
			{field:'this_year',title:'금년',width:'16%',halign:"center",align:'center',sortable:"true"},
			{field:'this_month',title:'금월',width:'16%',halign:"center",align:'center',sortable:"true"},
			{field:'note',title:'비고',width:'22%',halign:"center",align:'center',sortable:"true"}						
		]],
		onLoadSuccess: function(data){
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
	
	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left',
        	align:'left'
        }
	});	
	dockedLeftLegend = Ext.create("Ext.chart.Legend",{
		docked : 'left',
        label: {
        	textAlign: 'left',
           	align:'left'
        }
	});	
	
	OnSearch();
});


function cellStyler1(value,row,index){
	return 'background-color:#f2f2f2;white-space: nowrap !important';
}

//chart 1, DB점검현황
function createDbCheckResultChart(jsondata){
	if(dbCheckResultChart != "undefined" && dbCheckResultChart != undefined){
		dbCheckResultChart.destroy();
	}
	
	dbCheckResultChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : 'DB 점검현황',
		titleAlign:'center',
		renderTo : document.getElementById("dbCheckResultChart"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
	        captions: {
	            title: {
	                text: 'DB 점검현황('+maxCheckDay+')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
                }
	        },
            interactions: 'rotate',
            width: '99%',
            plugins: {
            	chartitemevents: {
            		moveEvents: true
            	}
            },
            //insetPadding: 35,
            insetPadding: 15,
            //innerPadding: 10,
            innerPadding: 50,
            //legend: dockedRightLegend,
			border:false,
			store : {
				fields:['status','cnt'],
				autoLoad:true,
				proxy:{
					type:'ajax',
					//url:'/DashboardV2/getSqlPerfCheck.json',
					//url:'data.json',
					reader:{
						type:'json',
						rootProperty:'data'
					}
				}
				,data : jsondata
			},
//            sprites: [],
			series : {
				type : 'pie3d'
	            ,donut: 50
	            ,thickness:5
                ,distortion:0.65
//                ,pieStyle:{
//                    bevelWidth: 15,
//                    colorSpread: 2
//                }
				,label:{
					field:'status',
				}
				,angleField:'cnt'
				//,radiusField:1
				
				//,xField: 'cnt'
				,highlight:{
					margin:5
				}
                ,tooltip:{
                	trackMouse:true,
                	renderer:function(tooltip,record){
                		tooltip.setHtml(record.get("status")+"("+record.get("cnt")+")");
                	}
                }
			    ,renderer: function(sprite, record, attr, index, store){
			    	var status = sprite.attr.label;
			    	var color="RED";

			    	if(status == "RED"){
			    		color='red';
			    	}else if(status == "YELLOW"){
			    		color='yellow';
			    	}else if(status == "GREEN"){
			    		color='green';
			    	}else if(status == "NONE"){
			    		color='gray';
			    	}
			    	
			        return Ext.apply(attr, {
			           fill: color,
			           //colors : color
			        });
			    	
			     }
                ,
				listeners: {
			        itemclick: function (chart, item, event) {
			            
			        	var menuId = "118";
			        	var menuNm = "일 예방 점검";
			        	var menuUrl = "/PreventiveCheck";
			        	var menuParam = "call_from_parent=Y&check_day="+maxCheckDayDash;
			        	console.log("menuParam:"+menuParam);

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});	
    
}
//chart 2, 긴급조치대상현황
function createUrgentActionTargetCondChart_bak(jsondata){
	if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
		urgentActionTargetCondChart.destroy();
	}
	
	urgentActionTargetCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : '긴급조치대상현황',
		titleAlign:'center',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("urgentActionTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                //text: '긴급조치대상현황('+ maxCheckDay +')',
	                text: '긴급조치대상현황',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			store : {
				data : jsondata
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
				title : ''
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
				type : 'bar3d',
				stacked : false,
				style: {
      			  minGapWidth: 25
    			},
				xField : 'check_pref_nm',
				yField : 'cnt',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.record.get('check_pref_nm') + " : " + item.record.get('cnt') + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "긴급조치대상현황";
			        	var menuUrl = "/DashboardV2/urgentActionTarget";
			        	var menuParam = "check_pref_id="+item.record.get("check_pref_id");

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
    
}

//chart 2, 긴급조치대상현황
function createUrgentActionTargetCondChart(jsondata){
	if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
		urgentActionTargetCondChart.destroy();
	}
	
	urgentActionTargetCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title:{color:"#000000",text:"긴급조치대상현황",textAlign:"left"},
		flex : 1,
		border : false,
		renderTo : document.getElementById("urgentActionTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '긴급조치대상현황('+ maxCheckDay +')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
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
				data : jsondata
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				majorTickSteps : 5,
				minorTickSteps: 0,
        		minimum: 0,
        		maximum: 5,
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
				label : {
					textAlign : 'left',
					x : 0,
					y : 0					
				}
			}],
			series : {
				type : 'bar3d',
				stacked : false, // stack 차트 해제
				style: {
	      			  minGapWidth: 5
    			},
//				xField : 'cnt',
//				yField : 'check_pref_nm',				
				xField : 'check_pref_nm',
				yField : 'cnt',				
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.record.get('check_pref_nm') + " : " + item.record.get('cnt') + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "긴급조치대상현황";
			        	var menuUrl = "/DashboardV2/urgentActionTarget";
			        	var menuParam = "check_pref_id="+item.record.get("check_pref_id")+"&check_day="+maxCheckDayDash;

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
}

//chart 2, 긴급조치대상현황
function createUrgentActionTargetCondChart___(jsondata){
	if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
		urgentActionTargetCondChart.destroy();
	}
	urgentActionTargetCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("urgentActionTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '긴급조치대상현황('+ maxCheckDay +')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
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
				data : jsondata
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
				type : 'bar3d',
				stacked : false, // stack 차트 해제
				style: {
      			  minGapWidth: 10
    			},
				xField : 'check_pref_nm',
				yField : 'cnt',
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(item.record.get('check_pref_nm') + " : " + item.record.get('cnt') + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            
			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "긴급조치대상현황";
			        	var menuUrl = "/DashboardV2/urgentActionTarget";
			        	var menuParam = "check_pref_id="+item.record.get("check_pref_id")+"&check_day="+maxCheckDayDash;

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
}

//chart 3, 성능개선작업현황
/*
function createPerfImprWorkCondChart(jsondata){
	if(perfImprWorkCondChart != "undefined" && perfImprWorkCondChart != undefined){
		perfImprWorkCondChart.destroy();
	}

	perfImprWorkCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : '성능개선작업현황',
		titleAlign:'center',
		flex : 1,
		border : false,		
		//renderTo : document.getElementById("perfImprWorkCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '성능개선작업현황',
	                align: 'left'
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			legend : dockedRightLegend,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : jsondata
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
				type : 'bar3d',
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
}
*/
//chart 3, 튜닝 미완료
function createIncompleteTuningChart(jsondata){
	if(incompleteTuningChart != "undefined" && incompleteTuningChart != undefined){
		incompleteTuningChart.destroy();
	}

	incompleteTuningChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : '튜닝 미완료',
		titleAlign:'center',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("incompleteTuningChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '튜닝 미완료('+todayTxt+')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			//legend : dockedRightLegend,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : jsondata
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
				type : 'bar3d',
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['cnt'],
				title : ['미완료'],
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
						//var menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd;
						var menuParam = "";
						createNewTab("110", "성능 개선 관리", "/ImprovementManagement", menuParam)
			        }
			    }
			}
		}]
	});	
}

//chart 4, APP 성능진단,APP성능진단
function createAppPerfCheckChart(jsondata){
	if(appPerfCheckChart != "undefined" && appPerfCheckChart != undefined){
		appPerfCheckChart.destroy();
	}
	
	appPerfCheckChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : 'APP 성능진단',
		titleAlign:'center',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("appPerfCheckChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: 'APP 성능진단('+maxBaseDay+')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			legend : dockedRightLegend,
		    innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : jsondata
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
				type : 'bar3d',
				style: {
      			  minGapWidth: 25,
      			  cursor:'hand'
    			},
				xField : 'wrkjob_cd_nm',
				yField : ['timeout_cnt','replytimeinc_cnt'],
				title : ['타임아웃','응답시간증가'],
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

			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "애플리케이션 진단현황";
			        	var menuUrl = "/DashboardV2/applicationCheckCombined";
			        	var menuParam = "";			        	
			            
			            var maxBaseDayDash = maxBaseDay.replace(/\./g,'-');
			            menuParam = "gather_day_dash="+maxBaseDayDash;
			            console.log("menuParam:"+menuParam);
			            
			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
}

//chart 5, SQL성능진단 , SQL 성능진단 
function createSqlPerfCheckChart(jsondata){
	if(sqlPerfCheckChart != "undefined" && sqlPerfCheckChart != undefined){
		sqlPerfCheckChart.destroy();
	}
	
	sqlPerfCheckChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		renderTo : document.getElementById("sqlPerfCheckChart"),
		layout : 'fit',
//		title : 'SQL 성능진단',
		titleAlign:'center',
		items : [{
			xtype : 'polar',
	        captions: {
	            title: {
	                text: 'SQL 성능진단('+ maxGatherDay +')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },	        
            width: '99%',
            //insetPadding: 30,
            insetPadding: 10,
            //innerPadding: 15,
            innerPadding: 35,
            legend: dockedRightLegend,
			border:false,
			store : {
				fields:['cd_nm','cnt'],
				autoLoad:true,
				proxy:{
					type:'ajax',
					//url:'/DashboardV2/getSqlPerfCheck.json',
					//url:'data.json',
					reader:{
						type:'json',
						rootProperty:'data'
					}
				}
				,data : jsondata
			},
            sprites: [],
			series : {
				type : 'pie3d'
				,label:{
					//position : 'left',
					field:'cd_nm',
					display: 'none',
				}
				,highlight:{
					margin:40
				}
				,angleField:'cnt'
                //,xField: 'cnt'
                ,donut: 20
                ,thickness:10
                ,distortion:0.65
                ,pieStyle:{
                    //bevelWidth: 15,
                    bevelWidth: 35,
                    colorSpread: 2
                }
                ,tooltip:{
                	trackMouse:true,
                	mouseOffset: [1,-10],
                	renderer:function(tooltip,record){
                		//tooltip.setHtml(record.get("cd_nm")+"("+record.get("cnt")+")");
                		tooltip.setTitle(record.get("cd_nm"));
                		tooltip.update("Count: "+record.get("cnt"));
                	}
                },
				listeners: {
			        itemclick: function (chart, item, event) {
			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			        	console.log("fieldIndex:"+fieldIndex);

			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "SQL 진단현황";
			        	var menuUrl = "/DashboardV2/sqlCheckCombined";		        	
			        	var menuParam = "";			        	
			            menuParam = "gather_day="+maxGatherDay+"&gather_day_dash="+maxGatherDayDash;
			            console.log("menuParam:"+menuParam);
			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});	
    
//	sqlPerfCheckChart = Ext.create("Ext.panel.Panel",{
//        renderTo: document.getElementById("sqlPerfCheckChart"),
//        width: 600,
//        height: 400,
//        layout: 'fit',
//        items: {
//            xtype: 'polar',
//            interactions: 'rotate',
//            width: '100%',
//            insetPadding: 50,
//            innerPadding: 20,
//            legend: {
//                docked: 'bottom'
//            },
//            store: {
//                fields: ['name', 'data1', 'data2', 'data3', 'data4', 'data5'],
//                autoLoad: true,
//                proxy: {
//                    type: 'ajax',
//                    url: 'data.json',
//                    reader: {
//                        type: 'json',
//                        rootProperty: 'data'
//                    }
//                }
//            },
//            sprites: [],
//            series: {
//                type: 'pie',
//                label: {
//                    field: 'name'
//                },
//                xField: 'data3',
//                donut: 30
//            },
//            listeners: {
//                boxready: function (chart) {
//                    chart.mon(chart.getStore(), 'load', function () {
//                        var surface = this.getSurface(),
//                            sprite = surface.add({
//                                type: 'text',
//                                text: 'Loaded!',
//                                fontSize: 22,
//                                height: 30,
//                                width: 100,
//                                x: 40,
//                                y: 20
//                            });
//                        sprite.show();
//                        this.redraw();
//                    }, chart, {
//                        delay: 1000
//                    });
//                }
//            }
//        }
//    });	
	
//	Ext.application({
//	    launch: function() {
//	        Ext.create('Ext.Container', {
//	        	renderTo: document.getElementById("sqlPerfCheckChart"),
//	            width: 600,
//	            height: 400,
//	            layout: 'fit',
//	            items: {
//	                xtype: 'polar',
//	                interactions: 'rotate',
//	                width: '100%',
//	                insetPadding: 50,
//	                innerPadding: 20,
//	                legend: {
//	                    docked: 'bottom'
//	                },
//	                store: {
//	                    fields: ['name', 'data1', 'data2', 'data3', 'data4', 'data5'],
//	                    autoLoad: true,
//	                    proxy: {
//	                        type: 'ajax',
//	                        url: '/DashboardV2/getSqlPerfCheck.json',
//	                        reader: {
//	                            type: 'json',
//	                            rootProperty: 'data'
//	                        }
//	                    }
//	                },
//	                sprites: [],
//	                series: {
//	                    type: 'pie',
//	                    label: {
//	                        field: 'name'
//	                    },
//	                    xField: 'data3',
//	                    donut: 30
//	                },
//	                listeners: {
//	                    boxready: function (chart) {
//	                        chart.mon(chart.getStore(), 'load', function () {
//	                            var surface = this.getSurface(),
//	                                sprite = surface.add({
//	                                    type: 'text',
//	                                    text: 'Loaded!',
//	                                    fontSize: 22,
//	                                    height: 30,
//	                                    width: 100,
//	                                    x: 40,
//	                                    y: 20
//	                                });
//	                            sprite.show();
//	                            this.redraw();
//	                        }, chart, {
//	                            delay: 1000
//	                        });
//	                    }
//	                }
//	            }
//	        });
//	    }
//	});	
	
}

//chart 6, Reorg 대상현황(TOP10 DB)
function createReorgTargetCondChart(jsondata){
	if(reorgTargetCondChart != "undefined" && reorgTargetCondChart != undefined){
		reorgTargetCondChart.destroy();
	}

	reorgTargetCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : 'Reorg 대상현황',
		titleAlign:'center',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("reorgTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: 'Reorg 대상현황('+maxGatherDay+')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
//			legend : dockedRightLegend,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : jsondata
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
				title : '(MB)'
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
				type : 'bar3d',
				style: {
      			  minGapWidth: 25
    			},
				xField : 'db_name',
				yField : ['reclaimable_space'],
				title : ['reclaimable_space'],
				stacked:false,
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			            sector = item.series.getTitle()[fieldIndex];
			            value = Ext.util.Format.number(record.get(item.field));
			            if(sector == "reclaimable_space"){
			            	tooltip.setHtml(sector+" : " + value + " MB");
			            }else{
			            	tooltip.setHtml(sector+" : " + value + " 건");
			            }
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			        	var object_change_type = item.field;

			        	var menuId = "174";
			        	var menuNm = "REORG 대상 진단";
			        	var menuUrl = "/ReorgTargetTable";
			        	
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            menuParam = "dbid="+item.record.get("dbid")+"&call_from_parent=Y";

			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
	
}

//chart 7, Object 변경현황 
function createObjectChangeCondChart(jsondata){
	if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
		objectChangeCondChart.destroy();
	}
	
	legend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left'
        }
	});
	
	Ext.onReady(function () {
	    var seriesConfig = function(field,legend) {
	        return {
	            type: 'radar',
	            xField: 'type',
	            yField: field,
	            title:legend,
	            showInLegend: true,
	            showMarkers: true,
	            markerConfig: {
	                radius: 5,
	                size: 5
	            },
			    plugins: {
			    	chartitemevents: {
			    		moveEvents: true
			    	}
			    },	            
	            tips: {
	                trackMouse: true,
	                renderer: function(storeItem, item) {
	                	this.setTitle(storeItem.get('name') + ': ' + storeItem.get(field));
	                }
	            },
	            style: {
	                'stroke-width': 2,
	                fill: 'none'
	            }
				,tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            sector = item.series.getTitle()[fieldIndex],
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(sector+" : " + value + " 건");
					}
				}
			    ,listeners: { // Listen to itemclick events on all series.
			        itemclick: function (chart, item, event) {
			            console.log('itemclick', item.category, item.field);
			        }
			    }
	        }
	    },

	    objectChangeCondChart = Ext.create('Ext.chart.PolarChart', {
	    	
	    	width:'100%',
		    height:'100%',
//		    plugins: {
//		    	chartitemevents: {
//		    		moveEvents: true
//		    	}
//		    },		    
			//title : 'Object 변경현황',
			titleAlign:'center',
			renderTo : document.getElementById("objectChangeCondChart"),
	        style: 'background:#fff',
	        theme: 'Category2',
	        animate: true,
	        insetPadding: 20,
	        legend: legend,
	        border:false,
	        store: {
		        fields: ['type','yesterday','recent_one_week','recent_one_month'],
				data : jsondata
		    },
		    // Set rotation
		    interactions: ['rotate'],
		    // Define radial and angular axis for the radar chart.
		    axes: [
		        {
		            type: 'Radial',
		            type: 'numeric',
		            position: 'radial',
		            fields: ['yesterday','recent_one_week','recent_one_month'],
		            grid: true,
		            label: {
		                fill: 'black',
		                y: -8,
		                display:true
		            }
		        }, {
		            type: 'category',
		            position: 'angular',
		            fields: 'type',
		            grid: true,
		            label: {
		                fill: 'black',
		                display:true
		            }
		        }
		    ],
		    series: [
	            seriesConfig('yesterday','전일'),
	            seriesConfig('recent_one_week','최근1주일'),
	            seriesConfig('recent_one_month','최근1개월')
		    ]
		    ,
	        captions: {
	            title: {
	                text: 'Object 변경현황',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        }
		});
	});
}

function createObjectChangeCondChart1(jsondata){
	if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
		objectChangeCondChart.destroy();
	}
	
	legend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
        label: {
        	textAlign: 'left'
        }
	});
	
	/**
	 * Multi-series radar chart displaying trends across multiple categories.
	 * Markers are placed at each point to clearly depict their position.
	 *
	 * The example makes use of the 'rotate' interaction. To use it, click or tap and then
	 * drag anywhere on the chart.
	 */
	objectChangeCondChart = Ext.define('KitchenSink.view.charts.radar.Marked', {
	    extend: 'Ext.Panel',
	    xtype: 'radar-marked',
	    controller: 'radar-basic',

	    width: 650,

	    tbar: [
	        '->',
	        {
	            text: 'Preview',
	            handler: 'onPreview'
	        }
	    ],
	    
		renderTo : document.getElementById("objectChangeCondChart"),

	    items: [{
	        xtype: 'polar',
	        reference: 'chart',
	        width: '100%',
	        height: 500,
	        legend: {
	            docked: 'right'
	        },
	        animation: {
	            duration: 200
	        },
	        store: {
	            type: 'browsers'
	        },
	        insetPadding: 20,
	        interactions: ['rotate'],
	        captions: {
	            title: 'Radar Charts - Marked',
	            credits: {
	                text: 'Data: Browser Stats 2012 - Internet Explorer\n' +
	                    'Source: http://www.w3schools.com/',
	                align: 'left'
	            }
	        },
	        axes: [{
	            type: 'numeric',
	            position: 'radial',
	            minimum: 0,
	            maximum: 50,
	            majorTickSteps: 9
	        }, {
	            type: 'category',
	            position: 'angular',
	            grid: true
	        }],
	        series: [{
	            type: 'radar',
	            title: 'IE',
	            angleField: 'month',
	            radiusField: 'data1',
	            style: {
	                lineWidth: 2,
	                fillStyle: 'none'
	            },
	            marker: true,
	            highlightCfg: {
	                radius: 6,
	                fillStyle: 'yellow'
	            },
	            tooltip: {
	                trackMouse: true,
	                renderer: 'onSeriesLabelRender'
	            }
	        }, {
	            type: 'radar',
	            title: 'Firefox',
	            angleField: 'month',
	            radiusField: 'data2',
	            style: {
	                lineWidth: 2,
	                fillStyle: 'none'
	            },
	            marker: true,
	            highlightCfg: {
	                radius: 6,
	                fillStyle: 'yellow'
	            },
	            tooltip: {
	                trackMouse: true,
	                renderer: 'onSeriesLabelRender'
	            }
	        }, {
	            type: 'radar',
	            title: 'Chrome',
	            angleField: 'month',
	            radiusField: 'data3',
	            style: {
	                lineWidth: 2,
	                fillStyle: 'none'
	            },
	            marker: true,
	            highlightCfg: {
	                radius: 6,
	                fillStyle: 'yellow'
	            },
	            tooltip: {
	                trackMouse: true,
	                renderer: 'onSeriesLabelRender'
	            }
	        }, {
	            type: 'radar',
	            title: 'Safari',
	            angleField: 'month',
	            radiusField: 'data4',
	            style: {
	                lineWidth: 2,
	                fillStyle: 'none'
	            }
	        }]
	    }]

	});
	
	Ext.define('KitchenSink.view.charts.radar.BasicController', {
	    extend: 'Ext.app.ViewController',
	    alias: 'controller.radar-basic',

	    onPreview: function () {
	        if (Ext.isIE8) {
	            Ext.Msg.alert('Unsupported Operation', 'This operation requires a newer version of Internet Explorer.');
	            return;
	        }
	        var chart = this.lookup('objectChangeCondChart');
	        chart.preview();
	    },

	    onRefresh: function () {
	        var chart = this.lookup('objectChangeCondChart');
	        chart.getStore().refreshData();
	    },

	    onDataRender: function (v) {
	        return v + '%';
	    },

	    onAxisLabelRender: function (axis, label, layoutContext) {
	        // Custom renderer overrides the native axis label renderer.
	        // Since we don't want to do anything fancy with the value
	        // ourselves except appending a '%' sign, but at the same time
	        // don't want to loose the formatting done by the native renderer,
	        // we let the native renderer process the value first.
	        return layoutContext.renderer(label) + '%';
	    },

	    onMultiAxisLabelRender: function (axis, label, layoutContext) {
	        return label === 'Jan' ? '' : label;
	    },

	    onSeriesLabelRender: function (tooltip, record, item) {
	        tooltip.setHtml(record.get('month') + ': ' + record.get(item.field) + '%');
	    }

	});
	
	Ext.define('KitchenSink.store.Browsers', {
	    extend: 'Ext.data.Store',
	    alias: 'store.browsers',

	    //                   IE    Firefox  Chrome   Safari
	    fields: ['month', 'data1', 'data2', 'data3', 'data4', 'other'],

	    constructor: function (config) {
	        config = config || {};

	        config.data = [
	            { month: 'Jan', data1: 20, data2: 37, data3: 35, data4: 4, other: 4 },
	            { month: 'Feb', data1: 20, data2: 37, data3: 36, data4: 5, other: 2 },
	            { month: 'Mar', data1: 19, data2: 36, data3: 37, data4: 4, other: 4 },
	            { month: 'Apr', data1: 18, data2: 36, data3: 38, data4: 5, other: 3 },
	            { month: 'May', data1: 18, data2: 35, data3: 39, data4: 4, other: 4 },
	            { month: 'Jun', data1: 17, data2: 34, data3: 42, data4: 4, other: 3 },
	            { month: 'Jul', data1: 16, data2: 34, data3: 43, data4: 4, other: 3 },
	            { month: 'Aug', data1: 16, data2: 33, data3: 44, data4: 4, other: 3 },
	            { month: 'Sep', data1: 16, data2: 32, data3: 44, data4: 4, other: 4 },
	            { month: 'Oct', data1: 16, data2: 32, data3: 45, data4: 4, other: 3 },
	            { month: 'Nov', data1: 15, data2: 31, data3: 46, data4: 4, other: 4 },
	            { month: 'Dec', data1: 15, data2: 31, data3: 47, data4: 4, other: 3 }
	        ];

	        this.callParent([config]);
	    }

	});	
}

//chart 8, 자원한계예측현황
function createResourceLimitPredictChart(jsondata){
	if(resourceLimitPredictChart != "undefined" && resourceLimitPredictChart != undefined){
		resourceLimitPredictChart.destroy();
	}

	resourceLimitPredictChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title : '자원한계예측현황',
		//titleAlign:'center',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("resourceLimitPredictChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '자원한계예측현황',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
				chartitemevents: {
					moveEvents: true
				}
			},
			legend : dockedRightLegend,
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : jsondata
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
				type : 'bar3d',
				style: {
      			  minGapWidth: 25
    			},
				xField : ['after_month'],
				yField : ['cpu','sequence','tablespace'],
				title : ['CPU','Sequence','TableSpace'],
				stacked:false,
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
			        	var menuNm = "자원한계예측현황";
			        	//var menuUrl = "/DashboardV2/resourceLimitPredictCondition";
			        	var menuUrl = "/extjs/DashboardV2/resourceLimitPredictCondition";
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            menuParam = "dbid="+item.record.get("dbid")+"&base_day_gubun=2&object_change_type="+object_change_type;

			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
	
}

function OnSearch(){
	$('#perfImprCondition').datagrid("loadData", []);

	/* DB점검 현황 차트 */
	ajaxCall("/DashboardV2/getDbCheckResult", $("#submit_form"), "POST", callback_DbCheckResultChartAction);
	
	/* 긴급조치대상 현황 차트 */
	ajaxCall("/DashboardV2/getUrgentActionCondition",
			$("#submit_form"),
			"POST",
			callback_UrgentActionTargetCondChartAction);
	
	/* 성능개선작업 현황 차트 */
//	ajaxCall("/DashboardV2/getPerfImprWorkCondition",
//			$("#submit_form"),
//			"POST",
//			callback_PerfImprWorkCondChartAction);
	
	/* 튜닝 미완료 차트 */
	ajaxCall("/DashboardV2/getIncompleteTuningList",
			$("#submit_form"),
			"POST",
			callback_IncompleteTuningChartAction);
	
	/* App성능진단 리스트 */
	ajaxCall("/DashboardV2/getAppPerfCheck",
			$("#submit_form"),
			"POST",
			callback_AppPerfCheckChartAction);	
	
	/* SQL성능진단 차트 */
	ajaxCall("/DashboardV2/getSqlPerfCheck",
			$("#submit_form"),
			"POST",
			callback_SqlPerfCheckChartAction);
	
	/* Reorg대상 현황 차트 */
	ajaxCall("/DashboardV2/getReorgTargetCond",
			$("#submit_form"),
			"POST",
			callback_ReorgTargetCondChartAction);
	
	/* 오브젝트 변경 현황 차트 */
	ajaxCall("/DashboardV2/getObjectChangeCondition",
			$("#submit_form"),
			"POST",
			callback_ObjectChangeCondChartAction);
	
	/* 자원한계예측 현황 차트 */
	ajaxCall("/DashboardV2/getResourceLimitPredict",
			$("#submit_form"),
			"POST",
			callback_ResourceLimitPredictChartAction);
	
	ajaxCallPerfImprCondition();			

}

//callback 함수
var callback_DbCheckResultChartAction = function(result) {
	var data = JSON.parse(result);
	createDbCheckResultChart(data.rows);
	//chart_callback(result, dbCheckResultChart);
};
//callback 함수
var callback_UrgentActionTargetCondChartAction = function(result) {
//	chart_callback(result, urgentActionTargetCondChart);
	var data = JSON.parse(result);
	createUrgentActionTargetCondChart(data.rows);	
};
//callback 함수
//var callback_PerfImprWorkCondChartAction = function(result) {
////	chart_callback(result, perfImprWorkCondChart);
//	var data = JSON.parse(result);
//	createPerfImprWorkCondChart(data.rows);	
//};
//callback 함수
var callback_IncompleteTuningChartAction = function(result) {
//	chart_callback(result, perfImprWorkCondChart);
	var data = JSON.parse(result);
	createIncompleteTuningChart(data.rows);	
};
//callback 함수
var callback_AppPerfCheckChartAction = function(result) {
//	chart_callback(result, appPerfCheckChart);
	var data = JSON.parse(result);
	createAppPerfCheckChart(data.rows);	
};
//callback 함수
var callback_SqlPerfCheckChartAction = function(result) {
	var data = JSON.parse(result);
	createSqlPerfCheckChart(data.rows);
	//chart_callback(result, sqlPerfCheckChart);
};
//callback 함수
var callback_ReorgTargetCondChartAction = function(result) {
//	chart_callback(result, reorgTargetCondChart);
	var data = JSON.parse(result);
	createReorgTargetCondChart(data.rows);	
};
//callback 함수
var callback_ObjectChangeCondChartAction = function(result) {
//	chart_callback(result, objectChangeCondChart);
	var data = JSON.parse(result);
	createObjectChangeCondChart(data.rows);	
};
//callback 함수
var callback_ResourceLimitPredictChartAction = function(result) {
//	chart_callback(result, resourceLimitPredictChart);
	var data = JSON.parse(result);
	createResourceLimitPredictChart(data.rows);	
};


function ajaxCallPerfImprCondition(){
	/* 튜닝 지연 현황 리스트 */
	ajaxCall("/DashboardV2/getPerfImprCondition",
			$("#submit_form"),
			"POST",
			callback_PerfImprConditionAction);	
}

/* 튜닝 지연 현황 리스트 */
//callback 함수
var callback_PerfImprConditionAction = function(result) {
	var data = JSON.parse(result);
	$('#perfImprCondition').datagrid("loadData", data);
	$('#perfImprCondition').datagrid('loaded');	

    $('.datagrid-htable').first().prepend('<caption><span>성능개선현황</span></caption>');
	//$( ".tabs-panels:eq(0) .panel-htop:eq( 3 ) td" ).css( "cursor", "default" );
};

/* 조치여부 업데이트 */
function actionBtn(val, row) {
	if(row.emergency_action_yn == "N"){
		return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_UpdateUrgentAction(\""+row.dbid+"\",\""+row.emergency_action_no+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 조치완료</a>";	
	}else{
		return "";
	}    
}

function Btn_UpdateUrgentAction(dbid, emergencyActionNo){
	$("#emergency_action_no").val(emergencyActionNo);
	$("#dbid").val(dbid);
	
	ajaxCall("/DashboardV2/UpdateUrgentAction",
			$("#submit_form"),
			"POST",
			callback_UpdateUrgentActionAction);		
}

//callback 함수
var callback_UpdateUrgentActionAction = function(result) {
	if(result.result){
		parent.$.messager.alert({
			msg : '긴급 조치 처리가 완료되었습니다.',
			fn :function(){
				Btn_UrgentActionSearch();
			} 
		});
	}else{
		parent.$.messager.alert('error','긴급 조치 처리가 실패하였습니다.');
	}
};

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