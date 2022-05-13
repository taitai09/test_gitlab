package omc.spop.dao;

import java.util.List;

import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.DeployPerfCheck;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface PreDeploymentCheckDao {	
	public List<BeforeAccidentCheck> sourceCheckList(BeforeAccidentCheck beforeAccidentCheck);
	
	public BeforeAccidentCheck sourceCheckView(BeforeAccidentCheck beforeAccidentCheck);
	
	public void updateSourceCheck(BeforeAccidentCheck beforeAccidentCheck);
	
	public List<DeployPerfCheck> applicationCheckList(DeployPerfCheck deployPerfCheck);
	
	public List<DeployPerfCheck> applicationDetailCheckList(DeployPerfCheck deployPerfCheck);
	
	public List<DeployPerfCheck> applicationDBIOCheckList(DeployPerfCheck deployPerfCheck);
	
	public List<DeployPerfCheck> dbioCheckList(DeployPerfCheck deployPerfCheck);
	
	public List<DeployPerfCheck> dbioDetailCheckList(DeployPerfCheck deployPerfCheck);
	
	public int insertDeployPerfCheck(DeployPerfCheck deployPerfCheck);
	
	public void insertDeployAppPerfStat(DeployPerfCheck deployPerfCheck);
	
	public void insertDeployDBIOPerfStat(DeployPerfCheck deployPerfCheck);

	public void applicationRequestCheck(DeployPerfCheck deployPerfCheck);

	public void dbioRequestCheck(DeployPerfCheck deployPerfCheck);
}
