package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.SqlsDetail;
import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface SqlsDao {
	public List<Sqls> loadSqls(Sqls sqls);
	
	public List<LinkedHashMap<String, Object>> excelDown(Sqls sqls);
	
	public int perfChkRequestTuningDupChk(TuningTargetSql tuningTargetSql);
	
	public int insertTuningTargetSqlBindFromVsqlBindCapture(Sqls sqls);
	
	public List<Sqls> beforeOperationPerformance(Sqls sqls);
	
	public List<Sqls> performanceCheck(Sqls sqls);
	
	public List<Sqls> afterDistributionOperationPerformance(Sqls sqls);
	
	public List<Sqls> sqlTextPerformanceCheck(Sqls sqls);
	
	public List<Sqls> sqlBindPerformanceCheck(Sqls sqls);
	
	public List<Sqls> sqlPlanPerformanceCheck(Sqls sqls);
	
	public List<Sqls> sqlTextAll(Sqls sqls);
	
	public List<Sqls> sqlBindOperation(Sqls sqls);
	
	public List<Sqls> sqlPlanOperation(Sqls sqls);
	
	public List<Sqls> bigTableThresholdCnt(Sqls sqls);
	
	public Sqls loadPerfCheckAllPgm(Sqls sqls);
	
	public List<Sqls> sqlStatTrend(Sqls sqls);
	
	public void insertTuningTargetSql(TuningTargetSql tuningTargetSql);
	
	public Sqls selectParsingSchemaName(Sqls sqls);
	
	public Sqls selectSqlText(Sqls sqls);
	
	public List<Sqls> performanceCheckResult(Sqls sqls) throws Exception;
	
	public List<Sqls> performanceCheckResultException(Sqls sqls) throws Exception;
	
	public List<SqlsDetail> bindValueListAll(SqlsDetail sqlsDetail) throws Exception;
	
	public List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid) throws Exception;
	
	public List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText) throws Exception;
	
	public List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid) throws Exception;
	
	public List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat) throws Exception;
	
	public List<Sqls> sqlPerformHistoryList(Sqls sqls) throws Exception;

	public List<Sqls> loadAutoSqls( Sqls sqls ) throws Exception;

	public List<Sqls> autoPerformanceCheck( Sqls sqls ) throws Exception;

	public List<Sqls> autoPerformanceCheckResult( Sqls sqls ) throws Exception;

	public List<LinkedHashMap<String, Object>> excelAutoDown(Sqls sqls) throws Exception;
}
