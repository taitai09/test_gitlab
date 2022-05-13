$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#multiDeleteBtn").linkbutton({disabled:true});

	// 업무 리스트 조회	
	$('#detail_form #wrkjob_cd').combotree({
	    url:"/Common/getWrkJobCd",
	    method:'get',
	    valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
	    onChange: function(newValue, oldValue) {
	    	if(newValue != '') {
	    		$('#wrkjob_cd_ui').textbox("setValue", newValue);
	    	}
	    },
		onLoadError: function(){
			parent.$.messager.alert('','업무 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function() {
			$('#detail_form #wrkjob_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$('#searchKey').combobox({
		onChange: function(newValue, oldValue) {
			$('#searchValue').textbox('setValue', '');
			
			if(newValue == '') {
				$('#searchValue').textbox('readonly', true);
			} else {
				$('#searchValue').textbox('readonly', false);
			}
		},
		onLoadSuccess: function(items) {
			$('#searchValue').textbox('readonly', true);
		},
	});
	
	getApplicationCodeList();
	
	Btn_OnClick();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	$("#detail_form #reg_dt").textbox({readonly:true})
	$("#detail_form #mgr_id").textbox({readonly:true});
	$("#detail_form #mgr_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	$("#detail_form #reg_dt").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	
	
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

//changeList
function getApplicationCodeList(){
	
	$("#tableList").datagrid({
			view: myview,
			nowrap : true,
			checkOnSelect : false,
			selectOnCheck : false,
			onCheckAll : function(rows) {
				setCheckDetail();
			},
			onUncheckAll : function(rows) {
				setCheckDetail();
			},
			onCheck : function(index, row){
				setCheckDetail();
			},
			onUncheck : function(index, row){
				setCheckDetail();
			},
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
			    {field:'chk_user_id',checkbox:"true"},
				{field:'wrkjob_cd',title:'업무코드',width:"8%",halign:"center",align:"center",sortable:"true"},
				{field:'wrkjob_cd_nm',title:'업무명',width:"10%",halign:"center",align:'center'},
				{field:'tr_cd',title:'애플리케이션 코드',width:"20%",halign:"center",align:'left'},
				{field:'tr_cd_nm',title:'애플리케이션 코드이름',width:"20%",halign:"center",align:'left'},
				{field:'mgr_id',title:'담당자ID',width:"8%",halign:"center",align:'center'},
				{field:'reg_dt',title:'등록일시',width:"14%",halign:"center",align:'center'},
				{field:'elapsed_time_threshold',hidden:true},
				{field:'buffer_gets_threshold',hidden:true}
			]],
//	    	fitColumns:true,
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		
};
function setCheckDetail(){
	var rows = $('#tableList').datagrid('getChecked');
	if(rows.length >= 1){  //여러개 체크 됐을 시 승인버튼만 활성화
		$("#multiDeleteBtn").linkbutton({disabled:false});
	}else if(rows.length == 0){  //한개만 체크할시 저장버튼만 활성화
		$("#multiDeleteBtn").linkbutton({disabled:true});
	}
};

/*function Btn_OnClick(){
	$("#multiDeleteBtn").linkbutton({disabled:true});
	
	if(($('#searchKey').combobox('getValue') == "" && $("#searchKey").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchKey").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	
	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케쥴러 설정 기본"," ");
	
		ajaxCall("/ApplicationCode/List",
				$("#submit_form"),
				"POST",
				callback_ApplicationCodeList);
}

//callback 함수
var callback_ApplicationCodeList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getApplicationCodeList();
	 modal progress close 
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};
*/
function Btn_SaveSetting(){
		
		if($("#detail_form #wrkjob_cd").combotree('getValue') == ""){
			parent.$.messager.alert('','업무명을 선택해주세요.');
			return false;
		}
		if($("#detail_form #tr_cd").textbox('getValue') == ""){
			parent.$.messager.alert('','애플리케이션 코드를 입력해주세요.');
			return false;
		}
		
		if($("#detail_form #tr_cd_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','애플리케이션 코드이름을 입력해주세요.');
			return false;
		}
		
		if(byteLength($("#detail_form #tr_cd").textbox('getValue')) > 1000) {
			parent.$.messager.alert('','애플리케이션 코드 정보가 1000 Byte를 초과 하였습니다.');
			return false;
		}
		
		if(byteLength($("#detail_form #tr_cd_nm").textbox('getValue')) > 150) {
			parent.$.messager.alert('','애플리케이션 코드이름 정보가 150 Byte를 초과 하였습니다.');
			return false;
		}
		
		ajaxCall("/ApplicationCode/Save",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	
};

//callback 함수
var callback_SaveSettingAction = function(result) {
	var message = "저장 되었습니다.";
	
	if($("#detail_form #crud_flag").val() == 'U') {
		message = "수정 되었습니다.";
	}
	
	if(result.result){
		parent.$.messager.alert('',message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_MultiDeleteSetting(){

	parent.$.messager.confirm('', '선택된 체크박스 모두를 삭제 하시겠습니까?', function(check) {
		if (check) {

			var rows = $('#tableList').datagrid('getChecked');
			var chk_tr_cd = new Array();
			var chk_wrkjob_cd = new Array();

			for(var i=0; i<rows.length; i++){
				chk_tr_cd.push(rows[i].tr_cd);
				chk_wrkjob_cd.push(rows[i].wrkjob_cd);
			}
			
			$("#chk_tr_cd").val(chk_tr_cd.join(','));  
			$("#chk_wrkjob_cd").val(chk_wrkjob_cd.join(',')); 
			
			ajaxCall("/ApplicationCode/MultiDelete",
					$("#detail_form"),
					"POST",
					callback_MultiDeleteApplicationCodeAction);
		
		}
	});
	
};
//callback 함수
var callback_MultiDeleteApplicationCodeAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','어플리케이션 코드삭제가 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_DeleteSetting(){
	var data = $("#tableList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','데이터를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 업무코드명 : ' + data.wrkjob_cd_nm + ', <br/>애플리케이션 코드 : '+ data.tr_cd +' ]' + ' 을(를) 삭제 하시겠습니까?', function(check) {
		if (check) {
			
			ajaxCall("/ApplicationCode/Delete", 
					$("#detail_form"), 
					"POST",
					callback_DeleteSettingAction);
		}
	});
}

//callback 함수
var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	
	$("#detail_form #wrkjob_cd").combotree({readonly:true});
	$("#detail_form #tr_cd").textbox({readonly:true});

	
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #wrkjob_cd_nm").val(selRow.wrkjob_cd_nm);
	$("#detail_form #wrkjob_cd").combotree("setValue", selRow.wrkjob_cd);
	$("#detail_form #wrkjob_cd_ui").textbox("setValue", selRow.wrkjob_cd);
	$("#detail_form #tr_cd").textbox("setValue", selRow.tr_cd);
	$("#detail_form #tr_cd_nm").textbox("setValue", selRow.tr_cd_nm);
	$("#detail_form #mgr_id").textbox("setValue", selRow.mgr_id);
	$("#detail_form #reg_dt").textbox("setValue", selRow.reg_dt);
}

function Btn_ResetField(){
	$("#tableList").datagrid('clearSelections');
	$("#detail_form #wrkjob_cd").combotree({readonly:false});
	$("#detail_form #tr_cd").textbox({readonly:false});

	
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #wrkjob_cd_nm").val("");
	
	$("#chk_tr_cd").val("");  
	$("#chk_wrkjob_cd").val(""); 
	$("#detail_form #wrkjob_cd").combotree("setValue","");
	$("#detail_form #wrkjob_cd_ui").textbox("setValue","");
	$("#detail_form #tr_cd").textbox("setValue","");
	$("#detail_form #tr_cd_nm").textbox("setValue","");
	$("#detail_form #mgr_id").textbox("setValue", "");
	$("#detail_form #reg_dt").textbox("setValue","");
}

//어플리케이션 일괄업로드 팝업
function Btn_ApplicationCodeRegist(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	//parent.$("#sql_text_pop").val("");
	//parent.$("#bindPopTbl tbody tr").remove();
	$('#applicationCodeExcelUploadPop').window("open");
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
	$("#multiDeleteBtn").linkbutton({disabled:true});
	fnSearch();
}


function fnSearch(){

	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("어플리케이션 코드 관리"," ");
	
		ajaxCall("/ApplicationCode/List",
				$("#submit_form"),
				"POST",
				callback_ApplicationCodeList);
		
}
//callback 함수
var callback_ApplicationCodeList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	//getApplicationCodeList();
	fnControlPaging(result);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
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
	if( $('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "" ) {
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

	$("#submit_form").attr("action","/ApplicationCode/List/ExcelDown");
	$("#submit_form").submit();
}
