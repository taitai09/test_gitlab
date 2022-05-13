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
	<title>모듈성능분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>        
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/modulePerformance.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
    <script type="text/javascript" src="/resources/js/paging2.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="sql_id" name="sql_id"/>
					<input type="hidden" id="subSearchYn" name="subSearchYn" value="N"/>
					<input type="hidden" id="start_interval_time" name="start_interval_time"/>
					<input type="hidden" id="end_interval_time" name="end_interval_time"/>
					
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="currentPage2" name="currentPage2" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					<input type="hidden" id="rcount" name="rcount" value="20"/>
					
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="editable:false" class="w100 easyui-combobox easyui-validatebox" required="true" ></select>
					<label>MODULE</label>
					<input type="text" id="module" name="module" class="w120 easyui-textbox easyui-validatebox"/>
					<label>ACTION</label>
					<input type="text" id="action" name="action" class="w120 easyui-textbox easyui-validatebox"/>
					<label>BEGIN_INTERVAL_TIME</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
					<input type="text" id="strStartTime" name="strStartTime" value="${startTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
					<input type="text" id="strEndTime" name="strEndTime" value="${nowTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false"/>
					<label>검색건수</label>
					<select id="selectRcount" name="selectRcount" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto',editable:false">
						<option value="10" selected>10</option>
						<option value="20">20</option>
						<option value="40">40</option>
						<option value="60">60</option>
						<option value="80">80</option>
						<option value="100">100</option>
						<option value="150">150</option>
						<option value="200">200</option>
					</select>
					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_MasterDownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(M)</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_SlaveDownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(S)</a>
					</div>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:670px">
				<div id="moudleTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div title="SQL List" data-options="fit:true" style="padding:5px;">
						<div class="easyui-layout" data-options="fit:true" style="border:0px;">
							
															
<!-- 							<div data-options="region:'center',border:false" style="width:50px;height:50%;"> -->
<!-- 								<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 								</table> -->
<!-- 								<div class="searchBtn" data-options="region:'north',split:false,border:false" style="width:100%;height:15%;padding-top:10px;text-align:right;"> -->
<!-- 									<a href="javascript:;" id="prevBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a> -->
<!-- 									<a href="javascript:;" id="nextBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a> -->
<!-- 								</div> -->
<!-- 							</div> -->
								
							<!-- 상단그리드 -->								
							<div data-options="region:'center',border:false" style="width:100%;height:50%;">
								<div class="easyui-layout" data-options="fit:true" style="border:0px;">
									<div data-options="region:'north',split:false,border:false" style="width:100%;height:85%;padding-top:10px;">
										<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
										</table>
									</div>
									<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:15%;padding-top:10px;text-align:right;">
										<a href="javascript:;" id="prevBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
										<a href="javascript:;" id="nextBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
									</div>
								</div>
							</div>
							
							<!-- 하단그리드 -->
							<div data-options="region:'south',border:false" style="width:100%;height:50%;">
								<div class="easyui-layout" data-options="fit:true" style="border:0px;">
									<div data-options="region:'north',split:false,border:false" style="width:100%;height:85%;padding-top:10px;">
										<table id="dtlTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
										</table>
									</div>
									<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:15%;padding-top:10px;text-align:right;">
										<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
										<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
									</div>
								</div>
							</div>
							
						</div>
					</div>
					<div title="TOP SQL Stat" style="padding:5px; border-bottom:1px solid lightgray;">
						<div id="totalElapsedChart" class="easyui-panel" data-options="title:'▶ Total Elapsed Time',border:true" style="width:99%;height:350px;padding:5px;margin-bottom:10px;">
						</div>
						<div id="avgElapsedChart" class="easyui-panel" data-options="title:'▶ Avg Elapsed Time',border:true" style="width:99%;height:350px;padding:5px;margin-bottom:10px;">
						</div>
						<div id="avgBufferChart" class="easyui-panel" data-options="title:'▶ Avg Buffer Gets',border:true" style="width:99%;height:350px;padding:5px;margin-bottom:10px;">
						</div>
						<div id="avgDiskChart" class="easyui-panel" data-options="title:'▶ Avg Disk Reads',border:true" style="width:99%;height:350px;padding:5px;margin-bottom:10px;">
						</div>
						<div id="executionsChart" class="easyui-panel" data-options="title:'▶ Executions',border:true" style="width:99%;height:350px;padding:5px;margin-bottom:10px;">
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>