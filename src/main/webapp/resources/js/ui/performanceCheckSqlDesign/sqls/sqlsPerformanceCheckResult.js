var bigTableThresholdCnt = "";

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	let parameter = "";
	let row = parent.$("#tableList").datagrid('getSelected');
	
	if(row == null) {
		return;
	}
	
	$('#dbid').val(row.dbid);
	$('#perf_check_id').val(row.perf_check_id);
	$('#program_id').val(row.program_id);
	$('#perf_check_step_id').val(row.perf_check_step_id);
	$('#program_execute_tms').val(row.program_execute_tms);
	$('#top_wrkjob_cd').val(row.top_wrkjob_cd);
	$('#after_prd_sql_id').val(row.after_prd_sql_id);
	$('#after_prd_plan_hash_value').val(row.after_prd_plan_hash_value);
	
	let except_yn = row.except_yn;
	
	callBigTableThresholdCnt();
	callResultList(except_yn);
});

function callResultList(except_yn) {
	let url = '/Sqls/performanceCheckResult';
	
	if(except_yn == 'Y') {
		url = '/Sqls/performanceCheckResultException';
	}
	
	ajaxCall(url, $("#submit_form"), "POST", callback_PerformanceCheckResult);
}

var callback_PerformanceCheckResult = function(result) {
	try{
		let data = JSON.parse(result);
		
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			let html = "";
			
			jQuery.each(data.rows, function(index, row){
				let perf_check_indc_nm = row.perf_check_indc_nm;
				let indc_pass_max_value = row.indc_pass_max_value;						// 적합, 부적합
				let exec_result_value = row.exec_result_value;							// 배포전 결과값
				let perf_check_result_div_nm = row.perf_check_result_div_nm;			// 배포전 결과
				let prd_exec_result_value = row.prd_exec_result_value;					// 배포후 결과값
				let prd_perf_check_result_div_nm = row.prd_perf_check_result_div_nm;	// 배포후 결과
				let exception_yn = row.exception_yn;									// 에외
				let perf_check_result_desc = row.perf_check_result_desc;				// 설명
				let perf_check_result_div_cd = row.perf_check_result_div_cd;
				let prd_perf_check_result_div_cd = row.prd_perf_check_result_div_cd;
				let perf_check_indc_id = row.perf_check_indc_id;
				let perf_check_meth_cd = row.perf_check_meth_cd;
				
				html += "<tr>\n";
				html += "	<td><input type='text' size='15' class='font11px width100per border0px' value='"+perf_check_indc_nm+"' readonly></td>\n";
				
				if(perf_check_meth_cd == 1){
					html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+indc_pass_max_value+"' readonly></td>\n";
					html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+indc_pass_max_value+" 초과' readonly></td>\n";
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
				
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+exec_result_value+"'readonly></td>\n";
				
				var backColor = "";
				
				if(perf_check_result_div_nm == '적합'){
					backColor = 'style="background-color:#1A9F55;color:white;"';
				}else if(perf_check_result_div_nm == '부적합'){
					backColor = 'style="background-color:#E41E2C;color:white;"';
				}else if(perf_check_result_div_nm == '오류'){
					backColor = 'style="background-color:#ED8C33;color:white;"';
				}else if(perf_check_result_div_nm == '미수행'){
					backColor = 'style="background-color:#7F7F7F;color:white;"';
				}else if(perf_check_result_div_nm == '수행중'){
					return 'background-color:#89BD4C;color:white;';
				}else if(perf_check_result_div_nm == '점검제외'){
					backColor = 'style="background-color:#012753;color:white;"';a
				}else if(perf_check_result_div_nm == 'N/A'){
					backColor = 'style="background-color:#f3f2f2;color:black;"';
				}
				
				html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+perf_check_result_div_nm+"'readonly></td>\n";
				
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+prd_exec_result_value+"'readonly></td>\n";
				
				if(prd_perf_check_result_div_nm == '적합'){
					backColor = 'style="background-color:#1A9F55;color:white;"';
				}else if(prd_perf_check_result_div_nm == '부적합'){
					backColor = 'style="background-color:#E41E2C;color:white;"';
				}else if(prd_perf_check_result_div_nm == '오류'){
					backColor = 'style="background-color:#ED8C33;color:white;"';
				}else if(prd_perf_check_result_div_nm == '미수행'){
					backColor = 'style="background-color:#7F7F7F;color:white;"';
				}else if(prd_perf_check_result_div_nm == '수행중'){
					return 'background-color:#89BD4C;color:white;';
				}else if(prd_perf_check_result_div_nm == '점검제외'){
					backColor = 'style="background-color:#012753;color:white;"';
				}else if(prd_perf_check_result_div_nm == 'N/A'){
					backColor = 'style="background-color:#f3f2f2;color:black;"';
				}
				
				html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+prd_perf_check_result_div_nm+"'readonly></td>\n";
				
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+exception_yn+"'readonly></td>\n";
				
				let labelStyle = '<label style="color: red; font-weight: bold;margin-left: 0px;">';
				
				if(index == 4) {
					html += "	<td>" + labelStyle + "대용량 테이블 기준 : " + getNumberFormat(bigTableThresholdCnt) + " 건</label></br>" + labelStyle + nvl(perf_check_result_desc,'') + "</label></td>\n";
				} else {
					html += "	<td>" + labelStyle + nvl(perf_check_result_desc,'') + "</label></td>\n";
				}
				html += "</tr>\n";
			});
			
			if(html.length == 0) {
				let blankText = '검색된 데이터가 없습니다.';
				
				html += "<tr>\n";
				html += "	<td colspan='9'><input type='text' class='tac font11px width100per border0px' value='"+blankText+"' readonly></td>\n";
				html += "</tr>\n";
			}
			
			$("#detailCheckResultTable tbody").html("");
			$("#detailCheckResultTable tbody").append(html);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

function callBigTableThresholdCnt() {
	ajaxCall("/Sqls/bigTableThresholdCnt",
			$("#submit_form"),
			"POST",
			callback_BigTableThresholdCntAction);
}

var callback_BigTableThresholdCntAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		bigTableThresholdCnt = data.rows[0].big_table_threshold_cnt;
		
		console.log("bigTableThreshold_cnt[" + bigTableThresholdCnt + "]");
	}
};

