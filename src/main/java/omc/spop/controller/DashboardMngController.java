package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import omc.spop.model.MotoringGroup;
import omc.spop.model.Result;
import omc.spop.service.DashboardMngService;

/***********************************************************
 * 2018.10.05	임호경	최초작성
 * 2020.05.20	이재우	기능개선
 **********************************************************/

@Controller
public class DashboardMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardMngController.class);

	@Autowired
	private DashboardMngService dashboardMngService;

	/*motoringGroup*/
	@RequestMapping(value = "/DashboardMng/MotoringGroup", method = RequestMethod.GET)
	public String MotoringGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", motoringGroup.getMenu_id() );
		model.addAttribute("menu_nm", motoringGroup.getMenu_nm() );
		
		return "dashboardMng/motoringGroup";
	}
	
	/*getMotoringGroupList */
	@RequestMapping(value = "/DashboardMng/getMotoringGroupList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getMotoringGroupList(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup, HttpServletRequest res,
			Model model) {
		List<MotoringGroup> resultList = new ArrayList<MotoringGroup>();

		try {
			resultList = dashboardMngService.getMotoringGroupList(motoringGroup);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString(); 
	}
	
	/* saveMotoringGroup 저장 */
	@RequestMapping(value = "/DashboardMng/saveMotoringGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result saveMotoringGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup,
			Model model) {
		Result result = new Result();
		int count = 0;
		int desplaySeqCount = 0;
		
		try {
			count = dashboardMngService.checkMotoringGroupId(motoringGroup);
			desplaySeqCount = dashboardMngService.checkMotoringDesplaySeq(motoringGroup);
			
			//check = (count == 0 ) ? true : false;
			
			if (motoringGroup.getCrud_flag().equals("C")) {
				
				if (count != 0) {
					result.setMessage("[ " + motoringGroup.getGroup_nm() + " ] 은(는) 이미 등록된 그룹 명 입니다.");
					result.setResult(false);
					return result;
				}
				
				if (desplaySeqCount != 0 ) {
					result.setMessage("[ " + motoringGroup.getDesplay_seq() + "] 번은 이미 등록된 정렬순서 입니다.");
					result.setResult(false);
					return result;
				} 
				count = dashboardMngService.insertMotoringGroup(motoringGroup);
				
			}
			
			if (motoringGroup.getCrud_flag().equals("U")) {
				MotoringGroup motoringGroupOne = dashboardMngService.getMotoringOneGroupId(motoringGroup);
				
				if (count != 0 && 
						!motoringGroupOne.getGroup_nm().equals(motoringGroup.getGroup_nm())) {
					result.setMessage("[ " + motoringGroup.getGroup_nm() + " ] 은(는) 이미 등록된 그룹 명 입니다.");
					result.setResult(false);
					return result;
				}
				
				boolean equalCheck = motoringGroupOne.getDesplay_seq().trim().equals(motoringGroup.getDesplay_seq().trim());
				
				if (desplaySeqCount != 0 && !equalCheck) {
					result.setMessage("[ " + motoringGroup.getDesplay_seq() + "] 번은 이미 등록된 정렬순서 입니다.");
					result.setResult(false);
					return result;
				}
				count = dashboardMngService.updateMotoringGroup(motoringGroup);
				
			}
			
			/*if(!check && motoringGroup.getCrud_flag().equals("U")){
				count = dashboardMngService.updateMotoringGroup(motoringGroup);
			}else if(check && motoringGroup.getCrud_flag().equals("C")){
				count = dashboardMngService.insertMotoringGroup(motoringGroup);
			}else{
				result.setMessage("[ " + motoringGroup.getGroup_nm() + " ] 은(는) 이미 등록된 그룹 명 입니다.");
				result.setResult(false);
				return result;
			}*/
			
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
	
	/* deleteMotoringGroup */
	@RequestMapping(value = "/DashboardMng/deleteMotoringGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMotoringGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup,
			Model model) {
		Result result = new Result();
		try {
			logger.debug("!!motoringGroup : " + motoringGroup);
			
			dashboardMngService.deleteMotoringGroup(motoringGroup);
			result.setResult(true);
			
			result.setMessage("성공적으로 [ " + motoringGroup.getGroup_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/*DatabaseGroup*/
	@RequestMapping(value = "/DashboardMng/DatabaseGroup", method = RequestMethod.GET)
	public String DatabaseGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", motoringGroup.getMenu_id() );
		model.addAttribute("menu_nm", motoringGroup.getMenu_nm() );
		
		return "dashboardMng/databaseGroup";
	}
	
	/*getDatabaseGroupList */
	@RequestMapping(value = "/DashboardMng/getDatabaseGroupList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDatabaseGroupList(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup, HttpServletRequest res,
			Model model) {
		List<MotoringGroup> resultList = new ArrayList<MotoringGroup>();
		
		try {
			resultList = dashboardMngService.getDatabaseGroupList(motoringGroup);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString(); 
	}
	
	/* saveDatabaseGroup 저장 */
	@RequestMapping(value = "/DashboardMng/saveDatabaseGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result saveDatabaseGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup,
			Model model) {
		Result result = new Result();
		int count = 0;
		int desplaySeqCount = 0;
		MotoringGroup motoringOne;
		
		try {
			desplaySeqCount = dashboardMngService.checkDatabaseDesplaySeq(motoringGroup);
			
			if (motoringGroup.getCrud_flag().equals("C")) {
				count = dashboardMngService.checkDatabaseGroupId(motoringGroup);
				
				if (count > 0) {
					result.setResult(false);
					result.setMessage("[ "+motoringGroup.getGroup_nm() + " / " + motoringGroup.getDb_name() +" ] 은(는) 이미 등록된 그룹명과 DB명 입니다." );
					return result;
				} 
				
				if (desplaySeqCount != 0 ) {
					result.setMessage("[ " + motoringGroup.getDesplay_seq() + " ] 번은 이미 등록된 정렬순서 입니다.");
					result.setResult(false);
					return result;
				}
				
				count = dashboardMngService.saveDatabaseGroup(motoringGroup);
				
			} else {
				motoringOne = dashboardMngService.getDatabaseOneGroupId(motoringGroup);
				
				if (desplaySeqCount != 0 && !motoringOne.getDesplay_seq().equals(motoringGroup.getDesplay_seq())) {
					result.setMessage("[ " + motoringGroup.getDesplay_seq() + " ] 번은 이미 등록된 정렬순서 입니다.");
					result.setResult(false);
					return result;
				}
				
				count = dashboardMngService.saveDatabaseGroup(motoringGroup);
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
	
	/* deleteMotoringGroup */
	@RequestMapping(value = "/DashboardMng/deleteDatabaseGroup", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteDatabaseGroup(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup,
			Model model) {
		Result result = new Result();
		try {
			logger.debug("!!motoringGroup : " + motoringGroup);
			
			dashboardMngService.deleteDatabaseGroup(motoringGroup);
			result.setResult(true);
			
			result.setMessage("성공적으로 [ " + motoringGroup.getGroup_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* getMotoringGroupId -셀렉트 */
	@RequestMapping(value = "/DashboardMng/getMotoringGroupId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerTypeCd(@ModelAttribute("motoringGroup") MotoringGroup motoringGroup, Model model) {
		List<MotoringGroup> resultList = new ArrayList<MotoringGroup>();
		
		try {
			motoringGroup.setUser_id(SessionManager.getLoginSession().getUsers().getUser_id());
			resultList = dashboardMngService.getMotoringGroupId(motoringGroup);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
}