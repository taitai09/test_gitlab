$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#guide_no").val(row.guide_no);
			$("#tuning_no").val(row.tuning_no);
			$("#reg_user_id").val(row.reg_user_id);
			getDetailView();
		},		
		columns:[[
			{field:'pinset',title:'',width:"2%",halign:"center",align:"center",styler:cellStyler},
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'tuning_no',title:'튜닝번호',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'guide_div_nm',title:'유형',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'sys_nm',title:'DB명',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"}
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
				console.log("d1:"+d1);
				console.log("d2:"+d2);
				console.log("d2>d1:"+(d2>d1));
				if(d2<d1){
					parent.$.messager.alert('','검색조건의 종료일은 시작일보다 커야 합니다.');
				}
				return d2 > d1;
			},
			message: 'Please select a greater date.'
		}
	});
	
	
	
	$("#selectValue").combobox('setValue',$("#searchKey").val());	
	var call_from_parent = $("#call_from_parent").val();
	var call_from_child = $("#call_from_child").val();
	var searchBtnClickCount = $("#searchBtnClickCount").val();
	if(call_from_parent == "Y"||searchBtnClickCount == "1"){
		$("#selectGuideCd").combobox('setValue',$("#guide_div_cd").val());
		fnSearch();
	}
	if(call_from_child == "Y"||searchBtnClickCount == "1"){
		$("#guide_div_cd").val("");
		fnSearch();
	}
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});		
	
});

function myparser(s){
	if (!s) return new Date();
	var ss = s.split('-');
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	console.log("y:"+y+" m:"+m+" d:"+d);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

function fnSearch(){
	$('#tableList').datagrid("loadData", []);
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("사례/가이드"," "); 
	
	ajaxCall("/PrecedentAction",
			$("#submit_form"),
			"POST",
			callback_PrecedentAddTable);		
}
function formValidationCheck(){
	if(($('#selectValue').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
			($('#selectValue').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 정확히 입력해 주세요.');
			return false;
		}
	return true;
}
function Btn_OnClick(){
	$("#currentPage").val("1");

	if(!formValidationCheck()){
		return;
	}
	$("#guide_div_cd").val($('#selectGuideCd').combobox('getValue'));
	$("#searchKey").val($('#selectValue').combobox('getValue'));
	
	fnUpdateSearchBtnClickFlag();	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	
//	$("#submit_form").attr("action","/Precedent");
//	$("#submit_form").submit();
	fnSearch();
}

//callback 함수
var callback_PrecedentAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		

	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
};

function Btn_InsertGuide(){
	location.href="/Precedent/Insert?menu_id="+$("#menu_id").val();
}

function getDetailView(){
	$("#submit_form").attr("action","/Precedent/Update");
	$("#submit_form").submit();	
}

function Excel_DownClick(){	
	if(($('#selectValue').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
			($('#selectValue').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 정확히 입력해 주세요.');
			return false;
	}
	$("#guide_div_cd").val($('#selectGuideCd').combobox('getValue'));
	$("#searchKey").val($('#selectValue').combobox('getValue'));
	
	$("#submit_form").attr("action","/Precedent/ExcelDown");
	$("#submit_form").submit();
}


function cellStyler(value,row,index){
		return 'color:#474038;';
}