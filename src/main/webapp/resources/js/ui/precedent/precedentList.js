var guideOrPrecedent=1;

$(document).ready(function() {
	$("#tableList0").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#submit_form1 #guide_no").val(row.guide_no);
			$("#submit_form1 #tuning_no").val(row.tuning_no);
			$("#submit_form1 #reg_user_id").val(row.reg_user_id);
			$("#submit_form1 #guide_div_cd").val(row.guide_div_cd);
			getDetailView1();
		},		
		columns:[[
			{field:'pinset',title:'',width:"2%",halign:"center",align:"center",styler:cellStyler},
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'guide_div_cd',hidden:true}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#tableList1").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#submit_form2 #guide_no").val(row.guide_no);
			$("#submit_form2 #tuning_no").val(row.tuning_no);
			$("#submit_form2 #reg_user_id").val(row.reg_user_id);
			$("#submit_form2 #guide_div_cd").val(row.guide_div_cd);
			getDetailView2();
		},		
		columns:[[
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'tuning_no',title:'튜닝번호',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'sys_nm',title:'DB명',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			//{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'guide_div_cd',hidden:true}
			]],
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});
	 
	$.extend($.fn.validatebox.defaults.rules, {
		greaterThan:{
			validator: function(value,param){
				var v1 = $(param[0]).datebox('getValue');
				var d1 = myparser(v1);
				var d2 = myparser(value);
				if(d2<d1){
					parent.$.messager.alert('','검색조건의 종료일은 시작일보다 커야 합니다.');
				}
				return d2 > d1;
			},
			message: 'Please select a greater date.'
		}
	});
	
	$("#submit_form1 #selectValue").combobox('setValue',$("#submit_form1 #searchKey").val());	
	var call_from_parent = $("#submit_form1 #call_from_parent").val();
	var call_from_child = $("#submit_form1 #call_from_child").val();

	var searchBtnClickCount1 = $("#submit_form1 #searchBtnClickCount1").val();
	var searchBtnClickCount2 = $("#submit_form1 #searchBtnClickCount2").val();
	
	guideOrPrecedent = $("#submit_form1 #guide_div_cd").val();
	if(guideOrPrecedent == 1){
		$("#sqlTuningTab").tabs('select',0);
	}else if(guideOrPrecedent == 2){
		$("#sqlTuningTab").tabs('select',1);
	}
	
	if(call_from_parent == "Y" || call_from_child == "Y" || searchBtnClickCount1 == "1" || searchBtnClickCount2 == "1"){
		if(searchBtnClickCount1 == "1"){
			fnSearch2(1);
		}
		
		if(searchBtnClickCount2 == "1"){
			fnSearch2(2);
		}
	}
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});

	$("#sqlTuningTab").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
				guideOrPrecedent = 1;
			}else if(index == 1){
				guideOrPrecedent = 2;
			}
		}		
	});
	
	//이전, 다음 처리
	$("#submit_form1 #prevBtn").click(function(){
		if(formValidationCheck1()){
			fnGoPrevOrNext1("PREV");
		}
	});
	$("#submit_form1 #nextBtn").click(function(){
		if(formValidationCheck1()){
			fnGoPrevOrNext1("NEXT");
		}
	});
	
	//이전, 다음 처리
	$("#submit_form2 #prevBtn").click(function(){
		if(formValidationCheck2()){
			fnGoPrevOrNext2("PREV");
		}
	});
	$("#submit_form2 #nextBtn").click(function(){
		if(formValidationCheck2()){
			fnGoPrevOrNext2("NEXT");
		}
	});
	
});

function fnGoPrevOrNext1(direction){
	
	if(direction == "PREV"){
		if($('#submit_form1 #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form1 #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#submit_form1 #currentPage").val();
	if(currentPage != null && currentPage != ""){
		var iCurrentPage = parseInt(currentPage);
		if(direction == "PREV"){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			iCurrentPage++;
		}
		
		$("#submit_form1 #currentPage").val(iCurrentPage);
	}else{
		$("#submit_form1 #currentPage").val("1");
	}
	fnSearch();
}

function fnGoPrevOrNext2(direction){
	
	if(direction == "PREV"){
		if($('#submit_form2 #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form2 #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#submit_form2 #currentPage").val();
	if(currentPage != null && currentPage != ""){
		var iCurrentPage = parseInt(currentPage);
		if(direction == "PREV"){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			iCurrentPage++;
		}
		
		$("#submit_form2 #currentPage").val(iCurrentPage);
	}else{
		$("#submit_form2 #currentPage").val("1");
	}
	fnSearch();
}

function myparser(s){
	if (!s) return new Date();
	var ss = s.split('-');
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

function fnSearch(){
	if(guideOrPrecedent == 1){
		$('#tableList0').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 가이드"," "); 
		
		ajaxCall("/SqlTuningGuideAction", $("#submit_form1"), "POST", callback_SqlTuningGuideAddTable);		
	}else if(guideOrPrecedent == 2){
		$('#tableList1').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 사례"," "); 
		
		ajaxCall("/PrecedentAction", $("#submit_form2"), "POST", callback_PrecedentAddTable);			
	}
	
}

function fnSearch2(arg1){
	if(arg1 == 1){
		$("#guide_div_cd").val("1");
		$('#tableList0').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 가이드"," "); 
		
		ajaxCall("/SqlTuningGuideAction", $("#submit_form1"), "POST", callback_SqlTuningGuideAddTable);		
	}else if(arg1 == 2){
		$("#guide_div_cd").val("2");
		$('#tableList1').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 사례"," "); 
		
		ajaxCall("/PrecedentAction", $("#submit_form2"), "POST", callback_PrecedentAddTable);			
	}
	
}

//callback 함수
var callback_SqlTuningGuideAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList0').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
	
	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn1(dataLength);
};

//callback 함수
var callback_PrecedentAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList1').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		

	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn2(dataLength);
};

function formValidationCheck1(){
	if(($('#submit_form1 #selectValue').combobox('getValue') != "" && $('#submit_form1 #searchValue').textbox('getValue') == "") ||
			($('#submit_form1 #selectValue').combobox('getValue') == "" && $('#submit_form1 #searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
	}
	return true;
}

function formValidationCheck2(){
	if(($('#submit_form2 #selectValue').combobox('getValue') != "" && $('#submit_form2 #searchValue').textbox('getValue') == "") ||
			($('#submit_form2 #selectValue').combobox('getValue') == "" && $('#submit_form2 #searchValue').textbox('getValue') != "")){
		parent.$.messager.alert('','검색조건을 입력해 주세요.');
		return false;
	}
	return true;
}

function Btn_OnClick(gubun){
	if(gubun == 1){
		guideOrPrecedent = 1;
		$("#submit_form1 #guide_div_cd").val("1");
		$("#submit_form1 #searchKey").val($('#submit_form1 #selectValue').combobox('getValue'));
		$("#submit_form1 #currentPage").val("1");
		$("#submit_form1 #searchBtnClickCount1").val("1");
		$("#submit_form2 #searchBtnClickCount1").val("1");
		if(!formValidationCheck1()){
			return;
		}
	}else if(gubun == 2){
		guideOrPrecedent = 2;
		$("#submit_form2 #guide_div_cd").val("2");
		$("#submit_form2 #searchKey").val($('#submit_form2 #selectValue').combobox('getValue'));
		$("#submit_form2 #currentPage").val("1");
		$("#submit_form1 #searchBtnClickCount2").val("1");
		$("#submit_form2 #searchBtnClickCount2").val("1");
		if(!formValidationCheck2()){
			return;
		}
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#submit_form1 #menu_id").val();
	
	fnSearch();
}

function Btn_InsertGuide(){
	location.href = "/Precedent/Insert?" 
			+ "menu_id=" + $("#submit_form1 #menu_id").val() 
			+ "&searchBtnClickCount1=" + $("#submit_form1 #searchBtnClickCount1").val() 
			+ "&searchBtnClickCount2=" + $("#submit_form1 #searchBtnClickCount2").val() 
			+ "&guide_div_cd=1";
}

function getDetailView1(){
	$("#submit_form1").attr("action","/Precedent/Update");
	$("#submit_form1").submit();	
}

function getDetailView2(){
	$("#submit_form2").attr("action","/Precedent/Update");
	$("#submit_form2").submit();	
}

function Excel_DownClick(gubun){
	if(gubun == 1){
		if(($('#submit_form1 #selectValue').combobox('getValue') != "" && $('#submit_form1 #searchValue').textbox('getValue') == "") ||
				($('#submit_form1 #selectValue').combobox('getValue') == "" && $('#submit_form1 #searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
		}
		$("#submit_form1 #guide_div_cd").val("1");
		$("#submit_form1 #searchKey").val($('#submit_form1 #selectValue').combobox('getValue'));
		
		$("#submit_form1").attr("action","/Precedent/SqlTuningGuideExcelDown");
		$("#submit_form1").submit();
	}else if(gubun == 2){
		if(($('#submit_form2 #selectValue').combobox('getValue') != "" && $('#submit_form2 #searchValue').textbox('getValue') == "") ||
				($('#submit_form2 #selectValue').combobox('getValue') == "" && $('#submit_form2 #searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
		}
		$("#submit_form2 #guide_div_cd").val("2");
		$("#submit_form2 #searchKey").val($('#submit_form2 #selectValue').combobox('getValue'));
		
		$("#submit_form2").attr("action","/Precedent/ExcelDown");
		$("#submit_form2").submit();
	}
}


function cellStyler(value,row,index){
		return 'color:#474038;';
}

function fnEnableDisablePagingBtn1(dataLength){
	//페이징 처리
	var currentPage = $("#submit_form1 #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form1 #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$('#submit_form1 #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form1 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form1 #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#submit_form1 #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form1 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form1 #nextBtn').linkbutton('disable');
		}
	}
}


function fnEnableDisablePagingBtn2(dataLength){
	//페이징 처리
	var currentPage = $("#submit_form2 #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form2 #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$('#submit_form2 #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form2 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form2 #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#submit_form2 #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form2 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form2 #nextBtn').linkbutton('disable');
		}
	}
}