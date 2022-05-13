var checkPerformanceYn;
var checkErrorYn;
var projectFormName;

var search_lib_nm="";
var search_model_nm="";
var search_sub_nm="";

var detail_lib_nm="";
var detail_model_nm="";
var detail_sub_nm="";

$(document).ready(function() {
	
	createList();
	selectSearchLibNm();
	selectDetailLibNm();
});

// 라이브러리명 조회
function selectSearchLibNm(){
	$("body").css("visibility", "visible");
	
	$('#submit_form #lib_nm').combobox({
	    url:"/Common/getAllLibNm?isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("onLoadSuccess search_lib_nm :["+search_lib_nm+"]");
			if(search_lib_nm == ""){
				if (items.length){
					console.log("###########################");
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
		},
		onClick:function(){
			search_model_nm = "";
			search_sub_nm = "";
		},
		onLoadError: function(){
			parent.$.messager.alert('','라이브러리명 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
			console.log("selectSearchLibNm onSelect lib_nm,rec.cd_nm :"+rec.cd_nm);
	    	$("#submit_form #model_nm").combobox("loadData","[]");
	    	$("#submit_form #sub_nm").combobox("loadData","[]");
	    	$("#submit_form #model_nm").combobox("setValue","");
	    	$("#submit_form #sub_nm").combobox("setValue","");	    	
	    	$("#submit_form #model_nm").combobox("setText","전체");
	    	$("#submit_form #sub_nm").combobox("setText","전체");	    	
	    	if(rec.cd_nm != "" && rec.cd_nm != "전체"){
	    		search_lib_nm = rec.cd_nm;
	    		selectSearchModelNm(rec.cd_nm);
	    	}
	    }	    
	});
}

// 모델명 조회			
function selectSearchModelNm(){
	console.log("selectSearchModelNm...");
	$('#submit_form #model_nm').combobox({
		url:"/Common/getAllModelNm?isAll=Y"+"&lib_nm="+search_lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("search_model_nm :"+search_model_nm);
			if(search_model_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				$(this).combobox('setValue', search_model_nm);
			}
		},			
		onLoadError: function(){
			parent.$.messager.alert('','모델명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
	    	console.log("rec.cd_nm :"+rec.cd_nm);
	    	if(rec.cd_nm != "" && rec.cd_nm != "전체"){
	    		search_model_nm = rec.cd_nm;
				selectSearchSubNm();
	    	}
		}	    
	});	
}

// 주제영역명 조회
function selectSearchSubNm(){
	$('#submit_form #sub_nm').combobox({
		url:"/Common/getAllSubNm?isAll=Y"+"&lib_nm="+search_lib_nm+"&model_nm="+search_model_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("search_sub_nm :"+search_sub_nm);
			if(search_sub_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				$(this).combobox('setValue', search_sub_nm);
			}
		},			
		onLoadError: function(){
			parent.$.messager.alert('','주제영역명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
		}	    
	});	
}

// 라이브러리명 조회
function selectDetailLibNm(){
	$('#detail_form #lib_nm').combobox({
		url:"/Common/getAllLibNm?isChoice=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("detail_lib_nm :",detail_lib_nm);
			if(detail_lib_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
		},
		onClick:function(){
			detail_model_nm = "";
			detail_sub_nm = "";
		},
		onLoadError: function(){
			parent.$.messager.alert('','라이브러리명 조회중 오류가 발생하였습니다.');
			return false;
		}
		, onSelect:function(rec){
			console.log("selectDetailLibNm onSelect lib_nm,rec.cd_nm :"+rec.cd_nm);
	    	$("#detail_form #model_nm").combobox("loadData","[]");
	    	$("#detail_form #sub_nm").combobox("loadData","[]");
	    	$("#detail_form #model_nm").combobox("setValue","");
	    	$("#detail_form #sub_nm").combobox("setValue","");
	    	if(detail_model_nm != null){
	    		$("#detail_form #model_nm").combobox("setText","선택");
	    	}
	    	if(detail_sub_nm != null){
		    	$("#detail_form #sub_nm").combobox("setText","선택");	
	    	}
			
			if(rec.cd_nm != "" && rec.cd_nm != "선택"){
				detail_lib_nm = rec.cd_nm;
				selectDetailModelNm();
			}
		}
    
	});
}

// 모델명 조회			
function selectDetailModelNm(){
	console.log("@detail_lib_nm :"+detail_lib_nm);
	console.log("@detail_model_nm :"+detail_model_nm);
	if(detail_lib_nm == ""){
		console.log("lib_nm 값이 없어 model_nm을 조회할 수 없습니다.");
		return;
	}
	$('#detail_form #model_nm').combobox({
		url:"/Common/getAllModelNm?isChoice=Y"+"&lib_nm="+detail_lib_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("detail_model_nm :"+detail_model_nm);
			if(detail_model_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				if(detail_model_nm == null){
					$(this).combobox('setValue', "");
					$(this).combobox('setText', "");
				}else{
					$(this).combobox('setValue', detail_model_nm);
				}
			}
		},			
		onLoadError: function(){
			parent.$.messager.alert('','모델명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
			console.log("model_nm onSelect......");
			console.log("model_nm, rec.cd_nm :"+rec.cd_nm);
			if(rec.cd_nm != "" && rec.cd_nm != "선택"){
				detail_model_nm = rec.cd_nm;
				selectDetailSubNm();
			}
		}	    
	});	
}

// 주제영역명 조회
function selectDetailSubNm(){
	console.log("url =/Common/getSubNm?isChoice=Y"+"&lib_nm="+detail_lib_nm+"&model_nm="+detail_model_nm);
	if(detail_lib_nm == ""){
		console.log("lib_nm 값이 없어 sub_nm을 조회할 수 없습니다.");
		return;
	}	
	if(detail_model_nm == ""){
		console.log("model_nm 값이 없어 sub_nm을 조회할 수 없습니다.");
		return;
	}	
	$('#detail_form #sub_nm').combobox({
		url:"/Common/getAllSubNm?isChoice=Y"+"&lib_nm="+detail_lib_nm+"&model_nm="+detail_model_nm,
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			console.log("detail_sub_nm :"+detail_sub_nm);
			if(detail_sub_nm == ""){
				if (items.length){
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}else{
				if(detail_sub_nm == null){
					$(this).combobox('setValue', "");
					$(this).combobox('setText', "");
				}else{
					$(this).combobox('setValue', detail_sub_nm);
				}
			}
			
		},			
		onLoadError: function(){
			parent.$.messager.alert('','주제영역명 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
			console.log("sub_nm onSelect......");
		},
		onChange:function(newValue, oldValue){
			console.log("sub_nm onSelect......");
			console.log("newValue :"+newValue);
		}
	});	
}

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		columns:[[
			{field:'lib_nm',title:'라이브러리명',halign:'center',align:'center'},
			{field:'model_nm',title:'모델명',halign:'center',align:'center'},
			{field:'sub_nm',title:'주제영역명',halign:'center',align:'center'},
			{field:'ent_type_cd',title:'엔터티 유형',halign:'center',align:'center'},
			{field:'ref_ent_type_nm',title:'참조 엔터티 PREFIX',halign:'center',align:'center'},
			{field:'ent_type_nm',title:'엔터티 SUFFIX',halign:'center',align:'center'},
			{field:'tbl_type_nm',title:'테이블 유형',halign:'center',align:'center'},
			{field:'tbl_type_cd',title:'테이블 유형 코드',halign:'center',align:'center'},
			{field:'ent_type_desc',title:'설명',halign:'center',align:'left'}
		]],
		onSelect:function(index,row) {
			detail_lib_nm = row.lib_nm;
			detail_model_nm = row.model_nm;
			detail_sub_nm = row.sub_nm;
			
			$("#detail_form #lib_nm").combobox("setValue", "");
			$("#detail_form #model_nm").combobox("setValue", "");
			$("#detail_form #sub_nm").combobox("setValue", "");
			
			$("#detail_form #lib_nm").combobox('setValue',row.lib_nm);
			console.log("lib_nm ==>"+$("#detail_form #lib_nm").combobox('getValue'));

			$("#detail_form #lib_nm").combobox('readonly',true);
			$("#detail_form #model_nm").combobox('readonly',true);
			$("#detail_form #sub_nm").combobox('readonly',true);
			$("#detail_form #ent_type_cd").textbox('readonly',true);
			
			$("#detail_form #ent_type_cd").textbox('setValue',row.ent_type_cd);
			$("#detail_form #ref_ent_type_nm").textbox('setValue',row.ref_ent_type_nm);
			$("#detail_form #ent_type_nm").textbox('setValue',row.ent_type_nm);
			$("#detail_form #tbl_type_nm").textbox('setValue',row.tbl_type_nm);
			$("#detail_form #tbl_type_cd").textbox('setValue',row.tbl_type_cd);
			$("#detail_form #ent_type_desc").val(row.ent_type_desc);
			
			$("#crud_flag").val("U");

			$("#detail_form #project_nm").textbox('readonly',true);
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function formValidationCheck(){
	return true;
}

function Btn_OnClick(){
	console.log("Btn_OnClick");
	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	$("#detail_form #currentPage").val("1");
	$("#detail_form #pagePerCount").val("20");
	
	if(!formValidationCheck()){
		return;
	}
	fnSearch();
}

function fnSearch(){
	$("#tableList").datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallModelEntityTypeList();
}

function ajaxCallModelEntityTypeList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모델 엔터티 유형 관리 목록 조회"," ");
	ajaxCall("/ModelEntityTypeList", $("#submit_form"), "POST", callback_ModelEntityTypeList);
}

//callback 함수
var callback_ModelEntityTypeList = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

var saveTypeText = "";
function Btn_SaveSetting(){
	
	if($("#detail_form #lib_nm").combobox('getValue') == ""){
		parent.$.messager.alert('경고','라이브러리명을 선택해 주세요','warning');
		return false;
	}

	if($("#detail_form #ent_type_cd").textbox('getValue') == ""){
		parent.$.messager.alert('경고','엔터티유형을 입력해 주세요','warning');
		return false;
	}	
	
	/* modal progress open */
	if($("#detail_form #crud_flag").val() == "C"){
		saveTypeText = "등록";
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("모델 엔터티 유형을 등록합니다."," ");
	}else{
		saveTypeText = "수정";
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("모델 엔터티 유형을 수정합니다."," ");
	}
	ajaxCall("/ModelEntityType/Save", $("#detail_form"), "POST", callback_ModelEntityTypeMngSave);
}

function Btn_DeleteSetting(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모델 엔터티 유형을 삭제합니다."," ");
	ajaxCall("/ModelEntityType/Delete", $("#detail_form"), "POST", callback_ProjectWrkjobDelete);
}

//callback 함수
var callback_ProjectWrkjobInsert = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','모델 엔터티 유형을 등록하였습니다.','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','모델 엔터티 유형 관리 등록중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ModelEntityTypeMngSave = function(result) {
	console.log("result:",result);
	if(result.result){
		parent.$.messager.alert('','모델 엔터티 유형을 '+saveTypeText+'하였습니다.','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','모델 엔터티 유형 관리 수정중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}
	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ProjectWrkjobDelete = function(result) {
	if(result.result){
		parent.$.messager.alert('','모델 엔터티 유형을 삭제하였습니다.','info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				fnSearch();
				},1000);
		});
	}else{
//		parent.$.messager.alert('','모델 엔터티 유형 관리 삭제중 오류가 발생하였습니다.');
		parent.$.messager.alert('',result.message);
	}	
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_ResetField(){
	console.log("Btn_ResetField start");
	
	$("#detail_form #crud_flag").val("C");
	
	$("#detail_form #model_nm").combobox("loadData","[]");
	$("#detail_form #sub_nm").combobox("loadData","[]");

	$("#detail_form #lib_nm").combobox("setValue", "");
	$("#detail_form #model_nm").combobox("setValue", "");
	$("#detail_form #sub_nm").combobox("setValue", "");
	
	$("#detail_form #model_nm").combobox("setText","선택");
	$("#detail_form #sub_nm").combobox("setText","선택");	    	
	
	$("#detail_form #lib_nm").combobox('readonly',false);
	$("#detail_form #model_nm").combobox('readonly',false);
	$("#detail_form #sub_nm").combobox('readonly',false);
	$("#detail_form #ent_type_cd").textbox('readonly',false);
	
	$("#detail_form #ent_type_cd").textbox("setValue", "");
	$("#detail_form #ref_ent_type_nm").textbox("setValue", "");
	$("#detail_form #ent_type_nm").textbox("setValue", "");
	$("#detail_form #tbl_type_nm").textbox("setValue", "");
	$("#detail_form #tbl_type_cd").textbox("setValue", "");
	$("#detail_form #ent_type_desc").val("");
	
	$("#detail_form #check_target_seq").val("");
	$("#detail_form #project_check_target_type_cd").val("");

	console.log("Btn_ResetField end");
}

function Excel_Download() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}	
	$("#submit_form").attr("action","/ModelEntityType/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}