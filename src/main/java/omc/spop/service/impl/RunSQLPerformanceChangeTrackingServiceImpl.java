package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.RunSQLPerformanceChangeTrackingDao;
import omc.spop.model.ExceptionHandlingSql;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.SqlPerformanceTraceStatusChart;
import omc.spop.service.RunSQLPerformanceChangeTrackingService;

/***********************************************************
 * 2021.08.25 	이재우	최초작성
 **********************************************************/

@Service("runSQLPerformanceChangeTrackingService")
public class RunSQLPerformanceChangeTrackingServiceImpl implements RunSQLPerformanceChangeTrackingService {
	private static final Logger logger = LoggerFactory.getLogger(RunSQLPerformanceChangeTrackingServiceImpl.class);
	
	@Autowired
	private RunSQLPerformanceChangeTrackingDao runSQLPerformanceChangeTrackingDao;

	@Override
	public List<PerformanceCheckSql> getInitialFinalDistributionDate( PerformanceCheckSql performanceCheckSql ) {
		return runSQLPerformanceChangeTrackingDao.getInitialFinalDistributionDate( performanceCheckSql );
	}
	
	@Override
	public List<PerformanceCheckSql> getConditionFinalDistributionDate( PerformanceCheckSql performanceCheckSql ) {
		return runSQLPerformanceChangeTrackingDao.getConditionFinalDistributionDate( performanceCheckSql );
	}
	
	@Override
	public List<PerformanceCheckSql> loadPerformanceVerifySql( PerformanceCheckSql performanceCheckSql ) {
		return runSQLPerformanceChangeTrackingDao.loadPerformanceVerifySql( performanceCheckSql );
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadPerformanceVerifySqlExcelDown( PerformanceCheckSql performanceCheckSql ) throws Exception {
		return runSQLPerformanceChangeTrackingDao.loadPerformanceVerifySqlExcelDown( performanceCheckSql );
	}

	@Override
	public List<ExceptionHandlingSql> loadExceptionHandlingSql( ExceptionHandlingSql exceptionHandlingSql )
			throws Exception {
		return runSQLPerformanceChangeTrackingDao.loadExceptionHandlingSql( exceptionHandlingSql );
	}

	@Override
	public List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown(
			ExceptionHandlingSql exceptionHandlingSql ) throws Exception {
		return runSQLPerformanceChangeTrackingDao.loadExceptionHandlingSqlExcelDown( exceptionHandlingSql );
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chart(SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart)
			throws Exception {
		return runSQLPerformanceChangeTrackingDao.chart( sqlPerformanceTraceStatusChart );
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartPerformance01(
			SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		return runSQLPerformanceChangeTrackingDao.chartPerformance01( sqlPerformanceTraceStatusChart );
	}
	
	@Override
	public List<SqlPerformanceTraceStatusChart> chartPerformance02(
			SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		return runSQLPerformanceChangeTrackingDao.chartPerformance02( sqlPerformanceTraceStatusChart );
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartException01(
			SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		return runSQLPerformanceChangeTrackingDao.chartException01( sqlPerformanceTraceStatusChart );
	}

	@Override
	public List<SqlPerformanceTraceStatusChart> chartException02(
			SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		return runSQLPerformanceChangeTrackingDao.chartException02( sqlPerformanceTraceStatusChart );
	}
	
	
}
