<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="applicationCodeExcelUploadPop" class="easyui-window popWin" style="background-color:#ffffff;width:420px;height:160px;">
	<div class="easyui-panel" data-options="fit:true" style="padding:20px 30px;">
		<form:form method="post" id="applicationCodeExcelUpload_form" name="applicationCodeExcelUpload_form" enctype="multipart/form-data" class="form-inline">
			<input id="uploadFile" name="uploadFile" class="easyui-filebox" style="width:100%" data-options="prompt:'애플리케이션코드를 등록할 엑셀 파일을 선택해 주세요.'">
			<div class="searchBtn" style="margin-top:20px;margin-right:-5px;">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ApplicationCodeFormDownload();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 템플릿 다운로드</a>
				<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_uploadApplicationExcelAction();"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> 엑셀 업로드</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('applicationCodeExcelUploadPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</form:form>
	</div>
</div>
<form:form method="post" id="excel_form" name="excel_form">
</form:form>					
<iframe id="hidden" name="hiddenifr" src=""></iframe>
