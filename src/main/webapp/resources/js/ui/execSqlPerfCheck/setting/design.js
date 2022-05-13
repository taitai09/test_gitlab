$(document).ready(function(){
	/** 성능 검증 설정 탭 1, 2, 3, 4, 5 */
	$("#settingDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				let settingDesignIF1Src = document.getElementById('settingDesignIF1').src;
				if(settingDesignIF1Src == undefined || settingDesignIF1Src == ""){
					document.getElementById('settingDesignIF1').src="/PerformanceCheckIndexDesign/DeployPrefChkIndc";
				}
				
				break;
			case 1:
				let settingDesignIF22Src = document.getElementById('settingDesignIF2').src;
				if(settingDesignIF22Src == undefined || settingDesignIF22Src == ""){
					document.getElementById('settingDesignIF2').src="/PerformanceCheckIndexDesign/DeployPerfChkStep";
				}
				
				break;
			case 2:
				let settingDesignIF3Src = document.getElementById('settingDesignIF3').src;
				if(settingDesignIF3Src == undefined || settingDesignIF3Src == ""){
					document.getElementById('settingDesignIF3').src="/PerformanceCheckIndexDesign/WjPerfChkIndc";
				}
				
				break;
			case 3:
				let settingDesignIF4Src = document.getElementById('settingDesignIF4').src;
				if(settingDesignIF4Src == undefined || settingDesignIF4Src == ""){
					document.getElementById('settingDesignIF4').src="/PerformanceCheckIndexDesign/DeployPerfChkStepTestDB";
				}
				
				break;
			case 4:
				let settingDesignIF5Src = document.getElementById('settingDesignIF5').src;
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
	console.log("param :",param);
	$("#settingDesignTab").tabs('select',1);
	
	console.log("settingDesignIF2.src :["+document.getElementById('settingDesignIF2').src+"]");
	document.getElementById('settingDesignIF2').src="/sqlPerformanceDesign/setting/dbStructuralStatus?"+param;
	
}

function selectTab4(param){
	console.log("param :",param);
	$("#settingDesignTab").tabs('select',3);
	
	console.log("settingDesignIF4.src :["+document.getElementById('settingDesignIF4').src+"]");
	document.getElementById('settingDesignIF4').src="/sqlPerformanceDesign/setting/dbStructuralHistory?"+param;
	
}