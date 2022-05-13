package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.Config;
import omc.spop.base.SessionManager;
import omc.spop.dao.MainDao;
import omc.spop.model.BasicCheckConfig;
import omc.spop.model.Board;
import omc.spop.model.DbEmergencyAction;
import omc.spop.model.Licence;
import omc.spop.model.Menu;
import omc.spop.model.ObjectChange;
import omc.spop.model.PerfGuide;
import omc.spop.model.SqlDiagSummary;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.MainService;
import omc.spop.utils.DateUtil;

@Service("MainService")
public class MainServiceImpl implements MainService {
	
	@Autowired
	private MainDao mainDao;
	
	@Override
	public List<TuningTargetSql> improvementProgressList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		tuningTargetSql.setDba_review(dbaReviewYn);
		
		return mainDao.improvementProgressList(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> improvementPerformanceChartList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		tuningTargetSql.setDba_review(dbaReviewYn);
		
		return mainDao.improvementPerformanceChartList(tuningTargetSql);
	}
	
	@Override
	public List<SqlImprovementType> improvementsChartList(SqlImprovementType sqlImprovementType) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		sqlImprovementType.setUser_id(user_id);
		
		return mainDao.improvementsChartList(sqlImprovementType);
	}	
	
	@Override
	public List<TuningTargetSql> tunerJobChartList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tunerJobChartList(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> tuningRequestList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningRequestList(tuningTargetSql);
	}	
	/* DB 예방 점검 현황  */
	@Override
	public List<BasicCheckConfig> preventionChartList(BasicCheckConfig basicCheckConfig) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		basicCheckConfig.setUser_id(user_id);
		
		return mainDao.preventionChartList(basicCheckConfig);
	}
	
	@Override
	public List<SqlDiagSummary> riskDiagnosisChartList(SqlDiagSummary sqlDiagSummary) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		sqlDiagSummary.setUser_id(user_id);
		
		return mainDao.riskDiagnosisChartList(sqlDiagSummary);
	}
	
	@Override
	public List<TuningTargetSql> tuningProgressChartList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningProgressChartList(tuningTargetSql);
	}
	
	@Override
	public List<ObjectChange> objectChangeChartList(ObjectChange objectChange) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		
		objectChange.setUser_id(user_id);
		objectChange.setBase_day(base_day);
		
		return mainDao.objectChangeChartList(objectChange);
	}

	@Override
	public List<DbEmergencyAction> urgentActionList(DbEmergencyAction dbEmergencyAction) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		dbEmergencyAction.setUser_id(user_id);
		
		return mainDao.urgentActionList(dbEmergencyAction);
	}
	
	@Override
	public int updateUrgentAction(DbEmergencyAction dbEmergencyAction) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		dbEmergencyAction.setEmergency_actor_id(user_id);
		
		return mainDao.updateUrgentAction(dbEmergencyAction);
	}
	
	@Override
	public List<TuningTargetSql> tuningWaitJobList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningWaitJobList(tuningTargetSql);
	}
	
	@Override
	public int tuningWaitJobCnt(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningWaitJobCnt(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> tuningProgressList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningProgressList(tuningTargetSql);
	}	
	
	@Override
	public int tuningProgressCnt(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningProgressCnt(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> tuningExpectedDelayList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningExpectedDelayList(tuningTargetSql);
	}
	
	@Override
	public int tuningExpectedDelayCnt(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningExpectedDelayCnt(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> tuningDelayList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setPerfr_id(user_id);
		
		if(auth_cd.equals("ROLE_DBMANAGER")){
			return mainDao.dbaTuningDelayList(tuningTargetSql);
		}else{
			return mainDao.tunerTuningDelayList(tuningTargetSql);
		}
	}
	
	@Override
	public int tuningDelayCnt(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setPerfr_id(user_id);
		
		if(auth_cd.equals("ROLE_DBMANAGER")){
			return mainDao.dbaTuningDelayCnt(tuningTargetSql);
		}else{
			return mainDao.tunerTuningDelayCnt(tuningTargetSql);
		}
	}
	
	@Override
	public List<TuningTargetSql> tuningCompleteList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningCompleteList(tuningTargetSql);
	}
	
	@Override
	public int tuningCompleteCnt(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningCompleteCnt(tuningTargetSql);
	}
	
	@Override
	public List<TuningTargetSql> tuningPerformanceChartList(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setPerfr_id(user_id);
		
		return mainDao.tuningPerformanceChartList(tuningTargetSql);
	}	
	
	@Override
	public List<TuningTargetSql> myWorkList(TuningTargetSql tuningTargetSql) throws Exception {
		String dbaReviewYn = Config.getString("dba.review.yn"); // DBA 검토여부
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		tuningTargetSql.setDba_review(dbaReviewYn);
		tuningTargetSql.setWrkjob_mgr_id(user_id);
		
		return mainDao.myWorkList(tuningTargetSql);
	}
	
	@Override
	public List<PerfGuide> precedentList(PerfGuide perfGuide) throws Exception {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "2");
		
		perfGuide.setStrStartDt(startDate);
		perfGuide.setStrEndDt(nowDate);
		
		return mainDao.precedentList(perfGuide);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadScmBasedStdQtyChk(Map<String, String> param) throws Exception {
		return mainDao.loadScmBasedStdQtyChk(param);
	}
	
	@Override
	public List<Menu> menuList(String auth_cd) {
		return mainDao.menuList(auth_cd);
	}
	
	@Override
	public Board noticeBoardOne() {
		return mainDao.noticeBoardOne();
	}
	
	@Override
	public List<Licence> licenseInquiry(Licence licence) throws Exception {
		return mainDao.licenseInquiry(licence);
	}
	
	@Override
	public List<Licence> getLicenseExceeded(Licence licence) throws Exception {
		return mainDao.getLicenseExceeded(licence);
	}
	
	@Override
	public List<Licence> getNoLicense(Licence licence) throws Exception {
		return mainDao.getNoLicense(licence);
	}
	
	@Override
	public int updateCloseLicensePopupForWeek(Licence licence) throws Exception {
		return mainDao.updateCloseLicensePopupForWeek(licence);
	}
}