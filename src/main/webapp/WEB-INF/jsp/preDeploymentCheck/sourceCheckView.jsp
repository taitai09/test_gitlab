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
	<title>소스점검 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/preDeploymentCheck/sourceCheckView.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">			
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
		</div>	
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-botton:10px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="selectValue" name="selectValue" value="${beforeAccidentCheck.selectValue}"/>
				<input type="hidden" id="bfac_chk_no" name="bfac_chk_no" value="${result.bfac_chk_no}"/>
				<input type="hidden" id="strStartDt" name="strStartDt" value="${beforeAccidentCheck.strStartDt}"/>
				<input type="hidden" id="strEndDt" name="strEndDt" value="${beforeAccidentCheck.strEndDt}"/>
				<input type="hidden" id="tuning_no" name="tuning_no" value="${result.tuning_no}"/>
				<input type="hidden" id="gubun" name="gubun" value="Pre"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:15%;">
						<col style="width:85%;">
					</colgroup>								
					<tr>
						<th>튜닝번호</th>
						<td>
							${result.tuning_no}&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="Btn_AddTabTuningHistory();">튜닝상세정보</a>
						</td>
					</tr>
					<tr>
						<th>점검대상 소스(SQL)</th>
						<td><textarea name="bfac_chk_source" id="bfac_chk_source" rows="30" style="margin-top:5px;width:98%;height:250px" readonly>${result.bfac_chk_source}</textarea></td>
					</tr>
					<tr>
						<th>점검 결과</th>
						<td><textarea name="bfac_chk_result_sbst" id="bfac_chk_result_sbst" rows="20" style="padding:10px;IME-MODE:active;">${result.bfac_chk_result_sbst}</textarea></td>
					</tr>
				</table>
				<div class="dtlBtn">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>						
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_Complete();"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> 점검 완료</a>
				</div>				
			</form:form>
		</div>
	</div>
	<!-- contents END -->			
</div>
<!-- container END -->
</body>
</html>