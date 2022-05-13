var paging = false;
$(document).ready(function(){
	$("body").css("visibility", "visible");
	
	$('[name=job_scheduler_type_cd]').val('37');
	
	createDbCombo();
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled").click(function(){
		fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();

	
	$("#scheduler_no_combo").combobox({
		panelHeight:'auto',
		onShowPanel : function(){
			createSchedulerCombo();
			$("body").click();
		}
	});

	createlowerList();
});

function setSchedulerStatus(){
	ajaxCall_GetSchedulerStatus();
}

function ajaxCall_GetSchedulerStatus(){
	ajaxCall("/SQLDiagnosisReport/selectSchedulerStatus",
			$('#detail_form'),
			"POST",
			callback_GetSchedulerStatus);
}
function callback_GetSchedulerStatus(result){
	
	let statusStr = '';
	let statusImgUrl ='';
	let sqlProgress = '';
	let sqlCnt =  '';
	let schedulerResult = ''
	let exec_status = ''
		
	try{
		let jobj = JSON.parse(result);
		
		exec_status = '미수행'
		statusImgUrl = '/resources/images/error.png'
		
		if(jobj[0]){
			
			exec_status = jobj[0].exec_status;
	
			statusImgUrl ='';
			sqlProgress =  jobj[0].in_progress_sql_cnt;
			sqlCnt =  jobj[0].sql_cnt;
			schedulerResult = sqlProgress + " / " + sqlCnt;
	
			if(exec_status){
				if(exec_status === '수행중'){			
					statusImgUrl = '/resources/images/performing.png'
				}else if(exec_status === '완료'){		
					statusImgUrl = '/resources/images/success.png'
				}else{ //강제완료 or 기타 사유
					statusImgUrl = '/resources/images/forceperformence.png'
				}
			}
		}
		
		$("#schedulerStaus").html(exec_status);
		$("#schedulerStausImg").attr('src',statusImgUrl)
		$("#schedulerResult").html(schedulerResult);
	}catch(e){
		clearData();
		return false;
	}
	
//	if(exec_status === '완료'){
		ajaxCallCollectionsetSchedulerSchedule();
		createLeftDataGrid();
		ajaxCallSchedulerRuleData();
//	}else{
//		clearData();
//	}
}

function clearData(){
	$('#leftTable').datagrid("loadData", []);
	$('#rightTable').datagrid("loadData", []);
	$('#diag_dt').html('');
	$('#gather_term').html('');
}

function createlowerList(){
	ajaxCall_LoadAllIndex();
}

function ajaxCall_LoadAllIndex(){
	ajaxCall("/SQLDiagnosisReport/loadIndexList",
			null,
			"GET",
			callback_LoadAllIndex);
}

var callback_LoadAllIndex = function(result){
	if ( result ) {
		let data = JSON.parse(result);
		let indexList = data.rows;
		
		let columns = [
			{field:'SQL_ID',title:'SQL ID',width:'130px',halign:'center',align:'left'}
		];
		
		let position = 8;
		indexList.forEach(
			function(idx){
				columns.splice(position, 0,
					{field:'SQL'+idx.qty_chk_idt_cd+'ERR_YN',title:idx.qty_chk_idt_nm, width:'130px',halign:'center',align:'center', hidden:true});
				position++;
			}
		);
		
		columns.push({field:'SQL_TEXT',title:'SQL TEXT',width:'200px',halign:'center',align:'left'})
		columns.push({field:'SQL_STD_QTY_CHKT_ID',hidden:true})
		columns.push({field:'SQL_STD_QTY_PROGRAM_SEQ',hidden:true})

		$("#rightTable").datagrid({
			view: myview,
			singleSelect: true,
			checkOnSelect : false,
			selectOnCheck : false,
			columns: [columns],
			onSelect: function(index,row){
				$("#sql_std_qty_program_seq").val(row.SQL_STD_QTY_PROGRAM_SEQ);
				$("#sql_std_qty_chkt_id").val(row.SQL_STD_QTY_CHKT_ID);
				ajaxCallSchedulerProgramSourceDESC()
			},onLoadError:function() {
				errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
			},onLoadSuccess: function(obj){
				paging = false;
				fnControlPaging(result);
			},onResizeColumn: function(result){
				if($("#rightSection .datagrid-header").css('height').replace('px','') < 60){
				}
			}
		});
		
	}else {
		errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
	}
}

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
	paging = true;
	
	let selectedRow = $('#leftTable').datagrid('getSelected');
	ajaxCall_LoadLowerData(selectedRow.QTY_CHK_IDT_CD);
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

function createDbCombo(){
	$('#db_name_combo').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
        panelHeight:'auto',
		onLoadSuccess: function() {
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
			if(call_from_parent === "Y"){
				console.log(parent_std_qty_target_dbid)
				$("#db_name_combo").combobox('setValue',parent_std_qty_target_dbid)
				createSchedulerCombo();
			}

		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(){
			$("#scheduler_no_combo").combobox('loadData', []);
			$("#scheduler_no_combo").combobox('setValue','');

		}
	});
}

function createSchedulerCombo(){
	
	if(!$("#db_name_combo").combobox('getValue')){
		return false;
	}
	
	$("#scheduler_no_combo").combobox({
		url:"/SQLDiagnosisReport/selectSqlDiagnosisReportSchedulerNameList?std_qty_target_dbid="+$("#db_name_combo").combobox('getValue'),
		method:"get",
		valueField:'sql_std_qty_scheduler_no',
		textField:'job_scheduler_nm',
		panelHeight:'auto',
		onLoadSuccess: function() {
			if(call_from_parent === "Y"){
				console.log(parent_sql_std_qty_scheduler_no)
				$("#scheduler_no_combo").combobox('setValue',parent_sql_std_qty_scheduler_no)
				Btn_OnClick()
				call_from_parent = 'N'
				parent_sql_std_qty_scheduler_no = ''
				parent_std_qty_target_dbid = ''

			}else{
				$(this).combobox('textbox').attr( 'placeholder' , '선택' );
				$(".textbox").removeClass("textbox-focused");
				$(".textbox-text").removeClass("tooltip-f");
				$(".textbox-text").removeClass("textbox-prompt");
			}

         },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
	});
}

function Btn_OnClick(){
	if($("#db_name_combo").combobox('getValue') === ''){
		parent.$.messager.alert('경고','품질진단DB를 선택해 주세요.','warning');
		return ; 
	}else if($("#scheduler_no_combo").combobox('getValue') === ''){
		parent.$.messager.alert('경고','스케줄러를 선택해 주세요.','warning');
		return ; 
	}
	$("#detail_form #currentPage").val('1')
	$("#std_qty_target_dbid").val($("#db_name_combo").combobox('getValue'));
	$("#sql_std_qty_scheduler_no").val($("#scheduler_no_combo").combobox('getValue'));
	
	clearData();
	setSchedulerStatus();
	
//	qty_chk_idt_cd :      
//	std_qty_target_dbid : 1600100018     
//	sql_std_qty_scheduler_no : 62     

	
}

function ajaxCall_LoadIndexList(){
	ajaxCall("/SQLDiagnosisReport/loadIndexList",
			$('#detail_form'),
			"GET",
			callback_LoadIndexList);
}

var callback_LoadIndexList = function(result){
	try {
		let data = JSON.parse(result);
		
		if(data){
			let rows = data.rows;
			for(i = 0; i< rows.length; i++){
				$('#rightTable').datagrid('showColumn', 'SQL'+rows[i].qty_chk_idt_cd+'ERR_YN');
			}
		}
	} catch(err) {
		console.error(err.message);
	}
}

function createResultArrJson(field , title , width , align){
	return	{field: field, title: title, width: width, halign:'center', align: align};
}

function isHidden(arr , qty_chk_idt_cd){
	if(arr.indexOf(qty_chk_idt_cd) === -1){
		return 'hidden:true';
	}
	return ''
}

function ajaxCallSchedulerProgramSourceDESC(){
	ajaxCall('/SQLDiagnosisReport/selectProgramSourceDesc',
			$('#detail_form'),
			"POST",
			showSQLTextPop);

}

function showSQLTextPop(result){
	
	let jObj = JSON.parse(result);
	
	$("#sqlText").textbox('setValue','');
	
	if(jObj && jObj.rows && jObj.rows[0]){
		$("#sqlText").textbox('setValue',jObj.rows[0].PROGRAM_SOURCE_DESC);
	}else{
	    $('#sqlDiagnosisReportPopup').window('setTitle','');            
		return false;
	}
	
    $('#sqlDiagnosisReportPopup').window('setTitle',jObj.rows[0].SQL_ID);

	$('#sqlDiagnosisReportPopup').window("open");
}

function ajaxCallSchedulerRuleData(){
	let url;
	let form;
	let callback = callback_SchedulerList;
	
	url = "/SQLDiagnosisReport/selectSqlDiagnosisRuleData";
	
	/* modal progress open */
	if (parent.openMessageProgress != undefined)
		parent.openMessageProgress("스케쥴러 조회", " ");
	
	ajaxCall(url,
			$('#detail_form'),
			"POST",
			callbackSchedulerRuleData);
}

function callbackSchedulerRuleData(result){
	try {
		json_string_callback_common(result, '#leftTable', true);
	} catch(err) {
		console.log(err.message);
	}
}

function ajaxCallCollectionsetSchedulerSchedule(){
	let url;
	let callback = callback_SchedulerList;
	
	url = "/SQLDiagnosisReport/selectSQLDiagnosisReportSchedulerSchedule";
	
	ajaxCall(url,
			$('#detail_form'),
			"POST",
			setSchedulerSchedule);
}


function setSchedulerSchedule(result){
	try{
		let jsonResult = JSON.parse(result)[0];
		if(!jsonResult){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
		$('#diag_dt').html(jsonResult.diag_dt);
		$('#gather_term').html(jsonResult.gather_term);
	}catch(e){
		parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
		return false;
	}
}


function cellStyler( value,row,index ) {
	
	if(row.QTY_CHK_IDT_CD === '000'){
		return 'font-weight : 700';
	}
	
	
}

/* 하단 테이블 데이터로드 */
function loadLowerTable(row){
	// modal progress open
	if(parent.openMessageProgress != undefined) parent.openMessageProgress('SQL 표준 점검 결과 조회'," ");
	
	$("#rightTable").datagrid('loadData', []);
	
	for(i = 101; i <= 123; i++){
		if(isNotEmpty($('#rightTable').datagrid("getColumnOption",'SQL'+i+'ERR_YN'))){
			$('#rightTable').datagrid('hideColumn', 'SQL'+ i +'ERR_YN');
		}
	}
	
	if(row){
		if(row){
			$('#detail_form #qty_chk_idt_cd').val(row.QTY_CHK_IDT_CD);
		}else{
			$('#detail_form #qty_chk_idt_cd').val('000');
		}
		
		ajaxCall_LoadIndexList();	// 사용 점검지표
		ajaxCall_LoadLowerData();	// 데이터 로드
		
	}else {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}
function ajaxCall_LoadLowerData(){
	ajaxCall("/SQLDiagnosisReport/selectSqlDiagnosisReportDetailInfo",
			$('#detail_form'),
			"POST",
			callback_LoadLowerData);
}

var callback_LoadLowerData = function(result){
	try{
		json_string_callback_common(result,'#rightTable',true);
		paging = false;
		fnControlPaging(result);

	}catch(e){
		console.log("LoadLowerData:"+e.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function createLeftDataGrid(){
	
	$("#leftTable").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'QTY_CHK_IDT_NM',width:'90%',halign:'center',align:'left' , styler:cellStyler},
			{field:'ERR_CNT',width:'13%',halign:'center',align:'right' , styler:cellStyler},
			{field:'QTY_CHK_IDT_CD',hidden:true},
		]],
		onSelect:function(index, row) {
			let selectedRow = $('#leftTable').datagrid('getSelected');
			$("#detail_form #currentPage").val('1')
			loadLowerTable(row);
		},
		onLoadSuccess:function(data){
			
			if(data.rows.length !== 0){
				$("#leftTable").datagrid('selectRow',0);
			}
			
			$("#excel_qty_chk_idt_cd").val('000');
			$("#excel_std_qty_target_dbid").val($("#db_name_combo").combobox('getValue'));
			$("#excel_sql_std_qty_scheduler_no").val($("#scheduler_no_combo").combobox('getValue'));
			
			let div = $("#leftTable").datagrid('getPanel').find('.datagrid-view2 .datagrid-body')
			if(div.prop('scrollHeight') > div.prop('clientHeight')){ //스크롤 여부 확인
				
				let firstCol = $("#leftSection .datagrid-view2 .datagrid-body tr td:first div");
				if(firstCol){
					let className = firstCol.attr('class').replace(' ','.');
					let width = $('.'+className).css('width')
					width = width.replace('px','');
					width = $('.'+className).css('width',(width*1 - 10) +'px')
				}
				
			}else{
				
				let secondCol = $("#leftSection .datagrid-view2 .datagrid-body tr td:eq(1) div");
				if(secondCol){
					let className = secondCol.attr('class').replace(' ','.');
					let width = $('.'+className).css('width')
					width = width.replace('px','')
					width = $('.'+className).css('width',(width*1 + 6) +'px')
				}
			}
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});

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

function Excel_Download() {

	if ( $('#excel_qty_chk_idt_cd').val() == '' ){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
	}
	
	$("#excel_submit_form").attr("action","/SQLDiagnosisReport/excelDownload");
	$("#excel_submit_form").submit();
	$("#excel_submit_form").attr("action","");
}

