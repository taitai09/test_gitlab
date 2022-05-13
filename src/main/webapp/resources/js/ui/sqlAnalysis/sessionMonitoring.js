var waitClassChart;
var topEventChart;
var topModuleChart;
var timer;

Ext.EventManager.onWindowResize(function () {
    var width = $("#waitClassChart").width();
    if(waitClassChart != "undefined" && waitClassChart != undefined){
    	waitClassChart.setSize(width);		
	}
    if(topEventChart != "undefined" && topEventChart != undefined){
    	topEventChart.setSize(width);		
	}
    if(topModuleChart != "undefined" && topModuleChart != undefined){
    	topModuleChart.setSize(width);		
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

	$('#chkType').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#type").val("YES"); 
			else $("#type").val("NO");
		}
	})
	
	$('#chkParallel').switchbutton({
		checked: false,
		onChange: function(checked){
			if(checked) $("#parallel").val("Y"); 
			else $("#parallel").val("N");
		}
	})	
	
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function(checked){
			if(checked) {
				$("#refresh").val("Y");
				$("#refresh_sec").textbox({disabled:false});
				
				Btn_RefreshSearch();
			}else{
				$("#refresh").val("N");
				$("#refresh_sec").textbox({disabled:true});
				
				window.clearTimeout(timer);
			}
		}
	})	
		
	$("#tableDiv").tabs({
		onSelect:function(title, index){
			window.clearTimeout(timer);
//			Btn_RefreshSearch();
			Btn_OnClick();
		}		
	});
	
	$("#tabsDiv").tabs({
		onSelect:function(title, index){
			getSubSessionTables(index);
		}		
	});
	
	createSessionListDatagrid();
	
	createAllCursorsListDatagrid();

});

function createSessionListDatagrid(){
	$("#sessionList").treegrid({
		idField:'sid',
		treeField:'sid',		
		lines: true,
		onClickRow : function(row) {
			$("#sub_form #dbid").val($("#submit_form #dbid").val());
			$("#sub_form #inst_id").val(row.inst_id);
			$("#sub_form #sql_id").val(row.sql_id);
			$("#sub_form #sid").val(row.sid);
			$("#sub_form #serial").val(row.serial);
			$("#sub_form #plan_hash_value").val(row.plan_hash_value);
			
			var tab = $('#tabsDiv').tabs('getSelected');
			var index = $('#tabsDiv').tabs('getTabIndex',tab);

			for(var tabIndex=4;tabIndex<22;tabIndex++){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(0);
			}

//			for(var tabIndex=4;tabIndex<22;tabIndex++){
//				console.log("tab"+tabIndex+"ClickCount:"+$("#submit_form #tab"+tabIndex+"ClickCount").val());
//			}

			getSubSessionTables(index);
		},			
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:'left',sortable:"true"},
			{field:'qcsid',title:'QCSID',halign:"center",align:'center',formatter:changeValue,sortable:"true"},
			{field:'inst_id',title:'INST_ID',halign:"center",align:"center",sortable:"true"},
			{field:'server',title:'SERVER',halign:"center",align:'center',sortable:"true"},
			{field:'qcinst_id',title:'QCINST_ID',halign:"center",align:'right',sortable:"true"},
			{field:'serial',title:'SERIAL#',halign:"center",align:'right',sortable:"true"},
			{field:'status',title:'STATUS',halign:"center",align:'center',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'sql_hash_value',title:'SQL_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'sql_address',title:'SQL_ADDRESS',halign:"center",align:'center',sortable:"true"},
			{field:'usrename',title:'USERNAME',halign:"center",align:'center',sortable:"true"},						
			{field:'spid',title:'SPID',halign:"center",align:'right',sortable:"true"},
			{field:'osuser',title:'OSUSER',halign:"center",align:'center',sortable:"true"},
			{field:'name',title:'NAME',halign:"center",align:'center',sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:'left',sortable:"true"},
			{field:'machine',title:'MACHINE',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:'left',sortable:"true"},
			{field:'program',title:'PROGRAM',halign:"center",align:'left',sortable:"true"},
			{field:'logon_time',title:'LOGON TIME',halign:"center",align:'center',sortable:"true"},
			{field:'logical_reads',title:'LOGICAL READS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'pga',title:'PGA',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'uga',title:'UGA',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sorts',title:'SORTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'workarea_mem_alloc',title:'WORKAREA MEM ALLOC',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'onepass',title:'ONEPASS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'multipass',title:'MULTIPASS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'cpu',title:'CPU',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sql_profile',title:'SQL PROFILE',halign:"center",align:'left',sortable:"true"},
			{field:'sql_startime',title:'SQL STARTIME',halign:"center",align:'center',sortable:"true"},
			{field:'sql_duration',title:'SQL DURATION',halign:"center",align:'center',sortable:"true"},
			{field:'awr_sqlid',title:'AWR_SQLID',halign:"center",align:'left',sortable:"true"},
			{field:'sql_plan_hash_value',title:'SQL_PLAN_HASH_VALUE',halign:"center",align:'left',sortable:"true"},
			{field:'sql_cursor_purge',title:'SQL CURSOR PURGE',halign:"center",align:'left',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"}
		]],

		rowStyler: function(row){
			if(row.status == 'KILLED'){
				return 'background-color:#cccccc;color:#fff;'; // return inline style
			}
		},			
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
}

function createAllCursorsListDatagrid(){
	$("#allCursorsList").datagrid({
		view: myview,
		columns:[[
			{field:'username',title:'PARSING_SCHEMA',halign:"center",align:"center",sortable:"true"},
			{field:'machine',title:'MARCHINE',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MOUDLE',halign:"center",align:'left',sortable:"true"},
			{field:'program',title:'PROGRAM',halign:"center",align:'left',sortable:"true"},
			{field:'terminal',title:'TERMINAL',halign:"center",align:'center',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'sql_text',title:'SQL TEXT',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
}

function createSqlListDatagrid(){
	
	$("#sqlList").datagrid({
		view: myview,
		columns:[[
			{field:'sql_id',title:'SQL_ID',width:"8%",halign:"center",align:"center",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'elapsed_time',title:'응답시간',width:"9%",halign:"center",align:'right',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_USER',width:"9%",halign:"center",align:'center',sortable:"true"},
			{field:'module',title:'MODULE',width:"11%",halign:"center",align:'left',sortable:"true"},
			{field:'executions',title:'실행횟수',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'logical_reads',title:'블록읽기(논리)',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'physical_reads',title:'블록읽기(물리)',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'row_processed',title:'결과건수',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'cpu_time',title:'CPU_TIME',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'cluster_wait_time',title:'CLWAIT_TIME',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'last_active_time',title:'최종수행시간',width:"8%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});	
}
	
function createSessionHistoryListDatagrid(){
	
	$("#sessionHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'sample_id',title:'SAMPLE_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sample_time',title:'SAMPLE_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'is_awr_sample',title:'IS_AWR_SAMPLE',halign:"center",align:'center',sortable:"true"},
			{field:'sid',title:'SESSION_ID',halign:"center",align:'right',sortable:"true"},
			{field:'serial',title:'SESSION_SERIAL#',halign:"center",align:'right',sortable:"true"},
			{field:'session_type',title:'SESSION_TYPE',halign:"center",align:'center',sortable:"true"},
			{field:'flags',title:'FLAGS',halign:"center",align:'right',sortable:"true"},
			{field:'user_id',title:'USER_ID',halign:"center",align:'right',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'left',sortable:"true"},
			{field:'is_sqlid_current',title:'IS_SQLID_CURRENT',halign:"center",align:'left',sortable:"true"},
			{field:'sql_child_number',title:'SQL_CHILD_NUMBER',halign:"center",align:'right',sortable:"true"},
			{field:'sql_opcode',title:'SQL_OPCODE',halign:"center",align:'right',sortable:"true"},
			{field:'force_matching_signature',title:'FORCE_MATCHING_SIGNATURE',halign:"center",align:'right',sortable:"true"},
			{field:'top_level_sql_id',title:'TOP_LEVEL_SQL_ID',halign:"center",align:'left',sortable:"true"},
			{field:'top_level_sql_opcode',title:'TOP_LEVEL_SQL_OPCODE',halign:"center",align:'right',sortable:"true"},
			{field:'sql_opname',title:'SQL_OPNAME',halign:"center",align:'center',sortable:"true"},
			{field:'sql_plan_hash_value',title:'SQL_PLAN_HASH_VALUE',halign:"center",align:'left',sortable:"true"},
			{field:'sql_plan_line_id',title:'SQL_PLAN_LINE_ID',halign:"center",align:'right',sortable:"true"},
			{field:'sql_plan_operation',title:'SQL_PLAN_OPERATION',halign:"center",align:'left',sortable:"true"},
			{field:'sql_plan_options',title:'SQL_PLAN_OPTIONS',halign:"center",align:'center',sortable:"true"},
			{field:'sql_exec_id',title:'SQL_EXEC_ID',halign:"center",align:'center',sortable:"true"},
			{field:'sql_exec_start',title:'SQL_EXEC_START',halign:"center",align:'center',sortable:"true"},
			{field:'plsql_entry_object_id',title:'PLSQL_ENTRY_OBJECT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'plsql_entry_subprogram_id',title:'PLSQL_ENTRY_SUBPROGRAM_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_instance_id',title:'QC_INSTANCE_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_session_id',title:'QC_SESSION_ID',halign:"center",align:'left',sortable:"true"},
			{field:'qc_session_serial',title:'QC_SESSION_SERIAL',halign:"center",align:'left',sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:'left',sortable:"true"},
			{field:'event_id',title:'EVENT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'event_sharp',title:'EVENT#',halign:"center",align:'left',sortable:"true"},
			{field:'sql_sharp',title:'SQL#',halign:"center",align:'left',sortable:"true"},
			{field:'p1text',title:'P1TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p1',title:'P1',halign:"center",align:'left',sortable:"true"},
			{field:'p2text',title:'P2TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p2',title:'P2',halign:"center",align:'left',sortable:"true"},
			{field:'p3text',title:'P3TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'p3',title:'P3',halign:"center",align:'left',sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:'left',sortable:"true"},
			{field:'wait_class_id',title:'WAIT_CLASS_ID',halign:"center",align:'left',sortable:"true"},
			{field:'wait_time',title:'WAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'session_state',title:'SESSION_STATE',halign:"center",align:'center',sortable:"true"},
			{field:'time_waited',title:'TIME_WAITED',halign:"center",align:'right',sortable:"true"},
			{field:'blocking_session_status',title:'BLOCKING_SESSION_STATUS',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_session',title:'BLOCKING_SESSION',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_serial',title:'BLOCKING_SERIAL',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_inst_id',title:'BLOCKING_INST_ID',halign:"center",align:'left',sortable:"true"},
			{field:'blocking_hangchain_info',title:'BLOCKING_HANGCHAIN_INFO',halign:"center",align:'left',sortable:"true"},
			{field:'current_obj',title:'CURRENT_OBJ#',halign:"center",align:'right',sortable:"true"},
			{field:'current_file',title:'CURRENT_FILE#',halign:"center",align:'right',sortable:"true"},
			{field:'current_block',title:'CURRENT_BLOCK#',halign:"center",align:'right',sortable:"true"},
			{field:'current_row',title:'CURRENT_ROW#',halign:"center",align:'right',sortable:"true"},
			{field:'top_level_call',title:'TOP_LEVEL_CALL#',halign:"center",align:'right',sortable:"true"},
			{field:'top_level_call_name',title:'TOP_LEVEL_CALL_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'consumer_group_id',title:'CONSUMER_GROUP_ID',halign:"center",align:'right',sortable:"true"},
			{field:'xid',title:'XID',halign:"center",align:'left',sortable:"true"},
			{field:'remote_instance',title:'REMOTE_INSTANCE#',halign:"center",align:'left',sortable:"true"},
			{field:'time_model',title:'TIME_MODEL',halign:"center",align:'right',sortable:"true"},
			{field:'in_connection_mgmt',title:'IN_CONNECTION_MGMT',halign:"center",align:'center',sortable:"true"},
			{field:'in_parse',title:'IN_PARSE',halign:"center",align:'center',sortable:"true"},
			{field:'in_hard_parse',title:'IN_HARD_PARSE',halign:"center",align:'center',sortable:"true"},
			{field:'in_sql_execution',title:'IN_SQL_EXECUTION',halign:"center",align:'center',sortable:"true"},
			{field:'in_plsql_execution',title:'IN_PLSQL_EXECUTION',halign:"center",align:'center',sortable:"true"},
			{field:'in_plsql_rpc',title:'IN_PLSQL_RPC',halign:"center",align:'center',sortable:"true"},
			{field:'in_plsql_compilation',title:'IN_PLSQL_COMPILATION',halign:"center",align:'center',sortable:"true"},
			{field:'in_java_execution',title:'IN_JAVA_EXECUTION',halign:"center",align:'center',sortable:"true"},
			{field:'in_bind',title:'IN_BIND',halign:"center",align:'center',sortable:"true"},
			{field:'in_cursor_close',title:'IN_CURSOR_CLOSE',halign:"center",align:'center',sortable:"true"},
			{field:'in_sequence_load',title:'IN_SEQUENCE_LOAD',halign:"center",align:'center',sortable:"true"},
			{field:'capture_overhead',title:'CAPTURE_OVERHEAD',halign:"center",align:'center',sortable:"true"},
			{field:'replay_overhead',title:'REPLAY_OVERHEAD',halign:"center",align:'center',sortable:"true"},
			{field:'is_captured',title:'IS_CAPTURED',halign:"center",align:'center',sortable:"true"},
			{field:'is_replayed',title:'IS_REPLAYED',halign:"center",align:'center',sortable:"true"},
			{field:'service_hash',title:'SERVICE_HASH',halign:"center",align:'center',sortable:"true"},
			{field:'program',title:'PROGRAM',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:'left',sortable:"true"},
			{field:'client_id',title:'CLIENT_ID',halign:"center",align:'left',sortable:"true"},
			{field:'machine',title:'MACHINE',halign:"center",align:'left',sortable:"true"},
			{field:'port',title:'PORT',halign:"center",align:'left',sortable:"true"},
			{field:'ecid',title:'ECID',halign:"center",align:'left',sortable:"true"},
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
			{field:'temp_space_allocated',title:'TEMP_SPACE_ALLOCATED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});	
	$("#sessionHistoryList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ "style": "border:0px" });
}
	
function createSqlGridPlanListDatagrid(){
	
	$("#sqlGridPlanList").treegrid({
		idField:'id',
		treeField:'operation',		
		lines: true,
		columns:[[
			{field:'id',title:'ID',halign:"center",align:"center",sortable:"true"},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',sortable:"true"},
			{field:'object_node',title:'OBJECT_NODE',halign:"center",align:'right',sortable:"true"},
			{field:'object',title:'OBJECT#',halign:"center",align:'right',sortable:"true"},
			{field:'object_owner',title:'OBJECT OWNER',halign:"center",align:'center',sortable:"true"},
			{field:'object_name',title:'OBJECT NAME',halign:"center",align:'left',sortable:"true"},
			{field:'object_type',title:'OBJECT TYPE',halign:"center",align:'left',sortable:"true"},
			{field:'optimizer',title:'OPTIMIZER',halign:"center",align:'left',sortable:"true"},
			{field:'cost',title:'COST',halign:"center",align:'right',sortable:"true"},
			{field:'cardinality',title:'CADINALITY',halign:"center",align:'right',sortable:"true"},
			{field:'bytes',title:'BYTES',halign:"center",align:'right',sortable:"true"},
			{field:'other_tag',title:'OTHER_TAG',halign:"center",align:'right',sortable:"true"},
			{field:'partition_start',title:'PARTITION_START',halign:"center",align:'right',sortable:"true"},
			{field:'partition_stop',title:'PARTITION_STOP',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_cost',title:'CPU COST',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'io_cost',title:'IO COST',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_predicates',title:'ACCESS_PREDICATES',halign:"center",align:'left',sortable:"true"},
			{field:'filter_predicates',title:'FILTER_PREDICATES',halign:"center",align:'left',sortable:"true"},
			{field:'projection',title:'PROJECTION',halign:"center",align:'left',sortable:"true"},
			{field:'time',title:'TIME',halign:"center",align:'right',sortable:"true"},
			{field:'qblock_name',title:'QBLOCK_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'timestamp',title:'TIMESTAMP',halign:"center",align:'center',sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});		
}
	
function createConnectInfoListDatagrid(){
	
	$("#connectInfoList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'serial_sharp',title:'SERIAL#',halign:"center",align:"center",sortable:"true"},
			{field:'authentication_type',title:'AUTHENTICATION_TYPE',halign:"center",align:"center",sortable:"true"},
			{field:'osuser',title:'OSUSER',halign:"center",align:"center",sortable:"true"},
			{field:'network_service_banner',title:'NETWORK_SERVICE_BANNER',halign:"center",align:"left",sortable:"true"},
			{field:'client_charset',title:'CLIENT_CHARSET',halign:"center",align:"center",sortable:"true"},
			{field:'client_connection',title:'CLIENT_CONNECTION',halign:"center",align:"center",sortable:"true"},
			{field:'client_oci_library',title:'CLIENT_OCI_LIBRARY',halign:"center",align:"center",sortable:"true"},
			{field:'client_version',title:'CLIENT_VERSION',halign:"center",align:"center",sortable:"true"},
			{field:'client_driver',title:'CLIENT_DRIVER',halign:"center",align:"center",sortable:"true"},
			{field:'client_lobattr',title:'CLIENT_LOBATTR',halign:"center",align:"center",sortable:"true"},
			{field:'client_regid',title:'CLIENT_REGID',halign:"center",align:"center",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createEventListDatagrid(){
	
	$("#eventList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:"center",sortable:"true"},
			{field:'total_waits',title:'TOTAL_WAITS',halign:"center",align:"center",sortable:"true"},
			{field:'total_timeouts',title:'TOTAL_TIMEOUTS',halign:"center",align:"center",sortable:"true"},
			{field:'time_waited',title:'TIME_WAITED',halign:"center",align:"center",sortable:"true"},
			{field:'average_wait',title:'AVERAGE_WAIT',halign:"center",align:"center",sortable:"true"},
			{field:'max_wait',title:'MAX_WAIT',halign:"center",align:"center",sortable:"true"},
			{field:'time_waited_micro',title:'TIME_WAITED_MICRO',halign:"center",align:"center",sortable:"true"},
			{field:'event_id',title:'EVENT_ID',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_id',title:'WAIT_CLASS_ID',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_sharp',title:'WAIT_CLASS#',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:"center",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createWaitListDatagrid(){
	
	$("#waitList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'seq_sharp',title:'SEQ#',halign:"center",align:"center",sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:"left",sortable:"true"},
			{field:'p1text',title:'P1TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p1',title:'P1',halign:"center",align:"center",sortable:"true"},
			{field:'p2text',title:'P2TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p2',title:'P2',halign:"center",align:"center",sortable:"true"},
			{field:'p2raw',title:'P2RAW',halign:"center",align:"center",sortable:"true"},
			{field:'p3text',title:'P3TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p3',title:'P3',halign:"center",align:"center",sortable:"true"},
			{field:'p3raw',title:'P3RAW',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_id',title:'WAIT_CLASS_ID',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_sharp',title:'WAIT_CLASS#',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:"center",sortable:"true"},
			{field:'wait_time',title:'WAIT_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'seconds_in_wait',title:'SECONDS_IN_WAIT',halign:"center",align:"center",sortable:"true"},
			{field:'state',title:'STATE',halign:"center",align:"center",sortable:"true"},
			{field:'wait_time_micro',title:'WAIT_TIME_MICRO',halign:"center",align:"center",sortable:"true"},
			{field:'time_remaining_micro',title:'TIME_REMAINING_MICRO',halign:"center",align:"right",sortable:"true"},
			{field:'time_since_last_wait_micro',title:'TIME_SINCE_LAST_WAIT_MICRO',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createWaitClassListDatagrid(){
	
	$("#waitClassList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'serial',title:'SERIAL#',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_id',title:'WAIT_CLASS_ID',halign:"center",align:"center",sortable:"true"},
			{field:'wait_class_sharp',title:'WAIT_CLASS#',halign:"center",align:"right",sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:"center",sortable:"true"},
			{field:'total_waits',title:'TOTAL_WAITS',halign:"center",align:"right",sortable:"true"},
			{field:'time_waited',title:'TIME_WAITED',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});	
}
	
function createWaitHistoryListDatagrid(){
	
	$("#waitHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'seq_sharp',title:'SEQ#',halign:"center",align:"center",sortable:"true"},
			{field:'event_sharp',title:'EVENT#',halign:"center",align:"right",sortable:"true"},
			{field:'event',title:'EVENT',halign:"center",align:"left",sortable:"true"},
			{field:'p1text',title:'P1TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p1',title:'P1',halign:"center",align:"center",sortable:"true"},
			{field:'p2text',title:'P2TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p2',title:'P2',halign:"center",align:"center",sortable:"true"},
			{field:'p3text',title:'P3TEXT',halign:"center",align:"center",sortable:"true"},
			{field:'p3',title:'P3',halign:"center",align:"center",sortable:"true"},
			{field:'wait_time',title:'WAIT_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'wait_time_micro',title:'WAIT_TIME_MICRO',halign:"center",align:"right",sortable:"true"},
			{field:'time_since_last_wait_micro',title:'TIME_SINCE_LAST_WAIT_MICRO',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});	
}
	
function createMetricListDatagrid(){
	
	$("#metricList").datagrid({
		view: myview,
		columns:[[
			{field:'begin_time',title:'BEGIN_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'end_time',title:'END_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'intsize_cses',title:'INTSIZE_CSES',halign:"center",align:"center",sortable:"true"},
			{field:'session_id',title:'SESSION_ID',halign:"center",align:"center",sortable:"true"},
			{field:'serial_num',title:'SERIAL_NUM',halign:"center",align:"center",sortable:"true"},
			{field:'cpu',title:'CPU',halign:"center",align:"right",sortable:"true"},
			{field:'physical_reads',title:'PHYSICAL_READS',halign:"center",align:"right",sortable:"true"},
			{field:'logical_reads',title:'LOGICAL_READS',halign:"center",align:"right",sortable:"true"},
			{field:'pga_memory',title:'PGA_MEMORY',halign:"center",align:"right",sortable:"true"},
			{field:'hard_parses',title:'HARD_PARSES',halign:"center",align:"right",sortable:"true"},
			{field:'soft_parses',title:'SOFT_PARSES',halign:"center",align:"right",sortable:"true"},
			{field:'physical_read_pct',title:'PHYSICAL_READ_PCT',halign:"center",align:"right",sortable:"true"},
			{field:'logical_read_pct',title:'LOGICAL_READ_PCT',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createTimeModelListDatagrid(){
	
	$("#timeModelList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'stat_id',title:'STAT_ID',halign:"center",align:"center",sortable:"true"},
			{field:'stat_name',title:'STAT_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'value',title:'VALUE',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createStatisticsListDatagrid(){
	
	$("#statisticsList").datagrid({
		view: myview,
		columns:[[
			{field:'name',title:'NAME',halign:"center",align:"left",sortable:"true"},
			{field:'wait_class',title:'WAIT_CLASS',halign:"center",align:"center",sortable:"true"},
			{field:'value',title:'VALUE',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createIoListDatagrid(){
	
	$("#ioList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'block_gets',title:'BLOCK_GETS',halign:"center",align:"right",sortable:"true"},
			{field:'consistent_gets',title:'CONSISTENT_GETS',halign:"center",align:"right",sortable:"true"},
			{field:'physical_reads',title:'PHYSICAL_READS',halign:"center",align:"right",sortable:"true"},
			{field:'block_changes',title:'BLOCK_CHANGES',halign:"center",align:"right",sortable:"true"},
			{field:'consistent_changes',title:'CONSISTENT_CHANGES',halign:"center",align:"right",sortable:"true"},
			{field:'oprimized_physical_reads',title:'OPRIMIZED_PHYSICAL_READS',halign:"center",align:"right",sortable:"true"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}
	
function createLongOpsListDatagrid(){
	
	$("#longOpsList").datagrid({
		view: myview,
		columns:[[
			{field:'sid',title:'SID',halign:"center",align:"center",sortable:"true"},
			{field:'serial_sharp',title:'SERIAL#',halign:"center",align:"center",sortable:"true"},
			{field:'opname',title:'OPNAME',halign:"center",align:"center",sortable:"true"},
			{field:'target',title:'TARGET',halign:"center",align:"center",sortable:"true"},
			{field:'target_desc',title:'TARGET_DESC',halign:"center",align:"center",sortable:"true"},
			{field:'sofar',title:'SOFAR',halign:"center",align:"right",sortable:"true"},
			{field:'totalwork',title:'TOTALWORK',halign:"center",align:"right",sortable:"true"},
			{field:'units',title:'UNITS',halign:"center",align:"center",sortable:"true"},
			{field:'start_time',title:'START_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'last_update_time',title:'LAST_UPDATE_TIME',halign:"center",align:"center",sortable:"true"},
			{field:'timestamp',title:'TIMESTAMP',halign:"center",align:"center",sortable:"true"},
			{field:'time_remaining',title:'TIME_REMAINING',halign:"center",align:"center",sortable:"true"},
			{field:'elapsed_seconds',title:'ELAPSED_SECONDS',halign:"center",align:"center",sortable:"true"},
			{field:'context',title:'CONTEXT',halign:"center",align:"center",sortable:"true"},
			{field:'message',title:'MESSAGE',halign:"center",align:"center",sortable:"true"},
			{field:'username',title:'USERNAME',halign:"center",align:"center",sortable:"true"},
			{field:'sql_address',title:'SQL_ADDRESS',halign:"center",align:"center",sortable:"true"},
			{field:'sql_hash_value',title:'SQL_HASH_VALUE',halign:"center",align:"center",sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sql_plan_hash_value',title:'SQL_PLAN_HASH_VALUE',halign:"center",align:"center",sortable:"true"},
			{field:'sql_exec_start',title:'SQL_EXEC_START',halign:"center",align:"center",sortable:"true"},
			{field:'sql_exec_id',title:'SQL_EXEC_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sql_plan_line_id',title:'SQL_PLAN_LINE_ID',halign:"center",align:"center",sortable:"true"},
			{field:'sql_plan_operation',title:'SQL_PLAN_OPERATION',halign:"center",align:"center",sortable:"true"},
			{field:'sql_plan_options',title:'SQL_PLAN_OPTIONS',halign:"center",align:"center",sortable:"true"},
			{field:'qcsid',title:'QCSID',halign:"center",align:"center",sortable:"true"}
			]],	
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
}

function Btn_RefreshSearch(){
	var intSec = 0;
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();	
	
	if($('#selectCombo').combobox('getValue') != ""){
		Btn_OnClick();
		if($("#refresh").val() == "Y"){
			intSec = strParseInt($("#refresh_sec").textbox("getValue"),0);			
			timer = window.setTimeout("Btn_RefreshSearch()",(intSec*1000));
		}else{
			window.clearTimeout(timer);
		}
	}
}

function Btn_OnClick(){	
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();

	var tab = $('#tableDiv').tabs('getSelected');
	var index = $('#tableDiv').tabs('getTabIndex',tab);
	var tabIndex = index + 1;
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#status").val($('#selectStatus').combobox('getValue'));
	
	var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
	
	if(tabClickCount < searchBtnClickCount){
		$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
		if(index == 0){ // Session List
			$('#sessionList').treegrid('loadData',[]);
			
			if($("#refresh").val() == "Y"){
				$('#sessionList').treegrid('options').loadMsg = '데이터를 불러오는 중입니다.';
				$('#sessionList').treegrid('loading');			
			}else{
				/* modal progress open */
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("세션 성능 분석"," "); 		
			}
			
			ajaxCall("/SessionMonitoring", $("#submit_form"), "POST", callback_SessionMonitoringAddTable);		
		}else if(index == 1){ // Wait Class Chart
			createWaitClassChart();
		}else if(index == 2){ // Top Event Chart
			createTopEventChart();
		}else if(index == 3){ // Top Module Chart
			createTopModuleChart();
		}
	}
	
	$('#tabsDiv').tabs('select',0);

}

//callback 함수
var callback_SessionMonitoringAddTable = function(result) {
	try{
		var data = JSON.parse(result);
		
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			$('#sessionList').treegrid("loadData", data);
		}
		
		if($("#refresh").val() == "Y"){
			$('#sessionList').treegrid('loaded');		
		}
	}catch(e){
		if(e.message == "Unexpected token < in JSON at position 8"){
			parent.$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);
			});			
		}
	}finally{
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();			
	}
};

function createWaitClassChart(){
	if(waitClassChart != "undefined" && waitClassChart != undefined){
		waitClassChart.destroy();
	}
	waitClassChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("waitClassChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			flipXY : true, // 가로/세로 축 변경
			border : false,
		    innerPadding : '10 5 5 0', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				adjustByMajorUnit: true,
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
				title : '(CNT)'
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
				style: {
	  			  minGapWidth: 10
				},
				xField : 'wait_class',
				yField : 'wait_class_cnt',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('wait_class')+" : " + value + " CNT");
					}
				}
			}
		}]
	});			
	
	/* 차트 데이터 생성 */
	ajaxCall("/SessionMonitoring/WaitClassChart",
			$("#submit_form"),
			"POST",
			callback_WaitClassChartAction);	
}

function createTopEventChart(){
	if(topEventChart != "undefined" && topEventChart != undefined){
		topEventChart.destroy();
	}
	topEventChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("topEventChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			flipXY : true, // 가로/세로 축 변경
			border : false,
		    innerPadding : '10 5 5 0', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				adjustByMajorUnit: true,
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
				title : '(CNT)'
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
				style: {
	  			  minGapWidth: 10
				},
				xField : 'event',
				yField : 'event_cnt',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('event')+" : " + value + " CNT");
					}
				}
			}
		}]
	});
	
	/* 차트 데이터 생성 */
	ajaxCall("/SessionMonitoring/TOPEventChart",
			$("#submit_form"),
			"POST",
			callback_TOPEventChartAction);	
}

function createTopModuleChart(){
	if(topModuleChart != "undefined" && topModuleChart != undefined){
		topModuleChart.destroy();
	}
	topModuleChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("topModuleChart"),
		layout : 'fit',				
		items : [{
			xtype : 'cartesian',
			flipXY : true, // 가로/세로 축 변경
			border : false,
		    innerPadding : '10 5 5 0', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				adjustByMajorUnit: true,
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
				title : '(CNT)'
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
				style: {
	  			  minGapWidth: 10
				},
				xField : 'module',
				yField : 'module_cnt',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						var fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(record.get('module')+" : " + value + " CNT");
					}
				}
			}
		}]
	});			
	
	/* 차트 데이터 생성 */
	ajaxCall("/SessionMonitoring/TOPModuleChart",
			$("#submit_form"),
			"POST",
			callback_TOPModuleChartAction);	
}

//callback 함수
var callback_WaitClassChartAction = function(result) {
	chart_callback(result, waitClassChart);
};

//callback 함수
var callback_TOPEventChartAction = function(result) {
	chart_callback(result, topEventChart);
};

//callback 함수
var callback_TOPModuleChartAction = function(result) {
	chart_callback(result, topModuleChart);
};

function getSubSessionTables(strIndex){
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	var tabIndex = strIndex + 4;
	if($("#sub_form #inst_id").val() != ""){

		var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
		if(tabClickCount < searchBtnClickCount){
			$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
		
			if(strIndex == "0"){
				/* last Cursor */
				ajaxCall("/SessionMonitoring/LastCursor", $("#sub_form"), "POST", callback_LastCursorAction);		
			}else if(strIndex == "1"){
				createAllCursorsListDatagrid();
				
				/* all Cursor */
				$('#allCursorsList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/AllCursor", $("#sub_form"), "POST", callback_AllCursorAction);		
			}else if(strIndex == "2"){
				createSqlListDatagrid();
				
				/* SQL 성능 */
				$('#sqlList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/SQLPerformance", $("#sub_form"), "POST", callback_SQLPerformanceAction);		
			}else if(strIndex == "3"){
				createSessionHistoryListDatagrid();
				
				/* Session History */
				$('#sessionHistoryList').datagrid('loadData',[]);		
				ajaxCall("/SessionMonitoring/SQLHistory", $("#sub_form"), "POST", callback_SQLHistoryAction);
			}else if(strIndex == "4"){
				createSqlGridPlanListDatagrid();
				
				/* SQL Grid Plan */
				$('#sqlGridPlanList').treegrid('loadData',[]);
				ajaxCall("/SessionMonitoring/SQLGridPlanList", $("#sub_form"), "POST", callback_SQLGridPlanListAction);		
			}else if(strIndex == "5"){
				/* SQL Text Plan */
				ajaxCall("/SessionMonitoring/SQLTextPlanList", $("#sub_form"), "POST", callback_SQLTextPlanListAction);		
			}else if(strIndex == "6"){
				/* Session Kill */
				ajaxCall("/SessionMonitoring/SessionKillScript", $("#sub_form"), "POST", callback_SessionKillScriptAction);		
			}else if(strIndex == "7"){
				/* Process Kill */
				ajaxCall("/SessionMonitoring/ProcessKillScript", $("#sub_form"), "POST", callback_ProcessKillScriptAction);		
			}else if(strIndex == "8"){
				createConnectInfoListDatagrid();
				
				/* Connect Info */
				$('#connectInfoList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/ConnectInfo", $("#sub_form"), "POST", callback_ConnectInfoAction);
			}else if(strIndex == "9"){
				createEventListDatagrid();
				
				/* Event */
				$('#eventList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/Event", $("#sub_form"), "POST", callback_EventAction);
			}else if(strIndex == "10"){
				createWaitListDatagrid();
				
				/* Wait */
				$('#waitList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/Wait", $("#sub_form"), "POST", callback_WaitAction);
			}else if(strIndex == "11"){
				createWaitClassListDatagrid();
				
				/* Wait Class */
				$('#waitClassList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/WaitClass", $("#sub_form"), "POST", callback_WaitClassAction);			
			}else if(strIndex == "12"){
				createWaitHistoryListDatagrid();
				
				/* Wait History */
				$('#waitHistoryList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/WaitHistory", $("#sub_form"), "POST", callback_WaitHistoryAction);
			}else if(strIndex == "13"){
				createMetricListDatagrid();
				
				/* Metric */
				$('#metricList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/Metric", $("#sub_form"), "POST", callback_MetricAction);
			}else if(strIndex == "14"){
				createTimeModelListDatagrid();
				
				/* Time Model */
				$('#timeModelList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/TimeModel", $("#sub_form"), "POST", callback_TimeModelAction);
			}else if(strIndex == "15"){
				createStatisticsListDatagrid();
				
				/* Statistics */
				$('#statisticsList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/Statistics", $("#sub_form"), "POST", callback_StatisticsAction);
			}else if(strIndex == "16"){
				createIoListDatagrid();
				
				/* IO */
				$('#ioList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/IO", $("#sub_form"), "POST", callback_IOAction);
			}else if(strIndex == "17"){
				createLongOpsListDatagrid();

				/* Long OPS */
				$('#longOpsList').datagrid('loadData',[]);
				ajaxCall("/SessionMonitoring/LongOPS", $("#sub_form"), "POST", callback_LongOPSAction);			
			}
		}
	}
}

//callback 함수
var callback_LastCursorAction = function(result) {
	if(result.result){
		$("#lastCursorDiv").html(strReplace(strReplace(result.txtValue,'\n','<br/>'),' ','&nbsp;'));
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_AllCursorAction = function(result) {
	json_string_callback_common(result,'#allCursorsList',false);
	$("#allCursorsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_SQLPerformanceAction = function(result) {
	json_string_callback_common(result,'#sqlList',false);
	$("#sqlList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_SQLHistoryAction = function(result) {
	json_string_callback_common(result,'#sessionHistoryList',false);
	$("#sessionHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_SQLGridPlanListAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#sqlGridPlanList').treegrid("loadData", data);
		$('#sqlGridPlanList').treegrid('loaded');
		$("#sqlGridPlanList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#sqlGridPlanList div.datagrid-cell.tree-node" ).css( "cursor", "default !important" );
	}
};

//callback 함수
var callback_SQLTextPlanListAction = function(result) {
	var strHtml = "";
	$("#textPlan li").remove();
	
	if(result.result){
		strHtml += "<li style='font-size:12px;'><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];		
			strHtml += "<li style='font-size:12px;'>"+strReplace(post.execution_plan,' ','&nbsp;')+"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#textPlan").append(strHtml);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_SessionKillScriptAction = function(result) {
	if(result.result){
		$("#sessionKillDiv").html(result.txtValue);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_ProcessKillScriptAction = function(result) {
	if(result.result){
		$("#processDiv").html(result.txtValue);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_ConnectInfoAction = function(result) {
	json_string_callback_common(result,'#connectInfoList',false);
	$("#connectInfoList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_EventAction = function(result) {
	json_string_callback_common(result,'#eventList',false);
	$("#eventList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_WaitAction = function(result) {
	json_string_callback_common(result,'#waitList',false);
	$("#waitList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_WaitClassAction = function(result) {
	json_string_callback_common(result,'#waitClassList',false);
	$("#waitClassList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_WaitHistoryAction = function(result) {
	json_string_callback_common(result,'#waitHistoryList',false);
	$("#waitHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_MetricAction = function(result) {
	json_string_callback_common(result,'#metricList',false);
	$("#metricList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_TimeModelAction = function(result) {
	json_string_callback_common(result,'#timeModelList',false);
	$("#timeModelList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_StatisticsAction = function(result) {
	json_string_callback_common(result,'#statisticsList',false);
	$("#statisticsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_IOAction = function(result) {
	json_string_callback_common(result,'#ioList',false);
	$("#ioList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_LongOPSAction = function(result) {
	json_string_callback_common(result,'#longOpsList',false);
	$("#longOpsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

function Btn_KillSessionClick(){

	if($("#sub_form #dbid").val() == "" || $("#sub_form #inst_id").val() == "" || $("#sub_form #sid").val() == "" || $("#sub_form #serial").val() == ""){
		parent.$.messager.alert('','해당 Row를 선택해 주세요.');
		return false;
	}

	var sid = $("#sub_form #sid").val();
	
	if(!confirm("SID = "+sid+" 세션을 강제 종료하시겠습니까?")){
		return;
	}
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("세션 모니터링 - Kill Session"," "); 
	
	ajaxCall("/SessionMonitoring/SessionKill",
			$("#sub_form"),
			"POST",
			callback_SessionKillAction);	
}

//callback 함수
var callback_SessionKillAction = function(result) {
	if(result.result){
		if(result.txtValue != "Complete"){
			parent.$.messager.alert('',result.txtValue);
		}
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		Btn_OnClick();
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Excel_DownClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#status").val($('#selectStatus').combobox('getValue'));
	
	$("#submit_form").attr("action","/SessionMonitoring/ExcelDown");
	$("#submit_form").submit();
}

// 값 변경
function changeValue(val, row) {
	if(val == "-1"){
		return "";
	}else{
		return val;
	}
}