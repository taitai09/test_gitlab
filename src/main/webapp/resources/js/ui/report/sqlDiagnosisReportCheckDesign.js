$(document).ready(function(){
	document.getElementById('sqlDiagnosisReportStatus').src="/SQLDiagnosisReportDesign/SQLDiagnosisReportStatus";
	$("#sqlDiagnosisReportCheckTab").tabs({
		plain: true,
		selected: 0,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				try{

					let sqlDiagnosisReportStatus = document.getElementById('sqlDiagnosisReportStatus').src;
					if ( sqlDiagnosisReportStatus == undefined || sqlDiagnosisReportStatus == "" ) {
						document.getElementById('sqlDiagnosisReportStatus').src="/SQLDiagnosisReportDesign/SQLDiagnosisReportStatus?menu_id";
					}
				}catch(e) {
					console.log("requestException tab is not exist");
				}

				break;
				
				
			case 1:
				try{

					let sqlDiagnosisReport = document.getElementById('sqlDiagnosisReport').src;
					if ( sqlDiagnosisReport == undefined || sqlDiagnosisReport == "" ) {
						console.log(11111);
						document.getElementById('sqlDiagnosisReport').src="/SQLDiagnosisReportDesign/SQLDiagnosisReport?menu_id&call_from_parent=N"
					}
				}catch(e) {
					console.log("requestException tab is not exist");
				}

				break;
				
			case 2:
				try{
					let manageScheduler = document.getElementById('sqlDiagnosisReportmanageScheduler').src;
					if ( manageScheduler == undefined || manageScheduler == "" ) {
						document.getElementById('sqlDiagnosisReportmanageScheduler').src="/SQLDiagnosisReportDesign/SQLDiagnosisReportManageScheduler";
					}
				}
				catch(e) {
					console.log("requestException tab is not exist");
				}
				break;
			default:
				break;
			}
		}
	});
});