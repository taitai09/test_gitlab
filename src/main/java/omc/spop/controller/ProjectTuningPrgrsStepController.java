package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import omc.spop.model.ProjectTuningPrgrsStep;
import omc.spop.model.Result;
import omc.spop.service.ProjectTuningPrgrsStepService;

/***********************************************************
 * 2019.09.16 initiate
 **********************************************************/

@Controller
@RequestMapping(value = "/ProjectTuningPrgrsStep")
public class ProjectTuningPrgrsStepController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTuningPrgrsStepController.class);
	
	@Autowired
	private ProjectTuningPrgrsStepService projectTuningPrgrsStepService;
	
	/* 프로젝트 튜닝진행단계 리스트 */
	@RequestMapping(value = "/ProjectTuningPrgrsStepList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectTuningPrgrsStepList(@ModelAttribute("projectTuningPrgrsStep") ProjectTuningPrgrsStep projectTuningPrgrsStep, Model model) {
		List<ProjectTuningPrgrsStep> resultList = new ArrayList<ProjectTuningPrgrsStep>();
		int dataCount4NextBtn = 0;
		
		try {
			logger.debug("projectTuningPrgrsStep :"+projectTuningPrgrsStep);

			resultList = projectTuningPrgrsStepService.getProjectTuningPrgrsStepList(projectTuningPrgrsStep);
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
	
	/* 기준정보 설정 - 프로젝트 튜닝진행단계 - 저장 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public Result insertProjectTuningPrgrsStep(@ModelAttribute("projectTuningPrgrsStep") ProjectTuningPrgrsStep projectTuningPrgrsStep, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = projectTuningPrgrsStepService.insertProjectTuningPrgrsStep(projectTuningPrgrsStep);
			if(check == 0) {
				result.setResult(false);
			} else {
				result.setMessage("등록하였습니다.");
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	/* 기준정보 설정 - 프로젝트 튜닝진행단계 - 수정 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Result updateProjectTuningPrgrsStep(@ModelAttribute("projectTuningPrgrsStep") ProjectTuningPrgrsStep projectTuningPrgrsStep, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = projectTuningPrgrsStepService.updateProjectTuningPrgrsStep(projectTuningPrgrsStep);
			if(check == 0) {
				result.setResult(false);
			} else {
				result.setMessage("수정하였습니다.");
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectTuningPrgrsStep") ProjectTuningPrgrsStep projectTuningPrgrsStep, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = projectTuningPrgrsStepService.excelDownload(projectTuningPrgrsStep);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_튜닝진행단계");
		model.addAttribute("sheetName", "프로젝트_튜닝진행단계");
		model.addAttribute("excelId", "PROJECT_TUNING_PRGRS_STEP");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 튜닝진행단계 조회 */
	@RequestMapping(value = "/getTuningPrgrsStep", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTuningPrgrsStep(@ModelAttribute("projectTuningPrgrsStep") ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception {
		List<ProjectTuningPrgrsStep> outerList = new ArrayList<ProjectTuningPrgrsStep>();
		ProjectTuningPrgrsStep prjTuningPrgrsStep = new ProjectTuningPrgrsStep();
		List<ProjectTuningPrgrsStep> resultList = new ArrayList<ProjectTuningPrgrsStep>();
		
		prjTuningPrgrsStep.setTuning_prgrs_step_seq("");
		
		if(projectTuningPrgrsStep.getProject_id().equals("")) {
			prjTuningPrgrsStep.setTuning_prgrs_step_nm("해당없음");
			
			outerList.add(prjTuningPrgrsStep);
		} else {
			resultList = projectTuningPrgrsStepService.getProjectTuningPrgrsStepList(projectTuningPrgrsStep);
			
			String isAll = StringUtils.defaultString(projectTuningPrgrsStep.getIsAll());
			String isChoice = StringUtils.defaultString(projectTuningPrgrsStep.getIsChoice());
			String isNotApplicable = StringUtils.defaultString(projectTuningPrgrsStep.getIsNotApplicable());
			if (isAll.equals("Y")) {
				prjTuningPrgrsStep.setTuning_prgrs_step_nm("전체");
			} else if (isChoice.equals("Y")) {
				prjTuningPrgrsStep.setTuning_prgrs_step_nm("선택");
			} else if (isNotApplicable.equals("Y")) {
				prjTuningPrgrsStep.setTuning_prgrs_step_nm("해당없음");
			} else {
				prjTuningPrgrsStep.setTuning_prgrs_step_nm("");
			}
			
			outerList.add(prjTuningPrgrsStep);
			outerList.addAll(resultList);
		}
		
		return success(outerList).toJSONObject().get("rows").toString();
	}
}