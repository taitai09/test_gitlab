<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검 :: SQL 표준 점검 결과</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<style>
		.execSqlStdChkResult .searchOption a,
		.execSqlStdChkResult .refreshOption a {
			margin-left: 10px;
		}
		.execSqlStdChkResult .selectedRow {
			background: #e7e7e7;
			color: #165dbb;
		}
		.execSqlStdChkResult .bottom_table .searchBtn {
			width: 100%;
			height: 100%;
			padding-top: 6px;
			text-align: right;
		}
		.execSqlStdChkResult .btnImg {
			width: 18px;
			height: 18px;
			vertical-align: middle;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="execSqlStdChkResult">
		<form:form method="post" id="submit_form_search" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			<input type="hidden" name="sql_std_qty_div_cd" value="4">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_id" name="project_id" data-options="editable:false" class="w350 easyui-combobox"></select>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
			</div>
		</form:form>
		<form:form method="post" id="forceComplete_form" name="forceComplete_form" class="form-inline">
			<input type="hidden" id="job_scheduler_nm" name="job_scheduler_nm" value="">
			<input type="hidden" id="sql_std_qty_scheduler_no" name="sql_std_qty_scheduler_no" value="">
			<input type="hidden" id="sql_std_qty_chkt_id" name="sql_std_qty_chkt_id" value="">
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			<input type="hidden" name="sql_std_qty_div_cd" value="4">
		</form:form>
		
		<div>
			<div class="multi refreshOption" style="margin:10px 0px; " align="right">
				<label>Refresh</label>
				<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton">
				<input type="hidden" id="refresh" name="refresh" value="N"/>
				<input type="number" id=timer_value name="timer_value" value="10" class="w40 easyui-numberbox" data-options="min:3"> 초
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ForcedCompletion();"><i class="btnIcon btnForcedCompletion fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:230px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableUpperList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
		
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="1">
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="">
			
			<input type="hidden" id="project_id_lowList" name="project_id" value="">
			<input type="hidden" id="scheduler_no_lowList" name="sql_std_qty_scheduler_no" value="">
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			<input type="hidden" name="sql_std_qty_div_cd" value="4">
			<input type="hidden" id="sql_std_gather_day" name="sql_std_gather_day" value="">
			<input type="hidden" name="qty_chk_idt_cd" value="000">
			<input type="hidden" name="sql_id" value="0">
			
			<div>
				<div class="multi" style="margin:10px 0px; " align="right">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
				</div>
				<div class="easyui-layout bottom_table" data-options="border:false" style="width:100%;min-height:280px">
					<div data-options="region:'west',split:false,collapsible:true,border:false" style="width:19.8%;height:100%;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
							<div data-options="region:'north',split:false,collapsible:true,border:false" style="width:100%;height:88.5%;">
								<table id="tableLeftList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div data-options="region:'south',split:false,collapsible:true,border:false" style="width:100%;height:11.5%;overflow:hidden">
								<div class="searchBtn">
									<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
									<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
								</div>
							</div>
						</div>
					</div>
					
					<div data-options="region:'east',split:false,collapsible:true,border:false" style="width:79.8%;height:99%;">
						<table id="tableLowerList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</div>
		</form:form>
	</div>	<!-- contents END -->
</div>	<!-- container END -->
<!-- HTML 해석 및 파싱 완료 후 JS 실행되도록 script 위치 수정 -->
<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/execSqlStdChkCommon.js?ver=<%=today%>"></script>
<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/sqlStandardCheckResult.js?ver=<%=today%>"></script>
<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</body>
</html>