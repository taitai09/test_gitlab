package omc.mqm.controller;

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

import omc.mqm.model.ProjectModel;
import omc.mqm.service.ProjectModelMngService;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.model.Users;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ProjectModelMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectModelMngController.class);

	@Autowired
	private ProjectModelMngService projectModelMngService;

	@RequestMapping(value = "/Mqm/ProjectModelManagement", method = RequestMethod.GET)
	public String ProjectModelMng(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", projectModel.getMenu_id());
		model.addAttribute("menu_nm", projectModel.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "mqm/projectModelManagement";
	}

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 모델 관리 - 리스트 */
	@RequestMapping(value = "/ProjectModelList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectModelList(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		List<ProjectModel> resultList = new ArrayList<ProjectModel>();
		int dataCount4NextBtn = 0;

		try {
			logger.debug("projectModel :" + projectModel);

			resultList = projectModelMngService.getProjectModelList(projectModel);
			if (resultList != null && resultList.size() > projectModel.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(projectModel.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 모델 관리 - 저장 */
	@RequestMapping(value = "/ProjectModel/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectModelSave(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			String crudFlag = projectModel.getCrud_flag();
			if (crudFlag.equals("C")) {
				int dupCnt = projectModelMngService.getDupCnt(projectModel);
				if (dupCnt > 0) {
					result.setMessage("이미 등록된 데이터입니다.");
					result.setResult(false);
				} else {
					check = projectModelMngService.insertProjectModel(projectModel);
					if (check == 0) {
						result.setMessage("등록된 데이터가 없습니다.");
						result.setResult(false);
					} else {
						result.setMessage("등록하였습니다.");
						result.setResult(true);
					}
				}
			} else if (crudFlag.equals("U")) {
				check = projectModelMngService.updateProjectModel(projectModel);
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

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 모델 관리 - 저장 */
	@RequestMapping(value = "/ProjectModel/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectModelInsert(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectModelMngService.insertProjectModel(projectModel);
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

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 모델 관리 - 수정 */
	@RequestMapping(value = "/ProjectModel/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectModelUpdate(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectModelMngService.updateProjectModel(projectModel);
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

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 모델 관리 - 삭제 */
	@RequestMapping(value = "/ProjectModel/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectModelDelete(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = projectModelMngService.deleteProjectModel(projectModel);
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
	@RequestMapping(value = "/ProjectModel/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectModelExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectModel") ProjectModel projectModel, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = projectModelMngService.excelDownload(projectModel);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_모델_관리");
		model.addAttribute("sheetName", "프로젝트_모델_관리");
		model.addAttribute("excelId", "PROJECT_MODEL_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
