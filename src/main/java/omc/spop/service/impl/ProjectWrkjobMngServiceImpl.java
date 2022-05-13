package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ProjectWrkjobMngDao;
import omc.spop.model.ProjectWrkjob;
import omc.spop.service.ProjectWrkjobMngService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectWrkjobMngService")
public class ProjectWrkjobMngServiceImpl implements ProjectWrkjobMngService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectWrkjobMngServiceImpl.class);

	@Autowired
	private ProjectWrkjobMngDao projectWrkjobMngDao;

	@Override
	public List<ProjectWrkjob> getProjectWrkjobList(ProjectWrkjob projectWrkjob) throws Exception {
		return projectWrkjobMngDao.getProjectWrkjobList(projectWrkjob);
	}

	@Override
	public int getDupCnt(ProjectWrkjob projectWrkjob) throws Exception {
		return projectWrkjobMngDao.getDupCnt(projectWrkjob);
	}

	@Override
	public int insertProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception {
		return projectWrkjobMngDao.insertProjectWrkjob(projectWrkjob);
	}

	@Override
	public int updateProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception {
		return projectWrkjobMngDao.updateProjectWrkjob(projectWrkjob);
	}

	@Override
	public int deleteProjectWrkjob(ProjectWrkjob projectWrkjob) throws Exception {
		return projectWrkjobMngDao.deleteProjectWrkjob(projectWrkjob);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectWrkjob projectWrkjob) {
		return projectWrkjobMngDao.excelDownload(projectWrkjob);
	}

}
