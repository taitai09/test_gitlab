<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.05.18 initialize 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 SQL 메뉴 Design</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/design.js?ver=<%=today%>"></script>
	<style>
		.tabs-header{
			background-color: rgb(255, 255, 255);
		}
	</style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<input type="hidden" id="menu_id" name="menu_id" value="${menu_id}"/>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="performanceCheckSqlTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="SQL 성능 추적 현황" data-options="fit:true,tabWidth:110" style="padding:5px;">
					<iframe id="performanceCheckSqlDesignIF1" name="performanceCheckSqlDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/PerformanceCheckSqlDesign/sqlPerformanceTraceStatusDesign" style="width:100%;min-height:680px;"></iframe>
<!-- 							style="width:100%;min-height:680px;"></iframe> -->
				</div>
				<div title="SQLs" data-options="fit:true,tabWidth:110" style="padding:5px;">
					<iframe id="performanceCheckSqlDesignIF2" name="performanceCheckSqlDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>