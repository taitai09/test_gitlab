<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true" %>
<%
/***********************************************************
 * 2021.09.23	김원재	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>종합 진단 :: SQL 품질 진단 :: SQL 품질 진단 현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReportStatus.js?ver=<%=today%>"></script>
<!-- 		<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReportStatus.js"></script> -->
	
	<!-- RSA 필수사항 자바라이브러리 -->
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rsa2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<!-- END RSA 필수사항 자바라이브러리 -->
	<style>
		#contents .datagrid-header-row td div{
			white-space: break-spaces;
			padding : 5px;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="manageScheduler">
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			
			<div class="well searchOption">
				<div style="display:inline">
					<label>품질진단DB</label>
					<select id="db_name_combo" name="std_qty_target_dbid_combo" data-options="editable:false" class="w170 easyui-combobox" required></select>
				</div>
				<a href="javascript:;" class="w80 easyui-linkbutton" style="margin-left : 10px;"  onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				
				<a href="javascript:;" style="float: right;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			
			<input type="hidden" name='std_qty_target_dbid' id='std_qty_target_dbid' value=''>
			
		</form:form>
		
		<form:form method="post" id="excel_submit_form" name="submit_form" class="form-inline">
			<input type="hidden" id="excel_std_qty_target_dbid" name="std_qty_target_dbid" value="">
		</form:form>
	
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px; margin-top:10px;">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99.5%;">
				<table id="reportStatusTable" class="tbl easyui-datagrid" data-options="fit:true,border:false" >
				</table>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlDiagnosisReportPopup.jsp" %> <!-- FILTER SQL 팝업 -->

</body>
</html>