<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.10.23	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>공지사항 :: 등록</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/board/noticeInsert.js?ver=<%=today%>"></script>
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
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
			
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="top_notice_yn" name="top_notice_yn" value="N"/>
				<input type="hidden" id="contents" name="contents" />

				<table class="detailT">
					<colgroup>
						<col style="width:15%;">
						<col style="width:85%;">
					</colgroup>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="title" name="title" class="easyui-textbox" style="width:1000px;"/>
							<span style="padding-left:10px;padding-right:5px;">상단공지</span>
							<input type="checkbox" id="chkTopFixYn" name="chkTopFixYn" class="w120 easyui-switchbutton"/>
						</td>
					</tr>
					<tr height="130px;">
						<th>내용</th>
						<td><textarea name="board_contents" id="board_contents" rows="20" style="width:97%;padding:15px;IME-MODE:active;"></textarea></td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td><input id="uploadFile" multiple="multiple" name="uploadFile" class="easyui-filebox" data-options="prompt:'파일을 선택해 주세요.'" style="width:50%"/></td>
					</tr>
				</table>
				<div class="dtlBtn">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 등록</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
				</div>				
			</form:form>
		</div>
	</div>
	<!-- contents END -->			
</div>
<!-- container END -->
</body>
</html>