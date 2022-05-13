<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.09.20	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: 기준정보 설정:: 프로젝트 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/systemManage/projectMng/projectMngDesign.js?ver=<%=today%>"></script>
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
			<div id="projectMngLTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="프로젝트 관리" data-options="fit:true" style="padding-top:5px;min-height:680px;">
					<iframe id="projectMngDesignIF1" name="projectMngDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/systemManage/projectMng/ProjectMng" style="width:100%;min-height:680px;"></iframe>
				</div>
				<div title="프로젝트 업무 관리" data-options="fit:true" style="padding-top:5px;min-height:680px;">
					<iframe id="projectMngDesignIF2" name="projectMngDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 							src="/systemManage/projectMng/ProjectWrkjobMng" style="width:100%;min-height:680px;"></iframe> -->
				</div>
				<div title="프로젝트 DB 관리" data-options="fit:true" style="padding-top:5px;min-height:680px;">
					<iframe id="projectMngDesignIF3" name="projectMngDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 							src="/systemManage/projectMng/ProjectDbMng" style="width:100%;min-height:680px;"></iframe> -->
				</div>
				<div title="프로젝트 튜닝진행단계" data-options="fit:true" style="padding-top:5px;min-height:680px;">
					<iframe id="projectMngDesignIF4" name="projectMngDesignIF4" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 							src="/systemManage/projectMng/ProjectTuningProcessStage" style="width:100%;min-height:680px;"></iframe> -->
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>