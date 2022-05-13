$(document).ready(function(){
	/** 솔루션프로그램관리 탭 1, 2 */
	$("#solutionProgramMngTab").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
				console.log("solutionProgramMngTabIF1.src :["+document.getElementById('solutionProgramMngIF1').src+"]");
				var collectSQLIndexDesignIF1Src = document.getElementById('solutionProgramMngIF1').src;
				if(collectSQLIndexDesignIF1Src == undefined || collectSQLIndexDesignIF1Src == ""){
					document.getElementById('solutionProgramMngIF1').src="/SolutionProgramMng/ProgramList";
				}
			}else if(index == 1){
				console.log("solutionProgramMngTabIF2.src :["+document.getElementById('solutionProgramMngIF2').src+"]");
				var collectSQLIndexDesignIF2Src = document.getElementById('solutionProgramMngIF2').src;
				if(collectSQLIndexDesignIF2Src == undefined || collectSQLIndexDesignIF2Src == ""){
					document.getElementById('solutionProgramMngIF2').src="/SolutionProgramMng/ProgramRule";
				}
			}
		}		
	});
});

function selectTab2(param){
//	var menuParam = "dbid="+$("#dbid").val()+"&exec_seq="+$("#exec_seq").val()+"&access_path_type=VSQL";	
	console.log("param :",param);
	$("#solutionProgramMngTab").tabs('select',1);
	
	console.log("solutionProgramMngIF2.src :["+document.getElementById('solutionProgramMngIF2').src+"]");
	document.getElementById('solutionProgramMngIF2').src="/solutionProgramMng/solutionProgramMng/programRule?"+param;
	
}