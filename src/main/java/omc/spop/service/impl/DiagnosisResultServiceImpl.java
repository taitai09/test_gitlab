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
import omc.spop.model.Report.DiagnosisResult;
import omc.spop.service.DiagnosisResultService;

/***********************************************************
 * 2019.10.28	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("DiagnosisResultService")
public class DiagnosisResultServiceImpl implements DiagnosisResultService {
	private static final Logger logger = LoggerFactory.getLogger(DiagnosisResultServiceImpl.class);
	
	@Autowired
	private DiagnosisReportDao diagnosisReportDao;
	
	@Override
	public void loadSQL(List<DiagnosisReport> sqlList, String contents_id, DiagnosisResult diagnosisResult, ReportHtml reportHtml) throws Exception {
		DiagnosisReport diagnosisReport;
		String html = getHtml(contents_id, diagnosisResult, reportHtml);
		int valueIndex = -1;
		String endHtml = "";
		String startStyle1 = "<strong><font color=#0073c6>";
		String endStyle1 = "</font></strong>";
		
		Queue<String> queue = new LinkedList<String>();
		DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

		try {
		
		for(int sqlIndex = 0; sqlIndex < sqlList.size(); sqlIndex++) {
			diagnosisReport = sqlList.get(sqlIndex);
			
			int slt_program_sql_number = diagnosisReport.getSlt_program_sql_number();
			logger.debug("slt_program_sql_number[" + slt_program_sql_number + "] sql[" + diagnosisReport.getSlt_program_chk_sql() + "]");
			String tableHtml = "";
			List<LinkedHashMap<String, Object>> loadSQL = diagnosisReportDao.loadSQL(diagnosisReport);
			
			if(contents_id.equalsIgnoreCase("P106")) {
				switch(slt_program_sql_number) {
				case 10006:	// Expired(grace) 계정
					html = html.replace("10006_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10006_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10007:	// 파라미터 변경
					html = html.replace("10007_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10007_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10008:	// DB File 생성율
					html = html.replace("10008_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10008_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10009:	// Library Cache Hit Ratio
					html = html.replace("10009_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10009_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10010:	// Dictionary Cache Hit Ratio
					html = html.replace("10010_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10010_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10011:	// Buffer Cache Hit Ratio
					html = html.replace("10011_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10011_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10012:	// Latch Hit Ratio
					html = html.replace("10012_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10012_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10013:	// Parse CPU To Parse Elapsed Ratio
					html = html.replace("10013_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10013_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10014:	// Disk Sort Ratio
					html = html.replace("10014_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10014_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10015:	// Shared Pool Usage
					html = html.replace("10015_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10015_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10016:	// Resource Limit
					html = html.replace("10016_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10016_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10020:	// FRA Space
					html = html.replace("10020_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10020_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10020_var3", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR3")) + endStyle1 + "");
					html = html.replace("10020_var4", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR4")) + endStyle1 + "");
					html = html.replace("10020_var5", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR5") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10021:	// FRA Usage Detail
					html = html.replace("10021_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10021_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10022:	// ASM Diskgroup Space
					html = html.replace("10022_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10022_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10023:	// Tablespace Space
					html = html.replace("10023_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10023_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10024:	// Recyclebin Object
					html = html.replace("10024_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10024_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10024_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10025:	// Invalid Object
					html = html.replace("10025_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10025_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10026:	// Nologging Object
					html = html.replace("10026_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10026_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10027:	// Parallel Object
					html = html.replace("10027_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10027_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10028:	// Unusable Index
					html = html.replace("10028_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10028_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10029:	// Chained Rows Table
					html = html.replace("10029_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10029_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10030:	// Corrupt Block
					html = html.replace("10030_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10030_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10031:	// Sequence Threshold  Exceeded
					html = html.replace("10031_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10031_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10032:	// Foreign Keys Without Index
					html = html.replace("10032_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10032_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10033:	// Disabled Constraint
					html = html.replace("10033_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10033_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10034:	// Stale Statistics
					html = html.replace("10034_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10034_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10035:	// Statistics Locked Table
					html = html.replace("10035_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10035_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10036:	// Long Running Operation
					html = html.replace("10036_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10036_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10036_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10037:	// Long Running Job
					html = html.replace("10037_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10037_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10037_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10038:	// Alert Log Error
					html = html.replace("10038_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10038_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10038_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10039:	// Active Incident
					html = html.replace("10039_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10039_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10039_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10040:	// Outstanding Alert
					html = html.replace("10040_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10040_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10040_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10319:	// Scheduler Job 실패 요약
					html = html.replace("10319_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10319_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10319_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;

				}
				
			} else if(contents_id.equalsIgnoreCase("P107")) {
				switch(slt_program_sql_number) {
				case 10041:	// Reorg 대상 점검
					html = html.replace("10041_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10041_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10041_var3", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR3")) + endStyle1 + "");
					html = html.replace("10041_var4", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR4")) + endStyle1 + "");
					html = html.replace("10041_var5", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR5") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10042:	// 파티셔닝 대상 점검
					html = html.replace("10042_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10042_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10043:	// 인덱스 사용 점검
					html = html.replace("10043_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10043_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				}
			} else if(contents_id.equalsIgnoreCase("P108")) {
				switch(slt_program_sql_number) {
				case 10044:
					html = html.replace("10044_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10044_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				}
			} else if(contents_id.equalsIgnoreCase("P109")) {
				switch(slt_program_sql_number) {
				case 10045:	// Plan 변경
					html = html.replace("10045_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10045_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10046:	// 신규 SQL
					html = html.replace("10046_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10046_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10047:	// Literal SQL
					html = html.replace("10047_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10047_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10048:	// Temp 과다 사용 SQL
					html = html.replace("10048_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10048_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10049:	// Full Scan SQL
					html = html.replace("10049_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10049_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10050:	// 조건 없는 Delete
					html = html.replace("10050_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10050_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10051:	// Offload 비효율 SQL
					if(reportHtml.getExadata_yn().equalsIgnoreCase("Y")) {
						html = html.replace("10051_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
						html = html.replace("10051_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
						html = html.replace("10051_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					}
					break;
				case 10052:	// Offload 효율저하 SQL
					if(reportHtml.getExadata_yn().equalsIgnoreCase("Y")) {
						html = html.replace("10052_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
						html = html.replace("10052_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
						html = html.replace("10052_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					}
					break;
				case 10053:	// TOP Elapsed Time SQL
					html = html.replace("10053_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10053_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10053_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10054:	// TOP CPU Time SQL
					html = html.replace("10054_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10054_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10054_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10055:	// TOP Buffer Gets SQL
					html = html.replace("10055_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10055_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10055_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10056:	// TOP Physical Reads SQL
					html = html.replace("10056_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10056_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10056_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10057:	// TOP Executions SQL
					html = html.replace("10057_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10057_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10057_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10058:	// TOP Cluster Wait SQL
					html = html.replace("10058_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10058_var2", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR2")) + endStyle1 + "");
					html = html.replace("10058_var3", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR3") + "").charAt(0), slt_program_sql_number)));
					break;
				}
			} else if(contents_id.equalsIgnoreCase("P110")) {
				switch(slt_program_sql_number) {
				case 10059:	// CPU 한계점 예측 요약
					html = html.replace("10059_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10059_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10060:	// Sequence 한계점 예측 요약
					html = html.replace("10060_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10060_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10061:	// Tablespace 한계점 예측 요약
					html = html.replace("10061_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10061_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				case 10062:	// 신규 SQL 타임아웃 예측 요약
					html = html.replace("10062_var1", startStyle1 + (loadSQL.size() == 0 ? "N/A" : loadSQL.get(0).get("VAR1")) + endStyle1 + "");
					html = html.replace("10062_var2", (loadSQL.size() == 0 ? "N/A" : reportHtml.getResultIcon((loadSQL.get(0).get("VAR2") + "").charAt(0), slt_program_sql_number)));
					break;
				}
			}
		}
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
//		logger.debug("contents_id[" + contents_id + "] html[" + html + "]"); // Used Debug
		queue.add(diagnosisReportController.sectionPlus(html,reportHtml));

		if(queue.size() != 0) {
			diagnosisReportController.intermediateHtmlWrite_include(queue,"P020");
		}

	}
	
	private String getHtml(String contents_id, DiagnosisResult diagnosisResult, ReportHtml reportHtml) {
		String html = "";
		
		if(contents_id.equalsIgnoreCase("P020")) {
			html = diagnosisResult.getP020();
		} else if(contents_id.equalsIgnoreCase("P106")) {
			html = diagnosisResult.getP106();
		} else if(contents_id.equalsIgnoreCase("P107")) {
			html = diagnosisResult.getP107();
		} else if(contents_id.equalsIgnoreCase("P108")) {
			html = diagnosisResult.getP108();
		} else if(contents_id.equalsIgnoreCase("P109")) {
			if(reportHtml.getExadata_yn().equalsIgnoreCase("N")) {
				html = diagnosisResult.getP109_noexadata();
			} else {
				html = diagnosisResult.getP109();
			}
		} else if(contents_id.equalsIgnoreCase("P110")) {
			html = diagnosisResult.getP110();
		}
		
		return html;
	}
	
	@Override
	public void getReportHtml(DiagnosisReport diagnosisReport, DiagnosisResult diagnosisResult, ReportHtml reportHtml) throws Exception {
		String html = getHtml(diagnosisReport.getContents_id(), diagnosisResult, reportHtml);
		new DiagnosisReportController().intermediateHtmlWrite_include(html, "P020");

	}
	
}
