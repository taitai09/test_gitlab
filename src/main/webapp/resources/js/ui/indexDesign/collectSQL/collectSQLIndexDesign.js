$(document).ready(function(){
	/**성능점검관리 탭 1, 2, 3, 4 */
	$("#indexDesignCollectSQLTab").tabs({
		plain: true,
		onSelect: function(title,index){
			console.log("index :",index);
			if(index == 0){
				console.log("collectSQLIndexDesignIF1.src :["+document.getElementById('collectSQLIndexDesignIF1').src+"]");
				var collectSQLIndexDesignIF1Src = document.getElementById('collectSQLIndexDesignIF1').src;
				if(collectSQLIndexDesignIF1Src == undefined || collectSQLIndexDesignIF1Src == ""){
					document.getElementById('collectSQLIndexDesignIF1').src="/indexDesign/collectSQL/ParsingCollectionTerms";
				}
			}else if(index == 1){
				console.log("collectSQLIndexDesignIF2.src :["+document.getElementById('collectSQLIndexDesignIF2').src+"]");
				var collectSQLIndexDesignIF2Src = document.getElementById('collectSQLIndexDesignIF2').src;
				if(collectSQLIndexDesignIF2Src == undefined || collectSQLIndexDesignIF2Src == ""){
					document.getElementById('collectSQLIndexDesignIF2').src="/indexDesign/collectSQL/AutoCollectionIndexDesign";
				}
			}else if(index == 2){
				console.log("collectSQLIndexDesignIF3.src :["+document.getElementById('collectSQLIndexDesignIF3').src+"]");
				var collectSQLIndexDesignIF3Src = document.getElementById('collectSQLIndexDesignIF3').src;
				if(collectSQLIndexDesignIF3Src == undefined || collectSQLIndexDesignIF3Src == ""){
					document.getElementById('collectSQLIndexDesignIF3').src="/indexDesign/collectSQL/CollectionIndexDesign";
				}
			}else{
				console.log("collectSQLIndexDesignIF4.src :["+document.getElementById('collectSQLIndexDesignIF4').src+"]");
				var collectSQLIndexDesignIF4Src = document.getElementById('collectSQLIndexDesignIF4').src;
				if(collectSQLIndexDesignIF4Src == undefined || collectSQLIndexDesignIF4Src == ""){
					document.getElementById('collectSQLIndexDesignIF4').src="/indexDesign/collectSQL/CollectionIndexUsage";
				}
			}
		}		
	});
});

function selectTab2(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	console.log("param :",param);
	$("#indexDesignCollectSQLTab").tabs('select',2);
	
	console.log("collectSQLIndexDesignIF3.src :["+document.getElementById('collectSQLIndexDesignIF3').src+"]");
	document.getElementById('collectSQLIndexDesignIF3').src="/indexDesign/collectSQL/CollectionIndexDesign?"+param;
	
}