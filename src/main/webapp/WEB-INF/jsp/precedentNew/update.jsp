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
    <style>
	.textarea_size {
	    resize:none;
	    line-height:150%;
	    width:100%;
	    overflow-y:hidden;
	    height:30px;
	    border:1px solid #ccc;
	}    
	</style>
</head>
<body style="visibility:hidden;">
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
				<span style="float:right;position:relative;top:-4px;width:6.2%;" class="h3">
						<a style="font-weight:normal;" href="javascript:;" class="w100 easyui-linkbutton" style="font-family:none;" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
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
						<div title="SQL 요청 상세">
							<div style="height:645px;overflow-y:auto">
								
								<table class="detailT">
									<colgroup>
										<col style="width:6%;">
										<col style="width:44%;">
										<col style="width:6%;">
										<col style="width:44%;">
									</colgroup>
										<c:if test="${sqlDetail.wrkjob_peculiar_point != null and sqlDetail.request_why != null}">
											<tr>
												<th>업무특이사항</th>
													<td><textarea class="textarea_size" name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:50px;" readonly>${sqlDetail.wrkjob_peculiar_point}</textarea></td>
												<th>요청사유</th>
													<td><textarea class="textarea_size" name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.request_why}</textarea></td>
											</tr>
										</c:if>
										<tr>
											<th>문제점</th>
											<td><textarea class="textarea_size" name="controversialist" id="controversialist" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.controversialist}</textarea></td>
											<th>개선내역</th>
											<td><textarea class="textarea_size" name="impr_sbst" id="impr_sbst" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.impr_sbst}</textarea></td>
										</tr>
										
										<tr>
											<th>
												개선전 SQL<br/><br/>
												<!-- <a href="javascript:;" id="sqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sql_text_h" data-options="iconCls:'icon-save'">SQL 복사</a> -->
												<a href="javascript:;" id="sqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea" data-options="iconCls:'icon-save'">복사</a>
											</th>
											<td style ="padding:10px;height:300px;">
											
												<%-- <textarea name="sql_text" id="sql_text" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
													${sqlDetail.sql_text}
												</textarea>
													${sqlDetail.sql_text}
												</textarea> --%>
												<div id="sqlTextArea" style="width:100%;height:100%;overflow-y:auto;">
													${fn:replace(fn:replace(sqlDetail.sql_text,newLineChar,'<br/>'),' ','&nbsp;')}
												</div>
											</td>
											<th>개선후 SQL<br/><br/>
												<a href="javascript:;" id="imprSqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text_h" data-options="iconCls:'icon-save'">복사</a>
												<!-- <a href="javascript:;" id="imprSqlCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text" data-options="iconCls:'icon-save'">SQL 복사</a> -->
											</th>
											<td style ="padding:10px;height:300px;">
												<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
													${sqlDetail.impr_sql_text}
												</textarea>
												<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="display:none;">
													${sqlDetail.impr_sql_text}
												</textarea>
												<%-- <div id="impr_sql_text" style="width:100%;height:100%;overflow-y:auto;">
													${fn:replace(fn:replace(sqlDetail.impr_sql_text,newLineChar,'<br/>'),' ','&nbsp;')}
												</div> --%>
											</td>
										</tr>
										<tr>
											<th>
												개선전 실행계획 <br/><br/>
												<a href="javascript:;" id="imprbExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan_h" data-options="iconCls:'icon-save'">복사</a>
												<!-- <a href="javascript:;" id="imprbExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan" data-options="iconCls:'icon-save'">SQL 복사</a> -->
											</th>
											<td style ="padding:10px;height:300px;">
												<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
													${sqlDetail.imprb_exec_plan}
												</textarea>
												<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="display:none;">
													${sqlDetail.imprb_exec_plan}
												</textarea>
												<%-- <div id="imprb_exec_plan" style="width:100%;height:100%;overflow-y:auto;">
													${fn:replace(fn:replace(sqlDetail.imprb_exec_plan,newLineChar,'<br/>'),' ','&nbsp;')}
												</div> --%>
											</td>
											<th>개선후 실행계획 <br/><br/>
												<a href="javascript:;" id="impraExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan_h" data-options="iconCls:'icon-save'">복사</a>
												<!-- <a href="javascript:;" id="impraExecCopyBtn" class="easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan" data-options="iconCls:'icon-save'">SQL 복사</a> -->
											</th>
											<td style ="padding:10px;height:300px;">
												<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
													${sqlDetail.impra_exec_plan}
												</textarea>
												<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="display:none;">
													${sqlDetail.impra_exec_plan}
												</textarea>
												
												<%-- <div id="impra_exec_plan" style="width:100%;height:100%;overflow-y:auto;">
													${fn:replace(fn:replace(sqlDetail.impra_exec_plan,newLineChar,'<br/>'),' ','&nbsp;')}
												</div> --%>
											</td>
										</tr>
										<tr id="indexTable">
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
								</table>
							</div>
						</div>
					</c:when>
					
					
					
					
					
					
					
					<c:otherwise>
						<table class="detailT">
							<colgroup>
								<col style="width:5%;">
								<col style="width:95%;">
							</colgroup>
							<tr>
								<th>제목</th>
								<td>
									<input type="text" id="guide_title_nm" name="guide_title_nm" value="${guide.guide_title_nm}" class="easyui-textbox" style="width:1000px"/>
									<span style="padding-left:10px;padding-right:5px;">상단공지</span>
									<input type="checkbox" id="chkTopFixYn" name="chkTopFixYn" class="w120 easyui-switchbutton"/>
								</td>
							</tr>
							<tr height="520px;">
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