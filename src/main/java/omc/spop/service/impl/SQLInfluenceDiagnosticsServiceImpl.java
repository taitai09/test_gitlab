package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.SQLInfluenceDiagnosticsDao;
import omc.spop.model.BeforePerfExpect;
import omc.spop.model.BeforePerfExpectSqlStat;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.OdsUsers;
import omc.spop.model.SqlPerfImplAnalSql;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.model.SqlPerfImpluenceAnalysis;
import omc.spop.service.SQLInfluenceDiagnosticsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLInfluenceDiagnosticsService")
public class SQLInfluenceDiagnosticsServiceImpl implements SQLInfluenceDiagnosticsService {
	/**
	 * 즉시 실행 상수 값
	 * 분단위
	 */
	private static final String INST_EXEC_MIN = "2";

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SQLInfluenceDiagnosticsDao sqlInfluenceDiagnosticsDao;

	@Override
	public List<BeforePerfExpect> topSQLDiagnosticsList(BeforePerfExpect beforePerfExpect) throws Exception {
		return sqlInfluenceDiagnosticsDao.topSQLDiagnosticsList(beforePerfExpect);
	}
	
	@Override
	public BeforePerfExpect getTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception {
		return sqlInfluenceDiagnosticsDao.getTOPSQLDiagnostics(beforePerfExpect);
	}
	
	@Override
	public int saveTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		int rowCnt = 0;		
		String beforePerfExpectNo = "";
		String nowDate = "";
		String nowTime = "";
		String execCycle = "";
		JobSchedulerConfigDetail jobSchedulerConfigDetail = new JobSchedulerConfigDetail();
		
		// 즉시실행 선택시 수행일자, 수행시간 NULL 처리
		if(beforePerfExpect.getImmediately_yn().equals("Y")){
			nowDate = DateUtil.getNowDate("yyyyMMdd");
			//nowTime = StringUtil.replace(DateUtil.getNextTime("P","5"),":",""); // 즉시 실행인 경우 5분후 실행으로 입력
			nowTime = StringUtil.replace(DateUtil.getNextTime("P",INST_EXEC_MIN),":",""); // 즉시 실행인 경우 즉시실행상수값을 입력
			
			beforePerfExpect.setExpect_work_exec_day(nowDate);
			beforePerfExpect.setExpect_work_exec_time(nowTime);
		}else{
			beforePerfExpect.setExpect_work_exec_day(StringUtil.replace(beforePerfExpect.getExpect_work_exec_day(),"-",""));
			beforePerfExpect.setExpect_work_exec_time(StringUtil.replace(beforePerfExpect.getExpect_work_exec_time(),":","")+"00");
		}
		
		jobSchedulerConfigDetail.setJob_scheduler_type_cd("23"); // TOPSQL성능영향도진단
		execCycle = StringUtil.getExecCycleFormat(beforePerfExpect.getExpect_work_exec_day(), beforePerfExpect.getExpect_work_exec_time(), "O"); // date, time, cycle
		jobSchedulerConfigDetail.setExec_cycle(execCycle);
		jobSchedulerConfigDetail.setUse_yn("Y");
		jobSchedulerConfigDetail.setUpd_id(user_id);
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(beforePerfExpect.getExpect_work_exec_day(), beforePerfExpect.getExpect_work_exec_time()));
		jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(beforePerfExpect.getExpect_work_exec_day(), beforePerfExpect.getExpect_work_exec_time()));
		jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(beforePerfExpect.getExpect_work_exec_day(), beforePerfExpect.getExpect_work_exec_time()));
		
		if(beforePerfExpect.getBefore_perf_expect_no() != null && !beforePerfExpect.getBefore_perf_expect_no().equals("")){ // 수정
			rowCnt = sqlInfluenceDiagnosticsDao.updateTOPSQLDiagnostics(beforePerfExpect);
			
			// JOB_SCHEDULER_CONFIG_DETAIL UPDATE
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(beforePerfExpect.getBefore_perf_expect_no());
			
			commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		}else{
			rowCnt = sqlInfluenceDiagnosticsDao.insertTOPSQLDiagnostics(beforePerfExpect);
			
			// JOB_SCHEDULER_CONFIG_DETAIL INSERT
			beforePerfExpectNo = sqlInfluenceDiagnosticsDao.getMaxBeforePerfExpectNo(beforePerfExpect);
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(beforePerfExpectNo);
			
			commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		}

		return rowCnt;
	}
	
	@Override
	public void deleteTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception {
		sqlInfluenceDiagnosticsDao.deleteTOPSQLDiagnostics(beforePerfExpect);
	}
	
	@Override
	public List<BeforePerfExpectSqlStat> topSQLDiagnosticsDetailList(BeforePerfExpectSqlStat beforePerfExpectSqlStat) throws Exception {
		return sqlInfluenceDiagnosticsDao.topSQLDiagnosticsDetailList(beforePerfExpectSqlStat);
	}
	
	@Override
	public int updateSQLProfile_TOPSQL(BeforePerfExpectSqlStat beforePerfExpectSqlStat) throws Exception {
		return sqlInfluenceDiagnosticsDao.updateSQLProfile_TOPSQL(beforePerfExpectSqlStat);
	}
	
	@Override
	public List<SqlPerfImpluenceAnalysis> objectImpactDiagnosticsList(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception {
		return sqlInfluenceDiagnosticsDao.objectImpactDiagnosticsList(sqlPerfImpluenceAnalysis);
	}
	
	@Override
	public SqlPerfImpluenceAnalysis getObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception {
		return sqlInfluenceDiagnosticsDao.getObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
	}
	
	@Override
	public List<SqlPerfImplAnalTable> getTargetTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception {
		return sqlInfluenceDiagnosticsDao.getTargetTableList(sqlPerfImplAnalTable);
	}
	
	@Override
	public List<OdsUsers> getTableOwner(OdsUsers odsUsers) throws Exception {
		return sqlInfluenceDiagnosticsDao.getTableOwner(odsUsers);
	}
	
	@Override
	public List<SqlPerfImplAnalTable> getSelectTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception {
		return sqlInfluenceDiagnosticsDao.getSelectTableList(sqlPerfImplAnalTable);
	}		
	
	@Override
	public int saveObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		int rowCnt = 0;				
		SqlPerfImplAnalTable sqlPerfImplAnalTable;
		String sqlPerfImplAnalNo = "";
		String nowDate = "";
		String nowTime = "";
		String execCycle = "";
		String[] tableOwnerArry = null;
		String[] tableNameArry = null;
		JobSchedulerConfigDetail jobSchedulerConfigDetail = new JobSchedulerConfigDetail();
		
		tableOwnerArry = StringUtil.split(sqlPerfImpluenceAnalysis.getTableOwnerArry(), "|");			
		tableNameArry = StringUtil.split(sqlPerfImpluenceAnalysis.getTableNameArry(), "|");
		
		// 즉시실행 선택시 수행일자, 수행시간 NULL 처리
		if(sqlPerfImpluenceAnalysis.getImmediately_yn().equals("Y")){
			nowDate = DateUtil.getNowDate("yyyyMMdd");
			//nowTime = StringUtil.replace(DateUtil.getNextTime("P","5"),":",""); // 즉시 실행인 경우 5분후 실행으로 입력
			nowTime = StringUtil.replace(DateUtil.getNextTime("P",INST_EXEC_MIN),":",""); // 즉시 실행인 경우 즉시실행상수값을 입력
			
			sqlPerfImpluenceAnalysis.setAnal_work_exec_day(nowDate);
			sqlPerfImpluenceAnalysis.setAnal_work_exec_time(nowTime);
		}else{
			sqlPerfImpluenceAnalysis.setAnal_work_exec_day(StringUtil.replace(sqlPerfImpluenceAnalysis.getAnal_work_exec_day(),"-",""));
			sqlPerfImpluenceAnalysis.setAnal_work_exec_time(StringUtil.replace(sqlPerfImpluenceAnalysis.getAnal_work_exec_time(),":","")+"00");
		}
		
		jobSchedulerConfigDetail.setJob_scheduler_type_cd("24"); // 오브젝트변경SQL성능영향도진단
		execCycle = StringUtil.getExecCycleFormat(sqlPerfImpluenceAnalysis.getAnal_work_exec_day(), sqlPerfImpluenceAnalysis.getAnal_work_exec_time(), "O"); // date, time, cycle
		jobSchedulerConfigDetail.setExec_cycle(execCycle);
		jobSchedulerConfigDetail.setUse_yn("Y");
		jobSchedulerConfigDetail.setUpd_id(user_id);
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(sqlPerfImpluenceAnalysis.getAnal_work_exec_day(), sqlPerfImpluenceAnalysis.getAnal_work_exec_time()));
		jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(sqlPerfImpluenceAnalysis.getAnal_work_exec_day(), sqlPerfImpluenceAnalysis.getAnal_work_exec_time()));
		jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(sqlPerfImpluenceAnalysis.getAnal_work_exec_day(), sqlPerfImpluenceAnalysis.getAnal_work_exec_time()));

		if(sqlPerfImpluenceAnalysis.getSql_perf_impl_anal_no() != null && !sqlPerfImpluenceAnalysis.getSql_perf_impl_anal_no().equals("")){ // 수정
			// 1. SQL_PERF_IMPLUENCE_ANALYSIS UPDATE
			rowCnt = sqlInfluenceDiagnosticsDao.updateObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
			
			// 2. SQL_PERF_IMPL_ANAL_TABLE DELETE
			sqlInfluenceDiagnosticsDao.deleteObjectImpactDiagnosticsTable(sqlPerfImpluenceAnalysis);
			
			// 3. SQL_PERF_IMPL_ANAL_TABLE INSERT
			for (int i = 0; i < tableOwnerArry.length; i++) {
				sqlPerfImplAnalTable = new SqlPerfImplAnalTable();
				sqlPerfImplAnalTable.setSql_perf_impl_anal_no(sqlPerfImpluenceAnalysis.getSql_perf_impl_anal_no());
				sqlPerfImplAnalTable.setTable_owner(tableOwnerArry[i]);
				sqlPerfImplAnalTable.setTable_name(tableNameArry[i]);
				
				sqlInfluenceDiagnosticsDao.insertObjectImpactDiagnosticsTable(sqlPerfImplAnalTable);
			}
			
			// 4. JOB_SCHEDULER_CONFIG_DETAIL UPDATE
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(sqlPerfImpluenceAnalysis.getSql_perf_impl_anal_no());
			
			commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);			
		}else{ // 등록
			// 1. SQL_PERF_IMPLUENCE_ANALYSIS INSERT
			rowCnt = sqlInfluenceDiagnosticsDao.insertObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
			
			// 2. SQL_PERF_IMPL_ANAL_TABLE INSERT
			for (int i = 0; i < tableOwnerArry.length; i++) {
				sqlPerfImplAnalTable = new SqlPerfImplAnalTable();
				sqlPerfImplAnalTable.setTable_owner(tableOwnerArry[i]);
				sqlPerfImplAnalTable.setTable_name(tableNameArry[i]);
				
				sqlInfluenceDiagnosticsDao.insertObjectImpactDiagnosticsTable(sqlPerfImplAnalTable);
			}
			
			// 3. JOB_SCHEDULER_CONFIG_DETAIL INSERT
			sqlPerfImplAnalNo = sqlInfluenceDiagnosticsDao.getSqlPerfImplAnalNo(sqlPerfImpluenceAnalysis);
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(sqlPerfImplAnalNo);
			
			commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail);	
		}

		return rowCnt;
	}
	
	@Override
	public void deleteObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception {
		sqlInfluenceDiagnosticsDao.deleteObjectImpactDiagnosticsTable(sqlPerfImpluenceAnalysis);
		sqlInfluenceDiagnosticsDao.deleteObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
	}
	
	@Override
	public List<SqlPerfImplAnalTable> objectImpactDiagnosticsTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception {
		return sqlInfluenceDiagnosticsDao.objectImpactDiagnosticsTableList(sqlPerfImplAnalTable);
	}
	
	@Override
	public List<SqlPerfImplAnalSql> objectImpactDiagnosticsTableDetailList(SqlPerfImplAnalSql sqlPerfImplAnalSql) throws Exception {
		return sqlInfluenceDiagnosticsDao.objectImpactDiagnosticsTableDetailList(sqlPerfImplAnalSql);
	}
	
	@Override
	public int updateSQLProfile_Object(SqlPerfImplAnalSql sqlPerfImplAnalSql) throws Exception {
		return sqlInfluenceDiagnosticsDao.updateSQLProfile_Object(sqlPerfImplAnalSql);
	}
}