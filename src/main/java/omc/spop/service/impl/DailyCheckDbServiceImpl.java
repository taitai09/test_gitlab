package omc.spop.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.DailyCheckDbDao;
import omc.spop.model.DailyCheckDb;
import omc.spop.service.DailyCheckDbService;

@Service("DailyCheckDbService")
public class DailyCheckDbServiceImpl implements DailyCheckDbService {
	private static final Logger logger = LoggerFactory.getLogger(DailyCheckDbServiceImpl.class);
	
	@Autowired
	private DailyCheckDbDao dailyCheckDbDao;
	
	@Override
	public List<DailyCheckDb> dbGroupList(DailyCheckDb dailyCheckDb) throws Exception {
		return dailyCheckDbDao.dbGroupList(dailyCheckDb);
	}
	
	@Override
	public List<DailyCheckDb> severityList(DailyCheckDb dailyCheckDb) throws Exception {
		return dailyCheckDbDao.severityList(dailyCheckDb);
	}
	
	@Override
	public List<DailyCheckDb> dbSeverityCount(DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb>dbSeverityList = new ArrayList<DailyCheckDb>();
		try {
			dbSeverityList = dailyCheckDbDao.dbSeverityCount(dailyCheckDb);
		
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<DailyCheckDb>();
		}
		
		return dbSeverityList;
	}
	
	@Override
	public List<DailyCheckDb> dbMain(DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb>dbSeverityList = new ArrayList<DailyCheckDb>();
		
		try {
			dbSeverityList = dailyCheckDbDao.dbMain(dailyCheckDb);
		
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ArrayList<DailyCheckDb>();
		}
		
		return dbSeverityList;
	}
	
	@Override
	public List<DailyCheckDb> diagnosisResultSummary(DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb> diagnosisResultSummaryList = null;
		String db_status_name = dailyCheckDb.getDb_status_name();
		
		if(db_status_name.equalsIgnoreCase("DB")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryDb(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("INSTANCE")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryInstance(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("SPACE")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummarySpace(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("OBJECT")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryObject(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("STATISTICS")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryStatistics(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("LONGRUNNINGWORK")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryLongRunningWork(dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("ALERT")) {
			diagnosisResultSummaryList = dailyCheckDbDao.diagnosisResultSummaryAlert(dailyCheckDb);
		}
		
		return diagnosisResultSummaryList;
	}
	
	@Override
	public List<DailyCheckDb> diagnosisResultMinute(DailyCheckDb dailyCheckDb) throws Exception {
		DailyCheckDb diagnosisResultMinute = new DailyCheckDb();
		List<DailyCheckDb> diagnosisResultMinuteList = new ArrayList<DailyCheckDb>();
		String db_status_name = dailyCheckDb.getDb_status_name();
		String html = "";
		
		if(db_status_name.equalsIgnoreCase("DB")) {
			html += db_table_1(dailyCheckDbDao.databaseStatus(dailyCheckDb), "DB", 1, "DATABASE STATUS", dailyCheckDb);
			html += db_table_2(dailyCheckDbDao.expiredGraceAccount(dailyCheckDb), "DB", 2, "EXPIRED GRACE ACCOUNT", dailyCheckDb);
			html += db_table_3(dailyCheckDbDao.modifiedParameter(dailyCheckDb), "DB", 3, "MODIFIED PARAMETER", dailyCheckDb);
			html += db_table_4(dailyCheckDbDao.newCreatedObject(dailyCheckDb), "DB", 4, "NEW CREATED OBJECT", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("INSTANCE")) {
			html += instance_table_1(dailyCheckDbDao.instanceStatus(dailyCheckDb), "INSTANCE", 1, "INSTANCE STATUS", dailyCheckDb);
			html += instance_table_2(dailyCheckDbDao.listenerStatus(dailyCheckDb), "INSTANCE", 2, "LISTENER STATUS", dailyCheckDb);
			html += instance_table_3(dailyCheckDbDao.dbfiles(dailyCheckDb), "INSTANCE", 3, "DBFILES", dailyCheckDb);
			html += instance_table_4(dailyCheckDbDao.resourceLimit(dailyCheckDb), "INSTANCE", 4, "RESOURCE LIMIT", dailyCheckDb);
			html += instance_table_5(dailyCheckDbDao.libraryCacheHit(dailyCheckDb), "INSTANCE", 5, "LIBRARY CACHE HIT", dailyCheckDb);
			html += instance_table_6(dailyCheckDbDao.dictionaryCacheHit(dailyCheckDb), "INSTANCE", 6, "DICTIONARY CACHE HIT", dailyCheckDb);
			html += instance_table_7(dailyCheckDbDao.bufferCacheHit(dailyCheckDb), "INSTANCE", 7, "BUFFER CACHE HIT", dailyCheckDb);
			html += instance_table_8(dailyCheckDbDao.latchHit(dailyCheckDb), "INSTANCE", 8, "LATCH HIT", dailyCheckDb);
			html += instance_table_9(dailyCheckDbDao.parseCpuToParseElapsd(dailyCheckDb), "INSTANCE", 9, "PARSE CPU TO PARSE ELAPSD", dailyCheckDb);
			html += instance_table_10(dailyCheckDbDao.diskSort(dailyCheckDb), "INSTANCE", 10, "DISK SORT", dailyCheckDb);
			html += instance_table_11(dailyCheckDbDao.memoryUsage(dailyCheckDb), "INSTANCE", 11, "MEMORY USAGE", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("SPACE")) {
			html += space_table_4(dailyCheckDbDao.fraSpaceFraFiles(dailyCheckDb), dailyCheckDbDao.fraSpaceFraUsage(dailyCheckDb), "SPACE", 4, "FRA SPACE-FRA FILES_FRA USAGE", dailyCheckDb);
			html += space_table_5(dailyCheckDbDao.asmDiskgroupSpace(dailyCheckDb), "SPACE", 5, "ASM DISKGROUP SPACE", dailyCheckDb);
			html += space_table_6(dailyCheckDbDao.tablespace(dailyCheckDb), "SPACE", 6, "TABLESPACE", dailyCheckDb);
			html += space_table_7(dailyCheckDbDao.recyclebinObject(dailyCheckDb), "SPACE", 7, "RECYCLEBIN OBJECT", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("OBJECT")) {
			html += object_table_1(dailyCheckDbDao.invalidObject(dailyCheckDb), "OBJECT", 1, "INVALID OBJECT", dailyCheckDb);
			html += object_table_2(dailyCheckDbDao.nologgingObject(dailyCheckDb), "OBJECT", 2, "NOLOGGING OBJECT", dailyCheckDb);
			html += object_table_3(dailyCheckDbDao.parallelObject(dailyCheckDb), "OBJECT", 3, "PARALLEL OBJECT", dailyCheckDb);
			html += object_table_4(dailyCheckDbDao.unusableIndex(dailyCheckDb), "OBJECT", 4, "UNUSABLE INDEX", dailyCheckDb);
			html += object_table_5(dailyCheckDbDao.corruptBlock(dailyCheckDb), "OBJECT", 5, "CORRUPT BLOCK", dailyCheckDb);
			html += object_table_6(dailyCheckDbDao.sequence(dailyCheckDb), "OBJECT", 6, "SEQUENCE", dailyCheckDb);
			html += object_table_7(dailyCheckDbDao.foreignkeysWithoutIndex(dailyCheckDb), "OBJECT", 7, "FOREIGNKEYS WITHOUT INDEX", dailyCheckDb);
			html += object_table_8(dailyCheckDbDao.disabledConstraint(dailyCheckDb), "OBJECT", 8, "DISABLED CONSTRAINT", dailyCheckDb);
			html += object_table_9(dailyCheckDbDao.chainedRows(dailyCheckDb), "OBJECT", 9, "CHAINED ROWS", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("STATISTICS")) {
			html += statistics_table_1(dailyCheckDbDao.missingOrStaleStatistics(dailyCheckDb), "STATISTICS", 1, "MISSING OR STALE STATISTICS", dailyCheckDb);
			html += statistics_table_2(dailyCheckDbDao.statisticsLockedTable(dailyCheckDb), "STATISTICS", 2, "STATISTICS LOCKED TABLE", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("LONGRUNNINGWORK")) {
			html += longrunningwork_table_1(dailyCheckDbDao.longRunningOperation(dailyCheckDb), "LONGRUNNINGWORK", 1, "LONG RUNNING OPERATION", dailyCheckDb);
			html += longrunningwork_table_2(dailyCheckDbDao.longRunningJob(dailyCheckDb), "LONGRUNNINGWORK", 2, "LONG RUNNING JOB", dailyCheckDb);
		} else if(db_status_name.equalsIgnoreCase("ALERT")) {
			html += alert_table_1(dailyCheckDbDao.alertLogError(dailyCheckDb), "ALERT", 1, "ALERT LOG ERROR", dailyCheckDb);
			html += alert_table_2(dailyCheckDbDao.activeIncidentProblem(dailyCheckDb), dailyCheckDbDao.activeIncidentIncident(dailyCheckDb), "ALERT", 2, "ACTIVE INCIDENT-PROBLEM_INCIDENT", dailyCheckDb);
			html += alert_table_3(dailyCheckDbDao.outstandingAlert(dailyCheckDb), "ALERT", 3, "OUTSTANDING ALERT", dailyCheckDb);
			html += alert_table_4(dailyCheckDbDao.dbmsSchedulerJobFailed(dailyCheckDb), "ALERT", 4, "DBMS SCHEDULER JOB FAILED", dailyCheckDb);
		}
		
		diagnosisResultMinute.setHtml(html);
		diagnosisResultMinute.setDb_status_name(dailyCheckDb.getDb_status_name());
		
		diagnosisResultMinuteList.add(diagnosisResultMinute);
		
		return diagnosisResultMinuteList;
	}
	
	private String replaceTitle(String title) {
		return title.toLowerCase().replaceAll(" ", "_");
	}
	
	private String db_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "LOG_MODE";
		String head03 = "OPEN_MODE";
		String head04 = "PLATFORM_NAME";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(4));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getIncident_id();
					String data2 = resultData.getLog_mode();
					String data3 = resultData.getOpen_mode();
					String data4 = resultData.getPlatform_name();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 1));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 1));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String db_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "USERNAME";
		String head02 = "ACCOUNT_STATUS";
		String head03 = "EXPIRY_DATE";
		String head04 = "CREATED";
		String head05 = "PASSWORD_EXPIRY_REMAIN_TIME";
		String head06 = "PASSWORD_GRACE_TIME";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getUsername();
					String data2 = resultData.getAccount_status();
					String data3 = resultData.getExpiry_date();
					String data4 = resultData.getCreated_date();
					String data5 = resultData.getPassword_expiry_remain_time();
					String data6 = resultData.getPassword_grace_time();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String db_table_3(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "NUM";
		String head03 = "NAME";
		String head04 = "변경전 VALUE";
		String head05 = "변경후 VALUE";
		String head06 = "변경일시";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getNum();
					String data3 = resultData.getName();
					String data4 = resultData.getBefore_value();
					String data5 = resultData.getAfter_value();
					String data6 = resultData.getUpdate_date();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 1));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 1));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String db_table_4(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "OBJECT_NAME";
		String head03 = "SUBOBJECT_NAME";
		String head04 = "OBJECT_TYPE";
		String head05 = "CREATED VALUE";
		String head06 = "LAST_DDL_TIME";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getObject_name();
					String data3 = resultData.getSubobject_name();
					String data4 = resultData.getObject_type();
					String data5 = resultData.getCreated_date();
					String data6 = resultData.getLast_ddl_time();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 1));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 1));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "INST_NM";
		String head03 = "HOST_NM";
		String head04 = "VERSION";
		String head05 = "STARTUP_TIME";
		String head06 = "UP_TIME";
		String head07 = "STATUS";
		String head08 = "ARCHIVER";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(8));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getInst_nm();
					String data3 = resultData.getHost_nm();
					String data4 = resultData.getVersion();
					String data5 = resultData.getStart_time();
					String data6 = resultData.getUp_time();
					String data7 = resultData.getStatus();
					String data8 = resultData.getArchiver();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data8, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data8, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "LISTENER_NM";
		String head03 = "STATUS";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getListener_nm();
					String data3 = resultData.getStatus();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_3(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "PARAM_DB_FILE_CNT";
		String head03 = "CREATE_DB_FILE_CNT";
		String head04 = "CREATED(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(4));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getParam_db_file_cnt();
					String data3 = resultData.getCreate_db_file_cnt();
					BigDecimal data4 = resultData.getCreated_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_4(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "RESOURCE_NM";
		String head03 = "MAX_UTILIZATION";
		String head04 = "LIMIT_VALUE";
		String head05 = "UTILIZATION(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(5));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getResource_nm();
					String data3 = resultData.getMax_utilization();
					String data4 = resultData.getLimit_value();
					BigDecimal data5 = resultData.getUtilization_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data5, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data5, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_5(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "LIBRARY_CACHE_HIT(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_6(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "DICTIONARY_CACHE_HIT(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_7(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "BUFFER_CACHE_HIT(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_8(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "LATCH_HIT(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_9(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "PARSE_CPU_TO_PARSE_ELAPSED(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_10(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "DISK_SORT(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String instance_table_11(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "MEMORY_USAGE(%)";
		String head03 = "OPTIMAL_HIT_THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getInst_efficiency_value();
					BigDecimal data3 = resultData.getThreshold_value();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 200, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 600, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 200, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 600, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}

	private String space_table_4(List<DailyCheckDb> diagnosisResultMinute1, List<DailyCheckDb> diagnosisResultMinute2, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml1 = new StringBuffer();
		StringBuffer tableHtml2 = new StringBuffer();
		String[] title_division1 = title.split("-");
		String[] title_division2 = title_division1[1].split("_");
		
		// FRA SPACE
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division1[0]).append("</h2>\n");
		
		{
			// FRA FILES
			outline.append("\t<h3 class='minute_title_3' name='").append(title_division2[0]).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division2[0]).append("</h3>\n");
			outline.append("\t<div id='").append(title_division2[0]).append("_T").append(index).append("'>\n");
			
			String head01 = "NAME";
			String head02 = "NUMBER_OF_FILES";
			String head03 = "TOTAL_SPACE(GB)";
			String head04 = "SPACE_USED(GB)";
			String head05 = "SPACE_RECLAIMABLE";
			String head06 = "CLAIM_BEFORE_USAGE(%)";
			String head07 = "CLAIM_AFTER_USAGE(%)";
			String head08 = "THRESHOLD(%)";
			
			try {
				int dataSize = diagnosisResultMinute1.size();
				
				if(dataSize == 0) {
					tableHtml1.append(dailyCheckDb.tableStyle1(3, false));
//					tableHtml1.append("\t\t\t\t\t<tr>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head08).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					
					tableHtml1.append(dailyCheckDb.tableStyleNoDataClose(8));
				} else {
					for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
						DailyCheckDb resultData = diagnosisResultMinute1.get(dataIndex);
						String data1 = resultData.getName();
						BigDecimal data2 = resultData.getNumber_of_files();
						BigDecimal data3 = resultData.getTotal_space();
						BigDecimal data4 = resultData.getSpace_used();
						BigDecimal data5 = resultData.getSpace_reclaimable();
						BigDecimal data6 = resultData.getClaim_before_usage_percent();
						BigDecimal data7 = resultData.getClaim_after_usage_percent();
						BigDecimal data8 = resultData.getThreshold_percent();
						
						if(dataIndex == 0) {
							tableHtml1.append(dailyCheckDb.tableStyle1(3, false));
							tableHtml1.append("\t\t\t\t\t<tr>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head08).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
						}
						
						if(dataIndex == (dataSize - 1)) {
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(3, data8, 0, 2));
							
							continue;
						}
						
						tableHtml1.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(3, data8, 0, 2));
					}
				}
				
				tableHtml1.append("\n");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			outline.append(tableHtml1);
			outline.append("</div>\n");
		}
		
		{
			// FRA USAGE
			outline.append("\t<h3 class='minute_title_3' name='").append(title_division2[1]).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division2[1]).append("</h3>\n");
			outline.append("\t<div id='").append(title_division2[1]).append("_T").append(index).append("'>\n");
			
			String head01 = "FILE_TYPE";
			String head02 = "SPACE_USED(%)";
			String head03 = "SPACE_RECLAIMABLE(%)";
			String head04 = "NUMBER_OF_FILES";
			
			try {
				int dataSize = diagnosisResultMinute2.size();
				
				if(dataSize == 0) {
					tableHtml2.append(dailyCheckDb.tableStyle1(3, false));
//					tableHtml2.append("\t\t\t\t\t<tr>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					
					tableHtml2.append(dailyCheckDb.tableStyleNoDataClose(4));
				} else {
					for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
						DailyCheckDb resultData = diagnosisResultMinute2.get(dataIndex);
						String data1 = resultData.getFile_type();
						BigDecimal data2 = resultData.getPercent_space_used();
						BigDecimal data3 = resultData.getPercent_space_reclaimable();
						BigDecimal data4 = resultData.getNumber_of_files();
						
						if(dataIndex == 0) {
							tableHtml2.append(dailyCheckDb.tableStyle1(3, false));
							tableHtml2.append("\t\t\t\t\t<tr>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
						}
						
						if(dataIndex == (dataSize - 1)) {
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 2));
							
							continue;
						}
						
						tableHtml2.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 2));
					}
				}
				
				tableHtml2.append("\n");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			outline.append(tableHtml2);
			outline.append("</div>\n");
		}
		
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String space_table_5(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "NAME";
		String head02 = "GROUP_NUMBER";
		String head03 = "STATE";
		String head04 = "TOTOAL_SPACE(GB)";
		String head05 = "FREE_SPACE(GB)";
		String head06 = "SPACE_USED(%)";
		String head07 = "THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(7));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getName();
					String data2 = resultData.getGroup_number();
					String data3 = resultData.getState();
					BigDecimal data4 = resultData.getTotal_space();
					BigDecimal data5 = resultData.getFree_space();
					BigDecimal data6 = resultData.getSpace_used_percent();
					BigDecimal data7 = resultData.getThreshold_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data7, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data7, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String space_table_6(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "TABLESPACE_NAME";
		String head02 = "SPACE_USED(GB)";
		String head03 = "TABLESPACE_SIZE(GB)";
		String head04 = "SPACE_USED(%)";
		String head05 = "THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(5));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getTablespace_name();
					BigDecimal data2 = resultData.getSpace_used();
					BigDecimal data3 = resultData.getTablespace_size();
					BigDecimal data4 = resultData.getSpace_used_percent();
					BigDecimal data5 = resultData.getThreshold_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data5, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data5, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String space_table_7(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "OBJECT_NAME";
		String head03 = "ORIGINAL_NAME";
		String head04 = "OPERATION";
		String head05 = "TYPE";
		String head06 = "TS_NAME";
		String head07 = "CREATETIME";
		String head08 = "DROPTIME";
		String head09 = "BLOCKS";
		String head10 = "SPACE_USED(GB)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head10).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(10));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getObject_name();
					String data3 = resultData.getOriginal_name();
					String data4 = resultData.getOperation();
					String data5 = resultData.getType();
					String data6 = resultData.getTs_name();
					String data7 = resultData.getCreatetime();
					String data8 = resultData.getDroptime();
					String data9 = resultData.getBlocks();
					BigDecimal data10 = resultData.getSpace_used();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head10).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data10, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data10, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "OBJECT_NAME";
		String head03 = "OBJECT TYPE";
		String head04 = "SCRIPT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(4));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getObject_name();
					String data3 = resultData.getObject_type();
					String data4 = resultData.getScript();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 2));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "OBJECT_NAME";
		String head03 = "OBJECT TYPE";
		String head04 = "PARTITION NAME";
		String head05 = "SUBPARTITION NAME";
		String head06 = "SCRIPT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getObject_name();
					String data3 = resultData.getObject_type();
					String data4 = resultData.getPartition_name();
					String data5 = resultData.getSubpartition_name();
					String data6 = resultData.getScript();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 2));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_3(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "OBJECT_NAME";
		String head03 = "OBJECT TYPE";
		String head04 = "PARTITION NAME";
		String head05 = "SUBPARTITION NAME";
		String head06 = "SCRIPT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getObject_name();
					String data3 = resultData.getObject_type();
					String data4 = resultData.getPartition_name();
					String data5 = resultData.getSubpartition_name();
					String data6 = resultData.getScript();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 2));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_4(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "INDEX_NAME";
		String head04 = "SCRIPT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(11));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getIndex_name();
					String data4 = resultData.getScript();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 2));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_5(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "FILE#";
		String head02 = "FILE_NAME";
		String head03 = "BLOCK#";
		String head04 = "BLOCKS";
		String head05 = "CORRUPTION_CHANGE#";
		String head06 = "CORRUPTION_TYPE";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(11));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getFile_number();
					String data2 = resultData.getFile_name();
					String data3 = resultData.getBlock_number();
					String data4 = resultData.getBlocks();
					String data5 = resultData.getCorruption_change_number();
					String data6 = resultData.getCorruption_type();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_6(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "SEQUENCE_OWNER";
		String head02 = "SEQUENCE_NAME";
		String head03 = "MIN_VALUE";
		String head04 = "MAX_VALUE";
		String head05 = "INCREMENT_BY";
		String head06 = "CYCLE_FLAG";
		String head07 = "ORDER_FLAG";
		String head08 = "CACHE_SIZE";
		String head09 = "LAST_NUMBER";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(9));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getSequence_owner();
					String data2 = resultData.getSequence_name();
					String data3 = resultData.getMin_value();
					String data4 = resultData.getMax_value();
					String data5 = resultData.getIncrement_by();
					String data6 = resultData.getCycle_flag();
					String data7 = resultData.getOrder_flag();
					String data8 = resultData.getCache_size();
					String data9 = resultData.getLast_number();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data9, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data9, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_7(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "CONSTRAINT_NAME";
		String head04 = "INDEX_COLUMN_NAME";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(4));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getConstraint_name();
					String data4 = resultData.getIndex_column_name();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_8(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "CONSTRAINT_NAME";
		String head04 = "CONSTRAINT_TYPE";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(11));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getConstraint_name();
					String data4 = resultData.getConstraint_type();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String object_table_9(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "TABLESPACE_NAME";
		String head04 = "NUM_ROWS";
		String head05 = "CHAIN_CNT";
		String head06 = "CHAIN(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(6));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getTablespace_name();
					String data4 = resultData.getNum_rows();
					String data5 = resultData.getChain_cnt();
					BigDecimal data6 = resultData.getChain_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data6, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data6, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String statistics_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "PARTITION_NAME";
		String head04 = "PARTITIONED";
		String head05 = "INSERTS";
		String head06 = "UPDATES";
		String head07 = "DELETES";
		String head08 = "TRUNCATED";
		String head09 = "TIMESTAMP";
		String head10 = "CHANGE(%)";
		String head11 = "NUM_ROWS";
		String head12 = "LAST_ANALYZED";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head12).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(12));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getPartition_name();
					String data4 = resultData.getPartitioned();
					String data5 = resultData.getInserts();
					String data6 = resultData.getUpdates();
					String data7 = resultData.getDeletes();
					String data8 = resultData.getTruncated();
					String data9 = resultData.getTimestamp();
					BigDecimal data10 = resultData.getChange_percent();
					String data11 = resultData.getNum_rows();
					String data12 = resultData.getLast_analyzed();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head12).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data12, 0, 1));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data10, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data12, 0, 1));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String statistics_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "OWNER";
		String head02 = "TABLE_NAME";
		String head03 = "PARTITION_NAME";
		String head04 = "SUBPARTITION_NAME";
		String head05 = "OBJECT_TYPE";
		String head06 = "LAST_ANALYZED";
		String head07 = "STATTYPE_LOCKED";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(7));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getOwner();
					String data2 = resultData.getTable_name();
					String data3 = resultData.getPartition_name();
					String data4 = resultData.getSubpartition_name();
					String data5 = resultData.getObject_type();
					String data6 = resultData.getLast_analyzed();
					String data7 = resultData.getStattype_locked();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.outlineButton(dailyCheckDb, replaceTitle(title), 1));
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data7, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data7, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String longrunningwork_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "SID";
		String head03 = "SERIAL#";
		String head04 = "START_TIME";
		String head05 = "LAST_UPDATE_TIME";
		String head06 = "ELAPSED_MINUTE";
		String head07 = "REMAINING_MINUTE";
		String head08 = "DONE(%)";
		String head09 = "MESSAGE";
		String head10 = "SQL_ID";
		String head11 = "SQL_PLAN_HASH_VALUE";
		String head12 = "SQL_TEXT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head12).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(12));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getSid();
					String data3 = resultData.getSerial_number();
					String data4 = resultData.getStart_time();
					String data5 = resultData.getLast_update_time();
					String data6 = resultData.getElapsed_minute();
					String data7 = resultData.getRemaining_minute();
					BigDecimal data8 = resultData.getDone_percent();
					String data9 = resultData.getMessage();
					String data10 = resultData.getSql_id();
					String data11 = resultData.getSql_plan_hash_value();
					String data12 = resultData.getSql_text();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head12).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data12, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data10, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data12, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String longrunningwork_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "SESSION_ID";
		String head02 = "OWNER";
		String head03 = "JOB_NAME";
		String head04 = "ELAPSED_TIME";
		String head05 = "CPU_USED_TIME";
		String head06 = "SLAVE_PROCESS_ID";
		String head07 = "RUNNING_INSTANCE";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(7));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getSession_id();
					String data2 = resultData.getOwner();
					String data3 = resultData.getJob_name();
					String data4 = resultData.getElapsed_time();
					String data5 = resultData.getCpu_used();
					String data6 = resultData.getSlave_process_id();
					String data7 = resultData.getRunning_instance();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data7, 0, 1));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data7, 0, 1));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String alert_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "ERR_CD";
		String head03 = "ERROR_CNT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(3));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getError_cd();
					String data3 = resultData.getError_cnt();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data3, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data3, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String alert_table_2(List<DailyCheckDb> diagnosisResultMinute1, List<DailyCheckDb> diagnosisResultMinute2, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml1 = new StringBuffer();
		StringBuffer tableHtml2 = new StringBuffer();
		String[] title_division1 = title.split("-");
		String[] title_division2 = title_division1[1].split("_");
		
		// ACTIVE INCIDENT
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division1[0]).append("</h2>\n");
		
		{
			// PROBLEM
			outline.append("\t<h3 class='minute_title_3' name='").append(title_division2[0]).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division2[0]).append("</h3>\n");
			outline.append("\t<div id='").append(title_division2[0]).append("_T").append(index).append("'>\n");
			
			String head01 = "INST_ID";
			String head02 = "PROBLEM_ID";
			String head03 = "PROBLEM_KEY";
			String head04 = "FIRST_INCIDENT";
			String head05 = "FIRSTINC_TIME";
			String head06 = "LAST_INCIDENT";
			String head07 = "LASTINC_TIME";
			String head08 = "IMPACT1";
			String head09 = "IMPACT2";
			String head10 = "IMPACT3";
			String head11 = "IMPACT4";
			String head12 = "SERVICE_REQUEST";
			String head13 = "BUG_NUMBER";
			
			try {
				int dataSize = diagnosisResultMinute1.size();
				
				if(dataSize == 0) {
					tableHtml1.append(dailyCheckDb.tableStyle1(3, false));
//					tableHtml1.append("\t\t\t\t\t<tr>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head13).append("</th>\n");
//					tableHtml1.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					
					tableHtml1.append(dailyCheckDb.tableStyleNoDataClose(13));
				} else {
					for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
						DailyCheckDb resultData = diagnosisResultMinute1.get(dataIndex);
						String data1 = resultData.getInst_id();
						String data2 = resultData.getProblem_id();
						String data3 = resultData.getProblem_key();
						String data4 = resultData.getFirst_incident();
						String data5 = resultData.getFirstinc_time();
						String data6 = resultData.getLast_incident();
						String data7 = resultData.getLastinc_time();
						String data8 = resultData.getImpact1();
						String data9 = resultData.getImpact2();
						String data10 = resultData.getImpact3();
						String data11 = resultData.getImpact4();
						String data12 = resultData.getService_request();
						String data13 = resultData.getBug_number();
						
						if(dataIndex == 0) {
							tableHtml1.append(dailyCheckDb.tableStyle1(3, false));
							tableHtml1.append("\t\t\t\t\t<tr>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head13).append("</th>\n");
							tableHtml1.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
						}
						
						if(dataIndex == (dataSize - 1)) {
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 1));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 2));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(2, data12, 0, 0));
							tableHtml1.append(dailyCheckDb.tableStyle1BodyClose(3, data13, 0, 0));
							
							continue;
						}
						
						tableHtml1.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 1));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data10, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 2));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(2, data12, 0, 0));
						tableHtml1.append(dailyCheckDb.tableStyle1Body(3, data13, 0, 0));
					}
				}
				
				tableHtml1.append("\n");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			outline.append(tableHtml1);
			outline.append("</div>\n");
		}
		
		{
			// INCIDENT
			outline.append("\t<h3 class='minute_title_3' name='").append(title_division2[1]).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title_division2[1]).append("</h3>\n");
			outline.append("\t<div id='").append(title_division2[1]).append("_T").append(index).append("'>\n");
			
			String head01 = "INST_ID";					String head02 = "INCIDENT_ID";				String head03 = "PROBLEM_ID";
			String head04 = "CREATE_TIME";				String head05 = "CLOSE_TIME";				String head06 = "STATUS";
			String head07 = "FLOOD_CONTROLLED";			String head08 = "ERROR_FACILITY";			String head09 = "ERROR_NUMBER";
			String head10 = "ERROR_ARG1";				String head11 = "ERROR_ARG2";				String head12 = "ERROR_ARG3";
			String head13 = "ERROR_ARG4";				String head14 = "ERROR_ARG5";				String head15 = "ERROR_ARG6";
			String head16 = "ERROR_ARG7";				String head17 = "ERROR_ARG8";				String head18 = "ERROR_ARG9";
			String head19 = "ERROR_ARG10";				String head20 = "ERROR_ARG11";				String head21 = "ERROR_ARG12";
			String head22 = "SIGNALLING_COMPONENT";		String head23 = "SIGNALLING_SUBCOMPONENT";	String head24 = "SUBSPECT_COMPONENT";
			String head25 = "SUBSPECT_SUBCOMPONENT";	String head26 = "ECID";						String head27 = "IMPACT";
			
			try {
				int dataSize = diagnosisResultMinute2.size();
				
				if(dataSize == 0) {
					tableHtml2.append(dailyCheckDb.tableStyle1(3, false));
//					tableHtml2.append("\t\t\t\t\t<tr>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head21).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head22).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head23).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head24).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head25).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head26).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head27).append("</th>\n");
//					tableHtml2.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					
					tableHtml2.append(dailyCheckDb.tableStyleNoDataClose(27));
				} else {
					for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
						DailyCheckDb resultData = diagnosisResultMinute2.get(dataIndex);
						String data1 = resultData.getInst_id();
						String data2 = resultData.getIncident_id();
						String data3 = resultData.getProblem_id();
						String data4 = resultData.getCreate_time();
						String data5 = resultData.getClose_time();
						String data6 = resultData.getStatus();
						String data7 = resultData.getFlood_controlled();
						String data8 = resultData.getError_facility();
						String data9 = resultData.getError_number();
						String data10 = resultData.getError_arg1();
						String data11 = resultData.getError_arg2();
						String data12 = resultData.getError_arg3();
						String data13 = resultData.getError_arg4();
						String data14 = resultData.getError_arg5();
						String data15 = resultData.getError_arg6();
						String data16 = resultData.getError_arg7();
						String data17 = resultData.getError_arg8();
						String data18 = resultData.getError_arg9();
						String data19 = resultData.getError_arg10();
						String data20 = resultData.getError_arg11();
						String data21 = resultData.getError_arg12();
						String data22 = resultData.getSignalling_component();
						String data23 = resultData.getSignalling_subcomponent();
						String data24 = resultData.getSuspect_component();
						String data25 = resultData.getSuspect_subcomponent();
						String data26 = resultData.getEcid();
						String data27 = resultData.getImpact();
						
						if(dataIndex == 0) {
							tableHtml2.append(dailyCheckDb.tableStyle1(3, false));
							tableHtml2.append("\t\t\t\t\t<tr>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head21).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head22).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head23).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head24).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head25).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head26).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head27).append("</th>\n");
							tableHtml2.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
						}
						
						if(dataIndex == (dataSize - 1)) {
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 2));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data12, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data13, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data14, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data15, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data16, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data17, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data18, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data19, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data20, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data21, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data22, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data23, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data24, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data25, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(2, data26, 0, 0));
							tableHtml2.append(dailyCheckDb.tableStyle1BodyClose(3, data27, 0, 2));
							
							continue;
						}
						
						tableHtml2.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 2));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data10, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data12, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data13, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data14, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data15, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data16, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data17, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data18, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data19, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data20, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data21, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data22, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data23, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data24, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data25, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(2, data26, 0, 0));
						tableHtml2.append(dailyCheckDb.tableStyle1Body(3, data27, 0, 2));
					}
				}
				
				tableHtml2.append("\n");
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			outline.append(tableHtml2);
			outline.append("</div>\n");
		}
		
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String alert_table_3(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "SEQUENCE_ID";			String head02 = "REASON_ID";		String head03 = "OWNER";
		String head04 = "OBJECT_NAME";			String head05 = "SUBOBJECT_NAME";	String head06 = "OBJECT_TYPE";
		String head07 = "REASON";				String head08 = "TIME_SUGGESTED";	String head09 = "CREATION_TIME";
		String head10 = "SUGGESTED_ACTION";		String head11 = "ADVISOR_NAME";		String head12 = "METRIC_VALUE";
		String head13 = "MESSAGE_TYPE";			String head14 = "MESSAGE_GROUP";	String head15 = "MESSAGE_LEVEL";
		String head16 = "HOSTING_CLIENT_ID";	String head17 = "MODULE_ID";		String head18 = "PROCESS_ID";
		String head19 = "HOST_ID";				String head20 = "HOST_NW_ADDR";		String head21 = "INSTANCE_NAME";
		String head22 = "INSTANCE_NUMBER";		String head23 = "USER_ID";			String head24 = "EXECUTION_CONTEXT_ID";
		String head25 = "ERROR_INSTANCE_ID";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head21).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head22).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head23).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head24).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head25).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(25));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getSequence_id();
					String data2 = resultData.getReason_id();
					String data3 = resultData.getOwner();
					String data4 = resultData.getObject_name();
					String data5 = resultData.getSubobject_name();
					String data6 = resultData.getObject_type();
					String data7 = resultData.getReason();
					String data8 = resultData.getTime_suggested();
					String data9 = resultData.getCreate_time();
					String data10 = resultData.getSuggested_action();
					String data11 = resultData.getAdvisor_name();
					String data12 = resultData.getMetric_value();
					String data13 = resultData.getMessage_type();
					String data14 = resultData.getMessage_group();
					String data15 = resultData.getMessage_level();
					String data16 = resultData.getHosting_client_id();
					String data17 = resultData.getModule_id();
					String data18 = resultData.getProcess_id();
					String data19 = resultData.getHost_id();
					String data20 = resultData.getHost_nw_addr();
					String data21 = resultData.getInstance_name();
					String data22 = resultData.getInstance_number();
					String data23 = resultData.getUser_id();
					String data24 = resultData.getExecution_context_id();
					String data25 = resultData.getError_instance_id();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, true));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head21).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head22).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head23).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head24).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head25).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 120, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 300, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data12, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data13, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data14, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data15, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data16, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data17, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data18, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data19, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data20, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data21, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data22, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data23, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data24, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data25, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 120, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data10, 300, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data12, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data13, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data14, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data15, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data16, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data17, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data18, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data19, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data20, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data21, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data22, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data23, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data24, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data25, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String alert_table_4(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";				String head02 = "LOG_ID";				String head03 = "LOG_DATE";
		String head04 = "OWNER";				String head05 = "JOB_NAME";				String head06 = "JOB_SUBNAME";
		String head07 = "STATUS";				String head08 = "ERROR#";				String head09 = "REQ_START_DATE";
		String head10 = "ACTUAL_START_DATE";	String head11 = "RUN_DURATION";			String head12 = "SESSION_ID";
		String head13 = "SLAVE_PID";			String head14 = "CPU_USED_TIME";		String head15 = "CREDENTIAL_OWNER";
		String head16 = "CREDENTIAL_NAME";		String head17 = "DESTINATION_OWNER";	String head18 = "DESTINATION";
		String head19 = "ADDITIONAL_INFO";		String head20 = "ERRORS";				String head21 = "OUTPUT";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head21).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(21));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					String data2 = resultData.getLog_id();
					String data3 = resultData.getLog_date();
					String data4 = resultData.getOwner();
					String data5 = resultData.getJob_name();
					String data6 = resultData.getJob_subname();
					String data7 = resultData.getStatus();
					String data8 = resultData.getError_number();
					String data9 = resultData.getReq_start_date();
					String data10 = resultData.getActual_start_date();
					String data11 = resultData.getRun_duration();
					String data12 = resultData.getSession_id();
					String data13 = resultData.getSlave_pid();
					String data14 = resultData.getCpu_used();
					String data15 = resultData.getCredential_owner();
					String data16 = resultData.getCredential_name();
					String data17 = resultData.getDestination_owner();
					String data18 = resultData.getDestination();
					String data19 = resultData.getAdditional_info();
					String data20 = resultData.getErrors();
					String data21 = resultData.getOutput();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head06).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head07).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head08).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head09).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head10).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head11).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head12).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head13).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head14).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head15).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head16).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head17).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head18).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head19).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head20).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head21).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data5, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data6, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data7, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data8, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data9, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data10, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data11, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data12, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data13, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data14, 0, 1));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data15, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data16, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data17, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data18, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data19, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data20, 0, 0));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data21, 0, 0));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data5, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data6, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data7, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data8, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data9, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data10, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data11, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data12, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data13, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data14, 0, 1));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data15, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data16, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data17, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data18, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data19, 0, 0));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data20, 0, 0));;
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data21, 0, 0));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> dailyCheckDbSituationTop(DailyCheckDb dailyCheckDb) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		LinkedHashMap<String, Object> resultMeta = new LinkedHashMap<String, Object>();
		List<DailyCheckDb> tempList = new ArrayList<DailyCheckDb>();
		DailyCheckDb temp = null;
		StringBuffer head = new StringBuffer();
		String tempData = "";
		String db_full_name = "";
		int dbid_check_grade_cd = -1;
		String day = "";
		String dbid = "";				// hidden, dbid_situation
		String check_day = "";			// hidden, check_day_situation
		String separator = ":";
		
		dailyCheckDb.setGroup_id(dailyCheckDb.getChoice_db_group_id());
		dailyCheckDb.setStart_first_analysis_day(dailyCheckDb.getStart_first_analysis_day().replace("-",""));
		dailyCheckDb.setEnd_first_analysis_day(dailyCheckDb.getEnd_first_analysis_day().replace("-",""));
		
		tempList = dailyCheckDbDao.dailyCheckDbSituationTop(dailyCheckDb);
		
		head.append("db_full_name").append(separator).append("DB").append(separator).append(200).append(separator).append("varchar").append(";");
		head.append("dbid").append(separator).append("dbid").append(separator).append(0).append(separator).append("Etc").append(separator).append("true").append(";");
		
		for(int index = 0; index < tempList.size(); index++) {
			temp = tempList.get(index);
			
			db_full_name = temp.getDb_full_name();
			dbid_check_grade_cd = changeValueDbidCheckGradeCd(temp.getDbid_check_grade_cd_situation());
			day = temp.getDay();
			dbid = temp.getDbid_situation();
			check_day = temp.getCheck_day_situation();
			
			if(head.indexOf(day) < 0) {
				head.append("day_").append(check_day).append(separator).append(day).append(separator).append(0).append(separator).append("Etc").append(separator).append("false").append(";");
				head.append("check_day_").append(check_day).append(separator).append("check_day").append(separator).append(0).append(separator).append("Etc").append(separator).append("true").append(";");
			}
			
			if(tempData.equals("") && !tempData.equals(db_full_name)) {
				resultData = new LinkedHashMap<String, Object>();
				
				resultData.put("db_full_name", db_full_name);
				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
			} else if(tempData.equals(db_full_name)) {
//				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
			} else if(!tempData.equals("") && !tempData.equals(db_full_name)) {
				resultList.add(resultData);
				
				resultData = new LinkedHashMap<String, Object>();
				
				resultData.put("db_full_name", db_full_name);
				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
			}
			
			tempData = db_full_name;
		}
		
		if(head.lastIndexOf(";") == (head.length() - 1)) {
			StringBuffer tempHead = new StringBuffer();
			
			tempHead.append(head);
			
			head.setLength(0);
			head.append(tempHead.substring(0, tempHead.length() - 1));
			
			tempHead.setLength(0);
		}
		
		resultMeta.put("HEAD", head.toString());
		
		if(resultData != null) resultList.add(resultData);
		
		resultList.add(resultMeta);
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> dailyCheckDbSituationBottom(DailyCheckDb dailyCheckDb) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		LinkedHashMap<String, Object> resultMeta = new LinkedHashMap<String, Object>();
		List<DailyCheckDb> tempList = new ArrayList<DailyCheckDb>();
		DailyCheckDb temp = null;
		StringBuffer head = new StringBuffer();
		String tempData = "";
		String check_class_div_nm = "";
		String day = "";
		int dbid_check_grade_cd = -1;
		String dbid = "";				// hidden, dbid_situation
		String check_class_div_cd = "";	// hidden, check_class_div_cd
		String check_day = "";			// hidden, check_day_situation
		String separator = ":";
		
		dailyCheckDb.setStart_first_analysis_day(dailyCheckDb.getStart_first_analysis_day_bottom().replace("-",""));
		dailyCheckDb.setEnd_first_analysis_day(dailyCheckDb.getEnd_first_analysis_day_bottom().replace("-",""));
		
		tempList = dailyCheckDbDao.dailyCheckDbSituationBottom(dailyCheckDb);
		
		head.append("check_class_div_nm").append(separator).append("점검항목그룹").append(separator).append(200).append(separator).append("varchar").append(";");
		head.append("dbid").append(separator).append("dbid").append(separator).append(0).append(separator).append("Etc").append(separator).append("true").append(";");
		
		for(int index = 0; index < tempList.size(); index++) {
			temp = tempList.get(index);
			
			check_class_div_nm = temp.getCheck_class_div_nm();
			dbid_check_grade_cd = changeValueDbidCheckGradeCd(temp.getDbid_check_grade_cd_situation());
			day = temp.getDay();
			dbid = temp.getDbid_situation();
			check_class_div_cd = temp.getCheck_class_div_cd();
			check_day = temp.getCheck_day_situation();
			
			if(head.indexOf(day) < 0) {
				head.append("day_").append(check_day).append(separator).append(day).append(separator).append(0).append(separator).append("Etc").append(separator).append("false").append(";");
				head.append("check_day_").append(check_day).append(separator).append("check_day").append(separator).append(0).append(separator).append("Etc").append(separator).append("true").append(";");
				head.append("check_class_div_cd_").append(check_day).append(separator).append("check_class_div_cd").append(separator).append(0).append(separator).append("Etc").append(separator).append("true").append(";");
			}
			
			if(tempData.equals("") && !tempData.equals(check_class_div_nm)) {
				resultData = new LinkedHashMap<String, Object>();
				
				resultData.put("check_class_div_nm", check_class_div_nm);
				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
				resultData.put("check_class_div_cd_" + check_day, check_class_div_cd);
			} else if(tempData.equals(check_class_div_nm)) {
//				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
				resultData.put("check_class_div_cd_" + check_day, check_class_div_cd);
			} else if(!tempData.equals("") && !tempData.equals(check_class_div_nm)) {
				resultList.add(resultData);
				
				resultData = new LinkedHashMap<String, Object>();
				
				resultData.put("check_class_div_nm", check_class_div_nm);
				resultData.put("dbid", dbid);
				resultData.put("check_day_" + check_day, check_day);
				resultData.put("day_" + check_day, dbid_check_grade_cd);
				resultData.put("check_class_div_cd_" + check_day, check_class_div_cd);
			}
			
			tempData = check_class_div_nm;
		}
		
		if(head.lastIndexOf(";") == (head.length() - 1)) {
			StringBuffer tempHead = new StringBuffer();
			
			tempHead.append(head);
			
			head.setLength(0);
			head.append(tempHead.substring(0, tempHead.length() - 1));
			
			tempHead.setLength(0);
		}
		
		resultMeta.put("HEAD", head.toString());
		
		resultList.add(resultData);
		resultList.add(resultMeta);
		
		return resultList;
	}
	
	private int changeValueDbidCheckGradeCd(String dbidCheckGradeCd) {
		int value = -1;
		
		if(dbidCheckGradeCd.equalsIgnoreCase("C")) {
			value = 0;
		} else if(dbidCheckGradeCd.equalsIgnoreCase("W")) {
			value = 1;
		} else if(dbidCheckGradeCd.equalsIgnoreCase("I")) {
			value = 2;
		} else if(dbidCheckGradeCd.equalsIgnoreCase("N")) {
			value = 3;
		} else if(dbidCheckGradeCd.equalsIgnoreCase("U")) {
			value = 4;
		}
		
		return value;
	}
	/*
	private String space_table_1(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "TOTAL_SPACE(GB)";
		String head02 = "SPACE_USED(GB)";
		String head03 = "SPACE_USED(%)";
		String head04 = "THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(4));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					BigDecimal data1 = resultData.getTotal_space_size();
					BigDecimal data2 = resultData.getSpace_used();
					BigDecimal data3 = resultData.getSpace_used_percent();
					BigDecimal data4 = resultData.getThreshold_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data4, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data4, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String space_table_2(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "TOTAL_SPACE_SIZE(GB)";
		String head03 = "SPACE_USED(GB)";
		String head04 = "SPACE_USED(%)";
		String head05 = "THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(5));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getTotal_space_size();
					BigDecimal data3 = resultData.getSpace_used();
					BigDecimal data4 = resultData.getSpace_used_percent();
					BigDecimal data5 = resultData.getThreshold_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data5, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data5, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	
	private String space_table_3(List<DailyCheckDb> diagnosisResultMinute, String statusName, int index, String title, DailyCheckDb dailyCheckDb) {
		String symbol = "▪";
		StringBuffer outline = new StringBuffer();
		StringBuffer tableHtml = new StringBuffer();
		
		outline.append("<div id='").append(statusName).append("_").append(index).append("'>\n");
		outline.append("\t<h2 class='minute_title_2' name='").append(statusName).append("_").append(index).append("'>").append(symbol).append("&nbsp;&nbsp;").append(title).append("</h2>\n");
		outline.append("\t<div id='").append(statusName).append("_T").append(index).append("'>\n");
		
		String head01 = "INST_ID";
		String head02 = "TOTAL_SPACE_SIZE(GB)";
		String head03 = "SPACE_USED(GB)";
		String head04 = "SPACE_USED(%)";
		String head05 = "THRESHOLD(%)";
		
		try {
			int dataSize = diagnosisResultMinute.size();
			
			if(dataSize == 0) {
				tableHtml.append(dailyCheckDb.tableStyle1(2, false));
//				tableHtml.append("\t\t\t\t\t<tr>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
//				tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
				
				tableHtml.append(dailyCheckDb.tableStyleNoDataClose(5));
			} else {
				for(int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
					DailyCheckDb resultData = diagnosisResultMinute.get(dataIndex);
					String data1 = resultData.getInst_id();
					BigDecimal data2 = resultData.getTotal_space_size();
					BigDecimal data3 = resultData.getSpace_used();
					BigDecimal data4 = resultData.getSpace_used_percent();
					BigDecimal data5 = resultData.getThreshold_percent();
					
					if(dataIndex == 0) {
						tableHtml.append(dailyCheckDb.tableStyle1(2, false));
						tableHtml.append("\t\t\t\t\t<tr>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-left\">").append(head01).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head02).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head03).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-center\">").append(head04).append("</th>\n");
						tableHtml.append("\t\t\t\t\t\t<th class=\"om-right\">").append(head05).append("</th>\n");
						tableHtml.append("\t\t\t\t\t</tr>\n\t\t\t\t</tr>\n\t\t\t</thead>\n");
					}
					
					if(dataIndex == (dataSize - 1)) {
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(1, data1, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data2, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data3, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(2, data4, 0, 2));
						tableHtml.append(dailyCheckDb.tableStyle1BodyClose(3, data5, 0, 2));
						
						continue;
					}
					
					tableHtml.append(dailyCheckDb.tableStyle1Body(1, data1, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data2, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data3, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(2, data4, 0, 2));
					tableHtml.append(dailyCheckDb.tableStyle1Body(3, data5, 0, 2));
				}
			}
			
			tableHtml.append("\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		outline.append(tableHtml);
		outline.append("</div>\n");
		outline.append("</div>\n");
		
		logger.debug("statusName[" + statusName + "] index[" + index + "] outline[" + outline + "]");
		
		return outline.toString();
	}
	*/
}
