$(document).ready(function() {
/*	
	$('#topSqlSchedulePop').window({
		title : "DB변경 SQL영향도 진단 > 스케쥴 등록",
		top:getWindowTop(270),
		left:getWindowLeft(700)
	});	
	
	$('#objectSchedulePop').window({
		title : "오브젝트 진단 > 스케쥴 등록",
		top:getWindowTop(550),
		left:getWindowLeft(700)
	});		
*/
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
	
	selTbl.datagrid('enableFilter');
	
	$("#targetList").datagrid({
		rownumbers: true,
		singleSelect:false,
		columns:[[
			{field:'table_owner',title:'TABLE_OWNER',width:"40%",halign:"center",align:"center"},
			{field:'table_name',title:'TABLE_NAME',width:"60%",halign:"center",align:"left"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	// Database 조회			
	$('#topSql_form #selectDbidCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	// Database 조회			
	$('#object_form #selectDbidCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	if(rec.dbid == "") return;
	    	$("#object_form #dbid").val(rec.dbid);
	    	// 테이블 리스트	    	
	    	$('#object_form #selTableOwner').combobox({
	    	    url:"/ObjectImpactDiagnostics/Popup/GetTableOwner?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'user_id',
	    	    textField:'username',
	    	    onSelect:function(rec1){	    	    	
	    	    	$("#object_form #table_owner").val(rec1.user_id);
	    	    	
	    	    	$('#selectList').datagrid('loadData',[]);
	    	    	$('#selectList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	    	    	$('#selectList').datagrid('loading');	
	    	    	ajaxCall("/ObjectImpactDiagnostics/Popup/GetSelectTable",
	    	    			$("#object_form"),
	    	    			"POST",
	    	    			callback_GetSelectTableAdd);	
	    	    }
	    	});	
	    }
	});	
	
	$('#topSql_form #chkImmediately').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#topSql_form #immediately_yn").val("Y");
				$("#topSql_form #expect_work_exec_day").datebox({disabled:true});
				$("#topSql_form #expect_work_exec_time").timespinner({disabled:true});
				
				$("#topSqlImmediatelySpan").html("(2분후 실행)");
			}else{
				$("#topSql_form #immediately_yn").val("N");
				$("#topSql_form #expect_work_exec_day").datebox({disabled:false});
				$("#topSql_form #expect_work_exec_time").timespinner({disabled:false});
				
				$("#topSqlImmediatelySpan").html("");
			}
		}
	})	
	
	$('#object_form #chkImmediately').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#object_form #immediately_yn").val("Y");
				$("#object_form #anal_work_exec_day").datebox({disabled:true});
				$("#object_form #anal_work_exec_time").timespinner({disabled:true});
				
				$("#objectImmediatelySpan").html("(2분후 실행)");
			}else{
				$("#object_form #immediately_yn").val("N");
				$("#object_form #anal_work_exec_day").datebox({disabled:false});
				$("#object_form #anal_work_exec_time").timespinner({disabled:false});
				
				$("#objectImmediatelySpan").html("");
			}
		}
	})

	// TOP순위유형 조회			
	$('#top_rank_measure_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1024",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	
	$("#removeSqlBtn").linkbutton({disabled:true});
	$("#removeObjectBtn").linkbutton({disabled:true});	
});

//callback 함수
var callback_GetSelectTableAdd = function(result) {
	var data = JSON.parse(result);
	$('#selectList').datagrid("loadData", data);
	$('#selectList').datagrid('loaded');	
};

function showSnapPopup(gubun){
	console.log("gubun:"+gubun);
	$('#snapList_form #snap_id').val("");

	strGb = gubun;
	
	if(gubun == "sql"){
		if($('#topSql_form #selectDbidCombo').combobox('getValue') == ""){
			$.messager.alert('','DB를 선택해 주세요.');
			return false;
		}	
		
		$("#snapList_form #dbid").val($('#topSql_form #selectDbidCombo').combobox('getValue'));		
		$('#snapList_form #dbName').textbox('setValue',$('#topSql_form #selectDbidCombo').combobox('getText'));
	}else{
		if($('#object_form #selectDbidCombo').combobox('getValue') == ""){
			$.messager.alert('','DB를 선택해 주세요.');
			return false;
		}	

		$("#snapList_form #dbid").val($('#object_form #selectDbidCombo').combobox('getValue'));		
		$('#snapList_form #dbName').textbox('setValue',$('#object_form #selectDbidCombo').combobox('getText'));
	}
	
	$('#startSnapIdList').datagrid('loadData',[]);
	$('#endSnapIdList').datagrid('loadData',[]);
	
//	$("#snapList_form #strStartDt").datebox("setValue", startDate);
//	$("#snapList_form #strEndDt").datebox("setValue", nowDate);
	$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
	$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);

	$('#snapListPop').window("open");
}

function Btn_ScheduleSave(form){
	var tableOwnerArry = "";
	var tableNameArry = "";
	
	if(form == "sql"){
		if($('#topSql_form #selectDbidCombo').combobox('getValue') == ""){
			$.messager.alert('','DB를 선택해 주세요.');
			return false;
		}else{
			$("#topSql_form #dbid").val($('#topSql_form #selectDbidCombo').combobox('getValue'));
		}
		
		if($('#topSql_form #begin_snap_id').textbox('getValue') == ""){
			$.messager.alert('','시작 스냅샷ID를 선택해 주세요.');
			return false;
		}
		
		if($('#topSql_form #end_snap_id').textbox('getValue') == ""){
			$.messager.alert('','종료 스냅샷ID를 선택해 주세요.');
			return false;
		}
		
		var immediately_yn =  $("#topSql_form #immediately_yn").val();
		console.log("immediately_yn:"+immediately_yn);
		if(immediately_yn != "Y"){
			if($('#topSql_form #expect_work_exec_day').datebox('getValue') == ""){
				$.messager.alert('','작업수행일자를 입력해 주세요.');
				return false;
			}
			
			if($('#topSql_form #expect_work_exec_time').timespinner('getValue') == ""){
				$.messager.alert('','작업수행시간를 입력해 주세요.');
				return false;
			}
		}

		
		if($('#topSql_form #top_rank_measure_type_cd').combobox('getValue') == ""){
			$.messager.alert('','TOP순위측정유형을 선택해 주세요.');
			return false;
		}
		
		if($('#topSql_form #topn_cnt').textbox('getValue') == ""){
			$.messager.alert('','TOP N 건수를 입력해 주세요.');
			return false;
		}
		$("#saveObjectBtn").prop( "disabled", true );
		ajaxCall("/TOPSQLDiagnostics/Popup/Save",
				$("#topSql_form"),
				"POST",
				callback_SaveTOPSQLDiagnosticsAction);		
	}else{
		if($('#object_form #selectDbidCombo').combobox('getValue') == ""){
			$.messager.alert('','DB를 선택해 주세요.');
			return false;
		}else{
			$("#object_form #dbid").val($('#object_form #selectDbidCombo').combobox('getValue'));
		}
		
		if($('#object_form #begin_snap_id').textbox('getValue') == ""){
			$.messager.alert('','시작 스냅샷ID를 선택해 주세요.');
			return false;
		}
		
		if($('#object_form #end_snap_id').textbox('getValue') == ""){
			$.messager.alert('','종료 스냅샷ID를 선택해 주세요.');
			return false;
		}
		
		var immediately_yn =  $("#object_form #immediately_yn").val();
		console.log("immediately_yn:"+immediately_yn);
		if(immediately_yn != "Y"){
			if($('#object_form #anal_work_exec_day').datebox('getValue') == ""){
				$.messager.alert('','작업수행일자를 입력해 주세요.');
				return false;
			}
			
			if($('#object_form #anal_work_exec_time').timespinner('getValue') == ""){
				$.messager.alert('','작업수행시간를 입력해 주세요.');
				return false;
			}
		}
		
		$("#object_form #dbid").val($('#object_form #selectDbidCombo').combobox('getValue'));
		
		rows = $('#targetList').datagrid('getRows');
		
		if(rows.length < 1){
			$.messager.alert('','분석대상 테이블은 최소 하나 이상은 존재해야 합니다.<br/>분석대상 테이블 목록을 선택해 주세요.');
			return false;
		}

		for(var i = 0 ; i < rows.length; i++){
			tableOwnerArry += rows[i].table_owner + "|";
			tableNameArry += rows[i].table_name + "|";
		}
		
		$("#object_form #tableOwnerArry").val(strRight(tableOwnerArry,1));
		$("#object_form #tableNameArry").val(strRight(tableNameArry,1));

		$("#saveObjectBtn").prop( "disabled", true );
		ajaxCall("/ObjectImpactDiagnostics/Popup/Save",
				$("#object_form"),
				"POST",
				callback_SaveObjectDiagnosticsAction);			
	}
}

//callback 함수
var callback_SaveTOPSQLDiagnosticsAction = function(result) {
	var msg = "";
	var crud_flag = $("#topSql_form #crud_flag").val();
	if(crud_flag == "C") msg = '정상적으로 스케쥴이 등록되었습니다.'; 
	if(crud_flag == "U") msg = '정상적으로 스케쥴이 변경되었습니다.'; 
	if(result.result){
		$.messager.alert('',msg,'info',function(){
			//var selectComboValue = eval("if_"+frameName).$("#submit_form #selectDbidCombo").combobox("getValue");
			var selectComboValue = $("#submit_form #selectDbidCombo").combobox("getValue");
			if(selectComboValue != ""){
				setTimeout(function() {
					Btn_OnClosePopup("topSqlSchedulePop");
//					eval("if_"+frameName).Btn_OnClick();
					Btn_OnClick();
				},1000);			
			}else{
				setTimeout(function() {
					resetTopSqlValue();
					Btn_OnClosePopup("topSqlSchedulePop");
				},1000);
			}			
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
	$("#saveObjectBtn").prop( "disabled", false );
};

//callback 함수
var callback_SaveObjectDiagnosticsAction = function(result) {
	var msg = "";
	var crud_flag = $("#object_form #crud_flag").val();
	if(crud_flag == "C") msg = '정상적으로 스케쥴이 등록되었습니다.'; 
	if(crud_flag == "U") msg = '정상적으로 스케쥴이 변경되었습니다.'; 
	if(result.result){
		$.messager.alert('',msg,'info',function(){
			//var selectComboValue = eval("if_"+frameName).$("#submit_form #selectDbidCombo").combobox("getValue");
			var selectComboValue = $("#submit_form #selectDbidCombo").combobox("getValue");
			if(selectComboValue != ""){
				setTimeout(function() {
					Btn_OnClosePopup("objectSchedulePop");
//					eval("if_"+frameName).Btn_OnClick();
					Btn_OnClick();
				},1000);			
			}else{
				setTimeout(function() {
					resetObjectValue();
					Btn_OnClosePopup("objectSchedulePop");
				},1000);
			}			
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
	$("#saveObjectBtn").prop( "disabled", false );
};

function Btn_ScheduleDelete(form){
	$.messager.confirm('', '스케줄을 삭제하시겠습니까?', function(r){
		if (r){
			if(form == "sql"){
				ajaxCall("/TOPSQLDiagnostics/Popup/Delete",
						$("#topSql_form"),
						"POST",
						callback_DeleteTOPSQLDiagnosticsAction);				
			}else{
				ajaxCall("/ObjectImpactDiagnostics/Popup/Delete",
						$("#object_form"),
						"POST",
						callback_DeleteObjectDiagnosticsAction);
			}
		}
	});
}

//callback 함수
var callback_DeleteTOPSQLDiagnosticsAction = function(result) {
	if(result.result){
		$.messager.alert('','정상적으로 스케쥴이 삭제되었습니다.','info',function(){
			//var selectComboValue = eval("if_"+frameName).$("#submit_form #selectCombo").combobox("getValue");
			var selectComboValue = $("#submit_form #selectDbidCombo").combobox("getValue");
			if(selectComboValue != ""){
				setTimeout(function() {
					Btn_OnClosePopup("topSqlSchedulePop");
//					eval("if_"+frameName).Btn_OnClick();
					Btn_OnClick();
				},1000);			
			}else{
				setTimeout(function() {
					resetTopSqlValue();
					Btn_OnClosePopup("topSqlSchedulePop");
				},1000);
			}			
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
};

//callback 함수
var callback_DeleteObjectDiagnosticsAction = function(result) {
	if(result.result){
		$.messager.alert('','정상적으로 스케쥴이 삭제되었습니다.','info',function(){
			//var selectComboValue = eval("if_"+frameName).$("#submit_form #selectCombo").combobox("getValue");
			var selectComboValue = $("#submit_form #selectDbidCombo").combobox("getValue");
			if(selectComboValue != ""){
				setTimeout(function() {
					Btn_OnClosePopup("objectSchedulePop");
//					eval("if_"+frameName).Btn_OnClick();
					Btn_OnClick();
				},1000);			
			}else{
				setTimeout(function() {
					resetObjectValue();
					Btn_OnClosePopup("objectSchedulePop");
				},1000);
			}			
		});
	}else{
		$.messager.alert('',result.message,'error');
	}
};

function Btn_AddTarget(){
	rows = $('#selectList').datagrid('getSelections');
	var tableOwner = $("#selTableOwner").combobox("getValue");
	
	if(rows.length < 1){
		$.messager.alert('','분석할 테이블을 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){
		$('#targetList').datagrid('appendRow',{
			table_owner : tableOwner,
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
		$.messager.alert('','적용 제외 테이블을 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){		
		if(tableOwner == rows[i].table_owner){
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

function resetTopSqlValue(){
	$("#topSql_form #snap_id").val("");
	$("#topSql_form #dbid").val("");
	$("#topSql_form #selectDbidCombo").combobox("setValue","");
	$("#topSql_form #begin_snap_id").textbox("setValue","");
	$("#topSql_form #end_snap_id").textbox("setValue","");
	$("#topSql_form #expect_work_exec_day").datebox({disabled:false});
	$("#topSql_form #expect_work_exec_time").timespinner({disabled:false});
	$("#topSql_form #top_rank_measure_type_cd").combobox("setValue","");
	$("#topSql_form #topn_cnt").textbox("setValue","");
	
	$("#snapBtn").linkbutton({disabled:false});
	$("#saveSqlBtn").linkbutton({disabled:false});	
}

function resetObjectValue(){
	$("#object_form #snap_id").val("");
	$("#object_form #immediately_yn").val("N");
	$('#object_form #chkImmediately').switchbutton({checked: false});
	$("#object_form #dbid").val("");
	$("#object_form #selectDbidCombo").combobox("setValue","");
	$("#object_form #begin_snap_id").textbox("setValue","");
	$("#object_form #end_snap_id").textbox("setValue","");
	$("#object_form #anal_work_exec_day").datebox({disabled:false});
	$("#object_form #anal_work_exec_time").timespinner({disabled:false});
	$("#object_form #selTableOwner").combobox("setValue","");
	$('#selectList').datagrid('loadData',[]);
	$('#targetList').datagrid('loadData',[]);	

	$("#saveObjectBtn").linkbutton({disabled:false});
	$("#snapOBtn").linkbutton({disabled:false});
	$("#addBtn").linkbutton({disabled:false});
	$("#removeBtn").linkbutton({disabled:false});	
}