<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>인덱스 자동 검증</title>
	<meta charset="utf-8"/>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/autoIndexAnalys.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/filter_sql_perf_impact_popup.js?ver=<%=today%>"></script> <!-- FILTER SQL 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">

		.combobox-item{
			line-height: 11px;
			font-weight: 300;
			-webkit-font-smoothing: antialiased;
		}
		.radioLabel{
			margin-left:0px;
			margin-right:0px;
		}
	</style>
</head>
<body style="visibility:hidden;">
	<!-- container START -->
	<div id="container">
		<!-- contents START -->
		<div id="contents">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="nowDate" name="nowDate" value="${nowDate}"/>
			<input type="hidden" id="startDate" name="startDate" value="${startDate}"/>
			<input type="hidden" id="endDate" name="endDate" value="${endDate}"/>
			<input type="hidden" id="startTime" name="startTime" value="${startTime}"/>
			<input type="hidden" id="endTime" name="endTime" value="${endTime}"/>
			<input type="hidden" id="commonCode" name="commonCode" value="${commonCode}"/>

			<input type="hidden" id="endTime" name="endTime" value="${endTime}"/>
			<input type="hidden" id="commonCode" name="commonCode" value="${commonCode}"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="ORACLE" />
			<input type="hidden" id="oneRow" name="oneRow"/>

			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="well">
					<table>
						<colgroup>
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:7%">
							<col style="width:15%">
							<col style="width:30%">
							<col style="width:20%">
						</colgroup>
						
						<tr height="35px;">
							<td><label>프로젝트</label></td>
							<td colspan="3">
								<select id="project_id" name="project_id" style='height:25px' class="w360 easyui-combobox required" requiredMsg="프로젝트를 먼저 선택해 주세요." required="true" data-options="editable:false"></select>
							</td>
							<td align="center"><label>SQL점검팩</label></td>
							<td>
								<select id="sqlPerformanceP" name="sql_auto_perf_check_id" style='height:25px' class="w360 easyui-combobox required" requiredMsg="SQL 점검팩을 선택해 주세요." required data-options="panelHeight:'auto',editable:false,readonly:true ,prompt:'선택'" ></select>
							</td>
							
						</tr>
						<tr height="35px;">
							<td><label>ASIS DB</label><br><label>(원천DB)</label></td>
							<td>
								<select id="original_dbid" name="original_dbid" style='height:25px' data-options="editable:false,prompt:'선택'" class="w130 easyui-combobox required" requiredMsg="ASIS DB(원천DB)를 선택해 주세요."  required="true" ></select>
							</td>
							<td><label>TOBE DB</label><br><label>(목표DB)</label></td>
							<td>
								<select id="perf_check_target_dbid" name="perf_check_target_dbid" style='height:25px' data-options="editable:false,prompt:'선택'" class="w130 easyui-combobox required" requiredMsg="TOBE DB(목표DB)를 선택해 주세요." required="true"></select>
							</td>
							<td align="center"><label>수집기간</label></td>
							<td>
								<input type="text" id="perf_check_range_begin_dt" name="perf_check_range_begin_dt" style='height:25px' value="${startDate}" class="w90 datapicker easyui-datebox required" requiredMsg="수집기간을 확인해 주세요." data-options="panelHeight:'auto',editable:false,required:true,prompt:'선택'"/>
								<input type="text" id="perf_period_start_time" name="perf_period_start_time" style='height:25px' value="${startTime}" class="w70 datatime easyui-timespinner required" requiredMsg="수집기간을 확인해 주세요." data-options="panelHeight:'auto',editable:false,prompt:'선택'"/> ~
								<input type="text" id="perf_check_range_end_dt" name="perf_check_range_end_dt" style='height:25px' value="${endDate}" class="w90 datapicker easyui-datebox required" requiredMsg="수집기간을 확인해 주세요." data-options="panelHeight:'auto',editable:false,required:true,prompt:'선택'"/>
								<input type="text" id="perf_period_end_time" name="perf_period_end_time" style='height:25px' value="${endTime}" class="w70 datatime easyui-timespinner required" requiredMsg="수집기간을 확인해 주세요." data-options="panelHeight:'auto',editable:false,prompt:'선택'"/>
							</td>
							<td colspan="3">
								<span style="margin-left: 10px;"><label>전체</label></span>
								<input id="all_sql_yn_chk" name="all_sql_yn_chk" class="easyui-checkbox">
								<input type="hidden" id="all_sql_yn" name="all_sql_yn">
								
								<span class="topn_cnt">
									<label>TOP N</label>
									<input type="number" id="topn_cnt" name="topn_cnt" style='height:25px' class="w60 easyui-numberbox required" requiredMsg="TOP N을 입력해 주세요." required data-options="min:1,readonly:true"/>
								</span>
								
							</td>
						</tr>
					</table>
				</div>
				<div class="marginT10" style="border-bottom:3px solid darkgray; padding-bottom:5px;">
					<span><label style="font-weight:bold; font-size:14px; ">필터링</label></span>
				</div>
				<div class="well">
					<div>
						<span><label>TABLE_OWNER <a style="color:blue">in&nbsp;</a></label></span>
						<span class="owner_list" >
							<input type="text" id="owner_list" name="owner_list" style='height:25px' class="w300 easyui-searchbox"/>
						</span>
						<span><label>TABLE_NAME <a style="color:blue">in</a></label></span>
						<span class="table_name_list">
							<input type="text" id="table_name_list" name="table_name_list" style='height:25px' class="w300 easyui-searchbox"/>
						</span>
						<span><label>MODULE <a style="color:blue">like</a></label></span>
						<span class="module_list">
							<input type="text" id="module_list" name="module_list" style='height:25px' class="w300 easyui-searchbox"/>
						</span>
						<span style="margin-left:10px;">
							<a href="javascript:;" class="w80 easyui-linkbutton" id="btnFilterSQL" onClick="showFilterSQL();"><i class="btnIcon fas fa-plus-circle fa-lg fa-fw"></i> Filter SQL</a>
							<input type="text" id="extra_filter_predication" name="extra_filter_predication" style='height:25px' editable="false" class="w300 easyui-textbox"/>
						</span>
					</div>
				</div>
				<div class="marginT10" style="border-bottom:3px solid darkgray; padding-bottom:5px;">
					<span><label style="font-weight:bold; font-size:14px">인덱스</label></span>
				</div>
				<div class="well">
					<span><label >Selectivity 계산 :</label></span>
					<span style="margin-right:10px;">
						<input class="easyui-radiobutton" name="idx_selectvity_calc_meth_cd" id="statistics" value="1" checked>
						<label class="radioLabel">Statistics 기반</label>
						<span>
							<i id="selectivity_statistics_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
						</span>
					</span>
					<span>
						<input class="easyui-radiobutton" name="idx_selectvity_calc_meth_cd" id="dataSample" value="2">
						<label class="radioLabel">Data Sampling 기반</label>
						<span>
							<i id="selectivity_data_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
						</span>
					</span>
					
				</div>
				<div class="searchBtn marginB10" style="margin-top:10px;">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="getExcuteAnalyzeConstraint();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="forceCompleteAnalyze();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
				</div>
			</div>
			<div class="searchBtn marginB10">
				<label>수행중</label>
				<input id="inProgress" name="inProgress" checked class="easyui-checkbox">
				<label>완료</label>
				<input id="completion" name="completion" checked class="easyui-checkbox">
				<label>Refresh</label>
				<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
				<input type="number" id=timer_value name="timer_value" style="height:25px;" value="60" class="w40 easyui-numberbox" data-options="min:3"/><label>초</label>
				<a href="javascript:;" class="w20 easyui-linkbutton" onClick="fnSearch();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw" style="padding:7px 2px;" ></i></a>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:275px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn" >
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"style="margin-right:0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
			</form:form>
		</div>
	</div>

	<!-- ownerEditBox popup START -->
	<div id="ownerEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:375px;height:250px; !important">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
				<textarea id="ownerEdit" style="height: 97%; width:99%;"></textarea>
			</div>
		</div>
	</div>
	<!-- ownerEditBox popup END -->
	<!-- moduleEditBox popup START -->
	<div id="moduleEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:360px;height:250px; !important">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
				<textarea id="moduleEdit" style="height: 97%; width:99%;"></textarea>
			</div>
		</div>
	</div>
	<!-- moduleEditBox popup END -->
	<div id="table_nameEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:360px;height:250px; !important">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
				<textarea id="table_nameEdit" style="height: 97%; width:99%;"></textarea>
			</div>
		</div>
	</div>

	<%@include file="/WEB-INF/jsp/include/popup/authorityScript_popup.jsp" %>
	<%@include file="/WEB-INF/jsp/include/popup/filter_sql_perf_impact_popup.jsp" %> <!-- FILTER SQL 팝업 -->
</body>
</html>