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
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#choice_tms").val(row.choice_tms);
			showDetailTable();
		},			
		columns:[[
			{field:'dbid',title:'DB',hidden:"true", rowspan:2},
			{field:'db_name',title:'DB',halign:"center",align:"center",sortable:"true", rowspan:2},
			{field:'choice_tms',title:'선정회차',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choicer_nm',title:'선정자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_dt',title:'선정일자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_cnt',title:'선정건수',halign:"center",align:'center',sortable:"true", rowspan:2},
			{title:'선정조건',halign:"center",colspan:15}
		],[		   
			{field:'start_snap_id',title:'시작 SNAP ID',halign:"center",align:'center',sortable:"true"},
			{field:'end_snap_id',title:'종료 SNAP ID',halign:"center",align:'center',sortable:"true"},
			{field:'before_choice_sql_except_yn',title:'이전선정SQL제외',halign:"center",align:'center',sortable:"true"},
			{field:'before_tuning_sql_except_yn',title:'이전튜닝SQL제외',halign:"center",align:'center',sortable:"true"},
			{field:'elap_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_cnt',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'exec_cnt',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'topn_cnt',title:'TOP N',halign:"center",align:'right',sortable:"true"},
			{field:'order_div_nm',title:'ORDERED',halign:"center",align:'center',sortable:"true"},
			{field:'module1',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"},
			{field:'excpt_module_list',title:'EXCPT_MODULE_LIST',halign:"center",align:'left',sortable:"true"},
			{field:'excpt_parsing_schema_name_list',title:'EXCPT_PARSING_SCHEMA_NAME_LIST',halign:"center",align:'left',sortable:"true"},
			{field:'excpt_sql_id_list',title:'EXCPT_SQL_ID_LIST',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dtlTableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:"center",sortable:"true"},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:"center",sortable:"true"},
			{field:'tuning_status_cd',hidden:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',halign:"center",align:'center',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true"},
			{field:'avg_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'avg_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'avg_disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'avg_row_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true"},			
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'ratio_buffer_get_total',title:'RATIO BUFFER GET TOTAL',halign:"center",align:'right',sortable:"true"},
			{field:'ratio_cpu_total',title:'RATIO CPU TOTAL',halign:"center",align:'right',sortable:"true"},
			{field:'ratio_cpu_per_executions',title:'RATIO CPU PER EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL TEXT',width:"300px",halign:"center",align:'left',sortable:"true"}
		]],
		onLoadSuccess: function(data){
			var opts = $(this).datagrid('options');
			for(var i = 0; i < data.rows.length ; i++){
				var row = data.rows[i];
				if(row.tuning_status_cd != "3"){
					var tr = opts.finder.getTr(this,i);
					tr.find('input[type=checkbox]').attr('disabled','disabled');					
				}
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
	ajaxManualSelectionStatus();
});

function ajaxManualSelectionStatus(){
	$('#dtlTableList').datagrid('loadData',[]);
	$('#tableList').datagrid('loadData',[]);
	
	$('#selectCombo').combobox('setValue',$("#dbid").val());
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수동선정 현황"," "); 
	
	ajaxCall("/ManualSelectionStatusAction",
			$("#submit_form"),
			"POST",
			callback_ManualSelectionStatusAddTable);	
	
}

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 선정일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 선정일자를 선택해 주세요.');
		return false;
	}	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
//	$("#submit_form").attr("action","/ManualSelectionStatus");
//	$("#submit_form").submit();
	ajaxManualSelectionStatus();
}

//callback 함수
var callback_ManualSelectionStatusAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function showDetailTable(){
	$('#dtlTableList').datagrid('loadData',[]);
	$('#dtlTableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dtlTableList').datagrid('loading'); 
	
	ajaxCall("/ManualSelectionStatus/Detail",
		$("#submit_form"),
		"POST",
		callback_ManualSelectionDetailAddTable);	
}

//callback 함수
var callback_ManualSelectionDetailAddTable = function(result) {
	json_string_callback_common(result,'#dtlTableList',false);
};

function endBatchTuning(){
	var tuningNoArry = "";
	rows = $('#dtlTableList').datagrid('getChecked');
	
	if(rows.length > 0){
		$('#batchTuningPop').window("open");	
		$('#batchTuning_form #tuningNoArry').val("");
		$("#batchTuning_form #tuning_end_why").val("");
		
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";
		}	
		
		$("#batchTuning_form #tuningNoArry").val(strRight(tuningNoArry,1));		
		
		// 튜닝종료사유 조회			
		$('#selectTuningEnd').combobox({
		    url:"/Common/getCommonCode?grp_cd_id=1009",
		    method:"get",
			valueField:'cd',
		    textField:'cd_nm'
		});
	}else{
		parent.$.messager.alert('','일괄튜닝종료할 행을 선택해 주세요.');
	}
}

function Btn_GoList(){
//	location.href="/ManualSelection?menu_id="+$("#menu_id").val();
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ManualSelection");
	$("#submit_form").submit();
}