package omc.spop.service.impl;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ImprovementManagementDao;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.model.TuningTargetSqlPlugIn;
import omc.spop.service.ImprovementManagementPlugInService;

/***********************************************************
 * 2021.10.18	명성태	OPENPOP V2 최초작업
 **********************************************************/

@Service("ImprovementManagementPlugInService")
public class ImprovementManagementPlugInServiceImpl implements ImprovementManagementPlugInService {
	
	private static final Logger logger = LoggerFactory.getLogger(ImprovementManagementPlugInServiceImpl.class);
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;

	@Override
	public int insertImprovement(TuningTargetSqlPlugIn tuningTargetSqlPlugIn) throws Exception {
		TuningTargetSql tuningTargetSql = new TuningTargetSql();
		int rowCnt = 0;
		LinkedList<String> bind_set_seq_list = null;
		LinkedList<String> bind_seq_list= null;
		LinkedList<String> bind_var_nm_list = null;
		LinkedList<String> bind_var_value_list= null;
		LinkedList<String> bind_var_type_list = null;
		LinkedList<String> mandatory_yn_list = null;
		
		String[] bindSetSeqArr = null;
		String[] bindSeqArr = null;
		String[] bindVarNmArr = null;
		String[] bindVarValueArr = null;
		String[] bindVarTypeArr = null;
		String[] mandatoryYnArr = null;
		
		try{
			// 1.TUNING_TARGET_SQL Next tuning no 조회
			String next_tuning_no = improvementManagementDao.getNextTuningNo();
			
			bind_set_seq_list = tuningTargetSqlPlugIn.getBind_set_set_array();
			bind_seq_list = tuningTargetSqlPlugIn.getBind_seq_array();
			bind_var_nm_list = tuningTargetSqlPlugIn.getBind_var_nm_array();
			bind_var_value_list = tuningTargetSqlPlugIn.getBind_var_value_array();
			bind_var_type_list = tuningTargetSqlPlugIn.getBind_var_type_array();
			mandatory_yn_list = tuningTargetSqlPlugIn.getMandatory_yn_array();
			
			if(bind_set_seq_list != null) {
				bindSetSeqArr = bind_set_seq_list.toArray(new String[bind_set_seq_list.size()]);
				bindSeqArr = bind_seq_list.toArray(new String[bind_seq_list.size()]);
				bindVarNmArr = bind_var_nm_list.toArray(new String[bind_var_nm_list.size()]);
				bindVarValueArr = bind_var_value_list.toArray(new String[bind_var_value_list.size()]);
				bindVarTypeArr = bind_var_type_list.toArray(new String[bind_var_type_list.size()]);
				mandatoryYnArr = mandatory_yn_list.toArray(new String[mandatory_yn_list.size()]);
			}
			
			tuningTargetSql.setTuning_no(next_tuning_no);
			tuningTargetSql.setTuning_requester_id(tuningTargetSqlPlugIn.getTuning_requester_id());
			tuningTargetSql.setTuning_requester_wrkjob_cd(tuningTargetSqlPlugIn.getTuning_requester_wrkjob_cd());
			tuningTargetSql.setTuning_requester_tel_num(tuningTargetSqlPlugIn.getTuning_requester_tel_num());
			tuningTargetSql.setTuning_status_change_why("Plug-in 튜닝요청");
			tuningTargetSql.setTuning_status_cd("2");
			tuningTargetSql.setTuning_status_changer_id(tuningTargetSqlPlugIn.getTuning_requester_id());
			tuningTargetSql.setSql_desc(tuningTargetSqlPlugIn.getSql_desc());
			
			// Only PlugIn Module
			tuningTargetSql.setDbid(tuningTargetSqlPlugIn.getDbid());
			tuningTargetSql.setProgram_type_cd(tuningTargetSqlPlugIn.getProgram_type_cd());
			tuningTargetSql.setTuning_complete_due_dt(tuningTargetSqlPlugIn.getTuning_complete_due_dt());
			tuningTargetSql.setSql_text(tuningTargetSqlPlugIn.getSql_text());
			tuningTargetSql.setParsing_schema_name(tuningTargetSqlPlugIn.getParsing_schema_name());
			tuningTargetSql.setProject_id(tuningTargetSqlPlugIn.getProject_id());
			
			// 2-1. FILE INSERT
			
			// 2. TUNING_TARGET_SQL INSERT
			rowCnt = improvementManagementDao.insertTuningTargetSql(tuningTargetSql);
			
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
			// 6. 요청 ALERT 보낼 DBA 정보 조회
			//return improvementManagementDao.getDBManagerInfo(tuningTargetSql);
		
		} catch (NullPointerException ex) {
			logger.error("튜닝요청에러 1 :::" + ex.getMessage());
		} catch (Exception ex2) {
			logger.error("튜닝요청에러 2 :::" + ex2.getMessage());
		}
		
		return rowCnt;
	}
}
