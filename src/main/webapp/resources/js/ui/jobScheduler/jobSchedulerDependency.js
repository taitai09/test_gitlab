var dpnd_job_sched_type_cd = '01';  // dpnd_job_sched_type_cd 값을 가지고오기위한 초기값;
	
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
	
	$('#dpnd_job_sched_type_cd').combobox({  //종속작업 스케쥴러 유형
		url:"/getJobSchedulerTypeCd",
		method:"get",
		valueField:'job_scheduler_type_cd',
		textField:'job_scheduler_type_cd_nm',
		onChange:function(newValue, oldValue){
			
			unableToSameCd(newValue, oldValue);
			selectDpndJobSchedDetailCd(oldValue);
		},
		onSelect:function(value){
			
			selectDpndJobSchedDetailCd(value.job_scheduler_type_cd);
			
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#dpnd_job_sched_type_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	selectDpndJobSchedDetailCd();
	
	//Btn_OnClick();
	getJobSchedulerDependency()
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
});

function unableToSameCd(newValue, oldValue){
	if(newValue == $("#job_scheduler_type_cd").combobox("getValue")){
		parent.$.messager.alert('','작업스케쥴러 유형과 종속작업스케쥴러 유형을 다르게 선택해 주세요.');

		 $("#dpnd_job_sched_type_cd").combobox("setValue", oldValue);
		 
	};
};
function selectDpndJobSchedDetailCd(old_dpnd_job_sched_type_cd){ 
	if($("#crud_flag").val() == "U"){ //값이 변경되었을 시 
		$('#old_dpnd_job_sched_type_cd').val(old_dpnd_job_sched_type_cd);	
	};
};
function selectDpndJobSchedDetailCd(dpnd_job_sched_type_cd){
	
	
	if($("#crud_flag").val() == "C"){ //값이 변경되었을 시 
		$('#old_dpnd_job_sched_type_cd').val(dpnd_job_sched_type_cd);	
	};
	
	//key값이 'dpnd_job_sched_type_cd' 따라 변경되기때문에 change할때마다 바꿔줘야함.
	$('#dpnd_job_sched_detail_type_cd').combobox({   //>종속작업 스케쥴러 상세유형
		url:"/getDpndJobSchedTypeCd?job_scheduler_type_cd="+dpnd_job_sched_type_cd,
		method:"get",
		valueField:'dpnd_job_sched_detail_type_cd',
		textField:'dpnd_job_sched_detail_type_nm',
		onSelect : function(row){
//			alert(row.dpnd_job_sched_detail_type_cd);
			selectDpndJobSchedDetailTypeCd(row.dpnd_job_sched_detail_type_cd, row.dpnd_job_sched_detail_type_nm);
		},
		onChange : function(newValue, oldValue){
			changeDpndJobSchedDetailTypeCd(newValue);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#dpnd_job_sched_detail_type_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
};
function changeDpndJobSchedDetailTypeCd(dpnd_job_sched_detail_type_cd){
	if($("#crud_flag").val() == "U"){
//		$('#old_dpnd_job_sched_detail_type_cd').val(dpnd_job_sched_detail_type_cd);	
	};
};
function selectDpndJobSchedDetailTypeCd(dpnd_job_sched_detail_type_cd, dpnd_job_sched_detail_type_nm){
	if($("#crud_flag").val() == "C"){
		$('#old_dpnd_job_sched_detail_type_cd').val(dpnd_job_sched_detail_type_cd);	
		$('#old_dpnd_job_sched_detail_type_nm').val(dpnd_job_sched_detail_type_nm);	
	};
	
};
//changeList
function getJobSchedulerDependency(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'job_scheduler_type_cd',title:'작업 스케쥴러 유형코드',width:"10%",halign:"center",align:"center",sortable:"true"},
				{field:'job_scheduler_type_cd_nm',title:'작업 스케쥴러 유형',width:"20%",halign:"center",align:'left'},
				{field:'dpnd_job_sched_type_cd',title:'종속작업 스케쥴러 유형코드',width:"10%",halign:"center",align:'center'},
				{field:'dpnd_job_sched_type_nm',title:'종속작업 스케쥴러 유형',width:"20%",halign:"center",align:'left'},
				{field:'dpnd_job_sched_detail_type_cd',title:'종속작업 스케쥴러 상세유형코드',width:"10%",halign:"center",align:'center'},
				{field:'dpnd_job_sched_detail_type_nm',title:'종속작업 스케쥴러 상세유형',width:"20%",halign:"center",align:'left'}
			]],
//	    	fitColumns:true,
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("작업스케쥴러 종속관계 관리"," ");
	
		ajaxCall("/getJobSchedulerDependency",
				$("#submit_form"),
				"POST",
				callback_JobSchedulerDependency);
}

//callback 함수
var callback_JobSchedulerDependency = function(result) {
	json_string_callback_common(result,'#tableList',true);
	//getJobSchedulerDependency();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
	var rows = $("#tableList").datagrid('getRows');
	 
//	var current_job_scheduler_type_cd = $("#job_scheduler_type_cd").combobox('getValue');
//	var current_job_scheduler_detail_type_cd = $("#job_scheduler_detail_type_cd").textbox('getValue');

//	for(var i=0; i<rows.length; i++){
//			if(rows[i].job_scheduler_type_cd == current_job_scheduler_type_cd &&
//					rows[i].job_scheduler_detail_type_cd == current_job_scheduler_detail_type_cd){
//				parent.$.messager.alert('','작업스케쥴러 유형과 작업스케쥴러 상세유형은 중복될 수 없습니다.');
//				return false;
//			}
//		};
	

	
		if($("#job_scheduler_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','작업스케쥴러 유형을 선택해 주세요.');
			return false;
		}
		if($("#dpnd_job_sched_type_cd").textbox('getValue') == ""){
			parent.$.messager.alert('','종속작업 스케쥴러 유형을 선택해 주세요.');
			return false;
		}
		if($("#dpnd_job_sched_detail_type_cd").textbox('getValue') == ""){
			parent.$.messager.alert('','종속작업 스케쥴러 상세유형을 선택해 주세요.');
			return false;
		}
		
			
			ajaxCall("/saveJobSchedulerDependency",
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
		parent.$.messager.alert('','테이터를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 종속작업스케쥴러 상세유형 : ' + data.dpnd_job_sched_detail_type_nm + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deleteJobSchedulerDependency", 
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
	
	$("#crud_flag").val("U");
//	var rows = $("#tableList").datagrid('getSelections');

//	
	$("#job_scheduler_type_cd").combobox({readonly : true});

	$("#job_scheduler_type_cd").combobox("setValue", selRow.job_scheduler_type_cd);
	$("#dpnd_job_sched_type_cd").combobox("setValue", selRow.dpnd_job_sched_type_cd);
	$("#dpnd_job_sched_detail_type_cd").combobox("setValue", selRow.dpnd_job_sched_detail_type_cd);
	
	$("#job_scheduler_type_cd_nm").val(selRow.job_scheduler_type_cd_nm);
	$("#dpnd_job_sched_type_nm").val(selRow.dpnd_job_sched_type_nm);
	$("#dpnd_job_sched_detail_type_nm").val(selRow.dpnd_job_sched_detail_type_nm);
	
	$("#old_dpnd_job_sched_type_cd").val(selRow.dpnd_job_sched_type_cd);
	$("#old_dpnd_job_sched_detail_type_cd").val(selRow.dpnd_job_sched_detail_type_cd);
	$("#old_dpnd_job_sched_detail_type_nm").val(selRow.dpnd_job_sched_detail_type_nm);
}

function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	
	$("#crud_flag").val("C");
	$("#job_scheduler_type_cd").combobox({readonly : false});
	$("#dpnd_job_sched_type_cd").combobox({readonly : false});
//	$("#job_scheduler_detail_type_cd").combobox({readonly : false});

	$("#crud_flag").val("C");
	
	$("#dpnd_job_sched_type_cd_nm").val("");
	$("#dpnd_job_sched_type_nm").val("");
	$("#job_scheduler_detail_type_nm").val("");
	
	$("#job_scheduler_type_cd").combobox("setValue", "");
	$("#dpnd_job_sched_type_cd").combobox("setValue", "");
	$("#dpnd_job_sched_detail_type_cd").combobox("setValue", "");
	
	$("#old_dpnd_job_sched_type_cd").val("");
	$("#old_dpnd_job_sched_detail_type_cd").val("");
	$("#old_dpnd_job_sched_detail_type_nm").val("");
	
}