package omc.spop.service;

import java.util.List;

import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisResult;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface DiagnosisResultService {
	void loadSQL(List<DiagnosisReport> diagnosisReport, String contents_id, DiagnosisResult diagnosisResult, ReportHtml reportHtml) throws Exception;
	
	void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResult diagnosisResult, ReportHtml reportHtml) throws Exception;
	
//	String writeHtml(ReportHtml reportHtml) throws Exception;
}
