<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.02.23	이원식	최초작성
 * 2021.03.30	이재우	성능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항  :: 게시물 등록</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/board/insert.js?ver=<%=today%>"></script>
	<style type="text/css">
		#cke_board_contents #cke_1_bottom {
			display: none;
		}
	</style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>
					<c:choose>
						<c:when test="${board.board_mgmt_no eq '2'}">
							게시판 등록
						</c:when>
						<c:otherwise>
							${boardManagement.board_nm} 등록
						</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline" enctype="multipart/form-data">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
				<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${board.board_mgmt_no}"/>
				<input type="hidden" id="top_notice_yn" name="top_notice_yn" value="N"/>
				<input type="hidden" id="file_add_yn" name="file_add_yn" value="${boardManagement.file_add_yn}"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:80%;">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="title" name="title" class="easyui-textbox" style="width:500px;"/>
							<c:choose>
								<c:when test="${board.board_mgmt_no eq '2'}">
									&nbsp;&nbsp;&nbsp;<label>상단게시여부</label><input type="checkbox" id="chkTopNotice" name="chkTopNotice" value="" class="w120 easyui-switchbutton"/>
								</c:when>
								<c:otherwise>
									&nbsp;&nbsp;&nbsp;<label>상위공지여부</label><input type="checkbox" id="chkTopNotice" name="chkTopNotice" value="" class="w120 easyui-switchbutton"/>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td style="padding-top:5px;padding-bottom:5px;"><textarea id="board_contents" name="board_contents" rows="20" style="width:97%; padding:15px;IME-MODE:active;"></textarea></td>
					</tr>
					<c:choose>
						<c:when test="${boardManagement.file_add_yn eq 'Y'}">
							<tr>
								<th>첨부파일</th>
								<td><input id="uploadFile" name="uploadFile" multiple="multiple" class="easyui-filebox" data-options="prompt:'파일을 선택해 주세요.'" style="width:50%"/></td>
							</tr>
						</c:when>
						<c:otherwise> 
						<!-- notice insert
							1. Login as "opmanager1"
							2. Only when the value is registered in "Settings"-> "Manage Bulletin Settings" can the bulletin board be registered.
						 -->
<!-- 							<input type="file" id="uploadFile" multiple="multiple" name="uploadFile" style="display:none;"/> -->
						</c:otherwise>
					</c:choose>
				</table>
			</form:form>
		</div>
		<div class="dtlBtn" style="margin-bottom:10px;">
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 등록</a>
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>