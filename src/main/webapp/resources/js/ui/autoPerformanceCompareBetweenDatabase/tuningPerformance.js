$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createList();
	
	// 프로젝트 조회
	$('#submit_form #project_id_cd').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// submit_form SQL점검팩 콤보
			$('#submit_form #sqlAutoPerfCheckId').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?isAll=Y&project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onSelect: function(item) {
//					if( item.sql_auto_perf_check_id == "") {
//						$("#submit_form #sql_auto_perf_check_id").val("");
//					} else {
//						$("#submit_form #sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
//					}
					$("#submit_form #data_yn").val(item.data_yn);
				},
				onShowPanel: function() {
					$('#submit_form #sqlAutoPerfCheckId').combobox({
						url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?isAll=Y&project_id="+data.project_id
						+"&database_kinds_cd="+$("#database_kinds_cd").val(),
						method:"post",
						valueField:'sql_auto_perf_check_id',
						textField:'perf_check_name',
						panelHeight: 300,
					});
					
					$(".textbox").removeClass("textbox-focused");
					$(".textbox-text").removeClass("tooltip-f");
				},
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onLoadSuccess: function() {
//					$("#submit_form #sql_auto_perf_check_id").combobox("textbox").attr("placeholder",'선택');
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id_cd").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

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
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:2},
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
			
			var field = $("#field").val();
			
			if ( field != 'perf_check_name' && field != 'project_nm' && field != 'sql_all_cnt' && field != 'elap_time_impr_ratio' && field != 'buffer_impr_ratio') {
				ajaxCallTableDetailList();
			}
			// 2. field 명 클릭시 조회안되게 처리.
		},
		onClickCell:function( index, field, row ) {
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
			
			// 1. field 명 hidden값 추가.
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
			{field:'tuning_status_nm',title:'튜닝상태',width:'3%',halign:'center',align:'center',rowspan:2},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'4%',halign:'center',align:'right',rowspan:2,formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'4%',halign:'center',align:'right',rowspan:2,formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',rowspan:2,styler:cellStyler},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'3%',halign:'center',align:'center',formatter:setValueNull,styler:cellPlanStyler,rowspan:2},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left',rowspan:2},
			{title:'ASIS',halign:'center',align:'center',colspan:3},
			{title:'TOBE',halign:'center',align:'center',colspan:3},
			{title:'TUNING',halign:'center',align:'center',colspan:2},
			{field:'sql_text_web',title:'SQL TEXT',width:'15%',halign:'center',align:'left',rowspan:2},
			{field:'tuning_no',title:'튜닝번호',halign:'center',align:'right',rowspan:2},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_name',title:'SQL점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			],[
			{field:'asis_plan_hash_value',title:'PLAN HASH VALUE',halign:'center',align:'right'},
			{field:'asis_elapsed_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_buffer_gets',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_plan_hash_value',title:'PLAN HASH VALUE',halign:'center',align:'right'},
			{field:'tobe_elapsed_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'impra_elap_time',title:'ELAPSED TIME',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'impra_buffer_cnt',title:'BUFFER GETS',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter}
		]],
		onSelect:function( index, row ) {
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_SqlAutoPerfSearch() {
	if ( !checkCondition() ) {
		return;
	}
	if ( $("#currentPage").val() == "" ) {
		$("#currentPage").val("1");
		$("#pagePerCount").val("20");
	}
	
	$("#submit_form #project_id").val( $("#submit_form #project_id_cd" ).textbox("getValue") );
	$("#submit_form #sql_auto_perf_check_id").val( $("#sqlAutoPerfCheckId").combobox("getValue") );
	
	$('#tableDefaultList').datagrid("loadData", []);
	$('#tableList').datagrid("loadData", []);
	
	//grid조회
	ajaxCallTableList();
}

function checkCondition() {
	if ( $("#submit_form #project_id_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	return true;
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
	
//	$('#submit_form #select_sql').val("00");
//	$('#submit_form #select_perf_impact').val("00");
	
	/* 튜닝실적 리스트 */
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadTuningPerformanceList",
			$("#submit_form"),
			"POST",
			callback_LoadTuningPerfListAction);
}

var callback_LoadTuningPerfListAction = function(result) {
	
	json_string_callback_common( result,'#tableDefaultList',true );
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function ajaxCallTableDetailList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("튜닝실적 상세 조회"," ");
	
	$('#tableList').datagrid("loadData", []);
	
	/* 튜닝실적 상세 리스트 */
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadTuningPerformanceDetailList",
			$("#submit_form"),
			"POST",
			callback_LoadTuningPerfDetailListAction);
}

var callback_LoadTuningPerfDetailListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn( dataLength );
	
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
	if ( !checkCondition() ) {
		return;
	}
	
	let rows = $("#tableDefaultList").datagrid('getData');
	
	if ( rows.rows.length <= 0) {
		parent.$.messager.alert('경고','데이터를 먼저 조회 해 주세요.','warning');
		return;
	}
	
	if ( $("#sqlAutoPerfCheckId").combobox("getValue") == null || $("#sqlAutoPerfCheckId").combobox("getValue") == '' ){
		let sqlAutoPerfId = $("#sql_auto_perf_check_id").val();
		$("#sql_auto_perf_check_id").val("");
		
		$("#submit_form").attr("action","/AutoPerformanceCompareBetweenDatabase/excelTuningDownload");
		$("#submit_form").submit();
		$("#submit_form").attr("action","");
		
		$("#sql_auto_perf_check_id").val( sqlAutoPerfId );
	} else {
		
		$("#submit_form").attr("action","/AutoPerformanceCompareBetweenDatabase/excelTuningDownload");
		$("#submit_form").submit();
		$("#submit_form").attr("action","");
	}
}

function Excel_Detail_Download() {
	if ( !checkCondition() ) {
		return;
	}
	
	$("#submit_form").attr("action","/AutoPerformanceCompareBetweenDatabase/excelTuningDetailDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function setValueNull( val, row ) {
	return '';
}
function cellPlanStyler( value,row,index ) {
	if ( row.plan_change_yn == 'Y' ) {
		return 'font-size:0px; background-image:url(/resources/images/planNotEqual.png);background-repeat:no-repeat;background-position: center;background-size: 60px;';
	} else if ( row.plan_change_yn == 'N' ){
		return 'font-size:0px; background-image:url(/resources/images/planEqual.png);background-repeat:no-repeat;background-position: center;background-size: 60px;';
	} else {
		return '';
	}
}

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?isAll=Y&project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sqlAutoPerfCheckId").combobox("reload", url);
	
	if ( $("#submit_form #sqlAutoPerfCheckId").combobox("getValue") == sqlPerfId ) {
		$("#submit_form #sqlAutoPerfCheckId").combobox("setValue", "");
	}
}
