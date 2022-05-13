<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.09	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>오브젝트변경 SQL영향도 진단</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlInfluenceDiagnostics/objectImpactDiagnostics.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlInfluenceDiagnostics_popup.js?ver=<%=today%>"></script> <!-- 성능진단 - DB변경 SQL영향도 진단 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/snapId_popup.js?ver=<%=today%>"></script> <!-- snap id 조회 팝업 -->

    <style>
		.datagrid-header-check input{ display: none; }
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="db_name" name="db_name"/>
					<input type="hidden" id="dbid" name="dbid" value="${sqlPerfImpluenceAnalysis.dbid}"/>
					<input type="hidden" id="sql_perf_impl_anal_no" name="sql_perf_impl_anal_no"/>
					<input type="hidden" id="menu_nm" name="menu_nm"/>
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showSchedulePopup();"><i class="btnIcon fas fa-clock fa-lg fa-fw"></i> 스케쥴 등록/변경</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:650px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false,rownumbers:true,singleSelect:true">
				<tbody><tr></tr></tbody>
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlInfluenceDiagnostics_popup.jsp" %> <!-- 성능진단 - SQL영향도 진단 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/snapId_popup.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>