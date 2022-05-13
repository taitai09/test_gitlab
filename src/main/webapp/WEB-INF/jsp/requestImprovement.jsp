<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>튜닝요청</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/requestImprovement.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/requestImprovement_popup.js?ver=<%=today%>"></script><!-- 성능개선 - 튜닝요청 초기값 설정 팝업 -->
	<script>
		var initValues = '${initValues}';
		
		var tuning_requester_tel_num = '${initValues.tuning_requester_tel_num}';
		var dbid = '${initValues.dbid}';
		var program_type_cd = '${initValues.program_type_cd}';
		var parsing_schema_name = '${initValues.parsing_schema_name}';
		var program_type_cd = '${initValues.program_type_cd}';
		var wrkjob_peculiar_point = '${initValues.wrkjob_peculiar_point}';
		var request_why = '${initValues.request_why}';
		
		var auth_cd = "${users.auth_cd}";
	</script>
	
	<style type="text/css">
		.disableHover:hover{
			border: 1px solid #bbb;
		}
		#wrkjob_mgr_nm:-ms-clear{
			display: none;
		}
		.inputArea{
			position: relative;
		}
		#resetBtn{
			position: absolute;
			top: 7px;
			right: 130px;
			width: 15px;
			height: 15px;
			font-size: 12px;
			color: #88837F;
			cursor: pointer;
			visibility:hidden;
		}
		#resetBtn:hover{
			color: #38312A;
		}
	</style>
	
	
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				<a href="javascript:;" class="marginB2 marginL5 easyui-linkbutton" onClick="showInitSaveSetting_popup();"><i class="btnIcon fas fa-cog fa-lg fa-fw"></i> 초기값 설정</a>
			</div>
		</div>	
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${user_id}"/>
				<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${wrkjob_cd}"/>
				<input type="hidden" id="dbid" name="dbid" value="${tuningTargetSql.dbid}"/>
				<input type="hidden" id="program_type_cd" name="program_type_cd"/>
				<input type="hidden" id="batch_work_div_cd" name="batch_work_div_cd"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name" value="${tuningTargetSql.parsing_schema_name}"/>
				<input type="hidden" id="sql_id" name="sql_id" value="${tuningTargetSql.sql_id}"/>
				<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${tuningTargetSql.plan_hash_value}"/>
				<input type="hidden" id="tuning_no" name="tuning_no"/>
				<input type="hidden" id="call_from_sqlPerformance" name="call_from_sqlPerformance" value="${tuningTargetSql.call_from_sqlPerformance}"/>
				<input type="hidden" id="bindValueIsNull" name="bindValueIsNull" value="${tuningTargetSql.bindValueIsNull}"/>
				<input type="hidden" id="sqlStats_sql_id" name="sqlStats_sql_id" value="${tuningTargetSql.sqlStats_sql_id}"/>
				<input type="hidden" id="sqlStats_plan_hash_value" name="sqlStats_plan_hash_value" value="${tuningTargetSql.sqlStats_plan_hash_value}"/>
				<input type="hidden" id="sqlStats_elapsed_time" name="sqlStats_elapsed_time" value="${tuningTargetSql.sqlStats_elapsed_time}"/>
				<input type="hidden" id="sqlStats_buffer_gets" name="sqlStats_buffer_gets" value="${tuningTargetSql.sqlStats_buffer_gets}"/>
				<input type="hidden" id="maxUploadSize" name="maxUploadSize" value="${maxUploadSize}"/>
				<input type="hidden" id="noneFileList" name="noneFileList" value="${noneFileList}"/>
				<input type="hidden" id="deleteFiles" name="deleteFiles" />
				
				<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${user_nm}"/>
				<input type="hidden" id="nowDate" name="nowDate" value="${nowDate}"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
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
						<th>요청자</th>
						<td class="ltext">${user_nm}</td>
						<th>업무팀</th>
						<td class="ltext">${wrkjob_nm}</td>
						<th>연락처</th>
						<td class="ltext"><input type="text" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${initValues.tuning_requester_tel_num}" data-options="readonly:false" class="w200 easyui-textbox" tabindex="1"/></td>
					</tr>
					<tr>
						<th>프로그램 유형</th>
						<td class="ltext"><select id="selectProgram" name="selectProgram" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="2" required="true"></select></td>
						<th>배치작업주기</th>
						<td class="ltext"><select id="selectBatch" name="selectBatch" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="3" ></select></td>
						<th>수행횟수</th>
						<td class="ltext"><input type="number" id="exec_cnt" name="exec_cnt" value="${tuningTargetSql.exec_cnt}" class="w200 easyui-textbox" tabindex="4" /></td>
					</tr>
					<tr>
						<th>업무담당자</th>
						<td class="inputArea">
							<input type="text" id="wrkjob_mgr_nm" name="wrkjob_mgr_nm" value="" data-options="readonly:true" class="w160 easyui-textbox">
							<sec:authorize access="hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')">
								<span id="resetBtn"><i class="fas fa-times"></i></span>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showWorkTunerPopup();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 업무담당자 조회</a>
							</sec:authorize>
							<sec:authorize access="!hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')">
								<a href="javascript:;" class="w120 easyui-linkbutton l-btn l-btn-small l-btn-disabled disableHover" data-options="disabled:true"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 업무담당자 조회</a>
							</sec:authorize>
							<input type="hidden" id="wrkjob_mgr_id" name="wrkjob_mgr_id" value="">
						</td>
						
						<th>담당업무</th>
						<td>
							<input type="text" id="wrkjob_mgr_wrkjob_nm" name="wrkjob_mgr_wrkjob_nm" value=""
								data-options="readonly:true" class="w120 easyui-textbox">
							<input type="hidden" id="wrkjob_mgr_wrkjob_cd" name="wrkjob_mgr_wrkjob_cd" value="">
						</td>
						
						<th>업무담당자연락처</th>
						<td>
							<input type="text" id="wrkjob_mgr_tel_num" name="wrkjob_mgr_tel_num" value=""
								data-options="readonly:true" class="w120 easyui-textbox">
						</td>
					</tr>
					<tr>
						<th>DB</th>
						<td class="ltext"><select id="selectSystem" name="selectSystem" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="5" required="true"></select></td>
						<th>DB접속계정</th>
						<td class="ltext"><select id="selectParsingSchemaName" name="selectParsingSchemaName" class="w200 easyui-combobox" tabindex="6" data-options="editable:true" required="true"></select></td>
						<th>튜닝완료요청일자</th>
						<td class="ltext"><input type="text" id="tuning_complete_due_dt" name="tuning_complete_due_dt" class="w200 datapicker easyui-datebox " tabindex="7" data-options="panelHeight:'auto',editable:false" required="true" value="${tuningTargetSql.tuning_complete_due_dt}"/></td>
					</tr>
					<tr>
						<th>현재 처리시간(초)</th>
						<td class="ltext"><input type="number" id="current_elap_time" name="current_elap_time" value="${tuningTargetSql.current_elap_time}" class="w200 easyui-textbox" tabindex="8" /></td>
						<th>결과건수</th>
						<td class="ltext"><input type="number" id="forecast_result_cnt" name="forecast_result_cnt" value="${tuningTargetSql.forecast_result_cnt}" class="w200 easyui-textbox" tabindex="9" /></td>
						<th>목표처리시간(초)</th>
						<td class="ltext"><input type="number" id="goal_elap_time" name="goal_elap_time" class="w200 easyui-textbox" tabindex="10" "/></td>
					</tr>
					<tr>
						<th>소스파일명(Full Path)</th>
						<td class="ltext"><input type="text" id="tr_cd" name="tr_cd" value="${tuningTargetSql.tr_cd}" class="easyui-textbox" style="width:98%" tabindex="11"data-options="prompt:'예) 파일명.XML'" /></td>
						<th>SQL식별자(DBIO)</th>
						<td colspan="3" class="ltext"><input type="text" id="dbio" name="dbio" value="${tuningTargetSql.dbio}" class="easyui-textbox" style="width:766px;" tabindex="12" /></td>
					</tr>
				</table>
				<table class="detailT">
					<colgroup>
						<col style="width:10%;">
						<col style="width:40%;">
						<col style="width:10%;">
						<col style="width:40%;">
					</colgroup>
					<tr>
						<th>업무특이사항</th>
						<td>
							<a href="javascript:;" class="textBtn" onclick="Btn_SettingText('${initValues.wrkjob_peculiar_point}')"> ${initValues.wrkjob_peculiar_point}</a>
							<textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;padding:5px;width:97%;height:25px" tabindex="13"></textarea>
						</td>
						<th>요청사유</th>
						
						<td>
							<a href="javascript:;" class="textBtn" onclick="Btn_SettingText2('${initValues.request_why}')"> ${initValues.request_why}</a>
							<textarea name="request_why" id="request_why" rows="5" style="margin-top:3px;padding:5px;width:97%;height:25px" tabindex="14"></textarea>
						</td>
					</tr>
					<tr>
						<th>SQL Text
						<span><span class="help-tip">
							<p>* SQL Text 내용 입력시 주의 사항<br/>
							- 화면에서 입력되는 조건에 대해 바인드 변수 처리된 상태로 기입<br/>
							- 프로그램에 사용되는 전체 SQL문을 누락없이 기입<br/>
							- 페이징 처리 부분 모두 포함<br/>
							</p>
						</span></span> 
						</th> 
						<td style="vertical-align:top;">
							
							<div style="color:#151515;">
								<br/>
								<span style="font-weight:bold;font-size:12px;">※ SQL Text 내용 입력시 주의 사항</span><br/>
								&emsp;- 화면에서 입력되는 조건에 대해 바인드 변수 처리된 상태로 기입<br/>
								&emsp;- 프로그램에 사용되는 전체 SQL문을 누락없이 기입<br/>
								&emsp;- 페이징 처리 부분 모두 포함<br/>
								<br/>
							</div>	
							
							<textarea name="sql_text" id="sql_text" class="validatebox-invalid" rows="30" style="overlow:scroll;margin-top:3px;padding:5px;width:97%;height:290px" wrap="off" tabindex="15" >${tuningTargetSql.sql_text}</textarea>
						</td>
						<th>SQL BIND
						
						<span class="help-tip">
							 <p>* SQL BIND 값 입력시 주의 사항<br/>
								- 바인드 변수 처리된 모든 변수에 값이 매핑되어야 함<br/>
								- 성능저하 발생시와 동일한 값<br/>
								- 결과값이 나오는 값<br/>
								- ex) :B1 NULL<br/>
									 &emsp;&nbsp;&nbsp;&nbsp;&nbsp; :B2 20180212<br/>
							</p>
						</span>
						<br/><br/>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="setBindValue();">변수추출</a><br>
						</th>
						<td style="vertical-align:top;">
						
							<div style="color:#151515;">
								 <span style="font-weight:bold;font-size:12px;">
								 	※ SQL BIND 값 입력시 주의 사항</span><br/>
									&emsp;- 바인드 변수 처리된 모든 변수에 값이 매핑되어야 함<br/>
									&emsp;- 성능저하 발생시와 동일한 값<br/>
									&emsp;- 결과값이 나오는 값<br/>
									&emsp;- ex) :B1 NULL<br/>
									&emsp;&emsp;&nbsp;&nbsp;&nbsp;&nbsp; :B2 20180212<br/>
							</div>
						
							<textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:3px;padding:5px;width:97%;height:290px;font-family:돋음;font-size:12px;" wrap="off" tabindex="16" ></textarea>
						</td>
					</tr>
					<tr height="30px;">
						<th>첨부파일</th>
						<td colspan="3" style="vertical-align: middle;">
							<input type="file" id="uploadFile" name="uploadFile" multiple="multiple" style="width: 75px; border: 0px;" onchange="fileUploadChange(this);">
							<span id="fileNames"><label style="color:darkgray;margin:0;">파일을 선택해 주세요.</label></span>
						</td>
					</tr>
				</table>
				<div class="dtlBtn">
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_GoSave();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/requestImprovement_popup.jsp" %> <!-- 튜닝요청 초기값 설정 팝업 -->
</body>
</html>