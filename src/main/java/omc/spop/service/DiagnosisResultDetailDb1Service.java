package omc.spop.service;

import java.util.List;

import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisResultDetailDb1;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface DiagnosisResultDetailDb1Service {
	void loadSQL(List<DiagnosisReport> diagnosisReport, String contents_id, DiagnosisResultDetailDb1 diagnosisResultDetail, ReportHtml reportHtml) throws Exception;
	
	void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailDb1 diagnosisResultDetail, ReportHtml reportHtml) throws Exception;
}
