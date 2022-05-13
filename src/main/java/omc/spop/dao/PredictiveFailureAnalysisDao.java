package omc.spop.dao;

import java.util.List;
import java.util.Map;

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

/***********************************************************
 * 2018.06.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface PredictiveFailureAnalysisDao {	
	public List<CPUIncreaseUsage> cpuIncreaseUsageChartList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPUIncreaseUsage> cpuUsageChartList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPUIncreaseUsage> userTimeChartList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPUIncreaseUsage> sysTimeChartList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPUIncreaseUsage> timeModelChartList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPUIncreaseUsage> topSQLList(CPUIncreaseUsage cpuIncreaseUsage);
	
	public List<CPULimitPrediction> cpuLimitPointPredictionList(CPULimitPrediction cpuLimitPrediction);
	
	public List<CPULimitPredictionDetail> cpuLimitPredictionChartList(CPULimitPredictionDetail cpuLimitPredictionDetail);

	public List<MEMORYLimitPrediction> memoryLimitPointPredictionList(MEMORYLimitPrediction memoryLimitPrediction);
	
	public List<MEMORYLimitPredictionDetail> memoryLimitPredictionChartList(MEMORYLimitPredictionDetail memoryLimitPredictionDetail);

	public List<SequenceLimitPoint> sequenceLimitPointPredictionChartList(SequenceLimitPoint sequenceLimitPoint);
	public List<SequenceLimitPoint> sequenceLimitPointArrivalList(SequenceLimitPoint sequenceLimitPoint);
	public List<Map<String, Object>> sequenceLimitPointArrivalMapList(Map<String, Object> param);
	public List<SequenceLimitPoint> sequenceLimitPointPredictionDetailList(SequenceLimitPoint sequenceLimitPoint);
	public List<SequenceLimitPoint> sequenceLimitPointPredictionDetailChartList(SequenceLimitPoint sequenceLimitPoint);
	
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionChartList(TablespaceLimitPoint tablespaceLimitPoint);
	public List<TablespaceLimitPoint> tablespaceLimitPointArrivalList(TablespaceLimitPoint tablespaceLimitPoint);
	public List<Map<String, Object>> tablespaceLimitPointArrivalMapList(Map<String, Object> param);
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailList(TablespaceLimitPoint tablespaceLimitPoint);
	public List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailChartList(TablespaceLimitPoint tablespaceLimitPoint);
	
	public List<NewAppTimeoutPrediction> newAppTimeoutPredictionList(NewAppTimeoutPrediction newAppTimeoutPrediction);
	public List<NewAppTimeoutPrediction> newAppTimeoutPredictionChartList(NewAppTimeoutPrediction newAppTimeoutPrediction);
	public int newAppTimeoutPredictionExceptYnUpdate(NewAppTimeoutPredictionUpdate newAppTimeoutPredictionUpdate);
	
	public List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction);
	public List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionChartList(NewSQLTimeoutPrediction newSQLTimeoutPrediction);
	public int newSQLTimeoutPredictionExceptYnUpdate(NewSQLTimeoutPredictionUpdate newSQLTimeoutPredictionUpdate);
	
	public List<UnknownSQLFaultPrediction> unknownSQLFaultPredictionChartList(Map param);
	
	public List<RegularSQLFilterCase> selectParsingSchemaNameComboBox(RegularSQLFilterCase regularSQLFilterCase);
	public List<RegularSQLFilterCase> selectRegularSQLParsingSchemaNameFilteringCase(RegularSQLFilterCase regularSQLFilterCase);
	public int insertRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase);
	public int deleteRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase);
	
	public List<RegularSQLFilterCase> selectRegularSQLModuleFilterCombobox();
	public List<RegularSQLFilterCase> selectRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	public List<RegularSQLFilterCase> checkRegisteredRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	public int insertRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	public int deleteRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	
}
