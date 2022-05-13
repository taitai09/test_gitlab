$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isAll=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			goDetailView(row);
		},		
		columns:[[
			{field:'job_exec_no',title:'수행번호',width:"10%",halign:"center",align:"center",sortable:"true"},
			{field:'job_scheduler_type_cd',hidden:"true"},
			{field:'job_scheduler_type_nm',title:'스케쥴러 유형',width:"14%",halign:"center",align:'left'},
			{field:'base_day',title:'기준일자',width:"10%",halign:"center",align:'center'},
			{field:'job_start_dt',title:'작업시작일시',width:"15%",halign:"center",align:'center'},
			{field:'job_end_dt',title:'작업종료일시',width:"15%",halign:"center",align:'center'},
			{field:'job_status_cd',title:'작업상태코드',width:"12%",halign:"center",align:'center'},
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB',width:"12%",halign:"center",align:'center'},
			{field:'wrkjob_cd',hidden:"true"},
			{field:'wrkjob_cd_nm',title:'업무명',width:"12%",halign:"center",align:'center'}			
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	// 스케쥴러 유형 조회			
	$('#schedulerTypeCd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1020&isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	
	$('#selectCombo').combobox('setValue',$("#dbid").val()); 
	$('#schedulerTypeCd').combobox('setValue',$("#job_scheduler_type_cd").val());
	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallPerformSchedulerAction();
	}
	
	
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

function ajaxCallPerformSchedulerAction(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid("loadData", []);
	
	ajaxCall("/PerformSchedulerAction",
	$("#submit_form"),
	"POST",
	callback_PerformSchedulerAddTable);	
}

//callback 함수
var callback_PerformSchedulerAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_OnClick(){
	if(($('#strStartDt').textbox('getValue') == "" && $("#strEndDt").textbox('getValue') != "") ||
		($('#strStartDt').textbox('getValue') != "" && $("#strEndDt").textbox('getValue') == "")){
		parent.$.messager.alert('','기준일자를 정확히 입력해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#job_scheduler_type_cd").val($('#schedulerTypeCd').combobox('getValue'));	
	
//	$("#submit_form").attr("action","/PerformScheduler");
//	$("#submit_form").submit();	
	ajaxCallPerformSchedulerAction();	
}

function goDetailView(selRow){
	$("#job_exec_no").val(selRow.job_exec_no);
	$("#list_job_scheduler_type_cd").val($("#job_scheduler_type_cd").val());
	$("#job_scheduler_type_cd").val(selRow.job_scheduler_type_cd);
	$("#job_scheduler_type_nm").val(selRow.job_scheduler_type_nm);
	
	$("#submit_form").attr("action","/PerformSchedulerDetail");
	$("#submit_form").submit();			
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

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#job_scheduler_type_cd").val($('#schedulerTypeCd').combobox('getValue'));
	

	/* modal progress open */
	
	ajaxCallPerformSchedulerAction();
	
}

function ajaxCallPerformSchedulerAction(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케줄러 수행 내역"," "); 

	$('#tableList').datagrid("loadData", []);
	
	ajaxCall("/PerformSchedulerAction",
	$("#submit_form"),
	"POST",
	callback_PerformSchedulerAddTable);	
}


//검색_callback 함수가 들어갈곳
//callback 함수
var callback_PerformSchedulerAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);

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
	
	if(($('#strStartDt').textbox('getValue') == "" && $("#strEndDt").textbox('getValue') != "") ||
			($('#strStartDt').textbox('getValue') != "" && $("#strEndDt").textbox('getValue') == "")){
			parent.$.messager.alert('','기준일자를 정확히 입력해 주세요.');
			return false;
		}
	
	return true;
}
/*페이징처리끝*/



function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}

	$("#submit_form").attr("action","/PerformSchedulerDetail/ExcelDown");
	$("#submit_form").submit();
}
