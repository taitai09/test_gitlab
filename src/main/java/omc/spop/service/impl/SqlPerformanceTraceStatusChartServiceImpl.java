package omc.spop.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SqlPerformanceTraceStatusChartDao;
import omc.spop.model.SqlPerformanceTraceStatusChart;
import omc.spop.service.SqlPerformanceTraceStatusChartService;

/***********************************************************
 * 2020.05.13 	명성태 	최초작성
 **********************************************************/

@Service("sqlPerformanceTraceStatusChartService")
public class SqlPerformanceTraceStatusChartServiceImpl implements SqlPerformanceTraceStatusChartService {
	private static final Logger logger = LoggerFactory.getLogger(SqlPerformanceTraceStatusChartServiceImpl.class);
	
	@Autowired
	private SqlPerformanceTraceStatusChartDao dao;

	@Override
	public List<SqlPerformanceTraceStatusChart> chart(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) {
		return dao.chart(sqlPerformanceTraceStatusChart);
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartPerformance01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) {
		return dao.chartPerformance01(sqlPerformanceTraceStatusChart);
	}
	
	@Override
	public List<SqlPerformanceTraceStatusChart> chartPerformance02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) {
		return dao.chartPerformance02(sqlPerformanceTraceStatusChart);
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartException01(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) {
		return dao.chartException01(sqlPerformanceTraceStatusChart);
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartException02(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) {
		return dao.chartException02(sqlPerformanceTraceStatusChart);
	}

}
