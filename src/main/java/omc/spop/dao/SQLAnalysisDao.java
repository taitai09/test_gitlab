package omc.spop.dao;

import java.util.List;
import java.util.Map;

import omc.spop.model.ActiveSessionHistory;
import omc.spop.model.ApmApplSql;
import omc.spop.model.OdsApmSAppl;
import omc.spop.model.OdsApmSSqls;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.TableUseSql;

/***********************************************************
 * 2018.03.13 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SQLAnalysisDao {
	public List<ApmApplSql> appPerformanceList(ApmApplSql apmApplSql);

	public List<OdsApmSAppl> appDailyList(OdsApmSAppl odsApmSAppl);

	public List<OdsApmSAppl> appTimeList(OdsApmSAppl odsApmSAppl);

	public List<OdsApmSSqls> dbioDailyList(OdsApmSSqls odsApmSSqls);

	public List<OdsApmSSqls> dbioTimeList(OdsApmSSqls odsApmSSqls);

	public List<OdsHistSqlstat> dbioHistoryList(OdsHistSqlstat odsHistSqlstat);

	public List<ActiveSessionHistory> allSessionList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> waitClassLegendList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> waitClassDataList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> topWaitEventLegendList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> topWaitEventDataList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> topSqlList(ActiveSessionHistory activeSessionHistory);

	public List<ActiveSessionHistory> topSessionList(ActiveSessionHistory activeSessionHistory);

	public List<TableUseSql> tableUseSqlList(TableUseSql tableUseSql);

	public List<Map<String, Object>> tableUseSqlList4Excel(TableUseSql tableUseSql);

	public List<TableUseSql> indexUseSqlList(TableUseSql tableUseSql);

	public List<Map<String, Object>> indexUseSqlList4Excel(TableUseSql tableUseSql);

	public List<ApmApplSql> getSqlText1(ApmApplSql param);

	public OdsHistSqlText getSqlText2(OdsHistSqlText param);

	public String getPlanHashValue(OdsHistSqlText odsHistSqlText);

}
