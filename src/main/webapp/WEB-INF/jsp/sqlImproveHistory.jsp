<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.17	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 개선 이력</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlImproveHistory.js?ver=<%=today%>"></script>
</head>
<body>
<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
	<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
	<input type="hidden" id="tuning_no" name="tuning_no" value="${sqlTuningHistory.tuning_no}"/>
	<input type="hidden" id="update_dt" name="update_dt"/>
	<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value=""/>						
	<div id="sqlImprove" class="easyui-panel" data-options="border:false" style="width:100%;min-height:650px;padding-top:10px;">
		<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
</form:form>
</body>
</html>