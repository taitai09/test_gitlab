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
import omc.spop.model.Report.DiagnosisOverview;
import omc.spop.service.DiagnosisReportService;
import omc.spop.utils.CryptoUtil;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisReportService")
public class DiagnosisReportServiceImpl implements DiagnosisReportService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisReportServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	private String key = "openmade";
	
	@Override
	public List<DiagnosisReport> loadSLTProgramContents(DiagnosisReport diagnosisReport) throws Exception {
		return diagnosisReportDao.loadSLTProgramContents(diagnosisReport);
	}
	
	@Override
	public List<DiagnosisReport> getSQL(DiagnosisReport diagnosisReport) throws Exception {
		List<DiagnosisReport> reportList = diagnosisReportDao.getSQL(diagnosisReport);
		int listSize = reportList.size();
		
		for(int listIndex = 0; listIndex < listSize; listIndex++) {
			DiagnosisReport tempReport = reportList.get(listIndex);
			String slt_program_chk_sql = CryptoUtil.decryptAES128(tempReport.getSlt_program_chk_sql(),key);
//			logger.debug("getSQL listIndex[" + listIndex + "] slt_program_chk_sql[" + slt_program_chk_sql + "]"); // Used Debug
			
			tempReport.setSlt_program_chk_sql(slt_program_chk_sql);
			
			reportList.set(listIndex, tempReport);
		}
		
		return reportList;
	}
	
	@Override
	public List<DiagnosisReport> getSQLUnit(DiagnosisReport diagnosisReport) throws Exception {
		List<DiagnosisReport> reportList = diagnosisReportDao.getSQLUnit(diagnosisReport);
		int listSize = reportList.size();
		
		for(int listIndex = 0; listIndex < listSize; listIndex++) {
			DiagnosisReport tempReport = reportList.get(listIndex);
			String slt_program_chk_sql = CryptoUtil.decryptAES128(tempReport.getSlt_program_chk_sql(),key);
//			logger.debug("getSQLUnit listIndex[" + listIndex + "] slt_program_chk_sql[" + slt_program_chk_sql + "]"); // Used Debug
			
			tempReport.setSlt_program_chk_sql(slt_program_chk_sql);
			
			reportList.set(listIndex, tempReport);
		}
		
		return reportList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadSQLUnit(DiagnosisReport diagnosisReport) throws Exception {
		return diagnosisReportDao.loadSQLUnit(diagnosisReport);
	}
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisOverview diagnosisOverview, ReportHtml reportHtml) throws Exception {
		DiagnosisReportController diagnosisReportController = new DiagnosisReportController();
		
		DiagnosisReport diagnosisReport;
		String html = getHtml(contents_id, diagnosisOverview);
		int valueIndex = -1;
		String endHtml = "";
		Queue<String> queue = new LinkedList<String>();

		try {
		
		for(int sqlIndex = 0; sqlIndex < sqlList.size(); sqlIndex++) {
			diagnosisReport = sqlList.get(sqlIndex);
			
			String tableHtml = "";
			List<LinkedHashMap<String, Object>> loadSQL = diagnosisReportDao.loadSQL(diagnosisReport);
//			logger.debug("loadSQL[" + loadSQL + "]"); // Used Debug
			
			if(contents_id.equalsIgnoreCase("P101")) {
				valueIndex = html.indexOf("P101_value");
				endHtml = html.substring(valueIndex + 10);
				html = html.substring(0, valueIndex) + loadSQL.get(0).get("DIAG_DT").toString() + endHtml;
			} else if(contents_id.equalsIgnoreCase("P102")) {
				valueIndex = html.indexOf("P102_value");
				endHtml = html.substring(valueIndex + 10);
				html = html.substring(0, valueIndex) + loadSQL.get(0).get("DIAG_START_DT").toString() + " ~ " + loadSQL.get(0).get("DIAG_END_DT").toString() + endHtml;
			} else if(contents_id.equalsIgnoreCase("P103")) {
				int loadSQLSize = loadSQL.size();
				
				if(loadSQLSize == 0) {
					if(sqlIndex == 0) {
						tableHtml += reportHtml.tableStyle1(2);
						tableHtml += reportHtml.tableStyle1Head(1, "db", "DB", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "dbid", "DBID", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "instance_name", "Instance", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "inst_id", "Inst ID", 10);
						tableHtml += reportHtml.tableStyle1Head(2, "startup_time", "Startup Time", 15);
						tableHtml += reportHtml.tableStyle1Head(3, "version", "Version", 15);
						
						tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
						
						valueIndex = html.indexOf("P103_T01") + 10;
						String endTHtml = html.substring(valueIndex);
						html = html.substring(0, valueIndex) + tableHtml + endTHtml;
						
						break;
					} else if(sqlIndex == 1) {
						tableHtml += reportHtml.tableStyle1(2);
						tableHtml += reportHtml.tableStyle1Head(1, "host_name", "Host Name", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "platform_name", "Platform", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "cpu", "CPUs", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "core", "Cores", 10);
						tableHtml += reportHtml.tableStyle1Head(2, "socket", "Sockets", 15);
						tableHtml += reportHtml.tableStyle1Head(3, "memory", "Memory(GB)", 15);
						
						tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
						
						valueIndex = html.indexOf("P103_T02") + 10;
						String endTHtml = html.substring(valueIndex);
						html = html.substring(0, valueIndex) + tableHtml + endTHtml;
						
//						logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
					} else {
						tableHtml += reportHtml.tableStyle1(2);
						tableHtml += reportHtml.tableStyle1Head(1, "host_name", "Inst ID", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "platform_name", "Diag Begin Time", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "cpu", "Diag End Time", 20);
						tableHtml += reportHtml.tableStyle1Head(2, "core", "Elapsed Time", 20);
						tableHtml += reportHtml.tableStyle1Head(3, "socket", "DB Time", 20);
						
						tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
						
						valueIndex = html.indexOf("P103_T03") + 10;
						String endTHtml = html.substring(valueIndex);
						html = html.substring(0, valueIndex) + tableHtml + endTHtml;
						
//						logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
					}
				}
				
				for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
					if(sqlIndex == 0) {
						String db = loadSQL.get(loadSQLIndex).get("DB") + "";
						String dbid = loadSQL.get(loadSQLIndex).get("DBID") + "";
						String instance_name = loadSQL.get(loadSQLIndex).get("INSTANCE_NAME") + "";
						String inst_id = loadSQL.get(loadSQLIndex).get("INST_ID") + "";
						String startup_time = loadSQL.get(loadSQLIndex).get("STARTUP_TIME") + "";
						String version = loadSQL.get(loadSQLIndex).get("VERSION") + "";
						
						if(loadSQLIndex == 0) {
							// 1 Row
							tableHtml += reportHtml.tableStyle1(2);
							tableHtml += reportHtml.tableStyle1Head(1, "db", "DB", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "dbid", "DBID", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "instance_name", "Instance", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "inst_id", "Inst ID", 10);
							tableHtml += reportHtml.tableStyle1Head(2, "startup_time", "Startup Time", 15);
							tableHtml += reportHtml.tableStyle1Head(3, "version", "Version", 15);
						}
						
						if(loadSQLIndex == (loadSQLSize - 1)) {
							tableHtml += reportHtml.tableStyle1BodyClose(1, db, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, dbid, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, instance_name, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, inst_id, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, startup_time, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(3, version, 1);
							
							valueIndex = html.indexOf("P103_T01") + 10;
							String endTHtml = html.substring(valueIndex);
							html = html.substring(0, valueIndex) + tableHtml + endTHtml;
							
//							logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
							
							continue;
						}
						
						tableHtml += reportHtml.tableStyle1Body(1, db, 1);
						tableHtml += reportHtml.tableStyle1Body(2, dbid, 1);
						tableHtml += reportHtml.tableStyle1Body(2, instance_name, 1);
						tableHtml += reportHtml.tableStyle1Body(2, inst_id, 1);
						tableHtml += reportHtml.tableStyle1Body(2, startup_time, 1);
						tableHtml += reportHtml.tableStyle1Body(3, version, 1);
					} else if(sqlIndex == 1) {
						String host_name = loadSQL.get(loadSQLIndex).get("HOST_NAME") + "";
						String platform_name = loadSQL.get(loadSQLIndex).get("PLATFORM_NAME") + "";
						String cpu = loadSQL.get(loadSQLIndex).get("CPU") + "";
						String core = loadSQL.get(loadSQLIndex).get("CORE") + "";
						String socket = loadSQL.get(loadSQLIndex).get("SOCKET") + "";
						String memory = loadSQL.get(loadSQLIndex).get("MEMORY") + "";
						
						if(loadSQLIndex == 0) {
							tableHtml += reportHtml.tableStyle1(2);
							tableHtml += reportHtml.tableStyle1Head(1, "host_name", "Host Name", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "platform_name", "Platform", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "cpu", "CPUs", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "core", "Cores", 10);
							tableHtml += reportHtml.tableStyle1Head(2, "socket", "Sockets", 15);
							tableHtml += reportHtml.tableStyle1Head(3, "memory", "Memory(GB)", 15);
						}
						
						if(loadSQLIndex == (loadSQLSize - 1)) {
							tableHtml += reportHtml.tableStyle1BodyClose(1, host_name, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, platform_name, 0);
							tableHtml += reportHtml.tableStyle1BodyClose(2, cpu, 2);
							tableHtml += reportHtml.tableStyle1BodyClose(2, core, 2);
							tableHtml += reportHtml.tableStyle1BodyClose(2, socket, 2);
							tableHtml += reportHtml.tableStyle1BodyClose(3, memory, 2);
							
							valueIndex = html.indexOf("P103_T02") + 10;
							String endTHtml = html.substring(valueIndex);
							html = html.substring(0, valueIndex) + tableHtml + endTHtml;
							
//							logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
							
							continue;
						}
						
						tableHtml += reportHtml.tableStyle1Body(1, host_name, 1);
						tableHtml += reportHtml.tableStyle1Body(2, platform_name, 0);
						tableHtml += reportHtml.tableStyle1Body(2, cpu, 2);
						tableHtml += reportHtml.tableStyle1Body(2, core, 2);
						tableHtml += reportHtml.tableStyle1Body(2, socket, 2);
						tableHtml += reportHtml.tableStyle1Body(3, memory, 2);
					} else {
						String instance_number = loadSQL.get(loadSQLIndex).get("INSTANCE_NUMBER") + "";
						String begin_time = loadSQL.get(loadSQLIndex).get("BEGIN_TIME") + "";
						String end_time = loadSQL.get(loadSQLIndex).get("END_TIME") + "";
						String elapsed = loadSQL.get(loadSQLIndex).get("ELAPSED") + "";
						String db_time = loadSQL.get(loadSQLIndex).get("DB_TIME") + "";
						
						if(loadSQLIndex == 0) {
							tableHtml += reportHtml.tableStyle2(2);
							tableHtml += reportHtml.tableStyle1Head(1, "host_name", "Inst ID", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "platform_name", "Diag Begin Time", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "cpu", "Diag End Time", 20);
							tableHtml += reportHtml.tableStyle1Head(2, "core", "Elapsed Time", 20);
							tableHtml += reportHtml.tableStyle1Head(3, "socket", "DB Time", 20);
						}
						
						if(loadSQLIndex == (loadSQLSize - 1)) {
							tableHtml += reportHtml.tableStyle1BodyClose(1, instance_number, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, begin_time, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, end_time, 1);
							tableHtml += reportHtml.tableStyle1BodyClose(2, elapsed, 2);
							tableHtml += reportHtml.tableStyle1BodyClose(3, db_time, 2);
							
							valueIndex = html.indexOf("P103_T03") + 10;
							String endTHtml = html.substring(valueIndex);
							html = html.substring(0, valueIndex) + tableHtml + endTHtml;
							
//							logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
							
							continue;
						}
						
						tableHtml += reportHtml.tableStyle1Body(1, instance_number, 1);
						tableHtml += reportHtml.tableStyle1Body(2, begin_time, 1);
						tableHtml += reportHtml.tableStyle1Body(2, end_time, 1);
						tableHtml += reportHtml.tableStyle1Body(2, elapsed, 2);
						tableHtml += reportHtml.tableStyle1Body(3, db_time, 2);
					}
				}
				
			} else if(contents_id.equalsIgnoreCase("P104")) {
			}
		}
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		queue.add(html);
		
		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P010");
		}

	}
	
	private String getHtml(String contents_id, DiagnosisOverview diagnosisOverview) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P010")) {
			html = diagnosisOverview.getP010();
		} else if(contents_id.equalsIgnoreCase("P101")) {
			html = diagnosisOverview.getP101();
		} else if(contents_id.equalsIgnoreCase("P102")) {
			html = diagnosisOverview.getP102();
		} else if(contents_id.equalsIgnoreCase("P103")) {
			html = diagnosisOverview.getP103();
		} else if(contents_id.equalsIgnoreCase("P104")) {
			html = diagnosisOverview.getP104();
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisOverview diagnosisOverview, ReportHtml reportHtml) throws Exception {
		String html = getHtml(diagnosisReport.getContents_id(), diagnosisOverview);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P010");

	}
}
