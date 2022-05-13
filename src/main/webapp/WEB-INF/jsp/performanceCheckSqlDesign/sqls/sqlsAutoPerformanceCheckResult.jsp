<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.09.01	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > 성능 점검 SQL 3차 메뉴 > SQLs 탭 > 성능 점검 결과</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqls/sqlsAutoPerformanceCheckResult.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			
			<input type="hidden" id="perf_check_id" name="perf_check_id"/>
			<input type="hidden" id="program_id" name="program_id"/>
			<input type="hidden" id="perf_check_step_id" name="perf_check_step_id"/>
			<input type="hidden" id="program_execute_tms" name="program_execute_tms"/>
			<input type="hidden" id="top_wrkjob_cd" name="top_wrkjob_cd"/>
			<input type="hidden" id=after_prd_sql_id name="after_prd_sql_id"/>
			<input type="hidden" id="after_prd_plan_hash_value" name="after_prd_plan_hash_value"/>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:290px;">
				<table class="detailT3" id="detailCheckResultTable">
					<colgroup>
						<col style="width:200px;">
						<col style="width:70px;">
						<col style="width:100px;">
						<col style="width:90px;">
						<col style="width:100px;">
						<col style="width:90px;">
						<col style="width:100px;">
						<col style="width:80px;">
						<col style="width:320px;">
					</colgroup>
					<thead>
						<tr>
							<th rowspan="2">검증 지표</th>
							<th rowspan="2">적합</th>
							<th rowspan="2">부적합</th>
							<th colspan="2">배포전 성능</th>
							<th colspan="2">배포후 성능</th>
							<th rowspan="2">예외등록여부</th>
							<th rowspan="2">성능검증 결과내용</th>
						</tr>
						<tr>
							<th>검증 결과값</th>
							<th>검증 결과</th>
							<th>검증 결과값</th>
							<th>검증 결과</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>