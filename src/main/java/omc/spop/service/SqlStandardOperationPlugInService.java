package omc.spop.service;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;

import omc.spop.model.Database;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.model.Result;
import omc.spop.model.SqlStandardOperationPlugIn;
import omc.spop.model.SqlStandardOperationPlugInResponse;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2021.06.09   김원재             firstTimeModule 메서드 추가.
 **********************************************************/

public interface SqlStandardOperationPlugInService {
	
	List<SqlStandardOperationPlugIn> getMaxSqlStdQtyChktId() throws Exception;
	
	List<Database> findDbid(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	boolean deleteTableList(SqlStandardOperationPlugIn model) throws SQLSyntaxErrorException, Exception;
	
	boolean insertSelfsqlStdQtyPlanExec(SqlStandardOperationPlugIn model) throws SQLSyntaxErrorException, Exception;
	
	List<ProjectSqlQtyChkRule> getCountCreatePlan(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception;
	
	Result receiveBoxBatch(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	HashMap<String, String> getWrkJobCdList();
	
	String createPlan(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	List<SqlStandardOperationPlugInResponse> getNonComplianceDataResponse(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	Result executeSql(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
	
	boolean updateSelfsqlStdQtyPlanExecForceComplete(SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception;
}
