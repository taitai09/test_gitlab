var menuArray = ["database_status","expired_grace_account","modified_parameter","new_created_object","instance_status","listener_status","dbfiles","library_cache_hit",
"dictionary_cache_hit","buffer_cache_hit","latch_hit","parse_cpu_to_parse_elapsd","disk_sort","memory_usage","resource_limit","background_dump_space",
"archive_log_space","alert_log_space","fra_space","asm_diskgroup_space","tablespace","recyclebin_object","invalid_object","nologging_object","parallel_object",
"unusable_index","chained_rows","corrupt_block","sequence","foreignkeys_without_index","disabled_constraint","missing_or_stale_statistics","statistics_locked_table",
"long_running_operation","long_running_job","alert_log_error","active_incident","outstanding_alert","dbms_scheduler_job_failed"]; 
var menuId = "";
var menuNm = "";
var dbListLoadSuccess = false;
var callFromParent = "";
var check_seq = "0";
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbid').combobox({
	    url:"/Common/getDatabase?isAll=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onLoadSuccess:function(rec){
	    	dbListLoadSuccess = true;
	    	var dbid = $("#dbid").val();
	    	if(dbid != '' && dbid != 'null'){
	    		$(this).combobox('setValue', dbid);
	    	}
	    },
	    onSelect:function(rec){
//	    	var win = parent.$.messager.progress({
//	    		title:'Please waiting',
//	    		text:'점검회차를 불러오는 중입니다.'
//	    	});
	    	if(dbListLoadSuccess){
	    		console.log("dbid1:"+rec.dbid); 
	    		$("#dbid").val(rec.dbid);
//	    		var selDate = $("#check_day").val();
	    		
//	    		$('#tableList').datagrid("loadData", []);
	    		// 점검회차 설정
	    		//setCheckSeq(selDate); 2019-08-21
	    	}
	    }
	    
	});	

	$("#strStartDt").datebox({
		editable: false,
		/*onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			console.log("y:"+y);
			console.log("m:"+m);
			console.log("d:"+d);
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			console.log("selDate:"+selDate);
			
    		$('#tableList').datagrid("loadData", []);
			// 점검회차 설정
			//setCheckSeq(selDate); 2019-08-21
		},*/
	    onChange:  function(newValue,oldValue) {
			//setCheckSeq(newValue); 2019-08-21
	    }		
	});
	
	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	//setCheckSeq($("#check_day").val()); 2019-08-21

	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		onClickCell : function(index,field,value) {
			
			var row = $(this).datagrid('getRows')[index];
			$("#dbid").val(row.dbid);
			$("#check_item_name").val(setJspName(field));
			console.log(field, value);
			//
			parent.noModifyDocSize = true;
			if(createTab(field, value)){
				$("#dbid").val('');
			};
			parent.noModifyDocSize = false
		},
		columns:[[
			{field:'db_name',width:'4%',title:'DB',halign:"center",align:"center",rowspan:2, sortable:"true"},
			{field:'dbid',width:'4%',title:'DBID',hidden:"true",rowspan:2},
			{field:'check_seq',width:'4%',title:'check_seq',hidden:"true",rowspan:2},
//			{field:'health',hidden:true},
			{field:'check_dt',width:'7%',title:'점검일시',halign:"center",align:"center",rowspan:2},
//			{field:'health',width:'3%',title:'HEALTH',halign:"center",align:"center",rowspan:2,formatter:getHealthImg},
			{field:'advisor_recommendation',width:'4%',title:'ADVISOR<br/>RECOMMENDATION',halign:"center",align:"center",rowspan:2},
			{title:'<b>DB</b>',halign:"center",colspan:4},
			{title:'<b>INSTANCE</b>',halign:"center",colspan:11},
			{title:'<b>SPACE</b>',halign:"center",colspan:7},
			{title:'<b>OBJECT</b>',halign:"center",colspan:9},			
			{title:'<b>STATISTICS',halign:"center",colspan:2},
			{title:'<b>LONG RUNNING WORK</b>',halign:"center",colspan:2},			
			{title:'<b>ALERT</b>',halign:"center",colspan:4}			
		],[
			{field:'database_status',width:'4%',title:'DATABASE<br/>STATUS',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'expired_grace_account',width:'4%',title:'EXPIRED<br/>GRACE<br/>ACCOUNT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'modified_parameter',width:'4%',title:'MODIFIED<br/>PARAMETER',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'new_created_object',width:'4%',title:'NEW<br/>CREATED<br/>OBJECT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},			
			{field:'instance_status',width:'4%',title:'INSTANCE<br/>STATUS',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'listener_status',width:'4%',title:'LISTENER<br/>STATUS',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'dbfiles',width:'4%',title:'DBFILES',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'library_cache_hit',width:'4%',title:'LIBRARY<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'dictionary_cache_hit',width:'4%',title:'DICTIONARY<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'buffer_cache_hit',width:'4%',title:'BUFFER<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'latch_hit',width:'4%',title:'LATCH HIT(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'parse_cpu_to_parse_elapsd',width:'4%',title:'PARSE CPU<br/>TO<br/>PARSE ELAPSD(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'disk_sort',width:'4%',title:'DISK SORT(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'memory_usage',width:'4%',title:'MEMORY<br/>USAGE(%)',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'resource_limit',width:'4%',title:'RESOURCE<br/>LIMIT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},			
			{field:'background_dump_space',width:'4%',title:'BACKGROUND<br/>DUMP<br/>SPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'archive_log_space',width:'4%',title:'ARCHIVE<br/>LOG<br/>SPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'alert_log_space',width:'4%',title:'ALERT<br/>LOG<br/>SPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'fra_space',width:'4%',title:'FRA<br/>SPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'asm_diskgroup_space',width:'4%',title:'ASM<br/>DISKGROUP<br/>SPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'tablespace',width:'4%',title:'TABLESPACE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'recyclebin_object',width:'4%',title:'RECYCLEBIN<br/>OBJECT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},			
			{field:'invalid_object',width:'4%',title:'INVALID<br/>OBJECT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'nologging_object',width:'4%',title:'NOLOGGING<br/>OBJECT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'parallel_object',width:'4%',title:'PARALLEL<br/>OBJECT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'unusable_index',width:'4%',title:'UNUSABLE<br/>INDEX',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'chained_rows',width:'4%',title:'CHAINED<br/>ROWS',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'corrupt_block',width:'4%',title:'CORRUPT<br/>BLOCK',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'sequence',width:'4%',title:'SEQUENCE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'foreignkeys_without_index',width:'4%',title:'FOREIGNKEYS<br/>WITHOUT<br/>INDEX',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'disabled_constraint',width:'4%',title:'DISABLED<br/>CONSTRAINT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'missing_or_stale_statistics',width:'4%',title:'MISSING<br/>OR<br/>STALE<br/>STATISTICS',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'statistics_locked_table',width:'4%',title:'STATISTICS<br/>LOCKED<br/>TABLE',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},			
			{field:'long_running_operation',width:'4%',title:'LONG<br/>RUNNING<br/>OPERATION',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'long_running_job',width:'4%',title:'LONG<br/>RUNNING<br/>JOB',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'alert_log_error',width:'4%',title:'ALERT<br/>LOG<br/>ERROR',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'active_incident',width:'4%',title:'ACTIVE<br/>INCIDENT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'outstanding_alert',width:'4%',title:'OUTSTANDING<br/>ALERT',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg},
			{field:'dbms_scheduler_job_failed',width:'4%',title:'DBMS<br/>SCHEDULER<br/>JOB<br/>FAILED',halign:"center",align:"center",styler:cellStyler,formatter:getStatusImg}
		]],		
		onLoadSuccess : function(data){
			if(data != null && data.rows.length > 0 && data.rows[0].check_seq > 0){
				$("#check_seq").val(data.rows[0].check_seq);
			}
		},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	//엑셀용
	$("#tableList2").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'db_name',width:'5%',title:'DB',halign:"center",align:"center",rowspan:2,sortable:"true"},
//			{field:'dbid',title:'DBID',hidden:"true",rowspan:2},	// 화면 그리드 타이틀과 엑셀 타이틀이 차이가 났어 주석처리함 
			{field:'check_dt',width:'7%',title:'점검일시',halign:"center",align:"center",rowspan:2},
//			{field:'health',width:'5%',title:'HEALTH',halign:"center",align:"center",rowspan:2,styler:cellStyler,formatter:getHealthStatus},
			{field:'advisor_recommendation',width:'5%',title:'ADVISOR<br/>RECOMMENDATION',halign:"center",align:"center",rowspan:2},
			{title:'<b>DB</b>',halign:"center",colspan:4},
			{title:'<b>INSTANCE</b>',halign:"center",colspan:11},
			{title:'<b>SPACE</b>',halign:"center",colspan:7},
			{title:'<b>OBJECT</b>',halign:"center",colspan:9},			
			{title:'<b>STATISTICS',halign:"center",colspan:2},
			{title:'<b>LONG RUNNING WORK</b>',halign:"center",colspan:2},			
			{title:'<b>ALERT</b>',halign:"center",colspan:4}			
		],[
			{field:'database_status',width:'5%',title:'DATABASE<br/>STATUS',halign:"center",align:"center",styler:cellStyler},
			{field:'expired_grace_account',width:'5%',title:'EXPIRED<br/>GRACE<br/>ACCOUNT',halign:"center",align:"center",styler:cellStyler},
			{field:'modified_parameter',width:'5%',title:'MODIFIED<br/>PARAMETER',halign:"center",align:"center",styler:cellStyler},
			{field:'new_created_object',width:'5%',title:'NEW<br/>CREATED<br/>OBJECT',halign:"center",align:"center",styler:cellStyler},			
			{field:'instance_status',width:'5%',title:'INSTANCE<br/>STATUS',halign:"center",align:"center",styler:cellStyler},
			{field:'listener_status',width:'5%',title:'LISTENER<br/>STATUS',halign:"center",align:"center",styler:cellStyler},
			{field:'dbfiles',width:'5%',title:'DBFILES',halign:"center",align:"center",styler:cellStyler},
			{field:'library_cache_hit',width:'5%',title:'LIBRARY<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'dictionary_cache_hit',width:'5%',title:'DICTIONARY<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'buffer_cache_hit',width:'5%',title:'BUFFER<br/>CACHE HIT(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'latch_hit',width:'5%',title:'LATCH HIT(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'parse_cpu_to_parse_elapsd',width:'5%',title:'PARSE CPU<br/>TO<br/>PARSE ELAPSD(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'disk_sort',width:'5%',title:'DISK SORT(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'memory_usage',width:'5%',title:'MEMORY<br/>USAGE(%)',halign:"center",align:"center",styler:cellStyler},
			{field:'resource_limit',width:'5%',title:'RESOURCE<br/>LIMIT',halign:"center",align:"center",styler:cellStyler},			
			{field:'background_dump_space',width:'5%',title:'BACKGROUND<br/>DUMP<br/>SPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'archive_log_space',width:'5%',title:'ARCHIVE<br/>LOG<br/>SPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'alert_log_space',width:'5%',title:'ALERT<br/>LOG<br/>SPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'fra_space',width:'5%',title:'FRA<br/>SPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'asm_diskgroup_space',width:'5%',title:'ASM<br/>DISKGROUP<br/>SPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'tablespace',width:'5%',title:'TABLESPACE',halign:"center",align:"center",styler:cellStyler},
			{field:'recyclebin_object',width:'5%',title:'RECYCLEBIN<br/>OBJECT',halign:"center",align:"center",styler:cellStyler},			
			{field:'invalid_object',width:'5%',title:'INVALID<br/>OBJECT',halign:"center",align:"center",styler:cellStyler},
			{field:'nologging_object',width:'5%',title:'NOLOGGING<br/>OBJECT',halign:"center",align:"center",styler:cellStyler},
			{field:'parallel_object',width:'5%',title:'PARALLEL<br/>OBJECT',halign:"center",align:"center",styler:cellStyler},
			{field:'unusable_index',width:'5%',title:'UNUSABLE<br/>INDEX',halign:"center",align:"center",styler:cellStyler},
			{field:'chained_rows',width:'5%',title:'CHAINED<br/>ROWS',halign:"center",align:"center",styler:cellStyler},
			{field:'corrupt_block',width:'5%',title:'CORRUPT<br/>BLOCK',halign:"center",align:"center",styler:cellStyler},
			{field:'sequence',width:'5%',title:'SEQUENCE',halign:"center",align:"center",styler:cellStyler},
			{field:'foreignkeys_without_index',width:'5%',title:'FOREIGNKEYS<br/>WITHOUT<br/>INDEX',halign:"center",align:"center",styler:cellStyler},
			{field:'disabled_constraint',width:'5%',title:'DISABLED<br/>CONSTRAINT',halign:"center",align:"center",styler:cellStyler},
			{field:'missing_or_stale_statistics',width:'5%',title:'MISSING<br/>OR<br/>STALE<br/>STATISTICS',halign:"center",align:"center",styler:cellStyler},
			{field:'statistics_locked_table',width:'5%',title:'STATISTICS<br/>LOCKED<br/>TABLE',halign:"center",align:"center",styler:cellStyler},			
			{field:'long_running_operation',width:'5%',title:'LONG<br/>RUNNING<br/>OPERATION',halign:"center",align:"center",styler:cellStyler},
			{field:'long_running_job',width:'5%',title:'LONG<br/>RUNNING<br/>JOB',halign:"center",align:"center",styler:cellStyler},
			{field:'alert_log_error',width:'5%',title:'ALERT<br/>LOG<br/>ERROR',halign:"center",align:"center",styler:cellStyler},
			{field:'active_incident',width:'5%',title:'ACTIVE<br/>INCIDENT',halign:"center",align:"center",styler:cellStyler},
			{field:'outstanding_alert',width:'5%',title:'OUTSTANDING<br/>ALERT',halign:"center",align:"center",styler:cellStyler},
			{field:'dbms_scheduler_job_failed',width:'5%',title:'DBMS<br/>SCHEDULER<br/>JOB<br/>FAILED',halign:"center",align:"center",styler:cellStyler}
		]],		

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$('#chkItems').switchbutton({
		checked: true,
		onText:"필수",
		offText:"전체",
		onChange: function(checked){
			if(checked){
				setTableShowHideColumn("hideColumn");
			}else{
				setTableShowHideColumn("showColumn");
			}
		}
	});

	callFromParent = $("#call_from_parent").val();
	
	if(callFromParent == "Y"){
		$('#strStartDt').textbox('setValue',$("#check_day_dash").val());
//		setCheckSeq($("#check_day").val()); 2019-08-21
		Btn_OnClick();
	}
	setTableShowHideColumn("hideColumn");
});

function Btn_OnClick(){
	/*if($('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}*/
	fnSearch();
}

function fnSearch(){

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
//	$("#check_seq").val($('#selectCheckSeq').combobox('getValue')); 
	
	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검"," "); 

	ajaxCall("/PreventiveCheck/Summary", $("#submit_form"), "POST", callback_SummaryAction);
}

//callback 함수
var callback_SummaryAction = function(result) {
	json_string_callback_common(result, '#tableList', true);
	//엑셀 다운로드를 위해 tableList2를 생성
	json_string_callback_common(result, '#tableList2', true);

};

/* 일 예방 점검 탭 생성 */
function createTab(fieldName, strVal){
	if(fieldName == 'check_dt') return false;
	
	var menuUrl = "/PreventiveCheck/DetailCheckInfo";
	var menuParam = "dbid="+$("#dbid").val()
					+"&strStartDt="+$("#strStartDt").val()
					+"&check_item_name="+$("#check_item_name").val()
					+"&check_day="+$("#check_day").val()
					+"&check_seq="+$("#check_seq").val();
	
	console.log("filedName:"+fieldName);
	if(fieldName != "db_name" && fieldName != "health"){
		//if(strVal == "N"){ // 문제가 있는 상태에 링크 생성
			setNewTabInfo(fieldName);
		//}
			/* 탭 생성 */
			parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
	}
	
	return true;
	
}

function setNewTabInfo(fieldName){
	for(var i = 0 ; i < menuArray.length ; i++){
		if(fieldName == menuArray[i]){
			menuId = $("#menu_id").val() + i;
			break;
		}
	}
	menuNm = $('#menu_nm').val() + ' - ' + strReplace(fieldName, "_"," ").toUpperCase();
}

/* 점검 Health 이미지 생성 */
function getHealthImg(val, row) {
    return "<img src='/resources/images/"+val.toLowerCase()+".png' style='vertical-align:bottom;'/>";
}

/* 점검상태 이미지 생성 */
function getStatusImg(val, row) {
	if(val == "Y"){
		return "<img src='/resources/images/status_n_new.png' style='vertical-align:center;padding-bottom:1px;'/>";	
	}else if(val == "N"){
		return "<img src='/resources/images/status_y_new.png' style='vertical-align:bottom;padding-left:1px;padding-bottom:2px;'/>";
	}
}

function getHealthStatus(value, row) {
	return "";
//	if(nvl(value,"") == "STATUS_RED"){
//		return "O";
//	}else if(nvl(value,"") == "STATUS_GREEN"){
//		return "O";
//	}else if(nvl(value,"") == "STATUS_YELLOW"){
//		return "O";
//	}else if(nvl(value,"") == "STATUS_NONE"){
//		return "O";
//	}
}

function cellStyler(value,row,index){
	if(nvl(value,"") == ""){
		return 'background-color:#ebe9e9;';
	}
//	else if(nvl(value,"") == "N"){
//		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
//	}
	else if(nvl(value,"") == "STATUS_RED"){
		return 'background-color:red;color:#ffffff;';		
//		return 'background-color:white;color:red;';		
	}else if(nvl(value,"") == "STATUS_GREEN"){
		return 'background-color:green;color:#ffffff;';		
//		return 'background-color:green;color:green;';		
	}else if(nvl(value,"") == "STATUS_YELLOW"){
		return 'background-color:yellow;color:#ffffff;';		
//		return 'background-color:yellow;color:yellow;';		
	}else if(nvl(value,"") == "STATUS_NONE"){
		return 'background-color:gray;color:#ffffff;';		
//		return 'background-color:gray;color:gray;';		
	}
}

function setTableShowHideColumn(strGubun){
	$("#tableList").datagrid(strGubun, "advisor_recommendation");
	
	$("#tableList").datagrid(strGubun, "modified_parameter");
	$("#tableList").datagrid(strGubun, "new_created_object");	
	
	$("#tableList").datagrid(strGubun, "library_cache_hit");
	$("#tableList").datagrid(strGubun, "dictionary_cache_hit");
	$("#tableList").datagrid(strGubun, "buffer_cache_hit");
	$("#tableList").datagrid(strGubun, "latch_hit");
	$("#tableList").datagrid(strGubun, "parse_cpu_to_parse_elapsd");
	$("#tableList").datagrid(strGubun, "disk_sort");
	$("#tableList").datagrid(strGubun, "memory_usage");	

	$("#tableList").datagrid(strGubun, "recyclebin_object");
	$("#tableList").datagrid(strGubun, "chained_rows");
	$("#tableList").datagrid(strGubun, "statistics_locked_table");
	$("#tableList").datagrid(strGubun, "long_running_operation");
	$("#tableList").datagrid(strGubun, "long_running_job");	

	$("#tableList").datagrid(strGubun, "alert_log_error");
	$("#tableList").datagrid(strGubun, "active_incident");
	$("#tableList").datagrid(strGubun, "outstanding_alert");
	$("#tableList").datagrid(strGubun, "dbms_scheduler_job_failed");
	
	if(strGubun == "hideColumn"){
		setParentColspan("STATISTICS", 1);
		setParentColspan("LONG RUNNING WORK", 0);
		setParentColspan("ALERT", 0);
	}else{
		setParentColspan("STATISTICS", 2);
		setParentColspan("LONG RUNNING WORK", 2);
		setParentColspan("ALERT", 4);
	}
}

function setParentColspan(columnText, colSpan) {
	var dc = $('#tableList').data('datagrid').dc,
	    htable = dc.header2.find('.datagrid-htable');

	htable.find('tr.datagrid-header-row:first td').each(function() {
		var innerHtml = $(this).html();
		if (innerHtml.indexOf(columnText) > -1) {
			$(this).attr('colspan', colSpan);
			
			if(colSpan == 0){
				$(this).attr('style', 'display:none;');
			}else{
				$(this).attr('style', '');
			}
		}
	});
}

/* jsp 파일명 생성 */
function setJspName(fileName){
	var	fileArry = fileName.split('_');
	var newFileName = "";

	for(var i = 0 ; i < fileArry.length ; i++){
		if(i == 0){
			newFileName = fileArry[i];	
		}else{
			newFileName += fileArry[i].substr(0,1).toUpperCase() + fileArry[i].substring(1, fileArry[i].length);
		}		
	}
	
	return newFileName;
}

function fnOpenExceptionManageTab(){
	var menuId = "1181";
	var menuNm = "일 예방 점검 예외 관리";
	var menuUrl = "/ExceptionManagement/ExceptionManagement";
	var menuParam = "dbid="+$("#dbid").val()
					+"&strStartDt="+$("#strStartDt").val()
					+"&check_item_name="+$("#check_item_name").val()
					+"&check_day="+$("#check_day").val()
					+"&check_seq="+$("#check_seq").val();
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}