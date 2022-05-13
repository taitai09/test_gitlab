package omc.spop.dao;

import java.util.List;

import omc.spop.model.Database;
import omc.spop.model.SqlStandardOperationPlugIn;
import omc.spop.model.WrkJobCd;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface SqlStandardOperationPlugInDao {
	
	public List<SqlStandardOperationPlugIn> getMaxSqlStdQtyChktId() throws Exception;
	
	public int deleteSelfsqlStdQtyChkErr(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int deleteSelfsqlStdQtyChkErrSum(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int deleteSelfsqlStdQtyChkResult(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int deleteSelfsqlStdQtyChkPgm(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int deleteSelfsqlStdQtyFileError(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int deleteSelfsqlStdPlanExec(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSelfsqlStdQtyChkPgmForeach(List<?> paramList) throws Exception;
	
	public int updateSelfsqlStdQtyChkPgmErrMsgForeach(List<?> paramList) throws Exception;
	
	public List<SqlStandardOperationPlugIn> getNonComplianceData(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public Database findDbId(Database database) throws Exception;
	
	public int insertSelfsqlStdQtyPlanExec(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public SqlStandardOperationPlugIn selectInsertSQL(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSqlStdQtyChkErr(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSqlStdQtyChkErrSum(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int updateSelfsqlStdQtyPlanExecComplete(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int updateSelfsqlStdQtyPlanExecForceComplete(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSelectSelfsqlStdQtyChkPgmForAWR(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSelectSelfsqlStdQtyChkPgmForVSQL(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int selectCountSelfSqlStdQtyChkPgm(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public String selectYesterday(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public String selectWeekAgo(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public String selectMonthAgo(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public String selectQuarterAgo(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public String selectOneYearAgo(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public List<WrkJobCd> getWrkJobCdList() throws Exception;
	
	public int updateSelfsqlStdQtyChkPgmForeach(List<?> paramList) throws Exception;
	
	public int updateSelfsqlStdQtyPlanExecStatus(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public int insertSelfsqlStdQtyFileError(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	public SqlStandardOperationPlugIn selectSelfsqlStdQtyPlanExecCnt(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
}
