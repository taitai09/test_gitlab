var searchFlag = false;
var rsa;
var isDevRole = false;

$(document).ready(function(){
	$("body").css("visibility", "visible");
	
	$('[name=job_scheduler_type_cd]').val('37');
	
	createCombobox('#project_id');
	createCombobox('#project_id_modify');
	customCombobox();
	
	createDbCombo();
	createList();
	Btn_OnClick();
	
	createCycleCombo();
	createMethodCombo();
	
	closePageLoadMessageProgress();
});

function customCombobox(){
	$('#project_id').combobox({
		onLoadSuccess: function(){
			$(this).combobox('setValue','selectAll');
		}
	});
	
	$('#project_id_modify').combobox({
		onChange:function(newValue,oldValue){
			if(newValue != ''){
				selectProject(newValue);
				
			}else {
				Btn_ResetField();
			}
			
			$('#btnDelete').attr('onClick','Btn_PreventDelete();');
		},
		onLoadSuccess: function(){
			$(this).combobox('textbox').attr('placeholder','선택');
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function selectProject(projectId){
	let rows =$('#tableList').datagrid('getRows');
	let data;
	if( rows.length > 0 ){
		data = filerByProjectId(projectId, rows);
	}
	
	if( data && data.length > 0 ){
		setDataToInput(data[0]);
		
	}else {
		ajaxCall_SchedulerList_combobox(projectId);
	}
}

function createDbCombo(){
	$('#db_name_combo').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function createCycleCombo(){
	$('#date_fir').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1088",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue,oldValue){
			$('[name=exec_cycle]').attr('data-part',newValue);
			
			$('#date_scnd').combobox('setValue','');
			$('#date_thrd').combobox('setValue','');
			$('#date_frth').combobox('setValue','');
			
			if(newValue == ''){
				$('#date_scnd').combobox("readonly",false);
				
				$('#date_scnd').combobox('loadData',[]);
				$('#date_thrd').combobox('loadData',[]);
				$('#date_frth').combobox('loadData',[]);
				
				$('#date_scnd').combobox('textbox').attr( 'placeholder' , '' );
				$('#date_thrd').combobox('textbox').attr( 'placeholder' , '' );
				$('#date_frth').combobox('textbox').attr( 'placeholder' , '' );
				
				$('#execOption').attr('name','');
				
			}else {
				loadSecondCombo(newValue);
			}
		},
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	$('#date_scnd').combobox({
		valueField:'dateVal',
		textField:'dateTxt',
		onChange:function(newValue,oldValue){
			$('#date_thrd').combobox('setValue','');
			$('#date_frth').combobox('setValue','');
			
			$('#execOption').val(newValue);
		},
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	$('#date_thrd').combobox({
		valueField:'timeVal',
		textField:'timeTxt',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	$('#date_frth').combobox({
		valueField:'minuteVal',
		textField:'minuteTxt',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
}

function loadSecondCombo(newValue){
	$('#date_scnd').combobox('setValue','');
	
	if( newValue == '1' ){
		$('#date_scnd').combobox("readonly",true);
		$('#execOption').attr('name','');
		$('#execOption').val('');
		
	}else{
		$('#date_scnd').combobox("readonly",false);
		$('#date_scnd').combobox("textbox").attr("required",true);
		
		let scndList;
		if( newValue == '2' ){
			$('#execOption').attr('name','exec_day_of_week');
			
			// 요일 콤보박스
			scndList = [
				 {'dateVal': 'SUN', 'dateTxt': 'SUN'}
				,{'dateVal': 'MON', 'dateTxt': 'MON'}
				,{'dateVal': 'TUE', 'dateTxt': 'TUE'}
				,{'dateVal': 'WED', 'dateTxt': 'WED'}
				,{'dateVal': 'THU', 'dateTxt': 'THU'}
				,{'dateVal': 'FRI', 'dateTxt': 'FRI'}
				,{'dateVal': 'SAT', 'dateTxt': 'SAT'}
				];
			
		}else {
			$('#execOption').attr('name','exec_day');
			
			// 날짜 콤보박스
			scndList = [];
			for(date = 1; date <= 31; date++){
				scndList.push( {'dateVal': date,'dateTxt': date+'일'} );
			}
		}
		$('#date_scnd').combobox('loadData',scndList);
	}
	
	// 시간 콤보박스
	let timeList = [{'time': ''}];
	for(time = 0; time <= 23; time++){
		timeList.push( {'timeTxt': String(time)+'시', 'timeVal': String(time)});
	}

	$('#date_thrd').combobox('loadData',timeList);
	
	// 분 콤보박스
	let minuteList = [{'minute': ''}];
	for(minute = 0; minute < 60; minute++){
		minuteList.push( {'minuteTxt': String(minute)+'분', 'minuteVal': String(minute)});
	}
	
	$('#date_frth').combobox('loadData',minuteList);
		
}

function createMethodCombo(){
	$('#svn_if_meth_cd').combobox({
		valueField:'methCode',
		textField:'methNm',
		onChange:function(newValue,oldValue){
			RequiredSetting(newValue);
		},
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	let codeList =[
		  {'methCode':'1', 'methNm':'SFTP'}
		, {'methCode':'2', 'methNm':'FTP'}
		, {'methCode':'3', 'methNm':'로컬디렉토리(NAS형태)'}
	];
	
	$('#svn_if_meth_cd').combobox('loadData', codeList);
}

function RequiredSetting(newValue){
	if(newValue == '3' ){
		$('#svn_ip').textbox("clear");
		$('#svn_port').textbox("clear");
		$('#svn_os_user_id').textbox("clear");
		$('#pre_svn_os_user_password').passwordbox("clear");
		$('#pre_svn_os_user_password').val("");
		
		$('#svn_ip').textbox("readonly",true);
		$('#svn_port').textbox("readonly",true);
		$('#svn_os_user_id').textbox("readonly",true);
		$('#pre_svn_os_user_password').passwordbox("readonly",true);
		
	}else {
		$('#svn_ip').textbox("readonly",false);
		$('#svn_port').textbox("readonly",false);
		$('#svn_os_user_id').textbox("readonly",false);
		$('#pre_svn_os_user_password').passwordbox("readonly",false);
		
	}
}

function createList(){
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'job_scheduler_nm',title:'스케줄러명',width:'18%',halign:'center',align:'left'},
			{field:'exec_start_dt',title:'시작일자',width:'6%',halign:'center',align:'center'},
			{field:'exec_end_dt',title:'종료일자',width:'6%',halign:'center',align:'center'},
			{field:'exec_cycle',title:'실행주기',width:'12%',halign:'center',align:'left'},
			{field:'std_qty_target_db_name',title:'타겟DB',width:'6%',halign:'center',align:'center'},
			{field:'std_qty_target_db_user_id',title:'DB 유저 ID',width:'6%',halign:'center',align:'center'},
			{field:'parse_code',title:'Parser Code',width:'6%',halign:'center',align:'center'},
			{field:'project_nm',title:'프로젝트',width:'18%',halign:'center',align:'left'},
			{field:'job_scheduler_desc',title:'스케줄러 설명',width:'18%',halign:'center',align:'left'},
			
			{field:'svn_if_meth_cd',title:'연동방법',width:'5%',halign:'center',align:'center', formatter:format},
			{field:'svn_dir_nm',title:'형상관리 시스템 디렉토리',width:'20%',halign:'center',align:'left'},
			{field:'svn_ip',title:'IP',width:'6%',halign:'center',align:'center'},
			{field:'svn_port',title:'PORT',width:'5%',halign:'center',align:'center'},
			{field:'svn_os_user_id',title:'사용자 ID',width:'5%',halign:'center',align:'center'},
			{field:'project_id',hidden:true},
			{field:'dbid',hidden:true},
			{field:'sql_std_qty_scheduler_no',hidden:true},
			{field:'exec_cycle_div_cd',hidden:true},
		]],
		onSelect:function(index, row) {
			$('#project_id_modify').combobox('setValue', row.project_id);
			setDataToInput(row);
			
			$('#btnDelete').attr('onClick','Btn_DeleteScheduler();');
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function format( value,row,index ) {
	if ( value == '1' ) {
		return 'SFTP';
	} else if ( value == '2'){
		return 'FTP';
	} else if ( value == '3'){
		return '로컬디렉토리(NAS형태)';
	}else {
		console.log('Something is wrong');
		return'';
	}
}

function filerByProjectId(newValue, rows) {
	return rows.filter(function(row){
				return row.project_id == newValue;
			});
}
/* 검색 */
function Btn_OnClick(){
	$('#tableList').datagrid("loadData", []);
	$('#project_id_modify').combobox('setValue','');
	
	$('#excel_project_id').val( $('#project_id').combobox('getValue') );
		
	ajaxCall_SchedulerList();
}

function ajaxCall_SchedulerList(){
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress("스케쥴러 조회", " ");
	
	ajaxCall("/manageScheduler/loadSchedulerList",
			$("#submit_form"),
			"GET",
			callback_SchedulerList);
}

var callback_SchedulerList = function(result){
	try {
		json_string_callback_common(result, '#tableList', true);
		
		searchFlag = true;
		
	} catch(err) {
		console.log(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function ajaxCall_SchedulerList_combobox(project_id){
	let std_qty_scheduler_div_cd = $('#detail_form [name=std_qty_scheduler_div_cd]').val();
	
	ajaxCall("/manageScheduler/loadSchedulerList?std_qty_scheduler_div_cd="+ std_qty_scheduler_div_cd +"&project_id=" + project_id,
			null,
			"GET",
			callback_SchedulerList_combobox);
}

var callback_SchedulerList_combobox = function(result){
	try {
		let data = JSON.parse(result);
		
		setDataToInput(data[0]);
		
	} catch(err) {
		console.log(err.message);
	}
}

/* 저장 */
function Btn_SaveSetting(){
	if( checkCondition() == true ){
		makeExecCycle();
		if ( $('#svn_if_meth_cd').combobox('getValue') != '3' ) {
			encryptRSA();
		}
		$('#std_qty_target_db_name').val($('#db_name_combo').combobox('getText'));
		
		ajaxCall_SaveSetting();
	}
}

function ajaxCall_SaveSetting(){
	ajaxCall('/manageScheduler/saveSetting',
			$('#detail_form'),
			"POST",
			callback_SaveSetting);
}

var callback_SaveSetting = function(result){
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				$('#project_id').combobox('setValue', 'selectAll');
				Btn_OnClick();
				
				$('#project_id_modify').combobox('setValue','');
				
				},500);
		});
		
	} else {
		parent.$.messager.alert('정보',result.message,'info');
	}
}

/* 수정 */
function Btn_ModifySetting(){
	if( checkCondition() == true ){
		makeExecCycle();
		if ( $('#svn_if_meth_cd').combobox('getValue') != '3' ) {
			encryptRSA();
		}
		$('#std_qty_target_db_name').val($('#db_name_combo').combobox('getText'));
		
		ajaxCall_ModifySetting();
	}
}

function ajaxCall_ModifySetting(){
	ajaxCall('/manageScheduler/modifySetting',
			$('#detail_form'),
			"POST",
			callback_SaveSetting);
}

/* 삭제 */
function Btn_PreventDelete(){
	parent.$.messager.alert('경고','데이터를 선택해 주세요.','warning');
}

function Btn_DeleteScheduler(){
	parent.$.messager.confirm('확인', '삭제하시겠습니까?', function(r) {
		if (r) {
			ajaxCall_DeleteScheduler();
		}
	});
}

function ajaxCall_DeleteScheduler(){
	ajaxCall('/manageScheduler/deleteScheduler',
			$('#detail_form'),
			"POST",
			callback_DeleteScheduler);
}

var callback_DeleteScheduler = function(result){
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				$('#project_id').combobox('setValue', 'selectAll');
				Btn_OnClick();
				
				},500);
		});
		
	} else {
		parent.$.messager.alert('정보',result.message,'info');
	}
}

function Excel_Download() {
	if ( searchFlag == false ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
		
	}
	
	$("#excel_submit_form").attr("action","/manageScheduler/excelDownload");
	$("#excel_submit_form").submit();
	$("#excel_submit_form").attr("action","");
}

/* 초기화 버튼 */
function Btn_ResetField(){
	$('#project_id_modify').combobox('setValue','');
	setDataToInput(null);
}

function checkCondition() {
	if ( $('#project_id_modify').combobox('getValue') == '' ) {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	let job_scheduler_nm = $('#job_scheduler_nm').textbox('getValue');
	if ( job_scheduler_nm == '' ) {
		parent.$.messager.alert('경고','스케쥴러명을 입력해 주세요.','warning');
		return false;
		
	}else {
		if( byteCheck(job_scheduler_nm) > 100){
			parent.$.messager.alert('경고','스케쥴러명이 100byte 초과합니다.<br> 다시 입력해 주세요.','warning');
			return false;
		}
	}
	
	let startDate = $('#exec_start_dt').datebox('getText');
	if ( startDate == '' ) {
		parent.$.messager.alert('경고','시작일자를 입력해 주세요.','warning');
		return false;
		
	}else {
		startDate = strToDate(startDate);
	}
	
	let endDate = $('#exec_end_dt').datebox('getText');
	if ( endDate == '' ) {
		parent.$.messager.alert('경고','종료일자를 입력해 주세요.','warning');
		return false;
		
	}else {
		endDate = strToDate(endDate);
	}
	
	if ( startDate > endDate ){
		parent.$.messager.alert('경고','시작일자와 종료일자를 확인해 주세요.','warning');
		$('#sqlPerformanceP').combobox('clear');
		return false;
	}
	
	if ( $('#date_fir').combobox('getValue') == '' ||
			( $('[name=exec_cycle]').attr('data-part') != '1' && $('#date_scnd').combobox('getValue') == '' ) ||
			$('#date_thrd').combobox('getValue') == '' ||
			$('#date_frth').combobox('getValue') == '') {
		
		parent.$.messager.alert('경고','실행주기를 입력해 주세요.','warning');
		return false;
	}
	
	if ( $('#db_name_combo').combobox('getValue') == '' ) {
		parent.$.messager.alert('경고','타겟DB를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $('#parse_code').textbox('getValue') == '' ) {
		parent.$.messager.alert('경고','Parser Code를 입력해 주세요.','warning');
		return false;
	}
	
	let job_scheduler_desc = $('#job_scheduler_desc').textbox('getValue');
	if( byteCheck(job_scheduler_desc) > 400){
		parent.$.messager.alert('경고','스케줄러 설명이 400byte 초과합니다.<br> 다시 입력해 주세요.','warning');
		return false;
	}
	
	let svn_if_meth_cd = $('#svn_if_meth_cd').combobox('getValue');
	if ( svn_if_meth_cd == '' ) {
		parent.$.messager.alert('경고','연동방법을 선택해 주세요.','warning');
		return false;
		
	}
	
	let svn_dir_nm = $('#svn_dir_nm').textbox('getValue');
	if ( svn_dir_nm == '' ) {
		parent.$.messager.alert('경고','형상관리 시스템 디렉토리명을 입력해 주세요.','warning');
		return false;
		
	}else {
		if( byteCheck(svn_dir_nm) > 400){
			parent.$.messager.alert('경고','형상관리 시스템 디렉토리명이 400byte 초과합니다.<br> 다시 입력해 주세요.','warning');
			return false;
		}
	}
	
	if( svn_if_meth_cd != 3 ){
		if ( $('#svn_ip').textbox('getValue') == '' ) {
			parent.$.messager.alert('경고','형상관리 시스템 IP주소를 입력해 주세요.','warning');
			return false;
		}
		
		if ( $('#svn_port').textbox('getValue') == '' ) {
			parent.$.messager.alert('경고','형상관리 시스템 PORT를 입력해 주세요.','warning');
			return false;
		}
		
		if ( $('#svn_os_user_id').textbox('getValue') == '' ) {
			parent.$.messager.alert('경고','형상관리 시스템 사용자ID를 입력해 주세요.','warning');
			return false;
		}
		
		if ( $('#pre_svn_os_user_password').passwordbox('getValue') == '' ) {
			parent.$.messager.alert('경고','형상관리 시스템 사용자 패스워드를 입력해 주세요.','warning');
			return false;
		}
	}
	
	function strToDate(strDate) {
		let strDateArr = strDate.split('-');
		
		let resultDate = new Date(strDateArr[0],strDateArr[1],strDateArr[2]);
		
		return resultDate;
	}
	
	function byteCheck(str){
		let codeByte = 0;
		for (let i = 0; i < str.length; i++) {
			let oneChar = escape(str.charAt(i));
			if ( oneChar.length == 1 ) {
				codeByte ++;
			} else if (oneChar.indexOf("%u") != -1) {
				codeByte += 2;
			} else if (oneChar.indexOf("%") != -1) {
				codeByte ++;
			}
		}
		return codeByte;
	}
	
	return true;
}

function makeExecCycle(){
	let execCycle = '0 ' 
					+ $('#date_frth').combobox('getValue') + ' ' 
					+ $('#date_thrd').combobox('getValue') + ' ';
	
	let cycleOption = $('[name=exec_cycle]').attr('data-part');
	
	let cron4th;
	if ( cycleOption == '1' ){
		execCycle = execCycle + '* * ? *';
		
	}else if( cycleOption == '2' ){
		execCycle = execCycle + '? * ' + $('#date_scnd').combobox('getValue') + ' *';
		
	}else {
		execCycle = execCycle + $('#date_scnd').combobox('getValue') + ' * ? *';
	}
	$('[name=exec_cycle]').val(execCycle);
}

/* 하단 콤보박스 선택 시 인풋에 세팅 */
function setDataToInput(data){
	if(data && data != ''){
		$('#job_scheduler_nm').textbox('setValue',replaceHtmlTagToNormal(data.job_scheduler_nm) );
		$('#exec_start_dt').datebox('setValue', data.exec_start_dt);
		$('#exec_end_dt').datebox('setValue', data.exec_end_dt);
		$('#job_scheduler_desc').textbox('setValue',replaceHtmlTagToNormal(data.job_scheduler_desc) );
		$('[name=sql_std_qty_scheduler_no]').val(data.sql_std_qty_scheduler_no);
		$('#db_name_combo').combobox('setValue',data.dbid);
		$('#std_qty_target_db_user_id').textbox('setValue',data.std_qty_target_db_user_id);
		$('#date_fir').combobox('setValue',data.exec_cycle_div_cd);
		
		execCycleArr = data.exec_cycle.split(' ');
		
		if( data.exec_cycle_div_cd == '1' ){
			$('#date_thrd').combobox('setValue',execCycleArr[1].slice(0,-1));
			$('#date_frth').combobox('setValue',execCycleArr[2].slice(0,-1));
			
		}else if(data.exec_cycle_div_cd == '2'){
			$('#date_scnd').combobox('setValue',execCycleArr[1]);
			$('#date_thrd').combobox('setValue',execCycleArr[2].slice(0,-1));
			$('#date_frth').combobox('setValue',execCycleArr[3].slice(0,-1));
			
		}else {
			$('#date_scnd').combobox('setValue',execCycleArr[1].slice(0,-1));
			$('#date_thrd').combobox('setValue',execCycleArr[2].slice(0,-1));
			$('#date_frth').combobox('setValue',execCycleArr[3].slice(0,-1));
		}
		
		$('#parse_code').textbox('setValue',data.parse_code);
		$('#svn_if_meth_cd').combobox('setValue', data.svn_if_meth_cd);
		$('#svn_dir_nm').textbox('setValue',data.svn_dir_nm);
		$('#svn_ip').textbox('setValue',data.svn_ip);
		$('#svn_port').textbox('setValue',data.svn_port);
		$('#svn_os_user_id').textbox('setValue',data.svn_os_user_id);
		
		$('#pre_svn_os_user_password').passwordbox('setValue', decryptRSA(data.svn_os_user_password));
		
		$('#btnSave').attr('onClick','Btn_ModifySetting();');
		
	}else {
		$('#job_scheduler_nm').textbox('setValue','');
		$('#exec_start_dt').datebox('setValue','');
		$('#exec_end_dt').datebox('setValue','');
		$('#job_scheduler_desc').textbox('setValue','');
		$('[name=sql_std_qty_scheduler_no]').val('');
		$('#db_name_combo').combobox("clear");
		$('#std_qty_target_db_user_id').textbox('setValue','');
		
		$('#date_fir').combobox('setValue','');
		
		$('#parse_code').textbox("clear");
		
		// 형상관리 시스템 연동 설정
		$('#svn_if_meth_cd').combobox('setValue', '');
		
		$('#svn_dir_nm').textbox("clear");
		$('#svn_ip').textbox("clear");
		$('#svn_port').textbox("clear");
		$('#svn_os_user_id').textbox("clear");
		$('#pre_svn_os_user_password').passwordbox("clear");
		$('#pre_svn_os_user_password').val("");
		$('[name=svn_os_user_password]').val("");
		
		
		$('#btnSave').attr('on;');
		
		$('#btnSave').attr('onClick','Btn_SaveSetting();');
	}
}

function encryptRSA(){
	rsa = new RSAKey();
	rsa.setPublic($('#RSAModulus').val(), $('#PuKeyExpon').val());
	
	let svn_pw = $('#pre_svn_os_user_password').passwordbox('getValue');
	
	let rsaPwd = rsa.encrypt( svn_pw );
	$('[name=svn_os_user_password]').val(rsaPwd);
}

function decryptRSA(pre_pw){
	let rsaPwd = '';
	
	if(pre_pw != null && pre_pw != ''){
		rsa = new RSAKey();
		rsa.setPrivate($('#RSAModulus').val(), $('#PuKeyExpon').val(), $('#PrKeyExpon').val());
		
		rsaPwd = rsa.decrypt( pre_pw );
	}
	
	$('#pre_svn_os_user_password').passwordbox('setValue', rsaPwd);
}