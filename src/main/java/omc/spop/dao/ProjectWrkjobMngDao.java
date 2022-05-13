package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectWrkjob;

public interface ProjectWrkjobMngDao {

	List<ProjectWrkjob> getProjectWrkjobList(ProjectWrkjob projectWrkjob);

	int getDupCnt(ProjectWrkjob projectWrkjob);

	int insertProjectWrkjob(ProjectWrkjob projectWrkjob);

	int updateProjectWrkjob(ProjectWrkjob projectWrkjob);

	int deleteProjectWrkjob(ProjectWrkjob projectWrkjob);

	List<LinkedHashMap<String, Object>> excelDownload(ProjectWrkjob projectWrkjob);
}
