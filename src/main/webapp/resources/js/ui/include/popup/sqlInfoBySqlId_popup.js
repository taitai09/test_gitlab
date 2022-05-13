const emptyData = [];
const emptyPlanTxt  = '<li><b> EXECUTION_PLAN </b></li>'
					+ '<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>'
					+ '<li> 해당 정보가 없습니다. </li>'
					+ '<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>';
var chartExecutions;
var chartElapsedTime;
var chartBufferGets;
var chartDiskReads;

$(document).ready(function() {
	createSqlIdList();
	createBindList();
	createAsisTree();
	
	createExecutionsChart( emptyData );
	createElapsedTimeChart( emptyData );
	createBufferGetsChart( emptyData );
	createDiskReadsChart( emptyData );
	
	var clipboard = new Clipboard('#asisPlanCopy');
	clipboard.on('success', function(e) {
		infoMessager('복사되었습니다.');
	});
});

function openSqlInfoPopup(projectId, sqlPackId, row){
	popupReset();
	setPopupData(projectId, sqlPackId, row);
	
	$('#sqlInfoBySqlId_popup').window('open');
	$('#sqlInfoBySqlIdTab').tabs('select', 0);
}

function createSqlIdList(){
	$('#sqlIdList').datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',width:'12%',halign:'center',align:'center'},
			{field:'sql_id',title:'SQL_ID',width:'44%',halign:'center',align:'center'},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',width:'46%',halign:'center',align:'center'}
		]],
		onSelect: function(index, row){
			$('#sql_id_popup').val(row.sql_id);
			$('#plan_hash_value_popup').val(row.plan_hash_value);
			
			resetTabs();
			
			ajaxCall_loadSqlText();
			ajaxCall_loadBindValue();
			ajaxCall_loadAsisPlanTree();
			ajaxCall_loadAsisPlanText();
			ajaxCall_loadChartData();
		},
		onLoadSuccess: function(data){
			let row = data.rows;
			if( row.length > 0 && isNotEmpty(row[0].rnum) ){
				$(this).datagrid('selectRow', 0);
			}
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	});
}

function createBindList(){
	$('#bindValueList').datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',halign:'center',width:'10%',align:'right'},
			{field:'name',title:'NAME',halign:'center',width:'18%',align:'left'},
			{field:'value',title:'VALUE',halign:'center',width:'18%',align:'left'},
			{field:'elapsed_time',title:'ELAPSED<br>TIME',halign:'center',width:'18%',formatter:getNumberFormatNullChk,align:'right'},
			{field:'buffer_gets',title:'BUFFER<br>GETS',halign:'center',width:'18%',formatter:getNumberFormatNullChk,align:'right'},
			{field:'rows_processed',title:'ROWS<br>PROCESSED',halign:'center',width:'17%',align:'right'},
		]],
		onLoadSuccess:function(data) {
			let row = data.rows;
			if( row.length > 0 && isNotEmpty(row[0].rnum) ){
				mergeCells(data);
			}
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
		}
	});
}

function mergeCells( data ){
	let fieldArr = ['rnum', 'elapsed_time', 'buffer_gets', 'rows_processed'];
	
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
		console.log('Error Occured while merging', err.message);
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

function createAsisTree(){
	$('#asisPlan').tree({
		animate:true,
		lines:true
	});
}

function popupReset(){
	$('#sqlInfoBySqlId_popup').window({
		title: 'SQL Info',
		width: '1600px',
		height: '680px',
		top:getWindowTop(800),
		left:getWindowLeft(1600)
	});
	
	$('#sqlIdList').datagrid('loadData',[]);
	resetTabs();
}

function resetTabs(){
	$('#sqlText').val('');
	$('#bindValueList').datagrid('loadData',[]);
	$('#asisPlan').tree('loadData',[]);
	$('#asisPlan').html(emptyPlanTxt);
	$('#asisTextPlan').html(emptyPlanTxt);
	
	createExecutionsChart( emptyData );
	createElapsedTimeChart( emptyData );
	createBufferGetsChart( emptyData );
	createDiskReadsChart( emptyData );
}

function setPopupData(projectId, sqlPackId, data){
	if( isNotEmpty(data) ){
		$('#database_kinds_cd_popup').val( $('#database_kinds_cd').val() );
		$('#project_id_popup').val(projectId);
		$('#perf_check_id_popup').val(sqlPackId);
		
		$('#table_owner_popup').val(data.table_owner);
		$('#table_name_popup').val(data.table_name);
		$('#dbid_popup').val(data.asis_dbid);
		$('#exec_seq_popup').val(data.exec_seq);
		$('#access_path_popup').val(data.access_path);
		$('#begin_dt_popup').val(data.perf_check_range_begin_dt);
		$('#end_dt_popup').val(data.perf_check_range_end_dt);
		
		ajaxCall_loadSqlListPopup();
		
	}else {
		return;
	}
}

function ajaxCall_loadSqlListPopup(){
	$('#sqlIdList').datagrid('loadData',[]);
	
	ajaxCall('/AISQLPV/loadSqlIdList',
			$('#sqlInfoBySqlId_form'), 
			'GET',
			callback_loadSqlListPopup);
}
var callback_loadSqlListPopup = function(result){
	try {
		json_string_callback_common(result,'#sqlIdList',true);
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadSqlText(){
	ajaxCall('/SQLAutomaticPerformanceCheck/loadExplainSqlText',
			$('#sqlInfoBySqlId_form'),
			'POST',
			callback_loadSqlText);
}
var callback_loadSqlText = function(result){
	let sqlText = '';
	
	try{
		if( result.result ){
			if( isNotEmpty(result.object) ){
				sqlText = result.object.sql_text;
			}
			$('#sqlText').val( sqlText );
			
		}else{
			console.log('Error Occured while sql text loaded', result.message);
		}
	}catch(err) {
		console.log('Error Occured while sql text loaded', err.message);
	}
}

function ajaxCall_loadBindValue(){
	ajaxCall('/Sqls/bindValueListAll',
			$('#sqlInfoBySqlId_form'),
			'POST',
			callback_loadBindValue);
}
var callback_loadBindValue = function(result) {
	try{
		json_string_callback_common(result,'#bindValueList',true);
		
	} catch(err) {
		console.log('Error Occured', err.message);
	}
};

function ajaxCall_loadAsisPlanTree(){
	ajaxCall('/Sqls/sqlTreePlanListAll',
			$('#sqlInfoBySqlId_form'),
			'POST',
			callback_loadAsisPlanTree);
}
var callback_loadAsisPlanTree = function(result){
	try {
		let data = JSON.parse(result);
		
		if( isNotEmpty(data) ){
			$('#asisPlan').tree('loadData', data);
			$('#asisPlan .tree-node' ).css( 'cursor', 'default' );
			
		}else {
			$('#asisPlan').html(emptyPlanTxt);
		}
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadAsisPlanText(){
	ajaxCall('/Sqls/sqlTextPlanListAll',
			$('#sqlInfoBySqlId_form'),
			'POST',
			callback_loadAsisPlanText);
}
var callback_loadAsisPlanText = function(result){
	try {
		let strHtml = '';
			strHtml += 'ExecutionPlan<br>';
			strHtml += '-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------<br>';
		
		if( result.result ){
			if( isNotEmpty(result.object) ){
				for(let i = 0 ; i < result.object.length ; i++){
					let post = result.object[i];
					strHtml += strReplace(post.execution_plan,' ','&nbsp;')+'<br>';
				}
			}
			
		}else{
			if ( isEmpty(result.message) ) {
				errorMessager('데이터 조회 중 에러가 발생하였습니다.');
				return false;
				
			} else {
				strHtml += '<li>'+ result.message +'</li>';
				console.log('Error Occured', result.message);
			}
		}
		strHtml += '-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------';
		$('#asisTextPlan').html(strHtml);
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function ajaxCall_loadChartData(){
	ajaxCall('/AISQLPV/sqlStateTrend',
			$('#sqlInfoBySqlId_form'),
			'GET',
			callback_loadChartData);
}
var callback_loadChartData = function(result){
	try{
		let data;
		
		if( isNotEmpty(result) ){
			try{
				data = JSON.parse(result);
				data = data.rows;
				
			}catch(e){
				console.log('Error Occured', e.message);
			}
		}else{
			data = [];
		}
		
		createExecutionsChart(data);
		createElapsedTimeChart(data);
		createBufferGetsChart(data);
		createDiskReadsChart(data);
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
}

function createExecutionsChart(data){
	if( isNotEmpty(chartExecutions) ){
		chartExecutions.destroy();
	}
	
	Ext.define('chartExecutions.colors', {
		singleton: true,
		COLOR_01: '#01b0f1'
	});
	
	chartExecutions = Ext.create('Ext.panel.Panel',{
		width: '100%',
		height: '100%',
		border: false,
		renderTo: document.getElementById('chartExecutionsPanel'),
		layout: 'fit',
		items: [{
			xtype: 'cartesian',
			border: false,
			store: {
				data: data
			},
			innerPadding : '5 25 0 0', // 차트안쪽 여백
			insetPadding : '10 10 10 10', // 차트 밖 여백
			axes: [{
				type: 'numeric',
				position: 'left',
				minimum: 0
			},{
				type: 'category',
				position: 'bottom'
			}],
			series: [{
				type: 'line',
				fill: false,
				marker: {
					lineWidth: 2,
					radius: 4,
				},
				xField: 'log_dt',
				yField: 'executions',
				colors: [chartExecutions.colors.COLOR_01],
			}]
		}]
	});
}

function createElapsedTimeChart(data){
	if( isNotEmpty(chartElapsedTime) ){
		chartElapsedTime.destroy();
	}
	
	Ext.define('chartElapsedTime.colors', {
		singleton: true,
		COLOR_01: '#fe0000'
	});
	
	chartElapsedTime = Ext.create('Ext.panel.Panel',{
		width: '100%',
		height: '100%',
		border: false,
		renderTo: document.getElementById('chartElapsedTimePanel'),
		layout: 'fit',
		items: [{
			xtype: 'cartesian',
			border: false,
			store: {
				data: data
			},
			innerPadding : '5 25 0 0', // 차트안쪽 여백
			insetPadding : '10 10 10 10', // 차트 밖 여백
			axes: [{
				type: 'numeric',
				position: 'left',
				minimum: 0
			},{
				type: 'category',
				position: 'bottom'
			}],
			series: [{
				type: 'line',
				fill: false,
				marker: {
					lineWidth: 2,
					radius: 4,
				},
				xField: 'log_dt',
				yField: 'elapsed_time',
				colors: [chartElapsedTime.colors.COLOR_01],
			}]
		}]
	});
}

function createBufferGetsChart(data){
	if( isNotEmpty(chartBufferGets) ){
		chartBufferGets.destroy();
	}
	
	Ext.define('chartBufferGets.colors', {
		singleton: true,
		COLOR_01: '#0170c1'
	});
	
	chartBufferGets = Ext.create('Ext.panel.Panel',{
		width: '100%',
		height: '100%',
		border: false,
		renderTo: document.getElementById('chartBufferGetsPanel'),
		layout: 'fit',
		items: [{
			xtype: 'cartesian',
			border: false,
			store: {
				data: data
			},
			innerPadding : '5 25 0 0', // 차트안쪽 여백
			insetPadding : '10 10 10 10', // 차트 밖 여백
			axes: [{
				type: 'numeric',
				position: 'left',
				minimum: 0
			},{
				type: 'category',
				position: 'bottom'
			}],
			series: [{
				type: 'line',
				fill: false,
				marker: {
					lineWidth: 2,
					radius: 4,
				},
				xField: 'log_dt',
				yField: 'buffer_gets',
				colors: [chartBufferGets.colors.COLOR_01],
			}]
		}]
	});
}

function createDiskReadsChart(data){
	if( isNotEmpty(chartDiskReads) ){
		chartDiskReads.destroy();
	}
	
	Ext.define('chartDiskReads.colors', {
		singleton: true,
		COLOR_01: '#00af50'
	});
	
	chartDiskReads = Ext.create('Ext.panel.Panel',{
		width: '100%',
		height: '100%',
		border: false,
		renderTo: document.getElementById('chartDiskReadsPanel'),
		layout: 'fit',
		items: [{
			xtype: 'cartesian',
			border: false,
			store: {
				data: data
			},
			innerPadding : '5 25 0 0', // 차트안쪽 여백
			insetPadding : '10 10 10 10', // 차트 밖 여백
			axes: [{
				type: 'numeric',
				position: 'left',
				minimum: 0
			},{
				type: 'category',
				position: 'bottom'
			}],
			series: [{
				type: 'line',
				fill: false,
				marker: {
					lineWidth: 2,
					radius: 4,
				},
				xField: 'log_dt',
				yField: 'disk_reads',
				colors: [chartDiskReads.colors.COLOR_01],
			}]
		}]
	});
}

function Btn_SetSQLFormatter(){
	$('#sqlText').format({method: 'sql'});
}

function copy_to_sqlId() {
	let element = document.getElementById('sql_id_popup');
	
	if ( isEmpty(element.value) ) {
		return;
	}
	
	element.setAttribute('type', 'text');
	element.select();
	
	let returnValue = document.execCommand('copy');
	element.setAttribute('type', 'hidden');
	
	infoMessager('복사되었습니다.');
}

function copy_to_clipboard() {
	let target = document.getElementById('sqlText_copy');
	target.value = document.getElementById('sqlText').value;
	target.select();
	
	let returnValue = document.execCommand('copy');
	target.value = '';
	
	infoMessager('복사되었습니다.');
}