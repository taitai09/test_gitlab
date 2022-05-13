<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검 :: 표준 미준수 SQL</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/execSqlStdChkCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/nonStandardSql.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlDiagnosisReportPopup.js?ver=<%=today%>"></script> <!-- SQL Full Text 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script type="text/javascript" src="/resources/js/paging2.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		.nonStandardSql .searchOption{
			margin-bottom: 15px;
		}
		.nonStandardSql .searchOption a{
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
			<input type="hidden" id="scheduler_no_inherited" value="${sql_std_qty_scheduler_no}">
			<input type="hidden" id="gather_day_inherited" value="${sql_std_gather_day}">
			<input type="hidden" id="currentPage" name="currentPage" value="${currentPage}">
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20">
			<input type="hidden" id="currentPage2" value="1">
			<input type="hidden" id="rcount" value="20">
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			<input type="hidden" name="sql_std_qty_div_cd" value="4">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_combo" data-options="editable:false,prompt:'선택'" class="w300 easyui-combobox" required></select>
				<input type="hidden" id="project_id" name="project_id" value="${project_id}">
				
				<label>스케줄러</label>
				<select id="scheduler_no" name="sql_std_qty_scheduler_no" data-options="editable:false,prompt:'선택'" class="w300 easyui-combobox" required></select>
				
				<label>품질 점검 지표</label>
				<select id="qty_chk_idt_cd_combo" data-options="editable:false" class="w200 easyui-combobox"></select>
				<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd" value="000">
				
				<label>SQL ID</label>
				<select id="sql_id_textbox" class="w150 easyui-textbox"></select>
				<input type="hidden" id="sql_id" name="sql_id" value="0">
				
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				<a href="javascript:;" class="w120 easyui-linkbutton Btn_Download" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
		
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:587px">
				<div region='west' data-options="split:false, border:false" style="width:19.9%;height:100%;">
					<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
						<div region='north' data-options="split:false,collapsible:true,border:false" style="width:100%;height:94.5%;">
							<table id="tableLeftList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
						<div region='south' data-options="split:false, border:false" style="width:100%;height:5.5%;overflow:hidden">
							<div class="searchBtn" data-options="split:false,border:false">
								<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
								<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
					</div>
				</div>
				
				<div region='east' data-options="split:false,collapsible:true,border:false" style="width:79.9%;height:99%;">
					<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
						<div region='north' data-options="split:false,collapsible:true,border:false" style="width:100%;height:94.5%;">
							<table id="tableRightList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
						<div region='south' data-options="split:false, border:false" style="width:100%;height:5.5%;overflow:hidden">
							<div class="searchBtn" data-options="split:false,border:false">
								<a href="javascript:;" id="prevBtn2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
								<a href="javascript:;" id="nextBtn2" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right: 2px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
					</div>
				</div>
			</form:form>
			
			<div id="form_area">
				<form:form method="post" id="submit_form_excel" name="submit_form" class="form-inline">
					<input type="hidden" id="project_id_excel" name="project_id" value="">
					<input type="hidden" id="scheduler_no_excel" name="sql_std_qty_scheduler_no" value="">
					<input type="hidden" id="sql_std_gather_day_excel" name="sql_std_gather_day" value="${sql_std_gather_day}">
					<input type="hidden" id="sql_id_excel" name="sql_id" value="">
					<input type="hidden" id="qty_chk_idt_cd_excel" name="qty_chk_idt_cd" value="">
					<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
					<input type="hidden" name="sql_std_qty_div_cd" value="4">
					<input type="hidden" id="currentPage_excel" name="currentPage" value="1">
					<input type="hidden" id="pagePerCount_excel" name="pagePerCount" value="20">
				</form:form>
				
				<form:form method="post" id="submit_form_popup" name="submit_form" class="form-inline">
					<input type="hidden" id="project_id_popup" name="project_id" value="">
					<input type="hidden" id="sql_std_gather_day_popup" name="sql_std_gather_day" value="">
					<input type="hidden" id="scheduler_no_popup" name="sql_std_qty_scheduler_no" value="">
					<input type="hidden" id="sql_id_popup" name="sql_id" value="">
					<input type="hidden" name="sql_std_qty_div_cd" value="4">
				</form:form>
			</div>
		</div>
	</div>	<!-- contents END -->
</div>	<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlDiagnosisReportPopup.jsp" %> <!-- SQL Full Text 팝업 -->
</body>
</html>