package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectTuningPrgrsStep;

public interface ProjectTuningPrgrsStepService {
	public List<ProjectTuningPrgrsStep> getProjectTuningPrgrsStepList(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception;

	public int insertProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception;

	public int updateProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectTuningPrgrsStep projectTuningPrgrsStep);
}
