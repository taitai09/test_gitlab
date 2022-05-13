package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectSqlQtyChkIdx;

public interface ProjectSqlQtyChkIdxMngService {
	public List<ProjectSqlQtyChkIdx> getProjectSqlQtyChkIdxList(ProjectSqlQtyChkIdx projectSqlQtyChkIdx)
			throws Exception;

	public int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception;

	public int insertProjectSqlQtyChkIdxApply(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception;

	public int insertProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception;

	public int updateProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception;

	public int deleteProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkIdx projectSqlQtyChkIdx);

}
