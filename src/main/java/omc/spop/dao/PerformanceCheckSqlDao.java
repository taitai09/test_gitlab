package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerformanceCheckSql;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface PerformanceCheckSqlDao {
	public List<PerformanceCheckSql> getInitialFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	public List<PerformanceCheckSql> getConditionFinalDistributionDate(PerformanceCheckSql performanceCheckSql);
	
	public List<PerformanceCheckSql> loadPerformanceCheckSql(PerformanceCheckSql performanceCheckSql);
	
	public List<LinkedHashMap<String, Object>> loadPerformanceCheckSqlExcelDown(PerformanceCheckSql performanceCheckSql);
}
