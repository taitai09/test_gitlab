<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.10.05	임호경	최초작성
  **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title></title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/databaseMngSetting/uiExceptDbUserMng.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:750px;">
			<div data-options="region:'west',border:false" style="width:50%;padding:5px 5px 0px 0px;">
				<div class="well marginB10">
					<form:form method="post" id="submit_form" name="left_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">전체</option>
							<option value="01">아이디</option>
<!-- 							<option value="02">DB유저아이디</option> -->
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:650px;margin-bottom:10px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px 0px 0px 5px;">
				<div class="well marginB10">
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ UI제외 DB사용자 관리 </span></div>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<div class="easyui-panel" data-options="border:false" style="height:600">

						<input type="hidden" id="user_id" name="user_id" value="${user_id}">

						<input type="hidden" id="old_username" name="old_username" value="">
						<input type="hidden" name="crud_flag" id="crud_flag" value="C"/>

						
						<table id="right_UsernameList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:30%;">
								<col style="width:70%;">
							</colgroup>
							
							<tr>
								<th>아이디</th>
								<td>
									<input type="text" id="username" name="username" data-options="panelHeight:'auto',editable:true" required="required" class="w200 easyui-textbox"/>
								</td>
							</tr>
						</table>
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
							<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
						</div>
					</div>
				</form:form>
						
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>