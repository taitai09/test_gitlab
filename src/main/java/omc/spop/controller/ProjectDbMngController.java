package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import omc.spop.model.ProjectDb;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.ProjectDbMngService;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ProjectDbMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectDbMngController.class);

	@Autowired
	private ProjectDbMngService projectDbMngService;

	@RequestMapping(value = "/ProjectDbMng", method = RequestMethod.GET)
	public String ProjectDbMng(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", projectDb.getMenu_id());
		model.addAttribute("menu_nm", projectDb.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectDbMng";
	}

	/* 기준정보 설정 - 프로젝트 DB 관리 - 리스트 */
	@RequestMapping(value = "/ProjectDbList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectDbList(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		List<ProjectDb> resultList = new ArrayList<ProjectDb>();

		try {
			logger.debug("projectDb :" + projectDb);

			resultList = projectDbMngService.getProjectDbList(projectDb);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 기준정보 설정 - 프로젝트 DB 관리- 저장 */
	@RequestMapping(value = "/ProjectDb/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeSave(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			String crudFlag = projectDb.getCrud_flag();
			if (crudFlag.equals("C")) {
				int dupCnt = projectDbMngService.getDupCnt(projectDb);
				if (dupCnt > 0) {
					result.setMessage("이미 등록된 데이터입니다.<br>동일한 데이터는 등록할 수 없습니다.");
					result.setResult(false);
				} else {
					check = projectDbMngService.insertProjectDb(projectDb);
					if (check == 0) {
						result.setMessage("등록중 오류가 발생하였습니다.");
						result.setResult(false);
					} else {
						result.setMessage("등록하였습니다.");
						result.setResult(true);
					}
				}
			} else if (crudFlag.equals("U")) {
				check = projectDbMngService.updateProjectDb(projectDb);
				if (check == 0) {
					result.setMessage("수정된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("수정하였습니다.");
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

	/* 기준정보 설정 - 프로젝트 DB 관리- 저장 */
	@RequestMapping(value = "/ProjectDb/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectDbInsert(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectDbMngService.insertProjectDb(projectDb);
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

	/* 기준정보 설정 - 프로젝트 DB 관리- 수정 */
	@RequestMapping(value = "/ProjectDb/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectDbUpdate(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectDbMngService.updateProjectDb(projectDb);
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

	/* 기준정보 설정 - 프로젝트 DB 관리- 삭제 */
	@RequestMapping(value = "/ProjectDb/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectDbDelete(@ModelAttribute("projectDb") ProjectDb projectDb, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			int validationCheck = projectDbMngService.validationCheckProjectDb(projectDb);
			logger.debug("validationCheck :"+validationCheck);
			if (validationCheck > 0) {
				result.setResult(false);
				result.setMessage("삭제할 원천DB에서 성능점검 수행이력이 있어서 삭제할 수 없습니다.");
			} else {
				check = projectDbMngService.deleteProjectDb(projectDb);
				if (check == 0) {
					result.setResult(false);
					result.setMessage("프로젝트 DB 삭제중 오류가 발생하였습니다.");
				} else {
					result.setResult(true);
					result.setMessage("프로젝트 DB를 삭제하였습니다.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 엑셀 다운로드 */
	@RequestMapping(value = "/ProjectDb/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectDbMngExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectDb") ProjectDb projectDb, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = projectDbMngService.excelDownload(projectDb);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_DB_관리");
		model.addAttribute("sheetName", "프로젝트_DB_관리");
		model.addAttribute("excelId", "PROJECT_DB_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
