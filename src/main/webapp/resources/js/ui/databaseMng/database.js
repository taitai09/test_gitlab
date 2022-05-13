var pageId = "Database";

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#exadata_yn').combobox('textbox').attr("placeholder","선택");
	
	// DB운영유형 조회
	$('#db_operate_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1019",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess:function() {
			$('#db_operate_type_cd').combobox('textbox').attr("placeholder","선택");
			$('#use_yn').combobox('textbox').attr("placeholder","선택");
		}
	});	
	
	// DBMS 종류 조회
	$('#database_kinds_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1095",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess:function() {
			$('#database_kinds_cd').combobox('textbox').attr("placeholder","선택");
		}
	});	
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'dbid',title:'DBID',width:"8%",halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB명',width:"8%",halign:"center",align:'center'},
			{field:'db_abbr_nm',title:'DB 한글명',width:"10%",halign:"center",align:'left'},
			{field:'database_kinds_cd',title:'DBMS 종류',width:"6%",halign:"center",align:'center'},
			{field:'exadata_yn',title:'Exadata여부',width:"6%",halign:"center",align:'center'},
			{field:'db_operate_type_nm',title:'운영유형',width:"6%",halign:"center",align:'center'},
			{field:'characterset',title:'CHARACTERSET',width:"10%",halign:"center",align:'left'},
			{field:'rgb_color_value',title:'컬러',width:"10%",halign:"center",align:'center',styler:cellStyler},
			{field:'collect_inst_id',title:'컬렉터인스턴스',width:"6%",halign:"center",align:'center'},
			{field:'gather_inst_id',title:'수집인스턴스',width:"6%",halign:"center",align:'center'},
			{field:'ordering',title:'정렬순서',width:"6%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"6%",halign:"center",align:'center'},
			{field:'rgb_color_id',hidden:"true"},
			{field:'db_operate_type_cd',hidden:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
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
	
//	var clr = new Array('00','20','30','40','50','60','70','80','a0','c0','ff');
//	
//	for(var i = 0; i < 8; i++) {
//		document.write('<'+'table width=500px border=1 cellpadding=8' + '>');
//		
//		for(var j = 0; j < 8; j++) {
//			document.write('<' + 'tr' + '>');
//			
//			for(var k = 0; k < 8; k++) {
//				document.write('<td style="background-color:#' + clr[i] + clr[j]+ clr[k] + '">');
//				document.write('<tt>#');
//				document.write(clr[i] + clr[j] + clr[k] + ' </tt></td' + '>');
//			}
//			
//			document.write("</tr" + ">");
//		}
//		
//		document.write("</table" + "><br" + "/>");
//	}
	
	$('#colorDiv').textbox('readonly');
	$('#searchValue').textbox({readonly:true});
	
	$('#searchKey').combobox({
		onChange : function(newValue,oldValue){
			if(newValue == "") {
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else{
				$('#searchValue').textbox({readonly:false});
			}
		}
	});
});

/*function Btn_OnClick(){
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터베이스 관리"," "); 
	
	ajaxCall("/Database",
			$("#submit_form"),
			"POST",
			callback_DatabaseAction);		
}*/
//callback 함수
//var callback_DatabaseAction = function(result) {
//	Btn_ResetField();
	
//	json_string_callback_common(result,'#tableList',true);
//	var data = JSON.parse(result);
//	$('#tableList').datagrid("loadData", data);
//
//	/* modal progress close */
//	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
//};

function cellStyler(value, row, index) {
	if(!value == "") {
		return 'background-color:' + value + ';color:#fff;text-shadow:-1px -1px #000, 1px -1px #000, -1px 1px #000, 1px 1px #000';
	}
}

function Btn_SaveSetting(){
	if($("#ui_dbid").textbox('getValue') == ""){
		parent.$.messager.alert('','DBID를 입력해 주세요.');
		return false;
	}
	
	if($("#db_name").textbox('getValue') == ""){
		parent.$.messager.alert('','DB명을 입력해 주세요.');
		return false;
	}
	
	let byteLength = 0;
	// byte길이
	byteLength = (function(s,b,i,c){
		for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?2:c>>7?2:1);
		return b
	})( $("#db_name").textbox('getValue') );
	
	if( byteLength > 9){
		parent.$.messager.alert('','DB명을 9자 이내로 입력해 주세요.');
		return false;
	}
	
	if($("#db_abbr_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','DB 한글명을 입력해 주세요.');
		return false;
	}
	if($("#exadata_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','엑사데이터여부를 선택해 주세요.');
		return false;
	}
	if($("#db_abbr_nm").textbox('getValue').length > 30){
		parent.$.messager.alert('','DB 한글명을 30자 이내로 입력해 주세요.');
		return false;
	}
	if($("#characterset").textbox('getValue').length > 64){
		parent.$.messager.alert('','CHARACTERSET을 64자 이내로 입력해 주세요.');
		return false;
	}
	
	if($("#db_operate_type_cd").combobox('getValue') == ""){
		parent.$.messager.alert('','DB운영유형을 선택해 주세요.');
		return false;
	}
	
	if($('#collect_inst_id').textbox('getValue') == ""){
		parent.$.messager.alert('','컬렉터인스턴스를 입력해 주세요.');
		return false;
	}
	if($('#collect_inst_id').textbox('getValue').length > 100){
		parent.$.messager.alert('','컬렉터인스턴스를 100자 이내로 입력해 주세요.');
		return false;
	}
	
	if($('#gather_inst_id').textbox('getValue') == ""){
		parent.$.messager.alert('','수집인스턴스를 입력해 주세요.');
		return false;
	}
	if($('#gather_inst_id').textbox('getValue').length > 100){
		parent.$.messager.alert('','수집인스턴스를 100자 이내로 입력해 주세요.');
		return false;
	}
	
	if($('#ordering').textbox('getValue') == ""){
		parent.$.messager.alert('','정렬 순서를 입력해 주세요.');
		return false;
	}

	if($('#ordering').textbox('getValue').length > 5){
		parent.$.messager.alert('','정렬 순서를 10만 이하로 입력해 주세요.');
		return false;
	}
	
	if($("#use_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','사용여부를 선택해 주세요.');
		return false;
	}
	
	if($("#colorDiv").textbox('getValue') == ""){
		parent.$.messager.alert('','RGB 컬러를 선택해 주세요.');
		return false;
	}
	
	if($("#database_kinds_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','DBMS 종류를 선택해 주세요.');
		return false;
	}
	
	if(!validationNumberType()) return false;
	
	$("#detail_form #rgb_color_id").val($("#submit_form #rgb_color_id").val());
	$("#detail_form #dbid").val($("#ui_dbid").textbox("getValue"));
	
	if($('#ui_dbid').prop('disabled')) {
		ajaxCall("/Database/Save",
				$("#detail_form"),
				"POST",
				callback_SaveDatabaseAction);
	} else {
		ajaxCall("/Database/notExistDbid",
				$("#detail_form"),
				"POST",
				callback_NotExistDbid);
	}
}

var callback_NotExistDbid = function(result) {
	if(result.result){
		console.log(result);
		if(Number(result.object[0].cnt) > 0) {
			parent.$.messager.alert('', "동일한 DBID 가 존재합니다.");
			return;
		}
		
		ajaxCall("/Database/Save",
				$("#detail_form"),
				"POST",
				callback_SaveDatabaseAction);
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function validationNumberType() {
	if(!$.isNumeric($('#ui_dbid').val())) {
		parent.$.messager.alert('입력형식 오류','DBID는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if(!$.isNumeric($('#collect_inst_id').val())) {
		parent.$.messager.alert('입력형식 오류','컬렉터인스턴스는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if(!$.isNumeric($('#gather_inst_id').val())) {
		parent.$.messager.alert('입력형식 오류','수집인스턴스는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if(!$.isNumeric($('#ordering').val())) {
		parent.$.messager.alert('입력형식 오류','정렬순서는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	return true;
}

//callback 함수
var callback_SaveDatabaseAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setDetailView(selRow){
	$("#old_db_name").val(selRow.db_name);
	$("#crud_flag").val("U");
	$("#ui_dbid").textbox("setValue", selRow.dbid);
	$("#ui_dbid").textbox({disabled: true});
	$("#dbid").val(selRow.dbid);
	$("#db_name").textbox("setValue", selRow.db_name);
	$("#db_abbr_nm").textbox("setValue", selRow.db_abbr_nm);
	$("#exadata_yn").combobox("setValue", selRow.exadata_yn);
	$("#ordering").textbox("setValue", selRow.ordering);
	$("#collect_inst_id").textbox("setValue", selRow.collect_inst_id);
	$("#gather_inst_id").textbox("setValue", selRow.gather_inst_id);
	$("#db_operate_type_cd").combobox("setValue", selRow.db_operate_type_cd);
	$("#use_yn").combobox("setValue", selRow.use_yn);
	$("#characterset").textbox("setValue", selRow.characterset);
	$('#database_kinds_cd').combobox('setValue', selRow.database_kinds_cd);
	var colorValue = selRow.rgb_color_value;
	
	if(colorValue == null || typeof(colorValue) == 'undefined'){
		$("#colorDiv").textbox('setValue','');
		$("#colorDiv").textbox('textbox').css("background-color", '#FFFFFF');
		return;
	}
	
	$("#rgb_color_id").val(selRow.rgb_color_id);
	$("#colorDiv").textbox('setValue',colorValue);
	$("#colorDiv").textbox('textbox').css("background-color", colorValue);
	
	setTextShadow($('#colorDiv'));
}

function setTextShadow(divId) {
	divId.textbox('textbox').css("color", '#FFF');
	divId.textbox('textbox').css("text-shadow", '-1px -1px #000, 1px -1px #000, -1px 1px #000, 1px 1px #000');
}

function Btn_ResetField(){
	$("#old_db_name").val("");
	$("#crud_flag").val("C");
	$("#ui_dbid").textbox("setValue", "");
	$("#ui_dbid").textbox({disabled: false});
	$("#dbid").val("");
	$("#db_name").textbox("setValue", "");
	$("#db_abbr_nm").textbox("setValue", "");
	$("#exadata_yn").combobox("setValue", "");
	$("#ordering").textbox("setValue", "");
	$("#collect_inst_id").textbox("setValue", "");
	$("#gather_inst_id").textbox("setValue", "");
	$("#db_operate_type_cd").combobox("setValue", "");
	$("#use_yn").combobox("setValue", "");
	$("#database_kinds_cd").combobox("setValue", "");
	$("#characterset").textbox("setValue", "");
	$("#colorDiv").textbox("setValue", "");
	$("#colorDiv").textbox("textbox").css("background-color", "#FFF");
	$("#rgb_color_id").val("");
	$("#tableList").datagrid('clearSelections');
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
		$("#submit_form #pagePerCount").val('10');
	}
	
	fnSearch();
}


function fnSearch(){

		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();

		$('#tableList').datagrid('loadData',[]);

		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터베이스 관리"," "); 
		
		ajaxCall("/Database",
				$("#submit_form"),
				"POST",
				callback_DatabaseAction);	
	
}

//callback 함수
var callback_DatabaseAction = function(result) {
	Btn_ResetField();
	
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

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
	
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
			($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
			parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
			return false;
		}
	
	return true;
}
/*페이징처리끝*/




function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}

	$("#submit_form").attr("action","/Database/ExcelDown");
	$("#submit_form").submit();
}

function update() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$('#colorPopup').window("open");
	ajaxCallGetRGBColor();
}
