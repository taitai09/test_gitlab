$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',    
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$('#selectCombo').combobox("setValue",$("#dbid").val());
	
	var call_from_parent = $("#call_from_parent").val();
	if(callFromParent == "Y"){
		ajaxCallSQLMonitor();
	}
	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL_ID를 입력해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	ajaxCallSQLMonitor();
}

function ajaxCallSQLMonitor(){
	var win = parent.$.messager.progress({
		title:'Please waiting',
		msg:'SQL Monitor',
		text:'생성중입니다.',
		interval:100
	});		
	ajaxCall("/SQLPerformance/SQLMonitor",
			$("#submit_form"),
			"POST",
			callback_SQLMonitorAction);		
}

//callback 함수
var callback_SQLMonitorAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#dataArea").html("");

		for(var i = 0 ; i < result.stringList.length ; i++){
			var value = result.stringList[i];
			strHtml += value;
		}
		
		$("#dataArea").append(strHtml);
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};