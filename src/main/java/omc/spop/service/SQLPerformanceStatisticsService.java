package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Result;
import omc.spop.model.SQLPerformanceStatistics;

/***********************************************************
 * 2019.08.28 	임호경 	최초작성
 **********************************************************/

public interface SQLPerformanceStatisticsService {

	Result getChartTopSqlTrendStatus(SQLPerformanceStatistics sqlPerformanceStatistics) throws Exception;

	Result getChartCpuUsage(SQLPerformanceStatistics sqlPerformanceStatistics) throws Exception;

	List<SQLPerformanceStatistics> getGridModule(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridAction(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridParsingSchema(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridTopSqlResultList(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<LinkedHashMap<String, Object>> getGridTopSqlResultListByExcelDown(SQLPerformanceStatistics sqlPerformanceStatistics);
	
}
