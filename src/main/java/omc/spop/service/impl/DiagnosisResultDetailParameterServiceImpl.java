package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.controller.DiagnosisReportController;
import omc.spop.dao.DiagnosisReportDao;
import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisResultDetailParameter;
import omc.spop.service.DiagnosisResultDetailParameterService;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailParameterService")
public class DiagnosisResultDetailParameterServiceImpl implements DiagnosisResultDetailParameterService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailParameterServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailParameter diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
		DiagnosisReport diagnosisReport;
		String html = getHtml(contents_id, diagnosisResultDetail);
		
		DiagnosisReportController diagnosisReportController = new DiagnosisReportController();
		Queue<String> queue = new LinkedList<String>();

		try {
			for(int sqlIndex = 0; sqlIndex < sqlList.size(); sqlIndex++) {
				diagnosisReport = sqlList.get(sqlIndex);
				
				int slt_program_sql_number = diagnosisReport.getSlt_program_sql_number();
				String tableHtml = "";
				List<LinkedHashMap<String, Object>> loadSQL = diagnosisReportDao.loadSQL(diagnosisReport);
				int loadSQLSize = loadSQL.size();
				
				if(contents_id.equalsIgnoreCase("P153")) {			// 파라미터 진단
					html = html;
				} else if(contents_id.equalsIgnoreCase("P154")) {	// RAC 인스턴스 간 다른 파라미터 진단
					html = html;
				} else if(contents_id.equalsIgnoreCase("P254")) {	// RAC 인스턴스 간 다른 파라미터
					html = P254(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		queue.add(html);
		
		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P153");
		}

//		logger.debug("contents_id[" + contents_id + "] html[" + html + "]"); // Used Debug
		
	}
	
	private String getHtml(String contents_id, DiagnosisResultDetailParameter diagnosisResultDetail1) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P153")) {
			html = diagnosisResultDetail1.getP153();
		} else if(contents_id.equalsIgnoreCase("P154")) {
			html = diagnosisResultDetail1.getP154();
		} else if(contents_id.equalsIgnoreCase("P254")) {
			html = diagnosisResultDetail1.getP254();
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailParameter diagnosisResultDetail1, ReportHtml reportHtml) throws Exception {
		String html = getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail1);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P153");
	}
	
	private String P254(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "파라미터";
		String head2 = "INST_NM";
		String head3 = "VALUE";
		
		String clm1 = "PARAMETER_NAME";
		String clm2 = "INST_NM";
		String clm3 = "VALUE";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(3, head3);
			
			tableHtml += reportHtml.tableStyleNoDataClose(3, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(clm1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(clm2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(clm3) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(3, head3);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data3, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data3, 0);
			}
		}
		
		valueIndex = html.indexOf("P254_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}

}
