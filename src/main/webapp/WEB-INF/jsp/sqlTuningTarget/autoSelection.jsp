<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>자동선정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlTuningTarget/autoSelection.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="db_name" name="db_name" value="${topsqlAutoChoice.db_name}"/>
					<input type="hidden" id="dbid" name="dbid" value="${topsqlAutoChoice.dbid}"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w120 easyui-combobox"></select>
<!-- 					<label>선정일자</label> -->
<%-- 					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~ --%>
<%-- 					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>					 --%>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:350px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="db_name" name="db_name" value="${topsqlAutoChoice.db_name}"/>
				<input type="hidden" id="dbid" name="dbid" value="${topsqlAutoChoice.dbid}"/>
				<input type="hidden" id="auto_choice_cond_no" name="auto_choice_cond_no" value="${topsqlAutoChoice.auto_choice_cond_no}"/>
				<input type="hidden" id="select_auto_choice_cond_no" name="select_auto_choice_cond_no" value=""/> <!-- autoSelectionStatus.js 로 넘기기 위한 파라미터 -->
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
				<input type="hidden" id="perfr_auto_assign_yn" name="perfr_auto_assign_yn" value="N"/>
				<input type="hidden" id="before_choice_sql_except_yn" name="before_choice_sql_except_yn" value="Y"/>
				<input type="hidden" id="before_tuning_sql_except_yn" name="before_tuning_sql_except_yn" value="Y"/>
				<input type="hidden" id="appl_filter_yn" name="appl_filter_yn" value="N"/>				
				<input type="hidden" id="use_yn" name="use_yn" value="Y"/>
				<input type="hidden" id="del_yn" name="del_yn" value="N"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:19%;">
					</colgroup>
					<tr>
						<th>DB</th>
						<td colspan="5"><select id="selectDbidCombo" name="selectDbidCombo" data-options="panelHeight:'auto',editable:false" required="true" class="w150 easyui-combobox"></select></td>
					</tr>
					<tr>
						<th>튜닝담당자</th>
						<td colspan="3">
							<label>튜닝담당자 자동할당</label>
							<input type="checkbox" id="chkPerfrId" name="chkPerfrId" value="" class="w120 easyui-switchbutton"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<select id="perfr_id" name="perfr_id" data-options="panelHeight:'auto',editable:false" required="true" class="w120 easyui-combobox"></select>
						</td>
						<th>프로그램유형</th>
						<td>
							<select id="program_type_cd" name="program_type_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w150 easyui-combobox">
								<option value="">선택</option>
								<option value="1">온라인</option>
								<option value="2">배치</option>
							</select>
						</td>						
					</tr>
					<tr>
						<th>수집주기</th>
						<td><select id="gather_cycle_div_cd" name="gather_cycle_div_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w180 easyui-combobox"></select></td>
						<th>수집범위</th>
						<td><select id="gather_range_div_cd" name="gather_range_div_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w180 easyui-combobox"></select></td>
						<th>이전 선정 SQL 제외</th>
						<td><input type="checkbox" id="chkChoiceExcept" name="chkChoiceExcept" value="" class="w120 easyui-switchbutton"/></td>						
					</tr>
					<tr>
						<th>선정시작일</th>
						<td><input type="text" id="choice_start_day" name="choice_start_day" data-options="panelHeight:'auto',editable:false" required="true" class="w180 datapicker easyui-datebox"/></td>
						<th>선정종료일</th>
						<td><input type="text" id="choice_end_day" name="choice_end_day" data-options="panelHeight:'auto',editable:false" required="true" class="w180 datapicker easyui-datebox"/></td>
						<th>이전 튜닝 SQL 제외</th>
						<td><input type="checkbox" id="chkTuningExcept" name="chkTuningExcept" value="" class="w120 easyui-switchbutton"/></td>
					</tr>
					<tr>
						<th>Elapsed Time (sec)</th>
						<td><input type="text" id="elap_time" name="elap_time" data-options="panelHeight:'auto'" required="true" class="w180 chkNum easyui-textbox"/></td>
						<th>Buffer Gets</th>
						<td><input type="text" id="buffer_cnt" name="buffer_cnt" data-options="panelHeight:'auto'" required="true" class="w180 chkNum easyui-textbox"/></td>
						<th>Executions</th>
						<td><input type="text" id="exec_cnt" name="exec_cnt" data-options="panelHeight:'auto'" required="true" class="w180 chkNum easyui-textbox"/></td>
					</tr>
					<tr>
						<th>Module명 1</th>
						<td><input type="text" id="module1" name="module1" class="w180 easyui-textbox"/></td>
						<th>Module명 2</th>
						<td><input type="text" id="module2" name="module2" class="w180 easyui-textbox"/></td>
						<th>Parsing Schema Name</th>
						<td><select id="selectParsingSchemaName" name="selectParsingSchemaName"  data-options="panelHeight:'300px',editable:true" class="w180 easyui-combobox"></select></td>
					</tr>
					<tr>
						<th>SQL TEXT</th>
						<td><input type="text" id="sql_text" name="sql_text" class="w180 easyui-textbox"/></td>
						<th>TOP N</th>
						<td><input type="text" id="topn_cnt" name="topn_cnt" data-options="panelHeight:'auto'" required="true" class="w180 chkNum easyui-textbox"/></td>
						<th>Ordered</th>
						<td><select id="order_div_cd" name="order_div_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w180 easyui-combobox"></select></td>
					</tr>
					<tr>
						<th>애플리케이션 필터 여부</th>
						<td><input type="checkbox" id="chkApplFilterYn" name="chkApplFilterYn" value="" class="w120 easyui-switchbutton"/></td>					
						<th>사용 여부</th>
						<td><input type="checkbox" id="chkUseYn" name="chkUseYn" value="" class="w120 easyui-switchbutton"/></td>
						<th>삭제 여부</th>
						<td colspan="3"><input type="checkbox" id="chkDelYn" name="chkDelYn" value="" class="w120 easyui-switchbutton"/></td>
					</tr>
				</table>
				<div class="innerBtn2">
					<div class="searchBtnLeft2">
						<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_AutoChoiceStatus();"><i class="btnIcon fas fa-file-alt fa-lg fa-fw"></i> 선정 현황</a>
						<a href="javascript:;" id="historyBtn" class="w120 easyui-linkbutton" data-options="disabled:true" onClick="Btn_AutoChoiceHistory();"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 선정조건 변경이력</a>
					</div>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveAutoChoice();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>