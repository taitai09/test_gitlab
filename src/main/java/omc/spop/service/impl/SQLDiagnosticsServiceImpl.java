package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.SQLDiagnosticsDao;
import omc.spop.model.Cd;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.FullscanSql;
import omc.spop.model.LiteralSql;
import omc.spop.model.NewSql;
import omc.spop.model.NonpredDeleteStmt;
import omc.spop.model.OffloadEffcReduceSql;
import omc.spop.model.OffloadSql;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.SqlDiagSummary;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.SQLDiagnosticsService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLDiagnosticsService")
public class SQLDiagnosticsServiceImpl implements SQLDiagnosticsService {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDiagnosticsServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SQLDiagnosticsDao sqlDiagnosticsDao;
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;
	
	@Override
	public List<Cd> getTableTitleList() throws Exception {
		return sqlDiagnosticsDao.getTableTitleList();
	}	
	
	@Override
	public List<SqlDiagSummary> summaryList(SqlDiagSummary sqlDiagSummary) throws Exception {
		return sqlDiagnosticsDao.summaryList(sqlDiagSummary);
	}
	
	@Override
	public List<SqlDiagSummary> summaryChartList(SqlDiagSummary sqlDiagSummary) throws Exception {
		return sqlDiagnosticsDao.summaryChartList(sqlDiagSummary);
	}	
	
	@Override
	public List<PlanChangeSql> planChangeSqlList(PlanChangeSql planChangeSql) throws Exception {
		return sqlDiagnosticsDao.planChangeSqlList(planChangeSql);
	}
	
	@Override
	public int updatePlanChangeSqlProfile(PlanChangeSql planChangeSql) throws Exception {
		return sqlDiagnosticsDao.updatePlanChangeSqlProfile(planChangeSql);
	}	

	@Override
	public List<NewSql> newSqlList(NewSql newSql) throws Exception {
		return sqlDiagnosticsDao.newSqlList(newSql);
	}
	
	@Override
	public List<LiteralSql> literalSqlList(LiteralSql literalSql) throws Exception {
		return sqlDiagnosticsDao.literalSqlList(literalSql);
	}
	
	@Override
	public List<TempUsageSql> tempOveruseSqlList(TempUsageSql tempUsageSql) throws Exception {
		return sqlDiagnosticsDao.tempOveruseSqlList(tempUsageSql);
	}
	
	@Override
	public List<FullscanSql> fullscanSqlList(FullscanSql fullscanSql) throws Exception {
		return sqlDiagnosticsDao.fullscanSqlList(fullscanSql);
	}
	
	@Override
	public List<NonpredDeleteStmt> deleteWithoutConditionList(NonpredDeleteStmt nonpredDeleteStmt) throws Exception {
		return sqlDiagnosticsDao.deleteWithoutConditionList(nonpredDeleteStmt);
	}
	
	@Override
	public List<TuningTargetSql> insertTuningRequest(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getExt_no());
		
		DatabaseTuner tuner = new DatabaseTuner();
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		int tunerCnt = 0;
		String auto_tuner = "";
		String fieldName = "PLAN_HASH_VALUE";
		
		String[] sqlIdArry = null;
		String[] planHashValueArry = null;
		String[] gatherDayArry = null;
		
		String tuningNoArray = "";
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
		try{
			// 1. 자동선정일 경우 Tuner 조회
			if(tuningTargetSql.getAuto_share().equals("Y")){
				tuner.setDbid(tuningTargetSql.getDbid());
				tunerList = commonDao.getTuner(tuner);
				
				tunerCnt = tunerList.size();
			}
			
			sqlIdArry = StringUtil.split(tuningTargetSql.getSqlIdArry(), "|");			
			planHashValueArry = StringUtil.split(tuningTargetSql.getPlanHashValueArry(), "|");
			gatherDayArry = StringUtil.split(tuningTargetSql.getGatherDayArry(), "|");
			
			for (int i = 0; i < sqlIdArry.length; i++) {
				TuningTargetSql temp = new TuningTargetSql();
				
				// 2. TUNING_TARGET_SEQ MAX TUNING_NO 조회
				String maxTuningNo = improvementManagementDao.getNextTuningNo();
				
				// tuning_no를 하나의 값으로 만들기..
				tuningNoArray += maxTuningNo + ",";
				
				temp.setTuning_no(maxTuningNo);
				temp.setDbid(tuningTargetSql.getDbid());
				temp.setChoice_div_cd(tuningTargetSql.getChoice_div_cd());
				temp.setTable_name(tuningTargetSql.getTable_name());
				temp.setProject_id(StringUtils.defaultString(tuningTargetSql.getProject_id()));
				temp.setTuning_prgrs_step_seq(StringUtils.defaultString(tuningTargetSql.getTuning_prgrs_step_seq()));
				
				if(tuningTargetSql.getAuto_share().equals("Y")){
					auto_tuner = tunerList.get(i%tunerCnt).getTuner_id();
					temp.setPerfr_id(auto_tuner);
				}else{
					temp.setPerfr_id(tuningTargetSql.getPerfr_id());
				}
				
				temp.setTuning_requester_id(user_id);
				temp.setTuning_requester_wrkjob_cd(wrkjob_cd);
				temp.setTuning_requester_tel_num(ext_no);
				temp.setSql_id(sqlIdArry[i]);
				temp.setPlan_hash_value(planHashValueArry[i]);
				temp.setGather_day(gatherDayArry[i]);

				if(tuningTargetSql.getChoice_div_cd().equals("4")){ // FULL SCAN SQL
					temp.setTuning_status_change_why("FULL SCAN 튜닝요청");
				}else if(tuningTargetSql.getChoice_div_cd().equals("5")){ //  PLAN_CHANGE_SQL
					temp.setTuning_status_change_why("플랜변경 SQL 튜닝요청");
					fieldName = "AFTER_PLAN_HASH_VALUE";
				}else if(tuningTargetSql.getChoice_div_cd().equals("6")){ // NEW_SQL
					temp.setTuning_status_change_why("신규 SQL 튜닝요청");
				}else if(tuningTargetSql.getChoice_div_cd().equals("7")){ // TEMP_USAGE_SQL 
					temp.setTuning_status_change_why("TEMP 과다사용 SQL 튜닝요청");
				}else if(tuningTargetSql.getChoice_div_cd().equals("C")){  //TOPSQL
					temp.setTuning_status_change_why("TOP SQL 튜닝요청");
					fieldName = "PLAN_HASH_VALUE ";
				}else if(tuningTargetSql.getChoice_div_cd().equals("D")){ //OFFLOAD_SQL
					fieldName = "PLAN_HASH_VALUE  ";
					temp.setTuning_status_change_why("OFFLOAD 비효율 튜닝요청");
				}else if(tuningTargetSql.getChoice_div_cd().equals("E")){  //OFFLOAD_EFFICIENCY_REDUCE_SQL
					fieldName = "PLAN_HASH_VALUE ";
					temp.setTuning_status_change_why("OFFLOAD 효율저하 튜닝요청");
				}
				
				temp.setField_name(fieldName); // PLAN_CHANGE_SQL 테이블에 사용되는 PLAN_HASH_VALUE가 AFTER_PLAN_HASH_VALUE임
				
				// 3. TUNING_TARGET_SQL INSERT
				sqlDiagnosticsDao.insertTuningTargetSql(temp);
				
				// 4. 각 테이블의 튜닝대상선정 UPDATE
				sqlDiagnosticsDao.updateTuningTarget(temp);
				
				// 5. SQL_TUNING_STATUS_HISTORY INSERT
				temp.setTuning_status_cd("3");
				temp.setTuning_status_changer_id(temp.getTuning_requester_id());
//				sqlTuningTargetDao.insertTuningStatusHistory(temp);
				improvementManagementDao.insertTuningStatusHistory(temp);
			}
			
			// 튜닝담당자 할당 건수 조회
			tuningNoArray = StringUtil.Right(tuningNoArray,1);
			tuningTargetSql.setTuning_no_array(tuningNoArray);
			perfrIdList = sqlDiagnosticsDao.perfrIdAssignCountList(tuningTargetSql);
			
		} catch (Exception ex){
			logger.error("error ==> " + ex.getMessage());
			throw ex;
		}
		return perfrIdList;
	}

	@Override
	public List<TopSql> topSqlList(TopSql topSql) {
		return sqlDiagnosticsDao.topSqlList(topSql);
	}

	@Override
	public List<OffloadSql> offloadSqlList(OffloadSql offloadSql) {
		return sqlDiagnosticsDao.offloadSqlList(offloadSql);
	}

	@Override
	public List<OffloadEffcReduceSql> offloadEffcReduceSqlList(OffloadEffcReduceSql offloadEffcReduceSql) {
		return sqlDiagnosticsDao.offloadEffcReduceSqlList(offloadEffcReduceSql);
	}

	@Override
	public List<LiteralSql> literalSqlStatusList(LiteralSql literalSql) {
 		return sqlDiagnosticsDao.literalSqlStatusList(literalSql);
	}

	@Override
	public Vector<String> getExceptionList(String string) {
 		return sqlDiagnosticsDao.getExceptionList(string);
	}		
}