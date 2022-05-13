$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
	
	$('#board_type_cd').combobox({
		url:"/Config/getBoardTypeCd",
		method:"get",
		valueField:'board_type_cd',
		textField:'board_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#detail_form #board_type_cd').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #file_add_yn').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #comment_use_yn').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #board_use_yn').combobox('textbox').attr("placeholder","선택");
		},
	});
	
	Btn_OnClick();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	initDetailTextbox();
});

function initDetailTextbox() {
	$("#detail_form #reg_dt").textbox({readonly:true});
	$("#detail_form #upd_dt").textbox({readonly:true});
	$("#detail_form #upd_id").textbox({readonly:true});
	$("#detail_form #reg_id").textbox({readonly:true});
	$("#detail_form #upd_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #upd_dt").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #reg_dt").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #reg_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
}

//changeList
function getBoardMngList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'board_mgmt_no',title:'게시판관리번호',width:"8%",halign:"center",align:"center",sortable:"true"},
//				{field:'board_type_cd',hidden:true}, 
				{field:'board_type_nm',title:'게시판타입',width:"16%",halign:"center",align:'center'},
				{field:'board_nm',title:'게시판이름',width:"16%",halign:"center",align:'center'},
				{field:'file_add_yn',title:'첨부파일사용여부',width:"8%",halign:"center",align:'center'},
				{field:'comment_use_yn',title:'댓글사용여부',width:"8%",halign:"center",align:'center'},
				{field:'board_use_yn',title:'게시판사용여부',width:"8%",halign:"center",align:'center'},
				{field:'reg_dt',title:'등록일시',width:"16%",halign:"center",align:'center'},
				{field:'reg_id',title:'등록자ID',width:"8%",halign:"center",align:'center'},
				{field:'upd_dt',title:'수정일시',width:"16%",halign:"center",align:'center'},
				{field:'upd_id',title:'수정자ID',width:"8%",halign:"center",align:'center'}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케쥴러 설정 기본"," ");
	
		ajaxCall("/Config/BoardSetting",
				$("#submit_form"),
				"POST",
				callback_BoardMngListAction);
}

//callback 함수
var callback_BoardMngListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getBoardMngList();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
		if($("#detail_form #board_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','게시판 관리 이름을 입력해주세요.');
			return false;
		}
		if($("#detail_form #file_add_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','첨부파일 사용여부를 입력해주세요');
			return false;
		}
		
		if($("#detail_form #comment_use_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','댓글 사용여부를 입력해주세요');
			return false;
		}
		
		if($("#detail_form #board_use_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','게시판 사용여부를 입력해주세요');
			return false;
		}	
		
	
		if($("#crud_flag").val() == 'C'){
			$("#reg_id").textbox("setValue", $("#user_id").val());
			$("#board_nm").val($("#board_type_cd").combobox("getText"));
			
		}else{
			$("#upd_id").textbox("setValue", $("#user_id").val());
			$("#board_nm").val($("#board_type_cd").combobox("getText"));
		}

		
		ajaxCall("/Config/BoardSetting/Save",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "";
	
	if($("#crud_flag").val() == 'C') {
		message = "저장 되었습니다.";
	} else {
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
	var data = $("#tableList").datagrid("getSelected");
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 게시판관리번호 ' + data.board_mgmt_no + '번  '+ data.board_nm +' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			
			ajaxCall("/Config/BoardSetting/Delete", 
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
	
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #board_nm").textbox("setValue",selRow.board_nm);
	$("#detail_form #board_mgmt_no").val(selRow.board_mgmt_no);
	$("#detail_form #board_type_cd").combobox("setValue", selRow.board_type_cd);
	$("#detail_form #file_add_yn").combobox("setValue", selRow.file_add_yn);
	$("#detail_form #comment_use_yn").combobox("setValue", selRow.comment_use_yn);
	$("#detail_form #board_use_yn").combobox("setValue", selRow.board_use_yn);
	$("#detail_form #reg_dt").textbox("setValue", selRow.reg_dt);
	$("#detail_form #reg_id").textbox("setValue", selRow.reg_id);
	$("#detail_form #upd_dt").textbox("setValue", selRow.upd_dt);
	$("#detail_form #upd_id").textbox("setValue", selRow.upd_id);
	$("#detail_form #reg_dt").textbox({disabled: true});
	$("#detail_form #reg_id").textbox({disabled: true});
	$("#detail_form #upd_dt").textbox({disabled: true});
	$("#detail_form #upd_id").textbox({disabled: true});
}

function Btn_ResetField(){
	$("#tableList").datagrid("clearSelections");
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #board_nm").textbox("setValue","");
	$("#detail_form #board_mgmt_no").val("");
	$("#detail_form #board_type_cd").combobox("setValue", "");
	$("#detail_form #file_add_yn").combobox("setValue", "");
	$("#detail_form #comment_use_yn").combobox("setValue", "");
	$("#detail_form #board_use_yn").combobox("setValue",  "");
	$("#detail_form #reg_dt").textbox("setValue", "");
	$("#detail_form #reg_id").textbox("setValue", "");
	$("#detail_form #upd_dt").textbox("setValue", "");
	$("#detail_form #upd_id").textbox("setValue", "");
	$("#detail_form #reg_dt").textbox({disabled: false});
	$("#detail_form #reg_id").textbox({disabled: false});
	$("#detail_form #upd_dt").textbox({disabled: false});
	$("#detail_form #upd_id").textbox({disabled: false});
	initDetailTextbox();
}