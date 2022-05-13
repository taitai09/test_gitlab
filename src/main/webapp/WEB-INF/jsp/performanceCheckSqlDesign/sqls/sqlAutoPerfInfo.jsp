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
	<title>신규 배포 SQL 성능 변화 분석(자동 검증) > SQLs 탭 > SQL Info</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqls/sqlAutoPerfInfo.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			
			<input type="hidden" id="program_id" name="program_id"/>
			<input type="hidden" id="perf_check_id" name="perf_check_id"/>
			<input type="hidden" id="perf_check_step_id" name="perf_check_step_id"/>
			<input type="hidden" id="program_execute_tms" name="program_execute_tms"/>
			<input type="hidden" id="after_prd_sql_id" name="after_prd_sql_id"/>
			<input type="hidden" id="after_prd_plan_hash_value" name="after_prd_plan_hash_value"/>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:530px;">
				<div data-options="iconCls:'fas fa-caret-square-right',title:'운영  SQL',region:'center',split:false,collapsible:false,border:true" style="width:99%;height:500px; padding-top:5px;overflow-y: hidden;">
					<div style="width:100%; display: flex;margin-bottom: 5px;">
						<div id="sql_text_area" style="width: 80%">
							<input id="sql_text_operation" class="easyui-textbox" data-options="readonly:true" multiline="true" style="width:99.9%;height:250px;">
						</div>
						<div id="sql_bind_area" style="width: 20%">
							<input id="sql_bind_operation" class="easyui-textbox" data-options="readonly:true" multiline="true" style="width:100%;height:250px;">
						</div>
					</div>
					<div class="easyui-panel" data-options="border:false" style="width:100%;height:235px;margin-bottom:10px;">
						<table id="sql_plan_operation" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
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