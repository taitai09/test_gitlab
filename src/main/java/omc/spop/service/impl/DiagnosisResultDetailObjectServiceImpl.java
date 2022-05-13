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
import omc.spop.model.Report.DiagnosisResultDetailObject;
import omc.spop.service.DiagnosisResultDetailObjectService;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailObjectService")
public class DiagnosisResultDetailObjectServiceImpl implements DiagnosisResultDetailObjectService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailObjectServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailObject diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
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
				
				if(contents_id.equalsIgnoreCase("P149")) {			// 오브젝트 진단
					html = html;
				} else if(contents_id.equalsIgnoreCase("P150")) {	// Reorg 대상 점검
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P248")) {	// Reorg Object 목록
					html = P248(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P249")) {	// Owner 재사용 가능 공간(GB)
					html = P249(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P250")) {	// Owner별 Reorg 대상 건수
					html = P250(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P151")) {	// 파티셔닝 대상 점검
					html = html;
				} else if(contents_id.equalsIgnoreCase("P251")) {	// 파티셔닝 대상
					html = P251(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P152")) {	// 인덱스 사용 점검
					html = html;
				} else if(contents_id.equalsIgnoreCase("P252")) {	// 최근 1개월 동안 사용하지 않은 인덱스
					html = P252(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P253")) {	// Owner 별 사용/미사용 현황
					html = P253(html , tableHtml, reportHtml, contents_id, loadSQL);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
//		logger.debug("contents_id[" + contents_id + "] html[" + html + "]"); // Used Debug
		queue.add(html);
		
		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P149");
		}
	}
	
	private String getHtml(String contents_id, DiagnosisResultDetailObject diagnosisResultDetail) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P149")) {
			html = diagnosisResultDetail.getP149();
		} else if(contents_id.equalsIgnoreCase("P150")) {
			html = diagnosisResultDetail.getP150();
		} else if(contents_id.equalsIgnoreCase("P248")) {
			html = diagnosisResultDetail.getP248();
		} else if(contents_id.equalsIgnoreCase("P249")) {
			html = diagnosisResultDetail.getP249();
		} else if(contents_id.equalsIgnoreCase("P250")) {
			html = diagnosisResultDetail.getP250();
		} else if(contents_id.equalsIgnoreCase("P151")) {
			html = diagnosisResultDetail.getP151();
		} else if(contents_id.equalsIgnoreCase("P251")) {
			html = diagnosisResultDetail.getP251();
		} else if(contents_id.equalsIgnoreCase("P152")) {
			html = diagnosisResultDetail.getP152();
		} else if(contents_id.equalsIgnoreCase("P252")) {
			html = diagnosisResultDetail.getP252();
		} else if(contents_id.equalsIgnoreCase("P253")) {
			html = diagnosisResultDetail.getP253();
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailObject diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
		String html =  getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P149");

	}
	
	private String P248(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
//		String head1 = "점검일";
		String head2 = "Owner";
		String head3 = "Table Name";
		String head4 = "Partition Name";
		String head5 = "Tablespace";
		String head6 = "Num Rows";
		String head7 = "Allocated Space(GB)";
		String head8 = "Used Space(GB)";
		String head9 = "Reclaimable Space(GB)";
		String head10 = "Reclaimable Ratio(%)";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
//			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(1, head2);
			tableHtml += reportHtml.tableStyle1Head(1, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(2, head7);
			tableHtml += reportHtml.tableStyle1Head(2, head8);
			tableHtml += reportHtml.tableStyle1Head(2, head9);
			tableHtml += reportHtml.tableStyle1Head(3, head10);
			
			tableHtml += reportHtml.tableStyleNoDataClose(9, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
//				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
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
//					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(1, head2);
					tableHtml += reportHtml.tableStyle1Head(1, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(2, head7);
					tableHtml += reportHtml.tableStyle1Head(2, head8);
					tableHtml += reportHtml.tableStyle1Head(2, head9);
					tableHtml += reportHtml.tableStyle1Head(3, head10);
				} 
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
//					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 1);
					tableHtml += reportHtml.tableStyle1BodyClose(1, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data10, 2);
					
					continue;
				}
				
//				tableHtml += reportHtml.tableStyle1Body(1, data1, 1);
				tableHtml += reportHtml.tableStyle1Body(1, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data10, 2);
			}
		}
		
		valueIndex = html.indexOf("P248_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P249(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		if(loadSQLSize > 0) {
			ArrayList<String> labelList = new ArrayList<String>();
//			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
//				String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
				String data2 = loadSQL.get(loadSQLIndex).get("Reclaimable Space(GB)") + "";
				
				labelList.add(label);
//				dataList1.add(data1);
				dataList2.add(data2);
			}
//			
			tableHtml += reportHtml.pieChartStyle(4, contents_id, labelList, dataList2);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P249_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P250(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
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
		
		valueIndex = html.indexOf("P250_C11") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P251(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "현재 사이즈(GB)";
		String head4 = "현재 Rows";
		String head5 = "6개월전 건수";
		String head6 = "5개월전 건수";
		String head7 = "4개월전 건수";
		String head8 = "3개월전 건수";
		String head9 = "2개월전 건수";
		String head10 = "1개월전 건수";
		String head11 = "6개월전 사이즈(GB)";
		String head12 = "5개월전 사이즈(GB)";
		String head13 = "4개월전 사이즈(GB)";
		String head14 = "3개월전 사이즈(GB)";
		String head15 = "2개월전 사이즈(GB)";
		String head16 = "1개월전 사이즈(GB)";
		String head17 = "Read Activity";
		String mergeHead1 = "건수 변화량";
		String mergeHead2 = "사이즈(GB) 변화량";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"6\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"6\">" + mergeHead2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" rowspan=\"2\">" + head17 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head12 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head13 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head14 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head15 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head16 + "</th>\n";
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(17, "해당 진단 항목에 대한 데이터가 없습니다.");
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
				String data14 = loadSQL.get(loadSQLIndex).get(head14) + "";
				String data15 = loadSQL.get(loadSQLIndex).get(head15) + "";
				String data16 = loadSQL.get(loadSQLIndex).get(head16) + "";
				String data17 = loadSQL.get(loadSQLIndex).get(head17) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" rowspan=\"2\">" + head4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"6\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"6\">" + mergeHead2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" rowspan=\"2\">" + head17 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
					
					tableHtml += "\t\t\t\t\t<tr>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head6 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head7 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head8 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head9 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head10 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head11 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head12 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head13 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head14 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head15 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\">" + head16 + "</th>\n";
					tableHtml += "\t\t\t\t\t</tr>\n";
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data12, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data13, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data14, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data15, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data16, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data17, 2);
					
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
				tableHtml += reportHtml.tableStyle1Body(2, data12, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data13, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data14, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data15, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data16, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data17, 2);
			}
		}
		
		valueIndex = html.indexOf("P251_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P252(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "Owner";
		String head2 = "Table Name";
		String head3 = "Index Name";
		String head4 = "PK여부";
		
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
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 1);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 1);
			}
		}
		
		valueIndex = html.indexOf("P252_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P253(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String label1 = "사용";
		String label2 = "미사용";
		String label3 = "미사용률(%)";
		
//		tableHtml += sample_bar_chart_1();
		
		if(loadSQLSize > 0) {
			ArrayList labelList = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			ArrayList dataList3 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label = loadSQL.get(loadSQLIndex).get("Owner") + "";
				String data1 = loadSQL.get(loadSQLIndex).get(label1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(label2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(label3) + "";
				
				labelList.add(label);
				dataList1.add(data1);
				dataList2.add(data2);
				dataList3.add(data3);
			}
			
			legend.add(label1);
			legend.add(label2);
			data.add(dataList1);
			data.add(dataList2);
			
			tableHtml += reportHtml.barChartStyle(4, contents_id, labelList, legend, data);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
//		tableHtml += "<span style=\"color:red\">새로운 챠트 스타일(바 타입 + 테이블 케이스) - P253_C01\n/////////////////////////////////////////////////////////////////////</span>";
		
		valueIndex = html.indexOf("P253_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}

}
