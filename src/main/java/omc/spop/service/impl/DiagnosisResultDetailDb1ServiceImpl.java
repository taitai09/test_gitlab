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
import omc.spop.model.Report.DiagnosisResultDetailDb1;
import omc.spop.service.DiagnosisResultDetailDb1Service;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailDb1Service")
public class DiagnosisResultDetailDb1ServiceImpl implements DiagnosisResultDetailDb1Service {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailDb1ServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailDb1 diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
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
				
				
				if(contents_id.equalsIgnoreCase("P113")) {			// Expired(grace) 계정
					html = P113(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P114")) {	// 최근 1개월 Expired(grace) 계정 발생 추이
					html = P114(html, tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P115")) {	// 파라미터 변경
					html = html;
				} else if(contents_id.equalsIgnoreCase("P178")) {	// 전일 파라미터 변경
					html = P178(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P179")) {	// 최근 1개월 파라미터 변경
					html = P179(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P116")) {	// DB File 생성율
					html = html;
				} else if(contents_id.equalsIgnoreCase("P180")) {	// DB File 생성율
					html = P180(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P181")) {	// 최근 1개월 DB File 생성율 추이
					html = P181(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P117")) {	// Library Cache Hit
					html = html;
				} else if(contents_id.equalsIgnoreCase("P182")) {	// Library Cache Hit 율
					html = P182(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P183")) {	// 최근 1개월 Library Cache Hit 변화
					html = P183(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P118")) {	// Dictionary Cache Hit
					html = html;
				} else if(contents_id.equalsIgnoreCase("P184")) {	// Dictionary Cache Hit 율
					html = P184(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P185")) {	// 최근 1개월 Dictionary Cache Hit 변화
					html = P185(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P119")) {	// Buffer Cache Hit
					html = html;
				} else if(contents_id.equalsIgnoreCase("P186")) {	// Buffer Cache Hit 율
					html = P186(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P187")) {	// 최근 1개월 Buffer Cache Hit 변화
					html = P187(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P120")) {	// Latch Hit
					html = html;
				} else if(contents_id.equalsIgnoreCase("P188")) {	// Latch Hit 율
					html = P188(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P189")) {	// 최근 1개월 Latch Hit 변화
					html = P189(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P121")) {	// Parse CPU To Parse Elapsed
					html = html;
				} else if(contents_id.equalsIgnoreCase("P190")) {	// Parse CPU To Parse Elapsed 율
					html = P190(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P191")) {	// 최근 1개월 Parse CPU To Parse Elapsed 율 변화
					html = P191(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P122")) {	// Disk Sort 
					html = html;
				} else if(contents_id.equalsIgnoreCase("P192")) {	// Disk Sort 율
					html = P192(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P193")) {	// 최근 1개월 Disk Sort 율 변화
					html = P193(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P123")) {	// Shared Pool Usage
					html = html;
				} else if(contents_id.equalsIgnoreCase("P194")) {	// Shared Pool 사용률
					html = P194(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P195")) {	// 최근 1개월 Shared Pool 사용률 변화
					html = P195(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P124")) {	// Resource Limit
					html = P124(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P128")) {	// FRA Space
					html = html;
				} else if(contents_id.equalsIgnoreCase("P202")) {	// FRA Space 사용량
					html = P202(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P203")) {	// 최근 1개월 FRA Space 사용량 변화
					html = P203(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P204")) {	// FRA 유형별 사용량
					html = P204(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P205")) {	// 최근 1개월 FRA 유형별 사용량 변화
					html = P205(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P129")) {	// ASM Diskgroup Space
					html = html;
				} else if(contents_id.equalsIgnoreCase("P206")) {	// ASM Diskgroup Space 사용률
					html = P206(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P207")) {	// 최근 1개월 ASM Diskgroup Space 사용률 변화
					html = P207(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P130")) {	// Tablespace Usage
					html = html;
				} else if(contents_id.equalsIgnoreCase("P208")) {	// Tablespace 사용률
					html = P208(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P209")) {	// 최근 1개월 Tablespace 사용량 변화
					html = P209(html , tableHtml, reportHtml, contents_id, loadSQL);
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
	
	private String getHtml(String contents_id, DiagnosisResultDetailDb1 diagnosisResultDetail1) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P030")) {
			html = diagnosisResultDetail1.getP030();
		} else if(contents_id.equalsIgnoreCase("P112")) {
			html = diagnosisResultDetail1.getP112();
		} else if(contents_id.equalsIgnoreCase("P113")) {
			html = diagnosisResultDetail1.getP113();
		} else if(contents_id.equalsIgnoreCase("P114")) {
			html = diagnosisResultDetail1.getP114();
		} else if(contents_id.equalsIgnoreCase("P115")) {
			html = diagnosisResultDetail1.getP115();
		} else if(contents_id.equalsIgnoreCase("P178")) {
			html = diagnosisResultDetail1.getP178();
		} else if(contents_id.equalsIgnoreCase("P179")) {
			html = diagnosisResultDetail1.getP179();
		} else if(contents_id.equalsIgnoreCase("P116")) {
			html = diagnosisResultDetail1.getP116();
		} else if(contents_id.equalsIgnoreCase("P180")) {
			html = diagnosisResultDetail1.getP180();
		} else if(contents_id.equalsIgnoreCase("P181")) {
			html = diagnosisResultDetail1.getP181();
		} else if(contents_id.equalsIgnoreCase("P117")) {
			html = diagnosisResultDetail1.getP117();
		} else if(contents_id.equalsIgnoreCase("P182")) {
			html = diagnosisResultDetail1.getP182();
		} else if(contents_id.equalsIgnoreCase("P183")) {
			html = diagnosisResultDetail1.getP183();
		} else if(contents_id.equalsIgnoreCase("P118")) {
			html = diagnosisResultDetail1.getP118();
		} else if(contents_id.equalsIgnoreCase("P184")) {
			html = diagnosisResultDetail1.getP184();
		} else if(contents_id.equalsIgnoreCase("P185")) {
			html = diagnosisResultDetail1.getP185();
		} else if(contents_id.equalsIgnoreCase("P119")) {
			html = diagnosisResultDetail1.getP119();
		} else if(contents_id.equalsIgnoreCase("P186")) {
			html = diagnosisResultDetail1.getP186();
		} else if(contents_id.equalsIgnoreCase("P187")) {
			html = diagnosisResultDetail1.getP187();
		} else if(contents_id.equalsIgnoreCase("P120")) {
			html = diagnosisResultDetail1.getP120();
		} else if(contents_id.equalsIgnoreCase("P188")) {
			html = diagnosisResultDetail1.getP188();
		} else if(contents_id.equalsIgnoreCase("P189")) {
			html = diagnosisResultDetail1.getP189();
		} else if(contents_id.equalsIgnoreCase("P121")) {
			html = diagnosisResultDetail1.getP121();
		} else if(contents_id.equalsIgnoreCase("P190")) {
			html = diagnosisResultDetail1.getP190();
		} else if(contents_id.equalsIgnoreCase("P191")) {
			html = diagnosisResultDetail1.getP191();
		} else if(contents_id.equalsIgnoreCase("P122")) {
			html = diagnosisResultDetail1.getP122();
		} else if(contents_id.equalsIgnoreCase("P192")) {
			html = diagnosisResultDetail1.getP192();
		} else if(contents_id.equalsIgnoreCase("P193")) {
			html = diagnosisResultDetail1.getP193();
		} else if(contents_id.equalsIgnoreCase("P123")) {
			html = diagnosisResultDetail1.getP123();
		} else if(contents_id.equalsIgnoreCase("P194")) {
			html = diagnosisResultDetail1.getP194();
		} else if(contents_id.equalsIgnoreCase("P195")) {
			html = diagnosisResultDetail1.getP195();
		} else if(contents_id.equalsIgnoreCase("P124")) {
			html = diagnosisResultDetail1.getP124();
		} else if(contents_id.equalsIgnoreCase("P128")) {
			html = diagnosisResultDetail1.getP128();	
		} else if(contents_id.equalsIgnoreCase("P202")) {
			html = diagnosisResultDetail1.getP202();	
		} else if(contents_id.equalsIgnoreCase("P203")) {
			html = diagnosisResultDetail1.getP203();	
		} else if(contents_id.equalsIgnoreCase("P204")) {
			html = diagnosisResultDetail1.getP204();	
		} else if(contents_id.equalsIgnoreCase("P205")) {
			html = diagnosisResultDetail1.getP205();	
		} else if(contents_id.equalsIgnoreCase("P129")) {
			html = diagnosisResultDetail1.getP129();	
		} else if(contents_id.equalsIgnoreCase("P206")) {
			html = diagnosisResultDetail1.getP206();	
		} else if(contents_id.equalsIgnoreCase("P207")) {
			html = diagnosisResultDetail1.getP207();	
		} else if(contents_id.equalsIgnoreCase("P130")) {
			html = diagnosisResultDetail1.getP130();	
		} else if(contents_id.equalsIgnoreCase("P208")) {
			html = diagnosisResultDetail1.getP208();	
		} else if(contents_id.equalsIgnoreCase("P209")) {
			html = diagnosisResultDetail1.getP209();	
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailDb1 diagnosisResultDetail1, ReportHtml reportHtml) throws Exception {
		String html = getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail1);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P112");

	}
	
	private String P113(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "User";
		String head2 = "User 생성일시";
		String head3 = "Password 만료일시";
		String head4 = "Password 만료 남은시간";
		String head5 = "Password 만료 통보일";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(2);
			tableHtml += reportHtml.tableStyle1Head(1, "username", head1);
			tableHtml += reportHtml.tableStyle1Head(2, "created", head2);
			tableHtml += reportHtml.tableStyle1Head(2, "expiry_date", head3);
			tableHtml += reportHtml.tableStyle1Head(2, "password_expiry_remin_time", head4);
			tableHtml += reportHtml.tableStyle1Head(3, "password_grace_time", head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String username = loadSQL.get(loadSQLIndex).get(head1) + "";
				String created = loadSQL.get(loadSQLIndex).get(head2) + "";
				int indexOf = -1;
				
				if((indexOf = created.indexOf(".")) > 0) {
					created = created.substring(0, indexOf);
				}
				
				String expiry_date = loadSQL.get(loadSQLIndex).get(head3) + "";
				
				if((indexOf = expiry_date.indexOf(".")) > 0) {
					expiry_date = expiry_date.substring(0, indexOf);
				}
				
				String password_expiry_remin_time = loadSQL.get(loadSQLIndex).get(head4) + "";
				String password_grace_time = loadSQL.get(loadSQLIndex).get(head5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(2);
					tableHtml += reportHtml.tableStyle1Head(1, "username", head1);
					tableHtml += reportHtml.tableStyle1Head(2, "created", head2);
					tableHtml += reportHtml.tableStyle1Head(2, "expiry_date", head3);
					tableHtml += reportHtml.tableStyle1Head(2, "password_expiry_remin_time", head4);
					tableHtml += reportHtml.tableStyle1Head(3, "password_grace_time", head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, username, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, created, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, expiry_date, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, password_expiry_remin_time, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, password_grace_time, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, username, 0);
				tableHtml += reportHtml.tableStyle1Body(2, created, 1);
				tableHtml += reportHtml.tableStyle1Body(2, expiry_date, 1);
				tableHtml += reportHtml.tableStyle1Body(2, password_expiry_remin_time, 0);
				tableHtml += reportHtml.tableStyle1Body(3, password_grace_time, 0);
			}
		}
		
		valueIndex = html.indexOf("P113_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P114(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(3, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P114_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P178(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Parameter";
		String head3 = "변경전 Value";
		String head4 = "변경후 Value";
		String head5 = "변경일시";
		
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
		
		valueIndex = html.indexOf("P178_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P179(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "Parameter";
		String head3 = "변경전 Value";
		String head4 = "변경후 Value";
		String head5 = "변경일시";
		
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
		
		valueIndex = html.indexOf("P179_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P180(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head2 = "결과 평가";
		String head3 = "Parameter DB Files";
		String head4 = "생성 파일 수";
		String head5 = "생성률(%)";
		
		String clm_name2 = "결과 평가";
		String clm_name3 = "Parameter DB Files";
		String clm_name4 = "생성파일수";
		String clm_name5 = "생성률(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(3, head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data2 = loadSQL.get(loadSQLIndex).get(clm_name2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(clm_name3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(clm_name4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(clm_name5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(3, head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 2);
			}
		}
		
		valueIndex = html.indexOf("P180_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P181(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "생성률(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P181_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P182(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Hit Ratio(%)";
		String head4 = "Optimal Hit Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P182_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P183(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Hit Ratio(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P183_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P184(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Hit Ratio(%)";
		String head4 = "Optimal Hit Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P184_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P185(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Hit Ratio(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P185_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P186(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Hit Ratio(%)";
		String head4 = "Optimal Hit Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P186_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P187(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Hit Ratio(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P187_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P188(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Hit Ratio(%)";
		String head4 = "Optimal Hit Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P188_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P189(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Hit Ratio(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P189_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P190(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "CPU Time Ratio(%)";
		String head4 = "Optimal CPU Time Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P190_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P191(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "CPU Time Ratio(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P191_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P192(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Disk Sort(%)";
		String head4 = "Optimal Disk Sort(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P192_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P193(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Disk Sort(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P193_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P194(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Shared Pool Usage(%)";
		String head4 = "Optimal Shared Pool Usage(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P194_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P195(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "Instance";
		String clm_value = "Shared Pool Usage(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P195_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P124(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Instance";
		String head2 = "결과 평가";
		String head3 = "Resource";
		String head4 = "Limit";
		String head5 = "Max";
		String head6 = "Utilization Ratio(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2,reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 2);
			}
		}
		
		valueIndex = html.indexOf("P124_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P202(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "FRA 위치";
		String head2 = "결과 평가";
		String head3 = "파일수";
		String head4 = "할당량(GB)";
		String head5 = "사용량(GB)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 2);
			}
		}
		
		valueIndex = html.indexOf("P202_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P203(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String legend1 = "Space Limit(GB)";
		String legend2 = "사용률(GB)";
		
		if(loadSQLSize > 0) {
			ArrayList labelList = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("기준일") + "";
				String data1 = loadSQL.get(loadSQLIndex).get(legend1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(legend2) + "";
				
				labelList.add(label);
				dataList1.add(data1);
				dataList2.add(data2);
			}
			
			legend.add(legend1);
			legend.add(legend2);
			data.add(dataList1);
			data.add(dataList2);
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, labelList, legend, data, true);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P203_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P204(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "파일 유형";
		String head2 = "결과 평가";
		String head3 = "파일수";
		String head4 = "사용률(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, reportHtml.getResultColorIcon((data2 + "").charAt(0)), 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 2);
			}
		}
		
		valueIndex = html.indexOf("P204_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P205(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "파일 유형";
		String clm_value = "사용률(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processMultiData(4, tableHtml, reportHtml, contents_id, loadSQL, clm_label, clm_legend, clm_value);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P205_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P206(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Group Number";
		String head2 = "Disk Group Name";
		String head3 = "State";
		String head4 = "할당량(GB)";
		String head5 = "사용량(GB)";
		String head6 = "사용률(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 2);
			}
		}
		
		valueIndex = html.indexOf("P206_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P207(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "사용률(%)";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P207_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P208(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Tablespace";
		String head2 = "할당량(GB)";
		String head3 = "사용량(GB)";
		String head4 = "사용률(%)";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 1);
			}
		}
		
		valueIndex = html.indexOf("P208_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P209(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String legend1 = "Tablespace";
		String legend2 = "사용률(%)";
		
		if(loadSQLSize > 0) {
			ArrayList labelList = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("기준일") + "";
				String data1 = loadSQL.get(loadSQLIndex).get(legend1) + "";
				String count = loadSQL.get(loadSQLIndex).get(legend2) + "";
				
				labelList.add(label);
				dataList1.add(count);
			}
			
			legend.add(legend1);
			legend.add(legend2);
			data.add(dataList1);
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, labelList, legend, data);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P209_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}

}
