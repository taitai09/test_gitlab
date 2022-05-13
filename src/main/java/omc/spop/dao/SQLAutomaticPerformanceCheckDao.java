package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AuthoritySQL;
import omc.spop.model.ExplainPlanTree;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.OdsUsers;
import omc.spop.model.ProjectSqlIdfyCondition;
import omc.spop.model.SQLAutomaticPerformanceCheck;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface SQLAutomaticPerformanceCheckDao {
	public List<SQLAutomaticPerformanceCheck> searchProjectList(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> searchPerformanceCheckId(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> loadPerformanceCheckCount(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> loadChartData(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> loadPerformanceCheckList(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public int countExecuteTms(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> loadOriginalDb(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<SQLAutomaticPerformanceCheck> maxPerformanceCheckId(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public int insertSqlAutomaticPerformanceCheck(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public int insertSqlAutomaticPerformanceCheckTarget(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public int forceUpdateSqlAutomaticPerformanceCheck(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public List<LinkedHashMap<String, Object>> excelDownload(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
	
	public OdsHistSqlText loadExplainSqlText(OdsHistSqlText odsHistSqlText);
	
	public List<OdsHistSqlstat> loadExplainBindValue(OdsHistSqlstat odsHistSqlstat);
	
	public List<ExplainPlanTree> loadExplainBeforePlan(ExplainPlanTree explainPlanTree);

	public List<ExplainPlanTree> loadExplainAfterSelectPlan(ExplainPlanTree explainPlanTree);

	public List<ExplainPlanTree> loadExplainAfterNotSelectPlan(ExplainPlanTree explainPlanTree);

	public List<ProjectSqlIdfyCondition> projectSqlIdfyConditionList(ProjectSqlIdfyCondition projectSqlIdfyCondition);

	public List<OdsUsers> getUserNameComboBox(OdsUsers odsUsers);

	public List<OdsUsers> getProjectUserNameComboBox(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public int insertProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public int updateProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public int deleteProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public int deleteProjectSqlIdfyConditionTypeCd(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public List<ProjectSqlIdfyCondition> getProjectSqlIdfyConditionCheck(ProjectSqlIdfyCondition projectSqlIdfyCondition);

	public List<OdsUsers> getTableOwner(OdsUsers odsUsers);
	
	public List<OdsUsers> getSelectTable(ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public List<OdsUsers> getSelectProjectSqlIdfyConditionTable(ProjectSqlIdfyCondition projectSqlIdfyCondition);

	public List<LinkedHashMap<String, Object>> excelDownloadProjectSqlIdfyCondition(
			ProjectSqlIdfyCondition projectSqlIdfyCondition);
	
	public List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL);
	
	public List<SQLAutomaticPerformanceCheck> getRoundingInfo(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck);
}
