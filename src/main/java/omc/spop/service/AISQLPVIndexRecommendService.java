package omc.spop.service;

import omc.spop.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AISQLPVIndexRecommendService {

	public List<SQLAutoPerformanceCompare> getIndexRecommend(SQLAutoPerformanceCompare sqlAutoPerformanceCompare);
	public String getIndexDataList(HashMap<String, String> paramMap, String paging_YN) throws Exception;
	public String getCurrentCreatedIndexes(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public List<SQLAutoPerformanceCompare> getAutoIndexCreateHistory(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public List<SQLAutoPerformanceCompare> getAutoIndexDropHistory(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public List<SQLAutoPerformanceCompare> getAutoIndexCreateHistoryAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public List<SQLAutoPerformanceCompare> getAutoIndexDropHistoryAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	public String getCreateScript(HashMap<String, String> paramMap) throws Exception;
	public String getDropScript(HashMap<String, String> paramMap) throws Exception;
	public String checkTableSpaceExists(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public List<SQLAutoPerformanceCompare> getPerformanceAnalysis(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public ArrayList<HashMap<String, String>> loadResultListExcel(HashMap<String, String> paramMap, Model model) throws Exception;

	public String autoGenerateIndex(HashMap<String, String> paramMap) throws Exception;
	public String autoIndexDrop(HashMap<String, String> paramMap) throws Exception;
	public String updateSqlAutoPerfChk(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public String forceCompleteAuto(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, String type) throws Exception;

	public String getVisibleIndexInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;
	public String setCreateIndexYN(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception;

	Result forceUpdateSqlAutoPerformance(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception;
	int countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;
	List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL) throws Exception;

	int countPerformanceRecord( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception;
	public int setExcuteAnalyzeSqlAutoPerfChk(HashMap<String, String> paramMap) throws Exception;

	String autoCreateIndexInvisible(HashMap<String, String> map)throws Exception;
	String getPerfChkAutoIndexingV2(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)throws Exception;
	ArrayList<HashMap<String,String>> getAutoError(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)throws Exception;
}
