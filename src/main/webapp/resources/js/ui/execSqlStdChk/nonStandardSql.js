var searchComplete = false;
var isPaging = false;
var usingindxList;
var indexCnt = 0;
var emptyInxList = [{'qty_chk_idt_nm' : '전체', 'qty_chk_idt_cd' : '000'}];

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	if( indexListFromParent ){
		usingindxList = JSON.parse(indexListFromParent).rows;
	}
	
	createNonStdSqlList( allIndesList );
	createQualityStatusList();
	
	createProjectCombo();
	createSchedulerCombo();
	createIndexCombo();
});	//end of document ready

function createNonStdSqlList(result){
	try{
		if ( result ) {
			let data = JSON.parse(result);
			let indexList = data.rows;
			
			let columns = [
				{field:'PROJECT_NM',title:'프로젝트',width:'200px',halign:'center',align:'left'},
				{field:'WRKJOB_CD_NM',title:'업무',width:'100px',halign:'center',align:'left'},
				{field:'SQL_ID',title:'SQL ID',width:'150px',halign:'center',align:'left'},
				{field:'PROGRAM_NM',title:'Module',width:'100px',halign:'center',align:'left'},
				{field:'SQL_COMMAND_TYPE_CD',title:'SQL명령유형',width:'100px',halign:'center',align:'center'},
				{field:'DBIO',title:'SQL식별자(DBIO)',width:'350px',halign:'center',align:'left'},
				
				{field:'SQL_TEXT',title:'SQL Text',width:'150px',halign:'center',align:'center'},
				{field:'SQL_STD_GATHER_DAY',title:'작업일자',width:'100px',halign:'center',align:'center'},
				{field:'DB_NAME',title:'표준점검DB',width:'150px',halign:'center',align:'center'}
			];
			
			if( indexList != null ){
				indexCnt = indexList.length;
				
				let position = 6;
				let colWidth = '150px';
				
				for(let i = 0; i < indexCnt; i++){
					colWidth = byteLength( indexList[i].qty_chk_idt_nm ) * 6.5 + 'px';
					
					columns.splice(position + i, 0,
							{field:'SQL'+indexList[i].qty_chk_idt_cd+'ERR_YN', title:indexList[i].qty_chk_idt_nm, width: colWidth, halign:'center', align:'center', hidden:true});
				}
			}
			
			$("#tableRightList").datagrid({
				view: myview,
				fitColumns: true,
				columns: [columns],
				onClickRow: function(index,row){
					showSqlTextPopup(row.SQL_ID);
				},
				onLoadError: function() {
					errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
				},
			});
			
		}else {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	}catch(e){
		errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		
	}
}

function createProjectCombo(){
	$('#project_combo').combobox({
		url:'/sqlStandardOperationDesign/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onChange: function(newValue, oldValue){
			$('#project_id').val( newValue );
			
			loadIndexList();
			loadSchedulerCombo();
		},
		onLoadSuccess: function() {
			if( $('#call_from_parent').val() ){
				$(this).combobox('setValue', $('#project_id_inherited').val());
			}
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function createSchedulerCombo(){
	$('#scheduler_no').combobox({
		valueField:'sql_std_qty_scheduler_no',
		textField:'job_scheduler_nm',
		onSelect: function(record){
			if( $('#call_from_parent').val() && $('#scheduler_no_inherited').val() ){
				Btn_OnClick();
			}
		},
		onLoadSuccess: function() {
			let scheduler_no_inherited = $('#scheduler_no_inherited').val();
			
			if( $('#call_from_parent').val() && scheduler_no_inherited ){
				$(this).combobox('setValue', scheduler_no_inherited);
			}
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

function createIndexCombo(){
	$('#qty_chk_idt_cd_combo').combobox({
		valueField:'qty_chk_idt_cd',
		textField:'qty_chk_idt_nm',
		onChange: function(newValue, oldValue){
			$('#qty_chk_idt_cd').val( newValue );
		},
		onLoadSuccess: function() {
			$(this).combobox('setValue', '000');
		},
		onLoadError: function(){
			errorMessager('DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#qty_chk_idt_cd_combo').combobox('loadData', emptyInxList);
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
			$('#form_area [name=sql_std_gather_day]').val(row.DIAG_DAY);
			loadNonStdSql();
			
			$('#call_from_parent').val('');
		},
		onClickCell:function(index,field,value) {
			if( field == 'STATUS' ){
				let diagDay = $(this).datagrid('getRows')[index].DIAG_DAY;
				let param = 'project_id='+ $('#project_id_excel').val()
						  + '&sql_std_qty_scheduler_no='+ $('#scheduler_no_excel').val()
						  + '&sql_std_gather_day='+diagDay;
				
				parent.moveToOtherTab(2, 'standardComplianceState', param);
			}
		},
		onLoadSuccess:function(data){
			if( data.total > 0 ){
				let inherited = $("#gather_day_inherited").val();
				if( $('#call_from_parent').val() == 'true' && inherited != '' ){
					let rnum = filerByDate(inherited, data.rows);
					$(this).datagrid('selectRow', rnum);
					
				}else if( isPaging == false ){
					$(this).datagrid('selectRow', 0);
				}
			}
		},
		onLoadError:function() {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
	});
	
	function formatBtn(){
		return '<img src="/resources/images/status_graph.png" alt="Button for status" class="btnImg">';
	}
	
	function filerByDate(newValue, rows) {
		let num = 0;
		let element;
		
		for (i = 0; i < rows.length; i++) {
			element = rows[i];
			
			if ( element.DIAG_DAY == newValue ) {
				num = i;
				break;
			}
		}
		return num;
	}
}

function loadSchedulerCombo(){
	$('#scheduler_no').combobox('clear');
	$('#scheduler_no').combobox('loadData',[]);
	
	ajaxCall_loadSchedulerCombo();
}

function ajaxCall_loadSchedulerCombo(){
	ajaxCall('/execSqlStdChk/loadSchedulerCombo',
			$("#submit_form"), 
			'GET',
			callback_loadSchedulerCombo);
}

var callback_loadSchedulerCombo = function(result){
	try {
		let data = JSON.parse(result);
		$('#scheduler_no').combobox('loadData', data);
		
	}catch(err) {
		console.log(err.message);
	}
}
/* 검색 */
function Btn_OnClick(){
	searchComplete = false;
	isPaging = false;
	
	if( formValidationCheck() ){
		$('#tableLeftList').datagrid("loadData", []);
		$('#tableRightList').datagrid("loadData", []);
		
		$('#form_area [name=project_id]').val( $('#project_combo').combobox('getValue') );
		$('#form_area [name="sql_std_qty_scheduler_no"]').val( $('#scheduler_no').combobox('getValue') );
		
		let sqlId = $('#sql_id_textbox').textbox('getValue');
			sqlId = ( sqlId == '' ) ? '0' : sqlId;
		$('#form_area [name=sql_id]').val( sqlId );
		
		let indexCode = $('#qty_chk_idt_cd_combo').combobox('getValue');
			indexCode = ( indexCode == '' ) ? '000' : indexCode;
		$('#form_area [name=qty_chk_idt_cd]').val( indexCode );
		
		if( $('#call_from_parent').val() != 'true' ){
			$("#currentPage").val("1");
		}
		$("#pagePerCount").val("20");
		$("#currentPage2").val("1");
		$("#rcount").val("20");
		
		columnHideAndShow();
		
	}else {
		searchComplete = true;
	}
}
/* 품질 점검 지표 로드 */
function loadIndexList(){
	$('#qty_chk_idt_cd_combo').combobox('loadData', emptyInxList);
	
	ajaxCall_LoadIndexList();
}

function ajaxCall_LoadIndexList(){
	ajaxCall("/execSqlStdChk/loadIndexList",
			$("#submit_form"),
			"GET",
			callback_LoadIndexList);
}

var callback_LoadIndexList = function(result){
	loadIndexCombo(result);
}

function loadIndexCombo(result){
	if(result){
		usingindxList = JSON.parse(result).rows;
		
		let rows = JSON.parse(result).rows;
		rows.unshift({'qty_chk_idt_cd' : '000', 'qty_chk_idt_nm' : '전체'});
		
		$('#qty_chk_idt_cd_combo').combobox('loadData', rows);
	}
}

function columnHideAndShow(){
	try {
		let index = 0;
		for(i = 0; i < indexCnt; i++){
			index = 101 + i;
			$('#tableRightList').datagrid('hideColumn', 'SQL'+ index +'ERR_YN');
		}
		
		if( usingindxList ){
			for(i = 0; i < usingindxList.length; i++){
				$('#tableRightList').datagrid('showColumn', 'SQL'+usingindxList[i].qty_chk_idt_cd+'ERR_YN');
			}
		}
		
		ajaxCall_LoadLeftTable();
		
	} catch(err) {
		console.error(err.message);
	}
}

function ajaxCall_LoadLeftTable(){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('표준 미준수 SQL 조회'," ");
	
	ajaxCall("/execSqlStdChk/loadQualityStatus",
			$("#submit_form"),
			"GET",
			callback_LoadLeftTable);
}

var callback_LoadLeftTable = function(result){
	let closePopUp = false;
	try{
		let data = JSON.parse(result);
		
		$('#tableLeftList').datagrid("loadData", data.rows);
		fnEnableDisablePagingBtn(data.dataCount4NextBtn);
		
		closePopUp = data.rows.length <= 0 || isPaging;
		
	} catch(err) {
		console.error(err.message);
	
	}finally {
		if(closePopUp && parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function loadNonStdSql(){
	$("#currentPage2").val("1");
	
	getModulePerformanceTable();
}

function ajaxCall_LoadNonStdSql(){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('표준 미준수 SQL 조회'," ");
	
	$('#tableRightList').datagrid("loadData", []);
	
	ajaxCall("/execSqlStdChk/loadNonStdSql",
			$("#submit_form_excel"),
			"GET",
			callback_LoadNonStdSql);
}

var callback_LoadNonStdSql = function(result){
	try{
		let data = JSON.parse(result);
		
		$('#tableRightList').datagrid("loadData", data.rows);
		fnEnableDisablePagingBtn2(data.dataCount4NextBtn);
		
	} catch(err) {
		console.error(err.message);
		
	}finally {
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function Excel_Download(){
	let src = "/execSqlStdChk/loadResultCnt/excelDownload";
	
	$("#submit_form_excel").attr("action",src);
	$("#submit_form_excel").submit();
	$("#submit_form_excel").attr("action","");
	
	showDownloadProgress( src );
}

function formValidationCheck(){
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	if ( $('#scheduler_no').combobox('getValue') == '' ) {
		warningMessager('스케줄러를 선택해 주세요.');
		return false;
	}
	return true;
}

function fnSearch(){
	isPaging = true;
	
	ajaxCall_LoadLeftTable();
}

function getModulePerformanceTable(){
	$('#currentPage_excel').val( $('#currentPage2').val() );
	
	ajaxCall_LoadNonStdSql();
}

function showSqlTextPopup(sql_id){
	$('#sql_id_popup').val( sql_id );
	$("#sqlText").textbox('setValue','');
	
	ajaxCall_loadSqlFullText();
}

function ajaxCall_loadSqlFullText(){
	ajaxCall('/execSqlStdChk/loadSqlFullText',
			$('#submit_form_popup'),
			"GET",
			callback_loadSqlFullText);
}

function callback_loadSqlFullText(result){
	if(result){
		let data = JSON.parse(result);
		
		if( data.rows.length > 0 ){
			data = data.rows[0];
			
			$("#sqlText").textbox('setValue',data.PROGRAM_SOURCE_DESC);
			$('#sqlDiagnosisReportPopup').window('setTitle',data.SQL_ID);
			
		}else{
			$('#sqlDiagnosisReportPopup').window('setTitle','');
			return false;
		}
	}
	$('#sqlDiagnosisReportPopup').window("open");
}