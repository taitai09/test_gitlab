package omc.spop.service;

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

public interface ExecSqlStdChkService {
	List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase);
	
	List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception;
	
	int checkExistScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	int insertSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	int updateSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	int deleteScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail);
	
	List<JobSchedulerBase> loadSqlStdChkRslt(SQLStandards sqlStandards);

	boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadQualityStatus(SQLStandards sqlStandards) throws Exception;
	
	List<JobSchedulerBase> loadSchedulerList(Map<String, String> param) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadNonStdSql(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadSqlFullText(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadStdComplianceState(SQLStandards sqlStandards) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadCountByIndex(SQLStandards sqlStandards) throws Exception;

	List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> loadExceptionList(SQLStandards sqlStadards) throws Exception;

	List<LinkedHashMap<String, Object>> exceptionExcelDown(SQLStandards sqlStandards) throws Exception;
	
	List<SQLStandards> getQtyChkIdxCd(SQLStandards sqlStandards) throws Exception;
	
	Result saveException(SQLStandards sqlStandards) throws Exception;
	
	Result modifyException(SQLStandards sqlStandards) throws Exception;
	
	int deleteException(SQLStandards sqlStandards) throws Exception;
	
	Result excelUpload(MultipartFile file) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadResultCnt(SQLStandards sqlStandards) throws Exception;
}
