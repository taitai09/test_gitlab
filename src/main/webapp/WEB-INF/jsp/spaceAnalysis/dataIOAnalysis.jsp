<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.02	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>데이터파일 I/O 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/spaceAnalysis/dataIOAnalysis.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="start_time" name="start_time"/>
					<input type="hidden" id="end_time" name="end_time"/>
					<input type="hidden" id="file_name" name="file_name"/>
					<input type="hidden" id="file_id" name="file_id"/>

					<label>DB</label>
					<select id="selectDbid" name="selectDbid" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
					<label>조회일시</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" />
					<input type="text" id="strStartTime" name="strStartTime" value="${startTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false" /> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" />
					<input type="text" id="strEndTime" name="strEndTime" value="${nowTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false" />
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px;">
			<div id="dataIOChart" data-options="region:'center',split:false,collapsible:false,border:true" style="padding:5px 0px;">
			</div>
			<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:300px;padding-top:10px;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>