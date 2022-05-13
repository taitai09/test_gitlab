$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// 인스턴스 조회			
	    	$('#instance_number').combobox({
	    	    url:"/Common/getMasterInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}					
				}
	    	});
	    }
	});	
	
	$('#chkGlobalViewYn').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#global_view_yn").val("Y");
				$("#instance_number").combobox("readonly",true);
			}else{
				$("#global_view_yn").val("N");
				$("#instance_number").combobox("readonly",false);
			}
		}
	})
	
	$("#collectionList").datagrid({
		view: myview,
		rownumbers:true,
		singleSelect:false,
		columns:[[
			{field:'username',title:'대상 스키마',width:"100%",halign:"center",align:"center",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#applyList").datagrid({
		view: myview,
		rownumbers: true,
		singleSelect:false,
		columns:[[
			{field:'parsing_username',title:'적용 스키마',width:"100%",halign:"center",align:"center",sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	Btn_ResetField();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));

	/* 수집대상 스키마 리스트 */
	$('#collectionList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수집 SQL 조건설정"," "); 
	
	ajaxCall("/SetCollectionCondition/CollectionTarget",
			$("#submit_form"),
			"POST",
			callback_CollectionTargetAddTable);
	
	/* 적용대상 스키마 리스트 */
	$('#applyList').datagrid('loadData',[]);

	ajaxCall("/SetCollectionCondition/ApplyTarget",
			$("#submit_form"),
			"POST",
			callback_ApplyTargetAddTable);
	
	/* GV$ 정보 조회 */
	ajaxCall("/SetCollectionCondition/GlobalViewInfo",
			$("#submit_form"),
			"POST",
			callback_GlobalViewInfoAction);
	
	/* 수집모듈 리스트 조회 */
	ajaxCall("/SetCollectionCondition/CollectionModule",
			$("#submit_form"),
			"POST",
			callback_CollectionModuleAction);	
}

//callback 함수
var callback_CollectionTargetAddTable = function(result) {
	var data = JSON.parse(result);
	$('#collectionList').datagrid("loadData", data);
	$('#collectionList').datagrid('loaded');	
};

//callback 함수
var callback_ApplyTargetAddTable = function(result) {
	var data = JSON.parse(result);
	$('#applyList').datagrid("loadData", data);
	$('#applyList').datagrid('loaded');	
};

//callback 함수
var callback_GlobalViewInfoAction = function(result) {
	var globalViewYn = "N";
	var chkGlobalViewYn = false;
	
	if(result.result){
		var post = result.object;

		if(post.global_view_yn != null || post.global_view_yn != ""){
			globalViewYn = post.global_view_yn;
			
			if(globalViewYn == "Y"){
				chkGlobalViewYn = true;
			}else{
				chkGlobalViewYn = false;
			}
		}
		$("#global_view_yn").val(globalViewYn);
		
		$('#chkGlobalViewYn').switchbutton({checked: chkGlobalViewYn});
		$("#instance_number").combobox("setValue",post.instance_number);		
	}
};

//callback 함수
var callback_CollectionModuleAction = function(result) {
	var strHtml = "";
	
	if(result.result){
		$("#moduleTable tbody tr").remove();
		
		$("#modCnt").val(result.object.length);
		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			strHtml += "<tr><th>모듈 "+(i+1)+"</th>";
			if(i == 0){
				strHtml += "<td style='padding-left:10px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LIKE&nbsp;&nbsp;&nbsp;<input type='text' id='module_name1' name='module_name' value='"+post.module_name+"' class='w150 easyui-textbox'/>&nbsp;&nbsp;%</td>";
			}else{
				strHtml += "<td style='padding-left:10px;'>&nbsp;&nbsp;&nbsp;OR LIKE&nbsp;&nbsp;&nbsp;<input type='text' id='module_name"+(i+1)+"' name='module_name' value='"+post.module_name+"' class='w150 modCls easyui-textbox'/>&nbsp;&nbsp;%</td></tr>";	
			}
		}
		
		$("#moduleTable tbody").append(strHtml);
		
		for(var i = 0 ; i < result.object.length ; i++){
			$("#module_name"+(i+1)).textbox();
		}
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};

function Btn_SaveSetCollectionCondition(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택후 검색을 해주세요.');
		return false;
	}
	
	rows = $('#applyList').datagrid('getRows');

	if(rows.length < 1){
		parent.$.messager.alert('','최소 한개의 적용 대상 스키마가 있어야 합니다.');
		return false;
	}
	
	var parsingSchemaArry = "";
	var moduleNameArry = "";
	
	for(var i = 0 ; i < rows.length ; i++){
		parsingSchemaArry += rows[i].parsing_username + "|";
	}
	
	$("#parsingSchemaArry").val(strRight(parsingSchemaArry,1));
	
	if($("#global_view_yn").val() == "N" && $("#instance_number").combobox('getValue') == ""){
		parent.$.messager.alert('','인스턴스를 선택해 주세요.');
		return false;
	}
	
	for(var i = 1 ; i <= $("#modCnt").val() ; i++){
		
		for(var j = (i+1) ; j <= $("#modCnt").val() ; j++){
			
			if($("#module_name"+i).textbox("getValue") == $("#module_name"+j).textbox("getValue")){
				parent.$.messager.alert('','수집 모듈명이 중복되었습니다.<br/>다시 확인해 주세요.');
				return false;
			}
		}
	}
	
	var modErrCnt = 0;
	
	for(var i = 1 ; i <= $("#modCnt").val() ; i++){
		if($("#module_name"+i).textbox("getValue") == ""){
			modErrCnt++;
		}
		
		moduleNameArry += $("#module_name"+i).textbox("getValue") + "|";
	}
	
	if(modErrCnt > 0){
		parent.$.messager.alert('','수집 모듈명을 정확히 입력해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));	
	$("#moduleNameArry").val(strRight(moduleNameArry,1));

	ajaxCall("/SetCollectionCondition/Save",
			$("#detail_form"),
			"POST",
			callback_SaveSetCollectionConditionAction);			
}

//callback 함수
var callback_SaveSetCollectionConditionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','자동선정조건 정보 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}
};

function Btn_AddApplyTarget(){
	rows = $('#collectionList').datagrid('getSelections');
	
	if(rows.length < 1){
		parent.$.messager.alert('','대상 스키마를 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){
		$('#applyList').datagrid('appendRow',{
			parsing_username : rows[i].username
		});
		
		$('#collectionList').datagrid('deleteRow', $('#collectionList').datagrid('getRowIndex', rows[i]));
	}
}

function Btn_RemoveApplyTarget(){
	rows = $('#applyList').datagrid('getSelections');
	
	if(rows.length < 1){
		parent.$.messager.alert('','적용 제외 스키마를 먼저 선택해 주세요.');
		return false;
	}
	
	for(var i = 0 ; i < rows.length; i++){
		$('#collectionList').datagrid('appendRow',{
			username : rows[i].parsing_username
		});
		
		$('#applyList').datagrid('deleteRow', $('#applyList').datagrid('getRowIndex', rows[i]));
	}
}

function Btn_addModule(){
	var strHtml = "";
	var totalRow = $("#moduleTable > tbody > tr").length;
	
	$("#modCnt").val(totalRow+1);
	
	strHtml += "<tr><th>모듈 "+(totalRow+1)+"<input type='hidden' id='modCnt' name='modCnt' value='"+(totalRow+1)+"'/></th>";
	strHtml += "<td style='padding-left:10px;'>&nbsp;&nbsp;&nbsp;OR LIKE&nbsp;&nbsp;&nbsp;<input type='text' id='module_name"+(totalRow+1)+"' name='module_name' class='w150 easyui-textbox'/>&nbsp;&nbsp;%</td></tr>";

	$("#moduleTable > tbody:last").append(strHtml);
	
	$("#module_name"+(totalRow+1)).textbox();	
}

function Btn_removeModule(){
	var totalRow = $("#moduleTable > tbody > tr").length;
	
	if(totalRow == 1){
		parent.$.messager.alert('','최소 한개의 모듈은 존재해야 합니다.');
	}else{
		$("#modCnt").val(totalRow-1);
		$("#moduleTable > tbody:last > tr:last").remove();
	}
}

function removeAllModule(){
	var totalRow = $("#moduleTable > tbody > tr").length;
	$("#modCnt").val(1);
	for(var i = totalRow ; i > 1 ; i--){
		$("#moduleTable > tbody:last > tr:last").remove();
	}
}

function Btn_ResetField(){
	$('#chkGlobalViewYn').switchbutton({checked: true});
	$("#instance_number").combobox("setValue", "");
	$("#instance_number").combobox("readonly", true);
	
	removeAllModule();
}