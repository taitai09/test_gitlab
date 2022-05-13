<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: SQL 표준 일괄 점검 :: 스케쥴러 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/sqlStandardDesign/operation/manageScheduler.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandardDesign/operation/sqlStandardCommon.js?ver=<%=today%>"></script>
	
	<!-- RSA 필수사항 자바라이브러리 -->
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rsa2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<!-- END RSA 필수사항 자바라이브러리 -->
	<style>
		.manageScheduler .searchOption a{
			margin-left: 10px;
		}
		.manageScheduler .searchOption {
			margin-bottom: 15px;
		}
		.manageScheduler a:last-child {
			float: right;
		}
		.manageScheduler .littleTitle {
			margin: 9px 5px 7px 5px;
			font-weight: bold;
			font-size: 13.5px;
		}
		.manageScheduler .detailT {
			margin-bottom: 10px;
		}
	</style>
	<script type="text/javascript">
		var auth_cd = '${auth_cd}';
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="manageScheduler">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" name="std_qty_scheduler_div_cd" value="01">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_id" name="project_id" data-options="editable:false" class="w350 easyui-combobox"></select>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
		</form:form>
		<form:form method="post" id="excel_submit_form" name="submit_form" class="form-inline">
			<input type="hidden" id="excel_project_id" name="project_id" value="">
			<input type="hidden" name="std_qty_scheduler_div_cd" value="01">
			<input type="hidden" name="sql_std_qty_div_cd" value="2">
		</form:form>
	
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:280px">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;margin-top: 5px;">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" name="job_scheduler_type_cd">
				<input type="hidden" name="sql_std_qty_scheduler_no">
				<input type="hidden" name="use_yn" value="Y">
				<input type="hidden" name="std_qty_scheduler_div_cd" value="01">
				
				<p class="littleTitle">스케줄러 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:14.5%;">
						<col style="width:10%;">
						<col style="width:14.5%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
					</colgroup>
					<tr>
						<th>프로젝트</th>
						<td colspan="3">
							<select id="project_id_modify" name="project_id" value="" data-options="panelHeight:'140px',editable:false" class="easyui-combobox" style="width: 63%;" required></select>
						</td>
						<th>스케줄러명</th>
						<td colspan="3">
							<input id="job_scheduler_nm" name="job_scheduler_nm" value="" data-options="editable:true" class="easyui-textbox" style="width: 61.5%;" required>
						</td>
					</tr>
					<tr>
						<th>시작일자</th>
						<td>
							<input type="text" id="exec_start_dt" name="exec_start_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" style="width: 85.5%;" required>
						</td>
						<th>종료일자</th>
						<td>
							<input type="text" id="exec_end_dt" name="exec_end_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"style="width: 85.5%;" required>
						</td>
						<th>실행주기</th>
						<td colspan="3">
							<select id="date_fir" name="exec_cycle_div_cd" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox" required></select>
							<select id="date_scnd" data-options="panelHeight:'150px',editable:false" class="w100 easyui-combobox" required></select>
							<input type="hidden" id="execOption" name="">
							<select id="date_thrd" name="exec_hour" data-options="panelHeight:'300px',editable:false" class="w100 easyui-combobox" required></select>
							<select id="date_frth" name="exec_minute" data-options="panelHeight:'300px',editable:false" class="w100 easyui-combobox" required></select>
							<input type="hidden" name="exec_cycle" data-part="">
						</td>
					</tr>
					<tr>
						<th>타겟DB</th>
						<td>
							<select id="db_name_combo" value="" class="w200 easyui-combobox" data-options="panelHeight:'auto'" required></select>
							<input type="hidden" id="std_qty_target_db_name" name="std_qty_target_db_name">
						</td>
						<th>DB 유저 ID</th>
						<td>
							<input type="text" id="std_qty_target_db_user_id" name="std_qty_target_db_user_id" class="easyui-textbox" style="width: 85.5%;">
						</td>
						<th>Parser Code</th>
						<td colspan="3">
							<input type="text" id="parse_code" name="parse_code" class="w200 easyui-textbox" required>
						</td>
					</tr>
					<tr>
						<th>스케줄러 설명</th>
						<td colspan="7">
							<input type="text" id="job_scheduler_desc" name="job_scheduler_desc" class="easyui-textbox" style="width: 41.2%;">
						</td>
					</tr>
				</table>
				
				<p class="littleTitle">형상관리 시스템 연동 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:14.5%;">
						<col style="width:10%;">
						<col style="width:14.5%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
					</colgroup>
					<tr>
						<th>연동방법</th>
						<td colspan="3">
							<select id="svn_if_meth_cd" name="svn_if_meth_cd" value="" data-options="panelHeight:'auto',editable:false" class="easyui-combobox" style="width: 31.4%;" required></select>
						</td>
						<th>디렉토리</th>
						<td colspan="3">
							<input id="svn_dir_nm" name="svn_dir_nm" value="" data-options="editable:true" class="easyui-textbox" style="width: 94.2%;" required>
						</td>
					</tr>
					<tr>
						<th>IP</th>
						<td>
							<input type="text" id="svn_ip" name="svn_ip" value="" class="w200 easyui-textbox" data-options="panelHeight:'auto'" required>
						</td>
						<th>PORT</th>
						<td>
							<input type="text" id="svn_port" name="svn_port" value="" class="w200 easyui-textbox" data-options="panelHeight:'auto'" required>
						</td>
						<th>사용자 ID</th>
						<td>
							<input type="text" id="svn_os_user_id" name="svn_os_user_id" value="" class="w200 easyui-textbox" data-options="panelHeight:'auto'" required>
						</td>
						<th>사용자 패스워드</th>
						<td>
							<input type="text" id="pre_svn_os_user_password" value="" class="w200 easyui-passwordbox" data-options="panelHeight:'auto',showEye: false" required>
							<input type="hidden" name="svn_os_user_password" value="">
							<input type="hidden" id="RSAModulus" value="${sessionScope.RSAKeys.Modulus}">
							<input type="hidden" id="PuKeyExpon" value="${sessionScope.RSAKeys.PublicExp}">
							<input type="hidden" id="PrKeyExpon" value="${sessionScope.RSAKeys.PrivateExp}">
						</td>
					</tr>
				</table>
				
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" id="btnSave" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" id="btnDelete" onClick="Btn_PreventDelete();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->

</body>
</html>