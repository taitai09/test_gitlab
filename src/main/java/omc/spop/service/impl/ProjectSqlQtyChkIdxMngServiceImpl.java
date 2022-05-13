package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ProjectSqlQtyChkIdxMngDao;
import omc.spop.model.ProjectSqlQtyChkIdx;
import omc.spop.service.ProjectSqlQtyChkIdxMngService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectSqlQtyChkIdxMngService")
public class ProjectSqlQtyChkIdxMngServiceImpl implements ProjectSqlQtyChkIdxMngService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectSqlQtyChkIdxMngServiceImpl.class);

	@Autowired
	private ProjectSqlQtyChkIdxMngDao projectSqlQtyChkIdxMngDao;

	@Override
	public List<ProjectSqlQtyChkIdx> getProjectSqlQtyChkIdxList(ProjectSqlQtyChkIdx projectSqlQtyChkIdx)
			throws Exception {
		return projectSqlQtyChkIdxMngDao.getProjectSqlQtyChkIdxList(projectSqlQtyChkIdx);
	}

	@Override
	public int updateProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception {
		return projectSqlQtyChkIdxMngDao.updateProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
	}

	@Override
	public int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception {
		return projectSqlQtyChkIdxMngDao.countProjectSqlStdQtyChkSql(projectSqlQtyChkIdx);
	}

	@Override
	public int insertProjectSqlQtyChkIdxApply(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception {
		return projectSqlQtyChkIdxMngDao.insertProjectSqlQtyChkIdxApply(projectSqlQtyChkIdx);
	}

	@Override
	public int insertProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception {
		return projectSqlQtyChkIdxMngDao.insertProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
	}

	@Override
	public int deleteProjectSqlQtyChkIdx(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) throws Exception {
		return projectSqlQtyChkIdxMngDao.deleteProjectSqlQtyChkIdx(projectSqlQtyChkIdx);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkIdx projectSqlQtyChkIdx) {
		return projectSqlQtyChkIdxMngDao.excelDownload(projectSqlQtyChkIdx);
	}

}
