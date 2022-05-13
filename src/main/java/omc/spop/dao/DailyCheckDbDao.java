package omc.spop.dao;

import java.util.List;

import omc.spop.model.DailyCheckDb;

public interface DailyCheckDbDao {
	public List<DailyCheckDb> dbGroupList(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> severityList(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> dbSeverityCount(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> dbMain(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryDb(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryInstance(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummarySpace(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryObject(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryStatistics(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryLongRunningWork(DailyCheckDb dailyCheckDb);
	
	public List<DailyCheckDb> diagnosisResultSummaryAlert(DailyCheckDb dailyCheckDb);
	
	/** Minite DB **/
	public List<DailyCheckDb> databaseStatus(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> expiredGraceAccount(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> modifiedParameter(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> newCreatedObject(DailyCheckDb dailyCheckDb);
	
	/** Minite INSTANCE **/
	public List<DailyCheckDb> instanceStatus(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> listenerStatus(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> dbfiles(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> resourceLimit(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> libraryCacheHit(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> dictionaryCacheHit(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> bufferCacheHit(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> latchHit(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> parseCpuToParseElapsd(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> diskSort(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> memoryUsage(DailyCheckDb dailyCheckDb);
	
	/** Minite SPACE **/
	public List<DailyCheckDb> archiveLogSpace(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> backgroundDumpSpace(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> alertLogSpace(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> fraSpaceFraFiles(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> fraSpaceFraUsage(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> asmDiskgroupSpace(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> tablespace(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> recyclebinObject(DailyCheckDb dailyCheckDb);
	
	/** Minite OBJECT **/
	public List<DailyCheckDb> invalidObject(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> nologgingObject(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> parallelObject(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> unusableIndex(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> corruptBlock(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> sequence(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> foreignkeysWithoutIndex(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> disabledConstraint(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> chainedRows(DailyCheckDb dailyCheckDb);
	
	/** Minite STATISTICS **/
	public List<DailyCheckDb> missingOrStaleStatistics(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> statisticsLockedTable(DailyCheckDb dailyCheckDb);
	
	/** Minite LONG RUNNING WORK **/
	public List<DailyCheckDb> longRunningOperation(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> longRunningJob(DailyCheckDb dailyCheckDb);
	
	/** Minite ALERT **/
	public List<DailyCheckDb> alertLogError(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> activeIncidentProblem(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> activeIncidentIncident(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> outstandingAlert(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> dbmsSchedulerJobFailed(DailyCheckDb dailyCheckDb);
	
	/** DailyCheckDbSituation **/
	public List<DailyCheckDb> dailyCheckDbSituationTop(DailyCheckDb dailyCheckDb);
	public List<DailyCheckDb> dailyCheckDbSituationBottom(DailyCheckDb dailyCheckDb);
}
