<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.04.22	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/board/noticeList.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

				</div>
				<div class="well">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
	
						<input type="hidden" id="board_mgmt_no" name="board_mgmt_no"/>
						<input type="hidden" id="board_no" name="board_no"/>
						<input type="hidden" id="reg_id" name="reg_id"/>
						<input type="hidden" id="file_seq" name="file_seq"/>
	
	
	
						<!-- 이전, 다음 처리 -->
						<input type="hidden" id="currentPage" name="currentPage" value="${board.currentPage}"/>
						<input type="hidden" id="pagePerCount" name="pagePerCount" value="${board.pagePerCount}"/>
	
						<label>등록일자</label>
						<input type="text" id="strStartDt" name="strStartDt" value="${board.strStartDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" /> ~
						<input type="text" id="strEndDt" name="strEndDt" value="${board.strEndDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,validType:'greaterThan[\'#strStartDt\']'" />
						<label>검색조건</label>
						<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">선택</option>
							<option value="01">제목</option>
							<option value="02">내용</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" value="${board.searchValue}" class="w200 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
				</div>
			</div>
			<sec:authorize access="hasAnyRole('ROLE_DBMANAGER','ROLE_OPENPOPMANAGER')">
				<div class="searchBtn innerBtn"><a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 공지사항 등록</a></div>
			</sec:authorize>
			<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:550px">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>