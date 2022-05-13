var searchFlag = false;
var clickFlag = false;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createList();
	
	//소스DB 선택 시 실행문
	$("#original_dbid").combobox({
		url:"/AnalyzeImpactChangedTable/getOperationDB?db_operate_type_cd=&database_kinds_cd="+$('#database_kinds_cd').val(),
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight: 300,
		onChange: function(newValue, oldValue) {
			$('#sqlPerformanceP').combobox('clear');
			$("#sqlPerformanceP").combobox("textbox").attr("placeholder",'전체');
			
			if ( newValue == '' ) {
				return; 
			}
			
			var item = $.grep($(this).combobox('getData'), function(row) {
				if ( row.original_dbid == newValue ) {
					return true;
				} else {
					return false;
				}
			});
			
			if(newValue != oldValue){
				searchFlag = false;
				clickFlag = false;
			}
		},
		onLoadError: function() {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onLoadSuccess: function(items) {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			$("#original_dbid").combobox("textbox").attr("placeholder",'선택');
		}
	});
	
	// SQL점검팩 combobox 클릭시 데이터 불러오기
	$('#sqlPerformanceP').siblings('span').on('click', function(){
		$('#sqlPerformanceP').combobox('clear');
		
		beginDate = encodeURIComponent( $('#perf_check_exec_begin_dt').datebox('getText') );
		endDate = encodeURIComponent( $('#perf_check_exec_end_dt').datebox('getText') );
		
		if ( $("#original_dbid").textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
			return false;
			
		}else if ( checkConditionDate() == false) {
			return;
			
		}else {
			let original_dbid = encodeURIComponent( $("#original_dbid").textbox("getValue") );
			
			$('#sqlPerformanceP').combobox({
				url:"/AnalyzeImpactChangedTable/getSqlPerfPacName?perf_check_exec_begin_dt="
					+ beginDate + "&perf_check_exec_end_dt=" + endDate + "&original_dbid=" + original_dbid
					+ "&database_kinds_cd=" + $('#database_kinds_cd').val()
					+ "&condition=1",
					method:"get",
					valueField:'sql_auto_perf_check_id',
					textField:'perf_check_name',
					panelHeight: 300,
					onHidePanel: function() {
						$(".tooltip ").hide();
					},
					onSelect: function(data) {
						if ( data.sql_auto_perf_check_id == null) {
							$('#sqlPerformanceP').combobox("setValue",'');
							
						} else {
							$('#sql_auto_perf_check_id').val(data.sql_auto_perf_check_id);
						}
					},
					onChange: function(newValue, oldValue){
						if(newValue != oldValue){
							searchFlag = false;
							clickFlag = false;
						}
					},
					onLoadSuccess: function(item) {
						$('#sqlPerformanceP').combobox('textbox').attr('placeholder','전체');
					},
					onLoadError: function() {
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
			});
			
		}
	});
	
});	//end of document ready
	

function createList() {
	$("#tableDefaultList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'perf_check_name',title:'SQL점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			{field:'sql_all_cnt',title:'전체 SQL 수',width:'8%',halign:'center',align:'right',rowspan:2},
			{field:'tuning_selection_cnt',title:'튜닝선정',width:'8%',halign:'center',align:'right',rowspan:2},
			{field:'plan_change_cnt',title:'Plan변경',width:'8%',halign:'center',align:'right',rowspan:2},
			{title:'성능저하 SQLs',halign:'center',align:'center',colspan:2},
			{title:'튜닝 SQLs',halign:'center',align:'center',colspan:2},
			{title:'개선 실적(평균 %)',halign:'center',align:'center',colspan:2},
			],[
			{field:'elapsed_time_std_cnt',title:'수행시간',width:'6%',halign:'center',align:'right'},
			{field:'buffer_std_cnt',title:'버퍼',width:'6%' ,halign:'center',align:'right'},
			{field:'tuning_end_cnt',title:'완료',width:'6%',halign:'center',align:'right'},
			{field:'tuning_cnt',title:'진행중',width:'6%',halign:'center',align:'right'},
			{field:'elap_time_impr_ratio',title:'수행시간',width:'6%',halign:'center',align:'right'},
			{field:'buffer_impr_ratio',title:'버퍼',width:'6%',halign:'center',align:'right'}
		]],
		onSelect:function(index, row) {
			$("#sql_auto_perf_check_id").val(row.sql_auto_perf_check_id);
			
			let field = $("#field").val();
			
			if ( field != 'perf_check_name' && field != 'project_nm' && field != 'sql_all_cnt' && field != 'elap_time_impr_ratio' && field != 'buffer_impr_ratio') {
				ajaxCallTableDetailList();
			}
		},
		onClickCell:function( index, field, value ) {
			$("#currentPage").val("1");
			$("#pagePerCount").val("20");
			
			$("#plan_change_cnt").val("N");
			$("#tuning_selection_cnt").val("N");
			$("#elapsed_time_std_cnt").val("N");
			$("#buffer_std_cnt").val("N");
			$("#tuning_end_cnt").val("N");
			$("#tuning_cnt").val("N");
			
			$("#"+field).val("Y");
			$("#field").val(field);
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'perf_check_name',title:'SQL점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			{field:'tuning_status_nm',title:'튜닝상태',width:'3.5%',halign:'center',align:'center',rowspan:2},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'4%',halign:'center',align:'right',rowspan:2,formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'4%',halign:'center',align:'right',rowspan:2,formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',rowspan:2,styler:cellStyler},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'3%',halign:'center',align:'center',rowspan:2},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left',rowspan:2},
			{title:'ASIS',halign:'center',align:'center',colspan:3},
			{title:'TOBE',halign:'center',align:'center',colspan:3},
			{title:'TUNING',halign:'center',align:'center',colspan:2},
			{field:'sql_text_web',title:'SQL TEXT',width:'15%',halign:'center',align:'left',rowspan:2},
			{field:'tuning_no',title:'튜닝번호',halign:'center',align:'right',rowspan:2},
			],[
			{field:'asis_plan_hash_value',title:'PLAN HASH VALUE',halign:'center',align:'right'},
			{field:'asis_elapsed_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_buffer_gets',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_plan_hash_value',title:'PLAN HASH VALUE',halign:'center',align:'right'},
			{field:'tobe_elapsed_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_buffer_gets',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'impra_elap_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'impra_buffer_cnt',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_SqlAutoPerfSearch() {
	if( $("#original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
		return;
	}
	
	if ( $("#currentPage").val() == "" ) {
		$("#currentPage").val("1");
		$("#pagePerCount").val("20");
	}
	
	$("#plan_change_cnt").val('');
	$("#tuning_selection_cnt").val('');
	$("#elapsed_time_std_cnt").val('');
	$("#buffer_std_cnt").val('');
	$("#tuning_end_cnt").val('');
	$("#tuning_cnt").val('');
	
	$("#sql_auto_perf_check_id").val( $('#sqlPerformanceP').combobox('getValue') );
	
	$('#tableList').datagrid("loadData", []);
	$('#tableDefaultList').datagrid("loadData", []);
	
	//grid조회
	ajaxCallTableList();
}

//페이지처리
function formValidationCheck(){
	return true;
}
function fnSearch(){
	ajaxCallTableDetailList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("튜닝실적 조회"," ");
	
	// 튜닝실적 리스트
	ajaxCall("/AnalyzeImpactChangedTable/loadTuningPerformanceList",
			$("#submit_form"),
			"POST",
			callback_LoadTuningPerfListAction);
}

var callback_LoadTuningPerfListAction = function(result) {
	
	json_string_callback_common(result,'#tableDefaultList',true );
	
	searchFlag = true;
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function ajaxCallTableDetailList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("튜닝실적 상세 조회"," ");
	
	$('#tableList').datagrid("loadData", []);
	
	// 튜닝실적 상세 리스트
	ajaxCall("/AnalyzeImpactChangedTable/loadTuningPerformanceDetailList",
			 $('#submit_form'),
			 "POST",
			 callback_LoadTuningPerfDetailListAction);
}

var callback_LoadTuningPerfDetailListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	let dataLength = JSON.parse(result).dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
	clickFlag = true;
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function cellStyler( value,row,index ) {
	// 적합
	if ( row.perf_check_result_yn == '적합' ) {
		return 'background-color:#1A9F55;color:white;';
	} else if ( row.perf_check_result_yn == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	} else { // 부적합
		return '';
	}
}

function Excel_Download() {
	if ( searchFlag == false ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
		
	}else if ( $("#original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
		return false;
		
	}else if ( checkConditionDate() == false ) {
		return;
	}
	
	$("#sql_auto_perf_check_id").val( $('#sqlPerformanceP').combobox('getValue') );
	
	$("#submit_form").attr("action","/AnalyzeImpactChangedTable/excelTuningDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function Excel_Detail_Download() {
	if ( clickFlag == false ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
		
	}else if ( $("#original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
		return false;
		
	}else if ( $('#tableDefaultList').datagrid('getSelected') == undefined ) {
		return false;
	}
	
	let row = $('#tableDefaultList').datagrid('getSelected');
	
	$("#sql_auto_perf_check_id").val( row.sql_auto_perf_check_id );
	
	$("#submit_form").attr("action","/AnalyzeImpactChangedTable/excelTuningDetailDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
//점검팩 삭제 시 SQL점검팩 클리어
function sqlPerfPacClear( sqlPerfId ) {
	let pastSqlPerfId = $('#sqlPerformanceP').combobox('getValue');
	
	if( sqlPerfId == pastSqlPerfId ){
		$('#sqlPerformanceP').combobox('clear');
		$('#sqlPerformanceP').combobox('textbox').attr('placeholder','전체');
		
		$('#perf_check_exec_begin_dt').datebox('setValue', beginDate);
		$('#perf_check_exec_end_dt').datebox('setValue', endDate);
		
		$('#oneMonth').radiobutton('check');
		
		$('#tableDefaultList').datagrid('loadData',[]);
		$('#tableList').datagrid('loadData',[]);
	}else {
		return;
	}
}