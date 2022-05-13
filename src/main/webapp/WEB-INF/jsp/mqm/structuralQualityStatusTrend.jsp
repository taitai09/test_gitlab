<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<%
/***********************************************************
 * 2019.08.23	임호경	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>구조 품질 준수 현황/추이</title>
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
	<script type="text/javascript" src="/resources/js/ui/mqm/structuralQualityStatusTrend.js?ver=<%=today%>"></script>
	<style>
	
	
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

			<input type="hidden" id="project_id" name="project_id"/>

			<input type="hidden" id="model_nm" name="model_nm"/>
			<input type="hidden" id="extrac_dt" name="extrac_dt"/>

			
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<div>
						<label>프로젝트</label>
						<input type="text" id="project_nm" name="project_nm" value="" class="w350 easyui-textbox"/>
					
						<label>라이브러리명</label>
						<select id="lib_nm" name="lib_nm" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
							<option value="">전체</option>
						</select>
<!-- 						<label>모델명</label> -->
<!-- 						<select id="model_nm" name="model_nm" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"> -->
<!-- 							<option value="">전체</option> -->
<!-- 						</select> -->
					
						<label>기준일자</label>
						<input type="text" id="startDate" name="startDate" value="${startDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
						<input type="text" id="endDate" name="endDate" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
					</div>
				</div>
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
							<th style="height:20px;" id="titleChartNonCompliantStandardizationRateTrend">미준수 건수 / 표준화율 추이</th>
							<th style="height:20px;" id="titleChartStatusByModel">모델별 현황</th>
							<th style="height:20px;" id="titleChartStandardizationRateStatusByModel">모델별 표준화율 현황</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div id="chartNonCompliantStandardizationRateTrend" title="" style="width:100%;height:100%;padding-top:0px;">
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
												<label>엔터티수</label>
											</td>
											<td>
												<label>속성수</label>
											</td>
											<td>
												<label style="line-height:normal;">비표준&ensp;&nbsp;<br/>&ensp;&ensp;속성수</label>
											</td>
											<td>
												<label>표준화율</label>
											</td>
											<td>
												<label style="margin-left:0px;line-height:normal;" for="incre_ent_cnt_text">엔터티<br/>증감</label><span id="incre_ent_cnt_sign"/>
											</td>
											<td>
												<label style="margin-left:0px;line-height:normal;" for="incre_att_cnt_text">속성<br/>증감</label><span id="incre_att_cnt_sign"/>
											</td>
											<td>
												<label style="margin-left:0px;line-height:normal;" for="incre_std_rate_text">표준화율<br/>증감</label><span id="incre_std_rate_sign"/>
											</td>
											<td>
												<label style="margin-left:0px;line-height:normal;" for="incre_err_cnt_text">비표준 속성<br/>증감</label><span id="incre_err_sign"/>
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
												<input type="text" id="last_ent_cnt" name="last_ent_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_att_cnt" name="last_att_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_err_101_cnt" name="last_err_101_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="last_std_rate" name="last_std_rate" value="0%" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_ent_cnt" name="incre_ent_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_att_cnt" name="incre_att_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td>
												<input type="text" id="incre_std_rate" name="incre_std_rate" value="0%" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
											<td style="border-right:1px solid #FFF;">
												<input type="text" id="incre_err_cnt" name="incre_err_cnt" value="0" data-options="readonly:true" class="w50 easyui-textbox" style="text-align:center;"/>
											</td>
										</tr>
									</tbody>
								</table>
								<div id="chartStatusByModel" title="" style="height:80%;padding-top:0px;">
								</div>
							</td>
							<td>
								<table style="width:100%;height:10%;" class="detailT6">
									<colgroup>
										<col style="width:8%;">
										<col style="width:8%;">
									</colgroup>
									<tbody>
										<tr>
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
									</colgroup>
									<tbody>
										<tr>
											<td>
												<input type="text" id="last_extrac_dt" name="last_extrac_dt" value="${nowDate}" data-options="readonly:true" class="w80 easyui-textbox" style="text-align:center;"/>
											</td>
											<td style="border-right:1px solid #FFF;">
												<input type="text" id="pre_extrac_dt" name="pre_extrac_dt" value="${nowDate}" data-options="readonly:true" class="w80 easyui-textbox" style="text-align:center;"/>
											</td>
										</tr>
									</tbody>
								</table>
								<div id="chartStandardizationRateStatusByModel" title="" style="height:80%;padding-top:0px;">
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
							<th style="height:20px;" id="titleChartNumberNonCompliantByModel">모델별 미준수 건수 추이(전체)</th>
							<th style="height:20px;" id="titleChartStandardComplianceRateTrend">표준 준수율 추이(전체)</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div id="chartNonComplianceStatus" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartNumberNonCompliantByModel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartStandardComplianceRateTrend" title="" style="width:100%;height:100%;padding-top:0px;">
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