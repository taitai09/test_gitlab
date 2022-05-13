$(document).ready(function() {
	$('#projectSqlIdfyConditionPop').window({
		title : "SQL 성능점검대상 테이블 일괄등록 ",
		top:getWindowTop(550),
		left:getWindowLeft(700)
	});
	
	var selTbl = $("#selectList").datagrid({
		view: myview,
		rownumbers:true,
		singleSelect:false,
		columns:[[
			{field:'table_name',title:'테이블명',width:"100%",halign:"center",align:"left",sortable:"true"}
		]],

		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#selectList').datagrid("loadData", []);
	
	selTbl.datagrid('enableFilter');
	
	$("#targetList").datagrid({
		rownumbers: true,
		singleSelect:false,
		columns:[[
			{field:'owner',title:'TABLE_OWNER',width:"40%",halign:"center",align:"center"},
			{field:'table_name',title:'TABLE_NAME',width:"60%",halign:"center",align:"left"}
		]],

		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#targetList').datagrid("loadData", []);

})

function Btn_TableBatchPopUpSelTableOwner(){
	$("#projectSqlIdfyConditionPop_form #selectComboOrigin").combobox({
		url:"/SQLAutomaticPerformanceCheck/loadOriginalDb?project_id="+$('#projectSqlIdfyConditionPop_form #project_id').val()+"&isChoice=Y",
		method:"get",
		valueField:'original_dbid',
		textField:'original_db_name',
		panelHeight: 300,
		onLoadError: function(){
			/* modal progress close */
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onChange: function(newValue, oldValue) {
			if(newValue == '') {
				$("#projectSqlIdfyConditionPop_form #selTableOwner").combobox('clear');
				$("#projectSqlIdfyConditionPop_form #selTableOwner").combobox('loadData', []);
				
				return;
			}
			
			$("#projectSqlIdfyConditionPop_form #dbid").val(newValue);
			
			$('#projectSqlIdfyConditionPop_form #selTableOwner').combobox({
				url:"/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/GetUserNameComboBox?dbid="+$("#projectSqlIdfyConditionPop_form #dbid").val()+"&isChoice=Y",
				method:'get',
				valueField:'user_id',
				textField:'user_nm',
				onSelect: function(rec) {
					if(rec.user_id != '') {
						$("#projectSqlIdfyConditionPop_form #owner").val(rec.user_id);
						
						getOwnerSelectTable(rec);					// select table
						getSelectProjectSqlIdfyConditionTable();	// target table
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					return false;
				},
				onLoadSuccess: function(items) {
					/* modal progress close */
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
					
					if (items.length){
						let owner = $('#projectSqlIdfyConditionPop_form #owner').val();
						
						if(owner !== '') {
							$(this).combobox("setValue", owner);
						} else {
							var opts = $(this).combobox('options');
							$(this).combobox('select', items[0][opts.valueField]);
						}
					}
				}
			});
		},
		onLoadSuccess: function(items) {
			/* modal progress close */
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			if (items.length){
				let dbid = $("#projectSqlIdfyConditionPop_form #dbid").val();
				
				if(dbid !== '') {
					$(this).combobox("setValue", dbid);
				} else {
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
		}
	});
}

function getOwnerSelectTable(rec){
	try {
		ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Popup/GetProjectSqlIdfyConditionCheck",
				$("#projectSqlIdfyConditionPop_form"),
				"POST",
				callback_GetProjectSqlIdfyConditionCheck);
	} catch(err) {
		consoloe.error(err.message);
	}
}

var callback_GetProjectSqlIdfyConditionCheck = function(result) {
	var data = JSON.parse(result);
	
	console.log("data.rows.length :",data.rows.length);
	
	if(data.rows.length > 0){
		if(data.rows[0].owner_yn != undefined && data.rows[0].owner_yn == 'Y'){
			parent.$.messager.alert('','해당 TABLE은 OWNER 범위로 프로젝트가 등록되어 있습니다. TABLE 범위로 등록이 필요할 경우 해당 OWNER를 삭제 바랍니다.');
			Btn_OnClosePopup("projectSqlIdfyConditionPop");
			return;
		}
	}
	
	$('#selectList').datagrid('loadData',[]);
	$('#selectList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#selectList').datagrid('loading');
	ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Popup/GetSelectTable",
			$("#projectSqlIdfyConditionPop_form"),
			"POST",
			callback_GetSelectTableAdd);
}
var callback_GetSelectTableAddFilter = function(result) {
	var data = JSON.parse(result);

	if (data.rows.length < 1){
		$('#selectList').datagrid("loadData", data);
		$('#selectList').datagrid('loaded');
		return;
	}
	
	var targetRows = $('#targetList').datagrid('getRows');
	var tableOwner = $("#selTableOwner").combobox("getValue");

	if(tableOwner.length < 1){
		$.messager.alert('','SQL 자동성늠점검 대상 테이블을 먼저 선택해 주세요.');
		return false;
	}

	$('#projectSqlIdfyConditionPop_form #selectList').datagrid('loadData',[]);
	
	for(var i = 0 ; i < data.rows.length; i++){
		var dup_chk = false;
		for(var j = 0 ; j < targetRows.length; j++){
			if(data.rows[i].owner == targetRows[j].owner && data.rows[i].table_name == targetRows[j].table_name){
				dup_chk = true;
				break;
			}
		}
		if (dup_chk){
			continue;
		}
		$('#selectList').datagrid('appendRow',{
			owner : tableOwner,
			table_name : data.rows[i].table_name
		});
	}
	$('#selectList').datagrid('loaded');
}
//callback 함수
var callback_GetSelectTableAdd = function(result) {
	var data = JSON.parse(result);
	$('#selectList').datagrid("loadData", data);
	$('#selectList').datagrid('loaded');
};

function getSelectProjectSqlIdfyConditionTable(){
	console.log("getSelectProjectSqlIdfyConditionTable");
	
	try {
		console.log("conditionTable dbid[" +$('#projectSqlIdfyConditionPop_form #dbid').val() + "]");
		
		ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Popup/GetSelectProjectSqlIdfyConditionTable",
				$("#projectSqlIdfyConditionPop_form"),
				"POST",
				callback_GetSelectProjectSqlIdfyConditionTable);
	} catch(err) {
		console.error(err.message);
		return;
	}
}
//callback 함수
var callback_GetSelectProjectSqlIdfyConditionTable = function(result) {
	console.log("callback_GetSelectProjectSqlIdfyConditionTable");
	var data = JSON.parse(result);
	$('#targetList').datagrid("loadData", data);
	$('#targetList').datagrid('loaded');
};


function Btn_BatchInsertSave(){
	var tableOwnerArry = "";
	var tableNameArry = "";
	
	if($('#projectSqlIdfyConditionPop_form #selTableOwner').combobox('getValue') == ""){
		$.messager.alert('','OWNER 를 선택해 주세요.');
		return false;
	}

	rows = $('#targetList').datagrid('getRows');
	
	if(rows.length < 1) {
		parent.$.messager.confirm('Confirm','테이블 일괄 삭제를  요청하시겠습니까?',function(r){
		if (! r){
			return;
		}
		
		$("#projectSqlIdfyConditionPop_form #crud_flag").val("D");
		$("#projectSqlIdfyConditionPop_form #tableOwnerArry").val("");
		$("#projectSqlIdfyConditionPop_form #tableNameArry").val("");

		ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Popup/Save",
				$("#projectSqlIdfyConditionPop_form"),
				"POST",
				callback_BatchInsertSaveAction);
		});
		
	} else {
		
		for(var i = 0 ; i < rows.length; i++){
			tableOwnerArry += rows[i].owner + "|";
			tableNameArry += rows[i].table_name + "|";
		}
		
		$("#projectSqlIdfyConditionPop_form #crud_flag").val("C");
		$("#projectSqlIdfyConditionPop_form #tableOwnerArry").val(strRight(tableOwnerArry,1));
		$("#projectSqlIdfyConditionPop_form #tableNameArry").val(strRight(tableNameArry,1));

		ajaxCall("/SQLAutomaticPerformanceCheck/SQLAutomaticPerformanceCheckTargetMng/Popup/Save",
				$("#projectSqlIdfyConditionPop_form"),
				"POST",
				callback_BatchInsertSaveAction);
	}
	
	//if(rows.length < 1){
	//	$.messager.alert('','SQL 자동성늠점검 대상 테이블은 최소 하나 이상은 존재해야 합니다.<br/>테이블 목록을 선택해 주세요.');
	//	return false;
	//}
}


//callback 함수
var callback_BatchInsertSaveAction = function(result) {
	var msg = "";
	if ($("#projectSqlIdfyConditionPop_form #crud_flag").val() == "C"){
		msg = 'SQL 자동성늠점검 대상 테이블이 정상적으로  등록되었습니다.';
	}
	else{
		msg = 'SQL 자동성늠점검 대상 테이블이 정상적으로  삭제되었습니다.';
	}
	
	if(result.result){
		$.messager.alert('',msg,'info',function(){
				setTimeout(function() {
					Btn_OnClosePopup("projectSqlIdfyConditionPop");
					Btn_OnClick( $("#projectSqlIdfyConditionPop_form #project_id").val() );
				},1000);
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
};

function Btn_AddTarget(){
	rows = $('#selectList').datagrid('getSelections');
	var tableOwner = $("#selTableOwner").combobox("getValue");
	
	if(rows.length < 1){
		$.messager.alert('','SQL 자동성늠점검 대상 테이블을 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){
		$('#targetList').datagrid('appendRow',{
			owner : tableOwner,
			table_name : rows[i].table_name
		});
		
		$('#selectList').datagrid('deleteRow', $('#selectList').datagrid('getRowIndex', rows[i]));
	}
}

function Btn_RemoveTarget(){
	rows = $('#targetList').datagrid('getSelections');
	var tableOwner = $("#selTableOwner").combobox("getValue");
	var errCnt = 0;
	
	if(rows.length < 1){
		$.messager.alert('','SQL 자동성늠점검 제외 테이블을 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){
		if(tableOwner == rows[i].owner){
			$('#selectList').datagrid('appendRow',{
				table_name : rows[i].table_name
			});
			
			$('#targetList').datagrid('deleteRow', $('#targetList').datagrid('getRowIndex', rows[i]));
		}else{
			errCnt++;
		}
	}
	
	if(errCnt > 0){
		//$.messager.alert('',tableOwner + '와 일치하지 않은 TABLE_OWNER가 존재합니다.<br/>일치하는 TABLE_OWNER을 선택 후 삭제해 주세요.');
		$.messager.alert('','TABLE_OWNER가 서로 일치하지 않습니다.<br/>일치하는 TABLE_OWNER을 선택 후 삭제해 주세요.');
		return false;
	}
}
