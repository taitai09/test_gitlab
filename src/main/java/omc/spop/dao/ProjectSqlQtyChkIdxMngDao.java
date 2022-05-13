package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectSqlQtyChkIdx;

public interface ProjectSqlQtyChkIdxMngDao {

	List<ProjectSqlQtyChkIdx> getProjectSqlQtyChkIdxList(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	int updateProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	int insertProjectSqlQtyChkIdxApply(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	int insertProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	int deleteProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

	List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);
}
