$(document).ready(function() {
});

function popupOpen(){
	$('#submit_form_popup [name=top_wrkjob_cd]').val( $("#submit_form  #top_wrkjob_cd").val() );
	
	setTabs();
	createPopupTable();
	createExecutionPlanTable();
	createBindValTable();
	setChk();
	initialize();
	
	getInspectSqlList();
	
	$('#inspectionResultPop').window({
		title : "검증 SQL 목록",
		top:getWindowTop(670),
		left:getWindowLeft(1500)
	});
	
	$('#inspectionResultPop').window("open");
	$('.westButton a').hide();
	$("#popupTabs").tabs('select',0);
	
	$('.tabTxt').css('height','525px');
	$("#popup_tableList").datagrid('resize');
}

function initialize(){
	$("#popup_tableList").datagrid('loadData', []);
	$("#bindValueList").datagrid('loadData', []);
	$("#ExecutionPlanList").datagrid('loadData', []);
	
	let row = $("#tableList").datagrid('getSelected');
	
	if( row.perf_check_result_div_nm == '부적합' ){
		$('#onlyIncorrectYn').checkbox('check');
		
	}else {
		$('#onlyIncorrectYn').checkbox('uncheck');
	}
}

function setChk(){
	$('#onlyIncorrectYn').checkbox({
		value: true,
		checked: true,
		onChange: function( checked ){
			$("#search_form [name=onlyIncorrectYn]").val( checked );
			getInspectSqlList();
		}
	});
	
}

function getInspectSqlList(){
	$("#popup_tableList").datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("검증 SQL 목록"," "); 
	ajaxCall("/perfInspectMng/getInspectSqlList",
			$("#search_form"),
			"GET",
			callback_getInspectSqlList);
}

var callback_getInspectSqlList= function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	json_string_callback_common(result,'#popup_tableList',true);
}

function setTabs(){
	$('#popupTabs').tabs({
		tabWidth: '90px',
		onSelect:function(title, index){
			$('.tabTxt').css('height','525px');
			
			if( index == 0 ){
				$('.westButton a').hide();
				
			}else{
				$('.westButton a').show();
				
				if( $("#popup_tableList").datagrid('getRows').length > 0 ){
					loadSqlText();
					loadBindValue();
				}
			}
		},
	});
}

function createPopupTable(){
	$("#popup_tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		fitColumn:true,
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',width:'35%'},
			{field:'program_exec_dt',title:'수행일시',halign:"center",align:'center',width:'45%'},
			{field:'perf_check_result_div_nm',title:'검증결과',halign:"center",align:'center',width:'20%',styler:cellStyler2}
		]],
		onSelect:function(index,row){
			$('#popupTabs').tabs('select', 0);
			
			setInfoToPopupForm(row);
			loadInspectResultDetail();	// 상세 검증 결과
			loadImproveGuide();			// 개선가이드
			loadExecutionPlan();		// Execution Plan
			
			$('#sql_id_popup').html( row.sql_id );
		},
		onLoadSuccess:function(data){
			if( data.totalCount > 0){
				$(this).datagrid('selectRow', 0);
				
			}else {
				$('#popupTabs').tabs('select', 0);
				
				$('#bindValueList').datagrid('loadData',[]);
				$("#detailResultPopup tbody").html("");
				$("#td_perf_impr_guide #perf_impr_guide_table_popup tbody").html("");
				$("#ta_exec_plan").val('');
				$("#textArea").val('');
			}
		}
	})
}

function createBindValTable(){
	$("#bindValueList").datagrid({
		view: myview,
		fitColumns:true,
		rownumbers: true,
		columns:[[
			 {field:'bind_seq',title:'NO',halign:"center",align:'center',width:'20%'}
			,{field:'bind_var_nm',title:'NAME',halign:"center",align:'center',width:'50%'}
			,{field:'bind_var_value',title:'VALUE',halign:"center",align:'left',width:'30%'}
		]],
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	});
}

function createExecutionPlanTable(){
	
	$("#ExecutionPlanList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'cost_percent',title:'COST<br>(%)',halign:"center",align:'right',width:'6%',styler:cellStyler_popup},
			{field:'cpu_cost_percent',title:'CPU_COST<br>(%)',halign:"center",align:'right',width:'6%',styler:cellStyler_popup},
			{field:'io_cost_percent',title:'IO_COST<br>(%)',halign:"center",align:'right',width:'6%',styler:cellStyler_popup},
			{field:'id',title:'ID',halign:"center",align:'left',width:'3%',styler:preventClick},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',width:'45%',formatter:toNbspFromWithespace,styler:preventClick},
			{field:'cost',title:'COST',halign:"center",align:'right',width:'9%',styler:preventClick},
			{field:'cpu_cost',title:'CPU_COST',halign:"center",align:'right',width:'9%',styler:preventClick},
			{field:'io_cost',title:'IO_COST',halign:"center",align:'right',width:'9%',styler:preventClick}
		]],
		onLoadError:function() {
			errorMessager('검색 에러','데이터 조회 중에 에러가 발생하였습니다.');
		},
	});
}

function cellStyler_popup(value,row,index){
	var color = '#ffe48d';
	
	if(value >= 100){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);'
				+ 'cursor: default;';
		
	}else if(value < 100){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);'
		+ 'cursor: default;';
	}
}

function preventClick(value,row,index){
	return 'cursor: default;';
}

//튜닝요청버튼을 눌렀을때
//sql text를 세션에 저장
function Btn_RequestTuning(){
	$("#submit_form_popup [name='sql_id']").val("");
	
	var dlg = parent.$.messager.confirm({
		title: 'Confirm',
		msg: "튜닝요청하시겠습니까?",
		buttons:[{
			text: '확인',
			onClick: function(){
				let row = $("#popup_tableList").datagrid('getSelected');
				
				if(row){
					$('#submit_form_popup [name="sql_id"]').val( row.sql_id );
					
					ajaxCall("/perfInspectMng/perfChkRequestTuning",
							$("#submit_form_popup"),
							"POST",
							callback_PerfChkRequestTuningAction);
				}else {
					warningMessager('튜닝 요청 할 SQL ID가 없습니다.');
				}
				
				dlg.dialog('destroy')
			}
		},{
			text: '취소',
			onClick: function(){
				dlg.dialog('destroy')
			}
		}]
	});
}

var callback_PerfChkRequestTuningAction = function(result) {
	try{
		if(result.result != undefined){
			infoMessager(result.message);
			
		}else{
			if(result.message == 'null'){
				errorMessager('데이터 조회중 오류가 발생하였습니다.');
				
			}else{
				errorMessager(result.message);
			}
		}
	}catch(e){
		errorMessager(e.message);
	}
};

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}

function copy_to_sqlId(){
	if ( $("#sql_id_popup").text() == "") {
		return;
	}
	
	let element = document.createElement('textarea');
	
	element.value = $('#sql_id_popup').text();
	element.setAttribute('readonly', '');
	element.style.position = 'absolute';
	element.style.left = '-9999px';
	document.body.appendChild(element);
	element.select();
	
	let returnValue = document.execCommand('copy');
	document.body.removeChild(element);
	
	if (!returnValue) {
		throw new Error('copied nothing');
	}
	
	infoMessager('복사되었습니다.');
}

function copy_to_clipboard(){
	if ($("#textArea").val( ) == "") {
		return;
	}
	
	let copyText = document.getElementById("textArea");
	copyText.focus();
	copyText.select();
	
	document.execCommand("Copy");
	
	copyText.setSelectionRange(0, 0);
	copyText.scrollTop = 0;
	
	infoMessager('복사되었습니다.');
}

function setInfoToPopupForm(row){
	$('#submit_form_popup [name=perf_check_id]').val(row.perf_check_id);
	$('#submit_form_popup [name=perf_check_step_id]').val(row.perf_check_step_id);
	$('#submit_form_popup [name=program_id]').val(row.program_id);
	$('#submit_form_popup [name=program_execute_tms]').val(row.program_execute_tms);
}

function loadInspectResultDetail(){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("검증 SQL 목록"," "); 
	ajaxCall("/perfInspectMng/getInspectResultDetail",
			$("#submit_form_popup"),
			"POST",
			callback_loadInspectResultDetail);
}

var callback_loadInspectResultDetail = function(result){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	try{
		let data = JSON.parse(result);
		if( data.result != undefined && !data.result ){
			if(data.message == 'null'){
				errorMessager('데이터 조회중 오류가 발생하였습니다.');
				
			}else{
				errorMessager(data.message);
			}
		}else{
			let html = "";
			$.each(data.rows, function(index, row){
				let perf_check_meth_cd = row.perf_check_meth_cd;
				let perf_check_result_div_cd = row.perf_check_result_div_cd;
				let backColor = bgColor(row.perf_check_result_div_nm);
				
				html += "<tr>\n";
				html += "	<td><input type='text' size='15' class='font11px width100per border0px' value='"+row.perf_check_indc_nm+"' readonly></td>\n";
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.indc_pass_max_value+"' readonly></td>\n";	//적합
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exec_result_value+"'readonly></td>\n";
				html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.perf_check_result_div_nm+"'readonly></td>\n";
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exception_yn+"'readonly></td>\n";
				html += "	<td>"+nvl(row.perf_check_result_desc,'')+"</td>\n";
				html += "</tr>\n";
			});
			$("#detailResultPopup tbody").html("");
			$("#detailResultPopup tbody").html(html);
		}
		
	}catch(e){
		errorMessager(e.message);
	}
}


function loadImproveGuide(){
	ajaxCall("/perfInspectMng/selectImprovementGuide",
			$("#submit_form_popup"),
			"POST",
			callback_loadImproveGuide);
}

var callback_loadImproveGuide = function(result){
	let data;
	try{
		data = JSON.parse(result);
		let rows = data.rows;
		
		let html = "";
		$.each(data.rows, function(index, row){
			let perf_check_indc_nm = nvl(row.perf_check_indc_nm,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			perf_check_indc_nm = perf_check_indc_nm.replace(/ /g, '&nbsp;');
			
			let perf_check_indc_desc = nvl(row.perf_check_indc_desc,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			perf_check_indc_desc = perf_check_indc_desc.replace(/ /g, '&nbsp;');
			
			let perf_check_fail_guide_sbst = nvl(row.perf_check_fail_guide_sbst,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			perf_check_fail_guide_sbst = perf_check_fail_guide_sbst.replace(/ /g, '&nbsp;');
			
			html += '<tr>';
			html += '	<td>'+perf_check_indc_nm+'</td>';
			html += '	<td>'+perf_check_indc_desc+'</td>';
			html += '	<td>'+perf_check_fail_guide_sbst+'</td>';
			html += '</tr>';
		});
		$("#td_perf_impr_guide #perf_impr_guide_table_popup tbody").html("");
		$("#td_perf_impr_guide #perf_impr_guide_table_popup tbody").html(html);
	}catch(e){
		errorMessager(e.message);
	}
}

function loadExecutionPlan(){
	$('#ExecutionPlanList').datagrid('loadData',[]);
	
	ajaxCall("/perfInspectMng/getExecutionPlan",
			$("#submit_form_popup"),
			"POST",
			callback_loadExecutionPlan);
}

var callback_loadExecutionPlan = function(result){
	json_string_callback_common(result,'#ExecutionPlanList',true);
}

function loadSqlText(){
	ajaxCall("/perfInspectMng/getSqlInfo",
			$("#submit_form_popup"),
			"POST",
			callback_loadSqlText);
}

var callback_loadSqlText = function(result){
	$("#textArea").val('');
	
	try{
		let data = JSON.parse(result);
		
		if(data.result){
			$("#textArea").val(data.sqlTextInfo.sql_text);
			$("#submit_form_popup [name='elapsed_time']").val(data.sqlTextInfo.elapsed_time);
			$("#submit_form_popup [name='buffer_gets']").val(data.sqlTextInfo.buffer_gets);
			$("#submit_form_popup [name='executions']").val(data.sqlTextInfo.executions);
			$("#submit_form_popup [name='rows_processed']").val(data.sqlTextInfo.rows_processed);
			
		}else{
			errorMessager('데이터 조회중 오류가 발생하였습니다.');
		}
		
	}catch(e){
		errorMessager(e.message);
	}
}

function loadBindValue(){
	$('#bindValueList').datagrid('loadData',[]);
	
	ajaxCall("/perfInspectMng/getBindValue",
			$("#submit_form_popup"),
			"POST",
			callback_inspectResultDetail);
}

var callback_inspectResultDetail = function(result){
	json_string_callback_common(result,'#bindValueList',true);
}

function bgColor(perf_check_result_div_nm){
	if(perf_check_result_div_nm == '적합'){
		return 'style="background-color:#1A9F55;color:white;"';
	}else if(perf_check_result_div_nm == '부적합'){
		return 'style="background-color:#E41E2C;color:white;"';
	}else if(perf_check_result_div_nm == '오류'){
		return 'style="background-color:#ED8C33;color:white;"';
	}else if(perf_check_result_div_nm == '미수행'){
		return 'style="background-color:#7F7F7F;color:white;"';
	}else if(perf_check_result_div_nm == '수행중'){
		return 'background-color:#89BD4C;color:white;';
	}else if(perf_check_result_div_nm == '검증제외'){
		return 'style="background-color:#012753;color:white;"';
	}
}

function Btn_CloseInspectResultPop() {
	
	Btn_OnClosePopup('inspectionResultPop');
}