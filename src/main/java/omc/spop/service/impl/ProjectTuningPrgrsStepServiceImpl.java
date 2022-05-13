package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ProjectTuningPrgrsStepDao;
import omc.spop.model.ProjectTuningPrgrsStep;
import omc.spop.service.ProjectTuningPrgrsStepService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectTuningPrgrsStepServiceImpl")
public class ProjectTuningPrgrsStepServiceImpl implements ProjectTuningPrgrsStepService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTuningPrgrsStepServiceImpl.class);

	@Autowired
	private ProjectTuningPrgrsStepDao projectTuningPrgrsStepDao;

	@Override
	public List<ProjectTuningPrgrsStep> getProjectTuningPrgrsStepList(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception {
		return projectTuningPrgrsStepDao.getProjectTuningPrgrsStepList(projectTuningPrgrsStep);
	}

	@Override
	public int insertProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception {
		return projectTuningPrgrsStepDao.insertProjectTuningPrgrsStep(projectTuningPrgrsStep);
	}

	@Override
	public int updateProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception {
		return projectTuningPrgrsStepDao.updateProjectTuningPrgrsStep(projectTuningPrgrsStep);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectTuningPrgrsStep projectTuningPrgrsStep) {
		return projectTuningPrgrsStepDao.excelDownload(projectTuningPrgrsStep);
	}

}
