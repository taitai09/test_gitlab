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
	<title>일자별 DML 변경 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/spaceAnalysis/dmlChangeDaily.js?ver=<%=today%>"></script>
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
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
					<label>OWNER</label>
					<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
					<label>테이블명</label>
					<select id="selectTableName" name="selectTableName"  data-options="editable:true,panelHeight:500" class="w240 easyui-combobox"></select>
					<label>기준일자</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:550px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
			<!--페이징기능 시작-->
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
			<!--페이징기능 끝-->
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>