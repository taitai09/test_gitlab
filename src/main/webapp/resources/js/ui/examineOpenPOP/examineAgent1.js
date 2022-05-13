$(document).ready(function() {
	$("body").css("visibility", "visible");
	$("#agentList").datagrid({
		view: myview,
		columns:[[
			{field:'agent',title:'Agent',halign:"center",width:"70%",align:"left"},
			{field:'status',title:'Status',halign:"center",width:"30%",align:'center'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onSelect: function(index, row) {
			$(this).datagrid('unselectRow', index);
		}
	});
	
	$("#instanceList").datagrid({
		view: myview,
		columns:[[
			{field:'db_name',title:'DB',halign:"center",width:"40%",align:"left"},
			{field:'dbid',title:'DBID',hidden:true},
			{field:'instance',title:'Instance',halign:"center",width:"15%",align:'center'},
			{field:'instance_status',title:'Instance Status',halign:"center",width:"15%",align:'center'},
			{field:'collect_status',title:'Collect Status',halign:"center",width:"15%",align:'center'},
			{field:'gather_status',title:'Gather Status',halign:"center",width:"15%",align:'center'},
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onSelect: function(index, row) {
			$(this).datagrid('unselectRow', index);
		}
	});
});

function Btn_Reflash() {
	rtrvAgentStatus();
}

function rtrvAgentStatus() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Agent 상태 수집 중"," "); 
	
	ajaxCall("/ExamineOpenPOP/rtrvAgentStatus",
		$("#submit_form"),
		"POST",
		callback_rtrvAgentStatus);
}

var callback_rtrvAgentStatus = function(result) {
	$('#agentList').datagrid("loadData", []);
	
	var baseDay = '점검 시간 : ';
	
	try {
		data = JSON.parse(result);
		var rows = data.rows;
		var dataLength = rows.length;
		
		if(dataLength > 0) {
			baseDay += rows[dataLength - 1].BASE_DAY;
			
			rows.splice(dataLength - 1, 1);
			
			$('#agentList').datagrid('loadData', rows);
			$('#agentList').datagrid('loaded');
		}
	} catch(err) {
		//errorFlow(err, result);
	} finally {
		parent.$.messager.progress('close');
		
		$('#managementTime').empty();
		$('#managementTime').append(baseDay);
		
		instanceList();
	}
}

function getStatusImg(val) {
	if(val == 'true') {		// 정상
		return "<img src='/resources/images/examine_scheduler/normal_status.png' style='vertical-align:bottom;'/>";	
	} else if(val == 'false') {	// 오류
		return "<img src='/resources/images/examine_scheduler/error_status.png' style='vertical-align:bottom;'/>";
	} else {
		return val;
	}
}

function instanceList() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Instance 상태 수집 중"," "); 
	
	ajaxCall("/ExamineOpenPOP/instanceList",
		$("#submit_form"),
		"POST",
		callback_instanceList);
}

var callback_instanceList = function(result) {
	$('#instanceList').datagrid("loadData", []);
	
	try {
		var data = JSON.parse(result);
		var rows = data.rows;
		
//		json_string_callback_common(result,'#instanceList',true);
		
		rows = changeValue(rows);
		
		$('#instanceList').datagrid('loadData', rows);
		$('#instanceList').datagrid('loaded');
	} catch(err) {
		console.error(err);
	} finally {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

function changeValue(pureData) {
	for(var index = 0; index < pureData.length; index++) {
		var row = pureData[index];
		
		for(var key in row) {
			row[key] = getStatusImg(row[key]);
		}
		
		pureData[index] = row;
	}
	
	return pureData
}
