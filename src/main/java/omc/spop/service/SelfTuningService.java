package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SelfTuningService {
	/** 데이터베이스 리스트 of wrkjob_cd */
	List<ApmApplSql> databaseListOfWrkjobCd(ApmApplSql apmApplSql) throws Exception;
	
	/** 셀프튜닝 리스트 */
	List<ApmApplSql> selfTuningList(ApmApplSql apmApplSql) throws Exception;

	/** 셀프튜닝 INSERT */
	String insertSelftunQuery(ApmApplSql apmApplSql) throws Exception;

	/** 셀프튜닝 - 인덱스자동설계 */
	List<IdxAdMst> startSelfIndexAutoDesign(ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception;

	/** 셀프튜닝 - 인덱스자동설계 엑셀 */
	List<IdxAdMst> idxAdMstList(IdxAdMst idxAdMst) throws Exception;
	
	/** 셀프튜닝 - 인덱스자동설계 엑셀 */
	List<LinkedHashMap<String, Object>> excelDownloadIdxAdMstList(IdxAdMst idxAdMst) throws Exception;

	/** 셀프튜닝 - 셀프테스트 */
	Result startSelfTest(HttpServletRequest req, ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception;

	Result startSelfTestNew(HttpServletRequest req, ApmApplSql apmApplSql, String selftunQuerySeq) throws Exception;

	/** 셀프튜닝 - Explain Plan */
	List<SelftunPlanTable> explainPlanTreeList(ApmApplSql apmApplSql) throws Exception;

	/** 셀프튜닝 - Parsing Schema Name 조회 */
	List<DbaUsers> parsingSchemaNameList(ApmApplSql apmApplSql) throws Exception;

	/** 셀프튜닝 - Explain Plan Detail Popup 조회 */
	Result explainPlanDetailPopup(DbaTables dbaTables) throws Exception;

	/** 셀프튜닝 - Explain Plan Detail Popup 2 - access_path 조회 */
	List<AccPathExec> accpathList(DbaTables dbaTables) throws Exception;

	/** 셀프튜닝 - Explain Plan Detail Popup 2 - columnHistory 조회 */
	List<DbTabColumnHistory> columnHistoryList(DbaTables dbaTables) throws Exception;

	/** 셀프튜닝 - Explain Plan Detail Popup 2 - indexHistory 조회 */
	List<DbIndexHistory> indexHistoryList(DbaTables dbaTables) throws Exception;

	/** 셀프튜닝 - Explain Plan Statistics Popup 조회 */
	List<DbaObjects> explainPlanStatisticsPopup(DbaObjects dbaObjects) throws Exception;

	/** 셀프튜닝 - 수행 Sql Info Popup 조회 */
	Result selfTuningSqlInfoPopup(SelftunSql selftunSql) throws Exception;

	List<ApmApplSql> selectPerfCheckResultList(ApmApplSql apmApplSql);

	List<PerformanceCheckMng> selectDeployPerfChkDetailResultList(SelftunExecutionPlan selftunExecutionPlan);

	List<PerformanceCheckMng> selectImprovementGuideList(SelftunExecutionPlan selftunExecutionPlan);

	List<PerformanceCheckMng> selectPerfCheckResultBasisWhy(SelftunExecutionPlan selftunExecutionPlan);
	
	List<SelftunExecutionPlan1> executionPlan1(SelftunExecutionPlan1 selftunExecutionPlan1);
}
