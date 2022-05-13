$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#selectUserName').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				},
				onSelect:function(rec1){
					var win = parent.$.messager.progress({
						title:'Please waiting',
						text:'데이터를 불러오는 중입니다.'
					});
					// table 조회			
					$('#selectTableName').combobox({
						url:"/Common/getTableName?dbid="+rec.dbid+"&username="+rec1.username+"&ods_table_name=ODS_TAB_MODIFICATIONS",
						method:"get",
						valueField:'table_name',
						textField:'table_name',
						onLoadSuccess: function(event) {
							parent.$.messager.progress('close');
							$('#selectTableName').combobox('textbox').attr("placeholder","전체");
						}
					});
				}
			});
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		columns:[[
//			{field:'table_name',title:'TABLE_NAME',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'partition_name',title:'PARTITION_NAME',width:"13%",halign:"center",align:'center',sortable:"true"},
//			{field:'subpartition_name',title:'SUBPARTITION_NAME',width:"13%",halign:"center",align:'center',sortable:"true"},
//			{field:'inserts',title:'INSERTS',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'updates',title:'UPDATES',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'deletes',title:'DELETES',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'truncated',title:'TRUNCATED',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'timestamp',title:'변경일시',width:"12%",halign:"center",align:'center',sortable:"true"},
//			{field:'base_day',title:'수집일자',width:"8%",halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
//			{field:'all_change_cnt',title:'전체변경건수',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'rank',title:'변경순위',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'subpartition_name',title:'SUBPARTITION_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'inserts',title:'INSERTS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'updates',title:'UPDATES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'deletes',title:'DELETES',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'truncated',title:'TRUNCATED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'timestamp',title:'변경일시',halign:"center",align:'center',sortable:"true"},
//			{field:'base_day',title:'수집일자',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'base_day',title:'수집일자',halign:"center",align:'center'},
			{field:'all_change_cnt',title:'전체변경건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'rank',title:'변경순위',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],
		
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		} 
	});
	
	//이전, 다음 처리
	$("#prevBtn").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#nextBtn").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtn").linkbutton({disabled:true});
	$("#nextBtn").linkbutton({disabled:true});
	
});

/*function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	}	
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_name").val($('#selectTableName').combobox('getValue'));
	
	$('#tableList').datagrid("loadData", []);
	
	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일자별 DML 변경 내역"," "); 
	
	ajaxCall("/DMLChangeDaily",
		$("#submit_form"),
		"POST",
		callback_DMLChangeDailyAddTable);		
}*/

//callback 함수
//var callback_DMLChangeDailyAddTable = function(result) {
//	json_string_callback_common(result,'#tableList',true);
//};


/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#submit_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}
	Btn_OnClick('P');
}

function Btn_OnClick(val){

	if(!formValidationCheck()){
		return false;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
//		$("#submit_form #pagePerCount").val('10');
		$("#submit_form #pagePerCount").val('23');
	}
	fnSearch();
	$("#call_from_parent").val("N");
	$("#call_from_child").val("N");
}



function fnSearch(){

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_name").val($('#selectTableName').combobox('getValue'));
	
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일자별 DML 변경 내역"," "); 
	
	ajaxCall("/DMLChangeDaily",
		$("#submit_form"),
		"POST",
		callback_DMLChangeDailyAddTable);		
	
}

//callback 함수
var callback_DMLChangeDailyAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result); //페이징 보여줄지 말지여부세팅
};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);

	var data;
	var dataLength=0;
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	}catch(e){
		parent.$.messager.alert('',e.message);
	}

	//페이지를 보여줄지말지 여부를 결정
	if(currentPage > 1){
		$("#prevBtn").linkbutton({disabled:false});
		
		if(dataLength > 10){
			$("#nextBtn").linkbutton({disabled:false});

		}else{
			$("#nextBtn").linkbutton({disabled:true});
		}
	}
	if(currentPage == 1){
		$("#prevBtn").linkbutton({disabled:true});
		
		$("#nextBtn").linkbutton({disabled:true});
		if(dataLength > pagePerCount){
			$("#nextBtn").linkbutton({disabled:false});
		}
	}	
};

function formValidationCheck(){
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	}	
	
	return true;
}
/*페이징처리끝*/

function Excel_Download(){
	
	if(!formValidationCheck()){
		return false;
	}
	
	
	$("#submit_form").attr("action","/DMLChangeDaily/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}





