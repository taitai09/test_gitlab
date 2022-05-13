<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 *
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: OPEN-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/dashboard/dashboard.js?ver=<%=today%>"></script>
	<script>
		var maxCheckDay = '${max_check_day}';	
		var maxCheckDayDash = '${max_check_day_dash}';	
		var maxGatherDay = '${max_gather_day}';	
		var maxGatherDayDash = '${max_gather_day_dash}';
		var todayTxt = '${today_txt}';	
		var maxBaseDay = '${max_base_day}';
	</script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<div id="dash_contents">
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:250px;min-height:230px">
					<!-- DB점검현황 -->
					<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:300px;height:100%;padding:5px;">
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%">
							<div id="dbCheckResultChart" title="" style="width:100%;height:100%;padding:0px;">
							</div>
						</div>
					</div>
					<!-- 긴급조치대상현황 -->
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:30%;height:100%;padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%">
							<div id="urgentActionTargetCondChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
<!-- 					성능개선작업현황 -->
<!-- 					<div data-options="region:'east',split:false,collapsible:false,border:false" style="width:33%;height:100%;padding:5px;"> -->
<!-- 						<div class="easyui-panel" data-options="border:true" style="width:100%;height:230px"> -->
<!-- 							<div id="perfImprWorkCondChart" title="" style="width:100%;height:100%;padding-top:0px;"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
					<!-- 튜닝 미완료 -->
					<div data-options="region:'east',split:false,collapsible:false,border:false" style="width:45%;height:100%;padding:5px;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
							<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:50%;height:100%;padding:5px;">
								<div style="font-size:12px;padding:5px 0px;font-weight:bold;font-face:굴림체">성능개선작업현황</div>
								<table id="perfImprCondition" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
								</table>						
							</div>
							<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:50%;height:100%;">
								<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;padding:0px 5px;">
									<div id="incompleteTuningChart" title="" style="width:100%;height:100%;padding-top:0px;">
									</div>
								</div>
							</div>
						</div>
					</div>					
				</div>

				<div class="easyui-layout" data-options="border:false" style="width:100%;height:250px;min-height:230px">

					<!-- SQL성능진단 -->
					<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:500px;height:100%;padding:5px;">
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%">
							<div id="sqlPerfCheckChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
				
					<!-- APP성능진단 -->
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:60.0%;padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%">
							<div id="appPerfCheckChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
					
								
				</div>
	
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:250px;min-height:230px">

					<!-- Object변경현황 -->
					<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:400px;padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%">
							<div id="objectChangeCondChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
				
					<!-- Reorg대상현황(TOP10DB) -->
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:33%;padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%">
							<div id="reorgTargetCondChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
					
					
					<!-- 자원한계예측현황 -->
					<div data-options="region:'east',collapsible:false,split:false,border:false" style="width:33%;padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%">
							<div id="resourceLimitPredictChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>									
					</div>				
								
				</div>

		</div>
	</form:form>
	<!-- dash_contents END -->
</div>
<!-- container END -->
</body>
</html>