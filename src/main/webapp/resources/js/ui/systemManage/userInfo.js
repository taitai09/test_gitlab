//var chkLeader = "N";  //wrkjob_cd 변경시 chkLeader를 서버단에서 체크하기 위함 20181018 수정
var oriPwd;
var oriUid;
var chgPwd;
$(document).ready(function() {

	var extNo2 = $('#extNo2');
	var extNo3 = $('#extNo3');
	var hpNo2 = $('#hpNo2');
	var hpNo3 = $('#hpNo3');

	//#detail_form
	$('#detail_form #password1_combo').textbox({
		onChange:function(rec){
			//alert("여기 몇번째로 들어오나");
			if(rec.password != ''){
				checkPassword_detail_form();
			}else{
				$("#detail_form #informPasswordCorrect1").text("");
			}
		}
	});	
	//#password_form
	$('#password_form #password2_combo').textbox({
		onChange:function(rec){
			if(rec.password != ''){
				checkPassword_password_form(rec.password);
			}else{
				$("#password_form #informPasswordCorrect2").text("");
			}
		}
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
	
	// 권한 리스트 조회	
	$('#auth_id').combobox({
	    url:"/UserInfo/DefaultAuth",
	    method:"get",
		valueField:'auth_grp_id',
	    textField:'auth_nm',
	    onLoadSuccess:function(obj){
	    	if(obj != undefined && obj != 'undefined' && obj != ''){
		    	var rows = obj[0]
		    	if(rows.default_auth_grp_id != null && rows.default_auth_grp_id != ''){
		    		$('#auth_id').combobox("setValue",rows.default_auth_grp_id);
		    	}else{
		    		$('#auth_id').combobox("setValue",rows.auth_grp_id);
		    	}
	    	}
	    }
	});

	// 업무 리스트 조회	
	$('#wrkjob_cd').combobox({
	    url:"/UserInfo/DefaultWrkjobCd",
	    method:'get',
    	valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
	    onLoadSuccess:function(obj){
	    	if(obj != undefined && obj != 'undefined' && obj != ''){
		    	var rows = obj[0]
		    	if(rows.default_wrkjob_cd != null && rows.default_wrkjob_cd != ''){
		    		$('#wrkjob_cd').combobox("setValue",rows.default_wrkjob_cd);
		    	}else{
		    		$('#wrkjob_cd').combobox("setValue",rows.wrkjob_cd);
		    	}
	    	}else{
	    		$('#wrkjob_cd').combobox("setText","(기본 업무가 없습니다.)");
	    	}
	    }
	});
	
});

//callback 함수
var callback_CheckWorkJobLeaderAction = function(result) {
	if(!result.result){
		$.messager.alert('',result.message + "<br> 업무리더를 원하시면 관리자에게 문의해주세요.",'info',function(){			
			$('#chkLeader').switchbutton({checked:false});
			$("#leader_yn").val("N");	
		});
	}
};

//현재 비밀번호일치여부 확인
function checkPassword_password_form(password){
	
	var rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	
	chgPwd = $("#password_form #password2_combo").textbox('getValue');
	
	// 로그인 정보 체크
	//alert(chgPwd);
	var rsaPwd = rsa.encrypt(chgPwd);
	$("#password_form #password2").val(rsaPwd);
	
	ajaxCall("/auth/UserPassword/Check",	$("#password_form"),"POST",	callback_CheckPasswordActionForPasswordForm);	
};
//현재 비밀번호일치여부 확인
function checkPassword_detail_form(){
	
	var rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	
	chgPwd = $("#detail_form #password1_combo").textbox('getValue');
	
	// 로그인 정보 체크
	//alert(chgPwd);
	var rsaPwd = rsa.encrypt(chgPwd);
	$("#detail_form #password1").val(rsaPwd);
	
	ajaxCall("/auth/UserPassword/Check",	$("#detail_form"),"POST",	callback_CheckPasswordActionForDetailForm);
	
};


//callback 함수
var callback_CheckPasswordActionForDetailForm = function(result) {
	if(result.result){
		$("#detail_form #check_password_yn1").val("Y");
		$("#detail_form #informPasswordCorrect1").attr("style","margin-left:6px;color:#767676;font-size:10px;font-weight:bold;");
		$("#detail_form #informPasswordCorrect1").text("비밀번호가 일치합니다.");
		
	}else{
		$("#detail_form #informPasswordCorrect1").attr("style","margin-left:6px;color:#ff0000;font-size:10px;font-weight:bold;");
		$("#detail_form #informPasswordCorrect1").text("비밀번호 불일치");
		$("#detail_form #check_password_yn1").val("N");	
	}
};

var callback_CheckPasswordActionForPasswordForm = function(result) {
	if(result.result){
		$("#password_form #check_password_yn2").val("Y");
		$("#password_form #informPasswordCorrect2").attr("style","margin-left:6px;color:#767676;font-size:10px;font-weight:bold;");
		$("#password_form #informPasswordCorrect2").text("비밀번호가 일치합니다.");
		
	}else{
		$("#password_form #informPasswordCorrect2").attr("style","margin-left:6px;color:#ff0000;font-size:10px;font-weight:bold;");
		$("#password_form #informPasswordCorrect2").text("비밀번호 불일치");
		$("#password_form #check_password_yn2").val("N");	
	}
};



//callback 함수
function Btn_checkID(){
	if($("#user_id").textbox("getValue") == ""){
		parent.$.messager.alert('','사용자 ID를 입력해 주세요.');
		return false;
	}
	
	ajaxCall("/Users/CheckUserId",
			$("#detail_form"),
			"POST",
			callback_CheckUserIdAction);	
}

//callback 함수
var callback_CheckUserIdAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','사용 가능한  ID입니다.','info',function(){
			$("#idcheck").val("Y");	
		});		
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			$("#user_id").textbox("setValue","");	
		});		
	}
};


function Btn_SaveUserInfo(){
	
	
	if($("#user_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','사용자명을 입력해 주세요.');
		return false;
	}
	if($("#user_nm").textbox('getValue').length > 16){
		parent.$.messager.alert('','사용자명을 16자 이내로 입력해 주세요.');
		return false;
	}
	
	if(($("#extNo1").combobox('getValue') != "" && $("#extNo2").textbox('getValue') == "" && $("#extNo3").textbox('getValue') == "") ||
		($("#extNo1").combobox('getValue') == "" && $("#extNo2").textbox('getValue') != "" && $("#extNo3").textbox('getValue') == "") ||
		($("#extNo1").combobox('getValue') == "" && $("#extNo2").textbox('getValue') == "" && $("#extNo3").textbox('getValue') != "")){
		parent.$.messager.alert('','내선번호를 입력해 주세요.');
		return false;
	}
	
	var extNo = $("#extNo1").combobox('getValue') + "-" + $("#extNo2").textbox('getValue') + "-" + $("#extNo3").textbox('getValue');
	
	if(!checkTelno(extNo)){
		parent.$.messager.alert('','내선 번호를 정확히 입력해 주세요.');
		return false;		
	}
	
	if(($("#hpNo1").combobox('getValue') != "" && $("#hpNo2").textbox('getValue') == "" && $("#hpNo3").textbox('getValue') == "") ||
		($("#hpNo1").combobox('getValue') == "" && $("#hpNo2").textbox('getValue') != "" && $("#hpNo3").textbox('getValue') == "") ||
		($("#hpNo1").combobox('getValue') == "" && $("#hpNo2").textbox('getValue') == "" && $("#hpNo3").textbox('getValue') != "")){
		parent.$.messager.alert('','핸드폰 번호를 입력해 주세요.');
		return false;
	}
	var hpNo = $("#hpNo1").combobox('getValue') + "-" + $("#hpNo2").textbox('getValue') + "-" + $("#hpNo3").textbox('getValue');
	
	if(!checkTelno(hpNo)){
		parent.$.messager.alert('','핸드폰 번호를 정확히 입력해 주세요.');
		return false;		
	}	
	
	if(($("#emailId").textbox('getValue') != "" && $("#emailCp").textbox('getValue') == "") ||
		($("#emailId").textbox('getValue') == "" && $("#emailCp").textbox('getValue') != "")){
		parent.$.messager.alert('','이메일 주소를 입력해 주세요.');
		return false;
	}
	
	var email = $("#emailId").textbox('getValue') + "@" + $("#emailCp").textbox('getValue');

	if(!checkEmail(email) || email.length > 30){
		parent.$.messager.alert('','이메일 주소를 정확히 입력해 주세요.');
		return false;
	}

	if($("#detail_form #check_password_yn1").val() == 'N'){
		parent.$.messager.alert('','현재 비밀번호를 정확히 입력해주세요');
		return false;
	}
	
	
	$("#email").val(email);
	$("#ext_no").val(extNo);
	$("#hp_no").val(hpNo);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCall("/UserInfo/Save",
			$("#detail_form"),
			"POST",
			callback_SaveUsersAction);		
};
//callback 함수
var callback_SaveUsersAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','사용자 정보가 변경 되었습니다.','info',function(){
			setTimeout(function() {			
				location.href = '/auth/UserInfo';
			},1000);
			
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_SaveUserAuthWrkjob(){

	//업무개발자 OR 업무개발_배포성능관리자
	if($("#get_auth_id").val() != "4" && $("#get_auth_id").val() != "6" ){  
		parent.$.messager.alert('','기본업무가 [ 업무개발자 ] 또는 [ 업무배포담당자 ] <br/>이외에는 변경이 불가능합니다.');
		return false;
	}
	
	
	if($("#auth_id").combobox("getValue") != "4" && $("#auth_id").combobox("getValue") != "6" ){  
		parent.$.messager.alert('','기본업무가 [ 업무개발자 ] 또는 [ 업무배포담당자 ] <br/>이외에는 변경이 불가능합니다.');
		return false;
	}
	
	
	if($("#wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
		ajaxCall("/UserWrkjob/Save",
				$("#auth_form"),
				"POST",
				callback_SaveUserAuthWrkjobAction);
};

//callback 함수
var callback_SaveUserAuthWrkjobAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','사용자 기본 권한/업무가 변경 되었습니다.','info',function(){
			setTimeout(function() {			
				location.href = '/auth/UserInfo';
			},1000);
			
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_ChangePassword(){
	if($("#user_id").textbox('getValue') == ""){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	if($("#password_form #check_password_yn2").val() == 'N'){
		parent.$.messager.alert('','현재 비밀번호를 정확히 입력해주세요');
		return false;
	}
	
	if($("#password_form #new_password").textbox("getValue") != $("#detail_form #check_password").textbox("getValue")){
		$.messager.alert('','비밀번호와 비밀번호 확인이 일치하지 않습니다.<br/>비밀번호를 확인해 주세요.');
		return false;
	}
	if($("#password_form #new_password").textbox("getValue").length < 8){
		$.messager.alert('','비밀번호는<br/>8 ~ 16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}	
	
	if(!checkPwd($("#password_form #check_password").textbox("getValue")) || $("#password_form #check_password").textbox("getValue").length > 16){
		$.messager.alert('','비밀번호는<br/>8 ~ 16자 영문, 숫자 1자 이상, 특수문자 1자 이상으로<br/>입력해 주세요.');
		return false;
	}	

	if($("#password_form #password2_combo").textbox("getValue") == $("#password_form #new_password").textbox("getValue")){
		$.messager.alert('','현재 비밀번호와 변경할 비밀번호를 다르게 입력해 주세요.');
		return false;
	}	

	$("#password_form #default_password_yn").val('N');
	
	
	var rsa = new RSAKey();
	rsa.setPublic($('#RSAModules').val(), $('#RSAExponent').val());
	
	
	//oriPwd = $("#login_form #userpassword").val();
	newPwd = $("#password_form #new_password").textbox('getValue');
	// 로그인 정보 체크

	//alert(chgPwd);
	var rsaNewPwd = rsa.encrypt(newPwd);
	//$("#password_form #password2").val(rsaPwd);

	/*$("#change_pwd_form #new_user_id").val(rsaUid);
	$("#change_pwd_form #new_password").val(rsaPwd);*/
	
	
	ajaxCall("/auth/UserPassword/Change?new_password="+rsaNewPwd,
			null,
			"POST",
			callback_ChangeUserPasswordAction);
	/*ajaxCall("/auth/UserPassword/Change",
			$("#password_form"),
			"POST",
			callback_ChangeUserPasswordAction);*/
}

//callback 함수
var callback_ChangeUserPasswordAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','사용자 비밀번호 변경이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				location.href = '/auth/UserInfo';
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
		$("#password_form #new_password").textbox("setValue","");
		$("#password_form #check_password").textbox("setValue","");
	}
};



function Btn_ResetField(){
	var default_password_yn = $("#detail_form #default_password_yn").val();
	
	//$("#detail_form #password").textbox("setValue", "");
	//$("#detail_form #new_password").textbox("setValue", "");
	//$("#detail_form #check_password").textbox("setValue", "");
	//$("#detail_form #check_password_yn").val("Y");
	//$("#detail_form #check_password_yn").val("");
	//$("#detail_form #default_password_yn").val(default_password_yn);
	$("#detail_form #email").val("");
	$("#detail_form #ext_no").val("");
	$("#detail_form #hp_no").val("");
	$("#detail_form #user_nm").textbox("setValue", "");
	$("#detail_form #extNo1").combobox("setValue", "02");
	$("#detail_form #extNo2").textbox("setValue", "");
	$("#detail_form #extNo3").textbox("setValue", "");
	$("#detail_form #hpNo1").combobox("setValue", "010");
	$("#detail_form #hpNo2").textbox("setValue", "");
	$("#detail_form #hpNo3").textbox("setValue", "");
	$("#detail_form #emailId").textbox("setValue", "");
	$("#detail_form #emailCp").textbox("setValue", "");
	$("#detail_form #selEmail").combobox("setValue", "");
		
	
}


