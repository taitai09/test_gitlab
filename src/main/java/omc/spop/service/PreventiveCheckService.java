package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AlertErrorCheck;
import omc.spop.model.AsmDiskgroupCheck;
import omc.spop.model.BlockCorruptionCheck;
import omc.spop.model.ChainedRowCheck;
import omc.spop.model.ConstraintCheck;
import omc.spop.model.DailyCheck;
import omc.spop.model.DatabaseDsbCheck;
import omc.spop.model.DbCacheAdvisor;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExec;
import omc.spop.model.DbFileCheck;
import omc.spop.model.DbUserCheck;
import omc.spop.model.DiagIncident;
import omc.spop.model.DiagProblem;
import omc.spop.model.FilesystemSpaceCheck;
import omc.spop.model.FkIndexCheck;
import omc.spop.model.FraSpaceCheck;
import omc.spop.model.FraSpaceUsage;
import omc.spop.model.HmRecommendation;
import omc.spop.model.InstanceDsbCheck;
import omc.spop.model.InstanceEfficiencyCheck;
import omc.spop.model.ListenerCheck;
import omc.spop.model.LongRunningOperationCheck;
import omc.spop.model.LongRunningSchedulerCheck;
import omc.spop.model.NewCreateObjectCheck;
import omc.spop.model.ObjectCheck;
import omc.spop.model.OutstandingAlerts;
import omc.spop.model.ParameterChangeCheck;
import omc.spop.model.PgaTargetAdvisor;
import omc.spop.model.RecyclebinCheck;
import omc.spop.model.ResourceLimitCheck;
import omc.spop.model.SchedulerJobFailedCheck;
import omc.spop.model.SegmentAdvisor;
import omc.spop.model.SequenceCheck;
import omc.spop.model.SgaTargetAdvisor;
import omc.spop.model.SharedPoolAdvisor;
import omc.spop.model.SqlAutoTuningRecommendation;
import omc.spop.model.TableStatisticsCheck;
import omc.spop.model.TableStatisticsLockCheck;
import omc.spop.model.TablespaceCheck;
import omc.spop.model.UnusableIndexCheck;

/***********************************************************
 * 2018.04.18	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface PreventiveCheckService {
	/** 점검 회차 리스트 */
	List<DbCheckExec> checkSeqList(DbCheckExec dbCheckExec) throws Exception;
	List<DbCheckExec> checkSeqListAll(DbCheckExec dbCheckExec) throws Exception;	
	
	/** 일 예방 점검 요약 리스트 */
	List<DailyCheck> summaryList(DailyCheck dailyCheck) throws Exception;
	
	/** Advisor Recommendations - SQL Tuning ADVISOR 리스트 */
	List<SqlAutoTuningRecommendation> sqlTuningAdvisorList(SqlAutoTuningRecommendation sqlAutoTuningRecommendation) throws Exception;
	
	/** Advisor Recommendations - SGA ADVISOR 리스트 */
	List<SgaTargetAdvisor> sgaAdvisorList(SgaTargetAdvisor sgaTargetAdvisor) throws Exception;
	
	/** Advisor Recommendations - Buffer Cache ADVISOR 리스트 */
	List<DbCacheAdvisor> bufferCacheAdvisorList(DbCacheAdvisor dbCacheAdvisor) throws Exception;
	
	/** Advisor Recommendations - Shared Pool ADVISOR 리스트 */
	List<SharedPoolAdvisor> sharedPoolAdvisorList(SharedPoolAdvisor sharedPoolAdvisor) throws Exception;
	
	/** Advisor Recommendations - PGA ADVISOR 리스트 */
	List<PgaTargetAdvisor> pgaAdvisorList(PgaTargetAdvisor pgaTargetAdvisor) throws Exception;
	
	/** Advisor Recommendations - SEGMENT ADVISOR 리스트 */
	List<SegmentAdvisor> segmentAdvisorList(SegmentAdvisor segmentAdvisor) throws Exception;
	
	/** Advisor Recommendations - Health Monitor Recommendation 리스트 */
	List<HmRecommendation> healthMonitorList(HmRecommendation hmRecommendation) throws Exception;	
	
	/** DatabaseStatus 리스트 */
	List<DatabaseDsbCheck> databaseStatusList(DatabaseDsbCheck databaseDsbCheck) throws Exception;

	/** ExpiredGraceAccount 리스트 */
	List<DbUserCheck> expiredGraceAccountList(DbUserCheck dbUserCheck) throws Exception;

	/** ModifiedParameter 리스트 */
	List<ParameterChangeCheck> modifiedParameterList(ParameterChangeCheck parameterChangeCheck) throws Exception;

	/** NewCreatedObject 리스트 */
	List<NewCreateObjectCheck> newCreatedObjectList(NewCreateObjectCheck newCreateObjectCheck) throws Exception;

	/** InstanceStatus 리스트 */
	List<InstanceDsbCheck> instanceStatusList(InstanceDsbCheck instanceDsbCheck) throws Exception;

	/** ListenerStatus 리스트 */
	List<ListenerCheck> listenerStatusList(ListenerCheck listenerCheck) throws Exception;

	/** Dbfiles 리스트 */
	List<DbFileCheck> dbfilesList(DbFileCheck dbFileCheck) throws Exception;

	/** LibraryCacheHit 리스트 */
	List<InstanceEfficiencyCheck> libraryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** DictionaryCacheHit 리스트 */
	List<InstanceEfficiencyCheck> dictionaryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** BufferCacheHit 리스트 */
	List<InstanceEfficiencyCheck> bufferCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** LatchHit 리스트 */
	List<InstanceEfficiencyCheck> latchHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** ParseCpuToParseElapsd 리스트 */
	List<InstanceEfficiencyCheck> parseCpuToParseElapsdList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** DiskSort 리스트 */
	List<InstanceEfficiencyCheck> diskSortList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** MemoryUsage 리스트 */
	List<InstanceEfficiencyCheck> memoryUsageList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	/** ResourceLimit 리스트 */
	List<ResourceLimitCheck> resourceLimitList(ResourceLimitCheck resourceLimitCheck) throws Exception;

	/** BackgroundDumpSpace 리스트 */
	List<FilesystemSpaceCheck> backgroundDumpSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	/** ArchiveLogSpace 리스트 */
	List<FilesystemSpaceCheck> archiveLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	/** AlertLogSpace 리스트 */
	List<FilesystemSpaceCheck> alertLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	/** FraSpace 리스트 */
	List<FraSpaceCheck> fraSpaceList(FraSpaceCheck fraSpaceCheck) throws Exception;
	
	/** FraUsage 리스트 */
	List<FraSpaceUsage> fraUsageList(FraSpaceUsage fraSpaceUsage) throws Exception;	

	/** AsmDiskgroupSpace 리스트 */
	List<AsmDiskgroupCheck> asmDiskgroupSpaceList(AsmDiskgroupCheck asmDiskgroupCheck) throws Exception;

	/** Tablespace 리스트 */
	List<TablespaceCheck> tablespaceList(TablespaceCheck tablespaceCheck) throws Exception;

	/** RecyclebinObject Count/Size 리스트 */
	RecyclebinCheck getRecyclebinObjectCount(RecyclebinCheck recyclebinCheck) throws Exception;
	
	/** RecyclebinObject 리스트 */
	List<RecyclebinCheck> recyclebinObjectList(RecyclebinCheck recyclebinCheck) throws Exception;

	/** InvalidObject 리스트 */
	List<ObjectCheck> invalidObjectList(ObjectCheck objectCheck) throws Exception;

	/** NologgingObject 리스트 */
	List<ObjectCheck> nologgingObjectList(ObjectCheck objectCheck) throws Exception;

	/** ParallelObject 리스트 */
	List<ObjectCheck> parallelObjectList(ObjectCheck objectCheck) throws Exception;

	/** UnusableIndex 리스트 */
	List<UnusableIndexCheck> unusableIndexList(UnusableIndexCheck unusableIndexCheck) throws Exception;

	/** ChainedRows 리스트 */
	List<ChainedRowCheck> chainedRowsList(ChainedRowCheck chainedRowCheck) throws Exception;

	/** CorruptBlock 리스트 */
	List<BlockCorruptionCheck> corruptBlockList(BlockCorruptionCheck blockCorruptionCheck) throws Exception;

	/** Sequence 리스트 */
	List<SequenceCheck> sequenceList(SequenceCheck sequenceCheck) throws Exception;

	/** ForeignkeysWithoutIndex 리스트 */
	List<FkIndexCheck> foreignkeysWithoutIndexList(FkIndexCheck fkIndexCheck) throws Exception;

	/** DisabledConstraint 리스트 */
	List<ConstraintCheck> disabledConstraintList(ConstraintCheck constraintCheck) throws Exception;

	/** MissingOrStaleStatistics 리스트 */
	List<TableStatisticsCheck> missingOrStaleStatisticsList(TableStatisticsCheck tableStatisticsCheck) throws Exception;
	List<LinkedHashMap<String, Object>> missingOrStaleStatisticsList4Excel(TableStatisticsCheck tableStatisticsCheck) throws Exception;

	/** StatisticsLockedTable 리스트 */
	List<TableStatisticsLockCheck> statisticsLockedTableList(TableStatisticsLockCheck tableStatisticsLockCheck) throws Exception;

	/** LongRunningOperation 리스트 */
	List<LongRunningOperationCheck> longRunningOperationList(LongRunningOperationCheck longRunningOperationCheck) throws Exception;

	/** LongRunningJob 리스트 */
	List<LongRunningSchedulerCheck> longRunningJobList(LongRunningSchedulerCheck longRunningSchedulerCheck) throws Exception;

	/** AlertLogError 리스트 */
	List<AlertErrorCheck> alertLogErrorList(AlertErrorCheck alertErrorCheck) throws Exception;

	/** ActiveIncident - Problem 리스트 */
	List<DiagProblem> activeIncidentProblemList(DiagProblem diagProblem) throws Exception;
	
	/** ActiveIncident - Incident 리스트 */
	List<DiagIncident> activeIncidentIncidentList(DiagIncident diagIncident) throws Exception;	

	/** OutstandingAlert 리스트 */
	List<OutstandingAlerts> outstandingAlertList(OutstandingAlerts outstandingAlerts) throws Exception;

	/** DbmsSchedulerJobFailed 리스트 */
	List<SchedulerJobFailedCheck> dbmsSchedulerJobFailedList(SchedulerJobFailedCheck schedulerJobFailedCheck) throws Exception;
	
	int saveDbCheckException(DbCheckException dbCheckException) throws Exception;

}
