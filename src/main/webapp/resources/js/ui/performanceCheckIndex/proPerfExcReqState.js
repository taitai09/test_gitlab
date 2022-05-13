tab_index = 0;
isChecked = true;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});
	
	$('#search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
//		url:"/Common/getWrkJobCd",
		method:'get',
		valueField:'id',
		textField:'text',
		onSelect:function(rec){
			
			//sql runner 를 실행해주기위함.			
			//dbid = rec.dbid;
		},
		onChange:function(newval,oldval){
			$('#search_perf_check_step_id').combobox({
				url:"/PerformanceCheckIndex/getPerfCheckStep?search_wrkjob_cd="+newval,
				method:"get",
				valueField:'perf_check_step_id',
				textField:'perf_check_step_nm',
			});
		},
		onLoadSuccess: function(rec) {
			$('#search_wrkjob_cd').combobox("textbox").attr("placeholder","선택");
			
		 	$('#search_perf_check_step_id').combobox({
				url:"/PerformanceCheckIndex/getPerfCheckStep?search_wrkjob_cd="+$('#search_wrkjob_cd').combotree("getValue"),
				method:"get",
				valueField:'perf_check_step_id',
				textField:'perf_check_step_nm',
			});
		}	
	});
	
	$('#search_chk').attr("checked","true");
	$('#search_chk').on("click", function(){
		isChecked = $(this).prop("checked");
		if(isChecked){
			$("#submit_form #search_from_exception_reqeust_dt").textbox({disabled:false});
			$("#submit_form #search_to_exception_reqeust_dt").textbox({disabled:false});
		}else{
			$("#submit_form #search_from_exception_reqeust_dt").textbox({disabled:true});
			$("#submit_form #search_to_exception_reqeust_dt").textbox({disabled:true});
		}
		
	});
//	$('#search_chk').checkbox({
//		checkbox : true,
//		onCheck:function(node,checked){
//			alert("ddd");
//		}
//	})
	
	
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
	
    // 예외요청처리상태
	$('#search_exception_prc_status_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1051&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
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
		url:"/Common/getCommonCode?grp_cd_id=1061",
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
	$('#exec_request_form #exception_request_detail_why').textbox({  //easyui를 안쓰면 데이터가 안들어감
		onChange:function(val){
		$('#detail_form #exception_request_detail_why').val(val);
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
	    url:"/PerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckMethCd",
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
	var s = $('#search_deploy_requester');
	s.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	var q = $('#search_deploy_id');
	q.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	var qq = $('#search_program_desc');
	qq.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	
	$("#deploy_id").textbox("readonly", "true");
	$("#deploy_nm").textbox("readonly", "true");
	$("#deploy_check_status_cd_nm").textbox("readonly", "true");
	$("#deploy_expected_day").textbox("readonly", "true");
	$("#program_id").textbox("readonly", "true");
	$("#program_nm").textbox("readonly", "true");
	$("#dbio").textbox("readonly", "true");
	$("#file_nm").textbox("readonly", "true");
	$("#dir_nm").textbox("readonly", "true");
	$("#program_type_cd_nm").textbox("readonly", "true");
	$("#sql_command_type_cd").textbox("readonly","true");
	$("#dynamic_sql_yn").textbox("readonly","true");
	$("#exec_request_form #btn_request").prop("disabled","true");
	$("#exec_request_form #btn_reset").prop("disabled","true");

	//클릭시 조건에다라 활성화 비활성화
	$("#perf_exec_req_tab").tabs({
		plain:true,
		onSelect: function(title,index){
			tab_index = index;
			
			rows = $('#tableList').datagrid('getSelected');
			if(rows != null && index == 0){
				setDetailView(rows);
			}else if(rows != null && index == 1){
				setDetailView(rows);
			}else if(rows != null && index == 2 ){
				setDetailView(rows);
			}else if(index == 3){
				createExceptionStateTab();
			}
		}		
	});

	var call_from_parent = $("#call_from_parent").val();
	var call_from_child = $("#call_from_child").val();
	if (call_from_parent == "Y" || call_from_child == "Y") {
		$('#search_wrkjob_cd').combotree('setValue',{
			id: search_wrkjob_cd,
			text: search_wrkjob_cd_nm
		});

		$('#search_perf_check_step_id').combobox('setValue',$("#perf_check_step_id").val());

		Btn_OnClick();
	}
	
//	$('#exec_request_form #wrkjob_cd_nm').textbox({color:'red'});

	
	
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
				setDetailView(row);
			},		
			columns:[[
				{field:'exception_prc_meth_cd_nm',title:'예외처리방법',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_why_cd_nm',title:'예외처리사유',width:"6%",halign:"center",align:'left'},
				{field:'exception_prc_status_cd_nm',title:'예외처리상태',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_dt',title:'예외요청일시',width:"8%",halign:"center",align:'center'},
				{field:'exception_requester',title:'예외요청자',width:"6%",halign:"center",align:'center'},
				{field:'exception_request_why',title:'예외요청상세사유',width:"20%",halign:"center",align:'left'},
				{field:'exception_prc_dt',title:'예외처리일시',width:"8%",halign:"center",align:'center'},	
				{field:'except_processor',title:'예외처리자',width:"6%",halign:"center",align:'center'},
				{field:'exception_prc_why',title:'예외처리결과',width:"12%",halign:"center",align:'left'},
				{field:'deploy_id',title:'배포ID',width:"6%",halign:"center",align:'center'},
				{field:'deploy_nm',title:'배포명',width:'300px',halign:"center",align:'left'},
				{field:'program_nm',title:'프로그램명',halign:"center",align:'left'},
				{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left'},
				{field:'wrkjob_cd_nm',title:'업무',width:"6%",halign:"center",align:'center'},
				{field:'deploy_check_status_cd_nm',title:'성능점검상태',width:"6%",halign:"center",align:'center'},
				{field:'perf_check_auto_pass_yn',title:'영구점검제외여부',width:"6%",halign:"center",align:'center'},
				{field:'perf_check_result_div_cd_nm',title:'성능점검결과',width:"6%",halign:"center",align:'center',styler:cellStyler},
				{field:'perf_check_auto_pass_del_yn',title:'점검제외삭제여부',width:"8%",halign:"center",align:'center'},
				{field:'deploy_request_dt',title:'배포요청일시',width:"8%",halign:"center",align:'center'},
//				{field:'deploy_expected_day',title:'배포예정일자',width:"6%",halign:"center",align:'center', formatter:getDateFormat},
				{field:'deploy_requester',title:'배포요청자',width:"6%",halign:"center",align:'center'},
				{field:'file_nm',title:'파일명',halign:"center",align:'left'},
				{field:'dir_nm',title:'디렉토리명',width:"20%",halign:"center",align:'left'},
				{field:'program_desc',title:'프로그램설명',width:"20%",halign:"center",align:'left'},
				{field:'perf_check_id',title:'성능점검ID',width:"4%",halign:"center",align:'center'},
				{field:'program_id',title:'프로그램ID',width:"4%",halign:"center",align:'center'},

				{field:'wrkjob_cd',hidden:true},
				{field:'exception_request_id', hidden:true},
				{field:'exception_request_why_cd', hidden:true},
				{field:'exception_prc_status_cd', hidden:true},
				{field:'exception_prc_meth_cd', hidden:true},
				{field:'excepter_id', hidden:true},
				{field:'exception_request_grp_id', hidden:true},
				{field:'perf_check_step_id', hidden:true},
				{field:'program_execute_tms', hidden:true},
				{field:'exception_requester_id', hidden:true},
				{field:'program_type_cd',hidden:true}, //예외요청ID
				{field:'program_type_cd_nm',hidden:true}, //예외요청ID
				{field:'sql_command_type_cd',hidden:true}, //SQL명령 유형코드
				{field:'dynamic_sql_yn',hidden:true},//다이나믹SQL여부
				{field:'program_div_cd', hidden:true}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};

function getPerfCheckResultTableList(){
	
	$("#perfCheckResultTableList").datagrid({
		view: myview,
		columns:[[
			{field:'perf_check_indc_nm',title:'점검 지표',width:"16%",halign:"center",align:'center'},
			{field:'indc_pass_max_value',title:'적합',width:"8%",halign:"center",align:'center'},
			{field:'not_pass_pass',title:'부적합',width:"8%",halign:"center",align:'center'},
//			{field:'indc_yn_decide_div_cd_nm',title:'여부값 판정구분',width:"14%",halign:"center",align:'center'},
			{field:'exec_result_value',title:'성능 점검 결과값',width:"8%",halign:"center",align:'center'},
			{field:'perf_check_result_div_cd_nm',title:'성능 점검 결과',width:"8%",halign:"center",align:'center', styler:cellStyler2},
			{field:'except_reg_yn',title:'예외등록여부',width:"8%",halign:"center",align:'center'},
			{field:'perf_check_result_desc',title:'성능점검 결과내용',width:"45%",halign:"center",align:'left'},
			{field:'perf_check_indc_id',hidden:true},
			{field:'indc_yn_decide_div_cd',hidden:true},
			{field:'perf_check_result_div_cd',hidden:true},
			{field:'perf_check_meth_cd',hidden:true}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 결과"," ");
	
	ajaxCall("/PerformanceCheckIndex/PerfCheckResultTableList",
			$("#detail_form"),
			"POST",
			callback_getPerfCheckResultTableListAction);
	
};

var callback_getPerfCheckResultTableListAction = function(result) {
	json_string_callback_common(result,'#perfCheckResultTableList',true);
};

function getBindTableList(){
	
	$("#bindTableList").datagrid({
		view: myview,
		columns:[[
			{field:'bind_seq',title:'BIND_SEQ',width:"20%",halign:"center",align:'center'},
			{field:'bind_var_nm',title:'BIND_VAR_NM',width:"27%",halign:"center",align:'center'},
			{field:'bind_var_value',title:'BIND_VAR_VALUE',width:"27%",halign:"center",align:'center'},
			{field:'bind_var_type',title:'BIND_VAR_TYPE',width:"20%",halign:"center",align:'center'}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Bind List"," ");
	
	ajaxCall("/PerformanceCheckIndex/BindTableList",
			$("#detail_form"),
			"POST",
			callback_getBindTableListAction);
	
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
}

//예외처리요청상태값 변경
function fnChangeValue(val){
	$("#search_exception_prc_status_cd").combobox('setValue',val);
	return true;
}

function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 예외 처리 이력"," ");
	
	
//	alert($("#detail_form #user_auth_id").val());
	//무조건 개발자는 
	if($("#submit_form #user_auth_id").val() == '4'){  //개발자라면
		$("#submit_form #search_user_id").val($("#submit_form #user_id").val());
	}
	
	ajaxCall("/PerformanceCheckIndex/ProPerfExcReqState",
			$("#submit_form"),
			"POST",
			callback_ProPerfExecReqAction);
}

//callback 함수
var callback_ProPerfExecReqAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);  //페이버튼세팅
//	$("#detail_form #dbid").val(dbid);	//sql runner 를 실행하기위해 dbid 값을 세팅해줌.

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
	
	if($("#submit_form #search_from_exception_reqeust_dt").combobox('getValue') == '' && $("#submit_form #search_to_exception_reqeust_dt").combobox('getValue') != '' ){
		parent.$.messager.alert('','배포요청일자를 정확히 입력해 주세요.');
		return false;
	}
	if($("#submit_form #search_from_exception_reqeust_dt").combobox('getValue') != '' && $("#submit_form #search_to_exception_reqeust_dt").combobox('getValue') == '' ){
		parent.$.messager.alert('','배포요청일자를 정확히 입력해 주세요.');
		return false;
	}
	
	if($("#submit_form #searchKey").combobox('getValue') == "" && !isChecked){
		parent.$.messager.alert('','배포요청일자 또는 검색조건을 입력해주세요.');
		return false;
	}
	
	if($("#submit_form #searchKey").combobox('getValue') == "S"){
		if($("#submit_form #search_dbio").textbox('getValue') == ""){
			parent.$.messager.alert('','SQL식별자(DBIO)를 입력해 주세요.');
			return false;
		}
	}else if($("#submit_form #searchKey").combobox('getValue') == "P"){
		if($("#submit_form #search_program_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','프로그램명을 입력해 주세요.');
			return false;
		}
	}else if($("#submit_form #searchKey").combobox('getValue') == "T"){
		if($("#submit_form #search_trade").textbox('getValue') == ""){
			parent.$.messager.alert('','거래명을 입력해 주세요.');
			return false;
		}
	}else if($("#submit_form #searchKey").combobox('getValue') == "Q"){
		if($("#submit_form #search_program_desc").textbox('getValue') == ""){
			parent.$.messager.alert('','프로그램설명을 입력해 주세요.');
			return false;
		}
	}
	
	return true;
}
/*페이징처리끝*/

function createExceptionStateTab(){
	$("#detail_form #program_source_desc").val("");
//	console.log("ifram params:::"+$("#detail_form").serialize());
	document.getElementById('exec_handling').src = "/PerformanceCheckIndex/ExceptionState/CreateTab?"+$("#detail_form").serialize();
	
};
function createExceptionStateTab_reload(){
	document.getElementById('exec_handling').src = "/PerformanceCheckIndex/ExceptionState/CreateTab";
};

function setDetailView(selRow){

	if(selRow.exception_request_detail_why != ''){
		$("#exec_request_form #exception_request_detail_why").textbox("setValue", selRow.exception_request_detail_why);
	}else{
		$("#exec_request_form #exception_request_detail_why").textbox("setValue","");
	}
	if(selRow.exception_requester != '' && selRow.exception_requester != null ){
		$("#exec_request_form #exception_requester").textbox("setValue", selRow.exception_requester);
	}else{
		$("#exec_request_form #exception_requester").textbox("setValue", $("#submit_form #user_nm").val());
	}
	$("#detail_form #perf_check_id").val(selRow.perf_check_id);
	$("#detail_form #perf_check_step_id").val(selRow.perf_check_step_id);
	$("#detail_form #exception_request_id").val(selRow.exception_request_id);
	$("#detail_form #deploy_check_status_cd").val(selRow.deploy_check_status_cd);
	$("#detail_form #exception_requester_id").val(selRow.exception_requester_id);
	$("#detail_form #excepter_id").val(selRow.excepter_id);
	$("#detail_form #program_div_cd").val(selRow.program_div_cd);
	$("#detail_form #deploy_requester_id").val(selRow.deploy_requester_id);
	$("#detail_form #perf_check_result_div_cd").val(selRow.perf_check_result_div_cd);
	$("#detail_form #exception_prc_meth_cd").val(selRow.exception_prc_meth_cd);
	$("#detail_form #program_source_desc").val(selRow.program_source_desc);
	$("#detail_form #program_source_desc_temp").val(selRow.program_source_desc_temp);
	$("#detail_form #last_perf_check_step_id").val(selRow.perf_check_step_id);
	$("#detail_form #wrkjob_cd").val(selRow.wrkjob_cd);
	$("#detail_form #exception_prc_dt").val(selRow.exception_prc_dt);
	$("#detail_form #exception_prc_status_cd_nm").val(selRow.exception_prc_status_cd_nm);
	$("#detail_form #perf_check_auto_pass_yn").val(selRow.perf_check_auto_pass_yn);
	$("#detail_form #exception_prc_why").val(selRow.exception_prc_why);
	$("#detail_form #perf_check_auto_pass_del_yn").val(selRow.perf_check_auto_pass_del_yn);

	$("#detail_form #deploy_id").textbox("setValue", selRow.deploy_id);
	$("#detail_form #deploy_nm").textbox("setValue", selRow.deploy_nm);
	$("#detail_form #deploy_check_status_cd_nm").textbox("setValue", selRow.deploy_check_status_cd_nm);
	$("#detail_form #deploy_expected_day").textbox("setValue", getDateFormat(selRow.deploy_expected_day));
	$("#detail_form #program_id").textbox("setValue", selRow.program_id);
	$("#detail_form #program_nm").textbox("setValue", selRow.program_nm);
	$("#detail_form #dbio").textbox("setValue", selRow.dbio);
	$("#detail_form #file_nm").textbox("setValue", selRow.file_nm);
	$("#detail_form #dir_nm").textbox("setValue", selRow.dir_nm);
	$("#detail_form #program_execute_tms").textbox("setValue", selRow.program_execute_tms);
	$("#detail_form #program_desc").textbox("setValue", selRow.program_desc);
	$("#detail_form #program_type_cd").val(selRow.program_type_cd);
	$("#detail_form #program_type_cd_nm").textbox("setValue", selRow.program_type_cd_nm);
	$("#detail_form #sql_command_type_cd").textbox("setValue", selRow.sql_command_type_cd);
	$("#detail_form #dynamic_sql_yn").textbox("setValue", selRow.dynamic_sql_yn);

	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue", selRow.wrkjob_cd_nm);
	$("#exec_request_form #program_nm").textbox("setValue", selRow.program_nm);
	$("#exec_request_form #dbio").textbox("setValue", selRow.dbio);
	$("#exec_request_form #exception_request_why").textbox("setValue", selRow.exception_request_why);

	
	$("#exec_request_form #exception_request_why_cd").combobox("setValue", selRow.exception_request_why_cd);
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", selRow.exception_prc_meth_cd);
	
	$("#detail_form #except_processor").val(selRow.except_processor);
	$("#detail_form #exception_prc_dt").val(selRow.exception_prc_dt);
	$("#detail_form #exception_prc_status_cd").val(selRow.exception_prc_status_cd);
	$("#detail_form #exception_request_why_cd_nm").val(selRow.exception_request_why_cd_nm);
	$("#exec_request_form #btn_request").attr("disabled","false");
	
//	alert("tab_index ::: " + tab_index);
	
	if(tab_index == 0){
		getProgramSourceDesc();
		getBindTableList();
	}else if(tab_index == 1){
		getPerfCheckResultTableList();
	}else if(tab_index == 2){
		
	}else if(tab_index == 3){
		createExceptionStateTab();  //탭을 생성한다.
	}
	
//	ajaxCallGetDefaultParsingSchemaInfo();
}

function ajaxCallGetDefaultParsingSchemaInfo(){
	ajaxCall("/getDefaultParsingSchemaInfo",
			$("#detail_form"),
			"POST",
			callback_getDefaultParsingSchemaInfoAction);
}

var callback_getDefaultParsingSchemaInfoAction = function(result){
	try{
		var data = JSON.parse(result);
		dbid = data.rows[0].dbid;
		$("#detail_form #dbid").val(dbid);
	}catch(e){
		parent.$.messager.alert('',result.message);
	}
}

function getProgramSourceDesc(){

	ajaxCall("/PerformanceCheckIndex/programSourceDesc",
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

	$("#submit_form").attr("action","/PerformanceCheckIndex/ProPerfExcReqState/ExcelDown");
	$("#submit_form").submit();
//	$("#submit_form").attr("action","");

}

function Btn_ShowTab(strIndex){
	$('#perf_exec_req_tab').tabs('select', strIndex);
}

function Btn_SetSQLFormatter(){
	$('#program_source_desc').format({method: 'sql'});
}
function cellStyler(value,row,index){
	if(row.perf_check_result_div_cd_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '점검제외'){
		return 'background-color:#012753;color:white;';
	}
}
function cellStyler2(value,row,index){
	if(row.perf_check_result_div_cd_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(perf_check_result_div_cd_nm == '점검제외'){
		return 'background-color:#012753;color:white;';

	}
}
