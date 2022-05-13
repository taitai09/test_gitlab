const emptyInxList = [{'qty_chk_idt_nm' : '전체', 'qty_chk_idt_cd' : '000'}];
var usingindxList;
var indexCnt = 0;
var auth_cd = '';
var isDevRole = false;
var isLeaderAuth = false;
var call_from_parent = false;

$(document).ready(function(){
	$('body').css('visibility', 'visible');
	
	if ( indexListFromParent ){
		usingindxList = JSON.parse(indexListFromParent).rows;
	}
	createNonStdSqlList( allIndesList );
	
	auth_cd = $('#auth_cd').val();
	isDevRole = auth_cd == 'ROLE_DEV';
	isLeaderAuth = $('#leader_yn').val() == 'Y';
	
	let project_id_inherited = $('#project_id_inherited').val();
	call_from_parent = isNotEmpty(project_id_inherited);
	
	createCombobox('#project_combo');
	customProjectCombo( project_id_inherited );
	
	if ( isDevRole && isLeaderAuth == false ){
		setDeveloperId();
	}
	
	createWrkjobCombo( $('#wrkjob_cd_inherited').val() );
	createIndexCombo();
	
	closePageLoadMessageProgress();
});	//end of document ready

function setDeveloperId(){
	$('#user_nm_textbox').textbox('setValue', $('#user_nm').val());
	$('#user_nm_textbox').textbox({readonly:true});
	
	$('#dev_id_textbox').textbox('setValue', $('#developer_id').val());
	$('#dev_id_textbox').textbox({readonly:true});
}

function customProjectCombo( project_id ){
	$('#project_combo').combobox({
		onChange: function(newValue, oldValue){
			$('#wrkjob_combo').combobox('clear');
			
			$('#project_id').val( newValue );
			loadIndexList();
			
			if( isDevRole && isNotEmpty(newValue) ){
				let url = '/Common/getUserWrkJobCd?project_id=' + newValue;
				
				ajaxCall_LoadWrkjobList( url );
			}
		},
		onLoadSuccess: function(){
			$(this).combobox('setValue', project_id);
		}
	});
}

function createWrkjobCombo( wrkjob_cd ){
	$('#wrkjob_combo').combobox({
		method:'get',
		valueField:'id',
		textField:'text',
		onLoadSuccess: function(data){
			if ( call_from_parent && data.length > 0 ){
				$(this).combobox('setValue', wrkjob_cd);
				
				dataResetAndSearch();
				call_from_parent = false;
			}
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	if( isDevRole == false ){
		ajaxCall_LoadWrkjobList('/Common/getWrkJobCd?isAll=Y');
		
	}else {
		$('#wrkjob_combo').combobox('loadData', []);
	}
}

function createIndexCombo(){
	$('#qty_chk_idt_cd_combo').combobox({
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_nm',
		onLoadSuccess: function(){
			$(this).combobox('setValue', '000');
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#qty_chk_idt_cd_combo').combobox('loadData', emptyInxList);
}

function createNonStdSqlList(result){
	try {
		if ( result ){
			let data = JSON.parse(result);
			let indexList = data.rows;
			
			let columns = [
				{field:'WRKJOB_CD_NM',title:'업무',width:'100px',halign:'center',align:'left'},
				{field:'DEVELOPER_NM',title:'개발자명',width:'70px',halign:'center',align:'center'},
				{field:'DEVELOPER_ID',title:'개발자ID',width:'100px',halign:'center',align:'left'},
				{field:'DIR_NM',title:'디렉토리',width:'350px',halign:'center',align:'left'},
				{field:'FILE_NM',title:'파일명',width:'150px',halign:'center',align:'left'},
				{field:'PROGRAM_NM',title:'ID',width:'120px',halign:'center',align:'left'},
				{field:'SQL_COMMAND_TYPE_CD',title:'SQL명령유형',width:'80px',halign:'center',align:'center'},
				{field:'DBIO',title:'SQL식별자(DBIO)',width:'300px',halign:'center',align:'left'},
				{field:'SQL_PARSING_ERR_YN',title:'SQL 파싱 오류',width:'80px',halign:'center',align:'center'},
				
				{field:'SQL_TEXT',title:'SQL Text',width:'320px',halign:'center',align:'center'},
				{field:'PROJECT_NM',title:'프로젝트',width:'200px',halign:'center',align:'left'},
				{field:'SQL_STD_GATHER_DAY',title:'작업일자',width:'100px',halign:'center',align:'center'},
			];
			
			if ( isNotEmpty(indexList) ){
				indexCnt = indexList.length;
				
				let position = 9;
				let colWidth = '150px';
				
				for ( let i = 0; i < indexCnt; i++ ){
					colWidth = byteLength( indexList[i].qty_chk_idt_nm ) * 6.3 + 'px';
					
					columns.splice(position + i, 0,
							{field:'SQL'+indexList[i].qty_chk_idt_cd+'ERR_YN', title:indexList[i].qty_chk_idt_nm, width: colWidth, halign:'center', align:'center', hidden:true});
				}
			}
			
			$('#tableList').datagrid({
				view: myview,
				fitColumns: true,
				columns: [columns],
				onClickRow: function(index,row){
					console.log(row.SQL_TEXT);
				},
				onLoadError: function(){
					errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
				},
			});
			
		}else {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
		
	} catch(e){
		errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
	}
}

/* 검색 */
function Btn_OnClick(){
	if ( formValidationCheck() ){
		dataResetAndSearch();
	}
}
function dataResetAndSearch(){
	$('#project_id').val( $('#project_combo').combobox('getValue') );
	$('#wrkjob_cd').val( $('#wrkjob_combo').combobox('getValue') );
	$('#user_nm').val( $('#user_nm_textbox').textbox('getValue') );
	$('#developer_id').val( $('#dev_id_textbox').textbox('getValue') );
	$('#dbio').val( $('#dbio_textbox').textbox('getValue') );
	
	let indexCode = $('#qty_chk_idt_cd_combo').combobox('getValue');
	indexCode = ( isEmpty(indexCode) ) ? '000' : indexCode;
	$('#qty_chk_idt_cd').val( indexCode );
	
	$('#tableList').datagrid('loadData', []);
	$('#tableList').datagrid('loading');
	
	$('#currentPage').val('1');
	$('#pagePerCount').val('20');
	
	columnHideAndShow();
}

/* 품질 점검 지표 로드 */
function loadIndexList(){
	$('#qty_chk_idt_cd_combo').combobox('loadData', emptyInxList);
	
	ajaxCall_LoadIndexList();
}

function ajaxCall_LoadIndexList(){
	ajaxCall('/execSqlStdChk/loadIndexList',
			$('#submit_form'),
			'GET',
			callback_LoadIndexList);
}
var callback_LoadIndexList = function(result){
	loadIndexCombo(result);
}

function loadIndexCombo(result){
	if ( result ){
		usingindxList = JSON.parse(result).rows;
		
		let rows = JSON.parse(result).rows;
		rows.unshift({'qty_chk_idt_cd' : '000', 'qty_chk_idt_nm' : '전체'});
		
		$('#qty_chk_idt_cd_combo').combobox('loadData', rows);
	}
}

function ajaxCall_LoadWrkjobList( url ){
	ajaxCall(url,
			null,
			'GET',
			callback_LoadWrkjobList);
}
var callback_LoadWrkjobList = function(result){
	if ( result ){
		result = JSON.parse(result);
		
		$('#wrkjob_combo').combobox('loadData', result);
	}
}

function columnHideAndShow(){
	try {
		let index = 0;
		
		for ( i = 0; i < indexCnt; i++ ){
			index = 100 + i;
			$('#tableList').datagrid('hideColumn', 'SQL'+ index +'ERR_YN');
		}
		
		if ( usingindxList ){
			for ( i = 0; i < usingindxList.length; i++ ){
				$('#tableList').datagrid('showColumn', 'SQL'+usingindxList[i].qty_chk_idt_cd+'ERR_YN');
			}
		}
		ajaxCall_NonStdSqlList();
		
	} catch(err){
		console.error(err);
	}
}

function ajaxCall_NonStdSqlList(){
	// modal progress open
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('표준 미준수 SQL 조회',' ');
	
	ajaxCall('/sqlStandards/loadNonStdSql',
			$('#submit_form'),
			'GET',
			callback_NonStdSqlList);
}

var callback_NonStdSqlList = function(result){
	try {
		let data = JSON.parse(result);
		
		$('#tableList').datagrid('loadData', data.rows);
		fnEnableDisablePagingBtn(data.dataCount4NextBtn);
		
	} catch(err){
		console.error(err);
	
	} finally {
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
		closePageLoadMessageProgress();
		
		$('#tableList').datagrid('loaded');
	}
}

function Excel_Download(){
	if( searchCompleteChecker() == false ){
		warningMessager('데이터 조회 후 엑셀 다운로드 바랍니다.');
		return;
	}
	
	let src = '/sqlStandardCheckResult/excelDownload';
	
	$('#submit_form').attr('action',src);
	$('#submit_form').submit();
	$('#submit_form').attr('action','');
	
	showDownloadProgress( src );
}

function formValidationCheck(){
	if ( isEmpty($('#project_combo').combobox('getValue')) ){
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	if( isEmpty($('#wrkjob_combo').combobox('getText')) ){
		warningMessager('업무를 선택해 주세요.');
		return false;
	}
	
	return true;
}

function searchCompleteChecker(){
	let value = '';
	
	value = $('#project_id').val();
	if ( isEmpty(value) || $('#project_combo').combobox('getValue') != value ){
		return false;
	}
	
	value = $('#wrkjob_cd').val();
	if ( (isEmpty(value) && $('#wrkjob_combo').combobox('getText') != '전체')
			|| $('#wrkjob_combo').combobox('getValue') != value ){
		
		return false;
	}
	
	if ( $('#user_nm_textbox').textbox('getValue') != $('#user_nm').val() ){
		return false;
	}
	
	if ( $('#dev_id_textbox').textbox('getValue') != $('#developer_id').val() ){
		return false;
	}
	
	if ( $('#dbio_textbox').textbox('getValue') != $('#dbio').val() ){
		return false;
	}
	
	value = $('#qty_chk_idt_cd_combo').combobox('getValue');
	value = ( isEmpty(value) ) ? '000' : value;
	if ( value != $('#qty_chk_idt_cd').val() ){
		return false;
	}
	
	return true;
}

function fnSearch(){
	ajaxCall_NonStdSqlList();
}