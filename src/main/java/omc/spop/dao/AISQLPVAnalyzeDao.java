package omc.spop.dao;

import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;

import java.util.HashMap;
import java.util.List;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationAnalyzeDao
 **********************************************************/

public interface AISQLPVAnalyzeDao {
	public List<SQLAutoPerformanceCompare> getProjectPerformancePacData( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> getProjectPerformancePacData_OneRow( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> getPerformancePacData( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> getExecutionConstraint( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public List<SQLAutoPerformanceCompare> getOriginalDB( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

	public int updateSqlAutoPerfChk( SQLAutoPerformanceCompare sqlAutoPerformanceCompare );

}