<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>사용자 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/userMng/users.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/usermng_excel_upload_popup.js?ver=<%=today%>"></script> <!-- 사용자관리 등록  팝업 -->
	<style>
	.panel-body .panel-body-noheader .panel-body-noborder {overflow:hidden;}
	.datagrid-cell {position: relative;}
	.lockIcon {
		width: 18px;
		height: 18px;
		position: absolute;
		top: 0;
		left :2px;
	}
	</style>
</head>
<body style="visibility:hidden;">
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
				
				<!-- 이전, 다음 처리 -->
				<input type="hidden" id="currentPage" name="currentPage" value="${users.currentPage}"/>
				<input type="hidden" id="pagePerCount" name="pagePerCount" value="${users.pagePerCount}"/>
				
				<label>검색 조건</label>
				<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
					<option value="">전체</option>
					<option value="01">사용자명</option>
					<option value="02">사용자ID</option>
				</select>
				<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
				<label>승인여부</label>
				<select id="search_approve_yn" name="search_approve_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
					<option value="">전체</option>
					<option value="Y">승인</option>
					<option value="N">미승인</option>
				</select>
				<label>사용여부</label>
				<select id="search_use_yn" name="search_use_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
					<option value="">전체</option>
					<option value="Y">사용</option>
					<option value="N">미사용</option>
				</select>
				<label>검색건수</label>
				<select id="searchCount" name="selectRcount" data-options="editable:true" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto'">
					<option value="10" selected>10</option>
					<option value="20" >20</option>
					<option value="40">40</option>
					<option value="60">60</option>
					<option value="80">80</option>
					<option value="100">100</option>
				</select>
				<span class="searchBtnLeft">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_UserRegist();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 사용자 등록</a>
					<a href="javascript:;" id="approveBtn" class="w80 easyui-linkbutton" onClick="Btn_ApproveUser();"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> 승인</a>
					<a href="javascript:;" id="unApproveBtn" class="w80 easyui-linkbutton" onClick="Btn_UnApproveUser();"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> 미승인</a>
				</span>
				<div class="searchBtn">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
				</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		
		<div id="userInfoTab" class="easyui-tabs" data-options="border:false" style="width:100%;height:320px;">
			<div title="기본정보관리">
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="idcheck" name="idcheck" value="N">
					<input type="hidden" id="is_new" name="is_new" value="Y">
					<input type="hidden" id="email" name="email">
					<input type="hidden" id="ext_no" name="ext_no">
					<input type="hidden" id="hp_no" name="hp_no">
					<input type="hidden" id="chk_user_id" name="chk_user_id">
					<input type="hidden" name="user_id">
					<input type="hidden" name="user_auth_id" value="${user_auth_id}">
					<input type="hidden" id="crud_flag" name="crud_flag" value="C">
					
					<div style="margin-top:10px;margin-left:10px;color:#ff0000;font-weight:bold;">
						※ 사용자ID는 최소 6자 ~ 최대 20자 이내이고 반드시 영문, 숫자 각 1자리 이상 포함되어야 합니다.
					</div>
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
							<th>사용자ID</th>
							<td colspan="5">
								<input type="text" id="user_id" class="w150 easyui-textbox" required="true"/>
								<a href="javascript:;" id="checkUserId" class="w100 easyui-linkbutton" onClick="Btn_checkID();"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> ID 중복확인</a>
								<a href="javascript:;" id="resetPwdBtn" class="w110 easyui-linkbutton" onClick="Btn_ResetPwd();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 비밀번호 초기화</a>
							</td>
						</tr>
						<tr>
							<th>사용자명</th>
							<td><input type="text" id="user_nm" name="user_nm" class="w150 easyui-textbox" required="true"/></td>
							<th>내선번호</th>
							<td>
								<select id="extNo1" name="extNo1" data-options="panelHeight:'auto'" class="w60 easyui-combobox">
									<option value="">선택</option>
									<option value="02">02</option>
									<option value="070">070</option>
									<option value="080">080</option>
									<option value="031">031</option>
									<option value="032">032</option>
									<option value="033">033</option>
									<option value="041">041</option>
									<option value="042">042</option>
									<option value="043">043</option>
									<option value="051">051</option>
									<option value="052">052</option>
									<option value="053">053</option>
									<option value="054">054</option>
									<option value="055">055</option>
									<option value="061">061</option>
									<option value="062">062</option>
									<option value="063">063</option>
									<option value="064">064</option>
								</select> - 
								<input type="text" id="extNo2" name="extNo2" class="w50 easyui-textbox"/> -
								<input type="text" id="extNo3" name="extNo3" class="w50 easyui-textbox"/>
							</td>
							<th>핸드폰번호</th>
							<td>
								<select id="hpNo1" name="hpNo1" data-options="panelHeight:'auto'" class="w60 easyui-combobox">
									<option value="">선택</option>
									<option value="010">010</option>
									<option value="011">011</option>
									<option value="013">013</option>
									<option value="016">016</option>
									<option value="017">017</option>
									<option value="018">018</option>
									<option value="019">019</option>
								</select> - 
								<input type="text" id="hpNo2" name="hpNo2" class="w50 easyui-textbox"/> -
								<input type="text" id="hpNo3" name="hpNo3" class="w50 easyui-textbox"/>
							</td>
						<tr>
							<th>이메일</th>
							<td>
								<input type="text" id="emailId" name="emailId" class="w100 easyui-textbox" /> @
								<input type="text" id="emailCp" name="emailCp" class="w80 easyui-textbox"/>
								<select id="selEmail" name="selEmail" data-options="panelHeight:'auto'" class="w80 easyui-combobox">
									<option value="">직접입력</option>
									<option value="naver.com">naver.com</option>
									<option value="daum.net">daum.net</option>
									<option value="hanmail.net">hanmail.net</option>
									<option value="nate.com">nate.com</option>
									<option value="gmail.com">gmail.com</option>
									<option value="paran.com">paran.com</option>
									<option value="chol.com">chol.com</option>
									<option value="dreamwiz.com">dreamwiz.com</option>
									<option value="empal.com">empal.com</option>
									<option value="freechal.com">freechal.com</option>
									<option value="hanafos.com">hanafos.com</option>
									<option value="hanmir.com">hanmir.com</option>
									<option value="hitel.net">hitel.net</option>
									<option value="hotmail.com">hotmail.com</option>
									<option value="korea.com">korea.com</option>
									<option value="lycos.co.kr">lycos.co.kr</option>
									<option value="netian.com">netian.com</option>
									<option value="yahoo.co.kr">yahoo.co.kr</option>
									<option value="yahoo.com">yahoo.com</option>
								</select>
							</td>
							<th>승인여부</th>
							<td>
								<select id="approve_yn" name="approve_yn" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
									<option value="Y" selected>승인</option>
									<option value="N">미승인</option>
								</select>
							</td>
							<th>사용여부</th>
							<td>
								<select id="use_yn" name="use_yn" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
									<option value="">선택</option>
									<option value="Y">사용</option>
									<option value="N">미사용</option>
								</select>
							</td>
						</tr>
						
					</table><br/>
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
							<th>기본 권한</th>
							<td>
								<input type="text" id="default_auth_grp_id_nm" name="default_auth_grp_id_nm" class="w150 easyui-textbox" disabled="true"/>
							</td>
							<th>기본 업무</th>
							<td colspan="3">
								<input type="text" id="default_wrkjob_cd_nm" name="default_wrkjob_cd_nm" class="w150 easyui-textbox" disabled="true"/>
							</td>
						</tr>
					</table>
					<div class="searchBtn innerBtn2">
						<a href="javascript:;" id="saveBtn" class="w80 easyui-linkbutton" onClick="Btn_SaveUser();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
						<a href="javascript:;" id="resetBtn" class="w90 easyui-linkbutton" onClick="Btn_ResetFieldAll();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
					</div>
				</form:form>
			</div>
			<div title="권한 관리">
				<div class="easyui-layout" data-options="border:false,fit:true">
					<div data-options="region:'center',border:false" style="padding-top:5px;">
						<table id="userAuthList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div data-options="region:'east',border:false" style="width:40%;padding:5px;">
						<form:form method="post" id="auth_form" name="auth_form" class="form-inline">
							<input type="hidden" id="auth_nm" name="auth_nm"/>
							<input type="hidden" id="user_id" name="user_id"/>
							<input type="hidden" id="crud_flag" name="crud_flag" />
							<input type="hidden" id="old_auth_grp_id" name="old_auth_grp_id" />
							<input type="hidden" id="old_default_auth_yn" name="old_default_auth_yn" />
							
							<input type="hidden" id="auth_start_day" name="auth_start_day"/>
							<input type="hidden" id="auth_end_day" name="auth_end_day"/>
							<input type="hidden" id="auth_comp_day" name="auth_comp_day"/>
							<table class="detailT" style="margin-left:0px;margin-top:0px;">
								<colgroup>	
									<col style="width:40%;">
									<col style="width:60%;">
								</colgroup>
								<tr>
									<th>권한명</th>
									<td><select id="auth_grp_id" name="auth_grp_id" data-options="panelHeight:'auto',editable:true" class="w200 easyui-combobox" required="required"></select></td>
								</tr>
								<tr>
									<th>권한시작일자</th>
									<td><input type="text" id="authStartDay" name="authStartDay" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
								<tr>
									<th>권한종료일자</th>
									<td><input type="text" id="authEndDay" name="authEndDay" value="9999-12-31" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
								<tr>
									<th>기본권한여부</th>
									<td>
										<select id="default_auth_yn" name="default_auth_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required>
										<option value=""></option>
										<option value="Y">Y</option>
										<option value="N">N</option>
										</select>
									</td>
								</tr>
							</table>
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserAuth();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteUserAuth();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
								<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField('auth');"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
							</div>
						</form:form>
					</div>
				</div>
			</div>
			<div title="업무 관리">
				<div class="easyui-layout" data-options="border:false,fit:true">
					<div data-options="region:'center',border:false" style="padding-top:5px">
						<table id="userWrkJobList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div data-options="region:'east',border:false" style="width:40%;padding:5px;">
						<form:form method="post" id="wrkjob_form" name="wrkjob_form" class="form-inline">
							<input type="hidden" id="user_id" name="user_id"/>
							<input type="hidden" id="user_nm" name="user_nm"/>  <!-- 추가 2018-10-17 -->
							<input type="hidden" id="workjob_start_day" name="workjob_start_day"/>
							<input type="hidden" id="workjob_end_day" name="workjob_end_day"/>
							<input type="hidden" id="work_comp_day" name="work_comp_day"/>
							<input type="hidden" id="leader_yn" name="leader_yn" value="N"/>
							<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
							<input type="hidden" id="wrkjob_nm" name="wrkjob_nm"/>
							<input type="hidden" id="old_wrkjob_cd" name="old_wrkjob_cd"/>
							<input type="hidden" id="old_default_wrkjob_yn" name="old_default_wrkjob_yn"/>
							
							<table class="detailT" style="margin-left:0px;margin-top:0px;">
								<colgroup>	
									<col style="width:40%;">
									<col style="width:60%;">
								</colgroup>
								<tr>
									<th>업무명</th>
									<td><select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w200 easyui-combotree" required="required"></select></td>
								</tr>
								<tr>
									<th>업무시작일자</th>
									<td><input type="text" id="workStartDay" name="workStartDay" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
								<tr>
									<th>업무종료일자</th>
									<td><input type="text" id="workEndDay" name="workEndDay" value="9999-12-31" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
								<tr>
									<th>기본업무여부</th>
									<td>
										<select id="default_wrkjob_yn" name="default_wrkjob_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required>
										<option value=""></option>
										<option value="Y">Y</option>
										<option value="N">N</option>
										</select>
									</td>
								</tr>
								<tr>
									<th>업무리더여부</th>
									<td><input type="checkbox" id="chkLeaderW" name="chkLeaderW" value="" class="w120 easyui-switchbutton"/></td>
								</tr>
							</table>
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserWrkJob();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteUserWrkJob();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
								<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField('wrkJob');"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
							</div>
						</form:form>
					</div>
				</div>
			</div>
			<div title="DB권한 관리">
				<div class="easyui-layout" data-options="border:false,fit:true">
					<div data-options="region:'center',border:false" style="padding-top:5px;">
						<table id="userDBAuthList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div data-options="region:'east',border:false" style="width:40%;padding:5px;">
						<form:form method="post" id="dbauth_form" name="dbauth_form" class="form-inline">
							<input type="hidden" id="user_id" name="user_id"/>
							<input type="hidden" id="db_name" name="db_name"/>
							<input type="hidden" id="privilege_start_day" name="privilege_start_day"/>
							<input type="hidden" id="privilege_end_day" name="privilege_end_day"/>
							<input type="hidden" id="old_dbid" name="old_dbid"/>
							<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
							
							<table class="detailT" style="margin-left:0px;margin-top:0px;">
								<colgroup>
									<col style="width:40%;">
									<col style="width:60%;">
								</colgroup>
								<tr>
									<th>DB명</th>
									<td><select id="dbid" name="dbid" data-options="panelHeight:'200px',editable:true" class="w200 easyui-combobox" required></select></td>
								</tr>
								<tr>
									<th>업무시작일자</th>
									<td><input type="text" id="privilegeStartDay" name="privilegeStartDay" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
								<tr>
									<th>업무종료일자</th>
									<td><input type="text" id="privilegeEndDay" name="privilegeEndDay" value="9999-12-31" class="w200 datapicker easyui-datebox" required/></td>
								</tr>
							</table>
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserDBAuth();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteUserDBAuth();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
								<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField('dbauth');"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container START -->
<%@include file="/WEB-INF/jsp/include/popup/usermng_excel_upload_popup.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>