package omc.mqm.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.mqm.model.ModelEntityType;
import omc.mqm.model.ProjectModel;

public interface ProjectModelMngDao {

	List<ProjectModel> getProjectModelList(ProjectModel projectModel);


	int getDupCnt(ProjectModel projectModel);

	int insertProjectModel(ProjectModel projectModel);

	int updateProjectModel(ProjectModel projectModel);

	int deleteProjectModel(ProjectModel projectModel);

	List<LinkedHashMap<String, Object>> excelDownload(ProjectModel projectModel);

}
