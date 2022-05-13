package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.SQLStandards;

/***********************************************************
 * 2021.09.15	황예지	최초작성
 **********************************************************/

public interface ExecSqlStdChkDao {	
	public List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase);
	
	public List<LinkedHashMap<String, Object>> schedulerExcelDownload(JobSchedulerBase jobSchedulerBase);
	
	public int checkExistScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);
	
	public List<JobSchedulerBase> loadSqlStdChkRslt(SQLStandards sqlStandards);

	public List<SQLStandards> loadExceptionList(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> loadExceptionExcelDownload(SQLStandards sqlStandards);

	public int countException(SQLStandards sqlStandards);

	public int saveException(SQLStandards sqlStandards);

	public List<SQLStandards> loadQtyIdxByProject(String project_id);
	
	public List<JobSchedulerBase> loadSchedulerList(Map<String, String> param);
	
	public List<LinkedHashMap<String, Object>> loadSqlFullText(SQLStandards sqlStandards);
}
