var timer;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(550),
		left:getWindowLeft(550)
	});
	
	$("#tuningAssignPop").window("close");
	
	// 프로젝트 조회
	$('#submit_form #project_id_cd').combobox({
		url:"/DBChangePerformanceImpactAnalysisForTibero/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			//점검팩
			sqlPerformancePac( data.project_id );
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id_cd").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	
	//ORACLE대비 성능저하  check
	$("#asis_perf_degrade_versus_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#tuning_perf_degrade_versus_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			}
		}
	});
	
	//튜닝대비 성능저하 check
	$("#tuning_perf_degrade_versus_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#asis_perf_degrade_versus_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			}
		}
	});
	
	//오류 check
	$("#error_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#asis_perf_degrade_versus_yn").checkbox("uncheck");
				$("#tuning_perf_degrade_versus_yn").checkbox("uncheck");
				$("#verify_buffer_gets").numberbox("setValue","");
				$("#verify_elapsed_time").textbox("setValue","");
			}
		}
	});
	
	$("#verify_buffer_gets").numberbox("textbox").attr("placeholder","1000");
	$("#verify_elapsed_time").textbox("textbox").attr("placeholder","3");
	
	$("#verify_buffer_gets").textbox("textbox").focus(function(){
		$("#verify_buffer_gets").numberbox("textbox").attr("placeholder","");
	});
	$("#verify_buffer_gets").textbox("textbox").blur(function(){
		$("#verify_buffer_gets").numberbox("textbox").attr("placeholder","1000");
	});
	
	$("#verify_elapsed_time").textbox("textbox").focus(function(){
		$("#verify_elapsed_time").numberbox("textbox").attr("placeholder","");
	});
	$("#verify_elapsed_time").textbox("textbox").blur(function(){
		$("#verify_elapsed_time").numberbox("textbox").attr("placeholder","3");
	});
	
	initPerfCheckResult();
	createList();
	
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked && $("#refresh").val() == "N" ) {
					$("#refresh").val("Y");
					
					if ( $('#submit_form #project_id_cd').combobox("getValue") == "" ) {
						parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					if ( $("#submit_form #sqlPerformanceP" ).textbox("getValue") == '' ) {
						parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					$("#timer_value").textbox({disabled:true});
					
					Btn_RefreshChange();
				} else {
					$("#refresh").val("N");
					$("#timer_value").textbox({disabled:false});
					
					window.clearTimeout(timer);
				}
			},300);
		}
	});
	
	document.getElementsByName('timer_value')[0].parentElement.firstChild.setAttribute('min','3');
	
	$("#sql_time_limt_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id="+$("#commonCode").val(),
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		panelHeight: 125,
		onLoadSuccess:function( item ) {
			$("#sql_time_limt_cd").combobox("setValue",item[0].cd);
		},
		onChange:function( newValue, oldValue ) {
			if ( newValue == "99" ) {
				$("#sql_time_limt_cd").combobox("setValue","");
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",false);
				
			} else {
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",true);
			}
			
		}
	});
	
	$(".sql_time_limt_cd .textbox").keyup(function() {
		
		if ( $("#sql_time_limt_cd").combobox("getText") != "" && !floatCheck( $("#sql_time_limt_cd").combobox("getText") ) ) {
			parent.$.messager.alert('경고','SQL Time Limit는 소수점을 입력할 수 없습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
		
		if ( $("#sql_time_limt_cd").combobox("getText") != "무제한" && $( ".sql_time_limt_cd .textbox-text").val() != "" && /^(\-|\+)?([0-9]+)$/.test( $( ".sql_time_limt_cd .textbox-text").val() ) == false ) {
			parent.$.messager.alert('경고','SQL Time Limit는 정수만 입력할 수 있습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
	});
	
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function sqlPerformancePac( projectId ) {
	// submit_form SQL점검팩 콤보
	$('#submit_form #sqlPerformanceP').combobox({
		url:"/DBChangePerformanceImpactAnalysisForTibero/getTobeSqlPerfPacName?project_id="+projectId
		+"&database_kinds_cd="+$("#database_kinds_cd").val(),
		method:"post",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onSelect: function( item ) {
			$("#submit_form #data_yn").val( item.data_yn );
			$("#submit_form #verify_project_id").val( item.verify_project_id );
			$("#submit_form #verify_sql_auto_perf_check_id").val( item.verify_sql_auto_perf_check_id );
		},
		onShowPanel: function() {
			let sqlAutoPerfId = $('#submit_form #sqlPerformanceP').combobox("getValue");
			
			$('#submit_form #sqlPerformanceP').combobox({
				url:"/DBChangePerformanceImpactAnalysisForTibero/getTobeSqlPerfPacName?project_id="+projectId
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
				method:"post",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onLoadSuccess: function() {
					$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
					
					if ( sqlAutoPerfId != '' && sqlAutoPerfId != null ) {
						$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfId );
					} else {
						$("#submit_form #sqlPerformanceP").combobox("setValue", "" );
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
			$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
}

function sqlTobePerfPac( projectId ) {
	$('#assign_form #sqlPerformanceP').combobox({
		url:"/DBChangePerformanceImpactAnalysisForTibero/getTobeSqlPerfPacName?project_id="+projectId
		+"&database_kinds_cd="+$("#database_kinds_cd").val(),
		method:"post",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onSelect: function(item) {
		},
		onLoadSuccess: function() {
			$("#assign_form #sqlPerformanceP").combobox("setValue", $("#submit_form #sql_auto_perf_check_id").val() );
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
}

function createList() {
	
	$("#tableNorthList").datagrid({
		view: myview,
		singleSelect: false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'sql_all_cnt',title:'전체 SQL수',width:'5%',halign:'center',align:'right',rowspan:2},
			{field:'tuning_selection_cnt',title:'튜닝선정',width:'5%',halign:'center',align:'right',rowspan:2},
			
			{title:'성능저하 SQLs',halign:'center',align:'center',colspan:2},
			{title:'튜닝 SQLs',halign:'center',align:'center',colspan:2},
			{title:'개선 실적 (평균 %)',halign:'center',align:'center',colspan:2},
			{title:'일괄검증 성능(평균 %)',halign:'center',align:'center',colspan:2, id:'colorId' },
			
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			],[
			{field:'elapsed_time_std_cnt',title:'수행시간',width:'5%',halign:'center',align:'right'},
			{field:'buffer_std_cnt',title:'버퍼',width:'5%',halign:'center',align:'right'},
			{field:'tuning_end_cnt',title:'완료',width:'5%',halign:'center',align:'right'},
			{field:'tuning_cnt',title:'진행중',width:'5%',halign:'center',align:'right'},
			
			{field:'tuning_elap_time_impr_ratio',title:'수행시간',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tuning_buffer_impr_ratio',title:'버퍼',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'verify_elap_time_impr_ratio',title:'수행시간',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter, id:'colorId1'},
			{field:'verify_buffer_impr_ratio',title:'버퍼',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter, id:'colorId2'},
			
			{field:'project_id',title:'project_id',hidden:'true'},
			{field:'sql_auto_perf_check_id',title:'sql_auto_perf_check_id',hidden:'true'},
			{field:'verity_project_id',title:'verity_project_id',hidden:'true'},
			{field:'veryfy_sql_auto_perf_check_id',title:'veryfy_sql_auto_perf_check_id',hidden:'true'}
		]],
		onSelect:function( index, row ) {
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #dbid").val(row.original_dbid);
			$("#submit_form #plan_hash_value").val(row.asis_plan_hash_value);
			$("#submit_form #sql_command_type_cd").val(row.sql_command_type_cd);
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#tableSouthList").datagrid({
		view: myview,
		singleSelect:false,
		checkOnSelect:true,
		selectOnCheck:true,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true",rowspan:2},
			
			{title:'일괄검증결과',halign:'center',align:'center',colspan:4},
			{title:'ORACLE',halign:'center',align:'center',colspan:4},
			{title:'TIBERO',halign:'center',align:'center',colspan:3},
			{title:'TUNING(TIBERO)',halign:'center',align:'center',colspan:2},
			{title:'일괄검증(TIBERO)',halign:'center',align:'center',colspan:3,id:'colorId3'},
			
			{field:'err_code',title:'에러코드',width:'5%',halign:"center",align:'left',rowspan:2},
			{field:'err_msg',title:'에러메시지',width: '10%',halign:"center",align:'left',rowspan:2},
			{field:'sql_text_web',title:'SQL TEXT',width: '15%',halign:"center",align:'left',rowspan:2},
			{field:'verify_tuning_no',title:'일괄검증<br>튜닝번호',width:'4%',halign:"center",align:'right',rowspan:2},
			{field:'before_tuning_no',title:'성능비교<br>튜닝번호',width:'4%',halign:"center",align:'right',rowspan:2},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left',rowspan:2},
			
			],[
			{field:'tuning_status_nm',title:'튜닝상태',width:'4%',halign:'center',align:'center'},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'4%',halign:'center',align:'center',styler:cellStyler},
			
			{field:'asis_sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left'},
			{field:'asis_plan_hash_value',title:'PLAN<br>HASH VALUE',width:'5%',halign:"center",align:'right'},
			{field:'asis_elapsed_time',title:'ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'asis_buffer_gets',title:'BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'tobe_plan_hash_value',title:'PLAN<br>HASH VALUE',width:'5%',halign:"center",align:'right'},
			{field:'tobe_elapsed_time',title:'ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'tuning_elapsed_time',title:'ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tuning_buffer_gets',title:'BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'verify_sql_id',title:'SQL ID',width:'6%',halign:"center",align:'left',id:'colorId4'},
			{field:'verify_elapsed_time',title:'ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter,id:'colorId5'},
			{field:'verify_buffer_gets',title:'BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter,id:'colorId6'},
			
			{field:'project_id',title:'project_id',hidden:'true'},
			{field:'sql_auto_perf_check_id',title:'sql_auto_perf_check_id',hidden:'true'},
			{field:'verity_project_id',title:'verity_project_id',hidden:'true'},
			{field:'veryfy_sql_auto_perf_check_id',title:'veryfy_sql_auto_perf_check_id',hidden:'true'}
		]],
		onSelect:function( index, row ) {
			$("#submit_form #sql_id").val(row.sql_id);
			$("#submit_form #dbid").val(row.original_dbid);
			$("#submit_form #plan_hash_value").val(row.asis_plan_hash_value);
			$("#submit_form #sql_command_type_cd").val(row.sql_command_type_cd);
			
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
	
	$("#colorId").css('background-color','#FACC2E');
	$("#colorId1").css('background-color','#FACC2E');
	$("#colorId2").css('background-color','#FACC2E');
	$("#colorId3").css('background-color','#FACC2E');
	$("#colorId4").css('background-color','#FACC2E');
	$("#colorId5").css('background-color','#FACC2E');
	$("#colorId6").css('background-color','#FACC2E');
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

//수행결과 초기화
function initPerfCheckResult() {
	$(".perf_check_result_blue").val("전체: 0");
	$(".perf_check_result_green").val("수행완료: 0");
	$(".perf_check_result_gray").val("수행중: 0");
	$(".perf_check_result_orange").val("오류: 0");
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
}

/* SQL-23 : 수행결과 조회 */
function Btn_RefreshChange() {
	var intSec = 0;
	
	Btn_LoadPerformanceCheckCount();
	
	if ( $("#refresh").val() == "Y" ) {
		intSec = strParseInt( $("#timer_value").textbox("getValue"),0 );
		timer = window.setTimeout("Btn_RefreshChange()",(intSec*1000));
	} else {
		
		window.clearTimeout(timer);
	}
}

// 검색 조회
function Btn_SqlAutoPerfSearch( num ) {
	if ( !checkCondition() ) {
		return;
	}
	
	if ( $("#assign_form #cntPage").val() != null && $("#assign_form #cntPage").val() != '' ) {
		$("#currentPage").val( $("#assign_form #cntPage").val() );
		$("#assign_form #cntPage").val("");
	} else {
		$("#currentPage").val("1");
	}
	
	$("#pagePerCount").val("10");
		
	$('#tableNorthList').datagrid("loadData", []);
	$('#tableSouthList').datagrid("loadData", []);
	
	if ( num == 1 ) {
		$("#submit_form #project_id").val( $("#submit_form #project_id_cd").combobox("getValue") );
		$("#submit_form #sql_auto_perf_check_id").val( $("#submit_form #sqlPerformanceP").combobox("getValue") );
	}
	
	// 수행중 & 수행한 내역 조회
	performedCheck( callback_SqlAutoPerfSearch );
	
	//수행결과조회
	Btn_LoadPerformanceCheckCount();
}

//페이지처리
function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallSouthTableList();
}

function ajaxCallNorthTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("일괄검증 결과 조회"," ");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadTuningBatchValidationNorthList",
			$("#submit_form"),
			"POST",
			callback_LoadTuningBatchValidationNorthListAction);
}

var callback_LoadTuningBatchValidationNorthListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableNorthList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}


function ajaxCallSouthTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("일괄검증 결과 조회"," ");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadTuningBatchValidationSouthList",
			$("#submit_form"),
			"POST",
			callback_LoadTuningBatchValidationSouthListAction);
}

var callback_LoadTuningBatchValidationSouthListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableSouthList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

// 수행결과 조회
function Btn_LoadPerformanceCheckCount() {
	/* modal progress open */
	if (parent.openMessageProgress != undefined) parent.openMessageProgress("수행결과 조회"," ");
	
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadTuningPerfResultCount",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

// 수행결과 조회 후 화면 update
var callback_LoadPerformanceCheckCountAction = function( rows ) {
	var data = JSON.parse(rows)[0];
	
	if ( typeof data == 'undefined' ) {
		/* modal progress close */
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		if ( data == null || data.length == 0 ) {
			$("#refresh").val("N");
			$('#chkRefresh').switchbutton("uncheck");
			window.clearTimeout(timer);
		}
		return;
	} else {
		
		$(".perf_check_result_blue").val(data.total_cnt);
		$(".perf_check_result_green").val(data.completed_cnt);
		$(".perf_check_result_gray").val(data.performing_cnt);
		$(".perf_check_result_orange").val(data.err_cnt);
		
		if ( data.perf_check_force_close_yn == 'N' ) {
			$("#perf_check_result_red").hide();
			
			if ( dateFormatValidationCheck( data.perf_check_error ) ) {
				$("#perf_check_result_violet").val("검증 완료");
				$("#perf_check_result_violet").show();
				$("#refresh").val("N");
				$('#chkRefresh').switchbutton("uncheck");
				window.clearTimeout(timer);
			} else {
				$("#perf_check_result_violet").hide();
			}
		} else {
			$("#perf_check_result_violet").hide();
			
			$(".perf_check_result_red").val("강제완료: " + data.perf_check_force_close_yn);
			$("#perf_check_result_red").show();
			$("#refresh").val("N");
			$('#chkRefresh').switchbutton("uncheck");
			window.clearTimeout(timer);
		}
		
		
	}
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function Btn_CollectiveVerification() {
	if ( !checkCondition() ) {
		return;
	}
	
	if ( $("#sql_time_limt_cd").combobox("getText").trim() == "" ) {
		parent.$.messager.alert('경고','SQL Time Limit를 입력해주세요','warning');
		$("#sql_time_limt_cd").combobox("setText","");
		return;
	}
	
	var param = "확인";
	var msgStr ="실행 하시겠습니까?";
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			$("#submit_form #project_id").val( $("#submit_form #project_id_cd").combobox("getValue") );
			$("#submit_form #sql_auto_perf_check_id").val( $("#submit_form #sqlPerformanceP").combobox("getValue") );
			
			ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/countTuningEndTms",
					$("#submit_form"),
					"POST",
					callback_countTuningEndAction);
			
		}
	});
}

var callback_countTuningEndAction = function( result ) {
	
	if ( result.result ) {
		
		var sqlTimeLimt_cd = $("#sql_time_limt_cd").combobox("getValue");
		
		if ( sqlTimeLimt_cd == "1" ) {
			$("#sql_time_limt_cd").combobox("setValue","01");
			$("#sql_time_direct_pref_value").val( '' );
		} else if ( sqlTimeLimt_cd != "01" && sqlTimeLimt_cd != "02" && sqlTimeLimt_cd != "03" && 
				sqlTimeLimt_cd != "04" && sqlTimeLimt_cd != "05" && sqlTimeLimt_cd != "98" ) {
			$("#sql_time_direct_pref_value").val( $("#sql_time_limt_cd").combobox("getText") );
		} else if ( sqlTimeLimt_cd == "98" ) {
			$("#sql_time_direct_pref_value").val( "0" );
		} else {
			$("#sql_time_direct_pref_value").val( '' );
		}
		
		// 수행중 & 수행한 내역 조회
		performedCheck( callback_CollectiveVerification );

	} else {
		parent.$.messager.alert('정보','해당 SQL점검팩에 튜닝 종료된 SQL이 없습니다.<br>튜닝 종료된 SQL에 대해서만 일괄 검증이 가능합니다.','warning');
	}
}

var callback_SqlAutoPerfSearch = function ( result ) {
	if ( result.txtValue == "true" ) {
		//grid조회
		ajaxCallNorthTableList();
		ajaxCallSouthTableList();
		$("#refresh").val("N");
	} else {
		parent.$.messager.alert('경고','해당 SQL점검팩에 대한 작업을 수행중입니다.<br>수행결과에서 진행상황을 확인하시고<br>일괄검증결과는 검증작업이 완료된 후 검색 가능합니다','warning');
	}
}

// 강제 완료처리
function Btn_ForceUpdateSqlAutoPerformanceCheck() {
	if ( !checkCondition() ) {
		return;
	}
	performedCheck( callback_ForceCountExecuteTmsAction );
}

var callback_ForceCountExecuteTmsAction = function(result) {
	if ( result.txtValue == 'false' ) {
		
		// 수행중일 경우
		var param = "확인";
		var msgStr = $('#submit_form #sqlPerformanceP').combobox('getText') + "에서 수행 중인 작업을 강제완료처리 하시겠습니까?";
		
		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("", "강제 완료 처리 중입니다.");
				
				ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/forceUpdateTuningSqlAutoPerf",
						$("#submit_form"),
						"POST",
						callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
			}
		});
	} else {
		parent.$.messager.alert('정보','해당 SQL점검팩에서 실행 중인 작업이 없습니다.','info');
	}
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('정보','해당 SQL점검팩에 대한 강제 완료 처리가 완료되었습니다.','info');
	} else {
		parent.$.messager.alert('정보','강제완료처리 하지 못했습니다.' , 'info');
	}
	
	Btn_RefreshChange();
}

var callback_CollectiveVerification = function( result ) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	// 검색조건 초기화
	searchCriteriaReset();
	
	$('#tableNorthList').datagrid("loadData", []);
	$('#tableSouthList').datagrid("loadData", []);
	
	if ( result.result && result.txtValue == "true" ) {
		// 01. 수행결과 초기화
		initPerfCheckResult();
		
		// 02. SQL 자동성능점검 수행
		updateTuningSqlAutoPerformance();
	} else {
		if ( result.txtValue == "true" && result.result == false ) {
			var param = "확인";
			var msgStr ="이전에 수행된 실행 정보가 있습니다.<br>재수행 시 이전 실행 정보가 삭제됩니다.<br> 계속 진행하시겠습니까?";
			
			parent.$.messager.confirm( param,msgStr,function(r) {
				if (r) {
					// 01. 수행결과 초기화
					initPerfCheckResult();
					
					// 02. SQL 자동성능점검 수행
					updateTuningSqlAutoPerformance();
				}
			});
		} else {
			parent.$.messager.alert('경고','해당 SQL점검팩에 대한 작업을 수행 중입니다.<br>수행결과에서 진행상황을 검색할 수 있습니다.','warning');
		}
	}
}

function updateTuningSqlAutoPerformance() {
	// 02. SQL 자동성능점검 수행
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/updateTuningSqlAutoPerformance",
			$("#submit_form"),
			"POST",
			callback_updateSqlAutoPerformanceAction);
}

function callback_updateSqlAutoPerformanceAction( result ) {
	if ( !result.result ) {
		parent.$.messager.alert('정보',result.message, 'warning');
		
		return false;
	} else {
		// 수행 결과 조회
		Btn_RefreshChange();
		
		// 수행중 & 수행한 내역 조회
		performedCheck( callback_SqlAutoPerfSearch );
		
	}
}

function initPopupSet(){
	// popup 초기화
	$('#assign_form #chkAutoShare').switchbutton("uncheck");
	$('#assign_form #chkExcept').switchbutton("uncheck");
	$('#assign_form #auto_share').val("N");
	$('#assign_form #selectTuner').combobox({readonly:false});
	$('#assign_form #selectTuner').combobox('setValue','');
	$("#assign_form #project_id").combobox('setValue','');
	$("#assign_form #sqlPerformanceP").combobox('setValue','');
	
};

//튜닝대상선정 popup
function showTuningReqPopup(){
	var tuningNoArry = "";
	var rows = $('#tableSouthList').datagrid('getChecked');
	
	if ( !checkCondition() ) {
		return;
	}
	
	if ( rows.length > 0 ) {
		
		// err_code로 판단
		if ( $("#assign_form #isAll").val() == "A" ) {
			ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadTuningBatchValidationSouthListAll",
					$("#submit_form"),
					"POST",
					callback_ReLoadPerfResultListAllErrChkAction );
		} else {
			var data = $('#tableSouthList').datagrid('getChecked');
			
			for ( var errChkIdx = 0; errChkIdx < data.length; errChkIdx++ ) {
				if ( data[errChkIdx].err_code != null && data[errChkIdx].err_code != "" ) {
					parent.$.messager.alert('경고','에러가 있는 SQL은 튜닝대상선정을 할 수 없습니다.','warning');
					return false;
				}
			}
			
			$('#tuningAssignPop').window({
				title : "튜닝 요청",
				top:getWindowTop(550),
				left:getWindowLeft(550)
			});
			
			$('#tuningAssignPop').window("open");
		}
		
		$('#assign_form #project_id').combobox({
			url:"/DBChangePerformanceImpactAnalysisForTibero/getProjectList",
			method:"get",
			valueField:'project_id',
			textField:'project_nm',
			onSelect: function(data) {
				// popup SQL점검팩 콤보
				sqlTobePerfPac( data.project_id );
			},
			onLoadSuccess: function() {
				$("#assign_form #project_id").combobox("textbox").attr("placeholder",'선택');
			},
			onLoadError: function() {
				parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
				return false;
			}
		});
		
		initPopupSet();
		
		$("#assign_form #project_id").combobox( "setValue", $("#submit_form #project_id").val() );
		$("#assign_form #sqlPerformanceP").combobox( "setValue", $("#submit_form #sql_auto_perf_check_id").val() );
		$("#assign_form #project_id").combobox("readonly","true");
		$("#assign_form #sqlPerformanceP").combobox("readonly","true");
		
		$("#dbid").val( rows[0].original_dbid );
		
		$('#assign_form #dbid').val("");
		$("#assign_form #sql_auto_perf_check_id").val("");
		
		$('#assign_form #tuningNoArry').val("");
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("M");
		
		const CHOICE_DIV_CD = "H";
		const TUNING_STATUS_CD = "3";
		
		$('#assign_form #dbid').val($('#dbid').val());
		$("#assign_form #choice_div_cd").val( CHOICE_DIV_CD );
		$("#assign_form #tuning_status_cd").val( TUNING_STATUS_CD );
		
		$("#assign_form #verify_project_id").val(rows[0].verify_project_id);
		$("#assign_form #verify_sql_auto_perf_check_id").val(rows[0].verify_sql_auto_perf_check_id);
		$("#assign_form #asis_sql_id").val(rows[0].asis_sql_id);
		$("#submit_form #choice_cnt").val(rows.length);
		
		// 튜닝 담당자 조회
		$('#assign_form #selectTuner').combobox({
			url:"/Common/getTuner?dbid="+$("#dbid").val(),
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

//tunerAssign_popup의 기능들은 tunerAssign_popup.js를 사용
//튜닝요청 및 담당자 지정만 재정의.
function Btn_SaveTuningDesignation(){
	var projectId = $('#assign_form #project_id').combobox('getValue');
	var sqlAutoPerfCheckId = $('#assign_form #sqlPerformanceP').combobox('getValue');
	
	$("#assign_form #sql_auto_perf_check_id").val( sqlAutoPerfCheckId );
	
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
	$('#assign_form #tuningNoArry').val("");
	
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
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadTuningBatchValidationSouthListAll",
				$("#submit_form"),
				"POST",
				callback_ReLoadPerfResultListAllAction)
	// 개별 check 시
	} else {
		var data = $('#tableSouthList').datagrid('getChecked');
		
		if ( $("#chkExcept").switchbutton("options").checked == true ) {
			// verify_tuning_no로 판단(일괄검증 튜닝번호)
			for ( var checkds = data.length-1; checkds > 0-1; checkds-- ) {
				if ( data[checkds].verify_tuning_no != null ) {
					data.splice( checkds, 1 );
				}
			}
			
			dataListCall( data );
		} else {
			dataListCall( data );
		}
	}
}

var callback_ReLoadPerfResultListAllErrChkAction = function ( result ) {
	var data = JSON.parse( result );
	var rows = $('#tableSouthList').datagrid('getChecked');
	
	for ( var checkdIdx = 0 ; checkdIdx < data.rows.length; checkdIdx++ ) {
		
		if ( data.rows[checkdIdx].err_code != null && data.rows[checkdIdx].err_code != "" ) {
			parent.$.messager.alert('경고','에러가 있는 SQL은 튜닝대상선정을 할 수 없습니다.','warning');
			return false;
		}
	}
	if ( rows.length > 0) {
		$('#tuningAssignPop').window({
			title : "튜닝 요청",
			top:getWindowTop(550),
			left:getWindowLeft(550)
		});
		
		$('#tuningAssignPop').window("open");
	}
	
}

var callback_ReLoadPerfResultListAllAction = function( result ) {
	
	var data = JSON.parse( result );
	
	dataListCall( data.rows );
}

//튜닝대상선정 작업
function dataListCall( data ) {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	if ( data.length > 0 ) {
		let sqlIdArry = "";
		let moduleArry = "";
		let tuningNoArry = "";
		let parsingSchemaNameArry = "";
		
		for ( var checkdListIdx = 0 ; checkdListIdx < data.length; checkdListIdx++ ) {
			sqlIdArry += data[checkdListIdx].asis_sql_id + "|";
			moduleArry += data[checkdListIdx].module + "|";
			tuningNoArry += data[checkdListIdx].before_tuning_no + "|";
			parsingSchemaNameArry += data[checkdListIdx].parsing_schema_name + "|";
		}
		
		$("#assign_form #module").val( strRight( moduleArry,1 ) );
		$("#assign_form #sqlIdArry").val( strRight( sqlIdArry,1 ) );
		$("#assign_form #tuningNoArry").val( strRight( tuningNoArry,1 ) );
		$("#assign_form #parsing_schema_name").val( strRight( parsingSchemaNameArry,1 ) );
		
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/Popup/insertSelectedTuningTarget",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		Btn_SqlAutoPerfSearch();
	}
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if ( result.result ) {
		/* modal progress close */
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
		
		//전체 판단 초기화
		$("#assign_form #isAll").val("");
		
		$.messager.alert('', msg ,'info',function(){
			setTimeout(function() {
				
				Btn_OnClosePopup('tuningAssignPop');
				Btn_SqlAutoPerfSearch();
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);
		});
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		Btn_SqlAutoPerfSearch();
	}
};

function checkCondition() {
	if ( $("#submit_form #project_id_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sqlPerformanceP" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	return true;
}

// 실수인지 체크
function floatCheck( obj ) {
	var fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;
	 
	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

function searchCriteriaReset(){
	// 검색조건 초기화
	$("#asis_perf_degrade_versus_yn").checkbox("uncheck");
	$("#tuning_perf_degrade_versus_yn").checkbox("uncheck");
	$("#error_yn").checkbox("uncheck");
	$("#verify_buffer_gets").numberbox("setValue","");
	$("#verify_elapsed_time").textbox("setValue","");
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

function performedCheck( callBackFunction ) {
	// 수행중 & 수행한 내역 조회
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/countTuningExecuteTms",
			$("#submit_form"),
			"POST",
			callBackFunction );
}

function Excel_Download_North() {
	if ( !checkCondition() ) {
		return;
	}
	
	let rows = $("#tableNorthList").datagrid('getData');
	if ( rows.rows.length <= 0) {
		parent.$.messager.alert('경고','데이터를 먼저 조회 해 주세요.','warning');
		return;
	}
		
	$("#submit_form").attr("action","/DBChangePerformanceImpactAnalysisForTibero/excelNorthDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function Excel_Download_South() {
	if ( !checkCondition() ) {
		return;
	}
	
	$("#submit_form").attr("action","/DBChangePerformanceImpactAnalysisForTibero/excelSouthDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function setValueNull( val, row ) {
	return '';
}

// 점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	if ( $("#submit_form #sqlPerformanceP").combobox("getValue") == sqlPerfId ) {
		$("#submit_form #sql_auto_perf_check_id").val("");
		$("#submit_form #sqlPerformanceP").combobox("setValue","");
	}
	
	var url = "/DBChangePerformanceImpactAnalysisForTibero/getTobeSqlPerfPacName?project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	
}