<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.12.23	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>Scheduler 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/examineOpenPOP/examineScheduler.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging_variable.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:740px;">
			<div title="스케줄러 점검 현황" data-options="region:'west',split:false,collapsible:true,border:true" style="width:340px;height:100%;padding:5px;">
				<div class="easyui-layout" data-options="border:false" style="width:325px;min-height:685px;">
					<form:form method="post" id="submit_form" name="submit_form" class="form-inline" style="width:325px;">
						<input type="hidden" id="user_id" name="user_id" />
						
						<input type="hidden" id="base_day" name="base_day" />
						<input type="hidden" id="error_yn" name="error_yn" value="N" />
						
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:27px;margin-top:5px;">
							<label>기준일자</label>
							<input type="text" id="endDate" name="endDate" value="${endDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
							<label>스케줄러 오류</label>
							<input type="checkbox" id="schedulerErrorSwitch" name="schedulerErrorSwitch" data-options="disabled:false" class="easyui-switchbutton"/>
						</div>
						<div class="easyui-panel" data-options="border:false" style="height:27px;margin-top:5px;">
							<div style="float:left;width:55px;margin-top:5px;margin-left:130px;"><img src="/resources/images/examine_scheduler/normal_status.png" style="vertical-align:bottom;"/> <b>정상</b></div>
							<div style="float:left;width:55px;margin-top:5px;"><img src="/resources/images/examine_scheduler/error_status.png" style="vertical-align:bottom;"/> <b>오류</b></div>
							<div style="float:left;width:55px;margin-top:5px;"><img src="/resources/images/examine_scheduler/unexecuted_status.png" style="vertical-align:bottom;"/> <b>미수행</b></div>
						</div>
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:620px;margin-bottom:0px;">
							<table id="schedulerStatusList" class="tbl easyui-datagrid" style="width:100%;height:100%;" data-options="fit:true,border:false">
							</table>
						</div>
					</form:form>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:100%;padding:0px;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
					<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:500px">
						<div data-options="region:'north',border:true" style="width:100%;height:325px;padding:0px;">
							<div id="schedulerHistoryTab" class="easyui-tabs grayTabs" data-options="fit:true,border:false">
								<div title="스케줄러 수행 내역" data-options="fit:true" style="padding:5px 0px 5px 5px;">
									<form:form method="post" id="scheduler_history_form" name="scheduler_history_form" class="form-inline">
										<div class="easyui-panel searchArea35" data-options="border:false" style="width:100%;">
											<div class="well2">
												<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
												<input type="hidden" id="dbid" name="dbid"/>
												<input type="hidden" id="job_exec_no" name="job_exec_no" />
												<input type="hidden" id="job_scheduler_type_cd" name="job_scheduler_type_cd" value="${jobSchedulerExecLog.job_scheduler_type_cd}"/>
												<input type="hidden" id="job_scheduler_type_nm" name="job_scheduler_type_nm"/>
												<input type="hidden" id="list_job_scheduler_type_cd" name="list_job_scheduler_type_cd"/>
												<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
												<input type="hidden" id="error_yn" name="error_yn" value="N" />
												<!-- 이전, 다음 처리 -->
												<input type="hidden" id="currentPage" name="currentPage" value="${jobSchedulerExecLog.currentPage}"/>
												<input type="hidden" id="pagePerCount" name="pagePerCount" value="${jobSchedulerExecLog.pagePerCount}"/>
												
												<label>기준일자</label>
												<input type="text" id="strStartDt" name="strStartDt" value="${jobSchedulerExecLog.strStartDt}" data-options="panelHeight:'auto',editable:false" class="w90 datapicker easyui-datebox"/> ~
												<input type="text" id="strEndDt" name="strEndDt" value="${jobSchedulerExecLog.strEndDt}" data-options="panelHeight:'auto',editable:false" class="w90 datapicker easyui-datebox"/>
												<label>DB</label>
												<select id="selectCombo" name="selectCombo" data-options="editable:true" class="w120 easyui-combobox"></select>
												<label>스케줄러 유형</label>
												<select id="schedulerTypeCd" name="schedulerTypeCd" data-options="panelHeight:'auto',editable:false" class="w230 easyui-combobox"></select>
												<label>스케줄러 오류</label>
												<input type="checkbox" id="schedulerErrorSwitch" name="schedulerErrorSwitch" data-options="disabled:false" class="easyui-switchbutton"/>
												<span class="searchBtnLeft">
													<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
												</span>
												<div class="searchBtn">
												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
												</div>
											</div>
										</div>
										<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:0px;min-height:200px;margin-bottom:5px;">
											<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
										<div class="searchBtn" data-options="collapsible:false,border:false" style="height:26px;padding-top:5px;text-align:right;">
											<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
											<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
										</div>
									</form:form>
								</div>
							</div>
						</div>
						<div data-options="region:'center',border:true" style="width:100%;height:425px;padding:0px;">
							<div id="schedulerHistoryDetailTab" class="easyui-tabs grayTabs" data-options="fit:true,border:false">
								<div title="스케줄러 상세 수행 내역" data-options="fit:true" style="padding:5px 0px 5px 5px;">
									<form:form method="post" id="scheduler_history_detail_form" name="scheduler_history_detail_form" class="form-inline">
										<div class="easyui-panel searchArea35" data-options="border:false" style="width:100%;">
											<div class="well2">
												<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
												<input type="hidden" id="dbid" name="dbid" value="${jobSchedulerExecDetailLog.dbid}"/>
												<input type="hidden" id="strStartDt" name="strStartDt" value="${jobSchedulerExecDetailLog.strStartDt}"/>
												<input type="hidden" id="strEndDt" name="strEndDt" value="${jobSchedulerExecDetailLog.strEndDt}"/>
												<input type="hidden" id="job_exec_no" name="job_exec_no" value="${jobSchedulerExecDetailLog.job_exec_no}"/>
												<input type="hidden" id="list_job_scheduler_type_cd" name="list_job_scheduler_type_cd" value="${jobSchedulerExecDetailLog.list_job_scheduler_type_cd}"/>
												<input type="hidden" id="job_scheduler_type_cd" name="job_scheduler_type_cd" value="${jobSchedulerExecDetailLog.job_scheduler_type_cd}"/>
												<input type="hidden" id="job_err_yn" name="job_err_yn" value="${jobSchedulerExecDetailLog.job_err_yn}"/>
												<input type="hidden" id="hndop_job_exec_yn" name="hndop_job_exec_yn" value="${jobSchedulerExecDetailLog.hndop_job_exec_yn}"/>
												<input type="hidden" id="menu_nm" name="menu_nm" value="${jobSchedulerExecDetailLog.menu_nm}"/>
												
												<label>작업 오류 조회</label>
												<input type="checkbox" id="chkJobErrYn" name="chkJobErrYn" value="" class="w120 easyui-switchbutton"/>
												<label>수동 작업 조회</label>
												<input type="checkbox" id="chkHndopJobExecYn" name="chkHndopJobExecYn" value="" class="w120 easyui-switchbutton"/>
											</div>
										</div>
										<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:0px;min-height:336px;margin-bottom:5px;border-bottom:1px solid gray;">
											<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</form:form>
								</div>
							</div>
						</div>
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