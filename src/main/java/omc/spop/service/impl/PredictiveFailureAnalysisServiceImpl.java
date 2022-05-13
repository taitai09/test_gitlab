package omc.spop.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import ch.qos.logback.classic.Logger;
import omc.spop.base.SessionManager;
import omc.spop.dao.PredictiveFailureAnalysisDao;
import omc.spop.model.CPUIncreaseUsage;
import omc.spop.model.CPULimitPrediction;
import omc.spop.model.CPULimitPredictionDetail;
import omc.spop.model.MEMORYLimitPrediction;
import omc.spop.model.MEMORYLimitPredictionDetail;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewAppTimeoutPredictionUpdate;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPredictionUpdate;
import omc.spop.model.RegularSQLFilterCase;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;
import omc.spop.model.UnknownSQLFaultPrediction;
import omc.spop.service.PredictiveFailureAnalysisService;

/***********************************************************
 * 2018.06.07 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Service("PredictiveFailureAnalysisService")
public class PredictiveFailureAnalysisServiceImpl implements PredictiveFailureAnalysisService {
	private static final Logger logger = LoggerFactory.getLogger(PredictiveFailureAnalysisServiceImpl.class);
	
	@Autowired
	private PredictiveFailureAnalysisDao predictiveFailureAnalysisDao;
	
	@Override
	public List<CPUIncreaseUsage> cpuIncreaseUsageChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		cpuIncreaseUsage.setUser_id(user_id);

		return predictiveFailureAnalysisDao.cpuIncreaseUsageChartList(cpuIncreaseUsage);
	}

	@Override
	public List<CPUIncreaseUsage> cpuUsageChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		return predictiveFailureAnalysisDao.cpuUsageChartList(cpuIncreaseUsage);
	}

	@Override
	public List<CPUIncreaseUsage> userTimeChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		return predictiveFailureAnalysisDao.userTimeChartList(cpuIncreaseUsage);
	}

	@Override
	public List<CPUIncreaseUsage> sysTimeChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		return predictiveFailureAnalysisDao.sysTimeChartList(cpuIncreaseUsage);
	}

	@Override
	public List<CPUIncreaseUsage> timeModelChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		return predictiveFailureAnalysisDao.timeModelChartList(cpuIncreaseUsage);
	}

	@Override
	public List<CPUIncreaseUsage> topSQLList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception {
		return predictiveFailureAnalysisDao.topSQLList(cpuIncreaseUsage);
	}

	@Override
	public List<CPULimitPrediction> cpuLimitPointPredictionList(CPULimitPrediction cpuLimitPrediction)
			throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		cpuLimitPrediction.setUser_id(user_id);

		return predictiveFailureAnalysisDao.cpuLimitPointPredictionList(cpuLimitPrediction);
	}

	@Override
	public List<CPULimitPredictionDetail> cpuLimitPredictionChartList(CPULimitPredictionDetail cpuLimitPredictionDetail)
			throws Exception {
		return predictiveFailureAnalysisDao.cpuLimitPredictionChartList(cpuLimitPredictionDetail);
	}
	
	@Override
	public List<MEMORYLimitPrediction> memoryLimitPointPredictionList(MEMORYLimitPrediction memoryLimitPrediction)
			throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		memoryLimitPrediction.setUser_id(user_id);

		return predictiveFailureAnalysisDao.memoryLimitPointPredictionList(memoryLimitPrediction);
	}

	@Override
	public List<MEMORYLimitPredictionDetail> memoryLimitPredictionChartList(MEMORYLimitPredictionDetail memoryLimitPredictionDetail)
			throws Exception {
		return predictiveFailureAnalysisDao.memoryLimitPredictionChartList(memoryLimitPredictionDetail);
	}

	/** Sequence 한계점예측 - 챠트 리스트 */
	@Override
	public List<SequenceLimitPoint> sequenceLimitPointPredictionChartList(SequenceLimitPoint sequenceLimitPoint)
			throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.sequenceLimitPointPredictionChartList(sequenceLimitPoint);
	}

	@Override
	public List<SequenceLimitPoint> sequenceLimitPointArrivalList(SequenceLimitPoint sequenceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.sequenceLimitPointArrivalList(sequenceLimitPoint);
	}

	@Override
	public List<Map<String, Object>> sequenceLimitPointArrivalMapList(Map<String, Object> param) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		param.put("ser_id", user_id);
		return predictiveFailureAnalysisDao.sequenceLimitPointArrivalMapList(param);
	}

	@Override
	public List<SequenceLimitPoint> sequenceLimitPointPredictionDetailList(SequenceLimitPoint sequenceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.sequenceLimitPointPredictionDetailList(sequenceLimitPoint);
	}

	@Override
	public List<SequenceLimitPoint> sequenceLimitPointPredictionDetailChartList(SequenceLimitPoint sequenceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.sequenceLimitPointPredictionDetailChartList(sequenceLimitPoint);
	}

	@Override
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionChartList(
			TablespaceLimitPoint tablespaceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.tablespaceLimitPointPredictionChartList(tablespaceLimitPoint);
	}

	@Override
	public List<TablespaceLimitPoint> tablespaceLimitPointArrivalList(TablespaceLimitPoint tablespaceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.tablespaceLimitPointArrivalList(tablespaceLimitPoint);
	}

	@Override
	public List<Map<String, Object>> tablespaceLimitPointArrivalMapList(Map<String, Object> param) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		param.put("user_id", user_id);
		return predictiveFailureAnalysisDao.tablespaceLimitPointArrivalMapList(param);
	}

	@Override
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailList(
			TablespaceLimitPoint tablespaceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.tablespaceLimitPointPredictionDetailList(tablespaceLimitPoint);
	}

	@Override
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailChartList(
			TablespaceLimitPoint tablespaceLimitPoint) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
		return predictiveFailureAnalysisDao.tablespaceLimitPointPredictionDetailChartList(tablespaceLimitPoint);
	}

	@Override
	public List<NewAppTimeoutPrediction> newAppTimeoutPredictionList(NewAppTimeoutPrediction newAppTimeoutPrediction) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newAppTimeoutPrediction.setUser_id(user_id);
		return predictiveFailureAnalysisDao.newAppTimeoutPredictionList(newAppTimeoutPrediction);
	}
	
	@Override
	public List<NewAppTimeoutPrediction> newAppTimeoutPredictionChartList(
			NewAppTimeoutPrediction newAppTimeoutPrediction) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newAppTimeoutPrediction.setUser_id(user_id);
		return predictiveFailureAnalysisDao.newAppTimeoutPredictionChartList(newAppTimeoutPrediction);
	}

	@Override
	public int newAppTimeoutPredictionExceptYnUpdate(NewAppTimeoutPredictionUpdate newAppTimeoutPredictionUpdate) {
		return predictiveFailureAnalysisDao.newAppTimeoutPredictionExceptYnUpdate(newAppTimeoutPredictionUpdate);
	}

	@Override
	public List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newSQLTimeoutPrediction.setUser_id(user_id);
		return predictiveFailureAnalysisDao.newSQLTimeoutPredictionList(newSQLTimeoutPrediction);
	}
	
	@Override
	public List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionChartList(
			NewSQLTimeoutPrediction newSQLTimeoutPrediction) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newSQLTimeoutPrediction.setUser_id(user_id);
		return predictiveFailureAnalysisDao.newSQLTimeoutPredictionChartList(newSQLTimeoutPrediction);
	}

	@Override
	public int newSQLTimeoutPredictionExceptYnUpdate(NewSQLTimeoutPredictionUpdate newSQLTimeoutPredictionUpdate) {
		return predictiveFailureAnalysisDao.newSQLTimeoutPredictionExceptYnUpdate(newSQLTimeoutPredictionUpdate);
	}
	
	@Override
	public List<UnknownSQLFaultPrediction> unknownSQLFaultPredictionChartList(Map param) {
		return predictiveFailureAnalysisDao.unknownSQLFaultPredictionChartList(param);
	}
	
	@Override
	public List<RegularSQLFilterCase> selectParsingSchemaNameComboBox(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.selectParsingSchemaNameComboBox(regularSQLFilterCase);
	}
	
	@Override
	public List<RegularSQLFilterCase> selectRegularSQLParsingSchemaNameFilteringCase(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.selectRegularSQLParsingSchemaNameFilteringCase(regularSQLFilterCase);
	}
	
	@Override
	public int insertRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.insertRegularSQLTargetUser(regularSQLFilterCase);
	}
	
	@Override
	public int deleteRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.deleteRegularSQLTargetUser(regularSQLFilterCase);
	}
	
	@Override
	public List<RegularSQLFilterCase> selectRegularSQLModuleFilterCombobox() {
		
		return predictiveFailureAnalysisDao.selectRegularSQLModuleFilterCombobox();
	}
	
	@Override
	public List<RegularSQLFilterCase> selectRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.selectRegularSQLModuleFilterCase(regularSQLFilterCase);
	}
	
	@Override
	public List<RegularSQLFilterCase> checkRegisteredRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.checkRegisteredRegularSQLModuleFilterCase(regularSQLFilterCase);
	}
	
	@Override
	public int insertRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.insertRegularSQLModuleFilterCase(regularSQLFilterCase);
	}
	
	@Override
	public int deleteRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase) {
		return predictiveFailureAnalysisDao.deleteRegularSQLModuleFilterCase(regularSQLFilterCase);
	}

}