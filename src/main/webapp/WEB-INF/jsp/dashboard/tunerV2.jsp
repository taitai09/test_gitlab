<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2022.01.10	이재우	튜너 dashboard 추가
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: OPEN-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on"/>
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/dashboard/tunerV2.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
		.full_widget div label{
			font-size:small;
			margin-left:0px;
			margin-right:1px;
		}
		.full_widget div .well{
			background-color:lightgray;
			border-radius: 10px;
			padding:10px 10px;
			width:78%;
			margin:15px 10px;
		}
		.full_widget .well div {
			padding:10px 5px;
			font-weight: bold;
		}
		.full_widget .well .cnts{
			background-color:white;
			border-radius:8px;
			width:32%;
			text-align: right;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- dash_contents START -->
	<div id="dash_contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="tuning_no" name="tuning_no"/>
			<input type="hidden" id="guide_no" name="guide_no"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" />
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="full_widget" style="height: 410px;margin-top:0px;">
				<div class="w_title_area">
					<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 튜닝현황</div>
				</div>
				<div style=" height: 370px;">
					<div style="height:90%;display: flex;">
						<div style="width:11%;">
							<div class="well" style="display: flex; margin-top:5px;">
								<div style="width:68%;"><label>튜닝작업대기</label></div>
								<div class="cnts"><label>${waitJobCnt}</label></div>
							</div>
							<div class="well" style="display: flex;">
								<div style="width:68%;"><label>튜닝진행</label></div>
								<div class="cnts"><label>${progressCnt}</label></div>
							</div>
							<div class="well" style="display: flex;">
								<div style="width:68%;"><label>튜닝지연예상</label></div>
								<div class="cnts"><label>${expectedDelayCnt}</label></div>
							</div>
							<div class="well" style="display: flex;">
								<div style="width:68%;"><label >튜닝지연</label></div>
								<div class="cnts"><label >${delayCnt}</label></div>
							</div>
							<div class="well" style="display: flex;">
								<div style="width:68%;"><label>주간튜닝완료</label></div>
								<div class="cnts"><label>${completeCnt}</label></div>
							</div>
						</div>
						<div class="w_body" style="height:100%;width: 1490px;">
							<div id="progressTab" class="easyui-tabs" data-options="fit:true,border:false">
								<div title="튜닝 작업 대기" style="padding-top:5px;">
									<table id="waitJobList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
										<tbody><tr></tr></tbody>
									</table>
								</div>
								<div title="튜닝 진행" style="padding-top:5px;">
									<table id="progressList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
								<div title="튜닝 지연 예상" style="padding-top:5px;">
									<table id="expectedDelayList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
								<div title="튜닝 지연" style="padding-top:5px;">
									<table id="delayList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
								<div title="주간 튜닝 완료" style="padding-top:5px;">
									<table id="completeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="searchBtn" style="width:100%;text-align:right;margin-top: 10px;">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
			</div>
		</form:form>
		<div class="full_widget" style="height:310px;">
			<div class="w_title_area">
				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 튜닝실적(최근1년)</div>
			</div>
			<div id="performanceChart" class="w_body" style="height:90%">
			</div>
		</div>
	</div>
	<!-- dash_contents END -->
</div>
<!-- container END -->
</body>
</html>