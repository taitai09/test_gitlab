$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			var menuParam = "";			        	
			
        	var menuId = "133";
        	var menuNm = "애플리케이션 분석";
        	var menuUrl = "/APPPerformance";
            
        	//var tr_cd = $("#submit_form #tr_cd").val();
        	//var dbio = $("#submit_form #temp_dbio").val();
            //menuParam = "&wrkjob_cd="+row.wrkjob_cd+"&tr_cd="+tr_cd+"&dbio="+dbio+"&call_from_parent=Y";
        	var tr_cd = row.tr_cd;
        	menuParam = "&wrkjob_cd="+row.wrkjob_cd+"&tr_cd="+tr_cd+"&call_from_parent=Y";
			
			parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
			
		},
		onClickCell : function(index,cellId,cellValue) {
			
//        	$("#submit_form #tr_cd").val("");
//        	$("#submit_form #temp_dbio").val("");
//        	
//            if(cellId == "dbio"){
//            	$("#submit_form #temp_dbio").val(cellValue);
//            }else if(cellId == "tr_cd"){
//            	$("#submit_form #tr_cd").val(cellValue);
//            }else{
//            	$("#submit_form #temp_dbio").val(cellValue);
//            }
		},		
		columns:[[
		    {field:'deploy_perf_check_no',title:'점검번호',halign:"center",align:'center',sortable:"true"},
		    {field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true"},
		    {field:'tr_cd',title:'애플리케이션명',halign:"center",align:"left",sortable:"true"},
			{field:'dbio',title:'DBIO',halign:"center",align:'left',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",formatter:getNumberFormat},
			{field:'prohibit_hint_use_yn',title:'금지힌트사용여부',halign:"center",align:'center',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"}
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
	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallApplicationDBIOCheck();
	}	
	
});

function ajaxCallApplicationDBIOCheck(){
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("애플리케이션 성능점검 - SQL식별자(DBIO) 상세내역"," "); 
	
	ajaxCall("/ApplicationDBIOCheckAction",
			$("#submit_form"),
			"POST",
			callback_ApplicationDBIOCheckActionAddTable);		
	
}

//callback 함수
var callback_ApplicationDBIOCheckActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);

	//$(".datagrid-btable td").css("cursor","default");
	
}

function Btn_OnClick(){
	goApplicationDbioCheck();
}

function goApplicationDetailCheck(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ApplicationDetailCheck");
	$("#submit_form").submit();
}

function goApplicationDbioCheck(){
	$("#submit_form").attr("action","/ApplicationDBIOCheck");
	$("#submit_form").submit();
}

function goApplicationCheck(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ApplicationCheck");
	$("#submit_form").submit();	
}