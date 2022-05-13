$(document).ready(function() {
	Set_CommentList();
	
	if ( $("#defaultText").val() != null && $("#defaultText").val() != '' ) {
		parent.$.messager.alert('', $("#defaultText").val() ,'error');
	}
});

function Set_CommentList(){
	ajaxCall("/Board/CommentList",
			$("#submit_form"),
			"POST",
			callback_CommentListAction);
}

//callback 함수
var callback_CommentListAction = function(result) {
	var strHtml = "";
	$("#boardDetail").attr("style","width:100%; height:400px;")
	$("#boardBottom").attr("style","width:100%;height:200px; padding-bottom:10px;overflow:auto; border-bottom: 1px solid black; border-top: 1px solid lightgray;");
	$("#commentTbl tbody tr").remove();
	$("#commentInsert div").remove();
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			strHtml += "<tr>";
			strHtml += "<th>" + post.reg_nm + "(" + post.reg_id + ")</th>";
			strHtml += "<td style='padding-top:5px;padding-bottom:5px;'>" + strReplace(post.comment_contents,'\n','<br/>');
			strHtml += "<div style='float:right;margin-right:10px;'>";
			strHtml += "<a href='javascript:;' onClick='Btn_DelteComment("+post.comment_seq+",\""+post.reg_id+"\");'><img src='/resources/images/board/btn_comt_del.png' style='vertical-align:middle;'/></a>";
			strHtml += "</div></td></tr>";
		}
	}else {
		$("#boardDetail").attr("style","width:100%; height:550px;")
		$("#boardBottom").attr("style","width:100%;height:-1px; padding-bottom:10px; border-bottom: 1px solid black;");
	}
	$("#commentTbl tbody").append(strHtml);
	
	strHtml = "<div colspan='2' style='padding-top:5px;padding-bottom:5px;'>";
	strHtml += "<textarea name='comm_contents' id='comm_contents' rows='4' style='width:95%; height:50px;'></textarea>";
	strHtml += "<div style='float:right;margin-right:10px;'>";
	strHtml += "<a href='javascript:;' onClick='Btn_SaveComment();'><img src='/resources/images/board/btn_comt_add.png' style='vertical-align:middle;'/></a>";
	strHtml += "</div></div>";
	
	$("#commentInsert").append(strHtml);
};

function Btn_ReplyBoard(){
	$("#submit_form").attr("action","/Board/Reply");
	$("#submit_form").submit();	
}

function Btn_UpdateBoard(){
	$("#submit_form").attr("action","/Board/Update");
	$("#submit_form").submit();	
}

function Btn_DeleteBoard(){	
//	parent.$.messager.confirm('','답글이 있는 게시글의 답변도 모두 삭제됩니다.<br/>삭제하시겠습니까?',function(r){
	parent.$.messager.confirm('','첨부파일과 댓글 모두 삭제됩니다.<br/>삭제하시겠습니까?',function(r){
		if (r){
			ajaxCall("/Board/Delete",
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
	if($("#comm_contents").val().length  > 2000){
		parent.$.messager.alert('','댓글 내용을 2000자 이내로 입력해 주세요.');
		return false;
	}
	let str = $("#comm_contents").val();
	
	str = str.replace(/<(\/a|a[^>]*)>/gi,"");
	str = str.replace(/<(\/p|p[^>]*)>/gi,"");
	str = str.replace(/<(\/span|span[^>]*)>/gi,"");
	str = str.replace(/<(\/script|script[^>]*)>/gi,"");
	str = str.replace(/&lt;/gi,"");
	str = str.replace(/&gt;/gi,"");
	
	$("#comment_contents").val( str );
	
	ajaxCall("/Board/SaveComment",
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

function deleteFile(obj,file_seq,file_nm, auth_cd, reg_user_id, user_id){
	
	console.log(obj);
	console.log(file_seq);
	console.log(file_nm);
	console.log(auth_cd);
	console.log(reg_user_id);
	console.log(user_id);
	
	if(auth_cd == 'ROLE_ITMANAGER' || auth_cd == 'ROLE_TUNER' || auth_cd == 'ROLE_DEV' || auth_cd == 'ROLE_DEV_DEPLOYMANAGER' || auth_cd == 'ROLE_DEPLOYMANAGER'){
		parent.$.messager.alert('','권한이 없습니다.','error');
		return false;
	}
	
	$("#file_form #file_nm").val(file_nm);
	$("#file_form #file_seq").val(file_seq);
	$.ajax({
		type: "POST",
		url: "/BoardMng/Notice/deleteAttachFile",
		data: $("#file_form").serialize(),
		success: function(result) {
			if(!result.result){
				parent.$.messager.alert('',result.message,'error');
			}else{
				parent.$.messager.alert('',result.message,'info');
				$(obj).parent().remove();
			}
		}
	});
}

function Btn_DelteComment(commentSeq,reg_id){
	user_id = $("#submit_form #user_id").val()
	auth_cd = $("#submit_form #auth_cd").val()
	
	if(auth_cd != 'ROLE_DBMANAGER' && auth_cd != 'ROLE_OPENPOPMANAGER' && reg_id != user_id){
		parent.$.messager.alert('','권한이 없습니다.');
		return false;
	}
	
	$("#comment_seq").val(commentSeq);
	
	ajaxCall("/Board/DeleteComment",
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
	$("#submit_form").attr("action","/Board/List");
	$("#submit_form").submit();
}

function Btn_downFile(fileNm, orgFileNm){
	$("#file_nm").val(fileNm);
	$("#org_file_nm").val(orgFileNm);
	$("#submit_form").attr("action","/Board/DownFile");
	$("#submit_form").submit();
}
