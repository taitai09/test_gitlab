<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.09.28	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>수집SQL 인덱스 정비</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesign/collectSQL/collectionIndexUsage.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		input[type="text"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 23px !important;
		    line-height: 23px !important;
		}
		input[type="checkbox"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 20px !important;
		    line-height: 23px !important;
		}	
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="owner" name="owner"/>
					<input type="hidden" id="table_owner" name="table_owner"/>
					<input type="hidden" id="exec_seq" name="exec_seq"/>
					<input type="hidden" id="table_name" name="table_name"/>
					<input type="hidden" id="access_path" name="access_path"/>
					<input type="hidden" id="usage_yn" name="usage_yn" value="N"/>
					<input type="hidden" id="pk_yn" name="pk_yn" value="N"/>
					<input type="hidden" id="access_path_type" name="access_path_type" value="VSQL"/>
					<input type="hidden" id="start_snap_no" name="start_snap_no" value=""/>
					<input type="hidden" id="end_snap_no" name="end_snap_no" value=""/>
					
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${odsIndexs.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${odsIndexs.pagePerCount}"/>

					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
						<label>파싱순번</label>
						<select id="selectExecSeq" name="selectExecSeq" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox" required="true"></select>
						<label>수집일시</label>
						<input type="text" id="strStartDt" name="strStartDt" data-options="readonly:true" class="w120 easyui-textbox"/> ~
						<input type="text" id="strEndDt" name="strEndDt" data-options="readonly:true" class="w120 easyui-textbox"/>
						<label>SQL수</label>
						<input type="text" id="analysis_sql_cnt" name="analysis_sql_cnt" data-options="readonly:true" class="w70 easyui-textbox"/>
						<label>파싱일시</label>
						<input type="text" id="access_path_exec_dt" name="access_path_exec_dt" data-options="readonly:true" class="w120 easyui-textbox"/>
					</div>
					<div class="multi">
						<label>OWNER</label>
						<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w150 easyui-combobox" required="true"></select>
						<label>TABLE</label>
						<input type="text" id="selectTableName" name="selectTableName" data-options="readonly:false" class="w120 easyui-textbox"/>
						<label>미사용 인덱스</label>
						<input type="checkbox" id="chkUsage" name="chkUsage" class="easyui-switchbutton"/>
						<label>PK 제외</label>
						<input type="checkbox" id="chkPk_yn" name="chkPk_yn" class="easyui-switchbutton"/>
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:530px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
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