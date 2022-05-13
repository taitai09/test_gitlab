package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.TuningHistoryDao;
import omc.spop.model.SqlTuningIndexHistory;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.service.TuningHistoryService;

/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("TuningHistoryService")
public class TuningHistoryServiceImpl implements TuningHistoryService {
	@Autowired
	private TuningHistoryDao tuningHistoryDao;
	
	@Override
	public List<TuningTargetSql> tuningHistoryList(TuningTargetSql tuningTargetSql) throws Exception {
		return tuningHistoryDao.tuningHistoryList(tuningTargetSql);
	}
	
	@Override
	public TuningTargetSql readSqlDetail(TuningTargetSql tuningTargetSql) throws Exception {
		return tuningHistoryDao.readSqlDetail(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql) throws Exception {
		return tuningHistoryDao.bindSetList(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql) throws Exception {
		return tuningHistoryDao.sqlBindList(tuningTargetSql);
	}

	@Override
	public List<SqlTuningIndexHistory> sqlTuningIndexHistoryList(TuningTargetSql tuningTargetSql) throws Exception {
		return tuningHistoryDao.sqlTuningIndexHistoryList(tuningTargetSql);
	}

	@Override
	public List<LinkedHashMap<String, Object>> tuningHistoryList4Excel(TuningTargetSql tuningTargetSql) {
		return tuningHistoryDao.tuningHistoryList4Excel(tuningTargetSql);
	}

	@Override
	public int tuningHistoryCount(TuningTargetSql tuningTargetSql) {
		return tuningHistoryDao.tuningHistoryCount(tuningTargetSql);
	}	
}
