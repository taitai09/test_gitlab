<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 * 2018.05.29	이원식	메뉴 위치 변경
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>애플리케이션 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/appPerformance.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->			
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>						
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="tr_cd" name="tr_cd" value="${tr_cd}"/>
					<input type="hidden" id="dbio" name="dbio" value="${dbio}"/>
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${wrkjob_cd}"/>
					<input type="hidden" id="searchKey" name="searchKey" value="${searchKey}"/>
					<input type="hidden" id="appl_hash" name="appl_hash"/>
					<input type="hidden" id="sql_hash" name="sql_hash"/>
					<label>업무구분</label>
					<select id="selectWrkJob" name="selectWrkJob" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>&nbsp;&nbsp;
					<label>검색조건</label>
					<select id="selectKey" name="selectKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="true">
						<option value="01">애플리케이션</option>
						<option value="02">SQL식별자(DBIO)</option>
					</select>&nbsp;&nbsp;
<%-- 					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox" value="${tr_cd}" /> --%>
					<input type="text" id="searchValue" name="searchValue" class="w300 easyui-textbox" value="" required="true"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						<a href="javascript:;" id="reqBtn" class="w90 easyui-linkbutton" data-options="disabled:true" onClick="Btn_TuningRequest();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
					</span>
				</form:form>	
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
			<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:30%;padding-top:5px;">
				<table id="dbioList" class="tbl easyui-datagrid" data-options="fitColumns:true,fit:true,border:false">
				    <tbody>
				        <tr>
				        </tr>
				    </tbody>
				</table>
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding-top:5px;">
				<div id="appTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div title="APP 일별(최근1개월)" style="padding:5px">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;">
								<table id="dailyTradeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div id="appDailyChart" data-options="region:'center',split:false,collapsible:false,border:false" style="margin-left:5px; margin-top:0px; border-top:1px solid; left:0px;">
							</div>
						</div>
					</div>
					<div title="SQL식별자(DBIO) 일별(최근1개월)" style="padding:5px">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;">
								<table id="dailyDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div id="dbioDailyChart" data-options="region:'center',split:false,collapsible:false,border:false">
							</div>
						</div>					
					</div>					
					<div title="SQL 수행이력(최근1주일)" style="padding:5px">
						<table id="historyDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>					
					<div title="SQL TEXT" style="padding:5px">
							<table class="detailT">
								<colgroup>	
									<col style="width:15%;"/>
									<col style="width:85%;"/>
								</colgroup>								
								<tr>
									<th>
										SQL TEXT<br/><br/>
										<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
										<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
									</th>
									<td style="padding:10px;height:135px;">
										<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off"></textarea>
									</td>
								</tr>
							</table>
					</div>
				</div>
			</div>
			<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:35%;padding-top:5px;">
				<div id="dbioTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div title="APP시간별(최근1개월)" style="padding:5px">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;">
								<table id="timeTradeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div id="appTimeChart" data-options="region:'center',split:false,collapsible:false,border:false" style="margin-left:5px; border-top:1px solid;">
							</div>
						</div>					
					</div>
					<div title="SQL식별자(DBIO) 시간별(최근1개월)" style="padding:5px">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;">
								<table id="timeDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div id="dbioTimeChart" data-options="region:'center',split:false,collapsible:false,border:false" style="margin-left:5px; border-top:1px solid;">
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
</body>
</html>