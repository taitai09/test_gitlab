$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var clipboard1 = new Clipboard('#sqlCopyBtn1');
	clipboard1.on('success', function(e) {
		infoMessager('복사 되었습니다.');
	});
	
	var clipboard2 = new Clipboard('#sqlCopyBtn2');
	clipboard2.on('success', function(e) {
		infoMessager('복사 되었습니다.');
	});
	
	$("#aSearchBtn").on('click', function(){
		Btn_OnClick();	
	});
	
	initialChecking();
	createResultCodeCombo();
	createProgramTypeCombo();
	createSqlTypeCombo();

	// 수행회차
	$('#program_execute_tms').combobox('setValue','01');
	
	$("#perfChkCompleteBtn").on('click', function(){
		inspectionComplete();
	});
	
	$("#perfChkRsltNoti").on('click', function(){
		inspectResultNoti();
	});
	
	/* 성능검증관리 목록*/
	$("#perfChkMngListBtn").on('click', function(){
		$("#submit_form #call_from_parent").val("Y");
		$("#submit_form").attr("action","/PerformanceCheckMng");
		$("#submit_form").submit();		
	});	
	
	$('#searchValue').textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){
			Btn_OnClick();
		}
	});

	//이전, 다음 처리
	$("#prevBtnEnabled").on('click', function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	
	$("#nextBtnEnabled").on('click', function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	$("#perfChkResultTabs").tabs({
		plain: true,
		onSelect: function(title,index){
			fnSelectedTabIndex(index);
		}
	});
	
	fnSearch();
	
	$('#detailResultTable.clickCell tbody').on('click', 'tr',function(){
		popupOpen();
	});
});

function inspectResultNoti(){
	parent.$.messager.confirm('', "검증결과를 배포시스템에 통보하시겠습니까?", function(r){
		if (r){
			if(parent.openMessageProgress != undefined) parent.openMessageProgress("검증결과 통보"," ");
			var paramArry = "";
			paramArry += deploy_id + "^" + perf_check_id + "^" + perf_check_result_div_cd + "|";
			$("#submit_form #params").val(strRight(paramArry,1));
				
			ajaxCall("/perfInspectMng/inspectRsltNoti?pageName=inspectionResult",
					$("#submit_form"),
					"POST",
					callback_inspectResultNoti);
		}
	});
}

var callback_inspectResultNoti = function(data){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	try{
		if(data.result){
			parent.$.messager.alert('성능검증결과통보',data.message,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
				},1000);
			});
		}else{
			infoMessager(result.message);
			
		}
	}catch(e){
		perrorMessager("오류가 발생하였습니다.");
		
	}
}

/* 성능검증완료,성능 검증 완료*/
/* 데이터가 없어도 성능 검증 완료 가능 */
function inspectionComplete(){
	ajaxCall("/perfInspectMng/getPerfInspectResultCount",
			$("#submit_form"),
			"POST",
			callback_GetInspectCount);
}

var callback_GetInspectCount = function(result) {
	try{
		let data = JSON.parse(result);
		let testMissCnt = parseInt(data.rows[0].test_miss_cnt);
		let passCnt = parseInt(data.rows[0].pass_cnt);
		let failCnt = parseInt(data.rows[0].fail_cnt);
		let errCnt = parseInt(data.rows[0].err_cnt);
		let msg = "";
		
		if(testMissCnt > 0){
			msg = "미수행 프로그램이 존재하여 성능검증을 완료할 수 없습니다. 미수행프로그램 : "+testMissCnt+"건";
			infoMessager(msg);
			return;
			
		}else{
			if((failCnt + errCnt) > 0 ){
				msg = "부적합(오류) 프로그램이 존재합니다. 성능검증을 완료하시겠습니까?";
				
			}else{
				msg = "성능검증을 완료하시겠습니까?";
			}
			fnPerfCheckComplete(msg);
		}
	}catch(e){
		console.log(e.name + ': ' + e.message);
	}
};

/* 성능검증완료,성능 검증 완료*/
function fnPerfCheckComplete(msg){
	var dlg = parent.$.messager.confirm({
		title: 'Confirm',
		msg: msg,
		buttons:[{
			text: '확인',
			onClick: function(){
				dlg.dialog('destroy');
				
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 완료"," ");
				
				ajaxCall("/perfInspectMng/perfInspectComplete",
						$("#submit_form"),
						"POST",
						callback_PerformanceCheckComplete);
			}
		},{
			text: '취소',
			onClick: function(){
				dlg.dialog('destroy')
			}
		}]
	});
}

/* 성능검증완료,성능 검증 완료*/
var callback_PerformanceCheckComplete = function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				//window.location.reload();
			},1000)
		});
		
	}else{
		infoMessager(result.message);
	}
};

//검증결과코드 조회
function createResultCodeCombo(){
	$('#perf_check_result_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1055&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
		},
		onChange:function(newValue, oldValue){
		}
	});
}

//프로그램유형 조회
function createProgramTypeCombo(){
	$('#program_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1005&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
				$(this).combobox('setValue',1);
			}
		},
		onChange:function(newValue, oldValue){
		}
	});
}

//SQL명령 유형 조회
function createSqlTypeCombo(){
	$('#sql_command_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1068&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
		},
		onChange:function(newValue, oldValue){
		}
	});
}

//체크박스 초기값 세팅
function initialChecking(){
	$('#improper').checkbox({
		checked: false,
		onChange:function( checked ){
			$(this).checkbox('setValue', checked);
		},
	});

	$('#unperformed').checkbox({
		checked: false,
		onChange:function( checked ){
			$(this).checkbox('setValue', checked);
		},
	});
	
	$('#searchCount').combobox({
		onChange:function(newValue, oldValue){
			$("#submit_form  #pagePerCount").val(newValue);
		},
	});
	
}

function setDataToSearchForm(){
	$('#search_form [name="selectValue"]').val( $('#selectValue').combobox('getValue') );
	$('#search_form [name="searchValue"]').val( $('#searchValue').textbox('getValue') );
	$('#search_form [name="program_type_cd"]').val( $('#program_type_cd').combobox('getValue') );
	$('#search_form [name="sql_command_type_cd"]').val( $('#sql_command_type_cd').combobox('getValue') );
	$('#search_form [name="perf_check_result_div_cd"]').val( $('#perf_check_result_div_cd').combobox('getValue') );
	$('#search_form [name="perf_check_step_id"]').val( $('#perf_check_step_id').val() );
	$('#search_form [name="perf_check_id"]').val( $('#perf_check_id').val() );
	$('#search_form [name="unperformed"]').val( $('#submit_form [name="unperformed"]').val() );
	$('#search_form [name="improper"]').val( $('#submit_form [name="improper"]').val() );
	$('#search_form [name="currentPage"]').val( $('#submit_form #currentPage').val() );
	$('#search_form [name="pagePerCount"]').val( $('#submit_form #pagePerCount').val() );
}

function createParam( data ){
	let param = "";
	
	if( data ){
		let wrkjob_cd = $("#submit_form #wrkjob_cd").val();
		let top_wrkjob_cd = $("#submit_form  #top_wrkjob_cd").val();
		let perf_check_step_id = $("#submit_form  #perf_check_step_id").val();
		let currentPage = $("#submit_form  #currentPage").val();
		let auto_skip = $("#submit_form  #auto_skip").val();
		let dbid = $("#submit_form  #dbid").val();
		
		param += "wrkjob_cd=" + wrkjob_cd;
		param += "&top_wrkjob_cd=" + top_wrkjob_cd;
		param += "&program_id=" + data.program_id;
		param += "&perf_check_id=" + data.perf_check_id;
		param += "&perf_check_step_id=" + perf_check_step_id;
		param += "&program_execute_tms=" + data.program_execute_tms;
		param += "&hidden_program_execute_tms=" + data.program_execute_tms;
		param += "&perf_check_result_div_nm=" + encodeURIComponent(data.perf_check_result_div_nm);
		param += "&program_exec_dt=" + data.program_exec_dt;
		param += "&currentPage=" + currentPage;
		param += "&auto_skip=" + auto_skip;
		param += "&dbid=" + dbid;
		param += "&program_type_cd=" + data.program_type_cd;
		param += "&sql_command_type_cd=" + data.sql_command_type_cd;
	}
	
	return param;
}

function fnSelectedTabIndex(index){
	let data = $("#tableList").datagrid('getChecked');
	if(data.length < 1){
		if(index != undefined && index != 0){
			warningMessager('프로그램ID를 선택하여 주세요.');
			return;
		}
	}
	
	let param = createParam( data[0] );
	if(index == 1){
		createProgramInfoTab( param );
		
	}else if(index == 2){
		let program_type_cd = $("#hidden_program_type_cd").val();
		let perf_check_result_div_nm = $("#perf_check_result_div_nm").val();
		let sql_command_type_cd = $("#hidden_sql_command_type_cd").val();
		let perf_check_result_div_cd_nm = $("#submit_form #perf_check_result_div_nm").val();
		
		callPerfChkResultTab( data );
		
		if(program_type_cd == 1){//온라인
			$(".tr_perf_impr_guide").show();
			$(".tr_perf_check_result_table").show();
			
		}else{
			$(".tr_perf_impr_guide").hide();
			$(".tr_perf_check_result_table").hide();
		}
	}
}

function setInfoToForm( row ){
	$("#submit_form #program_exec_dt").val(row.program_exec_dt);
	$("#submit_form #program_id").val(row.program_id);
	$("#submit_form #hidden_program_execute_tms").val(row.program_execute_tms);
	$("#submit_form [name=program_execute_tms]").val(row.program_execute_tms);
	$("#submit_form #hidden_program_type_cd").val(row.program_type_cd);
	$("#submit_form #hidden_sql_command_type_cd").val(row.sql_command_type_cd);
	$("#submit_form #perf_check_result_div_nm").val(row.perf_check_result_div_nm);
	$("#submit_form #hidden_program_execute_tms").html(row.program_execute_tms);
	$("#submit_form #perf_check_id").val( row.perf_check_id );
	$("#submit_form #perf_check_step_id").val( row.perf_check_step_id );
	$("#submit_form #deploy_request_dt").val( row.deploy_request_dt );
	
	$("#search_form [name='onlyIncorrectYn']").val( (row.perf_check_result_div_nm == '부적합') );
	$("#search_form [name='program_id']").val(row.program_id);
}

//프로그램 정보 탭
function createProgramInfoTab( param ){
	let submitData = $("#submit_form").formSerialize();
	
	$.ajax({
		url: '/perfInspectMng/programInfoTab',
		type: 'POST',
		data: param,
		success: function(data){
			$("#submit_form #html").val(data);
			
			ajaxCall("/perfInspectMng/programInfoBodySelect",
					$("#submit_form"),
					"POST",
					callback_ExtractHtmlBody);
		},
		error: function(e){
			errorMessager('프로그램 정보 조회 중 오류가 발생하였습니다.');
		}
	});
}

var callback_ExtractHtmlBody = function(result){
		$('#programInfo').html(result.txtValue);
		
		let auto_skip = $("#auto_skip").val();
		let isAutoSkip = false;
		
		if(auto_skip == "Y"){
			isAutoSkip = true;
		}
		
		$('#perf_check_auto_pass_yn_chk').checkbox({
			checked: isAutoSkip
		});
	$("#submit_form #html").val("");
}

var v_perf_check_result_div_nm;
function callPerfChkResultTab( data ){
	v_perf_check_result_div_nm = data.perf_check_result_div_nm;

	if( $("#submit_form #program_id").val() == "" ){
		warningMessager('프로그램ID를 선택하여 주세요.');
		return;
	}
	if( $("#submit_form #perf_check_id").val() == "" ){
		warningMessager('성능검증ID가 없습니다.');
		return;
	}
	if( $("#submit_form #perf_check_step_id").val() == ""){
		warningMessager('성능검증단계ID가 없습니다.');
		return;
	}
	
	//상세 검증 결과
	ajaxCall("/perfInspectMng/selectDeployPerfChkDetailResult",
			$("#submit_form"),
			"POST",
			callback_DeployPerfChkDetailResultList);
	
	//개선가이드
	if(v_perf_check_result_div_nm != "오류"){
		ajaxCall("/perfInspectMng/selectImprovementGuide",
				$("#submit_form"),
				"POST",
				callback_PerfChkResultImproveGuide);
	}
	
	if(v_perf_check_result_div_nm == "오류"){
		$(".tr_perf_check_result_table").hide();
		$(".tr_perf_impr_guide").hide();
		
	}else if(v_perf_check_result_div_nm == "적합"){
		$(".tr_perf_check_result_table").show();
		$(".tr_perf_impr_guide").hide();
		
	}else{
		$(".tr_perf_check_result_table").show();
		$(".tr_perf_impr_guide").show();
	}
}

//상세 검증 결과
var callback_DeployPerfChkDetailResultList = function(result) {
	try{
		let data = JSON.parse(result);
		if(data.result != undefined && !data.result){
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
				html += "	<td class='average'><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.avg_exec_result_value+"'readonly></td>\n";
				html += "	<td class='maxVal'><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.max_exec_result_value+"'readonly></td>\n";
				html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+row.perf_check_result_div_nm+"'readonly></td>\n";
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exception_yn+"'readonly></td>\n";
				html += "	<td>"+nvl(row.perf_check_result_desc,'')+"</td>\n";
				html += "</tr>\n";
			});
			$("#detailResultTable tbody").html("");
			$("#detailResultTable tbody").html(html);
			
			if( data.rows && data.rows.length > 0 ){
				let perf_check_evaluation_meth_cd = data.rows[0].perf_check_evaluation_meth_cd;
				let selector;	//average, maxVal
				
				// 1 : 개별 / 2 : 평균 / 3 : 최대
				if( perf_check_evaluation_meth_cd == '2' ){
					selector = '.average';
					
				}else if( perf_check_evaluation_meth_cd == '3' ){
					selector = '.maxVal';
				}
				
				if( selector ){
					$(selector).css('background-color', '#00B0F0');
					$(selector + '> input').css('color', 'white').css('background', 'none');
					$('.clickCell tbody tr, .clickCell tbody tr input').css('cursor', 'pointer');
				}
			}
		}
		
	}catch(e){
		errorMessager(e.message);
	}
};

//callback 함수
var callback_PerfChkResultImproveGuide = function(result) {
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
		
		$("#perf_impr_guide_table tbody").html(html);
	}catch(e){
		errorMessager(e.message);
	}
};

function formValidationCheck(){
	return true;
}

function Btn_OnClick(){
	$("#submit_form #currentPage").val("1");	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	
	fnSearch();
	
	$("#perfChkResultTabs").tabs('select',0);
}

function fnSearch(){
	setDataToSearchForm();
	fnCreateDatagrid();
	
	// 수행회차
	$('#program_execute_tms').combobox('setValue','01');
	$('#submit_form #tableList').datagrid("loadData", []);
	
	parent.frameName = $("#menu_id").val();
	
	ajaxCallPerfCheckResultList();
	ajaxCallPerfCheckResultBasicInfo();
}

function ajaxCallPerfCheckResultList(){
	$("#tableList tbody tr").remove();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 결과"," "); 
	ajaxCall("/perfInspectMng/perfCheckResultList",
			$("#search_form"),
			"POST",
			callback_PerfCheckResultList);
}

var callback_PerfCheckResultList = function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	try{
		json_string_callback_common(result,'#tableList',true);
		
		let data = JSON.parse(result);
		let dataLength=0;
		dataLength = data.dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
		
	}catch(e){
		console.log("e.message:"+e.message);
	}

};

function ajaxCallPerfCheckResultBasicInfo(){
	ajaxCall("/perfInspectMng/getPerfCheckResultBasicInfo",
			$("#search_form"),
			"POST",
			callback_GetPerfCheckResultBasicInfo);
}

//callback 함수
var callback_GetPerfCheckResultBasicInfo = function(result) {
	try{
		let data = JSON.parse(result);
		let row = data.rows[0];
		let total_cnt = row.total_cnt;
		let pass_cnt = row.pass_cnt;
		let fail_cnt = row.fail_cnt;
		let test_miss_cnt = row.test_miss_cnt;
		let exception_cnt = row.exception_cnt;
		
		$(".perf_check_result_blue").val("전체 : "+total_cnt);
		$(".perf_check_result_green").val("적합 : "+pass_cnt);
		$(".perf_check_result_red").val("부적합 : "+fail_cnt);
		$(".perf_check_result_gray").val("미수행 : "+test_miss_cnt);
		$(".perf_check_result_navy").val( "검증제외 : "+exception_cnt);
		
		$('#perfChkCompleteBtn').hide();
		$('#perfChkRsltNoti').hide();
		
		if( $('#role').val() == 'ROLE_DEV' ){
			if(row.deploy_check_status_cd == '01' && row.perf_test_complete_yn != 'Y'){
				$('#perfChkCompleteBtn').show();
				
			}else if( row.deploy_check_status_cd == '02' ){
				if( row.last_check_result_anc_dt == null || row.last_check_result_anc_dt == '' ){
					$('#perfChkRsltNoti').show();
					
				}else if( row.check_result_anc_yn != 'Y' ){
					$('#perfChkRsltNoti').show();
				}
			}
		}
		
	}catch(e){
		console.log(e.name + ': ' + e.message);
	}
};

function Excel_Download(){
	$("#search_form").attr("action","/perfInspectMng/perfInspectResult/ExcelDown");
	$("#search_form").submit();
	$("#search_form").attr("action","");
}

function cellStyler1(value,row,index){
	if(row.perf_test_complete_yn == 'Y'){
		return 'background-color:#00FF00;';
		
	}else if(row.perf_test_complete_yn == 'N'){
		return 'background-color:#FFF000;';
	}	
}

function cellStyler2(value,row,index){
	if(row.perf_check_result_div_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(row.perf_check_result_div_nm == '수행중'){
		return 'background-color:#89BD4C;color:white;';
	}else if(row.perf_check_result_div_nm =='검증제외'){
		return 'background-color:#012753;color:white;';
	}
}

//예외요청 버튼을 보일것인가, 말것인가?
function formatBtn(value,row){
	let program_execute_tms = (row.program_execute_tms == null) ? '' : row.program_execute_tms;
	
	if(row.exception_button_enable_yn == 'Y'){
		if(auth_cd == "ROLE_DEV" && (row.perf_check_result_div_nm == "부적합" || row.perf_check_result_div_nm == "오류" || row.perf_check_result_div_nm == "미수행")){
			return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_ExceptionRequest(\""+row.perf_check_id+"\",\""+row.program_id+"\",\""+program_execute_tms+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 예외요청</a>";
			
		}else if(auth_cd == "ROLE_DEPLOYMANAGER" && (row.perf_check_result_div_nm == "부적합" || row.perf_check_result_div_nm == "오류"|| row.perf_check_result_div_nm == "미수행")){
			//개발자일경우에만 예외요청 버튼이 보인다.
			//배포성능관리자일경우에도예외요청 버튼이 보인다.2019-03-08
			return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_ExceptionRequest(\""+row.perf_check_id+"\",\""+row.program_id+"\",\""+program_execute_tms+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 예외요청</a>";
			
		}else{
			return "";
		}
	}else{
		if(row.exception_processing_yn == 'Y'){
			return "요청중";
			
		}else{
			return "";
		}
	}
}

//예외요청
function Btn_ExceptionRequest(perf_check_id, program_id, program_execute_tms){
	let menuParam = "/execSqlPerfCheck/requestException";
	menuParam += "?perf_check_id="+perf_check_id;
	menuParam += "&perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&last_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&program_id="+program_id;
	menuParam += "&wrkjob_cd="+$("#wrkjob_cd").val();
	menuParam += "&wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd_nm").val());
	menuParam += "&program_execute_tms="+program_execute_tms;
	
	menuParam += "&search_perf_check_id="+perf_check_id;
	menuParam += "&search_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&search_last_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&search_program_id="+program_id;
	menuParam += "&search_wrkjob_cd="+$("#wrkjob_cd").val();
	menuParam += "&search_wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd_nm").val());
	menuParam += "&search_program_execute_tms="+program_execute_tms;
	menuParam += "&deploy_request_dt="+$("#deploy_request_dt").val();
	menuParam += "&call_from_parent=Y";
	
	let tabId = 'execSqlPerfCheckTab';
	let tabName = '예외 요청';
	let menuName = '배포전 자동 성능 검증';
	let menuId = parseInt( $("#menu_id").val() ) - 1;
	
	parent.goToExceptionTab(String(menuId), tabId, tabName, menuName, menuParam);
}

/**인덱스 변경 */
function Btn_BindSearch(){
	$('#indexRequestPop').window("open");
	
	$("#indexRequest_form #dbid").val($('#submit_form #dbid').val());
	$('#indexRequest_form #db_name').textbox('setValue',$('#submit_form #db_name').val());
	
	let rows = $('#tableList').datagrid('getRows');
	if(rows.length > 0){
		let ndg = $('#indexsRequestList').datagrid();
		ndg.datagrid('loadData', $.extend(true,[],rows));
	}
}

function fnCreateDatagrid(){
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		fitColumn:true,
		columns:[[
			{field:'perf_check_result_div_nm',title:'검증결과',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
			{field:'program_dvlp_div_nm',title:'개발구분',halign:"center",align:'center',sortable:"true"},
			{field:'sql_command_type_cd',title:'SQL명령 유형',hidden:'true'},
			{field:'sql_command_type_nm',title:'SQL명령 유형',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_nm',title:'프로그램',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true",styler:cellStyler1},
			{field:'program_type_cd',title:'프로그램유형',hidden:'true'},
			{field:'program_type_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_exec_dt',title:'수행일시',width:'130px',halign:"center",align:'center',sortable:"true"},
			{field:'exception_processing_yn',title:'예외요청',width:'100px',align:'center',sortable:"true",formatter:formatBtn},
			{field:'perf_check_id',title:'성능검증ID',halign:"center",align:'center',sortable:"true"},
			{field:'program_id',title:'프로그램ID',halign:"center",align:'center',sortable:"true"},
			{field:'program_execute_tms',hidden:true},
			{field:'deploy_request_dt',hidden:true},
		]],
		onClickRow : function(index,row) {
			$("#submit_form #clickedRowIndex").val(index);
			
			$("#detailResultTable tbody").html("");
			$('#perf_impr_guide_table tbody').html("");
			
			// get the selected tab panel and its tab object
			let pp = $('#perfChkResultTabs').tabs('getSelected');
			let tab = pp.panel('options').tab;	// the corresponding tab object
			let selectedTabIdx = pp.panel('options').index;	// the corresponding tab object
			
			if(row.perf_check_result_div_nm == '검증제외'){
				$("#submit_form #auto_skip").val("Y");
				
				if(selectedTabIdx == 2){
					fnSelectedTabIndex(2);
				}else{
					$("#perfChkResultTabs").tabs('select',2);
				}
			}else{
				$("#submit_form #auto_skip").val("N");
				
				var sqlCommandTypeCd = row.sql_command_type_cd;
				if(sqlCommandTypeCd == "TRUNCATE"){
					$('#perfChkResultTabs').tabs('disableTab', 2);
					parent.$.messager.alert('','TRUNCATE구문은 성능검증을 수행할 수 없습니다. 예외처리하세요!','info',function(){
						setTimeout(function() {
							if(selectedTabIdx == 1){
								fnSelectedTabIndex(1);
								
							}else{
								$("#perfChkResultTabs").tabs('select',1);
							}
						},1000);
					});
					
				}else{
					//프로그램유형=배치
					if(row.program_type_cd == "2"){
						$('#perfChkResultTabs').tabs('enableTab', 2);
					}
					
					if(selectedTabIdx != 2){
						$("#perfChkResultTabs").tabs('select',2);
					}else{
						fnSelectedTabIndex(2);
					}
				}
			}
		},
		onSelect:function(index,row) {
			setInfoToForm(row);
		},
		onLoadSuccess:function(data){
			$(this).datagrid('getPanel').find('a.easyui-linkbutton').linkbutton();
			
			let clickedRowIndex = $("#submit_form #clickedRowIndex").val();
			if(clickedRowIndex){
				clickedRowIndex = 0;
			}
			
			$(this).datagrid('selectRow', clickedRowIndex);
				$(this).datagrid('resize');
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

var callback_GetPerfCheckResultCount = function(result) {
	try{
		let data = JSON.parse(result);
		
		let testMissCnt = parseInt(data.rows[0].test_miss_cnt);
		let passCnt = parseInt(data.rows[0].pass_cnt);
		let failCnt = parseInt(data.rows[0].fail_cnt);
		let errCnt = parseInt(data.rows[0].err_cnt);
		
		let msg = "";
		if(testMissCnt > 0){
			msg = "미수행 프로그램이 존재하여 성능검증을 완료할 수 없습니다. 미수행프로그램 : "+testMissCnt+"건";
			infoMessager(msg);
			return;
		}else{
			if((failCnt + errCnt) > 0 ){
				msg = "부적합(오류) 프로그램이 존재합니다. 성능검증을 완료하시겠습니까?";
			}else{
				msg = "성능검증을 완료하시겠습니까?";
			}
			fnPerfCheckComplete(msg);
		}
	}catch(e){
		console.log(e.name + ': ' + e.message);
	}
};

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