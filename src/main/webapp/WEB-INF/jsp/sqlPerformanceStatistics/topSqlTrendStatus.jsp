<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>TOP SQL 추이/현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlPerformanceStatistics/topSqlTrendStatus.js?ver=<%=today%>"></script>

<style>
.tabs {background-color:#ffffff;}    
.tabs-header {background-color:#ffffff;}
.panel layout-panel{height:300px;width:50%;}    
.tabGrid{border-left: 0px solid #ffffff;border-bottom:0px solid #ffffff; border-right:0px solid #ffffff;}
.datagrid-row-over,
.datagrid-header td.datagrid-header-over {
  background: #E7E7E7;
  color: #000000;
  cursor: default;
}
.datagrid-row-selected {
/*   background: #ffe48d; */
  background: #E7E7E7;
}
.temp {
font-size:13px;
}
</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	 <div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="aDayAgo"  value="${aDayAgo}" />	
					<input type="hidden" id="aWeekAgo"  value="${aWeekAgo}" />	
					<input type="hidden" id="aMonthAgo"  value="${aMonthAgo}" />	
					<input type="hidden" id="nowDate"  value="${nowDate}" />	
					<input type="hidden" id="inst_id" name="inst_id" value="" />	
					<input type="hidden" id="inst_nm" name="inst_nm" value="" />	
					<input type="hidden" id="dbid" name="dbid" value="" />	
					<input type="hidden" id="action" name="action" value="" />	
					<input type="hidden" id="module" name="module" value="" />	
					<input type="hidden" id="parsing_schema_name" name="parsing_schema_name" value="" />	
					<input type="hidden" id="excel_inst_nm" name="excel_inst_nm" value="" />	

					<input type="hidden" id="whatTime" name="whatTime" value="allTime" />	
					<input type="hidden" id="strStartDt" name="strStartDt" value="" />	
					<input type="hidden" id="strEndDt" name="strEndDt" value="" />	

					<span style="padding-left:10px;padding-right:5px;">
						<input class="easyui-radiobutton" id="select_db" name="search_way1" value="Y" checked> DB 
					</span> 
				    <span style="padding-right:5px;">
				    	<input class="easyui-radiobutton" id="select_instance" name="search_way1" value="Y"> INSTANCE 
			    	</span> 
					
					<span id="span_inst_id">
						<select id="select_inst_id"  data-options="editable:false" class="w120 easyui-combobox" required="true" ></select>
					</span>
					<span id="span_dbid">
						<select id="select_dbid" data-options="editable:false" class="w120 easyui-combobox" required="true" ></select>
					</span>
					
				    <span style="padding-left:10px;padding-right:5px;">
				    	<input class="easyui-radiobutton" id="select_adayago" name="search_day" value="yesterday"> 전일 
			    	</span>
				    <span style="padding-right:5px;"><input class="easyui-radiobutton" id="select_aweekago" name="search_day"value="week"> 1주일 </span>
					<span style="padding-right:5px;"><input class="easyui-radiobutton" id="select_amonthago" name="search_day" value="month" checked> 1개월 </span> 
				    
					<input type="text" id="startDate" name="startDate" value="${aMonthAgo}"data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required"/> 
					<input type="text" id="startTime" name="startTime" value="00:00" data-options="panelHeight:'auto',editable:true" class="w60 easyui-timespinner" required="required"/>
					~
					<input type="text" id="endDate" name="endDate" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required"/>
					<input type="text" id="endTime" name="endTime" value="23:59" data-options="panelHeight:'auto',editable:true" class="w60 easyui-timespinner" required="required"/>
			
				    <label style="margin-right:5px;">SQL Activity</label>
					<input type="number" id="activity" name="activity" value="1" class="w50 easyui-textbox" required="true" />
					<span style="font-size:10px;"> % 이상</span>
					<label style="margin-right:5px;">TOP</label>
					<input type="number" id="top" name="top" value="20" class="w50 easyui-textbox" required="true"/>
					
					
					<span style="margin-left:15px;"class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					
					
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:670px;border-bottom:1px solid;">
					<div data-options="region:'west',border:false" class="gridLine" style="width:500px;height:100%;padding-right:5px;">
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titleChartCPUStatus">CPU</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartCPUStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titleChartElapsedTimeSQLStatus">Elapsed Time SQL</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartElapsedTimeSQLStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titleChartCPUTimeSQLStatus">CPU Time SQL</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartCPUTimeSQLStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
				
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titleChartExecutionStatus">Execution</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartExecutionStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
				
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titleChartLogicalReadsCPUStatus">Logical Reads</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartLogicalReadsCPUStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
				
						<table style="width:100%;height:130px;" class="detailT5">
							<colgroup>
								<col style="width:100%;">
							</colgroup>
							<thead>
								<tr>
									<th style="height:20px;" id="titlechartPhysicalReadsStatus">Physical Reads</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="">
										<div id="chartPhysicalReadsStatus" title="" style="width:100%;height:100%;padding-top:0px;">
										</div>
									</td>
								</tr>
							</tbody>
						</table>
				
					</div>
					
					
					<div data-options="region:'center',split:false,border:false" id="gridBox" style="height:100%;width:100%;margin-top:5px;border-left:1px solid #cccccc;">
						<div id="info" style="margin-top:2px;margin-bottom:10px;">
		    				<form:form method="post" id="notice_form" name="notice_form" class="form-inline">
								<span class="marginL10"><input type="text" id="check1" value="" class="w140 easyui-textbox" readonly/></span>
								<input type="text" id="check2" value="" class="w140 easyui-textbox" readonly/>
								<input type="text" id="check3" value="" class="w140 easyui-textbox" readonly/>
	
								<span style="margin-left:6px;padding-left:50px;padding-left:10px;"><input class="easyui-radiobutton" id="select_alltime" name="search_way3" value="allTime" checked> 전체 </span>
								<span style="padding-left:5px;padding-left:10px;"><input class="easyui-radiobutton" id="select_daytime" name="search_way3" value="dayTime"> 주간 </span>
								<span style="padding-left:5px;padding-left:10px;"><input class="easyui-radiobutton" id="select_nighttime" name="search_way3" value="nightTime"> 야간 </span> 
<!-- 							<span id="check1" class="temp"> </span> -->
<!-- 							<span id="check2" class="temp"> </span> -->
<!-- 							<span id="check3" class="temp"> </span> -->
							</form:form>
						</div>
						<div class="easyui-layout" data-options="fit:true">
							<div data-options="region:'north',split:false,border:false" style="height:300px;width:100%;margin-top:5px;">
								<div class="easyui-panel" data-options="border:false" style="width:100%;padding-top:5px;min-height:290px;margin-bottom:10px;">
									<div id="tabsDiv" class="easyui-tabs" data-options="fit:true,border:false">
										<div title="Module" class="tabGrid">
											<table id="tableList_module" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="height:215px;">
											</table>
										</div>
										<div title="Action" class="tabGrid">
											<table id="tableList_action" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
										<div title="Parsing Schema" class="tabGrid">
											<table id="tableList_parsingSchema" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</div>
								</div>
							</div>
								
							<div data-options="region:'center',split:false,border:false" style="width:100%;height:250px;margin-left:5px;">
								<div class="easyui-layout" data-options="fit:true"  style="width:50%;min-height:200px">>
									<div data-options="region:'north',split:false,border:false" style="height:50px;">
									
										<div style="margin-top:5px;margin-bottom:5px;">
											<span>
<!-- 											<input class="easyui-tagbox" id="search_condition_box" value="Apple,Orange" style="width:400px;"> -->
												<input class="w140 easyui-tagbox" id="tag_module" />
								        		<input class="w140 easyui-tagbox" id="tag_action" />
								        		<input class="w140 easyui-tagbox" id="tag_parsing_schema" />
								        	</span>
								        	<span style="float:right;margin-right:10px;">
												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
								        	</span>
										</div>
									</div>
									
									<div data-options="region:'center',split:false,border:false" style="width:50%;height:40px;">
										<div class="easyui-panel" data-options="border:false" style="width:100%;height:85%;margin-bottom:5px;padding-right:10px;">
											<table id="tableList_result" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlProfile_popup.jsp" %> <!-- SQL Profile 팝업 -->
</body>
</html>