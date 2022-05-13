$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	fnCreateDbidCombobox();	

	$("#strStartDt").datebox({
		onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			
			// 점검회차 설정
			setCheckSeq(selDate);
		}
	});
	
	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());

	$("#sqlTuningAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'rnum',title:'NO',width:"8%",halign:"center",align:"center"},
			{field:'recommendation_report',title:'RECOMMENDATION_REPORT',width:"92%",halign:"center",align:"left",formatter:setTextArea}			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#sgaAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'inst_id',title:'INIT_ID',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'sga_size',title:'SGA_SIZE',width:"18%",halign:"center",align:"right",sortable:"true"},
//			{field:'sga_size_factor',title:'SGA_SIZE_FACTOR',width:"18%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_db_time',title:'ESTD_DB_TIME',width:"18%",halign:"center",align:"right",sortable:"true"},			
//			{field:'estd_db_time_factor',title:'ESTD_DB_TIME_FACTOR',width:"18%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_physical_reads',title:'ESTD_PHYSICAL_READS',width:"18%",halign:"center",align:"right",sortable:"true"}
			{field:'inst_id',title:'INIT_ID',width:"65",halign:"center",align:"center",sortable:"true"},
			{field:'sga_size',title:'SGA_SIZE',halign:"center",align:"right",sortable:"true"},
			{field:'sga_size_factor',title:'SGA_SIZE_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_db_time',title:'ESTD_DB_TIME',halign:"center",align:"right",sortable:"true"},			
			{field:'estd_db_time_factor',title:'ESTD_DB_TIME_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_physical_reads',title:'ESTD_PHYSICAL_READS',halign:"center",align:"right",sortable:"true"}
		]],

		rowStyler: function(index,row){
			if(row.sga_size_factor == 1){
				return 'background-color:#ffee00;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#buffercacheAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'inst_id',title:'INIT_ID',width:"7%",halign:"center",align:"center",sortable:"true"},
//			{field:'id',title:'ID',width:"5%",halign:"center",align:"center",sortable:"true"},
//			{field:'name',title:'NAME',width:"7%",halign:"center",align:"center",sortable:"true"},
//			{field:'block_size',title:'BLOCK_SIZE',width:"8%",halign:"center",align:"right",sortable:"true"},			
//			{field:'size_for_estimate',title:'SIZE_FOR_ESTIMATE',width:"10%",halign:"center",align:"right",sortable:"true"},
//			{field:'size_factor',title:'SIZE_FACTOR',width:"8%",halign:"center",align:"right",sortable:"true"},
//			{field:'buffers_for_estimate',title:'BUFFERS_FOR_ESTIMATE',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_physical_read_factor',title:'ESTD_PHYSICAL_READ_FACTOR',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_physical_reads',title:'ESTD_PHYSICAL_READS',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_cluster_read_time',title:'ESTD_CLUSTER_READ_TIME',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_physical_read_time',title:'ESTD_PHYSICAL_READ_TIME',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_pct_of_db_time_for_reads',title:'ESTD_PCT_OF_DB_TIME_FOR_READS',width:"16%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_cluster_reads',title:'ESTD_CLUSTER_READS',width:"11%",halign:"center",align:"right",sortable:"true"}
			{field:'inst_id',title:'INIT_ID',width:"65",halign:"center",align:"center",sortable:"true"},
			{field:'id',title:'ID',halign:"center",align:"center",sortable:"true"},
			{field:'name',title:'NAME',halign:"center",align:"center",sortable:"true"},
			{field:'block_size',title:'BLOCK_SIZE',halign:"center",align:"right",sortable:"true"},			
			{field:'size_for_estimate',title:'SIZE_FOR_ESTIMATE',halign:"center",align:"right",sortable:"true"},
			{field:'size_factor',title:'SIZE_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'buffers_for_estimate',title:'BUFFERS_FOR_ESTIMATE',halign:"center",align:"right",sortable:"true"},
			{field:'estd_physical_read_factor',title:'ESTD_PHYSICAL_READ_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_physical_reads',title:'ESTD_PHYSICAL_READS',halign:"center",align:"right",sortable:"true"},
			{field:'estd_cluster_read_time',title:'ESTD_CLUSTER_READ_TIME',halign:"center",align:"right",sortable:"true"},
			{field:'estd_physical_read_time',title:'ESTD_PHYSICAL_READ_TIME',halign:"center",align:"right",sortable:"true"},
			{field:'estd_pct_of_db_time_for_reads',title:'ESTD_PCT_OF_DB_TIME_FOR_READS',halign:"center",align:"right",sortable:"true"},
			{field:'estd_cluster_reads',title:'ESTD_CLUSTER_READS',halign:"center",align:"right",sortable:"true"}
		]],

		rowStyler: function(index,row){
			if(row.size_factor == 1){
				return 'background-color:#ffee00;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#sharedPoolAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'inst_id',title:'INIT_ID',width:"6%",halign:"center",align:"center",sortable:"true"},
//			{field:'shared_pool_size_for_estimate',title:'SHARED_POOL_SIZE_FOR_ESTIMATE',width:"16%",halign:"center",align:"right",sortable:"true"},
//			{field:'shared_pool_size_factor',title:'SHARED_POOL_SIZE_FACTOR',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_size',title:'ESTD_LC_SIZE',width:"8%",halign:"center",align:"right",sortable:"true"},			
//			{field:'estd_lc_memory_objects',title:'ESTD_LC_MEMORY_OBJECTS',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_time_saved',title:'ESTD_LC_TIME_SAVED',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_time_saved_factor',title:'ESTD_LC_TIME_SAVED_FACTOR',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_load_time',title:'ESTD_LC_LOAD_TIME',width:"11%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_load_time_factor',title:'ESTD_LOAD_TIME_FACTOR',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_lc_memory_object_hits',title:'ESTD_LC_MEMORY_OBJECT_HITS',width:"15%",halign:"center",align:"right",sortable:"true"}
			{field:'inst_id',title:'INIT_ID',width:"65",halign:"center",align:"center",sortable:"true"},
			{field:'shared_pool_size_for_estimate',title:'SHARED_POOL_SIZE_FOR_ESTIMATE',halign:"center",align:"right",sortable:"true"},
			{field:'shared_pool_size_factor',title:'SHARED_POOL_SIZE_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_size',title:'ESTD_LC_SIZE',halign:"center",align:"right",sortable:"true"},			
			{field:'estd_lc_memory_objects',title:'ESTD_LC_MEMORY_OBJECTS',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_time_saved',title:'ESTD_LC_TIME_SAVED',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_time_saved_factor',title:'ESTD_LC_TIME_SAVED_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_load_time',title:'ESTD_LC_LOAD_TIME',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_load_time_factor',title:'ESTD_LOAD_TIME_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'estd_lc_memory_object_hits',title:'ESTD_LC_MEMORY_OBJECT_HITS',halign:"center",align:"right",sortable:"true"}
		]],

		rowStyler: function(index,row){
			if(row.shared_pool_size_factor == 1){
				return 'background-color:#ffee00;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#pgaAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'inst_id',title:'INIT_ID',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'pga_target_for_estimate',title:'PGA_TARGET_FOR_ESTIMATE',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'pga_target_factor',title:'PGA_TARGET_FACTOR',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'bytes_processed',title:'BYTES_PROCESSED',width:"10%",halign:"center",align:"right",sortable:"true"},			
//			{field:'estd_time',title:'ESTD_TIME',width:"10%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_extra_bytes_rw',title:'ESTD_EXTRA_BYTES_RW',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_pga_cache_hit_percentage',title:'ESTD_PGA_CACHE_HIT_PERCENTAGE',width:"15%",halign:"center",align:"right",sortable:"true"},
//			{field:'estd_overalloc_count',title:'ESTD_OVERALLOC_COUNT',width:"15%",halign:"center",align:"right",sortable:"true"}
			{field:'inst_id',title:'INIT_ID',width:"65",halign:"center",align:"center",sortable:"true"},
			{field:'pga_target_for_estimate',title:'PGA_TARGET_FOR_ESTIMATE',halign:"center",align:"right",sortable:"true"},
			{field:'pga_target_factor',title:'PGA_TARGET_FACTOR',halign:"center",align:"right",sortable:"true"},
			{field:'bytes_processed',title:'BYTES_PROCESSED',halign:"center",align:"right",sortable:"true"},			
			{field:'estd_time',title:'ESTD_TIME',halign:"center",align:"right",sortable:"true"},
			{field:'estd_extra_bytes_rw',title:'ESTD_EXTRA_BYTES_RW',halign:"center",align:"right",sortable:"true"},
			{field:'estd_pga_cache_hit_percentage',title:'ESTD_PGA_CACHE_HIT_PERCENTAGE',halign:"center",align:"right",sortable:"true"},
			{field:'estd_overalloc_count',title:'ESTD_OVERALLOC_COUNT',halign:"center",align:"right",sortable:"true"}
		]],

		rowStyler: function(index,row){
			if(row.pga_target_factor == 1){
				return 'background-color:#ffee00;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$("#segmentAdvisorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'task_name',title:'TASK_NAME',width:"12%",halign:"center",align:"center",sortable:"true"},
//			{field:'task_start_dt',title:'TASK_START_DT',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'segment_type',title:'SEGMENT_TYPE',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'owner',title:'OWNER',width:"12%",halign:"center",align:"right",sortable:"true"},			
//			{field:'segment_name',title:'SEGMENT_NAME',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'partition_name',title:'PARTITION_NAME',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'message',title:'MESSAGE',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'more_info',title:'MORE_INFO',width:"12%",halign:"center",align:"right",sortable:"true"}
			{field:'task_name',title:'TASK_NAME',width:"250",halign:"center",align:"center",sortable:"true"},
			{field:'task_start_dt',title:'TASK_START_DT',halign:"center",align:"right",sortable:"true"},
			{field:'segment_type',title:'SEGMENT_TYPE',halign:"center",align:"right",sortable:"true"},
			{field:'owner',title:'OWNER',halign:"center",align:"right",sortable:"true"},			
			{field:'segment_name',title:'SEGMENT_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'message',title:'MESSAGE',halign:"center",align:"left",sortable:"true"},
			{field:'more_info',title:'MORE_INFO',halign:"center",align:"left",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#healthMonitorTable").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
//			{field:'inst_id',title:'INST_ID',width:"12%",halign:"center",align:"center",sortable:"true"},
//			{field:'recommendation_id',title:'RECOMMENDATION_ID',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'name',title:'NAME',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'type',title:'TYPE',width:"12%",halign:"center",align:"right",sortable:"true"},			
//			{field:'status',title:'STATUS',width:"12%",halign:"center",align:"right",sortable:"true"},
//			{field:'description',title:'DESCRIPTION',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'repair_script',title:'REPAIR_SCRIPT',width:"13%",halign:"center",align:"right",sortable:"true"},
//			{field:'time_detected',title:'TIME_DETECTED',width:"13%",halign:"center",align:"right",sortable:"true"}
			{field:'inst_id',title:'INST_ID',width:"65",halign:"center",align:"center",sortable:"true"},
			{field:'recommendation_id',title:'RECOMMENDATION_ID',halign:"center",align:"right",sortable:"true"},
			{field:'name',title:'NAME',halign:"center",align:"right",sortable:"true"},
			{field:'type',title:'TYPE',halign:"center",align:"right",sortable:"true"},			
			{field:'status',title:'STATUS',halign:"center",align:"right",sortable:"true"},
			{field:'description',title:'DESCRIPTION',halign:"center",align:"right",sortable:"true"},
			{field:'repair_script',title:'REPAIR_SCRIPT',halign:"center",align:"right",sortable:"true"},
			{field:'time_detected',title:'TIME_DETECTED',halign:"center",align:"right",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$('#selectDbid').combobox("setValue",$("#dbid").val());
	$('#selectCheckSeq').combobox("setValue",$("#check_seq").val());	
	Btn_OnClick();
});

function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - ADVISOR RECOMMENDATION"," "); 
	
	$('#sqlTuningAdvisorTable').datagrid("loadData", []);
	$('#sgaAdvisorTable').datagrid("loadData", []);
	$('#buffercacheAdvisorTable').datagrid("loadData", []);
	$('#sharedPoolAdvisorTable').datagrid("loadData", []);
	$('#pgaAdvisorTable').datagrid("loadData", []);
	$('#segmentAdvisorTable').datagrid("loadData", []);
	$('#healthMonitorTable').datagrid("loadData", []);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/SQLTuningAdvisor",
			$("#submit_form"),
			"POST",
			callback_SQLTuningAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/SgaAdvisor",
			$("#submit_form"),
			"POST",
			callback_SgaAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/BufferCacheAdvisor",
			$("#submit_form"),
			"POST",
			callback_BufferCacheAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/SharedPoolAdvisor",
			$("#submit_form"),
			"POST",
			callback_SharedPoolAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/PgaAdvisor",
			$("#submit_form"),
			"POST",
			callback_PgaAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/SegmentAdvisor",
			$("#submit_form"),
			"POST",
			callback_SegmentAdvisorAction);
	
	ajaxCall("/PreventiveCheck/AdvisorRecommendations/HealthMonitor",
			$("#submit_form"),
			"POST",
			callback_HealthMonitorAction);
}

//callback 함수
var callback_SQLTuningAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#sqlTuningAdvisorTable').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_SgaAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#sgaAdvisorTable').datagrid("loadData", data);
};

//callback 함수
var callback_BufferCacheAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#buffercacheAdvisorTable').datagrid("loadData", data);
};

//callback 함수
var callback_SharedPoolAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#sharedPoolAdvisorTable').datagrid("loadData", data);
};

//callback 함수
var callback_PgaAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#pgaAdvisorTable').datagrid("loadData", data);
};

//callback 함수
var callback_SegmentAdvisorAction = function(result) {
	var data = JSON.parse(result);
	$('#segmentAdvisorTable').datagrid("loadData", data);
};

//callback 함수
var callback_HealthMonitorAction = function(result) {
	var data = JSON.parse(result);
	$('#healthMonitorTable').datagrid("loadData", data);
};
function setTextArea(val, row) {
	return "<textarea rows=10 cols=500 name='recommend"+row.rnum+"' id='recommend"+row.rnum+"' style='font-family:\"굴림체\";font-size:12px;margin-top:5px;padding:10px;width:99%;height:220px;' wrap='soft'>"+val+"</textarea>";
}