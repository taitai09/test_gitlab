package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.Cd;
import omc.spop.model.FullscanSql;
import omc.spop.model.NewSql;
import omc.spop.model.PerfGuide;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.SqlTuning;
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.SqlTuningHistory;
import omc.spop.model.SqlTuningStatusHistory;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSqlUnionOffloadSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.model.Users;

/***********************************************************
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 * 2020.06.03	이재우	기능개선
 **********************************************************/

public interface ImprovementManagementDao {
	public TuningTargetSql getImprovementSummary(TuningTargetSql tuningTargetSql);

	public List<TuningTargetSql> improvementStatusList(TuningTargetSql tuningTargetSql);

	public List<LinkedHashMap<String, Object>> improvementStatusList4Excel(TuningTargetSql tuningTargetSql);

	public TuningTargetSql getImprovementInfo(TuningTargetSql tuningTargetSql);

	public SqlTuning getSQLTuning(TuningTargetSql tuningTargetSql);

	public TuningTargetSql getSelection(TuningTargetSql tuningTargetSql);

	public TuningTargetSql getRequest(TuningTargetSql tuningTargetSql);

	public FullscanSql getFullScan(TuningTargetSql tuningTargetSql);

	public PlanChangeSql getPlanChange(TuningTargetSql tuningTargetSql);

	public NewSql getNewSQL(TuningTargetSql tuningTargetSql);

	public TempUsageSql getTempOver(TuningTargetSql tuningTargetSql);

	public TuningTargetSql getImprovements(TuningTargetSql tuningTargetSql);

	public List<Cd> completeReasonList();

	public List<Cd> completeReasonDetailList();

	public List<SqlImprovementType> sqlImprovementTypeList(TuningTargetSql tuningTargetSql);

	public List<SqlTuningHistory> sqlImproveHistoryList(SqlTuningHistory sqlTuningHistory);

	public SqlTuningHistory getSQLImproveHistory(SqlTuningHistory sqlTuningHistory);

	public List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql);

	public List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql);

	public String getNextTuningNo();

	public int insertTuningTargetSql(TuningTargetSql tuningTargetSql);

	public int insertTuningTargetSql2(TuningTargetSql tuningTargetSql);

	public int updateTuningTargetSql(TuningTargetSql tuningTargetSql);

	public int updateSqlTuning(TuningTargetSql tuningTargetSql);

	public int mergeSqlTuningEnd(TuningTargetSql tuningTargetSql);

	public List<TuningTargetSql> tuningRequesterCountList(TuningTargetSql tuningTargetSql);

	public int insertTuningStatusHistory(TuningTargetSql tuningTargetSql);

	public int deleteSqlImprovementType(TuningTargetSql tuningTargetSql);

	public int insertSqlImprovementType(SqlImprovementType sqlImprovementType);

	public int deleteTuningTargetSqlBind(String tuningNo);

	public int insertTuningTargetSqlBind(TuningTargetSqlBind tuningTargetSqlBind);

	public List<Users> getDBManagerInfo(TuningTargetSql tuningTargetSql);

	public String getTuningStatusCd(TuningTargetSql tuningTargetSql);

	public String getBfacChkNo();

	public int insertBeforeAccidentCheck(BeforeAccidentCheck beforeAccidentCheck);

	public SqlTuning getSqlTuningYn(TuningTargetSql tuningTargetSql);

	public int updateSqlTuningComplete(TuningTargetSql tuningTargetSql);

	public int insertSqlTuningComplete(TuningTargetSql tuningTargetSql);

	public int insertSqlTuningHistory(TuningTargetSql tuningTargetSql);

	public PerfGuide getPerfGuide(TuningTargetSql tuningTargetSql);

	public String getMaxPerfGuideNo();

	public int insertPerfGuide(PerfGuide perfGuide);

	public List<SqlTuningStatusHistory> processHistoryList(SqlTuningStatusHistory sqlTuningStatusHistory);

	public int updateSqlTuningResult(TuningTargetSql tuningTargetSql);

	public int deleteSqlTuning(TuningTargetSql tuningTargetSql);

	public int deleteSqlTuningIndexHistory(TuningTargetSql tuningTargetSql);

	public int insertSqlTuningIndexHistory(TuningTargetSql tuningTargetSql);

	public String getSysdate();

	public int deleteSqlTuningHistory(TuningTargetSql tuningTargetSql);

	public String getTemporarySaveDt(TuningTargetSql tuningTargetSql);

//	public String getUsersWrkjobCdDbid(String wrkjob_cd);

	public int updateAfterTuningNo(TuningTargetSql tuningTargetSql);

	public TuningTargetSql getInitValues(String user_id);

	public int saveInitSetting(TuningTargetSql tuningTargetSql);

	public String getTuningCompleteDueDay();

	public int updateRejectApplication(TuningTargetSql temp);

	public int updateRejectApplication2(TuningTargetSql temp);

	public SqlTuning getImprBeforeAfter(TuningTargetSql tuningTargetSql);

	public int insertTuningTargetSqlFromPerfChkResult(TuningTargetSql tuningTargetSql);

	public int insertTuningTargetSqlBindFromPerfChkResult(TuningTargetSql tuningTargetSql);

	public String getBindSetSeq(String next_tuning_no);

	public String getProgramExecuteTms(TuningTargetSql tuningTargetSql);

	public int checkRequesterId(TuningTargetSql temp);

	public TopSqlUnionOffloadSql getTopSqlUnionOffloadSql(TuningTargetSql tuningTargetSql);

	public String getMaxTuningAttachFileSeq(TuningTargetSql tuningTargetSql);
	
	public int insertTuningAttachFile(SqlTuningAttachFile sqlTuningAttachFile);

	public List<SqlTuningAttachFile> readTuningAttachFiles(TuningTargetSql tuningTargetSql);
	
	public int deleteTuningAttachFile(SqlTuningAttachFile sqlTuningAttachFile);

	public TuningTargetSql getDeployAfterPerf(TuningTargetSql tuningTargetSql);

	public int getPerfSourceType(TuningTargetSql tuningTargetSql);

	public void insertImprbExecPlan(TuningTargetSql tuningTargetSql);

	public Users getUsersInfo(String users);
}