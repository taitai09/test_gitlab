$(document).ready(function() {
	let sqlId = $('#popSqlId').val();
	$('#sqlDiagnosisReportPopup').window({
		top:getWindowTop(600),
		left:getWindowLeft(1000)
	});
	
});
$('#sqlText').format({method: 'sql'});

function Btn_OnAssignAllPopClose(){
	$('#tuningAssignAllPop').window("close");
}