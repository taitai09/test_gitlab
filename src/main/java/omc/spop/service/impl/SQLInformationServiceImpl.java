package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SQLInformationDao;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.DbioPlanTable;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.SqlGrid;
import omc.spop.service.SQLInformationService;

/***********************************************************
 * 2018.04.10	이원식	OPENPOP V2 최초작업 (SQL 정보 탭구성 호출)
 * 2021.02.04	이재우	기능개선
 **********************************************************/

@Service("SQLInformationService")
public class SQLInformationServiceImpl implements SQLInformationService {
	@Autowired
	private SQLInformationDao sqlInformationDao;
	
	@Override
	public OdsHistSqlText sqlText(OdsHistSqlText odsHistSqlText) throws Exception {
		return sqlInformationDao.sqlText(odsHistSqlText);
	}	
	
	@Override
	public List<SqlGrid> sqlTreePlanList(SqlGrid sqlGrid) throws Exception {
		return sqlInformationDao.sqlTreePlanList(sqlGrid);
	}		
	
	@Override
	public List<OdsHistSqlText> sqlTextPlanList(OdsHistSqlText odsHistSqlText) throws Exception {
		return sqlInformationDao.sqlTextPlanList(odsHistSqlText);
	}
	
	@Override
	public List<SqlGrid> sqlGridPlanList(SqlGrid sqlGrid) throws Exception {
		return sqlInformationDao.sqlGridPlanList(sqlGrid);
	}	
	
	@Override
	public List<OdsHistSqlstat> bindValueList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlInformationDao.bindValueList(odsHistSqlstat);
	}
	
	@Override
	public List<OdsHistSqlstat> outLineList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlInformationDao.outLineList(odsHistSqlstat);
	}
	
	@Override
	public List<OdsHistSqlstat> similaritySqlList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlInformationDao.similaritySqlList(odsHistSqlstat);
	}	

	@Override
	public List<OdsHistSqlstat> sqlPerformHistoryList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlInformationDao.sqlPerformHistoryList(odsHistSqlstat);
	}
	
	@Override
	public DbioLoadSql loadSqlText(DbioLoadSql dbioLoadSql) throws Exception {
		return sqlInformationDao.loadSqlText(dbioLoadSql);
	}	
	
	@Override
	public List<SqlGrid> sqlGraphicList(DbioPlanTable dbioPlanTable) throws Exception {
		return sqlInformationDao.sqlGraphicList(dbioPlanTable);
	}
	
	@Override
	public List<DbioPlanTable> sqlTextList(DbioPlanTable dbioPlanTable) throws Exception {
		return sqlInformationDao.sqlTextList(dbioPlanTable);
	}

	@Override
	public OdsHistSqlText sqlTextAll(OdsHistSqlText odsHistSqlText) {
		return sqlInformationDao.sqlTextAll(odsHistSqlText);
	}

	@Override
	public List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText) {
		return sqlInformationDao.sqlTextPlanListAll(odsHistSqlText);
	}

	@Override
	public List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid) {
		return sqlInformationDao.sqlTreePlanListAll(sqlGrid);
	}

	@Override
	public List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid) {
		return sqlInformationDao.sqlGridPlanListAll(sqlGrid);
	}

	@Override
	public List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat) {
		return sqlInformationDao.outLineListAll(odsHistSqlstat);
	}

	@Override
	public List<OdsHistSqlstat> similaritySqlListAll(OdsHistSqlstat odsHistSqlstat) {
		return sqlInformationDao.similaritySqlListAll(odsHistSqlstat);
	}

	@Override
	public List<OdsHistSqlstat> bindValueListAll(OdsHistSqlstat odsHistSqlstat) {
		return sqlInformationDao.bindValueListAll(odsHistSqlstat);
	}

	@Override
	public List<OdsHistSqlstat> bindValueNewList(OdsHistSqlstat odsHistSqlstat) {
		return sqlInformationDao.bindValueNewList(odsHistSqlstat);
	}

	@Override
	public List<OdsHistSqlstat> sqlPerformNewHistoryList(OdsHistSqlstat odsHistSqlstat) {
		return sqlInformationDao.sqlPerformNewHistoryList(odsHistSqlstat);
	}	
	
}