$(document).ready(function() {
	$('#manualSelectionHistoryPop').window({
		title : "수동선정이력",
		top:getWindowTop(450),
		left:getWindowLeft(700)
	});	

	$("#manualHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'choice_tms',title:'순번',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'choice_dt',title:'저장일시',width:"20%",halign:"center",align:'center',sortable:"true"},
			{field:'start_snap_id',title:'시작 SNAP_ID',width:"14%",halign:"center",align:'center',sortable:"true"},
			{field:'begin_interval_time',title:'시작 SNAP TIME',width:"21%",halign:"center",align:'center',sortable:"true"},
			{field:'end_snap_id',title:'종료 SNAP_ID',width:"14%",halign:"center",align:'center',sortable:"true"},
			{field:'end_interval_time',title:'종료 SNAP TIME',width:"21%",halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 			
	});	
});