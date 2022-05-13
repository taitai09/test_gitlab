$(document).ready(function(){
	console.log("design menu_id:" + $('#menu_id').val());
	/** 성능 점검 SQL 메뉴 탭 1, 2 */
	$("#performanceCheckSqlTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var performanceCheckSqlDesignIF1Src = document.getElementById('performanceCheckSqlDesignIF1').src;
				if(performanceCheckSqlDesignIF1Src == undefined || performanceCheckSqlDesignIF1Src == ""){
					document.getElementById('performanceCheckSqlDesignIF1').src="/PerformanceCheckSqlDesign/sqlPerformanceTrackingStatusDesign";
				}
				
				break;
			case 1:
				var performanceCheckSqlDesignIF22Src = document.getElementById('performanceCheckSqlDesignIF2').src;
				if(performanceCheckSqlDesignIF22Src == undefined || performanceCheckSqlDesignIF22Src == ""){
					document.getElementById('performanceCheckSqlDesignIF2').src="/PerformanceCheckSqlDesign/autoSqls?menu_id=" + $('#menu_id').val();;
//					document.getElementById('performanceCheckSqlDesignIF2').src="/Sqls";
				}
				
				break;
			default:
				console.log("What are you? index[" + index + "] title[" + title + "]");
				break;
			}
		}
	});
});

function selectTab1(param){
	console.log("param :",param);
	$('#performanceCheckSqlTab').tabs('select', 1);
	
	document.getElementById('performanceCheckSqlDesignIF2').src="/PerformanceCheckSqlDesign/autoSqls?" + param + "&menu_id=" + $('#menu_id').val();
//	document.getElementById('performanceCheckSqlDesignIF2').src="/Sqls?" + param;
	
}