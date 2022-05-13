package omc.spop.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import omc.spop.base.SessionManager;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.PerformanceCheckMngDao;
import omc.spop.dao.SqlsDao;
import omc.spop.model.IqmsURIConstants;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.ReqIqms;
import omc.spop.model.Result;
import omc.spop.model.RtnMsg;
import omc.spop.model.Sqls;
import omc.spop.model.Status;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.server.Iqms;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.PerfInspectMngService;
import omc.spop.utils.Util;

/***********************************************************
 * 2021.07.29	황예지	최초작성
 **********************************************************/

@Service("perfInspectMngService")
public class PerfInspectMngServiceImple implements PerfInspectMngService {
	private static final Logger logger = LoggerFactory.getLogger(PerfInspectMngServiceImple.class);
	int successCnt = 0;
	int failCnt = 0;

	@Value("#{defaultConfig['iqmsip']}")
	private String iqmsip;

	@Value("#{defaultConfig['iqmsport']}")
	private String iqmsport;

	@Value("#{defaultConfig['iqms_server_uri']}")
	private String IQMS_SERVER_URI;

	@Value("#{defaultConfig['customer']}")
	private String customer;

	@Autowired
	private PerformanceCheckMngDao performanceCheckMngDao;

	@Autowired
	private ImprovementManagementDao improvementManagementDao;
	
	@Autowired
	private SqlsDao sqlsDao;
	
	@Override
	public List<PerformanceCheckMng> getPerfCheckStep(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckStep(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> getInspectionList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getPerformanceCheckMngListExpc (performanceCheckMng);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getInspectionListExcelDown(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerformanceCheckMngListExpcExcel(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> getInspectionStep(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		
		List<PerformanceCheckMng> tempPerformanceCheckMng = performanceCheckMngDao.getPerformanceCheckStageBasicInfoList(performanceCheckMng);
		if (tempPerformanceCheckMng != null && tempPerformanceCheckMng.size() > 0) {
			String top_wrkjob_cd = StringUtils.defaultString(tempPerformanceCheckMng.get(0).getTop_wrkjob_cd());
			
			if (!top_wrkjob_cd.equals("")) {
				performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);
				return performanceCheckMngDao.getPerformanceCheckStageList(performanceCheckMng);
			}
		}
		return tempPerformanceCheckMng;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getInspectionStepExcelDown(
			PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerformanceCheckStageList4Excel(performanceCheckMng);
	}
	
	@Override
	public PerformanceCheckMng getPerfCheckResultBasicInfo(PerformanceCheckMng performanceCheckMng) throws Exception {
		PerformanceCheckMng tempPerformanceCheckMng = performanceCheckMngDao
				.getPerformanceCheckStageBasicInfo(performanceCheckMng);
		if (tempPerformanceCheckMng != null) {
			String top_wrkjob_cd = StringUtils.defaultString(tempPerformanceCheckMng.getTop_wrkjob_cd());
			if (!top_wrkjob_cd.equals("")) {
				performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);
				return performanceCheckMngDao.getPerfCheckResultBasicInfoEspc(performanceCheckMng);
			}
		}
		return null;
	}
	
	@Override
	public PerformanceCheckMng selectDeployCheckStatus(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.selectDeployCheckStatus(performanceCheckMng);
	}
	
	@Override
	public PerformanceCheckMng getPerfCheckAllPgm(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckAllPgmEspc(performanceCheckMng);
	}
	
	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkDetailResultListEspc(performanceCheckMng);
	}
	
	@Override
	public List<PerformanceCheckMng> selectImprovementGuideList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectImprovementGuideList(performanceCheckMng);
	}
	
	@Override
	public PerformanceCheckMng getPerfCheckIdFromDeployId(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerfCheckIdFromDeployId(performanceCheckMng);
	}
	
	public @ResponseBody RtnMsg sendResult(PerformanceCheckMng performanceCheckMng) {
		String perf_check_result_div_cd = StringUtils.defaultString(performanceCheckMng.getPerf_check_result_div_cd());
		String resultyn = "N";
		
		if(perf_check_result_div_cd.equals("A")) {
			resultyn = "Y";
		}
		
		ReqIqms rIqms = new ReqIqms();
		rIqms.setP_cmid(performanceCheckMng.getDeploy_id());
		rIqms.setP_systemgb("WBN0041007");
		rIqms.setP_servicegb("1001");
		rIqms.setP_resultyn(resultyn);
		
		String jsondata = null;
		String sendResult = null;
		RtnMsg rtnMsg = null;
		
		try {
			jsondata = new ObjectMapper().writeValueAsString(rIqms);
			JSONObject json = (JSONObject) JSONValue.parse(jsondata);
			if (customer != null && customer.equals("kbcd")) {
				sendResult = Util.httpJsonSendKbCd1000(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
						Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			} else {
				sendResult = Util.httpJsonSend(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
						Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			}
			
			JSONObject jso = (JSONObject) JSONValue.parse(sendResult);
			
			if (jso != null) {
				try {
					rtnMsg = new ObjectMapper().readValue(jso.toString(), RtnMsg.class);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (JsonProcessingException e2) {
			e2.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnMsg;
	}
	
	@Override
	public @ResponseBody Result perfChkRsltNoti(PerformanceCheckMng performanceCheckMng,  String pageName) throws Exception {
		Result result = new Result();
		String params = performanceCheckMng.getParams();
		String params_array[] = params.split("\\|");
		int totalCnt = params_array.length;
		successCnt = 0;
		failCnt = 0;

		for (String param : params_array) {
			String param_array[] = param.split("\\^");
			String deploy_id = param_array[0];
			String perf_check_id = param_array[1];
			String perf_check_result_div_cd = param_array[2];

			performanceCheckMng.setDeploy_id(deploy_id);
			performanceCheckMng.setPerf_check_id(perf_check_id);
			performanceCheckMng.setPerf_check_result_div_cd(perf_check_result_div_cd);
			/* KBCard CM 호출 */
			/* 성능검증완료 결과 통보 */
			Thread thread1 = new NotiThread(performanceCheckMng);
			thread1.start();
			thread1.join(60000);
			if(thread1.isAlive()) {
				thread1.interrupt();
			}
		}
		if (totalCnt != successCnt + failCnt) {
			failCnt = totalCnt - successCnt;
		}
		result.setResult(true);
		
		/*
		통보버튼을 클릭한 페이지 구분하는 조건식
		성능 검증 관리 페이지에서 클릭힌 경우 : pageName이 null
		성능 검증 결과 페이지에서 클릭한 경우 : pageName이 performanceCheckResult
		*/
		if(pageName == null) {
			result.setMessage("성능검증결과 통보 중 총 " + totalCnt + "건 중 " + successCnt + "건이 성공하고 " + failCnt + "건이 실패하였습니다.");
			
		} else {
			if(successCnt >= 1 && failCnt == 0) {
				//성능 검증 결과 페이지에서 클릭 한 경우 + 성공한 경우(successCnt가 1이상이면서 failCnt가 0인 경우)
				result.setMessage("배포시스템에 검증결과통보가 성공했습니다.");
				
			} else{
				//성능 검증 결과 페이지에서 클릭 한 경우 + 실패한 경우(successCnt가 0이거나 failCnt가 0이 아닌 경우)
				result.setMessage("배포시스템에 검증결과통보가 실패했습니다.<br>검증결과통보를 다시 시도하세요.<br>계속 실패할 경우 관리자에게 문의바랍니다.");
			}
		}
		
		return result;
	}
	
	@Override
	public List<PerformanceCheckMng> getPerfCheckResultList(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckResultListEspc(performanceCheckMng);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> getPerfCheckResultList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getPerfCheckResultListEspcExcel(performanceCheckMng);
	}
	
	@Override
	public int perfChkRequestTuningDupChkEspc(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.perfChkRequestTuningDupChkEspc(performanceCheckMng);
	}

	@Override
	public int perfChkRequestTuning(PerformanceCheckMng performanceCheckMng, HttpServletRequest req) throws Exception {
		TuningTargetSql tuningTargetSql = new TuningTargetSql();
		PerformanceCheckMng deployPerfSqlStat = null;
		PerformanceCheckMng perfCheckAllPgm = null;
		StringBuilder bindVar = new StringBuilder();
		StrBuilder imprb_exec_plan = new  StrBuilder();
		BigDecimal currentElapTime = null;
		BigDecimal current_elap_time = new BigDecimal("0");		// 현재수행시간
		String forecast_result_cnt = "0";						// 예상결과건수
		
		List<String> planList = Collections.emptyList();
		String sql_desc = null;
		String next_tuning_no = null;
		String dbid = null;
		String tuning_requester_id = null;
		String tuning_requester_wrkjob_cd = null;
		String parsing_schema_name = null;
		String auth_nm = null;
		String sql_text = null;
		String bindSetSeq = null;
		String imprb_exec_plan_str = null;
		
		int rowCnt = 0;
		
		List<PerformanceCheckMng> bindVars = Collections.emptyList();
		
		try {
			bindVars = performanceCheckMngDao.selectBindVarEspc(performanceCheckMng);
			for(PerformanceCheckMng p : bindVars) {
				bindVar.append(p.getBind_var()+"\n");
			}
			
			sql_desc = bindVar.toString();
			sql_desc = sql_desc.replace("${",":").replace("#{",":").replace("}","");
			tuningTargetSql.setSql_desc(sql_desc);
			
			// 1.TUNING_TARGET_SQL Next tuning no 조회
			next_tuning_no = improvementManagementDao.getNextTuningNo();
			dbid = performanceCheckMngDao.selectDbid(performanceCheckMng);
			
			logger.debug("dbid ==> {}",dbid);
			
			tuning_requester_id = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getUser_id());
			tuning_requester_wrkjob_cd = StringUtils.defaultString( performanceCheckMngDao.getUserWrkjob(tuning_requester_id) );
			deployPerfSqlStat = performanceCheckMngDao.selectDeployPerfSqlStat(performanceCheckMng);
			
			if(deployPerfSqlStat != null) {
				currentElapTime = deployPerfSqlStat.getCurrent_elap_time();
				
				if(currentElapTime != null) {
					current_elap_time = currentElapTime;
				}
				forecast_result_cnt = StringUtils.defaultString(deployPerfSqlStat.getForecast_result_cnt(),"0");
			}
			
			parsing_schema_name = performanceCheckMngDao.selectParsingSchemaNameEspc(performanceCheckMng);
			perfCheckAllPgm = (PerformanceCheckMng) performanceCheckMngDao.getPerfCheckAllPgm(performanceCheckMng);
			
			auth_nm = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getAuth_nm());
			sql_text = StringUtils.defaultString(perfCheckAllPgm.getProgram_source_desc());
			
			//sql_text에서 파라미터 구문을 변경한다.
			sql_text = sql_text.replace(":","¶")
								.replace("${",":")
								.replace("#{",":")
								.replace("}","")
								.replace("¶",":");
			
			tuningTargetSql.setSql_text(sql_text);
			tuningTargetSql.setSql_id(performanceCheckMng.getSql_id());
			tuningTargetSql.setPerf_check_id(performanceCheckMng.getPerf_check_id());
			tuningTargetSql.setPerf_check_step_id(performanceCheckMng.getPerf_check_step_id());
			tuningTargetSql.setProgram_id(performanceCheckMng.getProgram_id());
			tuningTargetSql.setTuning_no( next_tuning_no );
			tuningTargetSql.setDbid( dbid );
			tuningTargetSql.setChoice_div_cd( "K" );																	// 실행 SQL 성능 검증 관리(K)
			tuningTargetSql.setTuning_status_cd( "2" );																	//요청(2)
			tuningTargetSql.setTuning_requester_id( tuning_requester_id );
			tuningTargetSql.setTuning_status_changer_id( tuning_requester_id );
			tuningTargetSql.setTuning_requester_wrkjob_cd( tuning_requester_wrkjob_cd );
			tuningTargetSql.setProgram_type_cd( StringUtils.defaultString(perfCheckAllPgm.getProgram_type_cd()) );		//온라인('1'), 배치('2')
			tuningTargetSql.setDbio( StringUtils.defaultString(perfCheckAllPgm.getDbio()) );
			tuningTargetSql.setTr_cd( StringUtils.defaultString(perfCheckAllPgm.getTr_cd()) );
			tuningTargetSql.setCurrent_elap_time(current_elap_time);
			tuningTargetSql.setForecast_result_cnt(forecast_result_cnt);
			tuningTargetSql.setParsing_schema_name(parsing_schema_name);
			tuningTargetSql.setTuning_status_change_why(auth_nm+"튜닝요청");
			tuningTargetSql.setAvg_elapsed_time(performanceCheckMng.getElapsed_time());
			tuningTargetSql.setAvg_buffer_gets(performanceCheckMng.getBuffer_gets());
			tuningTargetSql.setAvg_row_processed(performanceCheckMng.getRows_processed());
			tuningTargetSql.setExecutions(performanceCheckMng.getExecutions());
			
			// 2. TUNING_TARGET_SQL INSERT
			rowCnt = improvementManagementDao.insertTuningTargetSqlFromPerfChkResult(tuningTargetSql);
			
			// 3. SQL_TUNING_STATUS_HISTORY INSERT
			improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
			
			// 4. 기존에 SQL BIND 값 DELETE
			improvementManagementDao.deleteTuningTargetSqlBind(next_tuning_no);
			
			// new. 튜닝 요청 시 이전 plan도 함께 저장
			planList = performanceCheckMngDao.getImprbExecPlan(performanceCheckMng);
			for(String str : planList) {
				imprb_exec_plan.appendln(str);
			}
			imprb_exec_plan_str = String.valueOf(imprb_exec_plan);
			
			tuningTargetSql.setImprb_exec_plan( ("null".equals(imprb_exec_plan_str)? "" : imprb_exec_plan_str) );
			
			improvementManagementDao.insertImprbExecPlan(tuningTargetSql);
			
			bindSetSeq = improvementManagementDao.getBindSetSeq(next_tuning_no);
			tuningTargetSql.setBind_set_seq(bindSetSeq);
			
			if( "0".equals(performanceCheckMng.getProgram_execute_tms())
					|| "".equals(performanceCheckMng.getProgram_execute_tms()) ) {
				// 수행결과가 없는 경우
				// 5. 새로운 SQL BIND 값 INSERT
				improvementManagementDao.insertTuningTargetSqlBindFromPerfChkResult(tuningTargetSql);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
			
		}finally {
			tuningTargetSql = null;
			deployPerfSqlStat = null;
			perfCheckAllPgm = null;
			bindVar = null;
			currentElapTime = null;
			current_elap_time = null;
			forecast_result_cnt = null;
			sql_desc = null;
			next_tuning_no = null;
			dbid = null;
			tuning_requester_id = null;
			tuning_requester_wrkjob_cd = null;
			parsing_schema_name = null;
			auth_nm = null;
			sql_text = null;
			bindSetSeq = null;
			planList = null;
			imprb_exec_plan = null;
			imprb_exec_plan_str = null;
		}
		
		return rowCnt;
	}
	
	@Override
	public List<PerformanceCheckMng> getInspectSqlList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getInspectSqlList(performanceCheckMng);
	}
	
	@Override
	public List<PerformanceCheckMng> getInspectResultDetail(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getInspectResultDetail(performanceCheckMng);
	}
	
	@Override
	public List<Sqls> getExecutionPlan(Sqls sqls) {
		return sqlsDao.sqlPlanPerformanceCheck(sqls);
	}
	
	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkExecBindValue(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkExecBindValue(performanceCheckMng);
	}
	
	@Override
	public PerformanceCheckMng getSqlInfo(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.selectSqlInfo(performanceCheckMng);
	}
	
	@Override
	public @ResponseBody Result inspectRsltNoti(PerformanceCheckMng performanceCheckMng,  String pageName) throws Exception {
		Result result = new Result();
		String params = performanceCheckMng.getParams();
		String params_array[] = params.split("\\|");
		int totalCnt = params_array.length;
		successCnt = 0;
		failCnt = 0;
		
		try {
			for (String param : params_array) {
				String param_array[] = param.split("\\^");
				String deploy_id = param_array[0];
				String perf_check_id = param_array[1];
				String perf_check_result_div_cd = param_array[2];
				
				performanceCheckMng.setDeploy_id(deploy_id);
				performanceCheckMng.setPerf_check_id(perf_check_id);
				performanceCheckMng.setPerf_check_result_div_cd(perf_check_result_div_cd);
				
				/* KBCard CM 호출 */
				/* 성능검증완료 결과 통보 */
				Thread thread1 = new NotiThread(performanceCheckMng);
				thread1.start();
				thread1.join(60000);
				if(thread1.isAlive()) {
					thread1.interrupt();
				}
			}
			if (totalCnt != successCnt + failCnt) {
				failCnt = totalCnt - successCnt;
			}
			result.setResult(true);
			
			if( pageName == null || pageName.isEmpty() ) {
				result.setMessage("성능검증결과 통보 중 총 " + totalCnt + "건 중 " + successCnt + "건이 성공하고 " + failCnt + "건이 실패하였습니다.");
				
			} else {
				if(successCnt >= 1 && failCnt == 0) {
					result.setMessage("배포시스템에 검증결과통보가 성공했습니다.");
					
				} else{
					result.setMessage("배포시스템에 검증결과통보가 실패했습니다.<br>검증결과통보를 다시 시도하세요.<br>검증결과통보가 계속 지속될 경우 관리자에게 문의바랍니다.");
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result inspectForceFinish(PerformanceCheckMng performanceCheckMng) throws InterruptedException {
		Result result = new Result();
		
		Object obj_user_id = SessionManager.getLoginSessionObject();
		String user_id = "";
		if(obj_user_id instanceof String) {
			user_id = performanceCheckMngDao.getUserId();
		}else {
			user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		}
		performanceCheckMng.setUser_id(user_id);
		
		try {
			String params = performanceCheckMng.getParams();
			String params_array[] = params.split("\\|");
			
			if (params_array.length <= 0) {
				result.setResult(false);
				result.setMessage("강제 검증완료 처리 대상이 없습니다.");
				return result;
			}
			
			int cnt = 0;
			String param_array[] = null;
			String deploy_id = null;
			String perf_check_id = null;
			String sysdate = null;
			Thread thread1 = null;
			
			for (String param : params_array) {
				param_array = param.split("\\^");
				
				deploy_id = param_array[0];
				perf_check_id = param_array[1];
				
				performanceCheckMng.setDeploy_id(deploy_id);
				performanceCheckMng.setPerf_check_id(perf_check_id);
				
				sysdate = improvementManagementDao.getSysdate();
				performanceCheckMng.setPerf_check_complete_dt(sysdate);
				performanceCheckMng.setDeploy_check_status_update_dt(sysdate);
				
				// 로그인사용자ID
				performanceCheckMng.setUser_id(user_id);
				performanceCheckMng.setPerf_check_completer_id(user_id);
				performanceCheckMng.setDeploy_check_status_updater_id(user_id);
				
				// 1. 성능검증 단계별 예외(영구/한시) 대상 저장(Snapshot)
				cnt = performanceCheckMngDao.insertDeployPerfChkTrgtExcptPgm2(performanceCheckMng);
				logger.debug("Insert into DEPLOY_PERF_CHK_TRGT_EXCPT_PGM : {}", cnt);
				
				// 2. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
				cnt = performanceCheckMngDao.updateDeployPerfChkStepExec2(performanceCheckMng);
				logger.debug("Update DEPLOY_PERF_CHK_STEP_EXEC : {}", cnt);
				
				// 3. UPDATE DEPLOY_PERF_CHK
				performanceCheckMng.setDeploy_check_status_cd("02");
				performanceCheckMng.setPerf_check_complete_meth_cd("3");//강제완료
				performanceCheckMng.setPerf_check_result_div_cd("A");
				cnt = performanceCheckMngDao.updateDeployPerfChk(performanceCheckMng);
				logger.debug("Update DEPLOY_PERF_CHK : {}", cnt);
				
				// 4. INSERT INTO DEPLOY_PERF_CHK_STATUS_HISTORY
				performanceCheckMng.setDeploy_check_status_chg_why("강제 검증완료로 자동 반려처리함");
				cnt = performanceCheckMngDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
				logger.debug("Insert into DEPLOY_PERF_CHK_STATUS_HISTORY : {}", cnt);
				
				// 5. 예외 요청상태 반려처리
				cnt = performanceCheckMngDao.insertDeployPerfChkExcptHistory(performanceCheckMng);
				logger.debug("Insert into DEPLOY_PERF_CHK_EXCPT_HISTORY : {}", cnt);
				
				// 6. UPDATE DEPLOY_PERF_CHK_EXCPT_REQUEST
				cnt = performanceCheckMngDao.updateDeployPerfChkExcptRequest2(performanceCheckMng);
				logger.debug("Update DEPLOY_PERF_CHK : {}", cnt);
				
				// 7. UPDATE DEPLOY_PERF_CHK_ALL_PGM
				cnt = performanceCheckMngDao.updateDeployPerfChkAllPgm(performanceCheckMng);
				logger.debug("Update DEPLOY_PERF_CHK_ALL_PGM : {}", cnt);
				
				/* KBCard CM 호출 */
				/* 성능검증완료 결과 통보 */
				thread1 = new NotiThread(performanceCheckMng);
				thread1.start();
				thread1.join(60000);
				if(thread1.isAlive()) {
					thread1.interrupt();
				}
			}
			
			result.setResult(true);
			result.setMessage("강제 검증완료 처리되었습니다.");
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
			return result;
		}
		return result;
	}
	
	@Override
	public int getProgramCnt(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getProgramCnt(performanceCheckMng);
	}
	
	@Override
	public PerformanceCheckMng getPerfCheckResultCount(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckResultCount(performanceCheckMng);
	}
	
	@Override
	public Result performanceCheckComplete(PerformanceCheckMng performanceCheckMng) throws InterruptedException {
		Result result = new Result();
		result.setMessage("성능검증완료 처리에 성공하였습니다.");
		result.setResult(true);
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		int cnt = 0;
		
		try {
			String perf_check_step_ids = StringUtils.defaultString(performanceCheckMng.getPerf_check_step_id());
			String[] perf_check_step_id_arr = perf_check_step_ids.split(",");
			PerformanceCheckMng performanceCheckMng1 = null;
			String lastPerfCheckStepYn = null;
			String deployRequestDt = null;
			String perf_check_request_cn_div_cd = null;
			String sysdate = null;
			String perfCheckResultDivCd = null;
			
			Thread thread1 = null;
			
			for (int i = 0; i < perf_check_step_id_arr.length; i++) {
				// 0.프로그램 갯수 조회
				int programCnt = performanceCheckMngDao.getProgramCnt(performanceCheckMng);
				if (programCnt > 0) {
					// 1.프로그램 미수행 건수 조회
					performanceCheckMng.setPerf_check_step_id(perf_check_step_id_arr[i]);
					int testMissCnt = performanceCheckMngDao.getTestMissCnt(performanceCheckMng);
					if (testMissCnt > 0) {
						result.setMessage("미수행 프로그램이 존재하여 성능검증을 완료할 수 없습니다.<br/>미수행 프로그램 : " + testMissCnt + "건");
						result.setResult(false);
						return result;
					}
				}
				// 2. 최종성능검증단계여부 구하기
				performanceCheckMng1 = performanceCheckMngDao.getLastPerfCheckStepYn(performanceCheckMng);
				lastPerfCheckStepYn = StringUtils.defaultString(performanceCheckMng1.getLast_perf_check_step_yn());
				performanceCheckMng.setLast_perf_check_step_yn(lastPerfCheckStepYn);

				// 배포요청일시
				deployRequestDt = StringUtils.defaultString(performanceCheckMng1.getDeploy_request_dt());
				performanceCheckMng.setDeploy_request_dt(deployRequestDt);
				// 성능검증요청일시 = 배포요청일시
				performanceCheckMng.setPerf_check_request_dt(deployRequestDt);
				
				// PERF_CHECK_REQUEST_CN_DIV_CD -- 성능검증요청채널구분코드
				perf_check_request_cn_div_cd = StringUtils.defaultString(performanceCheckMng1.getPerf_check_request_cn_div_cd());
				performanceCheckMng.setPerf_check_request_cn_div_cd(perf_check_request_cn_div_cd);

				sysdate = improvementManagementDao.getSysdate();
				performanceCheckMng.setPerf_check_complete_dt(sysdate);
				performanceCheckMng.setDeploy_check_status_update_dt(sysdate);
				
				// 로그인사용자ID
				performanceCheckMng.setUser_id(user_id);
				performanceCheckMng.setPerf_check_completer_id(user_id);
				performanceCheckMng.setDeploy_check_status_updater_id(user_id);
				performanceCheckMng.setPerf_test_complete_yn("Y");

				if(programCnt > 0) {
					// 3. GET PERF_CHECK_RESULT_DIV_CD
					perfCheckResultDivCd = performanceCheckMngDao.getPerfCheckResultDivCd(performanceCheckMng);
					performanceCheckMng.setPerf_check_result_div_cd(perfCheckResultDivCd);
					// 4. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
					//성능검증완료일시 , A.PERF_CHECK_COMPLETE_DT =  TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS') 
					cnt = performanceCheckMngDao.updateDeployPerfChkStepExec(performanceCheckMng);
					
				}else {
					performanceCheckMng.setPerf_check_result_div_cd("A");
					// 4. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
					//성능검증완료일시 , A.PERF_CHECK_COMPLETE_DT =  TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS')
					cnt = performanceCheckMngDao.updateDeployPerfChkStepExecAll(performanceCheckMng);
				}
				logger.debug("programCnt : {}", programCnt);
				logger.debug("Update DEPLOY_PERF_CHK_STEP_EXEC : {}", cnt);
				
				// 성능검증 단계별 예외(영구/한시) 대상 저장(Snapshot)
				// 저장전 데이터 삭제
				performanceCheckMngDao.deleteDeployPerfChkTrgtExcptPgm(performanceCheckMng);
				performanceCheckMngDao.insertDeployPerfChkTrgtExcptPgm(performanceCheckMng);
				
				// 현재 성능 검증 단계가 최종 단계인 경우
				// 최종성능검증단계여부
				if ("Y".equals(lastPerfCheckStepYn) || programCnt <= 0) {
					performanceCheckMng.setDeploy_check_status_cd("02");
					performanceCheckMng.setPerf_check_complete_meth_cd("1");//정상완료
					performanceCheckMng.setDeploy_check_status_chg_why("성능검증완료 처리");
					//성능검증완료일시  , A.PERF_CHECK_COMPLETE_DT = TO_DATE(#{perf_check_complete_dt},'YYYY-MM-DD HH24:MI:SS')
					cnt = performanceCheckMngDao.updateDeployPerfChk(performanceCheckMng);
					logger.debug("Update DEPLOY_PERF_CHK : {}", cnt);
					//배포성능검증상태변경일시
					//DEPLOY_CHECK_STATUS_UPDATE_DT = TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS')
					cnt = performanceCheckMngDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
					logger.debug("Insert into DEPLOY_PERF_CHK_STATUS_HISTORY : {}", cnt);
					// 배포전 성능검증 최종 검증 단계 [성능검증완료] 시 한시검증제외 대상 삭제 처리
					// 다음 배포시에 SQL이 수정되지 않더라도 성능검증대상으로 다시 올라와야 되기 때문에 삭제처리함
					cnt = performanceCheckMngDao.updateDeployPerfChkAllPgm(performanceCheckMng);
					logger.debug("Update DEPLOY_PERF_CHK_ALL_PGM : {}", cnt);
					
					/* KBCard CM 호출 */
					/* 성능검증완료 결과 통보 */
					if ("01".equals(perf_check_request_cn_div_cd)) {
						thread1 = new NotiThread(performanceCheckMng);
						thread1.start();
						thread1.join(60000);
						if(thread1.isAlive()) {
							thread1.interrupt();
						}
					}
					if( performanceCheckMng.getCheck_result_anc_yn() == null
							|| ( performanceCheckMng.getCheck_result_anc_yn() != null && !"Y".equals(performanceCheckMng.getLast_perf_check_step_yn()) ) ) {
						result.setMessage("성능검증이 완료되었지만, 배포시스템에 검증결과통보가 실패했습니다.<br>검증결과통보를 다시 시도하세요.");
					}
					
				} else {// lastPerfCheckStepYn.equals("N")
					cnt = performanceCheckMngDao.updateLastPerfCheckStepId(performanceCheckMng);
					logger.debug("Update DEPLOY_PERF_CHK"
							+ " : {}", cnt);
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("성능검증완료 처리에 실패하였습니다.<br>성능검증완료를 다시 시도하세요.<br>계속 실패할 경우 관리자에게 문의바랍니다.");
			return result;
		}
		return result;
	}
	
	public boolean reExecute(PerformanceCheckMng performanceCheckMng) {
		boolean result = true;
		
		String[] perf_check_id_arr = performanceCheckMng.getPerf_check_id().split("&");
		int threadCnt = (perf_check_id_arr.length > 5) ? 5 : perf_check_id_arr.length;	// thread 갯수는 최대 5개
		int secondIdx = 0;
		
		ReExecThread[] threadArr = new ReExecThread[threadCnt];
		
		try {
			for(int i = 0; i < threadCnt; i++) {
				String firstId = perf_check_id_arr[i];
				
				secondIdx = i+5;
				String secondId = (secondIdx > perf_check_id_arr.length-1) ? null : perf_check_id_arr[secondIdx];
				
				threadArr[i] = new ReExecThread( firstId, secondId );
				threadArr[i].start();
			}
			
			for(ReExecThread thread : threadArr) {
				thread.join(60000);
			}
			
			for(ReExecThread thread : threadArr) {
				thread.interrupt();
				
				if( thread.isError() ) {
					result = false;
				}
			}
			
		}catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			result = false;
			
		}finally {
			perf_check_id_arr = null;
			threadArr = null;
		}
		
		return result;
	}
	
	class NotiThread extends Thread {
		RtnMsg rtnMsg;
		PerformanceCheckMng performanceCheckMng;
		
		NotiThread(PerformanceCheckMng performanceCheckMng){
			this.performanceCheckMng = performanceCheckMng;
		}
		
		@Override
		public void run() {
			logger.debug("task1 run...rtnMsg before:" + rtnMsg);
			rtnMsg = sendResult(performanceCheckMng);
			logger.debug("task1 run...rtnMsg after:" + rtnMsg);
			if (rtnMsg != null) {
				String ret_code = StringUtils.defaultString(rtnMsg.getRet_code());
				logger.debug("ret_code :"+ret_code);
				
				List<Status> ls = (List<Status>) rtnMsg.getRet_data();
				for (int i = 0; i < ls.size(); i++) {
					Status status = (Status) ls.get(i);
					String strCmid = StringUtils.defaultString(status.getCmid());
					String strStatus = StringUtils.defaultString(status.getStatus());
					logger.debug("Cmid ="+strCmid);
					logger.debug("Status =" + strStatus);
					
					if(strStatus.equals("OK")||strStatus.equals("3")) {
						successCnt++;
						performanceCheckMng.setCheck_result_anc_yn("Y");
						
					}else {
						performanceCheckMng.setCheck_result_anc_yn("N");
						failCnt++;
					}
					int updateResult = performanceCheckMngDao.updateCheckResultAncYn(performanceCheckMng);
					logger.debug("updateResult:" + updateResult);
				}
			}
		}
	}
	
	class ReExecThread extends Thread {
		Iqms iqms;
		String[] perf_check_id_arr;
		boolean error = false;
		
		ReExecThread(String id1, String id2){
			this.perf_check_id_arr = new String[]{id1, id2};
			this.iqms = new Iqms();
			iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		}
		
		@Override
		public void run(){
			try {
				for(String perf_check_id : perf_check_id_arr) {
					if (perf_check_id != null) {
						DeployPerfChk.execSqlPerfCheck(iqms, "11", perf_check_id, null);
					}
				}
				
			} catch (Exception e) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + e.getMessage());
				e.printStackTrace();
				error = true;
				
			}finally {
				perf_check_id_arr = null;
			}
		}
		
		public boolean isError() {
			return error;
		}
	}
}
