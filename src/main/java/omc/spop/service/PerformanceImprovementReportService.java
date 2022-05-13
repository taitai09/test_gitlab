package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerfList;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2021.09.27	이재우	성능개선현황 보고서 엑셀다운 추가
 **********************************************************/

public interface PerformanceImprovementReportService {
	/** 성능관리현황 리스트 */
	List<PerfList> performanceImprovementReportList(PerfList perfList) throws Exception;

	/** 성능관리현황 리스트  엑셀다운 */
	List<LinkedHashMap<String, Object>> getPerformanceImprovementReportByExcelDown(PerfList perfList) throws Exception;
	
	/** 프로그램유형별 성능개선현황 리스트 */
	List<PerfList> getByProgramTypeReport(PerfList perfList);

	/** 프로그램유형별 성능개선현황 리스트 엑셀 다운*/
	List<LinkedHashMap<String, Object>> getByProgramTypeReportByExcelDown(PerfList perfList);
	
	/** 요청유형별 성능개선현황 리스트 
	 * @throws Exception */
	List<LinkedHashMap<String, Object>> getByRequestTypeReport(PerfList perfList) throws Exception;

	/** 요청유형별 성능개선현황 리스트 컬럼 만들기*/
	List<PerfList> makeColumnsValues(PerfList perfList);

	/** 요청유형별 성능개선현황 리스트 엑셀 다운
	 * @throws Exception */
//	List<LinkedHashMap<String, Object>> getByRequestTypeReportByExcelDown(PerfList perfList) throws Exception;

	/** 개선유형별 성능개선현황 리스트 
	 * @throws Exception */
	List<LinkedHashMap<String, Object>> getByImprovementTypeReport(PerfList perfList) throws Exception;

	/** 요청유형별 성능개선현황 리스트 엑셀 다운 */
//	List<LinkedHashMap<String, Object>> getByImprovementTypeReportByExcelDown(PerfList perfList) throws Exception;

}
