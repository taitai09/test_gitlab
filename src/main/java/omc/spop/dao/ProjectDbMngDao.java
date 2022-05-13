package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectDb;

public interface ProjectDbMngDao {

	List<ProjectDb> getProjectDbList(ProjectDb projectDb);

	int getDupCnt(ProjectDb projectDb);

	int insertProjectDb(ProjectDb projectDb);

	int updateProjectDb(ProjectDb projectDb);

	int deleteProjectDb(ProjectDb projectDb);

	List<LinkedHashMap<String, Object>> excelDownload(ProjectDb projectDb);

	int validationCheckProjectDb(ProjectDb projectDb);
}
