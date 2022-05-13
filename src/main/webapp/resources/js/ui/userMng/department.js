$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'dept_cd',title:'부서코드',width:"13%",halign:"center",align:"center",sortable:"true"},
			{field:'dept_nm',title:'부서명',width:"17%",halign:"center",align:'left'},			
			{field:'upper_dept_cd',title:'상위부서코드',width:"13%",halign:"center",align:'center'},
			{field:'upper_dept_nm',title:'상위부서명',width:"17%",halign:"center",align:'left'},
			{field:'dept_desc',title:'부서설명',width:"30%",halign:"center",align:'left'},
			{field:'use_yn',title:'사용여부',width:"10%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	// 부서 리스트 조회	
	$('#dept_cd').combotree({
	    url:"/Common/getDept",
	    method:'get'
	});
	
	// 상위부서 리스트 조회	
	$('#upper_dept_cd').combotree({
	    url:"/Common/getDept",
	    method:'get'
	});		
});

function Btn_OnClick(){
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}

	$('#tableList').datagrid('loadData',[]);
	$('#tableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#tableList').datagrid('loading'); 
	
	ajaxCall("/System/UserMng/Department",
			$("#submit_form"),
			"POST",
			callback_DepartmentAction);		
}

//callback 함수
var callback_DepartmentAction = function(result) {
	Btn_ResetField();
	
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	$('#tableList').datagrid('loaded');	
};

function Btn_SaveDept(){
	if($("#dept_cd").textbox('getValue') == ""){
		$.messager.alert('','부서코드를 입력해 주세요.');
		return false;
	}
	
	if($("#dept_nm").textbox('getValue') == ""){
		$.messager.alert('','부서명을 입력해 주세요.');
		return false;
	}
	
	if($("#use_yn").combobox('getValue') == ""){
		$.messager.alert('','사용여부를 선택해 주세요.');
		return false;
	}

	ajaxCall("/System/UserMng/SaveDepartment",
			$("#detail_form"),
			"POST",
			callback_SaveDepartmentAction);		
}

//callback 함수
var callback_SaveDepartmentAction = function(result) {
	if(result.result){
		$.messager.alert('','부서 정보 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});
	}
};

function setDetailView(selRow){
	$("#dept_cd").combotree("setValue", selRow.dept_cd);
	$("#upper_dept_cd").combotree("setValue", selRow.upper_dept_cd);
	$("#dept_desc").textbox("setValue", selRow.dept_desc);
	$("#use_yn").combobox("setValue", selRow.use_yn);
}

function Btn_ResetField(){
	$("#dept_cd").combotree("setValue", "");
	$("#upper_dept_cd").combotree("setValue", "");
	$("#dept_desc").textbox("setValue", "");
	$("#use_yn").combobox("setValue", "");
}