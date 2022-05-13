$(document).ready(function() {
});

function Btn_OnClick() {
	if(($("#searchKey").val() != "" && $("#searchValue").val() == "") ||
		($("#searchKey").val() == "" && $("#searchValue").val() != "")){
			parent.$.messager.alert('','검색 조건을 제대로 선택해 주세요');
			return false;
	}

	$("#submit_form").attr("action","/Board/List");
	$("#submit_form").submit();
}

function Btn_InsertBoard(){
	$("#submit_form").attr("action","/Board/Insert");
	$("#submit_form").submit();	
}

function Btn_ViewBoard(boardNo){
	$("#board_no").val(boardNo);
	$("#submit_form").attr("action","/Board/View");
	$("#submit_form").submit();
}

function goLinkPage(currentPage){
	$("#currentPage").val(currentPage);
	$("#submit_form").attr("action","/Board/List");
	$("#submit_form").submit();	
}

function Btn_downFile(fileNm, orgFileNm){
	$("#file_nm").val(fileNm);
	$("#org_file_nm").val(orgFileNm);
	$("#submit_form").attr("action","/Board/DownFile");
	$("#submit_form").submit();
}