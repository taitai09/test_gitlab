<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="excelUploadPop" class="easyui-window popWin" style="background-color:#ffffff;width:420px;height:160px;">
	<div class="easyui-panel" data-options="fit:true" style="padding:20px 30px;">
		<form:form method="post" id="excelUpload_form" name="excelUpload_form" enctype="multipart/form-data" class="form-inline">
			<input id="uploadFile" name="uploadFile" class="easyui-filebox" style="width:100%" data-options="prompt:'성능점검등록할 엑셀 파일을 선택해 주세요.'">
			<div class="searchBtn" style="margin-top:20px;margin-right:-5px;">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="downloadExcelTemp();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 템플릿 다운로드</a>
				<a href="javascript:;" class="w100 easyui-linkbutton" onClick="uploadExcelAction();"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> 엑셀 업로드</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('excelUploadPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</form:form>
	</div>
</div>
<div id="loadedExcelPop" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:700px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="loadedExcel_form" name="loadedExcel_form" enctype="multipart/form-data" class="form-inline">
			<div data-options="region:'north',border:false" style="height:110px;padding:5px;">
				<table class="detailT" style="margin:0px;">
					<colgroup>	
						<col style="width:40%;">
						<col style="width:60%;">
					</colgroup>
					<tr>
						<th>업무명</th>
						<td><select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'auto'" class="w130 easyui-combobox"></select></td>
					</tr>
					<tr>
						<th>성능점검유형</th>
						<td><select id="deploy_perf_check_type_cd" name="deploy_perf_check_type_cd" data-options="panelHeight:'auto'" class="w130 easyui-combobox"></select></td>
					</tr>
					<tr>
						<th>배포일자</th>
						<td><input type="text" id="deploy_day" name="deploy_day" value="${nowDate}" class="w130 datapicker easyui-datebox"/></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:true">
				<table id="popTableList" class="detailT" style="margin-top:0px;margin-left:0px;">
					<colgroup>	
						<col style="width:8%;">
						<col style="width:92%;">
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" id="chkAll" name="chkAll" checked class="chkBox"/></th>
							<th>애플리케이션 / SQL식별자(DBIO)</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div data-options="region:'south',border:false" style="height:70px;padding:5px 20px;">
				<p style="color:#ff0000;margin-left:-15px;">※ 현재 Excel에서 업로드된 결과는 DB에 저장이 안된 상태입니다.</p>
				<div class="searchBtn" style="margin-top:10px;margin-right:-5px;">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="saveExcelAction();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="closeLoadedExcelPopup();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>
