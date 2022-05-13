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
		nowrap: false,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
//			{field:'owner',title:'OWNER',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'object_name',title:'OBJECT_NAME',width:"15%",halign:"center",align:"center",sortable:"true"},
//			{field:'subobject_name',title:'SUBOBJECT_NAME',width:"15%",halign:"center",align:"center",sortable:"true"},
//			{field:'object_type',title:'OBJECT_TYPE',width:"20%",halign:"center",align:"center",sortable:"true"},
//			{field:'created',title:'CREATED',width:"20%",halign:"center",align:"center",sortable:"true"},
//			{field:'last_ddl_time',title:'LAST_DDL_TIME',width:"20%",halign:"center",align:"center",sortable:"true"}
			{field:'owner',title:'OWNER',width:"100",halign:"center",align:"left",sortable:"true"},
			{field:'object_name',title:'OBJECT_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'subobject_name',title:'SUBOBJECT_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'object_type',title:'OBJECT_TYPE',halign:"center",align:"left",sortable:"true"},
			{field:'created',title:'CREATED VALUE',halign:"center",align:"center",sortable:"true"},
			{field:'last_ddl_time',title:'LAST_DDL_TIME',halign:"center",align:"center",sortable:"true"}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - NEW CREATED OBJECT"," "); 

	ajaxCall("/PreventiveCheck/NewCreatedObject",
			$("#submit_form"),
			"POST",
			callback_NewCreatedObjectAction);
}

//callback 함수
var callback_NewCreatedObjectAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};