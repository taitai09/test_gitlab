<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- <div id="dailyCheckDb" class="easyui-layout" data-options="border:false" style="width:100%;height:100%;"> -->
<div id="dailyCheckDb" class="easyui-layout" data-options="border:false">
	<input type="hidden" id="group_dbid" name="group_dbid" "/>
	<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
	<input type="hidden" id="check_day" name="check_day" value=""/>
	<input type="hidden" id="check_seq" name="check_seq" value=""/>
	<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}")/>
	<input type="hidden" id="base_period_value" name="base_period_value" value="2" />
	<input type="hidden" id="start_day" name="start_day" />
	<input type="hidden" id="end_day" name="end_day" />
	<input type="hidden" id="dbid_situation" name="dbid_situation" />
	<input type="hidden" id="start_first_analysis_day_bottom" name="start_first_analysis_day_bottom" />
	<input type="hidden" id="end_first_analysis_day_bottom" name="end_first_analysis_day_bottom" />
	
<!-- 	<div data-options="border:false" style="width:100%;min-height:720px"> -->
<!-- 	<div data-options="border:false" style="width:1680px;min-height:720px"> -->
<!-- 	<div data-options="border:false" style="width:1680px;height:100%;"> -->
<!-- 	<div data-options="border:false" style="width:1680px;height:720px;overflow-y:hidden;"> -->
<!-- 	<div data-options="border:false" style="width:100%;height:730px;border:1px solid blue;"> -->
	<div data-options="border:false" style="width:100%;height:730px;">
		<div id="dailyCheckDbTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
			<div title="DB 상태 점검" style="padding:5px;">
				<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
<!-- 				<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:1670px;"> -->
					<div class="well">
						<div>
							<div class="severity_0"></div>
							<span style="padding-right:10px;">
								<label for="severity_text_0">긴급조치</label>
							</span>
							<div class="severity_1"></div>
							<span style="padding-right:10px;">
								<label for="severity_text_1">조치필요</label>
							</span>
							<div class="severity_2"></div>
							<span style="padding-right:10px;">
								<label for="severity_text_2">확인필요</label>
							</span>
							<div class="severity_3"></div>
							<span id="severity_span_3" style="padding-right:10px;">
								<label for="severity_text_3">정상</label>
							</span>
							<div id="severity_div_4" class="severity_4"></div>
							<span id="severity_span_4" style="padding-right:10px;">
								<label for="severity_text_4">미수행</label>
							</span>
							
							<label>DB 그룹</label>
							<select id="perfDbGroupCombo" name="perfDbGroupCombo" data-options="panelHeight:'auto',editable:false" class="w130 easyui-combobox"></select>
							<label>심각도</label>
							<select id="perfSeverityCombo" name="perfSeverityCombo" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn excelBtn"><a href="javascript:;" class="w80 easyui-linkbutton" data-options="" onClick="fnOpenExceptionManageTab();">예외 관리</a></div>
						</div>
					</div>
				</div>
					
<!-- 				<div id="canvas" class="easyui-panel" data-options="border:false" style="width:100%;padding:5px;"> -->
				<div id="canvas" class="easyui-panel" data-options="border:false" style="width:100%;height:610px;padding-top:5px;">
					<!-- Test Area -->
<!-- 					<div> -->
<!-- 						<label style="font:Bold 18px Open Sans;">OPENPOP</label> -->
<!-- 					</div> -->
<%-- 					<canvas id="canvas1" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas2" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas3" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas4" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
					
<%-- 					<canvas id="canvas11" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas22" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas33" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
<%-- 					<canvas id="canvas44" width="540" height="160" style="padding:5px;border:1px solid #9f9f9f;"></canvas> --%>
					
<%-- 					<canvas id="canvas1" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas2" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas3" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas4" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
					
<%-- 					<canvas id="canvas11" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas22" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas33" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
<%-- 					<canvas id="canvas44" width="510" height="160" style="padding:5px;border:1px solid #9f9f9f;"> </canvas> --%>
					<!-- End Test Area -->
				</div>
			</div>
			<div title="DB 상태 점검 현황" style="padding:5px;">
				<style>
					.datagrid-header .datagrid-cell{padding:0px;}
					.datagrid-body .datagrid-cell{padding:0px;}
				</style>
				
				<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
					<div class="well">
						<div>
							<label>DB 그룹</label>
							<select id="perfDbGroupComboSituation" name="perfDbGroupComboSituation" data-options="editable:false" class="w120 easyui-combobox" required="true"></select>
							<span style="margin-left:20px;"/>
							<label>기준일자</label>
							<span id="span_analysis_day">
								<input type="text" id="start_first_analysis_day" name="start_first_analysis_day" value="${dailyCheckDb.start_first_analysis_day}" class="w100 datapicker easyui-datebox" required="true"/> ~
								<input type="text" id="end_first_analysis_day" name="end_first_analysis_day" value="${dailyCheckDb.end_first_analysis_day}" class="w100 datapicker easyui-datebox" required="true"/>
							</span>
							<span style="margin-left:5px;"/>
							<input class="easyui-radiobutton" id="base_weekly" name="base_weekly" value="1" label="1주일" labelPosition="after" labelWidth="55px;">
							<input class="easyui-radiobutton" id="base_monthly" name="base_monthly" value="2" label="1개월" labelPosition="after" labelWidth="55px;">
							<span style="margin-left:20px;"/>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClickSituation();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</div>
					</div>
				</div>
				<div class="easyui-panel" data-options="border:false" style="width:100%;height:300px;padding-top:5px;padding-bottom:5px;">
					<table id="tableList1" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div id="bottom" class="easyui-panel" data-options="iconCls:'fas fa-database',border:false" style="width:100%;height:300px;padding-top:5px;" title=" ">
					<table id="tableList2" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</div>
</div>