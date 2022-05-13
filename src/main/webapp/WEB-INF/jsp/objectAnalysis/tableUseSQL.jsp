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
	<title>성능분석 :: 오브젝트 분석 :: 테이블 사용 SQL 내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/objectAnalysis/tableUseSQL.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="owner" name="owner"/>

					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w130 easyui-combobox"></select>
					<label>OWNER</label>
					<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
					<label>TABLE NAME</label>
					<input type="text" id="table_name" name="table_name" class="w130 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:650px">
			<div id="tableUseTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
				<div title="SQL 현황" style="padding:5px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>