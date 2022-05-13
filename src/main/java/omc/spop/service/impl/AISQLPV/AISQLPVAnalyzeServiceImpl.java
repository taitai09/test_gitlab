package omc.spop.service.impl.AISQLPV;

import omc.spop.dao.AISQLPVAnalyzeDao;
import omc.spop.dao.AISQLPVDao;
import omc.spop.dao.CommonDao;
import omc.spop.model.Project;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.server.tune.AutoIndexingSQLPerfVerify;
import omc.spop.server.tune.ProjectPerfChk;
import omc.spop.service.AISQLPVAnalyzeService;
import omc.spop.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static omc.spop.utils.SysUtil.strToJSONObject;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationAnalyzeServiceImpl
 **********************************************************/

@Service("AISQLPVAnalyzeService")
public class AISQLPVAnalyzeServiceImpl implements AISQLPVAnalyzeService {

	@Autowired CommonDao commonDao;
	@Autowired AISQLPVDao aisqlpvDao;
	@Autowired AISQLPVAnalyzeDao aisqlpvAnalyzeDao;

	@Override
	public List<Project> getProjectList(Project project) throws Exception {
		return commonDao.projectList(project);
	}

	@Override
	public List<SQLAutoPerformanceCompare> getProjectPerformancePacData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		if(sqlAutoPerformanceCompare.getOneRow().equals("Y")){
			return aisqlpvAnalyzeDao.getProjectPerformancePacData_OneRow(sqlAutoPerformanceCompare);
		} else {
			return aisqlpvAnalyzeDao.getProjectPerformancePacData(sqlAutoPerformanceCompare);
		}
	}

	@Override
	public List<SQLAutoPerformanceCompare> getPerformancePacData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return aisqlpvAnalyzeDao.getPerformancePacData(sqlAutoPerformanceCompare);
	}

	@Override
	public List<SQLAutoPerformanceCompare> getExcuteAnalyzeConstraint(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {

		List<SQLAutoPerformanceCompare> resultList = aisqlpvAnalyzeDao.getExecutionConstraint(sqlAutoPerformanceCompare);
		return resultList;
	}

	@Override
	public void excuteAnalyze(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {

		Long original_dbid = Long.parseLong(sqlAutoPerformanceCompare.getOriginal_dbid());
		Long project_id = Long.parseLong(sqlAutoPerformanceCompare.getProject_id());
		Long sqlAutoPerfCheckId = Long.parseLong(sqlAutoPerformanceCompare.getSql_auto_perf_check_id());

		new AutoIndexingSQLPerfVerify().indexAutoAnalyseExec(original_dbid, project_id, sqlAutoPerfCheckId);
	}

	@Override
	public ArrayList<HashMap<String, String>> excuteAISQLPVAnalyze() throws Exception {
		return null;
	}

	@Override
	public String forceCompleteAnalyze(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		Long original_dbid = Long.parseLong(sqlAutoPerformanceCompare.getOriginal_dbid());
		Long project_id = Long.parseLong(sqlAutoPerformanceCompare.getProject_id());
		Long sqlAutoPerfCheckId = Long.parseLong(sqlAutoPerformanceCompare.getSql_auto_perf_check_id());

		return new AutoIndexingSQLPerfVerify().forceKillIndexAutoAnalyseExec(original_dbid, project_id, sqlAutoPerfCheckId);
	}

	@Override
	public ArrayList<HashMap<String, String>> getExcutedAISQLPVAnalyzeData() throws Exception {
		return null;
	}

	@Override
	public List<SQLAutoPerformanceCompare> getOriginalDB(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		List<SQLAutoPerformanceCompare> finalList = new ArrayList<SQLAutoPerformanceCompare>();
		SQLAutoPerformanceCompare temp = new SQLAutoPerformanceCompare();

		String isAll = StringUtils.defaultString(sqlAutoPerformanceCompare.getIsAll());
		String isChoice = StringUtils.defaultString(sqlAutoPerformanceCompare.getIsChoice());

		if (isChoice.equals("Y")) {
			temp.setOriginal_dbid("");
			temp.setOriginal_db_name("선택");
		} else if (isAll.equals("Y")) {
			temp.setOriginal_dbid("");
			temp.setOriginal_db_name("전체");
		}

		resultList = aisqlpvAnalyzeDao.getOriginalDB(sqlAutoPerformanceCompare);

		finalList.add(temp);
		finalList.addAll(resultList);

		return finalList;
	}

	@Override
	public int updateSqlAutoPerfChk( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		return aisqlpvAnalyzeDao.updateSqlAutoPerfChk(sqlAutoPerformanceCompare);
	}


	@Override
	public String getRecommendIndexDbYn(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		String msg = "";

		String str_ProjectId = sqlAutoPerformanceCompare.getProject_id();
		String str_SqlAutoPerfCheckId = sqlAutoPerformanceCompare.getSql_auto_perf_check_id();
		String databaseKindsCd = sqlAutoPerformanceCompare.getDatabase_kinds_cd();

		Long projectId = Long.parseLong(str_ProjectId);
		Long sqlAutoPerfCheckId = Long.parseLong(str_SqlAutoPerfCheckId);

		AutoIndexingSQLPerfVerify autoIndexingSQLPerfVerify = new AutoIndexingSQLPerfVerify();
		msg = autoIndexingSQLPerfVerify.recommendIndexDbYn(projectId, sqlAutoPerfCheckId, databaseKindsCd);

		return msg;
	}
}
