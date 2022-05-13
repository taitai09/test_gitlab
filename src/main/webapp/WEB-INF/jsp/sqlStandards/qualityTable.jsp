<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.14	반광수	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 품질검토 작업/집계표 </title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandards/qualityTable.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	
	<script type="text/javascript">
		var search_wrkjob_cd = "${search_wrkjob_cd}";
		var search_wrkjob_cd_nm = "${search_wrkjob_cd_nm}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="db_name" name="db_name"/>
		<input type="hidden" id="inst_id" name="inst_id"/>
		<input type="hidden" id="wrkjob_cd" name="wrkjob_cd"/>
		<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/>
		<input type="hidden" id="hard_work_type" name="hard_work_type"/>
		<input type="hidden" id="project_id" name="project_id"/>
		
		<!-- 이전, 다음 처리 -->
		<input type="hidden" id="currentPage" name="currentPage" value="${sqlStandards.currentPage}"/>
		<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlStandards.pagePerCount}"/>
		<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
		
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<label>프로젝트</label>
					<input type="text" id="project_nm" name="project_nm" value="" class="w350 easyui-textbox"/>
					<label>업무</label>
					<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree"  value="${sqlStandards.wrkjob_cd}"></select>
					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn innerBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:620px">
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;height:100%;padding:10px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
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
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>