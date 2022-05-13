<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="tuningAssignPopSqls" class="easyui-window popWin" style="background-color:#ffffff;width:490px;height:240px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="assign_form" name="assign_form" class="form-inline">
			<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
				<input type="hidden" id="dbid_array" name="dbid_array"/>
				<input type="hidden" id="after_prd_sql_id_array" name="after_prd_sql_id_array"/>
				<input type="hidden" id="after_prd_plan_hash_value_array" name="after_prd_plan_hash_value_array"/>
				<input type="hidden" id="perf_check_step_id_array" name="perf_check_step_id_array"/>
				<input type="hidden" id="wrkjob_cd_array" name="wrkjob_cd_array"/>
				<input type="hidden" id="executions_array" name="executions_array"/>
				<input type="hidden" id="avg_buffer_gets_array" name="avg_buffer_gets_array"/>
				<input type="hidden" id="avg_elapsed_time_array" name="avg_elapsed_time_array"/>
				<input type="hidden" id="avg_row_processed_array" name="avg_row_processed_array"/>
				<input type="hidden" id="perf_check_id_array" name="perf_check_id_array"/>
				<input type="hidden" id="program_id_array" name="program_id_array"/>
				<input type="hidden" id="dbio_array" name="dbio_array"/>
				<input type="hidden" id="auto_share" name="auto_share"/>
				<input type="hidden" id="perfr_id" name="perfr_id"/>
				<input type="hidden" id="choice_div_cd" name="choice_div_cd"/> <!-- FULL SCAN SQL : 4, TEMP_USAGE_SQL : 7, PLAN_CHANGE_SQL : 5, NEW_SQL : 6 -->
				
				<table class="noneT_popup">
						<colgroup>	
							<col style="width:25%;">
							<col style="width:15%;">
							<col style="width:15%;">
							<col style="width:25%;">
						</colgroup>
						<tr>
							<th >튜닝담당자 자동할당</th>
							<td class="ltext">
						<input type="checkbox" id="chkAutoShare" name="chkAutoShare" value="" data-options="panelHeight:'auto',editable:false" class="w60 easyui-switchbutton" required="true"/>
							</td>
							<th >튜닝담당자</th>
								<td class="ltext">
								<select id="selectTuner" name="selectTuner" data-options="panelHeight:'300',editable:false" class="w110 easyui-combobox" required="true"></select>
							</td>
						</tr>
						<tr>
							<th >프로젝트</th>
							<td colspan="3" class="ltext">
								<select id="project_id" name="project_id" data-options="editable:false"  class="w290 easyui-combobox"></select>
							</td>
						</tr>
						<tr>
							<th>튜닝진행단계</th>
							<td colspan="3"  class="ltext">
								<select id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" data-options="editable:false"  class="w290 easyui-combobox">
									<option value="">해당없음</option>
								</select>
							</td>
						</tr>
					</table>
					
					
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
				<div class="searchBtn" style="padding-right:35px;">
					<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_SaveAssign();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청 및 담당자지정</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('tuningAssignPopSqls');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>	
<div id="tuningAssignAllPopSqls" class="easyui-window popWin" style="background-color:#ffffff;width:270px;height:150px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="assignAll_form" name="assignAll_form" class="form-inline">
			<input type="hidden" id="perfr_id" name="perfr_id"/>
			<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
			<input type="hidden" id="tuningStatusArry" name="tuningStatusArry"/>
			<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
				<label>튜닝담당자</label>
				<select id="selectTuner" name="selectTuner" data-options="panelHeight:'auto'" class="w140 easyui-combobox"></select>
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
				<div class="searchBtn" style="padding-right:35px;">
					<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SaveAssignAll();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 담당자 지정</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('tuningAssignAllPopSqls');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>