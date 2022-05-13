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
	<title>오브젝트변경 SQL영향도 진단 :: 테이블 내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlInfluenceDiagnostics/objectImpactDiagnosticsTable.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm} - [${sqlPerfImplAnalTable.sql_perf_impl_anal_no} - ${sqlPerfImplAnalTable.db_name}]</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="selectValue" name="selectValue" value="${sqlPerfImplAnalTable.selectValue}"/>
					<input type="hidden" id="dbid" name="dbid" value="${sqlPerfImplAnalTable.dbid}"/>
					<input type="hidden" id="db_name" name="db_name" value="${sqlPerfImplAnalTable.db_name}"/>
					<input type="hidden" id="sql_perf_impl_anal_no" name="sql_perf_impl_anal_no" value="${sqlPerfImplAnalTable.sql_perf_impl_anal_no}"/>
					<input type="hidden" id="table_owner" name="table_owner"/>
					<input type="hidden" id="list_table_name" name="list_table_name"/>
					<input type="hidden" id="menu_nm" name="menu_nm"/>
					<label>테이블명</label>
					<input type="text" id="table_name" name="table_name" value="${sqlPerfImplAnalTable.table_name}" class="w120 easyui-textbox"/>
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
		</div>		
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>