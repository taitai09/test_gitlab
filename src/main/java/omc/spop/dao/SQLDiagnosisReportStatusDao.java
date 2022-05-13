package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

/***********************************************************
 * 2021.06.21	김원재 OPENPOP V2 최초작업
 **********************************************************/
public interface SQLDiagnosisReportStatusDao {
	public List<LinkedHashMap<String,Object>> getReportHeaders();
}
