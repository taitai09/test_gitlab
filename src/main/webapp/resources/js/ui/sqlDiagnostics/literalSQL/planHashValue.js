$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
	    onSelect:function(rec){
	    	$("#dbid").val(rec.dbid);
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#gather_day").val(row.gather_day);
			$("#literal_seq").val(row.literal_seq);
			fnSearch();
		},
		columns:[[
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',width:"13%",halign:"center",align:"center",sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"62%",halign:"center",align:'left',sortable:"true"},
			{field:'sql_cnt',title:'COUNT',width:"10%",halign:"center",align:'right',sortable:"true"},
			{field:'gather_day',title:'기준일자',width:"15%",halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'literal_seq',hidden:true},
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#sqlTabs').tabs({
		plain : true
	});	
	
	
	$("#bottomList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			parent.createSQLNewTab($("#menu_id").val(), "sqlTabs", $("#dbid").val(), row.sql_id, row.plan_hash_value);
		},
		columns:[[
			{field:'sql_id',title:'SQL_ID',width:"10%",halign:"center",align:"CENTER",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',width:"10%",halign:"center",align:'center'},
			{field:'sql_text',title:'SQL_TEXT',width:"80%",halign:"center",align:'LEFT',sortable:"false"}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});
	
	$('#sqlTabs').tabs({
		plain : true,
		onSelect: function(title,index){
			if(index != 0){
				$("#paging_botton").hide();
			}else{
				$("#paging_botton").show();
			}
			/* 탭을 클릭시 화면을 높이 자동 조절 */
			/*var height = $("#container").height();
			parent.resizeTopFrame($("#menu_id").val(), height);*/  
		}
	});	
	
	
	$('#selectCombo').combobox("setValue",$("#dbid").val());
	
	Btn_OnClick();	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
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
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));

	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 진단 - Literal SQL[PLAN HASH VALUE 기반]"," "); 
	
	ajaxCall("/SQLDiagnostics/LiteralSQL",
		$("#submit_form"),
		"POST",
		callback_LiteralSQLAddTable);
}

//callback 함수
var callback_LiteralSQLAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}


function fnSearch(){
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$('#bottomList').datagrid('loadData',[]);
	
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 진단 - SQL 현황"," "); 
	
	ajaxCall("/SQLDiagnostics/LiteralSQLStatus",
			$("#submit_form"),
			"POST",
			callback_LiteralSQLStatusAction);
}

//callback 함수
var callback_LiteralSQLStatusAction = function(result) {
	json_string_callback_common(result,'#bottomList',true);
	
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
}

function formValidationCheck(){
	return true;
}
