package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;

/***********************************************************
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SystemOperationStatusDao {	
	public List<JobSchedulerExecLog> performSchedulerList(JobSchedulerExecLog jobSchedulerExecLog);
	
	public List<JobSchedulerExecDetailLog> performSchedulerDetailList(JobSchedulerExecDetailLog jobSchedulerExecDetailLog);

	public List<LinkedHashMap<String, Object>> performSchedulerListByExcelDown(
			JobSchedulerExecDetailLog jobSchedulerExecDetailLog);
}
