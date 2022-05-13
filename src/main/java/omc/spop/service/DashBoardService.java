package omc.spop.service;

import java.util.List;

import omc.spop.model.ApplicationCheckCombined;
import omc.spop.model.Database;
import omc.spop.model.DbCheck;
import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbEmergencyAction;
import omc.spop.model.ObjectChange;
import omc.spop.model.PerfImprCondition;
import omc.spop.model.PerfImprWorkCond;
import omc.spop.model.ReorgTargetCondition;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.SqlCheckCombined;
import omc.spop.model.WrkJobCd;

/***********************************************************
 * 2018.08.21 DashBoard
 **********************************************************/

public interface DashBoardService {
	List<ApplicationCheckCombined> getApplicationCheckCombined(ApplicationCheckCombined applicationCheckCombined) throws Exception;

	List<WrkJobCd> getAppPerfCheck(WrkJobCd wrkJobCd) throws Exception;

	String getMaxCheckDay() throws Exception;
	List<DbCheck> getDbCheckResult(DbCheck dbCheck) throws Exception;

	List<ObjectChange> getObjectChangeCheckCombined(ObjectChange objectChange) throws Exception;

	List<ObjectChange> getObjectChangeCondition(ObjectChange objectChange) throws Exception;

	List<PerfImprWorkCond> getPerfImprWorkCondition(PerfImprWorkCond perfImprWorkCond) throws Exception;

	List<Database> getIncompleteTuningList(Database database) throws Exception;

	List<ReorgTargetCondition> getReorgTargetCond(ReorgTargetCondition reorgTargetCondition) throws Exception;

	List<ResourceLimitPrediction> getResourceLimitPredict(ResourceLimitPrediction resourceLimitPrediction)
			throws Exception;

	List<ResourceLimitPrediction> getResourceLimitPredictCombined(ResourceLimitPrediction resourceLimitPrediction)
			throws Exception;

	/* Dashboard - SQL진단(통합) */
	List<SqlCheckCombined> getSqlCheckCombined(SqlCheckCombined sqlCheckCombined) throws Exception;

	List<WrkJobCd> getSqlPerfCheck(WrkJobCd wrkJobCd) throws Exception;

	List<DbCheckConfig> getUrgentActionCondition(DbCheckConfig dbCheckConfig) throws Exception;

	List<DbEmergencyAction> getUrgentActionTargetCondCombined(DbEmergencyAction dbEmergencyAction) throws Exception;

	List<DbCheckConfig> getBasicCheckPref() throws Exception;

	List<PerfImprCondition> getPerfImprCondition(Database database) throws Exception;

	String getCheckDateTopsqlDiagSummary() throws Exception;

	String getGatherDayDash() throws Exception;

	String getMaxGatherDay() throws Exception;

	String getMaxCheckDayDash() throws Exception;

	String getMaxBaseDay() throws Exception;

}
