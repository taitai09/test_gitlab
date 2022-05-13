package omc.spop.service;

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

public interface MainService {
	/** Dashboard - (관리자) 성능 개선 진행 현황 리스트 */
	List<TuningTargetSql> improvementProgressList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (관리자) 성능개선 3개월 누적 실적 현황 차트 리스트 */
	List<TuningTargetSql> improvementPerformanceChartList(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** Dashboard - (DBA) 성능 개선 유형 차트 리스트 */
	List<SqlImprovementType> improvementsChartList(SqlImprovementType sqlImprovementType) throws Exception;
	
	/** Dashboard - (DBA) 튜너별 작업부하 현황 차트 리스트 */
	List<TuningTargetSql> tunerJobChartList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (DBA) 튜닝 요청현황 리스트 */
	List<TuningTargetSql> tuningRequestList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (DBA) DB 예방 점검 현황 차트 리스트 */
	List<BasicCheckConfig> preventionChartList(BasicCheckConfig basicCheckConfig) throws Exception;
	
	/** Dashboard - (DBA) 성능 리스크 진단 현황 차트 리스트 */
	List<SqlDiagSummary> riskDiagnosisChartList(SqlDiagSummary sqlDiagSummary) throws Exception;
	
	/** Dashboard - (DBA) 튜닝 진행 현황 차트 리스트 */
	List<TuningTargetSql> tuningProgressChartList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (DBA) 오브젝트 변경 현황 차트 리스트 */
	List<ObjectChange> objectChangeChartList(ObjectChange objectChange) throws Exception;
	
	/** Dashboard - (DBA) 긴급 조치 대상 리스트 */
	List<DbEmergencyAction> urgentActionList(DbEmergencyAction dbEmergencyAction) throws Exception;
	
	/** Dashboard - (DBA) 긴급 조치 대상 - 조치완료 처리 */
	int updateUrgentAction(DbEmergencyAction dbEmergencyAction) throws Exception;
	
	/** Dashboard - (튜너) 튜닝 작업대기 리스트 */
	List<TuningTargetSql> tuningWaitJobList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (튜너) 튜닝 진행 리스트 */
	List<TuningTargetSql> tuningProgressList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (튜너) 튜닝 지연예상 리스트 */
	List<TuningTargetSql> tuningExpectedDelayList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (튜너,DBA) 튜닝 지연 리스트 */
	List<TuningTargetSql> tuningDelayList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (튜너) 튜닝 완료 리스트 */
	List<TuningTargetSql> tuningCompleteList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (튜너) 튜닝 실적 차트 */
	List<TuningTargetSql> tuningPerformanceChartList(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** Dashboard - (개발자) My WorkList */
	List<TuningTargetSql> myWorkList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard - (개발자) 사례/가이드 조회 */
	List<PerfGuide> precedentList(PerfGuide perfGuide) throws Exception;
	
	/** Dashboard - (개발자) 형상기반 SQL 표준 점검 */
	List<LinkedHashMap<String, Object>> loadScmBasedStdQtyChk(Map<String, String> param) throws Exception;
	
	/** Main 메뉴 조회 */
	List<Menu> menuList(String auth_cd);
	
	/** 공지사항 조회*/
	Board noticeBoardOne();

	/** Licence 조회*/
	List<Licence> licenseInquiry(Licence licence) throws Exception;

	/** LicenceCPU 초과 조회*/
	List<Licence> getLicenseExceeded(Licence licence) throws Exception;

	/** LicenceCPU 미셋팅 조회*/
	List<Licence> getNoLicense(Licence licence) throws Exception;

	/** LicencePopup 일주일 닫기*/
	int updateCloseLicensePopupForWeek(Licence licence) throws Exception;

	/** DashBoard 튜너 튜닝작업대기 COUNT 조회 */
	int tuningWaitJobCnt(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** DashBoard 튜너 튜닝진행 COUNT 조회 */
	int tuningProgressCnt(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard 튜너 튜닝지연예상 COUNT 조회 */
	int tuningExpectedDelayCnt(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard 튜너 튜닝지연 COUNT 조회 */
	int tuningDelayCnt(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** Dashboard 튜너 주간튜닝완료 COUNT 조회 */
	int tuningCompleteCnt(TuningTargetSql tuningTargetSql) throws Exception;
}
