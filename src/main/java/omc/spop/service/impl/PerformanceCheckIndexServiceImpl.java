package omc.spop.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import omc.spop.base.SessionManager;
import omc.spop.dao.PerformanceCheckAutoExecuteDao;
import omc.spop.dao.PerformanceCheckIndexDao;
import omc.spop.dao.PerformanceCheckMngDao;
import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunPlanTable;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.PerformanceCheckIndexService;
import omc.spop.utils.StringUtil;


@Service("PerformanceCheckIndexService")
public class PerformanceCheckIndexServiceImpl implements PerformanceCheckIndexService {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckIndexServiceImpl.class);

	@Autowired
	private PerformanceCheckIndexDao performanceCheckIndexDao;
	@Autowired
	private PerformanceCheckMngDao performanceCheckMngDao;
	@Autowired
	private PerformanceCheckAutoExecuteDao performanceCheckAutoExecuteDao;

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkIndc(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckMethCd(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckMethCd(deployPerfChkIndc);
	}

	@Override
	public int saveDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		int resultCnt = 0;
		if (deployPerfChkIndc.getCrud_flag().equals("C")) {
			check = performanceCheckIndexDao.insertDeployPerfChkIndc(deployPerfChkIndc);

		} else {// ??????
			if (!deployPerfChkIndc.getOld_perf_check_meth_cd().equals("") && !deployPerfChkIndc
					.getOld_perf_check_meth_cd().equals(deployPerfChkIndc.getPerf_check_meth_cd())) {
				check = performanceCheckIndexDao.checkDeployPerfChkDetailResult(deployPerfChkIndc);
			}

			if (deployPerfChkIndc.getOld_indc_use_yn().equals("Y") && deployPerfChkIndc.getIndc_use_yn().equals("N")) {
				resultCnt = performanceCheckIndexDao.checkDeployPerfChkDetailResult2(deployPerfChkIndc);
			}

			if (check == 0 && resultCnt == 0) {
				check = performanceCheckIndexDao.updateDeployPerfChkIndc(deployPerfChkIndc);
			} else if (check > 0) {
				throw new Exception("?????? ?????? ????????? ????????? ???????????? ?????? ????????? ???????????? <br/>[ ?????? ?????? ]??? ?????? ?????? ????????? ??? ????????????.");
			} else if (resultCnt > 0) {
				throw new Exception("?????? ?????? ????????? ????????? ????????? " + resultCnt
						+ "??? ???????????????. <br/>[????????? ?????? ?????? ?????? ??????] ???????????? [??????????????????]???<br/>??????????????? ?????? ??? ?????? ??????????????????");
			} else {
				return -1;
			}
		}
		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getDeployPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkIndcByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getWjPerfChkIndc(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckProgramDivCd(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckProgramDivCd(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getYnDecideDivCd(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getYnDecideDivCd(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckIndcId(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckIndcId(deployPerfChkIndc);
	}

	@Override
	public int saveWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		// check =
		// performanceCheckIndexDao.checkPkForWjPerfChkIndc(deployPerfChkIndc);

		if (deployPerfChkIndc.getCrud_flag().equals("C")) { // insert
			check = performanceCheckIndexDao.insertWjPerfChkIndc(deployPerfChkIndc);

		} else if (deployPerfChkIndc.getCrud_flag().equals("U")) { // update
			check += performanceCheckIndexDao.insertWjPerfChkIndcHistory(deployPerfChkIndc);
			if (check > 0) {
				check = performanceCheckIndexDao.updateWjPerfChkIndc(deployPerfChkIndc);
			} else {
				throw new Exception("???????????? ??????????????? ?????????????????????.");
			}

		} else {
			return -1;
		}
		return check;
	}

	@Override
	public int checkWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.checkWjPerfChkIndc(deployPerfChkIndc);
	}

	@Override
	public int deleteWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.deleteWjPerfChkIndc(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getWjPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getWjPerfChkIndcByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getProPerfExcReq(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		return performanceCheckIndexDao.getProPerfExcReq(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkStep(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkStepTestDB(deployPerfChkIndc);
	}

	@Override
	public int saveDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		int resultCnt = 0;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		check = performanceCheckIndexDao.checkStepOrderingCnt(deployPerfChkIndc);
		if (deployPerfChkIndc.getCrud_flag().equals("C")) {
			check = performanceCheckIndexDao.checkStepOrderingCnt(deployPerfChkIndc);
			if (check != 0) {
				throw new Exception("????????? ??????????????? ????????????.  <br/>??????????????? ??????????????? ???????????? ?????????.");
			} else {
				check = performanceCheckIndexDao.insertDeployPerfChkStep(deployPerfChkIndc);
			}

		} else if (deployPerfChkIndc.getCrud_flag().equals("U")) {

			if (!deployPerfChkIndc.getOld_step_ordering().equals(deployPerfChkIndc.getStep_ordering())) {
				check = performanceCheckIndexDao.checkStepOrderingCnt(deployPerfChkIndc);
				if (check != 0) {
					throw new Exception("????????? ??????????????? ????????????.  <br/>??????????????? ??????????????? ???????????? ?????????.");
				}
			}
			check = 0;
			if (deployPerfChkIndc.getDel_yn().equals("Y")) {
				check = performanceCheckIndexDao.checkDeployPerfChkCnt(deployPerfChkIndc);
			}
			if (deployPerfChkIndc.getOld_del_yn().equals("N") && deployPerfChkIndc.getDel_yn().equals("Y")) {
				resultMap = performanceCheckIndexDao.checkDeployPerfChkCnt2(deployPerfChkIndc);
				resultCnt = Integer.parseInt(String.valueOf(resultMap.get("WRKJOB_CD_TOTAL_CNT")));
			}

			if (check > 0) {
				throw new Exception("???????????? ?????? ?????? ????????? ???????????????. <br/> ?????? ?????? ?????? ?????? ?????? ????????? ????????? ??? ????????????.");
			} else if (resultCnt > 0) {
				throw new Exception("?????? ?????? ????????? ????????? ?????????<br/>" + "[????????? ?????? ?????? ?????? ??????]: "
						+ Integer.parseInt(String.valueOf(resultMap.get("WRKJOB_CD_PERF_CHK_STEP_CNT"))) + "??? <br/>"
						+ "[????????? ?????? ????????? ??????]: "
						+ Integer.parseInt(String.valueOf(resultMap.get("WRKJOB_CD_PARSING_SCHEMA_CNT")))
						+ "??? <br/>???????????????.");
			} else {
				check = performanceCheckIndexDao.updateDeployPerfChkStep(deployPerfChkIndc);
			}
		}

		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getDeployPerfChkStepByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkStepByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getBindTableList(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getBindTableList(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckResultTableList(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckResultTableList(deployPerfChkIndc);
	}

	// @Override
	// public int saveDeployPerfChkStepTestDB(DeployPerfChkIndc
	// deployPerfChkIndc) throws Exception {
	// int check = 0;
	//
	// if (deployPerfChkIndc.getCrud_flag().equals("C")) {
	// check =
	// performanceCheckIndexDao.checkDeployPerfChkStep(deployPerfChkIndc);
	// if(check == 0){
	// check =
	// performanceCheckIndexDao.insertDeployPerfChkStepTestDB(deployPerfChkIndc);
	// }else{
	// throw new Exception("????????? ????????? ????????? ??????????????? ???????????? ????????? ??? ????????????.");
	// }
	// } else { // ????????????
	// check =
	// performanceCheckIndexDao.updateDeployPerfChkStepTestDB(deployPerfChkIndc);
	// }
	// return check;
	// }
	@Override
	public int saveDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		if (deployPerfChkIndc.getCrud_flag().equals("C")) {
			check = performanceCheckIndexDao.checkDeployPerfChkStep(deployPerfChkIndc);
			if (check > 0) {
				throw new Exception("????????? ????????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.");
			} else {
				check = performanceCheckIndexDao.insertDeployPerfChkStepTestDB(deployPerfChkIndc);
			}

		} else { // ????????????

			if (deployPerfChkIndc.getOld_del_yn().equals("N") && deployPerfChkIndc.getDel_yn().equals("Y")) {
				check = performanceCheckIndexDao.checkDeployPerfChkCnt(deployPerfChkIndc);
				if (check > 0) {
					throw new Exception("???????????? ?????? ?????? ????????? ???????????????. <br/>?????? ?????? ????????? ????????? ??? ????????????.");
				} else {
					check = performanceCheckIndexDao.updateDeployPerfChkStepTestDB(deployPerfChkIndc);
				}
			} else {
				check = performanceCheckIndexDao.updateDeployPerfChkStepTestDB(deployPerfChkIndc);
			}
			// IF DEL_YN <> ???????????? ???????????? ????????? ??? THEN /* ??????????????? ???????????? ?????? */
			if (!deployPerfChkIndc.getDel_yn().equals(deployPerfChkIndc.getOld_del_yn())) {
				check = performanceCheckIndexDao.updateDeployPerfChkParsingSchemaAll(deployPerfChkIndc);
			}
		}
		return check;
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkStepId(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkStepId(deployPerfChkIndc);
	}

	@Transactional
	@Override
	public int cancelDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;

		// DEPLOY_PERF_CHK_EXCPT_HISTORY ????????????
		check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);
		if (check > 0) {
			check = 0;
			deployPerfChkIndc.setException_prc_status_cd("3"); /* ???????????? */
			check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest(deployPerfChkIndc);
		} else {
			throw new Exception("[????????????] ????????? ????????? ?????????????????????.");
		}

		return check;
	}

	// ???????????????????????? - ??????
	// ???????????????????????? - ??????
	@Override
	public int requestDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		String status_cd = StringUtils.defaultString(deployPerfChkIndc.getException_prc_status_cd());
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		deployPerfChkIndc.setUser_id(user_id);

		/* exception_prc_statuscd : 1:?????? 2:???????????? 3:???????????? 4:???????????? */
		if (status_cd.equals("") || status_cd.equals("2") || status_cd.equals("3") || status_cd.equals("4")) {

			deployPerfChkIndc.setException_prc_status_cd("1");// ??????
			deployPerfChkIndc.setExcepter_id("");
			deployPerfChkIndc.setException_prc_why("");
			deployPerfChkIndc.setException_prc_dt("");
			deployPerfChkIndc.setPerf_check_auto_pass_del_yn("N");
			if (deployPerfChkIndc.getException_requester_id() == null
					|| deployPerfChkIndc.getException_requester_id().equals("")) {
				deployPerfChkIndc.setException_requester_id(deployPerfChkIndc.getUser_id());
			}
			if (deployPerfChkIndc.getPerf_check_id() == null || deployPerfChkIndc.getPerf_check_id().equals("")) {
				deployPerfChkIndc.setPerf_check_id("");
			}
			if (deployPerfChkIndc.getLast_perf_check_step_id() == null
					|| deployPerfChkIndc.getLast_perf_check_step_id().equals("")) {
				deployPerfChkIndc.setLast_perf_check_step_id("");
			}
			if (deployPerfChkIndc.getProgram_execute_tms() == null
					|| deployPerfChkIndc.getProgram_execute_tms().equals("")) {
				deployPerfChkIndc.setProgram_execute_tms("");
			}
			
			check = performanceCheckIndexDao.insertDeployPerfChkExcptRequest(deployPerfChkIndc);

		} else if (status_cd.equals("1")) {
			throw new Exception("????????????????????? ?????? ?????? ????????? <br/>????????? ??? ??? ????????????.");
		} else {
			throw new Exception("???????????? ?????? ?????????????????????????????????.");
		}
		return check;
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkDetailResult(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkDetailResult(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTabEspc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkDetailResultByTabEspc(deployPerfChkIndc);
	}

	
	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTab(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkDetailResultByTab(deployPerfChkIndc);
	}

	@Transactional
	@Override
	public int exceptionHandling(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = -1;

		// ????????? AND exception_prc_meth_cd : 1: ????????????, 2:????????? ???????????? 3:????????? ????????????
		// ???????????? ????????? ????????????????????? ??????????????? ?????????????????????
		if (deployPerfChkIndc.getException_prc_status_cd().equals("1")) {
			String exception_prc_meth_cd = StringUtils.defaultString(deployPerfChkIndc.getException_prc_meth_cd());
			if (exception_prc_meth_cd.equals("1")) {
//				System.out.println("##########################[????????????]");

				check = 0;
				check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);
				if (check > 0) {
					check = 0;
					deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
					check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);
				}

				for (int i = 0; i < deployPerfChkIndc.getPerf_check_indc_id_chk().split(",").length; i++) {

					// ?????????????????? ????????? ?????????????????? ???.
					if (deployPerfChkIndc.getPerf_check_indc_id_chk().split(",").length - 1 < i) {
						deployPerfChkIndc.setPerf_check_indc_id("");
					} else {
						deployPerfChkIndc.setPerf_check_indc_id(
								StringUtils.defaultString(deployPerfChkIndc.getPerf_check_indc_id_chk().split(",")[i]));
					}
					if (deployPerfChkIndc.getChange_indc_pass_max_value_chk().split(",").length - 1 < i) {
						deployPerfChkIndc.setPass_max_value("");
					} else {
						deployPerfChkIndc.setPass_max_value(StringUtils
								.defaultString(deployPerfChkIndc.getChange_indc_pass_max_value_chk().split(",")[i]));
					}
					if (deployPerfChkIndc.getChange_yn_decide_div_cd_chk().split(",").length - 1 < i) {
						deployPerfChkIndc.setYn_decide_div_cd("");
					} else {
						deployPerfChkIndc.setYn_decide_div_cd(StringUtils
								.defaultString(deployPerfChkIndc.getChange_yn_decide_div_cd_chk().split(",")[i]));
					}
					
					check = performanceCheckIndexDao.deleteDpPerfChkIndcExcptHistory2(deployPerfChkIndc);
					
					// ???????????? ????????? INSERT????????? ??????
					deployPerfChkIndc.setDel_yn("N");
					check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory2(deployPerfChkIndc);
					check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt2(deployPerfChkIndc);
					// deployPerfChkIndc.setDel_yn("N");
					check = performanceCheckIndexDao.insertDeployPerfChkIndcExcpt2(deployPerfChkIndc);
				}
			} else if (exception_prc_meth_cd.equals("2")) {
//				System.out.println("##########################[??????????????????]");
				check = 0;
				check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

				deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
				check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);

				deployPerfChkIndc.setPerf_check_auto_pass_yn("Y");
				check = performanceCheckIndexDao.updateDeployPerfChkAllPgm(deployPerfChkIndc);

				// deployPerfChkIndc.setPerf_check_auto_pass_yn("Y");
				check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm(deployPerfChkIndc);

				check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory(deployPerfChkIndc);

				check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt(deployPerfChkIndc);
	
				// deployPerfChkIndc.setPerf_check_auto_pass_yn("Y"); //???????????????
				// ??????????????? ?????????????????? ?????? Y ??? ???????????? ????????????. ????????????????????? N?????? ????????????.
				// check =
				// performanceCheckIndexDao.updateDeployPerfChkTargetPgm(deployPerfChkIndc);

				// ????????? ????????????
			} else if (exception_prc_meth_cd.equals("3")) {
//				System.out.println("##########################[??????????????????]");
				check = 0;
				check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

				deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
				check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);

				// ???????????????????????? ??????????????? ????????? ?????????????
				// deployPerfChkIndc.setPerf_check_auto_pass_yn("Y");
				// check =
				// performanceCheckIndexDao.updateDeployPerfChkAllPgm(deployPerfChkIndc);

				check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm(deployPerfChkIndc);

				check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory(deployPerfChkIndc);

				check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt(deployPerfChkIndc);
				// ????????????
			} else {
				throw new Exception("???????????? ?????? ?????????????????????????????????.");
			}
		} else {
			throw new Exception("???????????? ?????? ?????????????????????????????????.");
		}
		return check;
	}

	public Result perfCheckExecute(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		DeployPerfChkIndc deployPerfChkIndc1 = performanceCheckAutoExecuteDao
				.selectDeployPerfChkInfo(deployPerfChkIndc);
		String perf_check_step_id = deployPerfChkIndc.getPerf_check_step_id();
		/* ?????????????????????????????? */
		String deploy_check_status_cd = deployPerfChkIndc1.getDeploy_check_status_cd();
		/* ??????????????????????????? */
		String deploy_check_status_nm = deployPerfChkIndc1.getDeploy_check_status_nm();
		/* ????????????????????????ID */
		String last_perf_check_step_id = deployPerfChkIndc1.getLast_perf_check_step_id();
		logger.debug("perf_check_step_id:" + perf_check_step_id);
		logger.debug("deploy_check_status_cd:" + deploy_check_status_cd);
		logger.debug("last_perf_check_step_id:" + last_perf_check_step_id);

		Result result = new Result();
		if (!deploy_check_status_cd.equals("01")) {
			result.setResult(false);
			result.setMessage("?????? ???????????? ?????? ????????? " + deploy_check_status_nm + "????????? ?????? ?????? ??? ????????? ??? ????????????.");
			result.setResultCount(0);
		} else if (!perf_check_step_id.equals(last_perf_check_step_id)) {
			result.setResult(false);
			result.setMessage("??????????????? ?????? ?????? ????????? ???????????? ?????? ???????????? ?????? ?????? ??? ????????? ??? ????????????.");
			result.setResultCount(0);
		} else {
			int maxProgramExecuteTms = performanceCheckAutoExecuteDao.selectMaxProgramExecuteTms(deployPerfChkIndc);
			deployPerfChkIndc.setProgram_execute_tms(maxProgramExecuteTms + "");

			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			deployPerfChkIndc.setProgram_executer_id(user_id);
			
			PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
			
			performanceCheckMng.setPerf_check_id( deployPerfChkIndc.getPerf_check_id() );
			performanceCheckMng.setPerf_check_step_id( deployPerfChkIndc.getPerf_check_step_id() );
			performanceCheckMng.setProgram_id( deployPerfChkIndc.getProgram_id() );
			performanceCheckMng.setProgram_execute_tms( deployPerfChkIndc.getProgram_execute_tms() );
			performanceCheckMng.setProgram_executer_id( user_id );
			performanceCheckMng.setProgramExecuteTms( Integer.valueOf( deployPerfChkIndc.getProgram_execute_tms() ) );
			
			PerformanceCheckMng performanceCheckMng2 = performanceCheckMngDao.getPagingYnCnt(performanceCheckMng);
			
			if ( performanceCheckMng2 != null && performanceCheckMng2.getPaging_yn() != null ) {
				deployPerfChkIndc.setPagingYn( performanceCheckMng2.getPaging_yn() );
			} else {
				deployPerfChkIndc.setPagingYn( "N" );
			}
			
			if ( performanceCheckMng2 != null && performanceCheckMng2.getPaging_cnt() != null ) {
				deployPerfChkIndc.setPagingCnt( performanceCheckMng2.getPaging_cnt() );
			} else {
				deployPerfChkIndc.setPagingCnt( "0" );
			}
			
			int insertDeployPerfChkResult = performanceCheckAutoExecuteDao.insertDeployPerfChkResult(deployPerfChkIndc);
//			int insertDeployPerfChkResult = performanceCheckMngDao.insertDeployPerfChkResult( performanceCheckMng );
			
			logger.debug("insertDeployPerfChkResult:" + insertDeployPerfChkResult);
			int insertDeployPerfChkExecBindResult = performanceCheckAutoExecuteDao
					.insertDeployPerfChkExecBind(deployPerfChkIndc);
			logger.debug("insertDeployPerfChkExecBindResult:" + insertDeployPerfChkExecBindResult);

			result.setResult(true);
			result.setMessage("?????? ?????? ????????? ?????????????????????.");
			result.setResultCount(insertDeployPerfChkExecBindResult);
				
			// ???????????? ?????? ????????? ?????????????????? ??????????????? ??? ?????? ???....
			// ????????? ?????? ??????
			// SQL Runner...
			try {
				String parsingSchemaName = performanceCheckAutoExecuteDao.selectParsingSchemaName(deployPerfChkIndc);
				deployPerfChkIndc.setParsing_schema_name(parsingSchemaName);

				String strDbId = deployPerfChkIndc.getDbid();
				String strPerfCheckId = deployPerfChkIndc.getPerf_check_id();
				String strPerfCheckStepId = deployPerfChkIndc.getPerf_check_step_id();
				String strProgramId = deployPerfChkIndc.getProgram_id();
				String strProgramExecuteTms = deployPerfChkIndc.getProgram_execute_tms();
				String strParsingSchema = deployPerfChkIndc.getParsing_schema_name();
				logger.debug("strDbId :" + strDbId);
				logger.debug("strPerfCheckId :" + strPerfCheckId);
				logger.debug("strPerfCheckStepId :" + strPerfCheckStepId);
				logger.debug("strProgramId :" + strProgramId);
				logger.debug("strProgramExecuteTms :" + strProgramExecuteTms);
				logger.debug("strParsingSchema :" + strParsingSchema);

				Long dbId = Long.parseLong(deployPerfChkIndc.getDbid());
				Long perfCheckId = Long.parseLong(deployPerfChkIndc.getPerf_check_id());
				Long perfCheckStepId = Long.parseLong(deployPerfChkIndc.getPerf_check_step_id());
				Long programId = Long.parseLong(deployPerfChkIndc.getProgram_id());
				Long programExecuteTms = Long.parseLong(deployPerfChkIndc.getProgram_execute_tms()) + 1;
				String parsingSchema = deployPerfChkIndc.getParsing_schema_name();
				String pagingYn =  deployPerfChkIndc.getPagingYn();
				Long pagingCnt = Long.parseLong( deployPerfChkIndc.getPagingCnt() );
				logger.debug("dbId :" + dbId);
				logger.debug("perfCheckId :" + perfCheckId);
				logger.debug("perfCheckStepId :" + perfCheckStepId);
				logger.debug("programId :" + programId);
				logger.debug("programExecuteTms :" + programExecuteTms);
				logger.debug("parsingSchema :" + parsingSchema);
				logger.debug("pagingYn :" + pagingYn);
				logger.debug("pagingCnt :" + pagingCnt);
				
				String getPerfChkResult = DeployPerfChk.getPerfChk(dbId, perfCheckId, perfCheckStepId, programId,
						programExecuteTms, parsingSchema, pagingYn, pagingCnt);
				
				logger.debug("getPerfChkResult :" + getPerfChkResult);
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonResult = (JSONObject) jsonParser.parse(getPerfChkResult);
				logger.debug("jsonResult :" + jsonResult);
				logger.debug("jsonResult.toString() :" + jsonResult.toString());

				String isError = (String) jsonResult.get("is_error");
				if (isError.equals("true")) {
					throw new Exception((String) jsonResult.get("err_msg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception msg :" + e.getMessage());
				throw e;
			}
		}
		return result;
	}

	@Override
	public int rejectRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
		if (deployPerfChkIndc.getException_prc_status_cd().equals("1")) {
			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);
			if (check > 0) {
				check = 0;
				deployPerfChkIndc.setException_prc_status_cd("4"); // ????????????
				check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);
			}
		} else {
			throw new Exception("?????? ??????????????? ???????????? ?????? ???????????? ????????? ???????????????.");
		}
		return check;
	}

	@Override
	public int checkExceptionRequestCnt(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.checkExceptionRequestCnt(deployPerfChkIndc);
	}

	@Override
	public String getProgramSourceDesc(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProgramSourceDesc(deployPerfChkIndc);
	}

	@Override
	public DeployPerfChkIndc checkExceptionPrcStatusCd(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.checkExceptionPrcStatusCd(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getProPerfExcReqDel(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProPerfExcReqDel(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckResultDelTableList(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckResultDelTableList(deployPerfChkIndc);
	}

	@Override
	public int exceptionDelete(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = -1;

		if (deployPerfChkIndc.getException_prc_status_cd().equals("1")
				&& deployPerfChkIndc.getPerf_check_auto_pass_yn().equals("Y")
				&& deployPerfChkIndc.getException_prc_meth_cd().equals("2")) {
//			System.out.println("##########################?????? ????????????#######################");
			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

			deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
			check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest3(deployPerfChkIndc);

			deployPerfChkIndc.setPerf_check_auto_pass_yn("N");
			check = performanceCheckIndexDao.updateDeployPerfChkAllPgm(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm2(deployPerfChkIndc);

		} else if (deployPerfChkIndc.getException_prc_status_cd().equals("1")
				&& deployPerfChkIndc.getPerf_check_auto_pass_yn().equals("N")
				&& deployPerfChkIndc.getException_prc_meth_cd().equals("3")) {
//			System.out.println("##########################?????? ????????????#######################");
			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

			deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
			check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest3(deployPerfChkIndc);

			// ??????
			// deployPerfChkIndc.setPerf_check_auto_pass_yn("N");
			// check =
			// performanceCheckIndexDao.updateDeployPerfChkAllPgm(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm2(deployPerfChkIndc);
		} else if (deployPerfChkIndc.getException_prc_status_cd().equals("1")
				&& deployPerfChkIndc.getExcept_reg_yn().equals("Y")
				&& deployPerfChkIndc.getException_prc_meth_cd().equals("1")) {
//			System.out.println("##########################????????????#######################");

			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);
			if (check > 0) {
				check = 0;
				deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
				check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);
			}

			for (int i = 0; i < deployPerfChkIndc.getPerf_check_indc_id_chk().split(",").length; i++) {

				// ?????????????????? ????????? ?????????????????? ???.
				if (deployPerfChkIndc.getPerf_check_indc_id_chk().split(",").length - 1 < i) {
					deployPerfChkIndc.setPerf_check_indc_id("");
				} else {
					deployPerfChkIndc.setPerf_check_indc_id(
							StringUtils.defaultString(deployPerfChkIndc.getPerf_check_indc_id_chk().split(",")[i]));
				}
				if (deployPerfChkIndc.getExcpt_pass_max_value_chk().split(",").length - 1 < i) {
					deployPerfChkIndc.setPass_max_value("");
				} else {
					deployPerfChkIndc.setPass_max_value(
							StringUtils.defaultString(deployPerfChkIndc.getExcpt_pass_max_value_chk().split(",")[i]));
				}
				if (deployPerfChkIndc.getExcpt_yn_decide_div_cd_chk().split(",").length - 1 < i) {
					deployPerfChkIndc.setYn_decide_div_cd("");
				} else {
					deployPerfChkIndc.setYn_decide_div_cd(
							StringUtils.defaultString(deployPerfChkIndc.getExcpt_yn_decide_div_cd_chk().split(",")[i]));
				}
				
				check = performanceCheckIndexDao.deleteDpPerfChkIndcExcptHistory2(deployPerfChkIndc);
				
				// ???????????? ????????? INSERT????????? ??????
				deployPerfChkIndc.setDel_yn("Y");
				check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory2(deployPerfChkIndc);
				check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt2(deployPerfChkIndc);
				check = performanceCheckIndexDao.insertDeployPerfChkIndcExcpt2(deployPerfChkIndc);
			}

		} else {
			throw new Exception("???????????? ?????? ?????????????????????????????????.");
		}

		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getProPerfExcReqByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProPerfExcReqByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckStep(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckStep(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getProPerfExcReqDelByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProPerfExcReqDelByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkParsingSchema(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getDeployPerfChkParsingSchemaByExcelDown(
			DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkParsingSchemaByExcelDown(deployPerfChkIndc);
	}

	@Override
	public int saveDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc) throws Exception {
		int check = 0;
//		System.out.println("###### ?????? ??????????????? ??? 0" + deployPerfChkIndc.getAcceptUpdate() + "/"
//				+ deployPerfChkIndc.getWrkjob_cd() + "/" + deployPerfChkIndc.getPerf_check_step_id() + "/"
//				+ deployPerfChkIndc.getDbid() + "/" + deployPerfChkIndc.getParsing_schema_name());
		if (deployPerfChkIndc.getCrud_flag().equals("C")) {
			check = performanceCheckIndexDao.checkDeployPerfChkParsingSchemaPk(deployPerfChkIndc);
			if (check > 0) {
				throw new Exception("????????? ????????? ????????? ?????? ????????? ???????????? ????????? ??? ????????????.");
			} else {
				check = performanceCheckIndexDao.insertDeployPerfChkParsingSchema(deployPerfChkIndc);
			}

		} else { // ????????????

			System.out
					.println(":::::::::::??" + deployPerfChkIndc.getOld_del_yn() + "/" + deployPerfChkIndc.getDel_yn());
			if (deployPerfChkIndc.getOld_del_yn().equals("N") && deployPerfChkIndc.getDel_yn().equals("Y")) {
				System.out.println("???????????????????????");
				check = performanceCheckIndexDao.checkDeployPerfChkParsingSchemaName(deployPerfChkIndc);
				if (check > 0) {
					throw new Exception("???????????? ?????? ?????? ????????? ???????????????. ?????? ?????? ?????? ?????? ?????? ????????? ????????? ??? ????????????.");
				}
			}
			// (1)
			if (!deployPerfChkIndc.getAcceptUpdate().equals("Y") && deployPerfChkIndc.getOld_del_yn().equals("Y")
					&& deployPerfChkIndc.getDel_yn().equals("N")) {
				check = performanceCheckIndexDao.checkDeployPerfChkStepDelYnCnt(deployPerfChkIndc);
				if (check > 0) {
					throw new Exception(
							"?????? ????????? ?????? ????????? ???????????? [????????????]??? 'N'(???)??? ????????? ??? ????????????. ?????? ????????? ????????? ?????? [????????? ?????? ?????? ?????? ??????] ???????????? ?????? ????????????.");
				}
			}
			// (2)
			// deployPerfChkIndc.getAcceptUpdate() == Y -> ?????????????????? ???????????? ???????????? ?????????.
			// ?????????????????? ?????? ????????? ?????????. ??????????????? ?????? X ????????????
			if (!deployPerfChkIndc.getAcceptUpdate().equals("Y") && !deployPerfChkIndc.getParsing_schema_name()
					.equals(deployPerfChkIndc.getOld_parsing_schema_name())) {
				check = 0;
				check = performanceCheckIndexDao.checkDeployPerfChkParsingSchemaName(deployPerfChkIndc);
				if (check > 0) {
					return 99; // ???????????? ???????????? ????????? ?????????????????? ???????????? ????????????????????? ????????????.
				}
			}

			// deployPerfChkIndc.getAcceptUpdate() == 'Y' ????????? (1),(2) ???????????? ?????? ?????? ????????????
			check = performanceCheckIndexDao.updateDeployPerfChkParsingSchema(deployPerfChkIndc);
		}
		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getDeployPerfChkStepTestDBByExcelDown(
			DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkStepTestDBByExcelDown(deployPerfChkIndc);
	}

	@Override
	public int checkDeployPerfChkParsingSchemaName(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.checkDeployPerfChkParsingSchemaName(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfChkIndcListState(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfChkIndcListState(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfChkIndcListState2(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfChkIndcListState2(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfChkIndcListState2ByExcelDown(
			DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfChkIndcListState2ByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfChkIndcListStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfChkIndcListStateByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getProPerfExcReqState(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProPerfExcReqState(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckResultStateTableList(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckResultStateTableList(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getProPerfExcReqStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getProPerfExcReqStateByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckState0(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState0(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckState1(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState1(deployPerfChkIndc);
	}

	@Override
	public List<DeployPerfChkIndc> getPerfCheckState2(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState2(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfCheckState0ByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState0ByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfCheckState1ByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState1ByExcelDown(deployPerfChkIndc);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getPerfCheckState2ByExcelDown(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getPerfCheckState2ByExcelDown(deployPerfChkIndc);
	}

	@Override
	public DeployPerfChkIndc checkExceptionPrcStatusCd2(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.checkExceptionPrcStatusCd2(deployPerfChkIndc);
	}

	@Override
	public int multiRequestDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc) {
		int check = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		deployPerfChkIndc.setUser_id(user_id);

		if (deployPerfChkIndc.getException_requester_id() == null
				|| deployPerfChkIndc.getException_requester_id().equals("")) {
			deployPerfChkIndc.setException_requester_id(deployPerfChkIndc.getUser_id());
		}
		check = performanceCheckIndexDao.insertDeployPerfChkExcptRequest(deployPerfChkIndc);
		return check;
	}

	@Override
	public int multiExceptionHandling(DeployPerfChkIndc deployPerfChkIndc) {

		int check = 0;
//		System.out.println("######[A]");
		if (deployPerfChkIndc.getException_prc_meth_cd().equals("2")) {
//			 System.out.println("######[??????]");
			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

			deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
			check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);

			deployPerfChkIndc.setPerf_check_auto_pass_yn("Y");
			check = performanceCheckIndexDao.updateDeployPerfChkAllPgm(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm(deployPerfChkIndc);

			check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt(deployPerfChkIndc);

			// ????????? ????????????
		} else if (deployPerfChkIndc.getException_prc_meth_cd().equals("3")) {
//				System.out.println("######[??????]");
			check = 0;
			check = performanceCheckIndexDao.insertDeployPerfChkExcptHistory(deployPerfChkIndc);

			deployPerfChkIndc.setException_prc_status_cd("2"); // ????????????
			check = performanceCheckIndexDao.updateDeployPerfChkExcptRequest2(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkTargetPgm(deployPerfChkIndc);

			check = performanceCheckIndexDao.insertDpPerfChkIndcExcptHistory(deployPerfChkIndc);

			check = performanceCheckIndexDao.updateDeployPerfChkIndcExcpt(deployPerfChkIndc);

		}

		return check;
	}

	@Override
	public List<SelftunPlanTable> getDeployPerfChkPlanTableList(DeployPerfChkIndc deployPerfChkIndc) {
		return performanceCheckIndexDao.getDeployPerfChkPlanTableList(deployPerfChkIndc);
	}
	
}
