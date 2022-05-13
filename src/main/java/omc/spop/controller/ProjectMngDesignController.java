package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Project;
import omc.spop.model.ProjectDb;
import omc.spop.model.ProjectWrkjob;
import omc.spop.model.Users;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.service.SystemMngService;

/***********************************************************
 * 2019.09.16 initialize
 **********************************************************/

@Controller
public class ProjectMngDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectMngDesignController.class);
	
	@Autowired
	private SystemMngService systemMngService;
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;
	
	@Autowired
	private CommonService commonService;
	
	/* 기준정보 설정 > 프로젝트 관리 */
	@RequestMapping(value = "/systemManage/projectMng/ProjectMngDesign", method = RequestMethod.GET)
	public String ProjectMngDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "systemManage/projectMng/projectMngDesign";
	}
	
	/* 기준정보 설정 > 프로젝트 관리 > 프로젝트 관리 */
	@RequestMapping(value = "/systemManage/projectMng/ProjectMng", method = RequestMethod.GET)
	public String ProjectMng(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "systemManage/projectMng/projectMng";
	}
	
	/* 기준정보 설정 > 프로젝트 관리 > 프로젝트 업무 관리 */
	@RequestMapping(value = "/systemManage/projectMng/ProjectWrkjobMng", method = RequestMethod.GET)
	public String ProjectWrkjobMng(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("menu_id", projectWrkjob.getMenu_id());
		model.addAttribute("menu_nm", projectWrkjob.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectMng/projectWrkjobMng";
	}
	
	/* 기준정보 설정 > 프로젝트 관리 > 프로젝트 DB 관리 */
	@RequestMapping(value = "/systemManage/projectMng/ProjectDbMng", method = RequestMethod.GET)
	public String ProjectDbMng(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("menu_id", projectDb.getMenu_id());
		model.addAttribute("menu_nm", projectDb.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectMng/projectDbMng";
	}
	
	/* 기준정보 설정 > 프로젝트 관리 > 프로젝트 튜닝진행단계 */
	@RequestMapping(value = "/systemManage/projectMng/ProjectTuningProcessStage", method = RequestMethod.GET)
	public String ProjectTuningProcessStage(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("menu_id", projectDb.getMenu_id());
		model.addAttribute("menu_nm", projectDb.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectMng/projectTuningProcessStage";
	}
}