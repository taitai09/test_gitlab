package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.SqlTuningIndexHistory;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;

/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface TuningHistoryService {
	/** 튜닝이력조회 리스트 */
	List<TuningTargetSql> tuningHistoryList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** SQL 상세 리스트 */
	TuningTargetSql readSqlDetail(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** BIND 리스트 */
	List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** BIND 리스트 */
	List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql) throws Exception;

	/** Index 이력조회 */
	List<SqlTuningIndexHistory> sqlTuningIndexHistoryList(TuningTargetSql tuningTargetSql) throws Exception;

	List<LinkedHashMap<String, Object>> tuningHistoryList4Excel(TuningTargetSql tuningTargetSql);

	int tuningHistoryCount(TuningTargetSql tuningTargetSql);
}
