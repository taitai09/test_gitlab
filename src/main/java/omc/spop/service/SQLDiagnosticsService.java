package omc.spop.service;

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

public interface SQLDiagnosticsService {
	/** 요약 테이블 헤더 리스트 */
	List<Cd> getTableTitleList() throws Exception;
	
	/** 요약 정보 리스트 */
	List<SqlDiagSummary> summaryList(SqlDiagSummary sqlDiagSummary) throws Exception;
	
	/** 요약 정보 차트 리스트 */
	List<SqlDiagSummary> summaryChartList(SqlDiagSummary sqlDiagSummary) throws Exception;	
	
	/** PLAN 변경 SQL 리스트 */
	List<PlanChangeSql> planChangeSqlList(PlanChangeSql planChangeSql) throws Exception;
	
	/** PLAN 변경 SQL - SQL Profile 정보 UPDATE */
	int updatePlanChangeSqlProfile(PlanChangeSql planChangeSql) throws Exception;	

	/** NEW SQL 리스트 */
	List<NewSql> newSqlList(NewSql newSql) throws Exception;
	
	/** LITERAL SQL(Dynamic SQL) 리스트 */
	List<LiteralSql> literalSqlList(LiteralSql literalSql) throws Exception;
	
	/** TEMP과다사용 SQL 리스트 */
	List<TempUsageSql> tempOveruseSqlList(TempUsageSql tempUsageSql) throws Exception;
	
	/** FULL SCAN 리스트 */
	List<FullscanSql> fullscanSqlList(FullscanSql fullscanSql) throws Exception;
	
	/** 조건없는 delete문 리스트 */
	List<NonpredDeleteStmt> deleteWithoutConditionList(NonpredDeleteStmt nonpredDeleteStmt) throws Exception;
	
	/** 튜닝요청 팝업 */
	List<TuningTargetSql> insertTuningRequest(TuningTargetSql tuningTargetSql) throws Exception;

	/** TOP SQL 리스트 */
	List<TopSql> topSqlList(TopSql topSql);

	List<OffloadSql> offloadSqlList(OffloadSql offloadSql);

	List<OffloadEffcReduceSql> offloadEffcReduceSqlList(OffloadEffcReduceSql offloadEffcReduceSql);

	/** LITERAL(SQL) STATUS 리스트*/
	List<LiteralSql> literalSqlStatusList(LiteralSql literalSql);

	Vector<String> getExceptionList(String string);	
}