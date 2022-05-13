<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="selfIndexAutoDesignPop" class="easyui-window popWin" style="border:0px solid; background-color:#ffffff;width:900px;height:360px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="selfIndexAutoDesign_form" name="selfIndexAutoDesign_form" class="form-inline">
			<div data-options="region:'north',split:false,border:false" style="height:133px;">
				<div class="well2" style="background-color:#ffffff;">
					
					<div data-options="region:'north',border:false" style="height:20px;padding:5px;margin-bottom:5px;">
					<label style="margin-left:10px;">Selectivity 계산 : </label>
					
					<span style="padding-right:2px;padding-left:5px;"><input class="easyui-radiobutton" id="selectivity_method1" name="selectivity_calc_method" value="STAT" checked> Statistics 기반 </span> 
					<span>
						<i id="selectivity_statistics_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
					</span>
				    <span style="padding-right:2px;padding-left:10px;"><input class="easyui-radiobutton" id="selectivity_method2" name="selectivity_calc_method" value="DATA"> Data Sampling 기반</span>
					<span>
						<i id="selectivity_data_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
					</span>	 
						
						<div class="searchBtn">
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_StartSelfIndexAutoDesign();"><i class="btnIcon fas fa-cogs fa-lg fa-fw"></i> 인덱스 자동설계</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SelfIndexExcelDown();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('selfIndexAutoDesignPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
						</div>
						
					</div>
					<!-- <div data-options="region:'center',border:false" style="heigth:20px;padding:5px;">
						<div class="searchBtn">
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_StartSelfIndexAutoDesign();"><i class="btnIcon fas fa-cogs fa-lg fa-fw"></i> 인덱스 자동설계</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SelfIndexExcelDown();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('selfIndexAutoDesignPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
						</div>
					</div> -->
				</div>
				<div class="well2" style="background-color:#ffffff; margin-top:5px;">
					<div data-options="region:'top',border:false" style="heigth:20px;padding:5px;">
						<label>* 테이블 테이터 건수가 적은 경우 인덱스를 추천하지 않음</label>
					</div>
					<div data-options="region:'center',border:false" style="heigth:20px;padding:5px;">
						<label>* 검색 조건의 변별력이 좋지 않은 경우 인덱스를 추천하지 않음</label>
					</div>
				</div>
			</div>
			<div id="sqlTextDiv" data-options="title:'자동설계 결과',region:'center',border:false" style="height:110px;padding:5px;">
				<table id="selfIndexResultList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					<tbody><tr><td></td></tr></tbody>
				</table>
			</div>
		</form:form>
	</div>
</div>
