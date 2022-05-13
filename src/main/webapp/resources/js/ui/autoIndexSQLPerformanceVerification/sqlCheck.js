$(document).ready(function() {
	$('body').css('visibility', 'visible');
	
	createProjectCombo();
	createList();
	
	$('.datagrid-header-check').css('height','35px');
	$('.datagrid-header-check').html('<p style="line-height:150%;">선<br>택</p>');
	
	/* modal progress close */
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function createProjectCombo(){
	// 프로젝트 조회
	$('#project_combo').combobox({
		url:'/AutoPerformanceCompareBetweenDatabase/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			$('[name=project_id]').val(data.project_id);
		},
		onLoadError: function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}

function createList() {
	$('#tableList').datagrid({
		view: myview,
		singleSelect: 'true',
		checkOnSelect : 'false',
		selectOnCheck : 'true',
		columns:[[
			{field:'chk',halign:'center',align:'center',checkbox:true, rowspan:2},
			{field:'perf_check_name',title:'SQL점검팩명',halign:'center',width:'200px',align:'left', rowspan:2},
			{field:'perf_check_desc',title:'SQL점검팩 설명',halign:'center',width:'200px',align:'left', rowspan:2},
			{field:'original_db_name',title:'ASIS DB<br>(원천DB)',width:'70px',halign:'center',align:'left', rowspan:2},
			{field:'perf_check_target_db_name',title:'TOBE DB<br>(목표DB)',width:'70px',halign:'center',align:'left', rowspan:2},
			{field:'check_range_period',title:'수집기간',width:'150px',halign:'center',align:'center', rowspan:2},
			{field:'topn_cnt',title:'TOP N',width:'100px',halign:'center',align:'right', rowspan:2},
			{field:'owner_list',title:'Owner',width:'200px',halign:'center',align:'left', rowspan:2},
			{field:'module_list',title:'Module',width:'200px',halign:'center',align:'left', rowspan:2},
			{field:'parallel_degree',title:'병렬실행',width:'70px',halign:'center',align:'right',rowspan:2},
			{field:'dml_exec_yn',title:'DML 실행',width:'70px',halign:'center',align:'center',rowspan:2},
			{field:'multiple_exec_cnt',title:'Multiple 실행',width:'80px',halign:'center',align:'right',rowspan:2},
			{field:'multiple_bind_exec_cnt',title:'Multiple Bind<br>실행',width:'80px',halign:'center',align:'right',rowspan:2},
			{field:'sql_time_limt_nm',title:'SQL Time<br>Limit(분)',width:'70px',halign:'center',align:'right',rowspan:2},
			{field:'max_fetch_cnt',title:'최대 Fetch<br>건수',width:'70px',halign:'center',align:'right',rowspan:2},
			{field:'perf_check_exec_end_dt',title:'최종수행일시',width:'70px',halign:'center',align:'right',rowspan:2},
			{field:'perf_check_exec_time',title:'수행시간',width:'70px',halign:'center',align:'right',rowspan:2},
			{field:'perf_check_force_close_yn',title:'강제완료<br>처리',width:'70px',halign:'center',align:'center',rowspan:2},
			{field:'user_nm',title:'수행자명',width:'70px',halign:'center',align:'left',rowspan:2},
			
			{field:'all_sql_yn',hidden:true, rowspan:2},
			{field:'sql_auto_perf_check_id',hidden:true, rowspan:2}
		],[
		]],
		onLoadError:function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
		}
	});
}
/* 검색 */
function Btn_OnClick(){
	if( formValidationCheck() ){
		$('#currentPage').val('1');
		$('#pagePerCount').val('20');
		
		$('#tableList').datagrid('loadData', []);
		
		ajaxCallTableList();
		
	}else {
		return
	}
}

function fnSearch() {
	ajaxCallTableList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress('SQL점검팩 조회', ' ');
	
	/* SQL점검팩 리스트 */
	ajaxCall('/AISQLPV/loadSqlPerformancePacList',
			$('#submit_form'),
			'GET',
			callback_LoadSqlPerformancePacListAction);
}

var callback_LoadSqlPerformancePacListAction = function(result) {
	json_string_callback_common(result, '#tableList', true);

	let dataLength = JSON.parse(result).dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if (parent.closeMessageProgress != undefined){
		parent.closeMessageProgress();
	}
}

// SQL점검팩 삭제
function Btn_delete() {
	let data = $('#tableList').datagrid('getChecked');
	
	if ( data.length > 0 ) {
		$('#sql_auto_perf_check_id_popup').val(data[0].sql_auto_perf_check_id);
		
		ajaxCall('/AISQLPV/checkUnfinishedCount',
				$('#popup_form'),
				'POST',
				callback_checkUnfinishedCount);
	} else {
		warningMessager('삭제할 점검팩을 선택해 주세요.');
	}
}
var callback_checkUnfinishedCount = function(result) {
	let message = result.message;
	
	if ( result.result ){
		if ( isEmpty(message) ){
			ajaxCall_deleteSqlPerfInfo();
			
		}else {
			parent.$.messager.confirm( '확인', message ,function(check) {
				if (check) {
					ajaxCall_deleteSqlPerfInfo();
				}
			});
		}
	}else if ( isEmpty(result.status) ){
		infoMessager( message );
		
	}else {
		errorMessager( message );
	}
}

function ajaxCall_deleteSqlPerfInfo(){
	let data = $('#tableList').datagrid('getChecked');
	$('#sql_auto_perf_check_id_popup').val(data[0].sql_auto_perf_check_id);
	
	ajaxCall("/AISQLPV/deleteSqlPerfInfo",
			$("#popup_form"),
			"POST",
			callback_deleteSqlPerfInfo);
}
var callback_deleteSqlPerfInfo = function(result) {
	let msg = "";
	
	try{
		let data = $('#tableList').datagrid('getChecked')[0];
		
		if (result.result) {
			msg = '점검팩 [' + data.perf_check_name + '] 가 삭제 되었습니다.';
			
			parent.reloadSqlPacCombo( data.project_id, data.sql_auto_perf_check_id );
			
		}else {
			msg = '점검팩 [' + data.perf_check_name + '] 삭제에 실패하였습니다.<br>관리자에게 문의 바랍니다.';
		}
		
	}catch(err){
		msg = '점검팩 삭제에 실패하였습니다.<br>관리자에게 문의 바랍니다.';
		console.log('Error Occured', err);
		
	}finally{
		infoMessager( msg );
		Btn_OnClick();
	}
}
/* 등록 팝업 호출 */
function showSaveSQLPerformance_popup() {
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 먼저 선택해 주세요.');
		return false;
	}
	
	Btn_ResetField();
	
	loadSqlPerformancePopupCombo();
	
	let selectData = $('#tableList').datagrid('getChecked');
	if( selectData.length > 0 ){
		let popupSqlAutoPerfCheckId = selectData[0].sql_auto_perf_check_id;
		
		$('#sqlPerformanceP_popup').combobox('setValue', popupSqlAutoPerfCheckId );
	}
	
	$('#saveSQLPerformance').window({
		title : "SQL점검팩",
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	$('#saveSQLPerformance').window('open');
}

function loadSqlPerformancePopupCombo() {
	let projectId = $('#project_combo').combobox('getValue');
	let database_kinds_cd = $('#database_kinds_cd').val();
	
	$('#sqlPerformanceP_popup').combobox({
		url:'/AISQLPV/getSqlPerformancePacList?project_id='+projectId
		+'&database_kinds_cd='+database_kinds_cd +'&perf_check_type_cd=4',
		method:'GET',
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onChange: function(newValue,oldValue) {
			$('#sql_auto_perf_check_id_popup').val(newValue);
			
			ajaxCall('/AutoPerformanceCompareBetweenDatabase/getSqlPerformanceInfo',
					$('#popup_form'),
					'POST',
					callback_setPopUpData);
		},
		onLoadError: function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}
var callback_setPopUpData = function(result){
	let data = JSON.parse(result);
	
	if ( data && data.length > 0 ) {
		$('#perf_check_name_popup').textbox('setValue',replaceHtmlTagToNormal(data[0].perf_check_name));
		$('#perf_check_desc_popup').textbox('setValue',replaceHtmlTagToNormal(data[0].perf_check_desc));
		
	}else {
		return;
	}
}

function Btn_ResetField() {
	$('#sqlPerformanceP_popup').combobox('clear');
	$('#perf_check_name_popup').textbox('setValue','');
	$('#perf_check_desc_popup').textbox('setValue','');
}

function Btn_SaveSqlPerfPac() {
	if( saveValidationCheck() ){	// 저장
		if ( $('#sqlPerformanceP_popup').combobox('getValue') == '' ) {
			ajaxCall('/AISQLPV/insertSqlPerformanceInfo',
					$('#popup_form'),
					'POST',
					callback_SaveSqlPerfPac);
			
		}else {	// 수정
			ajaxCall('/AISQLPV/updateSqlPerformanceInfo',
					$('#popup_form'),
					'POST',
					callback_SaveSqlPerfPac);
		}
	}else {
		return;
	}
}
var callback_SaveSqlPerfPac = function(result) {
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				Btn_OnClick();
				
				$('#saveSQLPerformance').window('close');
			},500);
		});
		
	}else {
		infoMessager(result.message);
	}
}

function formValidationCheck() {
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
		
	}else {
		return true;
	}
}

function saveValidationCheck() {
	if ( $('#perf_check_name_popup' ).textbox('getValue') == '' ) {
		warningMessager('SQL 점검팩명을 입력해 주세요.');
		return false;
	}
	
	if ( $('#perf_check_desc_popup').textbox('getValue') == '' ) {
		warningMessager('SQL 점검팩 설명을 입력해 주세요.');
		return false;
	}
	
	if ( byteLength( $('#perf_check_name_popup' ).textbox('getValue') ) > 100 ||
			byteLength( $('#perf_check_desc_popup' ).textbox('getValue') ) > 100 ) {
		
		warningMessager('입력한 값이 100byte 초과합니다.<br> 다시 입력해 주세요.');
		return false;
	}
	
	return true;
}