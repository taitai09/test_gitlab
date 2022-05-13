var tuningCaseIsFirstOrNotCount = 0;
$(document).ready(function(){
	$("body").css("visibility", "visible");
	
	$('.sqlCopyBtn').on('click', function(){
		let clipboardId = $(this).data('clipboard-target');
		let elementdId = $(this).attr('id');
		
		let editor;
		if ( clipboardId == '#impr_sql_text_h' ){
			editor = CKEDITOR.instances.impr_sql_text;
			
		} else if ( clipboardId == '#imprb_exec_plan_h' ){
			editor = CKEDITOR.instances.imprb_exec_plan;
			
		} else if ( clipboardId == '#impra_exec_plan_h' ){
			editor = CKEDITOR.instances.impra_exec_plan;
			
		} else {
			return;
		}
		
		let sqlText = editor.getData();
			sqlText = (sqlText) ? sqlText : '';
		
		$(clipboardId).val(parent.formatHTML(sqlText));
		clipboardId = clipboardId.replace("#",'');
		
		let copyText = document.getElementById(clipboardId);
		copyText.focus();
		copyText.select();
		document.execCommand("Copy");
		copyText.setSelectionRange(0, 0);
		copyText.scrollTop = 0;
		
		parent.$.messager.alert('','복사되었습니다.');
	});
	
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사되었습니다.');
	});
	
//	var clipImprSql = new Clipboard('#imprSqlCopyBtn',{
//		text: function() {
//			return $("#impr_sql_text_h").val();
//		}
//	});	
//	clipImprSql.on('success', function(e) {
//		parent.$.messager.alert('','개선 SQL TEXT가 복사되었습니다.');
//	});
//	
//	var clipImprbExec = new Clipboard('#imprbExecCopyBtn',{
//		text: function() {
//			return $("#imprb_exec_plan_h").val();
//		}
//	});	
//	clipImprbExec.on('success', function(e) {
//		parent.$.messager.alert('','개선전 실행계획이 복사되었습니다.');
//	});
//	
//	var clipImpraExec = new Clipboard('#impraExecCopyBtn',{
//		text: function() {
//			return $("#impra_exec_plan_h").val();
//		}
//	});	
//	clipImpraExec.on('success', function(e) {
//		parent.$.messager.alert('','개선후 실행계획이 복사되었습니다.');
//	});
	
	//CKEDITOR 주석처리 2018-11-09
	/* CKEDITOR 주석다시 풀기 2018-12-26 5개 화면이 모두 CKEditor를 사용하기 때문에 이 성능개선관리상세 화면만 TextArea로 변경할 수 없음.*/
	var status_cd = $("#sqlTuningStatusCd").val();
	
	let strArr = $("#imprb_exec_plan").val().split("\n");
	let max = 130;
	
	for (var strIdx = 0; strIdx < strArr.length; strIdx++) {
		if ( strArr[strIdx].length > max ){
			max = strArr[strIdx].length;
		}
	}
	if ( max > 130 ){
		max = max - 130;
	} else {
		max = 0;
	}
	
	if ( status_cd == '5' ) {
		CKEDITOR.replace("impr_sql_text",{
			width:"100%",
			height:"300px",
			contentsCss: "body {font-family:'Malgun Gothic', 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic', 'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic; font-size:11px;background-color:beige;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
		
		if ( $("#imprb_exec_plan").val() != null && $("#imprb_exec_plan").val().indexOf("[Tobe Plan]") > -1 ){
			CKEDITOR.replace("imprb_exec_plan",{
				width:"100%",
				height:"300px",
				contentsCss: "body {font-family:'굴림체';font-size:11px;background-color:beige;width:140%;}",
				extraPlugins: 'colorbutton,colordialog,font'
			});
		} else {
			CKEDITOR.replace("imprb_exec_plan",{
				width:"100%",
				height:"300px",
				contentsCss: "body {font-family:'굴림체';font-size:11px;background-color:beige;width:"+(99+max)+"%;}",
				extraPlugins: 'colorbutton,colordialog,font'
			});
		}
		CKEDITOR.replace("impra_exec_plan",{
			width:"100%",
			height:"300px",
			contentsCss: "body {font-family:'굴림체';font-size:11px;background-color:beige;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
	} else {
		CKEDITOR.replace("impr_sql_text",{
			width:"100%",
			height:"300px",
			contentsCss: "body {font-family:'Malgun Gothic', 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic', 'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic; font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
		if ( $("#imprb_exec_plan").val() != null && $("#imprb_exec_plan").val().indexOf("[Tobe Plan]") > -1 ) {
			CKEDITOR.replace("imprb_exec_plan",{
				width:"100%",
				height:"300px",
				contentsCss: "body {font-family:'굴림체';font-size:11px;width:140%;}",
				extraPlugins: 'colorbutton,colordialog,font'
			});
		} else {
			CKEDITOR.replace("imprb_exec_plan",{
				width:"100%",
				height:"300px",
				contentsCss: "body {font-family:'굴림체';font-size:11px;width:"+(99+max)+"%;}",
				extraPlugins: 'colorbutton,colordialog,font'
			});
		}
		CKEDITOR.replace("impra_exec_plan",{
			width:"100%",
			height:"300px",
			contentsCss: "body {font-family:'굴림체';font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
	}
	
	/* html 태크 제거 및 공백 제거*/	
	var impr_sql_text_h = $("#impr_sql_text").val();
	$("#impr_sql_text_h").val(parent.formatHTML(impr_sql_text_h));
	
	var imprb_exec_plan_h = $("#imprb_exec_plan").val();
	$("#imprb_exec_plan_h").val(parent.formatHTML(imprb_exec_plan_h));

	var impra_exec_plan_h = $("#impra_exec_plan").val();
	$("#impra_exec_plan_h").val(parent.formatHTML(impra_exec_plan_h));

	
	// 튜닝담당자 조회
	$('#selectTuner').combobox({
		url:"/Common/getTuner?dbid="+$("#dbid").val(),
		method:"get",
		valueField:'tuner_id',
		textField:'tuner_nm',
		onLoadSuccess: function() {
			$("#selectTuner").combobox("textbox").attr("placeholder","선택");
		}
	});

	// 완료사유 조회
	$('#selectComplete').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1008",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange : function(newValue, oldValue){
			resetCompleteReasonCheckbox();

			$(".4").hide();	
			if(newValue != ""){
				$("."+newValue).show();	
			}
		},
		onLoadSuccess: function() {
			$("#selectComplete").combobox("textbox").attr("placeholder","선택");
		}
	});
	// 완료사유 조회
	$('#tuning_case_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1079&isChoice=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function() {
			$("#tuning_case_type_cd").combobox("textbox").attr("placeholder","선택");
		}
	});
	

	// 종료사유 조회
	$('#selectEnd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1009",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function() {
			$("#selectEnd").combobox("textbox").attr("placeholder","선택");
		}
	});	
	
	//사례 게시 defaul 값을 no로 변경
	//var checkCase = true;
	//if($("#tuning_case_posting_yn").val() == "N") checkCase = false;
	var checkCase = false;
	$("#tuning_case_posting_title").textbox("readonly",true);
	$("#tuning_case_type_cd").combobox("readonly",true);
	$('#chkTuningCase').switchbutton({
		checked: checkCase,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			tuningCaseIsFirstOrNotCount += 1;
			//tuning_case_posting_yn 파라미터가 최초 Y 로 넘어오기때문에 로직수행시 에러.
			//chkTuningCase 를 작동시키지 않았다면 tuningCaseIsFirstOrNotCount 0이 되므로 N 으로 처리
			if(checked){
				$("#tuning_case_type_cd").combobox("readonly",false);
				$("#tuning_case_posting_title").textbox("readonly",false);
				$("#tuning_case_posting_yn").val("Y");
			}else {
				$("#tuning_case_type_cd").combobox("readonly",true);
				$("#tuning_case_posting_title").textbox("readonly",true);
				$("#tuning_case_posting_yn").val("N");
			}
		}
	});
	
	if( $("#tuning_case_posting_title").val() != null && $("#tuning_case_posting_title").val() != "" ){
		$('#chkTuningCase').switchbutton("check");
	} else {
		$('#chkTuningCase').switchbutton("uncheck");
	}
	
	var checkTuner = false;
	if($("#wrkjob_flag").val() == "Y") checkTuner = true;
	
	$('#chkTuner').switchbutton({
		checked: checkTuner,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#wrkjob_mgr_id").val($("#tuning_requester_id").val());
				$("#wrkjob_mgr_nm").textbox('setValue',$("#tuning_requester_nm").val());
				$("#wrkjob_mgr_wrkjob_cd").val($("#tuning_requester_wrkjob_cd").val());
				$("#wrkjob_mgr_wrkjob_nm").textbox('setValue',$("#tuning_requester_wrkjob_nm").val());
				$("#wrkjob_mgr_tel_num").textbox('setValue',$("#tuning_requester_tel_num").val());
				$("#chrTunerSearch").attr('onclick', '').unbind('click');
				$("#chrTunerSearch").attr('style','color:darkgray');
			}else{
				$("#wrkjob_mgr_id").val("");
				$("#wrkjob_mgr_nm").textbox('setValue',"");
				$("#wrkjob_mgr_wrkjob_cd").val("");
				$("#wrkjob_mgr_wrkjob_nm").textbox('setValue',"");
				$("#wrkjob_mgr_tel_num").textbox('setValue',"");
				$("#chrTunerSearch").attr('onclick', 'showWorkTunerPopup();').unbind('click');
				$("#chrTunerSearch").attr('style','color:black');
			}
		}
	});
	
	if ( $("#wrkjob_mgr_nm").val() != null && $("#wrkjob_mgr_nm").val() != "" ) {
		$('#chkTuner').switchbutton("check");
	} else {
		$('#chkTuner').switchbutton("uncheck");
	}
	
	var checkExcept = true;
	if($("#except_target_yn").val() == "N") checkExcept = false;
	
	$('#chkExceptTarget').switchbutton({
		checked: checkExcept,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#except_target_yn").val("Y"); 
			else $("#except_target_yn").val("N");
		}
	});	
	
	console.log("tuning_complete_why_cd :"+$("#tuning_complete_why_cd").val());
	
	$('#selectTuner').combobox('setValue',$("#perfr_id").val());
	$('#selectComplete').combobox('setValue',$("#tuning_complete_why_cd").val());
	$('#selectComplete').combobox('setValue',$("#tuning_complete_why_cd").val());
	$('#tuning_case_type_cd').combobox('setValue',$("#tuning_case_type_cd_temp").val());
	
	$('#selectEnd').combobox('setValue',$("#tuning_end_why_cd").val());
	
	$("#statusViewTab").tabs({
		plain: true,
		onSelect: function(title,index){
			/* 탭을 클릭시 화면을 높이 자동 조절 */
			var height;

			if(index == 2){
				height = $("#container").height() + 35;
			}else{
				height = $("#container").height();
			}
			
			//parent.resizeTopFrame($("#menu_id").val(), height);
		}
	});

	$("#imprb_elap_time").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#impra_elap_time").textbox("getValue"))){
					// elap_time_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($(this).textbox("getValue"),0);
					var afterVal = strParseFloat($("#impra_elap_time").textbox("getValue"),0);

					ratio = (beforeVal - afterVal) / beforeVal * 100; 

					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#elap_time_impr_ratio").textbox("setValue","0");
					}else{
						$("#elap_time_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});
	
	$("#impra_elap_time").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#imprb_elap_time").textbox("getValue"))){
					// elap_time_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($("#imprb_elap_time").textbox("getValue"),0);
					var afterVal = strParseFloat($(this).textbox("getValue"),0);
					
					ratio = (beforeVal - afterVal) / beforeVal * 100; 
					
					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#elap_time_impr_ratio").textbox("setValue","0");
					}else{
						$("#elap_time_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});	
	
	$("#imprb_buffer_cnt").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#impra_buffer_cnt").textbox("getValue"))){
					// buffer_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($(this).textbox("getValue"),0);
					var afterVal = strParseFloat($("#impra_buffer_cnt").textbox("getValue"),0);

					ratio = (beforeVal - afterVal) / beforeVal * 100; 

					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#buffer_impr_ratio").textbox("setValue","0");
					}else{
						$("#buffer_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});
	
	$("#impra_buffer_cnt").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#imprb_buffer_cnt").textbox("getValue"))){
					// buffer_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($("#imprb_buffer_cnt").textbox("getValue"),0);
					var afterVal = strParseFloat($(this).textbox("getValue"),0);
					
					ratio = (beforeVal - afterVal) / beforeVal * 100; 
					
					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#buffer_impr_ratio").textbox("setValue","0");
					}else{
						$("#buffer_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});		
	
	$("#imprb_pga_usage").textbox({
		onChange: function(value) {
			if (!onlyNumChk(value)) {
				$(this).textbox("setValue","");
			} else {
				if (onlyNumChk($("#impra_pga_usage").textbox("getValue"))) {
					// pga_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($(this).textbox("getValue"),0);
					var afterVal = strParseFloat($("#impra_pga_usage").textbox("getValue"),0);

					ratio = (beforeVal - afterVal) / beforeVal * 100; 

					if (beforeVal == 0 || afterVal == 0 || ratio == 0) {
						$("#pga_impr_ratio").textbox("setValue","0");
					} else {
						$("#pga_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});
	
	$("#impra_pga_usage").textbox({
		onChange: function(value) {
			if (!onlyNumChk(value)) {
				$(this).textbox("setValue","");
			} else {
				if (onlyNumChk($("#imprb_pga_usage").textbox("getValue"))) {
					// pga_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($("#imprb_pga_usage").textbox("getValue"),0);
					var afterVal = strParseFloat($(this).textbox("getValue"),0);
					
					ratio = (beforeVal - afterVal) / beforeVal * 100; 
					
					if (beforeVal == 0 || afterVal == 0 || ratio == 0) {
						$("#pga_impr_ratio").textbox("setValue","0");
					} else {
						$("#pga_impr_ratio").textbox("setValue",strRound(ratio,2));	
					}
				}
			}
		}
	});
	
	if ( $("#choice_div_cd").val() == 'G' && $("#tuning_status_cd").val() == "5" ) {
		$("#imprb_elap_time").textbox( 'setValue', $("#asisElapsedTime").val() );
		$("#imprb_buffer_cnt").textbox( 'setValue', $("#asisBufferGets").val() );
		
		$("#plan_hash_value").val( $("#asisPlanHashValue").val() );
		
//		ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainBeforePlanList"
		ajaxCall("/SQLInformation/TextPlanAll"
				, $("#submit_form")
				, "POST"
				, callback_SQLPerformTextPlanListAction);
	}
	
	/* 입력 변경시 hidden값 변경 */
	/* CKEDITOR 주석처리 2018-11-09 */
	/* CKEDITOR 주석다시 풀기 2018-12-26 5개 화면이 모두 CKEditor를 사용하기 때문에 이 성능개선관리상세 화면만 TextArea로 변경할 수 없음.*/
	var imprSqlText = CKEDITOR.instances.impr_sql_text;
	imprSqlText.on('blur', function(){
		$("#impr_sql_text_h").val(parent.formatHTML(imprSqlText.getData()));
	});
	
	var imprbExecPlan = CKEDITOR.instances.imprb_exec_plan;
	imprbExecPlan.on('blur', function(){
		$("#imprb_exec_plan_h").val(parent.formatHTML(imprbExecPlan.getData()));
	});
	
	var impraExecPlan = CKEDITOR.instances.impra_exec_plan;
	impraExecPlan.on('blur', function(){
		$("#impra_exec_plan_h").val(parent.formatHTML(impraExecPlan.getData()));
	});
	
	//index 내역
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		onClickRow : function(index,row) {
		},
		columns:[[
//			{field:'chk',width:"5%",halign:"center",align:"center",checkbox:"true",readonly:"true"},
			{field:'tuning_no',hidden:"true"},
			{field:'index_impr_type_cd',hidden:"true"},
			{field:'index_impr_type_nm',title:'개선유형',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'인덱스명',editor:'textbox',width:"12%",halign:"center",align:'center',sortable:"true"},
			{field:'index_column_name',title:'인덱스컬럼',editor:'textbox',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'before_index_column_name',title:'변경전 인덱스컬럼',editor:'textbox',width:"30%",halign:"center",align:'left',sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});

	ajaxCall("/SqlTuningIndexHistoryAction",
		$("#submit_form"),
		"POST",
		callback_SQLTuningIndexHistory);
	
	$("#tableList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable").remove();
	
	$('#userInfoBox').window({
		title : "사용자 정보",
		top:getWindowTop(315),
		left:getWindowLeft(1035),
		closable:false
	});
	
	$('#userInfoBox').window("close");
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
});


/** 선택 인덱스 삭제 */
function Btn_DeleteSelectedRow(){
	var selectedRows = $('#tableList').datagrid('getChecked');
	console.log("selectedRows:",selectedRows);
	if(selectedRows.length <= 0){
		parent.$.messager.alert('','선택된 인덱스가 없습니다.');
		return false;		
	}else{
		parent.$.messager.confirm('', '선택된 인덱스를 삭제하시겠습니까?', function(check) {
			if (check) {
				for(var i=0;i<selectedRows.length;i++){
					var selectedRow = selectedRows[i];
					var selectedRowIndex = $("#tableList").datagrid("getRowIndex", selectedRow);
					console.log("selectedRowIndex:"+selectedRowIndex);
					$('#tableList').datagrid('deleteRow', selectedRowIndex);
				}
			}
		});
	}

}
/**인덱스 변경 */
function Btn_ModifyIndex(){
	$('#indexRequestPop').window("open");
	
	$("#indexRequest_form #dbid").val($('#submit_form #dbid').val());
	$('#indexRequest_form #db_name').textbox('setValue',$('#submit_form #db_name').val());
	
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length > 0){
		var ndg = $('#indexsRequestList').datagrid();
		ndg.datagrid('loadData', $.extend(true,[],rows));
	}
}

function Btn_GoList(){
	$("#sql_id").val($("#list_sql_id").val());
	$("#submit_form #tuning_status_cd").val($("#list_tuning_status_cd").val());
	$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
	$("#dbio").textbox('setValue',$("#list_dbio").val());
	$("#dbid").val($("#list_selectValue").val());
	
	$("#submit_form").attr("action","/ImprovementManagement");
	$("#submit_form").submit();	
}

function Btn_SaveTunerAssign(gubun){
	if($('#selectTuner').combobox('getValue') == ""){
		parent.$.messager.alert('','튜닝담당자를 선택해 주세요.');
		return false;
	}
	
	//기존 튜닝담당자도 다시 지정할 수 있도록 처리 2018-18-17
//	if($('#selectTuner').combobox('getValue') == $("#perfr_id").val()){
//		$.messager.alert('','기존 튜닝담당자와 같습니다.<br/>다른 튜닝담당자를 선택해 주세요.');
//		return false;
//	}
	
	$("#tuningStatusArry").val(gubun);
	$("#perfr_id").val($('#selectTuner').combobox('getValue'));

	ajaxCall("/ImprovementManagement/SaveTunerAssign",
			$("#submit_form"),
			"POST",
			callback_SaveTunerAssignAction);
}

//callback 함수
var callback_SaveTunerAssignAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝담당자 지정이 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				//메시지 전송 삭제 2018-10-29
				// 튜닝담당자와 개발자에게 ALERT 전송
//				var tunerId = $('#selectTuner').combobox('getValue');
//				var devId = $("#tuning_requester_id").val();
//				var tunerName = $('#selectTuner').combobox('getText');
//				var strTitle = "";
//				var strMsg = "";
//				
//				if($("#tuningStatusArry").val() == "N"){
//					strTitle = "튜닝담당자 지정";
//					strMsg = "["+parent.loginUserName+"]님이 ["+tunerName+"]님을 튜닝담당자로 지정하셨습니다.";
//				}else{
//					strTitle = "튜닝담당자 변경";
//					strMsg = "["+parent.loginUserName+"]님이 ["+tunerName+"]님으로 튜닝담당자를 변경하셨습니다.";
//				}
//
//				parent.messageSendByUser(tunerId, strTitle, strMsg);
//				parent.messageSendByUser(devId, strTitle, strMsg);
				
//				Btn_GoList();
				
				//상단 요청,튜닝대기,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
			},500);
			closeTab();
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};
//튜닝중 처리
function Btn_SaveTuning(tuningStatus){
	$("#tuningNoArry").val($("#tuning_no").val());
	$("#tuningStatusArry").val(tuningStatus);
	
	if(tuningStatus == "6"){
		$("#push_yn").val("Y");
	}
	
	ajaxCall("/ImprovementManagement/SaveTuning",
			$("#submit_form"),
			"POST",
			callback_SaveTuningAction);	
}

//callback 함수
var callback_SaveTuningAction = function(result) {
	if (result.result) {
		parent.$.messager.alert('','튜닝중으로 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				
				//상단 요청,튜닝대기,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				$("#submit_form #tuning_status_cd").val($("#list_tuning_status_cd").val());
				$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
				$("#dbio").textbox('setValue',$("#list_dbio").val());
				
				if ($("#push_yn").val() == "Y") {
					//메시지 전송 삭제 2018-10-29
					/* 튜닝완료건을 튜닝중으로 변경시 => 개발자에게 Alert 전송*/
//					var userId = "";
//					var strTitle = "";
//					var strMsg = "";
//					
//					userId = $("#wrkjob_mgr_id").val();
//					strTitle = "튜닝중 처리";
//					strMsg = "["+parent.loginUserName+"]님이 튜닝완료건을 튜닝중으로 처리 하셨습니다.";
//
//					parent.messageSendByUser(userId, strTitle, strMsg);
				}
				
				$("#submit_form").attr("action","/ImprovementManagementView");
				$("#submit_form").submit();	
			},1000);
		});		
	} else {
		parent.$.messager.alert('',result.message,'error');
	}
};
/**
 * 튜닝 취소 버튼 클릭시
 * @returns
 */
function Btn_SaveTuningCancel(){
	ajaxCall("/ImprovementManagement/SaveTuningCancel",
			$("#submit_form"),
			"POST",
			callback_SaveTuningCancelAction);
}

//callback 함수
var callback_SaveTuningCancelAction = function(result) {
	if (result.result) {
		parent.$.messager.alert('','튜닝취소 처리가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
//				Btn_GoList();
				
				//상단 요청,튜닝대기,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
			},500);
			
			closeTab();
		});
	} else {
		parent.$.messager.alert('',result.message,'error');
	}
};

function Btn_SaveReceiptCancel(){
	ajaxCall("/ImprovementManagement/SaveReceiptCancel",
			$("#submit_form"),
			"POST",
			callback_SaveReceiptCancelAction);
}

//callback 함수
var callback_SaveReceiptCancelAction = function(result) {
	if (result.result) {
		parent.$.messager.alert('','접수 취소 처리가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
//				Btn_GoList();
			},500);
			closeTab();
		});
	} else {
		parent.$.messager.alert('',result.message,'error');
	}
};
//튜닝반려, 적용반려
function Btn_SaveCancel(cancelTitle) {
	if ($("#tuning_rcess_why").val() == "") {
		parent.$.messager.alert('','반려 사유를 입력해 주세요.');
		return false;
	}
	
	if ($("#rerequest").is(':checked')) {
		parent.$.messager.confirm({
			title : cancelTitle,
			msg : '현재 튜닝대상이 적용 반려되고 신규 튜닝대상이 생성 됩니다.<br/>계속 진행하시겠습니까?',
			ok : '예',
			cancel : '아니오',
			fn : function(r){
				if (r){
					ajaxCall("/ImprovementManagement/SaveCancel",
							$("#submit_form"),
							"POST",
							callback_SaveCancelAction);
				}
			}
		});
	} else {
		parent.$.messager.confirm({
			title : cancelTitle,
			msg : cancelTitle + '하시겠습니까?',
			ok : '예',
			cancel : '아니오',
			fn : function(r){
				if (r){
					ajaxCall("/ImprovementManagement/SaveCancel",
							$("#submit_form"),
							"POST",
							callback_SaveCancelAction);
				}
			}
		});
		
	}
}

//callback 함수
var callback_SaveCancelAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','반려 처리가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				//메시지 전송 삭제 2018-10-29
				/* txtValue = "4"(튜닝반려) => 개발자에게 Alert 전송, txtValue = "7"(적용반려) => 튜너에게 Alert 전송*/
//				var userId = "";
//				var strTitle = "";
//				var strMsg = "";
//
//				if(result.txtValue == "4"){
//					userId = $("#tuning_requester_id").val();
//					strTitle = "튜닝반려";
//					strMsg = "["+parent.loginUserName+"]님이 성능요청건을 튜닝반려하셨습니다.";
//				}else if(result.txtValue == "7"){
//					userId = $("#perfr_id").val();
//					strTitle = "적용반려";
//					strMsg = "["+parent.loginUserName+"]님이 성능요청건을 적용반려하셨습니다.";
//				}
//				
//				parent.messageSendByUser(userId, strTitle, strMsg);
				
//				Btn_GoList();

				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
			},500);
			closeTab();
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

function Btn_SaveEnd(strGubun){
	if(strGubun == "Y"){
		if($('#tr_cd').textbox('getValue') == ""){
			parent.$.messager.alert('','애플리케이션를 입력해 주세요.','info',function(){
				$('#statusViewTab').tabs('select', 0);
				$('#tr_cd').textbox('clear').textbox('textbox').focus();
			});
			
			return false;
		}
		
		if($('#dbio').textbox('getValue') == ""){
			parent.$.messager.alert('','DBIO를 입력해 주세요.','info',function(){
				$('#statusViewTab').tabs('select', 0);
				$('#dbio').textbox('clear').textbox('textbox').focus();
			});
			
			return false;
		}
	}
	
	if($('#selectEnd').combobox('getValue') == ""){
		parent.$.messager.alert('','종료사유를 선택해 주세요.');
		return false;
	}
	
	if($("#tuning_end_why").val() == ""){
		parent.$.messager.alert('','종료 사유를 입력해 주세요.');
		return false;
	}

	$("#tuning_end_why_cd").val($('#selectEnd').combobox('getValue'));

	ajaxCall("/ImprovementManagement/SaveEnd",
			$("#submit_form"),
			"POST",
			callback_SaveEndAction);
}

//callback 함수
var callback_SaveEndAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝종료가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
//				Btn_GoList();

				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
			},500);
			closeTab();
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};


//튜닝재요청
function Btn_ChangeRequest(){
	$("#submit_form #tuning_status_cd").val($("#list_tuning_status_cd").val());
	$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
	$("#dbio").textbox('setValue',$("#list_dbio").val());
	
//	$("#submit_form").attr("action","/UpdateImprovement");
//	$("#submit_form").submit();
	var menuId = "109";
	var menuNm = "튜닝재요청";
	var menuUrl = "/UpdateImprovement";
	var menuParam = "tuning_no="+$("#tuning_no").val()+"&dbid="+$("#dbid").val();
	console.log("menuParam:"+menuParam);
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
	
}

//튜닝 완료 버튼 클릭시
function Btn_SaveComplete(){
	$("#temporary_save_yn").val("N");
	var completeArry = "";	
	var strComplete = "";	
	var completeCnt = 0;

	/*if($('#dbio').textbox('getValue') == ""){
		parent.$.messager.alert('','DBIO를 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 0);
			$('#dbio').textbox('clear').textbox('textbox').focus();
		});
		
		return false;
	}*/
	
	if($('#imprb_elap_time').textbox('getValue') == ""){
		parent.$.messager.alert('','응답시간 개선전 값을 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			$('#imprb_elap_time').textbox('clear').textbox('textbox').focus();
		});
		return false;
	}
	
	if($('#imprb_buffer_cnt').textbox('getValue') == ""){
		parent.$.messager.alert('','블럭수 개선전 값을 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			$('#imprb_buffer_cnt').textbox('clear').textbox('textbox').focus();
		});
		return false;
	}
	
	if ($('#imprb_pga_usage').textbox('getValue') == "" ) {
		parent.$.messager.alert('','PGA 사용량 개선전 값을 정확히 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			$('#imprb_pga_usage').textbox('clear').textbox('textbox').focus();
		});
		return false;
	}
	
	//selectComplete=2 개선점없음 조건 추가
	var selectComplete = $('#selectComplete').combobox('getValue');
	console.log("selectComplete :"+selectComplete);
	
	//selectComplete=2 개선점없음 조건 추가
	if(selectComplete != 2){
		if($('#impra_elap_time').textbox('getValue') == ""){
			parent.$.messager.alert('','응답시간 개선후 값을 입력해 주세요.','info',function(){
				$('#statusViewTab').tabs('select', 1);
				$('#impra_elap_time').textbox('clear').textbox('textbox').focus();
			});
			return false;
		}
		
		if($('#impra_buffer_cnt').textbox('getValue') == ""){
			parent.$.messager.alert('','블럭수 개선후 값을 입력해 주세요.','info',function(){
				$('#statusViewTab').tabs('select', 1);
				$('#impra_buffer_cnt').textbox('clear').textbox('textbox').focus();
			});
			return false;
		}
		
		if ($('#impra_pga_usage').textbox('getValue') == "" ) {
			parent.$.messager.alert('','PGA 사용량 개선후 값을 정확히 입력해 주세요.','info',function(){
				$('#statusViewTab').tabs('select', 1);
				$('#impra_pga_usage').textbox('clear').textbox('textbox').focus();
			});
			return false;
		}
	}
	
//	if(($('#imprb_pga_usage').textbox('getValue') != "" && $('#impra_pga_usage').textbox('getValue') == "") || 
//		($('#imprb_pga_usage').textbox('getValue') == "" && $('#impra_pga_usage').textbox('getValue') != "")){
//		parent.$.messager.alert('','PGA 사용량 값을 정확히 입력해 주세요.','info',function(){
//			$('#statusViewTab').tabs('select', 1);
//			$('#imprb_pga_usage').textbox('clear').textbox('textbox').focus();
//		});
//		return false;
//	}
	
	//selectComplete=2 개선점없음 조건 추가
	if($('#controversialist').val() == "" && (selectComplete != 2)){
		parent.$.messager.alert('','문제점을 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			$('#controversialist').focus();
		});
		return false;
	}
	
	//selectComplete=2 개선점없음 조건 추가
	if($('#impr_sbst').val() == "" && (selectComplete != 2)){
		parent.$.messager.alert('','개선 내역을 입력해 주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			$('#impr_sbst').focus();
		});
		return false;
	}
	
	/* CKEDITOR 주석다시 풀기 2018-12-26 5개 화면이 모두 CKEditor를 사용하기 때문에 이 성능개선관리상세 화면만 TextArea로 변경할 수 없음.*/
	var str1 = CKEDITOR.instances.impr_sql_text.getData();
//	var str1 = impr_sql_text.value;
	
	str1 = str1.replace(/&nbsp;/g,"");
	str1 = str1.replace(/<P>/g,"");
	str1 = str1.replace(/<\/P>/g,"");
	str1 = str1.replace(/\r\n/g,"");
	
	//selectComplete=2 개선점없음 조건 추가
	if(str1.length <= 0 && (selectComplete != 2)){
		parent.$.messager.alert('','개선SQL을 입력해주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			CKEDITOR.instances.impr_sql_text.focus();
			//impr_sql_text.focus();
		});
		return false;
	}
	
	var str2 = CKEDITOR.instances.imprb_exec_plan.getData();
//	var str2 = imprb_exec_plan.value;
	
	str2 = str2.replace(/&nbsp;/g,"");
	str2 = str2.replace(/<P>/g,"");
	str2 = str2.replace(/<\/P>/g,"");
	str2 = str2.replace(/\r\n/g,"");
	
	if(str2.length <= 0 && (selectComplete != 2)){
		parent.$.messager.alert('','개선전 실행계획을 입력해주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			CKEDITOR.instances.imprb_exec_plan.focus();
		});
		return false;
	}
	
	var str3 = CKEDITOR.instances.impra_exec_plan.getData();
//	var str3 = impra_exec_plan.value;
	
	str3 = str3.replace(/&nbsp;/g,"");
	str3 = str3.replace(/<P>/g,"");
	str3 = str3.replace(/<\/P>/g,"");
	str3 = str3.replace(/\r\n/g,"");
	
	//selectComplete=2 개선점없음 조건 추가
	//개선점없음이 아닐 경우에만...
	if(str3.length <= 0 && (selectComplete != 2)){
		parent.$.messager.alert('','개선후 실행계획을 입력해주세요.','info',function(){
			$('#statusViewTab').tabs('select', 1);
			CKEDITOR.instances.impra_exec_plan.focus();
		});
		return false;
	}	

	if($('#selectComplete').combobox('getValue') == ""){
		parent.$.messager.alert('','완료 사유를 선택해 주세요.');
		return false;
	}
	
	strComplete = $('#selectComplete').combobox('getValue');	
	completeCnt = $('input:checkbox[name=\"'+strComplete+'\"]:checked').length;
	
	if(completeCnt < 1){
		parent.$.messager.alert('','완료 사유 상세정보를 선택해 주세요.');
		return false;
	}
	
	$('input:checkbox[name=\"'+strComplete+'\"]').each(function(){
		if(this.checked){
			completeArry += this.value + "|";
		}
	});
	
	$("#completeArry").val(strRight(completeArry,1));
	
	if($("#wrkjob_mgr_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','업무 담당자를 선택해 주세요.');
		$('#wrkjob_mgr_nm').textbox('clear').textbox('textbox').focus();
		return false;
	}
	
//	if($("#tuning_case_posting_yn").val() == "Y" && $("#tuning_case_posting_title").textbox('getValue') == ""){
//		parent.$.messager.alert('','사례 게시 제목을 입력해 주세요.');
//		$('#tuning_case_posting_title').textbox('clear').textbox('textbox').focus();
//		return false;
//	}
	var chkTuningCaseSwitchButton = $('#chkTuningCase').switchbutton('options').checked;
	console.log("chkTuningCaseSwitchButton :"+chkTuningCaseSwitchButton);
	//사례 게시가 ON 상태일 경우에 '사례 게시 제목을 입력해 주세요.'
	if(chkTuningCaseSwitchButton && $("#tuning_case_posting_title").textbox('getValue') == ""){
		parent.$.messager.alert('','사례 게시 제목을 입력해 주세요.');
		$('#tuning_case_posting_title').textbox('clear').textbox('textbox').focus();
		return false;
	}	
	if(chkTuningCaseSwitchButton && $("#tuning_case_type_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','튜닝사례유형을 선택해 주세요.');
		return false;
	}	
	
	if($("#tuning_complete_why").val() == ""){
		parent.$.messager.alert('','완료 사유를 입력해 주세요.');
		$('#tuning_complete_why').focus();
		return false;
	}	
	
	/* CKEDITOR 주석다시 풀기 2018-12-26 5개 화면이 모두 CKEditor를 사용하기 때문에 이 성능개선관리상세 화면만 TextArea로 변경할 수 없음.*/
	$("#impr_sql_text").val( CKEDITOR.instances.impr_sql_text.getData() );
	$("#imprb_exec_plan").val( CKEDITOR.instances.imprb_exec_plan.getData() );
	$("#impra_exec_plan").val( CKEDITOR.instances.impra_exec_plan.getData() );
	
	console.log("$('#selectComplete').combobox('getValue') :"+$('#selectComplete').combobox('getValue'));
	$("#tuning_complete_why_cd").val($('#selectComplete').combobox('getValue'));
	
	if(tuningCaseIsFirstOrNotCount == 0){//파라미터가 Y로 넘어오는데 0일경우 에는 N으로 설정을 해주어야 함.
		$("#tuning_case_posting_yn").val("N");
	}
	tuningCaseIsFirstOrNotCount == 0;//다시 0으로 초기화
	
	//index_history validation check
	if(!validationCheckSqlTuningIndexHistory()){
		$('#statusViewTab').tabs('select', 1);
		return false; 
	}
	
	//index_history 추가
	fnSetSqlTuningIndexHistory();
	
	ajaxCall("/ImprovementManagement/SaveComplete",
			$("#submit_form"),
			"POST",
			callback_SaveCompleteAction);
}

//callback 함수
var callback_SaveCompleteAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝완료가 정상적으로 처리되었습니다.','info',function(){
			
			$('#submit_form input[name="index_impr_type_cd"]').remove();
			$('#submit_form input[name="table_name"]').remove();
			$('#submit_form input[name="index_name"]').remove();
			$('#submit_form input[name="index_column_name"]').remove();
			$('#submit_form input[name="before_index_column_name"]').remove();
			
			setTimeout(function() {
				// 개발자에게 Alert 전송
				//메시지 전송 삭제 2018-10-29
//				var userId = $("#wrkjob_mgr_id").val();
//				var strTitle = "튜닝완료";
//				var strMsg = "["+parent.loginUserName+"]님이 성능요청건을 튜닝완료하셨습니다.";
//			
//				parent.messageSendByUser(userId, strTitle, strMsg);
//				리스트로 이동하지 말고...화면 리프레쉬			
//				Btn_GoList();
				
//				menuParam = "menu_id="+$("#menu_id").val();
//				parent.openLink("Y","110","성능 개선 관리","/ImprovementManagement",menuParam);
				
				//상단 요청,튜닝대기,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				$("#submit_form").attr("action","ImprovementManagementView");
				$("#submit_form").submit();
				
				//closeTab();
			},1000);
			
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

//탭닫기
function closeTab(){
	var menuNm="성능 개선 관리 상세(튜닝번호:"+$("#tuning_no").val()+")";
	let superMenuNm = "성능 개선 관리";
	
	if(parent.$("#mainTab").tabs("exists",menuNm)){
		parent.deleteCheckCnt = 1;
		
		if(parent.$("#mainTab").tabs("exists",superMenuNm)){
			parent.$("#mainTab").tabs("select",superMenuNm);
		}
		
		setTimeout(function(){
			parent.$("#mainTab").tabs("close",menuNm);
		},500);
		
//		parent.document.getElementById('if_110').src=parent.document.getElementById('if_110').src;
//		parent.$("#mainTab").tabs("select",menuNm);
//		var tab = parent.$("#mainTab").tabs("getSelected");
//		var index = tab.panel("options").index;
		//성능개선관리 화면 리프레쉬
//		parent.$("#mainTab").tabs("close",index);
	}
}

//임시 저장,임시저장 버튼 클릭시
function Btn_TempSave(){
	$("#temporary_save_yn").val("Y");
	var strComplete = "";
	var completeArry = "";
	
	$("#tuning_complete_why_cd").val($('#selectComplete').combobox('getValue'));
	
	strComplete = $('#selectComplete').combobox('getValue');
	$('input:checkbox[name=\"'+strComplete+'\"]').each(function(){
		if(this.checked){
			completeArry += this.value + "|";
		}
	});
	console.log("completeArry:"+strRight(completeArry,1));
	$("#completeArry").val(strRight(completeArry,1));
	
	/* CKEDITOR 주석다시 풀기 2018-12-26 5개 화면이 모두 CKEditor를 사용하기 때문에 이 성능개선관리상세 화면만 변경할 수 없음.*/
	$("#impr_sql_text").val( CKEDITOR.instances.impr_sql_text.getData() );
	$("#imprb_exec_plan").val( CKEDITOR.instances.imprb_exec_plan.getData() );
	$("#impra_exec_plan").val( CKEDITOR.instances.impra_exec_plan.getData() );
	
	//index_history validation check
	if(!validationCheckSqlTuningIndexHistory()){
		$('#statusViewTab').tabs('select', 1);
		return false; 
	}
	//index_history 추가
	fnSetSqlTuningIndexHistory();
	
	ajaxCall("/ImprovementManagement/TempSaveComplete",
			$("#submit_form"),
			"POST",
			callback_TempSaveCompleteAction);

}

//callback 함수
var callback_TempSaveCompleteAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','임시 저장을 하였습니다.','info',function(){
			$('#submit_form input[name="index_impr_type_cd"]').remove();
			$('#submit_form input[name="table_name"]').remove();
			$('#submit_form input[name="index_name"]').remove();
			$('#submit_form input[name="index_column_name"]').remove();
			$('#submit_form input[name="before_index_column_name"]').remove();
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

function validationCheckSqlTuningIndexHistory(){
	//index_history 추가
	var rows = $('#tableList').datagrid('getRows');
	console.log("row.length:"+rows.length);
	if(rows.length > 0){
		for(var i=0;i<rows.length;i++){
			var index_impr_type_cd = rows[i].index_impr_type_cd;
			var table_name = rows[i].table_name;
			var index_name = rows[i].index_name;
			var index_column_name = rows[i].index_column_name;
			
			if(index_name == ""){
				parent.$.messager.alert('',"인덱스명을 입력해 주세요.",'error');
				return false;
			}
			if(index_column_name == ""){
				parent.$.messager.alert('',"인덱스컬럼명을 입력해 주세요.",'error');
				return false;
			}
		}
	}
	return true;
}

function fnSetSqlTuningIndexHistory(){
	//index_history 추가
	var rows = $('#tableList').datagrid('getRows');
	console.log("row.length:"+rows.length);
	if(rows.length > 0){
		for(var i=0;i<rows.length;i++){
			var index_impr_type_cd = rows[i].index_impr_type_cd;
			var table_name = rows[i].table_name;
			var index_name = rows[i].index_name;
			var index_column_name = rows[i].index_column_name;
			if(index_column_name ==null || index_column_name =='null') index_column_name = '';
			var before_index_column_name = rows[i].before_index_column_name;
			if(before_index_column_name ==null || before_index_column_name =='null') before_index_column_name = '';
			
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "index_impr_type_cd");
			input.setAttribute("value", index_impr_type_cd);
			//append to form element that you want .
			document.getElementById("submit_form").appendChild(input);
			
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "table_name");
			input.setAttribute("value", table_name);
			//append to form element that you want .
			document.getElementById("submit_form").appendChild(input);
			
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "index_name");
			input.setAttribute("value", index_name);
			//append to form element that you want .
			document.getElementById("submit_form").appendChild(input);
			
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "index_column_name");
			input.setAttribute("value", index_column_name);
			//append to form element that you want .
			document.getElementById("submit_form").appendChild(input);
			
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "before_index_column_name");
			input.setAttribute("value", before_index_column_name);
			//append to form element that you want .
			document.getElementById("submit_form").appendChild(input);
			
		}
	}
}

function Btn_ChangeWorkUser(){
	if($("#u_wrkjob_mgr_nm").textbox('getValue') == ""){
		$.messager.alert('','업무 담당자를 선택해 주세요.');
		$('#u_wrkjob_mgr_nm').textbox('clear').textbox('textbox').focus();
		return false;
	}

	$("#modify_form #before_wrkjob_mgr_id").val($("#submit_form #u_before_wrkjob_mgr_id").val());
	$("#modify_form #before_wrkjob_mgr_nm").val($("#submit_form #u_before_wrkjob_mgr_nm").val());
	$("#modify_form #wrkjob_mgr_id").val($("#submit_form #u_wrkjob_mgr_id").val());
	$("#modify_form #wrkjob_mgr_wrkjob_cd").val($("#submit_form #u_wrkjob_mgr_wrkjob_cd").val());
	$("#modify_form #wrkjob_mgr_nm").val($("#submit_form #u_wrkjob_mgr_nm").textbox('getValue'));
	$("#modify_form #wrkjob_mgr_wrkjob_nm").val($("#submit_form #u_wrkjob_mgr_wrkjob_nm").textbox('getValue'));
	$("#modify_form #wrkjob_mgr_tel_num").val($("#submit_form #u_wrkjob_mgr_tel_num").textbox('getValue'));
	$("#modify_form #tuning_no").val($("#submit_form #tuning_no").val());
	$("#modify_form #tuning_status_cd").val($("#submit_form #tuning_status_cd").val());

	ajaxCall("/ImprovementManagement/ChangeWorkUser",
			$("#modify_form"),
			"POST",
			callback_ChangeWorkUserAction);	
}

//callback 함수
var callback_ChangeWorkUserAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','업무 담당자 변경이 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				var strTitle = "업무 담당자 변경";
				var strMsg = "";
				var tunerId = $("#perfr_id").val();
				var beforeUserId = $("#before_wrkjob_mgr_id").val();
				var beforeUserName = $("#before_wrkjob_mgr_nm").val();
				var afterUserId = $("#wrkjob_mgr_id").val();
				var afterUserName = $("#wrkjob_mgr_nm").textbox("getValue");
				
				strMsg = "["+beforeUserName+"]님에서<br/>["+afterUserName+"]님으로 업무담당자가 변경되었습니다.";
				
				//메시지 전송 삭제 2018-10-29
//				// 튜너에게 Alert 전송
//				parent.messageSendByUser(tunerId, strTitle, strMsg);
//				// 이전 개발자에게 Alert 전송
//				parent.messageSendByUser(beforeUserId, strTitle, strMsg);
//				// 변경 개발자에게 Alert 전송
//				parent.messageSendByUser(afterUserId, strTitle, strMsg);
				
//				Btn_GoList();
			},500);
			closeTab();
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

//function showProcessHistoryPopup(tuningNo){
//	parent.$('#processHistoryPop').window("open");	
//	parent.$('#processHistoryList').datagrid('loadData',[]);
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();
//	
//	/* modal progress open */
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로세스 처리 이력"," "); 
//	
//	ajaxCall("/ImprovementManagement/Popup/ProcessHistoryList?tuning_no="+tuningNo,
//			null,
//			"GET",
//			callback_ProcessHistoryList);
//}

function showUserInfoPopup() {
	$('#userInfoBox').window("open");
}

function showProcessHistoryPopup(tuningNo){
	$('#processHistoryPop').window("open");
	$('#processHistoryList').datagrid('loadData',[]);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로세스 처리 이력"," "); 
	
	ajaxCall("/ImprovementManagement/Popup/ProcessHistoryList?tuning_no="+tuningNo,
			null,
			"GET",
			callback_ProcessHistoryList);
}

//callback 함수
var callback_ProcessHistoryList = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#processHistoryList').datagrid("loadData", data);
		$("#processHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();  
};

function showWorkTunerPopup(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	
		parent.frameName = $("#menu_id").val();
		$('#workTunerPop').window("open");
}

/* 튜닝이력조회 탭 생성 */
function Btn_AddTabTuningHistory(){
	if($('#dbio').textbox('getValue') == ""){
		parent.$.messager.alert('','DBIO를 입력해 주세요.');
		return false;
	}
	//searchKey=04 : SQL식별자(DBIO)
	var menuId = "111";
	var menuNm = "튜닝 이력 조회";
	var menuUrl = "/TuningHistory";
	var menuParam = "dbid="+$("#dbid").val()+"&dbio="+encodeURI($('#dbio').textbox('getValue'))+
	"&call_from_parent=Y&searchKey=04&searchValue="+encodeURI($('#dbio').textbox('getValue'));
	
	/* 탭 생성  */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

function createSqlPerformance(sqlId, planHashValue){
//	parent.createVSQLNewTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, planHashValue);
//	parent.createSQLNewTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, planHashValue);
	let chkStr = $("#VSQLCheck").val();
	
	if ( chkStr == "AWR/VSQL" ) {
		$.ajax({
			type: "POST",
			url: "/ImprovementManagement/getPerfSourceType",
			datatype: 'json',
			data: { tuning_no : $("#pop_tuning_no").val() } ,
			success: function( result ) {
				console.log( "SQL TYPE ============> "+ result );
				if ( result != 2 ) {
					// AWR
					parent.createSQLInfoAWRTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, planHashValue);
				} else {
					// VSQL
					createSQLInfoVSQLAllTab2("statusViewTab", $("#dbid").val(), sqlId, planHashValue , "1");
				}
			}
		});
	} else {
		if ( chkStr != "VSQL" ) {
			// AWR
			parent.createSQLInfoAWRTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, planHashValue);
		} else {
			// VSQL
			createSQLInfoVSQLAllTab2("statusViewTab", $("#dbid").val(), sqlId, planHashValue , "1");
		}
	}
}

function createSqlPerformancePlan(sqlId, beforePlanHashValue, afterPlanHashValue){
	parent.$.messager.confirm({
		title : 'SQL 정보',
		msg : 'PLAN_HASH_VALUE를 선택하세요.<br/><br/><br/>AFTER_PLAN_HASH_VALUE -> AFTER<br/>BEFORE_PLAN_HASH_VALUE -> BEFORE',
		ok : 'AFTER',
		cancel : 'BEFORE',
		fn : function(r){
			if (r){
				// 신규 탭 생성..
				parent.createSQLNewTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, afterPlanHashValue);
			}else{
				// 신규 탭 생성..
				parent.createSQLNewTab($("#menu_id").val(), "statusViewTab", $("#dbid").val(), sqlId, beforePlanHashValue);
			}
		}
	});
}

function Btn_GoNext(strIndex){
	$('#statusViewTab').tabs('select', strIndex);
}

function Btn_SetSQLFormatter(){
	$('#sqlTextArea').format({method: 'sql'});
}

function resetCompleteReasonCheckbox(){
	$(".1").hide();
	$(".2").hide();
	$(".3").hide();
	//튜닝중이라 하더라도 SQL 튜닝완료 사유를 입력하면 임시저장이 가능하도록 처리하고
	//선택한 체크박스 값이 보이도록 처리한다.
	//2018-12-31
	//따라서 아래의 소스는 주석처리한다.
//	if($("#tuning_status_cd").val() == "5"){
//		$('input:checkbox[name="1"]').each(function(){
//			this.checked = false; 
//		});
//		$('input:checkbox[name="2"]').each(function(){
//			this.checked = false; 
//		});
//		$('input:checkbox[name="3"]').each(function(){
//			this.checked = false; 
//		});
//	}
}

function resizeSqlHistoryIF(dyHeight){
	document.getElementById("sqlHistoryIF").height = parseInt(dyHeight);
}

function showPreCheckPopup(){
	if($('#selectEnd').combobox('getValue') == ""){
		$.messager.alert('','종료사유를 선택해 주세요.');
		return false;
	}
	
	if($("#tuning_end_why").val() == ""){
		$.messager.alert('','종료 사유를 입력해 주세요.');
		return false;
	}

	$("#tuning_end_why_cd").val($('#selectEnd').combobox('getValue'));	
	
	$("#chk_tuning_no").html($("#pop_tuning_no").val());
	$("#chk_tuning_request_dt").html($("#pop_tuning_request_dt").val());
	$("#chk_choice_div_cd_nm").html($("#pop_choice_div_cd_nm").val());

	$('#perCheckPop').window("open");
	parent.frameName = $("#menu_id").val();
}

function ajaxCallSavePreCheck(){
	ajaxCall("/ImprovementManagement/SavePreCheck",
			$("#submit_form"),
			"POST",
			callback_SavePreCheckAction);	
}

//callback 함수
var callback_SavePreCheckAction = function(result) {
	$('#perCheckPop').window("close");
	console.log("result",result);
	if(result.result){
		$.messager.alert('','사전점검요청이 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
//				Btn_GoList();
				closeTab();
			},500);
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
};
/**
 * 튜닝 결과값 변경 버튼을 눌럿을때 입력박스를 입력가능하게 한다.
 * 튜닝결과값변경 텍스트를 튜닝결과값저장으로 변경한다.
 * onclick='Btn_ModifyTuningResult()'을 onclick='Btn_SaveTuningResult()'로 변경한다.
 */
var btnObj;
var Btn_ModifyTuningResult = function(obj){
	btnObj = obj;
	$("#imprb_elap_time").textbox({readonly:false});
	$("#imprb_buffer_cnt").textbox({readonly:false});
	$("#imprb_pga_usage").textbox({readonly:false});
	$("#impra_elap_time").textbox({readonly:false});
	$("#impra_buffer_cnt").textbox({readonly:false});
	$("#impra_pga_usage").textbox({readonly:false});
	
	$('#imprb_elap_time').textbox('textbox').focus();
	
	$(btnObj).html($(btnObj).html().replace('튜닝 결과값 변경', '튜닝 결과값 저장')); 
	$(btnObj).attr("onclick","Btn_SaveTuningResult()");
	
};
/**
 * 튜닝 결과값 저장 버튼을 눌럿을때 값을 저장한다.
 */
var Btn_SaveTuningResult = function(){
	ajaxCall("/ImprovementManagement/ModifyTuningResult",
			$("#submit_form"),
			"POST",
			callback_ModifyTuningResultAction);
}
/**
 * 튜닝 결과값 저장 버튼을 눌럿을때 값을 저장하고
 * 결과값을 리턴한다.
 */
var callback_ModifyTuningResultAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','변경된 튜닝 결과값을 저장하였습니다.','info',function(){
			$("#imprb_elap_time").textbox({readonly:true});
			$("#imprb_buffer_cnt").textbox({readonly:true});
			$("#imprb_pga_usage").textbox({readonly:true});
			$("#impra_elap_time").textbox({readonly:true});
			$("#impra_buffer_cnt").textbox({readonly:true});
			$("#impra_pga_usage").textbox({readonly:true});
			
			$(btnObj).html($(btnObj).html().replace('튜닝 결과값 저장', '튜닝 결과값 변경')); 
			$(btnObj).attr("onclick","Btn_ModifyTuningResult(this)");
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

/* 첨부파일 다운 이벤트 */
function downTuningFile(file_nm, org_file_nm){
	$("#file_nm").val(file_nm);
	$("#org_file_nm").val(org_file_nm);
	
	console.log(file_nm);
	console.log(org_file_nm);
	
	$("#submit_form").attr("action","/Tuning/DownFile");
	$("#submit_form").submit();

}

/* 첨부파일 삭제 이벤트 */
function deleteTuningFile(obj,file_seq,file_nm, auth_cd, reg_user_id, user_id){
	
	if(auth_cd == 'ROLE_DEV'){
		parent.$.messager.alert('','권한이 없습니다.','error');
		return false;
	}
	
	$("#file_nm").val(file_nm);
	$("#file_seq").val(file_seq);
	$.ajax({
		type: "POST",
		url: "/Tuning/DeleteFile",
		data: $("#submit_form").serialize(),
		success: function(result) {
			if(!result.result){
				parent.$.messager.alert('',result.message,'error');
			}else{
				parent.$.messager.alert('',result.message,'info');
				$(obj).parent().remove();
			}
		}
	});	
}

//callback 함수
var callback_SQLPerformTextPlanListAction = function(result) {
	var strHtml = "";
	
	$("#imprb_exec_plan li").remove();
	
	if ( result.result ) {
		strHtml += "<li><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for ( var i = 0 ; i < result.object.length ; i++ ) {
			var post = result.object[i];		
			strHtml += "<li>"+ strReplace( post.execution_plan,' ','&nbsp;' ) +"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#imprb_exec_plan").val( strHtml );
		$("#imprb_exec_plan_h").val( strHtml );
	}else{
		parent.$.messager.alert('',result.message);
	}
//	var arrayText = result.split(",");
//	
//	strHtml += "<li><b>ExecutionPlan</b></li>";
//	strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
//	
//	for ( var textIdx = 0; textIdx < arrayText.length; textIdx++ ) {
//		strHtml += "<li>" + strReplace( arrayText[textIdx] ,' ','&nbsp;') + "</li>";
//	}
//	
//	strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
//	
//	$("#imprb_exec_plan").val( strHtml );
//	$("#imprb_exec_plan_h").val( strHtml );

}