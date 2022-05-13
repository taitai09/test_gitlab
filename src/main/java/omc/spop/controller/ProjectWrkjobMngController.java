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

import omc.mqm.model.ModelEntityType;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ProjectWrkjob;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.ProjectWrkjobMngService;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ProjectWrkjobMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectWrkjobMngController.class);

	@Autowired
	private ProjectWrkjobMngService projectWrkjobMngService;

	@RequestMapping(value = "/ProjectWrkjobMng", method = RequestMethod.GET)
	public String ProjectWrkjobMng(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("menu_id", projectWrkjob.getMenu_id());
		model.addAttribute("menu_nm", projectWrkjob.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectWrkjobMng";
	}
	
	/* 기준정보 설정 - 프로젝트 업무 관리 - 리스트*/
	@RequestMapping(value = "/ProjectWrkjobList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectWrkjobList(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		List<ProjectWrkjob> resultList = new ArrayList<ProjectWrkjob>();
		int dataCount4NextBtn = 0;
		
		try {
			logger.debug("projectWrkjob :"+projectWrkjob);

			resultList = projectWrkjobMngService.getProjectWrkjobList(projectWrkjob);
			if (resultList != null && resultList.size() > projectWrkjob.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(projectWrkjob.getPagePerCount());
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
	
	/* 기준정보 설정 - 프로젝트 업무 관리 - 저장 */
	@RequestMapping(value = "/ProjectWrkjob/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeSave(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			String crudFlag = projectWrkjob.getCrud_flag();
			if (crudFlag.equals("C")) {
				int dupCnt = projectWrkjobMngService.getDupCnt(projectWrkjob);
				if (dupCnt > 0) {
					result.setMessage("이미 등록된 데이터입니다.");
					result.setResult(false);
				} else {
					check = projectWrkjobMngService.insertProjectWrkjob(projectWrkjob);
					if (check == 0) {
						result.setMessage("등록중 오류가 발생하였습니다.");
						result.setResult(false);
					} else {
						result.setMessage("등록하였습니다.");
						result.setResult(true);
					}
				}
			} else if (crudFlag.equals("U")) {
				check = projectWrkjobMngService.updateProjectWrkjob(projectWrkjob);
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
	
	/* 기준정보 설정 - 프로젝트 업무 관리 - 저장 */
	@RequestMapping(value = "/ProjectWrkjob/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectWrkjobInsert(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = projectWrkjobMngService.insertProjectWrkjob(projectWrkjob);
			if(check == 0) result.setResult(false); else result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	/* 기준정보 설정 - 프로젝트 업무 관리 - 수정 */
	@RequestMapping(value = "/ProjectWrkjob/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectWrkjobUpdate(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = projectWrkjobMngService.updateProjectWrkjob(projectWrkjob);
			if(check == 0) result.setResult(false); else result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	/* 기준정보 설정 - 프로젝트 업무 관리 - 삭제 */
	@RequestMapping(value = "/ProjectWrkjob/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectWrkjobDelete(@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = projectWrkjobMngService.deleteProjectWrkjob(projectWrkjob);
			if(check == 0) result.setResult(false); else result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 엑셀 다운로드 */
	@RequestMapping(value = "/ProjectWrkjob/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView projectWrkjobMngExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectWrkjob") ProjectWrkjob projectWrkjob, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = projectWrkjobMngService.excelDownload(projectWrkjob);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_업무_관리");
		model.addAttribute("sheetName", "프로젝트_업무_관리");
		model.addAttribute("excelId", "PROJECT_WRKJOB_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
