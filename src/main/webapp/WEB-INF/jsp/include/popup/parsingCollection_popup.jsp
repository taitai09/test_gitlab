<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="snapShotPop" class="easyui-window popWin" style="background:#ffffff;width:950px;height:570px;">
	<div class="easyui-layout" style="height:500px;margin:10px;">
		<div data-options="region:'north',border:false" style="height:80px;">
			<div class="well">
				<form:form method="post" id="snapShot_form" name="snapShot_form" class="form-inline">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="snap_s_no" name="snap_s_no"/>
					<input type="hidden" id="snap_e_no" name="snap_e_no"/>						
					<label>DB</label>		
					<input type="text" id="dbName" name="dbName" data-options="disabled:true" class="w130 easyui-textbox"/>
					<label>SNAP_DT</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" class="w130 datapicker easyui-datebox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="searchStartSnapNoList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="insertParsingCollectionTerms();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 파싱</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('snapShotPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
					</div>
				</form:form>
			</div>
		</div>		
		<div data-options="title:'시작 스냅샷 번호',region:'west',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="snapSList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div data-options="region:'center',border:false" style="width:10px;">
		</div>
		<div data-options="title:'종료 스냅샷 번호',region:'east',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="snapEList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
</div>