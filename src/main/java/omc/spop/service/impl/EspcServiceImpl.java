package omc.spop.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.IqmsDao;
import omc.spop.dao.PerformanceCheckMngDao;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Status;
import omc.spop.model.server.Iqms;
import omc.spop.server.tools.SPopTools;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.EspcService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.SysUtil;
import omc.spop.utils.Util;

/***********************************************************
 * * 2019.01.17
 **********************************************************/
@Service("espcService")
public class EspcServiceImpl implements EspcService {
	private static final Logger logger = LoggerFactory.getLogger(EspcServiceImpl.class);
	
	@Autowired
	private IqmsDao iqmsDao;
	
	@Autowired
	private PerformanceCheckMngDao performanceCheckMngDao;
	
	/**
	 * /*************************************************************** 인터페이스명 :
	 * 개발확정
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - cmpknm : 배포명(DEPLOY_NM) - cmdepday :
	 * 배포예정일자(DEPLOY_EXPECTED_DAY) - cmusrnum : 배포요청자ID(DEPLOY_REQUESTER_ID) - jobcd
	 * : 배포업무분류코드(DEPLOY_WRKJOB_DIV_CD) - cmcreateday : 배포요청일시(DEPLOY_REQUEST_DT)
	 ***************************************************************/
	/**
	 * GRP_CD_ID CD CD_NM 1056 00 개발확정 1056 01 점검중 1056 02 점검완료 1056 03 배포완료 1056 04
	 * 개발확정취소 1056 05 배포삭제
	 */
	public Iqms cmConfirm(Iqms iqms) throws Exception {
		String perfCheckId = "";
		String cmid = "";
		String cmpknm = "";
		String cmdepday = "";
		String cmusrnum = "";
		String jobcd = "";
		String cmcreateday = "";
		PerformanceCheckMng performanceCheckMng = null;
		
		try {
			// Master가 기동되었는지 체크하여 기동되지 않은 경우 에러를 던지고 수행을 중단한다.
			boolean ping = SysUtil.pingTimeTCPClient(SPopTools.masterIP, SPopTools.dmPort);
			if(ping) {
				logger.debug("Master Available");
			} else {
				logger.debug("Master Unavailable");
				throw new java.net.ConnectException("Master Unavailable");
			}
			
			cmid = iqms.getCmid();
			cmpknm = iqms.getCmpknm();
			cmdepday = iqms.getCmdepday();
			cmusrnum = iqms.getCmusrnum();
			jobcd = iqms.getJobcd();
			cmcreateday = iqms.getCmcreateday();
			
			performanceCheckMng = new PerformanceCheckMng();
			performanceCheckMng.setDeploy_id(cmid);
			performanceCheckMng.setDeploy_nm(cmpknm);
			performanceCheckMng.setDeploy_expected_day(cmdepday);
			performanceCheckMng.setDeploy_requester_id(cmusrnum);
			performanceCheckMng.setDeploy_wrkjob_div_cd(jobcd);
			performanceCheckMng.setDeploy_request_dt(cmcreateday);
			performanceCheckMng.setWrkjob_div_cd(jobcd);
			
			/** 1 : PERF_CHECK_ID 채번 (5 -> 1 순서변경) */
			perfCheckId = iqmsDao.selectNextPerfCheckId();
			logger.debug("selectNextPerfCheckId :" + perfCheckId);
			performanceCheckMng.setPerf_check_id(perfCheckId);
			iqms.setPerfcheckid(perfCheckId);
			
			/** 2 : 기업 업무코드로 OPENPOP 업무코드 조회 */
			PerformanceCheckMng PerformanceCheckMng1 = iqmsDao.selectWrkjobCd(performanceCheckMng);
			logger.debug("PerformanceCheckMng1===>" + PerformanceCheckMng1);
			performanceCheckMng.setWrkjob_cd(PerformanceCheckMng1.getWrkjob_cd());
			performanceCheckMng.setTop_wrkjob_cd(PerformanceCheckMng1.getTop_wrkjob_cd());
			
			/** 3 : 이전 배포ID(CMID)가 존재하면 최종성능점검단계ID로 업데이트 */
			String beforePerfCheckId = StringUtils.defaultString(iqmsDao.selectBeforePerfCheckId(performanceCheckMng));
			performanceCheckMng.setBefore_perf_check_id(beforePerfCheckId);
			
			/** 4 : 이전에 개발확정/점검중/점검완료상태의 이전 배포ID(CMID)가 존재하면 개발확정취소(04) 한다. */
			/** 4.1 : 성능점검 상태 변경 이력 생성 */
			int insertIntoSelectDeployPerfChkStatusHistory = iqmsDao
					.insertIntoSelectDeployPerfChkStatusHistory(performanceCheckMng);
			logger.debug("insertIntoSelectDeployPerfChkStatusHistory :" + insertIntoSelectDeployPerfChkStatusHistory);
			
			/** 4.2 : 개발확정취소로 업데이트 */
			int updateDeployPerfChkCancel = iqmsDao.updateDeployPerfChkCancel(performanceCheckMng);
			logger.debug("updateDeployPerfChkCancel :" + updateDeployPerfChkCancel);
			
			/** 5 : 최종성능점검단계ID 조회 */
			String selectLastPerfCheckStepId = iqmsDao.selectLastPerfCheckStepId(performanceCheckMng);
			logger.debug("selectLastPerfCheckStepId :" + selectLastPerfCheckStepId);
			performanceCheckMng.setLast_perf_check_step_id(selectLastPerfCheckStepId);
			
			/** 6 : 변수 세팅 */
			/**
			 * 배포성능점검상태코드 DEPLOY_CHECK_STATUS_CD = '00'; 개발확정
			 */
			/**
			 * 배포성능상태변경자ID DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
			 */
			/**
			 * 배포성능점검상태변경사유 DEPLOY_CHECK_STATUS_CHG_WHY = '배포요청';
			 */
			/**
			 * 성능점검요청유형코드 PERF_CHECK_REQUEST_TYPE_CD = 'A'; 일반
			 */
			/**
			 * 성능점검요청채널구분코드 PERF_CHECK_REQUEST_CN_DIV_CD = '01'; 배포시스템(CM)
			 */
			/**
			 * 에이전트상태구분코드 AGENT_STATUS_DIV_CD = '1'; 대기
			 */
			performanceCheckMng.setDeploy_check_status_cd("00");
			performanceCheckMng.setDeploy_check_status_updater_id("dbmanager");
			performanceCheckMng.setDeploy_check_status_chg_why("배포요청");
			performanceCheckMng.setPerf_check_request_type_cd("A");
			performanceCheckMng.setPerf_check_request_cn_div_cd("01");
			performanceCheckMng.setAgent_status_div_cd("1");
			
			/** 7 : 배포성능점검기본(DEPLOY_PERF_CHK) INSERT */
			int insertDeployPerfChk = iqmsDao.insertDeployPerfChk(performanceCheckMng);
			logger.debug("insertDeployPerfChk :" + insertDeployPerfChk);
			
			/** 8 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT */
			int insertDeployPerfChkStatusHistory = iqmsDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
			logger.debug("insertDeployPerfChkStatusHistory :" + insertDeployPerfChkStatusHistory);
			
			/** 9 : 배포성능점검단계별수행내역(DEPLOY_PERF_CHK_STEP_EXEC) INSERT */
			int insertDeployPerfChkStepExec = iqmsDao.insertDeployPerfChkStepExecEspc(performanceCheckMng);
			logger.debug("insertDeployPerfChkStepExec :" + insertDeployPerfChkStepExec);
			
			/** 10. 마스터 서버호출  (10 -> 2 --> 10  순서변경) */
			// 개발확정 프로세스를 진행하기전에 소스 다운로드 진행
			// Master 가 죽었거나 통신이 안되는 경우 개발확정 프로세스를 진행하지 않기 위함
			DeployPerfChk.execSqlPerfCheck(iqms, "1", perfCheckId, null);
			
			iqms.setResult(1);
		} catch (java.net.SocketTimeoutException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (java.net.ConnectException e2) {
			e2.printStackTrace();
			throw e2;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return iqms;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM완료
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public int cmComplete_old_2019701(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회 */
		String selectMaxPerfCheckId = StringUtils.defaultString(iqmsDao.selectMaxPerfCheckId(performanceCheckMng));
		if ("".equals(selectMaxPerfCheckId)) {
			return 0;
		}
		performanceCheckMng.setPerf_check_id(selectMaxPerfCheckId);
		
		/** 2 : 변수 세팅 */
		/**
		 * 배포성능점검상태코드 DEPLOY_CHECK_STATUS_CD = '03'; 배포완료
		 */
		/**
		 * 배포성능상태변경자ID DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
		 */
		/**
		 * 배포성능점검상태변경사유 DEPLOY_CHECK_STATUS_CHG_WHY = '배포완료';
		 */
		
		performanceCheckMng.setDeploy_check_status_cd("03");
		performanceCheckMng.setDeploy_check_status_updater_id("dbmanager");
		performanceCheckMng.setDeploy_check_status_chg_why("배포완료");
		
		/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE */
		int updateDeployPerfChk = iqmsDao.updateDeployPerfChk(performanceCheckMng);
		logger.debug("updateDeployPerfChk :" + updateDeployPerfChk);
		
		/** 7 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT */
		int insertDeployPerfChkStatusHistory = iqmsDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
		logger.debug("insertDeployPerfChkStatusHistory :" + insertDeployPerfChkStatusHistory);
		return insertDeployPerfChkStatusHistory;
	}
	/***************************************************************
	 * 인터페이스명 : CM완료
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public Iqms cmComplete(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		String result = iqmsDao.callSpSpopDeployStatusProc(performanceCheckMng);
		if(result == null) {
			//return 1;
			iqms.setResult(1);
		}else {
			//return 0;
			iqms.setResult(0);
		}		
		return iqms;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM취소
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public int cmCancel_old_20190701(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회 */
		String selectMaxPerfCheckId = StringUtils.defaultString(iqmsDao.selectMaxPerfCheckId(performanceCheckMng));
		if ("".equals(selectMaxPerfCheckId)) {
			return 0;
		}
		performanceCheckMng.setPerf_check_id(selectMaxPerfCheckId);
		
		/** 2 : 변수 세팅 */
		/**
		 * 배포성능점검상태코드 DEPLOY_CHECK_STATUS_CD = '04'; 배포완료
		 */
		/**
		 * 배포성능상태변경자ID DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
		 */
		/**
		 * 배포성능점검상태변경사유 DEPLOY_CHECK_STATUS_CHG_WHY = '배포취소';
		 */
		
		performanceCheckMng.setDeploy_check_status_cd("04");
		performanceCheckMng.setDeploy_check_status_updater_id("dbmanager");
		performanceCheckMng.setDeploy_check_status_chg_why("배포취소");
		
		/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE */
		int updateDeployPerfChk = iqmsDao.updateDeployPerfChk(performanceCheckMng);
		logger.debug("updateDeployPerfChk :" + updateDeployPerfChk);
		
		/** 7 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT */
		int insertDeployPerfChkStatusHistory = iqmsDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
		logger.debug("insertDeployPerfChkStatusHistory :" + insertDeployPerfChkStatusHistory);
		
		/**
		 * 2019-06-13 로직 추가 5 : SQL성능점검대상 프로그램 삭제처리함(다음 배포시에 성능점검대상으로 올라와야 함)
		 */
		int updateDeployPerfChkAllPgmDelYnResult = iqmsDao.updateDeployPerfChkAllPgmDelYn(performanceCheckMng);
		logger.debug("updateDeployPerfChkAllPgmDelYnResult :" + updateDeployPerfChkAllPgmDelYnResult);
		
		return insertDeployPerfChkStatusHistory;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM취소
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public Iqms cmCancel(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		logger.debug("cmid :"+cmid);
		logger.debug("status :"+status);
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		String result = iqmsDao.callSpSpopDeployStatusProc(performanceCheckMng);
		
		if(result == null) {
			//return 1;
			iqms.setResult(1);
		}else {
			//return 0;
			iqms.setResult(0);
		}
		
		return iqms;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM삭제
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public int cmDelete_old_20190701(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회 */
		String selectMaxPerfCheckId = StringUtils.defaultString(iqmsDao.selectMaxPerfCheckId(performanceCheckMng));
		if ("".equals(selectMaxPerfCheckId)) {
			return 0;
		}
		performanceCheckMng.setPerf_check_id(selectMaxPerfCheckId);
		
		/** 2 : 변수 세팅 */
		/**
		 * 배포성능점검상태코드 DEPLOY_CHECK_STATUS_CD = '05'; 배포삭제
		 */
		/**
		 * 배포성능상태변경자ID DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
		 */
		/**
		 * 배포성능점검상태변경사유 DEPLOY_CHECK_STATUS_CHG_WHY = '배포삭제';
		 */
		
		performanceCheckMng.setDeploy_check_status_cd("05");
		performanceCheckMng.setDeploy_check_status_updater_id("dbmanager");
		performanceCheckMng.setDeploy_check_status_chg_why("배포삭제");
		
		/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE */
		int updateDeployPerfChk = iqmsDao.updateDeployPerfChk(performanceCheckMng);
		logger.debug("updateDeployPerfChk :" + updateDeployPerfChk);
		
		/** 7 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT */
		int insertDeployPerfChkStatusHistory = iqmsDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
		logger.debug("insertDeployPerfChkStatusHistory :" + insertDeployPerfChkStatusHistory);
		/**
		 * 2019-06-13 로직 추가 5 : SQL성능점검대상 프로그램 삭제처리함(다음 배포시에 성능점검대상으로 올라와야 함)
		 */
		int updateDeployPerfChkAllPgmDelYnResult = iqmsDao.updateDeployPerfChkAllPgmDelYn(performanceCheckMng);
		logger.debug("updateDeployPerfChkAllPgmDelYnResult :" + updateDeployPerfChkAllPgmDelYnResult);
		
		return insertDeployPerfChkStatusHistory;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM삭제
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public Iqms cmDelete(Iqms iqms) throws Exception {
		String cmid = iqms.getCmid();
		String status = iqms.getStatus();
		
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(cmid);
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		String result = iqmsDao.callSpSpopDeployStatusProc(performanceCheckMng);
		if(result == null) {
			//return 1;
			iqms.setResult(1);
		}else {
			//return 0;
			iqms.setResult(0);
		}
		return iqms;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM상태동기화
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public int cmStatusSync_old_20190701(Status statusObject) throws Exception {
		/** JSON 리스트 갯수만큼 반복수행 */
		/** LOOP */
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(statusObject.getCmid());
		
		String status = statusObject.getStatus();
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회 */
		String selectMaxPerfCheckId = StringUtils.defaultString(iqmsDao.selectMaxPerfCheckId(performanceCheckMng));
		if ("".equals(selectMaxPerfCheckId)) {
			return 0;
		}
		performanceCheckMng.setPerf_check_id(selectMaxPerfCheckId);
		
		/** 2 : 변수 세팅 */
		/** 배포성능점검상태코드, 배포성능점검상태변경사유 */
		/**
		 * IF STATUS = '3' THEN DEPLOY_CHECK_STATUS_CD = '03'; 배포완료
		 * DEPLOY_CHECK_STATUS_CHG_WHY = '배포완료'; ELSIF STATUS = '4' THEN
		 * DEPLOY_CHECK_STATUS_CD = '04'; 배포취소 DEPLOY_CHECK_STATUS_CHG_WHY = '배포취소';
		 * ELSIF STATUS = '5' THEN DEPLOY_CHECK_STATUS_CD = '05'; 배포삭제
		 * DEPLOY_CHECK_STATUS_CHG_WHY = '배포삭제'; END IF;
		 */
		if ("3".equals(status)) {
			performanceCheckMng.setDeploy_check_status_cd("03");
			performanceCheckMng.setDeploy_check_status_chg_why("배포완료");
		} else if ("4".equals(status)) {
			performanceCheckMng.setDeploy_check_status_cd("04");
			performanceCheckMng.setDeploy_check_status_chg_why("배포취소");
		} else if ("5".equals(status)) {
			performanceCheckMng.setDeploy_check_status_cd("05");
			performanceCheckMng.setDeploy_check_status_chg_why("배포삭제");
		}
		/**
		 * 배포성능상태변경자ID DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
		 */
		performanceCheckMng.setDeploy_check_status_updater_id("dbmanager");
		
		/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE */
		int updateDeployPerfChk = iqmsDao.updateDeployPerfChk(performanceCheckMng);
		logger.debug("updateDeployPerfChk :" + updateDeployPerfChk);
		
		/** 7 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT */
		int insertDeployPerfChkStatusHistory = iqmsDao.insertDeployPerfChkStatusHistory(performanceCheckMng);
		logger.debug("insertDeployPerfChkStatusHistory :" + insertDeployPerfChkStatusHistory);
		return insertDeployPerfChkStatusHistory;
	}
	
	/***************************************************************
	 * 인터페이스명 : CM상태동기화
	 * 
	 * INPUT PARAM - cmid : 배포ID(DEPLOY_ID) - status :
	 * 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	 ***************************************************************/
	public int cmStatusSync(Status statusObject) throws Exception {
		/** JSON 리스트 갯수만큼 반복수행 */
		/** LOOP */
		PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
		performanceCheckMng.setDeploy_id(statusObject.getCmid());
		
		String status = statusObject.getStatus();
		performanceCheckMng.setDeploy_check_status_cd(status);
		
		/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회 */
		String selectMaxPerfCheckId = StringUtils.defaultString(iqmsDao.selectMaxPerfCheckId(performanceCheckMng));
		if ("".equals(selectMaxPerfCheckId)) {
			return 0;
		}
		performanceCheckMng.setPerf_check_id(selectMaxPerfCheckId);
		
		/** 2 : 변수 세팅 */
		/** 배포성능점검상태코드, 배포성능점검상태변경사유 */
		/**
		 * IF STATUS = '3' THEN DEPLOY_CHECK_STATUS_CD = '3'; 
		 * 						DEPLOY_CHECK_STATUS_CHG_WHY = '배포완료'; 
		 * ELSIF STATUS = '4' THEN DEPLOY_CHECK_STATUS_CD = '4'; 
		 * 						DEPLOY_CHECK_STATUS_CHG_WHY = '배포취소';
		 * ELSIF STATUS = '5' THEN DEPLOY_CHECK_STATUS_CD = '5'; 
		 * 						DEPLOY_CHECK_STATUS_CHG_WHY = '배포삭제'; 
		 * END IF;
		 */
		//SP_SPOP_DEPLOY_STATUS_PROC 프로시저에서 '0'||DEPLOY_CHECK_STATUS_CD 해줌
		String result = "";
		performanceCheckMng.setDeploy_check_status_cd(status);
		result = iqmsDao.callSpSpopDeployStatusProc(performanceCheckMng);
		if(result == null) {
			return 1;
		}else {
			return 0;
		}
	}
	
	@Override
	public int updateCheckResultAncYn(PerformanceCheckMng performanceCheckMng) throws Exception {
		return performanceCheckMngDao.updateCheckResultAncYn(performanceCheckMng);
	}
	
	@Override
	public String selectNextPerfCheckId() throws Exception {
		return iqmsDao.selectNextPerfCheckId();
	}
	
	@Override
	public String selectBeforePerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception {
		return iqmsDao.selectBeforePerfCheckId(performanceCheckMng);
	}
	
	@Override
	public int getValidateWrkjobDivCd(String wrkjobDivCd) throws Exception {
		return iqmsDao.getValidateWrkjobDivCd(wrkjobDivCd);
	}
	
	@Override
	public String getValidateDeployID(String deployId) throws Exception {
		return iqmsDao.getValidateDeployID(deployId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String generateIqmsResult(String cmStatus, String openpopStatus, Iqms iqms) {
		String openpopStatusMsg = "";
		JSONObject jsonResponseContent = null;
		
		if("200".equals(openpopStatus)) {
			openpopStatusMsg = "";
		} else if("1010".equals(openpopStatus)) {
			openpopStatusMsg = "Company Code Null";
		} else if("1011".equals(openpopStatus)) {
			openpopStatusMsg = "Company Code Verification Fail";
		} else if("1012".equals(openpopStatus)) {
			openpopStatusMsg = "WorkJob Code Not Found";
		} else if("1013".equals(openpopStatus)) {
			openpopStatusMsg = "PerfCheckID not found";
		} else if("1020".equals(openpopStatus)) {
			openpopStatusMsg = "Master Agent Connection Refuse";
		} else if("1021".equals(openpopStatus)) {
			openpopStatusMsg = "Master Agent XML File Process Fail";
		} else if("1022".equals(openpopStatus)) {
			openpopStatusMsg = "Master Agent Socket Timeout";
		} else if("1099".equals(openpopStatus)) {
			openpopStatusMsg = "Open-POP Internal Server Error";
		}
		
		jsonResponseContent = new JSONObject(); // 처리결과  JSON
		jsonResponseContent.put("ret_code", openpopStatus);
		jsonResponseContent.put("err_msg", openpopStatusMsg);
		
		JSONArray jsonArray = new JSONArray();
		
		JSONObject jsonResponseRetData = new JSONObject();
		jsonResponseRetData.put("cmid", iqms.getCmid());
		jsonResponseRetData.put("status", cmStatus);
		jsonResponseRetData.put("cmusrnum", iqms.getCmusrnum());
		jsonResponseRetData.put("jobcd", iqms.getJobcd());
		jsonResponseRetData.put("cmcreateday", iqms.getCmcreateday());
		jsonResponseRetData.put("perfcheckid", iqms.getPerfcheckid());
		jsonResponseRetData.put("cmworkday", iqms.getCmworkday());
		
		jsonArray.put(jsonResponseRetData);
		jsonResponseContent.put("ret_data", jsonArray);
		return jsonResponseContent.toString();
	}
	
	public Map<String,Object> setResponseData(Iqms iqms, String cmStatus) {
		String responseContent = "";
		//String cmStatus = "";			// 1: CM확정, 3: CM완료, 4: CM취소, 5: CM삭제
		String openpopStatus = "";		// 200: 정상, 1010:업체코드없음, 1011:업체코드 검증 실패, 1012:업무코드 검증 실패, 1013:성능점검ID 조회실패, 1020:Connection Refused, 1021:XML File Process Fail
		Iqms resultIqms = null;
		String compCd = "";				// 업체코드
		
		int responseStatus;
		
		Map<String,Object> responseMap = new HashMap<String, Object>();	//return 할 맵
		
		try {
			/* 정상처리 */
			openpopStatus = "200";
			responseStatus = HttpServletResponse.SC_OK;
			
			iqms.setCmworkday(DateUtil.getNowDate("yyyy-MM-dd hh:mm:ss"));
			iqms.setPerfcheckid("");
			
			/* cmStatus가 CM확정이외에 아닐 경우 추가로 세팅해주는 값 */
			if( !"1".equals(cmStatus) ) {
				iqms.setResult(0);
				iqms.setCmusrnum("");
				iqms.setJobcd("");
				iqms.setCmcreateday("");
			}
			
			resultIqms = iqms;
			
			logger.info("Compcd=" + iqms.getCompcd());
			
			/* 업체코드 Null 검증 */
			if ("".equals(iqms.getCompcd())) {
				logger.info("CompanyCD=Company Code Null");
				openpopStatus = "1010";
				responseStatus = HttpServletResponse.SC_BAD_REQUEST;
				
			/* 업체코드 Validation 검증 */
			} else {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setPassword("madeopen");
				compCd = StringUtils.defaultString(pbeEnc.decrypt(iqms.getCompcd()));
				
				if (!Util.chkVerification(compCd)) {
					logger.info("CompanyCD=Company Code Verification Fail");
					openpopStatus = "1011";
					responseStatus = HttpServletResponse.SC_BAD_REQUEST;
				}
			}
			
			if("1".equals(cmStatus)) {
				/* cmStatus에 따른 실행문 */
				//CM 확정 작업일 경우
				if("200".equals(openpopStatus)) {
					/*업무코드 검증*/ 
					int wrkjobDivCdCnt = getValidateWrkjobDivCd(iqms.getJobcd());
					if (wrkjobDivCdCnt == 0) {
						logger.info("WrkjobDivCD=WorkJob Code Not Found");
						openpopStatus = "1012";
						responseStatus = HttpServletResponse.SC_BAD_REQUEST;
					}
					
				}
				
				/*CM 확정*/
				if("200".equals(openpopStatus)) {
					resultIqms = cmConfirm(iqms);
				}
				
			} else{
				//CM 확정 작업이 아닐 경우 공통 실행문
				/* 성능점검ID 검증 */
				if("200".equals(openpopStatus)) {
					String perfCheckId = StringUtils.defaultString(getValidateDeployID(iqms.getCmid()));
					logger.info("PerfCheckID="+perfCheckId);
					iqms.setPerfcheckid(perfCheckId);
					
					if ("".equals(perfCheckId)) {
						logger.info("PerfCheckID=PerfCheckID not found");
						openpopStatus = "1013";
						responseStatus = HttpServletResponse.SC_BAD_REQUEST;
					}
				}
				
				if("200".equals(openpopStatus)) {
					if("3".equals(cmStatus)) {
						/* CM 완료 */
						resultIqms = cmComplete(iqms);
					
					} else if("4".equals(cmStatus)) {
						/* CM 취소 */
						resultIqms = cmCancel(iqms);
						
					} else if("5".equals(cmStatus)) {
						/* CM 삭제 */
						resultIqms = cmDelete(iqms);
						
					}
					
					/* error occured */
					if (resultIqms.getResult() == 0) {
						openpopStatus = "1099";
						responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
					}
				}
			}
		} catch (java.net.SocketTimeoutException e1) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e1.getMessage());
			openpopStatus = "1022";
			responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} catch (java.net.ConnectException e2) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e2.getMessage());
			openpopStatus = "1020";
			responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e3) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e3.getMessage());
			openpopStatus = "1011";
			responseStatus = HttpServletResponse.SC_BAD_REQUEST;
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			if(e.getMessage().contains("master agent")) { // Master 로 부터 throw 된 오류일경우
				openpopStatus = "1021";
			
			} else {// 기타 오류 일경우 
				openpopStatus = "1099";
			}
			responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;	
		}
		
		responseContent = generateIqmsResult(cmStatus, openpopStatus, resultIqms);
		logger.info("responseContent=" + responseContent);
		
		responseMap.put("responseContent", responseContent);
		responseMap.put("responseStatus", responseStatus);
		
		return responseMap;
	}
}