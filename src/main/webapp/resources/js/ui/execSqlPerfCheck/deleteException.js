var tab_index = 0;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#check_span1").show();
	$("#check_span2").hide();
	
	if($("call_from_child").val() == 'Y'){
		Btn_OnClick();
		createExceptionDeleteTab();
	}

	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});
	
	var clipboard2 = new Clipboard('#sqFormatterCopyBtn');
	clipboard2.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});	

	$('#search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onLoadSuccess: function() {
			$('#search_wrkjob_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
    
	$("#search_1").hide(); // SQL식별자(DBIO)
	$("#search_2").hide();  // 프로그램(PROGRAM_NM)
	$("#search_3").hide();  //프로그램 설명
	$("#search_4").hide();  //아직은 없음
	
	// 검색조건 프로그램
	$('#searchKey').combobox({
//		url:"/Common/getCommonCode?grp_cd_id=1052&isAll=Y",
		url:"/Common/getCommonCode?grp_cd_id=1067&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(val){
			if(val == 'S'){// SQL식별자(DBIO)
				$("#search_1").show(); // SQL식별자(DBIO)
				$("#search_2").hide();  // 프로그램(PROGRAM_NM)
				$("#search_3").hide();  // 프로그램 설명
				$("#search_program_nm").textbox("setValue","");
				$("#search_program_desc").textbox("setValue","");
				$("#search_trade").textbox("setValue","");
				
			}else if(val == 'P'){// 프로그램(PROGRAM_NM)
				$("#search_1").hide(); // SQL식별자(DBIO)
				$("#search_2").show();  // 프로그램(PROGRAM_NM)
				$("#search_3").hide();  // 프로그램 설명
				$("#search_dbio").textbox("setValue","");
				$("#search_program_desc").textbox("setValue","");
				$("#search_trade").textbox("setValue","");
				
			}else if(val =="Q"){
				$("#search_1").hide(); // SQL식별자(DBIO)
				$("#search_2").hide();  // 프로그램(PROGRAM_NM)
				$("#search_3").show();  // 프로그램 설명
				$("#search_4").hide();  //아직은 없음 추가해야함
				$("#search_dbio").textbox("setValue","");
				$("#search_program_nm").textbox("setValue","");
				$("#search_trade").textbox("setValue","");

			}else if(val == 'T'){//아직은 없음
				$("#search_1").hide(); // SQL식별자(DBIO)
				$("#search_2").hide();  // 프로그램(PROGRAM_NM)
				$("#search_3").hide(); // 프로그램 설명
				$("#search_dbio").textbox("setValue","");
				$("#search_trade").textbox("setValue","");
				$("#search_program_nm").textbox("setValue","");
				$("#search_program_desc").textbox("setValue","");
				
			}else{
				$("#search_1").hide(); // SQL식별자(DBIO)
				$("#search_2").hide();  // 프로그램(PROGRAM_NM)
				$("#search_3").hide();  // 프로그램 설명
				$("#search_4").hide();  //아직은 없음 추가해야함
				$("#search_program_nm").textbox("setValue","");
				$("#search_dbio").textbox("setValue","");
				$("#search_program_desc").textbox("setValue","");
				$("#search_trade").textbox("setValue","");
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	// 예외요청사유
	$('#exec_request_form #exception_request_why_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1060",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(val){
			$('#detail_form #exception_request_why_cd').val(val);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});

	// 예외처리방법
	$('#exec_request_form #exception_prc_meth_cd').combobox({
		url:"/Common/getCommonCodeRef2?grp_cd_id=1061",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(val){
			$('#detail_form #exception_prc_meth_cd').val(val);

		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});

//	// 예외요청 상세사유
	$('#exec_request_form #exception_del_request_why').textbox({  //easyui를 안쓰면 데이터가 안들어감
		onChange:function(val){
		$('#detail_form #exception_del_request_why').val(val);
		}
	});
	
	
	getProPerfExecReq();
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#nextBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	
	$('#detail_form #perf_check_meth_cd').combobox({
	    url:"/ExecPerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckMethCd",
	    method:"get",
		valueField:'perf_check_meth_cd',
	    textField:'perf_check_meth_cd_nm',
	    onChange:function(val){
	    	if(val == 1){ //범위
	    		$('#yn_decide_div_cd').combobox("setValue",'');
	    		$('#yn_decide_div_cd').combobox({disabled:true});
	    		$('#not_pass_pass').textbox({disabled:false});
	    		$('#pass_max_value').textbox({disabled:false}); //활성화
	    			    		
	    	}else{ //여부
	    		$('#pass_max_value').textbox("setValue",'');
	    		$('#pass_max_value').textbox({disabled:true});
	    		$('#not_pass_pass').textbox({disabled:true});
	    		$('#yn_decide_div_cd').combobox({disabled:false});  //활성화
	    	}
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	
	
	$('#search_indc_apply_yn').combobox({
		onSelect:function(val){
			$("#submit_form #indc_apply_yn").val(val.value);
		},
	});
	
	var d = $('#search_dbio');
	d.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	var p = $('#search_program_nm');
	p.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	
	$("#exec_request_form #btn_request").prop("disabled","true");
	$("#exec_request_form #btn_reset").prop("disabled","true");

	//클릭시 조건에다라 활성화 비활성화(tabs)
	$("#perf_exec_req_tab").tabs({
		plain:true,
		onSelect: function(title,index){
			tab_index = index;
			rows = $('#tableList').datagrid('getSelected');
		
			if(rows != null && index == 0){
				setDetailView(rows);
			}else if(rows != null && index == 1){
				checkValidRequest(rows); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
				setDetailView(rows);
			}else if(index == 2){
				createExceptionDeleteTab();
			}
		}		
	});
	
});

function selectChkBox(){
	if($(this).prop("checked")){
		$(this).parent().parent().addClass("chkRow");
//		var text = $(this).parent().parent().find("td").eq(3).text();
	}else{
		$(this).parent().parent().removeClass();
	}
}

function getProPerfExecReq(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {

				if(tab_index == 1){
					checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다. [예외 삭제 요청] 탭에서  validatiojn 체크
				}
				setDetailView(row);
//				getDeployPerfChkDetailResult();  //체크박스 테이블
			},		
			columns:[[
				{field:'program_nm',title:'프로그램명',halign:"center",align:'left'},
				{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left'},
				{field:'wrkjob_cd_nm',title:'업무',width:"6%",halign:"center",align:'center'},
				{field:'perf_check_auto_pass_yn',title:'영구검증제외여부',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_id',title:'예외요청ID',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_dt',title:'예외요청일시',width:"8%",halign:"center",align:'center'},
				{field:'exception_requester',title:'예외요청자',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_why_cd_nm',title:'예외요청사유',width:"10%",halign:"center",align:'center'},
				{field:'exception_request_why',title:'예외요청상세사유',width:"10%",halign:"center",align:'left'},
				{field:'exception_prc_dt',title:'예외처리일시',width:"8%",halign:"center",align:'center'},	
				{field:'except_processor',title:'예외처리자',width:"6%",halign:"center",align:'center'},
				{field:'exception_prc_meth_cd_nm',title:'예외처리방법',width:"6%",halign:"center",align:'center'},
				{field:'exception_prc_why',title:'예외처리결과',width:"10%",halign:"center",align:'left'},
				{field:'exception_del_request_dt',title:'예외삭제요청일시',width:"8%",halign:"center",align:'center'},	
				{field:'exception_del_request_why',title:'예외삭제요청상세사유',width:"8%",halign:"center",align:'left'},
				{field:'file_nm',title:'파일명',halign:"center",align:'left'},
				{field:'dir_nm',title:'디렉토리명',width:"20%",halign:"center",align:'left'},
				{field:'program_div_cd_nm',title:'프로그램구분',width:"6%",halign:"center",align:'center'},
				{field:'program_desc',title:'프로그램설명',width:"20%",halign:"center",align:'left'},
				{field:'program_id',title:'프로그램ID',width:"4%",halign:"center",align:'center'},
				{field:'exception_del_prc_why',hidden:true},	
				{field:'except_del_processor',hidden:true},	
				{field:'wrkjob_cd',hidden:true},
				{field:'last_exception_request_id',hidden:true},
				{field:'exception_request_why_cd',hidden:true},
				{field:'program_div_cd',hidden:true},
				{field:'exception_prc_status_cd', hidden:true},
				{field:'program_type_cd',hidden:true}, //예외요청ID
				{field:'program_type_cd_nm',hidden:true}, //예외요청ID
				{field:'sql_command_type_cd',hidden:true}, //SQL명령 유형코드
				{field:'dynamic_sql_yn',hidden:true},//다이나믹SQL여부
				{field:'exception_prc_meth_cd', hidden:true}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};


function checkValidRequest(row){
	
	if($("#submit_form #user_auth_id").val() != '4' && $("#submit_form #user_auth_id").val() != '5' && $("#submit_form #user_auth_id").val() != '6'){
//		alert("1");
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
		$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});

		$("#exec_request_form #btn_request").linkbutton({disabled:true});
		$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
		$("#exec_request_form #btn_reset").linkbutton({disabled:true});
		
	/*}else if($("#submit_form #user_auth_id").val() == '4' && $("#submit_form #user_id").val() != row.deploy_requester_id){
		alert("2/"+$("#submit_form #user_auth_id").val()+"/"+$("#submit_form #user_id").val()+"/"+row.deploy_requester_id);
		$("#exec_request_form #exception_request_detail_why").textbox({disabled:true});
		$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});

		$("#exec_request_form #btn_request").linkbutton({disabled:true});
		$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
		$("#exec_request_form #btn_reset").linkbutton({disabled:true});*/
		
	}else {

		if(row.exception_prc_status_cd == '1'){
//			alert("3");
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
			$("#exec_request_form #exception_del_request_why").textbox({readonly:true});
			
			$("#exec_request_form #btn_request").linkbutton({disabled:true});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:false});
			$("#exec_request_form #btn_reset").linkbutton({disabled:true});
		}else{
//			alert("4");
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
			$("#exec_request_form #exception_del_request_why").textbox({readonly:false});
			
			$("#exec_request_form #btn_request").linkbutton({disabled:false});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
			$("#exec_request_form #btn_reset").linkbutton({disabled:false});
		}
	}
};

function getPerfCheckResultDelTableList(){
	$("#perfCheckResultDelTableList").datagrid({
		view: myview,
		columns:[[
				{field:'perf_check_indc_nm',title:'검증지표',width:"30%",halign:"center",align:"center",sortable:"true"},
				{field:'pass_max_value',title:'기준지표(적합)',width:"22%",halign:"center",align:'center'},
				{field:'except_reg_yn',title:'예외등록여부',width:"22%",halign:"center",align:"center"},
				{field:'excpt_pass_max_value',title:'예외적용지표(적합)',width:"26%",halign:"center",align:'center'},
				{field:'dbio',hidden:true},
				{field:'perf_check_meth_cd_nm',hidden:true}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 삭제"," ");
	
	ajaxCall("/ExecPerformanceCheckIndex/PerfCheckResultDelTableList",
			$("#detail_form"),
			"POST",
			callback_getPerfCheckResultDelTableListAction);
	
};
var callback_getPerfCheckResultDelTableListAction = function(result) {
	
	let jsonResult = JSON.parse(result);
	
	jsonResult.rows.forEach(function(element){
		if(element.except_reg_yn === "y" || element.except_reg_yn === "Y"){
			element.except_reg_yn = "<span style='color:red'>"+element.except_reg_yn+"</span>";
		}
	});
	
	result = JSON.stringify(jsonResult);
	json_string_callback_common(result,'#perfCheckResultDelTableList',true);
};

var callback_getBindTableListAction = function(result) {
	json_string_callback_common(result,'#bindTableList',true);
};

/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#submit_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}
	Btn_OnClick('P');
}

function Btn_OnClick(val){
	if(!formValidationCheck()){
		return;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val('10');
	}
	fnSearch();
	Btn_ResetFieldAll();
}

function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 삭제"," ");
	
	
//	alert($("#detail_form #user_auth_id").val());
	//무조건 개발자는 
	if($("#submit_form #user_auth_id").val() == '4'){  //개발자라면
		$("#submit_form #search_user_id").val($("#submit_form #user_id").val());
	}
	
	ajaxCall("/ExecPerformanceCheckIndex/ProPerfExcReqDel",
			$("#submit_form"),
			"POST",
			callback_ProPerfExecReqAction);
}

//callback 함수
var callback_ProPerfExecReqAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);
};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
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

function formValidationCheck(){
	
	if($("#submit_form #search_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해주세요.');
		return false;
	}
	
	return true;
}
/*페이징처리끝*/


//예외요청
function Btn_Request(){
		
	if($("#exec_request_form #wrkjob_cd_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','예외요청할 데이터를 선택해 주세요.');
		return false;
	}
	if($("#exec_request_form #exception_request_why_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','예외 요청 사유를 선택해 주세요.');
		return false;
	}
	
	if($("#exec_request_form #exception_prc_meth_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','예외 처리 방법을 선택해 주세요.');
		return false;
	}
	
	if($("#exec_request_form #exception_del_request_why").val() == ""){
		parent.$.messager.alert('','예외 요청 상세 사유를 입력해 주세요.');
		return false;
	}
	if($("#exec_request_form #exception_del_request_why").val().length > 200){
		parent.$.messager.alert('','예외 요청 상세 사유를 200자 이내로 입력해 주세요.');
		return false;
	}

//	alert("예외요청상태코드 확인 : " +$("#detail_form #exception_prc_status_cd").val());
	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd",
			$("#detail_form"),
			"POST",
			callback_checkExceptionPrcStatusCdAction);	

	
};
//callback 함수
var callback_checkExceptionPrcStatusCdAction = function(result) {
	if(result.result){
		if(result.txtValue != null && result.txtValue != ''){
			parent.$.messager.alert('',result.txtValue);
	
		}else{
			rows = $('#tableList').datagrid('getSelected');
//			alert("예외요청상태코드 확인 : " +$("#detail_form #perf_check_auto_pass_yn").val() +"/"+$("#detail_form #exception_prc_meth_cd").val());
			
			if($("#detail_form #perf_check_auto_pass_yn").val() == 'Y' && $("#detail_form #exception_prc_meth_cd").val() == '1'){
				parent.$.messager.confirm('', '성능점검 점검제외가 삭제됩니다. <br/>점검제외 삭제를 요청하시겠습니까? ', function(check) {
				
					if (check) {
						ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/Request",
								$("#detail_form"),
								"POST",
								callback_RequestSettingAction);	
					}
				});
			}else{
				ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/Request",
						$("#detail_form"),
						"POST",
						callback_RequestSettingAction);	
			}
		}
		
	}else{
		parent.$.messager.alert('',result.message);
	}
};
//callback 함수
var callback_RequestSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				$('#search_chk').checkbox({checked:true});	
				Btn_OnClick();
//				Btn_ShowTab(3);		
				Btn_ResetFieldAll();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};
//요청취소
function Btn_Cancel(){
	
	if($("#exec_request_form #wrkjob_cd_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','예외요청할 데이터를 선택해 주세요.');
		return false;
	}
	
	rows = $('#tableList').datagrid('getSelected');
	if(rows.exception_prc_status_cd == '1'){
		parent.$.messager.confirm('', '요청을 취소하시겠습니까?', function(check) {
		
			if (check) {
				
				ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/Cancel",
						$("#detail_form"),
						"POST",
						callback_CancelSettingAction);	
			}
		});
	}else{
		parent.$.messager.alert('','예외처리상태가 요청중인 건에 대해서만 취소가 가능합니다.');
		return false;
	}
};
//callback 함수
var callback_CancelSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				$('#search_chk').checkbox({checked:false});	
				Btn_OnClick();
				Btn_ResetFieldAll();
//				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};
function createExceptionDeleteTab(){
	$("#detail_form #program_source_desc").val("");
	$("#detail_form #exception_del_request_why").val("");
//	console.log("ifram params:::"+$("#detail_form").serialize());
	if($('#tableList').datagrid('getSelections') && $('#tableList').datagrid('getSelections').length === 0){
		createExceptionDeleteTab_reload();
	}else{
		document.getElementById('exec_delete').src = "/ExecPerformanceCheckIndex/ExceptionDelete/CreateTab?"+$("#detail_form").serialize();
	}
};
function createExceptionDeleteTab_reload(){
	document.getElementById('exec_delete').src = "/ExecPerformanceCheckIndex/ExceptionDelete/CreateTab";
};


function setDetailView(selRow){

	if(selRow.exception_del_request_why != ''){
		$("#exec_request_form #exception_del_request_why").textbox("setValue", selRow.exception_del_request_why);
	}else{
		$("#exec_request_form #exception_del_request_why").textbox("setValue","");
	}
	
	if(selRow.exception_del_request_dt == null || selRow.exception_del_request_dt == ''){
		$("#exec_request_form #exception_requester").textbox("setValue", $("#submit_form #user_nm").val());
		$("#detail_form #exception_requester_id").val($("#submit_form #user_id").val());
	}else{
		$("#exec_request_form #exception_requester").textbox("setValue", selRow.except_processor);
		$("#detail_form #exception_requester_id").val(selRow.except_processor);
	}
	
	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue", selRow.wrkjob_cd_nm);
	$("#exec_request_form #program_nm").textbox("setValue", selRow.program_nm);
	$("#exec_request_form #dbio").textbox("setValue", selRow.dbio);
	$("#exec_request_form #exception_request_why_cd").combobox("setValue", 99); //무조건 예외 삭제

	//20210826 kwj 수정
//	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", 1); //무조건 지표단위
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", selRow.exception_prc_meth_cd); //무조건 지표단위
	$("#detail_form #exception_prc_meth_cd").val(selRow.exception_prc_meth_cd); 

	$("#exec_request_form #btn_request").attr("disabled","false");
	
	/*hidden*/
	$("#detail_form #wrkjob_cd").val(selRow.wrkjob_cd);
	$("#detail_form #program_div_cd").val(selRow.program_div_cd);
	$("#detail_form #exception_prc_status_cd").val(selRow.exception_prc_status_cd);
	$("#detail_form #program_id").val(selRow.program_id);
	$("#detail_form #excepter_id").val(selRow.excepter_id);
	$("#detail_form #exception_prc_dt").val(selRow.exception_prc_dt);
	$("#detail_form #exception_prc_status_cd_nm").val(selRow.exception_prc_status_cd_nm);
	$("#detail_form #exception_prc_why").val(selRow.exception_prc_why);
	$("#detail_form #exception_request_id").val(selRow.last_exception_request_id); 
	$("#detail_form #program_desc").html(selRow.program_desc);
	$("#detail_form #program_type_cd").val(selRow.program_type_cd);
	$("#detail_form #program_type_cd_nm").html(selRow.program_type_cd_nm);
	$("#detail_form #sql_command_type_cd").html(selRow.sql_command_type_cd);
	$("#detail_form #dynamic_sql_yn").html(selRow.dynamic_sql_yn);

	$("#detail_form #program_div_cd_nm").html(selRow.program_div_cd_nm);
	
	if(selRow.exception_prc_meth_cd === '2' || selRow.exception_prc_meth_cd === '3'){
		$("#detail_form #exception_prc_meth_cd_nm").html(selRow.exception_prc_meth_cd_nm);
	}else{
		$("#detail_form #exception_prc_meth_cd_nm").html("");
	}
	
	$("#detail_form #perf_check_auto_pass_yn").val(selRow.perf_check_auto_pass_yn);

	if(selRow.exception_prc_meth_cd){
		if(selRow.exception_prc_meth_cd==="2" || selRow.exception_prc_meth_cd==="3"){
			$("#perf_check_auto_pass_yn_chk").checkbox({checked:true});
		}else{
			$("#perf_check_auto_pass_yn_chk").checkbox({checked:false});
		}
	}

	$("#detail_form #program_nm").html(selRow.program_nm);
	$("#detail_form #dbio").html(selRow.dbio);
	$("#detail_form #file_nm").html(selRow.file_nm);
	$("#detail_form #dir_nm").html(selRow.dir_nm);
	$("#detail_form #deploy_nm").html(selRow.deploy_nm);
	$("#detail_form #deploy_id").html(selRow.deploy_id);
	$("#detail_form #deploy_check_status_cd_nm").html(selRow.deploy_check_status_cd_nm);
	$("#detail_form #deploy_expected_day").html(selRow.deploy_expected_day);
	$("#detail_form #reg_dt").html(selRow.reg_dt);
	$("#detail_form #last_update_dt").html(selRow.last_update_dt);
	
	$("#detail_form [name='program_nm']").val(selRow.program_nm);
	$("#detail_form [name='dbio']").val(selRow.dbio);
	
//    ,  A.DEPLOY_NM
//    ,  A.DEPLOY_ID
//    ,  CD5.CD_NM AS DEPLOY_CHECK_STATUS_CD_NM
//    ,  A.DEPLOY_EXPECTED_DAY
//    , TO_CHAR(A.REG_DT, 'YYYY-MM-DD HH24:MI:SS') AS REG_DT                  -- 등록일시
//    , TO_CHAR(A.LAST_UPDATE_DT, 'YYYY-MM-DD HH24:MI:SS') AS LAST_UPDATE_DT  -- 변경일시
	
	
	if(selRow.perf_check_auto_pass_yn == 'Y'){
		$("#check_span1").hide();
		$("#check_span2").show();
	}else{
		$("#check_span1").show();
		$("#check_span2").hide();
	}
	
	if(tab_index == 0){
		getProgramSourceDesc();  //프로그램 소스 탭 - SQL조회
	}else if(tab_index == 1){
		getPerfCheckResultDelTableList(); //예외삭제 요청 탭
	}else if(tab_index == 2){
		createExceptionDeleteTab();  //탭을 생성한다.
	}
	
}

function Btn_ResetField(){

	$("#exec_request_form #exception_request_why_cd").combobox("setValue", 99);
//	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", 1);
	$("#exec_request_form #exception_requester").textbox("setValue", $("#submit_form #user_nm").val());
	$("#exec_request_form #exception_del_request_why").textbox("setValue", "");
}

function Btn_ResetFieldAll(){
	
	$('#detail_form #deploy_nm').html('')
	$('#detail_form #deploy_id').html('')
	$('#detail_form #deploy_check_status_cd_nm').html('')
	$('#detail_form #deploy_expected_day').html('')
	
	$('#detail_form #program_nm').html('')
	$('#detail_form #program_div_cd_nm').html('')
	$('#detail_form #sql_command_type_cd').html('')
	$('#detail_form #program_type_cd_nm').html('')
	$("#detail_form #perf_check_auto_pass_yn_chk").checkbox('uncheck')
	$('#detail_form #exception_prc_meth_cd_nm').html('')
	$('#detail_form #program_nm').html('')
	
	$('#detail_form #program_desc').html('')
	$('#detail_form #dbio').html('')
	$('#detail_form #dynamic_sql_yn').html('')
	$('#detail_form #reg_dt').html('')
	$('#detail_form #last_update_dt').html('')
	
	$('#detail_form #file_nm').html('')
	$('#detail_form #dir_nm').html('')
	$("#detail_form #program_source_desc").val("")

	$("#exec_request_form #wrkjob_cd_nm").textbox('setValue','');
	$("#exec_request_form #program_nm").textbox('setValue','');
	$("#exec_request_form #dbio").textbox('setValue','');

	$("#exec_request_form #exception_request_why_cd").combobox("setValue", "");
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", "");
	$("#exec_request_form #exception_requester").textbox('setValue','');
	$("#exec_request_form #exception_del_request_why").textbox('setValue','');
	$('#perfCheckResultDelTableList').datagrid("loadData", []);

	createExceptionDeleteTab_reload();

}

function getProgramSourceDesc(){

	ajaxCall("/ExecPerformanceCheckIndex/programSourceDesc",
			$("#detail_form"),
			"POST",
			callback_getProgramSourceDescAction);
};
var callback_getProgramSourceDescAction = function(result) {
	if(result.result){
		$("#program_source_desc").val(result.txtValue);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Excel_Download(){
	if($("#submit_form #search_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해주세요.');
		return false;
	}

	$("#submit_form").attr("action","/ExecPerformanceCheckIndex/ProPerfExcReqDel/ExcelDown");
	$("#submit_form").submit();
}

function Btn_ShowTab(strIndex){
	$('#perf_exec_req_tab').tabs('select', strIndex);
}

function Btn_SetSQLFormatterEspc(){
	$('#program_source_desc').format({method: 'sql'});
}

function cellStyler(value,row,index){
	if(row.yn_decide_div_cd_nm == '적합'){
		return 'background-color:#1A9F55;';
	}else if(row.yn_decide_div_cd_nm == '부적합'){
		return 'background-color:#E41E2C;';
	}else if(row.yn_decide_div_cd_nm == '오류'){
		return 'background-color:#ED8C33;';
	}
}
function cellStyler2(value,row,index){
	if(row.perf_check_result_div_cd_nm == '적합'){
		return 'background-color:#1A9F55;';
	}else if(row.perf_check_result_div_cd_nm == '부적합'){
		return 'background-color:#E41E2C;';
	}else if(row.perf_check_result_div_cd_nm == '오류'){
		return 'background-color:#ED8C33;';
	}
}
