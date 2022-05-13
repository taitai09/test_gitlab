$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#board_mgmt_no").val(1);  //공지사항
			$("#board_no").val(row.board_no);
			$("#reg_id").val(row.reg_id);
			getDetailView();
		},		
		columns:[[
			{field:'pinset',title:'',width:"2%",halign:"center",align:"center",styler:cellStyler},
			{field:'board_no',title:'번호',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'title',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_id',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'upd_id',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'top_notice_yn',hidden:true},
			{field:'hit_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"}
			
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
//				console.log("d1:"+d1);
//				console.log("d2:"+d2);
//				console.log("d2>d1:"+(d2>d1));
				if(d2<d1){
					parent.$.messager.alert('','검색조건의 종료일은 시작일보다 커야 합니다.');
				}
				return d2 > d1;
			},
			message: 'Please select a greater date.'
		}
	});
	
	var call_from_parent = $("#call_from_parent").val();
	var searchBtnClickCount = $("#searchBtnClickCount").val();  //이게 뭐하는건가요??~~ 어떤용도?
	if(call_from_parent == "Y"||searchBtnClickCount == "1"){
		fnSearch();
	}
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


function Btn_OnClick(){
	$("#currentPage").val("1");

	if(!formValidationCheck()){
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
//	$("#submit_form").attr("action","/Precedent");
//	$("#submit_form").submit();
	fnSearch();
}

function fnSearch(){
	$('#tableList').datagrid("loadData", []);
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("공지사항"," "); 
	
	ajaxCall("/BoardMng/Notice/list",
			$("#submit_form"),
			"POST",
			callback_NoticeListAction);		
}
//callback 함수
var callback_NoticeListAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		

	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
};


function formValidationCheck(){
/*	if(($('#selectValue').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
			($('#selectValue').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 정확히 입력해 주세요.');
			return false;
		}*/
	return true;
}

function Btn_SaveSetting(){
	location.href="/BoardMng/Notice/insert?menu_id="+$("#menu_id").val();
}

function getDetailView(){
	$("#submit_form").attr("action","/BoardMng/Notice/update");
	
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
