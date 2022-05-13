<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.12	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>FULL SCAN</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlDiagnostics/fullScan.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
    
</head>
<body style="visibility:hidden;">
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
					<input type="hidden" id="dbid" name="dbid" value="${fullscanSql.dbid}"/>
					<input type="hidden" id="sql_id" name="sql_id"/>
					<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
					<input type="hidden" id="choice_id" name="choice_id" value="4"/>
					<input type="hidden" id="tableName" name="tableName" value="FULLSCAN_SQL"/>
					<input type="hidden" id="exception_list" name="exception_list" value="${exception_list}"/>
					
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>기준일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${fullscanSql.strStartDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${fullscanSql.strEndDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
						<span style="margin-left:10px;"><a href="javascript:;"  onClick="Btn_ExceptionList();">※ 제외대상 리스트</a></span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:650px">
			<div id="sqlTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
				<div title="FULLSCAN SQL" style="padding:5px;">
					<div class="searchBtn innerBtn"><a href="javascript:;" class="w80 easyui-linkbutton" onClick="showTuningReqPopup();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a></div>
					<table id="tableList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:93%">
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/tunerAssign_popup.jsp" %> <!-- 튜닝담당자 지정 팝업 -->

</body>
</html>