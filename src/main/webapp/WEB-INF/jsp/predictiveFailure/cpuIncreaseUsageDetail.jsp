<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.07	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>CPU사용량 증가원인예측 - 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
    <script type="text/javascript" src="/resources/js/ui/predictiveFailure/cpuIncreaseUsageDetail.js?ver=<%=today%>"></script>   
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${cpuIncreaseUsage.dbid}"/>
		<input type="hidden" id="selectValue" name="selectValue" value="${cpuIncreaseUsage.selectValue}"/>
		<input type="hidden" id="inst_id" name="inst_id" value="${cpuIncreaseUsage.inst_id}"/>
		<input type="hidden" id="searchKey" name="searchKey" value="${cpuIncreaseUsage.searchKey}"/>
		<input type="hidden" id="strGubun" name="strGubun"/>
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<label>DB</label>
					<input type="text" id="db_name" name="db_name" value="${cpuIncreaseUsage.db_name}" data-options="readonly:true" class="w130 easyui-textbox"/>
					<label>인스턴스명</label>
					<input type="text" id="inst_nm" name="inst_nm" value="${cpuIncreaseUsage.inst_nm}" data-options="readonly:true" class="w130 easyui-textbox"/>
					<label>기준일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${cpuIncreaseUsage.strStartDt}" data-options="readonly:true" class="w130 easyui-textbox"/>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;height:300px">
				<div class="easyui-layout" data-options="fit:true,border:false">
					<div id="cpuUsageChart" data-options="region:'west',split:false,border:false" style="width:32%">
					</div>
					<div id="userTimeChart" data-options="region:'center',split:false,border:false">
					</div>
					<div id="sysTimeChart" data-options="region:'east',split:false,border:false" style="width:32%">
					</div>
				</div>
			</div>
			<div id="timeModelChart" class="easyui-panel" data-options="border:false" style="width:100%;height:400px">
			</div>
			<div class="tbl_title" style="margin-bottom:10px;"><span id="subTitle" class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> TOP SQL</span></div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;height:250px;margin-bottom:10px;">
				<table id="topSqlList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>