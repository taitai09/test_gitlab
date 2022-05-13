function fnCreateDatagrid(){
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		fitColumn:true,
		onClickCell : function(index,field,value) {
			if(field == "exception_processing_yn") return;

			var row = $(this).datagrid('getRows')[index];
			
			$("#submit_form #program_id").val(row.program_id);
			$("#submit_form #hidden_program_execute_tms").val(row.program_execute_tms);
			$("#hidden_program_execute_tms").html(row.program_execute_tms);
			
			$("#detailCheckResultTable tbody").html("");			
			$("#span_program_execute_tms").text("");			
//			$("#td_perf_check_result_basis_why1").text("");			
			$("#ta_perf_check_result_basis_why1").val("");			
			$("#ta_perf_impr_guide").val("");			
			$("#ta_exec_plan").val("");			

			//바인드검색 팝업창이 열린 상태로 그리드를 클릭하였을 경우 바인드검색 팝업창 닫기
			Btn_OnClosePopup('bindSearchDialog');
			//바인드검색 팝업창이 열린 상태로 그리드를 클릭하였을 경우 바인드검색 아랫쪽 테이블 초기화
			$('#bindSearchList2Table tbody').html("");
			$('#bindSearchList4Table tbody').html("");

			if(row.perf_check_result_div_nm == "점검제외"){
				$("#auto_skip").val("Y");
				$('#perfChkResultTabs').tabs('disableTab', 2);
				$('#perfChkResultTabs').tabs('disableTab', 3);
				$("#perfChkResultTabs").tabs('select',0);
			}else{
				$("#auto_skip").val("N");
				// get the selected tab panel and its tab object
				var pp = $('#perfChkResultTabs').tabs('getSelected');
				var tab = pp.panel('options').tab;    // the corresponding tab object   				
				var idx = pp.panel('options').index;    // the corresponding tab object
				if(perf_test_complete_yn == 'Y'){
					$('#perfChkResultTabs').tabs('disableTab', 2);  // the tab panel index start with 0
					$('#perfChkResultTabs').tabs('enableTab', 3);
					if(idx != 3){
						$("#perfChkResultTabs").tabs('select',3);
					}else{
						selectedIndex(3);
					}
				}else{
					$('#perfChkResultTabs').tabs('enableTab', 2);
					$('#perfChkResultTabs').tabs('enableTab', 3);				
					if(idx != 2){
						$("#perfChkResultTabs").tabs('select',2);
					}else{
						selectedIndex(2);
					}
				}
			}
			console.log("onClickCell finished");
		},		
		onClickRow : function(index,row) {
		},
		columns:[[
//			{field:'perf_check_id',title:'성능점검ID',halign:"center",align:'center',sortable:"true"},
//			{field:'program_id',title:'프로그램ID',halign:"center",align:'center',sortable:"true"},
//			{field:'program_nm',title:'프로그램명',halign:"center",align:'center',sortable:"true"},
//			{field:'dbio',title:'SQL식별자(DBIO)',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
//			{field:'parsing_schema_name',title:'파싱스키마',halign:"center",align:'center'},
//			{field:'program_dvlp_div_nm',title:'개발구분',halign:"center",align:'center',sortable:"true"},
//			{field:'perf_check_result_div_nm',title:'점검결과',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
//			{field:'program_execute_tms',title:'수행회차',halign:"center",align:'center',sortable:"true"},
//			{field:'program_executer_nm',title:'수행자',halign:"center",align:'center',sortable:"true"},
//			{field:'program_exec_dt',title:'수행일시',halign:"center",align:'center',sortable:"true"},
////			{field:'program_exec_div_nm',title:'수행유형',halign:"center",align:'center',sortable:"true"},
//			{field:'file_nm',title:'파일명',halign:"center",align:'center',sortable:"true"},
//			{field:'dir_nm',title:'디렉토리',halign:"center",align:'center',sortable:"true"},
//			{field:'exception_processing_yn',width:'100px',title:'예외요청',align:'center',formatter:formatBtn}			
			{field:'perf_check_id',title:'성능점검ID',halign:"center",align:'center',sortable:"true"},
			{field:'program_id',title:'프로그램ID',halign:"center",align:'center',sortable:"true"},
			{field:'program_nm',title:'프로그램명',halign:"center",align:'center',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'parsing_schema_name',title:'파싱스키마',halign:"center",align:'center'},
			{field:'program_dvlp_div_nm',title:'개발구분',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_result_div_nm',title:'점검결과',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
			{field:'program_execute_tms',title:'수행회차',halign:"center",align:'center',sortable:"true"},
			{field:'program_executer_nm',title:'수행자',halign:"center",align:'center',sortable:"true"},
			{field:'program_exec_dt',title:'수행일시',halign:"center",align:'center',sortable:"true"},
//			{field:'program_exec_div_nm',title:'수행유형',halign:"center",align:'center',sortable:"true"},
			{field:'file_nm',title:'파일명',halign:"center",align:'center',sortable:"true"},
			{field:'dir_nm',title:'디렉토리',halign:"center",align:'center',sortable:"true"},
			{field:'exception_processing_yn',width:'100px',title:'예외요청',align:'center',formatter:formatBtn}			
		]],		
		onLoadSuccess:function(){
			$(this).datagrid('getPanel').find('a.easyui-linkbutton').linkbutton();
			
//			var rows=$(this).datagrid("getRows");
//			for(var i=0;i<rows.length;i++){
//				console.log("rows:",rows[i]);
//				var row = rows[i];
//				if(row.program_id == program_id){
//					
//				}
//			}
			$(this).datagrid('highlightRow', 0);
			$(this).datagrid('selectRow', 0);
			$(this).datagrid('checkRow', 0);
			$(this).datagrid('resize');
			$(this).datagrid('resize');
		},
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
}

$(document).ready(function() {
	
    fnCreateDatagrid();
	
	// 수행회차
	$('#program_execute_tms').combobox('setValue','01');
		
	$('#tableList').datagrid("loadData", []);
	
	fnSearch();
	

});

