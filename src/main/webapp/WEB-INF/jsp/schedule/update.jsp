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
	<title>일정 관리 :: 수정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/schedule/update.js?ver=<%=today%>"></script>    
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
				<input type="hidden" id="sched_type_cd" name="sched_type_cd" value="${result.sched_type_cd}"/>
				<input type="hidden" id="sched_start_dt" name="sched_start_dt"/>
				<input type="hidden" id="sched_end_dt" name="sched_end_dt"/>	
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:80%;">
					</colgroup>
					<tr>
						<th>일정 유형</th>
						<td>
							<select id="selSchedTypeCd" name="selSchedTypeCd" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
								<option value="1" <c:if test="${result.sched_type_cd eq '1'}">selected</c:if>>개인</option>
								<option value="2" <c:if test="${result.sched_type_cd eq '2'}">selected</c:if>>전체</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="sched_title" name="sched_title" class="easyui-textbox" value="${result.sched_title}" style="width:98%;"/>
						</td>
					</tr>
					<tr>
						<th>일정</th>
						<td>
							<input type="text" id="sched_start_date" name="sched_start_date" value="${result.sched_start_date}" class="w130 datapicker easyui-datebox"/>
							<input type="text" id="sched_start_time" name="sched_start_time" value="${result.sched_start_time}" class="w100 datatime easyui-timespinner"/> ~
							~
							<input type="text" id="sched_end_date" name="sched_end_date" value="${result.sched_end_date}" class="w130 datapicker easyui-datebox"/>
							<input type="text" id="sched_end_time" name="sched_end_time" value="${result.sched_end_time}" class="w100 datatime easyui-timespinner"/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td style="padding-top:5px;padding-bottom:5px;"><textarea id="sched_sbst" name="sched_sbst" rows="20" style="width:97%; padding:15px;IME-MODE:active;">${result.sched_sbst}</textarea></td>
					</tr>
				</table>
			</form:form>
		</div>
		<div class="dtlBtn" style="margin-bottom:10px;">
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>						
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>