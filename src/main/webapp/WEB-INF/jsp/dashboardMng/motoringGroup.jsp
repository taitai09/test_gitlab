<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.28	임호경	최초작성
 * 2020.05.20	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: 대시보드 설정 :: 모니터링 기본그룹 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/dashboardMng/MotoringGroup.js?ver=<%=today%>"></script>
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

					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="">전체</option>
						<option value="01">그룹명</option>
<!--						<option value="02">유저 아이디</option>	-->
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox" readonly="true" />
<!-- 					<label>사용여부</label> -->
<!-- 					<select id="search_use_yn" name="search_use_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox"> -->
<!-- 						<option value="">전체</option>		-->
<!-- 						<option value="Y">사용</option> 		-->
<!-- 						<option value="N">미사용</option> 	-->
<!-- 					</select> -->
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
				<input type="hidden" id="old_group_nm" name="old_group_nm" />
				<input type="hidden" id="group_id" name="group_id" />
				
				
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
			
				<table class="detailT">
					<colgroup>	
						<col style="width:5%;">
						<col style="width:15%;">
						<col style="width:5%;">
						<col style="width:8%;">
						<col style="width:7%;">
						<col style="width:40%;">
						<col style="width:7%;">
						<col style="width:13%;">
					</colgroup>
					<tr>
						<th>그룹명</th>
						<td>
							<input type="text" id="group_nm" name="group_nm" class="w180 easyui-textbox" required="required"/>
						</td>
						
						<th>정렬순서</th>
						<td>
							<input type="number" id="desplay_seq" name="desplay_seq" class="w80 easyui-textbox" required/>
						</td>
						
						<th>그룹 설명</th>
						<td>
							<input type="text" id="group_desc" name="group_desc" class="w500 easyui-textbox"/>
						</td>
						
						<th>등록 아이디</th>
						<td>
							<input type="text" id="user_id" name="user_id" class="w150 easyui-textbox"/>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>