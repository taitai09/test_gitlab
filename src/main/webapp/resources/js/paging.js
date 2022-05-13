$(document).ready(function(){
	//이전, 다음 처리
	$("#submit_form #prevBtn").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#submit_form #nextBtn").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});	
});

function fnGoPrevOrNext(direction){
	
	if(direction == "PREV"){
		if($('#submit_form #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#submit_form #currentPage").val();
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
		
		$("#submit_form #currentPage").val(iCurrentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
	fnSearch();
}

function fnEnableDisablePagingBtn(dataLength){
	
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$('#submit_form #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#submit_form #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form #nextBtn').linkbutton('disable');
		}
	}
}