package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.VsqlText;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationDao
 **********************************************************/

public interface AISQLPVDao {
	List<SQLAutoPerformanceCompare> getSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);

	List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int getExistCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SQLAutoPerformanceCompare> loadSummaryData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);

	List<SQLAutoPerformanceCompare> loadIndexList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SQLAutoPerformanceCompare> loadSqlListByIndex(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SqlGrid> loadAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SqlGrid> loadNoExecAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SQLAutoPerformanceCompare> loadResultCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int loadNumberOfSearch(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<SQLAutomaticPerformanceCheck> loadPerfChartData(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	List<SQLAutoPerformanceCompare> loadResultList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<LinkedHashMap<String, Object>> loadResultListExcel(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	List<VsqlText> loadSqlIdList(VsqlText vsqlText);

	List<Sqls> sqlStateTrend(Sqls sqls);
	
	List<SQLAutoPerformanceCompare> getDeleteTargets(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxAdAsisIndex(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxAdColNdv(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxAdProcLog(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxAdRecommendIndex(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxDbWorkDeatil(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxDbWork(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteIdxAdMst(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteAccPathIndexDesign(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteAccPath(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	
	int deleteTableChgPerfChkTargetSql(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
}