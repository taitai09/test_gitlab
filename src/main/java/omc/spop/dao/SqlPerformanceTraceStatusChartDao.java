package omc.spop.dao;

import java.util.List;

import omc.spop.model.SqlPerformanceTraceStatusChart;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface SqlPerformanceTraceStatusChartDao {
	public List<SqlPerformanceTraceStatusChart> chart(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	public List<SqlPerformanceTraceStatusChart> chartPerformance01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	public List<SqlPerformanceTraceStatusChart> chartPerformance02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	public List<SqlPerformanceTraceStatusChart> chartException01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	public List<SqlPerformanceTraceStatusChart> chartException02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
}
