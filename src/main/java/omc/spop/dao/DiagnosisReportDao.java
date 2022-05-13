package omc.spop.dao;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DiagnosisReport;

/***********************************************************
 * 2019.10.28	명성태	OPENPOP V2 최초작업
 **********************************************************/

public interface DiagnosisReportDao {
	public List<DiagnosisReport> loadSLTProgramContents(DiagnosisReport diagnosisReport);
	
	public List<DiagnosisReport> getSQL(DiagnosisReport diagnosisReport);
	
	public List<LinkedHashMap<String, Object>> loadSQL(DiagnosisReport diagnosisReport);
	
	public List<DiagnosisReport> getSQLUnit(DiagnosisReport diagnosisReport);
	
	public List<LinkedHashMap<String, Object>> loadSQLUnit(DiagnosisReport diagnosisReport);
}
