$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	fnCreateDbidCombobox();	

	$("#strStartDt").datebox({
		onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			
			// 점검회차 설정
			setCheckSeq(selDate);
		}
	});
	
	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());

	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'inst_id',title:'INST_ID',width:"12%",halign:"center",align:"right",sortable:"true"},
			{field:'inst_nm',title:'INST_NM',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'host_nm',title:'HOST_NM',width:"12%",halign:"center",align:"left",sortable:"true"},
			{field:'version',title:'VERSION',width:"12%",halign:"center",align:"center",sortable:"true"},
			{field:'startup_time',title:'STARTUP_TIME',width:"13%",halign:"center",align:"center",sortable:"true"},
			{field:'up_time',title:'UP_TIME',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'status',title:'STATUS',width:"13%",halign:"center",align:"left",sortable:"true",styler:cellStyler},
			{field:'archiver',title:'ARCHIVER',width:"13%",halign:"center",align:"left",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#selectDbid').combobox("setValue",$("#dbid").val());
	$('#selectCheckSeq').combobox("setValue",$("#check_seq").val());	
	Btn_OnClick();
});



function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - INSTANCE STATUS"," "); 

	ajaxCall("/PreventiveCheck/InstanceStatus",
			$("#submit_form"),
			"POST",
			callback_InstanceStatusAction);
}

//callback 함수
var callback_InstanceStatusAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function cellStyler(value,row,index){
	if(nvl(value,"") == "OPEN"){
		return 'background-color:#62f22f;color:#ffffff;font-weight:700;';
	}else{
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
	}
}