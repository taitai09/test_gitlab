<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.11	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>CPU 한계점 예측</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/predictiveFailure/cpuLimitPointPrediction.js?ver=<%=today%>"></script>        
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
		<div id="contents">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="dbid" name="dbid" value="${cpuLimitPrediction.dbid}"/>
						<input type="hidden" id="inst_id" name="inst_id" value="${cpuLimitPrediction.inst_id}"/>
						<input type="hidden" id="prediction_dt" name="prediction_dt"/>

						<input type="hidden" id="resource_type" name="resource_type" value="${resource_type}"/>
						<input type="hidden" id="predict_standard" name="predict_standard" value="${predict_standard}"/>
					</form:form>
					
					<label>예측 기준</label>
					<select id="selectPredictStandard" name="selectPredictStandard" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required">
						<option value="1" <c:if test="${predict_standard eq 1}">selected</c:if>>1개월 후 한계점 도달</option>
						<option value="2" <c:if test="${predict_standard eq 2}">selected</c:if>>2개월 후 한계점 도달</option>
						<option value="3" <c:if test="${predict_standard eq 3}">selected</c:if>>3개월 후 한계점 도달</option>
						<option value="6" <c:if test="${predict_standard eq 6}">selected</c:if>>6개월 후 한계점 도달</option>
						<option value="12" <c:if test="${predict_standard eq 12}">selected</c:if>>12개월 후 한계점 도달</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:630px">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
	<!-- contents END -->
</div>
<!-- container START -->
</body>
</html>