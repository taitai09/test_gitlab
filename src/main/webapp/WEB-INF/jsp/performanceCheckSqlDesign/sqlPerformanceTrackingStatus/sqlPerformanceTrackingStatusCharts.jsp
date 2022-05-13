<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.06.24	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > Charts 탭</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<link rel="stylesheet" href="/resources/css/function/distributeSqlPerformanceTraceChart_style.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqlPerformanceTrackingStatus/sqlPerformanceTrackingStatusCharts.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="db_name" name="db_name"/>
		
		<input type="hidden" id="base_period_value" name="base_period_value" value="2" />
		<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
		
		<div id="contents">
			<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%">
				<div class="well">
					<label>업무</label>
					<select id="wrkjob_cd_top_level" name="wrkjob_cd_top_level" data-options="panelHeight:'300px',editable:true,required:true" class="w200 easyui-combotree"></select>
					<span style="margin-left:5px"/>
					<input id="checkHighestRankYn" name="checkHighestRankYn" class="easyui-checkbox">
					<label style="margin-left:0px;">최상위</label>
					<span style="margin-left:20px"/>
					<label>배포일자</label>
					<span id="span_analysis_day">
						<input type="text" id="begin_dt" name="begin_dt" value="${performanceCheckSql.begin_dt}" class="w100 datapicker easyui-datebox" required="true"/> ~
						<input type="text" id="end_dt" name="end_dt" value="${performanceCheckSql.end_dt}" class="w100 datapicker easyui-datebox" required="true"/>
					</span>
					<span style="margin-left:5px;"/>
					<input class="easyui-radiobutton" id="base_daily" name="base_daily" value="1" label="1일" labelPosition="after" labelWidth="40px;">
					<input class="easyui-radiobutton" id="base_weekly" name="base_weekly" value="2" label="1주일" labelPosition="after" labelWidth="40px;">
					<input class="easyui-radiobutton" id="base_monthly" name="base_monthly" value="3" label="1개월" labelPosition="after" labelWidth="40px;">
					<span style="margin-left:20px"/>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:560px;border-bottom:1px solid;border-left:1px solid lightgray;overflow-y:auto;">
				<table style="width:100%;height:300px;border-right:1px solid lightgray;margin-top:0px;" class="detailT4">
					<colgroup>
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
					</colgroup>
					<thead>
						<tr>
							<th colspan="3" style="height: 20px;">Buffer Gets</th>
							<th colspan="3" style="height: 20px;">Elapsed Time</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="3" style="border-left: 1px solid #FFF;">
								<div id="chartBG" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="3">
								<div id="chartET" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
						</tr>
						<tr>
							<td style="border-left: 1px solid #FFF;">
								<div id="chartBGPie" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="2" style="border-left: 1px solid #FFF;">
								<div id="chartBGBar" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td>
								<div id="chartETPie" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="2" style="border-left: 1px solid #FFF;">
								<div id="chartETBar" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
						</tr>
					</tbody>
				</table><br><br>
				<table style="width:100%;height:300px;border-right:1px solid lightgray" class="detailT4">
					<colgroup>
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
						<col style="width: 16.5%;">
					</colgroup>
					<thead>
						<tr>
							<th colspan="3" style="height: 20px;">성능 검증 SQL</th>
							<th colspan="3" style="height: 20px;">예외 처리 SQL</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="3" style="border-left: 1px solid #FFF;">
								<div id="chartPCSql" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="3">
								<div id="chartEHSql" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
						</tr>
						<tr>
							<td style="border-left: 1px solid #FFF;">
								<div id="chartPCSqlPie" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="2" style="border-left: 1px solid #FFF;">
								<div id="chartPCSqlBar" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td>
								<div id="chartEHSqlPie" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
							<td colspan="2" style="border-left: 1px solid #FFF;">
								<div id="chartEHSqlBar" title="" style="width: 100%;height: 100%;padding-top: 0px;"></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>