package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AccPathExec;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.ProjectUnitLoadSql;
import omc.spop.model.SolutionProgramMng;
import omc.spop.model.VsqlGatheringModule;
import omc.spop.model.VsqlParsingSchema;
import omc.spop.model.VsqlSnapshot;

/***********************************************************
 * 2017.09.27	이원식	최초작성
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignPreProcessingDao {	
	public List<VsqlParsingSchema> collectionTargetList(VsqlParsingSchema vsqlParsingSchema);
	
	public List<VsqlParsingSchema> applyTargetList(VsqlParsingSchema vsqlParsingSchema);
	
	public VsqlParsingSchema glovalViewInfo(VsqlParsingSchema vsqlParsingSchema);
	
	public List<VsqlGatheringModule> collectionModuleList(VsqlGatheringModule vsqlGatheringModule);
	
	public String getMaxParsingSchemaNo(VsqlParsingSchema vsqlParsingSchema);
	
	public void insertParsingSchema(VsqlParsingSchema vsqlParsingSchema);
	
	public String getMaxGatheringModlueNo(VsqlParsingSchema vsqlParsingSchema);
	
	public void insertGatheringModule(VsqlGatheringModule vsqlGatheringModule);	
	
	public List<AccPathExec> parsingCollectionTermsList(AccPathExec accPathExec);
	
	public List<VsqlSnapshot> snapShotList(VsqlSnapshot vsqlSnapshot);
	
	public void insertParsingCollectionTerms(VsqlSnapshot vsqlSnapshot);	
	
	public List<DbioLoadFile> loadSQLList(DbioLoadFile dbioLoadFile);

	public String getDbioFileNo();
	
	public void insertDbioLoadFile(DbioLoadFile dbioLoadFile);
	
	public void insertDbioLoadSql(DbioLoadSql dbioLoadSql);
	
	public void updateDbioLoadFile(DbioLoadFile dbioLoadFile);
	
	public int deleteProjectUnitLoadSql(ProjectUnitLoadSql projectUnitLoadSql);
	
	public void insertProjectUnitLoadSql(ProjectUnitLoadSql projectUnitLoadSql);
	
	public List<SolutionProgramMng> selectSolution10235();
	
	public int update10235(ProjectUnitLoadSql projectUnitLoadSql);
	
	public List<DbioLoadSql> loadActionPlanList(DbioLoadSql dbioLoadSql);
	
	public List<LinkedHashMap<String, Object>> loadActionPlanList4Excel(DbioLoadSql dbioLoadSql);
	
	public DbioLoadSql actionPlanInfo(DbioLoadSql dbioLoadSql);
	
	public String getMaxExplainExecSeq(DbioLoadSql dbioLoadSql);
	
	public List<DbioLoadSql>  isTaskLoadActionPlan(DbioLoadSql dbioLoadSql);
	
	public void insertDbioExplainExec(DbioExplainExec dbioExplainExec);
	
	public List<DbioLoadFile> explainList(DbioLoadFile dbioLoadFile);
	
	public DbioExplainExec planExecCnt(DbioExplainExec dbioExplainExec);
	
	public DbioExplainExec selectActionPlanLog(DbioExplainExec dbioExplainExec);
	
	public int updateForceComplete(DbioExplainExec dbioExplainExec);
	
	public List<AccPathExec> accessPathList(AccPathExec accPathExec);
	
	public void insertParseLoadingCondition(AccPathExec accPathExec);

	public VsqlSnapshot getSnapNoList(VsqlSnapshot vsqlSnapshot);

}
