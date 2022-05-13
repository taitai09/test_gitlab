package omc.spop.service;

import omc.spop.base.SessionManager;
import omc.spop.model.Project;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface AISQLPVAnalyzeService {
	public List<Project> getProjectList(Project project) throws Exception;

	public List<SQLAutoPerformanceCompare> getProjectPerformancePacData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public List<SQLAutoPerformanceCompare> getPerformancePacData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public void excuteAnalyze(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public List<SQLAutoPerformanceCompare> getExcuteAnalyzeConstraint(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public String forceCompleteAnalyze(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public ArrayList<HashMap<String, String>> excuteAISQLPVAnalyze() throws Exception;

	public ArrayList<HashMap<String, String>> getExcutedAISQLPVAnalyzeData() throws Exception;

	public List<SQLAutoPerformanceCompare> getOriginalDB(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public String getRecommendIndexDbYn(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public int updateSqlAutoPerfChk( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;

}
