package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;

/***********************************************************
 * 2019.12.26	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface ExamineOpenPOPService {
	String getEcho(String ip, int port);
	
	String getPing(String ip, int port);
	
	List<LinkedHashMap<String, Object>> rtrvSchedulerStatusHistory(JobSchedulerExecLog jobSchedulerExecLog) throws Exception;
	
	/** 스케쥴러 수행내역 리스트 */
	List<JobSchedulerExecLog> performSchedulerList(JobSchedulerExecLog jobSchedulerExecLog) throws Exception;
	
	/** 스케쥴러 수행내역 상세 리스트 */
	List<JobSchedulerExecDetailLog> performSchedulerDetailList(JobSchedulerExecDetailLog jobSchedulerExecDetailLog) throws Exception;

	List<LinkedHashMap<String, Object>> performSchedulerListByExcelDown(JobSchedulerExecLog jobSchedulerExecLog);
	
	List<LinkedHashMap<String, Object>> rtrvAgentStatus() throws Exception;
	
	List<LinkedHashMap<String, Object>> instanceList() throws Exception;
	
	String getGatherServer(String ip, int port);
	
	List<LinkedHashMap<String, Object>> rtrvAgentStatusV2() throws Exception;
	
	List<LinkedHashMap<String, Object>> instanceListV2() throws Exception;
}
