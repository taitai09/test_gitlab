package omc.spop.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.StandardComplianceRateTrend;

public interface SQLStandardsService {
	List<SQLStandards> loadAllIndex(HashMap<String, String> map) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadQualityTable(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownQualityTable(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadProjectQualityTable(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadNonCompliantCode(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownNonCompliantCode(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> maintainQualityCheckIndicator(SQLStandards sqlStandards) throws Exception;
	
	Result saveMaintainQualityCheckIndicator(SQLStandards sqlStandards) throws Exception;
	
	int deleteMaintainQualityCheckIndicator(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckIndicator(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> getQtyChkIdtCd(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> getQtyChkIdtCd2(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> maintainQualityCheckSql(SQLStandards sqlStandards) throws Exception;
	
	Result saveMaintainQualityCheckSql(SQLStandards sqlStandards);
	
	int deleteMaintainQualityCheckSql(SQLStandards sqlStandards) throws Exception;
	
	List<Map<String, Object>> excelDownMaintainQualityCheckSql(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> getQtyChkIdtCdFromException(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> maintainQualityCheckException(SQLStandards sqlStadards) throws Exception;
	
	Result saveMaintainQualityCheckException(SQLStandards sqlStandards) throws Exception;
	
	int deleteMaintainQualityCheckException(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckException(SQLStandards sqlStandards) throws Exception;
	
	Result excelUploadMaintainQualityCheckException(MultipartFile file) throws Exception;
	
	List<SQLStandards> loadQualityReviewWork(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> loadSqlCount(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> loadWorkStatus(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> loadErrorMessage(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownQualityReviewWork(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> checkQualityReviewWorkInRun(SQLStandards sqlStandards) throws Exception;
	
	Result forceProcessingCompleted(SQLStandards sqlStandards) throws Exception;
	
	void qualityReviewOperation(SQLStandards temporaryModel) throws Exception;
	
	void qualityReviewOperationSync(SQLStandards temporaryModel) throws Exception;
	
	void loadPreProcess(SQLStandards temporaryModel) throws Exception;
	
	SQLStandards decryptRule(StandardComplianceRateTrend scrt) throws Exception;
	
	SQLStandards decryptRuleProject(StandardComplianceRateTrend scrt) throws Exception;
	
	int updateCheckMainProcess(SQLStandards sqlStandards) throws Exception;
	
	void summaryTask(SQLStandards temporaryModel, SQLStandards sqlStandards) throws Exception;
	
	void summaryErrorTask(SQLStandards temporaryModel, SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> checkMainProcess(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> checkProc(SQLStandards sqlStandards) throws Exception;
	
	int updateProc(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> getUnfinishedCheckProc(SQLStandards sqlStandards) throws Exception;
	
	int updatingCompleteCheckMainProcess(SQLStandards sqlStandards) throws Exception;
	
	int updatingForceCompleteCheckMainProcess(SQLStandards sqlStandards) throws Exception;
	
	List<JobSchedulerBase> loadSchedulerList(SQLStandards sqlStandards);
	
	List<JobSchedulerBase> loadSchedulerListTest(SQLStandards sqlStandards);

	List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception;
	
	List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase);
	
	List<LinkedHashMap<String, Object>> loadNonStdSqlList(SQLStandards sqlStandards) throws Exception;
	
	int saveSetting(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	int modifySetting(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	int deleteScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);
	
	List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception;
	
	int forcedCompletion(SQLStandards sqlStandards);

	boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception;

	List<LinkedHashMap<String, Object>> loadResultList(SQLStandards sqlStandards) throws Exception;
	
	boolean isUpdateY(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws ParseException;
}
