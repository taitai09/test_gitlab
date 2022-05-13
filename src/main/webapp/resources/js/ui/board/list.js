$(document).ready(function() {
	
	$('#searchKey').combobox({
		onLoadSuccess : function() {
			if ( $('#searchValue').textbox("getValue") == "" ) {
				$('#searchValue').textbox({readonly:true});
			} else {
				$('#searchValue').textbox({readonly:false});
			}
		},
		onChange : function(newValue,oldValue) {
			if(newValue == "") {
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else{
				$('#searchValue').textbox({readonly:false});
			}
		}
	});
	
	if ( $("#defaultText").val() != null && $("#defaultText").val() != '' ) {
		parent.$.messager.alert('', $("#defaultText").val() ,'error');
	}
	
});

function Btn_OnClick() {
	
	if(($("#searchKey").val() != "" && $("#searchValue").val() == "") ||
		($("#searchKey").val() == "" && $("#searchValue").val() != "")){
			parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
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

function Btn_downFile(fileNm, orgFileNm, board_no){
	$("#board_no").val(board_no);
	$("#file_nm").val(fileNm);
	$("#org_file_nm").val(orgFileNm);
	
	$("#submit_form").attr("action","/Board/DownFile");
	$("#submit_form").submit();
}

function Excel_Download(){
	
	$("#submit_form").attr("action","/Board/Notice/ExcelDown");
	$("#submit_form").submit();
}