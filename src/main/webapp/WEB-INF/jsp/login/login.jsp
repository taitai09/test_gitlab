<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.08.10	이원식	최초작성
 **********************************************************/
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	String today = formatter.format(new java.util.Date());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: Open-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
	<link rel="stylesheet" href="/resources/css/common.css" />
	<link rel="stylesheet" href="/resources/css/login.css" />
	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
	<link rel="stylesheet" href="/resources/css/lib/easyui/icon.css" />	
	<link rel="stylesheet" href="/resources/css/reset.css" />
	<link rel="stylesheet" href="/resources/css/customer/<spring:eval expression="@defaultConfig['customer']"/>_style.css?ver=<%=today%>" />
	
	<script type="text/javascript" src="/resources/js/lib/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery.form.js"></script>
	<script src="/resources/js/common.js"></script>	
	<script src="/resources/js/ui/login.js?ver=<%=today%>"></script>
	
	<!-- RSA 필수사항 자바라이브러리 -->
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<!-- END RSA 필수사항 자바라이브러리 -->

	<script>
		var deploy_id="${deploy_id}";
		var customer='${customer}';
		var sso_yn='${sso_yn}';
		var user_id='${user_id}';
		var isConnectedBySSO='${isConnectedBySSO}';
		var error='${error}';

 		$(document).ready(function() {
			var iframeCnt = $(window.parent.document).find('iframe').length;
			if(iframeCnt > 0){
				window.parent.document.location.href = "/auth/login";
			}else{
				<c:if test="${not empty error}">
					<c:choose>
						<c:when test="${error eq 'chgPwd'}">
							$('#changePwdPop').window("open");
						</c:when>
						<c:when test="${error eq 'access'}">
						//$.messager.alert('','관리자가 승인 처리중입니다.<br/>관리자의 승인 후 로그인해 주세요.','info');
						sessionOut();
						</c:when>
					</c:choose>
				</c:if>
			}
		});
	</script>
</head>
<body style="visibility:hidden;">
	<c:choose>
		<c:when test="${customer eq 'kbcd' and sso_yn eq 'Y'}">
			<c:set var="loginPageViewOption" scope="page" value="style='display:none;'"/>
		</c:when>
		<c:otherwise>
			<c:set var="loginPageViewOption" scope="page" value=""/>
		</c:otherwise>
	</c:choose>
	
	<div id="wrapper" ${loginPageViewOption}>
		<div class="contents">
			<div id="login_contents">
				<h1 class="logo"><img src="/resources/images/<spring:eval expression="@defaultConfig['customer']"/>_login_logo.png" alt="Open-POP"></h1>
				<div class="body">    
					<form:form method="post" name="login_form" id="login_form" class="login_iuput">
						<input type="hidden" id="deploy_id" name="deploy_id" value="${deploy_id}" />
<!-- 						<input type="hidden" id="deploy_id" name="deploy_id" value="AB000001_11501" /> -->

						<input type="hidden" id="RSAModules" value="${RSAModulus}"/>
						<input type="hidden" id="RSAExponent" value="${RSAExponent}"/> 
						<input type="hidden" id="defaultLoginRole" value="N"/>
						<input type="hidden" id="result" name="result" value="${result}"/>
						<input type="hidden" id="resultMessage" name="resultMessage" value="${resultMessage}"/>
						<input type="hidden" id="resultStatus" name="resultStatus" value="${resultStatus}"/>

						<input type="hidden" id="userpwd" name="userpwd" />
						<input type="hidden" id="default_auth_grp_cd" name="default_auth_grp_cd" />
						<input type="hidden" id="default_auth_grp_id" name="default_auth_grp_id" />
						<input type="hidden" id="default_wrkjob_cd" name="default_wrkjob_cd" />
						<input type="hidden" id="user_id" name="user_id" />
						
						<input type="hidden" name="_csrf" value="${CSRF_TOKEN}" />
						
						<input type="text" id="userid" name="userid" class="ins contrast" placeholder="아이디를 입력하세요">
						<input type="password" id="userpassword" name="userpassword" class="ins contrast" placeholder="비밀번호를 입력하세요" value="<spring:eval expression="@serverConfig['userpassword']"/>">
						<div id="login_button">
							<a href="javascript:;" id="login_submit">
								<c:if test="${customer ne 'kbcd'}">
									<img src="/resources/images/login.png"/>
								</c:if>	
							</a>
						</div>
						<div id="bottom">
							<div id="newUser" onClick="showNewUserPopup();">신규 사용자 등록</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- 기본권한선택팝업 -->
	<%@include file="/WEB-INF/jsp/login/defaultLoginRolePop.jsp"%>
	<!-- 기본권한선택팝업 -->
		<div id="changePwdPop" class="easyui-window" style="background-color:#ffffff;width:400px;height:230px;">
			<div class="easyui-layout" data-options="fit:true">
				<input type="hidden" name="tempId" id="tempId" value="${param.tempId}">
				<form:form method="post" name="change_pwd_form" id="change_pwd_form">
					<!-- <input type="hidden" id="new_password" name="new_password" />
					<input type="hidden" id="new_user_id" name="new_user_id" /> -->
				</form:form>
				<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
					<label>변경 비밀번호</label>
					<input type="text" id="change_pwd" name="change_pwd" data-options="showEye:false,checkInterval:1,lastDelay:1" class="w250 easyui-passwordbox"/><br/><br/>
					<label>비밀번호 확인</label>
					<input type="text" id="check_pwd" name="check_pwd" data-options="showEye:false,checkInterval:1,lastDelay:1" class="w250 easyui-passwordbox"/>
					<div style="margin-top:15px;color:#ff0000;font-weight:bold;">※ 비밀번호는 8 ~ 16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로 입력해 주세요.</div>
				</div>
				<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
					<div class="searchBtn" style="padding-right:35px;">
						<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-ok'" onClick="goChangePwd();">변경</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-ok'" onClick="closeThisPop();">취소</a>
					</div>
				</div>
			</div>
		</div>
		<div id="newUserPop" class="easyui-window" style="background-color:#ffffff;width:720px;height:440px;">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
					<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
						<input type="hidden" id="idcheck" name="idcheck" value="N">
						<input type="hidden" id="wrkjob_nm" name="wrkjob_nm" value="">
						<input type="hidden" id="leader_yn" name="leader_yn" value="N">
						<input type="hidden" id="email" name="email">
						<input type="hidden" id="ext_no" name="ext_no">
						<input type="hidden" id="hp_no" name="hp_no">
						
						<input type="hidden" name="_csrf" value="${CSRF_TOKEN}" />
						
						<span class="h3">▶ 기본 정보</span>
						<table class="loginT">
							<colgroup>
								<col style="width:15%;">
								<col style="width:40%;">
								<col style="width:13%;">
								<col style="width:33%;">
							</colgroup>
							<tr>
								<th>사용자 ID</th>
								<td>
									<input type="text" id="user_id" name="user_id" class="w150 easyui-textbox" required="true"/>
									<a href="javascript:;" class="w80 easyui-linkbutton" data-options="iconCls:'icon-ok'" onClick="Btn_checkID();">중복체크</a>
								</td>
								<th>사용자명</th>
								<td><input type="text" id="user_nm" name="user_nm" class="w150 easyui-textbox" required="true" autocomplete="off"/></td>
							</tr>
							<tr>
								<th>비밀번호</th>
	<!-- 							<td><input type="text" id="password" name="password" data-options="showEye:false" class="w150 easyui-passwordbox" required="true"/></td> -->
								<td><input type="password" id="password" name="password" data-options="showEye:false" class="rcorners1" required="true" autocomplete="off"/></td>
								<th>비밀번호확인</th>
	<!-- 							<td><input type="text" id="check_password" name="check_password" data-options="showEye:false" class="w150 easyui-passwordbox" required="true"/></td> -->
								<td><input type="password" id="check_password" name="check_password" data-options="showEye:false" class="rcorners1" required="true"/></td>
							</tr>
							<tr>
								<th>이메일</th>
								<td colspan="3">
									<input type="text" id="emailId" name="emailId" class="w150 easyui-textbox"/> @
									<input type="text" id="emailCp" name="emailCp" class="w150 easyui-textbox"/>
									<select id="selEmail" name="selEmail" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
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
									<input type="text" id="extNo2" name="extNo2" class="w60 easyui-textbox"/> -
									<input type="text" id="extNo3" name="extNo3" class="w60 easyui-textbox"/>
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
									<input type="text" id="hpNo2" name="hpNo2" class="w60 easyui-textbox"/> -
									<input type="text" id="hpNo3" name="hpNo3" class="w60 easyui-textbox"/>
								</td>
							</tr>
						</table>
						<span class="h3">▶ 권한/업무 정보</span>
						<table class="loginT">
							<colgroup>	
								<col style="width:20%;">
								<col style="width:80%;">
							</colgroup>
							<tr>
								<th>권한</th>
								<td><select id="auth_cd" name="auth_cd" data-options="panelHeight:'auto'" class="w200 easyui-combobox" required="true"></select></td>
							</tr>
							<tr>
								<th>업무</th>
								<td>
									<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'200'" class="w200 easyui-combotree"></select>
									<label>업무리더 여부</label>
									<input type="checkbox" id="chkLeader" name="chkLeader" value="" class="w120 easyui-switchbutton"/>
								</td>
							</tr>
						</table>
						<div style="margin-top:15px;color:#ff0000;font-weight:bold;">
							※ 사용자ID와 비밀번호는 6 ~ 20자 이내로 입력해 주세요.<br/>
							※ 사용자ID는  영문, 숫자 1자 이상으로 입력해 주세요.<br/>
							※ 비밀번호는 영문, 숫자 1자 이상, 특수문자 1자 이상으로 입력해 주세요.
						</div>
					</form:form>
				</div>
				<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
					<div class="searchBtn" style="padding-right:35px;">
						<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-ok'" onClick="Btn_SaveNewUser();">등록</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-cancel'" onClick="Btn_NewUserPopupClose();">닫기</a>
					</div>
				</div>
			</div>
		</div>
</body>
</html>
