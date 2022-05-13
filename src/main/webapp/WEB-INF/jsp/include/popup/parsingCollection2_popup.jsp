<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="snapShotPop" class="easyui-window popWin" style="background:#ffffff;width:530px;height:180px;overflow: hidden;">
	<div class="easyui-layout" style="height:160px;margin:10px;">
		<div data-options="region:'north',border:false" style="height:140px;">
			<div class="well">
				<form:form method="post" id="snapShot_form" name="snapShot_form" class="form-inline">
					<input type="hidden" id="dbid" name="dbid"/>
						<div id="a">
							<label>DB</label>		
							<input type="text" id="dbName" name="dbName" data-options="disabled:true" class="w100 easyui-textbox"/>
						</div>
						<div id="b" style="margin-top:10px;">
						<label>수집일시 </label>
							<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" class="w90 datapicker easyui-datebox"/>
							<input type="text" id="strStartTime" name="strStartTime" value="${startTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:true"/> ~ 
							<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" class="w90 datapicker easyui-datebox"/>
							<input type="text" id="strEndTime" name="strEndTime" value="${nowTime}" class="w80 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:true"/>
						</div>
						
					</form:form>
				</div>
			<div class="searchBtn" style="margin-top:10px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="insertParsingCollectionTerms();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 파싱</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('snapShotPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>		
		<!-- <div data-options="title:'시작 스냅샷 번호',region:'west',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="snapSList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div data-options="region:'center',border:false" style="width:10px;">
		</div>
		<div data-options="title:'종료 스냅샷 번호',region:'east',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="snapEList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div> -->
	</div>
</div>