<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.12.23	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>Agent 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/examineOpenPOP/examineAgent1.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-layout" data-options="border:false" style="width:60%;height:670px;">
			<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:36px;padding:5px;">
				<label id="managementTime" style="vertical-align:-webkit-baseline-middle;font-weight:bolder;">점검 시간</label>
				<span style="bottom:5px;float:right;">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_Reflash();"><i class="btnIcon fas fa-dolly fa-lg fa-fw"></i> 점검 수행</a>
				</span>	
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:false" style="height:100%;padding:0px;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px;">
					<div title="Open-POP 서버" data-options="region:'west',split:false,collapsible:false,border:true" style="width:30%;padding:5px;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
							<form:form method="post" id="workjob_form" name="workjob_form" class="form-inline">
								<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
								<input type="hidden" id="user_id" name="user_id" />
								<input type="hidden" id="workjob_start_day" name="workjob_start_day" />
							</form:form>
							<div class="easyui-panel" data-options="border:false" style="height:90%;margin-bottom:10px;">
								<table id="agentList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
						</div>
					</div>
					<div title="관리대상 DB" data-options="region:'center',border:true" style="width:70%;padding:5px;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:500px;">
							<form:form method="post" id="workjob_form" name="workjob_form" class="form-inline">
								<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
								<input type="hidden" id="user_id" name="user_id" />
								<input type="hidden" id="workjob_start_day" name="workjob_start_day" />
							</form:form>
							<div class="easyui-panel" data-options="border:false" style="height:90%;margin-bottom:10px;">
								<table id="instanceList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>