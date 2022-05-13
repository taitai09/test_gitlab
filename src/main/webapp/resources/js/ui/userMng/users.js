var currentPagePerCount = 10;
$(document).ready(function(){
	$("body").css("visibility", "visible");
	$(".panel-body .panel-body-noheader .panel-body-noborder").css("overflow","hidden");
	
	$('#detail_form #user_id').textbox({
		onChange: function(value){
			if( $("#detail_form #crud_flag").val() == 'C' ){
				$("#idcheck").val("N");	
			}else {
				$("#idcheck").val("Y");	
			}
			//텍스트박스 값 공백삭제
			value = value.replace(/ /gi, "");
			$(this).textbox("setValue",value);
		}
	});
	
	$('#searchValue').textbox({readonly:true});
	$('#searchKey').combobox({
		onChange : function(newValue,oldValue){
			if( newValue == "" ){
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else {
				$('#searchValue').textbox({readonly:false});
			}
		}
	});
	
	$('#searchCount').combobox({
		onSelect:function(rec){
			Btn_OnClick();
		}
	});
	
	$("#userInfoTab").tabs({
		plain:true
	});
	
	let t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
		if( e.keyCode == 13 ){	// when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});
	
	$('#selEmail').combobox({
		onSelect:function(rec){
			if( rec.value == "" ){
				$("#emailCp").textbox("setValue","");
				$("#emailCp").textbox("readonly",false);
			}else {
				$("#emailCp").textbox("setValue",rec.value);
				$("#emailCp").textbox("readonly",true);
			}
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : false,
		onCheckAll : function(){
			setCheckDetail();
		},
		onUncheckAll : function(){
			setCheckDetail();
		},
		onCheck:function(index, row){
			setCheckDetail();
		},
		onUncheck:function(){
			setCheckDetail();
		},
		onClickRow : function(index,row){
			setDetailView(row);
		},
		columns:[[
			{field:'chk_user_id',checkbox:"true"},
			{field:'user_id',title:'사용자ID',width:"10%",halign:"center",align:"center",sortable:"true", formatter:lockChecker},
			{field:'user_nm',title:'사용자명',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'ext_no',title:'내선번호',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'hp_no',title:'휴대폰번호',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'email',title:'이메일주소',width:"10%",halign:"center",align:'left',sortable:"true"},
			{field:'default_password_yn',title:'기본비밀번호여부',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'password_chg_dt',title:'비밀번호변경일시',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'approve_yn',title:'승인여부',width:"4%",halign:"center",align:'center',sortable:"true"},
			{field:'approve_dt',title:'승인일시',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'approve_nm',title:'승인자',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'default_auth_grp_id_nm',width:"8%",title:'기본권한',halign:"center",align:'center',sortable:"true"},
			{field:'default_wrkjob_cd_nm',width:"8%",title:'기본업무',halign:"center",align:'center',sortable:"true"},
			{field:'use_yn',title:'사용여부',width:"4%",halign:"center",align:'center',sortable:"true"},
			
			{field:'password',hidden:"true"},
			{field:'extNo1',hidden:"true"},
			{field:'extNo2',hidden:"true"},
			{field:'extNo3',hidden:"true"},
			{field:'hpNo1',hidden:"true"},
			{field:'hpNo2',hidden:"true"},
			{field:'hpNo3',hidden:"true"},
			{field:'emailId',hidden:"true"},
			{field:'emailCp',hidden:"true"},
			{field:'default_wrkjob_cd',hidden:"true"},
			{field:'default_auth_grp_id',hidden:"true"}
		]],
		onLoadError:function(){
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	function lockChecker(val, row){
		let viewStr = '';
		let imgStr = '<img class="lockIcon" src="/resources/images/lock.png" alt="Icon">';
		
		if( row.user_locked_yn === 'Y' ){
			viewStr = imgStr + val;
		}else {
			viewStr = val;
		}
		return viewStr;
	}
	
	$("#userAuthList").datagrid({
		view: myview,
		onClickRow : function(index,row){
			setDetailAuthView(row);
		},
		columns:[[
			{field:'auth_cd',title:'권한코드',width:"20%",halign:"center",align:"center",sortable:"true"},
			{field:'user_id',hidden:"true"},
			{field:'auth_grp_id',hidden:"true"},
			{field:'auth_nm',title:'권한명',width:"20%",halign:"center",align:'center'},
			{field:'auth_start_day',title:'권한시작일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'auth_end_day',title:'권한종료일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'default_auth_grp_id',hidden:true},
			{field:'default_auth_yn',title:'기본권한여부',width:"20%",halign:"center",align:'center'}
		]],
		onLoadError:function(){
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#userWrkJobList").datagrid({
		view: myview,
		onClickRow : function(index,row){
			setDetailWrkJobView(row);
		},
		columns:[[
			{field:'wrkjob_cd',hidden:"true"},
			{field:'user_id',hidden:"true"},
			{field:'wrkjob_nm',title:'업무명',width:"20%",halign:"center",align:'center',sortable:"true"},
			{field:'workjob_start_day',title:'업무시작일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'workjob_end_day',title:'업무종료일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'default_wrkjob_yn',title:'기본업무여부',width:"20%",halign:"center",align:'center'},
			{field:'leader_yn',title:'업무리더 여부',width:"20%",halign:"center",align:'center'},
		]],
		onLoadError:function(){
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#userDBAuthList").datagrid({
		view: myview,
		onClickRow : function(index,row){
			setDetailDBAuthView(row);
		},
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB명',width:"20%",halign:"center",align:'center',sortable:"true"},
			{field:'privilege_start_day',title:'권한시작일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'privilege_end_day',title:'권한종료일자',width:"20%",halign:"center",align:'center',formatter:getDateFormat},
		]],
		onLoadError:function(){
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#auth_form #auth_grp_id').combobox({
		url:"/Common/getAuth?isChoice=X",
		method:"get",
		valueField:'auth_id',
		textField:'auth_nm',
		onLoadSuccess: function(event){
			$('#auth_form #auth_grp_id').combobox('textbox').attr("placeholder","선택");
			$('#auth_form #default_auth_yn').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$('#wrkjob_form #wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?isChoice=X",
		method:'get',
		onChange : function(){
			$('#chkLeaderW').switchbutton({checked:false});
			$("#wrkjob_form #leader_yn").val("N");	
		},
		onLoadSuccess: function(event){
			$('#wrkjob_form #wrkjob_cd').combobox('textbox').attr("placeholder","선택");
			$('#wrkjob_form #default_wrkjob_yn').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$('#dbauth_form #dbid').combobox({
		url:"/Sysmng/getDatabase?isChoice=X",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadSuccess: function(event){
			$('#dbauth_form #dbid').combobox('textbox').attr("placeholder","선택");
		}
	});	
	
	$('#chkLeaderW').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if( checked ){
				if( $("#wrkjob_cd").combobox("getValue") != '' ){
					
					$("#wrkjob_form #leader_yn").val("Y");
					// 다른 리더의 존재여부 체크
					ajaxCall("/CheckWorkJobLeader",
							$("#wrkjob_form"),
							"POST",
							callback_CheckWorkJobLeaderAction);
				}
				
			}else {
				$("#wrkjob_form #leader_yn").val("N");
			}
		}
	});
	
	let extNo2 = $('#extNo2');
	let extNo3 = $('#extNo3');
	let hpNo2 = $('#hpNo2');
	let hpNo3 = $('#hpNo3');
	
	extNo2.textbox('textbox').bind('keydown', function(e){
		if( $(this).val().length > 3 ){
			extNo2.textbox('setValue', $(this).val().substring(0,3));
		}else {
			extNo2.textbox('setValue', $(this).val());
		}
	});
	
	extNo3.textbox('textbox').bind('keydown', function(e){
		if( $(this).val().length > 3 ){
			extNo3.textbox('setValue', $(this).val().substring(0,3));
		}else {
			extNo3.textbox('setValue', $(this).val());
		}
	});
	
	hpNo2.textbox('textbox').bind('keydown', function(e){
		if( $(this).val().length > 3 ){
			hpNo2.textbox('setValue', $(this).val().substring(0,3));
		}else {
			hpNo2.textbox('setValue', $(this).val());
		}
	});
	
	hpNo3.textbox('textbox').bind('keydown', function(e){
		if( $(this).val().length > 3 ){
			hpNo3.textbox('setValue', $(this).val().substring(0,3));
		}else {
			hpNo3.textbox('setValue', $(this).val());
		}
	});
	
	$('#searchCount').combobox({
		onChange:function(newval,oldval){
			currentPagePerCount = newval;
			$("#submit_form #pagePerCount").val(currentPagePerCount); //검색페이지수 설정
		}
	});
	
	$("#detail_form #use_yn").combobox({disabled:true});
	$("#approveBtn").linkbutton({disabled:true});
	$("#unApproveBtn").linkbutton({disabled:true});
	$("#resetPwdBtn").linkbutton({disabled:true});
	
	$('#detail_form #default_auth_grp_id_nm').textbox({readonly:true});
	$('#detail_form #default_wrkjob_cd_nm').textbox({readonly:true});
	$("#detail_form #leader_yn").textbox({readonly:true});
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		Btn_ResetFieldAll();
		if( formValidationCheck() ){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#nextBtnEnabled").click(function(){
		Btn_ResetFieldAll();
		if( formValidationCheck() ){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
});

//callback 함수
var callback_CheckWorkJobLeaderAction = function(result){
	if( result.result ){
		if( result.message != '' && result.message != null && result.message != undefined ){
			parent.$.messager.alert('',result.message);
		}
		$('#chkLeaderW').switchbutton({checked:true});
		$("#wrkjob_form #leader_yn").val("Y");
		
	}else {
		parent.$.messager.alert('',result.message);
		$('#chkLeaderW').switchbutton({checked:false});
		$("#wrkjob_form #leader_yn").val("N");
	}
};

function Btn_checkID(){
	if( $("#user_id").textbox("getValue") == "" ){
		parent.$.messager.alert('','사용자 ID를 입력해 주세요.');
		return false;
	}
	
	if( !checkHangle($("#detail_form #user_id").textbox('getValue')) ){
		$.messager.alert('','한글은 입력하실 수 없습니다.');
		$("#detail_form #user_id").textbox("setValue","");
		return false;
	}
	
	if( !checkId($("#user_id").textbox('getValue')) ){
		parent.$.messager.alert('','사용자 ID는 최소 6자 ~ 최대 20자 이내이고<br/>반드시 영문, 숫자 각 1자리 이상 포함되어야 합니다.');
		return false;
	}
	
	if( $("#user_id").textbox("getValue").length < 6 || $("#user_id").textbox("getValue").length > 20 ){
		$.messager.alert('','사용자 ID를  6 ~ 20자 사이로 입력해 주세요.');
		return false;
	}
	
	$("#detail_form input[name='user_id']").val($("#user_id").val());
	ajaxCall("/Users/CheckUserId",
			$("#detail_form"),
			"POST",
			callback_CheckUserIdAction);
}	// end of Btn_checkID

var callback_CheckUserIdAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용 가능한 ID입니다.','info',function(){
			$("#idcheck").val("Y");	
		});
		
	}else {
		parent.$.messager.alert('',result.message,'error',function(){
			$("#user_id").textbox("setValue","");
		});
	}
};
var callback_UsersAction = function(result){
	Btn_ResetField('basic');
	
	json_string_callback_common(result,'#tableList',true);
};

function Btn_SaveUser(){
	if( $("#idcheck").val() == "N" ){
		parent.$.messager.alert('','사용자 ID 중복체크를 진행해 주세요.');
		return false;
	}
	
	if( $("#user_id").textbox('getValue') == "" ){
		parent.$.messager.alert('','사용자 ID를 입력해 주세요.');
		return false;
	}
	if( $("#detail_form #crud_flag").val() == 'C' ){
		if( !checkId($("#user_id").textbox('getValue')) ){
			parent.$.messager.alert('','사용자 ID는 최소 6자 ~ 최대 20자 이내이고<br/>반드시 영문, 숫자 각 1자리 이상 포함되어야 합니다.');
			return false;
		}
	}
	
	if( $("#user_nm").textbox('getValue') == "" ){
		parent.$.messager.alert('','사용자명을 입력해 주세요.');
		return false;
	}
	
	if( $("#user_nm").textbox('getValue').length > 16 ){
		parent.$.messager.alert('','사용자명을 16자 이내로 입력해 주세요.');
		return false;
	}
	
	let email = "";
	if( $("#emailId").textbox('getValue') != "" && $("#emailCp").textbox('getValue') != "" ){
		email = $("#emailId").textbox('getValue') + "@" + $("#emailCp").textbox('getValue');
	}
	if( $("#emailId").textbox('getValue') != "" || $("#emailCp").textbox('getValue') != "" ){
		//특수문자 체크
		if( checkIdSpecial($("#emailId").textbox('getValue')) == -1 ){
			parent.$.messager.alert('','이메일 주소를 정확히 입력해 주세요.');
			return false;
		}
		//전체 이메일 정규식 체크
		if( !checkEmail(email) ){
			parent.$.messager.alert('','이메일 주소를 정확히 입력해 주세요.');
			return false;
		}
		
		if( email.length > 30 ){
			parent.$.messager.alert('','이메일 주소를 30자 이내로 등록히 주세요.');
			return false;
		}
	}
	
	let extNo = "";
	if( $("#extNo1").combobox('getValue') != "" && $("#extNo2").textbox('getValue') != "" && $("#extNo3").textbox('getValue') != "" ){
		extNo = $("#extNo1").combobox('getValue') + "-" + $("#extNo2").textbox('getValue') + "-" + $("#extNo3").textbox('getValue');
	}
	if( $("#extNo1").combobox('getValue') != "" || $("#extNo2").textbox('getValue') != "" || $("#extNo3").textbox('getValue') != "" ){
		if( !checkTelno(extNo) ){
			parent.$.messager.alert('','내선 번호를 정확히 입력해 주세요.');
			return false;
		}
	}
	
	let hpNo = "";
	if( $("#hpNo1").combobox('getValue') != "" && $("#hpNo2").textbox('getValue') != "" && $("#hpNo3").textbox('getValue') != "" ){
		hpNo = $("#hpNo1").combobox('getValue') + "-" + $("#hpNo2").textbox('getValue') + "-" + $("#hpNo3").textbox('getValue');
	}
	if( $("#hpNo1").combobox('getValue') != "" || $("#hpNo2").textbox('getValue') != "" || $("#hpNo3").textbox('getValue') != "" ){
		if( !checkTelno(hpNo) ){
			parent.$.messager.alert('','핸드폰 번호를 정확히 입력해 주세요.');
			return false;
		}
	}
	
	if( $("#detail_form #is_new").val() == "N" && $("#detail_form #use_yn").combobox('getValue') == "" ){
		parent.$.messager.alert('','사용여부를 선택해 주세요.');
		return false;
	}
	if( $("#detail_form #approve_yn").combobox('getValue') == "" ){
		parent.$.messager.alert('','승인여부를 선택해 주세요.');
		return false;
	}
	
	$("#email").val(email);
	$("#ext_no").val(extNo);
	$("#hp_no").val(hpNo);
	$("#detail_form input[name='user_id']").val($("#user_id").val());
	
	// iframe name에 사용된 menu_id를 상단 frameName에 설정
	parent.frameName = $("#menu_id").val();
	
	ajaxCall("/Users/Save",
			$("#detail_form"),
			"POST",
			callback_SaveUsersAction);
}

var callback_SaveUsersAction = function(result){
	let userResultMsg;
	if( $("#detail_form #crud_flag").val() == 'C' ){
		userResultMsg = "저장 되었습니다.";
	}else {
		userResultMsg = "수정 되었습니다."
	}
	
	if(result.result){
		parent.$.messager.alert('',userResultMsg,'info',function(){
			setTimeout(function(){
				Btn_OnClick();
				Btn_ResetFieldAll();
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

//승인버튼
function Btn_ApproveUser(){
	let rows = $('#tableList').datagrid('getChecked');
	
	let chk_user_id = new Array();
	for( let i = 0; i < rows.length; i++ ){
		chk_user_id.push(rows[i].user_id);
	}
	
	if( chk_user_id.length > 0 ){
		$("#chk_user_id").val(chk_user_id.join('/'));	//배열을 스트링 형태로 변환후 서버에 전달
	}
	
	parent.$.messager.confirm('', '일괄 승인처리를 하시겠습니까?', function(check){
		if( check ){
			$("#detail_form #approve_yn").combobox('setValue','Y');
			
			ajaxCall("/ApproveUser",
					$("#detail_form"),
					"POST",
					callback_ApproveUserAction);
		}
	});
}

//미승인버튼
function Btn_UnApproveUser(){
	let rows = $('#tableList').datagrid('getChecked');
	
	let chk_user_id = new Array();
	for( let i = 0; i < rows.length; i++ ){
		chk_user_id.push(rows[i].user_id);
	}
	if( chk_user_id.length > 0 ){
		$("#chk_user_id").val(chk_user_id.join('/'));	//배열을 스트링 형태로 변환후 서버에 전달
	}
	
	parent.$.messager.confirm('', '일괄 미승인처리를 하시겠습니까?', function(check){
		if( check ){
			$("#detail_form #approve_yn").combobox('setValue','N');
			
			ajaxCall("/ApproveUser",
					$("#detail_form"),
					"POST",
					callback_ApproveUserAction);
		}
	});
}
var callback_ApproveUserAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용자 승인여부 처리가 완료 되었습니다.','info',function(){
			setTimeout(function(){
				Btn_OnClick();
				Btn_ResetField('basic');
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_ResetPwd(){
	if( $("#user_id").textbox('getValue') == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', $("#user_nm").textbox('getValue') + '님의 비밀번호가 초기화 됩니다.<br/>진행하시겠습니까?', function(r){
		if(r){
			ajaxCall("/ResetUserPassword",
					$("#detail_form"),
					"POST",
					callback_ResetUserPasswordAction);
		}
	});
}
var callback_ResetUserPasswordAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용자 비밀번호 초기화가 완료 되었습니다. <br/>해당 사용자에게 임시 비밀번호를 안내 바랍니다.','info',function(){
			setTimeout(function(){
				Btn_OnClick();
				Btn_ResetFieldAll();
			},1000);
		});
	
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_SaveUserAuth(){
	if( $("#auth_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #auth_grp_id").combobox('getValue') == "" ){
		parent.$.messager.alert('','권한명을 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #authStartDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 시작일자를 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #authEndDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 종료일자를 선택해 주세요.');
		return false;
	}
	
	if( !compareAnBDate($("#auth_form #authStartDay").datebox('getValue'), $("#auth_form #authEndDay").datebox('getValue')) ){
		parent.$.messager.alert('','시작일과 종료일을 확인해 주세요.');
		return false;
	}
	
	if( $("#auth_form #default_auth_yn").combobox('getValue') == "" ){
		parent.$.messager.alert('','기본권한여부를 선택해 주세요.');
		return false;
	}
	
	$("#auth_form #auth_start_day").val(strReplace($("#auth_form #authStartDay").datebox('getValue'),"-",""));
	$("#auth_form #auth_end_day").val(strReplace($("#auth_form #authEndDay").datebox('getValue'),"-",""));
	
	ajaxCall("/UsersAuth/Save",
			$("#auth_form"),
			"POST",
			callback_SaveUsersAuthAction);
}

//callback 함수
var callback_SaveUsersAuthAction = function(result){
	let userAuthResultMsg;
	if( $("#auth_form #crud_flag").val() == 'C' ){
		userAuthResultMsg = "저장 되었습니다.";
	}else {
		userAuthResultMsg = "수정 되었습니다."
	}
	
	if( result.result ){
		parent.$.messager.alert('',userAuthResultMsg,'info',function(){
			setTimeout(function(){
				Btn_OnClickAuth();
				Btn_ShowTab(1);
			},1000);
		});
	
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteUserAuth(){
	if( $("#auth_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #auth_nm").val() == "" ){
		parent.$.messager.alert('','사용자 권한을 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #authStartDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 시작일자를 선택해 주세요.');
		return false;
	}
	
	if( $("#auth_form #authEndDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 종료일자를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', $("#auth_form #user_id").val()+ '의 권한 [ ' + $("#auth_form #auth_nm").val()+' ] 를 삭제 하시겠습니까?', function(check){
		if( check ){
			ajaxCall("/UsersAuth/Delete",
					$("#auth_form"),
					"POST",
					callback_DeleteUsersAuthAction);
		}
	});
}

var callback_DeleteUsersAuthAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용자 권한 정보 삭제가 완료 되었습니다.','info',function(){
			setTimeout(function(){
				Btn_OnClickAuth();
				Btn_ShowTab(1);
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_SaveUserWrkJob(){
	if( $("#wrkjob_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #wrkjob_cd").combobox('getValue') == "" ){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #workStartDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','업무 시작일자를 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #workEndDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','업무 종료일자를 선택해 주세요.');
		return false;
	}
	
	if( !compareAnBDate($("#wrkjob_form #workStartDay").datebox('getValue'), $("#wrkjob_form #workEndDay").datebox('getValue')) ){
		parent.$.messager.alert('','시작일과 종료일을 확인해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #default_wrkjob_yn").combobox('getValue') == "" ){
		parent.$.messager.alert('','기본업무여부를 선택해 주세요.');
		return false;
	}
	
	$("#wrkjob_form #workjob_start_day").val(strReplace($("#wrkjob_form #workStartDay").datebox('getValue'),"-",""));
	$("#wrkjob_form #workjob_end_day").val(strReplace($("#wrkjob_form #workEndDay").datebox('getValue'),"-",""));
	
	ajaxCall("/UsersWrkJob/Save",
			$("#wrkjob_form"),
			"POST",
			callback_SaveUsersWrkJobAction);
}

var callback_SaveUsersWrkJobAction = function(result){
	let userWrkJobResultMsg;
	if( $("#wrkjob_form #crud_flag").val() == 'C' ){
		userWrkJobResultMsg = "저장 되었습니다.";
	}else {
		userWrkJobResultMsg = "수정 되었습니다."
	}
	
	if( result.result ){
		parent.$.messager.alert('',userWrkJobResultMsg,'info',function(){
			setTimeout(function(){
				Btn_OnClickWrkJob();
				Btn_ShowTab(2);
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteUserWrkJob(){
	if( $("#wrkjob_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #wrkjob_nm").val() == "" ){
		parent.$.messager.alert('','사용자 업무를 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #workStartDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','업무 시작일자를 선택해 주세요.');
		return false;
	}
	
	if( $("#wrkjob_form #workEndDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','업무 종료일자를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 업무명 : '+$("#wrkjob_form #wrkjob_nm").val() + ' ] 을(를) 삭제 하시겠습니까?', function(check){
		if( check ){
			ajaxCall("/UsersWrkJob/Delete",
					$("#wrkjob_form"),
					"POST",
					callback_DeleteUsersWrkJobAction);
		}
	});
}

var callback_DeleteUsersWrkJobAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용자 업무 정보 삭제가 완료 되었습니다.','info',function(){
			setTimeout(function(){
				Btn_OnClickWrkJob();
				Btn_ShowTab(2);
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function setCheckDetail(){
	let rows = $('#tableList').datagrid('getChecked');
	if( rows.length >= 1 ){		//여러개 체크 됐을 시 승인버튼만 활성화
		$("#saveBtn").linkbutton({disabled:true});
		$("#resetBtn").linkbutton({disabled:true});
		$("#approveBtn").linkbutton({disabled:false});
		$("#unApproveBtn").linkbutton({disabled:false});
		
	}else {		//한개만 체크할시 저장버튼만 활성화
		$("#saveBtn").linkbutton({disabled:false});
		$("#resetBtn").linkbutton({disabled:false});
		$("#approveBtn").linkbutton({disabled:true});
		$("#unApproveBtn").linkbutton({disabled:true});
	}
	
	$("#checkUserId").linkbutton({disabled:true});
}

function setDetailView(selRow){
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #user_id").textbox({disabled:true});
	$("#detail_form #use_yn").combobox({disabled:false});
	
	$("#detail_form #use_yn").combobox("setValue", selRow.use_yn);
	$("#detail_form #use_yn").val(selRow.use_yn);
	$("#detail_form #is_new").val("N");
	$("#detail_form #idcheck").val("Y");
	$("#detail_form #user_id").textbox("setValue", selRow.user_id);
	
	$("#detail_form #user_nm").textbox("setValue", selRow.user_nm);
	$("#detail_form #password").textbox("setValue", selRow.password);
	$("#detail_form #emailId").textbox("setValue", selRow.emailId);
	$("#detail_form #emailCp").textbox("setValue", selRow.emailCp);
	$("#detail_form #selEmail").combobox("setValue", selRow.emailCp);
	$("#detail_form #extNo1").combobox("setValue", selRow.extNo1);
	$("#detail_form #extNo2").textbox("setValue", selRow.extNo2);
	$("#detail_form #extNo3").textbox("setValue", selRow.extNo3);
	$("#detail_form #hpNo1").combobox("setValue", selRow.hpNo1);
	$("#detail_form #hpNo2").textbox("setValue", selRow.hpNo2);
	$("#detail_form #hpNo3").textbox("setValue", selRow.hpNo3);	
	
	$("#detail_form #default_auth_grp_id_nm").textbox("setValue", selRow.default_auth_grp_id_nm);	
	$("#detail_form #default_wrkjob_cd_nm").textbox("setValue", selRow.default_wrkjob_cd_nm);	
	$("#detail_form #leader_yn").textbox("setValue", selRow.leader_yn);	
	
	$("#detail_form #leader_yn").textbox({disabled:false});
	
	$("#wrkjob_form #user_nm").val($("#detail_form #user_nm").textbox("getValue"));
	$("#detail_form #approve_yn").combobox("setValue", selRow.approve_yn);
	
	$("#resetPwdBtn").linkbutton({disabled:false});
	
	$("#detail_form input[name='user_id']").val(selRow.user_id);
	$("#auth_form #user_id").val(selRow.user_id);
	$("#wrkjob_form #user_id").val(selRow.user_id);
	$("#dbauth_form #user_id").val(selRow.user_id);
	
	Btn_OnClickAuth();
	Btn_OnClickWrkJob();
	Btn_OnClickUserDBAuth();
}

function Btn_OnClickAuth(){
	// 사용자 권한 조회
	$('#userAuthList').datagrid('loadData',[]);
	$('#userAuthList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#userAuthList').datagrid('loading');
	
	ajaxCall("/UsersAuth",
			$("#detail_form"),
			"POST",
			callback_UsersAuthAction);
}

var callback_UsersAuthAction = function(result){
	Btn_ResetField('auth');
	json_string_callback_common(result,'#userAuthList',false);
};

function Btn_OnClickWrkJob(){
	// 사용자 부서 조회
	$('#userWrkJobList').datagrid('loadData',[]);
	$('#userWrkJobList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#userWrkJobList').datagrid('loading');
	
	ajaxCall("/UsersWrkJob",
			$("#detail_form"),
			"POST",
			callback_UsersWrkJobAction);
}
var callback_UsersWrkJobAction = function(result){
	Btn_ResetField('wrkJob');
	json_string_callback_common(result,'#userWrkJobList',false);
};

function Btn_OnClickUserDBAuth(){
	// 사용자 부서 조회
	$('#userDBAuthList').datagrid('loadData',[]);
	$('#userDBAuthList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#userDBAuthList').datagrid('loading');
	
	ajaxCall("/Users/DBAuth",
			$("#detail_form"),
			"POST",
			callback_UserDBAuthAction);
}
var callback_UserDBAuthAction = function(result){
	Btn_ResetField('dbauth');
	json_string_callback_common(result,'#userDBAuthList',false);
};

function setDetailAuthView(selRow){
	let startDay = ""; 
	let endDay = "";
	
	$("#auth_form #auth_grp_id").combobox("setValue", selRow.auth_grp_id);
	
	if( selRow.auth_start_day != "" ){
		startDay = selRow.auth_start_day.substr(0,4) + "-" + selRow.auth_start_day.substr(4,2) + "-" + selRow.auth_start_day.substr(6,2);
	}
	
	if( selRow.auth_end_day != "" ){
		endDay = selRow.auth_end_day.substr(0,4) + "-" + selRow.auth_end_day.substr(4,2) + "-" + selRow.auth_end_day.substr(6,2);
	}
	
	$("#auth_form #authStartDay").textbox("setValue", startDay);
	$("#auth_form #authEndDay").textbox("setValue", endDay);
	$("#auth_form #default_auth_yn").combobox("setValue", selRow.default_auth_yn);
	$("#auth_form #old_default_auth_yn").val(selRow.default_auth_yn);
	$("#auth_form #auth_comp_day").val(selRow.auth_start_day);
	$("#auth_form #crud_flag").val("U");
	$("#auth_form #auth_nm").val(selRow.auth_nm);
	$("#auth_form #old_auth_grp_id").val(selRow.auth_grp_id);
}

function setDetailWrkJobView(selRow){
	let startDay = ""; 
	let endDay = "";
	let leaderYn = false;
	
	$("#wrkjob_form #wrkjob_cd").combotree("setValue", selRow.wrkjob_cd);
	
	if( selRow.workjob_start_day != "" ){
		startDay = selRow.workjob_start_day.substr(0,4) + "-" + selRow.workjob_start_day.substr(4,2) + "-" + selRow.workjob_start_day.substr(6,2);
	}
	
	if( selRow.workjob_end_day != "" ){
		endDay = selRow.workjob_end_day.substr(0,4) + "-" + selRow.workjob_end_day.substr(4,2) + "-" + selRow.workjob_end_day.substr(6,2);
	}
	
	if( selRow.leader_yn == "Y" ){
		leaderYn = true;
	}else {
		leaderYn = false;
	}
	
	$("#wrkjob_form #crud_flag").val("U");
	$("#wrkjob_form #leader_yn").val(selRow.leader_yn);
	$("#wrkjob_form #wrkjob_nm").val(selRow.wrkjob_nm);
	$("#wrkjob_form #old_wrkjob_cd").val(selRow.wrkjob_cd);
	$("#wrkjob_form #workStartDay").textbox("setValue", startDay);
	$("#wrkjob_form #workEndDay").textbox("setValue", endDay);
	$("#wrkjob_form #default_wrkjob_yn").combobox("setValue", selRow.default_wrkjob_yn);
	$("#wrkjob_form #old_default_wrkjob_yn").val(selRow.default_wrkjob_yn);
	$("#wrkjob_form #default_wrkjob_cd").val(selRow.default_wrkjob_cd);
	$("#wrkjob_form #work_comp_day").val(selRow.workjob_start_day);
	$('#wrkjob_form #chkLeaderW').switchbutton({checked: leaderYn})	
}

function setDetailDBAuthView(selRow){
	let startDay = ""; 
	let endDay = "";
	
	if( selRow.privilege_start_day != "" ){
		startDay = selRow.privilege_start_day.substr(0,4) + "-" + selRow.privilege_start_day.substr(4,2) + "-" + selRow.privilege_start_day.substr(6,2)
	}
	
	if( selRow.workjob_end_day != "" ){
		endDay = selRow.privilege_end_day.substr(0,4) + "-" + selRow.privilege_end_day.substr(4,2) + "-" + selRow.privilege_end_day.substr(6,2)
	}
	
	$("#dbauth_form #crud_flag").val("U");
	$("#dbauth_form #db_name").val(selRow.db_name);
	$("#dbauth_form #old_dbid").val(selRow.dbid);
	$("#dbauth_form #dbid").combobox("setValue",selRow.dbid);
	$("#dbauth_form #privilegeStartDay").textbox("setValue", startDay);
	$("#dbauth_form #privilegeEndDay").textbox("setValue", endDay);
}

function Btn_SaveUserDBAuth(){
	if( $("#dbauth_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#dbauth_form #dbid").combobox('getValue') == "" ){
		parent.$.messager.alert('','DB명을 선택해 주세요.');
		return false;
	}
	
	if( $("#dbauth_form #privilegeStartDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 시작일자를 선택해 주세요.');
		return false;
	}
	
	if( $("#dbauth_form #privilegeEndDay").datebox('getValue') == "" ){
		parent.$.messager.alert('','권한 종료일자를 선택해 주세요.');
		return false;
	}
	
	if( !compareAnBDate($("#dbauth_form #privilegeStartDay").datebox('getValue'), $("#dbauth_form #privilegeEndDay").datebox('getValue')) ){
		parent.$.messager.alert('','시작일과 종료일을 확인해 주세요.');
		return false;
	}
	
	let privilege_start_day = strReplace($("#dbauth_form #privilegeStartDay").datebox('getValue'),"-","");
	let privilege_end_day = strReplace($("#dbauth_form #privilegeEndDay").datebox('getValue'),"-","");
	
	$("#dbauth_form #privilege_start_day").val(privilege_start_day);
	$("#dbauth_form #privilege_end_day").val(privilege_end_day);
	
	ajaxCall("/Users/DBAuth/Save",
			$("#dbauth_form"),
			"POST",
			callback_SaveUserDBAuthAction);
}

var callback_SaveUserDBAuthAction = function(result){
	let userDBAuthResultMsg;
	if( $("#dbauth_form #crud_flag").val() == 'C' ){
		userDBAuthResultMsg = "저장 되었습니다.";
	}else {
		userDBAuthResultMsg = "수정 되었습니다."
	}
	
	if( result.result ){
		parent.$.messager.alert('',userDBAuthResultMsg,'info',function(){
			setTimeout(function(){
				Btn_OnClickUserDBAuth();
				Btn_ShowTab(3);
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteUserDBAuth(){
	if( $("#dbauth_form #user_id").val() == "" ){
		parent.$.messager.alert('','사용자를 선택해 주세요.');
		return false;
	}
	
	if( $("#dbauth_form #db_name").val() == "" ){
		parent.$.messager.alert('','DB명을 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ DB명 : '+$("#dbauth_form #db_name").val() + ' ] 을(를) 삭제 하시겠습니까?', function(check){
		if( check ){
			ajaxCall("/Users/DBAuth/Delete",
					$("#dbauth_form"),
					"POST",
					callback_DeleteUserDBAuthAction);
		}
	});
}

var callback_DeleteUserDBAuthAction = function(result){
	if( result.result ){
		parent.$.messager.alert('','사용자 DB권한 삭제가 완료 되었습니다.','info',function(){
			setTimeout(function(){
				Btn_OnClickUserDBAuth();
				Btn_ShowTab(3);
			},1000);
		});
		
	}else {
		parent.$.messager.alert('',result.message);
	}
};

function Btn_ResetField(gb){
	setCheckDetail();
	
	if( gb == "basic" ){
		$("#detail_form #idcheck").val("N");
		$("#checkUserId").linkbutton({disabled:false});
		$("#detail_form #crud_flag").val("C");
		$("#detail_form #is_new").val("Y");
		$("#detail_form #email").val("");
		$("#detail_form #ext_no").val("");
		$("#detail_form #hp_no").val("");
		$("#detail_form input[name='user_id']").val("");
		$("#detail_form #user_id").textbox("setValue", "");
		$("#detail_form #user_id").textbox({disabled:false});
		$("#detail_form #user_nm").textbox("setValue", "");
		$("#detail_form #password").textbox("setValue", "");
		$("#detail_form #emailId").textbox("setValue", "");
		$("#detail_form #emailCp").textbox("setValue", "");
		$("#detail_form #selEmail").combobox("setValue", "");
		$("#detail_form #extNo1").combobox("setValue", "");
		$("#detail_form #extNo2").textbox("setValue", "");
		$("#detail_form #extNo3").textbox("setValue", "");
		$("#detail_form #hpNo1").combobox("setValue", "");
		$("#detail_form #hpNo2").textbox("setValue", "");
		$("#detail_form #hpNo3").textbox("setValue", "");
		$("#detail_form #use_yn").combobox("setValue", "");
		$("#detail_form #default_auth_grp_id_nm").textbox("setValue", "");
		$("#detail_form #default_wrkjob_cd_nm").textbox("setValue", "");
		$("#detail_form #leader_yn").textbox("setValue", "");
		$('#detail_form #leader_yn').textbox({disabled:true});
		
		$("#approveBtn").linkbutton({disabled:true});
		$("#unApproveBtn").linkbutton({disabled:true});
		$("#resetPwdBtn").linkbutton({disabled:true});
		$("#detail_form #use_yn").combobox({disabled:true});
		$('#detail_form #wrkjob_cd').combotree({disabled:true});
		
		$("#auth_form #user_id").val("");
		$("#dept_form #user_id").val("");
		
	}else if( gb == "auth" ){
		$("#userAuthList").datagrid('clearSelections');
		$("#auth_form #auth_start_day").val("");
		$("#auth_form #auth_end_day").val("");
		$("#auth_form #auth_nm").val("");
		$("#auth_form #auth_grp_id").combobox("setValue", "");
		$("#auth_form #authStartDay").textbox("setValue", getDateFormat(nowDate));
		$("#auth_form #default_auth_yn").combobox("setValue", "");
		$("#auth_form #authEndDay").textbox("setValue", "9999-12-31");
		$("#auth_form #auth_comp_day").val("");
		$("#auth_form #crud_flag").val("C");
		
	}else if( gb == 'wrkJob' ){
		$("#userWrkJobList").datagrid('clearSelections');
		$("#wrkjob_form #crud_flag").val("C");
		$("#wrkjob_form #workjob_start_day").val("");
		$("#wrkjob_form #workjob_end_day").val("");
		$("#wrkjob_form #wrkjob_nm").val("");
		$("#wrkjob_form #wrkjob_cd").combotree("setValue", "");
		$("#wrkjob_form #workStartDay").textbox("setValue", getDateFormat(nowDate));
		$("#wrkjob_form #workEndDay").textbox("setValue", "9999-12-31");
		$("#wrkjob_form #default_wrkjob_yn").combobox("setValue", "");
		$("#wrkjob_form #work_comp_day").val("");
		
	}else if( gb == "dbauth" ){
		$("#userDBAuthList").datagrid('clearSelections');
		$("#dbauth_form #crud_flag").val("C");
		$("#dbauth_form #db_name").val("");
		$("#dbauth_form #old_dbid").val("");
		$("#dbauth_form #dbid").combobox('setValue',"");
		$("#dbauth_form #privilegeStartDay").datebox('setValue',getDateFormat(nowDate));
		$("#dbauth_form #privilegeEndDay").datebox('setValue',"9999-12-31");
		$("#dbauth_form #privilege_start_day").val("");
		$("#dbauth_form #privilege_end_day").val("");
	}
}

function Btn_ResetFieldAll(){
	$('#userAuthList').datagrid('loadData',[]);
	$('#userWrkJobList').datagrid('loadData',[]);
	$('#userDBAuthList').datagrid('loadData',[]);
	$("#tableList").datagrid('clearSelections');
	$("#userAuthList").datagrid('clearSelections');
	$("#userWrkJobList").datagrid('clearSelections');
	$("#userDBAuthList").datagrid('clearSelections');
	
	setCheckDetail();
	
	$("#detail_form #idcheck").val("N");
	$("#checkUserId").linkbutton({disabled:false});
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #is_new").val("Y");
	$("#detail_form #email").val("");
	$("#detail_form #ext_no").val("");
	$("#detail_form #hp_no").val("");
	$("#detail_form input[name='user_id']").val("");
	$("#detail_form #user_id").textbox("setValue", "");
	$("#detail_form #user_id").textbox({disabled:false});
	$("#detail_form #user_nm").textbox("setValue", "");
	$("#detail_form #password").textbox("setValue", "");
	$("#detail_form #emailId").textbox("setValue", "");
	$("#detail_form #emailCp").textbox("setValue", "");
	$("#detail_form #selEmail").combobox("setValue", "");
	$("#detail_form #extNo1").combobox("setValue", "");
	$("#detail_form #extNo2").textbox("setValue", "");
	$("#detail_form #extNo3").textbox("setValue", "");
	$("#detail_form #hpNo1").combobox("setValue", "");
	$("#detail_form #hpNo2").textbox("setValue", "");
	$("#detail_form #hpNo3").textbox("setValue", "");
	$("#detail_form #use_yn").combobox("setValue", "");
	$("#detail_form #default_auth_grp_id_nm").textbox("setValue", "");
	$("#detail_form #default_wrkjob_cd_nm").textbox("setValue", "");
	$("#detail_form #leader_yn").textbox("setValue", "");
	$('#detail_form #leader_yn').textbox({disabled:true});
	
	$("#approveBtn").linkbutton({disabled:true});
	$("#unApproveBtn").linkbutton({disabled:true});
	$("#resetPwdBtn").linkbutton({disabled:true});
	$("#detail_form #use_yn").combobox({disabled:true});
	$('#detail_form #wrkjob_cd').combotree({disabled:true});
	
	$("#auth_form #user_id").val("");
	$("#dept_form #user_id").val("");
	
	$("#auth_form #auth_start_day").val("");
	$("#auth_form #auth_end_day").val("");
	$("#auth_form #auth_nm").val("");
	$("#auth_form #auth_grp_id").combobox("setValue", "");
	$("#auth_form #authStartDay").textbox("setValue", getDateFormat(nowDate));
	$("#auth_form #default_auth_yn").combobox("setValue", "");
	$("#auth_form #authEndDay").textbox("setValue", "9999-12-31");
	$("#auth_form #auth_comp_day").val("");
	$("#auth_form #crud_flag").val("C");
	
	$("#wrkjob_form #crud_flag").val("C");
	$("#wrkjob_form #workjob_start_day").val("");
	$("#wrkjob_form #workjob_end_day").val("");
	$("#wrkjob_form #wrkjob_nm").val("");
	$("#wrkjob_form #wrkjob_cd").combotree("setValue", "");
	$("#wrkjob_form #workStartDay").textbox("setValue", getDateFormat(nowDate));
	$("#wrkjob_form #workEndDay").textbox("setValue", "9999-12-31");
	$("#wrkjob_form #default_wrkjob_yn").combobox("setValue", "");
	$("#wrkjob_form #work_comp_day").val("");
	
	$("#dbauth_form #crud_flag").val("C");
	$("#dbauth_form #db_name").val("");
	$("#dbauth_form #old_dbid").val("");
	$("#dbauth_form #dbid").combobox('setValue',"");
	$("#dbauth_form #privilegeStartDay").datebox('setValue',getDateFormat(nowDate));
	$("#dbauth_form #privilegeEndDay").datebox('setValue',"9999-12-31");
	$("#dbauth_form #privilege_start_day").val("");
	$("#dbauth_form #privilege_end_day").val("");
}

function Btn_ShowTab(strIndex){
	$('#userInfoTab').tabs('select', strIndex);
}

//사용자 일괄업로드 팝업
function Btn_UserRegist(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#userExcelUploadPop').window("open");
}

/*페이징처리시작*/
function fnSetCurrentPage(direction){
	let currentPage = $("#submit_form #currentPage").val();
	
	if( currentPage != null && currentPage != "" ){
		if( direction == "PREV" ){
			currentPage--;
		}else if( direction == "NEXT" ){
			currentPage++;
		}
		$("#submit_form #currentPage").val(currentPage);
		
	}else {
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);
	
	let currentPage = $("#submit_form #currentPage").val();	//현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	
	if( currentPage <= 0 ){
		$("#submit_form #currentPage").val("1");
		return;
	}
	Btn_OnClick('P');
}

function Btn_OnClick(val){
	if( !formValidationCheck() ){
		return;
	}
	if( val != 'P' ){	//페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
	}
	fnSearch();
}

function fnSearch(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("사용자 관리"," "); 
	
	ajaxCall("/Users",
			$("#submit_form"),
			"POST",
			callback_UsersAction_ForReset);	
}

var callback_UsersAction_ForReset = function(result){
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);
};

var fnControlPaging = function(result){
	//페이징 처리
	let currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	
	let pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);
	
	let data;
	let dataLength=0;
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	
	//페이지를 보여줄지말지 여부를 결정
	if( currentPage > 1 ){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if( dataLength > 10 ){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else {
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	
	if( currentPage == 1 ){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		
		if( dataLength > pagePerCount ){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}
};

function formValidationCheck(){
	if( ($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "") ){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	return true;
}	/*페이징처리끝*/

function Excel_Download(){
	if( !formValidationCheck() ){
		return false;
	}
	
	$("#submit_form").attr("action","/Users/ExcelDown");
	$("#submit_form").submit();
}