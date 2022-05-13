var WindowHeight;
var WindowWidth;

$(window).resize(function(){
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
	
	setLoginTop();
	
});

$(document).ready(function() {
	
	//고객사와 SSO연동시 호출됨
	if(customer != null && customer != '' && customer == 'kbcd'){
		if(sso_yn != null && sso_yn != ''  && sso_yn == 'Y' && isConnectedBySSO == 'Y' && error != 'chgPwd'){
			$("#login_form #userid").val(user_id);  //login.jsp 화면에 보여지는 값 세팅
			$("#login_form #userpassword").val("");  //login.jsp 화면에 보여지는 값 세팅
			Btn_LoginClick_KB(user_id);  //SSO연동 성공시 로그인 프로세스
		}
		if(sso_yn != null && sso_yn != ''  && sso_yn == 'Y' && isConnectedBySSO != 'Y' && error != 'chgPwd'){
			location.href="/sso/business";
		}
	}
	
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();

	setLoginTop();
	
	var Top1 = (WindowHeight - 230) / 2;
	var Left1 = (WindowWidth - 400) / 2; 
	if (Top1 < 0) Top1 = 0;
	if (Left1 < 0) Left1 = 0;	
	
	var Top2 = (WindowHeight - 420) / 2;
	var Left2 = (WindowWidth - 670) / 2; 
	if (Top2 < 0) Top2 = 0;
	if (Left2 < 0) Left2 = 0;		
	
	$('#selectDefaultWrkjobCd').combobox('setText', "업무선택");
	$('#selectRolePop').hide();
		
});

function closeThisPop(){
	$('#changePwdPop').window("close");
	ajaxCall("/auth/logout",$("#submit_form"),"POST",null);
}


function setLoginTop(){
	var Top = (WindowHeight - 388) / 2;
	if (Top < 0) Top = 0;

	$("#wrapper .contents").css("margin-top",Top+"px");
}

//KB국민카드 SSO연동
function Btn_LoginClick_KB(user_id){
	param_user_id = user_id;  //defaultLoginRole 로직에 필요한 paramter
	$("#login_form #user_id").val(user_id);
	ajaxCall("/sso/simpleLoginCheckBySSO", $("#login_form"), "GET", callback_simpleLoginCheckAction); // 로그인 1단계
}

