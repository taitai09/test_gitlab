<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	<title>:: OPEN-POP V2 ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/dashboard/dashboardV2Left.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/dashboard/dashboardV2Right.js?ver=<%=today%>"></script>
	<script>
		var maxCheckDay = '${max_check_day}';
		var maxCheckDayDash = '${max_check_day_dash}';
		var maxGatherDay = '${max_gather_day}';
		var maxGatherDayDash = '${max_gather_day_dash}';
		var todayTxt = '${today_txt}';
		var maxBaseDay = '${max_base_day}';
	</script>
</head>
<body>
<!-- container START -->
<div id="container dash_contents">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" name="strGubun" id="strGubun"/>
		<input type="hidden" name="dbid" id="dbid"/>
		<input type="hidden" name="inst_id" id="inst_id"/>
		<div id="dash_contents">
			<table style="width:100%;height:760px;">
				<colgroup>
					<col style="width:50%;"/>
					<col style="width:50%;"/>
				</colgroup>
				<tr>
					<td>
						<jsp:include page="/WEB-INF/jsp/include/dashboardV2Left.jsp"></jsp:include>
					</td>
					<td>
						<jsp:include page="/WEB-INF/jsp/include/dashboardV2Right.jsp"></jsp:include>
					</td>
				</tr>
			</table>
		</div>
	</form:form>
	<!-- dash_contents END -->
</div>
	<form:form method="post" id="submit_form_left" name="submit_form_left" class="form-inline">
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="user_id" name="user_id"/>
		<input type="hidden" id="gather_day" name="gather_day"/>
		<input type="hidden" id="check_grade_cd" name="check_grade_cd"/>
		<input type="hidden" name="gather_day_dash" id="gather_day_dash" value="${gather_day_dash}"/>
		<input type="hidden" name="check_date_topsql_diag_summary" id="check_date_topsql_diag_summary" value="${check_date_topsql_diag_summary}"/>
	</form:form>
<!-- container END -->
</body>
</html>