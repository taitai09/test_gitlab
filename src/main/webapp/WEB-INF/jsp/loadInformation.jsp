<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.10	이원식	OPENPOP V2 최초작업 (SQL 정보 탭구성 호출)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>적재SQL :: SQL 정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/loadInformation.js?ver=<%=today%>"></script>   
</head>
<body>
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="file_no" name="file_no" value="${dbioLoadSql.file_no}"/>
		<input type="hidden" id="explain_exec_seq" name="explain_exec_seq" value="${dbioLoadSql.explain_exec_seq}"/>
		<input type="hidden" id="query_seq" name="query_seq" value="${dbioLoadSql.query_seq}"/>
		<div class="easyui-layout" style="width:100%;height:550px">
			<div id="sqlTextDiv" data-options="title:'SQL TEXT',region:'center',border:false" style="padding:10px;">
				<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:98%;height:80%" wrap="off" readonly></textarea>
				<div class="searchBtn" style="margin-top:10px;">
					<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
				</div>		
			</div>
			<div data-options="title:'실행 계획',region:'south',split:true,border:false,hideCollapsedContent:false" style="height:40%">
				<div id="executePlanDiv" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
					<div title="Grid Plan" style="padding:5px">
						<table id="popGraphicList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Text Plan" style="padding:10px">
						<ul id="textPlan" style="font-size:13px;"></ul>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>