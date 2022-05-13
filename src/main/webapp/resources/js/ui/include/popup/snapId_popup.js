$(document).ready(function() {
	$('#snapListPop').window({
		title : "SNAP ID 조회",
		top:getWindowTop(870),
		left:getWindowLeft(950)
	});
	
	$("#startSnapIdList").datagrid({
		view: myview,
		nowrap: true,
		fit: true,
		fitColumns: true,
		onClickRow : function(index,row) {
			$("#snapList_form #snap_id").val(row.snap_id);
			$("#snapList_form #start_snap_id").val(row.snap_id);
			$("#snapList_form #instance_number").val(row.instance_number);
			searchEndSnapIdList();
		},
		columns:[[
			{field:'snap_id',title:'SNAP_ID',halign:"center",align:"center",sortable:"true"},
			{field:'instance_number',title:'INSTANCE<br/>NUMBER',halign:"center",align:'center',sortable:"true"},
			{field:'begin_interval_time',title:'BEGIN_INTERVAL_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'end_interval_time',title:'END_INTERVAL_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'startup_time',title:'STARTUP_TIME',halign:"center",align:'center',sortable:"true"}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#endSnapIdList").datagrid({
		view: myview,
		nowrap: true,
		fit: true,
		fitColumns: true,
		onClickRow : function(index,row) {
			$("#snapList_form #end_snap_id").val(row.snap_id);
			setSnapId();
		},
		columns:[[
			{field:'snap_id',title:'SNAP_ID',halign:"center",align:"center",sortable:"true"},
			{field:'instance_number',title:'INSTANCE<br/>NUMBER',halign:"center",align:'center',sortable:"true"},
			{field:'begin_interval_time',title:'BEGIN_INTERVAL_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'end_interval_time',title:'END_INTERVAL_TIME',halign:"center",align:'center',sortable:"true"},
			{field:'startup_time',title:'STARTUP_TIME',halign:"center",align:'center',sortable:"true"}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});	
	
	/* modal progress close */
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function searchStartSnapIdList(){
	$('#startSnapIdList').datagrid('loadData',[]);
	var linkUrl = "";
	
	$("#snapList_form #snap_id").val("");
	$("#snapList_form #start_snap_id").val("");
	$("#snapList_form #instance_number").val("");
	$('#endSnapIdList').datagrid('loadData',[]);
	
	if(strGb == "report1" || strGb == "report2"){
		linkUrl = "/AWRAdvancedAnalysis/Popup/SnapShotList";
	}else{
		linkUrl = "/Common/SnapIdList";
	}
	
	ajaxCall(linkUrl,
		$("#snapList_form"),
		"POST",
		callback_StartSnapIdListAddTable);
	
	/* modal progress open */
//	openMessageProgress('SNAP_ID조회','START_SANP_ID를 조회중입니다.');
	parent.openMessageProgress('SNAP_ID조회','START_SANP_ID를 조회중입니다.');
}

//callback 함수
var callback_StartSnapIdListAddTable = function(result) {
	json_string_callback_common(result, "#startSnapIdList",true);
}

function searchEndSnapIdList(){
	$('#endSnapIdList').datagrid('loadData',[]);
	var linkUrl = "";
	
	if(strGb == "report1" || strGb == "report2"){
		linkUrl = "/AWRAdvancedAnalysis/Popup/SnapShotList";
	}else{
		linkUrl = "/Common/SnapIdList";
	}
	
	ajaxCall(linkUrl,
		$("#snapList_form"),
		"POST",
		callback_EndSnapIdListAddTable);
	
	/* modal progress open */
//	openMessageProgress('SNAP_ID조회','END_SANP_ID를 조회중입니다.');
	parent.openMessageProgress('SNAP_ID조회','END_SANP_ID를 조회중입니다.');
}

//callback 함수
var callback_EndSnapIdListAddTable = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		parent.$.messager.alert('',data.message);
	}else{
		$('#endSnapIdList').datagrid("loadData", data);
	}	

	/* modal progress close */
//	closeMessageProgress();
	parent.closeMessageProgress();
};

function setSnapId(){
	console.log("strGb:"+strGb);
	if(strGb == "manual"){
//		eval("if_"+frameName).$("#submit_form #start_snap_id").textbox('setValue',$("#snapList_form #start_snap_id").val());
//		eval("if_"+frameName).$("#submit_form #end_snap_id").textbox('setValue',$("#snapList_form #end_snap_id").val());
		$("#submit_form #start_snap_id").textbox('setValue',$("#snapList_form #start_snap_id").val());
		$("#submit_form #end_snap_id").textbox('setValue',$("#snapList_form #end_snap_id").val());
	}else if(strGb == "report1"){
//		eval("if_"+frameName).$("#submit_form #start_snap_id1").textbox('setValue',$("#snapList_form #start_snap_id").val());
//		eval("if_"+frameName).$("#submit_form #end_snap_id1").textbox('setValue',$("#snapList_form #end_snap_id").val());
		$("#submit_form #start_snap_id1").textbox('setValue',$("#snapList_form #start_snap_id").val());
		$("#submit_form #end_snap_id1").textbox('setValue',$("#snapList_form #end_snap_id").val());
	}else if(strGb == "report2"){
//		eval("if_"+frameName).$("#submit_form #start_snap_id2").textbox('setValue',$("#snapList_form #start_snap_id").val());
//		eval("if_"+frameName).$("#submit_form #end_snap_id2").textbox('setValue',$("#snapList_form #end_snap_id").val());
		$("#submit_form #start_snap_id2").textbox('setValue',$("#snapList_form #start_snap_id").val());
		$("#submit_form #end_snap_id2").textbox('setValue',$("#snapList_form #end_snap_id").val());
	}else if(strGb == "sql"){
		$("#topSql_form #begin_snap_id").textbox('setValue',$("#snapList_form #start_snap_id").val());
		$("#topSql_form #end_snap_id").textbox('setValue',$("#snapList_form #end_snap_id").val());
	}else if(strGb == "object"){
		$("#object_form #begin_snap_id").textbox('setValue',$("#snapList_form #start_snap_id").val());
		$("#object_form #end_snap_id").textbox('setValue',$("#snapList_form #end_snap_id").val());
	}
	
	Btn_OnClosePopup("snapListPop");
}