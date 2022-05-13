$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadSuccess: function(items) {
			if (items.length){
				$(this).combobox('setValue', $("#dbid").val());
			}
			
			$("#selectDbidCombo").combobox("textbox").attr("placeholder","선택");
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickCell : function(index, field, value) {
			if(field == 'chk') {
				return;
			}
			
			var data = $(this).datagrid('getRows')[index];
			
			$("#submit_form #before_perf_expect_no").val(data.before_perf_expect_no);
			$("#submit_form #dbid").val($("#selectDbidCombo").combobox("getValue"));
			$("#submit_form #call_from_parent").val("Y");
			goTOPSQLDiagnosticsCheck();
		},
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : true,
		rownumbers:false,
		remoteSort:false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
		    {field:'before_perf_expect_no',title:'번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
		    {field:'dbid',hidden:'true'},
		    {field:'db_name',title:'DB명',halign:"center",align:'center'},
		    {field:'expect_work_exec_day',hidden:'true'},
		    {field:'expect_work_exec_time',hidden:'true'},
		    {field:'expect_work_dt',title:'작업예정일시',halign:"center",align:"center",sortable:"true"},
		    {field:'top_rank_measure_type_cd',hidden:'true'},
		    {field:'top_rank_measure_type_nm',title:'TOP순위측정유형',halign:"center",align:'center',sortable:"true"},
			{field:'topn_cnt',title:'TOP N',halign:"center",align:'right',formatter:getNumberFormat},
			{field:'begin_snap_id',title:'시작스냅샷번호',halign:"center",align:'center',sortable:"true"},
			{field:'end_snap_id',title:'종료스냅샷번호',halign:"center",align:'center',sortable:"true"},
			{field:'work_start_dt',title:'작업시작일시',halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'work_end_dt',title:'작업종료일시',halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'plan_change_cnt',title:'실행계획변경건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'perf_regress_cnt',title:'성능저하건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
//        onCheck : function(node, checked) {
//            var selectedrow = $("#tableList").datagrid("getSelected");
//            var rowIndex = $("#tableList").datagrid("getRowIndex", selectedrow);
//        	
//        	row = $('#tableList').datagrid('getChecked');
//        	var rowLength = row.length;
//
//        		if(row.length > 1){
//        			parent.$.messager.alert('','다중 선택은 불가합니다.');
//        			//$("input:checkbox[name='chk']").eq(node).attr("checked",false);
//        			return false;
//        		}
//        }
	});

	$("#selectDbidCombo").combobox("setValue",$("#submit_form #dbid").val());
	$("#tableList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable").remove();
	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallTOPSQLDiagnostics();
	}	
	
});

function ajaxCallTOPSQLDiagnostics(){
	
	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DB변경 SQL영향도 진단"," "); 
	
	ajaxCall("/TOPSQLDiagnosticsAction",
		$("#submit_form"),
		"POST",
		callback_TOPSQLDiagnosticsActionAddTable);
}

//callback 함수
var callback_TOPSQLDiagnosticsActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function Btn_OnClick(){
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #dbid").val($('#selectDbidCombo').combobox('getValue'));
	
//	$("#submit_form").attr("action","/TOPSQLDiagnostics");
//	$("#submit_form").submit();
	ajaxCallTOPSQLDiagnostics();
	
}
/*
function showSchedulePopup(){
	
	parent.resetTopSqlValue();
	
	var dbid = $("#selectDbidCombo").combobox('getValue');
	parent.$("#topSql_form #selectDbidCombo").combobox('setValue',dbid);
	parent.$("#topSql_form #dbid").val(dbid);
	
	row = $('#tableList').datagrid('getChecked');
	var rowLength = row.length;
	
	parent.frameName = $("#menu_id").val();	

	if(row.length > 0){
		if(row.length > 1){
			parent.$.messager.alert('','하나만 선택해 주세요.');
			return false;
		}else{
			parent.$("#topSql_form #before_perf_expect_no").val(row[0].before_perf_expect_no);
			
			// DB변경 SQL영향도 진단 조회
			ajaxCall("/TOPSQLDiagnostics/Popup/GetInfo",
					parent.$("#topSql_form"),
					"POST",
					callback_GetTOPSQLDiagnosticsAction);
		}
	}
	
	parent.$('#topSqlSchedulePop').window("open");
	if(rowLength > 0){
		parent.$('#topSqlSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 변경");		
		parent.$('#topSqlSchedulePop #crud_flag').val("U");				
	}else{
		parent.$('#topSqlSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 등록");		
		parent.$('#topSqlSchedulePop #before_perf_expect_no').val("");				
		parent.$('#topSqlSchedulePop #crud_flag').val("C");				
	}
}
*/
function showSchedulePopup(){
	
	resetTopSqlValue();
	
	var dbid = $("#selectDbidCombo").combobox('getValue');
	$("#topSql_form #selectDbidCombo").combobox('setValue',dbid);
	$("#topSql_form #dbid").val(dbid);
	
	row = $('#tableList').datagrid('getChecked');
	var rowLength = row.length;
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	if(row.length > 0){
		if(row.length > 1){
			parent.$.messager.alert('','하나만 선택해 주세요.');
			return false;
		}else{
			$("#topSql_form #before_perf_expect_no").val(row[0].before_perf_expect_no);
			
			// DB변경 SQL영향도 진단 조회
			ajaxCall("/TOPSQLDiagnostics/Popup/GetInfo",
					$("#topSql_form"),
					"POST",
					callback_GetTOPSQLDiagnosticsAction);
		}
	}
	
	if(rowLength > 0){
		
		$('#topSqlSchedulePop').window({
			title : "DB변경 SQL영향도 진단 > 스케쥴 변경",
			top:getWindowTop(270),
			left:getWindowLeft(700)
		});
				
	} else {
		
		$('#topSqlSchedulePop').window({
			title : "DB변경 SQL영향도 진단 > 스케쥴 등록",
			top:270,
			left:getWindowLeft(700)
		});
		
	}
	
	$('#topSqlSchedulePop').window("open");
	if(rowLength > 0){
//		$('#topSqlSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 변경");		
		$('#topSqlSchedulePop #crud_flag').val("U");				
	}else{
//		$('#topSqlSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 등록");
		$('#topSqlSchedulePop #before_perf_expect_no').val("");				
		$('#topSqlSchedulePop #crud_flag').val("C");				
	}
}

//callback 함수
//var callback_GetTOPSQLDiagnosticsAction = function(result) {
//	if(result.result){
//		var post = result.object;
//		
//		parent.$("#topSql_form #dbid").val(post.dbid);
//		parent.$("#topSql_form #selectDbidCombo").combobox("setValue",post.dbid);
//		parent.$("#topSql_form #begin_snap_id").textbox("setValue",post.begin_snap_id);
//		parent.$("#topSql_form #end_snap_id").textbox("setValue",post.end_snap_id);
//		parent.$("#topSql_form #immediately_yn").val(post.immediately_yn);
//		
//		if(post.immediately_yn == "Y"){
//			parent.$('#topSql_form #chkImmediately').switchbutton({checked: true});
//			parent.$("#topSql_form #expect_work_exec_day").datebox({disabled:true});
//			parent.$("#topSql_form #expect_work_exec_time").timespinner({disabled:true});
//		}else{
//			parent.$('#topSql_form #chkImmediately').switchbutton({checked: false});
//			parent.$("#topSql_form #expect_work_exec_day").datebox({disabled:false});
//			parent.$("#topSql_form #expect_work_exec_time").timespinner({disabled:false});			
//		}
//		
//		parent.$("#topSql_form #expect_work_exec_day").datebox("setValue",post.expect_work_exec_day);
//		parent.$("#topSql_form #expect_work_exec_time").timespinner("setValue",post.expect_work_exec_time);
//		
//		parent.$("#topSql_form #top_rank_measure_type_cd").combobox("setValue",post.top_rank_measure_type_cd);
//		parent.$("#topSql_form #topn_cnt").textbox("setValue",post.topn_cnt);
//
//		if(post.edit_yn == "N"){
//			parent.$("#removeSqlBtn").linkbutton({disabled:true});
//			parent.$("#saveSqlBtn").linkbutton({disabled:true});
//			parent.$("#snapBtn").linkbutton({disabled:true});			
//		}else{
//			parent.$("#removeSqlBtn").linkbutton({disabled:false});
//		}
//	}else{
//		parent.$.messager.alert('',result.message);
//	}
//};
var callback_GetTOPSQLDiagnosticsAction = function(result) {
	if(result.result){
		var post = result.object;
		
		$("#topSql_form #dbid").val(post.dbid);
		$("#topSql_form #selectDbidCombo").combobox("setValue",post.dbid);
		$("#topSql_form #begin_snap_id").textbox("setValue",post.begin_snap_id);
		$("#topSql_form #end_snap_id").textbox("setValue",post.end_snap_id);
		$("#topSql_form #immediately_yn").val(post.immediately_yn);
		
		if(post.immediately_yn == "Y"){
			$('#topSql_form #chkImmediately').switchbutton({checked: true});
			$("#topSql_form #expect_work_exec_day").datebox({disabled:true});
			$("#topSql_form #expect_work_exec_time").timespinner({disabled:true});
		}else{
			$('#topSql_form #chkImmediately').switchbutton({checked: false});
			$("#topSql_form #expect_work_exec_day").datebox({disabled:false});
			$("#topSql_form #expect_work_exec_time").timespinner({disabled:false});			
		}
		
		$("#topSql_form #expect_work_exec_day").datebox("setValue",post.expect_work_exec_day);
		$("#topSql_form #expect_work_exec_time").timespinner("setValue",post.expect_work_exec_time);
		
		$("#topSql_form #top_rank_measure_type_cd").combobox("setValue",post.top_rank_measure_type_cd);
		$("#topSql_form #topn_cnt").textbox("setValue",post.topn_cnt);

		if(post.edit_yn == "N"){
			$("#removeSqlBtn").linkbutton({disabled:true});
			$("#saveSqlBtn").linkbutton({disabled:true});
			$("#snapBtn").linkbutton({disabled:true});			
		}else{
			$("#removeSqlBtn").linkbutton({disabled:false});
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function goTOPSQLDiagnosticsCheck(){
	$("#submit_form").attr("action","/TOPSQLDiagnostics/Detail");
//	$("#submit_form #menu_nm").val("DB변경 SQL영향도 진단 상세내역");
	$("#submit_form #menu_nm").val("DB변경 SQL 성능 영향도 진단 상세내역");
	$("#submit_form").submit();
}