<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.09	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>오브젝트변경 SQL영향도 진단 :: 테이블 내역 - 상세 내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlInfluenceDiagnostics/objectImpactDiagnosticsTableDetail.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlProfile_popup.js?ver=<%=today%>"></script> <!-- SQL Profile 팝업 -->

</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm} - [${sqlPerfImplAnalSql.table_owner}.${sqlPerfImplAnalSql.list_table_name}]</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${sqlPerfImplAnalSql.dbid}"/>
					<input type="hidden" id="selectValue" name="selectValue" value="${sqlPerfImplAnalSql.selectValue}"/>
					<input type="hidden" id="sql_perf_impl_anal_no" name="sql_perf_impl_anal_no" value="${sqlPerfImplAnalSql.sql_perf_impl_anal_no}"/>
					<input type="hidden" id="db_name" name="db_name" value="${sqlPerfImplAnalSql.db_name}"/>
					<input type="hidden" id="plan_change_yn" name="plan_change_yn" value="${sqlPerfImplAnalSql.plan_change_yn}"/>
					<input type="hidden" id="table_owner" name="table_owner" value="${sqlPerfImplAnalSql.table_owner}"/>
					<input type="hidden" id="list_table_name" name="list_table_name" value="${sqlPerfImplAnalSql.list_table_name}"/>
					<input type="hidden" id="table_name" name="table_name" value="${sqlPerfImplAnalSql.table_name}"/>
					<input type="hidden" id="perf_impact_type_cd" name="perf_impact_type_cd" value="${sqlPerfImplAnalSql.perf_impact_type_cd}"/>
					<input type="hidden" id="sql_id" name="sql_id"/>
					<input type="hidden" id="before_plan_hash_value" name="before_plan_hash_value"/>
					<input type="hidden" id="sql_profile_nm" name="sql_profile_nm"/>
					<input type="hidden" id="menu_nm" name="menu_nm"/>
					<label>SQL식별자(DBIO)</label>
					<input type="text" id="dbio" name="dbio" value="${beforePerfExpectSqlStat.dbio}" class="w300 easyui-textbox"/>
					<label>성능임팩트유형</label>
					<select id="selPerfImpactTypeCd" name="selPerfImpactTypeCd" data-options="panelHeight:'auto'" class="w130 easyui-combobox"></select>
					<label>실행계획변경</label>
					<input type="checkbox" id="chkPlanChangeYn" name="chkPlanChangeYn" class="easyui-switchbutton"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>								
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn innerBtn" style="margin-top:10px;margin-bottom:0px;">
			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="goList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 돌아가기</a>
			<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Show_SqlProfilePopup();"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> SQL Profile 적용</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlProfile_popup.jsp" %> <!-- SQL Profile 팝업 -->
</body>
</html>