<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!-- 상단 메뉴 START -->
<div id="header">
	<div class="top">
		<a href="/"><h1>OPEN-POP</h1></a>						
		<!-- 대메뉴  START -->
		<nav>
			<ul>
				<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
				<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('000','My Menu');" class="dept1">My Menu</a></li>
				<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
				<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER', 'ROLE_TUNER')">
					<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('010','장애 예방');" class="dept1">장애 예방</a></li>
					<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
					<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('020','성능 진단');" class="dept1">성능 진단</a></li>
					<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
					<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('030','성능 분석');" class="dept1">성능 분석</a></li>
					<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
				</sec:authorize>
				<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('040','성능 개선');" class="dept1">성능 개선</a></li>				
				<li class="fmenu_line"><div class="line_span">&nbsp;</div></li>
				<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER', 'ROLE_TUNER')">
					<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('050','성능 리포트');" class="dept1">성능 리포트</a></li>
				</sec:authorize>
			</ul>
		</nav>
		<!-- 대메뉴  END -->
		
		<!-- 상단 Quick 메뉴 START -->
		<div id="my_menu">
			<ul>
				<li class="favo"><a href="javascript:;" onClick="resetWorkplace();" id="resetBtn"><i class="fas fa-eraser fa-lg fa-fw"></i> Reset Workplace</a></li>
				<li class="favo"> | </li>
				<li class="favo"><a href="javascript:fnMyMenuSetting();"><i class="fas fa-book fa-lg fa-fw"></i> My Menu 설정</a></li>
				<sec:authorize access="hasRole('ROLE_DBMANAGER')">
					<li class="favo"> | </li>
					<li class="favo"><a href="javascript:;" onClick="setLeftMenu('090','설정');"><i class="fas fa-cogs fa-lg fa-fw"></i> 설정</a></li>
				</sec:authorize>
				<li class="favo"> | </li>
				<li class="favo"><a href="javascript:Btn_goCommonLink('mypage','');"><i class="fas fa-address-card"></i> 사용자 정보 변경</a></li>
			</ul>
		</div>
		<!-- 상단 Quick 메뉴 END -->
		
		<!-- TOP INFO START -->
		<div class="top_info">
			<!-- USER INFO START -->
			<dl>
				<dd class="user_info" style="margin-right:2px;"><i class="far fa-address-card fa-2x fa-fw"></i></dd>
				<dd class="user_info">${users.user_nm}<span>님</span>, ${users.wrkjob_nm}<c:if test="${users.leader_yn == 'Y'}">(리더)</c:if></dd>
				<dd class="W01" onClick="Btn_goImprovementStatus('ALL','2');">요청 :<span id="requestSpan"></span>건</dd>
				<dd class="W01" onClick="Btn_goImprovementStatus('ALL','3');">접수 :<span id="receiptSpan"></span>건</dd>
				<dd class="W01" onClick="Btn_goImprovementStatus('ALL','5');">튜닝중 :<span id="tuningSpan"></span>건</dd>
				<dd class="W02" onClick="Btn_goImprovementStatus('ALL','6');">적용대기 :<span id="applySpan"></span>건</dd>
				<dd class="W02" onClick="Btn_goImprovementStatus('ALL','7');">적용반려 :<span id="cancelSpan"></span>건</dd>
			</dl>		
			<!-- USER INFO END -->
			<!-- TOP BUTTON START -->
			<div class="top_btn">
				<ul>
					<li><a href="javascript:Btn_goCommonLink('notice','1');" title="공지사항" class="easyui-tooltip"><i class="fas fa-bullhorn fa-2x fa-fw"></i></a></li>
					<!-- <li><a href="javascript:Btn_goCommonLink('schedule','');" title="일정관리" class="easyui-tooltip"><i class="fas fa-calendar-check fa-2x fa-fw"></i></a></li> -->
					<li><a href="javascript:;" onClick="logout();" class="w90 easyui-linkbutton c5" style="width:70px;margin-top:-7px;"><i class="fas fa-sign-out-alt fa-lg fa-fw"></i> 로그아웃</a></li>
				</ul>
			</div>
			<!-- TOP BUTTON END -->
			<!-- Used DEV -->
<%-- 			<form:form method="post" id="header_form" name="header_form" class="form-inline"> --%>
<!-- 				<input type="hidden" id="day_gubun" name="day_gubun"/> -->
<!-- 				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd"/>				 -->
<%-- 			</form:form> --%>
		</div>					
		<!-- TOP INFO END -->		
	</div>
</div>
<!-- 상단 메뉴 END -->