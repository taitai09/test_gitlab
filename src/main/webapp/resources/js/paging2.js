$(document).ready(function(){
	//이전, 다음 처리
	$("#submit_form #prevBtn2").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext2("PREV");
		}
	});
	$("#submit_form #nextBtn2").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext2("NEXT");
		}
	});	
});

function fnGoPrevOrNext2(direction){
	
	if(direction == "PREV"){
		if($('#submit_form #prevBtn2').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form #nextBtn2').linkbutton('options').disabled) return;
	}
	
	var currentPage2 = $("#submit_form #currentPage2").val();
	if(currentPage2 != null && currentPage2 != ""){
		var icurrentPage2 = parseInt(currentPage2);
		if(direction == "PREV"){
			if(icurrentPage2 > 1){
				icurrentPage2--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			icurrentPage2++;
		}
		
		$("#submit_form #currentPage2").val(icurrentPage2);
	}else{
		$("#submit_form #currentPage2").val("1");
	}
	getModulePerformanceTable();
//	fnSearch();
}

function fnEnableDisablePagingBtn2(dataLength){
	//페이징 처리
	var currentPage2 = $("#submit_form #currentPage2").val();
	var icurrentPage2 = parseInt(currentPage2);
	var pagePerCount = $("#submit_form #rcount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(icurrentPage2 > 1){
		$('#submit_form #prevBtn2').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form #nextBtn2').linkbutton('enable');
		}else{
			$('#submit_form #nextBtn2').linkbutton('disable');
		}
	}
	if(icurrentPage2 == 1){
		$('#submit_form #prevBtn2').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form #nextBtn2').linkbutton('enable');
		}else{
			$('#submit_form #nextBtn2').linkbutton('disable');
		}
	}
}