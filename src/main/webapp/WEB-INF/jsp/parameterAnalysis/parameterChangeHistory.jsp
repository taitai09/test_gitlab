<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 * 2018.05.14	이원식	파라미터 변경 이력으로 명칭 변경
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>파라미터 변경 이력</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/parameterAnalysis/parameterChangeHistory.js?ver=<%=today%>"></script>
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

					<input type="hidden" id="dbid" name="dbid" value="${odsHistParameter.dbid}"/>
					<input type="hidden" id="db_name" name="db_name" value="${odsHistParameter.db_name}"/>
					<input type="hidden" id="inst_id" name="inst_id" value="${odsHistParameter.inst_id}"/>
					<input type="hidden" id="parameter_name" name="parameter_name" value="${odsHistParameter.parameter_name}"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
					<label>Instance</label>
					<select id="selectInstance" name="selectInstance" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="true"></select>
					<label>파라미터명</label>
					<input id="parameterName" name="parameterName" data-options="panelHeight:'auto'" class="w150 easyui-textbox">
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:645px">
			<table id="tableList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container START -->
</body>
</html>