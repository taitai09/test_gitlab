package omc.spop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
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

import omc.spop.base.Config;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.BasicCheckConfig;
import omc.spop.model.Board;
import omc.spop.model.DbEmergencyAction;
import omc.spop.model.JobSchedulerExecLog;
import omc.spop.model.Licence;
import omc.spop.model.Menu;
import omc.spop.model.ObjectChange;
import omc.spop.model.PerfGuide;
import omc.spop.model.Result;
import omc.spop.model.SqlDiagSummary;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.DashBoardService;
import omc.spop.service.MainService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.OMCUtil;

@Controller
public class MainController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private DashBoardService dashBoardService;
	
	@Value("#{defaultConfig['version.date']}")
	private String settingDate;
	
	@Value("#{defaultConfig['customer.sso_yn']}")
	private String customerSsoYn;
	
	@Value("#{defaultConfig['end.date']}")
	private String endDate;
	
	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation
	
	@Value("#{defaultConfig['customer']}")
	private String CUSTOMER;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(Model model, @RequestParam(value = "deploy_id", required = false) String deploy_id,
			HttpSession session, HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		logger.debug("deploy_id = " + deploy_id);
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String nowTime = DateUtil.getHourMin();
		
		List<Menu> menuList = mainService.menuList(auth_cd);
		Board noticeBoardOne = mainService.noticeBoardOne();
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		String today = formatter.format(new java.util.Date());
		model.addAttribute("auth_cd", auth_cd);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nowTime", nowTime);
		model.addAttribute("startDate", startDate);
		model.addAttribute("today", today);
		model.addAttribute("settingDate", settingDate);
		model.addAttribute("deploy_id", deploy_id);
		model.addAttribute("menuList", menuList);
		model.addAttribute("notice", noticeBoardOne);
		model.addAttribute("customer", CUSTOMER);
		
		Cookie[] cookies = req.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			model.addAttribute(cookies[i].getName(), cookies[i].getValue());
		}
		
		String jsp = "main";
		
		model = OMCUtil.checkLicensePeried(model, endDate);
		
		if( model.containsAttribute("result") && (Boolean) model.asMap().get("result") == false) {
			jsp = "login/login";
		}
		
		if(customerSsoYn.equalsIgnoreCase("N") && !model.containsAttribute("MYID")) {
			logger.info("/////////////////////////////////////////////////////////////////////////////");
			logger.info("Not existed Cookie.....MainController");
			
			if(session.getAttribute("RSAModulus") != null) {
				model.addAttribute("RSAModulus", session.getAttribute("RSAModulus")); // rsa modulus 를 request 에 추가
				model.addAttribute("RSAExponent", session.getAttribute("RSAExponent")); // rsa exponent 를 request 에 추가
			}
			
			jsp = "login/login";
			
			session.invalidate();
			
			try {
				res.sendRedirect("/");
				
			} catch (IOException e) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error( "{} 예외발생 ==> ", methodName, e );
				res.sendRedirect("/");
			}
			return jsp;
		}
		return jsp;
	}
	
	@RequestMapping(value = "/Dashboard", method = RequestMethod.GET)
	public String Dashboard(Model model, @RequestParam(value = "deploy_id", required = false) String deploy_id)
			throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String nowDateTxt = DateUtil.getNowDate("(yyyy.MM.dd)");
		String gather_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		String gather_day_txt = DateUtil.getPlusDays("yyyyMMdd", "(yyyy.MM.dd)", DateUtil.getNowDate("yyyyMMdd"), -1);
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String today_txt = DateUtil.getNowDate("yyyy.MM.dd");
		
		String max_check_day = StringUtils.defaultString(dashBoardService.getMaxCheckDay());
		String max_check_day_dash = max_check_day.replaceAll("\\.", "-");
		max_check_day_dash = max_check_day_dash.replaceAll("\\(", "");
		max_check_day_dash = max_check_day_dash.replaceAll("\\)", "");
		model.addAttribute("max_check_day_dash", max_check_day_dash);
		
		String max_gather_day = StringUtils.defaultString(dashBoardService.getMaxGatherDay());
		String max_gather_day_dash = max_gather_day.replaceAll("\\.", "-");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\(", "");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\)", "");
		model.addAttribute("max_gather_day_dash", max_gather_day_dash);
		
		String check_date_topsql_diag_summary = StringUtils.defaultString(dashBoardService.getCheckDateTopsqlDiagSummary());
		
		logger.debug("check_date_topsql_diag_summary[" + check_date_topsql_diag_summary + "]");
		
		logger.debug("deploy_id ===>" + deploy_id);
		model.addAttribute("auth_cd", auth_cd);
		model.addAttribute("user_id", user_id);
		model.addAttribute("check_day", nowDate);
		model.addAttribute("check_day_txt", nowDateTxt);
		model.addAttribute("gather_day", gather_day);
		model.addAttribute("gather_day_txt", gather_day_txt);
		model.addAttribute("max_gather_day", max_gather_day);
		model.addAttribute("leader_yn", leader_yn);
		model.addAttribute("wrkjob_cd", wrkjob_cd);
		model.addAttribute("max_check_day", max_check_day);
		model.addAttribute("today_txt", today_txt);
		model.addAttribute("check_date_topsql_diag_summary", check_date_topsql_diag_summary);
		
		String gather_day_dash = dashBoardService.getGatherDayDash();
		model.addAttribute("gather_day_dash", gather_day_dash);
		
		String max_base_day = dashBoardService.getMaxBaseDay();
		model.addAttribute("max_base_day", max_base_day);
		
		model.addAttribute("settingDate", settingDate);
		model.addAttribute("menu_id", "000");
		
		String jsp = "";
		
		if (auth_cd.equals("ROLE_ITMANAGER")) { // IT관리자
			jsp = "dashboard/itmanager";
//			jsp = "dashboard/dashboardV2"; // Ver.2
		} else if (auth_cd.equals("ROLE_DBMANAGER")) { // DB관리자
//			jsp = "dashboard/dbmanager";
//			jsp = "dashboard/dashboard";
//			jsp = "dashboard/dashboardV2"; // Ver.2
			TuningTargetSql tts = new TuningTargetSql();
			
			model.addAttribute("waitJobCnt", mainService.tuningWaitJobCnt(tts));
			model.addAttribute("progressCnt", mainService.tuningProgressCnt(tts));
			model.addAttribute("expectedDelayCnt", mainService.tuningExpectedDelayCnt(tts));
			model.addAttribute("delayCnt", mainService.tuningDelayCnt(tts));
			model.addAttribute("completeCnt", mainService.tuningCompleteCnt(tts));
			
			jsp = "dashboard/tunerV2";
		} else if (auth_cd.equals("ROLE_TUNER")) { // 튜너
			TuningTargetSql tts = new TuningTargetSql();
			
			model.addAttribute("waitJobCnt", mainService.tuningWaitJobCnt(tts));
			model.addAttribute("progressCnt", mainService.tuningProgressCnt(tts));
			model.addAttribute("expectedDelayCnt", mainService.tuningExpectedDelayCnt(tts));
			model.addAttribute("delayCnt", mainService.tuningDelayCnt(tts));
			model.addAttribute("completeCnt", mainService.tuningCompleteCnt(tts));
			
			jsp = "dashboard/tunerV2";
		} else if (auth_cd.equals("ROLE_OPENPOPMANAGER")) { // Open-POP관리자
//			jsp = "dashboard/dashboardV2"; // Ver.2
			
			JobSchedulerExecLog jobSchedulerExecLog = new JobSchedulerExecLog();
			String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
			
			jobSchedulerExecLog.setStrStartDt(startDate);
			jobSchedulerExecLog.setStrEndDt(nowDate);
			
			model.addAttribute("endDate", nowDate);
			model.addAttribute("jobSchedulerExecLog",jobSchedulerExecLog);
			
			jsp = "examineOpenPOP/examineScheduler";
			
		} else if (auth_cd.equals("ROLE_DEV_DEPLOYMANAGER")) { // 업무개발_배포성능관리자
			jsp = "performanceCheckIndex/ProPerfExcReq";
		} else if (auth_cd.equals("ROLE_DEPLOYMANAGER")) { // 배포성능관리자
			jsp = "performanceCheckMng/performanceCheckMng";
		} else if (auth_cd.equals("ROLE_DA")) { // DA
			jsp = "mqm/qualityInspectionJob";
		} else { // 개발자
			model.addAttribute("sql_std_qty_div_cd", Config.getString("sql_std_qty_div_cd"));
			jsp = "dashboard/dev";
		}
		
		return jsp;
	}
	
	@RequestMapping(value = "/Dashboard/dbmanager", method = RequestMethod.GET)
	public String DashboardDbmanager(Model model) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String today = DateUtil.getNowDate("yyyyMMdd");
		String today_txt = DateUtil.getNowDate("(yyyy.MM.dd)");
		String gather_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		String gather_day_txt = DateUtil.getPlusDays("yyyyMMdd", "(yyyy.MM.dd)", DateUtil.getNowDate("yyyyMMdd"), -1);
		String max_gather_day = dashBoardService.getMaxGatherDay();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("today", today);
		model.addAttribute("today_txt", today_txt);
		model.addAttribute("gather_day", gather_day);
		model.addAttribute("gather_day_txt", gather_day_txt);
		model.addAttribute("max_gather_day", max_gather_day);
		
		String jsp = "";
		
		if (auth_cd.equals("ROLE_ITMANAGER") || auth_cd.equals("ROLE_DBMANAGER") || auth_cd.equals("ROLE_TUNER")) { // IT관리자,DB관리자
			jsp = "dashboard/dbmanager";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/Dashboard/dashboard", method = RequestMethod.GET)
	public String DashboardDashboard(Model model) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String nowDateTxt = DateUtil.getNowDate("(yyyy.MM.dd)");
		String gather_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		String gather_day_txt = DateUtil.getPlusDays("yyyyMMdd", "(yyyy.MM.dd)", DateUtil.getNowDate("yyyyMMdd"), -1);
		String max_gather_day = dashBoardService.getMaxGatherDay();
		String max_gather_day_dash = max_gather_day.replaceAll("\\.", "-");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\(", "");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\)", "");
		String max_check_day = dashBoardService.getMaxCheckDay();
		logger.debug("max_check_day:" + max_check_day);
		String max_check_day_dash = max_check_day.replaceAll("\\.", "-");
		max_check_day_dash = max_check_day_dash.replaceAll("\\(", "");
		max_check_day_dash = max_check_day_dash.replaceAll("\\)", "");
		model.addAttribute("user_id", user_id);
		model.addAttribute("check_day", nowDate);
		model.addAttribute("check_day_txt", nowDateTxt);
		model.addAttribute("gather_day", gather_day);
		model.addAttribute("gather_day_txt", gather_day_txt);
		model.addAttribute("max_gather_day", max_gather_day);
		model.addAttribute("max_gather_day_dash", max_gather_day_dash);
		model.addAttribute("max_check_day", max_check_day);
		model.addAttribute("max_check_day_dash", max_check_day_dash);
		
		String jsp = "";
		
		if (auth_cd.equals("ROLE_ITMANAGER") || auth_cd.equals("ROLE_DBMANAGER")) { // IT관리자,DB관리자
			jsp = "dashboard/dashboard";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/DashboardV2", method = RequestMethod.GET)
	public String DashboardV2(Model model) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String nowDateTxt = DateUtil.getNowDate("(yyyy.MM.dd)");
		String gather_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		String gather_day_txt = DateUtil.getPlusDays("yyyyMMdd", "(yyyy.MM.dd)", DateUtil.getNowDate("yyyyMMdd"), -1);
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String today_txt = DateUtil.getNowDate("yyyy.MM.dd");
		
		String max_check_day = StringUtils.defaultString(dashBoardService.getMaxCheckDay());
		String max_check_day_dash = max_check_day.replaceAll("\\.", "-");
		max_check_day_dash = max_check_day_dash.replaceAll("\\(", "");
		max_check_day_dash = max_check_day_dash.replaceAll("\\)", "");
		model.addAttribute("max_check_day_dash", max_check_day_dash);
		
		String max_gather_day = StringUtils.defaultString(dashBoardService.getMaxGatherDay());
		String max_gather_day_dash = max_gather_day.replaceAll("\\.", "-");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\(", "");
		max_gather_day_dash = max_gather_day_dash.replaceAll("\\)", "");
		model.addAttribute("max_gather_day_dash", max_gather_day_dash);
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("check_day", nowDate);
		model.addAttribute("check_day_txt", nowDateTxt);
		model.addAttribute("gather_day", gather_day);
		model.addAttribute("gather_day_txt", gather_day_txt);
		model.addAttribute("max_gather_day", max_gather_day);
		model.addAttribute("leader_yn", leader_yn);
		model.addAttribute("wrkjob_cd", wrkjob_cd);
		model.addAttribute("max_check_day", max_check_day);
		model.addAttribute("today_txt", today_txt);
		
		String check_date_topsql_diag_summary = StringUtils.defaultString(dashBoardService.getCheckDateTopsqlDiagSummary());
		
		logger.debug("check_date_topsql_diag_summary[" + check_date_topsql_diag_summary + "]");
		
		model.addAttribute("check_date_topsql_diag_summary", check_date_topsql_diag_summary);
		
		String gather_day_dash = dashBoardService.getGatherDayDash();
		model.addAttribute("gather_day_dash", gather_day_dash);
		
		String max_base_day = dashBoardService.getMaxBaseDay();
		model.addAttribute("max_base_day", max_base_day);
		
		model.addAttribute("settingDate", settingDate);
		model.addAttribute("menu_id", "000");
		
		String jsp = "dashboard/dashboardV2"; // Ver.2
		
		return jsp;
	}
	
	/* Dashboard - (관리자) 성능 개선 진행 현황 action */
	@RequestMapping(value = "/Dashboard/ImprovementProgress", method = RequestMethod.POST)
	@ResponseBody
	public Result ImprovementProgressAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		Result result = new Result();
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		
		try {
			resultList = mainService.improvementProgressList(tuningTargetSql);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage("성능 개선 진행 현황 조회에 실패하였습니다.");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* Dashboard - (관리자) 성능개선 3개월 누적 실적 현황 차트 action */
	@RequestMapping(value = "/Dashboard/ImprovementPerformanceChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ImprovementPerformanceChartAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		
		try {
			resultList = mainService.improvementPerformanceChartList(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 성능 개선 유형 차트 action */
	@RequestMapping(value = "/Dashboard/ImprovementsChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ImprovementsChartAction(@ModelAttribute("sqlImprovementType") SqlImprovementType sqlImprovementType,
			Model model) {
		List<SqlImprovementType> resultList = new ArrayList<SqlImprovementType>();
		
		try {
			resultList = mainService.improvementsChartList(sqlImprovementType);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 튜너별 작업부하 현황 차트 action */
	@RequestMapping(value = "/Dashboard/TunerJobChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TunerJobChartAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		
		try {
			resultList = mainService.tunerJobChartList(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 튜닝 요청현황 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningRequest", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningRequestAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		
		try {
			resultList = mainService.tuningRequestList(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) DB 예방 점검 현황 차트 action */
	@RequestMapping(value = "/Dashboard/PreventionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String PreventionChartAction(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig,
			Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();
		
		try {
			resultList = mainService.preventionChartList(basicCheckConfig);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 성능 리스크 진단 현황 차트 action */
	@RequestMapping(value = "/Dashboard/RiskDiagnosisChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String RiskDiagnosisChartAction(@ModelAttribute("sqlDiagSummary") SqlDiagSummary sqlDiagSummary,
			Model model) {
		List<SqlDiagSummary> resultList = new ArrayList<SqlDiagSummary>();
		
		try {
			resultList = mainService.riskDiagnosisChartList(sqlDiagSummary);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 튜닝 진행 현황 차트 action */
	@RequestMapping(value = "/Dashboard/TuningProgressChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningProgressChartAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		
		try {
			resultList = mainService.tuningProgressChartList(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 오브젝트 변경 현황 차트 action */
	@RequestMapping(value = "/Dashboard/ObjectChangeChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ObjectChangeChartAction(@ModelAttribute("objectChange") ObjectChange objectChange, Model model) {
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		
		try {
			resultList = mainService.objectChangeChartList(objectChange);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (DBA) 긴급 조치 대상 리스트 action */
	@RequestMapping(value = "/Dashboard/UrgentAction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String UrgentAction(@ModelAttribute("dbEmergencyAction") DbEmergencyAction dbEmergencyAction, Model model) {
		List<DbEmergencyAction> resultList = new ArrayList<DbEmergencyAction>();
		
		try {
			resultList = mainService.urgentActionList(dbEmergencyAction);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - (튜너) 긴급 조치 대상 - 조치완료 처리 */
	@RequestMapping(value = "/Dashboard/UpdateUrgentAction", method = RequestMethod.POST)
	@ResponseBody
	public Result UpdateUrgentAction(@ModelAttribute("dbEmergencyAction") DbEmergencyAction dbEmergencyAction,
			Model model) {
		
		Result result = new Result();
		int updateRow = 0;
		
		String emergency_action_no = StringUtils.defaultString(dbEmergencyAction.getEmergency_action_no());
		if (emergency_action_no.contains(",")) {
			String emergency_action_no_array[] = emergency_action_no.split(",");
			for (String s : emergency_action_no_array) {
				logger.debug("emergency_action_no:" + s);
				dbEmergencyAction.setEmergency_action_no(s);
				try {
					updateRow = mainService.updateUrgentAction(dbEmergencyAction);
					
					result.setResult(updateRow > 0 ? true : false);
					result.setMessage("긴급 조치 처리가 실패하였습니다.");
					
				} catch (Exception ex) {
					String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
					logger.error( "{} 예외발생 ==> ", methodName, ex );
					result.setResult(false);
					result.setMessage(ex.getMessage());
				}
			}
		} else {
			try {
				updateRow = mainService.updateUrgentAction(dbEmergencyAction);
				
				result.setResult(updateRow > 0 ? true : false);
				result.setMessage("긴급 조치 처리가 실패하였습니다.");
				
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error( "{} 예외발생 ==> ", methodName, ex );
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}
		}
		return result;
	}
	
	/* Dashboard - (튜너) 튜닝 작업대기 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningWaitJob", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningWaitJobAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.tuningWaitJobList(tuningTargetSql);
			
			if(resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* Dashboard - (튜너) 튜닝 진행 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningProgress", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningProgressAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.tuningProgressList(tuningTargetSql);
			
			if(resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* Dashboard - (튜너) 튜닝 지연예상 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningExpectedDelay", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningExpectedDelayAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.tuningExpectedDelayList(tuningTargetSql);
			
			if(resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* Dashboard - (튜너,DBA) 튜닝 지연 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningDelay", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningDelayAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.tuningDelayList(tuningTargetSql);
			
			if(resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* Dashboard - (튜너) 튜닝 완료 리스트 action */
	@RequestMapping(value = "/Dashboard/TuningComplete", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningCompleteAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.tuningCompleteList(tuningTargetSql);
			
			if(resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* Dashboard - (튜너) 튜닝 실적 차트 action */
	@RequestMapping(value = "/Dashboard/TuningPerformanceChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningPerformanceChartAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();

		try {
			resultList = mainService.tuningPerformanceChartList(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - (개발자) My WorkList action */
	@RequestMapping(value = "/Dashboard/MyWork", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MyWorkAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = mainService.myWorkList(tuningTargetSql);
			if (resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/* Dashboard - (개발자) 사례/가이드 List action */
	@RequestMapping(value = "/Dashboard/Precedent", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String PrecedentAction(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		List<PerfGuide> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		
		try {
			int dataCount4NextBtn = 0;
			
			String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
			perfGuide.setAuth_cd(auth_cd);
			
			resultList = mainService.precedentList(perfGuide);
			
			if (resultList != null && resultList.size() > perfGuide.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(perfGuide.getPagePerCount());	// 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			returnStr = jobj.toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* Dashboard - (개발자) 형상기반 SQL 표준 점검 */
	@RequestMapping(value = "/Dashboard/loadScmBasedStdQtyChk", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadScmBasedStdQtyChk(@RequestParam(required = false) Map<String, String> param) {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			param.put("user_id", user_id);
			
			resultList = mainService.loadScmBasedStdQtyChk(param);
			returnStr = getJSONResult(resultList, true).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		return returnStr;
	}
	
	@RequestMapping(value = "/licence/Check", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LicenceCheck(@ModelAttribute("licence") Licence licence, Model model) {
		List<Licence> resultList = null;
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		licence.setUser_id( user_id );
		
		try {
			resultList = mainService.licenseInquiry( licence );
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/licence/getLicenseExceeded", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getLicenseExceeded(@ModelAttribute("licence") Licence licence, Model model) {
		List<Licence> resultList = null;
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		licence.setUser_id( user_id );
		
		try {
			resultList = mainService.getLicenseExceeded( licence );
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/licence/getNoLicense", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getNoLicense(@ModelAttribute("licence") Licence licence, Model model) {
		List<Licence> resultList = null;
		
		try {
			resultList = mainService.getNoLicense( licence );
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/licence/updateCloseLicensePopupForWeek", method = RequestMethod.GET)
	@ResponseBody
	public Result updateCloseLicensePopupForWeek(@ModelAttribute("licence") Licence licence, Model model) {
		Result result = new Result();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		licence.setUser_id( user_id );
		
		logger.debug("licence ==================> "+licence.getLicence_id() + " , " +licence.getUser_id() );
		try {
			int cnt = mainService.updateCloseLicensePopupForWeek( licence );
			
			if ( cnt > 0 ) {
				result.setResult( true );
			} else {
				result.setResult( false );
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult( false );
			result.setMessage(ex.getMessage());
		}
		return result;
	}
}