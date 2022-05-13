$(document).ready(function(){
	let project_id_inherited = getCookie('PROJECT_ID');
	deleteCookie('PROJECT_ID');
	
	let tabIdx = 0;
	if( isEmpty(project_id_inherited) ){
		document.getElementById('sqlStandardCheckResult').src='/sqlStandardOperationDesign/sqlStandardCheckResult?wrkjob_cd=' + wrkjob_cd;
		
	}else {
		document.getElementById('nonStandardSql').src='/sqlStandardOperationDesign/nonStandardSql?project_id=' + project_id_inherited + '&wrkjob_cd=' + wrkjob_cd;
		tabIdx = 1;
	}
	
	if( auth_cd != 'ROLE_DBMANAGER' && auth_cd != 'ROLE_OPENPOPMANAGER' ){
		func_hideTab();
	}
	
	$("#operationDesignTab").tabs({
		plain: true,
		selected: tabIdx,
		onSelect: function(title,index){
			switch(index) {
				case 0:
					parent.openMessageProgress('SQL 표준 점검 결과',' ');
					
					let sqlStandardCheckResult = document.getElementById('sqlStandardCheckResult').src;
					
					if(sqlStandardCheckResult == undefined || sqlStandardCheckResult == ""){
						document.getElementById('sqlStandardCheckResult').src="/sqlStandardOperationDesign/sqlStandardCheckResult?wrkjob_cd=" + wrkjob_cd;
						
					} else {
						parent.closeMessageProgress();
					}
					break;
				case 1:
					parent.openMessageProgress('표준 미준수 SQL',' ');
					
					let nonStandardSql = document.getElementById('nonStandardSql').src;
					if(nonStandardSql == undefined || nonStandardSql == ''){
						document.getElementById('nonStandardSql').src="/sqlStandardOperationDesign/nonStandardSql";
						
					} else {
						parent.closeMessageProgress();
					}
					break;
				case 2:
					parent.openMessageProgress('SQL 표준 점검 예외 대상 관리',' ');
					
					let maintainQualityCheckException = document.getElementById('maintainQualityCheckException').src;
					if(maintainQualityCheckException == undefined || maintainQualityCheckException == ""){
						document.getElementById('maintainQualityCheckException').src="/sqlStandardOperationDesign/MaintainQualityCheckException";
						
					} else {
						parent.closeMessageProgress();
					}
					break;
				case 3:
					parent.openMessageProgress('스케줄러 관리',' ');
					
					let manageScheduler = document.getElementById('manageScheduler').src;
					if(manageScheduler == undefined || manageScheduler == ""){
						document.getElementById('manageScheduler').src="/sqlStandardOperationDesign/manageScheduler";
						
					} else {
						parent.closeMessageProgress();
					}
					break;
				default:
					console.log("Who are you? index[" + index + "] title[" + title + "]");
				break;
			}
		}
	});
});
function func_hideTab(){
	try {
		let tabIdxArr = [2, 3];
		let p = '';
		
		for( let loopIdx = 0; loopIdx < tabIdxArr.length; loopIdx++ ){
			p = $('#operationDesignTab').tabs('getTab', tabIdxArr[loopIdx]);
			p.panel('options').tab.hide();
		}
	}catch (err){
		console.log('Error Occured', err);
	}
}

function moveToOtherTab( tabIndex, tabId, parameter ) {
	let tabUrl = '/sqlStandardOperationDesign/' + tabId + '?' + parameter;
	document.getElementById( tabId ).src = tabUrl;
	
	$('#operationDesignTab').tabs('select',tabIndex);
}