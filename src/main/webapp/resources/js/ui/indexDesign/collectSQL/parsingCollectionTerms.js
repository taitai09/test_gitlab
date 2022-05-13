$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
			$("#selectDbid").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#exec_seq").val(row.exec_seq);
			AddTab_CollectionIndexDesign();
		},
		columns:[[
			{field:'exec_seq',title:'파싱순번',halign:"center",align:"center",sortable:"true"},
			{field:'start_snap_dt',title:'시작 수집일시',halign:"center",align:'center',sortable:"true"},
			{field:'end_snap_dt',title:'종료 수집일시',halign:"center",align:'center',sortable:"true"},
			{field:'analysis_sql_cnt',title:'SQL수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'table_count',title:'테이블수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_count',title:'ACCESS PATH수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_exec_dt',title:'파싱시작일시',halign:"center",align:'center',sortable:"true"},
			{field:'access_path_exec_end_dt',title:'파싱완료일시',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
});

function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	} else {
		if($('#strEndDt').textbox('getValue') == ""){
			parent.$.messager.alert('','종료일자를 선택해 주세요.');
			return false;
		}
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#dbid").val($('#selectDbid').combobox('getValue'));


	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수집 SQL 조건절 파싱"," "); 
	
	ajaxCall("/ParsingCollectionTerms",
		$("#submit_form"),
		"POST",
		callback_ParsingCollectionTermsAddTable);
}

//callback 함수
var callback_ParsingCollectionTermsAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function showSnapShot(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		
	
	$('#snapSList').datagrid('loadData',[]);
	$('#snapEList').datagrid('loadData',[]);
	
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	$("#snapShot_form #dbid").val($('#selectDbid').combobox('getValue'));
	$('#snapShot_form #dbName').textbox('setValue',$('#selectDbid').combobox('getText'));
	
	$("#snapShot_form #strStartDt").datebox("setValue", $("#submit_form #strStartDt").datebox("getValue"));
	$("#snapShot_form #strEndDt").datebox("setValue", $("#submit_form #strEndDt").datebox("getValue"));		

	
	
	$("#snapShotPop").window("open");
}

/* 수집SQL 인덱스 설계 탭 생성 */
function AddTab_CollectionIndexDesign(){
	$("#dbid").val($('#selectDbid').combobox('getValue'));

	var menuId = "151";
	var menuNm = "수집SQL 인덱스 설계";
	var menuUrl = "/CollectionIndexDesign";
	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	
	/* 탭 생성 */
//	parent.parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
	parent.selectTab2(menuParam);	
}