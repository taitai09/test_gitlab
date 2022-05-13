package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ProjectDbMngDao;
import omc.spop.model.ProjectDb;
import omc.spop.service.ProjectDbMngService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectDbMngService")
public class ProjectDbMngServiceImpl implements ProjectDbMngService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDbMngServiceImpl.class);

	@Autowired
	private ProjectDbMngDao projectDbMngDao;

	@Override
	public List<ProjectDb> getProjectDbList(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.getProjectDbList(projectDb);
	}

	@Override
	public int getDupCnt(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.getDupCnt(projectDb);
	}

	@Override
	public int insertProjectDb(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.insertProjectDb(projectDb);
	}

	@Override
	public int updateProjectDb(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.updateProjectDb(projectDb);
	}

	@Override
	public int deleteProjectDb(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.deleteProjectDb(projectDb);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectDb projectDb) {
		return projectDbMngDao.excelDownload(projectDb);
	}

	@Override
	public int validationCheckProjectDb(ProjectDb projectDb) throws Exception {
		return projectDbMngDao.validationCheckProjectDb(projectDb);
	}

}
