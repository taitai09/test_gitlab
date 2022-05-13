<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 형상기반 SQL 표준 일괄 점검 :: 표준 미준수 SQL</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/sqlStandardDesign/operation/sqlStandardCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandardDesign/operation/nonStandardSql.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlDiagnosisReportPopup.js?ver=<%=today%>"></script> <!-- SQL Full Text 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		.nonStandardSql .searchOption {
			margin-bottom: 15px;
		}
		.nonStandardSql .searchOption a {
			margin-left: 5px;
		}
		.nonStandardSql .searchBtn {
			width: 100%;
			height: 100%;
			padding-top: 6px;
			text-align: right;
		}
		.nonStandardSql .btnImg {
			width: 18px;
			height: 18px;
			vertical-align: middle;
		}
		.nonStandardSql .Btn_Download {
			float: right;
		}
		.nonStandardSql .searchOption label:not(.firstChild),
		.nonStandardSql .searchOption .marginLeft {
			margin-left: 15px;
		}
	</style>
	<script>
		var allIndesList = '${allIndexList}';
		var indexListFromParent = '${indexList}';
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="nonStandardSql">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
			<input type="hidden" id="project_id_inherited" value="${project_id}">
			<input type="hidden" id="wrkjob_cd_inherited" value="${wrkjob_cd}">
			<input type="hidden" id="auth_cd" name="auth_cd" value="${auth_cd}">
			<input type="hidden" id="leader_yn" name="leader_yn" value="${leader_yn}">
			<input type="hidden" name="sql_std_qty_div_cd" value="2">
			<input type="hidden" id="currentPage" name="currentPage" value="1">
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20">
			
			<div class="well searchOption">
				<label class="firstChild">프로젝트</label>
				<select id="project_combo" data-options="editable:false,prompt:'선택'" class="w300 easyui-combobox" required></select>
				<input type="hidden" id="project_id" name="project_id" value="${project_id}">
				
				<label>업무</label>
				<select id="wrkjob_combo" data-options="editable:false,prompt:'선택'" class="w200 easyui-combobox" required></select>
				<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${wrkjob_cd}">
				
				<label>개발자명</label>
				<input id="user_nm_textbox" class="w150 easyui-textbox">
				<input type="hidden" id="user_nm" name="user_nm" value="${user_nm}">
				
				<label>개발자ID</label>
				<input id="dev_id_textbox" class="w150 easyui-textbox">
				<input type="hidden" id="developer_id" name="developer_id" value="${developer_id}">
				
				<div class="multi">
					<label class="firstChild">품질 점검 지표</label>
					<select id="qty_chk_idt_cd_combo" data-options="editable:false" class="easyui-combobox" style="width:275px;"></select>
					<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd" value="000">
					
					<label>SQL 식별자(DBIO)</label>
					<input id="dbio_textbox" class="easyui-textbox" style="width:257px;"></select>
					<input type="hidden" id="dbio" name="dbio">
					
					<a href="javascript:;" class="w80 easyui-linkbutton marginLeft" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
					<a href="javascript:;" class="w120 easyui-linkbutton Btn_Download" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
				</div>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:552px">
				<div region='north' data-options="split:false,collapsible:true,border:false" style="width:100%;height:94.2%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div region='south' data-options="split:false, border:false" style="width:100%;height:5.8%;overflow:hidden">
					<div class="searchBtn" data-options="split:false,border:false">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
			</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlDiagnosisReportPopup.jsp" %> <!-- SQL Full Text 팝업 -->
</body>
</html>