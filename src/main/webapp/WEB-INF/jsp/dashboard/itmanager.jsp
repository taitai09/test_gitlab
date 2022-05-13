<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.15	이원식	권한별 dashboard 분리 (관리자)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: OPEN-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/dashboard/itmanager.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- dash_contents START -->
	<div id="dash_contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		</form:form>	
		<div class="full_widget" style="margin-top:0px;height:360px;">
			<div class="w_title_area">
				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 성능 개선 진행 현황</div>
			</div>
			<div style="float:right;margin-top:-35px;"><img src="/resources/images/db_info.gif"/></div>
			<div class="w_body">
				<div id="databaseInfo" class="easyui-panel" data-options="fit:true,border:false" style="padding:5px;">				
				</div>
			</div>
		</div>
		<div class="full_widget" style="height:280px;">
			<div class="w_title_area">
				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 성능 개선 3개월 누적 실적 현황</div>
			</div>
			<div id="performanceChart" class="w_body" style="height:85%">
			</div>
		</div>		
	</div>
	<!-- dash_contents END -->
</div>
<!-- container END -->
</body>
</html>