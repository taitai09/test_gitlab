var apm_operate_type_cd_nm = "";
var wrkjob_cd_nm = "";

$(document).ready(function() {

	$("body").css("visibility", "visible");

//	$('#upper_wrkjob_cd').combotree({
//	    url:"/Common/getWrkJobCd",
//	    method:'get'
//	});
//	
	
	$('#select_wrkjob_cd').combobox({
		url:"/getOnlyWrkJobCd?isChoice=N",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onSelect : function(rec){
			selectWrkjobCd(rec); 
		},
//    	onChange : function(newValue, oldValue){
//	    	changeWrkjobCd(newValue, oldValue); 
//	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#select_wrkjob_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
//	
//	// 업무구분 조회			
//	$('#select_wrkjob_cd').combobox({
//	    url:"/Common/getWrkJob",
//	    method:"get",
//		valueField:'wrkjob_cd',
//	    textField:'wrkjob_cd_nm',
//    	onSelect : function(rec){
//	    	selectWrkjobCd(rec); 
//	    },
//		onLoadError: function(){
//			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
//			return false;
//		},
//
//	});
	
	
//	$('#select_apm_operate_type_cd').combobox({
//		url : "/selectApmOperateType",
//		method:"get",
//		valueField:'apm_operate_type_cd',
//	    textField:'apm_operate_type_cd_nm',
//    	onSelect : function(rec){
//    		selectApmOperateType(rec); 
//    	},
//    	onChange : function(newValue, oldValue){
//    		changeApmOperateType(newValue, oldValue); 
//    		
////    		$("#apm_operate_type_cd").val(newValue);
//	    	
//	    },
//		onLoadError: function(){
//			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
//			return false;
//		},
//	});
	
	// APM운영유형
	$('#select_apm_operate_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1027&isChoice=N",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(rec){
    		selectApmOperateType(rec); 
    	},
    	onChange : function(newValue, oldValue){
    		changeApmOperateType(newValue, oldValue); 
    		
//    		$("#apm_operate_type_cd").val(newValue);
	    	
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#select_apm_operate_type_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_apmList();
	   }
	});	
	
	
	$('#apmList').datagrid('loadData',[]);
	$("#apmList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
//			var wrkjob_cd = $("#detail_form #select_wrkjob_cd").combobox("getValue");
//			$("#detail_form #old_wrkjob_cd").val(wrkjob_cd);
			
		},		
		columns:[[
			{field:'wrkjob_cd',title:'업무코드',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'wrkjob_cd_nm',title:'업무명',width:"14%",halign:"center",align:'left'},			
			{field:'apm_operate_type_cd',hidden:true},
			{field:'apm_operate_type_cd_nm',title:'APM운영유형',width:"14%",halign:"center",align:'center'},
			{field:'db_connect_ip',title:'DB접속IP',width:"26%",halign:"center",align:'center'},
			{field:'db_connect_port',title:'DB접속PORT',width:"18%",halign:"center",align:'center'},
			{field:'db_user_id',title:'DB유저아이디',width:"15%",halign:"center",align:'center'}
		]],
    	fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#searchValue').textbox({readonly:true});

	$('#searchKey').combobox({
		onChange : function(newValue,oldValue){
			if(newValue == "") {
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else{
				$('#searchValue').textbox({readonly:false});
			}
		}
	});
	
	
});

//function changeWrkjobCd(newValue, oldValue){
//	var crud_flag = $("#detail_form #crud_flag").val();
//	
//	if(crud_flag==="U"){
//		parent.$.messager.alert('','테이블 수정시 업무명은 변경할 수 없습니다.');
////		backToOldWrkjobCd(oldValue);
//	}
//};
function backToOldWrkjobCd(oldValue){
		$("#detail_form #select_wrkjob_cd").textbox("setValue", oldValue);
	
};

function selectWrkjobCd(rec){
	$("#detail_form #wrkjob_cd_nm").val(rec.wrkjob_cd_nm);
	$("#detail_form #wrkjob_cd").val(rec.wrkjob_cd);
};
function changeApmOperateType(newValue, oldValue){
	$("#detail_form #old_apm_operate_type_cd").val(oldValue);

};

function selectApmOperateType(rec){
	$("#detail_form #apm_operate_type_cd_nm").val(rec.cd_nm);
	$("#detail_form #old_apm_operate_type_cd").val(rec.cd);
	
	var crud_flag = $("#detail_form #crud_flag").val();
	if(crud_flag === "U"){
		$("#detail_form #updateIsAll").val("N");
	}
	
	
	
};

function Btn_apmList(){
	
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#apmList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터베이스 APM 관리"," ");	

	ajaxCall("/getApmList",
			$("#left_form"),
			"POST",
			callback_apmListAction);		
}

	
//callback 함수
var callback_apmListAction = function(result) {

	var data = JSON.parse(result);
	
	$('#apmList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};


function setDetailView(selRow){  //클릭시

	apm_operate_type_cd_nm = selRow.apm_operate_type_cd_nm;
	wrkjob_cd_nm = selRow.wrkjob_cd_nm;
	$("#detail_form #apm_operate_type_cd_nm").val(selRow.apm_operate_type_cd_nm);
	$("#detail_form #wrkjob_cd_nm").val(selRow.wrkjob_cd_nm);
	
	$("#detail_form #select_wrkjob_cd").combobox("setValue",selRow.wrkjob_cd);
	$("#detail_form #wrkjob_cd").val(selRow.wrkjob_cd);
	$("#detail_form #select_apm_operate_type_cd").combobox("setValue", selRow.apm_operate_type_cd);
	$("#detail_form #old_apm_operate_type_cd").val(selRow.apm_operate_type_cd);
	
	$("#detail_form #db_connect_ip").textbox("setValue",selRow.db_connect_ip);
	$("#detail_form #db_connect_port").textbox("setValue",selRow.db_connect_port);
	$("#detail_form #db_user_id").textbox("setValue",selRow.db_user_id);
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #updateIsAll").val("Y");
	
	$("#detail_form #select_wrkjob_cd").textbox({disabled: true});
	

}



function Btn_deleteAPM(){
	
//	var wrkjob_cd_nm = $("#detail_form #select_wrkjob_cd").combobox("getText");
//	var apm_operate_type_cd_nm = $("#detail_form #select_apm_operate_type_cd").combobox("getText");
//	var wrkjob_cd = $("#detail_form #wrkjob_cd").val();

	if(wrkjob_cd_nm == '' || apm_operate_type_cd_nm == ''){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 업무명 : ' +wrkjob_cd_nm + ', 운영유형 : '+apm_operate_type_cd_nm+ ' ]' + ' 을(를) 삭제하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deletApmConnection", 
					$("#detail_form"), 
					"POST",
					callback_deleteApmAction);
		}
	});
	
}

var callback_deleteApmAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제 되었습니다','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_apmList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_saveAPM(){
	var crud_flag = $("#detail_form #crud_flag").val();
	
	if($("#select_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if($("#select_apm_operate_type_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','APM운영유형 코드를 입력해주세요.');
		return false;
	}
//	if($("#db_connect_ip").textbox('getValue') == ""){
//		parent.$.messager.alert('','DB접속IP를 입력해주세요.');
//		return false;
//	}
	if(!$("#db_connect_ip").textbox('getValue')){
		parent.$.messager.alert('','DB 접속IP를 입력해주세요.');
		return false;
	}
	if(checkIdSpecial($("#db_connect_ip").textbox('getValue')) == -1){
		parent.$.messager.alert('','DB 접속IP는 한글 및 특수문자는 입력할 수 없습니다.');
		return false;
	}
	if($("#db_connect_ip").textbox('getValue').length > 25){
		parent.$.messager.alert('','DB 접속IP의 길이를 25자 이내로 입력해 주세요.');
		return false;
	}
//	if($("#db_connect_port").textbox('getValue') == ""){
//		parent.$.messager.alert('','DB접속PORT를 입력해주세요.');
//		return false;
//	}
//	if($("#db_user_id").textbox('getValue') == ""){
//		parent.$.messager.alert('','유저아이디를 입력해주세요.');
//		return false;
//	}
	if(!$("#db_user_id").textbox('getValue')){
		parent.$.messager.alert('','DB 유저아이디를 입력해주세요.');
		return false;
	}

	if(checkIdSpecial($("#db_user_id").textbox('getValue')) == -1){
		parent.$.messager.alert('','DB 유저아이디는 한글 및 특수문자는 입력할 수 없습니다.');
		return false;
	}
	if($("#db_user_id").textbox('getValue').length > 30){
		parent.$.messager.alert('','DB 유저아이디의 길이를 30자 이내로 입력해 주세요.');
		return false;
	}

	
	ajaxCall("/saveApmConnection",
			$("#detail_form"),
			"POST",
			callback_saveApmAction);		
}
//callback 함수
var callback_saveApmAction = function(result) {
//	var data = JSON.parse(result);
	if(result.result){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_apmList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};



function Btn_ResetField(){
	apm_operate_type_cd_nm = "";
	wrkjob_cd_nm = "";
	
	$("#detail_form #select_apm_operate_type_cd").combobox("setValue","");
	$("#detail_form #select_wrkjob_cd").combobox("setValue","");
	$("#detail_form #wrkjob_cd").val("");
	$("#detail_form #apm_operate_type_cd").val("");
	$("#detail_form #wrkjob_cd").val("");

	$("#detail_form #old_apm_operate_type_cd").val("");
	$("#detail_form #apm_operate_type_cd_nm").val("");
	$("#detail_form #wrkjob_cd_nm").val("");
	$("#detail_form #updateIsAll").val("Y");

	$("#detail_form #db_connect_ip").textbox("setValue","");
	$("#detail_form #db_connect_port").textbox("setValue","");
	$("#detail_form #db_user_id").textbox("setValue","");
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #select_wrkjob_cd").textbox({disabled: false});
	
	$("#apmList").datagrid('clearSelections');

}
