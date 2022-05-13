<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="sqlDiagnosisReportPopup" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:600px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="sqlDiagnosisReportPopup_Form" name="assign_form" class="form-inline">
			<input type="hidden" id="popSqlId" name="popSqlId"/>
						
			<div class="popup_area">
				<input id="sqlText" name="sqlText" class="easyui-textbox"  data-options="multiline:true" style="width:986px; height:555px" readonly>
			</div>
		</form:form>
	</div>
</div>
