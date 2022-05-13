var check_first = 0;
var set_first = 0;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#detail_form #perf_check_meth_cd').combobox({
	    url:"/PerformanceCheckIndex/DeployPrefChkIndc/getPerfCheckMethCd",
	    method:"get",
		valueField:'perf_check_meth_cd',
	    textField:'perf_check_meth_cd_nm',
	    onChange: function(newval, oldval){
	    	
	    	if(check_first == 1){  //처음 선택하고 변경시에만 올드 값을 넣어줌.
		    	$("#old_perf_check_meth_cd").val(oldval);
		    	check_first = 0;
	    	}
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
//	$('#indc_use_yn').combobox({
//		onChange:function(newval, oldval){
//			
//			if(set_first == 1){
//		    	$("#old_indc_use_yn").val(oldval);
//		    	set_first = 0;
//			}
//		}
//	});
	
	Btn_OnClick();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
});

//changeList
function getDeployPerfChkIndc(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'perf_check_indc_id',title:'점검 지표ID',width:"5%",halign:"center",align:"center"},
				{field:'perf_check_indc_nm',title:'점검 지표',width:"10%",halign:"center",align:'left'},
				{field:'perf_check_meth_cd',title:'성능점검방법코드', hidden : true},
				{field:'perf_check_meth_cd_nm',title:'점검 방법',width:"5%",halign:"center",align:'center'},
				{field:'perf_check_indc_desc',title:'지표 설명',width:"38%",halign:"center",align:'left'},
				{field:'perf_check_fail_guide_sbst',title:'부적합 가이드',width:"38%",halign:"center",align:'left'},
				{field:'indc_use_yn',title:'사용여부',width:"5%",halign:"center",align:'center'}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
//		$('#check_pref_nm').textbox("setValue","");
		
//		$("#div_check_pref_nm2").hide();
//		$("#div_check_pref_nm1").show();
//		$("#div_check_pref_nm2").attr('disabled','disabled');
};



function Btn_OnClick(){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 지표 관리"," ");
	
		ajaxCall("/PerformanceCheckIndex/DeployPrefChkIndc",
				$("#submit_form"),
				"POST",
				callback_DeployPerfChkIndcAction);
}

//callback 함수
var callback_DeployPerfChkIndcAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getDeployPerfChkIndc();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
		if($("#perf_check_indc_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','점검 지표를 입력해 주세요.');
			return false;
		}
		
		if($("#perf_check_meth_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','점검방법을 선택해 주세요.');
			return false;
		}
		
		if($("#indc_use_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','사용여부를 선택해 주세요.');
			return false;
		}	
		if($("#perf_check_indc_desc").val() == ""){
			parent.$.messager.alert('','지표 설명을 입력해 주세요.');
			return false;
		}	
		if($("#perf_check_fail_guide_sbst").val() == ""){
			parent.$.messager.alert('','부적합 가이드를 입력해 주세요.');
			return false;
		}	

		if($("#perf_check_indc_nm").val().length > 50){
			parent.$.messager.alert('','점검지표의 내용을 50자 이내로 입력해주세요.');
			return false;
		}
		
		if($("#perf_check_indc_desc").val().length > 200){
			parent.$.messager.alert('','지표설명의 내용을 200자 이내로 입력해주세요.');
			return false;
		}
		
		if($("#perf_check_fail_guide_sbst").val().length > 200){
			parent.$.messager.alert('','부적합 가이드의 내용을 200자 이내로 입력해주세요.');
			return false;
		}
		
		ajaxCall("/PerformanceCheckIndex/DeployPrefChkIndc/Save",
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

	check_first = 1;
	set_first = 1;
	
	$("#crud_flag").val("U");
	$("#perf_check_indc_id").val(selRow.perf_check_indc_id);
//	$("#perf_check_meth_cd").val(selRow.perf_check_meth_cd);
	
	$("#perf_check_indc_nm").textbox("setValue", selRow.perf_check_indc_nm);
	$("#perf_check_meth_cd").combobox("setValue", selRow.perf_check_meth_cd);
	$("#old_perf_check_meth_cd").val(selRow.perf_check_meth_cd);
	$("#indc_use_yn").combobox("setValue", selRow.indc_use_yn);
	$("#old_indc_use_yn").val(selRow.indc_use_yn);
	$("#perf_check_indc_desc").val(selRow.perf_check_indc_desc);
	$("#perf_check_fail_guide_sbst").val(selRow.perf_check_fail_guide_sbst);
	
	
}

function Btn_ResetField(){
	
	$("#crud_flag").val("C");
	$("#perf_check_indc_id").val("");
//	$("#perf_check_meth_cd").val("");
	
	$("#perf_check_indc_nm").textbox("setValue", "");
	$("#perf_check_meth_cd").combobox("setValue", "");
	$("#perf_check_indc_desc").val("");
	$("#perf_check_fail_guide_sbst").val("");
	$("#indc_use_yn").combobox("setValue", "");
}

function Excel_Download(){
//	var dbid = $('#submit_form #selectCombo').combobox('getValue');
//	if(dbid == ""){
//		parent.$.messager.alert('','DB를 선택하여 주세요.');
//		return false;
//	};
	
//	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));

	$("#submit_form").attr("action","/PerformanceCheckIndex/DeployPrefChkIndc/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}


