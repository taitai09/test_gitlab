var currentPagePerCount = 10;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#searchValue').textbox({readonly:true});
		
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});
	// 권한명 조회			
	$('#submit_form #auth_id').combobox({
		url:"/auth/getAuthNmList?isChoice=X",
		method:"get",
		valueField:'auth_id',
		textField:'auth_nm',
		onLoadError: function(){
			parent.$.messager.alert('','권한명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items){
//			if (items.length){
//				var opts = $(this).combobox('options');
//				$(this).combobox('select', items[0][opts.valueField]);
//			}
//			var data = $('#cc').combobox('getData');
//			data.push({id:'idvalue',text:'textvalue'});
//			$('#cc').combobox('loadData', data);
			$('#submit_form #auth_id').combobox('textbox').attr("placeholder","선택");
		},
		onChange : function(newValue,oldValue){
			if(newValue == "") {
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else{
				$('#searchValue').textbox({readonly:false});
			}
		}
/*	    onSelect:function(rec){
	    	if(rec.auth_id == "") return;
	    	
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	console.log("auth_id:"+rec.auth_id);
	    	$("#submit_form #auth_id").val(rec.auth_id);
	    	ajaxCallAuth();		    	
	    }*/
	});
	$('#auth_grp_id').combobox({
	    url:"/auth/getAuthNmList",
	    method:"get",
		valueField:'auth_id',
	    textField:'auth_nm',
		onLoadError: function(){
			parent.$.messager.alert('','권한명 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : false,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
//			{field:'auth_id',title:'순번',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'auth_nm',title:'권한명',width:"40%",halign:"center",align:'center'},
//			{field:'auth_cd',title:'권한코드',width:"40%",halign:"center",align:'center'},
//			{field:'use_yn',title:'사용여부',width:"10%",halign:"center",align:'center'}
		    {field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true"},
			{field:'auth_nm',title:'권한명',width:'20%',halign:"center",align:'center'},
			{field:'user_id',title:'사용자ID',width:'20%',halign:"center",align:'center'},
			{field:'user_nm',title:'사용자명',width:'20%',halign:"center",align:'center'},
			{field:'auth_start_day',title:'권한시작일자',width:'19%',halign:"center",align:'center'},
			{field:'auth_end_day',title:'권한종료일자',width:'19%',halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$('#searchCount').combobox({
		onChange:function(newval,oldval){
			currentPagePerCount = newval;
			$("#submit_form #pagePerCount").val(currentPagePerCount); //검색페이지수 설정
		}
	});
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#nextBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	
});
/*
function Btn_OnClick(){
//	if(($('#searchValue').textbox('getValue') == "")){
//		parent.$.messager.alert('','사용자명을 입력해 주세요.');
//		
//		return false;
//	}
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("권한 관리"," "); 

	ajaxCallAuth();	
}
*/

function Btn_SaveAuth(){
	if($("#auth_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','권한명을 입력해 주세요.');
		return false;
	}
	
	if($("#auth_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','권한코드를 입력해 주세요.');
		return false;
	}	
	
	if(!compareAnBDate($("#auth_form #authStartDay").datebox('getValue'), $("#auth_form #authEndDay").datebox('getValue'))){
		parent.$.messager.alert('','시작일과 종료일을 확인해 주세요.');
		return false;
	}
	
	if($("#use_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','사용여부를 선택해 주세요.');
		return false;
	}

	
	ajaxCall("/Auth/SaveAuth",
			$("#detail_form"),
			"POST",
			callback_SaveAuthAction);		
}

//callback 함수
var callback_SaveAuthAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_ResetField();
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setDetailView(selRow){
//	$("#auth_id").val(selRow.auth_id);
//	$("#auth_grp_id").combobox("setValue", selRow.auth_id);
	$("#authStartDay").datebox("setValue", selRow.auth_start_day);
	$("#authEndDay").datebox("setValue", selRow.auth_end_day);
}

function Btn_ResetField(){
//	$("#submit_form #auth_id").val($("#auth_form #auth_id").val());
	$("#auth_grp_id").combobox("setValue", "");
	$("#authStartDay").datebox("setValue", "");
	$("#authEndDay").datebox("setValue", "");
	//$("#authStartDay").textbox("readonly", false);
	//$("#authEndDay").textbox("readonly", false);
}

function Btn_SaveUserAuth(){
//    var checkedRow = $("#tableList").datagrid("getSelected");
//    var rowIndex = $("#tableList").datagrid("getRowIndex", checkedRow);
	
	row = $('#tableList').datagrid('getChecked');
	var rowLength = row.length;
    
	if(row.length == 0){
		parent.$.messager.alert('','저장할 데이터가 없습니다. <br/>사용자를 선택해 주세요.');
		return false;
	}
	if(!checkDate($("#auth_form #authStartDay").datebox('getValue')) || $("#auth_form #authStartDay").datebox('getValue') == ""){
		parent.$.messager.alert('','권한시작일자를 정확히 입력해주세요.');
		return false;
	}	
	if(!checkDate($("#auth_form #authEndDay").datebox('getValue')) || $("#auth_form #authEndDay").datebox('getValue') == ""){
		parent.$.messager.alert('','권한종료일자를 정확히 입력해주세요.');
		return false;
	}	
	
    var user_ids = new Array();
    for(var i=0;i<rowLength;i++){
    	user_ids.push(row[i].user_id);
    }
    $("#user_ids").val(user_ids.toString());
    
//	$("#auth_form #auth_id").val($("#auth_id").combobox("getValue"));
	$("#auth_start_day").val($("#authStartDay").datebox("getValue"));
	$("#auth_end_day").val($("#authEndDay").datebox("getValue"));    

	ajaxCall("/Auth/SaveAuthBundle",
			$("#auth_form"),
			"POST",
			callback_SaveAuthAction);
}



/*페이징처리시작*/
function fnSetCurrentPage(direction){
	console.log("direction : "+direction);
	var currentPage = $("#submit_form #currentPage").val();
	
	console.log("currentPage : "+currentPage);
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		console.log("currentPage2 : "+currentPage);
		
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
	if(!formValidationCheck()){  //현재 없음.
		return;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
	}
	
	fnSearch();
}

function fnSearch(){

	
	parent.frameName = $("#menu_id").val();

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("권한 관리(일괄 변경)"," "); 

	ajaxCallAuth();	
}


function ajaxCallAuth(){
//	ajaxCall("/Auth",
//			$("#submit_form"),
//			"POST",
//			callback_AuthAction);
	
	var auth_id = $("#auth_id").combobox("getValue");
	$("#auth_form #auth_id").val(auth_id);
	ajaxCall("/Common/getAuthUserName",
			$("#submit_form"),
			"POST",
			callback_AuthAction);	
}


//검색_callback 함수가 들어갈곳
//callback 함수
var callback_AuthAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);

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
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if(dataLength > 10){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else{
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	if(currentPage == 1){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		if(dataLength > pagePerCount){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}	
};

function formValidationCheck(){
	
	
	if($("#submit_form #auth_id").combobox('getValue') == ""){
		parent.$.messager.alert('','권한명을 선택해주세요.');
		return false;
	}

/*	if(($('#auth_id').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색어를 입력해 주세요.');
		return false;
	}*/
	
	return true;
}
/*페이징처리끝*/

function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}

	$("#submit_form").attr("action","/UsersAuth/ExcelDown");
	$("#submit_form").submit();
}
