var WindowHeight;
var WindowWidth;
var oriPwd;
//var oriUid;
var chgPwd;
var rsa;
$(window).resize(function(){
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
	
	setLoginTop();
	
});

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	closeWindow();
	
	jQuery(document).keyup(function(e){
		if(e.keyCode==27){
			e.returnValue = false;
			e.keyCode = 0;
			
			closeWindow();
		}
	});
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

	var cookie_userid = getCookie("MYID");
	
	if(cookie_userid != "" && cookie_userid != null){
		$('#login_form #userid').val(cookie_userid);
		$('#login_form #userpassword').focus();
	}else{
		$('#login_form #userid').focus();
	}			
	
	$("#login_form #userid").bind("keyup", function(e){
		if(e.keyCode==13) {
			Btn_LoginClick();
		}
		return false;
	});    	
	
	$("#login_form #userpassword").bind("keyup", function(e){
		if(e.keyCode==13) {
			Btn_LoginClick();
		}
		return false;
	});
	
	$('#changePwdPop').window({
		title : "비밀번호 변경",
		top:Top1,
		left:Left1,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closable:false,
		constrain:true,
		zIndex:9999
	});
	
	$('#newUserPop').window({
		title : "신규 사용자  등록",
		top:Top2,
		left:Left2,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		constrain:true,
		zIndex:9999
	});	
	
	$('#selEmail').combobox({
	    onSelect:function(rec){
	    	if(rec.value == ""){
	    		$("#emailCp").textbox("setValue","");
	    		$("#emailCp").textbox("readonly",false);
	    	}else{
	    		$("#emailCp").textbox("setValue",rec.value);
	    		$("#emailCp").textbox("readonly",true);
	    	}
	    }
	});	
	
	// 권한 리스트 조회	
	$('#auth_cd').combobox({
	    url:"/auth/getAuth",
	    method:"get",
		valueField:'auth_id',
	    textField:'auth_nm',
	    onSelect:function(rec){
	    	if(rec.auth_id == "4"){
	    		$("#wrkjob_cd").combotree({disabled:false});
	    		$("#chkLeader").switchbutton({disabled:false});
	    	}else{
	    		$("#wrkjob_cd").combotree({disabled:true});
	    		$("#chkLeader").switchbutton({disabled:true});
	    	}
	    }
	});
	
	// 업무 리스트 조회	
	$('#wrkjob_cd').combotree({
	    url:"/auth/getWrkJobCd",
	    method:'get',
	    onChange: function(){
			$('#chkLeader').switchbutton({checked:false});
			$("#leader_yn").val("N");	
	    }
	}); 	
	
	$("#wrkjob_cd").combotree({disabled:true});
	
	$('#chkLeader').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if($("#wrkjob_cd").combotree('getValue') != ''){
				$("#wrkjob_nm").val($("#wrkjob_cd").combotree('getText'));
				if(checked){
					$("#leader_yn").val("Y");
					// 다른 리더의 존재여부 체크
					ajaxCall("/auth/CheckWorkJobLeader2",
							$("#detail_form"),
							"POST",
							callback_CheckWorkJobLeaderAction);
				}else{
					$("#leader_yn").val("N");
				}
			}else{
				$.messager.alert('','업무를 먼저 선택해 주세요.','info');
//				$(this).switchbutton('setValue','false');
//				$(this).switchbutton('uncheck');
			}
		}
	})
	
	$("#chkLeader").switchbutton({disabled:true});
	
	var extNo2 = $('#extNo2');
	var extNo3 = $('#extNo3');
	var hpNo2 = $('#hpNo2');
	var hpNo3 = $('#hpNo3');
	
	extNo2.textbox('textbox').bind('keydown', function(e){
		if ($(this).val().length > 3){
			extNo2.textbox('setValue', $(this).val().substring(0,3));
		}else{
			extNo2.textbox('setValue', $(this).val());
		}
	});	
	
	extNo3.textbox('textbox').bind('keydown', function(e){
		if ($(this).val().length > 3){
			extNo3.textbox('setValue', $(this).val().substring(0,3));
		}else{
			extNo3.textbox('setValue', $(this).val());
		}
	});		
	
	hpNo2.textbox('textbox').bind('keydown', function(e){
		if ($(this).val().length > 3){
			hpNo2.textbox('setValue', $(this).val().substring(0,3));
		}else{
			hpNo2.textbox('setValue', $(this).val());
		}
	});	
	
	hpNo3.textbox('textbox').bind('keydown', function(e){
		if ($(this).val().length > 3){
			hpNo3.textbox('setValue', $(this).val().substring(0,3));
		}else{
			hpNo3.textbox('setValue', $(this).val());
		}
	});	
	
	$('#selectDefaultWrkjobCd').combobox('setText', "업무선택");
	
	$('#changePwdPop').window("close");
	$('#newUserPop').window("close");
	$('#selectRolePop').hide();
		
	
	$("#login_form #login_submit").click(function(){
		Btn_LoginClick();
	});	
	
	
	$("#detail_form #user_id").textbox({
		onChange:function(newval, oldval){
			//텍스트박스 값 공백삭제
			newval = newval.replace(/ /gi, "");
			$(this).textbox("setValue",newval);
		}
	});
	
});

function closeWindow(){
	var message = $("#resultMessage").val();
	var result = $("#result").val();
//	alert(result+" , "+message);
	
	if (result == 'false' ){
		$.messager.alert('',message,'info',function() {
			var browser = navigator.userAgent;
			if ( browser.indexOf("Chrome") > 0 ) {
				window.open('checkPage','_self').close();
			} else {
				window.open('about:blank','_self').self.close();  // IE에서 묻지 않고 창 닫기
			}
		});
	}
}

function closeThisPop(){
	$('#changePwdPop').window("close");
	ajaxCall("/auth/logout",$("#submit_form"),"POST",null);
}

//callback 함수
var callback_CheckWorkJobLeaderAction = function(result) {
	
	if(!result.result){
		//유저 정보가 있는경우
		$.messager.alert('',result.message,'info',function(){
			$('#chkLeader').switchbutton({checked:false});
			$("#leader_yn").val("N");
		});
	}else{
		//유저 정보가 없는경우
		$('#chkLeader').switchbutton({checked:true});
		$("#leader_yn").val("Y");
	}
};


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


/*function userPasswordValidCheck(){
	ajaxCall("/auth/userPasswordValidCheck",$("#change_pwd_form"),"POST",callback_userPasswordValidCheckAction);
}

var callback_userPasswordValidCheckAction = function(result){
	if(result.result){
	}else{
		$.messager.alert('',result.message,'info');
		return false;
	}
}*/
function Btn_LoginClick() {
	if($("#login_form #userid").val() == ""){
		$.messager.alert('','아이디를 입력해 주세요.','info');
		$("#login_form #userid").focus();

		return false;
	}
	//Login버튼을 누르면 param_user_id 변수에 값을 세팅한다.
	//2019-03-08
	param_user_id = $("#login_form #userid").val();
	
	
	if($("#login_form #userpassword").val() ==""){
		$.messager.alert('','비밀번호를 입력해 주세요.','info');
		$("#login_form #userpassword").focus();

		return false;
	}
	if($("#login_form #userpassword").val().length > 20){
		$.messager.alert('','비밀번호의 길이가 너무 깁니다. <br/>20자 이내로 입력해 주세요.','info');
		$("#login_form #userpassword").val("");
		return false;
	}
	//######################### GS인증관련 추가사항 ######################### // 	
	rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	
	oriPwd = $("#login_form #userpassword").val();
	oriUid = $("#login_form #userid").val();
	// 로그인 정보 체크
	var rsaPwd = rsa.encrypt(oriPwd);
	var rsaUid = rsa.encrypt(oriUid);
	
	$("#login_form #userpassword").val(rsaPwd);
	$("#login_form #user_id").val(rsaUid);
	
	// 로그인 정보 체크
	if ( $("#result").val() == 'false') {
		closeWindow();
	} else {
		ajaxCall("/auth/simpleLoginCheck", $("#login_form"), "GET", callback_simpleLoginCheckAction); // 로그인 1단계
		$("#login_form #userpassword").val("");
	}
	
}

function saveLogin(){
	var strId = $("#login_form #userid").val();
	
	var expdate = new Date();
	if(strId != ""){
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30);		
		setCookie("MYID",strId,expdate);
	}else{
		deleteCookie("MYID");
	}
}

function goChangePwd() {
//	if($("#change_pwd").passwordbox("getValue") == ""){
//		$.messager.alert('','변경 비밀번호를 입력해 주세요.');
//		return false;
//	}
//	
//	if($("#check_pwd").passwordbox("getValue") == ""){
//		$.messager.alert('','비밀번호 확인을 입력해 주세요.');
//		return false;
//	}
//
//	if($("#change_pwd").passwordbox("getValue") != $("#check_pwd").passwordbox("getValue")){
//		$.messager.alert('','변경 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
//		return false;
//	}
//	
//	if(!checkPwd($("#check_pwd").passwordbox("getValue"))){
//		$.messager.alert('','비밀번호는<br/>6~16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
//		return false;
//	}
	if($("#change_pwd").val() == ""){
		$.messager.alert('','변경 비밀번호를 입력해 주세요.');
		return false;
	}
	
	if($("#check_pwd").val() == ""){
		$.messager.alert('','비밀번호 확인을 입력해 주세요.');
		return false;
	}

	if($("#change_pwd").val() != $("#check_pwd").val()){
		$.messager.alert('','변경 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
		return false;
	}
	
	if(!checkPwd($("#check_pwd").val())){
		$.messager.alert('','비밀번호는<br/>8~16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}
	
	if($("#change_pwd").val().length < 8 || $("#change_pwd").val().length > 16){
		$.messager.alert('','비밀번호는<br/>8~16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}	
	/*if($("#change_pwd").val() == $("#check_pwd").val()){
		$.messager.alert('','현재 비밀번호와 변경할 비밀번호를 다르게 입력해 주세요.');
		return false;
	}	*/
	
//	var param = "user_id="+$("#tempId").val()+"&new_password="+$("#change_pwd").passwordbox("getValue");
//	var param = "user_id="+$("#tempId").val()+"&new_password="+$("#change_pwd").val();
	
	
	//################### GS 인증으로 인한 암호화 ##################

	
	
	rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	
	
	//oriPwd = $("#login_form #userpassword").val();
	oriUid = $("#login_form #userid").val();
	chgPwd = $("#change_pwd").val();
	// 로그인 정보 체크
	//alert(oriUid);
	//alert(chgPwd);
	var rsaUid = rsa.encrypt(oriUid);
	var rsaPwd = rsa.encrypt(chgPwd);
	
	//$("#login_form #userpassword").val(rsaPwd);
	//$("#login_form #user_id").val(rsaUid);

	//$("#change_pwd_form #new_user_id").val(rsaUid);
	//$("#change_pwd_form #new_password").val(rsaPwd);
	
	
	//$("#change_pwd_form #new_user_id").val($("#tempId").val());
	//$("#change_pwd_form #new_password").val($("#change_pwd").val());
	
	ajaxCall("/auth/updateNewPwd?new_user_id="+rsaUid+"&new_password="+rsaPwd,
			null,
			"POST",
			callback_updateNewPwdAction);			
}

//callback 함수
var callback_updateNewPwdAction = function(result) {
	if(result.result){
		$.messager.alert('','비밀번호가 정상적으로 변경되었습니다.<br/>다시 로그인을 시도해주세요.','info',function(){
			setTimeout(function() {
				location.href="/auth/logout";
			},1000);
		});
	}
};	

function Btn_checkID(){
//	if(!checkPwd($("#check_pwd").val())){
//		$.messager.alert('','비밀번호는<br/>6~16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
//		return false;
//	}
	
	if($("#detail_form #user_id").textbox("getValue").length < 6 || $("#detail_form #user_id").textbox("getValue").length > 20){
		$.messager.alert('','사용자 ID를  6 ~ 20자 사이로 입력해 주세요.');
		return false;
	}
	if($("#detail_form #user_id").textbox("getValue") == ""){
		$.messager.alert('','사용자 ID를 입력해 주세요.');
		return false;
	}
	
	if(!checkHangle($("#detail_form #user_id").textbox('getValue'))){
		$.messager.alert('','한글은 입력하실 수 없습니다.');
		$("#detail_form #user_id").textbox("setValue","");
		return false;
	}
	
	if(!checkId($("#detail_form #user_id").textbox('getValue'))){
		$.messager.alert('','사용자 ID는 최소 6자 ~ 최대 20자 이내이고<br/>반드시 영문, 숫자 각 1자리 이상 포함되어야 합니다.');
		return false;
	}
	ajaxCall("/auth/CheckUserId",
			$("#detail_form"),
			"POST",
			callback_CheckUserIdAction);	
}

//callback 함수
var callback_CheckUserIdAction = function(result) {
	if(result.result){
		$.messager.alert('','사용 가능한  ID입니다.','info',function(){
			$("#idcheck").val("Y");	
		});		
	}else{
		$.messager.alert('',result.message,'error',function(){
			$("#detail_form #user_id").textbox("setValue","");	
		});		
	}
};

function Btn_SaveNewUser(){
	if($("#detail_form #idcheck").val() == "N"){
		$.messager.alert('','사용자 ID 중복체크를 진행해 주세요.');
		return false;		
	}
	
	if($("#detail_form #user_id").textbox('getValue') == ""){
		$.messager.alert('','사용자 ID를 입력해 주세요.');
		return false;
	}
	
	if(!checkId($("#detail_form #user_id").textbox('getValue'))){
		$.messager.alert('','사용자 ID는 최소 6자 ~ 최대 20자 이내이고<br/>반드시 영문, 숫자 각 1자리 이상 포함되어야 합니다.');
		return false;
	}
	
	if($("#detail_form #user_nm").textbox('getValue') == ""){
		$.messager.alert('','사용자명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #user_nm").textbox('getValue').length > 16){
		$.messager.alert('','사용자명을 16자 이내로 입력해 주세요.');
		return false;
	}
	
//	if($("#detail_form #password").passwordbox('getValue') == ""){
//		$.messager.alert('','비밀번호를 입력해 주세요.');
//		return false;
//	}
//	
//	if($("#detail_form #check_password").passwordbox('getValue') == ""){
//		$.messager.alert('','비밀번호 확인을 입력해 주세요.');
//		return false;
//	}
//
//	if($("#detail_form #password").passwordbox('getValue') != $("#detail_form #check_password").passwordbox('getValue')){
//		$.messager.alert('','비밀번호와 비밀번호 확인이 일치하지 않습니다.<br/>비밀번호를 확인해 주세요.');
//		return false;
//	}
//	
//	if(!checkPwd($("#detail_form #password").passwordbox('getValue'))){
//		$.messager.alert('','비밀번호는<br/>6 ~ 16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
//		return false;
//	}
	

	
	if($("#detail_form #password").val() == ""){
		$.messager.alert('','비밀번호를 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #check_password").val() == ""){
		$.messager.alert('','비밀번호 확인을 입력해 주세요.');
		return false;
	}

	if($("#detail_form #password").val() != $("#detail_form #check_password").val()){
		$.messager.alert('','비밀번호와 비밀번호 확인이 일치하지 않습니다.<br/>비밀번호를 확인해 주세요.');
		return false;
	}
	if($("#detail_form #password").val().length < 8 || $("#detail_form #password").val().length > 16){
		$.messager.alert('','비밀번호는<br/>8 ~ 18자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}	
	
	if(!checkPwd($("#detail_form #password").val())){
		$.messager.alert('','비밀번호는<br/>8 ~ 18자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}	
	
//	if(($("#detail_form #emailId").textbox('getValue') != "" && $("#detail_form #emailCp").textbox('getValue') == "") ||
//		($("#detail_form #emailId").textbox('getValue') == "" && $("#detail_form #emailCp").textbox('getValue') != "")){
//		$.messager.alert('','이메일 주소를 입력해 주세요.');
//		return false;
//	}
	
	var email = $("#detail_form #emailId").textbox('getValue') + "@" + $("#detail_form #emailCp").textbox('getValue');
	console.log("email:"+email);
	if(email != '@'){
		if(!checkEmail(email) || email.length > 30){
			$.messager.alert('','이메일 주소를 정확히 입력해 주세요.');
			return false;
		}
	}else{
		email = "";
	}
	
//	if(($("#detail_form #extNo1").combobox('getValue') != "" && $("#detail_form #extNo2").textbox('getValue') == "" && $("#detail_form #extNo3").textbox('getValue') == "") ||
//		($("#detail_form #extNo1").combobox('getValue') == "" && $("#detail_form #extNo2").textbox('getValue') != "" && $("#detail_form #extNo3").textbox('getValue') == "") ||
//		($("#detail_form #extNo1").combobox('getValue') == "" && $("#detail_form #extNo2").textbox('getValue') == "" && $("#detail_form #extNo3").textbox('getValue') != "")){
//		$.messager.alert('','내선번호를 입력해 주세요.');
//		return false;
//	}
	var extNo = $("#detail_form #extNo1").combobox('getValue') + "-" + $("#detail_form #extNo2").textbox('getValue') + "-" + $("#detail_form #extNo3").textbox('getValue');
	console.log("extNo:"+extNo);

	if(extNo != '--'){
		if(!checkTelno(extNo)){
			$.messager.alert('','내선 번호를 정확히 입력해 주세요.');
			return false;		
		}
	}else{
		extNo = "";
	}
	
//	if(($("#detail_form #hpNo1").combobox('getValue') != "" && $("#detail_form #hpNo2").textbox('getValue') == "" && $("#detail_form #hpNo3").textbox('getValue') == "") ||
//		($("#detail_form #hpNo1").combobox('getValue') == "" && $("#detail_form #hpNo2").textbox('getValue') != "" && $("#detail_form #hpNo3").textbox('getValue') == "") ||
//		($("#detail_form #hpNo1").combobox('getValue') == "" && $("#detail_form #hpNo2").textbox('getValue') == "" && $("#detail_form #hpNo3").textbox('getValue') != "")){
//		$.messager.alert('','핸드폰 번호를 입력해 주세요.');
//		return false;
//	}
		
	var hpNo = $("#detail_form #hpNo1").combobox('getValue') + "-" + $("#detail_form #hpNo2").textbox('getValue') + "-" + $("#detail_form #hpNo3").textbox('getValue');
	
	if(hpNo != '--'){
		if(!checkTelno(hpNo)){
			$.messager.alert('','핸드폰 번호를 정확히 입력해 주세요.');
			return false;		
		}		
	}else{
		hpNo = ""; 
	}
	
	if($("#detail_form #auth_cd").combobox('getValue') == ''){
		$.messager.alert('','권한을 선택해 주세요.');
		return false;
	}
	
	if($("#detail_form #auth_cd").combobox('getValue') == "4" && $("#detail_form #wrkjob_cd").combotree('getValue') == ""){
		$.messager.alert('','개발자는 업무를 선택하셔야 합니다.<br/>업무를 선택해 주세요.');
		return false;
	}	

	$("#detail_form #email").val(email);
	$("#detail_form #ext_no").val(extNo);
	$("#detail_form #hp_no").val(hpNo);	

	/*//패스워드 validation 체크
	if(!userPasswordValidCheck()){
		alert("여기 들어왔나?!");
		return false;
	};*/
	
	
	var rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	chgPwd = $("#detail_form #password").val();
	var rsaPwd = rsa.encrypt(chgPwd);
	//var rsaPwd = rsa.encrypt(chgPwd);

	
	$("#detail_form #password").val(rsaPwd); 
	$("#detail_form #check_password").val(rsaPwd);
	
	ajaxCall("/auth/SaveNewUser",
			$("#detail_form"),
			"POST",
			callback_SaveNewUserAction);
	
}

//callback 함수
var callback_SaveNewUserAction = function(result) {
	$("#detail_form #password").val("");
	$("#detail_form #check_password").val("");

	if(result.result){
		$.messager.alert('','신규 사용자 등록이 완료 되었습니다.<br/>관리자의 승인 후 로그인이 가능합니다.','info',function(){
			setTimeout(function() {		
				Btn_ResetField();
				Btn_NewUserPopupClose();
			},1000);
		});
	}else{
		$.messager.alert('',result.message,'error',function(){
			setTimeout(function() {
				$("#detail_form #password").val("");
				$("#detail_form #check_password").val("");
				//Btn_ResetField();
				//Btn_NewUserPopupClose();
			},1000);
		});
	}
};

function showNewUserPopup(){
	Btn_ResetField();
	$('#newUserPop').window("open");
}

function Btn_NewUserPopupClose(){
	$('#newUserPop').window("close");	
}

function Btn_ResetField(){
	
	$("#detail_form #email").val("");
	$("#detail_form #ext_no").val("");
	$("#detail_form #hp_no").val("");	
	$("#detail_form #user_id").textbox("setValue", "");
	$("#detail_form #user_nm").textbox("setValue", "");
//	$("#detail_form #password").passwordbox("setValue", "");
//	$("#detail_form #check_password").passwordbox("setValue", "");
	$("#detail_form #password").val("");
	$("#detail_form #check_password").val("");
	$("#detail_form #emailId").textbox("setValue", "");
	$("#detail_form #emailCp").textbox("setValue", "");
	$("#detail_form #selEmail").combobox("setValue", "");
	$("#detail_form #extNo1").combobox("setValue", "");
	$("#detail_form #extNo2").textbox("setValue", "");
	$("#detail_form #extNo3").textbox("setValue", "");
	$("#detail_form #hpNo1").combobox("setValue", "");
	$("#detail_form #hpNo2").textbox("setValue", "");		
	$("#detail_form #hpNo3").textbox("setValue", "");	
	$("#detail_form #auth_cd").combobox("setValue", "");	
	$("#detail_form #wrkjob_cd").combotree("setValue", "");
}


function sessionOut(){

	ajaxCall("/auth/SessionOut",
			$("#detail_form"),
			"POST",
			callback_SessionOutAction);	
}

//callback 함수
var callback_SessionOutAction = function(result) {
	$.messager.alert('','관리자가 승인 처리중입니다.<br/>관리자의 승인 후 로그인해 주세요.','info');
	if(result.result){
		console.log("RESULT : 로그인실패 / 로그저장");
		setTimeout('go_url()',2500);
		
	}else{
		console.log("RESULT : 로그인실패 / 로그저장실패");
		setTimeout('go_url()',2500);
	}
};

function go_url(){
	location.href="/auth/login";
}