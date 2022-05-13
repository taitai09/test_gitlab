$(document).ready(function() {
});

function Btn_UpdateSchedule(){
	$("#submit_form").attr("action","/Schedule/Update");
	$("#submit_form").submit();	
}

function Btn_DeleteSchedule(){	
	parent.$.messager.confirm('','일정을 삭제하시겠습니까?',function(r){
		if (r){
			ajaxCall("/Schedule/Delete",
					$("#submit_form"),
					"POST",
					callback_delteAction);			
		}
	});
}

//callback 함수
var callback_delteAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 삭제되었습니다.','info',function(){
			setTimeout(function() {
				Btn_goList();
			},1000);			
		});
	}
};	

function Btn_goList(){
	$("#submit_form").attr("action","/Schedule/List");
	$("#submit_form").submit();	
}