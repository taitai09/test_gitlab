<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.18	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일 예방 점검 - UNUSABLE INDEX</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/dailyCheck/${dailyCheck.check_item_name}.js?ver=<%=today%>"></script>   
	<style>
		.datagrid-body .datagrid-btable td{cursor:default;}	
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		
		<input type="hidden" name="check_pref_id" id="check_pref_id" value="1029"/>
		<input type="hidden" name="check_except_object" id="check_except_object"/>
		
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<input type="hidden" id="dbid" name="dbid" value="${dailyCheck.dbid}"/>
					<input type="hidden" id="check_day" name="check_day" value="${dailyCheck.check_day}"/>
					<input type="hidden" id="check_seq" name="check_seq" value="${dailyCheck.check_seq}"/>
					<input type="hidden" id="check_item_name" name="check_item_name" value="${dailyCheck.check_item_name}"/>
					
					<label>DB</label>
					<select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" required="true" class="w110 easyui-combobox"></select>
					<label>점검일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${dailyCheck.strStartDt}" required="true" class="w130 datapicker easyui-datebox"/>
					<label>점검 회차</label>
					<select id="selectCheckSeq" name="selectCheckSeq" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox"></select>					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:550px">
				<div data-options="region:'north',split:false,border:false" style="height:40px;">
					<div style="float:right;">
						<a href="javascript:;" id="registExceptionBtn" class="w100 easyui-linkbutton">예외등록</a>
						<a href="javascript:;" id="allCopyBtn" class="w130 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#allScript"><i class="btnIcon fas fa-copy fa-lg fa-fw"></i> SCRIPT 전체 복사</a>
						<textarea name="allScript" id="allScript" style="display:none;"></textarea>
					</div>
				</div>
				<div data-options="region:'center',border:false">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</form:form>			
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>