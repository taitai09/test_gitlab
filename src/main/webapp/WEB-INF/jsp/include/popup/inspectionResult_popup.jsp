<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
	#inspectionResultPop .checkArea{
		height: 23px;
		margin-bottom: 3px;
		padding: 6px 0px 2px 15px;
	}
	#inspectionResultPop .tableListArea{
		width: 100%;
		height: 100%;
		border: 1px solid #d0d0d0;
	}
	#sql_id_popup {
		font-size: inherit;
	}
	#detailResultPopup tbody tr td input{
		background-color: transparent;
		color: inherit;
	}
	
	table.detail_popup{width:100%; border-top:2px solid #38312a; border-right:1px solid lightgray; border-left:1px solid lightgray; margin-top:10px;}
	table.detail_popup th{text-align:center;vertical-align: middle;}
	table.detail_popup th,
	table.detail_popup td{border:1px solid #ddd;height:25px; padding:2px 5px;}
	table.detail_popup tr th{background-color:#F3F2F2;color:#333;font-size:11px; border-left:0px;}
	table.detail_popup tr td{text-overflow: ellipsis;overflow: hidden;word-wrap: break-word;word-break: break-all;vertical-align:middle;}
	table.detail_popup tr:not(:first-child) td{border-top:0px;}
	table.detail_popup tr td:not(:first-child){border-left:0px;}

</style>
<div id="inspectionResultPop" class="easyui-window popWin" style="background-color:#ffffff;width:1500px;height:670px;">
	<div style="float: left; width:19.3%;height:92%;padding: 5px;">
		<div class="well checkArea">
			<label>부적합</label>
			<input type="checkbox" id="onlyIncorrectYn" name="" class="easyui-checkbox">
		</div>
		
		<div class="tableListArea" style="overflow: auto;">
			<table id="popup_tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	
	<div style="float: right;width:80%;height:98%;margin-top: 9px;">
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
		
			<div data-options="region:'east',border:false" style="width:100%;height:92%;padding-left:2.5px;">
				<div id="popupTabs" class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div class="tabTxt" title="상세 검증 결과" style="width:100%;height:100%;">
						<div style="overflow: auto;">
						
							<table class="detail_popup" style="width:99%;border-top: none;margin: 5px 0px 0px 5px;">
								<colgroup>
									<col style="width:6%;">
									<col style="width:84%;">
								</colgroup>
								<tr class="tr_perf_check_result_table">
									<th>상세 검증 결과</th>
									<td>
										<table id="detailResultPopup" class="detailT3" style="width:100%;">
											<colgroup>
												<col style="width:21%;">
												<col style="width:10%;">
												<col style="width:10%;">
												<col style="width:10%;">
												<col style="width:10%;">
												<col style="width:39%;">
											</colgroup>
											<thead>
												<tr>
													<th rowspan="2">검증 지표</th>
													<th rowspan="2">적합</th>
													<th>성능 검증 결과값</th>
													<th rowspan="2">성능 검증 결과</th>
													<th rowspan="2">예외등록여부</th>
													<th rowspan="2">성능검증 결과내용</th>
												</tr>
												<tr>
													<th>실제</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</td>
								</tr>
								<tr class="tr_perf_impr_guide">
									<th>개선가이드</th>
									<td id="td_perf_impr_guide">
										<table class="detailT3" id="perf_impr_guide_table_popup" style="width:100%;">
											<colgroup>
												<col style="width:21%;">
												<col style="width:40%;">
												<col style="width:39%;">
											</colgroup>
											<thead>
											<tr>
												<th>점검지표</th>
												<th>지표설명</th>
												<th>개선가이드</th>
											</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
										
									</td>
								</tr>
								<tr id="tr_exec_plan">
									<th>Execution Plan</th>
									<td style="width:100%;padding:10px;height:500px;">
										<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%;">
											<table id="ExecutionPlanList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</td>
								</tr>
							</table>
						
						</div>
					</div>
					
					<div class="tabTxt" title="SQL" style="width:100%;height:100%;">
						<div class="easyui-layout" data-options="fit:true" style="height:100%;">
							<div data-options="region:'north',border:false" style="width:100%;height:7%;padding-right:2.5px;">
								<div class="panel-title" style="margin-top: 10px;">SQL Info( <span id="sql_id_popup"></span> )</div>
							</div>
							<div data-options="region:'west',border:false" style="width:70%;height:95%;padding-right:2.5px;">
								<div class="easyui-tabs" data-options="fit:true,border:false">
									<div class="tabTxt" title="SQL TEXT" style="width:100%;height:100%;padding:5px;">
										<textarea name="sql_text" id="textArea" style="margin-top:5px;padding:5px;width:98%;height:95%" wrap="off" readonly>
										</textarea>
									</div>
									<form:form method="post" id="submit_form_popup" class="form-inline">
										<input type="hidden" name="dbid" value="">
										<input type="hidden" name="top_wrkjob_cd" value="">
										<input type="hidden" name="wrkjob" value="">
										<input type="hidden" name="menu_nm" value="">
										<input type="hidden" name="sql_id" value="">
										<input type="hidden" name="sql_desc" value="">
										<input type="hidden" name="perf_check_id" value="">
										<input type="hidden" name="perf_check_step_id" value="">
										<input type="hidden" name="program_id" value="">
										<input type="hidden" name="program_execute_tms" value="">
										
										<input type="hidden" name="elapsed_time" value="">
										<input type="hidden" name="buffer_gets" value="">
										<input type="hidden" name="executions" value="">
										<input type="hidden" name="rows_processed" value="">
									</form:form>
								</div>
							</div>
							<div data-options="region:'east',border:false" style="width:30%;height:95%;padding-left:2.5px;">
								<div class="easyui-tabs" data-options="fit:true,border:false">
									<div title="Bind Value" class="tabGrid" style="width:100%;padding:10px;height:100%;">
										<table id="bindValueList" class="tbl easyui-datagrid" data-options="fit:true,border:true">
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div data-options="region:'south',border:false" style="height:8%;" >
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
					<div data-options="region:'west',border:false" style="width:70%;height:92%;">
						<div class="inlineBtn westButton" style="padding-top:8px;padding-right:5px;float: right;">
							<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_RequestTuning();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-recycle fa-lg fa-fw"> </i> SQL Format</a>
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="copy_to_sqlId();">SQL ID 복사</a>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">SQL 복사</a>
						</div>
					</div>
					
					<div data-options="region:'east',border:false" style="width:30%;height:92%;">
						<div class="inlineBtn" style="padding-top:8px;padding-right:5px;float: right;">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_CloseInspectResultPop();"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>