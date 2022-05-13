$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#job_scheduler_type_cd').combobox({
		url:"/getJobSchedulerTypeCd",
		method:"get",
		valueField:'job_scheduler_type_cd',
		textField:'job_scheduler_type_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#job_scheduler_type_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	$('#job_scheduler_exec_type_cd').combobox({
		url:"/getJobSchedulerExecTypeCd",
		method:"get",
		valueField:'job_scheduler_exec_type_cd',
		textField:'job_scheduler_exec_type_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#job_scheduler_exec_type_cd').combobox('textbox').attr("placeholder","선택");
			$('#use_yn').combobox('textbox').attr("placeholder","선택");
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
		onLoadSuccess : function(items) {
			$('#searchValue').textbox('readonly', true);
		}
	});
	
	//Btn_OnClick();
	getJobShcedulerList();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	$("#detail_form #upd_id").textbox({readonly:true});
	$("#detail_form #upd_dt").textbox({readonly:true});
	$("#detail_form #upd_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #upd_dt").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
});

//changeList
function getJobShcedulerList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'job_scheduler_type_cd',title:'코드',width:"4%",halign:"center",align:"center",sortable:"true"},
				{field:'job_scheduler_type_cd_nm',title:'작업스케쥴러 유형',width:"16%",halign:"center",align:'left'},
				{field:'job_scheduler_nm',title:'스케쥴러명',width:"20%",halign:"center",align:'left'},
				{field:'job_scheduler_desc',title:'스케쥴러 설명',width:"14%",halign:"center",align:'left'},
				{field:'job_scheduler_exec_type_cd',hidden : true},
				{field:'job_scheduler_exec_type_cd_nm',title:'스케쥴러 유형',width:"8%",halign:"center",align:'center'},
				{field:'default_exec_cycle',title:'기본실행주기',width:"8%",halign:"center",align:'center'},
				{field:'use_yn',title:'사용여부',width:"8%",halign:"center",align:'center'},
				{field:'upd_dt',title:'수정일시',width:"8%",halign:"center",align:'center'},
				{field:'upd_id',title:'수정자 ID',width:"8%",halign:"center",align:'center'}
			]],
//	    	fitColumns:true,
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
	
		ajaxCall("/getJobSchedulerList",
				$("#submit_form"),
				"POST",
				callback_JobSchedulerBaseAction);
}

//callback 함수
var callback_JobSchedulerBaseAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	//getJobShcedulerList();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

var crud_flag = '';

function Btn_SaveSetting(){
		
		if($("#job_scheduler_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','작업스케쥴러 유형을 선택해 주세요.');
			return false;
		}
		if($("#job_scheduler_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러명을 입력해 주세요.');
			return false;
		}
		
		if($("#job_scheduler_desc").textbox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러 설명을 입력해 주세요.');
			return false;
		}
		
		if($("#job_scheduler_exec_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러 유형을 선택해 주세요.');
			return false;
		}	
		if($("#default_exec_cycle").textbox('getValue') == ""){
			parent.$.messager.alert('','기본실행주기를 입력해 주세요.');
			return false;
		}	
		if($("#use_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','사용여부를 선택해 주세요.');
			return false;
		}	
//		if($("#upd_dt").textbox('getValue') == ""){
//			parent.$.messager.alert('','수정일시를 입력해 주세요.');
//			return false;
//		}	
//		if($("#upd_id").textbox('getValue') == ""){
//			parent.$.messager.alert('','수정자 ID를 입력해 주세요.');
//			return false;
//		}
		
		if(byteLength($("#job_scheduler_nm").textbox('getValue')) > 100){
			parent.$.messager.alert('','스케쥴러명 정보가 100 Byte를 초과 하였습니다.');
			return false;
		}
		
		if(byteLength($("#job_scheduler_desc").textbox('getValue')) > 1000){
			parent.$.messager.alert('','스케쥴러 설명 정보가 1000 Byte를 초과 하였습니다.');
			return false;
		}
		
		if(byteLength($("#default_exec_cycle").textbox('getValue')) > 100){
			parent.$.messager.alert('','기본실행주기 정보가 100 Byte를 초과 하였습니다.');
			return false;
		}	
	
		$("#upd_id").textbox("setValue", $("#user_id").val());
		
		if($("#upd_dt") == '') {
			crud_flag = 'C';
		} else {
			crud_flag = 'U';
		}
		
		ajaxCall("/saveJobSchedulerBase",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if(crud_flag == 'U') {
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

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if(crud_flag == 'U') {
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
		return;
	}
	
	parent.$.messager.confirm('', '[ 작업스케쥴러유형 : ' + $("#job_scheduler_type_cd_nm").val()+ ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deleteJobSchedulerBase", 
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
	$("#job_scheduler_exec_type_cd").combobox("setValue", selRow.job_scheduler_exec_type_cd);
	
	$("#detail_form #job_scheduler_type_cd").textbox({readonly:true});
	$("#job_scheduler_type_cd").combobox("setValue", selRow.job_scheduler_type_cd);
	$("#job_scheduler_nm").textbox("setValue", selRow.job_scheduler_nm);
	$("#job_scheduler_desc").textbox("setValue", selRow.job_scheduler_desc);
	$("#default_exec_cycle").textbox("setValue", selRow.default_exec_cycle);
	$("#use_yn").combobox("setValue", selRow.use_yn);
	$("#upd_dt").textbox("setValue", selRow.upd_dt);
	$("#upd_id").textbox("setValue", selRow.upd_id);
	$("#job_scheduler_type_cd_nm").val(selRow.job_scheduler_type_cd_nm);
	$("#job_scheduler_exec_type_cd_nm").val(selRow.job_scheduler_exec_type_cd_nm);
}

function Btn_ResetField(){
	$("#tableList").datagrid("clearSelections");
	$("#job_scheduler_type_cd").combobox("setValue", "");
	$("#job_scheduler_nm").textbox("setValue", "");
	$("#job_scheduler_desc").textbox("setValue", "");
	$("#job_scheduler_exec_type_cd").combobox("setValue", "");
	$("#default_exec_cycle").textbox("setValue", "");
	$("#use_yn").combobox("setValue", "");
	$("#upd_dt").textbox("setValue", "");
	$("#upd_id").textbox("setValue", "");
	
	$("#job_scheduler_type_cd_nm").val("");
	$("#job_scheduler_exec_type_cd_nm").val("");
	$("#detail_form #job_scheduler_type_cd").textbox({readonly:false});
	$('#job_scheduler_type_cd').combobox('textbox').attr("placeholder","선택");
}