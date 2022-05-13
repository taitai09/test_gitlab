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
	<title>수집SQL 인덱스 자동설계</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesign/collectSQL/autoCollectionIndexDesign.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
    <style>
		.datagrid-header-check input{ display: none; }
		input[type="text"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 23px !important;
		    line-height: 23px !important;
		}
		input[type="checkbox"]{
			line-height:23px !important;
		    margin: 0px 0px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 20px !important;
		    line-height: 23px !important;
		}		
	</style>    
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="table_owner" name="table_owner"/>
					<input type="hidden" id="exec_seq" name="exec_seq"/>
					<input type="hidden" id="access_path_type" name="access_path_type" value="VSQL"/>
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${accPathExec.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${accPathExec.pagePerCount}"/>
					<div class="well">
							<label>DB</label>
	<!-- 						<select id="selectDbid" name="selectDbid" data-options="editable:false,required:true" class="w130 easyui-combobox"></select> -->
							<select id="selectCombo" name="selectCombo" data-options="editable:false,required:true" class="w130 easyui-combobox"></select>
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
	<!-- 				</div> -->
	<!-- 				<div style="height:1px"></div> -->
	<!-- 				<div class="well"> -->
	<!-- 						<label>DB</label> -->
	<!-- 						<select id="selectCombo" name="selectCombo" data-options="editable:false,required:true" class="w130 easyui-combobox"></select> -->
					</div>
					<div id="blockAutoCollectionIndex" style="padding-top:5px;">
						<label>OWNER</label>
						<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
						<label style="margin-left:30px;">Selectivity 계산 : </label>
						
						<span style="padding-right:2px;padding-left:5px;"><input class="easyui-radiobutton" name="selectivity_calc_method" value="STAT" checked> Statistics 기반 </span> 
						<span>
							<i id="selectivity_statistics_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
						</span>
					    <span style="padding-right:2px;padding-left:10px;"><input class="easyui-radiobutton" name="selectivity_calc_method" value="DATA"> Data Sampling 기반</span>
						<span>
							<i id="selectivity_data_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
						</span>
						<span class="marginL20 searchBtnLeft">
							<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_IndexAutoDesign();"><i class="btnIcon fab fa-modx fa-lg fa-fw"></i> 인덱스 자동설계</a>
						</span>
					</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:530px">
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