<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="memoBox" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:350px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="memo_form" name="memo_form" class="form-inline">
			<input type="hidden" name="sql_id">
			<input type="hidden" name="dbid">
			<input type="hidden" id="review_sbst" name="review_sbst">
			<div data-options="plain:true,region:'center',split:false,border:false" style="padding:5px;">
				<textarea id="memoEdit" style="height: 98.5%; width:99%;" placeholder="플랜검증 1단계(2021.06.02), 홍튜너 검토
-----------------------------------------------------------
SELECT-LIST 에 바인드값이 수집되지 않아 TO_DATE 함수에서 FORMAT 팅시에 오류"></textarea>
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:30px;">
				<div class="searchBtn" style="padding-right:16px;">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_memoSave();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_memoDelete();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('memoBox');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>