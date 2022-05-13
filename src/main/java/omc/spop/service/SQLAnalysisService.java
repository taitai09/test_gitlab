package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

/***********************************************************
 * 2018.03.13 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SQLAnalysisService {
	/** 애플리케이션 분석 리스트 */
	List<ApmApplSql> appPerformanceList(ApmApplSql apmApplSql) throws Exception;

	/** 애플리케이션 분석 - APP 일별 리스트 */
	List<OdsApmSAppl> appDailyList(OdsApmSAppl odsApmSAppl) throws Exception;

	/** 애플리케이션 분석 - APP 시간별 리스트 */
	List<OdsApmSAppl> appTimeList(OdsApmSAppl odsApmSAppl) throws Exception;

	/** 애플리케이션 분석 - DBIO 일별 리스트 */
	List<OdsApmSSqls> dbioDailyList(OdsApmSSqls odsApmSSqls) throws Exception;

	/** 애플리케이션 분석 - DBIO 시간별 리스트 */
	List<OdsApmSSqls> dbioTimeList(OdsApmSSqls odsApmSSqls) throws Exception;

	/** 애플리케이션 분석 - DBIO 수행이력 리스트 */
	List<OdsHistSqlstat> dbioHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** 세션모니터링 리스트 */
	List<Session> sessionMonitoringList(Session session) throws Exception;

	/** 세션모니터링 wait class chart 리스트 */
	List<ActiveSessionHistory> sessionWaitClassChartList(Session session) throws Exception;

	/** 세션모니터링 top event chart 리스트 */
	List<ActiveSessionHistory> topEventChartList(Session session) throws Exception;

	/** 세션모니터링 top module chart 리스트 */
	List<omc.spop.vo.server.Module> topModuleChartList(Session session) throws Exception;

	/** 세션모니터링 - session kill */
	String sessionKill(Session session) throws Exception;

	/** 세션모니터링 - Last Cursor */
	String lastCursor(Session session) throws Exception;

	/** 세션모니터링 - All Cursor 리스트 */
	List<Session> allCursorList(Session session) throws Exception;

	/** 세션모니터링 - SQL성능 리스트 */
	List<SessionSql> sqlPerformanceList(SessionSql sessionSql) throws Exception;

	/** 세션모니터링 - SQL History 리스트 */
	List<ActiveSessionHistory> sqlHistoryList(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** 세션모니터링 - SQL Grid Plan 리스트 */
	List<SqlGrid> sqlGridPlanList(SqlGrid sqlGrid) throws Exception;

	/** 세션모니터링 - SQL TEXT Plan 리스트 */
	List<Session> sqlTextPlanList(Session session) throws Exception;

	/** 세션모니터링 - session kill script */
	String sessionKillScript(Session session) throws Exception;

	/** 세션모니터링 - process kill script */
	String processKillScript(Session session) throws Exception;

	/** 세션모니터링 - Connect Info 리스트 */
	List<SessionConnectInfo> connectInfoList(Session session) throws Exception;

	/** 세션모니터링 - Event 리스트 */
	List<SessionEvent> eventList(Session session) throws Exception;

	/** 세션모니터링 - Wait 리스트 */
	List<SessionWait> waitList(Session session) throws Exception;

	/** 세션모니터링 - Wait Class 리스트 */
	List<SessionWaitClass> waitClassList(Session session) throws Exception;

	/** 세션모니터링 - WaitHistory 리스트 */
	List<SessionWait> waitHistoryList(Session session) throws Exception;

	/** 세션모니터링 - Metric 리스트 */
	List<SessMetric> metricList(Session session) throws Exception;

	/** 세션모니터링 - TimeModel 리스트 */
	List<SessTimeModel> timeModelList(Session session) throws Exception;

	/** 세션모니터링 - Statistics 리스트 */
	List<SessionWaitClass> statisticsList(Session session) throws Exception;

	/** 세션모니터링 - I/O 리스트 */
	List<SessIO> ioList(Session session) throws Exception;

	/** 세션모니터링 - LongOPS 리스트 */
	List<SessionLongops> longOPSList(Session session) throws Exception;

	/** 모듈성능분석 리스트 */
	List<Module> modulePerformanceList(Module module) throws Exception;

	List<LinkedHashMap<String, Object>> modulePerformanceList4Excel(Module module) throws Exception;

	/** 모듈성능분석 DETAIL 리스트 */
	List<DbaHistSqlstat> dtlModulePerformanceList(DbaHistSqlstat dbaHistSqlstat) throws Exception;

	List<LinkedHashMap<String, Object>> dtlModulePerformanceList4Excel(DbaHistSqlstat dbaHistSqlstat) throws Exception;

	/** 모듈성능분석 - TOP SQL Stat Chart 리스트 */
	List<Module> topSQLStatChartList(Module module) throws Exception;

	/** SQL성능분석 SQL TEXT */
	OdsHistSqlText sqlText(OdsHistSqlText odsHistSqlText) throws Exception;

	OdsHistSqlText getSqlText2(OdsHistSqlText param) throws Exception;

	List<ApmApplSql> getSqlText1(ApmApplSql param) throws Exception;

	/** SQL성능분석 - SQL Tree Plan 리스트 */
	List<SqlGrid> sqlPerformTreePlanList(SqlGrid sqlGrid) throws Exception;

	/** SQL성능분석 - SQL TEXT Plan 리스트 */
	List<OdsHistSqlText> sqlPerformTextPlanList(OdsHistSqlText odsHistSqlText) throws Exception;

	/** SQL성능분석 - SQL Grid Plan 리스트 */
	List<SqlGrid> sqlPerformGridPlanList(SqlGrid sqlGrid) throws Exception;

	/** SQL성능분석 - Bind Value 리스트 */
	List<OdsHistSqlstat> bindValueList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** SQL성능분석 - Out Line 리스트 */
	List<OdsHistSqlstat> outLineList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** SQL성능분석 - 유사 SQL 리스트 */
	List<OdsHistSqlstat> similaritySqlList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** SQL성능분석 SQL AWR별 SQL Stats 리스트 */
	List<OdsHistSqlstat> sqlPerformHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** SQL성능분석 SQL AWR별 SQL Stats 리스트 for Excel download */
	List<LinkedHashMap<String, Object>> sqlPerformHistoryList4Excel(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** SQL성능분석 SQL Plan별 SQL Stats 리스트 */
	List<SqlStatByPlan> sqlPerformPlanHistoryList(SqlStatByPlan sqlStatByPlan) throws Exception;

	/** SQL성능분석 AWR Execution Plan 리스트 */
	List<String> awrExecutionPlanList(OdsHistSqlText odsHistSqlText) throws Exception;

	/** SQL성능분석 SQL Tuning Advisor 리스트 */
	List<String> sqlTuningAdvisorList(OdsHistSqlText odsHistSqlText) throws Exception;

	/** SQL성능분석 SQL Access Advisor 리스트 */
	List<String> sqlAccessAdvisorList(OdsHistSqlText odsHistSqlText) throws Exception;

	/** SQL성능분석 SQL Monitor 리스트 */
	List<String> sqlMonitorList(OdsHistSqlText odsHistSqlText) throws Exception;

	/** ASH성능분석 - Wait Class Chart 정보 조회 */
	Result waitClassChartLegend(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - Wait Class Chart 정보 조회 */
	String ashWaitClassChart(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - Top Wait Event Chart 정보 조회 */
	Result topWaitEventChartLegend(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - Top Wait Event Chart 정보 조회 */
	String topWaitEventChart(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - All Session 리스트 */
	List<ActiveSessionHistory> allSessionList(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - Top Sql 리스트 */
	List<ActiveSessionHistory> topSqlList(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** ASH성능분석 - Top Session 리스트 */
	List<ActiveSessionHistory> topSessionList(ActiveSessionHistory activeSessionHistory) throws Exception;

	/** 테이블 사용 SQL 분석 리스트 */
	List<TableUseSql> tableUseSqlList(TableUseSql tableUseSql) throws Exception;

	List<Map<String, Object>> tableUseSqlList4Excel(TableUseSql tableUseSql) throws Exception;

	/** 인덱스 사용 SQL 분석 리스트 */
	List<TableUseSql> indexUseSqlList(TableUseSql tableUseSql) throws Exception;

	List<Map<String, Object>> indexUseSqlList4Excel(TableUseSql tableUseSql) throws Exception;

	/** 튜닝요청 SQLBind */
	List<String> SQLBind(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** dash board > sql 성능분석 plan_hash_value 값 가지고오기 */
	String getPlanHashValue(OdsHistSqlText odsHistSqlText);

}
