package omc.spop.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2021.07.26	황예지	최초작성
 **********************************************************/

@RequestMapping("/execSqlPerfCheck")
@Controller
public class ExecSqlPerfCheckDesignController extends InterfaceController {
	
	@Value("#{defaultConfig['customer']}")
	private String CUSTOMER;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String design(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "execSqlPerfCheck/execSqlPerfCheckDesign";
	}
	
	/* 성능 검증 관리 */
	@RequestMapping(value = "/perfInspectMng", method = RequestMethod.GET)
	public String perfInspectMng(@ModelAttribute("perfInspectMng") PerformanceCheckMng performanceCheckMng, Model model) {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -2);
		
		String deploy_request_dt_1 = StringUtils.defaultString(performanceCheckMng.getDeploy_request_dt_1());
		String deploy_request_dt_2 = StringUtils.defaultString(performanceCheckMng.getDeploy_request_dt_2());
		
		if (deploy_request_dt_1.equals("")) {
			performanceCheckMng.setDeploy_request_dt_1(startDate);
		}
		if (deploy_request_dt_2.equals("")) {
			performanceCheckMng.setDeploy_request_dt_2(nowDate);
		}
		model.addAttribute("menu_id", performanceCheckMng.getMenu_id());
		model.addAttribute("performanceCheckMng", performanceCheckMng);
		model.addAttribute("call_from_parent", performanceCheckMng.getCall_from_parent());
		
		return "execSqlPerfCheck/perfInspectMng";
	}
	
	/* 성능 검증 예외 요청 */
	@RequestMapping(value = "/requestException", method = RequestMethod.GET)
	public String requestException(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd","yyyy-MM-dd", nowDate,-2);
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("search_wrkjob_cd", deployPerfChkIndc.getSearch_wrkjob_cd());
		model.addAttribute("search_wrkjob_cd_nm", deployPerfChkIndc.getSearch_wrkjob_cd_nm());
		model.addAttribute("search_dbio", deployPerfChkIndc.getSearch_dbio());
		model.addAttribute("search_program_nm", deployPerfChkIndc.getSearch_program_nm());
		model.addAttribute("search_deploy_id", deployPerfChkIndc.getSearch_deploy_id());
		model.addAttribute("search_deploy_requester", deployPerfChkIndc.getSearch_deploy_requester());
		model.addAttribute("call_from_parent", deployPerfChkIndc.getCall_from_parent());
		model.addAttribute("call_from_child", deployPerfChkIndc.getCall_from_child());
		model.addAttribute("deployPerfChkIndc", deployPerfChkIndc);

		model.addAttribute("search_perf_check_id", deployPerfChkIndc.getSearch_perf_check_id());
		model.addAttribute("search_perf_check_step_id", deployPerfChkIndc.getSearch_perf_check_step_id());
		model.addAttribute("search_program_id", deployPerfChkIndc.getSearch_program_id());
		model.addAttribute("search_program_execute_tms", deployPerfChkIndc.getSearch_program_execute_tms());
		
		if(StringUtils.isEmpty(deployPerfChkIndc.getDeploy_request_dt()) == false) {
			deployPerfChkIndc.setSearch_from_deploy_request_dt(deployPerfChkIndc.getDeploy_request_dt());
			deployPerfChkIndc.setSearch_to_deploy_request_dt(deployPerfChkIndc.getDeploy_request_dt());
			
		}
		
		return "execSqlPerfCheck/requestException";
	}
	
	/* 성능 검증 예외 삭제 */
	@RequestMapping(value = "/deleteException", method = RequestMethod.GET)
	public String deleteException( Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		
		return "execSqlPerfCheck/deleteException";
	}
	
	/* 성능 검증 예외 처리 이력 */
	@RequestMapping(value = "/exceptionProcessHistory", method = RequestMethod.GET)
	public String exceptionProcessHistory( Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd","yyyy-MM-dd", nowDate,-2);

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		
		return "execSqlPerfCheck/exceptionProcessHistory";
	}
}