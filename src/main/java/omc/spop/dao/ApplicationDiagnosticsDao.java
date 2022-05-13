package omc.spop.dao;

import java.util.List;

import omc.spop.model.TrcdPerfSum;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ApplicationDiagnosticsDao {
	public List<TrcdPerfSum> summaryList(TrcdPerfSum trcdPerfSum);
	
	public List<TrcdPerfSum> summaryChartList(TrcdPerfSum trcdPerfSum);
	
	public List<TrcdPerfSum> timeoutList(TrcdPerfSum trcdPerfSum);
	
	public List<TrcdPerfSum> elapsedTimeDelayList(TrcdPerfSum trcdPerfSum);
}
