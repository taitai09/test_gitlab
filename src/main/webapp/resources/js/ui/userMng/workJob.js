$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	createUpperWrkjobCdCombotree();		// 업무 리스트 조회	
	loadDbList();
	createChckBox();
	
	// 성능점검임계치관리유형코드 조회
	$('#perf_check_threshold_type_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1023",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','성능점검임계치관리유형코드 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$("#tableList").treegrid({
		idField:'id',
		treeField:'text',
		rownumbers: true,
		lines: true,
		onClickCell : function(index,row) {
			setWorkJobCd(row);
		},
		columns:[[
			{field:'id',hidden:"true"},
			{field:'parent_id',hidden:"true"},
			{field:'text',title:'업무명',halign:"center",align:'left'},
			{field:'wrkjob_div_cd',title:'업무코드',width:"15%",halign:"center",align:"center"},
			{field:'deploy_check_target_yn',title:'배포성능점검대상여부',width:"15%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"10%",halign:"center",align:'center'},
			{field:'db1_name',title:'DB1', width:"10%", halign:"center", align:'center'},
			{field:'db2_name',title:'DB2', width:"10%", halign:"center", align:'center'},
			{field:'db3_name',title:'DB3', width:"10%", halign:"center", align:'center'},
			
			{field:'perf_check_threshold_type_cd',hidden:"true"},
			{field:'wrkjob_cd_nm',hidden:true}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#wrkUserList").datagrid({
		view: myview,
		singleSelect : true,
		onClickRow : function(index,row) {
			setUserList(row);
		},
		columns:[[
			{field:'user_id',title:'사용자ID',width:"14%",halign:"center",align:"center",sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"14%",halign:"center",align:'center'},
			{field:'ext_no',title:'내선번호',width:"20%",halign:"center",align:'center'},
			{field:'hp_no',title:'휴대폰번호',width:"20%",halign:"center",align:'center'},
			{field:'wrkjob_cd',hidden:"true"},
			{field:'workjob_start_day',hidden:"true"},
			{field:'use_yn',title:'사용여부',width:"10%",halign:"center",align:'center'},
			{field:'leader_yn',title:'업무리더 여부',width:"17%",halign:"center",align:'center'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});	
	
	$("#user_detail_form #user_id_temp").textbox({disabled : true});
	$("#user_detail_form #user_nm").textbox({disabled : true});
	$("#user_detail_form #ext_no").textbox({disabled : true});
	$("#user_detail_form #hp_no").textbox({disabled : true});
	$("#user_detail_form #use_yn").textbox({disabled : true});
	
	Btn_SearchWorkJob();
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		Btn_ResetField_Bottom();
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	
	$("#nextBtnEnabled").click(function(){
		Btn_ResetField_Bottom();
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	$('#tipInfo').tooltip({
		position: 'top',
		content: 'This is the tooltip message.'
	});
});

function loadDbList(){
	ajaxCall("/Common/getDatabase?isAll=N",
			null,
			"GET",
			callback_loadDbList);
}

var callback_loadDbList = function(result){
	try{
		if( result ){
			let data = JSON.parse(result);
			data[0].db_name = "없음";
			
			createDbCombo(data);
		}
		
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
}

function createDbCombo( dbList ){
	let comboboxId;
	
	for(let idx = 1; idx <= 3; idx++){
		comboboxId = '#db'+idx;
		$(comboboxId).combobox({
			valueField:'dbid',
			textField:'db_name',
		});
		
		$(comboboxId).combobox('loadData', dbList);
	}
}

function createChckBox(){
	$('#sub_apply').checkbox({
		value: true,
		checked: false,
	});
}

function createUpperWrkjobCdCombotree(){
	$('#upper_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?isChoice=N",
		method:'get',
		onLoadSuccess: function() {
			$('#detail_form #deploy_check_target_yn').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #use_yn').combobox('textbox').attr("placeholder","선택");
		}
	});
}

function Btn_SearchWorkJob(){
	$('#tableList').treegrid('loadData',[]);

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("업무 관리"," "); 
	
	ajaxCall("/WorkJobTree",
			null,
			"GET",
			callback_WorkJobTreeAction);
}

//callback 함수
var callback_WorkJobTreeAction = function(result) {
	try{
		var data = JSON.parse(result);
		
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			$('#tableList').treegrid("loadData", data);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Btn_SaveWorkJob(){
	if($("#wrkjob_div_cd").textbox('getValue') == ""){
		parent.$.messager.alert('','업무코드를 입력해 주세요.');
		return false;
	}
	if($("#wrkjob_div_cd").textbox('getValue').length > 20){
		parent.$.messager.alert('','업무코드를 20자 이내로 입력해 주세요.');
		return false;
	}
	
	if($("#wrkjob_cd_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','업무명을 입력해 주세요.');
		return false;
	}
	if($("#wrkjob_cd_nm").textbox('getValue').length > 50){
		parent.$.messager.alert('','업무명을 50자 이내로 입력해 주세요.');
		return false;
	}
	
	if($("#deploy_check_target_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','배포성능점검대상여부를 선택해 주세요.');
		return false;
	}
	if($("#use_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','사용여부를 선택해 주세요.');
		return false;
	}
	
	setDataBaseInfo();
	
	if($("#upper_wrkjob_cd").combobox('getValue') == ""){
		parent.$.messager.confirm('','최상위 업무로 변경 하시겠습니까?',function(check){
			if(check){
				ajaxCall_SaveWorkJobAction();
			}
		});
		
	}else{
		ajaxCall_SaveWorkJobAction();
	}
	
	function setDataBaseInfo(){
		let wrkjob_cd = $('#wrkjob_cd').val();
		let wrkjobCdArr = [ wrkjob_cd ];
		let childrenArr;
		
		let subApply = $('#sub_apply').checkbox('options').checked;
		
		if( subApply ){
			childrenArr = $("#tableList").treegrid('getChildren', wrkjob_cd);	// 하위업무
			
			if(childrenArr){
				for(let i = 0; i < childrenArr.length; i++){
					wrkjobCdArr.push(childrenArr[i].id);
				}
			}
		}
		$('#wrkjob_cd_target').val(wrkjobCdArr)
	}
}

function ajaxCall_SaveWorkJobAction(){
	ajaxCall("/WorkJob/Save",
			$("#detail_form"),
			"POST",
			callback_SaveWorkJobAction);
}

//callback 함수
var callback_SaveWorkJobAction = function(result) {
	let msg;
	if($("#detail_form #crud_flag").val() == 'C'){
		msg = '저장 되었습니다.';
	}else{
		msg = '수정 되었습니다.';
	}
	
	if(result.result){
		parent.$.messager.alert('',msg,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_SearchWorkJob();
				createUpperWrkjobCdCombotree();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_SaveWorkJobLeader1(){
	
	var row = $('#wrkUserList').datagrid('getSelected');
	
	if(row == null || row.user_id == ""){
		parent.$.messager.alert('','리더로 설정할 사용자를 선택해 주세요.');
		return false;
	}

	$("#workjob_form #wrkjob_cd").val(row.wrkjob_cd);
	$("#workjob_form #user_id").val(row.user_id);
	$("#workjob_form #workjob_start_day").val(row.workjob_start_day);

	ajaxCall("/WorkJobLeader/Save",
			$("#workjob_form"),
			"POST",
			callback_SaveWorkJobLeaderAction);
}

function Btn_SaveWorkJobLeader(){
	
	var row = $('#wrkUserList').datagrid('getSelected');
	
	var msg = '업무별 사용자 내역에서 사용자를 선택한 후\n업무리더 여부를 변경할 수 있습니다.';
	if(row == null || row.user_id == ""){
		parent.$.messager.alert('',msg);
		return false;
	}

	$("#user_detail_form #wrkjob_cd").val(row.wrkjob_cd);
	$("#user_detail_form #user_id").val(row.user_id);
	$("#user_detail_form #workjob_start_day").val(row.workjob_start_day);
	
	ajaxCall("/WorkJobLeader/Save",
			$("#user_detail_form"),
			"POST",
			callback_SaveWorkJobLeaderAction);
}

//callback 함수
var callback_SaveWorkJobLeaderAction = function(result) {
	var msg = "수정 되었습니다.";
	if(result.result){
		parent.$.messager.alert('',msg,'info',function(){
			setTimeout(function() {			
				Btn_OnClick();
				//업무리더 여부 변경 초기화
				initWorkjobLeaderChangeForm();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setWorkJobCd(selRow){
	
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #wrkjob_cd").val(selRow.id);	
	$("#detail_form #old_wrkjob_div_cd").val(selRow.wrkjob_div_cd);	
	$("#detail_form #wrkjob_div_cd").textbox("setValue", selRow.wrkjob_div_cd);
	
	if(selRow.parent_id != "-1"){
		$("#detail_form #upper_wrkjob_cd").combotree("setValue", selRow.parent_id);
	}else{
		$("#detail_form #upper_wrkjob_cd").combotree("setValue", "");
	}
	
	$("#detail_form #wrkjob_cd_nm").textbox("setValue", selRow.wrkjob_cd_nm);
	$("#detail_form #use_yn").combobox("setValue", selRow.use_yn);
	$("#detail_form #deploy_check_target_yn").combobox("setValue", selRow.deploy_check_target_yn);
	
	$('#db1').combobox('setValue', selRow.db1_id);
	$('#db2').combobox('setValue', selRow.db2_id);
	$('#db3').combobox('setValue', selRow.db3_id);
	$('#sub_apply').checkbox('uncheck');
	
	Btn_OnClick();
	initWorkjobLeaderChangeForm();
}

function setUserList(selRow){
	$("#user_detail_form #user_id").val(selRow.user_id);
	$("#user_detail_form #user_id_temp").textbox("setValue", selRow.user_id);	
	$("#user_detail_form #user_nm").textbox("setValue", selRow.user_nm);
	$("#user_detail_form #ext_no").textbox("setValue", selRow.ext_no);
	$("#user_detail_form #hp_no").textbox("setValue", selRow.hp_no);
	$("#user_detail_form #use_yn").textbox("setValue", selRow.use_yn);
	$("#user_detail_form #leader_yn").combobox("setValue", selRow.leader_yn);
	$("#user_detail_form #wrkjob_cd").val(selRow.wrkjob_cd);	
	$("#user_detail_form #workjob_start_day").val(selRow.workjob_start_day);	
}

//업무리더 여부 변경 초기화
function initWorkjobLeaderChangeForm(){
	$("#user_detail_form #user_id_temp").textbox("setValue", "");	
	$("#user_detail_form #user_nm").textbox("setValue", "");
	$("#user_detail_form #ext_no").textbox("setValue", "");
	$("#user_detail_form #hp_no").textbox("setValue", "");
	$("#user_detail_form #use_yn").textbox("setValue", "");
	$("#user_detail_form #leader_yn").combobox("setValue", "");
	$("#user_detail_form #wrkjob_cd").val("");	
	$("#user_detail_form #workjob_start_day").val("");	
}
function Btn_ResetField_Bottom(){
	$("#wrkUserList").datagrid('clearSelections');
	$("#user_detail_form #user_id_temp").textbox("setValue", "");	
	$("#user_detail_form #user_nm").textbox("setValue", "");
	$("#user_detail_form #ext_no").textbox("setValue", "");
	$("#user_detail_form #hp_no").textbox("setValue", "");
	$("#user_detail_form #use_yn").textbox("setValue", "");
	$("#user_detail_form #leader_yn").combobox("setValue", "");
	$("#user_detail_form #wrkjob_cd").val("");	
	$("#user_detail_form #workjob_start_day").val("");	

}
function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	$('#wrkUserList').datagrid('loadData',[]);
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #wrkjob_cd").val("");
	$("#detail_form #old_wrkjob_div_cd").val("");	
	$("#detail_form #wrkjob_div_cd").textbox("setValue", "");
	$("#detail_form #upper_wrkjob_cd").combotree("setValue", "");
	$("#detail_form #wrkjob_cd_nm").textbox("setValue", "");
	$("#detail_form #deploy_check_target_yn").combobox("setValue", "");
	
	let targetId;
	for(let idx = 1; idx <= 3; idx++){
		targetId = '#db'+idx;
		
		$(targetId).combobox('setValue', '');
		$(targetId+'_json').val('');
	}
	$('#sub_apply').checkbox('uncheck');
	$('#wrkjob_cd_target').val('');
	
	$("#user_detail_form #user_id_temp").textbox("setValue", "");	
	$("#user_detail_form #user_nm").textbox("setValue", "");
	$("#user_detail_form #ext_no").textbox("setValue", "");
	$("#user_detail_form #hp_no").textbox("setValue", "");
	$("#user_detail_form #use_yn").textbox("setValue", "");
	$("#user_detail_form #leader_yn").combobox("setValue", "");
	$("#user_detail_form #wrkjob_cd").val("");	
	$("#user_detail_form #workjob_start_day").val("");	
	
	$("#prevBtnDisabled").show();
	$("#prevBtnEnabled").hide();
	$("#nextBtnDisabled").show();
	$("#nextBtnEnabled").hide();
}

/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		console.log("currentPage2 : "+currentPage);
		
		$("#currentPage").val(currentPage);
	}else{
		$("#currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#currentPage").val("1");
		return;
	}
	Btn_OnClick('P');
}

function Btn_OnClick(val){
	if(!formValidationCheck()){  //현재 없음.
		return;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#currentPage").val('1');
		$("#pagePerCount").val('10');
	}
	fnSearch();
}

function fnSearch(){
	$('#wrkUserList').datagrid('loadData',[]);
	$('#wrkUserList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#wrkUserList').datagrid('loading'); 
	
	ajaxCall("/WorkJobUsers",
			$("#detail_form"),
			"POST",
			callback_WorkJobUsersAction);
}
var callback_WorkJobUsersAction = function(result) {
	json_string_callback_common(result,'#wrkUserList',false);
	fnControlPaging(result);
};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#currentPage").val();
		currentPage = parseInt(currentPage);
	var pagePerCount = $("#pagePerCount").val();
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
	if($("#detail_form #wrkjob_cd").val() == ""){
		parent.$.messager.alert('','업무를 선택해주세요.');
		return false;
	}
	
	return true;
}
/*페이징처리끝*/


function Excel_Download(){
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}

	$("#detail_form").attr("action","/WorkJobUsers/ExcelDown");
	$("#detail_form").submit();
}