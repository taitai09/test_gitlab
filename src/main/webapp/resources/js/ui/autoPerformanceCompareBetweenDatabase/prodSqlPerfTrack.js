var timer;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(550),
		left:getWindowLeft(550)
	});
	
	$("#tuningAssignPop").window("close");
	
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
					
					$('#submit_form #operation_dbid_cd').combobox({
						url:"/AutoPerformanceCompareBetweenDatabase/getOperationDB?database_kinds_cd="+$("#database_kinds_cd").val(),
						method:"post",
						valueField:'dbid',
						textField:'db_name',
						panelHeight: 300,
						onSelect: function(item) {
							
						},
						onLoadSuccess: function() {
							$("#submit_form #operation_dbid_cd").combobox("textbox").attr("placeholder",'선택');
							
						},
						onLoadError: function() {
							parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
							return false;
						}
					});
				},
				onShowPanel: function() {
					let sqlAutoPerfId = $('#submit_form #sql_auto_perf_check_id').combobox("getValue");
					
					$('#submit_form #sql_auto_perf_check_id').combobox({
						url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
						+"&database_kinds_cd="+$("#database_kinds_cd").val(),
						method:"post",
						valueField:'sql_auto_perf_check_id',
						textField:'perf_check_name',
						panelHeight: 300,
						onLoadSuccess: function() {
							$("#submit_form #sql_auto_perf_check_id").combobox("textbox").attr("placeholder",'선택');
							
							if ( sqlAutoPerfId != '' && sqlAutoPerfId != null ) {
								$("#submit_form #sql_auto_perf_check_id").combobox("setValue", sqlAutoPerfId );
							} else {
								$("#submit_form #sql_auto_perf_check_id").combobox("setValue", "" );
							}
						}
					});
					$(".textbox").removeClass("textbox-focused");
					$(".textbox-text").removeClass("tooltip-f");
				},
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onLoadSuccess: function() {
					$("#submit_form #sql_auto_perf_check_id").combobox("textbox").attr("placeholder",'선택');
					$('#submit_form #operation_dbid_cd').combobox("setValue","");
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id").combobox("textbox").attr("placeholder",'선택');
			
			$('#base_monthly').radiobutton({ checked: true });
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	// SQL수행일자 - 1일
	$('#base_daily').radiobutton({
		onChange:function( val ) {
			if ( val == true ) {
				$('#base_daily').radiobutton({ checked: true });
				$('#base_weekly').radiobutton({ checked: false });
				$('#base_monthly').radiobutton({ checked: false });
			}
			
			$("#strStartDt").datebox("setValue", $("#strEndDt").datebox("getValue") );
		}
	});
	
	// SQL수행일자 - 1주일
	$('#base_weekly').radiobutton({
		onChange:function( val ) {
			if ( val == true ) {
				$('#base_daily').radiobutton({ checked: false });
				$('#base_weekly').radiobutton({ checked: true });
				$('#base_monthly').radiobutton({ checked: false });
			}
			var end = $("#strEndDt").datebox("getText");
			var endDay = end.split('-');
			var now = new Date( endDay[0], endDay[1], endDay[2] );
			
			now.setDate( now.getDate()-6 );
			
			$("#strStartDt").datebox("setValue", now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate() );
		}
	});
	
	// SQL수행일자 - 1개월
	$('#base_monthly').radiobutton({
		onChange:function( val ) {
			if ( val == true ) {
				$('#base_daily').radiobutton({ checked: false });
				$('#base_weekly').radiobutton({ checked: false });
				$('#base_monthly').radiobutton({ checked: true });
			}
			
			var end = $("#strEndDt").datebox("getText");
			var endDay = end.split('-');
			
			if ( endDay[1] == 1) {
				endDay[0] = endDay[0]-1;
				endDay[1] = 12;
			} else if( endDay[1] == 3 && endDay[2] > 28 ) {
				endDay[1] = 2;
				endDay[2] = 28;
			} else if( endDay[1] == 5 | endDay[1] == 7 | endDay[1] == 10 | endDay[1] == 12 && endDay[2] > 30) {
				endDay[1] = endDay[1]-1;
				endDay[2] = 30;
			} else {
				endDay[1] = endDay[1]-1;
			}
			
			var now = new Date( endDay[0], endDay[1], endDay[2] );
			
			$("#strStartDt").datebox("setValue", now.getFullYear()+"-"+now.getMonth()+"-"+endDay[2] );
		}
	});
	
	// 전체 check
	$("#all_sql_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true && 
						$("#perf_down_yn").checkbox("options").checked != true && 
						$("#notPerf_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// plan변경 check
	$("#plan_change_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
			} else {
				if ( $("#perf_down_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// 성능저하 check
	$("#perf_down_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// 성능 부적합 check
	$("#notPerf_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox("uncheck");
			} else {
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#perf_down_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// 이전튜닝대상 선정 SQL 제외 check
	$('#chkExcept').switchbutton({
		value:"Y",
		checked:false,
		onText:"Yes",
		offText:"No",
		onChange:function( checked ) {
			if ( checked ) {
				$("#submit_form #sqlExclude").val("Y");
			} else {
				$("#submit_form #sqlExclude").val("");
			}
		}
	});
	
	$("#sql_profile_yn").checkbox({
		value:"Y",
		checked:false,
		onChange:function( checked ) {
			if ( checked ) {
				
			} else {
				
			}
		}
	});
	
	$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	$("#asis_elapsed_time").textbox("textbox").attr("placeholder","3");
	$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	$("#elapsed_time_regres").textbox("textbox").attr("placeholder","10");
	
	$("#buffer_gets_1day").textbox("textbox").focus(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_1day").textbox("textbox").blur(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	});
	
	$("#asis_elapsed_time").textbox("textbox").focus(function(){
		$("#asis_elapsed_time").textbox("textbox").attr("placeholder","");
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	$("#asis_elapsed_time").textbox("textbox").blur(function(){
		$("#asis_elapsed_time").textbox("textbox").attr("placeholder","3");
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	$(".asis_elapsed_time .textbox").keyup(function() {
		if ( $("#asis_elapsed_time").textbox("getValue") < 0 ) {
			$("#asis_elapsed_time").textbox("setValue","");
		}
	});
	
	$("#buffer_gets_regres").textbox("textbox").focus(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_regres").textbox("textbox").blur(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	});
	
	$("#elapsed_time_regres").textbox("textbox").focus(function(){
		$("#elapsed_time_regres").textbox("textbox").attr("placeholder","");
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	$("#elapsed_time_regres").textbox("textbox").blur(function(){
		$("#elapsed_time_regres").textbox("textbox").attr("placeholder","10");
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	$(".elapsed_time_regres .textbox").keyup(function() {
		if ( $("#elapsed_time_regres").textbox("getValue") < 0 ) {
			$("#elapsed_time_regres").textbox("setValue","");
		}
	});
	
	$('#tableList').datagrid("loadData", []);
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});


function createList() {
	var cnt = 0;
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true",rowspan:2},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_result',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',styler:cellStyler ,rowspan:2},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'3%',halign:'center',align:'center',formatter:setValueNull,styler:cellPlanStyler,rowspan:2},
			
			{title:'PLAN HASH VALUE',halign:'center',align:'center',colspan:2},
			{title:'평균수행시간(초)',halign:'center',align:'center',colspan:3},
			{title:'평균블럭수',halign:'center',align:'center',colspan:3},
			
			{field:'operation_executions',title:'운영<br>수행횟수',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter,rowspan:2},
			{field:'operation_rows_processed',title:'운영<br>수행건수',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter,rowspan:2},
			{field:'sql_text_web',title:'SQL TEXT',width: '15%',halign:"center",align:'left',rowspan:2},
			{field:'sql_profile_nm',title:'PROFILE NAME',width: '11%',halign:"center",align:'left',rowspan:2},
			{field:'tuning_no',title:'튜닝번호',width:'4%',halign:"center",align:'right',rowspan:2},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			],[
			
			{field:'asis_plan_hash_value',title:'ASIS',width:'5%',halign:"center",align:'right'},
			{field:'operation_plan_hash_value',title:'운영',width:'5%',halign:"center",align:'right'},
			{field:'asis_elapsed_time',title:'ASIS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'operation_elapsed_time',title:'운영',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'elapsed_time_activity',title:'Activity(%)',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_buffer_gets',title:'ASIS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'operation_buffer_gets',title:'운영',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'buffer_gets_activity',title:'Activity(%)',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'project_id',title:'project_id',hidden:'true'},
			{field:'sql_auto_perf_check_id',title:'sql_auto_perf_check_id',hidden:'true'},
			{field:'operation_dbid',title:'operation_dbid',hidden:'true'},
			{field:'original_dbid',title:'original_dbid',hidden:'true'},
			{field:'operation_parsing_schema_name',title:'operation_parsing_schema_name',hidden:'true'},
			{field:'perf_check_sql_source_type_cd',title:'perf_check_sql_source_type_cd',hidden:'true'}
		]],
		onSelect:function( index, row ) {
			$("#sqlinfo_form #project_id").val(row.project_id);
			$("#sqlinfo_form #sql_auto_perf_check_id").val( row.sql_auto_perf_check_id);
			$("#sqlinfo_form #operation_dbid").val(row.operation_dbid);
			$("#sqlinfo_form #dbid").val(row.operation_dbid);
			
			$("#sqlinfo_form #strStartDt").val( $("#submit_form #strStartDt").datebox("getValue") );
			$("#sqlinfo_form #strEndDt").val( $("#submit_form #strEndDt").datebox("getValue") );
			
			$("#sqlinfo_form #sql_id").val(row.sql_id);
			$("#submit_form #sql_id").val(row.sql_id);
			
			$("#sqlinfo_form #plan_hash_value").val(row.operation_plan_hash_value);
			$("#sqlinfo_form #asis_plan_hash_value").val(row.asis_plan_hash_value);
			$("#sqlinfo_form #sql_command_type_cd").val(row.sql_command_type_cd);
			$("#sqlinfo_form #perf_check_sql_source_type_cd").val(row.perf_check_sql_source_type_cd);
			
			openExplainPlan();
		},
		onUncheck:function(index, rows) {
			$("#isAll").val("");
			
		},
		onCheckAll:function( rows ) {
			$("#isAll").val("A");
			
		},
		onUncheckAll:function( rows ) { 
			$("#isAll").val("");
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function cellStyler( value,row,index ) {
	// 적합
	if ( row.perf_check_result == '적합' ) {
		return 'background-color:#1A9F55;color:white;';
	} else if ( row.perf_check_result == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	} else { // 부적합
		return '';
	}
}

function checkCondition() {
	if ( $("#submit_form #project_id" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sql_auto_perf_check_id" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #operation_dbid_cd" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','운영DB를 선택해 주세요.','warning');
		return false;
	}
	
	return true;
}

// 페이지처리
function formValidationCheck(){
	return true;
}

function fnSearch(){
	let rows = $("#tableList").datagrid("getData");
	
	let projectId = $("#submit_form #project_id").combobox("getValue");
	let sqlAutoPerfCheckId = $("#submit_form #sql_auto_perf_check_id").combobox("getValue");
	let operationDbid = $("#submit_form #operation_dbid_cd").combobox("getValue");
	// 검색 시 튜닝대상선정할 데이터가 가지고 있는 조건들로 세팅
	$("#submit_form #project_id").combobox( "setValue",rows.rows[0].project_id );
	$("#submit_form #sql_auto_perf_check_id").combobox( "setValue",rows.rows[0].sql_auto_perf_check_id );
	$("#submit_form #operation_dbid_cd").combobox( "setValue",rows.rows[0].operation_dbid );
	// 검색
	ajaxCallTableList();
	// 튜닝대상선정 하기전 검색조건들 다시 세팅.
	$("#submit_form #project_id").combobox("setValue", projectId );
	$("#submit_form #sql_auto_perf_check_id").combobox("setValue", sqlAutoPerfCheckId );
	$("#submit_form #operation_dbid_cd").combobox("setValue", operationDbid );
}

function openExplainPlan() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$("#loadExplainPlanPop").window({
		top:0,
		left:50
	});
	
	$('#loadExplainPlanPop').window("open");
	
	$("#loadExplainPlan_form #bindValueList").datagrid("resize",{
		width: 400
	});
	
	console.log("plan_hash_value[" + $("#sqlinfo_form #plan_hash_value").val() + "]");
	
	loadExplainPlan( $('#sqlinfo_form') );
	
	$("#loadExplainPlanPop").window('setTitle', "SQL Info( " + $("#sqlinfo_form #sql_id").val()+ " )");
	
	$('#tabs').tabs('select', 0);
}

function Btn_SqlAutoPerfSearch() {
	if ( !checkCondition() ) {
		return;
	}
	
	if ( $("#assign_form #cntPage").val() != null && $("#assign_form #cntPage").val() != '' ) {
		$("#currentPage").val( $("#assign_form #cntPage").val() );
		$("#assign_form #cntPage").val("");
	} else {
		$("#currentPage").val("1");
	}
	
	$("#pagePerCount").val("15");
	$("#checkAll").val("");
	$("#sqlExclude").val("");
	$("#isAll").val("");
	$('#assign_form #sqlIdArry').val("");
	
	$("#assign_form #project_id").combobox("readonly", false);
	$("#assign_form #sql_auto_perf_check_id").combobox("readonly", false);
	
	$('#tableList').datagrid("loadData", []);
	
	$("#submit_form #operation_dbid").val( $("#submit_form #operation_dbid_cd").combobox("getValue") );
	
	//grid조회
	ajaxCallTableList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능비교 결과 조회"," ");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadOperationSqlPerfTrackList",
			$("#submit_form"),
			"POST",
			callback_LoadPerfResultListAction);
}

var callback_LoadPerfResultListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

// 엑셀 다운
function operationExcelDownload() {
	if ( !checkCondition() ) {
		return;
	}
	let rows = $("#tableList").datagrid("getData");
	
	$("#submit_from #operation_dbid").val( rows.rows[0].operation_dbid );
	
	$("#submit_form").attr("action","/AutoPerformanceCompareBetweenDatabase/operationExcelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function initPopupSet(){
	// popup 초기화
	$('#assign_form #chkAutoShare').switchbutton("uncheck");
	$('#assign_form #chkExcept').switchbutton("uncheck");
	$('#assign_form #auto_share').val("N");
	$('#assign_form #selectTuner').combobox({readonly:false});
	$('#assign_form #selectTuner').combobox('setValue','');
	$("#assign_form #project_id").combobox('setValue','');
	$("#assign_form #sql_auto_perf_check_id").combobox('setValue','');
	
};

// 튜닝대상선정 popup
function showTuningReqPopup(){
	if ( !checkCondition() ) {
		return;
	}
	
	let rows = $('#tableList').datagrid('getChecked');
	
	$('#assign_form #project_id').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// submit_form SQL점검팩 콤보
			$('#assign_form #sql_auto_perf_check_id').combobox({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onSelect: function(item) {
				},
				onLoadSuccess: function() {
					$("#assign_form #sql_auto_perf_check_id").combobox("textbox").attr("placeholder",'선택');
					
					$("#assign_form #sql_auto_perf_check_id").combobox("setValue", rows[0].sql_auto_perf_check_id );
					$("#assign_form #sql_auto_perf_check_id").combobox("readonly","true");
				},
				onLoadError: function() {
					parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
					return false;
				}
			});
		},
		onLoadSuccess: function() {
			$("#assign_form #project_id").combobox("textbox").attr("placeholder",'선택');
			
			$("#assign_form #project_id").combobox("setValue", rows[0].project_id );
			$("#assign_form #project_id").combobox("readonly","true");
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	initPopupSet();
	
	if ( rows.length > 0 ) {
		
		$('#tuningAssignPop').window({
			top:getWindowTop(550),
			left:getWindowLeft(550)
		});
		
		$('#tuningAssignPop').window("open");
		$('#assign_form #dbid').val("");
		$("#assign_form #sql_auto_perf_check_id").combobox("setValue","");
		
		// 튜닝 담당자 조회
		$('#assign_form #selectTuner').combobox({
			url:"/Common/getTuner?dbid="+rows[0].operation_dbid,
			method:"get",
			valueField:'tuner_id',
			textField:'tuner_nm',
			onLoadSuccess:function() {
				$("#assign_form #selectTuner").combobox("textbox").attr("placeholder",'선택');
			}
		});
		
	} else {
		parent.$.messager.alert('경고','튜닝 대상을 선택해 주세요.','warning');
	}
}

// tunerAssign_popup의 기능들은 tunerAssign_popup.js를 사용
// 튜닝요청 및 담당자 지정만 재정의.
function Btn_SaveTuningDesignation(){
	var projectId = $('#assign_form #project_id').combobox('getValue');
	var sqlAutoPerfCheckId = $('#assign_form #sql_auto_perf_check_id').combobox('getValue');
	
	if ( projectId != "" && sqlAutoPerfCheckId == "" ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $('#selectTuner').combobox('getValue') == "" ) {
		if ( $('#auto_share').val() == "N" ) {
			parent.$.messager.alert('경고','튜닝담당자를 선택해 주세요.','warning');
			return false;
		}
	}
	$("#assign_form #cntPage").val( $("#currentPage").val() );
	$("#assign_form #perfr_id").val( $('#selectTuner').combobox('getValue') );
	$('#assign_form #sqlIdArry').val("");
	
	// 전체 check 시
	if ( $("#assign_form #isAll").val() == "A" ) {
		/* modal progress open */
		if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("자동 성능 점검 조회"," ");
		
		if ( $("#chkExcept").switchbutton("options").checked ) {
			$("#submit_form #sqlExclude").val("Y");
		} else {
			$("#submit_form #sqlExclude").val("");
		}
		/* 전체시 List 재조회 */
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadOperationSqlPerfTrackListAll",
				$("#submit_form"),
				"POST",
				callback_ReLoadOperSqlPerfTrackListAllAction)
	// 개별 check 시
	} else {
		var data = $('#tableList').datagrid('getChecked');
		
		if ( $("#chkExcept").switchbutton("options").checked == true ) {
			var numbers = "";
			// tuning_no로 판단
			for ( var checkds = data.length-1; checkds > 0-1; checkds-- ) {
				if ( data[checkds].tuning_no != null ) {
					data.splice( checkds, 1 );
				}
			}
			
			dataListCall( data );
		} else {
			dataListCall( data );
		}
	}
}

var callback_ReLoadOperSqlPerfTrackListAllAction = function( result ) {
	var data = JSON.parse( result );
	
	dataListCall( data.rows );
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

//튜닝대상선정 작업
function dataListCall( data ) {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	if ( data.length > 0 ) {
		const CHOICE_DIV_CD = "I";
		const TUNING_STATUS_CD = "3";
		
		let dbids = "";
		let sqlIdArry = "";
		let moduleArry = "";
		let planHashValues = "";
		let parsingSchemaName = "";
		let operationExecutions = "";
		let operationBufferGets = "";
		let operationElapsedTimes = "";
		let operationRowsProcesseds = "";
		
			
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("M");
		$("#assign_form #choice_div_cd").val( CHOICE_DIV_CD );
		$("#assign_form #tuning_status_cd").val( TUNING_STATUS_CD );
		
		for ( var checkdList = 0 ; checkdList < data.length; checkdList++ ) {
			sqlIdArry += data[checkdList].sql_id + "|";
			moduleArry += data[checkdList].module + "|";
			dbids += data[checkdList].operation_dbid + "|";
			planHashValues += data[checkdList].operation_plan_hash_value+"|";
			parsingSchemaName += data[checkdList].operation_parsing_schema_name+"|";
			operationExecutions += data[checkdList].operation_executions+"|";
			operationBufferGets += data[checkdList].operation_buffer_gets+"|";
			operationElapsedTimes += data[checkdList].operation_elapsed_time+"|";
			operationRowsProcesseds += data[checkdList].operation_rows_processed+"|";
		}
		
		$('#dbid').val( data[0].operation_dbid );
		$("#assign_form #operation_dbid").val( $("#submit_form #operation_dbid").val() );
		$('#assign_form #dbid').val( strRight( dbids,1 ) );
		
		$("#assign_form #module").val( strRight( moduleArry,1 ) );
		$("#assign_form #sqlIdArry").val( strRight( sqlIdArry,1 ) );
		$("#assign_form #plan_hash_value").val( strRight( planHashValues,1 ) );
		$("#assign_form #parsing_schema_name").val( strRight( parsingSchemaName,1 ) );
		$("#assign_form #operation_executions").val( strRight( operationExecutions,1 ) );
		$("#assign_form #operation_buffer_gets").val( strRight( operationBufferGets,1 ) );
		$("#assign_form #operation_elapsed_time").val( strRight( operationElapsedTimes,1 ) );
		$("#assign_form #operation_rows_processed").val( strRight( operationRowsProcesseds,1 ) );
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/Popup/insertOperationTuningTarget",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
		
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		
		let rows = $('#tableList').datagrid('getChecked');
		// 튜닝대상선정 하기전 검색조건들.
		let projectId = $("#submit_form #project_id").combobox("getValue");
		let sqlAutoPerfCheckId = $("#submit_form #sql_auto_perf_check_id").combobox("getValue");
		let operationDbid = $("#submit_form #operation_dbid_cd").combobox("getValue");
		// 검색 시 튜닝대상선정할 데이터가 가지고 있는 조건들로 세팅
		$("#submit_form #project_id").combobox( "setValue",rows[0].project_id );
		$("#submit_form #sql_auto_perf_check_id").combobox( "setValue",rows[0].sql_auto_perf_check_id );
		$("#submit_form #operation_dbid_cd").combobox( "setValue",rows[0].operation_dbid );
		// 검색
		Btn_SqlAutoPerfSearch();
		// 튜닝대상선정 하기전 검색조건들 다시 세팅.
		$("#submit_form #project_id").combobox("setValue", projectId );
		$("#submit_form #sql_auto_perf_check_id").combobox("setValue", sqlAutoPerfCheckId );
		$("#submit_form #operation_dbid_cd").combobox("setValue", operationDbid );
		$("#submit_form #operation_dbid").val( operationDbid );
	}
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if ( result.result ) {
		
		/* modal progress close */
//		closeMessageProgress();
		parent.closeMessageProgress();
		
		let cnt = 0;
		let msg = "";
		
		for ( var checkCnt = 0; checkCnt < result.object.length; checkCnt++ ) {
			cnt =  cnt + ( 1 * result.object[checkCnt].perfr_id_cnt);
		}
		
		if ( result.message == "Y" ) {
			msg = "이전 튜닝대상선정 SQL을 제외한<br> "+ cnt + "건이 선정 되었습니다."
		} else {
			msg = "튜닝대상 "+ cnt +"건이 선정 되었습니다."
		}
		
		$.messager.alert('', msg ,'info',function(){
			setTimeout(function() {
				
				Btn_OnClosePopup('tuningAssignPop');
				
				let rows = $('#tableList').datagrid('getChecked');
				// 튜닝대상선정 하기전 검색조건들.
				let projectId = $("#submit_form #project_id").combobox("getValue");
				let sqlAutoPerfCheckId = $("#submit_form #sql_auto_perf_check_id").combobox("getValue");
				let operationDbid = $("#submit_form #operation_dbid_cd").combobox("getValue");
				// 검색 시 튜닝대상선정할 데이터가 가지고 있는 조건들로 세팅
				$("#submit_form #project_id").combobox( "setValue",rows[0].project_id );
				$("#submit_form #sql_auto_perf_check_id").combobox( "setValue",rows[0].sql_auto_perf_check_id );
				$("#submit_form #operation_dbid_cd").combobox( "setValue",rows[0].operation_dbid );
				// 검색
				Btn_SqlAutoPerfSearch();
				// 튜닝대상선정 하기전 검색조건들 다시 세팅.
				$("#submit_form #project_id").combobox("setValue", projectId );
				$("#submit_form #sql_auto_perf_check_id").combobox("setValue", sqlAutoPerfCheckId );
				$("#submit_form #operation_dbid_cd").combobox("setValue", operationDbid );
				$("#submit_form #operation_dbid").val( operationDbid );
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);
		});
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		
		let rows = $('#tableList').datagrid('getChecked');
		// 튜닝대상선정 하기전 검색조건들.
		let projectId = $("#submit_form #project_id").combobox("getValue");
		let sqlAutoPerfCheckId = $("#submit_form #sql_auto_perf_check_id").combobox("getValue");
		let operationDbid = $("#submit_form #operation_dbid_cd").combobox("getValue");
		// 검색 시 튜닝대상선정할 데이터가 가지고 있는 조건들로 세팅
		$("#submit_form #project_id").combobox( "setValue",rows[0].project_id );
		$("#submit_form #sql_auto_perf_check_id").combobox( "setValue",rows[0].sql_auto_perf_check_id );
		$("#submit_form #operation_dbid_cd").combobox( "setValue",rows[0].operation_dbid );
		// 검색
		Btn_SqlAutoPerfSearch();
		// 튜닝대상선정 하기전 검색조건들 다시 세팅.
		$("#submit_form #project_id").combobox("setValue", projectId );
		$("#submit_form #sql_auto_perf_check_id").combobox("setValue", sqlAutoPerfCheckId );
		$("#submit_form #operation_dbid_cd").combobox("setValue", operationDbid );
		$("#submit_form #operation_dbid").val( operationDbid );
	}
};

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sql_auto_perf_check_id").combobox("reload", url);
	
	if ( $("#submit_form #sql_auto_perf_check_id").combobox("getValue") == sqlPerfId ) {
		$("#submit_form #sql_auto_perf_check_id").combobox("setValue","");
	}
}