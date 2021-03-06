package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
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

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ActiveSessionHistory;
import omc.spop.model.ApmApplSql;
import omc.spop.model.DbaHistSqlstat;
import omc.spop.model.Module;
import omc.spop.model.OdsApmSAppl;
import omc.spop.model.OdsApmSSqls;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SessIO;
import omc.spop.model.SessMetric;
import omc.spop.model.SessTimeModel;
import omc.spop.model.Session;
import omc.spop.model.SessionConnectInfo;
import omc.spop.model.SessionEvent;
import omc.spop.model.SessionLongops;
import omc.spop.model.SessionSql;
import omc.spop.model.SessionWait;
import omc.spop.model.SessionWaitClass;
import omc.spop.model.SqlGrid;
import omc.spop.model.SqlStatByPlan;
import omc.spop.model.TableUseSql;
import omc.spop.service.SQLAnalysisService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelWrite;
import omc.spop.utils.TreeWrite;
import omc.spop.utils.WriteOption;

/***********************************************************
 * 2018.03.13 ????????? OPENPOP V2 ????????????
 **********************************************************/

@Controller
public class SQLAnalysisController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SQLAnalysisController.class);

	@Autowired
	private SQLAnalysisService sqlAnalysisService;

	/* ?????????????????? ?????? */
	@RequestMapping(value = "/APPPerformance", method = RequestMethod.GET)
	public String TradingPerformance(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model)
			throws Exception {
		

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", apmApplSql.getMenu_id());
		model.addAttribute("menu_nm", apmApplSql.getMenu_nm());
		model.addAttribute("wrkjob_cd", apmApplSql.getWrkjob_cd());
		model.addAttribute("dbio", apmApplSql.getDbio());
		model.addAttribute("searchKey", apmApplSql.getSearchKey());
		model.addAttribute("tr_cd", apmApplSql.getTr_cd());
		model.addAttribute("call_from_parent", apmApplSql.getCall_from_parent());

		return "appPerformance";
	}

	/* ?????????????????? ?????? - list action */
	@RequestMapping(value = "/APPPerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String APPPerformanceAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model)
			throws Exception {
		
		List<ApmApplSql> resultList = new ArrayList<ApmApplSql>();

		try {
			resultList = sqlAnalysisService.appPerformanceList(apmApplSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - ?????? ?????? list action */
	@RequestMapping(value = "/APPPerformance/APPDailyList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String APPDailyListAction(@ModelAttribute("odsApmSAppl") OdsApmSAppl odsApmSAppl, Model model)
			throws Exception {

		List<OdsApmSAppl> resultList = new ArrayList<OdsApmSAppl>();

		String appl_hash = StringUtils.defaultString(odsApmSAppl.getAppl_hash());
		if (!appl_hash.equals("")) {
			try {
				resultList = sqlAnalysisService.appDailyList(odsApmSAppl);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - ?????? ????????? list action */
	@RequestMapping(value = "/APPPerformance/APPTimeList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String APPTimeListAction(@ModelAttribute("odsApmSAppl") OdsApmSAppl odsApmSAppl, Model model)
			throws Exception {
		
		List<OdsApmSAppl> resultList = new ArrayList<OdsApmSAppl>();
		String appl_hash = StringUtils.defaultString(odsApmSAppl.getAppl_hash());
		if (!appl_hash.equals("")) {
			try {
				resultList = sqlAnalysisService.appTimeList(odsApmSAppl);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - DBIO ?????? list action */
	@RequestMapping(value = "/APPPerformance/DBIODailyList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DBIODailyListAction(@ModelAttribute("odsApmSSqls") OdsApmSSqls odsApmSSqls, Model model)
			throws Exception {
		
		List<OdsApmSSqls> resultList = new ArrayList<OdsApmSSqls>();
		String sql_hash = StringUtils.defaultString(odsApmSSqls.getSql_hash());
		if (!sql_hash.equals("")) {
			try {
				resultList = sqlAnalysisService.dbioDailyList(odsApmSSqls);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - DBIO ????????? list action */
	@RequestMapping(value = "/APPPerformance/DBIOTimeList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DBIOTimeListAction(@ModelAttribute("odsApmSSqls") OdsApmSSqls odsApmSSqls, Model model)
			throws Exception {
		
		List<OdsApmSSqls> resultList = new ArrayList<OdsApmSSqls>();
		String sql_hash = StringUtils.defaultString(odsApmSSqls.getSql_hash());
		if (!sql_hash.equals("")) {
			try {
				resultList = sqlAnalysisService.dbioTimeList(odsApmSSqls);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - DBIO ???????????? list action */
	@RequestMapping(value = "/APPPerformance/DBIOHistoryList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DBIOHistoryListAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {
		
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		String sql_hash = StringUtils.defaultString(odsHistSqlstat.getSql_hash());
		if (!sql_hash.equals("")) {
			try {
				resultList = sqlAnalysisService.dbioHistoryList(odsHistSqlstat);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? ?????? - Get SQL Text action */
	@RequestMapping(value = "/APPPerformance/GetSQLText", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String GetSQLTextAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		
		String sql_hash = StringUtils.defaultString(odsHistSqlText.getSql_hash());
		OdsHistSqlText tempOdsHistSqlText = new OdsHistSqlText();
		if (!sql_hash.equals("")) {
			try {
				tempOdsHistSqlText = sqlAnalysisService.getSqlText2(odsHistSqlText);
			} catch (Exception ex) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " ???????????? ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(tempOdsHistSqlText).toJSONObject().toString();
	}
	
	/* ?????????????????? */
	@RequestMapping(value = "/SessionMonitoring", method = RequestMethod.GET)
	public String SessionMonitoring(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", session.getMenu_id());
		model.addAttribute("menu_nm", session.getMenu_nm());

		return "sqlAnalysis/sessionMonitoring";
	}

	/* ?????????????????? - list action */
	@RequestMapping(value = "/SessionMonitoring", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SessionMonitoringAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<Session> resultList = new ArrayList<Session>();
		String returnValue = "";

		try {
			resultList = sqlAnalysisService.sessionMonitoringList(session);
			List<Session> buildList = TreeWrite.buildSessionGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}

	/* ?????????????????? - wait class chart action */
	@RequestMapping(value = "/SessionMonitoring/WaitClassChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SessionWaitClassChartAction(@ModelAttribute("session") Session session, Model model)
			throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			resultList = sqlAnalysisService.sessionWaitClassChartList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - top event chart action */
	@RequestMapping(value = "/SessionMonitoring/TOPEventChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TOPEventChartAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			resultList = sqlAnalysisService.topEventChartList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - top module chart action */
	@RequestMapping(value = "/SessionMonitoring/TOPModuleChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TOPModuleChartAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<omc.spop.vo.server.Module> resultList = new ArrayList<omc.spop.vo.server.Module>();

		try {
			resultList = sqlAnalysisService.topModuleChartList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - session kill */
	@RequestMapping(value = "/SessionMonitoring/SessionKill", method = RequestMethod.POST)
	@ResponseBody
	public Result SessionKillAction(@ModelAttribute("session") Session session, Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setTxtValue(sqlAnalysisService.sessionKill(session));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ?????????????????? - last Cursor */
	@RequestMapping(value = "/SessionMonitoring/LastCursor", method = RequestMethod.POST)
	@ResponseBody
	public Result LastCursorAction(@ModelAttribute("session") Session session, Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setTxtValue(sqlAnalysisService.lastCursor(session));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ?????????????????? - all Cursor */
	@RequestMapping(value = "/SessionMonitoring/AllCursor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String AllCursorAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<Session> resultList = new ArrayList<Session>();

		try {
			resultList = sqlAnalysisService.allCursorList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - SQL ?????? */
	@RequestMapping(value = "/SessionMonitoring/SQLPerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformanceAction(@ModelAttribute("sessionSql") SessionSql sessionSql, Model model)
			throws Exception {
		
		List<SessionSql> resultList = new ArrayList<SessionSql>();

		try {
			resultList = sqlAnalysisService.sqlPerformanceList(sessionSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - SQL History */
	@RequestMapping(value = "/SessionMonitoring/SQLHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLHistoryAction(@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory,
			Model model) throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			resultList = sqlAnalysisService.sqlHistoryList(activeSessionHistory);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - SQL Grid Plan */
	@RequestMapping(value = "/SessionMonitoring/SQLGridPlanList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLGridPlanListAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid, Model model) throws Exception {
		
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try {
			resultList = sqlAnalysisService.sqlGridPlanList(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}

	/* ?????????????????? - SQL Text Plan */
	@RequestMapping(value = "/SessionMonitoring/SQLTextPlanList", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLTextPlanListAction(@ModelAttribute("session") Session session, Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setObject(sqlAnalysisService.sqlTextPlanList(session));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ?????????????????? - session kill script */
	@RequestMapping(value = "/SessionMonitoring/SessionKillScript", method = RequestMethod.POST)
	@ResponseBody
	public Result SessionKillScriptAction(@ModelAttribute("session") Session session, Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setTxtValue(sqlAnalysisService.sessionKillScript(session));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ?????????????????? - process kill script */
	@RequestMapping(value = "/SessionMonitoring/ProcessKillScript", method = RequestMethod.POST)
	@ResponseBody
	public Result ProcessKillScriptAction(@ModelAttribute("session") Session session, Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setTxtValue(sqlAnalysisService.processKillScript(session));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ?????????????????? - Connect Info */
	@RequestMapping(value = "/SessionMonitoring/ConnectInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ConnectInfoAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionConnectInfo> resultList = new ArrayList<SessionConnectInfo>();

		try {
			resultList = sqlAnalysisService.connectInfoList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Event */
	@RequestMapping(value = "/SessionMonitoring/Event", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String EventAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionEvent> resultList = new ArrayList<SessionEvent>();

		try {
			resultList = sqlAnalysisService.eventList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Wait */
	@RequestMapping(value = "/SessionMonitoring/Wait", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String WaitAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionWait> resultList = new ArrayList<SessionWait>();

		try {
			resultList = sqlAnalysisService.waitList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Wait Class */
	@RequestMapping(value = "/SessionMonitoring/WaitClass", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String WaitClassAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionWaitClass> resultList = new ArrayList<SessionWaitClass>();

		try {
			resultList = sqlAnalysisService.waitClassList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - WaitHistory */
	@RequestMapping(value = "/SessionMonitoring/WaitHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String WaitHistoryAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionWait> resultList = new ArrayList<SessionWait>();

		try {
			resultList = sqlAnalysisService.waitHistoryList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Metric */
	@RequestMapping(value = "/SessionMonitoring/Metric", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MetricAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessMetric> resultList = new ArrayList<SessMetric>();

		try {
			resultList = sqlAnalysisService.metricList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - TimeModel */
	@RequestMapping(value = "/SessionMonitoring/TimeModel", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TimeModelAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessTimeModel> resultList = new ArrayList<SessTimeModel>();

		try {
			resultList = sqlAnalysisService.timeModelList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Statistics */
	@RequestMapping(value = "/SessionMonitoring/Statistics", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String StatisticsAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionWaitClass> resultList = new ArrayList<SessionWaitClass>();

		try {
			resultList = sqlAnalysisService.statisticsList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - IO */
	@RequestMapping(value = "/SessionMonitoring/IO", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String IOAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessIO> resultList = new ArrayList<SessIO>();

		try {
			resultList = sqlAnalysisService.ioList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - Long OPS */
	@RequestMapping(value = "/SessionMonitoring/LongOPS", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LongOPSAction(@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<SessionLongops> resultList = new ArrayList<SessionLongops>();

		try {
			resultList = sqlAnalysisService.longOPSList(session);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - excel down action */
	@RequestMapping(value = "/SessionMonitoring/ExcelDown", method = RequestMethod.POST)
	public void SessionMonitoringExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("session") Session session, Model model) throws Exception {
		
		List<Session> resultList = new ArrayList<Session>();
		WriteOption wo = new WriteOption();

		wo.setFileName("??????????????????");
		wo.setSheetName("??????????????????");
		wo.setTitle("SessionMonitoring");

		try {
			resultList = sqlAnalysisService.sessionMonitoringList(session);
			wo.setSessionMonitoringContents(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}

		ExcelWrite.write(res, wo);
	}

	/* ?????????????????? */
	@RequestMapping(value = "/ModulePerformance", method = RequestMethod.GET)
	public String ModulePerformance(@ModelAttribute("module") Module module, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String nowTime = DateUtil.getNowFulltime();
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String startTime = DateUtil.getNextTime("M", "10");

		model.addAttribute("startDate", startDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nowTime", nowTime);
		model.addAttribute("menu_id", module.getMenu_id());
		model.addAttribute("menu_nm", module.getMenu_nm());

		return "sqlAnalysis/modulePerformance";
	}

	/* ?????????????????? - list action */
	@RequestMapping(value = "/ModulePerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ModulePerformanceAction(@ModelAttribute("module") Module module, Model model) throws Exception {
		
		List<Module> resultList = new ArrayList<Module>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlAnalysisService.modulePerformanceList(module);
			logger.debug("resultList.size:"+resultList.size());
			if (resultList != null && resultList.size() > module.getRcount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(module.getRcount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
//		return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
		
	}

	/* ?????????????????? - detail list action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ModulePerformance/DtlModulePerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DtlModulePerformanceAction(@ModelAttribute("dbaHistSqlstat") DbaHistSqlstat dbaHistSqlstat,
			Model model) throws Exception {
		
		List<DbaHistSqlstat> resultList = new ArrayList<DbaHistSqlstat>();
		int dataCount4NextBtn = 0;
		try {
			resultList = sqlAnalysisService.dtlModulePerformanceList(dbaHistSqlstat);
			logger.debug("resultList.size:"+resultList.size());
			if (resultList != null && resultList.size() > dbaHistSqlstat.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(dbaHistSqlstat.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* ?????????????????? - Top SQL Stat Chart action */
	@RequestMapping(value = "/ModulePerformance/TOPStatChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TOPStatChartAction(@ModelAttribute("module") Module module, Model model) throws Exception {
		
		List<Module> resultList = new ArrayList<Module>();

		try {
			resultList = sqlAnalysisService.topSQLStatChartList(module);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ?????????????????? - master excel down action */
	// @RequestMapping(value = "/ModulePerformance/ExcelDown", method =
	// RequestMethod.POST)
	// public void ModulePerformanceExcelDown(HttpServletRequest req,
	// HttpServletResponse res,
	// @ModelAttribute("module") Module module, Model model)
	// throws UnsupportedEncodingException, IllegalArgumentException,
	// IllegalAccessException {
	// List<Module> resultList = new ArrayList<Module>();
	// WriteOption wo = new WriteOption();
	//
	// wo.setFileName("??????????????????_?????????");
	// wo.setSheetName("??????????????????");
	// wo.setTitle("ModulePerformance");
	//
	// try {
	// resultList = sqlAnalysisService.modulePerformanceList(module);
	// wo.setModulePerformanceContents(resultList);
	// } catch (Exception ex) {
	// String methodName = new Object()
	// {}.getClass().getEnclosingMethod().getName();
	// logger.error(methodName+" ???????????? ==> " + ex.getMessage());
	// }
	//
	// ExcelWrite.write(res, wo);
	// }

	@RequestMapping(value = "/ModulePerformance/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView ModulePerformanceExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("module") Module module, Model model) throws Exception {

		List<Module> resultList = new ArrayList<Module>();
		List<LinkedHashMap<String, String>> resultMapList = new ArrayList<LinkedHashMap<String, String>>();
		
		try {
//			resultList = sqlAnalysisService.modulePerformanceList4Excel(module);
			module.setRcount(0);
			module.setCurrentPage2(0);
			resultList = sqlAnalysisService.modulePerformanceList(module);
			LinkedHashMap<String,String> map = null;
			for(Module obj : resultList) {
				map = new LinkedHashMap<String,String>();
				map.put("MODULE",obj.getModule());
				map.put("ACTION",obj.getAction());
				map.put("SQL_ID",obj.getSql_id());
				map.put("PLAN_HASH_VALUE",obj.getPlan_hash_value());
				map.put("PARSING_SCHEMA_NAME",obj.getParsing_schema_name());
				map.put("ELAPSED_TIME",obj.getMax_elapsed_time());
				map.put("CPU_TIME",obj.getAvg_cpu_time());
				map.put("BUFFER_GETS",obj.getAvg_buffer_gets());
				map.put("ROWS_PROCESSED",obj.getAvg_row_processed());
				map.put("EXECUTIONS",obj.getExecutions());
				map.put("MAX_ELAPSED_TIME",obj.getMax_elapsed_time());
				map.put("AVG_ORDS",obj.getAvg_phyiscal_reads());
				map.put("LAST_EXEC_TIME",obj.getLast_exec_time());
				map.put("SQL_TEXT",obj.getSql_text());
				resultMapList.add(map);
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "??????????????????_?????????");
		model.addAttribute("sheetName", "??????????????????");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultMapList);
	}

	/* ?????????????????? - detail excel down action */
	// @RequestMapping(value = "/ModulePerformance/DtlExcelDown", method =
	// RequestMethod.POST)
	// public void ModulePerformanceDtlExcelDown(HttpServletRequest req,
	// HttpServletResponse res,
	// @ModelAttribute("dbaHistSqlstat") DbaHistSqlstat dbaHistSqlstat, Model
	// model)
	// throws UnsupportedEncodingException, IllegalArgumentException,
	// IllegalAccessException {
	// List<DbaHistSqlstat> resultList = new ArrayList<DbaHistSqlstat>();
	// WriteOption wo = new WriteOption();
	//
	// wo.setFileName("??????????????????_??????");
	// wo.setSheetName("??????????????????");
	// wo.setTitle("ModulePerformanceDtl");
	//
	// try {
	// resultList = sqlAnalysisService.dtlModulePerformanceList(dbaHistSqlstat);
	// wo.setDtlModulePerformanceContents(resultList);
	// } catch (Exception ex) {
	// String methodName = new Object()
	// {}.getClass().getEnclosingMethod().getName();
	// logger.error(methodName+" ???????????? ==> " + ex.getMessage());
	// }
	//
	// ExcelWrite.write(res, wo);
	// }

	/**
	 * Handle request to download an Excel document SQL???????????? - excel down action
	 */
	// @ResponseBody
	@RequestMapping(value = "/ModulePerformance/DtlExcelDown", method = RequestMethod.POST)
	public ModelAndView ModulePerformanceDtlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("dbaHistSqlstat") DbaHistSqlstat dbaHistSqlstat, Model model) throws Exception {

		List<DbaHistSqlstat> resultList = new ArrayList<DbaHistSqlstat>();
		List<LinkedHashMap<String, String>> resultMapList = new ArrayList<LinkedHashMap<String, String>>();

		try {
			dbaHistSqlstat.setRcount(0);
			dbaHistSqlstat.setCurrentPage(0);
			resultList = sqlAnalysisService.dtlModulePerformanceList(dbaHistSqlstat);
			LinkedHashMap<String,String> map = null;
		
			for(DbaHistSqlstat obj : resultList) {
				map = new LinkedHashMap<String,String>();
				map.put("begin_interval_time", obj.getBegin_interval_time());
				map.put("snap_id", obj.getSnap_id());
				map.put("instance_number", obj.getInstance_number());
				map.put("sql_id", obj.getSql_id());
				map.put("plan_hash_value", obj.getPlan_hash_value());
				map.put("parsing_schema_name", obj.getParsing_schema_name());
				map.put("elapsed_time", obj.getElapsed_time());
				map.put("cpu_time", obj.getCpu_time());
				map.put("buffer_gets", obj.getBuffer_gets());
				map.put("executions", obj.getExecutions());
				map.put("disk_reads", obj.getDisk_reads());
				map.put("rows_processed", obj.getRows_processed());
				map.put("clwait_time", obj.getClwait_rate());
				map.put("iowait_time", obj.getIowait_time());
				map.put("apwait_time", obj.getApwait_time());
				map.put("ccwait_time", obj.getCcwait_time());
				map.put("cpu_rate", obj.getCpu_rate());
				map.put("clwait_rate", obj.getClwait_rate());
				map.put("iowait_rate", obj.getIowait_rate());
				map.put("apwait_rate", obj.getApwait_rate());
				map.put("ccwait_rate", obj.getCcwait_rate());
				map.put("parse_calls", obj.getParse_calls());
				map.put("module", obj.getModule());
				map.put("fetches", obj.getFetches());
				map.put("optimizer_env_hash_value", obj.getOptimizer_env_hash_value());
				resultMapList.add(map);
			}

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "??????????????????_??????");
		model.addAttribute("sheetName", "????????????????????????");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultMapList);
	}

	/* SQL???????????? */
	@RequestMapping(value = "/SQLPerformance")
	public String SQLPerformance(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		
		String dbid = StringUtils.defaultString(odsHistSqlText.getDbid());
		String sql_id = StringUtils.defaultString(odsHistSqlText.getSql_id());
		String gather_day = StringUtils.defaultString(odsHistSqlText.getGather_day());
		String plan_hash_value = StringUtils.defaultString(odsHistSqlText.getPlan_hash_value());

		if(!dbid.equals("") && !sql_id.equals("") && !gather_day.equals("") && plan_hash_value.equals("")){
			plan_hash_value = sqlAnalysisService.getPlanHashValue(odsHistSqlText);
			logger.debug("#### plan_hash_value :::: " + plan_hash_value);
		}
		
		String enableFunc = "FALSE";
		String authCd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		if(authCd.equalsIgnoreCase("ROLE_DBMANAGER") || authCd.equalsIgnoreCase("ROLE_OPENPOPMANAGER"))  {
			enableFunc = "TRUE";
		}
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", odsHistSqlText.getMenu_id());
		model.addAttribute("menu_nm", odsHistSqlText.getMenu_nm());
		model.addAttribute("plan_hash_value", plan_hash_value);
		model.addAttribute("sql_id", sql_id);
		model.addAttribute("dbid", dbid);
		model.addAttribute("call_from_parent", odsHistSqlText.getCall_from_parent());
		model.addAttribute("enable_func", enableFunc);
		
		return "sqlAnalysis/sqlPerformance";
	}

	/* SQL???????????? - SQL TEXT */
	@RequestMapping(value = "/SQLPerformance/SQLText", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLTextAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setObject(sqlAnalysisService.sqlText(odsHistSqlText));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	/* ???????????? - SQL BIND */
	@RequestMapping(value = "/SQLPerformance/SQLBind", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLBindAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, 
			@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,
			Model model) throws Exception {
		Result result = new Result();
		
		try {

			result.setResult(true);
			result.setObject(sqlAnalysisService.SQLBind(odsHistSqlstat));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}

	/* SQL???????????? - SQL Tree */
	@RequestMapping(value = "/SQLPerformance/SQLPerformTreePlanList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformTreePlanListAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid, Model model)
			throws Exception {
		
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try {
			resultList = sqlAnalysisService.sqlPerformTreePlanList(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSQLTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}

	/* SQL???????????? - Text Plan */
	@RequestMapping(value = "/SQLPerformance/SQLPerformTextPlanList", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLPerformTextPlanListAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText,
			Model model) throws Exception {

		Result result = new Result();

		try {
			result.setResult(true);
			result.setObject(sqlAnalysisService.sqlPerformTextPlanList(odsHistSqlText));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* SQL???????????? - SQL Grid Plan */
	@RequestMapping(value = "/SQLPerformance/SQLPerformGridPlanList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformGridPlanListAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid, Model model)
			throws Exception {
		
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try {
			resultList = sqlAnalysisService.sqlPerformGridPlanList(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}

	/* SQL???????????? - Bind Value */
	@RequestMapping(value = "/SQLPerformance/BindValueList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String BindValueListAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {
		
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try {
			resultList = sqlAnalysisService.bindValueList(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* SQL???????????? - Bind Value Next action */
	@RequestMapping(value = "/SQLPerformance/BindValueListNext", method = RequestMethod.POST)
	@ResponseBody
	public Result BindValueListNextAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {

		Result result = new Result();
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try {
			resultList = sqlAnalysisService.bindValueList(odsHistSqlstat);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* SQL???????????? - Out Line */
	@RequestMapping(value = "/SQLPerformance/OutLineList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String OutLineListAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {
		
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try {
			resultList = sqlAnalysisService.outLineList(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* SQL???????????? - ?????? SQL */
	@RequestMapping(value = "/SQLPerformance/SimilaritySqlList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SimilaritySqlListAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {
		
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try {
			resultList = sqlAnalysisService.similaritySqlList(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* SQL???????????? - sql ???????????? action */
	@RequestMapping(value = "/SQLPerformance/SQLPerformHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformHistoryAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
			throws Exception {
		
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		int dataCount4NextBtn = 0;

		try {
			odsHistSqlstat.setPagePerCount(10);
			resultList = sqlAnalysisService.sqlPerformHistoryList(odsHistSqlstat);
			logger.debug("resultList.size:"+resultList.size());

/*			for(int i =0; i < 11; i++){
				resultList.add(resultList.get(i));
			}*/
			
			if (resultList != null && resultList.size() > odsHistSqlstat.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsHistSqlstat.getPagePerCount());
			}		
			} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}

	
	/* SQL???????????? - sql ???????????? Next action */
	@RequestMapping(value = "/SQLPerformance/SQLPerformHistoryNext", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLPerformHistoryNextAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,
			Model model) throws Exception {

		Result result = new Result();
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try {
			resultList = sqlAnalysisService.sqlPerformHistoryList(odsHistSqlstat);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	// /* SQL???????????? - excel down action */
	// @RequestMapping(value="/SQLPerformance/SQLPerformHistory/ExcelDown1",
	// method=RequestMethod.POST)
	// public void SQLPerformHistoryExcelDown1(HttpServletRequest req,
	// HttpServletResponse res, @ModelAttribute("odsHistSqlstat") OdsHistSqlstat
	// odsHistSqlstat, Model model) throws UnsupportedEncodingException,
	// IllegalArgumentException, IllegalAccessException {
	// List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
	// WriteOption wo = new WriteOption();
	//
	// wo.setFileName("SQL????????????");
	// wo.setSheetName("AWR_SQL_Stats");
	// wo.setTitle("SQLPerformHistory");
	//
	// try{
	// resultList = sqlAnalysisService.sqlPerformHistoryList(odsHistSqlstat);
	// wo.setSQLPerformHistoryContents(resultList);
	// } catch (Exception ex){
	// String methodName = new Object()
	// {}.getClass().getEnclosingMethod().getName();
	// logger.error(methodName+" ???????????? ==> " + ex.getMessage());
	// }
	//
	// ExcelWrite.write(res, wo);
	// }

	/* SQL???????????? - sql Plan??? ???????????? action */
	@RequestMapping(value = "/SQLPerformance/SQLPerformPlanHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformPlanHistoryAction(@ModelAttribute("sqlStatByPlan") SqlStatByPlan sqlStatByPlan, Model model)
			throws Exception {
		
		List<SqlStatByPlan> resultList = new ArrayList<SqlStatByPlan>();

		try {
			resultList = sqlAnalysisService.sqlPerformPlanHistoryList(sqlStatByPlan);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* SQL???????????? - AWR Execution Plan action */
	@RequestMapping(value = "/SQLPerformance/AWRExecutionPlan", method = RequestMethod.POST)
	@ResponseBody
	public Result AWRExecutionPlanAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {

		Result result = new Result();
		List<String> resultList = new ArrayList<String>();

		try {
			resultList = sqlAnalysisService.awrExecutionPlanList(odsHistSqlText);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setStringList(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* SQL???????????? - sql Plan??? excel down action */
	@RequestMapping(value = "/SQLPerformance/SQLPerformPlanHistory/ExcelDown", method = RequestMethod.POST)
	public void SQLPerformPlanHistoryExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStatByPlan") SqlStatByPlan sqlStatByPlan, Model model) throws Exception {

		List<SqlStatByPlan> resultList = new ArrayList<SqlStatByPlan>();
		WriteOption wo = new WriteOption();

		wo.setFileName("SQL????????????");
		wo.setSheetName("PLAN???_SQL_Stats");
		wo.setTitle("SQLPerformPlanHistory");

		try {
			resultList = sqlAnalysisService.sqlPerformPlanHistoryList(sqlStatByPlan);
			wo.setSQLPerformPlanHistoryContents(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}

		ExcelWrite.write(res, wo);
	}

	/* SQL???????????? - SQL Tuning Advisor */
	@RequestMapping(value = "/SQLPerformance/SQLTuningAdvisor", method = RequestMethod.GET)
	public String SQLTuningAdvisor(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", odsHistSqlText.getMenu_id());
		model.addAttribute("menu_nm", odsHistSqlText.getMenu_nm());
		model.addAttribute("call_from_parent", odsHistSqlText.getCall_from_parent());

		return "sqlAnalysis/sqlPerformance/sqlTuningAdvisor";
	}

	/* SQL???????????? - SQL Tuning Advisor action */
	@RequestMapping(value = "/SQLPerformance/SQLTuningAdvisor", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLTuningAdvisorAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {

		Result result = new Result();
		List<String> resultList = new ArrayList<String>();

		try {
			resultList = sqlAnalysisService.sqlTuningAdvisorList(odsHistSqlText);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setStringList(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* SQL???????????? - SQL Access Advisor */
	@RequestMapping(value = "/SQLPerformance/SQLAccessAdvisor", method = RequestMethod.GET)
	public String SQLAccessAdvisor(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", odsHistSqlText.getMenu_id());
		model.addAttribute("menu_nm", odsHistSqlText.getMenu_nm());
		model.addAttribute("call_from_parent", odsHistSqlText.getCall_from_parent());

		return "sqlAnalysis/sqlPerformance/sqlAccessAdvisor";
	}

	/* SQL???????????? - SQL Access Advisor action */
	@RequestMapping(value = "/SQLPerformance/SQLAccessAdvisor", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLAccessAdvisorAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {

		Result result = new Result();
		List<String> resultList = new ArrayList<String>();

		try {
			resultList = sqlAnalysisService.sqlAccessAdvisorList(odsHistSqlText);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setStringList(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* SQL???????????? - SQL Monitor */
	@RequestMapping(value = "/SQLPerformance/SQLMonitor", method = RequestMethod.GET)
	public String SQLMonitor(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", odsHistSqlText.getMenu_id());
		model.addAttribute("menu_nm", odsHistSqlText.getMenu_nm());
//		model.addAttribute("call_from_parent", odsHistSqlText.getCall_from_parent());

		return "sqlAnalysis/sqlPerformance/sqlMonitor";
	}

	/* SQL???????????? - SQL Monitor action */
	@RequestMapping(value = "/SQLPerformance/SQLMonitor", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLMonitorAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {

		Result result = new Result();
		List<String> resultList = new ArrayList<String>();

		try {
			resultList = sqlAnalysisService.sqlMonitorList(odsHistSqlText);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setStringList(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ASH???????????? */
	@RequestMapping(value = "/ASHPerformance", method = RequestMethod.GET)
	public String ASHPerformance(@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory,
			Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String nowTime = DateUtil.getNowFulltime();
		String startTime = DateUtil.getNextTime("M", "10");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("nowTime", nowTime);
		model.addAttribute("menu_id", activeSessionHistory.getMenu_id());
		model.addAttribute("menu_nm", activeSessionHistory.getMenu_nm());

		return "sqlAnalysis/ashPerformance";
	}

	/* ASH???????????? - all session action */
	@RequestMapping(value = "/ASHPerformance/AllSession", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ASHPerformanceAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getDbid() != null) {
				resultList = sqlAnalysisService.allSessionList(activeSessionHistory);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ASH???????????? - all session list next */
	@RequestMapping(value = "/ASHPerformance/AllSessionNext", method = RequestMethod.POST)
	@ResponseBody
	public Result ASHPerformanceNextAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		Result result = new Result();
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getDbid() != null) {
				resultList = sqlAnalysisService.allSessionList(activeSessionHistory);
				result.setResult(resultList.size() > 0 ? true : false);
				result.setObject(resultList);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ASH???????????? - Wait Class ?????? ?????? action */
	@RequestMapping(value = "/ASHPerformance/WaitClassChartLegend", method = RequestMethod.POST)
	@ResponseBody
	public Result WaitClassChartLegendAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		Result result = new Result();

		try {
			result = sqlAnalysisService.waitClassChartLegend(activeSessionHistory);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ASH???????????? - Wait Class ?????? ?????? action */
	@RequestMapping(value = "/ASHPerformance/WaitClassChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ASHWaitClassChartAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {
		
		String strResult = "";
		try {
			strResult = sqlAnalysisService.ashWaitClassChart(activeSessionHistory);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return strResult;
	}

	/* ASH???????????? - Top Wait Event ?????? ?????? action */
	@RequestMapping(value = "/ASHPerformance/TopWaitEventChartLegend", method = RequestMethod.POST)
	@ResponseBody
	public Result TopWaitEventChartLegendAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		Result result = new Result();

		try {
			result = sqlAnalysisService.topWaitEventChartLegend(activeSessionHistory);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* ASH???????????? - Top Wait Event ?????? ?????? action */
	@RequestMapping(value = "/ASHPerformance/TopWaitEventChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TopWaitEventChartAction(
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {
		
		String strResult = "";

		try {
			strResult = sqlAnalysisService.topWaitEventChart(activeSessionHistory);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return strResult;
	}

	/* ASH???????????? - top sql action */
	@RequestMapping(value = "/ASHPerformance/TopSql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TopSqlAction(@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory,
			Model model) throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getDbid() != null) {
				resultList = sqlAnalysisService.topSqlList(activeSessionHistory);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ASH???????????? - top session action */
	@RequestMapping(value = "/ASHPerformance/TopSession", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TopSessionAction(@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory,
			Model model) throws Exception {
		
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getDbid() != null) {
				resultList = sqlAnalysisService.topSessionList(activeSessionHistory);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* ASH???????????? - All Session exceldown action */
	@RequestMapping(value = "/ASHPerformance/AllSession/ExcelDown", method = RequestMethod.POST)
	public void AllSessionExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		WriteOption wo = new WriteOption();

		wo.setFileName("ASH????????????_AllSession");
		wo.setSheetName("AllSession");
		wo.setTitle("ASHPerformance");

		try {
			resultList = sqlAnalysisService.allSessionList(activeSessionHistory);
			wo.setASHPerformanceContents(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}

		ExcelWrite.write(res, wo);
	}

	/* ASH???????????? - Top Sql exceldown action */
	@RequestMapping(value = "/ASHPerformance/TopSql/ExcelDown", method = RequestMethod.POST)
	public void TopSqlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		WriteOption wo = new WriteOption();

		wo.setFileName("ASH????????????_TOPSQL");
		wo.setSheetName("TOPSQL");
		wo.setTitle("TopSql");

		try {
			resultList = sqlAnalysisService.topSqlList(activeSessionHistory);
			wo.setTopSqlContents(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}

		ExcelWrite.write(res, wo);
	}

	/* ASH???????????? - Top Session exceldown action */
	@RequestMapping(value = "/ASHPerformance/TopSession/ExcelDown", method = RequestMethod.POST)
	public void TopSessionExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("activeSessionHistory") ActiveSessionHistory activeSessionHistory, Model model)
			throws Exception {

		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		WriteOption wo = new WriteOption();

		wo.setFileName("ASH????????????_TOPSESSION");
		wo.setSheetName("TOPSession");
		wo.setTitle("TopSession");

		try {
			resultList = sqlAnalysisService.topSessionList(activeSessionHistory);
			wo.setTopSessionContents(resultList);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}

		ExcelWrite.write(res, wo);
	}
	
	/* ?????????/????????? ?????? SQL ?????? */
	@RequestMapping(value = "/TableIndexUseSQL", method = RequestMethod.GET)
	public String TableIndexUseSQL2(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", tableUseSql.getMenu_id());
		model.addAttribute("menu_nm", tableUseSql.getMenu_nm());

		return "sqlAnalysis/tableIndexUseSQL_variable";
	}
	
	/* ????????? ?????? SQL ?????? */
	@RequestMapping(value = "/TableUseSQL", method = RequestMethod.GET)
	public String TableUseSQL(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", tableUseSql.getMenu_id());
		model.addAttribute("menu_nm", tableUseSql.getMenu_nm());

		return "sqlAnalysis/tableUseSQL";
	}

	/* ????????? ?????? SQL ?????? action */
	@RequestMapping(value = "/TableUseSQL", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TableUseSQLAction(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model)
			throws Exception {
		
		List<TableUseSql> resultList = new ArrayList<TableUseSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlAnalysisService.tableUseSqlList(tableUseSql);
			if(resultList != null && resultList.size() > tableUseSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tableUseSql.getPagePerCount());
				/*???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ??????????????????*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* ????????? ?????? SQL ?????? */
	@RequestMapping(value = "/IndexUseSQL", method = RequestMethod.GET)
	public String IndexUseSQL(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", tableUseSql.getMenu_id());
		model.addAttribute("menu_nm", tableUseSql.getMenu_nm());

		return "sqlAnalysis/indexUseSQL";
	}

	/* ????????? ?????? SQL ?????? action */
	@RequestMapping(value = "/IndexUseSQL", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String IndexUseSQLAction(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model)
			throws Exception {
		
		List<TableUseSql> resultList = new ArrayList<TableUseSql>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlAnalysisService.indexUseSqlList(tableUseSql);
			if(resultList != null && resultList.size() > tableUseSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tableUseSql.getPagePerCount());
				/*???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ??????????????????*/
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* ???????????? */
	@RequestMapping(value = "/getDate", method = RequestMethod.GET)
	@ResponseBody
	public String getDate() throws Exception {
		
		String toDate = DateUtil.getNowDate("yyyyMMddHHmmssSSS");
		return toDate;
	}

	/**
	 * Handle request to download an Excel document ????????? ?????? SQL ?????? action
	 */
	@RequestMapping(value = "/TableUseSQL/downloadExcel", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView TableUseSQLDownloadExcel(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model)
			throws Exception {

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			resultList = sqlAnalysisService.tableUseSqlList4Excel(tableUseSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("sheetName", "TableUseSQL");
		model.addAttribute("excelId", "TABLE_USE_SQL_ANALYSIS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/**
	 * Handle request to download an Excel document Index ?????? SQL ?????? action
	 */
	@RequestMapping(value = "/IndexUseSQL/downloadExcel", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView IndexUseSQLDownloadExcel(@ModelAttribute("tableUseSql") TableUseSql tableUseSql, Model model)
			throws Exception {

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			resultList = sqlAnalysisService.indexUseSqlList4Excel(tableUseSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("sheetName", "IndexUseSQL");
		model.addAttribute("excelId", "INDEX_USE_SQL_ANALYSIS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/**
	 * Handle request to download an Excel document SQL???????????? - excel down action
	 */
	@RequestMapping(value = "/SQLPerformance/SQLPerformHistory/ExcelDown", method = { RequestMethod.GET,
			RequestMethod.POST })
	// @ResponseBody
	public ModelAndView SQLPerformHistoryExcelDown(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,
			Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = sqlAnalysisService.sqlPerformHistoryList4Excel(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL????????????");
		model.addAttribute("sheetName", "AWR_SQL_Stats");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
//
//	private String getErrorJsonString(Exception ex){
//		return "{\"result\":false, \"message\":\""+ex.getMessage()+"\"}";
//	}
}