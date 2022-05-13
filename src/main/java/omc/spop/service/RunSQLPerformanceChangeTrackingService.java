package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ExceptionHandlingSql;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.SqlPerformanceTraceStatusChart;

/***********************************************************
 * 2021.08.25 	이재우	최초작성
 **********************************************************/

public interface RunSQLPerformanceChangeTrackingService {
	List<PerformanceCheckSql> getInitialFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	List<PerformanceCheckSql> getConditionFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	List<PerformanceCheckSql> loadPerformanceVerifySql(PerformanceCheckSql performanceCheckSql);
	
	List<LinkedHashMap<String, Object>> loadPerformanceVerifySqlExcelDown(PerformanceCheckSql performanceCheckSql) throws Exception;
	
	// 예외처리 SQL
	List<ExceptionHandlingSql> loadExceptionHandlingSql(ExceptionHandlingSql exceptionHandlingSql) throws Exception;
	// 예외처리 SQL 엑셀
	List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown(ExceptionHandlingSql exceptionHandlingSql) throws Exception;
	
	List<SqlPerformanceTraceStatusChart> chart( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart ) throws Exception;
	
	List<SqlPerformanceTraceStatusChart> chartPerformance01( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart ) throws Exception;
	
	List<SqlPerformanceTraceStatusChart> chartPerformance02( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart ) throws Exception;
	
	List<SqlPerformanceTraceStatusChart> chartException01( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart ) throws Exception;
	
	List<SqlPerformanceTraceStatusChart> chartException02( SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart ) throws Exception;
}
