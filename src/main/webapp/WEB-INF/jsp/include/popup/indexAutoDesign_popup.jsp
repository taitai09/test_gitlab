<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="indexAutoDesignPop" class="easyui-window popWin" style="border:0px solid; background-color:#ffffff;width:900px;height:360px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="indexAutoDesign_form" name="indexAutoDesign_form" class="form-inline">
			<div data-options="region:'north',split:false,border:false" style="height:133px;">
				<div class="well2" style="background-color:#ffffff;">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="owner" name="owner"/>
					<input type="hidden" id="exec_seq" name="exec_seq"/>
					<input type="hidden" id="table_name" name="table_name"/>
					<input type="hidden" id="access_path_type" name="access_path_type"/>
					
					<div data-options="region:'center',border:false" style="heigth:20px;padding:5px;">
						
						<label style="margin-left:10px;">Selectivity 계산 : </label>
						<span style="padding-right:2px;padding-left:5px;"><input class="easyui-radiobutton" id="selectivity_method1" name="selectivity_calc_method" value="STAT" checked> Statistics 기반 </span> 
						<span>
							<i id="selectivity_statistics_tooltip" class="fas fa_question_circle easyui-tooltip" title="통계정보 기반  <br/>selectivity 계산" position="top"></i>
						</span>
					    <span style="padding-right:2px;padding-left:10px;"><input class="easyui-radiobutton" id="selectivity_method2" name="selectivity_calc_method" value="DATA"> Data Sampling 기반</span>
						<span>
							<i id="selectivity_data_tooltip" class="fas fa_question_circle easyui-tooltip" title="데이터 샘플링 기반</br>selectivity 계산" position="top"></i>
						</span>	
						
						
						<div class="searchBtn">
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_StartIndexAutoDesign();"><i class="btnIcon fas fa-cogs fa-lg fa-fw"></i> 인덱스 자동설계</a>
							<a id="applyBtn" href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_AppendIndexDesignList();"><i class="btnIcon fab fa-steam fa-lg fa-fw"></i> 인덱스 설계 반영</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('indexAutoDesignPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
						</div>
					</div>
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
			<div id="sqlTextDiv" data-options="title:'자동설계 결과',region:'center',border:false" style="height:840px;padding:5px;">
				<table id="indexResultList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</form:form>
	</div>
</div>
