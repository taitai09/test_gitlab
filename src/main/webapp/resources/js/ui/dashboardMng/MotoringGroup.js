var dpnd_job_sched_type_cd = '01';  // dpnd_job_sched_type_cd 값을 가지고오기위한 초기값;
	
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#detail_form #user_id").textbox({readonly : true});
	$("#detail_form #user_id").textbox("setValue",$("#submit_form #user_id").val());
	
	initMotoringGroupList();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	$('#searchKey').combobox({
		onClick:function(obj){
			if(obj.value){
				$("#searchValue").textbox('readonly','');
			}else {
				$("#searchValue").textbox('readonly','true');
				$("#searchValue").textbox('setValue','');
			}
		}
	});
	
});

function initMotoringGroupList(){
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'group_id',title:'그룹 아이디',width:"10%",halign:"center",align:"right",sortable:"true"},
			{field:'group_nm',title:'그룹명',width:"15%",halign:"center",align:'left'},
			{field:'desplay_seq',title:'정렬순서',width:"10%",halign:"center",align:'right'},
			{field:'group_desc',title:'그룹 설명',width:"40%",halign:"center",align:'left'},
			{field:'user_id',title:'등록 아이디',width:"15%",halign:"center",align:'left'}
		]],
//			fitColumns:true,
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
};

function Btn_OnClick(){
	
	if (($('#searchKey').combobox('getValue') == "" && $("#searchKey").textbox('getValue') != "") ||
			($('#searchKey').combobox('getValue') != "" && $("#searchKey").textbox('getValue') == "")) {
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) {
		parent.openMessageProgress("작업스케쥴러 종속관계 관리"," ");
	}
	
	ajaxCall("/DashboardMng/getMotoringGroupList",
			$("#submit_form"),
			"POST",
			callback_MotoringGroupAction);
}

//callback 함수
var callback_MotoringGroupAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) {
		parent.closeMessageProgress();
	}
};

function Btn_SaveSetting(){
	
	var rows = $("#tableList").datagrid('getRows');
	
	if($("#group_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','그룹명을 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #user_id").textbox('getValue') == ""){
		parent.$.messager.alert('','유저아이디를 입력해 주세요.');
		return false;
	}
	
	if($("#detail_form #group_desc").val().length > 50) {
		parent.$.messager.alert('','그룹 설명은 50자리 이하입니다.');
		return false;
	}
	
	if($("#detail_form #desplay_seq").val() <= 0){
		parent.$.messager.alert('','정렬순서는 1부터 시작번호 입니다.');
		return false;
	}
	
	if($("#detail_form #user_id").val() != $("#submit_form #user_id").val()) {
		
		parent.$.messager.confirm('', '등록아이디와 본인 아이디가 다르기때문에 본인 아이디로 수정됩니다. 수정하시겠습니까?', function(check) {
			
			if (check) {
				$("#detail_form #user_id").textbox("setValue",$("#submit_form #user_id").val());
				saveMotoringGroup();
			}else{
				return false;
			}
		});
		
	}else{
		saveMotoringGroup();
	}
	
	function saveMotoringGroup(){
		
		ajaxCall("/DashboardMng/saveMotoringGroup",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	};
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if($("#crud_flag").val() == 'U') {
		message = "수정 되었습니다.";
	}
	
	if(result.result){
		parent.$.messager.alert('',message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteSetting(){
	var data = $("#tableList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ ' + data.group_nm + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/DashboardMng/deleteMotoringGroup", 
					$("#detail_form"), 
					"POST",
					callback_DeleteSettingAction);
		}
	});
}

//callback 함수
var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setDetailView(selRow){
	
	$("#crud_flag").val("U");
	$("#old_group_nm").val(selRow.group_nm);

	$("#detail_form #user_id").textbox({readonly : true});

	$("#group_nm").textbox("setValue", selRow.group_nm);
	$("#desplay_seq").textbox("setValue", selRow.desplay_seq);
	$("#group_id").val(selRow.group_id);
	$("#detail_form #user_id").textbox("setValue", selRow.user_id);
	$("#group_desc").textbox("setValue", selRow.group_desc);
	
}

function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	$("#detail_form #user_id").textbox("setValue",$("#submit_form #user_id").val());
	
	$("#crud_flag").val("C");
	$("#old_group_id").val("");

	
	$("#group_nm").textbox({readonly : false});
	$("#crud_flag").val("C");
	$("#old_group_id").val("");
	$("#group_id").val("");
	$("#old_group_nm").val("");
	
	$("#group_nm").textbox("setValue", "");
	$("#group_desc").textbox("setValue", "");
	$("#desplay_seq").textbox("setValue", "");
	
}