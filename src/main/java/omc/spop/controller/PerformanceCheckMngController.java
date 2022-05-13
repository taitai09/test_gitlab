package omc.spop.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ApmApplSql;
import omc.spop.model.IqmsURIConstants;
import omc.spop.model.Login;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.ReqIqms;
import omc.spop.model.Result;
import omc.spop.model.RtnMsg;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.Users;
import omc.spop.service.PerformanceCheckMngService;
import omc.spop.service.SelfTuningService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2019.01.04 성능 점검 관리
 **********************************************************/

@Controller
public class PerformanceCheckMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckMngController.class);

	@Autowired
	private PerformanceCheckMngService perfChkMngSvc;

	@Value("#{defaultConfig['iqmsip']}")
	private String iqmsip;

	@Value("#{defaultConfig['iqmsport']}")
	private String iqmsport;

	@Value("#{defaultConfig['iqms_server_uri']}")
	private String IQMS_SERVER_URI;

	@RequestMapping(value = "/PerformanceCheckMng")
	public String performanceCheckMng(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -1);    // 한달 전
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//		String startDate = dateFormatter.format(cal.getTime());
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -2);
		
		String deploy_request_dt_1 = StringUtils.defaultString(performanceCheckMng.getDeploy_request_dt_1());
		String deploy_request_dt_2 = StringUtils.defaultString(performanceCheckMng.getDeploy_request_dt_2());
		if (deploy_request_dt_1.equals("")) {
			performanceCheckMng.setDeploy_request_dt_1(startDate);
		}
		if (deploy_request_dt_2.equals("")) {
			performanceCheckMng.setDeploy_request_dt_2(nowDate);
		}
		model.addAttribute("menu_id", performanceCheckMng.getMenu_id());
		model.addAttribute("menu_nm", performanceCheckMng.getMenu_nm());
		model.addAttribute("user_id", user_id);
		model.addAttribute("call_from_parent", performanceCheckMng.getCall_from_parent());

		return "performanceCheckMng/performanceCheckMng";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/PerformanceCheckMngList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerformanceBasicCheckList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {

		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		performanceCheckMng.setAuth_cd(auth_cd);
		performanceCheckMng.setUser_id(user_id);

		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		int dataCount4NextBtn = 0;
		try {
			resultList = perfChkMngSvc.getPerformanceCheckMngList(performanceCheckMng);
			if (resultList != null && resultList.size() > performanceCheckMng.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(performanceCheckMng.getPagePerCount());
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

	/* 최종점검단계 조회 */
	@RequestMapping(value = "/getPerfCheckStep", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckStep(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng)
			throws Exception {
		List<PerformanceCheckMng> list = new ArrayList<PerformanceCheckMng>();

		PerformanceCheckMng temp = new PerformanceCheckMng();
		temp.setPerf_check_step_id("");
		temp.setPerf_check_step_nm("전체");

		list.add(temp);
		try {
			List<PerformanceCheckMng> perfCheckStepList = perfChkMngSvc.getPerfCheckStep(performanceCheckMng);
			list.addAll(perfCheckStepList);
		} catch (Exception ex) {
			logger.error("Common Error ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(list).toJSONObject().get("rows").toString();
	}

	@RequestMapping(value = "/PerformanceCheckMngList/ExcelDown1", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PerformanceCheckMngListExcelDown1(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = perfChkMngSvc.getPerformanceCheckMngList4Excel(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_점검_관리");
		model.addAttribute("sheetName", "성능_점검_관리");
		model.addAttribute("excelId", "PERF_CHECK_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	@RequestMapping(value = "/PerformanceCheckMngList/ExcelDown2", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PerformanceCheckMngListExcelDown2(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = perfChkMngSvc.getPerformanceCheckStageList4Excel(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_점검_단계");
		model.addAttribute("sheetName", "성능_점검_단계");
		model.addAttribute("excelId", "PERF_CHECK_STEP");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	@RequestMapping(value = "/PerformanceCheckStageList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerformanceCheckStageList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			resultList = perfChkMngSvc.getPerformanceCheckStageList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		return jobj.toString();
	}

	@RequestMapping(value = "/getPerfCheckResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultCount(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		PerformanceCheckMng resultList = new PerformanceCheckMng();
		try {
			int programCnt = perfChkMngSvc.getProgramCnt(performanceCheckMng);
			if (programCnt > 0) {
				resultList = perfChkMngSvc.getPerfCheckResultCount(performanceCheckMng);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		return jobj.toString();
	}

	/* 성능점검완료,성능 점검 완료 */
	@RequestMapping(value = "/PerformanceCheckComplete", method = RequestMethod.POST)
	@ResponseBody
	public Result performanceCheckComplete(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		Result result = new Result();
		try {
			result = perfChkMngSvc.performanceCheckComplete(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 강제점검완료,강제 점검 완료 */
	@RequestMapping(value = "/PerfChkForceFinish", method = RequestMethod.POST)
	@ResponseBody
	public Result PerfChkForceFinish(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		Result result = new Result();
		try {
			result = perfChkMngSvc.perfChkForceFinish(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/auth/PerfChkAutoFinish", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json; charset=utf8")
	@ResponseBody
	public Result PerfChkAutoFinish(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		Result result = new Result();
		try {
			result = perfChkMngSvc.perfChkAutoFinish(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	public RtnMsg sendResult(PerformanceCheckMng performanceCheckMng, boolean result) {
		String resultyn = "N";
		if (result) {
			resultyn = "Y";
		}
		ReqIqms rIqms = new ReqIqms();
		// var url =
		// "http://115.68.102.154:8082/wbn/Wbn9001I.jsp?systemgb=1001&cmid=63&status=Y";
		RestTemplate restTemplate = new RestTemplate();
		rIqms.setP_cmid(performanceCheckMng.getDeploy_id());
		rIqms.setP_systemgb("WBN0041007");
		rIqms.setP_servicegb("1001");
		rIqms.setP_resultyn(resultyn);
		String SERVER_URI = "http://" + iqmsip + ":" + iqmsport;
		logger.debug("SERVER_URI + IqmsURIConstants.Iqms:" + SERVER_URI + IqmsURIConstants.Iqms);
		RtnMsg response = restTemplate.postForObject(SERVER_URI + IqmsURIConstants.Iqms, rIqms, RtnMsg.class);
		logger.debug("SERVER_URI + IqmsURIConstants.Iqms:" + SERVER_URI + IqmsURIConstants.Iqms);
		return response;
	}

	/* 성능점검완료 취소 */
	@RequestMapping(value = "/PerformanceCheckCompleteCancel", method = RequestMethod.POST)
	@ResponseBody
	public Result PerformanceCheckCompleteCancel(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		Result result = new Result();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		performanceCheckMng.setUser_id(user_id);
		try {
			int updateResult = perfChkMngSvc.updatePerfTestCompleteYnToN(performanceCheckMng);
			logger.debug("updateResult1 :" + updateResult);

			result.setMessage("성능점검완료 취소 처리에 성공하였습니다.");
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

	@RequestMapping(value = "/PerformanceCheckStageList/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PerformanceCheckStageListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {
		logger.debug("wrkjob_cd :" + performanceCheckMng.getWrkjob_cd());
		logger.debug("perf_check_id :" + performanceCheckMng.getPerf_check_id());
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = perfChkMngSvc.getPerformanceCheckStageList4Excel(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_점검_단계");
		model.addAttribute("sheetName", "성능_점검_단계");
		model.addAttribute("excelId", "PERF_CHECK_STEP");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	@RequestMapping(value = "/PerformanceCheckResult")
	public String performanceCheckResult(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) throws Exception {

		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -6);
		String program_id = StringUtils.defaultString(performanceCheckMng.getProgram_id());
		String refresh = StringUtils.defaultString(performanceCheckMng.getRefresh());
		String deployId = StringUtils.defaultString(performanceCheckMng.getDeploy_id());
		String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());
		logger.debug("deployId :["+deployId+"]");
		logger.debug("perf_check_id :["+perf_check_id+"]");

		model.addAttribute("auth_cd", auth_cd);
		model.addAttribute("user_id", user_id);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("call_from_parent", performanceCheckMng.getCall_from_parent());
		model.addAttribute("program_id", program_id);
		model.addAttribute("refresh", refresh);
		model.addAttribute("menu_id", performanceCheckMng.getMenu_id());
		model.addAttribute("menu_nm", performanceCheckMng.getMenu_nm());
		PerformanceCheckMng perfCheckIdEtc = null;
		PerformanceCheckMng perfCheckResultBasicInfo = null;
		try {
			if (!deployId.equals("")) {
				model.addAttribute("deploy_id", deployId);
			}
			if (!deployId.equals("") && perf_check_id.equals("")) {
				perfCheckIdEtc = perfChkMngSvc.getPerfCheckIdFromDeployId(performanceCheckMng);
				if (perfCheckIdEtc != null) {
					perf_check_id = StringUtils.defaultString(perfCheckIdEtc.getPerf_check_id());
					String perf_check_step_id = StringUtils.defaultString(perfCheckIdEtc.getPerf_check_step_id());
					String wrkjob_cd = StringUtils.defaultString(perfCheckIdEtc.getWrkjob_cd());
					String top_wrkjob_cd = StringUtils.defaultString(perfCheckIdEtc.getTop_wrkjob_cd());
					performanceCheckMng.setPerf_check_id(perf_check_id);
					performanceCheckMng.setPerf_check_step_id(perf_check_step_id);
					performanceCheckMng.setWrkjob_cd(wrkjob_cd);
					performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);

					model.addAttribute("perf_check_id", perf_check_id);
					model.addAttribute("perf_check_step_id", perf_check_step_id);
					model.addAttribute("wrkjob_cd", wrkjob_cd);
					model.addAttribute("top_wrkjob_cd", top_wrkjob_cd);

					perfCheckResultBasicInfo = perfChkMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
				} else {
					performanceCheckMng.setError_message("배포ID가 존재하지 않습니다.</br>관리자에게 문의 바랍니다.");
					
					model.addAttribute("error_message", performanceCheckMng.getError_message());
					
					perfCheckResultBasicInfo = perfChkMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
					
					return "performanceCheckMng/performanceCheckResult";
				}
			} else {
				perfCheckResultBasicInfo = perfChkMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
			}
			model.addAttribute("perfCheckResultBasicInfo", perfCheckResultBasicInfo);

			PerformanceCheckMng deployCheckStatus = perfChkMngSvc.selectDeployCheckStatus(performanceCheckMng);
			model.addAttribute("deployCheckStatus", deployCheckStatus);

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "performanceCheckMng/performanceCheckResult";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getPerfCheckResultBasicInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultBasicInfo(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		PerformanceCheckMng resultList = null;
		try {
			resultList = perfChkMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/PerfCheckResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultList(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		int dataCount4NextBtn = 0;
		try {

			resultList = perfChkMngSvc.getPerfCheckResultList(performanceCheckMng);
			if (resultList != null && resultList.size() > performanceCheckMng.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(performanceCheckMng.getPagePerCount());
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

	@RequestMapping(value = "/PerfCheckResultList/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PerfCheckResultListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = perfChkMngSvc.getPerfCheckResultList4Excel(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_점검_결과");
		model.addAttribute("sheetName", "성능_점검_결과");
		model.addAttribute("excelId", "PERF_CHECK_RESULT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	@RequestMapping("/PerfCheckResultList/createPerfChkResultDataGrid.json")
	public ModelAndView createPerfChkResultDataGrid(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {

		logger.debug("/PerfCheckResultList/createPerfChkResultDataGrid started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);

		String rstUrl = "performanceCheckMng/performanceCheckResultList";
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelAndView(rstUrl, model);
	}

	@RequestMapping("/PerfCheckResultList/createPerfChkResultTab2.json")
	// public ModelAndView createPerfChkResultTab2(@RequestParam(required =
	// true) Map<String, Object> param, HttpSession httpSession,
	// HttpServletRequest request) throws Exception {
	public ModelAndView createPerfChkResultTab2(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {

		logger.debug("/PerfCheckResultList/createPerfChkResultTab2 started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);

		Login login = SessionManager.getLoginSession();
		logger.debug("login:" + login);
		Users users = login.getUsers();
		logger.debug("users:" + users);
		String auth_cd = users.getAuth_cd();
		logger.debug("auth_cd:" + auth_cd);
		String user_id = users.getUser_id();
		logger.debug("user_id:" + user_id);

		logger.debug("PROGRAM_ID:" + performanceCheckMng.getProgram_id());
		logger.debug("PERF_CHECK_ID:" + performanceCheckMng.getPerf_check_id());

		Map<String, Object> model = new HashMap<String, Object>();

		PerformanceCheckMng perfCheckAllPgm = perfChkMngSvc.getPerfCheckAllPgm(performanceCheckMng);
		String program_source_desc2 = perfCheckAllPgm.getProgram_source_desc();
		program_source_desc2 = program_source_desc2.replace("${", ":");
		program_source_desc2 = program_source_desc2.replace("#{", ":");
		program_source_desc2 = program_source_desc2.replace("}", "");
		perfCheckAllPgm.setProgram_source_desc2(program_source_desc2);

		logger.debug("perfCheckAllPgm:" + perfCheckAllPgm);
		model.put("perfCheckAllPgm", perfCheckAllPgm);

		/** 수행회차 */
		List<PerformanceCheckMng> programExecuteTmsList = perfChkMngSvc
				.selectProgramExecuteTmsList(performanceCheckMng);
		logger.debug("programExecuteTmsList:" + programExecuteTmsList);
		model.put("programExecuteTmsList", programExecuteTmsList);

		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		logger.debug("programExecuteTms:" + programExecuteTms);
		if (!programExecuteTms.equals("")) {
			model.put("programExecuteTms", programExecuteTms);
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
			List<PerformanceCheckMng> deployPerfChkExecBindList = perfChkMngSvc
					.selectDeployPerfChkExecBindList(performanceCheckMng);
			logger.debug("deployPerfChkExecBindList:" + deployPerfChkExecBindList);
			model.put("deployPerfChkExecBindList", deployPerfChkExecBindList);
		}
		
		String maxProgramExecuteTms = perfChkMngSvc.selectMaxProgramExecuteTmsPlus1(performanceCheckMng);
		performanceCheckMng.setProgramExecuteTms( Integer.valueOf( maxProgramExecuteTms ) - 1 );
		PerformanceCheckMng perfCheckMng = perfChkMngSvc.getPagingYnCnt( performanceCheckMng );
		
		if ( perfCheckMng != null && !perfCheckMng.getPaging_yn().equals("") ) {
			perfCheckMng.setPagingYn( perfCheckMng.getPaging_yn() );
			perfCheckMng.setPagingCnt( Integer.valueOf( perfCheckMng.getPaging_cnt() ));
		}
		model.put("perfCheckMng", perfCheckMng);

		String rstUrl = "performanceCheckMng/performanceCheckResultTab2";
		return new ModelAndView(rstUrl, model);
	}

	@RequestMapping(value = "/PerfCheckResultList/createPerfChkResultTab2BodySelect", method = RequestMethod.POST)
	@ResponseBody
	public Result createPerfChkResultTab2BodySelect(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, HttpServletRequest request,
			Model model) {
		Result result = new Result();
		String html = performanceCheckMng.getHtml();

		try {
			Document doc = Jsoup.parseBodyFragment(html);	// jsoup 1.10.3
//			Document doc = Jsoup.parse(html);				// jsoup 1.11.3
//			Element body = doc.selectFirst("body");			// jsoup 1.11.3
			
			result.setResult(true);
			result.setMessage("HTML BODY 추출완료");
//			result.setTxtValue(body.html());			// jsoup 1.11.3
			result.setTxtValue(doc.html());				// jsoup 1.10.3
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setStatus("exception");
			result.setMessage("프로그램 정보 조회중 오류가 발생하였습니다.");
		}
		return result;
	}

	/**
	 * 파라미터 : PERF_CHECK_ID,PERF_CHECK_STEP_ID,PROGRAM_ID,PROGRAM_EXECUTE_TMS 수행회차
	 * 변경에 따라 순번, 변수명, 변수값, 변수타입 조회
	 * 
	 * @param performanceCheckMng
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/PerfCheckResultList/selectDeployPerfChkExecBindList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkExecBindList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();

		try {
			String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
			logger.debug("programExecuteTms:" + programExecuteTms);
			if (!programExecuteTms.equals("")) {
				performanceCheckMng.setProgram_execute_tms(programExecuteTms);
				resultList = perfChkMngSvc.selectDeployPerfChkExecBindList(performanceCheckMng);
			}

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 바인드 목록 action */
	@RequestMapping(value = "/PerfCheckResultList/selectDeployPerfChkExecBindListPop", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkExecBindListPop(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();

		try {
			performanceCheckMng.setEndRowNum(10);
			resultList = perfChkMngSvc.selectDeployPerfChkExecBindListPop(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 바인드 목록 action */
	@RequestMapping(value = "/PerfCheckResultList/selectDeployPerfChkExecBindValue", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkExecBindValue(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();

		String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());
		String perf_check_step_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_step_id());
		String program_id = StringUtils.defaultString(performanceCheckMng.getProgram_id());
		String program_execute_tms = StringUtils.defaultString(performanceCheckMng.getProgram_execute_tms());

		logger.debug("perf_check_id :" + perf_check_id);
		logger.debug("perf_check_step_id :" + perf_check_step_id);
		logger.debug("program_id :" + program_id);
		logger.debug("program_execute_tms :" + program_execute_tms);

		String pop_perf_check_id = StringUtils.defaultString(performanceCheckMng.getPop_perf_check_id());
		String pop_perf_check_step_id = StringUtils.defaultString(performanceCheckMng.getPop_perf_check_step_id());
		String pop_program_id = StringUtils.defaultString(performanceCheckMng.getPop_program_id());
		String pop_program_execute_tms = StringUtils.defaultString(performanceCheckMng.getPop_program_execute_tms());

		logger.debug("pop_perf_check_id :" + pop_perf_check_id);
		logger.debug("pop_perf_check_step_id :" + pop_perf_check_step_id);
		logger.debug("pop_program_id :" + pop_program_id);
		logger.debug("pop_program_execute_tms :" + pop_program_execute_tms);

		performanceCheckMng.setPerf_check_id(pop_perf_check_id);
		performanceCheckMng.setPerf_check_step_id(pop_perf_check_step_id);
		performanceCheckMng.setProgram_id(pop_program_id);
		performanceCheckMng.setProgram_execute_tms(pop_program_execute_tms);
		if (pop_perf_check_id.equals("") || pop_perf_check_step_id.equals("") || pop_program_id.equals("")
				|| pop_program_execute_tms.equals("")) {
			return success(resultList).toJSONObject().toString();
		}
		try {
			resultList = perfChkMngSvc.selectDeployPerfChkExecBindValue(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 바인드 목록 action */
	@RequestMapping(value = "/PerfCheckResultList/selectDeployPerfChkAllPgmList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkAllPgmList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		logger.debug("programExecuteTms:" + programExecuteTms);
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		try {
			performanceCheckMng.setEndRowNum(10);
			resultList = perfChkMngSvc.selectDeployPerfChkAllPgmList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 바인드 목록 action */
	@RequestMapping(value = "/PerfCheckResultList/selectVsqlBindCaptureList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectVsqlBindCaptureList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		logger.debug("programExecuteTms:" + programExecuteTms);
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		try {
			resultList = perfChkMngSvc.selectVsqlBindCaptureList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	@RequestMapping("/PerfCheckResultList/createPerfChkResultTab3.json")
	// public ModelAndView createPerfChkResultTab3(@RequestParam(required =
	// true) Map<String, Object> param, HttpSession httpSession,
	// HttpServletRequest request) throws Exception {
	public ModelAndView createPerfChkResultTab3(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {

		logger.debug("/PerfCheckResultList/createPerfChkResultTab3.json started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);
		String rstUrl = "performanceCheckMng/performanceCheckResultTab3";
		Map<String, Object> model = new HashMap<String, Object>();

		Login login = SessionManager.getLoginSession();
		logger.debug("login:" + login);
		Users users = login.getUsers();
		logger.debug("users:" + users);
		String auth_cd = users.getAuth_cd();
		logger.debug("auth_cd:" + auth_cd);
		String user_id = users.getUser_id();
		logger.debug("user_id:" + user_id);

		String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());
		logger.debug("perf_check_id:" + perf_check_id);
		if (perf_check_id.equals("")) {
			rstUrl = "login/toplogout";
			return new ModelAndView(rstUrl, model);
		}

		PerformanceCheckMng deployCheckStatus = perfChkMngSvc.selectDeployCheckStatus(performanceCheckMng);
		String deploy_check_status_cd = StringUtils.defaultString(deployCheckStatus.getDeploy_check_status_cd());
		model.put("deploy_check_status_cd", deploy_check_status_cd);

		String maxProgramExecuteTms = perfChkMngSvc.selectMaxProgramExecuteTmsPlus1(performanceCheckMng);
		performanceCheckMng.setProgram_execute_tms(maxProgramExecuteTms);

		PerformanceCheckMng perfCheckAllPgm = perfChkMngSvc.getPerfCheckAllPgm(performanceCheckMng);
		logger.debug("perfCheckAllPgm:" + perfCheckAllPgm);
		model.put("perfCheckAllPgm", perfCheckAllPgm);

		// PerformanceCheckMng deployPerfChkStepTestDb=
		// perfChkMngSvc.selectDeployPerfChkStepTestDbList(performanceCheckMng);
		PerformanceCheckMng defaultParsingSchemaInfo = perfChkMngSvc
				.selectDefaultParsingSchemaInfo(performanceCheckMng);
		logger.debug("defaultParsingSchemaInfo:" + defaultParsingSchemaInfo);
		model.put("defaultParsingSchemaInfo", defaultParsingSchemaInfo);

		logger.debug("getWrkjob_cd:" + performanceCheckMng.getWrkjob_cd());
		logger.debug("getPerf_check_step_id:" + performanceCheckMng.getPerf_check_step_id());

		logger.debug("<program_id>:::::::::::::::::::::::");
		logger.debug(performanceCheckMng.getProgram_id());
		logger.debug(":::::::::::::::::::::::</program_id>");
		// 바인딩값 자동 세팅을 위해 추가
		performanceCheckMng.setEndRowNum(1);
		List<PerformanceCheckMng> selectDeployPerfChkExecBindList = perfChkMngSvc
				.selectDeployPerfChkExecBindListPop(performanceCheckMng);
		List<PerformanceCheckMng> bindValueList = null;
		if (selectDeployPerfChkExecBindList != null && selectDeployPerfChkExecBindList.size() > 0) {
			PerformanceCheckMng selectDeployPerfChkExecBind = selectDeployPerfChkExecBindList.get(0);

			perf_check_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getPerf_check_id());
			String perf_check_step_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getPerf_check_step_id());
			String program_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getProgram_id());
			String program_execute_tms = StringUtils
					.defaultString(selectDeployPerfChkExecBind.getProgram_execute_tms());

			if (!perf_check_id.equals("") && !perf_check_step_id.equals("") && !program_id.equals("")
					&& !program_execute_tms.equals("")) {
				bindValueList = perfChkMngSvc.selectDeployPerfChkExecBindValue(selectDeployPerfChkExecBind);
				// 성능점검이력 바인드가 없으면 SQL수행이력 바인드 검색
				if (bindValueList == null || bindValueList.size() <= 0) {
					bindValueList = selectVsqlBindCaptureList(selectDeployPerfChkExecBind);
				}
				logger.debug("bindValueList1:" + bindValueList);
			}
		} else {
			bindValueList = selectVsqlBindCaptureList(performanceCheckMng);
			logger.debug("bindValueList2:" + bindValueList);
		}
		model.put("bindValueList", bindValueList);

		return new ModelAndView(rstUrl, model);
	}

//	@RequestMapping("/PerfCheckResultList/createPerfChkResultTab3If")
	@RequestMapping(value = "/PerfCheckResultList/createPerfChkResultTab3If", method = RequestMethod.POST)
	public String createPerfChkResultTab3If(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {

		logger.debug("/PerfCheckResultList/createPerfChkResultTab3If started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);

		String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());

		PerformanceCheckMng deployCheckStatus = perfChkMngSvc.selectDeployCheckStatus(performanceCheckMng);
		String deploy_check_status_cd = StringUtils.defaultString(deployCheckStatus.getDeploy_check_status_cd());
		model.addAttribute("deploy_check_status_cd", deploy_check_status_cd);

		String maxProgramExecuteTms = perfChkMngSvc.selectMaxProgramExecuteTmsPlus1(performanceCheckMng);
		performanceCheckMng.setProgram_execute_tms(maxProgramExecuteTms);

		PerformanceCheckMng perfCheckAllPgm = perfChkMngSvc.getPerfCheckAllPgm(performanceCheckMng);
		logger.debug("perfCheckAllPgm:" + perfCheckAllPgm);
		model.addAttribute("perfCheckAllPgm", perfCheckAllPgm);

		// PerformanceCheckMng deployPerfChkStepTestDb=
		// perfChkMngSvc.selectDeployPerfChkStepTestDbList(performanceCheckMng);
		PerformanceCheckMng defaultParsingSchemaInfo = perfChkMngSvc
				.selectDefaultParsingSchemaInfo(performanceCheckMng);
		logger.debug("defaultParsingSchemaInfo:" + defaultParsingSchemaInfo);
		model.addAttribute("defaultParsingSchemaInfo", defaultParsingSchemaInfo);

		logger.debug("getWrkjob_cd:" + performanceCheckMng.getWrkjob_cd());
		logger.debug("getPerf_check_step_id:" + performanceCheckMng.getPerf_check_step_id());

		logger.debug("<program_id>:::::::::::::::::::::::");
		logger.debug(performanceCheckMng.getProgram_id());
		logger.debug(":::::::::::::::::::::::</program_id>");
		// 바인딩값 자동 세팅을 위해 추가
		performanceCheckMng.setEndRowNum(1);
		List<PerformanceCheckMng> selectDeployPerfChkExecBindList = perfChkMngSvc
				.selectDeployPerfChkExecBindListPop(performanceCheckMng);
		List<PerformanceCheckMng> bindValueList = null;
		if (selectDeployPerfChkExecBindList != null && selectDeployPerfChkExecBindList.size() > 0) {
			PerformanceCheckMng selectDeployPerfChkExecBind = selectDeployPerfChkExecBindList.get(0);

			perf_check_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getPerf_check_id());
			String perf_check_step_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getPerf_check_step_id());
			String program_id = StringUtils.defaultString(selectDeployPerfChkExecBind.getProgram_id());
			String program_execute_tms = StringUtils
					.defaultString(selectDeployPerfChkExecBind.getProgram_execute_tms());

			if (!perf_check_id.equals("") && !perf_check_step_id.equals("") && !program_id.equals("")
					&& !program_execute_tms.equals("")) {
				bindValueList = perfChkMngSvc.selectDeployPerfChkExecBindValue(selectDeployPerfChkExecBind);
				// 성능점검이력 바인드가 없으면 SQL수행이력 바인드 검색
				if (bindValueList == null || bindValueList.size() <= 0) {
					bindValueList = selectVsqlBindCaptureList(selectDeployPerfChkExecBind);
				}
				logger.debug("bindValueList1:" + bindValueList);
			}
		} else {
			bindValueList = selectVsqlBindCaptureList(performanceCheckMng);
			logger.debug("bindValueList2:" + bindValueList);
		}
		model.addAttribute("bindValueList", bindValueList);

		String defaultPagingCnt = perfChkMngSvc.getDefaultPagingCnt(performanceCheckMng);
		model.addAttribute("defaultPagingCnt", defaultPagingCnt);
		
		performanceCheckMng.setProgramExecuteTms( Integer.valueOf(maxProgramExecuteTms) - 1 );
		PerformanceCheckMng perfCheckMng = perfChkMngSvc.getPagingYnCnt( performanceCheckMng );
		
		if ( perfCheckMng != null && !perfCheckMng.getPaging_yn().equals("") ) {
			perfCheckMng.setPagingYn( perfCheckMng.getPaging_yn() );
			perfCheckMng.setPagingCnt( Integer.valueOf( perfCheckMng.getPaging_cnt() ));
		}
		model.addAttribute("perfCheckMng", perfCheckMng);

		String rstUrl = "performanceCheckMng/performanceCheckResultTab3";
		return rstUrl;
	}

	public List<PerformanceCheckMng> selectVsqlBindCaptureList(PerformanceCheckMng performanceCheckMng)
			throws Exception {
		List<PerformanceCheckMng> selectDeployPerfChkAllPgmList = perfChkMngSvc
				.selectDeployPerfChkAllPgmList(performanceCheckMng);
		List<PerformanceCheckMng> BindValueList = null;
		if (selectDeployPerfChkAllPgmList != null && selectDeployPerfChkAllPgmList.size() > 0) {
			PerformanceCheckMng selectDeployPerfChkAllPgm = selectDeployPerfChkAllPgmList.get(0);

			String sql_id = StringUtils.defaultString(selectDeployPerfChkAllPgm.getSql_id());
			String snap_time = StringUtils.defaultString(selectDeployPerfChkAllPgm.getSnap_time());
			String last_captured = StringUtils.defaultString(selectDeployPerfChkAllPgm.getLast_captured());
			performanceCheckMng.setSql_id(sql_id);
			performanceCheckMng.setSnap_time(snap_time);
			performanceCheckMng.setLast_captured(last_captured);
			if (!sql_id.equals("") && !snap_time.equals("") && !last_captured.equals("")) {
				BindValueList = perfChkMngSvc.selectVsqlBindCaptureList(performanceCheckMng);
				logger.debug("BindValueList :" + BindValueList);
			}
		}
		return BindValueList;
	}

	/**
	 * 성능점검결과 탭 - 성능점검수행내역 그리드
	 * 
	 * @param performanceCheckMng
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/PerformanceCheckExeHistList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDeployPerfChkExeHistList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			resultList = perfChkMngSvc.getDeployPerfChkExeHistList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	@RequestMapping(value = "/PerfCheckResultList/getDeployPerfChkExeHistList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectPerfCheckResultTab3List(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			resultList = perfChkMngSvc.getDeployPerfChkExeHistList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	@RequestMapping(value = "/PerfCheckResultList/selectDeployPerfChkDetailResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkDetailResultList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		logger.debug("/PerfCheckResultList/selectDeployPerfChkDetailResultList started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);
		logger.debug("performanceCheckMng.program_execute_tms:" + performanceCheckMng.getProgram_execute_tms());
		logger.debug("performanceCheckMng.Hidden_program_execute_tms:"
				+ performanceCheckMng.getHidden_program_execute_tms());
		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			resultList = perfChkMngSvc.selectDeployPerfChkDetailResultList(performanceCheckMng);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	@RequestMapping("/PerfCheckResultList/selectPerfCheckResultBasisWhy")
	@ResponseBody
	public ModelAndView selectPerfCheckResultBasisWhy(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {

		logger.debug("/PerfCheckResultList/selectPerfCheckResultBasisWhy started");

		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		try {

			PerformanceCheckMng perfCheckResultBasisWhy = perfChkMngSvc
					.selectPerfCheckResultBasisWhy(performanceCheckMng);
			logger.debug("perfCheckResultBasisWhy:" + perfCheckResultBasisWhy);
			mav.addObject("result", true);
			mav.addObject("perf_check_result_basis_why1", perfCheckResultBasisWhy.getPerf_check_result_basis_why1());
			mav.addObject("perf_check_result_basis_why2", perfCheckResultBasisWhy.getPerf_check_result_basis_why2());
			mav.addObject("exec_plan", perfCheckResultBasisWhy.getExec_plan());
		} catch (Exception e) {
			mav.addObject("result", false);
			mav.addObject("perf_check_result_basis_why1", "");
			mav.addObject("perf_check_result_basis_why2", "");
			mav.addObject("exec_plan", "");
		}
		return mav;
	}

	@RequestMapping(value = "/PerfCheckResultList/selectImprovementGuideList", produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectImprovementGuideList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {

		logger.debug("/PerfCheckResultList/selectImprovementGuideList started");
		logger.debug("performanceCheckMng:" + performanceCheckMng);
		logger.debug("performanceCheckMng.program_execute_tms:" + performanceCheckMng.getProgram_execute_tms());
		logger.debug("performanceCheckMng.Hidden_program_execute_tms:"
				+ performanceCheckMng.getHidden_program_execute_tms());
		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			List<PerformanceCheckMng> improvementGuide = perfChkMngSvc.selectImprovementGuideList(performanceCheckMng);
			logger.debug("improvementGuide:" + resultList);
			if (improvementGuide != null) {
				return success(improvementGuide).toJSONObject().toString();
			}
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			e.printStackTrace();
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 수행회차 조회 */
	@RequestMapping(value = "/PerfCheckResultList/selectProgramExecuteTms", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectProgramExecuteTms(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {
		List<PerformanceCheckMng> dbList = new ArrayList<PerformanceCheckMng>();
		/** 수행회차 */
		List<PerformanceCheckMng> databaseList = perfChkMngSvc.selectProgramExecuteTmsList(performanceCheckMng);

		return success(databaseList).toJSONObject().get("rows").toString();
	}

	/* 성능점검수행 , 성능 점검 수행 */
	/* 성능점검을 수행하였습니다. */
	@RequestMapping(value = "/PerformanceCheckExecute", method = RequestMethod.POST)
	@ResponseBody
	public Result PerformanceCheckExecute(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, HttpServletRequest request,
			Model model) {
		Result result = new Result();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		performanceCheckMng.setUser_id(user_id);
		performanceCheckMng.setProgram_executer_id(user_id);

		logger.debug("performanceCheckMng###>" + performanceCheckMng);
		logger.debug("performanceCheckMng.pagingYn###>" + performanceCheckMng.getPagingYn());
		logger.debug("performanceCheckMng.pagingCnt###>" + performanceCheckMng.getPagingCnt());

		String pagingYn = StringUtils.defaultString(performanceCheckMng.getPagingYn(), "N");
		performanceCheckMng.setPagingYn(pagingYn);
		String pagingCnt = StringUtils.defaultString( String.valueOf(performanceCheckMng.getPagingCnt() ), "0");
		if (pagingCnt.equals(""))
			pagingCnt = "0";
		performanceCheckMng.setPagingCnt( Integer.valueOf(pagingCnt) );
		logger.debug("performanceCheckMng.pagingYn###>" + performanceCheckMng.getPagingYn());
		logger.debug("performanceCheckMng.pagingCnt###>" + performanceCheckMng.getPagingCnt());

		String program_exec_div_cd = performanceCheckMng.getProgram_exec_div_cd();
		logger.debug("program_exec_div_cd###>" + program_exec_div_cd);

		try {
			result = perfChkMngSvc.performanceCheckExecuteResult(performanceCheckMng, request);
			logger.debug("result:" + result);

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setStatus("exception");
			result.setMessage("성능점검 오류가 발생하였습니다. <br/>성능점검결과탭에서 오류 내용을 확인하세요.");
		}
		return result;
	}

	/* 튜닝요청 중복 체크 action */
	@RequestMapping(value = "/PerfChkRequestTuningDupChk", method = RequestMethod.POST)
	@ResponseBody
	public Result PerfChkRequestTuningDupChk(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model, HttpServletRequest req) {
		Result result = new Result();
		result.setResult(true);
		result.setMessage("아직 튜닝 요청이 안된 프로그램입니다.");
		
		int resultCount = 0;
		try {
			resultCount = perfChkMngSvc.perfChkRequestTuningDupChk(performanceCheckMng);
			result.setResultCount(resultCount);
			if(resultCount > 0) {
				result.setResult(false);
				result.setMessage("이미 튜닝 요청된 프로그램입니다.");
			}
			return result;
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setResultCount(resultCount);
			result.setMessage("튜닝 요청 중복 체크중 오류가 발생하였습니다.");
		}
		return result;
	}

	/* 튜닝요청 action */
	@RequestMapping(value = "/PerfChkRequestTuning", method = RequestMethod.POST)
	@ResponseBody
	public Result PerfChkRequestTuning(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model, HttpServletRequest req) {
		Result result = new Result();
		int resultList = 0;
		int resultCount = 0;

		try {
			resultCount = perfChkMngSvc.perfChkRequestTuningDupChk(performanceCheckMng);
			if(resultCount > 0) {
				result.setResult(false);
				result.setResultCount(resultCount);
				result.setMessage("이미 튜닝 요청된 프로그램입니다.");
				return result;
			}
			
			resultList = perfChkMngSvc.perfChkRequestTuning(performanceCheckMng, req);
			if (resultList > 0) {
				result.setResult(true);
				result.setMessage("튜닝 요청을 완료하였습니다.");
			} else {
				result.setResult(false);
				result.setMessage("튜닝 요청 중 오류가 발생하였습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage("튜닝 요청 중 오류가 발생하였습니다.");
		}
		return result;
	}

	/* 튜닝요청 action sql_text를 세션에 담는 역할 */
	@RequestMapping(value = "/SetSqlTextToSession", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String RequestTuningAction(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model, HttpServletRequest req) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		try {
			// String sql_text = performanceCheckMng.getSql_text();

			logger.debug("PROGRAM_ID:" + performanceCheckMng.getProgram_id());
			logger.debug("PERF_CHECK_ID:" + performanceCheckMng.getPerf_check_id());

			PerformanceCheckMng perfCheckAllPgm = perfChkMngSvc.getPerfCheckAllPgm(performanceCheckMng);
			logger.debug("perfCheckAllPgm:" + perfCheckAllPgm);
			String sql_text = perfCheckAllPgm.getProgram_source_desc();
			HttpSession session = req.getSession(true);
			session.setAttribute("sql_text", sql_text);

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 배포전성능점검>성능점검관리>성능점검결과>예상실행계획 */
	@RequestMapping(value = "/getDeployPerfChkPlanTable", method = RequestMethod.POST)
	@ResponseBody
	public Result getDeployPerfChkPlanTable(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		Result result = new Result();
		String returnValue = "";
		resultList = perfChkMngSvc.getDeployPerfChkPlanTable(performanceCheckMng);
		List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
		JSONArray jsonArray = JSONArray.fromObject(buildList);

		returnValue = jsonArray.toString();
		result.setResult(true);
		result.setTxtValue(returnValue);
		return result;
	}

	/**
	 * 배포전성능점검>성능점검관리>성능점검결과>예상실행계획 온라인 DML
	 */
	@RequestMapping(value = "/getDeployPerfSqlPlan", method = RequestMethod.POST)
	@ResponseBody
	public Result getDeployPerfSqlPlan(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) throws Exception {
		String program_execute_tms = performanceCheckMng.getHidden_program_execute_tms();
		performanceCheckMng.setProgram_execute_tms(program_execute_tms);
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		Result result = new Result();
		String returnValue = "";
		resultList = perfChkMngSvc.getDeployPerfSqlPlan(performanceCheckMng);
		List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
		JSONArray jsonArray = JSONArray.fromObject(buildList);

		returnValue = jsonArray.toString();
		result.setResult(true);
		result.setTxtValue(returnValue);
		return result;
	}

	@Autowired
	private SelfTuningService selfTuningService;

	@RequestMapping(value = "/getDeployPerfChkPlanTableTest", method = RequestMethod.POST)
	@ResponseBody
	public Result getDeployPerfChkPlanTableTest(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		apmApplSql.setSql_text("select * from orders");
		apmApplSql.setDbid("212205444");
		apmApplSql.setParsing_schema_name("OPENSIMUL");
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		Result result = new Result();
		String returnValue = "";
		try {
			resultList = selfTuningService.explainPlanTreeList(apmApplSql);
			List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
			logger.debug("returnValue:" + returnValue);
			result.setResult(true);
			result.setTxtValue(returnValue);
		} catch (SQLException se) {
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e) {
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/getDefaultParsingSchemaInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDefaultParsingSchemaInfo(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		PerformanceCheckMng defaultParsingSchemaInfo = null;
		try {
			defaultParsingSchemaInfo = perfChkMngSvc.selectDefaultParsingSchemaInfo(performanceCheckMng);
			resultList.add(defaultParsingSchemaInfo);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(defaultParsingSchemaInfo).toJSONObject().toString();
	}

	/**
	 * 성능점검 결과 통보
	 * 
	 * @param rIqms
	 * @return
	 */
	@RequestMapping(value = "PerfChkRsltNoti", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Result PerfChkRsltNoti(PerformanceCheckMng performanceCheckMng, @RequestParam(required = false) String pageName) {
		Result result = new Result();
		try {
			result = perfChkMngSvc.perfChkRsltNoti(performanceCheckMng, pageName);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("성능점검결과 통보중 오류가 발생하였습니다.");
		}
		return result;
	}

}
