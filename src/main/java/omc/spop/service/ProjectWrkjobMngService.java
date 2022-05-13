package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectWrkjob;

public interface ProjectWrkjobMngService {
	public List<ProjectWrkjob> getProjectWrkjobList(ProjectWrkjob projectWrkjob) throws Exception;

	public int getDupCnt(ProjectWrkjob projectWrkjob) throws Exception;

	public int insertProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception;

	public int updateProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception;

	public int deleteProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectWrkjob projectWrkjob);
}
