package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectSqlQtyChkRule;

public interface ProjectSqlQtyChkRuleMngDao {

	List<ProjectSqlQtyChkRule> getProjectSqlQtyChkRuleList(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	int insertProjectSqlQtyChkRuleApply(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	int insertProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	int updateProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	int deleteProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	ProjectSqlQtyChkRule getProjectSqlStdQtyChkSqlUnit(ProjectSqlQtyChkRule projectSqlQtyChkRule);
	
	List<ProjectSqlQtyChkRule> getCountCreatePlan(ProjectSqlQtyChkRule projectSqlQtyChkRule);
}
