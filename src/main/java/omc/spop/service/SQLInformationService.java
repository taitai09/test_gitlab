package omc.spop.service;

import java.util.List;

import omc.spop.model.DbioLoadSql;
import omc.spop.model.DbioPlanTable;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.SqlGrid;

/***********************************************************
 * 2018.04.10	이원식	OPENPOP V2 최초작업 (SQL 정보 탭구성 호출)
 * 2021.02.04	이재우	기능개선
 **********************************************************/

public interface SQLInformationService {
	/** SQL 정보 - SQL TEXT */
	OdsHistSqlText sqlText(OdsHistSqlText odsHistSqlText) throws Exception;
	
	/** SQL 정보 - SQL Tree Plan 리스트 */
	List<SqlGrid> sqlTreePlanList(SqlGrid sqlGrid) throws Exception;	
	
	/** SQL 정보 - SQL TEXT Plan 리스트 */
	List<OdsHistSqlText> sqlTextPlanList(OdsHistSqlText odsHistSqlText) throws Exception;
	
	/** SQL 정보 - SQL Grid Plan 리스트 */
	List<SqlGrid> sqlGridPlanList(SqlGrid sqlGrid) throws Exception;

	/** SQL 정보 - Bind Value 리스트 */
	List<OdsHistSqlstat> bindValueList(OdsHistSqlstat odsHistSqlstat) throws Exception;
	
	/** SQL 정보 - Out Line 리스트 */
	List<OdsHistSqlstat> outLineList(OdsHistSqlstat odsHistSqlstat) throws Exception;
	
	/** SQL 정보 - 유사 SQL 리스트 */
	List<OdsHistSqlstat> similaritySqlList(OdsHistSqlstat odsHistSqlstat) throws Exception;	

	/** SQL 정보 - SQL History 리스트 */
	List<OdsHistSqlstat> sqlPerformHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception;	
	
	/** 적재SQL 인덱스 설계 - SQL 정보 - SQL TEXT 리스트 */
	DbioLoadSql loadSqlText(DbioLoadSql dbioLoadSql) throws Exception;
	
	/** 적재SQL 인덱스 설계 - SQL 정보 - Graphic 리스트 */
	List<SqlGrid> sqlGraphicList(DbioPlanTable dbioPlanTable) throws Exception;
	
	/** 적재SQL 인덱스 설계 - SQL 정보 - Text 리스트 */
	List<DbioPlanTable> sqlTextList(DbioPlanTable dbioPlanTable) throws Exception;

	OdsHistSqlText sqlTextAll(OdsHistSqlText odsHistSqlText) throws Exception;

	List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText);

	List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid);

	List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid);

	List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat);

	List<OdsHistSqlstat> similaritySqlListAll(OdsHistSqlstat odsHistSqlstat);

	List<OdsHistSqlstat> bindValueListAll(OdsHistSqlstat odsHistSqlstat);

	List<OdsHistSqlstat> bindValueNewList(OdsHistSqlstat odsHistSqlstat);

	List<OdsHistSqlstat> sqlPerformNewHistoryList(OdsHistSqlstat odsHistSqlstat);
}
