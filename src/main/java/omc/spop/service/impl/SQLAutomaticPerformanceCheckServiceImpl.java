package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SQLAutomaticPerformanceCheckDao;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.OdsUsers;
import omc.spop.model.ProjectSqlIdfyCondition;
import omc.spop.model.Result;
import omc.spop.model.AuthoritySQL;
import omc.spop.model.ExplainPlanTree;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.server.tune.ProjectPerfChk;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLAutomaticPerformanceCheckService")
public class SQLAutomaticPerformanceCheckServiceImpl implements SQLAutomaticPerformanceCheckService {
	private static final Logger logger = LoggerFactory.getLogger(SQLAutomaticPerformanceCheckServiceImpl.class);
	
	@Autowired
	private SQLAutomaticPerformanceCheckDao sqlAutomaticPerformanceCheckDao;
	
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public List<SQLAutomaticPerformanceCheck> searchProjectList(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.searchProjectList(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> searchPerformanceCheckId(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.searchPerformanceCheckId(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> loadPerformanceCheckCount(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.loadPerformanceCheckCount(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> loadChartData(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.loadChartData(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> loadPerformanceCheckList(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.loadPerformanceCheckList(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public int countExecuteTms(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.countExecuteTms(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> loadOriginalDb(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.loadOriginalDb(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> maxPerformanceCheckId(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.maxPerformanceCheckId(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public Result insertSqlAutomaticPerformanceCheck(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try {
			int resultValue = sqlAutomaticPerformanceCheckDao.insertSqlAutomaticPerformanceCheck(sqlAutomaticPerformanceCheck);
			
			if(resultValue > 0) {
				result.setResult(true);
				result.setMessage("SQL_AUTO_PERF_CHK에 등록되었습니다.");
				
				resultValue = sqlAutomaticPerformanceCheckDao.insertSqlAutomaticPerformanceCheckTarget(sqlAutomaticPerformanceCheck);
				
				if(resultValue > 0) {
					result.setResult(true);
					result.setMessage("SQL_AUTO_PERF_CHK_TARGET에 등록되었습니다.");
				} else {
					result.setResult(false);
					result.setMessage("SQL_AUTO_PERF_CHK_TARGET에 등록되지 않았습니다.");
				}
				
				String projectId = sqlAutomaticPerformanceCheck.getProject_id();
				String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id();
				
				String jsonResult = ProjectPerfChk.getPerfChk(Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId), StringUtil.getRandomJobKey());
			} else {
				result.setResult(false);
				result.setMessage("SQL_AUTO_PERF_CHK에 등록되지 않았습니다.");
			}
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
			
			throw new Exception(ex);
		}
		
		return result;
	}
	
	@Override
	public Result forceUpdateSqlAutomaticPerformanceCheck(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try {
			int resultValue = sqlAutomaticPerformanceCheckDao.forceUpdateSqlAutomaticPerformanceCheck(sqlAutomaticPerformanceCheck);
			
			if(resultValue > 0) {
				result.setResult(true);
				result.setMessage("SQL_AUTO_PERF_CHK에 수정되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("해당 프로젝트에 SQL 자동 성능 점검 중인 수행회차가 없습니다.");
			}
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());	
		}
		
		return result;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) {
		return sqlAutomaticPerformanceCheckDao.excelDownload(sqlAutomaticPerformanceCheck);
	}
	
	@Override
	public OdsHistSqlText loadExplainSqlText(OdsHistSqlText odsHistSqlText) throws Exception {
		OdsHistSqlText result = new OdsHistSqlText();
		try {
			// DBID, SQL_ID
//			result = PerfMonSqlAna.getSearch(StringUtil.parseLong(odsHistSqlText.getDbid(), 0),
//					odsHistSqlText.getSql_id());
			result = sqlAutomaticPerformanceCheckDao.loadExplainSqlText(odsHistSqlText);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}
	
	@Override
	public List<OdsHistSqlstat> loadExplainBindValue(OdsHistSqlstat odsHistSqlstat) throws Exception {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = sqlAutomaticPerformanceCheckDao.loadExplainBindValue(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		return resultList;
	}
	
	@Override
	public List<ExplainPlanTree> loadExplainBeforePlan(ExplainPlanTree explainPlanTree) throws Exception {
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		try {
			resultList = sqlAutomaticPerformanceCheckDao.loadExplainBeforePlan(explainPlanTree);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}
	
	@Override
	public List<ExplainPlanTree> loadExplainAfterSelectPlan(ExplainPlanTree explainPlanTree) throws Exception {
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		try {
			resultList = sqlAutomaticPerformanceCheckDao.loadExplainAfterSelectPlan(explainPlanTree);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}
	
	@Override
	public List<ExplainPlanTree> loadExplainAfterNotSelectPlan(ExplainPlanTree explainPlanTree) throws Exception {
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		try {
			resultList = sqlAutomaticPerformanceCheckDao.loadExplainAfterNotSelectPlan(explainPlanTree);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}
	
	@Override
	public List<ProjectSqlIdfyCondition> getProjectSqlIdfyConditionList(
			ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.projectSqlIdfyConditionList(projectSqlIdfyCondition);
	}
	@Override
	public List<OdsUsers> getUserNameComboBox(OdsUsers odsUsers) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getUserNameComboBox(odsUsers);
	}
	@Override
	public List<OdsUsers> getProjectUserNameComboBox(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getProjectUserNameComboBox(projectSqlIdfyCondition);
	}
	@Override
	public int saveProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		int saveReturn = 0;
		List<ProjectSqlIdfyCondition> chkCond = sqlAutomaticPerformanceCheckDao.getProjectSqlIdfyConditionCheck(projectSqlIdfyCondition);
		//Map<String, Object> chkCond = sqlAutomaticPerformanceCheckDao.getProjectSqlIdfyConditionCheck(projectSqlIdfyCondition);
		
		if (chkCond == null || chkCond.size() == 0)
		{
			throw new Exception("SQL 성능점검대상범위를 체크할 수 없습니다 .");
		}
		logger.debug("chkCond.size()" + chkCond.size());
		String ownerYn = chkCond.get(0).getOwner_yn();
		String ownerTableYn = chkCond.get(0).getOwner_table_yn();
		String tableYn = chkCond.get(0).getTable_yn();
		String moduleYn = chkCond.get(0).getModule_yn();
		
		logger.debug("getProject_check_target_type_cd : " + projectSqlIdfyCondition.getProject_check_target_type_cd());
		logger.debug("getSql_idfy_cond_type_cd : " + projectSqlIdfyCondition.getSql_idfy_cond_type_cd());
		logger.debug("getCrud_flag : " + projectSqlIdfyCondition.getCrud_flag());
		logger.debug("ownerYn : " + ownerYn);
		logger.debug("ownerTableYn : " + ownerTableYn);
		logger.debug("tableYn : " + tableYn);
		logger.debug("moduleYn : " + moduleYn);
		
		if (projectSqlIdfyCondition.getCrud_flag().equals("C")){
			if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("1")) {
				if(ownerYn.equals("Y"))  throw new Exception("이미 등록된 OWNER 입니다.");
				if(ownerTableYn.equals("Y")) throw new Exception("이미 동일한 OWNER에 대해 TABLE 범위로 프로젝트가 등록되어 있습니다. OWNER 범위로 등록이 필요할 경우 [테이블 일괄등록] 버튼을 클릭하여 해당 TABLE들을 모두 삭제 바랍니다.");
			} else if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("2")) {
				if(tableYn.equals("Y"))  throw new Exception("이미 등록된 TABLE 입니다.");
				if(ownerYn.equals("Y")) throw new Exception("해당 TABLE은 OWNER 범위로 프로젝트가 등록되어 있습니다. TABLE 범위로 등록이 필요할 경우 해당 OWNER를 삭제 바랍니다.");				
			} if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("3")) {
				if(moduleYn.equals("Y"))  throw new Exception("이미 등록된 MODULE 입니다.");
			}
			return sqlAutomaticPerformanceCheckDao.insertProjectSqlIdfyCondition(projectSqlIdfyCondition);
		} else if (projectSqlIdfyCondition.getCrud_flag().equals("U")){
			if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("1")) {
				//if(ownerYn.equals("Y"))  throw new Exception("이미 등록된 OWNER 입니다.");
				if(ownerTableYn.equals("Y")) throw new Exception("이미 동일한 OWNER에 대해 TABLE 범위로 프로젝트가 등록되어 있습니다. OWNER 범위로 등록이 필요할 경우 [테이블 일괄등록] 버튼을 클릭하여 해당 TABLE들을 모두 삭제 바랍니다.");
			} else if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("2")) {
				//if(ownerTableYn.equals("Y"))  throw new Exception("이미 등록된 TABLE 입니다.");
				if(ownerYn.equals("Y")) throw new Exception("해당 TABLE은 OWNER 범위로 프로젝트가 등록되어 있습니다. TABLE 범위로 등록이 필요할 경우 해당 OWNER를 삭제 바랍니다.");				
			} if(projectSqlIdfyCondition.getSql_idfy_cond_type_cd().equals("3")) {
				//if(moduleYn.equals("Y"))  throw new Exception("이미 등록된 MODULE 입니다.");
			}
			return sqlAutomaticPerformanceCheckDao.updateProjectSqlIdfyCondition(projectSqlIdfyCondition);
		} else {
			throw new Exception("저장 상태가 아닙니다 .");
		}
	}
	
	@Override
	public int deleteProjectSqlIdfyCondition(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.deleteProjectSqlIdfyCondition(projectSqlIdfyCondition);
	}
	
	@Override
	public List<OdsUsers> getTableOwner(OdsUsers odsUsers) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getTableOwner(odsUsers);
	}
	
	@Override
	public List<OdsUsers> getSelectTable(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getSelectTable(projectSqlIdfyCondition);
	}
	
	@Override
	public List<OdsUsers> getSelectProjectSqlIdfyConditionTable(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getSelectProjectSqlIdfyConditionTable(projectSqlIdfyCondition);
	}
	
	@Override
	public int saveProjectSqlIdfyConditionPopup(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		int del_row = 0;
		String[] tableOwnerArry = null;
		String[] tableNameArry = null;
		ProjectSqlIdfyCondition projectSqlIdfyConditionIns;
		boolean dupChk = false;
		
		tableOwnerArry = StringUtil.split(projectSqlIdfyCondition.getTableOwnerArry(), "|");			
		tableNameArry = StringUtil.split(projectSqlIdfyCondition.getTableNameArry(), "|");
		
		projectSqlIdfyConditionIns = new ProjectSqlIdfyCondition();
		projectSqlIdfyConditionIns.setProject_id(projectSqlIdfyCondition.getProject_id());
		projectSqlIdfyConditionIns.setSql_idfy_cond_type_cd("2");
		projectSqlIdfyConditionIns.setDbid(projectSqlIdfyCondition.getDbid());
		
		del_row = sqlAutomaticPerformanceCheckDao.deleteProjectSqlIdfyConditionTypeCd(projectSqlIdfyConditionIns);
		
		if (projectSqlIdfyCondition.getCrud_flag().equals("D"))
		{
			return del_row;
		}
		
//		for (int i = 0; i < tableOwnerArry.length; i++) {
//			dupChk = false;
//			logger.debug("tableOwner : " + tableOwnerArry[i]);
//			logger.debug("tableName : " + tableNameArry[i]);
//			for (int j = 0; j < tableOwnerArry.length; j++) {
//				if (tableOwnerArry[i] == tableOwnerArry[j] && tableNameArry[i] == tableNameArry[j]) {
//					logger.debug("Dup tableOwner : " + tableOwnerArry[i]);
//					logger.debug("Dup tableName : " + tableNameArry[i]);
//					dupChk = true;
//					break;
//				}
//			}
//			if (dupChk) continue;
//			
//			projectSqlIdfyConditionIns = new ProjectSqlIdfyCondition();
//			projectSqlIdfyConditionIns.setProject_id(projectSqlIdfyCondition.getProject_id());
//			projectSqlIdfyConditionIns.setSql_idfy_cond_type_cd("2");
//			projectSqlIdfyConditionIns.setOwner(tableOwnerArry[i]);
//			projectSqlIdfyConditionIns.setTable_name(tableNameArry[i]);
//			projectSqlIdfyConditionIns.setModule("");
//
//			sqlAutomaticPerformanceCheckDao.insertProjectSqlIdfyCondition(projectSqlIdfyConditionIns);
//		}
		
		for (int i = 0; i < tableOwnerArry.length; i++) {
			logger.debug("tableOwner : " + tableOwnerArry[i]);
			logger.debug("tableName : " + tableNameArry[i]);
			projectSqlIdfyConditionIns = new ProjectSqlIdfyCondition();
			projectSqlIdfyConditionIns.setProject_id(projectSqlIdfyCondition.getProject_id());
			projectSqlIdfyConditionIns.setProject_check_target_type_cd(projectSqlIdfyCondition.getProject_check_target_type_cd());
			projectSqlIdfyConditionIns.setSql_idfy_cond_type_cd("2");
			projectSqlIdfyConditionIns.setDbid(projectSqlIdfyCondition.getDbid());
			projectSqlIdfyConditionIns.setOwner(tableOwnerArry[i]);
			projectSqlIdfyConditionIns.setTable_name(tableNameArry[i]);
			projectSqlIdfyConditionIns.setModule("");

			sqlAutomaticPerformanceCheckDao.insertProjectSqlIdfyCondition(projectSqlIdfyConditionIns);
		}
		
		return tableOwnerArry.length;
	}
	
	@Override
	public List<ProjectSqlIdfyCondition> getProjectSqlIdfyConditionCheck(ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getProjectSqlIdfyConditionCheck(projectSqlIdfyCondition);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownloadProjectSqlIdfyCondition(
			ProjectSqlIdfyCondition projectSqlIdfyCondition) {
		return sqlAutomaticPerformanceCheckDao.excelDownloadProjectSqlIdfyCondition(projectSqlIdfyCondition);
	}
	
	@Override
	public List<AuthoritySQL> getAuthoritySQL(AuthoritySQL authoritySQL) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getAuthoritySQL(authoritySQL);
	}
	
	@Override
	public List<SQLAutomaticPerformanceCheck> getRoundingInfo(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return sqlAutomaticPerformanceCheckDao.getRoundingInfo(sqlAutomaticPerformanceCheck);
	}
}
