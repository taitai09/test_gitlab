package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ApplicationDiagnosticsDao;
import omc.spop.model.TrcdPerfSum;
import omc.spop.service.ApplicationDiagnosticsService;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("ApplicationDiagnosticsService")
public class ApplicationDiagnosticsServiceImpl implements ApplicationDiagnosticsService {
	@Autowired
	private ApplicationDiagnosticsDao applicationDiagnosticsDao;
	
	@Override
	public List<TrcdPerfSum> summaryList(TrcdPerfSum trcdPerfSum) throws Exception {
		return applicationDiagnosticsDao.summaryList(trcdPerfSum);
	}
	
	@Override
	public List<TrcdPerfSum> summaryChartList(TrcdPerfSum trcdPerfSum) throws Exception {
		return applicationDiagnosticsDao.summaryChartList(trcdPerfSum);
	}	
	
	@Override
	public List<TrcdPerfSum> timeoutList(TrcdPerfSum trcdPerfSum) throws Exception {
		return applicationDiagnosticsDao.timeoutList(trcdPerfSum);
	}
	
	@Override
	public List<TrcdPerfSum> elapsedTimeDelayList(TrcdPerfSum trcdPerfSum) throws Exception {
		return applicationDiagnosticsDao.elapsedTimeDelayList(trcdPerfSum);
	}
}