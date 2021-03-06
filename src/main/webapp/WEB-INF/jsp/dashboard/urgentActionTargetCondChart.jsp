<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 *
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
	<script type="text/javascript" src="/resources/js/ui/dashboard/urgentActionTargetCondChart.js?ver=<%=today%>"></script>
	<script>
		var maxCheckDay = '${max_check_day}';	
		var maxCheckDayDash = '${max_check_day_dash}';	
		var maxGatherDay = '${max_gather_day}';	
		var maxGatherDayDash = '${max_gather_day_dash}';
		var todayTxt = '${today_txt}';	
	</script>	
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<div id="dash_contents">
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;min-height:650px">
					<div data-options="title:'????????????????????????',region:'center',split:false,collapsible:false,border:false" style="width:50%;height:100%;padding:5px;cursor:default;">
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%">
							<div id="urgentActionTargetCondChart" title="????????????????????????" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>			
					</div>			
				</div>		
		</div>
	</form:form>
	<!-- dash_contents END -->
</div>
<!-- container END -->
</body>
</html>