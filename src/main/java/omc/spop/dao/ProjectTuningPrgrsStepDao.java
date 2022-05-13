package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ProjectTuningPrgrsStep;

public interface ProjectTuningPrgrsStepDao {
	
	List<ProjectTuningPrgrsStep> getProjectTuningPrgrsStepList(ProjectTuningPrgrsStep projectTuningPrgrsStep);
	
	int insertProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep);
	
	int updateProjectTuningPrgrsStep(ProjectTuningPrgrsStep projectTuningPrgrsStep);
	
	List<LinkedHashMap<String, Object>> excelDownload(ProjectTuningPrgrsStep projectTuningPrgrsStep);
}
