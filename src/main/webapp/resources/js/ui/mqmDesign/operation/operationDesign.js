$(document).ready(function(){
	/**DB 구조 품질점검 작업 탭 1, 2, 3, 4 */
	$("#operationDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				var operationDesignIF1Src = document.getElementById('operationDesignIF1').src;
				if(operationDesignIF1Src == undefined || operationDesignIF1Src == ""){
					document.getElementById('operationDesignIF1').src="/MqmDesign/Operation/QualityInspectionJob";
				}
				
				break;
			case 1:
				var operationDesignIF22Src = document.getElementById('operationDesignIF2').src;
				if(operationDesignIF22Src == undefined || operationDesignIF22Src == ""){
					document.getElementById('operationDesignIF2').src="/MqmDesign/Operation/QualityInspectionJobSheet";
				}
				
				break;
			case 2:
				var operationDesignIF3Src = document.getElementById('operationDesignIF3').src;
				if(operationDesignIF3Src == undefined || operationDesignIF3Src == ""){
					document.getElementById('operationDesignIF3').src="/MqmDesign/Operation/QualityRevExcManagement";
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
	$("#operationDesignTab").tabs('select',1);
	
	console.log("operationDesignIF2.src :["+document.getElementById('operationDesignIF2').src+"]");
	document.getElementById('operationDesignIF2').src="/sqlPerformanceDesign/Operation/dbStructuralStatus?"+param;
	
}

function selectTab4(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";
	console.log("param :",param);
	$("#operationDesignTab").tabs('select',3);
	
	console.log("operationDesignIF4.src :["+document.getElementById('operationDesignIF4').src+"]");
	document.getElementById('operationDesignIF4').src="/sqlPerformanceDesign/Operation/dbStructuralHistory?"+param;
	
}