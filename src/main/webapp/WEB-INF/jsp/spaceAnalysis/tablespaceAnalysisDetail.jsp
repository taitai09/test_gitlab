<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.05.02	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>TABLESPACE 분석 - 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>    
    <script type="text/javascript" src="/resources/js/ui/spaceAnalysis/tablespaceAnalysisDetail.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${odsTablespaces.dbid}"/>
					<input type="hidden" id="owner" name="owner"/>
					<input type="hidden" id="segment_name" name="segment_name"/>
					<input type="hidden" id="partition_name" name="partition_name"/>					
					<input type="hidden" id="file_name" name="file_name"/>
					<input type="hidden" id="recommendations" name="recommendations"/>
					<input type="hidden" id="c1" name="c1"/>
					<input type="hidden" id="c2" name="c2"/>
					<input type="hidden" id="c3" name="c3"/>					
					<label>DB</label>
					<input type="text" id="db_name" name="db_name" data-options="readonly:true" value="${odsTablespaces.db_name}" class="w100 easyui-textbox"/>
					<label>TABLESPACE NAME</label>
					<input type="text" id="tablespace_name" name="tablespace_name" data-options="readonly:true" value="${odsTablespaces.tablespace_name}" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px">
			<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding-top:5px;">
				<div id="segmentTab" class="easyui-tabs" data-options="border:false" style="width:100%;height:100%;">
					<div id="segmentTable" title="Segment List(TOP 50)" style="padding-top:5px;">
						<table id="segmentList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="segmentChart" title="Segment Statistics / Size History" style="padding-top:5px;">
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div id="recommendationDiv" data-options="region:'north',split:false,collapsible:false,border:false" style="height:140px;">
								<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ Recommendation</span></div>
								<table id="recommendTbl" class="realT" style="margin-top:0px;">
									<colgroup>	
										<col style="width:20%;"/>
										<col style="width:80%;"/>
									</colgroup>
									<tbody></tbody>
								</table>
							</div>
							<div id="segmentStatChart" data-options="title:'▶ Segment Statistics',region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:5px 5px 0px 0px;">
							</div>
							<div id="sizeHistoryChart" data-options="title:'▶ Segment Size History',region:'center',split:false,collapsible:false,border:false" style="padding:5px 0px 0px 5px;">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:250px;padding-top:5px;">
				<div id="datafileTab" class="easyui-tabs" data-options="border:false" style="width:100%;height:100%;">
					<div id="datafileTable" title="Datafile List" style="padding-top:5px;">
						<table id="datafileList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="datafileChart" title="Datafile Statistics" style="padding-top:5px;">
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>