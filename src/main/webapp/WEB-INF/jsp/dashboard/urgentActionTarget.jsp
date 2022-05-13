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
	<title>긴급조치대상현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/dashboard/urgentActionTarget.js?ver=<%=today%>"></script>   
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->

		<div id="contents">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="dbid" name="dbid" value="${dbid}"/>
						<input type="hidden" id="check_items" name="check_items" value="Y"/>
						<input type="hidden" id="check_day" name="check_day" value="${check_day}"/>
						<input type="hidden" id="gather_day" name="gather_day" value="${gather_day}"/>
						<input type="hidden" id="check_seq" name="check_seq"/>
						<input type="hidden" id="check_item_name" name="check_item_name"/>

						<input type="hidden" id="check_pref_id" name="check_pref_id" value="${check_pref_id}"/>
						<input type="hidden" id="emergency_action_yn" name="emergency_action_yn" value="${emergency_action_yn}"/>
						<input type="hidden" id="emergency_action_no" name="emergency_action_no" value="${emergency_action_no}"/>
						<div>
							<label>DB</label>
							<select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select>
							<label>점검일</label>
							<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/>
							<label>점검 회차</label>
							<select id="selectCheckSeq" name="selectCheckSeq" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="true"></select>					
						</div>
						<div class="multi">
							<label>리스크유형</label>
							<select id="selectRiskType" name="selectRiskType" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
							<label>조치구분</label>
							<select id="selectActionType" name="selectActionType" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox">
								<option value="">전체</option>
								<option value="Y">조치</option>
								<option value="N">미조치</option>
							</select>
		
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_Search();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_UpdateUrgentAction();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조치완료</a>
							</span>
						</div>
					</form:form>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px">
				<div data-options="region:'center',border:false">
					<table id="urgentActionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>

	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>