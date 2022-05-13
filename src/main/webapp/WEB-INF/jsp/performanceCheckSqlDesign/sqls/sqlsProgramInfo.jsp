<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2020.05.28	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > 성능 점검 SQL 3차 메뉴 > SQLs 탭 > 프로그램 정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
</head>
<body>
	<div style="width:100%;height:240px;overflow-y:auto;">
		<table class="detailT">
			<tr>
				<th style="width:105px">업무명</th>
				<td>${sqls.wrkjob_cd_nm}</td>
				<th style="width:105px">배포요청자</th>
				<td>${sqls.user_nm}</td>
			</tr>
			<tr>
				<th>프로그램 구분</th>
				<td>${sqls.program_div_nm}</td>
				<th>프로그램명</th>
				<td>${sqls.program_nm}</td>
			</tr>
			<tr>
				<th>SQL식별자(DBIO)</th>
				<td>${sqls.dbio}</td>
				<th>프로그램유형</th>
				<td>${sqls.program_type_nm}</td>
			</tr>
			<tr>
				<th>SQL명령 유형</th>
				<td>${sqls.sql_command_type_nm}</td>
				<th>다이나믹SQL 여부</th>
				<td>${sqls.dynamic_sql_yn}</td>
			</tr>
			<tr>
				<th>프로그램설명</th>
				<td colspan="3">${sqls.program_desc}</td>
			</tr>
			<tr>
				<th>파일명</th>
				<td>${sqls.file_nm}</td>
				<th>디렉토리</th>
				<td>${sqls.dir_nm}</td>
			</tr>
			<tr>
				<th>등록일시</th>
				<td>${sqls.reg_dt}</td>
				<th>변경일시</th>
				<td>${sqls.last_update_dt}</td>
			</tr>
		</table>
	</div>
</body>
</html>