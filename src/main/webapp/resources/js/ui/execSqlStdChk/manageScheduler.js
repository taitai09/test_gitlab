var old_filter_sql;

$(document).ready(function(){
	$('body').css('visibility', 'visible');
	
	setPlaceHolder();
	
	createCombobox('#project_id');
	
	createModifyCombo();
	createDbCombo();
	createRangeCombo();
	createGatherTypeCombo();
	createList();
	createCycleCombo();
	
	createPopup();
	
	Btn_OnClick();
});

function createPopup(){
	$('#ownerEditBox').window({
		title : 'Table Owner <a style="color:lightblue">in</a> <a href="javascript:;" style="float:right; font-size:15px;" class="w20 easyui-linkbutton" onclick="closeOwnerBox();"><i class="btnIcon fas fa-close fa-lg fa-fw"></i></a>',
		closable:false
	});
	$('#moduleEditBox').window({
		title : 'Module <a style="color:lightblue">like</a> <a href="javascript:;" style="float:right; font-size:15px;" class="w20 easyui-linkbutton" onclick="closeModuleBox();"><i class="btnIcon fas fa-close fa-lg fa-fw"></i></a>',
		closable:false
	});
	
	$('.owner_list .searchbox-button').click( function() {
		$('#ownerEdit').val( $('#owner_list').val() );
		
		$('#ownerEditBox').window('move',{
			top:getWindowTop(50),
			left:getWindowLeft(1000)
		});
		$('#ownerEditBox').window('open');
	});
	$('.module_list .searchbox-button').click( function() {
		$('#moduleEdit').val( $('#module_list').val() );
		
		$('#moduleEditBox').window('move',{
			top:getWindowTop(50),
			left:getWindowLeft(150)
		});
		$('#moduleEditBox').window('open');
	});
}

function closeOwnerBox() {
	$('#owner_list').textbox( 'setValue', $('#ownerEdit').val() );
	$('#ownerEditBox').window('close');
}

function closeModuleBox() {
	$('#module_list').textbox( 'setValue', $('#moduleEdit').val() );
	$('#moduleEditBox').window('close');
}

function setPlaceHolder(){
	$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
	$('#module_list').textbox('textbox').prop('placeholder', 'JDBC, B_ERP0001, S_ERP0001');
}

function createGatherTypeCombo(){
	$('#gather_term_type_cd').combobox({
		url:'/Common/getCommonCode?grp_cd_id=1093',
		method:'get',
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue,oldValue){
			$('#gather_range_div_cd').combobox('setValue','');
			
			if( newValue != '' ){
				if( newValue == '1' ){
					$('#gather_term_1').show();
					$('#gather_term_2').hide();
					
					$('#gather_term_start_day').datebox('setValue', '');
					$('#gather_term_end_day').datebox('setValue', '');
					
				}else {
					$('#gather_term_1').hide();
					$('#gather_term_2').show();
					
					$('#gather_term_start_day').datebox('setValue', defaultStartDt );
					$('#gather_term_end_day').datebox('setValue', defaultEndDt );
				}
			}
		},
		onLoadSuccess: function() {
			$(this).combobox('setValue', '1');
		}
	});
}
function createRangeCombo(){
	$('#gather_range_div_cd').combobox({
		url:'/Common/getCommonCode?grp_cd_id=1002',
		method:'get',
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function() {
			$(this).combobox('setValue', '1');
		},
		onLoadSuccess: function(){
			$(this).combobox('textbox').attr('placeholder','???/???/???/??????/???');
		}
	});
}

function createModifyCombo(){
	$('#project_id_modify').combobox({
		url:'/sqlStandardOperationDesign/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onChange:function(newValue,oldValue){
			setDataToInput(null);
		},
		onLoadSuccess: function(){
			$(this).combobox('textbox').attr('placeholder','??????');
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB ????????? ????????? ?????????????????????.');
			return false;
		}
	});
}

function createDbCombo(){
	$('#std_qty_target_dbid').combobox({
		url:'/Common/getDatabase',
		method:'get',
		valueField:'dbid',
		textField:'db_name',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '??????' );
		},
		onLoadError: function(){
			errorMessager('DB ????????? ????????? ?????????????????????.');
			return false;
		}
	});
}

function createCycleCombo(){
	$('#date_fir').combobox({
		url:'/Common/getCommonCode?grp_cd_id=1088',
		method:'get',
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue,oldValue){
			$('[name=exec_cycle]').attr('data-part',newValue);
			
			$('#date_scnd').combobox('setValue','');
			$('#date_thrd').combobox('setValue','');
			$('#date_frth').combobox('setValue','');
			
			if(newValue == ''){
				$('#date_scnd').combobox('readonly',false);
				
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
			$(this).combobox('textbox').attr( 'placeholder' , '??????' );
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
			$(this).combobox('textbox').attr( 'placeholder' , '??????' );
		}
	});
	
	$('#date_thrd').combobox({
		valueField:'timeVal',
		textField:'timeTxt',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '??????' );
		}
	});
	
	$('#date_frth').combobox({
		valueField:'minuteVal',
		textField:'minuteTxt',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '??????' );
		}
	});
}

function loadSecondCombo(newValue){
	$('#date_scnd').combobox('setValue','');
	
	if( newValue == '1' ){
		$('#date_scnd').combobox('readonly',true);
		$('#execOption').attr('name','');
		$('#execOption').val('');
		
	}else{
		$('#date_scnd').combobox('readonly',false);
		$('#date_scnd').combobox('textbox').attr('required',true);
		
		let scndList;
		if( newValue == '2' ){
			$('#execOption').attr('name','exec_day_of_week');
			
			// ?????? ????????????
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
			
			// ?????? ????????????
			scndList = [];
			for(date = 1; date <= 31; date++){
				scndList.push( {'dateVal': date,'dateTxt': date+'???'} );
			}
		}
		$('#date_scnd').combobox('loadData',scndList);
	}
	
	// ?????? ????????????
	let timeList = [{'time': ''}];
	for(time = 0; time <= 23; time++){
		timeList.push( {'timeTxt': String(time)+'???', 'timeVal': String(time)});
	}

	$('#date_thrd').combobox('loadData',timeList);
	
	// ??? ????????????
	let minuteList = [{'minute': ''}];
	for(minute = 0; minute < 60; minute++){
		minuteList.push( {'minuteTxt': String(minute)+'???', 'minuteVal': String(minute)});
	}
	
	$('#date_frth').combobox('loadData',minuteList);
}

function createList(){
	$('#tableList').datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'job_scheduler_nm',title:'???????????????',width:'18%',halign:'center',align:'left'},
			{field:'std_qty_target_db_name',title:'????????????DB',width:'6%',halign:'center',align:'center'},
			{field:'exec_start_dt',title:'???????????? ????????????',width:'6%',halign:'center',align:'center'},
			{field:'exec_end_dt',title:'???????????? ????????????',width:'6%',halign:'center',align:'center'},
			{field:'exec_cycle',title:'????????????',width:'10%',halign:'center',align:'left'},
			{field:'parse_code',title:'Parser Code',width:'6%',halign:'center',align:'center'},
			{field:'project_nm',title:'????????????',width:'18%',halign:'center',align:'left'},
			{field:'job_scheduler_desc',title:'???????????? ??????',width:'18%',halign:'center',align:'left'},
			{field:'sql_source_type_cd',title:'SQL ??????',width:'6%',halign:'center',align:'center'},
			{field:'gather_range',title:'????????????',width:'10%',halign:'center',align:'left'},
			{field:'owner_list',title:'Table Owner',width:'6%',halign:'center',align:'left'},
			{field:'module_list',title:'Module',width:'6%',halign:'center',align:'left'},
			{field:'extra_filter_predication',title:'Filter SQL',width:'18%',halign:'center',align:'left'},
			
			{field:'project_id',hidden:true},
			{field:'std_qty_target_dbid',hidden:true},
			{field:'sql_std_qty_scheduler_no',hidden:true},
			{field:'exec_cycle_div_cd',hidden:true},
		]],
		onSelect:function(index, row) {
			$('#project_id_modify').combobox('setValue',row.project_id);
			setDataToInput(row);
			
			$('#btnDelete').attr('onClick','Btn_DeleteScheduler();');
		},
		onLoadError:function() {
			errorMessager('????????? ?????? ?????? ????????? ?????????????????????.');
		}
	});
}

/* ?????? */
function Btn_OnClick(){
	$('#tableList').datagrid('loadData', []);
	$('#project_id_modify').combobox('setValue','');
	
	$('#excel_project_id').val( $('#project_id').combobox('getValue') );
	
	ajaxCall_SchedulerList();
}

function ajaxCall_SchedulerList(){
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress('???????????? ??????', ' ');
	
	ajaxCall('/execSqlStdChk/loadSchedulerList',
			$('#search_form'),
			'GET',
			callback_SchedulerList);
}

var callback_SchedulerList = function(result){
	try {
		json_string_callback_common(result, '#tableList', true);
		
	} catch(err) {
		console.log(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function ajaxCall_SchedulerList_combobox(project_id){
	let std_qty_scheduler_div_cd = $('#submit_form [name=std_qty_scheduler_div_cd]').val();
	
	ajaxCall('/execSqlStdChk/loadSchedulerList?std_qty_scheduler_div_cd='+ std_qty_scheduler_div_cd +'&project_id=' + project_id,
			null,
			'GET',
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

/* ?????? */
function Btn_SaveSetting(){
	if( checkCondition() == true ){
		makeExecCycle();
		
		ajaxCall_CheckExistScheduler( callback_SaveCheckExistScheduler );
	}
}

function ajaxCall_CheckExistScheduler(callBack){
	ajaxCall('/execSqlStdChk/checkExistScheduler',
			$('#submit_form'),
			'GET',
			callBack);
}

var callback_SaveCheckExistScheduler = function(result){
	if(result && result.result){
		if(result.resultCount > 0){
			errorMessager('?????? ??????????????? ????????? ????????? ??????????????? ???????????????.');
			
		}else {
			ajaxCall_SaveSetting();
		}
		
	}else {
		errorMessager('????????? ?????? ?????? ????????? ?????????????????????.');
	}
}

function ajaxCall_SaveSetting(){
	ajaxCall('/execSqlStdChk/insertSqlStdQtyScheduler',
			$('#submit_form'),
			'POST',
			callback_SaveSetting);
}

var callback_SaveSetting = function(result){
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				Btn_OnClick();
				
			},500);
		});
		
	} else {
		errorMessager(result.message);
	}
}

/* ?????? */
function Btn_ModifySetting(){
	if( checkCondition() == true ){
		makeExecCycle();
		
		ajaxCall_CheckExistScheduler( callback_UpdateCheckExistScheduler );
	}
}

var callback_UpdateCheckExistScheduler = function(result){
	if(result && result.result){
		if(result.resultCount > 0){
			errorMessager('?????? ??????????????? ????????? ????????? ??????????????? ???????????????.');
			
		}else {
			ajaxCall_ModifySetting();
		}
		
	}else {
		errorMessager('????????? ?????? ?????? ????????? ?????????????????????.');
	}
}

function ajaxCall_ModifySetting(){
	ajaxCall('/execSqlStdChk/updateSqlStdQtyScheduler',
			$('#submit_form'),
			'POST',
			callback_SaveSetting);
}

/* ?????? */
function Btn_PreventDelete(){
	warningMessager('???????????? ????????? ?????????.');
}

function Btn_DeleteScheduler(){
	parent.$.messager.confirm('??????', '?????????????????????????', function(r) {
		if (r) {
			ajaxCall_DeleteScheduler();
		}
	});
}

function ajaxCall_DeleteScheduler(){
	ajaxCall('/execSqlStdChk/deleteScheduler',
			$('#submit_form'),
			'POST',
			callback_DeleteScheduler);
}

var callback_DeleteScheduler = function(result){
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				Btn_OnClick();
			},500);
		});
		
	} else {
		errorMessager(result.message);
	}
}

function Excel_Download() {
	$('#excel_submit_form').attr('action','/execSqlStdChk/loadSchedulerList/excelDownload');
	$('#excel_submit_form').submit();
	$('#excel_submit_form').attr('action','');
}

/* ????????? ?????? */
function Btn_ResetField(){
	$('#project_id_modify').combobox('setValue','');
	setDataToInput(null);
}

function checkCondition() {
	let job_scheduler_nm = $('#job_scheduler_nm').textbox('getValue');
	if ( job_scheduler_nm == '' ) {
		warningMessager('?????????????????? ????????? ?????????.');
		return false;
		
	}else {
		if( byteLength(job_scheduler_nm) > 100){
			warningMessager('?????????????????? 100byte ???????????????.<br> ?????? ????????? ?????????.');
			return false;
		}
	}
	
	let startDate = $('#exec_start_dt').datebox('getText');
	if ( startDate == '' ) {
		warningMessager('???????????? ??????????????? ????????? ?????????.');
		return false;
		
	}else {
		startDate = strToDate(startDate);
	}
	
	let endDate = $('#exec_end_dt').datebox('getText');
	if ( endDate == '' ) {
		warningMessager('???????????? ??????????????? ????????? ?????????.');
		return false;
		
	}else {
		endDate = strToDate(endDate);
	}
	
	if ( startDate > endDate ){
		warningMessager('???????????? ??????????????? ???????????? ???????????????<br>????????? ?????????.');
		return false;
	}
	
	if ( $('#date_fir').combobox('getValue') == '' ||
			( $('[name=exec_cycle]').attr('data-part') != '1' && $('#date_scnd').combobox('getValue') == '' ) ||
			$('#date_thrd').combobox('getValue') == '' ||
			$('#date_frth').combobox('getValue') == '') {
		
		warningMessager('??????????????? ????????? ?????????.');
		return false;
	}
	
	if ( $('#std_qty_target_dbid').combobox('getValue') == '' ) {
		warningMessager('????????????DB??? ????????? ?????????.');
		return false;
	}
	
	if ( $('#parse_code').textbox('getValue') == '' ) {
		warningMessager('Parser Code??? ????????? ?????????.');
		return false;
	}
	
	let job_scheduler_desc = $('#job_scheduler_desc').textbox('getValue');
	if( byteLength(job_scheduler_desc) > 400 ){
		warningMessager('???????????? ????????? 400byte ???????????????.<br> ?????? ????????? ?????????.');
		return false;
	}
	
	if ( $('#gather_term_type_cd').combobox('getValue') == '' ) {
		warningMessager('???????????? ????????? ????????? ?????????.');
		return false;
	}
	
	if( $('#gather_term_type_cd').combobox('getValue') == 1 ){
		if ( $('#gather_range_div_cd').combobox('getValue') == '' ) {
			warningMessager('???????????? ????????? ????????? ?????????.');
			return false;
		}
		
	}else {
		let gatherStartDate = $('#gather_term_start_day').datebox('getText');
		if ( gatherStartDate == '' ) {
			warningMessager('???????????? ??????????????? ????????? ?????????.');
			return false;
			
		}else {
			$('[name=gather_term_start_day]').val( gatherStartDate.replace(/-/gi, '') );
			gatherStartDate = strToDate(gatherStartDate);
		}
		
		let gatherEndDate = $('#gather_term_end_day').datebox('getText');
		if ( gatherEndDate == '' ) {
			warningMessager('???????????? ??????????????? ????????? ?????????.');
			return false;
			
		}else {
			$('[name=gather_term_end_day]').val( gatherEndDate.replace(/-/gi, '') );
			gatherEndDate = strToDate(gatherEndDate);
		}
		
		if ( gatherStartDate > gatherEndDate ){
			warningMessager('??????????????? ????????? ?????????.');
			return false;
		}
	}
	
	let owner_list = $('#owner_list').searchbox('getValue');
	if( owner_list != ''){
		owner_list = owner_list.trim().split('\,');
		
		if ( owner_list.length > 100) {
			warningMessager('Table Owner ?????? 100??? ?????? ?????? ???????????????.');
			return false;
			
		} else {
			if ( owner_list.length > 1 ) {
				for ( let i = 0; i < owner_list.length; i++) {
					if ( owner_list[i].trim() == '' ) {
						warningMessager('Table Owner ?????? ?????? ??? ?????? ?????? ????????? ????????? ????????? ?????????.');
						return false;
					}
				}
			}
		}
	}
	
	let module_list = $('#module_list').searchbox('getValue');
	if( module_list != ''){
		module_list = module_list.trim().split('\,');
		
		if ( module_list.length > 100) {
			warningMessager('Module ?????? 100??? ?????? ?????? ???????????????.');
			return false;
			
		} else {
			if ( module_list.length > 1 ) {
				for ( let i = 0; i < module_list.length; i++) {
					if ( module_list[i].trim() == '' ) {
						warningMessager('Module ?????? ?????? ??? ?????? ?????? ????????? ????????? ????????? ?????????.');
						return false;
					}
				}
			}
		}
	}
	
	let textArea = $('#extra_filter_predication').textbox('getValue');
	if( textArea != '' ){
		let byte = byteLength(textArea);
		if(byte >= 4000){
			warningMessager('Filter SQL????????? ?????? ????????????.<br>?????? : 4000byte, ?????? : '+byte+' byte');
			return false;
		}
	}
	
	function strToDate(strDate) {
		let strDateArr = strDate.split('-');
		
		let resultDate = new Date(strDateArr[0],strDateArr[1],strDateArr[2]);
		
		return resultDate;
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

/* ?????? ???????????? ?????? ??? ????????? ?????? */
function setDataToInput(data){
	if(data && data != ''){
		$('#job_scheduler_nm').textbox('setValue',replaceHtmlTagToNormal(data.job_scheduler_nm) );
		$('#exec_start_dt').datebox('setValue', data.exec_start_dt);
		$('#exec_end_dt').datebox('setValue', data.exec_end_dt);
		$('#std_qty_target_dbid').combobox('setValue',data.std_qty_target_dbid);
		$('#parse_code').textbox('setValue',data.parse_code);
		$('#job_scheduler_desc').textbox('setValue',replaceHtmlTagToNormal(data.job_scheduler_desc) );
		$('[name=sql_std_qty_scheduler_no]').val(data.sql_std_qty_scheduler_no);
		
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
		if( data.sql_source_type_cd == 'AWR' ){
			$('#btn-AWR').radiobutton('check');
			
		}else {
			$('#btn-all').radiobutton('check');
		}
		
		$('#gather_term_type_cd').combobox('setValue', data.gather_term_type_cd);
		$('#gather_range_div_cd').combobox('setValue', data.gather_range_div_cd);
		$('#gather_term_start_day').datebox('setValue', data.gather_term_start_day);
		$('#gather_term_end_day').datebox('setValue', data.gather_term_end_day);
		
		$('#owner_list').searchbox('setValue', data.owner_list);
		$('#module_list').searchbox('setValue', data.module_list);
		$('#extra_filter_predication').textbox('setValue', data.extra_filter_predication);
		
		$('#btnSave').attr('onClick','Btn_ModifySetting();');
		
	}else {
		$('#job_scheduler_nm').textbox('setValue','');
		$('#exec_start_dt').datebox('setValue','');
		$('#exec_end_dt').datebox('setValue','');
		$('#std_qty_target_dbid').combobox('clear');
		$('#parse_code').textbox('clear');
		$('#job_scheduler_desc').textbox('setValue','');
		$('[name=sql_std_qty_scheduler_no]').val('');
		
		$('#date_fir').combobox('setValue','');
		
		$('#btn-AWR').radiobutton('check');
		
		$('#gather_term_type_cd').combobox('setValue','1');
		$('#gather_range_div_cd').combobox('clear');
		$('#gather_term_start_day').datebox('clear');
		$('#gather_term_end_day').datebox('clear');
		
		$('#owner_list').searchbox('clear');
		$('#module_list').searchbox('clear');
		$('#extra_filter_predication').textbox('clear');
		
		$('#btnSave').attr('onClick','Btn_SaveSetting();');
		$('#btnDelete').attr('onClick','Btn_PreventDelete();');
	}
}

function showFilterSQL(){
	$('#condition_1').combobox('clear');
	$('#condition_2').combobox('clear');
	$('#filter_sql').textbox('setValue', $('#extra_filter_predication').textbox('getValue'));
	
	old_filter_sql = filter_sql;
	$('#filterSqlPopup').window('open');
}