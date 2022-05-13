$(document).ready(function(){
	//이전, 다음 처리
	$("#projectList_form #prevBtn").click(function(){
		if(formValidationCheckPopup()){
			fnGoPrevOrNextPopup("PREV");
		}
	});
	$("#projectList_form #nextBtn").click(function(){
		if(formValidationCheckPopup()){
			fnGoPrevOrNextPopup("NEXT");
		}
	});
});

function fnGoPrevOrNextPopup(direction){
	
	if(direction == "PREV"){
		if($('#projectList_form #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#projectList_form #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#projectList_form #currentPage").val();
	if(currentPage != null && currentPage != ""){
		var iCurrentPage = parseInt(currentPage);
		if(direction == "PREV"){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			iCurrentPage++;
		}
		
		$("#projectList_form #currentPage").val(iCurrentPage);
	}else{
		$("#projectList_form #currentPage").val("1");
	}
	fnSearchPopup();
}

function fnEnableDisablePagingBtnPopup(dataLength){
	//페이징 처리
	var currentPage = $("#projectList_form #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#projectList_form #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
//	console.log("iCurrentPage :"+iCurrentPage);
//	console.log("iPagePerCount :"+iPagePerCount);
	
	if(iCurrentPage > 1){
		$('#projectList_form #prevBtn').linkbutton('enable');

		if(dataLength > 10){
			$('#projectList_form #nextBtn').linkbutton('enable');
		}else{
			$('#projectList_form #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#projectList_form #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#projectList_form #nextBtn').linkbutton('enable');
		}else{
			$('#projectList_form #nextBtn').linkbutton('disable');
		}
	}
}