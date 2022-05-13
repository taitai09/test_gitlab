<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.10.27	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능검증 :: DB간 SQL성능비교 :: 튜닝 실적</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/tuningPerformance.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
	/******** 3th TAB - 튜닝 실적 ********/
	.tuningPerf [field=perf_check_name], .tuningPerf [field=project_nm], .tuningPerf [field=sql_all_cnt], 
	.tuningPerf [field=elap_time_impr_ratio], .tuningPerf [field=buffer_impr_ratio] {
		cursor: default;
	}
	.non-click .datagrid td, .non-click .datagrid td div{
		cursor : default;
	}
	
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="sql_all_cnt" name="sql_all_cnt" value="N"/>
			<input type="hidden" id="plan_change_cnt" name="plan_change_cnt" value="N"/>
			<input type="hidden" id="tuning_selection_cnt" name="tuning_selection_cnt" value="N"/>
			<input type="hidden" id="elapsed_time_std_cnt" name="elapsed_time_std_cnt" value="N"/>
			<input type="hidden" id="buffer_std_cnt" name="buffer_std_cnt" value="N"/>
			<input type="hidden" id="tuning_end_cnt" name="tuning_end_cnt" value="N"/>
			<input type="hidden" id="tuning_cnt" name="tuning_cnt" value="N"/>
			<input type="hidden" id="project_id" name="project_id" value=""/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" value=""/>
			<input type="hidden" id="field" name="field" value=""/>
			<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="G"/>
			<input type="hidden" id="perf_check_type_cd" name="perf_check_type_cd" value="1"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="well">
				<table>
					<colgroup>
						<col style="width:5%">
						<col style="width:7%">
						<col style="width:5%">
						<col style="width:8%">
						<col style="width:5%">
						<col style="width:5%">
						<col style="width:6.5%">
						<col style="width:30%">
					</colgroup>
					
					<tr style="height:30px;">
						<td><label>프로젝트</label></td>
						<td colspan="2">
							<select id="project_id_cd" name="project_id_cd" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
						</td>
						<td style="text-align: right"><label>SQL점검팩</label></td>
						<td colspan="3">
							<select id="sqlAutoPerfCheckId" name="sqlAutoPerfCheckId" class="w340 easyui-combobox" data-options="editable:false"><option value="">전체</option></select>
						</td>
						<td>
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
						</td>
					</tr><tr style="height:5px;"></tr>
				</table>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout tuningPerf" data-options="border:false" style="width:100%;min-height:200px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableDefaultList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Detail_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout non-click" data-options="border:false" style="width:100%;min-height:280px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right:0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->

</body>
</html>