package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.NotUseHint;
import omc.spop.model.Result;
import omc.spop.model.UiExceptDbUser;
import omc.spop.service.DBMngService;

/***********************************************************
 * 2018.09.03 	임호경	 최초작성
 **********************************************************/

@Controller
public class DBMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(DBMngController.class);

	@Autowired
	private DBMngService dbMngService;

	@RequestMapping(value = "/DBMng/NotUseHint", method = RequestMethod.GET)
	public String NotUseHint(@ModelAttribute("notUseHint") NotUseHint notUseHint, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", notUseHint.getMenu_id() );
		model.addAttribute("menu_nm", notUseHint.getMenu_nm() );

		return "databaseMngSetting/notUseHint";
	}

	
	/*검색시 힌트리스트 조회*/
	@RequestMapping(value = "/DBMng/getHintList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getHintList(@ModelAttribute("notUseHint") NotUseHint notUseHint, HttpServletRequest res,
			Model model) {
		List<NotUseHint> resultList = new ArrayList<NotUseHint>();

		try {
			resultList = dbMngService.getHintList(notUseHint);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	
	
	/*셀렉트시 db_abbr_nm 조회*/        
	@RequestMapping(value = "/DBMng/getDbAbbrNm", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDbAbbrNm(@ModelAttribute("notUseHint") NotUseHint notUseHint, HttpServletRequest res, Model model) {
		List<NotUseHint> resultList = new ArrayList<NotUseHint>();
		String db_abbr_nm;
		try {
			db_abbr_nm = dbMngService.getDbAbbrNm(notUseHint);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		logger.debug("db_abbr_nm:"+db_abbr_nm);
		return db_abbr_nm;
	}
	
	
	
	/*힌트 삭제*/
	@RequestMapping(value = "/DBMng/deleteHint", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteHint(@ModelAttribute("notUseHint") NotUseHint notUseHint, HttpServletRequest res, Model model)
			throws Exception {

		Result result = new Result();
		String resultMsg = null;
		boolean resultCheck = false;
		int resultCount = 0;

		try {
			resultCount = dbMngService.deleteHint(notUseHint);
			resultCheck = (resultCount > 0) ? true : false;
			resultMsg = "\'"+ notUseHint.getOld_hint_nm() + "\'"+" 이(가) 성공적으로 삭제되었습니다.";
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			resultCheck = false;
			resultCount = 0;
			result.setMessage("데이터베이스 오류입니다");
		}
		result.setMessage(resultMsg);
		result.setResultCount(resultCount);
		result.setResult(resultCheck);
		logger.debug("result"+result);

		return result;
	}
	
	/*힌트 저장 및 수정*/
	@RequestMapping(value = "/DBMng/saveHint", method = RequestMethod.POST)
	@ResponseBody
	public Result saveHint(@ModelAttribute("notUseHint") NotUseHint notUseHint, HttpServletRequest res, Model model)
			throws Exception {

		String crud_flag = StringUtils.defaultString(notUseHint.getCrud_flag());
		Result result = new Result(); String resultMsg = null;	boolean resultCheck = false; int resultCount = 0;

		logger.debug("dbid 값 확인 : " + notUseHint.toString());
		logger.debug("dbid 값 확인2 : " + notUseHint.getDbid());
		
		
		try {
			int check = 0; // hint가 이미 테이블에 있는지여부 판단
			check = dbMngService.checkHint(notUseHint);  //hint명이 중복되는지 여부 판단.
			
//			resultCount = dbMngService.updateHint(notUseHint);  //업데이트 나중에추가적으로 추가 될 수있음. 일단 보류.
			if(check > 0){  //중복된다면,
			
				result.setMessage("\'"+notUseHint.getOld_hint_nm()+"\'"+"는 이미 " + notUseHint.getDb_abbr_nm() + "에 추가되어있습니다.");
				result.setResult(false);
				return result;

			}else{ //중복이 아니라면,
				
				if(crud_flag.equals("U")){
					resultCount = dbMngService.updateHint(notUseHint);
					resultCheck = (resultCount > 0) ? true : false;
					resultMsg = "수정 되었습니다.";
					
				}else{
					resultCount = dbMngService.insertHint(notUseHint);
					resultCheck = (resultCount > 0) ? true : false;
					resultMsg = "저장 되었습니다.";
				}

				
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			resultCheck = false;
			resultCount = 0;
			result.setMessage("데이터베이스 오류입니다");
		}
		result.setMessage(resultMsg);
		result.setResultCount(resultCount);
		result.setResult(resultCheck);

		return result;
	}
	
	
	
	
	
	
	@RequestMapping(value = "/DBMng/UiExceptDbUserMng", method = RequestMethod.GET)
	public String UiExceptDbUserMng(@ModelAttribute("uiExceptDbUser") UiExceptDbUser uiExceptDbUser, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", uiExceptDbUser.getMenu_id() );
		model.addAttribute("menu_nm", uiExceptDbUser.getMenu_nm() );

		return "databaseMngSetting/uiExceptDbUserMng";
//		return "databaseMngSetting/notUseHint";

	}
	
	/*검색시 유저리스트 조회*/
	@RequestMapping(value = "/DBMng/getUsernameList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUsernameList(@ModelAttribute("uiExceptDbUser") UiExceptDbUser uiExceptDbUser, HttpServletRequest res,
			Model model) {
		List<UiExceptDbUser> resultList = new ArrayList<UiExceptDbUser>();

		try {
			resultList = dbMngService.getUsernameList(uiExceptDbUser);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString(); 
	}
	
	
	/* saveUsername 저장 */
	@RequestMapping(value = "/DBMng/saveUsername", method = RequestMethod.POST)
	@ResponseBody
	public Result saveUsername(@ModelAttribute("uiExceptDbUser") UiExceptDbUser uiExceptDbUser,
			Model model) {

		Result result = new Result();
		int count = 0;
		boolean check = false;
		try {
			count = dbMngService.checkUsername(uiExceptDbUser);
			check = (count == 0) ? true : false;
			
			if(check && uiExceptDbUser.getCrud_flag().equals("U")){
				count = dbMngService.updateUsername(uiExceptDbUser);
			}else if(check && uiExceptDbUser.getCrud_flag().equals("C")){
				count = dbMngService.insertUsername(uiExceptDbUser);
			}else{
				result.setMessage("[ "+uiExceptDbUser.getUsername()+" ] 은(는) 이미 추가 되어있습니다.");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* deleteUsername 삭제 - deleteUsername */
	@RequestMapping(value = "/DBMng/deleteUsername", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteUsername(@ModelAttribute("uiExceptDbUser") UiExceptDbUser uiExceptDbUser,
			Model model) {
		Result result = new Result();

		try {
//			logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);

			dbMngService.deleteUsername(uiExceptDbUser);
			result.setResult(true);

			result.setMessage("성공적으로 [ " + uiExceptDbUser.getUsername() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

}
