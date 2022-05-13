package omc.spop.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.service.SQLStandardsService;
import omc.spop.utils.ConvertRecord;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;
import omc.spop.view.XlsxMultiDynamicBuilder;
import oracle.sql.CLOB;

@Service("SQLStandardsService")
public class SQLStandardsServiceImpl implements SQLStandardsService {
	private final Logger logger = LoggerFactory.getLogger(SQLStandardsServiceImpl.class);
	
	@Autowired
	private SQLStandardsDao sqlStandardsDao;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private String key = "openmade";
	
	@Override
	public List<SQLStandards> loadAllIndex(HashMap<String, String> map) throws Exception {
		return sqlStandardsDao.loadAllIndex(map);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadQualityTable(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		String project_id = sqlStandards.getProject_id();
		String wrkjob_cd = sqlStandards.getWrkjob_cd();
		int currentPage = sqlStandards.getCurrentPage();
		int pagePerCount = sqlStandards.getPagePerCount();
		List<SQLStandards> dataQtyChkSql = sqlStandardsDao.getQtyChkSQL(sqlStandards);
		
		for (SQLStandards SQLStandardsRow : dataQtyChkSql) {
			String qty_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getQty_chk_sql(), key);
			
			SQLStandardsRow.setProject_id(project_id);
			SQLStandardsRow.setWrkjob_cd(wrkjob_cd);
			SQLStandardsRow.setCurrentPage(currentPage);
			SQLStandardsRow.setPagePerCount(pagePerCount);
			SQLStandardsRow.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
			
			dataList = sqlStandardsDao.loadQualityTable(SQLStandardsRow);
		}
		
		return convertRecord(dataList);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownQualityTable(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		String project_id = sqlStandards.getProject_id();
		String wrkjob_cd = sqlStandards.getWrkjob_cd();
		List<SQLStandards> dataQtyChkSql = sqlStandardsDao.getProjectQtyChkSQL(sqlStandards);
		
		for (SQLStandards SQLStandardsRow : dataQtyChkSql) {
			String qty_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getQty_chk_sql(), key);
			logger.debug("qualityTable sql:\n" + qty_chk_sql);
			
			SQLStandardsRow.setProject_id(project_id);
			SQLStandardsRow.setWrkjob_cd(wrkjob_cd);
			SQLStandardsRow.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
			
			try {
				dataList = sqlStandardsDao.excelDownQualityTable(SQLStandardsRow);
			} catch (Exception ex) {
				dataList.add(getExcelErrorMap(ex));
				
				return dataList;
			}
		}
		
		return convertRecord(dataList);
	}
	
	private LinkedHashMap<String, Object> getExcelErrorMap(Exception ex) {
		String msg = ex.getMessage();
		
		if (msg != null && msg.contains("ORA-")) {
			msg = msg.substring(msg.lastIndexOf("ORA-"));
		}
		
		if (msg == null) {
			msg = "처리중 오류가 발생하였습니다.";
		}
		
		LinkedHashMap<String, Object> temporaryMap = new LinkedHashMap<String, Object>();
		
		temporaryMap.put(XlsxMultiDynamicBuilder.HEAD_OPTIONS_EXCEL_ERROR_KEY, msg);
		
		return temporaryMap;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadProjectQualityTable(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		String project_id = sqlStandards.getProject_id();
		String wrkjob_cd = sqlStandards.getWrkjob_cd();
		int currentPage = sqlStandards.getCurrentPage();
		int pagePerCount = sqlStandards.getPagePerCount();
		List<SQLStandards> dataQtyChkSql = sqlStandardsDao.getProjectQtyChkSQL(sqlStandards);
		
		for (SQLStandards SQLStandardsRow : dataQtyChkSql) {
			String qty_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getQty_chk_sql(), key);
			
			SQLStandardsRow.setProject_id(project_id);
			SQLStandardsRow.setWrkjob_cd(wrkjob_cd);
			SQLStandardsRow.setCurrentPage(currentPage);
			SQLStandardsRow.setPagePerCount(pagePerCount);
			SQLStandardsRow.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
			
			dataList = sqlStandardsDao.loadQualityTable(SQLStandardsRow);
		}
		
		return convertRecord(dataList);
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
	
	private String pastAndCaseWrkjob_cd(String preColumnName, String wrkjob_cd) {
		return "\r\n" + "-- 업무코드가 입력되면\r\n" + "AND " + preColumnName + " IN ( SELECT WRKJOB_CD\r\n"
				+ "                FROM WRKJOB_CD\r\n" + "                START WITH WRKJOB_CD =  '" + wrkjob_cd
				+ "'\r\n" + "                CONNECT BY PRIOR WRKJOB_CD = UPPER_WRKJOB_CD )";
	}
	
	private String pastAndCaseDbio(String dbio) {
		return "\r\n" + "-- SQL 식별자(DBIO)가 입력되면\r\n" + "AND UPPER(A.DBIO) LIKE '%'||UPPER('" + dbio + "')||'%'";
	}
	
	private List<LinkedHashMap<String, Object>> convertRecord(List<LinkedHashMap<String, Object>> dataList) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		ConvertRecord convertRecord = ConvertRecord.getInstance();
		
		convertRecord.converRecord(dataList);
		
		resultList = convertRecord.getResultData();
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadNonCompliantCode(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		String wrkjob_cd = sqlStandards.getWrkjob_cd();
		String dbio = sqlStandards.getDbio();
		int currentPage = sqlStandards.getCurrentPage();
		int pagePerCount = sqlStandards.getPagePerCount();
		List<SQLStandards> dataQtyChkSql = sqlStandardsDao.getProjectQtyChkSQL(sqlStandards);
		SQLStandards data = dataQtyChkSql.get(0);
		String qty_chk_sql = CryptoUtil.decryptAES128(data.getQty_chk_sql(), key);
		
		data.setWrkjob_cd(wrkjob_cd);
		data.setCurrentPage(currentPage);
		data.setPagePerCount(pagePerCount);
		data.setDbio(dbio);
		data.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
		
		dataList = sqlStandardsDao.loadQualityTable(data);
		
		return convertRecord(dataList);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownNonCompliantCode(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		String wrkjob_cd = sqlStandards.getWrkjob_cd();
		String project_id = sqlStandards.getProject_id();
		List<SQLStandards> dataQtyChkSql = sqlStandardsDao.getProjectQtyChkSQL(sqlStandards);
		SQLStandards data = dataQtyChkSql.get(0);
		String qty_chk_sql = CryptoUtil.decryptAES128(data.getQty_chk_sql(), key);
		logger.debug("excelDownNonCompliantCode sql:\n" + qty_chk_sql);
		
		data.setWrkjob_cd(wrkjob_cd);
		data.setProject_id(project_id);
		data.setQty_chk_sql(parseAndCondition(sqlStandards, qty_chk_sql));
		
		try {
			dataList = sqlStandardsDao.excelDownQualityTable(data);
		} catch (Exception ex) {
			dataList.add(getExcelErrorMap(ex));
			
			return dataList;
		}
		
		return convertRecord(dataList);
	}
	
	@Override
	public List<SQLStandards> maintainQualityCheckIndicator(SQLStandards sqlStandards) {
		return sqlStandardsDao.maintainQualityCheckIndicator(sqlStandards);
	}
	
	@Override
	public Result saveMaintainQualityCheckIndicator(SQLStandards sqlStandards) {
		Result result = new Result();
		HashMap<String, Object> param = new HashMap<String, Object>();
		int resultValue = -1;
		
		param.put("qty_chk_idt_cd", sqlStandards.getQty_chk_idt_cd());
		
		if (sqlStandards.getRowStatus().equals("i")) {
			if (sqlStandardsDao.countMaintainQualityCheckIndicator(param) > 0) {
				result.setResult(false);
				result.setMessage("품질 점검 지표 코드가 존재하여 등록되지 않았습니다.");
			} else {
				resultValue = sqlStandardsDao.saveMaintainQualityCheckIndicator(sqlStandards);
				
				if (resultValue > 0) {
					result.setResult(true);
					result.setMessage("품질 점검 지표가 등록되었습니다.");
				} else {
					result.setResult(false);
					result.setMessage("품질 점검 지표가 등록되지 않았습니다.");
				}
			}
		} else {
			resultValue = sqlStandardsDao.saveMaintainQualityCheckIndicator(sqlStandards);
			
			if (resultValue > 0) {
				result.setResult(true);
				result.setMessage("품질 점검 지표가 등록되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("품질 점검 지표가 등록되지 않았습니다.");
			}
		}
		
		return result;
	}
	
	@Override
	public int deleteMaintainQualityCheckIndicator(SQLStandards sqlStandards) {
		return sqlStandardsDao.deleteMaintainQualityCheckIndicator(sqlStandards);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckIndicator(SQLStandards sqlStandards) {
		return sqlStandardsDao.excelDownMaintainQualityCheckIndicator(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> getQtyChkIdtCd(SQLStandards sqlStandards) {
		return sqlStandardsDao.getQtyChkIdtCd(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> getQtyChkIdtCd2(SQLStandards sqlStandards) {
		return sqlStandardsDao.getQtyChkIdtCd2(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> maintainQualityCheckSql(SQLStandards sqlStandards) throws Exception {
		List<SQLStandards> qtyChkSqlList = sqlStandardsDao.maintainQualityCheckSql(sqlStandards);
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String qty_chk_sql = "";
		
		for (SQLStandards sqlStandardsRow : qtyChkSqlList) {
			if (user_auth_id.equals("9")) { // OPENPOP-MANAGER 일 경우
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(sqlStandardsRow.getQty_chk_sql(), key);
				} catch (Exception e) {
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
				
				sqlStandardsRow.setQty_chk_sql(qty_chk_sql);
			} else {
				sqlStandardsRow.setQty_chk_sql(sqlStandardsRow.getQty_chk_sql());
			}
			
			resultList.add(sqlStandardsRow);
		}
		
		return resultList;
	}
	
	@Override
	public Result saveMaintainQualityCheckSql(SQLStandards sqlStandards) {
		Result result = new Result();
		HashMap<String, Object> param = new HashMap<String, Object>();
		int resultValue = -1;
		String qty_chk_sql = "";
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		
		param.put("qty_chk_idt_cd", sqlStandards.getQty_chk_idt_cd());
		
		try {
			if (user_auth_id.equals("9")) { // OPENPOP-MANAGER 일 경우
				qty_chk_sql = CryptoUtil.encryptAES128(sqlStandards.getQty_chk_sql(), key);
			} else {
				qty_chk_sql = sqlStandards.getQty_chk_sql();
			}
			
			param.put("qty_chk_sql", qty_chk_sql);
			param.put("dml_yn", sqlStandards.getDml_yn());
			param.put("project_by_mgmt_yn", sqlStandards.getProject_by_mgmt_yn());
			
			if (sqlStandards.getRowStatus().equals("i")) {
				if (sqlStandardsDao.countMaintainQualityCheckSql(param) == 0) {
					resultValue = sqlStandardsDao.insertMaintainQualityCheckSql(param);
					
					if (resultValue > 0) {
						result.setResult(true);
						result.setMessage("품질 점검 SQL이 등록되었습니다.");
						
					} else {
						result.setResult(false);
						result.setMessage("품질 점검 SQL이 등록되지 않았습니다.");
					}
				} else {
					result.setResult(false);
					result.setMessage("품질 점검 지표 코드가 존재하여 등록되지 않았습니다.");
				}
			} else {
				resultValue = sqlStandardsDao.saveMaintainQualityCheckSql(param);
				
				if (resultValue > 0) {
					result.setResult(true);
					result.setMessage("품질 점검 SQL이 수정되었습니다.");
				} else {
					result.setResult(false);
					result.setMessage("품질 점검 SQL이 수정되지 않았습니다.");
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public int deleteMaintainQualityCheckSql(SQLStandards sqlStandards) {
		return sqlStandardsDao.deleteMaintainQualityCheckSql(sqlStandards);
	}
	
	@Override
	public List<Map<String, Object>> excelDownMaintainQualityCheckSql(SQLStandards sqlStandards) throws Exception {
		List<Map<String, Object>> dataList = sqlStandardsDao.excelDownMaintainQualityCheckSql(sqlStandards);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String qty_chk_sql = "";
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		
		for (Map<String, Object> dataListRow : dataList) {
			CLOB clob = (oracle.sql.CLOB) dataListRow.get("QTY_CHK_SQL");
			
			if (user_auth_id.equals("9")) { // OPENPOP-MANAGER 일 경우
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(clob.stringValue(), key);
				} catch (Exception e) {
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
			} else {
				qty_chk_sql = clob.stringValue();
			}
			
			dataListRow.put("QTY_CHK_SQL", qty_chk_sql);
			resultList.add(dataListRow);
		}
		
		return resultList;
	}
	
	@Override
	public List<SQLStandards> getQtyChkIdtCdFromException(SQLStandards sqlStandards) {
		return sqlStandardsDao.getQtyChkIdtCdFromException(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> maintainQualityCheckException(SQLStandards sqlStandards) {
		return sqlStandardsDao.maintainQualityCheckException(sqlStandards);
	}
	
	@Override
	public Result saveMaintainQualityCheckException(SQLStandards sqlStandards) throws Exception {
		Result result = new Result();
		HashMap<String, Object> param = new HashMap<String, Object>();
		int resultValue = -1;
		
		try {
			param.put("qty_chk_idt_cd", sqlStandards.getQty_chk_idt_cd());
			param.put("wrkjob_cd", sqlStandards.getWrkjob_cd());
			param.put("dir_nm", sqlStandards.getDir_nm());
			param.put("dbio", sqlStandards.getDbio());
			
			if (sqlStandards.getRowStatus().equals("i")) {
				if (sqlStandardsDao.countMaintainQualityCheckException(param) > 0) {
					result.setResult(false);
					result.setMessage("입력한 예외 대상 정보는 SQL 품질검토 예외 대상 관리에 등록되어 있습니다.");
				} else {
					resultValue = sqlStandardsDao.saveMaintainQualityCheckException(sqlStandards);
					
					if (resultValue > 0) {
						result.setResult(true);
						result.setMessage("예외 대상 정보가 등록되었습니다.");
					} else {
						result.setResult(false);
						result.setMessage("예외 대상 정보가 등록되지 않았습니다.");
					}
				}
			} else {
				resultValue = sqlStandardsDao.saveMaintainQualityCheckException(sqlStandards);
				
				if (resultValue > 0) {
					result.setResult(true);
					result.setMessage("예외 대상 정보가 수정되었습니다.");
				} else {
					result.setResult(false);
					result.setMessage("예외 대상 정보가 수정되지 않았습니다.");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public int deleteMaintainQualityCheckException(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.deleteMaintainQualityCheckException(sqlStandards);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownMaintainQualityCheckException(SQLStandards sqlStandards) {
		
		List<LinkedHashMap<String, Object>> returnList
			= sqlStandardsDao.excelDownMaintainQualityCheckException(sqlStandards);
		removeHtmlChar(returnList);
		
		return returnList;
	}
	
	@Override
	public Result excelUploadMaintainQualityCheckException(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		int resultCount = 0;
		String filePath = "";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);
			
		} catch (Exception ex) {
			logger.error( "엑셀 파일 업로드 error => ", ex );
			throw ex;
		}
		
		option.setFilePath(filePath);
		option.setStartRow(2);
		
		int errCnt = 0;
		int totalCnt = 0;
		String errMsg = null;
		Map<String, Object> errObj = new HashMap<String, Object>(); // 최종 err 정보를 담을 obj
		List<SQLStandards> errList = new ArrayList<SQLStandards>();
		Vector<Integer> errIndexs = new Vector<Integer>(); // err 로우를 담을 obj
		int errIndex = 1; // 엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		SQLStandards sqlStandards = null;
		
		try {
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();
			
			for (Map<String, String> article : excelContent) {
				errIndex += 1;
				
				sqlStandards = new SQLStandards();
				sqlStandards.setQty_chk_idt_cd(StringUtils.defaultString(article.get("A")));
				sqlStandards.setWrkjob_cd(StringUtils.defaultString(article.get("B")));
				sqlStandards.setDir_nm(StringUtils.defaultString(article.get("C")));
				sqlStandards.setDbio(StringUtils.defaultString(article.get("D")));
				sqlStandards.setExcept_sbst(StringUtils.defaultString(article.get("E")));
				sqlStandards.setRequester(StringUtils.defaultString(article.get("F")));
				sqlStandards.setUser_id(user_id);
				
				int check = checkByteMethodForMaintainQualityCheckException(sqlStandards);
				logger.trace("check:" + check);
				
				if (check > 0) {
					logger.trace("################# 에러발생시 ################# ");
					logger.trace(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t");
					errList.add(sqlStandards);
					errIndexs.add(errIndex);
					errMsg = "입력한 필드 값 중에 한계치를 벗어났습니다. 아래의 필드값과 필드 한계치를 확인해 주세요." + "\n품질점검지표코드 "
							+ article.get("A").getBytes().length + "Byte / 업무코드 " + article.get("B").getBytes().length
							+ "Byte / 디렉토리명 " + article.get("C").getBytes().length + "Byte / DBIO "
							+ article.get("D").getBytes().length + "Byte / 예외사유 " + article.get("E").getBytes().length
							+ "Byte / 요청자 " + article.get("F").getBytes().length + "Byte"
							+ "\n품질점검지표코드 10Byte / 업무코드 5Byte / 디렉토리명 400Byte / DBIO 4000Byte / 예외사유 100Byte / 요청자 1000Byte";
					
					errCnt += 1;
				} else if (check == -1) {
					logger.trace("################# 에러발생시 ################# ");
					logger.trace(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t");
					errList.add(sqlStandards);
					errIndexs.add(errIndex);
					errMsg = "[품질 점검 지표 코드]는 필수로 입력되어야 하며, [업무 코드],[디렉토리명],[SQL 식별자(DBIO)] 등 이 셋 중의 하나는 입력되어야 합니다.";
					errCnt += 1;
					
				} else {
					resultCount += sqlStandardsDao.saveMaintainQualityCheckException(sqlStandards);
				}
			}
			
			errObj.put("totalCnt", totalCnt); // 사이즈
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errList", errList);
			errObj.put("errIndex", errIndexs.toString());
			if (errMsg != null && !errMsg.equals("")) {
				errObj.put("errMsg", errMsg);
			}
			result.setObject(errObj);
			result.setMessage("success");
			result.setResult(true);
			
		} catch (Exception ex) {
			logger.error( "엑셀 파일 조회 error => ", ex );
			
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errList", errList);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errIndex", errIndexs.toString());
			result.setObject(errObj);
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ " + totalCnt + " ] 건 중  [ " + (errIndex - 2) + " ] 건 성공 <BR/>"
					+ "[ " + errIndex + " ] 번째 행에서 에러가 발생하였습니다.<BR/>" + "[ 품질점검 지표코드 : "
					+ sqlStandards.getQty_chk_idt_cd() + ", " + "업무코드 : " + sqlStandards.getWrkjob_cd() + ", "
					+ "SQL 식별자(DBIO) : " + sqlStandards.getDbio() + " ]<BR/> " + ex.getMessage());
			result.setResult(false);
		}
		
		return result;
	}
	
	private int checkByteMethodForMaintainQualityCheckException(SQLStandards sqlStandards) {
		int check = 0;
		
		if (sqlStandards.getQty_chk_idt_cd().trim().equals("") || (sqlStandards.getWrkjob_cd().trim().equals("")
				&& sqlStandards.getDbio().trim().equals("") && sqlStandards.getDir_nm().trim().equals(""))) {
			check = -1;
		} else {
			check += sqlStandards.getQty_chk_idt_cd().getBytes().length <= 10 ? 0 : 1;
			check += sqlStandards.getWrkjob_cd().getBytes().length <= 5 ? 0 : 1;
			check += sqlStandards.getDir_nm().getBytes().length <= 400 ? 0 : 1;
			check += sqlStandards.getDbio().getBytes().length <= 4000 ? 0 : 1;
			check += sqlStandards.getExcept_sbst().getBytes().length <= 100 ? 0 : 1;
			check += sqlStandards.getRequester().getBytes().length <= 1000 ? 0 : 1;
		}
		
		return check;
	}
	
	@Override
	public List<SQLStandards> loadQualityReviewWork(SQLStandards sqlStandards) {
		return sqlStandardsDao.loadQualityReviewWork(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> loadSqlCount(SQLStandards sqlStandards) {
		return sqlStandardsDao.loadSqlCount(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> loadWorkStatus(SQLStandards sqlStandards) {
		return sqlStandardsDao.loadWorkStatus(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> loadErrorMessage(SQLStandards sqlStandards) {
		return sqlStandardsDao.loadErrorMessage(sqlStandards);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownQualityReviewWork(SQLStandards sqlStandards) {
		return sqlStandardsDao.excelDownQualityReviewWork(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> checkQualityReviewWorkInRun(SQLStandards sqlStandards) {
		return sqlStandardsDao.checkQualityReviewWorkInRun(sqlStandards);
	}
	
	@Override
	public Result forceProcessingCompleted(SQLStandards sqlStandards) {
		Result result = new Result();
		StringBuffer msg = new StringBuffer();
		List<SQLStandards> resultList;
		
		try {
			resultList = sqlStandardsDao.checkQualityReviewWorkInRun(sqlStandards);
			
			if (resultList.get(0).getWrk_process_yn().equalsIgnoreCase("Y")) { // 강제완료처리가 가능
				resultList = sqlStandardsDao.getUnfinishedCheckProc(sqlStandards);
				
				// check checkProc
				sqlStandards.setSql_std_gather_dt(resultList.get(0).getSql_std_gather_dt());
				sqlStandards.setWrk_end_dt(printCurrentTime(false));
				sqlStandardsDao.updatingForceCompleteCheckMainProcess(sqlStandards);
				
				int completed1 = sqlStandardsDao.forceProcessingCompleted1(sqlStandards);
				int completed2 = sqlStandardsDao.forceProcessingCompleted2(sqlStandards);
				int completed3 = sqlStandardsDao.forceProcessingCompleted3(sqlStandards);
				
				logger.debug("completed1:" + completed1 + " completed2:" + completed2 + " completed3:" + completed3);
				
				msg.append("강제 삭제 정보<br>테이블 SQL_STD_QTY_CHK_PROC_TMP1 : ").append(completed1);
				msg.append("<br>테이블 SQL_STD_QTY_CHK_PROC_TMP2 : ").append(completed2);
				msg.append("<br>테이블 SQL_STD_QTY_CHK_PROC_TMP3 : ").append(completed3);
				
				result.setResult(true);
				result.setObject(msg.toString());
			} else {
				logger.debug(
						"[품질검토작업 진행 여부 판단] 값이 [" + resultList.get(0).getWrk_process_yn() + "]임으로 강제완료처리는 진행하지 않음.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public void loadPreProcess(SQLStandards temporaryModel) throws Exception {
		SQLStandards dummyModel = null;
		String[] processArray = temporaryModel.getQty_chk_sql().split(";");
		
		try {
			for (int index = 0; index < processArray.length; index++) {
				if ((processArray[index]).trim().length() == 0) {
					continue;
				}
				
				dummyModel = new SQLStandards();
				
				dummyModel.setQty_chk_sql(processArray[index]);
				
				logger.debug("loadPreProcess index[" + index + "]\n" + processArray[index]);
				int updateCount = sqlStandardsDao.updateSQL(dummyModel);
				logger.debug("loadPreProcess updateCount:" + updateCount);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	@Async("threadPoolTaskExecutor")
	public void qualityReviewOperation(SQLStandards temporaryModel) throws Exception {
		String[] processArray = temporaryModel.getQty_chk_sql().split(";");
		
		try {
			for (int index = 0; index < processArray.length; index++) {
				if ((processArray[index]).trim().length() == 0) {
					continue;
				}
				
				logger.debug("qualityReviewOperation index[" + index + "]\n" + processArray[index]);
				loadMainProcess(processArray[index]);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			String errorMessage = ex.getMessage();
			int oraIndex = errorMessage.lastIndexOf("ORA-");
			String err_sbst = errorMessage.substring(oraIndex);
			int insertIntoIndex = errorMessage.indexOf("INSERT INTO ");
			int afterInsertIntoIndex = errorMessage.indexOf("(", insertIntoIndex);
			String err_table_name = errorMessage.substring(insertIntoIndex + 12, afterInsertIntoIndex);
			
			temporaryModel.setErr_yn("Y");
			temporaryModel.setErr_sbst(err_sbst);
			temporaryModel.setErr_table_name(err_table_name);
			temporaryModel.setWrk_end_dt(printCurrentTime(false));
			
			sqlStandardsDao.updateCheckMainProcess(temporaryModel);
		}
	}
	
	private String printCurrentTime(boolean printFlag) {
		SimpleDateFormat dateFormat = null;
		
		if (printFlag) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		} else {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}
	
	@Override
	public int updateCheckMainProcess(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.updateCheckMainProcess(sqlStandards);
	}
	
	public void loadMainProcess(String process) throws Exception {
		SQLStandards sqlStandards = new SQLStandards();
		
		try {
			sqlStandards.setQty_chk_sql(process);
			
			int updateCount = sqlStandardsDao.updateSQL(sqlStandards);
			logger.debug("loadMainProcess updateCount:" + updateCount);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			throw ex;
		}
	}
	
	@Override
	public void qualityReviewOperationSync(SQLStandards temporaryModel) throws Exception {
		String[] processArray = temporaryModel.getQty_chk_sql().split(";");
		
		try {
			for (int index = 0; index < processArray.length; index++) {
				if ((processArray[index]).trim().length() == 0) {
					continue;
				}
				
				logger.debug("qualityReviewOperation index[" + index + "]\n" + processArray[index]);
				loadMainProcess(processArray[index]);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	@Override
	public void summaryTask(SQLStandards temporaryModel, SQLStandards sqlStandards) throws Exception {
		SQLStandards dummyModel = null;
		String[] processArray = temporaryModel.getQty_chk_sql().split(";");
		
		try {
			logger.debug("processArray.length:" + processArray.length);
			
			for (int index = 0; index < processArray.length; index++) {
				if ((processArray[index]).trim().length() == 0) {
					continue;
				}
				
				dummyModel = new SQLStandards();
				
				dummyModel.setQty_chk_sql(processArray[index]);
				
				logger.debug("summaryTask index[" + index + "]\n" + processArray[index]);
				
				int updateCount = sqlStandardsDao.updateSQL(dummyModel);
				logger.debug("summaryTask updateCount:" + updateCount);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	@Override
	public void summaryErrorTask(SQLStandards temporaryModel, SQLStandards sqlStandards) throws Exception {
		SQLStandards dummyModel = null;
		String[] processArray = temporaryModel.getQty_chk_sql().split(";");
		
		try {
			logger.debug("processArray.length:" + processArray.length);
			
			for (int index = 0; index < processArray.length; index++) {
				if ((processArray[index]).trim().length() == 0) {
					continue;
				}
				
				dummyModel = new SQLStandards();
				
				dummyModel.setQty_chk_sql(processArray[index]);
				
				logger.debug("summaryErrorTask index[" + index + "]\n" + processArray[index]);
				
				int updateCount = sqlStandardsDao.updateSQL(dummyModel);
				logger.debug("summaryErrorTask updateCount:" + updateCount);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	@Override
	public List<SQLStandards> checkProc(SQLStandards sqlStandards) {
		return sqlStandardsDao.checkProc(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> checkMainProcess(SQLStandards sqlStandards) {
		return sqlStandardsDao.checkMainProcess(sqlStandards);
	}
	
	@Override
	public int updateProc(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.updateProc(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> getUnfinishedCheckProc(SQLStandards sqlStandards) {
		return sqlStandardsDao.getUnfinishedCheckProc(sqlStandards);
	}
	
	@Override
	public int updatingCompleteCheckMainProcess(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.updatingCompleteCheckMainProcess(sqlStandards);
	}
	
	@Override
	public int updatingForceCompleteCheckMainProcess(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.updatingForceCompleteCheckMainProcess(sqlStandards);
	}
	
	public void printCurrentTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Calendar calendar = Calendar.getInstance();
		logger.debug("current time:" + dateFormat.format(calendar.getTime()));
	}
	
	@Override
	public SQLStandards decryptRule(StandardComplianceRateTrend scrt) throws Exception {
		return commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list());
	}
	
	private SQLStandards commonModule(String qty_chk_idt_cd, String parameter_list) throws Exception {
		SQLStandards sqlStandards = new SQLStandards();
		sqlStandards.setQty_chk_idt_cd(qty_chk_idt_cd);
		List<SQLStandards> qtyChkSqlList = sqlStandardsDao.getQtyChkSQL(sqlStandards);
		logger.debug("[" + qty_chk_idt_cd + "] qtyChkSqlList*****\n" + qtyChkSqlList);
		SQLStandards data = qtyChkSqlList.get(0);
		String qty_chk_sql = CryptoUtil.decryptAES128(data.getQty_chk_sql(), key);
		logger.debug("[" + qty_chk_idt_cd + "] decrypt qty_chk_sql*****\n" + qty_chk_sql);
		data.setQty_chk_sql(parseAndCondition(parameter_list, qty_chk_sql));
		
		return data;
	}
	
	@Override
	public SQLStandards decryptRuleProject(StandardComplianceRateTrend scrt) throws Exception {
		return commonModuleProject(scrt.getProject_id(), scrt.getQty_chk_idt_cd(), scrt.getParameter_list());
	}
	
	private SQLStandards commonModuleProject(String project_id, String qty_chk_idt_cd, String parameter_list)
			throws Exception {
		SQLStandards sqlStandards = new SQLStandards();
		sqlStandards.setProject_id(project_id);
		sqlStandards.setQty_chk_idt_cd(qty_chk_idt_cd);
		List<SQLStandards> qtyChkSqlList = sqlStandardsDao.getProjectQtyChkSQL(sqlStandards);
		logger.debug("[" + qty_chk_idt_cd + "] qtyChkSqlList*****\n" + qtyChkSqlList);
		SQLStandards data = qtyChkSqlList.get(0);
		String qty_chk_sql = CryptoUtil.decryptAES128(data.getQty_chk_sql(), key);
		logger.debug("[" + qty_chk_idt_cd + "] decrypt qty_chk_sql*****\n" + qty_chk_sql);
		data.setQty_chk_sql(parseAndCondition(parameter_list, qty_chk_sql));
		
		return data;
	}
	
	private String parseAndCondition(String parameter_list, String qty_chk_sql) throws Exception {
		logger.debug("...parameter_list\n");
		logger.debug(parameter_list);
		logger.debug("\nqty_chk_sql\n");
		logger.debug(qty_chk_sql);
		logger.debug("..............");
		
		try {
			String[] parameterArray = parameter_list.split(",");
			Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
			
			for (int parameterIndex = 0; parameterIndex < parameterArray.length; parameterIndex++) {
				String parameter = parameterArray[parameterIndex];
				int separateIndex = parameter.indexOf(":");
				String key = parameter.substring(0, separateIndex);
				String value = parameter.substring(separateIndex + 1);
				
				parameterMap.put(key, value);
			}
			
			String[] numberSignArray = qty_chk_sql.split("#");
			List removeSyntax = new ArrayList();
			
			for (int numberSignIndex = 1; numberSignIndex < numberSignArray.length; numberSignIndex++) {
				String syntax = numberSignArray[numberSignIndex];
				int firstIndex = syntax.indexOf("}");
				String numberSignName = syntax.substring(1, firstIndex);
				String numberSign = parameterMap.get(numberSignName) == null ? "NULL"
						: parameterMap.get(numberSignName) + "";
				
				if (numberSign.equals("NULL")) {
					removeSyntax.add(numberSignName);
					numberSign = numberSignName;
				}
				
				syntax = "'" + numberSign + "'" + syntax.substring(firstIndex + 1);
				numberSignArray[numberSignIndex] = syntax;
			}
			
			qty_chk_sql = "";
			
			for (int numberSignIndex = 0; numberSignIndex < numberSignArray.length; numberSignIndex++) {
				qty_chk_sql = qty_chk_sql + numberSignArray[numberSignIndex];
			}
			
			if (removeSyntax.size() > 0) {
				logger.debug("before removeSyntax sql\n" + qty_chk_sql);
				qty_chk_sql = proofreadingQty_chk_sql(qty_chk_sql, removeSyntax);
				logger.debug("after removeSyntax sql\n" + qty_chk_sql);
			}
		} catch (Exception ex) {
			logger.error("parseAndCondition error:" + ex);
			throw ex;
		}
		
		logger.debug("final query...\n");
		logger.debug(qty_chk_sql);
		logger.debug("..............");
		
		return qty_chk_sql;
	}
	
	private String proofreadingQty_chk_sql(String qty_chk_sql, List removeSyntax) {
		for (int removeIndex = 0; removeIndex < removeSyntax.size(); removeIndex++) {
			String syntax = removeSyntax.get(removeIndex) + "";
			
			while (qty_chk_sql.contains(syntax)) {
				int index = qty_chk_sql.indexOf(syntax);
				String preTemp = qty_chk_sql.substring(0, index);
				String postTemp = qty_chk_sql.substring(index);
				int preIndex = preTemp.lastIndexOf("AND");
				int postIndex = postTemp.indexOf("AND");
				
				String preSql = preTemp.substring(0, preIndex);
				String postSql = postTemp.substring(postIndex);
				
				qty_chk_sql = preSql + postSql;
			}
		}
		
		return qty_chk_sql;
	}
	
	@Override
	public List<JobSchedulerBase> loadSchedulerList(SQLStandards sqlStandards) {
		if ("selectAll".equals(sqlStandards.getProject_id())) {
			sqlStandards.setProject_id("");
		}
		
		return sqlStandardsDao.loadSchedulerList(sqlStandards);
	}
	
	@Override
	public List<JobSchedulerBase> loadSchedulerListTest(SQLStandards sqlStandards) {
		if ("selectAll".equals(sqlStandards.getProject_id())) {
			sqlStandards.setProject_id("");
		}
		
		return sqlStandardsDao.loadSchedulerListTest(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception {
		String project_id = sqlStandards.getProject_id();
		
		return sqlStandardsDao.loadQtyIdxByProject(project_id);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadResultList(SQLStandards sqlStandards) throws Exception {
		
		return sqlStandardsDao.loadResultList(sqlStandards);
	}
	
	@Override
	public boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sql = "";
		String sqlId = "";
		
		try {
			String projectId = sqlStandards.getProject_id();
			List<SQLStandards> codeList = sqlStandardsDao.loadQtyIdxByProject(projectId);
			
			handler.open();
			
			handler.buildInit("SQL_품질검토_작업_집계표", "SQL_STD_QTY_RESULT_001");
			handler.addKoHeaders( getCodeListByProject(codeList, "ko", 1) );
			handler.addEnHeaders( getCodeListByProject(codeList, "en", 1) );
			
			sqlId = "omc.spop.dao.SQLStandardsDao.loadResultList";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlStandards);
			handler.buildDocument(sqlSessionFactory, sql);
			
			
			handler.buildInit("표준_미준수_SQL", "SQL_STD_QTY_RESULT_002");
			handler.addKoHeaders( getCodeListByProject(codeList, "ko", 2) );
			handler.addEnHeaders( getCodeListByProject(codeList, "en", 2) );
			
			sqlId = "omc.spop.dao.SQLStandardsDao.loadNonStdSqlResult";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlStandards);
			handler.buildDocument(sqlSessionFactory, sql);
			
			
			handler.buildInit("표준_미준수_개발자_집계표", "SQL_STD_QTY_RESULT_003");
			
			sqlId = "omc.spop.dao.SQLStandardsDao.summaryNonStdSqlByDev";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlStandards);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getName();
			logger.error("{} Excel Build Error",methodName , e);
			
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		return resultSuccess;
	}
	
	@Override
	public int forcedCompletion(SQLStandards sqlStandards) {

		return sqlStandardsDao.forcedCompletion(sqlStandards);
	}
	
	@Override
	public List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase) {
		if ("selectAll".equals(jobSchedulerBase.getProject_id())) {
			jobSchedulerBase.setProject_id("");
		}
		
		List<JobSchedulerBase> returnList = sqlStandardsDao.loadSchedulerByManager(jobSchedulerBase);
		
		for (JobSchedulerBase jobScheduler : returnList) {
			try {
				if (StringUtil.isEmpty(jobScheduler.getSvn_os_user_password()) == false) {
					jobScheduler.setSvn_os_user_password(
							CryptoUtil.decryptAES128(jobScheduler.getSvn_os_user_password(), key));
				}
			} catch (Exception e) {
				if (StringUtils.isEmpty(jobScheduler.getSvn_os_user_password()) == false) {
					jobScheduler.setSvn_os_user_password("복호화하는 과정에서 에러가 발생하였습니다.");
				}
			}
		}
		
		return returnList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadNonStdSqlList(SQLStandards sqlStandards) throws Exception {
		return sqlStandardsDao.loadNonStdSqlList(sqlStandards);
	}
	
	@Override
	public int saveSetting(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		int resultCount = 0;
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + " 23:59:59");
		
		String svn_os_user_password = jobSchedulerConfigDetail.getSvn_os_user_password();
		
		if (StringUtil.isEmpty(svn_os_user_password) == false) {
			String Encrypted = CryptoUtil.encryptAES128(svn_os_user_password, key);
			jobSchedulerConfigDetail.setSvn_os_user_password(Encrypted);
		}
		
		resultCount += sqlStandardsDao.saveSetting(jobSchedulerConfigDetail);
		
		getDate(jobSchedulerConfigDetail);
		
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());
		resultCount += commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		
		return resultCount;
	}

	@Override
	public int modifySetting(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		int resultCount = 0;
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + " 23:59:59");
		
		String svn_os_user_password = jobSchedulerConfigDetail.getSvn_os_user_password();
		
		if (StringUtil.isEmpty(svn_os_user_password) == false) {
			String Encrypted = CryptoUtil.encryptAES128(svn_os_user_password, key);
			jobSchedulerConfigDetail.setSvn_os_user_password(Encrypted);
		}
		
		resultCount += sqlStandardsDao.modifySetting(jobSchedulerConfigDetail);
		
		getDate(jobSchedulerConfigDetail);
		
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());
		resultCount += commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		
		return resultCount;
	}

	@Override
	public int deleteScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		int resultCount = 0;
		
		resultCount += sqlStandardsDao.deleteScheduler(jobSchedulerConfigDetail);
		
		jobSchedulerConfigDetail.setExec_cycle(" ");
		jobSchedulerConfigDetail.setUse_yn("N");
		jobSchedulerConfigDetail.setExec_start_dt(null);
		jobSchedulerConfigDetail.setExec_end_dt(null);
		
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());
		resultCount += commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		
		return resultCount;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception {
		if ("selectAll".equals(jobSchedulerBase.getProject_id())) {
			jobSchedulerBase.setProject_id("");
		}
		
		List<LinkedHashMap<String, Object>> returnList = sqlStandardsDao.excelDownload(jobSchedulerBase);
		removeHtmlChar(returnList);
		
		Object objCd = new Object();
		String svn_if_meth_cd = "";
		for (LinkedHashMap<String, Object> map : returnList) {
			objCd = map.get("SVN_IF_METH_CD");
			if (objCd != null) {
				svn_if_meth_cd = (String) objCd;
				
				if ("1".equals(svn_if_meth_cd)) {
					map.put("SVN_IF_METH_CD", "SFTP");
					
				} else if ("2".equals(svn_if_meth_cd)) {
					map.put("SVN_IF_METH_CD", "FTP");
					
				} else if ("3".equals(svn_if_meth_cd)) {
					map.put("SVN_IF_METH_CD", "로컬디렉토리(NAS형태)");
				} else {
				}
				objCd = null;
			}
		}
		
		return returnList;
	}
	
	private void getDate(JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String nowTime = StringUtil.replace(DateUtil.getNextTime("P", "0"), ":", "");
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(nowDate, nowTime));
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
	
	private void removeHtmlChar(List<LinkedHashMap<String, Object>> returnList) {
		Object valueStr = null;
		
		for (LinkedHashMap<String, Object> map : returnList) {
			String[] keyArr = map.keySet().toArray(new String[map.size()]);
			for (String keyStr : keyArr) {
				valueStr = map.get(keyStr);
				
				if (valueStr != null) {
					map.put(keyStr, StringUtil.removeSpecialChar(valueStr.toString()));
				}
			}
		}
	}
	
	@Override
	public boolean isUpdateY(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws ParseException {
		String execCycle = jobSchedulerConfigDetail.getExec_cycle();
		String endTime = jobSchedulerConfigDetail.getExec_end_dt().replaceAll("-","");
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Date now = new Date();
		
		String customTime = "";
		String strNow = sf.format(now);
		
		long int_Endtime = Long.parseLong(endTime);
		long int_now = Long.parseLong(strNow);
		
		if(int_Endtime == int_now) {
			customTime = new SimpleDateFormat("HHmmss").format(now);
			
		}else {
			if(int_Endtime < int_now) {
				return false;
			}
			customTime = "235959";
		}
		
		endTime = endTime + customTime;
		Date date_EndTime = sf2.parse(endTime);
		
		//스케줄러 종료일이 현재보다 과거일 경우
		CronExpression exp = new CronExpression(execCycle);
		Date afterDate = exp.getNextValidTimeAfter(now);
		
		//date == null => 현재 시간을 포함하여 스케줄링일정에 걸릴 시간이 없다는것.
		if(afterDate == null) {
			return false;
			
		}else if(afterDate.compareTo(date_EndTime) > 0) {
			// date.compareTo(date_EndTime) > 0 인경우
			// 종료일시와 스케줄러 예정일이 같은 날이면 금일 수행 건이므로 true
			// 다른 날이면 금일 수행건이 아니므로 false
			if(sf.format(afterDate).equals(sf.format(date_EndTime))) {
				return true;
			}
			return false;
			
		}else if(afterDate.compareTo(date_EndTime) == 0 ) {
			// date.compareTo(date_EndTime) == 0 인경우 => 스케줄러 예정일시가 종료일시와 같으므로 false
			return false;
		}
		return true;
	}
}