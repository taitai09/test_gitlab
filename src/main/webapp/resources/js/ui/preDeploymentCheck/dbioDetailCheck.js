$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			var menuParam = "";			        	
			
        	var menuId = "131";
        	var menuNm = "SQL 성능 분석";
        	var menuUrl = "/SQLPerformance";
            
            menuParam = "dbid="+row.dbid+"&sql_id="+row.sql_id+"&call_from_parent=Y";
			parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
			
		},
		onClickCell : function(index,cellId,cellValue) {
//            if(cellId == "dbio"){
//            	menuParam = "dbio="+cellValue+"&call_from_parent=Y";
//            }else if(cellId == "tr_cd"){
//            	menuParam = "tr_cd="+cellValue+"&call_from_parent=Y";
//            }else if(cellId == "sql_id"){
//            	menuParam = "sql_id="+cellValue+"&call_from_parent=Y";
//            }else{
//            	menuParam = "dbio="+cellValue+"&call_from_parent=Y";
//            }
//        	menuParam = "sql_id="+cellValue+"&call_from_parent=Y";
		},		
		columns:[[
		    {field:'deploy_perf_check_no',title:'점검번호',halign:"center",align:'center',sortable:"true",styler:cellStyler},
		    {field:'dbid',hidden:'true'},
//		    {field:'dbid',title:'dbid',halign:"center",align:'center',sortable:"true",styler:cellStyler},
		    {field:'wrkjob_cd',hidden:'true'},
		    {field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'dbio',title:'DBIO',halign:"center",align:'left',sortable:"true",styler:cellStyler},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'perf_fitness_yn',title:'성능접합여부',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'prohibit_hint_use_yn',title:'금지힌트사용여부',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true",styler:cellStyler}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	var chkValue = false;
	
	if($("#prohibit_hint_use_yn").val() == "Y") chkValue = true;
	
	$('#chkProhibitUseYn').switchbutton({
		checked: chkValue,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#prohibit_hint_use_yn").val("Y"); 
			else $("#prohibit_hint_use_yn").val("N");
		}
	});
	
	ajaxCallDBIODetailCheck();

});

function cellStyler(value,row,index){
	var perf_fitness_yn = row.perf_fitness_yn;
	if(perf_fitness_yn == "부적합"){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
	}
}

function ajaxCallDBIODetailCheck(){
	
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL식별자(DBIO) 성능점검 - 상세내역"," "); 
	
	ajaxCall("/DBIODetailCheckAction",
			$("#submit_form"),
			"POST",
			callback_DBIODetailCheckActionAddTable);
	
}

//callback 함수
var callback_DBIODetailCheckActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	$("#tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	
}

function Btn_OnClick(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
//	$("#submit_form").attr("action","/DBIODetailCheck");
//	$("#submit_form").submit();
	ajaxCallDBIODetailCheck();
	
}

function returnDBIODetailCheck(){
	$("#call_from_child").val("Y");
	$("#submit_form").attr("action","/DBIOCheck");
	$("#submit_form").submit();	
}