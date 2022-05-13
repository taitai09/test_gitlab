<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
    <script type="text/javascript" src="/resources/js/ui/indexDesignMaintenance/loadIndexDesign.js?ver=<%=today%>"></script>
	<!-- 성능개선 - 인덱스설계/정비 - 인덱스 자동 설계  팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/indexAutoDesign_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->

</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${odsTables.dbid}"/>
					<input type="hidden" id="owner" name="owner"/>
					<input type="hidden" id="table_owner" name="table_owner"/>
					<input type="hidden" id="file_no" name="file_no" value="${odsTables.file_no}"/>
					<input type="hidden" id="exec_seq" name="exec_seq" value="${odsTables.exec_seq}"/>
					<input type="hidden" id="table_name" name="table_name"/>
					<input type="hidden" id="access_path" name="access_path"/>
					<input type="hidden" id="explain_exec_seq" name="explain_exec_seq"/>
					<input type="hidden" id="query_seq" name="query_seq"/>
					<input type="hidden" id="access_path_type" name="access_path_type" value="DBIO"/>
					<input type="hidden" id="access_path_value" name="access_path_value" value="${odsTables.access_path_value}"/>
					<input type="hidden" id="indexColumnArry" name="indexColumnArry"/>
					<input type="hidden" id="selected_table_name" name="selected_table_name"/>
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${odsTables.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${odsTables.pagePerCount}"/>

					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
						<label>파일번호</label>
						<select id="selectFileNo" name="selectFileNo"  data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
						<label>파일명</label>
						<input type="text" id="file_nm" name="file_nm" data-options="disabled:true" class="w200 easyui-textbox"/>								
						<label>파싱순번</label>
						<select id="selectExecSeq" name="selectExecSeq"  data-options="editable:false" class="w100 easyui-combobox" required="true"></select>
						<label>SQL수</label>
						<input type="text" id="analysis_sql_cnt" name="analysis_sql_cnt" data-options="disabled:true" class="w80 easyui-textbox"/>
						<label>파싱일시</label>
						<input type="text" id="access_path_exec_dt" name="access_path_exec_dt" data-options="disabled:true" class="w150 easyui-textbox"/>
					</div>
					<div class="multi">
						<label>OWNER</label>
						<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w150 easyui-combobox" required="true"></select>												
						<label>TABLE</label>
						<input type="text" id="selectTableName" name="selectTableName" data-options="readonly:false" class="w120 easyui-textbox"/>
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>							
					</div>
					<div class="searchBtn multiBtn">
						<a href="javascript:;" class="w140 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 인덱스설계 엑셀</a>
					</div>							
				</div>
			</div>
			<div id="accessPathTabs" class="easyui-tabs" data-options="border:false" style="width:100%;height:640px">
				<div title="테이블 목록" style="padding:10px;">
					<div class="easyui-layout" data-options="fit:true,border:false">
						<div data-options="region:'west',split:false,collapsible:false,border:false" style="padding-left:5px;padding-right:5px;width:98%;height:100%">
							<table id="odstableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								<tbody><tr></tr></tbody>
							</table>
						</div>
						<div class="searchBtn" data-options="region:'south',collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
							<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
							<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						</div>
					</div>
				</div>
				<div title="테이블  상세정보" style="padding-left:5px;padding-right:5px;padding-top:5px;">
					<div class="easyui-layout" data-options="fit:true,border:false">
						<div data-options="region:'north',border:false" style="height:59%">
							<div class="easyui-layout" data-options="fit:true,border:false">
								<div data-options="region:'west',split:false,collapsible:false,border:false" style="padding-left:5px;padding-right:5px;width:50%;height:100%">
									<div class="tbl_title" style="margin-top:-1px;margin-bottom:3px;"><span class="h3">※ 컬럼 정보</span></div>
									<table id="columnList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:90%;">
									</table>
								</div>
								<div data-options="region:'center',border:true" style="border-right:0px;border-top:0px;border-bottom:0px;"></div>
								<div data-options="region:'east',split:false,collapsible:false,border:false" style="padding-left:5px;padding-right:5px;width:49.5%;height:100%">
									<div class="tbl_title" style="margin-top:-1px;margin-bottom:3px;"><span class="h3">※Access Path 현황</span></div>
									<table id="accPathList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:90%;">
									</table>
								</div>
							</div>
						</div>
						<div data-options="region:'center',border:true" style="border-left:0px;border-right:0px;border-bottom:0px;"></div>
						<div data-options="region:'south',border:false" style="height:40.5%">
							<div class="easyui-layout" data-options="fit:true,border:false">
								<div data-options="region:'west',collapsible:false,split:false,border:false" style="padding-left:5px;padding-right:5px;width:50%;height:100%">
									<div class="tbl_title" style="margin-top:-1px;margin-bottom:3px;"><span class="h3">※인덱스 현황</span></div>
									<table id="indexsList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:85.5%;">
									</table>
								</div>
								<div data-options="region:'center',border:true" style="border-right:0px;border-top:0px;border-bottom:0px;"></div>
								<div data-options="region:'east',collapsible:false,split:false,border:false" style="padding-left:5px;padding-right:5px;width:49.5%;height:100%">
									<div class="tbl_title" style="margin-top:-1px;margin-bottom:3px;"><span class="h3">※인덱스 설계</span></div>
									<div class="easyui-layout" data-options="border:false" style="width:98.2%;height:90%;">
										<div data-options="region:'center',border:false">
											<table id="indexDesignList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
										<div data-options="region:'south',collapsible:false,split:false,border:false" style="width:100%;height:40px;padding:3px;">
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showIndexAutoDesign();"><i class="btnIcon fab fa-modx fa-lg fa-fw"></i> 인덱스 자동설계</a>
											<div style="float:right;">
												<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_AddRow();"><i class="btnIcon fas fa-plus fa-lg fa-fw"></i> ADD ROW</a>
												<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_DeleteRow();"><i class="btnIcon fas fa-minus fa-lg fa-fw"></i> REMOVE ROW</a>
												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveIndexDesign();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
											</div>
										</div>
									</div>									
								</div>
							</div>
						</div>
					</div>
				</div>
				<div title="SQL 현황" style="padding:10px">
					<table id="sqlTextList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</form:form>								
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/indexAutoDesign_popup.jsp" %><!-- 성능개선 - 인덱스설계/정비 - 인덱스 자동 설계  팝업 -->
</body>
</html>