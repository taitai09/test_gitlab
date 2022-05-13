package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.SqlTuningIndexHistory;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;

/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface TuningHistoryDao {	
	public List<TuningTargetSql> tuningHistoryList(TuningTargetSql tuningTargetSql);
	
	public TuningTargetSql readSqlDetail(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql);

	public List<SqlTuningIndexHistory> sqlTuningIndexHistoryList(TuningTargetSql tuningTargetSql);

	public List<LinkedHashMap<String, Object>> tuningHistoryList4Excel(TuningTargetSql tuningTargetSql);

	public int tuningHistoryCount(TuningTargetSql tuningTargetSql);
}