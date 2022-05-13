<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.14	반광수	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>Tablespace 한계점 예측 - 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
    <script type="text/javascript" src="/resources/js/ui/predictiveFailure/tablespaceLimitPointPredictionDetail.js?ver=<%=today%>"></script>   
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${tablespaceLimitPoint.dbid}"/>
		<input type="hidden" id="prediction_dt" name="prediction_dt" value="${tablespaceLimitPoint.prediction_dt}"/>
		<input type="hidden" id="tablespace_name" name="tablespace_name" value="${tablespaceLimitPoint.tablespace_name}"/>
		<input type="hidden" id="nav_from_parent" name="nav_from_parent" value="${tablespaceLimitPoint.nav_from_parent}"/>
		<input type="hidden" id="predict_standard" name="predict_standard" value="${tablespaceLimitPoint.predict_standard}"/>
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<label>예측 기준</label>
					<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
						<option value="1" <c:if test="${tablespaceLimitPoint.predict_standard eq '1'}">selected</c:if>>1개월 후 한계점 도달</option>
						<option value="2" <c:if test="${tablespaceLimitPoint.predict_standard eq '2'}">selected</c:if>>2개월 후 한계점 도달</option>
						<option value="3" <c:if test="${tablespaceLimitPoint.predict_standard eq '3'}">selected</c:if>>3개월 후 한계점 도달</option>	
						<option value="6" <c:if test="${tablespaceLimitPoint.predict_standard eq '6'}">selected</c:if>>6개월 후 한계점 도달</option>
						<option value="12" <c:if test="${tablespaceLimitPoint.predict_standard eq '12'}">selected</c:if>>12개월 후 한계점 도달</option>
					</select>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w110 easyui-combobox"></select>					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
<!-- 			<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;"> -->
<!-- 				<div class="easyui-panel" data-options="border:false" style="width:100%;height:580px"> -->
<!-- 					<div id="chartDrawPanel" style="width:100%;height:100%"></div> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>

		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
			<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:330px;padding-top:5px;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div id="chartDrawPanel1" data-options="title:'',region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:10px 5px 0px 0px;">
			</div>
			<div id="chartDrawPanel2" data-options="title:'',region:'center',split:false,collapsible:false,border:false" style="padding:10px 0px 0px 5px;">
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>