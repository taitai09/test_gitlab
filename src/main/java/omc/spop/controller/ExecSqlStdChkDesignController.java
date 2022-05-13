package omc.spop.controller;

import java.util.Collections;
import java.util.HashMap;
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
import omc.spop.model.SQLStandards;
import omc.spop.service.ExecSqlStdChkService;
import omc.spop.service.SQLStandardsService;
import omc.spop.utils.DateUtil;

@RequestMapping(value="/execSqlStdChkDesign")
@Controller
public class ExecSqlStdChkDesignController extends InterfaceController {
	
	@Autowired
	private ExecSqlStdChkService execSqlStdChkService;
	
	@Autowired
	private SQLStandardsService sqlStandarsService;
	
	private static final Logger logger = LoggerFactory.getLogger(ExecSqlStdChkDesignController.class);

	private String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
	
	/* 실행기반 SQL 표준 일괄 점검 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String execSqlStdChk(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "execSqlStdChk/execSqlStdChkDesign";
	}
	
	/* SQL 표준 점검 결과 */
	@RequestMapping(value = "/sqlStandardCheckResult", method = RequestMethod.GET)
	public String sqlStandardCheckResult(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "execSqlStdChk/sqlStandardCheckResult";
	}
	
	/* 표준 미준수 SQL */
	@RequestMapping(value = "/nonStandardSql", method = RequestMethod.GET)
	public String nonStandardSql(@RequestParam(required = false) HashMap<String, String> param, Model model) {
		String project_id = param.get("project_id");
		String resultStr = "";
		String currentPage = "1";
		param.put("exclusion", "100");
		
		List<SQLStandards> resultList = Collections.emptyList();
		SQLStandards sqlStandards = new SQLStandards();
		sqlStandards.setProject_id(project_id);
		
		try {
			if( param.containsKey("project_id")
					&& param.get("project_id").isEmpty() == false ) {
				
				resultList = execSqlStdChkService.loadIndexList(sqlStandards);
				resultStr = success(resultList).toJSONObject().toString();
				currentPage = param.get("currentPage");
				
				model.addAttribute("indexList", resultStr );
				model.addAttribute("project_id", project_id );
				model.addAttribute("sql_std_qty_scheduler_no", param.get("sql_std_qty_scheduler_no") );
				model.addAttribute("sql_std_gather_day", param.get("sql_std_gather_day") );
				model.addAttribute("call_from_parent", "true" );
			}
			
			resultList = sqlStandarsService.loadAllIndex(param);
			resultStr = success(resultList).toJSONObject().toString();
			
			model.addAttribute("allIndexList", resultStr);
		
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
			
			resultStr = getErrorJsonString(e);
			
		}finally {
			resultList = null;
			sqlStandards = null;
		}
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "execSqlStdChk/nonStandardSql";
	}
	
	/* SQL 표준 준수 현황 */
	@RequestMapping(value="/standardComplianceState", method=RequestMethod.GET)
	public String standardComplianceState(
			@RequestParam(required = false) Map<String, String> param, Model model) {
		
		String today = nowDate;
		
		if( param.containsKey("project_id")
				&& param.get("project_id").isEmpty() == false ) {
			
			today = param.get("sql_std_gather_day");
			model.addAttribute("project_id", param.get("project_id") );
			model.addAttribute("sql_std_qty_scheduler_no", param.get("sql_std_qty_scheduler_no") );
			model.addAttribute("call_from_parent", "true" );
		}
		
		String aMonthAgo = DateUtil.getPlusMons("yyyy-MM-dd", "yyyy-MM-dd", today, -1);
		
		model.addAttribute("nowDate", today );
		model.addAttribute("aMonthAgo", aMonthAgo );
		model.addAttribute("menu_id", param.get("menu_id") );
		model.addAttribute("menu_nm", param.get("menu_nm") );
		
		return "execSqlStdChk/standardComplianceState";
	}
	
	/* SQL 표준 점검 예외 대상 관리 */
	@RequestMapping(value="/maintainQualityCheckException", method=RequestMethod.GET)
	public String MaintainQualityCheckException(
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		
		model.addAttribute("user_id", SessionManager.getLoginSession().getUsers().getUser_id());
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", sqlStandards.getMenu_id() );
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );
		
		return "execSqlStdChk/maintainQualityCheckException";
	}
	
	/* 스케줄러 관리 */
	@RequestMapping(value = "/manageScheduler", method = RequestMethod.GET)
	public String manageScheduler(
			@RequestParam(required = false) Map<String, String> param, Model model) {
		String yesterDay = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -1);
		String aMonthAgo = DateUtil.getPlusMons("yyyy-MM-dd", "yyyy-MM-dd", yesterDay, -1);
		
		model.addAttribute("yesterDay", yesterDay);
		model.addAttribute("aMonthAgo", aMonthAgo);
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "execSqlStdChk/manageScheduler";
	}
}