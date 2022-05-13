package omc.spop.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.DashBoardDao;
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
import omc.spop.service.DashBoardService;

/***********************************************************
 * 2018.08.21 DashBoard
 **********************************************************/

@Service("DashBoardService")
public class DashBoardServiceImpl implements DashBoardService {

	private static final Logger logger = LoggerFactory.getLogger(DashBoardServiceImpl.class);

	@Autowired
	private DashBoardDao dashBoardDao;

	@Override
	public List<ApplicationCheckCombined> getApplicationCheckCombined(ApplicationCheckCombined param) throws Exception {

		return dashBoardDao.getApplicationCheckCombined(param);
	}

	@Override
	public List<WrkJobCd> getAppPerfCheck(WrkJobCd param) throws Exception {

		return dashBoardDao.getAppPerfCheck(param);
	}

	@Override
	public List<DbCheck> getDbCheckResult(DbCheck param) throws Exception {
		return dashBoardDao.getDbCheckResult(param);
	}

	@Override
	public String getMaxCheckDay() throws Exception {
		return dashBoardDao.getMaxCheckDay();
	}

	@Override
	public List<ObjectChange> getObjectChangeCheckCombined(ObjectChange param) throws Exception {

		return dashBoardDao.getObjectChangeCheckCombined(param);
	}

	@Override
	public List<ObjectChange> getObjectChangeCondition(ObjectChange param) throws Exception {

		return dashBoardDao.getObjectChangeCondition(param);
	}

	@Override
	public List<PerfImprWorkCond> getPerfImprWorkCondition(PerfImprWorkCond param) throws Exception {
		return dashBoardDao.getPerfImprWorkCondition(param);
	}

	@Override
	public List<Database> getIncompleteTuningList(Database database) throws Exception {
		return dashBoardDao.getIncompleteTuningList(database);
	}

	@Override
	public List<ReorgTargetCondition> getReorgTargetCond(ReorgTargetCondition param) throws Exception {

		return dashBoardDao.getReorgTargetCond(param);
	}

	@Override
	public List<ResourceLimitPrediction> getResourceLimitPredict(ResourceLimitPrediction param) throws Exception {

		return dashBoardDao.getResourceLimitPredict(param);
	}

	@Override
	public List<ResourceLimitPrediction> getResourceLimitPredictCombined(ResourceLimitPrediction param)
			throws Exception {

		return dashBoardDao.getResourceLimitPredictCombined(param);
	}

	/* Dashboard - SQL진단(통합) */
	@Override
	public List<SqlCheckCombined> getSqlCheckCombined(SqlCheckCombined param) throws Exception {

		return dashBoardDao.getSqlCheckCombined(param);
	}

	@Override
	public List<WrkJobCd> getSqlPerfCheck(WrkJobCd param) throws Exception {

		return dashBoardDao.getSqlPerfCheck(param);
	}

	@Override
	public List<DbCheckConfig> getUrgentActionCondition(DbCheckConfig param) throws Exception {

		return dashBoardDao.getUrgentActionCondition(param);
	}

	@Override
	public List<DbEmergencyAction> getUrgentActionTargetCondCombined(DbEmergencyAction param) throws Exception {

		return dashBoardDao.getUrgentActionTargetCondCombined(param);
	}

	@Override
	public List<DbCheckConfig> getBasicCheckPref() {
		return dashBoardDao.getBasicCheckPref();
	}

	@Override
	public List<PerfImprCondition> getPerfImprCondition(Database database) throws Exception {
		return dashBoardDao.getPerfImprCondition(database);
	}

	@Override
	public String getCheckDateTopsqlDiagSummary() throws Exception {
		return dashBoardDao.getCheckDateTopsqlDiagSummary();
	}

	@Override
	public String getGatherDayDash() throws Exception {
		return dashBoardDao.getGatherDayDash();
	}

	@Override
	public String getMaxGatherDay() throws Exception {
		return dashBoardDao.getMaxGatherDay();
	}
	@Override
	public String getMaxCheckDayDash() throws Exception {
		return dashBoardDao.getMaxCheckDayDash();
	}

	@Override
	public String getMaxBaseDay() throws Exception {
		return dashBoardDao.getMaxBaseDay();
	}

}
