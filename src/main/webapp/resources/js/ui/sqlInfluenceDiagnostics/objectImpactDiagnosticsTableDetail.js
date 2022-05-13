$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 성능임팩트유형코드 조회			
	$('#selPerfImpactTypeCd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1025",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	

	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : true,
		columns:[[
		    {field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true",rowspan:2},
		    {field:'sql_perf_impl_anal_no',hidden:'true',rowspan:2},
		    {field:'plan_change_yn',title:'실행계획<br/>변경여부',halign:"center",align:'center',sortable:"true",rowspan:2},
		    {field:'sql_profile_yn',title:'SQL PROFILE<br/>적용여부',halign:"center",align:'center',sortable:"true",rowspan:2},
		    {field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",rowspan:2},
		    {field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true",rowspan:2},		    
		    {field:'perf_impact_type_nm',title:'성능임팩트유형',halign:"center",align:'center',sortable:"true",rowspan:2},
		    {title:'BEFORE',halign:"center",align:'center',colspan:4},
		    {title:'AFTER',halign:"center",align:'center',colspan:4},
		    {field:'elapsed_time_increase_ratio',title:'수행시간<br/>임팩트',halign:"center",align:'right',sortable:"true",rowspan:2,formatter:dataRatio},
		    {field:'buffer_increase_ratio',title:'버퍼<br/>임팩트',halign:"center",align:'right',sortable:"true",rowspan:2,formatter:dataRatio}
		 ],[
		  	{field:'before_plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
		  	{field:'before_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
		  	{field:'before_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		  	{field:'before_executions',title:'EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'after_plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
		    {field:'after_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
		    {field:'after_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'after_executions',title:'EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],
		rowStyler: function(index,row){
			if(row.perf_impact_type_nm == 'Regressed'){
				return 'background-color:#f76e6e;color:#fff;'; // return inline style
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	var chkValue = false;
	
	if($("#plan_change_yn").val() == "Y") chkValue = true;
	
	$('#chkPlanChangeYn').switchbutton({
		checked: chkValue,
		onText:"Yes",
		offText:"전체",
		onChange: function(checked){
			if(checked) $("#plan_change_yn").val("Y"); 
			else $("#plan_change_yn").val("N");
		}
	});
	
	$('#selPerfImpactTypeCd').combobox("setValue",$("#perf_impact_type_cd").val());

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("오브젝트 진단 상세"," "); 
	
	ajaxCall("/ObjectImpactDiagnostics/TableDetailAction",
		$("#submit_form"),
		"POST",
		callback_ObjectImpactDiagnosticsTableDetailActionAddTable);	
});

//callback 함수
var callback_ObjectImpactDiagnosticsTableDetailActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	$("#tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

function Btn_OnClick(){
	$("#perf_impact_type_cd").val($('#selPerfImpactTypeCd').combobox("getValue"));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$("#submit_form").attr("action","/ObjectImpactDiagnostics/TableDetail");
//	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단 상세 내역");
	$("#submit_form #menu_nm").val("오브젝트변경 SQL 성능 영향도 진단 상세 내역");
	$("#submit_form").submit();		
}

function goList(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ObjectImpactDiagnostics/Table");
//	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단 테이블 내역");
	$("#submit_form #menu_nm").val("오브젝트변경 SQL 성능 영향도 진단 테이블 내역");
	$("#submit_form").submit();	
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

	$("#sql_id").val(rows[0].sql_id);
	$("#before_plan_hash_value").val(rows[0].before_plan_hash_value);
	
	ajaxCall("/ObjectImpactDiagnostics/UpdateSQLProfile",
			$("#submit_form"),
			"POST",
			callback_UpdateSQLProfileAction);
}

//callback 함수
var callback_UpdateSQLProfileAction = function(result) {
	if(result.result){
		Btn_OnClick();
	}else{
		parent.$.messager.alert('',result.message);
	}
};