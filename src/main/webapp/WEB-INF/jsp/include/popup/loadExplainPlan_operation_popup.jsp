<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
	#asis_plan, #tobe_plan{
		height:465px !important;
		position: absolute;
	}
	#asisTextPlan, #tobeTextPlan{
		position: absolute;
	}
</style>
<div id="loadExplainPlanPop" class="easyui-window popWin" style="background-color:#ffffff;width:1600px;height:600px;">
	<div class="easyui-layout" data-options="fit:true" style="height:100%;">
		<form:form method="post" id="loadExplainPlan_form" name="loadExplainPlan_form" enctype="multipart/form-data" class="form-inline">
			<input type="hidden" id="dbid" name="dbid" value="">
			<input type="hidden" name="sql_id" value="">
			<input type="hidden" name="project_id" value="">
			<input type="hidden" name="plan_hash_value" value="">
			<input type="hidden" name="asis_plan_hash_value" value="">
			<input type="hidden" name="tobe_plan_hash_value" value="">
			<input type="hidden" name="sql_command_type_cd" value="">
			<input type="hidden" name="sql_auto_perf_check_id" value="">
			<input type="hidden" name="perf_check_sql_source_type_cd" value="">
			<input type="hidden" name="tobe_executions" value="">
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
		</form:form>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:515px">
			<div data-options="region:'west',border:false" style="width:50%;height:100%;padding-right:5px;">
				<div class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div class="tabTxt" title="SQL TEXT" style="width:100%;padding:5px;height:400px;">
						<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:98%;height:96%" wrap="off" readonly></textarea>
					</div>
				</div>
			</div>
			
			<div data-options="region:'center',split:false,border:false" style="width:100%;height:100%;">
				<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false" style="width:100%;height:100%; min-height: 484px;">
					<div title="Bind Value" class="tabGrid" style="width:100%;padding:10px;height:100%;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
							<table id="bindValueList" class="tbl easyui-datagrid" data-options="fit:true,border:true">
							</table>
						</div>
					</div>
					<div title="ASIS Plan" class="tabGrid" id="asis_plan" style="padding:5px;">
						<ul id="asisTextPlan" class="easyui-text" style="padding:5px;"></ul>
						<textarea id="asisTextPlan_h" name="operTextPlan_h" style="width: 0px; height:0px;"></textarea>
					</div>
					<div title="운영 Plan" class="tabGrid" id="tobe_plan" style="padding:5px;">
						<ul id="operTextPlan" class="easyui-text" style="padding:5px;"></ul>
						<textarea id="operTextPlan_h" name="operTextPlan_h" style="width: 0px; height:0px;"></textarea>
					</div>
					
					<div title="성능 점검 결과" class="tabGrid tabPerfChkResult" class="easyui-panel" data-options="border:true" style="padding:5px; overflow-y:visible;border-left:0px;">
						<table class="detailT">
							<tr id="tr_perf_check_result_table">
								<td>
									<table class="detailT3" id="detailCheckResultTable">
										<colgroup>
											<col style="width:200px;">
											<col style="width:70px;">
											<col style="width:90px;">
											<col style="width:110px;">
											<col style="width:100px;">
											<col style="width:300px;">
										</colgroup>
										<thead>
										<tr>
											<th>점검 지표</th>
											<th>적합</th>
											<th>부적합</th>
											<th>성능 점검 결과값</th>
											<th>성능 점검 결과</th>
											<th>성능점검 결과내용</th>
										</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div region='south' style="text-align:right;height:46px;padding-top:9px;padding-right:5px;" >
			<div style="display: flex;">
				<div style="width:50%;">
					<a href="javascript:;" class="w100 easyui-linkbutton sqlMemo" id="btnSqlReview" style="visibility:hidden;" onClick="Btn_SetSQLmemo();"> SQL 검토결과</a>
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-recycle fa-lg fa-fw"> </i> SQL Format</a>
					<a href="javascript:;" class="w100 easyui-linkbutton" id="sqlIdCopy" onClick="copy_to_sqlId();">SQL ID 복사</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" style="display:none;" id="tiberoSqlIdCopy" onClick="copy_to_TiberoSqlId();">TIBERO SQL ID 복사</a>
					<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">SQL 복사</a>
				</div>
				<div style="width:50%;">
					<a href="javascript:;" class="w120 easyui-linkbutton" id="planCompare" style="visibility:hidden;"> PLAN 비교 </a>
					<a href="javascript:;" class="w120 easyui-linkbutton" id="asisPlanCopy" onClick="asisTextPlanCopy();"> ASIS PLAN 복사 </a>
					<a href="javascript:;" class="w120 easyui-linkbutton" id="operPlanCopy" onClick="operTextPlanCopy();"> 운영 PLAN 복사 </a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_CloseLoadExplainPlanPop();"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</div>
	</div>
</div>
