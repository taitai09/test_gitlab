package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.Sqls;

public interface PerfInspectMngService {

	List<PerformanceCheckMng> getPerfCheckStep(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> getInspectionList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getInspectionListExcelDown(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> getInspectionStep(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getInspectionStepExcelDown(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	/** 성능점검결과 기본정보 */
	PerformanceCheckMng getPerfCheckResultBasicInfo(PerformanceCheckMng performanceCheckMng) throws Exception;

	/** 배포성능점검전체프로그램내역 */
	PerformanceCheckMng getPerfCheckAllPgm(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	List<PerformanceCheckMng> selectImprovementGuideList(PerformanceCheckMng performanceCheckMng) throws Exception;

	PerformanceCheckMng getPerfCheckIdFromDeployId(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result perfChkRsltNoti(PerformanceCheckMng performanceCheckMng, String pageName) throws Exception;

	List<PerformanceCheckMng> getPerfCheckResultList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<LinkedHashMap<String, Object>> getPerfCheckResultList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	int perfChkRequestTuningDupChkEspc(PerformanceCheckMng performanceCheckMng) throws Exception;

	int perfChkRequestTuning(PerformanceCheckMng performanceCheckMng, HttpServletRequest req) throws Exception;

	List<PerformanceCheckMng> getInspectSqlList(PerformanceCheckMng performanceCheckMng) throws Exception;

	List<PerformanceCheckMng> getInspectResultDetail(PerformanceCheckMng performanceCheckMng);

	List<Sqls> getExecutionPlan(Sqls sqls);

	List<PerformanceCheckMng> selectDeployPerfChkExecBindValue(PerformanceCheckMng performanceCheckMng)
			throws Exception;

	PerformanceCheckMng getSqlInfo(PerformanceCheckMng performanceCheckMng);

	Result inspectForceFinish(PerformanceCheckMng performanceCheckMng) throws InterruptedException;

	Result inspectRsltNoti(PerformanceCheckMng performanceCheckMng, String pageName) throws Exception;

	int getProgramCnt(PerformanceCheckMng performanceCheckMng);

	PerformanceCheckMng getPerfCheckResultCount(PerformanceCheckMng performanceCheckMng) throws Exception;

	Result performanceCheckComplete(PerformanceCheckMng performanceCheckMng) throws InterruptedException;

	PerformanceCheckMng selectDeployCheckStatus(PerformanceCheckMng performanceCheckMng) throws Exception;

	boolean reExecute(PerformanceCheckMng performanceCheckMng);
}
