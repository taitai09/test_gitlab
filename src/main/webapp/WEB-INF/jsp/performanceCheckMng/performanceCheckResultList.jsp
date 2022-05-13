<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	String today = formatter.format(new java.util.Date());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui/performanceCheckMng/performanceCheckResultList.js?ver=<%=today%>"></script>        
</head>
<body>
		<div class="easyui-layout" fit="true" data-options="border:false" style="width:100%;min-height:310px">
			<div data-options="region:'center',border:false" style="padding-top:5px;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
</body>
</html>