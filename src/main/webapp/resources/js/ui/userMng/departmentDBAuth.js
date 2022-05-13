$(document).ready(function() {
	$("#deptList").treegrid({
		idField:'id',
		treeField:'text',		
		lines: true,		
		onClickCell : function(index,row) {
			setDeptCd(row);
		},		
		columns:[[
			{field:'id',title:'부서코드',width:"15%",halign:"center",align:"center"},
			{field:'text',title:'부서명',width:"40%",halign:"center",align:'left'},
			{field:'dept_nm',hidden:"true"},
			{field:'dept_desc',title:'부서설명',width:"30%",halign:"center",align:'left'},
			{field:'use_yn',title:'사용여부',width:"15%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#dbAuthHistoryList").datagrid({
		view: myview,
		rownumbers: true,
		columns:[[			
			{field:'dbid',hidden:"true"},
			{field:'db_name',title:'DB명',width:"30%",halign:"center",align:"center",sortable:"true"},			
			{field:'privilege_start_day',title:'권한시작일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
			{field:'privilege_end_day',title:'권한종료일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
	
	$("#chkAll").click( function (){
		if ( $("#chkAll").is(":checked") ){
			$(".chkCate").prop("checked", true);
		}else{
			$(".chkCate").prop("checked", false);
		}
	});	
	
	Btn_OnClickDept();
});

function Btn_OnClickDept(){
	$('#deptList').treegrid('loadData',[]);
	$('#deptList').treegrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#deptList').treegrid('loading'); 
	
	ajaxCall("/System/UserMng/DepartmentTree",
			null,
			"GET",
			callback_DepartmentTreeAction);		
}

//callback 함수
var callback_DepartmentTreeAction = function(result) {
	$("#dbAuthList tbody tr").remove();
	$('#dbAuthHistoryList').datagrid('loadData',[]);

	var data = JSON.parse(result);
	$('#deptList').treegrid("loadData", data);
	$('#deptList').treegrid('loaded');
};

function Btn_OnClick(){
	$('#dbAuthHistoryList').datagrid('loadData',[]);
	
	ajaxCall("/System/UserMng/DepartmentDBAuth",
			$("#detail_form"),
			"POST",
			callback_DepartmentDBAuthAction);
}

//callback 함수
var callback_DepartmentDBAuthAction = function(result) {	
	var strHtml = "";
	$("#dbAuthList tbody tr").remove();
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			var startDay = ""; 
			var endDay = "";	
			var backValue = "";
			var chkValue = "";
			
			if(post.use_flag == "Y"){
				backValue = "style='background:#edf3fb;'";
				chkValue = "checked";
			}			
			
			strHtml += "<tr "+backValue+">";
			strHtml += "<td class='ctext'><input type='checkbox' id='chk"+i+"' name='chk' value='"+i+"' class='chkCate chkBox' "+chkValue+" onClick='setBackground("+i+");'/></td>";
			strHtml += "<td class='ctext' onClick='setDetailView(\""+post.dbid+"\");' style='cursor:pointer;'>"+post.db_name+"<input type='hidden' id='dbid"+i+"' name='dbid' value='"+post.dbid+"'/></td>";
			strHtml += "<input type='hidden' id='use_flag"+i+"' name='use_flag' value='"+post.use_flag+"'/><input type='hidden' id='privilege_start_day"+i+"' name='privilege_start_day'/><input type='hidden' id='privilege_end_day"+i+"' name='privilege_end_day'/>";
			
			if(post.privilege_start_day != "" && post.privilege_start_day != null){
				startDay = post.privilege_start_day.substr(0,4) + "-" + post.privilege_start_day.substr(4,2) + "-" + post.privilege_start_day.substr(6,2)
		    }
			
			if(post.privilege_end_day != "" && post.privilege_end_day != null){
				endDay = post.privilege_end_day.substr(0,4) + "-" + post.privilege_end_day.substr(4,2) + "-" + post.privilege_end_day.substr(6,2)
		    }
			
			strHtml += "<td class='ctext'><input type='text' id='privilegeStartDay"+i+"' name='privilegeStartDay' value='"+startDay+"' class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='privilegeEndDay"+i+"' name='privilegeEndDay' value='"+endDay+"' class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "</tr>";			
		}
		
		$("#dbAuthList tbody").append(strHtml);
		
		$(".datapicker").datebox({
			formatter:myformatter,
			parser:myparser
		});
	}else{
		$.messager.alert('','검색된 데이터가 없습니다.');
	}
};

function setBackground(rowIndex){
	if($("#chk"+rowIndex).is(":checked")){
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#edf3fb");
	}else{
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#ffffff");
	}
}

function Btn_SaveDeptDBAuth(){
	var errSCnt = 0;
	var errECnt = 0;
	
	$('.chkCate').each(function() {
		if(this.checked){
	        if($("#privilegeStartDay"+$(this).val()).textbox("getValue") == ""){
	        	errSCnt++;
	        }
	        
	        if($("#privilegeEndDay"+$(this).val()).textbox("getValue") == ""){
	        	errECnt++;
	        }		
	        
	        $("#detail_form #use_flag"+$(this).val()).val("Y");
		}else{
			$("#detail_form #use_flag"+$(this).val()).val("N");
		}

    	$("#detail_form #privilege_start_day"+$(this).val()).val(strReplace($("#detail_form #privilegeStartDay"+$(this).val()).datebox('getValue'),"-",""));
    	$("#detail_form #privilege_end_day"+$(this).val()).val(strReplace($("#detail_form #privilegeEndDay"+$(this).val()).datebox('getValue'),"-",""));
	});

	if(errSCnt > 0){
		$.messager.alert('','권한 시작일자를 정확히 입력해 주세요.');
		return false;
	}
	
	if(errECnt > 0){
		$.messager.alert('','권한 종료일자를 정확히 입력해 주세요.');
		return false;
	}	

	ajaxCall("/System/UserMng/SaveDepartmentDBAuth",
			$("#detail_form"),
			"POST",
			callback_SaveDepartmentDBAuthAction);		
}

//callback 함수
var callback_SaveDepartmentDBAuthAction = function(result) {
	if(result.result){
		$.messager.alert('','부서 DB권한 정보 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});
	}
};

function setDeptCd(selRow){
	$("#subTitle").html("▶ [ " + selRow.dept_nm + " ] DB권한  추가");
	$("#histTitle").html("▶ [ " + selRow.dept_nm + " ] DB권한 이력");
	$("#detail_form #dept_cd").val(selRow.id);
	$("#history_form #dept_cd").val(selRow.id);

	Btn_OnClick();
}

function setDetailView(dbId){
	$("#history_form #dbid").val(dbId);
	
	$('#dbAuthHistoryList').datagrid('loadData',[]);
	$('#dbAuthHistoryList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dbAuthHistoryList').datagrid('loading'); 
	
	ajaxCall("/System/UserMng/DepartmentDBAuthHistory",
			$("#history_form"),
			"POST",
			callback_DepartmentDBAuthHistoryAction);
}

//callback 함수
var callback_DepartmentDBAuthHistoryAction = function(result) {
	var data = JSON.parse(result);
	$('#dbAuthHistoryList').datagrid("loadData", data);
	$('#dbAuthHistoryList').datagrid('loaded');
};