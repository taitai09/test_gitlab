$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#user_id").val(row.user_id);
			$("#sched_seq").val(row.sched_seq);
			getDetailView();
		},		
		columns:[[
			{field:'user_id',hidden:'true'},
			{field:'sched_seq',hidden:'true'},
			{field:'sched_type_cd',hidden:'true'},
			{field:'user_nm',title:'등록자',width:"10%",halign:"center",align:'center'},
			{field:'sched_type_nm',title:'유형',width:"10%",halign:"center",align:'center'},
			{field:'sched_title',title:'제목',width:"20%",halign:"center",align:'left'},
			{field:'sched_sbst',title:'내용',width:"20%",halign:"center",align:'left'},
			{field:'sched_start_dt',title:'일정시작일시',width:"15%",halign:"center",align:'center'},
			{field:'sched_end_dt',title:'일정종료일시',width:"15%",halign:"center",align:'center'},
			{field:'reg_dt',title:'등록일',width:"10%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#tableList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일정 관리"," "); 	

	ajaxCall("/Schedule/ListAction",
		$("#submit_form"),
		"POST",
		callback_CalendarAddTable);	
});

function Btn_OnClick() {
	if(($("#searchKey").val() != "" && $("#searchValue").val() == "") ||
		($("#searchKey").val() == "" && $("#searchValue").val() != "")){
			$.messager.alert('','검색 조건을 제대로 선택해 주세요');
			return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#submit_form").attr("action","/Schedule/List");
	$("#submit_form").submit();
}

//callback 함수
var callback_CalendarAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}

function Btn_InsertSchedule(){
	$("#submit_form").attr("action","/Schedule/Insert");
	$("#submit_form").submit();	
}

function getDetailView(){
	$("#submit_form").attr("action","/Schedule/View");
	$("#submit_form").submit();	
}