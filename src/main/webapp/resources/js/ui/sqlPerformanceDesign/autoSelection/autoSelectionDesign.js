$(document).ready(function(){
	/**자동 선정 탭 1, 2, 3, 4 */
	$("#autoSelectionDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			console.log("index :",index);
			if(index == 0){
				console.log("autoSelectionDesignIF1.src :["+document.getElementById('autoSelectionDesignIF1').src+"]");
				var autoSelectionDesignIF1Src = document.getElementById('autoSelectionDesignIF1').src;
				if(autoSelectionDesignIF1Src == undefined || autoSelectionDesignIF1Src == ""){
					document.getElementById('autoSelectionDesignIF1').src="/sqlPerformanceDesign/autoSelection/autoSelection";
				}
			}else if(index == 1){
				console.log("autoSelectionDesignIF2.src :["+document.getElementById('autoSelectionDesignIF2').src+"]");
				var autoSelectionDesignIF22Src = document.getElementById('autoSelectionDesignIF2').src;
				if(autoSelectionDesignIF22Src == undefined || autoSelectionDesignIF22Src == ""){
					document.getElementById('autoSelectionDesignIF2').src="/sqlPerformanceDesign/autoSelection/autoSelectionStatus";
				}
			}else if(index == 2){
				console.log("autoSelectionDesignIF3.src :["+document.getElementById('autoSelectionDesignIF3').src+"]");
				var autoSelectionDesignIF3Src = document.getElementById('autoSelectionDesignIF3').src;
				if(autoSelectionDesignIF3Src == undefined || autoSelectionDesignIF3Src == ""){
					document.getElementById('autoSelectionDesignIF3').src="/sqlPerformanceDesign/autoSelection/autoSelectionStatusSearch";
				}
			}else{
				console.log("autoSelectionDesignIF4.src :["+document.getElementById('autoSelectionDesignIF4').src+"]");
				var autoSelectionDesignIF4Src = document.getElementById('autoSelectionDesignIF4').src;
				if(autoSelectionDesignIF4Src == undefined || autoSelectionDesignIF4Src == ""){
					document.getElementById('autoSelectionDesignIF4').src="/sqlPerformanceDesign/autoSelection/autoSelectionHistory";
				}
			}
		}		
	});
});

function selectTab2(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	console.log("param :",param);
	$("#autoSelectionDesignTab").tabs('select',1);
	
	console.log("autoSelectionDesignIF2.src :["+document.getElementById('autoSelectionDesignIF2').src+"]");
	document.getElementById('autoSelectionDesignIF2').src="/sqlPerformanceDesign/autoSelection/autoSelectionStatus?"+param;
	
}

function selectTab4(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	console.log("param :",param);
	$("#autoSelectionDesignTab").tabs('select',3);
	
	console.log("autoSelectionDesignIF4.src :["+document.getElementById('autoSelectionDesignIF4').src+"]");
	document.getElementById('autoSelectionDesignIF4').src="/sqlPerformanceDesign/autoSelection/autoSelectionHistory?"+param;
	
}