package omc.spop.dao;

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

public interface PreventiveCheckDao {	
	public List<DbCheckExec> checkSeqList(DbCheckExec dbCheckExec);
	public List<DbCheckExec> checkSeqListAll(DbCheckExec dbCheckExec);

	public List<DailyCheck> summaryList(DailyCheck dailyCheck);
	
	public List<SqlAutoTuningRecommendation> sqlTuningAdvisorList(SqlAutoTuningRecommendation sqlAutoTuningRecommendation) throws Exception;
	
	public List<SgaTargetAdvisor> sgaAdvisorList(SgaTargetAdvisor sgaTargetAdvisor) throws Exception;
	
	public List<DbCacheAdvisor> bufferCacheAdvisorList(DbCacheAdvisor dbCacheAdvisor) throws Exception;
	
	public List<SharedPoolAdvisor> sharedPoolAdvisorList(SharedPoolAdvisor sharedPoolAdvisor) throws Exception;
	
	public List<PgaTargetAdvisor> pgaAdvisorList(PgaTargetAdvisor pgaTargetAdvisor) throws Exception;
	
	public List<SegmentAdvisor> segmentAdvisorList(SegmentAdvisor segmentAdvisor) throws Exception;
	
	public List<HmRecommendation> healthMonitorList(HmRecommendation hmRecommendation) throws Exception;
	
	public List<DatabaseDsbCheck> databaseStatusList(DatabaseDsbCheck databaseDsbCheck) throws Exception;

	public List<DbUserCheck> expiredGraceAccountList(DbUserCheck dbUserCheck) throws Exception;

	public List<ParameterChangeCheck> modifiedParameterList(ParameterChangeCheck parameterChangeCheck) throws Exception;

	public List<NewCreateObjectCheck> newCreatedObjectList(NewCreateObjectCheck newCreateObjectCheck) throws Exception;

	public List<InstanceDsbCheck> instanceStatusList(InstanceDsbCheck instanceDsbCheck) throws Exception;

	public List<ListenerCheck> listenerStatusList(ListenerCheck listenerCheck) throws Exception;

	public List<DbFileCheck> dbfilesList(DbFileCheck dbFileCheck) throws Exception;

	public List<InstanceEfficiencyCheck> libraryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> dictionaryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> bufferCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> latchHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> parseCpuToParseElapsdList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> diskSortList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<InstanceEfficiencyCheck> memoryUsageList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception;

	public List<ResourceLimitCheck> resourceLimitList(ResourceLimitCheck resourceLimitCheck) throws Exception;

	public List<FilesystemSpaceCheck> backgroundDumpSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	public List<FilesystemSpaceCheck> archiveLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	public List<FilesystemSpaceCheck> alertLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception;

	public List<FraSpaceCheck> fraSpaceList(FraSpaceCheck fraSpaceCheck) throws Exception;
	
	public List<FraSpaceUsage> fraUsageList(FraSpaceUsage fraSpaceUsage) throws Exception;

	public List<AsmDiskgroupCheck> asmDiskgroupSpaceList(AsmDiskgroupCheck asmDiskgroupCheck) throws Exception;

	public List<TablespaceCheck> tablespaceList(TablespaceCheck tablespaceCheck) throws Exception;

	public RecyclebinCheck getRecyclebinObjectCount(RecyclebinCheck recyclebinCheck) throws Exception;
	
	public List<RecyclebinCheck> recyclebinObjectList(RecyclebinCheck recyclebinCheck) throws Exception;

	public List<ObjectCheck> invalidObjectList(ObjectCheck objectCheck) throws Exception;

	public List<ObjectCheck> nologgingObjectList(ObjectCheck objectCheck) throws Exception;

	public List<ObjectCheck> parallelObjectList(ObjectCheck objectCheck) throws Exception;

	public List<UnusableIndexCheck> unusableIndexList(UnusableIndexCheck unusableIndexCheck) throws Exception;

	public List<ChainedRowCheck> chainedRowsList(ChainedRowCheck chainedRowCheck) throws Exception;

	public List<BlockCorruptionCheck> corruptBlockList(BlockCorruptionCheck blockCorruptionCheck) throws Exception;

	public List<SequenceCheck> sequenceList(SequenceCheck sequenceCheck) throws Exception;

	public List<FkIndexCheck> foreignkeysWithoutIndexList(FkIndexCheck fkIndexCheck) throws Exception;

	public List<ConstraintCheck> disabledConstraintList(ConstraintCheck constraintCheck) throws Exception;

	public List<TableStatisticsCheck> missingOrStaleStatisticsList(TableStatisticsCheck tableStatisticsCheck) throws Exception;
	public List<LinkedHashMap<String, Object>> missingOrStaleStatisticsList4Excel(TableStatisticsCheck tableStatisticsCheck) throws Exception;

	public List<TableStatisticsLockCheck> statisticsLockedTableList(TableStatisticsLockCheck tableStatisticsLockCheck) throws Exception;

	public List<LongRunningOperationCheck> longRunningOperationList(LongRunningOperationCheck longRunningOperationCheck) throws Exception;

	public List<LongRunningSchedulerCheck> longRunningJobList(LongRunningSchedulerCheck longRunningSchedulerCheck) throws Exception;

	public List<AlertErrorCheck> alertLogErrorList(AlertErrorCheck alertErrorCheck) throws Exception;

	public List<DiagProblem> activeIncidentProblemList(DiagProblem diagProblem) throws Exception;
	
	public List<DiagIncident> activeIncidentIncidentList(DiagIncident diagIncident) throws Exception;

	public List<OutstandingAlerts> outstandingAlertList(OutstandingAlerts outstandingAlerts) throws Exception;

	public List<SchedulerJobFailedCheck> dbmsSchedulerJobFailedList(SchedulerJobFailedCheck schedulerJobFailedCheck) throws Exception;
	
	public int saveDbCheckException(DbCheckException dbCheckException);

}
