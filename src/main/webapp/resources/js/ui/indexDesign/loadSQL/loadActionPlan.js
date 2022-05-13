$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#loadActionPlan_form #selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$("#loadActionPlan_form #_easyui_combobox_i1_0").css("height","14px");
			$('#loadActionPlan_form #selectDbid').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#loadActionPlan_form #file_no").val("");
			$('#loadActionPlan_form #file_nm').textbox('setValue',"");
			$('#loadActionPlan_form #reg_dt').textbox('setValue',"");
			$('#loadActionPlan_form #query_load_cnt').textbox('setValue',"");
			$("#loadActionPlan_form #search_val").val("");
			$("#loadActionPlan_form #dbid").val(rec.dbid);
			
			$("#loadActionPlan_form #current_schema").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#loadActionPlan_form #current_schema').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			if(rec.dbid != ""){
				setFileNo(rec.dbid);
			}
		}
	});
	
	$('#loadActionPlan_form #selectDbid').combobox("setValue",$("#loadActionPlan_form #dbid").val());
	
	$("#loadActionPlan_form #tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			if(typeof(row.query_seq) == 'undefined') {
				return;
			}
			
			var note = strReplace(row.note, "\n", "<br/>");
			$('#loadActionPlan_form #unusual').html(note);
			$("#loadActionPlan_form #query_seq").val(row.query_seq);
			showSQLInfo();
		},			
		columns:[[
			{field:'query_seq',title:'SQL순번',width:"5%",halign:"center",align:"center",sortable:"true",fixed:"false"},
			{field:'sql_text',title:'SQL_TEXT',width:"40%",halign:"center",align:'left',fixed:"false"},
			{field:'plan_yn',title:'PLAN생성여부',width:"10%",halign:"center",align:'center',sortable:"true",fixed:"false"},
			{field:'note',title:'특이사항',width:"45%",halign:"center",align:'left',sortable:"true",fixed:"false"}
//			{field:'query_seq',title:'SQL순번',halign:"center",align:"center",sortable:"true",fixed:"false"},
//			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',fixed:"false"},
//			{field:'plan_yn',title:'PLAN생성여부',halign:"center",align:'center',sortable:"true",fixed:"false"},
//			{field:'note',title:'특이사항',halign:"center",align:'left',sortable:"true",fixed:"false"}
		]],
		fit:true,
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	var checkCase1 = false;
	$('#loadActionPlan_form #plan_creation_yn').switchbutton({
		checked: checkCase1,
		onText:"Yes",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				$("#loadActionPlan_form #plan_yn").val("Y"); 
			}else {
				$("#loadActionPlan_form #plan_yn").val("N");
			}

			var fileNo = $("#loadActionPlan_form #selectFileNo").combobox('getValue');
			var createSeq = $("#loadActionPlan_form #selectExplainExecSeq").combobox('getValue');
			if(fileNo != ""){
				if(createSeq != ""){
					setExplainExecInfo(createSeq);
				}else{
					parent.$.messager.alert('','먼저 생성순번을 선택해 주세요.');
					//setFileInfo_loadActionPlan(fileNo);
				}				
			}else{
				if($('#loadActionPlan_form #selectDbid').combobox('getValue') == "") {
					parent.$.messager.alert('','DB를 선택해 주세요.');
				} else {
					parent.$.messager.alert('','파일번호를 선택해 주세요.');
				}
			}
			
		}
	});
	
	var checkCase2 = false;
	$('#loadActionPlan_form #error_yn').switchbutton({
		checked: checkCase2,
		onText:"Yes",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				$("#loadActionPlan_form #note").val("Y");
				
			}else {
				$("#loadActionPlan_form #note").val("");
			}
			
			var fileNo = $("#loadActionPlan_form #selectFileNo").combobox('getValue');
			var createSeq = $("#loadActionPlan_form #selectExplainExecSeq").combobox('getValue');
			if(fileNo != ""){
				if(createSeq != ""){
					setExplainExecInfo(createSeq);
				}else{
					parent.$.messager.alert('','먼저 생성순번을 선택해 주세요.');
					//setFileInfo_loadActionPlan(fileNo);				
				}				
			}else{
				if($('#loadActionPlan_form #selectDbid').combobox('getValue') == "") {
					parent.$.messager.alert('','DB를 선택해 주세요.');
				} else {
					parent.$.messager.alert('','파일번호를 선택해 주세요.');
				}
			}
		}
	});
	
	setMarster_form_selector('#loadActionPlan_form');
});
//페이지 번호 증가 또는 감소
function fnSetCurrentPage(direction){
	var currentPage = $("#loadActionPlan_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#loadActionPlan_form #currentPage").val(currentPage);
	}else{
		$("#loadActionPlan_form #currentPage").val("1");
	}
}

//이전,다음페이지로 이동
function fnGoNextBeforePage(direction){
	fnSetCurrentPage(direction);
	
	var currentPage = $("#loadActionPlan_form #currentPage").val();
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#loadActionPlan_form #currentPage").val("1");
		return;
	}
	//검색
	searchActionPlan();
}

//function fnSearch(){
//	searchActionPlan();
//}

function formValidationCheck(){
	return true;
}

function searchActionPlan(){
	if(formValidationCheck_loadActionPlan()){
		
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#loadActionPlan_form #menu_id").val();
		
		$("#loadActionPlan_form #dbid").val($('#loadActionPlan_form #selectDbid').combobox('getValue'));
	
		$('#loadActionPlan_form #tableList').datagrid("loadData", []);
	
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("적제 SQL 실행 계획 생성 조회"," "); 	
		
		ajaxCall("/LoadActionPlanAction",
			$("#loadActionPlan_form"),
			"POST",
			callback_LoadActionPlanAddTable);
	}
}

//callback 함수
var callback_LoadActionPlanAddTable = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#loadActionPlan_form #tableList',true);
	
	setMarster_form_selector('#loadActionPlan_form');	//master_form 지정
	
	fnEnableDisablePagingBtn(dataLength);				//페이징 함수
}

function formValidationCheck_loadActionPlan(){
	
	if($('#loadActionPlan_form #selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if( ( $('#loadActionPlan_form #search_val').val() == "" || $('#loadActionPlan_form #search_val').val() == "1") && $('#loadActionPlan_form #file_no').val() == ""){
		parent.$.messager.alert('','파일 번호를 선택해 주세요.');
		return false;
	}	
	
	if($('#loadActionPlan_form #search_val').val() == "2" && $('#loadActionPlan_form #explain_exec_seq').val() == ""){
		parent.$.messager.alert('','생성 순번을 선택해 주세요.');
		return false;
	}	
	
	return true;
	
}

function Btn_OnClick_loadActionPlan(){
	if ( formValidationCheck_loadActionPlan() ) {
		$("#loadActionPlan_form #currentPage").val("1");
		$("#loadActionPlan_form #pagePerCount").val("20");
		
		$("#loadActionPlan_form #textArea li").remove();
		$("#loadActionPlan_form #completeYn").val("");
		
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#loadActionPlan_form #menu_id").val();
		
		$("#loadActionPlan_form #dbid").val($('#loadActionPlan_form #selectDbid').combobox('getValue'));
		
		var fileNo = $("#loadActionPlan_form #selectFileNo").combobox('getValue');
		var createSeq = $("#loadActionPlan_form #selectExplainExecSeq").combobox('getValue');
		if(fileNo != ""){
			if(createSeq != ""){
				setExplainExecInfo(createSeq);
			}else{
				setFileInfo_loadActionPlan(fileNo);
			}
		}
	}
}

function Btn_CreateActionPlan(){
	if(formValidationCheck_loadActionPlan()){
		if($('#loadActionPlan_form #current_schema').combobox('getValue') == ""){
			parent.$.messager.alert('','파싱스키마를 선택해 주세요.');
			return false;
		}
		
		isTaskLoadActionPlan(callback_isTaskLoadActionPlan);
	}
}

function isTaskLoadActionPlan(callback_function) {
	// 선행 작업 판단
	ajaxCall("/LoadActionPlan/isTaskLoadActionPlan",
			$("#loadActionPlan_form"),
			"POST",
			callback_function);
}

var callback_isTaskLoadActionPlan = function(result) {
	var data = JSON.parse(result);
	
	if(data.rows.length > 0 && data.rows[0].executing_cnt > 0) {
		var dbName = $('#loadActionPlan_form #selectDbid').combobox('getText');
		
		// 현재 db에서 실행계획을 생성중...
		parent.$.messager.alert('', 'DB ' + dbName + '에서 실행계획을 생성중인 작업이 있습니다. 작업이 종료된 후 다시 실행하세요');
		return false;
	}
	
	parent.$.messager.confirm('Confirm','실행계획을 생성 하시겠습니까?',function(r){
		if (r){
			ajaxCall("/LoadActionPlan/MaxExplainExecSeq",
					$("#loadActionPlan_form"),
					"POST",
					callback_MaxExplainExecSeqAction);	
		}
	});
}

var timerId;


//callback 함수
var callback_MaxExplainExecSeqAction = function(result) {
	if(result.result){
		$("#loadActionPlan_form #explain_exec_seq").val(result.txtValue);
		
		ajaxCall("/LoadActionPlan/Insert",
				$("#loadActionPlan_form"),
				"POST",
				null);
		
		timerId = setInterval("examinePlanExecCnt()", 5000);
		
		parent.openMessageProgressTypeB('실행계획생성', '', 0);
	}else{
		if(result.message == undefined){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.','error');
		}else{
			parent.$.messager.alert('',result.message,'error');
		}
	}
};

function examinePlanExecCnt(){
	ajaxCall("/LoadActionPlan/planExecCnt",
			$("#loadActionPlan_form"),
			"POST",
			callback_planExecCnt);
}

var callback_planExecCnt = function(result) {
	var data = result.object;
	var sql_cnt = Number(data.sql_cnt);
	var plan_exec_cnt = Number(data.plan_exec_cnt);
	var complete_yn = data.complete_yn;
	var time = new Date();
	console.log(time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds() + " sql_cnt:" + sql_cnt + ", plan_exec_cnt:" + plan_exec_cnt + ", complete_yn:" + complete_yn);
	if(complete_yn == "Y"){
		window.clearInterval(timerId);
		parent.closeMessageProgressTypeB('close');
		
		parent.$.messager.alert('실행계획생성 완료', '총 생성 건수 : ' + sql_cnt, 'info');
		
		timerId = null;
		
		// 선택된 파일번호로 재조회
		reloadFileNo();
		
		return;
	}
	
	var rest = 0;
	var percentage = 0;
	
	if(plan_exec_cnt > 0) {
		percentage = (plan_exec_cnt / sql_cnt) * 100;
	}
	
	var text = "생성 건수 : " + plan_exec_cnt;
	parent.updateMessageProgressTypeB(Math.round(percentage), text);
	console.log("end success percentage:" + percentage + "% /////////////////");
};

function reloadFileNo() {
	// 선택된 파일번호에 대하여 재조회를 진행
	var file_no = $("#loadActionPlan_form #selectFileNo").combobox("getValue");
	
	$("#loadActionPlan_form #search_val").val("1");
	$("#loadActionPlan_form #explain_exec_seq").val("");
	$('#loadActionPlan_form #explain_ret_dt').textbox('setValue',"");
	$('#loadActionPlan_form #explain_end_dt').textbox('setValue',"");
	$('#loadActionPlan_form #proc_cnt').textbox('setValue',"");

	setFileInfo_loadActionPlan(file_no);
	setExplainExecSeq(file_no);
	
}

/**
 * 파일번호 콤보박스 변경시
 * @param selDbid
 * @returns
 */
function setFileNo(selDbid){
	$("#loadActionPlan_form #dbid").val(selDbid);
	
	$("#loadActionPlan_form #selectFileNo").combobox({
		url:"/Common/getDBIOLoadFile?dbid="+selDbid,
		method:"get",
		valueField:'file_info',
		textField:'file_no',
		onSelect:function(rec1){
			$("#loadActionPlan_form #search_val").val("1");
			$("#loadActionPlan_form #explain_exec_seq").val("");
			$('#loadActionPlan_form #explain_ret_dt').textbox('setValue',"");
			$('#loadActionPlan_form #explain_end_dt').textbox('setValue',"");
			$('#loadActionPlan_form #proc_cnt').textbox('setValue',"");

			setFileInfo_loadActionPlan(rec1.file_info);
			setExplainExecSeq(rec1.file_no);
			
			$('#loadActionPlan_form #sqlInfo').html("");
			$('#loadActionPlan_form #unusual').html("");
		},
		onShowPanel: function() {
			setFileNo( selDbid );
			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
		onLoadSuccess: function() {
			$('#loadActionPlan_form #selectFileNo').combobox('textbox').attr("placeholder","선택");
		}
	});		
	
	$("#loadActionPlan_form #selectFileNo").combobox("setValue",$("#loadActionPlan_form #temp_file_no").val());
}

function setFileInfo_loadActionPlan(selValue){
	$('#loadActionPlan_form #file_no').val(selValue);
	
	ajaxCall("/Common/getDBIOLoadFileInfo?dbid="+$("#loadActionPlan_form #dbid").val()+"&file_no="+selValue,
			null,
			"GET",
			callback_getDBIOLoadFileInfoAction_loadActionPlan);	
}

//callback 함수
var callback_getDBIOLoadFileInfoAction_loadActionPlan = function(result) {
	if(result.result){
		var post = result.object;
		
		$('#loadActionPlan_form #file_nm').textbox('setValue',post.file_nm);
		$("#loadActionPlan_form #query_cnt").val(post.query_load_cnt);
		$('#loadActionPlan_form #query_load_cnt').textbox('setValue',post.query_load_cnt);
		$('#loadActionPlan_form #reg_dt').textbox('setValue',post.reg_dt);
		
		searchActionPlan();
	}else{
		if(result.message == undefined){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.','error');
		}else{
			parent.$.messager.alert('',result.message,'error');
		}
	}
}

function setExplainExecSeq(file_no){
	//생성순번
	$("#loadActionPlan_form #selectExplainExecSeq").combobox({
		url:"/Common/getDBIOExplainExec?file_no="+file_no,
		method:"get",
		valueField:'explain_info',
		textField:'explain_exec_seq',
		onSelect:function(rec2){
			$("#loadActionPlan_form #search_val").val("2");
			setExplainExecInfo(rec2.explain_info);
		},
		onLoadSuccess:function(data){
			if ( data.length > 0 ) {
				$("#loadActionPlan_form #search_val").val("2");
				var opts = $("#loadActionPlan_form #selectExplainExecSeq").combobox("options");
				$("#loadActionPlan_form #selectExplainExecSeq").combobox('setValue', data[0]);
			} else {
				$("#loadActionPlan_form #search_val").val("1");
			}
		}
	});
}

function setExplainExecInfo(selValue){
	$('#loadActionPlan_form #explain_exec_seq').val(selValue);
	
	ajaxCall("/Common/getDBIOExplainExecInfo?file_no="+$("#loadActionPlan_form #file_no").val()+"&explain_exec_seq="+selValue,
			null,
			"GET",
			callback_getDBIOExplainExecInfoAction);
}


//callback 함수
var callback_getDBIOExplainExecInfoAction = function(result) {
	if(result.result){	
		var post = result.object;
		var procCnt = post.plan_create_cnt + " / " + post.plan_error_cnt + " / " + post.plan_no_exec_cnt;
		$('#loadActionPlan_form #proc_cnt').textbox('setValue',procCnt);
		$('#loadActionPlan_form #explain_ret_dt').textbox('setValue',post.reg_dt);
		$('#loadActionPlan_form #explain_end_dt').textbox('setValue',post.exec_end_dt);
		
		searchActionPlan();
	}else{
		if(result.message == undefined){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.','error');
		}else{
			parent.$.messager.alert('',result.message,'error');
		}		
	}
}

function showSQLInfo(){
	$('#loadActionPlan_form #tabsDiv').tabs('select', 0);
	
	ajaxCall("/LoadActionPlan/ActionPlanSQLInfo",
			$("#loadActionPlan_form"),
			"POST",
			callback_ActionPlanSQLInfoAction);	
}

//callback 함수
var callback_ActionPlanSQLInfoAction = function(result) {
	var strHtml = "";
	if(typeof result == 'string'){
		result = result.trim();
		if(result.indexOf("<!DOCTYPE html>" != -1)){
			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);	
			});
			return;
		}
	}
	if(result.result){	
		var post = result.object;
		if(post != null){
			strHtml = strReplace(post.sql_text, "\n", "<br/>");
		}
		
		$('#loadActionPlan_form #sqlInfo').html(strHtml);
	}else{
		if(result.message == undefined){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.','error');
		}else{
			parent.$.messager.alert('',result.message,'error');
		}
	}
}
/**
 * 적재SQL 실행 계획 생성 엑셀 다운로드
 * @returns
 */
function Excel_Download_loadActionPlan(){	
	if(formValidationCheck_loadActionPlan()){
		$("#loadActionPlan_form #completeYn").val("");
		$("#loadActionPlan_form #dbid").val($('#loadActionPlan_form #selectDbid').combobox('getValue'));
		
		let src = "/loadActionPlan/ExcelDown";
		$("#loadActionPlan_form").attr("action", src);
		$("#loadActionPlan_form").submit();
		$("#loadActionPlan_form").attr("action","");
		
		showDownloadProgress( src );		// 대용량 엑셀 다운로드 로딩 바
	}	
}

function Btn_OnForceComplete(){
	if($('#loadActionPlan_form #selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	var dbName = $('#loadActionPlan_form #selectDbid').combobox('getText');
	
	parent.$.messager.confirm('', 'DB ' + dbName + '에서 미완료된 실행계획생성 작업을 전부 완료처리합니다', function(check) {
		if (check){
			ajaxCall("/LoadActionPlan/updateForceComplete",
					$("#loadActionPlan_form"),
					"POST",
					callback_forceComplete);
		}
	});
}

var callback_forceComplete = function(result) {
	parent.$.messager.alert('실행계획생성 강제완료처리', result.message, 'info');
}
