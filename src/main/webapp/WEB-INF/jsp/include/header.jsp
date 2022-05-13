<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- 상단 메뉴 START -->
<div id="header">
	<div class="top">
		<a class="brand-logo" href="/"><h1 >OPEN-POP</h1></a>
		<!-- 대메뉴  START -->
		<nav class="gnb">
			<ul>
				<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('000','My Menu');" class="dept1 main-menu active">My Menu</a></li>
				<c:forEach items="${menuList}" var="menu">
					<li class="fmenu"><a href="javascript:;" onClick="setLeftMenu('${menu.menu_id}','${menu.menu_nm}');" class="dept1 main-menu">${menu.menu_nm}</a></li>
				</c:forEach>
			</ul>
		</nav>
		<!-- 대메뉴  END -->
	
		<!-- 상단 Quick 메뉴 START -->
		<div id="my_menu" class="my-menu">
			<ul>
					<li class="favo"><a href="javascript:;" onClick="resetWorkplace();" id="resetBtn" class="menu-reset-workplace easyui-tooltip" title="Reset Workplace">Reset Workplace </a></li>
					<li class="favo"><a href="javascript:fnMyMenuSetting();" class="menu-setting-mymenu easyui-tooltip" title="My Menu 설정"> My Menu 설정 </a></li>
				<sec:authorize access="hasAnyRole('ROLE_DBMANAGER', 'ROLE_OPENPOPMANAGER','ROLE_DIAGNOSTIC')">
					<li class="favo"><a href="javascript:;" onClick="setLeftMenu('090','설정');"class="menu-setting easyui-tooltip" title="설정"> 설정 </a></li>
				</sec:authorize>
				<sec:authorize access="!hasAnyRole('ROLE_DIAGNOSTIC')">
					<li class="favo"><a href="javascript:Btn_goCommonLink('defaultLoginRole','')" id="resetBtn" class="menu-edit-qualify easyui-tooltip" title="로그인 권한 변경"> 로그인 권한 변경 </a></li>
				</sec:authorize>
					<li class="favo"><a href="javascript:Btn_goCommonLink('mypage','');" class="menu-edit-user-info easyui-tooltip" title="사용자 정보 변경 "> 사용자 정보 변경 </a></li>
					<li class="favo"><a href="javascript:Btn_goCommonLink('precedentNew','');" class="menu-help easyui-tooltip" title="SQL 튜닝 사례/가이드">SQL 튜닝 사례/가이드 </a></li>
					<li class="favo"><a href="javascript:;" onClick="newGuideOpen();" class="menu-guide easyui-tooltip" title="OPEN-POP USER GUIDE">OPEN-POP USER GUIDE</a></li>
					<!-- <li class="favo"><a href="javascript:;" onClick="openGuide();" class="menu-guide easyui-tooltip" title="OPEN-POP USER GUIDE">OPEN-POP USER GUIDE</a></li> -->
			</ul>
		</div>
		<!-- 상단 Quick 메뉴 END -->
	</div>
	
	<div id="guideModal" class="guideModal">
		<div class="guide-modal-content">
			
		</div>
		<div class="guideBtn">
			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="closeGuide();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
		</div>
	</div>
	
		<!-- TOP INFO START -->
	<div class="top_info">
		<!-- USER INFO START -->
		<div class="user_info">
			<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER', 'ROLE_TUNER')">
<!-- 					<dd class="intro" style="margin-right:2px;"><i class="far fa-address-card fa-2x fa-fw"></i></dd> -->
			</sec:authorize>
			<sec:authorize access="!hasAnyRole('ROLE_DEV')">
				<p class="intro">${users.user_nm} (${users.auth_nm}) <span>님 환영합니다.</span></p>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_DEV')">
				<p class="intro">${users.user_nm} (${users.wrkjob_nm})<c:if test="${users.leader_yn == 'Y'}">(리더)</c:if><span>님 환영합니다.</span></p>
			</sec:authorize>
			<a href="javascript:;" onClick="logout();" class="w60 easyui-linkbutton c5 button-logout" style="height:22px;"> 로그아웃</a> <!-- <i class="fas fa-sign-out-alt fa-lg fa-fw"></i> -->
		
<%-- 				<sec:authorize access="!hasAnyRole('ROLE_DIAGNOSTIC')"> 
					<dd class="W02" onClick="Btn_goImprovementStatus('ALL','2','Y');">튜닝요청 :<span id="requestSpan"></span>건</dd>
					<dd class="W02" onClick="Btn_goImprovementStatus('ALL','3','Y');">튜닝대기 :<span id="receiptSpan"></span>건</dd>
					<dd class="W01" onClick="Btn_goImprovementStatus('ALL','5','Y');">튜닝중 :<span id="tuningSpan"></span>건</dd>
					<dd class="W02" onClick="Btn_goImprovementStatus('ALL','6','Y');">적용대기 :<span id="applySpan"></span>건</dd>
				</sec:authorize> --%>
<!-- 				<dd class="W02" onClick="Btn_goImprovementStatus('ALL','4');">튜닝반려(제거예정) :<span id="cancelSpan"></span>건</dd> -->
		
		</div>
		<!-- USER INFO END -->
		<!-- TOP BUTTON START -->
		<div class="notice-list">
			<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER')">
				<a href="javascript: Btn_goCommonLink('dashboard','');" title="ToDo Dashboard" class="easyui-tooltip"><span class="notice todo">ToDo</span></a> &nbsp; 
			</sec:authorize>
			<a href="javascript:Btn_goCommonLink('notice','1');" title="공지사항" class="easyui-tooltip"><span class="notice-title">공지사항</span></a>
			<ul>
				<li>
					<a href="javascript:Btn_goNoticeLink('notice','1','${notice.board_no}');" class="notice"><marquee class="notice" scrollamount="2" width="160px" height="15px;" style="margin-top: 5px;" >${notice.title} ( ${notice.reg_dt} )</marquee></a>
				</li>
			</ul>
		</div>
	</div>
	<!-- TOP INFO END -->
</div>
<!-- 상단 메뉴 END -->
