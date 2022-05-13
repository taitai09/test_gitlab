package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AccPathExec;
import omc.spop.model.ApmApplSql;
import omc.spop.model.DbIndexHistory;
import omc.spop.model.DbTabColumnHistory;
import omc.spop.model.DbaObjects;
import omc.spop.model.DbaTables;
import omc.spop.model.IdxAdMst;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.SelftunExecutionPlan;
import omc.spop.model.SelftunExecutionPlan1;
import omc.spop.model.SelftunSql;
import omc.spop.model.SelftunSqlBind;
import omc.spop.model.SelftunSqlStatistics;

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SelfTuningDao {
	public List<ApmApplSql> databaseListOfWrkjobCd(ApmApplSql apmApplSql);
	
	public List<ApmApplSql> selfTuningList(ApmApplSql apmApplSql);

	public String getMaxSelftunQuerySeq(ApmApplSql apmApplSql);

	public int insertSelftunQuery(SelftunSql selftunSql);

	public List<IdxAdMst> idxAdMstList(IdxAdMst idxAdMst);
	
	public List<LinkedHashMap<String, Object>> excelDownloadIdxAdMstList(IdxAdMst idxAdMst);

	public int insertSelftunSqlStatistics(SelftunSqlStatistics selftunSqlStatistics);

	public int insertSelftunSqlBind(SelftunSqlBind selftunSqlBind);

	public List<AccPathExec> accpathList(DbaTables dbaTables);

	public List<DbTabColumnHistory> columnHistoryList(DbaTables dbaTables);

	public List<DbIndexHistory> indexHistoryList(DbaTables dbaTables);

	public List<DbaObjects> explainPlanStatisticsPopup(DbaObjects dbaObjects);

	public SelftunSql getSelftunSql(SelftunSql selftunSql);

	public List<SelftunSqlBind> selectSelftunSqlBindList(SelftunSql selftunSql);

	public List<ApmApplSql> selectPerfCheckResultList(ApmApplSql apmApplSql);

	public List<PerformanceCheckMng> selectPerfCheckResultList(SelftunExecutionPlan selftunExecutionPlan);

	public List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(SelftunExecutionPlan selftunExecutionPlan);

	public List<PerformanceCheckMng> selectImprovementGuideList(SelftunExecutionPlan selftunExecutionPlan);

	public List<PerformanceCheckMng> selectPerfCheckResultBasisWhy(SelftunExecutionPlan selftunExecutionPlan);

	public List<SelftunExecutionPlan1> executionPlan1(SelftunExecutionPlan1 selftunExecutionPlan1);
}
