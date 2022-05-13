var check_first = 0;
var set_first = 0;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	getDeployPerfChkStep();
	Btn_OnClick();
	
	$("#perf_check_auto_exec_yn").combobox("textbox").attr("placeholder","선택");
	$("#del_yn").combobox("textbox").attr("placeholder","선택");
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	
	$('#step_ordering').textbox({
		onChange:function(newvalue, oldvalue){
			
			if(check_first == 0){
				$("#old_step_ordering").val(newvalue);
			}
			check_first += 1;
		},
	});
});

//changeList
function getDeployPerfChkStep(){
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'step_ordering',title:'단계순서',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_step_id',hidden:true},
			{field:'perf_check_step_nm',title:'검증단계',width:"22%",halign:"center",align:'left'},
			{field:'perf_check_auto_exec_yn',title:'성능검증자동 실행여부',width:"10%",halign:"center",align:'center'},
			{field:'perf_check_step_desc',title:'검증단계 설명',width:"38%",halign:"center",align:'left'},
			{field:'del_yn',title:'삭제여부',width:"10%",halign:"center",align:'center'}
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
	
		ajaxCall("/PerformanceCheckIndex/DeployPerfChkStep",
				$("#submit_form"),
				"POST",
				callback_DeployPerfChkStepAction);
}

//callback 함수
var callback_DeployPerfChkStepAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if($("#step_ordering").textbox('getValue') == ""){
		parent.$.messager.alert('','단계순서를 입력해 주세요.');
		return false;
	}

	if($("#step_ordering").textbox('getValue').length > 5){
		parent.$.messager.alert('','단계순서를 정확히 입력해주세요');
		return false;
	}
	
	if($("#perf_check_step_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','검증단계를 입력해 주세요.');
		return false;
	}
	
	if($("#perf_check_step_nm").textbox("getText").length > 100){
		parent.$.messager.alert('','검증단계의 내용을 100자 이내로 입력해주세요.');
		return false;
	}
	
	if($("#perf_check_auto_exec_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','성능검증자동 실행여부를 선택해 주세요.');
		return false;
	}
	
	if($("#del_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','삭제여부를 선택해 주세요.');
		return false;
	}	
	if($("#perf_check_step_desc").val() == ""){
		parent.$.messager.alert('','검증단계 설명을 입력해 주세요.');
		return false;
	}	
	if($("#perf_check_step_desc").val().length > 200){
		parent.$.messager.alert('','검증단계의 설명의 내용을 200자 이내로 입력해주세요.');
		return false;
	}
	
	ajaxCall("/PerformanceCheckIndex/DeployPerfChkStep/Save",
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
	check_first == 0
	
	$("#crud_flag").val("U");
	
	$("#perf_check_step_id").val(selRow.perf_check_step_id);
	$("#step_ordering").textbox("setValue",selRow.step_ordering);
	$("#old_step_ordering").val(selRow.step_ordering);
	$("#perf_check_step_nm").textbox("setValue", selRow.perf_check_step_nm);
	$("#perf_check_auto_exec_yn").combobox("setValue", selRow.perf_check_auto_exec_yn);
	$("#perf_check_step_desc").val(selRow.perf_check_step_desc);
	$("#del_yn").combobox("setValue",selRow.del_yn);
	$("#old_del_yn").val(selRow.del_yn);
}

function Btn_ResetField(){
	$("#step_ordering").textbox("setValue","");
	$("#old_step_ordering").val("");
	$("#crud_flag").val("C");
	$("#perf_check_step_id").val("");
	$("#perf_check_step_nm").textbox("setValue", "");
	$("#perf_check_auto_exec_yn").combobox("setValue", "");
	$("#perf_check_step_desc").val("");
	$("#del_yn").combobox("setValue", "");
	
	$("#tableList").datagrid("unselectAll");
}

function Excel_Download(){
	$("#submit_form").attr("action","/PerformanceCheckIndex/DeployPerfChkStep/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}