var beginDate = '';
var endDate = '';
var url = '';

$(document).ready(function(){
	
	beginDate = $('#perf_check_exec_begin_dt').datebox('getText');
	endDate = $('#perf_check_exec_end_dt').datebox('getText');
	
	// 점검팩 등록일자 변경 시 sql 점검팩 초기화 - 시작일자 변경
	$('#perf_check_exec_begin_dt').datebox({
		onChange:function(newValue,oldValue){
			if( strToDate(newValue) > strToDate(endDate) ){
				// 선택 한 날짜가 end_dt보다 과거일 경우 이전 선택 날짜로 세팅
				$('#perf_check_exec_begin_dt').datebox('setValue', oldValue );
				beginDate = oldValue;
				
			}else {
				beginDate = newValue;
			}
			$('#sqlPerformanceP').combobox('clear');
			$('#oldValue').val('');
		}
	});
	// 점검팩 등록일자 변경 시 sql 점검팩 초기화 - 종료일자 변경
	$('#perf_check_exec_end_dt').datebox({
		onChange:function(newValue,oldValue){
			if( strToDate(newValue) < strToDate(beginDate) ){
				// 선택 한 날짜가 begin_dt보다 과거일 경우 이전 선택 날짜로 세팅
				$('#perf_check_exec_end_dt').datebox('setValue', oldValue );
				endDate = oldValue;
				
			}else {
				endDate = newValue;
			}
			$('#sqlPerformanceP').combobox('clear');
			$('#oldValue').val('');
		}
	});
	
	// 1주일 클릭 시 점검팩 등록일자 시작일자 변경 (현재 날짜 6일 전 ~ 현재 날짜)
	$('#aWeek').radiobutton({
		onChange:function(val){
			if(val == true){
				let baseDate = $('#perf_check_exec_end_dt').datebox('getText');
				let dateArr = baseDate.split('-');
				let resultDate = dateCalculator(dateArr[0], dateArr[1], dateArr[2], 'D', -6);
				
				$('#perf_check_exec_begin_dt').datebox('setValue', resultDate );
				$('#sqlPerformanceP').combobox('clear');
			}
		}
	});
	
	// 1개월 클릭 시 점검팩 등록일자 시작일자 변경 (현재 날짜 1개월 전 ~ 현재 날짜)
	$('#oneMonth').radiobutton({
		onChange:function(val){
			if(val == true){
				let baseDate = $('#perf_check_exec_end_dt').datebox("getText");
				let dateArr = baseDate.split('-');
				let resultDate = dateCalculator(dateArr[0], dateArr[1], dateArr[2], 'M', -1);
				
				$('#perf_check_exec_begin_dt').datebox('setValue', resultDate );
				$('#sqlPerformanceP').combobox('clear');
			}
		}
	});
});

function checkConditionDate(){
	if ( beginDate == '' ||	endDate == ''	) {
		parent.$.messager.alert('경고','점검팩 등록일자를 입력해 주세요.','warning');
		$('#sqlPerformanceP').combobox('clear');
		return false;
	}else if ( beginDate > endDate ){
		parent.$.messager.alert('경고','점검팩 등록일자를 확인해 주세요.','warning');
		$('#sqlPerformanceP').combobox('clear');
		return false;
	}
	
	return true;
}

function checkConditionSqlP(){
	if ( $("#original_dbid").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
		return false;
		
	}else if ( $("#sqlPerformanceP" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
}

var ajaxCallWithSimpleData = function(url, simpleData, typeString, callback){
	
	var options = {
		
		url: url,
		type: typeString,
		data : simpleData,
		success:
			function(result){
			if(callback != 'undefined' && callback != null && callback != '')
			{
				callback(result);
			}
		},
		error: function(xhr, error){
			if (xhr.status == 401) {
				top.location.href="/auth/logout";
			}else if (xhr.status == 500) {
					top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 보안 에러가 발생했습니다. 관리자에게 문의하세요.");
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
					if(closeMessageProgress != undefined) closeMessageProgress();	
			}else if(xhr.status == 403 || xhr.status == 405){
				top.location.href="/auth/logout";
			}else if(xhr.status == 404){
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else if(xhr.status == 0){
				top.$.messager.alert('',"[net::ERR_CONNECTION_REFUSED] 서버 연결에 실패하였습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else{
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
			}
		},
		timeout: 3600000
	};
	
	$.ajax(options);
};

var strToDate = function(strDate) {
	let strDateArr = strDate.split('-');
	
	let resultDate = new Date(strDateArr[0],strDateArr[1],strDateArr[2]);
	
	return resultDate;
}