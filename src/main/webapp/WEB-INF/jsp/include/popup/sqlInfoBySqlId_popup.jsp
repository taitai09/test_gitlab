<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>

<style type="text/css">
	#sqlInfoBySqlId_popup .detailT4 tbody tr td:first-child{
		border-left:none;
	}
	#sqlInfoBySqlId_popup .float{
		float: right;
	}
	#sqlInfoBySqlId_popup .leftWidth{
		width:17%;
		height:100%;
	}
	#sqlInfoBySqlId_popup .fitExtend{
		width:100%;
		height:100%;
	}
	#sqlInfoBySqlId_popup #asisTextPlan {
		position: absolute;
		top: -500px;
		left: -500px;
		width: 1px;
		height: 1px;
		margin: 0;
		padding:0;
		border: 0;
	}
	#sqlInfoBySqlId_popup #sqlInfoBySqlIdTab .datagrid-row-over,
	#sqlInfoBySqlId_popup #sqlInfoBySqlIdTab .datagrid-row-checked,
	#sqlInfoBySqlId_popup #sqlInfoBySqlIdTab .datagrid-row-selected {
		cursor: default;
		color: #000;
		background-color: inherit;
	}
</style>

<div id="sqlInfoBySqlId_popup" class="easyui-window popWin" style="background-color:#ffffff;width:1600px;height:600px;">
	<div class="easyui-layout" data-options="fit:true" style="height:100%;">
		<form:form method="post" id="sqlInfoBySqlId_form" name="sqlInfoBySqlId_form" enctype="multipart/form-data" class="form-inline">
			<input type="hidden" id="database_kinds_cd_popup" name="database_kinds_cd" value="${database_kinds_cd}"/>
			<input type="hidden" id="project_id_popup" name="project_id" value="">
			<input type="hidden" id="perf_check_id_popup" name="sql_auto_perf_check_id" value="">
			<input type="hidden" id="table_owner_popup" name="owner" value="">
			<input type="hidden" id="table_name_popup" name="table_name" value="">
			<input type="hidden" id="dbid_popup" name="dbid" value="">
			<input type="hidden" id="access_path_popup" name="access_path" value="">
			<input type="hidden" id="exec_seq_popup" name="exec_seq" value="">
			<input type="hidden" id="begin_dt_popup" name="begin_dt" value="">
			<input type="hidden" id="end_dt_popup" name="end_dt" value="">
			
			<input type="hidden" id="sql_id_popup" name="sql_id" value="">
			<input type="hidden" id="plan_hash_value_popup" name="plan_hash_value" value="">
		</form:form>
		
		<div style="width:100%;height:100%;">
			<div class="leftWidth" style="float: left; padding-right:5px;">
				<table id="sqlIdList" class="tbl easyui-datagrid leftWidth" data-options="fit:true,border:true">
					<tbody><tr></tr></tbody>
				</table>
			</div>
			
			<div style="float: left;width:82.68%;height:100%;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:50%;">
				
					<div data-options="region:'west',border:false" style="width:49.85%;height:100%;">
						<div class="easyui-tabs fitExtend" data-options="fit:false,border:false" style="height:100%;">
							<div title="SQL TEXT" class="tabGrid fitExtend" style="padding:5px 0px 0px 5px;">
								<textarea id="sqlText" style="padding:5px;width:98%;height:95%" wrap="off" readonly></textarea>
								<textarea id="sqlText_copy" style="width: 0px; height:0px;"></textarea>
								<div id="asisTextPlan"></div>
							</div>
						</div>
					</div>
					
					<div data-options="region:'east',border:false" style="width:49.85%;height:100%;">
						<div id="sqlInfoBySqlIdTab" class="easyui-tabs fitExtend" data-options="fit:false,border:false" style="height:100%;">
							<div title="Bind Value" class="tabGrid fitExtend" style="padding:10px;">
								<div class="easyui-layout fitExtend" data-options="border:false">
									<table id="bindValueList" class="tbl easyui-datagrid" data-options="fit:true,border:true">
										<tbody><tr></tr></tbody>
									</table>
								</div>
							</div>
							
							<div title="ASIS Plan" class="tabGrid" id="asis_plan" style="padding:5px;">
								<ul id="asisPlan" class="easyui-tree" style="width:96%;height:95%;padding:5px;"></ul>
							</div>
						</div>
					</div>
				</div>
				
				<div class="searchBtn" style="width: 100%; height: 26px; margin: 10px 5px 10px;">
					<div style="width: 50%; float: left;">
						<a href="javascript:;" class="w60 easyui-linkbutton float" style="margin-right: 0px;" onClick="copy_to_clipboard();">SQL 복사</a>
						<a href="javascript:;" class="w100 easyui-linkbutton float" onClick="copy_to_sqlId();">SQL ID 복사</a>
						<a href="javascript:;" class="w110 easyui-linkbutton float" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
						
					</div>
					<div style="width: 50%; float: left;">
						<a href="javascript:;" class="w80 easyui-linkbutton float" onClick="Btn_OnClosePopup('sqlInfoBySqlId_popup');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
						<a href="javascript:;" class="w120 easyui-linkbutton float" id="asisPlanCopy" data-clipboard-action="copy" data-clipboard-target="#asisTextPlan"> ASIS PLAN 복사 </a>
					</div>
				</div>
				
				<div style="height:43%; margin-top:5px;">
					<table class="detailT4 fitExtend">
						<colgroup>
							<col style="width: 25%;">
							<col style="width: 25%;">
							<col style="width: 25%;">
							<col style="width: 25%;">
						</colgroup>
						<thead style="height:5px;">
							<tr>
								<th>Executions</th>
								<th>Elapsed Time</th>
								<th>Buffer Gets</th>
								<th>Disk Reads</th>
							</tr>
						</thead>
						<tbody>
							<td>
								<div id="chartExecutionsPanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartElapsedTimePanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartBufferGetsPanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartDiskReadsPanel" class="fitExtend">
								</div>
							</td>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
