<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.15	이원식	권한별 dashboard 분리 (DBA)
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
	<script type="text/javascript" src="/resources/js/ui/dashboard/dbmanager.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	
	<style>
		/*.tabs-panels .panel .panel-htop .datagrid-view2 td{cursor:default;}*/	
	</style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- dash_contents START -->
	<div id="dash_contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="tuning_no" name="tuning_no"/>
			<input type="hidden" id="guide_no" name="guide_no"/>
			<input type="hidden" id="emergency_action_no" name="emergency_action_no"/>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="gather_day" name="gather_day" value="${gather_day}"/>

			<!-- 긴급조치대상 링크를 처리하기 위해 추가 2018-06-27 -->
			<input type="hidden" id="strStartDt" name="strStartDt" value=""/>
			<input type="hidden" id="check_item_name" name="check_item_name" value=""/>
			<input type="hidden" id="check_day" name="check_day" value=""/>
			<input type="hidden" id="check_seq" name="check_seq" value=""/>
			
		</form:form>
<!-- 		<div class="widgetL firstW"> -->
<!-- 			<div class="w_title_area"> -->
<!-- 				<div class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> 성능 개선 유형 현황</div> -->
<!-- 			</div> -->
<!-- 			<div id="improvementsChart" class="w_body" style="height:85%"> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="widgetR firstW"> -->
<!-- 			<div class="w_body" style="height:99%"> -->
<!-- 				<div id="requestTab" class="easyui-tabs" data-options="fit:true,border:false"> -->
<!-- 					<div id="tunerJobChart" title="튜닝 진행" style="padding-top:5px;">						 -->
<!-- 					</div> -->
<!-- 					<div title="튜닝 요청 현황" style="padding-top:5px;"> -->
<!-- 						<div class="easyui-layout" data-options="fit:true,border:false"> -->
<!-- 							<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:35px;"> -->
<!-- 								<span class="searchBtnLeft"> -->
<!-- 									<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showTuningAssignAllPopup();"><i class="btnIcon fas fa-street-view fa-lg fa-fw"></i> 튜닝담당자 지정</a> -->
<!-- 								</span> -->
<!-- 							</div> -->
<!-- 							<div data-options="region:'center',split:false,collapsible:false,border:false"> -->
<!-- 								<table id="tuningRequestList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 								</table> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="full_widget">
			<div class="w_body" style="height:99%">
				<div id="checkTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div id="preventionChart" title="DB 예방 점검 현황(${max_gather_day})" style="padding-top:5px;"> </div>
					<div id="riskDiagnosisChart" title="성능 리스크 진단 현황(${max_gather_day})" style="padding-top:5px;"> </div>
					<div id="tuningProgressChart" title="튜닝 진행 목록" style="padding-top:5px;"> </div>
					<div title="튜닝 지연 목록" style="padding-top:5px;cursor:pointer;">
						<table id="tuningDelayList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>					
				</div>
			</div>
		</div>
		<div class="full_widget">
			<div class="w_body" style="height:99%">
				<div id="objectTab" class="easyui-tabs" data-options="fit:true,border:false">
					<div title="긴급 조치 대상(${max_gather_day})" style="padding-top:5px;cursor:pointer;">
						<table id="urgentActionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="objectChangeChart" title="오브젝트 변경 현황(최근 일주일)" style="padding-top:5px;">
					</div>
				</div>
			</div>
		</div>
		
		<div class="full_widget">
			<div class="w_body" style="height:99%">
				<div id="requestTab" class="easyui-tabs" data-options="fit:true,border:false">
<!-- 					<div id="tunerJobChart" title="튜너별할당현황" style="padding-top:5px;">						 -->
<!-- 					</div> -->
					<div title="튜너미할당요청목록" style="padding-top:5px;">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:35px;">
								<span class="searchBtnLeft">
									<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showTuningAssignAllPopup();"><i class="btnIcon fas fa-street-view fa-lg fa-fw"></i> 튜닝담당자 지정</a>
								</span>
							</div>
							<div data-options="region:'center',split:false,collapsible:false,border:false">
								<table id="tuningRequestList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
						</div>
					</div>
					<div id="tunerJobChart" title="튜너별할당현황" style="padding-top:5px;">
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- dash_contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/tunerAssign_popup.jsp" %> <!-- 튜닝담당자 지정 팝업 -->
</body>
</html>