package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.SolutionProgramMng;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 **********************************************************/

public interface SolutionProgramMngService {

	List<SolutionProgramMng> programListTree(SolutionProgramMng solutionProgramMng);

	int saveSolutionProgramListMng(SolutionProgramMng solutionProgramMng) throws Exception;

	int saveMultiSolutionProgramListMng(SolutionProgramMng solutionProgramMng)  throws Exception;

	int deleteSolutionProgramListMng(SolutionProgramMng solutionProgramMng);

	boolean contentsIsEmpty(SolutionProgramMng solutionProgramMng);

	List<SolutionProgramMng> programRule(SolutionProgramMng solutionProgramMng);

	int saveProgramRule(SolutionProgramMng solutionProgramMng) throws Exception;

	int deleteProgramRule(SolutionProgramMng solutionProgramMng);

	List<Map<String, Object>> programRuleByExcelDown(omc.spop.model.SolutionProgramMng solutionProgramMng);

	List<LinkedHashMap<String, Object>> dataCollectionStatus(SolutionProgramMng solutionProgramMng) throws Exception;

	
	
}
