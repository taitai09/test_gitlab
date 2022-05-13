<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.03.22	명성태	OPENPOP V3 최초작업 (SQL 정보 탭구성 호출 - 신규 SQL 타임아웃 예측)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/predictiveFailure/sqlInformationNSTP.js?ver=<%=today%>"></script>
    <style>
	.tree-node {
	  cursor: default;
	}
	</style>       
</head>
<body>
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${odsHistSqlText.dbid}"/>
		<input type="hidden" id="sql_id" name="sql_id" value="${odsHistSqlText.sql_id}"/>
		<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${odsHistSqlText.plan_hash_value}"/>
		<input type="hidden" id="pagePerCount" name="pagePerCount" value="50"/>
		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
		<input type="hidden" id="bindPage" name="bindPage" value="1"/>
		<input type="hidden" id="historyPage" name="historyPage" value="1"/>	
		<div class="easyui-layout" style="width:100%;height:404px">
			<div data-options="region:'west',border:false" style="width:45%;height:60%;padding:5px;">
				<div class="easyui-tabs" data-options="plain:true,fit:true,border:false">
					<div title="SQL TEXT" class="tabTxt" style="height:100%;padding:5px;">
						<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:97%;height:82%" wrap="off" readonly></textarea>
						<div class="searchBtn" style="margin-top:10px;">
							<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
						</div>
					</div>
				</div>
			</div>
			<div data-options="region:'center',split:false,border:false" style="padding:5px;padding-right:7px;">
				<div class="easyui-tabs" data-options="plain:true,fit:true,border:false">
					<div title="Tree Plan" class="tabGrid">
						<ul id="treePlan" style="width:98%;height:96%;padding:5px;"></ul>
					</div>
					<div title="Text Plan" class="tabGrid" style="padding:5px;">
						<ul id="textPlan" style="width:98%;height:96%;padding:5px;white-space:nowrap;"></ul>
					</div>
					<div title="Grid Plan" class="tabGrid">
						<table id="sqlGridPlanList" class="tbl easyui-treegrid" data-options="border:false" style="width:100%;height:98%;">
						</table>
					</div>
					<div title="Bind Value" class="tabGrid">
						<table id="bindValueList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:88%;">
							<tbody><tr></tr></tbody>
						</table>
						<div class="searchBtn" style="margin-top:10px;">
							<a href="javascript:;" id="bindBtn" class="w80 easyui-linkbutton" data-options="iconCls:'icon-next'" onClick="Btn_NextBindSearch();">다음</a>
						</div>
					</div>
					<div title="OutLine" class="tabGrid">
						<table id="outLineList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:98%;">
						</table>
					</div>
					<div title="유사 SQL" class="tabGrid">
						<table id="similarityList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:98%;">
						</table>
					</div>
					<div title="AWR SQL Stats" class="tabGrid">
						<table id="bottomList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:83%;">
						</table>
						<div class="searchBtn" style="margin-top:10px;">
							<a href="javascript:;" id="historyBtn" class="w80 easyui-linkbutton" data-options="iconCls:'icon-next'" onClick="Btn_NextHistorySearch();">다음</a>
						</div>	
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>