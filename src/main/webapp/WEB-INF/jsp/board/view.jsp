<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.02.23	이원식	최초작성
 * 2021.03.30	이재우	성능개선(DBA게시판 같이사용)
 * 2021.12.14	이재우	성능개선(댓글수정)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항  :: 게시물 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />    
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/board/view.js?ver=<%=today%>"></script>
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
						<c:when test="${board.board_mgmt_no eq 2}">
							게시판 상세
						</c:when>
						<c:otherwise>
							${boardManagement.board_nm} 상세
						</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:5px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
				<input type="hidden" id="auth_cd" name="auth_cd" value="${auth_cd}"/>
				<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
				<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${board.board_mgmt_no}"/>
				<input type="hidden" id="board_no" name="board_no" value="${board.board_no}"/>
				<input type="hidden" id="ref_board_no" name="ref_board_no" value="${result.ref_board_no}"/>
				<input type="hidden" id="top_board_no" name="top_board_no" value="${result.top_board_no}"/>
				<input type="hidden" id="depth" name="depth" value="${result.depth}"/>
				<input type="hidden" id="lvl" name="lvl" value="${result.lvl}"/>
				<input type="hidden" id="searchKey2" name="searchKey2" value="view"/>
				<input type="hidden" id="defaultText" name="defaultText" value="${defaultText}"/>
				<input type="hidden" id="file_nm" name="file_nm"/>
				<input type="hidden" id="org_file_nm" name="org_file_nm"/>
				<input type="hidden" id="comment_seq" name="comment_seq"/>
				<input type="hidden" id="comment_contents" name="comment_contents"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<div id="boardDetail"style="width:100%; height:550px;">
					<table class="detailT">
						<colgroup>
							<col style="width:20%;">
							<col style="width:30%;">
							<col style="width:20%;">
							<col style="width:30%;">
						</colgroup>
						<tr>
							<th>제목</th>
							<td colspan="3"><c:out value="${result.title}"></c:out></td>
						</tr>
						<tr>
							<th>등록일자</th>
							<td>${result.reg_dt}</td>
							<th>등록자</th>
							<td>${result.upd_nm}</td>
						</tr>
						<tr style="height:250px;">
							<th>내용</th>
							<td colspan="3" style="padding-top:5px;padding-bottom:5px;vertical-align:top;"><div style="height:250px;overflow:auto;">${result.board_contents}</div></td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td colspan="3">
								<c:forEach items="${attachFile}" var="attachFile" varStatus="status" >
								<span>
								<c:if test="${boardManagement.file_add_yn eq 'Y' and (attachFile.file_nm ne null and attachFile.file_nm ne '')}">
									<a href="javascript:;" onClick="Btn_downFile('${attachFile.file_nm}','${attachFile.org_file_nm}');">${attachFile.org_file_nm}</a>
									<c:if test="${!status.last}">&nbsp;,&nbsp;</c:if>
								</c:if>
									<%-- <c:choose>
										<c:when test="${auth_cd eq 'ROLE_DBMANAGER'}">
											<a href="javascript:;" onClick="deleteFile (this,'${attachFile.file_seq}','${attachFile.file_nm}','${auth_cd}','${upd_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
										</c:when>
										<c:when test="${result.upd_id eq user_id}">
											<a href="javascript:;" onClick="deleteFile (this,'${attachFile.file_seq}','${attachFile.file_nm}','${auth_cd}','${upd_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
										</c:when>
									</c:choose> --%>
								</span>
								</c:forEach>
							</td>
						</tr>
					</table>
				</div>
			</form:form>
			
			<form:form method="post" id="file_form" name="file_form" target="downloadFrame">
				<input type="hidden" id="board_no" name="board_no" value="${board.board_no}"/>
				<!-- <input type="hidden" id="file_seq" name="file_seq" value=""/> -->
 				<input type="hidden" id="file_seq" name="file_seq" value="${file_seq}"/> 
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
			</form:form>
			
		</div>
		<div style="width:100%; height:500px; overflow:auto;">
			<div class="dtlBtn" style="margin-bottom:10px;">
				<c:if test="${boardManagement.board_type_cd ne '1' and boardManagement.board_mgmt_no ne '2'}">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ReplyBoard();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 답글</a>
				</c:if>
				<c:if test="${(auth_cd eq 'ROLE_DBMANAGER') or (result.upd_id eq user_id)}">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_UpdateBoard();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteBoard();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
				</c:if>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
			</div>
			<c:if test="${boardManagement.comment_use_yn eq 'Y'}">
				<div id="boardBottom" class="easyui-panel" data-options="border:false" style="width:100%;height:-1px; padding-bottom:10px; border-bottom: 1px solid black;">
					<table id="commentTbl" class="detailT" style="">
						<colgroup>
							<col style="width:20%;">
							<col style="width:80%;">
						</colgroup>
						<tbody>
						</tbody>
					</table>
				</div>
				<div id="commentInsert">
				</div>
			</c:if>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>