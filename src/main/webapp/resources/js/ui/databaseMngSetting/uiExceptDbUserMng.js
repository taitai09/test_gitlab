	
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	Btn_OnClick();
	
	$('#searchKey').combobox({
		onChange : function(newValue, oldValue) {
			$('#searchValue').textbox('setValue', '');
			
			if(newValue == '') {
				$('#searchValue').textbox('readonly', true);
			} else {
				$('#searchValue').textbox('readonly', false);
			}
		},
		onLoadSuccess : function(items) {
				$('#searchValue').textbox('readonly', true);
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
});


function getUsernameList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'username',title:'아이디',width:"50%",halign:"center",align:"center",sortable:"true"}
			]],
//	    	fitColumns:true,
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("작업스케쥴러 종속관계 관리"," ");
	
		ajaxCall("/DBMng/getUsernameList",
				$("#submit_form"),
				"POST",
				callback_getUsernameListAction);
};

//callback 함수
var callback_getUsernameListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getUsernameList();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
//	var rows = $("#tableList").datagrid('getRows');
	
	if(byteLength($('#username').textbox('getValue')) > 128) {
		parent.$.messager.alert('','아이디 정보가 128 Byte를 초과 하였습니다.');
		return false;
	}
	
	if($("#crud_flag").val() == "C"){
		$("#old_username").val($("#username").val());
	};
	
		if($("#username").textbox('getValue') == ""){
			parent.$.messager.alert('','아이디를 입력해 주세요.');
			return false;
		}
			
			ajaxCall("/DBMng/saveUsername",
					$("#detail_form"),
					"POST",
					callback_SaveSettingAction);		
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if($("#crud_flag").val() == "U") {
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
		parent.$.messager.alert('',result.message,'error');
	}
};






function Btn_DeleteSetting(){
	var data = $('#tableList').datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}

	parent.$.messager.confirm('', '[ 아이디 : ' + data.username + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
				
			$("#username").textbox("setValue",data.username);
				ajaxCall("/DBMng/deleteUsername", 
						$("#detail_form"), 
						"POST",
						callback_deleteSettingAction);
			}
		});
	

};

//callback 함수
var callback_deleteSettingAction = function(result) {
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
	
//	var row = $("#tableList").datagrid('getSelections');

	$("#crud_flag").val("U");
	$("#old_username").val(selRow.username);
	$("#username").textbox("textbox").prop('placeholder', '추가하시려면 초기화를 눌러주세요.');

	$("#username").textbox("setValue", selRow.username);
	
};

function Btn_ResetField(){
	$('#tableList').datagrid('clearSelections');
	$("#username").textbox("textbox").prop('placeholder', '');
	$("#crud_flag").val("C");
	$("#old_username").val("");
	$("#username").textbox("setValue" , "");
};
	
