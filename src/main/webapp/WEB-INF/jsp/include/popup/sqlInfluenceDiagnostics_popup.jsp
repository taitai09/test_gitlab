<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="topSqlSchedulePop" class="easyui-window popWin" style="background-color:#ffffff;width:700px;height:270px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
			<form:form method="post" id="topSql_form" name="topSql_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="before_perf_expect_no" name="before_perf_expect_no"/>
				<input type="hidden" id="crud_flag" name="crud_flag"/>
				<input type="hidden" id="immediately_yn" name="immediately_yn" value="N"/>
				<input type="hidden" id="snap_id" name="snap_id"/>
				<table class="detailT" style="width:91%;">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>	
						<th>DB</th>
						<td><select id="selectDbidCombo" name="selectDbidCombo" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select></td>
						<th>즉시실행</th>
						<td><input type="checkbox" id="chkImmediately" name="chkImmediately" value="" class="w120 easyui-switchbutton"/>&nbsp;&nbsp;&nbsp;<span id="topSqlImmediatelySpan" style="font-weight:700;color:red;"></span></td>
					</tr>
					<tr>	
						<th>SNAP ID</th>
						<td colspan="3">
							<input type="text" id="begin_snap_id" name="begin_snap_id" data-options="panelHeight:'auto',editable:false" required="true" class="w80 easyui-textbox"/> ~
							<input type="text" id="end_snap_id" name="end_snap_id" data-options="panelHeight:'auto',editable:false" required="true" class="w80 easyui-textbox"/>
							&nbsp;&nbsp;<a href="javascript:;" id="snapBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="showSnapPopup('sql');"></a>
						</td>
					</tr>
					<tr>	
						<th>작업수행일자</th>
						<td><input type="text" id="expect_work_exec_day" name="expect_work_exec_day" value="${nowDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/></td>
						<th>작업수행시간</th>
						<td><input type="text" id="expect_work_exec_time" name="expect_work_exec_time" value="${nowTime}" data-options="panelHeight:'auto',editable:true" required="true" class="w80 easyui-timespinner"/></td>
					</tr>
					<tr>	
						<th>TOP순위유형</th>
						<td><select id="top_rank_measure_type_cd" name="top_rank_measure_type_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select></td>
						<th>TOP N</th>
						<td><input type="number" id="topn_cnt" name="topn_cnt" data-options="panelHeight:'auto'" required="true" class="w80 easyui-textbox"/></td>
					</tr>
				</table>
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" id="removeSqlBtn" class="w80 easyui-linkbutton" onClick="Btn_ScheduleDelete('sql');"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
				<a href="javascript:;" id="saveSqlBtn" class="w80 easyui-linkbutton" onClick="Btn_ScheduleSave('sql');"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('topSqlSchedulePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="objectSchedulePop" class="easyui-window popWin" style="background-color:#ffffff;width:700px;height:550px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:10px;margin-left:5px;">
			<form:form method="post" id="object_form" name="object_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="snap_id" name="snap_id"/>
				<input type="hidden" id="sql_perf_impl_anal_no" name="sql_perf_impl_anal_no"/>
				<input type="hidden" id="crud_flag" name="crud_flag"/>
				<input type="hidden" id="table_owner" name="table_owner"/>				
				<input type="hidden" id="immediately_yn" name="immediately_yn" value="N"/>
				<input type="hidden" id="tableOwnerArry" name="tableOwnerArry"/>
				<input type="hidden" id="tableNameArry" name="tableNameArry"/>
				<table class="detailT" style="width:96%;">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>	
						<th>DB</th>
						<td><select id="selectDbidCombo" name="selectDbidCombo" data-options="panelHeight:'auto',editable:false" required="true" class="w130 easyui-combobox"></select></td>
						<th>즉시실행</th>
						<td><input type="checkbox" id="chkImmediately" name="chkImmediately" value="" class="w120 easyui-switchbutton"/>&nbsp;&nbsp;&nbsp;<span id="objectImmediatelySpan" style="font-weight:700;color:red;"></span></td>
					</tr>
					<tr>	
						<th>SNAP ID</th>
						<td colspan="3">
							<input type="text" id="begin_snap_id" name="begin_snap_id" data-options="panelHeight:'auto',editable:false" required="true" class="w80 easyui-textbox"/> ~
							<input type="text" id="end_snap_id" name="end_snap_id" data-options="panelHeight:'auto',editable:false" required="true" class="w80 easyui-textbox"/>
							&nbsp;&nbsp;<a href="javascript:;" id="snapOBtn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="showSnapPopup('object');"></a>
						</td>
					</tr>
					<tr>	
						<th>작업수행일자</th>
						<td><input type="text" id="anal_work_exec_day" name="anal_work_exec_day" value="${nowDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/></td>
						<th>작업수행시간</th>
						<td><input type="text" id="anal_work_exec_time" name="anal_work_exec_time" value="${nowTime}" data-options="panelHeight:'auto',editable:true" required="true" class="w80 easyui-timespinner"/></td>
					</tr>
				</table>
				<table style="width:96%;margin-left:10px; table-layout: fixed;height:330px;">	
					<tr>	
						<td>
							<div class="easyui-layout" data-options="border:false" style="width:100%;height:300px;">
								<div data-options="region:'north',border:false" style="height:35px;">
									<div class="easyui-layout" data-options="fit:true,border:false">
										<div data-options="region:'west',border:false" style="width:42%;">
											<span style="font-size:12px;">▶ 테이블 목록</span>&nbsp;&nbsp;&nbsp;&nbsp;
											<select id="selTableOwner" name="selTableOwner" data-options="panelHeight:'300px'" required="true" class="w130 easyui-combobox"></select>
										</div>
										<div data-options="region:'center',border:false">
										</div>
										<div data-options="region:'east',border:false" required="true" style="width:50%;">
											<span style="font-size:12px;">▶ 분석대상 테이블 목록</span>
										</div>
									</div>								
								</div>
								<div data-options="region:'center',border:false">
									<div class="easyui-layout" data-options="fit:true,border:false">
										<div data-options="region:'west'" style="width:42%;">
											<table id="selectList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
										<div data-options="region:'center', border:false">
											<div style="width:100%;text-align:center;margin-top:100px;">
												<a href="javascript:;" id="addBtn" class="easyui-linkbutton" data-options="iconCls:'icon-next'" onClick="Btn_AddTarget();"></a>											
											</div>
											<div style="width:100%;text-align:center;margin-top:30px;">
												<a href="javascript:;" id="removeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onClick="Btn_RemoveTarget();"></a>
											</div>
										</div>
										<div data-options="region:'east'" style="width:50%;">
											<table id="targetList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</div>								
								</div>
							</div>
						</td>
					</tr>
				</table>
			</form:form>
			<span style="color:#ff0000;font-weight:700;">&nbsp;&nbsp;※ 분석대상 테이블 목록은 "저장" 후에 DB에 저장됩니다.</span> 
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" id="removeObjectBtn" class="w80 easyui-linkbutton" onClick="Btn_ScheduleDelete('object');"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
				<a href="javascript:;" id="saveObjectBtn" class="w80 easyui-linkbutton" onClick="Btn_ScheduleSave('object');"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('objectSchedulePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>