/**
 * 
 */
var forceSuccessPop ;
var allSuccessPop;

var bef_AutoIndexAnalys_progress = false;
var timer;
$(document).ready(function(){
	initializeElements();
});

function initializeElements(){

	$("body").css("visibility", "visible");

	$("#currentPage").val("1");
	$("#pagePerCount").val("10");
	$('#saveSQLPerformance').window("close");

	setNumberBoxAttrByName('topn_cnt','min',1);
	setNumberBoxAttrByName('timer_value','min',3);

	initializeSqlPerformancePCombobox();
	initializeFilterBox();
	initialAllSQLYNCombobox();
	initialRefreshButton();
	initialProgressButton();
	initialCompletionButton();

	createProjectIdCombobox();
	createSelectiviyTooltip();
	createList();
	closeMessageProgress();

}

function initialProgressButton(){

	$("#inProgress").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			$("#currentPage").val("1");
			getProjectPerformancePacData(getEasyUiFieldValue('project_id'));
		}
	});

}

function initialCompletionButton(){
	$("#completion").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			$("#currentPage").val("1");
			getProjectPerformancePacData(getEasyUiFieldValue('project_id'));
		}
	});
}

function initialRefreshButton(){
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function( checked ) {

			setTimeout(function() {
				if ( checked && $("#refresh").val() == "N" ) {
					$("#refresh").val("Y");

					if ( checkRequiredFieldAndShowMsg('project_id') === false) {
						$("#refresh").val("N");
						$(' #chkRefresh').switchbutton("uncheck");

						return;
					}else if ( isEmpty(getEasyUiFieldValue('timer_value')) ){
						getEasyUiFieldValue('timer_value',60);
					}

					$("#timer_value").textbox({disabled:true});

					getProjectPerformancePacData_Timeout();
				} else {
					$("#refresh").val("N");
					$("#timer_value").textbox({disabled:false});
					bef_AutoIndexAnalys_progress = false;
					window.clearTimeout(timer);
				}
				setNumberBoxAttrByName('timer_value','min',3);

			},500);
		}
	});

}

function initialAllSQLYNCombobox(){
	$("#all_sql_yn_chk").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").val("Y");
				$("#topn_cnt").textbox("setValue","");
				$("#topn_cnt").textbox("readonly",true);
			} else {
				$("#all_sql_yn").val("N");
				$("#topn_cnt").textbox("readonly",false);
				$(".topn_cnt .textbox-prompt").removeClass("textbox-prompt");
				$(".topn_cnt .validatebox-readonly").removeClass("validatebox-readonly");
			}
		}
	});
}

function initializeSqlPerformancePCombobox(){

	$("#sqlPerformanceP").combobox({
		onShowPanel : function(){
			getSqlPerformancePacList();
		}
	});

}
function initializeFilterBox(){

	$('#ownerEditBox').window({
		title : "TABLE_OWNER <a style='color:lightblue'>in</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeOwnerBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(315),
		left:getWindowLeft(1435),
		closable:false
	});

	$('#moduleEditBox').window({
		title : "MODULE <a style='color:lightblue'>like</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeModuleBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(315),
		left:getWindowLeft(990),
		closable:false
	});

	$('#table_nameEditBox').window({
		title : "TABLE_NAME <a style='color:lightblue'>in</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeTableNameBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(315),
		left:getWindowLeft(545),
		closable:false
	});

	$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
	$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
	$('#table_name_list').textbox('textbox').prop('placeholder', "ORDER, CUSTOMER, PRODUCT");

	$('#ownerEditBox').window("close");
	$('#moduleEditBox').window("close");
	$('#table_nameEditBox').window("close");


	$('.owner_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}

		$("#ownerEdit").val($("#owner_list").val() );
		$('#ownerEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(1475)
		});
		$("#ownerEditBox").window("open");

	});

	$('.module_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}

		$("#moduleEdit").val( $("#module_list").val() );
		$('#moduleEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(-560)
		});
		$("#moduleEditBox").window("open");
	});
	$('.table_name_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}

		$("#table_nameEdit").val( $("#table_name_list").val() );
		$('#table_nameEditBox').window({
			top:getWindowTop(435),
			left:getWindowLeft(465)
		});
		$("#table_nameEditBox").window("open");
	});

}
function closeOwnerBox() {
	$("#owner_list").textbox( "setValue", $("#ownerEdit").val() );
	$('#ownerEditBox').window("close");
}
function closeModuleBox() {
	$("#module_list").textbox( "setValue", $("#moduleEdit").val() );
	$('#moduleEditBox').window("close");
}
function closeTableNameBox() {
	$("#table_name_list").textbox( "setValue", $("#table_nameEdit").val() );
	$('#table_nameEditBox').window("close");
}
function createSelectiviyTooltip(){

	let ndvRatioTooltip = "통계정보 기반 </br>selectivity 계산";
	let colNullTooltip = "데이터 샘플링 기반</br>selectivity 계산";

	$('#selectivity_statistics_tooltip').tooltip({
		content : '<span style="color:#fff">' + ndvRatioTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});

	$('#selectivity_data_tooltip').tooltip({
		content : '<span style="color:#fff">' + colNullTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});

}
function createProjectIdCombobox(){

	$('#project_id').combobox({
		url:"/AISQLPVAnalyze/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadError: function(){
			parent.$.messager.alert('오류','프로젝트 리스트 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#project_id').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(row){
			getOriginalDB(row);
			if(getEasyUiFieldValue('chkRefresh') === true){
				setEasyUiFieldValue('chkRefresh','uncheck');
				setEasyUiFieldValue('inProgress','uncheck');
				setEasyUiFieldValue('completion','uncheck');
			}
			setTimeout(function() {
				getSqlPerformancePacList();
				getProjectPerformancePacData(row.project_id);
			},300);
			// setReadOnlyCombobox(false);
			resetElements(true);

			$("#currentPage").val("1");
		}
	});

}

function resetRefreshBtn(){

}

function getPerformancePacData(){

	ajaxCall("/AISQLPVAnalyze/getPerformancePacData",
			$("#submit_form"),
			"POST",
			callback_GetPerformancePacData);

}

function callback_GetPerformancePacData(result){
	if(isEmpty(result)){return false;}

	let jsonData = JSON.parse(result)[0];

	// if(jsonData.result === false){
	// 	parent.$.messager.alert('오류','SQL 점검팩 실행 데이터를 조회중 오류가 발생하였습니다.');
	// 	return false;
	// }

	//to-be db 네임이 없을경우 실행되지 않은 점검팩
	if(isEmpty(jsonData.perf_check_target_dbid)){
		// setReadOnlyCombobox(false);
		return false;
	}

	setPerformancePacData(jsonData);
	// setReadOnlyCombobox(true);

}

function setPerformancePacData(jsonData){

	let idx_selectvity_calc_meth_cd = jsonData.idx_selectvity_calc_meth_cd;

	setEasyUiFieldValue('original_dbid',jsonData.original_dbid);
	setEasyUiFieldValue('perf_check_target_dbid',jsonData.perf_check_target_dbid);
	setEasyUiFieldValue('perf_check_range_begin_dt',jsonData.perf_check_range_begin_dt);
	setEasyUiFieldValue('perf_period_start_time',jsonData.perf_period_start_time);
	setEasyUiFieldValue('perf_check_range_end_dt',jsonData.perf_check_range_end_dt);
	setEasyUiFieldValue('perf_period_end_time',jsonData.perf_period_end_time);

	if( isEmpty(jsonData.topn_cnt) ){
		$("#all_sql_yn_chk").checkbox('check')
	}else{
		$("#all_sql_yn_chk").checkbox('uncheck')
		setEasyUiFieldValue("topn_cnt",jsonData.topn_cnt);
	}

	setEasyUiFieldValue('owner_list',jsonData.owner_list);
	setEasyUiFieldValue('table_name_list',jsonData.table_name_list);
	setEasyUiFieldValue('module_list',jsonData.module_list);
	setEasyUiFieldValue('extra_filter_predication',jsonData.extra_filter_predication);

	if(idx_selectvity_calc_meth_cd === '1'){
		$('#statistics').radiobutton('check');
	}else if(idx_selectvity_calc_meth_cd === '2'){
		$('#dataSample').radiobutton('check');
	}

}

function resetPerformanceP(){
	$('#sqlPerformanceP').combobox('clear');
	$('#sqlPerformanceP').combobox('loadData', []);
}

function resetElements(isProjectId) {

	if (isProjectId) {
		$('#sqlPerformanceP').combobox('clear');
		$('#sqlPerformanceP').combobox('loadData', []);
		$('#sql_auto_perf_check_id').val('');

	}

	$('#owner_list').textbox('setValue','');
	$('#module_list').textbox('setValue','');
	$('#table_name_list').textbox('setValue','');

	$('#statistics').radiobutton('check');
	$('#all_sql_yn_chk').checkbox('check');

	$('#original_dbid').combobox('setValue','');
	$('#perf_check_target_dbid').combobox('setValue','');
	$('#extra_filter_predication').textbox('setValue','');
	$('#perf_check_range_begin_dt').datebox('setValue',$('#startDate').val());
	$('#perf_period_start_time').textbox('setValue',$('#startTime').val());
	$('#perf_check_range_end_dt').datebox('setValue',$('#endDate').val());
	$('#perf_period_end_time').textbox('setValue',$('#endTime').val());

}


function setReadOnlyCombobox(isReadOnly){

	if(isReadOnly === false) {
		$('#sqlPerformanceP').combobox('readonly', isReadOnly);
	}
	$('#original_dbid').combobox('readonly', isReadOnly);
	$('#perf_check_target_dbid').combobox('readonly', isReadOnly);
	$('#perf_check_range_begin_dt').datebox('readonly',isReadOnly);

	$('#perf_period_start_time').datebox('readonly',isReadOnly);
	$('#perf_check_range_end_dt').datebox('readonly',isReadOnly);
	$('#perf_period_end_time').datebox('readonly',isReadOnly);
	$('#all_sql_yn_chk').checkbox('readonly',isReadOnly);

	$('#owner_list').textbox('readonly',isReadOnly);
	$('#ownerEdit').attr('readonly',isReadOnly)

	$('#module_list').textbox('readonly',isReadOnly);
	$('#moduleEdit').attr('readonly',isReadOnly)


	$('#table_name_list').textbox('readonly',isReadOnly);
	$('#table_nameEdit').attr('readonly',isReadOnly)

	$('#extra_filter_predication').textbox('readonly',isReadOnly);

	if(getEasyUiFieldValue('all_sql_yn_chk') === false){
		$('#topn_cnt').textbox('readonly',isReadOnly);
	}
}

function getSqlPerformancePacList(){

	let project_id = $("#project_id").combobox('getValue');

	if(isEmpty(project_id)){
		return false;
	}

	let url = "/AISQLPVAnalyze/getSqlPerformancePacList?project_id="+project_id;
	url += "&perf_check_type_cd=4";
	url += "&database_kinds_cd=ORACLE";

	$('#sqlPerformanceP').combobox({
		url:url,
		method:"get",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		readonly:false,
		onLoadSuccess: function(rows){
			if(isEmpty(rows)){
				setEasyUiFieldValue('sqlPerformanceP','');
				return false;
			}

			// setEasyUiFieldValue('sqlPerformanceP', sql_auto_perf_check_id);
			// getPerformancePacData();
		},
		onLoadError: function(ex){
			parent.$.messager.alert('오류','SQL 점검팩 리스트 조회중 오류가 발생하였습니다.');
			return false;
		},
		// onSelect:function(row){
		onChange:function(newVal , oldVal){
			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
			$(".textbox-text").removeClass("textbox-prompt");
			resetElements(false);

			if(isNotEmpty(newVal)) {
				checkTableListRowForSelectedPac(newVal);
				setTimeout(function () {
					getPerformancePacData();
					// getSelectedProjectPerformancePacData();
				}, 100)
			}
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
	});
}
//하단 그리드에서 동일 건을 하이라이트함
function checkTableListRowForSelectedPac(val){

	let isUnselect = false;
	let sql_auto_perf_check_id = '';

	if(isEmpty(val)) {
		sql_auto_perf_check_id = getEasyUiFieldValue("sqlPerformanceP");
	}else{
		sql_auto_perf_check_id = val;
	}

	$('#tableList').datagrid('getData').rows.forEach(function(element,idx){
		if(element.sql_auto_perf_check_id === sql_auto_perf_check_id){
			$('#tableList').datagrid('unselectAll');
			$('#tableList').datagrid('selectRow',idx);
			isUnselect = true;
			return ;
		}
	});
	if(isUnselect === false){
		$('#tableList').datagrid('unselectAll');
	}

	getSelectedProjectPerformancePacData();

}

function SetSqlAutoPerfChk(){
	let url = '/AISQLPVAnalyze/setSqlAutoPerfChk';
	ajaxCall(url,
		$("#submit_form"),
		"POST",
		callback_SetSqlAutoPerfChk);

}
function callback_SetSqlAutoPerfChk(result){
	if(isEmpty(result)){
		closeMessageProgress();
		parent.$.messager.alert('오류','인덱스 자동 분석 모듈 실행중 오류가 발생했습니다.');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(jsonData.result === false){
		closeMessageProgress();
		parent.$.messager.alert('오류','인덱스 자동 분석 모듈 실행중 오류가 발생했습니다.');
		console.log(jsonData.message);
		return false;
	}

	excuteAnalyze();
}

function excuteAnalyze(){
	let project_id = getEasyUiFieldValue('sqlPerformanceP');
	let url = '/AISQLPVAnalyze/excuteAnalyze';
	setReadOnlyCombobox(true);
	ajaxCallWithTimeout(url,
						$("#submit_form"),
				"POST",
						getSelectedProjectPerformancePacData,
				43200000
	);

	setTimeout(function() {
		// 수행 결과 조회
		closeMessageProgress();
		if(getEasyUiFieldValue('chkRefresh')){
			getProjectPerformancePacData();
		}else{
			$(' #chkRefresh').switchbutton("check");
		}
	},1000)

}

// function callback_ExcuteAnalyze(result){
//
// 	if(isNotEmpty(result)){
// 	}else{
// 		parent.$.messager.alert('',result);
// 		return false;idx_selectvity_calc_meth_cd
// 	}
// }

function getExcuteAnalyzeConstraint(){
	if(checkRequiredFields() === false){
		return false;
	}
	if (parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 자동 분석 진행을 위한 정보를 조회 중입니다."," ");

	let url = '/AISQLPVAnalyze/getExcuteAnalyzeConstraint';
	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_GetExcuteAnalyzeConstraint);
}

function callback_GetExcuteAnalyzeConstraint(result) {
	result = JSON.parse(result);
	if(result.result === false){
		parent.$.messager.alert('오류', "수행 정보를 조회 중 오류가 발생하였습니다.");
	}

	excuteAnalyzeConstraint(result,true);
}

function excuteAnalyzeConstraint(result,showExcutePop){

	let rows = result.rows;

	if(isEmpty(rows)){

	}else {
		rows = rows[0];
		if (isNotEmpty(rows.perf_check_end_yn) && rows.perf_check_end_yn === 'Y') {
			closeMessageProgress();
			parent.$.messager.alert('정보', "성능 분석 작업이 완료되었습니다. 성능 분석이 완료된 경우 실행 할 수 없습니다.");
			return false;
		}else if (isNotEmpty(rows.acces_path_exec_yn) && rows.acces_path_exec_yn === 'Y') {
			closeMessageProgress();
			parent.$.messager.alert('정보', "현재 인덱스 자동 분석 작업이 실행(ACCESS PATH 분석) 중 입니다.");
			return false;
		}else if (isNotEmpty(rows.index_recommend_exec_yn) && rows.index_recommend_exec_yn === 'Y') {
			closeMessageProgress();
			parent.$.messager.alert('정보', "현재 인덱스 자동 분석 작업이 실행(인덱스 검증 분석) 중 입니다");
			return false;
		} else if (isNotEmpty( rows.index_db_create_exec_yn ) && rows.index_db_create_exec_yn === 'Y') {
			closeMessageProgress();
			parent.$.messager.alert('정보', "인덱스 자동 분석 작업이 완료되었습니다. 현재 인덱스 자동 생성 중입니다");
			return false;
		}
	}

	ajaxCallWithCallbackParam("/AISQLPVAnalyze/getRecommendIndexDbYn",
	$("#submit_form"),
			"POST",
			callback_GetRecommendIndexDbYn,
			showExcutePop);
}

function callback_GetRecommendIndexDbYn(result,showExcutePop){
	if(isEmpty(result)){
		parent.$.messager.alert('오류','DB에 추천으로 생성된 인덱스 존재 여부 조회중 오류가 발생하였습니다.');
		closeMessageProgress();
		return false;
	}

	let jsonData = JSON.parse(result);

	if(isNotEmpty(result)){
		if(jsonData.is_error === 'true'){
			parent.$.messager.alert('오류','DB에 추천으로 생성된 인덱스 존재 여부 조회중 오류가 발생하였습니다.');
			closeMessageProgress();
			return false;
		}else if(jsonData.use_yn == 'Y'){
			parent.$.messager.alert('정보', "인덱스 자동 분석 작업이 완료되어 1건 이상의 추천 인덱스가 DB에 생성되었습니다. 추천 인덱스가 생성된 경우 실행 할 수 없습니다.");
			closeMessageProgress();
			return false;
		}
	}else{
		parent.$.messager.alert('오류','DB에 추천으로 생성된 인덱스 존재 여부 조회중 오류가 발생하였습니다.');
		closeMessageProgress();
		return false;
	}


	if(showExcutePop){
		var param = "확인";
		// var msgStr ="SQL 점검팩 - [" + $('#sqlPerformanceP').combobox('getText') +"] 실행 하시겠습니까?";
		var msgStr = '인덱스 자동 분석을 실행하시겠습니까?';

		parent.$.messager.confirm({
			title : param,
			msg : msgStr,
			onClose:function(){
				closeMessageProgress();
				return false;
			},
			fn : function(r) {
				if (r) {
					if (parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 자동 분석 작업이 진행 중입니다..", " ");

					SetSqlAutoPerfChk();
				} else {

				}
			}
		});
	}else{
		if (parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 자동 분석 작업이 진행 중입니다..", " ");
		SetSqlAutoPerfChk();
	}
}

function formValidationCheck(){
	return true;
}


function fnSearch(){
	// setEasyUiFieldValue('chkRefresh','uncheck');
	// bef_AutoIndexAnalys_progress = false;
	getProjectPerformancePacData();
	getSelectedProjectPerformancePacData();

}

function getSelectedProjectPerformancePacData(){
	let url = "/AISQLPVAnalyze/getSelectedProjectPerformancePacData";

	if (checkRequiredFieldAndShowMsg('project_id') === false) {
		return false;
	}

	$('#oneRow').val('Y');

	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_GetSelectedProjectPerformancePacData);
}

function callback_GetSelectedProjectPerformancePacData(result){
	if(result === '[]'){
		setReadOnlyCombobox(false);
		return ;
	}

	let jsonData = JSON.parse(result)[0];

	if(isNotEmpty(jsonData.index_exec_end_dt) &&
		(isNotEmpty($('#tableList').datagrid('getSelected')) && isNotEmpty($('#tableList').datagrid('getSelected').index_exec_end_dt))){
		if (isNotEmpty(jsonData.table_count) && jsonData.table_count == 0) {
			setReadOnlyCombobox(false);
		}else{
			setReadOnlyCombobox(true);
		}
	}else{
		if( isNotEmpty(isNotEmpty(jsonData.table_count) && jsonData.table_count > 0)){
			setReadOnlyCombobox(true);
		}else{
			setReadOnlyCombobox(false);
		}
	}
}

//하단부 데이터 그리드
function getProjectPerformancePacData(){
	let url = "/AISQLPVAnalyze/getProjectPerformancePacData";
	if (checkRequiredFieldAndShowMsg('project_id') === false) {
		setEasyUiFieldValue('chkRefresh','uncheck');
		return false;
	}

	$('#oneRow').val('N');
	$("#tableList").datagrid("loading");

	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_GetProjectPerformancePacData);

}

function getProjectPerformancePacData_Timeout() {
	var intSec = 0;

	getProjectPerformancePacData();

	if ( $("#refresh").val() == "Y" ) {
		intSec = strParseInt( getEasyUiFieldValue('timer_value'),0 );
		timer = window.setTimeout("getProjectPerformancePacData_Timeout()",(intSec*1000));
	} else {
		window.clearTimeout(timer);
	}
}

function checkComplete(){
	if(getEasyUiFieldValue('chkRefresh') === false){
		return false;
	}

	if(( getEasyUiFieldValue('completion') === true || getEasyUiFieldValue('inProgress') === true )
		&& ( getEasyUiFieldValue('completion') === true && getEasyUiFieldValue('inProgress') === true ) === false){
		return false;
	}
	let b_Complete_AutoIndexAnalys = true;
	$('#tableList').datagrid('getData').rows.forEach(function(element,idx){
		if(isEmpty(element.index_exec_end_dt)) {
			b_Complete_AutoIndexAnalys = false;
			if(bef_AutoIndexAnalys_progress === false){
				bef_AutoIndexAnalys_progress = true;
			}
			if(forceSuccessPop){
				forceSuccessPop = null;
			}

		}
	});

	if(bef_AutoIndexAnalys_progress && b_Complete_AutoIndexAnalys === true){
		if(!forceSuccessPop){
			parent.$.messager.alert('정보', '인덱스 자동 분석이 완료되었습니다.');
		}else{
			forceSuccessPop = null;
		}
		bef_AutoIndexAnalys_progress = false;
		window.clearTimeout(timer);
		$('#chkRefresh').switchbutton("uncheck");
	}
}


function callback_GetProjectPerformancePacData(result){

	if(isEmpty(result)){
		setEasyUiFieldValue('chkRefresh','uncheck');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(jsonData.result === false){
		parent.$.messager.alert('오류','SQL 점검팩 데이터를 조회중 오류가 발생하였습니다.');
		setEasyUiFieldValue('chkRefresh','uncheck');
		return false;
	}

	var dataLength = JSON.parse(result).dataCount4NextBtn;

	json_string_callback_common(result,'#tableList',true);
	fnEnableDisablePagingBtn(dataLength);

	/* modal progress close */
	$("#tableList").datagrid("loaded");
	closeMessageProgress();
	checkTableListRowForSelectedPac(null);
	checkComplete();

}

function getOriginalDB(row){

	let url = '/AISQLPVAnalyze/getOriginalDB';
	url += '?database_kinds_cd=ORACLE';
	url += '&project_id=' + row.project_id;

	// 원천 DB
	$("#original_dbid").combobox({
		url: url,
		method:"get",
		valueField:'original_dbid',
		textField:'original_db_name',
		// panelHeight: 300,
		onLoadError: function() {
			/* modal progress close */
			closeMessageProgress();
		},
		onLoadSuccess: function(items) {
			/* modal progress close */
			closeMessageProgress();
			setEasyUiFieldValue('perf_check_target_dbid','');
		},
		onSelect: function(data) {
			if(data.original_dbid != null && data.original_dbid != "") {
				// 대상 DB
				getTargetDB(row,data);
				// setReadOnlyCombobox(false);
			}
		}
	});
}

function getTargetDB(row,data){

	let original_dbid = $("#original_dbid").combobox('getValue');

	let url = "/AutoPerformanceCompareBetweenDatabase/loadOriginalDb";
	url += "?project_id="+row.project_id ;
	url += "&original_dbid="+data.original_dbid;
	url += "&database_kinds_cd=ORACLE";

	//이걸 안넣으면 readonly가 true가 됨. 원인 불명
	$("#perf_check_target_dbid").combobox('options')

	$("#perf_check_target_dbid").combobox({
		url:url,
		method:"get",
		valueField:'perf_check_target_dbid',
		textField:'perf_check_target_db_name',
		// panelHeight: 300,
		onLoadError: function() {
			/* modal progress close */
			closeMessageProgress();
		},
		onChange : function(value){
			if(isEmpty(value)){
				// setReadOnlyCombobox(false);
				return false;
			}
		},
		onLoadSuccess: function(items) {
			/* modal progress close */
			closeMessageProgress();
			// onReadonlyCheck();
			}
	});
}
function cellStyler( value,row,index ) {

		if(row.exec_status.indexOf('강제완료')>-1) {
			return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
		}else if(row.exec_status === '완료') {
			return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
		} else if(isEmpty(row.exec_status)){
			return '';
		}else {
			return 'background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
		}
}

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'perf_check_name',title:'SQL점검팩명',width:'8%',halign:'center',align:'left',rowspan:'2'},
			{title:'DB',width:'8%',halign:'center',align:'center',colspan:2},
			{field:'exec_status',title:'수행상태',width:'12%',halign:'center',align:'center',rowspan:'2' , styler:cellStyler},
			{title:'수집',width:'12%',halign:'center',align:'center',colspan:3},
			{title:'추천',width:'8%',halign:'center',align:'center',colspan:2},
			{field:'access_path_exec_dt',title:'작업시작일시',width:'8%',halign:'center',align:'center',rowspan:'2'},
			{field:'index_exec_end_dt',title:'작업종료일시',width:'8%',halign:'center',align:'center',rowspan:'2'},
			{field:'exec_time',title:'수행시간',width:'8%',halign:'center',align:'center',rowspan:'2',formatter:getNumberFormatNullChk},
			{field:'defaultText',title:'분석 결과',width:'6%',halign:'center',align:'center',rowspan:'2'},
			],[
				{field:'asis_db_name',title:'ASIS',width:'5%',halign:'center',align:'left'},
				{field:'tobe_db_name',title:'TOBE',width:'5%',halign:'center',align:'left'},
				{field:'table_count',title:'테이블 수',width:'5%',halign:'center',align:'right'},
				{field:'analysis_sql_cnt',title:'SQL 수',width:'5%',halign:'center',align:'right'},
				{field:'access_path_count',title:'ACCESS PATH 수',width:'6%',halign:'center',align:'right'},
				{field:'running_table_cnt',title:'테이블 수',width:'5%',halign:'center',align:'right'},
				{field:'recommend_index_cnt',title:'인덱스 수',width:'5%',halign:'center',align:'right'},
				{field:'topn_cnt',title:'topn_cnt',hidden:'true'},
			// {field:'sql_auto_perf_check_id',title:'점검팩ID',hidden:'true'}
		]],
		onClickRow:function( index, row ) {
			selectTableListRow(row);
		},
		onLoadSuccess:function(data) {
			if(data.rows.length === 0 ){
				return false;
			}

			let trObjs = $('#submit_form .datagrid-view2 .datagrid-body tr')
			let length = trObjs.length;
			let resizeWidth = 0;
			let colOpt = $('#tableList').datagrid('getColumnOption','perf_check_name');

			if(isEmpty(colOpt)){
				return false;
			}

			for(let i = 0 ; i < length ; i++){
				let col = $(trObjs[i]).find('td:first div');

				if(isEmpty(col)){
					return false;
				}
				col = col[0];

				let scrollWidth = col.scrollWidth;
				let colWidth = col.clientWidth;

				if(scrollWidth > colWidth && resizeWidth < scrollWidth){
					resizeWidth = scrollWidth;
				}
			}

			if(resizeWidth > 0){
				colOpt.boxWidth = resizeWidth;
				colOpt.width = resizeWidth;

				$('#tableList').datagrid('resize');
			}

		},
		onLoadError:function() {
			parent.$.messager.alert('오류','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function showFilterSQL(){

	let isReadOnly = false;

	if($('#owner_list').textbox('options').readonly === true){
		isReadOnly = true;
	}

	$("#condition_1").combobox('readonly',isReadOnly);
	$("#condition_2").combobox('readonly',isReadOnly);
	$('#filter_sql').textbox('readonly', isReadOnly);

	if(isReadOnly === true){
		$('#btn_Filter_Add').linkbutton('disable');
		$('#btn_Filter_Save').linkbutton('disable');
		old_filter_sql = getEasyUiFieldValue('extra_filter_predication')
		setEasyUiFieldValue('filter_sql',old_filter_sql);
	}else{
		$('#btn_Filter_Add').linkbutton('enable');
		$('#btn_Filter_Save').linkbutton('enable');
		old_filter_sql = getEasyUiFieldValue('extra_filter_predication')
		setEasyUiFieldValue('filter_sql',old_filter_sql);
	}

	$("#condition_1").combobox("setValue","");
	$("#condition_2").combobox("setValue","");

	$('#filterSqlPopup').window("open");
}


function forceCompleteAnalyze() {

	if(isEmpty($("#tableList").datagrid('getSelected'))){
		parent.$.messager.alert('정보','강제완료 처리할 SQL점검팩을 선택해야 합니다.','info');
		return false;

	}

	// if ( checkCondition() === false ) {
	// 	return;
	// }
	ajaxCall("/AISQLPVAnalyze/getExcuteAnalyzeConstraint",
			$("#submit_form"),
			"POST",
			callback_ForceCompleteAnalyze);
}

function callback_ForceCompleteAnalyze(result) {

	let jsonData = JSON.parse(result);
	let rows = jsonData.rows[0];

	if (isNotEmpty(rows.acces_path_exec_yn) && rows.acces_path_exec_yn === 'Y') {
	} else if (isNotEmpty( rows.index_recommend_exec_yn ) && rows.index_recommend_exec_yn === 'Y') {
	}else{
		parent.$.messager.alert('정보','해당 SQL점검팩에서 실행 중인 작업이 없습니다.','info');
		return false;
	}

	// 수행중일 경우
	var param = "확인";
	var msgStr = $('#sqlPerformanceP').combobox('getText') + "에서 수행 중인 작업을 강제완료처리 하시겠습니까?";

	parent.$.messager.confirm({
		title : param,
		msg : msgStr,
		onClose:function(){
			closeMessageProgress();
			return false;
		},
		fn : function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("강제완료처리", "강제 완료 처리 중입니다.");

				ajaxCall("/AISQLPVAnalyze/forceCompleteAnalyze",
					$("#submit_form"),
					"POST",
					callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
			}
		}
	});
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	if(isEmpty(result)){
		parent.$.messager.alert('오류','해당 SQL점검팩에 대한 강제 완료 처리중 오류가 발생하였습니다.','info');
		console.log('callBack data is null');
		getProjectPerformancePacData(getEasyUiFieldValue('project_id'));

	}

	let jsonData = JSON.parse(result);

	/* modal progress close */
	closeMessageProgress();
	if ( jsonData.is_error === 'false' ) {
		forceSuccessPop = parent.$.messager.alert('정보','해당 SQL점검팩에 대한 강제 완료 처리가 완료되었습니다.','info');

	} else {
		console.log("resultMsg ===> "+ jsonData.err_msg );
		parent.$.messager.alert('오류','강제완료처리 하지 못했습니다.' , 'info');
	}

	getProjectPerformancePacData(getEasyUiFieldValue('project_id'));
}

function checkDate(){
	let startDate = getEasyUiFieldValue('perf_check_range_begin_dt');
	let endDate = getEasyUiFieldValue('perf_check_range_end_dt');
	let startTime = getEasyUiFieldValue('perf_period_start_time');
	let endTime = getEasyUiFieldValue('perf_period_end_time');


	if(compareAnBDatatime(startDate, startTime, endDate, endTime) < 0){
		parent.$.messager.alert('정보','수집기간을 확인해 주세요.','warning');
		return false;
	}
	return true;
}

function moveToOtherTab(project_id, sql_auto_perf_check_id, database_kinds_cd) {

	let parameter = {};
	parameter.project_id = project_id;
	parameter.sql_auto_perf_check_id = sql_auto_perf_check_id;
	parameter.database_kinds_cd = database_kinds_cd;

	setTimeout(function() {
		parent.moveToOtherTab( 1, 'IndexRecommend', parameter, 500, 20);
	},150);
}

function selectTableListRow(row){

	let hasSqlPerformacnePList = ($('#sqlPerformanceP').combobox('getData').length !== 0);
	setEasyUiFieldValue('sqlPerformanceP', row.sql_auto_perf_check_id);

	// if(hasSqlPerformacnePList){
	// 	getPerformancePacData();
	// }else{
	// 	getSqlPerformancePacList(row.sql_auto_perf_check_id);
	// }

}
function sqlPerfPacReload( projectId, sqlPerfId ) {
	let currentSqlPackId = $('#sqlPerformanceP').combobox('getValue');

	if(projectId === getEasyUiFieldValue('project_id') && sqlPerfId === getEasyUiFieldValue('sqlPerformanceP')) {
			setEasyUiFieldValue('sqlPerformanceP', '');
	}

	if ( currentSqlPackId == sqlPerfId ) {
		$('#sqlPerformanceP').combobox('clear');
		
	}else {
		$('#sqlPerformanceP').combobox('setValue', currentSqlPackId);
	}
}

function closeMessageProgress() {
	if (isNotEmpty(parent.closeMessageProgress)) {
		parent.closeMessageProgress();
	}
	if (isNotEmpty(parent.parent.closeMessageProgress)) {
		parent.parent.closeMessageProgress();
	}
}

function checkRequiredFieldAndShowMsg(id){
	if(isEmpty(getEasyUiFieldValue(id))){
		if($('#'+id)[getEasyUiFieldType(id)]('options').readonly === false){
			closeMessageProgress();
			parent.$.messager.alert('정보', document.getElementById(id).getAttribute('requiredMsg'));
			return false;
		}
	}
	return true;
}

function checkRequiredFields(){
	let fields = $('.required');

	let len = fields.length;
	for(let idx = 0 ; idx < len ; idx++){
		if(isEmpty(getEasyUiFieldValue(fields[idx].id))){
			if(jQuery(fields[idx])[getEasyUiFieldType(fields[idx].id)]('options').readonly === false) {
				closeMessageProgress();
				parent.$.messager.alert('정보', fields[idx].getAttribute('requiredMsg'));
				return false;
			}
		}
	}

	return true;
}

function setNumberBoxAttrByName(name, str, value){
	let obj = document.getElementsByName(name);

	if(isNotEmpty(obj)){
		obj = obj[0];
	}else{
		return false;
	}

	if(isNotEmpty(obj.parentElement) && isNotEmpty(obj.parentElement.firstChild)){
		obj.parentElement.firstChild.setAttribute(str,value);
	}
}