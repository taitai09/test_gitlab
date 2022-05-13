package omc.spop.dao;

import java.util.List;

import omc.spop.model.DashboardV2Left;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/

public interface DashBoardV2Dao {
	// Left
	public List<DashboardV2Left> totalCntGrade(DashboardV2Left param);
	
	public List<DashboardV2Left> cntGradePerDb(DashboardV2Left param);
	
	public List<DashboardV2Left> reloadDbCheckResultGrid01(DashboardV2Left param);
	
	public List<DashboardV2Left> listGradeForDb(DashboardV2Left param);
	
	public List<DashboardV2Left> listSqlAppCheckDb(DashboardV2Left param);
	
	public List<DashboardV2Left> chartSqlAppCheckDb(DashboardV2Left param);
	
	public List<DashboardV2Left> chartLegendSqlAppCheckDb(DashboardV2Left param);
	
	public List<DashboardV2Left> listSqlAppDiagStatus(DashboardV2Left param);
	
	public List<DashboardV2Left> listTopSqlPerDb(DashboardV2Left param);
	
	public List<DashboardV2Left> listTopSql(DashboardV2Left param);
	
	public List<DashboardV2Left> chartTopSql(DashboardV2Left param);
	
	// Right
	List<ResourceLimitPrediction> getResourceLimitPointPredictionList(ResourceLimitPrediction resourceLimitPrediction);

	List<TablespaceLimitPoint> getTablespacePresentConditionDBList(TablespaceLimitPoint tablespaceLimitPoint);

	List<TablespaceLimitPoint> getTablespacePresentConditionList(TablespaceLimitPoint tablespaceLimitPoint);

	List<TablespaceLimitPoint> getTablespacePresentConditionChartLegendList(TablespaceLimitPoint tablespaceLimitPoint);

	List<SequenceLimitPoint> getSequencePresentConditionDBList(SequenceLimitPoint sequenceLimitPoint);

	List<SequenceLimitPoint> getSequencePresentConditionList(SequenceLimitPoint sequenceLimitPoint);

	List<TablespaceLimitPoint> getSequencePresentConditionLegendList(SequenceLimitPoint sequenceLimitPoint);

	List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction);

	List<NewAppTimeoutPrediction> getNewAppTimeoutPredictList(NewAppTimeoutPrediction newAppTimeoutPrediction);

	List<ResourceLimitPrediction> getResourceLimitPointPredictionChart(ResourceLimitPrediction resourceLimitPrediction);

	List<TablespaceLimitPoint> getTablespacePresentConditionChart(TablespaceLimitPoint tablespaceLimitPoint);

	List<SequenceLimitPoint> getSequencePresentConditionChart(SequenceLimitPoint sequenceLimitPoint);

	List<SequenceLimitPoint> getSequencePresentConditionChartLegendList(SequenceLimitPoint sequenceLimitPoint);

	List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionChart(NewSQLTimeoutPrediction newSQLTimeoutPrediction);

	List<NewAppTimeoutPrediction> getNewAppTimeoutPredictionChart(NewAppTimeoutPrediction newAppTimeoutPrediction);

	List<ResourceLimitPrediction> getResourceLimitPointPredictionChartLegendList(
			ResourceLimitPrediction resourceLimitPrediction);

	public List<DashboardV2Left> chartTopSql2(DashboardV2Left dashboardV2Left);
}
