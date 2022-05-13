package omc.spop.service;

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

public interface PredictiveFailureAnalysisService {
	/** CPU 사용량 증가 챠트 리스트 */
	List<CPUIncreaseUsage> cpuIncreaseUsageChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;
	
	/** CPU 사용량 증가 상세 - CPU Usage 챠트 리스트 */
	List<CPUIncreaseUsage> cpuUsageChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;
	
	/** CPU 사용량 증가 상세 - User Time 챠트 리스트 */
	List<CPUIncreaseUsage> userTimeChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;
	
	/** CPU 사용량 증가 상세 - SYS Time 챠트 리스트 */
	List<CPUIncreaseUsage> sysTimeChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;	
	
	/** CPU 사용량 증가 상세 - Time Model 챠트 리스트 */
	List<CPUIncreaseUsage> timeModelChartList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;
	
	/** CPU 사용량 증가 상세 - TOP SQL 리스트 */
	List<CPUIncreaseUsage> topSQLList(CPUIncreaseUsage cpuIncreaseUsage) throws Exception;	
	
	/** CPU 한계점 예측 - 리스트  */
	List<CPULimitPrediction> cpuLimitPointPredictionList(CPULimitPrediction cpuLimitPrediction) throws Exception;
	
	/** CPU 한계점 예측 - 상세 - 한계점 챠트 리스트  */
	List<CPULimitPredictionDetail> cpuLimitPredictionChartList(CPULimitPredictionDetail cpuLimitPredictionDetail) throws Exception;
	
	/** MEMORY 한계점 예측 - 리스트  */
	List<MEMORYLimitPrediction> memoryLimitPointPredictionList(MEMORYLimitPrediction memoryLimitPrediction) throws Exception;
	
	/** MEMORY 한계점 예측 - 상세 - 한계점 챠트 리스트  */
	List<MEMORYLimitPredictionDetail> memoryLimitPredictionChartList(MEMORYLimitPredictionDetail memoryLimitPredictionDetail) throws Exception;

	/** Sequence 한계점예측 - 챠트 리스트 */
	List<SequenceLimitPoint> sequenceLimitPointPredictionChartList(SequenceLimitPoint sequenceLimitPoint) throws Exception;
	List<SequenceLimitPoint> sequenceLimitPointArrivalList(SequenceLimitPoint sequenceLimitPoint);	
	List<Map<String,Object>> sequenceLimitPointArrivalMapList(Map<String,Object> param);	
	List<SequenceLimitPoint> sequenceLimitPointPredictionDetailList(SequenceLimitPoint sequenceLimitPoint);
	List<SequenceLimitPoint> sequenceLimitPointPredictionDetailChartList(SequenceLimitPoint sequenceLimitPoint);
	
	/** Tablespace 한계점예측 */
	List<TablespaceLimitPoint> tablespaceLimitPointPredictionChartList(TablespaceLimitPoint tablespaceLimitPoint);
	List<TablespaceLimitPoint> tablespaceLimitPointArrivalList(TablespaceLimitPoint tablespaceLimitPoint);	
	List<Map<String,Object>> tablespaceLimitPointArrivalMapList(Map<String,Object> param);
	List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailList(TablespaceLimitPoint tablespaceLimitPoint);	
	List<TablespaceLimitPoint> tablespaceLimitPointPredictionDetailChartList(TablespaceLimitPoint tablespaceLimitPoint);
	
	/** 신규 APP 타임아웃 예측  */
	List<NewAppTimeoutPrediction> newAppTimeoutPredictionList(NewAppTimeoutPrediction newAppTimeoutPrediction);
	List<NewAppTimeoutPrediction> newAppTimeoutPredictionChartList(NewAppTimeoutPrediction newAppTimeoutPrediction);
	int newAppTimeoutPredictionExceptYnUpdate(NewAppTimeoutPredictionUpdate newAppTimeoutPredictionUpdate);
	
	/** 신규 SQL 타임아웃 예측  */
	List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction);
	List<NewSQLTimeoutPrediction> newSQLTimeoutPredictionChartList(NewSQLTimeoutPrediction newSQLTimeoutPrediction);
	int newSQLTimeoutPredictionExceptYnUpdate(NewSQLTimeoutPredictionUpdate newSQLTimeoutPredictionUpdate);
	
	/** Unknown SQL 장애 예측 */
	List<UnknownSQLFaultPrediction> unknownSQLFaultPredictionChartList(Map param);
	
	/** 정규 SQL 필터링 조건 */
	List<RegularSQLFilterCase> selectParsingSchemaNameComboBox(RegularSQLFilterCase regularSQLFilterCase);
	List<RegularSQLFilterCase> selectRegularSQLParsingSchemaNameFilteringCase(RegularSQLFilterCase regularSQLFilterCase);
	int insertRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase);
	int deleteRegularSQLTargetUser(RegularSQLFilterCase regularSQLFilterCase);
	
	List<RegularSQLFilterCase> selectRegularSQLModuleFilterCombobox();
	List<RegularSQLFilterCase> selectRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	List<RegularSQLFilterCase> checkRegisteredRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	int insertRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
	int deleteRegularSQLModuleFilterCase(RegularSQLFilterCase regularSQLFilterCase);
}
