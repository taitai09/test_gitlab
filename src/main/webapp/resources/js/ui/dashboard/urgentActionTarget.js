var menuArray = ["database_status","expired_grace_account","modified_parameter","new_created_object","instance_status","listener_status","dbfiles","library_cache_hit",
"dictionary_cache_hit","buffer_cache_hit","latch_hit","parse_cpu_to_parse_elapsd","disk_sort","memory_usage","resource_limit","background_dump_space",
"archive_log_space","alert_log_space","fra_space","asm_diskgroup_space","tablespace","recyclebin_object","invalid_object","nologging_object","parallel_object",
"unusable_index","chained_rows","corrupt_block","sequence","foreignkeys_without_index","disabled_constraint","missing_or_stale_statistics","statistics_locked_table",
"long_running_operation","long_running_job","alert_log_error","active_incident","outstanding_alert","dbms_scheduler_job_failed"]; 
var menuId = "";
var menuNm = "";
var dbListLoadSuccess = false;

$(document).ready(function() {
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
			$(this).combobox('setValue', dbid);
	    },
	    onSelect:function(rec){
//	    	var win = parent.$.messager.progress({
//	    		title:'Please waiting',
//	    		text:'점검회차를 불러오는 중입니다.'
//	    	});
	    	if(dbListLoadSuccess){
	    		$("#dbid").val(rec.dbid);
	    		var selDate = $("#check_day").val();
	    		
	    		$('#urgentActionList').datagrid("loadData", []);
	    		// 점검회차 설정
	    		setCheckSeq(selDate);
	    	}
	    }
	});	
	
	// 리스크유형 조회			
	$('#selectRiskType').combobox({
	    url:"/DashboardV2/getBasicCheckPref",
	    method:"get",
		valueField:'check_pref_id',
	    textField:'check_pref_nm',
		onLoadError: function(){
			parent.$.messager.alert('','리스크유형 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onLoadSuccess:function(rec){
	    	var check_pref_id = $("#check_pref_id").val();
			$(this).combobox('setValue', check_pref_id);
	    }
	});	
	
	$("#strStartDt").datebox({
		editable: false,
		onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			
    		$('#urgentActionList').datagrid("loadData", []);
			// 점검회차 설정
			setCheckSeq(selDate);
		}
	});
	
	$("#strStartDt").datebox("setValue",$("#check_day").val());

	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());


	//긴급 조치 대상
	$("#urgentActionList").datagrid({
		view: myview,
		nowrap : false,
		singleSelect:false,
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			$("#dbid").val(row.dbid);
			var check_pref_id = row.check_pref_id;
			if(check_pref_id.length > 3){
				check_pref_id = check_pref_id.substring(1);
			}
			
			var jsp_page = getJspPage(check_pref_id);
			$("#check_item_name").val(jsp_page);
			$("#check_day").val(row.check_day);
			$("#check_seq").val(row.check_seq);
			$("#strStartDt").val(row.check_dt);

			if(field != "action_btn"){
				createTab(row.dbid, jsp_page, row.check_tbl);
			}
		},		
//		onClickRow : function(index,row) {
//			$("#dbid").val(row.dbid);
//			var jsp_page = getJspPage(row.check_pref_id);
//			$("#check_item_name").val(jsp_page);
//			$("#check_day").val(row.check_day);
//			$("#check_seq").val(row.check_seq);
//			$("#strStartDt").val(row.check_day);
//
//		},			
		columns:[[
			{field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'dbid',hidden:"true"},
			{field:'check_pref_id',hidden:"true"},
			{field:'check_day',hidden:"true"},
			{field:'check_seq',hidden:"true"},
			{field:'check_dt',hidden:"true"},
			{field:'check_tbl',hidden:"true"},
			{field:'check_pref_nm',title:'리스크 유형',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_sbst',title:'긴급조치내용',halign:"center",align:'left',sortable:"true"},						
			{field:'emergency_action_yn',title:'조치여부',halign:"center",align:'center',sortable:"true"},						
			{field:'emergency_actor_nm',title:'조치자',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_dt',title:'조치일시',halign:"center",align:'center',sortable:"true"},
			{field:'emergency_action_no',hidden:"true"},
			{field:'emergency_action_target_id',hidden:"true"}
		]],
		onLoadSuccess:function(){
			$(this).datagrid('getPanel').find('a.easyui-linkbutton').linkbutton();
		},
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	Btn_Search();	
});

function Btn_Search(){

	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());
	
	
	if($("#check_day").val() == "" && $('#strStartDt').datebox('getValue') == ""){
		parent.$.messager.alert('','점검일을 입력해 주세요.');
		return false;
	}

	if($("#check_day").val() == "" && $('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_pref_id").val($('#selectRiskType').combobox('getValue'));
	$("#emergency_action_yn").val($('#selectActionType').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("긴급조치대상현황"," "); 

	$('#urgentActionList').datagrid("loadData", []);
	ajaxCallUrgentAction();
}

function ajaxCallUrgentAction(){
	/* 긴급 조치 대상 리스트 */
	ajaxCall("/Dashboard/UrgentAction",
			$("#submit_form"),
			"POST",
			callback_UrgentActionAction);	
}

//callback 함수
var callback_UrgentActionAction = function(result) {
	json_string_callback_common(result, '#urgentActionList', true)
};

/* 긴급 조치 대상 리스트 */
//callback 함수
//var callback_UrgentActionAction = function(result) {
//	var data = JSON.parse(result);
//	$('#urgentActionList').datagrid("loadData", data);
//	$('#urgentActionList').datagrid('loaded');
//};

function setNewTabInfo(fieldName){
	for(var i = 0 ; i < menuArray.length ; i++){
		if(fieldName == menuArray[i]){
			menuId = $("#menu_id").val() + i;
			break;
		}
	}
	menuNm = strReplace(fieldName, "_"," ").toUpperCase(); 
}

function Btn_UpdateUrgentAction(){
	$("#dbid").val($('#selectDbid').combobox('getValue'));
	
	var row = $('#urgentActionList').datagrid('getChecked');
	
	var rowLength = row.length;
	var emergencyActionNo="";
	for(var i=0;i<rowLength;i++){
		emergencyActionNo += row[i].emergency_action_no;
		if(i != rowLength -1){
			emergencyActionNo += ",";
		}
	}

	$("#emergency_action_no").val(emergencyActionNo);
	
	ajaxCall("/Dashboard/UpdateUrgentAction",
			$("#submit_form"),
			"POST",
			callback_UpdateUrgentActionAction);		
}

//callback 함수
var callback_UpdateUrgentActionAction = function(result) {
	if(result.result){
		parent.$.messager.alert({
			msg : '긴급 조치 처리가 완료되었습니다.',
			fn :function(){
				ajaxCallUrgentAction();
			} 
		});
	}else{
		parent.$.messager.alert('error','긴급 조치 처리가 실패하였습니다.');
	}
};

var risk_jsp_page = [[ '001','databaseStatus' ],
	[ '004','instanceStatus' ],
	[ '005','listenerStatus' ],
	[ '006','dbfiles' ],
	[ '019','backgroundDumpSpace' ],
	[ '020','archiveLogSpace' ],
	[ '021','alertLogSpace' ],
	[ '022','fraSpace' ],
	[ '023','asmDiskgroupSpace' ],
	[ '024','tablespace' ],
	[ '026','invalidObject' ],
	[ '029','unusableIndex' ],
	[ '032','sequence']
	];

function getJspPage(check_pref_id){
	//console.log("check_pref_id :"+check_pref_id);
	for(var i=0;i<risk_jsp_page.length;i++){
		var array = risk_jsp_page[i];
		for(var j=0;j<array.length;j++){
			var k = array[0];
			var v = array[1];
			if(check_pref_id == k){
				return v;
			}
		}
	}
	return "";
}

/* 일 예방 점검 탭 생성 */
function createTab(dbid, check_pref_id, check_tbl){
	var menuUrl = "/PreventiveCheck/DetailCheckInfo";
	var menuParam = "dbid=" + $("#dbid").val() 
			+ "&strStartDt=" + $("#strStartDt").val() 
			+ "&check_item_name=" + $("#check_item_name").val() 
			+ "&check_day=" + $("#check_day").val() 
			+ "&check_seq=" + $("#check_seq").val();
	
			setNewTabInfo(check_pref_id,check_tbl);

	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}
