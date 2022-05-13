package omc.spop.service;

import java.util.List;

import omc.spop.model.TrcdPerfSum;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ApplicationDiagnosticsService {
	/** 요약 정보 리스트 */
	List<TrcdPerfSum> summaryList(TrcdPerfSum trcdPerfSum) throws Exception;
	
	/** 요약 차트 정보 리스트 */
	List<TrcdPerfSum> summaryChartList(TrcdPerfSum trcdPerfSum) throws Exception;	
	
	/** 타임아웃거래 리스트 */
	List<TrcdPerfSum> timeoutList(TrcdPerfSum trcdPerfSum) throws Exception;
	
	/** 응답시간지연 리스트 */
	List<TrcdPerfSum> elapsedTimeDelayList(TrcdPerfSum trcdPerfSum) throws Exception;	
}
