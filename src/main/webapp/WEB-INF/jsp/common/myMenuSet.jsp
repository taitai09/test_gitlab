<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<%
	/***********************************************************
	 * 2018.07.03	bks	OPENPOP V2 최초작업
	 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>My Menu 설정</title>
<meta charset="utf-8" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="cleartype" content="on" />
<%@include file="/WEB-INF/jsp/include/sub_include.jsp"%>
<script type="text/javascript" src="/resources/js/lib/treegrid-dnd.js?ver=<%=today%>"></script>

<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
<script type="text/javascript" src="/resources/js/ui/common/myMenuSet.js?ver=<%=today%>"></script>
</head>
<body>
	<!-- container START -->
	<div id="container">
		<!-- contents START -->
		<div id="contents">
			<div class="easyui-panel" data-options="border:false" style="width: 100%">
				<div class="title paddingT5">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="user_id" name="user_id" value="${user_id}" />
					<input type="hidden" id="dbid" name="dbid" />
					<input type="hidden" id="global_view_yn" name="global_view_yn" value="Y" />
					<input type="hidden" id="parsingSchemaArry" name="parsingSchemaArry" />
					<input type="hidden" id="moduleNameArry" name="moduleNameArry" />
					<input type="hidden" id="modCnt" name="modCnt" value="1" />
				</form:form>
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="user_id" name="user_id" value="${user_id}" />
				</form:form>				
			</div>
			<div class="easyui-panel" data-options="border:false" style="min-height: 650px; margin-bottom: 10px;">
				<div class="easyui-layout" data-options="border:false" style="width: 95%; min-height: 650px">
					<div class="easyui-panel" data-options="region:'west',border:true" style="width: 46%; min-height: 650px">
						<div class="dtl_title2"><span id="subTitle" class="h3" style="margin:15px;">▶ My Menu 선택 목록</span></div>
						<ul id="authMenuList" class="easyui-tree" checkbox="true">
						</ul>
					</div>
<!-- 					<div data-options="region:'center', border:false"> -->
<!-- 						<div -->
<!-- 							style="width: 100%; text-align: center; margin-top: 150px;"> -->
<!-- 							<a href="javascript:;" class="easyui-linkbutton" onClick="Btn_AddApplyTarget();"><i class="btnIcon fas fa-angle-right fa-lg fa-fw"></i></a> -->
<!-- 						</div> -->
<!-- 						<div style="width: 100%; text-align: center; margin-top: 30px;"> -->
<!-- 							<a href="javascript:;" class="easyui-linkbutton" onClick="Btn_RemoveApplyTarget();"><i class="btnIcon fas fa-angle-left fa-lg fa-fw"></i></a> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="easyui-panel" data-options="region:'east',border:false" style="width: 46%; min-height: 650px"> -->
<!-- 						<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ My Menu 목록</span></div> -->
<!-- 						<ul id="myMenuList" class="easyui-tree" checkbox="true"> -->
<!-- 						</ul> -->
<!-- 					</div> -->
				</div>
				<div data-options="region:'south',border:false" style="height: 120px;padding-left:600px;">
					<div class="searchBtnLeft innerBtn2 marginR5">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_AddApplyTarget();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 저장</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>