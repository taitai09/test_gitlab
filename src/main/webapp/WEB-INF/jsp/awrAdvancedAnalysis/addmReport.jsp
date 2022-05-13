<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.06	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>ADDM 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/lib/jquery.mb.browser.js"></script>
    <script type="text/javascript" src="/resources/js/lib/jquery.PrintArea.js_4.js"></script>
    <script type="text/javascript" src="/resources/js/lib/jquery.printElement.js"></script>
    <script type="text/javascript" src="/resources/js/ui/awrAdvancedAnalysis/addmReport.js?ver=<%=today%>"></script>
 	<!-- 성능개선 - 배포전점검 - 성능점검 엑셀 업로드 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/advancedAnalysis_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/snapId_popup.js?ver=<%=today%>"></script> <!-- snap id 조회 팝업 -->
    
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="inst_id" name="inst_id"/>
					<input type="hidden" id="snap_id" name="snap_id"/>
					<input type="hidden" id="gubun" name="gubun" value="text"/>

					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w120 easyui-combobox" required="true"></select>
					<label>Instance</label>
					<select id="selectInstance" name="selectInstance" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox" required="true"></select>
					<label>SNAP ID</label>
					<input type="text" id="start_snap_id1" name="start_snap_id1" data-options="readonly:'true'" class="w80 easyui-textbox"/> ~
					<input type="text" id="end_snap_id1" name="end_snap_id1" data-options="readonly:'true'" class="w80 easyui-textbox"/>
					&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" onClick="fnShowSnapPopup('report1');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></a>
					&nbsp;&nbsp;<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showBaseLinePopup('1');"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 베이스라인 선택</a>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> Generate</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;margin-bottom:10px;">
			<div style="float:right;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveClick();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_PrintClick();"><i class="btnIcon fas fa-print fa-lg fa-fw"></i> 프린트</a>
			</div>
		</div>
		<div id="dataArea" class="easyui-panel" data-options="border:true" style="width:100%;padding:10px;height:650px;font-family:'굴림체';">
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/advancedAnalysis_popup.jsp" %> <!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/snapId_popup.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>