package omc.spop.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openpop.xml.parser.Parser;

import omc.spop.dao.ProjectSqlQtyChkRuleMngDao;
import omc.spop.dao.SQLDiagnosisReportDao;
import omc.spop.dao.SqlStandardOperationPlugInDao;
import omc.spop.model.Database;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.model.Result;
import omc.spop.model.SqlStandardOperationPlugIn;
import omc.spop.model.SqlStandardOperationPlugInResponse;
import omc.spop.model.WrkJobCd;
import omc.spop.server.tune.CxClient;
import omc.spop.server.tune.InspectSqlCodeTask;
import omc.spop.server.tune.SelfSQLStdQtyExplainPlan;
import omc.spop.service.SqlStandardOperationPlugInService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.DateUtil;
import omc.spop.utils.SystemUtil;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("SqlStandardOperationPlugInService")
public class SqlStandardOperationPlugInServiceImpl implements SqlStandardOperationPlugInService {
	private static final Logger logger = LoggerFactory.getLogger(SqlStandardOperationPlugInServiceImpl.class);
	private String key = "openmade";
	
	@Autowired
	private SqlStandardOperationPlugInDao sqlStandardOperationPlugInDao;
	
	@Autowired
	private ProjectSqlQtyChkRuleMngDao projectSqlQtyChkRuleMngDao;
	
	@Value("#{defaultConfig['sql_cnt_list']}")
	private String sqlCntList;
	
	@Value("#{defaultConfig['ci_agent_ip']}")
	private String ciAgentIp;
	
	@Value("#{defaultConfig['ci_agent_port']}")
	private String ciAgentPort;
	
	@Value("#{defaultConfig['send_msg_sleep_time']}")
	private String sendMsgSleepTime;
	
	@Value("#{defaultConfig['send_msg_sql_cnt']}")
	private String send_msg_sql_cnt;
	
	@Value("#{defaultConfig['number_compare']}")
	private String number_compare;

	@Value("#{defaultConfig['number_compare_wait_time']}")
	private String number_compare_wait_time;

	@Value("#{defaultConfig['more_then_retry_numbers']}")
	private String more_then_retry_numbers;

	@Autowired
	private SQLDiagnosisReportDao sqlDiagnosisReportDao;
	
	private HashMap<String, String> forceCompletionMap = new HashMap<String, String>();
	
	private final int KEY_SQL_STD_QTY_DIV_CD_BATCH_EXEC	= 4;	/* 실행 기반 SQL 일괄 */
	
	/**
	 * Error Trace :
	 */
	private final String ERROR_TRACE = "Error Trace :";
	
	private Map<String, Integer> pipscMap = new LinkedHashMap<String, Integer>();
	
	private JSONObject getFirst(SqlStandardOperationPlugIn boxModel) throws Exception {
		logger.trace("Start getFirst");
		LinkedList<JSONObject> jsonList = null;
		JSONObject obj = null;
		
		try {
			jsonList = boxModel.getJson_list();
			obj = jsonList.getFirst();
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		logger.trace("end of getFirst");
		return obj;
	}
	
	private boolean checkFirstFile(SqlStandardOperationPlugIn boxModel) throws Exception {
		logger.trace("Start checkFirstFile");
		boolean flag = true;
		JSONObject obj = null;
		
		try {
			obj = getFirst(boxModel);
			
			if(obj.containsKey("first_time") == false || Integer.parseInt(obj.get("first_time") + "") > 0) {
				logger.info("checkFirstTime >> This model is not first_time");
				return false;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return flag;
	}
	
	private boolean checkIsLast(SqlStandardOperationPlugIn boxModel) throws Exception {
		logger.trace("Start checkIsLast");
		boolean flag = true;
		JSONObject obj = null;
		
		try {
			obj = getLast(boxModel);
			
			if(Boolean.parseBoolean(obj.get("is_last") + "") == false) {
				logger.info("checkIsLast >> This model is not the last file");
				return false;
			}
			
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return flag;
	}
	
	private JSONObject getLast(SqlStandardOperationPlugIn boxModel) throws Exception {
		logger.trace("Start getFirst");
		LinkedList<JSONObject> jsonList = null;
		JSONObject obj = null;
		
		try {
			jsonList = boxModel.getJson_list();
			obj = jsonList.getLast();
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		logger.trace("end of getLast");
		return obj;
	}
	
	private boolean checkFirstTimeExecuteSql(SqlStandardOperationPlugIn model) throws Exception {
		logger.trace("Start checkFirstTimeExecuteSql");
		boolean flag = false;
		Result result = new Result();
		
		result = firstTimeModuleBaseExec(model);
		
		if(Integer.parseInt(result.getStatus()) != 11) {
			return flag;
		}
		
		logger.trace("end of checkFirstTimeExecuteSql");
		return true;
	}
	
	private Result checkAgentStatusResult(JSONObject obj) throws Exception {
		logger.trace("Start checkAgentStatusResult");
		final String CI_AGENT_STR = "//CI Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
		Result result = new Result();
		boolean flag = false;
		String project_id = "";
		String sql_std_qty_chkt_id = "";
		String sql_std_qty_scheduler_no = "";
		String exceptionMsg = "";
		
		try {
			project_id = obj.get("project_id") + "";
			sql_std_qty_chkt_id = obj.get("sql_std_qty_chkt_id") + "";
			sql_std_qty_scheduler_no = obj.get("sql_std_qty_scheduler_no") + "";
		
			if(checkCiAgent(project_id, sql_std_qty_chkt_id, sql_std_qty_scheduler_no) == true) {
				flag = true;
			} else {
				exceptionMsg = CI_AGENT_STR;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			exceptionMsg = e.getMessage();
		}
		
		logger.debug("checkAgentStatusResult >> CI Agent status >>> " + flag);
		
		if(Integer.parseInt(obj.get("count_create_plan") + "") == 0) {
			logger.info("** [" + SystemUtil.getMethodName() + "]");
			logger.info("** Not Check MASTER Agent");
			logger.info("********************");
			
			if(flag == false) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent). Will be goning soon.");
				logger.info("********************");
				
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error(exceptionMsg);
				logger.error("********************");
				result.setResult(false);
				result.setStatus("4");
				result.setMessage(exceptionMsg);
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
			
			result.setResult(false);
			result.setStatus("5");
			result.setMessage("Agent 체크 정상");
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
			return result;
		}
		
		try {
			if(checkMasterAgent(project_id, sql_std_qty_chkt_id, sql_std_qty_scheduler_no) == true) {
				logger.debug("checkAgentStatusResult >> Equipment OK.");
				flag = true;
			} else {
				flag = false;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			if(exceptionMsg.length() > 0) {
				exceptionMsg = "//CI Agent와 MASTER Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent and MASTER Agent). Will be going soon.");
				logger.info("********************");
				
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error(exceptionMsg);
				logger.error("********************");
				result.setResult(false);
				result.setStatus("4");
				result.setMessage(exceptionMsg);
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			} else {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(MASTER Agent). Will be going soon.");
				logger.info("********************");
				result.setResult(false);
				result.setStatus("4");
				result.setMessage(e.getMessage());
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
		}
		
		if(exceptionMsg.length() > 0) {
			logger.info("** [" + SystemUtil.getMethodName() + "]");
			logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent)");
			logger.info("********************");
			
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(exceptionMsg);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("4");
			result.setMessage(exceptionMsg);
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
			return result;
		}
		logger.trace("end of checkAgentStatusResult");
		result.setResult(false);
		result.setStatus("5");
		result.setMessage("Agent 체크 정상");
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result checkAgentStatusExecuteSqlResult(SqlStandardOperationPlugIn model) throws Exception {
		logger.trace("Start checkAgentStatusExecuteSqlResult");
		final String CI_AGENT_STR = "//CI Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
		Result result = new Result();
		boolean flag = false;
		String project_id = "";
		String sql_std_qty_chkt_id = "";
		String sql_std_qty_scheduler_no = "";
		String exceptionMsg = "";
		
		try {
			project_id = model.getProject_id();
			sql_std_qty_chkt_id = model.getSql_std_qty_chkt_id();
			sql_std_qty_scheduler_no = model.getSql_std_qty_scheduler_no();
		
			if(checkCiAgent(project_id, sql_std_qty_chkt_id, sql_std_qty_scheduler_no) == true) {
				flag = true;
			} else {
				exceptionMsg = CI_AGENT_STR;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			exceptionMsg = e.getMessage();
		}
		
		logger.debug("checkAgentStatusExecuteSqlResult >> CI Agent status >>> " + flag);
		
		if(Integer.parseInt(model.getCount_create_plan()) == 0) {
			logger.info("** [" + SystemUtil.getMethodName() + "]");
			logger.info("** Not Check MASTER Agent");
			logger.info("********************");
			
			if(flag == false) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent). Will be goning soon.");
				logger.info("********************");
				
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error(exceptionMsg);
				logger.error("********************");
				result.setMessage(exceptionMsg);
				result.setResult(false);
				result.setStatus("4");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
			
			result.setResult(false);
			result.setStatus("5");
			result.setMessage("Agent 체크 정상");
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
			return result;
		}
		
		try {
			if(checkMasterAgent(project_id, sql_std_qty_chkt_id, sql_std_qty_scheduler_no) == true) {
				logger.debug("checkAgentStatusExecuteSqlResult >> Equipment OK.");
				flag = true;
			} else {
				flag = false;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			if(exceptionMsg.length() > 0) {
				exceptionMsg = "//CI Agent와 MASTER Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent and MASTER Agent). Will be going soon.");
				logger.info("********************");
				
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error(exceptionMsg);
				logger.error("********************");
				result.setResult(false);
				result.setStatus("4");
				result.setMessage(exceptionMsg);
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			} else {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(MASTER Agent). Will be going soon.");
				logger.info("********************");
//				logger.error(e.getMessage());
				result.setResult(false);
				result.setStatus("4");
				result.setMessage(e.getMessage());
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
		}
		
		if(exceptionMsg.length() > 0) {
			logger.info("** [" + SystemUtil.getMethodName() + "]");
			logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent)");
			logger.info("********************");
			
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(exceptionMsg);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("4");
			result.setMessage(exceptionMsg);
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
			return result;
		}
		logger.info("end of checkAgentStatusExecuteSqlResult");
		result.setResult(false);
		result.setStatus("5");
		result.setMessage("Agent 체크 정상");
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private boolean checkCiAgent(String project_id, String sql_std_qty_chkt_id,
			String sql_std_qty_scheduler_no) throws Exception {
		logger.trace("Start checkCiAgent");
		boolean flag = true;
		final String CI_AGENT_STR = "//CI Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
		long aliveSleepTime = 30L;
		long threadSleepTime = 1000L;
		int delayCnt = 0;
		
		try {
			putMsg("alive");
			Thread.sleep(aliveSleepTime);
			putMsg("alive");
			Thread.sleep(aliveSleepTime);
			
			while(delayCnt++ < 3) {
				Thread.sleep(threadSleepTime);
				
				if(CxClient.get().isConnected() == false) {
					flag = false;
					break;
				}
			}
		} catch(Exception e) {
			flag = false;
			logger.error(CI_AGENT_STR + "\nproject_id[" + project_id + 
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id +
					"] sql_std_qty_scheduler_no[" + sql_std_qty_scheduler_no + "]");
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			throw new Exception(CI_AGENT_STR);
		}
		logger.trace("end of checkCiAgent");
		return flag;
	}
	
	private boolean checkMasterAgent(String project_id, String sql_std_qty_chkt_id,
			String sql_std_qty_scheduler_no) throws Exception {
		logger.trace("Start checkMasterAgent");
		boolean flag = false;
		final String CI_AGENT_STR = "//MASTER Agent 연결 오류입니다. Open-POP 시스템 담당자에게 문의 바랍니다.";
		JSONObject jsonMaster = new JSONObject();
		HashMap<String, Object> resultMap = null;
		
		try {
			jsonMaster.put("telegram_code", "PING");
			
			resultMap = createPlan(jsonMaster);
			if("false".equals(resultMap.get("is_error")) &&
					"Complete".equals(resultMap.get("err_msg"))) {
				flag = true;
			}
		} catch(Exception e) {
			logger.error(CI_AGENT_STR + "\nproject_id[" + project_id + 
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id +
					"] sql_std_qty_scheduler_no[" + sql_std_qty_scheduler_no + "]");
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			throw new Exception(CI_AGENT_STR);
		}
		logger.trace("end of checkMasterAgent");
		return flag;
	}
	
	private Result checkStatusInfo(Result result, SqlStandardOperationPlugIn model) throws Exception {
		int status = -1;
		String message = "";
		SqlStandardOperationPlugIn forceModel = null;
		
		try {
			status = Integer.parseInt(result.getStatus());
			logger.info("** checkStatusInfo status [" + status + "] ******************************");
			if(status == 0) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** Request next file.");
				logger.info("********************");
			} else if(status == 1) {
				insertErrTable(model);
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.info("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.info("********************");
				
				model.setSql_std_qty_status_cd("10");
				sqlStandardOperationPlugInDao.updateSelfsqlStdQtyPlanExecComplete(model);
				result.setResultCount(1);
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Complete Update.");
				logger.info("** Normal termination.");
				logger.info("********************");
			} else if(status == 2) {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				
				forceModel = new SqlStandardOperationPlugIn();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				forceModel.setSql_std_qty_status_cd("03");
				
				/* SQL-4: 강제완료처리 */
				updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Register compulsion complete.");
				logger.error("** Abnormal termination.");
				logger.error("********************");
			} else if(status == 3) {
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** This file is registered as compulsory completion.");
				logger.error("** Abnormal termination.");
				logger.error("********************");
			} else if(status == 4) {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				
				forceModel = new SqlStandardOperationPlugIn();
				message = result.getMessage();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				
				if(message.indexOf("CI Agent") > 0 && message.indexOf("MASTER Agent") > 0) {
					forceModel.setStd_qty_agent_status_cd("03");
					
					updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
					result.setResultCount(1);
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent and MASTER Agent)");
					logger.error("********************");
				} else if(message.indexOf("CI Agent") > 0) {
					forceModel.setStd_qty_agent_status_cd("01");
					
					updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
					result.setResultCount(1);
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(CI Agent)");
					logger.error("********************");
				} else if(message.indexOf("MASTER Agent") > 0) {
					forceModel.setStd_qty_agent_status_cd("02");
					
					updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
					result.setResultCount(1);
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(MASTER Agent)");
					logger.info("********************");
				} else {
					forceModel.setStd_qty_agent_status_cd("99");
					
					updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
					result.setResultCount(1);
					// 프로그램 에러
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** An error occurred in the program on the agent side.");
					logger.error("** Check the message :\n" + message);
					logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(Program error)");
					logger.error("********************");
				}
				
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Agent status error.");
				logger.error("** Abnormal termination.");
				logger.error("********************");
			} else if(status == 5) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** Agent status Normal.");
				logger.info("********************");
			} else if(status == 6) {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				
				forceModel = new SqlStandardOperationPlugIn();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				forceModel.setStd_qty_agent_status_cd("99");
				
				updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(Program error)");
				logger.error("** AbNormal termination.");
				logger.error("********************");
			} else if(status == 7) {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				
				forceModel = new SqlStandardOperationPlugIn();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				forceModel.setStd_qty_agent_status_cd("02");
				
				updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Master agent status error.");
				logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Force Out Update.(MASTER Agent)");
				logger.error("** AbNormal termination.");
				logger.error("********************");
			} else if(status == 8) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** Retry count exceeded (SQL Count Comparison).");
				logger.info("** Request next quality check.");
				logger.info("********************");
			} else if(status == 9) {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				forceModel = new SqlStandardOperationPlugIn();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				forceModel.setStd_qty_agent_status_cd("06");
				
				updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Retry count exceeded (SQL Count Comparison)");
				logger.error("** Last SQL order.");
				logger.error("** AbNormal termination.");
				logger.error("********************");
			} else if(status == 10) {
				forceModel = new SqlStandardOperationPlugIn();
				
				forceModel.setSql_std_qty_chkt_id(model.getSql_std_qty_chkt_id());
				forceModel.setSql_std_qty_status_cd("10");
				
				updateSelfsqlStdQtyPlanExecForceComplete(forceModel);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Data does not exist in the SELFSQL_STD_QTY_CHK_PGM table.");
				logger.error("** SELFSQL_STD_QTY_PLAN_EXEC Table Complete Update.");
				logger.error("** Normal termination.");
				logger.info("********************");
			} else {
				insertErrTable(model);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** SQL_STD_QTY_CHK_ERR Table Complete Update.");
				logger.error("** SQL_STD_QTY_CHK_ERR_SUM Table Complete Update.");
				logger.error("********************");
				
				model.setStd_qty_agent_status_cd("99");
				
				updateSelfsqlStdQtyPlanExecForceComplete(model);
				result.setResultCount(1);
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** Unknown result Status [" + status +"]");
				logger.error("** AbNormal termination.");
				logger.error("********************");
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		} finally {
			if(result.getResultCount() == 1) {
				delPipscMap(model.getProject_id(), model.getSql_std_qty_chkt_id());
			}
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private void startCxClient() throws Exception {
		int port = 0;
		
		try {
			if(ciAgentPort != null && ciAgentPort != "") {
				port = Integer.parseInt(ciAgentPort);
			}
			
			CxClient.get().thrdStart(ciAgentIp, port);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			throw new Exception(e);
		}
	}
	
	private boolean checkFileError(JSONObject obj) throws Exception {
		boolean flag = false;
		
		try {
			if(obj.containsKey("err_msg") == true && 
					(obj.get("err_msg") + "").startsWith("F100") == true || (obj.get("err_msg") + "").startsWith("F101") == true &&
					obj.get("program_nm").toString().length() == 0) {
				flag = true;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			
			throw new Exception(e);
		}
		
		return flag;
	}
	
	private Result insertSelfsqlStdQtyFileError(JSONObject obj) {
		Result result = new Result();
		SqlStandardOperationPlugIn model = new SqlStandardOperationPlugIn();
		
		try {
			model.setSql_std_qty_chkt_id(obj.get("sql_std_qty_chkt_id").toString());
			model.setSql_std_qty_error_seq(Integer.parseInt(obj.get("sql_std_qty_error_seq").toString()));
			model.setAbs_dir_nm(obj.get("abs_dir_nm").toString());
			model.setDir_nm(obj.get("dir_nm").toString());
			model.setFile_nm(obj.get("file_nm").toString());
			model.setErr_sbst(obj.get("err_msg").toString());
			
			sqlStandardOperationPlugInDao.insertSelfsqlStdQtyFileError(model);
			
			result.setResult(true);
			result.setStatus("0");
			result.setMessage("다음 파일 요청");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result checkSqlQty(String sql_std_qty_div_cd, String sql_std_qty_chkt_id,
			String project_id, String sql_std_qty_scheduler_no, String parser_code, 
			int count_create_plan, long dbid, String db_user) throws NullPointerException, Exception {
		return checkSqlQty(sql_std_qty_div_cd, sql_std_qty_chkt_id,
				project_id, sql_std_qty_scheduler_no, parser_code, 
				count_create_plan, dbid, db_user, "0");
	}
	
	private Result checkSqlQty(String sql_std_qty_div_cd, String sql_std_qty_chkt_id,
			String project_id, String sql_std_qty_scheduler_no, String parser_code, 
			int count_create_plan, long dbid, String db_user, String file_cnt) throws NullPointerException, Exception {
		Result result = new Result();
		SqlStandardOperationPlugIn model = new SqlStandardOperationPlugIn();
		int resultCount = -1;
		
		try {
			model.setDbid(String.valueOf(dbid));
			
			model.setSql_std_qty_chkt_id(sql_std_qty_chkt_id);
			
			resultCount = sqlStandardOperationPlugInDao.selectCountSelfSqlStdQtyChkPgm(model);
			
			model.setSql_cnt(resultCount);
			model.setFile_cnt(file_cnt);
			model.setSql_std_qty_status_cd("02");
			
			sqlStandardOperationPlugInDao.updateSelfsqlStdQtyPlanExecStatus(model);
			
			addPipscMap(project_id, sql_std_qty_chkt_id, 0);
			
			if(resultCount == 0) {
				logger.info("** [" + SystemUtil.getMethodName() + "]");
				logger.info("** Data does not exist in the SELFSQL_STD_QTY_CHK_PGM table.");
				logger.info("** SELFSQL_STD_QTY_PLAN_EXEC Table Complete Update. Will be going soon.");
				logger.info("********************");
				
				result.setResult(false);
				result.setStatus("10");
				result.setMessage("Data does not exist in the SELFSQL_STD_QTY_CHK_PGM table.");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
			
			result = checkSqlQtyDetail(resultCount, project_id, sql_std_qty_chkt_id, sql_std_qty_div_cd, parser_code, count_create_plan,
					dbid, sql_std_qty_scheduler_no, db_user);
		} catch(NullPointerException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage(e.getMessage());
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage(e.getMessage());
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result checkSqlQtyDetail(int resultCount, String project_id, String sql_std_qty_chkt_id, 
			String sql_std_qty_div_cd, String parser_code, int count_create_plan, 
			long dbid, String sql_std_qty_scheduler_no, String db_user) throws Exception {
		Result result = new Result();
		float count = 50;
		float startNum = 1;
		float endNum = 0;
		int group = -1;
		HashMap<String, String> wrkJobCdMap = null;
		ArrayList<SqlStandardOperationPlugIn> batchList = null;
		HashMap<String ,String> map = null;
		int first_time = 0;
		List<SqlStandardOperationPlugIn> packageList = null;
		int packageListSize = -1;
		SqlStandardOperationPlugIn model = null;
		StringBuffer createPlanSb = null;
		boolean isCreatePlan = false;
		int updateCnt = -1;
		JSONObject jsonMaster = null;
		HashMap resultMap = null;
		int status = -1;
		boolean isBreak = false;
		long send_msg_sleep_time_l = Long.valueOf(this.sendMsgSleepTime);
		int send_msg_sql_cnt_i = Integer.parseInt(this.send_msg_sql_cnt);
		
		try {
			if( (count_create_plan > 0 && ("1".equals(sql_std_qty_div_cd) == true ||
					"2".equals(sql_std_qty_div_cd) == true || "4".equals(sql_std_qty_div_cd) == true )) ) {
				isCreatePlan = true;
			}
			
			wrkJobCdMap = getWrkJobCdList();
			count = Integer.parseInt(this.sqlCntList);
			endNum = count;
			
			//50개씩 그룹으로 묶음. 
			group = (int) Math.ceil(resultCount/count);
			logger.debug("checkSqlQtyDetail >> group[" + group + "] resultCount[" + resultCount + "] count[" + count + "]");
			
			for(int iter = 0 ; iter < group ; iter++) {
				batchList = new ArrayList<SqlStandardOperationPlugIn>();
				map = new HashMap<String ,String>();
				
				map.put("sql_std_qty_chkt_id", sql_std_qty_chkt_id);
				map.put("from_sql_std_qty_program_seq", String.valueOf(startNum));
				map.put("to_sql_std_qty_program_seq", String.valueOf(endNum));
				
				packageList = sqlDiagnosisReportDao.selectSelfSqlStdQtyChkPgm(map);
				packageListSize = packageList.size();
				
				createPlanSb = new StringBuffer();
				
				logger.debug("checkSqlQtyDetail >> packageListSize[" + packageListSize + "]");
				for(int idx = 0; idx < packageListSize; idx++) {
					model = packageList.get(idx);
					
					if("3".equals(sql_std_qty_div_cd) == false) {
						if(checkRegisteredForceOut(project_id, sql_std_qty_chkt_id) == 1) {
							isBreak = true;
							result.setResult(false);
							result.setStatus("3");
							result.setMessage("강제 완료로 등록된 정보");
							
							break;
						}
					}
					
					if(isCreatePlan == true) {
						createPlanSb.append(model.getSql_std_qty_program_seq());
						
						if(idx < packageListSize - 1) {
							createPlanSb.append(",");
						}
					}
					
					model = makeReplenishData(model, first_time, resultCount, sql_std_qty_div_cd, 
							parser_code, wrkJobCdMap, dbid);
					
					batchList.add(model);
					
					first_time++;
				}
				
				if(isBreak == true) {
					logger.debug("checkSqlQtyDetail > 강제 완료 진행 중");
					break;
				}
				
				if(isCreatePlan == true) {
					jsonMaster = new JSONObject();
					resultMap = new HashMap();
					
					if(createPlanSb.length() == 0) {
						logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.error("** createPlanSb Size is Zero.");
						logger.error("********************");
						result.setResult(false);
						result.setStatus("6");
						result.setMessage("프로그램 오류");
						
						break;
					}
					
					jsonMaster.put("telegram_code", "INSPECT_SQL_CODE_TASK");
					jsonMaster.put("dbid", dbid);
					jsonMaster.put("sql_std_qty_chkt_id", sql_std_qty_chkt_id);
					jsonMaster.put("sql_std_qty_program_seq", createPlanSb.toString());
					jsonMaster.put("schema", db_user);
					jsonMaster.put("sql_std_qty_scheduler_no", sql_std_qty_scheduler_no);
					jsonMaster.put("sql_std_qty_div_cd", sql_std_qty_div_cd);
					
					resultMap = createPlan100(jsonMaster);
					
					String KILL_INSPECT = "KILL_INSPECT";
					
					if(resultMap.containsKey("is_error") == true && "false".equals(resultMap.get("is_error")) &&
							resultMap.containsKey("err_msg") == true && KILL_INSPECT.equals(resultMap.get("err_msg")) == true) {
						logger.info("checkSqlQtyDetail > createPlan >> MASTER >>> is_error[" + resultMap.get("is_error") + 
								"] err_msg[" +  resultMap.get("err_msg") + "]");
						logger.info("checkSqlQtyDetail >> Ending the program with a forced completion message from the MASTER.");
						
						result.setResult(false);
						result.setStatus("3");
						result.setMessage("강제완료료 등록된 파일");
						
						break;
					}
					
					if(resultMap.containsKey("is_error") == true && (Boolean) "true".equals(resultMap.get("is_error"))) {
						logger.info("** [" + SystemUtil.getMethodName() + "]");
						logger.info("** MASTER Agent Error");
						logger.info("********************");
						
						result.setResult(false);
						result.setStatus("7");
						result.setMessage("MASTER Agent 오류");
						
						break;
					}
				}
				
				startNum = startNum + count;
				endNum = endNum + count;
				
				if("3".equals(sql_std_qty_div_cd) == true || "4".equals(sql_std_qty_div_cd) == true) {
					updateCnt = sqlStandardOperationPlugInDao.updateSelfsqlStdQtyChkPgmForeach(batchList);
				}
				
				result = sendCi(packageList, resultMap, send_msg_sql_cnt_i, send_msg_sleep_time_l);
				
				result = checkSelfsqlStdQtyPlanExecResult(sql_std_qty_chkt_id, Integer.parseInt(result.getStatus()),
						Integer.parseInt(model.getSql_std_qty_program_seq()), model.getProject_id());
				
				status = Integer.parseInt(result.getStatus());
				
				if(status == 1 || status == 6 || status == 7 || status == 9 || status == 10)  {
					break;
				}
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage(e.getMessage());
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private SqlStandardOperationPlugIn makeReplenishData(SqlStandardOperationPlugIn model, 
			int first_time, int resultCount, String sql_std_qty_div_cd, String parser_code,
			HashMap<String, String> wrkJobCdMap, long dbid) throws Exception {
		
		try {
			model.setFirst_time(first_time);
			logger.trace("makeReplenishData > resultCount[" + resultCount + "] first_time[" + first_time + "]");
			if(resultCount == first_time + 1) {
				model.setIs_last(true);
			}else {
				model.setIs_last(false);
			}
			
			model.setSql_std_qty_div_cd(sql_std_qty_div_cd);
			model.setParser_code(parser_code);
			model.setDbid(String.valueOf(dbid));
			
			model = updateSelfsqlStdQtyChkPgmModel(model, wrkJobCdMap);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return model;
	}
	
	private HashMap<String, Object> createPlan100(JSONObject jsonMaster) throws NullPointerException, Exception {
		logger.trace("Start createPlan");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		long beforeTime = System.currentTimeMillis();
		JSONArray jsonArrayRes = null;
		JSONObject jsonObjRes = null;
		int errCnt = 0;
		
		try {
			/* Call Rule 100 */
			long beforeCreatePlanTime = System.currentTimeMillis();
			jsonArrayRes = InspectSqlCodeTask.createPlan(jsonMaster);
			logger.trace("createPlan100 >> used time [" + (System.currentTimeMillis() - beforeCreatePlanTime) / 1000.0 + "]");
			logger.trace("createPlan100 >> jsonArrayRes >> \n" + jsonArrayRes.toJSONString());
			
			jsonObjRes = (JSONObject) jsonArrayRes.get(0);
			String KILL_INSPECT = "KILL_INSPECT";
			
			if(jsonObjRes.containsKey("is_error") == true && "false".equals(jsonObjRes.get("is_error")) &&
					jsonObjRes.containsKey("err_msg") == true && KILL_INSPECT.equals(jsonObjRes.get("err_msg")) == true) {
				/* 강제 완료 */
				logger.debug("createPlan100 >> Force Out.");
				resultMap.put("is_error", jsonObjRes.get("is_error"));
				resultMap.put("err_msg", jsonObjRes.get("err_msg"));
			} else if(jsonObjRes.containsKey("is_error") == true && "false".equals(jsonObjRes.get("is_error")) &&
					jsonObjRes.containsKey("err_msg") == true && "Complete".equals(jsonObjRes.get("err_msg"))) {
				/* Master Alive(PING 요청) */
				logger.debug("createPlan100 >> Master Alive?");
				resultMap.put("is_error", jsonObjRes.get("is_error"));
				resultMap.put("err_msg", jsonObjRes.get("err_msg"));
			} else {
				errCnt = Integer.parseInt(jsonObjRes.get("err_cnt") + "");
				resultMap.put("err_cnt", errCnt);
				logger.debug("createPlan100 >> errCnt["+ errCnt + "]");
				
				if(errCnt > 0) {
					resultMap = updatePlan100((JSONArray) jsonArrayRes.get(1));
				}
			}
		} catch(IOException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** IOException >>> ", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} catch(NullPointerException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** NullPointerException >>> ", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception >>> ", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		}
		logger.trace("createPlan100 >> resultMap [ " + resultMap.toString() + " ]");
		logger.trace("end of createPlan100 [" + (System.currentTimeMillis() - beforeTime) / 1000.0 + "]");
		return resultMap;
	}
	
	private Result sendCi(List<SqlStandardOperationPlugIn> packageList, HashMap resultMap,
			int send_msg_sql_cnt_i, long send_msg_sleep_time_l) throws Exception {
		Result result = new Result();
		int packageListSize = -1;
		SqlStandardOperationPlugIn model = null;
		JSONObject jsonObj = null;
		String project_id = "";
		String sql_std_qty_div_cd = "";
		String sql_std_qty_program_seq = "";
		boolean is_last = true;
		
		try {
			packageListSize = packageList.size();
			
			for(int idx = 0; idx < packageListSize; idx++) {
				jsonObj = new JSONObject();
				
				model = packageList.get(idx);
				
				sql_std_qty_div_cd = model.getSql_std_qty_div_cd();
				sql_std_qty_program_seq = model.getSql_std_qty_program_seq();
				project_id = model.getProject_id();
				is_last = model.isIs_last();
				
				if(resultMap != null && resultMap.get("err_cnt") != null && Integer.parseInt(resultMap.get("err_cnt") + "") > 0 &&
						resultMap.containsKey(sql_std_qty_program_seq) == true) {
					jsonObj.put("err_msg", resultMap.get("err_msg"));
				}
				
				jsonObj.put("sql_std_qty_chkt_id", model.getSql_std_qty_chkt_id());
				jsonObj.put("sql_std_qty_program_seq", sql_std_qty_program_seq);
				jsonObj.put("sql_std_qty_div_cd", sql_std_qty_div_cd);
				jsonObj.put("parser_code", model.getParser_code());
				jsonObj.put("dbid", Long.parseLong(model.getDbid()));
				jsonObj.put("project_id", project_id);
				jsonObj.put("program_source_desc", model.getProgram_source_desc());
				
				logger.debug("sendCi > putMsg >> sql_std_qty_program_seq[" + sql_std_qty_program_seq +  "] is_last[" + is_last + 
						"] project_id[" + project_id + "] sql_std_qty_div_cd[" + sql_std_qty_div_cd + "]");
				
				putMsg(sql_std_qty_div_cd, jsonObj.toString());
				
				if(idx > 0 && (idx % send_msg_sql_cnt_i) == 0) {
					logger.trace("sendCi >> wait time[" + send_msg_sleep_time_l + "] ms idx[" + idx + 
							"] send_msg_sql_cnt_i[" + send_msg_sql_cnt_i + "]");
					Thread.sleep(send_msg_sleep_time_l);
				}
			}
			
			if(is_last == true) {
				result.setResult(false);
				result.setStatus("1");
				result.setMessage("마지막 파일");
			} else {
				result.setResult(true);
				result.setStatus("0");
				result.setMessage("다음 파일 요청");
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setStatus("6");
			result.setMessage("sendCi >> Exception >> " + e.getMessage());
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result checkSelfsqlStdQtyPlanExecResult(String sql_std_qty_chkt_id, int status,
			int sql_std_qty_program_seq, String project_id) throws Exception {
		Result result = new Result();
		SqlStandardOperationPlugIn model = null;
		int sc = -1;			// sql_cnt
		int ipsc = -1;			// in_progress_sql_cnt
		long ctwt = -1L;		// ci_total_work_time
								/*************************************************************
								 * ctwt / ipsc = CI 현재까지 처리한 1건(SQL)의 평균 속도(ms)를 구할 수 있다.
								 * Thread는 10개 가동 중
								 * 최악의 속도로 1건당 200ms를 처리한다고 보면 1분에 5건을 처리하고
								 * T가 10개이므로 종합적으로 1분에 50건을 처리한다는 초기 모드로 잡는다.
								 *************************************************************/
		String projectId = "";
		String sqlStdQtyDivCd = "";
		int numberCompare = -1;
		long numberCompareWaitTime = -1L;
		int moreThenRetryNumbers = -1;
		int retryNumber = 0;
		Result resultCompare = new Result();
		int resultStatus = -1;
		String resultTxtValue =  "";
		int in_pipsc = 0;			// 이전 in_progress_sql_cnt
		Result resultPipscMap = null;
		
		try {
			numberCompare = Integer.parseInt(number_compare);
			numberCompareWaitTime = Long.parseLong(number_compare_wait_time);
			moreThenRetryNumbers = Integer.parseInt(more_then_retry_numbers);
			
			while(true) {
				model = new SqlStandardOperationPlugIn();
				
				model.setSql_std_qty_chkt_id(sql_std_qty_chkt_id);
				
				model = sqlStandardOperationPlugInDao.selectSelfsqlStdQtyPlanExecCnt(model);
				sc = model.getSql_cnt();
				ipsc = model.getIn_progress_sql_cnt();
				ctwt = model.getCi_total_work_time();
				projectId = model.getProject_id();
				sqlStdQtyDivCd = model.getSql_std_qty_div_cd();
				
				if(status == 1) {
					/* 마지막 SQL */
					if(sc == ipsc) {
						logger.info("** [" + SystemUtil.getMethodName() + "]");
						logger.info("** Last SQL >>> SQL_CNT and IN_PROGRESS_SQL_CNT are the same. sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
						logger.info("********************");
						result.setResult(true);
						result.setStatus("1");
						result.setMessage("SQL_CNT and IN_PROGRESS_SQL_CNT are the same. sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
						
						break;
					}
					
					logger.trace("checkSelfsqlStdQtyPlanExec >> Last SQL >>> CI Agent processing status.");
				}
				
				resultPipscMap = getPipscMap(project_id, sql_std_qty_chkt_id);
				
				if(Integer.parseInt(resultPipscMap.getStatus()) != 0) {
					logger.error("** An error occurred [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]\n" 
							+ resultPipscMap.toString());
					result.setResult(resultPipscMap.getResult());
					result.setStatus(resultPipscMap.getStatus());
					result.setMessage(resultPipscMap.getMessage());
					
					break;
				}
				
				in_pipsc = Integer.parseInt(resultPipscMap.getObject() + "");
				
				resultCompare = compareSqlCountResult(retryNumber, ipsc, in_pipsc, numberCompare, ctwt,
						numberCompareWaitTime, moreThenRetryNumbers, status, projectId, sqlStdQtyDivCd,
						sql_std_qty_program_seq, sql_std_qty_chkt_id);
				
				resultStatus = Integer.parseInt(resultCompare.getStatus());
				
				if(resultStatus == 8) {
					logger.info("** [" + SystemUtil.getMethodName() + "]");
					logger.info("** Retry Count Exceeded (SQL Count Comparison).");
					logger.info("********************");
					result.setResult(false);
					result.setStatus("8");
					result.setMessage("SQL 건수 비교 재시도 초과 (다음 품질 점검 진행)");
					setPipscMap(project_id, sql_std_qty_chkt_id, ipsc);
					
					break;
				} else if(resultStatus == 9) {
					logger.info("** [" + SystemUtil.getMethodName() + "]");
					logger.info("** Last SQL >>> Retry Count Exceeded (SQL Count Comparison). sql_std_qty_chkt_id [" +sql_std_qty_chkt_id + "]");
					logger.info("********************");
					
					result.setResult(false);
					result.setStatus("9");
					result.setMessage("SQL 건수 비교 재시도 초과 (마지막 SQL로 프로그램 종료). sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
					setPipscMap(project_id, sql_std_qty_chkt_id, ipsc);
					
					break;
				} else if(resultStatus == 0 || resultStatus == 1) {
					resultTxtValue = resultCompare.getTxtValue();
					
					if(resultTxtValue.equals("retry") == true) {
						retryNumber++;
					} else if(resultTxtValue.equals("continue") == true) {
						retryNumber = 0;
					} else if(resultTxtValue.equals("break") == true) {
						logger.info("** [" + SystemUtil.getMethodName() + "]");
						logger.info("** The sql_std_qty_program_seq value and the ipsc value are the same.");
						logger.info("********************");
						result.setResult(false);
						result.setStatus("0");
						result.setMessage("다음 품질 점검 진행");
						setPipscMap(project_id, sql_std_qty_chkt_id, ipsc);
						
						break;
					}
				}
				
				setPipscMap(project_id, sql_std_qty_chkt_id, ipsc);
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result addPipscMap(String project_id, String sql_std_qty_chkt_id, Integer pipsc) throws Exception {
		Result result = new Result();
		
		try {
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() +
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] pipsc[" + pipsc + "] project_id[" + project_id + "]");
			if(this.pipscMap.containsKey(sql_std_qty_chkt_id) == false) {
				this.pipscMap.put(sql_std_qty_chkt_id, pipsc);
				
				result.setResult(true);
				result.setStatus("0");
			} else {
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
				logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
				logger.error("********************");
				result.setResult(false);
				result.setStatus("6");
				result.setMessage("프로그램 오류");
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
			logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.debug("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] pipscMap :\n" + this.pipscMap.toString());
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result getPipscMap(String project_id, String sql_std_qty_chkt_id) throws Exception {
		Result result = new Result();
		Integer pipsc = -1;
		
		try {
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() +
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] pipsc[" + pipsc + "] project_id[" + project_id + "]");
			if(this.pipscMap.containsKey(sql_std_qty_chkt_id) == false) {
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
				logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
				logger.error("********************");
				result.setResult(false);
				result.setStatus("6");
				result.setMessage("프로그램 오류");
			} else {
				pipsc = this.pipscMap.get(sql_std_qty_chkt_id);
				
				result.setResult(true);
				result.setStatus("0");
				result.setObject(pipsc);
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
			logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.debug("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] pipscMap :\n" + this.pipscMap.toString());
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result setPipscMap(String project_id, String sql_std_qty_chkt_id, Integer pipsc) throws Exception {
		Result result = new Result();
		
		try {
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() +
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] pipsc[" + pipsc + "] project_id[" + project_id + "]");
			if(this.pipscMap.containsKey(sql_std_qty_chkt_id) == false) {
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
				logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
				logger.error("********************");
				result.setResult(false);
				result.setStatus("6");
				result.setMessage("프로그램 오류");
			} else {
				this.pipscMap.put(sql_std_qty_chkt_id, pipsc);
				
				result.setResult(true);
				result.setStatus("0");
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
			logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.debug("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] pipscMap :\n" + this.pipscMap.toString());
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private Result delPipscMap(String project_id, String sql_std_qty_chkt_id) throws Exception {
		Result result = new Result();
		
		try {
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() +
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
			if(this.pipscMap.containsKey(sql_std_qty_chkt_id) == false) {
				logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
				logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
				logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
				logger.error("********************");
				result.setResult(false);
				result.setStatus("6");
				result.setMessage("프로그램 오류");
			} else {
				this.pipscMap.remove(project_id);
				
				result.setResult(true);
				result.setStatus("0");
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]");
			logger.error("** pipscMap Error. pipcsMap :\n" + this.pipscMap.toString());
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.debug("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] pipscMap :\n" + this.pipscMap.toString());
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	/**
	 * 
	 * @param retryNumber
	 * @param ipsc
	 * @param pipsc
	 * @param numberCompare
	 * @param ctwt
	 * @param numberCompareWaitTime
	 * @param moreThenRetryNumbers
	 * @param status
	 * @param projectId
	 * @param sqlStdQtyDivCd
	 * @param sql_std_qty_program_seq
	 * @return status , txtValue : break / retry / continue
	 * @throws Exception
	 */
	private Result compareSqlCountResult(int retryNumber, int ipsc, int pipsc, int numberCompare, long ctwt,
			long numberCompareWaitTime, long moreThenRetryNumbers, int status, String projectId, String sqlStdQtyDivCd,
			int sql_std_qty_program_seq, String sql_std_qty_chkt_id) throws Exception {
		Result result = new Result();
		int in = -1;		// 증가된 개수 = 현재 IN_PROGRESS_SQL_CNT - 이전 IN_PROGRESS_SQL_CNT;
		long avrgTimeAnalyzedCi = 0L;	// Current average time analyzed for sql (1 case) in ci_agent
		int gapNum = 0;
		long avrgTimeAnalyzedWait = 0L;
		
		try {
			logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.debug("** compareSqlCount > sqlStdQtyDivCd[" + sqlStdQtyDivCd + "] projectId[" + projectId +
					"] status[" + status + "]\nipsc[" + ipsc + "] pipsc[" + pipsc + "] ctwt[" + ctwt + 
					"] retryNumber[" + retryNumber + "] numberCompareWaitTime[" + numberCompareWaitTime + 
					"] moreThenRetryNumbers[" + moreThenRetryNumbers + "] sql_std_qty_program_seq[" + sql_std_qty_program_seq + 
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "]");
			logger.debug("********************");
			if(moreThenRetryNumbers == retryNumber) {
				if(status == 1) {
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** Last sql > Exceeded countercheck. sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
					logger.error("********************");
					result.setStatus("9");
				} else {
					logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.debug("** State in progress > Exceeded countercheck.");
					result.setStatus("8");
				}
			} else {
				if(sql_std_qty_program_seq == ipsc) {
					logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.debug("** State in progress > The sql_std_qty_program_seq value and the ipsc value are the same. sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
					result.setStatus(String.valueOf(status));
					result.setTxtValue("break");
				} else {
					in = ipsc - pipsc;
					
					if(ctwt == 0) {
						avrgTimeAnalyzedWait = numberCompareWaitTime;
					} else if(in == 0) {
						avrgTimeAnalyzedCi = ctwt / ipsc;
						avrgTimeAnalyzedWait = avrgTimeAnalyzedCi;
					} else {
						gapNum = in - numberCompare;
						avrgTimeAnalyzedCi = ctwt / ipsc;
						avrgTimeAnalyzedWait = gapNum * avrgTimeAnalyzedCi;
					}
					
					if(avrgTimeAnalyzedWait < numberCompareWaitTime) {
						logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.debug("** correct wait time to numberCompareWaitTime.");
						avrgTimeAnalyzedWait = numberCompareWaitTime;
					}
					
					if(pipsc == ipsc) {
						logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.debug("** The pipsc value and ipsc value are the same. (Increase the number of retries.) sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
						logger.debug("** Wait time [" + avrgTimeAnalyzedWait + "] ms");
						result.setStatus(String.valueOf(status));
						result.setTxtValue("retry");
						Thread.sleep(avrgTimeAnalyzedWait);
					} else {
						logger.trace("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.trace("** CI Agent is checking (increasing condition) sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
						result.setStatus(String.valueOf(status));
						result.setTxtValue("continue");
						if(in > numberCompare) {
							logger.debug("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
							logger.debug("The number of CI's to check exceeded the num number. sql_std_qty_chkt_id [" + sql_std_qty_chkt_id + "]");
							logger.debug("** Wait time [" + avrgTimeAnalyzedWait + "] ms");
							Thread.sleep(avrgTimeAnalyzedWait);
						}
					}
				}
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private boolean insertErrTable(SqlStandardOperationPlugIn model) throws Exception {
		boolean flag = false;
		
		try {
			if("2".equals(model.getSql_std_qty_div_cd()) == true ||
					"4".equals(model.getSql_std_qty_div_cd()) == true) {
				// SQL표준품질점검오류내역 Insert - RULE 003
				insertSqlStdQtyChkErr(model.getSql_std_qty_chkt_id());
				
				// SQL표준품질점검오류집계 Insert - RULE 004
				insertSqlStdQtyChkErrSum(model.getProject_id(), model.getSql_std_qty_div_cd(), model.getSql_std_qty_chkt_id(), model.getSql_std_qty_scheduler_no());
				
				flag = true;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return flag;
	}
	
	/**
	 * SQL표준품질점검오류내역 Insert
	 * @param project_id
	 */
	private void insertSqlStdQtyChkErr(String sql_std_qty_chkt_id) {
		logger.info("Start insertSqlStdQtyChkErr");
		SqlStandardOperationPlugIn model	= new SqlStandardOperationPlugIn();
		String qty_chk_idt_cd_err								= "003";
		String qty_chk_sql										= "";
		int update												= -1;
		
		try {
			model.setQty_chk_idt_cd(qty_chk_idt_cd_err);
			
			model = sqlStandardOperationPlugInDao.selectInsertSQL(model);
			qty_chk_sql = model.getQty_chk_sql();
			qty_chk_sql = CryptoUtil.decryptAES128(qty_chk_sql, key);
			logger.trace("** [" + SystemUtil.getMethodName() + "]");
			logger.trace(qty_chk_sql);
			logger.trace("********************");
			model.setQty_chk_sql(qty_chk_sql);
			model.setSql_std_qty_chkt_id(sql_std_qty_chkt_id);
			
			update = sqlStandardOperationPlugInDao.insertSqlStdQtyChkErr(model);
			logger.trace("insertSqlStdQtyChkErr update[" + update + "]");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception :", e);
			logger.error("********************");
			model.setQty_chk_sql("insertSqlStdQtyChkErr > 복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.");
			e.printStackTrace();
		}
		logger.info("end of insertSqlStdQtyChkErr");
	}
	
	/**
	 * SQL표준품질점검오류집계 Insert - RULE 004
	 * @param project_id
	 */
	private void insertSqlStdQtyChkErrSum(String project_id, String sql_std_qty_div_cd,
			String sql_std_qty_chkt_id, String sql_std_qty_scheduler_no) {
		logger.info("Start insertSqlStdQtyChkErrSum");
		SqlStandardOperationPlugIn model	= new SqlStandardOperationPlugIn();
		String qty_chk_idt_cd_err_sum							= "004";
		String qty_chk_sql										= "";
		int update												= -1;
		
		try {
			model.setQty_chk_idt_cd(qty_chk_idt_cd_err_sum);
			
			model = sqlStandardOperationPlugInDao.selectInsertSQL(model);
			qty_chk_sql = model.getQty_chk_sql();
			qty_chk_sql = CryptoUtil.decryptAES128(qty_chk_sql, key);
			logger.trace("** [" + SystemUtil.getMethodName() + "]");
			logger.trace(qty_chk_sql);
			logger.trace("********************");
			
			model.setQty_chk_sql(qty_chk_sql);
			model.setProject_id(project_id);
			model.setSql_std_qty_scheduler_no(sql_std_qty_scheduler_no);
			model.setSql_std_qty_div_cd(sql_std_qty_div_cd);
			model.setSql_std_qty_chkt_id(sql_std_qty_chkt_id);
			
			update = sqlStandardOperationPlugInDao.insertSqlStdQtyChkErrSum(model);
			logger.trace("insertSqlStdQtyChkErrSum update[" + update + "]");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception :", e);
			logger.error("********************");
			model.setQty_chk_sql("insertSqlStdQtyChkErrSum > 복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.");
			e.printStackTrace();
		}
		logger.info("end of insertSqlStdQtyChkErrSum");
	}
	
	/* MyBatis batchForeach */
	private void batchInsertForeachModel(LinkedList<JSONObject> jsonList, String sql_std_qty_chkt_id) throws Exception {
		logger.trace("Start batchInsertForeachModel sql_std_qty_chkt_id[ " + sql_std_qty_chkt_id + "]");
		int jsonListSize = 0;
		JSONObject jsonObj = null;
		long beforeLoopTime = 0L;
		SqlStandardOperationPlugIn model = null;
		ArrayList<SqlStandardOperationPlugIn> batchList = new ArrayList<SqlStandardOperationPlugIn>();
		
		try {
			beforeLoopTime = System.currentTimeMillis();
			jsonListSize = jsonList.size();
			
			for(int jsonIdx = 0; jsonIdx < jsonListSize; jsonIdx++) {
				jsonObj = jsonList.get(jsonIdx);
				model = makeModelfromJSONObject(jsonObj);
				
				batchList.add(model);
			}
			
			batchInsertForeach(batchList);
			logger.trace("batchInsertForeachModel [" + (System.currentTimeMillis() - beforeLoopTime) / 1000.0 + "]");
		} catch(SQLException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** SQLException occured >>>", e);
			logger.error("********************");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception occured >>>", e);
			logger.error("********************");
		}
		logger.trace("end of batchInsertForeachModel sql_std_qty_chkt_id[ " + sql_std_qty_chkt_id + "]");
		return;
	}
	
	private void batchInsertForeach(List<SqlStandardOperationPlugIn> modelList) throws Exception {
		logger.trace("Start batchInsertForeach");
		long startTime = System.currentTimeMillis();
		
		try {
			int count = sqlStandardOperationPlugInDao.insertSelfsqlStdQtyChkPgmForeach(modelList);
			logger.trace("batchInsertForeach >> count[" + count + "]");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception occured :", e);
			logger.error("********************");
			
			throw new Exception(e);
		}
		logger.trace("end of batchInsertForeach >> used time [" + (System.currentTimeMillis() - startTime) / 1000 + "](ms)");
	}
	
	private void updatePlan100Foreach(List<SqlStandardOperationPlugIn> modelList) throws Exception {
		logger.trace("Start updatePlan100Foreach");
		long startTime = System.currentTimeMillis();
		
		try {
			int count = sqlStandardOperationPlugInDao.updateSelfsqlStdQtyChkPgmErrMsgForeach(modelList);
			logger.trace("updatePlan100Foreach >> count[" + count + "]");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception occured :", e);
			logger.error("********************");
			
			throw new Exception(e);
		}
		logger.trace("end of updatePlan100Foreach >> used time [" + (System.currentTimeMillis() - startTime) / 1000 + "](ms)");
	}
	
	private SqlStandardOperationPlugIn setGatherTermDay(SqlStandardOperationPlugIn model) {
		String yesterday = "";
		
		try {
			if("3".equals(model.getSql_std_qty_div_cd()) ||
					"4".equals(model.getSql_std_qty_div_cd())) {
				if("1".equals(model.getGather_term_type_cd())) {
					yesterday = sqlStandardOperationPlugInDao.selectYesterday(model);
					
					if("1".equals(model.getGather_range_div_cd())) {
						model.setGather_term_start_day(yesterday);
					} else if("2".equals(model.getGather_range_div_cd())) {
						model.setGather_term_start_day(sqlStandardOperationPlugInDao.selectWeekAgo(model));
					} else if("3".equals(model.getGather_range_div_cd())) {
						model.setGather_term_start_day(sqlStandardOperationPlugInDao.selectMonthAgo(model));
					} else if("4".equals(model.getGather_range_div_cd())) {
						model.setGather_term_start_day(sqlStandardOperationPlugInDao.selectQuarterAgo(model));
					} else if("5".equals(model.getGather_range_div_cd())) {
						model.setGather_term_start_day(sqlStandardOperationPlugInDao.selectOneYearAgo(model));
					}
					
					model.setGather_term_end_day(yesterday);
				}
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return model;
	}
	
	/**
	 * 기존 로직 변경
	 * 스케쥴러에서 delete, insert 작업이 선행된다.
	 * @param model
	 * @return
	 */
	private Result firstTimeModuleBaseExec(SqlStandardOperationPlugIn model) {
		logger.info("Start firstTimeModuleBaseExec");
		Result result = new Result();
		
		try {
			model = setGatherTermDay(model);
			
			if("3".equals(model.getSql_std_qty_div_cd()) || "4".equals(model.getSql_std_qty_div_cd())) {
				long beforeInsertSelectPlanExecTime = System.currentTimeMillis();
				List<String> ownerStrList = new ArrayList<String>();
				List<String> moduleStrList = new ArrayList<String>();
				int updateCnt = -1;
				
				if(model.getOwner_list().indexOf(',') != -1 ) {
					String[] strOwnerArr = model.getOwner_list().split(",");
					
					for ( String ownerStr : strOwnerArr ) {
						if ( ownerStr.trim().equals("") ) {
							result.setResult(false);
							result.setStatus("6");
							result.setMessage("OWNER 값을 확인 후 다시 성능비교 를 실행해 주세요.");
							logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
							return result;
						} else {
							ownerStrList.add( ownerStr.trim() );
						}
					}
				}
				
				model.setStrOwnerList(ownerStrList);
				
				if (model.getModule_list().indexOf(',') != -1 ) {
					String[] strModuleArr = model.getModule_list().split(",");
					
					for ( String moduleStr : strModuleArr ) {
						if ( moduleStr.trim().equals("") ) {
							result.setResult(false);
							result.setStatus("6");
							result.setMessage("MODULE 값을 확인 후 다시 성능비교 를 실행해 주세요.");
							logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
							return result;
						} else {
							moduleStrList.add( moduleStr.trim() );
						}
					}
				}
				
				model.setStrModuleList( moduleStrList );
				if("1".equals(model.getSql_source_type_cd()) == true) {
					try {
						/* AWR(TOP) */
						updateCnt = sqlStandardOperationPlugInDao.insertSelectSelfsqlStdQtyChkPgmForAWR(model);
					} catch(Exception e) {
						logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.error("** insert AWR model :\n" + model.toString());
						logger.error("** insert AWR Exception occured :", e);
						logger.error("********************");
						
						throw new Exception(e);
					}
				} else if("2".equals(model.getSql_source_type_cd()) == true) {
					try {
						/* AWR(TOP) */
						/* 전체SQL(Cursor Cache) */
						updateCnt = sqlStandardOperationPlugInDao.insertSelectSelfsqlStdQtyChkPgmForVSQL(model);
					} catch(Exception e) {
						logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
						logger.error("** insert VSQL model :\n" + model.toString());
						logger.error("** insert VSQL Exception occured :", e);
						logger.error("********************");
						
						throw new Exception(e);
					}
				}
				logger.trace("firstTimeModule >> updateCnt[" + updateCnt + "]");
				logger.trace("firstTimeModule >> time used insertSelectSelfsqlStdQtyChkPgmFor [" + (beforeInsertSelectPlanExecTime - System.currentTimeMillis()) / 1000.0 + "]");
			}
			
			result.setResult(true);
			result.setStatus("11");
			result.setMessage("다음 단계로 이동");
			logger.trace("firstTimeModuleBaseExec result:" + result.toString());
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception occured :", e);
			logger.error("********************");
			result.setMessage(e.getMessage());
			result.setResult(false);
			result.setStatus("6");
			result.setMessage("프로그램 오류");
		}
		logger.info("end of firstTimeModuleBaseExec");
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	private SqlStandardOperationPlugIn makeModelfromJSONObject(JSONObject jsonObj) throws Exception {
		logger.trace("Start makeModelfromJSONObject");
		SqlStandardOperationPlugIn model = new SqlStandardOperationPlugIn();
		
		try {
			model.setSql_std_qty_chkt_id		(jsonObj.get("sql_std_qty_chkt_id")			== null ? "" : jsonObj.get("sql_std_qty_chkt_id") + "");
			model.setSql_std_qty_program_seq	(jsonObj.get("sql_std_qty_program_seq")		== null ? "" : jsonObj.get("sql_std_qty_program_seq") + "");
			model.setProject_id					(jsonObj.get("project_id")					== null ? "" : jsonObj.get("project_id") + "");
			model.setWrkjob_cd					(jsonObj.get("wrkjob_cd")					== null ? "" : jsonObj.get("wrkjob_cd") + "");
			model.setProgram_div_cd				(jsonObj.get("program_div_cd")				== null ? "" : jsonObj.get("program_div_cd") + "");
			model.setProgram_nm					(jsonObj.get("program_nm")					== null ? "" : jsonObj.get("program_nm") + "");
			model.setProgram_desc				(jsonObj.get("program_desc")				== null ? "" : jsonObj.get("program_desc") + "");
			model.setDbio						(jsonObj.get("dbio")						== null ? "" : jsonObj.get("dbio") + "");
			model.setSql_hash					(jsonObj.get("sql_hash")					== null ? "" : jsonObj.get("sql_hash") + "");
			model.setSql_length					(jsonObj.get("sql_length")					== null ? "" : jsonObj.get("sql_length") + "");
			model.setProgram_source_desc		(jsonObj.get("program_source_desc")			== null ? "" : jsonObj.get("program_source_desc") + "");
			model.setDir_nm						(jsonObj.get("dir_nm")						== null ? "" : jsonObj.get("dir_nm") + "");
			model.setFile_nm					(jsonObj.get("file_nm")						== null ? "" : jsonObj.get("file_nm") + "");
			model.setProgram_type_cd			(jsonObj.get("program_type_cd")				== null ? "" : jsonObj.get("program_type_cd") + "");
			model.setSql_command_type_cd		(jsonObj.get("sql_command_type_cd")			== null ? "" : jsonObj.get("sql_command_type_cd") + "");
			model.setDynamic_sql_yn				(jsonObj.get("dynamic_sql_yn")				== null ? "" : jsonObj.get("dynamic_sql_yn") + "");
			model.setDb_user					(jsonObj.get("db_user")						== null ? "" : jsonObj.get("db_user") + "");
			model.setDb_name					(jsonObj.get("db_name")						== null ? "" : jsonObj.get("db_name") + "");
			model.setFirst_time					(jsonObj.get("first_time")					== null ? -1 : Integer.parseInt(jsonObj.get("first_time") + ""));
			model.setDeveloper_id				(jsonObj.get("developer_id")				== null ? "" : jsonObj.get("developer_id") + "");
			model.setDeveloper_nm				(jsonObj.get("developer_nm")				== null ? "" : jsonObj.get("developer_nm") + "");
			model.setAbs_dir_nm					(jsonObj.get("abs_dir_nm")					== null ? "" : jsonObj.get("abs_dir_nm") + "");
			model.setCount_create_plan			(jsonObj.get("count_create_plan")			== null ? "" : jsonObj.get("count_create_plan") + "");
			model.setDbid						(jsonObj.get("dbid")						== null ? "" : jsonObj.get("dbid") + "");
			model.setParser_code				(jsonObj.get("parser_code")					== null ? "" : jsonObj.get("parser_code") + "");
			model.setSql_std_qty_div_cd			(jsonObj.get("sql_std_qty_div_cd")			== null ? "" : jsonObj.get("sql_std_qty_div_cd") + "");
			model.setSql_std_qty_scheduler_no	(jsonObj.get("sql_std_qty_scheduler_no")	== null ? "" : jsonObj.get("sql_std_qty_scheduler_no") + "");
			model.setFile_cnt					(jsonObj.get("file_cnt")					== null ? "" : jsonObj.get("file_cnt") + "");
			
			model.setSql_source_type_cd			(jsonObj.get("sql_source_type_cd")			== null ? "" : jsonObj.get("sql_source_type_cd") + "");
			model.setGather_term_type_cd		(jsonObj.get("gather_term_type_cd")			== null ? "" : jsonObj.get("gather_term_type_cd") + "");
			model.setGather_range_div_cd		(jsonObj.get("gather_range_div_cd")			== null ? "" : jsonObj.get("gather_range_div_cd") + "");
			model.setGather_term_start_day		(jsonObj.get("gather_term_start_day")		== null ? "" : jsonObj.get("gather_term_start_day") + "");
			model.setGather_term_end_day		(jsonObj.get("gather_term_end_day")			== null ? "" : jsonObj.get("gather_term_end_day") + "");
			model.setOwner_list					(jsonObj.get("owner_list")					== null ? "" : jsonObj.get("owner_list") + "");
			model.setModule_list				(jsonObj.get("module_list")					== null ? "" : jsonObj.get("module_list") + "");
			model.setExtra_filter_predication	(jsonObj.get("extra_filter_predication")	== null ? "" : jsonObj.get("extra_filter_predication") + "");
			model.setStd_qty_target_dbid		(jsonObj.get("std_qty_target_dbid")			== null ? "" : jsonObj.get("std_qty_target_dbid") + "");
			
			if(jsonObj.get("sql_cnt") != null) {
				model.setSql_cnt(Integer.parseInt(jsonObj.get("sql_cnt") + ""));
			}
			
			model.setSql_std_qty_error_seq		(jsonObj.get("sql_std_qty_error_seq")		== null ? -1 : Integer.parseInt(jsonObj.get("sql_std_qty_error_seq") + ""));
			model.setErr_sbst					(jsonObj.get("err_sbst")					== null ? "" : jsonObj.get("err_sbst") + "");
			model.setSql_std_qty_status_cd		(jsonObj.get("sql_std_qty_status_cd")		== null ? "" : jsonObj.get("sql_std_qty_status_cd") + "");
		} catch(NumberFormatException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** NumberFormatException occured :", e);
			logger.error("********************");
			throw new NumberFormatException(e.getMessage());
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			throw new Exception(e);
		}
		
		logger.trace("makeModelfromJSONObject >> model:\n" + model);
		logger.trace("end of makeModelfromJSONObject");
		return model;
	}
	
	private void putMsg(String type) throws Exception {
		CxClient.get().putMsg(type);
	}
	
	private void putMsg(String type, String msg) throws Exception {
		CxClient.get().putMsg(type, msg);
	}
	
	private HashMap<String, Object> createPlan(JSONObject jsonMaster) throws NullPointerException, Exception {
		logger.trace("Start createPlan");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		long beforeTime = System.currentTimeMillis();
		JSONArray jsonArrayRes = null;
		JSONObject jsonObjRes = null;
		int errCnt = 0;
		
		try {
			/* Call Rule 100 */
			long beforeCreatePlanTime = System.currentTimeMillis();
			jsonArrayRes = InspectSqlCodeTask.createPlan(jsonMaster);
			logger.trace("createPlan >> use time [" + (System.currentTimeMillis() - beforeCreatePlanTime) / 1000.0 + "]");
			logger.trace("createPlan >> jsonArrayRes >> \n" + jsonArrayRes.toJSONString());
			
			if(( jsonMaster.get("sql_std_qty_div_cd") != null && "2".equals(jsonMaster.get("sql_std_qty_div_cd"))) && 
					jsonMaster.get("force_completion") != null && "true".equals(jsonMaster.get("force_completion"))) {
				/* check batch And force completion */
				jsonObjRes = (JSONObject) jsonArrayRes.get(0);
				resultMap.put("is_error", jsonObjRes.get("is_error"));
				resultMap.put("err_msg", jsonObjRes.get("err_msg"));
			} else {
				jsonObjRes = (JSONObject) jsonArrayRes.get(0);
				String KILL_INSPECT = "KILL_INSPECT";
				
				if(jsonObjRes.containsKey("is_error") == true && "false".equals(jsonObjRes.get("is_error")) &&
						jsonObjRes.containsKey("err_msg") == true && KILL_INSPECT.equals(jsonObjRes.get("err_msg")) == true) {
					resultMap.put("is_error", jsonObjRes.get("is_error"));
					resultMap.put("err_msg", jsonObjRes.get("err_msg"));
				} else if(jsonObjRes.containsKey("is_error") == true && "false".equals(jsonObjRes.get("is_error")) &&
						jsonObjRes.containsKey("err_msg") == true && "Complete".equals(jsonObjRes.get("err_msg"))) {
					/* Master Alive(PING 요청) */
					resultMap.put("is_error", jsonObjRes.get("is_error"));
					resultMap.put("err_msg", jsonObjRes.get("err_msg"));
				} else {
					errCnt = Integer.parseInt(jsonObjRes.get("err_cnt") + "");
					resultMap.put("err_cnt", errCnt);
					logger.trace("createPlan >> errCnt["+ errCnt + "]");
					
					if(errCnt > 0) {
						resultMap = updatePlan100((JSONArray) jsonArrayRes.get(1));
					}
				}
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** SQLSyntaxErrorException >>> ", e);
			logger.error("********************");
			throw new Exception(e);
		} catch(SQLException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** SQLException >>> ", e);
			logger.error("********************");
			throw new Exception(e);
		} catch(NullPointerException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** NullPointerException >>> ", e);
			logger.error("********************");
			throw new Exception(e);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception >>> ", e);
			logger.error("********************");
			throw new Exception(e);
		}
		logger.trace("createPlan >> resultMap [ " + resultMap.toString() + " ]");
		logger.trace("end of createPlan [" + (System.currentTimeMillis() - beforeTime) / 1000.0 + "]");
		
		return resultMap;
	}
	
	private HashMap updatePlan100(JSONArray jsonArrayErr) throws SQLException {
		logger.trace("Start updatePlan100");
		long startTime = System.currentTimeMillis();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		SqlStandardOperationPlugIn updateModel = new SqlStandardOperationPlugIn();
		ArrayList<SqlStandardOperationPlugIn> batchList = new ArrayList<SqlStandardOperationPlugIn>();
		HashMap errMap = null;
		Iterator keyIter = null;
		
		try {
			errMap = (HashMap) jsonArrayErr.get(0);
			keyIter = errMap.keySet().iterator();
			
			while(keyIter.hasNext()) {
				updateModel = new SqlStandardOperationPlugIn();
				
				String key = keyIter.next() + "";		/* sqlStdQtyChktId_sqlStdQtyProgramSeq */
				String value = errMap.get(key) + "";	/* err_msg > ORAERR_xxx */
				String[] splitArr = key.split("_");		/* index 0 > sql_std_qty_chkt_id     *
														 * index 1 > sql_std_qty_program_seq */
				updateModel.setErr_msg(value);
				updateModel.setSql_std_qty_chkt_id(splitArr[0]);
				updateModel.setSql_std_qty_program_seq(splitArr[1]);
				
				batchList.add(updateModel);
				resultMap.put(splitArr[1], value);
			}
			
			if(batchList.size() > 0) {
				updatePlan100Foreach(batchList);
			}
		} catch(SQLSyntaxErrorException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** SQLSyntaxErrorException :", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} catch(SQLException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** SQLException :", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} catch(NullPointerException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** NullPointerException :", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception :", e);
			logger.error("********************");
			resultMap.put("is_error", "true");
			resultMap.put("err_msg", e.getMessage());
		} finally {
		}
		logger.trace("updatePlan100 >> resultMap [ " + resultMap.toString() + " ]");
		logger.trace("end of updatePlan100 UsedTime[" + (System.currentTimeMillis() - startTime) / 1000.0 + "]s");
		return resultMap;
	}
	
	/**
	 * 
	 * @param boxModel
	 * @return  0 : This message is not force  completion.<br>
	 *			1 : Force completion of this message is already registered.<br>
	 *			2 : Force completion of this message is newly registered.<br>
	 */
	private int isAlreadyRegisteredForceComplete(String project_id, int sql_std_qty_div_cd, 
			String sql_std_qty_scheduler_no, String sql_std_qty_chkt_id, boolean force_completion) 
					throws NullPointerException, Exception {
		logger.trace("Start isAlreadyRegisteredForceComplete");
		int resultIdx = 0;			/* 0 : This message is not force  completion.                  */
									/* 1 : Force completion of this message is already registered. */
									/* 2 : Force completion of this message is newly registered.   */
		
		try {
			if(sql_std_qty_div_cd != 2 && sql_std_qty_div_cd != 4) {
				return resultIdx;
			}
			
			logger.trace("isAlreadyRegisteredForceComplete >> project_id[" + 
					project_id + "] sql_std_qty_div_cd[" + sql_std_qty_div_cd + 
					"] sql_std_qty_scheduler_no[" + sql_std_qty_scheduler_no + 
					"] sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + 
					"] force_completion[" + force_completion + "]");
			if(force_completion == true) {
				if(this.forceCompletionMap.containsKey(sql_std_qty_chkt_id) == true) {
					resultIdx = 1;
				} else {
					this.forceCompletionMap.put(sql_std_qty_chkt_id, project_id);
					
					resultIdx = 2;
				}
			} else {
				resultIdx = checkRegisteredForceOut(project_id, sql_std_qty_chkt_id);
			}
		} catch(NullPointerException e) {
			resultIdx = -1;
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** NullPointerException :", e);
			logger.error("********************");
			throw new Exception(e);
		} catch(Exception e) {
			resultIdx = -1;
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception :", e);
			logger.error("********************");
			throw new Exception(e);
		}
		
		logger.trace("end of isAlreadyRegisteredForceComplete >> resultIdx [" + resultIdx + "]");
		return resultIdx;
	}
	
	private int checkRegisteredForceOut(String project_id, String sql_std_qty_chkt_id) throws Exception {
		int resultIdx = 0;		/* 0 : This message is not force  completion.                  */
								/* 1 : Force completion of this message is already registered. */
		
		try {
			if(this.forceCompletionMap.containsKey(sql_std_qty_chkt_id) == true) {
				this.forceCompletionMap.remove(sql_std_qty_chkt_id);
				
				resultIdx = 1;
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + "] project_id[" + project_id + "]\n" +
					this.forceCompletionMap.toString());
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return resultIdx;
	}
	
	private String getDbid(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		Database tempDb = new Database();
		Database database = new Database();
		String dbid = "";
		
		try {
			tempDb.setDb_name(sqlStandardOperationPlugIn.getDb_name());
			
			database = sqlStandardOperationPlugInDao.findDbId(tempDb);
			
			dbid = database.getDbid();
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			dbid = "N/A";
		}
		
		return dbid;
	}
	
	private SqlStandardOperationPlugIn updateSelfsqlStdQtyChkPgmModel(SqlStandardOperationPlugIn model, HashMap<String, String> wrkJobCdMap) throws Exception {
		String program_source_desc = "";
		String sql_std_qty_div_cd = "";
		String parser_code = "";
		Map<String, String> idMap = null;
		String dbio = "", dev_id = "", dev_name = "", program_desc = "", wrkjob_cd = "";
		
		try {
			program_source_desc = model.getProgram_source_desc();
			sql_std_qty_div_cd = model.getSql_std_qty_div_cd();
			parser_code = model.getParser_code();
			
			if(sql_std_qty_div_cd.length() > 0 && Integer.parseInt(sql_std_qty_div_cd) == KEY_SQL_STD_QTY_DIV_CD_BATCH_EXEC) {
				idMap = Parser.pullDBIOBatchExec(sql_std_qty_div_cd, 
						parser_code, program_source_desc, wrkJobCdMap);
				
				dbio = idMap.get("dbio") + "";
				dev_id = idMap.get("developer_id") + "";
				dev_name = idMap.get("developer_nm") + "";
				program_desc = idMap.get("program_desc") + "";
				wrkjob_cd = idMap.get("wrkjob_cd") + "";
				
				if(wrkJobCdMap != null && wrkjob_cd.length() > 0 && wrkJobCdMap.containsKey(wrkjob_cd) == true) {
					wrkjob_cd = wrkJobCdMap.get(wrkjob_cd);
				}
				
				model.setDbio(dbio);
				model.setDeveloper_id(dev_id);
				model.setDeveloper_nm(dev_name);
				model.setProgram_desc(program_desc);
				model.setWrkjob_cd(wrkjob_cd);
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception >>> ", e);
			logger.error("********************");
			
			throw new Exception(e);
		}
		
		return model;
	}
	
	private List<SqlStandardOperationPlugInResponse> processingDataResponse(List<SqlStandardOperationPlugIn> orgList) throws Exception {
		SqlStandardOperationPlugIn model;
		SqlStandardOperationPlugInResponse responseModel;
		List<SqlStandardOperationPlugInResponse> resultList = new LinkedList<SqlStandardOperationPlugInResponse>();
		int orgListSize = 0;
		
		try {
			orgListSize = orgList.size();
			
			for(int orgListIndex = 0; orgListIndex < orgListSize; orgListIndex++) {
				responseModel = new SqlStandardOperationPlugInResponse();
				model = orgList.get(orgListIndex);
				
				compareResultListResponse(resultList, responseModel, model, orgListIndex);
			}
		} catch(Exception e) {
			throw e;
		}
		
		return resultList;
	}
	
	private void compareResultListResponse(List<SqlStandardOperationPlugInResponse> resultList, 
			SqlStandardOperationPlugInResponse shortModel, SqlStandardOperationPlugIn model, 
			int targetListIndex) throws Exception {
		SqlStandardOperationPlugInResponse targetModel;
		String fileNm = "";
		String programNm = "";
		String dirNm = "";
		boolean fileCompareFlag = false;
		
		try {
			shortModel.setFile_nm_table(combineFileNmTable(model));
			shortModel.setProgram_nm_table(combineProgramNmTable(model));
			shortModel.setDir_nm_table(combineDirNmTable(model));
			shortModel.setFile_nm(model.getFile_nm());
			shortModel.setProgram_nm(model.getProgram_nm() == null ? "" : model.getProgram_nm());
			shortModel.setDir_nm(model.getDir_nm());
			shortModel.setErr_msg(model.getErr_msg());
			shortModel.setQty_chk_idt_cd(model.getQty_chk_idt_cd());
			shortModel.setQty_chk_idt_nm(model.getQty_chk_idt_nm());
			shortModel.setAbs_dir_nm(model.getAbs_dir_nm());
			
			if(targetListIndex == 0) {
				resultList.add(shortModel);
				return;
			}
			
			targetModel = resultList.get(targetListIndex - 1);
			
			fileNm = model.getFile_nm();
			programNm = model.getProgram_nm() == null ? "" : model.getProgram_nm();
			dirNm = model.getDir_nm();
			fileCompareFlag = fileNm.equals(targetModel.getFile_nm());
			
			if(model.getErr_msg() == null) {
				shortModel.setErr_msg("");
			}
			
			if(dirNm.equals(targetModel.getDir_nm())  && fileCompareFlag == true) {
				shortModel.setDir_nm_table("");
			}
			
			if(fileCompareFlag) {
				shortModel.setFile_nm_table("");
			}
			
			if(programNm.equals(targetModel.getProgram_nm()) && fileCompareFlag == true) {
				shortModel.setProgram_nm_table("");
			}
			
			resultList.add(shortModel);
		} catch(Exception e) {
			throw e;
		}
		
		return;
	}
	
	private String combineFileNmTable(SqlStandardOperationPlugIn model) throws Exception {
		StringBuffer fileNmTable = new StringBuffer();
		
		try {
			fileNmTable.append(model.getFile_nm()).append(" (").append(model.getDir_err_cnt()).append(" 건)");
		} catch(Exception e) {
			throw e;
		}
		
		return fileNmTable.toString();
	}
	
	private String combineProgramNmTable(SqlStandardOperationPlugIn model) throws Exception {
		StringBuffer programNmTable = new StringBuffer();
		String program_nm = "";
		
		try {
			program_nm = model.getProgram_nm() == null ? "" : model.getProgram_nm();
			programNmTable.append(program_nm).append(" (").append(model.getProgram_err_cnt()).append(" 건)");
		} catch(Exception e) {
			throw e;
		}
		
		return programNmTable.toString();
	}
	
	private String combineDirNmTable(SqlStandardOperationPlugIn model) throws Exception {
		StringBuffer dirNmTable = new StringBuffer();
		
		try {
			dirNmTable.append(model.getDir_nm());
		} catch(Exception e) {
			throw e;
		}
		
		return dirNmTable.toString();
	}
	
	@Override
	public List<SqlStandardOperationPlugIn> getMaxSqlStdQtyChktId() throws Exception {
		return sqlStandardOperationPlugInDao.getMaxSqlStdQtyChktId();
	}
	
	@Override
	public List<Database> findDbid(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		Database tempDb = new Database();
		Database database = new Database();
		List<Database> resultList = new ArrayList<Database>();
		
		try {
			tempDb.setDb_name(sqlStandardOperationPlugIn.getDb_name());
			
			database = sqlStandardOperationPlugInDao.findDbId(tempDb);
			
			resultList.add(database);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return resultList;
	}
	
	@Override
	public boolean deleteTableList(SqlStandardOperationPlugIn model) throws SQLSyntaxErrorException, Exception {
		logger.info("Start deleteTableList");
		boolean resultBoolean = false;
		int resultExec = -1;
		int resultFileError = -1;
		int resultResult = -1;
		int resultPgm = -1;
		int resultErr = -1;
		int resultErrSum = -1;
		
		try {
			if("2".equals(model.getSql_std_qty_div_cd()) == true ||
					"4".equals(model.getSql_std_qty_div_cd()) == true) {
				resultErr = sqlStandardOperationPlugInDao.deleteSelfsqlStdQtyChkErr(model);
				resultErrSum = sqlStandardOperationPlugInDao.deleteSelfsqlStdQtyChkErrSum(model);
				
				/* sql_std_qty_div_cd 값이 형상기반("2")이거나 실행기반("4")일 때에는 하드코드로 정의한 dev_id "OPENPOP"으로 정의한다. */
				model.setDeveloper_id("OPENPOP");
			}
			
			resultResult = sqlStandardOperationPlugInDao.deleteSelfsqlStdQtyChkResult(model);
			resultPgm = sqlStandardOperationPlugInDao.deleteSelfsqlStdQtyChkPgm(model);
			
			if("1".equals(model.getSql_std_qty_div_cd()) == true || "2".equals(model.getSql_std_qty_div_cd()) == true) {
				resultFileError = sqlStandardOperationPlugInDao.deleteSelfsqlStdQtyFileError(model);
			}
			
			resultExec = sqlStandardOperationPlugInDao.deleteSelfsqlStdPlanExec(model);
			
			logger.trace("delete resultErr[" + resultErr + "] resultErrSum[" + resultErrSum +
					"] resultResult[" + resultResult + "] resultPgm[" + resultPgm + 
					"] resultFileError[" + resultFileError + "] resultExec[" + resultExec + "]");
			resultBoolean = true;
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception occured :", e);
			logger.error("********************");
		}
		logger.info("end of deleteTableList");
		return resultBoolean;
	}
	
	@Override
	public boolean insertSelfsqlStdQtyPlanExec(SqlStandardOperationPlugIn model) throws SQLSyntaxErrorException, Exception {
		logger.info("Start insertSelfsqlStdQtyPlanExec");
		boolean flag = false;
		int result = 0;
		
		try {
			/* 이 테이블의 dev_id가 아닌 일반적으로 알고 있는 dev_id가 아닌 하드코드로 정의한 dev_id "OPENPOP"으로 정의한다. */
			if("2".equals(model.getSql_std_qty_div_cd()) == true ||
					"4".equals(model.getSql_std_qty_div_cd()) == true) {
				model.setDeveloper_id("OPENPOP");
			}
			
			model = setGatherTermDay(model);
			
			result = sqlStandardOperationPlugInDao.insertSelfsqlStdQtyPlanExec(model);
			flag = true;
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		logger.info("end of insertSelfsqlStdQtyPlanExec");
		return flag;
	}
	
	@Override
	public List<ProjectSqlQtyChkRule> getCountCreatePlan(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		return projectSqlQtyChkRuleMngDao.getCountCreatePlan(projectSqlQtyChkRule);
	}
	
	@Override
	public Result receiveBoxBatch(SqlStandardOperationPlugIn boxModel) throws SQLSyntaxErrorException, Exception {
		long beforeTime = System.currentTimeMillis();
		logger.info("***** Start receiveBoxBatch[" + DateUtil.getNowFulltime() + "]");
		Result result = new Result();
		long afterTime = 0L;
		
		JSONObject obj = null;
		String sql_std_qty_scheduler_no = "";
		String sql_std_qty_chkt_id = "";
		String project_id = "";
		String file_cnt = "";
		String sql_std_qty_div_cd = "";
		int forceCompletionIdx = -1;
		boolean force_completion = false;
		long dbid = 0L;
		String parser_code = "";
		int count_create_plan = -1;
		String db_user = "";
		
		try {
			startCxClient();
			
			obj = getFirst(boxModel);
			
			project_id = obj.get("project_id") + "";
			sql_std_qty_div_cd = obj.get("sql_std_qty_div_cd") + "";
			sql_std_qty_scheduler_no = obj.get("sql_std_qty_scheduler_no") + "";
			sql_std_qty_chkt_id = obj.get("sql_std_qty_chkt_id") + "";
			file_cnt = obj.get("file_cnt") == null ? "0" : obj.get("file_cnt") + "";
			parser_code = obj.get("parser_code") == null ? "" : obj.get("parser_code") + "";
			count_create_plan = obj.get("count_create_plan") == null ? 0 : Integer.parseInt(obj.get("count_create_plan") + "");
			dbid = obj.get("dbid") == null ? 0L : Long.parseLong(obj.get("dbid") + "");
			db_user = obj.get("db_user") + "";
			force_completion = obj.containsKey("force_completion");
			logger.trace("receiveBoxBatch >> receiveData >>> sql_std_qty_chkt_id[" + sql_std_qty_chkt_id + 
					"] project_id[" + project_id + "] sql_std_qty_div_cd[" + sql_std_qty_div_cd +
					"] force_completion[" + force_completion + "]");
			
			/*
			 * 0 : This message is not force completion.
			 * 1 : Force completion of this message is already registered.
			 * 2 : Force completion of this message is newly registered.
			 */
			forceCompletionIdx = isAlreadyRegisteredForceComplete(project_id, 
					Integer.parseInt(sql_std_qty_div_cd), 
					sql_std_qty_scheduler_no, sql_std_qty_chkt_id, force_completion);
			
			if(forceCompletionIdx == 2) {
				logger.info("Force completion of this message is newly registered.");
				result.setResult(true);
				result.setStatus("2");
				result.setMessage("force completion successful.");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;		// To WEB
			} else if(forceCompletionIdx == 1) {
				result.setResult(false);
				result.setStatus("3");
				result.setMessage("force completion data");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;		// To Scheduler
			}
			
			if(checkFirstFile(boxModel) == true) {
				result = checkAgentStatusResult(obj);
				
				if(Integer.parseInt(result.getStatus()) == 4) {
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** Agent error.");
					logger.error("** End the program. Will be going soon.");
					logger.error("********************");
					logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
					return result;
				}
			}
			
			if(checkFileError(obj) == true) {
				result = insertSelfsqlStdQtyFileError(obj);
			} else {
				batchInsertForeachModel(boxModel.getJson_list(), sql_std_qty_chkt_id);
			}
			
			
			if(checkIsLast(boxModel) == false) {
				result.setResult(true);
				result.setStatus("0");
				result.setMessage("다음 파일 요청");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			} else {
				result.setResult(true);
				result.setStatus("11");
				result.setMessage("다음 단계로 이동");
				logger.trace("recevieBoxBatch result:" + result.toString());
				
				result = checkSqlQty(sql_std_qty_div_cd, sql_std_qty_chkt_id,
						project_id, sql_std_qty_scheduler_no, parser_code, 
						count_create_plan, dbid, db_user, file_cnt);
			}
		} catch(NullPointerException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(" NullPointerException >>> ", e);
			logger.error("********************");
			result.setResult(false);
			
			throw new Exception(e);
		} catch(SQLSyntaxErrorException e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(" SQLSyntaxErrorException >>> ", e);
			logger.error("********************");
			result.setResult(false);
			
			throw new SQLSyntaxErrorException(e);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(" Exception >>> ", e);
			logger.error("********************");
			result.setResult(false);
			
			throw new Exception(e);
		} finally {
			logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
			checkStatusInfo(result, makeModelfromJSONObject(getLast(boxModel)));
		}
		afterTime = System.currentTimeMillis();
		logger.info("end of receiveBoxBatch ServiceImpl end [" + (afterTime - beforeTime) / 1000.0 + "]");
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	@Override
	public HashMap<String, String> getWrkJobCdList() {
		HashMap<String, String> wrkJobCdMap = new HashMap<String, String>();
		List<WrkJobCd> wrkJobCdList = null;
		WrkJobCd wrkJobCd = null;
		
		try {
			wrkJobCdList = sqlStandardOperationPlugInDao.getWrkJobCdList();
			
			if(wrkJobCdList == null || wrkJobCdList.size() == 0) {
				logger.warn("getWrkJobCdList > Wrong");
				return wrkJobCdMap;
			}
			
			for(int listIdx = 0; listIdx < wrkJobCdList.size(); listIdx++) {
				wrkJobCd = wrkJobCdList.get(listIdx);
				
				wrkJobCdMap.put(wrkJobCd.getWrkjob_div_cd(), wrkJobCd.getWrkjob_cd());
			}
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception >>> ", e);
			logger.error("********************");
		}
		
		return wrkJobCdMap;
	}
	
	@Override
	public String createPlan(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		String status = "true";
		int result = -1;
		String dbId = "";
		String sql_std_qty_chkt_id = "";
		
		try {
			dbId = getDbid(sqlStandardOperationPlugIn);
			sql_std_qty_chkt_id = sqlStandardOperationPlugIn.getSql_std_qty_chkt_id();
			
			result = SelfSQLStdQtyExplainPlan.createPlan(Long.parseLong(dbId), sql_std_qty_chkt_id, sqlStandardOperationPlugIn.getDb_user());
			
			logger.trace("createPlan result[" + result + "]");
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			status = "false";
		}
		
		return status;
	}
	
	@Override
	public Result executeSql(SqlStandardOperationPlugIn model) throws Exception {
		long beforeTime = System.currentTimeMillis();
		logger.info("***** Start executeSql[" + DateUtil.getNowFulltime() + "]");
		Result result = new Result();
		long afterTime = 0L;
		
		int forceCompletionIdx = -1;
		boolean force_completion = false;
		String project_id = "";
		String sql_std_qty_div_cd = "";
		String sql_std_qty_scheduler_no = "";
		String sql_std_qty_chkt_id = "";
		String parser_code = "";
		int count_create_plan = -1;
		long dbid = 0L;
		String db_user = "";
		JSONObject jsonObj = null;
		
		try {
			startCxClient();
			
			if(model.getJson_list() != null) {
				jsonObj = model.getJson_list().get(0);
				
				force_completion = Boolean.parseBoolean(jsonObj.get("force_completion") + "");
				project_id = jsonObj.get("project_id") + "";
				sql_std_qty_div_cd = jsonObj.get("sql_std_qty_div_cd") + "";
				sql_std_qty_scheduler_no = jsonObj.get("sql_std_qty_scheduler_no") + "";
				sql_std_qty_chkt_id = jsonObj.get("sql_std_qty_chkt_id") + "";
				
				if(force_completion == false) {
					parser_code = jsonObj.get("parser_code") + "";
					count_create_plan = Integer.parseInt(jsonObj.get("count_create_plan") + "");
					dbid = Long.parseLong(jsonObj.get("dbid") + "");
					db_user = jsonObj.get("db_user") + "";
				} else {
					model.setForce_completion(force_completion + "");
					model.setProject_id(project_id);
					model.setSql_std_qty_div_cd(sql_std_qty_div_cd);
					model.setSql_std_qty_chkt_id(sql_std_qty_chkt_id);
				}
			} else {
				project_id = model.getProject_id();
				sql_std_qty_div_cd = model.getSql_std_qty_div_cd();
				sql_std_qty_scheduler_no = model.getSql_std_qty_scheduler_no();
				sql_std_qty_chkt_id = model.getSql_std_qty_chkt_id();
				parser_code = model.getParser_code();
				count_create_plan = Integer.parseInt(model.getCount_create_plan());
				dbid = Long.parseLong(model.getDbid());
				db_user = model.getDb_user();
			}
			
			forceCompletionIdx = isAlreadyRegisteredForceComplete(project_id, 
					Integer.parseInt(sql_std_qty_div_cd), 
					sql_std_qty_scheduler_no, sql_std_qty_chkt_id, force_completion);
			
			if(forceCompletionIdx == 2) {
				logger.info("executeSql > Force completion of this message is newly registered.");
				result.setResult(true);
				result.setStatus("2");
				result.setMessage("force completion successful");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;		// To WEB
			} else if(forceCompletionIdx == 1) {
				result.setResult(false);
				result.setStatus("3");
				result.setMessage("force completion data");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;		// To Scheduler
			}
			
			if(checkFirstTimeExecuteSql(model) == true) {
				result = checkAgentStatusExecuteSqlResult(model);
				
				if(Integer.parseInt(result.getStatus()) == 4) {
					logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
					logger.error("** Agent error.");
					logger.error("** End the program. Will be going soon.");
					logger.error("********************");
					logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
					return result;
				} else {
					result.setResult(true);
					result.setStatus("11");
					result.setMessage("다음 단계로 이동");
					logger.trace("executeSql result:" + result.toString());
				}
			} else {
				result.setResult(false);
				result.setStatus("6");
				result.setMessage("프로그램 오류");
				logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
				return result;
			}
			
			result = checkSqlQty(sql_std_qty_div_cd, sql_std_qty_chkt_id,
					project_id, sql_std_qty_scheduler_no, parser_code, 
					count_create_plan, dbid, db_user);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error("** Exception >>> ", e);
			logger.error("********************");
			result.setResult(false);
			
			throw new Exception(e);
		} finally {
			checkStatusInfo(result, model);
		}
		afterTime = System.currentTimeMillis();
		logger.info("end of executeSql ServiceImpl end [" + (afterTime - beforeTime) / 1000.0 + "]");
		logger.trace("[" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "] result :\n" + result.toString());
		return result;
	}
	
	@Override
	public boolean updateSelfsqlStdQtyPlanExecForceComplete(SqlStandardOperationPlugIn model) throws Exception {
		boolean flag = false;
		int updateNum = -1;
		
		try {
			updateNum = sqlStandardOperationPlugInDao.updateSelfsqlStdQtyPlanExecForceComplete(model);
			logger.trace("updateSelfsqlStdQtyPlanExecForceComplete > updateNum[" + updateNum + "]");
			flag = true;
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
		}
		
		return flag;
	}
	
	@Override
	public List<SqlStandardOperationPlugInResponse> getNonComplianceDataResponse(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		ProjectSqlQtyChkRule projectSqlQtyChkRule = new ProjectSqlQtyChkRule();
		List<SqlStandardOperationPlugIn> resultList = new ArrayList<SqlStandardOperationPlugIn>();
		List<SqlStandardOperationPlugInResponse> responseList = null;
		ProjectSqlQtyChkRule resultModel = new ProjectSqlQtyChkRule();
		String qty_chk_sql = "";
		
		try {
			sqlStandardOperationPlugIn.setDbid(getDbid(sqlStandardOperationPlugIn));
			
			projectSqlQtyChkRule.setProject_id(sqlStandardOperationPlugIn.getProject_id());
			projectSqlQtyChkRule.setQty_chk_idt_cd(sqlStandardOperationPlugIn.getQty_chk_idt_cd());
			
			resultModel = projectSqlQtyChkRuleMngDao.getProjectSqlStdQtyChkSqlUnit(projectSqlQtyChkRule);
			
			if(resultModel == null) {
				String msg = "프로젝트[" + sqlStandardOperationPlugIn.getProject_id() + 
						"]의 SQL 품질점검 RULE 정보가 DB에 존재하지 않습니다.";
				throw new Exception(msg); 
			}
			
			qty_chk_sql = resultModel.getQty_chk_sql();
			
			try {
				qty_chk_sql = CryptoUtil.decryptAES128(qty_chk_sql, key);
				resultModel.setQty_chk_sql(qty_chk_sql);
				sqlStandardOperationPlugIn.setQty_chk_sql(qty_chk_sql);
			}catch(Exception e) {
				resultModel.setQty_chk_sql("복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.");
			}
			
			resultList = sqlStandardOperationPlugInDao.getNonComplianceData(sqlStandardOperationPlugIn);
			
			responseList = processingDataResponse(resultList);
		} catch(Exception e) {
			logger.error("** [" + SystemUtil.getMethodName() + "][" + SystemUtil.getLineNumber() + "]");
			logger.error(ERROR_TRACE, e);
			logger.error("********************");
			throw e;
		}
		
		return responseList;
	}
}
