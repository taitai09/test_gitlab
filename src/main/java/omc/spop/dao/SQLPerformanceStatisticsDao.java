package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.SQLPerformanceStatistics;

/***********************************************************
 * 2019.08.28 	임호경 	최초작성
 **********************************************************/

public interface SQLPerformanceStatisticsDao {

	List<SQLPerformanceStatistics> getChartTopSqlTrendStatus(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getChartCpuUsage(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridModule(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridAction(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridParsingSchema(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<SQLPerformanceStatistics> getGridTopSqlResultList(SQLPerformanceStatistics sqlPerformanceStatistics);

	List<LinkedHashMap<String, Object>> getGridTopSqlResultListByExcelDown(SQLPerformanceStatistics sqlPerformanceStatistics);

}
