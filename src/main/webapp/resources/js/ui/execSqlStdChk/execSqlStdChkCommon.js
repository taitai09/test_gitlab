function createCombobox(comboboxId){
	$(comboboxId).combobox({
		valueField:'project_id',
		textField:'project_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	loadProjectList(comboboxId);
}

var loadProjectList = function(comboboxId){
	var options = {
		url: '/sqlStandardOperationDesign/getProjectList',
		type: "GET",
		dataType:'Json',
		success:
			function(result){
				callback_loadProjectList(result,comboboxId);
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
}

function callback_loadProjectList(result,comboboxId){
	result.unshift({'project_id' : '', 'project_nm' : '전체'});
	$(comboboxId).combobox('loadData', result);
}