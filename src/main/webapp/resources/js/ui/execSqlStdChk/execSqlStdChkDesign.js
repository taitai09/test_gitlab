$(document).ready(function(){
	$("#execSqlStdChkTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				let sqlStandardCheckResult = document.getElementById('sqlStandardCheckResult').src;
				if(sqlStandardCheckResult == undefined || sqlStandardCheckResult == ""){
					document.getElementById('sqlStandardCheckResult').src="/execSqlStdChkDesign/sqlStandardCheckResult";
				}
				
				break;
			case 1:
				let nonStandardSql = document.getElementById('nonStandardSql').src;
				if(nonStandardSql == undefined || nonStandardSql == ''){
					document.getElementById('nonStandardSql').src="/execSqlStdChkDesign/nonStandardSql";
				}
				
				break;
			case 2:
				let standardComplianceState = document.getElementById('standardComplianceState').src;
				if(standardComplianceState == undefined || standardComplianceState == ''){
					document.getElementById('standardComplianceState').src="/execSqlStdChkDesign/standardComplianceState";
				}
				
				break;
			case 3:
				let maintainQualityCheckException = document.getElementById('maintainQualityCheckException').src;
				if(maintainQualityCheckException == undefined || maintainQualityCheckException == ""){
					document.getElementById('maintainQualityCheckException').src="/execSqlStdChkDesign/maintainQualityCheckException";
				}
				
				break;
			case 4:
				let manageScheduler = document.getElementById('manageScheduler').src;
				if(manageScheduler == undefined || manageScheduler == ""){
					document.getElementById('manageScheduler').src="/execSqlStdChkDesign/manageScheduler";
				}
				
				break;
			default:
				break;
			}
		}
	});
});

function moveToOtherTab( tabIndex, tabId, parameter ) {
	let tabUrl = '/execSqlStdChkDesign/' + tabId + '?' + parameter;
	document.getElementById( tabId ).src = tabUrl;
	
	$('#execSqlStdChkTab').tabs('select',tabIndex);
}