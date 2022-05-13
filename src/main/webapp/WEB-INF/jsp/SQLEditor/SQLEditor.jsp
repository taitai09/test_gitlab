<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.12.03	명성태	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL Editor</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/lib/datagrid-scrollview.js"></script>
	<script type="text/javascript" src="/resources/js/ui/SQLEditor/SQLEditor.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		
		<input type="hidden" id="sql_text" name="sql_text"/>
		<input type="hidden" id="base_command_type" name="base_command_type" value=1 />
		<input type="hidden" id="conn_info" name="conn_info"/>
		
		<div id="contents">
<!-- 			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%"> -->
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<label>ROWNUM&nbsp;&nbsp;&nbsp;&#60;&#61;</label>
					<input type="number" id="rownum" name="rownum" class="w60 easyui-numberbox" value=1000 data-options="min:1,max:1000"/>
<!-- 					<input type="hidden" id="rownum" name="rownum" /> -->
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="setBindValue();">변수추출</a>
						<span>
							<i id="bind_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
						</span>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();" disable>실행</a>
						<span style="margin-left:100px;"></span>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="transaction(true);">Commit</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="transaction(false);">Rollback</a>
					</span>
					<label>Conn Info</label>
					<input type="text" id="connInfo" name="connInfo" class="w250 easyui-textbox" readonly />
					<span class="searchBtn innerBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Dis_Conn();">DisConn</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</span>
				</div>
			</div>
			<table id="testTbl" class="detailT">
				<colgroup>
					<col style="width:60%;"/>
					<col style="width:40%;"/>
				</colgroup>
				<tr>
					<td><textarea name="sql_editor" id="sql_editor" rows="30" style="margin-top:5px 5px 0px 5px;width:100%;height:400px" wrap="off"></textarea></td>
					<td style="vertical-align:top;">
						<div style="width:100%; height:400px; overflow:auto">
							<table id="bindTbl" class="detailT" style="margin-top:5px;margin-bottom:5px;width:99%;">
								<colgroup>
									<col style="width:10%;"/>
									<col style="width:30%;"/>
									<col style="width:30%;"/>
									<col style="width:30%;"/>
								</colgroup>
								<thead>
									<tr>
										<th>#</th>
										<th>변수</th>
										<th>값</th>
										<th>타입</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:210px">
				<div id="result" data-options="title:'▶ Result',region:'south',split:false,collapsible:false,border:false" style="height:200px;border-top:0px;background-color: lightgray;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false"></table>
				</div>
			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>