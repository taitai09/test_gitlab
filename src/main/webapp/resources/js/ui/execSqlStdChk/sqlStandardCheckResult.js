var timer;
var reloadLowerTable = true;
var isNotComplete = true;
var isPaging = false;
var indexCnt = 0;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createCombobox('#project_id');
	createlowerList();
	createQualityStatusList();
	createList();
	
	createRefreshBtn();
	
	Btn_OnClick();
});	//end of document ready

function createlowerList(){
	isNotComplete = true;
	
	ajaxCall_LoadAllIndex();
}

function ajaxCall_LoadAllIndex(){
	ajaxCall("/sqlStandards/loadAllIndex?exclusion=100",
			null,
			"GET",
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
				
				{field:'CURRENT_SQL_STD_GATHER_DAY',title:'금회차 작업일자',width:'150px',halign:'center',align:'center'},
				{field:'PREVIOUS_SQL_STD_GATHER_DAY',title:'전회차 작업일자',width:'150px',halign:'center',align:'center'},
				{field:'DB_NAME',title:'표준점검DB',width:'150px',halign:'center',align:'center'},
				{field:'GATHER_TERM',title:'수집기간',width:'150px',halign:'center',align:'center'}
			];
			
			if( indexList != null ){
				indexCnt = indexList.length;
				
				let position = 8;
				for(let i = 0; i < indexCnt; i++){
					columns.splice(position + i, 0,
							{field:'SQL'+indexList[i].qty_chk_idt_cd+'ERR_CNT',title:indexList[i].qty_chk_idt_nm, width:'150px',halign:'center',align:'right', hidden:true});
				}
			}
			
			$("#tableLowerList").datagrid({
				view: myview,
				singleSelect: true,
				checkOnSelect : false,
				selectOnCheck : false,
				columns: [columns],
				onSelect: function(index,row){
					let param = 'project_id='+ $('#project_id_lowList').val()
							  + '&sql_std_qty_scheduler_no='+ $('#scheduler_no_lowList').val()
							  + '&sql_std_gather_day='+$('#sql_std_gather_day').val()
							  + '&currentPage='+$('#currentPage').val();
					
					parent.moveToOtherTab(1, 'nonStandardSql', param );
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

function createQualityStatusList() {
	$("#tableLeftList").datagrid({
		view: myview,
		singleSelect: "true",
		selectOnCheck: "true",
		columns:[[
			{field:'DIAG_DAY', title:'점검일자', width:'30%', halign:'center', align:'center'},
			{field:'TOT_ERR_CNT', title:'미준수본수', width:'40%', halign:'center', align:'right'},
			{field:'STATUS', title:'현황', width:'30%', halign:'center', align:'center', formatter: formatBtn}
		]],
		onSelect:function(index, row) {
			$('#sql_std_gather_day').val(row.DIAG_DAY);
			loadLowerTable();
		},
		onClickCell:function(index,field,value) {
			if( field == 'STATUS' ){
				let diagDay = $(this).datagrid('getRows')[index].DIAG_DAY;
				let param = 'project_id='+ $('#project_id_lowList').val()
						  + '&sql_std_qty_scheduler_no='+ $('#scheduler_no_lowList').val()
						  + '&sql_std_gather_day='+diagDay;
				
				parent.moveToOtherTab(2, 'standardComplianceState', param);
			}
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		},
		onLoadSuccess:function(data){
			if( isPaging == false && reloadLowerTable == true && data.rows.length > 0 ){
				try{
					$(this).datagrid('selectRow', 0);
					
				}catch(e){
					console.log(e.message);
				}
			}
		}
	});
	
	function formatBtn(){
		return '<img src="/resources/images/status_graph.png" alt="Button for status" class="btnImg">';
	}
}

function createList() {
	$("#tableUpperList").datagrid({
		view: myview,
		singleSelect: "true",
		selectOnCheck: "true",
		columns:[[
			{field:'chk',halign:"center",width:'2%',align:"center",checkbox:true,rowspan:2},
			{field:'job_scheduler_nm',title:'스케줄러명',width:'18%',halign:'center',align:'left',rowspan:2},
			{field:'exec_status',title:'수행상태',width:'10%',halign:'center',align:'center',rowspan:2, styler:cellStyler},
			{field:'exec_result',title:'수행결과',width:'21%',halign:'center', colspan:3},
			{field:'exec_start_dt',title:'수행시작일시',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_end_dt',title:'수행종료일시',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_start_time',title:'수행시간',width:'7%',halign:'center',align:'right',rowspan:2},
			{field:'std_qty_target_db_name',title:'표준점검DB',width:'7%',halign:'center',align:'center',rowspan:2},
			{field:'project_nm',title:'프로젝트',width:'13%',halign:'center',align:'left',rowspan:2},
			
			{field:'sql_std_qty_chkt_id',hidden:true,rowspan:2},
			{field:'sql_std_qty_scheduler_no',hidden:true,rowspan:2},
			{field:'project_id',hidden:true,rowspan:2},
			],[
			{field:'sql_cnt',title:'전체',width:'7%',halign:'center',align:'right'},
			{field:'complete_cnt',title:'완료',width:'7%' ,halign:'center',align:'right'},
			{field:'in_progress_sql_cnt',title:'수행중',width:'7%',halign:'center',align:'right'},
		]],
		onCheck:function(index, row) {
			preventClick(index, row);
		},
		onSelect:function(index, row) {
			preventClick(index, row);
			
			$('#project_id_lowList').val(row.project_id);
			$('#scheduler_no_lowList').val(row.sql_std_qty_scheduler_no);
			$('#sql_std_gather_day').val(row.exec_start_dt.split(' ')[0]);
			
			if ( row.exec_status == '완료' ){
				resetPaging();
				loadIndexList();
				loadLeftTable(true);
				
			}else {
				loadLeftTable(false);
			}
		},
		onBeforeSelect:function(){
			$("#tableUpperList").datagrid('clearChecked');
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
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
	
	$('.datagrid-header-row > [field=chk] > div > input').remove();
	
	function preventClick( index,row ){
		if(row.exec_status != '수행중'){
			$("#tableUpperList").datagrid('uncheckRow', index);
		}
	}
	
	function resetPaging(){
		isPaging = false;
		$("#currentPage").val("1");
		$("#pagePerCount").val("20");
	}
}

function cellStyler( value,row,index ) {
	if ( value == '강제완료' ) {
		return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
	} else if ( row.exec_status == '완료'){
		return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
	} else if ( row.exec_status == '수행중'){ 
		return ' background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
	}else {
		return 'color:red; background-image:url(/resources/images/error.png);background-repeat: no-repeat;background-position-x: right;';
	}
}
/* 새로고침 */
function createRefreshBtn(){
	// 새로고침 슬라이드 버튼 생성
	$('#chkRefresh').switchbutton({
		value: "off",
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked ) {
					$('#chkRefresh').switchbutton('setValue','on');
					
					if ( $("#tableUpperList").datagrid('getRows').length < 1 ) {
						$('#chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					$("#timer_value").textbox({disabled:true});
					
					refreshList( checked );
				} else {
					$('#chkRefresh').switchbutton('setValue','off');
					$("#timer_value").textbox({disabled:false});
					
					setNumberBoxAttr();
					
					window.clearTimeout(timer);
				}
			},300);
		}
	});
	
	setNumberBoxAttr();
}

function refreshList( checked ) {
	let intSec = 0;
	reloadLowerTable = false;
	
	ajaxCall_UpperTableList(false);
	
	if ( checked ) {
		intSec = strParseInt( $("#timer_value").textbox("getValue"),0 );
		timer = window.setTimeout("refreshList(true)",(intSec*1000));
	} else {
		window.clearTimeout(timer);
	}
}
/* 검색 */
function Btn_OnClick(){
	reloadLowerTable = true;
	
	$('#tableUpperList').datagrid("loadData", []);
	$('#tableLeftList').datagrid("loadData", []);
	$("#tableLowerList").datagrid('loadData', []);
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$('#chkRefresh').switchbutton("uncheck");
	$('#chkRefresh').switchbutton('setValue','off');
	
	ajaxCall_UpperTableList(true);
}

function ajaxCall_UpperTableList(messageOpen){
	if( $('#chkRefresh').val() != 'on' && messageOpen){
		// modal progress open
		if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회'," ");
	}
	
	ajaxCall("/execSqlStdChk/loadSqlStdChkRslt",
			$("#submit_form_search"),
			"GET",
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

/* 좌측 테이블 데이터로드 */
function loadLeftTable(isComplete){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회'," ");
	
	$("#tableLeftList").datagrid('loadData', []);
	
	if(isComplete){
		ajaxCall_LoadLeftTable();
		
	}else {
		$("#tableLowerList").datagrid('loadData', []);
		
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function ajaxCall_LoadLeftTable(){
	ajaxCall("/execSqlStdChk/loadQualityStatus",
			$("#submit_form"),
			"GET",
			callback_LoadLeftTable);
}

var callback_LoadLeftTable = function(result){
	try {
		json_string_callback_common(result,'#tableLeftList',true);
		
		let data = JSON.parse(result);
		fnEnableDisablePagingBtn(data.dataCount4NextBtn);
		
		if( data.rows.length <= 0 ){
			$("#tableLowerList").datagrid('loadData', []);
		}
		
	} catch(err) {
		console.error(err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

/* 하단 테이블 - 품질 점검 지표 로드 */
function loadIndexList(){
	ajaxCall_LoadIndexList();
}

function ajaxCall_LoadIndexList(){
	ajaxCall("/execSqlStdChk/loadIndexList",
			$("#submit_form"),
			"GET",
			callback_LoadIndexList);
}

var callback_LoadIndexList = function(result){
	try {
		let flag = true;
		while(flag || isNotComplete){
			flag = $("#tableLowerList").datagrid('getColumnFields', false) > 0;
		}
		
		let index = 0;
		for(i = 0; i < indexCnt; i++){
			index = 101 + i;
			$('#tableLowerList').datagrid('hideColumn', 'SQL'+ index +'ERR_CNT');
		}
		
		if(result){
			let rows = JSON.parse(result).rows;
			for(i = 0; i< rows.length; i++){
				$('#tableLowerList').datagrid('showColumn', 'SQL'+rows[i].qty_chk_idt_cd+'ERR_CNT');
			}
		}
		
	} catch(err) {
		console.error(err.message);
	}
}

/* 하단 테이블 데이터로드 */
function loadLowerTable(){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회'," ");
	
	$("#tableLowerList").datagrid('loadData', []);
	
	ajaxCall_LoadLowerData();
}

/* 하단 테이블 - 점검 결과 로드 */
function ajaxCall_LoadLowerData(){
	ajaxCall("/execSqlStdChk/loadResultCnt",
			$("#submit_form"),
			"GET",
			callback_LoadLowerData);
}

var callback_LoadLowerData = function(result){
	try{
		json_string_callback_common(result,'#tableLowerList',true);
		
	}catch(e){
		console.log("LoadLowerData:"+e.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

/* 강제 완료 */
function Btn_ForcedCompletion() {
	let data = $("#tableUpperList").datagrid('getChecked');
	
	if( !data || data.length != 1 ){
		warningMessager('강제완료를 처리할 스케줄러를 선택 해 주세요.');
		return false;
		
	}else {
		let param = "확인";
		let msgStr ='진행 중인 작업이 중단됩니다. 강제완료를 처리하시겠습니까?';
		
		$('#sql_std_qty_scheduler_no').val(data.sql_std_qty_scheduler_no);
		
 		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("강제완료처리", "강제완료 처리 중입니다.");
				
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
			, sql_std_qty_div_cd : '4'
			, is_last : 'true'
	}]};
	
	ajaxCallWithJson('/ssopi/executeSql',
					submitMsg,
					"POST",
					callback_ForcedCompletion);
}

var callback_ForcedCompletion = function(result){
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result ) {
		infoMessager('강제완료 처리가 완료 되었습니다.');
		
	}else {
		errorMessager('강제완료처리에 실패 하였습니다.');
	}
	
	setTimeout(ajaxCall_UpperTableList(false), 200);
}

/* 엑셀 다운로드 */
function Excel_Download() {
	let src = "/execSqlStdChk/loadResultCnt/excelDownload";
	$("#submit_form").attr("action",src);
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
	showDownloadProgress( src );		// 대용량 엑셀 다운로드 로딩 바
}

function formValidationCheck(){
	// paging 시 필요. 현 페이지에서 체크할 부분 없어서 무조건 true로 return
	return true;
}
function fnSearch(){
	// 페이징대상 테이블그리드
	isPaging = true;
	loadLeftTable(true);
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