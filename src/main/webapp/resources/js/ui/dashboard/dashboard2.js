var dbCheckResultChart;
var urgentActionTargetCondChart;
var perfImprWorkCondChart;
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
    var width3 = $("#perfImprWorkCondChart").width();
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
    if(perfImprWorkCondChart != "undefined" && perfImprWorkCondChart != undefined){
    	perfImprWorkCondChart.setSize(width3);
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
	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
		boxStroke:'#fff',
        label: {
        	textAlign: 'left'
        }
	});	
	
	OnSearch();
});

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
//		renderTo : document.getElementById("dbCheckResultChart"),
		layout : 'fit',
		items : [{
			xtype : 'polar',
            interactions: 'rotate',
            width: '99%',
            insetPadding: 10,
            innerPadding: 10,
            legend: dockedRightLegend,
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
				type : 'pie'
				,label:{
					field:'status'
				}
				,angleField:'cnt'
                //,xField: 'cnt'
                ,tooltip:{
                	trackMouse:true,
                	renderer:function(tooltip,record){
                		tooltip.setHtml(record.get("status")+"("+record.get("cnt")+")");
                	}
                }					
			}
		}]
	});	
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: 'DB 점검현황',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("dbCheckResultChart"),
        layout: 'fit',
        items: dbCheckResultChart
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
		//title : '긴급조치대상현황',
		titleAlign:'center',
		flex : 1,
		border : false,		
//		renderTo : document.getElementById("urgentActionTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
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
				type : 'bar',
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
			        	var menuId = $("#menu_id").val()+1;
			        	var menuNm = "긴급조치대상현황";
			        	var menuUrl = "/DashboardV2/urgentActionTarget";
			        	var menuParam = "check_pref_id="+item.record.get("check_pref_id");

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: '긴급조치대상현황',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("urgentActionTargetCondChart"),
        layout: 'fit',
        items: urgentActionTargetCondChart
    });    
    
}
//chart 3, 성능개선작업현황
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
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: '성능개선작업현황',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("perfImprWorkCondChart"),
        layout: 'fit',
        items: perfImprWorkCondChart
    });    

}
//chart 4, APP성능진단
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
				type : 'bar',
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
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: 'APP 성능진단',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("appPerfCheckChart"),
        layout: 'fit',
        items: appPerfCheckChart
    });    

}

//chart 5, SQL성능진단 
function createSqlPerfCheckChart(jsondata){
	if(sqlPerfCheckChart != "undefined" && sqlPerfCheckChart != undefined){
		sqlPerfCheckChart.destroy();
	}
	
	sqlPerfCheckChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		renderTo : document.getElementById("sqlPerfCheckChart"),
		layout : 'fit',
		//title : 'SQL 성능진단',
		titleAlign:'center',
		items : [{
			xtype : 'polar',
            interactions: 'rotate',
            width: '99%',
            insetPadding: 15,
            innerPadding: 15,
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
				type : 'pie3d',
				label:{
					field:'cd_nm'
				}
				,highlight:{
					margin:40
				}
				,angleField:'cnt'
                //,xField: 'cnt'
                ,donut: 40
                ,tooltip:{
                	trackMouse:true,
                	renderer:function(tooltip,record){
                		tooltip.setHtml(record.get("cd_nm")+"("+record.get("cnt")+")");
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
//            width: '99%',
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
//	                width: '99%',
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
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: 'SQL 성능진단',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("sqlPerfCheckChart"),
        layout: 'fit',
        items: sqlPerfCheckChart
    });    
	
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
//		renderTo : document.getElementById("reorgTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			legend : dockedRightLegend,
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
				yField : ['cnt','reclaimable_space'],
				title : ['cnt','reclaimable_space'],
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
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: 'Reorg 대상현황',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("reorgTargetCondChart"),
        layout: 'fit',
        items: reorgTargetCondChart
    });    
	
}

//chart 7, Object 변경현황 
function createObjectChangeCondChart(jsondata){
	if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
		objectChangeCondChart.destroy();
	}
	
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
	        }
	    },

//	    chart = Ext.create('Ext.chart.Chart', {
//	        style: 'background:#fff',
//	        theme: 'Category2',
//	        animate: true,
//	        store: store1,
//	        insetPadding: 20,
//	        legend: {
//	            position: 'right'
//	        },
//	        axes: [{
//	            type: 'Radial',
//	            position: 'radial',
//	            label: {
//	                display: true
//	            }
//	        }],
//	        series: [
//	            seriesConfig('data1'),
//	            seriesConfig('data2'),
//	            seriesConfig('data3')
//	        ]
//	    });
	    
	    
	    objectChangeCondChart = Ext.create('Ext.chart.PolarChart', {
		    width:'100%',
		    height:'100%',
			//title : 'Object 변경현황',
			titleAlign:'center',
//			renderTo : document.getElementById("objectChangeCondChart"),
	        style: 'background:#fff',
	        theme: 'Category2',
	        animate: true,
	        insetPadding: 20,
	        legend: dockedRightLegend,
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
		    
		});	    

	    var win = Ext.create('Ext.Window', {
	    	width: '99%',
	    	height: 240,
	    	minHeight: 240,
	    	minWidth: 300,
	    	hidden: false,
	    	shadow: false,
	    	maximizable: true,
	    	titleAlign: 'center',
	    	style: 'overflow: hidden;',
	    	title: 'Object 변경현황 ',
	    	constrain: true,
//        renderTo: Ext.getBody(),
	    	renderTo : document.getElementById("objectChangeCondChart"),
	    	layout: 'fit',
	    	items: objectChangeCondChart
	    });
	    
	});

}

//chart 8, 자원한계예측현황
function createResourceLimitPredictChart(jsondata){
	if(resourceLimitPredictChart != "undefined" && resourceLimitPredictChart != undefined){
		resourceLimitPredictChart.destroy();
	}

	resourceLimitPredictChart = Ext.create("Ext.panel.Panel",{
		width : '90%',
		height : '100%',
		//title : '자원한계예측현황',
		titleAlign:'center',
		flex : 1,
		border : false,		
//		renderTo : document.getElementById("resourceLimitPredictChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			legend : dockedRightLegend,
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
	
    var win = Ext.create('Ext.Window', {
        width: '99%',
        height: 240,
        minHeight: 240,
        minWidth: 300,
        hidden: false,
        shadow: false,
        maximizable: true,
        titleAlign: 'center',
        style: 'overflow: hidden;',
        title: '자원한계예측현황',
        constrain: true,
//        renderTo: Ext.getBody(),
		renderTo : document.getElementById("resourceLimitPredictChart"),
        layout: 'fit',
        items: resourceLimitPredictChart
    });    
	
}

function OnSearch(){
	/* DB점검 현황 차트 */
	ajaxCall("/DashboardV2/getDbCheckResult",
			$("#submit_form"),
			"POST",
			callback_DbCheckResultChartAction);
	
	/* 긴급조치대상 현황 차트 */
	ajaxCall("/DashboardV2/getUrgentActionCondition",
			$("#submit_form"),
			"POST",
			callback_UrgentActionTargetCondChartAction);
	
	/* 성능개선작업 현황 차트 */
	ajaxCall("/DashboardV2/getPerfImprWorkCondition",
			$("#submit_form"),
			"POST",
			callback_PerfImprWorkCondChartAction);
	
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
var callback_PerfImprWorkCondChartAction = function(result) {
//	chart_callback(result, perfImprWorkCondChart);
	var data = JSON.parse(result);
	createPerfImprWorkCondChart(data.rows);	
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