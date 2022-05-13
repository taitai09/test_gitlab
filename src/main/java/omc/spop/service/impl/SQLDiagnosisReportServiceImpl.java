package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import omc.spop.dao.SQLDiagnosisReportDao;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.SQLDiagnosisReport;
import omc.spop.model.SQLStandards;
import omc.spop.service.SQLDiagnosisReportService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.ExcelDownHandler;

/** 
	* @packageName	:	omc.spop.service.impl 
	* @fileName		:	SQLDiagnosisReportServiceImpl.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	:   SQL 종합 진단 SERVICE
	* @History		
	============================================================
	2021.06.09        wonjae kim 			history
	============================================================
*/
@Service("SQLDiagnosisReportService")
public class SQLDiagnosisReportServiceImpl implements SQLDiagnosisReportService {
	
	@Autowired
	SQLDiagnosisReportDao sqlDiagnosisReportDAO;
	
	@Autowired
	SQLStandardsDao sqlStandardsDAO;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ArrayList<SQLDiagnosisReport> selectSqlDiagnosisReportSchedulerNameList(SQLDiagnosisReport sqlDiagnosisReport) throws Exception{
		return sqlDiagnosisReportDAO.selectSqlDiagnosisReportSchedulerNameList(sqlDiagnosisReport);
	}
	
	@Override
	public SQLDiagnosisReport selectSQLDiagnosisReportSchedulerSchedule(SQLDiagnosisReport sqlDiagnosisReport) throws Exception{
		return sqlDiagnosisReportDAO.selectSQLDiagnosisReportSchedulerSchedule(sqlDiagnosisReport);
	}
	@Override
	public List<LinkedHashMap<String, Object>> selectSqlDiagnosisRuleData(SQLStandards sqlStandards) throws Exception {
		
		return loadQualityTable(sqlStandards , "021" , false);
		
//		return sqlDiagnosisReportDAO.selectSqlDiagnosisRuleData(sqlDiagnosisReport);
	}
	@Override
	public List<LinkedHashMap<String, Object>> selectSqlDiagnosisReportDetailInfo(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> list = loadQualityTable(sqlStandards , "022" , true);
		
		//해당 데이터 사이즈가 커 JS에서 데이터가 오염되어 String type 으로 변경
		for(LinkedHashMap<String , Object> map : list) {
			if(map.get("SQL_STD_QTY_CHKT_ID") != null && StringUtils.isNotEmpty(map.get("SQL_STD_QTY_CHKT_ID").toString())) {
				map.put("SQL_STD_QTY_CHKT_ID", map.get("SQL_STD_QTY_CHKT_ID").toString());
			}
		}
		
		
		return list;
//		return sqlDiagnosisReportDAO.selectSqlDiagnosisReportDetailInfo(sqlDiagnosisReport);
	}
	@Override
	public List<LinkedHashMap<String, Object>> loadQualityTable(SQLStandards sqlStandards , String dbNum , boolean paging) throws Exception{
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		SQLStandards dataQtyChkSql = null;
		
		
		try {
			List<String> qtyChkIdtCdList = new ArrayList<String>();
			qtyChkIdtCdList.add(dbNum);
			
			dataQtyChkSql = sqlStandardsDAO.getQtyChkSQL_List(qtyChkIdtCdList).get(0);
			String qty_chk_sql = CryptoUtil.decryptAES128(dataQtyChkSql.getQty_chk_sql(), "openmade");
			
			sqlStandards.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
			
			if(paging) {
				dataList.addAll(sqlStandardsDAO.loadQualityTable(sqlStandards));
			}else {
				dataList.addAll(sqlStandardsDAO.excelDownQualityTable(sqlStandards));
			}

			qtyChkIdtCdList = null;
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			
		}finally {
			dataQtyChkSql = null;
		}
		
		return dataList;
	}
	
	private String parseAndCondition(SQLStandards sqlStandards, String qty_chk_sql) throws Exception {
		logger.debug("...parseAndCondition");
		logger.debug(qty_chk_sql);
		StringBuffer sb = new StringBuffer();
		
		int preIndex = qty_chk_sql.indexOf("#{wrkjob_cd}");
		int postIndex = qty_chk_sql.indexOf("}", preIndex);
		logger.debug("preIndex[" + preIndex + "] postIndex[" + postIndex + "]");
		
		try {
			if (preIndex > 0 && qty_chk_sql.substring(preIndex + 2, postIndex).equalsIgnoreCase("wrkjob_cd")) {
				String wrkjob_cd = sqlStandards.getWrkjob_cd();
				int preAndIndex = qty_chk_sql.lastIndexOf("AND", preIndex);
				int postEndOfLikeIndex = qty_chk_sql.indexOf("'%'", postIndex) + 3;
				int preColumnIndex = qty_chk_sql.lastIndexOf("WRKJOB_CD", preIndex) - 2;
				String preColumnName = qty_chk_sql.substring(preColumnIndex, preColumnIndex + 11);
				logger.debug("preAndIndex[" + preAndIndex + "] postEndOfLikeIndex[" + postEndOfLikeIndex
						+ "] preColumnIndex[" + preColumnIndex + "] preColumnName[" + preColumnName + "]");
				
				if (wrkjob_cd != null && wrkjob_cd.equalsIgnoreCase("0")) {
					sb.append(qty_chk_sql.substring(0, preAndIndex));
					sb.append(qty_chk_sql.substring(postEndOfLikeIndex));
				} else {
					sb.append(qty_chk_sql.substring(0, preAndIndex));
					if (wrkjob_cd != null) {
						sb.append(pastAndCaseWrkjob_cd(preColumnName, wrkjob_cd));
					}
					sb.append(qty_chk_sql.substring(postEndOfLikeIndex));
				}
			} else {
				sb.append(qty_chk_sql);
			}
			
			String dbio = sqlStandards.getDbio();
			
			if (dbio != null && dbio.length() > 0) {
				logger.debug("SQL 식별자(DBIO)가 입력된 경우  조건");
				String sql = sb.toString();
				
				sb = new StringBuffer();
				
				int preIndexOrderBy = sql.indexOf("ORDER BY");
				logger.debug("preIndexOrderby[" + preIndexOrderBy + "]");
				
				if (preIndexOrderBy > 0) {
					logger.debug("ORDER BY 조건이 있는 경우");
					
					sb.append(sql.substring(0, preIndexOrderBy));
					sb.append(pastAndCaseDbio(dbio));
					sb.append(sql.substring(preIndexOrderBy));
				} else {
					logger.debug("ORDER BY 조건이 없는 경우");
					
					sb.append(sql.substring(0));
					sb.append(pastAndCaseDbio(dbio));
				}
			}
		} catch (Exception ex) {
			logger.error("parseAndCondition error:" + ex);
			throw ex;
		}
		
		logger.debug("final query...");
		logger.debug(sb.toString());
		logger.debug("..............");
		
		return sb.toString();
	}
	@Override
	public boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sql = "";
		
		try {
			List<SQLStandards> codeList = sqlStandardsDAO.loadQtyIdxByProject("0");
			
			handler.open();
			
			handler.buildInit("SQL 품질진단 집계표", "SQL_DIAGNOSIS_REPORT_021");
			handler.addKoHeaders( getCodeListByProject(codeList, "ko", 1) );
			handler.addEnHeaders( getCodeListByProject(codeList, "en", 1) );
			
			sql = selectRule("021", sqlStandards);
			handler.buildDocument(sqlSessionFactory, sql);
			
			
			handler.buildInit("표준 미준수 SQL", "SQL_DIAGNOSIS_REPORT_022");
			
			List<String> returnList_ko = new ArrayList<String>();
			List<String> returnList_en = new ArrayList<String>();

			returnList_ko = getCodeListByProject(codeList, "ko", 2);
			returnList_en = getCodeListByProject(codeList, "en", 2);
			
			returnList_ko.add("SQL_TEXT");
			returnList_en.add("SQL_TEXT");

			handler.addKoHeaders( returnList_ko );
			handler.addEnHeaders( returnList_en );
			
			sql = selectRule("022", sqlStandards);
			handler.buildDocument(sqlSessionFactory, sql);
			
			logger.debug("*************************");
			logger.debug(codeList.toString());
			logger.debug(getCodeListByProject(codeList, "ko", 2).toString());
			logger.debug(getCodeListByProject(codeList, "en", 2).toString());
			logger.debug("*************************");

			
			handler.writeDoc();
			
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		return resultSuccess;
	}
	@Override
	public SQLDiagnosisReport selectSchedulerStatus(SQLDiagnosisReport sqlDiagnosisReport) throws Exception {
		return sqlDiagnosisReportDAO.selectSchedulerStatus(sqlDiagnosisReport);
	}
	private String selectRule(String qtyChekIdtCd, SQLStandards sqlStandards) throws Exception {
		List<String> qtyChkIdtCdList = new ArrayList<String>();
		List<SQLStandards> dataQtyChkSql = null;
		SQLStandards SQLStandardsRow = null;
		
		String qty_chk_sql = null;
		
		try {
			qtyChkIdtCdList.add(qtyChekIdtCd);
			
			dataQtyChkSql = sqlStandardsDAO.getQtyChkSQL_List(qtyChkIdtCdList);
			SQLStandardsRow = dataQtyChkSql.get(0);
			
			qty_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getQty_chk_sql(), "openmade");
			
			qty_chk_sql = qty_chk_sql.replace("#{std_qty_target_dbid}", sqlStandards.getStd_qty_target_dbid());
			qty_chk_sql = qty_chk_sql.replace("#{sql_std_qty_scheduler_no}", sqlStandards.getSql_std_qty_scheduler_no());
			qty_chk_sql = qty_chk_sql.replace("#{qty_chk_idt_cd}","\'" + sqlStandards.getQty_chk_idt_cd() + "\'");
			
//			SQLStandardsRow.setStd_qty_target_dbid( sqlStandards.getStd_qty_target_dbid() );
			SQLStandardsRow.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			qtyChkIdtCdList = null;
			dataQtyChkSql = null;
		}
		
		return qty_chk_sql;
	}
	private List<String> getCodeListByProject(List<SQLStandards> codeList, String lang, int code) {
		List<String> returnList = new ArrayList<String>();
		String tail = (code == 1) ? "CNT" : "YN";
		
		if("ko".equals(lang)) {
			for(int i = 0; i < codeList.size(); i++) {
				returnList.add( codeList.get(i).getQty_chk_idt_nm() );
			}
		}else {
			for(int i = 0; i < codeList.size(); i++) {
				returnList.add( "SQL" + codeList.get(i).getQty_chk_idt_cd() + "ERR_" + tail );
			}
		}
		
		return returnList;
	}
	private String pastAndCaseWrkjob_cd(String preColumnName, String wrkjob_cd) {
		return "\r\n" + "-- 업무코드가 입력되면\r\n" + "AND " + preColumnName + " IN ( SELECT WRKJOB_CD\r\n"
				+ "                FROM WRKJOB_CD\r\n" + "                START WITH WRKJOB_CD =  '" + wrkjob_cd
				+ "'\r\n" + "                CONNECT BY PRIOR WRKJOB_CD = UPPER_WRKJOB_CD )";
	}
	
	private String pastAndCaseDbio(String dbio) {
		return "\r\n" + "-- SQL 식별자(DBIO)가 입력되면\r\n" + "AND UPPER(A.DBIO) LIKE '%'||UPPER('" + dbio + "')||'%'";
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> selectProgramSourceDesc(SQLDiagnosisReport sqlDiagnosisReport) throws Exception {
		return sqlDiagnosisReportDAO.selectProgramSourceDesc(sqlDiagnosisReport);
	}
	
	@Override
	public List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception {
		return sqlDiagnosisReportDAO.loadQtyIdxByProject(sqlStandards);
	}

}
