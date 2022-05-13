<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- <div id="dblPopup" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:650px;"> -->
<div id="dblPopup" class="easyui-window popWin" style="background-color:#02f213;width:1000px;height:650px;">
<!-- 	<div class="easyui-layout" style="height:580px;margin:10px;"> -->
<%-- 		<form:form method="post" id="dblPopup_form" name="dblPopup_form" class="form-inline"> --%>
<!-- 			<input type="hidden" id="dbid" name="dbid"/> -->
<!-- 			<input type="hidden" id="parameter_list" name="parameter_list"/> -->
<!-- 			<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/> -->
			
<!-- 			<div data-options="region:'north',border:false" style="width:100%;height:80px;"> -->




<!-- 				<div class="well"> -->
<!-- 				<label>프로젝트명</label> -->
<!-- 				<input type="text" id="project_nm" name="project_nm" value="" class="w200 easyui-textbox"/> -->
<!-- 				<label>종료여부</label> -->
<!-- 				<select id="del_yn" name="del_yn" data-options="panelHeight:'auto',editable:false" class="w50 easyui-combobox"> -->
<!-- 					<option value="">ALL</option> -->
<!-- 					<option value="Y">Y</option> -->
<!-- 					<option value="N" selected>N</option> -->
<!-- 				</select> -->
<!-- 				<span class="searchBtnLeft"> -->
<!-- 					<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_SearchProjectList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a> -->
<!-- 				</span> -->
<!-- 				<span class="searchBtn"> -->
<!-- 					<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_OnClosePopup('projectListPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a> -->
<!-- 				</span> -->
<!-- 				</div> -->




<!-- 				<div id="dblPopupChart" style="width:100%;height:900px;padding:0px;"> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div data-options="region:'center',border:false" style="width:100%;min-height:600px;"> -->
<!-- 			<div id="dblPopupChart" style="width:100%;height:900px;padding:0px;"> -->
<!-- 			</div> -->
<!-- 			</div> -->
<%-- 		</form:form> --%>
<!-- 	</div> -->

	<div class="easyui-layout" style="height:580px;margin:10px;">
		<form:form method="post" id="dblPopup_form" name="dblPopup_form" class="form-inline">
			<input type="hidden" id="parameter_list" name="parameter_list"/>
			<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/>
			
			<div region="east" style="width:100%;height:100%;">
				<div id="dblPopupChart" style="width:100%;height:100%;padding:0px;">
				</div>
			</div>
		</form:form>
	</div>
</div>