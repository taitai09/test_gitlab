package omc.spop.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.mqm.model.QualityStdInfo;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.SQLStandards;

public interface SQLStandardsDao {	
	public List<SQLStandards> getQtyChkSQL(SQLStandards sqlStandards);
	
	public List<SQLStandards> getQtyChkSQL_List(List<String> qtyChkIdtCdList);
	
	public List<SQLStandards> getProjectQtyChkSQL(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> loadQualityTable(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> excelDownQualityTable(SQLStandards sqlStandards);
	
	public List<SQLStandards> maintainQualityCheckIndicator(SQLStandards sqlStandards);
	
	public int countMaintainQualityCheckIndicator(HashMap<String, Object> param);
	
	public int saveMaintainQualityCheckIndicator(SQLStandards sqlStandards);
	
	public int deleteMaintainQualityCheckIndicator(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckIndicator(SQLStandards sqlStandards);
	
	public List<SQLStandards> getQtyChkIdtCd(SQLStandards sqlStandards);
	
	public List<SQLStandards> getQtyChkIdtCd2(SQLStandards sqlStandards);
	
	public List<SQLStandards> maintainQualityCheckSql(SQLStandards sqlStandards);
	
	public int countMaintainQualityCheckSql(HashMap<String, Object> param);
	
	public int insertMaintainQualityCheckSql(HashMap<String, Object> param);
	
	public int saveMaintainQualityCheckSql(HashMap<String, Object> param);
	
	public int deleteMaintainQualityCheckSql(SQLStandards sqlStandards);
	
	public List<Map<String, Object>> excelDownMaintainQualityCheckSql(SQLStandards sqlStandards);
	
	public List<SQLStandards> getQtyChkIdtCdFromException(SQLStandards sqlStandards);
	
	public List<SQLStandards> maintainQualityCheckException(SQLStandards sqlStandards);
	
	public int countMaintainQualityCheckException(HashMap<String, Object> param);
	
	public int saveMaintainQualityCheckException(SQLStandards sqlStandards);
	
	public int deleteMaintainQualityCheckException(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckException(SQLStandards sqlStandards);
	
	public List<SQLStandards> loadQualityReviewWork(SQLStandards sqlStandards);
	
	public List<SQLStandards> loadSqlCount(SQLStandards sqlStandards);
	
	public List<SQLStandards> loadWorkStatus(SQLStandards sqlStandards);
	
	public List<SQLStandards> loadErrorMessage(SQLStandards sqlStandards);
	
	public List<LinkedHashMap<String, Object>> excelDownQualityReviewWork(SQLStandards sqlStandards);
	
	public List<SQLStandards> checkQualityReviewWorkInRun(SQLStandards sqlStandards);
	
	public int forceProcessingCompleted1(SQLStandards sqlStandards);
	
	public int forceProcessingCompleted2(SQLStandards sqlStandards);
	
	public int forceProcessingCompleted3(SQLStandards sqlStandards);
	
	public int updateCheckMainProcess(SQLStandards sqlStandards);
	
	public List<SQLStandards> checkMainProcess(SQLStandards sqlStandards);
	
	public int updateSQL(SQLStandards sqlStandards);
	
	public List<SQLStandards> checkProc(SQLStandards sqlStandards);
	
	public int updateProc(SQLStandards sqlStandards);
	
	public List<SQLStandards> getUnfinishedCheckProc(SQLStandards sqlStandards);
	
	public int updatingCompleteCheckMainProcess(SQLStandards sqlStandards);
	
	public int updatingForceCompleteCheckMainProcess(SQLStandards sqlStandards);

	public List<JobSchedulerBase> loadSchedulerList(SQLStandards sqlStandards);
	
	public List<QualityStdInfo> loadIndexList(SQLStandards sqlStandards);
	
	public int forcedCompletion(SQLStandards sqlStandards);
	
	public List<JobSchedulerBase> loadSchedulerListTest(SQLStandards sqlStandards);

	public List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase);

	public int saveSetting(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public int modifySetting(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public int deleteScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase);

	public List<SQLStandards> loadQtyIdxByProject(String project_id);
	
	public List<LinkedHashMap<String, Object>> loadResultList(SQLStandards sqlStandards);
	
	public int insertSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public int updateSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public int deleteSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public List<SQLStandards> loadAllIndex(HashMap<String, String> map);

	public List<LinkedHashMap<String, Object>> loadNonStdSqlList(SQLStandards sqlStandards);
}