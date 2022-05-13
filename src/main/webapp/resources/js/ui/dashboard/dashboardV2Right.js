var resourceLimitPointPredictionChart;
var tablespaceSequencePresentConditionChart;
var newSQLAppTimeoutPredictChart;
var dockedRightLegend;
var cpu_memory_gb="CPU";

Ext.EventManager.onWindowResize(function () {
    var width = $("#resourceLimitPointPredictionChart").width();

    if(resourceLimitPointPredictionChart != "undefined" && resourceLimitPointPredictionChart != undefined){
    	resourceLimitPointPredictionChart.setSize(width);		
	}
    if(tablespaceSequencePresentConditionChart != "undefined" && tablespaceSequencePresentConditionChart != undefined){
    	tablespaceSequencePresentConditionChart.setSize(width);		
    }
    if(newSQLAppTimeoutPredictChart != "undefined" && newSQLAppTimeoutPredictChart != undefined){
    	newSQLAppTimeoutPredictChart.setSize(width);		
    }
    
});

Ext.require([
	'Ext.data.*',
    'Ext.chart.*',
    'Ext.Window',
    'Ext.fx.target.Sprite',
    'Ext.layout.container.Fit',
    'Ext.window.MessageBox'
]);

Ext.onReady(function() {
	createResourceLimitPointPredictionChart(); //SGA-PGA
	createTablespacePresentConditionChart();		
	createNewSqlTimeoutPredictionChart();

	/*Grid1 자원 한계점 예측(3월내 한계도래) */
	ajaxCallResourceLimitPointPrediction();
	
	/*Grid2 Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp */
	/* default : tablespace */
	$("submit_form #strGubun").val("tablespace");
	ajaxCallTablespacePresentConditionDBList();
	
	/*Grid3 신규SQL/App Timeout 예측*/
	/*default : newsql */
	$("submit_form #strGubun").val("newsql");
	ajaxCallNewSQLTimeoutPrediction();
});

$(document).ready(function() {
//    $('input[name="cpuMemory"]').radiobutton({
//        onChange: function(checked){
//        	if(checked){
//        		cpu_memory_gb = $("input[name='cpuMemory']:checked").val();
//        		ajaxCallResourceLimitPointPredictionChart(cpu_memory_gb);
//        	}
//        }
//    });
    $('#cpuMemory1').radiobutton({
        onChange: function(checked){
        	if(checked){
        		cpu_memory_gb = $("input[name='cpuMemory']:checked").val();
        		ajaxCallResourceLimitPointPredictionChart(cpu_memory_gb);
        	}
        }
    });    
    $('#cpuMemory2').radiobutton({
        onChange: function(checked){
        	if(checked){
        		cpu_memory_gb = $("input[name='cpuMemory']:checked").val();
        		ajaxCallResourceLimitPointPredictionChart(cpu_memory_gb);
        	}
        }
    });    
	$("#tbsSeqTab").tabs({
		plain: true,
		onSelect:function(title, index){
//			var tab = $("#tbsSeqTab").tabs("getSelected");
//			var idx = tab.panel("options").index;
			if(index == 0){
				//Tablespace
				ajaxCallTablespacePresentConditionDBList();
			}else if(index == 1){
				//Sequence
				ajaxCallSequencePresentConditionDBList();
			}
		}
	});
	
	$("#sqlAppTab").tabs({
		plain: true,
		onSelect:function(title, index){
//			var tab = $("#tbsSeqTab").tabs("getSelected");
//			var idx = tab.panel("options").index;
			if(index == 0){
				//SQL
				$("submit_form #strGubun").val("newsql");
				ajaxCallNewSQLTimeoutPrediction();
			}else if(index == 1){
				//App
				$("submit_form #strGubun").val("app");
				ajaxCallNewAppTimeoutPrediction();
			}

		}
	});
	
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
	$("#resourceLimitPointPrediction").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			
			$("#dbid").val(row.dbid);
			$("#inst_id").val(row.inst_id);

			var menuParam = "dbid="+row.dbid+"&inst_id="+row.inst_id+"&call_from_parent=Y";
			if(field=='cpu_core_cnt'||field=='current_cpu_usage'||field=='cpu_increase_ratio'){
				//장애예방 > 장애예측분석 > CPU한계점예측
				createNewTab("186", "CPU 한계점 예측", "/CPULimitPointPrediction", menuParam);
			}else if(field=='physical_memory_size'||field=='current_mem_usage'||field=='mem_increase_usage'){
				//장애예방 > 장애예측분석 > MEMORY한계점예측
				createNewTab("186", "MEMORY 한계점 예측", "/MEMORYLimitPointPrediction", menuParam);
			}
		},
		columns:[[
			{field:'inst_nm',title:'Instance',width:'126px',halign:"center",align:'center',rowspan:2,formatter:formatStrAndColor},
			{title:'CPU(%)',width:'36%',halign:"center",align:'center',colspan:3},
			{title:'SGA+PGA(GB)',width:'36%',halign:"center",align:'center',colspan:3},
			{field:'rgb_color_value',title:'rgb_color_value',hidden:true, rowspan:2},
			{field:'dbid',title:'DB',hidden:true, rowspan:2},
			{field:'inst_id',title:'DB',hidden:true, rowspan:2}
		]
		,[
			{field:'cpu_core_cnt',title:'Core',width:'12%',halign:"center",align:'right'},
			{field:'current_cpu_usage',title:'현재',width:'12%',halign:"center",align:'right'},
			{field:'cpu_increase_ratio',title:'증가',width:'12%',halign:"center",align:'right'},
			{field:'physical_memory_size',title:'Physical',width:'12%',halign:"center",align:'right'},
			{field:'current_mem_usage',title:'현재',width:'12%',halign:"center",align:'right'},
			{field:'mem_increase_usage',title:'증가',width:'12%',halign:"center",align:'right'}
		]],
		onLoadSuccess: function(data){
			$('#resourceLimitPointPrediction').datagrid('resize');
			if(data.resultCode == "00000" && data.rows.length > 0){
				/* 자원 한계점 예측(3월내 한계도래) 차트 */
				cpu_memory_gb = "CPU";
				ajaxCallResourceLimitPointPredictionChart("CPU");			
			}
	    },
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	$("#resourceLimitPointPrediction").parent().find(".datagrid-body td" ).css( "cursor", "default" );

	//Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp
	$("#tablespacePresentConditionDBList").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
			ajaxCallTablespacePresentCondition();
		},
		columns:[[
			{field:'db_name',title:'DB',width:'70%',halign:"center",align:'center'},
			{field:'db_id',title:'DB',hidden:true},
			{field:'cnt',title:'대상',width:'48%',halign:"center",align:'center'}
		]],
		onSelect : function(index, row) {
			$("#submit_form #dbid").val(row.dbid);
			ajaxCallTablespacePresentCondition();
		},
		onLoadSuccess: function(data){
			if(data == null || data.rows.length == 0) {
				return;
			}
			
			$(this).datagrid("selectRow", 0);
	    },
    	onLoadError:function() {
    		alert("onLoadError");
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	//Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp
	$("#sequencePresentConditionDBList").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
			ajaxCallSequencePresentCondition();
		},
		columns:[[
			{field:'db_name',title:'DB',width:'70%',halign:"center",align:'center'},
			{field:'db_id',title:'DB',hidden:true},
			{field:'cnt',title:'대상',width:'48%',halign:"center",align:'right'}
		]],
		onLoadSuccess: function(data){
            var datas = $(this).datagrid('getData');
            var rows = $(this).datagrid('getRows');
            var row;
            if(rows.length > 0){
            	row = rows[0];
            	$("#submit_form #dbid").val(row.dbid);
            }
			ajaxCallSequencePresentCondition();
	    },
    	onLoadError:function() {
    		alert("onLoadError");
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	//Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp
	$("#tablespacePresentCondition").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			
			$("#dbid").val(row.dbid);
			$("#tablespace_name").val(row.tablespace_name);


			var menuParam = "dbid="+row.dbid+"&tablespace_name="+row.tablespace_name+"&predict_standard=3&nav_from_parent=Y";
			if(field=='tablespace_name'){
				//1. 장애예방 > 장애예측분석 > Tablespace 한계점 예측 상세 로 이동
				//2. 파라미터 - DBID, 예측기준(3개월후한계점도달)
				createNewTab("1881", "Tablespace 한계점 예측 - 상세", "/TablespaceLimitPointPredictionDetail", menuParam)
			}
		},		
		columns:[[
			{field:'tablespace_name',title:'Tablespace',width:'57%',halign:"center",align:'center'},
			{field:'tablespace_size',title:'할당량(GB)',width:'27%',halign:"center",align:'right'},
			{field:'current_ts_used_space',title:'사용량(GB)',width:'27%',halign:"center",align:'right'},
			{field:'current_ts_used_percent',title:'사용률',width:'25%',halign:"center",align:'right'}
			]],
			onLoadSuccess: function(data){
				ajaxCallTablespacePresentConditionChart();
			},
			onLoadError:function() {
				$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});

	//Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp
	$("#sequencePresentCondition").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			
			$("#dbid").val(row.dbid);
			$("#sequence_name").val(row.sequence_name);
			
			
			var menuParam = "dbid="+row.dbid+"&sequence_name="+row.sequence_name+"&predict_standard=3&nav_from_parent=Y";
			if(field=='sequence_name'){
				//1. 장애예방 > 장애예측분석 > SEQUENCE 한계점 예측 상세 로 이동
				//2. 파라미터 - DBID, 예측기준(3개월후한계점도달)
				createNewTab("1871", "Sequence 한계점 예측 - 상세", "/SequenceLimitPointPredictionDetail", menuParam)
			}
		},		
		columns:[[
			{field:'sequence_name',title:'Sequence',width:'57%',halign:"center",align:'center'},
			{field:'current_sequence_value',title:'Cur Val',width:'25%',halign:"center",align:'right'},
			{field:'sequence_max_value',title:'Max Val',width:'25%',halign:"center",align:'right'},
			{field:'current_sequence_ratio',title:'사용률',width:'25%',halign:"center",align:'right'}
			]],
			onLoadSuccess: function(data){
				ajaxCallSequencePresentConditionChart();
			},
			onLoadError:function() {
				$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});	
	
	//신규SQL/App Timeout 예측
	$("#newSQLTimeoutPrediction").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			
//			if(field=='db_name'){
				
//				1. 장애예방 > 장애예측분석 > 신규SQL타임아웃예측 화면으로 이동
//				2. 파라미터
//				- DBID
//				- 발생일 조건: 최근3개월
//				- 타임아웃조건 : 1주일후
//				- 예외 조건 : 미예외
				var timeout_condition = "";
				if(field == "after_1_week_elapsed_time") timeout_condition = 1;
				else if(field == "after_1_month_elapsed_time") timeout_condition = 4;
				else if(field == "after_2_month_elapsed_time") timeout_condition = 5;
				else if(field == "after_3_month_elapsed_time") timeout_condition = 6;
				else timeout_condition = 6;
				var menuParam = "dbid="+row.dbid+"&predict_standard=3&timeout_condition="+timeout_condition+"&except_yn=N&call_from_parent=Y";
				createNewTab("225", "신규 SQL 타임아웃 예측", "/NewSQLTimeoutPrediction", menuParam)
//			}
		},		
		columns:[[
			{field:'db_name',title:'DB',width:'120px',halign:"center",align:'center',formatter:formatStrAndColor},
			{field:'total_cnt',title:'대상',width:'14%',halign:"center",align:'right'},
			{field:'after_1_week_elapsed_time',title:'1주후',width:'14%',halign:"center",align:'right'},
			{field:'after_1_month_elapsed_time',title:'1개월후',width:'14%',halign:"center",align:'right'},
			{field:'after_2_month_elapsed_time',title:'2개월후',width:'15%',halign:"center",align:'right'},
			{field:'after_3_month_elapsed_time',title:'3개월후',width:'15%',halign:"center",align:'right'},
			{field:'dbid',title:'DB',hidden:true},
			{field:'rgb_color_value',title:'rgb_color_value',hidden:true}
		]],
		onLoadSuccess: function(data){
//			ajaxCallNewSQLTimeoutPredictionChart();			
	    },
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#newAppTimeoutPrediction").datagrid({
		view : naview,
		nowrap : true,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			
//			if(field=='wrkjob_cd_nm'){
//				1. 장애예방 > 장애예측분석 > 신규SQL타임아웃예측 화면으로 이동
//				2. 파라미터
//				- WRKJOB_CD
//				- 발생일 조건: 최근3개월
//				- 타임아웃조건 : 1주일후
//				- 예외 조건 : 미예외
				var timeout_condition = "";
				if(field == "after_1_week_elapsed_time") timeout_condition = 1;
				else if(field == "after_1_month_elapsed_time") timeout_condition = 4;
				else if(field == "after_2_month_elapsed_time") timeout_condition = 5;
				else if(field == "after_3_month_elapsed_time") timeout_condition = 6;
				else timeout_condition = 6;
				
				var menuParam = "&wrkjob_cd="+row.wrkjob_cd+"&predict_standard=3&timeout_condition="+timeout_condition+"&except_yn=N&call_from_parent=Y";
				createNewTab("224", "신규 APP 타임아웃 예측", "/NewAppTimeoutPrediction", menuParam)
//			}
		},		
		columns:[[
			
			{field:'wrkjob_cd_nm',title:'업무',width:'120px',halign:"center",align:'center',formatter:formatStrAndColor},
			{field:'total_cnt',title:'대상',width:'14%',halign:"center",align:'right'},
			{field:'after_1_week_elapsed_time',title:'1주후',width:'14%',halign:"center",align:'right'},
			{field:'after_1_month_elapsed_time',title:'1개월후',width:'14%',halign:"center",align:'right'},
			{field:'after_2_month_elapsed_time',title:'2개월후',width:'15%',halign:"center",align:'right'},
			{field:'after_3_month_elapsed_time',title:'3개월후',width:'15%',halign:"center",align:'right'},
			{field:'wrkjob_cd',title:'업무',hidden:true},
			{field:'rgb_color_value',title:'rgb_color_value',hidden:true}
			]],
			onLoadSuccess: function(data){
//				ajaxCallNewAppTimeoutPredictionChart();
			},
			onLoadError:function() {
				$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});

	dockedRightLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right',
		border : false,
		padding : 5,
        label: {
        	textAlign: 'left',
        	align:'left'
        }
	});
	dockedLeftLegend = Ext.create("Ext.chart.Legend",{
		docked : 'left',
		border : false,
        label: {
        	textAlign: 'left',
           	align:'left'
        }
	});

});

//chart1, 자원 한계점 예측(3월내 한계도래)
var createResourceLimitPointPredictionChart = function(jsondata){
	
	if(resourceLimitPointPredictionChart != "undefined" && resourceLimitPointPredictionChart != undefined){
		resourceLimitPointPredictionChart.destroy();
	}
	
	resourceLimitPointPredictionChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		titleAlign:'center',
		flex : 1,
		border : false,
		renderTo : document.getElementById("resourceLimitPointPredictionChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			captions: {
				title: {
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
			innerPadding : '10 10 5 5', // 차트안쪽 여백
			insetPadding : '10 10 10 5', // 차트 밖 여백
//			store : { data : jsondata },
//	        store: store1,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minorTickSteps: 0,
				minimum: 0,
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
				grid: true,
				label : {
					x : 0,
					y : 0
				}
			}],
			series: [ ]
		}]
	});
}
/* 자원 한계점 예측(3월내 한계도래) 차트 
 * CPU, MEMORY(SGA+PGA) 
 */
function ajaxCallResourceLimitPointPredictionChart(cpuMemoryGubun){
	$("#submit_form #strGubun").val(cpuMemoryGubun);
	ajaxCall("/DashboardV2/getResourceLimitPointPredictionChart", $("#submit_form"), "POST", callback_ResourceLimitPointPredictChartAction);
}

//callback 함수
var callback_ResourceLimitPointPredictChartAction= function(result) {
	var store;
	var data;	
	var chart = resourceLimitPointPredictionChart.down("chart");
	var nameArray = [];
	
	var unit = "";
	if(cpu_memory_gb == "CPU"){
		unit = "(%)";
	}else{
		unit = "(GB)";
	}
	
    if(result.object){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'period',
				yField : post.inst_nm,
				title : post.inst_nm,
				marker: {
	                radius: 2,
	                lineWidth: 2
	            },
	            highlight: {
	                size: 7,
	                radius: 7
	            },
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						if(cpu_memory_gb == "CPU"){
							tooltip.setHtml("["+item.series.getTitle()+"] "+record.get("period")+" : " + record.get(item.series.getYField()) + "(%)");
						}else{
							tooltip.setHtml("["+item.series.getTitle()+"] "+record.get("period")+" : " + record.get(item.series.getYField()) + "(GB)");
						}
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			            $("#legend").val(item.series.getTitle());
			            if(cpu_memory_gb == "CPU"){
			            	/*
			            	 * 1. 장애예방 > 장애예측분석 > CPU한계점예측 으로 이동
			            	 * 2. 파라미터- DBID, INST_ID, 예측기준(3개월후한계점도달)
			            	 */
			            	//장애예방 > 장애예측분석 > CPU한계점예측
			            	var menuParam = "dbid="+item.record.data.dbid+"&inst_id="+item.record.data.inst_id+"&call_from_parent=Y";
			            	createNewTab("186", "CPU 한계점 예측", "/CPULimitPointPrediction", menuParam)
			            }else if(cpu_memory_gb == "MEMORY"){
			            	//장애예방 > 장애예측분석 > MEMORY한계점예측
			            	var menuParam = "dbid="+item.record.data.dbid+"&inst_id="+item.record.data.inst_id+"&call_from_parent=Y";
			            	//createNewTab("186", "MEMORY 한계점 예측", "/MemoryLimitPointPrediction", menuParam)
			            	$.messager.alert('알림','준비중입니다.','info');
			            }
			        }
			    }
			});
		}
		
		var axes = [];
		axes.push(
			{
				type : 'numeric',
				position : 'left',
				title : unit,
				minorTickSteps: 0,
	    		minimum: 0,
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
				grid: true,
				label : {
					x : 0,
					y : 0
				}
			}
		);
		
//		var captions = [];
//		captions.push({
//			title: {
//				align: 'left',
//				style: {
//					color: "#000000",
//					font: 'bold 12px Arial',
//					fill:"#000000"
//				}
//			}
//		});
		
		chart.setAxes(axes);
		chart.setSeries(nameArray);
//		chart.setCaptions(captions);
		chart.setInnerPadding('10 10 5 5');// 차트안쪽 여백
		chart.setInsetPadding('10 10 10 5');// 차트 밖 여백
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		
		var jsondata = data.rows;
		store.loadData(jsondata);
		
		var rows = $("#resourceLimitPointPrediction").datagrid('getRows');
		var colors = new Array();
		for(var i=0;i<rows.length;i++){
			colors.push(rows[i].rgb_color_value);
		}

		//console.log("colors :",colors);
		
		chart.setColors(colors);
		chart.redraw();
	}
};
//callback 함수
var callback_ResourceLimitPointPredictChartAction1 = function(result) {
	var data = JSON.parse(result.txtValue);

	createResourceLimitPointPredictionChart(data.rows);
};

//chart, Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp
//Tablespace chart
var createTablespacePresentConditionChart = function(){

	if(tablespaceSequencePresentConditionChart != "undefined" && tablespaceSequencePresentConditionChart != undefined){
		tablespaceSequencePresentConditionChart.destroy();
	}
	
	tablespaceSequencePresentConditionChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		titleAlign:'center',
		flex : 1,
		border : false,
		renderTo : document.getElementById("tablespaceSequencePresentConditionChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
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
//		    legend: {
//	            type: 'sprite',
//	            docked: 'right'
//	        },
			//legend : dockedRightLegend,
		    innerPadding : '10 10 5 5', // 차트안쪽 여백
			insetPadding : '10 10 10 5', // 차트 밖 여백
//			store : { data : jsondata },
//			store : store3,
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(%)',
				minimum : 0,
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
				grid: true,
				label : {
					x : 0,
					y : 0
				}
			}],
	        series : []
		}]
	});
}

//chart3, 신규SQL/App Timeout 예측
//신규SQL
var createNewSqlTimeoutPredictionChart=function(jsondata){
	if(newSQLAppTimeoutPredictChart != "undefined" && newSQLAppTimeoutPredictChart != undefined){
		newSQLAppTimeoutPredictChart.destroy();
	}
	
//	newSQLAppTimeoutPredictChart = Ext.create("Ext.panel.Panel",{
//	newSQLAppTimeoutPredictChart = Ext.create({
	newSQLAppTimeoutPredictChart = Ext.create("Ext.chart.PolarChart",{
	   xtype: 'polar',
	   renderTo : document.getElementById("newSQLAppTimeoutPredictChart"),
	   width: 410,
	   height: 205,
	   border:0,
//	   legend: dockedRightLegend,
	   legend:{
		   docked:'right'
	   },
	   theme: 'green',
//	   innerPadding : '10 10 0 5', // 차트안쪽 여백
	   insetPadding : '35', // 차트 밖 여백
	   interactions: ['rotate', 'itemhighlight']
	});	
}

var drawNewSqlTimeoutPredictionChart=function(jsondata){
	var series = [];
	
	series.push({
		type : 'pie'
		,showInLegend: true
		,label:{
			//position : 'left',
			field:'db_name',
			display: 'inside',//'inside','outside' | 'rotate' | 'horizontal' | 'vertical'.
			contrast: true,
			font: '12px Arial',
			color:'#ffffff'
		}
		,highlight: false
//		,highlight:{
//			margin:40
//		}
//	    ,highlight: {
//	        segment: {
//	            margin: 20
//	        }
//	    }	
		,angleField:'total_cnt'
		//,xField: 'cnt'
		,donut: 0
		,thickness:50
//		,distortion:0.65
		,pieStyle:{
			bevelWidth: 35,
			colorSpread: 2
		}
		,tooltip:{
			trackMouse:true,
			mouseOffset: [1,-10],
			renderer:function(tooltip,record){
//				tooltip.setTitle(record.get("db_name"));
				tooltip.update(record.get("db_name")+","+record.get("total_cnt"));
			}
		},
		listeners: {
			itemclick: function (chart, item, event) {
				
				fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
	
	//				1. 장애예방 > 장애예측분석 > 신규SQL타임아웃예측 화면으로 이동
	//				2. 파라미터
	//				- WRKJOB_CD
	//				- 발생일 조건: 최근3개월
	//				- 타임아웃조건 : 1주일후
	//				- 예외 조건 : 미예외
					var menuParam = "&dbid="+item.record.data.dbid+"&predict_standard=3&timeout_condition=1&except_yn=N&call_from_parent=Y";
					createNewTab("225", "신규 SQL 타임아웃 예측", "/NewSQLTimeoutPrediction", menuParam)
			}
		}
	});
	
	try {
		var chart = newSQLAppTimeoutPredictChart;
		
		var store = chart.getStore();
		store.loadData(jsondata);
		var colors = new Array();
		
		for(var index = 0; index < jsondata.length; index++) {
			colors.push(jsondata[index].rgb_color_value);
		}
		chart.setSeries(series);
		chart.setColors(colors);
		chart.redraw();
	} catch(error) {
		console.log(error.message);
	}
	
}

//chart3, 신규SQL/App Timeout 예측
//신규App
var drawNewAppTimeoutPredictionChart=function(jsondata){
	var series = [];
	
	series.push({
		type : 'pie'
		,showInLegend: true
		,label:{
			//position : 'left',
			field:'wrkjob_cd_nm',
			display: 'inside',//'inside','outside' | 'rotate' | 'horizontal' | 'vertical'.
			contrast: true,
			font: '12px Arial',
			color:'#ffffff'
		}
		,highlight: false
//		,highlight:{
//			margin:40
//		}
//	    ,highlight: {
//      	segment: {
//          	margin: 20
//      	}
//  	}
		,angleField:'total_cnt'
		//,xField: 'cnt'
		,donut: 0
		,thickness:50
		,distortion:0.65
		,pieStyle:{
			bevelWidth: 35,
			colorSpread: 2
		}
		,tooltip:{
			trackMouse:true,
			mouseOffset: [1,-10],
			renderer:function(tooltip,record){
	//			tooltip.setTitle(record.get("wrkjob_cd_nm"));
				tooltip.update(record.get("wrkjob_cd_nm")+","+record.get("total_cnt"));
			}
		},
		listeners: {
			itemclick: function (chart, item, event) {
				
				fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
	
	//				1. 장애예방 > 장애예측분석 > 신규SQL타임아웃예측 화면으로 이동
	//				2. 파라미터
	//				- WRKJOB_CD
	//				- 발생일 조건: 최근3개월
	//				- 타임아웃조건 : 1주일후
	//				- 예외 조건 : 미예외
					var menuParam = "&wrkjob_cd="+item.record.data.wrkjob_cd+"&predict_standard=3&timeout_condition=1&except_yn=N&call_from_parent=Y";
					createNewTab("224", "신규 APP 타임아웃 예측", "/NewAppTimeoutPrediction", menuParam);
			}
		}
	});

	try {
		var chart = newSQLAppTimeoutPredictChart;
		
		var store = chart.getStore();
		store.loadData(jsondata);
		var colors = new Array();
		
		for(var index = 0; index < jsondata.length; index++) {
			colors.push(jsondata[index].rgb_color_value);
		}
		chart.setSeries(series);
		chart.setColors(colors);
		chart.redraw();
	} catch(error) {
		console.log(error.message);
	}
	
}

/*Grid1 자원 한계점 예측(3월내 한계도래) */
function ajaxCallResourceLimitPointPrediction(){
	ajaxCall("/DashboardV2/getResourceLimitPointPredictionList", $("#submit_form"), "POST", callback_ResourceLimitPointPredictAction);
}

/*Grid1 자원 한계점 예측(3월내 한계도래) */
//callback 함수
var callback_ResourceLimitPointPredictAction = function(result) {
	var data = JSON.parse(result);
	$('#resourceLimitPointPrediction').datagrid("loadData", data);
	$('#resourceLimitPointPrediction').datagrid('loaded');

};

/*Grid2 Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp */
function ajaxCallTablespacePresentConditionDBList(){
	ajaxCall("/DashboardV2/getTablespacePresentConditionDBList", $("#submit_form"), "POST", callback_TablespacePresentConditionDBListAction);
}

/*Grid2 Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp */
//callback 함수
var callback_TablespacePresentConditionDBListAction = function(result) {
	var data = JSON.parse(result);
	$('#tablespacePresentConditionDBList').datagrid("loadData", data);
	$('#tablespacePresentConditionDBList').datagrid('loaded');
};

/*Grid2 Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp */
function ajaxCallTablespacePresentCondition(){
	ajaxCall("/DashboardV2/getTablespacePresentConditionList", $("#submit_form"), "POST", callback_TablespacePresentConditionAction);
}

//callback 함수
var callback_TablespacePresentConditionAction = function(result) {
	var data = JSON.parse(result);
	$('#tablespacePresentCondition').datagrid("loadData", data);
	$('#tablespacePresentCondition').datagrid('loaded');
};

/* Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp 차트 */
function ajaxCallTablespacePresentConditionChart(){
	ajaxCall("/DashboardV2/getTablespacePresentConditionChart", $("#submit_form"), "POST", callback_TablespacePresentConditionChartAction);
}

/* Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp 차트 */
var callback_TablespacePresentConditionChartAction = function(result) {
	var store;
	var data;	
	var chart = tablespaceSequencePresentConditionChart.down("chart");
	var nameArray = [];
	
	if(result.object){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'period',
				yField : post.tablespace_name,
				title : post.tablespace_name,	
				marker: {
	                radius: 2,
	                lineWidth: 2
	            },
	            highlight: {
	                size: 7,
	                radius: 7
	            },
	            markerConfig: {
	                type: 'cross',
	                size: 4,
	                radius: 4,
	                'stroke-width': 0
	            },	            
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml("["+item.series.getTitle()+"] "+record.get("period")+" : " + record.get(item.series.getYField()) + "(%)");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        }
			    }
			});
		}
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}	
};


/*Grid2 Sequence/SEQ 현황 = 3월내 한계도래되는것만 Disp */
function ajaxCallSequencePresentConditionDBList(){
	ajaxCall("/DashboardV2/getSequencePresentConditionDBList", $("#submit_form"), "POST", callback_SequencePresentConditionDBListAction);
}

/*Grid2 Sequence/SEQ 현황 = 3월내 한계도래되는것만 Disp */
//callback 함수
var callback_SequencePresentConditionDBListAction = function(result) {
	var data = JSON.parse(result);
	$('#sequencePresentConditionDBList').datagrid("loadData", data);
	$('#sequencePresentConditionDBList').datagrid('loaded');
};

/*Grid2 Sequence/SEQ 현황 = 3월내 한계도래되는것만 Disp */
function ajaxCallSequencePresentCondition(){
	ajaxCall("/DashboardV2/getSequencePresentConditionList", $("#submit_form"), "POST", callback_SequencePresentConditionAction);
}

//callback 함수
var callback_SequencePresentConditionAction = function(result) {
	var data = JSON.parse(result);
	$('#sequencePresentCondition').datagrid("loadData", data);
	$('#sequencePresentCondition').datagrid('loaded');
};

/* Sequence/SEQ 현황 = 3월내 한계도래되는것만 Disp 차트 */
function ajaxCallSequencePresentConditionChart(){
	ajaxCall("/DashboardV2/getSequencePresentConditionChart", $("#submit_form"), "POST", callback_SequencePresentConditionChartAction);
}

/* Sequence/SEQ 현황 = 3월내 한계도래되는것만 Disp 차트 */
var callback_SequencePresentConditionChartAction = function(result) {
	var store;
	var data;	
	var chart = tablespaceSequencePresentConditionChart.down("chart");
	var nameArray = [];
	
	if(result.object){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];

			nameArray.push({
				type : 'line',
				xField : 'period',
				yField : post.sequence_name,
				title : post.sequence_name,	
				marker: {
	                radius: 2,
	                lineWidth: 2
	            },
	            highlight: {
	                size: 7,
	                radius: 7
	            },
	            markerConfig: {
	                type: 'circle',
	                size: 4,
	                radius: 4,
	                'stroke-width': 0
	            },	            
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(record.get("period")+"["+item.series.getTitle()+"] : " + record.get(item.series.getYField()) + " %");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        }
			    }
			});
		}
		chart.setSeries(nameArray);		
		data = JSON.parse(result.txtValue);
		store = chart.getStore();
		store.loadData(data.rows);
		chart.redraw();
	}	
};

/*Grid3 신규SQL/App Timeout 예측*/
function ajaxCallNewSQLTimeoutPrediction(){
	ajaxCall("/DashboardV2/getNewSQLTimeoutPredictionList", $("#submit_form"), "POST", callback_NewSQLTimeoutPredictAction);
}

/*Grid3 신규SQL/App Timeout 예측*/
//callback 함수
var callback_NewSQLTimeoutPredictAction = function(result) {
	var data = JSON.parse(result);
	$('#newSQLTimeoutPrediction').datagrid("loadData", data);
	$('#newSQLTimeoutPrediction').datagrid('loaded');
	
//	if(data.rows.length > 0){
		drawNewSqlTimeoutPredictionChart(data.rows);
//	}else{
//		$("#newSQLAppTimeoutPredictChart").html("");
//		createNewSqlTimeoutPredictionChart();
//	}
};

/*Grid3 신규SQL/App Timeout 예측*/
function ajaxCallNewAppTimeoutPrediction(){
	ajaxCall("/DashboardV2/getNewAppTimeoutPredictList", $("#submit_form"), "POST", callback_NewAppTimeoutPredictAction);
}

/*Grid3 신규SQL/App Timeout 예측*/
//callback 함수
var callback_NewAppTimeoutPredictAction = function(result) {
	var data = JSON.parse(result);
	$('#newAppTimeoutPrediction').datagrid("loadData", data);
	$('#newAppTimeoutPrediction').datagrid('loaded');
//	if(data.rows.length > 0){
		drawNewAppTimeoutPredictionChart(data.rows);
//	}else{
//		document.getElementById("newSQLAppTimeoutPredictChart").innerHtml="";
//		$("#newSQLAppTimeoutPredictChart").html("");
//		createNewSqlTimeoutPredictionChart();
//	}
};
