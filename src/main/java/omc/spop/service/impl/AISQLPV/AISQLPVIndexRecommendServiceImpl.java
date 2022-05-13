package omc.spop.service.impl.AISQLPV;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import omc.spop.base.SessionManager;
import omc.spop.controller.AISQLIndexRecommendController;
import omc.spop.dao.AISQLPVIndexRecommendDao;
import omc.spop.model.*;
import omc.spop.server.tune.AutoIndexingSQLPerfVerify;
import omc.spop.server.tune.ProjectPerfChk;
import omc.spop.service.AISQLPVIndexRecommendService;

import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationAnalyzeServiceImpl
 **********************************************************/

@Service("AISQLPVIndexRecommendService")
public class AISQLPVIndexRecommendServiceImpl implements AISQLPVIndexRecommendService {

	private static final Logger logger = LoggerFactory.getLogger(AISQLPVIndexRecommendService.class);

	public static String PAGING_Y = "Y";
	public static String PAGING_N = "N";

	private String FORCE_CREATE_DROP = "D";
	private String FORCE_CREATE_CREATE = "C";

	@Autowired
	AISQLPVIndexRecommendDao aisqlpvIndexRecommendDao;

	@Override
	public List<SQLAutoPerformanceCompare> getIndexRecommend(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {

		if(logger.isDebugEnabled()){
			logger.debug("getIndexRecommend Params \n project_id = {}",sqlAutoPerformanceCompare.getProject_id());
			logger.debug("sql_auto_perf_check_id = {}",sqlAutoPerformanceCompare.getSql_auto_perf_check_id());
			logger.debug("database_kinds_cd = {}",sqlAutoPerformanceCompare.getDatabase_kinds_cd());
		}

		return aisqlpvIndexRecommendDao.getIndexRecommend(sqlAutoPerformanceCompare);
	}

	@Override
	public String getIndexDataList(HashMap<String, String> paramMap, String paging_YN) throws Exception {

		if(logger.isDebugEnabled()){
			logger.debug("getIndexDataList Params = {}",paramMap.toString());
		}

		String projectId = paramMap.get("project_id");
		String sqlAutoPerfCheckId = paramMap.get("sql_auto_perf_check_id");
		String databaseKidnsCd = paramMap.get("database_kinds_cd");
		int currentPage = Integer.parseInt(paramMap.get("currentPage"));
		int pagePerCount = Integer.parseInt(paramMap.get("pagePerCount"));
		String tableName = paramMap.get("tableName");
		String recommendType = paramMap.get("recommendType");
		String indexCreateYn = paramMap.get("indexAdd");
		String lastRecommendTypeCd = paramMap.get("spsRecommendIndex");
		String owner = paramMap.get("owner");

		Long l_projectId = Long.parseLong(projectId);
		Long l_sqlAutoPerfCheckId = Long.parseLong(sqlAutoPerfCheckId);

		String result = new AutoIndexingSQLPerfVerify().requestRecommendIndexList
				(l_projectId, l_sqlAutoPerfCheckId, databaseKidnsCd, paging_YN, pagePerCount, currentPage,
						owner, tableName, recommendType, indexCreateYn, lastRecommendTypeCd);
		return result;
	}

	@Override
	public String getCurrentCreatedIndexes(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {

		String perf_Check_Target_Dbid = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String idx_Ad_No = sqlAutoPerformanceCompare.getIdx_ad_no();

		Long l_Perf_Check_Target_Dbid = Long.parseLong(perf_Check_Target_Dbid);
		Long l_idx_Ad_No = Long.parseLong(idx_Ad_No);

		if(logger.isDebugEnabled()){
			logger.debug("getCurrentCreatedIndexes Params \n perf_Check_Target_Dbid = {}, idx_Ad_No = {}"
					, perf_Check_Target_Dbid, idx_Ad_No);
		}


		String result = new AutoIndexingSQLPerfVerify().requestRecommendIndexState(l_Perf_Check_Target_Dbid, l_idx_Ad_No);

		return result;
	}

	@Override
	public List<SQLAutoPerformanceCompare> getAutoIndexCreateHistory(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("getIndexRecommend Params \n project_id = {}",sqlAutoPerformanceCompare.getProject_id());
			logger.debug("sql_auto_perf_check_id = {}",sqlAutoPerformanceCompare.getSql_auto_perf_check_id());
			logger.debug("database_kinds_cd = {}",sqlAutoPerformanceCompare.getDatabase_kinds_cd());
		}

		sqlAutoPerformanceCompare.setIdx_work_div_cd("C");
		return aisqlpvIndexRecommendDao.getCreationOrDropHistory(sqlAutoPerformanceCompare);
	}

	@Override
	public List<SQLAutoPerformanceCompare> getAutoIndexDropHistory(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("getIndexRecommend Params \n project_id = {}",sqlAutoPerformanceCompare.getProject_id());
			logger.debug("sql_auto_perf_check_id = {}",sqlAutoPerformanceCompare.getSql_auto_perf_check_id());
			logger.debug("database_kinds_cd = {}",sqlAutoPerformanceCompare.getDatabase_kinds_cd());
		}

		sqlAutoPerformanceCompare.setIdx_work_div_cd("D");
		return aisqlpvIndexRecommendDao.getCreationOrDropHistory(sqlAutoPerformanceCompare);
	}

	@Override
	public List<SQLAutoPerformanceCompare> getAutoIndexCreateHistoryAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("getIndexRecommend Params \n project_id = {}",sqlAutoPerformanceCompare.getProject_id());
			logger.debug("sql_auto_perf_check_id = {}",sqlAutoPerformanceCompare.getSql_auto_perf_check_id());
			logger.debug("database_kinds_cd = {}",sqlAutoPerformanceCompare.getDatabase_kinds_cd());
		}

		sqlAutoPerformanceCompare.setIdx_work_div_cd("C");
		return aisqlpvIndexRecommendDao.getCreationOrDropHistoryAll(sqlAutoPerformanceCompare);
	}

	@Override
	public List<SQLAutoPerformanceCompare> getAutoIndexDropHistoryAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("getIndexRecommend Params \n project_id = {}",sqlAutoPerformanceCompare.getProject_id());
			logger.debug("sql_auto_perf_check_id = {}",sqlAutoPerformanceCompare.getSql_auto_perf_check_id());
			logger.debug("database_kinds_cd = {}",sqlAutoPerformanceCompare.getDatabase_kinds_cd());
		}

		sqlAutoPerformanceCompare.setIdx_work_div_cd("D");
		return aisqlpvIndexRecommendDao.getCreationOrDropHistoryAll(sqlAutoPerformanceCompare);
	}

	@Override
	public String getCreateScript(HashMap<String, String> paramMap) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("getCreateScript Params \n {}",paramMap.toString());
		}

		String tobeDbid = paramMap.get("tobeDbid");
		String idxAdNo = paramMap.get("idxAdNo");
		String tablespaceName = paramMap.get("tablespace_name");
		String isWholeChoice = paramMap.get("isWholeChoice");
		String partitionTableLocalIndexCreateYn = paramMap.get("partitionTableLocalIndexCreateYn");

		String tableOwner = paramMap.get("search_owner");
		String recommendType = paramMap.get("search_recommendType");
		String lastRecommendTypeCd = paramMap.get("lastRecommendTypeCd");
		String tableName = paramMap.get("search_tableName");

		String maxParallelDegreeYn = paramMap.get("maxParallelDegreeYn");
		String maxParallelDegree = paramMap.get("maxParallelDegree");

		List<HashMap> partialSelectList = new ArrayList<HashMap>();

		Long l_tobeDbid = Long.parseLong(tobeDbid);
		Long l_idxAdNo = Long.parseLong(idxAdNo);
		int int_maxParallel = Integer.parseInt(maxParallelDegree);

		if (isWholeChoice.equals("N")) {
			partialSelectList = getCreateScriptListMap(paramMap);
		}
		logger.debug("getCreateScript Params \n {}", paramMap.toString());

		String result = new AutoIndexingSQLPerfVerify().requestIndexCreateScript(l_tobeDbid, l_idxAdNo,tableOwner,tableName,recommendType,lastRecommendTypeCd, tablespaceName, partitionTableLocalIndexCreateYn,maxParallelDegreeYn,int_maxParallel, isWholeChoice, partialSelectList);

		return result;
	}

	@Override
	public String getDropScript(HashMap<String, String> paramMap) throws Exception {
		String tobeDbid = paramMap.get("tobeDbid");
		String idxAdNo = paramMap.get("idxAdNo");
		String isWholeChoice = paramMap.get("isWholeChoice");
		List<HashMap> partialSelectList = new ArrayList<HashMap>();

		String tableOwner = paramMap.get("search_owner");
		String recommendType = paramMap.get("search_recommendType");
		String lastRecommendTypeCd = paramMap.get("lastRecommendTypeCd");
		String tableName = paramMap.get("search_tableName");

		Long l_tobeDbid = Long.parseLong(tobeDbid);
		Long l_idxAdNo = Long.parseLong(idxAdNo);

		if (isWholeChoice.equals("N")) {
			partialSelectList = getCreateScriptListMap(paramMap);
		}
		logger.debug("getDropScript Params \n {}", paramMap.toString());

		String result = new AutoIndexingSQLPerfVerify().requestIndexDropScript(l_tobeDbid, l_idxAdNo, tableOwner, tableName, recommendType, lastRecommendTypeCd, isWholeChoice, partialSelectList);

		return result;
	}

	@Override
	public String checkTableSpaceExists(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		String tobe_Dbid = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String tablespaceName = sqlAutoPerformanceCompare.getTablespace_name();

		Long l_tobe_Dbid = Long.parseLong(tobe_Dbid);
		logger.debug("checkTableSpaceExists Params \n tobe_Dbid = {}, tablespaceName = {}", tobe_Dbid, tablespaceName);

		String result = new AutoIndexingSQLPerfVerify().checkTablespaceNameYn(l_tobe_Dbid, tablespaceName);

		return result;
	}


	private List<HashMap> getCreateScriptListMap(HashMap<String, String> paramMap) {

		ArrayList<HashMap> resultListMap = new ArrayList<HashMap>();
		HashMap<String, String> resultMap = null;

		String idxRecommendSeq = paramMap.get("idxRecommendSeq");
		String recommendType = paramMap.get("recommendType");
		String sourceIndexName = paramMap.get("sourceIndexName");
		String tableName = paramMap.get("tableName");

		String tableOwner = paramMap.get("tableOwner");
		String sourceIndexOwner = paramMap.get("sourceIndexOwner");
		String tempIndexName = paramMap.get("tempIndexName");
		String accessPathColumnList = paramMap.get("accessPathColumnList");

		String[] idxRecommendSeq_split = idxRecommendSeq.split(",");
		String[] recommendType_split = recommendType.split(",");
		String[] sourceIndexName_split = sourceIndexName.split(",");
		String[] tableName_split = tableName.split(",");

		String[] tableOwner_split = tableOwner.split(",");
		String[] sourceIndexOwner_split = sourceIndexOwner.split(",");
		String[] tempIndexName_split = tempIndexName.split(",");
		String[] accessPathColumnList_split = accessPathColumnList.split(",");

		int length = idxRecommendSeq_split.length;

		for (int idx = 0; idx < length; idx++) {
			resultMap = new HashMap<String, String>();

			resultMap.put("idxRecommendSeq", idxRecommendSeq_split[idx].equals("null") ? "" : idxRecommendSeq_split[idx]);
			resultMap.put("recommendType", recommendType_split[idx].equals("null") ? "" : recommendType_split[idx]);
			resultMap.put("sourceIndexName", sourceIndexName_split[idx].equals("null") ? "" : sourceIndexName_split[idx]);
			resultMap.put("tableName", tableName_split[idx].equals("null") ? "" : tableName_split[idx]);

			resultMap.put("sourceIndexOwner", sourceIndexOwner_split[idx].equals("null") ? "" : sourceIndexOwner_split[idx]);
			resultMap.put("tableOwner", tableOwner_split[idx].equals("null") ? "" : tableOwner_split[idx]);
			resultMap.put("tempIndexName", tempIndexName_split[idx].equals("null") ? "" : tempIndexName_split[idx]);
			resultMap.put("accessPathColumnList", accessPathColumnList_split[idx].equals("null") ? "" : accessPathColumnList_split[idx]);

			resultListMap.add(resultMap);
		}

		return resultListMap;
	}

	@Override
	public List<SQLAutoPerformanceCompare> getPerformanceAnalysis(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return aisqlpvIndexRecommendDao.getPerformanceAnalysis(sqlAutoPerformanceCompare);
	}

	@Override
	public ArrayList<HashMap<String, String>> loadResultListExcel(HashMap<String, String> paramMap, Model model) throws Exception {

		String result = getIndexDataList(paramMap, PAGING_N);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(result);
		JSONObject jsonObject = (JSONObject) obj;

		String recommendIndexList = jsonObject.get("recommendIndexList").toString();

		ObjectMapper mapper = new ObjectMapper();

		ArrayList<HashMap<String, String>> resultListMap = mapper.readValue(recommendIndexList, new TypeReference<ArrayList<HashMap<String, String>>>() {});

		return resultListMap;
	}

	@Override
	public String autoGenerateIndex(HashMap<String, String> paramMap) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();
		String userId = users.getUser_id();

		String asisDbid = paramMap.get("asisDbid");
		String tobeDbid = paramMap.get("tobeDbid");
		String idxAdNo = paramMap.get("idxAdNo");
		String tablespaceName = paramMap.get("tablespace_name");
		String isWholeChoice = paramMap.get("isWholeChoice");
		String partitionTableLocalIndexCreateYn = paramMap.get("partitionTableLocalIndexCreateYn");

		String errorIgnoreYn = paramMap.get("err_ignore_yn");
		String execSeq = paramMap.get("execseq");
		String idx_work_div_cd = paramMap.get("idxWorkDivCd");
		String maxParallelDegreeYn = paramMap.get("maxParallelDegreeYn");
		String maxParallelDegree = paramMap.get("maxParallelDegree");

		String tableOwner = paramMap.get("search_owner");
		String recommendType = paramMap.get("search_recommendType");
		String lastRecommendTypeCd = paramMap.get("lastRecommendTypeCd");
		String tableName = paramMap.get("search_tableName");

		List<HashMap> partialSelectList = new ArrayList<HashMap>();

		Long l_asisDbid = Long.parseLong(asisDbid);
		Long l_tobeDbid = Long.parseLong(tobeDbid);
		Long l_idxAdNo = Long.parseLong(idxAdNo);
		Long l_execSeq = Long.parseLong(execSeq);

		int int_maxParallel = Integer.parseInt(maxParallelDegree);
		if (isWholeChoice.equals("N")) {
			partialSelectList = getCreateScriptListMap(paramMap);
		}

		logger.debug("getCurrentCreatedIndexes Params \n {}", paramMap.toString());

		String result = new AutoIndexingSQLPerfVerify().requestIndexAutoCreate(l_asisDbid,l_tobeDbid, l_idxAdNo, tableOwner, tableName, recommendType, lastRecommendTypeCd ,
				tablespaceName, partitionTableLocalIndexCreateYn,maxParallelDegreeYn,int_maxParallel, errorIgnoreYn, l_execSeq, idx_work_div_cd, isWholeChoice, userId, partialSelectList);
		return result;
	}

	@Override
	public String autoIndexDrop(HashMap<String, String> paramMap) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();
		String userId = users.getUser_id();

		String asisDbid = paramMap.get("asisDbid");
		String tobeDbid = paramMap.get("tobeDbid");
		String idxAdNo = paramMap.get("idxAdNo");
		String isWholeChoice = paramMap.get("isWholeChoice");
		String execSeq = paramMap.get("execseq");
		String idx_work_div_cd = paramMap.get("idxWorkDivCd");

		String tableOwner = paramMap.get("search_owner");
		String recommendType = paramMap.get("search_recommendType");
		String lastRecommendTypeCd = paramMap.get("lastRecommendTypeCd");
		String tableName = paramMap.get("search_tableName");

		List<HashMap> partialSelectList = new ArrayList<HashMap>();

		Long l_asisDbid = Long.parseLong(asisDbid);
		Long l_tobeDbid = Long.parseLong(tobeDbid);
		Long l_idxAdNo = Long.parseLong(idxAdNo);
		Long l_execSeq = Long.parseLong(execSeq);

		if (isWholeChoice.equals("N")) {
			partialSelectList = getCreateScriptListMap(paramMap);
		}

		logger.debug("autoIndexDrop Params \n {}", paramMap.toString());

		String result = new AutoIndexingSQLPerfVerify().requestIndexAutoDrop(l_asisDbid,l_tobeDbid, l_idxAdNo, tableOwner, tableName, recommendType, lastRecommendTypeCd,
				l_execSeq,idx_work_div_cd, isWholeChoice, userId, partialSelectList);
		return result;
	}

	@Override
	public String updateSqlAutoPerfChk(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return null;
	}

	@Override
	public String forceCompleteAuto(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, String type) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();
		String userId = users.getUser_id();

		String result = "";
		String original_dbid = sqlAutoPerformanceCompare.getOriginal_dbid();
		String tobeDbId = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String execSeq = sqlAutoPerformanceCompare.getExec_seq();

		Long l_original_dbid = Long.parseLong(original_dbid);
		Long l_tobeDbid = Long.parseLong(tobeDbId);
		Long l_execSeq = Long.parseLong(execSeq);

		result = FORCE_CREATE_CREATE.equals(type) ?
				new AutoIndexingSQLPerfVerify().requestIndexAutoCreateForceCompletion(l_original_dbid,l_tobeDbid, l_execSeq, userId) :
				new AutoIndexingSQLPerfVerify().requestIndexAutoDropForceCompletion(l_original_dbid,l_tobeDbid, l_execSeq, userId);

		return result;
	}

	@Override
	public String getVisibleIndexInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		String result = "";

		String tobeDbId = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String idx_ad_no = sqlAutoPerformanceCompare.getIdx_ad_no();
		String table_owner = sqlAutoPerformanceCompare.getTable_owner();
		String table_name = sqlAutoPerformanceCompare.getTable_name();

		Long l_tobeDbId = Long.parseLong(tobeDbId);
		Long l_idx_ad_no = Long.parseLong(idx_ad_no);

		result = AutoIndexingSQLPerfVerify.requestInvisibleIndexList(l_tobeDbId, l_idx_ad_no, table_owner, table_name) ;

		return result;
	}

	@Override
	public String autoCreateIndexInvisible(HashMap<String,String> paramMap) throws Exception {
		String tobeDbId = paramMap.get("perf_check_target_dbid");
		String invisible_index_list = paramMap.get("invisible_index_list");
		Long l_tobeDbId = Long.parseLong(tobeDbId);
		ArrayList<String> list = new ArrayList<String>();
		String[] split_list = invisible_index_list.split(",");
		int len = split_list.length;

		for(int iter = 0 ; iter < len ; iter++){
			list.add(split_list[iter]);
		}

		if(logger.isDebugEnabled()){
			logger.debug("perf_check_target_dbid = [{}]\n invisible_index_list = {}",tobeDbId,invisible_index_list.toString());
		}

		return AutoIndexingSQLPerfVerify.requestIndexInvisibleExec(l_tobeDbId, list);
	}

	@Override
	public String setCreateIndexYN(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		String tobeDbId = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String idx_ad_no = sqlAutoPerformanceCompare.getIdx_ad_no();

		Long l_tobeDbId = Long.parseLong(tobeDbId);
		Long l_idx_ad_no = Long.parseLong(idx_ad_no);

		return AutoIndexingSQLPerfVerify.updateIndexCreate(l_tobeDbId, l_idx_ad_no);
	}

	@Override
	public Result forceUpdateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception {
		Result result = new Result();

		String projectId = sqlAutomaticPerformanceCheck.getProject_id();
		String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id();

		try {
			String rtnVal = ProjectPerfChk.threadForceKillAutoIndexing(Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId), StringUtil.getRandomJobKey());
			JSONObject rtnJson = strToJSONObject(rtnVal);

			String err_msg = (String)rtnJson.get("err_msg");
			String is_error = (String)rtnJson.get("is_error");
			logger.debug("err_msg ===> ["+err_msg+"] , is_error ===> ["+is_error+"]");

			if ( err_msg.equals("Complete") == true && is_error.equals("false") == true ) {
				result.setResult( true );
				aisqlpvIndexRecommendDao.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );
			} else {
				logger.debug("err_msg ====> "+err_msg+" , is_error ====> "+ is_error);
				result.setMessage( rtnVal );
				result.setResult( false );
			}

		} catch (Exception ex) {
			throw new Exception(ex);
		}

		return result;
	}

	@Override
	public int countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) {
		return aisqlpvIndexRecommendDao.countExecuteTms( sqlAutoPerformanceCompare );
	}

	@Override
	public int countPerformanceRecord(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return aisqlpvIndexRecommendDao.countPerformanceRecord( sqlAutoPerformanceCompare );
	}

	@Override
	public List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL) throws Exception {
		return aisqlpvIndexRecommendDao.getAuthoritySQL(authoritySQL);
	}
	@Override
	public int setExcuteAnalyzeSqlAutoPerfChk(HashMap<String, String> paramMap) throws Exception {
		return aisqlpvIndexRecommendDao.setExcuteAnalyzeSqlAutoPerfChk(paramMap);
	}

	@Override
	public String getPerfChkAutoIndexingV2(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {

		String projectId = sqlAutoPerformanceCompare.getProject_id();
		String sqlAutoPerfCheckId = sqlAutoPerformanceCompare.getSql_auto_perf_check_id();
		String tobe_dbid = sqlAutoPerformanceCompare.getPerf_check_target_dbid();
		String idx_ad_no = sqlAutoPerformanceCompare.getIdx_ad_no();

		Long l_tobe_dbid = Long.parseLong(tobe_dbid);
		Long l_idx_ad_no = Long.parseLong(idx_ad_no);
		Long l_projectId = Long.parseLong(projectId);
		Long l_sqlAutoPerfCheckId = Long.parseLong(sqlAutoPerfCheckId);

		return ProjectPerfChk.getPerfChkAutoIndexingV2(l_projectId, l_sqlAutoPerfCheckId, l_tobe_dbid, l_idx_ad_no, StringUtil.getRandomJobKey());
	}

	@Override
	public ArrayList<HashMap<String,String>> getAutoError(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return aisqlpvIndexRecommendDao.getAutoError(sqlAutoPerformanceCompare);
	}

	private JSONObject strToJSONObject(String strMsg) throws Exception{
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) jsonParser.parse(strMsg);
		}catch(Exception e) {
			throw e;
		}
		return jsonObj;
	}

}
