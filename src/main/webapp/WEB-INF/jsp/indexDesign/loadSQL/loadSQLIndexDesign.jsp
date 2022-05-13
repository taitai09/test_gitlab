<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.22	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>적재SQL 인덱스 설계</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />    
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/loadSQL.js?ver=<%=today%>"></script>				<!-- SQL 적재 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadSQL_popup.js?ver=<%=today%>"></script>				<!-- 성능개선 - 인덱스 설계 전처리 - SQL 적재 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/loadActionPlan.js?ver=<%=today%>"></script>		<!-- 적재SQL 실행 계획 생성 -->
	<script type="text/javascript" src="/resources/js/paging_variable.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/parseLoadingCondition.js?ver=<%=today%>"></script>	<!-- 적재SQL 조건절 파싱 -->
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/autoLoadIndexDesign.js?ver=<%=today%>"></script>	<!-- 적재SQL 인덱스 자동설계 -->
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/loadIndexDesign.js?ver=<%=today%>"></script>		<!-- 적재SQL 인덱스 설계 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/indexAutoDesign_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/indexDesign/loadSQL/loadIndexUsage.js?ver=<%=today%>"></script>		<!-- 적재SQL 인덱스 정비 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="loadSQLIndexDesignTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
				<div title="SQL 적재" style="padding:5px;">
					<form:form method="post" id="loadSQLMain_form" name="loadSQLMain_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/loadSQL.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="적재SQL 실행 계획 생성" style="padding:5px;">
					<form:form method="post" id="loadActionPlan_form" name="loadActionPlan_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/loadActionPlan.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="적재SQL 조건절 파싱" style="padding:5px;">
					<form:form method="post" id="parseLoadingCondition_form" name="parseLoadingCondition_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/parseLoadingCondition.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="적재SQL 인덱스 자동설계" style="padding:5px;">
					<form:form method="post" id="autoLoadIndexDesign_form" name="autoLoadIndexDesign_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/autoLoadIndexDesign.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="적재SQL 인덱스 설계" style="padding:5px;">
					<form:form method="post" id="loadIndexDesign_form" name="loadIndexDesign_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/loadIndexDesign.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="적재SQL 인덱스 정비" style="padding:5px;">
					<form:form method="post" id="loadIndexUsage_form" name="loadIndexUsage_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/indexDesign/loadSQL/loadIndexUsage.jsp"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/indexAutoDesign_popup.jsp" %> <!-- 성능개선 - 인덱스 설계 전처리 - SQL 적재 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/loadSQL_popup.jsp" %> <!-- 성능개선 - 인덱스 설계 전처리 - SQL 적재 팝업 -->
</body>
</html>