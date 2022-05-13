package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SQLAnalysisDao;
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
import omc.spop.server.perfmon.PerfMonAshReal;
import omc.spop.server.perfmon.PerfMonModule;
import omc.spop.server.perfmon.PerfMonSess;
import omc.spop.server.perfmon.PerfMonSqlAna;
import omc.spop.service.SQLAnalysisService;
import omc.spop.utils.CollectionSortDesc;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.13 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLAnalysisService")
public class SQLAnalysisServiceImpl implements SQLAnalysisService {
	private static final Logger logger = LoggerFactory.getLogger(SQLAnalysisServiceImpl.class);
	@Autowired
	private SQLAnalysisDao sqlAnalysisDao;

	@Override
	public List<ApmApplSql> appPerformanceList(ApmApplSql apmApplSql) throws Exception {
		return sqlAnalysisDao.appPerformanceList(apmApplSql);
	}

	@Override
	public List<OdsApmSAppl> appDailyList(OdsApmSAppl odsApmSAppl) throws Exception {
		return sqlAnalysisDao.appDailyList(odsApmSAppl);
	}

	@Override
	public List<OdsApmSAppl> appTimeList(OdsApmSAppl odsApmSAppl) throws Exception {
		return sqlAnalysisDao.appTimeList(odsApmSAppl);
	}

	@Override
	public List<OdsApmSSqls> dbioDailyList(OdsApmSSqls odsApmSSqls) throws Exception {
		return sqlAnalysisDao.dbioDailyList(odsApmSSqls);
	}

	@Override
	public List<OdsApmSSqls> dbioTimeList(OdsApmSSqls odsApmSSqls) throws Exception {
		return sqlAnalysisDao.dbioTimeList(odsApmSSqls);
	}

	@Override
	public List<OdsHistSqlstat> dbioHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlAnalysisDao.dbioHistoryList(odsHistSqlstat);
	}

	@Override
	public List<Session> sessionMonitoringList(Session session) throws Exception {
		List<Session> resultList = new ArrayList<Session>();
		try {
			// Long DBID, int INST_ID, String SQL_ID, String PROGRAM, String
			// MODULE, String EVENT, String MACHINE, String OSUSER, String
			// PARALLEL, String STATUS, String EXCEPT_BACK
			resultList = PerfMonSess.getSessionList(Long.parseLong(session.getDbid()),
					StringUtil.parseInt(session.getInst_id(), 0), session.getSql_id(), session.getProgram(),
					session.getModule(), session.getEvent(), session.getMachine(), session.getOsuser(),
					session.getParallel(), session.getStatus(), session.getType());

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<ActiveSessionHistory> sessionWaitClassChartList(Session session) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			// Long DBID, int INST_ID, String SQL_ID, String PROGRAM, String
			// MODULE, String EVENT, String MACHINE, String OSUSER, String
			// PARALLEL, String STATUS, String EXCEPT_BACK
			resultList = PerfMonSess.getAllSessionWaitClassStat(Long.parseLong(session.getDbid()),
					StringUtil.parseInt(session.getInst_id(), 0), session.getSql_id(), session.getProgram(),
					session.getModule(), session.getEvent(), session.getMachine(), session.getOsuser(),
					session.getParallel(), session.getStatus(), session.getType());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<ActiveSessionHistory> topEventChartList(Session session) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			// Long DBID, int INST_ID, String SQL_ID, String PROGRAM, String
			// MODULE, String EVENT, String MACHINE, String OSUSER, String
			// PARALLEL, String STATUS, String EXCEPT_BACK
			resultList = PerfMonSess.getAllSessionWaitEventStat(Long.parseLong(session.getDbid()),
					StringUtil.parseInt(session.getInst_id(), 0), session.getSql_id(), session.getProgram(),
					session.getModule(), session.getEvent(), session.getMachine(), session.getOsuser(),
					session.getParallel(), session.getStatus(), session.getType());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<omc.spop.vo.server.Module> topModuleChartList(Session session) throws Exception {
		List<omc.spop.vo.server.Module> resultList = new ArrayList<omc.spop.vo.server.Module>();
		try {
			// Long DBID, int INST_ID, String SQL_ID, String PROGRAM, String
			// MODULE, String EVENT, String MACHINE, String OSUSER, String
			// PARALLEL, String STATUS, String EXCEPT_BACK
			resultList = PerfMonSess.getAllSessionModuleStat(Long.parseLong(session.getDbid()),
					StringUtil.parseInt(session.getInst_id(), 0), session.getSql_id(), session.getProgram(),
					session.getModule(), session.getEvent(), session.getMachine(), session.getOsuser(),
					session.getParallel(), session.getStatus(), session.getType());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public String sessionKill(Session session) throws Exception {
		String result = "";
		try {
			// Long DBID, int INST_ID, Long SID, Long SERIAL_NO
			result = PerfMonSess.doSessionKill(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public String lastCursor(Session session) throws Exception {
		String fullText = "";
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			fullText = PerfMonSess.Search_Last_Cursor(StringUtil.parseLong(session.getDbid(), 0), "NO", "NO", "ALL",
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.nvl(session.getSql_id()), null, null, null,
					null, null);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return fullText;
	}

	@Override
	public List<Session> allCursorList(Session session) throws Exception {
		List<Session> resultList = new ArrayList<Session>();
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			resultList = PerfMonSess.Search_All_Cursor(StringUtil.parseLong(session.getDbid(), 0), "NO", "NO", "ALL",
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.nvl(session.getSql_id()),
					StringUtil.parseLong(session.getSid(), 0), StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionSql> sqlPerformanceList(SessionSql sessionSql) throws Exception {
		List<SessionSql> resultList = new ArrayList<SessionSql>();
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			resultList = PerfMonSess.Search_Sql_Perf(StringUtil.parseLong(sessionSql.getDbid(), 0), "NO", "NO", "ALL",
					StringUtil.parseInt(sessionSql.getInst_id(), 0), StringUtil.nvl(sessionSql.getSql_id()));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<ActiveSessionHistory> sqlHistoryList(ActiveSessionHistory activeSessionHistory) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			resultList = PerfMonSess.Search_Sess_His(StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
					StringUtil.parseInt(activeSessionHistory.getInst_id(), 0),
					StringUtil.parseLong(activeSessionHistory.getSid(), 0),
					StringUtil.parseLong(activeSessionHistory.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SqlGrid> sqlGridPlanList(SqlGrid sqlGrid) throws Exception {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			resultList = PerfMonSess.Search_Grid_Plan(StringUtil.parseLong(sqlGrid.getDbid(), 0),
					StringUtil.parseInt(sqlGrid.getInst_id(), 0), StringUtil.nvl(sqlGrid.getSql_id()),
					StringUtil.parseLong(sqlGrid.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<Session> sqlTextPlanList(Session session) throws Exception {
		List<Session> resultList = new ArrayList<Session>();
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			resultList = PerfMonSess.Search_Text_Plan(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.nvl(session.getSql_id()),
					StringUtil.parseLong(session.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public String sessionKillScript(Session session) throws Exception {
		String result = "";
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			result = PerfMonSess.getSessionKillScript(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public String processKillScript(Session session) throws Exception {
		String result = "";
		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			result = PerfMonSess.Search_Program_Kill(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public List<SessionConnectInfo> connectInfoList(Session session) throws Exception {
		List<SessionConnectInfo> resultList = new ArrayList<SessionConnectInfo>();
		try {
			// Long DBID, int INST_ID, Long SID, Long SERIAL_NO
			resultList = PerfMonSess.getSessionConnectInfo(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionEvent> eventList(Session session) throws Exception {
		List<SessionEvent> resultList = new ArrayList<SessionEvent>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionEvent(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionWait> waitList(Session session) throws Exception {
		List<SessionWait> resultList = new ArrayList<SessionWait>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionWait(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionWaitClass> waitClassList(Session session) throws Exception {
		List<SessionWaitClass> resultList = new ArrayList<SessionWaitClass>();
		try {
			// Long DBID, int INST_ID, Long SID, Long SERIAL_NO
			resultList = PerfMonSess.getSessionWaitClass(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionWait> waitHistoryList(Session session) throws Exception {
		List<SessionWait> resultList = new ArrayList<SessionWait>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionWaitHistory(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessMetric> metricList(Session session) throws Exception {
		List<SessMetric> resultList = new ArrayList<SessMetric>();
		try {
			// Long DBID, int INST_ID, Long SID, Long SERIAL_NO
			resultList = PerfMonSess.getSessionMetric(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessTimeModel> timeModelList(Session session) throws Exception {
		List<SessTimeModel> resultList = new ArrayList<SessTimeModel>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionTimeModel(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionWaitClass> statisticsList(Session session) throws Exception {
		List<SessionWaitClass> resultList = new ArrayList<SessionWaitClass>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionStat(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessIO> ioList(Session session) throws Exception {
		List<SessIO> resultList = new ArrayList<SessIO>();
		try {
			// Long DBID, int INST_ID, Long SID
			resultList = PerfMonSess.getSessionIO(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SessionLongops> longOPSList(Session session) throws Exception {
		List<SessionLongops> resultList = new ArrayList<SessionLongops>();
		try {
			// Long DBID, int INST_ID, Long SID, Long SERIAL_NO
			resultList = PerfMonSess.getSessionLongOPS(StringUtil.parseLong(session.getDbid(), 0),
					StringUtil.parseInt(session.getInst_id(), 0), StringUtil.parseLong(session.getSid(), 0),
					StringUtil.parseLong(session.getSerial(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<Module> modulePerformanceList(Module module) throws Exception {
		List<Module> resultList = new ArrayList<Module>();
		try {
			// DBID, MODULE, START_DATE, END_DATE
			long dbid = StringUtil.parseLong(module.getDbid(), 0);
			String module1 = module.getModule();
			String action = module.getAction();
			String start_interval_time = module.getStart_interval_time();
			String end_interval_time = module.getEnd_interval_time();
			Long rcount = new Long(module.getRcount());
			long currentPage= (long) module.getCurrentPage2();

			logger.debug("##### dbid : " + dbid);
			logger.debug("##### module1 : " + module1);
			logger.debug("##### action : " + action);
			logger.debug("##### start_interval_time : " + start_interval_time);
			logger.debug("##### end_interval_time : " + end_interval_time);
			logger.debug("##### rcount : " + rcount);
			logger.debug("##### current_page : " + currentPage);
			resultList = PerfMonModule.getSqlList(dbid, module1, action, start_interval_time, end_interval_time,
					rcount, currentPage);

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> modulePerformanceList4Excel(Module module) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			// DBID, MODULE, START_DATE, END_DATE
			resultList = PerfMonModule.getSqlList4Excel(
					StringUtil.parseLong(module.getDbid(), 0)
					, module.getModule()
					, module.getStart_interval_time()
					, module.getEnd_interval_time()
					, new Long(module.getRcount()));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<DbaHistSqlstat> dtlModulePerformanceList(DbaHistSqlstat dbaHistSqlstat) throws Exception {
		List<DbaHistSqlstat> resultList = new ArrayList<DbaHistSqlstat>();
		try {
			// DBID, MODULE, START_DATE, END_DATE
			resultList = PerfMonModule.getSqlPerf(
					StringUtil.parseLong(dbaHistSqlstat.getDbid(), 0)
					,dbaHistSqlstat.getSql_id()
					,dbaHistSqlstat.getStart_interval_time()
					,dbaHistSqlstat.getEnd_interval_time()
					,new Long(dbaHistSqlstat.getPagePerCount())
					,new Long(dbaHistSqlstat.getCurrentPage())
					);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> dtlModulePerformanceList4Excel(DbaHistSqlstat dbaHistSqlstat)
			throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			// DBID, MODULE, START_DATE, END_DATE
			resultList = PerfMonModule.getSqlPerf4Excel(
					StringUtil.parseLong(dbaHistSqlstat.getDbid(), 0),
					dbaHistSqlstat.getSql_id()
					, dbaHistSqlstat.getStart_interval_time()
					, dbaHistSqlstat.getEnd_interval_time()
					, new Long(dbaHistSqlstat.getPagePerCount())
					, new Long(dbaHistSqlstat.getCurrentPage())
					);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<Module> topSQLStatChartList(Module module) throws Exception {
		List<Module> resultList = new ArrayList<Module>();
		try {
			// DBID, MODULE, START_DATE, END_DATE
			resultList = PerfMonModule.getModuleTopSQL(StringUtil.parseLong(module.getDbid(), 0), module.getModule(),
					module.getStart_interval_time(), module.getEnd_interval_time());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public OdsHistSqlText sqlText(OdsHistSqlText odsHistSqlText) throws Exception {
		OdsHistSqlText result = new OdsHistSqlText();
		try {
			// DBID, SQL_ID
			result = PerfMonSqlAna.getSearch(StringUtil.parseLong(odsHistSqlText.getDbid(), 0),
					odsHistSqlText.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public List<ApmApplSql> getSqlText1(ApmApplSql param) throws Exception {
		return sqlAnalysisDao.getSqlText1(param);
	}

	@Override
	public OdsHistSqlText getSqlText2(OdsHistSqlText param) {
		return sqlAnalysisDao.getSqlText2(param);
	}

	@Override
	public List<SqlGrid> sqlPerformTreePlanList(SqlGrid sqlGrid) throws Exception {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		try {
			resultList = PerfMonSqlAna.getTreePlan(StringUtil.parseLong(sqlGrid.getDbid(), 0), sqlGrid.getSql_id(),
					StringUtil.parseLong(sqlGrid.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<OdsHistSqlText> sqlPerformTextPlanList(OdsHistSqlText odsHistSqlText) throws Exception {
		List<OdsHistSqlText> resultList = new ArrayList<OdsHistSqlText>();
		try {
			resultList = PerfMonSqlAna.getTextPlan(StringUtil.parseLong(odsHistSqlText.getDbid(), 0),
					odsHistSqlText.getSql_id(), StringUtil.parseLong(odsHistSqlText.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SqlGrid> sqlPerformGridPlanList(SqlGrid sqlGrid) throws Exception {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		try {
			resultList = PerfMonSqlAna.getGridPlan(StringUtil.parseLong(sqlGrid.getDbid(), 0), sqlGrid.getSql_id(),
					StringUtil.parseLong(sqlGrid.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<OdsHistSqlstat> bindValueList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = PerfMonSqlAna.getBindVar(StringUtil.parseLong(odsHistSqlstat.getDbid(), 0),
					odsHistSqlstat.getSql_id(), odsHistSqlstat.getPagePerCount(), odsHistSqlstat.getCurrentPage());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		return resultList;
	}

	@Override
	public List<String> SQLBind(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> tempList = new ArrayList<OdsHistSqlstat>();
		List<OdsHistSqlstat> getFromDB = new ArrayList<OdsHistSqlstat>();

		try {
			getFromDB = PerfMonSqlAna.getBindVar(StringUtil.parseLong(odsHistSqlstat.getDbid(), 0),
					odsHistSqlstat.getSql_id(), odsHistSqlstat.getPagePerCount(), odsHistSqlstat.getCurrentPage());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		for (int i = 0; i < getFromDB.size(); i++) {

			HashSet<String> name_valueSet = new HashSet<String>();

			for (int j = 0; j < getFromDB.size(); j++) {
				// snap_id 가 같을 경우 하나로 묶음
				if (getFromDB.get(i).getSnap_id().equals(getFromDB.get(j).getSnap_id())
						&& getFromDB.get(i).getExec_time().equals(getFromDB.get(j).getExec_time())) {
					name_valueSet.add(StringUtils.defaultString(getFromDB.get(j).getName(), "0") + "-"
							+ StringUtils.defaultString(getFromDB.get(j).getValue(), "0"));
					getFromDB.get(i).setName_value(name_valueSet);
				}
			}

			// for(int j = 0; j < getFromDB.size(); j++){
			// //snap_id 가 같을 경우 하나로 묶음
			// if(getFromDB.get(i).getSnap_id().equals(getFromDB.get(j).getSnap_id())){
			// name_valueSet.add(StringUtils.defaultString(getFromDB.get(j).getName(),"0")+"_"+StringUtils.defaultString(getFromDB.get(j).getValue(),"0"));
			// getFromDB.get(i).setName_value(name_valueSet);
			// }
			// }

			tempList.add(new OdsHistSqlstat(getFromDB.get(i).getSnap_id(),
					StringUtils.defaultString(getFromDB.get(i).getExec_time(), "0"),
					StringUtils.defaultString(getFromDB.get(i).getElapsed_time(), "0"),
					StringUtils.defaultString(getFromDB.get(i).getBuffer_gets(), "0"),
					getFromDB.get(i).getName_value()));
		}

		Collections.sort(tempList, new CollectionSortDesc());
		List<String> resultList = new ArrayList<String>();

		if (tempList.size() >= 3) {

			for (int i = 0; i < 3; i++) {
				String strSnapIdAndExecTime = "[ SNAP_ID : " + tempList.get(i).getSnap_id() + ", EXECUTE_TIME : "
						+ tempList.get(i).getExec_time() + " ] ";
				String strElapsedTimeAndBufferGets = (i + 1) + ". 수행시간(Elapsed_Time) : "
						+ tempList.get(i).getElapsed_time() + "(초), Buffer Gets : " + tempList.get(i).getBuffer_gets();
				resultList.add(strSnapIdAndExecTime);
				resultList.add(strElapsedTimeAndBufferGets);
				resultList.add("-------------------------------------------------------------------------------");
				String strNameValue = "";

				Iterator<String> itr = tempList.get(i).getName_value().iterator();
				while (itr.hasNext()) {
					strNameValue += itr.next() + " \n";
				}
				resultList.add(strNameValue);
			}

		} else if (tempList.size() == 2) {

			for (int i = 0; i < 2; i++) {
				String strSnapIdAndExecTime = "[ SNAP_ID : " + tempList.get(i).getSnap_id() + ", EXECUTE_TIME : "
						+ tempList.get(i).getExec_time() + " ] ";
				String strElapsedTimeAndBufferGets = (i + 1) + ". 수행시간(Elapsed_Time) : "
						+ tempList.get(i).getElapsed_time() + "(초), Buffer Gets : " + tempList.get(i).getBuffer_gets();
				resultList.add(strSnapIdAndExecTime);
				resultList.add(strElapsedTimeAndBufferGets);
				resultList.add("-------------------------------------------------------------------------------");
				String strNameValue = "";

				Iterator<String> itr = tempList.get(i).getName_value().iterator();
				while (itr.hasNext()) {
					strNameValue += itr.next() + " \n";
				}
				resultList.add(strNameValue);
			}

		} else if (tempList.size() == 1) {

			for (int i = 0; i < 1; i++) {
				String strSnapIdAndExecTime = "[ SNAP_ID : " + tempList.get(i).getSnap_id() + ", EXECUTE_TIME : "
						+ tempList.get(i).getExec_time() + " ] ";
				String strElapsedTimeAndBufferGets = (i + 1) + ". 수행시간(Elapsed_Time) : "
						+ tempList.get(i).getElapsed_time() + "(초), Buffer Gets : " + tempList.get(i).getBuffer_gets();
				resultList.add(strSnapIdAndExecTime);
				resultList.add(strElapsedTimeAndBufferGets);
				resultList.add("-------------------------------------------------------------------------------");
				String strNameValue = "";

				Iterator<String> itr = tempList.get(i).getName_value().iterator();
				while (itr.hasNext()) {
					strNameValue += itr.next() + " \n";
				}
				resultList.add(strNameValue);
			}

		} else {
			throw new Exception("SQL BIND를 가지고오지 못했습니다.");
		}

		return resultList;
	}

	@Override
	public List<OdsHistSqlstat> outLineList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = PerfMonSqlAna.getOutline(StringUtil.parseLong(odsHistSqlstat.getDbid(), 0),
					odsHistSqlstat.getSql_id(), StringUtil.parseLong(odsHistSqlstat.getPlan_hash_value(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<OdsHistSqlstat> similaritySqlList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = PerfMonSqlAna.getSimilarSql(StringUtil.parseLong(odsHistSqlstat.getDbid(), 0),
					odsHistSqlstat.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<OdsHistSqlstat> sqlPerformHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = PerfMonSqlAna.getSqlRunHistory(StringUtil.parseLong(odsHistSqlstat.getDbid(), 0),
					odsHistSqlstat.getSql_id(), odsHistSqlstat.getPagePerCount(), odsHistSqlstat.getCurrentPage());
			System.out.println("resultList.size() ::: " +resultList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
//		System.out.println("resultList:::"+resultList);
		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> sqlPerformHistoryList4Excel(OdsHistSqlstat odsHistSqlstat)
			throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			long dbid = StringUtil.parseLong(odsHistSqlstat.getDbid(), 0);
			String sql_id = odsHistSqlstat.getSql_id();
			int pagePerCount = odsHistSqlstat.getPagePerCount();
			int currentPage = odsHistSqlstat.getCurrentPage();
			resultList = PerfMonSqlAna.getSqlRunHistory4Excel(dbid, sql_id, pagePerCount, currentPage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<SqlStatByPlan> sqlPerformPlanHistoryList(SqlStatByPlan sqlStatByPlan) throws Exception {
		List<SqlStatByPlan> resultList = new ArrayList<SqlStatByPlan>();
		try {
			resultList = PerfMonSqlAna.getSqlStatByPlan(StringUtil.parseLong(sqlStatByPlan.getDbid(), 0),
					sqlStatByPlan.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<String> awrExecutionPlanList(OdsHistSqlText odsHistSqlText) throws Exception {
		List<String> resultList = new ArrayList<String>();
		try {
			resultList = PerfMonSqlAna.getXPlanDisplayAWR(StringUtil.parseLong(odsHistSqlText.getDbid(), 0),
					odsHistSqlText.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<String> sqlTuningAdvisorList(OdsHistSqlText odsHistSqlText) throws Exception {
		List<String> resultList = new ArrayList<String>();
		try {
			resultList = PerfMonSqlAna.getSQLTuningAdvisorRecommendation(
					StringUtil.parseLong(odsHistSqlText.getDbid(), 0), odsHistSqlText.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<String> sqlAccessAdvisorList(OdsHistSqlText odsHistSqlText) throws Exception {
		List<String> resultList = new ArrayList<String>();
		try {
			resultList = PerfMonSqlAna.getSQLAccessAdvisorRecommendation(
					StringUtil.parseLong(odsHistSqlText.getDbid(), 0), odsHistSqlText.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<String> sqlMonitorList(OdsHistSqlText odsHistSqlText) throws Exception {
		List<String> resultList = new ArrayList<String>();
		try {
			long dbid = StringUtil.parseLong(StringUtils.defaultString(odsHistSqlText.getDbid(), "0"), 0);
			resultList = PerfMonSqlAna.getReportSQLMonitor(dbid, odsHistSqlText.getSql_id());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public Result waitClassChartLegend(ActiveSessionHistory activeSessionHistory) throws Exception {
		Result result = new Result();
		List<ActiveSessionHistory> legendList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				// 1. 차트 범례 리스트 조회
				legendList = sqlAnalysisDao.waitClassLegendList(activeSessionHistory);
			} else {
				// 서버모듈....
				// 1. 차트 범례 리스트 조회
				legendList = PerfMonAshReal
						.waitClassLegendList(StringUtil.parseLong(activeSessionHistory.getDbid(), 0));
			}

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setMessage("차트 생성 데이터가 존재하지 않습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String ashWaitClassChart(ActiveSessionHistory activeSessionHistory) throws Exception {
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<ActiveSessionHistory> dataList = new ArrayList<ActiveSessionHistory>();
		String tempDate = "";
		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				// 1. 차트 데이터 조회
				dataList = sqlAnalysisDao.waitClassDataList(activeSessionHistory);
			} else {
				// 1. 차트 데이터 조회
				dataList = PerfMonAshReal.waitClassDataList(StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id(),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time());
			}

			for (int i = 0; i < dataList.size(); i++) {
				ActiveSessionHistory temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSample_time())) {
					data = new JSONObject();
					data.put("date", temp.getSample_time().split(" ")[0]);
					data.put("time", temp.getSample_time().split(" ")[1]);

					data.put(temp.getWait_class(), StringUtil.parseInt(temp.getWait_class_cnt(), 0));
				} else if (tempDate.equals(temp.getSample_time())) {
					data.put(temp.getWait_class(), StringUtil.parseInt(temp.getWait_class_cnt(), 0));
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSample_time())) {

					list.add(data);
					data = new JSONObject();
					data.put("date", temp.getSample_time().split(" ")[0]);
					data.put("time", temp.getSample_time().split(" ")[1]);
					data.put(temp.getWait_class(), StringUtil.parseInt(temp.getWait_class_cnt(), 0));
				}

				tempDate = temp.getSample_time();
			}

			list.add(data);
			jsonResult.put("rows", list);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return jsonResult.toString();
	}

	@Override
	public Result topWaitEventChartLegend(ActiveSessionHistory activeSessionHistory) throws Exception {
		Result result = new Result();
		List<ActiveSessionHistory> legendList = new ArrayList<ActiveSessionHistory>();

		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				// 1. 차트 범례 리스트 조회
				legendList = sqlAnalysisDao.topWaitEventLegendList(activeSessionHistory);
			} else {
				// 1. 차트 범례 리스트 조회
				legendList = PerfMonAshReal.topWaitEventLegendList(
						StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id(),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time());
			}

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setMessage("차트 생성 데이터가 존재하지 않습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String topWaitEventChart(ActiveSessionHistory activeSessionHistory) throws Exception {
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<ActiveSessionHistory> dataList = new ArrayList<ActiveSessionHistory>();
		String tempDate = "";
		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				// 1. 차트 데이터 조회
				dataList = sqlAnalysisDao.topWaitEventDataList(activeSessionHistory);
			} else {
				// 1. 차트 데이터 조회
				dataList = PerfMonAshReal.topWaitEventDataList(StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id(),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time());
			}

			for (int i = 0; i < dataList.size(); i++) {
				ActiveSessionHistory temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSample_time())) {
					data = new JSONObject();
					data.put("date", temp.getSample_time().split(" ")[0]);
					data.put("time", temp.getSample_time().split(" ")[1]);

					data.put(temp.getEvent(), StringUtil.parseInt(temp.getEvent_cnt(), 0));
				} else if (tempDate.equals(temp.getSample_time())) {
					data.put(temp.getEvent(), StringUtil.parseInt(temp.getEvent_cnt(), 0));
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSample_time())) {

					list.add(data);
					data = new JSONObject();
					data.put("date", temp.getSample_time().split(" ")[0]);
					data.put("time", temp.getSample_time().split(" ")[1]);
					data.put(temp.getEvent(), StringUtil.parseInt(temp.getEvent_cnt(), 0));
				}

				tempDate = temp.getSample_time();
			}

			list.add(data);
			jsonResult.put("rows", list);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return jsonResult.toString();
	}

	@Override
	public List<ActiveSessionHistory> allSessionList(ActiveSessionHistory activeSessionHistory) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				resultList = sqlAnalysisDao.allSessionList(activeSessionHistory);
			} else {
				// SEARCH_ALL_ACTIVE_SESSION(long DBID, long INST_ID, long SID,
				// long SERIAL, String EVENT, String MODULE, String SQL_ID,
				// String sample_start_time, String sample_end_time, int
				// pageSize, int currentPage)
				resultList = PerfMonAshReal.SEARCH_ALL_ACTIVE_SESSION(
						StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id(),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time(),
						activeSessionHistory.getPagePerCount(), activeSessionHistory.getCurrentPage());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<ActiveSessionHistory> topSqlList(ActiveSessionHistory activeSessionHistory) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				resultList = sqlAnalysisDao.topSqlList(activeSessionHistory);
			} else {
				// TOP_SQL(Long DBID, long INST_ID, String SAMPLE_TIME_BEGIN,
				// String SAMPLE_TIME_END, long SESS_ID, long SESS_SERIAL,
				// String EVENT, String MODULE, String SQL_ID)
				resultList = PerfMonAshReal.TOP_SQL(StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time(),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<ActiveSessionHistory> topSessionList(ActiveSessionHistory activeSessionHistory) throws Exception {
		List<ActiveSessionHistory> resultList = new ArrayList<ActiveSessionHistory>();
		try {
			if (activeSessionHistory.getReal_search().equals("N")) {
				resultList = sqlAnalysisDao.topSessionList(activeSessionHistory);
			} else {
				// TOP_SESSION(Long DBID, long INST_ID, String
				// sample_start_time, String sample_end_time, long SESS_ID, long
				// SESS_SERIAL, String EVENT, String MODULE, String SQL_ID)
				resultList = PerfMonAshReal.TOP_SESSION(StringUtil.parseLong(activeSessionHistory.getDbid(), 0),
						StringUtil.parseLong(activeSessionHistory.getInst_id(), 0),
						activeSessionHistory.getSample_start_time(), activeSessionHistory.getSample_end_time(),
						StringUtil.parseLong(activeSessionHistory.getSid(), 0),
						StringUtil.parseLong(activeSessionHistory.getSerial(), 0), activeSessionHistory.getEvent(),
						activeSessionHistory.getModule(), activeSessionHistory.getSql_id());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<TableUseSql> tableUseSqlList(TableUseSql tableUseSql) throws Exception {
		return sqlAnalysisDao.tableUseSqlList(tableUseSql);
	}

	@Override
	public List<Map<String, Object>> tableUseSqlList4Excel(TableUseSql tableUseSql) throws Exception {
		return sqlAnalysisDao.tableUseSqlList4Excel(tableUseSql);
	}

	@Override
	public List<TableUseSql> indexUseSqlList(TableUseSql tableUseSql) throws Exception {
		return sqlAnalysisDao.indexUseSqlList(tableUseSql);
	}

	@Override
	public List<Map<String, Object>> indexUseSqlList4Excel(TableUseSql tableUseSql) throws Exception {
		return sqlAnalysisDao.indexUseSqlList4Excel(tableUseSql);
	}

	@Override
	public String getPlanHashValue(OdsHistSqlText odsHistSqlText) {
		return sqlAnalysisDao.getPlanHashValue(odsHistSqlText);
	}

}