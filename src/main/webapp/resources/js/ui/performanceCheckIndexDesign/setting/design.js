$(document).ready(function(){
	/** 성능 점검 설정 탭 1, 2, 3, 4, 5 */
	$("#settingDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var settingDesignIF1Src = document.getElementById('settingDesignIF1').src;
				if(settingDesignIF1Src == undefined || settingDesignIF1Src == ""){
					document.getElementById('settingDesignIF1').src="/PerformanceCheckIndexDesign/DeployPrefChkIndc";
				}
				
				break;
			case 1:
				var settingDesignIF22Src = document.getElementById('settingDesignIF2').src;
				if(settingDesignIF22Src == undefined || settingDesignIF22Src == ""){
					document.getElementById('settingDesignIF2').src="/PerformanceCheckIndexDesign/DeployPerfChkStep";
				}
				
				break;
			case 2:
				var settingDesignIF3Src = document.getElementById('settingDesignIF3').src;
				if(settingDesignIF3Src == undefined || settingDesignIF3Src == ""){
					document.getElementById('settingDesignIF3').src="/PerformanceCheckIndexDesign/WjPerfChkIndc";
				}
				
				break;
			case 3:
				var settingDesignIF4Src = document.getElementById('settingDesignIF4').src;
				if(settingDesignIF4Src == undefined || settingDesignIF4Src == ""){
					document.getElementById('settingDesignIF4').src="/PerformanceCheckIndexDesign/DeployPerfChkStepTestDB";
				}
				
				break;
			case 4:
				var settingDesignIF5Src = document.getElementById('settingDesignIF5').src;
				if(settingDesignIF5Src == undefined || settingDesignIF5Src == ""){
					document.getElementById('settingDesignIF5').src="/PerformanceCheckIndexDesign/DeployPerfChkParsingSchema";
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
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";
	console.log("param :",param);
	$("#settingDesignTab").tabs('select',1);
	
	console.log("settingDesignIF2.src :["+document.getElementById('settingDesignIF2').src+"]");
	document.getElementById('settingDesignIF2').src="/sqlPerformanceDesign/setting/dbStructuralStatus?"+param;
	
}

function selectTab4(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";
	console.log("param :",param);
	$("#settingDesignTab").tabs('select',3);
	
	console.log("settingDesignIF4.src :["+document.getElementById('settingDesignIF4').src+"]");
	document.getElementById('settingDesignIF4').src="/sqlPerformanceDesign/setting/dbStructuralHistory?"+param;
	
}