package omc.spop.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
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
import omc.spop.model.IqmsURIConstants;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.ReqIqms;
import omc.spop.model.Result;
import omc.spop.model.RtnMsg;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.Status;
import omc.spop.model.TuningTargetSql;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.PerformanceCheckMngService;
import omc.spop.utils.StringUtil;
import omc.spop.utils.Util;

/***********************************************************
 * 2019.01.11
 **********************************************************/

@Service("performanceCheckMngService")
public class PerformanceCheckMngServiceImpl implements PerformanceCheckMngService {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckMngServiceImpl.class);
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

	@Override
	public List<PerformanceCheckMng> getPerformanceCheckMngList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getPerformanceCheckMngList(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> getPerfCheckStep(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckStep(performanceCheckMng);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerformanceCheckMngList4Excel(
			PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerformanceCheckMngList4Excel(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng getPerformanceCheckStageBasicInfo(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getPerformanceCheckStageBasicInfo(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> getPerformanceCheckStageList(PerformanceCheckMng performanceCheckMng)
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
	public List<LinkedHashMap<String, Object>> getPerformanceCheckStageList4Excel(
			PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerformanceCheckStageList4Excel(performanceCheckMng);
	}

	@Override
	public int getTestMissCnt(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getTestMissCnt(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng getPerfCheckResultCount(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckResultCount(performanceCheckMng);
	}

	@Override
	public String getPerfCheckResultDivCd(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckResultDivCd(performanceCheckMng);
	}

	@Override
	public int updateDeployPerfChkStepExec(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.updateDeployPerfChkStepExec(performanceCheckMng);
	}

	@Override
	public int updatePerfTestCompleteYnToN(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.updatePerfTestCompleteYnToN(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng getPerfCheckResultBasicInfo(PerformanceCheckMng performanceCheckMng) throws Exception {
		PerformanceCheckMng tempPerformanceCheckMng = performanceCheckMngDao
				.getPerformanceCheckStageBasicInfo(performanceCheckMng);
		if (tempPerformanceCheckMng != null) {
			String top_wrkjob_cd = StringUtils.defaultString(tempPerformanceCheckMng.getTop_wrkjob_cd());
			if (!top_wrkjob_cd.equals("")) {
				performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);
				return performanceCheckMngDao.getPerfCheckResultBasicInfo(performanceCheckMng);
			}
		}
		return null;
	}

	@Override
	public List<PerformanceCheckMng> getPerfCheckResultList(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckResultList(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng getPerfCheckAllPgm(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPerfCheckAllPgm(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng selectDeployPerfChkStepTestDbList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		PerformanceCheckMng tempPerformanceCheckMng = performanceCheckMngDao
				.getPerformanceCheckStageBasicInfo(performanceCheckMng);
		if (tempPerformanceCheckMng != null) {
			String top_wrkjob_cd = StringUtils.defaultString(tempPerformanceCheckMng.getTop_wrkjob_cd());
			if (!top_wrkjob_cd.equals("")) {
				performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);
				return performanceCheckMngDao.selectDeployPerfChkStepTestDbList(performanceCheckMng);
			}
		}
		return null;
	}

	@Override
	public PerformanceCheckMng selectDefaultParsingSchemaInfo(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDefaultParsingSchemaInfo(performanceCheckMng);

	}

	@Override
	public PerformanceCheckMng selectDeployCheckStatus(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.selectDeployCheckStatus(performanceCheckMng);
	}

	@Override
	public int updateDeployPerfChkStepExecTotalCnt(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.updateDeployPerfChkStepExecTotalCnt(performanceCheckMng);
	}

	@Override
	public String selectMaxProgramExecuteTmsPlus1(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.selectMaxProgramExecuteTmsPlus1(performanceCheckMng);

	}

	@Override
	public int insertDeployPerfChkResult(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.insertDeployPerfChkResult(performanceCheckMng);
	}

	@Override
	public int insertDeployPerfChkExecBind(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.insertDeployPerfChkExecBind(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> getDeployPerfChkExeHistList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getDeployPerfChkExeHistList(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkDetailResultList(performanceCheckMng);

	}

	@Override
	public PerformanceCheckMng selectPerfCheckResultBasisWhy(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.selectPerfCheckResultBasisWhy(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectProgramExecuteTmsList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectProgramExecuteTmsList(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkExecBindList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkExecBindList(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkExecBindListPop(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkExecBindListPop(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkAllPgmList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkAllPgmList(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectVsqlBindCaptureList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectVsqlBindCaptureList(performanceCheckMng);

	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkExecBindValue(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectDeployPerfChkExecBindValue(performanceCheckMng);

	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfCheckResultList4Excel(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.getPerfCheckResultList4Excel(performanceCheckMng);
	}

	@Override
	public List<PerformanceCheckMng> selectImprovementGuideList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		return performanceCheckMngDao.selectImprovementGuideList(performanceCheckMng);
	}

	@Override
	public Result performanceCheckExecuteResult(PerformanceCheckMng performanceCheckMng, HttpServletRequest request)
			throws Exception {
		Result result = new Result();
		boolean success = true;

		PerformanceCheckMng deployCheckStatus = performanceCheckMngDao.selectDeployCheckStatus(performanceCheckMng);
		String deploy_check_status_cd = StringUtils.defaultString(deployCheckStatus.getDeploy_check_status_cd());
		String deploy_check_status_nm = StringUtils.defaultString(deployCheckStatus.getDeploy_check_status_nm());
		if (!deploy_check_status_cd.equals("01")) {
			result.setMessage("현재 배포성능점검상태가 " + deploy_check_status_nm + "이므로 성능점검을 수행할 수 없습니다.");
			result.setStatus("disabled");
			result.setResult(false);
			return result;
		}

		// 성능점검수행
		// -- .1 DEPLOY_PERF_CHK_STEP_EXEC(배포성능점검단계별수행내역) 테이블 UPDATE

		int performanceCheckMng1 = performanceCheckMngDao.updateDeployPerfChkStepExecTotalCnt(performanceCheckMng);
		logger.debug("performanceCheckMng1 :" + performanceCheckMng1);
		// -- .2 프로그램 수행회차 조회(PROGRAM_EXECUTE_TMS)
		String maxProgramExecuteTms = performanceCheckMngDao.selectMaxProgramExecuteTmsPlus1(performanceCheckMng);
		logger.debug("maxProgramExecuteTms :" + maxProgramExecuteTms);
		//performanceCheckMng.setProgramExecuteTms( Integer.valueOf(maxProgramExecuteTms) - 1 );
		performanceCheckMng.setProgram_execute_tms(maxProgramExecuteTms);
		// -- .3 DEPLOY_PERF_CHK_RESULT(배포성능점검결과내역) 테이블 INSERT
		// :PROGRAM_EXECUTE_TMS = maxProgramExecuteTms;
		// :PROGRAM_EXEC_DT = SYSDATE
		// :PROGRAM_EXECUTER_ID = 로그인사용자ID
		// :PROGRAM_EXEC_DIV_CD = 화면에서 선택한 코드
		// :PARSING_SCHEMA_NAME = 화면에서 선택한 값

		performanceCheckMng.setPagingCnt( Integer.valueOf( performanceCheckMng.getPagingCnt() ) );
		performanceCheckMng.setPerfCheckId( Integer.valueOf( performanceCheckMng.getPerf_check_id() ) );
		performanceCheckMng.setPerfCheckStepId( Integer.valueOf( performanceCheckMng.getPerf_check_step_id() ) );
		performanceCheckMng.setProgramId( Integer.valueOf( performanceCheckMng.getProgram_id() ) );

		int insertResult = performanceCheckMngDao.insertDeployPerfChkResult( performanceCheckMng );
		logger.debug("insertResult :" + insertResult);

		// -- .4 DEPLOY_PERF_CHK_EXEC_BIND(배포성능점검수행바인드내역) 테이블 INSERT
		// :PROGRAM_EXECUTE_TMS = QUERY_9.2;
		// :BIND_SEQ : 화면의 바인값을 1부터 순차적으로 저장;
		// :BIND_VAR_NM : 화면의 바인드명;
		// :BIND_VAR_VALUE : 화면의 바인드값;
		// :BIND_VAR_TYPE : 화면의 바인드 타입;
		String bind_seqs = StringUtils.defaultString(performanceCheckMng.getBind_seq());
		String bind_var_nms = StringUtils.defaultString(performanceCheckMng.getBind_var_nm());
		String bind_var_types = StringUtils.defaultString(performanceCheckMng.getBind_var_type());
		String bind_var_values = StringUtils.defaultString(performanceCheckMng.getBind_var_value());
		String hidden_bind_var_values = StringUtils.defaultString(performanceCheckMng.getHidden_bind_var_value());
		logger.debug("bind_seqs:" + bind_seqs);
		logger.debug("bind_var_nms:" + bind_var_nms);
		logger.debug("bind_var_types:" + bind_var_types);
		logger.debug("bind_var_values:" + bind_var_values);
		logger.debug("hidden_bind_var_values:" + hidden_bind_var_values);
		if (!bind_seqs.equals("")) {
			String bind_seq_arr[] = bind_seqs.split(",");
			String bind_var_nm_arr[] = bind_var_nms.split(",");
			String bind_var_type_arr[] = bind_var_types.split(",");
			String bind_var_value_arr[] = request.getParameterValues("bind_var_value");
			String hidden_bind_var_value_arr[] = request.getParameterValues("hidden_bind_var_value");
			// bind_seq -- 바인드순번
			// bind_var_nm -- 바인드변수명
			// bind_var_value -- 바인드변수값
			// bind_var_type -- 바인드변수타
			logger.debug("bind_seq_arr.length :" + bind_seq_arr.length);
			logger.debug("bind_var_nm_arr.length :" + bind_var_nm_arr.length);
			logger.debug("bind_var_type_arr.length :" + bind_var_type_arr.length);
			logger.debug("bind_var_value_arr.length :" + bind_var_value_arr.length);
			logger.debug("hidden_bind_var_value_arr.length :" + hidden_bind_var_value_arr.length);
			for (int i = 0; i < bind_seq_arr.length; i++) {
				performanceCheckMng.setBind_seq(bind_seq_arr[i]);
				performanceCheckMng.setBind_var_nm(bind_var_nm_arr[i]);
				performanceCheckMng.setBind_var_type(bind_var_type_arr[i]);
				if (bind_var_value_arr.length < bind_seq_arr.length) {
					if (i < bind_var_value_arr.length - 1) {
						String bind_var_value = StringUtils.defaultString(hidden_bind_var_value_arr[i]);
						bind_var_value = bind_var_value.replaceAll("@~!", ",");
						performanceCheckMng.setBind_var_value(bind_var_value);
					}
				} else {
					String bind_var_value = StringUtils.defaultString(hidden_bind_var_value_arr[i]);
					bind_var_value = bind_var_value.replaceAll("@~!", ",");
					performanceCheckMng.setBind_var_value(bind_var_value);
				}
				int insertDeployPerfChkExecBindResult = performanceCheckMngDao
						.insertDeployPerfChkExecBind(performanceCheckMng);
				logger.debug("insertDeployPerfChkExecBindResult [" + i + "]:" + insertDeployPerfChkExecBindResult);
			}
		}

		// 성능점검수행 버튼 클릭시 성능점검수행 프로세스가 다 끝난 후....
		// 마스터 서버 호출
		// SQL Runner...
		try {
			Long dbId = Long.parseLong(performanceCheckMng.getDbid());
			Long perfCheckId = Long.parseLong(performanceCheckMng.getPerf_check_id());
			Long perfCheckStepId = Long.parseLong(performanceCheckMng.getPerf_check_step_id());
			Long programId = Long.parseLong(performanceCheckMng.getProgram_id());
			Long programExecuteTms = Long.parseLong(performanceCheckMng.getProgram_execute_tms());
			String parsingSchema = performanceCheckMng.getParsing_schema_name();
			String pagingYn = performanceCheckMng.getPagingYn();
			int pagingCnt = Integer.valueOf(performanceCheckMng.getPagingCnt());

			String getPerfChkResult = DeployPerfChk.getPerfChk(dbId, perfCheckId, perfCheckStepId, programId,
					programExecuteTms, parsingSchema, pagingYn, pagingCnt);
			logger.debug("getPerfChkResult :" + getPerfChkResult);

			if (getPerfChkResult != null) {
				logger.debug("넘어온 값이 not null입니다.");
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonResult = (JSONObject) jsonParser.parse(getPerfChkResult);
				logger.debug("jsonResult :" + jsonResult);
				logger.debug("jsonResult.toString() :" + jsonResult.toString());

				String isError = (String) jsonResult.get("is_error");
				if (isError.equals("true")) {
					throw new Exception((String) jsonResult.get("err_msg"));
				}
			} else {
				success = false;
				logger.debug("넘어온 값이 null입니다.");

				performanceCheckMng.setPerf_check_result_basis_why1("예기치않은 오류");
				performanceCheckMng.setPerf_check_result_basis_why2("관리자에게 문의하세요!");

				int updateResult = performanceCheckMngDao.updateDeployPerfChkResult(performanceCheckMng);
				logger.debug("updateResult :" + updateResult);
			}
		} catch (Exception e) {
			logger.error("Exception msg :" + e.getMessage());

			performanceCheckMng.setPerf_check_result_basis_why1(e.getMessage());
			performanceCheckMng.setPerf_check_result_basis_why2("관리자에게 문의하세요!");

			int updateResult = performanceCheckMngDao.updateDeployPerfChkResult(performanceCheckMng);
			logger.error("updateResult :" + updateResult);

			throw e;
		}
		logger.debug("success :" + success);

		if (success) {
			result.setStatus("succeed");
			result.setMessage("성능점검을 수행하였습니다.");
			result.setResult(true);
		} else {
			result.setMessage("fail");
			result.setMessage("성능점검수행중 오류가 발생하였습니다.");
			result.setResult(false);
		}

		return result;
	}

	@Override
	public int updateLastPerfCheckStepId(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.updateLastPerfCheckStepId(performanceCheckMng);
	}

	@Override
	public List<SelftunPlanTable> getDeployPerfChkPlanTable(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getDeployPerfChkPlanTable(performanceCheckMng);
	}

	@Override
	public List<SelftunPlanTable> getDeployPerfSqlPlan(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getDeployPerfSqlPlan(performanceCheckMng);
	}

	@Override
	public String getDefaultPagingCnt(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getDefaultPagingCnt(performanceCheckMng);
	}

	@Override
	public Result performanceCheckComplete(PerformanceCheckMng performanceCheckMng) throws InterruptedException {
		Result result = new Result();
		result.setMessage("성능점검완료 처리에 성공하였습니다.");
		result.setResult(true);

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		int insertResult = 0;
		int updateResult = 0;

		try {
			String perf_check_step_ids = StringUtils.defaultString(performanceCheckMng.getPerf_check_step_id());
			String[] perf_check_step_id_arr = perf_check_step_ids.split(",");
			for (int i = 0; i < perf_check_step_id_arr.length; i++) {
				logger.debug("perf_check_step_id_arr[" + i + "] :" + perf_check_step_id_arr[i]);
				// 0.프로그램 갯수 조회
				int programCnt = performanceCheckMngDao.getProgramCnt(performanceCheckMng);
				if (programCnt > 0) {
					// 1.프로그램 미수행 건수 조회
					performanceCheckMng.setPerf_check_step_id(perf_check_step_id_arr[i]);
					int testMissCnt = performanceCheckMngDao.getTestMissCnt(performanceCheckMng);
					if (testMissCnt > 0) {
						result.setMessage("미수행 프로그램이 존재하여 성능점검을 완료할 수 없습니다.<br/>미수행 프로그램 : " + testMissCnt + "건");
						result.setResult(false);
						return result;
					}
				}
				// 2. 최종성능점검단계여부 구하기
				PerformanceCheckMng performanceCheckMng1 = performanceCheckMngDao.getLastPerfCheckStepYn(performanceCheckMng);
				String lastPerfCheckStepYn = StringUtils.defaultString(performanceCheckMng1.getLast_perf_check_step_yn());
				logger.debug("lastPerfCheckStepYn:" + lastPerfCheckStepYn);
				performanceCheckMng.setLast_perf_check_step_yn(lastPerfCheckStepYn);

				// 배포요청일시
				String deployRequestDt = StringUtils.defaultString(performanceCheckMng1.getDeploy_request_dt());
				logger.debug("deployRequestDt:" + deployRequestDt);
				performanceCheckMng.setDeploy_request_dt(deployRequestDt);
				// 성능점검요청일시 = 배포요청일시
				performanceCheckMng.setPerf_check_request_dt(deployRequestDt);
				
				// PERF_CHECK_REQUEST_CN_DIV_CD -- 성능점검요청채널구분코드
				String perf_check_request_cn_div_cd = StringUtils.defaultString(performanceCheckMng1.getPerf_check_request_cn_div_cd());
				logger.debug("perf_check_request_cn_div_cd :" + perf_check_request_cn_div_cd);
				performanceCheckMng.setPerf_check_request_cn_div_cd(perf_check_request_cn_div_cd);

				String sysdate = improvementManagementDao.getSysdate();
				performanceCheckMng.setPerf_check_complete_dt(sysdate);
				performanceCheckMng.setDeploy_check_status_update_dt(sysdate);
				
				// 로그인사용자ID
				performanceCheckMng.setUser_id(user_id);
				performanceCheckMng.setPerf_check_completer_id(user_id);
				performanceCheckMng.setDeploy_check_status_updater_id(user_id);
				performanceCheckMng.setPerf_test_complete_yn("Y");

				if(programCnt > 0) {
					// 3. GET PERF_CHECK_RESULT_DIV_CD
					String perfCheckResultDivCd = performanceCheckMngDao.getPerfCheckResultDivCd(performanceCheckMng);
					performanceCheckMng.setPerf_check_result_div_cd(perfCheckResultDivCd);
					// 4. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
				    //성능점검완료일시 , A.PERF_CHECK_COMPLETE_DT =  TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS') 
					updateResult = performanceCheckMngDao.updateDeployPerfChkStepExec(performanceCheckMng);
				}else {
					performanceCheckMng.setPerf_check_result_div_cd("A");
					// 4. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
				    //성능점검완료일시 , A.PERF_CHECK_COMPLETE_DT =  TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS')
					updateResult = performanceCheckMngDao.updateDeployPerfChkStepExecAll(performanceCheckMng);
				}

				// 성능점검 단계별 예외(영구/한시) 대상 저장(Snapshot)
				// 저장전 데이터 삭제
				int deleteDeployPerfChkTrgtExcptPgmResult = performanceCheckMngDao.deleteDeployPerfChkTrgtExcptPgm(performanceCheckMng);
				logger.debug("deleteDeployPerfChkTrgtExcptPgmResult :" + deleteDeployPerfChkTrgtExcptPgmResult);
				
				int insertDeployPerfChkTrgtExcptPgmResult = performanceCheckMngDao.insertDeployPerfChkTrgtExcptPgm(performanceCheckMng);
				logger.debug("insertDeployPerfChkTrgtExcptPgmResult :" + insertDeployPerfChkTrgtExcptPgmResult);

				// 현재 성능 점검 단계가 최종 단계인 경우
				// 최종성능점검단계여부
				if ("Y".equals(lastPerfCheckStepYn) || programCnt <= 0) {
					performanceCheckMng.setDeploy_check_status_cd("02");
					performanceCheckMng.setPerf_check_complete_meth_cd("1");//정상완료
					performanceCheckMng.setDeploy_check_status_chg_why("성능점검완료 처리");
					//성능점검완료일시  , A.PERF_CHECK_COMPLETE_DT = TO_DATE(#{perf_check_complete_dt},'YYYY-MM-DD HH24:MI:SS')
					updateResult = performanceCheckMngDao.updateDeployPerfChk(performanceCheckMng);
					//배포성능점검상태변경일시
					//DEPLOY_CHECK_STATUS_UPDATE_DT = TO_DATE(#{deploy_check_status_update_dt},'YYYY-MM-DD HH24:MI:SS')
					insertResult = performanceCheckMngDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
					// 2019-06-05 추가
					// 배포전 성능점검 최종 점검 단계 [성능점검완료] 시 한시점검제외 대상 삭제 처리
					// 다음 배포시에 SQL이 수정되지 않더라도 성능점검대상으로 다시 올라와야 되기 때문에 삭제처리함
					updateResult = performanceCheckMngDao.updateDeployPerfChkAllPgm(performanceCheckMng);

					/* KBCard CM 호출 */
					/* 성능점검완료 결과 통보 */
					if ("01".equals(perf_check_request_cn_div_cd)) {
						Thread thread1 = new NotiThread(performanceCheckMng);
						thread1.start();
						thread1.join(60000);
						if(thread1.isAlive()) {
							thread1.interrupt();
						}
						
//						long startTime = System.currentTimeMillis();
//						logger.debug("startTime :" + startTime);
//						long elapsedTime = 0L;
//						while (elapsedTime < 60 * 1000) {
//							// perform db poll/check
//							Thread.sleep(500);
//							elapsedTime = (new Date()).getTime() - startTime;
//							boolean isAlive = thread1.isAlive();
//							logger.debug("isAlive :"+isAlive+" elapsedTime:"+elapsedTime);
//							if(!isAlive) break;
//						}
//						if(elapsedTime >= 60 * 1000) {
//							thread1.interrupt();
//						}
					} // else if (!perf_check_request_cn_div_cd.equals("01")) {}
					if( performanceCheckMng.getCheck_result_anc_yn() == null
							|| ( performanceCheckMng.getCheck_result_anc_yn() != null && !"Y".equals(performanceCheckMng.getLast_perf_check_step_yn()) ) ) {
						result.setMessage("성능점검이 완료되었지만, 배포시스템에 점검결과통보가 실패했습니다.<br>점검결과통보를 다시 시도하세요.");
					}
					
				} else {// lastPerfCheckStepYn.equals("N")
					updateResult = performanceCheckMngDao.updateLastPerfCheckStepId(performanceCheckMng);
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			//result.setMessage(ex.getMessage());
			result.setMessage("성능점검완료 처리에 실패하였습니다.<br>성능점검완료를 다시 시도하세요.<br>계속 실패할 경우 관리자에게 문의바랍니다.");
			return result;
		}
		return result;
	}

	public @ResponseBody RtnMsg sendResult2(PerformanceCheckMng performanceCheckMng) {
		String perf_check_result_div_cd = StringUtils.defaultString(performanceCheckMng.getPerf_check_result_div_cd());
		logger.debug("perf_check_result_div_cd :"+perf_check_result_div_cd);
		String resultyn = "N";
		//perf_check_result_div_cd=성능점검결과구분코드 
		if(perf_check_result_div_cd.equals("A")) {
			resultyn = "Y";
		}
		logger.debug("resultyn :"+resultyn);
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
			logger.debug(jsondata);
			JSONObject json = (JSONObject) JSONValue.parse(jsondata);
			if (customer != null && customer.equals("kbcd")) {
				sendResult = Util.httpJsonSendKbCd1000(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
						Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			} else {
				sendResult = Util.httpJsonSend(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
						Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			}
			logger.info("sendResult=" + sendResult);
			JSONObject jso = (JSONObject) JSONValue.parse(sendResult);
			logger.debug("jso=" + jso);
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
//		try {
//			logger.debug("60초 휴식시작");
//			Thread.sleep(10 * 1000);
//			logger.debug("60초 휴식끝");
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		return rtnMsg;
	}

	/**
	 * 강제점검완료
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public Result perfChkForceFinish(PerformanceCheckMng performanceCheckMng) throws InterruptedException {
		Result result = new Result();
		result.setResult(true);
		result.setMessage("성능점검 강제 점검 완료 처리에 성공하였습니다.");

		Object obj_user_id = SessionManager.getLoginSessionObject();
		logger.debug("obj_user_id===>" + obj_user_id);
		String user_id = "";
		if(obj_user_id instanceof String) {
			logger.debug("obj_user_id instanceof String");
			user_id = performanceCheckMngDao.getUserId();
		}else {
			logger.debug("obj_user_id instanceof Login");
			user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		}
		performanceCheckMng.setUser_id(user_id);
		
		try {
//			String perf_check_step_ids = StringUtils.defaultString(performanceCheckMng.getPerf_check_step_id());
//			String[] perf_check_step_id_arr = perf_check_step_ids.split(",");

			String params = performanceCheckMng.getParams();
			logger.debug("params===>" + params);
			String params_array[] = params.split("\\|");
			if (params_array.length <= 0) {
				result.setResult(false);
				result.setMessage("성능점검 강제 점검 완료 처리 대상이 없습니다.");
				return result;
			}
			logger.debug("param_array.length===>" + params_array.length);
			for (String param : params_array) {
				String param_array[] = param.split("\\^");
				logger.debug("param_array.length===>" + param_array.length);
				logger.debug("deploy_id===>" + param_array[0]);
				logger.debug("perf_check_id===>" + param_array[1]);

				String deploy_id = param_array[0];
				String perf_check_id = param_array[1];

//				performanceCheckMng.setPerf_check_step_id(perf_check_step_id_arr[i]);
				performanceCheckMng.setDeploy_id(deploy_id);
				performanceCheckMng.setPerf_check_id(perf_check_id);

				int updateResult = 0;

				String sysdate = improvementManagementDao.getSysdate();
				performanceCheckMng.setPerf_check_complete_dt(sysdate);
				performanceCheckMng.setDeploy_check_status_update_dt(sysdate);

				// 로그인사용자ID
				performanceCheckMng.setUser_id(user_id);
				performanceCheckMng.setPerf_check_completer_id(user_id);
				performanceCheckMng.setDeploy_check_status_updater_id(user_id);

				logger.debug("performanceCheckMng:" + performanceCheckMng);

				// 1. 성능점검 단계별 예외(영구/한시) 대상 저장(Snapshot)
//				int deleteDeployPerfChkTrgtExcptPgmResult = performanceCheckMngDao.deleteDeployPerfChkTrgtExcptPgm2(performanceCheckMng);
//				logger.debug("deleteDeployPerfChkTrgtExcptPgmResult :" + deleteDeployPerfChkTrgtExcptPgmResult);

				// 1. 성능점검 단계별 예외(영구/한시) 대상 저장(Snapshot)
				int insertDeployPerfChkTrgtExcptPgmResult = performanceCheckMngDao
						.insertDeployPerfChkTrgtExcptPgm2(performanceCheckMng);
				logger.debug("insertDeployPerfChkTrgtExcptPgmResult :" + insertDeployPerfChkTrgtExcptPgmResult);

				// 2. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
				updateResult = performanceCheckMngDao.updateDeployPerfChkStepExec2(performanceCheckMng);
				logger.debug("updateResult1 :" + updateResult);

				// 3. UPDATE DEPLOY_PERF_CHK
				performanceCheckMng.setDeploy_check_status_cd("02");
				performanceCheckMng.setPerf_check_complete_meth_cd("3");//강제완료
				performanceCheckMng.setPerf_check_result_div_cd("A");
				logger.debug("performanceCheckMng:" + performanceCheckMng);
				updateResult = performanceCheckMngDao.updateDeployPerfChk(performanceCheckMng);
				logger.debug("updateResult2 :" + updateResult);

				// 4. INSERT INTO DEPLOY_PERF_CHK_STATUS_HISTORY
				performanceCheckMng.setDeploy_check_status_chg_why("강제점검완료로 자동 반려처리함");
				int insertResult = performanceCheckMngDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
				logger.debug("insertResult :" + insertResult);

				// 5. 예외 요청상태 반려처리
				insertResult = performanceCheckMngDao.insertDeployPerfChkExcptHistory(performanceCheckMng);
				logger.debug("insertResult :" + insertResult);

				// 6. UPDATE DEPLOY_PERF_CHK_EXCPT_REQUEST
				updateResult = performanceCheckMngDao.updateDeployPerfChkExcptRequest2(performanceCheckMng);
				logger.debug("updateResult :" + updateResult);

				// 7. UPDATE DEPLOY_PERF_CHK_ALL_PGM
				updateResult = performanceCheckMngDao.updateDeployPerfChkAllPgm(performanceCheckMng);
				logger.debug("updateResult :" + updateResult);

				/* KBCard CM 호출 */
				/* 성능점검완료 결과 통보 */
				Thread thread1 = new NotiThread(performanceCheckMng);
				thread1.start();
				thread1.join(60000);
				if(thread1.isAlive()) {
					thread1.interrupt();
				}
				
			}
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
	/**
	 * 자동점검완료,자동완료
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public Result perfChkAutoFinish(PerformanceCheckMng performanceCheckMng) throws InterruptedException {
		Result result = new Result();
		result.setResult(true);
		result.setMessage("성능점검 자동 점검 완료 처리에 성공하였습니다.");
		
		Object obj_user_id = SessionManager.getLoginSessionObject();
		logger.debug("obj_user_id===>" + obj_user_id);
		String user_id = "";
		if(obj_user_id instanceof String) {
			logger.debug("obj_user_id instanceof String");
			user_id = performanceCheckMngDao.getUserId();
		}else {
			logger.debug("obj_user_id instanceof Login");
			user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		}
		performanceCheckMng.setUser_id(user_id);
		
		try {
//			String perf_check_step_ids = StringUtils.defaultString(performanceCheckMng.getPerf_check_step_id());
//			String[] perf_check_step_id_arr = perf_check_step_ids.split(",");
			
			String deploy_id = StringUtils.defaultString(performanceCheckMng.getDeploy_id());
			String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());
			logger.debug("deploy_id===>" + deploy_id);
			logger.debug("perf_check_id===>" + perf_check_id);
			if (perf_check_id== null || perf_check_id.equals("")) {
				result.setResult(false);
				result.setMessage("성능점검 강제 점검 완료 처리 대상이 없습니다.");
				return result;
			}
				
			int updateResult = 0;
			
			String sysdate = improvementManagementDao.getSysdate();
			performanceCheckMng.setPerf_check_complete_dt(sysdate);
			performanceCheckMng.setDeploy_check_status_update_dt(sysdate);
			
			// 로그인사용자ID
			performanceCheckMng.setUser_id(user_id);
			performanceCheckMng.setPerf_check_completer_id(user_id);
			performanceCheckMng.setDeploy_check_status_updater_id(user_id);
			
			logger.debug("performanceCheckMng:" + performanceCheckMng);
			
			// 2. UPDATE DEPLOY_PERF_CHK_STEP_EXEC
			updateResult = performanceCheckMngDao.updateDeployPerfChkStepExec2(performanceCheckMng);
			logger.debug("updateResult1 :" + updateResult);
			
			// 3. UPDATE DEPLOY_PERF_CHK
			performanceCheckMng.setDeploy_check_status_cd("02");
			performanceCheckMng.setPerf_check_complete_meth_cd("2");//자동점검완료,자동완료
			performanceCheckMng.setPerf_check_result_div_cd("A");
			logger.debug("performanceCheckMng:" + performanceCheckMng);
			updateResult = performanceCheckMngDao.updateDeployPerfChk(performanceCheckMng);
			logger.debug("updateResult2 :" + updateResult);
			
			// 4. INSERT INTO DEPLOY_PERF_CHK_STATUS_HISTORY
			performanceCheckMng.setDeploy_check_status_chg_why("성능점검 자동 완료 처리");
			int insertResult = performanceCheckMngDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
			logger.debug("insertResult :" + insertResult);
			
			/* KBCard CM 호출 */
			/* 성능점검완료 결과 통보 */
			Thread thread1 = new NotiThread(performanceCheckMng);
			thread1.start();
			thread1.join(60000);
			if(thread1.isAlive()) {
				thread1.interrupt();
			}
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
	public PerformanceCheckMng getPerfCheckIdFromDeployId(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getPerfCheckIdFromDeployId(performanceCheckMng);
	}

	@Override
	public @ResponseBody Result perfChkRsltNoti(PerformanceCheckMng performanceCheckMng,  String pageName) throws Exception {
		Result result = new Result();
		String params = performanceCheckMng.getParams();
		logger.debug("params===>" + params);
		String params_array[] = params.split("\\|");
		logger.debug("params_array.length===>" + params_array.length);
		int totalCnt = params_array.length;
		successCnt = 0;
		failCnt = 0;

		for (String param : params_array) {
			String param_array[] = param.split("\\^");
			logger.debug("param_array.length===>" + param_array.length);
			logger.debug("deploy_id===>" + param_array[0]);
			logger.debug("perf_check_id===>" + param_array[1]);
			logger.debug("perf_check_result_div_cd===>" + param_array[2]);

			String deploy_id = param_array[0];
			String perf_check_id = param_array[1];
			String perf_check_result_div_cd = param_array[2];

			performanceCheckMng.setDeploy_id(deploy_id);
			performanceCheckMng.setPerf_check_id(perf_check_id);
			performanceCheckMng.setPerf_check_result_div_cd(perf_check_result_div_cd);
			/* KBCard CM 호출 */
			/* 성능점검완료 결과 통보 */
			Thread thread1 = new NotiThread(performanceCheckMng);
			thread1.start();
			thread1.join(60000);
			if(thread1.isAlive()) {
				thread1.interrupt();
			}
		}
		logger.debug("totalCnt :" + totalCnt);
		logger.debug("successCnt :" + successCnt);
		logger.debug("failCnt :" + failCnt);
		if (totalCnt != successCnt + failCnt) {
			failCnt = totalCnt - successCnt;
		}
		result.setResult(true);
		
		/*
		통보버튼을 클릭한 페이지 구분하는 조건식
		성능 점검 관리 페이지에서 클릭힌 경우 : pageName이 null
		성능 점검 결과 페이지에서 클릭한 경우 : pageName이 performanceCheckResult
		*/
		if(pageName == null) {
			result.setMessage("성능점검결과 통보중 총 " + totalCnt + "건 중 " + successCnt + "건이 성공하고 " + failCnt + "건이 실패하였습니다.");
			
		} else {
			if(successCnt >= 1 && failCnt == 0) {
				//성능 점검 결과 페이지에서 클릭 한 경우 + 성공한 경우(successCnt가 1이상이면서 failCnt가 0인 경우)
				result.setMessage("배포시스템에 점검결과통보가 성공했습니다.");
				
			} else{
				//성능 점검 결과 페이지에서 클릭 한 경우 + 실패한 경우(successCnt가 0이거나 failCnt가 0이 아닌 경우)
				result.setMessage("배포시스템에 점검결과통보가 실패했습니다.<br>점검결과통보를 다시 시도하세요.<br>계속 실패할 경우 관리자에게 문의바랍니다.");
			}
		}
		
		return result;
	}

	@Override
	public int getProgramCnt(PerformanceCheckMng performanceCheckMng) {
		return performanceCheckMngDao.getProgramCnt(performanceCheckMng);
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
			rtnMsg = sendResult2(performanceCheckMng);
			logger.debug("task1 run...rtnMsg after:" + rtnMsg);
			if (rtnMsg != null) {
				String ret_code = StringUtils.defaultString(rtnMsg.getRet_code());
				logger.debug("ret_code :"+ret_code);
				
				List<Status> ls = (List<Status>) rtnMsg.getRet_data();
				for (int i = 0; i < ls.size(); i++) {
					Status status = (Status) ls.get(i);
					String strCmid = StringUtils.defaultString(status.getCmid());
					String strStatus = StringUtils.defaultString(status.getStatus());
					System.out.println("Cmid ="+strCmid);
					System.out.println("Status =" + strStatus);
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

	@Override
	public int perfChkRequestTuning(PerformanceCheckMng performanceCheckMng, HttpServletRequest req) {
		
		String perfCheckId = performanceCheckMng.getPerf_check_id();
		String programId = performanceCheckMng.getProgram_id();
		logger.debug("perfCheckId :"+perfCheckId);
		logger.debug("programId :"+programId);

		TuningTargetSql tuningTargetSql = new TuningTargetSql();

		List<PerformanceCheckMng> bindVars = performanceCheckMngDao.selectBindVar(performanceCheckMng);
		StringBuilder bindVar = new StringBuilder();
		for(PerformanceCheckMng p : bindVars) {
			bindVar.append(p.getBind_var()+"\n");
		}
		String sql_desc = bindVar.toString();
		sql_desc = sql_desc.replace("${",":");
		sql_desc = sql_desc.replace("#{",":");
		sql_desc = sql_desc.replace("}","");
		
		tuningTargetSql.setSql_desc(sql_desc);

		int rowCnt = 0;
		
		// 1.TUNING_TARGET_SQL Next tuning no 조회
		String next_tuning_no = improvementManagementDao.getNextTuningNo();
		String dbid = performanceCheckMngDao.selectDbid(performanceCheckMng);

		String user_id = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getUser_id());

		String tuning_requester_id = user_id;
		String tuning_requester_wrkjob_cd = StringUtils.defaultString(performanceCheckMngDao.getUserWrkjob(user_id));

		// 현재수행시간
		String str_current_elap_time = "0";
		BigDecimal current_elap_time = new BigDecimal("0");
		// 예상결과건수
		String forecast_result_cnt = "0";
		
		PerformanceCheckMng deployPerfSqlStat = performanceCheckMngDao.selectDeployPerfSqlStat(performanceCheckMng);
		if(deployPerfSqlStat != null) {
			BigDecimal currentElapTime = deployPerfSqlStat.getCurrent_elap_time();
			if(currentElapTime != null) {
				current_elap_time = currentElapTime;
			}
			forecast_result_cnt = StringUtils.defaultString(deployPerfSqlStat.getForecast_result_cnt(),"0");
		}
		
		String parsing_schema_name = performanceCheckMngDao.selectParsingSchemaName(performanceCheckMng);


		PerformanceCheckMng perfCheckAllPgm = performanceCheckMngDao.getPerfCheckAllPgm(performanceCheckMng);

		String program_type_cd = StringUtils.defaultString(perfCheckAllPgm.getProgram_type_cd());
		String dbio = StringUtils.defaultString(perfCheckAllPgm.getDbio());
		String tr_cd = StringUtils.defaultString(perfCheckAllPgm.getTr_cd());
		String sql_text = StringUtils.defaultString(perfCheckAllPgm.getProgram_source_desc());

		logger.debug("=====================");
		logger.debug("sql_text before :\n"+sql_text);
		logger.debug("=====================");
		//sql_text에서 파라미터 구문을 변경한다.
		sql_text = sql_text.replace(":","¶");
		sql_text = sql_text.replace("${",":");
		sql_text = sql_text.replace("#{",":");
		sql_text = sql_text.replace("}","");

		sql_text = sql_text.replace("¶",":");
		tuningTargetSql.setSql_text(sql_text);
		logger.debug("=====================");
		logger.debug("sql_text after :\n"+sql_text);
		logger.debug("=====================");
		
		tuningTargetSql.setPerf_check_id(performanceCheckMng.getPerf_check_id());
		tuningTargetSql.setPerf_check_step_id(performanceCheckMng.getPerf_check_step_id());
		tuningTargetSql.setProgram_id(performanceCheckMng.getProgram_id());
		
		tuningTargetSql.setTuning_no(next_tuning_no);
		tuningTargetSql.setDbid(dbid);
		tuningTargetSql.setChoice_div_cd("B");//배포전성능점검(B)
		tuningTargetSql.setTuning_status_cd("2");//요청(2)
		tuningTargetSql.setTuning_requester_id(tuning_requester_id);
		tuningTargetSql.setTuning_requester_wrkjob_cd(tuning_requester_wrkjob_cd);
		//tuningTargetSql.setTuning_request_dt("SYSDATE");
		tuningTargetSql.setProgram_type_cd(program_type_cd);//온라인('1'), 배치('2')
		tuningTargetSql.setDbio(dbio);
		tuningTargetSql.setTr_cd(tr_cd);
		tuningTargetSql.setCurrent_elap_time(current_elap_time);
		tuningTargetSql.setForecast_result_cnt(forecast_result_cnt);
		tuningTargetSql.setParsing_schema_name(parsing_schema_name);
		
		String auth_nm = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getAuth_nm());

		tuningTargetSql.setTuning_status_change_why(auth_nm+"튜닝요청");
		tuningTargetSql.setTuning_status_changer_id(tuningTargetSql.getTuning_requester_id());
		
		// 2. TUNING_TARGET_SQL INSERT
		rowCnt = improvementManagementDao.insertTuningTargetSqlFromPerfChkResult(tuningTargetSql);

		// 3. SQL_TUNING_STATUS_HISTORY INSERT
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		// 4. 기존에 SQL BIND 값 DELETE
		improvementManagementDao.deleteTuningTargetSqlBind(next_tuning_no);
		
		String bindSetSeq = improvementManagementDao.getBindSetSeq(next_tuning_no);
		tuningTargetSql.setBind_set_seq(bindSetSeq);
		
		String programExecuteTms = StringUtils.defaultString(improvementManagementDao.getProgramExecuteTms(tuningTargetSql));
		tuningTargetSql.setProgram_execute_tms(programExecuteTms);
		
		if(!programExecuteTms.equals("")) {
			// 5. 새로운 SQL BIND 값 INSERT				
			improvementManagementDao.insertTuningTargetSqlBindFromPerfChkResult(tuningTargetSql);
		}
		
		return rowCnt;
	}

	@Override
	public int perfChkRequestTuningDupChk(PerformanceCheckMng performanceCheckMng) throws Exception {
		String perfCheckId = performanceCheckMng.getPerf_check_id();
		String programId = performanceCheckMng.getProgram_id();
		logger.debug("perfCheckId :"+perfCheckId);
		logger.debug("programId :"+programId);
		
		return performanceCheckMngDao.perfChkRequestTuningDupChk(performanceCheckMng);
	}

	@Override
	public PerformanceCheckMng getPagingYnCnt(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.getPagingYnCnt( performanceCheckMng );
	}
	
}
