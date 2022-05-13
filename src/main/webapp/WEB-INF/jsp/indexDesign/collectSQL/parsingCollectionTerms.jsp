<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>수집 SQL 조건절 파싱</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesign/collectSQL/parsingCollectionTerms.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/parsingCollection2_popup.js?ver=<%=today%>"></script> <!-- 성능개선 - 인덱스설계사전처리 - 수집SQL 조건절파싱  팝업 -->
	<style>
		input[type="text"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 23px !important;
		    line-height: 23px !important;
		}
		input[type="checkbox"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 20px !important;
		    line-height: 23px !important;
		}	
	</style>
        
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%">
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="exec_seq" name="exec_seq"/>
<%-- 					<input type="hidden" id="startDateTime" value="${startDateTime}" /> --%>
<%-- 					<input type="hidden" id="nowDateTime" value="${nowDateTime}" /> --%>

					<label>DB </label>
					<select id="selectDbid" name="selectDbid" data-options="editable:false" class="w130 easyui-combobox" required="true">
					</select>
					<label>파싱 일시</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w150 easyui-linkbutton" onClick="showSnapShot();"><i class="btnIcon fab fa-quinscape fa-lg fa-fw"></i> 수집 SQL 조건절 파싱</a>
					</div>
				</form:form>
			</div>
		</div>			
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:560px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/parsingCollection2_popup.jsp" %> <!-- 성능개선 - 인덱스설계사전처리 - 수집SQL 조건절파싱  팝업 -->
	
</body>
</html>