$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#table_owner").val(row.table_owner);
			$("#list_table_name").val(row.table_name);
			getDetailView();
		},	
		columns:[[
		  	{field:'rnum',title:'NO',halign:"center",align:'center',sortable:"true"},
		    {field:'table_owner',title:'OWNER',halign:"center",align:'center',sortable:"true"},
		    {field:'table_name',title:'테이블명',halign:"center",align:'center',sortable:"true"},		    
		    {field:'plan_change_cnt',title:'플랜변경건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'perf_regress_cnt',title:'성능저하건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'sql_cnt',title:'SQL수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallObjectImpactDiagnostics();
	}

});

function ajaxCallObjectImpactDiagnostics(){
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("오브젝트 진단 > 테이블 내역"," "); 
	
	ajaxCall("/ObjectImpactDiagnostics/TableAction",
			$("#submit_form"),
			"POST",
			callback_ObjectImpactDiagnosticsTableActionAddTable);	
	
}

//callback 함수
var callback_ObjectImpactDiagnosticsTableActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}

function getDetailView(){
	$("#submit_form").attr("action","/ObjectImpactDiagnostics/TableDetail");
//	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단 상세 내역");
	$("#submit_form #menu_nm").val("오브젝트변경 SQL 성능 영향도 진단 상세 내역");
	$("#submit_form").submit();
}

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$("#submit_form").attr("action","/ObjectImpactDiagnostics/Table");
//	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단 테이블 내역")
	$("#submit_form #menu_nm").val("오브젝트변경 SQL 성능 영향도 진단 테이블 내역")
	$("#submit_form").submit();
}

function goList(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ObjectImpactDiagnostics");
	$("#submit_form #menu_nm").val("오브젝트변경 SQL영향도 진단");
	$("#submit_form").submit();	
}