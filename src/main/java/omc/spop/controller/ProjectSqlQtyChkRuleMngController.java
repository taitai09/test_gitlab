package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.ProjectSqlQtyChkRuleMngService;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ProjectSqlQtyChkRuleMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectSqlQtyChkRuleMngController.class);

	@Autowired
	private ProjectSqlQtyChkRuleMngService projectSqlQtyChkRuleMngService;

	@RequestMapping(value = "/ProjectSqlQtyChkRuleMng", method = RequestMethod.GET)
	public String ProjectSqlQtyChkRuleMng(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", projectSqlQtyChkRule.getMenu_id());
		model.addAttribute("menu_nm", projectSqlQtyChkRule.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "sqlStandards/projectSqlQtyChkRuleMng";
	}

	/* 기준정보 설정 - 프로젝트 관리 - 리스트 */
	@RequestMapping(value = "/ProjectSqlQtyChkRuleList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectSqlQtyChkRuleList(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {

		List<ProjectSqlQtyChkRule> resultList = new ArrayList<ProjectSqlQtyChkRule>();
//		int dataCount4NextBtn = 0;

		try {
			logger.debug("projectSqlQtyChkRule :" + projectSqlQtyChkRule);

			resultList = projectSqlQtyChkRuleMngService.getProjectSqlQtyChkRuleList(projectSqlQtyChkRule);
//			if (resultList != null && resultList.size() > projectSqlQtyChkRule.getPagePerCount()) {
//				dataCount4NextBtn = resultList.size();
//				resultList.remove(projectSqlQtyChkRule.getPagePerCount());
//			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
//		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 적용 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/Apply", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkRuleApply(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			int cnt = projectSqlQtyChkRuleMngService.countProjectSqlStdQtyChkSql(projectSqlQtyChkRule);
			if (cnt <= 0) {
				check = projectSqlQtyChkRuleMngService.insertProjectSqlQtyChkRuleApply(projectSqlQtyChkRule);
				if (check == 0) {
					result.setResult(false);
				} else {
					result.setResult(true);
					result.setMessage("이미 프로젝트 품질점검 RULE이 적용되었습니다.");
				}
			} else {
				result.setResult(false);
				result.setMessage("이미 프로젝트 품질점검 RULE이 등록되어 있습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 저장 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkRuleSave(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			logger.debug("SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 저장");

			String qty_chk_idt_cd = projectSqlQtyChkRule.getQty_chk_idt_cd();
			String project_id = projectSqlQtyChkRule.getProject_id();
			String qty_chk_sql = projectSqlQtyChkRule.getQty_chk_sql();
			logger.debug("qty_chk_idt_cd :" + qty_chk_idt_cd);
			logger.debug("project_id :" + project_id);
			logger.debug("qty_chk_sql :" + qty_chk_sql);
			String crudFlag = projectSqlQtyChkRule.getCrud_flag();
			if (crudFlag.equals("C")) {
				check = projectSqlQtyChkRuleMngService.insertProjectSqlQtyChkRule(projectSqlQtyChkRule);
				if (check == 0) {
					result.setMessage("등록된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("등록하였습니다.");
					result.setResult(true);
				}
			} else if (crudFlag.equals("U")) {
				check = projectSqlQtyChkRuleMngService.updateProjectSqlQtyChkRule(projectSqlQtyChkRule);
				if (check == 0) {
					result.setMessage("저장된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("저장하였습니다.");
					result.setResult(true);
				}

			} else if (crudFlag.equals("D")) {
				check = projectSqlQtyChkRuleMngService.deleteProjectSqlQtyChkRule(projectSqlQtyChkRule);
				if (check == 0) {
					result.setMessage("저장된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("저장하였습니다.");
					result.setResult(true);
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 저장 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkRuleInsert(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			logger.debug("SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 저장");

			String qty_chk_idt_cd = projectSqlQtyChkRule.getQty_chk_idt_cd();
			String project_id = projectSqlQtyChkRule.getProject_id();
			String qty_chk_sql = projectSqlQtyChkRule.getQty_chk_sql();
			logger.debug("qty_chk_idt_cd :" + qty_chk_idt_cd);
			logger.debug("project_id :" + project_id);
			logger.debug("qty_chk_sql :" + qty_chk_sql);

			check = projectSqlQtyChkRuleMngService.insertProjectSqlQtyChkRule(projectSqlQtyChkRule);
			if (check == 0)
				result.setResult(false);
			else
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 수정 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkRuleUpdate(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Result result = new Result();
		int check = 0;

		String qty_chk_idt_cd = projectSqlQtyChkRule.getQty_chk_idt_cd();
		String project_id = projectSqlQtyChkRule.getProject_id();
		String qty_chk_sql = projectSqlQtyChkRule.getQty_chk_sql();
		logger.debug("qty_chk_idt_cd :" + qty_chk_idt_cd);
		logger.debug("project_id :" + project_id);
		logger.debug("qty_chk_sql :" + qty_chk_sql);

		try {
			logger.debug("SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 수정");
			check = projectSqlQtyChkRuleMngService.updateProjectSqlQtyChkRule(projectSqlQtyChkRule);
			if (check == 0)
				result.setResult(false);
			else
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 삭제 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkRuleDelete(
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			logger.debug("SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 삭제");
//			check = projectSqlQtyChkRuleMngService.deleteProjectSqlQtyChkRule(projectSqlQtyChkRule);
			if (check == 0)
				result.setResult(false);
			else
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

	/* 엑셀 다운로드 */
	@RequestMapping(value = "/ProjectSqlQtyChkRule/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectSqlQtyChkRuleMngExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			logger.debug("SQL 표준 점검 - 프로젝트 SQL 품질점검 RULE 관리 - 엑셀 다운로드");
			resultList = projectSqlQtyChkRuleMngService.excelDownload(projectSqlQtyChkRule);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_SQL_품질점검_RULE_관리");
		model.addAttribute("sheetName", "프로젝트_SQL_품질점검_RULE_관리");
		model.addAttribute("excelId", "PROJECT_SQL_QTY_CHK_RULE_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
