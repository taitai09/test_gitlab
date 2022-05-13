var timer;
var reloadLowerTable = true;
var isNotComplete = true;
var indexCnt = 0;
var auth_cd = '';
var isDevRole = false;
var isLeaderAuth = false;

$(document).ready(function() {
	$('body').css('visibility', 'visible');
	
	createlowerList();
	
	auth_cd = $('#auth_cd').val();
	isDevRole = auth_cd == 'ROLE_DEV';
	isLeaderAuth = $('#leader_yn').val() == 'Y';
	
	createCombobox('#project_id');
	customProjectCombo();
	createList();
	
	if ( auth_cd == 'ROLE_DBMANAGER' || auth_cd == 'ROLE_OPENPOPMANAGER' ){
		createRefreshBtn();
		
	}else {
		removeRefreshBtn();
	}
	
	closePageLoadMessageProgress();
});	//end of document ready

function createlowerList(){
	isNotComplete = true;
	
	ajaxCall_LoadAllIndex();
}

function ajaxCall_LoadAllIndex(){
	ajaxCall('/sqlStandards/loadAllIndex?exclusion=',
			null,
			'GET',
			callback_LoadAllIndex);
}
var callback_LoadAllIndex = function(result){
	try{
		if ( result ) {
			let data = JSON.parse(result);
			let indexList = data.rows;
			
			let columns = [
				{field:'PROJECT_NM',title:'프로젝트',width:'200px',halign:'center',align:'left'},
				{field:'WRKJOB_CD_NM',title:'업무',width:'150px',halign:'center',align:'left'},
				{field:'PREVIOUS_PROGRAM_CNT',title:'전회차 총본수',width:'150px',halign:'center',align:'right'},
				{field:'CURRENT_PROGRAM_CNT',title:'금회차 총본수',width:'150px',halign:'center',align:'right'},
				{field:'PREVIOUS_TOT_ERR_CNT',title:'전회차 미준수본수',width:'150px',halign:'center',align:'right'},
				{field:'CURRENT_TOT_ERR_CNT',title:'금회차 미준수본수',width:'150px',halign:'center',align:'right'},
				{field:'PREVIOUS_COMPIANCE_RATE',title:'전회차 준수율',width:'150px',halign:'center',align:'right'},
				{field:'CURRENT_COMPIANCE_RATE',title:'금회차 준수율',width:'150px',halign:'center',align:'right'},
				{field:'SQL_PARSING_ERR_CNT',title:'SQL 파싱 오류',width:'150px',halign:'center',align:'right'},
				
				{field:'CURRENT_SQL_STD_GATHER_DAY',title:'금회차 작업일자',width:'150px',halign:'center',align:'center'},
				{field:'PREVIOUS_SQL_STD_GATHER_DAY',title:'전회차 작업일자',width:'150px',halign:'center',align:'center'},
			];
			
			if( indexList != null ){
				indexCnt = indexList.length;
				
				let position = 9;
				let idxNm = '';
				let colWidth = '150px';
				
				for(let i = 0; i < indexCnt; i++){
					idxNm = indexList[i].qty_chk_idt_nm;
					colWidth = byteLength( idxNm ) * 6.5 + 'px';
				
					columns.splice(position + i, 0,
							{field:'SQL'+indexList[i].qty_chk_idt_cd+'ERR_CNT',title:idxNm, width:colWidth, halign:'center',align:'right', hidden:true});
				}
			}
			
			$('#tableLowerList').datagrid({
				view: myview,
				singleSelect: true,
				checkOnSelect : false,
				selectOnCheck : false,
				columns: [columns],
				onClickRow: function(index,row){
					if( isDevRole ){
						$(this).datagrid('unselectRow', index);
						
					}else {
						let wrkjob_cd = ( isEmpty(row.WRKJOB_CD_NM) ) ? '' : row.WRKJOB_CD;
						let param = 'project_id='+ row.PROJECT_ID + '&wrkjob_cd='+ wrkjob_cd;
						
						parent.moveToOtherTab(1, 'nonStandardSql', param);
					}
				},
				onLoadSuccess:function(){
					if( isDevRole ){
						$('.sqlStandardCheckResult .bottom_table table tbody tr td').css('cursor', 'default');
					}
				},
				onLoadError:function() {
					errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
				}
			});
			
		}else {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	}catch(e){
		errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		
	}finally{
		isNotComplete = false;
	}
}

function customProjectCombo(){
	$('#project_id').combobox({
		onLoadSuccess: function(){
			if( isDevRole == false ){
				$(this).combobox('setValue','selectAll');
				Btn_OnClick();
				
			}else { return; }
		}
	});
}

function createList() {
	$('#tableUpperList').datagrid({
		view: myview,
		singleSelect: 'true',
		selectOnCheck: 'true',
		columns:[[
			{field:'chk',halign:'center',width:'2%',align:'center',checkbox:true,rowspan:2},
			{field:'job_scheduler_nm',title:'스케줄러명',width:'18%',halign:'center',align:'left',rowspan:2},
			{field:'exec_status',title:'수행상태',width:'12%',halign:'center',align:'center',rowspan:2, styler:cellStyler},
			{field:'exec_result',title:'수행결과',width:'21%',halign:'center', colspan:3},
			{field:'exec_start_dt',title:'수행시작일시',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_end_dt',title:'수행종료일시',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_start_time',title:'수행시간',width:'7%',halign:'center',align:'right',rowspan:2},
			{field:'project_nm',title:'프로젝트',width:'18%',halign:'center',align:'left',rowspan:2},
			{field:'analyzeResult',title:'분석 결과',width:'6%',halign:'center',align:'center',rowspan:2,formatter: formatBtn},
			{field:'sql_std_qty_chkt_id',hidden:true,rowspan:2},
			{field:'sql_std_qty_scheduler_no',hidden:true,rowspan:2},
			{field:'project_id',hidden:true,rowspan:2},
			],[
			{field:'sql_cnt',title:'전체',width:'5%',halign:'center',align:'right'},
			{field:'complete_cnt',title:'완료',width:'5%' ,halign:'center',align:'right'},
			{field:'in_progress_sql_cnt',title:'수행중',width:'5%',halign:'center',align:'right'},
		]],
		onCheck:function(index, row) {
			preventClick(index, row);
		},
		onSelect:function(index, row) {
			preventClick(index, row);
			removeSelected(index);
			
			$('#project_id_lowList').val('');
			$('#wrk_complete_yn').val(row.exec_status);
			
			if ( row.exec_status == '완료' ){
				loadLowerTable(row);
				
			}else {
				loadLowerTable(null);
			}
		},
		onClickCell:function(index,field,value) {
			if( field == 'analyzeResult' ){
				let row = $(this).datagrid('getRows')[index];
				let param = 'project_id='+ row.project_id + '&wrkjob_cd='+ $('#wrkjob_cd').val();
				
				parent.moveToOtherTab(1, 'nonStandardSql', param);
				row = null;
			}
		},
		onBeforeSelect:function(){
			$('#tableUpperList').datagrid('clearChecked');
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data){
			if(data.rows.length>0){
				for(let i = 0; i<data.rows.length; i++){
					if(data.rows[i].exec_status != '수행중'){
						$('[datagrid-row-index='+i+'] > [field=chk] input').attr('disabled',true);
					}
				}
				
				if(reloadLowerTable == true){
					try{
						$(this).datagrid('selectRow', 0);
						
					}catch(e){
						console.log(e.message);
					}
				}
			}
		}
	});
	
	function formatBtn(){
		return '<img src="/resources/images/report.png" alt="Button for result" class="btnImg">';
	}
	
	$('.datagrid-header-row > [field=chk] > div > input').remove();
	
	function preventClick( index,row ){
		if(row.exec_status != '수행중'){
			$('#tableUpperList').datagrid('uncheckRow', index);
		}
	}
	function removeSelected(index){
		let classNm = $('#tableUpperList_datagrid-row-r5-2-'+index).attr('class').split(' ');
		
		let isSelected = false
		for(i = 0; i<classNm.length; i++){
			if( classNm[i] == 'selectedRow' ){
				isSelected = true;
				break;
			}
		}
		$('.datagrid-btable tbody tr').removeClass('selectedRow');
		
		if (isSelected == false) {
			$('#tableUpperList_datagrid-row-r5-2-'+index).addClass('selectedRow');
		}
	}
}

function cellStyler( value,row,index ) {
	if ( value == '강제완료' ) {
		return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
	} else if ( row.exec_status == '완료'){
		return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
	} else if ( row.exec_status == '수행중'){ 
		return ' background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
	} else {
		return 'color:red; background-image:url(/resources/images/error.png);background-repeat: no-repeat;background-position-x: right;';
	}
}
/* 새로고침 */
function createRefreshBtn(){
	// 새로고침 슬라이드 버튼 생성
	$('#chkRefresh').switchbutton({
		value: 'off',
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked ) {
					$('#chkRefresh').switchbutton('setValue','on');
					
					if ( $('#tableUpperList').datagrid('getRows').length < 1 ) {
						
						$('#chkRefresh').switchbutton('uncheck');
						
						return;
					}
					
					$('#timer_value').textbox({disabled:true});
					
					refreshList( checked );
				} else {
					$('#chkRefresh').switchbutton('setValue','off');
					$('#timer_value').textbox({disabled:false});
					
					setNumberBoxAttr();
					
					window.clearTimeout(timer);
				}
				
			},300);
		}
	});
	setNumberBoxAttr();
}
function removeRefreshBtn(){
	try {
		let target = document.getElementById('refreshBtnWrapper');
		
		while ( target.hasChildNodes() ) {
			target.removeChild( target.firstChild );
		}
		
		target.style.height = 0;
		
		$('#variableDiv').layout('resize', {
			width: '100%',
			height: '237px'
		});
		
	}catch (err){
		console.log('Error Occured', err);
	}
}

function refreshList( checked ) {
	let intSec = 0;
	reloadLowerTable = false;
	
	ajaxCall_UpperTableList(false);
	
	if ( checked ) {
		intSec = strParseInt( $('#timer_value').textbox('getValue'),0 );
		timer = window.setTimeout('refreshList(true)',(intSec*1000));
		
	} else {
		window.clearTimeout(timer);
	}
}
/* 검색 */
function Btn_OnClick(){
	if ( formValidationCheck() ){
		reloadLowerTable = true;
		
		$('#tableUpperList').datagrid('loadData', []);
		$('#tableLowerList').datagrid('loadData', []);
		$('#chkRefresh').switchbutton('uncheck');
		$('#chkRefresh').switchbutton('setValue','off');
		
		ajaxCall_UpperTableList(true);
		
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $('#menu_id').val();
	}
}

function formValidationCheck(){
	if ( isEmpty($('#project_id').combobox('getValue')) ){
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	return true;
}

function ajaxCall_UpperTableList(messageOpen){
	if( $('#chkRefresh').val() != 'on' && messageOpen){
		// modal progress open
		if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회',' ');
	}
	
	ajaxCall('/sqlStandardCheckResult/loadSchedulerList',
			$('#submit_form'),
			'GET',
			callback_UpperTableList);
}

var callback_UpperTableList = function(result){
	try {
		json_string_callback_common(result, '#tableUpperList', true);
		
	} catch(err) {
		console.error(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}
/* 검색 끝 */

/* 하단 테이블 데이터로드 */
function loadLowerTable(row){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회',' ');
	
	$('#tableLowerList').datagrid('loadData', []);
	
	if(row){
		$('#project_id_lowList').val(row.project_id);
		
		ajaxCall_LoadIndexList();	// 사용 점검지표
		
	}else {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function ajaxCall_LoadIndexList(){
	ajaxCall('/sqlStandardCheckResult/loadIndexList',
			$('#submit_form_lowList'),
			'GET',
			callback_LoadIndexList);
}

var callback_LoadIndexList = function(result){
	try {
		let flag = true;
		while(flag || isNotComplete){
			flag = $('#tableLowerList').datagrid('getColumnFields', false) > 0;
		}
		
		let index = 0;
		for(i = 0; i < indexCnt; i++){
			index = 100 + i;
			$('#tableLowerList').datagrid('hideColumn', 'SQL'+ index +'ERR_CNT');
		}
		
		if(result){
			let rows = JSON.parse(result).rows;
			for(i = 0; i< rows.length; i++){
				$('#tableLowerList').datagrid('showColumn', 'SQL'+rows[i].qty_chk_idt_cd+'ERR_CNT');
			}
		}
		ajaxCall_LoadLowerData();
		
	} catch(err) {
		console.error(err);
	}
}

function ajaxCall_LoadLowerData(){
	ajaxCall('/sqlStandardCheckResult/loadResultList',
			$('#submit_form_lowList'),
			'GET',
			callback_LoadLowerData);
}

var callback_LoadLowerData = function(result){
	try{
		json_string_callback_common(result,'#tableLowerList',true);
		
	}catch(e){
		console.log('Error Occured LoadLowerData:', e);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}
/* 하단 테이블 데이터로드 끝 */

/* 강제 완료 */
function Btn_ForcedCompletion() {
	let data = $('#tableUpperList').datagrid('getChecked');
	
	if( !data || data.length != 1 ){
		parent.$.messager.alert('경고','강제완료를 처리할 스케줄러를 선택 해 주세요.','warning');
		return false;
		
	}else {
		let param = '확인';
		let msgStr ='진행 중인 작업이 중단됩니다. 강제완료를 처리하시겠습니까?';
		
		$('#sql_std_qty_scheduler_no').val(data.sql_std_qty_scheduler_no);
		
 		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress('강제완료처리', '강제완료 처리 중입니다.');
				
				ajaxCall_ForcedCompletion(data[0]);
			}
		});
	}
}

function ajaxCall_ForcedCompletion(row){
	let submitMsg = {json_list : [{
			force_completion : 'true'
			, sql_std_qty_chkt_id : row.sql_std_qty_chkt_id
			, project_id : row.project_id
			, sql_std_qty_scheduler_no : row.sql_std_qty_scheduler_no
			, sql_std_qty_div_cd : '2'
			, is_last : 'true'
	}]};
	
	ajaxCallWithJson('/ssopi/receiveBox',
			submitMsg,
			'POST',
			callback_ForcedCompletion);
}

var callback_ForcedCompletion = function(result){
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('정보','강제완료 처리가 완료 되었습니다.','info');
		
	}else {
		parent.$.messager.alert('정보','강제완료처리에 실패 하였습니다..' , 'info');
	}
	
	setTimeout(ajaxCall_UpperTableList(false), 200);
}

/* 엑셀 다운로드 */
function Excel_Download() {
	if( $('#project_id_lowList').val() == '' ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
	}
	
	let src = '/sqlStandardCheckResult/excelDownload';
	
	$('#submit_form_lowList').attr('action',src);
	$('#submit_form_lowList').submit();
	$('#submit_form_lowList').attr('action','');
	
	showDownloadProgress( src );		// 대용량 엑셀 다운로드 로딩 바
}

function setNumberBoxAttr(){
	let obj = document.getElementsByName('timer_value');

	if( isNotEmpty(obj) ){
		obj = obj[0];
		obj = obj.parentElement;
		
		if(isNotEmpty(obj) && isNotEmpty(obj.firstChild)){
			obj.firstChild.setAttribute('min', 3);
		}
	}else { return false; }
}