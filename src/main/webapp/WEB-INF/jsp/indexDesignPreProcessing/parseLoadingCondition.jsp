<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>적재SQL 조건절 파싱</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesignPreProcessing/parseLoadingCondition.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="file_no" name="file_no"/>
					<input type="hidden" id="explain_exec_seq" name="explain_exec_seq"/>
					<input type="hidden" id="exec_seq" name="exec_seq"/>

					<label>DB</label>
					<select id="dbid" name="dbid" data-options="editable:false" class="w130 easyui-combobox" required="true">							
					</select>
					<label>파일번호</label>
					<select id="selectFileNo" name="selectFileNo" data-options="editable:false" class="w130 easyui-combobox" required="true">							
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w150 easyui-linkbutton" onClick="insertParseLoadingCondition();"><i class="btnIcon fab fa-quinscape fa-lg fa-fw"></i> 적재SQL 조건절 파싱</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:390px">					
			<table id="explainList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>			
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:240px;margin-top:20px;">			
			<table id="accesspathList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>