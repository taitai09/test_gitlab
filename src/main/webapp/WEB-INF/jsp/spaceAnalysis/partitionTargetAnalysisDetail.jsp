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
	<title>파티셔닝 대상 상세 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/spaceAnalysis/partitionTargetAnalysisDetail.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="dbid" name="dbid" value="${odsTables.dbid}"/>
					<label>DB</label>
					<input type="text" id="db_name" name="db_name" data-options="readonly:true" value="${odsTables.db_name}" class="w100 easyui-textbox"/>
					<label>OWNER</label>
					<input type="text" id="owner" name="owner" data-options="readonly:true" value="${odsTables.owner}" class="w100 easyui-textbox"/>
					<label>TABLE NAME</label>
					<input type="text" id="table_name" name="table_name" value="${odsTables.table_name}" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
			<div id="tableSizeHistoryChart" data-options="title:'▶ Table Size History',region:'north',split:false,collapsible:false,border:false" style="height:350px;padding:5px 0px;">
			</div>
			<div data-options="title:'▶ Access Path',region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:5px 5px 0px 0px;">
				<table id="accessPathList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="title:'▶ Partition Key Recommendation',region:'center',split:false,collapsible:false,border:false" style="padding:5px 0px 0px 5px;">
				<table id="recommendList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>