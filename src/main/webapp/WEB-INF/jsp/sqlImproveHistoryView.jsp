<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2018.04.17	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>튜닝이력조회 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/sqlImproveHistoryView.js?ver=<%=today%>"></script>
</head>
<body>
<!-- <div id="sqlImproveDiv" class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;"> -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="tuning_no" name="tuning_no" value="${sqlDetail.tuning_no}"/>
		<input type="hidden" id="update_dt" name="update_dt" value="${update_dt}"/>
		<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${update_dt}"/>				
		<!-- 튜닝중 텍스트박스 편집 가능/불가능 -->
		<c:set var="dataOptionsReadonlyFlag" scope="page" value="readonly:true"/>
		<c:set var="textAreaReadonlyFlag" scope="page" value="readonly"/>
		<table class="detailT">
			<colgroup>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:11%;"/>
				<col style="width:12%;"/>
			</colgroup>
			<tr>
				<th colspan="3">개선전</th>
				<th colspan="3">개선후</th>
				<th colspan="3">개선율 (%)</th>
			</tr>
			<tr>
				<th>응답시간 (Sec)</th>
				<th>블럭수</th>
				<th>PGA 사용량(MB)</th>
				<th>응답시간 (Sec)</th>
				<th>블럭수</th>
				<th>PGA 사용량(MB)</th>
				<th>응답시간</th>
				<th>블럭수</th>
				<th>PGA 사용량</th>
			</tr>
			<tr>
				<!--
				<td class="ctext">${sqlDetail.imprb_elap_time}</td>
				<td class="ctext">${sqlDetail.imprb_buffer_cnt}</td>
				<td class="ctext">${sqlDetail.imprb_pga_usage}</td>
				<td class="ctext">${sqlDetail.impra_elap_time}</td>
				<td class="ctext">${sqlDetail.impra_buffer_cnt}</td>
				<td class="ctext">${sqlDetail.impra_pga_usage}</td>
				<td class="ctext">${sqlDetail.elap_time_impr_ratio} %</td>
				<td class="ctext">${sqlDetail.buffer_impr_ratio} %</td>
				<td class="ctext">${sqlDetail.pga_impr_ratio} %</td>
				-->
				<td class="ctext"><input type="number" id="imprb_elap_time" name="imprb_elap_time" value="${sqlDetail.imprb_elap_time}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="imprb_buffer_cnt" name="imprb_buffer_cnt" value="${sqlDetail.imprb_buffer_cnt}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="imprb_pga_usage" name="imprb_pga_usage" value="${sqlDetail.imprb_pga_usage}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="impra_elap_time" name="impra_elap_time" value="${sqlDetail.impra_elap_time}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="impra_buffer_cnt" name="impra_buffer_cnt" value="${sqlDetail.impra_buffer_cnt}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="impra_pga_usage" name="impra_pga_usage" value="${sqlDetail.impra_pga_usage}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%"/></td>

				<td class="ctext"><input type="number" id="elap_time_impr_ratio" name="elap_time_impr_ratio" value="${sqlDetail.elap_time_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="buffer_impr_ratio" name="buffer_impr_ratio" value="${sqlDetail.buffer_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
				<td class="ctext"><input type="number" id="pga_impr_ratio" name="pga_impr_ratio" value="${sqlDetail.pga_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
			</tr>
		</table>
		<div style="height:530px;overflow-y:auto;overflow-x:hidden;">
			<table class="detailT2">
				<colgroup>
					<col style="width:10%;"/>
					<col style="width:40%;"/>
					<col style="width:10%;"/>
					<col style="width:40%;"/>
				</colgroup>
				<tr>
					<th>문제점</th>
					<td><textarea name="controversialist" id="controversialist" rows="10" style="margin-top:5px;width:99%;height:100px" ${textAreaReadonlyFlag} >${sqlDetail.controversialist}</textarea></td>
					<th>개선내역</th>
					<td><textarea name="impr_sbst" id="impr_sbst" rows="10" style="margin-top:5px;width:99%;height:100px" ${textAreaReadonlyFlag} >${sqlDetail.impr_sbst}</textarea></td>
				</tr>
				<tr>
					<th>인덱스내역</th>
					<td colspan="3">
						<div id="indexHist" class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;padding-top:10px;">
							<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>								
						<!-- 튜닝상태가 6:튜닝완료(적용대기)이거나 8:튜닝종료일 경우에는 수정불가 -->
						<c:choose>
							<c:when test="${sqlTuning.tuning_status_cd eq '6'}">
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>				
				<tr>
					<th>
						개선SQL<br/><br/>
						<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a>
					</th>
					<td colspan="3">
						<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:99%;height:300px" wrap="off" ${textAreaReadonlyFlag} >
						${sqlDetail.impr_sql_text}
						</textarea>
						<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="width:0px;height:0px;visibility:hidden;">
						${sqlDetail.impr_sql_text}
						</textarea>
					</td>
				</tr>
				<tr>
					<th colspan="2">
						개선전 실행계획<br/><br/>
						<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
					</th>
					<th colspan="2">
						개선후 실행계획<br/><br/>
						<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
					</th>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" wrap="off" ${textAreaReadonlyFlag} >
						${sqlDetail.imprb_exec_plan}
						</textarea>
						<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="width:0px;height:0px;visibility:hidden;">
						${sqlDetail.imprb_exec_plan}
						</textarea>
					</td>
					<td colspan="2">
						<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" wrap="off" ${textAreaReadonlyFlag} >
						${sqlDetail.impra_exec_plan}
						</textarea>
						<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="width:0px;height:0px;visibility:hidden;">
						${sqlDetail.impra_exec_plan}
						</textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dtlBtn">
			<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="Btn_GoList();">목록</a>
		</div>
	</form:form>
<!-- </div> -->
</body>
</html>