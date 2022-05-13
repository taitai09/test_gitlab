package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ParameterAnalysisDao;
import omc.spop.model.DbParameterHistory;
import omc.spop.model.OdsHistParameter;
import omc.spop.service.ParameterAnalysisService;

/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("ParameterAnalysisService")
public class ParameterAnalysisServiceImpl implements ParameterAnalysisService {
	@Autowired
	private ParameterAnalysisDao parameterAnalysisDao;
	
	@Override
	public List<DbParameterHistory> parameterRecentChangesChartList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.parameterRecentChangesChartList(dbParameterHistory);
	}
	
	@Override
	public List<DbParameterHistory> parameterRecentChangesDetailList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.parameterRecentChangesDetailList(dbParameterHistory);
	}	

	@Override
	public List<DbParameterHistory> standardParameterCompChartList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.standardParameterCompChartList(dbParameterHistory);
	}
	
	@Override
	public List<DbParameterHistory> standardParameterCompDetailList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.standardParameterCompDetailList(dbParameterHistory);
	}
	
	@Override
	public List<DbParameterHistory> instanceParameterCompChartList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.instanceParameterCompChartList(dbParameterHistory);
	}
	
	@Override
	public List<DbParameterHistory> instanceParameterCompDetailList(DbParameterHistory dbParameterHistory) throws Exception {
		return parameterAnalysisDao.instanceParameterCompDetailList(dbParameterHistory);
	}	
	
	@Override
	public List<OdsHistParameter> parameterChangeHistoryList(OdsHistParameter odsHistParameter) throws Exception {
		return parameterAnalysisDao.parameterChangeHistoryList(odsHistParameter);
	}	
}