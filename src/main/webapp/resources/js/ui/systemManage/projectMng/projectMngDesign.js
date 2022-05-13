$(document).ready(function(){
	/**프로젝트관리 탭 1, 2, 3, 4 */
	$("#projectMngLTab").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
				var projectMngDesignIF1Src = document.getElementById('projectMngDesignIF1').src;
				if(projectMngDesignIF1Src == undefined || projectMngDesignIF1Src == ""){
					document.getElementById('projectMngDesignIF1').src="/systemManage/projectMng/ProjectMng";
				}
			}else if(index == 1){
				var projectMngDesignIF2Src = document.getElementById('projectMngDesignIF2').src;
				if(projectMngDesignIF2Src == undefined || projectMngDesignIF2Src == ""){
					document.getElementById('projectMngDesignIF2').src="/systemManage/projectMng/ProjectWrkjobMng";
				}
			}else if(index == 2){
				var projectMngDesignIF3Src = document.getElementById('projectMngDesignIF3').src;
				if(projectMngDesignIF3Src == undefined || projectMngDesignIF3Src == ""){
					document.getElementById('projectMngDesignIF3').src="/systemManage/projectMng/ProjectDbMng";
				}
			}else{
				var projectMngDesignIF4Src = document.getElementById('projectMngDesignIF4').src;
				if(projectMngDesignIF4Src == undefined || projectMngDesignIF4Src == ""){
					document.getElementById('projectMngDesignIF4').src="/systemManage/projectMng/ProjectTuningProcessStage";
				}
			}
		}
	});
});