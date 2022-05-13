$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Work Job 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getWrkJob",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			var menuParam = "tr_cd="+row.tr_cd+"&wrkjob_cd="+$('#selectCombo').combobox('getValue')+"&searchKey=01&call_from_parent=Y";
			createNewTab("133", "애플리케이션 분석", "/APPPerformance", menuParam);
		},		
		columns:[[
//			{field:'tr_cd',title:'애플리케이션코드',width:"26%",halign:"center",align:"left",sortable:"true"},
//			{field:'tr_cd_nm',title:'애플리케이션명',width:"26%",halign:"center",align:'left',sortable:"true"},
//			{field:'user_nm',title:'담당자',width:"8%",halign:"center",align:'center',sortable:"true"},
//			{field:'exec_cnt',title:'지연건수',width:"8%",halign:"center",align:'right',sortable:"true"},
//			{field:'prev_elap_time',title:'이전응답시간',width:"8%",halign:"center",align:'right',sortable:"true"},
//			{field:'cur_elap_time',title:'현재응답시간',width:"8%",halign:"center",align:'right',sortable:"true"},
//			{field:'delay_elap_time',title:'지연응답시간',width:"8%",halign:"center",align:'right',sortable:"true"},
//			{field:'elap_time_increase_ratio',title:'응답시간증가율',width:"8%",halign:"center",align:'right',sortable:"true",formatter:dataRatio}
			{field:'tr_cd',title:'애플리케이션코드',halign:"center",align:"left",sortable:"true"},
			{field:'tr_cd_nm',title:'애플리케이션명',halign:"center",align:'left',sortable:"true"},
			{field:'user_nm',title:'담당자',halign:"center",align:'center',sortable:"true"},
			{field:'exec_cnt',title:'지연건수',halign:"center",align:'right',sortable:"true"},
			{field:'prev_elap_time',title:'이전응답시간',halign:"center",align:'right',sortable:"true"},
			{field:'cur_elap_time',title:'현재응답시간',halign:"center",align:'right',sortable:"true"},
			{field:'delay_elap_time',title:'지연응답시간',halign:"center",align:'right',sortable:"true"},
			{field:'elap_time_increase_ratio',title:'응답시간증가율',halign:"center",align:'right',sortable:"true",formatter:dataRatio}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#selectCombo').combobox("setValue", $("#wrkjob_cd").val());
	
	$('#applTabs').tabs({
		plain : true
	});		
	
	Btn_OnClick();
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 시작일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 종료일자를 선택해 주세요.');
		return false;
	}

	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("애플리케이션 진단 - 응답시간지연"," "); 
	
	ajaxCall("/ApplicationDiagnostics/ElapsedTimeDelay",
		$("#submit_form"),
		"POST",
		callback_ElapsedTimeDelayAddTable);		
}

//callback 함수
var callback_ElapsedTimeDelayAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}


function Excel_DownClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','업무명을 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 시작일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일 종료일자를 선택해 주세요.');
		return false;
	}

	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/ApplicationDiagnostics/ElapsedTimeDelay/ExcelDown");
	$("#submit_form").submit();
}
