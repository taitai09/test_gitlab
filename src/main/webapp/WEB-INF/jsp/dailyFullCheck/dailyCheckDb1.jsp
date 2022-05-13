<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<%
/***********************************************************
 * 2020.03.17	명성태	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일 종합 진단~</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/css/function/dailyCheckDb.css">
	<script type="text/javascript" src="/resources/js/ui/dailyFullCheck/dailyCheckDb.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			
			<input type="hidden" id="severity_1" name="severity_1"/>
			<input type="hidden" id="severity_2" name="severity_2"/>
			<input type="hidden" id="severity_3" name="severity_3"/>
			<input type="hidden" id="severity_4" name="severity_4"/>
			<input type="hidden" id="check_day" name="check_day"/>
			<input type="hidden" id="check_seq" name="check_seq"/>
			<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
			<input type="hidden" id="choice_db_group_id" name="choice_db_group_id"/>
			<input type="hidden" id="choice_severity_id" name="choice_severity_id"/>
			<input type="hidden" id="check_day" name="check_day"/>
			<input type="hidden" id="check_seq" name="check_seq"/>
			
<!-- 			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px"> -->
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:670px;">
				<div id="dailyCheckDbTabs" class="easyui-tabs" data-options="plain:false,fit:true,border:false">
					<div title="Db 상태 점검" style="padding:5px;min-height:300px;overflow:auto;">
<!-- 					<div title="Db 상태 점검" style="padding:5px;"> -->
						<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
							<div class="well">
								<div>
									<div class="severity_1"></div>
									<span style="padding-right:10px;">
										<label for="severity_1">긴급조치</label>
									</span>
									<div class="severity_2"></div>
									<span style="padding-right:10px;">
										<label for="severity_2">조치필요</label>
									</span>
									<div class="severity_3"></div>
									<span style="padding-right:10px;">
										<label for="severity_3">확인필요</label>
									</span>
									<div class="severity_4"></div>
									<span style="padding-right:10px;">
										<label for="severity_4">정상</label>
									</span>
									<label>DB 그룹</label>
									<select id="perfDbGroupCombo" name="perfDbGroupCombo" data-options="panelHeight:'auto',editable:false" class="w130 easyui-combobox"></select>
									<label>심각도</label>
									<select id="perfSeverityCombo" name="perfSeverityCombo" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>
									<span class="searchBtnLeft">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
									</span>
									<div class="searchBtn excelBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="" onClick="fnOpenExceptionManageTab();">예외 관리</a></div>
								</div>
							</div>
						</div>
<!-- 						<div class="easyui-panel" data-options="border:false" style="width:100%;;main-height:400px;padding:5px;overflow-y:auto;"> -->
						<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:100px;padding:5px;overflow-y:auto;">
							<div>
								<label style="font:Bold 18px Open Sans;">OPENPOP</label>
							</div>
<%-- 							<canvas id="canvas1" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas2" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas3" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas4" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
							
<%-- 							<canvas id="canvas11" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas22" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas33" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
<%-- 							<canvas id="canvas44" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"> --%>
<%-- 							</canvas> --%>
							
							<canvas id="canvas1" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas2" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas3" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas4" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							
							<canvas id="canvas11" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas22" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas33" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
							<canvas id="canvas44" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;">
							</canvas>
						</div>
						<div class="easyui-panel" data-options="border:false" style="width:100%;;min-height:340px;padding:5px;">
							<div id="canvas" style="width:700px;heigh:300px;">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>