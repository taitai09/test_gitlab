<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일정 관리  :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/schedule/view.js?ver=<%=today%>"></script>    
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="strGubun" name="strGubun" value="${schedule.strGubun}"/>
				<input type="hidden" id="strStartDt" name="strStartDt" value="${schedule.strStartDt}"/>
				<input type="hidden" id="strEndDt" name="strEndDt" value="${schedule.strEndDt}"/>
				<input type="hidden" id="searchKey" name="searchKey" value="${schedule.searchKey}"/>
				<input type="hidden" id="searchValue" name="searchValue" value="${schedule.searchValue}"/>
				<input type="hidden" id="user_id" name="user_id" value="${schedule.user_id}"/>
				<input type="hidden" id="sched_seq" name="sched_seq" value="${schedule.sched_seq}"/>						
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>
						<th>일정 유형</th>
						<td colspan="3">${result.sched_type_nm}</td>
					</tr>
					<tr>
						<th>제목</th>
						<td colspan="3">${result.sched_title}</td>
					</tr>
					<tr>
						<th>일정</th>
						<td colspan="3">${result.sched_start_dt} ~ ${result.sched_end_dt}</td>
					</tr>							
					<tr style="height:250px;">
						<th>내용</th>
						<td colspan="3" style="padding-top:5px;padding-bottom:5px;vertical-align:top;">${result.sched_sbst}</td>
					</tr>
					<tr>
						<th>등록일자</th>
						<td>${result.reg_dt}</td>
						<th>등록자</th>
						<td>${result.user_nm}</td>
					</tr>
					
				</table>
			</form:form>
		</div>
		<div class="dtlBtn" style="margin-bottom:10px;">
			<c:if test="${result.user_id eq loginId}">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_UpdateSchedule();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>												
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSchedule();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
			</c:if>
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>