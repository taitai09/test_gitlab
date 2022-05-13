<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.02.16	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능시험 :: DB 변경 SQL 영향도 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/analyzeDbChangeSqlImpact/analyzeDbChangeSqlImpact.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlConsiderations_popup.js?ver=<%=today%>"></script> <!-- DB 변경 SQL 고려사항  팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
	#sqlConsiderationsPop ul, ol, li{
		margin:10px 0px 10px 10px;
		line-height:30px;
		list-style:inside;
		font-size: 15px;
		
	}
	ul.minus, ul.minus li
	{
		list-style:none;
	}	
	ul.minus li:before{
		content:"－";
		padding-right:10px;
	}
	ul.minus li.non:before{
		content:"";
	}
	ul.square li:before{
		content:"＊";
	}
	
	img {
		border: 1px solid black;
		padding:5px;
		margin:5px 0px;
	}
	
	.datagrid td, .datagrid td div{
		cursor : default;
	}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3" ><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<table>
						<colgroup>
							<col style="width:5%">
							<col style="width:12%">
							<col style="width:8%">
							<col style="width:12%">
							<col style="width:8%">
							<col style="width:5%">
							<col style="width:8%">
							<col style="width:6%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
						</colgroup>
						<tr style="height:30px;">
							<td><label>프로젝트</label></td>
							<td colspan="1">
								<select id="project_id" name="project_id" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td align="right"><label>SQL점검팩</label>&emsp;</td>
							<td colspan="1">
								<select id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" class="w350 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td align="right"><label>ASIS Oracle Ver.</label></td>
							<td>
								<select id="asis_oracle_version_cd" name="asis_oracle_version_cd" class="w80 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td align="right"><label>TOBE Oracle Ver.</label></td>
							<td>
								<select id="tobe_oracle_version_cd" name="tobe_oracle_version_cd" class="w80 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td style="margin-top:1px;">
								<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_Search();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
							</td>
							<td style="margin-top:1px;">
								<a href="javascript:;" class="w100 easyui-linkbutton" style="margin:0px 5px;" onClick="excelDownload();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</td>
							<td style="margin-top:1px;">
								<a href="javascript:;" class="w100 easyui-linkbutton" style="margin:0px;" onClick="Btn_ShowSqlConsiderationPop();"><i class="btnIcon fas fa-info fa-lg fa-fw"></i> SQL 고려사항 </a>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div style="overflow-y:auto; overflow-x:hidden; margin-top:5px;">
			</div>
			<div class="multi" style="display: flex;">
				<div class="easyui-layout" data-options="border:true" style="width:12%; margin-top:10px; min-height:600px">
					<div data-options="region:'center',split:false,collapsible:true" style="width:100%;height:99%;border:1px solid black;">
						<table id="tableLeftList" style="width:100%">
							<colgroup >
								<col style="width:60%">
								<col style="width:40%">
							</colgroup>
							<tr height="30px;">
								<td style="border:1px solid black; border-top:0px; border-left:0px; margin-left:5px; background-color: #E6E6E6;" align="left"><label style="font-size: larger;">전체 SQL</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right" id="totalSQLCnt"><span style="text-align: center; font-size: larger;" class="totalSQLCnt"></span></td>
							</tr>
							<tr height="30px" >
								<td style="border:1px solid black; border-top:0px; border-left:0px; background-color: #E6E6E6;" align="left"><label style="font-size: larger;">변경대상</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right"><span class="changeTarget" style="font-size: larger; color: red"></span></td>
							</tr>
							<tr height="30px" >
								<td style="border:1px solid black; border-top:0px; border-left:0px; align="left"><label style="font-size: larger;">미지원 힌트</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right"><span class="unsupportedHint" style="font-size: larger;"></span></td>
							</tr>
							<tr height="30px">
								<td style="border:1px solid black; border-top:0px; border-left:0px; align="left"><label style="font-size: larger;">ORA-01719</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right"><span class="ora_01719" style="font-size: larger;"></span></td>
							</tr>
							<tr height="30px">
								<td style="border:1px solid black; border-top:0px; border-left:0px; align="left"><label style="font-size: larger;">ORA-00979</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right"><span class="ora_00979" style="font-size: larger;"></span></td>
							</tr>
							<!-- 
							<tr height="30px">
								<td style="border:1px solid black; border-top:0px; border-left:0px; align="left"><label style="font-size: larger;">Order by 추가</label></td>
								<td style="border-bottom:1px solid; padding-right:10px;" align="right"><span class="addOrderBy" style="font-size: larger;"></span></td>
							</tr>
							 -->
							<tr height="30px">
								<td style="border:1px solid black; border-top:0px; border-left:0px; text-align: left;"><label style="font-size: larger;">ORA-30563</label></td>
								<td style="border-bottom:1px solid; padding-right:10px; text-align: right;"><span id="ora_30563" style="font-size: larger;"></span></td>
							</tr>
							<tr height="30px">
								<td style="border:1px solid black; border-top:0px; border-left:0px; text-align: left;"><label style="font-size: larger;">WM_CONCAT</label></td>
								<td style="border-bottom:1px solid; padding-right:10px; text-align: right;"><span id="wmConcat" style="font-size: larger;"></span></td>
							</tr>
						</table>
<!-- 						<table id="tableLeftList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 						</table> -->
					</div>
				</div>
				<div style="width:0.1%"></div>
				<div class="easyui-layout" data-options="border:false" style="width:87.9%; margin-top:10px; min-height:550px">
					<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
						<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<div id = "comment_container">
	<%@include file="/WEB-INF/jsp/include/popup/sqlConsiderations_popup.jsp" %> <!-- DB 변경 SQL 고려사항  팝업 -->
</div>
</body>
</html>