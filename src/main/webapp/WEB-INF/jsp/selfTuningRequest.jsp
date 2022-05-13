<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.26	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 셀프 성능 점검 :: 튜닝요청</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/selfTuningRequest.js?ver=<%=today%>"></script>
    <!-- 성능개선 - 튜닝요청 초기값 설정 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/requestImprovement_popup.js?ver=<%=today%>"></script>
		<script>
		var initValues = '${initValues}';
		
		var tuning_requester_tel_num = '${initValues.tuning_requester_tel_num}';
		var dbid = '${initValues.dbid}';
		var program_type_cd = '${initValues.program_type_cd}';
		var parsing_schema_name = '${initValues.parsing_schema_name}';
		var program_type_cd = '${initValues.program_type_cd}';
		var wrkjob_peculiar_point = '${initValues.wrkjob_peculiar_point}';
		var request_why = '${initValues.request_why}';

		var param_parsing_schema_name = '${tuningTargetSql.parsing_schema_name}';
		var param_program_type_cd = '${tuningTargetSql.program_type_cd}';
		var param_dbid = '${tuningTargetSql.dbid}';

	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>
				<a href="javascript:;" class="marginB2 marginL5 easyui-linkbutton" onClick="showInitSaveSetting_popup();"><i class="btnIcon fas fa-cog fa-lg fa-fw"></i> 초기값 설정</a>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${user_id}"/>
				<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${wrkjob_cd}"/>
				<input type="hidden" id="dbid" name="dbid" value="${tuningTargetSql.dbid}"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name" value="${tuningTargetSql.parsing_schema_name}"/>
				<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${tuningTargetSql.wrkjob_cd}"/>
				<input type="hidden" id="searchKey" name="searchKey" value="${tuningTargetSql.searchKey}"/>
				<input type="hidden" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}"/>
				<input type="hidden" id="program_type_cd" name="program_type_cd" value="${tuningTargetSql.program_type_cd}"/>
				<input type="hidden" id="batch_work_div_cd" name="batch_work_div_cd"/>
				<input type="hidden" id="tuning_no" name="tuning_no"/>
				
<!-- 				<input type="hidden" id="uploadFile" multiple="multiple" name="uploadFile" class="easyui-filebox"/> -->
				
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
						<td class="ltext"><input type="text" id="tuning_requester_nm" name="tuning_requester_nm" value="${user_nm}" data-options="readonly:true" class="w200 easyui-textbox" tabindex="1"/></td>
						<th>업무팀</th>
						<td class="ltext"><input type="text" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${wrkjob_nm}" data-options="readonly:true" class="w200 easyui-textbox" tabindex="2"/></td>
						<th>연락처</th>
						<td class="ltext"><input type="text" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${initValues.tuning_requester_tel_num}" data-options="readonly:true" class="w200 easyui-textbox" tabindex="3"/></td>
					</tr>
					<tr>
						<th>프로그램 유형</th>
						<td class="ltext"><select id="selectProgram" name="selectProgram" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="4" required="true"></select></td>
						<th>배치작업주기</th>
						<td class="ltext"><select id="selectBatch" name="selectBatch" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="5" ></select></td>
						<th>수행횟수</th>
						<td class="ltext"><input type="number" id="exec_cnt" name="exec_cnt" value="${tuningTargetSql.exec_cnt}" class="w200 easyui-textbox" tabindex="6" /></td>
					</tr>
					<tr>
						<th>DB</th>
						<td class="ltext"><select id="selectSystem" name="selectSystem" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" tabindex="7" required="true"></select></td>
						<th>DB접속계정</th>
						<td class="ltext"><select id="selectParsingSchemaName" name="selectParsingSchemaName" data-options="editable:true" class="w200 easyui-combobox" tabindex="8" required></select></td>
						<th>튜닝완료요청일자</th>
						<td class="ltext"><input type="text" id="tuning_complete_due_dt" name="tuning_complete_due_dt" value="${tuningTargetSql.tuning_complete_due_dt}" data-options="editable:false" required="true" class="w200 datapicker easyui-datebox" tabindex="9"/></td>
					</tr>
					<tr>
						<th>현재 처리시간(초)</th>
						<td class="ltext"><input type="number" id="current_elap_time" name="current_elap_time" value="${tuningTargetSql.current_elap_time}" class="w200 easyui-textbox" tabindex="10" /></td>
						<th>결과건수</th>
						<td class="ltext"><input type="number" id="forecast_result_cnt" name="forecast_result_cnt" value="${tuningTargetSql.forecast_result_cnt}" class="w200 easyui-textbox" tabindex="11" /></td>
						<th>목표처리시간(초)</th>
						<td class="ltext"><input type="number" id="goal_elap_time" name="goal_elap_time" value="${tuningTargetSql.goal_elap_time}" class="w200 easyui-textbox" tabindex="11" /></td>
					</tr>
					<tr>
						<th>소스파일명(Full Path)</th>
						<td class="ltext"><input type="text" id="tr_cd" name="tr_cd" value="${tuningTargetSql.tr_cd}" class="easyui-textbox" style="width:98%" tabindex="12" data-options="prompt:'예) 파일명.XML'" /></td>
						<th>SQL식별자(DBIO)</th>
						<td colspan="3" class="ltext"><input type="text" id="dbio" name="dbio" style="width:766px;" value="${tuningTargetSql.dbio}" class="easyui-textbox" style="width:98%" tabindex="13" /></td>
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
							<textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;padding:5px;width:98%;height:65px" tabindex="14">${tuningTargetSql.wrkjob_peculiar_point}</textarea>
						</td>
						<th>요청사유</th>
						<td>
							<a href="javascript:;" class="textBtn" onclick="Btn_SettingText2('${initValues.request_why}')"> ${initValues.request_why}</a>
							<textarea name="request_why" id="request_why" rows="5" style="margin-top:3px;padding:5px;width:98%;height:65px" tabindex="15">${tuningTargetSql.request_why}</textarea>
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
						<td>
							<div style="color:#151515;">
								<br/>
								<span style="font-weight:bold;font-size:12px;">※ SQL Text 내용 입력시 주의 사항</span><br/>
								&emsp;- 화면에서 입력되는 조건에 대해 바인드 변수 처리된 상태로 기입<br/>
								&emsp;- 프로그램에 사용되는 전체 SQL문을 누락없이 기입<br/>
								&emsp;- 페이징 처리 부분 모두 포함<br/>
								<br/>
							</div>
							<textarea name="sql_text" id="sql_text" rows="30" style="overlow:scroll;margin-top:3px;padding:5px;width:98%;height:300px" wrap="off" tabindex="16" class="validatebox-invalid" >${tuningTargetSql.sql_text}</textarea>
						</td>
						<th>SQL BIND
						<span><span class="help-tip">
							 <p>* SQL BIND 값 입력시 주의 사항<br/>
								- 바인드 변수 처리된 모든 변수에 값이 매핑되어야 함<br/>
								- 성능저하 발생시와 동일한 값<br/>
								- 결과값이 나오는 값<br/>
								- ex) :B1 NULL<br/>
									 &emsp;&nbsp;&nbsp;&nbsp;&nbsp; :B2 20180212<br/>
							</p>
						</span></span>
						<br/><br/>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="setBindValue();">변수추출</a>
						</th>
						<td>
						
						<div style="color:#151515;">
							 <span style="font-weight:bold;font-size:12px;">
							 	※ SQL BIND 값 입력시 주의 사항</span><br/>
								&emsp;- 바인드 변수 처리된 모든 변수에 값이 매핑되어야 함<br/>
								&emsp;- 성능저하 발생시와 동일한 값<br/>
								&emsp;- 결과값이 나오는 값<br/>
								&emsp;- ex) :B1 NULL<br/>
								&emsp;&emsp;&nbsp;&nbsp;&nbsp;&nbsp; :B2 20180212<br/>
						</div>
						
							<textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:3px;padding:5px;width:98%;height:300px;font-family:돋음;font-size:12px;" wrap="off" tabindex="16">${tuningTargetSql.sql_desc}</textarea>
						</td>
					</tr>
				</table>
						<%-- <table class="detailT">
					<tr>
						<td style="padding:5px;height:400px;">
							<div class="easyui-layout" style="width:100%;height:400px;">
								<div data-options="region:'center'" title="SQL Text" style="padding:5px;">
									<textarea name="sql_text" id="sql_text" rows="30" style="overlow:scroll;margin-top:5px;width:99%;height:350px" wrap="off">${tuningTargetSql.sql_text}</textarea>
								</div>
								<div id="bindList" data-options="region:'east',split:true,hideCollapsedContent:false" title="Bind Varable List" style="width:45%;padding:3px">
									<div class="easyui-layout" data-options="border:false,fit:true">
										<div data-options="region:'center',border:false" style="padding:5px;">
											<table id="bindTable" class="detailT" style="border-top:1px solid #CDC4DE;margin-left:0px;margin-top:0px;margin-bottom:0px;width:100%">
												<colgroup>
													<col style="width:15%;">
													<col style="width:85%;">
												</colgroup>	
												<tbody>	
													<tr>
														<th>바인드 1</th>
														<td>										
															<table id="bindValueTable1" class="detailT" style="margin-left:3px;margin-top:3px;width:100%">
																<colgroup>	
																	<col style="width:13%;">
																	<col style="width:23%;">
																	<col style="width:23%;">
																	<col style="width:24%;">
																	<col style="width:17%;">
																</colgroup>
																<thead>
																	<tr>
																		<th>순번</th>
																		<th>변수명</th>
																		<th>변수값</th>
																		<th>변수타입</th>
																		<th>필수여부</th>
																	</tr>	
																</thead>						
																<tbody>
																	<c:forEach items="${sqlBindList}" var="sqlBind" varStatus="stat">
																		<tr>													
																			<td class="ltext"><input type="hidden" id="bind_set_seq${stat.count}" name="bind_set_seq" value="${sqlBind.bind_set_seq}"/><input type="text" id="bind_seq${stat.count}" name="bind_seq" value="${sqlBind.bind_seq}" data-options="readonly:true" class="w40 easyui-textbox"/></td>
																			<td class="ltext"><input type="text" id="bind_var_nm${stat.count}" name="bind_var_nm" value="${sqlBind.bind_var_nm}" class="w80 easyui-textbox"/></td>
																			<td class="ltext"><input type="text" id="bind_var_value${stat.count}" name="bind_var_value" value="${sqlBind.bind_var_value}" class="w70 easyui-textbox"/></td>
																			<td class="ltext"><select id="bind_var_type${stat.count}" name="bind_var_type" data-options="panelHeight:'auto'" class="w80 easyui-combobox"><option value="string" <c:if test="${sqlBind.bind_var_type eq 'string'}">selected</c:if>>String타입</option><option value="number" <c:if test="${sqlBind.bind_var_type eq 'number'}">selected</c:if>>Number타입</option><option value="date" <c:if test="${sqlBind.bind_var_type eq 'date'}">selected</c:if>>Date타입</option><option value="char" <c:if test="${sqlBind.bind_var_type eq 'char'}">selected</c:if>>Char타입</option></select></td>
																			<td class="ltext"><select id="mandatory_yn${stat.count}" name="mandatory_yn" data-options="panelHeight:'auto'" class="w60 easyui-combobox"><option value="Y" <c:if test="${sqlBind.mandatory_yn eq 'Y'}">selected</c:if>>필수</option><option value="N" <c:if test="${sqlBind.mandatory_yn ne 'Y'}">selected</c:if>>필수아님</option></select></td>															
																		</tr>														
																	</c:forEach>
																</tbody>
															</table>
															<div class="dtlBtn" style="margin-bottom:5px;">
																<a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-add'" onClick="Btn_addBindValue('1');">변수추가</a>						
																<a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-remove'" onClick="Btn_removeBindValue('1');">변수삭제</a>
															</div>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
										<div data-options="region:'south',border:false" style="height:50px;padding:5px;">
											<div class="dtlBtn">
												<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-add'" onClick="Btn_addBindTable();">바인드 유형추가</a>						
												<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-remove'" onClick="Btn_removeBindTable();">바인드 유형삭제</a>
											</div>
										</div> 
									</div>
								</div>
							</div>
						</td>
					</tr>
				</table> --%>
				<div class="dtlBtn">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="closeTab();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 닫기 </a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_GoSave();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
<%@include file="/WEB-INF/jsp/include/popup/requestImprovement_popup.jsp" %> <!-- 튜닝요청 초기값 설정 팝업 -->
</html>