$(document).ready(function(){
	/** SQL 성능 추적 현황 탭 1, 2, 3 */
	$("#sqlPerformanceTraceStatusTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var sqlPerformanceTraceStatusDesignIF1Src = document.getElementById('sqlPerformanceTraceStatusDesignIF1').src;
				if(sqlPerformanceTraceStatusDesignIF1Src == undefined || sqlPerformanceTraceStatusDesignIF1Src == ""){
					document.getElementById('sqlPerformanceTraceStatusDesignIF1').src="/PerformanceCheckSql";
				}
				
				break;
			case 1:
				var sqlPerformanceTraceStatusDesignIF2Src = document.getElementById('sqlPerformanceTraceStatusDesignIF2').src;
				if(sqlPerformanceTraceStatusDesignIF2Src == undefined || sqlPerformanceTraceStatusDesignIF2Src == ""){
					document.getElementById('sqlPerformanceTraceStatusDesignIF2').src="/ExceptionHandlingSql";
				}
				
				break;
			case 2:
				var sqlPerformanceTraceStatusDesignIF3Src = document.getElementById('sqlPerformanceTraceStatusDesignIF3').src;
				if(sqlPerformanceTraceStatusDesignIF3Src == undefined || sqlPerformanceTraceStatusDesignIF3Src == ""){
					document.getElementById('sqlPerformanceTraceStatusDesignIF3').src="/SqlPerformanceTraceStatusChart";
				}
				
				break;
			default:
				console.log("What are you? index[" + index + "] title[" + title + "]");
				break;
			}
		}
	});
});

function selectTab2(param){
	$("#settingDesignTab").tabs('select',1);
	
	console.log("settingDesignIF2.src :["+document.getElementById('settingDesignIF2').src+"]");
	document.getElementById('settingDesignIF2').src="/sqlPerformanceDesign/setting/dbStructuralStatus?"+param;
	
}

function selectTab4(param){
	$("#settingDesignTab").tabs('select',3);
	
	console.log("settingDesignIF4.src :["+document.getElementById('settingDesignIF4').src+"]");
	document.getElementById('settingDesignIF4').src="/sqlPerformanceDesign/setting/dbStructuralHistory?"+param;
	
}