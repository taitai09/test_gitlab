package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlPerfImplAnalTable;

/***************************************************
 * 2021.02.10	황예지	최초작성
 ***************************************************/
public interface AnalyzeImpactChangedTableService {

	Result updateSqlAutoPerformance(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;

	List<SqlPerfImplAnalTable> loadTableCHGPerfChkTargetLeftList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	List<SQLAutoPerformanceCompare> loadTableCHGPerfChkTargetRightList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	List<LinkedHashMap<String, Object>> excelTuningDownload(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownload(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception;

	int getPerformanceResultCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

}
