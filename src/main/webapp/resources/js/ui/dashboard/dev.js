var currentPagePerCount = 10;
var auth_cd = '';
var leader_yn = '';
var sql_std_qty_div_cd = '';

$(document).ready(function() {
	auth_cd = $('#auth_cd').val();
	leader_yn = $('#leader_yn').val();
	sql_std_qty_div_cd = $('#sql_std_qty_div_cd').val();
	
	let isShown = false;
	
	if( (sql_std_qty_div_cd == '2' || sql_std_qty_div_cd == '4') && leader_yn == 'Y' && auth_cd == 'ROLE_DEV' ){
		LoadIndexList();
		isShown = true;
		
	}else {
		isShown = false;
	}
	
	showVariableDiv( isShown );
	
	$("#myworkList").datagrid({
		view: myview,
		nowrap: true,
		onClickRow: function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			let menuParam = "tuning_no="+row.tuning_no+"&choice_div_cd="+row.choice_div_cd+"&tuning_status_cd="+row.tuning_status_cd+"&dbid="+row.dbid;
			
			parent.openLink("Y", "110", "성능 개선 관리 상세(튜닝번호:"+row.tuning_no+")", "/ImprovementManagementView", menuParam);
		},
		columns:[[
			{field:'tuning_no',title:'튜닝번호',width:"63px",halign:"center",align:'center',sortable:"true"},
			{field:'dbid',hidden:"true"},
			{field:'choice_div_cd',hidden:"true"},
			{field:'tuning_status_cd',hidden:"true"},
			{field:'tuning_status_nm',title:'튜닝상태',width:"63px",halign:"center",align:'center',sortable:"true"},
			{field:'tuning_request_dt',title:'요청일시',width:"120px",halign:"center",align:'center',sortable:"true"},
			{field:'tuning_rcess_dt',title:'반려일시',width:"114px",halign:"center",align:'center'},
			{field:'tuning_complete_dt',title:'완료일시',width:"120px",halign:"center",align:'center'},
			{field:'tuning_rcess_why',title:'반려사유',width:"128px",halign:"center",align:'left'},
			{field:'tuning_complete_why',title:'완료사유',width:"168px",halign:"center",align:'left'},
			{field:'controversialist',title:'문제점',width:"165px",halign:"center",align:'left'},
			{field:'impr_sbst',title:'개선점',width:"132px",halign:"center",align:'left'},
			{field:'dba_name',title:'담당DBA',width:"74px",halign:"center",align:'center'},
			{field:'perfr_nm',title:'담당튜너',width:"50px",halign:"center",align:'center'},
			{field:'imprb_elap_time',title:'개선전 수행시간',width:"82px",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'imprb_buffer_cnt',title:'개선전 버퍼수',width:"72px",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'impra_elap_time',title:'개선후 수행시간',width:"82px",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'impra_buffer_cnt',title:'개선후 버퍼수',width:"72px",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'elap_time_impr_ratio',title:'수행시간 개선율',width:"82px",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'buffer_impr_ratio',title:'버퍼개선율',width:"60px",halign:"center",align:'right',formatter:getNumberFormat}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#precedentList").datagrid({
		view: myview,
		onClickRow: function(index,row) {
			$("#guide_no").val(row.guide_no);
			let tuning_no = row.tuning_no;
			
			if ( tuning_no != null ){
				$("#tuning_no").val(tuning_no);
				
			}else {
				tuning_no = "";
				$("#tuning_no").val(tuning_no);
			}
			
			let menuParam = "guide_no="+row.guide_no+"&tuning_no="+tuning_no;
			
			parent.openLink("Y", "117", "SQL 튜닝 가이드", "/Precedent/Update", menuParam);
		},
		columns:[[
			{field:'pinset',title:'',width:"2%",halign:"center",align:"center",styler:cellStyler},
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center"},
			{field:'guide_div_nm',title:'유형',width:"5%",halign:"center",align:'center'},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left'},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center'},
			{field:'reg_dt',title:'등록일시',width:"120px",halign:"center",align:'center'},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$(".window").hide();
	$(".window-shadow").hide();
	$("div.panel.window.panel-htop").hide();
	
	OnSearch();
	OnSearch2();
	
	$('#searchCount').combobox({
		onChange:function(newval,oldval){
			currentPagePerCount = newval;
			$("#submit_form #pagePerCount").val(currentPagePerCount); //검색페이지수 설정
		},
		onSelect:function(rec){
			$("#submit_form #currentPage").val('1');
			$("#submit_form #pagePerCount").val(rec.value);
			
			OnSearch();
		}
	});	
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		rightAfterBtnClick();
		fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled").click(function(){
		rightAfterBtnClick();
		fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
});
function rightAfterBtnClick(){
	$("#prevBtnEnabled").hide();
	$("#prevBtnDisabled").show();
	$("#nextBtnEnabled").hide();
	$("#nextBtnDisabled").show();
}

function LoadIndexList(){
	ajaxCall("/sqlStandards/loadAllIndex",
			$("#submit_form"),
			"GET",
			callback_LoadIndexList);
}
var callback_LoadIndexList = function(result){
	try {
		if ( isNotEmpty(result) ) {
			let data = JSON.parse(result);
			data = data.rows;
			
			createSqlStdChkList(data);
			
		}else {
			errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
		}
		
	} catch(err) {
		console.error(err.message);
	}
}
function createSqlStdChkList( indexList ){
	let columns = [];
	let position = 0;
	
	if( sql_std_qty_div_cd == '2' ){
		columns = [
			{field:'WRKJOB_CD_NM',title:'업무',width:'100px',halign:'center',align:'left'},
			{field:'PROJECT_NM',title:'프로젝트',width:'200px',halign:'center',align:'left'},
			{field:'PREVIOUS_PROGRAM_CNT',title:'전회차 총본수',width:'130px',halign:'center',align:'right'},
			{field:'CURRENT_PROGRAM_CNT',title:'금회차 총본수',width:'130px',halign:'center',align:'right'},
			{field:'PREVIOUS_TOT_ERR_CNT',title:'전회차 미준수본수',width:'130px',halign:'center',align:'right'},
			{field:'CURRENT_TOT_ERR_CNT',title:'금회차 미준수본수',width:'130px',halign:'center',align:'right'},
			{field:'PREVIOUS_COMPIANCE_RATE',title:'전회차 준수율',width:'130px',halign:'center',align:'right'},
			{field:'CURRENT_COMPIANCE_RATE',title:'금회차 준수율',width:'130px',halign:'center',align:'right'},
			{field:'SQL_PARSING_ERR_CNT',title:'SQL 파싱 오류',width:'130px',halign:'center',align:'right'},
			
			{field:'CURRENT_SQL_STD_GATHER_DAY',title:'금회차 작업일자',width:'130px',halign:'center',align:'center'},
			{field:'PREVIOUS_SQL_STD_GATHER_DAY',title:'전회차 작업일자',width:'130px',halign:'center',align:'center'},
		];
		position = 9;
		
	}else if( sql_std_qty_div_cd == '4' ){
		// 추후 개발 예정
	}
	
	if( isNotEmpty(indexList) ){
		indexCnt = indexList.length;
		
		let idxNm = '';
		let colWidth = '150px';
		
		for(let i = 0; i < indexCnt; i++){
			idxNm = indexList[i].qty_chk_idt_nm;
			colWidth = byteLength( idxNm ) * 6.5 + 'px';
			
			columns.splice(position + i, 0,
					{field:'SQL'+indexList[i].qty_chk_idt_cd+'ERR_CNT',title:idxNm, width:colWidth, halign:'center',align:'right'});
		}
	}
	
	$("#sqlStdChkList").datagrid({
		view: myview,
		columns:[columns],
		onClickRow: function(index,row) {
			openSqlStdChkMenu( row );
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	OnSearch3_2();
}

function openSqlStdChkMenu( row ){
	let menuId = $('#menu_id').val();
	let menuName = '';
	let menuUrl = '';
	
	if( sql_std_qty_div_cd == '2' ){
		menuName = '형상기반 SQL 표준 일괄 점검';
		menuUrl = '/sqlStandardOperationDesign/designRenewal?wrkjob_cd='+row.WRKJOB_CD;
		
		let expdate = new Date();
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30);
		setCookie("PROJECT_ID", row.PROJECT_ID, expdate);
		
	}else if( sql_std_qty_div_cd == '4' ){
		// 추후 개발 예정
	}
	parent.createSqlStdChkTab(menuId, menuName, menuUrl, "");
}

function showVariableDiv( isShown ){
	try{
		let divArr = document.getElementsByClassName('variableDiv');
		let target = '';
		
		if ( isNotEmpty(divArr) ){
			if( isShown ){
				$('#submit_form').css('height', '68%');
				document.getElementById('staticDiv').style.height = '100%';
				
			}else {
				for(let loopIdx = 0; loopIdx < divArr.length; loopIdx++){
					target = divArr[loopIdx];
					target.parentNode.removeChild(target);
				}
				
				divArr = document.getElementsByClassName('full_widget');
				divArr[0].style.height = '39.3%';
				divArr[1].style.height = '44.5%';
			}
			
		}else {
			return;
		}
	}catch(err){
		console.log('Error Occured', err);
	}
}

function OnSearch(){
	ajaxCall("/Dashboard/MyWork",
			$("#submit_form"),
			"POST",
			callback_MyWorkAction);
};

function OnSearch2(){
	$('#currentPage2').val('1');
	
	getModulePerformanceTable();
}

function OnSearch3_2(){
	$('#sqlStdChkList').datagrid("loadData", []);
	
	if( sql_std_qty_div_cd == '2' ){
		ajaxCall_scmBasedStdQtyChk();
		
	}else if( sql_std_qty_div_cd == '4' ){
		// 추후 개발 예정
		
	}else { return; }
}
//callback 함수
var callback_MyWorkAction = function(result) {
	json_string_callback_common(result,'#myworkList',true);
	fnControlPaging(result);
};

function getModulePerformanceTable(){
	$('#currentPage_sub').val( $('#currentPage2').val() );
	
	ajaxCall_PrecedentAction();
}
function ajaxCall_PrecedentAction(){
	$('#submit_form #prevBtn2').linkbutton('disable');
	$('#submit_form #nextBtn2').linkbutton('disable');
	
	$('#precedentList').datagrid("loadData", []);
	
	ajaxCall("/Dashboard/Precedent",
			$("#submit_form2"),
			"POST",
			callback_PrecedentAction);
}
var callback_PrecedentAction = function(result) {
	try{
		let data = JSON.parse(result);
		
		$('#precedentList').datagrid('loadData', data.rows);
		fnEnableDisablePagingBtn2(data.dataCount4NextBtn);
		
	} catch(err) {
		console.log('Error Occured', err.message);
	}
	
};
function ajaxCall_scmBasedStdQtyChk(){
	ajaxCall("/Dashboard/loadScmBasedStdQtyChk",
			$("#submit_form"),
			"GET",
			callback_sqlStdChkAction);
}
var callback_sqlStdChkAction = function(result) {
	let data = JSON.parse(result);
	
	$('#sqlStdChkList').datagrid("loadData", data);
	$('#sqlStdChkList').datagrid('loaded');	
};

function cellStyler(value,row,index){
	return 'color:#474038;';
}

/*페이징처리시작*/
function fnSetCurrentPage(direction){
	let currentPage = $("#submit_form #currentPage").val();
	
	if( currentPage != null && currentPage != "" ){
		if ( direction == "PREV" ){
			currentPage--;
			
		}else if ( direction == "NEXT" ){
			currentPage++;
		}
		
		$("#submit_form #currentPage").val(currentPage);
		
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);
	
	let currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	
	if ( currentPage <= 0 ){
		$("#submit_form #currentPage").val("1");
		return;
	}
	OnSearch();
}

//검색_callback 함수가 들어갈곳
var fnControlPaging = function(result) {
	//페이징 처리
	let currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	
	let pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);
	
	let data;
	let dataLength=0;
	
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn;
		
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	
	if ( currentPage > 1 ){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if ( dataLength > 10 ){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
			
		}else {
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	
	if ( currentPage == 1 ){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		
		if ( dataLength > pagePerCount ){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}
};

function formValidationCheck(){
	return true;
}