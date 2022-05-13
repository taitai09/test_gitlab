$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onSelect:function(rec) {
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$('#dbid').val(rec.dbid);
			
			loadSelectAutoChoiceCondNoCombobox(rec.dbid);
			
			// 튜닝 담당자 조회
			$('#selectTuner').combobox({
				url:"/Common/getTunerCondition?dbid="+rec.dbid+"&isAll=Y",
				method:"get",
				valueField:'tuner_id',
				textField:'tuner_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
			});
		},
		onLoadSuccess: function(items) {
			if($('#dbid').val() != null || $('#dbid').val() != '') {
				$(this).combobox('setValue', $('#dbid').val());
				$(this).combobox('textbox').attr('placeholder','선택');
				loadSelectAutoChoiceCondNoCombobox($('#dbid').val());
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#selectProject').combobox({
		url:"/Common/getProject?isAll=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		}
	});
	 
	$('#strStartDt').datebox({
		onChange : function(){
		}
	});	
	$('#strEndDt').datebox({
		onChange : function(){
		}
	});	
	
	
	$("#tableList").datagrid({
		view: myview,
		nowarp: true,
		singleSelect : true,
		checkOnSelect : false,
		multiOnSelect : false,
		selectOnCheck : false,
		onClickRow : function(index,row) {
			$("#choice_tms").val(row.choice_tms);
			$("#auto_choice_cond_no").val(row.auto_choice_cond_no);
			
			showDetailTable(row);
		},
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true", rowspan:2},
			{field:'dbid',title:'DB',hidden:"true", rowspan:2},
			{field:'db_name',title:'DB',halign:"center",align:"center",sortable:"true", rowspan:2},
			{field:'auto_choice_cond_no',title:'선정조건번호',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_tms',title:'선정회차',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_dt',title:'선정일자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_cnt',title:'선정건수',halign:"center",align:'right',sortable:"true", rowspan:2},
			{title:'선정조건',halign:"center",colspan:9},
			{field:'project_nm',title:'프로젝트',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'project_id',hidden:'true', rowspan:2},
			{field:'tuning_prgrs_step_seq',hidden:'true', rowspan:2}
		],[		   
			{field:'gather_cycle_div_nm',title:'수집주기',halign:"center",align:'center',sortable:"true"},
			{field:'gather_range_div_nm',title:'수집범위',halign:"center",align:'center',sortable:"true"},
			{field:'before_choice_sql_except_yn',title:'이전 선정 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'before_tuning_sql_except_yn',title:'이전 튜닝 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'elap_time',title:'Elapsed Time(sec)',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_cnt',title:'Buffer Gets',halign:"center",align:'right',sortable:"true"},
			{field:'exec_cnt',title:'Executions',halign:"center",align:'right',sortable:"true"},
			{field:'topn_cnt',title:'TOP N',halign:"center",align:'right',sortable:"true"},
			{field:'order_div_nm',title:'Ordered',halign:"center",align:'center',sortable:"true"}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#dtlTableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:"center",sortable:"true"},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:"center",sortable:"true"},
			{field:'tuning_status_cd',hidden:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',halign:"center",align:'center',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true"},
			{field:'avg_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_disk_reads',title:'DISK_READS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_row_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'ratio_buffer_get_total',title:'RATIO_BUFFER_GET_TOTAL',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'ratio_cpu_total',title:'RATIO_CPU_TOTAL',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'ratio_cpu_per_executions',title:'RATIO_CPU_PER_EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"300px",halign:"center",align:'left',sortable:"true"}
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
	
});

function loadSelectAutoChoiceCondNoCombobox(dbid) {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("선정조건번호"," "); 
	
	$('#select_auto_choice_cond_no').combobox({
		url:"/AutoSelection/getChoiceCondNo?dbid=" + dbid+"&isAll=Y",
		method:"get",
		valueField:'auto_choice_cond_no',
		textField:'auto_choice_cond_no',
		onLoadSuccess : function(items){
			/* modal progress close */
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			if($('#auto_choice_cond_no').val() != null && $('#auto_choice_cond_no').val() != '') {
				$(this).combobox("setValue", $('#auto_choice_cond_no').val());
				
//				Btn_OnClick();
			} else {
				if(items.length > 0) {
					$(this).combobox("setValue", items[0].auto_choice_cond_no);
//					Btn_OnClick();
				}
			}
		},
		onLoadError: function(){
			/* modal progress close */
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
}

function ajaxAutoSelectionStatus(){
	$('#dtlTableList').datagrid('loadData',[]);
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("자동 선정 현황(검색)"," "); 
	
	ajaxCall("/AutoSelectionStatusAction",
			$("#submit_form"),
			"POST",
			callback_AutoSelectionStatusAddTable);	
}

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		$.messager.alert('','시작 선정일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		$.messager.alert('','종료 선정일자를 선택해 주세요.');
		return false;
	}	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$('#project_id').val($('#selectProject').combobox('getValue'));
	$('#perfr_id').val($('#selectTuner').combobox('getValue'));

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	
//	$("#submit_form").attr("action","/AutoSelectionStatus");
//	$("#submit_form").submit();
	ajaxAutoSelectionStatus();	
}

//callback 함수
var callback_AutoSelectionStatusAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function showDetailTable(selRow){
	$('#dtlTableList').datagrid('loadData',[]);
	$('#dtlTableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dtlTableList').datagrid('loading'); 
//	$("#auto_choice_cond_no").val(selRow.auto_choice_cond_no);

	ajaxCall("/AutoSelectionStatus/Detail",
		$("#submit_form"),
		"POST",
		callback_AutoSelectionDetailAddTable);	
}

//callback 함수
var callback_AutoSelectionDetailAddTable = function(result) {
	json_string_callback_common(result,'#dtlTableList',false);
};

function endBatchTuningBundle(){
	var choiceTmsArray = "";
	var autoChoiceCondNoArray = "";
	var rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length > 0){
		$('#batchTuningBundlePop').window("open");
		$('#batchTuningBundle_form #autoChoiceCondNoArray').val("");
		$('#batchTuningBundle_form #choiceTmsArray').val("");
		$('#batchTuningBundle_form #tuning_end_why').val("");
		
		for(var i = 0 ; i < rows.length ; i++){
			autoChoiceCondNoArray += rows[i].auto_choice_cond_no + "|";
			choiceTmsArray += rows[i].choice_tms + "|";
		}
		
		$("#batchTuningBundle_form #autoChoiceCondNoArray").val(strRight(autoChoiceCondNoArray,1));
		$("#batchTuningBundle_form #choiceTmsArray").val(strRight(choiceTmsArray,1));
		
		// 튜닝종료사유 조회
		$('#selectTuningEnd').combobox({
			url:"/Common/getCommonCode?grp_cd_id=1009",
			method:"get",
			valueField:'cd',
			textField:'cd_nm'
		});
	}else{
		parent.$.messager.alert('','선택된 선정조건번호가 없습니다.');
	}
}

function Excel_Download(gubun){
	var dbid = $('#submit_form #selectCombo').combobox('getValue');
	if(dbid == ""){
		parent.$.messager.alert('','DB를 선택하여 주세요.');
		return false;
	};
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));

	var url = "";
	if(gubun == 'M'){
		url = "/AutoSelectionStatusSearch/ExcelDown";
	}else if(gubun == 'S'){
		url = "/AutoSelectionStatusSearchDetail/ExcelDown";
	}

	$("#submit_form").attr("action",url);
	$("#submit_form").submit();
}