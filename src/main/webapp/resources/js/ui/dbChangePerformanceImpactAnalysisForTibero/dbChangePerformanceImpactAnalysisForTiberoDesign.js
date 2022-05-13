$(document).ready(function(){
	/** DB간 자동성능비교 탭 */
	$("#dbChangePerformanceImpactAnalysisForTiberoTab").tabs({
		plain: true,
		onSelect: function(title,index){
			
			switch(index) {
			case 0:
				parent.openMessageProgress("성능 영향도 분석"," ");
				var performanceCompareForTibero = document.getElementById('performanceCompareForTibero').src;
				if ( performanceCompareForTibero == undefined || performanceCompareForTibero == "" ) {
					document.getElementById('performanceCompareForTibero').src="/DBChangePerformanceImpactAnalysisForTibero/PerformanceCompareForTibero";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 1:
				parent.openMessageProgress("성능 영향도 분석 결과"," ");
				
				var performanceCompareResultForTibero = document.getElementById('performanceCompareResultForTibero').src;
				if ( performanceCompareResultForTibero == undefined || performanceCompareResultForTibero == "" ) {
					document.getElementById('performanceCompareResultForTibero').src="/DBChangePerformanceImpactAnalysisForTibero/PerformanceCompareResultForTibero";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 2:
				parent.openMessageProgress("튜닝실적"," ");
				var tuningPerformanceForTibero = document.getElementById('tuningPerformanceForTibero').src;
				if ( tuningPerformanceForTibero == undefined || tuningPerformanceForTibero == "" ) {
					document.getElementById('tuningPerformanceForTibero').src="/DBChangePerformanceImpactAnalysisForTibero/TuningPerformanceForTibero";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 3:
				parent.openMessageProgress("튜닝SQL일괄검증"," ");
				var tuningSqlBatchVerifyForTibero = document.getElementById('tuningSqlBatchVerifyForTibero').src;
				if ( tuningSqlBatchVerifyForTibero == undefined || tuningSqlBatchVerifyForTibero == "" ) {
					document.getElementById('tuningSqlBatchVerifyForTibero').src="/DBChangePerformanceImpactAnalysisForTibero/TuningSqlBatchVerifyForTibero";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 4:
				parent.openMessageProgress("SQL점검팩"," ");
				var sqlCheckForTibero = document.getElementById('sqlCheckForTibero').src;
				if ( sqlCheckForTibero == undefined || sqlCheckForTibero == "" ) {
					document.getElementById('sqlCheckForTibero').src="/DBChangePerformanceImpactAnalysisForTibero/SqlCheckForTibero";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			default:
				console.log("What are you? index[" + index + "] title[" + title + "]");
				break;
			}
			
		}
	});
	
});

//삭제 시 점검팩 Reload
function ReloadSqlPerformancePac( projectId, sqlPerfId ) {
	document.getElementById("performanceCompareForTibero").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	if ( document.getElementById("performanceCompareResultForTibero").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("performanceCompareResultForTibero").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
	if ( document.getElementById("tuningPerformanceForTibero").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("tuningPerformanceForTibero").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
	if ( document.getElementById("tuningSqlBatchVerifyForTibero").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("tuningSqlBatchVerifyForTibero").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
}

function selectTab( projectId, sqlAutoPerfCheckId ) {
	console.log("project : "+projectId+" , sqlAutoPerfCheckId : "+sqlAutoPerfCheckId);
	
	$("#dbChangePerformanceImpactAnalysisForTiberoTab").tabs('select',1);
	
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( document.getElementById("performanceCompareResultForTibero").contentWindow.document.getElementById("project_id") == null ) {
		setTimeout(function() {
			document.getElementById("performanceCompareResultForTibero").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId);
		},3000);
	} else {
		setTimeout(function() {
			document.getElementById("performanceCompareResultForTibero").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId);
		},300);
	}
	
}
