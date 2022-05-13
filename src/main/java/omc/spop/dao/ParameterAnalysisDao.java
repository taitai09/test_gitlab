package omc.spop.dao;

import java.util.List;

import omc.spop.model.DbParameterHistory;
import omc.spop.model.OdsHistParameter;

/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ParameterAnalysisDao {
	public List<DbParameterHistory> parameterRecentChangesChartList(DbParameterHistory dbParameterHistory);
	
	public List<DbParameterHistory> parameterRecentChangesDetailList(DbParameterHistory dbParameterHistory);
	
	public List<DbParameterHistory> standardParameterCompChartList(DbParameterHistory dbParameterHistory);
	
	public List<DbParameterHistory> standardParameterCompDetailList(DbParameterHistory dbParameterHistory);
	
	public List<DbParameterHistory> instanceParameterCompChartList(DbParameterHistory dbParameterHistory);
	
	public List<DbParameterHistory> instanceParameterCompDetailList(DbParameterHistory dbParameterHistory);	
	
	public List<OdsHistParameter> parameterChangeHistoryList(OdsHistParameter odsHistParameter);
}