const SPACE = " ";
const NO_DATA = "검색된 데이터가 없습니다.";

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
	$('#after_prd_sql_id').val(row.after_prd_sql_id);
	$('#after_prd_plan_hash_value').val(row.after_prd_plan_hash_value);
	
//	createSqlPlanPerformanceCheck();
	createSqlPlanOperation();
	
//	loadSqlInfoPerformanceCheck();
	loadSqlInfoOperation();
});

function createSqlPlanPerformanceCheck() {
	$("#sql_plan_performance_check").datagrid({
		view: myview,
		singleSelect: true,
		striped: true,
		columns:[[
			{field:'cost_percent',title:'COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'cpu_cost_percent',title:'CPU_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'io_cost_percent',title:'IO_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'id',title:'ID',halign:"center",align:'left',width:'25px'},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',width:'365px',formatter:toNbspFromWithespace,styler:diffStyle1},
			{field:'cost',title:'COST',halign:"center",align:'right',width:'75px'},
			{field:'cpu_cost',title:'CPU_COST',halign:"center",align:'right',width:'75px'},
			{field:'io_cost',title:'IO_COST',halign:"center",align:'right',width:'75px'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
	});
}

function createSqlPlanOperation() {
	$("#sql_plan_operation").datagrid({
		view: myview,
		singleSelect: true,
		striped: true,
		columns:[[
			{field:'cost_percent',title:'COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'cpu_cost_percent',title:'CPU_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'io_cost_percent',title:'IO_COST<br>(%)',halign:"center",align:'right',width:'63px',styler:cellStyler1},
			{field:'id',title:'ID',halign:"center",align:'left',width:'25px'},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',width:'365px',formatter:toNbspFromWithespace},
			{field:'cost',title:'COST',halign:"center",align:'right',width:'75px'},
			{field:'cpu_cost',title:'CPU_COST',halign:"center",align:'right',width:'75px'},
			{field:'io_cost',title:'IO_COST',halign:"center",align:'right',width:'75px'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}
function diffStyle1(value,row,index){
	let diffTag = row.diffTag;
	
	if(diffTag == 'CHANGE' || diffTag == 'DELETE'){
		return 'background-color: #FAEBD9; color:#BB0000;';
	}else if(diffTag == 'INSERT'){
		return 'background-color: #F2F2F2';
	}else {
		return 'background-color: none';;
	}
}
function diffStyle2(value,row,index){
	let diffTag = row.diffTag;
	
	if(diffTag == 'CHANGE' || diffTag == 'INSERT'){
		return 'background-color: #FAEBD9; color:#BB0000;';
	}else if(diffTag == 'DELETE'){
		return 'background-color: #F2F2F2';
	}else {
		return 'background-color: none';;
	}
}
function cellStyler1(value,row,index){
	var color = '#ffe48d';
	
	if(value >= 100){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);';
	}else if(value < 100){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);';
	}
}

function cellStyler2(value,row,index){
	var color = '#efbd7f';
	
	if(value >= 100){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);';
	}else if(value < 100){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);';
	}
}

function loadSqlInfoPerformanceCheck() {
	$('#sql_plan_performance_check').datagrid("loadData", []);
	
//	ajaxCall("/Sqls/sqlTextPerformanceCheck",
//			$("#submit_form"),
//			"POST",
//			callback_SqlTextPerformanceCheckAction);
	
	ajaxCall("/Sqls/sqlBindPerformanceCheck",
			$("#submit_form"),
			"POST",
			callback_SqlBindPerformanceCheckAction);
	
	ajaxCall("/Sqls/sqlPlanPerformanceCheck",
			$("#submit_form"),
			"POST",
			callback_SqlPlanPerformanceCheckAction);
}

function loadSqlInfoOperation() {
	$('#sql_plan_operation').datagrid("loadData", []);
	
	ajaxCall("/Sqls/sqlTextAll",
			$("#submit_form"),
			"POST",
			callback_SqlTextAll);
	
	ajaxCall("/Sqls/sqlBindOperation",
			$("#submit_form"),
			"POST",
			callback_SqlBindOperationAction);
	
	ajaxCall("/Sqls/sqlPlanOperation",
			$("#submit_form"),
			"POST",
			callback_SqlPlanOperationAction);
}

var callback_SqlTextPerformanceCheckAction = function(result) {
	try {
		var data = JSON.parse(result);
		
		if(data.rows == null || data.rows.length == 0) {
			$('#sql_text_performance_check').textbox('setValue', NO_DATA);
			
			return;
		}
		
		let rows = data.rows;
		
		$('#sql_text_performance_check').textbox('setValue', rows[0].program_source_desc);
	} catch(err) {
		parent.$.messager.alert('에러', '성능 점검 SQL - SQL TEXT</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_SqlBindPerformanceCheckAction = function(result) {
	try {
		let bind = '';
		let data = JSON.parse(result);
		
		if(data.rows == null || data.rows.length == 0) {
			$('#sql_bind_performance_check').textbox('setValue', NO_DATA);
			
			return;
		}
		
		let rows = data.rows;
		let row;
		
		for(let bindIndex = 0; bindIndex < rows.length; bindIndex++) {
			if(bindIndex > 0) {
				bind += '\n';
			}
			
			row = rows[bindIndex];
			bind += row.name + SPACE + row.value_string;
		}
		
		$('#sql_bind_performance_check').textbox('setValue', bind);
	} catch(err) {
		parent.$.messager.alert('에러', '성능 점검 SQL - SQL BIND</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_SqlPlanPerformanceCheckAction = function(result) {
	json_string_callback_common(result,'#sql_plan_performance_check',true);
}

var callback_SqlTextAll = function(result) {
	try {
		if(result.result){
			var post = result.object;
			//$("#textArea").html(strReplace(post.sql_text, "\r\n", "<br/>"));
			if(post != null){
				$('#sql_text_operation').textbox('setValue', post[0].sql_text);
			}
		}else{
			parent.$.messager.alert('SQL TEXT',result.message);
		}
	} catch(err) {
		parent.$.messager.alert('에러', '운영 SQL - SQL TEXT</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_SqlBindOperationAction = function(result) {
	try {
		let bind = '';
		let data = JSON.parse(result);
		
		if(data.rows == null || data.rows.length == 0) {
			$('#sql_bind_operation').textbox('setValue', NO_DATA);
			
			return;
		}
		
		let rows = data.rows;
		let row;
		for(let bindIndex = 0; bindIndex < rows.length; bindIndex++) {
			if(bindIndex > 0) {
				bind += '\n';
			}
			
			row = rows[bindIndex];
			bind += row.name + SPACE + row.value_string;
		}
		
		$('#sql_bind_operation').textbox('setValue', bind);
	} catch(err) {
		parent.$.messager.alert('에러', '운영 SQL - SQL BIND</br>' + err.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

var callback_SqlPlanOperationAction = function(result) {
	json_string_callback_common(result,'#sql_plan_operation',true);
}