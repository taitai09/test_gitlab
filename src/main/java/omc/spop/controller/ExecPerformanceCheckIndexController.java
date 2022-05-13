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

	/*성능점검 > 성능점검 지표관리*/
	@RequestMapping(value = "/DeployPrefChkIndc", method = RequestMethod.GET)
	public String deployPrefChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());
		model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/deployPerfChkIndc";
	}
	/* 공통코드 조회 */
	@RequestMapping(value = "/getCommonCode", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCode(@ModelAttribute("cd") Cd cd) throws Exception {
		String isAll = StringUtils.defaultString(cd.getIsAll());
		String isChoice = StringUtils.defaultString(cd.getIsChoice());
		List<Cd> commonList = new ArrayList<Cd>();
		
		Cd temp = new Cd();
		if(isAll.equals("Y")){
			temp.setCd("");
			temp.setCd_nm("전체");
		}else if(isChoice.equals("Y")){
			temp.setCd("");
			temp.setCd_nm("선택");
		}else if(isAll.equals("N") && isChoice.equals("N")){
		}else{
			temp.setCd("");
			temp.setCd_nm("선택");
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
	/*검색시 배포성능점검지표기본 조회*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*셀렉트시 범위여부 조회*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckMethCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getRangeWhether(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckMethCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*검색시 배포성능점검지표기본 저장*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/**
	 * 자동선정 엑셀 다운로드
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
		
		fileName = "성능_검증_지표_관리";
		sheetName = "성능_검증_지표_관리";
		excelId = "DEPLOY_PERF_CHK_INDC_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * 자동선정 엑셀 다운로드
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
		
		fileName = "업무별_성능_검증_지표_관리";
		sheetName = "업무별_성능_검증_지표_관리";
		excelId = "WJ_PERF_CHK_INDC_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	

	/*성능점검 > 성능점검 지표관리*/
	@RequestMapping(value = "/WjPerfChkIndc", method = RequestMethod.GET)
	public String WjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/wjPerfChkIndc";
	}

	/*검색시 배포성능점검지표기본 조회*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*셀렉트시 프로그램 조회*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckProgramDivCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckProgramDivCd(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckProgramDivCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}

	/*셀렉트시 여부값 판정 구분 조회*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getYnDecideDivCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getYnDecideDivCd(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getYnDecideDivCd(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*셀렉트시 여부값 판정 구분 조회*/        
	@RequestMapping(value = "/DeployPrefChkIndc/getPerfCheckIndcId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckIndcId(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getPerfCheckIndcId(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	
	/*성능점검 기준값 관리 중복 여부 */
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/*검색시 배포성능점검지표기본 저장*/
	@Transactional
	@RequestMapping(value="/WjPerfChkIndc/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		Result result = new Result();
		try{
			int check = execPerformanceCheckIndexService.saveWjPerfChkIndc(deployPerfChkIndc);
			if(check == -1){
				result.setMessage("[ 서버에러 ] 관리자에게 문의해주세요");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	
	/*검색시 배포성능점검지표기본 삭제*/
	@Transactional
	@RequestMapping(value="/WjPerfChkIndc/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteWjPerfChkIndc(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) {
		Result result = new Result();
		try{
			int check = execPerformanceCheckIndexService.deleteWjPerfChkIndc(deployPerfChkIndc);
			if(check == -1){
				result.setMessage("[ 서버에러 ] 관리자에게 문의해주세요");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/*성능점검 > 성능점검 예외 요청*/
	@RequestMapping(value = "/ProPerfExcReq", method = RequestMethod.GET)
	public String ProPerfExcReq(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
//		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -1);    // 한달 전
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

		logger.debug("call_from_parent:::::값:::######::"+deployPerfChkIndc.getCall_from_parent());
		logger.debug("call_from_child:::::값:::######::"+deployPerfChkIndc.getCall_from_child());
		
		model.addAttribute("search_perf_check_id", deployPerfChkIndc.getSearch_perf_check_id());
		model.addAttribute("search_perf_check_step_id", deployPerfChkIndc.getSearch_perf_check_step_id());
		model.addAttribute("search_program_id", deployPerfChkIndc.getSearch_program_id());
		model.addAttribute("search_program_execute_tms", deployPerfChkIndc.getSearch_program_execute_tms());
		
		return "performanceCheckIndex/proPerfExcReq";
	}
	
	/*검색시 배포성능점검지표기본 조회*/
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
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}

	/* 배치일경우- Explain Plan Tree */
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
	
	/*검색시 배포성능점검지표기본 조회 후 엑셀 다운*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		};
		model.addAttribute("fileName", "성능_검증_예외_요청");
		model.addAttribute("sheetName", "성능_검증_예외_요청");
		model.addAttribute("excelId", "PRO_PERF_EXC_REQ_ESPC");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/*클릭시 바인드값 테이블 리스트 조회*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*클릭시 성능 점검 결과 테이블 리스트 조회*/
	@RequestMapping(value = "/PerfCheckResultTableList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultTableList(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		List<PerformanceCheckMng> resultListPerfChkMng = new ArrayList<PerformanceCheckMng>();
		try {
			//실행 SQL 성능 검증
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
	}
	
	/*성능점검 > 성능점검 단계관리*/
	@RequestMapping(value = "/DeployPerfChkStep", method = RequestMethod.GET)
	public String deployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());

		return "performanceCheckIndex/deployPerfChkStep";
	}
	
	/*성능점검 단계관리 조회*/
	@RequestMapping(value = "/DeployPerfChkStep", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();

		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkStep(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*성능점검단계 저장*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/**
	 * 자동선정 엑셀 다운로드
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		String fileName = "";
		String sheetName = "";
		String excelId = "";
	
		fileName = "성능_검증_단계_관리";
		sheetName = "성능_검증_단계_관리";
		excelId = "DEPLOY_PERF_CHK_STEP_ESPC";
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("sheetName", sheetName);
		model.addAttribute("excelId", excelId);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/*성능점검 > 성능점검 단계관리*/
	@RequestMapping(value = "/DeployPerfChkStepTestDB", method = RequestMethod.GET)
	public String deployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
		
		return "performanceCheckIndex/deployPerfChkStepTestDB";
	}
	
	/*성능점검 단계관리 조회*/
	@RequestMapping(value = "/DeployPerfChkStepTestDB", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStepTestDB(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
			Model model) {
		List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
		
		try {
			resultList = execPerformanceCheckIndexService.getDeployPerfChkStepTestDB(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/*셀렉트시  조회*/        
	@RequestMapping(value = "/DeployPerfChkStep/DeployPerfChkStepId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkStepId(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc) {
		
		List<DeployPerfChkIndc> tableNameList = new ArrayList<DeployPerfChkIndc>();
		try {
			tableNameList = execPerformanceCheckIndexService.getDeployPerfChkStepId(deployPerfChkIndc);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	/*업무별성능점검단계 저장*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	/* 프로그램 성능 점검 예외 요청 - ExceptionPrcStatusCd 확인*/
	@RequestMapping(value="/DeployPerfChkExcptRequest/CheckExceptionPrcStatusCd2", method=RequestMethod.POST)
	@ResponseBody
	public Result CheckExceptionPrcStatusCd2(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model){
		Result result = new Result();
		try{
			deployPerfChkIndc.setException_prc_status_cd("1");
			DeployPerfChkIndc deployPerfChkIndc1 = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(deployPerfChkIndc);
			
			if(deployPerfChkIndc1 != null ){  //deployPerfChkIndc1 값이 있다면,
				if(deployPerfChkIndc1.getPerf_check_id() != null && !deployPerfChkIndc1.getPerf_check_id().equals("") && !deployPerfChkIndc1.getPerf_check_id().equals("0")){
					String addText ="";
					if(deployPerfChkIndc.getDeploy_id() != null && !deployPerfChkIndc.getDeploy_id().equals("")){
						addText = "배포명 : "+deployPerfChkIndc.getDeploy_id()+",<br/>";
					}
					result.setTxtValue("현재 예외 요청 중인 건이 존재합니다. <br/>요청 취소 후 재요청 바랍니다.<br/>[ "+addText+"프로그램명 : "+deployPerfChkIndc.getProgram_nm()+",<br/>SQL식별자 : "+deployPerfChkIndc.getDbio() +" ]");
				
				}else{ //deployPerfChkIndc1 값이 없다면
					result.setTxtValue("");; //진행
				}
			}else{
				result.setTxtValue("");; //진행
			}
			result.setResult(true);
		} catch (Exception ex){
			ex.printStackTrace();
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	/* 프로그램 성능 점검 삭제 요청 - ExceptionPrcStatusCd 확인*/
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
						addText = "배포명 : "+deployPerfChkIndc.getDeploy_id()+",<br/>";
					}
					result.setTxtValue("현재 예외 요청 중인 건이 존재합니다. <br/>요청 취소 후 재요청 바랍니다.<br/>[ "+addText+"프로그램명 : "+deployPerfChkIndc.getProgram_nm()+",<br/>SQL식별자 : "+deployPerfChkIndc.getDbio() +" ]");
				}else{
					result.setTxtValue("");; //진행
				}
			}else{
				result.setTxtValue("");; //진행
			}
			result.setResult(true);
		} catch (Exception ex){
			ex.printStackTrace();
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	/* 프로그램 성능 점검 예외 요청*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 프로그램 성능 점검 일괄 예외 요청*/
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
				//여기가 0 이여서 아마 안될듯... 내일 고치기!!!
				program_nm = deployPerfChkIndc.getProgram_nm().split(",").length > 0 ? deployPerfChkIndc.getProgram_nm().split(",")[i] : "";
				dbio = deployPerfChkIndc.getDbio().split(",").length > 0 ? deployPerfChkIndc.getDbio().split(",")[i] : "";
				deploy_id = deployPerfChkIndc.getDeploy_id().split(",").length > 0 ? deployPerfChkIndc.getDeploy_id().split(",")[i] : "";
				perf_check_id = deployPerfChkIndc.getPerf_check_id().split(",").length > 0 ? deployPerfChkIndc.getPerf_check_id().split(",")[i] : "";
				program_id =  deployPerfChkIndc.getProgram_id().split(",").length > 0 ? deployPerfChkIndc.getProgram_id().split(",")[i] : "";
				exception_requester_id = deployPerfChkIndc.getException_requester_id().split(",").length > 0 ? deployPerfChkIndc.getException_requester_id().split(",")[i] : "";
//				exception_request_why_cd = deployPerfChkIndc.getException_request_why_cd().split(",").length > 0 ? deployPerfChkIndc.getException_request_why_cd().split(",")[i] : "";  //input값으로 받음 
//				exception_request_detail_why = deployPerfChkIndc.getException_request_detail_why().split(",").length > 0 ? deployPerfChkIndc.getException_request_detail_why().split(",")[i] : ""; //input값으로 받음
//				exception_prc_meth_cd = deployPerfChkIndc.getException_prc_meth_cd().split(",").length > 0 ? deployPerfChkIndc.getException_prc_meth_cd().split(",")[i] : "";  //input값으로 받음
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
				//배포요청중인건이 있는지 체크
				DeployPerfChkIndc resultTemp = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(temp);
				//배포요청중인건이 있다면 SKIP 
				if(resultTemp != null && !StringUtils.defaultString(resultTemp.getPerf_check_id(),"").equals("") 
						&& !StringUtils.defaultString(resultTemp.getProgram_id()).equals("")){  //temp 값이 있다면,
		
				//배포요청중인건에대해서 저장해두었다가 사용자에게 보여주기위한 용도
					HashMap<String, String> mapTemp = new HashMap<String, String>();
					mapTemp.put("deploy_id", deploy_id);
					mapTemp.put("dbio", dbio);
					mapTemp.put("program_nm", program_nm);
					listTemp.add(mapTemp);
				//배포요청중인건이 없는건에 대해서만 INSERT
				}else{
					check += execPerformanceCheckIndexService.multiRequestDeployPerfChkExcptRequest(temp);
				}
			}
			
			//요청중이 있었을때 알려주기 위한 용도
			String text = "";
			if(listTemp != null && listTemp.size() > 0){
				text += "요청중인건을 제외하고 요청을 완료하였습니다.<br/>" ;
				text += "[ 프로그램명 : ";
				for(int i = 0; i < listTemp.size(); i++){
					text += listTemp.get(i).get("program_nm");
					if(listTemp.size() == (i+1)){
						text += " ]<br/>";
					}else{
						text += ", ";
					}
				}
				text += "(총 "+array_length+"건 중 "+listTemp.size()+"건 실패)";
			}
			result.setTxtValue(text);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			logger.error(ex.toString());
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* 프로그램 성능 점검 예외 요청취소*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	/* 프로그램 성능 점검 예외 요청취소*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	
	//클릭시 예외처리 탭 테이블 생성 - 예외처리
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
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
	
	/*클릭시 성능 점검 결과 테이블 리스트 조회*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능 점검 수행 */
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	
	/* 프로그램 성능 점검 예외 요청*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 프로그램 성능 점검 일괄 예외 요청*/
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
				//사용자에게 반려건을 제외하고 요청했다는 증거를 보여주기위한 파라미터
				program_nm = deployPerfChkIndc.getProgram_nm().split(",").length > 0 ? deployPerfChkIndc.getProgram_nm().split(",")[i] : "";
				
				//예외처리를 위한 파라미터
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
				//배포요청중인건이 있는지 체크
				DeployPerfChkIndc resultTemp = execPerformanceCheckIndexService.checkExceptionPrcStatusCd2(temp);
				//배포요청중인건이 있다면 SKIP 
				if(resultTemp != null && !StringUtils.defaultString(resultTemp.getPerf_check_id(),"").equals("") 
						&& !StringUtils.defaultString(resultTemp.getProgram_id()).equals("")){  //temp 값이 있다면,
					
					//배포요청중인건에 대해서만 예외처리 실행
					check = execPerformanceCheckIndexService.multiExceptionHandling(temp); ///임시
				}else{
					HashMap<String, String> mapTemp = new HashMap<String, String>();
					mapTemp.put("program_nm", program_nm);
					listTemp.add(mapTemp);
				}
				
				iqms.setCmid(deploy_id);
				DeployPerfChk.execSqlPerfCheck(iqms, "11", perf_check_id , program_id);
				
			}
			
			//요청중이 있었을때 알려주기 위한 용도
			String text = "";
			if(listTemp != null && listTemp.size() > 0){
				text += "이미 완료된 예외처리, 반려건을 제외하고<br/>예외처리를 완료하였습니다.<br/>" ;
				text += "[ 프로그램명 : ";
				for(int i = 0; i < listTemp.size(); i++){
					text += listTemp.get(i).get("program_nm");
					if(listTemp.size() == (i+1)){
						text += " ]<br/>";
					}else{
						text += ", ";
					}
				}
				text += "(총 "+array_length+"건 중 "+listTemp.size()+"건 실패)";
			}
			
			result.setTxtValue(text);
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	/* 프로그램 성능 점검 예외 삭제요청*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	/* 프로그램 성능 점검 반려*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}

	/*성능점검 > 성능점검 예외 삭제*/
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
	
	/*검색시 배포성능점검지표기본 조회*/
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
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/*검색시 배포성능점검지표기본 조회 후 엑셀 다운*/
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
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_검증_예외_삭제");
		model.addAttribute("sheetName", "성능_검증_예외_삭제");
		model.addAttribute("excelId", "PRO_PERF_EXC_REQ_DEL_ESPC");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	
	//클릭시 예외처리 탭 테이블 생성 - 예외삭제
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
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

			//실행 SQL 성능 검증
			return new ModelAndView(restURL, "data", model);
		}
	
		/*클릭시 성능점검예외삭제 - 프로그램 점검 지표 테이블 조회*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/* 최종점검단계 조회 */
		@RequestMapping(value = "/getPerfCheckStep", method = RequestMethod.GET, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfCheckStep(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc)
				throws Exception {
			List<DeployPerfChkIndc> list = new ArrayList<DeployPerfChkIndc>();

			DeployPerfChkIndc temp = new DeployPerfChkIndc();
			temp.setPerf_check_step_id("");
			temp.setPerf_check_step_nm("전체");

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
	
		
		/*성능점검 > 업무별 파싱스키마 관리*/
		@RequestMapping(value = "/DeployPerfChkParsingSchema", method = RequestMethod.GET)
		public String DeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
			
			return "performanceCheckIndex/deployPerfChkParsingSchema";
		}
		
		/*업무별 파싱스키마 관리 조회*/
		@RequestMapping(value = "/DeployPerfChkParsingSchema", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getDeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getDeployPerfChkParsingSchema(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*업무별 파싱스키마 관리 엑셀 다운로드*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			
			String excelId = "";
			
			excelId = "DEPLOY_PERF_CHK_PARSING_SCHEMA_ESPC";
			
			model.addAttribute("fileName", "업무별_파싱_스키마_관리");
			model.addAttribute("sheetName", "업무별_파싱_스키마_관리");
			model.addAttribute("excelId", excelId);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		/*업무별 성능점검 단계 관리 엑셀 다운로드*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			
			String fileName = "";
			String sheetName = "";
			String excelId = "";
			
			fileName = "업무별_성능_검증_단계_관리";
			sheetName = "업무별_성능_검증_단계_관리";
			excelId = "DEPLOY_PERF_CHK_STEP_TEST_DB_ESPC";
			
			model.addAttribute("fileName", fileName);
			model.addAttribute("sheetName", sheetName);
			model.addAttribute("excelId", excelId);
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*검색시 배포성능점검지표기본 저장*/
		@RequestMapping(value="/DeployPerfChkParsingSchema/Save", method=RequestMethod.POST)
		@ResponseBody
		public Result saveDeployPerfChkParsingSchema(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc,  Model model) throws Exception{
			Result result = new Result();
			int check = 0;
			try{
				check = execPerformanceCheckIndexService.saveDeployPerfChkParsingSchema(deployPerfChkIndc);
				if(check == 99){
					result.setTxtValue("Y");//배포성능 점검중인 건수가 존재하는경우 스키마를 변경할지여부를 물어야함. 
					result.setResult(true);
					return result;
				}
				result.setTxtValue("N");
				result.setResult(true);
				result.setResultCount(check);
			} catch (Exception ex){
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}

			return result;	
		}	
		
		/*체크 파싱스키마 사용여부*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}
			
			return result;	
		}

		/*성능점검 > 성능점검예외처리현황조회*/
		@RequestMapping(value = "/PerfChkIndcListState", method = RequestMethod.GET)
		public String perfChkIndcListState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, Model model) {
			
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			model.addAttribute("user_id", user_id);
			model.addAttribute("menu_id", deployPerfChkIndc.getMenu_id());model.addAttribute("menu_nm", deployPerfChkIndc.getMenu_nm());
			
			return "performanceCheckIndex/perfChkIndcListState";
		}
	
		/*성능점검 > 성능점검예외처리현황 리스트 조회*/
		@RequestMapping(value = "/PerfChkIndcListState", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfChkIndcListState(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListState(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		/*성능점검 > 성능 점검 예외 처리 사유별 현황 리스트 조회*/
		@RequestMapping(value = "/PerfChkIndcListState2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
		@ResponseBody
		public String getPerfChkIndcListState2(@ModelAttribute("deployPerfChkIndc") DeployPerfChkIndc deployPerfChkIndc, HttpServletRequest res,
				Model model) {
			List<DeployPerfChkIndc> resultList = new ArrayList<DeployPerfChkIndc>();
			
			try {
				resultList = execPerformanceCheckIndexService.getPerfChkIndcListState2(deployPerfChkIndc);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/**
		 * 자동선정 엑셀 다운로드
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "성능_검증_예외_처리_현황");
			model.addAttribute("sheetName", "성능_검증_예외_처리_현황");
			model.addAttribute("excelId", "PERF_CHK_INDC_LIST_STATE_ESPC");	
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/**
		 * 자동선정 엑셀 다운로드
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "성능_검증_예외_처리_사유별_현황");
			model.addAttribute("sheetName", "성능_검증_예외_처리_사유별_현황");
			model.addAttribute("excelId", "PERF_CHK_INDC_LIST_STATE2");
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*성능점검 > 성능점검예외처리조회*/
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
	
		/*성능점검 > 성능점검예외처리조회 리스트*/
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
					/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
				}
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingClass().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
//			return success(resultList).toJSONObject().toString();	
			JSONObject jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			return jobj.toString();	
		}
		/*성능점검 > 성능점검예외처리조회 리스트 엑셀 다운*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}

			model.addAttribute("fileName", "예외_처리_이력");
			model.addAttribute("sheetName", "예외_처리_이력");
			model.addAttribute("excelId", "PRO_PERF_EXC_REQ_STATE_ESPC");
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		
		//클릭시 예외처리 탭 테이블 생성 - 예외처리
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
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
		/*클릭시 성능 점검 예외 처리 테이블 리스트 조회*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*성능점검 > 성능점검현황 */
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
		
		/*성능점검 > 성능점검현황>CM점검현황 리스트 조회*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*성능점검 > 성능점검현황>프로그램점검현황 리스트 조회*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*성능점검 > 성능점검현황 리스트 조회2*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
			
			return success(resultList).toJSONObject().toString();
		}
		
		/*성능점검 > 성능 점검 현황>CM점검현황 엑셀 다운*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "CM_검증_현황");
			model.addAttribute("sheetName", "CM_검증_현황");
			model.addAttribute("excelId", "PERF_CHECK_STATE0_ESPC");	
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*성능점검 > 성능 점검 현황>프로그램점검현황 엑셀 다운*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "프로그램_검증_현황");
			model.addAttribute("sheetName", "프로그램_검증_현황");
			model.addAttribute("excelId", "PERF_CHECK_STATE1_ESPC");
			
			// return a view which will be resolved by an excel view resolver
			return new ModelAndView("xlsxView", "resultList", resultList);
		}
		
		/*성능점검 > 성능 점검 현황>지표별부적합현황 엑셀 다운*/
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
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			model.addAttribute("fileName", "지표별_부적합_현황");
			model.addAttribute("sheetName", "지표별_부적합_현황");
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
				logger.error(methodName + " 예외발생 ==> " + e.getMessage());
				result.setResult(false);
			}
			returnMap.put("result", result);
			return returnMap;
		}
//		
}