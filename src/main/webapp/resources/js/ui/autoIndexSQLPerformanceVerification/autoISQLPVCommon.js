const emptyHtml = '<li><b> EXECUTION_PLAN </b></li>'
				+ '<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>'
				+ '<li> 해당 정보가 없습니다. </li>'
				+ '<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>';

function cellStyler( value,row,index ) {
	if ( row.perf_check_result_yn == '적합' ) {
		return 'background-color:#1A9F55;color:white;';
	} else if ( row.perf_check_result_yn == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	} else {
		return '';
	}
}

function setValueNull( val, row ) {
	return '';
}

/* Popup */
function createBindList(){
	$('#bindValueList').datagrid({
		view: myview,
		columns:[[
			{field:'no',title:'NO',halign:'center',width:'8.3%',align:'right'},
			{field:'bind_var_nm',title:'NAME',halign:'center',width:'20%',align:'left'},
			{field:'bind_var_value',title:'VALUE',halign:'center',width:'20%',align:'left'},
			{field:'elapsed_time',title:'ELAPSED<br>TIME',halign:'center',width:'18.7%',formatter:getNumberFormatNullChk,align:'right'},
			{field:'buffer_gets',title:'BUFFER<br>GETS',halign:'center',width:'16.5%',formatter:getNumberFormatNullChk,align:'right'},
			{field:'rows_processed',title:'ROWS<br>PROCESSED',halign:'center',width:'16.5%',align:'right'},
		]],
		onLoadSuccess:function(data) {
			mergeCells(data);
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
		}
	});
}

function mergeCells( data ){
	let fieldArr = ['no', 'elapsed_time', 'buffer_gets', 'rows_processed'];
	
	try{
		let cnt = 1;
		let idx = 0;
		let dataLength = data.rows.length;
		
		for( let listIdx = 1; listIdx <= dataLength; listIdx++ ) {
			let test = listIdx != dataLength;
			
			if ( listIdx != dataLength && data.rows[idx].no == data.rows[listIdx].no ) {
				cnt ++;
				
			} else {
				mergeLoop( idx, cnt );
				
				if ( listIdx != dataLength ){
					cnt = 1;
					idx = listIdx;
				}
			}
		}
	}catch(err){
		console.log("Error Occured while merging", err.message);
	}
	
	function mergeLoop( idx, cnt ){
		for( let fieldIdx = 0; fieldIdx < fieldArr.length; fieldIdx++ ){
			$('#bindValueList').datagrid('mergeCells',{
				index: idx,
				field: fieldArr[fieldIdx],
				rowspan: cnt
			});
		}
	}
}

function createPopUp(){
	parent.frameName = $('#menu_id').val();
	$('#planCompare').css('visibility', 'visible');
	$('#btnSqlReview').hide();
	
	createPlanTree();
	
	$('#loadExplainPlanPop').window({
		top:0,
		left:50
	});
	
	$('.sqlMemo').css('visibility','visible');
	
	$('#tabs').tabs('update',{
		tab: $('#tabs').tabs('getTab',1),
		options:{
			title:'ASIS Plan'
		}
	});
	$('#tabs').tabs('update',{
		tab: $('#tabs').tabs('getTab',2),
		options:{
			title:'TOBE Plan'
		}
	});
	
	$('.tabs-last:not(.tabs-first)').hide();
	
	$('#operPlanCopy').linkbutton({
		text:'TOBE PLAN 복사'
	});
	
	$('#planCompare').on('click', function(){
		ajaxCall_planCompare();
	});
}

function showSqlTextPopup( row ){
	popupReset();
	popupSetting( row );
	loadPopupData( row );
	
	$('#loadExplainPlanPop').window('open');
}

function popupReset(){
	$('#textArea').val('');
	$('#bindValueList').datagrid('loadData',[]);
	$('#asisTextPlan').tree('loadData',[]);
	$('#asisTextPlan_h').val('');
	$('#operTextPlan').tree('loadData',[]);
	$('#operTextPlan_h').val('');
	$('#loadExplainPlanPop #tabs').tabs('select', 0);
}

function popupSetting( row ){
	try{
		$('#loadExplainPlan_form #bindValueList').datagrid('resize',{
			width: 400
		});
		
		let sqlId = row.sql_id;
		$('#loadExplainPlanPop').window('setTitle', 'SQL Info( ' + sqlId+ ' )');
		
		$('#loadExplainPlan_form [name=project_id]').val( row.child_project_id );
		$('#loadExplainPlan_form [name=sql_auto_perf_check_id]').val( row.child_sql_auto_perf_check_id );
		$('#loadExplainPlan_form [name=sql_id]').val( sqlId );
		$('#loadExplainPlan_form [name=dbid]').val( row.original_dbid );
		$('#loadExplainPlan_form [name=plan_hash_value], #loadExplainPlan_form [name=asis_plan_hash_value]').val( row.asis_plan_hash_value );
		$('#loadExplainPlan_form [name=tobe_plan_hash_value]').val( row.tobe_plan_hash_value );
		$('#loadExplainPlan_form [name=tobe_executions]').val( (row.dml_exec_yn == 'Y') ? 'Y' : '' );
		
	}catch(err){
		console.log("Error Occured while data set", err.message);
		
	}finally {
		$('#textArea').val('');
		
		let emptyText = removeHtmlTag( emptyHtml );
		$("#asisTextPlan_h").html( emptyText );
		$("#operTextPlan_h").html( emptyText );
	}
}

function loadPopupData( row ){
	try{
		$('#bindValueList').datagrid('loadData',[]);
		$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
		$('#bindValueList').datagrid('loading');
		
		ajaxCall_loadSqlFullText();
		ajaxCall_loadBindValue();
		
		let url_planTree = '/Sqls/sqlTreePlanListAll';
		let url_planText = '/Sqls/sqlTextPlanListAll';
		
		ajaxCall_loadPlanTree(url_planTree, '#asisTextPlan');
		ajaxCall_loadPlanText(url_planText, '#asisTextPlan_h');
		
		
		let sql_command_type_cd = row.sql_command_type_cd;
		let dml_exec_yn = row.dml_exec_yn;
		let tobe_plan_hash_value = row.tobe_plan_hash_value;
		
		url_planTree = '/AISQLPV/loadAfterPlanTree';
		url_planText = '/AISQLPV/loadAfterSelectTextPlanListAll';
		
		if( (dml_exec_yn == 'N' && sql_command_type_cd != 'SELECT') ||
				(isEmpty(tobe_plan_hash_value) && sql_command_type_cd == 'INSERT') ){
			
			url_planTree = '/AISQLPV/loadNoExecAfterPlanTree';
			url_planText = '/AutoPerformanceCompareBetweenDatabase/loadAfterDMLTextPlanListAll';
		}
		
		ajaxCall_loadPlanTree(url_planTree, '#operTextPlan');
		ajaxCall_loadPlanText(url_planText, '#operTextPlan_h');
		
	}catch(err){
		console.log('Error Occured while data load', err.message);
	}
}

function createPlanTree(){
	$('#asisTextPlan, #operTextPlan').tree({
		animate:true,
		lines:true,
		onLoadSuccess:function(){
			$('.tree-node' ).css( 'cursor', 'default' );
		}
	});
}

function ajaxCall_loadBindValue(){
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadExplainInfoBindValue"
			, $('#loadExplainPlan_form')
			, "POST"
			, callback_loadBindValue);
}
var callback_loadBindValue = function(result){
	try{
		json_string_callback_common(result,'#bindValueList',true);
		
	} catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadSqlFullText(){
	ajaxCall('/SQLAutomaticPerformanceCheck/loadExplainSqlText'
			, $('#loadExplainPlan_form')
			, "POST"
			, callback_loadSqlFullText);
}
var callback_loadSqlFullText = function(result){
	let sqlText = '';
	
	try{
		if( result.result ){
			if( isNotEmpty(result.object) ){
				sqlText = result.object.sql_text;
			}
			$('#textArea').val(sqlText);
			
		}else{
			$('#textArea').val(result.message);
		}
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadPlanTree(url, treeId){
	$(treeId).tree('loadData',[]);
	
	ajaxCallWithCallbackParam(url
							, $('#loadExplainPlan_form')
							, "POST"
							, callback_loadPlanTree
							, treeId);
}
var callback_loadPlanTree = function(result, treeId){
	try {
		if( isNotEmpty(result) ){
			let data = JSON.parse(result);
			
			if( isNotEmpty(data) ){
				$(treeId).tree('loadData', data);
				
			}else{
				$(treeId).html( emptyHtml )
			}
		}
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadPlanText(url, elementId){
	$(elementId).html('');
	
	ajaxCallWithCallbackParam(url
							, $('#loadExplainPlan_form')
							, 'POST'
							, callback_loadPlanText
							, elementId);
}
var callback_loadPlanText = function(result, elementId){
	try {
		let innerText = '';
		if( result.result ){
			if( isNotEmpty(result.object) ){
				for(let i = 0 ; i < result.object.length ; i++){
					let post = result.object[i];
					innerText += strReplace(post.execution_plan,' ','&nbsp;')+'\n';
				}
			}else {
				innerText = '해당 정보가 없습니다.\n';
			}
			
		}else {
			if ( isEmpty(result.message) ) {
				innerText = '데이터 조회 중 에러가 발생하였습니다.\n';
				
			} else {
				innerText = result.message + '\n';
				console.log('Error Occured', result.message);
			}
		}
		
		let strHtml = 'EXECUTION_PLAN---------------------------------------------------------------------------------------------\n';
		strHtml += innerText;
		strHtml += '-----------------------------------------------------------------------------------------------------------';
		$(elementId).html(strHtml);
		
	}catch(err) {
		console.log('Error Occured', err.message);
		
	}finally{
		strHtml = null;
		innerText = null;
	}
}

function Btn_CloseLoadExplainPlanPop() {
	popupReset();
	Btn_OnClosePopup('loadExplainPlanPop');
}

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}
function asisTextPlanCopy() {
	let copyText = $('#asisTextPlan_h').text();
		
	if ( isEmpty(copyText) ) {
		return;
	}
	let element = document.createElement('textarea');
	
	element.value = copyText;
	element.setAttribute('readonly', '');
	element.style.position = 'absolute';
	element.style.left = '-9999px';
	document.body.appendChild(element);
	element.select();
	
	let returnValue = document.execCommand('copy');
	document.body.removeChild(element);
	
	if ( isEmpty(returnValue) ) {
		throw new Error('copied nothing');
	}
	
	infoMessager('복사되었습니다.');
}

function operTextPlanCopy() {
	let copyText = $('#operTextPlan_h').text();
	
	if ( isEmpty(copyText) ) {
		return;
	}
	
	let element = document.createElement('textarea');
	
	element.value = copyText;
	element.setAttribute('readonly', '');
	element.style.position = 'absolute';
	element.style.left = '-9999px';
	document.body.appendChild(element);
	element.select();
	
	let returnValue = document.execCommand('copy');
	document.body.removeChild(element);
	
	infoMessager('복사되었습니다.');
}
function removeHtmlTag( strHtml ){
	strHtml = strHtml.replace(/\<\/b\>/g,"");
	strHtml = strHtml.replace(/\<\/li\>/g,"\n");
	strHtml = strHtml.replace(/(\<b\>|\<li\>)/g,"");
	
	return strHtml;
}

function copy_to_clipboard() {
	let copyText = document.getElementById("textArea");
	
	if ( isEmpty(copyText.value) ) {
		return;
	}
	
	copyText.focus();
	copyText.select();
	document.execCommand("Copy");
	copyText.setSelectionRange(0, 0);
	copyText.scrollTop = 0;
	
	infoMessager('복사되었습니다.');
}

function copy_to_sqlId() {
	let sqlId = $('#loadExplainPlan_form [name=sql_id]').val();
	
	if ( isEmpty(sqlId) ) {
		return;
	}
	
	let element = document.createElement('textarea');
	
	element.value = sqlId;
	element.setAttribute('readonly', '');
	element.style.position = 'absolute';
	element.style.left = '-9999px';
	document.body.appendChild(element);
	element.select();
	
	let returnValue = document.execCommand('copy');
	document.body.removeChild(element);
	
	infoMessager('복사되었습니다.');
}

function ajaxCall_planCompare(){
	ajaxCall("/Common/getPlanCompareResult"
			,$('#loadExplainPlan_form')
			,"GET"
			,callback_planCompare);
}
var callback_planCompare = function(result){
	drawDiffTable(result);
}