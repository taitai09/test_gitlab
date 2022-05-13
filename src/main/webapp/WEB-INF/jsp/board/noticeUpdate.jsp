<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2017.10.20	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/board/noticeUpdate.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3">공지사항</span>

			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${noticeContents.board_mgmt_no}"/>
				<input type="hidden" id="board_no" name="board_no" value="${noticeContents.board_no}"/>
				<input type="hidden" id="strStartDt" name="strStartDt" value="${noticeContents.strStartDt}"/>
				<input type="hidden" id="strEndDt" name="strEndDt" value="${noticeContents.strEndDt}"/>
<%-- 				<input type="hidden" id="file_nm" name="file_nm" value="${noticeContentsFile.file_nm}"/> --%>
<%-- 				<input type="hidden" id="org_file_nm" name="org_file_nm" value="${noticeContentsFile.org_file_nm}"/> --%>
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
				<input type="hidden" id="use_seq" name="use_seq" value="${hit_cnt}"/>
				<input type="hidden" id="auth_cd" name="auth_cd" value="${auth_cd}"/>
				<input type="hidden" id="top_notice_yn" name="top_notice_yn" value="${noticeContents.top_notice_yn}"/>

						<table class="detailT">
							<colgroup>
								<col style="width:15%;">
								<col style="width:85%;">
							</colgroup>
							<tr>
								<th>제목</th>
								<td>
									<input type="text" id="title" name="title" value="${noticeContents.title}" class="easyui-textbox" style="width:1000px"/>
									<span style="padding-left:10px;padding-right:5px;">상단공지</span>
									<input type="checkbox" id="chkTopFixYn" name="chkTopFixYn" class="w120 easyui-switchbutton"/>
								</td>
							</tr>
							<tr height="130px;">
								<th>내용</th>
								<td><textarea name="board_contents" id="board_contents" rows="20" style="width:97%;padding:15px;IME-MODE:active;">${noticeContents.contents}</textarea></td>
							</tr>
							<tr>
								<th>첨부파일</th>
								<td>
									<c:if test="${(auth_cd ne 'ROLE_DEV') and (auth_cd ne 'ROLE_ITMANAGER')}">
										<input id="uploadFile" multiple="multiple" name="uploadFile" class="easyui-filebox" data-options="prompt:'파일을 선택해 주세요.'" style="width:50%"/>
									</c:if>
									<c:forEach items="${noticeFiles}" var="noticeFiles" varStatus="status">
										<c:if test="${noticeFiles.file_seq ne ''}">
											<span>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downGuideFile ('${noticeFiles.file_nm}','${noticeFiles.org_file_nm}');">${noticeFiles.org_file_nm}</a>
												<c:choose>
													<c:when test="${auth_cd eq 'ROLE_DBMANAGER'}">
														<a href="javascript:;" onClick="deleteFile (this,'${noticeFiles.file_seq}','${noticeFiles.file_nm}','${auth_cd}','${reg_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
													</c:when>
													<c:when test="${reg_id eq user_id}">
														<a href="javascript:;" onClick="deleteFile (this,'${noticeFiles.file_seq}','${noticeFiles.file_nm}','${auth_cd}','${reg_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
													</c:when>
												</c:choose>
											</span>
										</c:if>
									</c:forEach>
								</td>
							</tr>
						</table>
						
				<div class="dtlBtn">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
					
					
					<!-- 임시 -->
					<sec:authorize access="hasAnyRole('ROLE_DBMANAGER')">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnUpdate();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnDelete();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
					</sec:authorize>
					
					<sec:authorize access="hasAnyRole('ROLE_OPENPOPMANAGER')">
						<c:if test="${user_id eq reg_id}">
							<c:if test="${noticeContents.reg_id eq user_id}">
								<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnUpdate();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>
							</c:if>
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnDelete();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
						</c:if>
					</sec:authorize>
				</div>
			</form:form>

<!-- 			<iframe id="downloadFrame" style="display:none"></iframe> -->

			<form:form method="post" id="file_form" name="file_form" target="downloadFrame">
				<input type="hidden" id="board_no" name="board_no" value="${noticeContents.board_no}"/>
				<input type="hidden" id="file_seq" name="file_seq" value=""/>
<%-- 				<input type="hidden" id="file_seq" name="file_seq" value="${file_seq}"/> --%>
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
			</form:form>

		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>