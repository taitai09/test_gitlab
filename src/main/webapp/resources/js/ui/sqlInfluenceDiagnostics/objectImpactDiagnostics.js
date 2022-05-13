$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#selectDbidCombo').combobox('textbox').attr("placeholder","선택");
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickCell : function(index, field, value) {
			if(field == 'chk') {
				return;
			}
			
			var data = $(this).datagrid('getRows')[index];
			
			$("#submit_form #sql_perf_impl_anal_no").val(data.sql_perf_impl_anal_no);
			$("#submit_form #db_name").val(data.db_name);
			$("#submit_form #dbid").val($("#selectDbidCombo").combobox("getValue"));
			$("#submit_form #call_from_parent").val("Y");
			goObjectDiagnosticsTable();
		},
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : true,
		rownumbers:false,
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
		    {field:'sql_perf_impl_anal_no',title:'번호',halign:"center",align:'center',sortable:"true",sorter:sorterInt},
		    {field:'dbid',hidden:'true'},
		    {field:'db_name',title:'DB명',halign:"center",align:'center',sortable:"true"},
		    {field:'table_cnt',title:'테이블수',halign:"center",align:"right",sortable:"true"},
		    {field:'anal_work_exec_day',hidden:'true'},
		    {field:'anal_work_exec_time',hidden:'true'},		    
		    {field:'anal_work_exec_dt',title:'작업예정일시',halign:"center",align:"center",sortable:"true"},
			{field:'begin_snap_time',title:'시작스냅샷시간',halign:"center",align:'center',sortable:"true"},
			{field:'end_snap_time',title:'종료스냅샷시간',halign:"center",align:'center',sortable:"true"},
			{field:'work_start_dt',title:'작업시작일시',halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'work_end_dt',title:'작업종료일시',halign:"center",align:'center',sortable:"true",sorter:sorterDatetime}
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
		ajaxCallObjectImpactDiagnostics();
	}
	
});

function ajaxCallObjectImpactDiagnostics(){
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("오브젝트 진단"," "); 
	
	ajaxCall("/ObjectImpactDiagnosticsAction",
			$("#submit_form"),
			"POST",
			callback_ObjectImpactDiagnosticsActionAddTable);
	
}

//callback 함수
var callback_ObjectImpactDiagnosticsActionAddTable = function(result) {
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
	
//	$("#submit_form").attr("action","/ObjectImpactDiagnostics");
//	$("#submit_form").submit();
	ajaxCallObjectImpactDiagnostics();
	
}
/*
function showSchedulePopup(){

	parent.resetObjectValue();

	var dbid = $("#selectDbidCombo").combobox('getValue');
	parent.$("#object_form #selectDbidCombo").combobox('setValue',dbid);
	parent.$("#object_form #dbid").val(dbid);

    var selectedrow = $("#tableList").datagrid("getSelected");
    var rowIndex = $("#tableList").datagrid("getRowIndex", selectedrow);
	
	row = $('#tableList').datagrid('getChecked');
	var rowLength = row.length;

	parent.frameName = $("#menu_id").val();

	if(row.length > 0){
		if(row.length > 1){
			parent.$.messager.alert('','하나만 선택해 주세요.');
			return false;
		}else{
			parent.$("#object_form #sql_perf_impl_anal_no").val(row[0].sql_perf_impl_anal_no);
			
			// 기존 정보 조회
			ajaxCall("/ObjectImpactDiagnostics/Popup/GetInfo",
					parent.$("#object_form"),
					"POST",
					callback_GetObjectDiagnosticsAction);
			
			// 분석대상 테이블 조회
			parent.$('#targetList').datagrid('loadData',[]);
			parent.$('#targetList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
			parent.$('#targetList').datagrid('loading');
			
			ajaxCall("/ObjectImpactDiagnostics/Popup/GetTargetTable",
					parent.$("#object_form"),
					"POST",
					callback_GetTargetTableAdd);
		}
	}

	parent.$('#objectSchedulePop').window("open");
	
	if(rowLength > 0){
		parent.$('#objectSchedulePop').prev().select(".panel-title").text("오브젝트 진단 > 스케쥴 변경");
		parent.$('#objectSchedulePop #crud_flag').val("U");				
	}else{
		parent.$('#objectSchedulePop').prev().select(".panel-title").text("오브젝트 진단 > 스케쥴 등록");		
		parent.$('#objectSchedulePop #sql_perf_impl_anal_no').val("");				
		parent.$('#objectSchedulePop #crud_flag').val("C");				
	}
}
*/
function showSchedulePopup(){

	resetObjectValue();

	var dbid = $("#selectDbidCombo").combobox('getValue');
	$("#object_form #selectDbidCombo").combobox('setValue',dbid);
	$("#object_form #dbid").val(dbid);

    var selectedrow = $("#tableList").datagrid("getSelected");
    var rowIndex = $("#tableList").datagrid("getRowIndex", selectedrow);
	
	row = $('#tableList').datagrid('getChecked');
	var rowLength = row.length;

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	if(row.length > 0){
		if(row.length > 1){
			parent.$.messager.alert('','하나만 선택해 주세요.');
			return false;
		}else{
			$("#object_form #sql_perf_impl_anal_no").val(row[0].sql_perf_impl_anal_no);
			
			// 기존 정보 조회
			ajaxCall("/ObjectImpactDiagnostics/Popup/GetInfo",
					$("#object_form"),
					"POST",
					callback_GetObjectDiagnosticsAction);
			
			// 분석대상 테이블 조회
			$('#targetList').datagrid('loadData',[]);
			$('#targetList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
			$('#targetList').datagrid('loading');
			
			ajaxCall("/ObjectImpactDiagnostics/Popup/GetTargetTable",
					$("#object_form"),
					"POST",
					callback_GetTargetTableAdd);
		}
	}

	let topPoint = 100;
	
	if(rowLength > 0){
		
		$('#objectSchedulePop').window({
			title : "DB변경 SQL영향도 진단 > 스케쥴 변경",
			top:topPoint,
			left:getWindowLeft(700)
		});
		
	} else {
		
		$('#objectSchedulePop').window({
			title : "DB변경 SQL영향도 진단 > 스케쥴 등록",
			top:topPoint,
			left:getWindowLeft(700)
		});
		
	}
	
	$('#objectSchedulePop').window("open");
	
	if(rowLength > 0){
//		$('#objectSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 변경");		
		$('#objectSchedulePop #crud_flag').val("U");				
	}else{
//		$('#objectSchedulePop').prev().select(".panel-title").text("DB변경 SQL영향도 진단 > 스케쥴 등록");
		$('#objectSchedulePop #sql_perf_impl_anal_no').val("");				
		$('#objectSchedulePop #crud_flag').val("C");				
	}
	
}

//callback 함수
//var callback_GetObjectDiagnosticsAction = function(result) {
//	if(result.result){
//		var post = result.object;
//		parent.$("#object_form #begin_snap_id").textbox("setValue",post.begin_snap_id);
//		parent.$("#object_form #end_snap_id").textbox("setValue",post.end_snap_id);
//		parent.$("#object_form #immediately_yn").val(post.immediately_yn);
//		
//		if(post.immediately_yn == "Y"){
//			parent.$('#object_form #chkImmediately').switchbutton({checked: true});
//			parent.$("#object_form #anal_work_exec_day").datebox({disabled:true});
//			parent.$("#object_form #anal_work_exec_time").timespinner({disabled:true});
//		}else{
//			parent.$('#object_form #chkImmediately').switchbutton({checked: false});
//			parent.$("#object_form #anal_work_exec_day").datebox({disabled:false});
//			parent.$("#object_form #anal_work_exec_time").timespinner({disabled:false});			
//		}
//		parent.$("#object_form #anal_work_exec_day").datebox("setValue",post.anal_work_exec_day);
//		parent.$("#object_form #anal_work_exec_time").timespinner("setValue",post.anal_work_exec_time);
//
//		if(post.edit_yn == "N"){
//			parent.$("#removeObjectBtn").linkbutton({disabled:true});
//			parent.$("#saveObjectBtn").linkbutton({disabled:true});
//			parent.$("#snapOBtn").linkbutton({disabled:true});
//			parent.$("#addBtn").linkbutton({disabled:true});
//			parent.$("#removeBtn").linkbutton({disabled:true});
//		}else{
//			parent.$("#removeObjectBtn").linkbutton({disabled:false});
//		}
//	}else{
//		parent.$.messager.alert('',result.message);
//	}
//};
var callback_GetObjectDiagnosticsAction = function(result) {
	if(result.result){
		var post = result.object;
		$("#object_form #begin_snap_id").textbox("setValue",post.begin_snap_id);
		$("#object_form #end_snap_id").textbox("setValue",post.end_snap_id);
		$("#object_form #immediately_yn").val(post.immediately_yn);
		
		if(post.immediately_yn == "Y"){
			$('#object_form #chkImmediately').switchbutton({checked: true});
			$("#object_form #anal_work_exec_day").datebox({disabled:true});
			$("#object_form #anal_work_exec_time").timespinner({disabled:true});
		}else{
			$('#object_form #chkImmediately').switchbutton({checked: false});
			$("#object_form #anal_work_exec_day").datebox({disabled:false});
			$("#object_form #anal_work_exec_time").timespinner({disabled:false});			
		}
		$("#object_form #anal_work_exec_day").datebox("setValue",post.anal_work_exec_day);
		$("#object_form #anal_work_exec_time").timespinner("setValue",post.anal_work_exec_time);

		if(post.edit_yn == "N"){
			$("#removeObjectBtn").linkbutton({disabled:true});
			$("#saveObjectBtn").linkbutton({disabled:true});
			$("#snapOBtn").linkbutton({disabled:true});
			$("#addBtn").linkbutton({disabled:true});
			$("#removeBtn").linkbutton({disabled:true});
		}else{
			$("#removeObjectBtn").linkbutton({disabled:false});
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
};
//callback 함수
var callback_GetTargetTableAdd = function(result) {
	json_string_callback_common(result,'#targetList',false);
};

function goObjectDiagnosticsTable(){
	$("#submit_form").attr("action","/ObjectImpactDiagnostics/Table");
//	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단 테이블 내역");
	$("#submit_form #menu_nm").val("오브젝트변경 SQL 성능 영향도 진단 테이블 내역");
	$("#submit_form").submit();
}