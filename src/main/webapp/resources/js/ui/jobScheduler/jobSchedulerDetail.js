//var choose = "target_combobox";
//var choose = "target_textbox";

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
//	$("#exec_start_time").inputmask("datetime", {
//	    mask: "y-1-2 h:s", 
//	    placeholder: "yyyy-mm-dd hh:mm", 
//	    leapday: "-02-29", 
//	    separator: "-", 
//	    alias: "yyyy/mm/dd"
//	  });
	$('#exec_start_time').timespinner({
	    showSeconds: true
	});
	$('#exec_end_time').timespinner({
		showSeconds: true
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
	
	$('#job_scheduler_type_cd').combobox({
	    url:"/getJobSchedulerTypeCd2",
	    method:"get",
		valueField:'job_scheduler_type_cd',
	    textField:'job_scheduler_type_cd_nm',
	    onChange: function(newValue, oldValue){
	    	
	    	if(newValue != null && newValue != '' && newValue != 'undefined'){
	    		selectJobSchedulerTypeCd(newValue);
	    	}
	    
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items) {
			$('#job_scheduler_type_cd').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	
	$('#job_scheduler_exec_type_cd').combobox({
	    url:"/getJobSchedulerExecTypeCd",
	    method:"get",
		valueField:'job_scheduler_exec_type_cd',
	    textField:'job_scheduler_exec_type_cd_nm',
	    onChange: function(newValue, oldValue){
	    
	    	selectExecTypeCd(newValue);
	    	
	    },
	    onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items) {
			$('#job_scheduler_exec_type_cd').combobox('textbox').attr("placeholder","선택");
			$('#use_yn').combobox('textbox').attr("placeholder","선택");
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
	$("#detail_form #job_scheduler_nm").textbox({readonly:true});
	$("#detail_form #job_scheduler_exec_type_cd").textbox({readonly:true});
	$("#detail_form #job_scheduler_desc").textbox({readonly:true});
	$("#detail_form #upd_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #upd_dt").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
});



function selectExecTypeCd(newValue){
	if(newValue == '2'){
		$("#job_scheduler_wrk_target_2").hide();
		$("#job_scheduler_wrk_target_1").show();
		chooseDB(newValue);
		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox({readonly:false});
	}else if(newValue == '3'){
		$("#job_scheduler_wrk_target_2").hide();
		$("#job_scheduler_wrk_target_1").show();
		chooseWork(newValue);
		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox({readonly:false});
	}else if(newValue =='5'){
		$("#job_scheduler_wrk_target_1").show();
		$("#job_scheduler_wrk_target_2").hide();
		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox({readonly:false});
		chooseUserAsk(newValue);
	}else{  //4  
		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox({readonly:true});
//		$("#job_scheduler_wrk_target_1").hide();
//		$("#job_scheduler_wrk_target_2").show();
//		$("#job_scheduler_wrk_target_1").html("");
		
	}
};
//chooseDB
function chooseDB(newValue){
	$('#job_scheduler_wrk_target_id').combobox({
		url:"/selectWrkTargetId?select=DB",
		method:"get",
		valueField:'job_scheduler_wrk_target_id',
		textField:'job_scheduler_wrk_target',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items) {
			$('#job_scheduler_wrk_target_id').combobox('textbox').attr("placeholder","선택");
		}
	});
	
};
//chooseWork
function chooseWork(newValue){
	$('#job_scheduler_wrk_target_id').combobox({
		url:"/selectWrkTargetId?select=Work",
		method:"get",
		valueField:'job_scheduler_wrk_target_id',
		textField:'job_scheduler_wrk_target',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items) {
			$('#job_scheduler_wrk_target_id').combobox('textbox').attr("placeholder","선택");
		}
	});
	
};
//chooseUserAsk
function chooseUserAsk(newValue){
	$('#job_scheduler_wrk_target_id').combobox({
		url:"/selectWrkTargetId?select=UserAsk",
		method:"get",
		valueField:'job_scheduler_wrk_target_id',
		textField:'job_scheduler_wrk_target',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items) {
			$('#job_scheduler_wrk_target_id').combobox('textbox').attr("placeholder","선택");
		}
	});
	
};





//select
function selectJobSchedulerTypeCd(newValue){
	
	var value = $("#detail_form #job_scheduler_type_cd").combobox("getValue");
	if(value != null && value != '' && value != 'undefined'){
		ajaxCall("/selectJobSchedulerTypeCd",
				$("#detail_form"),
				"POST",
				callback_selectJobSchedulerTypeCdAction);
	}
};

//callback 함수
var callback_selectJobSchedulerTypeCdAction = function(result) {
		
	var data = JSON.parse(result);  //jsonobject로 만듬.
	//JSON.stringify
	var dataRows = data.rows;
//	console.log("result.job_scheduler_nm:",dataRows[0].job_scheduler_nm);
//	console.log("result.job_scheduler_exec_type_cd:",dataRows[0].job_scheduler_exec_type_cd);
//	console.log("result.job_scheduler_desc:",dataRows[0].job_scheduler_desc);
	
//	parent.$.messager.alert('검색 에러',result,'error');

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	$("#detail_form #job_scheduler_nm").textbox("setValue",dataRows[0].job_scheduler_nm);
	$("#detail_form #job_scheduler_exec_type_cd").combobox("setValue", dataRows[0].job_scheduler_exec_type_cd);
	$("#detail_form #job_scheduler_desc").textbox("setValue",dataRows[0].job_scheduler_desc);
//	
};



//changeList
function getJobShcedulerList(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
				setDetailView(row);  
		//두번 하는 이유는 셀렉트바가 change될때 로드만되고 데이터가 안들어가서 한번더 데이터 로드되고 값을 넣어주기 위함.
			},		
			columns:[[
				{field:'job_scheduler_type_cd',title:'코드',width:"4%",halign:"center",align:"center",sortable:"true"},
				{field:'job_scheduler_type_cd_nm',title:'작업스케쥴러 유형',width:"10%",halign:"center",align:'left'},
				{field:'job_scheduler_nm',title:'스케쥴러명',width:"14%",halign:"center",align:'left'},
				{field:'job_scheduler_exec_type_cd',hidden:true},
				{field:'job_scheduler_exec_type_cd_nm',title:'스케쥴러수행 유형',width:"7%",halign:"center",align:'center'},
				{field:'job_scheduler_desc',title:'스케쥴러 설명',width:"13%",halign:"center",align:'left'},
				{field:'job_scheduler_wrk_target',title:'스케쥴러 작업대상',width:"9%",halign:"center",align:'center'},
				{field:'job_scheduler_wrk_target_id',hidden : true},
				{field:'exec_cycle',title:'실행주기',width:"8%",halign:"center",align:'center'},
				{field:'use_yn',title:'사용여부',width:"4%",halign:"center",align:'center'},
				{field:'exec_start_dt',title:'수행시작일시',width:"8%",halign:"center",align:'center'},
				{field:'exec_start_time',hidden : true},
				{field:'exec_end_dt',title:'수행종료일시',width:"8%",halign:"center",align:'center'},
				{field:'exec_end_time',hidden : true},
				{field:'upd_dt',title:'수정일시',width:"8%",halign:"center",align:'center'},
				{field:'upd_id',title:'수정자 ID',width:"6%",halign:"center",align:'center'}
			]],
//	    	fitColumns:true,
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	},
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케쥴러 설정 상세"," ");
	
		ajaxCall("/getJobSchedulerDetailList",
				$("#submit_form"),
				"POST",
				callback_JobSchedulerDetailAction);
}

//callback 함수
var callback_JobSchedulerDetailAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	//getJobShcedulerList();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

var crud_flag = '';

function Btn_SaveSetting(){
	var check = $("#job_scheduler_exec_type_cd").combobox('getValue');

		if(check == '4'){
			parent.$.messager.alert('','[ 스케쥴러수행 유형 : 사용자요청 ] 은 추가/수정/삭제할 수 없습니다');
			return false;
		}
	
		if($("#job_scheduler_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','작업스케쥴러 유형을 선택해 주세요.');
			return false;
		}
		if($("#job_scheduler_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러명을 입력해 주세요.');
			return false;
		}
		
		if($("#job_scheduler_exec_type_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러 유형을 선택해 주세요.');
			return false;
		}	
		
		if($("#job_scheduler_desc").textbox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러 설명을 입력해 주세요.');
			return false;
		}
		if($("#job_scheduler_wrk_target_id").textbox('getValue') == ""){
			parent.$.messager.alert('','스케쥴러 작업대상을 입력해 주세요.');
			return false;
		}
		if($("#exec_cycle").textbox('getValue') == ""){
			parent.$.messager.alert('','실행주기를 입력해 주세요.');
			return false;
		}	
		if($("#use_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','사용여부를 선택해 주세요.');
			return false;
		}	
		if($("#exec_start_dt").datebox('getValue') == ""){
			parent.$.messager.alert('','수행시작일시를 선택해 주세요.');
			return false;
		}	
		if($("#exec_start_time").timespinner('getValue') == ""){
			parent.$.messager.alert('','수행시작시간을 선택해 주세요.');
			return false;
		}	
		if($("#exec_end_dt").datebox('getValue') == ""){
			parent.$.messager.alert('','수행종료일시를 선택해 주세요.');
			return false;
		}	
		if($("#exec_end_time").timespinner('getValue') == ""){
			parent.$.messager.alert('','수행종료시간을 선택해 주세요.');
			return false;
		}	
		if(!validateHH24MMSS($("#exec_start_time").val())) {
			var msg = "수행시작시간을 확인해 주세요.";
			parent.$.messager.alert('오류',msg,'error');
			return false;
		}
		if(!validateHH24MMSS($("#exec_end_time").val())) {
			var msg = "수행종료시간을 확인해 주세요.";
			parent.$.messager.alert('오류',msg,'error');
			return false;
		}
		
		if(byteLength($("#exec_cycle").textbox('getValue')) >= 100){
			parent.$.messager.alert('','실행주기 정보가 100 Byte를 초과 하였습니다.');
			return false;
		}	
		
		if(compareAnBDatatime( $("#exec_start_dt").datebox('getValue'), $("#exec_start_time").timespinner('getValue'), 
				$("#exec_end_dt").datebox('getValue'), $("#exec_end_time").timespinner('getValue') ) <= 0) {
			var msg = "수행 시작 / 종료 시간을 확인해 주세요.";
			parent.$.messager.alert('오류',msg,'error');
			return false;;
		}
	
		$("#upd_id").textbox("setValue", $("#user_id").val());
		
		if($("#upd_dt").val() == '') {
			crud_flag = "C";
		} else {
			crud_flag = "U";
		}

		
		ajaxCall("/saveJobSchedulerDetail",
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






function Btn_DeleteSetting(){
	var data = $("#tableList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return;
	}
	
	if(data.job_scheduler_exec_type_cd == '4'){
		parent.$.messager.alert('','[ 스케쥴러수행 유형 : 사용자요청 ] 은 추가/수정/삭제 할 수 없습니다');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 작업스케쥴러 작업대상 : ' + $("#job_scheduler_wrk_target").val()+ ' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deleteJobSchedulerDetail", 
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
	
//	if(choose == 'combobox'){
//		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox("setValue", selRow.job_scheduler_wrk_target_id);
//		$("#job_scheduler_wrk_target_2 #job_scheduler_wrk_target_id").textbox("setValue", "");
//	}else{
//		$("#job_scheduler_wrk_target_2 #job_scheduler_wrk_target_id").textbox("setValue", selRow.job_scheduler_wrk_target_id);
//		$("#job_scheduler_wrk_target_1 #job_scheduler_wrk_target_id").combobox("setValue", "");
//		
//	}

	
	$("#job_scheduler_wrk_target_id").combobox("setValue", selRow.job_scheduler_wrk_target_id);
	$("#job_scheduler_wrk_target").val(selRow.job_scheduler_wrk_target);
	$("#detail_form #job_scheduler_type_cd").textbox({readonly:true});
	$("#job_scheduler_type_cd").combobox("getValue"); 
	$("#job_scheduler_type_cd").combobox("setValue", selRow.job_scheduler_type_cd);
	$("#job_scheduler_nm").textbox("setValue", selRow.job_scheduler_nm);
	$("#job_scheduler_exec_type_cd").combobox("setValue", selRow.job_scheduler_exec_type_cd);
	$("#job_scheduler_desc").textbox("setValue", selRow.job_scheduler_desc);
	$("#exec_cycle").textbox("setValue", selRow.exec_cycle);
	$("#use_yn").combobox("setValue", selRow.use_yn);
	$("#upd_dt").textbox("setValue", selRow.upd_dt);
	$("#upd_id").textbox("setValue", selRow.upd_id);
	$("#exec_start_dt").datebox("setValue", selRow.exec_start_dt);
	$("#exec_end_dt").datebox("setValue", selRow.exec_end_dt);
	$("#exec_start_time").textbox("setValue", selRow.exec_start_time);
	$("#exec_end_time").textbox("setValue", selRow.exec_end_time);
	
	$("#job_scheduler_type_cd_nm").val(selRow.job_scheduler_type_cd_nm);
	$("#job_scheduler_exec_type_cd_nm").val(selRow.job_scheduler_exec_type_cd_nm);
	
//	selectExecTypeCd(selRow.job_scheduler_exec_type_cd);
	
	
}

function Btn_ResetField(){
	$("#tableList").datagrid("clearSelections");
	$("#job_scheduler_type_cd").combobox("setValue", "");
	$("#job_scheduler_nm").textbox("setValue", "");
	$("#job_scheduler_exec_type_cd").combobox("setValue", "");
	$("#job_scheduler_desc").textbox("setValue", "");
	$("#job_scheduler_wrk_target_id").textbox("setValue", "");
	$("#job_scheduler_wrk_target").val("");
	$("#exec_cycle").textbox("setValue", "");
	$("#use_yn").combobox("setValue", "");
	$("#upd_dt").textbox("setValue", "");
	$("#upd_id").textbox("setValue", "");
	$("#exec_start_dt").datebox("setValue", "");
	$("#exec_end_dt").datebox("setValue", "");
	$("#exec_start_time").textbox("setValue", "");
	$("#exec_end_time").textbox("setValue", "");
	
	$("#job_scheduler_type_cd_nm").val("");
	$("#job_scheduler_exec_type_cd_nm").val("");
	$("#detail_form #job_scheduler_type_cd").textbox({readonly:false});

//	selectJobSchedulerTypeCd();
//	$("#detail_form #job_scheduler_nm").textbox({readonly:false});
//	$("#detail_form #job_scheduler_exec_type_cd").textbox({readonly:false});
//	$("#detail_form #job_scheduler_desc").textbox({readonly:false});
	
	
	
	
}