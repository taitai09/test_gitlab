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
import omc.spop.model.Report.DiagnosisResultDetailSQL;
import omc.spop.service.DiagnosisResultDetailSQLService;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultDetailSQLService")
public class DiagnosisResultDetailSQLServiceImpl implements DiagnosisResultDetailSQLService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultDetailSQLServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResultDetailSQL diagnosisResultDetail, ReportHtml reportHtml) throws Exception {
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
				
				if(contents_id.equalsIgnoreCase("P155")) {			// SQL 성능 진단
					html = html;
				} else if(contents_id.equalsIgnoreCase("P156")) {	// 성능점검 임계값
					html = P156(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P157")) {	// 1개월 동안 성능점검대상 SQL 발생 현황
					html = P157(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P158")) {	// Plan 변경
					html = html;
				} else if(contents_id.equalsIgnoreCase("P255")) {	// 최근 1개월 Plan 변경 SQL 발생 현황
					html = P255(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P256")) {	// 최근 1개월 Plan 변경 SQL
					html = P256(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P159")) {	// 신규 SQL
					html = html;
				} else if(contents_id.equalsIgnoreCase("P257")) {	// 최근 1개월 신규 SQL 발생 현황
					html = P257(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P258")) {	// 최근 1개월 신규 SQL
					html = P258(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P160")) {	// Literal SQL
					html = html;
				} else if(contents_id.equalsIgnoreCase("P259")) {	// 최근 1개월 Literal SQL 발생 현황
					html = P259(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P260")) {	// 최근 1개월 Literal SQL
					html = P260(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P161")) {	// Temp 과다 사용 SQL
					html = html;
				} else if(contents_id.equalsIgnoreCase("P261")) {	// 최근 1개월 Temp 과다 사용 SQL 발생 현황
					html = P261(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P262")) {	// 최근 1개월 Temp 과다 사용 SQL
					html = P262(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P162")) {	// Full Scan SQL
					html = html;
				} else if(contents_id.equalsIgnoreCase("P263")) {	// 최근 1개월 Full Scan SQL 발생 현황
					html = P263(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P264")) {	// 최근 1개월 Full Scan SQL
					html = P264(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P163")) {	// 조건 없는 Delete
					html = html;
				} else if(contents_id.equalsIgnoreCase("P265")) {	// 최근 1개월 조건 없는 Delete SQL 발생 현황
					html = P265(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P266")) {	// 최근 1개월 조건 없는 Delete 문장
					html = P266(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P164")) {	// OFFLOAD 비효율 SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P267")) {	// 최근 1개월 OFFLOAD 비효율 SQL 발생 현황
					html = P267(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P268")) {	// 최근 1개월 OFFLOAD 비효율 SQL
					html = P268(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P165")) {	// OFFLOAD 효율저하 SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P269")) {	// 최근 1개월 OFFLOAD 효율저하 SQL 발생 현황
					html = P269(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P270")) {	// 최근 1개월 OFFLOAD 효율저하 SQL
					html = P270(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P166")) {	// TOP Elapsed Time SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P271")) {	// TOP SQL 발생 현황
					html = P271(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P272")) {	// TOP SQL 목록
					html = P272(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P167")) {	// TOP CPU Time SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P273")) {	// TOP SQL 발생 현황
					html = P273(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P274")) {	// TOP SQL 목록
					html = P274(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P168")) {	// TOP Buffer Gets SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P275")) {	// TOP SQL 발생 현황
					html = P275(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P276")) {	// TOP SQL 목록
					html = P276(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P169")) {	// TOP Physical Reads SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P277")) {	// TOP SQL 발생 현황
					html = P277(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P278")) {	// TOP SQL 목록
					html = P278(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P170")) {	// TOP Executions SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P279")) {	// TOP SQL 발생 현황
					html = P279(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P280")) {	// TOP SQL 목록
					html = P280(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P171")) {	// TOP Cluster Wait SQL
					html = reportHtml.replaceVariable(html , tableHtml, reportHtml, contents_id, loadSQL, slt_program_sql_number);
				} else if(contents_id.equalsIgnoreCase("P281")) {	// TOP SQL 발생 현황
					html = P281(html , tableHtml, reportHtml, contents_id, loadSQL);
				} else if(contents_id.equalsIgnoreCase("P282")) {	// TOP SQL 목록
					html = P282(html , tableHtml, reportHtml, loadSQL, sqlIndex, slt_program_sql_number);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		queue.add(html);
		
		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P155");
		}

//		logger.debug("contents_id[" + contents_id + "] html[" + html + "]"); // Used Debug
		
	}
	
	private String getHtml(String contents_id, DiagnosisResultDetailSQL diagnosisResultDetail) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P155")) {
			html = diagnosisResultDetail.getP155();
		} else if(contents_id.equalsIgnoreCase("P156")) {
			html = diagnosisResultDetail.getP156();
		} else if(contents_id.equalsIgnoreCase("P157")) {
			html = diagnosisResultDetail.getP157();
		} else if(contents_id.equalsIgnoreCase("P158")) {
			html = diagnosisResultDetail.getP158();
		} else if(contents_id.equalsIgnoreCase("P255")) {
			html = diagnosisResultDetail.getP255();
		} else if(contents_id.equalsIgnoreCase("P256")) {
			html = diagnosisResultDetail.getP256();
		} else if(contents_id.equalsIgnoreCase("P159")) {
			html = diagnosisResultDetail.getP159();
		} else if(contents_id.equalsIgnoreCase("P257")) {
			html = diagnosisResultDetail.getP257();
		} else if(contents_id.equalsIgnoreCase("P258")) {
			html = diagnosisResultDetail.getP258();
		} else if(contents_id.equalsIgnoreCase("P160")) {
			html = diagnosisResultDetail.getP160();
		} else if(contents_id.equalsIgnoreCase("P259")) {
			html = diagnosisResultDetail.getP259();
		} else if(contents_id.equalsIgnoreCase("P260")) {
			html = diagnosisResultDetail.getP260();
		} else if(contents_id.equalsIgnoreCase("P161")) {
			html = diagnosisResultDetail.getP161();
		} else if(contents_id.equalsIgnoreCase("P261")) {
			html = diagnosisResultDetail.getP261();
		} else if(contents_id.equalsIgnoreCase("P262")) {
			html = diagnosisResultDetail.getP262();
		} else if(contents_id.equalsIgnoreCase("P162")) {
			html = diagnosisResultDetail.getP162();
		} else if(contents_id.equalsIgnoreCase("P163")) {
			html = diagnosisResultDetail.getP163();
		} else if(contents_id.equalsIgnoreCase("P263")) {
			html = diagnosisResultDetail.getP263();
		} else if(contents_id.equalsIgnoreCase("P264")) {
			html = diagnosisResultDetail.getP264();
		} else if(contents_id.equalsIgnoreCase("P163")) {
			html = diagnosisResultDetail.getP163();
		} else if(contents_id.equalsIgnoreCase("P265")) {
			html = diagnosisResultDetail.getP265();
		} else if(contents_id.equalsIgnoreCase("P266")) {
			html = diagnosisResultDetail.getP266();
		} else if(contents_id.equalsIgnoreCase("P164")) {
			html = diagnosisResultDetail.getP164();
		} else if(contents_id.equalsIgnoreCase("P267")) {
			html = diagnosisResultDetail.getP267();
		} else if(contents_id.equalsIgnoreCase("P268")) {
			html = diagnosisResultDetail.getP268();
		} else if(contents_id.equalsIgnoreCase("P165")) {
			html = diagnosisResultDetail.getP165();
		} else if(contents_id.equalsIgnoreCase("P269")) {
			html = diagnosisResultDetail.getP269();
		} else if(contents_id.equalsIgnoreCase("P270")) {
			html = diagnosisResultDetail.getP270();
		} else if(contents_id.equalsIgnoreCase("P166")) {
			html = diagnosisResultDetail.getP166();
		} else if(contents_id.equalsIgnoreCase("P271")) {
			html = diagnosisResultDetail.getP271();
		} else if(contents_id.equalsIgnoreCase("P272")) {
			html = diagnosisResultDetail.getP272();
		} else if(contents_id.equalsIgnoreCase("P167")) {
			html = diagnosisResultDetail.getP167();
		} else if(contents_id.equalsIgnoreCase("P273")) {
			html = diagnosisResultDetail.getP273();
		} else if(contents_id.equalsIgnoreCase("P274")) {
			html = diagnosisResultDetail.getP274();
		} else if(contents_id.equalsIgnoreCase("P168")) {
			html = diagnosisResultDetail.getP168();
		} else if(contents_id.equalsIgnoreCase("P275")) {
			html = diagnosisResultDetail.getP275();
		} else if(contents_id.equalsIgnoreCase("P276")) {
			html = diagnosisResultDetail.getP276();
		} else if(contents_id.equalsIgnoreCase("P169")) {
			html = diagnosisResultDetail.getP169();
		} else if(contents_id.equalsIgnoreCase("P277")) {
			html = diagnosisResultDetail.getP277();	
		} else if(contents_id.equalsIgnoreCase("P278")) {
			html = diagnosisResultDetail.getP278();	
		} else if(contents_id.equalsIgnoreCase("P170")) {
			html = diagnosisResultDetail.getP170();	
		} else if(contents_id.equalsIgnoreCase("P279")) {
			html = diagnosisResultDetail.getP279();	
		} else if(contents_id.equalsIgnoreCase("P280")) {
			html = diagnosisResultDetail.getP280();	
		} else if(contents_id.equalsIgnoreCase("P171")) {
			html = diagnosisResultDetail.getP171();	
		} else if(contents_id.equalsIgnoreCase("P281")) {
			html = diagnosisResultDetail.getP281();	
		} else if(contents_id.equalsIgnoreCase("P282")) {
			html = diagnosisResultDetail.getP282();	
		}
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResultDetailSQL diagnosisResultDetail1, ReportHtml reportHtml) throws Exception {
		String html =  getHtml(diagnosisReport.getContents_id(), diagnosisResultDetail1);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P155");
	}
	
	private String P156(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "임계값 구분";
		String head2 = "임계값";
		String head3 = "설명";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(3);
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
					tableHtml += reportHtml.tableStyle1(3);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(3, head3);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data3, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data3, 0);
			}
		}
		
		valueIndex = html.indexOf("P156_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P157(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String label1 = "기준일";
		String label2 = "성능진단유형";
		String label3 = "건수";
		String legend1 = "FULLSCAN";
		String legend2 = "LITERAL(PLAN)";
		String legend3 = "LITERAL(SQL)";
		String legend4 = "OFFLOAD 비효율 SQL";
		String legend5 = "OFFLOAD 효율저하 SQL";
		String legend6 = "TEMP과다사용";
		String legend7 = "TOPSQL";
		String legend8 = "신규";
		String legend9 = "조건절없는DELETE";
		String legend10 = "플랜변경";
		
		if(loadSQLSize > 0) {
			ArrayList labelList = new ArrayList();
			ArrayList legend = new ArrayList();
			ArrayList data = new ArrayList();
			ArrayList dataList1 = new ArrayList();
			ArrayList dataList2 = new ArrayList();
			ArrayList dataList3 = new ArrayList();
			ArrayList dataList4 = new ArrayList();
			ArrayList dataList5 = new ArrayList();
			ArrayList dataList6 = new ArrayList();
			ArrayList dataList7 = new ArrayList();
			ArrayList dataList8 = new ArrayList();
			ArrayList dataList9 = new ArrayList();
			ArrayList dataList10 = new ArrayList();
			
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String label1_1 = loadSQL.get(loadSQLIndex).get(label1) + "";
				String label2_1 = loadSQL.get(loadSQLIndex).get(label2) + "";
				String count = loadSQL.get(loadSQLIndex).get(label3) + "";
				
				if(label2_1.equalsIgnoreCase(legend1)) {
					dataList1.add(count);
				} else if(label2_1.equalsIgnoreCase(legend2)) {
					dataList2.add(count);
				} else if(label2_1.equalsIgnoreCase(legend3)) {
					dataList3.add(count);
				} else if(label2_1.equalsIgnoreCase(legend4)) {
					dataList4.add(count);
				} else if(label2_1.equalsIgnoreCase(legend5)) {
					dataList5.add(count);
				} else if(label2_1.equalsIgnoreCase(legend6)) {
					dataList6.add(count);
				} else if(label2_1.equalsIgnoreCase(legend7)) {
					dataList7.add(count);
				} else if(label2_1.equalsIgnoreCase(legend8)) {
					dataList8.add(count);
				} else if(label2_1.equalsIgnoreCase(legend9)) {
					dataList9.add(count);
				} else if(label2_1.equalsIgnoreCase(legend10)) {
					dataList10.add(count);
				}
				
				if(!labelList.contains(label1_1)) {
					labelList.add(label1_1);
				}
				
				if(!legend.contains(label2_1)) {
					legend.add(label2_1);
				}
			}
			
			data.add(dataList1);
			data.add(dataList2);
			data.add(dataList3);
			data.add(dataList4);
			data.add(dataList5);
			data.add(dataList6);
			data.add(dataList7);
			data.add(dataList8);
			data.add(dataList9);
			data.add(dataList10);
			
			tableHtml += reportHtml.areaLineChartStyle(3, contents_id, labelList, legend, data);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P157_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P255(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P255_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P256(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		
		String head1 = "SQL_ID";
		String head2 = "변경전";
		String head3 = "변경후";
		String head4 = "변경전";
		String head5 = "변경후";
		String head6 = "변경전";
		String head7 = "변경후";
		String head8 = "변경전";
		String head9 = "변경후";
		String head10 = "변경전";
		String head11 = "변경후";
		String head12 = "SQL Text";
		String mergeHead1 = "PLAN_HASH_VALUE";
		String mergeHead2 = "Elapsed Time";
		String mergeHead3 = "Executions";
		String mergeHead4 = "Buffer Gets";
		String mergeHead5 = "CPU Time";
		
		String col1 = "SQL_ID";
		String col2 = "변경전 PLAN_HASH_VALUE";
		String col3 = "변경후 PLAN_HASH_VALUE";
		String col4 = "변경전 Elapsed Time";
		String col5 = "변경후 Elapsed Time";
		String col6 = "변경전 Executions";
		String col7 = "변경후 Executions";
		String col8 = "변경전 Buffer Gets";
		String col9 = "변경후 Buffer Gets";
		String col10 = "변경전 CPU Time";
		String col11 = "변경후 CPU Time";
		String col12 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += "\t\t\t\t\t<tr>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" rowspan=\"2\">" + head1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead1 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead2 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead3 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead4 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead5 + "</th>\n";
			tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" rowspan=\"2\">" + head12 + "</th>\n";
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
			tableHtml += "\t\t\t\t\t</tr>\n";
			
			tableHtml += reportHtml.tableStyleNoDataClose(12, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(col1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
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
					tableHtml += "\t\t\t\t\t\t<th class=\"om-left\" style='width:100px;' rowspan=\"2\">" + head1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead1 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead2 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead3 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead4 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-center\" colspan=\"2\">" + mergeHead5 + "</th>\n";
					tableHtml += "\t\t\t\t\t\t<th class=\"om-right\" rowspan=\"2\">" + head12 + "</th>\n";
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
					tableHtml += "\t\t\t\t\t</tr>\n";
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
					tableHtml += reportHtml.tableStyle1BodyClose(3, data12, 0);
					
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
				tableHtml += reportHtml.tableStyle1Body(3, data12, 0);
			}
		}
		
		valueIndex = html.indexOf("P256_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P257(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P257_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P258(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "PLAN_HASH_VALUE";
		String head3 = "Parsing Schema";
		String head4 = "Elapsed Time";
		String head5 = "CPU Time";
		String head6 = "Buffer Gets";
		String head7 = "Executions";
		String head8 = "Disk Reads";
		String head9 = "Rows Processed";
		String head10 = "Module";
		String head11 = "First Load";
		String head12 = "Last Load";
		String head13 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
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
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data10, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data11, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data12, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data13, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data10, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data11, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data12, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data13, 0);
			}
		}
		
		valueIndex = html.indexOf("P258_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P259(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P259_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P260(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL Text";
		String head2 = "Literal SQL 수";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1);
			tableHtml += reportHtml.tableStyle1Head(3, head2);
			
			tableHtml += reportHtml.tableStyleNoDataClose(2, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1);
					tableHtml += reportHtml.tableStyle1Head(3, head2);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data2, 2);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data2, 2);
			}
		}
		
		valueIndex = html.indexOf("P260_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P261(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P261_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P262(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "PLAN_HASH_VALUE";
		String head3 = "Command Type";
		String head4 = "Temp Usage(GB)";
		String head5 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(3, head5);
			
			tableHtml += reportHtml.tableStyleNoDataClose(5, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(3, head5);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data5, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data5, 0);
			}
		}
		
		valueIndex = html.indexOf("P262_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P263(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P263_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P264(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "PLAN_HASH_VALUE";
		String head3 = "Operation";
		String head4 = "Elapsed Time";
		String head5 = "Buffer Gets";
		String head6 = "Executions";
		String head7 = "Rows Processed";
		String head8 = "CPU Time";
		String head9 = "Disk Reads";
		String head10 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
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
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data9, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data10, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data9, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data10, 0);
			}
		}
		
		valueIndex = html.indexOf("P264_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P265(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P265_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P266(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "Elapsed Time";
		String head3 = "Buffer Gets";
		String head4 = "Row Processed";
		String head5 = "Executions";
		String head6 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(3, head6);
			
			tableHtml += reportHtml.tableStyleNoDataClose(6, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data6, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data6, 0);
			}
		}
		
		valueIndex = html.indexOf("P266_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P267(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P267_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P268(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "PLAN_HASH_VALUE";
		String head3 = "Elapsed Time";
		String head4 = "Executions";
		String head5 = "Parallel Servers";
		String head6 = "Offload 여부";
		String head7 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(2, head4);
			tableHtml += reportHtml.tableStyle1Head(2, head5);
			tableHtml += reportHtml.tableStyle1Head(2, head6);
			tableHtml += reportHtml.tableStyle1Head(3, head7);
			
			tableHtml += reportHtml.tableStyleNoDataClose(7, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				String data5 = loadSQL.get(loadSQLIndex).get(head5) + "";
				String data6 = loadSQL.get(loadSQLIndex).get(head6) + "";
				String data7 = loadSQL.get(loadSQLIndex).get(head7) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(2, head4);
					tableHtml += reportHtml.tableStyle1Head(2, head5);
					tableHtml += reportHtml.tableStyle1Head(2, head6);
					tableHtml += reportHtml.tableStyle1Head(3, head7);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data4, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data5, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data7, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data7, 0);
			}
		}
		
		valueIndex = html.indexOf("P268_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P269(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P269_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P270(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "PLAN_HASH_VALUE";
		String head3 = "Elapsed Time";
		String head4 = "Executions";
		String head5 = "Offload 여부";
		String head6 = "I/O Saved(%)";
		String head7 = "일주일전 I/O Saved(%)";
		String head8 = "I/O Saved감소량(%)";
		String head9 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
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
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
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
					tableHtml += reportHtml.tableStyle1BodyClose(2, data6, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data7, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data8, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data9, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data4, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data5, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data6, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data7, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data8, 0);
				tableHtml += reportHtml.tableStyle1Body(3, data9, 0);
			}
		}
		
		valueIndex = html.indexOf("P270_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P271(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P271_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P272(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P272_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P273(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P273_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P274(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P274_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P275(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P275_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P276(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P276_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P277(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P277_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P278(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P278_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P279(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P279_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P280(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P280_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}
	
	private String P281(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String clm_label = "기준일";
		String clm_legend = "건수";
		
		if(loadSQLSize > 0) {
			tableHtml += reportHtml.processSingleData(4, reportHtml, contents_id, loadSQL, clm_label, clm_legend);
		} else {
			tableHtml += reportHtml.noDataChartStyle();
		}
		
		valueIndex = html.indexOf("P281_C01") + 26;	// 개행문자 포함 (+1)
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
		return html;
	}
	
	private String P282(String html, String tableHtml, ReportHtml reportHtml, List<LinkedHashMap<String, Object>> loadSQL, int sqlIndex, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		int valueIndex = -1;
		String head1 = "SQL_ID";
		String head2 = "발생횟수";
		String head3 = "Activity(%)";
		String head4 = "SQL Text";
		
		if(loadSQLSize == 0) {
			tableHtml += reportHtml.tableStyle1(4);
			tableHtml += reportHtml.tableStyle1Head(1, head1,true);
			tableHtml += reportHtml.tableStyle1Head(2, head2);
			tableHtml += reportHtml.tableStyle1Head(2, head3);
			tableHtml += reportHtml.tableStyle1Head(3, head4);
			
			tableHtml += reportHtml.tableStyleNoDataClose(4, "해당 진단 항목에 대한 데이터가 없습니다.");
		} else {
			for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
				String data1 = loadSQL.get(loadSQLIndex).get(head1) + "";
				reportHtml.getSqlIdSet().add(data1);
				data1 = "<a class='om' style='cursor: pointer;' onclick='callFunction3(\""+data1+"\",\"P177\"); return false;'>"+data1+"</a>";
				String data2 = loadSQL.get(loadSQLIndex).get(head2) + "";
				String data3 = loadSQL.get(loadSQLIndex).get(head3) + "";
				String data4 = loadSQL.get(loadSQLIndex).get(head4) + "";
				
				if(loadSQLIndex == 0) {
					// 1 Row
					tableHtml += reportHtml.excelTag(slt_program_sql_number);
					tableHtml += reportHtml.tableStyle1(4);
					tableHtml += reportHtml.tableStyle1Head(1, head1,true);
					tableHtml += reportHtml.tableStyle1Head(2, head2);
					tableHtml += reportHtml.tableStyle1Head(2, head3);
					tableHtml += reportHtml.tableStyle1Head(3, head4);
				}
				
				if(loadSQLIndex == (loadSQLSize - 1)) {
					tableHtml += reportHtml.tableStyle1BodyClose(1, data1, 0);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data2, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(2, data3, 2);
					tableHtml += reportHtml.tableStyle1BodyClose(3, data4, 0);
					
					continue;
				}
				
				tableHtml += reportHtml.tableStyle1Body(1, data1, 0);
				tableHtml += reportHtml.tableStyle1Body(2, data2, 2);
				tableHtml += reportHtml.tableStyle1Body(2, data3, 2);
				tableHtml += reportHtml.tableStyle1Body(3, data4, 0);
			}
		}
		
		valueIndex = html.indexOf("P282_T01") + 10;
		String endTHtml = html.substring(valueIndex);
		html = html.substring(0, valueIndex) + tableHtml + endTHtml;
		
//		logger.debug("sqlIndex[" + sqlIndex + "] html[" + html + "]"); // Used Debug
		
		return html;
	}

}
