package omc.mqm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.mqm.dao.ProjectModelMngDao;
import omc.mqm.model.ProjectModel;
import omc.mqm.service.ProjectModelMngService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectModelMngService")
public class ProjectModelMngServiceImpl implements ProjectModelMngService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectModelMngServiceImpl.class);

	@Autowired
	private ProjectModelMngDao projectModelMngDao;

	@Override
	public List<ProjectModel> getProjectModelList(ProjectModel projectModel) throws Exception {
		return projectModelMngDao.getProjectModelList(projectModel);
	}

	@Override
	public int getDupCnt(ProjectModel projectModel) throws Exception {
		return projectModelMngDao.getDupCnt(projectModel);
	}

	@Override
	public int insertProjectModel(ProjectModel projectModel) throws Exception {
		return projectModelMngDao.insertProjectModel(projectModel);
	}

	@Override
	public int updateProjectModel(ProjectModel projectModel) throws Exception {
		return projectModelMngDao.updateProjectModel(projectModel);
	}
	
	@Override
	public int deleteProjectModel(ProjectModel projectModel) throws Exception {
		return projectModelMngDao.deleteProjectModel(projectModel);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectModel projectModel) {
		return projectModelMngDao.excelDownload(projectModel);
	}

}
