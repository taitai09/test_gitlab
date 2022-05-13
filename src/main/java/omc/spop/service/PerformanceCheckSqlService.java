package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerformanceCheckSql;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface PerformanceCheckSqlService {
	List<PerformanceCheckSql> getInitialFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	List<PerformanceCheckSql> getConditionFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	List<PerformanceCheckSql> loadPerformanceCheckSql(PerformanceCheckSql performanceCheckSql);
	
	/* @throws Exception */
	List<LinkedHashMap<String, Object>> loadPerformanceCheckSqlExcelDown(PerformanceCheckSql performanceCheckSql) throws Exception;
}
