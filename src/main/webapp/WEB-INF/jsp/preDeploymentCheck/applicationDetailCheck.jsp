<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>애플리케이션 성능 점검 - 상세내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/preDeploymentCheck/applicationDetailCheck.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="selectValue" name="selectValue" value="${deployPerfCheck.selectValue}"/>
					<input type="hidden" id="strStartDt" name="strStartDt" value="${deployPerfCheck.strStartDt}"/>							
					<input type="hidden" id="strEndDt" name="strEndDt" value="${deployPerfCheck.strEndDt}"/>
					<input type="hidden" id="perf_check_yn" name="perf_check_yn" value="${deployPerfCheck.perf_check_yn}"/>
					<input type="hidden" id="deploy_perf_check_no" name="deploy_perf_check_no" value="${deployPerfCheck.deploy_perf_check_no}"/>
					<input type="hidden" id="prohibit_hint_use_yn" name="prohibit_hint_use_yn" value="${deployPerfCheck.prohibit_hint_use_yn}"/>
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${deployPerfCheck.wrkjob_cd}"/>
					<input type="hidden" id="tr_cd" name="tr_cd" value="${deployPerfCheck.tr_cd}"/>
					<label>애플리케이션명</label>
					<input type="text" id="lst_tr_cd" name="lst_tr_cd" value="${deployPerfCheck.lst_tr_cd}" class="w300 easyui-textbox"/>
					<select id="perf_fitness_yn" name="perf_fitness_yn" data-options="panelHeight:'auto',editable:false" class="w130 easyui-combobox">
						<option value="A">전체</option>
						<option value="" <c:if test="${deployPerfCheck.perf_fitness_yn eq ''}">selected</c:if>>미점검</option>
						<option value="Y" <c:if test="${deployPerfCheck.perf_fitness_yn eq 'Y'}">selected</c:if>>접합</option>
						<option value="N" <c:if test="${deployPerfCheck.perf_fitness_yn eq 'N'}">selected</c:if>>부적합</option>
					</select>
					<label>금지힌트사용</label>
					<input type="checkbox" id="chkProhibitUseYn" name="chkProhibitUseYn" class="easyui-switchbutton"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn innerBtn" style="margin-top:10px;margin-bottom:0px;">
			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="goApplicationCheck();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 돌아가기</a>
		</div>				
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>