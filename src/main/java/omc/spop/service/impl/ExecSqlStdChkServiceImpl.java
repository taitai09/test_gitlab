package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.ExecSqlStdChkDao;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.service.ExecSqlStdChkService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;
import oracle.sql.CLOB;

@Service("ExecSqlStdChkService")
public class ExecSqlStdChkServiceImpl implements ExecSqlStdChkService {
	private final Logger logger = LoggerFactory.getLogger(ExecSqlStdChkServiceImpl.class);

	@Autowired
	private ExecSqlStdChkDao execSqlStdChkDao;
	
	@Autowired
	private SQLStandardsDao sqlStandardsDao;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	private String key = "openmade";
	
	@Override
	public List<JobSchedulerBase> loadSchedulerByManager(JobSchedulerBase jobSchedulerBase) {
		
		return execSqlStdChkDao.loadSchedulerByManager(jobSchedulerBase);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception {
		
		return execSqlStdChkDao.schedulerExcelDownload(jobSchedulerBase);
	}
	
	@Override
	public int checkExistScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		
		return execSqlStdChkDao.checkExistScheduler(jobSchedulerConfigDetail);
	}
	
	@Override
	public int insertSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		int resultCount = 0;
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + " 23:59:59");
		
		resultCount += sqlStandardsDao.insertSqlStdQtyScheduler(jobSchedulerConfigDetail);
		
		getDate(jobSchedulerConfigDetail);
		
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());
		resultCount += commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		
		return resultCount;
	}
	
	@Override
	public int updateSqlStdQtyScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		int resultCount = 0;
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + " 23:59:59");
		
		resultCount += sqlStandardsDao.updateSqlStdQtyScheduler(jobSchedulerConfigDetail);
		
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
	public List<JobSchedulerBase> loadSqlStdChkRslt(SQLStandards sqlStandards) {
		
		return execSqlStdChkDao.loadSqlStdChkRslt(sqlStandards);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadQualityStatus(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			String qty_chk_sql = selectRule("026", sqlStandards, false);
			parseAndCondition(sqlStandards, qty_chk_sql);
			
			dataList.addAll(sqlStandardsDao.loadQualityTable(sqlStandards));
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return dataList;
	}
	
	@Override
	public List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception {
		String project_id = sqlStandards.getProject_id();

		return execSqlStdChkDao.loadQtyIdxByProject(project_id);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadResultCnt(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			String qty_chk_sql = selectRule("029", sqlStandards, false);
			parseAndCondition(sqlStandards, qty_chk_sql);
			
			// 페이징 처리를 피하기 위해 엑셀다운로드 함수 사용
			dataList.addAll(sqlStandardsDao.excelDownQualityTable(sqlStandards));
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return dataList;
	}
	
	@Override
	public List<JobSchedulerBase> loadSchedulerList(Map<String, String> param) throws Exception {
		return execSqlStdChkDao.loadSchedulerList(param);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadSqlFullText(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = execSqlStdChkDao.loadSqlFullText(sqlStandards);
		String key = "PROGRAM_SOURCE_DESC";
		
		if( resultList.size() > 0 ) {
			LinkedHashMap<String, Object> map = resultList.get(0);
			CLOB clob = (oracle.sql.CLOB) map.get(key);
			String strValue = clob.stringValue();
			
			map.put(key, strValue);
		}
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadNonStdSql(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			String qty_chk_sql = selectRule("030", sqlStandards, false);
			parseAndCondition(sqlStandards, qty_chk_sql);
			
			dataList.addAll(sqlStandardsDao.loadQualityTable(sqlStandards));
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return dataList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadStdComplianceState(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			String qty_chk_sql = selectRule("027", sqlStandards, false);
			parseAndCondition(sqlStandards, qty_chk_sql);
			
			// 페이징 처리를 피하기 위해 엑셀다운로드 함수 사용
			dataList.addAll(sqlStandardsDao.excelDownQualityTable(sqlStandards));
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return dataList;
	}
	
	
	@Override
	public List<LinkedHashMap<String, Object>> loadCountByIndex(SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			String qty_chk_sql = selectRule("028", sqlStandards, false);
			parseAndCondition(sqlStandards, qty_chk_sql);
			
			// 페이징 처리를 피하기 위해 엑셀다운로드 함수 사용
			dataList.addAll(sqlStandardsDao.excelDownQualityTable(sqlStandards));
			
		}catch(Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return dataList;
	}
	
	@Override
	public boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sql = "";
		
		try {
			String projectId = sqlStandards.getProject_id();
			List<SQLStandards> codeList = execSqlStdChkDao.loadQtyIdxByProject(projectId);
			
			handler.open();
			
			handler.buildInit("SQL_품질검토_작업_집계표", "EXEC_SQL_STD_CHK_RESULT_001");
			handler.addKoHeaders( getCodeListByProject(codeList, "ko", 1) );
			handler.addEnHeaders( getCodeListByProject(codeList, "en", 1) );
			
			sql = selectRule("029", sqlStandards, true);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.buildInit("표준_미준수_SQL", "EXEC_SQL_STD_CHK_RESULT_002");
			handler.addKoHeaders( getCodeListByProject(codeList, "ko", 2) );
			handler.addEnHeaders( getCodeListByProject(codeList, "en", 2) );
			
			sql = selectRule("030", sqlStandards, true);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.buildInit("표준_미준수_개발자_집계표", "EXEC_SQL_STD_CHK_RESULT_003");
			
			sql = selectRule("025", sqlStandards, true);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getName();
			logger.error("{} Excel Build Error {}", methodName, e);
			
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		return resultSuccess;
	}
	
	private String selectRule(
			String qtyChekIdtCd, SQLStandards sqlStandards, boolean excelYN) throws Exception {
		
		List<String> qtyChkIdtCdList = new ArrayList<String>();
		List<SQLStandards> dataQtyChkSql = Collections.emptyList();
		SQLStandards SQLStandardsRow = null;
		
		String qty_chk_sql = null;
		
		try {
			qtyChkIdtCdList.add(qtyChekIdtCd);
			
			dataQtyChkSql = sqlStandardsDao.getQtyChkSQL_List(qtyChkIdtCdList);
			SQLStandardsRow = dataQtyChkSql.get(0);
			
			qty_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getQty_chk_sql(), key);
			
			if( excelYN ) {
				qty_chk_sql = qty_chk_sql.replace("#{project_id}", "'" + sqlStandards.getProject_id() + "'");
				qty_chk_sql = qty_chk_sql.replace("#{sql_std_qty_div_cd}", "'" + sqlStandards.getSql_std_qty_div_cd() + "'");
				qty_chk_sql = qty_chk_sql.replace("#{sql_std_qty_scheduler_no}", "'" + sqlStandards.getSql_std_qty_scheduler_no() + "'");
				qty_chk_sql = qty_chk_sql.replace("#{sql_std_gather_day}", "'" + sqlStandards.getSql_std_gather_day() + "'");
				qty_chk_sql = qty_chk_sql.replace("#{qty_chk_idt_cd}", "'" + sqlStandards.getQty_chk_idt_cd() + "'");
				qty_chk_sql = qty_chk_sql.replace("#{sql_id}", "'" + sqlStandards.getSql_id() + "'");
				
				if(sqlStandards.getWrkjob_cd() == null) {
					qty_chk_sql = qty_chk_sql.replace("AND NVL(C.WRKJOB_CD, ' ') LIKE '%'||#{wrkjob_cd}||'%'", "")
							.replace("AND D.WRKJOB_CD(+) LIKE '%'||#{wrkjob_cd}||'%'", "")
							.replace("AND B.WRKJOB_CD(+) LIKE '%'||#{wrkjob_cd}||'%'", "");
					
				}else {
					qty_chk_sql = qty_chk_sql.replace("#{wrkjob_cd}", "'" + sqlStandards.getWrkjob_cd() + "'");
				}
			}
			
		} catch (Exception e) {
			logger.error("예외발생 ==> ", e);
		
		} finally {
			qtyChkIdtCdList = null;
			dataQtyChkSql = null;
		}
		
		return qty_chk_sql;
	}
	
	@Override
	public List<SQLStandards> loadExceptionList(SQLStandards sqlStandards) {
		
		return execSqlStdChkDao.loadExceptionList(sqlStandards);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> exceptionExcelDown(SQLStandards sqlStandards) {
		
		return execSqlStdChkDao.loadExceptionExcelDownload(sqlStandards);
	}
	
	@Override
	public List<SQLStandards> getQtyChkIdxCd(SQLStandards sqlStandards) {
		
		return sqlStandardsDao.getQtyChkIdtCdFromException(sqlStandards);
	}
	
	@Override
	public Result saveException(SQLStandards sqlStandards) throws Exception {
		Result result = new Result();
		int resultValue = -1;
		
		try {
			int existCnt = execSqlStdChkDao.countException(sqlStandards);
			
			if (existCnt > 0) {
				result.setResult(false);
				result.setMessage("입력한 예외 대상 정보는 SQL 표준 점검 예외 대상 관리에 등록되어 있습니다.");
				
			} else {
				resultValue = execSqlStdChkDao.saveException(sqlStandards);
				
				if (resultValue > 0) {
					result.setResult(true);
					result.setMessage("예외 대상 정보가 등록되었습니다.");
					
				} else {
					result.setResult(false);
					result.setMessage("예외 대상 정보 등록에 실패하였습니다.");
				}
			}
			
		} catch (Exception ex) {
			logger.error("예외발생 ==> ", ex);
		}
		
		return result;
	}
	
	@Override
	public Result modifyException(SQLStandards sqlStandards) throws Exception {
		Result result = new Result();
		int resultValue = -1;
		
		try {
			resultValue = execSqlStdChkDao.saveException(sqlStandards);
			
			if (resultValue > 0) {
				result.setResult(true);
				result.setMessage("예외 대상 정보가 수정되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("예외 대상 정보 수정에 실패하였습니다.");
			}
		} catch (Exception ex) {
			logger.error("예외발생 ==> ", ex);
			
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public int deleteException(SQLStandards sqlStandards) throws Exception {
		
		return sqlStandardsDao.deleteMaintainQualityCheckException(sqlStandards);
	}
	
	@Override
	public Result excelUpload(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		String filePath = "";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);
			
		} catch (Exception ex) {
			logger.error("엑셀 파일 업로드 error => ", ex);
			throw ex;
		}
		
		option.setFilePath(filePath);
		option.setStartRow(2);
		
		int errCnt = 0;
		int totalCnt = 0;
		int errIndex = 1; // 엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		boolean errOccur = false;
		StringBuffer errMsgSb = new StringBuffer();;
		Map<String, Object> errObj = new HashMap<String, Object>(); // 최종 err 정보를 담을 obj
		List<SQLStandards> errList = new ArrayList<SQLStandards>();
		Vector<Integer> errIndexs = new Vector<Integer>(); // err 로우를 담을 obj
		SQLStandards sqlStandards = null;
		
		try {
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();
			
			for (Map<String, String> article : excelContent) {
				errIndex += 1;
				
				sqlStandards = new SQLStandards();
				sqlStandards.setQty_chk_idt_cd(StringUtils.defaultString(article.get("A")));
				sqlStandards.setDbid(StringUtils.defaultString(article.get("B")));
				sqlStandards.setWrkjob_cd(StringUtils.defaultString(article.get("C")));
				sqlStandards.setDir_nm(StringUtils.defaultString(article.get("D")));
				sqlStandards.setDbio(StringUtils.defaultString(article.get("E")));
				sqlStandards.setSql_id(StringUtils.defaultString(article.get("F")));
				sqlStandards.setExcept_sbst(StringUtils.defaultString(article.get("G")));
				sqlStandards.setRequester(StringUtils.defaultString(article.get("H")));
				sqlStandards.setUser_id(user_id);
				
				int check = byteChecker(sqlStandards);
				logger.debug("check:" + check);
				
				if (check > 0) {
					errOccur = true;
					
					logger.trace("################# 에러발생시 ################# ");
					logger.trace(article.get("A") +"\t" + article.get("B") + "\t" + article.get("C") + "\t" +
								 article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t" + 
								 article.get("G") + "\t" + article.get("H") + "\t");
					
					errList.add(sqlStandards);
					errIndexs.add(errIndex);
					
					errMsgSb.append("입력한 필드 값 중에 한계치를 벗어났습니다. 아래의 필드값과 필드 한계치를 확인해 주세요.");
					errMsgSb.append("\n품질점검지표코드 " + article.get("A").getBytes().length + "Byte / ");
					errMsgSb.append("업무코드 " + article.get("C").getBytes().length + "Byte / ");
					errMsgSb.append("디렉토리명 " + article.get("D").getBytes().length + "Byte / ");
					errMsgSb.append("DBIO " + article.get("E").getBytes().length + "Byte / ");
					errMsgSb.append("SQL ID " + article.get("F").getBytes().length + "Byte / ");
					errMsgSb.append("예외사유 " + article.get("G").getBytes().length + "Byte / ");
					errMsgSb.append("요청자 " + article.get("H").getBytes().length + "Byte");
					errMsgSb.append("\n품질점검지표코드 10Byte / 업무코드 5Byte / 디렉토리명 400Byte / DBIO 4000Byte / SQL ID 13Byte /예외사유 100Byte / 요청자 1000Byte");
					
					errCnt += 1;
					
				} else if (check == -1) {
					String dbid = sqlStandards.getDbid();
					String sqlId = sqlStandards.getSql_id();
					String msg = "";
					
					if ( "".equals(dbid) && "".equals(sqlId) ) {
						errOccur = true;
						msg = "[품질 점검 지표 코드]는 필수로 입력되어야 하며, [업무 코드], [디렉토리명], [SQL 식별자(DBIO)] 등 이 셋 중의 하나를 입력하거나, [표준점검DB], [SQL ID]가입력되어야 합니다.";
						
					}else if( ( !"".equals(dbid) && "".equals(sqlId) ) ||
							  ( "".equals(dbid) && !"".equals(sqlId) ) ) {
						errOccur = true;
						msg = "[품질 점검 지표 코드]는 필수로 입력되어야 하며, [표준점검DB] 입력 시 반드시 [SQL ID]가 입력되어야 합니다.";
					}
					
					if( errOccur ) {
						logger.trace("################# 에러발생시 ################# ");
						logger.trace(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
								+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t");
						
						errList.add(sqlStandards);
						errIndexs.add(errIndex);
						errMsgSb.append(msg);
						errCnt += 1;
					}
					
				}
				
				if( errOccur == false ) {
					execSqlStdChkDao.saveException(sqlStandards);
				}
				
			}
			excelContent = null;
			
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errList", errList);
			errObj.put("errIndex", errIndexs.toString());
			
			String errMsg = new String(errMsgSb);
			errMsgSb = null;
			
			if ( errMsg != null && errMsg.isEmpty() == false ) {
				errObj.put("errMsg", errMsg);
			}
			
			result.setObject(errObj);
			result.setMessage("success");
			result.setResult(true);
			
		} catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => ", ex);
			
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errList", errList);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errIndex", errIndexs.toString());
			
			result.setObject(errObj);
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ " + totalCnt + " ] 건 중  [ " + (errIndex - 2) + " ] 건 성공 <BR/>" +
							"[ " + errIndex + " ] 번째 행에서 에러가 발생하였습니다.<BR/>" +
							"[ 품질점검 지표코드 : " + sqlStandards.getQty_chk_idt_cd() + ", " +
							"표준점검DB : " + sqlStandards.getDbid() + ", " +
							"업무코드 : " + sqlStandards.getWrkjob_cd() + ", " +
							"SQL 식별자(DBIO) : " + sqlStandards.getDbio() +
							"SQL ID : " + sqlStandards.getSql_id() + " ]<BR/> " + ex.getMessage());
			result.setResult(false);
		
		}finally {
			fileMng = null;
			option = null;
			filePath = null;
			errObj = null;
			errList = null;
		}
		
		return result;
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
	
	private void parseAndCondition(SQLStandards sqlStandards, String qty_chk_sql) throws Exception {
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
			
			sqlStandards.setQty_chk_sql(sb.toString());
		} catch (Exception ex) {
			logger.error("parseAndCondition error:", ex);
			throw ex;
		}
		
		logger.debug("final query...");
		logger.debug(sb.toString());
		logger.debug("..............");
	}
	
	private String pastAndCaseWrkjob_cd(String preColumnName, String wrkjob_cd) {
		return "\r\n" + "-- 업무코드가 입력되면\r\n" + "AND " + preColumnName + " IN ( SELECT WRKJOB_CD\r\n"
				+ "                FROM WRKJOB_CD\r\n" + "                START WITH WRKJOB_CD =  '" + wrkjob_cd
				+ "'\r\n" + "                CONNECT BY PRIOR WRKJOB_CD = UPPER_WRKJOB_CD )";
	}
	
	private String pastAndCaseDbio(String dbio) {
		return "\r\n" + "-- SQL 식별자(DBIO)가 입력되면\r\n" + "AND UPPER(A.DBIO) LIKE '%'||UPPER('" + dbio + "')||'%'";
	}
	
	private int byteChecker(SQLStandards sqlStandards) {
		int check = 0;
		
		if ("".equals(sqlStandards.getQty_chk_idt_cd().trim()) || ("".equals(sqlStandards.getWrkjob_cd().trim())
				&& "".equals(sqlStandards.getDbio().trim()) && "".equals(sqlStandards.getDir_nm().trim()))) {
			
			check = -1;
			
		} else {
			check += sqlStandards.getQty_chk_idt_cd().getBytes().length <= 10 ? 0 : 1;
			check += sqlStandards.getWrkjob_cd().getBytes().length <= 5 ? 0 : 1;
			check += sqlStandards.getDir_nm().getBytes().length <= 400 ? 0 : 1;
			check += sqlStandards.getDbio().getBytes().length <= 4000 ? 0 : 1;
			check += sqlStandards.getSql_id().getBytes().length <= 13 ? 0 : 1;
			check += sqlStandards.getExcept_sbst().getBytes().length <= 100 ? 0 : 1;
			check += sqlStandards.getRequester().getBytes().length <= 1000 ? 0 : 1;
		}
		
		return check;
	}
}