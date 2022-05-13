<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.11	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>시스템관리 :: 사용자 관리 :: 부서 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/system/userMng/department.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3">부서 관리</span>
				<ul>
                  		<li><a href="/"><img src="/resources/images/ico_home.gif" class="homeimg" alt="home"></a></li>
                  		<li> > 시스템 > <a href="/System/UserMng/User">사용자 관리</a> > <span class="linkN">부서 관리</span></li>							
				</ul>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto'" class="w120 easyui-combobox">
						<option value="">선택</option>
						<option value="01">부서명</option>
						<option value="02">부서코드</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="Btn_OnClick();">조회</a>
					</span>
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="padding-left:5px;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="min-height:350px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<table class="detailT">
					<colgroup>	
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:19%;">
					</colgroup>
					<tr>
						<th>부서</th>
						<td><select id="dept_cd" name="dept_cd" data-options="panelHeight:'auto'" class="w200 easyui-combotree"></select></td>
						<th>상위부서</th>
						<td><select id="upper_dept_cd" name="upper_dept_cd" data-options="panelHeight:'auto'" class="w200 easyui-combotree"></select></td>
						<th>사용여부</th>
						<td>
							<select id="use_yn" name="use_yn" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
						</td>								
					</tr>
					<tr>
						<th>부서설명</th>
						<td colspan="5"><input type="text" id="dept_desc" name="dept_desc" class="w400 easyui-textbox"/></td>							
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-save'" onClick="Btn_SaveDept();">저장</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_ResetField();">초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>