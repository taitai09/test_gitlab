var _second_field;

$(document).ready(function() {
	$("body").css("visibility", "visible");

	$('#submit_form #endDate').datebox({
		onChange: function(newValue, oldValue) {
			ajaxCallSchedulerStatus();
		}
	});
	
	$('#submit_form #schedulerErrorSwitch').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$('#submit_form #error_yn').val("Y");
			}else{
				$('#submit_form #error_yn').val("N");
			}
			
			ajaxCallSchedulerStatus();
		}
	});
	
	// End of Left View
	
	// Start of Right View
	// Scheduler History
	// Database 조회
	$('#scheduler_history_form #selectCombo').combobox({
		url:"/Common/getDatabase?isAll=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(data) {
			if(data.length > 0) {
				var flag = false;
				for(var i = 0; i < data.length; i++) {
					if(data[i].dbid == '0') {
						break;
					} else {
						flag = true;
					}
					
					if(i == (data.length - 1) && flag == true) {
						data.push({
							dbid:'0',
							db_name:'기타'
						});
						
						$(this).combobox('loadData', data);
					}
				}
			}
		}
	});
	
	$('#scheduler_history_form #schedulerErrorSwitch').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$('#scheduler_history_form #error_yn').val("Y");
			}else{
				$('#scheduler_history_form #error_yn').val("N");
			}
		}
	});
	
	$("#scheduler_history_form #tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			goDetailView(row);
		},
		columns:[[
			{field:'job_exec_no',title:'수행번호',width:"10%",halign:"center",align:"center",sortable:false},
			{field:'job_scheduler_type_cd',hidden:"true"},
			{field:'job_scheduler_type_nm',title:'스케쥴러 유형',width:"14%",halign:"center",align:'left'},
			{field:'base_day',title:'기준일자',width:"10%",halign:"center",align:'center'},
			{field:'job_start_dt',title:'작업시작일시',width:"15%",halign:"center",align:'center'},
			{field:'job_end_dt',title:'작업종료일시',width:"15%",halign:"center",align:'center'},
			{field:'job_status_cd',title:'작업상태코드',width:"12%",halign:"center",align:'center'},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',width:"12%",halign:"center",align:'center'},
			{field:'wrkjob_cd',hidden:"true"},
			{field:'wrkjob_cd_nm',title:'업무명',width:"12%",halign:"center",align:'center'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data) {
			var opts = $(this).datagrid('options');
			if(data.rows.length > 0){
				dataRow = data.rows[0];
				
				$(this).datagrid('selectRow', 0);
				
				goDetailView(dataRow);
			}
		}
	});
	
	// 스케쥴러 유형 조회
	$('#scheduler_history_form #schedulerTypeCd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1020&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		panelHeight: 300
	});	
	
	$('#scheduler_history_form #selectCombo').combobox('setValue',$("#scheduler_history_form #dbid").val()); 
	$('#scheduler_history_form #schedulerTypeCd').combobox('setValue',$("#scheduler_history_form #job_scheduler_type_cd").val());
	
	var callFromParent = $("#scheduler_history_form #call_from_parent").val();
	var callFromChild = $("#scheduler_history_form #call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallPerformSchedulerAction();
	}
	
	setMarster_form_selector($('#scheduler_history_form').selector);
	
	// Scheduler History Detail
	$("#scheduler_history_detail_form #tableList").datagrid({
		view: myview,
		columns:[[
			{field:'job_exec_no',hidden:"true"},
			{field:'job_scheduler_type_cd',hidden:"true"},
			{field:'job_scheduler_detail_type_cd',hidden:"true"},
			{field:'job_scheduler_detail_type_nm',title:'스케쥴러 상세 유형',width:"15%",halign:"center",align:'left',sortable:false},
			{field:'job_exec_dt',title:'작업수행일시',width:"11%",halign:"center",align:'center'},
			{field:'job_target_cnt',title:'작업대상건수',width:"7%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'job_err_code',title:'작업오류코드',width:"8%",halign:"center",align:'center'},
			{field:'job_err_sbst',title:'작업오류내용',width:"36%",halign:"center",align:'left'},
			{field:'hndop_job_exec_yn',title:'수동작업수행여부',width:"9%",halign:"center",align:'center'},
			{field:'hndop_worker_id',hidden:"true"},
			{field:'hndop_worker_nm',title:'수동작업자',width:"7%",halign:"center",align:'center'},
			{field:'hndop_worker_nm',title:'재작업',width:"7%",halign:"center",align:'center'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onSelect: function(index, row) {
			$(this).datagrid('unselectRow', index);
		}
	});
	
	var checkJobErr = false;
	if($("#scheduler_history_detail_form #job_err_yn").val() == "Y") checkJobErr = true;
	
	$('#scheduler_history_detail_form #chkJobErrYn').switchbutton({
		checked: checkJobErr,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#scheduler_history_detail_form #job_err_yn").val("Y");
			}else{
				$("#scheduler_history_detail_form #job_err_yn").val("N");
			}
			
			Btn_OnClick_Detail();
		}
	})
	
	var checkHndopJobExecYn = false;
	if($("#scheduler_history_detail_form #hndop_job_exec_yn").val() == "Y") checkHndopJobExecYn = true;
	
	$('#scheduler_history_detail_form #chkHndopJobExecYn').switchbutton({
		checked: checkHndopJobExecYn,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#scheduler_history_detail_form #hndop_job_exec_yn").val("Y");
			}else{
				$("#scheduler_history_detail_form #hndop_job_exec_yn").val("N");
			}
			
			Btn_OnClick_Detail();
		}
	})
	
	ajaxCallSchedulerStatus();
});

function ajaxCallSchedulerStatus() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케줄러 점검 현황");
	
	$('#submit_form #base_day').val($('#submit_form #endDate').val());
	
	ajaxCall("/ExamineOpenPOP/rtrvSchedulerStatusHistory",
		$("#submit_form"),
		"POST",
		callback_rtrvSchedulerStatusHistory);
}

var callback_rtrvSchedulerStatusHistory = function(result) {
	try {
		var data = JSON.parse(result);
		var rows = data.rows;
		var rowsLen = rows.length;
		var headArray = new Array();
		
		if(rowsLen > 0) {
			var headRow = rows.splice(rowsLen - 1, 1);
			var head = headRow[0].HEAD;
			
			headArray = head.split('\;');
		}
		
		var pureData = rows;
		getByGrid(headArray, false);
		
		data.rows = changeValue(pureData);
		
		json_string_callback_common(JSON.stringify(data), '#submit_form  #schedulerStatusList', true);
	} catch(err) {
		if(err.message.indexOf('Unexpected') == 0) {
			parent.$.messager.alert('검색 에러',result.substring(result.indexOf('message') + 10, result.length - 2),'error');
		} else {
			parent.$.messager.alert('검색 에러',err.message,'error');
		}
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function changeValue(pureData) {
	for(var index = 0; index < pureData.length; index++) {
		var row = pureData[index];
		
		for(var key in row) {
			var value = row[key];
			
			if(key !== 'base_day') {
				row[key] = getStatusImg(value);
			} else {
				row[key] = value;
			}
		}
		
		pureData[index] = row;
	}
	
	return pureData
}

function getByGrid(headArray, isError){
	var jsonArray = new Array();
	var jsonMap = new Object();
	var jsonArrayColumnGroup = new Array();
	var jsonMap2 = new Object();
	var jsonMap3 = new Object();
	
	if(isError) {
		jsonMap = new Object();  //맵객체를 새로만듬.
		jsonMap.field='error';
		jsonMap.title='에러';
		jsonMap.width='100%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	} else {
		if(headArray.length == 0) {
			jsonMap = new Object();  //맵객체를 새로만듬.
			//jsonMap.field='coulmn_'+i;
			jsonMap.field='info';
			jsonMap.title='정보';
			jsonMap.width='100%';
			jsonMap.halign='center';
			jsonMap.align='center';
			jsonArray.push(jsonMap);
		}
		
		for(var i = 0; i < headArray.length; i++){
			var head = headArray[i];
			var options = head.split('\/');
			
			if(i == 1) {
				_second_field = options[0];
			}
			
			jsonMap = new Object();  //맵객체를 새로만듬.
			jsonMap.field = options[0];
			jsonMap.title = options[1];
//			jsonMap.width = 100;
			jsonMap.halign = 'center';
			jsonMap.align = setAlign(options[2]);
			jsonMap.hidden = setHidden(options[3]);
			
			if(i > 0) {
				jsonMap.formatter = getStatusImg();
			}
			
			jsonArray.push(jsonMap);
		}
	}
	
	$("#submit_form #schedulerStatusList").datagrid({
		view: emptyview,
		nowrap : false,
		fitColumns:true,
		autoRowHeight:false,
		striped:true,
		singleSelect:true,
		rownumbers:false,
		columns:[jsonArray],
		onClickCell:function(index, field, value) {
			var base_day = $(this).datagrid('getRows')[index].base_day;
			var clm_name = field + '_ID';
			var dbid = $(this).datagrid('getRows')[index][clm_name];
			
			if(field === 'base_day') {
				return;
			}
			
			if(value.indexOf('error_status.png') > 0) {
				$('#scheduler_history_form #schedulerErrorSwitch').switchbutton('check');
			} else if(value.indexOf('normal_status.png') > 0) {
				$('#scheduler_history_form #schedulerErrorSwitch').switchbutton('uncheck');
			} else {
				return;
			}
			
			$('#scheduler_history_form #strStartDt').datebox('setValue', formatReserDate(base_day, 'en'));
			$('#scheduler_history_form #strEndDt').datebox('setValue', formatReserDate(base_day, 'en'));
			
			$('#scheduler_history_form #selectCombo').combobox('setValue', dbid);
			
			Btn_OnClick();
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data) {
			var opts = $(this).datagrid('options');
			if(data.rows.length > 0){
				dataRow = data.rows[0];
				
				var base_day = dataRow.base_day;
				var clm_name = _second_field + '_ID';
				var dbid = dataRow[clm_name];
				var status = dataRow[_second_field];
				
				if(status.indexOf('error_status.png') > 0) {
					$('#scheduler_history_form #schedulerErrorSwitch').switchbutton('check');
				} else if(status.indexOf('normal_status.png') > 0) {
					$('#scheduler_history_form #schedulerErrorSwitch').switchbutton('uncheck');
				} else {
					return;
				}
				
				$('#scheduler_history_form #strStartDt').datebox('setValue', formatReserDate(base_day, 'en'));
				$('#scheduler_history_form #strEndDt').datebox('setValue', formatReserDate(base_day, 'en'));
				
				$('#scheduler_history_form #selectCombo').combobox('setValue', dbid);
				
				$(this).datagrid('selectRow', 0);
				
				Btn_OnClick();
			}
		}
	});
}

function setAlign(typeName) {
	var align = '';
	
	if(typeName.toUpperCase() === 'NUMBER') {
		align = 'right';
	} else if(typeName.toUpperCase() === 'VARCHAR' || typeName.toUpperCase() === 'VARCHAR2' ||
			typeName.toUpperCase() === 'CLOB') {
		align = 'left';
	} else {
		align = 'center';
	}
	
	return align;
}

function setHidden(hidden) {
	if(typeof hidden != 'undefined') {
		return eval(hidden)
	}
	
	return eval('false');
}

function getStatusImg(val) {
	if(val == 1) {		// 정상
		return "<img src='/resources/images/examine_scheduler/normal_status.png' style='vertical-align:bottom;'/>";	
	} else if(val == 2) {	// 오류
		return "<img src='/resources/images/examine_scheduler/error_status.png' style='vertical-align:bottom;'/>";
	} else if(val == 3) {	// 미사용
		return "<img src='/resources/images/examine_scheduler/unexecuted_status.png' style='vertical-align:bottom;'/>";
	} else {
		return val;
	}
}

// End of Left View

// Start of Right View
function Btn_OnClick(){
	if(($('#scheduler_history_form #strStartDt').textbox('getValue') == "" && $("#scheduler_history_form #strEndDt").textbox('getValue') != "") ||
		($('#scheduler_history_form #strStartDt').textbox('getValue') != "" && $("#scheduler_history_form #strEndDt").textbox('getValue') == "")){
		parent.$.messager.alert('','기준일자를 정확히 입력해 주세요.');
		return false;
	}
	
	$('#scheduler_history_form #currentPage').val("1");
	
	fnSearch();
}

function fnSearch(){
	$("#scheduler_history_form #dbid").val($('#scheduler_history_form #selectCombo').combobox('getValue'));
	$("#scheduler_history_form #job_scheduler_type_cd").val($('#scheduler_history_form #schedulerTypeCd').combobox('getValue'));
	
	ajaxCallPerformSchedulerAction();
}

function ajaxCallPerformSchedulerAction(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케줄러 수행 내역"," ");
	
	$('#scheduler_history_form #tableList').datagrid("loadData", []);
	
	ajaxCall("/ExamineOpenPOP/PerformSchedulerAction",
			$("#scheduler_history_form"),
			"POST",
			callback_PerformSchedulerAddTable);
}

//검색_callback 함수가 들어갈곳
//callback 함수
var callback_PerformSchedulerAddTable = function(result) {
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	
	json_string_callback_common(JSON.stringify(data), '#scheduler_history_form  #tableList', true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	fnEnableDisablePagingBtn(dataLength);
};

function Excel_Download(){
	if(!formValidationCheck()) {
		return false;
	}
	
	$("#scheduler_history_form").attr("action","/ExamineOpenPOP/PerformSchedulerDetail/ExcelDown");
	$("#scheduler_history_form").submit();
}

function formValidationCheck(){
	return true;
}

// Scheduler_History_Detail
function goDetailView(selRow){
	$("#scheduler_history_detail_form #job_exec_no").val(selRow.job_exec_no);
	$("#scheduler_history_detail_form #list_job_scheduler_type_cd").val($("#scheduler_history_form #job_scheduler_type_cd").val());
	$("#scheduler_history_detail_form #job_scheduler_type_cd").val(selRow.job_scheduler_type_cd);
	$("#scheduler_history_detail_form #job_scheduler_type_nm").val(selRow.job_scheduler_type_nm);
	
	Btn_OnClick_Detail();
}

function Btn_OnClick_Detail(){
//	/* modal progress open */
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케쥴러 수행 내역 - 상세"," "); 
	
	$('#scheduler_history_detail_form #tableList').datagrid('loadData',[]);
	$('#scheduler_history_detail_form #tableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#scheduler_history_detail_form #tableList').datagrid('loading');

	ajaxCall("/ExamineOpenPOP/PerformSchedulerDetailAction",
			$("#scheduler_history_detail_form"),
			"POST",
			callback_PerformSchedulerDetailAddTable);
}

//callback 함수
var callback_PerformSchedulerDetailAddTable = function(result) {
	var data = JSON.parse(result);
	
	json_string_callback_common(JSON.stringify(data), '#scheduler_history_detail_form  #tableList', false);
};
