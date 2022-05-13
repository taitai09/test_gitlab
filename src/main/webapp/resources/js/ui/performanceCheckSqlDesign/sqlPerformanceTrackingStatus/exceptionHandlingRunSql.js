var isFirstLoading = 'true';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#checkHighestRankYn').checkbox({
		onChange:function(val) {
			workjobComboTree(val);
		}
	});
	
	createTreeList();
	
	$('#selectSqlPerfTrace').combobox({
		url:"/Common/getCommonCodeRef2?isAll=Y&grp_cd_id=1061&ref_vl_1=SQL_PERF_TRACE",
		method:"get",
		valueField:'cd',
		textField:'cd_nm'
	})
	
	$('#checkHighestRankYn').checkbox('check');
	
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
	
	resetBasePeriod(2);
});

function workjobComboTree(isTopLevel) {
	let workjobComboTreeUrl = '';
	
	if(isTopLevel) {
		workjobComboTreeUrl = '/Common/getWrkJobTopLevel';
	} else {
		workjobComboTreeUrl = '/Common/getWrkJobCd';
	}
	
	
	// 시스템 유형에 관계없이 업무구분 조회
	$('#wrkjob_cd_top_level').combotree({
		url:workjobComboTreeUrl,
		method:"get",
		valueField:'id',
		textField:'text',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(node, data){
			$('#wrkjob_cd').val('');
			combotreeSetValue();
			$("#submit_form #wrkjob_cd_top_level").combotree("textbox").attr("placeholder",'선택');
		},
		onChange: function(newValue, oldValue) {
			$('#wrkjob_cd').val(newValue);
		}
	});
}

function combotreeSetValue() {
	$('#wrkjob_cd_top_level').combotree('setValue', '');
}

function resetBasePeriod(base_period_value) {
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
	
	if(isFirstLoading == 'true') {
		ajaxCall("/RunSqlPerformanceChangeTracking/baseDate",
				$("#submit_form"),
				"POST",
				callback_BaseDateAction);
	} else {
		ajaxCall("/RunSqlPerformanceChangeTracking/baseDatePeriodOnly",
				$("#submit_form"),
				"POST",
				callback_BaseDatePeriodOnlyAction);
	}
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
	
	if(isFirstLoading == 'true') {
		isFirstLoading = 'false';
		
		search(false);
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

function createTreeList() {
	$("#treeList").treegrid({
		singleSelect: true,
		//animate: true,
		idField: 'wrkjob_cd',
		treeField: 'wrkjob_cd_nm',
		autoRowHeight: false,
		columns:[[
			{field:'wrkjob_cd_nm',title:'업무',width:'400px',halign:"center",align:'left',rowspan:2},
			{field:'wrkjob_cd',rowspan:2,hidden:true},
			{field:'deploy_sql_cnt',title:'배포 SQL수',width:'100px',halign:"center",align:'right',rowspan:2,styler:parentStyler},
			{field:'sql_cnt',title:'실행 SQL수',width:'100px',halign:"center",align:'right',rowspan:2,styler:parentStyler},
			{field:'pass',title:'적합',width:'100px',halign:"center",align:'right',rowspan:2,styler:parentStyler},
			{field:'fail',title:'부적합',width:'100px',halign:"center",align:'right',rowspan:2,formatter:formatFail,styler:parentStyler},
			{title:'수행시간(초)',halign:"center",width:'700px',colspan:7}
		],[
			{field:'less_than_0_dot_1',title:'<= 0.1s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'less_than_0_dot_3',title:'< 0.3s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'less_than_1',title:'< 1s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'less_than_3',title:'< 3s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'less_than_10',title:'< 10s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'less_than_60',title:'< 60s',width:'100px',halign:"center",align:'right',styler:parentStyler},
			{field:'more_than_60',title:'>= 60s',width:'100px',halign:"center",align:'right',styler:parentStyler},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onBeforeLoad: function(row,param){
			if (!row) {    // load top level rows
				param.id = 0;    // set id=0, indicate to load new page rows
			}
		},
		onClickCell:function(field, row) {
			if(field == 'wrkjob_cd_nm' || field == 'deploy_sql_cnt' ) {
				return;
			}
			
			let clm = field;
			let clmValue = row[field];
			let param = '';
			let isCheckHighestRank;
			let wrkjob_cd = row['wrkjob_cd'];
			let begin_dt = $('#begin_dt').datebox('getValue');
			let end_dt = $('#end_dt').datebox('getValue');
			let after_fail_yn_condition = 'N';	/* 부적합 */
			let isCheckPass = 'N';				/* SQLs 탭의 적합 체크 박스, 적합 컬럼만 'Y'로 전달 */
			
			let selectSqlPerfTrace;				/* 예외처리방법 : 예외처리SQL 탭에서 전달받은경우*/
			let selectElapsedTimeMetirc;		/* 예외처리SQL - 콤보 */
			let selectSearchType = '02';
			
			if($('#checkHighestRankYn').checkbox('options').checked) {
				isCheckHighestRank = 'Y';
			} else {
				isCheckHighestRank = 'N';
			}
			
			switch(field) {
			case 'deploy_sql_cnt':
				selectElapsedTimeMetirc = '';
				break;
			case 'sql_cnt':
				selectElapsedTimeMetirc = '';
				break;
			case 'pass':
				isCheckPass = 'Y';
				selectElapsedTimeMetirc = '';
				break;
			case 'fail':
				after_fail_yn_condition = 'Y';
				selectElapsedTimeMetirc = '';
				break;
			case 'less_than_0_dot_1':
				selectElapsedTimeMetirc = '0.1XLT';
				break;
			case 'less_than_0_dot_3':
				selectElapsedTimeMetirc = '0.3XLT';
				break;
			case 'less_than_1':
				selectElapsedTimeMetirc = '1XLT';
				break;
			case 'less_than_3':
				selectElapsedTimeMetirc = '3XLT';
				break;
			case 'less_than_10':
				selectElapsedTimeMetirc = '10XLT';
				break;
			case 'less_than_60':
				selectElapsedTimeMetirc = '60XLT';
				break;
			case 'more_than_60':
				selectElapsedTimeMetirc = '60XMT';
				break;
			default:
				break;
			}
			
			let parameter = 'isHandOff=Y&isCheckHighestRank=' + isCheckHighestRank + '&wrkjob_cd='+ wrkjob_cd + 
					'&begin_dt=' + begin_dt + '&end_dt=' + end_dt + '&base_period_value=' + $('#base_period_value').val() +
					'&isCheckFail=' + after_fail_yn_condition + '&isCheckPass=' + isCheckPass + 
					'&selectSearchType=' + selectSearchType +
					'&selectSqlPerfTrace=' + $('#exception_prc_meth_cd').val() +
					'&selectElapsedTimeMetirc=' + selectElapsedTimeMetirc;
			
			parent.parent.selectTab1(parameter);
		},
		onLoadSuccess: function(row, data) {
			$(this).treegrid('collapseAll');
		},
		loadFilter: function(data){
			if ($.isArray(data)){
				return data;
			} else {
				$.map(data.rows, function(row){
					if (row._parentId == -1){
						row._parentId = undefined;
					}
				});
				return data;
			}
		}
	});
}

function parentStyler(value,row,index){
	if(typeof(row._parentId) == 'undefined') {
		return 'background-color:#deebf7;';
	}
}

function formatFail(value) {
	let format = '';
	let fontStyle = "font-size: 11px;font-family: 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic', 'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic;";
	
	if(typeof(value) == 'undefined') {
		format = '<label/>';
	} else {
		if(value > 0) {
			format = '<img src="/resources/images/fail.png" style="float:left;margin-top: -4px;"/>' + 
					'<label style="color: red;margin-right: 0px;' + fontStyle + '">' + value + '</label>';
		} else {
			format = '<label style="margin-right: 0px;' + fontStyle + '">' + value + '</label>';
		}
	}
	
	return format;
}

function search(isClickEvent) {
	$('#exception_prc_meth_cd').val($('#selectSqlPerfTrace').combobox('getValue'));
	
	loadTreeList();
}

function Btn_OnClick(){
	if(!isValidationCheck()) {
		return;
	}
	
	search(true);
}

function loadTreeList() {
	$('#treeList').treegrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val()," ");
	
	ajaxCallTreeList();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
}

function isValidationCheck() {
	if( ($('#checkHighestRankYn').checkbox('options').checked == false) &&
			($('#wrkjob_cd_top_level').combotree('getValue') == '') ) {
		parent.$.messager.alert('경고','업무를 선택해 주세요','warning');
		return false;
	}
	
	if( $('#begin_dt').textbox('getValue') == "" ){
		parent.$.messager.alert('경고','기준일자를 확인해 주세요','warning');
		return false;
	}
	
	if( $('#end_dt').textbox('getValue') == "" ){
		parent.$.messager.alert('경고','종료일자를 확인해 주세요','warning');
		return false;
	}
	
	if(!compareAnBDate($('#begin_dt').datebox('getValue'), $('#end_dt').datebox('getValue'))) {
		let msg = "기준일자를 확인해 주세요.<br>시작일자[" + $('#begin_dt').datebox('getValue') + "] 종료일자[" + $('#end_dt').datebox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	return true;
}

function ajaxCallTreeList(){
	ajaxCall("/RunSqlPerformanceChangeTracking/loadExceptionHandlingSql",
			$("#submit_form"),
			"POST",
			callback_TreeListAction);
}

var callback_TreeListAction = function(result) {
	json_string_treegrid_callback_common(result, '#treeList', true);
};

var callback_TreeListActionTest = function(result) {
	try {
		let target = $('#treeList');
		var opts = $(target).treegrid('options');
		var vc = $(target).treegrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		
		$('#treeList').treegrid('loadData', testData);
	} catch(err) {
		console.error(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

const testData = {"totalCount":9,"rows":[
	{"wrkjob_cd":1,"_parentId":"-1","wrkjob_cd_nm":"GS인증"},
	{"wrkjob_cd":11,"wrkjob_cd_nm":"대출","sql_cnt":2000,"improve":1800,"regress":1903,"fail":3,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":1},
	{"wrkjob_cd":12,"wrkjob_cd_nm":"보험","sql_cnt":2000,"improve":1800,"regress":1903,"fail":0,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":1},
	{"wrkjob_cd":13,"wrkjob_cd_nm":"세일즈","sql_cnt":2000,"improve":1800,"regress":1903,"fail":83,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":1},
	{"wrkjob_cd":2,"wrkjob_cd_nm":"openpop"},
	{"wrkjob_cd":21,"wrkjob_cd_nm":"수신","sql_cnt":2000,"improve":1800,"regress":1903,"fail":2183,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":2},
	{"wrkjob_cd":22,"wrkjob_cd_nm":"외환","sql_cnt":2000,"improve":1800,"regress":1903,"fail":283,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":2},
	{"wrkjob_cd":23,"wrkjob_cd_nm":"세일즈","sql_cnt":2000,"improve":1800,"regress":1903,"fail":22183,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":2},
	{"wrkjob_cd":24,"wrkjob_cd_nm":"마일즈","sql_cnt":2000,"improve":1800,"regress":1903,"fail":2,"less_than_2":2133,"less_than_5":1923,"less_than_10":2018,"less_than_30":1838,"_parentId":2}
]};

function Excel_Download(){
	if(!isValidationCheck()) {
		return;
	}
	
	$("#submit_form").attr("action","/RunSqlPerformanceChangeTracking/loadExceptionHandlingSqlExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
//	openMessageProgress();
}