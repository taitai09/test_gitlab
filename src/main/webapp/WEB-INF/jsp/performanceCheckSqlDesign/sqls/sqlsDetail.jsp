<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.06.05	명성태	최초작성 (SQLs > SQL 성능 추적 > SQL ID에서 호출)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > 성능 점검 SQL 3차 메뉴 > 일시적인 탭</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqls/sqlsDetail.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents" style="width:100%;height:700px;">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid" value="${dbid}"/>
			
			<input type="hidden" id="after_prd_sql_id" name="after_prd_sql_id" value="${sql_id}"/>
			<input type="hidden" id="sql_id" name="sql_id" value="${sql_id}"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${plan_hash_value}"/>
			<input type="hidden" id="begin_dt" name="begin_dt" value="${begin_dt}"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="50"/>
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="bindPage" name="bindPage" value="1"/>
			<input type="hidden" id="historyPage" name="historyPage" value="1"/>
			
			<div class="easyui-layout" style="width:100%;height:680px;">
				<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:45%;height:80%;padding:5px;">
					<div class="easyui-tabs" data-options="plain:true,fit:true,border:false">
						<div title="SQL TEXT" class="tabTxt" style="height:100%;padding:5px;">
							<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:97%;height:75%" wrap="off" readonly></textarea>
							<div class="searchBtn" style="margin-top:10px;">
								<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
							</div>
						</div>
					</div>
				</div>
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding:5px;padding-right:7px;">
					<div class="easyui-tabs" data-options="plain:true,fit:true,border:false">
						<div title="Bind Value" class="tabGrid">
							<table id="bindValueList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:83%;">
								<tbody><tr></tr></tbody>
							</table>
							<div class="searchBtn" style="margin-top:10px;">
								<a href="javascript:;" id="bindBtn" class="w80 easyui-linkbutton" data-options="iconCls:'icon-next'" onClick="Btn_NextBindSearch();">다음</a>
							</div>
						</div>
						<div title="Tree Plan" class="tabGrid">
							<ul id="treePlan" style="width:96%;height:95%;padding:5px;"></ul>
						</div>
						<div title="Text Plan" class="tabGrid" style="padding:5px;">
							<ul id="textPlan" style="width:96%;height:95%;padding:5px;white-space:nowrap;"></ul>
						</div>
						<div title="Grid Plan" class="tabGrid">
							<table id="sqlGridPlanList" class="tbl easyui-treegrid" data-options="border:false" style="width:100%;height:98%;">
							</table>
						</div>
						<div title="OutLine" class="tabGrid">
							<table id="outLineList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:98%;">
							</table>
						</div>
					</div>
				</div>
				<div data-options="region:'south',split:false,collapsible:false,border:false" style="width:100%;height:47%;padding:5px;">
					<div class="easyui-tabs" data-options="plain:true,fit:true,border:false">
						<div title="SQL Stat Trend">
							<table style="width:100%;height:265px;" class="detailT4">
								<colgroup>
									<col style="width:25%;">
									<col style="width:25%;">
									<col style="width:25%;">
									<col style="width:25%;">
								</colgroup>
								<thead>
									<tr>
										<th style="height:5px;">Executions</th>
										<th style="height:5px;">Elapsed Time</th>
										<th style="height:5px;">Buffer Gets</th>
										<th style="height:5px;">Disk Reads</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td style="border-left:1px solid #FFF;">
											<div id="chartExecutionsPanel" title="" style="width:100%;height:100%;padding-top:0px;">
											</div>
										</td>
										<td>
											<div id="chartElapsedTimePanel" title="" style="width:100%;height:100%;padding-top:0px;">
											</div>
										</td>
										<td>
											<div id="chartBufferGetsPanel" title="" style="width:100%;height:100%;padding-top:0px;">
											</div>
										</td>
										<td>
											<div id="chartDiskReadsPanel" title="" style="width:100%;height:100%;padding-top:0px;">
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div title="SQL Stat History">
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
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>