package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.SelftunPlanTable;

public interface PerformanceCheckMngDao {

	List<PerformanceCheckMng> getPerformanceCheckMngList(PerformanceCheckMng performanceCheckMng);
	
	List<PerformanceCheckMng> getPerformanceCheckMngListExpc(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> getPerfCheckStep(PerformanceCheckMng performanceCheckMng);

	List<LinkedHashMap<String, Object>> getPerformanceCheckMngList4Excel(PerformanceCheckMng performanceCheckMng);
	
	List<LinkedHashMap<String, Object>> getPerformanceCheckMngListExpcExcel(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerformanceCheckStageBasicInfo(PerformanceCheckMng performanceCheckMng);
	
	List<PerformanceCheckMng> getPerformanceCheckStageBasicInfoList(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> getPerformanceCheckStageList(PerformanceCheckMng performanceCheckMng);

	List<LinkedHashMap<String, Object>> getPerformanceCheckStageList4Excel(PerformanceCheckMng performanceCheckMng);

	int getTestMissCnt(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckResultCount(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getLastPerfCheckStepYn(PerformanceCheckMng performanceCheckMng);

	String getPerfCheckResultDivCd(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkStepExec(PerformanceCheckMng performanceCheckMng);

	// 성능점검 단계별 예외(영구/한시) 대상 저장(Snapshot)
	int insertDeployPerfChkTrgtExcptPgm(PerformanceCheckMng performanceCheckMng);

	int updatePerfTestCompleteYnToN(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChk(PerformanceCheckMng performanceCheckMng);

	int insertDeployPerfChkStatusHistory(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckResultBasicInfo(PerformanceCheckMng performanceCheckMng);
	
	PerformanceCheckMng getPerfCheckResultBasicInfoEspc(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> getPerfCheckResultList(PerformanceCheckMng performanceCheckMng);
	
	List<PerformanceCheckMng> getPerfCheckResultListEspc(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckAllPgm(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng selectDeployPerfChkStepTestDbList(PerformanceCheckMng performanceCheckMng) throws Exception;

	PerformanceCheckMng selectDefaultParsingSchemaInfo(PerformanceCheckMng performanceCheckMng) throws Exception;

	PerformanceCheckMng selectDeployCheckStatus(PerformanceCheckMng performanceCheckMng) throws Exception;

	int updateDeployPerfChkStepExecTotalCnt(PerformanceCheckMng performanceCheckMng) throws Exception;

	String selectMaxProgramExecuteTmsPlus1(PerformanceCheckMng performanceCheckMng) throws Exception;

	int insertDeployPerfChkResult(PerformanceCheckMng performanceCheckMng) throws Exception;

	int insertDeployPerfChkExecBind(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> getDeployPerfChkExeHistList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(PerformanceCheckMng performanceCheckMng)
			throws Exception;
	
	List<PerformanceCheckMng> selectDeployPerfChkDetailResultListEspc(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	PerformanceCheckMng selectPerfCheckResultBasisWhy(PerformanceCheckMng performanceCheckMng) throws Exception;

	/** 수행회차 */
	List<PerformanceCheckMng> selectProgramExecuteTmsList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindListPop(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindValue(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkAllPgmList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectVsqlBindCaptureList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getPerfCheckResultList4Excel(PerformanceCheckMng performanceCheckMng);
	
	List<LinkedHashMap<String, Object>> getPerfCheckResultListEspcExcel(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> selectImprovementGuideList(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkResultNotify(PerformanceCheckMng performanceCheckMng);

	int updateLastPerfCheckStepId(PerformanceCheckMng performanceCheckMng);

	List<SelftunPlanTable> getDeployPerfChkPlanTable(PerformanceCheckMng performanceCheckMng);

	List<SelftunPlanTable> getDeployPerfSqlPlan(PerformanceCheckMng performanceCheckMng);

	String getDefaultPagingCnt(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkStepExec2(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChk2(PerformanceCheckMng performanceCheckMng);

	int insertDeployPerfChkExcptHistory(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkExcptRequest2(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkAllPgm(PerformanceCheckMng performanceCheckMng);

	int insertDeployPerfChkTrgtExcptPgm2(PerformanceCheckMng performanceCheckMng);

	int deleteDeployPerfChkTrgtExcptPgm2(PerformanceCheckMng performanceCheckMng);

	int updateCheckResultAncYn(PerformanceCheckMng performanceCheckMng);

	int deleteDeployPerfChkTrgtExcptPgm(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckIdFromDeployId(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkResult(PerformanceCheckMng performanceCheckMng);

	int getProgramCnt(PerformanceCheckMng performanceCheckMng);

	int updateDeployPerfChkStepExecAll(PerformanceCheckMng performanceCheckMng);

	String getUserId();

	String selectDbid(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng selectDeployPerfSqlStat(PerformanceCheckMng performanceCheckMng);
	
	PerformanceCheckMng selectDeployPerfSqlStatEspc(PerformanceCheckMng performanceCheckMng);

	String selectParsingSchemaName(PerformanceCheckMng performanceCheckMng);
	
	String selectParsingSchemaNameEspc(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> selectBindVar(PerformanceCheckMng performanceCheckMng);
	
	List<PerformanceCheckMng> selectBindVarEspc(PerformanceCheckMng performanceCheckMng);

	String getUserWrkjob(String user_id);

	int perfChkRequestTuningDupChk(PerformanceCheckMng performanceCheckMng);
	
	int perfChkRequestTuningDupChkEspc(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPagingYnCnt(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> getInspectSqlList(PerformanceCheckMng performanceCheckMng);

	List<PerformanceCheckMng> getInspectResultDetail(PerformanceCheckMng performanceCheckMng);
	
	List<PerformanceCheckMng> selectImproveGuide(PerformanceCheckMng performanceCheckMng);
	
	PerformanceCheckMng selectSqlInfo(PerformanceCheckMng performanceCheckMng);
	
	void reExecution(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckAllPgmEspc(PerformanceCheckMng performanceCheckMng);

	List<String> getImprbExecPlan(PerformanceCheckMng performanceCheckMng);
}
