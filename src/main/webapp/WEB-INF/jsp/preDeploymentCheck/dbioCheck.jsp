<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL식별자(DBIO) 성능점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/preDeploymentCheck/dbioCheck.js?ver=<%=today%>"></script>
 	<!-- 성능개선 - 배포전점검 - 성능점검 엑셀 업로드 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/excelUpload_popup.js?ver=<%=today%>"></script>
    
    <style>
		.datagrid-header-check input{ display: none; }
	</style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${deployPerfCheck.wrkjob_cd}"/>
					<input type="hidden" id="perf_check_yn" name="perf_check_yn" value="${deployPerfCheck.perf_check_yn}"/>
					<input type="hidden" id="deploy_perf_check_no" name="deploy_perf_check_no"/>

					<label>등록일시 </label>
					<input type="text" id="strStartDt" name="strStartDt" value="${deployPerfCheck.strStartDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required="true" /> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${deployPerfCheck.strEndDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required="true" />
					<label>업무명 </label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w130 easyui-combobox"></select>
					<label>성능점검완료</label>
					<input type="checkbox" id="chkPerfCheck" name="chkPerfCheck" class="easyui-switchbutton"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_ExcelUploadPopup('2');"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> 성능점검등록</a>								
					</div>
				</form:form>
			</div>
		</div>
		<div class="searchBtn innerBtn">
			<a href="javascript:;" class="w110 easyui-linkbutton" onClick="requestPerformanceCheck();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 성능점검요청</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">			
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/excelUpload_popup.jsp" %> <!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 -->
</body>
</html>