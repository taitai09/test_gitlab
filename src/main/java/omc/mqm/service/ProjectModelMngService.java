package omc.mqm.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.mqm.model.ProjectModel;

public interface ProjectModelMngService {
	public List<ProjectModel> getProjectModelList(ProjectModel projectModel) throws Exception;

	public int getDupCnt(ProjectModel projectModel) throws Exception;

	public int insertProjectModel(ProjectModel projectModel) throws Exception;

	public int updateProjectModel(ProjectModel projectModel) throws Exception;

	public int deleteProjectModel(ProjectModel projectModel) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectModel projectModel) throws Exception;

}
