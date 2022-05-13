var searchComplete = false;
var isFromParent = false;
var isPaging = false;
var usingindxList;
var indexCnt = 0;

$(document).ready(function() {
	$('body').css('visibility', 'visible');
	
	let project_id = $('#project_id').val();
	isFromParent = isNotEmpty( project_id );
	
	createProjectCombo( project_id );
	createSqlPackCombo( $('#sql_auto_perf_check_id').val() );
	
	resetSummaryData();
	createIndexList();
	createSqlList();
	
	createPopUp();
	createBindList();
	
	/* modal progress close */
	parent.parent.closeMessageProgress();
});  //end of document ready

function resetSummaryData(){
	$('#summaryArea table tr td').text('');
}

function createProjectCombo( project_id ){
	$('#project_combo').combobox({
		url:'/AutoPerformanceCompareBetweenDatabase/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			$('[name=project_id]').val(data.project_id);
			$('#sqlPack_combo').combobox('clear');
		},
		onLoadSuccess: function(data){
			$(this).combobox('setValue', project_id);
			
			if ( isFromParent ){
				ajaxCall_loadSqlPCombo();
			}
		},
		onLoadError: function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}

function createSqlPackCombo( sql_auto_perf_check_id ){
	$('#sqlPack_combo').combobox({
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		onChange: function(){
			$('[name=sql_auto_perf_check_id]').val('');
		},
		onShowPanel: function() {
			ajaxCall_loadSqlPCombo();
		},
		onLoadSuccess: function(data){
			if ( isFromParent ){
				$(this).combobox('setValue', sql_auto_perf_check_id);
				searchAction();
				isFromParent = false;
			}
		},
		onLoadError: function() {
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}

function ajaxCall_loadSqlPCombo(){
	$('#sqlPack_combo').combobox('loadData',[]);
	
	if ( $('#project_combo' ).combobox('getValue') != '' ){
		ajaxCall('/AISQLPV/getSqlPerformancePacList',
				$('#submit_form'), 
				'GET',
				callback_loadSqlPCombo);
	} else {
		return;
	}
}
var callback_loadSqlPCombo = function(result){
	try {
		let data = JSON.parse(result);
		$('#sqlPack_combo').combobox('loadData', data);
		
	}catch(err) {
		console.log('Error Occured', err);
	}
}

function createIndexList() {
	$('#tableLeftList').datagrid({
		view: myview,
		fitColumns: true,
		singleSelect: 'true',
		columns:[[
			{field:'table_owner', title:'OWNER', width:'70px', halign:'center', align:'center', rowspan:2},
			{field:'table_name', title:'테이블명', width:'160px', halign:'center', align:'left', rowspan:2},
			{field:'index_name', title:'추천 인덱스명', width:'110px', halign:'center', align:'left', rowspan:2},
			{field:'sql_cnt', title:'SQL수', width:'42px', halign:'center', align:'right', rowspan:2},
			{title:'ELAPSED TIME', halign:'center', align:'right', colspan:3},
			{title:'BUFFER GETS', halign:'center', align:'right', colspan:3},
			{field:'access_path_column_list', title:'추천 인덱스 칼럼', width:'500px', halign:'center', align:'left', rowspan:2}
		],[
			{field:'asis_elapsed_time',title:'ASIS',width:'70px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_elapsed_time',title:'TOBE',width:'70px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'임팩트(배)',width:'90px',halign:'center',align:'right',formatter:elapsedTimeRegres,sortable:true,sorter:numberSorter},
			
			{field:'asis_buffer_gets',title:'ASIS',width:'70px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'TOBE',width:'70px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'buffer_increase_ratio',title:'임팩트(배)',width:'95px',halign:'center',align:'right',formatter:bufferGetsRegres,sortable:true,sorter:numberSorter}
		]],
		onSelect:function(index, row) {
			if ( row.table_owner == '전체' ){
				$('#submit_form_excel [name=table_owner]').val( $('#table_owner').val() );
				$('#submit_form_excel [name=table_name]').val( $('#table_name').val() );
				$('#submit_form_excel [name=index_name]').val('');
				
			}else {
				$('#submit_form_excel [name=table_owner]').val(row.table_owner);
				$('#submit_form_excel [name=table_name]').val(row.table_name);
				$('#submit_form_excel [name=index_name]').val(row.index_name);
			}
			
			loadRightTable();
		},
		onLoadSuccess:function(data){
			if( data.total > 0 ){
				if( isPaging == false ){
					$(this).datagrid('selectRow', 0);
				}
			}
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	});
}
function elapsedTimeRegres(val, row){
	let isNotChanged = (val == 1) && (row.asis_elapsed_time == row.tobe_elapsed_time);
	
	return nullChkAndImgSet(val, isNotChanged);
}
function bufferGetsRegres(val, row){
	let isNotChanged = (val == 1) && (row.asis_buffer_gets == row.tobe_buffer_gets);
	
	return nullChkAndImgSet(val, isNotChanged);
}
function nullChkAndImgSet(val, isNotChanged) {
	let newVal = "0";
	
	try {
		if ( isNotEmpty(val) && val != 0 ) {
			if ( isNotChanged == false ) {
				let imgTag = ( val < 0 ) ? 'down' : 'up';
				
				newVal = '<img class="ratioIcon" src="/resources/images/' + imgTag + '_arrow.png">' + formatComma(val);
			}
		} else if ( isEmpty(val) ) {
			newVal = null;
		}
		
	} catch(err){
		console.log('Error Occured', err);
	}
	return newVal;
}

function createSqlList(result){
	$('#tableRightList').datagrid({
		view: myview,
		fitColumns: true,
		columns:[[
			{field:'perf_impact_type_nm',title:'성능임팩트<br>유형',width:'80px',halign:'center',align:'center',rowspan:2},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'80px',halign:'center',align:'right',formatter:bufferGetsRegres,sortable:true,rowspan:2,sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'80px',halign:'center',align:'right',formatter:elapsedTimeRegres,sortable:true,rowspan:2,sorter:numberSorter},
			/*{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'50px',halign:'center',align:'center',styler:cellStyler,rowspan:2},*/ // 재사용 가능성이 있어 삭제하지 않고 주석처리함.
			{field:'sql_id',title:'SQL ID',width:'108px',halign:'center',align:'left',rowspan:2},
			
			{title:'PLAN HASH VALUE',halign:'center',align:'right',colspan:2},
			{title:'ELAPSED TIME',halign:'center',align:'right',colspan:2},
			{title:'BUFFER GETS',halign:'center',align:'right',colspan:2},
			{title:'EXECUTIONS',halign:'center',align:'right',colspan:2},
			{title:'ROWS PROCESSED',halign:'center',align:'right',colspan:2},
			{title:'FULLSCAN YN',halign:'center',align:'right',colspan:2},
			{title:'PARTITION ALL ACCESS YN',halign:'center',align:'right',colspan:2},
			
			{field:'sql_command_type_cd',title:'SQL 명령 유형',width:'80px',halign:'center',align:'center',rowspan:2},
			{field:'parsing_schema_name',title:'PARSING<br>SCHEMA NAME',width:'90px',halign:'center',align:'left',rowspan:2},
			{field:'sql_text_web',title:'SQL TEXT',width: '200px',halign:'center',align:'left',rowspan:2},
			{field:'table_owner',title:'OWNER',width:'100px',halign:'center',align:'left',rowspan:2},
			{field:'table_name',title:'테이블명',width:'155px',halign:'center',align:'left',rowspan:2},
			{field:'index_name',title:'추천 인덱스명',width:'110px',halign:'center',align:'left',rowspan:2},
			{field:'project_nm',title:'프로젝트명',width:'180px',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_name',title:'SQL점검팩명',width:'180px',halign:'center',align:'left',rowspan:2},
			],[
			{field:'asis_plan_hash_value',title:'ASIS',width:'80px',halign:'center',align:'right'},
			{field:'tobe_plan_hash_value',title:'TOBE',width:'80px',halign:'center',align:'right'},
			
			{field:'asis_elapsed_time',title:'ASIS',width:'60px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_elapsed_time',title:'TOBE',width:'60px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_buffer_gets',title:'ASIS',width:'60px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'TOBE',width:'60px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_executions',title:'ASIS',width:'55px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_executions',title:'TOBE',width:'55px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_rows_processed',title:'ASIS',width:'55px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_rows_processed',title:'TOBE',width:'55px',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_fullscan_yn',title:'ASIS',width:'50px',halign:'center',align:'center'},
			{field:'tobe_fullscan_yn',title:'TOBE',width:'50px',halign:'center',align:'center'},
			
			{field:'asis_partition_all_access_yn',title:'ASIS',width:'75px',halign:'center',align:'center'},
			{field:'tobe_partition_all_access_yn',title:'TOBE',width:'75px',halign:'center',align:'center'}
		]],
		onClickRow: function(index,row){
			showSqlTextPopup(row);
		},
		onLoadError: function() {
			searchComplete = true;
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		},
	});
}

/* 검색 */
function Btn_OnClick(){
	searchComplete = false;
	isPaging = false;
	
	if( formValidationCheck() ){
		resetSummaryData();
		sizeAdjuster();
		
		$('#tableLeftList').datagrid('loadData', []);
		$('#tableRightList').datagrid('loadData', []);
		
		$('[name=currentPage], #currentPage2').val('1');
		$('[name=pagePerCount], #rcount').val('20');
		
		fnEnableDisablePagingBtn2(0);
		searchAction();
		
	}else {
		searchComplete = true;
	}
}
function searchAction(){
	setSearchData();
	
	ajaxCall_loadSummaryData();
	ajaxCall_loadLeftTable();
}
function setSearchData(){
	$('[name=project_id]').val( $('#project_combo').combobox('getValue') );
	$('[name=sql_auto_perf_check_id]').val( $('#sqlPack_combo').combobox('getValue') );
	$('[name=table_owner]').val( $('#owner_textbox').textbox('getValue') );
	$('[name=table_name]').val( $('#table_name_textbox').textbox('getValue') );
}

function ajaxCall_loadSummaryData(){
	$('#summaryArea table tr td').text('-');
	
	ajaxCall('/AISQLPV/loadSummaryData',
			$('#submit_form'),
			'GET',
			callback_loadSummaryData);
}
var callback_loadSummaryData = function(result){
	try{
		if( isNotEmpty(result) ){
			let data = JSON.parse(result)
			
			if( data.totalCount > 0 ){
				setSummaryData(data.rows[0]);
			}
		}
		
	} catch(err) {
		console.log('Error Occured', err);
		searchComplete = true;
	}
}

function setSummaryData(restSummary){
	let $tdArr, asis_elapsed_time, tobe_elapsed_time, asis_buffer_gets, tobe_buffer_gets, 
		asis_total_elapsed_time, tobe_total_elapsed_time, asis_total_buffer_gets, tobe_total_buffer_gets = '';
	
	try{
		asis_elapsed_time = restSummary.asis_elapsed_time;
		tobe_elapsed_time = restSummary.tobe_elapsed_time;
		asis_buffer_gets = restSummary.asis_buffer_gets;
		tobe_buffer_gets = restSummary.tobe_buffer_gets;
		asis_total_elapsed_time = restSummary.asis_total_elapsed_time;
		tobe_total_elapsed_time = restSummary.tobe_total_elapsed_time;
		asis_total_buffer_gets = restSummary.asis_total_buffer_gets;
		tobe_total_buffer_gets = restSummary.tobe_total_buffer_gets;
		
		$tdArr = $('#summaryArea table tr:last-child td');
		{
			$tdArr.eq(0).text( addComma(restSummary.recommend_index_add_cnt) );
			$tdArr.eq(1).text( addComma(restSummary.recommend_index_modify_cnt) );
			$tdArr.eq(2).text( addComma(restSummary.sql_cnt) );
			$tdArr.eq(3).text( addComma(restSummary.tablespace_usage) );
			$tdArr.eq(4).text( addComma(asis_elapsed_time) );
			$tdArr.eq(5).text( addComma(tobe_elapsed_time) );
			$tdArr.eq(6).text( addComma(asis_buffer_gets) );
			$tdArr.eq(7).text( addComma(tobe_buffer_gets) );
			$tdArr.eq(8).text( addComma(asis_total_elapsed_time) );
			$tdArr.eq(9).text( addComma(tobe_total_elapsed_time) );
			$tdArr.eq(10).text( addComma(asis_total_buffer_gets) );
			$tdArr.eq(11).text( addComma(tobe_total_buffer_gets) );
		}
		{
			$tdArr = $('#summaryArea table tr:nth-child(2) td');
			
			$tdArr.eq(0).html( makeHtml(restSummary.elapsed_time_ratio, asis_elapsed_time, tobe_elapsed_time, ' 배') );
			$tdArr.eq(1).html( makeHtml(restSummary.buffer_gets_ratio, asis_buffer_gets, tobe_buffer_gets, ' 배') );
			$tdArr.eq(2).html( makeHtml(restSummary.total_elapsed_time_percent, asis_total_elapsed_time, tobe_total_elapsed_time, ' %') );
			$tdArr.eq(3).html( makeHtml(restSummary.total_buffer_gets_percent, asis_total_buffer_gets, tobe_total_buffer_gets, ' %') );
		}
		
	} catch(err){
		console.log('Error Occured', err);
		
	} finally {
		$tdArr, asis_elapsed_time, tobe_elapsed_time, asis_buffer_gets, tobe_buffer_gets
		, asis_total_elapsed_time, tobe_total_elapsed_time, asis_total_buffer_gets, tobe_total_buffer_gets = null;
	}
	
	function makeHtml(ratio, asis, tobe, unit){
		let htmlText = '';
		
		try {
			if( isNotEmpty(ratio) ){
				if( ratio == 0 || (ratio == 1 && asis == tobe) ){
					htmlText = ratio + unit;
					
				}else {
					let imgTag = (ratio < 0) ? 'down' : 'up';
					
					htmlText = '<p class="imgP"><img src="/resources/images/';
					htmlText += imgTag + '_arrow.png"></p><span>' + formatComma(ratio) + unit +'</span>';
				}
				
			}else {
				htmlText = '-';
			}
			
		} catch(err){
			console.log('Error Occured', err);
			htmlText = new String(ratio);
		}
		return htmlText;
	}
	function addComma(num){
		if( isNotEmpty(num) ){
			return formatComma(num);
			
		}else{
			return '-';
		}
	}
}

function ajaxCall_loadLeftTable(){
	// modal progress open
	if(parent.openMessageProgress != undefined)
		parent.openMessageProgress('인덱스별 성능 영향도 분석 결과 조회',' ');
	
	ajaxCall('/AISQLPV/loadIndexList',
			$('#submit_form'),
			'GET',
			callback_loadLeftTable);
}
var callback_loadLeftTable = function(result){
	try{
		if( isNotEmpty(result) ){
			let data = JSON.parse(result);
			let dataCnt = data.dataCount4NextBtn;
			
			$('#tableLeftList').datagrid('loadData', data.rows);
			fnEnableDisablePagingBtn(dataCnt);
			
			if( dataCnt <= 0 ){
				searchComplete = true;
			}
		}else {
			searchComplete = true;
		}
	} catch(err) {
		searchComplete = true;
		console.log('Error Occured', err);
		
	}finally {
		if( searchComplete )
			closeSearchProgress();
	}
}

function loadRightTable(){
	$('#currentPage2').val('1');
	
	getModulePerformanceTable();
}

function ajaxCall_loadRightTable(){
	// modal progress open
	if(parent.openMessageProgress != undefined)
		parent.openMessageProgress('인덱스별 성능 영향도 분석 결과 조회',' ');
	
	$('#tableRightList').datagrid('loadData', []);
	
	ajaxCall('/AISQLPV/loadSqlListByIndex',
			$('#submit_form_excel'),
			'GET',
			callback_loadRightTable);
}

var callback_loadRightTable = function(result){
	try{
		let data = JSON.parse(result);
		
		$('#tableRightList').datagrid('loadData', data.rows);
		fnEnableDisablePagingBtn2(data.dataCount4NextBtn);
		
	} catch(err) {
		console.log('Error Occured', err);
		
	}finally {
		searchComplete = true;
		closeSearchProgress();
	}
}

function Excel_Download(){
	if ( $('#sql_auto_perf_check_id').val() != '' ) {
		let src = '/AISQLPV/loadSqlListByIndex/excelDownload';
		
		$('#submit_form_excel').attr('action',src);
		$('#submit_form_excel').submit();
		
		showDownloadProgress( src );
		
		$('#submit_form_excel').attr('action','');
		
	}else {
		warningMessager('인덱스별 성능 영향도 분석 결과 조회 후 가능합니다.');
		return false;
	}
}

function formValidationCheck(){
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	if ( $('#sqlPack_combo').combobox('getValue') == '' ) {
		warningMessager('SQL점검팩을 선택해 주세요.');
		return false;
	}
	return true;
}
function fnSearch(){
	isPaging = true;
	
	ajaxCall_loadLeftTable();
}

function getModulePerformanceTable(){
	$('#currentPage_excel').val( $('#currentPage2').val() );
	
	ajaxCall_loadRightTable();
}
function sizeAdjuster( $target, mainElement ){
	let subElement = '';
	let actionExpand = '';
	
	try{
		if( isEmpty($target) ){
			actionExpand = false;
			
		}else {
			actionExpand = $target.dataset.expanding == 'true';
		}
		
		if( actionExpand ){
			subElement = ( mainElement == 'Left' ) ? 'Right' : 'Left';
			
			$target.classList.remove( 'expandIcon', 'compressIcon' );
			$target.classList.add( actionExpand ? 'compressIcon' : 'expandIcon' );
			
			$('#layout' + subElement).css('width', '0%');
			$('#layout' + mainElement).css('width', '100%');
			
			$target.dataset.expanding = actionExpand ? 'false' : 'true';
			
		}else {
			sizeReset();
		}
		
	}catch(err){
		sizeReset();
		console.log('Error Occured', err);
		
	} finally {
		$('#layoutInnerLeft').layout('resize', {width: '100%'});
		$('#layoutInnerRight').layout('resize', {width: '100%'});
	}
}
function sizeReset(){
	$('#layoutLeft').css('width', '39.9%');
	$('#layoutRight').css('width', '59.9%');
	
	let $resetElement = $('.compressIcon');
	$resetElement.removeClass( 'compressIcon' );
	$resetElement.addClass( 'expandIcon' );
	$resetElement.attr('data-expanding', 'true');
}
function closeSearchProgress(){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}