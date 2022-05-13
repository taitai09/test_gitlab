package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.SQLTuningTargetDao;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlTuning;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.model.TopsqlHandopChoice;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.SQLTuningTargetService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLTuningTargetService")
public class SQLTuningTargetServiceImpl implements SQLTuningTargetService {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLTuningTargetServiceImpl.class);
	/**
	 * 즉시 실행 상수 값
	 * 분단위
	 */
	private static final String INST_EXEC_MIN = "2";
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SQLTuningTargetDao sqlTuningTargetDao;
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;

	/** 자동선정 리스트*/
	@Override
	public List<TopsqlAutoChoice> autoSelectionList(TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		return sqlTuningTargetDao.autoSelectionList(topsqlAutoChoice);
	}
	/** 자동선정 리스트 엑셀 다운로드*/
	@Override
	public List<LinkedHashMap<String, Object>> autoSelectionList4Excel(TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		return sqlTuningTargetDao.autoSelectionList4Excel(topsqlAutoChoice);
	}
	@Override
	public List<LinkedHashMap<String, Object>> autoSelectionStatusList4Excel(TopsqlAutoChoice topsqlAutoChoice)
			throws Exception {
		return sqlTuningTargetDao.autoSelectionStatusList4Excel(topsqlAutoChoice);
	}
	@Override
	public List<LinkedHashMap<String, Object>> autoSelectionStatusSearchList4Excel(TopsqlAutoChoice topsqlAutoChoice)
			throws Exception {
		return sqlTuningTargetDao.autoSelectionStatusSearchList4Excel(topsqlAutoChoice);
	}
	
	@Override
	public void saveAutoSelection(TopsqlAutoChoice topsqlAutoChoice) throws Exception{
		String autoChoiceCondNo = "";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String execCycle = "";
		String execCycleGb = "";
		JobSchedulerConfigDetail jobSchedulerConfigDetail = new JobSchedulerConfigDetail();
		
		jobSchedulerConfigDetail.setJob_scheduler_type_cd("13"); // TOPSQL자동선정
		
		if(topsqlAutoChoice.getGather_cycle_div_cd().equals("1")){ // 일
			execCycleGb = "D";
		}else if(topsqlAutoChoice.getGather_cycle_div_cd().equals("2")){ // 주
			execCycleGb = "W";
		}else{ // 월
			execCycleGb = "M";
		}
		
		String nowDate = "";
		String nowTime = "";
		nowDate = DateUtil.getNowDate("yyyyMMdd");
		//nowTime = StringUtil.replace(DateUtil.getNextTime("P","5"),":",""); // 즉시 실행인 경우 5분후 실행으로 입력
		nowTime = StringUtil.replace(DateUtil.getNextTime("P",INST_EXEC_MIN),":",""); // 즉시 실행인 경우 즉시실행상수값을 입력
		//execCycle = StringUtil.getExecCycleFormat(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000", execCycleGb); // date, time, cycle
		
//		if( "Y".equals(topsqlAutoChoice.getDel_yn()) ) {
//			topsqlAutoChoice.setChoice_end_day(DateUtil.getNowDate("yyyy-MM-dd"));
//			
//			execCycle = StringUtil.getExecCycleFormat(
//					StringUtil.replace(topsqlAutoChoice.getChoice_start_day(), "-", ""),
//					"000000",
//					nowDate, nowTime,execCycleGb); // date, time, cycle
//			
//			jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(nowDate, nowTime));
//			
//		}else {
		execCycle = StringUtil.getExecCycleFormat(
				StringUtil.replace(topsqlAutoChoice.getChoice_start_day(), "-", ""),
				"000000",
				StringUtil.replace(topsqlAutoChoice.getChoice_end_day(), "-", ""),
				"235959",
				execCycleGb); // date, time, cycle
		
		jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
//		}
		logger.debug("execCycle ===> "+execCycle);
		
		jobSchedulerConfigDetail.setExec_cycle(execCycle);
		jobSchedulerConfigDetail.setUpd_id(user_id);
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(nowDate, nowTime));
		jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000"));
		//jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
		
		logger.debug("======================================================================================================");
		logger.debug(jobSchedulerConfigDetail.getExec_end_dt());
		
		if(topsqlAutoChoice.getAuto_choice_cond_no() == null || topsqlAutoChoice.getAuto_choice_cond_no().equals("")){
			// 선정조건번호가 없는 경우는 신규
			// 1. AUTH_CHOICE_COND_NO MAX 값 조회
			//autoChoiceCondNo = sqlTuningTargetDao.getMaxAutoChoiceCondNo(topsqlAutoChoice);
			//topsqlAutoChoice.setAuto_choice_cond_no(autoChoiceCondNo);
			
			// 2. TOPSQL_AUTO_CHOICE_COND INSERT
			topsqlAutoChoice.setChoicer_id(user_id);
			sqlTuningTargetDao.insertAutoChoiceCond(topsqlAutoChoice);
			
			// 3. TOPSQL_AUTO_CHOICE_COND_LOG SELECT/INSERT
			topsqlAutoChoice.setUpdate_id(user_id);
			sqlTuningTargetDao.insertAutoChoiceCondLog(topsqlAutoChoice);
			
			// 4. JOB_SCHEDULER_CONFIG_DETAIL INSERT
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(topsqlAutoChoice.getAuto_choice_cond_no());
			jobSchedulerConfigDetail.setUse_yn(topsqlAutoChoice.getUse_yn());
			commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		}else{
			// 선정조건번호가 있는 경우는 수정
			// 1. TOPSQL_AUTO_CHOICE_COND UPDATE
			topsqlAutoChoice.setChoicer_id(user_id);
			sqlTuningTargetDao.updateAutoChoiceCond(topsqlAutoChoice);
			
			// 2. TOPSQL_AUTO_CHOICE_COND SELECT
			TopsqlAutoChoice temp = new TopsqlAutoChoice();
			temp = sqlTuningTargetDao.selectAutoChoiceCond(topsqlAutoChoice);
			
			// 3. TOPSQL_AUTO_CHOICE_COND_LOG SELECT/INSERT
			temp.setUpdate_id(user_id);
			sqlTuningTargetDao.insertAutoChoiceCondLog(temp);
			
			// 4. JOB_SCHEDULER_CONFIG_DETAIL UPDATE
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(topsqlAutoChoice.getAuto_choice_cond_no());
			jobSchedulerConfigDetail.setUse_yn(topsqlAutoChoice.getUse_yn());
			commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		}
	}
	
	@Override
	public int bundleSaveAutoSelection(TopsqlAutoChoice topsqlAutoChoice) throws Exception{
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String execCycle = "";
		String execCycleGb = "";
		JobSchedulerConfigDetail jobSchedulerConfigDetail = new JobSchedulerConfigDetail();
		
		jobSchedulerConfigDetail.setJob_scheduler_type_cd("13"); // TOPSQL자동선정
		
		if(topsqlAutoChoice.getGather_cycle_div_cd() != null) {
			if(topsqlAutoChoice.getGather_cycle_div_cd().equals("1")){ // 일
				execCycleGb = "D";
			}else if(topsqlAutoChoice.getGather_cycle_div_cd().equals("2")){ // 주
				execCycleGb = "W";
			}else{ // 월
				execCycleGb = "M";
			}
		}
		
		String nowDate = "";
		String nowTime = "";
		nowDate = DateUtil.getNowDate("yyyyMMdd");
		//nowTime = StringUtil.replace(DateUtil.getNextTime("P","5"),":",""); // 즉시 실행인 경우 5분후 실행으로 입력
		nowTime = StringUtil.replace(DateUtil.getNextTime("P",INST_EXEC_MIN),":",""); // 즉시 실행인 경우 즉시실행상수값을 입력
		
		jobSchedulerConfigDetail.setUpd_id(user_id);
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(nowDate, nowTime));
		
		String[] autoChoiceCondNoArray =  StringUtil.split(topsqlAutoChoice.getAutoChoiceCondNoArray(), "|");
		TopsqlAutoChoice temp = new TopsqlAutoChoice();
		int rowCnt = 0;
		
		temp.setDbid(topsqlAutoChoice.getDbid());
		
		if(topsqlAutoChoice.getElap_time() != null) {
			temp.setElap_time(topsqlAutoChoice.getElap_time());
		}
		
		if(topsqlAutoChoice.getOrder_div_cd() != null) {
			temp.setOrder_div_cd(topsqlAutoChoice.getOrder_div_cd());
		}
		
		if(topsqlAutoChoice.getBefore_choice_sql_except_yn() != null && !topsqlAutoChoice.getBefore_choice_sql_except_yn().equals("")) {
			temp.setBefore_choice_sql_except_yn(topsqlAutoChoice.getBefore_choice_sql_except_yn());
		}
		
		if(topsqlAutoChoice.getBuffer_cnt() != null) {
			temp.setBuffer_cnt(topsqlAutoChoice.getBuffer_cnt());
		}
		
		if(topsqlAutoChoice.getCb_perfr_id_detail() != null && topsqlAutoChoice.getCb_perfr_id_detail().equalsIgnoreCase("Y")) {
			if(topsqlAutoChoice.getPerfr_auto_assign_yn() != null) {
				temp.setPerfr_auto_assign_yn(topsqlAutoChoice.getPerfr_auto_assign_yn());
			}
			
			if(topsqlAutoChoice.getPerfr_id() != null) {
				temp.setPerfr_id(topsqlAutoChoice.getPerfr_id());
			}
		}
		
		if(topsqlAutoChoice.getAppl_filter_yn() != null && !topsqlAutoChoice.getAppl_filter_yn().equals("")) {
			temp.setAppl_filter_yn(topsqlAutoChoice.getAppl_filter_yn());
		}
		
		if(topsqlAutoChoice.getExec_cnt() != null) {
			temp.setExec_cnt(topsqlAutoChoice.getExec_cnt());
		}
		
		if(topsqlAutoChoice.getProgram_type_cd() != null && !topsqlAutoChoice.getProgram_type_cd().equals("")) {
			temp.setProgram_type_cd(topsqlAutoChoice.getProgram_type_cd());
		}
		
		if(topsqlAutoChoice.getUse_yn() != null && !topsqlAutoChoice.getUse_yn().equals("")){
			temp.setUse_yn(topsqlAutoChoice.getUse_yn());
		}
		
		if(topsqlAutoChoice.getChoice_start_day() != null) {
			temp.setChoice_start_day(topsqlAutoChoice.getChoice_start_day());
		}
		
		if(topsqlAutoChoice.getGather_cycle_div_cd() != null) {
			temp.setGather_cycle_div_cd(topsqlAutoChoice.getGather_cycle_div_cd());
		}
		
		if(topsqlAutoChoice.getDel_yn() != null && !topsqlAutoChoice.getDel_yn().equals("")) {
			temp.setDel_yn(topsqlAutoChoice.getDel_yn());
		}
		
		if(topsqlAutoChoice.getChoice_end_day() != null) {
			temp.setChoice_end_day(topsqlAutoChoice.getChoice_end_day());
		}
		
		if(topsqlAutoChoice.getGather_range_div_cd() != null) {
			temp.setGather_range_div_cd(topsqlAutoChoice.getGather_range_div_cd());
		}
		
		if(topsqlAutoChoice.getProject_id() != null) {
			temp.setProject_id(topsqlAutoChoice.getProject_id());
		}
		
		if(topsqlAutoChoice.getTopn_cnt() != null) {
			temp.setTopn_cnt(topsqlAutoChoice.getTopn_cnt());
		}
		
		if(topsqlAutoChoice.getBefore_tuning_sql_except_yn() != null && !topsqlAutoChoice.getBefore_tuning_sql_except_yn().equals("")) {
			temp.setBefore_tuning_sql_except_yn(topsqlAutoChoice.getBefore_tuning_sql_except_yn());
		}
		
		if(topsqlAutoChoice.getTuning_prgrs_step_seq() != null) {
			temp.setTuning_prgrs_step_seq(topsqlAutoChoice.getTuning_prgrs_step_seq());
		}
		
		for(int autoChoiceCondNoArrayIndex = 0; autoChoiceCondNoArrayIndex < autoChoiceCondNoArray.length; autoChoiceCondNoArrayIndex++) {
			// 선정조건번호가 있는 경우는 수정
			// 1. TOPSQL_AUTO_CHOICE_COND UPDATE
			temp.setChoicer_id(user_id);
			temp.setAuto_choice_cond_no(autoChoiceCondNoArray[autoChoiceCondNoArrayIndex]);
			
			rowCnt = sqlTuningTargetDao.updateAutoChoiceCond(temp);
			
			// 2. TOPSQL_AUTO_CHOICE_COND SELECT
			TopsqlAutoChoice innerTemp = new TopsqlAutoChoice();
			innerTemp = sqlTuningTargetDao.selectAutoChoiceCond(temp);
			
			// 3. TOPSQL_AUTO_CHOICE_COND_LOG SELECT/INSERT
			innerTemp.setUpdate_id(user_id);
			sqlTuningTargetDao.insertAutoChoiceCondLog(innerTemp);
			
			// 4. JOB_SCHEDULER_CONFIG_DETAIL UPDATE
			// 4.1 execCycleGb
			String choice_start_day = "";
			String choice_end_day = "";
			
			if(!execCycleGb.equals("") || topsqlAutoChoice.getChoice_start_day() != null || topsqlAutoChoice.getChoice_end_day() != null) {
				if(!execCycleGb.equals("")) {
					if(topsqlAutoChoice.getChoice_start_day() == null && topsqlAutoChoice.getChoice_end_day() == null) {
						choice_start_day = innerTemp.getChoice_start_day();
						choice_end_day = innerTemp.getChoice_end_day();
					} else if(topsqlAutoChoice.getChoice_start_day() == null && topsqlAutoChoice.getChoice_end_day() != null) {
						choice_start_day = innerTemp.getChoice_start_day();
						choice_end_day = topsqlAutoChoice.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
					} else if(topsqlAutoChoice.getChoice_start_day() != null && topsqlAutoChoice.getChoice_end_day() == null) {
						choice_start_day = topsqlAutoChoice.getChoice_start_day();
						choice_end_day = innerTemp.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000"));
					} else {
						// all data is from web(topsqlAutoChoice)
						choice_start_day = topsqlAutoChoice.getChoice_start_day();
						choice_end_day = topsqlAutoChoice.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000"));
						jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
					}
				} else {
					if(innerTemp.getGather_cycle_div_cd().equals("1")){ // 일
						execCycleGb = "D";
					}else if(innerTemp.getGather_cycle_div_cd().equals("2")){ // 주
						execCycleGb = "W";
					}else{ // 월
						execCycleGb = "M";
					}
					
					if(topsqlAutoChoice.getChoice_start_day() != null && topsqlAutoChoice.getChoice_end_day() != null) {
						choice_start_day = topsqlAutoChoice.getChoice_start_day();
						choice_end_day = topsqlAutoChoice.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000"));
						jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
					} else if(topsqlAutoChoice.getChoice_start_day() == null && topsqlAutoChoice.getChoice_end_day() != null) {
						choice_start_day = innerTemp.getChoice_start_day();
						choice_end_day = topsqlAutoChoice.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_end_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_end_day(),"-",""), "235959"));
					} else if(topsqlAutoChoice.getChoice_start_day() != null && topsqlAutoChoice.getChoice_end_day() == null) {
						choice_start_day = topsqlAutoChoice.getChoice_start_day();
						choice_end_day = innerTemp.getChoice_end_day();
						
						jobSchedulerConfigDetail.setExec_start_dt(StringUtil.getDateFormatString(StringUtil.replace(topsqlAutoChoice.getChoice_start_day(),"-",""), "000000"));
					} else {
						// all data is from DB
						// not update
						logger.debug("All data is from DB");
					}
				}
				
				execCycle = StringUtil.getExecCycleFormat(
						StringUtil.replace(choice_start_day, "-", ""),
						"000000",
						StringUtil.replace(choice_end_day, "-", ""),
						"235959",
						execCycleGb); // date, time, cycle
				
				jobSchedulerConfigDetail.setExec_cycle(execCycle);
			}
			
			if(topsqlAutoChoice.getUse_yn() != null && !topsqlAutoChoice.getUse_yn().equals("")) {
				jobSchedulerConfigDetail.setUse_yn(topsqlAutoChoice.getUse_yn());
			}
			
			jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(temp.getAuto_choice_cond_no());
			commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail);
		}
		
		return rowCnt;
	}
	
	@Override
	public List<TopsqlAutoChoice> autoSelectionHistoryList(TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		return sqlTuningTargetDao.autoSelectionHistoryList(topsqlAutoChoice);
	}	
	
	@Override
	public List<TopsqlAutoChoice> getChoiceCondNo(TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		return sqlTuningTargetDao.getChoiceCondNo(topsqlAutoChoice);
	}
	
	@Override
	public List<TopsqlAutoChoice> autoSelectionStatusList(TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		return sqlTuningTargetDao.autoSelectionStatusList(topsqlAutoChoice);
	}
	
	@Override
	public List<TuningTargetSql> autoSelectionStatusDetailList(TuningTargetSql tuningTargetSql) throws Exception {
		return sqlTuningTargetDao.autoSelectionStatusDetailList(tuningTargetSql);
	}	
	@Override
	public List<LinkedHashMap<String, Object>> autoSelectionStatusDetailList4Excel(TuningTargetSql tuningTargetSql)
			throws Exception {
		return sqlTuningTargetDao.autoSelectionStatusDetailList4Excel(tuningTargetSql);
	}	
	/** 수동선정 리스트 */
	@Override
	public List<OdsHistSqlstat> manualSelectionList(OdsHistSqlstat odsHistSqlstat) throws Exception {
		
		String[] moduleS = StringUtils.defaultString(odsHistSqlstat.getExcpt_module_list()) != "" ? (odsHistSqlstat.getExcpt_module_list().trim()).split(",") : null;
		String[] parsing_schema_nameS =  StringUtils.defaultString(odsHistSqlstat.getExcpt_parsing_schema_name_list()) != "" ? (odsHistSqlstat.getExcpt_parsing_schema_name_list().trim()).split(",") : null;
		String[] sql_idS = StringUtils.defaultString(odsHistSqlstat.getExcpt_sql_id_list()) != "" ? (odsHistSqlstat.getExcpt_sql_id_list().trim()).split(",") : null;
		if(moduleS != null){
			odsHistSqlstat.setArray_module(Arrays.asList(moduleS));
		}
		if(parsing_schema_nameS != null){
			odsHistSqlstat.setArray_parsing_schema_name(Arrays.asList(parsing_schema_nameS));
		}

		if(sql_idS != null){
			odsHistSqlstat.setArray_sql_id(Arrays.asList(sql_idS));
		}
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = sqlTuningTargetDao.manualSelectionList(odsHistSqlstat);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		return resultList;
	}
	/** 수동선정 리스트 엑셀 다운로드 */
	@Override
	public List<LinkedHashMap<String, Object>> manualSelectionList4Excel(OdsHistSqlstat odsHistSqlstat) throws Exception {
		
		String[] moduleS = StringUtils.defaultString(odsHistSqlstat.getExcpt_module_list()) != "" ? (odsHistSqlstat.getExcpt_module_list().trim()).split(",") : null;
		String[] parsing_schema_nameS =  StringUtils.defaultString(odsHistSqlstat.getExcpt_parsing_schema_name_list()) != "" ? (odsHistSqlstat.getExcpt_parsing_schema_name_list().trim()).split(",") : null;
		String[] sql_idS = StringUtils.defaultString(odsHistSqlstat.getExcpt_sql_id_list()) != "" ? (odsHistSqlstat.getExcpt_sql_id_list().trim()).split(",") : null;
		
		if(moduleS != null){
			odsHistSqlstat.setArray_module(Arrays.asList(moduleS));
		}
		
		if(parsing_schema_nameS != null){
			odsHistSqlstat.setArray_parsing_schema_name(Arrays.asList(parsing_schema_nameS));
		}
		
		if(sql_idS != null){
			odsHistSqlstat.setArray_sql_id(Arrays.asList(sql_idS));
		}
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();
		
		try {
			resultList = sqlTuningTargetDao.manualSelectionList4Excel(odsHistSqlstat);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		return resultList;
	}

	@Override
	public List<TopsqlHandopChoice> manualSelectionStatusList(TopsqlHandopChoice topsqlHandopChoice) throws Exception {
		return sqlTuningTargetDao.manualSelectionStatusList(topsqlHandopChoice);
	}
	
	@Override
	public List<TuningTargetSql> manualSelectionStatusDetailList(TuningTargetSql tuningTargetSql) throws Exception {
		return sqlTuningTargetDao.manualSelectionStatusDetailList(tuningTargetSql);
	}	
	
	@Override
	public List<OdsHistSnapshot> manualSelectionHistoryList(OdsHistSnapshot odsHistSnapshot) throws Exception {
		return sqlTuningTargetDao.manualSelectionHistoryList(odsHistSnapshot);
	}	
	
	@Override
	public Result insertTuningRequest(TuningTargetSql tuningTargetSql) throws Exception {
		Result result = new Result();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		
		DatabaseTuner tuner = new DatabaseTuner();
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		int tunerCnt = 0;
		String auto_tuner = "";		
		String[] sqlIdArry = null;
		String[] tuningNoArry = null;
		String choiceTms = "";
		String tuningNo = "";
		
		String tuningNoArray = "";
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
	
		try{
			logger.debug("##### 수동선정 & 자동선정 으로 진행");
			// 1. 자동선정일 경우 Tuner 조회
			if(tuningTargetSql.getAuto_share().equals("Y")){
				tuner.setDbid(tuningTargetSql.getDbid());
				tunerList = commonDao.getTuner(tuner);
				
				tunerCnt = tunerList.size();
			}

			// 2. 수동선정과 자동선정현황 & 수동선정현황 분리
			if(tuningTargetSql.getStrGubun().equals("A")){ // 수동선정현황 & 자동선정현황
				tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");			

				for (int i = 0; i < tuningNoArry.length; i++) {
					TuningTargetSql temp = new TuningTargetSql();

					temp.setTuning_no(tuningNoArry[i]);
					
					tuningNoArray += tuningNoArry[i] + ",";
					
					if(tuningTargetSql.getAuto_share().equals("Y")){
						auto_tuner = tunerList.get(i%tunerCnt).getTuner_id();
					}else{
						auto_tuner = tuningTargetSql.getPerfr_id();
					}						
				
					temp.setTuning_status_change_why("튜닝담당자 일괄지정");
					temp.setTuning_requester_id(user_id);
					temp.setPerfr_id(auto_tuner);
					
					// 3. TUNING TARGET SQL UPDATE
					sqlTuningTargetDao.updateTuningTargetSql(temp);
					
					// 4. SQL_TUNING_STATUS_HISTORY INSERT
					temp.setTuning_status_cd("3");
					temp.setTuning_status_changer_id(temp.getTuning_requester_id());
//					sqlTuningTargetDao.insertTuningStatusHistory(temp);
					improvementManagementDao.insertTuningStatusHistory(temp);
				}
			}else{ // 수동선정
				logger.debug("##### 수동선정으로 진행 - project_id :" +tuningTargetSql.getProject_id() + ", tuning_prgrs_step_seq :"+ tuningTargetSql.getTuning_prgrs_step_seq() );
				sqlIdArry = StringUtil.split(tuningTargetSql.getSqlIdArry(), "|");			

				for (int i = 0; i < sqlIdArry.length; i++) {
					TuningTargetSql temp = new TuningTargetSql();
					
					temp.setSql_id(sqlIdArry[i]);
					
					if(tuningTargetSql.getAuto_share().equals("Y")){
						logger.debug("##### 수동선정 - 자동선정 진행 #####");
						auto_tuner = tunerList.get(i%tunerCnt).getTuner_id();
					}else{
						auto_tuner = tuningTargetSql.getPerfr_id();
					}
					
					// 3. TUNING_TARGET_SQL 의 INSERT 정보 조회
					temp.setDbid(tuningTargetSql.getDbid());
					temp.setStart_snap_id(tuningTargetSql.getStart_snap_id());
					temp.setEnd_snap_id(tuningTargetSql.getEnd_snap_id());
					
					temp = sqlTuningTargetDao.selectTuningTargetSql(temp);
					
					// 4-1. TOPSQL_HANDOP_CHOICE_EXEC MAX CHOICE_TMS & TUNING_TARGET_SQL MAX TUNING_NO 조회
					choiceTms = sqlTuningTargetDao.getMaxChoiceTms(tuningTargetSql);
					tuningNo = improvementManagementDao.getNextTuningNo();
					
					tuningNoArray += tuningNo + ",";
					
					temp.setTuning_no(tuningNo);
					temp.setChoice_tms(choiceTms);
					temp.setPerfr_id(auto_tuner);
					temp.setTuning_status_change_why("수동튜닝대상선정 및 접수");
					temp.setTuning_requester_id(user_id);
					temp.setTuning_requester_wrkjob_cd(wrkjob_cd);
					temp.setTuning_requester_tel_num(ext_no);
					
					temp.setProject_id(tuningTargetSql.getProject_id());
					temp.setTuning_prgrs_step_seq(tuningTargetSql.getTuning_prgrs_step_seq());
					
					// 4-2. TUNING_TARGET_SQL INSERT
					if(temp.getProject_id().equals(""))  
						sqlTuningTargetDao.insertTuningTargetSql(temp);					
					else  //프로젝트 아이디가 있는경우
						sqlTuningTargetDao.insertTuningTargetSqlByProject(temp);
					
					// 5. SQL_TUNING_STATUS_HISTORY INSERT
					temp.setTuning_status_cd("3");
					temp.setTuning_status_changer_id(temp.getTuning_requester_id());
//					sqlTuningTargetDao.insertTuningStatusHistory(temp);
					improvementManagementDao.insertTuningStatusHistory(temp);
				}
			}
			
			// 튜닝담당자 할당 건수 조회
			tuningNoArray = StringUtil.Right(tuningNoArray,1);
			tuningTargetSql.setTuning_no_array(tuningNoArray);
			perfrIdList = sqlTuningTargetDao.perfrIdAssignCountList(tuningTargetSql);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> " + ex.getMessage());
			throw ex;
		}
		
		result.setResult(perfrIdList.size() > 0 ? true : false);
		result.setTxtValue(choiceTms);
		result.setObject(perfrIdList);
		
		return result;
	}	
	
	@Override
	public String insertTopsqlHandopChoiceExec(TopsqlHandopChoice topsqlHandopChoice) throws Exception {
		String isOk = "N";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		try{
			logger.debug("##### 수동선정으로 진행 - project_id :" +topsqlHandopChoice.getProject_id() + ", tuning_prgrs_step_seq :"+ topsqlHandopChoice.getTuning_prgrs_step_seq() );

			// TOPSQL_HANDOP_CHOICE_EXEC INSERT
			topsqlHandopChoice.setChoicer_id(user_id);
			
			int check = sqlTuningTargetDao.insertTopsqlHandopChoiceExec(topsqlHandopChoice);
			
			isOk = check > 0 ? "Y": "N";
		} catch (Exception ex){
			logger.error("error ==> " + ex.getMessage());
			isOk = "N";
		}
		return isOk;
	}	
	
	@Override
	public String endBathTuning(SqlTuning sqlTuning) throws Exception {
		String isOk = "N";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String[] tuningNoArry = null;
		
		try{
			tuningNoArry = StringUtil.split(sqlTuning.getTuningNoArry(), "|");			

			for (int i = 0; i < tuningNoArry.length; i++) {
				SqlTuning temp = new SqlTuning();

				// 1. TUNING TARGET SQL UPDATE
				temp.setTuning_no(tuningNoArry[i]);
				sqlTuningTargetDao.updateTuningTargetSqlStatus(temp);
				
				// 2. SQL TUNING MERGE
				temp.setTuning_end_why_cd(sqlTuning.getTuning_end_why_cd());
				temp.setTuning_end_why(sqlTuning.getTuning_end_why());
				temp.setTuning_ender_id(user_id);
				
				sqlTuningTargetDao.mergeSqlTuning(temp);

				// 3. SQL TUNING STATUS HISTORY INSERT
				sqlTuningTargetDao.insertSqlTuningStatusHistory(temp);
			}
			
			isOk = "Y";
		} catch (Exception ex){
			logger.error("error ==> " + ex.getMessage());
			isOk = "N";
		}				

		return isOk;
	}
	@Override
	public List<LinkedHashMap<String, Object>> manualSelectionStatusListByExcelDown(
			TopsqlHandopChoice topsqlHandopChoice) {
		return sqlTuningTargetDao.manualSelectionStatusListByExcelDown(topsqlHandopChoice);
	}
	
	@Override
	public String endBatchTuningBundle(SqlTuning sqlTuning) throws Exception {
		String isOk = "N";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String[] choiceTmsArray = null;
		String[] autoChoiceCondNoArray = null;
		List<TuningTargetSql> tuningTargetSql = null;
		SqlTuning temp = null;
		String tuningNo = null;
		
		try{
			choiceTmsArray = StringUtil.split(sqlTuning.getChoiceTmsArray(), "|");
			autoChoiceCondNoArray = StringUtil.split(sqlTuning.getAutoChoiceCondNoArray(), "|");

			for (int i = 0; i < choiceTmsArray.length; i++) {
				temp = new SqlTuning();

				temp.setChoice_tms(choiceTmsArray[i]);
				temp.setAuto_choice_cond_no(autoChoiceCondNoArray[i]);
				
				tuningTargetSql = sqlTuningTargetDao.autoSelectionStatusTuningNoList(temp);
				
				for(int notTuningCompleteIndex = 0; notTuningCompleteIndex < tuningTargetSql.size(); notTuningCompleteIndex++) {
					
					tuningNo = tuningTargetSql.get(notTuningCompleteIndex).getTuning_no();
					
					temp.setTuning_no(tuningNo);
					// 1. TUNING TARGET SQL UPDATE
					sqlTuningTargetDao.updateTuningTargetSqlStatusBundle(temp);
					
					// 2. SQL TUNING MERGE
					temp.setTuning_end_why_cd(sqlTuning.getTuning_end_why_cd());
					temp.setTuning_end_why(sqlTuning.getTuning_end_why());
					temp.setTuning_ender_id(user_id);
					
					sqlTuningTargetDao.mergeSqlTuningBundle(temp);

					// 3. SQL TUNING STATUS HISTORY INSERT
					sqlTuningTargetDao.insertSqlTuningStatusBundleHistory(temp);
				}
			}
			
			isOk = "Y";
		} catch (Exception ex){
			logger.error("error ==> " + ex.getMessage());
			isOk = "N";
		}				

		return isOk;
	}
	
}
