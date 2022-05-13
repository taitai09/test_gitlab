package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.PerformanceCheckSqlDao;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.service.PerformanceCheckSqlService;

/***********************************************************
 * 2020.05.13 	명성태 	최초작성
 **********************************************************/

@Service("performanceCheckSqlService")
public class PerformanceCheckSqlServiceImpl implements PerformanceCheckSqlService {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckSqlServiceImpl.class);
	
	@Autowired
	private PerformanceCheckSqlDao performanceCheckSqlDao;

	@Override
	public List<PerformanceCheckSql> getInitialFinalDistributionDate(PerformanceCheckSql performanceCheckSql) {
		return performanceCheckSqlDao.getInitialFinalDistributionDate(performanceCheckSql);
	}
	
	@Override
	public List<PerformanceCheckSql> getConditionFinalDistributionDate(PerformanceCheckSql performanceCheckSql) {
		return performanceCheckSqlDao.getConditionFinalDistributionDate(performanceCheckSql);
	}
	
	@Override
	public List<PerformanceCheckSql> loadPerformanceCheckSql(PerformanceCheckSql performanceCheckSql) {
		return performanceCheckSqlDao.loadPerformanceCheckSql(performanceCheckSql);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadPerformanceCheckSqlExcelDown(PerformanceCheckSql performanceCheckSql) throws Exception {
		return performanceCheckSqlDao.loadPerformanceCheckSqlExcelDown(performanceCheckSql);
	}
}
