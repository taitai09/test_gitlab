$(document).ready(function(){
	/** DB간 자동성능비교 탭 */
	$("#performanceImpactChangedTableTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var performanceImpactAnalysis = document.getElementById('performanceImpactAnalysis').src;
				if ( performanceImpactAnalysis == undefined || performanceImpactAnalysis == "" ) {
					document.getElementById('performanceImpactChangedTable').src="/AnalyzeImpactChangedTable/PerformanceImpactAnalysis";
				}
				
				break;
			case 1:
				var performanceImpactAnalysisResult = document.getElementById('performanceImpactAnalysisResult').src;
				if ( performanceImpactAnalysisResult == undefined || performanceImpactAnalysisResult == "" ) {
					document.getElementById('performanceImpactAnalysisResult').src="/AnalyzeImpactChangedTable/PerformanceImpactAnalysisResult";
				}
				
				break;
				
			case 2:
				var tuningPerformance = document.getElementById('tuningPerformance').src;
				if ( tuningPerformance == undefined || tuningPerformance == "" ) {
					document.getElementById('tuningPerformance').src="/AnalyzeImpactChangedTable/TuningPerformance";
				}
				
				break;
				
			case 3:
				var tuningSqlBatchVerify = document.getElementById('sqlCheck').src;
				if ( tuningSqlBatchVerify == undefined || tuningSqlBatchVerify == "" ) {
					document.getElementById('sqlCheck').src="/AnalyzeImpactChangedTable/SqlCheck";
				}
				
				break;
				
			default:
				break;
			}
		}
	});
});

//삭제 시 점검팩 Reload
function ReloadSqlPerformancePac( sqlPerfId ) {
	document.getElementById("performanceImpactAnalysis").contentWindow.sqlPerfPacClear( sqlPerfId );
	try{
		document.getElementById("performanceImpactAnalysisResult").contentWindow.sqlPerfPacClear( sqlPerfId );
		
	}catch(e){
		console.log("before performanceImpactAnalysisResult tab load");
	}
	
	try{
		document.getElementById("tuningPerformance").contentWindow.sqlPerfPacClear( sqlPerfId );
		
	}catch(e){
		console.log("before tuningPerformance tab load");
	}
}