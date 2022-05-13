$(document).ready(function() {

	var initValues = '${initValues}';
	var program_type_cd = '${initValues.program_type_cd}';
	
	// 프로그램 유형 조회			
	$('#popup_form #program_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1005&isAll=N&isChoice=N",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
	});
	
	// Database 조회			
	$('#popup_form #dbid').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess:function(){
			setInitSetting();
		},
		onSelect:function(rec){	    	
			// parsing_schema_name 조회			
		$("#popup_form #parsing_schema_name").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}
	});	
	
	/*업무 담당자 조회 팝업 오픈*/
	$('#workTunerPop').window({
		title : "업무 담당자 조회",
		top:getWindowTop(540),
		left:getWindowLeft(850)
	});
	
	/*업무 담당자 조회 팝업 > 업무 조회*/
	$("#deptTree").treegrid({
		idField:'id',
		treeField:'text',
		rownumbers: true,
		lines: true,
		onClickCell : function(index,row) {
			setUserAddTable(row.id);
		},
		columns:[[
			{field:'parent_id',hidden:"true"},
			{field:'text',title:'업무명',width:"50%",halign:"center",align:'left'},
			{field:'wrkjob_div_cd',title:'업무코드',width:"50%",halign:"center",align:"center"},
			{field:'id', hidden:true},
			{field:'wrkjob_cd_nm',hidden:"true"}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	/*업무 담당자 조회 팝업 > 담당자 조회*/
	$("#workUserList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
			$("#wrkjob_mgr_nm").textbox('setValue',row.user_nm);
			$("#wrkjob_mgr_id").val(row.user_id);
			$("#wrkjob_mgr_wrkjob_nm").textbox('setValue',row.wrkjob_nm);
			$("#wrkjob_mgr_tel_num").textbox('setValue',row.ext_no);
			$("#wrkjob_mgr_wrkjob_cd").val(row.wrkjob_cd);
			
			$('#resetBtn').css("visibility","visible");
			closeBtn('workTunerPop');
		},			
		columns:[[
			{field:'user_id',title:'사용자ID',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"25%",halign:"center",align:'center'},
			{field:'wrkjob_nm',title:'업무명',width:"25%",halign:"center",align:'center'},
			{field:'wrkjob_div_cd',title:'업무코드',width:"25%",halign:"center",align:'center'},
			{field:'wrkjob_cd',hidden:true},
			{field:'ext_no',title:'연락처',hidden:"true"}
		]],

		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#selectValue1').combobox({
		onSelect : function(rec){
		},
		onChange : function(newValue, oldValue){
			if(newValue != ""){
				$("#searchValue1Span").css("visibility","visible");
			}else{
				$("#searchValue1Span").css("visibility","hidden");
				$("#searchValue1").textbox('setText','');
			}
		}
	});

	var t1 = $('#searchValue1');
	t1.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){	// when press ENTER key, accept the inputed value.
			searchWrkjob();
		}
	});	
	
	$('#selectValue2').combobox({
		onSelect : function(rec){
		},
		onChange : function(newValue, oldValue){
			if(newValue == "1" || newValue == "2"){
				$("#searchValue2Span").css("visibility","visible");
			}else{
				$("#searchValue2Span").css("visibility","hidden");
				$("#searchValue2").textbox('setValue','');
			}
		}
	});
	
	var t2 = $('#searchValue2');
	t2.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){	// when press ENTER key, accept the inputed value.
			searchUser();
		}
	});
	
});

function Btn_SaveInit(){
	
	//전화번호 체크
	var extNo = $("#popup_form #tuning_requester_tel_num").textbox("getValue");
	if(extNo != null && extNo != ''){
		 if(!checkTelno(extNo)){
			 parent.$.messager.alert('','전화번호를 정확히 입력해 주세요.');
			 return false;		
		 }
	}

	/* 상단 텍스트 박스 */
	ajaxCall("/SaveInitSettingPop",
			$("#popup_form"),
			"POST",
			callback_saveInitSettingAction);
}

//callback 함수
var callback_saveInitSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','초기값 설정이 완료되었습니다.');
		
		//화면에 보여줌
//		$("#submit_form #tuning_requester_tel_num").textbox('setValue',$("#popup_form #tuning_requester_tel_num").textbox('getValue'));	
//		$("#submit_form #selectProgram").combobox('setValue',$("#popup_form #program_type_cd").combobox('getValue'));	
//		$("#submit_form #selectSystem").combobox('setValue',$("#popup_form #dbid").combobox('getValue'));	
//		$("#submit_form #selectParsingSchemaName").combobox('setValue',$("#popup_form #parsing_schema_name").combobox('getValue'));	
		Btn_OnClosePopup("saveInitSettingPop");
//		location.reload();
		var action = document.forms[0].action;
		
		if ( action.indexOf('?') < 0 ) {
			document.forms[0].action = document.forms[0].action;
			document.forms[0].method = "POST";
			document.forms[0].submit();
			
		} else {
			location.href = location.href;
		}
		
	}else{
		parent.$.messager.alert('',"초기값 설정이 실패하였습니다.");
	}
	
};

function closeBtn(popUpId){
	$('#deptTree').treegrid("loadData", []);
	$('#workUserList').datagrid("loadData", []);
	Btn_OnClosePopup(popUpId);
};

/*업무담당자 조회 팝업 > 검색 클릭 시 호출되는 함수*/
function searchWrkjob(){
	$("#node_id").val("");
	setWrkjobAddTable();
}

function setWrkjobAddTable(){
	$('#deptTree').treegrid("loadData", []);
	$('#deptTree').treegrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	
	let selectValue1 = encodeURIComponent( $("#selectValue1").combobox("getValue") );
	let searchValue1 = encodeURIComponent( $("#searchValue1").textbox("getValue") );
	
	ajaxCall("/Common/getWrkJobCd?wrkjob_cd=&selectValue="+selectValue1+"&searchValue="+searchValue1,
			null,
			"GET",
			callback_getWrkjobAddTable);
}

var callback_getWrkjobAddTable = function(result) {
	var data = JSON.parse(result);
	$('#deptTree').treegrid("loadData", data);
};

function searchUser(){
	var node_id = $("#node_id").val();
	setUserAddTable(node_id);
}

function setUserAddTable(strWrkJobCd){
	
	$('#workUserList').datagrid("loadData", []);
	$('#workUserList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#workUserList').datagrid('loading');
	
	var selectValue2 = $("#selectValue2").combobox("getValue");
	var searchValue2 = $("#searchValue2").textbox("getValue");
	
	$("#tmpForm #wrkjob_cd").val(strWrkJobCd);
	$("#tmpForm #selectValue").val(selectValue2);
	$("#tmpForm #searchValue").val(searchValue2);
	
	ajaxCall("/Common/getUsers",
			$("#tmpForm"),
			"GET",
			callback_getUsersAddTable);
}

//callback 함수
var callback_getUsersAddTable = function(result) {	
	var data = JSON.parse(result);
	$('#workUserList').datagrid("loadData", data);
	$('#workUserList').datagrid('loaded');		
};

function dataReset(){
	$('#deptTree').treegrid("loadData", []);
	$('#workUserList').datagrid("loadData", []);
};

