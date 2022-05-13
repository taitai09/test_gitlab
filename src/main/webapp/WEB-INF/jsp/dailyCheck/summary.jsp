<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.18	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일 예방 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/dailyCheck/summary.js?ver=<%=today%>"></script>

<style>
.datagrid-header .datagrid-cell span {
  font-size: 10px;
}
</style>    
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${dailyCheck.dbid}"/>
		<input type="hidden" id="check_items" name="check_items" value="Y"/>
		<input type="hidden" id="check_day" name="check_day" value="${dailyCheck.check_day}"/>
		<input type="hidden" id="check_day_dash" name="check_day_dash" value="${check_day}"/>
		<input type="hidden" id="check_seq" name="check_seq" value="0"/>
		<input type="hidden" id="check_item_name" name="check_item_name"/>
		<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>


		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<label>DB</label>
					<select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>점검일</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/>
<!--  					<label>점검 회차</label> -->
<!-- 					<select id="selectCheckSeq" name="selectCheckSeq" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="true"></select>					 -->
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
<!-- 					<div class="searchBtn innerBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-xls'" onClick="ExcelDownClick(0,'일 예방 점검','일 예방 점검');">엑셀</a></div> -->
					<div class="searchBtn excelBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-xls'" onClick="ExcelDownClick(1,'','일예방점검');">엑셀</a></div>
					<div class="searchBtn excelBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="" onClick="fnOpenExceptionManageTab();">예외 관리</a></div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
				<div data-options="region:'north',split:false,border:false" style="height:40px;">
					<div style="float:left;width:50%;">
						<label>점검 항목</label>
						<input type="checkbox" id="chkItems" name="chkItems" class="w120 easyui-switchbutton"/>
					</div>
<!-- 					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_none_new.png" style="vertical-align:bottom;"/> <b>Health None</b></div> -->
					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_none_new.png" style="vertical-align:bottom;"/> <b>Health None</b></div>
					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_red_new.png" style="vertical-align:bottom;"/> <b>Health Red</b></div>
					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_green_new.png" style="vertical-align:bottom;"/> <b>Health Green</b></div>
<!-- 					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_yellow.gif" style="vertical-align:bottom;"/> <b>Health Yellow</b></div> -->
<!-- 					<div style="float:right;width:110px;margin-top:5px;"><img src="/resources/images/status_green_new.png" style="vertical-align:bottom;"/> <b>Health Green</b></div> -->
				</div>
				<div data-options="region:'center',border:false">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div style="display:none">
					<table id="tableList2" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</form:form>			
	<!-- contents END -->
</div>
<iframe id="excelDownIf" style="display:none"></iframe>
<!-- container END -->
</body>
</html>