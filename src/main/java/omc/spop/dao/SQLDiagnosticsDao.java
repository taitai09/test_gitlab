package omc.spop.dao;

import java.util.List;
import java.util.Vector;

import omc.spop.model.Cd;
import omc.spop.model.FullscanSql;
import omc.spop.model.LiteralSql;
import omc.spop.model.NewSql;
import omc.spop.model.NonpredDeleteStmt;
import omc.spop.model.OffloadEffcReduceSql;
import omc.spop.model.OffloadSql;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.SqlDiagSummary;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSql;
import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SQLDiagnosticsDao {
	public List<Cd> getTableTitleList();
	
	public List<SqlDiagSummary> summaryList(SqlDiagSummary sqlDiagSummary);
	
	public List<SqlDiagSummary> summaryChartList(SqlDiagSummary sqlDiagSummary);
	
	public List<PlanChangeSql> planChangeSqlList(PlanChangeSql planChangeSql);
	
	public int updatePlanChangeSqlProfile(PlanChangeSql planChangeSql);

	public List<NewSql> newSqlList(NewSql newSql);	
	
	public List<LiteralSql> literalSqlList(LiteralSql literalSql);
	
	public List<TempUsageSql> tempOveruseSqlList(TempUsageSql tempUsageSql);
	
	public List<FullscanSql> fullscanSqlList(FullscanSql fullscanSql);
	
	public List<NonpredDeleteStmt> deleteWithoutConditionList(NonpredDeleteStmt nonpredDeleteStmt);
	
	public void insertTuningTargetSql(TuningTargetSql tuningTargetSql);
	
	public void updateTuningTarget(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> perfrIdAssignCountList(TuningTargetSql tuningTargetSql);

	public List<TopSql> topSqlList(TopSql topSql);

	public List<OffloadSql> offloadSqlList(OffloadSql offloadSql);

	public List<OffloadEffcReduceSql> offloadEffcReduceSqlList(OffloadEffcReduceSql offloadEffcReduceSql);

	public List<LiteralSql> literalSqlStatusList(LiteralSql literalSql);

	public Vector<String> getExceptionList(String string);	
}
