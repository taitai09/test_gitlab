$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isAll=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadSuccess: function(items) {
			if (items.length){
				var dbid = $("#submit_form #dbid").val();
		    	if(dbid != '' && dbid != 'null'){
		    		$(this).combobox('setValue', dbid);
		    		
		    		var callFromParent = $("#call_from_parent").val();
		    		var callFromChild = $("#call_from_child").val();
		    		if(callFromParent == "Y" || callFromChild == "Y"){
		    			Btn_OnClick();
		    		}		    		
		    	}
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	// Database 조회			
	$('#selectDbidCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// 튜닝 담당자 조회			
	    	$('#perfr_id').combobox({
	    	    url:"/Common/getTuner?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'tuner_id',
	    	    textField:'tuner_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	// parsing_schema_name 조회			
//	    	$('#selectParsingSchemaName').combobox({
//	    	    url:"/SelfTuning/getParsingSchemaName?dbid="+rec.dbid,
//	    	    method:"get",
//	    		valueField:'username',
//	    	    textField:'username'
//	    	});
	    	
	    	$("#selectParsingSchemaName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});	    	
	    }
	});	
	
	// 수집주기 조회			
	$('#gather_cycle_div_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1001",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});
	
	// 수집범위 조회			
	$('#gather_range_div_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1002",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	
	// 정렬구분 조회			
	$('#order_div_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1010",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
	    onLoadSuccess : function(){
	    	//console.log("onLoadSuccess");
			//$(this).combobox('select', items[0][""]);
            //$(this).combobox('setValue','');
	    }
	});

	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'auto_choice_cond_no',title:'자동선정조건번호',halign:"center",align:'center',sortable:"true"},
			{field:'dbid',title:'DB',hidden:'true'},
			{field:'db_name',title:'DB',halign:"center",align:"center",sortable:"true"},			
			{field:'program_type_cd',title:'프로그램유형코드',hidden:'true'},
			{field:'program_type_cd_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_auto_assign_yn',title:'자동할당여부',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_id',title:'튜닝담당자',hidden:'true'},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:'center',sortable:"true",sorter:sorterString},
			{field:'gather_cycle_div_cd',title:'수집주기코드',hidden:'true'},
			{field:'gather_cycle_div_nm',title:'수집주기',halign:"center",align:'center',sortable:"true"},
			{field:'gather_range_div_cd',title:'수집주기코드',hidden:'true'},
			{field:'gather_range_div_nm',title:'수집범위',halign:"center",align:'center',sortable:"true"},
			{field:'choice_start_day',title:'선정시작일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'choice_end_day',title:'선정종료일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'before_choice_sql_except_yn',title:'이전 선정 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'before_tuning_sql_except_yn',title:'이전 튜닝 SQL 제외',halign:"center",align:'center',sortable:"true"},
			{field:'elap_time',title:'Elapsed Time(sec)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'buffer_cnt',title:'Buffer Gets',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'exec_cnt',title:'Executions',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'topn_cnt',title:'TOP N',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'order_div_cd',title:'ORDERED코드',hidden:'true'},
			{field:'order_div_nm',title:'Ordered',halign:"center",align:'center',sortable:"true"},
			{field:'module1',title:'Module명 1',halign:"center",align:'left',sortable:"true",sorter:sorterString},
			{field:'module2',title:'Module명 2',halign:"center",align:'left',sortable:"true",sorter:sorterString},
			{field:'parsing_schema_name',title:'Parsing Schema Name',halign:"center",align:'left',sortable:"true",sorter:sorterString},
			{field:'sql_text',title:'SQL TEXT',width:"300px",halign:"center",align:'left',sortable:"true",sorter:sorterString},
			{field:'choicer_nm',title:'선정자',halign:"center",align:'center',sortable:"true"},
			{field:'choice_dt',title:'선정일시',halign:"center",align:'center',sortable:"true"},
			{field:'appl_filter_yn',title:'애플리케이션 필터 여부',halign:"center",align:'center',sortable:"true"},
			{field:'use_yn',title:'사용 여부',halign:"center",align:'center',sortable:"true"},
			{field:'del_yn',title:'삭제 여부',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#chkPerfrId').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#perfr_auto_assign_yn").val("Y");
				$("#perfr_id").combobox("readonly", true);
				$("#perfr_id").combobox("setValue", "");
			}else{
				$("#perfr_auto_assign_yn").val("N");
				$("#perfr_id").combobox("readonly", false);
				$("#perfr_id").combobox("setValue", "");
			}
		}
	});	
	
	$('#chkChoiceExcept').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#before_choice_sql_except_yn").val("Y"); 
			else $("#before_choice_sql_except_yn").val("N");
		}
	});
	
	$('#chkTuningExcept').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#before_tuning_sql_except_yn").val("Y"); 
			else $("#before_tuning_sql_except_yn").val("N");
		}
	});
	
	// 애플리케이션 필터 여부			
	$('#chkApplFilterYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#appl_filter_yn").val("Y"); 
			else $("#appl_filter_yn").val("N");
		}
	});	
	
	// 사용 여부			
	$('#chkUseYn').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#use_yn").val("Y"); 
			else $("#use_yn").val("N");
		}
	});
	
	// 삭제 여부			
	$('#chkDelYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#del_yn").val("Y"); 
			else $("#del_yn").val("N");
		}
	});	
	
	$(".chkNum").textbox({
		onChange: function(){
			if(!onlyNumChk($(this).textbox("getValue"))){
				$.messager.alert('','숫자만 입력해 주세요.');
				$(this).textbox("setValue","");
				$(this).textbox('clear').textbox('textbox').focus();
			}
		}
	});

});

function Btn_OnClick(){
	$("#submit_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#selectDbidCombo").combobox("readonly", false);

	$('#tableList').datagrid('loadData',[]);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("자동선정"," "); 

	ajaxCall("/AutoSelectionAction",
			$("#submit_form"),
			"POST",
			callback_AutoSelectionAction);		
}

//callback 함수
var callback_AutoSelectionAction = function(result) {
	Btn_ResetField();
	
	json_string_callback_common(result,'#tableList',true);
};
//저장버튼
function Btn_SaveAutoChoice(){
	if($("#selectDbidCombo").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#perfr_auto_assign_yn").val() == "N" && $("#perfr_id").combobox('getValue') == ""){
		parent.$.messager.alert('','튜닝담당자를 선택해 주세요.');
		return false;
	}
	
	if($("#program_type_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','프로그램 유형을 선택해 주세요.');
		return false;
	}
	
	if($("#gather_cycle_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','수집주기를 선택해 주세요.');
		return false;
	}
	
	if($("#gather_range_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','수집범위를 선택해 주세요.');
		return false;
	}	
	
	if($("#choice_start_day").datebox('getValue') == ""){
		parent.$.messager.alert('','선정시작일을 입력해 주세요.');
		return false;
	}
	
	if($("#choice_end_day").datebox('getValue') == ""){
		parent.$.messager.alert('','선정종료일을 입력해 주세요.');
		return false;
	}
	
	if($("#elap_time").textbox('getValue') == ""){
		parent.$.messager.alert('','Elapsed Time을 입력해 주세요.');
		return false;
	}
	
	if($("#buffer_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','Buffer Gets을 입력해 주세요.');
		return false;
	}
	
	if($("#exec_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','Executions을 입력해 주세요.');
		return false;
	}	
	
//	if($("#selectParsingSchemaName").combobox('getValue') == ""){
//		parent.$.messager.alert('','PARSING_SCHEMA를 선택해 주세요.');
//		return false;
//	}	
	
	if($("#topn_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','TOP N을 입력해 주세요.');
		return false;
	}	
	
	if($("#order_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','Ordered를 선택해 주세요.');
		return false;
	}
	
	$("#parsing_schema_name").val($('#selectParsingSchemaName').combobox("getValue"));

	$("#detail_form #dbid").val($("#selectDbidCombo").combobox('getValue'));
	$("#detail_form #db_name").val($("#selectDbidCombo").combobox('getText'));

	ajaxCall("/AutoSelection/Save",
			$("#detail_form"),
			"POST",
			callback_SaveAutoSelectionAction);
}

//callback 함수
var callback_SaveAutoSelectionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','자동선정조건 정보 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_AutoChoiceHistory(){
	var menuId = $("#menu_id").val()+1;
	var menuNm = "자동 선정  이력";
	var menuUrl = "/AutoSelectionHistory";
	var menuParam = 
		"dbid=" + $("#selectDbidCombo").combobox("getValue")
		+ "&db_name=" + $("#selectDbidCombo").combobox("getText")
		+ "&auto_choice_cond_no=" + $("#tableList").datagrid("getSelected").auto_choice_cond_no
		+ "&nav_from_parent=Y" 
		;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

function Btn_AutoChoiceStatus(){
	if($("#tableList").datagrid("getSelected") == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	var menuId = $("#menu_id").val()+1;
	var menuNm = "자동 선정 현황";
	var menuUrl = "/AutoSelectionStatus";
	var menuParam = 
		"dbid=" + $("#selectDbidCombo").combobox("getValue")
		+ "&db_name=" + $("#selectDbidCombo").combobox("getText")
		+ "&auto_choice_cond_no=" + $("#tableList").datagrid("getSelected").auto_choice_cond_no
		+ "&nav_from_parent=Y" 
		;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

function setDetailView(selRow){
	var chkPerfr = false;
	var chkChoice = false;
	var chkTuning = false;
	var chkApplFilter = false;
	var chkUseYn = false;
	var chkDelYn = false;
	var startDay = ""; 
	var endDay = "";
	$("#selectDbidCombo").combobox("setValue", selRow.dbid);
	
	$("#selectDbidCombo").combobox("readonly", true);

	$("#auto_choice_cond_no").val(selRow.auto_choice_cond_no);
	$("#perfr_auto_assign_yn").val(selRow.perfr_auto_assign_yn);
	
	$('#detail_form #select_auto_choice_cond_no').val(selRow.auto_choice_cond_no);
	$('#detail_form #dbid').val(selRow.dbid);
	
	// 튜닝 담당자 조회			
	$('#perfr_id').combobox({
	    url:"/Common/getTuner?dbid="+selRow.dbid,
	    method:"get",
		valueField:'tuner_id',
	    textField:'tuner_nm'
	});
	
	if(selRow.perfr_auto_assign_yn == "Y"){
		chkPerfr = true;
		$("#perfr_id").combobox("readonly", true);
	}else{
		$("#perfr_id").combobox("readonly", false);
	}
	
	$("#program_type_cd").combobox("setValue", selRow.program_type_cd);
	
	$('#chkPerfrId').switchbutton({checked: chkPerfr});
	$("#perfr_id").combobox("setValue", selRow.perfr_id);	
	$("#gather_cycle_div_cd").combobox("setValue", selRow.gather_cycle_div_cd);
	$("#gather_range_div_cd").combobox("setValue", selRow.gather_range_div_cd);	

	if(selRow.choice_start_day != "" && selRow.choice_start_day != null){
		startDay = selRow.choice_start_day.substr(0,4) + "-" + selRow.choice_start_day.substr(4,2) + "-" + selRow.choice_start_day.substr(6,2)
    }
	
	if(selRow.choice_end_day != "" && selRow.choice_end_day != null){
		endDay = selRow.choice_end_day.substr(0,4) + "-" + selRow.choice_end_day.substr(4,2) + "-" + selRow.choice_end_day.substr(6,2)
    }
	
	$("#choice_start_day").textbox("setValue", startDay);
	$("#choice_end_day").textbox("setValue", endDay);
	
	$("#before_choice_sql_except_yn").val(selRow.before_choice_sql_except_yn);
	if(selRow.before_choice_sql_except_yn == "Y") chkChoice = true;
	
	$('#chkChoiceExcept').switchbutton({checked: chkChoice});
	
	$("#before_tuning_sql_except_yn").val(selRow.before_tuning_sql_except_yn);
	if(selRow.before_tuning_sql_except_yn == "Y") chkTuning = true;
	
	$('#chkTuningExcept').switchbutton({checked: chkTuning});	

	$("#elap_time").textbox("setValue", selRow.elap_time);
	$("#buffer_cnt").textbox("setValue", selRow.buffer_cnt);
	$("#exec_cnt").textbox("setValue", selRow.exec_cnt);
	$("#module1").textbox("setValue", selRow.module1);
	$("#module2").textbox("setValue", selRow.module2);
	$('#selectParsingSchemaName').combobox("setValue",selRow.parsing_schema_name);
	$("#sql_text").textbox("setValue", selRow.sql_text);
	$("#topn_cnt").textbox("setValue", selRow.topn_cnt);
	$("#order_div_cd").combobox("setValue", selRow.order_div_cd);
	
	$("#appl_filter_yn").val(selRow.appl_filter_yn);
	if(selRow.appl_filter_yn == "Y") chkApplFilter = true;
	
	$('#chkApplFilterYn').switchbutton({checked: chkApplFilter});
	
	$("#use_yn").val(selRow.use_yn);
	if(selRow.use_yn == "Y") chkUseYn = true;
	
	$('#chkUseYn').switchbutton({checked: chkUseYn});
	
	$("#del_yn").val(selRow.del_yn);
	if(selRow.del_yn == "Y") chkDelYn = true;
	
	$('#chkDelYn').switchbutton({checked: chkDelYn});

	$("#historyBtn").linkbutton({disabled:false});
}
//초기화 버튼
function Btn_ResetField(){
	$("#tableList").datagrid("clearSelections");
	
	$("#selectDbidCombo").combobox("readonly", false);
	$("#selectDbidCombo").combobox("setValue", "");
	$("#auto_choice_cond_no").val("");
	$("#perfr_auto_assign_yn").val("N");
	$("#before_choice_sql_except_yn").val("Y");
	$("#before_tuning_sql_except_yn").val("Y");
	$("#appl_filter_yn").val("N");
	$("#use_yn").val("Y");
	$("#del_yn").val("N");
	$("#parsing_schema_name").val("");
	$("#perfr_id").combobox("readonly", false);
	$('#chkPerfrId').switchbutton({checked: false});
	$("#perfr_id").combobox("setValue", "");	
	$("#program_type_cd").combobox("setValue", "");
	$("#gather_cycle_div_cd").combobox("setValue", "");
	$("#gather_range_div_cd").combobox("setValue", "");
	$("#choice_start_day").textbox("setValue", "");
	$("#choice_end_day").textbox("setValue", "");

	$("#elap_time").textbox("setValue", "");
	$("#buffer_cnt").textbox("setValue", "");
	$("#exec_cnt").textbox("setValue", "");
	$("#module1").textbox("setValue", "");
	$("#module2").textbox("setValue", "");	
	$('#selectParsingSchemaName').combobox("setValue","");
	$("#sql_text").textbox("setValue", "");
	$("#topn_cnt").textbox("setValue", "");
	$("#order_div_cd").combobox("setValue", "");
	$('#chkChoiceExcept').switchbutton({checked: true});
	$('#chkTuningExcept').switchbutton({checked: true});
	$('#chkApplFilterYn').switchbutton({checked: false});
	$('#chkUseYn').switchbutton({checked: true});
	$('#chkDelYn').switchbutton({checked: false});
	
	$("#historyBtn").linkbutton({disabled:true});
}

function Excel_Download(){
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));

	$("#submit_form").attr("action","/AutoSelection/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
