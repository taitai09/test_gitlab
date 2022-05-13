<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
	<title>성능시험 :: 자동 인덱싱 SQL 성능 검증 :: 인덱스별 성능 영향도 분석 결과</title>
	<meta charset="UTF-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/perfImpactByIndex.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/autoISQLPVCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/planCompare_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script type="text/javascript" src="/resources/js/paging2.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		.combobox-item{
			line-height: 11px;
			font-weight: 300;
			-webkit-font-smoothing: antialiased;
		}
		.perfImpactByIndex .searchOption{
			margin-bottom: 10px;
		}
		.perfImpactByIndex .searchOption a{
			margin-left: 5px;
		}
		.perfImpactByIndex .searchBtn {
			width: 100%;
			height: 100%;
			padding-top: 6px;
			text-align: right;
		}
		.perfImpactByIndex .btnImg {
			width: 18px;
			height: 18px;
			vertical-align: middle;
		}
		.perfImpactByIndex .Btn_Download {
			float: right;
		}
		.perfImpactByIndex #summaryArea table{
			width:100%;
			margin-left: 0px;
			margin-bottom: 10px;
			text-align:center;
		}
		.perfImpactByIndex #summaryArea table tr td .imgP{
			display: inline-block;
			vertical-align: middle;
			text-indent: -10px;
		}
		.perfImpactByIndex #summaryArea table tr td .imgP img{
			width: 24px;
			height: 40px;
		}
		.perfImpactByIndex #summaryArea table tr th{
			background-color: #fafafa;
		}
		.perfImpactByIndex #summaryArea table tr th,
		.perfImpactByIndex #summaryArea table tr td{
			width: 6.25%;
			height: 25px;
			border: 1px solid #cbcbcb;
		}
		.perfImpactByIndex #summaryArea table .paddingT{
			padding-top: 3px;
			font-weight: bold;
		}
		.perfImpactByIndex td[field=buffer_increase_ratio],
		.perfImpactByIndex td[field=elapsed_time_increase_ratio] {
			position: relative;
		}
		.perfImpactByIndex .ratioIcon {
			width: 23px;
			height: 36px;
			position: absolute;
			top: -6px;
			left: 0px;
		}
		.perfImpactByIndex .expandIcon p,
		.perfImpactByIndex .compressIcon p{
			width: 16px;
			height: 24px;
			background: none center no-repeat;
			background-size: 16px;
		}
		.perfImpactByIndex .expandIcon p{
			background-image: url("../resources/images/expand_arrows.png");
		}
		.perfImpactByIndex .compressIcon p{
			background-image: url("../resources/images/compress_arrows.png");
		}
		#loadExplainPlanPop .tabTxt {
			overflow: hidden;
		}
		#loadExplainPlanPop #textArea {
			width: 98.7% !important;
		}
		#loadExplainPlanPop .datagrid-header {
			height: 36px !important;
		}
		#loadExplainPlanPop .datagrid-row-over,
		#loadExplainPlanPop .datagrid-row-checked,
		#loadExplainPlanPop .datagrid-row-selected {
			cursor: default;
			color: #000;
			background-color: inherit;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="perfImpactByIndex">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
			<input type="hidden" id="currentPage" name="currentPage" value="1">
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20">
			<input type="hidden" id="currentPage2" value="1">
			<input type="hidden" id="rcount" value="20">
			<input type="hidden" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" name="perf_check_type_cd" value="4">
			<input type="hidden" id="tobe_dbid" name="tobe_dbid" value="">
			<input type="hidden" id="idx_ad_no" name="idx_ad_no" value="">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_combo" data-options="editable:false,prompt:'선택'" class="w300 easyui-combobox" required></select>
				<input type="hidden" id="project_id" name="project_id" value="${project_id}">
				
				<label>SQL점검팩</label>
				<select id="sqlPack_combo" data-options="editable:false,prompt:'선택'" class="w300 easyui-combobox" required></select>
				<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" value="${sql_auto_perf_check_id}">
				
				<label>OWNER</label>
				<input id="owner_textbox" class="w150 easyui-textbox" value="${table_owner}">
				<input type="hidden" id="table_owner" name="table_owner">
				
				<label>테이블명</label>
				<input id="table_name_textbox" class="w250 easyui-textbox" value="${table_name}">
				<input type="hidden" id="table_name" name="table_name">
				
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				<a href="javascript:;" class="w120 easyui-linkbutton Btn_Download" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			
			<div id="summaryArea">
				<table>
					<tr>
						<th colspan="2">추천 인덱스</th>
						<th rowspan="2">Distinct SQL 수</th>
						<th rowspan="2">테이블 스페이스<br>증가 (GB)</th>
						<th colspan="3">추천 인덱스 Elapsed Time 개선율</th>
						<th colspan="3">추천 인덱스 Buffer Gets 개선율</th>
						<th colspan="3">전체 Elapsed Time 개선율</th>
						<th colspan="3">전체 Buffer Gets 개선율</th>
					</tr>
					<tr>
						<th>ADD</th>
						<th>MODIFY</th>
						<th>ASIS</th>
						<th>TOBE</th>
						<td rowspan="2" class="paddingT"></td>
						<th>ASIS</th>
						<th>TOBE</th>
						<td rowspan="2" class="paddingT"></td>
						<th>ASIS</th>
						<th>TOBE</th>
						<td rowspan="2" class="paddingT"></td>
						<th>ASIS</th>
						<th>TOBE</th>
						<td rowspan="2" class="paddingT"></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</div>
			
			<div style="width:100%;height:517px;display:flex;">
				<div id="layoutLeft" style="width:39.9%;height:100%;margin-right:auto;">
					<div id="layoutInnerLeft" class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
						<div region='north' data-options="border:false" style="width:100%;height:93.5%;">
							<table id="tableLeftList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
						<div region='south' data-options="split:false,border:false" style="width:100%;height:6.5%;overflow:hidden">
							<div class="searchBtn">
								<a href="javascript:;" class="w20 easyui-linkbutton expandIcon" data-expanding="true" onclick="sizeAdjuster(this,'Left');"><p></p></a>
								<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
								<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
					</div>
				</div>
				
				<div id="layoutRight" style="width:59.9%;height:100%;">
					<div id="layoutInnerRight" class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
						<div region='north' data-options="border:false" style="width:100%;height:93.5%;">
							<table id="tableRightList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
						<div region='south' data-options="split:false, border:false" style="width:100%;height:6.5%;overflow:hidden">
							<div class="searchBtn">
								<a href="javascript:;" class="w20 easyui-linkbutton expandIcon" data-expanding="true" onclick="sizeAdjuster(this,'Right');"><p></p></a>
								<a href="javascript:;" id="prevBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
								<a href="javascript:;" id="nextBtn2" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
					</div>
				</div>
			</form:form>
			
			<div id="formArea">
				<form:form method="post" id="submit_form_excel" name="submit_form" class="form-inline">
					<input type="hidden" name="project_id" value="">
					<input type="hidden" name="sql_auto_perf_check_id" value="">
					<input type="hidden" name="table_owner" value="">
					<input type="hidden" name="table_name" value="">
					<input type="hidden" name="index_name" value="">
					<input type="hidden" name="perf_check_type_cd" value="4">
					<input type="hidden" name="database_kinds_cd" value="${database_kinds_cd}">
					<input type="hidden" id="currentPage_excel" name="currentPage" value="1">
					<input type="hidden" id="pagePerCount_excel" name="pagePerCount" value="20">
				</form:form>
			</div>
		</div>
	</div>		<!-- contents END -->
</div>	<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_operation_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/planCompare_popup.jsp" %>
</body>
</html>