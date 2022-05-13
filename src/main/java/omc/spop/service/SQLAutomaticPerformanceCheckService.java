package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import omc.spop.model.AuthoritySQL;
import omc.spop.model.ExplainPlanTree;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.OdsUsers;
import omc.spop.model.ProjectSqlIdfyCondition;
import omc.spop.model.Result;
import omc.spop.model.SQLAutomaticPerformanceCheck;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

public interface SQLAutomaticPerformanceCheckService {
	
	List<SQLAutomaticPerformanceCheck> searchProjectList(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> searchPerformanceCheckId(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> loadPerformanceCheckCount(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> loadChartData(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> loadPerformanceCheckList(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	int countExecuteTms(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> loadOriginalDb(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> maxPerformanceCheckId(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	@Transactional
	Result insertSqlAutomaticPerformanceCheck(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	Result forceUpdateSqlAutomaticPerformanceCheck(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownload(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
	
	OdsHistSqlText loadExplainSqlText(OdsHistSqlText odsHistSqlText) throws Exception;
	
	List<OdsHistSqlstat> loadExplainBindValue(OdsHistSqlstat odsHistSqlstat) throws Exception;
	
	List<ExplainPlanTree> loadExplainBeforePlan(ExplainPlanTree explainPlanTree) throws Exception;
	
	List<ExplainPlanTree> loadExplainAfterSelectPlan(ExplainPlanTree explainPlanTree) throws Exception;
	
	List<ExplainPlanTree> loadExplainAfterNotSelectPlan(ExplainPlanTree explainPlanTree) throws Exception;
	
	List<ProjectSqlIdfyCondition> getProjectSqlIdfyConditionList(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;
	
	List<OdsUsers> getUserNameComboBox(OdsUsers odsUsers) throws Exception;
	
	List<OdsUsers> getProjectUserNameComboBox(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;

	int saveProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;

	int deleteProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;

	List<OdsUsers> getSelectTable(ProjectSqlIdfyCondition projectSqlIdfyCondition)  throws Exception;
	
	List<OdsUsers> getSelectProjectSqlIdfyConditionTable(ProjectSqlIdfyCondition projectSqlIdfyCondition)   throws Exception;

	List<OdsUsers> getTableOwner(OdsUsers odsUsers) throws Exception;

	int saveProjectSqlIdfyConditionPopup(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;

	List<ProjectSqlIdfyCondition> getProjectSqlIdfyConditionCheck(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception;

	List<LinkedHashMap<String, Object>> excelDownloadProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition)  throws Exception;
	
	List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL) throws Exception;
	
	List<SQLAutomaticPerformanceCheck> getRoundingInfo(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception;
}
