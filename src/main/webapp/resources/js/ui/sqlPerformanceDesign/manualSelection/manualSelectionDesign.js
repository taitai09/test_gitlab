$(document).ready(function(){
	
	/**SQL 성능 점검 수동 선정 탭 1 2 */
	$("#manualSelectionDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			console.log("index :",index);
			if(index == 0){
				console.log("manualSelectionDesignTabIF1.src :["+document.getElementById('manualSelectionDesignTabIF1').src+"]");
				var collectSQLIndexDesignIF1Src = document.getElementById('manualSelectionDesignTabIF1').src;
				if(collectSQLIndexDesignIF1Src == undefined || collectSQLIndexDesignIF1Src == ""){
					document.getElementById('manualSelectionDesignTabIF1').src="/sqlPerformanceDesign/manualSelection/manualSelection";
				}
			}else if(index == 1){
				console.log("manualSelectionDesignTabIF2.src :["+document.getElementById('manualSelectionDesignTabIF2').src+"]");
				var collectSQLIndexDesignIF2Src = document.getElementById('manualSelectionDesignTabIF2').src;
				if(collectSQLIndexDesignIF2Src == undefined || collectSQLIndexDesignIF2Src == ""){
					document.getElementById('manualSelectionDesignTabIF2').src="/sqlPerformanceDesign/manualSelection/manualSelectionStatus";
				}
			}
		}		
	});
});

function selectTab1(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	console.log("param :",param);
	$("#manualSelectionDesignTab").tabs('select',1);
	
	console.log("collectSQLIndexDesignIF3.src :["+document.getElementById('manualSelectionDesignTabIF2').src+"]");
	document.getElementById('manualSelectionDesignTabIF2').src="/sqlPerformanceDesign/manualSelection/manualSelectionStatus?"+param;
	
}

