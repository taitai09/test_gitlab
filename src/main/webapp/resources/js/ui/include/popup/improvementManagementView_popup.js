$(document).ready(function() {
	$('#processHistoryPop').window({
		title : "프로세스 처리이력",
		top:getWindowTop(450),
		left:getWindowLeft(680)
	});	

	$('#workTunerPop').window({
		title : "업무 담당자 조회",
		top:getWindowTop(350),
		left:getWindowLeft(400)
	});
	
	$('#perCheckPop').window({
		title : "사전점검 요청",
		top:getWindowTop(330),
		left:getWindowLeft(700)
	});	
	
	$('#deptTree').tree({
		url:"/Common/getWrkJobCd?wrkjob_cd="+loginUserWrkjobCd,
		method:"get",
		animate:true,
		lines:true,
		onClick: function(node){
			setUserAddTable(node.id);
		}
	});
	
	$("#processHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'tuning_status_changer_nm',title:'사용자',width:"20%",halign:"center",align:"center",sortable:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',width:"20%",halign:"center",align:'center'},
			{field:'tuning_status_change_dt',title:'변경일시',width:"20%",halign:"center",align:'center'},
			{field:'tuning_status_change_why',title:'변경사유',width:"40%",halign:"center",align:'left',formatter:getStringNewLine}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 			
	});
	
	$("#workUserList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			if(auth_cd == "ROLE_DEV"){
				$("#u_wrkjob_mgr_id").val(row.user_id);
				$("#u_wrkjob_mgr_nm").textbox('setValue',row.user_nm);
				$("#u_wrkjob_mgr_wrkjob_cd").val(row.wrkjob_cd);
				$("#u_wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
				$("#u_wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
				$("#wrkjob_mgr_nm").textbox('setValue',row.user_nm);
				$("#wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
				$("#wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
			}else{
//				eval("if_"+frameName).$("#u_wrkjob_mgr_id").val(row.user_id);
//				eval("if_"+frameName).$("#u_wrkjob_mgr_nm").textbox('setValue',row.user_nm);
//				eval("if_"+frameName).$("#u_wrkjob_mgr_wrkjob_cd").val(row.wrkjob_cd);
//				eval("if_"+frameName).$("#u_wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
//				eval("if_"+frameName).$("#u_wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
				$("#u_wrkjob_mgr_id").val(row.user_id);
				$("#u_wrkjob_mgr_nm").textbox('setValue',row.user_nm);
				$("#u_wrkjob_mgr_wrkjob_cd").val(row.wrkjob_cd);
				$("#u_wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
				$("#u_wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
				$("#wrkjob_mgr_id").val(row.user_id);
				$("#wrkjob_mgr_nm").textbox('setValue',row.user_nm);
				$("#wrkjob_mgr_wrkjob_cd").val(row.wrkjob_cd);
				$("#wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
				$("#wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
				
			}
			Btn_OnClosePopup('workTunerPop');
		},			
		columns:[[
			{field:'user_id',title:'사용자ID',width:"60%",halign:"center",align:'center',sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"50%",halign:"center",align:'center'},
			{field:'wrkjob_cd',title:'부서코드',width:"50%",halign:"center",align:'center'},
			{field:'wrkjob_nm',title:'업무명',width:"60%",halign:"center",align:'center'},
			{field:'ext_no',title:'연락처',width:"60%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
});

//function showPreCheckPopup(){
//	if($('#selectEnd').combobox('getValue') == ""){
//		$.messager.alert('','종료사유를 선택해 주세요.');
//		return false;
//	}
//	
//	if($("#tuning_end_why").val() == ""){
//		$.messager.alert('','종료 사유를 입력해 주세요.');
//		return false;
//	}
//
//	$("#tuning_end_why_cd").val($('#selectEnd').combobox('getValue'));	
//	
//	$("#chk_tuning_no").html($("#pop_tuning_no").val());
//	$("#chk_tuning_request_dt").html($("#pop_tuning_request_dt").val());
//	$("#chk_choice_div_cd_nm").html($("#pop_choice_div_cd_nm").val());
//
//	$('#perCheckPop').window("open");
//}

function setUserAddTable(strWrkJobCd){
	$('#workUserList').datagrid("loadData", []);
	$('#workUserList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#workUserList').datagrid('loading');
	
	ajaxCall("/Common/getUsers?wrkjob_cd="+strWrkJobCd,
			null,
			"GET",
			callback_getUsersAddTable);	
}

//callback 함수
var callback_getUsersAddTable = function(result) {	
	var data = JSON.parse(result);
	$('#workUserList').datagrid("loadData", data);
	$('#workUserList').datagrid('loaded');		
};

function Btn_SavePreCheck(){
	if($("#bfac_chk_source_text").val() == ""){
		$.messager.alert('','사전점검 소스 내용을 입력해 주세요.');
		return false;
	}
	console.log("frameName:"+frameName);
	//eval("if_"+frameName).$("#bfac_chk_source").val($("#bfac_chk_source_text").val());
	$("#bfac_chk_source").val($("#bfac_chk_source_text").val());
	
	//eval("if_"+frameName).ajaxCallSavePreCheck();	
	ajaxCallSavePreCheck();	
//	ajaxCall("/ImprovementManagement/SavePreCheck",
//			parent.$("#submit_form"),
//			"POST",
//			callback_SavePreCheckAction);	
}

//callback 함수
//var callback_SavePreCheckAction = function(result) {
//	console.log("result",result);
//	if(result.result){
//		$.messager.alert('','사전점검이 정상적으로 처리되었습니다.','info',function(){
//			setTimeout(function() {
//				Btn_GoList();
//			},1000);			
//		});
//	}else{
//		$.messager.alert('',result.message,'error');
//	}
//};