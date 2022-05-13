<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.06.12	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > 성능 점검 SQL 3차 메뉴 > SQL 성능 추적 현황 탭 > 예외 처리 SQL 탭</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqlPerformanceTraceStatus/exceptionHandlingSql.js?ver=<%=today%>"></script>
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
		<input type="hidden" id="exception_prc_meth_cd" name="exception_prc_meth_cd" />
		
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
					<label>예외처리방법</label>
					<select id="selectSqlPerfTrace" name="selectSqlPerfTrace" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
					</select>
					<span style="margin-left:20px"/>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					
					<div class="searchBtn innerBtn">
<!-- 						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Test();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> Test</a> -->
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:560px">
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;height:100%;padding:1px;">
					<table id="treeList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>