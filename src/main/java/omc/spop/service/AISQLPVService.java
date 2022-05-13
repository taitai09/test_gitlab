package omc.spop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.VsqlText;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationService
 **********************************************************/

public interface AISQLPVService {
	List<SQLAutoPerformanceCompare> getSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	void insertSqlPerformanceInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception;
	
	void updateSqlPerformanceInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception;
	
	void checkUnfinishedCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception;
	
	void deleteSqlPerfInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadSummaryData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadIndexList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadSqlListByIndex(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadAfterSelectTextPlanListAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SqlGrid> loadAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SqlGrid> loadNoExecAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadResultCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	int loadNumberOfSearch(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> loadPerfChartData(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutoPerformanceCompare> loadResultList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	
	boolean loadSqlListExcel(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	boolean loadResultListExcel(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	List<VsqlText> loadSqlIdList(VsqlText vsqlText) throws Exception;

	List<Sqls> sqlStateTrend(Sqls sqls) throws Exception;
}
