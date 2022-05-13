<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.15	이원식	권한별 dashboard 분리 (튜너)
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
    <script type="text/javascript" src="/resources/js/ui/dashboard/tuner.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- dash_contents START -->
	<div id="dash_contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="tuning_no" name="tuning_no"/>
			<input type="hidden" id="guide_no" name="guide_no"/>
		</form:form>	
		<div class="full_widget" style="margin-top:0px;">
			<div class="w_title_area">
				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 튜닝 작업 대기</div>
			</div>
			<div class="w_body" style="height:85%">
				<table id="waitJobList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					<tbody><tr></tr></tbody>
				</table>
			</div>
		</div>	
		<div class="full_widget">
			<div class="w_body" style="height:99%">
				<div id="progressTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div title="튜닝 진행" style="padding-top:5px;">
						<table id="progressList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="튜닝 지연 예상" style="padding-top:5px;">
						<table id="expectedDelayList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="튜닝 지연" style="padding-top:5px;">
						<table id="delayList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="주간 튜닝 완료" style="padding-top:5px;">
						<table id="completeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>					
					</div>
				</div>
			</div>
		</div>
		<div class="full_widget">
			<div class="w_title_area">
				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 튜닝실적(최근1년)</div>
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