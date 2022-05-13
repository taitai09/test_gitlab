$(document).ready(function() {
	// Work Job 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getWrkJob",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
//				$(this).combobox('select', items[0][opts.valueField]);
				$(this).combobox("setValue",$("#wrkjob_cd").val());
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		onClickRow : function(index,row) {
			$("#deploy_perf_check_no").val(row.deploy_perf_check_no);
			goDBIODetailCheck();
		},			
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
		    {field:'deploy_perf_check_no',title:'점검번호',halign:"center",align:'center',sortable:"true"},
		    {field:'wrkjob_cd',hidden:'true'},
		    {field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true"},
		    {field:'deploy_day',title:'배포일자',halign:"center",align:"center",formatter:getDateFormat,sortable:"true"},
			{field:'reg_dt',title:'등록일시',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_dt',title:'성능점검일시',halign:"center",align:'center',sortable:"true"},
			{field:'dbio_cnt',title:'DBIO수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'perf_fitness_cnt',title:'적합 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'perf_not_fitness_cnt',title:'부적합 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'perf_not_check_cnt',title:'미점검 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'prohibit_hint_use_cnt',title:'금지힌트 사용건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reg_nm',title:'등록자',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
//        onCheck : function(node, checked) {
//            var selectedrow = $("#tableList").datagrid("getSelected");
//            var rowIndex = $("#tableList").datagrid("getRowIndex", selectedrow);
//        	
//        	row = $('#tableList').datagrid('getChecked');
//        	var rowLength = row.length;
//        		if(row.length > 1){
//        			parent.$.messager.alert('','다중 선택은 불가합니다.');
//        			//$("input:checkbox[name='chk']").eq(node).attr("checked",false);
//        			return false;
//        		}
//        }
	});
	
	var chkValue = false;
	if($("#perf_check_yn").val() == "Y") chkValue = true;	
	
	$('#chkPerfCheck').switchbutton({
		checked: chkValue,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#perf_check_yn").val("Y"); 
			else $("#perf_check_yn").val("N");
		}
	})
	
	$("#selectCombo").combobox("setValue",$("#wrkjob_cd").val());
	
	//ajaxCall("/ApplicationCheckAction", $("#submit_form"), "POST", callback_ApplicationCheckActionAddTable);	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallDBIOCheck();	
	}
	
});

function ajaxCallDBIOCheck(){
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL식별자(DBIO) 성능점검"," "); 
	
	ajaxCall("/DBIOCheckAction",
			$("#submit_form"),
			"POST",
			callback_DBIOCheckActionAddTable);
}

//callback 함수
var callback_DBIOCheckActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}

function Btn_OnClick(){
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 시작일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 종료일자를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));
	
//	$("#submit_form").attr("action","/DBIOCheck");
//	$("#submit_form").submit();
	ajaxCallDBIOCheck();
}

function requestPerformanceCheck(){
	var rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		$("#deploy_perf_check_no").val(rows[0].deploy_perf_check_no);
		$("#wrkjob_cd").val(rows[0].wrkjob_cd);
		
		ajaxCall("/DBIORequestCheck",
				$("#submit_form"),
				"POST",
				null);
	
		showAlert();
	}else{
		parent.$.messager.alert('','성능점검을 요청할 ROW를 선택해 주세요.');
	}
}

function Btn_ExcelUploadPopup(strGubun){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$("#loadedExcel_form #wrkjob_cd").combobox("setValue",$('#selectCombo').combobox('getValue'));
	$("#loadedExcel_form #deploy_perf_check_type_cd").combobox("setValue",strGubun);

	$('#excelUploadPop').window("open");
}

function showAlert(){
	parent.$.messager.alert('','SQL식별자(DBIO) 성능점검요청 수행 시간이<br/>오래걸려 서버에서 실행중입니다.','info',function(){
		setTimeout(function() {
			Btn_OnClick();
		},1000);
	});
}

function goDBIODetailCheck(){
	$("#submit_form").attr("action","/DBIODetailCheck");
	$("#submit_form").submit();	
}