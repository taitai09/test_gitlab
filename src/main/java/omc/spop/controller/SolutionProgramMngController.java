package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.json.simple.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.model.SolutionProgramMng;
import omc.spop.service.SolutionProgramMngService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class SolutionProgramMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SolutionProgramMngController.class);

	@Autowired
	private SolutionProgramMngService solutionProgramMngService;

	@RequestMapping(value = "/SolutionProgramMng", method = RequestMethod.GET)
	public String SolutionProgramMng(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "solutionProgramMng/solutionProgramMng";
	}

	@RequestMapping(value = "/SolutionProgramMng/ProgramList", method = RequestMethod.GET)
	public String ProgramListMng(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "solutionProgramMng/programList";
	}
	
	@RequestMapping(value = "/SolutionProgramMng/ProgramRule", method = RequestMethod.GET)
	public String ProgramRuleMng(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		model.addAttribute("auth_cd", SessionManager.getLoginSession().getUsers().getAuth_cd());
		
		return "solutionProgramMng/programRule";
	}
	

	/* 솔루션 프로그램 관리 - 솔루션 프로그램 목차관리  - 목차리스트 Tree */
	@RequestMapping(value = "/SolutionProgramMng/ProgramListTree", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProgramList(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model) {
		List<SolutionProgramMng> resultList = new ArrayList<SolutionProgramMng>();
		List<SolutionProgramMng> tempList = new ArrayList<SolutionProgramMng>();
		SolutionProgramMng tmp = new SolutionProgramMng();
		String returnValue = "";

		try {
			resultList = solutionProgramMngService.programListTree(solutionProgramMng);
				
			List<SolutionProgramMng> buildList = TreeWrite.buildSolutionProgramMngList(resultList, "-1");

			if(StringUtils.defaultString(solutionProgramMng.getIsChoice()).equals("menu")){
				tmp.setText("없음");
				tmp.setId("");
				tempList.add(tmp);
				buildList.addAll(tempList);
			}
			
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			returnValue = jsonArray.toString();

			System.out.println("returnValue======"+returnValue);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return returnValue;
	}
	
	/* 솔루션 프로그램 관리 - 솔루션 프로그램 목차관리  - 목차저장 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramList/Save", method = { RequestMethod.POST })
	@ResponseBody
	public Result SaveSolutionProgramList(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = solutionProgramMngService.saveSolutionProgramListMng(solutionProgramMng);
			result.setResult(true);
			result.setMessage(solutionProgramMng.getCrud_flag().equals("C") ? "저장 되었습니다." : "수정 되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}
	
	/* 솔루션 프로그램 관리 - 솔루션 프로그램 목차관리  - 목차 멀티저장 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramList/SaveMulti", method = { RequestMethod.POST })
	@ResponseBody
	public Result SaveMultiSolutionProgramList(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = solutionProgramMngService.saveMultiSolutionProgramListMng(solutionProgramMng);
			result.setResult(true);
			result.setMessage("수정 되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}

	/* 솔루션 프로그램 관리 - 솔루션 프로그램 목차관리 - 목차삭제 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramList/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result DeleteSolutionProgramList(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model) throws Exception {

		Result result = new Result();
		boolean menuIsEmpty = false;  //

		try {
				menuIsEmpty = solutionProgramMngService.contentsIsEmpty(solutionProgramMng);
			if(!menuIsEmpty){  // empty가 아닐경우
				result.setMessage("하위 목차가 있을시에는 삭제가 불가능합니다.");
				result.setResult(false);
				result.setResultCount(0);
				return result;
			}
			
				int check = 0;
				check += solutionProgramMngService.deleteSolutionProgramListMng(solutionProgramMng);
				result.setResult(check > 0 ? true : false);
				result.setMessage("삭제 되었습니다.");

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(methodName + "예외발생 ==> " + ex.getMessage());
			result.setResultCount(0);
			result.setResult(false);
		}
		return result;
	}

	
	/* 솔루션 프로그램 관리 - 프로그램 RULE 관리  - 리스트 */
	@RequestMapping(value="/SolutionProgramMng/ProgramRule", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ProgramRule(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model) {
		List<SolutionProgramMng> resultList = new ArrayList<SolutionProgramMng>();
		int dataCount4NextBtn = 0;

		try{
			resultList = solutionProgramMngService.programRule(solutionProgramMng);
			if (resultList != null && resultList.size() > solutionProgramMng.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(solutionProgramMng.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/* 솔루션 프로그램 관리 - 프로그램 RULE 관리  - 엑셀다운 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramRule/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ProgramRuleByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model)
			throws Exception {
		logger.debug("SolutionProgramMngByExcelDown Call");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			resultList = solutionProgramMngService.programRuleByExcelDown(solutionProgramMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		try {
		model.addAttribute("fileName", "솔루션_프로그램_RULE관리");
		model.addAttribute("sheetName", "솔루션_프로그램_RULE관리");
		model.addAttribute("excelId", "SLT_PROGRAM_SQL");
		// return a view which will be resolved by an excel view resolver
		
		logger.debug("solutionProgramMngByExcelDown resultList", resultList);
		}catch(Exception ex){
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());			
		}
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/* 솔루션 프로그램 관리 - 프로그램 RULE 관리  - 저장 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramRule/Save", method = { RequestMethod.POST })
	@ResponseBody
	public Result SaveProgramRule(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = solutionProgramMngService.saveProgramRule(solutionProgramMng);

			result.setResult(check > 0 ? true : false);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}
	/* 솔루션 프로그램 관리 - 프로그램 RULE 관리  - 삭제 */
	@RequestMapping(value = "/SolutionProgramMng/ProgramRule/Delete", method = { RequestMethod.POST })
	@ResponseBody
	public Result DeleteProgramRule(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = solutionProgramMngService.deleteProgramRule(solutionProgramMng);
			
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}

	
	
	
	/* 시스템 운영 현황 - 데이터 수집 현황 */
	@RequestMapping(value = "/SolutionProgramMng/DataCollectionStatus", method = RequestMethod.GET)
	public String DataCollectionStatus(@RequestParam(required = false) Map<String, String> param, Model model) {
//		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		String nowDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), -1);
		
		model.addAttribute("nowDate", nowDate);
		return "solutionProgramMng/dataCollectionStatus";
	}
	
	/* 시스템 운영 현황 - 데이터 수집 현황 - 리스트 */
	@RequestMapping(value = "/SolutionProgramMng/DataCollectionStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DataCollectionStatusList(@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = solutionProgramMngService.dataCollectionStatus(solutionProgramMng);
			
//			if(resultList != null && resultList.size() > solutionProgramMng.getPagePerCount()){
//				dataCount4NextBtn = resultList.size();
//				resultList.remove(solutionProgramMng.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
//			} else if(resultList != null && resultList.size() == 1 && resultList.get(0).containsKey("HEAD")) {
//				resultList.remove(resultList.size() - 1);
				dataCount4NextBtn = resultList.size();
//			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = getJSONResult(resultList, true).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jobj.toString();
	}
	
	
	/* 시스템 운영 현황 - 데이터 수집 현황 - 엑셀다운 */
	@RequestMapping(value = "/SolutionProgramMng/DataCollectionStatus/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownQualityTableMultiDynamic(HttpServletRequest req, HttpServletResponse res, 
			@ModelAttribute("solutionProgramMng") SolutionProgramMng solutionProgramMng, Model model) throws Exception {
		List multiDynamicResultList = new ArrayList();
		List<LinkedHashMap<String, Object>> resultList = null;
		List<String> sheetName = new ArrayList<String>();
		String menuNm = solutionProgramMng.getMenu_nm();
		
		try {
			resultList = solutionProgramMngService.dataCollectionStatus(solutionProgramMng);
			sheetName.add("데이터 수집 현황");
			multiDynamicResultList.add(resultList);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", menuNm);
		model.addAttribute("sheetName",sheetName);
		
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxMultiDynamicView", "multiDynamicResultList", multiDynamicResultList);
	}
	
}
