package omc.spop.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/***********************************************************
 * 2020.05.06	명성태	최초작성
 **********************************************************/

@RequestMapping(value="/PerformanceCheckIndexDesign")
@Controller
public class PerformanceCheckIndexSituationDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckIndexSituationDesignController.class);
	
	@Value("#{defaultConfig['customer']}")
	private String customer;
	
	/* 성능 점검 현황 */
	@RequestMapping(value = "/situation", method = RequestMethod.GET)
	public String situation(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		model.addAttribute("customer",customer);
		if("kbcd".equals(customer)) {
			return "performanceCheckIndexDesign/situation/perfCheckState";
		}else {
			return "execSqlPerfCheck/situation/perfCheckState";
		}
	}
	
	/* 성능 점검 설정 Design */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/design";
			
		}else {
			return "execSqlPerfCheck/setting/design";
		}
	}
	
	
	/*성능점검 > 성능점검 지표관리*/
	@RequestMapping(value = "/DeployPrefChkIndc", method = RequestMethod.GET)
	public String deployPrefChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/deployPerfChkIndc";
			
		}else {
			return "execSqlPerfCheck/setting/deployPerfChkIndc";
		}
	}
	
	/*성능점검 > 성능점검 단계관리*/
	@RequestMapping(value = "/DeployPerfChkStep", method = RequestMethod.GET)
	public String deployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/deployPerfChkStep";
			
		}else {
			return "execSqlPerfCheck/setting/deployPerfChkStep";
		}
	}
	
	/*성능점검 > 업무별 성능점검 지표관리*/
	@RequestMapping(value = "/WjPerfChkIndc", method = RequestMethod.GET)
	public String WjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/wjPerfChkIndc";
			
		}else {
			return "execSqlPerfCheck/setting/wjPerfChkIndc";
		}
	}
	
	/*성능점검 > 업무별 성능점검 단계관리*/
	@RequestMapping(value = "/DeployPerfChkStepTestDB", method = RequestMethod.GET)
	public String deployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/deployPerfChkStepTestDB";
			
		}else {
			return "execSqlPerfCheck/setting/deployPerfChkStepTestDB";
		}
	}
	
	/*성능점검 > 업무별 파싱스키마 관리*/
	@RequestMapping(value = "/DeployPerfChkParsingSchema", method = RequestMethod.GET)
	public String DeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		if("kbcd".equals(customer)){
			return "performanceCheckIndexDesign/setting/deployPerfChkParsingSchema";
			
		}else {
			return "execSqlPerfCheck/setting/deployPerfChkParsingSchema";
		}
	}
}