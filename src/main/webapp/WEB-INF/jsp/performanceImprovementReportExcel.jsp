<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.sql.Timestamp" %>
<%@page import="java.util.Locale" %>
<%
/***********************************************************
 * 2017.10.12	이원식	최초작성
 **********************************************************/
%>
<%
	String rtnStr = null;
    SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.KOREA);
    Timestamp ts = new Timestamp(System.currentTimeMillis());

    rtnStr = sdfCurrent.format(ts.getTime());

	//MS excel로 다운로드/실행, filename에 저장될 파일명을 적어준다.
 	String fileName = "성능관리현황_" + rtnStr;
	fileName = new String(fileName.getBytes("KSC5601"), "8859_1");
	response.setHeader("Content-Disposition","attachment;filename="+fileName+".xls;");
	response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	response.setHeader("Content-Description", "JSP Generated Data");
	response.setHeader("Content-Transfer-Encoding", "binary;"); 
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
%>
<html xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"xmlns="http://www.w3.org/TR/REC-html40">
<head>
<title>OMC-SPOP</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<style>
<!--
tr
	{mso-height-source:auto;
	mso-ruby-visibility:none;}
col
	{mso-width-source:auto;
	mso-ruby-visibility:none;}
br
	{mso-data-placement:same-cell;}
.style0
	{mso-number-format:General;
	text-align:general;
	vertical-align:middle;
	white-space:nowrap;
	mso-rotate:0;
	mso-background-source:auto;
	mso-pattern:auto;
	color:black;
	font-size:11.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:"맑은 고딕", monospace;
	mso-font-charset:129;
	border:none;
	mso-protection:locked visible;
	mso-style-name:표준;
	mso-style-id:0;}
td
	{mso-style-parent:style0;
	padding-top:1px;
	padding-right:1px;
	padding-left:1px;
	mso-ignore:padding;
	color:black;
	font-size:11.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:"맑은 고딕", monospace;
	mso-font-charset:129;
	mso-number-format:General;
	text-align:general;
	vertical-align:middle;
	border:none;
	mso-background-source:auto;
	mso-pattern:auto;
	mso-protection:locked visible;
	white-space:nowrap;
	mso-rotate:0;}
.xl65
	{mso-style-parent:style0;
	text-align:center;
	padding:5px;
	border:.5pt solid #D9D9D9;}
.xl67
	{mso-style-parent:style0;
	text-align:center;
	padding:5px;
	border-top:.5pt solid #D9D9D9;
	border-right:.5pt solid #D9D9D9;
	border-bottom:.5pt solid #D9D9D9;
	border-left:.5pt solid #D9D9D9;
	background:#F2F2F2;
	mso-pattern:black none;}
.xl66
	{mso-style-parent:style0;
	text-align:center;
	padding:5px;
	border-top:1.5pt solid #3579D4;
	border-right:.5pt solid #D9D9D9;
	border-bottom:.5pt solid #D9D9D9;
	border-left:.5pt solid #D9D9D9;
	background:#F2F2F2;
	mso-pattern:black none;}	
rt
	{color:windowtext;
	font-size:8.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:"맑은 고딕", monospace;
	mso-font-charset:129;
	mso-char-type:none;
	display:none;}
-->
</style>
</head>

<body>
	<table border="0" cellpadding="0" cellspacing="0" style='border-collapse:collapse;table-layout:fixed;'>
		<tr height=35 style='mso-height-source:userset;height:26.25pt'>
			<td class=xl66 colspan="2" style='border-left:none'>구분</td>
			<td class=xl66 colspan="5" style='border-left:none'>개선대상</td>
			<td class=xl66 colspan="8" style='border-left:none'>처리단계</td>
		</tr>
		<tr height=35 style='mso-height-source:userset;height:26.25pt'>
			<td class=xl67 rowspan="3" style='border-left:none'>DB</td>
			<td class=xl67 rowspan="3" style='border-left:none'>업무</td>
			<td class=xl67 colspan="2" style='border-left:none'>의뢰</td>
			<td class=xl67 colspan="2" style='border-left:none'>선정</td>
			<td class=xl67 rowspan="3" style='border-left:none'>총계</td>
			<td class=xl67 colspan="4" style='border-left:none'>성능관리팀</td>
			<td class=xl67 colspan="4" style='border-left:none'>업무개발팀</td>
		</tr>
		<tr height=35 style='mso-height-source:userset;height:26.25pt'>
			<td class=xl67 rowspan="2" style='border-left:none'>전일누적</td>
			<td class=xl67 rowspan="2" style='border-left:none'>당일추가</td>
			<td class=xl67 rowspan="2" style='border-left:none'>전일누적</td>
			<td class=xl67 rowspan="2" style='border-left:none'>당일추가</td>
			<td class=xl67 rowspan="2" style='border-left:none'>분석중</td>
			<td class=xl67 colspan="3" style='border-left:none'>분석완료</td>
			<td class=xl67 rowspan="2" style='border-left:none'>분석중</td>
			<td class=xl67 colspan="3" style='border-left:none'>분석완료</td>
		</tr>
		<tr height=35 style='mso-height-source:userset;height:26.25pt'>
			<td class=xl67 style='border-left:none'>개선완료</td>
			<td class=xl67 style='border-left:none'>개선사항없음</td>
			<td class=xl67 style='border-left:none'>오류</td>
			<td class=xl67 style='border-left:none'>튜닝반려</td>
			<td class=xl67 style='border-left:none'>적용완료</td>
			<td class=xl67 style='border-left:none'>적용반려</td>
		</tr> 
		<c:forEach items="${resultList}" var="result" varStatus="status">		
			<tr height=30 style='mso-height-source:userset;height:22.5pt'>
				<td class=xl65 style='border-top:none;border-left:none'>${result.db_name}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.db_abbr_nm}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.req_before}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.req_today}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.sel_before}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.sel_today}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.improve_tot}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.mng_analyzing}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.mng_complete}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.mng_not_improve}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.mng_error}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.dev_analyzing}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.dev_cancel}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.dev_complete}</td>
				<td class=xl65 style='border-top:none;border-left:none'>${result.dev_apply_cancel}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>