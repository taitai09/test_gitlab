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
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Project;
import omc.spop.model.ProjectSqlQtyChkIdx;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.model.SQLStandards;
import omc.spop.model.Users;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;

/***********************************************************
 * 2020.05.06	명성태	최초작성
 * 2020.07.01	이재우	기능개선
 **********************************************************/

@RequestMapping(value="/sqlStandardSettingDesign")
@Controller
public class SqlStandardSettingDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlStandardSettingDesignController.class);
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;
	
	@Autowired
	private CommonService commonService;
	
	/* SQL 표준 품질점검 설정 */
	@RequestMapping(value = "/design", method = RequestMethod.GET)
	public String design(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "sqlStandardDesign/setting/design";
	}
	
	/* 품질 점검 지표 관리 */
	@RequestMapping(value = "/MaintainQualityCheckIndicator", method = RequestMethod.GET)
	public String MaintainSqlQualityCheck(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );
	
		return "sqlStandardDesign/setting/maintainQualityCheckIndicator";
	}
	
	/* projectList */
	@RequestMapping(value = "/getProjectList" , method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectList() throws Exception{
		List<Project> resultList = new ArrayList<Project>();
		
		try {
			resultList = commonService.projectList( null );
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 품질 점검 SQL 관리 */
	@RequestMapping(value = "/MaintainQualityCheckSql", method = RequestMethod.GET)
	public String MaintainQualityCheckSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );

		return "sqlStandardDesign/setting/maintainQualityCheckSql";
	}
	
	/* 프로젝트 SQL 품질점검 지표 관리 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdxMng", method = RequestMethod.GET)
	public String ProjectSqlQtyChkIdxMng(@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx,
			Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("menu_id", projectSqlQtyChkIdx.getMenu_id());
		model.addAttribute("menu_nm", projectSqlQtyChkIdx.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());
		
		return "sqlStandardDesign/setting/projectSqlQtyChkIdxMng";
	}
	
	
	
	/* 프로젝트 SQL 품질점검 RULE 관리 */
	@RequestMapping(value = "/ProjectSqlQtyChkRuleMng", method = RequestMethod.GET)
	public String ProjectSqlQtyChkRuleMng(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("menu_id", projectSqlQtyChkRule.getMenu_id());
		model.addAttribute("menu_nm", projectSqlQtyChkRule.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "sqlStandardDesign/setting/projectSqlQtyChkRuleMng";
	}
}