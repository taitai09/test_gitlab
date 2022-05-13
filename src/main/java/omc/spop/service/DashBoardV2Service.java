package omc.spop.service;

import java.util.List;

import omc.spop.model.DashboardV2Left;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.Result;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public interface DashBoardV2Service {
//left	
	List<DashboardV2Left> totalCntGrade(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> cntGradePerDb(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> reloadDbCheckResultGrid01(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> listGradeForDb(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> listSqlAppCheckDb(DashboardV2Left dbCheck) throws Exception;

	Result chartSqlAppCheckDb(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> listSqlAppDiagStatus(DashboardV2Left dbCheck) throws Exception;

	List<DashboardV2Left> listTopSqlPerDb(DashboardV2Left param) throws Exception;

	List<DashboardV2Left> listTopSql(DashboardV2Left param) throws Exception;

	List<DashboardV2Left> chartTopSql(DashboardV2Left param) throws Exception;

//right
	List<ResourceLimitPrediction> getResourceLimitPointPredictionList(ResourceLimitPrediction resourceLimitPrediction)
			throws Exception;

	List<TablespaceLimitPoint> getTablespacePresentConditionDBList(TablespaceLimitPoint tablespaceLimitPoint)
			throws Exception;

	List<TablespaceLimitPoint> getTablespacePresentConditionList(TablespaceLimitPoint tablespaceLimitPoint)
			throws Exception;

	List<SequenceLimitPoint> getSequencePresentConditionDBList(SequenceLimitPoint sequenceLimitPoint) throws Exception;

	List<SequenceLimitPoint> getSequencePresentConditionList(SequenceLimitPoint sequenceLimitPoint) throws Exception;

	List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction)
			throws Exception;

	List<NewAppTimeoutPrediction> getNewAppTimeoutPredictList(NewAppTimeoutPrediction newAppTimeoutPrediction)
			throws Exception;

	List<ResourceLimitPrediction> getResourceLimitPointPredictionChart(ResourceLimitPrediction resourceLimitPrediction)
			throws Exception;

	Result getResourceLimitPointPredictionChartResult(ResourceLimitPrediction resourceLimitPrediction) throws Exception;

	List<TablespaceLimitPoint> getTablespacePresentConditionChart(TablespaceLimitPoint tablespaceLimitPoint)
			throws Exception;

	Result getTablespacePresentConditionChartResult(TablespaceLimitPoint tablespaceLimitPoint) throws Exception;

	Result getSequencePresentConditionChartResult(SequenceLimitPoint sequenceLimitPoint) throws Exception;

	List<SequenceLimitPoint> getSequencePresentConditionChart(SequenceLimitPoint sequenceLimitPoint) throws Exception;

	List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionChart(NewSQLTimeoutPrediction newSQLTimeoutPrediction)
			throws Exception;

	List<NewAppTimeoutPrediction> getNewAppTimeoutPredictionChart(NewAppTimeoutPrediction newAppTimeoutPrediction)
			throws Exception;

	List<DashboardV2Left> chartTopSql2(DashboardV2Left dashboardV2Left);
}
