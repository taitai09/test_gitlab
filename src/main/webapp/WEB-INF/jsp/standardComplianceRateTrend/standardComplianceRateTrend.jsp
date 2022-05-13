<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.11	명성태	OPENPOP V2 최초작업
 * 2020.07.01	이재우	기능개선
 * 2021.09.29	황예지	검색조건 추가
 * 2022.01.05	이재우	스케줄러 콤보박스 추가
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 표준 준수 현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/dblPopup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/standardComplianceRateTrend/standardComplianceRateTrend.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="db_name" name="db_name"/>
			<input type="hidden" id="strStartDt" name="strStartDt"/>
			<input type="hidden" id="strEndDt" name="strEndDt"/>
			<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/>
			<input type="hidden" id="parameter_list" name="parameter_list"/>
			<input type="hidden" id="wrkjob_cd_nm" name="wrkjob_cd_nm"/>
			
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<div>
						<label>프로젝트</label>
						<select id="project_id" name="project_id" required="true" class="w350 easyui-combobox" data-options="panelHeight:'auto',editable:false,prompt:'선택'" required></select>
						
						<label>SQL표준점검구분</label>
						<select id="sql_std_qty_div_cd" name="sql_std_qty_div_cd" class="w160 easyui-combobox" data-options="panelHeight:'auto',editable:false,prompt:'선택'" required></select>
						
						<label>스케줄러</label>
						<select id="sql_std_qty_scheduler_no" name="sql_std_qty_scheduler_no" class="w250 easyui-combobox" data-options="panelHeight:'auto',editable:false,prompt:'선택'" required></select>
						
						<label>기준일자</label>
						<input type="text" id="startDate" name="startDate" value="${startDate}" class="w100 datapicker easyui-datebox" required="required" data-options="panelHeight:'auto',editable:false"/>
						<input type="text" id="endDate" name="endDate" value="${nowDate}" class="w100 datapicker easyui-datebox" required="required" data-options="panelHeight:'auto',editable:false"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:150px;min-height:330px">
				<table style="width:100%;height:97%;" class="detailT5">
					<colgroup>
						<col style="width:50%;">
						<col style="width:50%;">
					</colgroup>
					<thead>
						<tr>
							<th style="height:20px;" id="titleChartStandardComplianceRateTrendTotal">표준 준수율 추이(전체)</th>
							<th style="height:20px;" id="titleChartWorkStatus">업무별 현황(전체)</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div id="chartStandardComplianceRateTrendTotal" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<table style="width:100%;height:10%;" class="detailT6">
									<colgroup>
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
									</colgroup>
									<tbody>
										<tr>
											<td style="border-left:1px solid #FFF;">
												<label>SQL 본수</label>
											</td>
											<td>
												<label>미준수 본수</label>
											</td>
											<td>
												<label>표준 준수율</label>
											</td>
											<td>
												<label for="incre_sql_text">SQL 증감</label><span id="incre_sql_cnt_point"/>
											</td>
											<td>
												<label for="incre_err_text">미준수 증감</label><span id="incre_err_cnt_point"/>
											</td>
											<td>
												<label for="incre_rate_text">준수율 증감</label><span id="incre_rate_point"/>
											</td>
											<td>
												<label>금회차</label>
											</td>
											<td style="border-right:1px solid #FFF;">
												<label>전회차</label>
											</td>
										</tr>
									</tbody>
								</table>
								<table style="width:100%;height:10%;" class="detailT7">
									<colgroup>
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
										<col style="width:8%;">
									</colgroup>
									<tbody>
										<tr>
											<td style="border-left:1px solid #FFF;">
												<input type="text" id="last_program_cnt" name="last_program_cnt" value="0" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_tot_err_cnt" name="last_tot_err_cnt" value="0" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_cpla_rate" name="last_cpla_rate" value="0%" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_sql_cnt" name="incre_sql_cnt" value="0" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_err_cnt" name="incre_err_cnt" value="0" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_rate" name="incre_rate" value="0%" data-options="readonly:true" class="w60 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_sql_std_gather_day" name="last_sql_std_gather_day" value="${nowDate}" data-options="readonly:true" class="w80 easyui-textbox" style="text-align:center;"/>
											</td>
											<td style="border-right:1px solid #FFF;">
												<input type="text" id="pre_sql_std_gather_day" name="pre_sql_std_gather_day" value="${nowDate}" data-options="readonly:true" class="w80 easyui-textbox" style="text-align:center;"/>
											</td>
										</tr>
									</tbody>
								</table>
								<div id="chartWorkStatus" title="" style="height:80%;padding-top:0px;">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:150px;min-height:330px">
				<table style="width:100%;height:97%;" class="detailT5">
					<colgroup>
						<col style="width:33%;">
						<col style="width:33%;">
						<col style="width:33%;">
					</colgroup>
					<thead>
						<tr>
							<th style="height:20px;" id="titleChartNonComplianceStatus">품질지표별 미준수 현황(전체)</th>
							<th style="height:20px;" id="titleChartStandardComplianceRateTrend">업무별 표준 준수율 추이(전체)</th>
							<th style="height:20px;" id="titleChartNonStandardComplianceRateTrend">품질지표별 미준수 추이(전체)</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div id="chartNonComplianceStatus" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartStandardComplianceRateTrend" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartNonStandardComplianceRateTrend" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/dblPopup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</div>
</body>
</html>