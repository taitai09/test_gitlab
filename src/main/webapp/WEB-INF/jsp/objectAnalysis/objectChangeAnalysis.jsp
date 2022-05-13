<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.28	bks	OPENPOP V2 최초작업
 * 2020.07.06	이재우	화면수정
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>${menu_nm}</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
    <script type="text/javascript" src="/resources/js/ui/objectAnalysis/objectChangeAnalysis.js?ver=<%=today%>"></script>
    <style>
    .datagrid-head-over{ cursor: pointer }
    .datagrid-row-over{ cursor: pointer }
    </style>    
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${dbid}"/>
		<input type="hidden" id="base_day_gubun" name="base_day_gubun" value="${base_day_gubun}"/>
		<input type="hidden" id="object_change_type" name="object_change_type" value="${object_change_type}"/>

		<div id="contents" style="height: 90px;">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					
					<label>변경 기준</label>
					<select id="selectBaseDayGubunCombo" name="selectBaseDayGubunCombo" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="true">
						<option value=""></option>
						<option value="1">전일 변경</option>
						<option value="2">최근 1주일 변경</option>
						<option value="3">최근 1개월 변경</option>
					</select>
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="panelHeight:'auto',editable:false" class="w130 easyui-combobox" required="true"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
		</div>
		<div class="easyui-layout" data-options="region:'center',border:false" style="width:100%; height:300px;min-height:100px;">
<!-- 			<div class="easyui-panel" data-options="border:false" style="width:100%;height:50%;padding-left:5px;min-height:350px"> -->
<%-- 				<div id="objectChangeChart" title="DB 예방 점검 현황${check_day_txt}" style="padding-top:5px;"> --%>
<!-- 				</div>			 -->
<!-- 			</div> -->
			
				<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;min-height:300px">
					<div id="objectChangeChart" style="width:100%;height:100%"></div>
				</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:350px;min-height:200px;vertical-align:top;">
			<div data-options="region:'south',split:false,border:false" style="padding-top:10px;height:100%;">
				<div id="checkTab" class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div title="테이블 변경" style="padding-top:5px;">
						<table id="tableChangeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>					
					<div title="컬럼 변경" style="padding-top:5px;">
						<table id="columnChangeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>					
					<div title="인덱스 변경" style="padding-top:5px;">
						<table id="indexChangeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>			
				</div>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container START -->
</body>
</html>