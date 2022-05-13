package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;

/***********************************************************
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SystemOperationStatusService {
	/** 스케쥴러 수행내역 리스트 */
	List<JobSchedulerExecLog> performSchedulerList(JobSchedulerExecLog jobSchedulerExecLog) throws Exception;
	
	/** 스케쥴러 수행내역 상세 리스트 */
	List<JobSchedulerExecDetailLog> performSchedulerDetailList(JobSchedulerExecDetailLog jobSchedulerExecDetailLog) throws Exception;

	List<LinkedHashMap<String, Object>> performSchedulerListByExcelDown(JobSchedulerExecDetailLog jobSchedulerExecDetailLog);	
}
