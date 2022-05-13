$(document).ready(function(){
	/**탭 1, 2 */
	$("#examineOpenPOPDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
//			console.log("index :",index);
			if(index == 0){
//				console.log("examineOpenPOPDesignIF1.src :["+document.getElementById('examineOpenPOPDesignIF1').src+"]");
				var collectSQLIndexDesignIF1Src = document.getElementById('examineOpenPOPDesignIF1').src;
				if(collectSQLIndexDesignIF1Src == undefined || collectSQLIndexDesignIF1Src == ""){
					document.getElementById('examineOpenPOPDesignIF1').src="/ExamineOpenPOP/examineAgent1";
				}
			}else{
//				console.log("examineOpenPOPDesignIF2.src :["+document.getElementById('examineOpenPOPDesignIF2').src+"]");
				var collectSQLIndexDesignIF4Src = document.getElementById('examineOpenPOPDesignIF2').src;
				if(collectSQLIndexDesignIF4Src == undefined || collectSQLIndexDesignIF4Src == ""){
					document.getElementById('examineOpenPOPDesignIF2').src="/ExamineOpenPOP/examineScheduler";
				}
			}
		}		
	});
});

//function selectTab2(param){
////	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
//	console.log("param :",param);
//	$("#indexDesignCollectSQLTab").tabs('select',2);
//	
//	console.log("collectSQLIndexDesignIF3.src :["+document.getElementById('collectSQLIndexDesignIF3').src+"]");
//	document.getElementById('collectSQLIndexDesignIF3').src="/indexDesign/collectSQL/CollectionIndexDesign?"+param;
//	
//}