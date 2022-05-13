<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.14	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>튜닝담당자 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/userMng/performanceOfficer.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:720px;">
			<div data-options="region:'west',border:false" style="width:55%;padding:5px 5px 0px 0px;overflow:hidden;">
				<div class="well marginB10">
					<form:form method="post" id="left_form" name="left_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">전체</option>
							<option value="01">사용자명</option>
							<option value="02">사용자 ID</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickUser();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:600px;margin-bottom:10px;">
					<table id="tunerList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px 0px 0px 5px;">
				<div class="well marginB10">
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ DB 튜닝담당자 추가</span></div>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<input type="hidden" id="tuner_id" name="tuner_id"/>
					<div class="easyui-panel" data-options="border:false" style="height:600px; border:1px solid; border-top:0px;" >
						<table id="dbOfficerList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:8%;">
								<col style="width:22%;">
								<col style="width:35%;">
								<col style="width:35%;">
							</colgroup>
							<thead>
								<tr>
									<th><input type="checkbox" id="chkAll" name="chkAll" class="chkBox"/></th>
									<th>DB명</th>
									<th>성능담당 시작일자</th>
									<th>성능담당 종료일자</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div class="searchBtn innerBtn2" style="display:block;">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveTunerDBOfficer();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					</div>
				</form:form>
<!-- 				<div class="dtl_title" style="clear:both;"><span id="histTitle" class="h3" style="margin-left:0px;">▶ DB 튜닝담당자 이력</span></div> -->
<%-- 				<form:form method="post" id="history_form" name="history_form" class="form-inline"> --%>
<!-- 					<input type="hidden" id="dbid" name="dbid"/> -->
<!-- 					<input type="hidden" id="tuner_id" name="tuner_id"/> -->
<!-- 					<div class="easyui-panel" data-options="border:false" style="height:300px">							 -->
<!-- 						<table id="dbOfficerHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 						</table> -->
<!-- 					</div> -->
<%-- 				</form:form> --%>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>