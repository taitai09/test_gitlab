<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.08.31	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>신규 배포 SQL 성능 변화 분석(자동 검증) > SQLs 탭 > SQL 성능 추적</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqls/sqlAutoPerformanceTrace.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="db_name" name="db_name"/>
			
			<input type="hidden" id="before_prd_sql_id" name="before_prd_sql_id"/>
			<input type="hidden" id="before_prd_plan_hash_value" name="before_prd_plan_hash_value"/>
			<input type="hidden" id="after_prd_sql_id" name="after_prd_sql_id"/>
			<input type="hidden" id="after_prd_plan_hash_value" name="after_prd_plan_hash_value"/>
			<input type="hidden" id="perf_check_id" name="perf_check_id"/>
			<input type="hidden" id="perf_check_step_id" name="perf_check_step_id"/>
			<input type="hidden" id="program_id" name="program_id"/>
			<input type="hidden" id="program_execute_tms" name="program_execute_tms"/>
			<input type="hidden" id="program_exec_dt" name="program_exec_dt"/>
			<input type="hidden" id="deploy_complete_dt" name="deploy_complete_dt"/>
			<input type="hidden" id="elapsed_time_activity" name="elapsed_time_activity"/>
			<input type="hidden" id="buffer_gets_activity" name="buffer_gets_activity"/>
			<input type="hidden" id="prd_elap_time_increase_ratio" name="prd_elap_time_increase_ratio"/>
			<input type="hidden" id="prd_buffer_gets_increase_ratio" name="prd_buffer_gets_increase_ratio"/>
			<input type="hidden" id="prd_rows_proc_increase_ratio" name="prd_rows_proc_increase_ratio"/>
			
			<input type="hidden" id="performance_chekc_elapsed_time" name="performance_chekc_elapsed_time"/>
			<input type="hidden" id="performance_chekc_buffer_gets" name="performance_chekc_buffer_gets"/>
			<input type="hidden" id="performance_chekc_rows_processed" name="performance_chekc_rows_processed"/>
			
			<input type="hidden" id="after_performance_chekc_elapsed_time" name="after_performance_chekc_elapsed_time"/>
			<input type="hidden" id="after_performance_chekc_buffer_gets" name="after_performance_chekc_buffer_gets"/>
			<input type="hidden" id="after_performance_chekc_rows_processed" name="after_performance_chekc_rows_processed"/>
			
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:275px;">
				<div id="sql_performance_trace" class="sql_performance_trace .font-15">
					<div class="sql_performance_trace_table">
						
						<div class="sql_performance_trace_step">
							<span id="sql_performance_trace_name_current" class="sql_performance_trace_name_begin">성능검증</span>
							<span id="sql_performance_trace_point_begin" class="sql_performance_trace_point_begin"></span>
<!-- 							<span id="sql_performance_trace_point_current" class="sql_performance_trace_point_current"></span> -->
						</div>
						
						<div class="sql_performance_trace_space">
							<div id="sql_performance_trace_space_line_begin" class="sql_performance_trace_space_line_default"></div>
						</div>
						
						<div class="sql_performance_trace_step">
							<span id="sql_performance_trace_name_current" class="sql_performance_trace_name_begin">배포후 운영 성능</span>
<!-- 							<span id="sql_performance_trace_point_after" class="sql_performance_trace_point_after"></span> -->
							<span id="sql_performance_trace_point_current" class="sql_performance_trace_point_current"></span>
						</div>
					</div>
				</div>
				
				<div id="sql_performance_trace" class="sql_performance_trace .font-15">
					<div class="sql_performance_trace_table">
						<div id="sql_performance_trace_status_current" class="sql_performance_trace_status">
							<ul class="sql_performance_trace_status_ul">
								<li class="sql_performance_trace_status_li">수행시간</li>
								<li class="sql_performance_trace_status_li">블럭수</li>
								<li class="sql_performance_trace_status_li">처리건수</li>
<!-- 								<li class="sql_performance_trace_status_li">SQL ID</li> -->
<!-- 								<li class="sql_performance_trace_status_li">PLAN HASH VALUE</li> -->
								<li class="sql_performance_trace_status_li">성능검증일자</li>
								<li class="sql_performance_trace_status_li">배포일자<li>
							</ul>
						</div>
						<div id="sql_performance_trace_status_begin_colon" class="sql_performance_trace_status_colon">
							<ul>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
<!-- 								<li class="sql_performance_trace_status_li_value_non_color">:</li> -->
<!-- 								<li class="sql_performance_trace_status_li_value_non_color">:</li> -->
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
							</ul>
						</div>
						<div class="sql_performance_trace_status_value">
							<ul class="sql_performance_trace_status_ul_value">
								<li id="sql_performance_trace_status_li_value_elapsed_time" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_buffer_gets" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_rows_processed" class="sql_performance_trace_status_li_value"></li>
<!-- 								<li id="sql_performance_trace_status_li_value_sql_id" class="sql_performance_trace_status_li_value"></li> -->
<!-- 								<li id="sql_performance_trace_status_li_value_plan_hash_value" class="sql_performance_trace_status_li_value"></li> -->
								<li id="sql_performance_trace_status_li_value_program_exec_dt" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_deploy_complete_dt" class="sql_performance_trace_status_li_value"></li>
							</ul>
						</div>
						
						<div id="sql_performance_trace_status_after" class="sql_performance_trace_status">
							<ul class="sql_performance_trace_status_ul">
								<li class="sql_performance_trace_status_li">수행시간</li>
								<li class="sql_performance_trace_status_li">블럭수</li>
								<li class="sql_performance_trace_status_li">처리건수</li>
								<li class="sql_performance_trace_status_li">SQL ID</li>
								<li class="sql_performance_trace_status_li">PLAN HASH VALUE</li>
								<li class="sql_performance_trace_status_li">최종수행일시</li>
								<li class="sql_performance_trace_status_li">수행시간 Activity(%)</li>
								<li class="sql_performance_trace_status_li">블럭수 Activity(%)</li>
							</ul>
						</div>
						<div id="sql_performance_trace_status_begin_colon" class="sql_performance_trace_status_colon">
<!-- 							<ul class="sql_performance_trace_status_ul_colon"> -->
							<ul>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
								<li class="sql_performance_trace_status_li_value_non_color">:</li>
							</ul>
						</div>
						<div class="sql_performance_trace_status_after_value">
							<ul class="sql_performance_trace_status_ul_value">
								<li id="sql_performance_trace_status_li_value_after_elapsed_time" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_buffer_gets" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_rows_processed" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_sql_id" class="sql_performance_trace_status_li_value_action" onclick="sqlPerformanceDetailAfter(this.id)"></li>
								<li id="sql_performance_trace_status_li_value_after_plan_hash_value" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_last_active_time" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_elapsed_time_activity" class="sql_performance_trace_status_li_value"></li>
								<li id="sql_performance_trace_status_li_value_after_buffer_gets_activity" class="sql_performance_trace_status_li_value"></li>
							</ul>
						</div>
						<div id="sql_performance_trace_status_after_ratio" class="sql_performance_trace_status_after_ratio">
							<ul class="sql_performance_trace_status_ul_value">
								<li id="sql_performance_trace_status_li_value_after_prd_elap_time_increase_ratio" class="sql_performance_trace_status_li_value_after"></li>
								<li id="sql_performance_trace_status_li_value_after_prd_buffer_gets_increase_ratio" class="sql_performance_trace_status_li_value_after"></li>
								<li id="sql_performance_trace_status_li_value_after_prd_rows_proc_increase_ratio" class="sql_performance_trace_status_li_value_after"></li>
							</ul>
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