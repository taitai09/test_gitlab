package omc.spop.service.impl;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.controller.DiagnosisReportController;
import omc.spop.dao.DiagnosisReportDao;
import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisResultDetailFault;
import omc.spop.service.DiagnosisResultDetailFaultService;
import omc.spop.utils.DBUtil;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailFaultService")
public class DiagnosisResultDetailFaultServiceImpl implements DiagnosisResultDetailFaultService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailFaultServiceImpl.class);
	DiagnosisReportController diagnosisController = new DiagnosisReportController();

	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailFault diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
		DiagnosisReport diagnosisReport = null;
		String html = getHtml(contents_id, diagnosisResultDetail);
		StopWatch stopWatch = new StopWatch();
		Queue<String> queue = new LinkedList<String>();

		String orgSql = "";

		for (int sqlIndex = 0; sqlIndex < sqlList.size(); sqlIndex++) {
			try {
				stopWatch.reset();
				stopWatch.start();

				diagnosisReport = sqlList.get(sqlIndex);
				orgSql = diagnosisReport.getSql();

				System.out.println("-" + diagnosisReport.getSlt_program_sql_number());

				int slt_program_sql_number = diagnosisReport.getSlt_program_sql_number();
				String tableHtml = "";
				List<LinkedHashMap<String, Object>> loadSQL = null;


				if (contents_id.equalsIgnoreCase("P177")) {
					try {
						int idx = 0;
						while (true) {
							idx++;
							diagnosisReport.setSql(orgSql);
							diagnosisReport.setSql(setValue(diagnosisReport,reportHtml));

							if(StringUtils.isEmpty(diagnosisReport.getSql())) {
								break;
							}
							loadSQL = diagnosisReportDao.loadSQL(diagnosisReport);
							if (loadSQL.size() == 0) {
								queue.add("</div>\n</div>\n");

								try {
									diagnosisController.intermediateHtmlWrite_include(queue, "P177");
								} catch (Exception e) {
									throw new Exception(String.valueOf(diagnosisReport.getSlt_program_sql_number()));
								}
								break;
							}

							if (contents_id.equalsIgnoreCase("P177")) { // SQL Text List
								P177(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number, idx);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new Exception();
					}
				} else {
					loadSQL = diagnosisReportDao.loadSQL(diagnosisReport);

					if (contents_id.equalsIgnoreCase("P172")) { // 장애 예측 분석
						html = html;
					} else if (contents_id.equalsIgnoreCase("P173")) { // CPU 한계점 예측
						html = reportHtml.replaceVariable(html, tableHtml, reportHtml, contents_id, loadSQL,
								slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P283")) { // CPU 한계점 예측 현황
						html = P283(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P284")) { // CPU 사용률(%)
						html = P284(html, tableHtml, reportHtml, contents_id, loadSQL);
					} else if (contents_id.equalsIgnoreCase("P174")) { // Sequence 한계점 예측
						html = reportHtml.replaceVariable(html, tableHtml, reportHtml, contents_id, loadSQL,
								slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P285")) { // 1년 이내 한계점에 도달하는 Sequence
						html = P285(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P286")) { // Sequence 사용량 증가 추이
						html = P286(html, tableHtml, reportHtml, contents_id, loadSQL);
					} else if (contents_id.equalsIgnoreCase("P175")) { // Tablespace 한계점 예측
						html = reportHtml.replaceVariable(html, tableHtml, reportHtml, contents_id, loadSQL,
								slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P287")) { // 1개월 이내 한계점에 도달하는 Tablespace
						html = P287(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P288")) { // Tablespace 사용량 증가 추이
						html = P288(html, tableHtml, reportHtml, contents_id, loadSQL);
					} else if (contents_id.equalsIgnoreCase("P176")) { // 신규 SQL 타임아웃 예측
						html = reportHtml.replaceVariable(html, tableHtml, reportHtml, contents_id, loadSQL,
								slt_program_sql_number);
					} else if (contents_id.equalsIgnoreCase("P289")) { // 최근 1개월 신규 SQL 발생건수
						html = P289(html, tableHtml, reportHtml, contents_id, loadSQL);
					} else if (contents_id.equalsIgnoreCase("P290")) { // 1개월 후 타임아웃 발생 신규 SQL수행시간 추이
						html = P290(html, tableHtml, reportHtml, contents_id, loadSQL);
					} else if (contents_id.equalsIgnoreCase("P291")) { // 1개월 후 타임아웃 발생 신규 SQL 현황
						html = P291(html, tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
					}
				}
			} catch (Exception ex) {
				throw new Exception(String.valueOf(diagnosisReport.getSlt_program_sql_number()));
			}
		}
		if (contents_id.equalsIgnoreCase("P177") == false) {
			queue.add(html);

			try {
				diagnosisController.intermediateHtmlWrite_include(queue, "P172");
			} catch (Exception e) {
				throw new Exception(String.valueOf(diagnosisReport.getSlt_program_sql_number()));
			}

		}

	}
	private String setValue(DiagnosisReport diagnosisReport , ReportHtml reportHtml) throws Exception {
		
		HashSet<String> sqlIdSet = reportHtml.getSqlIdSet();
		HashSet<String> sqlIdSetClone = (HashSet<String>) sqlIdSet.clone();
		
		if(sqlIdSetClone == null) {
			return "";
		}
		
		Iterator iter = sqlIdSetClone.iterator();	// Iterator 사용
		
		StringBuilder sb = new StringBuilder();
		String sql = diagnosisReport.getSql();

		int size = sqlIdSet.size();
		int length = size;
		int idx = 0 ;
		int queueThresHold = 100;
		if(size > queueThresHold) {
			length = queueThresHold;
		}
//		(:SQL_ID1)
		if(iter.hasNext() == false) {
			return "";
		}
		while(iter.hasNext()) {
			
			if(length == idx) break;

			String id = iter.next().toString();
			sb.append("'" + id +"' ,");
			sqlIdSet.remove(id);
			idx ++;
			
		}
		
		String str= sb.toString();
		sql = sql.replace("#{sql_id}", str.substring(0, str.length()-1));
		return sql;
	}

	private String getHtml(String contents_id, DiagnosisResultDetailFault diagnosisResultDetail) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P172")) {
			html = diagnosisResultDetail.getP172();
		} else if(contents_id.equalsIgnoreCase("P173")) {
			html = diagnosisResultDetail.getP173();
		} else if(contents_id.equalsIgnoreCase("P283")) {
			html = diagnosisResultDetail.getP283();
		} else if(contents_id.equalsIgnoreCase("P284")) {
			html = diagnosisResultDetail.getP284();
		} else if(contents_id.equalsIgnoreCase("P174")) {
			html = diagnosisResultDetail.getP174();
		} else if(contents_id.equalsIgnoreCase("P285")) {
			html = diagnosisResultDetail.getP285();
		} else if(contents_id.equalsIgnoreCase("P286")) {
			html = diagnosisResultDetail.getP286();
		} else if(contents_id.equalsIgnoreCase("P175")) {
			html = diagnosisResultDetail.getP175();
		} else if(contents_id.equalsIgnoreCase("P287")) {
			html = diagnosisResultDetail.getP287();
		} else if(contents_id.equalsIgnoreCase("P288")) {
			html = diagnosisResultDetail.getP288();
		} else if(contents_id.equalsIgnoreCase("P176")) {
			html = diagnosisResultDetail.getP176();
		} else if(contents_id.equalsIgnoreCase("P289")) {
			html = diagnosisResultDetail.getP289();
		} else if(contents_id.equalsIgnoreCase("P290")) {
			html = diagnosisResultDetail.getP290();
		} else if(contents_id.equalsIgnoreCase("P291")) {
			html = diagnosisResultDetail.getP291();
		} else if(contents_id.equalsIgnoreCase("P177")) {
			html = diagnosisResultDetail.getP177();
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailFault diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
		String html = getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail);
		String fileName = "P172";
		if(diagnosisReport.getContents_id().equalsIgnoreCase("P177")) {
			fileName = "P177";
		}
		new DiagnosisReportController().intermediateHtmlWrite_include(html, fileName);
	}
	
	private String P283(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "Instance";
		String head2 = "예측일시";
		String head3 = "3개월전";
		String head4 = "2개월전";
		String head5 = "1개월전";
		String head6 = "현재";
		String head7 = "1개월후";
		String head8 = "2개월후";
		String head9 = "3개월후";
		String head10 = "6개월후";
		String head11 = "12개월후";
		String mergeHead1 = "CPU 사용률(%)";
		
		String col1 = "Instance";
		String col2 = "예측일시";
		String col3 = "3개월전 CPU(%)";
		String col4 = "2개월전 CPU(%)";
		String col5 = "1개월전 CPU(%)";
		String col6 = "현재 CPU(%)";
		String col7 = "1개월후 CPU(%)";
		String col8 = "2개월후 CPU(%)";
		String col9 = "3개월후 CPU(%)";
		String col10 = "6개월후 CPU(%)";
		String col11 = "12개월후 CPU(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head11 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(11, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(col1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(col2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(col3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(col4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(col5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(col6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(col7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(col8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(col9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(col10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(col11) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
					
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head11 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data11, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data11, 2);
			}
		}
		
		valueIndex = html.indexOf("P283_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Use Debug
		
		return html;
	}

	/*
	 * 특이 케이스
	 */
	private String P284(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String legend1 = "Instance";
		String label1 = "3개월전";
		String label2 = "2개월전";
		String label3 = "1개월전";
		String label4 = "현재";
		String label5 = "1개월후";
		String label6 = "2개월후";
		String label7 = "3개월후";
		String label8 = "6개월후";
		String label9 = "12개월후";
		
		if(loadSQLSize > 0) {
			ArrayList label = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				LinkedHashMap<String, Object> temp = loadSQL.get(loadSQLIndex);
				dataList1 = new ArrayList();
				
				legend.add(temp.get(legend1));
				
				dataList1.add(temp.get(label1));
				dataList1.add(temp.get(label2));
				dataList1.add(temp.get(label3));
				dataList1.add(temp.get(label4));
				dataList1.add(temp.get(label5));
				dataList1.add(temp.get(label6));
				dataList1.add(temp.get(label7));
				dataList1.add(temp.get(label8));
				dataList1.add(temp.get(label9));
				
				data.add(dataList1);
			}
			
			label.add("'" + label1 + "'");
			label.add("'" + label2 + "'");
			label.add("'" + label3 + "'");
			label.add("'" + label4 + "'");
			label.add("'" + label5 + "'");
			label.add("'" + label6 + "'");
			label.add("'" + label7 + "'");
			label.add("'" + label8 + "'");
			label.add("'" + label9 + "'");
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, label, legend, data, true);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P284_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P285(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "Owner";
		String head2 = "Sequence Name";
		String head3 = "Max Value";
		String head4 = "3개월 전";
		String head5 = "2개월 전";
		String head6 = "1개월 전";
		String head7 = "현재";
		String head8 = "1개월 후";
		String head9 = "2개월 후";
		String head10 = "3개월 후";
		String head11 = "6개월 후";
		String head12 = "12개월 후";
		String mergeHead1 = "Sequence 사용률(%)";
		
		String col1 = "Owner";
		String col2 = "Sequence Name";
		String col3 = "Max Value";
		String col4 = "3개월전 SEQ사용률(%)";
		String col5 = "2개월전 SEQ사용률(%)";
		String col6 = "1개월전 SEQ사용률(%)";
		String col7 = "현재 SEQ사용률(%)";
		String col8 = "1개월후 SEQ사용률(%)";
		String col9 = "2개월후 SEQ사용률(%)";
		String col10 = "3개월후 SEQ사용률(%)";
		String col11 = "6개월후 SEQ사용률(%)";
		String col12 = "12개월후 SEQ사용률(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head12 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(12, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(col1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(col2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(col3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(col4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(col5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(col6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(col7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(col8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(col9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(col10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(col11) + "";
				String data12 = loadSQL.get(loadSQLIndex).get(col12) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
					
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head12 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data11, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data12, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data11, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data12, 2);
			}
		}
		
		valueIndex = html.indexOf("P285_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P286(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String legend1 = "Sequence Name";
		String label1 = "3개월전";
		String label2 = "2개월전";
		String label3 = "1개월전";
		String label4 = "현재";
		String label5 = "1개월후";
		String label6 = "2개월후";
		String label7 = "3개월후";
		String label8 = "6개월후";
		String label9 = "12개월후";
		
		if(loadSQLSize > 0) {
			ArrayList label = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				LinkedHashMap<String, Object> temp = loadSQL.get(loadSQLIndex);
				dataList1 = new ArrayList();
				
				legend.add(temp.get(legend1));
				
				dataList1.add(temp.get(label1));
				dataList1.add(temp.get(label2));
				dataList1.add(temp.get(label3));
				dataList1.add(temp.get(label4));
				dataList1.add(temp.get(label5));
				dataList1.add(temp.get(label6));
				dataList1.add(temp.get(label7));
				dataList1.add(temp.get(label8));
				dataList1.add(temp.get(label9));
				
				data.add(dataList1);
			}
			
			label.add("'" + label1 + "'");
			label.add("'" + label2 + "'");
			label.add("'" + label3 + "'");
			label.add("'" + label4 + "'");
			label.add("'" + label5 + "'");
			label.add("'" + label6 + "'");
			label.add("'" + label7 + "'");
			label.add("'" + label8 + "'");
			label.add("'" + label9 + "'");
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, label, legend, data, true);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P286_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P287(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "Tablespace";
		String head2 = "할당량";
		String head3 = "사용량";
		String head4 = "3개월 전";
		String head5 = "2개월 전";
		String head6 = "1개월 전";
		String head7 = "현재";
		String head8 = "1개월 후";
		String head9 = "2개월 후";
		String head10 = "3개월 후";
		String head11 = "6개월 후";
		String head12 = "12개월 후";
		String mergeHead1 = "사이즈(GB)";
		String mergeHead2 = "사용률(%)";
		
		String col1 = "Tablespace";
		String col2 = "할당량(GB)";
		String col3 = "사용량(GB)";
		String col4 = "3개월전 사용률(%)";
		String col5 = "2개월전 사용률(%)";
		String col6 = "1개월전 사용률(%)";
		String col7 = "현재 사용률(%)";
		String col8 = "1개월후 사용률(%)";
		String col9 = "2개월후 사용률(%)";
		String col10 = "3개월후 사용률(%)";
		String col11 = "6개월후 사용률(%)";
		String col12 = "12개월후 사용률(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead2 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head12 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(12, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(col1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(col2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(col3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(col4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(col5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(col6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(col7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(col8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(col9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(col10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(col11) + "";
				String data12 = loadSQL.get(loadSQLIndex).get(col12) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"9\">" + mergeHead2 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
					
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head12 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data11, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data12, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data11, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data12, 2);
			}
		}
		
		valueIndex = html.indexOf("P287_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P288(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String legend1 = "Tablespace";
		String label1 = "3개월전";
		String label2 = "2개월전";
		String label3 = "1개월전";
		String label4 = "현재";
		String label5 = "1개월후";
		String label6 = "2개월후";
		String label7 = "3개월후";
		String label8 = "6개월후";
		String label9 = "12개월후";
		
		if(loadSQLSize > 0) {
			ArrayList label = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				LinkedHashMap<String, Object> temp = loadSQL.get(loadSQLIndex);
				dataList1 = new ArrayList();
				
				legend.add(temp.get(legend1));
				
				dataList1.add(temp.get(label1));
				dataList1.add(temp.get(label2));
				dataList1.add(temp.get(label3));
				dataList1.add(temp.get(label4));
				dataList1.add(temp.get(label5));
				dataList1.add(temp.get(label6));
				dataList1.add(temp.get(label7));
				dataList1.add(temp.get(label8));
				dataList1.add(temp.get(label9));
				
				data.add(dataList1);
			}
			
			label.add("'" + label1 + "'");
			label.add("'" + label2 + "'");
			label.add("'" + label3 + "'");
			label.add("'" + label4 + "'");
			label.add("'" + label5 + "'");
			label.add("'" + label6 + "'");
			label.add("'" + label7 + "'");
			label.add("'" + label8 + "'");
			label.add("'" + label9 + "'");
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, label, legend, data, true);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P288_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P289(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String legend1 = "건수";
		
		if(loadSQLSize > 0) {
			ArrayList labelList = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("최초발생일") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get(legend1) + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
			
			legend.add(legend1);
			data.add(dataList2);
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, labelList, legend, data);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P289_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P290(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String legend1 = "SQL_ID";
		String label1 = "1주일후 수행시간(S)";
		String label2 = "2주일후 수행시간(S)";
		String label3 = "3주일후 수행시간(S)";
		String label4 = "1개월후 수행시간(S)";
		String label5 = "2개월후 수행시간(S)";
		String label6 = "3개월후 수행시간(S)";
		String label7 = "6개월후 수행시간(S)";
		String label8 = "12개월후 수행시간(S)";
		
		if(loadSQLSize > 0) {
			ArrayList label = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				LinkedHashMap<String, Object> temp = loadSQL.get(loadSQLIndex);
				dataList1 = new ArrayList();
				
				legend.add(temp.get(legend1));
				
				dataList1.add(temp.get(label1));
				dataList1.add(temp.get(label2));
				dataList1.add(temp.get(label3));
				dataList1.add(temp.get(label4));
				dataList1.add(temp.get(label5));
				dataList1.add(temp.get(label6));
				dataList1.add(temp.get(label7));
				dataList1.add(temp.get(label8));
				
				data.add(dataList1);
			}
			
			label.add("'" + label1 + "'");
			label.add("'" + label2 + "'");
			label.add("'" + label3 + "'");
			label.add("'" + label4 + "'");
			label.add("'" + label5 + "'");
			label.add("'" + label6 + "'");
			label.add("'" + label7 + "'");
			label.add("'" + label8 + "'");
			
			tableHtml += reportHtml.lineChartStyle(4, contents_id, label, legend, data, true);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P290_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P291(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "SQL_ID";
		String head2 = "최초 수행일";
		String head3 = "최종 수행일";
		String head4 = "수행일";
		String head5 = "일 최대 수행횟수";
		String head6 = "1주일후";
		String head7 = "2주일후";
		String head8 = "3주일후";
		String head9 = "1개월후";
		String head10 = "2개월후";
		String head11 = "3개월후";
		String head12 = "6개월후";
		String head13 = "12개월후";
		String mergeHead1 = "Elapsed Time(second)";
		
		String col1 = "SQL_ID";
		String col2 = "최초 수행일";
		String col3 = "최종 수행일";
		String col4 = "수행일";
		String col5 = "일 최대 수행횟수";
		String col6 = "1주일후 수행시간(S)";
		String col7 = "2주일후 수행시간(S)";
		String col8 = "3주일후 수행시간(S)";
		String col9 = "1개월후 수행시간(S)";
		String col10 = "2개월후 수행시간(S)";
		String col11 = "3개월후 수행시간(S)";
		String col12 = "6개월후 수행시간(S)";
		String col13 = "12개월후 수행시간(S)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"8\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head12 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head13 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(13, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(col1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(col2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(col3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(col4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(col5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(col6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(col7) + "";
				String data8 = loadSQL.get(loadSQLIndex).get(col8) + "";
				String data9 = loadSQL.get(loadSQLIndex).get(col9) + "";
				String data10 = loadSQL.get(loadSQLIndex).get(col10) + "";
				String data11 = loadSQL.get(loadSQLIndex).get(col11) + "";
				String data12 = loadSQL.get(loadSQLIndex).get(col12) + "";
				String data13 = loadSQL.get(loadSQLIndex).get(col13) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" colspan=\"8\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
					
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head12 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\">" + head13 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n";
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data11, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data12, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data13, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 1);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data11, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data12, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data13, 2);;
			}
		}
		
		valueIndex = html.indexOf("P291_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private void P177(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL,
			int sqlIndex, int slt_program_sql_number, int count) throws Exception {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "SQL Text";

		int queueThreshold = 100;

		Queue<String> queue = new LinkedList<String>();
		Queue<String> sqlQueue = new LinkedList<String>();

		valueIndex = html.indexOf("P177_T01") + 10;

		StringBuilder sb = new StringBuilder();
//		sb.append("</div>\n");
		if (count == 1) {
//			sb.append("<iframe src='/resources/db_report/include/P177.html' id=\"tab-7\" class=\"tab-content\"></iframe>\n");
//			diagnosisController.intermediateHtmlWrite(sb);

			sb.setLength(0);
			// sb.append("<div id=\"tab-7\" class=\"tab-content\">\n");
			sb.append(reportHtml.getWrapper_css());
			sb.append(diagnosisController.referenceCSS_include());
			sb.append(diagnosisController.referenceLib_include());
			sb.append(diagnosisController.getTextListJs());
			sb.append("<style type=\"text/css\">\n");
			sb.append("#P177_T01 table { table-layout:fixed;}");
			sb.append("#P177_T01 table tr td:nth-child(1){ width:90px;}");
			sb.append("#P177_T01 table tr th:nth-child(1){ width:90px;}");
			sb.append("</style>\n");

			sb.append(html.substring(0, valueIndex));
			sb.append(tableHtml);
		}
		try {

			if (count == 1 && loadSQLSize == 0) {
				sb.append(reportHtml.tableStyle4(2));
				sb.append(reportHtml.tableStyle1Head(1, head1));
				sb.append(reportHtml.tableStyle1Head(3, head2));

				sb.append(reportHtml.tableStyleNoDataClose(2, "해당 진단 항목에 대한 데이터가 없습니다."));
			} else {
				for (int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
					String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
					String data2 = DBUtil.ClobtoString((Clob) loadSQL.get(loadSQLIndex).get(head2));
					String id = data1;
					data1 = "<span>" + data1 + "</span>";
					data2 = "<span>" + data2 + "</span>";

//					data2 = data2.replaceAll("\t", " ");
//					data2 = data2.replaceAll("\\s+", " ");
					data2 = "<pre style='word-break:break-all;white-space:break-spaces;'>" + data2 + "</pre>";
					if (count == 1 && loadSQLIndex == 0) {
						// 1 Row
						sb.append(reportHtml.tableStyle5(2, id));
						sb.append(reportHtml.tableStyle1Head(1, head1));
						sb.append(reportHtml.tableStyle1Head(3, head2));
					} else {
						sb.append(reportHtml.tableStyle5(2, id));
					}

//					if (loadSQLIndex == (loadSQLSize - 1)) {
//						sb.append(reportHtml.tableStyle1BodyClose(1, data1, 0));
//						sb.append(reportHtml.tableStyle1BodyCloseFont(3, data2, 0));
//
//						continue;
//					}

					sb.append(reportHtml.tableStyle1Body(1, data1, 0));
					sb.append(reportHtml.tableStyle1BodyFont(3, data2, 0));

					queue.add(sb.toString());
					sb.setLength(0);

				}

				queue.add("</table>");
//				String endTHtml = html.substring(valueIndex);
//				queue.add(endTHtml);
				diagnosisController.intermediateHtmlWrite_include(queue, "P177");
			}

		} catch (Exception e) {
			String endTHtml = html.substring(valueIndex);
			queue.add(endTHtml);
			diagnosisController.intermediateHtmlWrite_include(queue, "P177");
		}

//		logger.debug("P177 sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
	}

}
