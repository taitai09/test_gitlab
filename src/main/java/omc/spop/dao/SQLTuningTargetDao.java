package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.SqlTuning;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.model.TopsqlHandopChoice;
import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2018.03.12 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SQLTuningTargetDao {
	public List<TopsqlAutoChoice> autoSelectionList(TopsqlAutoChoice topsqlAutoChoice);

	public List<LinkedHashMap<String, Object>> autoSelectionList4Excel(TopsqlAutoChoice topsqlAutoChoice);

	public String getMaxAutoChoiceCondNo(TopsqlAutoChoice topsqlAutoChoice);

	public TopsqlAutoChoice selectAutoChoiceCond(TopsqlAutoChoice topsqlAutoChoice);

	public void insertAutoChoiceCond(TopsqlAutoChoice topsqlAutoChoice);

	public int updateAutoChoiceCond(TopsqlAutoChoice topsqlAutoChoice);

	public void insertAutoChoiceCondLog(TopsqlAutoChoice topsqlAutoChoice);

	public List<TopsqlAutoChoice> autoSelectionHistoryList(TopsqlAutoChoice topsqlAutoChoice);
	
	public List<TopsqlAutoChoice> getChoiceCondNo(TopsqlAutoChoice topsqlAutoChoice);

	public List<TopsqlAutoChoice> autoSelectionStatusList(TopsqlAutoChoice topsqlAutoChoice);
	public List<LinkedHashMap<String, Object>> autoSelectionStatusList4Excel(TopsqlAutoChoice topsqlAutoChoice);
	public List<LinkedHashMap<String, Object>> autoSelectionStatusSearchList4Excel(TopsqlAutoChoice topsqlAutoChoice);

	public List<TuningTargetSql> autoSelectionStatusDetailList(TuningTargetSql tuningTargetSql);
	public List<LinkedHashMap<String, Object>> autoSelectionStatusDetailList4Excel(TuningTargetSql tuningTargetSql);

	public List<OdsHistSqlstat> manualSelectionList(OdsHistSqlstat odsHistSqlstat);
	
	public List<LinkedHashMap<String, Object>> manualSelectionList4Excel(OdsHistSqlstat odsHistSqlstat);

	public List<TopsqlHandopChoice> manualSelectionStatusList(TopsqlHandopChoice topsqlHandopChoice);

	public List<TuningTargetSql> manualSelectionStatusDetailList(TuningTargetSql tuningTargetSql);

	public List<OdsHistSnapshot> manualSelectionHistoryList(OdsHistSnapshot odsHistSnapshot);

	public void updateTuningTargetSql(TuningTargetSql tuningTargetSql);

	public void insertTuningTargetSql(TuningTargetSql tuningTargetSql);

	public List<TuningTargetSql> perfrIdAssignCountList(TuningTargetSql tuningTargetSql);

	public TuningTargetSql selectTuningTargetSql(TuningTargetSql tuningTargetSql);

	public String getMaxChoiceTms(TuningTargetSql tuningTargetSql);

	public int insertTopsqlHandopChoiceExec(TopsqlHandopChoice topsqlHandopChoice);

	public void updateTuningTargetSqlStatus(SqlTuning sqlTuning);

	public void mergeSqlTuning(SqlTuning sqlTuning);

	public void insertSqlTuningStatusHistory(SqlTuning sqlTuning);

	public List<TuningTargetSql> autoSelectionStatusTuningNoList(SqlTuning sqlTuning);

	public void updateTuningTargetSqlStatusBundle(SqlTuning sqlTuning);

	public void mergeSqlTuningBundle(SqlTuning sqlTuning);

	public void insertSqlTuningStatusBundleHistory(SqlTuning sqlTuning);
	
	public List<LinkedHashMap<String, Object>> manualSelectionStatusListByExcelDown(
			TopsqlHandopChoice topsqlHandopChoice);

	public void insertTuningTargetSqlByProject(TuningTargetSql temp);


}
