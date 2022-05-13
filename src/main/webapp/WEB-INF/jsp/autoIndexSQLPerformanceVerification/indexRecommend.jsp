<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>인덱스 검증</title>
	<meta charset="utf-8"/>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/indexRecommend.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/indexRecommendPopup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/authorityScript_popup.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlInfoBySqlId_popup.js?ver=<%=today%>"></script>

	<style type="text/css">
		.narrowMod , .narrowMod:hover{
			background-image: url("../resources/images/compress_arrows.png");
			background-size: 19px;
			background-origin: content-box;
			background-repeat: no-repeat;
			background-position: center;
		}
		.wideMod , .wideMod:hover{
			background-image: url("../resources/images/expand_arrows.png");
			background-size: 16px;
			background-origin: content-box;
			background-repeat: no-repeat;
			background-position: center;
		}
		.datagrid-cell .addOrDelHistoryList_datagrid-cell-c18-err_sbst{
			white-space: nowrap;
			word-wrap: normal;
		}
		.bottomGrid .datagrid-row-selected,
		#addOrDelHistoryList_div .datagrid-row-selected,
		#autoCrerateErrList_div .datagrid-row-selected{
			color: black;
			background : rgba(0,0,0,0);
		}
		.bottomGrid .datagrid-row.datagrid-row-over,
		#addOrDelHistoryList_div .datagrid-row.datagrid-row-over,
		#autoCrerateErrList_div .datagrid-row.datagrid-row-over{
			cursor:auto;
			background : rgba(0,0,0,0);
		}
		.x-body{}
		.combobox-item{
			line-height: 11px;
			font-weight: 300;
			-webkit-font-smoothing: antialiased;
		}
		#addDelTab .panel-body.panel-body-noheader.panel-body-noborder{
			width : 100% !important;
		}
		.datagrid-cell.perfAnalysisList_datagrid-cell-c9-ERR_MSG{
			text-overflow:ellipsis;
		}
		#submit_form .easyui-fluid .bottomGrid:nth-child(4) .datagrid-view2 .datagrid-body{
			overflow: hidden;
		}
		.topDiv{
			margin-top:7px;
		}
		.well{
			padding : 5px 12px 5px 12px;
		}
		.well .cnts{
			border-radius: 4px;
			background-color: #EEEEEE;
			margin:3px;
			padding:3px;
		}
		.well .cnts .cntLabel{
			width:70px;
			line-height:24px;
		}
		.well .cnts .cntLabel label{
			text-align:left;
			font-weight: bold;
		}
		.well .cnts .cnt{
			min-height:15px;
			width:50px;
			background-color:white;
			border-radius:2px;
			padding:3px;
			text-align: right;
		}
		#addDelTab .tabs-first{
			margin-bottom: 5px;
		}
		#addDelTab .tabs-selected a {
			background:white;
		}
		#addDelTab .tabs .tabs-inner{
			height:70px !important;
			line-height: 14px !important;
			padding-left : 12px;
		}
		#addDelTab .tabs-panels-right{
			width:calc(100% - 35px) !important;
		}

	</style>
</head>
<body style="visibility:hidden;">
<%@include file="/WEB-INF/jsp/include/popup/sqlInfoBySqlId_popup.jsp" %>

	<!-- container START -->
	<div id="container">
		<!-- contents START -->
		<div id="contents">
			<form:form method="post" id="searchForm" name="searchForm" class="form-inline">

				<input type="hidden" id="search_projectId" name="project_id"/>
				<input type="hidden" id="search_sqlPerformanceP" name="sql_auto_perf_check_id"/>
				<input type="hidden" id="search_owner" name="owner"/>
				<input type="hidden" id="search_tableName" name="tableName"/>
				<input type="hidden" id="search_recommendType" name="recommendType"/>
				<input type="hidden" id="search_spsRecommendIndex" name="spsRecommendIndex"/>
				<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="ORACLE"/>
				<input type="hidden" id="search_currentPage" name="currentPage"/>
				<input type="hidden" id="search_pagePerCount" name="pagePerCount"/>
				<input type="hidden" id="search_indexAdd" name="indexAdd"/>
				<input type="hidden" id="search_sqlPerformanceP_Name" name="sqlPerformanceP_Name"/>
				<input type="hidden" id="idx_ad_no" name="idx_ad_no"/>
				<input type="hidden" id="perf_check_target_dbid" name="perf_check_target_dbid"/>
				<input type="hidden" id="original_dbid" name="original_dbid"/>
				<input type="hidden" id="tobe_db_name" name="tobe_db_name"/>
				<input type="hidden" id="exec_seq" name="exec_seq"/>
				<!-- 이전, 다음 처리 -->
			</form:form>
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="currentPage" name="currentPage"/>
				<input type="hidden" id="pagePerCount" name="pagePerCount"/>
				<input type="hidden" id="autoProcessRefreshYn" name="autoProcessRefreshYn" value="N"/>
				<input type="hidden" id="performanceAnalysisRefreshYn" name="performanceAnalysisRefreshYn" value="N"/>


				<div class="easyui-panel" data-options="border:false" style="width:100%">
					<div class="well">
						<table>
							<colgroup>
								<col style="width:3%">
								<col style="width:3%">
								<col style="width:4%">
								<col style="width:6%">
								<col style="width:5%">
								<col style="width:5%">
								<col style="width:5%">
								<col style="width:5%">
								<col style="width:9%">
								<col style="width:5%">
								<col style="width:5%">
								<col style="width:15%">
							</colgroup>
							
							<tr height="30px;">
								<td><label>프로젝트</label></td>
								<td colspan="3">
									<select id="project_id" name="project_id" style='height:25px' class="w340 easyui-combobox required" requiredMsg="프로젝트를 선택해 주세요." required="true" data-options="editable:false"></select>
								</td>
								<td align=""><label>SQL점검팩</label></td>
								<td colspan="7">
									<select id="sqlPerformanceP" name="sql_auto_perf_check_id" style='height:25px' class="w360 easyui-combobox required" requiredMsg="SQL 점검팩을 선택해 주세요." required data-options="panelHeight:'auto',editable:false,readonly:true"></select>
								</td>
							</tr>
							<tr height="30px;">
								<td><label>OWNER</label></td>
								<td><input type="text" id="owner" name="owner" style='height:25px' class="w100 easyui-textbox"  data-options=""></td>
								<td><label>테이블명</label></td>
								<td><input type="text" id="tableName" name="tableName" style='height:25px' class="w250 easyui-textbox"  data-options=""></td>
								<td><label>추천유형</label></td>
								<td>
									<select id="recommendType" name="recommendType" style='height:25px' data-options="editable:false" class="w130 easyui-combobox" required="true" ></select>
								</td>
								<td style="text-align: center;"><label>인덱스생성</label></td>
								<td>
									<select id="indexAdd" name="indexAdd" style='height:25px' data-options="editable:false" class="w130 easyui-combobox" required="true" ></select>
								</td>
								<td style="text-align: center;"><label>성능 분석 결과 최종 추천</label></td>
								<td>
									<select id="spsRecommendIndex" name="spsRecommendIndex" style='height:25px' data-options="editable:false" class="w140 easyui-combobox" required="true"></select>
								</td>
								<td class="searchBtn" style="margin-top:3px;">
									<a href="javascript:;" class="w120 easyui-linkbutton" onClick="getIndexRecommend();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
								</td>
							</tr>
						</table>
					</div>
					
					<div class="well topDiv mainGrid" style="padding:10px;">
						<div style="display: flex; height: 240px;">
							<div style=" width: 11%;">
								<div style="margin:0px;text-align: center;margin-bottom:5px;margin-top:-5px;"><label style="margin:0;font-size:13pt;font-weight: bold;">인덱스 검증</label></div>
								<div class="well cnts" style="height:24px; border:#b8b8b8 2px solid;margin:7.5px 0px; display: flex;">
									<div class="cntLabel" style="width:108px">
										<label>추천 테이블 수</label>
									</div>
									<div class="cnt" >
										<span id="running_table_cnt"></span>
									</div>
								</div>
								<div class="well" style="border:#b8b8b8 2px solid; border-radius: 4px;padding:2px;">
									<div style="display: flex;">
										<div style="width:31px;">
											<label style="font-size:10pt;font-weight:bold;display:table-caption;margin-top:27px;">추천</label>
											<br>
											<label style="font-size:10pt;font-weight:bold;display:table-caption;">인덱스수</label>
										</div>
										<div style="width:100%;">
											<div class="well cnts" style="margin:3px; margin-bottom:10px; height:24px; margin-top:4px; display: flex; border:#b8b8b8 1px solid;">
												<div class="cntLabel">
													<label>전체</label>
												</div>
												<div class="cnt">
													<span id="recommend_index_cnt">

													</span>
												</div>
											</div>
											<div class="well cnts" style="margin:3px; margin-bottom:10px; height:24px; display: flex; border:#b8b8b8 1px solid;">
												<div class="cntLabel">
													<label>ADD</label>
												</div>
												<div class="cnt">
													<span id="recommend_index_add_cnt">

													</span>
												</div>
											</div>
											<div class="well cnts" style="margin:3px; margin-bottom:10px; height:24px; display: flex; border:#b8b8b8 1px solid;">
												<div class="cntLabel">
													<label>MODIFY</label>
												</div>
												<div class="cnt">
													<span id="recommend_index_modify_cnt">

													</span>
												</div>
											</div>
											<div class="well cnts" style="margin:3px; margin-bottom:4px; height:24px; display: flex; border:#b8b8b8 1px solid;">
												<div class="cntLabel">
													<label>UNUSED</label>
												</div>
												<div class="cnt">
													<span id="recommend_index_unused_cnt">

													</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div style="width:0.5%;"></div>
							<div style="width:88.5%;">
								<div style="height:100%;">
									<table id="indexList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
										<tbody><tr></tr></tbody>
									</table>
								</div>
							</div>
						</div>
						<div style="height:25px">
							<div class="searchBtn" style="margin-top:5px;">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="excelDownload();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
								<a href="javascript:;" class="w20 easyui-linkbutton wideMod" onclick="gridWide();"></a>
								<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
								<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
					</div>
					<div class="well topDiv bottomGrid">
						<div style="display: flex;">
							<div style="width:70%;position: relative;">
								<label style="font-size: 12pt;font-weight:bold;position:absolute;bottom:2px;">인덱스 생성</label>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showIndexRecommendPopup(CREATE_INDEX_SCRIPT);" style=" margin-left: 120px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 인덱스생성 스크립트</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showIndexRecommendPopup(AUTO_CREATE_INDEX);" style=" margin-left: 10px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 인덱스자동생성</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showIndexRecommendPopup(DROP_INDEX_SCRIPT);" style=" margin-left: 10px;"><i class="btnIcon fas fa-minus-square fa-lg fa-fw"></i> 인덱스제거 스크립트</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showIndexRecommendPopup(AUTO_DROP_INDEX);" style=" margin-left: 10px;"><i class="btnIcon fas fa-minus-square fa-lg fa-fw"></i> 인덱스자동제거</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="forceCompleteAuto();" style=" margin-left: 10px;"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
							</div>
							<div style="text-align: right;width:30%;">
								<label>Refresh</label>
								<input type="checkbox" id="autoProcessRefresh" name="autoProcessRefresh" class="w80 easyui-switchbutton"/>
								<input type="number" id=autoProcessRefresh_val name="autoProcessRefresh_val" value="60" class="w40 easyui-numberbox" style="height:25px" data-options="min:3"/> 초
								<a href="javascript:;" class="w20 easyui-linkbutton" onClick="getAutoIndexCreateHistory();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw" style="padding:7px 2px;"></i></a>
							</div>
						</div>
						<div style="display:flex;height:90px;">
							<div style="width:30%; margin-top:10px;">
								<table id="recommendIdxAddList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									<tbody><tr></tr></tbody>
								</table>
							</div>
							<div style="width:1%;"></div>
							<div style="width:69%;height:90px;">
								<div id="addDelTab" class="easyui-tabs" data-options="border:false,pill: true" style="margin-top:10px;">
								</div>
							</div>
						</div>
					</div>
					
					<div class="well topDiv bottomGrid" style="height:148px;">
						<div style="display:flex;">
							<div style="">
								<label style="font-size: 12pt;font-weight:bold;">성능 분석</label>
							</div>
							<div style="text-align: right;width:90%">
								<label class="marginR5">병렬실행</label>
								<input type="checkbox" id="parallel_degree_yn" name="parallel_degree_yn" class="w80 easyui-switchbutton"/>
								<input type="number" id="parallel_degree" name="parallel_degree" value="4" class="w40 easyui-numberbox" min="1" max="8"/>
								
								<label class="marginR5">DML 실행</label>
								<input type="checkbox" id="dml_exec_yn" name="dml_exec_yn" class="w80 easyui-switchbutton" />
								
								<label class="marginR5">Multiple 실행</label>
								<input type="checkbox" id="multi_execution" name="multi_execution" class="w80 easyui-switchbutton" />
								<input type="number" id="multiple_exec_cnt" name="multiple_exec_cnt" value="4" class="w40 easyui-numberbox" min="1" max="10"/>
								
								<label class="marginR5">Multiple Bind 실행</label>
								<input type="checkbox" id="multi_bind_execution" name="multi_bind_execution" class="w80 easyui-switchbutton" />
								<input type="number" id="multiple_bind_exec_cnt" name="multiple_bind_exec_cnt" value="4" class="w40 easyui-numberbox" min="1" max="10"/>
								
								<span class="sql_time_limt_cd">
									<label class="marginR5">SQL Time Limit</label>
									<select id="sql_time_limt_cd" name="sql_time_limt_cd" data-options="editable:false" class="w100 easyui-combobox"></select> 분
									<input type="hidden" id="sql_time_direct_pref_value" name="sql_time_direct_pref_value">
								</span>
								
								<label class="marginR5">최대 Fetch 건수</label>
								<input type="number" id="max_fetch_cnt" name="max_fetch_cnt" class="w80 easyui-numberbox"  required data-options=" min:1, max:1000000" value="100000">
								
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="excutePerformanceAnalysis();" style="margin:0px 10px 0px 50px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ForceUpdateSqlAutoPerformanceCheck();" style="margin-right:10px;"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
								<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_AuthorityScript();"><i class="btnIcon fas fa-ruler fa-lg fa-fw"></i> 권한</a>
							</div>
						</div>
						<div style="margin:10px 0px;text-align: right;">
							<label>Refresh</label>
							<input type="checkbox" id="performanceAnalysisRefresh" name="performanceAnalysisRefresh" class="w80 easyui-switchbutton"/>
							<input type="number" id=performanceAnalysisTimer_value name="performanceAnalysisTimer_value" value="60"  class="w40 easyui-numberbox" style="height:25px" data-options="min:3"/> 초
							<a href="javascript:;" class="w20 easyui-linkbutton" onClick="getPerformanceAnalysis();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw" style="padding:7px 2px;"></i></a>
						</div>
						<div style="height:76px;">
							<table id="perfAnalysisList" style="overflow: hidden;" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								<tbody><tr></tr></tbody>
							</table>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<%@include file="/WEB-INF/jsp/include/popup/indexRecommendPopup.jsp" %> <!-- FILTER SQL 팝업 -->
	<%@include file="/WEB-INF/jsp/include/popup/authorityScript_popup.jsp" %>

</body>
</html>