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
	<title>CPU사용량 증가원인예측</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
    <script type="text/javascript" src="/resources/js/ui/predictiveFailure/cpuIncreaseUsage.js?ver=<%=today%>"></script>   
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="db_name" name="db_name"/>
		<input type="hidden" id="inst_id" name="inst_id"/>

		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<label>예측 기준</label>
					<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox">
						<option value="1.1">Baseline 기준 CPU 1.1배 이상 증가</option>
						<option value="1.2">Baseline 기준 CPU 1.2배 이상 증가</option>
						<option value="1.5">Baseline 기준 CPU 1.5배 이상 증가</option>
						<option value="1.8">Baseline 기준 CPU 1.8배 이상 증가</option>
						<option value="2">Baseline 기준 CPU 2배 이상 증가</option>
						<option value="3">Baseline 기준 CPU 3배 이상 증가</option>
						<option value="4">Baseline 기준 CPU 4배 이상 증가</option>
						<option value="5">Baseline 기준 CPU 5배 이상 증가</option>
					</select>
					<label>기준일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					<label>시간 구분</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w110 easyui-combobox">
						<option value="">전체</option>
						<option value="01">주간</option>
						<option value="02">야간</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:true" style="width:100%;height:650px;">
				<div id="cpuUsageChart" style="width:100%;height:100%"></div>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>