var param_user_id;
var login_user_id;
var oriUid;
$(document).ready(function() {
	
	login_user_id = $("#login_user_id").val();
	
	if(login_user_id != null && login_user_id != ""){
		param_user_id = login_user_id;
		createAuthCombo();
	}else{
		$('#selectRolePop').hide();
	}
	
//	alert("시작");
//	loginProcess();
//	alert("끝");
});

function showSelectRolePop(){
	$('#selectRolePop').show();
//	$("#selectRolePop").css("display", "block");
}

function hideSelectRolePop(){
	$('#selectRolePop').hide();	
}

//로그인시 기본권한 콤보
function createAuthCombo(){
	// 권한명 조회			
	$('#selectDefaultAuthCombo').combobox({
	    url:"/auth/getUsersAuthList?user_id="+param_user_id,
	    method:"get",
		valueField:'auth_cd',
	    textField:'auth_nm',
		onLoadError: function(){
			parent.$.messager.alert('','권한명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items){
		},
	    onSelect:function(rec){
	    	
			$("#login_form #default_auth_grp_id").val(rec.auth_id);
			$("#login_form #default_auth_grp_cd").val(rec.auth_cd);

			//업무콤보박스 데이터 클리어
			$('#selectDefaultWrkjobCd').combobox('clear');
	    	
	    	if(rec.auth_id == 4){
	    		$("#selectDefaultWrkjobCdSpan").show();
		    	$('#selectDefaultWrkjobCd').combobox({
		    	    url:"/auth/getUsersWrkJobCdList?user_id="+param_user_id,
		    	    method:'get',
		        	valueField:'wrkjob_cd',
		    	    textField:'wrkjob_cd_nm',
		    	    onChange : function(rec){
		    	    },
					onLoadSuccess : function(items){
						if (items.length){
							var opts = $(this).combobox('options');
							$(this).combobox('select', items[0][opts.valueField]);
//							$(this).combobox('setText', "업무선택");
						}						
					},
					onSelect:function(rec){
						$("#login_form #default_wrkjob_cd").val(rec.wrkjob_cd);
					}
		    	});	    	
	    	}else{
	    		$("#selectDefaultWrkjobCdSpan").hide();
	    	}
	    }
	});	
}

//최초 로그인시 
//단일 권한
//업무개발자
//단일 업무만 있는 경우 : 팝업창 없이 대시보드로 진입
//다중 업무를 가진 경우 : 팝업창에서 업무선택 후 대시보드로 진입
//업무개발자 외 : 팝업창 없이 대시보드로 진입
//
//다중 권한
//공통 : 권한 선택 팝업창으로 진입 후 권한 선택
//업무개발자 권한 선택 
//단일 업무 : 대시보드로 진입
//다중 업무 : 콤보박스에서 업무 선택 한 후에 대시보드로 진입
//업무개발자 외 권한 선택 : 대시보드로 진입


//callback 함수
var callback_simpleLoginCheckAction = function(result) { //로그인 2단계 (유저 정보를 가지고옴)
	if(result.result){

		//스프링시큐리티로 인해 초기화;
		//alert("oriPwd 확인 :::"+oriPwd);
		//$("#login_form #userpassword").val(oriPwd);
		
		var default_auth_grp_cd = result.object.default_auth_grp_cd;
		var default_auth_grp_id = result.object.default_auth_grp_id;
		var default_wrkjob_cd = result.object.default_wrkjob_cd;
		
		$("#login_form #default_auth_grp_id").val(default_auth_grp_id);
		$("#login_form #default_auth_grp_cd").val(default_auth_grp_cd);
		$("#login_form #default_wrkjob_cd").val(default_wrkjob_cd);

		//기본권한코드가 없을때 기본값을 선택하도록 팝업창 띄움
		if(nvl(default_auth_grp_cd,"") == ""){
			ajaxCall("/auth/getUsersAuthListObject?user_id="+param_user_id, null, "GET", callback_getUsersAuthListObjectAction);
		}else{
			//기본권한코드가 저장되어 있을때 기본값을 선택하도록 팝업창 띄움
			ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
		}
	}else{
		/*parent.$.messager.confirm('',result.message, function(check) {
			if(check){
				location.href="/auth/logout";
			}
		});*/
		
		location.href="/auth/logout";
	}
};

//callback 함수
var callback_loginCheckAction = function(result) { //로그인 3단계 중 두번째: 기본권한코드가 저장되어 있을때 기본값을 선택하도록 팝업창 띄움

	if(result.result){
		if(result.object.auth_grp_id == 4 && result.object.wrkjob_cd == null){
			$.messager.alert('','업무코드가 등록되지 않았습니다.<br/>관리자에게 문의바랍니다.','info');
		}else{
			$("#userpwd").val(result.object.password); //스프링시큐리티 패스워드가 채워져있어야 로그인이 가능함. //DB에 저장된 패스워드랑 일치해야함.
			loginProcess();
		}
	}else{
		$.messager.alert('',result.message,'info');
	}
};
//로그인처리
function loginProcess(){
	//스프링 시큐리티로인한 아이디 초기화
//	if((oriUid == undefined || oriUid == null || oriUid == 'undefined') && sso_yn != undefined && sso_yn != 'Y'){
	if((oriUid == undefined || oriUid == null || oriUid == 'undefined')){
		//oriUid = $("#login_user_id").val();
		oriUid = param_user_id;
	}
	
	/*if(sso_yn == 'Y'){
		oriUid = param_user_id;
	}*/
	
	saveLogin(oriUid);
	$("#login_form #user_id").val(oriUid);
	//saveWatchLoginUsers(); //WatchLoginUsers
	//$("#login_form").attr("action", "/auth/process?user_id="+result.object.user_id+"&userpwd="+result.object.password);
	$("#login_form").attr("target","_parent");
//	$("#login_form").attr("action", "/auth/process?user_id=dbmanager&default_auth_grp_cd=ROLE_DBMANAGER&userpwd=95kxiCqBAXedsg/59oLRyN4nCt7IWplO");
	$("#login_form").attr("action", "/auth/process");
	$("#login_form").submit(); 
	
}

//getUsersAuthListObject
var callback_getUsersAuthListObjectAction = function(result){ //로그인 3단계 중 첫번째: 기본권한코드가 없을때 기본값을 선택하도록 팝업창 띄움

	if(result.result){
		//권한이 한개이면.................
		if(result.object.length == 1){

			$("#login_form #default_auth_grp_cd").val(result.object[0].auth_cd);

			//업무개발자이면.. 업무갯수 파악 
			if(result.object[0].auth_id == 4){
				ajaxCall("/auth/getUsersWrkJobCdObject?user_id="+param_user_id, null, "GET", callback_getUsersWrkJobCdObjectAction);
			}else{
				//업무개발자 외 : 팝업창 없이 대시보드로 진입
				ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
			}
		}else{
			//다중권한이면
			//콤보박스 생성
			createAuthCombo();
			showSelectRolePop();
		}
	}else{
//		$.messager.alert('',"시스템 접근 권한이 없습니다.<br/>관리자에게 문의바랍니다.",'info');
		$.messager.alert('',result.message,'info');
		return;		
	}
}

var callback_getUsersWrkJobCdObjectAction = function(result){
	if(result.result){
		//단일업무이면...업무가 하나이면...
		if(result.object.length == 1){
			//단일업무를 기본업무로 세팅한다.
			$("#login_form #default_wrkjob_cd").val(result.object[0].wrkjob_cd);
			ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
		}else{
			//다중업무이면...업무가 여러개이면...
			//콤보박스 생성
			createAuthCombo();
			showSelectRolePop();
		}
	}else{
		$.messager.alert('',result.message,'info');
	}
}

function fnDefaultRoleValidationCheck(){
	var default_auth_grp_cd=$("#selectDefaultAuthCombo").combobox('getValue');
	var default_wrkjob_cd= $('#selectDefaultWrkjobCd').combo('getValue');
	
	$("#login_form #default_auth_grp_cd").val(default_auth_grp_cd);
	$("#login_form #default_wrkjob_cd").val(default_wrkjob_cd);
	
	var selectDefaultAuthCombo= $("#selectDefaultAuthCombo").combobox('getValue');
	var selectDefaultWrkjobCd=$("#selectDefaultWrkjobCd").combobox('getValue');

	if(selectDefaultAuthCombo == "ROLE_DEV" && selectDefaultWrkjobCd == ""){
		$.messager.alert('','업무를 선택하여 주세요.');
		return;
	}
	return true;
}
//변경, 확인, 저장 버튼
//변경, 확인, 저장 버튼을 누르면 팝업창을 닫고 로그인을 한다.
//저장 버튼을 누르면 권한과 업무를 저장한다.
var isChecked = false;
function fnSaveDefaultRole(){
	
	if(!fnDefaultRoleValidationCheck()){
		return;
	}
	
	if(login_user_id == null || login_user_id == ""){
		hideSelectRolePop();
	}else{
		$("#login_form #user_id").val(login_user_id);
	}
	//로그인시 기본설정으로 저장 체크되어 있으면...
	isChecked = $("#role_save").is(":checked");
	if(isChecked){
		ajaxCall("/auth/updateUserDefaultRole", $("#login_form"), "GET", callback_updateUserDefaultRoleAction);
	}else{
		//로그인시 기본설정으로 저장 체크되어 있지 않으면
		ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
	}
}

function fnDefaultRoleValidationCheck(){
	var default_auth_grp_cd=$("#selectDefaultAuthCombo").combobox('getValue');
	var default_wrkjob_cd= $('#selectDefaultWrkjobCd').combo('getValue');
	
	$("#login_form #default_auth_grp_cd").val(default_auth_grp_cd);
	$("#login_form #default_wrkjob_cd").val(default_wrkjob_cd);
	
	var selectDefaultAuthCombo= $("#selectDefaultAuthCombo").combobox('getValue');
	var selectDefaultWrkjobCd=$("#selectDefaultWrkjobCd").combobox('getValue');

	if(selectDefaultAuthCombo == ""){
		$.messager.alert('','권한을 선택하여 주세요.');
		return false;
	}
	if(selectDefaultAuthCombo == "ROLE_DEV" && selectDefaultWrkjobCd == ""){
		$.messager.alert('','업무를 선택하여 주세요.');
		return false;
	}
	return true;
}

var callback_updateUserDefaultRoleAction = function(result){
	if(result.result){
		if(login_user_id == null || login_user_id == ""){
			ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
		}else{
			parent.$.messager.alert('','저장하였습니다','info',function(){
				setTimeout(function() {			
					ajaxCall("/auth/loginCheck", $("#login_form"), "GET", callback_loginCheckAction);
					},500);
			});
		}
	}
}

