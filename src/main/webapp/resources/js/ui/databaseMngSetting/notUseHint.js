$(document).ready(function() {
	$("body").css("visibility", "visible");

	$('#selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onChange : function(newValue, oldValue){
			selectDbAbbrNm(newValue, oldValue); 
			
			if(newValue != ""){
				$("."+newValue).show();	
			}
		},
		onLoadError: function(items){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess:function(){
			$('#selectDbid').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$('#searchKey').combobox({
		onChange : function(newValue, oldValue) {
			$('#searchValue').textbox('setValue', '');
			
			if(newValue == '') {
				$('#searchValue').textbox('readonly', true);
			} else {
				$('#searchValue').textbox('readonly', false);
			}
		},
		onLoadSuccess : function() {
			$('#searchValue').textbox('readonly', true);
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_getHintList();
	   }
	});	
	$('#hintList').datagrid('loadData',[]);
	$("#hintList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			console.log("row",row);
			setHintList(row);
		},		
		columns:[[
			{field:'dbid',title:'DBID',width:"11%",halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB',width:"11%",halign:"center",align:'left'},			
			{field:'db_abbr_nm',title:'DB 약어명',width:"11%",halign:"center",align:'center'},
			{field:'hint_nm',title:'힌트명',width:"31%",halign:"center",align:'center'},
			{field:'hint_reg_dt',title:'등록날짜',width:"21%",halign:"center",align:'center'},
			{field:'hint_reg_id',title:'등록아이디',width:"15%",halign:"center",align:'center'}
		]],
    	fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
//	$("#detail_form #db_abbr_nm").textbox({disabled: true});
	$("#detail_form #hint_reg_dt").textbox({disabled: true});
	$("#detail_form #hint_reg_id").textbox({disabled: true});
});


function selectDbAbbrNm(new_dbid, old_dbid){
	
	$("#left_form #dbid").val(new_dbid);
	$("#detail_form #old_dbid").val(old_dbid);
	$("#detail_form #dbid").val(new_dbid);


	ajaxCall("/DBMng/getDbAbbrNm",
			$("#left_form"),
			"POST",
			callback_selectDbAbbrNmAction);	
};

//callback 함수
//result=string, db_abbr_nm
var callback_selectDbAbbrNmAction = function(result) {
	$('#db_abbr_nm').textbox("setValue", result);
};

function Btn_getHintList(){
	
//	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
//		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
//		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
//		return false;
//	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#hintList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터베이스 힌트 관리"," ");	

	ajaxCall("/DBMng/getHintList",
			$("#left_form"),
			"POST",
			callback_getHintListAction);		
}

	
//callback 함수
var callback_getHintListAction = function(result) {

	var data = JSON.parse(result);
	
	$('#hintList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};


function setHintList(selRow){
	$("#detail_form #dbid").val(selRow.dbid);
	$("#detail_form #selectDbid").combobox("setValue",selRow.dbid);
	$("#detail_form #db_name").textbox("setValue",selRow.db_name);
	$("#detail_form #db_abbr_nm").textbox("setValue",selRow.db_abbr_nm);
	$("#detail_form #hint_nm").textbox("setValue",selRow.hint_nm);
	$("#detail_form #hint_reg_dt").textbox("setValue",selRow.hint_reg_dt);
	$("#detail_form #hint_reg_id").textbox("setValue",selRow.hint_reg_id);
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #old_hint_nm").val(selRow.hint_nm);
	$("#detail_form #old_dbid").val(selRow.dbid);
//	$("#detail_form #dbid").combobox({disabled: true});
//	$("#detail_form #dbid").combobox({readonly:true});
//	$("#detail_form #db_abbr_nm").textbox({disabled: true});
//	$("#detail_form #hint_reg_dt").textbox({disabled: true});
//	$("#detail_form #hint_reg_id").textbox({disabled: true});
//	$("#detail_form #hint_nm").textbox({disabled: true});
	$("#detail_form #db_abbr_nm").textbox({readonly:true});
	$("#detail_form #hint_nm").textbox({readonly:false});
	$("#detail_form #hint_reg_dt").textbox({readonly:true});
	$("#detail_form #hint_reg_id").textbox({readonly:true});


}



function Btn_deleteHint(){
	var data = $("#hintList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	$("#detail_form #dbid").val($("#selectDbid").combobox('getValue'));  //id의 dbid 가 두개여서 값을 옮김.
	
	parent.$.messager.confirm('', '[ 힌트명 : ' + data.hint_nm + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/DBMng/deleteHint", 
					$("#detail_form"), 
					"POST",
					callback_deleteHint);
		}
	});
	
}

var callback_deleteHint = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제 되었습니다.','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_getHintList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_saveHint(){
	var crud_flag = $("#detail_form #crud_flag").val();
	
	
	if($("#selectDbid").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	let hintNm = $("#hint_nm").textbox('getValue');
	var blank_pattern = /^\s+|\s+$/g;
	
	if(hintNm == "" || hintNm.replace( blank_pattern, '' ) == ""){
		parent.$.messager.alert('','힌트명을 입력해주세요.');
		return false;
	}
	
	if(byteLength(hintNm) > 1000) {
		parent.$.messager.alert('','힌트명 정보가 1000 Byte를 초과 하였습니다.');
		return false;
	}
	
	$("#hint_nm").textbox('setValue', hintNm.trim());
	
//	if(crud_flag == 'U'){
//		parent.$.messager.confirm('','\''+$("#hint_nm").textbox('getValue')+'\''+' 힌트를 정말로 수정하시겠습니까?',function(check){
//			if(check){
//				ajaxCall("/deleteMenuInfo",
//						$("#detail_form"),
//						"POST",
//						callback_deleteMenuInfo);			
//			}
//		});
//	}

	ajaxCall("/DBMng/saveHint",
			$("#detail_form"),
			"POST",
			callback_saveHint);		
}
//callback 함수
var callback_saveHint = function(result) {
//	var data = JSON.parse(result);
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_getHintList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};




function Btn_ResetField(){
	$("#hintList").datagrid("clearSelections");
	$("#detail_form #dbid").val("");
	$("#detail_form #selectDbid").combobox("setValue","");
	$("#detail_form #db_abbr_nm").textbox("setValue","DB를 선택해주세요");
	$("#detail_form #hint_nm").textbox("setValue","");
	$("#detail_form #hint_reg_dt").textbox("setValue","자동으로 등록됩니다");
	$("#detail_form #hint_reg_id").textbox("setValue","");
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #hint_reg_id").textbox("setValue",$('#user_id').val());
//	$("#detail_form #db_abbr_nm").textbox({disabled: true});
	$("#detail_form #hint_reg_dt").textbox({disabled: true});
	$("#detail_form #hint_reg_id").textbox({disabled: true});
	$("#detail_form #hint_nm").textbox({readonly:false});

//	$("#detail_form #hint_reg_id").textbox("setValue","");
	
//	for(var i = 0; i < 4; i++){
//		$("input[id='auth_id"+i+"']:checkbox").removeProp("checked");
//	}
	//	$("#detail_form #use_yn").combobox("setValue", "");	
}
