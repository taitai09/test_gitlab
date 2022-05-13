package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerfList;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2021.09.27	이재우	성능개선현황 보고서 엑셀다운 추가
 **********************************************************/

public interface PerformanceImprovementReportDao {	
	public List<PerfList> performanceImprovementReportList(PerfList perfList);

	public List<PerfList> getByProgramTypeReport(PerfList perfList);

	public List<LinkedHashMap<String, Object>> getByProgramTypeReportByExcelDown(PerfList perfList);

	public List<PerfList> makeColumnsValues(PerfList perfList);

	public List<PerfList> getByRequestTypeReport(PerfList perfList);

//	public List<LinkedHashMap<String, Object>> getByRequestTypeReportByExcelDown(PerfList perfList);

	public List<PerfList> getByImprovementTypeReport(PerfList perfList);

	public List<LinkedHashMap<String, Object>> getPerformanceImprovementReportByExcelDown( PerfList perfList );

//	public List<LinkedHashMap<String, Object>> getByImprovementTypeReportByExcelDown(PerfList perfList);


}
