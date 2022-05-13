var menuId = "";
var menuNm = "";
var dataGridDynamicHeaderCnt = 0;
var callFromParent = "";
var check_pref_id = "";

$(document).ready(function() {
	// Database 조회			
	$('#selectDbid').combobox({
	    url:"/Common/getDatabase?isAll=N&isSelected=N",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onLoadSuccess:function(rec){
	    },
	    onSelect:function(rec){
    		$("#dbid").val(rec.dbid);
	    }
	    
	});
	
	$('#selectDbid').combobox("setValue",$("#dbid").val());
	
	// 점검항목 조회			
	$('#selectCheckItem').combobox({
	    url:"/ExceptionManagement/getCheckItem",
	    method:"get",
		valueField:'check_pref_id',
	    textField:'check_pref_nm',
		onLoadError: function(){
			parent.$.messager.alert('','점검항목 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onLoadSuccess:function(items){
	    	if (items.length){
	    		var opts = $(this).combobox('options');
	    		$(this).combobox('select', items[0][opts.valueField]);
	    	}
	    },
	    onSelect:function(rec){
	    	check_pref_id = rec.check_pref_id;
	    	$("#searchValue").textbox('setValue','');
    		fnCreateSearchKey(rec.check_pref_id);
	    }
	});

	function fnCreateSearchKey(check_pref_id){
		// 점검항목 검색조건 콤보데이터 조회			
		$('#searchKey').combobox({
			url:"/ExceptionManagement/getDbCheckExceptionHeadTitleList?check_pref_id="+check_pref_id,
			method:"get",
			valueField:'check_except_head_title_cd',
			textField:'check_except_head_title_nm',
			onLoadError: function(){
				parent.$.messager.alert('','검색조건 조회중 오류가 발생하였습니다.');
				return false;
			},
			onLoadSuccess:function(items){
				dataGridDynamicHeaderCnt = items.length;
				console.log("items.length:"+items.length);
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
					
//					$("#submit_form #check_except_object_index").val(1);
					
					$("#submit_form #check_pref_id").val($("#selectCheckItem").combobox("getValue"));
				}
			},
			onSelect:function(rec){
		    	$("#searchValue").textbox('setValue','');
				$("#submit_form #check_except_object_index").val(rec.no);
			}
		});
	}
	
	$("#tableList").datagrid({
		view: emptyview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,		
		onClickCell : function(index,field,value) {
			var row = $(this).datagrid('getRows')[index];
			$("#dbid").val(row.dbid);

		},		
		columns:[[
		]],		

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#searchValue").textbox('textbox').bind('keyup', function(e){
			if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
				Btn_OnClick();
			}
	});
	
	$("#deleteExceptionBtn").click(function(){
		fnDeleteException();
	});
	
	callFromParent = $("#call_from_parent").val();
	
	if(callFromParent == "Y"){
	}
});

/**
 * 예외삭제 버튼
 * @returns
 */
function fnDeleteException(){
//	DB_CHECK_EXCEPTION_NO
	var exceptionNoArry = "";
	
	var rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		for(var i = 0; i < rows.length; i++){
			exceptionNoArry += rows[i].db_check_exception_no + "|";
			console.log("rows[i].db_check_exception_no :"+rows[i].db_check_exception_no);
		}		
		console.log("exceptionNoArry :",exceptionNoArry);
		$("#submit_form #db_check_exception_no").val(strRight(exceptionNoArry,1));

		ajaxCall("/ExceptionManagement/DeleteException", $("#submit_form"), "POST", callback_DeleteExceptionAction);		
	}else{
		parent.$.messager.alert('','예외삭제할 데이터를 선택해 주세요.');
	}		
}

//callback 함수
var callback_DeleteExceptionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','예외삭제 처리되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);			
		});		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
}

function formValidationCheck(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	return true;
}

function Btn_OnClick(){
	if(!formValidationCheck()) return;
	
	$('#tableList').datagrid("loadData", []);
	
	$("#currentPage").val("1");
	//fnSearch();
	ajaxCall("/ExceptionManagement/getDbCheckExceptionHeadTitleList", $("#submit_form"), "POST", callback_DbCheckExceptionHeadTitleAction);
	
}

function fnSearch(){

	$("#submit_form #check_pref_id").val($("#selectCheckItem").combobox("getValue"));
	$("#submit_form #check_except_object").val($("#searchKey").combobox("getText"));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("예외 관리"," "); 

	ajaxCall("/ExceptionManagement/getDbCheckExceptionList", $("#submit_form"), "POST", callback_DbCheckExceptionAction);
}

//callback 함수
var callback_DbCheckExceptionHeadTitleAction = function(result) {
	try{
		
		var data = JSON.parse(result);
		console.log("data.length :",data.length);
		var headArrayTest = new Array();
		headArrayTest.push({field:'chk',halign:"center",align:"center",checkbox:"true"});
		for(var i=0;i<data.length;i++){
			var check_except_head_title_cd = data[i].check_except_head_title_cd;
			var check_except_head_title_nm = data[i].check_except_head_title_nm;
			headArrayTest.push({field:'check_except_object_name_'+(i+1),title:check_except_head_title_nm,halign:"center",align:"center"});
		}	
		headArrayTest.push({field:'except_process_dt',title:'예외등록일시',halign:"center",align:"center"});
		headArrayTest.push({field:'user_nm',title:'예외등록자',halign:"center",align:"center"});
		headArrayTest.push({field:'dbid',title:'dbid',hidden:true});
		headArrayTest.push({field:'db_name',title:'db_name',hidden:true});
		headArrayTest.push({field:'check_pref_id',title:'check_pref_id',hidden:true});
		headArrayTest.push({field:'check_pref_nm',title:'check_pref_nm',hidden:true});
		headArrayTest.push({field:'db_check_exception_no',title:'db_check_exception_no',hidden:true});
		console.log(headArrayTest);
		
		$("#tableList").datagrid({
			columns:[headArrayTest]
		});
		
		fnSearch();
	}catch(e){
		console.log("e.message:"+e.message);
		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);	
			});			
		}else{
			parent.$.messager.alert('',e.message);
		}		
	}
	
};
//callback 함수
var callback_DbCheckExceptionAction = function(result) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

	//	json_string_callback_common(result, '#tableList', true);
	try{
		
		var data = JSON.parse(result);
		$('#tableList').datagrid("loadData", data);
		
		
		var dataLength=0;
		dataLength = data.dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
	}catch(e){
		console.log("e.message:"+e.message);
		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);	
			});			
		}else{
			parent.$.messager.alert('',e.message);
		}
	}
};

/**
 * 엑셀 다운로드
 * @returns
 */
function Excel_Download(){
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}
	
	$("#submit_form #check_pref_id").val($("#selectCheckItem").combobox("getValue"));
	$("#submit_form #check_except_object").val($("#searchKey").combobox("getText"));
	
	$("#submit_form").attr("action","/ExceptionManagement/getDbCheckExceptionList/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
