package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import omc.spop.model.PlanCompareResult;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.TuningTargetSql;

/***************************************************
 * 2021.10.28	이재우	최초작성
 ***************************************************/

public interface DBChangePerformanceImpactAnalysisForTiberoService {

	List<SQLAutoPerformanceCompare> getSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> getSqlPerfDetailInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> getSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int insertSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int updateSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int countExecuteTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int countPerformanceRecord(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	@Transactional
	Result updateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;

	SQLAutoPerformanceCompare getMaxSqlCheckId(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadPerformanceResultList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadPerfResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	boolean excelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	List<SQLAutomaticPerformanceCheck> loadPerfChartData(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;

	int updateAutoPerfChkIsNull(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int getPerformanceResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result insertTuningRequest( TuningTargetSql tuningTargetSql ) throws Exception;

	int getTargetEqualCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadTuningPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadTuningPerformanceDetailList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadPerformanceResultListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	boolean excelTuningDetailDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response ) throws Exception;

	boolean excelTuningDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response ) throws Exception;

	List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int getTuningTargetCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int deleteSqlAutoPerformanceChk(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result forceUpdateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;

	List<SQLAutoPerformanceCompare> getTobeSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadTuningBatchValidationNorthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result updateTuningSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;

	int countTuningEndTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result forceUpdateTuningSqlAutoPerf(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;

	boolean excelNorthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response ) throws Exception;
	
	boolean excelSouthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response ) throws Exception;

	List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result insertSelectedTuningTarget( TuningTargetSql tuningTargetSql ) throws Exception;

	List<SQLAutoPerformanceCompare> getOperationDB(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result insertOperationTuningTarget( TuningTargetSql tuningTargetSql ) throws Exception;

	List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadPerfCheckResultList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadExplainBindValueNew(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadAfterSelectTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadAfterDMLTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int countTuningExecuteTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	Result performanceCompareCall(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;

	Result callSqlProfileApply(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutoPerformanceCompare> loadExplainInfoBindValue(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	String getSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	int updateSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;
	
	int deleteSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	Result performanceCompareReCall(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

	List<SQLAutomaticPerformanceCheck> loadOriginalDb(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;

	List<PlanCompareResult> getPlanCompareResultForTibero(PlanCompareResult planCompareResult ) throws Exception;

	String getTiberoSqlId( PlanCompareResult planCompareResult ) throws Exception;
}
