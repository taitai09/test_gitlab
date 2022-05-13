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
	<title>일정  관리 :: 리스트</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/schedule/list.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="user_id" name="user_id"/>
					<input type="hidden" id="sched_seq" name="sched_seq"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>

					<label>일자</label>
					<select id="strGubun" name="strGubun" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
						<option value="01" <c:if test="${schedule.strGubun eq '01' or (schedule.strGubun eq '' or schedule.strGubun eq null)}">selected</c:if>>일정시작일자</option>
						<option value="02" <c:if test="${schedule.strGubun eq '02'}">selected</c:if>>일정종료일자</option>
						<option value="03" <c:if test="${schedule.strGubun eq '03'}">selected</c:if>>등록일자</option>
					</select>
					<input type="text" id="strStartDt" name="strStartDt" value="${schedule.strStartDt}" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${schedule.strEndDt}" class="w130 datapicker easyui-datebox"/>
					<label>검색</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
						<option value="">선택</option>
						<option value="01" <c:if test="${schedule.searchKey eq '01'}">selected</c:if>>제목</option>
						<option value="02" <c:if test="${schedule.searchKey eq '02'}">selected</c:if>>내용</option>
						<option value="03" <c:if test="${schedule.searchKey eq '03'}">selected</c:if>>제목+내용</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" value="${schedule.searchValue}" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>							
				</form:form>								
			</div>
		</div>
		<div class="searchBtn innerBtn">
			<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_InsertSchedule();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 일정 등록</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:580px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>				
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>