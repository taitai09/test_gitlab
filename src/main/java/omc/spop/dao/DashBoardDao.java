package omc.spop.dao;

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

public interface DashBoardDao {

	public List<ApplicationCheckCombined> getApplicationCheckCombined(ApplicationCheckCombined param);

	public List<WrkJobCd> getAppPerfCheck(WrkJobCd param);

	public List<DbCheck> getDbCheckResult(DbCheck param);


	public List<ObjectChange> getObjectChangeCheckCombined(ObjectChange param);

	public List<ObjectChange> getObjectChangeCondition(ObjectChange param);

	public List<PerfImprWorkCond> getPerfImprWorkCondition(PerfImprWorkCond param);

	public List<ReorgTargetCondition> getReorgTargetCond(ReorgTargetCondition param);

	public List<ResourceLimitPrediction> getResourceLimitPredict(ResourceLimitPrediction param);

	public List<ResourceLimitPrediction> getResourceLimitPredictCombined(ResourceLimitPrediction param);

	/* Dashboard - SQL진단(통합) */
	public List<SqlCheckCombined> getSqlCheckCombined(SqlCheckCombined param);

	public List<WrkJobCd> getSqlPerfCheck(WrkJobCd param);

	public List<DbCheckConfig> getUrgentActionCondition(DbCheckConfig param);

	public List<DbEmergencyAction> getUrgentActionTargetCondCombined(DbEmergencyAction param);

	public List<DbCheckConfig> getBasicCheckPref();

	public List<Database> getIncompleteTuningList(Database database);

	public List<PerfImprCondition> getPerfImprCondition(Database database);

	public String getCheckDateTopsqlDiagSummary();

	public String getGatherDayDash();

	public String getMaxGatherDay();
	
	public String getMaxCheckDay();

	public String getMaxCheckDayDash();

	public String getMaxBaseDay();
}
