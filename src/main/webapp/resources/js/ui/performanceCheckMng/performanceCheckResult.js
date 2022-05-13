$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var clipboard1 = new Clipboard('#sqlCopyBtn1');
    clipboard1.on('success', function(e) {
    	parent.$.messager.alert('','복사 되었습니다.');
    });
	var clipboard2 = new Clipboard('#sqlCopyBtn2');
    clipboard2.on('success', function(e) {
    	parent.$.messager.alert('','복사 되었습니다.');
    });
    
	$("#aSearchBtn").click(function(){
		Btn_OnClick();	
	});
	
	$('#improper').checkbox({
		checked: false
	});

	$('#unperformed').checkbox({
		checked: false 
	});
	
	$('#searchCount').combobox({
	    onChange:function(newValue, oldValue){
	    	$("#submit_form  #pagePerCount").val(newValue);
	    },
	    onSelect:function(record){
	    }
	});
	
	/*성능 점검 결과 탭 상단 table */
    /**
	$("#perfChkResultList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		onClickRow : function(index,row) {
			callPerfChkResultTab(row.program_execute_tms, row.perf_check_result_div_nm);
		},			
		columns:[[
			{field:'program_execute_tms',title:'수행회차',width:'100px',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_result_div_nm',title:'점검결과',width:'100px',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
			{field:'program_executer_nm',title:'수행자',width:'100px',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_exec_dt',title:'수행 일시',width:'200px',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'parsing_schema_name',title:'파싱 스키마',width:'100px',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_exec_div_nm',title:'수행 유형',width:'100px',halign:"center",align:'center',sortable:"true",styler:cellStyler1}
		]],
		onLoadSuccess:function(){
			var selectedRow = $("#perfChkResultList").datagrid("getRows");
			if(selectedRow.length > 0){
				callPerfChkResultTab(selectedRow[0].program_execute_tms, selectedRow[0].perf_check_result_div_nm);
			}else{
				$("#tr_perf_check_result_table").show();
				$("#tr_perf_impr_guide").show();
				$("#tr_exec_plan").show();
			}
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
    */
	// 점검결과코드 조회
	$('#perf_check_result_div_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1055&isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
//				$(this).combobox('setValue',deploy_check_status_cd);
			}
		},
	    onChange:function(newValue, oldValue){
	    }
	});

	// 프로그램유형 조회
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

	// SQL명령 유형 조회
	$('#sql_command_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1068&isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
//				$(this).combobox('setValue',deploy_check_status_cd);
			}
		},
	    onChange:function(newValue, oldValue){
	    }
	});

	// 수행회차
	$('#program_execute_tms').combobox('setValue','01');
		
	/* 성능점검완료,성능 점검 완료*/
	/* 데이터가 없어도 성능 점검 완료 가능 */
	$("#perfChkCompleteBtn").click(function(){
		ajaxCall("/getPerfCheckResultCount", $("#submit_form"), "POST", callback_GetPerfCheckResultCount);
	});
	
	//callback 함수
	var callback_GetPerfCheckResultCount = function(result) {
		try{
			var data = JSON.parse(result);
			console.log("data.resultCode:",data.resultCode);
			console.log("data.rows:",data.rows);
			console.log("data.rows is null:",(data.rows == null));
			
			var testMissCnt = parseInt(data.rows[0].test_miss_cnt);
			var passCnt = parseInt(data.rows[0].pass_cnt);
			var failCnt = parseInt(data.rows[0].fail_cnt);
			var errCnt = parseInt(data.rows[0].err_cnt);

			console.log("testMissCnt :"+testMissCnt);
			console.log("passCnt :"+passCnt);
			console.log("failCnt :"+failCnt);
			console.log("errCnt :"+errCnt);
			console.log("testMissCnt + errCnt :"+(failCnt + errCnt));
			console.log("testMissCnt > 0 :"+(testMissCnt > 0));
			
			var msg = "";
			if(testMissCnt > 0){
				msg = "미수행 프로그램이 존재하여 성능점검을 완료할 수 없습니다. 미수행프로그램 : "+testMissCnt+"건";
				parent.$.messager.alert('',msg,'info');
				return;
			}else{
				if((failCnt + errCnt) > 0 ){
					msg = "부적합(오류) 프로그램이 존재합니다. 성능점검을 완료하시겠습니까?";
				}else{
					msg = "성능점검을 완료하시겠습니까?";
				}
				fnPerfCheckComplete(msg);
			}
		}catch(e){
			  console.log(e.name + ': ' + e.message);
		}
	};
	
	/* 성능점검완료,성능 점검 완료*/
	function fnPerfCheckComplete(msg){
		var dlg = parent.$.messager.confirm({
		    title: 'Confirm',
		    msg: msg,
		    buttons:[{
		        text: '확인',
		        onClick: function(){
					ajaxCall("/PerformanceCheckComplete", $("#submit_form"), "POST", callback_PerformanceCheckComplete);
		            dlg.dialog('destroy')
		        }
		    },{
		        text: '취소',
		        onClick: function(){
		            dlg.dialog('destroy')
		        }
		    }
		    ]
		});
	}
	/* 성능점검단계 목록*/
//	$("#perfChkStageListBtn").click(function(){
//		$("#submit_form #call_from_parent").val("Y");
//	});
	/* 성능점검관리 목록*/
	$("#perfChkMngListBtn").click(function(){
		$("#submit_form #call_from_parent").val("Y");
		$("#submit_form").attr("action","/PerformanceCheckMng");
		$("#submit_form").submit();		
	});	
	$("#checkboxTestBtn").click(function(){
		$("#submit_form").attr("action","/PerformanceCheckBasic");
		$("#submit_form").submit();
	});
	
	$('#searchValue').textbox('textbox').bind('keyup', function(e){
		   if (e.keyCode == 13){
			   Btn_OnClick();
		   }
	});

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
	
	/**성능점검관리 탭 1, 2, 3, 4 */
	$("#perfChkResultTabs").tabs({
		plain: true,
		onSelect: function(title,index){
			/* 탭을 클릭시 화면을 높이 자동 조절 */
//			var height;
//
//			if(index == 2){
//				height = $("#container").height() + 35;
//			}else{
//				height = $("#container").height();
//			}
			//parent.resizeTopFrame($("#menu_id").val(), height);
			fnSelectedTabIndex(index);
		}		
	});
	
	//$("#perfChkResultTabs").tabs('select',0);
	$('#bindSearchDialog').dialog({
		width:600,
		height:620,
		top:getWindowTop(10),
		left:getWindowLeft(10),
		title:"바인드 검색",
		closed:true,
//		modal:true
		modal:false
	});

	if(perf_test_complete_yn == 'Y'){
		$('#perfChkResultTabs').tabs('disableTab', 2);  // the tab panel index start with 0
	}

//    //성능점검결과 상단 데이터 그리드 생성 스크립트
//    createPerfChkResultDataGrid(createParam());
    
	fnSearch();
	
	if(error_message != null) {
		console.log("error_message[" + error_message + "]");
		if(error_message.length > 0) {
			parent.$.messager.alert('',error_message,'info');
		}
	}

	$("#perfChkRsltNoti").click(function(){
		
		parent.$.messager.confirm('', "점검결과를 배포시스템에 통보하시겠습니까?", function(r){
			if (r){
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("점검결과 통보"," ");
				var paramArry = "";
				
				paramArry += deploy_id + "^" + perf_check_id + "^" + perf_check_result_div_cd + "|";
				$("#submit_form #params").val(strRight(paramArry,1));
					
				ajaxCall("/PerfChkRsltNoti?pageName=performanceCheckResult", $("#submit_form"), "POST", callback_PerfChkRsltNotiAction);	
			}
		});
		
	});
});

var callback_PerfChkRsltNotiAction = function(data){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	try{
		if(data.result){
			parent.$.messager.alert('성능점검결과통보',data.message,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
					window.location.reload();
				},1000);
			});
		}else{
			parent.$.messager.alert('',result.message,'info');
			
		}
	}catch(e){
		parent.$.messager.alert('',"오류가 발생하였습니다.");
		
	}
}

function createParam(){
	var param = "";
	var wrkjob_cd = $("#submit_form #wrkjob_cd").val();
	var top_wrkjob_cd = $("#submit_form  #top_wrkjob_cd").val();
	var program_id = $("#submit_form  #program_id").val();
	var perf_check_id = $("#submit_form  #perf_check_id").val();
	var perf_check_step_id = $("#submit_form  #perf_check_step_id").val();
	var program_execute_tms = $("#submit_form  #hidden_program_execute_tms").val();
	var perf_check_result_div_nm = encodeURIComponent($("#submit_form  #perf_check_result_div_nm").val());
	var program_exec_dt = $("#submit_form  #program_exec_dt").val();
	var currentPage = $("#submit_form  #currentPage").val();
	var auto_skip = $("#submit_form  #auto_skip").val();
	var dbid = $("#submit_form  #dbid").val();
	var program_type_cd = $("#submit_form  #hidden_program_type_cd").val();
	var sql_command_type_cd = $("#submit_form  #hidden_sql_command_type_cd").val();

	param += "wrkjob_cd="+wrkjob_cd;
	param += "&top_wrkjob_cd="+top_wrkjob_cd;
	param += "&program_id="+program_id;
	param += "&perf_check_id="+perf_check_id;
	param += "&perf_check_step_id="+perf_check_step_id;
	param += "&program_execute_tms="+program_execute_tms;
	param += "&hidden_program_execute_tms="+program_execute_tms;
	param += "&perf_check_result_div_nm="+perf_check_result_div_nm;
	param += "&program_exec_dt="+program_exec_dt;
	param += "&currentPage="+currentPage;
	param += "&auto_skip="+auto_skip;
	param += "&dbid="+dbid;
	param += "&program_type_cd="+program_type_cd;
	param += "&sql_command_type_cd="+sql_command_type_cd;
	return param;
}

function createIframeParam(){
	$("#if_submit_form  #wrkjob_cd").val( $("#submit_form  #wrkjob_cd").val());
	$("#if_submit_form  #top_wrkjob_cd").val( $("#submit_form  #top_wrkjob_cd").val());
	$("#if_submit_form  #program_id").val( $("#submit_form  #program_id").val());
	$("#if_submit_form  #perf_check_id").val( $("#submit_form  #perf_check_id").val());
	$("#if_submit_form  #perf_check_step_id").val( $("#submit_form  #perf_check_step_id").val());
	$("#if_submit_form  #program_execute_tms").val( $("#submit_form  #hidden_program_execute_tms").val());
	$("#if_submit_form  #perf_check_result_div_nm").val( $("#submit_form  #perf_check_result_div_nm").val());
	$("#if_submit_form  #program_exec_dt").val( $("#submit_form  #program_exec_dt").val());
	$("#if_submit_form  #currentPage").val( $("#submit_form  #currentPage").val());
	$("#if_submit_form  #auto_skip").val( $("#submit_form  #auto_skip").val());
	$("#if_submit_form  #dbid").val( $("#submit_form  #dbid").val());
	$("#if_submit_form  #program_type_cd").val( $("#submit_form  #hidden_program_type_cd").val());
	$("#if_submit_form  #sql_command_type_cd").val( $("#submit_form  #hidden_sql_command_type_cd").val());
}

function fnSelectedTabIndex(index){
	console.log("fnSelectedTabIndex("+index+")");
	
	var program_id = $("#program_id").val();
	if(program_id == ""){
		if(index != undefined && index != 0){
			parent.$.messager.alert('','프로그램ID를 선택하여 주세요.','info');
			return;
		}
	}
	
	var param = createParam();
	if(index == 1){
		createPerfChkResultTab2(param);
	}else if(index == 2){
		//createPerfChkResultTab3(param);
		createIframeParam();
//		$('#perfCheckIF').attr('src', "/PerfCheckResultList/createPerfChkResultTab3If?"+param);
		$("#if_submit_form").submit();
	}else if(index == 3){
		var program_type_cd = $("#hidden_program_type_cd").val();
		var perf_check_result_div_nm = $("#perf_check_result_div_nm").val();
		var sql_command_type_cd = $("#hidden_sql_command_type_cd").val();
		var perf_check_result_div_cd_nm = $("#submit_form #perf_check_result_div_nm").val();
		console.log("program_type_cd:"+program_type_cd+" sql_command_type_cd:"+sql_command_type_cd);
		
		if(program_type_cd == 1){//온라인
			createPerfChkResultTab4_1(param);
			
			$("#ta_exec_plan").html("");
			$("#tr_exec_plan").hide();
			$("#tr_perf_chk_result").hide();
			
			if(perf_check_result_div_cd_nm != "미수행"){
				if(sql_command_type_cd == "SELECT"){
						$("#tr_perf_chk_result").hide();
						$("#tr_exec_plan").show();
					
				}else{
					$("#tr_perf_chk_result").show();
					$("#tr_exec_plan").hide();
				}// SELECT DML 구분 조건문
			}//미수행 확인 조건문
			
			$("#tr_perf_check_result_table").show();
			if(perf_check_result_div_nm == "오류"){
				$("#tr_perf_check_result_basis_why1").show();
			}else{
				$("#tr_perf_check_result_basis_why1").hide();
			}
			$("#tr_perf_impr_guide").show();
		}else{//배치
			createPerfChkResultTab4_2(param);
			$("#tr_perf_chk_result").show();			
			$("#tr_exec_plan").hide();			

			$("#tr_perf_check_result_table").hide();
			$("#tr_perf_check_result_basis_why1").hide();
			$("#tr_perf_impr_guide").hide();
		}
	}
}

//성능점검결과 목록 조회
//function createPerfChkResultDataGrid(param){
//	console.log("createPerfChkResultDataGrid function called");
//	$.ajax({
//	    url: '/PerfCheckResultList/createPerfChkResultDataGrid.json',
//	    type: 'POST',
//	    data: param,
//	    success: function(data){
//	    	$('#perfCheckResultDataGrid').html(data);
//	    },
//	    error: function(e){
//			parent.$.messager.alert('','성능점검결과 목록 조회중 오류가 발생하였습니다.','info');
//	    }
//	});
//}

//프로그램 정보 탭
function createPerfChkResultTab2(param){
	
	$.ajax({
	    url: '/PerfCheckResultList/createPerfChkResultTab2.json',
	    type: 'POST',
	    data: param,
//		dataType: "json",
	    success: function(data){
	    	$("#submit_form #html").val(data);
			ajaxCall("/PerfCheckResultList/createPerfChkResultTab2BodySelect", $("#submit_form"), "POST", callback_ExtractHtmlBody);		
	    },
	    error: function(e){
			parent.$.messager.alert('','프로그램 정보 조회중 오류가 발생하였습니다.','info');
	    }
	});
}

var callback_ExtractHtmlBody = function(result){
	if(result.result){
		$('#programInfo').html(result.txtValue);
		
		var auto_skip = $("#auto_skip").val();
		var isAutoSkip = false;
		if(auto_skip == "Y"){
			isAutoSkip = true;
		}
		$('#perf_check_auto_pass_yn_chk').checkbox({
			checked: isAutoSkip
		});
		fnChangeProgramExecuteTms();
	}
	$("#submit_form #html").val("");
	console.log("["+$("#submit_form #html").val()+"]");	
}

//성능 점검 탭
//function createPerfChkResultTab3(param){
//	console.log("createPerfChkResultTab3 function called");
//	$.ajax({
//	    url: '/PerfCheckResultList/createPerfChkResultTab3.json',
//	    type: 'POST',
//	    data: param,
//	    success: function(data){
//	    	$('#perfCheck').html(data);
//	    },
//	    error: function(e){
//			parent.$.messager.alert('','성능 점검 데이터 조회중 오류가 발생하였습니다.','info');
//	    }
//	});	
//}

//성능점검결과 탭
function createPerfChkResultTab4_1(param){
	console.log("createPerfChkResultTab4_1 function called");
	console.log("PROGRAM_EXECUTE_TMS:"+$("#hidden_program_execute_tms").val());
	ajaxCall("/PerfCheckResultList/getDeployPerfChkExeHistList", $("#submit_form"), "POST", callback_DeployPerfChkExeHistList);
}

var v_perf_check_result_div_nm;
function callPerfChkResultTab(program_execute_tms, perf_check_result_div_nm, parsing_schema_name){
	v_perf_check_result_div_nm = perf_check_result_div_nm;
	$("#submit_form #hidden_program_execute_tms").val(program_execute_tms);
	$("#submit_form #parsing_schema_name").val(parsing_schema_name);
	$("#span_program_execute_tms").text(program_execute_tms);
	if(program_execute_tms == ""){
		$("#span_program_execute_tms").parent().find("a").hide();
	}else{
		$("#span_program_execute_tms").parent().find("a").show();
	}

	var program_id = $("#submit_form #program_id").val();
	var perf_check_id = $("#submit_form #perf_check_id").val();
	var perf_check_step_id = $("#submit_form #perf_check_step_id").val();
	var program_execute_tms = $("#submit_form #program_execute_tms").val();
	var wrkjob_cd = $("#submit_form #wrkjob_cd").val();
	if(program_id == ""){
		parent.$.messager.alert('','프로그램ID를 선택하여 주세요.','info');
		return;
	}
	if(perf_check_id == ""){
		parent.$.messager.alert('','성능점검ID가 없습니다.','info');
		return;
	}
	if(perf_check_step_id == ""){
		parent.$.messager.alert('','성능점검단계ID가 없습니다.','info');
		return;
	}
	if(perf_check_step_id == ""){
		parent.$.messager.alert('','수행회차가 없습니다.','info');
		return;
	}
	ajaxCall("/PerfCheckResultList/selectPerfCheckResultBasisWhy", $("#submit_form"), "POST", callback_PerfCheckResultBasisWhy);				//근거사유
	ajaxCall("/PerfCheckResultList/selectDeployPerfChkDetailResultList", $("#submit_form"), "POST", callback_DeployPerfChkDetailResultList);	//상세 점검 결과
	ajaxCall("/getDeployPerfSqlPlan", $("#submit_form"), "POST", callback_ExplainPlanTreeAction);												//예상 시행 계획
	if(v_perf_check_result_div_nm != "오류"){
		//개선가이드
		ajaxCall("/PerfCheckResultList/selectImprovementGuideList", $("#submit_form"), "POST", callback_PerfChkResultImproveGuide);
	}
	if(v_perf_check_result_div_nm == "오류"){
		$("#tr_perf_check_result_table").hide();
		$("#tr_perf_impr_guide").hide();
		$("#tr_perf_check_result_basis_why1").show();
		$("#tr_exec_plan").hide();
	}else if(v_perf_check_result_div_nm == "적합"){
		$("#tr_perf_check_result_table").show();
		$("#tr_perf_impr_guide").hide();
		$("#tr_perf_check_result_basis_why1").hide();
		$("#tr_exec_plan").show();
	}else{
		$("#tr_perf_check_result_table").show();
		$("#tr_perf_impr_guide").show();
		$("#tr_exec_plan").show();
	}
	
}
//callback 함수
//상세 점검 결과
var callback_DeployPerfChkDetailResultList = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var html = "";
	        jQuery.each(data.rows, function(index, row){
	            var perf_check_meth_cd = row.perf_check_meth_cd;
	            var perf_check_result_div_cd = row.perf_check_result_div_cd;
	            html += "<tr>\n";
	            html += "	<td><input type='text' size='15' class='font11px width100per border0px' value='"+row.perf_check_indc_nm+"' readonly></td>\n";
	            if(perf_check_meth_cd == 1){
//	            	html += "	<td><span class='textbox' style='width:100px'><input type='text' class='tar' value='"+row.indc_pass_max_value+"'></span></td>";
	            	html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.indc_pass_max_value+"' readonly></td>\n";
	            	html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.indc_pass_max_value+" 초과' readonly></td>\n";
	            }else if(perf_check_meth_cd == 2){
	            	var chk1 = "";
	            	var chk2 = "";
	            	if(perf_check_result_div_cd == 'A'){
		            	var chk1 = "checked='checked'";
		            	var chk2 = "";
	            	}else if(perf_check_result_div_cd == 'B'){
		            	var chk1 = "";
		            	var chk2 = "checked='checked'";
	            	}
	            	html += "	<td class='tac' colspan='2'>";
	            }
	            html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exec_result_value+"'readonly></td>\n";
	            
	            var backColor = "";
            	if(row.perf_check_result_div_nm == '적합'){
            		backColor = 'style="background-color:#1A9F55;color:white;"';
            	}else if(row.perf_check_result_div_nm == '부적합'){
            		backColor = 'style="background-color:#E41E2C;color:white;"';
            	}else if(row.perf_check_result_div_nm == '오류'){
            		backColor = 'style="background-color:#ED8C33;color:white;"';
            	}else if(row.perf_check_result_div_nm == '미수행'){
            		backColor = 'style="background-color:#7F7F7F;color:white;"';
            	}else if(row.perf_check_result_div_nm == '수행중'){
            		return 'background-color:#89BD4C;color:white;';
            	}else if(row.perf_check_result_div_nm == '점검제외'){
            		backColor = 'style="background-color:#012753;color:white;"';
            	}
	            
	            html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+row.perf_check_result_div_nm+"'readonly></td>\n";
	            html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exception_yn+"'readonly></td>\n";
	            html += "	<td>"+nvl(row.perf_check_result_desc,'')+"</td>\n";
	            html += "</tr>\n";
	        });
			$("#detailCheckResultTable tbody").html("");			
			$("#detailCheckResultTable tbody").append(html);			
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

//callback 함수
//var callback_DeployPerfChkExeHistList = function(result) {
//	json_string_callback_common(result,'#perfChkResultList',true);
//};

//데이터 그리드 말고, 일반 테이블
var callback_DeployPerfChkExeHistList = function(result) {
	console.log("callback_DeployPerfChkExeHistList function called");
	//튜닝요청 버튼 숨기기
	$("#span_program_execute_tms").parent().find("a").hide();

	var data;
	var style1;
	var style2;
	try{
		data = JSON.parse(result);
		var html = "";
		if(data.rows.length > 0){
			for(var i=0;i<data.rows.length;i++){
				var row = data.rows[i];
				style1 = cellStyler1('',row,'');
				style2 = cellStyler2('',row,'');
				html += "<tr style='cursor:pointer;' id='perfChkResultListTr"+i+"' onclick='changeBgColor("+i+");callPerfChkResultTab(\""+row.program_execute_tms+"\",\""+row.perf_check_result_div_nm+"\",\""+row.parsing_schema_name+"\")'>";
				html += "	<td class='tac'>"+row.program_execute_tms+"</td>";
				html += "	<td class='tac' style='"+cellStyler2('',row,'')+"'>"+row.perf_check_result_div_nm+"</td>";
				html += "	<td class='tac' style='"+cellStyler1('',row,'')+"'>"+row.program_executer_nm+"</td>";
				html += "	<td class='tac' style='"+cellStyler1('',row,'')+"'>"+row.program_exec_dt+"</td>";
				html += "	<td class='tac' style='"+cellStyler1('',row,'')+"'>"+row.parsing_schema_name+"</td>";
				html += "	<td class='tac' style='"+cellStyler1('',row,'')+"'>"+row.program_exec_div_nm+"</td>";
				html += "</tr>";
			}
			let sqlCommandTypeCd = $("#if_submit_form  #sql_command_type_cd").val();
			if(sqlCommandTypeCd == "SELECT"){
				callPerfChkResultTab(data.rows[0].program_execute_tms, data.rows[0].perf_check_result_div_nm, data.rows[0].parsing_schema_name);
			} else{
				ajaxCall("/getDeployPerfSqlPlan", $("#submit_form"), "POST", callback_ExplainPlanTreeAction);
				callPerfChkResultTab(data.rows[0].program_execute_tms, data.rows[0].perf_check_result_div_nm, data.rows[0].parsing_schema_name);
			}
		}else{
			html += "<tr><td colspan='6' class='tac'>데이터가 없습니다.</td></tr>";
		}
		$("#perfChkResultList tbody").html(html);
		
	}catch(e){
		console.log(e.name + ': ' + e.message);
	}
//	json_string_callback_common(result,'#perfChkResultList',true);
};

function changeBgColor(idx){
	for(var i=0;i<$("#perfChkResultList tr").length;i++){
		$("#perfChkResultListTr"+i).css("background-color","#ffffff");
	}
	$("#perfChkResultListTr"+idx).css("background-color","#e7e7e7");
}

//callback 함수
var callback_PerfCheckResultBasisWhy = function(result) {
	var data;
	try{
		data = JSON.parse(result);
		$("#ta_perf_check_result_basis_why1").val(data.perf_check_result_basis_why1+"\n"+data.perf_check_result_basis_why2);
		
		var program_type_cd = $("#hidden_program_type_cd").val();
		var sql_command_type_cd = $("#hidden_sql_command_type_cd").val();
		if(program_type_cd == "1"){
			if(sql_command_type_cd == "SELECT"){
				if(v_perf_check_result_div_nm != "오류"){
					$("#ta_exec_plan").val(data.exec_plan);
					$("#tr_exec_plan").show();
				}
			}else{
				$("#tr_exec_plan").hide();
			}
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

//callback 함수
var callback_PerfChkResultImproveGuide = function(result) {
	var data;
	try{
		data = JSON.parse(result);
		var rows = data.rows;
		
//		var txt = "";
		var html = "";
        jQuery.each(data.rows, function(index, row){
        	var perf_check_indc_nm = nvl(row.perf_check_indc_nm,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
        	perf_check_indc_nm = perf_check_indc_nm.replace(/ /g, '&nbsp;');
        	
        	var perf_check_indc_desc = nvl(row.perf_check_indc_desc,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
        	perf_check_indc_desc = perf_check_indc_desc.replace(/ /g, '&nbsp;');
        	
        	var perf_check_fail_guide_sbst = nvl(row.perf_check_fail_guide_sbst,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
        	perf_check_fail_guide_sbst = perf_check_fail_guide_sbst.replace(/ /g, '&nbsp;');
//        	txt += row.impr_guide +"\n";
        	html += '<tr>';
        	html += '	<td>'+perf_check_indc_nm+'</td>';
        	html += '	<td>'+perf_check_indc_desc+'</td>';
        	html += '	<td>'+perf_check_fail_guide_sbst+'</td>';
        	html += '</tr>';
        });

//		$("#ta_perf_impr_guide").val(txt);
		$("#perf_impr_guide_table tbody").html(html);
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

function formValidationCheck(){
	return true;
}

//callback 함수
/* 성능점검완료,성능 점검 완료*/
var callback_PerformanceCheckComplete = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				window.location.reload();
			},1000);	
		});			
	}else{
		parent.$.messager.alert('',result.message,'info');
	}
};

function Btn_OnClick(){
	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	$("#submit_form #currentPage").val("1");
	fnSearch();
}

function fnSearch(){
	//그리드 헤더와 내용이 어긋나는 현상을 해결하기 위해...
	//검색을 수행할때 매번 그리드를 새로 생성한다.
	fnCreateDatagrid();
	// 수행회차
	$('#program_execute_tms').combobox('setValue','01');
	
	$('#submit_form #tableList').datagrid("loadData", []);
	//페이징버튼 초기화
//	fnInitPagingBtn();
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallPerfCheckResultList();
	ajaxCallPerfCheckResultBasicInfo();
}

function ajaxCallPerfCheckResultList(){
	console.log("ajaxCallPerfCheckResultList function called");
	$("#tableList tbody tr").remove();
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 결과"," "); 
	ajaxCall("/PerfCheckResultList", $("#submit_form"), "POST", callback_PerfCheckResultList);
}

//callback 함수
var callback_PerfCheckResultList = function(result) {
	json_string_callback_common(result,'#tableList',true);
//	fnControlPaging(result);
	
	try{
		var data = JSON.parse(result);
		var dataLength=0;
		dataLength = data.dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
	}catch(e){
		console.log("e.message:"+e.message);
	}

};

//전체,적합,오류,부적합,미수행 건수 조회
function ajaxCallPerfCheckResultBasicInfo(){
	console.log("ajaxCallPerfCheckResultBasicInfo function called");
	ajaxCall("/getPerfCheckResultBasicInfo", $("#submit_form"), "POST", callback_GetPerfCheckResultBasicInfo);
}

//callback 함수
var callback_GetPerfCheckResultBasicInfo = function(result) {
	try{
		var data = JSON.parse(result);
		var row = data.rows[0];
		var total_cnt     	=row.total_cnt;
		var pass_cnt      	=row.pass_cnt;
		var err_cnt       	=row.err_cnt;
		var fail_cnt      	=row.fail_cnt	;
		var test_miss_cnt 	=row.test_miss_cnt;
		var exception_cnt 	=row.exception_cnt;
		
		console.log("total_cnt :"+total_cnt);
		console.log("pass_cnt :"+pass_cnt);
		console.log("err_cnt :"+err_cnt);
		console.log("fail_cnt :"+fail_cnt);
		console.log("test_miss_cnt :"+test_miss_cnt);
		console.log("exception_cnt :"+exception_cnt);
		
		$(".perf_check_result_blue").val("전체 : "+total_cnt);
		$(".perf_check_result_green").val("적합 : "+pass_cnt);
		$(".perf_check_result_orange").val("오류 : "+err_cnt);
		$(".perf_check_result_red").val("부적합 : "+fail_cnt);
		$(".perf_check_result_gray").val("미수행 : "+test_miss_cnt);
		$(".perf_check_result_navy").val("점검제외 : "+exception_cnt);
		
	}catch(e){
		  console.log(e.name + ': ' + e.message);
	}
	
};

/**
 * 엑셀 다운로드
 * @returns
 */
function Excel_Download(){
	$("#submit_form").attr("action","/PerfCheckResultList/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function getDetailView(){
//	var menuId = $("#menu_id").val();
//	parent.document.getElementById('if_'+menuId).src = "/PerformanceCheckMngListView?"+$("#submit_form").serialize();
	
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
	}else if(row.perf_check_result_div_nm == '점검제외'){
		return 'background-color:#012753;color:white;';
	}
}
//예외요청 버튼을 보일것인가, 말것인가?
function formatBtn(value,row){
	if(row.exception_button_enable_yn == 'Y'){
		if(auth_cd == "ROLE_DEV" && (row.perf_check_result_div_nm == "부적합" || row.perf_check_result_div_nm == "오류" || row.perf_check_result_div_nm == "미수행")){
			return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_ExceptionRequest(\""+row.perf_check_id+"\",\""+row.program_id+"\",\""+nvl(row.program_execute_tms, '')+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 예외요청</a>";
		}else if(auth_cd == "ROLE_DEPLOYMANAGER" && (row.perf_check_result_div_nm == "부적합" || row.perf_check_result_div_nm == "오류"|| row.perf_check_result_div_nm == "미수행")){
			//개발자일경우에만 예외요청 버튼이 보인다.
			//배포성능관리자일경우에도예외요청 버튼이 보인다.2019-03-08
			//임시주석처리 2019-02-19
			return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_ExceptionRequest(\""+row.perf_check_id+"\",\""+row.program_id+"\",\""+nvl(row.program_execute_tms, '')+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 예외요청</a>";
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
	var menuParam = "";
	menuParam += "&perf_check_id="+perf_check_id;
	menuParam += "&perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&last_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&program_id="+program_id;
	menuParam += "&wrkjob_cd="+$("#wrkjob_cd").val();
//	menuParam += "&wrkjob_cd_nm="+$("#wrkjob_cd_nm").val();
	menuParam += "&wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd_nm").val());
	menuParam += "&program_execute_tms="+program_execute_tms;
	
	menuParam += "&search_perf_check_id="+perf_check_id;
	menuParam += "&search_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&search_last_perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&search_program_id="+program_id;
	menuParam += "&search_wrkjob_cd="+$("#wrkjob_cd").val();
//	menuParam += "&search_wrkjob_cd_nm="+$("#wrkjob_cd_nm").val();
	menuParam += "&search_wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd_nm").val());
	menuParam += "&search_program_execute_tms="+program_execute_tms;
	menuParam += "&call_from_parent=Y";
//exception_request_id
	parent.openLink("Y",'214', "성능 점검 예외 요청", "/PerformanceCheckIndex/ProPerfExcReq", menuParam);
	
}

/**인덱스 변경 */
function Btn_BindSearch(){
	$('#indexRequestPop').window("open");
	
	$("#indexRequest_form #dbid").val($('#submit_form #dbid').val());
	$('#indexRequest_form #db_name').textbox('setValue',$('#submit_form #db_name').val());
	
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length > 0){
		var ndg = $('#indexsRequestList').datagrid();
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
		onClickCell : function(index,field,value) {
			if(field == "exception_processing_yn") return;

			var row = $(this).datagrid('getRows')[index];
			
			$("#submit_form #program_exec_dt").val(row.program_exec_dt);
			$("#submit_form #program_id").val(row.program_id);
			$("#submit_form #hidden_program_execute_tms").val(row.program_execute_tms);
			$("#submit_form #hidden_program_execute_tms").html(row.program_execute_tms);
			$("#program_execute_tms2").val(row.program_execute_tms);
			$("#submit_form #hidden_program_type_cd").val(row.program_type_cd);
			$("#submit_form #hidden_sql_command_type_cd").val(row.sql_command_type_cd);
			$("#submit_form #clickedRowIndex").val(index);
			$("#submit_form #perf_check_result_div_nm").val(row.perf_check_result_div_nm);
			
			$("#detailCheckResultTable tbody").html("");
			$('#perf_impr_guide_table tbody').html("");
			$("#span_program_execute_tms").text("");
//			$("#td_perf_check_result_basis_why1").text("");
			$("#submit_form #ta_perf_check_result_basis_why1").val("");
			$("#submit_form #ta_perf_impr_guide").val("");
			$("#submit_form #ta_exec_plan").val("");

			//바인드검색 팝업창이 열린 상태로 그리드를 클릭하였을 경우 바인드검색 팝업창 닫기
			Btn_OnClosePopup('bindSearchDialog');
			//바인드검색 팝업창이 열린 상태로 그리드를 클릭하였을 경우 바인드검색 아랫쪽 테이블 초기화
			$('#bindSearchList2Table tbody').html("");
			$('#bindSearchList4Table tbody').html("");
			$('#perfChkResultList tbody').html("");

			// get the selected tab panel and its tab object
			var pp = $('#perfChkResultTabs').tabs('getSelected');
			var tab = pp.panel('options').tab;    // the corresponding tab object
			var selectedTabIdx = pp.panel('options').index;    // the corresponding tab object
			
			if(row.perf_check_result_div_nm == "점검제외"){
				$("#submit_form #auto_skip").val("Y");
				
				//프로그램유형=배치
				$('#perfChkResultTabs').tabs('disableTab', 2);
				$('#perfChkResultTabs').tabs('enableTab', 3);
				if(selectedTabIdx == 3){
					fnSelectedTabIndex(3);
				}else{
					$("#perfChkResultTabs").tabs('select',3);
				}
			}else{
				$("#submit_form #auto_skip").val("N");

				var sqlCommandTypeCd = row.sql_command_type_cd;
				if(sqlCommandTypeCd == "TRUNCATE"){
					$('#perfChkResultTabs').tabs('disableTab', 2);
					$('#perfChkResultTabs').tabs('disableTab', 3);
					parent.$.messager.alert('','TRUNCATE구문은 성능점검을 수행할 수 없습니다. 예외처리하세요!','info',function(){
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
							$('#perfChkResultTabs').tabs('disableTab', 2);
							$('#perfChkResultTabs').tabs('enableTab', 3);
							if(selectedTabIdx == 3){
								fnSelectedTabIndex(3);
							}else{
								$("#perfChkResultTabs").tabs('select',3);
							}
					}else{
						console.log("perf_test_complete_yn:"+perf_test_complete_yn);
						if(perf_test_complete_yn == 'Y'){
							$('#perfChkResultTabs').tabs('disableTab', 2);  // the tab panel index start with 0
							$('#perfChkResultTabs').tabs('enableTab', 3);
							if(selectedTabIdx != 3){
								$("#perfChkResultTabs").tabs('select',3);
							}else{
								fnSelectedTabIndex(3);
							}
						}else{
							$('#perfChkResultTabs').tabs('enableTab', 2);
							$('#perfChkResultTabs').tabs('enableTab', 3);
							if(selectedTabIdx != 2){
								$("#perfChkResultTabs").tabs('select',2);
							}else{
								fnSelectedTabIndex(2);
							}
						}
					}
				}
			}
			createIframeParam();
//			$('#perfCheckIF').attr('src', "/PerfCheckResultList/createPerfChkResultTab3If?"+param);
			$("#if_submit_form").submit();
			console.log("onClickCell finished");
		},		
		onClickRow : function(index,row) {
		},
		columns:[[
			{field:'perf_check_result_div_nm',title:'점검결과',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
			{field:'program_dvlp_div_nm',title:'개발구분',halign:"center",align:'center',sortable:"true"},
			{field:'sql_command_type_cd',title:'SQL명령 유형',hidden:'true'},
			{field:'sql_command_type_nm',title:'SQL명령 유형',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_nm',title:'프로그램',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true",styler:cellStyler1},
			{field:'program_type_cd',title:'프로그램유형',hidden:'true'},
			{field:'program_type_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'program_exec_dt',title:'수행일시',width:'130px',halign:"center",align:'center',sortable:"true"},
			{field:'exception_processing_yn',title:'예외요청',width:'100px',align:'center',sortable:"true",formatter:formatBtn},
			{field:'perf_check_id',title:'성능점검ID',halign:"center",align:'center',sortable:"true"},
			{field:'program_id',title:'프로그램ID',halign:"center",align:'center',sortable:"true"}
//			{field:'dynamic_sql_yn',title:'다이나믹SQL 여부',halign:"center",align:'center',sortable:"true",styler:cellStyler1}
//			{field:'parsing_schema_name',title:'파싱스키마',halign:"center",align:'center',sortable:"true"},
//			{field:'program_execute_tms',title:'수행회차',halign:"center",align:'center',sortable:"true"},
//			{field:'program_executer_nm',title:'수행자',halign:"center",align:'center',sortable:"true"},
//			{field:'program_exec_div_nm',title:'수행유형',halign:"center",align:'center',sortable:"true"},
//			{field:'file_nm',title:'파일명',halign:"center",align:'center',sortable:"true"},
//			{field:'dir_nm',title:'디렉토리',halign:"center",align:'center',sortable:"true"},
		]],		
		onLoadSuccess:function(){
			$(this).datagrid('getPanel').find('a.easyui-linkbutton').linkbutton();
			
//			var rows=$(this).datagrid("getRows");
//			for(var i=0;i<rows.length;i++){
//				console.log("rows:",rows[i]);
//				var row = rows[i];
//				if(row.program_id == program_id){
//					
//				}
//			}
			var clickedRowIndex = $("#submit_form #clickedRowIndex").val();

			$(this).datagrid('highlightRow', clickedRowIndex);
			$(this).datagrid('selectRow', clickedRowIndex);
			$(this).datagrid('checkRow', clickedRowIndex);
			$(this).datagrid('resize');
		},
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
}
//튜닝요청버튼을 눌렀을때
//sql text를 세션에 저장하도록 처리
function Btn_RequestTuning(){
	var dlg = parent.$.messager.confirm({
	    title: 'Confirm',
	    msg: "튜닝요청하시겠습니까?",
	    buttons:[{
	        text: '확인',
	        onClick: function(){
	        	ajaxCall("/PerfChkRequestTuning", $("#submit_form"), "POST", callback_PerfChkRequestTuningAction);	
	            dlg.dialog('destroy')
	        }
	    },{
	        text: '취소',
	        onClick: function(){
	            dlg.dialog('destroy')
	        }
	    }
	    ]
	});	
}
var callback_PerfChkRequestTuningAction = function(result) {
	console.log("result:",result);
	try{
		if(result.result != undefined){
			parent.$.messager.alert('',result.message);
		}else{			
			if(result.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',result.message);
			}
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

function createSqlDesc(){
	console.log("createSqlDesc............");
	var sqlDesc = "";

	var bind_var_nm_obj = perfCheckIF.$("input[name='bind_var_nm']");
	var bind_var_value_obj = perfCheckIF.$("input[name='bind_var_value']");
	for(var i=0;i<bind_var_nm_obj.length;i++){
		sqlDesc += $(bind_var_nm_obj[i]).val()+" "+ $(bind_var_value_obj[i]).val()+"\n";
	}
	return sqlDesc;
}

//성능점검결과 트리 플랜
function createPerfChkResultTab4_2(param){
	console.log("createPerfChkResultTab4_2 function called");
	ajaxCall("/getDeployPerfChkPlanTable", $("#submit_form"), "POST", callback_ExplainPlanTreeAction);
	//테스트용
//	ajaxCall("/getDeployPerfChkPlanTableTest", $("#submit_form"), "POST", callback_ExplainPlanTreeAction);
	//테스트용
//	ajaxCall("/SelfTuning/ExplainPlanTree", $("#submit_form"), "POST", callback_ExplainPlanTreeAction);	
}

//callback 함수
var callback_ExplainPlanTreeAction = function(result) {
	console.log("callback_ExplainPlanTreeAction function called");
	parent.$.messager.progress('close');
	$('#treePlan').tree("loadData", []);
	console.log("result.result:",result);
	try{
		var data = JSON.parse(result.txtValue);
		if(result.result){		
			$('#treePlan').tree("loadData", data);
			
			$('.help').tooltip({
				position: 'right'
			});
		}else{
			if(result.message != undefined){
				parent.$.messager.alert('ERROR',result.message,'error', function(){
					$('#treePlan').tree("loadData", []);	
				});
			}else{
				parent.$.messager.alert('ERROR',"성능 점검 결과 조회중 오류가 발생했습니다.",'error', function(){
					$('#treePlan').tree("loadData", []);	
				});
			}
		}
	}catch(e){
		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);	
			});			
		}else{
			parent.$.messager.alert('',e.message);
		}		
	}
	
};

function fnChangeProgramExecuteTms(){
	$("#submit_form #hidden_program_execute_tms").val($("#programExecuteTms").val());
	
	let index = $("#programExecuteTms option:selected").data("index");
	
	let date = dateArr[index],
		pagingYn = pagingYnArr[index],
		pagingCnt = pagingCntArr[index];
	
	$("#pagingYn").text(pagingYn);
	$("#pagingCnt").text(pagingCnt);
	$("#programExecDtSpan").text(date);
	
	ajaxCall("/PerfCheckResultList/selectDeployPerfChkExecBindList", $("#submit_form"), "POST", callback_BindSearch1);		
}

var callback_BindSearch1 = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var strHtml = "";
	        jQuery.each(data.rows, function(index, row){
	        	var bind_var_type = row.bind_var_type;
	        	if(bind_var_type == null || bind_var_type == "null"){
	        		bind_var_type = " ";
	        	}
	        	var bind_var_value = row.bind_var_value;
	        	if(bind_var_value == null || bind_var_value == "null"){
		        	bind_var_value = " ";
	        	}
	        	
	            strHtml += "<tr>";
	            strHtml += "	<td class='ctext'>"+row.bind_seq+"</td>";
	            strHtml += "	<td class='ctext tac'>"+row.bind_var_nm+"</td>";
	            strHtml += "	<td class='ctext'>"+bind_var_type+"</td>";
	            strHtml += "	<td class='ctext tac'>"+bind_var_value+"</td>";
	            strHtml += "</tr>";
	        });
	        if(data.rows.length == 0){
	        	strHtml += "<tr><td colspan='4' class='tac'>검색된 데이터가 없습니다.</td></tr>";
	        }
	    	$("#deployPerfChkExecBindTable tbody").html(strHtml);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

