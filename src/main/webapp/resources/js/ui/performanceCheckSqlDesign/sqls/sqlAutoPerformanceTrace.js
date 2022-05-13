$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	let row = parent.$("#tableList").datagrid('getSelected');
	
	if(row == null) {
		return;
	}
	
	console.log("SQL 성능 추적 menu_id:" + $('#menu_id').val());
	
	$('#dbid').val(row.dbid);
	$('#before_prd_sql_id').val(row.before_prd_sql_id);
	$('#before_prd_plan_hash_value').val(row.before_prd_plan_hash_value);
	$('#after_prd_sql_id').val(row.after_prd_sql_id);
	$('#after_prd_plan_hash_value').val(row.after_prd_plan_hash_value);
	$('#perf_check_id').val(row.perf_check_id);
	$('#perf_check_step_id').val(row.perf_check_step_id);
	$('#program_id').val(row.program_id);
	$('#program_execute_tms').val(row.program_execute_tms);
	$('#program_exec_dt').val(row.program_exec_dt);
	$('#deploy_complete_dt').val(row.deploy_complete_dt);
	$('#elapsed_time_activity').val(row.elapsed_time_activity);
	$('#buffer_gets_activity').val(row.buffer_gets_activity);
	$('#prd_elap_time_increase_ratio').val(row.prd_elap_time_increase_ratio);
	$('#prd_buffer_gets_increase_ratio').val(row.prd_buffer_gets_increase_ratio);
	$('#prd_rows_proc_increase_ratio').val(row.prd_rows_proc_increase_ratio);
	
	callData();
});

function callData() {
	if($('#perf_check_id').val() != '' && $('#perf_check_step_id').val() != '' &&
			$('#program_id').val() != ''  && $('#program_execute_tms').val() != '') {
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val(),"성능 점검 추적 중");
		
		ajaxCall("/RunSqlPerformanceChangeTracking/autoPerformanceCheck",
				$("#submit_form"),
				"POST",
				callback_PerformanceCheckAction);
	} else {
		if($('#after_prd_sql_id').val() != '' && $('#after_prd_plan_hash_value').val() != '') {
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val(),"배포후 운영 성능 추적 중");
			
			ajaxCall("/Sqls/afterDistributionOperationPerformance",
					$("#submit_form"),
					"POST",
					callback_AfterDistributionOperationPerformanceAction);
		}
	}
}

var callback_PerformanceCheckAction = function(result) {
	try {
		var data = JSON.parse(result);
		let row = parent.$("#tableList").datagrid('getSelected');
		
		if(typeof data != 'undefined' && data.length > 0) {
			let elapsed_time = row.test_elapsed_time;
			let buffer_gets = row.test_buffer_gets;
			let rows_processed = row.test_rows_processed;
			let sql_id = data[0].sql_id;
			let plan_hash_value = data[0].plan_hash_value;
			let program_exec_dt = $('#program_exec_dt').val();
			let deploy_complete_dt = $('#deploy_complete_dt').val();
			
			if(!elapsed_time){
				elapsed_time = "&#160;";
			}
			if(!buffer_gets){
				buffer_gets = "&#160;";
			}
			if(!rows_processed){
				rows_processed = "&#160;";
			}
			if(!sql_id){
				sql_id = "&#160;";
			}
			if(!plan_hash_value){
				plan_hash_value = "&#160;";
			}
			if(!program_exec_dt){
				program_exec_dt = "&#160;";
			}
			if(!deploy_complete_dt){
				deploy_complete_dt = "&#160;";
			}
			
//			$('#sql_performance_trace_status_current').css('color', '#000000');
//			
//			$('#sql_performance_trace_name_current').removeClass('sql_performance_trace_name_begin');
//			$('#sql_performance_trace_name_current').addClass('sql_performance_trace_name');
//
//			$('#sql_performance_trace_point_begin').removeClass('sql_performance_trace_point_begin');
//			$('#sql_performance_trace_point_begin').addClass('sql_performance_trace_point_begin_completion');
//			
//			$('#sql_performance_trace_space_line_current').removeClass('sql_performance_trace_space_line_default');
//			$('#sql_performance_trace_space_line_current').addClass('sql_performance_trace_space_line');
			
			$('#sql_performance_trace_status_current').css('color', '#000000');
			
			$('#sql_performance_trace_name_begin').removeClass('sql_performance_trace_name_begin');
			$('#sql_performance_trace_name_begin').addClass('sql_performance_trace_name');
			$('#sql_performance_trace_point_begin').removeClass('sql_performance_trace_point_begin');
			$('#sql_performance_trace_point_begin').addClass('sql_performance_trace_point_begin_completion');
			
			$('#sql_performance_trace_space_line_begin').removeClass('sql_performance_trace_space_line_default');
			$('#sql_performance_trace_space_line_begin').addClass('sql_performance_trace_space_line');
			
			$('#sql_performance_trace_status_li_value_elapsed_time').text(elapsed_time);
			$('#sql_performance_trace_status_li_value_buffer_gets').text(buffer_gets);
			$('#sql_performance_trace_status_li_value_rows_processed').text(rows_processed);
			$('#sql_performance_trace_status_li_value_sql_id').text(sql_id);
			$('#sql_performance_trace_status_li_value_plan_hash_value').text(plan_hash_value);
			$('#sql_performance_trace_status_li_value_program_exec_dt').text(program_exec_dt);
			$('#sql_performance_trace_status_li_value_deploy_complete_dt').text(deploy_complete_dt);
			
			$('#performance_chekc_elapsed_time').val(elapsed_time);
			$('#performance_chekc_buffer_gets').val(buffer_gets);
			$('#performance_chekc_rows_processed').val(rows_processed);
		}
	} catch(err) {
		parent.$.messager.alert('에러', '성능 점검</br>' + err.message);
	} finally {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		if($('#after_prd_sql_id').val() != '' && $('#after_prd_plan_hash_value').val() != '') {
			/* modal progress open */
			if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val(),"배포후 운영 성능 추적 중");
			
			ajaxCall("/Sqls/afterDistributionOperationPerformance",
					$("#submit_form"),
					"POST",
					callback_AfterDistributionOperationPerformanceAction);
		}
	}
}

var callback_AfterDistributionOperationPerformanceAction = function(result) {
	try {
		var data = JSON.parse(result);
		let row = parent.$("#tableList").datagrid('getSelected');
		
		if(typeof data != 'undefined' && data.length > 0) {
			let elapsed_time = row.after_prd_elapsed_time;
			let buffer_gets = row.after_prd_buffer_gets;
			let rows_processed = row.after_prd_rows_processed;
			let sql_id = data[0].sql_id;
			let plan_hash_value = data[0].plan_hash_value;
			let last_active_time = data[0].last_active_time;
			let elapsed_time_activity = $('#elapsed_time_activity').val();
			let buffer_gets_activity = $('#buffer_gets_activity').val();
			
			let prd_elap_time_increase_ratio = $('#prd_elap_time_increase_ratio').val();
			let prd_buffer_gets_increase_ratio = $('#prd_buffer_gets_increase_ratio').val();
			let prd_rows_proc_increase_ratio = $('#prd_rows_proc_increase_ratio').val();

			if(!elapsed_time){
				elapsed_time = "&#160;";
			}
			if(!buffer_gets){
				buffer_gets = "&#160;";
			}
			if(!rows_processed){
				rows_processed = "&#160;";
			}
			if(!sql_id){
				sql_id = "&#160;";
			}
			if(!plan_hash_value){
				plan_hash_value = "&#160;";
			}
			if(!last_active_time){
				last_active_time = "&#160;";
			}
			if(!elapsed_time_activity){
				elapsed_time_activity = "&#160;";
			}
			if(!buffer_gets_activity){
				buffer_gets_activity = "&#160;";
			}
			if(!prd_elap_time_increase_ratio){
				prd_elap_time_increase_ratio = "&#160;";
			}
			if(!prd_buffer_gets_increase_ratio){
				prd_buffer_gets_increase_ratio = "&#160;";
			}
			if(!prd_rows_proc_increase_ratio){
				prd_rows_proc_increase_ratio = "&#160;";
			}
			
			$('#sql_performance_trace_status_after').css('color', '#000000');
			$('#sql_performance_trace_status_after_ratio').css('color', '#000000');
			
			$('#sql_performance_trace_name_current').removeClass('sql_performance_trace_name_begin');
			$('#sql_performance_trace_name_current').addClass('sql_performance_trace_name');
			$('#sql_performance_trace_point_current').removeClass('sql_performance_trace_point_current');
			$('#sql_performance_trace_point_current').addClass('sql_performance_trace_point_current_completion');

			$('#sql_performance_trace_space_line_current').removeClass('sql_performance_trace_space_line_default');
			$('#sql_performance_trace_space_line_current').addClass('sql_performance_trace_space_line');
			
//			$('#sql_performance_trance_name_after').removeClass('sql_performance_trace_name_begin');
//			$('#sql_performance_trance_name_after').addClass('sql_performance_trace_name');
//			$('#sql_performance_trace_point_after').removeClass('sql_performance_trace_point_after');
//			$('#sql_performance_trace_point_after').addClass('sql_performance_trace_point_after_completion');
			
//			$('#sql_performance_trace_space_line_current').removeClass('sql_performance_trace_space_line_default');
//			$('#sql_performance_trace_space_line_current').addClass('sql_performance_trace_space_line');
			
			$('#sql_performance_trace_status_li_value_after_elapsed_time').text(elapsed_time);
			$('#sql_performance_trace_status_li_value_after_buffer_gets').text(buffer_gets);
			$('#sql_performance_trace_status_li_value_after_rows_processed').text(rows_processed);
			$('#sql_performance_trace_status_li_value_after_sql_id').text(sql_id);
			$('#sql_performance_trace_status_li_value_after_plan_hash_value').text(plan_hash_value);
			$('#sql_performance_trace_status_li_value_after_last_active_time').text(last_active_time);
			$('#sql_performance_trace_status_li_value_after_elapsed_time_activity').text(elapsed_time_activity);
			$('#sql_performance_trace_status_li_value_after_buffer_gets_activity').text(buffer_gets_activity);
			
			$('#after_performance_chekc_elapsed_time').val(elapsed_time);
			$('#after_performance_chekc_buffer_gets').val(buffer_gets);
			$('#after_performance_chekc_rows_processed').val(rows_processed);
			
			increaseRate();
		}
	} catch(err) {
		parent.$.messager.alert('에러', '배포 후 운영 성능</br>' + err.message);
	} finally {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function increaseRate() {
	if(isTargetState($('#sql_performance_trace_name_current').css('color')) == false) {
		// 성능 점검이 없는 경우
		return;
	}
	
	console.log("$('#prd_elap_time_increase_ratio').val():" + $('#prd_elap_time_increase_ratio').val());
	console.log("$('#prd_buffer_gets_increase_ratio').val():" + $('#prd_buffer_gets_increase_ratio').val());
	console.log("$('#prd_rows_proc_increase_ratio').val():" + $('#prd_rows_proc_increase_ratio').val());
	
	$('#sql_performance_trace_status_li_value_after_prd_elap_time_increase_ratio').html(formattedRatio(Number($('#prd_elap_time_increase_ratio').val())));
	$('#sql_performance_trace_status_li_value_after_prd_buffer_gets_increase_ratio').html(formattedRatio(Number($('#prd_buffer_gets_increase_ratio').val())));
	$('#sql_performance_trace_status_li_value_after_prd_rows_proc_increase_ratio').html(formattedRatio(Number($('#prd_rows_proc_increase_ratio').val())));
}

function formattedRatio(ratio) {
	let html = '';
	
	if(typeof(ratio) == 'undefined') {
		html = '<label/>';
	} else {
		if(ratio > 0) {
			html += '<img src="/resources/images/after_ratio_up_arrow.png" style="float: left;"/>';
		} else if(ratio < 0) {
			html += '<img src="/resources/images/after_ratio_down_arrow.png" style="float: left;"/>';
		}
		
		html += '<label style="margin-right: 0px;">' + fillDigit(Math.sqrt(Math.pow(ratio, 2)), 2) + ' x</label>';
	}
	
	return html;
}

function isTargetState(css) {
	css = css.substring(4, css.length - 1);
	console.log("css:" + css);
	
	let splitCss = css.split(', ');
	let hex = rgbToHex(Number(splitCss[0]), Number(splitCss[1]), Number(splitCss[2]));
	console.log("hex:" + hex);
	if(hex == '#a2a2a2') {
		return false;
	} else {
		return true;
	}
}

function componentToHex(c) {
	var hex = c.toString(16);
	
	return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(r, g, b) {
	return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

function sqlPerformanceDetailBegin(id) {
	let sql_id = $('#' + id).text();
	
	if(sql_id.length == 0) {
		return;
	}
	
	let begin_dt = parent.$('#begin_dt').datebox('getValue');
	
	// 신규 탭 생성..
	parent.parent.parent.parent.createSqlsDetailNewTab($("#menu_id").val(), "performanceCheckSqlTab", 
			$("#dbid").val(), sql_id, $('#before_prd_plan_hash_value').val(), begin_dt);
}

function sqlPerformanceDetailAfter(id) {
	let sql_id = $('#' + id).text();
	
	if(sql_id.length == 0) {
		return;
	}
	
	let begin_dt = parent.$('#begin_dt').datebox('getValue');
	
	// 신규 탭 생성..
	parent.parent.parent.parent.createSqlsDetailNewTab($("#menu_id").val(), "performanceCheckSqlTab", 
			$("#dbid").val(), sql_id, $('#after_prd_plan_hash_value').val(), begin_dt);
}