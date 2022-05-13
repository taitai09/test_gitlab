$(document).ready(function() {
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
});

function Btn_OnClick() {
	if(($("#searchKey").val() != "" && $("#searchValue").val() == "") ||
		($("#searchKey").val() == "" && $("#searchValue").val() != "")){
			parent.$.messager.alert('','검색 조건을 제대로 선택해 주세요');
			return false;
	}

	$("#submit_form").attr("action","/BoardMng/List");
	$("#submit_form").submit();
}

function Btn_InsertBoard(){
	$("#submit_form").attr("action","/BoardMng/Insert");
	$("#submit_form").submit();	
}

function Btn_ViewBoard(boardNo){
	$("#board_no").val(boardNo);
	$("#submit_form").attr("action","/BoardMng/View");
	$("#submit_form").submit();
}

function goLinkPage(currentPage){
	$("#currentPage").val(currentPage);
	$("#submit_form").attr("action","/BoardMng/List");
	$("#submit_form").submit();	
}

function Btn_downFile(fileNm, orgFileNm){
	$("#file_nm").val(fileNm);
	$("#org_file_nm").val(orgFileNm);
	$("#submit_form").attr("action","/BoardMng/downFile");
	$("#submit_form").submit();
}