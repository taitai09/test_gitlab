
var CURRENT_POP = "";

$(document).ready(function() {
	$('#indexRecommendPopup').window({
		title : "인덱스 생성 스크립트",
		top:getWindowTop(800),
		left:getWindowLeft(440),
		width:750,
		height:750,
		resizable:false
	});
	createAddOrDelHistoryList();
	createAutoErrList();
});

function initializeMaxParallelDegree() {
	setEasyUiFieldValue('maxParallelDegree_txt',8);
	$('#maxParallelDegree').switchbutton({
		onChange: function (checked) {
			if (checked) {
				$('#maxParallelDegree_txt').textbox("readonly",false);
			} else {
				$('#maxParallelDegree_txt').textbox("readonly",true);
			}
			setEasyUiFieldValue('maxParallelDegree_txt',checked ? 8 : 1);
		}
	});
}

// function openIndexRecommendPopup(type){
//
// 	let checkedAll = $('.datagrid-header-check > input').prop('checked');
//
// 	var param = "확인";
// 	var msgStr ="개별 인덱스가 선택되었습니다. 효율적인 성능 분석을 위해서는 전체 인덱스 생성을 추천 드립니다. 계속 진행하시겠습니까?";
//
// 	if(checkedAll === false){
// 		if($("#indexList").datagrid('getChecked').length !== 0 ) {
// 			closeMessageProgress();
// 			parent.$.messager.confirm( param,msgStr,function(r) {
// 				if (r) {
// 					setIndexRecommendPopup(type)
// 				}else{
// 				}
// 			});
// 		}
// 	}else{
// 		setIndexRecommendPopup(type);
// 	}
// }

function setIndexRecommendPopup(type){
	let title = '';
	let height = '';
	let width ='';
	let top = '';
	let left = '';
	let obj = $('#indexRecommendPopup');

	if(type !== ADD_DEL_ERR_POP){
		resetEle();
	}

	if(type === CREATE_INDEX_SCRIPT){
		title = '인덱스 생성 스크립트';
		height = '550px';
		width = '750px';
		top = '570';
		left = '700';

		// if(checkCreateIndexScript() === false){
		// 	return false;
		// }

		$('#pop_TableSpace').css('display','block');
		$('#pop_LocalIndex').css('display','block');
		$('#btn_CreateScript').css('display','inline-block');
		$('#btn_CopyScript').css('display','inline-block');
		$('#popup_area').css('display','inline-block');
		$('#pop_MaxParallel_div').css('display','block');
		$('#popup_area').css('width','734px');
		$('#popup_area').css('height','345px');

		initializeMaxParallelDegree();

	}else if(type === AUTO_CREATE_INDEX){

		title = '인덱스 자동생성';
		top = getWindowTop(100),
		left = getWindowLeft(600),
		width = '600px';
		height = '230px';

		$('#pop_TableSpace').css('display','block');
		$('#pop_LocalIndex').css('display','block');
		$('#ignoreError').css('display','block');
		$('#btn_IndexAutoCreate').css('display','inline-block');
		$('#pop_MaxParallel_div').css('display','block');
		initializeMaxParallelDegree();

	}else if(type === DROP_INDEX_SCRIPT){
		height = '480px';
		width = '750px';
		top = '535';
		left = '700';
		title = '인덱스 제거 스크립트';

		$('.popupHeader > .searchBtn').css('text-align','center');
		$('.popupHeader > .searchBtn').css('float','none');

		// $('#btn_CreateScript').css('display','inline-block');
		$('#btn_CopyScript').css('display','inline-block');

		$('#popup_area').css('display','inline-block');
		$('#popup_area').css('width','734px');
		$('#popup_area').css('height','385px');
	}else if(type === VISIBLE_INDEX){
		height = '400px';
		width = '630px';
		top = '430';
		left = '600';
		title = '인덱스 INVISIBLE';

		$('.popupHeader > .searchBtn').css('text-align','center');
		$('.popupHeader > .searchBtn').css('float                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      ','none');

		$('#btn_CopyScript').css('display','inline-block');
		$('#btn_AutoCreate').css('display','inline-block');
		$('#pop_IndexInvisible').css('display','inline-block');
		$('#popup_area').css('display','inline-block');
		$('#popup_area').css('width','734px');
		$('#popup_area').css('height','345px');

	}else if(type === CREATE_HISTORY_ALL || type === DROP_HISTORY_ALL){
		title = (type === CREATE_HISTORY_ALL) ?
			'인덱스 생성 / 제거 이력' :
			'인덱스 제거 이력';

		height = '550px';
		width = '1300px';
		top = '570';
		left = '1300';

		$('#addOrDelHistoryList_div').css('display','block');
	}else if(type === ADD_DEL_ERR_POP){
		title = '인덱스 생성/제거 오류 상세'
		height = '550px';
		width = '1000px';
		top = '570';
		left = '1100';

		$('#autoCrerateErrList_div').css('display','block');


		// $('.popupHeader').css('display','none');
		// $('#popup_area').css('display','inline-block');
		// $('#popup_area').css('width','584px');
		// $('#popup_area').css('height','182px');
		obj = $('#indexRecommendPopup2');

	}

	obj.window({
		title : title,
		height : height,
		width : width,
		top:getWindowTop(top),
		left:getWindowLeft(left),
		resizable:false
	});

	CURRENT_POP = type;
	obj.window("open");
	$('#addOrDelHistoryList').datagrid('resize');
}

function setAddOrDelHistoryList(type){
	createAddOrDelHistoryList();
	let url = '';
	if( type === CREATE_HISTORY_ALL ){
		url = "/AISQLPVIndexRecommend/getAutoIndexCreateHistoryAll";
	}else if( type === DROP_HISTORY_ALL ){
		url = "/AISQLPVIndexRecommend/getAutoIndexDropHistoryAll";
	}
	ajaxCallWithCallbackParam(url,
			$("#searchForm"),
			"POST",
			callback_SetAddOrDelHistoryList,
			type
		)
}

function callback_SetAddOrDelHistoryList(result,type){

	let msg = '';

	if( type === CREATE_HISTORY_ALL ){
		msg = '인덱스 생성 이력 정보를 조회 중 오류가 발생했습니다.';
	}else if ( type === DROP_HISTORY_ALL ){
		msg = '인덱스 제거 이력 정보를 조회 중 오류가 발생했습니다.';
	}

	if(checkErr(result, msg) === false){
		return false;
	}
	let jsonData = JSON.parse(result);
	if(isNotEmpty(jsonData.rows)){
		for(let iter = 0 ; iter < jsonData.rows.length ; iter ++) {
			if (jsonData.rows[iter].err_cnt > 0) {
				jsonData.rows[iter].err_cnt = '<div style="cursor:pointer" onclick="setAutoErrorPop('+jsonData.rows[iter].idx_db_work_id+')">' + jsonData.rows[iter].err_cnt + '</div>';
				// jsonData.rows[0].err_cnt = '<div onclick="setIndexRecommendPopup('+ ADD_DEL_ERR_POP +')">' + jsonData.rows[0].err_cnt + '<a onclick="alert(1)"> (오류상세)</a></div>';
			}
		}
	}

	json_string_callback_common(JSON.stringify(jsonData),'#addOrDelHistoryList',true);

}
function setAutoErrorPop(idx_db_work_id){

	let url = "/AISQLPVIndexRecommend/getAutoError";
	let jsonData = {};
	jsonData.idx_db_work_id = idx_db_work_id
	let form = createDynamicJqueryForm(jsonData);

	ajaxCall(url,
		form,
		"POST",
		callback_SetAutoErrorPop
	)
}

function setAutoErrorPop_addList(){

	let url = "/AISQLPVIndexRecommend/getAutoError";

	let jsonData = {};
	jsonData.idx_db_work_id = $('#addList').datagrid('getData').rows[0].idx_db_work_id
	let form = createDynamicJqueryForm(jsonData);

	ajaxCall(url,
		form,
			"POST",
		callback_SetAutoErrorPop
		)
}

function callback_SetAutoErrorPop(result){
	if(isEmpty(result)){
		parent.$.messager.alert('오류', '자동 생성/제거 오류정보를 가져오던중 오류가 발생했습니다.', 'error');
		return false;
	}

	result = result.replace('data":"[','data":[');
	result = result.replace('}]"}','}]}');

	if(checkErr(result, '자동 생성/제거 오류정보를 가져오던중 오류가 발생했습니다.') === false){
		return false;
	}
	let jsonData = {};
	jsonData.result = true;
	jsonData.rows = JSON.parse(result).data;

	let length = jsonData.rows.length;

	for(let iter = 0 ; iter < length ; iter ++){
		let err_sbst = jsonData.rows[iter].ERR_SBST;
		if(isNotEmpty(err_sbst)){
			jsonData.rows[iter].ERR_SBST = '<pre>' + err_sbst + '</pre>';
		}
	}

	json_string_callback_common(JSON.stringify(jsonData),'#autoCrerateErrList',true);

}
function createAutoErrList() {
	$("#autoCrerateErrList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect: false,
		selectOnCheck: false,
		columns: [[
			{field: 'RNUM', title: '순번', width: '5%', halign: 'center', align: 'right'},
			{field: 'TABLE_NAME', title: '테이블명', width: '20%', halign: 'center', align: 'left'},
			{field: 'ACCESS_PATH_COLUMN_LIST', title: '추천 인덱스 컬럼', width: '25%', halign: 'center', align: 'left'},
			{field:'ERR_SBST',title:'오류상세',width:'48%',halign:'center',align:'left', styler:err_sbst_style}
		]],
		onLoadSuccess: function (data) {
			if (isEmpty(data.rows) || isEmpty(data.rows[0].TABLE_NAME)) {

				return false;
			}
			setIndexRecommendPopup(ADD_DEL_ERR_POP);
			$('#autoCrerateErrList_div .datagrid-body').scrollLeft(0);
			$("#autoCrerateErrList").datagrid('resize');

			let trObjs = $('#autoCrerateErrList_div .datagrid-view2 .datagrid-body tr')
			let length = trObjs.length;
			let resizeWidth = 0;
			let colOpt = $('#autoCrerateErrList').datagrid('getColumnOption','ERR_SBST');

			for(let i = 0 ; i < length ; i++){
				let col = $(trObjs[i]).find('td:last div');

				if(isEmpty(col)){
					continue;
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

				$('#autoCrerateErrList').datagrid('resize');
			}

		},
		onLoadError: function () {
			closeMessageProgress();
			parent.$.messager.alert('오류', '데이터 조회 중에 에러가 발생하였습니다.', 'error');
		}
	});
}

function err_sbst_style(){
	return 'white-space:nowrap;word-wrap:normal;'
}

function createAddOrDelHistoryList() {

	$("#addOrDelHistoryList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect: false,
		selectOnCheck: false,
		columns: [[
			{field: 'rnum', title: '회차', width: '4%', halign: 'center', align: 'right', rowspan: 2},
			{field: 'exec_status', title: '수행상태', width: '25%', halign: 'center', align: 'center', rowspan: 2, styler : cellStyler},
			{title: '수행결과(인덱스 수)', halign: 'center', align: 'center', colspan: 3},
			{field: 'complete_percent', title: '수행률(%)', width: '6%', halign: 'center', align: 'right', rowspan: 2, styler:style_Gradient},
			{field: 'exec_start_dt', title: '작업시작일시', width: '10%', halign: 'center', align: 'center', rowspan: 2},
			{field: 'exec_end_dt', title: '작업종료일시', width: '10%', halign: 'center', align: 'center', rowspan: 2},
			{field: 'exec_time', title: '수행시간', width: '10%', halign: 'center', align: 'center', rowspan: 2},
			{field:'user_nm',title:'작업자',width:'10%',halign:'center',align:'center',rowspan:2},

		], [
			{field: 'idx_db_work_cnt', title: '전체', width: '6%', halign: 'center', align: 'right'},
			{field: 'completed_cnt', title: '완료', width: '6%', halign: 'center', align: 'right'},
			{field: 'err_cnt', title: '오류', width: '6%', halign: 'center', align: 'right'},
		]],
		onSelect: function (index, row) {
		},
		onLoadSuccess: function (data) {
			if (isEmpty(data.rows) || isEmpty(data.rows[0].rnum)) {
				return false;
			}
			setIndexRecommendPopup(CREATE_HISTORY_ALL);

			let trObjs = $('#addOrDelHistoryList_div .datagrid-view2 .datagrid-body tr')
			let length = trObjs.length;
			let resizeWidth = 0;
			let colOpt = $('#addOrDelHistoryList').datagrid('getColumnOption','err_sbst');

			if(isEmpty(colOpt)){
				return false;
			}

			for(let i = 0 ; i < length ; i++){
				let col = $(trObjs[i]).find('td:last div');
				if(i===36) {
					console.log('i = ' + i + '/ ' + data.rows[i].err_sbst);
				}
				if(isEmpty(col)){
					continue;
				}
				col = col[0];

				let scrollWidth = col.scrollWidth;
				let colWidth = col.clientWidth;

				if(scrollWidth > colWidth){
					resizeWidth = scrollWidth;
				}
			}

			if(resizeWidth > 0){
				colOpt.boxWidth = resizeWidth;
				colOpt.width = resizeWidth;

				$('#addOrDelHistoryList').datagrid('resize');
			}

		},
		onLoadError: function () {
			closeMessageProgress();
			parent.$.messager.alert('오류', '데이터 조회 중에 에러가 발생하였습니다.', 'error');
		}
	});

}

function resetEle(){
	resetCss();
	resetVal();
}

function resetVal(){
	setEasyUiFieldValue('tablespace_name','');
	setEasyUiFieldValue('scriptArea','');

	setEasyUiFieldValue('ignoreErrorYn','check');
	setEasyUiFieldValue('partitionTalbelocalIndex','check');

	setEasyUiFieldValue('maxParallelDegree','check');
	setEasyUiFieldValue('maxParallelDegree_txt','');

	setEasyUiFieldValue('maxParallelDegree_txt','');
}

function resetCss(){

	$('.popupHeader > .searchBtn').css('text-align','none');
	$('.popupHeader > .searchBtn').css('float','right');

	$('#pop_IndexInvisible').css('display','none');
	$('#autoCrerateErrList_div').css('display','none');
	$('#visibleErrList_div').css('display','none');

	$('#pop_TableSpace').css('display','none');
	$('#pop_LocalIndex').css('display','none');
	$('#btn_CreateScript').css('display','none');
	$('#btn_CopyScript').css('display','none');
	$('#ignoreError').css('display','none');

	$('#btn_IndexAutoCreate').css('display','none');
	$('#btn_AutoCreate').css('display','none');
	$('#pop_IndexInvisible').css('display','none');
	$('#ignoreError').css('display','none');

	$('#popup_area').css('display','none');
	$('#addOrDelHistoryList_div').css('display','none');
	$('#pop_MaxParallel_div').css('display','none');
	$('.popupHeader').css('display','block');
}

function copyScript(){
	let obj = document.getElementById('popup_area');
	
	let content = obj.querySelector('span > textarea')
	content.select();
	document.execCommand('copy');
}

function closePopup(){
	resetEle();
	$('#indexRecommendPopup').window("close");
}

function getScript(type){
	setEasyUiFieldValue("scriptArea","");

	switch (CURRENT_POP) {
		case CREATE_INDEX_SCRIPT :
		case AUTO_CREATE_INDEX :
			checkTableSpaceExists(CURRENT_POP);
			break;
		case DROP_INDEX_SCRIPT :
			getDropScript();
			break;
		case AUTO_DROP_INDEX :
			break;
	}
}

function getDropScript(){

	let rows = $("#indexList").datagrid('getChecked');

	let form = setCrearteScriptForm(rows);
	if (parent.openMessageProgress != undefined) parent.openMessageProgress('인덱스 제거 스크립트를 생성 중 입니다.'," ");

	if(isEmpty(form)){
		closeMessageProgress();
		parent.$.messager.alert('오류','제거 가능한 인덱스가 없습니다.');
		return false;
	}

	// 12시간
	ajaxCallWithTimeout("/AISQLPVIndexRecommend/getDropScript",
				form,
				"POST",
				callback_GetDropScript,
				43200
	);
}

function callback_GetDropScript(result){
	if(isEmpty(result)){
		closeMessageProgress();
		parent.$.messager.alert('오류','스크립트 생성중 오류가 발생했습니다.');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(jsonData.is_error === 'true' || jsonData.result === false){
		closeMessageProgress();
		parent.$.messager.alert('오류','스크립트 생성중 오류가 발생했습니다.');
		return false;
	}

	closeMessageProgress();

	setIndexRecommendPopup(DROP_INDEX_SCRIPT);
	if(isEmpty(jsonData.index_drop_script)){
		setEasyUiFieldValue("scriptArea",'제거할 인덱스가 없습니다.');
	}else{
		setEasyUiFieldValue("scriptArea",jsonData.index_drop_script);
	}

}


function checkTableSpaceExists(type){

	let callbackParam = '';

	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스크립트 생성중입니다."," ");

	if(checkRequiredFieldAndShowMsg('tablespace_name') === false){
		return false;
	}

	let jsonData ={};

	let tablespace_name = getEasyUiFieldValue('tablespace_name');
	setEasyUiFieldValue('tablespace_name',tablespace_name.toUpperCase());

	jsonData.tablespace_name = tablespace_name.toUpperCase();
	jsonData.perf_check_target_dbid = $('#perf_check_target_dbid').val();

	let form = createDynamicJqueryForm(jsonData);

	switch (type){
		case CREATE_INDEX_SCRIPT :
			callbackParam = createScript;
			break;
		case AUTO_CREATE_INDEX :
			callbackParam = autoGenerateIndex;
			break;
		default : return false;
	}


	ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/checkTableSpaceExists",
				form,
				"POST",
				callbackCheckTableSpaceExists,
				callbackParam);
}

function callbackCheckTableSpaceExists(result ,param){
	if( checkErr(result, '테이블 스페이스 존재 여부 조회중 오류가 발생했습니다.') === false ){
		return false;
	}
	let jsonData = JSON.parse(result);

	if(isNotEmpty(jsonData.tablespace_yn) && jsonData.tablespace_yn === 'Y'){

	}else{
		closeMessageProgress();
		parent.$.messager.alert('정보','테이블스페이스가 존재하지 않습니다. 테이블스페이스명을 재확인 바랍니다.');
		return false;
	}

	param(result);
}

function autoGenerateIndex(result){

	let rows = $("#indexList").datagrid('getChecked');

	let form = setAutoGenerateORDropIndexForm(rows, "C");

	ajaxCall("/AISQLPVIndexRecommend/autoGenerateIndex",
				form,
				"POST",
				callback_AutoGenerateIndex
				);

}

function callback_AutoGenerateIndex(result){
	if(isEmpty(result)){
		closeMessageProgress();
		parent.$.messager.alert('오류','인덱스 생성중 오류가 발생했습니다.');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(jsonData.is_error === 'true' || jsonData.result === false){
		closeMessageProgress();
		parent.$.messager.alert('오류','인덱스 생성중 오류가 발생했습니다.');
		return false;
	}

	if(isNotEmpty(jsonData.target_list_yn) && jsonData.target_list_yn ==='N'){
		closeMessageProgress();
		parent.$.messager.alert('정보','생성 가능한 인덱스가 없습니다.');
		return false;
	}

	setTimeout(function() {
		// 수행 결과 조회
		closeMessageProgress();
		$(' #autoProcessRefresh').switchbutton("check");
		closePopup();
	},300)
}


function setAutoGenerateORDropIndexForm(rows , idxWorkDivCd){

	let data = {};
	let checkedAll = $('.datagrid-header-check > input').prop('checked');

	if(checkedAll === true){
		data.isWholeChoice = 'Y';
	}else{
		data.isWholeChoice = 'N';
		Object.assign(data, getCreateScriptjsonData(rows));
	}
	data.asisDbid = rows[0].ASIS_DBID;
	data.tobeDbid = rows[0].TOBE_DBID;
	data.idxAdNo = rows[0].IDX_AD_NO;
	data.tablespace_name = getEasyUiFieldValue('tablespace_name');

	if(getEasyUiFieldValue('partitionTalbelocalIndex')){
		data.partitionTableLocalIndexCreateYn = 'Y';
	}else{
		data.partitionTableLocalIndexCreateYn = 'N';
	}

	if(getEasyUiFieldValue('ignoreErrorYn')){
		data.err_ignore_yn = 'Y';
	}else{
		data.err_ignore_yn = 'N';
	}

	data.execseq = rows[0].EXEC_SEQ
	data.idxWorkDivCd = idxWorkDivCd;
	data.maxParallelDegreeYn = getEasyUiFieldValue('maxParallelDegree') === true ? 'Y' : 'N';
	data.maxParallelDegree = getEasyUiFieldValue('maxParallelDegree_txt');

	data.search_owner = $('#search_owner').val();
	data.search_recommendType = $('#search_recommendType').val();
	data.lastRecommendTypeCd = $('#search_spsRecommendIndex').val();
	if(data.lastRecommendTypeCd === null){
		data.lastRecommendTypeCd = '';
	}
	data.search_tableName = $('#search_tableName').val();

	return createDynamicJqueryForm(data);
}

function createScript(result){

	let rows = $("#indexList").datagrid('getChecked');

	let form = setCrearteScriptForm(rows);

	if(isEmpty(form)){
		closeMessageProgress();
		parent.$.messager.alert('오류','생성 가능한 인덱스가 없습니다.');
		return false;
	}

	ajaxCall("/AISQLPVIndexRecommend/getCreateScript",
				form,
				"POST",
				callback_GetCreateScript
	);
}

function setCrearteScriptForm(rows){

	let data = {};
	let checkedAll = $('.datagrid-header-check > input').prop('checked');

	if(checkedAll === true){
		data.isWholeChoice = 'Y';
	}else{
		if(isEmpty(rows[0])){
			return null;
		}
		data.isWholeChoice = 'N';
		Object.assign(data, getCreateScriptjsonData(rows));
	}


	data.search_owner = $('#search_owner').val();
	data.search_recommendType = $('#search_recommendType').val();
	data.lastRecommendTypeCd = $('#search_spsRecommendIndex').val();
	if(data.lastRecommendTypeCd === null){
		data.lastRecommendTypeCd = '';
	}
	data.search_tableName = $('#search_tableName').val();
	data.tobeDbid = $('#perf_check_target_dbid').val();;
	data.idxAdNo = rows[0].IDX_AD_NO;
	data.tablespace_name = getEasyUiFieldValue('tablespace_name');
	data.maxParallelDegreeYn = getEasyUiFieldValue('maxParallelDegree') === true ? 'Y' : 'N';
	data.maxParallelDegree = getEasyUiFieldValue('maxParallelDegree_txt');


	if(getEasyUiFieldValue('partitionTalbelocalIndex')){
		data.partitionTableLocalIndexCreateYn = 'Y';
	}else{
		data.partitionTableLocalIndexCreateYn = 'N';
	}

	return createDynamicJqueryForm(data);
}

function callback_GetCreateScript(result){
	if(isEmpty(result)){
		closeMessageProgress();
		parent.$.messager.alert('오류','스크립트 생성중 오류가 발생했습니다.');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(jsonData.is_error === 'true' || jsonData.result === false){
		closeMessageProgress();
		parent.$.messager.alert('오류','스크립트 생성중 오류가 발생했습니다.');
		return false;
	}
	if(isEmpty(jsonData.index_create_script)){
		setEasyUiFieldValue("scriptArea",'생성할 인덱스가 없습니다.');
	}else{
		setEasyUiFieldValue("scriptArea",jsonData.index_create_script);
	}

	closeMessageProgress();
}

function getCreateScriptjsonData(rows){

	let jsonData = {};
	jsonData.idxRecommendSeq = '';
	jsonData.tableOwner = '';
	jsonData.tableName = '';
	jsonData.recommendType = '';
	jsonData.tempIndexName = '';
	jsonData.sourceIndexName = '';
	jsonData.accessPathColumnList = '';
	jsonData.sourceIndexOwner='';

	rows.forEach(function(row){
		jsonData.idxRecommendSeq += nvl(row.IDX_RECOMMEND_SEQ,'null') + ',';
		jsonData.tableOwner += nvl(row.TABLE_OWNER,'null') + ',';
		jsonData.tableName += nvl(row.TABLE_NAME,'null') + ',';
		jsonData.recommendType += nvl(row.RECOMMEND_TYPE,'null') + ',';
		jsonData.tempIndexName += nvl(row.INDEX_NAME,'null') + ',';
		jsonData.sourceIndexOwner += nvl(row.SOURCE_INDEX_OWNER,'null') + ',';
		jsonData.sourceIndexName += nvl(row.SOURCE_INDEX_NAME,'null') + ',';
		jsonData.accessPathColumnList += nvl(row.ACCESS_PATH_COLUMN_LIST,'null') + ',';

	});

	jsonData.idxRecommendSeq = removeLastchar(jsonData.idxRecommendSeq,',');
	jsonData.tableOwner = removeLastchar(jsonData.tableOwner,',');
	jsonData.tableName = removeLastchar(jsonData.tableName,',');
	jsonData.recommendType = removeLastchar(jsonData.recommendType,',');
	jsonData.tempIndexName = removeLastchar(jsonData.tempIndexName,',');
	jsonData.sourceIndexOwner = removeLastchar(jsonData.sourceIndexOwner,',');
	jsonData.sourceIndexName = removeLastchar(jsonData.sourceIndexName,',');
	jsonData.accessPathColumnList = removeLastchar(jsonData.accessPathColumnList,',');

	return jsonData;
}



function removeLastchar(str,word){
	return str.substring(0,str.lastIndexOf(word));
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

function autoCreate(){

	let param = "확인";
	let msgStr ="자동 생성을 실행 하시겠습니까?";


	let jsonData = {};
	jsonData.perf_check_target_dbid = $('#perf_check_target_dbid').val();
	jsonData.invisible_index_list = autoCreateList;
	let form = createDynamicJqueryForm(jsonData);

	if(checkRequiredFields()) {
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("자동 생성 작업을 수행 중 입니다.."," ");
				ajaxCall("/AISQLPVIndexRecommend/autoCreateIndexInvisible",
							form,
							"POST",
							callback_AutoCreate
				);
			}
		});
	}
}

function callback_AutoCreate(result){
	if( checkErr(result, '자동 생성 중 오류가 발생했습니다.') === false ) {
		closeMessageProgress();
	}else{
		let jsonData = JSON.parse(result);
		if((jsonData.err_no*1) > 0){
			closeMessageProgress();
			let sql =  jsonData.sql.join('<br/>');
			if(isEmpty(sql)){
				sql ='';
			}
			setCall_BackAutoCreateFailPop(jsonData);
			// parent.$.messager.alert('정보', '자동생성 작업중 ' + jsonData.err_no +'건의 작업이 실패했습니다.</br>'+sql,'');
		}else{
			closeMessageProgress();
			parent.$.messager.alert('정보', '자동생성 작업이 완료되었습니다.','',function(){getIndexRecommend()});
			closePopup();
		}
	}
}

function setCall_BackAutoCreateFailPop(jsonData){
	$('#visibleErrList_div').css('display','block');
	createAutoCreateFailDatagrid();
	jsonData.rows = [];

	let length = jsonData.err_no;
	let sqls = jsonData.sql;
	let sql = '';
	for (let iter = 0; iter < length; iter++) {
		jsonData.rows[iter] = {};
		jsonData.rows[iter].RNUM = iter;
		jsonData.rows[iter].SQL = sqls[iter];
		sql += sqls[iter] + '\n';
	}

	json_string_callback_common(JSON.stringify(jsonData), '#visibleErrList', true);

	let title = '자동 생성 오류'
	let height = '400px';
	let width = '700px';
	let top = '430';
	let left = '650';

	$('#indexRecommendPopup3').window({
		title : title,
		height : height,
		width : width,
		top:getWindowTop(top),
		left:getWindowLeft(left),
		resizable:false
	});
	$("#indexRecommendPopup3").window("open");
	$('#visibleErrList').datagrid('resize');
	setEasyUiFieldValue('scriptArea',sql);
}

function createAutoCreateFailDatagrid(){
	$("#visibleErrList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect: false,
		selectOnCheck: false,
		columns: [[
			{field: 'RNUM', title: '순번', width: '8%', halign: 'center', align: 'right'},
			{field: 'SQL', title: '실패 SQL', width: '90%', halign: 'center', align: 'LEFT'},
		]],
		onLoadSuccess: function (data) {
		},
		onLoadError: function () {
			closeMessageProgress();
			parent.$.messager.alert('오류', '데이터 조회 중에 에러가 발생하였습니다.', 'error');
		}
	});
}
