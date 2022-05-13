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
	<title>성능분석 :: SQL 분석 :: 거래성능분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/tradingPerformance.js?ver=<%=today%>"></script>
</head>
<body>
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
					<input type="hidden" id="tr_cd" name="tr_cd"/>
					<input type="hidden" id="dbio" name="dbio"/>
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd"/>
					<input type="hidden" id="searchKey" name="searchKey"/>
					<input type="hidden" id="appl_hash" name="appl_hash"/>
					<input type="hidden" id="sql_hash" name="sql_hash"/>

					<label>업무구분</label>
					<select id="selectWrkJob" name="selectWrkJob" data-options="editable:false" class="w150 easyui-combobox"></select>&nbsp;&nbsp;
					<select id="selectKey" name="selectKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="">선택</option>
						<option value="01">애플리케이션</option>
						<option value="02">SQL식별자(DBIO)</option>
					</select>&nbsp;&nbsp;
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>	
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px">
			<table id="dbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-tabs" data-options="plain:true,border:false" style="width:100%;margin-top:10px;padding-left:5px;height:230px">
			<div title="거래 일별" style="padding:5px">
				<table id="dailyTradeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div title="거래 시간별" style="padding:5px">
				<table id="timeTradeList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>						
		</div>
		<div class="easyui-tabs" data-options="plain:true,border:false" style="width:100%;margin-top:10px;padding-left:5px;height:230px;">
			<div title="SQL식별자(DBIO) 일별" style="padding:5px">
				<table id="dailyDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div title="SQL식별자(DBIO) 시간별" style="padding:5px">
				<table id="timeDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div title="SQL식별자(DBIO) 수행이력" style="padding:5px">
				<table id="historyDbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
	</div>		
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>