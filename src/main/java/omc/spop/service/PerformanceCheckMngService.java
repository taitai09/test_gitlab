package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunPlanTable;

public interface PerformanceCheckMngService {

	List<PerformanceCheckMng> getPerfCheckStep(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> getPerformanceCheckMngList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getPerformanceCheckMngList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	PerformanceCheckMng getPerformanceCheckStageBasicInfo(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> getPerformanceCheckStageList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getPerformanceCheckStageList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	int getTestMissCnt(PerformanceCheckMng performanceCheckMng) throws Exception;

	PerformanceCheckMng getPerfCheckResultCount(PerformanceCheckMng performanceCheckMng) throws Exception;

	String getPerfCheckResultDivCd(PerformanceCheckMng performanceCheckMng) throws Exception;

	int updateDeployPerfChkStepExec(PerformanceCheckMng performanceCheckMng) throws Exception;

	int updatePerfTestCompleteYnToN(PerformanceCheckMng performanceCheckMng) throws Exception;

	/** 성능점검결과 기본정보 */
	PerformanceCheckMng getPerfCheckResultBasicInfo(PerformanceCheckMng performanceCheckMng) throws Exception;

	/** 성능점검결과 목록 조회 */
	List<PerformanceCheckMng> getPerfCheckResultList(PerformanceCheckMng performanceCheckMng) throws Exception;

	/** 배포성능점검전체프로그램내역 */
	PerformanceCheckMng getPerfCheckAllPgm(PerformanceCheckMng performanceCheckMng) throws Exception;

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

	PerformanceCheckMng selectPerfCheckResultBasisWhy(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectProgramExecuteTmsList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindListPop(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkExecBindValue(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkAllPgmList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectVsqlBindCaptureList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getPerfCheckResultList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectImprovementGuideList(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result performanceCheckExecuteResult(PerformanceCheckMng performanceCheckMng, HttpServletRequest request)
			throws Exception;

	int updateLastPerfCheckStepId(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<SelftunPlanTable> getDeployPerfChkPlanTable(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<SelftunPlanTable> getDeployPerfSqlPlan(PerformanceCheckMng performanceCheckMng) throws Exception;

	String getDefaultPagingCnt(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result performanceCheckComplete(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result perfChkForceFinish(PerformanceCheckMng performanceCheckMng) throws Exception;

	PerformanceCheckMng getPerfCheckIdFromDeployId(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result perfChkRsltNoti(PerformanceCheckMng performanceCheckMng, String pageName) throws Exception;

	int getProgramCnt(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result perfChkAutoFinish(PerformanceCheckMng performanceCheckMng) throws Exception;

	int perfChkRequestTuningDupChk(PerformanceCheckMng performanceCheckMng) throws Exception;

	int perfChkRequestTuning(PerformanceCheckMng performanceCheckMng, HttpServletRequest request) throws Exception;

	PerformanceCheckMng getPagingYnCnt(PerformanceCheckMng performanceCheckMng) throws Exception;

}
