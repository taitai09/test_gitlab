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
	<title>인덱스 검증 현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesignAdviser/indexRecommendStatus.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="idx_ad_no" name="idx_ad_no" value="${idxAdRecommendIndex.idx_ad_no}"/>
					<input type="hidden" id="dbid" name="dbid" value="${idxAdRecommendIndex.dbid}"/>

					<label>DB</label>
					<input type="text" id="db_name" name="db_name" data-options="disabled:true" class="w120 easyui-textbox" value="${idxAdRecommendIndex.db_name}"/>
					<label>스키마</label>
					<input type="text" id="table_owner" name="table_owner" data-options="disabled:true" class="w120 easyui-textbox" value="${idxAdRecommendIndex.table_owner}"/>
					<label>AccessPath 유형</label>
					<input type="text" id="access_path_type" name="access_path_type" data-options="disabled:true" class="w120 easyui-textbox" value="${idxAdRecommendIndex.access_path_type}"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">			
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
<!-- 		<div class="searchBtn innerBtn"> -->
<!-- 			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ReturnList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i>돌아가기</a> -->
<!-- 		</div>			 -->
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>