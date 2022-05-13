$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	//업무
	$('#upper_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onSelect:function(val){
			$("#search_wrkjob_cd").val(val.id);
		},
		onLoadSuccess: function() {
			$("#upper_wrkjob_cd").combobox("textbox").attr("placeholder","선택");
			$('#pass_max_value').textbox({disabled:true});
		}
	});
	
	//적합
	$('#pass_max_value').textbox({
		onChange:function(val){
			if(val < 0){
				$('#pass_max_value').textbox("setValue","");
				$('#not_pass_pass').textbox("setValue","");
				parent.$.messager.alert('','0 이하는 입력할 수 없습니다.');
			}
			
			$('#not_pass_pass').textbox("setValue",val + " 초과");
			
			if($('#pass_max_value').textbox("getValue") == ''){
				$('#not_pass_pass').textbox("setValue",''); 
			};
			
		}
	});
	
	//지표여부값판정구분코드
	$('#yn_decide_div_cd').combobox({
		url:"/PerformanceCheckIndex/DeployPrefChkIndc/getYnDecideDivCd",
		method:'get',
		valueField:'yn_decide_div_cd',
		textField:'yn_decide_div_cd_nm',
		onLoadSuccess: function() {
			$("#yn_decide_div_cd").combobox("textbox").attr("placeholder","선택");
			$("#indc_apply_yn").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	//성능검증지표ID
	$('#perf_check_indc_id').combobox({
		url:"/PerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckIndcId",
		method:'get',
		valueField:'perf_check_indc_id',
		textField:'perf_check_indc_nm'
	});
	
	$('#temp_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd",
		method:'get',
		valueField:'id',
		textField:'text'
	});
	
	//프로그램
	$('#perf_check_program_div_cd').combobox({
		url:"/PerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckProgramDivCd",
		method:'get',
		valueField:'perf_check_program_div_cd',
		textField:'perf_check_program_div_cd_nm',
		onChange:function(){
			
			ajaxCall("/PerformanceCheckIndex/WjPerfChkIndc/Check",
					$("#detail_form"),
					"POST",
					callback_CheckPkAction);
		},
		onLoadSuccess: function() {
			$("#perf_check_program_div_cd").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	getWjPerfChkIndc();
	
	$('#detail_form #perf_check_meth_cd').combobox({
		url:"/PerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckMethCd",
		method:"get",
		valueField:'perf_check_meth_cd',
		textField:'perf_check_meth_cd_nm',
		onChange:function(val){
			if(val == 1){ //범위
				$('#yn_decide_div_cd').combobox("setValue",'');
				$('#yn_decide_div_cd').combobox({disabled:true});
				$('#not_pass_pass').textbox({disabled:false});
				$('#pass_max_value').textbox({disabled:false}); //활성화
								
			}else{ //여부
				$('#pass_max_value').textbox("setValue",'');
				$('#pass_max_value').textbox({disabled:true});
				$('#not_pass_pass').textbox({disabled:true});
				$('#yn_decide_div_cd').combobox({disabled:false});  //활성화
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	
	$('#search_indc_apply_yn').combobox({
		onSelect:function(val){
			$("#submit_form #indc_apply_yn").val(val.value);
		},
	});
});

//changeList
function getWjPerfChkIndc(){
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'reg_div',title:'등록구분',width:"4%",halign:"center",align:"center",sortable:"true"},
			{field:'wrkjob_cd',hidden:true},
			{field:'wrkjob_cd_nm',title:'업무',width:"7%",halign:"center",align:'center'},
			{field:'perf_check_indc_id',hidden:true},
			{field:'perf_check_indc_nm',title:'검증지표',width:"10%",halign:"center",align:'left'},
			{field:'perf_check_program_div_cd',hidden:true},
			{field:'perf_check_program_div_cd_nm',title:'프로그램',width:"7%",halign:"center",align:'center'},
			{field:'perf_check_meth_cd',hidden:true},	
			{field:'perf_check_meth_cd_nm',title:'검증방법',width:"4%",halign:"center",align:'center'},
			{field:'pass_max_value',title:'적합',width:"7%",halign:"center",align:'center'},
			{field:'not_pass_pass',title:'부적합',width:"7%",halign:"center",align:'center'},
			{field:'yn_decide_div_cd',hidden:true},
			{field:'yn_decide_div_cd_nm',title:'여부값 판정구분',width:"7%",halign:"center",align:'center'},
			{field:'indc_apply_yn',title:'지표적용여부',width:"4%",halign:"center",align:'center'},
			{field:'perf_check_indc_desc',title:'지표 설명',width:"26%",halign:"center",align:'left'},
			{field:'perf_check_fail_guide_sbst',title:'부적합가이드',width:"26%",halign:"center",align:'left'},
			{field:'update_dt',title:'변경일시',width:"8%",halign:"center",align:'center'}
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
	if($("#upper_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해주세요.');
		return false;
	}
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("업무별 성능 검증지표 기준값 관리"," ");
		
	ajaxCall("/PerformanceCheckIndex/WjPerfChkIndc",
			$("#submit_form"),
			"POST",
			callback_WjPerfChkIndcAction);
}

//callback 함수
var callback_WjPerfChkIndcAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if($("#temp_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
	if($("#perf_check_indc_id").combobox('getValue') == ""){
		parent.$.messager.alert('','검증지표를 선택해 주세요.');
		return false;
	}
	
	if($("#perf_check_program_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','프로그램을 선택해 주세요.');
		return false;
	}	
	if($("#perf_check_meth_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','검증방법을 선택해 주세요.');
		return false;
	}	
	
	
	if($("#perf_check_meth_cd").combobox('getValue') == 1){ //범위
		if($("#pass_max_value").textbox('getValue') == ""){
			parent.$.messager.alert('','적합을 입력해 주세요.');
			return false;
		}
 
	}else{ //여부
		if($("#yn_decide_div_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','여부값 판정구분을 선택해주세요.');
			return false;
		}
	}
	
	if($("#indc_apply_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','지표적용여부를 선택해 주세요.');
		return false;
	}
	
	if($("#pass_max_value").val().length > 20){
		parent.$.messager.alert('','적합의 내용을 20자 이내로 입력해주세요.');
		return false;
	}
	
	savekWjPerfChkIndc();
};

//callback 함수
var callback_CheckPkAction = function(result) {
	if(result.result){  //true 라면 중복안됨
		$("#crud_flag").val("C");
		$("#Btn_SaveSetting").html("");
		$("#Btn_SaveSetting").html(" 등록");

	}else{ //false 라면 중복됨 그래서 업데이트 
		$("#crud_flag").val("U");
		$("#Btn_SaveSetting").html("");
		$("#Btn_SaveSetting").html(" 수정");
	}
};

function savekWjPerfChkIndc(){
	ajaxCall("/PerformanceCheckIndex/WjPerfChkIndc/Save",
			$("#detail_form"),
			"POST",
			callback_SaveSettingAction);
};

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
				Btn_ResetAllField();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};
function Btn_DeleteSetting(){
	if($("#temp_wrkjob_cd").combobox("getValue") ==='' || $("#temp_wrkjob_cd").combobox("getValue") === null ){
		parent.$.messager.alert('','삭제할 데이터가 없습니다.');
	}else{
		
		parent.$.messager.confirm('', '[ 검증지표 : ' + $("#perf_check_indc_id").combobox("getText")+ ', <br/>프로그램 : '+ $("#perf_check_program_div_cd").combobox("getText")+' ]' + ' 을(를)<br/>삭제 하시겠습니까?', function(check) {
			if (check) {
				
				ajaxCall("/PerformanceCheckIndex/WjPerfChkIndc/Delete", 
						$("#detail_form"), 
						"POST",
						callback_DeleteSettingAction);
			}
		});
	}
}

//callback 함수
var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','[ 검증지표 : ' + $("#perf_check_indc_id").combobox("getText")+ ', <br/>프로그램 : '+ $("#perf_check_program_div_cd").combobox("getText")+' ]'+' 이(가) 성공적으로 삭제되었습니다.','info',function(){
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
	$("#detail_form #not_pass_pass").textbox({disabled: false});
	$("#detail_form #perf_check_indc_desc").attr("disabled",false);
	$("#detail_form #perf_check_fail_guide_sbst").attr("disabled",false);
	
	$("#crud_flag").val("U");
	
	$("#temp_wrkjob_cd").combotree("setValue", selRow.wrkjob_cd);
	$("#perf_check_indc_id").combobox("setValue", selRow.perf_check_indc_id);
	$("#perf_check_program_div_cd").combobox("setValue", selRow.perf_check_program_div_cd);
	$("#perf_check_meth_cd").combobox("setValue", selRow.perf_check_meth_cd);
	$("#yn_decide_div_cd").combobox("setValue", selRow.yn_decide_div_cd);
	$("#detail_form #indc_apply_yn").combobox("setValue", selRow.indc_apply_yn);
	$("#pass_max_value").textbox("setValue", selRow.pass_max_value);
	$("#not_pass_pass").textbox("setValue", selRow.not_pass_pass);
	$("#perf_check_fail_guide_sbst").val(selRow.perf_check_fail_guide_sbst);
	$("#perf_check_indc_desc").val(selRow.perf_check_indc_desc);
	
	if(	$("#perf_check_program_div_cd").combobox("getValue") != ''){
		$("#Btn_SaveSetting").html("");
		$("#Btn_SaveSetting").html(" 수정");
	}else{
		$("#Btn_SaveSetting").html("");
		$("#Btn_SaveSetting").html(" 등록");
	}
}

function Btn_ResetField(){
	$("#crud_flag").val("C");
	
	$("#detail_form #indc_apply_yn").combobox("setValue", "");
	$("#perf_check_program_div_cd").combobox("setValue","" );
	$("#perf_check_program_div_cd").combobox("setText","선택");
	$("#pass_max_value").textbox("setValue","");
	
	$("#Btn_SaveSetting").html("");
	$("#Btn_SaveSetting").html(" 등록");
}
function Btn_ResetAllField(){
	
	$("#temp_wrkjob_cd").combotree("setValue","");
	$("#perf_check_indc_id").combobox("setValue","");
	$("#perf_check_program_div_cd").combobox("setValue","");
	$("#perf_check_meth_cd").combobox("setValue","");
	$("#pass_max_value").textbox("setValue","");
	
	$("#yn_decide_div_cd").combobox("setValue","");
	$("#indc_apply_yn").combobox("setValue", "");
	
	$("#detail_form #perf_check_indc_desc").val("");
	$("#detail_form #perf_check_fail_guide_sbst").val("");
	
	$("#tableList").datagrid("unselectAll");
}

function Excel_Download(){
	$("#submit_form").attr("action","/PerformanceCheckIndex/WjPerfChkIndc/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}