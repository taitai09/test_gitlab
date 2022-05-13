<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.12	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>시스템관리 :: 사용자 관리 :: 부서DB권한관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/system/userMng/departmentDBAuth.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
			<span class="h3">부서DB권한 관리</span>
			<ul>
                 		<li><a href="/"><img src="/resources/images/ico_home.gif" class="homeimg" alt="home"></a></li>
                 		<li> > 시스템 > <a href="/System/UserMng/User">사용자 관리</a> > <span class="linkN">부서DB권한 관리</span></li>							
			</ul>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:790px;">
			<div data-options="region:'west',border:false" style="width:55%;padding:5px;">
				<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶부서 리스트</span></div>
				<div class="easyui-panel" data-options="border:false" style="min-height:700px;margin-bottom:10px;">
					<table id="deptList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px">
				<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ DB권한 추가</span></div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<input type="hidden" id="dept_cd" name="dept_cd"/>
					<div class="easyui-panel" data-options="border:false" style="height:330px;">
						<table id="dbAuthList" class="detailT click" style="margin-left:0px;margin-top:0px;">
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
									<th>권한시작일자</th>
									<th>권한종료일자</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div class="searchBtn innerBtn2" style="display:block;">
						<a href="javascript:;" class="w120 easyui-linkbutton" data-options="iconCls:'icon-save'" onClick="Btn_SaveDeptDBAuth();">저장</a>
					</div>
				</form:form>
				<div class="dtl_title" style="clear:both;"><span id="histTitle" class="h3" style="margin-left:0px;">▶ DB권한 이력</span></div>
				<form:form method="post" id="history_form" name="history_form" class="form-inline">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="dept_cd" name="dept_cd"/>
					<div class="easyui-panel" data-options="border:false" style="height:330px">							
						<table id="dbAuthHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
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