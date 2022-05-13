package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.PreventiveCheckDao;
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
import omc.spop.service.PreventiveCheckService;

/***********************************************************
 * 2018.04.18	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("PreventiveCheckService")
public class PreventiveCheckServiceImpl implements PreventiveCheckService {
	@Autowired
	private PreventiveCheckDao preventiveCheckDao;
	
	@Override
	public List<DbCheckExec> checkSeqList(DbCheckExec dbCheckExec) throws Exception {
		return preventiveCheckDao.checkSeqList(dbCheckExec);
	}	

	@Override
	public List<DbCheckExec> checkSeqListAll(DbCheckExec dbCheckExec) throws Exception {
		return preventiveCheckDao.checkSeqListAll(dbCheckExec);
	}
	
	@Override
	public List<DailyCheck> summaryList(DailyCheck dailyCheck) throws Exception {
		return preventiveCheckDao.summaryList(dailyCheck);
	}
	
	@Override
	public List<SqlAutoTuningRecommendation> sqlTuningAdvisorList(SqlAutoTuningRecommendation sqlAutoTuningRecommendation) throws Exception {
		return preventiveCheckDao.sqlTuningAdvisorList(sqlAutoTuningRecommendation);
	}
	
	@Override
	public List<SgaTargetAdvisor> sgaAdvisorList(SgaTargetAdvisor sgaTargetAdvisor) throws Exception {
		return preventiveCheckDao.sgaAdvisorList(sgaTargetAdvisor);
	}
	
	@Override
	public List<DbCacheAdvisor> bufferCacheAdvisorList(DbCacheAdvisor dbCacheAdvisor) throws Exception {
		return preventiveCheckDao.bufferCacheAdvisorList(dbCacheAdvisor);
	}
	
	@Override
	public List<SharedPoolAdvisor> sharedPoolAdvisorList(SharedPoolAdvisor sharedPoolAdvisor) throws Exception {
		return preventiveCheckDao.sharedPoolAdvisorList(sharedPoolAdvisor);
	}
	
	@Override
	public List<PgaTargetAdvisor> pgaAdvisorList(PgaTargetAdvisor pgaTargetAdvisor) throws Exception {
		return preventiveCheckDao.pgaAdvisorList(pgaTargetAdvisor);
	}
	
	@Override
	public List<SegmentAdvisor> segmentAdvisorList(SegmentAdvisor segmentAdvisor) throws Exception {
		return preventiveCheckDao.segmentAdvisorList(segmentAdvisor);
	}
	
	@Override
	public List<HmRecommendation> healthMonitorList(HmRecommendation hmRecommendation) throws Exception {
		return preventiveCheckDao.healthMonitorList(hmRecommendation);
	}	
	
	@Override
	public List<DatabaseDsbCheck> databaseStatusList(DatabaseDsbCheck databaseDsbCheck) throws Exception {
		return preventiveCheckDao.databaseStatusList(databaseDsbCheck);
	}	
	
	@Override
	public List<DbUserCheck> expiredGraceAccountList(DbUserCheck dbUserCheck) throws Exception {
		return preventiveCheckDao.expiredGraceAccountList(dbUserCheck);
	}	

	@Override
	public List<ParameterChangeCheck> modifiedParameterList(ParameterChangeCheck parameterChangeCheck) throws Exception {
		return preventiveCheckDao.modifiedParameterList(parameterChangeCheck);
	}	

	@Override
	public List<NewCreateObjectCheck> newCreatedObjectList(NewCreateObjectCheck newCreateObjectCheck) throws Exception {
		return preventiveCheckDao.newCreatedObjectList(newCreateObjectCheck);
	}	

	@Override
	public List<InstanceDsbCheck> instanceStatusList(InstanceDsbCheck instanceDsbCheck) throws Exception {
		return preventiveCheckDao.instanceStatusList(instanceDsbCheck);
	}	

	@Override
	public List<ListenerCheck> listenerStatusList(ListenerCheck listenerCheck) throws Exception {
		return preventiveCheckDao.listenerStatusList(listenerCheck);
	}	

	@Override
	public List<DbFileCheck> dbfilesList(DbFileCheck dbFileCheck) throws Exception {
		return preventiveCheckDao.dbfilesList(dbFileCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> libraryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.libraryCacheHitList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> dictionaryCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.dictionaryCacheHitList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> bufferCacheHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.bufferCacheHitList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> latchHitList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.latchHitList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> parseCpuToParseElapsdList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.parseCpuToParseElapsdList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> diskSortList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.diskSortList(instanceEfficiencyCheck);
	}	

	@Override
	public List<InstanceEfficiencyCheck> memoryUsageList(InstanceEfficiencyCheck instanceEfficiencyCheck) throws Exception {
		return preventiveCheckDao.memoryUsageList(instanceEfficiencyCheck);
	}	

	@Override
	public List<ResourceLimitCheck> resourceLimitList(ResourceLimitCheck resourceLimitCheck) throws Exception {
		return preventiveCheckDao.resourceLimitList(resourceLimitCheck);
	}	

	@Override
	public List<FilesystemSpaceCheck> backgroundDumpSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception {
		return preventiveCheckDao.backgroundDumpSpaceList(filesystemSpaceCheck);
	}	

	@Override
	public List<FilesystemSpaceCheck> archiveLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception {
		return preventiveCheckDao.archiveLogSpaceList(filesystemSpaceCheck);
	}	

	@Override
	public List<FilesystemSpaceCheck> alertLogSpaceList(FilesystemSpaceCheck filesystemSpaceCheck) throws Exception {
		return preventiveCheckDao.alertLogSpaceList(filesystemSpaceCheck);
	}	

	@Override
	public List<FraSpaceCheck> fraSpaceList(FraSpaceCheck fraSpaceCheck) throws Exception {
		return preventiveCheckDao.fraSpaceList(fraSpaceCheck);
	}
	
	@Override
	public List<FraSpaceUsage> fraUsageList(FraSpaceUsage fraSpaceUsage) throws Exception {
		return preventiveCheckDao.fraUsageList(fraSpaceUsage);
	}	

	@Override
	public List<AsmDiskgroupCheck> asmDiskgroupSpaceList(AsmDiskgroupCheck asmDiskgroupCheck) throws Exception {
		return preventiveCheckDao.asmDiskgroupSpaceList(asmDiskgroupCheck);
	}	

	@Override
	public List<TablespaceCheck> tablespaceList(TablespaceCheck tablespaceCheck) throws Exception {
		return preventiveCheckDao.tablespaceList(tablespaceCheck);
	}
	
	@Override
	public RecyclebinCheck getRecyclebinObjectCount(RecyclebinCheck recyclebinCheck) throws Exception {
		return preventiveCheckDao.getRecyclebinObjectCount(recyclebinCheck);
	}

	@Override
	public List<RecyclebinCheck> recyclebinObjectList(RecyclebinCheck recyclebinCheck) throws Exception {
		return preventiveCheckDao.recyclebinObjectList(recyclebinCheck);
	}

	@Override
	public List<ObjectCheck> invalidObjectList(ObjectCheck objectCheck) throws Exception {
		return preventiveCheckDao.invalidObjectList(objectCheck);
	}	

	@Override
	public List<ObjectCheck> nologgingObjectList(ObjectCheck objectCheck) throws Exception {
		return preventiveCheckDao.nologgingObjectList(objectCheck);
	}	

	@Override
	public List<ObjectCheck> parallelObjectList(ObjectCheck objectCheck) throws Exception {
		return preventiveCheckDao.parallelObjectList(objectCheck);
	}	

	@Override
	public List<UnusableIndexCheck> unusableIndexList(UnusableIndexCheck unusableIndexCheck) throws Exception {
		return preventiveCheckDao.unusableIndexList(unusableIndexCheck);
	}	

	@Override
	public List<ChainedRowCheck> chainedRowsList(ChainedRowCheck chainedRowCheck) throws Exception {
		return preventiveCheckDao.chainedRowsList(chainedRowCheck);
	}	

	@Override
	public List<BlockCorruptionCheck> corruptBlockList(BlockCorruptionCheck blockCorruptionCheck) throws Exception {
		return preventiveCheckDao.corruptBlockList(blockCorruptionCheck);
	}	

	@Override
	public List<SequenceCheck> sequenceList(SequenceCheck sequenceCheck) throws Exception {
		return preventiveCheckDao.sequenceList(sequenceCheck);
	}	

	@Override
	public List<FkIndexCheck> foreignkeysWithoutIndexList(FkIndexCheck fkIndexCheck) throws Exception {
		return preventiveCheckDao.foreignkeysWithoutIndexList(fkIndexCheck);
	}	

	@Override
	public List<ConstraintCheck> disabledConstraintList(ConstraintCheck constraintCheck) throws Exception {
		return preventiveCheckDao.disabledConstraintList(constraintCheck);
	}	

	@Override
	public List<TableStatisticsCheck> missingOrStaleStatisticsList(TableStatisticsCheck tableStatisticsCheck) throws Exception {
		return preventiveCheckDao.missingOrStaleStatisticsList(tableStatisticsCheck);
	}	
	@Override
	public List<LinkedHashMap<String, Object>> missingOrStaleStatisticsList4Excel(TableStatisticsCheck tableStatisticsCheck) throws Exception {
		return preventiveCheckDao.missingOrStaleStatisticsList4Excel(tableStatisticsCheck);
	}	

	@Override
	public List<TableStatisticsLockCheck> statisticsLockedTableList(TableStatisticsLockCheck tableStatisticsLockCheck) throws Exception {
		return preventiveCheckDao.statisticsLockedTableList(tableStatisticsLockCheck);
	}	

	@Override
	public List<LongRunningOperationCheck> longRunningOperationList(LongRunningOperationCheck longRunningOperationCheck) throws Exception {
		return preventiveCheckDao.longRunningOperationList(longRunningOperationCheck);
	}	

	@Override
	public List<LongRunningSchedulerCheck> longRunningJobList(LongRunningSchedulerCheck longRunningSchedulerCheck) throws Exception {
		return preventiveCheckDao.longRunningJobList(longRunningSchedulerCheck);
	}	

	@Override
	public List<AlertErrorCheck> alertLogErrorList(AlertErrorCheck alertErrorCheck) throws Exception {
		return preventiveCheckDao.alertLogErrorList(alertErrorCheck);
	}	

	@Override
	public List<DiagProblem> activeIncidentProblemList(DiagProblem diagProblem) throws Exception {
		return preventiveCheckDao.activeIncidentProblemList(diagProblem);
	}
	
	@Override
	public List<DiagIncident> activeIncidentIncidentList(DiagIncident diagIncident) throws Exception {
		return preventiveCheckDao.activeIncidentIncidentList(diagIncident);
	}	

	@Override
	public List<OutstandingAlerts> outstandingAlertList(OutstandingAlerts outstandingAlerts) throws Exception {
		return preventiveCheckDao.outstandingAlertList(outstandingAlerts);
	}	

	@Override
	public List<SchedulerJobFailedCheck> dbmsSchedulerJobFailedList(SchedulerJobFailedCheck schedulerJobFailedCheck) throws Exception {
		return preventiveCheckDao.dbmsSchedulerJobFailedList(schedulerJobFailedCheck);
	}

	@Override
	public int saveDbCheckException(DbCheckException dbCheckException) throws Exception {
		return preventiveCheckDao.saveDbCheckException(dbCheckException);
	}

}