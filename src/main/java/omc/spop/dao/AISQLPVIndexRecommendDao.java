package omc.spop.dao;

import omc.spop.model.AuthoritySQL;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationAnalyzeDao
 **********************************************************/

public interface AISQLPVIndexRecommendDao {
	public List<SQLAutoPerformanceCompare> getIndexRecommend(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	public List<SQLAutoPerformanceCompare> getCreationOrDropHistory(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	public List<SQLAutoPerformanceCompare> getCreationOrDropHistoryAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	public List<SQLAutoPerformanceCompare> getPerformanceAnalysis(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	public int forceUpdateSqlAutoPerformance( SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck );

	public int countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	public int countPerformanceRecord( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );
	public List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL);

	int setExcuteAnalyzeSqlAutoPerfChk(HashMap<String, String> paramMap);
	public ArrayList<HashMap<String, String>> getAutoError(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
}