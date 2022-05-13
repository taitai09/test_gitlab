
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
		},
	});
	
	//Btn_OnClick();
	getJobShcedulerDetailList();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
});

//changeList
function getJobShcedulerDetailList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'job_scheduler_type_cd',title:'작업스케쥴러 유형코드',width:"10%",halign:"center",align:"center",sortable:"true"},
				{field:'job_scheduler_type_cd_nm',title:'작업스케쥴러 유형',width:"20%",halign:"center",align:'left'},
				{field:'job_scheduler_detail_type_cd',title:'작업스케쥴러 상세유형코드',width:"10%",halign:"center",align:'center'},
				{field:'job_scheduler_detail_type_nm',title:'작업스케쥴러 상세유형',width:"20%",halign:"center",align:'left'},
				{field:'job_scheduler_detail_type_desc',title:'작업스케쥴러 상세유형 설명',width:"20%",halign:"center",align:'left'}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("작업스케쥴러 설정 상세"," ");
	
		ajaxCall("/getJobSchedulerdetailTypeCdList",
				$("#submit_form"),
				"POST",
				callback_JobSchedulerDetailAction);
}

//callback 함수
var callback_JobSchedulerDetailAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	//getJobShcedulerDetailList();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
		
	var rows = $("#tableList").datagrid('getRows');
//	console.log("rows",rows);
	 
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
		
		let detailTypeCd = $("#job_scheduler_detail_type_cd").textbox('getValue');
		if(detailTypeCd == ""){
			parent.$.messager.alert('','작업스케쥴러 상세유형 코드를 입력해 주세요.');
			return false;
			
		}else if( byteLength(detailTypeCd) > 3 ){
			parent.$.messager.alert('','작업스케쥴러 상세유형 코드가 3 Byte를 초과 하였습니다.');
			return false;
		}
		
		if($("#job_scheduler_detail_type_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','작업스케쥴러 상세유형을 입력해 주세요.');
			return false;
		}
		
//		if($("#job_scheduler_detail_type_desc").textbox('getValue') == ""){
//			parent.$.messager.alert('','스케쥴러 상세유형 설명을 입려해 주세요.');
//			return false;
//		}
		
		if(byteLength($("#job_scheduler_detail_type_nm").textbox('getValue')) > 100){
			parent.$.messager.alert('','작업스케쥴러 상세유형 정보가 100 Byte를 초과 하였습니다.');
			return false;
		}
		
		if(byteLength($("#job_scheduler_detail_type_desc").textbox('getValue')) > 400){
			parent.$.messager.alert('','작업스케쥴러 상세유형 설명 정보가 400 Byte를 초과 하였습니다.');
			return false;
		}
		
		ajaxCall("/saveJobSchedulerDetailTypeCd",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if($("#crud_flag").val() == 'U') {
		message = "수정 되었습니다.";
	}
	
	if(result.result){
		parent.$.messager.alert('',message,'',function(){
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
	
	parent.$.messager.confirm('', '[ 작업스케쥴러 상세유형 : ' + data.job_scheduler_detail_type_nm + ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deleteJobSchedulerDetailTypeCd", 
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

	$("#job_scheduler_type_cd").combobox({readonly : true});
	$("#job_scheduler_detail_type_cd").textbox({readonly : true});
	$("#job_scheduler_type_cd_nm").val(selRow.job_scheduler_type_cd);
	
	
	$("#job_scheduler_type_cd").combobox("setValue", selRow.job_scheduler_type_cd);
	$("#job_scheduler_detail_type_cd").textbox("setValue", selRow.job_scheduler_detail_type_cd);
	$("#job_scheduler_detail_type_nm").textbox("setValue", selRow.job_scheduler_detail_type_nm);
	$("#job_scheduler_detail_type_desc").textbox("setValue", selRow.job_scheduler_detail_type_desc);
}

function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	$("#job_scheduler_type_cd").combobox({readonly : false});
	$("#job_scheduler_detail_type_cd").textbox({readonly : false});

	$("#crud_flag").val("C");
	$("#job_scheduler_type_cd_nm").val("");
	$("#job_scheduler_detail_type_cd").textbox("setValue", "");
	$("#job_scheduler_type_cd").combobox("setValue", "");
	$("#job_scheduler_detail_type_nm").textbox("setValue", "");
	$("#job_scheduler_detail_type_desc").textbox("setValue", "");
	
}