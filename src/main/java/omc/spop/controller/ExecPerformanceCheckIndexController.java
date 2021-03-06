package omc.spop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Cd;
import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.server.Iqms;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.CommonService;
import omc.spop.service.ExecPerformanceCheckIndexService;
import omc.spop.service.ExecPerformanceCheckMngService;
import omc.spop.service.impl.PerfInspectMngServiceImple;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

@RequestMapping(value="/ExecPerformanceCheckIndex")
@Controller
public class ExecPerformanceCheckIndexController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExecPerformanceCheckIndexController.class);
	
	@Autowired
	private ExecPerformanceCheckMngService execPerfChkMngSvc;
	
	@Autowired
	private ExecPerformanceCheckIndexService execPerformanceCheckIndexService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PerfInspectMngServiceImple perfIMngSvc;

	/*???????????? > ???????????? ????????????*/
	@RequestMapping(value = "/DeployPrefChkIndc", method = RequestMethod.GET)
	public String deployPrefChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/deployPerfChkIndc";
	}
	/* ???????????? ?????? */
	@RequestMapping(value = "/getCommonCode", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCode(@ModelAttribute("cd") Cd cd) throws Exception {
		String isAll = StringUtils.defaultString(cd.getIsAll());
		String isChoice = StringUtils.defaultString(cd.getIsChoice());
		List<Cd> commonList = new ArrayList<Cd>();
		
		Cd temp = new Cd();
		if(isAll.equals("Y")){
			temp.setCd("");
			temp.setCd_nm("??????");
		}else if(isChoice.equals("Y")){
			temp.setCd("");
			temp.setCd_nm("??????");
		}else if(isAll.equals("N") && isChoice.equals("N")){
		}else{
			temp.setCd("");
			temp.setCd_nm("??????");
		}

		commonList.add(temp);

		try {
			List<Cd> commonList2 = commonService.commonCodeList(cd);
			commonList.addAll(commonList2);
			commonList.remove(commonList.size() -1);
		} catch (Exception ex) {
			logger.error("Common Error ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(commonList).toJSONObject().get("rows").toString();
	}
	/*????????? ?????????????????????????????? ??????*/
	@RequestMapping(value = "/DeployPrefChkIndc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkIndc(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*???????????? ???????????? ??????*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckMethCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getRangeWhether(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckMethCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*????????? ?????????????????????????????? ??????*/
	@RequestMapping(value="/DeployPrefChkIndc/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveDeployPrefChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.saveDeployPerfChkIndc(deployPerfChkIndc);
			if(check == -1){
				result.setResult(false);
				result.setResultCount(check);
			}else{
				result.setResult(true);
				result.setResultCount(check);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/**
	 * ???????????? ?????? ????????????
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeployPrefChkIndc/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView DeployPerfChkIndcExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkIndcByExcelDown(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
		
		fileName = "??????_??????_??????_??????";
		sheetName = "??????_??????_??????_??????";
		excelId = "DEPLOY_PERF_CHK_INDC_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * ???????????? ?????? ????????????
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/WjPerfChkIndc/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView WjperfChkIndcExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = execPerformanceCheckIndexService.getWjPerfChkIndcByExcelDown(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
		
		fileName = "?????????_??????_??????_??????_??????";
		sheetName = "?????????_??????_??????_??????_??????";
		excelId = "WJ_PERF_CHK_INDC_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	

	/*???????????? > ???????????? ????????????*/
	@RequestMapping(value = "/WjPerfChkIndc", method = RequestMethod.GET)
	public String WjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/wjPerfChkIndc";
	}

	/*????????? ?????????????????????????????? ??????*/
	@RequestMapping(value = "/WjPerfChkIndc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

		try {
			resultList = execPerformanceCheckIndexService.getWjPerfChkIndc(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*???????????? ???????????? ??????*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckProgramDivCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckProgramDivCd(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckProgramDivCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}

	/*???????????? ????????? ?????? ?????? ??????*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getYnDecideDivCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getYnDecideDivCd(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getYnDecideDivCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*???????????? ????????? ?????? ?????? ??????*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckIndcId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckIndcId(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckIndcId(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	
	/*???????????? ????????? ?????? ?????? ?????? */
	@RequestMapping(value="/WjPerfChkIndc/Check", method=RequestMethod.POST)
	@ResponseBody
	public Result checkWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		Result result = new Result();
		try{
			int check = execPerformanceCheckIndexService.checkWjPerfChkIndc(deployPerfChkIndc);
			if(check == 1){
				result.setResult(false);
				return result;
			}else{
				result.setResult(true);
				return result;
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/*????????? ?????????????????????????????? ??????*/
	@Transactional
	@RequestMapping(value="/WjPerfChkIndc/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		Result result = new Result();
		try{
			int check = execPerformanceCheckIndexService.saveWjPerfChkIndc(deployPerfChkIndc);
			if(check == -1){
				result.setMessage("[ ???????????? ] ??????????????? ??????????????????");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	
	/*????????? ?????????????????????????????? ??????*/
	@Transactional
	@RequestMapping(value="/WjPerfChkIndc/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		Result result = new Result();
		try{
			int check = execPerformanceCheckIndexService.deleteWjPerfChkIndc(deployPerfChkIndc);
			if(check == -1){
				result.setMessage("[ ???????????? ] ??????????????? ??????????????????");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/*???????????? > ???????????? ?????? ??????*/
	@RequestMapping(value = "/ProPerfExcReq", method = RequestMethod.GET)
	public String ProPerfExcReq(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
//		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -1);    // ?????? ???
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//		String startDate = dateFormatter.format(cal.getTime());
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd","yyyy-MM-dd", nowDate,-2);
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		//model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
//		model.addAttribute("data", deployPerfChkIndc);
		model.addAttribute("search_wrkjob_cd", deployPerfChkIndc.getSearch_wrkjob_cd());
		model.addAttribute("search_wrkjob_cd_nm", deployPerfChkIndc.getSearch_wrkjob_cd_nm());
		model.addAttribute("search_dbio", deployPerfChkIndc.getSearch_dbio());
		model.addAttribute("search_program_nm", deployPerfChkIndc.getSearch_program_nm());
		model.addAttribute("search_deploy_id", deployPerfChkIndc.getSearch_deploy_id());
		model.addAttribute("search_deploy_requester", deployPerfChkIndc.getSearch_deploy_requester());
		model.addAttribute("call_from_parent", deployPerfChkIndc.getCall_from_parent());
		model.addAttribute("call_from_child", deployPerfChkIndc.getCall_from_child());
		model.addAttribute("deployPerfChkIndc", deployPerfChkIndc);

		logger.debug("call_from_parent:::::???:::######::"+deployPerfChkIndc.getCall_from_parent());
		logger.debug("call_from_child:::::???:::######::"+deployPerfChkIndc.getCall_from_child());
		
		model.addAttribute("search_perf_check_id", deployPerfChkIndc.getSearch_perf_check_id());
		model.addAttribute("search_perf_check_step_id", deployPerfChkIndc.getSearch_perf_check_step_id());
		model.addAttribute("search_program_id", deployPerfChkIndc.getSearch_program_id());
		model.addAttribute("search_program_execute_tms", deployPerfChkIndc.getSearch_program_execute_tms());
		
		return "performanceCheckIndex/proPerfExcReq";
	}
	
	/*????????? ?????????????????????????????? ??????*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ProPerfExcReq", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProPerfExcReq(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		int dataCount4NextBtn = 0;
		try {
			resultList = execPerformanceCheckIndexService.getProPerfExcReq(deployPerfChkIndc);
			
			if(resultList != null && resultList.size() > deployPerfChkIndc.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(deployPerfChkIndc.getPagePerCount());
				/*???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ??????????????????*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}

	/* ???????????????- Explain Plan Tree */
	@RequestMapping(value="/DeployPerfChkPlanTableList", method=RequestMethod.POST)
	@ResponseBody
	public Result getDeployPerfChkPlanTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		Result result = new Result();
		String returnValue = "";
		resultList = execPerformanceCheckIndexService.getDeployPerfChkPlanTableList(deployPerfChkIndc);
		List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
		JSONArray jsonArray = JSONArray.fromObject(buildList);
		
		returnValue = jsonArray.toString();
		result.setResult(true);
		result.setTxtValue(returnValue);
		return result;	
	}
	
	/*????????? ?????????????????????????????? ?????? ??? ?????? ??????*/
	@RequestMapping(value = "/ProPerfExcReq/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceCheckMngListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = execPerformanceCheckIndexService.getProPerfExcReqByExcelDown(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		};
		model.addAttribute("fileName", "??????_??????_??????_??????");
		model.addAttribute("sheetName", "??????_??????_??????_??????");
		model.addAttribute("excelId", "PRO_PERF_EXC_REQ_ESPC");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/*????????? ???????????? ????????? ????????? ??????*/
	@RequestMapping(value = "/BindTableList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getBindTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

		try {
			resultList = execPerformanceCheckIndexService.getBindTableList(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*????????? ?????? ?????? ?????? ????????? ????????? ??????*/
	@RequestMapping(value = "/PerfCheckResultTableList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		List<PerformanceCheckMng> resultListPerfChkMng = new ArrayList<PerformanceCheckMng>();
		try {
			//?????? SQL ?????? ??????
				PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
				performanceCheckMng.setProgram_id(deployPerfChkIndc.getProgram_id());
				performanceCheckMng.setPerf_check_id(deployPerfChkIndc.getPerf_check_id());
				performanceCheckMng.setPerf_check_step_id(deployPerfChkIndc.getLast_perf_check_step_id());
				performanceCheckMng.setProgram_execute_tms(deployPerfChkIndc.getProgram_execute_tms());
				resultListPerfChkMng = perfIMngSvc.selectDeployPerfChkDetailResultList(performanceCheckMng);
				return success(resultListPerfChkMng).toJSONObject().toString();
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
	}
	
	/*???????????? > ???????????? ????????????*/
	@RequestMapping(value = "/DeployPerfChkStep", method = RequestMethod.GET)
	public String deployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/deployPerfChkStep";
	}
	
	/*???????????? ???????????? ??????*/
	@RequestMapping(value = "/DeployPerfChkStep", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkStep(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*?????????????????? ??????*/
	@RequestMapping(value="/DeployPerfChkStep/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveDeployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.saveDeployPerfChkStep(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/**
	 * ???????????? ?????? ????????????
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/DeployPerfChkStep/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deployPerfChkStepExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkStepByExcelDown(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
	
		fileName = "??????_??????_??????_??????";
		sheetName = "??????_??????_??????_??????";
		excelId = "DEPLOY_PERF_CHK_STEP_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/*???????????? > ???????????? ????????????*/
	@RequestMapping(value = "/DeployPerfChkStepTestDB", method = RequestMethod.GET)
	public String deployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		return "performanceCheckIndex/deployPerfChkStepTestDB";
	}
	
	/*???????????? ???????????? ??????*/
	@RequestMapping(value = "/DeployPerfChkStepTestDB", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		
		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkStepTestDB(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/*????????????  ??????*/        
	@RequestMapping(value = "/DeployPerfChkStep/DeployPerfChkStepId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStepId(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getDeployPerfChkStepId(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*??????????????????????????? ??????*/
	@RequestMapping(value="/DeployPerfChkStepTestDB/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveDeployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.saveDeployPerfChkStepTestDB(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	/* ???????????? ?????? ?????? ?????? ?????? - ExceptionPrcStatusCd ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd2", method=RequestMethod.POST)
	@ResponseBody
	public Result CheckExceptionPrcStatusCd2(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model){
		Result result = new Result();
		try{
			deployPerfChkIndc.setException_prc_status_cd("1");
			DeployPerfChkIndc deployPerfChkIndc1 = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(deployPerfChkIndc);
			
			if(deployPerfChkIndc1 != null ){  //deployPerfChkIndc1 ?????? ?????????,
				if(deployPerfChkIndc1.getPerf_check_id() != null && !deployPerfChkIndc1.getPerf_check_id().equals("") && !deployPerfChkIndc1.getPerf_check_id().equals("0")){
					String addText ="";
					if(deployPerfChkIndc.getDeploy_id() != null && !deployPerfChkIndc.getDeploy_id().equals("")){
						addText = "????????? : "+deployPerfChkIndc.getDeploy_id()+",<br/>";
					}
					result.setTxtValue("?????? ?????? ?????? ?????? ?????? ???????????????. <br/>?????? ?????? ??? ????????? ????????????.<br/>[ "+addText+"??????????????? : "+deployPerfChkIndc.getProgram_nm()+",<br/>SQL????????? : "+deployPerfChkIndc.getDbio() +" ]");
				
				}else{ //deployPerfChkIndc1 ?????? ?????????
					result.setTxtValue("");; //??????
				}
			}else{
				result.setTxtValue("");; //??????
			}
			result.setResult(true);
		} catch (Exception ex){
			ex.printStackTrace();
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	/* ???????????? ?????? ?????? ?????? ?????? - ExceptionPrcStatusCd ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd", method=RequestMethod.POST)
	@ResponseBody
	public Result CheckExceptionPrcStatusCd(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model){
		Result result = new Result();
		try{
			deployPerfChkIndc.setException_prc_status_cd("1");
			DeployPerfChkIndc deployPerfChkIndc1 = execPerformanceCheckIndexService.checkExceptionPrcStatusCd(deployPerfChkIndc);
			if(deployPerfChkIndc1 != null ){
				if(deployPerfChkIndc1.getPerf_check_id() != null && !deployPerfChkIndc1.getPerf_check_id().equals("") && !deployPerfChkIndc1.getPerf_check_id().equals("0")){
					String addText ="";
					if(deployPerfChkIndc.getDeploy_id() != null && !deployPerfChkIndc.getDeploy_id().equals("")){
						addText = "????????? : "+deployPerfChkIndc.getDeploy_id()+",<br/>";
					}
					result.setTxtValue("?????? ?????? ?????? ?????? ?????? ???????????????. <br/>?????? ?????? ??? ????????? ????????????.<br/>[ "+addText+"??????????????? : "+deployPerfChkIndc.getProgram_nm()+",<br/>SQL????????? : "+deployPerfChkIndc.getDbio() +" ]");
				}else{
					result.setTxtValue("");; //??????
				}
			}else{
				result.setTxtValue("");; //??????
			}
			result.setResult(true);
		} catch (Exception ex){
			ex.printStackTrace();
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	/* ???????????? ?????? ?????? ?????? ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/Request", method=RequestMethod.POST)
	@ResponseBody
	public Result requestDeployPerfChkExcptRequest(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
//		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		Result result = new Result();
		int check = 0;
		try{
//			deployPerfChkIndc.setUser_id(user_id);
			check = execPerformanceCheckIndexService.requestDeployPerfChkExcptRequest(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* ???????????? ?????? ?????? ?????? ?????? ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/MultiRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result multiRequestDeployPerfChkExcptRequest(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{

		Result result = new Result();
		try{
			int check = 0;
			String program_id_check = StringUtils.defaultString(deployPerfChkIndc.getProgram_id()); 
			int array_length = program_id_check.split(",").length;
			DeployPerfChkIndc temp = new DeployPerfChkIndc();
			List<HashMap<String, String>> listTemp = new ArrayList<HashMap<String, String>>();
			String program_nm, dbio, deploy_id, perf_check_id, program_id, exception_requester_id, excepter_id, exception_prc_why, exception_prc_dt, last_perf_check_step_id;
			
			
			for(int i = 0; i < array_length; i++){
				//????????? 0 ????????? ?????? ?????????... ?????? ?????????!!!
				program_nm = deployPerfChkIndc.getProgram_nm().split(",").length > 0 ? deployPerfChkIndc.getProgram_nm().split(",")[i] : "";
				dbio = deployPerfChkIndc.getDbio().split(",").length > 0 ? deployPerfChkIndc.getDbio().split(",")[i] : "";
				deploy_id = deployPerfChkIndc.getDeploy_id().split(",").length > 0 ? deployPerfChkIndc.getDeploy_id().split(",")[i] : "";
				perf_check_id = deployPerfChkIndc.getPerf_check_id().split(",").length > 0 ? deployPerfChkIndc.getPerf_check_id().split(",")[i] : "";
				program_id =  deployPerfChkIndc.getProgram_id().split(",").length > 0 ? deployPerfChkIndc.getProgram_id().split(",")[i] : "";
				exception_requester_id = deployPerfChkIndc.getException_requester_id().split(",").length > 0 ? deployPerfChkIndc.getException_requester_id().split(",")[i] : "";
//				exception_request_why_cd = deployPerfChkIndc.getException_request_why_cd().split(",").length > 0 ? deployPerfChkIndc.getException_request_why_cd().split(",")[i] : "";  //input????????? ?????? 
//				exception_request_detail_why = deployPerfChkIndc.getException_request_detail_why().split(",").length > 0 ? deployPerfChkIndc.getException_request_detail_why().split(",")[i] : ""; //input????????? ??????
//				exception_prc_meth_cd = deployPerfChkIndc.getException_prc_meth_cd().split(",").length > 0 ? deployPerfChkIndc.getException_prc_meth_cd().split(",")[i] : "";  //input????????? ??????
				excepter_id = deployPerfChkIndc.getExcepter_id().split(",").length > 0 ? deployPerfChkIndc.getExcepter_id().split(",")[i] : "";
				exception_prc_why =  deployPerfChkIndc.getException_prc_why().split(",").length > 0 ? deployPerfChkIndc.getException_prc_why().split(",")[i] : "";
				exception_prc_dt = deployPerfChkIndc.getException_prc_dt().split(",").length > 0 ? deployPerfChkIndc.getException_prc_dt().split(",")[i] : "";
				last_perf_check_step_id = deployPerfChkIndc.getLast_perf_check_step_id().split(",").length > 0 ? deployPerfChkIndc.getLast_perf_check_step_id().split(",")[i] : "";
					
				temp.setProgram_nm(program_nm);
				temp.setDbio(dbio);
				temp.setDeploy_id(deploy_id);
				
				temp.setPerf_check_id(perf_check_id);
				temp.setProgram_id(program_id);
				temp.setException_requester_id(exception_requester_id);
				temp.setException_request_why_cd(StringUtils.defaultString(deployPerfChkIndc.getException_request_why_cd()));
				temp.setException_request_detail_why(StringUtils.defaultString(deployPerfChkIndc.getException_request_detail_why()));
				temp.setException_prc_meth_cd(StringUtils.defaultString(deployPerfChkIndc.getException_prc_meth_cd()));
				temp.setException_prc_status_cd("1");
				temp.setExcepter_id(excepter_id);
				temp.setException_prc_why(exception_prc_why);
				temp.setException_prc_dt(exception_prc_dt);
				temp.setLast_perf_check_step_id(last_perf_check_step_id);
				temp.setPerf_check_auto_pass_del_yn("N");
				//???????????????????????? ????????? ??????
				DeployPerfChkIndc resultTemp = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(temp);
				//???????????????????????? ????????? SKIP 
				if(resultTemp != null && !StringUtils.defaultString(resultTemp.getPerf_check_id(),"").equals("") 
						&& !StringUtils.defaultString(resultTemp.getProgram_id()).equals("")){  //temp ?????? ?????????,
		
				//????????????????????????????????? ????????????????????? ??????????????? ?????????????????? ??????
					HashMap<String, String> mapTemp = new HashMap<String, String>();
					mapTemp.put("deploy_id", deploy_id);
					mapTemp.put("dbio", dbio);
					mapTemp.put("program_nm", program_nm);
					listTemp.add(mapTemp);
				//???????????????????????? ???????????? ???????????? INSERT
				}else{
					check += execPerformanceCheckIndexService.multiRequestDeployPerfChkExcptRequest(temp);
				}
			}
			
			//???????????? ???????????? ???????????? ?????? ??????
			String text = "";
			if(listTemp != null && listTemp.size() > 0){
				text += "?????????????????? ???????????? ????????? ?????????????????????.<br/>" ;
				text += "[ ??????????????? : ";
				for(int i = 0; i < listTemp.size(); i++){
					text += listTemp.get(i).get("program_nm");
					if(listTemp.size() == (i+1)){
						text += " ]<br/>";
					}else{
						text += ", ";
					}
				}
				text += "(??? "+array_length+"??? ??? "+listTemp.size()+"??? ??????)";
			}
			result.setTxtValue(text);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			logger.error(ex.toString());
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* ???????????? ?????? ?????? ?????? ????????????*/
	@Transactional
	@RequestMapping(value="/DeployPerfChkExcptRequest/Cancel", method=RequestMethod.POST)
	@ResponseBody
	public Result cancelDeployPerfChkExcptRequest(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.cancelDeployPerfChkExcptRequest(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	/* ???????????? ?????? ?????? ?????? ????????????*/
	@Transactional
	@RequestMapping(value="/programSourceDesc", method=RequestMethod.POST)
	@ResponseBody
	public Result getProgramSourceDesc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			String textValue = execPerformanceCheckIndexService.getProgramSourceDesc(deployPerfChkIndc);
			result.setTxtValue(textValue);;
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	
	//????????? ???????????? ??? ????????? ?????? - ????????????
	@RequestMapping(value = "/ExceptionHandling/CreateTab", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public ModelAndView getDeployPerfChkDetailResultByTab(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,
			Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(new java.util.Date());
		int checkExceptionRequestCnt = 0;

		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		Cd cd = new Cd();
		List<Cd> textValueList = new ArrayList<Cd>();
		
		try {
			if(!StringUtils.defaultString(deployPerfChkIndc.getProgram_id()).equals("") && !StringUtils.defaultString(deployPerfChkIndc.getLast_perf_check_step_id()).equals("") 
					&& !StringUtils.defaultString(deployPerfChkIndc.getPerf_check_id()).equals("")){
				resultList = execPerformanceCheckIndexService.getDeployPerfChkDetailResultByTabEspc(deployPerfChkIndc);
				model.addAttribute("perf_check_step_id",deployPerfChkIndc.getPerf_check_step_id());
			}
			if(!StringUtils.defaultString(deployPerfChkIndc.getException_requester_id()).equals("")){
				checkExceptionRequestCnt = execPerformanceCheckIndexService.checkExceptionRequestCnt(deployPerfChkIndc);
			}
			cd.setGrp_cd_id("1066");
			textValueList = commonService.commonCodeList(cd);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("today", today);
		model.addAttribute("textValueList", textValueList);
//		model.addAttribute("deployPerfChkIndc", deployPerfChkIndc);
		model.addAttribute("RequestCnt", checkExceptionRequestCnt);
		model.addAttribute("resultList", resultList);
		String requestURL ="execSqlPerfCheck/proPerfExcReqExceptionHandlingTab";
		
		return new ModelAndView(requestURL, "data", model);
	}
	
	/*????????? ?????? ?????? ?????? ????????? ????????? ??????*/
	@RequestMapping(value = "/DeployPerfChkDetailResult", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkDetailResult(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		
		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkDetailResult(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* ?????? ?????? ?????? */
	@RequestMapping(value="/DeployPerfChkExcptRequest/perfCheckExecute", method=RequestMethod.POST)
	@ResponseBody
	public Result perfCheckExecute(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,
			Model model) throws Exception {
		logger.debug("perf_check_step_id =============>"+deployPerfChkIndc.getPerf_check_step_id());
		logger.debug("last_perf_check_step_id =============>"+deployPerfChkIndc.getLast_perf_check_step_id());
		String last_perf_check_step_id = deployPerfChkIndc.getLast_perf_check_step_id();
		deployPerfChkIndc.setPerf_check_step_id(last_perf_check_step_id);
		Result result = new Result();
		try{
			result = execPerformanceCheckIndexService.perfCheckExecute(deployPerfChkIndc);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	
	/* ???????????? ?????? ?????? ?????? ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/exceptionHandling", method=RequestMethod.POST)
	@ResponseBody
	public Result exceptionHandling(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			
			check = execPerformanceCheckIndexService.exceptionHandling(deployPerfChkIndc);
			Iqms iqms = new Iqms();
			iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
			iqms.setCmid(deployPerfChkIndc.getDeploy_id());
			DeployPerfChk.execSqlPerfCheck(iqms, "11", deployPerfChkIndc.getPerf_check_id() , deployPerfChkIndc.getProgram_id());
			result.setResult(true);
			result.setResultCount(check);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* ???????????? ?????? ?????? ?????? ?????? ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/multiExceptionHandling", method=RequestMethod.POST)
	@ResponseBody
	public Result multiExceptionHandling(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			DeployPerfChkIndc temp = new DeployPerfChkIndc();
			String temp_program_id = StringUtils.defaultString(deployPerfChkIndc.getProgram_id());
			int array_length = temp_program_id.split(",").length;
			String exception_prc_status_cd, excepter_id, exception_prc_why, exception_request_id, perf_check_auto_pass_yn, program_id,exception_prc_meth_cd, program_nm, perf_check_id , deploy_id = "";
			List<HashMap<String, String>> listTemp = new ArrayList<HashMap<String, String>>();
			
			HashSet<String> set = new HashSet<String>();
			
			Iqms iqms = new Iqms();
			iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
			
			HashSet<String> perfCheckIdSet = new HashSet<String>();
			HashSet<String> deployIdSet = new HashSet<String>();

			for(int i = 0; i < array_length; i++){
				//??????????????? ???????????? ???????????? ??????????????? ????????? ?????????????????? ????????????
				program_nm = deployPerfChkIndc.getProgram_nm().split(",").length > 0 ? deployPerfChkIndc.getProgram_nm().split(",")[i] : "";
				
				//??????????????? ?????? ????????????
				perf_check_id =  deployPerfChkIndc.getPerf_check_id().split(",").length > 0 ? deployPerfChkIndc.getPerf_check_id().split(",")[i] : "";
				exception_prc_status_cd =  deployPerfChkIndc.getException_prc_status_cd().split(",").length > 0 ? deployPerfChkIndc.getException_prc_status_cd().split(",")[i] : "";
				excepter_id = StringUtils.defaultString(deployPerfChkIndc.getExcepter_id());
				exception_prc_why = StringUtils.defaultString(deployPerfChkIndc.getException_prc_why());
				exception_request_id = deployPerfChkIndc.getException_request_id().split(",").length > 0 ? deployPerfChkIndc.getException_request_id().split(",")[i] : "";
				perf_check_auto_pass_yn = deployPerfChkIndc.getPerf_check_auto_pass_yn().split(",").length > 0 ? deployPerfChkIndc.getPerf_check_auto_pass_yn().split(",")[i] : "";
				program_id = deployPerfChkIndc.getProgram_id().split(",").length > 0 ? deployPerfChkIndc.getProgram_id().split(",")[i] : "";
				exception_prc_meth_cd = deployPerfChkIndc.getException_prc_meth_cd().split(",").length > 0 ? deployPerfChkIndc.getException_prc_meth_cd().split(",")[i] : "";

				temp.setPerf_check_id(perf_check_id);
				temp.setException_prc_status_cd(exception_prc_status_cd);
				temp.setExcepter_id(excepter_id);
				temp.setException_prc_why(exception_prc_why);
				temp.setException_request_id(exception_request_id);
				temp.setPerf_check_auto_pass_yn(perf_check_auto_pass_yn);
				temp.setProgram_id(program_id);
				temp.setException_prc_meth_cd(exception_prc_meth_cd);
				
				set.add(perf_check_id);
				deploy_id = deployPerfChkIndc.getDeploy_id().split(",").length > 0 ? deployPerfChkIndc.getDeploy_id().split(",")[i] : "";
				deployIdSet.add(deploy_id);
				//???????????????????????? ????????? ??????
				DeployPerfChkIndc resultTemp = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(temp);
				//???????????????????????? ????????? SKIP 
				if(resultTemp != null && !StringUtils.defaultString(resultTemp.getPerf_check_id(),"").equals("") 
						&& !StringUtils.defaultString(resultTemp.getProgram_id()).equals("")){  //temp ?????? ?????????,
					
					//???????????????????????? ???????????? ???????????? ??????
					check = execPerformanceCheckIndexService.multiExceptionHandling(temp); ///??????
				}else{
					HashMap<String, String> mapTemp = new HashMap<String, String>();
					mapTemp.put("program_nm", program_nm);
					listTemp.add(mapTemp);
				}
				
				iqms.setCmid(deploy_id);
				DeployPerfChk.execSqlPerfCheck(iqms, "11", perf_check_id , program_id);
				
			}
			
			//???????????? ???????????? ???????????? ?????? ??????
			String text = "";
			if(listTemp != null && listTemp.size() > 0){
				text += "?????? ????????? ????????????, ???????????? ????????????<br/>??????????????? ?????????????????????.<br/>" ;
				text += "[ ??????????????? : ";
				for(int i = 0; i < listTemp.size(); i++){
					text += listTemp.get(i).get("program_nm");
					if(listTemp.size() == (i+1)){
						text += " ]<br/>";
					}else{
						text += ", ";
					}
				}
				text += "(??? "+array_length+"??? ??? "+listTemp.size()+"??? ??????)";
			}
			
			result.setTxtValue(text);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	/* ???????????? ?????? ?????? ?????? ????????????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/exceptionDelete", method=RequestMethod.POST)
	@ResponseBody
	public Result exceptionDelete(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.exceptionDelete(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	/* ???????????? ?????? ?????? ??????*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/rejectRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result rejectRequest(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
		Result result = new Result();
		int check = 0;
		try{
			check = execPerformanceCheckIndexService.rejectRequest(deployPerfChkIndc);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}

	/*???????????? > ???????????? ?????? ??????*/
	@RequestMapping(value = "/ProPerfExcReqDel", method = RequestMethod.GET)
	public String ProPerfExcReqDelete(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("data", deployPerfChkIndc);
		model.addAttribute("search_wrkjob_cd", deployPerfChkIndc.getSearch_wrkjob_cd());
		model.addAttribute("search_dbio", deployPerfChkIndc.getSearch_dbio());
		model.addAttribute("search_program_nm", deployPerfChkIndc.getSearch_program_nm());
		model.addAttribute("search_deploy_id", deployPerfChkIndc.getSearch_deploy_id());
		model.addAttribute("search_deploy_requester", deployPerfChkIndc.getSearch_deploy_requester());
		model.addAttribute("call_from_parent", deployPerfChkIndc.getCall_from_parent());
		model.addAttribute("call_from_child", deployPerfChkIndc.getCall_from_child());
		
		return "performanceCheckIndex/proPerfExcReqDel";
	}
	
	/*????????? ?????????????????????????????? ??????*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ProPerfExcReqDel", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProPerfExcReqDel(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		int dataCount4NextBtn = 0;

		try {
			resultList = execPerformanceCheckIndexService.getProPerfExcReqDel(deployPerfChkIndc);
			if(resultList != null && resultList.size() > deployPerfChkIndc.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(deployPerfChkIndc.getPagePerCount());
				/*???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ??????????????????*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/*????????? ?????????????????????????????? ?????? ??? ?????? ??????*/
	@RequestMapping(value = "/ProPerfExcReqDel/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ProPerfExcReqDelByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = execPerformanceCheckIndexService.getProPerfExcReqDelByExcelDown(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "??????_??????_??????_??????");
		model.addAttribute("sheetName", "??????_??????_??????_??????");
		model.addAttribute("excelId", "PRO_PERF_EXC_REQ_DEL_ESPC");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	
	//????????? ???????????? ??? ????????? ?????? - ????????????
		@RequestMapping(value = "/ExceptionDelete/CreateTab", method = RequestMethod.GET, produces = "application/text; charset=utf8")
		public ModelAndView getDeployPerfChkDetailResultDelByTab(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,
				Model model) {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
			String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String today = formatter.format(new java.util.Date());
			Cd cd = new Cd();
			List<Cd> textValueList = new ArrayList<Cd>();
			int checkExceptionRequestCnt = 0;
			
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				if(!StringUtils.defaultString(deployPerfChkIndc.getWrkjob_cd()).equals("") && !StringUtils.defaultString(deployPerfChkIndc.getProgram_id()).equals("")){
					resultList = execPerformanceCheckIndexService.getPerfCheckResultDelTableList(deployPerfChkIndc);
				}
				if(!StringUtils.defaultString(deployPerfChkIndc.getException_request_id()).equals("")){
					checkExceptionRequestCnt = execPerformanceCheckIndexService.checkExceptionRequestCnt(deployPerfChkIndc);
				}
				cd.setGrp_cd_id("1066");
				textValueList = commonService.commonCodeList(cd);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				ex.printStackTrace();
			}
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_nm", user_nm);
			model.addAttribute("user_auth_id", user_auth_id);
			model.addAttribute("today", today);
			model.addAttribute("textValueList", textValueList);
//			model.addAttribute("deployPerfChkIndc", deployPerfChkIndc);
			model.addAttribute("RequestCnt", checkExceptionRequestCnt);
			model.addAttribute("resultList", resultList);
			String restURL ="execSqlPerfCheck/proPerfExcReqDelExceptionDeleteTab";

			//?????? SQL ?????? ??????
			return new ModelAndView(restURL, "data", model);
		}
	
		/*????????? ???????????????????????? - ???????????? ?????? ?????? ????????? ??????*/
		@RequestMapping(value = "/PerfCheckResultDelTableList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckResultDelTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckResultDelTableList(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/* ?????????????????? ?????? */
		@RequestMapping(value = "/getPerfCheckStep", method = RequestMethod.GET, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc)
				throws Exception {
			List<DeployPerfChkIndc> list = new ArrayList<DeployPerfChkIndc>();

			DeployPerfChkIndc temp = new DeployPerfChkIndc();
			temp.setPerf_check_step_id("");
			temp.setPerf_check_step_nm("??????");

			list.add(temp);
			try {
				List<DeployPerfChkIndc> perfCheckStepList = execPerformanceCheckIndexService.getPerfCheckStep(deployPerfChkIndc);
				list.addAll(perfCheckStepList);
			} catch (Exception ex) {
				logger.error("Common Error ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}

			return success(list).toJSONObject().get("rows").toString();
		}
	
		
		/*???????????? > ????????? ??????????????? ??????*/
		@RequestMapping(value = "/DeployPerfChkParsingSchema", method = RequestMethod.GET)
		public String DeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
			
			return "performanceCheckIndex/deployPerfChkParsingSchema";
		}
		
		/*????????? ??????????????? ?????? ??????*/
		@RequestMapping(value = "/DeployPerfChkParsingSchema", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getDeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getDeployPerfChkParsingSchema(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*????????? ??????????????? ?????? ?????? ????????????*/
		@RequestMapping(value = "/DeployPerfChkParsingSchema/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getDeployPerfChkParsingSchemaByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
				throws Exception {

			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

			try {
				resultList = execPerformanceCheckIndexService.getDeployPerfChkParsingSchemaByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			
			String excelId = "";
			
			excelId = "DEPLOY_PERF_CHK_PARSING_SCHEMA_ESPC";
			
			model.addAttribute("fileName", "?????????_??????_?????????_??????");
			model.addAttribute("sheetName", "?????????_??????_?????????_??????");
			model.addAttribute("excelId", excelId);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		/*????????? ???????????? ?????? ?????? ?????? ????????????*/
		@RequestMapping(value = "/DeployPerfChkStepTestDB/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getDeployPerfChkStepTestDBByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
						throws Exception {
			
			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
			
			try {
				resultList = execPerformanceCheckIndexService.getDeployPerfChkStepTestDBByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			
			String fileName = "";
			String sheetName = "";
			String excelId = "";
			
			fileName = "?????????_??????_??????_??????_??????";
			sheetName = "?????????_??????_??????_??????_??????";
			excelId = "DEPLOY_PERF_CHK_STEP_TEST_DB_ESPC";
			
			model.addAttribute("fileName", fileName);
			model.addAttribute("sheetName", sheetName);
			model.addAttribute("excelId", excelId);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*????????? ?????????????????????????????? ??????*/
		@RequestMapping(value="/DeployPerfChkParsingSchema/Save", method=RequestMethod.POST)
		@ResponseBody
		public Result saveDeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
			Result result = new Result();
			int check = 0;
			try{
				check = execPerformanceCheckIndexService.saveDeployPerfChkParsingSchema(deployPerfChkIndc);
				if(check == 99){
					result.setTxtValue("Y");//???????????? ???????????? ????????? ?????????????????? ???????????? ????????????????????? ????????????. 
					result.setResult(true);
					return result;
				}
				result.setTxtValue("N");
				result.setResult(true);
				result.setResultCount(check);
			} catch (Exception ex){
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}

			return result;	
		}	
		
		/*?????? ??????????????? ????????????*/
		@RequestMapping(value="/DeployPerfChkParsingSchema/Check", method=RequestMethod.POST)
		@ResponseBody
		public Result checkDeployPerfChkParsingSchemaName(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
			Result result = new Result();
			int check = 0;
			try{
				check = execPerformanceCheckIndexService.checkDeployPerfChkParsingSchemaName(deployPerfChkIndc);
				if(check > 0){
					result.setTxtValue("Y");
				}else{
					result.setTxtValue("N");
				}
				result.setResult(true);
				result.setResultCount(check);
			} catch (Exception ex){
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}
			
			return result;	
		}

		/*???????????? > ????????????????????????????????????*/
		@RequestMapping(value = "/PerfChkIndcListState", method = RequestMethod.GET)
		public String perfChkIndcListState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
			
			return "performanceCheckIndex/perfChkIndcListState";
		}
	
		/*???????????? > ?????????????????????????????? ????????? ??????*/
		@RequestMapping(value = "/PerfChkIndcListState", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfChkIndcListState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListState(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		/*???????????? > ?????? ?????? ?????? ?????? ????????? ?????? ????????? ??????*/
		@RequestMapping(value = "/PerfChkIndcListState2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfChkIndcListState2(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListState2(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/**
		 * ???????????? ?????? ????????????
		 * @param req
		 * @param res
		 * @param module
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/PerfChkIndcListState/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getPerfChkIndcListStateByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) throws Exception {

			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListStateByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "??????_??????_??????_??????_??????");
			model.addAttribute("sheetName", "??????_??????_??????_??????_??????");
			model.addAttribute("excelId", "PERF_CHK_INDC_LIST_STATE_ESPC");	
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/**
		 * ???????????? ?????? ????????????
		 * @param req
		 * @param res
		 * @param module
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/PerfChkIndcListState2/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getPerfChkIndcListState2ByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) throws Exception {

			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListState2ByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "??????_??????_??????_??????_?????????_??????");
			model.addAttribute("sheetName", "??????_??????_??????_??????_?????????_??????");
			model.addAttribute("excelId", "PERF_CHK_INDC_LIST_STATE2");
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*???????????? > ??????????????????????????????*/
		@RequestMapping(value = "/ProPerfExcReqState", method = RequestMethod.GET)
		public String ProPerfExcReqState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
			String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
			
			String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
			String startDate = DateUtil.getPlusMons("yyyy-MM-dd","yyyy-MM-dd", nowDate,-12);	
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_nm", user_nm);
			model.addAttribute("user_auth_id", user_auth_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

			model.addAttribute("nowDate", nowDate);
			model.addAttribute("startDate", startDate);
			
			return "performanceCheckIndex/proPerfExcReqState";
		}
	
		/*???????????? > ?????????????????????????????? ?????????*/
		@RequestMapping(value = "/ProPerfExcReqState", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getProPerfExcReqState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			int dataCount4NextBtn = 0;
			logger.debug(deployPerfChkIndc.getSearch_wrkjob_cd());
			logger.debug(deployPerfChkIndc.getException_prc_meth_cd());
			logger.debug(deployPerfChkIndc.getDbio());
			logger.debug(deployPerfChkIndc.getSearch_program_nm());
			try {
				resultList = execPerformanceCheckIndexService.getProPerfExcReqState(deployPerfChkIndc);
				if(resultList != null && resultList.size() > deployPerfChkIndc.getPagePerCount()){
					dataCount4NextBtn = resultList.size();
					resultList.remove(deployPerfChkIndc.getPagePerCount());
					/*???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ??????????????????*/
				}
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
//			return success(resultList).toJSONObject().toString();	
			JSONObject jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			return jobj.toString();	
		}
		/*???????????? > ?????????????????????????????? ????????? ?????? ??????*/
		@RequestMapping(value = "/ProPerfExcReqState/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView ProPerfExcReqStateExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
				throws Exception {

			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
			try {
				resultList = execPerformanceCheckIndexService.getProPerfExcReqStateByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}

			model.addAttribute("fileName", "??????_??????_??????");
			model.addAttribute("sheetName", "??????_??????_??????");
			model.addAttribute("excelId", "PRO_PERF_EXC_REQ_STATE_ESPC");
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		
		//????????? ???????????? ??? ????????? ?????? - ????????????
		@RequestMapping(value = "/ExceptionState/CreateTab", method = RequestMethod.GET, produces = "application/text; charset=utf8")
		public ModelAndView getDeployPerfChkDetailResulStatetByTab(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,
				Model model) {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
			String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String today = formatter.format(new java.util.Date());
			
			int checkExceptionRequestCnt = 0;

			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

			try {
				if(!deployPerfChkIndc.getProgram_id().equals("") && !deployPerfChkIndc.getLast_perf_check_step_id().equals("") && !deployPerfChkIndc.getPerf_check_id().equals("")){
					resultList = execPerformanceCheckIndexService.getDeployPerfChkDetailResultByTabEspc(deployPerfChkIndc);
				}
//				if(deployPerfChkIndc.getException_request_id() != null && !deployPerfChkIndc.getException_requester_id().equals("")){
//					checkExceptionRequestCnt = execPerformanceCheckIndexService.checkExceptionRequestCnt(deployPerfChkIndc);
//				}

			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_nm", user_nm);
			model.addAttribute("user_auth_id", user_auth_id);
			model.addAttribute("today", today);
//			model.addAttribute("deployPerfChkIndc", deployPerfChkIndc);
			model.addAttribute("RequestCnt", checkExceptionRequestCnt);
			model.addAttribute("resultList", resultList);
			String restURL ="execSqlPerfCheck/proPerfExcReqExceptionStateTab";
			
			return new ModelAndView(restURL, "data", model);
		}
//		
//		
		/*????????? ?????? ?????? ?????? ?????? ????????? ????????? ??????*/
		@RequestMapping(value = "/PerfCheckResultStateTableList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckResultStateTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckResultStateTableList(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*???????????? > ?????????????????? */
		@RequestMapping(value = "/PerfCheckState", method = RequestMethod.GET)
		public String PerfCheckState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
			String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
			
			String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
			String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);	
			model.addAttribute("user_id", user_id);
			model.addAttribute("user_nm", user_nm);
			model.addAttribute("user_auth_id", user_auth_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

			model.addAttribute("nowDate", nowDate);
			model.addAttribute("startDate", startDate);
			
			return "performanceCheckIndex/perfCheckState";
		}
		
		/*???????????? > ??????????????????>CM???????????? ????????? ??????*/
		@RequestMapping(value = "/PerfCheckState0", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckState0(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState0(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*???????????? > ??????????????????>???????????????????????? ????????? ??????*/
		@RequestMapping(value = "/PerfCheckState1", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckState1(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState1(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*???????????? > ?????????????????? ????????? ??????2*/
		@RequestMapping(value = "/PerfCheckState2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckState2(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState2(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*???????????? > ?????? ?????? ??????>CM???????????? ?????? ??????*/
		@RequestMapping(value = "/PerfCheckState0/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getPerfCheckState0ByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
						throws Exception {
			
			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState0ByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "CM_??????_??????");
			model.addAttribute("sheetName", "CM_??????_??????");
			model.addAttribute("excelId", "PERF_CHECK_STATE0_ESPC");	
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*???????????? > ?????? ?????? ??????>???????????????????????? ?????? ??????*/
		@RequestMapping(value = "/PerfCheckState1/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getPerfCheckState1ByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
				throws Exception {

			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState1ByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "????????????_??????_??????");
			model.addAttribute("sheetName", "????????????_??????_??????");
			model.addAttribute("excelId", "PERF_CHECK_STATE1_ESPC");
			
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*???????????? > ?????? ?????? ??????>???????????????????????? ?????? ??????*/
		@RequestMapping(value = "/PerfCheckState2/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
		public ModelAndView getPerfCheckState2ByExcelDown(HttpServletRequest req, HttpServletResponse res,
				@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model)
						throws Exception {
			
			List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
			try {
				resultList = execPerformanceCheckIndexService.getPerfCheckState2ByExcelDown(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "?????????_?????????_??????");
			model.addAttribute("sheetName", "?????????_?????????_??????");
			model.addAttribute("excelId", "PERF_CHECK_STATE2");
			
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		@RequestMapping(value="/ProPerfExcReq/getExecPlan", method=RequestMethod.POST)
		@ResponseBody
		public Map<String,Object> getExecPlan(@RequestBody String jData, Model model) throws Exception {
			Result result = new Result();
			Map<String,Object> returnMap = new HashMap<String,Object>();
			
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParse.parse(jData);
			
			PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
			
			performanceCheckMng.setProgram_id((String)jsonObj.get("program_id"));
			performanceCheckMng.setPerf_check_step_id((String)jsonObj.get("perf_check_step_id"));
			performanceCheckMng.setPerf_check_id((String)jsonObj.get("perf_check_id"));
			performanceCheckMng.setProgram_execute_tms((String)jsonObj.get("program_execute_tms"));
			performanceCheckMng.setSql_command_type_cd((String)jsonObj.get("sql_command_type_cd"));
			
			try {
				if("SELECT".equals(performanceCheckMng.getSql_command_type_cd())) {
					
					PerformanceCheckMng executionPlan = execPerfChkMngSvc
							.selectPerfCheckResultBasisWhy(performanceCheckMng);
					returnMap.put("executionPlan", executionPlan);
					
				} else{
					List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
					resultList = execPerfChkMngSvc.getDeployPerfSqlPlan(performanceCheckMng);
					
					List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
					JSONArray jsonArray = JSONArray.fromObject(buildList);

					String returnValue = "";
					returnValue = jsonArray.toString();
					
					result.setTxtValue(returnValue);
				}
				
				result.setResult(true);
				
			} catch(Exception e) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + e.getMessage());
				result.setResult(false);
			}
			returnMap.put("result", result);
			return returnMap;
		}
//		
}