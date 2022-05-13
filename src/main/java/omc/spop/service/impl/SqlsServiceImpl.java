package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import difflib.DiffRow;
import difflib.DiffRow.Tag;
import difflib.DiffRowGenerator;
import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.SQLTuningTargetDao;
import omc.spop.dao.SqlsDao;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.SqlsDetail;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.SqlsService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2020.05.13 	명성태 	최초작성
 **********************************************************/

@Service("sqlsService")
public class SqlsServiceImpl implements SqlsService {
	private static final Logger logger = LoggerFactory.getLogger(SqlsServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SQLTuningTargetDao sqlTuningTargetDao;
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;
	
	@Autowired
	private SqlsDao sqlsDao;

	@Override
	public List<Sqls> loadSqls(Sqls sqls) {
		return sqlsDao.loadSqls(sqls);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDown(Sqls sqls) {
		return sqlsDao.excelDown(sqls);
	}
	
	@Override
	public List<Sqls> beforeOperationPerformance(Sqls sqls) {
		return sqlsDao.beforeOperationPerformance(sqls);
	}
	
	@Override
	public List<Sqls> performanceCheck(Sqls sqls) {
		return sqlsDao.performanceCheck(sqls);
	}
	
	@Override
	public List<Sqls> afterDistributionOperationPerformance(Sqls sqls) {
		return sqlsDao.afterDistributionOperationPerformance(sqls);
	}
	
	@Override
	public List<Sqls> sqlTextPerformanceCheck(Sqls sqls) {
		return sqlsDao.sqlTextPerformanceCheck(sqls);
	}
	
	@Override
	public List<Sqls> sqlBindPerformanceCheck(Sqls sqls) {
		return sqlsDao.sqlBindPerformanceCheck(sqls);
	}
	
	@Override
	public List<Sqls> sqlPlanPerformanceCheck(Sqls sqls) {
		List<Sqls> originSqls = sqlsDao.sqlPlanPerformanceCheck(sqls);
		List<Sqls> revisedSqls = sqlsDao.sqlPlanOperation(sqls);
		
		List<String> original = new ArrayList<String>();
		List<String> revised = new ArrayList<String>();
		
		List<DiffRow> rows = makeDiffList(originSqls, revisedSqls, original, revised, sqls);
		
		List<Sqls> resultList =  new ArrayList<Sqls>();
		Sqls emptySql = null;
		
		int oldNum = 0;
		
		if(originSqls.size() < 1) {
			return resultList;
		}
		
		for (int i = 0; i<rows.size(); i++) {
			emptySql = new Sqls();
			
			if ( "EQUAL".equals(rows.get(i).getTag().toString()) ) {
				originSqls.get(oldNum).setDiffTag(Tag.EQUAL.toString());
				resultList.add( originSqls.get(oldNum) );
				
				oldNum++;
				
			}else if( "INSERT".equals(rows.get(i).getTag().toString()) ){
				// insert인경우 (text가 newLine에만 있는 경우)
				emptySql.setDiffTag(Tag.INSERT.toString());
				resultList.add( emptySql );
				
			}else if( "DELETE".equals(rows.get(i).getTag().toString()) ){
				// delete인 경우 (text가 orininLine에만 있는 경우)
				originSqls.get(oldNum).setDiffTag(Tag.DELETE.toString());
				resultList.add( originSqls.get(oldNum) );
				
				oldNum++;
				
			}else {
				// change인 경우 insert와 delete를 다시한번 구분하여 세팅
				if( "".equals(rows.get(i).getOldLine()) ) {
					emptySql.setDiffTag(Tag.INSERT.toString());
					resultList.add( emptySql );
					
				}else {
					originSqls.get(oldNum).setDiffTag( Tag.CHANGE.toString() );
					resultList.add( originSqls.get(oldNum) );
					oldNum++;
				}
				
				if( "".equals(rows.get(i).getNewLine()) ) {
					resultList.get(i).setDiffTag(Tag.DELETE.toString());
				}
			}
			logger.debug("|"+resultList.get(i).getDiffTag()
						+"|" +resultList.get(i).getCost_percent()
						+"|"+resultList.get(i).getCpu_cost_percent()
						+"|"+resultList.get(i).getIo_cost_percent()
						+"|"+resultList.get(i).getOperation()
						+"|"+resultList.get(i).getCost()
						+"|"+resultList.get(i).getCpu_cost()
						+"|"+resultList.get(i).getIo_cost()
						+"|");
		}
		// return 하지 않는 리스트 데이터 비움
		original = null;
		revised = null;
		originSqls = null;
		revisedSqls = null;
		rows = null;
		
		return resultList;
	}
	
	@Override
	public List<Sqls> sqlTextAll(Sqls sqls) {
		return sqlsDao.sqlTextAll(sqls);
	}
	
	@Override
	public List<Sqls> sqlBindOperation(Sqls sqls) {
		return sqlsDao.sqlBindOperation(sqls);
	}
	@Override
	public List<Sqls> sqlPlanOperation(Sqls sqls) {
		List<Sqls> originSqls = sqlsDao.sqlPlanPerformanceCheck(sqls);
		List<Sqls> revisedSqls = sqlsDao.sqlPlanOperation(sqls);
		
		List<String> original = new ArrayList<String>();
		List<String> revised = new ArrayList<String>();
		
		List<DiffRow> rows = makeDiffList(originSqls, revisedSqls, original, revised, sqls);
		
		List<Sqls> resultList =  new ArrayList<Sqls>();
		Sqls emptySql = new Sqls();
		
		int newNum = 0;
		
		if(revisedSqls.size() < 1) {
			return resultList;
		} else {
			logger.debug("####testData == "+revisedSqls.get(newNum).toString());
		}
		
		if ( rows.size() > 0 ) {
		
			for (int i = 0; i<rows.size(); i++) {
				
				if ( "EQUAL".equals(rows.get(i).getTag().toString()) ) {
					revisedSqls.get(newNum).setDiffTag(Tag.EQUAL.toString());
					
					resultList.add( revisedSqls.get(newNum) );
					newNum++;
					
				} else if( "INSERT".equals(rows.get(i).getTag().toString()) ){
					// insert인경우 (text가 newLine에만 있는 경우)
					revisedSqls.get(newNum).setDiffTag(Tag.INSERT.toString());
					resultList.add( revisedSqls.get(newNum) );
					newNum++;
					
				} else if( "DELETE".equals(rows.get(i).getTag().toString()) ){
					// delete인 경우 (text가 orininLine에만 있는 경우)
					emptySql.setDiffTag(Tag.DELETE.toString());
					resultList.add( emptySql );
					
				} else {
					// change인 경우 insert와 delete를 다시한번 구분하여 세팅
					if( "".equals(rows.get(i).getNewLine()) ) {
						emptySql.setDiffTag(Tag.DELETE.toString());
						emptySql.setOperation("");
						
						resultList.add( emptySql );
						
					} else {
						revisedSqls.get(newNum).setDiffTag( Tag.CHANGE.toString() );
						resultList.add( revisedSqls.get(newNum) );
						newNum++;
					}
					
					if( "".equals(rows.get(i).getOldLine()) ) {
						resultList.get(i).setDiffTag(Tag.INSERT.toString());
					}
				}
				logger.debug("|"+resultList.get(i).getDiffTag()
							+"|" +resultList.get(i).getCost_percent()
							+"|"+resultList.get(i).getCpu_cost_percent()
							+"|"+resultList.get(i).getIo_cost_percent()
							+"|"+resultList.get(i).getOperation()
							+"|"+resultList.get(i).getCost()
							+"|"+resultList.get(i).getCpu_cost()
							+"|"+resultList.get(i).getIo_cost()
							+"|");
			}
			// return 하지 않는 리스트 데이터 비움
			original = null;
			revised = null;
			originSqls = null;
			revisedSqls = null;
			rows = null;
		}
		
		return resultList;
	}
	
	private List<DiffRow> makeDiffList(
			List<Sqls> originSqls, List<Sqls> revisedSqls, List<String> original, List<String> revised, Sqls sqls) {
		
		for(Sqls sql: originSqls) {
			original.add( sql.getOperation() );
		}
		
		for(Sqls sql: revisedSqls) {
			revised.add( sql.getOperation() );
		}
		
		DiffRowGenerator generator = new DiffRowGenerator.Builder()
										.showInlineDiffs(true)
										.ignoreWhiteSpaces(true)
										.ignoreBlankLines(true)
										.build();
		
		return generator.generateDiffRows(original, revised);
	}
	
	@Override
	public List<Sqls> bigTableThresholdCnt(Sqls sqls) {
		return sqlsDao.bigTableThresholdCnt(sqls);
	}
	
	@Override
	public Sqls loadPerfCheckAllPgm(Sqls sqls) throws Exception {
		return sqlsDao.loadPerfCheckAllPgm(sqls);
	}
	
	@Override
	public List<Sqls> sqlStatTrend(Sqls sqls) throws Exception {
		return sqlsDao.sqlStatTrend(sqls);
	}
	
	@Override
	public Result insertTuningRequest(Sqls sqls) throws Exception {
		Result result = new Result();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		
		DatabaseTuner tuner = null;
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		int tunerCnt = 0;
		String[] dbidArray = null;
		String[] sqlIdArray = null;
		String[] planHashValueArray = null;
		String[] perfCheckStepIdArray = null;
		String[] wrkjobCdArray = null;
		String[] executionsArray = null;
		String[] avgBufferGetsArray = null;
		String[] avgElapsedTimeArray = null;
		String[] avgRowProcessedArray = null;
		String[] perfCheckIdArray = null;
		String[] programIdArray = null;
		String[] dbioArray = null;
		String choiceTms = "";
		String next_tuning_no = "";
		String perfr_id = sqls.getPerfr_id();
		String dbid = "";
		String choice_div_cd = "F";
		String tuning_status_cd = "3";
		
		String module = "";
		String max_buffer_gets = "";
		String total_buffer_gets = "";
		String max_elapsed_time = "";
		String avg_cpu_time = "";
		String avg_disk_reads= "";
		String ratio_buffer_get_total = "";
		String ratio_cpu_total = "";
		String ratio_cpu_per_executions = "";
		String program_id = sqls.getProject_id();
		String tuning_prgrs_step_seq = sqls.getTuning_prgrs_step_seq();
		String tuningNoArray = "";
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
		String auto_tuner = "";
		final String TUNING_STATUS_CHANGE_WHY = "배포후성능점검 튜닝대상선정 및 접수";
		String bindSetSeq = "";
		String programExecuteTms = "";
		Sqls tempSqls = null;
		int resultCount = 0;
		List<Object> list = null;
		String message = "";
		int requestTuningTotalCount = 0;
		
		try{
			TuningTargetSql temp = null;
			
			dbidArray = StringUtil.split(sqls.getDbid_array(), "[]");
			sqlIdArray = StringUtil.split(sqls.getAfter_prd_sql_id_array(), "[]");
			planHashValueArray = StringUtil.split(sqls.getAfter_prd_plan_hash_value_array(), "[]");
			perfCheckStepIdArray = StringUtil.split(sqls.getPerf_check_step_id_array(), "[]");
			wrkjobCdArray = StringUtil.split(sqls.getWrkjob_cd_array(), "[]");
			executionsArray = StringUtil.split(sqls.getExecutions_array(), "[]");
			avgBufferGetsArray = StringUtil.split(sqls.getAvg_buffer_gets_array(), "[]");
			avgElapsedTimeArray = StringUtil.split(sqls.getAvg_elapsed_time_array(), "[]");
			avgRowProcessedArray = StringUtil.split(sqls.getAvg_row_processed_array(), "[]");
			perfCheckIdArray = StringUtil.split(sqls.getPerf_check_id_array(), "[]");
			programIdArray = StringUtil.split(sqls.getProgram_id_array(), "[]");
			dbioArray = StringUtil.split(sqls.getDbio_array(), "[]");
			
			requestTuningTotalCount = sqlIdArray.length;
			
			for (int i = 0; i < requestTuningTotalCount; i++) {
				temp = new TuningTargetSql();
				
				next_tuning_no = improvementManagementDao.getNextTuningNo();
				
				tuningNoArray += next_tuning_no + ",";
				
				temp.setTuning_no(next_tuning_no);
				
				dbid = dbidArray[i];
				
				temp.setDbid(dbid);
				temp.setSql_id(sqlIdArray[i]);
				
				if ( sqls.getChoice_div_cd() != null && "".equals( sqls.getChoice_div_cd() ) == false ) {
					temp.setChoice_div_cd( sqls.getChoice_div_cd() );
				} else {
					temp.setChoice_div_cd(choice_div_cd);
				}
				
				temp.setTuning_status_cd(tuning_status_cd);
				
				// 1. 자동선정일 경우 Tuner 조회
				if(sqls.getAuto_share().equals("Y")) {
					tuner = new DatabaseTuner();
					tuner.setDbid(dbid);
					tunerList = commonDao.getTuner(tuner);
					tunerCnt = tunerList.size();
					
					auto_tuner = tunerList.get(i%tunerCnt).getTuner_id();
					temp.setPerfr_id(auto_tuner);
				} else {
					temp.setPerfr_id(perfr_id);
				}
				
				temp.setTuning_requester_id( user_id );
				temp.setTuning_requester_wrkjob_cd( wrkjob_cd );
				temp.setTuning_requester_tel_num( ext_no );
				temp.setPlan_hash_value(planHashValueArray[i]);
				temp.setModule(module);
				temp.setParsing_schema_name(getParsingSchemaName(wrkjobCdArray[i], perfCheckStepIdArray[i]));
				temp.setExecutions(executionsArray[i]);
				temp.setAvg_buffer_gets(avgBufferGetsArray[i]);
				temp.setMax_buffer_gets(max_buffer_gets);
				temp.setTotal_buffer_gets(total_buffer_gets);
				temp.setAvg_elapsed_time(avgElapsedTimeArray[i]);
				temp.setMax_elapsed_time(max_elapsed_time);
				temp.setAvg_cpu_time(avg_cpu_time);
				temp.setAvg_disk_reads(avg_disk_reads);
				temp.setAvg_row_processed(avgRowProcessedArray[i]);
				temp.setRatio_buffer_get_total(ratio_buffer_get_total);
				temp.setRatio_cpu_total(ratio_cpu_total);
				temp.setRatio_cpu_per_executions(ratio_cpu_per_executions);
				temp.setSql_text(getSqlText(dbidArray[i], sqlIdArray[i]));
				temp.setPerf_check_id(perfCheckIdArray[i]);
				temp.setProgram_id(programIdArray[i]);
				temp.setProject_id(program_id);
				temp.setTuning_prgrs_step_seq(tuning_prgrs_step_seq);
				temp.setPerf_check_step_id(perfCheckStepIdArray[i]);
				temp.setDbio(dbioArray[i]);
				logger.debug("dbio#########  "+ dbioArray[i]);
				
				// 1-1. 중복 체크
				resultCount = sqlsDao.perfChkRequestTuningDupChk(temp);
				
				if(resultCount > 0) {
					list = result.getList();
					
					if(list == null) {
						list = new ArrayList<Object>();
					}
					
					list.add(dbioArray[i]);
					
					result.setList(list);
					
					continue;
				}
				
				// 2. TUNING_TARGET_SQL INSERT
				sqlsDao.insertTuningTargetSql(temp);
				
				// 3. SQL_TUNING_STATUS_HISTORY INSERT
				temp.setTuning_status_cd(tuning_status_cd);
				temp.setTuning_status_changer_id(temp.getTuning_requester_id());
				temp.setTuning_status_change_why(TUNING_STATUS_CHANGE_WHY);
				
				improvementManagementDao.insertTuningStatusHistory(temp);
				
				// 4. 기존에 SQL BIND 값 DELETE
				improvementManagementDao.deleteTuningTargetSqlBind(next_tuning_no);
				
				bindSetSeq = improvementManagementDao.getBindSetSeq(next_tuning_no);
				
				temp.setBind_set_seq(bindSetSeq);
				
				// 5. 프로그램 수행 회사
				programExecuteTms = StringUtils.defaultString(improvementManagementDao.getProgramExecuteTms(temp));
				
				temp.setProgram_execute_tms(programExecuteTms);
				
				if(programExecuteTms.equals("") == false) {
					tempSqls = new Sqls();
					
					tempSqls.setTuning_no(next_tuning_no);
					tempSqls.setDbid(dbid);
					tempSqls.setAfter_prd_sql_id(sqlIdArray[i]);
					
					// 7. 새로운 SQL BIND 값 INSERT	
					sqlsDao.insertTuningTargetSqlBindFromVsqlBindCapture(tempSqls);
				}
			}
			
			// 튜닝담당자 할당 건수 조회
			tuningNoArray = StringUtil.Right(tuningNoArray,1);
			
			temp.setTuning_no_array(tuningNoArray);
			
			perfrIdList = sqlTuningTargetDao.perfrIdAssignCountList(temp);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> " + ex.getMessage());
			throw ex;
		}
		
		result.setResult(perfrIdList.size() > 0 ? true : false);
		result.setTxtValue(requestTuningTotalCount + "");
		result.setObject(perfrIdList);
		
		return result;
	}
	
	private String getParsingSchemaName(String wrkjobCd, String perfCheckStepId) {
		Sqls sqls = new Sqls();
		
		sqls.setWrkjob_cd(wrkjobCd);
		sqls.setPerf_check_step_id(perfCheckStepId);
		
		return sqlsDao.selectParsingSchemaName(sqls).getParsing_schema_name();
	}
	
	private String getSqlText(String dbid, String sqlId) {
		Sqls sqls = new Sqls();
		
		sqls.setDbid(dbid);
		sqls.setSql_id(sqlId);
		
		return sqlsDao.selectSqlText(sqls).getSql_fulltext();
	}
	
	@Override
	public List<Sqls> performanceCheckResult(Sqls sqls)
			throws Exception {
		return sqlsDao.performanceCheckResult(sqls);
	}
	
	@Override
	public List<Sqls> performanceCheckResultException(Sqls sqls)
			throws Exception {
		return sqlsDao.performanceCheckResultException(sqls);
	}

	@Override
	public List<SqlsDetail> bindValueListAll(SqlsDetail sqlsDetail) throws Exception {
		return sqlsDao.bindValueListAll(sqlsDetail);
	}

	@Override
	public List<SqlGrid> sqlTreePlanListAll(SqlGrid sqlGrid) throws Exception {
		return sqlsDao.sqlTreePlanListAll(sqlGrid);
	}

	@Override
	public List<OdsHistSqlText> sqlTextPlanListAll(OdsHistSqlText odsHistSqlText) throws Exception {
		return sqlsDao.sqlTextPlanListAll(odsHistSqlText);
	}

	@Override
	public List<SqlGrid> sqlGridPlanListAll(SqlGrid sqlGrid) throws Exception {
		return sqlsDao.sqlGridPlanListAll(sqlGrid);
	}

	@Override
	public List<OdsHistSqlstat> outLineListAll(OdsHistSqlstat odsHistSqlstat) throws Exception {
		return sqlsDao.outLineListAll(odsHistSqlstat);
	}

	@Override
	public List<Sqls> sqlPerformHistoryList(Sqls sqls) throws Exception {
		return sqlsDao.sqlPerformHistoryList(sqls);
	}

	@Override
	public List<Sqls> loadAutoSqls(Sqls sqls) throws Exception {
		return sqlsDao.loadAutoSqls(sqls);
	}
	

	@Override
	public List<LinkedHashMap<String, Object>> excelAutoDown(Sqls sqls) throws Exception {
		return sqlsDao.excelAutoDown(sqls);
	}

	@Override
	public List<Sqls> autoPerformanceCheck(Sqls sqls) throws Exception {
		return sqlsDao.autoPerformanceCheck(sqls);
	}

	@Override
	public List<Sqls> autoPerformanceCheckResult(Sqls sqls) throws Exception {
		return sqlsDao.autoPerformanceCheckResult(sqls);
	}
	
}
