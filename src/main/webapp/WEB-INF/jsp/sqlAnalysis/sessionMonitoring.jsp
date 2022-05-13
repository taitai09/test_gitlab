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
	<title>세션 성능 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>     
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/sessionMonitoring.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="inst_id" name="inst_id"/>
					<input type="hidden" id="status" name="status"/>
					<input type="hidden" id="type" name="type" value="YES"/>
					<input type="hidden" id="parallel" name="parallel" value="N"/>
					<input type="hidden" id="refresh" name="refresh" value="N"/>

					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w100 easyui-combobox" required="true"></select>
						<label>INSTANCE</label>
						<select id="selectInstance" name="selectInstance" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox"></select>						
						<label>SQL_ID</label>
						<input type="text" id="sql_id" name="sql_id" value="" class="w100 easyui-textbox"/>
						<label>PROGRAM</label>
						<input type="text" id="program" name="program" value="" class="w100 easyui-textbox"/>
						<label>MODULE</label>
						<input type="text" id="module" name="module" value="" class="w100 easyui-textbox"/>
						<label>EVENT</label>
						<input type="text" id="event" name="event" value="" class="w100 easyui-textbox"/>
					</div>
					<div class="multi">						
						<label>MACHINE</label>
						<input type="text" id="machine" name="machine" value="" class="w100 easyui-textbox"/>
						<label>OSUSER</label>
						<input type="text" id="osuser" name="osuser" value="" class="w100 easyui-textbox"/>
						<label>STATUS</label>
						<select id="selectStatus" name="selectStatus" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox">
							<option value="ALL">전체</option>
							<option value="ACTIVE">ACTIVE</option>
							<option value="INACTIVE">INACTIVE</option>
							<option value="KILLED">KILLED</option>
						</select>
						<label>BACKGROUND 제외</label>
						<input type="checkbox" id="chkType" name="chkType" class="w80 easyui-switchbutton"/>
						<label>Parallel Only</label>
						<input type="checkbox" id="chkParallel" name="chkParallel" class="w80 easyui-switchbutton"/>
						<label>Refresh</label>
						<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
						<input type="text" id="refresh_sec" name="refresh_sec" value="10" class="w40 easyui-textbox"/> 초
						<span class="searchBtnLeft multiBtn">						
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_RefreshSearch();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
					</div>
					<div class="searchBtn multiBtn">						
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_KillSessionClick();"><i class="btnIcon fas fa-terminal fa-lg fa-fw"></i> Kill Session</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:620px">
			<div class="easyui-layout" data-options="region:'center',border:false">
				<div id="tableDiv" class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div title="Session List" style="padding:5px">
						<table id="sessionList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="waitClassChart" title="Wait Class" style="padding:5px">
					</div>
					<div id="topEventChart" title="TOP Event" style="padding:5px">
					</div>
					<div id="topModuleChart" title="TOP Module" style="padding:5px">
					</div>
				</div>	
			</div>
			<div data-options="region:'south',split:false,border:false" style="padding-top:10px;height:45%;">
				<div id="tabsDiv" class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<form:form method="post" id="sub_form" name="sub_form" class="form-inline">
						<input type="hidden" id="dbid" name="dbid"/>
						<input type="hidden" id="inst_id" name="inst_id"/>
						<input type="hidden" id="sql_id" name="sql_id"/>
						<input type="hidden" id="sid" name="sid"/>
						<input type="hidden" id="serial" name="serial"/>
						<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>								
					</form:form>								
					<div id="lastCursorDiv" title="Last Cursor" style="padding:10px;font-size:12px;">
					</div>
					<div title="All Cursors" style="padding:5px">
						<table id="allCursorsList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>			
					<div title="SQL 성능" style="padding:5px">
						<table id="sqlList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Session History" style="padding:5px">
						<table id="sessionHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							<tbody><tr></tr></tbody>
						</table>
					</div>
					<div title="SQL Grid Plan" style="padding:5px">
						<table id="sqlGridPlanList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
						</table>
					</div>						
					<div title="SQL Text Plan" style="padding:10px">
						<ul id="textPlan" style="font-size:13px;"></ul>
					</div>
					<div id="sessionKillDiv" title="Session Kill Script" style="padding:10px;font-weight:700;font-size:13px;">
					</div>
					<div id="processDiv" title="Process Kill Script" style="padding:10px;font-weight:700;font-size:13px;">
					</div>
					<div title="Connect Info" style="padding:5px;">
						<table id="connectInfoList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Event" style="padding:5px;">
						<table id="eventList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Wait" style="padding:5px;">
						<table id="waitList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Wait Class" style="padding:5px;">
						<table id="waitClassList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Wait History" style="padding:5px;">
						<table id="waitHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Metric" style="padding:5px;">
						<table id="metricList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Time Model" style="padding:5px;">
						<table id="timeModelList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="Statistics" style="padding:5px;">
						<table id="statisticsList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="I/O" style="padding:5px;">
						<table id="ioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div title="LongOPS" style="padding:5px;">
						<table id="longOpsList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>				
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>