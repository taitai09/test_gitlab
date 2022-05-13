package omc.spop.dao;

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

public interface SQLInformationDao {
	public OdsHistSqlText sqlText(OdsHistSqlText odsHistSqlText);
	
	public List<SqlGrid> sqlTreePlanList(SqlGrid sqlGrid);
	
	public List<OdsHistSqlText> sqlTextPlanList(OdsHistSqlText odsHistSqlText);
	
	public List<SqlGrid> sqlGridPlanList(SqlGrid sqlGrid);
	
	public List<OdsHistSqlstat> bindValueList(OdsHistSqlstat odsHistSqlstat);
	
	public List<OdsHistSqlstat> outLineList(OdsHistSqlstat odsHistSqlstat);
	
	public List<OdsHistSqlstat> similaritySqlList(OdsHistSqlstat odsHistSqlstat);

	public List<OdsHistSqlstat> sqlPerformHistoryList(OdsHistSqlstat odsHistSqlstat);	
	
	public DbioLoadSql loadSqlText(DbioLoadSql dbioLoadSql);
	
	public List<SqlGrid> sqlGraphicList(DbioPlanTable dbioPlanTable);
	
	public List<DbioPlanTable> sqlTextList(DbioPlanTable dbioPlanTable);

	public OdsHistSqlText sqlTextAll(OdsHistSqlText odsHistSqlText);

	public List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText);

	public List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid);

	public List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid);

	public List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat);

	public List<OdsHistSqlstat> similaritySqlListAll(OdsHistSqlstat odsHistSqlstat);

	public List<OdsHistSqlstat> bindValueListAll(OdsHistSqlstat odsHistSqlstat);

	public List<OdsHistSqlstat> bindValueNewList(OdsHistSqlstat odsHistSqlstat);

	public List<OdsHistSqlstat> sqlPerformNewHistoryList(OdsHistSqlstat odsHistSqlstat);
}
