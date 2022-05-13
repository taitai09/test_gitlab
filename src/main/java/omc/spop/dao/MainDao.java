package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

/***********************************************************
 * 2017.11.03	이원식	최초작성
 * 2018.05.15	이원식	권한별 Dashboard 기능 정의로 변경
 * 2020.07.23	이재우	menu db조회 변경
 **********************************************************/

public interface MainDao {	
	public List<TuningTargetSql> improvementProgressList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> improvementPerformanceChartList(TuningTargetSql tuningTargetSql);
	
	public List<SqlImprovementType> improvementsChartList(SqlImprovementType sqlImprovementType);
	
	public List<TuningTargetSql> tunerJobChartList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tuningRequestList(TuningTargetSql tuningTargetSql);
	
	public List<BasicCheckConfig> preventionChartList(BasicCheckConfig basicCheckConfig);
	
	public List<SqlDiagSummary> riskDiagnosisChartList(SqlDiagSummary sqlDiagSummary);
	
	public List<TuningTargetSql> tuningProgressChartList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> dbaTuningDelayList(TuningTargetSql tuningTargetSql);
	
	public List<ObjectChange> objectChangeChartList(ObjectChange objectChange);
	
	public List<DbEmergencyAction> urgentActionList(DbEmergencyAction dbEmergencyAction);
	
	public int updateUrgentAction(DbEmergencyAction dbEmergencyAction);
	
	public List<TuningTargetSql> tuningWaitJobList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tuningProgressList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tuningExpectedDelayList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tunerTuningDelayList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tuningCompleteList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> tuningPerformanceChartList(TuningTargetSql tuningTargetSql);
	
	public List<TuningTargetSql> myWorkList(TuningTargetSql tuningTargetSql);
	
	public List<PerfGuide> precedentList(PerfGuide perfGuide);
	
	public List<LinkedHashMap<String, Object>> loadScmBasedStdQtyChk(Map<String, String> param);
	
	public List<Menu> menuList(String auth_cd);
	
	public Board noticeBoardOne();
	
	public List<Licence> licenseInquiry(Licence licence);

	public List<Licence> getLicenseExceeded(Licence licence);

	public List<Licence> getNoLicense(Licence licence);

	public int updateCloseLicensePopupForWeek(Licence licence);

	public int tuningWaitJobCnt(TuningTargetSql tuningTargetSql);

	public int tuningProgressCnt(TuningTargetSql tuningTargetSql);

	public int tuningExpectedDelayCnt(TuningTargetSql tuningTargetSql);

	public int dbaTuningDelayCnt(TuningTargetSql tuningTargetSql);

	public int tunerTuningDelayCnt(TuningTargetSql tuningTargetSql);

	public int tuningCompleteCnt(TuningTargetSql tuningTargetSql);
}
