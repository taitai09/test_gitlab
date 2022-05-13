$(document).ready(function(){
	/** 성능 개선 현황 탭 1, 2, 3, 4, 5 */
	$("#situationDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var situationDesignIF22Src = document.getElementById('situationDesignIF2').src;
				if(situationDesignIF22Src == undefined || situationDesignIF22Src == ""){
					document.getElementById('situationDesignIF2').src="/PerformanceImprovementDesign/PerformanceImprovementOutputs_V2?menu_id="+$("#menu_id").val();
				}
				
				break;
//			case 1:
//				var situationDesignIF1Src = document.getElementById('situationDesignIF1').src;
//				if(situationDesignIF1Src == undefined || situationDesignIF1Src == ""){
//					document.getElementById('situationDesignIF1').src="/PerformanceImprovementDesign/PerformanceImprovementReport";
//				}
//				
//				break;
//			case 2:
//				var situationDesignIF3Src = document.getElementById('situationDesignIF3').src;
//				if(situationDesignIF3Src == undefined || situationDesignIF3Src == ""){
//					document.getElementById('situationDesignIF3').src="/PerformanceImprovementDesign/ByProgramType";
//				}
//				
//				break;
//			case 3:
//				var situationDesignIF4Src = document.getElementById('situationDesignIF4').src;
//				if(situationDesignIF4Src == undefined || situationDesignIF4Src == ""){
//					document.getElementById('situationDesignIF4').src="/PerformanceImprovementDesign/ByRequestType";
//				}
//				
//				break;
//			case 4:
//				var situationDesignIF5Src = document.getElementById('situationDesignIF5').src;
//				if(situationDesignIF5Src == undefined || situationDesignIF5Src == ""){
//					document.getElementById('situationDesignIF5').src="/PerformanceImprovementDesign/ByImprovementType";
//				}
//				
//				break;
			default:
				console.log("What are you? index[" + index + "] title[" + title + "]");
				break;
			}
		}
	});
});

function selectTab2(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";
	console.log("param :",param);
	$("#situationDesignTab").tabs('select',1);
	
	console.log("situationDesignIF2.src :["+document.getElementById('situationDesignIF2').src+"]");
	document.getElementById('situationDesignIF2').src="/sqlPerformanceDesign/situation/dbStructuralStatus?"+param;
	
}

function selectTab4(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";
	console.log("param :",param);
	$("#situationDesignTab").tabs('select',3);
	
	console.log("situationDesignIF4.src :["+document.getElementById('situationDesignIF4').src+"]");
	document.getElementById('situationDesignIF4').src="/sqlPerformanceDesign/situation/dbStructuralHistory?"+param;
	
}