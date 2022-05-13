tab_index = 0;
var dataRow = "";
var dbid = "";
var call_from_parent = "";
var call_from_child = "";
var currentPagePerCount = 10;
var inspectionResultPop;

//var howToWay = "A"; //개별예외요청(디폴트값)
//processNotPerformed = false;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	inspectionResultPop = $("#inspectionResultPop");
	
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});

	var clipboard2 = new Clipboard('#sqFormatterCopyBtn');
	clipboard2.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});	
	//클릭시 조건에다라 활성화 비활성화
	$("#perf_exec_req_tab").tabs({
		plain:true,
		onSelect: function(title,index){
			tab_index = index;
			
			var rows = $('#tableList').datagrid('getSelected');
			if(rows != null && index == 0){
				setDetailView(rows);
			}else if(rows != null && index == 1){
				checkValidRequest(rows); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
				setDetailView(rows);
			}else if(index == 2){
				createExceptionHandlingTab(rows);
			}
			
			if(call_from_parent == 'Y' && index == 0 && (rows == null || rows == '')){
				setDetailView(dataRow);
			}
		}		
	});
	
	$('#search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
//		url:"/Common/getWrkJobCd",
		method:'get',
		valueField:'id',
		textField:'text',
		onSelect:function(rec){
//			$("#wrkjob_cd").val(rec.wrkjob_cd);
			// 최종검증단계 조회
//			alert(rec.id);
//			$('#search_perf_check_step_id').combobox({
//				url:"/ExecPerformanceCheckIndex/getPerfCheckStep?search_wrkjob_cd="+rec.id,
//				method:"get",
//				valueField:'perf_check_step_id',
//				textField:'perf_check_step_nm',
//			});
			
			//sql runner 를 실행해주기위함.			
			//dbid = rec.dbid;
		},
		onChange:function(newval,oldval){
			$('#search_perf_check_step_id').combobox({
				url:"/ExecPerformanceCheckIndex/getPerfCheckStep?search_wrkjob_cd="+newval,
				method:"get",
				valueField:'perf_check_step_id',
				textField:'perf_check_step_nm',
			});
		},
		onLoadSuccess: function(rec) {
			$('#search_wrkjob_cd').combobox('textbox').attr("placeholder","선택");
			
			$('#search_wrkjob_cd').combotree('setValue',{
				id: search_wrkjob_cd,
				text: search_wrkjob_cd_nm
			});
			
		 	$('#search_perf_check_step_id').combobox({
				url:"/ExecPerformanceCheckIndex/getPerfCheckStep?search_wrkjob_cd="+$('#search_wrkjob_cd').combotree("getValue"),
				method:"get",
				valueField:'perf_check_step_id',
				textField:'perf_check_step_nm',
			});
		}	
	});

	// 검색건수
	$('#searchCount').combobox({
		onChange:function(newval,oldval){
			currentPagePerCount = newval;
			$("#submit_form #pagePerCount").val(currentPagePerCount); //검색페이지수 설정
		}
	});
	
	//예외 요청 처리 상태 초기화
	resetExceptionStatus();
	$("#search_exception_prc_status_cd").combobox("readonly",false);
	
	// 예외요청&처리 방법변경
	$('#way1').radiobutton({
		onChange:function(val){
			if(val == true){
				resetExceptionStatus();
				$("#search_exception_prc_status_cd").combobox("readonly",false);
				$("#howToWay").val("A");
				getProPerfExecReq('A');
				call_from_parent = '';
				$("#call_from_parent").val('');
				call_from_child = '';
				$("#call_from_child").val('');
				getPerfCheckResultTableList();
				createExceptionHandlingTab_reload()
//				createExceptionHandlingTab();
				Btn_ResetFieldAll();
				Btn_ResetBtnAll();
				Btn_ShowTab(0);
			}
		}
	});
	$('#way2').radiobutton({
		onChange:function(val){
			if(val == true){
				resetExceptionStatus();
				$("#search_exception_prc_status_cd").combobox("readonly",true);
				$("#howToWay").val("B");
				getProPerfExecReq('B');
				call_from_parent = '';
				$("#call_from_parent").val('');
				call_from_child = '';
				$("#call_from_child").val('');
				getPerfCheckResultTableList();
				createExceptionHandlingTab_reload()
//				createExceptionHandlingTab();
				Btn_ResetFieldAll();
				Btn_ResetBtnAll();
				Btn_ShowTab(1);
			}
		}
	});
	$('#way3').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#search_exception_prc_status_cd").combobox('setValue',1);			
				$("#search_exception_prc_status_cd").combobox("readonly","true");
				$("#howToWay").val("C");
				getProPerfExecReq('C');
				call_from_parent = '';
				$("#call_from_parent").val('');
				call_from_child = '';
				$("#call_from_child").val('');
				getPerfCheckResultTableList();
				createExceptionHandlingTab_reload()
//				createExceptionHandlingTab();
				Btn_ResetFieldAll();
				Btn_ResetBtnAll();
				Btn_ShowTab(2);

			}
		}
	});

/*	// 예외요청처리상태
	$('#search_exception_prc_status_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1051&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});*/
	// 예외처리방법
	$('#search_exception_prc_meth_cd').combobox({
		url:"/Common/getCommonCodeRef2?grp_cd_id=1061&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	// 성능검증결과
	$('#search_perf_check_result_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1055&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items){
			if (items.length){
				var default_perf_check_result_div_cd = "";
				for(var i=0;i<items.length;i++){
					if(items[i].ref_vl_1 == "Y"){
						default_perf_check_result_div_cd = items[i].cd;
					}
				}
				$(this).combobox('setValue',default_perf_check_result_div_cd);
			}
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
		url:"/ExecPerformanceCheckIndex/getCommonCode?grp_cd_id=1060&isChoice=N&isAll=N",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		prompt: '선택',
		onChange:function(val){
			$('#detail_form #exception_request_why_cd').val(val);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
//	    	console.log("rec.cd:",rec.cd);
//	    	console.log("rec.cd_nm:",rec.cd_nm);
//	    	console.log("rec.ref_vl_1:",rec.cd_desc);
//	    	console.log("rec.ref_vl_1:",rec.ref_vl_1);
	    }
	});

	// 예외처리방법
	$('#exec_request_form #exception_prc_meth_cd').combobox({
		url:"/Common/getCommonCodeRef2?grp_cd_id=1061&isChoice=N&isAll=N",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		prompt:'선택',
		onChange:function(val){
			$('#detail_form #exception_prc_meth_cd').val(val);
		},
//		onLoadSuccess: function(){
//			console.log("readonly",$(this).combobox("attr"));
//			
//			if($(this).prop("readonly")){
//				alert("실행");
//			}
//		},
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
	
	
	//개별 예외요청
	getProPerfExecReq('init');
	
	getPerfCheckResultTableList();
	
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
	
	$("#exec_request_form #btn_request").prop("disabled","true");
	$("#exec_request_form #btn_reset").prop("disabled","true");

	call_from_parent = $("#call_from_parent").val();
	call_from_child = $("#call_from_child").val();
	if (call_from_parent == "Y" || call_from_child == "Y") {
		$('#search_wrkjob_cd').combotree('setValue',{
			id: search_wrkjob_cd,
			text: search_wrkjob_cd_nm
		});

		$('#search_perf_check_step_id').combobox('setValue',$("#perf_check_step_id").val());
		$("#search_exception_prc_status_cd").combobox('setValue','5');

		//		Btn_OnClick();
		fnSearch();

	}
	
});

function selectChkBox(){
	if($(this).prop("checked")){
		$(this).parent().parent().addClass("chkRow");
//		var text = $(this).parent().parent().find("td").eq(3).text();
	}else{
		$(this).parent().parent().removeClass();
	}
}


function getProPerfExecReq(way){
	this.dataGridView == '';

		//개별 예외요청
		if(way == 'init'){
			
			$("#tableList").datagrid({
				view: myview,
				singleSelect : true,
				checkOnSelect : true,
				selectOnCheck : true,
				onCheckAll : function(row){
					
					if(row.length > 0){
						checkValidRequest(row[0]);
						setDetailView(row[0]);
					}
				},
//				onUncheckAll : function(row){
//					if(row.length > 0){
//						checkValidRequest(row[0]);
//						setDetailView(row[0]);
//					} 
//				},
				onCheck:function(index, row){
					checkValidRequest(row); 
					setDetailView(row);
					$('#detail_form #top_wrkjob_cd').val(row.top_wrkjob_cd);
				},
//				onUncheck:function(index, row){
//					checkValidRequest(row);
//					setDetailView(row);
//				},
//				onClickRow : function(index,row) {
//					checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
//					setDetailView(row);
//				},		
				columns:[[
					{field:'chk_user_id',checkbox:"true"},
					{field:'deploy_id',title:'배포ID',width:"6%",halign:"center",align:'center'},
					{field:'deploy_nm',title:'배포명',width:'300px',halign:"center",align:'left'},
					{field:'program_nm',title:'프로그램명',halign:"center",align:'left'},
					{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left'},
					{field:'wrkjob_cd_nm',title:'업무',width:"6%",halign:"center",align:'center'},
					{field:'perf_check_step_nm',title:'성능검증단계',width:"8%",halign:"center",align:'center'},
					{field:'deploy_check_status_cd_nm',title:'성능검증상태',width:"6%",halign:"center",align:'center'},
					{field:'perf_check_auto_pass_yn',title:'영구검증제외여부',width:"6%",halign:"center",align:'center'},
					{field:'perf_check_result_div_cd_nm',title:'성능검증결과',width:"6%",halign:"center",align:'center',styler:cellStyler},
					{field:'exception_prc_meth_cd_nm',title:'예외처리방법',width:"6%",halign:"center",align:'center'},
					{field:'exception_prc_status_cd_nm',title:'예외처리상태',width:"6%",halign:"center",align:'center'},
					{field:'perf_check_auto_pass_del_yn',title:'검증제외삭제여부',width:"6%",halign:"center",align:'center'},
					{field:'exception_request_dt',title:'예외요청일시',width:"8%",halign:"center",align:'center'},
					{field:'exception_requester',title:'예외요청자',width:"6%",halign:"center",align:'center'},
					{field:'exception_request_why_cd_nm',title:'예외요청사유',width:"6%",halign:"center",align:'center'},
					{field:'exception_request_detail_why',title:'예외요청상세사유',width:"12%",halign:"center",align:'left'},
					{field:'exception_prc_dt',title:'예외처리일시',width:"8%",halign:"center",align:'center'},	
					{field:'except_processor',title:'예외처리자',width:"6%",halign:"center",align:'center'},
					{field:'exception_prc_why',title:'예외처리사유',width:"12%",halign:"center",align:'left'},
					{field:'file_nm',title:'파일명',halign:"center",align:'left'},
					{field:'dir_nm',title:'디렉토리명',width:"20%",halign:"center",align:'left'},
					{field:'program_desc',title:'프로그램설명',width:"20%",halign:"center",align:'left'},
					{field:'deploy_request_dt',title:'배포요청일시',width:"8%",halign:"center",align:'center'},
					{field:'deploy_requester',title:'배포요청자',width:"6%",halign:"center",align:'center'},
//					{field:'deploy_expected_day',title:'배포예정일자',width:"6%",halign:"center",align:'center', formatter:getDateFormat},
//					{field:'program_type_cd_nm',title:'프로그램유형',width:"8%",halign:"center",align:'center'},  //임시
					{field:'perf_check_id',title:'성능검증ID',width:"4%",halign:"center",align:'center'},
					{field:'program_id',title:'프로그램ID',width:"4%",halign:"center",align:'center'},

					{field:'top_wrkjob_cd',hidden:true},
					{field:'exception_request_why_cd', hidden:true},
					{field:'wrkjob_cd',hidden:true},
					{field:'exception_request_id',hidden:true},
					{field:'deploy_check_status_cd', hidden:true},
					{field:'exception_prc_status_cd', hidden:true},
					{field:'exception_requester_id',hidden:true},
					{field:'excepter_id',hidden:true},
					{field:'program_div_cd',hidden:true},
					{field:'program_execute_tms',hidden:true},
					{field:'deploy_requester_id',hidden:true},
					{field:'perf_check_result_div_cd',hidden:true},
					{field:'exception_prc_meth_cd',hidden:true},
					{field:'last_perf_check_step_id',hidden:true},
					{field:'step_deploy_check_status_cd',hidden:true},
					{field:'program_type_cd',hidden:true}, //예외요청ID
					{field:'program_type_cd_nm',hidden:true}, //예외요청ID
					{field:'sql_command_type_cd',hidden:true}, //SQL명령 유형코드
					{field:'dynamic_sql_yn',hidden:true},//다이나믹SQL여부
					{field:'perf_check_step_id',hidden:true},//성능검증단계
					{field:'exception_request_why',hidden:true},
					{field:'pagingYn',hidden:true},
					{field:'pagingCnt',hidden:true},
					{field:'top_wrkjob_cd',hidden:true}
					]],
					onLoadError:function() {
						parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
					},
					onLoadSuccess: function(data){
//						if(call_from_parent == 'Y'){
//							var opts = $(this).datagrid('options');
//							if(data.rows.length > 0){
//								dataRow = data.rows[0];
//								setDetailView(dataRow);
//								$('#perf_exec_req_tab').tabs('select', 1);
//							}
//						}
					}
			});
			
			
		}else if(way =='A'){
			
			$("#tableList").datagrid({
				singleSelect : true,
				checkOnSelect : true,
				selectOnCheck : true,
			});
			Btn_OnClick();

		//일괄 예외요청방법
		}else if(way == 'B'){
			$("#tableList").datagrid({
				singleSelect : false,
				checkOnSelect : true,
				selectOnCheck : true,
			});
			Btn_OnClick();

		//일괄 예외처리
		}else if(way == 'C'){
			
			$("#tableList").datagrid({
				singleSelect : false,
				checkOnSelect : true,
				selectOnCheck : true,
			});
			Btn_OnClick();

		}
	
};

/*function getDeployPerfChkDetailResult(){
	
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 요청 - 예외 처리"," ");
	
	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkDetailResult",
			$("#detail_form"),
			"POST",
			callback_getDeployPerfChkDetailResultAction);
};

var callback_getDeployPerfChkDetailResultAction = function(result) {
//	json_string_callback_common(result,'#perfCheckResultTableList',true);
	try{
		      var data = JSON.parse(result);
		  		console.log(data);
		      if(data.result != undefined && !data.result){
		         if(data.message == 'null'){
		            parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		         }else{
		            parent.$.messager.alert('',data.message);
		         }
		      }else{
		         var html = ""
		           jQuery.each(data.rows, function(index, row){
		        	   html += "<tr>\n";
		        	   html += "<td><input id='chk_form_"+index+"' type='checkbox' name='chk_info' value=''></td>\n";
		        	   html += "<td>"+row.perf_check_indc_nm+"</td>\n";
		        	   html += "<td>"+row.indc_pass_max_value+"</td>\n";
		        	   html += "<td>"+row.not_pass_pass+"</td>\n";
		        	   html += "<td>"+row.indc_yn_decide_div_cd_nm+"</td>\n";
		        	   html += "<td>"+row.exec_result_value+"</td>\n";
		        	   html += "<td>"+row.perf_check_result_div_cd_nm+"</td>\n";
		        	   html += "<td>"+row.except_reg_yn+"</td>\n";
	        		   html += "<td><span class='textbox' style='width: 202px;'><input id='indc_pass_max_value' name='indc_pass_max_value' type='text' class='textbox-text validatebox-text validatebox-readonly' autocomplete='off' tabindex='' placeholder='' readonly='' style='margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px; width: 194px;'></span></td>\n";
	        		   html += "<td><span class='textbox' style='width: 202px;'><input id='not_pass_pass' name='not_pass_pass' type='text' class='textbox-text validatebox-text validatebox-readonly' autocomplete='off' tabindex='' placeholder='' readonly='' style='margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px; width: 194px;'></span></td>\n";
        			   html += "<td><span class='textbox' style='width: 202px;'><input id='indc_yn_decide_div_cd_nm' name='indc_yn_decide_div_cd_nm' type='text' class='textbox-text validatebox-text validatebox-readonly' autocomplete='off' tabindex='' placeholder='' readonly='' style='margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px; width: 194px;'></span></td>\n";
        			   <input type='text' id='' name='' class='w110 textbox-text validatebox-text validatebox-readonly' style='margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px; width: 194px;'/>
        			   html += "<tr>\n";
		           });
//		           html += "</table>\n";
		         	alert(html);
		          $("#exceptionHandling").html(html);
		      }
		   }catch(e){
		      parent.$.messager.alert('',e.message);
		   }
};*/

function checkValidRequest(row){

/*********** 성능검증결과구분코드 (PERF_CHECK_RESULT_DIV_CD) **********
| 	A : 적합		|	B : 부적합	|	C : 오류		|	9 : 미수행	|
******************************************************************/
	
/******************* 배포성능검증기본코드 (DEPLOY_CHECK_STATUS_CD) ********************
| 00 : 개발확정 | 01 : 검증중 | 02 : 검증완료 | 03 : 배포완료 | 04 : 배포취소 | 05 : 배포삭제	 |
**********************************************************************************/
	
/***** 예외처리방법코드 (EXCEPTION_PRC_METH_CD) ***** 
| 1 : 요청 | 2 : 처리완료 | 3 : 요청취소 | 4 : 처리반려 |	 
*************************************************/
	
	if($("#submit_form #user_auth_id").val() != '4' && $("#submit_form #user_auth_id").val() != '5' && $("#submit_form #user_auth_id").val() != '6'){
		//개발자(X) && 업무개발_배포성능관리자(X) && 배포성능관리자(X) 
		$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
		$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
		$("#exec_request_form #btn_request").linkbutton({disabled:true});
		$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
		$("#exec_request_form #btn_reset").linkbutton({disabled:true});

	}else if($("#submit_form #user_auth_id").val() == '4' && $("#submit_form #user_id").val() != row.deploy_requester_id){
		//개발자(O) && 자신요청(X)
		$("#exec_request_form #exception_request_detail_why").textbox({disabled:true});
		$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
		$("#exec_request_form #btn_request").linkbutton({disabled:true});
		$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
		$("#exec_request_form #btn_reset").linkbutton({disabled:true});
	
	}else if(($("#submit_form #user_auth_id").val() == '4' ||$("#submit_form #user_auth_id").val() == '5' ||
			 $("#submit_form #user_auth_id").val() == '6') && row.step_deploy_check_status_cd == '2'){
			//[개발자(O) && 자신요청(O)] 개발자(O) 업무개발_배포성능관리자(O) 배포성능관리자(O) 검증완료(O)
		
		if((row.perf_check_result_div_cd == 'C' || row.perf_check_result_div_cd == 'B' || row.perf_check_result_div_cd == '9') &&
			row.exception_prc_status_cd != '1' &&  $("#detail_form #howToWay").val() == 'B'){
			//[개발자(O) && 자신요청(O)]
			//(오류(O) || 부적합(O) || 미수행(O)) && 요청중(X) && 개별예외요청
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:false});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:false});
			$("#exec_request_form #exception_request_detail_why").textbox({readonly:false});
			$("#exec_request_form #btn_request").linkbutton({disabled:false});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
			$("#exec_request_form #btn_reset").linkbutton({disabled:false});
		}else if((row.perf_check_result_div_cd == 'C' || row.perf_check_result_div_cd == 'B' || row.perf_check_result_div_cd == '9') && 
				  row.exception_prc_status_cd == '1' &&  $("#detail_form #howToWay").val() == 'A'){
			//[개발자(O) && 자신요청(O)]
			//(오류(O) || 부적합(O) || 미수행(O)) && 요청중(O) && 개별예외요청
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
			$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
			$("#exec_request_form #btn_request").linkbutton({disabled:true});
			$("#exec_request_form #btn_cancel").linkbutton( {disabled:false});
			$("#exec_request_form #btn_reset").linkbutton({disabled:true});
		}else if(row.exception_prc_status_cd != 1 && row.perf_check_result_div_cd != 'A'){
			//20190611 오류도 요청 가능하게 함.
			//[개발자(O) && 자신요청(O)]
			//요청중(X) && 적합(X)
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:false});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:false});
			$("#exec_request_form #exception_request_detail_why").textbox({readonly:false});
			$("#exec_request_form #btn_request").linkbutton({disabled:false});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
			$("#exec_request_form #btn_reset").linkbutton({disabled:false});
		}else if(row.perf_check_result_div_cd == 'A'){  
			//[개발자(O) && 자신요청(O)]
			//적합(O)
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
			$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
			$("#exec_request_form #btn_request").linkbutton({disabled:true});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
			$("#exec_request_form #btn_reset").linkbutton({disabled:true});
		}else{
			//[개발자(O) && 자신요청(O)] 
			$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
			$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
			$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
			$("#exec_request_form #btn_request").linkbutton({disabled:true});
			$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
			$("#exec_request_form #btn_reset").linkbutton({disabled:true});
		}
		
	}else{

		$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
		$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
		$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
		$("#exec_request_form #btn_request").linkbutton({disabled:true});
		$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
		$("#exec_request_form #btn_reset").linkbutton({disabled:true});
	}

};


function Btn_ResetBtnAll(){
	$("#exec_request_form #exception_request_why_cd").combobox({readonly:true});
	$("#exec_request_form #exception_prc_meth_cd").combobox({readonly:true});
	$("#exec_request_form #exception_request_detail_why").textbox({readonly:true});
	
	$("#exec_request_form #btn_request").linkbutton({disabled:true});
	$("#exec_request_form #btn_cancel").linkbutton({disabled:true});
	$("#exec_request_form #btn_reset").linkbutton({disabled:true});
	
};


/*function getDeployPerfChkPlantable(){

	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 요청"," ");
	
	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkPlanTableList",
			$("#detail_form"),
			"POST",
			callback_getDeployPerfChkPlanTableListAction);
	ajaxCall("/getDeployPerfChkPlanTable",
			$("#detail_form"),
			"POST",
			callback_getDeployPerfChkPlanTableListAction);
	
};

var callback_getDeployPerfChkPlanTableListAction = function(result) {
	parent.$.messager.progress('close');
	$('#treePlan').tree("loadData", []);
	console.log("result.result:",result);
	if(result.result){		
		var data = JSON.parse(result.txtValue);
		$('#treePlan').tree("loadData", data);
		console.log(data);
		
		$('.help').tooltip({
			position: 'right'
		});
	}else{
		parent.$.messager.alert('ERROR',result.message,'error', function(){
			$('#treePlan').tree("loadData", []);	
		});
	}
};*/

function getPerfCheckResultTableList(){
	$("#perfCheckResultList").datagrid({
		view: myview,
		columns:[
//			[
//			{field:'perf_check_indc_nm',title:'검증 지표',width:"18%",halign:"center",align:'center'},
//			{field:'indc_pass_max_value',title:'적합',width:"10%",halign:"center",align:'center'},
//			{field:'not_pass_pass',title:'부적합',width:"10%",halign:"center",align:'center'},
////			{field:'indc_yn_decide_div_cd_nm',title:'여부값 판정구분',width:"14%",halign:"center",align:'center'},
//			{field:'exec_result_value',title:'성능 검증 결과값',width:"10%",halign:"center",align:'center'},
//			{field:'perf_check_result_div_cd_nm',title:'성능 검증 결과',width:"10%",halign:"center",align:'center', styler:cellStyler2},
//			{field:'except_reg_yn',title:'예외등록여부',width:"10%",halign:"center",align:'center'},
//			{field:'perf_check_result_desc',title:'성능검증 결과내용',width:"33%",halign:"center",align:'left'},
//			{field:'perf_check_indc_id',hidden:true},
//			{field:'indc_yn_decide_div_cd',hidden:true},
//			{field:'perf_check_result_div_cd',hidden:true},
//			{field:'perf_check_meth_cd',hidden:true}
//			]
				[
					{field:'perf_check_indc_nm',title:'검증지표',width:"18%",halign:"center",align:'center',rowspan:2},
					{field:'indc_pass_max_value',title:'적합',width:"11%",halign:"center",align:'center',rowspan:2},
					{field:'exec_result_value',title:'성능검증결과값',width:"15%",halign:"center",align:'center',colspan:2},
					{field:'perf_check_result_div_cd_nm',title:'성능검증결과',width:"11%",halign:"center",align:'center', styler:cellStyler2,rowspan:2},
					{field:'exception_yn',title:'예외등록여부',width:"11%",halign:"center",align:'center',rowspan:2},
					{field:'perf_check_result_desc',title:'성능검증결과내용',width:"34%",halign:"center",align:'left',rowspan:2},
				],
				[
					{field:'avg_exec_result_value',title:'평균',width:"7.5%",halign:"center",align:'center', styler:cellStyler3},
					{field:'max_exec_result_value',title:'최대',width:"7.5%",halign:"center",align:'center', styler:cellStyler4},
					{field:'perf_check_indc_id',hidden:true},
					{field:'indc_yn_decide_div_cd',hidden:true},
					{field:'perf_check_result_div_cd',hidden:true},
					{field:'perf_check_meth_cd',hidden:true}
				]
			],
			onClickRow : function(index,row,event) {
				popupOpen($("#detail_form"));
			},		
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
};

function ajaxCallGetResultList(){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 요청"," ");
	
	ajaxCall("/ExecPerformanceCheckIndex/PerfCheckResultTableList",
			$("#detail_form"),
			"POST",
			callback_getPerfCheckResultTableListAction);
}

var callback_getPerfCheckResultTableListAction = function(result) {
	console.log("check");
	var jsonResult = JSON.parse(result);	

	jsonResult.rows.forEach(function(element){

		if(element.perf_check_result_div_nm){
			element.perf_check_result_div_cd_nm = element.perf_check_result_div_nm;
		}
	});

	result = JSON.stringify(jsonResult);
	json_string_callback_common(result,'#perfCheckResultList',true);
};

function getBindTableList(){
	
	$("#bindTableList").datagrid({
		view: myview,
		columns:[[
			{field:'bind_seq',title:'BIND_SEQ',width:"20%",halign:"center",align:'center'},
			{field:'bind_var_nm',title:'BIND_VAR_NM',width:"20%",halign:"center",align:'center'},
			{field:'bind_var_value',title:'BIND_VAR_VALUE',width:"20%",halign:"center",align:'center'},
			{field:'bind_var_type',title:'BIND_VAR_TYPE',width:"20%",halign:"center",align:'center'}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Bind List"," ");
	
	ajaxCall("/ExecPerformanceCheckIndex/BindTableList",
			$("#detail_form"),
			"POST",
			callback_getBindTableListAction);
	
};
var callback_getBindTableListAction = function(result) {
	json_string_callback_common(result,'#bindTableList',true);
};

//예외 요청 처리 상태 초기화 함
function resetExceptionStatus(){
	if(user_auth_id == 4){
		$("#search_exception_prc_status_cd").combobox('setValue',5);
	} else {
		$("#search_exception_prc_status_cd").combobox('setValue',1);
	}
}

//예외처리요청상태값 변경
function fnchangevalue(val){
	$("#search_exception_prc_status_cd").combobox('setValue',val);
	return true;
}

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
		return false;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
	}
	
	call_from_parent = '';
	$("#call_from_parent").val('');
	call_from_child = '';
	$("#call_from_child").val('');
	
	Btn_ResetFieldAll();
	createExceptionHandlingTab_reload();
	fnSearch();
}

function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 예외 요청"," ");
	
	
//	alert($("#detail_form #user_auth_id").val());
	//무조건 개발자는 
	if($("#submit_form #user_auth_id").val() == '4'){  //개발자라면
		$("#submit_form #search_user_id").val($("#submit_form #user_id").val());
	}
	
	ajaxCall("/ExecPerformanceCheckIndex/ProPerfExcReq",
			$("#submit_form"),
			"POST",
			callback_ProPerfExecReqAction);
}

//callback 함수
var callback_ProPerfExecReqAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);  //페이버튼세팅
	$("#detail_form #dbid").val(dbid);	//sql runner 를 실행하기위해 dbid 값을 세팅해줌.

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
	
	if($('#submit_form #search_from_deploy_request_dt').textbox('getValue') == ""){
		parent.$.messager.alert('','배포요청일시 검색 시작일을 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #search_to_deploy_request_dt').textbox('getValue') == ""){
		parent.$.messager.alert('','배포요청일시 검색 종료일을 선택해 주세요.');
		return false;
	}	
	
	return true;
}
/*페이징처리끝*/


//예외요청
function Btn_Request(){
	
	var rows = $('#tableList').datagrid('getChecked');
	
	if($("#howToWay").val() == 'B' && rows.length == 0){
		parent.$.messager.alert('','예외요청할 데이터를 선택해 주세요.');
		return false;
	}
	
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
	
	if($("#exec_request_form #exception_request_detail_why").val() == ""){
		parent.$.messager.alert('','예외 요청 상세 사유를 입력해 주세요.');
		return false;
	}
	
	if($("#exec_request_form #exception_request_detail_why").val().length > 200){
		parent.$.messager.alert('','예외 요청 상세 사유를 200자 이내로 입력해 주세요.');
		return false;
	}

//	alert($("#detail_form #perf_check_result_div_cd").val());
//	alert($("#exec_request_form #exception_prc_meth_cd").combobox("getValue"));

	//20190530 추가 지표단위일경우 오류 또는 미수행건으로 예외요청 불가능
	if(($("#detail_form #perf_check_result_div_cd").val() == "C" || $("#detail_form #perf_check_result_div_cd").val() == "9") && $("#exec_request_form #exception_prc_meth_cd").combobox("getValue") == "1"){
		parent.$.messager.alert('','성능검증 결과가 [미수행] 일 경우 지표단위로 예외요청 할 수 없습니다.');
		return false;
	}
	

		//개별 예외요청
		if($("#howToWay").val() == 'A'){
		
			if($("#detail_form #perf_check_result_div_cd").val() == "9"){
				parent.$.messager.confirm('', '성능검증결과가 미수행건입니다. <br/>그래도 예외요청을 하시겠습니까?', function(check) {
					if(check){
						ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd2",$("#detail_form"),"POST",callback_checkExceptionPrcStatusCdAction);	
					}
				});
			}else{
				//ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd",
				ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd2",$("#detail_form"),"POST",callback_checkExceptionPrcStatusCdAction);
			}
		
			
		//일괄 예외요청
		}else if($("#howToWay").val() == 'B'){
			var rows = $('#tableList').datagrid('getChecked');
			
			//20190530 추가 지표단위일경우 오류 또는 미수행건으로 예외요청 불가능
			for(var i = 0; i < rows.length; i++){
				if(rows[i].perf_check_result_div_cd == '9' || rows[i].perf_check_result_div_cd == 'C'){
					if($("#exec_request_form #exception_prc_meth_cd").combobox("getValue") == "1"){
						parent.$.messager.alert('','성능검증 결과가 [미수행] 일경우 지표단위로 예외요청 할 수 없습니다.');
						return false;
					}
				}
			}
			
			for(var i = 0; i < rows.length; i++){
				if(rows[i].perf_check_result_div_cd == '9'){
				//미수행건이 있다면 한번 물어봄.
					parent.$.messager.confirm('', '[미수행] 된 성능검증결과 건이 존재합니다. 그래도 예외요청을 하시겠습니까?', function(check) {
						if(check){
							multiExceptionRequest(); //일괄 예외요청
						}
					});
					break;
					
				}
				
				if(rows.length-1 == i){
//					console.log("isPassed", "true");
					multiExceptionRequest(); //일괄 예외요청
				}
				
			}//for
			
		//일괄 예외처리
		}else if($("#howToWay").val() == 'C'){
			
		}
	
};


//일괄 예외요청
function multiExceptionRequest(){
	var rows = $('#tableList').datagrid('getChecked');
	
	var program_nm = new Array();
	var dbio = new Array();
	var deploy_id = new Array();
		
	var perf_check_id = new Array();
	var program_id = new Array();
	var exception_requester_id = new Array();
//	var exception_request_why_cd = new Array();  //input값
//	var exception_request_detail_why = new Array();
//	var exception_prc_meth_cd = new Array();  // input값
	var excepter_id = new Array();
	var exception_prc_why = new Array();
	var exception_prc_dt = new Array();
	var last_perf_check_step_id = new Array();
		
//		console.log("row값 확인?",rows);
		//루프를 돌면서 파라미터 저장
		for(var i = 0; i < rows.length; i++){
			//예외요청 실패할경우 사용자에게 보여주기위한 파라미터
			program_nm.push(rows[i].program_nm);
			dbio.push(rows[i].dbio);
			deploy_id.push(rows[i].deploy_id);

			//DB에 저장할 파라미터
			perf_check_id.push(rows[i].perf_check_id);
			program_id.push(rows[i].program_id);
			exception_requester_id.push(rows[i].exception_requester_id);
//			exception_request_why_cd.push(rows[i].exception_request_why_cd);  //input창에서 임력받음
//			exception_request_detail_why.push(rows[i].exception_request_detail_why);
//			exception_prc_meth_cd.push(rows[i].exception_prc_meth_cd);
			excepter_id.push(rows[i].excepter_id);
			exception_prc_why.push(rows[i].exception_prc_why);
			exception_prc_dt.push(rows[i].exception_prc_dt);
			last_perf_check_step_id.push(rows[i].last_perf_check_step_id);
		}
		
		//보낼 파라미터를 저장
		$("#detail_form #program_nm[type=hidden]").val(program_nm.join(","));
		$("#detail_form #dbio[type=hidden]").val(dbio.join(","));
		$("#detail_form #deploy_id[type=hidden]").val(deploy_id.join(","));
		$("#detail_form #perf_check_id[type=hidden]").val(perf_check_id.join(","));

//		$("#perf_check_id").val(perf_check_id.join(","));
		$("#program_id").val(program_id.join(","));
		$("#exception_requester_id").val(exception_requester_id.join(","));
//		$("#exception_request_why_cd").val(exception_request_why_cd.join(","));
//		$("#exception_request_detail_why").val(exception_request_detail_why.join(","));
//		$("#exception_prc_meth_cd").val(exception_prc_meth_cd.join(","));
		$("#excepter_id").val(excepter_id.join(","));
		$("#exception_prc_why").val(exception_prc_why.join(","));
		$("#exception_prc_dt").val(exception_prc_dt.join(","));
		$("#last_perf_check_step_id").val(last_perf_check_step_id.join(","));
		
/*		console.log("넣은값 program_nm :::"+$("#program_nm").textbox("getValue"));
		console.log("넣은값 dbio :::"+$("#dbio").textbox("getValue"));
		console.log("넣은값 deploy_id :::"+$("#deploy_id").textbox("getValue"));
		console.log("넣은값 perf_check_id :::"+$("#perf_check_id").val());
		console.log("넣은값 program_id :::"+$("#program_id").textbox("getValue"));
		console.log("넣은값 exception_requester_id :::"+$("#exception_requester_id").val())
		console.log("넣은값 exception_request_why_cd(한개공통) :::"+$("#exception_request_why_cd").val())
		console.log("넣은값 exception_request_detail_why(한개공통) :::"+$("#exception_request_detail_why").val())
		console.log("넣은값 exception_prc_meth_cd(한개공통) :::"+$("#exception_prc_meth_cd").val())
		console.log("넣은값 excepter_id :::"+$("#excepter_id").val())
		console.log("넣은값 exception_prc_why :::"+$("#exception_prc_why").val())
		console.log("넣은값 exception_prc_dt :::"+$("#exception_prc_dt").val())
		console.log("넣은값 last_perf_check_step_id :::"+$("#last_perf_check_step_id").val())*/
		
		ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/MultiRequest",
				$("#detail_form"),
				"POST",
				callback_MultiRequestSettingAction);	
	
}
//일괄 예외처리
function multiExceptionHandling(excepter_id, exception_prc_why){
	var strTest = "";

	//예외처리에 필요한 파라미터
	var excepter_id = excepter_id;  //input값
	var exception_prc_why = exception_prc_why; //input값 
	
	var program_nm = new Array();
	var perf_check_id = new Array();
	var exception_prc_status_cd = new Array();
	var exception_request_id = new Array();
	var perf_check_auto_pass_yn = new Array();
	var program_id = new Array();
	var exception_prc_meth_cd = new Array();
	var deploy_id = new Array();
	rows = $('#tableList').datagrid('getChecked');
	for(var i = 0; i < rows.length; i++){
		
//		console.log(rows[i].exception_prc_status_cd);
//		console.log(rows[i].exception_request_id);
//		console.log(rows[i].perf_check_auto_pass_yn);
//		console.log(rows[i].program_id);
//		console.log(rows[i].exception_prc_meth_cd);
		deploy_id.push(rows[i].deploy_id);
		program_nm.push(rows[i].program_nm);
		perf_check_id.push(rows[i].perf_check_id);
		exception_prc_status_cd.push(rows[i].exception_prc_status_cd);
		exception_request_id.push(rows[i].exception_request_id);
		perf_check_auto_pass_yn.push(rows[i].perf_check_auto_pass_yn);
		program_id.push(rows[i].program_id);
		exception_prc_meth_cd.push(rows[i].exception_prc_meth_cd);
	}
	//보낼 파라미터를 저장
	$("#detail_form #excepter_id").val(excepter_id);
	$("#detail_form #exception_prc_why").val(exception_prc_why);
	
	$("#detail_form #deploy_id[type=hidden]").val(deploy_id.join(','));
	$("#detail_form #perf_check_id[type=hidden]").val(perf_check_id.join(","));
	$("#detail_form #program_nm[type=hidden]").val(program_nm.join(","));
	$("#detail_form #exception_prc_status_cd").val(exception_prc_status_cd.join(","));
	$("#exception_request_id").val(exception_request_id.join(","));
	$("#perf_check_auto_pass_yn").val(perf_check_auto_pass_yn.join(","));
	$("#program_id").val(program_id.join(","));
	$("#detail_form #exception_prc_meth_cd").val(exception_prc_meth_cd.join(","));
	
	
	console.log("넣은값 excepter_id(공통) :::"+$("#detail_form #excepter_id").val());
	console.log("넣은값 exception_prc_why(공통) :::"+$("#detail_form #exception_prc_why").val());
	console.log("넣은값 exception_request_id :::"+$("#detail_form #exception_request_id").val());
	console.log("넣은값 perf_check_id:::"+$("#detail_form #perf_check_id").val());
	console.log("넣은값 program_nm :::"+$("#detail_form #program_nm[type=hidden]").val());
	console.log("넣은값 exception_prc_status_cd :::"+$("#detail_form #exception_prc_status_cd").val());
	console.log("넣은값 perf_check_auto_pass_yn :::"+$("#perf_check_auto_pass_yn").val());
	console.log("넣은값 exception_request_id :::"+$("#exception_request_id").val());
	console.log("넣은값 program_id :::"+$("#program_id").val());
	console.log("넣은값 exception_prc_meth_cd :::"+$("#detail_form #exception_prc_meth_cd").val());
	
	top.openMessageProgressTypeB("성능 검증 예외 처리","예외처리 중입니다.",100);
	ajaxCall("/ExecPerformanceCheckIndex/DeployPerfChkExcptRequest/multiExceptionHandling",
			$("#detail_form"),
			"POST",
			callback_MultiExceptionHandlingAction);	
};

//callback 함수(일괄)
var callback_MultiExceptionHandlingAction = function(result) {
	top.closeMessageProgress()

	if(result.result){
		//요청중인건이 있었을 시
		if(result.txtValue != null && result.txtValue != ""){
			parent.$.messager.alert('',result.txtValue,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
					Btn_ResetFieldAll();
					createExceptionHandlingTab_reload();
				},1000);	
			});
		//없었을시
		}else{
			parent.$.messager.alert('','일괄 처리되었습니다.','info',function(){
				setTimeout(function() {
					Btn_OnClick();
					Btn_ResetFieldAll();
					createExceptionHandlingTab_reload();
				},1000);	
			});
		}
		
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_checkExceptionPrcStatusCdAction = function(result) {
	if(result.result){
		if(result.txtValue != null && result.txtValue != ''){
			parent.$.messager.alert('',result.txtValue);
	
		}else{
//			rows = $('#tableList').datagrid('getSelected');
			
			if($("#detail_form #perf_check_auto_pass_yn").val() == 'Y' && $("#detail_form #exception_prc_meth_cd").val() == '1'){
					parent.$.messager.alert('','성능 검증제외에서 지표단위검증으로<br/>변경 요청하셨습니다. 검증제외 삭제이므로 <br/>[성능 검증 예외 삭제] 메뉴를 통해 처리 바랍니다.');

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
//				fnChangeValue(1);  //예외처리요청상태 : 요청중 으로 변경 (20190307 보류)
				Btn_OnClick();
				Btn_ResetFieldAll();
//				Btn_ShowTab(2);
//				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};
//callback 함수(일괄)
var callback_MultiRequestSettingAction = function(result) {
	if(result.result){
		
		//요청중인건이 있었을 시
		if(result.txtValue != null && result.txtValue != ""){
			parent.$.messager.alert('',result.txtValue,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
					Btn_ResetFieldAll();
				},1000);	
			});

		//없었을시
		}else{
			parent.$.messager.alert('','일괄 처리되었습니다.','info',function(){
				setTimeout(function() {
					Btn_OnClick();
					Btn_ResetFieldAll();
				},1000);	
			});
		}
		
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
	
	var rows = $('#tableList').datagrid('getSelected');
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
		return false;	
	}
};
//callback 함수
var callback_CancelSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
//				fnChangeValue(3);//요청취소     (보류 20190307)
				Btn_OnClick();
				Btn_ResetFieldAll();
//				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};
function createExceptionHandlingTab(row){
	$("#detail_form #program_source_desc").val("");
	$("#detail_form #exception_request_detail_why").val("");
//	console.log("ifram params:::"+$("#detail_form").serialize());
	if(!row &&
			($('#tableList').datagrid('getSelections') && $('#tableList').datagrid('getSelections').length === 0)){
		createExceptionHandlingTab_reload();
	}else{
		document.getElementById('exec_handling').src = "/ExecPerformanceCheckIndex/ExceptionHandling/CreateTab?"+$("#detail_form").serialize();
	}
	
};
function createExceptionHandlingTab_reload(){
	document.getElementById('exec_handling').src = "/ExecPerformanceCheckIndex/ExceptionHandling/CreateTab";
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
	$("#detail_form #last_perf_check_step_id").val(selRow.last_perf_check_step_id);
	$("#detail_form #wrkjob_cd").val(selRow.wrkjob_cd);
	$("#detail_form #exception_request_why").val(selRow.exception_request_why);
	$("#detail_form #exception_prc_dt").val(selRow.exception_prc_dt);
	$("#detail_form #exception_prc_status_cd_nm").val(selRow.exception_prc_status_cd_nm);
	
	$("#detail_form #perf_check_auto_pass_yn").val(selRow.perf_check_auto_pass_yn);
	
	if(selRow.exception_prc_meth_cd === '2' || selRow.exception_prc_meth_cd === '3'){
		$("#detail_form #exception_prc_meth_cd_nm").html(selRow.exception_prc_meth_cd_nm);
	}else{
		$("#detail_form #exception_prc_meth_cd_nm").html("");
	}
	
	$("#detail_form #exception_prc_why").val(selRow.exception_prc_why);
	$("#detail_form #perf_check_auto_pass_del_yn").val(selRow.perf_check_auto_pass_del_yn);
//	$("#detail_form #perf_check_result_div_cd_nm").val(selRow.perf_check_result_div_nm);

	$("#detail_form #deploy_id[type=id]").val(selRow.deploy_id);
	$("#detail_form td[id=deploy_id]").html(selRow.deploy_id);

	$("#detail_form #deploy_nm").html(selRow.deploy_nm);
	$("#detail_form #deploy_check_status_cd_nm").html(selRow.deploy_check_status_cd_nm);
	$("#detail_form #deploy_expected_day").html(getDateFormat(selRow.deploy_expected_day));
	$("#detail_form #program_id").val(selRow.program_id);

	$("#detail_form #program_nm[type=hidden]").val(selRow.program_nm);
	$("#detail_form td[id=program_nm]").html(selRow.program_nm);

	$("#detail_form #dbio[type=hidden]").val(selRow.dbio);
	$("#detail_form td[id=dbio]").html(selRow.dbio);

	$("#detail_form #file_nm").html(selRow.file_nm);
	$("#detail_form #dir_nm").html(selRow.dir_nm);
	$("#detail_form #program_execute_tms").val(selRow.program_execute_tms);
	$("#detail_form #program_desc").html(selRow.program_desc);
	$("#detail_form #program_type_cd").val(selRow.program_type_cd);
	$("#detail_form #program_type_cd_nm").html(selRow.program_type_cd_nm);
	$("#detail_form #sql_command_type_cd").html(selRow.sql_command_type_cd);
	$("#detail_form #program_type_cd_nm").val(selRow.program_type_cd_nm);
	$("#detail_form #sql_command_type_cd").val(selRow.sql_command_type_cd);
	$("#detail_form #dynamic_sql_yn").html(selRow.dynamic_sql_yn);
	$("#detail_form #perf_check_result_div_cd_nm").val(selRow.perf_check_result_div_cd_nm);

	$("#detail_form #reg_dt").html(selRow.reg_dt);
	$("#detail_form #last_update_dt").html(selRow.last_update_dt);
	$("#detail_form #program_div_cd_nm").html(selRow.program_div_cd_nm);
	$("#detail_form #exception_prc_meth_cd_nm").html(selRow.exception_prc_meth_cd_nm);
	$("#detail_form #deploy_id[type=hidden]").html(selRow.deploy_id);

	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue",selRow.wrkjob_cd_nm);
	$("#exec_request_form #dbio[type=hidden]").val(selRow.dbio);
	$("#dbio td[id=dbio]").html(selRow.dbio);
	
	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue", selRow.wrkjob_cd_nm);
	$("#exec_request_form #program_nm").textbox("setValue", selRow.program_nm);
	$("#exec_request_form #dbio").textbox("setValue", selRow.dbio);

	$("#exec_request_form #exception_request_why_cd").combobox("setValue", selRow.exception_request_why_cd);
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", selRow.exception_prc_meth_cd);
	
	$("#detail_form #except_processor").val(selRow.except_processor);
	$("#detail_form #exception_prc_dt").val(selRow.exception_prc_dt);
	$("#detail_form #exception_prc_status_cd").val(selRow.exception_prc_status_cd);
	$("#detail_form #exception_request_why_cd_nm").val(selRow.exception_request_why_cd_nm);
	$("#exec_request_form #btn_request").attr("disabled","false");
	
	if(tab_index == 0){
		console.log("tab0");
		getProgramSourceDesc();
//		getBindTableList();
		getPerfCheckResultTableList();
	}else if(tab_index == 1){
		console.log("tab1");
		getPerfCheckResultTableList(); //온라인
	}else if(tab_index == 2){
		console.log("tab2");
		createExceptionHandlingTab(selRow);  //탭을 생성한다.
	}

	if($(".tabs .tabs-selected").index()){
		//예외 요청 탭
		if($(".tabs .tabs-selected").index() === 1){
			ajaxCallGetDefaultParsingSchemaInfo();
			ajaxCallGetResultList();	
		}
	}
//	ajaxCallGetDefaultParsingSchemaInfo();
//	ajaxCallGetResultList();
//	
	ajaxCallGetExecPlan();
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

function ajaxCallGetExecPlan(){
	
	let sql_command_type_cd = $("#detail_form #sql_command_type_cd").val();
	
	let jData = {
			"program_id" :$("#detail_form #program_id").val()
			,"perf_check_step_id" :$("#detail_form #perf_check_step_id").val()
			,"perf_check_id":$("#detail_form #perf_check_id").val()
			,"program_execute_tms":$("#detail_form #program_execute_tms").val()
			,"sql_command_type_cd": sql_command_type_cd
	}
	
	ajaxCallWithJson("/ExecPerformanceCheckIndex/ProPerfExcReq/getExecPlan",
			jData,
			"POST",
			callback_setExecPlan);
}

var callback_setExecPlan = function(result){
	$("#ta_exec_plan").val("");
	$('#treePlan').tree("loadData", []);
	
	let sql_command_type_cd = $("#detail_form #sql_command_type_cd").val();
	
	try{
		if(sql_command_type_cd == "SELECT"){
			if(result.executionPlan){
				if (result.executionPlan.exec_plan){
					$("#ta_exec_plan").val(result.executionPlan.exec_plan);
				}
			}
			
		}else {
			console.log("sql Type : "+sql_command_type_cd);
			var data = JSON.parse(result.result.txtValue);
			if(result.result){		
				$('#treePlan').tree("loadData", data);
				
			}else{
				if(result.message != undefined){
					parent.$.messager.alert('ERROR',result.message,'error', function(){
						$('#treePlan').tree("loadData", []);	
					});
				}else{
					parent.$.messager.alert('ERROR',"성능 검증 결과 조회중 오류가 발생했습니다.",'error', function(){
						$('#treePlan').tree("loadData", []);	
					});
				}
			}
		}
		
	} catch(e){
		parent.$.messager.alert('',e.message);
	}
	
}

function Btn_ResetField(){
	
	$("#exec_request_form #exception_request_why_cd").combobox("setValue", "");
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", "");
	$("#exec_request_form #exception_requester").textbox("setValue", $("#submit_form #user_nm").val());
	$("#exec_request_form #exception_request_detail_why").textbox("setValue", "");
}

function Btn_ResetFieldAll(){
	
	$("#detail_form #deploy_id[type=hidden]").html("");
	$("#detail_form #deploy_nm").html("");
	$("#detail_form #deploy_check_status_cd_nm").html("");
	$("#detail_form #deploy_expected_day").html("");
	$("#detail_form #program_id").html("");
	$("#detail_form td[id=program_nm]").html("");
	$("#detail_form #dbio").html("");
	$("#detail_form #dbio").html("");
	$("#detail_form #file_nm").html("");
	$("#detail_form #dir_nm").html("");
	$("#detail_form #program_execute_tms").html("");
	$("#detail_form #program_desc").html("");
	$("#detail_form #program_source_desc").val("");
	$('#detail_form #program_div_cd_nm').html('')
	$('#detail_form #deploy_id').html('')


	$('#detail_form #exception_prc_meth_cd_nm').html('')

	$('#detail_form #program_type_cd_nm ').html('')
	$('#detail_form #sql_command_type_cd').html('')
	$('#detail_form #dynamic_sql_yn ').html('')
	$('#detail_form #reg_dt ').html('')
	$('#detail_form #last_update_dt ').html('')
	$('#detail_form #perf_check_auto_pass_yn_chk').val('')
	$("#detail_form #perf_check_auto_pass_yn_chk").checkbox({checked:false});
	
	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue", "");
	$("#exec_request_form #wrkjob_cd_nm").textbox("setValue", "");
	$("#exec_request_form #dbio").textbox("setValue", "");
	$("#exec_request_form #program_nm").textbox("setValue", "");
	$("#exec_request_form #exception_request_why_cd").combobox("setValue", "");
	$("#exec_request_form #exception_prc_meth_cd").combobox("setValue", "");
	$("#exec_request_form #exception_requester").textbox("setValue", "");
	$("#exec_request_form #exception_request_detail_why").textbox("setValue", "");
	$('#bindTableList').datagrid("loadData", []);
	$('#perfCheckResultTableList').datagrid("loadData", []);
	$("#perfCheckResultList").datagrid({}
	
	)
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

	$("#submit_form").attr("action","/ExecPerformanceCheckIndex/ProPerfExcReq/ExcelDown");
	$("#submit_form").submit();
}

function Btn_ShowTab(strIndex){
	$('#').tabs('select', strIndex);
}

function Btn_SetSQLFormatterEspc(){
	$('#program_source_desc').format({method: 'sql'});
}
function cellStyler(value,row,index){
	if(row.perf_check_result_div_cd_nm == '적합' || row.perf_check_result_div_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '부적합' || row.perf_check_result_div_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '오류' || row.perf_check_result_div_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '미수행' || row.perf_check_result_div_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(row.perf_check_result_div_cd_nm == '검증제외' || row.perf_check_result_div_nm == '검증제외'){
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
	}else if(row.perf_check_result_div_cd_nm == '검증제외'){
		return 'background-color:#012753;color:white;';
	}
}

function cellStyler3(value,row,index){
	if(row.perf_check_evaluation_meth_cd === "2"){	//평균
		return 'background-color:#00b0f0;color:white;';
	}
}
function cellStyler4(value,row,index){
	if (row.perf_check_evaluation_meth_cd === "3"){			//최대
		return 'background-color:#00b0f0;color:white;';
	}
function cellStyler5(value,row,index){
		return 'background-color:white; border:none;';
	}
}
function cellStyler_popup(value,row,index){
	if(row.perf_check_result_div_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(row.perf_check_result_div_nm == '검증제외'){
		return 'background-color:#012753;color:white;';
	}
}
