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
	<title>게시판 :: 리스트</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/board/list.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${boardManagement.board_nm}</span>

			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
					<input type="hidden" id="board_mgmt_no" name="board_mgmt_no" value="${board.board_mgmt_no}"/>
					<input type="hidden" id="board_no" name="board_no"/>
					<input type="hidden" id="file_nm" name="file_nm"/>
					<input type="hidden" id="org_file_nm" name="org_file_nm"/>
					<label>검색</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
						<option value="">선택</option>
						<option value="01" <c:if test="${board.searchKey eq '01'}">selected</c:if>>제목</option>
						<option value="02" <c:if test="${board.searchKey eq '02'}">selected</c:if>>내용</option>
						<option value="03" <c:if test="${board.searchKey eq '03'}">selected</c:if>>제목+내용</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" value="${board.searchValue}" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>
				</form:form>								
			</div>
		</div>
		<c:set var="colCnt" scope="page" value="5"/>
		<c:if test="${boardManagement.comment_use_yn eq 'Y'}">
			<c:set var="colCnt" scope="page" value="${colCnt+1}"/>
		</c:if>								
		<c:if test="${boardManagement.file_add_yn eq 'Y'}">
			<c:set var="colCnt" scope="page" value="${colCnt+1}"/>
		</c:if>	
		<div class="easyui-panel" data-options="border:false" style="min-height:400px;margin-bottom:10px;">
			<c:if test="${boardManagement.board_type_cd ne '1'}">
				<div class="searchBtn innerBtn">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_InsertBoard();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> ${boardManagement.board_nm} 등록</a>
				</div>
			</c:if>
			<table id="tableList" class="detailT">
				<colgroup>	
					<col style="width:8%;">
					<c:choose>
						<c:when test="${boardManagement.comment_use_yn eq 'Y'}">
							<col style="width:40%;">
							<col style="width:10%;">
						</c:when>
						<c:otherwise>
							<col style="width:52%;">
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${boardManagement.file_add_yn eq 'Y'}">									
							<col style="width:9%;">
							<col style="width:9%;">
							<col style="width:12%;">
							<col style="width:10%;">									
						</c:when>
						<c:otherwise>
							<col style="width:14%;">
							<col style="width:14%;">
							<col style="width:14%;">
						</c:otherwise>
					</c:choose>
				</colgroup>					
				<thead>
					<tr>
						<th>NO</th>
						<th>제목</th>
						<c:if test="${boardManagement.comment_use_yn eq 'Y'}">
							<th>댓글수</th>
						</c:if>								
						<th>조회수</th>
						<c:if test="${boardManagement.file_add_yn eq 'Y'}">
							<th>첨부파일</th>
						</c:if>	
						<th>등록일시</th>
						<th>등록자</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${resultList.size() < 1}">
						<tr>
							<td colspan="${colCnt}" class="ctext">검색된 게시물이 존재하지 않습니다.</td>
						</tr>
					</c:if>
					<c:forEach items="${resultList}" var="result" varStatus="status">
					<c:choose>
						<c:when test="${result.rnum eq 0}">
							<tr style="background-color:#EEEEEE;">
						</c:when>
						<c:otherwise>
							<tr>
						</c:otherwise>
					</c:choose>								
							<td class="ctext">
								<c:choose>
									<c:when test="${result.rnum eq 0}"><b>[공지]</b></c:when>
									<c:otherwise>${result.rnum}</c:otherwise>
								</c:choose>
							</td>
							<td style="cursor:pointer;padding-left:10px;" onClick="Btn_ViewBoard('${result.board_no}');">
								<c:if test="${result.depth ne '0'}">
									<c:forEach begin="2" end="${result.depth}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>
									<img src="/resources/images/board/icon_re.gif" style="vertical-align:middle;"/>&nbsp;	
								</c:if>
								<c:choose>
									<c:when test="${fn:length(result.title) > 30}">
										${fn:substring(result.title,0,30)} ...
									</c:when>
									<c:otherwise><c:out value="${result.title}"></c:out></c:otherwise>
								</c:choose>
							</td>
							<c:if test="${boardManagement.comment_use_yn eq 'Y'}">
								<td class="ctext">${result.comment_cnt}</td>
							</c:if>
							<td class="ctext">${result.hit_cnt}</td>
							<c:if test="${boardManagement.file_add_yn eq 'Y'}">
								<td class="ctext">
									<c:if test="${result.file_ext_nm ne '' and result.file_ext_nm ne null}">
										<a href="javascript:;" onClick="Btn_downFile('${result.file_nm}','${result.org_file_nm}');"><img src="/resources/images/file_icon/${result.file_ext_nm}.gif" style="vertical-align:middle;"/></a>
									</c:if>
								</td>
							</c:if>
							<td class="ctext">${result.reg_dt}</td>
							<td class="ctext">${result.reg_nm}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!--// 페이징 START -->
				${board.pageUtil.pageHtml}
			<!--//페이징 END -->
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>