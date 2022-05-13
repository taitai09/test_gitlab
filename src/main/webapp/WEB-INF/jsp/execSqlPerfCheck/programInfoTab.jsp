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
 * 2021.08.02	황예지	최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 결과-프로그램 정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<script type="text/javascript">
		var dateArr= [],
			pagingYnArr = [],
			pagingCntArr = [];
		
		<c:forEach items="${programExecuteTmsList}" var="programExeuteInfo">
			
			dateArr.push("${programExeuteInfo.program_exec_dt}");
			pagingYnArr.push("${programExeuteInfo.pagingYn}");
			pagingCntArr.push("${programExeuteInfo.pagingCnt}");
 		
		</c:forEach>
	</script>
	<style>
		#sqlTextArea2 {
			position: absolute;
			top: -500;
			left: -500;
			width: 1px;
			height: 1px;
			margin: 0;
			padding:0;
			border: 0;
		}
	</style>
</head>
<body>
	<div style="width:100%;height:380px;overflow-y:auto;">
		<table class="detailT">
			<colgroup>
				<col style="width: 130px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: auto; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 150px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
			</colgroup>
			<tbody>
				<tr>
					<th>프로그램명</th>
					<td>${perfCheckAllPgm.program_nm}</td>
					<th style="font-size:13px;">
						<span style="font-size:13px;">검증제외</span>
						<span id="check_span1" style="position: relative; top: -1px;margin-left: 3px;">
							<input class="easyui-checkbox checkbox-f" id="perf_check_auto_pass_yn_chk" disabled="" style="display: none;">
						</span>
					</th>
					<td>&nbsp;${perfCheckAllPgm.exception_prc_meth_nm}</td>
					<th>프로그램 구분</th>
					<td>${perfCheckAllPgm.program_div_nm}</td>
					<th>프로그램유형</th>
					<td>${perfCheckAllPgm.program_type_nm}</td>
					<th>SQL명령 유형</th>
					<td>${perfCheckAllPgm.sql_command_type_nm}</td>
				</tr>
				<tr>
					<th>프로그램설명</th>
					<td colspan="9">${perfCheckAllPgm.program_desc}</td>
				</tr>
				<tr>
					<th>SQL식별자(DBIO)</th>
					<td colspan="3">${perfCheckAllPgm.dbio}</td>
					<th>다이나믹SQL 여부</th>
					<td>${perfCheckAllPgm.dynamic_sql_yn}</td>
					<th>등록일시</th>
					<td>${perfCheckAllPgm.reg_dt}</td>
					<th>변경일시</th>
					<td>${perfCheckAllPgm.last_update_dt}</td>
				</tr>
				<tr>
					<th>파일명</th>
					<td colspan="3">${perfCheckAllPgm.file_nm}</td>
					<th>디렉토리</th>
					<td colspan="5">${perfCheckAllPgm.dir_nm}</td>
				</tr>
				<tr>
					<th rowspan="2" style="vertical-align:top;">
						<br><br><br><br><br><br><br><br>
						<a href="javascript:;" id="sqlCopyBtn1" class="easyui-linkbutton l-btn l-btn-small" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea1">
							<span class="l-btn-left l-btn-icon-left" style="padding:5px"><i class="btnIcon fas fa-copy fa-lg fa-fw"></i> SQL 원본 복사</span>
						</a>
						<br><br>
						<a href="javascript:;" id="sqlCopyBtn2" class="easyui-linkbutton l-btn l-btn-small" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea2">
							<span class="l-btn-left l-btn-icon-left" style="padding:5px"><i class="btnIcon fas fa-copy fa-lg fa-fw"></i> SQL 변환 복사</span><br/>
						</a>
					</th>
					<td colspan="9" rowspan="2" style="vertical-align:top">
						<div id="sqlTextArea1" style="width:100%;height:320px;overflow-y:auto;"><pre>${perfCheckAllPgm.program_source_desc}</pre></div>
						<div id="sqlTextArea2"><pre>${perfCheckAllPgm.program_source_desc2}</pre></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>