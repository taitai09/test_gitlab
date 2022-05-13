package omc.spop.service.impl;

import java.util.ArrayList;
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
import omc.spop.model.Report.DiagnosisResultDetailDb2;
import omc.spop.service.DiagnosisResultDetailDb2Service;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailDb2Service")
public class DiagnosisResultDetailDb2ServiceImpl implements DiagnosisResultDetailDb2Service {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailDb2ServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailDb2 diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
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
				
				if(contents_id.equalsIgnoreCase("P131")) {			// Recyclebin Object
					html = html;
				} else if(contents_id.equalsIgnoreCase("P210")) {	// Recyclebin Obejct 현황 
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P211")) {	// 최근 1개월 Recyclebin Object 건수 추이
					html = P211(html, tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P212")) {	// 최근 1개월 Recyclebin 사이즈 추이
					html = P212(html, tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P213")) {	// Recycle Object
					html = P213(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P132")) {	// Invalid Object
					html = html;
				} else if(contents_id.equalsIgnoreCase("P214")) {	//  Invalid Object
					html = P214(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P215")) {	// 최근 1개월 Invalid Object 건수 추이
					html = P215(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P133")) {	// Nologging Object
					html = html;
				} else if(contents_id.equalsIgnoreCase("P216")) {	// Nologging Object
					html = P216(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P217")) {	// 최근 1개월 Nologging Object 건수 추이
					html = P217(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P134")) {	// Parallel Object
					html = html;
				} else if(contents_id.equalsIgnoreCase("P218")) {	// Parallel Object
					html = P218(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P219")) {	// 최근 1개월 Parallel Object 건수 추이
					html = P219(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P135")) {	// Unusable Index
					html = html;
				} else if(contents_id.equalsIgnoreCase("P220")) {	// Unusable Index
					html = P220(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P221")) {	// 최근 1개월 Unusable Index 건수 추이
					html = P221(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P136")) {	// Chained Rows Table
					html = html;
				} else if(contents_id.equalsIgnoreCase("P222")) {	// Chained Rows Table율
					html = P222(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P223")) {	// 최근 1개월 Chained Rows 테이블 발생 추이
					html = P223(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P137")) {	// Corrupt Block
					html = html;
				} else if(contents_id.equalsIgnoreCase("P224")) {	// Corrupt Block 목록
					html = P224(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P225")) {	// 최근 1개월 Corrupt Block 발생 추이
					html = P225(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P138")) {	// Sequence Threshold Exceeded
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P226")) {	// Sequence 목록
					html = P226(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P227")) {	// 최근 1개월 Sequence Usage 증가 추이
					html = P227(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P139")) {	// Foreign Keys Without Index
					html = html;
				} else if(contents_id.equalsIgnoreCase("P228")) {	// 인덱스 없는 Foreign Key
					html = P228(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P229")) {	// Owner 별 인덱스 없는 Foreign Key 건수
					html = P229(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P140")) {	// Disabled Constraint
					html = html;
				} else if(contents_id.equalsIgnoreCase("P230")) {	// Disabled Constraint
					html = P230(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P231")) {	// Owner 별 Disabled Constraint 건수
					html = P231(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P141")) {	// Missing or Stale Statistics
					html = html;
				} else if(contents_id.equalsIgnoreCase("P232")) {	// Missing or Stale Statistics Table
					html = P232(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P233")) {	// Owner 별 Missing or Stale Statistics Table 건수
					html = P233(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P142")) {	// Statistics Locked Table
					html = html;
				} else if(contents_id.equalsIgnoreCase("P234")) {	// Statistics Locked Table
					html = P234(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P235")) {	// Owner 별 Statistics Lock Table 건수
					html = P235(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P143")) {	// Long Running Work
					html = html;
				} else if(contents_id.equalsIgnoreCase("P236")) {	// Long Running Work
					html = P236(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P237")) {	// 최근 1개월동안 Long Running Work 발생 현황
					html = P237(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P144")) {	// Long Running Job
					html = html;
				} else if(contents_id.equalsIgnoreCase("P238")) {	// Long Running Job
					html = P238(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P239")) {	// 최근 1개월동안 Long Running Job 발생 현황
					html = P239(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P145")) {	// Scheduler Job 실패
					html = html;
				} else if(contents_id.equalsIgnoreCase("P240")) {	// Scheduler Job 실패
					html = P240(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P241")) {	// 최근 1개월 Scheduler Job 실패 발생 추이
					html = P241(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P146")) {	// Alert Log Error
					html = html;
				} else if(contents_id.equalsIgnoreCase("P242")) {	// Alert Log Error
					html = P242(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P243")) {	// 최근 1개월 Error 발생 현황
					html = P243(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P147")) {	// Active Incident
					html = html;
				} else if(contents_id.equalsIgnoreCase("P244")) {	// Problem
					html = P244(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P245")) {	// Incident
					html = P245(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P148")) {	// Outstanding Alert
					html = html;
				} else if(contents_id.equalsIgnoreCase("P246")) {	// Outstanding Alert
					html = P246(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P247")) {	// 최근 1개월 Outstanding Alert 발생 현황
					html = P247(html , tableHtml, reportHtml, contents_id, loadSQL);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		queue.add(html);
		
		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P112");
		}

//		logger.debug("contents_id[" + contents_id + "] html[" + html + "]"); // Used Debug
		
	}
	
	private String getHtml(String contents_id, DiagnosisResultDetailDb2 diagnosisResultDetail) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P131")) {
			html = diagnosisResultDetail.getP131();
		} else if(contents_id.equalsIgnoreCase("P210")) {
			html = diagnosisResultDetail.getP210();
		} else if(contents_id.equalsIgnoreCase("P211")) {
			html = diagnosisResultDetail.getP211();
		} else if(contents_id.equalsIgnoreCase("P212")) {
			html = diagnosisResultDetail.getP212();
		} else if(contents_id.equalsIgnoreCase("P213")) {
			html = diagnosisResultDetail.getP213();
		} else if(contents_id.equalsIgnoreCase("P132")) {
			html = diagnosisResultDetail.getP132();
		} else if(contents_id.equalsIgnoreCase("P214")) {
			html = diagnosisResultDetail.getP214();
		} else if(contents_id.equalsIgnoreCase("P215")) {
			html = diagnosisResultDetail.getP215();
		} else if(contents_id.equalsIgnoreCase("P133")) {
			html = diagnosisResultDetail.getP133();
		} else if(contents_id.equalsIgnoreCase("P216")) {
			html = diagnosisResultDetail.getP216();
		} else if(contents_id.equalsIgnoreCase("P217")) {
			html = diagnosisResultDetail.getP217();
		} else if(contents_id.equalsIgnoreCase("P134")) {
			html = diagnosisResultDetail.getP134();
		} else if(contents_id.equalsIgnoreCase("P218")) {
			html = diagnosisResultDetail.getP218();
		} else if(contents_id.equalsIgnoreCase("P219")) {
			html = diagnosisResultDetail.getP219();
		} else if(contents_id.equalsIgnoreCase("P135")) {
			html = diagnosisResultDetail.getP135();
		} else if(contents_id.equalsIgnoreCase("P220")) {
			html = diagnosisResultDetail.getP220();
		} else if(contents_id.equalsIgnoreCase("P221")) {
			html = diagnosisResultDetail.getP221();
		} else if(contents_id.equalsIgnoreCase("P136")) {
			html = diagnosisResultDetail.getP136();
		} else if(contents_id.equalsIgnoreCase("P222")) {
			html = diagnosisResultDetail.getP222();
		} else if(contents_id.equalsIgnoreCase("P223")) {
			html = diagnosisResultDetail.getP223();
		} else if(contents_id.equalsIgnoreCase("P137")) {
			html = diagnosisResultDetail.getP137();
		} else if(contents_id.equalsIgnoreCase("P224")) {
			html = diagnosisResultDetail.getP224();
		} else if(contents_id.equalsIgnoreCase("P225")) {
			html = diagnosisResultDetail.getP225();
		} else if(contents_id.equalsIgnoreCase("P138")) {
			html = diagnosisResultDetail.getP138();
		} else if(contents_id.equalsIgnoreCase("P226")) {
			html = diagnosisResultDetail.getP226();
		} else if(contents_id.equalsIgnoreCase("P227")) {
			html = diagnosisResultDetail.getP227();
		} else if(contents_id.equalsIgnoreCase("P139")) {
			html = diagnosisResultDetail.getP139();
		} else if(contents_id.equalsIgnoreCase("P228")) {
			html = diagnosisResultDetail.getP228();
		} else if(contents_id.equalsIgnoreCase("P229")) {
			html = diagnosisResultDetail.getP229();
		} else if(contents_id.equalsIgnoreCase("P140")) {
			html = diagnosisResultDetail.getP140();
		} else if(contents_id.equalsIgnoreCase("P230")) {
			html = diagnosisResultDetail.getP230();
		} else if(contents_id.equalsIgnoreCase("P231")) {
			html = diagnosisResultDetail.getP231();
		} else if(contents_id.equalsIgnoreCase("P141")) {
			html = diagnosisResultDetail.getP141();
		} else if(contents_id.equalsIgnoreCase("P232")) {
			html = diagnosisResultDetail.getP232();
		} else if(contents_id.equalsIgnoreCase("P233")) {
			html = diagnosisResultDetail.getP233();
		} else if(contents_id.equalsIgnoreCase("P142")) {
			html = diagnosisResultDetail.getP142();
		} else if(contents_id.equalsIgnoreCase("P234")) {
			html = diagnosisResultDetail.getP234();
		} else if(contents_id.equalsIgnoreCase("P235")) {
			html = diagnosisResultDetail.getP235();
		} else if(contents_id.equalsIgnoreCase("P143")) {
			html = diagnosisResultDetail.getP143();
		} else if(contents_id.equalsIgnoreCase("P236")) {
			html = diagnosisResultDetail.getP236();
		} else if(contents_id.equalsIgnoreCase("P237")) {
			html = diagnosisResultDetail.getP237();
		} else if(contents_id.equalsIgnoreCase("P144")) {
			html = diagnosisResultDetail.getP144();	
		} else if(contents_id.equalsIgnoreCase("P238")) {
			html = diagnosisResultDetail.getP238();	
		} else if(contents_id.equalsIgnoreCase("P239")) {
			html = diagnosisResultDetail.getP239();	
		} else if(contents_id.equalsIgnoreCase("P145")) {
			html = diagnosisResultDetail.getP145();	
		} else if(contents_id.equalsIgnoreCase("P240")) {
			html = diagnosisResultDetail.getP240();	
		} else if(contents_id.equalsIgnoreCase("P241")) {
			html = diagnosisResultDetail.getP241();	
		} else if(contents_id.equalsIgnoreCase("P146")) {
			html = diagnosisResultDetail.getP146();	
		} else if(contents_id.equalsIgnoreCase("P242")) {
			html = diagnosisResultDetail.getP242();	
		} else if(contents_id.equalsIgnoreCase("P243")) {
			html = diagnosisResultDetail.getP243();	
		} else if(contents_id.equalsIgnoreCase("P147")) {
			html = diagnosisResultDetail.getP147();	
		} else if(contents_id.equalsIgnoreCase("P244")) {
			html = diagnosisResultDetail.getP244();	
		} else if(contents_id.equalsIgnoreCase("P245")) {
			html = diagnosisResultDetail.getP245();	
		} else if(contents_id.equalsIgnoreCase("P148")) {
			html = diagnosisResultDetail.getP148();	
		} else if(contents_id.equalsIgnoreCase("P246")) {
			html = diagnosisResultDetail.getP246();	
		} else if(contents_id.equalsIgnoreCase("P247")) {
			html = diagnosisResultDetail.getP247();	
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailDb2 diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
		String html =  getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P112");
	}
	
	private String P211(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Object 건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P211_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P212(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "사이즈(GB)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P212_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P213(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Object Name";
		String head3 = "Original Name";
		String head4 = "Operation";
		String head5 = "Type";
		String head6 = "Tablespace";
		String head7 = "Create Time";
		String head8 = "Drop Time";
		String head9 = "Blocks";
		String head10 = "Space Used(GB)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(2, head9);
			tableHtml += reportHtml.tableStyle1Head(3, head10);
			
			tableHtml += reportHtml.tableStyleNoDataClose(10, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(head9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(head10) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(2, head9);
					tableHtml += reportHtml.tableStyle1Head(3, head10);
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data10, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data10, 2);
			}
		}
		
		valueIndex = html.indexOf("P213_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P214(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Object Name";
		String head3 = "Object Type";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(3, head3);
			
			tableHtml += reportHtml.tableStyleNoDataClose(3, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				
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
		
		valueIndex = html.indexOf("P214_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P215(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Object Type";
		String clm_value = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P215_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P216(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Object Name";
		String head3 = "Object Type";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(3, head3);
			
			tableHtml += reportHtml.tableStyleNoDataClose(3, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				
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
		
		valueIndex = html.indexOf("P216_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P217(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P217_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P218(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Object Name";
		String head3 = "Object Type";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(3, head3);
			
			tableHtml += reportHtml.tableStyleNoDataClose(3, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				
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
		
		valueIndex = html.indexOf("P218_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P219(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P219_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P220(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Index Name";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(3, head3);
			
			tableHtml += reportHtml.tableStyleNoDataClose(3, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				
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
		
		valueIndex = html.indexOf("P220_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]");
		
		return html;
	}
	
	private String P221(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P221_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P222(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Tablespace";
		String head4 = "Num Rows";
		String head5 = "Chain Rows";
		String head6 = "Chain Ratio(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(3, head6);
			
			tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(3, head6);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 2);
			}
		}
		
		valueIndex = html.indexOf("P222_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P223(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P223_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P224(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "File#";
		String head2 = "File Name";
		String head3 = "Block#";
		String head4 = "손상 Block 수";
		String head5 = "논리 손상 번호";
		String head6 = "손상 유형";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(3, head6);
			
			tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(3, head6);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 1);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 0);
			}
		}
		
		valueIndex = html.indexOf("P224_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P225(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P225_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P226(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Sequence";
		String head3 = "Min";
		String head4 = "Max";
		String head5 = "Last Number";
		String head6 = "Used(%)";
		String head7 = "Increment By";
		String head8 = "Cycle";
		String head9 = "Order";
		String head10 = "Cache Size";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(2, head9);
			tableHtml += reportHtml.tableStyle1Head(3, head10);
			
			tableHtml += reportHtml.tableStyleNoDataClose(10, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(head9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(head10) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(2, head9);
					tableHtml += reportHtml.tableStyle1Head(3, head10);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data10, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data10, 2);
			}
		}
		
		valueIndex = html.indexOf("P226_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P227(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm1 = "기준일";
		String clm2 = "Sequence";
		String clm3 = "Used(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm1, clm2, clm3);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P227_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P228(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Constraint Name";
		String head4 = "Columns";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P228_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P229(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		if(loadSQLSize > 0) {
			ArrayList<String> labelList = new ArrayList<String>();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get("건수") + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
//			
			tableHtml += reportHtml.pieChartStyle(4, contents_id, labelList, dataList2);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P229_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P230(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Constraint Name";
		String head4 = "Constraint Type";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P230_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P231(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		if(loadSQLSize > 0) {
			ArrayList<String> labelList = new ArrayList<String>();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get("건수") + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
//			
			tableHtml += reportHtml.pieChartStyle(4, contents_id, labelList, dataList2);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P231_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P232(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Partition Name";
		String head4 = "Partitioned";
		String head5 = "Last Analyzed";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(3, head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(3, head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 1);
			}
		}
		
		valueIndex = html.indexOf("P232_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P233(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		if(loadSQLSize > 0) {
			ArrayList<String> labelList = new ArrayList<String>();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get("건수") + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
//			
			tableHtml += reportHtml.pieChartStyle(4, contents_id, labelList, dataList2);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P233_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P234(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Object Type";
		String head4 = "Lock Type";
		String head5 = "Last Analyzed";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(3, head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(3, head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 1);
			}
		}
		
		valueIndex = html.indexOf("P234_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P235(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		if(loadSQLSize > 0) {
			ArrayList<String> labelList = new ArrayList<String>();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get("건수") + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
//			
			tableHtml += reportHtml.pieChartStyle(4, contents_id, labelList, dataList2);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P235_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P236(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "SID";
		String head3 = "Serial#";
		String head4 = "SQL_ID";
		String head5 = "PLAN_HASH_VALUE";
		String head6 = "시작시간";
		String head7 = "경과시간";
		String head8 = "남은시간(분)";
		String head9 = "진행률(%)";
		String head10 = "메시지";
		String head11 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(2, head9);
			tableHtml += reportHtml.tableStyle1Head(2, head10);
			tableHtml += reportHtml.tableStyle1Head(3, head11);
			
			tableHtml += reportHtml.tableStyleNoDataClose(11, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(head9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(head10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(head11) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(2, head9);
					tableHtml += reportHtml.tableStyle1Head(2, head10);
					tableHtml += reportHtml.tableStyle1Head(3, head11);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data11, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data11, 0);
			}
		}
		
		valueIndex = html.indexOf("P236_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P237(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P237_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P238(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "SID";
		String head3 = "Owner";
		String head4 = "Job Name";
		String head5 = "수행시간";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(3, head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(3, head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 1);
			}
		}
		
		valueIndex = html.indexOf("P238_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P239(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Object Type";
		String clm_value = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P239_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P240(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Owner";
		String head3 = "Job Name";
		String head4 = "Sub Job Name";
		String head5 = "Job 상태";
		String head6 = "오류코드";
		String head7 = "Job 요청일시";
		String head8 = "Job 시작일시";
		String head9 = "수행시간";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(3, head9);
			
			tableHtml += reportHtml.tableStyleNoDataClose(9, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(head9) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(3, head9);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data9, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 1);
			}
		}
		
		valueIndex = html.indexOf("P240_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P241(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P241_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P242(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "진단일";
		String head2 = "Instance";
		String head3 = "오류코드";
		String head4 = "발생횟수";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P242_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P243(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm1 = "기준일";
		String clm2 = "오류코드";
		String clm3 = "발생건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm1, clm2, clm3);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P243_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P244(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Problem ID";
		String head3 = "Problem Key";
		String head4 = "첫번째 Incident ID";
		String head5 = "첫번째 Incident 발생시간";
		String head6 = "최종 Incident ID";
		String head7 = "최종 Incident 발생시간";
		String head8 = "첫번째 Impact";
		String head9 = "두번째 Impact";
		String head10 = "세번째 Impact";
		String head11 = "네번째 Impact";
		String head12 = "SR 요청번호";
		String head13 = "버그번호";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(2, head9);
			tableHtml += reportHtml.tableStyle1Head(2, head10);
			tableHtml += reportHtml.tableStyle1Head(2, head11);
			tableHtml += reportHtml.tableStyle1Head(2, head12);
			tableHtml += reportHtml.tableStyle1Head(3, head13);
			
			tableHtml += reportHtml.tableStyleNoDataClose(13, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(head9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(head10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(head11) + "";
				String data12 = loadSQL.get(loadSQLIndex).get(head12) + "";
				String data13 = loadSQL.get(loadSQLIndex).get(head13) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(2, head9);
					tableHtml += reportHtml.tableStyle1Head(2, head10);
					tableHtml += reportHtml.tableStyle1Head(2, head11);
					tableHtml += reportHtml.tableStyle1Head(2, head12);
					tableHtml += reportHtml.tableStyle1Head(3, head13);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data11, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data12, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data13, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data11, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data12, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data13, 2);
			}
		}
		
		valueIndex = html.indexOf("P244_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P245(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Incident ID";
		String head3 = "Problem ID";
		String head4 = "생성일시";
		String head5 = "종료일시";
		String head6 = "Incident 상태";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(3, head6);
			
			tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(3, head6);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 1);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 2);
			}
		}
		
		valueIndex = html.indexOf("P245_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P246(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Owner";
		String head3 = "Object Name";
		String head4 = "Sub Object Name";
		String head5 = "Object Type";
		String head6 = "생성일시";
		String head7 = "사유";
		String head8 = "조치내용";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(3, head8);
			
			tableHtml += reportHtml.tableStyleNoDataClose(8, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(head8) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(3, head8);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data8, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data8, 0);
			}
		}
		
		valueIndex = html.indexOf("P246_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("P246 sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P247(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "발생일시";
		String clm_legend = "Instance";
		String clm_value = "발생건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P247_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
}
