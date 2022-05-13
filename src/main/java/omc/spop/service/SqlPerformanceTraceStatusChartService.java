package omc.spop.service;

import java.util.List;

import omc.spop.model.SqlPerformanceTraceStatusChart;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface SqlPerformanceTraceStatusChartService {
	List<SqlPerformanceTraceStatusChart> chart(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	List<SqlPerformanceTraceStatusChart> chartPerformance01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	List<SqlPerformanceTraceStatusChart> chartPerformance02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	List<SqlPerformanceTraceStatusChart> chartException01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
	
	List<SqlPerformanceTraceStatusChart> chartException02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart);
}
