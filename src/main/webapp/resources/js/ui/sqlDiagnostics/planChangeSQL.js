$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : true,		
		onClickRow : function(index,row) {
			parent.$.messager.confirm({
				title : 'SQL 정보',
				msg : 'PLAN_HASH_VALUE를 선택하세요.<br/><br/><br/>AFTER_PLAN_HASH_VALUE -> AFTER<br/>BEFORE_PLAN_HASH_VALUE -> BEFORE',
				ok : 'AFTER',
				cancel : 'BEFORE',
				fn : function(r){
					if (r){
						// 신규 탭 생성..
						parent.createSQLNewTab($("#menu_id").val(), "sqlTabs", $("#dbid").val(), row.sql_id, row.plan_hash_value);
					}else{
						// 신규 탭 생성..
						parent.createSQLNewTab($("#menu_id").val(), "sqlTabs", $("#dbid").val(), row.sql_id, row.before_plan_hash_value);
					}
				}
			});
		},			
//		columns:[[
//		    {field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true", rowspan:2},
//		    {field:'tuning_tgt_yn',title:'튜닝요청',width:"6%",halign:"center",align:'center',rowspan:2},
//		    {field:'sql_profile_yn',title:'SQL_PROFILE<br/>적용여부',width:"7%",halign:"center",align:'center',rowspan:2},
//		    {field:'gather_day',title:'기준일자',width:"8%",halign:"center",align:'center',sortable:"true", rowspan:2,formatter:getDateFormat},
//		    {field:'sql_id',title:'SQL_ID',width:"12%",halign:"center",align:"center",sortable:"true", rowspan:2},
//		    {title:'PLAN_HASH_VALUE',halign:"center",colspan:2},
//		    {title:'ELAPSED_TIME',halign:"center",colspan:2},
//		    {title:'CPU_TIME',halign:"center",colspan:2},
//		    {title:'BUFFER_GETS',halign:"center",colspan:2},
//		    {title:'EXECUTIONS',halign:"center",colspan:2},
//		    {title:'MAXELAPSED_TIME',halign:"center",colspan:2}
//		],[
//			{field:'before_plan_hash_value',title:'BEFORE',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'plan_hash_value',title:'AFTER',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'before_elapsed_time',title:'BEFORE',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'after_elapsed_time',title:'AFTER',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'before_cpu_time',title:'BEFORE',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'after_cpu_time',title:'AFTER',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'before_buffer_gets',title:'BEFORE',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'after_buffer_gets',title:'AFTER',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'before_executions',title:'BEFORE',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'after_executions',title:'AFTER',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'before_max_elapsed_time',title:'BEFORE',width:"6%",halign:"center",align:'right',sortable:"true"},
//			{field:'after_max_elapsed_time',title:'AFTER',width:"6%",halign:"center",align:'right',sortable:"true"}
//		]],
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true", rowspan:2},
		    {field:'tuning_tgt_yn',title:'튜닝요청',halign:"center",align:'center',rowspan:2},
		    {field:'sql_profile_yn',title:'SQL_PROFILE<br/>적용여부',halign:"center",align:'center',rowspan:2},
		    {field:'gather_day',title:'기준일자',halign:"center",align:'center',sortable:"true", rowspan:2,formatter:getDateFormat},
		    {field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true", rowspan:2},
		    {title:'PLAN_HASH_VALUE',halign:"center",colspan:2},
		    {title:'ELAPSED_TIME',halign:"center",colspan:2},
		    {title:'CPU_TIME',halign:"center",colspan:2},
		    {title:'BUFFER_GETS',halign:"center",colspan:2},
		    {title:'EXECUTIONS',halign:"center",colspan:2},
		    {title:'MAXELAPSED_TIME',halign:"center",colspan:2}
		],[
			{field:'before_plan_hash_value',title:'BEFORE',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'AFTER',halign:"center",align:'center',sortable:"true"},
			{field:'before_elapsed_time',title:'BEFORE',halign:"center",align:'right',sortable:"true"},
			{field:'after_elapsed_time',title:'AFTER',halign:"center",align:'right',sortable:"true"},
			{field:'before_cpu_time',title:'BEFORE',halign:"center",align:'right',sortable:"true"},
			{field:'after_cpu_time',title:'AFTER',halign:"center",align:'right',sortable:"true"},
			{field:'before_buffer_gets',title:'BEFORE',halign:"center",align:'right',sortable:"true"},
			{field:'after_buffer_gets',title:'AFTER',halign:"center",align:'right',sortable:"true"},
			{field:'before_executions',title:'BEFORE',halign:"center",align:'right',sortable:"true"},
			{field:'after_executions',title:'AFTER',halign:"center",align:'right',sortable:"true"},
			{field:'before_max_elapsed_time',title:'BEFORE',halign:"center",align:'right',sortable:"true"},
			{field:'after_max_elapsed_time',title:'AFTER',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#sqlTabs').tabs({
		plain : true
	});	
	
	$('#selectDbidCombo').combobox("setValue",$("#dbid").val());
	
	Btn_OnClick();	
});

function Btn_OnClick(){
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 시작일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 종료일자를 선택해 주세요.');
		return false;
	}	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		

	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 진단 - PLAN 변경 SQL"," "); 
	
	ajaxCall("/SQLDiagnostics/PlanChangeSQL",
		$("#submit_form"),
		"POST",
		callback_PlanChangeSQLAddTable);		
}

//callback 함수
var callback_PlanChangeSQLAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

//function showTuningReqPopup(){	
//	var sqlIdArry = "";
//	var gatherDayArry = "";
//	var planHashValueArry = "";
//	var rows = "";
//	
//	if($('#selectDbidCombo').combobox('getValue') == ""){
//		parent.$.messager.alert('','DB를 선택해 주세요.');
//		return false;
//	}	
//	
//	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();
//	
//	rows = $('#tableList').datagrid('getChecked');
//	
//	if(rows.length > 0){
//		parent.$('#tuningAssignPop').window("open");
//		
//		parent.$('#assign_form #dbid').val("");
//		parent.$('#assign_form #auto_share').val("N");
//		parent.$('#assign_form #sqlIdArry').val("");
//		parent.$('#assign_form #planHashValueArry').val("");
//		parent.$('#assign_form #gatherDayArry').val("");
//		parent.$('#assign_form #perfr_id').val("");
//		parent.$('#assign_form #strGubun').val("");
//
//		for(var i = 0 ; i < rows.length ; i++){
//			sqlIdArry += rows[i].sql_id + "|";
//			gatherDayArry += rows[i].gather_day + "|";
//			planHashValueArry += rows[i].plan_hash_value + "|";
//		}
//		
//		parent.$("#assign_form #sqlIdArry").val(strRight(sqlIdArry,1));
//		parent.$("#assign_form #gatherDayArry").val(strRight(gatherDayArry,1));
//		parent.$("#assign_form #planHashValueArry").val(strRight(planHashValueArry,1));
//
//		parent.$('#assign_form #dbid').val($('#dbid').val());
//		parent.$('#assign_form #choice_div_cd').val($('#choice_id').val());
//		parent.$('#assign_form #table_name').val($('#tableName').val());
//		
//		$("#submit_form #choice_cnt").val(rows.length);
//
//		// 튜닝 담당자 조회			
//		parent.$('#assign_form #selectTuner').combobox({
//		    url:"/Common/getTuner?dbid="+$("#dbid").val(),
//		    method:"get",
//			valueField:'tuner_id',
//		    textField:'tuner_nm'
//		});		
//	}else{
//		parent.$.messager.alert('','튜닝 대상을 선택해 주세요.');
//	}
//}

function showTuningReqPopup(){	
	var sqlIdArry = "";
	var gatherDayArry = "";
	var planHashValueArry = "";
	var rows = "";
	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	rows = $('#tableList').datagrid('getChecked');
	initPopupSet();  //튜닝 요청 버그를 방지하기위해 초기화.
	
	if(rows.length > 0){
		$('#tuningAssignPop').window("open");
		
		$('#assign_form #dbid').val("");
		$('#assign_form #auto_share').val("N");
		$('#assign_form #sqlIdArry').val("");
		$('#assign_form #planHashValueArry').val("");
		$('#assign_form #gatherDayArry').val("");
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("");

		for(var i = 0 ; i < rows.length ; i++){
			sqlIdArry += rows[i].sql_id + "|";
			gatherDayArry += rows[i].gather_day + "|";
			planHashValueArry += rows[i].plan_hash_value + "|";
		}
		
		$("#assign_form #sqlIdArry").val(strRight(sqlIdArry,1));
		$("#assign_form #gatherDayArry").val(strRight(gatherDayArry,1));
		$("#assign_form #planHashValueArry").val(strRight(planHashValueArry,1));

		$('#assign_form #dbid').val($('#dbid').val());
		$('#assign_form #choice_div_cd').val($('#choice_id').val());
		$('#assign_form #table_name').val($('#tableName').val());
		
		$("#submit_form #choice_cnt").val(rows.length);

		// 튜닝 담당자 조회			
		$('#assign_form #selectTuner').combobox({
		    url:"/Common/getTuner?dbid="+$("#dbid").val(),
		    method:"get",
			valueField:'tuner_id',
		    textField:'tuner_nm'
		});		
	}else{
		parent.$.messager.alert('','튜닝 대상을 선택해 주세요.');
	}
}

function Show_SqlProfilePopup(){
	var profileText = "SPOP_SQLPROFILE_";	
	var rows = $('#tableList').datagrid('getChecked');
	var profileYn = "";
	var sqlId = "";
	var planHashValue = "";

	if(rows.length > 0){
		if(rows.length > 1){
			parent.$.messager.alert('','SQL Profile 적용할 하나의 Row만 선택해 주세요.');
		}else{
			profileYn = rows[0].sql_profile_yn;
			
			if(profileYn == "Y"){
				parent.$.messager.alert('','SQL Profile을 이미 적용하였습니다.');
			}else{
//				parent.$("#profile_form #dbid").val($('#dbid').val());
//				
//				sqlId = rows[0].sql_id;
//				
//				planHashValue = rows[0].before_plan_hash_value;
//				parent.$("#profile_form #plan_hash_value").val(planHashValue);
//
//				parent.$("#profile_form #sqlId").textbox("setValue",sqlId);
//				parent.$("#profile_form #sql_id").val(sqlId);	
//
//				parent.$("#profile_form #planHashValue").textbox("setValue",planHashValue);
//
//				profileText += sqlId;
//				
//				parent.$("#profile_form #sqlProfile").textbox("setValue",profileText);			
//				parent.$('#sqlProfilePop').window("open");

				$("#profile_form #dbid").val($('#dbid').val());
				
				sqlId = rows[0].sql_id;
				
				planHashValue = rows[0].before_plan_hash_value;
				$("#profile_form #plan_hash_value").val(planHashValue);

				$("#profile_form #sqlId").textbox("setValue",sqlId);
				$("#profile_form #sql_id").val(sqlId);	

				$("#profile_form #planHashValue").textbox("setValue",planHashValue);

				profileText += sqlId;
				
				$("#profile_form #sqlProfile").textbox("setValue",profileText);			
				$('#sqlProfilePop').window("open");					
			}
		}
	}else{
		parent.$.messager.alert('','SQL Profile 적용할 Row를 선택해 주세요.');
	}
}

function SqlProfileUpdate(){
	var rows = $('#tableList').datagrid('getChecked');
	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	$("#sql_id").val(rows[0].sql_id);
	$("#gather_day").val(rows[0].gather_day);
	$("#before_plan_hash_value").val(rows[0].before_plan_hash_value);
	$("#plan_hash_value").val(rows[0].plan_hash_value);
	
	ajaxCall("/SQLDiagnostics/UpdatePlanChangeSQLProfile",
			$("#submit_form"),
			"POST",
			callback_UpdatePlanChangeSQLProfileAction);
}

//callback 함수
var callback_UpdatePlanChangeSQLProfileAction = function(result) {
	if(result.result){
		Btn_OnClick();
	}
};