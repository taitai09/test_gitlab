<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.09	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 성능 분석 - SQL Tuning Advisor</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/sqlPerformance/sqlTuningAdvisor.js?ver=<%=today%>"></script>
    <script>
    	var callFromParent = '${odsHistSqlText.call_from_parent}';
    </script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${odsHistSqlText.dbid}"/>
					<input type="hidden" id="call_from_parent" name="call_from_parent" value="${odsHistSqlText.call_from_parent}"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false,readonly:true" class="w120 easyui-combobox"></select>
					<label>SQL_ID</label>
					<input type="text" id="sql_id" name="sql_id" value="${odsHistSqlText.sql_id}" class="w120 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행</a>
					</span>
				</form:form>								
			</div>
		</div>
		<div id="dataArea" class="easyui-panel" data-options="border:true" style="width:100%;padding:10px;height:650px;font-family:'굴림체';font-size:11px;">
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>