$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onSelect:function(rec) {
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			// 튜닝 담당자 조회
			$('#selectTuner').combobox({
				url:"/Common/getTunerCondition?dbid="+rec.dbid+"&isAll=Y",
				method:"get",
				valueField:'tuner_id',
				textField:'tuner_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$("#selectTuner").combobox('textbox').attr( 'placeholder' , '전체' );
				}
			});
			
			// 튜닝 담당자 조회
			$('#bundle_form #perfr_id_detail').combobox({
				url:"/Common/getTuner?dbid="+rec.dbid,
				method:"get",
				valueField:'tuner_id',
				textField:'tuner_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
			});
		},
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
			
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
			$("#selectTuner").combobox('textbox').attr( 'placeholder' , '전체' );
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#selectProject').combobox({
		url:"/Common/getProject?isAll=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		}
	});
	
	// Database 조회
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$(this).combobox('textbox').attr( 'placeholder' , '선택' );
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			// 튜닝 담당자 조회			
			$('#perfr_id_detail').combobox({
				url:"/Common/getTuner?dbid="+rec.dbid,
				method:"get",
				valueField:'tuner_id',
				textField:'tuner_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#perfr_id_detail').combobox('textbox').attr( 'placeholder' , '선택' );
				}
			});
			
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
	
	$("#program_type_cd_nm").combobox("setValue", "");
	$('#bundle_form #program_type_cd_nm').combobox('setValue', '');
	
	// 수집주기 조회
	$('#gather_cycle_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1001",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function() {
			$('#gather_cycle_div_cd').combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	// 수집범위 조회
	$('#gather_range_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1002",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function() {
			$('#gather_range_div_cd').combobox('textbox').attr( 'placeholder' , '선택' );
		}
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
			$('#order_div_cd').combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : false,
		onClickRow : function(index,row) {
			setDetailView(row);
		},
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'auto_choice_cond_no',title:'선정조건번호',halign:"center",align:'center',sortable:"true"},
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
			{field:'choice_start_day',title:'선정시작일',halign:"center",align:'center',sortable:"true"},
			{field:'choice_end_day',title:'선정종료일',halign:"center",align:'center',sortable:"true"},
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
			{field:'del_yn',title:'삭제 여부',halign:"center",align:'center',sortable:"true"},
			{field:'project_nm',title:'프로젝트',halign:"center",align:'center',sortable:"true"},
			{field:'project_id',title:'project_id',hidden:'true'},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_prgrs_step_seq',title:'tuning_prgrs_step_seq',hidden:'true'},
		]],
		
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#detail_form #chkPerfrId').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#perfr_auto_assign_yn").val("Y");
				$("#perfr_id_detail").combobox("readonly", true);
				$("#perfr_id_detail").combobox("setValue", "");
			}else{
				$("#perfr_auto_assign_yn").val("N");
				$("#perfr_id_detail").combobox("readonly", false);
				$("#perfr_id_detail").combobox("setValue", "");
			}
		}
	});
	
	$('#detail_form #chkChoiceExcept').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#before_choice_sql_except_yn").val("Y");
			else $("#before_choice_sql_except_yn").val("N");
		}
	});
	
	$('#detail_form #chkTuningExcept').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#before_tuning_sql_except_yn").val("Y");
			else $("#before_tuning_sql_except_yn").val("N");
		}
	});
	
	// 애플리케이션 필터 여부
	$('#detail_form chkApplFilterYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#appl_filter_yn").val("Y");
			else $("#appl_filter_yn").val("N");
		}
	});
	
	// 사용 여부
	$('#detail_form #chkUseYn').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#use_yn").val("Y");
			else $("#use_yn").val("N");
		}
	});
	
	// 삭제 여부
	$('#detail_form #chkDelYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) {
				$("#detail_form #del_yn").val("Y");
				$('#detail_form #chkUseYn').switchbutton('uncheck');
				$('#detail_form #chkUseYn').switchbutton('disable');
				
			}else {
				$("#detail_form #del_yn").val("N");
				$('#detail_form #chkUseYn').switchbutton('enable');
			}
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
	
	
	initializeProjectNTuningPrgrsStep();
	
	initializeBundleTab();
	
	Btn_ResetField();
	
});

function initializeProjectNTuningPrgrsStep() {
	$('#project_nm').combobox({
		url:"/Common/getProject?isNotApplicable=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect:function(rec) {
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			// 튜닝진행단계
			$('#tuning_prgrs_step_nm').combobox({
				url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?project_id="+rec.project_id+"&isNotApplicable=Y",
				method:"get",
				valueField:'tuning_prgrs_step_seq',
				textField:'tuning_prgrs_step_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
			});
		},
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		}
	});
}

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == '') {
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if( ($('#strStartDt').textbox('getValue') != '' && $('#strEndDt').textbox('getValue') == '') ||
			($('#strStartDt').textbox('getValue') == '' && $('#strEndDt').textbox('getValue') != '') ) {
		parent.$.messager.alert('','선정종료일자를 확인해 주세요.');
		return false;
	}
	
	if(!compareAnBDate($('#strStartDt').datebox('getValue'), $('#strEndDt').datebox('getValue'))) {
		var msg = "선정종료일자를 확인해 주세요.<br>[" + $('#strStartDt').textbox('getValue') + "] [" + $('#strEndDt').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	$("#submit_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#selectDbidCombo").combobox("readonly", false);
	$("#submit_form #perfr_id").val($('#selectTuner').combobox('getValue'));
	$('#submit_form #program_type_cd').val($('#selectProgramType').combobox('getValue'));
	$('#submit_form #project_id').val($('#selectProject').combobox('getValue'));
	
	$('#tableList').datagrid('loadData',[]);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("자동 선정"," ");
	
	ajaxCall("/AutoSelectionAction",
			$("#submit_form"),
			"POST",
			callback_AutoSelectionAction);
}

//callback 함수
var callback_AutoSelectionAction = function(result) {
	Btn_ResetField();
	Btn_BundleReset();
	
	json_string_callback_common(result,'#tableList',true);
};
//저장버튼
function Btn_SaveAutoChoice(){
	if($("#selectDbidCombo").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#perfr_auto_assign_yn").val() == "N" && $("#perfr_id_detail").combobox('getValue') == ""){
		parent.$.messager.alert('','튜닝담당자를 선택해 주세요.');
		return false;
	}
	
	if($("#program_type_cd_nm").combobox('getValue') == ""){
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
	
	if($("#topn_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','TOP N을 입력해 주세요.');
		return false;
	}
	
	if($("#order_div_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','Ordered를 선택해 주세요.');
		return false;
	}
	
	if($('#project_nm').combobox('getValue') != '' && $('#tuning_prgrs_step_nm').combobox('getValue') == '') {
		parent.$.messager.alert('','튜닝진행단계를 선택해 주세요.');
		return false;
	}
	
	if(!compareAnBDate($('#detail_form #choice_start_day').datebox('getValue'), $('#detail_form #choice_end_day').datebox('getValue'))) {
		var msg = "선정종료일자를 확인해 주세요.<br>[" + $('#detail_form #choice_start_day').textbox('getValue') + "] [" + $('#detail_form #choice_end_day').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	$("#parsing_schema_name").val($('#selectParsingSchemaName').combobox("getValue"));
	
	$("#detail_form #dbid").val($("#selectDbidCombo").combobox('getValue'));
	$("#detail_form #db_name").val($("#selectDbidCombo").combobox('getText'));
	$('#detail_form #perfr_id').val($('#perfr_id_detail').combobox('getValue'));
	$("#detail_form #project_id").val($('#project_nm').combobox('getValue'));
	$('#detail_form #tuning_prgrs_step_seq').val($('#tuning_prgrs_step_nm').combobox('getValue'));
	$('#detail_form #program_type_cd').val($('#program_type_cd_nm').combobox('getValue'));
	
	ajaxCall("/AutoSelection/Save",
			$("#detail_form"),
			"POST",
			callback_SaveAutoSelectionAction);
}

//callback 함수
var callback_SaveAutoSelectionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','자동 선정 조건 정보가 저장이 되었습니다.','info',function(){
			setTimeout(function() {
				if($('#selectCombo').combobox('getValue') == '') {
					Btn_ResetField();
				}else{
					Btn_OnClick();
				}
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
//	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
	parent.selectTab4(menuParam);
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
//	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
	parent.selectTab2(menuParam);
}

function setDetailView(selRow){
	var chkPerfr = false;
	var chkChoice = false;
	var chkTuning = false;
	var chkApplFilter = false;
	var chkUseYn = false;
	var chkDelYn = false;
//	var startDay = ""; 
//	var endDay = "";
	$("#selectDbidCombo").combobox("setValue", selRow.dbid);
	
	$("#selectDbidCombo").combobox("readonly", true);

	$("#auto_choice_cond_no").val(selRow.auto_choice_cond_no);
	$("#perfr_auto_assign_yn").val(selRow.perfr_auto_assign_yn);
	
	$('#detail_form #select_auto_choice_cond_no').val(selRow.auto_choice_cond_no);
	$('#detail_form #dbid').val(selRow.dbid);
	
	// 튜닝 담당자 조회
	$('#perfr_id_detail').combobox({
		url:"/Common/getTuner?dbid="+selRow.dbid,
		method:"get",
		valueField:'tuner_id',
		textField:'tuner_nm',
		onLoadSuccess:function() {
			$('#perfr_id_detail').combobox('textbox').attr( 'placeholder' , '선택' );
		}
	});
	
	if(selRow.perfr_auto_assign_yn == "Y"){
		chkPerfr = true;
		$("#perfr_id_detail").combobox("readonly", true);
	}else{
		$("#perfr_id_detail").combobox("readonly", false);
	}
	
	$("#program_type_cd_nm").combobox("setValue", selRow.program_type_cd);
	
	$('#chkPerfrId').switchbutton({checked: chkPerfr});
	$("#perfr_id_detail").combobox("setValue", selRow.perfr_id);
	$("#gather_cycle_div_cd").combobox("setValue", selRow.gather_cycle_div_cd);
	$("#gather_range_div_cd").combobox("setValue", selRow.gather_range_div_cd);
	
//	if(selRow.choice_start_day != "" && selRow.choice_start_day != null){
//		startDay = selRow.choice_start_day.substr(0,4) + "-" + selRow.choice_start_day.substr(4,2) + "-" + selRow.choice_start_day.substr(6,2)
//	}
//	
//	if(selRow.choice_end_day != "" && selRow.choice_end_day != null){
//		endDay = selRow.choice_end_day.substr(0,4) + "-" + selRow.choice_end_day.substr(4,2) + "-" + selRow.choice_end_day.substr(6,2)
//	}
//	
//	$("#choice_start_day").textbox("setValue", startDay);
//	$("#choice_end_day").textbox("setValue", endDay);
	
	$("#choice_start_day").textbox("setValue", selRow.choice_start_day);
	$("#choice_end_day").textbox("setValue", selRow.choice_end_day);
	
	$("#detail_form #before_choice_sql_except_yn").val(selRow.before_choice_sql_except_yn);
	if(selRow.before_choice_sql_except_yn == "Y") chkChoice = true;
	
	$('#chkChoiceExcept').switchbutton({checked: chkChoice});
	
	$("#detail_form #before_tuning_sql_except_yn").val(selRow.before_tuning_sql_except_yn);
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
	
	$("#detail_form #appl_filter_yn").val(selRow.appl_filter_yn);
	if(selRow.appl_filter_yn == "Y") chkApplFilter = true;
	
	$('#chkApplFilterYn').switchbutton({checked: chkApplFilter});
	
	$("#detail_form #use_yn").val(selRow.use_yn);
	if(selRow.use_yn == "Y") chkUseYn = true;
	
	$('#chkUseYn').switchbutton({checked: chkUseYn});
	
	$("#detail_form #del_yn").val(selRow.del_yn);
	if(selRow.del_yn == "Y") chkDelYn = true;
	
	$('#chkDelYn').switchbutton({checked: chkDelYn});
	
	$("#historyBtn").linkbutton({disabled:false});
	$("#detail_form #chkUseYn").switchbutton({disabled:chkDelYn});
	
	$('#project_nm').combobox('setValue', selRow.project_id);
	
	$('#tuning_prgrs_step_nm').combobox('setValue', selRow.tuning_prgrs_step_seq);
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
	$("#perfr_id_detail").combobox("readonly", false);
	$('#chkPerfrId').switchbutton({checked: false});
	$("#perfr_id_detail").combobox("setValue", "");
	$("#program_type_cd_nm").combobox("setValue", "");
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
	
	$('#selectDbidCombo').combobox('textbox').attr( 'placeholder' , '선택' );
	$('#program_type_cd_nm').combobox('textbox').attr( 'placeholder' , '선택' );
	
	$('#chkChoiceExcept').switchbutton({checked: true});
	$('#chkTuningExcept').switchbutton({checked: true});
	$('#chkApplFilterYn').switchbutton({checked: false});
	$('#chkUseYn').switchbutton('enable');
	$('#chkUseYn').switchbutton({checked: true});
	$('#chkDelYn').switchbutton({checked: false});
	
	$("#historyBtn").linkbutton({disabled:true});
	
	initializeProjectNTuningPrgrsStep();
}

function Excel_Download(){
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/AutoSelection/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function initializeBundleTab() {
	// 1 Line
	$('#bundle_form #cbElapTime').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #elap_time').textbox({disabled:false});
			else {
				$('#bundle_form #elap_time').textbox('setValue', '');
				$('#bundle_form #elap_time').textbox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbOrderDivCd').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #order_div_cd').combobox({disabled:false});
			else {
				$('#bundle_form #order_div_cd').combobox('setValue', '');
				$('#bundle_form #order_div_cd').combobox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbChoiceExcept').checkbox({
		onChange: function(checked){
			if(checked) {
				$('#bundle_form #chkChoiceExcept').switchbutton({disabled:false});
				$('#bundle_form #before_choice_sql_except_yn').val('Y');
			}
			else {
				$('#bundle_form #chkChoiceExcept').switchbutton('uncheck');
				$('#bundle_form #before_choice_sql_except_yn').val('');
				$('#bundle_form #chkChoiceExcept').switchbutton({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkChoiceExcept').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
	});
	
	// 2 Line
	$('#bundle_form #cbBufferCnt').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #buffer_cnt').textbox({disabled:false});
			else {
				$('#bundle_form #buffer_cnt').textbox('setValue', '');
				$('#bundle_form #buffer_cnt').textbox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbPerfrIdDetail').checkbox({
		onChange: function(checked){
			if(checked) {
				$('#bundle_form #cb_perfr_id_detail').val('Y');
				$('#bundle_form #chkPerfrId').switchbutton({disabled:false});
				$('#bundle_form #perfr_id_detail').combobox({disabled:false});
			} else {
				$('#bundle_form #cb_perfr_id_detail').val('N');
				$('#bundle_form #chkPerfrId').switchbutton('uncheck');
				$('#bundle_form #chkPerfrId').switchbutton({disabled:true});
				$('#bundle_form #perfr_id_detail').combobox('setValue', '');
				$('#bundle_form #perfr_id').val('');
				$('#bundle_form #perfr_id_detail').combobox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkPerfrId').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#bundle_form #perfr_auto_assign_yn").val("Y");
				$("#bundle_form #perfr_id_detail").combobox("readonly", true);
				$("#bundle_form #perfr_id_detail").combobox("setValue", "");
			}else{
				$("#bundle_form #perfr_auto_assign_yn").val("N");
				$("#bundle_form #perfr_id_detail").combobox("readonly", false);
				$("#bundle_form #perfr_id_detail").combobox("setValue", "");
			}
		}
	});
	
	$('#cbApplFilterYn').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #chkApplFilterYn').switchbutton({disabled:false});
			else {
				$('#bundle_form #chkApplFilterYn').switchbutton('uncheck');
				$('#bundle_form #appl_filter_yn').val('');
				$('#bundle_form #chkApplFilterYn').switchbutton({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkApplFilterYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
	});
	
	// 3 Line
	$('#bundle_form #cbExecCnt').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #exec_cnt').textbox({disabled:false});
			else {
				$('#bundle_form #exec_cnt').textbox('setValue', '');
				$('#bundle_form #exec_cnt').textbox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbProgramTypeCdNm').checkbox({
		onChange: function(checked){
			if(checked) {
				$('#bundle_form #program_type_cd_nm').combobox({disabled:false});
				$('#bundle_form #program_type_cd_nm').combobox('setValue', '');
			} else {
				$('#bundle_form #program_type_cd').val('');
				$('#bundle_form #program_type_cd_nm').combobox({disabled:true});
				$('#bundle_form #program_type_cd_nm').combobox('setValue', '');
			}
		}
	});
	
	$('#cbUseYn').checkbox({
		onChange: function(checked){
			if(checked) {
				if( $('#bundle_form #chkDelYn').switchbutton('options').checked ){
					$('#bundle_form #chkUseYn').switchbutton({disabled:true});
					
				}else {
					$('#bundle_form #chkUseYn').switchbutton({disabled:false});
				}
				
			}else {
				$('#bundle_form #chkUseYn').switchbutton('uncheck');
				$('#bundle_form #use_yn').val('');
				$('#bundle_form #chkUseYn').switchbutton({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkUseYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
	});
	
	// 4 Line
	$('#bundle_form #cbChoiceStartDay').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #choice_start_day').textbox({disabled:false});
			else {
				$('#bundle_form #choice_start_day').textbox('setValue', '');
				$('#bundle_form #choice_start_day').textbox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbGatherCycleDivCd').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #gather_cycle_div_cd').combobox({disabled:false});
			else {
				$('#bundle_form #gather_cycle_div_cd').combobox('setValue', '');
				$('#bundle_form #gather_cycle_div_cd').combobox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbDelYn').checkbox({
		onChange: function(checked){
			if(checked) {
				$('#bundle_form #chkDelYn').switchbutton({disabled:false});
				
			}else {
				$('#bundle_form #chkDelYn').switchbutton('uncheck');
				$('#bundle_form #del_yn').val('');
				$('#bundle_form #chkDelYn').switchbutton({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkDelYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if( $('#bundle_form #cbUseYn').checkbox('options').checked == true ){
				if(checked) {
					$('#bundle_form #chkUseYn').switchbutton('uncheck');
					$('#bundle_form #chkUseYn').switchbutton('disable');
					
				}else {
					$('#bundle_form #chkUseYn').switchbutton('enable');
				}
			}
		}
	});
	
	// 5 Line
	$('#bundle_form #cbChoiceEndDay').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #choice_end_day').textbox({disabled:false});
			else $('#bundle_form #choice_end_day').textbox({disabled:true});
		}
	});
	
	$('#bundle_form #cbGatherRangeDivCd').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #gather_range_div_cd').combobox({disabled:false});
			else {
				$('#bundle_form #choice_end_day').textbox('setValue', '');
				$('#bundle_form #gather_range_div_cd').combobox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbProjectNm').checkbox({
		onChange: function(checked){
			if(checked) {
				$('#bundle_form #project_nm').combobox({disabled:false});
				$('#bundle_form #cbTuningPrgrsStepNm').checkbox('check');
			} else {
				$('#bundle_form #project_nm').combobox('setValue', '');
				$('#bundle_form #project_id').val('');
				$('#bundle_form #project_nm').combobox({disabled:true});
				$('#bundle_form #cbTuningPrgrsStepNm').checkbox('uncheck');
			}
		}
	});
	
	// 6 Line
	$('#bundle_form #cbTopnCnt').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #topn_cnt').textbox({disabled:false});
			else {
				$('#bundle_form #topn_cnt').textbox('setValue', '');
				$('#bundle_form #topn_cnt').textbox({disabled:true});
			}
		}
	});
	
	$('#bundle_form #cbTuningExcept').checkbox({
		onChange: function(checked){
			if(checked) $('#bundle_form #chkTuningExcept').switchbutton({disabled:false});
			else {
				$('#bundle_form #chkTuningExcept').switchbutton('uncheck');
				$('#bundle_form #before_tuning_sql_except_yn').val('');
				$('#bundle_form #chkTuningExcept').switchbutton({disabled:true});
			}
		}
	});
	
	$('#bundle_form #chkTuningExcept').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
	});
	
	$('#bundle_form #cbTuningPrgrsStepNm').checkbox({
		onChange: function(checked){
			if(checked) {
				if($('#bundle_form #cbProjectNm').checkbox('options').checked) {
					$('#bundle_form #tuning_prgrs_step_nm').combobox({disabled:false});
				} else {
					$('#bundle_form #cbTuningPrgrsStepNm').checkbox('uncheck');
				}
			} else {
				if($('#bundle_form #cbProjectNm').checkbox('options').checked) {
					$('#bundle_form #cbTuningPrgrsStepNm').checkbox('check');
				} else {
					$('#bundle_form #tuning_prgrs_step_nm').combobox('setValue', '');
					$('#bundle_form #tuning_prgrs_step_seq').val('');
					$('#bundle_form #tuning_prgrs_step_nm').combobox({disabled:true});
				}
			}
		}
	});
	
	// 정렬구분 조회
	$('#bundle_form #order_div_cd').combobox({
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
	
	// 수집주기 조회
	$('#bundle_form #gather_cycle_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1001",
		method:"get",
		valueField:'cd',
		textField:'cd_nm'
	});
	
	// 수집범위 조회
	$('#bundle_form #gather_range_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1002",
		method:"get",
		valueField:'cd',
		textField:'cd_nm'
	});
	
	$('#bundle_form #project_nm').combobox({
		url:"/Common/getProject?isNotApplicable=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect:function(rec) {
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			// 튜닝 담당자 조회
			$('#bundle_form #tuning_prgrs_step_nm').combobox({
				url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?project_id="+rec.project_id+"&isNotApplicable=Y",
				method:"get",
				valueField:'tuning_prgrs_step_seq',
				textField:'tuning_prgrs_step_nm',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
			});
		},
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		}
	});
}

function confirmProjectChange() {
	var msg = "프로젝트, 튜닝진행단계를 변경하겠습니까?"
	parent.$.messager.confirm("확인",msg,function(r){
		if (r){
			return true;
		} else {
			return false;
		}
	});
}

function Btn_BundleSave() {
	if($('#tableList').datagrid('getChecked').length == 0) {
		warningMessager('선택된 선정조건번호가 없습니다.');
		return false;
	}
	
	var isChecked = false;
	
	if($('#bundle_form #cbElapTime').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #elap_time').textbox('getValue') == '') {
			warningMessager('Elapsed Time (sec) 항목을 입력하세요.');
			return false;
		}
	} else {
		$('#bundle_form #elap_time').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbOrderDivCd').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #order_div_cd').combobox('getValue') == '') {
			warningMessager('Ordered 항목을 선택하세요.');
			return false;
		}
	} else {
		$('#bundle_form #order_div_cd').combobox('setValue', '');
	}
	
	if($('#bundle_form #cbBufferCnt').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #buffer_cnt').textbox('getValue') == '') {
			warningMessager('Buffer Get 항목을 입력하세요.');
			return false;
		}
	} else {
		$('#bundle_form #buffer_cnt').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbPerfrIdDetail').checkbox('options').checked) {
		isChecked = true;
		if($("#bundle_form #perfr_auto_assign_yn").val() == "N" && $("#bundle_form #perfr_id_detail").combobox('getValue') == ""){
			warningMessager('튜닝담당자 항목을 선택하세요.');
			return false;
		} else {
			$('#bundle_form #perfr_id').val($('#bundle_form #perfr_id_detail').combobox('getValue'));
		}
	} else {
		$('#bundle_form #perfr_id_detail').combobox('setValue', '');
		$('#bundle_form #perfr_id').val('');
	}
	
	if($('#bundle_form #cbExecCnt').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #exec_cnt').textbox('getValue') == '') {
			warningMessager('Executions 항목을 입력하세요.');
			return false;
		}
	} else {
		$('#bundle_form #exec_cnt').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbProgramTypeCdNm').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #program_type_cd_nm').combobox('getValue') == '') {
			warningMessager('프로그램 유형 항목을 선택하세요.');
			return false;
		} else{
			$('#bundle_form #program_type_cd').val($('#bundle_form #program_type_cd_nm').combobox('getValue'));
		}
	} else {
		$('#bundle_form #program_type_cd_nm').combobox('setValue', '');
		$('#bundle_form #program_type_cd').val('');
	}
	
	if($('#bundle_form #cbChoiceStartDay').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #choice_start_day').textbox('getValue') == '') {
			warningMessager('선정시작일 항목을 선택하세요.');
			return false;
		}
	} else {
		$('#bundle_form #choice_start_day').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbGatherCycleDivCd').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #gather_cycle_div_cd').combobox('getValue') == '') {
			warningMessager('수집주기 항목을 선택하세요.');
			return false;
		}
	} else {
		$('#bundle_form #gather_cycle_div_cd').combobox('setValue', '');
	}
	
	if($('#bundle_form #cbChoiceEndDay').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #choice_end_day').textbox('getValue') == '') {
			warningMessager('선정종료일 항목을 선택하세요.');
			return false;
		}
	} else {
		$('#bundle_form #choice_end_day').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbGatherRangeDivCd').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #gather_range_div_cd').combobox('getValue') == '') {
			warningMessager('수집범위 항목을 선택하세요.');
			return false;
		}
	} else {
		$('#bundle_form #gather_range_div_cd').combobox('setValue', '');
	}
	
	if($('#bundle_form #cbProjectNm').checkbox('options').checked) {
		isChecked = true;
		$('#bundle_form #project_id').val($('#bundle_form #project_nm').combobox('getValue'));
	} else {
		$('#bundle_form #project_nm').combobox('setValue', '');
		$('#bundle_form #project_id').val('');
	}
	
	if($('#bundle_form #cbTopnCnt').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #topn_cnt').textbox('getValue') == '') {
			warningMessager('TOP N 항목을 입력하세요.');
			return false;
		}
	} else {
		$('#bundle_form #topn_cnt').textbox('setValue', '');
	}
	
	if($('#bundle_form #cbTuningPrgrsStepNm').checkbox('options').checked) {
		isChecked = true;
		if($('#bundle_form #project_nm').combobox('getValue') != '' && $('#bundle_form #tuning_prgrs_step_nm').combobox('getValue') == '') {
			warningMessager('튜닝진행단계 항목을 선택하세요.');
			return false;
		}
		
		$('#bundle_form #tuning_prgrs_step_seq').val($('#bundle_form #tuning_prgrs_step_nm').combobox('getValue'));
	} else {
		$('#bundle_form #tuning_prgrs_step_nm').combobox('setValue', '');
		$('#bundle_form #tuning_prgrs_step_seq').val('');
	}
	

	if(!compareAnBDate($('#bundle_form #choice_start_day').datebox('getValue'), $('#bundle_form #choice_end_day').datebox('getValue'))) {
		var msg = "선정종료일자를 확인해 주세요.<br>[" + $('#bundle_form #choice_start_day').textbox('getValue') + "] [" + $('#bundle_form #choice_end_day').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}

	
	
	var msg = "일괄 수정하시겠습니까?"
	parent.$.messager.confirm("확인",msg,function(r){
		if(r){
			nextStep(isChecked);
		} else {
			return;
		}
	});
}

function nextStep(isChecked) {
	// checkbox
	if($('#bundle_form #cbChoiceExcept').checkbox('options').checked == true) {
		isChecked = true;
		$('#bundle_form #before_choice_sql_except_yn').val($('#bundle_form #chkChoiceExcept').switchbutton('options').checked ? 'Y' : 'N');
	} else {
		$('#bundle_form #before_choice_sql_except_yn').val('');
	}
	
	if($('#bundle_form #cbApplFilterYn').checkbox('options').checked == true) {
		isChecked = true;
		$('#bundle_form #appl_filter_yn').val($('#bundle_form #chkApplFilterYn').switchbutton('options').checked ? 'Y' : 'N');
	} else {
		$('#bundle_form #appl_filter_yn').val('');
	}
	
	let useYn = $('#bundle_form #chkUseYn').switchbutton('options').checked;
	if($('#bundle_form #cbUseYn').checkbox('options').checked == true) {
		isChecked = true;
		$('#bundle_form #use_yn').val( useYn ? 'Y' : 'N' );
		
	} else {
		$('#bundle_form #use_yn').val($('#bundle_form #chkDelYn').switchbutton('options').checked ? 'N' : '');
	}
	
	if($('#bundle_form #cbDelYn').checkbox('options').checked == true) {
		isChecked = true;
		$('#bundle_form #del_yn').val($('#bundle_form #chkDelYn').switchbutton('options').checked ? 'Y' : 'N');
		
	} else {
		$('#bundle_form #del_yn').val( useYn ? 'N' : '' );
		//$('#bundle_form #del_yn').val('');
	}
	
	if($('#bundle_form #cbTuningExcept').checkbox('options').checked == true) {
		isChecked = true;
		$('#bundle_form #before_tuning_sql_except_yn').val($('#bundle_form #chkTuningExcept').switchbutton('options').checked ? 'Y' : 'N');
	} else {
		$('#bundle_form #before_tuning_sql_except_yn').val('');
	}
	
	$("#bundle_form #dbid").val($("#selectCombo").combobox('getValue'));
	$("#bundle_form #db_name").val($("#selectCombo").combobox('getText'));
	
	var rows = $('#tableList').datagrid('getChecked');
	var autoChoiceCondNoArray = "";
	
	$("#assignAll_form #tuningNoArry").val("");
	
	if(rows != null && rows != ""){
		if(!isChecked) {
			warningMessager('일괄 수정이 필요한 항목을 체크해 주세요.');
			return false;
		}
		
		if($('#bundle_form #cbChoiceEndDay').checkbox('options').checked 
				&& $('#bundle_form #cbChoiceStartDay').checkbox('options').checked){
		}else{
			for(var i = 0 ; i < rows.length; i ++){
				if(new Date($("#bundle_form #choice_end_day").combobox('getValue')) < new Date($('#tableList').datagrid('getChecked')[i].choice_start_day)){
					warningMessager('선정 종료일을 선정 시작일 이전으로 설정할 수 없습니다.');
					return false;
				}
				if(new Date($("#bundle_form #choice_start_day").combobox('getValue')) > new Date($('#tableList').datagrid('getChecked')[i].choice_end_day)){
					warningMessager('선정 시작일을 선정 종료일 이후로 설정할 수 없습니다.');
					return false;
				}
			}
		}
		
		for(var i = 0; i < rows.length; i++){
			autoChoiceCondNoArray += rows[i].auto_choice_cond_no + "|";
		}
		
		$("#bundle_form #autoChoiceCondNoArray").val(strRight(autoChoiceCondNoArray,1));
		
		console.log($('#bundle_form'));
		console.log('del_yn', $('#bundle_form #del_yn').val());
		console.log('use_yn', $('#bundle_form #use_yn').val());
		
		ajaxCall("/AutoSelection/BundleSave",
				$("#bundle_form"),
				"POST",
				callback_BundleSaveAutoSelectionAction);
	}
}

//callback 함수
var callback_BundleSaveAutoSelectionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','자동선정조건 정보 일괄 수정이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_BundleReset();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_BundleReset(){
	$('#tableList').datagrid("clearChecked");
	
	$('#bundle_form #db_name').val('');
	$('#bundle_form #dbid').val('');
	$('#bundle_form #before_choice_sql_except_yn').val('');
	$('#bundle_form #before_tuning_sql_except_yn').val('');
	$('#bundle_form #appl_filter_yn').val('');
	$('#bundle_form #use_yn').val('');
	$('#bundle_form #del_yn').val('');
	$('#bundle_form #perfr_id').val('');
	$('#bundle_form #project_id').val('');
	$('#bundle_form #tuning_prgrs_step_seq').val('');
	$('#bundle_form #program_type_cd').val('');
	$('#bundle_form #autoChoiceCondNoArray').val('');
	
	$('#bundle_form #cbElapTime').checkbox('uncheck');
	$('#bundle_form #elap_time').textbox('setValue', '');
	$('#bundle_form #cbOrderDivCd').checkbox('uncheck');
	$('#bundle_form #order_div_cd').combobox('setValue', '');
	$('#bundle_form #cbChoiceExcept').checkbox('uncheck');
	$('#bundle_form #chkChoiceExcept').switchbutton('uncheck');
	
	$('#bundle_form #cbBufferCnt').checkbox('uncheck');
	$('#bundle_form #buffer_cnt').textbox('setValue', '');
	$('#bundle_form #cbPerfrIdDetail').checkbox('uncheck');
	$('#bundle_form #perfr_id_detail').combobox('setValue', '');
	$('#bundle_form #cbApplFilterYn').checkbox('uncheck');
	$('#bundle_form #chkApplFilterYn').switchbutton('uncheck');
	
	$('#bundle_form #cbExecCnt').checkbox('uncheck');
	$('#bundle_form #exec_cnt').textbox('setValue', '');
	$('#bundle_form #cbProgramTypeCdNm').checkbox('uncheck');
	$('#bundle_form #program_type_cd_nm').combobox('setValue', '');
	$('#bundle_form #cbUseYn').checkbox('uncheck');
	$('#bundle_form #chkUseYn').switchbutton('uncheck');
	
	$('#bundle_form #cbChoiceStartDay').checkbox('uncheck');
	$('#bundle_form #choice_start_day').textbox('setValue', '');
	$('#bundle_form #cbGatherCycleDivCd').checkbox('uncheck');
	$('#bundle_form #gather_cycle_div_cd').combobox('setValue', '');
	$('#bundle_form #cbDelYn').checkbox('uncheck');
	$('#bundle_form #chkDelYn').switchbutton('uncheck');
	
	$('#bundle_form #cbChoiceEndDay').checkbox('uncheck');
	$('#bundle_form #choice_end_day').textbox('setValue', '');
	$('#bundle_form #cbGatherRangeDivCd').checkbox('uncheck');
	$('#bundle_form #gather_range_div_cd').combobox('setValue', '');
	$('#bundle_form #cbProjectNm').checkbox('uncheck');
	$('#bundle_form #project_nm').combobox('setValue', '');
	
	$('#bundle_form #cbTopnCnt').checkbox('uncheck');
	$('#bundle_form #topn_cnt').textbox('setValue', '');
	$('#bundle_form #cbTuningExcept').checkbox('uncheck');
	$('#bundle_form #chkTuningExcept').switchbutton('uncheck');
	$('#bundle_form #cbTuningPrgrsStepNm').checkbox('uncheck');
	$('#bundle_form #tuning_prgrs_step_nm').combobox('setValue', '');
}
