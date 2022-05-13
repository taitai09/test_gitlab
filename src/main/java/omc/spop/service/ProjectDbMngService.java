package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectDb;

public interface ProjectDbMngService {
	public List<ProjectDb> getProjectDbList(ProjectDb projectDb) throws Exception;

	public int getDupCnt(ProjectDb projectDb) throws Exception;

	public int insertProjectDb(ProjectDb projectDb) throws Exception;

	public int updateProjectDb(ProjectDb projectDb) throws Exception;

	public int deleteProjectDb(ProjectDb projectDb) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectDb projectDb) throws Exception;

	public int validationCheckProjectDb(ProjectDb projectDb) throws Exception;
}
