var isFirstLoading = 'true';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createTableList();
	
	$('#bottomSqlsTab').tabs({
		tabWidth: 160,
		onSelect: function(title, index) {
			let tab = $(this).tabs('getSelected');
			let hest = $(this).tabs('getTabIndex', tab);
			
			if($('#tableList').treegrid('getSelected') == null) {
				parent.$.messager.alert('','대상을 선택해 주세요.');
				
				return;
			}
			
			loadTabs(index);
		},
		onLoad: function(panel) {
		}
	});
	
	$('#bottomSqlsTab').tabs('select', 0);
	
	
	$('#selectSearchType').combobox({
		onLoadSuccess: function() {
			$("#selectSearchType").combobox("textbox").attr("placeholder",'선택');
		},
		onSelect:function(record) {
			let recValue = record.value;
			
			if(recValue == '01') {											// 성능 점검 SQL
				$('#is_regress').val('N');
				
				$('#selectSqlPerfTrace').combobox('setValue', '');
				$('#selectElapsedTimeMetirc').combobox('setValue', '');
				
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				$('#selectPerfRegressedMetric').combobox('readonly', true);
				$('#regress_type').css('display', 'inline-block');
				$('#sql_perf_trace').css('display', 'none');
				
				$('#checkImprove').checkbox('uncheck');
				$('#checkRegress').checkbox('uncheck');
				
				$('#is_regress').val('');
			} else if(recValue == '02') {									// 예외 처러 SQL
				$('#is_regress').val('');
				
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				
				$('#regress_type').css('display', 'none');
				$('#sql_perf_trace').css('display', 'inline-block');
				
				loadExceptionHandlingMethodCombobox();
			} else {														// 전체
				$('#is_regress').val('');
				
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				$('#selectSqlPerfTrace').combobox('setValue', '');
				$('#selectElapsedTimeMetirc').combobox('setValue', '');
				
				$('#sql_perf_trace').css('display', 'none');
				$('#regress_type').css('display', 'none');
			}
		}
	});
	
	$('#checkImprove').checkbox({		// 성능향상
		onChange:function(checked) {
			if(checked) {
				if($('#checkImprove').checkbox('options').checked == true) {	// 성능 향상만 조회
					$('#checkRegress').checkbox('uncheck');
				}
				
				$('#is_regress').val('N');
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				$('#selectPerfRegressedMetric').combobox('readonly', true);
			} else {
				if($('#checkRegress').checkbox('options').checked == false) {
					$('#is_regress').val('');
				}
			}
		}
	});
	
	$('#checkRegress').checkbox({		// 성능저하
		onChange:function(checked) {
			if(checked) {
				if($('#checkImprove').checkbox('options').checked == true) {	// 성능 저하만 조회
					$('#checkImprove').checkbox('uncheck');
				}
				
				$('#is_regress').val('Y');
				$('#selectPerfRegressedMetric').combobox('setValue', '');
				$('#selectPerfRegressedMetric').combobox('readonly', false);
			} else {
				if($('#checkImprove').checkbox('options').checked == false) {
					$('#is_regress').val('');
					$('#selectPerfRegressedMetric').combobox('setValue', '');
					$('#selectPerfRegressedMetric').combobox('readonly', true);
				}
			}
		}
	});
	
	$('#checkHighestRankYn').checkbox({
		onChange:function(val) {
			console.log('high:' + val);
			workjobComboTree(val);
		}
	});
	
	$('#begin_dt').datebox('clear');
	$('#end_dt').datebox('clear');
	
	$('#base_daily').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#base_weekly').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#base_monthly').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#checkFail').checkbox({
		onChange:function(val) {
			if(val) {
				if($('#checkPass').checkbox('options').checked == true) {
					$('#after_fail_yn_condition').val('');		// 전체
				} else {
					$('#after_fail_yn_condition').val('Y');		// 부적합
				}
			} else {
				if($('#checkPass').checkbox('options').checked == true) {
					$('#after_fail_yn_condition').val('N');		// 적합
				} else {
					$('#after_fail_yn_condition').val('');		// 전체
				}
			}
		}
	});
	
	$('#checkPass').checkbox({
		onChange:function(val) {
			if(val) {
				if($('#checkFail').checkbox('options').checked == true) {
					$('#after_fail_yn_condition').val('');		// 전체
				} else {
					$('#after_fail_yn_condition').val('N');		// 적합
				}
			} else {
				if($('#checkFail').checkbox('options').checked == true) {
					$('#after_fail_yn_condition').val('Y');		// 부적합
				} else {
					$('#after_fail_yn_condition').val('');		// 전체
				}
			}
		}
	});
	
	if($('#_isHandOff').val() == 'Y') {
		handOff();
	} else {
		$('#checkHighestRankYn').checkbox('check');
		$('#checkFail').checkbox('check');
		
		resetBasePeriod(2);
	}
	
	$('#btn_sqlInfo_pop').on('click',function(){
		createSqlInfoPopup();
	});
});

function loadExceptionHandlingMethodCombobox() {
	$('#selectSqlPerfTrace').combobox({
		url:"/Common/getCommonCodeRef2?isAll=Y&grp_cd_id=1061&ref_vl_1=SQL_PERF_TRACE",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess : function(){
			if($('#_isHandOff').val() == 'Y') {
				$(this).combobox('setValue', $('#_selectSqlPerfTrace').val());
				
				if ( $('#_selectElapsedTimeMetirc').val() != null 
						&& $('#_selectElapsedTimeMetirc').val() != "deploy_sql_cnt"
						&& $('#_selectElapsedTimeMetirc').val() != "sql_cnt" ) {
					$('#selectElapsedTimeMetirc').combobox('setValue', $('#_selectElapsedTimeMetirc').val());
				} else {
					$('#selectElapsedTimeMetirc').combobox('setValue',"");
				}
			}
		}
	});
}

function workjobComboTree(isTopLevel) {
	let workjobComboTreeUrl = '';
	
	if(isTopLevel) {
		workjobComboTreeUrl = '/Common/getWrkJobTopLevel';
	} else {
		workjobComboTreeUrl = '/Common/getWrkJobCd';
	}
	
//	$('#wrkjob_cd_top_level').combotree('clear');
	
	// 시스템 유형에 관계없이 업무구분 조회
	$('#wrkjob_cd_top_level').combotree({
		url:workjobComboTreeUrl,
		method:"get",
//		valueField:'id',
//		textField:'text',
		id: 'id',
		text: 'text',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(node, data){
			$('#wrkjob_cd').val('');
			combotreeSetValue();
			$("#submit_form #wrkjob_cd_top_level").combotree("textbox").attr("placeholder",'선택');
		}
	});
}

function combotreeSetValue() {
	if($('#_isHandOff').val() == 'Y') {
		if($('#checkHighestRankYn').checkbox('options').checked == true) {
			$('#wrkjob_cd_top_level').combotree('setValue', $('#wrkjob_cd').val());
		} else {
			$('#wrkjob_cd_top_level').combotree('setValue', $('#_wrkjob_cd').val());
			
			$('#_wrkjob_cd').val('');
		}
		
		$('#end_dt').datebox('setValue', $('#_end_dt').val());
		resetBasePeriod($('#_base_period_value').val());
	} else {
		$('#wrkjob_cd_top_level').combotree('setValue', '');
		
		if(isFirstLoading == 'true') {
			ajaxCall("/PerformanceCheckSql/baseDatePeriodOnly",
					$("#submit_form"),
					"POST",
					callback_BaseDatePeriodOnlyAction);
		}
	}
}

function resetBasePeriod(_base_period_value) {
	let base_period_value = Number(_base_period_value);
	$('#base_period_value').val(base_period_value);
	
	switch(base_period_value) {
	case 1:
		$('#base_daily').radiobutton({
			checked: true
		});
		
		$('#base_weekly').radiobutton({
			checked: false
		});
		
		$('#base_monthly').radiobutton({
			checked: false
		});
		break;
	case 2:
		$('#base_daily').radiobutton({
			checked: false
		});
		
		$('#base_weekly').radiobutton({
			checked: true
		});
		
		$('#base_monthly').radiobutton({
			checked: false
		});
		break;
	case 3:
		$('#base_daily').radiobutton({
			checked: false
		});
		
		$('#base_weekly').radiobutton({
			checked: false
		});
		
		$('#base_monthly').radiobutton({
			checked: true
		});
		break;
	}
	
	if($('#_isHandOff').val() == 'Y' && isFirstLoading == 'true') {
		$('#begin_dt').datebox('setValue', $('#_begin_dt').val());
		
		Btn_OnClick();
	} else {
		ajaxCall("/PerformanceCheckSql/baseDatePeriodOnly",
				$("#submit_form"),
				"POST",
				callback_BaseDatePeriodOnlyAction);
	}
	
	isFirstLoading = 'false';
	$('#_isHandOff').val('N');
}

var callback_BaseDateAction = function(result) {
	let data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','기준일자를 조회하지 못하였습니다.');
		return false;
	}
	
	let row = data[0];
	
	$('#begin_dt').datebox('setValue', row.begin_dt);
	$('#end_dt').datebox('setValue', row.end_dt);
	
	let finalDistributionDate = row.deploy_complete_dt;
	
	if(finalDistributionDate === 'N/A') {
		finalDistributionDate = '최종 배포일 : ' + finalDistributionDate;
	} else {
		finalDistributionDate = '최종 배포일 : ' + finalDistributionDate + " 일일 기준";
	}
	
	$('#finalDistributionDate').empty();
	$('#finalDistributionDate').append(finalDistributionDate);
	
	if($('#_isHandOff').val() == 'Y') {
		$('#_isHandOff').val('N');
		
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		Btn_OnClick();
	}
}

var callback_BaseDatePeriodOnlyAction = function(result) {
	let data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','기준일자를 조회하지 못하였습니다.');
		return false;
	}
	
	let row = data[0];
	
	$('#begin_dt').datebox('setValue', row.begin_dt);
	$('#end_dt').datebox('setValue', row.end_dt);
}

var callback_BaseDateConditionAction = function(result) {
	let data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','최종 배포일을 조회하지 못하였습니다.');
		return false;
	}
	
	let row = data[0];
	
	let finalDistributionDate = row.deploy_complete_dt;
	
	if(finalDistributionDate === 'N/A') {
		finalDistributionDate = '최종 배포일 : ' + finalDistributionDate;
	} else {
		finalDistributionDate = '최종 배포일 : ' + finalDistributionDate + " 일일 기준";
	}
	
	$('#finalDistributionDate').empty();
	$('#finalDistributionDate').append(finalDistributionDate);
	
	Btn_OnClick();
}

var length = 70;

function createTableList() {
	$("#tableList").treegrid({
		fitColumns: true,
		singleSelect: true,
		selectOnCheck: false,
		checkOnSelect: false,
		autoRowHeight: false,
		idField: 'id',
		treeField: 'dbio',
		lines: true,
		columns:[[
			{field:'id',rowspan:2, hidden:true},
			{field:'parent_id',rowspan:2, hidden:true},
			{field:'chk',halign:"center",align:"center",checkbox:"true",width:"100px",rowspan:2},
			{field:'dbio',title:'SQL 식별자</br>(DBIO)',halign:"center",align:'left',
				styler: function(value,row,index){
					let lengths = value.length * 7;
					lengths += (row.children.length + 1)*30;
					
					if ( length < lengths ) {
						length = lengths;
					}
					console.log("length ======> "+ length );
					if ( length > 450 ){
						return 'width:450px;';
					} else {
						return 'width:'+ length +'px;';
					}
			},rowspan:2},
			{field:'program_nm',title:'프로그램',halign:"center",align:'left',width:"90px",rowspan:2},
			{field:'after_prd_sql_id',title:'SQL ID',halign:"center",align:'left',width:"100px",rowspan:2},
			{field:'after_prd_plan_hash_value',title:'PLAN</br>HASH VALUE',halign:"center",align:'right',width:"100px",rowspan:2},
//			{field:'prd_plan_change_yn',title:'Plan</br>변경</br>여부',halign:"center",align:'center',width:"50px",rowspan:2},
			{title:'평균 수행시간(초)',halign:"center",colspan:3},
			{title:'평균 블럭수',halign:"center",colspan:3},
			{title:'평균 처리건수',halign:"center",colspan:2},
			{field:'after_executions',title:'수행횟수',halign:"center",align:'right',width:"80px",rowspan:2},
			{field:'exception_prc_meth_nm',title:'에외</br>방법',halign:"center",align:'center',width:"45px",rowspan:2},
			{field:'after_fail_yn',title:'부적합</br>여부',halign:"center",align:'center',width:"45px",rowspan:2,styler:reverseStyler},
//			{field:'before_fail_yn',title:'이전</br>부적합</br>여부',halign:"center",align:'center',width:"50px",rowspan:2,styler:reverseStyler},
			{field:'program_exec_dt',title:'성능검증일자',halign:"center",align:'center',width:"80px",rowspan:2},
			{field:'deploy_complete_dt',title:'배포일자',halign:"center",align:'center',width:"80px",rowspan:2},
			{field:'perf_regressed_metric',hidden:'true',rowspan:2},
			{field:'elapsed_time_metirc',hidden:'true',rowspan:2},
			{field:'exception_prc_meth_cd',hidden:'true',rowspan:2},
			{field:'test_sql_id',hidden:'true',rowspan:2},
			{field:'test_plan_hash_value',hidden:'true',rowspan:2},
			{field:'before_prd_sql_id',hidden:'true',rowspan:2},
			{field:'before_prd_plan_hash_value',hidden:'true',rowspan:2},
			{field:'elapsed_time_regress_yn',hidden:'true',rowspan:2},
			{field:'buffer_gets_regress_yn',hidden:'true',rowspan:2},
			{field:'prd_buffer_gets_increase_ratio',hidden:'true',rowspan:2},
			{field:'prd_elap_time_increase_ratio',hidden:'true',rowspan:2},
			{field:'prd_rows_proc_increase_ratio',hidden:'true',rowspan:2},
			{field:'wrkjob_cd',hidden:'true',rowspan:2},
			{field:'top_wrkjob_cd',hidden:'true',rowspan:2},
			{field:'dbid',hidden:'true',rowspan:2},
			{field:'program_id',hidden:'true',rowspan:2},
			{field:'perf_check_id',hidden:'true',rowspan:2},
			{field:'perf_check_step_id',hidden:'true',rowspan:2},
			{field:'program_execute_tms',hidden:'true',rowspan:2},
			{field:'before_prd_elapsed_time',hidden:'true',rowspan:2},
			{field:'before_prd_buffer_gets',hidden:'true',rowspan:2},
			{field:'before_prd_rows_processed',hidden:'true',rowspan:2},
		],[
			{field:'test_elapsed_time',title:'배포',halign:"center",align:'right',width:"70px"},
			{field:'after_prd_elapsed_time',title:'운영',halign:"center",align:'right',width:"70px",formatter:formatElapsedTime},
			{field:'elapsed_time_activity',title:'Activity(%)',halign:"center",align:'right',width:"70px",styler:gradientStyler},
			{field:'test_buffer_gets',title:'배포',halign:"center",align:'right',width:"70px"},
			{field:'after_prd_buffer_gets',title:'운영',halign:"center",align:'right',width:"70px",formatter:formatBufferGets},
			{field:'buffer_gets_activity',title:'Activity(%)',halign:"center",align:'right',width:"70px",styler:gradientStyler},
			{field:'test_rows_processed',title:'배포',halign:"center",align:'right',width:"70px"},
			{field:'after_prd_rows_processed',title:'운영',halign:"center",align:'right',width:"70px"},
			{field:'except_yn',title:'에외</br>여부',hidden:true},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data) {
			let opts = $(this).datagrid('options');

			if(data != null && data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		onSelect:function(row) {
			$('#bottomSqlsTab').tabs('select', 0);
			
			loadSqlPerformanceTrace(row);
			
			$('#compare_form [name=after_prd_sql_id]').val(row.after_prd_sql_id);
			$('#compare_form [name=after_prd_plan_hash_value]').val(row.after_prd_plan_hash_value);
			$('#compare_form [name=dbid]').val(row.dbid);
			$('#compare_form [name=program_id]').val(row.program_id);
			$('#compare_form [name=perf_check_id]').val(row.perf_check_id);
			$('#compare_form [name=perf_check_step_id]').val(row.perf_check_step_id);
			$('#compare_form [name=program_execute_tms]').val(row.program_execute_tms);
		}
	});
}

function gradientStyler(value,row,index){
	if(value >= 100){
		return 'background-color:#15ae57;';
	}else if(value < 100){
		let colorVal = 100 - value;
		
		return 'background: linear-gradient(90deg, #ffffff '+ colorVal +'%, #15ae57 '+ colorVal+'%);';
	}
}

function reverseStyler(value,row,index) {
	if(value == 'Y') {
		return 'color:red';
	}
}

function parentStyler(value,row,index){
	if(typeof(row._parentId) == 'undefined') {
		return 'background-color:#deebf7;';
	}
}

var fontStyle = "font-size: 11px;font-family: 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic', 'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic;";
var formatStyle = "float: left;margin-top: -4px;";

function formatElapsedTime(value,row,index) {
	let format = '';
	
	if(typeof(value) == 'undefined') {
		format = '<label/>';
	} else {
		if(row.test_elapsed_time !== null) {
			if(row.elapsed_time_regress_yn == 'Y') {
				format = '<img src="/resources/images/down_arrow.png" style="' + formatStyle + '"/>';
			} else {
				format = '<img src="/resources/images/up_arrow.png" style="' + formatStyle + '"/>';
			}
		}
		
		format += '<label style="margin-right:0px;' + fontStyle + '">' + value + '</label>';
	}
	
	return format;
}

function formatBufferGets(value,row,index) {
	let format = '';
	
	if(typeof(value) == 'undefined') {
		format = '<label/>';
	} else {
		if(row.test_buffer_gets !== null) {
			if(row.buffer_gets_regress_yn == 'Y') {
				format = '<img src="/resources/images/down_arrow.png" style="' + formatStyle + '"/>';
			} else {
				format = '<img src="/resources/images/up_arrow.png" style="' + formatStyle + '"/>';
			}
		}
		
		format += '<label style="margin-right:0px;' + fontStyle + '">' + value + '</label>';
	}
	
	return format;
}

function handOff() {
	try {
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val()," ");
		
		if($('#_select_search_type').val() == '01') {
			$('#selectSearchType').combobox('setValue', $('#_select_search_type').val());
			
			if ( $('#_select_perf_regressed_metric').val() != null
					&& $('#_select_perf_regressed_metric').val() != "deploy_sql_cnt"
					&& $('#_select_perf_regressed_metric').val() != "sql_cnt" ) {
				$('#selectPerfRegressedMetric').combobox('setValue', $('#_select_perf_regressed_metric').val());
			} else {
				$('#selectPerfRegressedMetric').combobox('setValue',"");
			}
			
		} else if($('#_select_search_type').val() == '02') {
			$('#selectSearchType').combobox('setValue', $('#_select_search_type').val());
			$('#selectSqlPerfTrace').combobox('setValue', $('#_selectSqlPerfTrace').val());
			
			if ( $('#_selectElapsedTimeMetirc').val() != null
					&& $('#_selectElapsedTimeMetirc').val() != "deploy_sql_cnt"
					&& $('#_selectElapsedTimeMetirc').val() != "sql_cnt" ) {
				$('#selectElapsedTimeMetirc').combobox('setValue', $('#_selectElapsedTimeMetirc').val());
			} else {
				$('#selectElapsedTimeMetirc').combobox('setValue',"");
			}
		}
		
		if($('#_is_check_fail').val() == 'Y') {
			$('#checkFail').checkbox('check');
		} else {
			$('#checkFail').checkbox('uncheck');
			
			if($('#_is_check_pass').val() == 'Y') {
				$('#checkPass').checkbox('check');
			}
		}
		
		$('#checkHighestRankYn').checkbox('uncheck');
		
		workjobComboTree(false);
		
		if($('#_isRegressYn').val() == '') {			// 성능 점검 SQL > SQL수
			$('#is_regress').val('');
		} else if($('#_isRegressYn').val() == 'Y') {	// 성능 점검 SQL > 성능 저하, 성능 저하(배)
			$('#is_regress').val('Y');
			$('#checkRegress').checkbox('check');
			if ( $('#_select_perf_regressed_metric').val() != null 
					&& $('#_select_perf_regressed_metric').val() != "deploy_sql_cnt"
					&& $('#_select_perf_regressed_metric').val() != "sql_cnt" ) {
				$('#selectPerfRegressedMetric').combobox('setValue', $('#_select_perf_regressed_metric').val());
			} else {
				$('#selectPerfRegressedMetric').combobox('setValue',"");
			}
		} else {										// 성능 점검 SQL > 성능 향상
			$('#is_regress').val('N');
			$('#checkImprove').checkbox('check');
		}
	} catch(error) {
		parent.$.messager.alert('Function ready', error.getMessage());
		return false;
	}
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallTableList();
}

function Btn_OnClick(){
	if(!isValidationCheck()) {
		return;
	}
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("10");
	
	ajaxCallTableList();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
}

function isValidationCheck() {
	if( ($('#checkHighestRankYn').checkbox('options').checked == false) &&
			$('#wrkjob_cd_top_level').combotree('getValue') == null ||
			($('#wrkjob_cd_top_level').combotree('getValue') == '') ) {
		parent.$.messager.alert('경고','업무를 선택해 주세요','warning');
		return false;
	}
	
	if( $('#selectSearchType').combobox('getValue') == null ||
			$('#selectSearchType').combobox('getValue').trim() == '') {
		parent.$.messager.alert('경고','검색조건을 선택해 주세요','warning');
		return false;
	}
	
	return true;
}

function ajaxCallTableList(){
	$('#wrkjob_cd').val($('#wrkjob_cd_top_level').combotree('getValue'));
	
	$('#tableList').treegrid("loadData", []);
	
	ajaxCall("/RunSqlPerformanceChangeTracking/loadAutoSqls",
			$("#submit_form"),
			"POST",
			callback_LoadSqlsAction);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val()," ");
}

var callback_LoadSqlsAction = function(result) {
	try {
		let data = JSON.parse(result);
		var dataLength = data.dataCount4NextBtn;
//		let leng = 0;
//		
//		for (var i = 0; i < data.rows.length; i++) {
//			let datalength = (data.rows[i].dbio.length*7) + 
//			((data.rows[i].children.length + 1) * 30);
//			if ( datalength > leng ) {
//				leng = datalength;
//			}
//		}
		
		json_string_treegrid_callback_common(result,'#tableList',true);
		
		fnEnableDisablePagingBtn(dataLength);
	} catch(err) {
		console.error(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function showTuningReqPopup(){
	let dbidArray = "";
	let sqlIdArray = "";
	let planHashValueArray = "";
	let perfCheckStepIdArray = "";
	let wrkjobCdArray = "";
	let executionsArray = "";
	let avgBufferGetsArray = "";
	let avgElapsedTimeArray = "";
	let avgRowProcessedArray = "";
	let perfCheckIdArray = "";
	let programIdArray = "";
	let tuningNoArry = "";
	let dbioArray = "";
	let rows = "";
	
	rows = $('#tableList').treegrid('getChecked');
	
	if(rows == null || rows.length == 0) {
		parent.$.messager.alert('','튜닝 대상을 체크해 주세요.');
		return;
	}
	
	$("#dbid").val(rows[0].dbid);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	initPopupSet();  //튜닝 요청 버그를 방지하기위해 초기화.
	$('#tuningAssignPopSqls').window("open");
	
	$("#assign_form #choice_div_cd").val( $("#submit_form #choice_div_cd").val() );
	$('#assign_form #dbid').val("");
	$('#assign_form #auto_share').val("N");
	$('#assign_form #sqlIdArray').val("");
	$('#assign_form #tuningNoArry').val("");
	$('#assign_form #perfr_id').val("");
	$('#assign_form #strGubun').val("M");
	
	for(var i = 0 ; i < rows.length ; i++){
		dbidArray += rows[i].dbid + "[]";
		sqlIdArray += rows[i].after_prd_sql_id + "[]";
		planHashValueArray += rows[i].after_prd_plan_hash_value + "[]";
		perfCheckStepIdArray += rows[i].perf_check_step_id + "[]";
		wrkjobCdArray += rows[i].wrkjob_cd + "[]";
		executionsArray += rows[i].after_executions + "[]";
		avgBufferGetsArray += rows[i].after_prd_buffer_gets + "[]";
		avgElapsedTimeArray += rows[i].after_prd_elapsed_time + "[]";
		avgRowProcessedArray += rows[i].after_prd_rows_processed + "[]";
		perfCheckIdArray += rows[i].perf_check_id + "[]";
		programIdArray += rows[i].program_id + "[]";
		dbioArray += rows[i].dbio + "[]";
	}
	
	$('#assign_form #dbid_array').val(strRight(dbidArray, 2));
	$('#assign_form #after_prd_sql_id_array').val(strRight(sqlIdArray,2));
	$('#assign_form #after_prd_plan_hash_value_array').val(strRight(planHashValueArray, 2));
	$('#assign_form #perf_check_step_id_array').val(strRight(perfCheckStepIdArray, 2));
	$('#assign_form #wrkjob_cd_array').val(strRight(wrkjobCdArray, 2));
	$('#assign_form #executions_array').val(strRight(executionsArray, 2));
	$('#assign_form #avg_buffer_gets_array').val(strRight(avgBufferGetsArray, 2));
	$('#assign_form #avg_elapsed_time_array').val(strRight(avgElapsedTimeArray, 2));
	$('#assign_form #avg_row_processed_array').val(strRight(avgRowProcessedArray, 2));
	$('#assign_form #perf_check_id_array').val(strRight(perfCheckIdArray, 2));
	$('#assign_form #program_id_array').val(strRight(programIdArray, 2));
	$('#assign_form #dbio_array').val(strRight(dbioArray, 2));
	
	// 튜닝 담당자 조회
	$('#assign_form #selectTuner').combobox({
		url:"/Common/getTuner?dbid="+$("#dbid").val(),
		method:"get",
		valueField:'tuner_id',
		textField:'tuner_nm',
		onLoadSuccess : function(){
			$('#assign_form #selectTuner').combobox("textbox").attr("placeholder",'선택');
		}
	});
}

function Excel_Download(){
	if(!isValidationCheck()) {
		return;
	}
	
	$("#submit_form").attr("action","/RunSqlPerformanceChangeTracking/excelAutoDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function loadSqlPerformanceTrace(row) {
	let tab = $('#bottomSqlsTab').tabs('getSelected');
	
	if($('#bottomSqlsTab').tabs('getTabIndex', tab) == 0) {
		document.getElementById('bottomSqlsTabDesignIF1').src="/RunSqlPerformanceChangeTracking/sqlAutoPerformanceTrace?menu_id=" + $('#menu_id').val();
	} else {
		loadTabs(0);
	}
}

function loadTabs(index) {
	switch(index) {
	case 0:
		document.getElementById('bottomSqlsTabDesignIF1').src="/RunSqlPerformanceChangeTracking/sqlAutoPerformanceTrace?menu_id=" + $('#menu_id').val();
		$('#btn_sqlInfo_pop').css('visibility', 'hidden');
		break;
	case 1:
		document.getElementById('bottomSqlsTabDesignIF2').src="/RunSqlPerformanceChangeTracking/sqlAutoPerfInfo";
		$('#btn_sqlInfo_pop').css('visibility', 'visible');
		break;
	case 2:
		document.getElementById('bottomSqlsTabDesignIF3').src="/RunSqlPerformanceChangeTracking/sqlsAutoPerformanceCheckResult";
		$('#btn_sqlInfo_pop').css('visibility', 'hidden');
		break;
	case 3:
		loadSqlsProgramInfo();
		$('#btn_sqlInfo_pop').css('visibility', 'hidden');
		break;
	default:
		break;
	}
}

function loadSqlsProgramInfo() {
	let data = $("#tableList").treegrid('getSelected');
	
	createPerfChkResultTab2("program_id=" + data.program_id + "&perf_check_id=" + data.perf_check_id);
}

function convertPerfChkResult() {
	let html = $('#html').val();
	let beginBodyIndex = html.indexOf('<body>');
	let endBodyIndex = html.indexOf('</body>');
	
	html = '<html><head></head>\n' +
			html.substring(beginBodyIndex, endBodyIndex + 7) + '\n' +
			'</html>';
	
	$('#sqlsProgramInfo').html(html);
	
	$("#html").val("");
}

function createPerfChkResultTab2(param){
	$.ajax({
		url: '/Sqls/loadPerfCheckAllPgm',
		data: param,
		type: 'post',
		success : function(result) {
			$("#html").val(result);
			
			convertPerfChkResult();
		}
	});
}
function createSqlInfoPopup(){
	let row = $("#tableList").datagrid('getSelected');
	
	$('#sqlInfoPop').window({
		title : "SQL Info( "+row.after_prd_sql_id+" )",
		top:getWindowTop(550),
		left:getWindowLeft(1070)
	});
	
//	createSqlPlanPerformanceCheck();
	createSqlPlanOperation();
	
//	loadSqlInfoPerformanceCheck();
	loadSqlInfoOperation();
	
	$('#sqlInfoPop').window('open');  // open a window
	
	$('#pop_tabs').tabs('select', 0);
}

function loadSqlInfoOperation() {
	$('#sql_plan_operation').datagrid("loadData", []);
	
	ajaxCall("/Sqls/sqlTextAll",
			$("#compare_form"),
			"POST",
			callback_SqlTextAll);
	
	ajaxCall("/Sqls/sqlBindOperation",
			$("#compare_form"),
			"POST",
			callback_SqlBindOperationAction);
	
	ajaxCall("/Sqls/sqlPlanOperation",
			$("#compare_form"),
			"POST",
			callback_SqlPlanOperationAction);
}


function createSqlPlanOperation() {
	$("#sql_plan_operation").datagrid({
		view: myview,
		singleSelect: true,
		//striped: true,
		columns:[[
			{field:'cost_percent',title:'COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler},
			{field:'cpu_cost_percent',title:'CPU_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler},
			{field:'io_cost_percent',title:'IO_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler},
			{field:'id',title:'ID',halign:"center",align:'left',width:'25px'},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',width:'370px',formatter:toNbspFromWithespace},
			{field:'cost',title:'COST',halign:"center",align:'right',width:'73px'},
			{field:'cpu_cost',title:'CPU_COST',halign:"center",align:'right',width:'73px'},
			{field:'io_cost',title:'IO_COST',halign:"center",align:'right',width:'73px'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function diffStyle1(value,row,index){
	let diffTag = row.diffTag;
	
	if(diffTag == 'CHANGE' || diffTag == 'DELETE'){
		return 'background-color: #FAEBD9; color:#BB0000;';
	}else if(diffTag == 'INSERT'){
		return 'background-color: #F2F2F2';
	}else {
		return 'background-color: none';;
	}
}
function diffStyle2(value,row,index){
	let diffTag = row.diffTag;
	
	if(diffTag == 'CHANGE' || diffTag == 'INSERT'){
		return 'background-color: #FAEBD9; color:#BB0000;';
	}else if(diffTag == 'DELETE'){
		return 'background-color: #F2F2F2';
	}else {
		return 'background-color: none';;
	}
}
function cellStyler(value,row,index){
	var color = '#ffe48d';	//노랑
	//var color = '#efbd7f';		//주황
	
	if(value >= 100){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);';
	}else if(value < 100){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);';
	}
}

var callback_SqlTextAll = function(result) {
	try {
		if(result.result){
			var post = result.object;
			//$("#textArea").html(strReplace(post.sql_text, "\r\n", "<br/>"));
			if(post != null){
				//$('#sql_text_operation').textbox('setValue', post[0].sql_text);
				$('#sql_text_operation').val(post[0].sql_text);
				$('#sql_text_operation').height( $('#sql_text_operation').prop('scrollHeight') );
			}
		}else{
			parent.$.messager.alert('SQL TEXT',result.message);
		}
	} catch(err) {
		parent.$.messager.alert('에러', '운영 SQL - SQL TEXT</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_SqlBindOperationAction = function(result) {
	try {
		let bind = '';
		let data = JSON.parse(result);
		
		if(data.rows == null || data.rows.length == 0) {
			//$('#sql_bind_operation').textbox('setValue', "검색된 데이터가 없습니다.");
			$('#sql_bind_operation').val("검색된 데이터가 없습니다.");
			
			return;
		}
		
		let rows = data.rows;
		let row;
		for(let bindIndex = 0; bindIndex < rows.length; bindIndex++) {
			if(bindIndex > 0) {
				bind += '\n';
			}
			
			row = rows[bindIndex];
			bind += row.name + " " + row.value_string;
		}
		
		//$('#sql_bind_operation').textbox('setValue', bind);
		$('#sql_bind_operation').val(bind);
	} catch(err) {
		parent.$.messager.alert('에러', '운영 SQL - SQL BIND</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}
var callback_SqlPlanOperationAction = function(result) {
	json_string_callback_common(result,'#sql_plan_operation',true);
}
function Btn_CloseSqlInfoPop() {
	
	Btn_OnClosePopup('sqlInfoPop');
}
