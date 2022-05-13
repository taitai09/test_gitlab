<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>

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
	<script type="text/javascript" src="/resources/js/ui/dashboard/dev.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging2.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		#dash_contents .wtitle {
			margin-top: 4px;
			float: left;
		}
		#dash_contents .w_body {
			width: 100%;
			height: 83%;
		}
		#dash_contents .searchBtn {
			margin-top: 5px;
			margin-right: 4px;
		}
	</style>
</head>
<body>
<div id="container" style="height:97.8%;">
	<div id="dash_contents" style="height:100%;">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline" style="height:89%;">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="tuning_no" name="tuning_no"/>
			<input type="hidden" id="guide_no" name="guide_no"/>
			<input type="hidden" id="auth_cd" name="auth_cd" value="${auth_cd}">
			<input type="hidden" id="leader_yn" name="leader_yn" value="${leader_yn}">
			<input type="hidden" id="sql_std_qty_div_cd" name="sql_std_qty_div_cd" value="${sql_std_qty_div_cd}">
			<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${wrkjob_cd}"/>
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
			<input type="hidden" id="currentPage2" value="1">
			<input type="hidden" id="rcount" value="10">
		
			<div id="staticDiv" style="height:760px;">
				<div class="full_widget" style="height:39.6%;margin-top:0px;">
					<div class="w_title_area clearfix">
						<span class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> MY WORKLIST</span>
						<div style="float: right;">
							<label>검색건수</label>
							<select id="searchCount" name="selectRcount" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto'">
								<option value="10" selected>10</option>
								<option value="20" >20</option>
								<option value="40">40</option>
								<option value="60">60</option>
								<option value="80">80</option>
								<option value="100">100</option>
							</select>
						</div>
					</div>
					<div class="w_body">
						<table id="myworkList" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="width: 100%;height: 100%;">
						</table>
					</div>
				</div>
				<div class="searchBtn" data-options="region:'south',split:false,collapsible:false,border:false">
					<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
				
				<div class="full_widget" style="height:220px;">
					<div class="w_title_area">
						<span class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> SQL 튜닝 가이드</span>
					</div>
					<div class="w_body">
						<table id="precedentList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
				<div class="searchBtn pagingBtn" data-options="region:'south',split:false,collapsible:false,border:false" style="margin-right: 4px;">
					<a href="javascript:;" id="prevBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn2" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 5px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
		<form:form method="post" id="submit_form2" name="submit_form" class="form-inline">
			<input type="hidden" id="currentPage_sub" name="currentPage" value="1">
			<input type="hidden" name="pagePerCount" value="10">
		</form:form>
		
		<div class="variableDiv" style="height:32%;">
			<div class="full_widget" style="height:200px;">
				<div class="w_title_area">
					<span class="wtitle"><i class="fas fa-check-circle fa-lg fa-fw"></i> SQL 표준 점검</span>
				</div>
				<div class="w_body">
					<table id="sqlStdChkList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		<div>
	</div> <!-- dash_contents END -->
</div> <!-- container END -->
</body>
</html>