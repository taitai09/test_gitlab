<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>DB 상태 점검 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/css/function/dailyCheckDb.css">
	<script type="text/javascript" src="/resources/js/ui/dailyFullCheck/minuteDbStatus.js?ver=<%=today%>"></script>
	<style type="text/css">
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="minute_form" name="minute_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid" value="${dbid}"/>
			<input type="hidden" id="check_day" name="check_day" value="${check_day}"/>
			<input type="hidden" id="check_seq" name="check_seq" value="${check_seq}"/>
			<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
			
			<input type="hidden" id="db_status_name" name="db_status_name" value="${db_status_name}"/>
			<input type="hidden" id="severity_color_0" name="severity_color_0" value="${severity_color_0}"/>
			<input type="hidden" id="severity_color_1" name="severity_color_1" value="${severity_color_1}"/>
			<input type="hidden" id="severity_color_2" name="severity_color_2" value="${severity_color_2}"/>
			<input type="hidden" id="severity_color_3" name="severity_color_3" value="${severity_color_3}"/>
			<input type="hidden" id="db_status_tabs_severity" name="db_status_tabs_severity" value="${db_status_tabs_severity}"/>

<!-- <div id="dailyCheckDb" class="easyui-layout" data-options="border:false" style="width:100%;height:100px;"> -->
<!-- <div id="dailyCheckDb" class="easyui-layout" data-options="border:false" style="width:100%;height:800px;"> -->

<div id="dailyCheckDb" class="easyui-panel" data-options="border:false" style="width:100%;">

<!-- 	<div data-options="border:false" style="width:100%;height:730px;"> -->
<!-- 	<div data-options="border:false" style="width:1680px;height:100%"> -->
<!-- 	<div data-options="border:false" style="width:100%;height:730px;"> -->
	<div data-options="border:false" style="width:100%;height:690px;">
		<div id="statusTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
<!-- 			<div id="db" title="TEST"> -->
<!-- 				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;"> -->
<!-- 					<div id="object_title" class="title_1"></div> -->
<%-- 					<canvas id="object_canvas" width="1500" height="70"></canvas> --%>
<!-- 				</div> -->
<!-- 				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;"> -->
<!-- 					<div id="minute_title" class="title_1"></div> -->
<!-- 					<div id="minute_area" class="easyui-panel" data-options="border:true" style="width:clac(100% - 10px);height:460px;"></div> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<div id="db" title="DB">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_db" class="title_1"></div>
					<canvas id="canvas_db" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ DATABASE 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="instance" title="INSTANCE">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_instance" class="title_1"></div>
					<canvas id="canvas_instance" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ INSTANCE 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="space" title="SPACE">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_space" class="title_1"></div>
					<canvas id="canvas_space" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ SPACE 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="object" title="OBJECT">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_object" class="title_1"> </div>
					<canvas id="canvas_object" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ OBJECT 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="statistics" title="STATISTICS">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_statistics" class="title_1"></div>
					<canvas id="canvas_statistics" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ STATISTICS 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="longrunningwork" title="LONG RUNNING WORK">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_longrunningwork" class="title_1"></div>
					<canvas id="canvas_longrunningwork" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ LONG RUNNING WORK 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
				</div>
			</div>
			<div id="alert" title="ALERT">
				<div id="object" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="title_alert" class="title_1"></div>
					<canvas id="canvas_alert" width="1500" height="70"></canvas>
				</div>
				<div id="minute" class="easyui-panel" data-options="border:false" style="width:100%;padding:20px 0px 0px 0px;">
					<div id="minute_title" class="title_1">
						<label class='minute_title_1'>▉ ALERT 점검결과 상세</label>
					</div>
					<div id="minute_area" class="easyui-panel" data-options="border:false" style="width:clac(100% - 10px);height:460px;"></div>
<!-- 					<div id="minute_area" style="width:clac(100% - 10px);height:460px;overflow:auto"></div> -->
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