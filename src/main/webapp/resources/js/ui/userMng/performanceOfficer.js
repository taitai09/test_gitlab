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
		   Btn_OnClickUser();
	   }
	});	
	
	$("#tunerList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setTunerId(row);
		},		
		columns:[[
			{field:'user_id',title:'사용자 ID',width:"15%",halign:"center",align:"center",sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"24%",halign:"center",align:'left'},			
			{field:'ext_no',title:'내선번호',width:"23%",halign:"center",align:'center'},
			{field:'hp_no',title:'핸드폰 번호',width:"23%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"15%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

//	$("#dbOfficerHistoryList").datagrid({
//		view: myview,
//		rownumbers: true,
//		columns:[[			
//			{field:'dbid',hidden:"true"},
//			{field:'db_name',title:'DB명',width:"30%",halign:"center",align:"center",sortable:"true"},			
//			{field:'tun_start_day',title:'성능담당 시작일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
//			{field:'tun_end_day',title:'성능담당 종료일자',width:"35%",halign:"center",align:'center',formatter:getDateFormat},
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
});

function Btn_OnClickUser(){
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#tunerList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("튜닝담당자 관리"," "); 

	ajaxCall("/PerformanceTuner",
			$("#left_form"),
			"POST",
			callback_PerformanceTunerAction);		
}

//callback 함수
var callback_PerformanceTunerAction = function(result) {
	$("#dbOfficerList tbody tr").remove();
//	$('#dbOfficerHistoryList').datagrid('loadData',[]);

	var data = JSON.parse(result);
	$('#tunerList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};

function Btn_OnClick(){
//	$('#dbOfficerHistoryList').datagrid('loadData',[]);
	
/*	if(($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
		parent.$.messager.alert('','검색어를 입력해 주세요.');
		return false;
	}*/
	
	ajaxCall("/PerformanceOfficer",
			$("#detail_form"),
			"POST",
			callback_PerformanceOfficerAction);
}

//callback 함수
var callback_PerformanceOfficerAction = function(result) {	
	var strHtml = "";
	$("#dbOfficerList tbody tr").remove();
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
			//strHtml += "<td class='ctext' onClick='setDetailView(\""+post.dbid+"\");' style='cursor:pointer;'>"+post.db_name+"<input type='hidden' id='dbid"+i+"' name='dbid' value='"+post.dbid+"'/></td>";
			strHtml += "<td class='ctext' onClick='setDetailView(\""+post.dbid+"\");'>"+post.db_name+"<input type='hidden' id='dbid"+i+"' name='dbid' value='"+post.dbid+"'/></td>";
			strHtml += "<input type='hidden' id='use_flag"+i+"' name='use_flag' value='"+post.use_flag+"'/><input type='hidden' id='tun_start_day"+i+"' name='tun_start_day'/><input type='hidden' id='tun_end_day"+i+"' name='tun_end_day'/>";
			
			if(post.tun_start_day != "" && post.tun_start_day != null){
				startDay = post.tun_start_day.substr(0,4) + "-" + post.tun_start_day.substr(4,2) + "-" + post.tun_start_day.substr(6,2)
		    }
			
			if(post.tun_end_day != "" && post.tun_end_day != null){
				endDay = post.tun_end_day.substr(0,4) + "-" + post.tun_end_day.substr(4,2) + "-" + post.tun_end_day.substr(6,2)
		    }
			
			strHtml += "<td class='ctext'><input type='text' id='tunStartDay"+i+"' name='tunStartDay' value='"+startDay+"' data-options=\"panelHeight:'auto',editable:true\" class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='tunEndDay"+i+"' name='tunEndDay' value='"+endDay+"' data-options=\"panelHeight:'auto',editable:true\" class='w150 datapicker easyui-datebox'/></td>";
			strHtml += "</tr>";			
		}
		
		$("#dbOfficerList tbody").append(strHtml);
		
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

function Btn_SaveTunerDBOfficer(){
	var errSCnt = 0;
	var errECnt = 0;
	
	$('.chkCate').each(function() {
		if(this.checked){
	        if($("#tunStartDay"+$(this).val()).textbox("getValue") == ""){
	        	errSCnt++;
	        }
	        
	        if($("#tunEndDay"+$(this).val()).textbox("getValue") == ""){
	        	errECnt++;
	        }		
	        
	        $("#detail_form #use_flag"+$(this).val()).val("Y");
		}else{
			$("#detail_form #use_flag"+$(this).val()).val("N");
		}

    	$("#detail_form #tun_start_day"+$(this).val()).val(strReplace($("#detail_form #tunStartDay"+$(this).val()).datebox('getValue'),"-",""));
    	$("#detail_form #tun_end_day"+$(this).val()).val(strReplace($("#detail_form #tunEndDay"+$(this).val()).datebox('getValue'),"-",""));
	});

	if(errSCnt > 0){
		parent.$.messager.alert('','성능담당 시작일자를 정확히 입력해 주세요.');
		return false;
	}
	
	if(errECnt > 0){
		parent.$.messager.alert('','성능담당 종료일자를 정확히 입력해 주세요.');
		return false;
	}	

	ajaxCall("/PerformanceOfficer/Save",
			$("#detail_form"),
			"POST",
			callback_SavePerformanceOfficerAction);
}

//callback 함수
var callback_SavePerformanceOfficerAction = function(result) {
	if(result.result){
//		parent.$.messager.alert('','튜닝담당자 정보 저장이 완료 되었습니다.','info',function(){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);	
		});
	}
};

function setTunerId(selRow){
	$("#subTitle").html("▶ [ " + selRow.user_nm + " ] DB 튜닝담당자  추가");
	$("#histTitle").html("▶ [ " + selRow.user_nm + " ] DB 튜닝담당자 이력");
	$("#detail_form #tuner_id").val(selRow.user_id);
	$("#history_form #tuner_id").val(selRow.user_id);

	Btn_OnClick();
}

function setDetailView(dbId){
//	$("#history_form #dbid").val(dbId);
//	
//	$('#dbOfficerHistoryList').datagrid('loadData',[]);
//	$('#dbOfficerHistoryList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#dbOfficerHistoryList').datagrid('loading'); 
//	
//	ajaxCall("/PerformanceOfficerHistory",
//			$("#history_form"),
//			"POST",
//			callback_PerformanceOfficerHistoryAction);
}

//callback 함수
//var callback_PerformanceOfficerHistoryAction = function(result) {
//	var data = JSON.parse(result);
//	$('#dbOfficerHistoryList').datagrid("loadData", data);
//	$('#dbOfficerHistoryList').datagrid('loaded');
//};