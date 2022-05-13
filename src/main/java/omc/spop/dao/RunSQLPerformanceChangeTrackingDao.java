package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ExceptionHandlingSql;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.SqlPerformanceTraceStatusChart;

/***********************************************************
 * 2021.08.25	이재우	최초작성
 **********************************************************/

public interface RunSQLPerformanceChangeTrackingDao {
	public List<PerformanceCheckSql> getInitialFinalDistributionDate( PerformanceCheckSql performanceCheckSql );
	
	public List<PerformanceCheckSql> getConditionFinalDistributionDate( PerformanceCheckSql performanceCheckSql );
	
	public List<PerformanceCheckSql> loadPerformanceVerifySql( PerformanceCheckSql performanceCheckSql );
	
	public List<LinkedHashMap<String, Object>> loadPerformanceVerifySqlExcelDown( PerformanceCheckSql performanceCheckSql );

	public List<ExceptionHandlingSql> loadExceptionHandlingSql( ExceptionHandlingSql exceptionHandlingSql );

	public List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown( ExceptionHandlingSql exceptionHandlingSql );

	public List<SqlPerformanceTraceStatusChart> chart( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart );

	public List<SqlPerformanceTraceStatusChart> chartPerformance01( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart );

	public List<SqlPerformanceTraceStatusChart> chartPerformance02( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart );

	public List<SqlPerformanceTraceStatusChart> chartException01( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart );

	public List<SqlPerformanceTraceStatusChart> chartException02( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart );
}
