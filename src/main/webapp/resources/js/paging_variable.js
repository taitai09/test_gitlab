var master_form_selector;

$(document).ready(function(){
	//이전, 다음 처리
	btnClickEvent();
});

function setMarster_form_selector(master_form) {
	master_form_selector = master_form;
}

function btnClickEvent() {
	if(master_form_selector == null || master_form_selector == undefined) {
		return;
	}
	
	$(master_form_selector + ' #prevBtn').click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext('PREV');
		}
	});
	$(master_form_selector + ' #nextBtn').click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext('NEXT');
		}
	});	
}

function fnGoPrevOrNext(direction){
	if(master_form_selector == null || master_form_selector == undefined) {
		return;
	}
	
	if(direction == 'PREV'){
		if($(master_form_selector + ' #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == 'NEXT'){
		if($(master_form_selector + ' #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $(master_form_selector + ' #currentPage').val();
	if(currentPage != null && currentPage != ''){
		var iCurrentPage = parseInt(currentPage);
		if(direction == 'PREV'){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == 'NEXT'){
			iCurrentPage++;
		}
		
		$(master_form_selector + ' #currentPage').val(iCurrentPage);
	}else{
		$(master_form_selector + ' #currentPage').val('1');
	}
	fnSearch(master_form_selector);
}

function fnEnableDisablePagingBtn(dataLength){
	//페이징 처리
	var currentPage = $(master_form_selector + ' #currentPage').val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $(master_form_selector + ' #pagePerCount').val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$(master_form_selector + ' #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$(master_form_selector + ' #nextBtn').linkbutton('enable');
		}else{
			$(master_form_selector + ' #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$(master_form_selector + ' #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$(master_form_selector + ' #nextBtn').linkbutton('enable');
		}else{
			$(master_form_selector + ' #nextBtn').linkbutton('disable');
		}
	}
}