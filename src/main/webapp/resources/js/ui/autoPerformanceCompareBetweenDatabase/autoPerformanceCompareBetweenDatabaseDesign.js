$(document).ready(function(){
	/** DB간 자동성능비교 탭 */
	$("#autoPerformanceCompareBetweenDatabaseTab").tabs({
		plain: true,
		onSelect: function(title,index){
			
			switch(index) {
			case 0:
				parent.openMessageProgress("성능 영향도 분석"," ");
//				var performanceCompare = document.getElementById('performanceCompare').src;
//				if ( performanceCompare == undefined || performanceCompare == "" ) {
//					document.getElementById('performanceCompare').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompare";
//				}
				
				var performanceCompare2 = document.getElementById('performanceCompare2').src;
				if ( performanceCompare2 == undefined || performanceCompare2 == "" ) {
					document.getElementById('performanceCompare2').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompare2";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 1:
				parent.openMessageProgress("성능 영향도 분석 결과"," ");
//				var performanceCompareResult = document.getElementById('performanceCompareResult').src;
//				if ( performanceCompareResult == undefined || performanceCompareResult == "" ) {
//					document.getElementById('performanceCompareResult').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompareResult";
//				}
				
				var performanceCompareResult_V2 = document.getElementById('performanceCompareResult_V2').src;
				if ( performanceCompareResult_V2 == undefined || performanceCompareResult_V2 == "" ) {
					document.getElementById('performanceCompareResult_V2').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompareResult_V2";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 2:
				parent.openMessageProgress("튜닝실적"," ");
				var tuningPerformance = document.getElementById('tuningPerformance').src;
				if ( tuningPerformance == undefined || tuningPerformance == "" ) {
					document.getElementById('tuningPerformance').src="/AutoPerformanceCompareBetweenDatabase/TuningPerformance";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 3:
				parent.openMessageProgress("튜닝SQL일괄검증"," ");
				var tuningSqlBatchVerify = document.getElementById('tuningSqlBatchVerify').src;
				if ( tuningSqlBatchVerify == undefined || tuningSqlBatchVerify == "" ) {
					document.getElementById('tuningSqlBatchVerify').src="/AutoPerformanceCompareBetweenDatabase/TuningSqlBatchVerify";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 4:
				parent.openMessageProgress("운영SQL성능추적"," ");
				var prodSqlPerfTrack = document.getElementById('prodSqlPerfTrack').src;
				if ( prodSqlPerfTrack == undefined || prodSqlPerfTrack == "" ) {
					document.getElementById('prodSqlPerfTrack').src="/AutoPerformanceCompareBetweenDatabase/ProdSqlPerfTrack";
				} else {
					parent.closeMessageProgress();
				}
				
				break;
			case 5:
				parent.openMessageProgress("SQL점검팩"," ");
				var sqlCheck = document.getElementById('sqlCheck').src;
				if ( sqlCheck == undefined || sqlCheck == "" ) {
					document.getElementById('sqlCheck').src="/AutoPerformanceCompareBetweenDatabase/SqlCheck";
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
	document.getElementById("performanceCompare2").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	if ( document.getElementById("performanceCompareResult_V2").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("performanceCompareResult_V2").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
	if ( document.getElementById("tuningPerformance").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("tuningPerformance").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
	if ( document.getElementById("tuningSqlBatchVerify").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("tuningSqlBatchVerify").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
	if ( document.getElementById("prodSqlPerfTrack").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("prodSqlPerfTrack").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	}
}

function selectTab( projectId, sqlAutoPerfCheckId, dbcd ) {
	console.log("project : "+projectId+" , sqlAutoPerfCheckId : "+sqlAutoPerfCheckId+" , dbcd : "+dbcd);
	
	$("#autoPerformanceCompareBetweenDatabaseTab").tabs('select',1);
	
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( document.getElementById("performanceCompareResult_V2").contentWindow.document.getElementById("project_id") == null ) {
		setTimeout(function() {
			document.getElementById("performanceCompareResult_V2").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId, dbcd);
		},3000);
	} else {
		setTimeout(function() {
			document.getElementById("performanceCompareResult_V2").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId, dbcd);
		},300);
	}
	
}
