$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	createList();
	
	// 프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// submit_form SQL점검팩 콤보
			$('#submit_form #sql_auto_perf_check_id').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onSelect: function(item) {
					// ASIS ORACLE VERSION
					$("#submit_form #asis_oracle_version_cd").combobox({
						url:"/AnalyzeDbChangeSqlImpact/getOracleVersionList",
						method:"get",
						valueField:'cd',
						textField:'cd_nm',
						onSelect: function(data) {
							// TOBE ORACLE VERSION
							$("#submit_form #tobe_oracle_version_cd").combobox({
								url:"/AnalyzeDbChangeSqlImpact/getOracleVersionList?cd="+data.cd,
								method:"get",
								valueField:'cd',
								textField:'cd_nm',
								onSelect: function(data) {
								},
								onLoadSuccess: function() {
									$("#submit_form #tobe_oracle_version_cd").combobox("textbox").attr("placeholder",'선택');
								},
								onLoadError: function() {
									parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
									return false;
								}
							});
						},
						onLoadSuccess: function() {
							$("#submit_form #asis_oracle_version_cd").combobox("textbox").attr("placeholder",'선택');
						},
						onLoadError: function() {
							parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
							return false;
						}
					});
					$("#submit_form #tobe_oracle_version_cd").combobox('clear');
				},
				onLoadSuccess: function() {
					$("#submit_form #sql_auto_perf_check_id").combobox("textbox").attr("placeholder",'선택');
					$('#submit_form #operation_dbid').combobox("setValue","");
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
			$("#submit_form #asis_oracle_version_cd").combobox('clear');
			$("#submit_form #tobe_oracle_version_cd").combobox('clear');
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
});

function createList() {
	var cnt = 0;
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'no',title:'NO',width:'2%',halign:'center',align:'center'},
			{field:'sql_id',title:'SQL ID',width:'8%',halign:'center',align:'left'},
			{field:'deprecated_yn',title:'미지원<br>힌트',width:'4%',halign:'center',align:'center',id:'colorId_1',formatter:formatYnChk},
			{field:'ora_01719_yn',title:'ORA<br>01719',width:'4%',halign:'center',align:'center',id:'colorId_2',formatter:formatYnChk},
			{field:'ora_00979_yn',title:'ORA<br>00979',width:'4%',halign:'center',align:'center',id:'colorId_3',formatter:formatYnChk},
			//{field:'order_by_yn',title:'ORDER<br>BY',width:'4%',halign:'center',align:'center',id:'colorId_4',formatter:formatYnChk},
			{field:'ora_30563_yn',title:'ORA<br>30563',width:'4%',halign:'center',align:'center',id:'colorId_5',formatter:formatYnChk},
			{field:'wm_concat_yn',title:'WM<br>CONCAT',width:'4%',halign:'center',align:'center',id:'colorId_6',formatter:formatYnChk},
			{field:'perf_impact_type_nm',title:'성능<br>임팩트유형',width:'6%',halign:'center',align:'left'},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'5%',halign:'center',align:'left'},
			{field:'asis_executions',title:'ASIS<br>EXECUTIONS',width:'6%',halign:'center',align:'right'},
			{field:'asis_elapsed_time',title:'ASIS<br>ELAPSED TIME',width:'7%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'tobe_elapsed_time',title:'TOBE<br>ELAPSED TIME',width:'7%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'asis_buffer_gets',title:'ASIS<br>BUFFER GETS',width:'7%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'tobe_buffer_gets',title:'TOBE<br>BUFFER GETS',width:'7%',halign:'center',align:'right',formatter:getNumberFormatNullChk},
			{field:'sql_text_web',title:'SQL TEXT',width:'20.5%',halign:'center',align:'left'},
			
			{field:'sql_text_excel',title:'SQL TEXT',hidden:'true'},
			{field:'sql_cnt',title:'전체 SQL',hidden:'true'},
			{field:'change_sql_cnt',title:'변경대상',hidden:'true'},
			{field:'deprecated_sql_cnt',title:'미지원힌트',hidden:'true'},
			{field:'ora_01719_cnt',title:'ORA-01719',hidden:'true'},
			{field:'ora_00979_cnt',title:'ORA-00979',hidden:'true'},
			//{field:'order_by_cnt',title:'ORDER<br>BY',hidden:'true'},
			
			{field:'bypass_ujvc_cnt',title:'BYPASS_UJVC',width:'6%',halign:'center',align:'right',styler:cellStyler},
			{field:'and_equal_cnt',title:'AND_EQUAL',width:'5%',halign:'center',align:'right',styler:cellStyler},
			{field:'merge_aj_cnt',title:'MERGE_AJ',width:'5%',halign:'center',align:'right',styler:cellStyler},
			{field:'hash_aj_cnt',title:'HASH_AJ',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'nl_aj_cnt',title:'NL_AJ',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'merge_sj_cnt',title:'MERGE_SJ',width:'5%',halign:'center',align:'right',styler:cellStyler},
			{field:'hash_sj_cnt',title:'HASH_SJ',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'nl_sj_cnt',title:'NL_SJ',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'ordered_predicates_cnt',title:'ORDERED_PREDICATES',width:'10%',halign:'center',align:'right',styler:cellStyler},
			{field:'rowid_cnt',title:'ROWID',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'star_cnt',title:'STAR',width:'4%',halign:'center',align:'right',styler:cellStyler},
			{field:'noparallel_cnt',title:'NOPARALLEL',width:'6%',halign:'center',align:'right',styler:cellStyler},
			{field:'noparallel_index_cnt',title:'NOPARALLEL_INDEX',width:'8%',halign:'center',align:'right',styler:cellStyler},
			{field:'norewrite_cnt',title:'NOREWRITE',width:'6%',halign:'center',align:'right',styler:cellStyler}
		]],
		onSelect:function( index, row ) {
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#colorId_1").css('background-color','#FACC2E');
	$("#colorId_2").css('background-color','#FACC2E');
	$("#colorId_3").css('background-color','#FACC2E');
	$("#colorId_4").css('background-color','#FACC2E');
	$("#colorId_5, #colorId_6").css('background-color','#FACC2E');
}

function cellStyler(value,row,index){
	if ( value > 0 ) {
		return 'background-color:#F7BE81;color:black;';
	}
}

function Btn_Search() {
	if ( !checkCondition() ) {
		return;
	}
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$(".totalSQLCnt").html("");
	$(".changeTarget").html("");
	$(".unsupportedHint").html("");
	$(".ora_01719").html("");
	$(".ora_00979").html("");
	//$(".addOrderBy").html("");
	
	$("#ora_30563").html("");
	$("#wmConcat").html("");
	
	$('#tableList').datagrid("loadData", []);
	
	ajaxCallTableList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("일괄검증 결과 조회"," ");
	
	/* DB변경 SQL 영향도 분석 리스트 */
	ajaxCall("/AnalyzeDbChangeSqlImpact/loadAnalyzeDbChangeSqlImpactList",
			$("#submit_form"),
			"POST",
			callback_LoadAnalyzeDbChangeSqlImpactListAction);
}

var callback_LoadAnalyzeDbChangeSqlImpactListAction = function(result) {
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	
	console.log(data.rows[0]);
	
	if ( data.totalCount != 0 ) {
		$(".totalSQLCnt").html( data.rows[0].sql_cnt );
		$(".changeTarget").html( data.rows[0].change_sql_cnt );
		$(".unsupportedHint").html( data.rows[0].deprecated_sql_cnt );
		$(".ora_01719").html( data.rows[0].ora_01719_cnt );
		$(".ora_00979").html( data.rows[0].ora_00979_cnt );
		//$(".addOrderBy").html( data.rows[0].order_by_cnt );
		
		$("#ora_30563").html( data.rows[0].ora_30563_cnt );
		$("#wmConcat").html( data.rows[0].wm_concat_cnt );
	}
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function checkCondition() {
	if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sql_auto_perf_check_id" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #asis_oracle_version_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','ASIS Oracle Version을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #tobe_oracle_version_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','TOBE Oracle Version을 선택해 주세요.','warning');
		return false;
	}
	
	return true;
}

//페이지처리
function formValidationCheck(){
	return true;
}
function fnSearch(){
	ajaxCallTableList();
}

function excelDownload(){
	if ( !checkCondition() ) {
		return;
	}
	
	$("#submit_form").attr("action","/AnalyzeDbChangeSqlImpact/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

