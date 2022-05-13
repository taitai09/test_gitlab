$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		columns:[[
			{field:'auto_choice_cond_no',title:'선정조건번호',halign:"center",align:'center',sortable:"true"},
			{field:'update_dt',title:'수정일시',halign:"center",align:'center',sortable:"true"},
			{field:'dbid',title:'DB',hidden:'true'},
			{field:'db_name',title:'DB',halign:"center",align:"center",sortable:"true"},			
			{field:'program_type_cd',title:'프로그램유형코드',hidden:'true'},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_auto_assign_yn',title:'자동할당여부',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_id',title:'튜닝담당자',hidden:'true'},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:'center',sortable:"true"},
			{field:'gather_cycle_div_cd',title:'수집주기코드',hidden:'true'},
			{field:'gather_cycle_div_nm',title:'수집주기',halign:"center",align:'center',sortable:"true"},
			{field:'gather_range_div_cd',title:'수집주기코드',hidden:'true'},
			{field:'gather_range_div_nm',title:'수집범위',halign:"center",align:'center',sortable:"true"},
			{field:'choice_start_day',title:'선정시작일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'choice_end_day',title:'선정종료일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'before_choice_sql_except_yn',title:'이전 선정 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'before_tuning_sql_except_yn',title:'이전 튜닝 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'elap_time',title:'Elapsed Time(sec)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'buffer_cnt',title:'Buffer Gets',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'exec_cnt',title:'Executions',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'topn_cnt',title:'TOP N',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'order_div_cd',title:'ORDERED코드',hidden:'true'},
			{field:'order_div_nm',title:'Ordered',halign:"center",align:'center',sortable:"true"},
			{field:'module1',title:'Module명 1',halign:"center",align:'left',sortable:"true"},
			{field:'module2',title:'Module명 2',halign:"center",align:'left',sortable:"true"},
			{field:'parsing_schema_name',title:'Parsing Schema Name',halign:"center",align:'center',sortable:"true"},
			{field:'sql_text',title:'SQL TEXT',width:"300px",halign:"center",align:'left',sortable:"true"},
			{field:'appl_filter_yn',title:'애플리케이션 필터 여부',halign:"center",align:'center',sortable:"true"},
			{field:'use_yn',title:'사용 여부',halign:"center",align:'center',sortable:"true"},
			{field:'del_yn',title:'삭제 여부',halign:"center",align:'center',sortable:"true"},
			{field:'choicer_nm',title:'선정자',halign:"center",align:'center',sortable:"true"},
			{field:'choice_dt',title:'선정일시',halign:"center",align:'center',sortable:"true"},
			{field:'update_nm',title:'수정자',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("자동선정 이력"," "); 

	ajaxCall("/AutoSelectionHistoryAction",
			$("#submit_form"),
			"POST",
			callback_AutoSelectionHistoryAction);	
});

//callback 함수
var callback_AutoSelectionHistoryAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
};