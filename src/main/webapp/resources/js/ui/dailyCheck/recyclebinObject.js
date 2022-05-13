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
			{field:'owner',title:'OWNER',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'object_name',title:'OBJECT_NAME',width:"20%",halign:"center",align:"left",sortable:"true"},
			{field:'original_name',title:'ORIGINAL_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'operation',title:'OPERATION',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'type',title:'TYPE',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'ts_name',title:'TS_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'createtime',title:'CREATETIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'droptime',title:'DROPTIME',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'blocks',title:'BLOCKS',width:"5%",halign:"center",align:"right",sortable:"true"},
			{field:'space_used',title:'SPACE_USED(GB)',width:"10%",halign:"center",align:"right",sortable:"true"}
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - RECYCLEBIN OBJECT"," "); 
	
	ajaxCall("/PreventiveCheck/RecyclebinObjectCount",
			$("#submit_form"),
			"POST",
			callback_RecyclebinObjectCountAction);	

	ajaxCall("/PreventiveCheck/RecyclebinObject",
			$("#submit_form"),
			"POST",
			callback_RecyclebinObjectAction);
}

//callback 함수
var callback_RecyclebinObjectCountAction = function(result) {
	if(result.result){
		var obj = result.object;
		$("#objectCnt").html(nvl(obj.recyclebin_cnt,"0"));
		$("#objectSize").html(nvl(obj.recyclebin_size,"0") + " MB");
	}
}

//callback 함수
var callback_RecyclebinObjectAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};