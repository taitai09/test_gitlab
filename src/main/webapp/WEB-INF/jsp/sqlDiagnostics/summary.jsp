<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 진단</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript">
		var fieldName = []; 
		var fieldRealName = [];
		<c:forEach items="${tableTitleList}" var="result" varStatus="status">
			fieldName.push('${result.cd_desc}');
			fieldRealName.push('${result.cd_nm}');
		</c:forEach>	
	</script>
    <script type="text/javascript" src="/resources/js/ui/sqlDiagnostics/summary.js?ver=<%=today%>"></script>   
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${sqlDiagSummary.dbid}"/>
		<input type="hidden" id="day_gubun" name="day_gubun" value="D"/>
		<input type="hidden" id="gather_day" name="gather_day" value="${sqlDiagSummary.gather_day}"/>
		<input type="hidden" id="uuid" name="uuid" value="${uuid}"/>

		<input type="hidden" id="colspanCnt" name="colspanCnt" value="${tableTitleList.size()+1}"/>

		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>기준일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:670px;">
				<div data-options="region:'north',split:false,border:false" style="height:50%;">
					<div id="summeryTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:100%;">
						<div id="dayChart" title="Day" style="padding:5px;">
						</div>
						<div id="weekChart" title="Week" style="padding:5px">
						</div>
						<div id="monthChart" title="Month" style="padding:5px">
						</div>
					</div>
				</div>
				<div data-options="region:'center',border:false" class="gridTable" style="padding:5px 1px 0px;">
					<table id="summeryTbl" class="detailT" style="">
						<thead >
							<tr style="">
								<th>Day/Week</th>
								<c:forEach items="${tableTitleList}" var="result" varStatus="status">
									<th style="">${fn:replace(result.cd_nm, '(', '<br/>(')}</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody style="">
							<tr>
								<td colspan="${tableTitleList.size()+1}" class="ctext">검색 조건을 선택 후 "검색"을 해주세요.</td>
							</tr>
						</tbody>
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