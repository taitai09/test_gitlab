package omc.spop.dao;

import java.util.HashMap;

import omc.spop.model.DeployPerfChkIndc;

/***********************************************************
 * 2019.03.04 banks 최초작성
 **********************************************************/

public interface PerformanceCheckAutoExecuteDao {

	DeployPerfChkIndc selectDeployPerfChkInfo(DeployPerfChkIndc deployPerfChkIndc);

	int selectMaxProgramExecuteTms(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkResult(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkExecBind(DeployPerfChkIndc deployPerfChkIndc);
	
	String selectParsingSchemaName(DeployPerfChkIndc deployPerfChkIndc) throws Exception;
	
	void sp_Spop_Deploy_Perf_Check_Auto(DeployPerfChkIndc deployPerfChkIndc)throws Exception;
	
}
