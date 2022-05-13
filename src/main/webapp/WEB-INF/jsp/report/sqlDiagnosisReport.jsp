<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true" %>
<%
/***********************************************************
 * 2021.09.23	김원재	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>종합 진단 :: SQL 품질 진단 :: 스케쥴러 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
		<script type="text/javascript" src="/resources/js/ui/include/popup/sqlDiagnosisReportPopup.js?ver=<%=today%>"></script> <!-- FILTER SQL 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReport.js?ver=<%=today%>"></script>
	
	<!-- RSA 필수사항 자바라이브러리 -->
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rsa2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<!-- END RSA 필수사항 자바라이브러리 -->
	<style>
		#leftSection .datagrid-view2 .datagrid-header{
			display: none;
		}
		#leftSection .datagrid-view2 .datagrid-body{
			border-top: 2px solid #38312a;
		}
/* 		#rightSection .datagrid-header .datagrid-htable tr td span{ */
/* 			font-weight : 700; */
/* 		} */
		#rightSection .datagrid-header-row td div{
			white-space: break-spaces;
			padding : 5px;
		}

		.datagrid-empty{
			top : 0px;
		}
		#rightSection .datagrid-empty{
			top : 60px;
		}
		#rightSection .datagrid-empty{
			top : 60px;
		}
		
		
	</style>
	
	<script type="text/javascript">
		var parent_std_qty_target_dbid = ''
		var parent_sql_std_qty_scheduler_no = ''
		var call_from_parent = 'N'
		
		if('${call_from_parent}' && '${call_from_parent}' != 'N'){
			call_from_parent = 'Y'
		}
			if('${std_qty_target_dbid}'){
				parent_std_qty_target_dbid = '${std_qty_target_dbid}'
			}
			if('${sql_std_qty_scheduler_no}'){
				parent_sql_std_qty_scheduler_no = '${sql_std_qty_scheduler_no}'
		}
		console.log(parent_std_qty_target_dbid)
		console.log(parent_sql_std_qty_scheduler_no)
		console.log(call_from_parent)
	</script>
	
	
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="manageScheduler">
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			
			<div class="well searchOption">
				<div style="display:inline">
					<label>품질진단DB</label>
					<select id="db_name_combo" name="std_qty_target_dbid_combo" data-options="editable:false" class="w170 easyui-combobox" required></select>
				</div>
					<label >스케줄러</label>
				<div id="sql_std_qty_scheduler_no_combo_div" style="display:inline-block">
					<select onClick="createSchedulerCombo();" id="scheduler_no_combo" name="sql_std_qty_scheduler_no_combo" data-options="panelHeight:'auto',editable:false" class="w400 easyui-combobox" required></select>
				</div>
				<a href="javascript:;" class="w80 easyui-linkbutton" style="margin-left : 10px;"  onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				
				<a href="javascript:;" style="float: right;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			
				<input type="hidden" id="currentPage" name="currentPage" value="1"/>
				<input type="hidden" id="pagePerCount" name="pagePerCount" value="15"/>
				<input type="hidden" id="qty_chk_idt_cd" name ="qty_chk_idt_cd"/>
				<input type="hidden" id="std_qty_target_dbid" name ="std_qty_target_dbid"/>
				<input type="hidden" id="sql_std_qty_scheduler_no" name ="sql_std_qty_scheduler_no"/>
				<input type="hidden" id="sql_std_qty_chkt_id" name ="sql_std_qty_chkt_id"/>
				<input type="hidden" id="sql_std_qty_program_seq" name ="sql_std_qty_program_seq"/>
				
		</form:form>
		
		<form:form method="post" id="excel_submit_form" name="submit_form" class="form-inline">
			<input type="hidden" id="excel_qty_chk_idt_cd" name="qty_chk_idt_cd" value="">
			<input type="hidden" id="excel_std_qty_target_dbid" name="std_qty_target_dbid" value="">
			<input type="hidden" id="excel_sql_std_qty_scheduler_no" name="sql_std_qty_scheduler_no" value="">
			
		</form:form>
	
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px; margin-top:10px;">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99.5%;">
				<div style="width:29% ; float:left; height:100%">
					<table class="detailT" style="margin-top:0px; border-top:none;height:60px;">
						<colgroup>	
							<col style="width:15%;">
							<col style="width:35%;">
							<col style="width:15%;">
							<col style="width:35%;">
						</colgroup>
						<tr style="text-align: center">
							<th>진단상태</th>
							<td>
								<img id="schedulerStausImg" style="vertical-align: middle"/>
								<span id="schedulerStaus" name="schedulerStaus"></span>
							</td>
							<th>진단결과</th>
							<td>
								<span id="schedulerResult" name="schedulerResult"></span>
							</td>						</tr>
						<tr style="text-align: center">
							<th>진단일시</th>
							<td>
									<span id="diag_dt" name="diag_dt"></span>
<!-- 								<input id="diag_dt" name="diag_dt" value="" data-options="editable:true" class="easyui-textbox" readOnly> -->
							</td>
							<th>수집기간</th>
							<td>
									<span id="gather_term" name="gather_term"></span>
<!-- 								<input id="gather_term" name="gather_term" value="" data-options="editable:true" class="easyui-textbox" readOnly> -->
							</td>
						</tr>
					</table>
					<div id="leftSection" style="width:100%;min-height:490px; margin-top:10px">
						<table id="leftTable" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="border-top: none;">
						</table>
					</div>
					
				</div>
				<div id = 'rightSection' style="width:70% ; float:right; min-height:560px;">
					<table id="rightTable" class="tbl easyui-datagrid" data-options="fit:true,border:false" >
					</table>
				</div>
			</div>
			
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="margin-top:10px; width:100%;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlDiagnosisReportPopup.jsp" %> <!-- FILTER SQL 팝업 -->

</body>
</html>