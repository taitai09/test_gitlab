$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#detail_form #user_id").textbox({placeholder : '자동으로 입력됩니다.'});
	$('#reg_dt').textbox('textbox').prop('placeholder', '자동으로 입력됩니다.');
	
	$('#detail_form #group_id').combobox({
		url:"/DashboardMng/getMotoringGroupId",
		method:"get",
		valueField:'group_id',
		textField:'group_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#detail_form #group_id').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$('#dbid').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#detail_form #dbid').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	initMotoringGroupList();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
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
				{field:'group_nm',title:'그룹명',width:"20%",halign:"center",align:'left'},
				{field:'dbid',hidden : true},
				{field:'db_name',title:'DB명',width:"10%",halign:"center",align:'left'},
				{field:'desplay_seq',title:'정렬 순서',width:"10%",halign:"center",align:'right'},
				{field:'reg_dt',title:'등록일자',width:"20%",halign:"center",align:'center'}
			]],
//			fitColumns:true,
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
		});
		
};

function Btn_OnClick(){
	
	if(($('#searchKey').combobox('getValue') == "" && $("#searchKey").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchKey").textbox('getValue') == "")){
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
	
	ajaxCall("/DashboardMng/getDatabaseGroupList",
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
		
	if($("#group_id").combobox('getValue') == ""){
		parent.$.messager.alert('','그룹명을 선택해 주세요.');
		return false;
	}
	
	if($("#dbid").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#desplay_seq").textbox('getValue') == ""){
		parent.$.messager.alert('','정렬순서를 입력해 주세요.');
		return false;
	}
	
	if($("#desplay_seq").textbox('getValue') <= 0 ) {
		parent.$.messager.alert('','정렬순서는 1부터 시작입니다.');
		return false;
	}
	
	$("#group_nm").val($("#group_id").combobox("getText"));
	$("#db_name").val($("#dbid").combobox("getText"));
		
	saveDatabaseGroup();
		
	function saveDatabaseGroup(){
		ajaxCall("/DashboardMng/saveDatabaseGroup",
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
	
	parent.$.messager.confirm('', '[ 그룹명 : ' + data.group_nm + ' / DB명 : '+ data.db_name + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
				ajaxCall("/DashboardMng/deleteDatabaseGroup", 
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
	$("#group_nm").val(selRow.group_nm);
	$("#db_name").val(selRow.db_name);
	
	$("#group_id").combobox({readonly : true});
	$("#dbid").combobox({readonly : true});
	
	$("#group_id").combobox("setValue", selRow.group_id);
	$("#dbid").combobox("setValue", selRow.dbid);
	$("#desplay_seq").textbox("setValue", selRow.desplay_seq);
	$("#reg_dt").textbox("setValue", selRow.reg_dt);
	
}

function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	$("#group_id").combobox({readonly : false});
	$("#dbid").combobox({readonly : false});
	
	$("#crud_flag").val("C");
	$("#db_name").val("");
	$("#group_nm").val("");
	
	$("#group_id").combobox("setValue", "");
	$("#dbid").combobox("setValue", "");
	$("#desplay_seq").textbox("setValue", "");
	$("#reg_dt").textbox("setValue", "");
	
}