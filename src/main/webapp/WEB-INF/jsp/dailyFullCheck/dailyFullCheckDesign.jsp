<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.03.24	명성태	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일 종합 진단</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/css/function/dailyCheckDb.css">
	<script type="text/javascript" src="/resources/js/ui/dailyFullCheck/dailyCheckDb.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
<!-- 		<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%;"> -->
<!-- 		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:780px;"> -->
<!-- 		<div class="easyui-panel" data-options="border:false" style="width:100%;height:850px;overflow:hidden;"> -->
		<div class="easyui-panel" data-options="border:false" style="width:100%;height:1850px;">
			<div id="dailyFullCheckDesignTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
				<div title="DB 점검" style="padding:5px;">
					<form:form method="post" id="dailyCheckDb_form" name="dailyCheckDb_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="dbid" name="dbid"/>
						
						<input type="hidden" id="severity_color_0" name="severity_color_0"/>
						<input type="hidden" id="severity_color_1" name="severity_color_1"/>
						<input type="hidden" id="severity_color_2" name="severit_colory_2"/>
						<input type="hidden" id="severity_color_3" name="severity_color_3"/>
						<input type="hidden" id="choice_db_group_id" name="choice_db_group_id"/>
						<input type="hidden" id="choice_severity_id" name="choice_severity_id"/>
						
						<jsp:include page="/WEB-INF/jsp/dailyFullCheck/dailyCheckDb.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="SQL 성능 점검" style="padding:5px;">
					<form:form method="post" id="sqlCheckDb_form" name="sqlCheckDb_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="dbid" name="dbid"/>
						
						<input type="hidden" id="severity_color_0" name="severity_color_0"/>
						<input type="hidden" id="severity_color_1" name="severity_color_1"/>
						<input type="hidden" id="severity_color_2" name="severit_colory_2"/>
						<input type="hidden" id="severity_color_3" name="severity_color_3"/>
						<input type="hidden" id="choice_db_group_id" name="choice_db_group_id"/>
						<input type="hidden" id="choice_severity_id" name="choice_severity_id"/>
						
<%-- 						<jsp:include page="/WEB-INF/jsp/dailyFullCheck/sqlCheckDb.jsp"></jsp:include> --%>
						<jsp:include page="/WEB-INF/jsp/error/working.jsp"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>