package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.SelfTuningDao;
import omc.spop.model.AccPathExec;
import omc.spop.model.ApmApplSql;
import omc.spop.model.DbIndexHistory;
import omc.spop.model.DbTabColumnHistory;
import omc.spop.model.DbaObjects;
import omc.spop.model.DbaTables;
import omc.spop.model.DbaUsers;
import omc.spop.model.IdxAdMst;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunExecutionPlan;
import omc.spop.model.SelftunExecutionPlan1;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.SelftunSql;
import omc.spop.model.SelftunSqlBind;
import omc.spop.model.SelftunSqlStatistics;
import omc.spop.model.Users;
import omc.spop.model.VsqlStats;
import omc.spop.server.tune.AccPathParsingSt;
import omc.spop.server.tune.SPopIndexAutoDesign;
import omc.spop.server.tune.SelfTuneTest;
import omc.spop.service.SelfTuningService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SelfTuningService")
public class SelfTuningServiceImpl implements SelfTuningService {
	
	private static final Logger logger = LoggerFactory.getLogger(SelfTuningServiceImpl.class);
	
	@Autowired
	private SelfTuningDao selfTuningDao;
	
	@Override
	public List<ApmApplSql> databaseListOfWrkjobCd(ApmApplSql apmApplSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		apmApplSql.setUser_id(user_id);

		return selfTuningDao.databaseListOfWrkjobCd(apmApplSql);
	}

	@Override
	public List<ApmApplSql> selfTuningList(ApmApplSql apmApplSql) throws Exception {
		return selfTuningDao.selfTuningList(apmApplSql);
	}
	
	@Override
	public String insertSelftunQuery(ApmApplSql apmApplSql) throws Exception {
		SelftunSql selftunSql = new SelftunSql();
		String selftunQuerySeq = "";
		Users users = null;				/* 로그인 SessionManager의 User 정보로 Reg_id로 사용한다. */
		
		try{
			users = SessionManager.getLoginSession().getUsers();
			
			// 1. SELFTUN MAX SEQ 조회
			selftunQuerySeq = selfTuningDao.getMaxSelftunQuerySeq(apmApplSql);
			
			// 2. SELFTUN_SQL INSERT
			selftunSql.setDbid(apmApplSql.getDbid());
			selftunSql.setSelftun_query_seq(selftunQuerySeq);
			selftunSql.setUuid(apmApplSql.getUuid());
			selftunSql.setReg_id(users.getUser_id());
			logger.debug("uuid[" + apmApplSql.getUuid()+ "]");
			
			selftunSql.setSql_text(apmApplSql.getDefaultText());
			selftunSql.setWrkjob_cd(apmApplSql.getWrkjob_cd());
			
			selfTuningDao.insertSelftunQuery(selftunSql);
		}catch(Exception ex){
			logger.error("SELFTUN_SQL INSERT ERROR ==> " + ex.getMessage());
			throw ex;
		}
		
		return selftunQuerySeq;
	}
	
	@Override
	public List<IdxAdMst> startSelfIndexAutoDesign(ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception {
		IdxAdMst temp = new IdxAdMst();
		String idxAdNo = "";

		// 1. 서버모듈 호출 (AccPathParsingSt.startParse);
		try{
			idxAdNo = AccPathParsingSt.startParse2(StringUtil.parseLong(apmApplSql.getDbid(), 0),
					StringUtil.parseLong(selftunQuerySeq, 0),  apmApplSql.getParsing_schema_name(),
					null,
					StringUtils.defaultString(apmApplSql.getAccess_path_type()),
					apmApplSql.getSelectivity_calc_method());
		}catch(Exception ex){
			logger.error("SERVER ERROR(startParse) ==> " + ex.getMessage());
			throw ex;
		}
		
		// 2. 인덱스 자동 설계 리스트 조회		
		temp.setDbid(apmApplSql.getDbid());
		temp.setIdx_ad_no(idxAdNo);

		return selfTuningDao.idxAdMstList(temp);
	}	
	
	@Override
	public List<IdxAdMst> idxAdMstList(IdxAdMst idxAdMst) throws Exception {
		return selfTuningDao.idxAdMstList(idxAdMst);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownloadIdxAdMstList(IdxAdMst idxAdMst) {
		return selfTuningDao.excelDownloadIdxAdMstList(idxAdMst);
	}
	
	public Result startSelfTest(HttpServletRequest req, ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception {
		Result result = new Result();
		SelftunSqlStatistics statistics = new SelftunSqlStatistics();
		SelftunSqlBind bind = null;
		VsqlStats vsqlStats = new VsqlStats();
		int bindInt = 0;

		String[] bindSeqArr = req.getParameterValues("bind_seq");
		String[] bindVarNmArr = req.getParameterValues("bind_var_nm");
		String[] bindVarTypeArr = req.getParameterValues("bind_var_type");
		String[] bindVarValueArr = req.getParameterValues("bind_var_value");		

		if(bindVarNmArr != null){
			bindInt = bindVarNmArr.length;
		}
		
		String[] bindArry = new String[bindInt];
		
		if(bindVarNmArr != null){
			for(int i = 0; i < bindVarNmArr.length ; i++){
				bindArry[i] = bindVarNmArr[i] + "," + bindVarTypeArr[i] + "," + bindVarValueArr[i].replaceAll(",","@~!");
			}
		}
		
		// 서버모듈 - ExecutionPlan 조회
		try{ 
			result = SelfTuneTest.Get_ExecPlan(StringUtil.parseLong(apmApplSql.getDbid(), 0),
					StringUtil.parseLong(selftunQuerySeq, 0), bindArry, apmApplSql.getParsing_schema_name());
		}catch(Exception ex){
			logger.error("SERVER ERROR(Get_ExecPlan) ==> " + ex.getMessage());
			throw ex;
		}		
		
		// 결과가 정상(true)일 경우..
		if(result.getResult()){
			// 1. SELFTUN_SQL_STATISTICS INSERT
			vsqlStats = (VsqlStats)result.getList().get(1);
			statistics.setDbid(apmApplSql.getDbid());
			statistics.setSelftun_query_seq(vsqlStats.getSelftun_query_seq());
			statistics.setSql_id(vsqlStats.getSql_id());
			statistics.setPlan_hash_value(vsqlStats.getPlan_hash_value());
			statistics.setParse_calls(vsqlStats.getParse_calls());
			statistics.setBuffer_gets(vsqlStats.getBuffer_gets());
			statistics.setRows_processed(vsqlStats.getRows_processed());
			statistics.setElapsed_time(vsqlStats.getElapsed_time());
			statistics.setCpu_time(vsqlStats.getCpu_time());
			statistics.setUser_io_wait_time(vsqlStats.getUser_io_wait_time());
			statistics.setFetches(vsqlStats.getFetches());
			statistics.setDisk_reads(vsqlStats.getDisk_reads());
			statistics.setDirect_writes(vsqlStats.getDirect_writes());
			
			selfTuningDao.insertSelftunSqlStatistics(statistics);
			
			// 2. SELFTUN_SQL_BIND INSERT
			if(bindVarNmArr != null){
				for(int i = 0; i < bindVarNmArr.length ; i++){
					bind = new SelftunSqlBind();
					bind.setDbid(apmApplSql.getDbid());
					bind.setSelftun_query_seq(selftunQuerySeq);
					bind.setBind_seq(bindSeqArr[i]);
					bind.setBind_var_nm(bindVarNmArr[i]);
					bind.setBind_var_value(bindVarValueArr[i]);
					bind.setBind_var_type(bindVarTypeArr[i]);
					
					selfTuningDao.insertSelftunSqlBind(bind);
				}
			}
		}
		
		/*for(int i = 0 ; i < testPlanList.size() ; i++){
			OdsHistSqlText temp = (OdsHistSqlText)testPlanList.get(i);
			if(i == 0){
				sqlId = temp.getExecution_plan();
			}else{
				break;
			}
		}

		// 서버모듈 - 셀프테스트 이력 조회
		try{
			testHistoryList = SelfTuneTest.SqlStat_Only(StringUtil.parseLong(apmApplSql.getDbid(),0), sqlId);
			resultList.add(1, testHistoryList);
		}catch(Exception ex){
			logger.debug("SERVER ERROR(SqlStat_Only) ==> " + ex.getMessage());
			throw ex;
		}*/

		return result;
	}
	
	@Override
	public Result startSelfTestNew(HttpServletRequest req, ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception {
		Result result = new Result();
		SelftunSqlBind bind = null;
		int bindInt = 0;

		String[] bindSeqArr = req.getParameterValues("bind_seq");
		String[] bindVarNmArr = req.getParameterValues("bind_var_nm");
		String[] bindVarTypeArr = req.getParameterValues("bind_var_type");
		String[] bindVarValueArr = req.getParameterValues("bind_var_value");		

		if(bindVarNmArr != null){
			bindInt = bindVarNmArr.length;
		}
		
		String[] bindArry = new String[bindInt];
		
		if(bindVarNmArr != null){
			for(int i = 0; i < bindVarNmArr.length ; i++){
				bindArry[i] = bindVarNmArr[i] + "," + bindVarTypeArr[i] + "," + bindVarValueArr[i].replaceAll(",","@~!");
			}
		}
		
		// 서버모듈 - ExecutionPlan 조회
		// select * from orders where order_id = :order_id
		String execPlanNew = "";
		try{ 
			execPlanNew = SelfTuneTest.getExecPlanNew(StringUtil.parseLong(apmApplSql.getDbid(), 0),
					StringUtil.parseLong(selftunQuerySeq, 0), bindArry, apmApplSql.getParsing_schema_name());
			logger.debug("execPlanNew :"+execPlanNew);
			if (execPlanNew != null) {
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonResult = (JSONObject) jsonParser.parse(execPlanNew);

				String isError = (String) jsonResult.get("is_error");
				String errMsg = (String) jsonResult.get("err_msg");
				if (isError.equals("true")) {
					throw new Exception((String) jsonResult.get("err_msg"));
				}
//				else{
//					if(errMsg.contains("ORA-")) {
//						throw new Exception((String) jsonResult.get("err_msg"));
//					}
//				}

				// SELFTUN_SQL_BIND INSERT
				if(bindVarNmArr != null){
					int insertResult = 0;
					for(int i = 0; i < bindVarNmArr.length ; i++){
						bind = new SelftunSqlBind();
						bind.setDbid(apmApplSql.getDbid());
						bind.setSelftun_query_seq(selftunQuerySeq);
						bind.setBind_seq(bindSeqArr[i]);
						bind.setBind_var_nm(bindVarNmArr[i]);
						bind.setBind_var_value(bindVarValueArr[i]);
						bind.setBind_var_type(bindVarTypeArr[i]);
						
						insertResult += selfTuningDao.insertSelftunSqlBind(bind);
					}
					if(insertResult == bindVarNmArr.length) {
						result.setResult(true);
						result.setMessage("성능점검수행을 완료하였습니다.");
					}
				}				
			}
		}catch(Exception ex){
			logger.error("SERVER ERROR(Get_ExecPlan) ==> " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}		
		return result;
	}	
	
	@Override
	public List<SelftunPlanTable> explainPlanTreeList(ApmApplSql apmApplSql) throws Exception {
		// 서버모듈 - Explain Plan 조회
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		
		try{
			resultList = SelfTuneTest.Get_ExplainPlan(StringUtil.parseLong(apmApplSql.getDbid(),0), apmApplSql.getSql_text(), apmApplSql.getParsing_schema_name());
		}catch(Exception ex){
			logger.error("SERVER ERROR(Get_ExplainPlan) ==> " + ex.getMessage());
			throw ex;
		}
		
		return resultList;
	}
	
	@Override
	public List<DbaUsers> parsingSchemaNameList(ApmApplSql apmApplSql) throws Exception {
		// 서버모듈 - Parsing Schema Name 조회
		List<DbaUsers> resultList = new ArrayList<DbaUsers>();
		
		try{
			resultList = SelfTuneTest.Get_ParsingSchema(StringUtil.parseLong(apmApplSql.getDbid(),0));
		}catch(Exception ex){
			logger.error("SERVER ERROR(Get_ParsingSchema) ==> " + ex.getMessage());
			throw ex;
		}
		
		return resultList;
	}	
	
	@Override
	public Result explainPlanDetailPopup(DbaTables dbaTables) throws Exception {
		Result result = new Result();
		
		try{
			result = SelfTuneTest.SelfTuneExplanPlan(StringUtil.parseLong(dbaTables.getDbid(),0), dbaTables.getOwner(), dbaTables.getTable_name());
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " SERVER ERROR(SelfTuneExplanPlan) ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<AccPathExec> accpathList(DbaTables dbaTables) throws Exception {
		return selfTuningDao.accpathList(dbaTables);
	}
	
	@Override
	public List<DbTabColumnHistory> columnHistoryList(DbaTables dbaTables) throws Exception {
		return selfTuningDao.columnHistoryList(dbaTables);
	}
	
	@Override
	public List<DbIndexHistory> indexHistoryList(DbaTables dbaTables) throws Exception {
		return selfTuningDao.indexHistoryList(dbaTables);
	}
	
	@Override
	public List<DbaObjects> explainPlanStatisticsPopup(DbaObjects dbaObjects) throws Exception {
		return selfTuningDao.explainPlanStatisticsPopup(dbaObjects);
	}
	
	@Override
	public Result selfTuningSqlInfoPopup(SelftunSql selftunSql) throws Exception {
		Result result = new Result();
		List<Object> resultList = new ArrayList<Object>();
		SelftunSql temp = new SelftunSql();
		List<SelftunSqlBind> selftunSqlBindList = new ArrayList<SelftunSqlBind>();
		try {
			temp = selfTuningDao.getSelftunSql(selftunSql);
			selftunSqlBindList = selfTuningDao.selectSelftunSqlBindList(selftunSql);

			resultList.add(0, temp);
			resultList.add(1, selftunSqlBindList);

			result.setResult(true);
			result.setList(resultList);
			result.setMessage("SQL 상세 정보 조회를 완료했습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public List<ApmApplSql> selectPerfCheckResultList(ApmApplSql apmApplSql) {
		return selfTuningDao.selectPerfCheckResultList(apmApplSql);
	}

	@Override
	public List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(SelftunExecutionPlan selftunExecutionPlan) {
		return selfTuningDao.selectDeployPerfChkDetailResultList(selftunExecutionPlan);
	}

	@Override
	public List<PerformanceCheckMng> selectImprovementGuideList(SelftunExecutionPlan selftunExecutionPlan) {
		return selfTuningDao.selectImprovementGuideList(selftunExecutionPlan);
	}

	@Override
	public List<PerformanceCheckMng> selectPerfCheckResultBasisWhy(SelftunExecutionPlan selftunExecutionPlan) {
		return selfTuningDao.selectPerfCheckResultBasisWhy(selftunExecutionPlan);
	}
	
	@Override
	public List<SelftunExecutionPlan1> executionPlan1(SelftunExecutionPlan1 selftunExecutionPlan1) {
		return selfTuningDao.executionPlan1(selftunExecutionPlan1);
	}
}
