$(document).ready(function() {
	Set_CommentList();
});

function Set_CommentList(){
	ajaxCall("/BoardMng/CommentList",
			$("#submit_form"),
			"POST",
			callback_CommentListAction);	
}

//callback 함수
var callback_CommentListAction = function(result) {
	var strHtml = "";
	$("#commentTbl tbody tr").remove();
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			strHtml += "<tr>";
			strHtml += "<th>" + post.reg_nm + "(" + post.reg_id + ")</th>";
			strHtml += "<td style='padding-top:5px;padding-bottom:5px;'>" + strReplace(post.comment_contents,'\n','<br/>');
			strHtml += "<div style='float:right;margin-right:10px;'>";
			strHtml += "<a href='javascript:;' onClick='Btn_DelteComment(\""+post.comment_seq+"\");'><img src='/resources/images/btn_comt_del.png' style='vertical-align:middle;'/></a>";
			strHtml += "</div></td></tr>";
		}
	}

	strHtml += "<tr>";
	strHtml += "<td colspan='2' style='padding-top:5px;padding-bottom:5px;'>";
	strHtml += "<textarea name='comm_contents' id='comm_contents' rows='4' style='width:95%; height:80px;'></textarea>";
	strHtml += "<div style='float:right;margin-right:10px;'>";
	strHtml += "<a href='javascript:;' onClick='Btn_SaveComment();'><img src='/resources/images/btn_comt_add.png' style='vertical-align:middle;'/></a>";
	strHtml += "</div></td></tr>";
	
	$("#commentTbl tbody").append(strHtml);	
};

function Btn_ReplyBoard(){
	$("#submit_form").attr("action","/BoardMng/Reply");
	$("#submit_form").submit();	
}

function Btn_UpdateBoard(){
	$("#submit_form").attr("action","/BoardMng/Update");
	$("#submit_form").submit();	
}

function Btn_DeleteBoard(){	
	parent.$.messager.confirm('','답글이 있는 게시글의 답변도 모두 삭제됩니다.<br/>삭제하시겠습니까?',function(r){
		if (r){
			ajaxCall("/BoardMng/Delete",
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

function Btn_SaveComment(){
	if($("#comm_contents").val() == ""){
		parent.$.messager.alert('','댓글 내용을 입력해 주세요.');
		return false;
	}

	$("#comment_contents").val($("#comm_contents").val());
	
	ajaxCall("/BoardMng/SaveComment",
			$("#submit_form"),
			"POST",
			callback_SaveCommentAction);		
}

//callback 함수
var callback_SaveCommentAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 댓글이 등록 되었습니다.','info',function(){
			setTimeout(function() {
				Set_CommentList();
			},1000);			
		});
	}
};

function Btn_DelteComment(commentSeq){
	$("#comment_seq").val(commentSeq);
	
	ajaxCall("/BoardMng/DeleteComment",
			$("#submit_form"),
			"POST",
			callback_DeleteCommentAction);		
}

//callback 함수
var callback_DeleteCommentAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 댓글이 삭제 되었습니다.','info',function(){
			setTimeout(function() {
				Set_CommentList();
			},1000);			
		});
	}
};

function Btn_goList(){
	$("#submit_form").attr("action","/BoardMng/List");
	$("#submit_form").submit();	
}

function Btn_downFile(fileNm, orgFileNm){
	$("#file_nm").val(fileNm);
	$("#org_file_nm").val(orgFileNm);
	$("#submit_form").attr("action","/BoardMng/downFile");
	$("#submit_form").submit();
}