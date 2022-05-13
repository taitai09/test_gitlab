<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.10.31	명성태	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>DB 종합 진단 보고서</title>
	<meta charset="utf-8" />
	<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/lib/chartjs/Chart.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/chartjs/chartjs-plugin-labels.min.js"></script>
<!-- 	<script type="text/javascript" src="/resources/js/lib/es6-promise.auto.js"></script> -->
	<script type="text/javascript" src="/resources/js/lib/FileSaver.min.js"></script>
<!-- 	<script type = "text/javascript" src = "https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js"></script> 인터라넷에서 속도 문제로 임시적으로 주석 처리합니다.-->
<!-- 	<script type = "text/javascript" src = "https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.4.1/jspdf.min.js"></script> -->
<!-- 	<script type="text/javascript" src="/resources/js/lib/html2pdf.bundle.min.js"></script> -->
<!-- 	<script type="text/javascript" src="/resources/js/lib/html2pdf.bundle.js"></script> -->
<!-- 	<script src="https://superal.github.io/canvas2image/canvas2image.js"></script> 인터라넷에서 속도 문제로 임시적으로 주석 처리합니다. -->
<!-- 	<script type="text/javascript" src = "https://html2canvas.hertzen.com/dist/html2canvas.min.js"></script> 인터라넷에서 속도 문제로 임시적으로 주석 처리합니다. -->
	<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReport.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="strEndDt" name="strEndDt"/>
		<input type="hidden" id="exadata_yn" name="exadata_yn"/>
				
		<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
		<input type="hidden" id="base_period_value" name="base_period_value" value=1 />
		<input type="hidden" id="contents_id" name="contents_id" />
			
		<!-- contents START -->
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w120 easyui-combobox" required="true"></select>
						<span style="margin-left:20px;"/>
						<label>기준일자</label>
						<input type="text" id="nowDate" name="nowDate" value="${nowDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" style="margin-left:10px;"/>
						<span style="margin-left:5px;"/>
						<input class="easyui-radiobutton" id="base_weekly" name="base_weekly" value="1" checked label="주간 진단" labelPosition="after" labelWidth="55px;">
						<input class="easyui-radiobutton" id="base_monthly" name="base_monthly" value="2" label="월간 진단" labelPosition="after" labelWidth="55px;">
						<span style="margin-left:20px;"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 진단 실행</a>
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickPDF();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> PDF</a> -->
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickPDF2();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> PDF2</a> -->
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickPDF3();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> PDF3</a> -->
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickPDF4();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> PDF4</a> -->
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickPDF5();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> PDF5</a> -->
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="exportHTML();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> exportHTML</a> -->
						</span>
						<span class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnDownload();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 다운로드</a>
						</span>
					</div>
				</div>
			</div>
<!-- 			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:660px"> -->
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:680px;">
				<div data-options="region:'center',border:false">
<!-- 					<div id="reportView" style="display:inline-block;width:100%;height:100%;"> -->
					<div id="reportView" style="display:inline-block;"></div>
				</div>
			</div>
		</div>
		<!-- contents END -->
	</form:form>
</div>
<!-- container END -->
</body>
</html>