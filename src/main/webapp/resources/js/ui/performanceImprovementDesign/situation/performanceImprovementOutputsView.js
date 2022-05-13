$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.parent.$.messager.alert('','SQL TEXT가 복사되었습니다.');
	});
	
	var clipImprSql = new Clipboard('#imprSqlCopyBtn',{
		text: function() {
	        return $("#impr_sql_text_h").val();
	    }
	});    
	clipImprSql.on('success', function(e) {
		parent.parent.$.messager.alert('','개선 SQL TEXT가 복사되었습니다.');
	});
	
	var clipImprbExec = new Clipboard('#imprbExecCopyBtn',{
		text: function() {
	        return $("#imprb_exec_plan_h").val();
	    }
	});    
	clipImprbExec.on('success', function(e) {
		parent.parent.$.messager.alert('','개선전 실행계획이 복사되었습니다.');
	});
	
	var clipImpraExec = new Clipboard('#impraExecCopyBtn',{
		text: function() {
	        return $("#impra_exec_plan_h").val();
	    }
	});    
	clipImpraExec.on('success', function(e) {
		parent.parent.$.messager.alert('','개선후 실행계획이 복사되었습니다.');
	});
	
	CKEDITOR.replace("impr_sql_text",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	let strArrB = $("#imprb_exec_plan").val().split("\n");
	let strArrA = $("#impra_exec_plan").val().split("\n");
	let maxB = 130;
	let maxA = 130;
	
	for (var strIdx = 0; strIdx < strArrB.length; strIdx++) {
		if ( strArrB[strIdx].length > maxB ){
			maxB = strArrB[strIdx].length;
		}
	}
	if ( maxB > 130 ){
		maxB = maxB - 130;
	} else {
		maxB = 0;
	}
	
	for (var strIdx = 0; strIdx < strArrA.length; strIdx++) {
		if ( strArrA[strIdx].length > maxA ){
			maxA = strArrA[strIdx].length;
		}
	}
	if ( maxA > 130 ){
		maxA = maxA - 130;
	} else {
		maxA = 0;
	}
	
	CKEDITOR.replace("imprb_exec_plan",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;width:"+(99+maxB)+"%;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	CKEDITOR.replace("impra_exec_plan",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;width:"+(99+maxA)+"%;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	
	/* html 태크 제거 및 공백 제거*/	
	var impr_sql_text_h = $("#impr_sql_text_h").val();
	$("#impr_sql_text_h").val(parent.parent.formatHTML(impr_sql_text_h));

	var imprb_exec_plan_h = $("#imprb_exec_plan_h").val();
	$("#imprb_exec_plan_h").val(parent.parent.formatHTML(imprb_exec_plan_h));

	var impra_exec_plan_h = $("#impra_exec_plan_h").val();
	$("#impra_exec_plan_h").val(parent.parent.formatHTML(impra_exec_plan_h));

	// 튜닝담당자 조회			
	$('#selectTuner').combobox({
	    url:"/Common/getTuner?dbid="+$("#dbid").val(),
	    method:"get",
		valueField:'tuner_id',
	    textField:'tuner_nm'
	});

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

			parent.parent.resizeTopFrame($("#menu_id").val(), height);
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
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#impra_pga_usage").textbox("getValue"))){
					// pga_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($(this).textbox("getValue"),0);
					var afterVal = strParseFloat($("#impra_pga_usage").textbox("getValue"),0);

					ratio = (beforeVal - afterVal) / beforeVal * 100;

					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#pga_impr_ratio").textbox("setValue","0");
					}else{
						$("#pga_impr_ratio").textbox("setValue",strRound(ratio,2));
					}
				}
			}
		}
	});
	
	$("#impra_pga_usage").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}else{
				if(onlyNumChk($("#imprb_pga_usage").textbox("getValue"))){
					// pga_impr_ratio 값 연산
					var ratio = 0;
					var beforeVal = strParseFloat($("#imprb_pga_usage").textbox("getValue"),0);
					var afterVal = strParseFloat($(this).textbox("getValue"),0);
					
					ratio = (beforeVal - afterVal) / beforeVal * 100;
					
					if(beforeVal == 0 || afterVal == 0 || ratio == 0){
						$("#pga_impr_ratio").textbox("setValue","0");
					}else{
						$("#pga_impr_ratio").textbox("setValue",strRound(ratio,2));
					}
				}
			}
		}
	});
	
	/* 입력 변경시 hidden값 변경 */
	var imprSqlText = CKEDITOR.instances.impr_sql_text;
	imprSqlText.on('blur', function(){
		$("#impr_sql_text_h").val(parent.parent.formatHTML(imprSqlText.getData()));
	});

	var imprbExecPlan = CKEDITOR.instances.imprb_exec_plan;
	imprbExecPlan.on('blur', function(){
		$("#imprb_exec_plan_h").val(parent.parent.formatHTML(imprbExecPlan.getData()));
	});
	
	var impraExecPlan = CKEDITOR.instances.impra_exec_plan;
	impraExecPlan.on('blur', function(){
		$("#impra_exec_plan_h").val(parent.parent.formatHTML(impraExecPlan.getData()));
	});

	//인덱스 이력 조회
	createSqlTuningIndexHistoryTable();	
	
});

function Btn_GoList(){
	$("#sql_id").val($("#list_sql_id").val());
	$("#submit_form #tuning_status_cd").val($("#list_tuning_status_cd").val());
//	$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
//	$("#dbio").textbox('setValue',$("#list_dbio").val());
	$("#selectValue").val($("#list_selectValue").val());
	if ( $("#tuning_complete_why_origin_cd").val()!= null && $("#tuning_complete_why_origin_cd").val() == "" ) {
		$("#tuning_complete_why_cd").val("");
	}
	
	// Original_dbid
	$("#dbid").val( $("#operation_dbid").val() );

	$("#submit_form #call_from_parent").val("Y");
	if ( $("#menu_id").val() != null && $("#menu_id").val() == "308" ) { 
		$("#submit_form").attr("action","/PerformanceImprovementDesign/PerformanceImprovementOutputs");
	} else if ( $("#menu_id").val() != null && $("#menu_id").val() == "325") {
		$("#submit_form").attr("action","/PerformanceImprovementDesign/PerformanceImprovementOutputs_V2");
	}
	$("#submit_form").submit();	
}

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
	if(result.result){
		parent.parent.$.messager.alert('','튜닝중으로 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				$("#submit_form #tuning_status_cd").val($("#list_tuning_status_cd").val());
				$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
				$("#dbio").textbox('setValue',$("#list_dbio").val());
				
				if($("#push_yn").val() == "Y"){
					/* 튜닝완료건을 튜닝중으로 변경시 => 개발자에게 Alert 전송*/
					//메시지 전송 삭제 2018-10-29
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
	}else{
		parent.parent.$.messager.alert('',result.message,'error');
	}
};

function Btn_GoNext(strIndex){
	$('#statusViewTab').tabs('select', strIndex);
}

function Btn_SetSQLFormatter(){
	$('#sqlTextArea').format({method: 'sql'});
}

function Btn_DownloadOutputs(){
	$("#submit_form").attr("action","/getPerformanceImprovementOutputs");
	$("#submit_form").submit();		
}