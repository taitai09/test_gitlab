<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>수동 선정 :: 수동선정현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlTuningTarget/manualSelectionStatus.js?ver=<%=today%>"></script>    
	<!-- 자동/수정 선정현황  일괄튜닝종료 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/batchTuning_popup.js?ver=<%=today%>"></script>

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
					<input type="hidden" id="dbid" name="dbid" value="${topsqlHandopChoice.dbid}"/>
					<input type="hidden" id="start_snap_id" name="start_snap_id" value="${topsqlHandopChoice.start_snap_id}"/>
					<input type="hidden" id="end_snap_id" name="end_snap_id" value="${topsqlHandopChoice.end_snap_id}"/>
					<input type="hidden" id="elapsed_time" name="elapsed_time" value="${topsqlHandopChoice.elapsed_time}"/>
					<input type="hidden" id="buffer_gets" name="buffer_gets" value="${topsqlHandopChoice.buffer_gets}"/>
					<input type="hidden" id="executions" name="executions" value="${topsqlHandopChoice.executions}"/>
					<input type="hidden" id="topn_cnt" name="topn_cnt" value="${topsqlHandopChoice.topn_cnt}"/>
					<input type="hidden" id="selectOrdered" name="selectOrdered" value="${topsqlHandopChoice.selectOrdered}"/>
					<input type="hidden" id="choice_tms" name="choice_tms"/>
					<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="2"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w150 easyui-combobox"></select>
					<label>선정일자</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${topsqlHandopChoice.strStartDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${topsqlHandopChoice.strEndDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="padding-left:5px;min-height:280px">
			<table id="dtlTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn innerBtn2" style="margin-top:10px;">
<!-- 			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 돌아가기</a> -->
			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="endBatchTuning();"><i class="btnIcon fas fa-stop-circle fa-lg fa-fw"></i> 일괄 튜닝종료</a>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/batchTuning_popup.jsp" %> <!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 -->
</body>
</html>