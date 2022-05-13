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
import omc.spop.model.ProjectSqlQtyChkIdx;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.ProjectSqlQtyChkIdxMngService;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ProjectSqlQtyChkIdxMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectSqlQtyChkIdxMngController.class);

	@Autowired
	private ProjectSqlQtyChkIdxMngService projectSqlQtyChkIdxMngService;

	@RequestMapping(value = "/ProjectSqlQtyChkIdxMng", method = RequestMethod.GET)
	public String ProjectSqlQtyChkIdxMng(@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx,
			Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", projectSqlQtyChkIdx.getMenu_id());
		model.addAttribute("menu_nm", projectSqlQtyChkIdx.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "sqlStandards/projectSqlQtyChkIdxMng";
	}

	/* 기준정보 설정 - 프로젝트 관리 - 리스트 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdxList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectSqlQtyChkIdxList(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		List<ProjectSqlQtyChkIdx> resultList = new ArrayList<ProjectSqlQtyChkIdx>();
		int dataCount4NextBtn = 0;

		try {
			logger.debug("projectSqlQtyChkIdx :" + projectSqlQtyChkIdx);

			resultList = projectSqlQtyChkIdxMngService.getProjectSqlQtyChkIdxList(projectSqlQtyChkIdx);
//			if (resultList != null && resultList.size() > projectSqlQtyChkIdx.getPagePerCount()) {
//				dataCount4NextBtn = resultList.size();
//				resultList.remove(projectSqlQtyChkIdx.getPagePerCount());
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 지표 관리 - 적용 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/Apply", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkIdxApply(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			int cnt = projectSqlQtyChkIdxMngService.countProjectSqlStdQtyChkSql(projectSqlQtyChkIdx);
			if (cnt <= 0) {
				check = projectSqlQtyChkIdxMngService.insertProjectSqlQtyChkIdxApply(projectSqlQtyChkIdx);
				if (check == 0) {
					result.setResult(false);
				} else {
					result.setResult(true);
					result.setMessage("이미 프로젝트 품질점검 지표가 적용되었습니다.");
				}
			} else {
				result.setResult(false);
				result.setMessage("이미 프로젝트 품질점검 지표가 등록되어 있습니다.");
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 지표 관리 - 저장 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkIdxSave(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		String crudFlag = projectSqlQtyChkIdx.getCrud_flag();
		logger.debug("crudflag :" + projectSqlQtyChkIdx.getCrud_flag());
		Result result = new Result();
		int check = 0;

		try {
			if (crudFlag.equals("C")) {
				check = projectSqlQtyChkIdxMngService.insertProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
				if (check == 0) {
					result.setMessage("등록된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("등록하였습니다.");
					result.setResult(true);
				}
			} else {
				check = projectSqlQtyChkIdxMngService.deleteProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 지표 관리 - 저장 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkIdxInsert(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectSqlQtyChkIdxMngService.insertProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 지표 관리 - 수정 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkIdxUpdate(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectSqlQtyChkIdxMngService.updateProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
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

	/* SQL 표준 점검 - 프로젝트 SQL 품질점검 지표 관리 - 삭제 */
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectSqlQtyChkIdxDelete(
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectSqlQtyChkIdxMngService.deleteProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
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
	@RequestMapping(value = "/ProjectSqlQtyChkIdx/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectSqlQtyChkIdxMngExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectSqlQtyChkIdx") ProjectSqlQtyChkIdx projectSqlQtyChkIdx, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = projectSqlQtyChkIdxMngService.excelDownload(projectSqlQtyChkIdx);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_SQL_품질점검_지표_관리");
		model.addAttribute("sheetName", "프로젝트_SQL_품질점검_지표_관리");
		model.addAttribute("excelId", "PROJECT_SQL_QTY_CHK_IDX_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
