<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.20	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>인덱스 자동설계현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesignAdviser/autoIndexStatus.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<div id="contents">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="db_name" name="db_name"/>
						<input type="hidden" id="dbid" name="dbid" value="${idxAdMst.dbid}"/>
						<input type="hidden" id="idx_ad_no" name="idx_ad_no"/>
						<!-- <input type="hidden" id="access_path_type" name="access_path_type"/> -->
						<input type="hidden" id="table_owner" name="table_owner"/>
						<!-- 이전, 다음 처리 -->
						<input type="hidden" id="currentPage" name="currentPage" value="${idxAdMst.currentPage}"/>
						<input type="hidden" id="pagePerCount" name="pagePerCount" value="${idxAdMst.pagePerCount}"/>
						
						<label>DB</label>
						<select id="selectDbid" name="selectDbid" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
						<label>자동설계일시</label>
						<span id="span_start_dt">
							<input type="text" id="start_dt" name="start_dt" value="${idxAdMst.start_dt}" class="w100 datapicker easyui-datebox" required="true"/> ~
							<input type="text" id="end_dt" name="end_dt" value="${idxAdMst.end_dt}" class="w100 datapicker easyui-datebox" required="true"/>
						</span>
						<label>소스구분</label>
						<select id="access_path_type" name="access_path_type" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="ALL" selected> 전체 </option>
							<option value="VSQL"> 수집SQL </option>
							<option value="DBIO"> 적재SQL </option>
						</select>
						<label>진행상태</label>
						<select id="status" name="status" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="A" selected> 전체 </option>
							<option value="C"> COMPLETE </option>
							<option value="E"> EXECUTING </option>
						</select>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>						
				</div>
				<div id="forceComplete" style="padding-top:8px;">
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w160 easyui-linkbutton" onClick="Btn_OnForceComplete();"><i class="btnIcon fas fa-chevron-circle-down fa-lg fa-fw"></i> 인덱스 자동설계 강제완료처리</a>
					</span>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>			
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>			
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>