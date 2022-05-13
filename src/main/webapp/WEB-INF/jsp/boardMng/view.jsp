<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.11.14	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: 게시판 관리 :: 게시물 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/boardMng/view.js?ver=<%=today%>"></script>    
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${boardManagement.board_nm} 상세</span>

			</div>					
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
				<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${board.board_mgmt_no}"/>
				<input type="hidden" id="board_no" name="board_no" value="${board.board_no}"/>
				<input type="hidden" id="ref_board_no" name="ref_board_no" value="${result.ref_board_no}"/>
				<input type="hidden" id="top_board_no" name="top_board_no" value="${result.top_board_no}"/>
				<input type="hidden" id="depth" name="depth" value="${result.depth}"/>
				<input type="hidden" id="lvl" name="lvl" value="${result.lvl}"/>
				<input type="hidden" id="file_nm" name="file_nm"/>
				<input type="hidden" id="org_file_nm" name="org_file_nm"/>
				<input type="hidden" id="comment_seq" name="comment_seq"/>						
				<input type="hidden" id="comment_contents" name="comment_contents"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>
						<th>제목</th>
						<td colspan="3">${result.title}</td>
					</tr>
					<tr>
						<th>등록일자</th>
						<td>${result.reg_dt}</td>
						<th>등록자</th>
						<td>${result.reg_nm}</td>
					</tr>
					<tr style="height:350px;">
						<th>내용</th>
						<td colspan="3" style="padding-top:5px;padding-bottom:5px;vertical-align:top;">${result.board_contents}</td>
					</tr>
					<c:if test="${boardManagement.file_add_yn eq 'Y' and (result.file_nm ne null and result.file_nm ne '')}">
						<tr>
							<th>첨부파일</th>
							<td colspan="3">${result.org_file_nm} <a href="javascript:;" onClick="Btn_downFile('${result.file_nm}','${result.org_file_nm}');"><img src="/resources/images/file_icon/${result.file_ext_nm}.gif" style="vertical-align:middle;"/></a></td>
						</tr>
					</c:if>	
				</table>
			</form:form>
		</div>
		<div class="dtlBtn" style="margin-bottom:10px;">
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ReplyBoard();"><i class="btnIcon fas fa-reply fa-lg fa-fw"></i> 답글</a>
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_UpdateBoard();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>						
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteBoard();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
		</div>
		<c:if test="${boardManagement.comment_use_yn eq 'Y'}">
			<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
				<table id="commentTbl" class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:80%;">
					</colgroup>
					<tbody>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>