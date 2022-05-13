$(document).ready(function() {

	
	$('#snapShotPop').window({
		title : "수집 SQL 조건절 파싱",
		top:getWindowTop(570),
		left:getWindowLeft(950)
	});

	$('#strStartTime').timespinner({
	    required: true,
	    showSeconds: true
	});
	$('#strEndTime').timespinner({
		required: true,
		showSeconds: true
	});
	
	/*$("#snapSList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
//			$("#snap_s_no").val(row.snap_no);
			getSnapEndNo();
		},		    		
		columns:[[
			{field:'snap_no',title:'SNAP_NO',width:"35%",halign:"center",align:"center",sortable:"true"},
			{field:'snap_dt',title:'SNAP_DT',width:"35%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	    	

	$("#snapEList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#snap_e_no").val(row.snap_no);
		},	    		
		columns:[[
			{field:'snap_no',title:'SNAP_NO',width:"35%",halign:"center",align:"center",sortable:"true"},
			{field:'snap_dt',title:'SNAP_DT',width:"35%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});*/
});
/*
function searchStartSnapNoList(){
	$('#snapSList').datagrid('loadData',[]);

	ajaxCall("/ParsingCollectionTerms/Popup/SnapShot",
		$("#snapShot_form"),
		"POST",
		callback_StartSnapNoListAddTable);
	
	 modal progress open 
//	openMessageProgress('SNAP 번호 조회','시작 스냅샷 번호를 조회중입니다.');
	if(parent.openMessageProgress != undefined){
		parent.openMessageProgress('SNAP 번호 조회','시작 스냅샷 번호를 조회중입니다.');
	}

}
*/
//callback 함수
/*var callback_StartSnapNoListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#snapSList').datagrid("loadData", data);

	 modal progress close 
	if(parent.closeMessageProgress != undefined){
		parent.closeMessageProgress();
	}
	
};*/

/*function getSnapEndNo(){
	$('#snapEList').datagrid('loadData',[]);
	$('#snapEList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#snapEList').datagrid('loading');  
	
	ajaxCall("/ParsingCollectionTerms/Popup/SnapShot",
		$("#snapShot_form"),
		"POST",
		callback_SnapEndListAddTable);			
}

//callback 함수
var callback_SnapEndListAddTable = function(result) {
	var data = JSON.parse(result);
	$('#snapEList').datagrid("loadData", data);
	$('#snapEList').datagrid('loaded');  
};*/		

function insertParsingCollectionTerms(){
	/*if($('#snapShot_form #snap_s_no').val() == ""){
		$.messager.alert('','시작 스냅샷 번호를 선택해 주세요.');
		return false;
	}
	
	if($('#snapShot_form #snap_e_no').val() == ""){
		$.messager.alert('','종료 스냅샷 번호를 선택해 주세요.');
		return false;
	}*/
	if(compareAnBDatatime( $("#snapShot_form #strStartDt").datebox('getValue'), $("#snapShot_form #strStartTime").timespinner('getValue'), 
			$("#snapShot_form #strEndDt").datebox('getValue'), $("#snapShot_form #strEndTime").timespinner('getValue') ) <= 0) {
		var msg = "수집일시 시간을 확인해 주세요.";
		parent.$.messager.alert('오류',msg,'error');
		return false;
	}
	
	ajaxCall("/ParsingCollectionTerms/Insert",
			$("#snapShot_form"),
			"POST",
			callback_parsingCollectionInsertAction);
	
	beforeParsingCollectionTerms();
}
var callback_parsingCollectionInsertAction = function(result) {

	if(result.result == false){
		parent.$.messager.alert('오류',result.message,'error');
	}
};

function beforeParsingCollectionTerms(){
	var w = parent.$.messager.alert('','수집SQL ACCESSPATH 파싱은 수행시간이</br>오래걸려 서버에서 실행됩니다.</br>수행 결과는 그리드에서 확인하세요.','info',function(){
//		$("#snapShot_form #snap_s_no").val("");
//		$("#snapShot_form #snap_e_no").val("");
		
		Btn_OnClosePopup('snapShotPop');
//		eval("if_"+frameName).Btn_OnClick();
		Btn_OnClick();	
	});
	
	w.window('resize', {width:330}).window('center');
}