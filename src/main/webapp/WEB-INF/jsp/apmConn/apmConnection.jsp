<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.04	임호경	최초작성
  **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>APM 접속 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/apmConn/apmConnection.js?ver=<%=today%>"></script>
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
			<div data-options="region:'west',border:false" style="width:70%;padding:5px 5px 0px 0px;">
				<div class="well marginB10">
					<form:form method="post" id="left_form" name="left_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">전체</option>
							<option value="01">업무명</option>
							<option value="02">DB유저아이디</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_apmList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>								
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:650px;margin-bottom:10px;">
					<table id="apmList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px">
				<div class="well marginB10">
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ APM 추가/수정 </span></div>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<div class="easyui-panel" data-options="border:false" style="height:600">
						<input type="hidden" id="user_id" name="user_id" value="${user_id}">

						<input type=hidden id="wrkjob_cd_nm" name="wrkjob_cd_nm" value="">
						<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="">
						<input type="hidden" id="apm_operate_type_cd_nm" name="apm_operate_type_cd_nm" value="">
						<input type="hidden" id="old_apm_operate_type_cd" name="old_apm_operate_type_cd" value="">

						<input type="hidden" name="crud_flag" id="crud_flag" value="C"/>
						<input type="hidden" name="updateIsAll" id="updateIsAll" value="Y"/>

						
						<table id="right_ApmList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:30%;">
								<col style="width:70%;">
							</colgroup>
							
							<tr>
								<th>업무명</th>
								<td>
<!-- 								<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'auto'" class="w200 easyui-combotree"></select> -->
									<select id="select_wrkjob_cd" name="select_wrkjob_cd" data-options="panelHeight:'500',editable:true" class="w210 easyui-textbox" required="required"/></select>
								</td>
							</tr>
							<tr>
								<th>APM 운영유형</th>
								<td>
									<select id="select_apm_operate_type_cd" name="apm_operate_type_cd" data-options="panelHeight:'auto',editable:true" class="w210 easyui-combotree" required></select>
								</td>
							</tr>
							<tr>
								<th>DB 접속IP</th>
								<td>
									<input type="text" id="db_connect_ip" name="db_connect_ip" pattern="^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$" class="w200 easyui-textbox" required/>
								</td>
							</tr>
							<tr>
								<th>DB 접속PORT</th>
								<td>
									<input type="number" id="db_connect_port" name="db_connect_port" class="w200 easyui-textbox"/>
								</td>
							</tr>
							<tr>
								<th>DB 유저아이디</th>
								<td><input type="text" id="db_user_id" name="db_user_id" class="w200 easyui-textbox" required/></td>
							</tr>
						</table>
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_saveAPM();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_deleteAPM();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
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