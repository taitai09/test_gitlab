$(document).ready(function(){
	/** DB간 자동성능비교 탭 */
	$("#autoPerformanceCompareBetweenDatabaseTab_V2").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var performanceCompare2 = document.getElementById('performanceCompare2').src;
				if ( performanceCompare2 == undefined || performanceCompare2 == "" ) {
					document.getElementById('performanceCompare2').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompare2";
				}
				
				break;
			case 1:
				var performanceCompareResult_V2 = document.getElementById('performanceCompareResult_V2').src;
				if ( performanceCompareResult_V2 == undefined || performanceCompareResult_V2 == "" ) {
					document.getElementById('performanceCompareResult_V2').src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompareResult_V2";
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
	document.getElementById("performanceCompare").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
	if ( document.getElementById("performanceCompareResult").contentWindow.document.getElementById("project_id") != null ) {
		document.getElementById("performanceCompareResult").contentWindow.sqlPerfPacReload(projectId, sqlPerfId );
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

function selectTab( projectId, sqlAutoPerfCheckId ) {
	console.log("project : "+projectId+" , sqlAutoPerfCheckId : "+sqlAutoPerfCheckId);
	
	$("#autoPerformanceCompareBetweenDatabaseTab_V2").tabs('select',1);
	if ( document.getElementById("performanceCompareResult_V2").contentWindow.document.getElementById("project_id") == null ) {
		setTimeout(function() {
			document.getElementById("performanceCompareResult_V2").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId);
		},3500);
	} else {
		setTimeout(function() {
			document.getElementById("performanceCompareResult_V2").contentWindow.resultSearch(projectId, sqlAutoPerfCheckId);
		},300);
	}
	
}
