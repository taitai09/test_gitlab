package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsHistSqlText;
import omc.spop.model.PlanCompareResult;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.TuningTargetSql;

public interface DBChangePerformanceImpactAnalysisForTiberoServiceDao {
//	AutoPerformanceCompareBetweenDbServiceMapper

	public List<SQLAutoPerformanceCompare> getSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	
	public List<SQLAutoPerformanceCompare> getSqlPerfDetailInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> getSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int insertSqlPerformanceInfo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int updateSqlPerformanceInfo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int countPerformanceRecord( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int updateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int deleteSqlAutoPerformanceTarget(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformanceResult(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformanceError(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformancePlanTable(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformanceStat(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformancePlan(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int insertSqlAutoPerformanceTarget(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public SQLAutoPerformanceCompare getMaxSqlCheckId(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadPerformanceResultList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadPerfResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<LinkedHashMap<String, Object>> excelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	
	public List<LinkedHashMap<String, Object>> loadPerformanceResultListExcel(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutomaticPerformanceCheck> loadPerfChartData(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int updateAutoPerfChkIsNull( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int getPerformanceResultCount( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int getTargetEqualCount( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public TuningTargetSql selectTuningTargetSql( TuningTargetSql temp );

	public void insertTuningTargetSql( TuningTargetSql temp );

	public void deleteTuningTargetSqlBind( String tuningNo );

	public String getBindSetSeq( String tuningNo );

	public void insertTuningTargetSqlBindFromVsqlBindCapture( TuningTargetSql temp );

	public List<SQLAutoPerformanceCompare> loadTuningPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadTuningPerformanceDetailList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadPerformanceResultListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<LinkedHashMap<String, Object>> excelTuningDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<LinkedHashMap<String, Object>> excelTuningDetailDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int getTuningTargetCount( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerformanceChk(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int forceUpdateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int deleteSqlAutoPerformanceBind(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int insertTuningTargetSqlBindNew( TuningTargetSql temp );

	public List<SQLAutoPerformanceCompare> getTobeSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationNorthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	
	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int insertTuningSqlAutoPerfChk(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int insertTuningSqlAutoPerfChkTarget(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int countTuningEndTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<LinkedHashMap<String, Object>> excelNorthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	
	public List<LinkedHashMap<String, Object>> excelSouthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare);

	public void insertTobeTuningTargetSql(TuningTargetSql temp);

	public void insertTobeTuningTargetSqlBind( TuningTargetSql temp );

	public List<SQLAutoPerformanceCompare> getOperationDB(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<LinkedHashMap<String, Object>> operationExcelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadPerfCheckResultList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadExplainBindValueNew(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadAfterSelectTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadAfterDMLTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int countTuningExecuteTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteTableCHGPerfTargetSql(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	
	public int insertTableCHGPerfChkTarget(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int insertSqlAutoPerformanceTargetForAWR(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public void insertTuningTargetSqlBindForVerify( TuningTargetSql temp );
	
	public List<SQLAutoPerformanceCompare> loadPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> loadExplainInfoBindValue(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlAutoPerfChkBindPlan(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public SQLAutoPerformanceCompare getVeritySqlAutoPerfCheckId(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public List<SQLAutoPerformanceCompare> loadPerformanceResultListSqlIdAndHashValue(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare);

	public String getSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int updateSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int deleteSqlMemo( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int getDuplicateSQLTuningTargetByProjectAndDBCount( TuningTargetSql temp );

	public int getDuplicateSQLTuningTargetByProjectCount( TuningTargetSql temp );

	public List<String> getTobeSQLPlan( TuningTargetSql temp );
//	public List<OdsHistSqlText> getTobeSQLPlan( TuningTargetSql temp );

	public int insertPlanInSQLTuning(TuningTargetSql temp);

	public int getTobeSQLPlanCnt(TuningTargetSql temp);

	public List<SQLAutomaticPerformanceCheck> loadOriginalDb(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public List<String> sqlTextPlanListForTibero(PlanCompareResult planCompareResult);

	public List<String> sqlTextPlanOptionForTibero(PlanCompareResult planCompareResult);

	public String getTiberoSqlId( PlanCompareResult planCompareResult );

}
