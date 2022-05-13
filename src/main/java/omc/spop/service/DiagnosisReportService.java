package omc.spop.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisOverview;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface DiagnosisReportService {
	
	List<DiagnosisReport> loadSLTProgramContents(DiagnosisReport diagnosisReport) throws Exception;
	
	List<DiagnosisReport> getSQL(DiagnosisReport diagnosisReport) throws Exception;
	
	void loadSQL(List<DiagnosisReport> diagnosisReport, String contents_id, DiagnosisOverview diagnosisOverview, ReportHtml reportHtml) throws Exception;
	
	void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisOverview diagnosisOverview, ReportHtml reportHtml) throws Exception;
	
	List<DiagnosisReport> getSQLUnit(DiagnosisReport diagnosisReport) throws Exception;
	
	List<LinkedHashMap<String, Object>> loadSQLUnit(DiagnosisReport diagnosisReport) throws Exception;
	
//	void compress(HashMap<String,String> infoMap , String type ,String except) throws Exception;
}
