<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.04.18	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>일 예방 점검 예외 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/preventiveCheck/exceptionManagement.js?ver=<%=today%>"></script>   
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${dbCheckException.dbid}"/>
		<input type="hidden" id="check_items" name="check_items" value="Y"/>
		<input type="hidden" id="check_seq" name="check_seq"/>
		<input type="hidden" id="check_item_name" name="check_item_name"/>
		
		<input type="hidden" id="check_pref_id" name="check_pref_id"/>
		
		<input type="hidden" id="check_except_object_index" name="check_except_object_index"/>
		<input type="hidden" id="check_except_object_name" name="check_except_object_name"/>
		<input type="hidden" id="db_check_exception_no" name="db_check_exception_no"/>
		
		<!-- 이전, 다음 처리 -->
		<input type="hidden" id="currentPage" name="currentPage" value="${dbCheckException.currentPage}"/>
		<input type="hidden" id="pagePerCount" name="pagePerCount" value="${dbCheckException.pagePerCount}"/>

		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">						
					<label>DB</label>
					<select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>점검항목</label>
					<select id="selectCheckItem" name="selectCheckItem" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox"></select>
					<label>검색조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>					
					<input type="text" id="searchValue" name="searchValue" value="" class="w200 easyui-textbox" data-options="panelHeight:'auto',editable:true"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn excelBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-xls'" onClick="Excel_Download();">엑셀</a></div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px">
				<div data-options="region:'center',border:false">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="deleteExceptionBtn" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 예외삭제</a>
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>				
		</div>
	</form:form>			
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>