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
		ajaxCallSQLAccessAdvisor();	
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
	
	ajaxCallSQLAccessAdvisor();	
}

function ajaxCallSQLAccessAdvisor(){
	var win = parent.$.messager.progress({
		title:'Please waiting',
		msg:'',
		text:'SQL Access Advisor를 실행중입니다.',
		interval:100
	});
	
	ajaxCall("/SQLPerformance/SQLAccessAdvisor",
			$("#submit_form"),
			"POST",
			callback_SQLAccessAdvisorAction);	
}

//callback 함수
var callback_SQLAccessAdvisorAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#dataArea").html("");
		
		for(var i = 0 ; i < result.stringList.length ; i++){
			var value = result.stringList[i];
			strHtml += strReplace(strReplace(value,"\n","<br/>")," ","&nbsp;") + "<br/>";
		}
		$("#dataArea").append(strHtml);
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};