<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.03	임호경	최초작성
  **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>사용자정보 변경</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/systemManage/userInfo.js?ver=<%=today%>"></script>
    
   	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:750px;">
			
					<div data-options="region:'west',border:false" style="padding:5px">
					<div class="dtl_title" style="margin-top:10px;"><span id="subTitle" class="h3" style="margin-left:0px;font-size:13px;">▶ 사용자정보 변경</span></div>
						<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							<div class="easyui-panel" data-options="border:false" style="height:600">

							<input type="hidden" id="RSAModules" value="${RSAModulus}"/>
							<input type="hidden" id="RSAExponent" value="${RSAExponent}"/> 


							<input type="hidden" id="email" name="email">
							<input type="hidden" id="ext_no" name="ext_no">
							<input type="hidden" id="hp_no" name="hp_no">
							<input type="hidden" id="get_wrkjob_cd" name="get_wrkjob_cd" value="${userInfo.wrkjob_cd}">
							<%-- <input type="hidden" id="get_auth_id" name="get_auth_id" value="${userInfo.auth_id}"> --%>
							<input type="hidden" id="get_auth_id" name="get_auth_id" value="${userInfo.default_auth_grp_id}">
							<input type="hidden" id="default_password_yn" name="default_password_yn" value="${userInfo.default_password_yn}">
							<input type="hidden" id="check_password_yn1" name="check_password_yn" value="N">
							<input type="hidden" id="wrkjob_nm" name="wrkjob_nm" value="">
							<input type="hidden" id="leader_yn" name="leader_yn" value="N">
						
								<table id="userInfoList" class="detailT click" style="margin-left:0px;margin-top:0px;">
									<colgroup>
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>
									
									<tr>
										<th>사용자 ID</th>
										<td>
											<input type="text" id="user_id" name="user_id" class="w150 easyui-textbox" value="${userInfo.user_id}" readonly="readonly"/>
										</td>
									</tr>
									<tr>
										<th>사용자명</th>
										<td><input type="text" id="user_nm" name="user_nm" class="w150 easyui-textbox" value="${userInfo.user_nm}"></td>
									</tr>
									<tr>
										<th>내선번호</th>
										<td>
											<select id="extNo1" name="extNo1" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox">
												<option value="${userInfo.extNo1}">${userInfo.extNo1}</option>
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
											<input type="text" id="extNo2" name="extNo2" class="w50 easyui-textbox" value="${userInfo.extNo2}" maxlength="4"/>  -
											<input type="text" id="extNo3" name="extNo3" class="w50 easyui-textbox" value="${userInfo.extNo3}" maxlength="4"/>
										</td>
									</tr>
									<tr>
										<th>핸드폰번호</th>
										<td>
											<select id="hpNo1" name="hpNo1" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox">
												<option value="${userInfo.hpNo1}">${userInfo.hpNo1}</option>
												<option value="010">010</option>
												<option value="011">011</option>
												<option value="013">013</option>
												<option value="016">016</option>
												<option value="017">017</option>
												<option value="018">018</option>
												<option value="019">019</option>
											</select> - 
											<input type="text" id="hpNo2" name="hpNo2" class="w50 easyui-textbox" value="${userInfo.hpNo2}" maxlength="4"/> -
											<input type="text" id="hpNo3" name="hpNo3" class="w50 easyui-textbox" value="${userInfo.hpNo3}"maxlength="4"/>
										</td>
									</tr>
									<tr>
										<th>이메일</th>
										<td>
											<input type="text" id="emailId" name="emailId" class="w150 easyui-textbox" value="${userInfo.emailId}"/> @
											<input type="text" id="emailCp" name="emailCp" class="w150 easyui-textbox" value="${userInfo.emailCp}"/>
											<select id="selEmail" name="selEmail" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
												<option value="${userInfo.emailCp}">${userInfo.emailCp}</option>
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
									</tr>
									<tr>
										<th>현재 비밀번호</th>
										<td>
											<input type="password" id="password1_combo" class="w150 easyui-textbox" />
											<input type="hidden" id="password1" name="password" />
											<span id="informPasswordCorrect1" style="margin-top:10px;margin-left:10px;color:#ff0000;"></span>
										</td>
									</tr>
								</table>
							</form:form>
								<div class="searchBtn innerBtn2 marginB40">
									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserInfo();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
									<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
								</div>
								
							<form:form method="post" id="auth_form" name="auth_form" class="form-inline">
								<div style="margin-top:60px;"></div>
								<div class="dtl_title" style="margin-top:20px;"><span id="subTitle" class="h3" style="margin-left:0px;font-size:13px;"> ▶ 기본 권한/업무 변경</span></div>
								<input type="hidden" id="user_id" name="user_id" value="${userInfo.user_id}"/>
								<table id="userInfoList_2" class="detailT click">
									<colgroup>	
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>
									<tr>
										<th>권한</th>
										<td><select id="auth_id" name="auth_id" data-options="panelHeight:'200',editable:false" class="w200 easyui-combobox"></select></td>
									</tr>
									<tr>
										<th>업무</th>
										<td>
											<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'200',editable:false" class="w200 easyui-combobox"></select>
<!-- 											<label>업무리더 여부</label> -->
<!-- 											<input type="checkbox" id="chkLeader" name="chkLeader" value="" class="w120 easyui-switchbutton"/> -->
										</td>
									</tr>
								</table>
							</form:form>
						

								<div class="searchBtn innerBtn2 marginB40">
									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserAuthWrkjob();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								</div>
								
								<div style="margin-top:60px;"></div>
								<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;font-size:13px;">▶ 비밀번호 변경</span></div>
							
							<form:form method="post" id="password_form" name="password_form" class="form-inline">
								<input type="hidden" id="user_id" name="user_id"  value="${userInfo.user_id}"/>
								<input type="hidden" id="check_password_yn2" name="check_password_yn" value="N">
								
								<table id="userInfoList_2" class="detailT click" style="margin-left:0px;margin-top:0px;">
									<colgroup>
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>
									
									<tr>
										<th>현재 비밀번호</th>
										<td>
											<input type="password" id="password2_combo" class="w150 easyui-textbox" />
											<input type="hidden" id="password2" name="password" />
											<span id="informPasswordCorrect2" style="margin-top:10px;margin-left:10px;color:#ff0000;"></span>
										</td>
															
									</tr>
									<tr>
										<th>변경할 비밀번호</th>
										<td><input type="password" id="new_password" name="new_password" class="w150 easyui-textbox"/></td>								
									</tr>
									<tr>
										<th>변경할 비밀번호 확인</th>
										<td><input type="password" id="check_password" name="check_password" class="w150 easyui-textbox"/></td>								
									</tr>
								</table>
							</form:form>
								
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ChangePassword();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 비밀번호 변경</a>
							</div>								
						</div>
						
						
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>