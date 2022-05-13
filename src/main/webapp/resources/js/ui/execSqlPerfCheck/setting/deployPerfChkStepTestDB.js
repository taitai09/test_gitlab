var check_first = 0;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#search_wrkjob_cd').combobox({
		url:"/Common/getWrkJob?deploy_check_target_yn=Y&isAll=Y",
		method:'get',
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	//업무
	$('#wrkjob_cd').combobox({
		url:"/Common/getWrkJob?deploy_check_target_yn=Y",
		method:'get',
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#wrkjob_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	//점검단계
	$('#perf_check_step_id').combobox({
		url:"/PerformanceCheckIndex/DeployPerfChkStep/DeployPerfChkStepId",
		method:'get',
		valueField:'perf_check_step_id',
		textField:'perf_check_step_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#perf_check_step_id').combobox("textbox").attr("placeholder","선택");
			$('#del_yn').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	// 검증평가방법
	$('#perf_check_evaluation_meth_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1091",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#perf_check_evaluation_meth_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	// Database 조회
	$('#dbid').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#dbid').combobox("textbox").attr("placeholder","선택");
		}
	});	
	
	// 프로그램
	$('#program_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1052",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$('#program_div_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	getDeployPerfChkStepTestDB();
	Btn_OnClick();
	
	$("#step_ordering").textbox({disabled:true});
	$("#perf_check_step_id").combobox({readonly:false});
	$("#wrkjob_cd").combobox({readonly:false});
	
});

//changeList
function getDeployPerfChkStepTestDB(){
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'wrkjob_cd',hidden:true},
			{field:'perf_check_step_id',hidden:true},
			{field:'program_div_cd',hidden:true},
			{field:'wrkjob_cd_nm', title:'업무',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_step_nm',title:'검증단계',width:"22%",halign:"center",align:'left'},
			{field:'step_ordering',title:'단계순서',width:"10%",halign:"center",align:'center'},
			{field:'del_yn',title:'삭제여부',width:"10%",halign:"center",align:'center'},
			{field:'dbid',hidden:true},
			{field:'db_name',title:'DB',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_program_div_cd_nm',title:'프로그램',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_evaluation_meth_nm',title:'검증평가방법',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_evaluation_meth_cd',hidden:true}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
};

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증단계 관리"," ");
	
		ajaxCall("/PerformanceCheckIndex/DeployPerfChkStepTestDB",
				$("#submit_form"),
				"POST",
				callback_DeployPerfChkStepTestDBAction);
}

//callback 함수
var callback_DeployPerfChkStepTestDBAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if($("#wrkjob_cd").combotree('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
	if($("#perf_check_step_id").combobox('getValue') == ""){
		parent.$.messager.alert('','점검단계를 선택해 주세요.');
		return false;
	}
	
	if($("#perf_check_evaluation_meth_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','검증평가방법을 선택해 주세요.');
		return false;
	}
	
	if($("#del_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','삭제여부를 선택해 주세요.');
		return false;
	}
	
	if($("#dbid").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#program_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','프로그램을 선택해 주세요.');
		return false;
	}
	ajaxCall("/PerformanceCheckIndex/DeployPerfChkStepTestDB/Save",
			$("#detail_form"),
			"POST",
			callback_SaveSettingAction);
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
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
	
	check_first = 0;
	
	$("#crud_flag").val("U");
	
	$("#step_ordering").textbox({disabled:false});
	$("#perf_check_step_id").combobox({readonly:true});
	$("#wrkjob_cd").combobox({readonly:true});
	
	$("#perf_check_step_id").combobox("setValue", selRow.perf_check_step_id);
	$("#wrkjob_cd").combobox("setValue", selRow.wrkjob_cd);
	$("#step_ordering").textbox("setValue", selRow.step_ordering);
	$("#del_yn").textbox("setValue", selRow.del_yn);
	$("#old_del_yn").val(selRow.del_yn);
	$("#perf_check_evaluation_meth_cd").combobox("setValue",selRow.perf_check_evaluation_meth_cd);
	$("#dbid").combobox("setValue",selRow.dbid);
	$("#program_div_cd").combobox("setValue",selRow.program_div_cd);
}

function Btn_ResetField(){
	$("#crud_flag").val("C");

	$("#perf_check_step_id").combobox("setValue", "");
	$("#program_div_cd").combobox("setValue", "");
	$("#wrkjob_cd").combobox("setValue", "");
	$("#step_ordering").textbox("setValue", "");
	$("#del_yn").textbox("setValue", "");
	$("#old_del_yn").val("");
	$("#perf_check_evaluation_meth_cd").combobox("setValue", "");
	$("#dbid").combobox("setValue", "");
	
	$("#step_ordering").textbox({disabled:true});
	$("#perf_check_step_id").combobox({readonly:false});
	$("#wrkjob_cd").combobox({readonly:false});
	
	$("#tableList").datagrid("unselectAll");
}

function Excel_Download(){
	$("#submit_form").attr("action","/PerformanceCheckIndex/DeployPerfChkStepTestDB/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}