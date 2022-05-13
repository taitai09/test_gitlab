<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.02.23	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항 :: 게시물 답변</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/board/reply.js?ver=<%=today%>"></script>    
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${boardManagement.board_nm} 답글</span>

			</div>					
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline" enctype="multipart/form-data">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
				<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${board.board_mgmt_no}"/>
				<input type="hidden" id="board_no" name="board_no" value="${result.board_no}"/>
				<input type="hidden" id="top_notice_yn" name="top_notice_yn" value="N"/>
				<input type="hidden" id="file_add_yn" name="file_add_yn" value="${boardManagement.file_add_yn}"/>
				<input type="hidden" id="ref_board_no" name="ref_board_no" value="${result.ref_board_no}"/>
				<input type="hidden" id="top_board_no" name="top_board_no" value="${result.board_no}"/>
				<input type="hidden" id="depth" name="depth" value="${result.depth}"/>
				<input type="hidden" id="lvl" name="lvl" value="${result.lvl}"/>
				<input type="hidden" id="file_nm" name="file_nm" value="${result.file_nm}"/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value="${result.org_file_nm}"/>
				<input type="hidden" id="file_size" name="file_size" value="${result.file_size}"/>
				<input type="hidden" id="file_ext_nm" name="file_ext_nm" value="${result.file_ext_nm}"/>

				<table class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:80%;">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="title" name="title" value="[답글].${result.title}" class="easyui-textbox" style="width:500px;"/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td style="padding-top:5px;padding-bottom:5px;"><textarea id="board_contents" name="board_contents" rows="20" style="width:97%; padding:15px;IME-MODE:active;">${result.board_contents}</textarea></td>
					</tr>
					<c:choose>
						<c:when test="${boardManagement.file_add_yn eq 'Y'}">
							<tr>
								<th>첨부파일</th>
								<td><input id="uploadFile" name="uploadFile" multiple="multiple" class="easyui-filebox" data-options="prompt:'파일을 선택해 주세요.'" style="width:50%"/></td>
							</tr>
						</c:when>
						<c:otherwise>
							<input type="file" id="uploadFile" name="uploadFile" style="display:none;"/>
						</c:otherwise>	
					</c:choose>	
				</table>
			</form:form>
		</div>
		<div class="dtlBtn" style="margin-bottom:10px;">
			<a href="javascript:;" class="w130 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 답글 등록</a>						
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>