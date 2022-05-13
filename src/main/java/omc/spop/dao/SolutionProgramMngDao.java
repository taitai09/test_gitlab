package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.SolutionProgramMng;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 **********************************************************/

public interface SolutionProgramMngDao {

	List<SolutionProgramMng> programListTree(SolutionProgramMng solutionProgramMng);

	List<String> getParentContentsIdList(String contents_id);

	int updateSolutionProgramListMng(SolutionProgramMng solutionProgramMng);

	int insertSolutionProgramListMng(SolutionProgramMng solutionProgramMng);

	int deleteSolutionProgramListMng(SolutionProgramMng solutionProgramMng);

	int contentsIsEmpty(SolutionProgramMng solutionProgramMng);

	List<SolutionProgramMng> programRule(SolutionProgramMng solutionProgramMng);

	int inertProgramRule(SolutionProgramMng solutionProgramMng);

	int updateProgramRule(SolutionProgramMng solutionProgramMng);

	int deleteProgramRule(SolutionProgramMng solutionProgramMng);

	List<SolutionProgramMng> getSolutionProgramListBack();

	int insertProgramRule(SolutionProgramMng solutionProgramMng);

	List<Map<String, Object>> programRuleByExcelDown(SolutionProgramMng solutionProgramMng);

	List<LinkedHashMap<String, Object>> dataCollectionStatusLoadSql(SolutionProgramMng sQLStandardsRow);

	List<SolutionProgramMng> getSolutionProgramChkSql(SolutionProgramMng solutionProgramMng);


}
