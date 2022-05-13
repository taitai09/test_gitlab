package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.SqlsDetail;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface SqlsService {
	List<Sqls> loadSqls(Sqls sqls);
	
	List<LinkedHashMap<String, Object>> excelDown(Sqls sqls) throws Exception;
	
	List<Sqls> beforeOperationPerformance(Sqls sqls);
	
	List<Sqls> performanceCheck(Sqls sqls);
	
	List<Sqls> afterDistributionOperationPerformance(Sqls sqls);
	
	List<Sqls> sqlTextPerformanceCheck(Sqls sqls);
	
	List<Sqls> sqlBindPerformanceCheck(Sqls sqls);
	
	List<Sqls> sqlPlanPerformanceCheck(Sqls sqls);
	
	List<Sqls> sqlTextAll(Sqls sqls);
	
	List<Sqls> sqlBindOperation(Sqls sqls);
	
	List<Sqls> sqlPlanOperation(Sqls sqls);
	
	List<Sqls> bigTableThresholdCnt(Sqls sqls);
	
	Sqls loadPerfCheckAllPgm(Sqls sqls) throws Exception;
	
	List<Sqls> sqlStatTrend(Sqls sqls) throws Exception;
	
	Result insertTuningRequest(Sqls sqls) throws Exception;
	
	List<Sqls> performanceCheckResult(Sqls sqls) throws Exception;
	
	List<Sqls> performanceCheckResultException(Sqls sqls) throws Exception;
	
	List<SqlsDetail> bindValueListAll(SqlsDetail sqlsDetail) throws Exception;
	
	List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid) throws Exception;
	
	List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText) throws Exception;
	
	List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid) throws Exception;
	
	List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat) throws Exception;
	
	List<Sqls> sqlPerformHistoryList(Sqls sqls) throws Exception;

	List<Sqls> loadAutoSqls(Sqls sqls) throws Exception;

	List<LinkedHashMap<String, Object>> excelAutoDown(Sqls sqls) throws Exception;
	
	List<Sqls> autoPerformanceCheck( Sqls sqls ) throws Exception;

	List<Sqls> autoPerformanceCheckResult( Sqls sqls ) throws Exception;
}
