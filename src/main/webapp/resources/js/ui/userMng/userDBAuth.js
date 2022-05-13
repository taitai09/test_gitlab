var currentPagePerCount = 100;
$(document).ready(function() {
	$("body").css("visibility", "visible");
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
	
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	

	
	
	$('#search_dbid').combobox({
	    url:"/Common/getDatabase?isAll=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
	    onSelect: function(val){
	    	if(val.dbid != ""){
	    		$("#span_dbAuth").show();
	    	}else{
	    		$("#chk_dbAuth").checkbox({checked:false});
	    		$("#span_dbAuth").hide();
	    	}
	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess:function(){
			$("#span_dbAuth").hide();
		}
	});	
	
	$("#userList").datagrid({
		view: myview,
		selectOnCheck : false,
		checkOnSelect : false,
		onClickRow : function(index,row) {
			setUserId(row);
		},		
		columns:[[
			{field:'chk',checkbox:"true"}, //user_id 를 두개적을시 체크박스가 두개 생성되서 임시로 chk로 만들어놈.
			{field:'user_id',title:'사용자 ID',width:"22%",halign:"center",align:"center",sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"22%",halign:"center",align:'center'},			
			{field:'ext_no',title:'내선번호',width:"22%",halign:"center",align:'center'},
			{field:'hp_no',title:'핸드폰 번호',width:"22%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"10%",halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

//	$("#dbAuthHistoryList").datagrid({
//		view: myview,
//		rownumbers: true,
//		columns:[[			
//			{field:'dbid',hidden:"true"},
//			{field:'db_name',title:'DB명',width:"30%",halign:"center",align:"center",sortable:"true"},			
//			{field:'privilege_start_day',title:'권한시작일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
//			{field:'privilege_end_day',title:'권한종료일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
//		]],
//
//    	onLoadError:function() {
//    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
//    	} 
//	});		
	
	$("#chkAll").click( function (){
		if ( $("#chkAll").is(":checked") ){
			$(".chkCate").prop("checked", true);
		}else{
			$(".chkCate").prop("checked", false);
		}
	});	
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
			fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled").click(function(){
			fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
});

//function Btn_OnClick(){
//
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();	
//	
//	$('#userList').datagrid('loadData',[]);
//	
//	/* modal progress open */
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress("사용자 DB권한 관리"," "); 
//
//	ajaxCall("/Users",
//			$("#left_form"),
//			"POST",
//			callback_UsersAction);		
//}

//callback 함수
//var callback_UsersAction = function(result) {
//	$("#dbAuthList tbody tr").remove();
////	$('#dbAuthHistoryList').datagrid('loadData',[]);
//
//	json_string_callback_common(result,'#userList',true);
//};

function Select_onClickUser(){
//	$('#dbAuthHistoryList').datagrid('loadData',[]);
	
	ajaxCall("/UserDBAuth",
			$("#detail_form"),
			"POST",
			callback_UserDBAuthAction);
}

//callback 함수
var callback_UserDBAuthAction = function(result) {	
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
				backValue = "style='background:#f5f5f5;'";
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
			
			strHtml += "<td class='ctext'><input type='text' id='privilegeStartDay"+i+"' name='privilegeStartDay' value='"+startDay+"' data-options=\"panelHeight:'auto',editable:true\" class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='privilegeEndDay"+i+"' name='privilegeEndDay' value='"+endDay+"' data-options=\"panelHeight:'auto',editable:true\" class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "</tr>";			
		}
		
		$("#dbAuthList tbody").append(strHtml);
		
		$(".datapicker").datebox({
			formatter:myformatter,
			parser:myparser
		});
	}else{
		parent.$.messager.alert('','검색된 데이터가 없습니다.');
	}
};

function setBackground(rowIndex){
	if($("#chk"+rowIndex).is(":checked")){
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#f5f5f5");
	}else{
		$("#chk"+rowIndex).parent().parent("tr").css("background-color","#ffffff");
	}
}

function Btn_SaveUserDBAuth(){
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
			parent.$.messager.alert('','권한 시작일자를 정확히 입력해 주세요.');
			return false;
		}
		
		if(errECnt > 0){
			parent.$.messager.alert('','권한 종료일자를 정확히 입력해 주세요.');
			return false;
		}	
	
		
		var rows = $('#userList').datagrid('getChecked');
		var chk_user_id = new Array();
	
		if(rows.length == 0){
			parent.$.messager.alert('','왼쪽테이블의 사용자를 선택(체크)해 주세요.');
			return false;
		}
		
		parent.$.messager.confirm('', '선택된 체크박스 모두를 업데이트 하시겠습니까?', function(check) {
			if (check) {
				
				for(var i=0; i<rows.length; i++){
					chk_user_id.push(rows[i].user_id);
				}
				
				$("#chk_user_id").val(chk_user_id.join(','));  //배열을 스트링으로
				
				ajaxCall("/UserDBAuth/Save",
						$("#detail_form"),
						"POST",
						callback_SaveUserDBAuthAction);		
			}
		});

}
//callback 함수
var callback_SaveUserDBAuthAction = function(result) {
	if(result.result){
		
		setTimeout(function() {
			//Select_onClickUser();
			Btn_OnClick();			
			parent.$.messager.alert('','저장 되었습니다.');
//			parent.$.messager.alert('','사용자 DB권한 정보 저장이 완료 되었습니다.');
		},1000);
		
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function setUserId(selRow){
	$("#subTitle").html("▶ [ " + selRow.user_nm + " ] DB권한  추가");
	$("#histTitle").html("▶ [ " + selRow.user_nm + " ] DB권한 이력");
	$("#detail_form #user_id").val(selRow.user_id);
	$("#history_form #user_id").val(selRow.user_id);

	Select_onClickUser();
}

function setDetailView(dbId){
	$("#history_form #dbid").val(dbId);
	
//	$('#dbAuthHistoryList').datagrid('loadData',[]);
//	$('#dbAuthHistoryList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#dbAuthHistoryList').datagrid('loading'); 
	
//	ajaxCall("/UserDBAuthHistory",
//			$("#history_form"),
//			"POST",
//			callback_UserDBAuthHistoryAction);
}

//callback 함수
//var callback_UserDBAuthHistoryAction = function(result) {
//	var data = JSON.parse(result);
//	$('#dbAuthHistoryList').datagrid("loadData", data);
//	$('#dbAuthHistoryList').datagrid('loaded');
//};



/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#left_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#left_form #currentPage").val(currentPage);
	}else{
		$("#left_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#left_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#left_form #currentPage").val("1");
		return;
	}

	Btn_OnClick('P');
}

function Btn_OnClick(val){
	
	if(($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#left_form #currentPage").val('1');
		$("#left_form #pagePerCount").val(currentPagePerCount);
	}
	fnSearch();
	$("#call_from_parent").val("N");
	$("#call_from_child").val("N");
}

function fnSearch(){
	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$('#userList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("사용자 DB권한 관리"," "); 

	ajaxCall("/Users",
			$("#left_form"),
			"POST",
			callback_UsersAction);	
};
var callback_UsersAction = function(result) {
	$("#dbAuthList tbody tr").remove();
	json_string_callback_common(result,'#userList',true);
	fnControlPaging(result);  //페이버튼세팅

};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#left_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#left_form #pagePerCount").val();
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
/*페이징처리끝*/