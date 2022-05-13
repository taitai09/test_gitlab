package omc.spop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.base.SessionManager;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.Cd;
import omc.spop.model.FullscanSql;
import omc.spop.model.NewSql;
import omc.spop.model.PerfGuide;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.Result;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.SqlTuning;
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.SqlTuningHistory;
import omc.spop.model.SqlTuningStatusHistory;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSqlUnionOffloadSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.model.Users;
import omc.spop.service.ImprovementManagementService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 * 2020.06.02	이재우	기능개선
 **********************************************************/

@Service("ImprovementManagementService")
public class ImprovementManagementServiceImpl implements ImprovementManagementService {
	
	private static final Logger logger = LoggerFactory.getLogger(ImprovementManagementServiceImpl.class);
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;
	
	@Override
	public int insertImprovement(MultipartHttpServletRequest req, TuningTargetSql tuningTargetSql) throws Exception {
		
		// 파일 저장위치장소 설정하는 Util
		FileMngUtil fileMng = new FileMngUtil();
		// 첨부파일 매치되는 model
		SqlTuningAttachFile sqlTuningAttachFile = new SqlTuningAttachFile();
		int rowCnt = 0;
		String[] delFileNms = new String[0];
		
		// 1.TUNING_TARGET_SQL Next tuning no 조회
		
		String next_tuning_no = improvementManagementDao.getNextTuningNo();
		String tuning_requester_id = StringUtil.nvl(req.getParameter("tuning_requester_id"));
		String tuning_requester_wrkjob_cd = StringUtil.nvl(req.getParameter("tuning_requester_wrkjob_cd"));
		String tuning_requester_tel_num = StringUtil.nvl(req.getParameter("tuning_requester_tel_num"));
		
		String[] bindSetSeqArr = req.getParameterValues("bind_set_seq");
		String[] bindSeqArr = req.getParameterValues("bind_seq");
		String[] bindVarNmArr = req.getParameterValues("bind_var_nm");
		String[] bindVarValueArr = req.getParameterValues("bind_var_value");
		String[] bindVarTypeArr = req.getParameterValues("bind_var_type");
		String[] mandatoryYnArr = req.getParameterValues("mandatory_yn");
		
		try{
			tuningTargetSql.setTuning_no(next_tuning_no);
			tuningTargetSql.setTuning_requester_id(tuning_requester_id);
			tuningTargetSql.setTuning_requester_wrkjob_cd(tuning_requester_wrkjob_cd);
			tuningTargetSql.setTuning_requester_tel_num(tuning_requester_tel_num);
			
			if ( tuningTargetSql.getDeleteFiles() != null && "".equals( tuningTargetSql.getDeleteFiles() ) == false ) {
				delFileNms = tuningTargetSql.getDeleteFiles().split(" , ");
			}
			
			String auth_nm = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getAuth_nm());
			logger.debug("auth_nm :"+auth_nm);
			
			tuningTargetSql.setTuning_status_change_why(auth_nm+"튜닝요청");
			tuningTargetSql.setTuning_status_cd("2");
			tuningTargetSql.setTuning_status_changer_id(tuningTargetSql.getTuning_requester_id());
			
			// 2-1. FILE INSERT
			List<MultipartFile> fileList = req.getFiles("uploadFile");
			String fileSeq = "";
			
			if (fileList.size() > 0 && fileList.get(0).getOriginalFilename().equals("") == false ) {
				for ( MultipartFile file : fileList ) {
					boolean delYn = false;
					// 삭제 파일 있을 시
					if ( delFileNms.length > 0) {
						for ( int fileIdx = 0; fileIdx < delFileNms.length; fileIdx++ ) {
							if ( file.getOriginalFilename().trim().equals( delFileNms[fileIdx].trim() ) ) {
								delYn = true;
								logger.debug("#### UPLOAD FAIL : "+file.getOriginalFilename()+" , delYn : "+delYn);
								break;
							}
						}
					}
					if ( !delYn ) {
						logger.debug("#### UPLOAD SUCCESS : "+file.getOriginalFilename());
						// 2. 파일 업로드 정보 조회
						try {
							sqlTuningAttachFile = fileMng.uploadTuningFile(file, tuningTargetSql);
							sqlTuningAttachFile.setTuning_no(tuningTargetSql.getTuning_no());
							// 2-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
							fileSeq = improvementManagementDao.getMaxTuningAttachFileSeq(tuningTargetSql);
							
							// 2-2. 파일 존재시 PERF_GUIDE_ATTACH_FILE INSERT
							logger.debug("sqlTuning 정보 : " + sqlTuningAttachFile);
							sqlTuningAttachFile.setFile_seq(fileSeq);
							rowCnt = improvementManagementDao.insertTuningAttachFile(sqlTuningAttachFile);
						} catch (Exception ex) {
							logger.error("error => " + ex.getMessage());
							throw ex;
						}
					}
				}
				
				logger.debug(rowCnt+" <=====rowCnt , fileSize=====> "+fileList.size());
				if (rowCnt != 0) {
					rowCnt = improvementManagementDao.insertTuningTargetSql(tuningTargetSql);
				} else {
					return rowCnt;
				}
			} else {
				// 2. TUNING_TARGET_SQL INSERT
				rowCnt = improvementManagementDao.insertTuningTargetSql(tuningTargetSql);
			}
			
			// 3. SQL_TUNING_STATUS_HISTORY INSERT
			improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
			
			// 4. 기존에 SQL BIND 값 DELETE
			improvementManagementDao.deleteTuningTargetSqlBind(next_tuning_no);
			
			if (bindSetSeqArr != null) {
				
				for (int i = 0; i < bindSetSeqArr.length ; i++) {
					TuningTargetSqlBind result = new TuningTargetSqlBind();
					
					result.setTuning_no(next_tuning_no);
					result.setBind_set_seq(bindSetSeqArr[i]);
					result.setBind_seq(bindSeqArr[i]);
					result.setBind_var_nm(bindVarNmArr[i]);
					result.setBind_var_value(bindVarValueArr[i]);
					result.setBind_var_type(bindVarTypeArr[i]);
					result.setMandatory_yn(mandatoryYnArr[i]);
					
					// 5. 새로운 SQL BIND 값 INSERT	
					improvementManagementDao.insertTuningTargetSqlBind(result);
				}
			}
			fileList = null;
			// 6. 요청 ALERT 보낼 DBA 정보 조회
			//return improvementManagementDao.getDBManagerInfo(tuningTargetSql);
		} catch (NullPointerException ex) {
			logger.error("튜닝요청에러 1 :::" + ex.getMessage());
		} catch (Exception ex2) {
			logger.error("튜닝요청에러 2 :::" + ex2.getMessage());
		}
		fileMng = null;
		delFileNms = null;
		sqlTuningAttachFile = null;
		
		next_tuning_no = null;
		tuning_requester_id = null;
		tuning_requester_wrkjob_cd = null;
		tuning_requester_tel_num = null;
		
		bindSetSeqArr = null;
		bindSeqArr = null;
		bindVarNmArr = null;
		bindVarValueArr = null;
		bindVarTypeArr = null;
		mandatoryYnArr = null;
		
		return rowCnt;
	}
	
	@Override
	public TuningTargetSql getImprovementInfo(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getImprovementInfo(tuningTargetSql);
	}

	@Override
	public int updateImprovement(HttpServletRequest req) throws Exception {
		TuningTargetSql tuningTargetSql = new TuningTargetSql(); 
		int rowCnt = 0;
		
		String tuning_no = StringUtil.nvl(req.getParameter("tuning_no"));

		// 1.TUNING_TARGET_SQL Next tuning no 조회
		String next_tuning_no = improvementManagementDao.getNextTuningNo();

		//이전 튜닝에 이후 튜닝번호 update
		tuningTargetSql.setTuning_no(tuning_no);
		tuningTargetSql.setAfter_tuning_no(next_tuning_no);
		int updateAfterTuningNoResult = improvementManagementDao.updateAfterTuningNo(tuningTargetSql);
		logger.debug("updateAfterTuningNoResult:"+updateAfterTuningNoResult);

		String dbid = StringUtil.nvl(req.getParameter("dbid"));
		String parsing_schema_name = StringUtil.nvl(req.getParameter("parsing_schema_name"));
		String tuning_requester_id = StringUtil.nvl(req.getParameter("tuning_requester_id"));
		String tuning_requester_wrkjob_cd = StringUtil.nvl(req.getParameter("tuning_requester_wrkjob_cd"));
		String tuning_requester_tel_num = StringUtil.nvl(req.getParameter("tuning_requester_tel_num"));
		String program_type_cd = StringUtil.nvl(req.getParameter("program_type_cd"));
		String batch_work_div_cd = StringUtil.nvl(req.getParameter("batch_work_div_cd"));
		String tuning_complete_due_dt = StringUtil.nvl(req.getParameter("tuning_complete_due_dt"));
		String str_current_elap_time = StringUtil.nvl(req.getParameter("current_elap_time"));
		BigDecimal current_elap_time = null;
		if(str_current_elap_time != null && !str_current_elap_time.equals("")){
			current_elap_time = new BigDecimal(str_current_elap_time);
		}
		
		String forecast_result_cnt = StringUtil.nvl(req.getParameter("forecast_result_cnt"));
		String goal_elap_time = StringUtil.nvl(req.getParameter("goal_elap_time"));
		String wrkjob_peculiar_point = StringUtil.nvl(req.getParameter("wrkjob_peculiar_point"));
		String request_why = StringUtil.nvl(req.getParameter("request_why"));
		String sql_desc = StringUtil.nvl(req.getParameter("sql_desc"));
		String dbio = StringUtil.nvl(req.getParameter("dbio"));
		String tr_cd = StringUtil.nvl(req.getParameter("tr_cd"));
		String sql_text = StringUtil.nvl(req.getParameter("sql_text"));
		String exec_cnt = StringUtil.nvl(req.getParameter("exec_cnt"));
		
		String[] bindSetSeqArr = req.getParameterValues("bind_set_seq");
		String[] bindSeqArr = req.getParameterValues("bind_seq");
		String[] bindVarNmArr = req.getParameterValues("bind_var_nm");
		String[] bindVarValueArr = req.getParameterValues("bind_var_value");
		String[] bindVarTypeArr = req.getParameterValues("bind_var_type");
		String[] mandatoryYnArr = req.getParameterValues("mandatory_yn");

		tuningTargetSql.setBefore_tuning_no(tuning_no);
		tuningTargetSql.setTuning_no(next_tuning_no);
		tuningTargetSql.setDbid(dbid);
		tuningTargetSql.setParsing_schema_name(parsing_schema_name);
		tuningTargetSql.setTuning_requester_id(tuning_requester_id);
		tuningTargetSql.setTuning_requester_wrkjob_cd(tuning_requester_wrkjob_cd);
		tuningTargetSql.setTuning_requester_tel_num(tuning_requester_tel_num);
		tuningTargetSql.setProgram_type_cd(program_type_cd);
		tuningTargetSql.setBatch_work_div_cd(batch_work_div_cd);
		tuningTargetSql.setTuning_complete_due_dt(tuning_complete_due_dt);
		tuningTargetSql.setCurrent_elap_time(current_elap_time);
		tuningTargetSql.setForecast_result_cnt(forecast_result_cnt);
		tuningTargetSql.setGoal_elap_time(goal_elap_time);
		tuningTargetSql.setWrkjob_peculiar_point(wrkjob_peculiar_point);
		tuningTargetSql.setRequest_why(request_why);
		tuningTargetSql.setSql_desc(sql_desc);
		tuningTargetSql.setDbio(dbio);
		tuningTargetSql.setTr_cd(tr_cd);
		tuningTargetSql.setSql_text(sql_text);
		tuningTargetSql.setExec_cnt(exec_cnt);
		
		tuningTargetSql.setTuning_status_cd("2");
		tuningTargetSql.setTuning_status_change_why("업무개발자 튜닝 재요청");
		tuningTargetSql.setTuning_status_changer_id(tuningTargetSql.getTuning_requester_id());

		// 1. TUNING_TARGET_SQL UPDATE
//		rowCnt = improvementManagementDao.updateTuningTargetSql(tuningTargetSql);
		rowCnt = improvementManagementDao.insertTuningTargetSql(tuningTargetSql);
				
		// SQL_TUNING_STATUS_HISTORY INSERT
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		// 4. 기존에 SQL BIND 값 DELETE
		improvementManagementDao.deleteTuningTargetSqlBind(next_tuning_no);
		
		logger.debug("bindSetSeqArr :"+bindSetSeqArr);
		if(bindSetSeqArr != null){
			logger.debug("bindSetSeqArr.length :"+bindSetSeqArr.length);
			for(int i = 0; i < bindSetSeqArr.length ; i++){
				TuningTargetSqlBind result = new TuningTargetSqlBind();
				
				result.setTuning_no(next_tuning_no);
				result.setBind_set_seq(bindSetSeqArr[i]);
				result.setBind_seq(bindSeqArr[i]);
				result.setBind_var_nm(bindVarNmArr[i]);
				result.setBind_var_value(bindVarValueArr[i]);
				result.setBind_var_type(bindVarTypeArr[i]);
				result.setMandatory_yn(mandatoryYnArr[i]);
				
				// 5. 새로운 SQL BIND 값 INSERT				
				improvementManagementDao.insertTuningTargetSqlBind(result);
			}
		}
		return rowCnt;
	}	
	
	@Override
	public TuningTargetSql getImprovementSummary(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getImprovementSummary(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> improvementStatusList(TuningTargetSql tuningTargetSql) throws Exception {
		logger.debug("#### 확인 ::: tuningTargetSql.getIs_complted() ::::" + tuningTargetSql.getIs_completed());
		logger.debug("#### 확인222 ::: tuningTargetSql.headerBtnIsClicked() ::::" + tuningTargetSql.getHeaderBtnIsClicked());
		
		return improvementManagementDao.improvementStatusList(tuningTargetSql);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> improvementStatusList4Excel(TuningTargetSql tuningTargetSql)
			throws Exception {
		return improvementManagementDao.improvementStatusList4Excel(tuningTargetSql);
	}	
	
	@Override
	public SqlTuning getSQLTuning(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getSQLTuning(tuningTargetSql);
	}	
	
	@Override
	public TuningTargetSql getSelection(TuningTargetSql tuningTargetSql) throws Exception {
		TuningTargetSql tuningSql = improvementManagementDao.getSelection(tuningTargetSql);
		
		if ( tuningSql.getSql_text() != null && "".equals( tuningSql.getSql_text().trim() ) == false ) {
			tuningSql.setSql_text(tuningSql.getSql_text().replaceAll("(< br>)|(<br >)|(< br\\/>)|(<br \\/>)","\n") );
		}
		
		return tuningSql;
	}
	
	@Override
	public TuningTargetSql getRequest(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getRequest(tuningTargetSql);
	}
	
	@Override
	public FullscanSql getFullScan(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getFullScan(tuningTargetSql);
	}
	
	@Override
	public PlanChangeSql getPlanChange(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getPlanChange(tuningTargetSql);
	}
	
	@Override
	public NewSql getNewSQL(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getNewSQL(tuningTargetSql);
	}
	
	@Override
	public TempUsageSql getTempOver(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getTempOver(tuningTargetSql);
	}
	
	@Override
	public TuningTargetSql getImprovements(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.getImprovements(tuningTargetSql);
	}
	
	@Override
	public List<Cd> completeReasonList() throws Exception {
		return improvementManagementDao.completeReasonList();
	}
	
	@Override
	public List<Cd> completeReasonDetailList() throws Exception {
		return improvementManagementDao.completeReasonDetailList();
	}
	
	@Override
	public List<SqlImprovementType> sqlImprovementTypeList(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.sqlImprovementTypeList(tuningTargetSql);
	}
	
	@Override
	public List<SqlTuningHistory> sqlImproveHistoryList(SqlTuningHistory sqlTuningHistory) throws Exception {
		return improvementManagementDao.sqlImproveHistoryList(sqlTuningHistory);
	}
	
	@Override
	public SqlTuningHistory getSQLImproveHistory(SqlTuningHistory sqlTuningHistory) throws Exception {
		return improvementManagementDao.getSQLImproveHistory(sqlTuningHistory);
	}

	@Override
	public List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.bindSetList(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql) throws Exception {
		return improvementManagementDao.sqlBindList(tuningTargetSql);
	}

	@Override
	public int saveTuning(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		String[] tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");
		
		for (int i = 0; i < tuningNoArry.length; i++) {
			temp.setTuning_no(tuningNoArry[i]);
			temp.setTuning_status_cd("5");
			temp.setTuning_status_change_why("튜닝담당자가 튜닝중으로 변경함");
			temp.setTuning_status_changer_id(user_id);
			
			// 1. TUNING_TARGET_SQL UPDATE
			rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
			
			// 2. SQL_TUNING_STATUS_HISTORY INSERT		
			improvementManagementDao.insertTuningStatusHistory(temp);
		}
		return rowCnt;
	}	
	/**
	 * 접수취소를 하면 요청상태로 변경한다.
	 * 2018-08-16
	 */
	@Override
	public int saveReceiptCancel(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		// 1. TUNING_TARGET_SQL UPDATE
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd("2"); // "접수" 상태로 변경
		temp.setTuning_status_change_why(tuningTargetSql.getTuning_receipt_cancel_why());
		temp.setTuning_status_changer_id(user_id);

		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 2. SQL_TUNING_STATUS_HISTORY INSERT
		improvementManagementDao.insertTuningStatusHistory(temp);

		return rowCnt;
	}	
	
	@Override
	public int saveTuningCancelAll(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		String[] tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");
		TuningTargetSql temp = new TuningTargetSql();
		
		for (int i = 0; i < tuningNoArry.length; i++) {
			int processCnt = 0;
			temp.setTuning_no(tuningNoArry[i]);
			processCnt = saveTuningCancel(temp);
			
			if(processCnt > 0){
				rowCnt++;
			}
		}
		
		return rowCnt;
	}
	@Override
	public int saveRequestCancelAll(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String[] tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");
		TuningTargetSql temp = new TuningTargetSql();
		int processCnt, check = 0;
		
		for (int i = 0; i < tuningNoArry.length; i++) {
			processCnt = 0; check = 0;
			temp.setTuning_no(tuningNoArry[i]);
			temp.setUser_id(user_id);
			
			check = improvementManagementDao.checkRequesterId(temp);
			if(check == 0){
				throw new Exception("자신의 요청건에 한해 '요청취소' 처리할 수 있습니다.");
			}
			processCnt = saveRequestCancel(temp);
			
			if(processCnt > 0){
				rowCnt++;
			}
		}
		
		return rowCnt;
	}
	
	public int saveRequestCancel(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String tuningStatusCd = "";
		
		// 2. TUNING_TARGET_SQL UPDATE
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd("A");
		temp.setTuning_status_change_why("튜닝요청자 요청취소");
		temp.setTuning_status_changer_id(user_id);
		
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		if(rowCnt > 0){
			// 3. SQL_TUNING_STATUS_HISTORY INSERT
			improvementManagementDao.insertTuningStatusHistory(temp);
		}
		
		return rowCnt;
	}
	@Override
	public int saveTuningCancel(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String tuningStatusCd = "";
		
		// 1. 튜닝 이전 이력 조회
		tuningStatusCd = improvementManagementDao.getTuningStatusCd(tuningTargetSql);
				
		// 2. TUNING_TARGET_SQL UPDATE
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd(tuningStatusCd);
		
		if(tuningStatusCd.equals("3")){
			temp.setTuning_status_change_why("튜닝담당자가 튜닝취소하여 접수상태로 변경");
		}else{
			temp.setTuning_status_change_why("튜닝담당자가 튜닝취소하여 적용반려상태로 변경");
		}
		temp.setTuning_status_changer_id(user_id);
		
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 3. SQL_TUNING_STATUS_HISTORY INSERT
		improvementManagementDao.insertTuningStatusHistory(temp);
		
		return rowCnt;
	}
	
	@Override
	public int saveCancel(TuningTargetSql tuningTargetSql) throws Exception {
		int returnValue = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();

		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_changer_id(user_id);
		
		if(auth_cd.equals("ROLE_DEV")){ // 업무담당자인 경우...
			// 7 적용반려
			temp.setTuning_status_cd("7");
			temp.setTuning_status_change_why(tuningTargetSql.getTuning_rcess_why());
			temp.setTuning_apply_rcess_why(tuningTargetSql.getTuning_rcess_why());
			returnValue = 7;
			
			// 1. SQL_TUNING UPDATE
			improvementManagementDao.updateSqlTuning(temp);

			// 2. TUNING_TARGET_SQL UPDATE
			improvementManagementDao.updateRejectApplication(temp);

			// 3. SQL_TUNING_STATUS_HISTORY INSERT
			improvementManagementDao.insertTuningStatusHistory(temp);
			
			if(tuningTargetSql.getRerequest() != null){

				// 신규로직 2019-03-28 banks,작업지시서_20190325.xlsx 참조
				String tuning_requester_wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
				String tuning_requester_tel_num = SessionManager.getLoginSession().getUsers().getHp_no();

				// 신규 튜닝 번호
				String next_tuning_no = improvementManagementDao.getNextTuningNo();
				temp.setTuning_no(next_tuning_no);
				temp.setAfter_tuning_no(next_tuning_no);
				temp.setBefore_tuning_no(tuningTargetSql.getTuning_no());
				temp.setTuning_requester_id(user_id);
				temp.setTuning_requester_wrkjob_cd(tuning_requester_wrkjob_cd);
				temp.setTuning_requester_tel_num(tuning_requester_tel_num);

				// 튜닝완료요청일자
				String tuning_complete_due_day = StringUtils
						.defaultString(improvementManagementDao.getTuningCompleteDueDay(), "0");
				int i_tuning_complete_due_day = Integer.parseInt(tuning_complete_due_day);
				String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
				String tuning_complete_due_dt = DateUtil.getPlusDays("yyyy-MM-dd", "yyyyMMdd", nowDate,
						i_tuning_complete_due_day);
				
				temp.setTuning_complete_due_dt(tuning_complete_due_dt);
				
				temp.setChoice_div_cd("3");// 요청
				temp.setTuning_status_cd("2");// 요청
				temp.setBefore_tuning_status_cd("7");// 적용반려
				
				// 2. TUNING_TARGET_SQL UPDATE
				int updateResult = improvementManagementDao.updateRejectApplication2(temp);
				logger.debug("updateResult:" + updateResult);
				int insertResult = improvementManagementDao.insertTuningTargetSql2(temp);
				logger.debug("insertResult:" + insertResult);
				
				String auth_nm = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getAuth_nm());
				logger.debug("auth_nm :"+auth_nm);
				temp.setTuning_status_change_why(auth_nm+"튜닝요청");
				
				// 4. SQL_TUNING_STATUS_HISTORY INSERT
				improvementManagementDao.insertTuningStatusHistory(temp);
			}
		}else{ // 튜닝담당자인 경우..
			//4	튜닝반려
			temp.setTuning_status_cd("4");
			temp.setTuning_status_change_why(tuningTargetSql.getTuning_rcess_why());
			temp.setTuning_rcess_why(tuningTargetSql.getTuning_rcess_why());
			returnValue = 4;
			
			// 3. TUNING_TARGET_SQL UPDATE
			improvementManagementDao.updateTuningTargetSql(temp);
			
			// 4. SQL_TUNING_STATUS_HISTORY INSERT
			improvementManagementDao.insertTuningStatusHistory(temp);
		}
		return returnValue;
	}
	
	@Override
	public int saveEnd(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();

		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd("8");
		temp.setTuning_end_why_cd(tuningTargetSql.getTuning_end_why_cd());
		temp.setTuning_end_why(tuningTargetSql.getTuning_end_why());
		temp.setExcept_target_yn(tuningTargetSql.getExcept_target_yn());
		temp.setTuning_status_changer_id(user_id);
		
		if(auth_cd.equals("ROLE_DEV")){ // 업무담당자인 경우...			
			temp.setTuning_status_change_why("업무개발자 튜닝종료\n"+tuningTargetSql.getTuning_end_why());
			
			// 1. SQL_TUNING UPDATE
			improvementManagementDao.updateSqlTuning(temp);
		}else{ // 튜닝담당자인 경우..
			temp.setTuning_status_change_why("튜닝담당자 튜닝종료\n"+tuningTargetSql.getTuning_end_why());
			
			// 1. SQL_TUNING MERGE
			improvementManagementDao.mergeSqlTuningEnd(temp);
		}

		// 2. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 3. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(temp);				

		return rowCnt;
	}
	
	@Override
	public int savePreCheck(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String bfacChkNo = "";
		
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd("8");
		temp.setTuning_end_why_cd(tuningTargetSql.getTuning_end_why_cd());
		temp.setTuning_end_why(tuningTargetSql.getTuning_end_why());
		temp.setExcept_target_yn(tuningTargetSql.getExcept_target_yn());
		temp.setTuning_status_changer_id(user_id);
		
		temp.setTuning_status_change_why("소스점검요청\n"+tuningTargetSql.getTuning_end_why());
		
		// 1. SQL_TUNING UPDATE
		improvementManagementDao.updateSqlTuning(temp);

		// 2. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 3. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(temp);		
		
		// 4. 사전점검내용 저장
		BeforeAccidentCheck beforeAccidentCheck = new BeforeAccidentCheck();
		
		// 4-1. MAX BFAC_CHK_NO 조회
		bfacChkNo = improvementManagementDao.getBfacChkNo();
		
		// 4-2. BEFORE_ACCIDENT_CHECK INSERT
		beforeAccidentCheck.setBfac_chk_no(bfacChkNo);
		beforeAccidentCheck.setTuning_no(tuningTargetSql.getTuning_no());
		beforeAccidentCheck.setBfac_chk_rqtr_id(user_id);
		beforeAccidentCheck.setBfac_chk_source(tuningTargetSql.getBfac_chk_source());
		
		improvementManagementDao.insertBeforeAccidentCheck(beforeAccidentCheck);

		return rowCnt;
	}	

//	public TuningTargetSql replaceCkeditorText(TuningTargetSql tuningTargetSql) {
//
//		String impr_sql_text = tuningTargetSql.getImpr_sql_text();
//		logger.debug("before impr_sql_text=["+impr_sql_text+"]");
//
//		impr_sql_text = impr_sql_text.replace("&nbsp;", " ");
////		impr_sql_text = impr_sql_text.replace("<br />", "\r\n");
//		impr_sql_text = impr_sql_text.replace("<br />", "");
//		impr_sql_text = impr_sql_text.replace("&#39;", "'");
//		
//		tuningTargetSql.setImpr_sql_text(impr_sql_text);
//		logger.debug("after impr_sql_text=["+impr_sql_text+"]");
//
//		String imprb_exec_plan = tuningTargetSql.getImprb_exec_plan();
//		logger.debug("before imprb_exec_plan=["+imprb_exec_plan+"]");
//
//		imprb_exec_plan = imprb_exec_plan.replace("&nbsp;", " ");
////		imprb_exec_plan = imprb_exec_plan.replace("<br />", "\r\n");
//		imprb_exec_plan = imprb_exec_plan.replace("<br />", "");
//		imprb_exec_plan = imprb_exec_plan.replace("&#39;", "'");
//		
//		tuningTargetSql.setImprb_exec_plan(imprb_exec_plan);
//		logger.debug("after imprb_exec_plan=["+imprb_exec_plan+"]");
//
//		String impra_exec_plan = tuningTargetSql.getImpra_exec_plan();
//		logger.debug("before impra_exec_plan=["+impra_exec_plan+"]");
//
//		impra_exec_plan = impra_exec_plan.replace("&nbsp;", " ");
////		impra_exec_plan = impra_exec_plan.replace("<br />", "\r\n");
//		impra_exec_plan = impra_exec_plan.replace("<br />", "");
//		impra_exec_plan = impra_exec_plan.replace("&#39;", "'");
//		
//		tuningTargetSql.setImpra_exec_plan(impra_exec_plan);
//		logger.debug("after impra_exec_plan=["+impra_exec_plan+"]");
//
//		return tuningTargetSql;
//	}
	
	@Override
	public int saveComplete(TuningTargetSql tuningTargetSql,HttpServletRequest req) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String guideNo = "";
		SqlTuning sqlTuning = new SqlTuning();

		tuningTargetSql.setTuning_status_cd("6");
		tuningTargetSql.setTuning_status_changer_id(user_id);

		// 1. SQL_TUNING COMPLETE MERGE
		//performanceImprovementsDao.mergeSqlTuningComplete(tuningTargetSql);
		
		// 1. SQL_TUNING 데이터 존재여부
		sqlTuning = improvementManagementDao.getSqlTuningYn(tuningTargetSql);
		
		String tuning_no = "";
		if(sqlTuning != null){
			tuning_no = StringUtils.defaultString(sqlTuning.getTuning_no());
		}
//		tuningTargetSql = replaceCkeditorText(tuningTargetSql);

		if(!tuning_no.equals("")){
			improvementManagementDao.updateSqlTuningComplete(tuningTargetSql);
		}else{
			improvementManagementDao.insertSqlTuningComplete(tuningTargetSql);
		}
		
		// 2. SQL_TUNING_HISTORY INSERT
		improvementManagementDao.insertSqlTuningHistory(tuningTargetSql);
		
		tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());

		// 3. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(tuningTargetSql);
		
		// 4. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		// 5-1. SQL 개선 유형 내역(SQL_IMPROVEMENT_TYPE) DELETE
		improvementManagementDao.deleteSqlImprovementType(tuningTargetSql);
		
		// 5-2. SQL 개선 유형 내역(SQL_IMPROVEMENT_TYPE) INSERT		
		String[] completeReasonArry = StringUtil.split(tuningTargetSql.getCompleteArry(), "|");
		
		for (int i = 0; i < completeReasonArry.length; i++) {
			SqlImprovementType sqlImprovementType = new SqlImprovementType();
			
			String[] reasonDetailArry = StringUtil.split(completeReasonArry[i], "_");
			sqlImprovementType.setTuning_no(tuningTargetSql.getTuning_no());
			sqlImprovementType.setImpr_type_cd(reasonDetailArry[0]);
			sqlImprovementType.setImpr_detail_type_cd(reasonDetailArry[1]);

			improvementManagementDao.insertSqlImprovementType(sqlImprovementType);
		}
		
		// 6. 사례게시를 체크할 경우 PERF_GUIDE에 INSERT
		if(tuningTargetSql.getTuning_case_posting_yn().equals("Y")){
			// 6.1 사례 가이드 존재 여부 체크
			PerfGuide guide = new PerfGuide();
			
			guide = improvementManagementDao.getPerfGuide(tuningTargetSql);
			
			// 사례 가이드가 존재하지 않을 경우 INSERT
			if(guide == null){
				// 6-2. MAX GUIDE_NO 조회
				guide = new PerfGuide();
				guideNo = improvementManagementDao.getMaxPerfGuideNo();
				
				// 6-3. PERF_GUIDE INSERT				
				guide.setGuide_no(guideNo);
				guide.setGuide_div_cd("2");
				guide.setSys_nm(tuningTargetSql.getDb_name());
				guide.setGuide_title_nm(tuningTargetSql.getTuning_case_posting_title());
				guide.setReg_user_id(user_id);
				guide.setTuning_no(tuningTargetSql.getTuning_no());
				
				improvementManagementDao.insertPerfGuide(guide);
			}
		}
		
		return rowCnt;
	}	

	public int tempSaveComplete(TuningTargetSql tuningTargetSql,HttpServletRequest req) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		tuningTargetSql.setTuning_status_changer_id(user_id);

		// 1. SQL_TUNING 데이터 존재여부
		SqlTuning sqlTuning = improvementManagementDao.getSqlTuningYn(tuningTargetSql);
		
		String tuning_no = "";
		if(sqlTuning != null){
			tuning_no = StringUtils.defaultString(sqlTuning.getTuning_no());
		}
//		tuningTargetSql = replaceCkeditorText(tuningTargetSql);

		if(!tuning_no.equals("")){
			improvementManagementDao.updateSqlTuningComplete(tuningTargetSql);
		}else{
			improvementManagementDao.insertSqlTuningComplete(tuningTargetSql);
		}
		
		// 2. SQL_TUNING_HISTORY INSERT
		improvementManagementDao.insertSqlTuningHistory(tuningTargetSql);
		
		//tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());
		tuningTargetSql.setTuning_complete_why("임시 저장");
		tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());

		// 3. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(tuningTargetSql);
		
		// 4. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		return rowCnt;
	}
	/**
	 * 튜닝완료
	 */
	public int completeSqlTuning(TuningTargetSql tuningTargetSql, HttpServletRequest req) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String guideNo = "";

		String temporary_save_yn =StringUtils.defaultString(tuningTargetSql.getTemporary_save_yn());
		logger.debug("temporary_save_yn:"+temporary_save_yn);

		// 0-0. 임시저장일이 있는지 조회, SQL_TUNING 테이블에서 조회하는 것이므로 데이터 삭제하기 전에 조회.
		String temporary_save_dt = StringUtils.defaultString(improvementManagementDao.getTemporarySaveDt(tuningTargetSql));
		logger.debug("temporary_save_dt:"+temporary_save_dt);
		
		// 0-1. Sysdate구하기
		String sysdate = improvementManagementDao.getSysdate();
		logger.debug("sysdate:"+sysdate);

		if(temporary_save_yn.equals("Y")){
			tuningTargetSql.setTuning_status_cd("5");
			tuningTargetSql.setTuning_complete_dt("");
			tuningTargetSql.setTuning_completer_id("");
			tuningTargetSql.setTemporary_save_dt(sysdate);
		}else{
			tuningTargetSql.setTuning_status_cd("6");
			tuningTargetSql.setTuning_complete_dt(sysdate);
			tuningTargetSql.setTuning_completer_id(user_id);
		}
		//임시저장이든, 아니든  SQL_TUNING_INDEX_HISTORY 테이블의 update_dt는 sysdate
		//튜닝종료일경우만 SQL_TUNING_HISTORY 테이블의 update_dt는 sysdate
		tuningTargetSql.setUpdate_dt(sysdate);
		tuningTargetSql.setTuning_status_changer_id(user_id);
		
		// 1. SQL_TUNING 삭제
		int deleteSqlTuning = improvementManagementDao.deleteSqlTuning(tuningTargetSql);
		logger.debug("deleteSqlTuning:"+deleteSqlTuning);
		
		// 1-1. SQL_TUNING INSERT
		//임시 저장일을 세팅한다.
//		tuningTargetSql = replaceCkeditorText(tuningTargetSql);

		int insertSqlTuning = improvementManagementDao.insertSqlTuningComplete(tuningTargetSql);
		logger.debug("insertSqlTuning:"+insertSqlTuning);

		// 2. SQL_TUNING_HISTORY INSERT
		//int deleteSqlTuningHistory = improvementManagementDao.deleteSqlTuningHistory(tuningTargetSql);
		//logger.debug("deleteSqlTuningHistory:"+deleteSqlTuningHistory);
		if(!temporary_save_yn.equals("Y")){
			improvementManagementDao.insertSqlTuningHistory(tuningTargetSql);
			tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());
		}else{
			tuningTargetSql.setTuning_status_change_why("임시 저장");
		}

		// 3. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(tuningTargetSql);
		
		// 4. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		// 5-1. SQL 개선 유형 내역(SQL_IMPROVEMENT_TYPE) DELETE
		improvementManagementDao.deleteSqlImprovementType(tuningTargetSql);
		
		// 5-2. SQL 개선 유형 내역(SQL_IMPROVEMENT_TYPE) INSERT		
		String[] completeReasonArry = StringUtil.split(tuningTargetSql.getCompleteArry(), "|");
		
		for (int i = 0; i < completeReasonArry.length; i++) {
			SqlImprovementType sqlImprovementType = new SqlImprovementType();
			
			String[] reasonDetailArry = StringUtil.split(completeReasonArry[i], "_");
			logger.debug("reasonDetailArry.length:"+reasonDetailArry.length);
			if(reasonDetailArry.length > 0){
				sqlImprovementType.setTuning_no(tuningTargetSql.getTuning_no());
				if(!StringUtils.defaultString(reasonDetailArry[0]).equals("")){
					sqlImprovementType.setImpr_type_cd(reasonDetailArry[0]);
					sqlImprovementType.setImpr_detail_type_cd(reasonDetailArry[1]);
					
					improvementManagementDao.insertSqlImprovementType(sqlImprovementType);
				}
			}
		}
		
		// 6. 사례게시를 체크할 경우 PERF_GUIDE에 INSERT
		String tuning_case_posting_yn = tuningTargetSql.getTuning_case_posting_yn();
		logger.debug("tuning_case_posting_yn :"+tuning_case_posting_yn);
		if(tuning_case_posting_yn.equals("Y")){
			// 6.1 사례 가이드 존재 여부 체크
			PerfGuide guide = new PerfGuide();
			
			guide = improvementManagementDao.getPerfGuide(tuningTargetSql);
			
			// 사례 가이드가 존재하지 않을 경우 INSERT
			if(guide == null){
				// 6-2. MAX GUIDE_NO 조회
				guide = new PerfGuide();
				guideNo = improvementManagementDao.getMaxPerfGuideNo();
				
				// 6-3. PERF_GUIDE INSERT				
				guide.setGuide_no(guideNo);
				guide.setGuide_div_cd("2");
				guide.setSys_nm(tuningTargetSql.getDb_name());
				guide.setGuide_title_nm(tuningTargetSql.getTuning_case_posting_title());
				guide.setReg_user_id(user_id);
				guide.setTuning_no(tuningTargetSql.getTuning_no());
				guide.setTuning_case_type_cd(StringUtils.defaultString(tuningTargetSql.getTuning_case_type_cd()));
				
				improvementManagementDao.insertPerfGuide(guide);				
			}
		}
		
		String tuning_no = tuningTargetSql.getTuning_no();
		String[] index_impr_type_cd_arr = req.getParameterValues("index_impr_type_cd");
		String[] table_name_arr = req.getParameterValues("table_name");
		String[] index_name_arr = req.getParameterValues("index_name");
		String[] index_column_name_arr = req.getParameterValues("index_column_name");
		String[] before_index_column_name_arr = req.getParameterValues("before_index_column_name");
		//7. 인덱스 변경 요청
		//7-1. 임시저장일이 있으면 인덱스 삭제
		if(!temporary_save_dt.equals("")){
			tuningTargetSql.setUpdate_dt(temporary_save_dt);
			int indexDeleteResult = improvementManagementDao.deleteSqlTuningIndexHistory(tuningTargetSql);
			logger.debug("indexDeleteResult:"+indexDeleteResult);
		}
		if(index_impr_type_cd_arr != null){
			for(int i = 0; i < index_impr_type_cd_arr.length ; i++){
				TuningTargetSql result = new TuningTargetSql();
				logger.debug("index_impr_type_cd:"+index_impr_type_cd_arr[i]);
				logger.debug("table_name:"+table_name_arr[i]);
				logger.debug("index_name:"+index_name_arr[i]);
				logger.debug("index_column_name:"+index_column_name_arr[i]);
				logger.debug("before_index_column_name:"+before_index_column_name_arr[i]);
				
				result.setTuning_no(tuning_no);
				result.setUpdate_dt(sysdate);
				result.setIndex_impr_type_cd(StringUtils.defaultString(index_impr_type_cd_arr[i],""));
				result.setTable_name(StringUtils.defaultString(table_name_arr[i],""));
				result.setIndex_name(StringUtils.defaultString(index_name_arr[i],""));
				result.setIndex_column_name(StringUtils.defaultString(index_column_name_arr[i],""));
				result.setBefore_index_column_name(StringUtils.defaultString(before_index_column_name_arr[i],""));
				
				//7-2. 인덱스 입력
				improvementManagementDao.insertSqlTuningIndexHistory(result);
			}
		}
		
		return rowCnt;
	}
	
	/**
	 * 튜닝 결과 변경값 저장
	 * 수정해야할 곳..........
	 */
	@Override
	public int modifyTuningResult(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setTuning_status_changer_id(user_id);
		
		rowCnt = improvementManagementDao.updateSqlTuningResult(tuningTargetSql);
		
		// SQL_TUNING_HISTORY INSERT
		//improvementManagementDao.insertSqlTuningHistory(tuningTargetSql);
		
		//tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());
		tuningTargetSql.setTuning_complete_why("튜닝 결과 변경값 저장");
		tuningTargetSql.setTuning_status_change_why(tuningTargetSql.getTuning_complete_why());
		
		// SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(tuningTargetSql);
		
		return rowCnt;
	}

	@Override
	public Result saveTunerAssignAll(TuningTargetSql tuningTargetSql) throws Exception {		
		Result result = new Result();
		String[] tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");
		TuningTargetSql temp = new TuningTargetSql();

		String tuningNo = "";
		List<TuningTargetSql> tuningRequesterList = new ArrayList<TuningTargetSql>();
		
		try{
			for (int i = 0; i < tuningNoArry.length; i++) {
				temp.setTuning_no(tuningNoArry[i]);
				temp.setPerfr_id(tuningTargetSql.getPerfr_id());
				
				saveTunerAssign(temp);
				
				tuningNo += tuningNoArry[i] + ",";
			}
			
			// 튜닝담당자 할당 건수 조회
			tuningNo = StringUtil.Right(tuningNo,1);
			tuningTargetSql.setTuning_no_array(tuningNo);
			tuningRequesterList = improvementManagementDao.tuningRequesterCountList(tuningTargetSql);
			
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		result.setResult(tuningRequesterList.size() > 0 ? true : false);
		result.setTxtValue(String.valueOf(tuningRequesterList.size())); // 튜닝담당자 할당 건수
		result.setObject(tuningRequesterList);
		
		return result;
	}
	
	@Override
	public int saveTunerAssign(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		TuningTargetSql temp = new TuningTargetSql();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setPerfr_id(tuningTargetSql.getPerfr_id());
		temp.setTuning_status_cd("3");
		temp.setTuning_status_change_why("성능관리자가 튜닝담당자를 지정함");
		temp.setTuning_status_changer_id(user_id);
		
		// 1. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 2. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(temp);
		return rowCnt;
	}
	
	@Override
	public int changeWorkUser(TuningTargetSql tuningTargetSql) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		TuningTargetSql temp = new TuningTargetSql();
		
		temp.setTuning_no(tuningTargetSql.getTuning_no());
		temp.setTuning_status_cd(tuningTargetSql.getTuning_status_cd());
		temp.setWrkjob_mgr_id(tuningTargetSql.getWrkjob_mgr_id());
		temp.setWrkjob_mgr_wrkjob_cd(tuningTargetSql.getWrkjob_mgr_wrkjob_cd());
		temp.setWrkjob_mgr_tel_num(tuningTargetSql.getWrkjob_mgr_tel_num());

		temp.setTuning_status_changer_id(user_id);
		temp.setTuning_status_change_why("["+user_nm+"]님이 업무 담당자를 변경함");

		// 1. TUNING_TARGET_SQL UPDATE
		rowCnt = improvementManagementDao.updateTuningTargetSql(temp);
		
		// 2. SQL_TUNING_STATUS_HISTORY INSERT		
		improvementManagementDao.insertTuningStatusHistory(temp);

		return rowCnt;
	}	
	
	@Override
	public List<SqlTuningStatusHistory> processHistoryList(SqlTuningStatusHistory sqlTuningStatusHistory) throws Exception {
		return improvementManagementDao.processHistoryList(sqlTuningStatusHistory);
	}

//	@Override
//	public String getUsersWrkjobCdDbid(String wrkjob_cd) {
//		return improvementManagementDao.getUsersWrkjobCdDbid(wrkjob_cd);
//	}

	@Override
	public int saveInitSetting(TuningTargetSql tuningTargetSql) {
		return improvementManagementDao.saveInitSetting(tuningTargetSql);
	}

	@Override
	public TuningTargetSql getInitValues(String user_id) {
		return improvementManagementDao.getInitValues(user_id);
	}

	@Override
	public String getTuningCompleteDueDay() {
		return improvementManagementDao.getTuningCompleteDueDay();
	}

	@Override
	public SqlTuning getImprBeforeAfter(TuningTargetSql tuningTargetSql) {
		return improvementManagementDao.getImprBeforeAfter(tuningTargetSql);
	}

	@Override
	public TopSqlUnionOffloadSql getTopSqlUnionOffloadSql(TuningTargetSql tuningTargetSql) {
		return improvementManagementDao.getTopSqlUnionOffloadSql(tuningTargetSql);
	}

	@Override
	public List<SqlTuningAttachFile> readTuningAttachFiles(TuningTargetSql tuningTargetSql) {
		return improvementManagementDao.readTuningAttachFiles(tuningTargetSql);
	}

	@Override
	public int deleteTuningAttachFile(SqlTuningAttachFile sqlTuningAttachFile) {
		return improvementManagementDao.deleteTuningAttachFile(sqlTuningAttachFile);
	}

	@Override
	public TuningTargetSql getDeployAfterPerf(TuningTargetSql tuningTargetSql) {
		return improvementManagementDao.getDeployAfterPerf(tuningTargetSql);
	}

	@Override
	public int getPerfSourceType(TuningTargetSql tuningTargetSql) throws Exception {
		int chkType = 0;
		
		if ( !tuningTargetSql.getTuning_no().equals("") && tuningTargetSql.getTuning_no() != null ) {
			chkType = improvementManagementDao.getPerfSourceType( tuningTargetSql );
		}
		
		return chkType;
	}

	@Override
	public Users getUsersInfo(String users) throws Exception {
		return improvementManagementDao.getUsersInfo( users );
	}
	
}
