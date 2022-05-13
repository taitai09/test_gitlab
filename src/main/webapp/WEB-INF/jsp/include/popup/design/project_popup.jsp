<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="projectListPop" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:650px;visibility:hidden;">
	<div class="easyui-layout" style="height:580px;margin:10px;">
		<form:form method="post" id="projectList_form" name="projectList_form" class="form-inline">
			<div data-options="region:'north',border:false" style="width:100%;height:80px;">
				<div class="well">
					<input type="hidden" id="dbid" name="dbid"/>
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${sqlAutomaticPerformanceCheck.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlAutomaticPerformanceCheck.pagePerCount}"/>
					
					<label>프로젝트명</label>
					<input type="text" id="project_nm" name="project_nm" value="" class="w200 easyui-textbox"/>
					<label>종료여부</label>
					<select id="del_yn" name="del_yn" data-options="panelHeight:'auto',editable:false" class="w50 easyui-combobox">
						<option value="">ALL</option>
						<option value="Y">Y</option>
						<option value="N" selected>N</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_SearchProjectList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<span class="searchBtn">
						<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_OnClosePopup('projectListPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
					</span>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:100%;min-height:600px;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:450px">
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;padding:10px;">
						<table id="projectList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							<tbody><tr></tr></tbody>
						</table>
					</div>
				</div>
				<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
</div>