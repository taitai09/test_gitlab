var strNoGb = "";
var timeGb = "";

$(document).ready(function() {
	$('#baseLinePop').window({
		title : "베이스라인 선택",
		top:getWindowTop(450),
		left:getWindowLeft(680)
	});	
	
	$('#timePop').window({
		title : "시간 선택",
		top:getWindowTop(150),
		left:getWindowLeft(380)
	});

	$("#baseLineList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setBaseLine(row.start_snap_id, row.end_snap_id);
		},	
		columns:[[
			{field:'baseline_id',title:'BASELINE_ID',width:"19%",halign:"center",align:"center",sortable:"true"},
			{field:'baseline_name',title:'BASELINE_NAME',width:"28%",halign:"center",align:'left'},
			{field:'baseline_type',title:'BASELINE_TYPE',width:"20%",halign:"center",align:'center'},
			{field:'start_snap_id',title:'START_SNAP_ID',width:"20%",halign:"center",align:'center'},
			{field:'start_snap_time',title:'START_SNAP_TIME',width:"21%",halign:"center",align:'center'},
			{field:'end_snap_id',title:'END_SNAP_ID',width:"20%",halign:"center",align:'center'},
			{field:'end_snap_time',title:'END_SNAP_TIME',width:"21%",halign:"center",align:'center'},
			{field:'creation_time',title:'CREATION_TIME',width:"21%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}			
	});	
	
	$('#strTime').timespinner({
		showSeconds: true
	});
});

function Btn_SetDateTime(){
	var strDate = $("#strDate").datebox('getValue');
	var strTime = $("#strTime").timespinner('getValue');
	
	if(timeGb == "start"){
		//eval("if_"+frameName).$("#start_dateTime").textbox('setValue',strDate + " " + strTime);
		$("#start_dateTime").textbox('setValue',strDate + " " + strTime);
	}else{
		//eval("if_"+frameName).$("#end_dateTime").textbox('setValue',strDate + " " + strTime);	
		$("#end_dateTime").textbox('setValue',strDate + " " + strTime);	
	}

	Btn_OnClosePopup("timePop");
}

function setBaseLine(strStartId, strEndId){
//	eval("if_"+frameName).$("#start_snap_id"+strNoGb).textbox('setValue',strStartId);
//	eval("if_"+frameName).$("#end_snap_id"+strNoGb).textbox('setValue',strEndId);	
	$("#start_snap_id"+strNoGb).textbox('setValue',strStartId);
	$("#end_snap_id"+strNoGb).textbox('setValue',strEndId);	

	Btn_OnClosePopup("baseLinePop");
}