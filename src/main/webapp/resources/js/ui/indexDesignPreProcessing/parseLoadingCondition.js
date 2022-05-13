$(document).ready(function() {
	// Database 조회			
	$('#dbid').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	$("#file_no").val("");
	    	$('input[name="dbid"]').val(rec.dbid)
	    	
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});	    	
	    	
	    	$("#selectFileNo").combobox({
				url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
				method:"get",
				valueField:'file_info',
				textField:'file_no',
				onSelect:function(rec1){
					$("#explain_exec_seq").val("");

					setFileInfo(rec1.file_info);
				},
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				}
	    	});
	    }
	});
	
	$("#explainList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#explain_exec_seq").val(row.explain_exec_seq);
			getSubTable();
		},		
		singleSelect : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'explain_exec_seq',title:'생성회차',width:"",halign:"center",align:"center",sortable:"true"},
			{field:'plan_desc',title:'PLAN (생성/오류/미생성)',width:"",halign:"center",align:'center'},
			{field:'reg_dt',title:'수행일시',width:"",halign:"center",align:'center',sortable:"true"},
			{field:'access_path_exec_yn',title:'ACCESSPATH 파싱여부',width:"",halign:"center",align:'center',sortable:"true"}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#accesspathList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#exec_seq").val(row.exec_seq);
			AddTab_LoadIndexDesign();
		},
		columns:[[
			{field:'exec_seq',title:'ACCESS PATH 파싱회차',halign:"center",align:"center",sortable:"true"},
			{field:'analysis_sql_cnt',title:'SQL수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'table_count',title:'분석대상 테이블수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_count',title:'ACCESSPATH 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_exec_dt',title:'ACCESSPATH 파싱일시',halign:"center",align:'center',sortable:"true"},
			{field:'access_path_exec_end_dt',title:'ACCESSPATH 파싱종료일시',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
});

function Btn_OnClick(){
	if($('#dbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#explainList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 조건절 파싱"," "); 
	
	ajaxCall("/ParseLoadingCondition/ExplainList",
		$("#submit_form"),
		"POST",
		callback_ExplainListAddTable);
}

//callback 함수
var callback_ExplainListAddTable = function(result) {
	json_string_callback_common(result,'#explainList',true);
};

function setFileInfo(selValue){
	$('#file_no').val(selValue);
}

function getSubTable(){
	$('#accesspathList').datagrid("loadData", []);
	$('#accesspathList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#accesspathList').datagrid('loading'); 	
	
	ajaxCall("/ParseLoadingCondition/AccessPathList",
		$("#submit_form"),
		"POST",
		callback_AccessPathListAddTable);	
}

//callback 함수
var callback_AccessPathListAddTable = function(result) {
	json_string_callback_common(result,'#accesspathList',false);
};

function insertParseLoadingCondition(){
	if($('#dbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	var rows = $('#explainList').datagrid('getChecked');
	if(rows.length <= 0){
		parent.$.messager.alert('','Explain Plan 생성회차 리스트를  선택해 주세요.');
		return false;
	}	

	for(var i=0;i<rows.length;i++){
		var row = rows[i];
		var explain_exec_seq = row.explain_exec_seq;
		$('#explain_exec_seq').val(explain_exec_seq);
		
		ajaxCall("/ParseLoadingCondition/Insert",
				$("#submit_form"),
				"POST",
				null);
	}

	before_insertAccessPath();
}

function before_insertAccessPath(){
	var w = parent.$.messager.alert('','적재SQL ACCESSPATH 파싱은 수행시간이</br>오래걸려 서버에서 실행됩니다.</br>수행 결과는 그리드에서 확인하세요.','info',function(){
				Btn_OnClick();	
				getSubTable();
	});
	
	w.window('resize', {width:330}).window('center');
}

/* 적재SQL 인덱스 설계 탭 생성 */
function AddTab_LoadIndexDesign(){

	var menuId = "153";
	var menuNm = "적재SQL 인덱스 설계";
	var menuUrl = "/LoadIndexDesign";
	var menuParam = "dbid="+$('input[name="dbid"]').val()+"&file_no="+$("#file_no").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=DBIO";
	console.log("menuParam:"+menuParam);
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);	
}