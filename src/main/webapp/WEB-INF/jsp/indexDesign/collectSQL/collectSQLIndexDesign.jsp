<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.09.16 initiate 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>수집SQL 인덱스 설계</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesign/collectSQL/collectSQLIndexDesign.js?ver=<%=today%>"></script>
    <style>
    	.tabs-header{
			background-color: rgb(255, 255, 255);
		}
    </style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">

		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="indexDesignCollectSQLTab" class="easyui-tabs" data-options="fit:true,border:false">
			
				<div title="수집SQL 조건절 파싱" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="collectSQLIndexDesignIF1" name="collectSQLIndexDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
					src="/indexDesign/collectSQL/ParsingCollectionTerms" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="수집SQL 인덱스 자동설계" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="collectSQLIndexDesignIF2" name="collectSQLIndexDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
					style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/indexDesign/collectSQL/AutoCollectionIndexDesign"  -->
				</div>
				
				<div title="수집SQL 인덱스 설계" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="collectSQLIndexDesignIF3" name="collectSQLIndexDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
					style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/indexDesign/collectSQL/CollectionIndexDesign"  -->
				</div>
							
				<div title="수집SQL 인덱스 정비" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="collectSQLIndexDesignIF4" name="collectSQLIndexDesignIF4" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
					style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/indexDesign/collectSQL/CollectionIndexUsage"  -->
				</div>				
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>