package omc.spop.dao;

import java.util.List;

import omc.spop.model.BeforePerfExpect;
import omc.spop.model.BeforePerfExpectSqlStat;
import omc.spop.model.OdsUsers;
import omc.spop.model.SqlPerfImplAnalSql;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.model.SqlPerfImpluenceAnalysis;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SQLInfluenceDiagnosticsDao {	
	public List<BeforePerfExpect> topSQLDiagnosticsList(BeforePerfExpect beforePerfExpect);
	
	public BeforePerfExpect getTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect);
	
	public String getMaxBeforePerfExpectNo(BeforePerfExpect beforePerfExpect);
	
	public int insertTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect);
	
	public int updateTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect);
	
	public int deleteTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect);
	
	public List<BeforePerfExpectSqlStat> topSQLDiagnosticsDetailList(BeforePerfExpectSqlStat beforePerfExpectSqlStat);
	
	public int updateSQLProfile_TOPSQL(BeforePerfExpectSqlStat beforePerfExpectSqlStat);
	
	public List<SqlPerfImpluenceAnalysis> objectImpactDiagnosticsList(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public SqlPerfImpluenceAnalysis getObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public List<SqlPerfImplAnalTable> getTargetTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable);
	
	public List<OdsUsers> getTableOwner(OdsUsers odsUsers);
	
	public List<SqlPerfImplAnalTable> getSelectTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable);	

	public String getSqlPerfImplAnalNo(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public int insertObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public int updateObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public void deleteObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public void deleteObjectImpactDiagnosticsTable(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis);
	
	public int insertObjectImpactDiagnosticsTable(SqlPerfImplAnalTable sqlPerfImplAnalTable);
	
	public List<SqlPerfImplAnalTable> objectImpactDiagnosticsTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable);
	
	public List<SqlPerfImplAnalSql> objectImpactDiagnosticsTableDetailList(SqlPerfImplAnalSql sqlPerfImplAnalSql);
	
	public int updateSQLProfile_Object(SqlPerfImplAnalSql sqlPerfImplAnalSql);	
}