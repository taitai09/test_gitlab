<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<style type="text/css">
	.panel-title {
		position : absolute;
		bottom : 11px;
	}


</style>

<div id="indexRecommendPopup2" class="easyui-window popWin" style="background-color:#ffffff;width:494px;height:470px;">

	<div id='autoCrerateErrList_div' style="width:100%; height:100%">
		<table id="autoCrerateErrList" style="width:100%; height:100%" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			<tbody><tr></tr></tbody>
		</table>
	</div>
</div>

<div id="indexRecommendPopup3" class="easyui-window popWin" style="background-color:#ffffff;width:494px;height:470px;">

	<div id='visibleErrList_div' style="width:100%; height:100%">
		<table id="visibleErrList" style="width:100%; height:100%" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			<tbody><tr></tr></tbody>
		</table>
	</div>
</div>


<div id="indexRecommendPopup" class="easyui-window popWin" style="background-color:#ffffff;width:494px;height:470px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="indexRecommendPopup_form" name="assign_form" class="form-inline" style="width:100%; height:100%">

			<div id='addOrDelHistoryList_div' style="width:100%; height:100%">
				<table id="addOrDelHistoryList" style="width:100%; height:100%" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					<tbody><tr></tr></tbody>
				</table>
			</div>

			<div class="popupHeader" style="overflow:auto;background: #f5f5f5;padding: 10px 40px 10px 40px;border : 1px solid #c9c9c9;">
				<div id="pop_IndexInvisible" style="margin-bottom: 10px;">
					<span>추천 유형이 MODIFY 인덱스의 경우 기존 인덱스 VISIBILITY를 INVISIBLE로 변경 후 성능 분석을 실행해야 합니다.
					아래의 인덱스 VISIBILITY를 INVISIBLE로 변경 바랍니다.</span>
				</div>
				<div id="pop_TableSpace" style="margin-bottom: 10px;">
					<label class="w220">테이블스페이스</label>
					<input type="text" id="tablespace_name" requiredMsg='테이블 스페이스를 입력해주세요.' style="width:300px" name="tablespace_name" class="easyui-textbox required" required="true" ></input>
				</div>

				<div id="ignoreError" style="margin-bottom: 10px;">
					<label class="w220">오류 무시</label>
					<input type="checkbox" id="ignoreErrorYn" name="ignoreerroryn" class="w80 easyui-switchbutton" checked/>
				</div>

				<div id="pop_LocalIndex" style="margin-bottom: 10px;">
					<label class="w220">파티션 테이블 Local 인덱스 생성</label>
					<input type="checkbox" id="partitionTalbelocalIndex" name="partitionTalbelocalIndex" class="w80 easyui-switchbutton" style="text-transform: uppercase" checked/>
				</div>

				<div id="pop_MaxParallel_div" style="margin-bottom: 10px;">
					<label class="w220">Max Parallel Degree</label>
					<input type="checkbox" id="maxParallelDegree" name="maxParallelDegree" class="w80 easyui-switchbutton"  checked/>
					<input type="text" id="maxParallelDegree_txt" type="number" requiredMsg='Max Parallel Degree를 입력해주세요.' min="1" max="8" style="width:50px" name="maxParallelDegree_txt" class="easyui-numberbox required" required="true" ></input>
				</div>

				<div class="searchBtn">
					<a id="btn_CreateScript" style="margin-bottom: 0px;" href="javascript:;" class="w100 easyui-linkbutton" onClick="getScript();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 스크립트 생성</a>
					<a id="btn_CopyScript" style="margin-bottom: 0px;" href="javascript:;" class="w100 easyui-linkbutton" onClick="copyScript();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i>스크립트 복사</a>
					<a id="btn_IndexAutoCreate" style="margin-bottom: 0px;" href="javascript:;" class="w100 easyui-linkbutton" onClick="getScript();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 인덱스 자동생성</a>
					<a id="btn_AutoCreate" style="margin-bottom: 0px;" href="javascript:;" class="w100 easyui-linkbutton" onClick="autoCreate();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 자동 생성</a>
					<a id="btn_Close" style="margin-bottom: 0px;" href="javascript:;" class="w100 easyui-linkbutton" onClick="closePopup();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>

			<div id="popup_area" style="height:385px">
<%--			<textarea id="scriptArea" name="scriptArea" class="easyui-textbox"  data-options="multiline:true" data-options="editable:false" style="width:735px;height:480px;border-radius: 0px;" ></textarea>--%>
				<textarea id="scriptArea" name="scriptArea" class="easyui-textbox"  data-options="multiline:true" editable="false" style="width:100%;height:100%;border-radius: 0px;" ></textarea>
			</div>
		</form:form>
	</div>
</div>
