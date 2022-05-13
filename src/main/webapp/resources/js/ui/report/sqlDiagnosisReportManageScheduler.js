var searchFlag = false;
var rsa;
var old_filter_sql;
$(document).ready(function(){
	$("body").css("visibility", "visible");
	
	$('[name=job_scheduler_type_cd]').val('37');
	
	createDbCombo();
	createList();
	
	createCycleCombo();
	
	creategatherTermTypeCd();
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled").click(function(){
		fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
});

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#detail_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#detail_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);

	var data;
	var dataLength=0;
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	}catch(e){
		parent.$.messager.alert('',e.message);
	}

	//페이지를 보여줄지말지 여부를 결정
	if(currentPage > 1){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if(dataLength > 10){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else{
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	if(currentPage == 1){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		if(dataLength > pagePerCount){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}	
};

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	let currentPage = $("#detail_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#detail_form #currentPage").val("1");
		return;
	}
	Btn_OnClick(true);
}

function fnSetCurrentPage(direction){
	let currentPage = $("#detail_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#detail_form #currentPage").val(currentPage);
	}else{
		$("#detail_form #currentPage").val("1");
	}
}

function showFilterSQL(){
	$("#condition_1").combobox("setValue","");
	$("#condition_2").combobox("setValue","");
	
	old_filter_sql = filter_sql;
	$('#filterSqlPopup').window("open");
}

function creategatherTermTypeCd(){
	
	$('#gather_term_type_cd').combobox({
		valueField:'gatherTermTypeCd',
		textField:'gatherTermTypeNm',
		onChange:function(value){
			$("#gather_term_start_day").textbox('setValue','')
			$("#gather_term_end_day").textbox('setValue','')
			setGathertermEle(value)
		},
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		},
	});
	
	let data =[
		  {'gatherTermTypeCd':'1', 'gatherTermTypeNm':'주기'}
		, {'gatherTermTypeCd':'2', 'gatherTermTypeNm':'기간'}
	];
	
	$('#gather_term_type_cd').combobox('loadData', data);

}

function setGathertermEle(value){
	if( value === '1' ){

		$("#gather_term_type2").css('display','none');
		$("#gather_term_start_day").datebox('setValue','');
		$("#gather_term_end_day").datebox('setValue','');
		$("#gather_term_type1").css('display','inline-block');
		$("#gather_term_type_cd_week").radiobutton()
		$("#gather_term_type_cd_month").radiobutton()
		
	}else if( value === '2' ){
		
		$("#gather_term_type1").css('display','none');
		$("#gather_term_type_cd_week").radiobutton('uncheck');
		$("#gather_term_type_cd_month").radiobutton('uncheck');
		$("#gather_term_type2").css('display','inline-block');
		
		$("#gather_term_start_day").datebox()
		$("#gather_term_end_day").datebox()
		$("#gather_term_start_day").datebox('setValue',$('#lastMonth').val())
		$("#gather_term_end_day").datebox('setValue',$('#nowDate').val())
	
	}
}

function createDbCombo(){
	$('#db_name_combo').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
        panelHeight:'auto',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
			
			$('#std_qty_target_dbid').combobox({
				data:$("#db_name_combo").combobox('getData'),
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
			
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	
}

function loadDbUserCombo(dbid){
	$("#std_qty_target_db_user_id").combobox({
		url:"/Common/getUserName?dbid="+dbid,
		method:"get",
		valueField:'username',
		textField:'username',
		onLoadSuccess: function(event) {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		},
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

function createList(){
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'db_name',title:'표준진단DB',width:'8%',halign:'center',align:'center'},
			{field:'job_scheduler_nm',title:'스케줄러명',width:'16%',halign:'center',align:'left'},
			{field:'exec_start_dt',title:'스케줄러 시작일자',width:'8%',halign:'center',align:'center'},
			{field:'exec_end_dt',title:'스케줄러 종료일자',width:'8%',halign:'center',align:'center'},
			{field:'exec_cycle',title:'실행주기',width:'8%',halign:'center',align:'center'},
			{field:'job_scheduler_desc',title:'스케줄러 설명',width:'20%',halign:'center',align:'left'},
			{field:'sql_source_type_nm',title:'SQL 소스',width:'6%',halign:'center',align:'center'},
			{field:'gather_term',title:'수집기간',width:'12%',halign:'center',align:'center'},
			
			{field:'owner_list',title:'TABLE_OWNER',width:'8%',halign:'center',align:'center'},
			{field:'module_list',title:'MODULE',width:'8%',halign:'center',align:'center'},
			{field:'extra_filter_predication',title:'FILTER SQL',width:'8%',halign:'center',align:'center'},
			{field:'project_id',hidden:true},
			{field:'dbid',hidden:true},
			{field:'sql_std_qty_scheduler_no',hidden:true},
			{field:'exec_cycle_div_cd',hidden:true},
		]],
		onSelect:function(index, row) {
			$('#project_id_modify').combobox('setValue',row.project_id);
			$('#btnDelete').attr('onClick','Btn_DeleteScheduler();');
			setDetailData(row);
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}
function clearDetailData(){
	$("#submit_form [name=sql_std_qty_scheduler_no]").val('');
	
	$("#submit_form #job_scheduler_nm").textbox('setValue','');
	$("#submit_form #exec_start_dt").datebox('setValue','');
	$("#submit_form #exec_end_dt").datebox('setValue','');
	$("#submit_form #job_scheduler_desc").textbox('setValue','');
	$("#submit_form #std_qty_target_dbid").combobox('setValue','');
	
	$('#date_fir').combobox('setValue','');
	$("#submit_form #sqlSourceTop").radiobutton('uncheck');
	$("#submit_form #sqlSourceAll").radiobutton('uncheck');
	$("#submit_form #gather_term_type_cd").combobox('setValue','');
	$("#submit_form #owner_list").textbox('setValue','');
	
	$("#submit_form #module_list").textbox('setValue','');
	$("#submit_form #extra_filter_predication").textbox('setValue','');
	$("#filter_sql").textbox('setValue','');
	
	$('#btnSave').attr('onClick','Btn_SaveSetting();');

}
function setDetailData(data){
	$("#submit_form [name=sql_std_qty_scheduler_no]").val(data.sql_std_qty_scheduler_no);

	$("#submit_form #job_scheduler_nm").textbox('setValue',data.job_scheduler_nm);
	$("#submit_form #exec_start_dt").datebox('setValue',data.exec_start_dt);
	$("#submit_form #exec_end_dt").datebox('setValue',data.exec_end_dt);
	$("#submit_form #job_scheduler_desc").textbox('setValue',data.job_scheduler_desc);
	$("#submit_form #std_qty_target_dbid").combobox('setValue',data.std_qty_target_dbid);
	$('#date_fir').combobox('setValue',data.exec_cycle_div_cd);

	if(data.sql_source_type_cd === '1'){
		$("#submit_form #sqlSourceTop").radiobutton('check');
	}else if(data.sql_source_type_cd === '2'){
		$("#submit_form #sqlSourceAll").radiobutton('check');
	}
	
	$("#submit_form #gather_term_type_cd").combobox('setValue',data.gather_term_type_cd);
	if(data.gather_term_type_cd === '1'){
		
		if(data.gather_range_div_cd === '2'){
			$("#submit_form #gather_term_type_cd_week").radiobutton('check');
		}else if(data.gather_range_div_cd === '3'){
			$("#submit_form #gather_term_type_cd_month").radiobutton('check');
		}
		
	}else if( data.gather_term_type_cd === '2'){
		
		$("#submit_form #gather_term_start_day").textbox('setValue',[data.gather_term_start_day.slice(0,4),'-',data.gather_term_start_day.slice(4,6),'-',data.gather_term_start_day.slice(6,8)].join(''))
		$("#submit_form #gather_term_end_day").textbox('setValue',[data.gather_term_end_day.slice(0,4),'-',data.gather_term_end_day.slice(4,6),'-',data.gather_term_end_day.slice(6,8)].join(''))
	}
	
	$("#submit_form #owner_list").textbox('setValue',data.owner_list);
	$("#submit_form #module_list").textbox('setValue',data.module_list);
	$("#submit_form #extra_filter_predication").textbox('setValue',data.extra_filter_predication);
	$("#filter_sql").textbox('setValue',data.extra_filter_predication);

	
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
	
	$('#btnSave').attr('onClick','Btn_ModifySetting();');

}

function filerByProjectId(newValue, rows) {
	return rows.filter(function(row){
				return row.project_id == newValue;
			});
}
/* 검색 */
function Btn_OnClick(b_paging,b_sel_dbId){
	
	if(b_sel_dbId){
		$("#db_name_combo").combobox('setValue',$("#std_qty_target_dbid").combobox('getValue'));
		
	}else if($("#db_name_combo").combobox('getValue') === ''){
		parent.$.messager.alert('경고','품질진단DB를 선택해 주세요.','warning');
		return ; 
	}
	
	if(b_paging){ //페이징으로 검색을 하지않는는경우
		$("#detail_form #currentPage").val('1');
		$("#detail_form #pagePerCount").val(10);
	}

	$('#tableList').datagrid("loadData", []);

	$('#excel_db_id').val( $("#db_name_combo").combobox('getValue') );
	
	ajaxCall_SchedulerList('');
	clearDetailData()
}

function ajaxCall_SchedulerList(){
	let url;
	let form;
	let callback = callback_SchedulerList;
	
	url = "/SQLDiagnosisReportManageScheduler/selectSQLDiagnosisReportScheduler";
	
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress("스케쥴러 조회", " ");
	
	ajaxCall(url,
			$('#detail_form'),
			"POST",
			callback_SchedulerList);
}

var callback_SchedulerList = function(result){
	try {
		json_string_callback_common(result, '#tableList', true);
		fnControlPaging(result);
		searchFlag = true;
		
	} catch(err) {
		console.log(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

/* 저장 */
function Btn_SaveSetting(){
	if( checkCondition() == true ){
		makeExecCycle();
//		if ( $('#svn_if_meth_cd').combobox('getValue') != '3' ) {
//			encryptRSA();
//		}
//		$('#std_qty_target_db_name').val($('#db_name_combo').combobox('getText'));
		
		ajaxCall_SaveSetting();
	}
}

function ajaxCall_SaveSetting(){
	ajaxCall('/SQLDiagnosisReportManageScheduler/insertSQLDiagnosisReportScheduler',
			$('#submit_form'),
			"POST",
			callback_SaveSetting);
}

var callback_SaveSetting = function(result){
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				 Btn_OnClick(false,true);
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
		
		ajaxCall_ModifySetting();
	}
}

function ajaxCall_ModifySetting(){
	ajaxCall('/SQLDiagnosisReportManageScheduler/updateSQLDiagnosisReportScheduler',
			$('#submit_form'),
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
	ajaxCall('/SQLDiagnosisReportManageScheduler/deleteSQLDiagnosisReportScheduler',
			$('#submit_form'),
			"POST",
			callback_DeleteScheduler);
}

var callback_DeleteScheduler = function(result){
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				 Btn_OnClick(false);
				 $('#project_id_modify').combobox('setValue','');
				 
				},500);
		});
		
	} else {
		parent.$.messager.alert('정보',result.message,'info');
	}
}

function Excel_Download() {
	if ( $('#excel_db_id').val() == '' ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
	}
	
	$("#excel_submit_form").attr("action","/SQLDiagnosisReportManageScheduler/excelDownload");
	$("#excel_submit_form").submit();
	$("#excel_submit_form").attr("action","");
}

/* 초기화 버튼 */
function Btn_ResetField(){
	clearDetailData();
}

function checkCondition() {

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

	let job_scheduler_desc = $('#job_scheduler_desc').textbox('getValue');
	if( byteCheck(job_scheduler_desc) > 400){
		parent.$.messager.alert('경고','스케줄러 설명이 400byte 초과합니다.<br> 다시 입력해 주세요.','warning');
		return false;
	}
	
	if ( $('#std_qty_target_dbid').combobox('getValue') == '' ) {
		parent.$.messager.alert('경고','품질 진단 DB를 입력해 주세요.','warning');
		return false;
	}
	
	if( $('#sqlSourceTop').radiobutton('options').checked === false && $('#sqlSourceAll').radiobutton('options').checked === false ){
		parent.$.messager.alert('경고','SQL 소스을 입력해 주세요.','warning');
		return false;
	}
	
	let gather_term_type_cd = $("#gather_term_type_cd").combobox('getValue')
	if(gather_term_type_cd){
		
		if(gather_term_type_cd === '1'){
			
			if( !$('#gather_term_type_cd_week').radiobutton('options').checked && !$('#gather_term_type_cd_month').radiobutton('options').checked ){
				
				parent.$.messager.alert('경고','수집 주기를 선택해 주세요.','warning');
				return false;
				
			}
		
		}else if (gather_term_type_cd === '2'){
			
			let gather_term_start = $('#gather_term_start_day').datebox('getText');
			let gather_term_end = $('#gather_term_end_day').datebox('getText');
			let termStartDate = strToDate(gather_term_start);
			let termEndDate = strToDate(gather_term_end);

			if( !$('#gather_term_start_day').textbox('getValue') ){
				
				parent.$.messager.alert('경고','수집 기간 시작일을 선택해 주세요.','warning');
				return false;
				
			}else if( !$('#gather_term_end_day').textbox('getValue') ){
				
				parent.$.messager.alert('경고','수집 기간 종료일을 선택해 주세요.','warning');
				return false;
				
			}
			else if ( termStartDate > termEndDate ){
				parent.$.messager.alert('경고','시작일자와 종료일자를 확인해 주세요.','warning');
				return false;
			}
			
		}
		
	}else{
		
		parent.$.messager.alert('경고','수집 기간을 선택해 주세요.','warning');
		return false;
		
	}
	
//	if ( $('#extra_filter_predication').textbox('getValue') == '' ) {
//		parent.$.messager.alert('경고','Filter SQL 를 입력해 주세요.','warning');
//		return false;
//	}
	
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

