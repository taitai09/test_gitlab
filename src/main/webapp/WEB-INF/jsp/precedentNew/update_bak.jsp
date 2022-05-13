<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2017.10.20	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>사례/가이드 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/precedentNew/update.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" style="width:100%;height:773px">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				
				<c:set var = "title" scope = "page" value = ""/>

				<c:choose>
					<c:when test="${auth_cd eq 'ROLE_DEV'}">
     					<c:set var = "title" value = "SQL 튜닝 가이드"/>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${guide.guide_div_cd eq '1'}">
		     					<c:set var = "title" value = "SQL 튜닝 가이드"/>
							</c:when>
							<c:when test="${guide.guide_div_cd eq '2'}">
		     					<c:set var = "title" value = "SQL 튜닝 사례"/>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				<span class="h3">
						<i class="far fa-check-square fa-lg fa-fw"></i><c:out value="${title}"/>
				</span>

			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="submit_form" name="submit_form" enctype="multipart/form-data" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="searchBtnClickCount1" name="searchBtnClickCount1" value="${searchBtnClickCount1}"/>
				<input type="hidden" id="searchBtnClickCount2" name="searchBtnClickCount2" value="${searchBtnClickCount2}"/>
				<input type="hidden" id="searchKey" name="searchKey" value="${perfGuide.searchKey}"/>
				<input type="hidden" id="guide_div_cd" name="guide_div_cd" value="${guide.guide_div_cd}"/>
				<input type="hidden" id="guide_no" name="guide_no" value="${perfGuide.guide_no}"/>
				<input type="hidden" id="tuning_no" name="tuning_no" value="${perfGuide.tuning_no}"/>
				<input type="hidden" id="strStartDt" name="strStartDt" value="${perfGuide.strStartDt}"/>
				<input type="hidden" id="strEndDt" name="strEndDt" value="${perfGuide.strEndDt}"/>
<%-- 				<input type="hidden" id="file_nm" name="file_nm" value="${guideFile.file_nm}"/> --%>
<%-- 				<input type="hidden" id="org_file_nm" name="org_file_nm" value="${guideFile.org_file_nm}"/> --%>
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
				<input type="hidden" id="use_seq" name="use_seq" value="${useSeq}"/>
				<input type="hidden" id="auth_cd" name="auth_cd" value="${auth_cd}"/>
				<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${sqlDetail.tuning_complete_dt}"/>
				<input type="hidden" id="top_fix_yn" name="top_fix_yn" value="${guide.top_fix_yn}"/>
				
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>

				<c:choose>
					<c:when test="${guide.guide_div_cd eq '2'}">
						<div id="historyViewTab" class="easyui-tabs" data-options="border:false" style="width:100%;">
							<div title="SQL 요청 상세">
								<div style="height:645px;overflow-y:auto">
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
											<th>튜닝번호</th>
											<td>${sqlDetail.tuning_no}</td>
											<th>요청(선정)일자</th>
											<td>${sqlDetail.tuning_request_dt}</td>
											<th>튜닝요청구분</th>
											<td>${sqlDetail.choice_div_cd_nm}</td>
										</tr>
										<tr>
											<th>업무담당자</th>
											<td>${selection.wrkjob_mgr_nm}</td>
											<th>담당업무</th>
											<td>${selection.wrkjob_mgr_wrkjob_nm}</td>
											<th>업무담당자 연락처</th>
											<td>${selection.wrkjob_mgr_tel_num}</td>
										</tr>
										<tr>
											<th>프로그램 유형</th>
											<td>${sqlDetail.program_type_cd_nm}</td>
											<th>배치작업주기</th>
											<td >${sqlDetail.batch_work_div_cd_nm}</td>
											<th>수행횟수</th>
											<td >${sqlDetail.exec_cnt}</td>
										</tr>
										<tr>
											<th>DB</th>
											<td>${sqlDetail.db_name}</td>
											<th>DB접속정보</th>
											<td>${sqlDetail.parsing_schema_name}</td>
											<th>튜닝담당자</th>
											<td>${sqlDetail.perfr_nm}</td>
										</tr>
										<tr>
											<th>소스파일명(Full Path)</th>
											<td>${sqlDetail.tr_cd}</td>
											<th>SQL식별자(DBIO)</th>
											<td>${sqlDetail.dbio}</td>
											<th>튜닝일자</th>
											<td>${sqlDetail.tuning_complete_dt}</td>
										</tr>
									</table>
									<table class="detailT">
										<colgroup>
											<col style="width:10%;">
											<col style="width:40%;">
											<col style="width:10%;">
											<col style="width:40%;">
										</colgroup>
										<c:if test="${sqlDetail.choice_div_cd eq '3' or sqlDetail.choice_div_cd eq 'B'}"> <!-- 요청일 경우만 표시 -->
											<tr>
												<th>업무특이사항</th>
												<td><textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.wrkjob_peculiar_point}</textarea></td>
												<th>요청사유</th>
												<td><textarea name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.request_why}</textarea></td>
											</tr>
											<tr>
												<th>
													SQL TEXT<br/><br/>
													<a href="javascript:;" id="sqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea" data-options="iconCls:'icon-save'">SQL 복사</a>
												</th>
												<td style="padding:10px;height:300px;">
													<div id="sqlTextArea" style="width:100%;height:100%;overflow-y:auto;">
														${fn:replace(fn:replace(sqlDetail.sql_text,newLineChar,'<br/>'),' ','&nbsp;')}
													</div>
												</td>
												<th>SQL BIND</th>
												<td><textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:5px;width:98%;height:300px" readonly>${sqlDetail.sql_desc}</textarea></td>
											</tr>
										</c:if>
										<%-- <tr>
											<td colspan="2" style="padding:5px;height:350px;">
											    <div class="easyui-layout" style="width:100%;height:350px;">
													<div data-options="region:'center'" title="SQL Text" style="padding:5px;">
														<div style="width:100%;height:100%;overflow-y:auto;">
															${fn:replace(sqlDetail.sql_text,newLineChar,'<br/>')}
														</div>
													</div>
													<div id="bindList" data-options="region:'east',split:true,hideCollapsedContent:false" title="Bind Varable List" style="width:35%;padding:3px">
														<c:if test="${bindSetList != null}">
															<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:99%">
																<colgroup>
																	<col style="width:10%;">
																	<col style="width:90%;">
																</colgroup>
																<c:forEach items="${bindSetList}" var="bindSet" varStatus="status">
																	<tr>
																		<th>바인드<br/>${status.count}</th>
																		<td>
																			<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:99%">
																				<colgroup>
																					<col style="width:15%;">
																					<col style="width:23%;">
																					<col style="width:23%;">
																					<col style="width:24%;">
																					<col style="width:15%;">
																				</colgroup>
																				<tr>
																					<th>순번</th>
																					<th>변수명</th>
																					<th>변수값</th>
																					<th>변수타입</th>
																					<th>필수여부</th>
																				</tr>
																				<c:forEach items="${sqlBindList}" var="sqlBind" varStatus="stat">
																					<c:if test="${bindSet.bind_set_seq eq sqlBind.bind_set_seq}">
																						<tr>
																							<td class="ctext">${sqlBind.bind_seq}</td>
																							<td>${sqlBind.bind_var_nm}</td>
																							<td>${sqlBind.bind_var_value}</td>
																							<td class="ctext">
																								<c:choose>
																									<c:when test="${sqlBind.bind_var_type eq 'string'}">String타입</c:when>
																									<c:when test="${sqlBind.bind_var_type eq 'number'}">Number타입</c:when>
																									<c:when test="${sqlBind.bind_var_type eq 'date'}">Date타입</c:when>
																									<c:when test="${sqlBind.bind_var_type eq 'char'}">Char타입</c:when>
																								</c:choose>
																							</td>
																							<td class="ctext">${sqlBind.mandatory_yn}</td>
																						</tr>
																					</c:if>
																				</c:forEach>
																			</table>
																		</td>
																	</tr>
																</c:forEach>
															</table>
														</c:if>
													</div>
												</div>
											</td>
										</tr> --%>
									</table>
								</div>
							</div>
							<div title="SQL 개선 상세">
								<div style="height:645px;overflow-y:auto">
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
											<th>응답시간 (Sec)</th>
											<th>블럭수</th>
											<th>PGA 사용량(MB)</th>
										</tr>
										<tr>
											<td class="ctext">${sqlDetail.imprb_elap_time}</td>
											<td class="ctext">${sqlDetail.imprb_buffer_cnt}</td>
											<td class="ctext">${sqlDetail.imprb_pga_usage}</td>
											<td class="ctext">${sqlDetail.impra_elap_time}</td>
											<td class="ctext">${sqlDetail.impra_buffer_cnt}</td>
											<td class="ctext">${sqlDetail.impra_pga_usage}</td>
											<td class="ctext">${sqlDetail.elap_time_impr_ratio} %</td>
											<td class="ctext">${sqlDetail.buffer_impr_ratio} %</td>
											<td class="ctext">${sqlDetail.pga_impr_ratio} %</td>
										</tr>
									</table>
									<table class="detailT2">
										<colgroup>
											<col style="width:10%;"/>
											<col style="width:40%;"/>
											<col style="width:10%;"/>
											<col style="width:40%;"/>
										</colgroup>
										<tr>
											<th>문제점</th>
											<td><textarea name="controversialist" id="controversialist" rows="10" style="margin-top:5px;width:99%;height:100px" readonly>${sqlDetail.controversialist}</textarea></td>
											<th>개선내역</th>
											<td><textarea name="impr_sbst" id="impr_sbst" rows="10" style="margin-top:5px;width:99%;height:100px" readonly>${sqlDetail.impr_sbst}</textarea></td>
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
												<a href="javascript:;" id="imprSqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text_h" data-options="iconCls:'icon-save'">SQL 복사</a>
											</th>
											<td colspan="3">
												<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
												${sqlDetail.impr_sql_text}
												</textarea>
												<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="display:none;">
												${sqlDetail.impr_sql_text}
												</textarea>
											</td>
										</tr>
										<tr>
											<th colspan="2">
												개선전 실행계획<br/><br/>
												<a href="javascript:;" id="imprbExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan_h" data-options="iconCls:'icon-save'">실행계획 복사</a>
											</th>
											<th colspan="2">
												개선후 실행계획<br/><br/>
												<a href="javascript:;" id="impraExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan_h" data-options="iconCls:'icon-save'">실행계획 복사</a>
											</th>
										</tr>
										<tr>
											<td colspan="2">
												<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
												${sqlDetail.imprb_exec_plan}
												</textarea>
												<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="display:none;">
												${sqlDetail.imprb_exec_plan}
												</textarea>
											</td>
											<td colspan="2">
												<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
												${sqlDetail.impra_exec_plan}
												</textarea>
												<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="display:none;">
												${sqlDetail.impra_exec_plan}
												</textarea>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<table class="detailT">
							<colgroup>
								<col style="width:15%;">
								<col style="width:85%;">
							</colgroup>
							<tr>
								<th>제목</th>
								<td>
									<input type="text" id="guide_title_nm" name="guide_title_nm" value="${guide.guide_title_nm}" class="easyui-textbox" style="width:1000px"/>
									<span style="padding-left:10px;padding-right:5px;">상단공지</span>
									<input type="checkbox" id="chkTopFixYn" name="chkTopFixYn" class="w120 easyui-switchbutton"/>
								</td>
							</tr>
							<tr height="130px;">
								<th>내용</th>
								<td><textarea name="guide_sbst" id="guide_sbst" rows="20" style="width:97%;padding:15px;IME-MODE:active;">${guide.guide_sbst}</textarea></td>
							</tr>
							<tr>
								<th>첨부파일</th>
								<td>
<!-- 									<input id="uploadFile" name="uploadFile" class="easyui-filebox" data-options="prompt:'튜닝가이드 파일을 선택해 주세요.'" style="width:50%"/> -->
<%-- 									<c:if test="${guideFile.file_seq ne ''}"> --%>
<%-- 										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downGuideFile();">${guideFile.org_file_nm}</a> --%>
<%-- 									</c:if>								 --%>
									<c:if test="${(auth_cd ne 'ROLE_DEV') and (auth_cd ne 'ROLE_ITMANAGER')}">
										<input id="uploadFile" multiple="multiple" name="uploadFile" class="easyui-filebox" data-options="prompt:'튜닝가이드 파일을 선택해 주세요.'" style="width:50%"/>
									</c:if>
									<c:forEach items="${guideFiles}" var="result" varStatus="status">
										<c:if test="${result.file_seq ne ''}">
											<span>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downGuideFile ('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
												<c:choose>
													<c:when test="${auth_cd eq 'ROLE_DBMANAGER'}">
														<a href="javascript:;" onClick="deleteGuideFile (this,'${result.file_seq}','${result.file_nm}','${auth_cd}','${reg_user_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
													</c:when>
													<c:when test="${reg_user_id eq user_id}">
														<a href="javascript:;" onClick="deleteGuideFile (this,'${result.file_seq}','${result.file_nm}','${auth_cd}','${reg_user_id}','${user_id}');"><i class="btnIcon fas fa-trash-alt"></i></a>
													</c:when>
												</c:choose>
											</span>
										</c:if>
									</c:forEach>
								</td>
							</tr>
						</table>
					</c:otherwise>
				</c:choose>
				<div class="dtlBtn">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
					<sec:authorize access="hasAnyRole('ROLE_DBMANAGER')">
						<c:if test="${guide.guide_div_cd eq '1'}">
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnUpdate();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>
						</c:if>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnDelete();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ROLE_TUNER')">
						<c:if test="${user_id eq reg_user_id}">
							<c:if test="${guide.guide_div_cd eq '1'}">
								<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnUpdate();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 수정</a>
							</c:if>
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnDelete();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
						</c:if>
					</sec:authorize>
				</div>
			</form:form>

			<iframe id="downloadFrame" style="display:none"></iframe>

			<form:form method="post" id="file_form" name="file_form" target="downloadFrame">
				<input type="hidden" id="guide_no" name="guide_no" value="${perfGuide.guide_no}"/>
				<input type="hidden" id="file_seq" name="file_seq" value=""/>
				<input type="hidden" id="use_seq" name="use_seq" value="${useSeq}"/>
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
			</form:form>

		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>