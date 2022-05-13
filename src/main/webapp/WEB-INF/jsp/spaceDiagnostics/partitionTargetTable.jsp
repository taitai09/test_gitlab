<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.04	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>파티셔닝 대상 진단</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/spaceDiagnostics/partitionTargetTable.js?ver=<%=today%>"></script>
	<style>
		.datagrid-view2:eq(1) td{cursor:default;}
		.datagrid-view2:eq(2) td{cursor:default;}
	</style>
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
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="owner" name="owner"/>
					<input type="hidden" id="table_name" name="table_name"/>

					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
			<div data-options="title:'▶ Partition Recommendation',region:'north',split:false,collapsible:false,border:false" style="height:330px;padding:5px 0px;">
				<table id="partitionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="title:'▶ Access Path',region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:10px 5px 0px 0px;">
				<table id="accessPathList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="title:'▶ Partition Key Recommendation',region:'center',split:false,collapsible:false,border:false" style="padding:10px 0px 0px 5px;cursor:default;">
<!-- 				<div style="float:right;margin-right:10px;margin-bottom:5px;"><span id="rowCount" class="h3" style="margin-left:0px;"><b>총 0 건</b></span></div> -->
				<table id="partitionKeyList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>			
			</div>			
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>