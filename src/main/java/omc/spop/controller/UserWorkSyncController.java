package omc.spop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.service.UserWorkSyncService;

@Controller
public class UserWorkSyncController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(UserWorkSyncController.class);

	@Autowired
	private UserWorkSyncService userWorkSyncService;

	/*
	<IQMS연동>
	1. 단위업무코드 조회(VWSKIHR57)

	<통합계정관리시스템(EAM) 연동>
	2. 직원(사용자)조회(VWWDBIT01)
	3. 직원(사용자)업무조회(VWWDBIT02)


	<OPENPOP>
	4. ODS_KBCD_VWWDBIT01, ODS_KBCD_VWWDBIT02, ODS_KBCD_VWSKIHR57 테이블에 저장
	5. 신규 업무 등록
	6. 신규 사용자 등록
	7. 신규 사용자 업무 등록
	 */
	
	/**
	 * 1. 단위업무코드 조회(VWSKIHR57) 하여 ODS_KBCD_VWSKIHR57 테이블에 저장
	 * IQMS
	 * 
	 * @return
	 */
//	@RequestMapping(value="/syncWrkJobCd", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@RequestMapping(value = "/syncWrkJobCd", method = RequestMethod.POST)
	@ResponseBody
	public Result syncWrkJobCd(Model model) {

		Result result = new Result();
		int syncResult = 0;

		try {
			syncResult = userWorkSyncService.syncWrkJobCd();
			logger.debug("syncResult:" + syncResult);
			if (syncResult > 0) {
				result.setResult(true);
				result.setMessage("업무코드 동기화 완료");
			} else {
				result.setResult(false);
				result.setMessage("업무코드 동기화 실패");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage("업무코드 동기화 실패");
		}
		return result;
	}

	/**
	 * 2. 사용자 조회(VWWDBIT01) 하여 ODS_KBCD_VWWDBIT02 테이블에 저장
	 * EAM
	 * 
	 * @return
	 */
//	@RequestMapping(value="/syncUsers", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@RequestMapping(value = "/syncUsers", method = RequestMethod.POST)
	@ResponseBody
	public Result syncUsers(Model model) {

		Result result = new Result();
		int syncResult = 0;

		try {
			syncResult = userWorkSyncService.syncUsers();
			if (syncResult > 0) {
				result.setResult(true);
				result.setMessage("사용자 동기화 완료");
			} else {
				result.setResult(false);
				result.setMessage("사용자 동기화 실패");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage("사용자 동기화 실패");
		}
		return result;
	}

	/**
	 * 3. 사용자 업무 조회(VWWDBIT02) 하여 ODS_KBCD_VWSKIHR57 테이블에 저장
	 * EAM
	 * 
	 * @return
	 */
//	@RequestMapping(value="/syncUserWrkjob", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@RequestMapping(value = "/syncUserWrkjob", method = RequestMethod.POST)
	@ResponseBody
	public Result syncUserWrkjob(Model model) {

		Result result = new Result();
		int syncResult = 0;

		try {
			syncResult = userWorkSyncService.syncUserWrkjob();
			if (syncResult > 0) {
				result.setResult(true);
				result.setMessage("사용자 업무 동기화 완료");
			} else {
				result.setResult(false);
				result.setMessage("사용자 업무 동기화 실패");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage("사용자 업무 동기화 실패");
		}
		return result;
	}

	/**
	 * 4. 업무, 사용자, 사용자업무 동기화
	 * 
	 * @return
	 */
	@RequestMapping(value = "/syncWrkRelatedTable", method = RequestMethod.POST)
	@ResponseBody
	public Result syncWrkRelatedTable() {
		Result result = new Result();
		int syncResult = 0;

		try {
			syncResult = userWorkSyncService.syncWrkRelatedTable();
			if (syncResult > 0) {
				result.setResult(true);
				result.setMessage("업무, 사용자, 사용자업무 동기화 완료");
			} else {
				result.setResult(false);
				result.setMessage("업무, 사용자, 사용자업무 동기화 실패");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("업무, 사용자, 사용자업무 동기화 실패");
		}
		return result;
	}

}
