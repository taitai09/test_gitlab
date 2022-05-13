var waitChart;
var eventChart;
var fieldName = []; 
var startDt = "";
var endDt = "";

Ext.EventManager.onWindowResize(function () {
    var width = Ext.getBody().getViewSize().width - 40;
    if(waitChart != "undefined" && waitChart != undefined){
    	waitChart.setSize(width);		
	}
    if(eventChart != "undefined" && eventChart != undefined){
    	eventChart.setSize(width);		
	}
});

$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// Instance 조회			
	    	$('#selectInstance').combobox({
	    	    url:"/Common/getAgentInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','Instance 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});	    	
	    }	    
	});	

	$('#chkRealSearch').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#real_search").val("Y"); 
			else $("#real_search").val("N");
		}
	})

	$("#allSessionList").datagrid({
		view: myview,
		rownumbers:true,
		columns:[[
			{field:'inst_id',title:'INST_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sample_id',title:'SAMPLE_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sample_time',title:'SAMPLE_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'partition_key',hidden:"true"},
			{field:'is_awr_sample',title:'IS_AWR_SAMPLE',halign:"center",align:'center',sortable:"true"},
			{field:'session_id',title:'SESSION_ID',halign:"center",align:'right',sortable:"true"},
			{field:'serial',title:'SESSION_SERIAL#',halign:"center",align:'right',sortable:"true"},
			{field:'session_type',title:'SESSION_TYPE',halign:"center",align:'center',sortable:"true"},
			{field:'flags',title:'FLAGS',halign:"center",align:'right',sortable:"true"},
			{field:'user_id',title:'USER_ID',halign:"center",align:'right',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'is_sqlid_current',title:'IS_SQLID_CURRENT',halign:"center",align:'center',sortable:"true"},
			{field:'sql_child_number',title:'SQL_CHILD_NUMBER',halign:"center",align:'right',sortable:"true"},
			{field:'sql_opcode',title:'SQL_OPCODE',halign:"center",align:'right',sortable:"true"},
			{field:'force_matching_signature',title:'FORCE_MATCHING_SIGNATURE',halign:"center",align:'left',sortable:"true"},
			{field:'top_level_sql_id',title:'TOP_LEVEL_SQL_ID',halign:"center",align:'left',sortable:"true"},
			{field:'top_level_sql_opcode',title:'TOP_LEVEL_SQL_OPCODE',halign:"center",align:'right',sortable:"true"},
			{field:'sql_opname',title:'SQL_OPNAME',halign:"center",align:'center',sortable:"true"},
			{field:'sql_plan_hash_value',title:'SQL_PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true"},
			{field:'sql_plan_line_id',title:'SQL_PLAN_LINE_ID',halign:"center",align:'left',sortable:"true"},
			{field:'sql_plan_operation',title:'SQL_PLAN_OPERATION',halign:"center",align:'left',sortable:"true"},
			{field:'sql_plan_options',title:'SQL_PLAN_OPTIONS',halign:"center",align:'left',sortable:"true"},
			{field:'sql_exec_id',title:'SQL_EXEC_ID',halign:"center",align:'center',sortable:"true"},
			{field:'sql_exec_start',title:'SQL_EXEC_START',halign:"center",align:'center',sortable:"true"},
			{field:'plsql_entry_object_id',title:'PLSQL_ENTRY_OBJECT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'plsql_entry_subprogram_id',title:'PLSQL_ENTRY_SUBPROGRAM_ID',halign:"center",align:'left',sortable:"true"},
			{field:'plsql_object_id',title:'PLSQL_OBJECT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'plsql_subprogram_id',title:'PLSQL_SUBPROGRAM_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_instance_id',title:'QC_INSTANCE_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_session_id',title:'QC_SESSION_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_session_serial',title:'QC_SESSION_SERIAL#',halign:"center",align:'left',sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:'left',sortable:"true"},
			{field:'event_id',title:'EVENT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'event_sharp',title:'EVENT#',halign:"center",align:'right',sortable:"true"},
			{field:'seq_sharp',title:'SEQ#',halign:"center",align:'right',sortable:"true"},
			{field:'p1text',title:'P1TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p1',title:'P1',halign:"center",align:'right',sortable:"true"},
			{field:'p2text',title:'P2TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p2',title:'P2',halign:"center",align:'right',sortable:"true"},
			{field:'p3text',title:'P3TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p3',title:'P3',halign:"center",align:'right',sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:'left',sortable:"true"},
			{field:'wait_time',title:'WAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'session_state',title:'SESSION_STATE',halign:"center",align:'center',sortable:"true"},
			{field:'time_waited',title:'TIME_WAITED',halign:"center",align:'right',sortable:"true"},
			{field:'blocking_session_status',title:'BLOCKING_SESSION_STATUS',halign:"center",align:'center',sortable:"true"},
			{field:'blocking_session',title:'BLOCKING_SESSION',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_session_serial',title:'BLOCKING_SESSION_SERIAL#',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_inst_id',title:'BLOCKING_INST_ID',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_hangchain_info',title:'BLOCKING_HANGCHAIN_INFO',halign:"center",align:'left',sortable:"true"},
//			{field:'current_obj',title:'CURRENT_OBJ#',halign:"center",align:'right',sortable:"true"},
//			{field:'current_file',title:'CURRENT_FILE#',halign:"center",align:'right',sortable:"true"},
//			{field:'current_block',title:'CURRENT_BLOCK#',halign:"center",align:'right',sortable:"true"},
//			{field:'current_row',title:'CURRENT_ROW#',halign:"center",align:'right',sortable:"true"},
//			{field:'top_level_call',title:'TOP_LEVEL_CALL#',halign:"center",align:'right',sortable:"true"},
//			{field:'top_level_call_name',title:'TOP_LEVEL_CALL_NAME',halign:"center",align:'left',sortable:"true"},
//			{field:'consumer_group_id',title:'CONSUMER_GROUP_ID',halign:"center",align:'right',sortable:"true"},
//			{field:'xid',title:'XID',halign:"center",align:'left',sortable:"true"},
//			{field:'remote_instance',title:'REMOTE_INSTANCE#',halign:"center",align:'left',sortable:"true"},
			{field:'time_model',title:'TIME_MODEL',halign:"center",align:'right',sortable:"true"},
//			{field:'in_connection_mgmt',title:'IN_CONNECTION_MGMT',halign:"center",align:'center',sortable:"true"},
//			{field:'in_parse',title:'IN_PARSE',halign:"center",align:'center',sortable:"true"},
//			{field:'in_hard_parse',title:'IN_HARD_PARSE',halign:"center",align:'center',sortable:"true"},
//			{field:'in_sql_execution',title:'IN_SQL_EXECUTION',halign:"center",align:'center',sortable:"true"},
//			{field:'in_plsql_execution',title:'IN_PLSQL_EXECUTION',halign:"center",align:'center',sortable:"true"},
//			{field:'in_plsql_rpc',title:'IN_PLSQL_RPC',halign:"center",align:'center',sortable:"true"},
//			{field:'in_plsql_compilation',title:'IN_PLSQL_COMPILATION',halign:"center",align:'center',sortable:"true"},
//			{field:'in_java_execution',title:'IN_JAVA_EXECUTION',halign:"center",align:'center',sortable:"true"},
//			{field:'in_bind',title:'IN_BIND',halign:"center",align:'center',sortable:"true"},
//			{field:'in_cursor_close',title:'IN_CURSOR_CLOSE',halign:"center",align:'center',sortable:"true"},
//			{field:'in_sequence_load',title:'IN_SEQUENCE_LOAD',halign:"center",align:'center',sortable:"true"},
//			{field:'capture_overhead',title:'CAPTURE_OVERHEAD',halign:"center",align:'center',sortable:"true"},
//			{field:'replay_overhead',title:'REPLAY_OVERHEAD',halign:"center",align:'center',sortable:"true"},
//			{field:'is_captured',title:'IS_CAPTURED',halign:"center",align:'center',sortable:"true"},
//			{field:'is_replayed',title:'IS_REPLAYED',halign:"center",align:'center',sortable:"true"},
//			{field:'service_hash',title:'SERVICE_HASH',halign:"center",align:'center',sortable:"true"},
			{field:'program',title:'PROGRAM',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:'left',sortable:"true"},
			{field:'client_id',title:'CLIENT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'machine',title:'MACHINE',halign:"center",align:'left',sortable:"true"},
//			{field:'port',title:'PORT',halign:"center",align:'right',sortable:"true"},
//			{field:'ecid',title:'ECID',halign:"center",align:'left',sortable:"true"},
			{field:'tm_delta_time',title:'TM_DELTA_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'tm_delta_cpu_time',title:'TM_DELTA_CPU_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'tm_delta_db_time',title:'TM_DELTA_DB_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_time',title:'DELTA_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_read_io_requests',title:'DELTA_READ_IO_REQUESTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_write_io_requests',title:'DELTA_WRITE_IO_REQUESTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_read_io_bytes',title:'DELTA_READ_IO_BYTES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_write_io_bytes',title:'DELTA_WRITE_IO_BYTES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'delta_interconnect_io_bytes',title:'DELTA_INTERCONNECT_IO_BYTES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'pga_allocated',title:'PGA_ALLOCATED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'temp_space_allocated',title:'TEMP_SPACE_ALLOCATED',halign:"center",align:'right',sortable:"true"}			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#topsqlList").datagrid({
		view: myview,
		rownumbers:true,
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			$("#sql_id").val(row.sql_id);
			$("#plan_hash_value").val(row.plan_hash_value);

			createTab();
		},		
//		onClickRow : function(index,row) {
//			$("#dbid").val(row.dbid);
//			var jsp_page = getJspPage(row.check_pref_id);
//			$("#check_item_name").val(jsp_page);
//			$("#check_day").val(row.check_day);
//			$("#check_seq").val(row.check_seq);
//			$("#strStartDt").val(row.check_day);
//
//			createNewTab1("", "");
//		},			
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:"center",sortable:"true"},
			{field:'activity',title:'ACTIVITY(%)',halign:"center",align:"right",sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:"left",sortable:"true"}			
		]],		
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#topsessionList").datagrid({
		view: myview,
		rownumbers:true,
		columns:[[
			{field:'session_id',title:'SESSION_ID',halign:"center",align:"center",sortable:"true"},
			{field:'program',title:'PROGRAM',halign:"center",align:"left",sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:"left",sortable:"true"},
			{field:'username',title:'USERNAME',halign:"center",align:"center",sortable:"true"},
			{field:'activity',title:'ACTIVITY(%)',halign:"center",align:'right',sortable:"true"}			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		

	$('.datatime').timespinner({
		showSeconds: true
	});
	
	$("#chartTab").tabs({
		plain: true,
		onSelect:function(title, index){
			if(index == 0 || index == 1){
				createChartLegend();
			}else{
				Search_Grid(startDt,endDt);
			}
		}
	});
});

function createChartLegend(){
	var tab = $('#chartTab').tabs('getSelected');
	var index = $('#chartTab').tabs('getTabIndex',tab);
	
	$("#submit_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#submit_form #inst_id").val($('#selectInstance').combobox('getValue'));
	
	$("#submit_form #sample_start_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
	$("#submit_form #sample_end_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));
	
	$("#submit_form #partition_start_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strStartDt').datebox('getValue'),"-",""));
	$("#submit_form #partition_end_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strEndDt').datebox('getValue'),"-",""));	
	
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	if($('#selectCombo').combobox('getValue') != "" && $('#selectInstance').combobox('getValue') != ""){
		if(index == 0){
			var tab1ClickCount = $("#submit_form #tab1ClickCount").val();
			if(tab1ClickCount < searchBtnClickCount){
				// Wait Class Chart
				ajaxCall("/ASHPerformance/WaitClassChartLegend", $("#submit_form"), "POST", callback_WaitClassChartLegendAction);
				$("#submit_form #tab1ClickCount").val(1);
			}
		}else if(index == 1){
			var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
			if(tab2ClickCount < searchBtnClickCount){
				// Top Wait Class Chart
				ajaxCall("/ASHPerformance/TopWaitEventChartLegend", $("#submit_form"), "POST", callback_TopWaitEventChartLegendAction);
				$("#submit_form #tab2ClickCount").val(1);
			}
		}
	}		
}

var callback_WaitClassChartLegendAction = function(result) {
	if(result.result){
		$("#waitChartInfo").hide();
		fieldName = []; 
		for(var i = 0 ; i < result.object.length ; i++){
			var legend = result.object[i];
			
			fieldName.push(legend.wait_class);
		}
		
		createWaitClassChart();
		Search_Chart();
	}else{
		$.messager.alert('ERROR',result.message,'error', function(){
			$("#waitChartInfo").show();	
			$("#waitChartInfo").html("<b>검색된 조건으로 Wait Class Chart가 생성됩니다.</b>");
		});	
	}
};

var callback_TopWaitEventChartLegendAction = function(result) {
	if(result.result){
		$("#topWaitChartInfo").hide();
		fieldName = []; 
		for(var i = 0 ; i < result.object.length ; i++){
			var legend = result.object[i];
			
			fieldName.push(legend.event);
		}
		
		createTopEventChart();
		Search_Chart();
	}else{
		$.messager.alert('ERROR',result.message,'error', function(){
			$("#topWaitChartInfo").show();	
			$("#topWaitChartInfo").html("<b>검색된 조건으로 Top Wait Event Chart가 생성됩니다.</b>");
		});	
	}
};

function createWaitClassChart(){
	if(waitChart != "undefined" && waitChart != undefined){
		waitChart.destroy();
	}
	
	waitChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("waitChart"),
		layout : 'fit',
		items : {
			xtype : 'chartnavigator',
			reference: 'chartnavigator',
			chart : {
				xtype : 'cartesian',
				reference: 'chart',
				plugins: {
					chartitemevents: {
						moveEvents: true
					}
				},
			    legend : {
					docked : 'right'
				},
		        innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				interactions: {
	                type: 'panzoom',
	                zoomOnPanGesture: false,
	                axes: {
	                    left: {
	                        allowPan: false,
	                        allowZoom: false
	                    }
	                }
	            },
				store : {
					data : []
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minimum : 0,
					grid : true,
					title : '건수'
				},{
					id: 'bottom',
					type : 'category',
					position : 'bottom',
					label: {
		                rotate: {
		                    degrees: -45
		                }
		            }
				}],
				series : {
					type : 'area',
					style : {
						opacity : 0.6
					},
					marker : {
						radius : 4,
						opacity: 0,
		                scaling: 0.01,
		                animation: {
		                    duration: 200,
		                    easing: 'easeOut'
		                }
					},
					highlightCfg : {
						opacity : 1,
						scaling : 1.5
					},
					xField : 'time',
					yField : fieldName,
					title : fieldName,
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
				            event = item.series.getTitle()[fieldIndex];
							
							tooltip.setHtml(record.get('date') + ' ' + record.get('time') + "[ "+event+" ] : " + record.get(item.field) + '건');
						}
					},
					listeners: {
						itemclick: function (chart, item, event) {
				        	$("#selChart").val("Y");					        	
							startDt = dateAddSecond("M", strReplace(item.record.get('date'),"/",""), strReplace(item.record.get('time'),":",""), 150);
				        	endDt = dateAddSecond("P", strReplace(item.record.get('date'),"/",""), strReplace(item.record.get('time'),":",""), 150);

				        	$('#chartTab').tabs('select', 2);					        	
				        }
				    }
				}
			},
			navigator: {
	            axis: 'bottom'
	        }
		}			
	});		
}

function createTopEventChart(){
	if(eventChart != "undefined" && eventChart != undefined){
		eventChart.destroy();
	}

	eventChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("eventChart"),
		layout : 'fit',
		items : {
			xtype : 'chartnavigator',
			reference: 'chartnavigator',
			chart : {
				xtype : 'cartesian',
				reference: 'chart',
				plugins: {
					chartitemevents: {
						moveEvents: true
					}
				},
			    legend : {
					docked : 'right'
				},
		        innerPadding : '10 5 0 5', // 차트안쪽 여백[top, right, bottom, left]
				insetPadding : 10, // 차트 밖 여백
				interactions: {
	                type: 'panzoom',
	                zoomOnPanGesture: false,
	                axes: {
	                    left: {
	                        allowPan: false,
	                        allowZoom: false
	                    }
	                }
	            },
				store : {
					data : []
				},
				axes : [{
					type : 'numeric',
					position : 'left',
					minimum : 0,
					grid : true,
					title : '건수'
				},{
					id: 'bottom',
					type : 'category',
					position : 'bottom',
					label: {
		                rotate: {
		                    degrees: -45
		                }
		            }
				}],
				series : {
					type : 'area',
					style : {
						opacity : 0.6
					},
					marker : {
						radius : 4,
						opacity: 0,
		                scaling: 0.01,
		                animation: {
		                    duration: 200,
		                    easing: 'easeOut'
		                }
					},
					highlightCfg : {
						opacity : 1,
						scaling : 1.5
					},
					xField : 'time',
					yField : fieldName,
					title : fieldName,
					tooltip : {
						trackMouse : true,
						renderer : function(tooltip, record, item){
							var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
				            event = item.series.getTitle()[fieldIndex];
							
							tooltip.setHtml(record.get('date') + ' ' + record.get('time') + "[ "+event+" ] : " + record.get(item.field) + '건');
						}
					},
					listeners: {
						itemclick: function (chart, item, event) {
				        	$("#selChart").val("Y");					        	
				        	
							startDt = dateAddSecond("M", strReplace(item.record.get('date'),"-",""), strReplace(item.record.get('time'),":",""), 150);
				        	endDt = dateAddSecond("P", strReplace(item.record.get('date'),"-",""), strReplace(item.record.get('time'),":",""), 150);

				        	$('#chartTab').tabs('select', 2);					        	
				        }
				    }
				}
			},
			navigator: {
	            axis: 'bottom'
	        }
		}
	});	
}

function Btn_OnClick(){
	$("#selChart").val("N");
	$("#submit_form #sample_start_time").val("")
	$("#submit_form #sample_end_time").val("")
	$("#detail_form #sample_start_time").val("");	
	$("#detail_form #sample_end_time").val("");

	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','INST_ID를 선택해 주세요.');
		return false;
	}	
	
	fnUpdateSearchBtnClickFlag();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		

	createChartLegend();
	Search_Grid("","");
}

function Search_Chart(){
	var tab = $('#chartTab').tabs('getSelected');
	var index = $('#chartTab').tabs('getTabIndex',tab);

	if(index == 0){
		$("#submit_form #tab1ClickCount").val(1);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석 - Wait Class"," "); 
		
		// Wait Class Chart
		ajaxCall("/ASHPerformance/WaitClassChart", $("#submit_form"), "POST", callback_WaitClassChartAction);
	}else if(index == 1){
		$("#submit_form #tab2ClickCount").val(1);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석 - Top Wait Event"," "); 
		
		// Top Wait Event Chart
		ajaxCall("/ASHPerformance/TopWaitEventChart", $("#submit_form"), "POST", callback_TopWaitEventChartAction);
	}
}

var callback_WaitClassChartAction = function(result) {
	var store;
	var data = "";
	
	data = JSON.parse(result);
	store = waitChart.down("chart").getStore();
	
	if(data.rows.length > 1){
		store.loadData(data.rows);
	}else{
		parent.$.messager.alert('','Wait Class Chart 데이터가 존재하지 않습니다.');
	}

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};

var callback_TopWaitEventChartAction = function(result) {
	var store;
	var data = "";
	
	data = JSON.parse(result);
	store = eventChart.down("chart").getStore();
	
	if(data.rows.length > 1){
		store.loadData(data.rows);
	}else{
		parent.$.messager.alert('','TOP Wait Event Chart 데이터가 존재하지 않습니다.');
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};

function Search_Grid(strStartDt, strEndDt){
	var tab = $('#chartTab').tabs('getSelected');
	var index = $('#chartTab').tabs('getTabIndex',tab);
	
	$("#submit_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#submit_form #inst_id").val($('#selectInstance').combobox('getValue'));
	
	if($("#selChart").val() == "N"){
		$("#submit_form #sample_start_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
		$("#submit_form #sample_end_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));		
	}else{
		if(strStartDt != "" && strEndDt != ""){
			$("#submit_form #sample_start_time").val(strStartDt);
			$("#submit_form #sample_end_time").val(strEndDt);
		}
	}

	$("#submit_form #partition_start_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strStartDt').datebox('getValue'),"-",""));
	$("#submit_form #partition_end_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strEndDt').datebox('getValue'),"-",""));		
	
	//검색버튼 클릭여부
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	
	if(index == 2){
		var tab3ClickCount = $("#submit_form #tab3ClickCount").val();
		if(tab3ClickCount < searchBtnClickCount){
			$("#submit_form #tab3ClickCount").val(1);
			
			$("#submit_form #currentPage").val("1");
			$('#allSessionList').datagrid('loadData',[]);
			
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석 - All Session"," "); 
			
			ajaxCall("/ASHPerformance/AllSession", $("#submit_form"), "POST", callback_AllSessionAddTable);		
			
			/* detail_form 에 데이터 Insert */
			$("#detail_form #dbid").val($("#submit_form #dbid").val());
			$("#detail_form #inst_id").val($("#submit_form #inst_id").val());		
			$("#detail_form #sample_start_time").val($("#submit_form #sample_start_time").val());
			$("#detail_form #sample_end_time").val($("#submit_form #sample_end_time").val());
			$("#detail_form #partition_start_key").val($("#submit_form #partition_start_key").val());
			$("#detail_form #partition_end_key").val($("#submit_form #partition_end_key").val());
			$("#detail_form #real_search").val($("#submit_form #real_search").val());
			$("#detail_form #sid").val($("#submit_form #sid").textbox("getValue"));
			$("#detail_form #serial").val($("#submit_form #serial").textbox("getValue"));
			$("#detail_form #sql_id").val($("#submit_form #sql_id").textbox("getValue"));
			$("#detail_form #module").val($("#submit_form #module").textbox("getValue"));
			$("#detail_form #event").val($("#submit_form #event").textbox("getValue"));
			$("#detail_form #currentPage").val($("#submit_form #currentPage").val());
		}
		
		
	}else if(index == 3){
		var tab4ClickCount = $("#submit_form #tab4ClickCount").val();
		if(tab4ClickCount < searchBtnClickCount){
			$("#submit_form #tab4ClickCount").val(1);
	
			$('#topsqlList').datagrid('loadData',[]);
			
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석 - TOP SQL"," "); 
			
			ajaxCall("/ASHPerformance/TopSql", $("#submit_form"), "POST", callback_TopSqlAddTable);		
		}
	}else if(index == 4){
		var tab5ClickCount = $("#submit_form #tab5ClickCount").val();
		if(tab5ClickCount < searchBtnClickCount){
			$("#submit_form #tab5ClickCount").val(1);
			
			$('#topsessionList').datagrid('loadData',[]);
			
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석 - TOP Session"," "); 
			
			ajaxCall("/ASHPerformance/TopSession", $("#submit_form"), "POST", callback_TopSessionAddTable);		
		}
	}		
}

//callback 함수
var callback_AllSessionAddTable = function(result) {
	$("#nextBtn").linkbutton({disabled:false});
	var data = JSON.parse(result);

	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			var msg = data.message;
			msg = msg.replace(/\+/g, '%20');
			//msg = decodeURIComponent(msg);
			parent.$.messager.alert('',msg);
		}
	}else{
		if(data.rows.length > 0){
			$("#detail_form #sample_id").val(data.rows[data.rows.length-1].sample_id);
			$("#detail_form #session_id").val(data.rows[data.rows.length-1].session_id);
			$("#detail_form #partition_key").val(data.rows[data.rows.length-1].partition_key);
		}
		$('#allSessionList').datagrid("loadData", data);
		$("#allSessionList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_TopSqlAddTable = function(result) {
	json_string_callback_common(result,'#topsqlList',true);
};

//callback 함수
var callback_TopSessionAddTable = function(result) {
	json_string_callback_common(result,'#topsessionList',true);
	$("#topsessionList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

function Btn_NextSearch(){
	var currentPage = $("#detail_form #currentPage").val();

	if(currentPage != null && currentPage != ""){
		currentPage++;
		
		$("#detail_form #currentPage").val(currentPage);
	}else{
		$("#detail_form #currentPage").val("1");
	}

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("ASH 성능분석"," "); 
	
	ajaxCall("/ASHPerformance/AllSessionNext",
		$("#detail_form"),
		"POST",
		callback_AllSessionNextAddTable);
}

//callback 함수
var callback_AllSessionNextAddTable = function(result) {
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			$('#allSessionList').datagrid('appendRow',{
				inst_id                     : post.inst_id,
				sample_id                   : post.sample_id,
				sample_time                 : post.sample_time,
				partition_key               : post.partition_key,
				is_awr_sample               : post.is_awr_sample,
				session_id                  : post.session_id,
				serial                      : post.serial,
				session_type                : post.session_type,
				flags                       : post.flags,
				user_id                     : post.user_id,
				sql_id                      : post.sql_id,
				is_sqlid_current            : post.is_sqlid_current,
				sql_child_number            : post.sql_child_number,
				sql_opcode                  : post.sql_opcode,
				force_matching_signature    : post.force_matching_signature,
				top_level_sql_id            : post.top_level_sql_id,
				top_level_sql_opcode        : post.top_level_sql_opcode,
				sql_opname                  : post.sql_opname,
				sql_plan_hash_value         : post.sql_plan_hash_value,
				sql_plan_line_id            : post.sql_plan_line_id,
				sql_plan_operation          : post.sql_plan_operation,
				sql_plan_options            : post.sql_plan_options,
				sql_exec_id                 : post.sql_exec_id,
				sql_exec_start              : post.sql_exec_start,
				plsql_entry_object_id       : post.plsql_entry_object_id,
				plsql_entry_subprogram_id   : post.plsql_entry_subprogram_id,
				plsql_object_id             : post.plsql_object_id,
				plsql_subprogram_id         : post.plsql_subprogram_id,
				qc_instance_id              : post.qc_instance_id,
				qc_session_id               : post.qc_session_id,
				qc_session_serial           : post.qc_session_serial,
				event                       : post.event,
				event_id                    : post.event_id,
				event_sharp                 : post.event_sharp,
				seq_sharp                   : post.seq_sharp,
				p1text                      : post.p1text,
				p1                          : post.p1,
				p2text                      : post.p2text,
				p2                          : post.p2,
				p3text                      : post.p3text,
				p3                          : post.p3,
				wait_class                  : post.wait_class,
				wait_time                   : post.wait_time,
				session_state               : post.session_state,
				time_waited                 : post.time_waited,
				blocking_session_status     : post.blocking_session_status,
				blocking_session            : post.blocking_session,
				blocking_session_serial     : post.blocking_session_serial,
				blocking_inst_id            : post.blocking_inst_id,
				blocking_hangchain_info     : post.blocking_hangchain_info,
				current_obj                 : post.current_obj,
				current_file                : post.current_file,
				current_block               : post.current_block,
				current_row                 : post.current_row,
				top_level_call              : post.top_level_call,
				top_level_call_name         : post.top_level_call_name,
				consumer_group_id           : post.consumer_group_id,
				xid                         : post.xid,
				remote_instance             : post.remote_instance,
				time_model                  : post.time_model,
				in_connection_mgmt          : post.in_connection_mgmt,
				in_parse                    : post.in_parse,
				in_hard_parse               : post.in_hard_parse,
				in_sql_execution            : post.in_sql_execution,
				in_plsql_execution          : post.in_plsql_execution,
				in_plsql_rpc                : post.in_plsql_rpc,
				in_plsql_compilation        : post.in_plsql_compilation,
				in_java_execution           : post.in_java_execution,
				in_bind                     : post.in_bind,
				in_cursor_close             : post.in_cursor_close,
				in_sequence_load            : post.in_sequence_load,
				capture_overhead            : post.capture_overhead,
				replay_overhead             : post.replay_overhead,
				is_captured                 : post.is_captured,
				is_replayed                 : post.is_replayed,
				service_hash                : post.service_hash,
				program                     : post.program,
				module                      : post.module,
				action                      : post.action,
				client_id                   : post.client_id,
				machine                     : post.machine,
				port                        : post.port,
				ecid                        : post.ecid,
				tm_delta_time               : post.tm_delta_time,
				tm_delta_cpu_time           : post.tm_delta_cpu_time,
				tm_delta_db_time            : post.tm_delta_db_time,
				delta_time                  : post.delta_time,
				delta_read_io_requests      : post.delta_read_io_requests,
				delta_write_io_requests     : post.delta_write_io_requests,
				delta_read_io_bytes         : post.delta_read_io_bytes,
				delta_write_io_bytes        : post.delta_write_io_bytes,
				delta_interconnect_io_bytes : post.delta_interconnect_io_bytes,
				pga_allocated               : post.pga_allocated,
				temp_space_allocated        : post.temp_space_allocated
			});		
			
			if(i == (result.object.length-1)){
				$("#detail_form #sample_id").val(post.sample_id);
				$("#detail_form #session_id").val(post.session_id);
				$("#detail_form #partition_key").val(post.partition_key);
			}
		}
	}else{
		//parent.$.messager.alert('','더 이상 검색된 데이터가 없습니다.');	
		parent.$.messager.alert('',result.message);
		$("#nextBtn").linkbutton({disabled:true});
	}
	
	$('#allSessionList').datagrid('reload');
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Excel_DownClick(strGb){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','INST_ID를 선택해 주세요.');
		return false;
	}
	
	$("#submit_form #currentPage").val("0");
	$("#submit_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#submit_form #inst_id").val($('#selectInstance').combobox('getValue'));
	
	if($("#selChart").val() == "N"){
		$("#submit_form #sample_start_time").val($('#strStartDt').datebox('getValue') + " " + $('#strStartTime').timespinner('getValue'));
		$("#submit_form #sample_end_time").val($('#strEndDt').datebox('getValue') + " " + $('#strEndTime').timespinner('getValue'));	
	}
	
	$("#submit_form #partition_start_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strStartDt').datebox('getValue'),"-",""));
	$("#submit_form #partition_end_key").val($("#dbid").val() + "_" + $("#inst_id").val() + "_" + strReplace($('#strEndDt').datebox('getValue'),"-",""));
	
	var strUrl = "";
	
	if(strGb == "1"){
		strUrl = "/ASHPerformance/AllSession/ExcelDown";
	}else if(strGb == "2"){
		strUrl = "/ASHPerformance/TopSql/ExcelDown";
	}else if(strGb == "3"){
		strUrl = "/ASHPerformance/TopSession/ExcelDown";
	}
	
	$("#submit_form").attr("action",strUrl);
	$("#submit_form").submit();
}

/* 일 예방 점검 탭 생성 */
function createTab(){
	var menuId = "131";
	var menuNm = "SQL 성능 분석";
	var menuUrl = "/SQLPerformance";
	var menuParam = "dbid=" + $("#dbid").val() 
			+ "&sql_id=" + $("#sql_id").val() 
			+ "&plan_hash_value=" + $("#plan_hash_value").val();

	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}