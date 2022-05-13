$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	$("#dbid").val(rec.dbid);

	    	// Instance 조회			
	    	$('#selectInstance').combobox({
	    	    url:"/Common/getAgentInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
	    	    onSelect:function(rec1){
	    	    	setMinMaxDateTime();
	    	    },
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
						$("#inst_id").val(items[0][opts.valueField]);
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','Instance 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$("#inst_id").val("");
	    	$("#start_dateTime").textbox("setValue","");
	    	$("#end_dateTime").textbox("setValue","");
	    }	    
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance를 선택해 주세요.');
		return false;
	}	
	
	if($('#start_dateTime').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 날짜와 시간을 선택해 주세요.');
		return false;
	}
	
	if($('#end_dateTime').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 날짜와 시간을 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#gubun").val($('#selectGubun').combobox('getValue'));

	ajaxCall("/ASHReport",
		$("#submit_form"),
		"POST",
		callback_ASHReportAction);
	
	var win = parent.$.messager.progress({
		title:'Please waiting',
		msg:'ASH 분석',
		text:'생성중입니다.',
		interval:100
	});
}

//callback 함수
var callback_ASHReportAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#dataArea").html("");
		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			if($("#selectGubun").combobox("getValue") == "html"){
				strHtml += post.row_value;
			}else{
				strHtml += strReplace(strReplace(post.row_value,"\n","<br/>")," ","&nbsp;") + "<br/>";
			}
		}
		
		$("#dataArea").html(strHtml);
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};

function showDateTimePopup(timeGubun){
	timeGb = timeGubun;
	$('#timePop').window("open");
	var minDate = $("#min_date").val();
	var maxDate = $("#max_date").val();
	var minTime;
	var maxTime;
	if(timeGubun == "start"){
		minTime = $("#min_time").val();
		maxTime = "23:59:59";
	}else{
		minTime = "00:00:00"
		maxTime = $("#max_time").val();
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	var tempMinDate = strReplace(minDate, "-", "");
	var tempMaxDate = strReplace(maxDate, "-", "");
	
//	parent.$('#strDate').datebox().datebox('calendar').calendar({
//		validator: function(date){			
//			var d1 = new Date(tempMinDate.substr(0,4),tempMinDate.substr(4,2)-1,tempMinDate.substr(6,2));
//			var d2 = new Date(tempMaxDate.substr(0,4),tempMaxDate.substr(4,2)-1,tempMaxDate.substr(6,2));
//			return d1<=date && date<=d2;
//		}
//	});
//	
//	parent.$('#strTime').timespinner({
//		min : minTime,
//		max : maxTime
//	});
//
//	if(timeGubun == "start"){
//		parent.$("#strDate").datebox("setValue",minDate);
//		parent.$("#strTime").timespinner("setValue",$("#min_time").val());
//	}else{
//		parent.$("#strDate").datebox("setValue",maxDate);
//		parent.$("#strTime").timespinner("setValue",$("#max_time").val());
//	}
	$('#strDate').datebox().datebox('calendar').calendar({
		validator: function(date){			
			var d1 = new Date(tempMinDate.substr(0,4),tempMinDate.substr(4,2)-1,tempMinDate.substr(6,2));
			var d2 = new Date(tempMaxDate.substr(0,4),tempMaxDate.substr(4,2)-1,tempMaxDate.substr(6,2));
			return d1<=date && date<=d2;
		}
	});
	
	$('#strTime').timespinner({
		min : minTime,
		max : maxTime
	});

	if(timeGubun == "start"){
		$("#strDate").datebox("setValue",minDate);
		$("#strTime").timespinner("setValue",$("#min_time").val());
	}else{
		$("#strDate").datebox("setValue",maxDate);
		$("#strTime").timespinner("setValue",$("#max_time").val());
	}	
}

function Btn_SaveClick(){
	$("#submit_form").attr("action","/ASHReport/Save");
	$("#submit_form").submit();		
}

function Btn_PrintClick() {
   $("#dataArea").printElement({pageTitle: 'ASH REPORT',printMode: 'popup',overrideElementCSS:true,leaveOpen:false});
}

function setMinMaxDateTime() {
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	
	ajaxCall("/ASHReport/Popup/SetMinMaxDateTime",
			$("#submit_form"),
			"POST",
			callback_SetMinMaxDateTimeAction);		
}

//callback 함수
var callback_SetMinMaxDateTimeAction = function(result) {
	if(result.result){
		var post = result.object;
		
		$("#min_date").val(post.min_date);
		$("#min_time").val(post.min_time);
		$("#max_date").val(post.max_date);
		$("#max_time").val(post.max_time);
	}else{
		parent.$.messager.alert('',result.message);
	}
};	