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
	<title>Literal SQL :: PLAN HASH VALUE 기반</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlDiagnostics/literalSQL/planHashValue.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					
					<input type="hidden" id="dbid" name="dbid" value="${literalSql.dbid}"/>
					<input type="hidden" id="gather_day" name="gather_day" value="${literalSql.gather_day}"/>
					<input type="hidden" id="literal_seq" name="literal_seq" value="${literalSql.literal_seq}"/>
					<input type="hidden" id="literal_type_cd" name="literal_type_cd" value="${literalSql.literal_type_cd}"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>기준일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${literalSql.strStartDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${literalSql.strEndDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${literalSql.currentPage}"/>
					<%-- <input type="hidden" id="pagePerCount" name="pagePerCount" value="${literalSql.pagePerCount}"/> --%>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>

					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:220px">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
		</div>
			<div class="easyui-panel" data-options="border:false" style="margin-top:20px;width:100%;min-height:380px;">
				<div id="sqlTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
					<div title="SQL현황" style="padding:5px">						
						<table id="bottomList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>	
			</div>
			<div class="searchBtn" id="paging_botton" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>