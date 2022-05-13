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
 * 2020.06.29	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 표준 품질검토 작업 </title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/include/popup/pop_project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/design/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandards/qualityReviewWork.js?ver=<%=today%>"></script>
	<!-- 성능개선 - 인덱스 설계 전처리 - SQL 적재 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadSQLProjectUnit_popup.js?ver=<%=today%>"></script> 
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
<!-- 		<input type="hidden" id="project_id" name="project_id"/> -->
		
		<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
		
		<div id="contents">
			<div class="easyui-panel searchArea100" data-options="border:false" style="width:100%">
				<div class="well">
					<label>프로젝트</label>
					<select id="project_id" name="project_id" required="true" class="w350 easyui-combobox" ></select>
					<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="SQL 수 : " readonly/>
					<input type="text" class="perf_check_result_green perf_check_result_common" size="20" value="작업상태 : " readonly/>
					<span id="error_message_span" style="visibility:hidden">
						<input type="text" id="error_message" name="error_message" data-options="multiline:true" class="w620 easyui-textbox" style="height:23px;" readonly/>
					</span>
					
					<div class="searchBtn innerBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						<a href="javascript:;" class="w90 easyui-linkbutton" onClick="showLoadSqlProjectUnitPop();"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> SQL 적재</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
				<div style="padding: 5px;">
					<div class="searchBtn innerBtn">
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_Work();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 품질검토작업</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ForceProcessingCompleted();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:560px">
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;height:100%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/loadSQLProjectUnit_popup.jsp" %> <!-- 성능개선 - 인덱스 설계 전처리 - SQL 적재 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/design/project_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/pop_project_popup.jsp" %>
</body>
</html>