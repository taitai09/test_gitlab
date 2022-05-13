package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DailyCheckDb;

/***********************************************************
 * 2020.03.17	명성태	 최초작성
 **********************************************************/

public interface DailyCheckDbService {
	/** DB그룹 리스트 */
	List<DailyCheckDb> dbGroupList(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** 심각도 리스트 */
	List<DailyCheckDb> severityList(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** dbSeverityCount */
	List<DailyCheckDb> dbSeverityCount(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** dbMain 조회 */
	List<DailyCheckDb> dbMain(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** 진단결과 요약 **/
	List<DailyCheckDb> diagnosisResultSummary(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** 진단결과 상세 **/
	List<DailyCheckDb> diagnosisResultMinute(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** DB 상태 점검 현황 TOP **/
	List<LinkedHashMap<String, Object>> dailyCheckDbSituationTop(DailyCheckDb dailyCheckDb) throws Exception;
	
	/** DB 상태 점검 현황 BOTTOM **/
	List<LinkedHashMap<String, Object>> dailyCheckDbSituationBottom(DailyCheckDb dailyCheckDb) throws Exception;
}
